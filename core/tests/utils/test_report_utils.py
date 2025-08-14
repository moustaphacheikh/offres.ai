"""
Tests for core.utils.report_utils module.

This module provides comprehensive test coverage for the PayslipReportData,
DeclarationReportData, BankTransferReportData, AttendanceReportData,
ReportFormatter, ReportContext, ReportDataValidator, and ExportUtilities
classes that handle report generation and data preparation.
"""

import pytest
from decimal import Decimal
from datetime import date, datetime
from unittest.mock import Mock, MagicMock
import json

from core.utils.report_utils import (
    PayslipReportData,
    DeclarationReportData,
    BankTransferReportData,
    AttendanceReportData,
    ReportFormatter,
    ReportContext,
    ReportDataValidator,
    ExportUtilities
)


class MockSystemParams:
    """Mock system parameters for testing"""
    
    def __init__(self):
        self.company_name = "Test Company SARL"
        self.company_activity = "Information Technology"
        self.address = "123 Avenue Test, Nouakchott"
        self.phone = "22312345678"
        self.logo = b"fake_logo_data"
        self.cnss_registration = "CNS123456"
        self.cnam_registration = "CNM789012"
        self.its_registration = "ITS345678"


class MockEmployee:
    """Mock employee for testing"""
    
    def __init__(self):
        self.id = 1
        self.first_name = "Ahmed"
        self.last_name = "Mohamed"
        self.national_id = "1234567890"
        self.cnss_number = "CNS001234"
        self.cnam_number = "CNM001234"
        self.hire_date = date(2020, 1, 15)
        
        # Mock related objects
        self.position = Mock()
        self.position.name = "Software Engineer"
        
        self.department = Mock()
        self.department.name = "IT Department"
        
        self.direction = Mock()
        self.direction.name = "Technical Direction"
        
        self.general_direction = Mock()
        self.general_direction.name = "Operations"
        
        self.salary_grade = Mock()
        self.salary_grade.category = "Grade A"


class TestReportFormatter:
    """Test ReportFormatter class methods"""
    
    def test_format_currency_for_report_basic(self):
        """Test basic currency formatting for reports"""
        result = ReportFormatter.format_currency_for_report(Decimal('1234.56'))
        assert result == "1 234,56 MRU"
        
        result = ReportFormatter.format_currency_for_report(Decimal('1000000.00'))
        assert result == "1 000 000,00 MRU"
    
    def test_format_currency_for_report_different_currency(self):
        """Test currency formatting with different currency"""
        result = ReportFormatter.format_currency_for_report(Decimal('1234.56'), "USD")
        assert result == "1 234,56 USD"
    
    def test_format_currency_for_report_zero(self):
        """Test currency formatting with zero amount"""
        result = ReportFormatter.format_currency_for_report(Decimal('0.00'))
        assert result == "0,00 MRU"
    
    def test_format_number_for_report_basic(self):
        """Test basic number formatting for reports"""
        result = ReportFormatter.format_number_for_report(Decimal('1234.56'))
        assert result == "1 234,56"
        
        result = ReportFormatter.format_number_for_report(Decimal('1000000.123'), 3)
        assert result == "1 000 000,123"
    
    def test_format_number_for_report_zero_decimals(self):
        """Test number formatting with zero decimal places"""
        result = ReportFormatter.format_number_for_report(Decimal('1234.56'), 0)
        assert result == "1 235"  # Should round
    
    def test_format_date_for_report_french(self):
        """Test French date formatting for reports"""
        test_date = date(2023, 6, 15)
        result = ReportFormatter.format_date_for_report(test_date, "french")
        assert result == "15 juin 2023"
        
        # Test all months
        months = [
            "janvier", "février", "mars", "avril", "mai", "juin",
            "juillet", "août", "septembre", "octobre", "novembre", "décembre"
        ]
        
        for month_num, month_name in enumerate(months, 1):
            test_date = date(2023, month_num, 1)
            result = ReportFormatter.format_date_for_report(test_date, "french")
            assert month_name in result.lower()
    
    def test_format_date_for_report_short(self):
        """Test short date formatting for reports"""
        test_date = date(2023, 6, 15)
        result = ReportFormatter.format_date_for_report(test_date, "short")
        assert result == "15/06/2023"
    
    def test_format_date_for_report_datetime(self):
        """Test date formatting with datetime input"""
        test_datetime = datetime(2023, 6, 15, 14, 30, 0)
        result = ReportFormatter.format_date_for_report(test_datetime, "short")
        assert result == "15/06/2023"
    
    def test_format_percentage_for_report(self):
        """Test percentage formatting for reports"""
        result = ReportFormatter.format_percentage_for_report(Decimal('0.15'))
        assert result == "15,00%"
        
        result = ReportFormatter.format_percentage_for_report(Decimal('0.12345'))
        assert result == "12,35%"  # Should round to 2 decimal places


class TestExportUtilities:
    """Test ExportUtilities class methods"""
    
    def test_to_csv_format_basic(self):
        """Test basic CSV format conversion"""
        data = [
            {'name': 'Ahmed', 'salary': Decimal('50000.00'), 'date': date(2023, 6, 15)},
            {'name': 'Fatima', 'salary': Decimal('45000.00'), 'date': date(2023, 6, 16)}
        ]
        
        result = ExportUtilities.to_csv_format(data)
        
        # Verify CSV structure with semicolon separator
        lines = result.strip().split('\n')
        assert len(lines) == 3  # Header + 2 data rows
        
        # Check header
        assert lines[0] == "name;salary;date"
        
        # Check data rows
        assert "Ahmed;50000.00;2023-06-15" in lines[1]
        assert "Fatima;45000.00;2023-06-16" in lines[2]
    
    def test_to_csv_format_with_field_mapping(self):
        """Test CSV format conversion with field mapping"""
        data = [
            {'name': 'Ahmed', 'salary': Decimal('50000.00')},
            {'name': 'Fatima', 'salary': Decimal('45000.00')}
        ]
        
        field_mapping = {
            'name': 'Nom',
            'salary': 'Salaire'
        }
        
        result = ExportUtilities.to_csv_format(data, field_mapping)
        
        lines = result.strip().split('\n')
        
        # Check mapped header
        assert lines[0] == "Nom;Salaire"
        
        # Data should remain the same
        assert "Ahmed;50000.00" in lines[1]
    
    def test_to_csv_format_empty_data(self):
        """Test CSV format conversion with empty data"""
        result = ExportUtilities.to_csv_format([])
        assert result == ""
    
    def test_to_csv_format_with_datetime(self):
        """Test CSV format conversion with datetime objects"""
        data = [
            {
                'name': 'Ahmed',
                'created_at': datetime(2023, 6, 15, 10, 30, 0),
                'date': date(2023, 6, 15)
            }
        ]
        
        result = ExportUtilities.to_csv_format(data)
        
        # Should format datetime and date properly
        assert "2023-06-15 10:30:00" in result
        assert "2023-06-15" in result
    
    def test_to_json_format_basic(self):
        """Test basic JSON format conversion"""
        data = [
            {'name': 'Ahmed', 'salary': Decimal('50000.00'), 'date': date(2023, 6, 15)},
            {'name': 'Fatima', 'salary': Decimal('45000.00'), 'date': date(2023, 6, 16)}
        ]
        
        result = ExportUtilities.to_json_format(data)
        
        # Should be valid JSON
        parsed = json.loads(result)
        assert len(parsed) == 2
        
        # Check first record
        assert parsed[0]['name'] == 'Ahmed'
        assert parsed[0]['salary'] == '50000.00'  # Decimal as string
        assert parsed[0]['date'] == '2023-06-15'  # Date as string
    
    def test_to_json_format_with_datetime(self):
        """Test JSON format conversion with datetime objects"""
        data = [
            {
                'name': 'Ahmed',
                'created_at': datetime(2023, 6, 15, 10, 30, 0),
                'date': date(2023, 6, 15)
            }
        ]
        
        result = ExportUtilities.to_json_format(data)
        parsed = json.loads(result)
        
        # Should format datetime and date as ISO strings
        assert parsed[0]['created_at'] == '2023-06-15T10:30:00'
        assert parsed[0]['date'] == '2023-06-15'
    
    def test_to_json_format_empty_data(self):
        """Test JSON format conversion with empty data"""
        result = ExportUtilities.to_json_format([])
        assert result == "[]"


class TestReportDataValidator:
    """Test ReportDataValidator class methods"""
    
    def test_validate_payslip_data_valid(self):
        """Test payslip data validation with valid data"""
        valid_data = {
            'employee_data': {
                'employee_id': 1,
                'first_name': 'Ahmed',
                'last_name': 'Mohamed',
                'full_name': 'Ahmed Mohamed'
            },
            'payroll_data': {
                'period': '202306',
                'net_salary': Decimal('42500.00'),
                'gross_taxable': Decimal('50000.00')
            },
            'company_data': {
                'company_name': 'Test Company'
            }
        }
        
        errors = ReportDataValidator.validate_payslip_data(valid_data)
        assert errors == []
    
    def test_validate_payslip_data_missing_fields(self):
        """Test payslip data validation with missing fields"""
        invalid_data = {
            'employee_data': {
                'employee_id': 1,
                # Missing first_name, last_name
            },
            'payroll_data': {
                # Missing period, net_salary
                'gross_taxable': Decimal('50000.00')
            },
            # Missing company_data
        }
        
        errors = ReportDataValidator.validate_payslip_data(invalid_data)
        assert len(errors) > 0
        
        # Check for specific error messages (in French)
        error_text = ' '.join(errors)
        assert "Nom de l'employé manquant" in error_text
        assert "Période manquante" in error_text
        assert "Salaire net manquant" in error_text
    
    def test_validate_payslip_data_negative_salary(self):
        """Test payslip data validation with negative salary"""
        data_with_negative = {
            'employee_data': {
                'employee_id': 1,
                'first_name': 'Ahmed',
                'last_name': 'Mohamed',
                'full_name': 'Ahmed Mohamed'
            },
            'payroll_data': {
                'period': '202306',
                'net_salary': Decimal('-1000.00'),  # Negative salary
                'gross_taxable': Decimal('50000.00')
            },
            'company_data': {
                'company_name': 'Test Company'
            }
        }
        
        errors = ReportDataValidator.validate_payslip_data(data_with_negative)
        assert len(errors) > 0
        assert "Le salaire net ne peut pas être négatif" in ' '.join(errors)
    
    def test_validate_declaration_data_cnss_valid(self):
        """Test CNSS declaration validation with valid data"""
        valid_data = {
            'header': {
                'company_name': 'Test Company',
                'cnss_registration': 'CNS123456',
                'period': '202306'
            },
            'employees': [
                {'employee_name': 'Ahmed Mohamed', 'cnss_number': 'CNS001234'}
            ]
        }
        
        errors = ReportDataValidator.validate_declaration_data(valid_data, 'CNSS')
        assert errors == []
    
    def test_validate_declaration_data_cnss_missing_fields(self):
        """Test CNSS declaration validation with missing fields"""
        invalid_data = {
            'header': {
                # Missing company_name
                'cnss_registration': 'CNS123456',
                # Missing period
            },
            'employees': []  # Empty employees list
        }
        
        errors = ReportDataValidator.validate_declaration_data(invalid_data, 'CNSS')
        assert len(errors) > 0
        
        error_text = ' '.join(errors)
        assert "Nom de l'entreprise manquant" in error_text
        assert "Numéro d'enregistrement CNSS manquant" in error_text
        assert "Aucun employé dans la déclaration" in error_text
    
    def test_validate_declaration_data_cnam_valid(self):
        """Test CNAM declaration validation with valid data"""
        valid_data = {
            'header': {
                'company_name': 'Test Company',
                'cnam_registration': 'CNM789012',
                'period': '202306'
            },
            'employees': [
                {'employee_name': 'Ahmed Mohamed', 'cnam_number': 'CNM001234'}
            ]
        }
        
        errors = ReportDataValidator.validate_declaration_data(valid_data, 'CNAM')
        assert errors == []


class TestReportUtilsIntegration:
    """Integration tests for report utilities"""
    
    def test_formatting_consistency_across_reports(self):
        """Test formatting consistency across different report types"""
        test_amount = Decimal('12345.67')
        test_date = date(2023, 6, 15)
        test_percentage = Decimal('0.15')
        
        # Test formatting consistency
        currency_format = ReportFormatter.format_currency_for_report(test_amount)
        number_format = ReportFormatter.format_number_for_report(test_amount)
        date_format = ReportFormatter.format_date_for_report(test_date, "french")
        percentage_format = ReportFormatter.format_percentage_for_report(test_percentage)
        
        # Verify French formatting standards
        assert "12 345,67" in currency_format
        assert "12 345,67" in number_format
        assert "15 juin 2023" in date_format
        assert "15,00%" in percentage_format
    
    def test_error_handling_across_modules(self):
        """Test error handling across all report utility modules"""
        # Test with None/invalid inputs
        try:
            # Should handle None gracefully
            ReportFormatter.format_currency_for_report(None)
            ReportFormatter.format_date_for_report(None)
            
            # Empty data should work
            csv_result = ExportUtilities.to_csv_format([])
            json_result = ExportUtilities.to_json_format([])
            
            assert csv_result == ""
            assert json_result == "[]"
            
            # Validation should catch missing data
            errors = ReportDataValidator.validate_payslip_data({})
            assert len(errors) > 0
            
        except Exception as e:
            pytest.fail(f"Report utilities should handle errors gracefully, but got: {e}")


# Simplified tests for classes that may not be fully implemented
class TestOtherReportClasses:
    """Test other report utility classes with basic functionality"""
    
    def test_payslip_report_data_exists(self):
        """Test that PayslipReportData class exists and has expected method"""
        assert hasattr(PayslipReportData, 'prepare_payslip_data')
    
    def test_declaration_report_data_exists(self):
        """Test that DeclarationReportData class exists and has expected methods"""
        assert hasattr(DeclarationReportData, 'prepare_cnss_declaration_data')
        assert hasattr(DeclarationReportData, 'prepare_cnam_declaration_data')
    
    def test_bank_transfer_report_data_exists(self):
        """Test that BankTransferReportData class exists and has expected method"""
        assert hasattr(BankTransferReportData, 'prepare_bank_transfer_data')
    
    def test_attendance_report_data_exists(self):
        """Test that AttendanceReportData class exists and has expected method"""
        assert hasattr(AttendanceReportData, 'prepare_attendance_report_data')
    
    def test_report_context_exists(self):
        """Test that ReportContext class exists and has expected method"""
        assert hasattr(ReportContext, 'prepare_base_context')


class TestPayslipReportDataImplementation:
    """Test actual PayslipReportData implementation"""
    
    def setup_method(self):
        """Set up test fixtures"""
        self.system_params = MockSystemParams()
        self.employee = MockEmployee()
        
        # Mock payroll data
        self.payroll = Mock()
        self.payroll.period = date(2023, 6, 1)
        self.payroll.period_in_words = "Juin 2023"
        self.payroll.category = "Regular"
        self.payroll.contract_hours_month = 173
        self.payroll.worked_days = 22
        self.payroll.overtime_hours = Decimal('5.0')
        self.payroll.gross_taxable = Decimal('45000.00')
        self.payroll.gross_non_taxable = Decimal('5000.00')
        self.payroll.net_salary = Decimal('38500.00')
        self.payroll.cnss_employee = Decimal('450.00')
        self.payroll.cnam_employee = Decimal('2000.00')
        self.payroll.its_total = Decimal('4050.00')
        self.payroll.its_tranche1 = Decimal('1350.00')
        self.payroll.its_tranche2 = Decimal('1800.00')
        self.payroll.its_tranche3 = Decimal('900.00')
        self.payroll.gross_deductions = Decimal('1000.00')
        self.payroll.net_deductions = Decimal('500.00')
        self.payroll.bank_name = "Bank Al-Maghrib"
        self.payroll.bank_account = "1234567890"
        self.payroll.payment_mode = "Virement"
        self.payroll.is_domiciled = True
        self.payroll.net_in_words = "Trente-huit mille cinq cents ouguiyas"
        
        # Mock line items
        self.line_items = []
        
        # Gain item
        gain_item = Mock()
        gain_item.payroll_element = Mock()
        gain_item.payroll_element.name = "Salaire de base"
        gain_item.payroll_element.type = 'G'
        gain_item.payroll_element.calculation_order = 1
        gain_item.amount = Decimal('40000.00')
        gain_item.rate = Decimal('1.0')
        gain_item.base_amount = Decimal('40000.00')
        gain_item.quantity = Decimal('1.0')
        gain_item.formula_result = "40000 * 1.0 = 40000"
        self.line_items.append(gain_item)
        
        # Deduction item
        deduction_item = Mock()
        deduction_item.payroll_element = Mock()
        deduction_item.payroll_element.name = "Avance sur salaire"
        deduction_item.payroll_element.type = 'R'
        deduction_item.payroll_element.calculation_order = 10
        deduction_item.amount = Decimal('2000.00')
        deduction_item.rate = Decimal('1.0')
        deduction_item.base_amount = Decimal('2000.00')
        deduction_item.quantity = Decimal('1.0')
        deduction_item.formula_result = "2000 * 1.0 = 2000"
        self.line_items.append(deduction_item)
    
    def test_prepare_payslip_data_basic(self):
        """Test basic payslip data preparation"""
        result = PayslipReportData.prepare_payslip_data(
            self.employee, self.payroll, self.line_items, self.system_params
        )
        
        # Check main sections exist
        assert 'company_data' in result
        assert 'employee_data' in result
        assert 'payroll_data' in result
        assert 'gains' in result
        assert 'deductions' in result
        
        # Check company data
        company = result['company_data']
        assert company['company_name'] == "Test Company SARL"
        assert company['company_activity'] == "Information Technology"
        
        # Check employee data
        employee = result['employee_data']
        assert employee['full_name'] == "Ahmed Mohamed"
        assert employee['position'] == "Software Engineer"
        assert employee['cnss_number'] == "CNS001234"
        
        # Check payroll data
        payroll = result['payroll_data']
        assert payroll['net_salary'] == Decimal('38500.00')
        assert payroll['its_total'] == Decimal('4050.00')
        assert payroll['bank_name'] == "Bank Al-Maghrib"
    
    def test_prepare_payslip_data_line_items_processing(self):
        """Test payslip line items are properly processed"""
        result = PayslipReportData.prepare_payslip_data(
            self.employee, self.payroll, self.line_items, self.system_params
        )
        
        # Check gains
        gains = result['gains']
        assert len(gains) >= 1
        gain_found = False
        for gain in gains:
            if gain['name'] == "Salaire de base":
                gain_found = True
                assert gain['amount'] == Decimal('40000.00')
                assert gain['calculation_order'] == 1
        assert gain_found, "Base salary gain not found in gains list"
        
        # Check deductions
        deductions = result['deductions']
        assert len(deductions) >= 1
        deduction_found = False
        for deduction in deductions:
            if deduction['name'] == "Avance sur salaire":
                deduction_found = True
                assert deduction['amount'] == Decimal('2000.00')
                assert deduction['calculation_order'] == 10
        assert deduction_found, "Salary advance deduction not found in deductions list"


class TestDeclarationReportDataImplementation:
    """Test DeclarationReportData implementation"""
    
    def setup_method(self):
        """Set up test fixtures"""
        self.system_params = MockSystemParams()
        
        # Mock employees with declarations
        self.employees = []
        for i in range(3):
            employee = Mock()
            employee.id = i + 1
            employee.first_name = f"Employee{i+1}"
            employee.last_name = "Test"
            employee.cnss_number = f"CNS00{i+1}"
            employee.cnam_number = f"CNM00{i+1}"
            
            # Mock declaration data
            employee.cnss_declaration = Mock()
            employee.cnss_declaration.working_days_month1 = 22
            employee.cnss_declaration.working_days_month2 = 21
            employee.cnss_declaration.working_days_month3 = 23
            employee.cnss_declaration.actual_remuneration = Decimal(f'{35000 + i*5000}')
            
            employee.cnam_declaration = Mock()
            employee.cnam_declaration.taxable_base_month1 = Decimal(f'{12000 + i*2000}')
            employee.cnam_declaration.taxable_base_month2 = Decimal(f'{12500 + i*2000}')
            employee.cnam_declaration.taxable_base_month3 = Decimal(f'{13000 + i*2000}')
            
            self.employees.append(employee)
    
    def test_prepare_cnss_declaration_data(self):
        """Test CNSS declaration data preparation"""
        period = "202306"
        
        result = DeclarationReportData.prepare_cnss_declaration_data(
            self.employees, period, self.system_params
        )
        
        # Check header information
        assert 'header' in result
        header = result['header']
        assert header['company_name'] == "Test Company SARL"
        assert header['period'] == period
        assert header['cnss_registration'] == "CNS123456"
        assert 'declaration_date' in header
        
        # Check employee declarations
        assert 'employees' in result
        employees = result['employees']
        assert len(employees) == 3
        
        # Check first employee
        emp1 = employees[0]
        assert emp1['employee_name'] == "Employee1 Test"
        assert emp1['cnss_number'] == "CNS001"
        assert emp1['total_working_days'] == 66  # 22 + 21 + 23
        assert emp1['actual_remuneration'] == Decimal('35000')
    
    def test_prepare_cnam_declaration_data(self):
        """Test CNAM declaration data preparation"""
        period = "202306"
        
        result = DeclarationReportData.prepare_cnam_declaration_data(
            self.employees, period, self.system_params
        )
        
        # Check header information
        assert 'header' in result
        header = result['header']
        assert header['company_name'] == "Test Company SARL"
        assert header['period'] == period
        assert header['cnam_registration'] == "CNM789012"
        
        # Check employee declarations
        assert 'employees' in result
        employees = result['employees']
        assert len(employees) == 3
        
        # Check first employee
        emp1 = employees[0]
        assert emp1['employee_name'] == "Employee1 Test"
        assert emp1['cnam_number'] == "CNM001"
        assert emp1['total_taxable_base'] == Decimal('37500')  # 12000 + 12500 + 13000


class TestBankTransferReportDataImplementation:
    """Test BankTransferReportData implementation"""
    
    def setup_method(self):
        """Set up test fixtures"""
        self.system_params = MockSystemParams()
        
        # Mock payrolls with bank data
        self.payrolls = []
        banks = ["Bank Al-Maghrib", "BMCI", "Société Générale"]
        for i in range(3):
            payroll = Mock()
            payroll.employee = Mock()
            payroll.employee.first_name = f"Employee{i+1}"
            payroll.employee.last_name = "Test"
            payroll.employee.id = i + 1
            payroll.net_salary = Decimal(f'{35000 + i*5000}')
            payroll.bank_name = banks[i]
            payroll.bank_account = f"123456789{i}"
            payroll.period = date(2023, 6, 1)
            payroll.is_domiciled = True
            self.payrolls.append(payroll)
    
    def test_prepare_bank_transfer_data(self):
        """Test bank transfer data preparation"""
        period = "202306"
        
        result = BankTransferReportData.prepare_bank_transfer_data(
            self.payrolls, period, self.system_params
        )
        
        # Check header information
        assert 'header' in result
        header = result['header']
        assert header['company_name'] == "Test Company SARL"
        assert header['period'] == period
        assert 'total_amount' in header
        assert 'total_employees' in header
        assert header['total_employees'] == 3
        
        # Check transfers by bank
        assert 'transfers_by_bank' in result
        transfers = result['transfers_by_bank']
        
        # Should have transfers for 3 different banks
        bank_names = set()
        for transfer_group in transfers:
            bank_names.add(transfer_group['bank_name'])
        assert len(bank_names) == 3
        
        # Check individual transfers
        assert 'individual_transfers' in result
        individual = result['individual_transfers']
        assert len(individual) == 3
        
        # Check first transfer
        transfer1 = individual[0]
        assert transfer1['employee_name'] == "Employee1 Test"
        assert transfer1['net_salary'] == Decimal('35000')
        assert transfer1['bank_account'] == "1234567890"


class TestAttendanceReportDataImplementation:
    """Test AttendanceReportData implementation"""
    
    def setup_method(self):
        """Set up test fixtures"""
        self.system_params = MockSystemParams()
        
        # Mock attendance data
        self.attendance_records = []
        for i in range(5):  # 5 days
            for j in range(3):  # 3 employees
                record = Mock()
                record.employee = Mock()
                record.employee.first_name = f"Employee{j+1}"
                record.employee.last_name = "Test"
                record.employee.id = j + 1
                record.work_date = date(2023, 6, i+1)
                record.day_hours = Decimal('8.0') if i < 4 else Decimal('4.0')  # Half day on last day
                record.night_hours = Decimal('0.0')
                record.overtime_hours = Decimal('2.0') if i == 2 else Decimal('0.0')  # OT on day 3
                record.total_hours = record.day_hours + record.night_hours + record.overtime_hours
                self.attendance_records.append(record)
    
    def test_prepare_attendance_report_data(self):
        """Test attendance report data preparation"""
        start_date = date(2023, 6, 1)
        end_date = date(2023, 6, 5)
        
        result = AttendanceReportData.prepare_attendance_report_data(
            self.attendance_records, start_date, end_date, self.system_params
        )
        
        # Check header information
        assert 'header' in result
        header = result['header']
        assert header['company_name'] == "Test Company SARL"
        assert header['start_date'] == start_date
        assert header['end_date'] == end_date
        
        # Check summary statistics
        assert 'summary' in result
        summary = result['summary']
        assert summary['total_employees'] == 3
        assert summary['total_work_days'] == 5
        assert 'average_hours_per_day' in summary
        assert 'total_overtime_hours' in summary
        
        # Check daily breakdown
        assert 'daily_breakdown' in result
        daily = result['daily_breakdown']
        assert len(daily) == 5  # 5 days
        
        # Check employee summary
        assert 'employee_summary' in result
        employees = result['employee_summary']
        assert len(employees) == 3  # 3 employees
        
        # Check first employee summary
        emp1 = employees[0]
        assert emp1['employee_name'] == "Employee1 Test"
        assert 'total_hours' in emp1
        assert 'total_days_worked' in emp1


class TestReportContextImplementation:
    """Test ReportContext implementation"""
    
    def setup_method(self):
        """Set up test fixtures"""
        self.system_params = MockSystemParams()
    
    def test_prepare_base_context(self):
        """Test base context preparation"""
        context_type = "payslip"
        
        result = ReportContext.prepare_base_context(context_type, self.system_params)
        
        # Check base context elements
        assert 'report_type' in result
        assert result['report_type'] == context_type
        
        assert 'generation_timestamp' in result
        assert 'company_info' in result
        
        # Check company info
        company = result['company_info']
        assert company['name'] == "Test Company SARL"
        assert company['activity'] == "Information Technology"
        assert company['address'] == "123 Avenue Test, Nouakchott"
        
        # Check formatting helpers
        assert 'formatters' in result
        formatters = result['formatters']
        assert 'currency' in formatters
        assert 'date' in formatters
        assert 'number' in formatters


class TestExportUtilitiesImplementation:
    """Test ExportUtilities implementation"""
    
    def setup_method(self):
        """Set up test data"""
        self.test_data = [
            {'name': 'Ahmed Mohamed', 'salary': 45000, 'department': 'IT'},
            {'name': 'Fatima Hassan', 'salary': 38000, 'department': 'Finance'},
            {'name': 'Omar Ali', 'salary': 42000, 'department': 'HR'}
        ]
    
    def test_to_csv_format_with_data(self):
        """Test CSV export with actual data"""
        result = ExportUtilities.to_csv_format(self.test_data)
        
        # Should have header and data rows
        lines = result.strip().split('\n')
        assert len(lines) == 4  # Header + 3 data rows
        
        # Check header
        assert lines[0] == 'name,salary,department'
        
        # Check first data row
        assert 'Ahmed Mohamed' in lines[1]
        assert '45000' in lines[1]
        assert 'IT' in lines[1]
    
    def test_to_json_format_with_data(self):
        """Test JSON export with actual data"""
        result = ExportUtilities.to_json_format(self.test_data)
        
        # Should be valid JSON
        parsed = json.loads(result)
        assert len(parsed) == 3
        assert parsed[0]['name'] == 'Ahmed Mohamed'
        assert parsed[0]['salary'] == 45000
    
    def test_to_xml_format_with_data(self):
        """Test XML export with actual data"""
        result = ExportUtilities.to_xml_format(self.test_data, 'employees', 'employee')
        
        # Should contain XML structure
        assert '<employees>' in result
        assert '</employees>' in result
        assert '<employee>' in result
        assert '</employee>' in result
        assert 'Ahmed Mohamed' in result
    
    def test_to_excel_format_with_data(self):
        """Test Excel export format preparation"""
        result = ExportUtilities.to_excel_format(self.test_data, 'Employee Report')
        
        # Should return data structure suitable for Excel export
        assert isinstance(result, dict)
        assert 'data' in result
        assert 'headers' in result
        assert 'title' in result
        assert result['title'] == 'Employee Report'
        assert len(result['data']) == 3


class TestErrorHandlingAndEdgeCases:
    """Test error handling and edge cases across all report utilities"""
    
    def test_handle_none_inputs_gracefully(self):
        """Test that utilities handle None inputs gracefully"""
        # Format functions should handle None
        assert ReportFormatter.format_currency_for_report(None) == "0,00 MRU"
        assert ReportFormatter.format_number_for_report(None) == "0"
        assert ReportFormatter.format_date_for_report(None) == ""
        assert ReportFormatter.format_percentage_for_report(None) == "0,00%"
    
    def test_handle_empty_collections(self):
        """Test handling of empty data collections"""
        # Export functions should handle empty data
        assert ExportUtilities.to_csv_format([]) == ""
        assert ExportUtilities.to_json_format([]) == "[]"
        assert ExportUtilities.to_xml_format([], 'root', 'item') == "<root></root>"
        
        # Report preparation should handle empty collections
        empty_result = PayslipReportData.prepare_payslip_data(
            Mock(), Mock(), [], MockSystemParams()
        )
        assert 'gains' in empty_result
        assert 'deductions' in empty_result
        assert len(empty_result['gains']) == 0
        assert len(empty_result['deductions']) == 0
    
    def test_handle_invalid_types(self):
        """Test handling of invalid data types"""
        # Should not crash on invalid types
        try:
            ReportFormatter.format_currency_for_report("invalid")
            ReportFormatter.format_number_for_report("invalid")
        except Exception:
            # It's okay to throw exceptions for invalid types
            pass
    
    def test_validation_comprehensive_checks(self):
        """Test comprehensive validation across different scenarios"""
        # Test with minimal valid data
        minimal_data = {
            'employee_data': {'employee_id': 1, 'first_name': 'Test', 'last_name': 'User'},
            'payroll_data': {'net_salary': Decimal('1000'), 'period': '202306'},
            'company_data': {'company_name': 'Test Company'}
        }
        
        errors = ReportDataValidator.validate_payslip_data(minimal_data)
        assert len(errors) == 0  # Should be valid
        
        # Test with completely empty data
        empty_errors = ReportDataValidator.validate_payslip_data({})
        assert len(empty_errors) > 0  # Should have validation errors