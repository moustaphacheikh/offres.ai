"""
Comprehensive tests for payroll calculations module.
This file provides focused testing to achieve high coverage for payroll_calculations.py
"""

import pytest
from decimal import Decimal
from datetime import date, datetime
from unittest.mock import Mock, patch, MagicMock

# Import the actual classes that exist
from core.utils.payroll_calculations import (
    PayrollCalculator,
    OvertimeCalculator,
    InstallmentCalculator
)


class TestPayrollCalculatorComprehensive:
    """Comprehensive tests for PayrollCalculator class"""
    
    def setup_method(self):
        """Set up test instances"""
        self.system_params = Mock()
        
        # Mock all required system parameter methods
        self.system_params.get_cnss_ceiling.return_value = Decimal('15000')
        self.system_params.get_cnss_rate_employee.return_value = Decimal('0.01')
        self.system_params.get_cnss_rate_employer.return_value = Decimal('0.02')
        self.system_params.get_cnam_rate_employee.return_value = Decimal('0.04')
        self.system_params.get_cnam_rate_employer.return_value = Decimal('0.055')
        self.system_params.tax_abatement = Decimal('1500')
        self.system_params.deduct_cnss_from_its = True
        self.system_params.deduct_cnam_from_its = True
        
        # Mock ITS brackets
        self.system_params.get_its_brackets.return_value = [
            {'min': 0, 'max': 9000, 'rate': 0.15},
            {'min': 9000, 'max': 21000, 'rate': 0.25},
            {'min': 21000, 'max': float('inf'), 'rate': 0.40}
        ]
        
        # Mock the PayrollFunctions class to avoid the constructor error
        with patch('core.utils.payroll_calculations.PayrollFunctions') as mock_pf:
            # Create a mock instance
            mock_instance = Mock()
            mock_pf.return_value = mock_instance
            self.calculator = PayrollCalculator(self.system_params)
    
    def test_init_with_system_parameters(self):
        """Test PayrollCalculator initialization"""
        assert self.calculator.system_parameters == self.system_params
        assert hasattr(self.calculator, 'payroll_functions')
    
    def test_calculate_cnss_employee_basic(self):
        """Test basic CNSS employee calculation"""
        result = self.calculator.calculate_cnss_employee(
            Decimal('10000'), Decimal('1.0'), 2020
        )
        assert result == Decimal('100.00')  # 1% of 10000
    
    def test_calculate_cnss_employee_with_ceiling(self):
        """Test CNSS calculation with ceiling applied"""
        result = self.calculator.calculate_cnss_employee(
            Decimal('20000'), Decimal('1.0'), 2020
        )
        # Should be capped at ceiling of 15000
        assert result == Decimal('150.00')  # 1% of 15000
    
    def test_calculate_cnss_employee_before_2018(self):
        """Test CNSS calculation for year before 2018"""
        result = self.calculator.calculate_cnss_employee(
            Decimal('10000'), Decimal('1.0'), 2017
        )
        assert result == Decimal('100.00')  # Same rate applies
    
    def test_calculate_cnss_employer_basic(self):
        """Test CNSS employer calculation"""
        result = self.calculator.calculate_cnss_employer(
            Decimal('10000'), Decimal('5.0'), Decimal('1.0'), 2020
        )
        # (1% of 10000) * 5% = 5.00
        assert result == Decimal('5.00')
    
    def test_calculate_cnss_employer_with_reimbursement(self):
        """Test CNSS employer with reimbursement rate"""
        result = self.calculator.calculate_cnss_employer(
            Decimal('10000'), Decimal('10.0'), Decimal('1.0'), 2020
        )
        assert result == Decimal('10.00')
    
    def test_calculate_cnam_employee_basic(self):
        """Test CNAM employee calculation"""
        result = self.calculator.calculate_cnam_employee(Decimal('10000'))
        assert result == Decimal('400.00')  # 4% of 10000
    
    def test_calculate_cnam_employer_basic(self):
        """Test CNAM employer calculation"""
        result = self.calculator.calculate_cnam_employer(
            Decimal('10000'), Decimal('10.0')
        )
        # (4% of 10000) * 10% = 40.00
        assert result == Decimal('40.00')
    
    def test_calculate_its_tranche1_basic(self):
        """Test ITS tranche 1 calculation"""
        result = self.calculator.calculate_its_tranche1(
            2020, Decimal('8000'), Decimal('100'), Decimal('200'),
            Decimal('8000'), Decimal('0'), Decimal('1.0'), False
        )
        # 8000 - 100 - 200 - 1500 = 6200 * 15% = 930
        assert result == Decimal('930.00')
    
    def test_calculate_its_tranche1_expatriate(self):
        """Test ITS tranche 1 with expatriate reduction"""
        result = self.calculator.calculate_its_tranche1(
            2020, Decimal('8000'), Decimal('100'), Decimal('200'),
            Decimal('8000'), Decimal('0'), Decimal('1.0'), True
        )
        # Same base but 7.5% rate for expatriates
        assert result == Decimal('465.00')
    
    def test_calculate_its_tranche2_basic(self):
        """Test ITS tranche 2 calculation"""
        result = self.calculator.calculate_its_tranche2(
            2020, Decimal('15000'), Decimal('100'), Decimal('200'),
            Decimal('15000'), Decimal('0'), Decimal('1.0'), False
        )
        # 15000 - 100 - 200 - 1500 = 13200
        # Only amount above 9000 = 4200 * 25% = 1050
        assert result == Decimal('1050.00')
    
    def test_calculate_its_tranche3_basic(self):
        """Test ITS tranche 3 calculation"""
        result = self.calculator.calculate_its_tranche3(
            2020, Decimal('30000'), Decimal('100'), Decimal('200'),
            Decimal('30000'), Decimal('0'), Decimal('1.0'), False
        )
        # 30000 - 100 - 200 - 1500 = 28200
        # Amount above 21000 = 7200 * 40% = 2880
        assert result == Decimal('2880.00')
    
    def test_calculate_its_total_all_tranches(self):
        """Test total ITS calculation"""
        result = self.calculator.calculate_its_total(
            2020, Decimal('30000'), Decimal('100'), Decimal('200'),
            Decimal('30000'), Decimal('0'), Decimal('1.0'), False
        )
        # Should be sum of all tranches
        assert result > Decimal('0.00')
    
    def test_private_calculate_cnss_employee(self):
        """Test private _calculate_cnss_employee method"""
        result = self.calculator._calculate_cnss_employee(Decimal('10000'))
        assert result == Decimal('100.00')
    
    def test_private_calculate_cnss_employer(self):
        """Test private _calculate_cnss_employer method"""
        employee = Mock()
        employee.cnss_reimbursement_rate = None
        
        result = self.calculator._calculate_cnss_employer(Decimal('10000'), employee)
        assert result == Decimal('200.00')  # 2% of 10000
    
    def test_private_calculate_cnss_employer_with_reimbursement(self):
        """Test private _calculate_cnss_employer with reimbursement"""
        employee = Mock()
        employee.cnss_reimbursement_rate = Decimal('50.0')  # 50%
        
        result = self.calculator._calculate_cnss_employer(Decimal('10000'), employee)
        assert result == Decimal('100.00')  # 1% of 10000 (50% of 2%)
    
    def test_private_calculate_cnam_employee(self):
        """Test private _calculate_cnam_employee method"""
        result = self.calculator._calculate_cnam_employee(Decimal('10000'))
        assert result == Decimal('400.00')
    
    def test_private_calculate_cnam_employer(self):
        """Test private _calculate_cnam_employer method"""
        employee = Mock()
        employee.cnam_reimbursement_rate = None
        
        result = self.calculator._calculate_cnam_employer(Decimal('10000'), employee)
        assert result == Decimal('550.00')  # 5.5% of 10000
    
    def test_private_calculate_cnam_employer_with_reimbursement(self):
        """Test private _calculate_cnam_employer with reimbursement"""
        employee = Mock()
        employee.cnam_reimbursement_rate = Decimal('20.0')  # 20%
        
        result = self.calculator._calculate_cnam_employer(Decimal('10000'), employee)
        assert result == Decimal('440.00')  # 4.4% of 10000 (80% of 5.5%)
    
    def test_private_calculate_its(self):
        """Test private _calculate_its method"""
        result = self.calculator._calculate_its(
            Decimal('25000'), Decimal('100'), Decimal('200'), 
            Decimal('0'), False
        )
        
        assert 'total' in result
        assert 'tranche1' in result
        assert 'tranche2' in result
        assert 'tranche3' in result
        assert result['total'] > Decimal('0.00')
    
    def test_get_methods_return_defaults(self):
        """Test that various get methods return appropriate defaults"""
        employee = Mock()
        
        assert self.calculator.get_njt_record(employee, None, None) is None
        assert self.calculator.get_rubrique_paie_record(employee, 1, None, None) is None
        assert self.calculator.get_used_rub_id(1) == 1
        assert self.calculator.get_cumulative_amount_by_type(employee, "BI") == Decimal('0.00')
        assert self.calculator.get_cumulative_gross_12_months(employee, date.today(), date.today()) == Decimal('0.00')
        assert self.calculator.get_fixed_monthly_gross_salary(employee, date.today()) == Decimal('0.00')
        assert self.calculator.get_housing_allowance_base(employee) == Decimal('0.00')
        assert self.calculator.get_salary_increase(employee, date.today()) == Decimal('0.00')
    
    def test_calculate_payroll_basic_workflow(self):
        """Test basic payroll calculation workflow"""
        employee = Mock()
        employee.is_subject_to_cnss.return_value = True
        employee.is_subject_to_cnam.return_value = True
        employee.is_subject_to_its.return_value = True
        employee.is_expatriate = False
        employee.cnss_reimbursement_rate = None
        employee.cnam_reimbursement_rate = None
        
        motif = Mock()
        motif.employee_subject_to_cnss = True
        motif.employee_subject_to_cnam = True
        motif.employee_subject_to_its = True
        
        # Mock the private method that gets payroll line items
        with patch.object(self.calculator, '_get_payroll_line_items') as mock_items:
            mock_items.return_value = []
            
            result = self.calculator.calculate_payroll(
                employee, motif, date.today(), date.today()
            )
            
            # Verify all required keys are present
            required_keys = [
                'gross_taxable', 'gross_non_taxable', 'cnss_employee', 
                'cnss_employer', 'cnam_employee', 'cnam_employer',
                'its_total', 'its_tranche1', 'its_tranche2', 'its_tranche3',
                'net_salary'
            ]
            for key in required_keys:
                assert key in result
                assert isinstance(result[key], Decimal)


class TestOvertimeCalculatorComprehensive:
    """Comprehensive tests for OvertimeCalculator"""
    
    def test_calculate_overtime_rates_normal_hours(self):
        """Test overtime calculation for normal overtime hours"""
        result = OvertimeCalculator.calculate_overtime_rates(Decimal('10.0'), 8)
        
        assert result['ot_115'] == Decimal('8.00')  # First 8 hours
        assert result['ot_140'] == Decimal('2.00')  # Remaining 2 hours
        assert result['ot_150'] == Decimal('0.00')
        assert result['ot_200'] == Decimal('0.00')
    
    def test_calculate_overtime_rates_high_hours(self):
        """Test overtime calculation for very high hours"""
        result = OvertimeCalculator.calculate_overtime_rates(Decimal('20.0'), 8)
        
        assert result['ot_115'] == Decimal('8.00')   # First 8 OT hours
        assert result['ot_140'] == Decimal('6.00')   # Next 6 hours (9-14)
        assert result['ot_150'] == Decimal('6.00')   # Remaining hours
        assert result['ot_200'] == Decimal('0.00')
    
    def test_calculate_overtime_rates_with_holiday(self):
        """Test overtime calculation with holiday work"""
        result = OvertimeCalculator.calculate_overtime_rates(
            Decimal('12.0'), 8, has_holiday_work=True
        )
        
        assert result['ot_115'] == Decimal('8.00')
        assert result['ot_140'] == Decimal('4.00')
        assert result['ot_150'] == Decimal('0.00')
        assert result['ot_200'] == Decimal('4.00')  # Holiday hours at 200%
    
    def test_calculate_overtime_amounts_all_rates(self):
        """Test overtime amount calculations for all rates"""
        rates = {
            'ot_115': Decimal('5.0'),
            'ot_140': Decimal('3.0'),
            'ot_150': Decimal('2.0'),
            'ot_200': Decimal('1.0')
        }
        hourly_rate = Decimal('100.00')
        
        result = OvertimeCalculator.calculate_overtime_amounts(rates, hourly_rate)
        
        assert result['ot_115_amount'] == Decimal('575.00')  # 5 * 100 * 1.15
        assert result['ot_140_amount'] == Decimal('420.00')  # 3 * 100 * 1.40
        assert result['ot_150_amount'] == Decimal('300.00')  # 2 * 100 * 1.50
        assert result['ot_200_amount'] == Decimal('200.00')  # 1 * 100 * 2.00
    
    def test_calculate_overtime_amounts_zero_rates(self):
        """Test overtime amounts when no overtime"""
        rates = {
            'ot_115': Decimal('0.0'),
            'ot_140': Decimal('0.0'),
            'ot_150': Decimal('0.0'),
            'ot_200': Decimal('0.0')
        }
        
        result = OvertimeCalculator.calculate_overtime_amounts(rates, Decimal('100.00'))
        
        for amount in result.values():
            assert amount == Decimal('0.00')


class TestInstallmentCalculatorComprehensive:
    """Comprehensive tests for InstallmentCalculator"""
    
    def test_calculate_quota_cessible_standard(self):
        """Test standard quota cessible calculation"""
        result = InstallmentCalculator.calculate_quota_cessible(
            Decimal('10000.00'), Decimal('30.0')
        )
        assert result == Decimal('3000.00')  # 30% of 10000
    
    def test_calculate_quota_cessible_high_percentage(self):
        """Test quota cessible with high percentage"""
        result = InstallmentCalculator.calculate_quota_cessible(
            Decimal('5000.00'), Decimal('50.0')
        )
        assert result == Decimal('2500.00')  # 50% of 5000
    
    def test_calculate_quota_cessible_zero_salary(self):
        """Test quota cessible with zero salary"""
        result = InstallmentCalculator.calculate_quota_cessible(
            Decimal('0.00'), Decimal('30.0')
        )
        assert result == Decimal('0.00')
    
    def test_adjust_installments_within_quota(self):
        """Test installment adjustment when within quota"""
        installments = [
            {'id': 1, 'amount': Decimal('1000.00'), 'priority': 1},
            {'id': 2, 'amount': Decimal('500.00'), 'priority': 2}
        ]
        quota = Decimal('2000.00')
        
        result = InstallmentCalculator.adjust_installments_for_quota(installments, quota)
        
        assert len(result) == 2
        assert result[0]['amount'] == Decimal('1000.00')
        assert result[1]['amount'] == Decimal('500.00')
    
    def test_adjust_installments_exceeds_quota(self):
        """Test installment adjustment when exceeding quota"""
        installments = [
            {'id': 1, 'amount': Decimal('1000.00'), 'priority': 1},
            {'id': 2, 'amount': Decimal('800.00'), 'priority': 2}
        ]
        quota = Decimal('1200.00')  # Total is 1800, quota is 1200
        
        result = InstallmentCalculator.adjust_installments_for_quota(installments, quota)
        
        assert len(result) == 2
        # Should be reduced proportionally (1200/1800 = 2/3)
        assert result[0]['amount'] == Decimal('666.67')  # 1000 * 2/3
        assert result[1]['amount'] == Decimal('533.33')  # 800 * 2/3
    
    def test_adjust_installments_empty_list(self):
        """Test installment adjustment with empty list"""
        result = InstallmentCalculator.adjust_installments_for_quota([], Decimal('1000.00'))
        assert result == []
    
    def test_adjust_installments_zero_quota(self):
        """Test installment adjustment with zero quota"""
        installments = [
            {'id': 1, 'amount': Decimal('1000.00'), 'priority': 1}
        ]
        quota = Decimal('0.00')
        
        result = InstallmentCalculator.adjust_installments_for_quota(installments, quota)
        
        assert len(result) == 1
        assert result[0]['amount'] == Decimal('0.00')


class TestPayrollCalculatorUtilityMethods:
    """Test utility methods in PayrollCalculator that aren't covered yet"""
    
    def setup_method(self):
        """Set up test instances"""
        self.system_params = Mock()
        self.system_params.tax_abatement = Decimal('1500')
        self.system_params.deduct_cnss_from_its = True
        self.system_params.deduct_cnam_from_its = True
        
        with patch('core.utils.payroll_calculations.PayrollFunctions'):
            self.calculator = PayrollCalculator(self.system_params)
    
    def test_private_get_payroll_line_items(self):
        """Test the private _get_payroll_line_items method"""
        employee = Mock()
        motif = Mock()
        period_start = date.today()
        period_end = date.today()
        
        # This method returns an empty list by default
        result = self.calculator._get_payroll_line_items(employee, motif, period_start, period_end)
        assert result == []
    
    def test_calculate_payroll_with_non_subject_employee(self):
        """Test payroll calculation for employee not subject to taxes"""
        employee = Mock()
        employee.is_subject_to_cnss.return_value = False
        employee.is_subject_to_cnam.return_value = False
        employee.is_subject_to_its.return_value = False
        employee.is_expatriate = False
        
        motif = Mock()
        motif.employee_subject_to_cnss = False
        motif.employee_subject_to_cnam = False
        motif.employee_subject_to_its = False
        
        with patch.object(self.calculator, '_get_payroll_line_items') as mock_items:
            mock_items.return_value = []
            
            result = self.calculator.calculate_payroll(
                employee, motif, date.today(), date.today()
            )
            
            # All tax amounts should be zero
            assert result['cnss_employee'] == Decimal('0.00')
            assert result['cnam_employee'] == Decimal('0.00')
            assert result['its_total'] == Decimal('0.00')