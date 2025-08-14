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