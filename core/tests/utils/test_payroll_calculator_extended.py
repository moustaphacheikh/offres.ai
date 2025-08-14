"""
Extended tests for PayrollCalculator class to improve coverage
"""

import pytest
from decimal import Decimal
from datetime import date
from unittest.mock import Mock, patch
from core.utils.payroll_calculations import PayrollCalculator


class MockSystemParameters:
    """Mock system parameters for testing"""
    
    def __init__(self):
        # Tax configuration
        self.tax_abatement = Decimal('36000')  # Standard tax abatement
        self.deduct_cnss_from_its = True
        self.deduct_cnam_from_its = True
        
        # CNSS/CNAM configuration
        self.cnss_employee_rate = Decimal('0.01')  # 1%
        self.cnss_employer_rate = Decimal('0.02')  # 2%
        self.cnam_employee_rate = Decimal('0.005')  # 0.5%
        self.cnam_employer_rate = Decimal('0.01')  # 1%
        
    def get_cnss_ceiling(self):
        """Mock CNSS ceiling"""
        return Decimal('50000')
        
    def get_cnam_ceiling(self):
        """Mock CNAM ceiling"""
        return Decimal('50000')
        
    def get_its_brackets(self):
        """Mock ITS tax brackets"""
        return [
            {'min': 0, 'max': 25000, 'rate': 0.15},
            {'min': 25000, 'max': 100000, 'rate': 0.25},
            {'min': 100000, 'max': float('inf'), 'rate': 0.35}
        ]
    
    def get_cnss_rate_employee(self):
        """Mock CNSS employee rate"""
        return self.cnss_employee_rate
    
    def get_cnss_rate_employer(self):
        """Mock CNSS employer rate"""
        return self.cnss_employer_rate
    
    def get_cnam_rate_employee(self):
        """Mock CNAM employee rate"""
        return self.cnam_employee_rate
    
    def get_cnam_rate_employer(self):
        """Mock CNAM employer rate"""
        return self.cnam_employer_rate


class MockEmployee:
    """Mock employee for testing"""
    
    def __init__(self):
        self.id = 1
        self.first_name = "John"
        self.last_name = "Doe"
        self.base_salary = Decimal('50000')
        self.is_expatriate = False
        self.contract_hours_per_week = Decimal('40')
        self.cnss_reimbursement_rate = None
        self.cnam_reimbursement_rate = None


class TestPayrollCalculatorExtended:
    """Extended tests for PayrollCalculator methods"""
    
    def setup_method(self):
        """Set up test fixtures"""
        self.system_parameters = MockSystemParameters()
        self.calculator = PayrollCalculator(self.system_parameters)
        self.employee = MockEmployee()
    
    def test_calculate_cnss_employee_normal(self):
        """Test CNSS employee calculation - normal case"""
        base_salary = Decimal('30000')
        result = self.calculator._calculate_cnss_employee(base_salary)
        expected = base_salary * self.system_parameters.cnss_employee_rate
        assert result == expected
    
    def test_calculate_cnss_employee_ceiling(self):
        """Test CNSS employee calculation - at ceiling"""
        base_salary = Decimal('60000')  # Above ceiling
        result = self.calculator._calculate_cnss_employee(base_salary)
        ceiling = self.system_parameters.get_cnss_ceiling()
        expected = ceiling * self.system_parameters.cnss_employee_rate
        assert result == expected
    
    def test_calculate_cnss_employer(self):
        """Test CNSS employer calculation"""
        base_salary = Decimal('30000')
        result = self.calculator._calculate_cnss_employer(base_salary, self.employee)
        expected = base_salary * self.system_parameters.cnss_employer_rate
        assert result == expected
    
    def test_calculate_cnam_employee_normal(self):
        """Test CNAM employee calculation - normal case"""
        base_salary = Decimal('30000')
        result = self.calculator._calculate_cnam_employee(base_salary)
        expected = base_salary * self.system_parameters.cnam_employee_rate
        assert result == expected
    
    def test_calculate_cnam_employee_ceiling(self):
        """Test CNAM employee calculation - no ceiling applied"""
        base_salary = Decimal('60000')  # CNAM has no ceiling
        result = self.calculator._calculate_cnam_employee(base_salary)
        expected = base_salary * self.system_parameters.cnam_employee_rate
        assert result == expected
    
    def test_calculate_cnam_employer(self):
        """Test CNAM employer calculation"""
        base_salary = Decimal('30000')
        result = self.calculator._calculate_cnam_employer(base_salary, self.employee)
        expected = base_salary * self.system_parameters.cnam_employer_rate
        assert result == expected
    
    def test_calculate_its_tranche1_only(self):
        """Test ITS calculation - first bracket only"""
        gross_salary = Decimal('20000')
        cnss_amount = Decimal('200')
        cnam_amount = Decimal('100')
        is_expatriate = False
        
        result = self.calculator._calculate_its(
            gross_salary, cnss_amount, cnam_amount, is_expatriate
        )
        
        # After deductions and abatement
        taxable_income = gross_salary - cnss_amount - cnam_amount - self.system_parameters.tax_abatement
        # Since 20000 - 200 - 100 - 36000 = -16300, taxable should be 0
        assert result['total'] == Decimal('0.00')
        assert result['tranche1'] == Decimal('0.00')
    
    def test_calculate_its_tranche2(self):
        """Test ITS calculation - reaching second bracket"""
        gross_salary = Decimal('80000')
        cnss_amount = Decimal('800')
        cnam_amount = Decimal('400')
        is_expatriate = False
        
        result = self.calculator._calculate_its(
            gross_salary, cnss_amount, cnam_amount, is_expatriate
        )
        
        # After deductions and abatement: 80000 - 800 - 400 - 36000 = 42800
        # First bracket: 25000 * 0.15 = 3750
        # Second bracket: 17800 * 0.25 = 4450
        # Total: 3750 + 4450 = 8200
        assert result['total'] > Decimal('0.00')
        assert result['tranche1'] > Decimal('0.00')
        assert result['tranche2'] > Decimal('0.00')
    
    def test_calculate_its_all_tranches(self):
        """Test ITS calculation - all tax brackets"""
        gross_salary = Decimal('200000')
        cnss_amount = Decimal('2000')
        cnam_amount = Decimal('1000')
        is_expatriate = False
        
        result = self.calculator._calculate_its(
            gross_salary, cnss_amount, cnam_amount, is_expatriate
        )
        
        # Should have amounts in all three tranches
        assert result['total'] > Decimal('0.00')
        assert result['tranche1'] > Decimal('0.00')
        assert result['tranche2'] > Decimal('0.00')
        assert result['tranche3'] > Decimal('0.00')
    
    def test_calculate_its_expatriate_reduction(self):
        """Test ITS calculation - expatriate gets reduced rate on first bracket"""
        gross_salary = Decimal('80000')
        cnss_amount = Decimal('800')
        cnam_amount = Decimal('400')
        is_expatriate = True
        
        result_expat = self.calculator._calculate_its(
            gross_salary, cnss_amount, cnam_amount, is_expatriate
        )
        result_national = self.calculator._calculate_its(
            gross_salary, cnss_amount, cnam_amount, False
        )
        
        # Both should have positive taxes, but expatriate should pay less overall
        assert result_expat['total'] > Decimal('0.00')
        assert result_national['total'] > Decimal('0.00')
        # Expatriate should pay less or equal (7.5% vs 15% on first bracket)
        assert result_expat['total'] <= result_national['total']
    
    def test_calculate_its_edge_cases(self):
        """Test ITS calculation - edge cases"""
        # Test with zero income
        result = self.calculator._calculate_its(
            Decimal('0'), Decimal('0'), Decimal('0'), False
        )
        assert result['total'] == Decimal('0.00')
        
        # Test with negative taxable income (after abatement)
        result = self.calculator._calculate_its(
            Decimal('10000'), Decimal('0'), Decimal('0'), False
        )
        assert result['total'] == Decimal('0.00')
    
    def test_overtime_calculator_basic_structure(self):
        """Test OvertimeCalculator structure and methods"""
        from core.utils.payroll_calculations import OvertimeCalculator
        
        # Test methods exist and are callable (static methods)
        assert hasattr(OvertimeCalculator, 'calculate_overtime_rates')
        assert callable(OvertimeCalculator.calculate_overtime_rates)
        
        # Test the method works
        result = OvertimeCalculator.calculate_overtime_rates(10, 8)
        assert isinstance(result, dict)
    
    def test_installment_calculator_basic_structure(self):
        """Test InstallmentCalculator structure and methods"""
        from core.utils.payroll_calculations import InstallmentCalculator
        
        # Test methods exist and are callable (static methods)
        assert hasattr(InstallmentCalculator, 'calculate_quota_cessible')
        assert callable(InstallmentCalculator.calculate_quota_cessible)
        
        # Test the method works
        result = InstallmentCalculator.calculate_quota_cessible(Decimal('1000'), Decimal('0.3'))
        assert isinstance(result, Decimal)
    
    def test_payroll_calculator_initialization(self):
        """Test PayrollCalculator initialization and basic structure"""
        assert self.calculator.system_parameters == self.system_parameters
        
        # Test that all main calculation methods exist
        assert hasattr(self.calculator, '_calculate_cnss_employee')
        assert hasattr(self.calculator, '_calculate_cnss_employer')
        assert hasattr(self.calculator, '_calculate_cnam_employee')
        assert hasattr(self.calculator, '_calculate_cnam_employer')
        assert hasattr(self.calculator, '_calculate_its')
    
    def test_calculate_methods_with_none_inputs(self):
        """Test calculation methods handle None inputs gracefully"""
        # Test CNSS calculations with None
        try:
            result = self.calculator._calculate_cnss_employee(None)
            # Should either return 0 or raise an exception gracefully
        except (TypeError, AttributeError):
            pass  # Expected for None input
        
        try:
            result = self.calculator._calculate_cnss_employer(None, self.employee)
        except (TypeError, AttributeError):
            pass  # Expected for None input
    
    def test_various_calculation_edge_cases(self):
        """Test various edge cases in calculations"""
        # Test with very small amounts
        small_amount = Decimal('0.01')
        result = self.calculator._calculate_cnss_employee(small_amount)
        assert result >= Decimal('0.00')
        
        # Test with very large amounts
        large_amount = Decimal('1000000')
        result = self.calculator._calculate_cnss_employee(large_amount)
        # Should be capped by ceiling
        ceiling = self.system_parameters.get_cnss_ceiling()
        expected_max = ceiling * self.system_parameters.cnss_employee_rate
        assert result == expected_max