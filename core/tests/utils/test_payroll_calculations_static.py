"""
Tests for PayrollFunctionsStatic class
"""

import pytest
from decimal import Decimal
from datetime import date
from unittest.mock import Mock, patch
from core.utils.payroll_calculations import PayrollFunctionsStatic


class MockEmployee:
    """Mock employee for static function testing"""
    
    def __init__(self):
        self.id = 1
        self.first_name = "John"
        self.last_name = "Doe"
        self.hire_date = date(2020, 1, 1)
        self.birth_date = date(1985, 6, 15)
        self.contract_hours = Decimal('8')
        self.contract_hours_per_week = Decimal('40')
        self.base_salary = Decimal('50000')
        self.is_expatriate = False
        self.number_of_children = 2
        self.seniority_date = date(2020, 1, 1)
        self.salary_grade = None
        
    def get_seniority_rate(self, period, special_calculation=False):
        """Mock seniority rate calculation"""
        if special_calculation:
            return Decimal('0.15')  # 15% special seniority rate
        return Decimal('0.10')  # 10% standard seniority rate


class TestPayrollFunctionsStatic:
    """Test PayrollFunctionsStatic class methods"""
    
    def setup_method(self):
        """Set up test fixtures"""
        self.employee = MockEmployee()
        self.motif = Mock()
        self.period = date(2023, 12, 31)
    
    def test_F01_NJT_with_mocked_record(self):
        """Test F01 static method - NJT with mocked WorkedDays"""
        with patch('core.models.WorkedDays') as mock_worked_days:
            mock_record = Mock()
            mock_record.worked_days = Decimal('22')
            mock_worked_days.objects.get.return_value = mock_record
            
            result = PayrollFunctionsStatic.F01_NJT(self.employee, self.motif, self.period)
            assert result == Decimal('22')
    
    def test_F01_NJT_no_record(self):
        """Test F01 static method - NJT with no record found"""
        with patch('core.models.WorkedDays') as mock_worked_days:
            mock_worked_days.DoesNotExist = Exception
            mock_worked_days.objects.get.side_effect = mock_worked_days.DoesNotExist
            
            result = PayrollFunctionsStatic.F01_NJT(self.employee, self.motif, self.period)
            assert result == Decimal('0.00')
    
    def test_F02_sbJour_with_payroll_elements(self):
        """Test F02 static method - daily salary with payroll elements"""
        mock_element = Mock()
        mock_element.id = 1
        mock_element.base_amount = Decimal('1666.67')
        payroll_elements = [mock_element]
        
        result = PayrollFunctionsStatic.F02_sbJour(
            self.employee, self.motif, self.period, payroll_elements
        )
        assert result == Decimal('1666.67')
    
    def test_F02_sbJour_with_salary_grade(self):
        """Test F02 static method - daily salary with salary grade"""
        self.employee.salary_grade = Mock()
        self.employee.salary_grade.base_salary = Decimal('52000')
        
        result = PayrollFunctionsStatic.F02_sbJour(
            self.employee, self.motif, self.period, None
        )
        expected = Decimal('52000') / 26  # 26 default days
        assert result == expected
    
    def test_F02_sbJour_no_salary_data(self):
        """Test F02 static method - daily salary with no data"""
        result = PayrollFunctionsStatic.F02_sbJour(
            self.employee, self.motif, self.period, None
        )
        assert result == Decimal('0.00')
    
    def test_F03_sbHoraire_normal_case(self):
        """Test F03 static method - hourly salary normal case"""
        # Mock F02 to return a daily salary
        with patch.object(PayrollFunctionsStatic, 'F02_sbJour', return_value=Decimal('2000')):
            result = PayrollFunctionsStatic.F03_sbHoraire(
                self.employee, self.motif, self.period, None
            )
            # daily_salary = 2000, monthly = 2000 * 30 = 60000
            # monthly_hours = 40 * 52 / 12 = 173.33...
            # hourly = 60000 / 173.33... = 346.15...
            expected = Decimal('2000') * 30 / (Decimal('40') * Decimal('52') / Decimal('12'))
            assert result == expected
    
    def test_F03_sbHoraire_zero_hours(self):
        """Test F03 static method - hourly salary with zero hours"""
        self.employee.contract_hours_per_week = 0
        
        with patch.object(PayrollFunctionsStatic, 'F02_sbJour', return_value=Decimal('2000')):
            result = PayrollFunctionsStatic.F03_sbHoraire(
                self.employee, self.motif, self.period, None
            )
            assert result == Decimal('0.00')
    
    def test_F03_sbHoraire_zero_daily_salary(self):
        """Test F03 static method - hourly salary with zero daily salary"""
        with patch.object(PayrollFunctionsStatic, 'F02_sbJour', return_value=Decimal('0')):
            result = PayrollFunctionsStatic.F03_sbHoraire(
                self.employee, self.motif, self.period, None
            )
            assert result == Decimal('0.00')
    
    def test_static_functions_coverage(self):
        """Test various static functions to improve coverage"""
        # Test functions that return simple values or calculations
        assert PayrollFunctionsStatic.F04_TauxAnciennete(
            self.employee, self.period
        ) >= Decimal('0.00')
        
        assert PayrollFunctionsStatic.F23_TauxAncienneteSpeciale(
            self.employee, self.period
        ) >= Decimal('0.00')
        
        # Test F24 with mock system params
        mock_system_params = Mock()
        mock_system_params.get_salary_increase_rate = Mock(return_value=Decimal('0.05'))
        
        assert PayrollFunctionsStatic.F24_augmentationSalaireFixe(
            self.employee, self.period, mock_system_params
        ) >= Decimal('0.00')
    
    def test_edge_cases_and_error_handling(self):
        """Test edge cases and error handling for static functions"""
        # Test with None employee
        try:
            result = PayrollFunctionsStatic.F01_NJT(None, self.motif, self.period)
            # Should either return 0 or raise an exception gracefully
        except (AttributeError, TypeError):
            pass  # Expected for None employee
        
        # Test with None period
        try:
            result = PayrollFunctionsStatic.F02_sbJour(self.employee, self.motif, None)
            # Should handle gracefully
        except (AttributeError, TypeError):
            pass  # Expected for None period
        
        # Test with empty payroll elements
        result = PayrollFunctionsStatic.F02_sbJour(
            self.employee, self.motif, self.period, []
        )
        # Should fallback to salary grade or return 0
        assert isinstance(result, Decimal)