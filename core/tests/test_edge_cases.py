"""
Simple edge case tests to improve code coverage.
These tests target specific lines that are not covered by the main test suites.
"""

import pytest
from decimal import Decimal
from datetime import date, datetime
from unittest.mock import Mock, patch

from core.utils.formula_engine import FormulaEngine, FormulaCalculationError
from core.utils.date_utils import DateCalculator, PayrollPeriodUtils
from core.utils.text_utils import NumberToTextConverter, TextFormatter
from core.utils.report_utils import PayslipReportData, DeclarationReportData


class TestFormulaEngineEdgeCases:
    """Test edge cases for formula engine to improve coverage"""
    
    def test_formula_engine_division_by_zero(self):
        """Test division by zero handling"""
        engine = FormulaEngine()
        
        with pytest.raises(FormulaCalculationError):
            engine.calculate("10 / 0")
    
    def test_formula_engine_invalid_syntax(self):
        """Test invalid formula syntax"""
        engine = FormulaEngine()
        
        with pytest.raises(FormulaCalculationError):
            engine.calculate("10 + + 5")
    
    def test_formula_engine_empty_formula(self):
        """Test empty formula raises error"""
        engine = FormulaEngine()
        
        with pytest.raises(FormulaCalculationError):
            engine.calculate("")
    
    def test_formula_engine_complex_expression(self):
        """Test complex mathematical expression"""
        engine = FormulaEngine()
        
        result = engine.calculate("(10 + 5) * 2 - 8 / 4")
        assert result == Decimal('28')
    
    def test_formula_engine_subtraction(self):
        """Test formula engine subtraction operation"""
        engine = FormulaEngine()
        
        result = engine.calculate("20 - 5")
        assert result == Decimal('15')
    
    def test_formula_engine_multiplication(self):
        """Test formula engine multiplication operation"""
        engine = FormulaEngine()
        
        result = engine.calculate("6 * 7")
        assert result == Decimal('42')
    
    def test_formula_engine_division(self):
        """Test formula engine division operation"""
        engine = FormulaEngine()
        
        result = engine.calculate("20 / 4")
        assert result == Decimal('5')
    
    def test_formula_engine_parentheses(self):
        """Test formula engine with parentheses"""
        engine = FormulaEngine()
        
        result = engine.calculate("(5 + 3) * 2")
        assert result == Decimal('16')
    
    def test_formula_engine_negative_number(self):
        """Test formula engine with negative expression"""
        engine = FormulaEngine()
        
        result = engine.calculate("0 - 5 + 10")
        assert result == Decimal('5')
    
    def test_formula_engine_decimal_numbers(self):
        """Test formula engine with decimal numbers"""
        engine = FormulaEngine()
        
        result = engine.calculate("10.5 + 4.25")
        assert result == Decimal('14.75')


class TestDateUtilsEdgeCases:
    """Test edge cases for date utils to improve coverage"""
    
    def test_payroll_period_edge_month(self):
        """Test payroll period calculation for edge months"""
        # Test December to January transition
        dec_date = date(2023, 12, 15)
        next_period = PayrollPeriodUtils.get_next_period(dec_date)
        assert next_period.year == 2024
        assert next_period.month == 1
    
    def test_payroll_period_february_leap_year(self):
        """Test payroll period for February in leap year"""
        feb_date = date(2024, 2, 29)  # Leap year
        start, end = PayrollPeriodUtils.get_period_start_end(feb_date)
        assert start.month == 2
        assert end.month == 2
    
    def test_date_calculator_with_datetime_objects(self):
        """Test DateCalculator methods with datetime objects"""
        base_datetime = datetime(2023, 6, 15, 10, 30, 0)
        
        # Test add_days with datetime
        result = DateCalculator.add_days(base_datetime, 5)
        assert result == date(2023, 6, 20)
        
        # Test add_months with datetime
        result = DateCalculator.add_months(base_datetime, 2)
        assert result == date(2023, 8, 15)
        
        # Test add_years with datetime
        result = DateCalculator.add_years(base_datetime, 1)
        assert result == date(2024, 6, 15)
    
    def test_get_days_between_with_datetime(self):
        """Test get_days_between with datetime objects"""
        start_dt = datetime(2023, 1, 1, 10, 0, 0)
        end_dt = datetime(2023, 1, 11, 15, 30, 0)
        
        result = DateCalculator.get_days_between(start_dt, end_dt)
        assert result == 10
    
    def test_get_months_between_with_datetime(self):
        """Test get_months_between with datetime objects"""
        start_dt = datetime(2023, 1, 15, 10, 0, 0)
        end_dt = datetime(2023, 6, 15, 15, 30, 0)
        
        result = DateCalculator.get_months_between(start_dt, end_dt)
        assert result == 5
    
    def test_get_years_between_with_datetime(self):
        """Test get_years_between with datetime objects"""
        start_dt = datetime(2020, 6, 15, 10, 0, 0)
        end_dt = datetime(2023, 6, 15, 15, 30, 0)
        
        result = DateCalculator.get_years_between(start_dt, end_dt)
        assert result == 3


class TestTextUtilsEdgeCases:
    """Test edge cases for text utils to improve coverage"""
    
    def test_number_to_text_large_number(self):
        """Test number to text conversion for large numbers"""
        converter = NumberToTextConverter()
        result = converter.convert_to_mru(Decimal('9999999.99'))
        assert isinstance(result, str)
        assert len(result) > 0
    
    def test_number_to_text_zero(self):
        """Test number to text conversion for zero"""
        converter = NumberToTextConverter()
        result = converter.convert_to_mru(Decimal('0.00'))
        assert isinstance(result, str)
    
    def test_text_formatter_edge_cases(self):
        """Test TextFormatter edge cases"""
        formatter = TextFormatter()
        
        # Test with various input types
        result = formatter.format_currency(Decimal('1234.56'))
        assert isinstance(result, str)
        
        result = formatter.format_percentage(Decimal('45.67'))
        assert isinstance(result, str)
    
    def test_number_converter_different_amounts(self):
        """Test NumberToTextConverter with different amounts"""
        converter = NumberToTextConverter()
        
        # Test small amount
        result = converter.convert_to_mru(Decimal('1.50'))
        assert isinstance(result, str)
        
        # Test medium amount
        result = converter.convert_to_mru(Decimal('1234.56'))
        assert isinstance(result, str)
        
        # Test larger amount
        result = converter.convert_to_mru(Decimal('999999.99'))
        assert isinstance(result, str)


class TestReportUtilsEdgeCases:
    """Test edge cases for report utils to improve coverage"""
    
    def test_payslip_report_data_none_values(self):
        """Test payslip report data with None values"""
        employee = Mock()
        employee.position = None
        employee.department = None
        employee.direction = None
        employee.general_direction = None
        employee.salary_grade = None
        
        payroll = Mock()
        payroll.overtime_hours = None
        
        system_params = Mock()
        system_params.company_name = "Test Company"
        system_params.company_activity = "Test Activity"
        system_params.address = "Test Address"
        system_params.phone = "123456789"
        system_params.logo = None
        
        line_items = []
        
        result = PayslipReportData.prepare_payslip_data(
            employee, payroll, line_items, system_params
        )
        
        assert isinstance(result, dict)
        assert 'company_name' in result
        assert result['position'] == ""
        assert result['department'] == ""
    
    def test_declaration_report_data_empty_list(self):
        """Test declaration report data with empty employee list"""
        system_params = Mock()
        system_params.company_name = "Test Company"
        system_params.cnss_number = "123456"
        
        result = DeclarationReportData.prepare_cnss_declaration_data(
            [], date(2023, 12, 31), system_params
        )
        
        assert isinstance(result, dict)
        assert result['total_employees'] == 0
        assert result['total_days'] == 0
    
    def test_declaration_report_data_with_employees(self):
        """Test declaration report data with employee data"""
        employees_data = [
            {'total_days': 22, 'taxable_salary': Decimal('50000'), 'cnss_contribution': Decimal('500')},
            {'total_days': 20, 'taxable_salary': Decimal('45000'), 'cnss_contribution': Decimal('450')}
        ]
        
        system_params = Mock()
        system_params.company_name = "Test Company"
        system_params.cnss_number = "123456"
        
        result = DeclarationReportData.prepare_cnss_declaration_data(
            employees_data, date(2023, 12, 31), system_params
        )
        
        assert result['total_employees'] == 2
        assert result['total_days'] == 42
        assert result['total_taxable_salary'] == Decimal('95000')
        assert result['total_contributions'] == Decimal('950')
    
    def test_cnam_declaration_report_data(self):
        """Test CNAM declaration report data preparation"""
        employees_data = [
            {'taxable_salary': Decimal('30000'), 'cnam_contribution': Decimal('1200')}
        ]
        
        system_params = Mock()
        system_params.company_name = "Test Company"
        system_params.cnam_number = "CNAM123"
        
        result = DeclarationReportData.prepare_cnam_declaration_data(
            employees_data, date(2023, 12, 31), system_params
        )
        
        assert isinstance(result, dict)
        assert 'company_name' in result
        assert result['total_employees'] == 1
        assert result['total_taxable_salary'] == Decimal('30000')
    
    def test_payslip_report_data_with_line_items(self):
        """Test payslip report data with various line items"""
        employee = Mock()
        employee.first_name = "John"
        employee.last_name = "Doe"
        employee.position = Mock()
        employee.position.name = "Developer"
        employee.department = Mock()
        employee.department.name = "IT"
        employee.direction = None
        employee.general_direction = None
        employee.salary_grade = None
        
        payroll = Mock()
        payroll.period = date(2023, 12, 31)
        payroll.period_in_words = "December 2023"
        payroll.category = "Regular"
        payroll.overtime_hours = Decimal('5.00')
        
        system_params = Mock()
        system_params.company_name = "Test Corp"
        system_params.company_activity = "Software"
        system_params.address = "123 Main St"
        system_params.phone = "555-0123"
        system_params.logo = b"logo_data"
        
        # Create mock line items
        gain_item = Mock()
        gain_item.payroll_element = Mock()
        gain_item.payroll_element.id = "G001"
        gain_item.payroll_element.label = "Base Salary"
        gain_item.payroll_element.type = "G"
        gain_item.base_amount = Decimal('50000')
        gain_item.quantity = Decimal('1.00')
        gain_item.amount = Decimal('50000')
        
        deduction_item = Mock()
        deduction_item.payroll_element = Mock()
        deduction_item.payroll_element.id = "D001"
        deduction_item.payroll_element.label = "CNSS"
        deduction_item.payroll_element.type = "D"
        deduction_item.base_amount = None
        deduction_item.quantity = None
        deduction_item.amount = Decimal('500')
        
        line_items = [gain_item, deduction_item]
        
        result = PayslipReportData.prepare_payslip_data(
            employee, payroll, line_items, system_params
        )
        
        assert result['full_name'] == "John Doe"
        assert result['position'] == "Developer"
        assert result['department'] == "IT"
        assert len(result['gains']) == 1
        assert len(result['deductions']) == 1
        assert result['gains'][0]['code'] == "G001"
        assert result['deductions'][0]['code'] == "D001"


class TestMiscellaneousEdgeCases:
    """Test other edge cases to improve overall coverage"""
    
    def test_utility_functions_edge_cases(self):
        """Test various utility functions with edge case inputs"""
        # Test safe_divide with zero divisor
        from core.utils.formula_engine import safe_divide
        
        result = safe_divide(Decimal('10'), Decimal('0'))
        assert result == Decimal('0')
        
        result = safe_divide(Decimal('10'), Decimal('2'))
        assert result == Decimal('5')
    
    def test_percentage_function(self):
        """Test percentage calculation function"""
        from core.utils.formula_engine import percentage
        
        result = percentage(Decimal('50'), Decimal('10'))
        assert result == Decimal('5')
        
        result = percentage(Decimal('100'), Decimal('0'))
        assert result == Decimal('0')
    
    def test_round_currency_function(self):
        """Test currency rounding function"""
        from core.utils.formula_engine import round_currency
        
        result = round_currency(Decimal('10.555'))
        assert result == Decimal('10.56')
        
        result = round_currency(Decimal('10.554'))
        assert result == Decimal('10.55')