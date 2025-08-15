"""
Comprehensive Accounting System Integration Module

This module provides enterprise-grade accounting system integration with support for:
- Sage Accounting export with proper journal entry format
- Ciel Accounting export with compatible data format  
- Generic accounting export with configurable formats
- UNL format export (58-field pipe-delimited)
- Chart of accounts management and validation
- Balance validation and reconciliation
- Multiple export formats (CSV, TXT, XML, UNL)

Integrates with the existing accounting_integration models (MasterPiece, DetailPiece)
and provides comprehensive validation and error handling.
"""

import csv
import json
import xml.etree.ElementTree as ET
from datetime import datetime, timedelta
from decimal import Decimal, ROUND_HALF_UP
from pathlib import Path
from typing import Dict, List, Optional, Tuple, Union, Any
from dataclasses import dataclass, field
from enum import Enum
import re
import logging

from django.conf import settings
from django.db import models, transaction
from django.db.models import Q, Sum, Count
from django.core.exceptions import ValidationError
from django.utils import timezone

from ..models.accounting_integration import MasterPiece, DetailPiece, ExportFormat, AccountGenerator
from ..models.payroll_processing import Payroll
from ..models.employee import Employee
from ..models.organizational import Bank
from ..models.reference import PayrollMotif
from ..models.payroll_elements import PayrollElement
from ..utils.validators import validate_decimal_precision
from ..utils.date_utils import format_date_for_export


# Configure logging
logger = logging.getLogger(__name__)


class AccountingSystemType(Enum):
    """Supported accounting system types"""
    SAGE = "SAGE"
    CIEL = "CIEL" 
    GENERIC = "GENERIC"
    UNL = "UNL"


class ExportStatus(Enum):
    """Export processing status"""
    PENDING = "PENDING"
    IN_PROGRESS = "IN_PROGRESS"
    COMPLETED = "COMPLETED"
    FAILED = "FAILED"
    CANCELLED = "CANCELLED"


@dataclass
class AccountMapping:
    """Chart of accounts mapping configuration"""
    account_code: str
    account_name: str
    account_type: str
    debit_account: bool = True
    chapter_code: str = ""
    description: str = ""
    validation_rules: Dict[str, Any] = field(default_factory=dict)
    
    def __post_init__(self):
        """Validate account mapping on creation"""
        if not AccountGenerator.validate_account_format(self.account_code):
            raise ValidationError(f"Invalid account code format: {self.account_code}")


@dataclass 
class ExportConfiguration:
    """Export configuration for different accounting systems"""
    system_type: AccountingSystemType
    file_extension: str
    delimiter: str = "|"
    date_format: str = "dd/MM/yyyy"
    currency_code: str = "UM"
    agency_code: str = ""
    operation_code: str = ""
    service_code: str = ""
    encoding: str = "utf-8"
    header_required: bool = False
    footer_required: bool = False
    field_count: int = 0
    custom_fields: Dict[str, Any] = field(default_factory=dict)


@dataclass
class ValidationResult:
    """Result of export validation"""
    is_valid: bool
    errors: List[str] = field(default_factory=list)
    warnings: List[str] = field(default_factory=list)
    balance_difference: Decimal = Decimal('0.00')
    total_debit: Decimal = Decimal('0.00')
    total_credit: Decimal = Decimal('0.00')
    
    def add_error(self, error: str):
        """Add validation error"""
        self.errors.append(error)
        self.is_valid = False
    
    def add_warning(self, warning: str):
        """Add validation warning"""
        self.warnings.append(warning)


class ChartOfAccountsManager:
    """
    Chart of accounts management and validation
    Handles dynamic account mapping and validation rules
    """
    
    def __init__(self):
        self.account_mappings: Dict[str, AccountMapping] = {}
        self.account_hierarchy: Dict[str, List[str]] = {}
        self.validation_rules: Dict[str, callable] = {}
        self._load_default_mappings()
    
    def _load_default_mappings(self):
        """Load default account mappings for common payroll accounts"""
        default_mappings = {
            # Employee accounts
            'EMPLOYEE_CASH': AccountMapping(
                account_code='307{employee_id:04d}',
                account_name='Employee Cash Payment',
                account_type='CASH',
                debit_account=False,
                description='Cash payment to employee'
            ),
            'EMPLOYEE_ENGAGEMENT': AccountMapping(
                account_code='511{employee_id:04d}',
                account_name='Employee Engagement',
                account_type='ENGAGEMENT',
                debit_account=False,
                description='Employee engagement account'
            ),
            
            # Statutory accounts
            'ITS_LIABILITY': AccountMapping(
                account_code='4421',
                account_name='ITS Payable',
                account_type='STATUTORY',
                debit_account=False,
                description='Income tax liability'
            ),
            'CNSS_LIABILITY': AccountMapping(
                account_code='4311',
                account_name='CNSS Employee Contributions',
                account_type='STATUTORY', 
                debit_account=False,
                description='CNSS employee contributions payable'
            ),
            'CNAM_LIABILITY': AccountMapping(
                account_code='4312',
                account_name='CNAM Employee Contributions',
                account_type='STATUTORY',
                debit_account=False,
                description='CNAM employee contributions payable'
            ),
            
            # Payroll expense accounts
            'SALARY_EXPENSE': AccountMapping(
                account_code='6411',
                account_name='Salary Expense',
                account_type='EXPENSE',
                debit_account=True,
                description='Employee salary expense'
            ),
            'BONUS_EXPENSE': AccountMapping(
                account_code='6412',
                account_name='Bonus Expense', 
                account_type='EXPENSE',
                debit_account=True,
                description='Employee bonus expense'
            ),
            'OVERTIME_EXPENSE': AccountMapping(
                account_code='6413',
                account_name='Overtime Expense',
                account_type='EXPENSE',
                debit_account=True,
                description='Overtime pay expense'
            ),
        }
        
        for key, mapping in default_mappings.items():
            self.add_account_mapping(key, mapping)
    
    def add_account_mapping(self, key: str, mapping: AccountMapping):
        """Add account mapping to chart of accounts"""
        self.account_mappings[key] = mapping
        logger.debug(f"Added account mapping: {key} -> {mapping.account_code}")
    
    def get_account_mapping(self, key: str) -> Optional[AccountMapping]:
        """Get account mapping by key"""
        return self.account_mappings.get(key)
    
    def get_employee_account(self, account_type: str, employee_id: int) -> str:
        """Generate employee-specific account number"""
        if account_type == 'CASH':
            return AccountGenerator.employee_cash_account(employee_id)
        elif account_type == 'ENGAGEMENT':
            return AccountGenerator.employee_engagement_account(employee_id)
        else:
            raise ValueError(f"Unknown employee account type: {account_type}")
    
    def validate_account_code(self, account_code: str) -> ValidationResult:
        """Validate account code format and existence"""
        result = ValidationResult(is_valid=True)
        
        if not account_code:
            result.add_error("Account code cannot be empty")
            return result
        
        if not AccountGenerator.validate_account_format(account_code):
            result.add_error(f"Invalid account code format: {account_code}")
        
        # Additional validation rules can be added here
        if len(account_code) < 3:
            result.add_warning(f"Account code may be too short: {account_code}")
        
        return result
    
    def get_account_hierarchy(self, account_code: str) -> List[str]:
        """Get account hierarchy for given account code"""
        return self.account_hierarchy.get(account_code, [])


class JournalEntryGenerator:
    """
    Generates journal entries from payroll data
    Handles automatic journal entry creation with validation
    """
    
    def __init__(self, chart_manager: ChartOfAccountsManager):
        self.chart_manager = chart_manager
        self.validation_tolerance = Decimal('0.01')
    
    def generate_payroll_entries(
        self,
        period: str,
        motif_name: str,
        user_name: str,
        custom_config: Optional[Dict] = None
    ) -> Tuple[MasterPiece, List[DetailPiece]]:
        """
        Generate complete journal entries for payroll period
        Returns master piece and list of detail pieces
        """
        
        logger.info(f"Generating journal entries for period {period}, motif {motif_name}")
        
        with transaction.atomic():
            # Create master piece
            master_piece = self._create_master_piece(period, motif_name, user_name)
            
            # Generate detail pieces
            detail_pieces = []
            
            # 1. Payroll rubrique entries (gains)
            rubrique_details = self._generate_rubrique_entries(master_piece, period, motif_name, sens='G')
            detail_pieces.extend(rubrique_details)
            
            # 2. Bank transfer entries
            bank_details = self._generate_bank_transfer_entries(master_piece, period, motif_name)
            detail_pieces.extend(bank_details)
            
            # 3. Cash payment entries
            cash_details = self._generate_cash_payment_entries(master_piece, period, motif_name) 
            detail_pieces.extend(cash_details)
            
            # 4. Employee engagement entries
            engagement_details = self._generate_engagement_entries(master_piece, period, motif_name)
            detail_pieces.extend(engagement_details)
            
            # 5. Payroll deduction entries (retenues)
            deduction_details = self._generate_rubrique_entries(master_piece, period, motif_name, sens='R')
            detail_pieces.extend(deduction_details)
            
            # 6. Statutory liability entries
            statutory_details = self._generate_statutory_entries(master_piece, period, motif_name)
            detail_pieces.extend(statutory_details)
            
            # Recalculate and validate totals
            master_piece.recalculate_totals()
            validation = self._validate_journal_balance(master_piece, detail_pieces)
            
            if not validation.is_valid:
                raise ValidationError(f"Journal entries not balanced: {validation.errors}")
            
            logger.info(f"Generated {len(detail_pieces)} journal entries with balanced totals")
            return master_piece, detail_pieces
    
    def _create_master_piece(self, period: str, motif_name: str, user_name: str) -> MasterPiece:
        """Create master piece header"""
        numero = self._generate_piece_number(period, motif_name)
        
        master_piece = MasterPiece.objects.create(
            numero=numero,
            libelle_service="Service Paie",
            dateop=timezone.now().date(),
            rubrique=motif_name,
            beneficiaire="-",
            initiateur=user_name,
            init_hr=timezone.now(),
            period=period,
            motif=motif_name,
            status='DRAFT'
        )
        
        logger.debug(f"Created master piece: {numero}")
        return master_piece
    
    def _generate_piece_number(self, period: str, motif_name: str) -> str:
        """Generate unique piece number"""
        timestamp = timezone.now().strftime("%Y%m%d%H%M%S")
        period_code = period.replace("-", "")
        motif_code = re.sub(r'[^A-Z0-9]', '', motif_name.upper())[:4]
        return f"PC{period_code}{motif_code}{timestamp[-6:]}"
    
    def _generate_rubrique_entries(
        self, 
        master_piece: MasterPiece, 
        period: str, 
        motif_name: str,
        sens: str
    ) -> List[DetailPiece]:
        """Generate journal entries for payroll rubriques"""
        
        detail_pieces = []
        
        # Query payroll elements aggregated by rubrique
        payroll_query = Payroll.objects.filter(
            period__year=int(period.split('-')[0]),
            period__month=int(period.split('-')[1]),
            motif__name=motif_name
        )
        
        # Group by payroll element and sum amounts
        # This is a simplified version - actual implementation would need
        # to handle the relationship between Payroll and PayrollElement properly
        
        logger.debug(f"Generating {sens} rubrique entries for {payroll_query.count()} payroll records")
        
        # Placeholder for rubrique aggregation logic
        # In practice, this would aggregate payroll line items by rubrique
        rubrique_totals = {}
        
        for piece_data in rubrique_totals.items():
            rubrique_code, amount = piece_data
            if amount > 0:
                detail_piece = DetailPiece.objects.create(
                    nupiece=master_piece,
                    dateop=master_piece.dateop,
                    journal="PAI",
                    compte=rubrique_code,
                    libelle=f"Payroll {sens} - {motif_name}",
                    intitulet=f"Rubrique {rubrique_code}",
                    montant=amount,
                    sens='D' if sens == 'G' else 'C',
                    account_type='RUBRIQUE'
                )
                detail_pieces.append(detail_piece)
        
        return detail_pieces
    
    def _generate_bank_transfer_entries(
        self, 
        master_piece: MasterPiece, 
        period: str, 
        motif_name: str
    ) -> List[DetailPiece]:
        """Generate bank transfer entries grouped by bank"""
        
        detail_pieces = []
        
        # Get bank transfers aggregated by bank
        bank_transfers = Payroll.objects.filter(
            period__year=int(period.split('-')[0]),
            period__month=int(period.split('-')[1]),
            motif__name=motif_name,
            payment_mode="Virement"
        ).values('bank_name').annotate(
            total_amount=Sum('net_salary')
        ).exclude(total_amount=0)
        
        for transfer in bank_transfers:
            bank_name = transfer['bank_name']
            amount = transfer['total_amount']
            
            # Get bank account information
            try:
                bank = Bank.objects.get(nom=bank_name)
                account_code = getattr(bank, 'no_compte_compta', f"BANK_{bank.id}")
            except Bank.DoesNotExist:
                account_code = f"BANK_{bank_name.upper()}"
                logger.warning(f"Bank not found: {bank_name}, using default account")
            
            detail_piece = DetailPiece.objects.create(
                nupiece=master_piece,
                dateop=master_piece.dateop,
                journal="PAI",
                compte=account_code,
                libelle=f"Virement {bank_name} ({motif_name})",
                intitulet=bank_name,
                montant=amount,
                sens='C',
                account_type='BANK'
            )
            detail_pieces.append(detail_piece)
        
        return detail_pieces
    
    def _generate_cash_payment_entries(
        self, 
        master_piece: MasterPiece, 
        period: str, 
        motif_name: str
    ) -> List[DetailPiece]:
        """Generate individual cash payment entries"""
        
        detail_pieces = []
        
        # Get cash payments (non-bank transfers)
        cash_payments = Payroll.objects.filter(
            period__year=int(period.split('-')[0]),
            period__month=int(period.split('-')[1]),
            motif__name=motif_name
        ).exclude(
            payment_mode="Virement"
        ).exclude(net_salary=0)
        
        for payroll in cash_payments:
            account_code = self.chart_manager.get_employee_account('CASH', payroll.employee.id)
            
            detail_piece = DetailPiece.objects.create(
                nupiece=master_piece,
                dateop=master_piece.dateop,
                journal="PAI",
                compte=account_code,
                libelle=f"Salaire ({motif_name}) - {payroll.employee.prenom} {payroll.employee.nom}",
                intitulet=f"{payroll.employee.prenom} {payroll.employee.nom}",
                montant=payroll.net_salary,
                sens='C',
                account_type='CASH',
                employee=payroll.employee,
                employee_net_amount=payroll.net_salary
            )
            detail_pieces.append(detail_piece)
        
        return detail_pieces
    
    def _generate_engagement_entries(
        self, 
        master_piece: MasterPiece, 
        period: str, 
        motif_name: str
    ) -> List[DetailPiece]:
        """Generate employee engagement entries"""
        
        detail_pieces = []
        
        # This would handle special engagement rubriques
        # Implementation depends on specific business rules for engagements
        
        logger.debug("Generating engagement entries (placeholder)")
        return detail_pieces
    
    def _generate_statutory_entries(
        self, 
        master_piece: MasterPiece, 
        period: str, 
        motif_name: str
    ) -> List[DetailPiece]:
        """Generate statutory liability entries (ITS, CNSS, CNAM)"""
        
        detail_pieces = []
        
        # Get statutory totals
        statutory_totals = Payroll.objects.filter(
            period__year=int(period.split('-')[0]),
            period__month=int(period.split('-')[1]),
            motif__name=motif_name
        ).aggregate(
            total_its=Sum('its_total'),
            total_cnss=Sum('cnss_employee'),
            total_cnam=Sum('cnam_employee')
        )
        
        # ITS entry
        if statutory_totals['total_its'] and statutory_totals['total_its'] > 0:
            detail_piece = DetailPiece.objects.create(
                nupiece=master_piece,
                dateop=master_piece.dateop,
                journal="PAI",
                compte="4421",  # ITS liability account
                libelle=f"ITS ({motif_name})",
                intitulet="Impôt sur Traitement et Salaire",
                montant=statutory_totals['total_its'],
                sens='C',
                account_type='STATUTORY'
            )
            detail_pieces.append(detail_piece)
        
        # CNSS entry
        if statutory_totals['total_cnss'] and statutory_totals['total_cnss'] > 0:
            detail_piece = DetailPiece.objects.create(
                nupiece=master_piece,
                dateop=master_piece.dateop,
                journal="PAI",
                compte="4311",  # CNSS liability account
                libelle=f"CNSS Employés ({motif_name})",
                intitulet="CNSS Contributions Employés",
                montant=statutory_totals['total_cnss'],
                sens='C',
                account_type='STATUTORY'
            )
            detail_pieces.append(detail_piece)
        
        # CNAM entry
        if statutory_totals['total_cnam'] and statutory_totals['total_cnam'] > 0:
            detail_piece = DetailPiece.objects.create(
                nupiece=master_piece,
                dateop=master_piece.dateop,
                journal="PAI",
                compte="4312",  # CNAM liability account
                libelle=f"CNAM Employés ({motif_name})",
                intitulet="CNAM Contributions Employés",
                montant=statutory_totals['total_cnam'],
                sens='C',
                account_type='STATUTORY'
            )
            detail_pieces.append(detail_piece)
        
        return detail_pieces
    
    def _validate_journal_balance(
        self, 
        master_piece: MasterPiece, 
        detail_pieces: List[DetailPiece]
    ) -> ValidationResult:
        """Validate that journal entries are balanced"""
        
        result = ValidationResult(is_valid=True)
        
        total_debit = sum(
            piece.montant for piece in detail_pieces if piece.sens == 'D'
        )
        total_credit = sum(
            piece.montant for piece in detail_pieces if piece.sens == 'C'
        )
        
        result.total_debit = total_debit
        result.total_credit = total_credit
        result.balance_difference = total_debit - total_credit
        
        if abs(result.balance_difference) > self.validation_tolerance:
            result.add_error(
                f"Journal entries not balanced: "
                f"Debit {total_debit} != Credit {total_credit} "
                f"(Difference: {result.balance_difference})"
            )
        
        # Additional validation rules
        if total_debit == 0 and total_credit == 0:
            result.add_error("No journal entries generated")
        
        if len(detail_pieces) == 0:
            result.add_error("No detail pieces generated")
        
        return result


class SageAccountingExporter:
    """
    Sage Accounting export functionality
    Handles Sage-specific journal entry format and account mapping
    """
    
    def __init__(self, chart_manager: ChartOfAccountsManager):
        self.chart_manager = chart_manager
        self.config = ExportConfiguration(
            system_type=AccountingSystemType.SAGE,
            file_extension=".csv",
            delimiter=",",
            date_format="dd/MM/yyyy",
            header_required=True
        )
    
    def export_journal_entries(
        self,
        master_piece: MasterPiece,
        output_path: str,
        export_config: Optional[ExportConfiguration] = None
    ) -> ValidationResult:
        """Export journal entries in Sage format"""
        
        if export_config:
            self.config = export_config
        
        result = ValidationResult(is_valid=True)
        
        try:
            with open(output_path, 'w', newline='', encoding=self.config.encoding) as csvfile:
                writer = csv.writer(csvfile, delimiter=self.config.delimiter)
                
                # Write header if required
                if self.config.header_required:
                    writer.writerow(self._get_sage_header())
                
                # Write detail entries
                for detail in master_piece.detailpieces.all():
                    row = self._format_sage_entry(detail)
                    writer.writerow(row)
                
                logger.info(f"Exported {master_piece.detailpieces.count()} entries to Sage format: {output_path}")
                
        except Exception as e:
            result.add_error(f"Export failed: {str(e)}")
            logger.error(f"Sage export failed: {e}")
        
        return result
    
    def _get_sage_header(self) -> List[str]:
        """Get Sage CSV header columns"""
        return [
            'Journal',
            'Date',
            'Piece',
            'Compte',
            'Libelle', 
            'Debit',
            'Credit',
            'Sens',
            'Reference',
            'Echeance'
        ]
    
    def _format_sage_entry(self, detail: DetailPiece) -> List[str]:
        """Format detail piece for Sage export"""
        
        debit_amount = str(detail.montant) if detail.sens == 'D' else '0.00'
        credit_amount = str(detail.montant) if detail.sens == 'C' else '0.00'
        
        return [
            detail.journal,
            detail.dateop.strftime('%d/%m/%Y'),
            detail.nupiece.numero,
            detail.compte,
            detail.libelle,
            debit_amount,
            credit_amount,
            detail.sens,
            detail.nupiece.motif,
            ''  # Echeance
        ]


class CielAccountingExporter:
    """
    Ciel Accounting export functionality  
    Handles Ciel-specific data format and import specifications
    """
    
    def __init__(self, chart_manager: ChartOfAccountsManager):
        self.chart_manager = chart_manager
        self.config = ExportConfiguration(
            system_type=AccountingSystemType.CIEL,
            file_extension=".txt",
            delimiter="\t",
            date_format="ddMMyyyy",
            header_required=False
        )
    
    def export_journal_entries(
        self,
        master_piece: MasterPiece,
        output_path: str,
        export_config: Optional[ExportConfiguration] = None
    ) -> ValidationResult:
        """Export journal entries in Ciel format"""
        
        if export_config:
            self.config = export_config
        
        result = ValidationResult(is_valid=True)
        
        try:
            with open(output_path, 'w', encoding=self.config.encoding) as file:
                for detail in master_piece.detailpieces.all():
                    line = self._format_ciel_entry(detail)
                    file.write(line + '\n')
                
                logger.info(f"Exported {master_piece.detailpieces.count()} entries to Ciel format: {output_path}")
                
        except Exception as e:
            result.add_error(f"Export failed: {str(e)}")
            logger.error(f"Ciel export failed: {e}")
        
        return result
    
    def _format_ciel_entry(self, detail: DetailPiece) -> str:
        """Format detail piece for Ciel export"""
        
        # Ciel specific format: DATE|JOURNAL|COMPTE|LIBELLE|DEBIT|CREDIT
        fields = [
            detail.dateop.strftime('%d%m%Y'),
            detail.journal,
            detail.compte,
            detail.libelle[:30],  # Limit libelle length
            str(detail.montant) if detail.sens == 'D' else '0.00',
            str(detail.montant) if detail.sens == 'C' else '0.00'
        ]
        
        return self.config.delimiter.join(fields)


class UNLExporter:
    """
    UNL format exporter (58-field pipe-delimited)
    Legacy format for external accounting system integration
    """
    
    def __init__(self, chart_manager: ChartOfAccountsManager):
        self.chart_manager = chart_manager
        self.config = ExportConfiguration(
            system_type=AccountingSystemType.UNL,
            file_extension=".unl",
            delimiter="|",
            date_format="dd/MM/yyyy",
            field_count=58
        )
    
    def export_journal_entries(
        self,
        master_piece: MasterPiece,
        output_path: str,
        export_params: Dict[str, str],
        export_config: Optional[ExportConfiguration] = None
    ) -> ValidationResult:
        """Export journal entries in UNL format (58 fields)"""
        
        if export_config:
            self.config = export_config
        
        result = ValidationResult(is_valid=True)
        
        try:
            with open(output_path, 'w', encoding=self.config.encoding) as file:
                for detail in master_piece.detailpieces.all():
                    line = self._format_unl_entry(detail, export_params)
                    file.write(line + '\n')
                
                logger.info(f"Exported {master_piece.detailpieces.count()} entries to UNL format: {output_path}")
                
        except Exception as e:
            result.add_error(f"Export failed: {str(e)}")
            logger.error(f"UNL export failed: {e}")
        
        return result
    
    def _format_unl_entry(self, detail: DetailPiece, export_params: Dict[str, str]) -> str:
        """Format detail piece for UNL export (58 fields)"""
        
        # Initialize 58 fields array
        fields = [''] * 58
        
        # Map fields according to UNL specification
        fields[0] = export_params.get('CODE_AGENCE', '')
        fields[1] = export_params.get('CODE_DEVISE', 'UM')
        fields[2] = detail.chapitre or ''
        fields[3] = detail.compte
        fields[5] = export_params.get('CODE_OPERATION', '')
        fields[11] = detail.dateop.strftime(self._get_date_format(export_params.get('DATE_FORMAT', 'dd/MM/yyyy')))
        fields[12] = export_params.get('CODE_SERVICE', '')
        fields[13] = detail.dateop.strftime(self._get_date_format(export_params.get('DATE_FORMAT', 'dd/MM/yyyy')))
        fields[14] = str(detail.montant)
        fields[15] = detail.sens
        fields[16] = detail.libelle
        fields[18] = self._generate_piece_number(detail.nupiece.period, export_params)
        
        return self.config.delimiter.join(fields)
    
    def _get_date_format(self, format_string: str) -> str:
        """Convert date format string to Python strftime format"""
        format_map = {
            'dd/MM/yyyy': '%d/%m/%Y',
            'dd/MM/yy': '%d/%m/%y',
            'ddMMyyyy': '%d%m%Y',
            'ddMMyy': '%d%m%y',
            'yyyy-MM-dd': '%Y-%m-%d'
        }
        return format_map.get(format_string, '%d/%m/%Y')
    
    def _generate_piece_number(self, period: str, export_params: Dict[str, str]) -> str:
        """Generate UNL piece number"""
        period_date = datetime.strptime(period, '%Y-%m')
        return f"EP{period_date.strftime('%m%Y')}"


class GenericAccountingExporter:
    """
    Generic accounting export with configurable formats
    Supports multiple output formats (CSV, TXT, XML)
    """
    
    def __init__(self, chart_manager: ChartOfAccountsManager):
        self.chart_manager = chart_manager
    
    def export_journal_entries(
        self,
        master_piece: MasterPiece,
        output_path: str,
        format_type: str = 'CSV',
        export_config: Optional[ExportConfiguration] = None
    ) -> ValidationResult:
        """Export journal entries in generic format"""
        
        format_type = format_type.upper()
        
        if format_type == 'CSV':
            return self._export_csv(master_piece, output_path, export_config)
        elif format_type == 'XML':
            return self._export_xml(master_piece, output_path, export_config)
        elif format_type == 'TXT':
            return self._export_txt(master_piece, output_path, export_config)
        else:
            result = ValidationResult(is_valid=False)
            result.add_error(f"Unsupported format type: {format_type}")
            return result
    
    def _export_csv(
        self,
        master_piece: MasterPiece, 
        output_path: str,
        export_config: Optional[ExportConfiguration]
    ) -> ValidationResult:
        """Export as CSV format"""
        
        result = ValidationResult(is_valid=True)
        
        try:
            with open(output_path, 'w', newline='', encoding='utf-8') as csvfile:
                writer = csv.writer(csvfile)
                
                # Header
                writer.writerow([
                    'Piece_Number', 'Date', 'Journal', 'Account', 'Description',
                    'Debit', 'Credit', 'Direction', 'Employee_ID', 'Account_Type'
                ])
                
                # Data rows
                for detail in master_piece.detailpieces.all():
                    writer.writerow([
                        detail.nupiece.numero,
                        detail.dateop.strftime('%Y-%m-%d'),
                        detail.journal,
                        detail.compte,
                        detail.libelle,
                        str(detail.montant) if detail.sens == 'D' else '0.00',
                        str(detail.montant) if detail.sens == 'C' else '0.00',
                        detail.sens,
                        detail.employee.id if detail.employee else '',
                        detail.account_type
                    ])
                
                logger.info(f"Exported CSV: {output_path}")
                
        except Exception as e:
            result.add_error(f"CSV export failed: {str(e)}")
        
        return result
    
    def _export_xml(
        self,
        master_piece: MasterPiece,
        output_path: str, 
        export_config: Optional[ExportConfiguration]
    ) -> ValidationResult:
        """Export as XML format"""
        
        result = ValidationResult(is_valid=True)
        
        try:
            root = ET.Element('JournalEntries')
            master_elem = ET.SubElement(root, 'MasterPiece')
            master_elem.set('numero', master_piece.numero)
            master_elem.set('date', master_piece.dateop.strftime('%Y-%m-%d'))
            master_elem.set('period', master_piece.period)
            master_elem.set('motif', master_piece.motif)
            
            for detail in master_piece.detailpieces.all():
                detail_elem = ET.SubElement(master_elem, 'DetailPiece')
                detail_elem.set('account', detail.compte)
                detail_elem.set('amount', str(detail.montant))
                detail_elem.set('direction', detail.sens)
                detail_elem.set('description', detail.libelle)
                detail_elem.set('type', detail.account_type)
                
                if detail.employee:
                    detail_elem.set('employee_id', str(detail.employee.id))
            
            tree = ET.ElementTree(root)
            tree.write(output_path, encoding='utf-8', xml_declaration=True)
            
            logger.info(f"Exported XML: {output_path}")
            
        except Exception as e:
            result.add_error(f"XML export failed: {str(e)}")
        
        return result
    
    def _export_txt(
        self,
        master_piece: MasterPiece,
        output_path: str,
        export_config: Optional[ExportConfiguration]
    ) -> ValidationResult:
        """Export as TXT format"""
        
        result = ValidationResult(is_valid=True)
        
        try:
            with open(output_path, 'w', encoding='utf-8') as file:
                # Header
                file.write(f"Journal Entries - Piece: {master_piece.numero}\n")
                file.write(f"Date: {master_piece.dateop}\n")
                file.write(f"Period: {master_piece.period}\n")
                file.write(f"Motif: {master_piece.motif}\n")
                file.write("-" * 80 + "\n")
                
                # Detail entries
                total_debit = Decimal('0.00')
                total_credit = Decimal('0.00')
                
                for detail in master_piece.detailpieces.all():
                    direction = "Debit " if detail.sens == 'D' else "Credit"
                    file.write(f"{detail.compte:<15} {direction:<8} {detail.montant:>12.2f} {detail.libelle}\n")
                    
                    if detail.sens == 'D':
                        total_debit += detail.montant
                    else:
                        total_credit += detail.montant
                
                # Summary
                file.write("-" * 80 + "\n")
                file.write(f"Total Debit:  {total_debit:>12.2f}\n")
                file.write(f"Total Credit: {total_credit:>12.2f}\n")
                file.write(f"Balance:      {total_debit - total_credit:>12.2f}\n")
                
                logger.info(f"Exported TXT: {output_path}")
                
        except Exception as e:
            result.add_error(f"TXT export failed: {str(e)}")
        
        return result


class AccountingExportService:
    """
    Main service class for comprehensive accounting system integration
    Orchestrates journal generation, validation, and export processes
    """
    
    def __init__(self):
        self.chart_manager = ChartOfAccountsManager()
        self.journal_generator = JournalEntryGenerator(self.chart_manager)
        self.sage_exporter = SageAccountingExporter(self.chart_manager)
        self.ciel_exporter = CielAccountingExporter(self.chart_manager)
        self.unl_exporter = UNLExporter(self.chart_manager)
        self.generic_exporter = GenericAccountingExporter(self.chart_manager)
        
        # Export status tracking
        self.export_status: Dict[str, ExportStatus] = {}
    
    def generate_and_export_payroll_accounting(
        self,
        period: str,
        motif_name: str,
        user_name: str,
        export_systems: List[AccountingSystemType],
        output_directory: str,
        export_params: Optional[Dict[str, Any]] = None
    ) -> Dict[str, ValidationResult]:
        """
        Complete payroll accounting workflow:
        1. Generate journal entries
        2. Validate balances
        3. Export to specified accounting systems
        """
        
        results = {}
        export_params = export_params or {}
        
        try:
            logger.info(f"Starting accounting export for period {period}, motif {motif_name}")
            
            # Generate journal entries
            master_piece, detail_pieces = self.journal_generator.generate_payroll_entries(
                period, motif_name, user_name, export_params.get('custom_config')
            )
            
            # Create output directory
            output_path = Path(output_directory)
            output_path.mkdir(parents=True, exist_ok=True)
            
            # Export to each requested system
            for system_type in export_systems:
                export_result = self._export_to_system(
                    system_type, master_piece, output_path, export_params
                )
                results[system_type.value] = export_result
                
                if export_result.is_valid:
                    logger.info(f"Successfully exported to {system_type.value}")
                else:
                    logger.error(f"Export to {system_type.value} failed: {export_result.errors}")
            
            # Mark master piece as exported if all exports successful
            if all(result.is_valid for result in results.values()):
                master_piece.mark_as_exported()
                logger.info(f"Master piece {master_piece.numero} marked as exported")
            
        except Exception as e:
            error_result = ValidationResult(is_valid=False)
            error_result.add_error(f"Accounting export failed: {str(e)}")
            results['ERROR'] = error_result
            logger.error(f"Accounting export failed: {e}")
        
        return results
    
    def _export_to_system(
        self,
        system_type: AccountingSystemType,
        master_piece: MasterPiece,
        output_path: Path,
        export_params: Dict[str, Any]
    ) -> ValidationResult:
        """Export to specific accounting system"""
        
        period_code = master_piece.period.replace('-', '')
        motif_code = re.sub(r'[^A-Z0-9]', '', master_piece.motif.upper())
        
        if system_type == AccountingSystemType.SAGE:
            filename = f"SAGE_Payroll_{period_code}_{motif_code}.csv"
            file_path = output_path / filename
            return self.sage_exporter.export_journal_entries(
                master_piece, str(file_path), export_params.get('sage_config')
            )
            
        elif system_type == AccountingSystemType.CIEL:
            filename = f"CIEL_Payroll_{period_code}_{motif_code}.txt"
            file_path = output_path / filename
            return self.ciel_exporter.export_journal_entries(
                master_piece, str(file_path), export_params.get('ciel_config')
            )
            
        elif system_type == AccountingSystemType.UNL:
            filename = f"Payroll_{period_code}_{motif_code}.unl"
            file_path = output_path / filename
            return self.unl_exporter.export_journal_entries(
                master_piece, str(file_path), export_params.get('unl_params', {}), 
                export_params.get('unl_config')
            )
            
        elif system_type == AccountingSystemType.GENERIC:
            format_type = export_params.get('generic_format', 'CSV')
            extension = '.csv' if format_type == 'CSV' else '.xml' if format_type == 'XML' else '.txt'
            filename = f"Generic_Payroll_{period_code}_{motif_code}{extension}"
            file_path = output_path / filename
            return self.generic_exporter.export_journal_entries(
                master_piece, str(file_path), format_type, export_params.get('generic_config')
            )
        
        else:
            result = ValidationResult(is_valid=False)
            result.add_error(f"Unsupported accounting system: {system_type}")
            return result
    
    def validate_chart_of_accounts(self, account_mappings: List[str]) -> ValidationResult:
        """Validate chart of accounts configuration"""
        
        result = ValidationResult(is_valid=True)
        
        for account_key in account_mappings:
            mapping = self.chart_manager.get_account_mapping(account_key)
            if not mapping:
                result.add_error(f"Missing account mapping: {account_key}")
                continue
            
            validation = self.chart_manager.validate_account_code(mapping.account_code)
            if not validation.is_valid:
                result.add_error(f"Invalid account {account_key}: {validation.errors}")
            
            result.warnings.extend(validation.warnings)
        
        return result
    
    def reconcile_export_balances(self, master_piece: MasterPiece) -> ValidationResult:
        """Reconcile and validate export balances"""
        
        result = ValidationResult(is_valid=True)
        
        # Recalculate totals
        master_piece.recalculate_totals()
        
        # Check balance
        if not master_piece.is_balanced:
            result.add_error(
                f"Master piece not balanced: "
                f"Debit {master_piece.total_debit} != Credit {master_piece.total_credit}"
            )
        
        result.total_debit = master_piece.total_debit
        result.total_credit = master_piece.total_credit
        result.balance_difference = master_piece.balance_difference
        
        # Check for empty entries
        empty_details = master_piece.detailpieces.filter(montant=0)
        if empty_details.exists():
            result.add_warning(f"Found {empty_details.count()} empty detail entries")
        
        # Check for missing accounts
        missing_accounts = master_piece.detailpieces.filter(compte='')
        if missing_accounts.exists():
            result.add_error(f"Found {missing_accounts.count()} entries with missing account codes")
        
        return result
    
    def get_export_summary(self, master_piece: MasterPiece) -> Dict[str, Any]:
        """Get comprehensive export summary"""
        
        detail_pieces = master_piece.detailpieces.all()
        
        summary = {
            'master_piece': {
                'numero': master_piece.numero,
                'period': master_piece.period,
                'motif': master_piece.motif,
                'date': master_piece.dateop,
                'total_debit': float(master_piece.total_debit),
                'total_credit': float(master_piece.total_credit),
                'is_balanced': master_piece.is_balanced,
                'status': master_piece.status
            },
            'detail_summary': {
                'total_entries': detail_pieces.count(),
                'debit_entries': detail_pieces.filter(sens='D').count(),
                'credit_entries': detail_pieces.filter(sens='C').count(),
                'account_types': list(
                    detail_pieces.values_list('account_type', flat=True).distinct()
                ),
                'employee_entries': detail_pieces.filter(employee__isnull=False).count()
            },
            'validation': self.reconcile_export_balances(master_piece).__dict__
        }
        
        return summary


# Utility functions for external access
def create_accounting_export_service() -> AccountingExportService:
    """Factory function to create accounting export service"""
    return AccountingExportService()


def export_payroll_to_sage(
    period: str,
    motif_name: str,
    user_name: str,
    output_directory: str,
    **kwargs
) -> ValidationResult:
    """Convenience function for Sage export"""
    service = create_accounting_export_service()
    results = service.generate_and_export_payroll_accounting(
        period, motif_name, user_name, [AccountingSystemType.SAGE], output_directory, kwargs
    )
    return results.get('SAGE', ValidationResult(is_valid=False))


def export_payroll_to_ciel(
    period: str,
    motif_name: str,
    user_name: str,
    output_directory: str,
    **kwargs
) -> ValidationResult:
    """Convenience function for Ciel export"""
    service = create_accounting_export_service()
    results = service.generate_and_export_payroll_accounting(
        period, motif_name, user_name, [AccountingSystemType.CIEL], output_directory, kwargs
    )
    return results.get('CIEL', ValidationResult(is_valid=False))


def export_payroll_to_unl(
    period: str,
    motif_name: str,
    user_name: str,
    output_directory: str,
    unl_params: Dict[str, str],
    **kwargs
) -> ValidationResult:
    """Convenience function for UNL export"""
    kwargs['unl_params'] = unl_params
    service = create_accounting_export_service()
    results = service.generate_and_export_payroll_accounting(
        period, motif_name, user_name, [AccountingSystemType.UNL], output_directory, kwargs
    )
    return results.get('UNL', ValidationResult(is_valid=False))


# Example usage and testing functions
def demo_accounting_export():
    """Demonstration of accounting export functionality"""
    
    service = create_accounting_export_service()
    
    # Example export to multiple systems
    results = service.generate_and_export_payroll_accounting(
        period="2024-01",
        motif_name="Salaire Janvier",
        user_name="admin",
        export_systems=[
            AccountingSystemType.SAGE,
            AccountingSystemType.CIEL,
            AccountingSystemType.UNL
        ],
        output_directory="/tmp/accounting_exports",
        export_params={
            'unl_params': {
                'CODE_AGENCE': 'AG001',
                'CODE_DEVISE': 'UM',
                'CODE_OPERATION': 'PAIE',
                'CODE_SERVICE': 'SRV001',
                'DATE_FORMAT': 'dd/MM/yyyy'
            }
        }
    )
    
    # Print results
    for system, result in results.items():
        print(f"{system}: {'SUCCESS' if result.is_valid else 'FAILED'}")
        if not result.is_valid:
            print(f"  Errors: {result.errors}")
        if result.warnings:
            print(f"  Warnings: {result.warnings}")


if __name__ == "__main__":
    # Run demo if executed directly
    demo_accounting_export()