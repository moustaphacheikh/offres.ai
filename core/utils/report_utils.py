# report_utils.py
"""
Report generation utilities and data preparation
Converted from Java report generation logic and JasperReports functionality

Supports comprehensive reporting including:
- Payslip generation (bulletinPaie)
- Tax declarations (CNSS, CNAM, ITS, TA)
- Bank transfer reports (virementbank)
- Attendance reports (dTA)
- Accounting exports and statistics
- Multi-format exports (CSV, Excel, XML, UNL, PDF)

Maintains compatibility with Mauritanian tax laws and reporting requirements.
"""

from decimal import Decimal
from datetime import date, datetime
from typing import Dict, List, Any, Union
import json
import xml.etree.ElementTree as ET
from xml.dom import minidom
import csv
import io
from collections import defaultdict
# Cleaned up imports - removed unused timedelta, Optional, Tuple, OrderedDict, calendar
# Optional babel for enhanced formatting - remove if not available
# from babel.numbers import format_currency, format_decimal
# from babel.dates import format_date
import re


class PayslipReportData:
    """
    Payslip report data preparation
    Equivalent to data preparation for bulletinPaie.jrxml
    """
    
    @staticmethod
    def prepare_payslip_data(employee, payroll, line_items, system_params) -> Dict[str, Any]:
        """
        Prepare data for payslip report
        
        Args:
            employee: Employee instance
            payroll: Payroll instance
            line_items: List of PayrollLineItem instances
            system_params: SystemParameters instance
            
        Returns:
            Dictionary with all payslip data
        """
        # Company information
        company_data = {
            'company_name': system_params.company_name,
            'company_activity': system_params.company_activity,
            'company_address': system_params.address,
            'company_phone': system_params.phone,
            'company_logo': system_params.logo,  # Binary data for logo
        }
        
        # Employee information
        employee_data = {
            'employee_id': employee.id,
            'employee_number': employee.id,  # Using ID as employee number
            'first_name': employee.first_name,
            'last_name': employee.last_name,
            'full_name': f"{employee.first_name} {employee.last_name}",
            'position': employee.position.name if employee.position else "",
            'department': employee.department.name if employee.department else "",
            'direction': employee.direction.name if employee.direction else "",
            'general_direction': employee.general_direction.name if employee.general_direction else "",
            'hire_date': employee.hire_date,
            'national_id': employee.national_id,
            'cnss_number': employee.cnss_number,
            'cnam_number': employee.cnam_number,
            'salary_grade': employee.salary_grade.category if employee.salary_grade else "",
        }
        
        # Payroll summary data
        payroll_data = {
            'period': payroll.period,
            'period_text': payroll.period_in_words,
            'category': payroll.category,
            'contract_hours_month': payroll.contract_hours_month,
            'worked_days': payroll.worked_days,
            'overtime_hours': payroll.overtime_hours or Decimal('0.00'),
            
            # Financial totals
            'gross_taxable': payroll.gross_taxable,
            'gross_non_taxable': payroll.gross_non_taxable,
            'net_salary': payroll.net_salary,
            
            # Tax calculations
            'cnss_employee': payroll.cnss_employee,
            'cnam_employee': payroll.cnam_employee,
            'its_total': payroll.its_total,
            'its_tranche1': payroll.its_tranche1,
            'its_tranche2': payroll.its_tranche2,
            'its_tranche3': payroll.its_tranche3,
            
            # Deductions
            'gross_deductions': payroll.gross_deductions,
            'net_deductions': payroll.net_deductions,
            
            # Banking
            'bank_name': payroll.bank_name,
            'bank_account': payroll.bank_account,
            'payment_mode': payroll.payment_mode,
            'is_domiciled': payroll.is_domiciled,
            
            # Display fields
            'net_in_words': payroll.net_in_words,
        }
        
        # Payroll line items (gains and deductions)
        gains = []
        deductions = []
        
        for item in line_items:
            line_data = {
                'code': getattr(item.payroll_element, 'id', ''),
                'label': getattr(item.payroll_element, 'label', getattr(item.payroll_element, 'name', '')),
                'base': getattr(item, 'base_amount', Decimal('0.00')) or Decimal('0.00'),
                'quantity': getattr(item, 'quantity', Decimal('1.00')) or Decimal('1.00'),
                'amount': getattr(item, 'amount', Decimal('0.00')),
                'calculation_order': getattr(item.payroll_element, 'calculation_order', 0),
                'name': getattr(item.payroll_element, 'name', ''),
                'rate': getattr(item, 'rate', Decimal('0.00')),
                'formula_result': getattr(item, 'formula_result', ''),
            }
            
            if getattr(item.payroll_element, 'type', '') == 'G':
                gains.append(line_data)
            else:
                deductions.append(line_data)
        
        return {
            **company_data,
            **employee_data,
            **payroll_data,
            'gains': gains,
            'deductions': deductions,
        }


class DeclarationReportData:
    """
    Tax declaration report data preparation
    Equivalent to data for declarationCNSS.jrxml and declarationCNAM.jrxml
    """
    
    @staticmethod
    def prepare_cnss_declaration_data(employees_data: List[Dict], 
                                    period: date,
                                    system_params) -> Dict[str, Any]:
        """
        Prepare CNSS declaration report data
        
        Args:
            employees_data: List of employee CNSS data
            period: Declaration period
            system_params: System parameters
            
        Returns:
            CNSS declaration data
        """
        # Header information
        header_data = {
            'company_name': system_params.company_name,
            'company_cnss': system_params.cnss_number,
            'period': period,
            'period_text': f"{period.month:02d}/{period.year}",
            'declaration_date': datetime.now().date(),
        }
        
        # Summary calculations
        total_employees = len(employees_data)
        total_days = sum(emp.get('total_days', 0) for emp in employees_data)
        total_salary = sum(emp.get('taxable_salary', Decimal('0')) for emp in employees_data)
        total_contributions = sum(emp.get('cnss_contribution', Decimal('0')) for emp in employees_data)
        
        summary_data = {
            'total_employees': total_employees,
            'total_days': total_days,
            'total_taxable_salary': total_salary,
            'total_contributions': total_contributions,
        }
        
        return {
            **header_data,
            **summary_data,
            'employees': employees_data,
        }
    
    @staticmethod
    def prepare_cnam_declaration_data(employees_data: List[Dict],
                                    period: date,
                                    system_params) -> Dict[str, Any]:
        """
        Prepare CNAM declaration report data
        
        Args:
            employees_data: List of employee CNAM data
            period: Declaration period
            system_params: System parameters
            
        Returns:
            CNAM declaration data
        """
        # Header information
        header_data = {
            'company_name': system_params.company_name,
            'company_cnam': system_params.cnam_number,
            'period': period,
            'period_text': f"{period.month:02d}/{period.year}",
            'declaration_date': datetime.now().date(),
        }
        
        # Summary calculations
        total_employees = len(employees_data)
        total_salary = sum(emp.get('taxable_salary', Decimal('0')) for emp in employees_data)
        total_contributions = sum(emp.get('cnam_contribution', Decimal('0')) for emp in employees_data)
        
        summary_data = {
            'total_employees': total_employees,
            'total_taxable_salary': total_salary,
            'total_contributions': total_contributions,
        }
        
        return {
            **header_data,
            **summary_data,
            'employees': employees_data,
        }


class BankTransferReportData:
    """
    Bank transfer report data preparation
    Equivalent to data for virementbank.jrxml
    Enhanced with bank grouping and UNL export capability
    """
    
    @staticmethod
    def prepare_bank_transfer_data(payroll_records: List[Dict],
                                 period: date,
                                 system_params,
                                 bank_filter: str = None) -> Dict[str, Any]:
        """
        Prepare bank transfer report data with enhanced grouping
        
        Args:
            payroll_records: List of payroll records for transfer
            period: Transfer period
            system_params: System parameters
            bank_filter: Optional bank name filter
            
        Returns:
            Bank transfer data grouped by bank
        """
        # Filter records with bank accounts
        valid_records = [rec for rec in payroll_records if rec.get('bank_account') and rec.get('net_salary', 0) > 0]
        
        # Apply bank filter if specified
        if bank_filter:
            valid_records = [rec for rec in valid_records if rec.get('bank_name') == bank_filter]
        
        # Header information
        header_data = {
            'company_name': system_params.company_name,
            'company_account': getattr(system_params, 'transfer_account', ''),
            'period': period,
            'period_text': f"{period.strftime('%m/%Y')}",
            'transfer_date': datetime.now().date(),
            'reference': f"VIR-{period.strftime('%Y%m%d')}",
        }
        
        # Group by bank
        bank_groups = defaultdict(list)
        for record in valid_records:
            bank_name = record.get('bank_name', 'Non spécifiée')
            bank_groups[bank_name].append(record)
        
        # Prepare bank summaries
        bank_summaries = []
        total_all_banks = Decimal('0')
        total_employees_all_banks = 0
        
        for bank_name, bank_records in bank_groups.items():
            bank_total = sum(rec.get('net_salary', Decimal('0')) for rec in bank_records)
            bank_employee_count = len(bank_records)
            
            # Sort records by employee name
            sorted_bank_records = sorted(bank_records, key=lambda x: x.get('employee_name', ''))
            
            bank_summary = {
                'bank_name': bank_name,
                'employee_count': bank_employee_count,
                'total_amount': bank_total,
                'transfers': sorted_bank_records,
                'bank_reference': f"VIR-{period.strftime('%Y%m%d')}-{bank_name.upper()[:3]}",
            }
            
            bank_summaries.append(bank_summary)
            total_all_banks += bank_total
            total_employees_all_banks += bank_employee_count
        
        # Sort banks by name
        bank_summaries.sort(key=lambda x: x['bank_name'])
        
        summary_data = {
            'total_employees': total_employees_all_banks,
            'total_amount': total_all_banks,
            'bank_count': len(bank_summaries),
        }
        
        return {
            **header_data,
            **summary_data,
            'bank_summaries': bank_summaries,
            'all_transfers': valid_records,
        }


class AttendanceReportData:
    """
    Attendance report data preparation
    Equivalent to data for dTA.jrxml (Time Attendance)
    """
    
    @staticmethod
    def prepare_attendance_report_data(attendance_records: List[Dict],
                                     period: date,
                                     system_params) -> Dict[str, Any]:
        """
        Prepare attendance report data
        
        Args:
            attendance_records: List of employee attendance records
            period: Report period
            system_params: System parameters
            
        Returns:
            Attendance report data
        """
        # Header information
        header_data = {
            'company_name': system_params.company_name,
            'period': period,
            'period_text': f"{period.strftime('%B %Y')}",
            'report_date': datetime.now().date(),
        }
        
        # Summary statistics
        total_employees = len(attendance_records)
        total_worked_hours = sum(rec.get('total_hours', Decimal('0')) for rec in attendance_records)
        total_overtime = sum(rec.get('overtime_hours', Decimal('0')) for rec in attendance_records)
        
        summary_data = {
            'total_employees': total_employees,
            'total_worked_hours': total_worked_hours,
            'total_overtime': total_overtime,
        }
        
        return {
            **header_data,
            **summary_data,
            'attendance_records': attendance_records,
        }


class ReportFormatter:
    """
    Enhanced report formatting utilities for Mauritanian payroll reports
    Supports French/Arabic localization and proper number formatting
    """
    
    @staticmethod
    def format_currency_for_report(amount: Decimal, currency: str = "MRU") -> str:
        """Format currency for report display with Mauritanian formatting"""
        if amount is None:
            return "0,00 MRU"
        
        # Use French formatting: space for thousands, comma for decimals
        formatted = f"{amount:,.2f}".replace(',', ' ').replace('.', ',')
        return f"{formatted} {currency}"
    
    @staticmethod
    def format_number_for_report(number: Union[int, Decimal], decimal_places: int = 2) -> str:
        """Format number for report display with French formatting"""
        if number is None:
            return "0" + (",00" if decimal_places > 0 else "")
        
        if decimal_places == 0:
            return f"{int(round(number)):,}".replace(',', ' ')
        else:
            formatted = f"{number:,.{decimal_places}f}".replace(',', ' ').replace('.', ',')
            return formatted
    
    @staticmethod
    def format_date_for_report(date_obj: Union[date, datetime], format_type: str = "french") -> str:
        """Format date for report display in French or standard format"""
        if not date_obj:
            return ""
        
        if isinstance(date_obj, datetime):
            date_obj = date_obj.date()
        
        if format_type == "french":
            months = [
                "janvier", "février", "mars", "avril", "mai", "juin",
                "juillet", "août", "septembre", "octobre", "novembre", "décembre"
            ]
            return f"{date_obj.day} {months[date_obj.month - 1]} {date_obj.year}"
        elif format_type == "period":
            # For period display like "Juin 2023"
            months = [
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
            ]
            return f"{months[date_obj.month - 1]} {date_obj.year}"
        elif format_type == "quarter":
            # For quarterly display like "1er Trimestre 2023"
            quarter = ((date_obj.month - 1) // 3) + 1
            quarter_names = ["1er", "2ème", "3ème", "4ème"]
            return f"{quarter_names[quarter - 1]} Trimestre {date_obj.year}"
        
        return date_obj.strftime("%d/%m/%Y")
    
    @staticmethod
    def format_percentage_for_report(value: Decimal) -> str:
        """Format percentage for report display"""
        if value is None:
            return "0,00%"
        
        percentage = value * 100
        return f"{percentage:.2f}%".replace('.', ',')
    
    @staticmethod
    def format_cnss_number(cnss_number: str) -> str:
        """Format CNSS number with proper spacing"""
        if not cnss_number:
            return ""
        
        # Remove any existing spaces or dashes
        clean_number = cnss_number.replace(' ', '').replace('-', '')
        
        # Format as XXX XXX XXX if 9 digits
        if len(clean_number) == 9:
            return f"{clean_number[:3]} {clean_number[3:6]} {clean_number[6:9]}"
        
        return cnss_number
    
    @staticmethod
    def format_cnam_number(cnam_number: str) -> str:
        """Format CNAM number with proper spacing"""
        if not cnam_number:
            return ""
        
        # Remove any existing spaces or dashes
        clean_number = cnam_number.replace(' ', '').replace('-', '')
        
        # Format as XXX XXX XXX if 9 digits
        if len(clean_number) == 9:
            return f"{clean_number[:3]} {clean_number[3:6]} {clean_number[6:9]}"
        
        return cnam_number
    
    @staticmethod
    def format_nni(nni: str) -> str:
        """Format National ID (NNI) with proper spacing"""
        if not nni:
            return ""
        
        # Remove any existing spaces or dashes
        clean_nni = nni.replace(' ', '').replace('-', '')
        
        # Format as XXXX XXXX XX if 10 digits
        if len(clean_nni) == 10:
            return f"{clean_nni[:4]} {clean_nni[4:8]} {clean_nni[8:10]}"
        
        return nni
    
    @staticmethod
    def format_account_number(account: str) -> str:
        """Format bank account number with proper spacing"""
        if not account:
            return ""
        
        # Remove any existing spaces or dashes
        clean_account = account.replace(' ', '').replace('-', '')
        
        # Format with spaces every 4 digits
        formatted = ""
        for i, char in enumerate(clean_account):
            if i > 0 and i % 4 == 0:
                formatted += " "
            formatted += char
        
        return formatted
    
    @staticmethod
    def format_working_days(days: Decimal) -> str:
        """Format working days with proper decimal display"""
        if days is None:
            return "0"
        
        if days == int(days):
            return str(int(days))
        else:
            return f"{days:.1f}".replace('.', ',')
    
    @staticmethod
    def format_net_salary_in_words(amount: Decimal, currency: str = "ouguiya") -> str:
        """Format net salary amount in French words"""
        if amount is None or amount == 0:
            return f"Zéro {currency}"
        
        return MauritanianNumberConverter.number_to_french_words(amount, currency)


class ReportContext:
    """
    Unified report context preparation
    """
    
    @staticmethod
    def prepare_base_context(system_params, user=None) -> Dict[str, Any]:
        """
        Prepare base context used by all reports
        
        Args:
            system_params: SystemParameters instance
            user: Optional user instance
            
        Returns:
            Base context dictionary
        """
        return {
            # Company information
            'company': {
                'name': system_params.company_name,
                'activity': system_params.company_activity,
                'address': system_params.address,
                'phone': system_params.phone,
                'email': system_params.email,
                'website': system_params.website,
                'logo': system_params.logo,
                'manager': system_params.company_manager,
                'manager_title': system_params.manager_title,
            },
            
            # Registration numbers
            'registrations': {
                'cnss': system_params.cnss_number,
                'cnam': system_params.cnam_number,
                'its': system_params.its_number,
            },
            
            # Report metadata
            'report_meta': {
                'generated_date': datetime.now(),
                'generated_by': user.full_name if user else "System",
                'system_version': "Django Payroll v1.0",
            },
            
            # Formatting utilities
            'format': {
                'currency': ReportFormatter.format_currency_for_report,
                'number': ReportFormatter.format_number_for_report,
                'date': ReportFormatter.format_date_for_report,
                'percentage': ReportFormatter.format_percentage_for_report,
            }
        }


class ReportDataValidator:
    """
    Enhanced validation for all report types including new declarations
    """
    
    @staticmethod
    def validate_payslip_data(data: Dict[str, Any]) -> List[str]:
        """
        Validate payslip report data
        
        Args:
            data: Payslip data dictionary
            
        Returns:
            List of validation errors (empty if valid)
        """
        errors = []
        
        # Employee data validation
        if 'employee_data' in data:
            emp_data = data['employee_data']
            if not emp_data.get('first_name'):
                errors.append("Nom de l'employé manquant")
            if not emp_data.get('last_name'):
                errors.append("Prénom de l'employé manquant")
        else:
            if not data.get('first_name'):
                errors.append("Nom de l'employé manquant")
            if not data.get('last_name'):
                errors.append("Prénom de l'employé manquant")
        
        # Payroll data validation
        if 'payroll_data' in data:
            payroll = data['payroll_data']
            if not payroll.get('period'):
                errors.append("Période manquante")
            if not payroll.get('net_salary'):
                errors.append("Salaire net manquant")
            elif payroll['net_salary'] < 0:
                errors.append("Le salaire net ne peut pas être négatif")
        else:
            if not data.get('period'):
                errors.append("Période manquante")
            if not data.get('net_salary'):
                errors.append("Salaire net manquant")
            elif data.get('net_salary', 0) < 0:
                errors.append("Le salaire net ne peut pas être négatif")
        
        # Company data validation
        if 'company_data' in data:
            if not data['company_data'].get('company_name'):
                errors.append("Nom de l'entreprise manquant")
        else:
            if not data.get('company_name'):
                errors.append("Nom de l'entreprise manquant")
        
        return errors
    
    @staticmethod
    def validate_declaration_data(data: Dict[str, Any], declaration_type: str) -> List[str]:
        """
        Validate tax declaration data for CNSS, CNAM, ITS, TA
        
        Args:
            data: Declaration data dictionary
            declaration_type: "CNSS", "CNAM", "ITS", or "TA"
            
        Returns:
            List of validation errors
        """
        errors = []
        
        # Check required company information
        if not data.get('company_name'):
            errors.append("Nom de l'entreprise manquant")
        
        # Type-specific validations
        if declaration_type == "CNSS":
            if not data.get('company_cnss'):
                errors.append("Numéro d'enregistrement CNSS manquant")
            if not data.get('employees'):
                errors.append("Aucun employé dans la déclaration")
            # Validate CNSS-specific data
            for emp in data.get('employees', []):
                if not emp.get('cnss_number'):
                    errors.append(f"Numéro CNSS manquant pour l'employé {emp.get('employee_name', 'Inconnu')}")
        
        elif declaration_type == "CNAM":
            if not data.get('company_cnam'):
                errors.append("Numéro d'enregistrement CNAM manquant")
            if not data.get('employees'):
                errors.append("Aucun employé dans la déclaration")
            # Validate CNAM-specific data
            for emp in data.get('employees', []):
                if not emp.get('cnam_number'):
                    errors.append(f"Numéro CNAM manquant pour l'employé {emp.get('employee_name', 'Inconnu')}")
        
        elif declaration_type == "ITS":
            if not data.get('company_its'):
                errors.append("Numéro ITS de l'entreprise manquant")
            if not data.get('period'):
                errors.append("Période de déclaration manquante")
            if data.get('total_employees', 0) <= 0:
                errors.append("Aucun employé à déclarer")
            # Validate ITS calculations
            total_gross = data.get('total_gross_remuneration', 0)
            total_benefits = data.get('total_benefits_in_kind', 0)
            total_non_taxable = data.get('total_non_taxable', 0)
            if total_gross < 0 or total_benefits < 0 or total_non_taxable < 0:
                errors.append("Les montants de rémunération ne peuvent pas être négatifs")
        
        elif declaration_type == "TA":
            if not data.get('company_its'):
                errors.append("Numéro ITS de l'entreprise manquant")
            if not data.get('declaration_year'):
                errors.append("Année de déclaration manquante")
            # Validate TA calculations
            taxable_amount = data.get('taxable_amount', 0)
            ta_amount = data.get('ta_amount', 0)
            if taxable_amount < 0 or ta_amount < 0:
                errors.append("Les montants de la taxe d'apprentissage ne peuvent pas être négatifs")
        
        return errors
    
    @staticmethod
    def validate_bank_transfer_data(data: Dict[str, Any]) -> List[str]:
        """
        Validate bank transfer report data
        
        Args:
            data: Bank transfer data dictionary
            
        Returns:
            List of validation errors
        """
        errors = []
        
        if not data.get('company_name'):
            errors.append("Nom de l'entreprise manquant")
        
        if not data.get('period'):
            errors.append("Période de virement manquante")
        
        if not data.get('bank_summaries') and not data.get('all_transfers'):
            errors.append("Aucune donnée de virement trouvée")
        
        # Validate individual transfers
        transfers = data.get('all_transfers', [])
        for transfer in transfers:
            if not transfer.get('bank_account'):
                errors.append(f"Compte bancaire manquant pour {transfer.get('employee_name', 'Employé inconnu')}")
            if not transfer.get('net_salary') or transfer['net_salary'] <= 0:
                errors.append(f"Montant de virement invalide pour {transfer.get('employee_name', 'Employé inconnu')}")
        
        return errors
    
    @staticmethod
    def validate_accounting_export_data(data: Dict[str, Any]) -> List[str]:
        """
        Validate accounting export data
        
        Args:
            data: Accounting export data dictionary
            
        Returns:
            List of validation errors
        """
        errors = []
        
        if not data.get('company_name'):
            errors.append("Nom de l'entreprise manquant")
        
        if not data.get('period'):
            errors.append("Période d'export manquante")
        
        if not data.get('accounting_entries'):
            errors.append("Aucune écriture comptable générée")
        
        # Validate balance
        balance_check = data.get('balance_check', 0)
        if abs(balance_check) > Decimal('0.01'):  # Allow small rounding differences
            errors.append(f"Balance comptable déséquilibrée: {balance_check}")
        
        # Validate entries
        entries = data.get('accounting_entries', [])
        for entry in entries:
            if not entry.get('account_code'):
                errors.append("Code compte manquant dans une écriture")
            if not entry.get('account_name'):
                errors.append("Nom de compte manquant dans une écriture")
            if entry.get('debit', 0) < 0 or entry.get('credit', 0) < 0:
                errors.append("Les montants débit/crédit ne peuvent pas être négatifs")
        
        return errors


class ExportUtilities:
    """
    Data export utilities for reports
    """
    
    @staticmethod
    def to_csv_format(data: List[Dict[str, Any]], 
                     field_mapping: Dict[str, str] = None) -> str:
        """
        Convert report data to CSV format
        
        Args:
            data: List of data dictionaries
            field_mapping: Optional mapping of field names to headers
            
        Returns:
            CSV formatted string
        """
        if not data:
            return ""
        
        # Get headers
        headers = list(data[0].keys())
        if field_mapping:
            headers = [field_mapping.get(h, h) for h in headers]
        
        lines = [";".join(headers)]
        
        # Add data rows
        for row in data:
            values = []
            for key in data[0].keys():
                value = row.get(key, "")
                # Handle different data types
                if isinstance(value, Decimal):
                    values.append(str(value).replace('.', ','))
                elif isinstance(value, (date, datetime)):
                    values.append(value.strftime("%d/%m/%Y"))
                else:
                    values.append(str(value))
            
            lines.append(";".join(values))
        
        return "\n".join(lines)
    
    @staticmethod
    def to_json_format(data: Any) -> str:
        """
        Convert report data to JSON format
        
        Args:
            data: Data to convert
            
        Returns:
            JSON formatted string
        """
        def decimal_serializer(obj):
            if isinstance(obj, Decimal):
                return float(obj)
            if isinstance(obj, (date, datetime)):
                return obj.isoformat()
            raise TypeError(f"Object of type {type(obj)} is not JSON serializable")
        
        return json.dumps(data, default=decimal_serializer, indent=2, ensure_ascii=False)


class ITSDeclarationReportData:
    """
    Enhanced ITS (Impôt sur les Traitements et Salaires) declaration report data
    with proper Mauritanian tax brackets and validation
    """
    
    # Mauritanian ITS tax brackets (as of 2024)
    ITS_BRACKETS = [
        {'min': Decimal('0'), 'max': Decimal('30000'), 'rate': Decimal('0.00')},
        {'min': Decimal('30000'), 'max': Decimal('100000'), 'rate': Decimal('0.15')},
        {'min': Decimal('100000'), 'max': None, 'rate': Decimal('0.25')}
    ]
    
    @staticmethod
    def calculate_its_by_brackets(taxable_amount: Decimal) -> Dict[str, Decimal]:
        """
        Calculate ITS by tax brackets according to Mauritanian law
        
        Args:
            taxable_amount: Monthly taxable amount
            
        Returns:
            Dictionary with ITS calculation details
        """
        its_tranche1 = Decimal('0')
        its_tranche2 = Decimal('0')
        its_tranche3 = Decimal('0')
        
        remaining_amount = taxable_amount
        
        for i, bracket in enumerate(ITSDeclarationReportData.ITS_BRACKETS):
            bracket_min = bracket['min']
            bracket_max = bracket['max']
            rate = bracket['rate']
            
            if remaining_amount <= 0:
                break
            
            if bracket_max is None:
                taxable_in_bracket = remaining_amount
            else:
                taxable_in_bracket = min(remaining_amount, bracket_max - bracket_min)
            
            tax_in_bracket = taxable_in_bracket * rate
            
            if i == 0:
                its_tranche1 = tax_in_bracket
            elif i == 1:
                its_tranche2 = tax_in_bracket
            elif i == 2:
                its_tranche3 = tax_in_bracket
            
            remaining_amount -= taxable_in_bracket
        
        return {
            'its_tranche1': its_tranche1,
            'its_tranche2': its_tranche2,
            'its_tranche3': its_tranche3,
            'total_its': its_tranche1 + its_tranche2 + its_tranche3
        }
    
    @staticmethod
    def prepare_enhanced_its_declaration_data(employees_data: List[Dict],
                                            period: date,
                                            system_params) -> Dict[str, Any]:
        """
        Prepare enhanced ITS monthly declaration report data with proper validation
        
        Args:
            employees_data: List of employee ITS data
            period: Declaration period (monthly)
            system_params: System parameters
            
        Returns:
            Enhanced ITS declaration data with validation
        """
        # Validate input data
        validation_errors = []
        if not employees_data:
            validation_errors.append("Aucun employé à déclarer")
        
        if not system_params.its_number:
            validation_errors.append("Numéro ITS de l'entreprise manquant")
        
        # Header information
        header_data = {
            'company_name': system_params.company_name,
            'company_its': system_params.its_number,
            'company_activity': system_params.company_activity,
            'company_address': system_params.address,
            'company_phone': system_params.phone,
            'company_email': system_params.email,
            'period': period,
            'period_text': ReportFormatter.format_date_for_report(period, "period"),
            'declaration_date': datetime.now().date(),
            'validation_errors': validation_errors,
        }
        
        # Enhanced calculations with validation
        total_employees = len(employees_data)
        total_gross_remuneration = sum(emp.get('gross_remuneration', Decimal('0')) for emp in employees_data)
        total_benefits_in_kind = sum(emp.get('benefits_in_kind', Decimal('0')) for emp in employees_data)
        total_non_taxable = sum(emp.get('non_taxable_remuneration', Decimal('0')) for emp in employees_data)
        
        # Fixed deduction: 6000 MRU per employee per month
        abatement_forfaitaire = Decimal('6000') * total_employees
        
        # Taxable remuneration calculation
        total_taxable_before_abatement = total_gross_remuneration + total_benefits_in_kind - total_non_taxable
        taxable_remuneration = max(total_taxable_before_abatement - abatement_forfaitaire, Decimal('0'))
        
        # Calculate ITS by brackets
        its_calculation = ITSDeclarationReportData.calculate_its_by_brackets(taxable_remuneration)
        
        # Validate individual employee data
        validated_employees = []
        for emp in employees_data:
            emp_errors = []
            if not emp.get('employee_name'):
                emp_errors.append("Nom manquant")
            if not emp.get('national_id'):
                emp_errors.append("NNI manquant")
            if emp.get('gross_remuneration', 0) < 0:
                emp_errors.append("Rémunération négative")
            
            validated_emp = dict(emp)
            validated_emp['validation_errors'] = emp_errors
            validated_employees.append(validated_emp)
        
        summary_data = {
            'total_employees': total_employees,
            'total_gross_remuneration': total_gross_remuneration,
            'total_benefits_in_kind': total_benefits_in_kind,
            'abatement_forfaitaire': abatement_forfaitaire,
            'total_non_taxable': total_non_taxable,
            'total_taxable_before_abatement': total_taxable_before_abatement,
            'taxable_remuneration': taxable_remuneration,
            **its_calculation,
        }
        
        return {
            **header_data,
            **summary_data,
            'employees': validated_employees,
        }
    
    @staticmethod
    def prepare_its_declaration_data(employees_data: List[Dict],
                                   period: date,
                                   system_params) -> Dict[str, Any]:
        """
        Legacy method - calls enhanced version for backward compatibility
        """
        return ITSDeclarationReportData.prepare_enhanced_its_declaration_data(
            employees_data, period, system_params
        )


class TADeclarationReportData:
    """
    Enhanced TA (Taxe d'Apprentissage) annual declaration report data
    with proper validation and calculation logic
    """
    
    TA_RATE = Decimal('0.006')  # 0.6% rate for TA
    
    @staticmethod
    def prepare_ta_declaration_data(annual_data: Dict[str, Any],
                                  year: int,
                                  system_params) -> Dict[str, Any]:
        """
        Prepare TA annual declaration report data
        
        Args:
            annual_data: Annual payroll aggregated data
            year: Declaration year
            system_params: System parameters
            
        Returns:
            TA declaration data
        """
        # Header information
        header_data = {
            'company_name': system_params.company_name,
            'company_its': system_params.its_number,
            'company_activity': system_params.company_activity,
            'company_address': system_params.address,
            'period_text': f"31/12/{year}",
            'declaration_year': year,
            'declaration_date': datetime.now().date(),
        }
        
        # Annual calculations
        annual_taxable_remuneration = annual_data.get('total_taxable_remuneration', Decimal('0'))
        annual_benefits_in_kind = annual_data.get('total_benefits_in_kind', Decimal('0'))
        annual_non_taxable = annual_data.get('total_non_taxable', Decimal('0'))
        
        # TA calculation (0.6% of taxable amount)
        taxable_amount = annual_taxable_remuneration + annual_benefits_in_kind
        ta_rate = Decimal('0.006')  # 0.6%
        ta_amount = taxable_amount * ta_rate
        
        calculation_data = {
            'annual_taxable_remuneration': annual_taxable_remuneration,
            'annual_benefits_in_kind': annual_benefits_in_kind,
            'annual_non_taxable': annual_non_taxable,
            'taxable_amount': taxable_amount,
            'ta_rate': ta_rate,
            'ta_amount': ta_amount,
        }
        
        return {
            **header_data,
            **calculation_data,
        }


class PayrollStatsReportData:
    """
    Payroll statistics and analytical report data
    Equivalent to functionality in etats.java and statistiques.java
    """
    
    @staticmethod
    def prepare_payroll_summary_data(payroll_records: List[Dict],
                                   period: date,
                                   filters: Dict[str, Any],
                                   system_params) -> Dict[str, Any]:
        """
        Prepare comprehensive payroll summary report
        
        Args:
            payroll_records: List of payroll records
            period: Report period
            filters: Department, direction, status filters
            system_params: System parameters
            
        Returns:
            Payroll summary data
        """
        # Apply filters
        filtered_records = PayrollStatsReportData._apply_filters(payroll_records, filters)
        
        # Header information
        header_data = {
            'company_name': system_params.company_name,
            'period': period,
            'period_text': f"{period.strftime('%B %Y')}",
            'report_date': datetime.now().date(),
            'filters_applied': filters,
        }
        
        # Calculate summary statistics
        total_employees = len(filtered_records)
        total_gross_taxable = sum(rec.get('gross_taxable', Decimal('0')) for rec in filtered_records)
        total_gross_non_taxable = sum(rec.get('gross_non_taxable', Decimal('0')) for rec in filtered_records)
        total_net_salary = sum(rec.get('net_salary', Decimal('0')) for rec in filtered_records)
        total_cnss_employee = sum(rec.get('cnss_employee', Decimal('0')) for rec in filtered_records)
        total_cnam_employee = sum(rec.get('cnam_employee', Decimal('0')) for rec in filtered_records)
        total_its = sum(rec.get('its_total', Decimal('0')) for rec in filtered_records)
        
        # Group by department/direction
        dept_summary = PayrollStatsReportData._group_by_department(filtered_records)
        direction_summary = PayrollStatsReportData._group_by_direction(filtered_records)
        
        summary_data = {
            'total_employees': total_employees,
            'total_gross_taxable': total_gross_taxable,
            'total_gross_non_taxable': total_gross_non_taxable,
            'total_net_salary': total_net_salary,
            'total_cnss_employee': total_cnss_employee,
            'total_cnam_employee': total_cnam_employee,
            'total_its': total_its,
            'average_net_salary': total_net_salary / total_employees if total_employees > 0 else Decimal('0'),
        }
        
        return {
            **header_data,
            **summary_data,
            'department_breakdown': dept_summary,
            'direction_breakdown': direction_summary,
            'detailed_records': filtered_records,
        }
    
    @staticmethod
    def _apply_filters(records: List[Dict], filters: Dict[str, Any]) -> List[Dict]:
        """Apply filters to payroll records"""
        filtered = records
        
        if filters.get('department_ids'):
            filtered = [r for r in filtered if r.get('department_id') in filters['department_ids']]
        
        if filters.get('direction_ids'):
            filtered = [r for r in filtered if r.get('direction_id') in filters['direction_ids']]
        
        if filters.get('status'):
            filtered = [r for r in filtered if r.get('employment_status') == filters['status']]
        
        return filtered
    
    @staticmethod
    def _group_by_department(records: List[Dict]) -> List[Dict]:
        """Group payroll records by department"""
        dept_groups = defaultdict(list)
        for record in records:
            dept_name = record.get('department_name', 'Non spécifié')
            dept_groups[dept_name].append(record)
        
        summary = []
        for dept_name, dept_records in dept_groups.items():
            dept_summary = {
                'department_name': dept_name,
                'employee_count': len(dept_records),
                'total_net_salary': sum(rec.get('net_salary', Decimal('0')) for rec in dept_records),
                'average_net_salary': sum(rec.get('net_salary', Decimal('0')) for rec in dept_records) / len(dept_records),
            }
            summary.append(dept_summary)
        
        return sorted(summary, key=lambda x: x['department_name'])
    
    @staticmethod
    def _group_by_direction(records: List[Dict]) -> List[Dict]:
        """Group payroll records by direction"""
        dir_groups = defaultdict(list)
        for record in records:
            dir_name = record.get('direction_name', 'Non spécifiée')
            dir_groups[dir_name].append(record)
        
        summary = []
        for dir_name, dir_records in dir_groups.items():
            dir_summary = {
                'direction_name': dir_name,
                'employee_count': len(dir_records),
                'total_net_salary': sum(rec.get('net_salary', Decimal('0')) for rec in dir_records),
                'average_net_salary': sum(rec.get('net_salary', Decimal('0')) for rec in dir_records) / len(dir_records),
            }
            summary.append(dir_summary)
        
        return sorted(summary, key=lambda x: x['direction_name'])


class CumulativeReportData:
    """
    Cumulative payroll report data across multiple periods
    Equivalent to functionality in etatsCumul.java
    """
    
    @staticmethod
    def prepare_cumulative_payroll_data(payroll_records: List[Dict],
                                      start_period: date,
                                      end_period: date,
                                      system_params) -> Dict[str, Any]:
        """
        Prepare cumulative payroll report across periods
        
        Args:
            payroll_records: List of payroll records across periods
            start_period: Start period for cumulation
            end_period: End period for cumulation
            system_params: System parameters
            
        Returns:
            Cumulative payroll data
        """
        # Header information
        header_data = {
            'company_name': system_params.company_name,
            'start_period': start_period,
            'end_period': end_period,
            'period_text': f"Du {start_period.strftime('%m/%Y')} au {end_period.strftime('%m/%Y')}",
            'report_date': datetime.now().date(),
        }
        
        # Group records by employee and accumulate
        employee_cumulative = defaultdict(lambda: {
            'periods_worked': 0,
            'total_gross_taxable': Decimal('0'),
            'total_gross_non_taxable': Decimal('0'),
            'total_net_salary': Decimal('0'),
            'total_cnss_employee': Decimal('0'),
            'total_cnam_employee': Decimal('0'),
            'total_its': Decimal('0'),
        })
        
        for record in payroll_records:
            emp_id = record.get('employee_id')
            if emp_id:
                emp_data = employee_cumulative[emp_id]
                emp_data['employee_name'] = record.get('employee_name', '')
                emp_data['employee_number'] = record.get('employee_number', '')
                emp_data['department_name'] = record.get('department_name', '')
                emp_data['periods_worked'] += 1
                emp_data['total_gross_taxable'] += record.get('gross_taxable', Decimal('0'))
                emp_data['total_gross_non_taxable'] += record.get('gross_non_taxable', Decimal('0'))
                emp_data['total_net_salary'] += record.get('net_salary', Decimal('0'))
                emp_data['total_cnss_employee'] += record.get('cnss_employee', Decimal('0'))
                emp_data['total_cnam_employee'] += record.get('cnam_employee', Decimal('0'))
                emp_data['total_its'] += record.get('its_total', Decimal('0'))
        
        # Convert to list and calculate averages
        cumulative_records = []
        for emp_id, emp_data in employee_cumulative.items():
            periods = emp_data['periods_worked']
            emp_data['average_net_salary'] = emp_data['total_net_salary'] / periods if periods > 0 else Decimal('0')
            emp_data['employee_id'] = emp_id
            cumulative_records.append(emp_data)
        
        # Calculate grand totals
        grand_totals = {
            'total_employees': len(cumulative_records),
            'total_gross_taxable': sum(rec['total_gross_taxable'] for rec in cumulative_records),
            'total_gross_non_taxable': sum(rec['total_gross_non_taxable'] for rec in cumulative_records),
            'total_net_salary': sum(rec['total_net_salary'] for rec in cumulative_records),
            'total_cnss_employee': sum(rec['total_cnss_employee'] for rec in cumulative_records),
            'total_cnam_employee': sum(rec['total_cnam_employee'] for rec in cumulative_records),
            'total_its': sum(rec['total_its'] for rec in cumulative_records),
        }
        
        return {
            **header_data,
            **grand_totals,
            'employee_cumulative': sorted(cumulative_records, key=lambda x: x['employee_name']),
        }


class AccountingExportData:
    """
    Accounting system export data preparation
    Equivalent to functionality in compta.java
    """
    
    @staticmethod
    def prepare_accounting_export_data(payroll_records: List[Dict],
                                     period: date,
                                     export_format: str,
                                     system_params) -> Dict[str, Any]:
        """
        Prepare accounting export data
        
        Args:
            payroll_records: List of payroll records
            period: Export period
            export_format: 'sage', 'ciel', 'generic'
            system_params: System parameters
            
        Returns:
            Accounting export data
        """
        # Header information
        header_data = {
            'company_name': system_params.company_name,
            'period': period,
            'period_text': f"{period.strftime('%m/%Y')}",
            'export_format': export_format,
            'export_date': datetime.now().date(),
        }
        
        # Generate accounting entries
        accounting_entries = []
        
        # Salary entries
        for record in payroll_records:
            # Salary expense entry
            salary_entry = {
                'account_code': '641100',  # Salary expense account
                'account_name': 'Salaires et appointements',
                'debit': record.get('gross_taxable', Decimal('0')),
                'credit': Decimal('0'),
                'reference': f"PAIE-{period.strftime('%m%Y')}-{record.get('employee_number', '')}",
                'description': f"Salaire {record.get('employee_name', '')} - {period.strftime('%m/%Y')}",
                'employee_id': record.get('employee_id'),
            }
            accounting_entries.append(salary_entry)
            
            # CNSS entries
            if record.get('cnss_employee', Decimal('0')) > 0:
                cnss_entry = {
                    'account_code': '431100',  # CNSS payable account
                    'account_name': 'CNSS - Part salariale',
                    'debit': Decimal('0'),
                    'credit': record.get('cnss_employee', Decimal('0')),
                    'reference': f"CNSS-{period.strftime('%m%Y')}-{record.get('employee_number', '')}",
                    'description': f"CNSS salariale {record.get('employee_name', '')} - {period.strftime('%m/%Y')}",
                    'employee_id': record.get('employee_id'),
                }
                accounting_entries.append(cnss_entry)
            
            # CNAM entries
            if record.get('cnam_employee', Decimal('0')) > 0:
                cnam_entry = {
                    'account_code': '431200',  # CNAM payable account
                    'account_name': 'CNAM - Part salariale',
                    'debit': Decimal('0'),
                    'credit': record.get('cnam_employee', Decimal('0')),
                    'reference': f"CNAM-{period.strftime('%m%Y')}-{record.get('employee_number', '')}",
                    'description': f"CNAM salariale {record.get('employee_name', '')} - {period.strftime('%m/%Y')}",
                    'employee_id': record.get('employee_id'),
                }
                accounting_entries.append(cnam_entry)
            
            # ITS entries
            if record.get('its_total', Decimal('0')) > 0:
                its_entry = {
                    'account_code': '431300',  # ITS payable account
                    'account_name': 'ITS retenu sur salaires',
                    'debit': Decimal('0'),
                    'credit': record.get('its_total', Decimal('0')),
                    'reference': f"ITS-{period.strftime('%m%Y')}-{record.get('employee_number', '')}",
                    'description': f"ITS {record.get('employee_name', '')} - {period.strftime('%m/%Y')}",
                    'employee_id': record.get('employee_id'),
                }
                accounting_entries.append(its_entry)
            
            # Net salary payable
            net_salary_entry = {
                'account_code': '421100',  # Employee payable account
                'account_name': 'Salaires nets à payer',
                'debit': Decimal('0'),
                'credit': record.get('net_salary', Decimal('0')),
                'reference': f"NET-{period.strftime('%m%Y')}-{record.get('employee_number', '')}",
                'description': f"Salaire net {record.get('employee_name', '')} - {period.strftime('%m/%Y')}",
                'employee_id': record.get('employee_id'),
            }
            accounting_entries.append(net_salary_entry)
        
        # Calculate totals
        total_debit = sum(entry['debit'] for entry in accounting_entries)
        total_credit = sum(entry['credit'] for entry in accounting_entries)
        
        summary_data = {
            'total_entries': len(accounting_entries),
            'total_debit': total_debit,
            'total_credit': total_credit,
            'balance_check': total_debit - total_credit,  # Should be 0
        }
        
        return {
            **header_data,
            **summary_data,
            'accounting_entries': accounting_entries,
        }


class AdvancedExportUtilities:
    """
    Advanced data export utilities for multiple formats
    Includes UNL, XML, Excel with formatting, and enhanced CSV
    """
    
    @staticmethod
    def to_unl_format(data: List[Dict[str, Any]], 
                     field_order: List[str] = None) -> str:
        """
        Convert report data to UNL (Universal) format
        Pipe-delimited format commonly used for data interchange
        
        Args:
            data: List of data dictionaries
            field_order: Optional order of fields
            
        Returns:
            UNL formatted string
        """
        if not data:
            return ""
        
        # Determine field order
        if field_order is None:
            field_order = list(data[0].keys())
        
        lines = []
        
        # Add data rows
        for row in data:
            values = []
            for field in field_order:
                value = row.get(field, "")
                # Handle different data types
                if isinstance(value, Decimal):
                    values.append(str(value))
                elif isinstance(value, (date, datetime)):
                    values.append(value.strftime("%Y%m%d"))
                elif value is None:
                    values.append("")
                else:
                    # Escape pipe characters
                    values.append(str(value).replace('|', '¦'))
            
            lines.append("|".join(values) + "|")
        
        return "\n".join(lines)
    
    @staticmethod
    def to_xml_format(data: List[Dict[str, Any]], 
                     root_element: str = "data",
                     row_element: str = "record") -> str:
        """
        Convert report data to XML format
        
        Args:
            data: List of data dictionaries
            root_element: Name of root XML element
            row_element: Name of each row element
            
        Returns:
            XML formatted string
        """
        if not data:
            return f"<{root_element}></{root_element}>"
        
        root = ET.Element(root_element)
        
        for row in data:
            record_elem = ET.SubElement(root, row_element)
            
            for key, value in row.items():
                # Clean field name for XML
                clean_key = re.sub(r'[^a-zA-Z0-9_]', '_', str(key))
                field_elem = ET.SubElement(record_elem, clean_key)
                
                if isinstance(value, Decimal):
                    field_elem.text = str(value)
                elif isinstance(value, (date, datetime)):
                    field_elem.text = value.isoformat()
                elif value is None:
                    field_elem.text = ""
                else:
                    field_elem.text = str(value)
        
        # Pretty print XML
        rough_string = ET.tostring(root, 'utf-8')
        reparsed = minidom.parseString(rough_string)
        return reparsed.toprettyxml(indent="  ", encoding=None)
    
    @staticmethod
    def to_excel_structured_format(data: List[Dict[str, Any]], 
                                 title: str = "Report",
                                 include_summary: bool = True) -> Dict[str, Any]:
        """
        Prepare data for Excel export with advanced formatting
        
        Args:
            data: List of data dictionaries
            title: Report title
            include_summary: Whether to include summary statistics
            
        Returns:
            Structured data for Excel export with formatting instructions
        """
        if not data:
            return {
                'title': title,
                'headers': [],
                'data': [],
                'summary': {},
                'formatting': {}
            }
        
        # Extract headers and categorize by data type
        headers = list(data[0].keys())
        
        # Analyze data types for formatting
        formatting_info = {}
        for header in headers:
            sample_values = [row.get(header) for row in data[:5] if row.get(header) is not None]
            if sample_values:
                sample_value = sample_values[0]
                if isinstance(sample_value, Decimal):
                    formatting_info[header] = {'type': 'currency', 'format': '#,##0.00'}
                elif isinstance(sample_value, (int, float)):
                    formatting_info[header] = {'type': 'number', 'format': '#,##0'}
                elif isinstance(sample_value, (date, datetime)):
                    formatting_info[header] = {'type': 'date', 'format': 'dd/mm/yyyy'}
                else:
                    formatting_info[header] = {'type': 'text', 'format': '@'}
        
        # Prepare summary if requested
        summary = {}
        if include_summary:
            for header in headers:
                values = [row.get(header) for row in data if isinstance(row.get(header), (int, float, Decimal))]
                if values:
                    summary[header] = {
                        'count': len(values),
                        'sum': sum(values),
                        'average': sum(values) / len(values),
                        'min': min(values),
                        'max': max(values)
                    }
        
        return {
            'title': title,
            'headers': headers,
            'data': data,
            'summary': summary,
            'formatting': formatting_info,
            'creation_date': datetime.now(),
        }
    
    @staticmethod
    def to_delimited_format(data: List[Dict[str, Any]], 
                          delimiter: str = ";",
                          quote_char: str = '"',
                          include_headers: bool = True,
                          field_mapping: Dict[str, str] = None) -> str:
        """
        Convert report data to custom delimited format
        
        Args:
            data: List of data dictionaries
            delimiter: Field delimiter
            quote_char: Quote character for text fields
            include_headers: Whether to include header row
            field_mapping: Optional mapping of field names to headers
            
        Returns:
            Delimited formatted string
        """
        if not data:
            return ""
        
        output = io.StringIO()
        
        # Get headers
        headers = list(data[0].keys())
        if field_mapping:
            display_headers = [field_mapping.get(h, h) for h in headers]
        else:
            display_headers = headers
        
        writer = csv.writer(output, delimiter=delimiter, quotechar=quote_char, 
                          quoting=csv.QUOTE_NONNUMERIC)
        
        # Write headers if requested
        if include_headers:
            writer.writerow(display_headers)
        
        # Write data rows
        for row in data:
            row_values = []
            for key in headers:
                value = row.get(key, "")
                # Handle different data types
                if isinstance(value, Decimal):
                    row_values.append(str(value).replace('.', ','))
                elif isinstance(value, (date, datetime)):
                    row_values.append(value.strftime("%d/%m/%Y"))
                elif value is None:
                    row_values.append("")
                else:
                    row_values.append(str(value))
            
            writer.writerow(row_values)
        
        return output.getvalue()


class MauritanianNumberConverter:
    """
    Convert numbers to words in French and Arabic for Mauritanian reports
    """
    
    # French number words
    FRENCH_UNITS = [
        '', 'un', 'deux', 'trois', 'quatre', 'cinq', 'six', 'sept', 'huit', 'neuf',
        'dix', 'onze', 'douze', 'treize', 'quatorze', 'quinze', 'seize', 
        'dix-sept', 'dix-huit', 'dix-neuf'
    ]
    
    FRENCH_TENS = [
        '', '', 'vingt', 'trente', 'quarante', 'cinquante', 
        'soixante', 'soixante-dix', 'quatre-vingt', 'quatre-vingt-dix'
    ]
    
    FRENCH_SCALES = [
        '', 'mille', 'million', 'milliard', 'billion'
    ]
    
    @staticmethod
    def number_to_french_words(amount: Decimal, currency: str = "ouguiya") -> str:
        """
        Convert a number to French words
        
        Args:
            amount: Number to convert
            currency: Currency name in French
            
        Returns:
            Number in French words
        """
        if amount == 0:
            return f"zéro {currency}"
        
        # Split into integer and decimal parts
        integer_part = int(amount)
        decimal_part = int((amount % 1) * 100)
        
        # Convert integer part
        words = MauritanianNumberConverter._convert_integer_to_french(integer_part)
        
        # Add currency
        if integer_part == 1:
            words += f" {currency}"
        else:
            words += f" {currency}s"
        
        # Add decimal part if present
        if decimal_part > 0:
            decimal_words = MauritanianNumberConverter._convert_integer_to_french(decimal_part)
            words += f" et {decimal_words} centime"
            if decimal_part > 1:
                words += "s"
        
        return words
    
    @staticmethod
    def _convert_integer_to_french(number: int) -> str:
        """
        Convert integer to French words
        """
        if number == 0:
            return "zéro"
        
        if number < 20:
            return MauritanianNumberConverter.FRENCH_UNITS[number]
        
        if number < 100:
            tens = number // 10
            units = number % 10
            if tens == 7:  # Special case for 70-79
                if units == 0:
                    return "soixante-dix"
                else:
                    return f"soixante-{MauritanianNumberConverter.FRENCH_UNITS[10 + units]}"
            elif tens == 9:  # Special case for 90-99
                if units == 0:
                    return "quatre-vingt-dix"
                else:
                    return f"quatre-vingt-{MauritanianNumberConverter.FRENCH_UNITS[10 + units]}"
            else:
                result = MauritanianNumberConverter.FRENCH_TENS[tens]
                if units > 0:
                    if tens == 8 and units == 1:  # quatre-vingt-un
                        result += "-un"
                    else:
                        result += f"-{MauritanianNumberConverter.FRENCH_UNITS[units]}"
                return result
        
        # For larger numbers, implement recursively
        if number < 1000:
            hundreds = number // 100
            remainder = number % 100
            
            if hundreds == 1:
                result = "cent"
            else:
                result = f"{MauritanianNumberConverter.FRENCH_UNITS[hundreds]} cents"
            
            if remainder > 0:
                result += f" {MauritanianNumberConverter._convert_integer_to_french(remainder)}"
            
            return result
        
        # Handle thousands and larger
        for i, scale in enumerate(MauritanianNumberConverter.FRENCH_SCALES):
            if number < 1000 ** (i + 1):
                break
        
        divisor = 1000 ** i
        quotient = number // divisor
        remainder = number % divisor
        
        if quotient == 1 and i == 1:  # Special case for "mille"
            result = "mille"
        else:
            result = f"{MauritanianNumberConverter._convert_integer_to_french(quotient)} {scale}"
        
        if remainder > 0:
            result += f" {MauritanianNumberConverter._convert_integer_to_french(remainder)}"
        
        return result
    
    @staticmethod
    def number_to_french_words_enhanced(amount: Decimal, currency: str = "ouguiya", include_decimals: bool = True) -> str:
        """
        Enhanced French number to words conversion with better formatting
        
        Args:
            amount: Number to convert
            currency: Currency name in French
            include_decimals: Whether to include decimal part
            
        Returns:
            Number in French words with proper formatting
        """
        if amount == 0:
            return f"zéro {currency}"
        
        # Handle negative numbers
        negative = amount < 0
        amount = abs(amount)
        
        # Split into integer and decimal parts
        integer_part = int(amount)
        decimal_part = int(round((amount % 1) * 100)) if include_decimals else 0
        
        # Convert integer part
        words = MauritanianNumberConverter._convert_integer_to_french_enhanced(integer_part)
        
        # Add negative prefix if needed
        if negative:
            words = f"moins {words}"
        
        # Add currency with proper agreement
        if integer_part <= 1:
            words += f" {currency}"
        else:
            # Handle special plural forms
            if currency == "ouguiya":
                words += " ouguiyas"
            elif currency == "euro":
                words += " euros"
            else:
                words += f" {currency}s"
        
        # Add decimal part if present and requested
        if include_decimals and decimal_part > 0:
            decimal_words = MauritanianNumberConverter._convert_integer_to_french_enhanced(decimal_part)
            if decimal_part == 1:
                words += f" et {decimal_words} centime"
            else:
                words += f" et {decimal_words} centimes"
        
        return words
    
    @staticmethod
    def _convert_integer_to_french_enhanced(number: int) -> str:
        """
        Enhanced integer to French words conversion with better handling of special cases
        """
        if number == 0:
            return "zéro"
        
        if number < 0:
            return f"moins {MauritanianNumberConverter._convert_integer_to_french_enhanced(-number)}"
        
        # Handle numbers less than 20
        if number < 20:
            return MauritanianNumberConverter.FRENCH_UNITS[number]
        
        # Handle numbers less than 100
        if number < 100:
            tens = number // 10
            units = number % 10
            
            # Special cases for 70-79 and 90-99
            if tens == 7:
                if units == 0:
                    return "soixante-dix"
                elif units == 1:
                    return "soixante et onze"
                else:
                    return f"soixante-{MauritanianNumberConverter.FRENCH_UNITS[10 + units]}"
            elif tens == 9:
                if units == 0:
                    return "quatre-vingt-dix"
                elif units == 1:
                    return "quatre-vingt-onze"
                else:
                    return f"quatre-vingt-{MauritanianNumberConverter.FRENCH_UNITS[10 + units]}"
            else:
                result = MauritanianNumberConverter.FRENCH_TENS[tens]
                if units > 0:
                    if tens == 2 and units == 1:  # Special case: vingt et un
                        result += " et un"
                    elif tens == 3 and units == 1:  # Special case: trente et un
                        result += " et un"
                    elif tens == 4 and units == 1:  # Special case: quarante et un
                        result += " et un"
                    elif tens == 5 and units == 1:  # Special case: cinquante et un
                        result += " et un"
                    elif tens == 6 and units == 1:  # Special case: soixante et un
                        result += " et un"
                    elif tens == 8 and units == 1:  # Special case: quatre-vingt-un
                        result += "-un"
                    else:
                        result += f"-{MauritanianNumberConverter.FRENCH_UNITS[units]}"
                return result
        
        # Handle hundreds
        if number < 1000:
            hundreds = number // 100
            remainder = number % 100
            
            if hundreds == 1:
                result = "cent"
            else:
                result = f"{MauritanianNumberConverter.FRENCH_UNITS[hundreds]} cents"
            
            if remainder > 0:
                result += f" {MauritanianNumberConverter._convert_integer_to_french_enhanced(remainder)}"
            
            return result
        
        # Handle thousands and larger numbers
        for i, scale in enumerate(MauritanianNumberConverter.FRENCH_SCALES[1:], 1):
            if number < 1000 ** (i + 1):
                divisor = 1000 ** i
                quotient = number // divisor
                remainder = number % divisor
                
                if quotient == 1 and i == 1:  # Special case for "mille"
                    result = "mille"
                else:
                    quotient_words = MauritanianNumberConverter._convert_integer_to_french_enhanced(quotient)
                    if i == 2 and quotient > 1:  # millions
                        result = f"{quotient_words} {scale}s"
                    else:
                        result = f"{quotient_words} {scale}"
                
                if remainder > 0:
                    result += f" {MauritanianNumberConverter._convert_integer_to_french_enhanced(remainder)}"
                
                return result
        
        return str(number)  # Fallback for very large numbers
    
    @staticmethod
    def number_to_arabic_words(amount: Decimal, currency: str = "أوقية") -> str:
        """
        Basic Arabic number to words conversion (simplified)
        
        Args:
            amount: Number to convert
            currency: Currency name in Arabic
            
        Returns:
            Number in Arabic words (basic implementation)
        """
        # This is a basic implementation - a full Arabic number converter would be much more complex
        integer_part = int(amount)
        
        if integer_part == 0:
            return f"صفر {currency}"
        
        # For now, fall back to numerals with Arabic currency
        return f"{integer_part} {currency}"
    
    @staticmethod
    def format_currency_mauritanian(amount: Decimal, language: str = "french") -> str:
        """
        Format currency according to Mauritanian conventions
        
        Args:
            amount: Amount to format
            language: "french" or "arabic"
            
        Returns:
            Formatted currency string
        """
        if language == "arabic":
            # Arabic formatting: number + currency
            formatted_number = f"{amount:,.2f}".replace(',', ' ').replace('.', '،')
            return f"{formatted_number} أ.م"  # Mauritanian Ouguiya abbreviation
        else:
            # French formatting: number + currency
            formatted_number = f"{amount:,.2f}".replace(',', ' ').replace('.', ',')
            return f"{formatted_number} MRU"
    
    @staticmethod
    def format_cnss_cnam_number_enhanced(number: str, type_: str = "cnss") -> str:
        """
        Enhanced formatting for CNSS and CNAM numbers with validation
        
        Args:
            number: The number to format
            type_: "cnss" or "cnam"
            
        Returns:
            Properly formatted number with validation
        """
        if not number:
            return ""
        
        # Remove any existing formatting
        clean_number = re.sub(r'[^0-9]', '', number)
        
        # Validate length
        if len(clean_number) not in [8, 9, 10]:
            return number  # Return original if invalid length
        
        # Format based on length and type
        if len(clean_number) == 9:
            return f"{clean_number[:3]} {clean_number[3:6]} {clean_number[6:9]}"
        elif len(clean_number) == 10:
            return f"{clean_number[:3]} {clean_number[3:6]} {clean_number[6:8]} {clean_number[8:10]}"
        elif len(clean_number) == 8:
            return f"{clean_number[:2]} {clean_number[2:5]} {clean_number[5:8]}"
        
        return number  # Return original if no formatting rule applies


class ExportUtilities:
    """
    Enhanced data export utilities for reports with comprehensive error handling
    """
    
    @staticmethod
    def to_csv_format_enhanced(data: List[Dict[str, Any]], 
                              field_mapping: Dict[str, str] = None,
                              delimiter: str = ";",
                              encoding: str = "utf-8-sig") -> str:
        """
        Enhanced CSV export with proper French formatting and encoding
        
        Args:
            data: List of data dictionaries
            field_mapping: Optional mapping of field names to headers
            delimiter: Field delimiter (default semicolon for French locale)
            encoding: Output encoding (default UTF-8 with BOM)
            
        Returns:
            CSV formatted string with proper encoding
        """
        if not data:
            return ""
        
        try:
            # Get headers
            headers = list(data[0].keys())
            if field_mapping:
                display_headers = [field_mapping.get(h, h) for h in headers]
            else:
                display_headers = headers
            
            lines = [delimiter.join(display_headers)]
            
            # Add data rows with proper formatting
            for row in data:
                values = []
                for key in headers:
                    value = row.get(key, "")
                    # Handle different data types with French formatting
                    if isinstance(value, Decimal):
                        formatted_value = str(value).replace('.', ',')
                    elif isinstance(value, (date, datetime)):
                        formatted_value = value.strftime("%d/%m/%Y")
                    elif isinstance(value, bool):
                        formatted_value = "Oui" if value else "Non"
                    elif value is None:
                        formatted_value = ""
                    else:
                        # Escape delimiter and quotes
                        formatted_value = str(value).replace(delimiter, f" {delimiter} ").replace('"', '""')
                        if delimiter in formatted_value or '"' in str(value) or '\n' in str(value):
                            formatted_value = f'"{formatted_value}"'
                    
                    values.append(formatted_value)
                
                lines.append(delimiter.join(values))
            
            return "\n".join(lines)
        
        except Exception as e:
            raise Exception(f"Erreur lors de l'export CSV: {str(e)}")
    
    @staticmethod
    def to_json_format_enhanced(data: Any, 
                               include_metadata: bool = True,
                               indent: int = 2) -> str:
        """
        Enhanced JSON export with metadata and proper serialization
        
        Args:
            data: Data to convert
            include_metadata: Whether to include export metadata
            indent: JSON indentation level
            
        Returns:
            JSON formatted string with metadata
        """
        try:
            def enhanced_serializer(obj):
                if isinstance(obj, Decimal):
                    return float(obj)
                if isinstance(obj, (date, datetime)):
                    return obj.isoformat()
                if hasattr(obj, '__dict__'):
                    return obj.__dict__
                raise TypeError(f"Object of type {type(obj)} is not JSON serializable")
            
            # Wrap data with metadata if requested
            if include_metadata:
                export_data = {
                    'metadata': {
                        'export_date': datetime.now().isoformat(),
                        'format': 'JSON Enhanced',
                        'version': '1.0',
                        'record_count': len(data) if isinstance(data, list) else 1
                    },
                    'data': data
                }
            else:
                export_data = data
            
            return json.dumps(export_data, default=enhanced_serializer, 
                            indent=indent, ensure_ascii=False, sort_keys=True)
        
        except Exception as e:
            raise Exception(f"Erreur lors de l'export JSON: {str(e)}")
    
    @staticmethod
    def validate_export_data(data: List[Dict[str, Any]], 
                           required_fields: List[str] = None) -> Dict[str, Any]:
        """
        Validate data before export
        
        Args:
            data: Data to validate
            required_fields: List of required field names
            
        Returns:
            Validation result with errors and warnings
        """
        validation_result = {
            'is_valid': True,
            'errors': [],
            'warnings': [],
            'statistics': {
                'total_records': len(data) if data else 0,
                'empty_records': 0,
                'incomplete_records': 0
            }
        }
        
        if not data:
            validation_result['errors'].append("Aucune donnée à exporter")
            validation_result['is_valid'] = False
            return validation_result
        
        required_fields = required_fields or []
        
        for i, record in enumerate(data):
            if not record:
                validation_result['statistics']['empty_records'] += 1
                validation_result['warnings'].append(f"Enregistrement vide à la ligne {i + 1}")
                continue
            
            # Check required fields
            missing_fields = [field for field in required_fields if not record.get(field)]
            if missing_fields:
                validation_result['statistics']['incomplete_records'] += 1
                validation_result['warnings'].append(
                    f"Ligne {i + 1}: Champs manquants - {', '.join(missing_fields)}"
                )
        
        # Set validation status
        if validation_result['errors']:
            validation_result['is_valid'] = False
        
        return validation_result