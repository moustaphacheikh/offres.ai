# report_utils.py
"""
Report generation utilities and data preparation
Converted from Java report generation logic and JasperReports functionality
"""

from decimal import Decimal
from datetime import date, datetime
from typing import Dict, List, Optional, Any, Union
import json


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
                'code': item.payroll_element.id,
                'label': item.payroll_element.label,
                'base': item.base_amount or Decimal('0.00'),
                'quantity': item.quantity or Decimal('1.00'),
                'amount': item.amount,
            }
            
            if item.payroll_element.type == 'G':
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
    """
    
    @staticmethod
    def prepare_bank_transfer_data(payroll_records: List[Dict],
                                 bank,
                                 period: date,
                                 system_params) -> Dict[str, Any]:
        """
        Prepare bank transfer report data
        
        Args:
            payroll_records: List of payroll records for transfer
            bank: Bank instance
            period: Transfer period
            system_params: System parameters
            
        Returns:
            Bank transfer data
        """
        # Header information
        header_data = {
            'company_name': system_params.company_name,
            'company_account': system_params.transfer_account,
            'bank_name': bank.name,
            'period': period,
            'transfer_date': datetime.now().date(),
            'reference': f"VIR-{period.strftime('%Y%m%d')}-{bank.id}",
        }
        
        # Transfer summary
        total_amount = sum(rec.get('net_salary', Decimal('0')) for rec in payroll_records)
        total_employees = len(payroll_records)
        
        summary_data = {
            'total_employees': total_employees,
            'total_amount': total_amount,
        }
        
        # Sort records by employee name
        sorted_records = sorted(payroll_records, key=lambda x: x.get('employee_name', ''))
        
        return {
            **header_data,
            **summary_data,
            'transfers': sorted_records,
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
    Report formatting utilities
    """
    
    @staticmethod
    def format_currency_for_report(amount: Decimal, currency: str = "MRU") -> str:
        """Format currency for report display"""
        if amount is None:
            return "0,00"
        
        formatted = f"{amount:,.2f}".replace(',', ' ').replace('.', ',')
        return f"{formatted} {currency}"
    
    @staticmethod
    def format_number_for_report(number: Union[int, Decimal], decimal_places: int = 2) -> str:
        """Format number for report display"""
        if number is None:
            return "0" + (",00" if decimal_places > 0 else "")
        
        if decimal_places == 0:
            return f"{int(number):,}".replace(',', ' ')
        else:
            formatted = f"{number:,.{decimal_places}f}".replace(',', ' ').replace('.', ',')
            return formatted
    
    @staticmethod
    def format_date_for_report(date_obj: Union[date, datetime], format_type: str = "french") -> str:
        """Format date for report display"""
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
        
        return date_obj.strftime("%d/%m/%Y")
    
    @staticmethod
    def format_percentage_for_report(value: Decimal) -> str:
        """Format percentage for report display"""
        if value is None:
            return "0,00%"
        
        percentage = value * 100
        return f"{percentage:.2f}%".replace('.', ',')


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
    Validate report data before generation
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
        
        # Required fields
        required_fields = [
            'first_name', 'last_name', 'period', 'net_salary'
        ]
        
        for field in required_fields:
            if field not in data or not data[field]:
                errors.append(f"Champ requis manquant: {field}")
        
        # Numeric validations
        if 'net_salary' in data and data['net_salary'] < 0:
            errors.append("Le salaire net ne peut pas être négatif")
        
        return errors
    
    @staticmethod
    def validate_declaration_data(data: Dict[str, Any], declaration_type: str) -> List[str]:
        """
        Validate tax declaration data
        
        Args:
            data: Declaration data dictionary
            declaration_type: "CNSS" or "CNAM"
            
        Returns:
            List of validation errors
        """
        errors = []
        
        # Check required company information
        if not data.get('company_name'):
            errors.append("Nom de l'entreprise requis")
        
        if declaration_type == "CNSS" and not data.get('company_cnss'):
            errors.append("Numéro CNSS de l'entreprise requis")
        
        if declaration_type == "CNAM" and not data.get('company_cnam'):
            errors.append("Numéro CNAM de l'entreprise requis")
        
        # Check employee data
        if not data.get('employees'):
            errors.append("Aucune données d'employé trouvées")
        
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