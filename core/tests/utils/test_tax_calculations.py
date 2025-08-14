"""
Tests for core.utils.tax_calculations module.

This module provides comprehensive test coverage for the CNSSCalculator,
CNAMCalculator, ITSCalculator, and TaxCalculationService classes that
implement Mauritanian tax calculation logic.
"""

import pytest
from decimal import Decimal, ROUND_HALF_UP
from unittest.mock import Mock, MagicMock

from core.utils.tax_calculations import (
    CNSSCalculator,
    CNAMCalculator,
    ITSCalculator,
    TaxCalculationService
)


class TestCNSSCalculator:
    """Test CNSSCalculator class methods"""
    
    def test_calculate_employee_contribution_below_ceiling(self):
        """Test CNSS employee calculation below ceiling"""
        taxable_amount = Decimal('10000')
        result = CNSSCalculator.calculate_employee_contribution(taxable_amount)
        
        # 1% of 10,000 = 100
        expected = Decimal('100.00')
        assert result == expected
    
    def test_calculate_employee_contribution_at_ceiling(self):
        """Test CNSS employee calculation at ceiling"""
        taxable_amount = Decimal('15000')  # At ceiling
        result = CNSSCalculator.calculate_employee_contribution(taxable_amount)
        
        # 1% of 15,000 = 150
        expected = Decimal('150.00')
        assert result == expected
    
    def test_calculate_employee_contribution_above_ceiling(self):
        """Test CNSS employee calculation above ceiling"""
        taxable_amount = Decimal('25000')  # Above ceiling
        result = CNSSCalculator.calculate_employee_contribution(taxable_amount)
        
        # Should cap at ceiling: 1% of 15,000 = 150
        expected = Decimal('150.00')
        assert result == expected
    
    def test_calculate_employee_contribution_zero(self):
        """Test CNSS employee calculation with zero amount"""
        taxable_amount = Decimal('0')
        result = CNSSCalculator.calculate_employee_contribution(taxable_amount)
        
        expected = Decimal('0.00')
        assert result == expected
    
    def test_calculate_employee_contribution_with_currency_rate(self):
        """Test CNSS employee calculation with currency conversion"""
        taxable_amount = Decimal('10000')
        currency_rate = Decimal('2.0')  # 2x exchange rate
        
        result = CNSSCalculator.calculate_employee_contribution(taxable_amount, currency_rate)
        
        # 1% of 10,000 * 2.0 = 200
        expected = Decimal('200.00')
        assert result == expected
    
    def test_calculate_employee_contribution_rounding(self):
        """Test CNSS employee calculation with rounding"""
        taxable_amount = Decimal('10001')  # Results in 100.01
        result = CNSSCalculator.calculate_employee_contribution(taxable_amount)
        
        expected = Decimal('100.01')
        assert result == expected
    
    def test_calculate_employer_contribution_basic(self):
        """Test basic CNSS employer calculation"""
        taxable_amount = Decimal('10000')
        result = CNSSCalculator.calculate_employer_contribution(taxable_amount)
        
        # 2% of 10,000 = 200
        expected = Decimal('200.00')
        assert result == expected
    
    def test_calculate_employer_contribution_with_reimbursement(self):
        """Test CNSS employer calculation with reimbursement rate"""
        taxable_amount = Decimal('10000')
        reimbursement_rate = Decimal('0.50')  # 50% reimbursement
        
        result = CNSSCalculator.calculate_employer_contribution(
            taxable_amount, reimbursement_rate
        )
        
        # Base: 2% of 10,000 = 200
        # With 50% reimbursement: 200 * (1 + 0.5) = 300
        expected = Decimal('300.00')
        assert result == expected
    
    def test_calculate_employer_contribution_above_ceiling(self):
        """Test CNSS employer calculation above ceiling"""
        taxable_amount = Decimal('20000')  # Above ceiling
        result = CNSSCalculator.calculate_employer_contribution(taxable_amount)
        
        # Should cap at ceiling: 2% of 15,000 = 300
        expected = Decimal('300.00')
        assert result == expected
    
    def test_calculate_employer_contribution_with_currency_rate(self):
        """Test CNSS employer calculation with currency conversion"""
        taxable_amount = Decimal('10000')
        currency_rate = Decimal('1.5')
        
        result = CNSSCalculator.calculate_employer_contribution(
            taxable_amount, currency_rate=currency_rate
        )
        
        # 2% of 10,000 * 1.5 = 300
        expected = Decimal('300.00')
        assert result == expected


class TestCNAMCalculator:
    """Test CNAMCalculator class methods"""
    
    def test_calculate_employee_contribution_basic(self):
        """Test basic CNAM employee calculation"""
        taxable_amount = Decimal('50000')
        result = CNAMCalculator.calculate_employee_contribution(taxable_amount)
        
        # 0.5% of 50,000 = 250
        expected = Decimal('250.00')
        assert result == expected
    
    def test_calculate_employee_contribution_no_ceiling(self):
        """Test CNAM employee calculation has no ceiling"""
        taxable_amount = Decimal('100000')  # High amount
        result = CNAMCalculator.calculate_employee_contribution(taxable_amount)
        
        # 0.5% of 100,000 = 500 (no ceiling applied)
        expected = Decimal('500.00')
        assert result == expected
    
    def test_calculate_employee_contribution_zero(self):
        """Test CNAM employee calculation with zero amount"""
        taxable_amount = Decimal('0')
        result = CNAMCalculator.calculate_employee_contribution(taxable_amount)
        
        expected = Decimal('0.00')
        assert result == expected
    
    def test_calculate_employee_contribution_rounding(self):
        """Test CNAM employee calculation with rounding"""
        taxable_amount = Decimal('10001')  # 0.5% = 50.005, rounds to 50.01
        result = CNAMCalculator.calculate_employee_contribution(taxable_amount)
        
        expected = Decimal('50.01')
        assert result == expected
    
    def test_calculate_employer_contribution_basic(self):
        """Test basic CNAM employer calculation"""
        taxable_amount = Decimal('50000')
        result = CNAMCalculator.calculate_employer_contribution(taxable_amount)
        
        # 1.5% of 50,000 = 750
        expected = Decimal('750.00')
        assert result == expected
    
    def test_calculate_employer_contribution_with_reimbursement(self):
        """Test CNAM employer calculation with reimbursement rate"""
        taxable_amount = Decimal('50000')
        reimbursement_rate = Decimal('0.30')  # 30% reimbursement
        
        result = CNAMCalculator.calculate_employer_contribution(
            taxable_amount, reimbursement_rate
        )
        
        # Base: 1.5% of 50,000 = 750
        # With 30% reimbursement: 750 * (1 + 0.3) = 975
        expected = Decimal('975.00')
        assert result == expected
    
    def test_calculate_employer_contribution_no_ceiling(self):
        """Test CNAM employer calculation has no ceiling"""
        taxable_amount = Decimal('200000')  # Very high amount
        result = CNAMCalculator.calculate_employer_contribution(taxable_amount)
        
        # 1.5% of 200,000 = 3000 (no ceiling)
        expected = Decimal('3000.00')
        assert result == expected


class TestITSCalculator:
    """Test ITSCalculator class methods"""
    
    def test_get_tax_brackets_national(self):
        """Test getting tax brackets for national employee"""
        brackets = ITSCalculator.get_tax_brackets(2018, is_expatriate=False)
        
        assert len(brackets) == 2
        
        # First bracket: 0-9000 at 15%
        assert brackets[0]['min'] == Decimal('0')
        assert brackets[0]['max'] == Decimal('9000')
        assert brackets[0]['rate'] == Decimal('0.15')
        
        # Second bracket: 9000+ at 20%
        assert brackets[1]['min'] == Decimal('9000')
        assert brackets[1]['max'] is None
        assert brackets[1]['rate'] == Decimal('0.20')
    
    def test_get_tax_brackets_expatriate(self):
        """Test getting tax brackets for expatriate employee"""
        brackets = ITSCalculator.get_tax_brackets(2018, is_expatriate=True)
        
        assert len(brackets) == 2
        
        # First bracket: 0-9000 at 7.5% (half rate for expatriates)
        assert brackets[0]['min'] == Decimal('0')
        assert brackets[0]['max'] == Decimal('9000')
        assert brackets[0]['rate'] == Decimal('0.075')
        
        # Second bracket: 9000+ at 20% (same rate)
        assert brackets[1]['min'] == Decimal('9000')
        assert brackets[1]['max'] is None
        assert brackets[1]['rate'] == Decimal('0.20')
    
    def test_calculate_its_progressive_first_bracket_only(self):
        """Test ITS calculation within first bracket only"""
        taxable_income = Decimal('5000')  # Below 9000 threshold
        
        result = ITSCalculator.calculate_its_progressive(
            taxable_income, is_expatriate=False
        )
        
        # 15% of 5000 = 750
        assert result['total'] == Decimal('750.00')
        assert result['tranche1'] == Decimal('750.00')
        assert result['tranche2'] == Decimal('0.00')
        assert result['tranche3'] == Decimal('0.00')
        assert result['taxable_income'] == taxable_income
    
    def test_calculate_its_progressive_two_brackets(self):
        """Test ITS calculation spanning both brackets"""
        taxable_income = Decimal('15000')  # Above 9000 threshold
        
        result = ITSCalculator.calculate_its_progressive(
            taxable_income, is_expatriate=False
        )
        
        # First bracket: 9000 * 15% = 1350
        # Second bracket: (15000 - 9000) * 20% = 1200
        # Total: 1350 + 1200 = 2550
        assert result['total'] == Decimal('2550.00')
        assert result['tranche1'] == Decimal('1350.00')
        assert result['tranche2'] == Decimal('1200.00')
        assert result['tranche3'] == Decimal('0.00')
    
    def test_calculate_its_progressive_expatriate_discount(self):
        """Test ITS calculation for expatriate with discount"""
        taxable_income = Decimal('5000')
        
        result_national = ITSCalculator.calculate_its_progressive(
            taxable_income, is_expatriate=False
        )
        result_expatriate = ITSCalculator.calculate_its_progressive(
            taxable_income, is_expatriate=True
        )
        
        # Expatriate should pay half in first bracket (7.5% vs 15%)
        assert result_expatriate['total'] == result_national['total'] / 2
        assert result_expatriate['tranche1'] == Decimal('375.00')  # 5000 * 7.5%
    
    def test_calculate_its_progressive_with_deductions(self):
        """Test ITS calculation with CNSS and CNAM deductions"""
        taxable_income = Decimal('10000')
        cnss_amount = Decimal('150')  # 1% of 15000 (capped)
        cnam_amount = Decimal('50')   # 0.5% of 10000
        
        result = ITSCalculator.calculate_its_progressive(
            taxable_income=taxable_income,
            cnss_amount=cnss_amount,
            cnam_amount=cnam_amount,
            deduct_cnss=True,
            deduct_cnam=True,
            is_expatriate=False
        )
        
        # Adjusted income: 10000 - 150 - 50 = 9800
        expected_adjusted = taxable_income - cnss_amount - cnam_amount
        assert result['taxable_income'] == expected_adjusted
        
        # First bracket: 9000 * 15% = 1350
        # Second bracket: (9800 - 9000) * 20% = 160
        # Total: 1350 + 160 = 1510
        assert result['total'] == Decimal('1510.00')
    
    def test_calculate_its_progressive_no_deductions(self):
        """Test ITS calculation without deducting CNSS/CNAM"""
        taxable_income = Decimal('10000')
        cnss_amount = Decimal('150')
        cnam_amount = Decimal('50')
        
        result = ITSCalculator.calculate_its_progressive(
            taxable_income=taxable_income,
            cnss_amount=cnss_amount,
            cnam_amount=cnam_amount,
            deduct_cnss=False,
            deduct_cnam=False,
            is_expatriate=False
        )
        
        # Should use full taxable income without deductions
        assert result['taxable_income'] == taxable_income
        
        # First bracket: 9000 * 15% = 1350
        # Second bracket: (10000 - 9000) * 20% = 200
        # Total: 1350 + 200 = 1550
        assert result['total'] == Decimal('1550.00')
    
    def test_calculate_its_progressive_with_abatement(self):
        """Test ITS calculation with tax abatement"""
        taxable_income = Decimal('10000')
        abatement = Decimal('2000')
        
        result = ITSCalculator.calculate_its_progressive(
            taxable_income=taxable_income,
            abatement=abatement,
            is_expatriate=False
        )
        
        # Adjusted income: 10000 - 2000 = 8000
        assert result['taxable_income'] == Decimal('8000')
        
        # All in first bracket: 8000 * 15% = 1200
        assert result['total'] == Decimal('1200.00')
        assert result['tranche1'] == Decimal('1200.00')
        assert result['tranche2'] == Decimal('0.00')
    
    def test_calculate_its_progressive_zero_income(self):
        """Test ITS calculation with zero taxable income"""
        result = ITSCalculator.calculate_its_progressive(
            taxable_income=Decimal('0'),
            is_expatriate=False
        )
        
        assert result['total'] == Decimal('0.00')
        assert result['tranche1'] == Decimal('0.00')
        assert result['tranche2'] == Decimal('0.00')
        assert result['taxable_income'] == Decimal('0')
    
    def test_calculate_its_progressive_negative_after_deductions(self):
        """Test ITS calculation when deductions exceed income"""
        taxable_income = Decimal('1000')
        cnss_amount = Decimal('500')
        cnam_amount = Decimal('600')  # Total deductions > income
        
        result = ITSCalculator.calculate_its_progressive(
            taxable_income=taxable_income,
            cnss_amount=cnss_amount,
            cnam_amount=cnam_amount,
            deduct_cnss=True,
            deduct_cnam=True,
            is_expatriate=False
        )
        
        # Should not go negative
        assert result['taxable_income'] == Decimal('0')
        assert result['total'] == Decimal('0.00')
    
    def test_calculate_its_progressive_with_currency_rate(self):
        """Test ITS calculation with currency conversion"""
        taxable_income = Decimal('5000')
        currency_rate = Decimal('2.0')
        
        result = ITSCalculator.calculate_its_progressive(
            taxable_income=taxable_income,
            currency_rate=currency_rate,
            is_expatriate=False
        )
        
        # 15% of 5000 * 2.0 = 1500
        assert result['total'] == Decimal('1500.00')
    
    def test_calculate_tranche_methods(self):
        """Test individual tranche calculation methods"""
        taxable_income = Decimal('15000')
        
        # Test tranche 1
        tranche1 = ITSCalculator.calculate_tranche1_its(
            taxable_income, is_expatriate=False
        )
        assert tranche1 == Decimal('1350.00')  # 9000 * 15%
        
        # Test tranche 2
        tranche2 = ITSCalculator.calculate_tranche2_its(
            taxable_income, is_expatriate=False
        )
        assert tranche2 == Decimal('1200.00')  # (15000 - 9000) * 20%
        
        # Test tranche 3 (should be 0 with current brackets)
        tranche3 = ITSCalculator.calculate_tranche3_its(
            taxable_income, is_expatriate=False
        )
        assert tranche3 == Decimal('0.00')
    
    def test_calculate_its_reimbursement_basic(self):
        """Test ITS reimbursement calculation"""
        taxable_income = Decimal('15000')
        tranche1_reimb_rate = Decimal('0.50')  # 50% reimbursement
        tranche2_reimb_rate = Decimal('0.30')  # 30% reimbursement
        
        result = ITSCalculator.calculate_its_reimbursement(
            taxable_income=taxable_income,
            tranche1_reimb_rate=tranche1_reimb_rate,
            tranche2_reimb_rate=tranche2_reimb_rate,
            is_expatriate=False
        )
        
        # Tranche 1: 1350 * 50% = 675
        # Tranche 2: 1200 * 30% = 360
        # Total: 675 + 360 = 1035
        assert result == Decimal('1035.00')
    
    def test_calculate_its_reimbursement_zero_rates(self):
        """Test ITS reimbursement with zero rates"""
        taxable_income = Decimal('15000')
        
        result = ITSCalculator.calculate_its_reimbursement(
            taxable_income=taxable_income,
            tranche1_reimb_rate=Decimal('0'),
            tranche2_reimb_rate=Decimal('0'),
            tranche3_reimb_rate=Decimal('0'),
            is_expatriate=False
        )
        
        assert result == Decimal('0.00')


class TestTaxCalculationService:
    """Test TaxCalculationService class methods"""
    
    def setup_method(self):
        """Set up test instances"""
        self.system_parameters = Mock()
        self.system_parameters.deduct_cnss_from_its = True
        self.system_parameters.deduct_cnam_from_its = True
        self.system_parameters.tax_abatement = Decimal('0')
        
        self.service = TaxCalculationService(self.system_parameters)
        
        # Mock employee
        self.employee = Mock()
        self.employee.is_expatriate = False
        self.employee.cnss_reimbursement_rate = Decimal('0')
        self.employee.cnam_reimbursement_rate = Decimal('0')
        self.employee.its_tranche1_reimbursement = Decimal('0')
        self.employee.its_tranche2_reimbursement = Decimal('0')
        self.employee.its_tranche3_reimbursement = Decimal('0')
        
        # Set up employee tax subject methods
        self.employee.is_subject_to_cnss.return_value = True
        self.employee.is_subject_to_cnam.return_value = True
        self.employee.is_subject_to_its.return_value = True
    
    def test_calculate_all_taxes_full_scenario(self):
        """Test complete tax calculation for employee subject to all taxes"""
        taxable_salaries = {
            'cnss': Decimal('50000'),
            'cnam': Decimal('50000'),
            'its': Decimal('50000'),
            'benefits_in_kind': Decimal('0')
        }
        
        result = self.service.calculate_all_taxes(self.employee, taxable_salaries)
        
        # Verify all tax components are calculated
        assert 'cnss_employee' in result
        assert 'cnss_employer' in result
        assert 'cnam_employee' in result
        assert 'cnam_employer' in result
        assert 'its_total' in result
        assert 'its_tranche1' in result
        assert 'its_tranche2' in result
        assert 'its_tranche3' in result
        assert 'its_reimbursement' in result
        
        # Verify values are positive
        assert result['cnss_employee'] > Decimal('0')
        assert result['cnss_employer'] > Decimal('0')
        assert result['cnam_employee'] > Decimal('0')
        assert result['cnam_employer'] > Decimal('0')
        assert result['its_total'] > Decimal('0')
    
    def test_calculate_all_taxes_cnss_ceiling_applied(self):
        """Test that CNSS ceiling is properly applied"""
        taxable_salaries = {
            'cnss': Decimal('25000'),  # Above CNSS ceiling
            'cnam': Decimal('25000'),
            'its': Decimal('25000'),
        }
        
        result = self.service.calculate_all_taxes(self.employee, taxable_salaries)
        
        # CNSS should be capped at ceiling (15000 * rates)
        assert result['cnss_employee'] == Decimal('150.00')  # 15000 * 1%
        assert result['cnss_employer'] == Decimal('300.00')  # 15000 * 2%
        
        # CNAM should not be capped
        assert result['cnam_employee'] == Decimal('125.00')  # 25000 * 0.5%
        assert result['cnam_employer'] == Decimal('375.00')  # 25000 * 1.5%
    
    def test_calculate_all_taxes_employee_not_subject_to_cnss(self):
        """Test calculation when employee is not subject to CNSS"""
        self.employee.is_subject_to_cnss.return_value = False
        
        taxable_salaries = {
            'cnss': Decimal('50000'),
            'cnam': Decimal('50000'),
            'its': Decimal('50000'),
        }
        
        result = self.service.calculate_all_taxes(self.employee, taxable_salaries)
        
        # CNSS should be zero
        assert result['cnss_employee'] == Decimal('0.00')
        assert result['cnss_employer'] == Decimal('0.00')
        
        # Other taxes should still be calculated
        assert result['cnam_employee'] > Decimal('0')
        assert result['its_total'] > Decimal('0')
    
    def test_calculate_all_taxes_employee_not_subject_to_cnam(self):
        """Test calculation when employee is not subject to CNAM"""
        self.employee.is_subject_to_cnam.return_value = False
        
        taxable_salaries = {
            'cnss': Decimal('50000'),
            'cnam': Decimal('50000'),
            'its': Decimal('50000'),
        }
        
        result = self.service.calculate_all_taxes(self.employee, taxable_salaries)
        
        # CNAM should be zero
        assert result['cnam_employee'] == Decimal('0.00')
        assert result['cnam_employer'] == Decimal('0.00')
        
        # Other taxes should still be calculated
        assert result['cnss_employee'] > Decimal('0')
        assert result['its_total'] > Decimal('0')
    
    def test_calculate_all_taxes_employee_not_subject_to_its(self):
        """Test calculation when employee is not subject to ITS"""
        self.employee.is_subject_to_its.return_value = False
        
        taxable_salaries = {
            'cnss': Decimal('50000'),
            'cnam': Decimal('50000'),
            'its': Decimal('50000'),
        }
        
        result = self.service.calculate_all_taxes(self.employee, taxable_salaries)
        
        # ITS should be zero
        assert result['its_total'] == Decimal('0.00')
        assert result['its_tranche1'] == Decimal('0.00')
        assert result['its_tranche2'] == Decimal('0.00')
        assert result['its_tranche3'] == Decimal('0.00')
        assert result['its_reimbursement'] == Decimal('0.00')
        
        # Other taxes should still be calculated
        assert result['cnss_employee'] > Decimal('0')
        assert result['cnam_employee'] > Decimal('0')
    
    def test_calculate_all_taxes_with_reimbursements(self):
        """Test calculation with reimbursement rates"""
        self.employee.cnss_reimbursement_rate = Decimal('0.25')  # 25%
        self.employee.cnam_reimbursement_rate = Decimal('0.15')  # 15%
        self.employee.its_tranche1_reimbursement = Decimal('0.50')  # 50%
        
        taxable_salaries = {
            'cnss': Decimal('10000'),
            'cnam': Decimal('10000'),
            'its': Decimal('5000'),  # Within first ITS bracket
        }
        
        result = self.service.calculate_all_taxes(self.employee, taxable_salaries)
        
        # Employer contributions should be affected by reimbursement rates
        expected_cnss_employer = Decimal('10000') * Decimal('0.02') * (Decimal('1') + Decimal('0.25'))
        expected_cnam_employer = Decimal('10000') * Decimal('0.015') * (Decimal('1') + Decimal('0.15'))
        
        assert result['cnss_employer'] == expected_cnss_employer
        assert result['cnam_employer'] == expected_cnam_employer
        
        # ITS reimbursement should be calculated
        assert result['its_reimbursement'] > Decimal('0')
    
    def test_calculate_all_taxes_expatriate_employee(self):
        """Test calculation for expatriate employee"""
        self.employee.is_expatriate = True
        
        taxable_salaries = {
            'cnss': Decimal('10000'),
            'cnam': Decimal('10000'),
            'its': Decimal('5000'),  # Within first bracket
        }
        
        result_expatriate = self.service.calculate_all_taxes(self.employee, taxable_salaries)
        
        # Compare with national employee
        self.employee.is_expatriate = False
        result_national = self.service.calculate_all_taxes(self.employee, taxable_salaries)
        
        # CNSS and CNAM should be the same
        assert result_expatriate['cnss_employee'] == result_national['cnss_employee']
        assert result_expatriate['cnam_employee'] == result_national['cnam_employee']
        
        # ITS should be lower for expatriate (half rate in first bracket)
        assert result_expatriate['its_total'] < result_national['its_total']
    
    def test_get_tax_summary(self):
        """Test comprehensive tax summary generation"""
        taxable_salaries = {
            'cnss': Decimal('20000'),
            'cnam': Decimal('20000'),
            'its': Decimal('15000'),
        }
        
        summary = self.service.get_tax_summary(self.employee, taxable_salaries)
        
        # Verify summary structure
        assert 'employee_contributions' in summary
        assert 'employer_contributions' in summary
        assert 'reimbursements' in summary
        assert 'its_breakdown' in summary
        
        # Verify employee contributions
        employee_contrib = summary['employee_contributions']
        assert 'cnss' in employee_contrib
        assert 'cnam' in employee_contrib
        assert 'its' in employee_contrib
        assert 'total' in employee_contrib
        
        # Verify total calculations
        expected_employee_total = (
            employee_contrib['cnss'] + 
            employee_contrib['cnam'] + 
            employee_contrib['its']
        )
        assert employee_contrib['total'] == expected_employee_total
        
        # Verify employer contributions
        employer_contrib = summary['employer_contributions']
        assert 'cnss' in employer_contrib
        assert 'cnam' in employer_contrib
        assert 'total' in employer_contrib
        
        expected_employer_total = employer_contrib['cnss'] + employer_contrib['cnam']
        assert employer_contrib['total'] == expected_employer_total
        
        # Verify ITS breakdown
        its_breakdown = summary['its_breakdown']
        assert 'tranche1' in its_breakdown
        assert 'tranche2' in its_breakdown
        assert 'tranche3' in its_breakdown
        assert 'total' in its_breakdown
        
        expected_its_total = (
            its_breakdown['tranche1'] + 
            its_breakdown['tranche2'] + 
            its_breakdown['tranche3']
        )
        assert its_breakdown['total'] == expected_its_total


class TestTaxCalculationsIntegration:
    """Integration tests for tax calculations"""
    
    def test_full_mauritanian_payroll_tax_calculation(self):
        """Test complete Mauritanian payroll tax calculation scenario"""
        # High-earning employee scenario
        employee = Mock()
        employee.is_expatriate = False
        employee.cnss_reimbursement_rate = Decimal('0')
        employee.cnam_reimbursement_rate = Decimal('0')
        employee.its_tranche1_reimbursement = Decimal('0')
        employee.its_tranche2_reimbursement = Decimal('0')
        employee.its_tranche3_reimbursement = Decimal('0')
        employee.is_subject_to_cnss.return_value = True
        employee.is_subject_to_cnam.return_value = True
        employee.is_subject_to_its.return_value = True
        
        taxable_salaries = {
            'cnss': Decimal('80000'),  # Above CNSS ceiling
            'cnam': Decimal('80000'),  # No CNAM ceiling
            'its': Decimal('80000'),   # Multiple ITS brackets
        }
        
        service = TaxCalculationService()
        result = service.calculate_all_taxes(employee, taxable_salaries)
        
        # Verify CNSS ceiling is applied
        assert result['cnss_employee'] == Decimal('150.00')  # Capped at 15000 * 1%
        assert result['cnss_employer'] == Decimal('300.00')  # Capped at 15000 * 2%
        
        # Verify CNAM has no ceiling
        assert result['cnam_employee'] == Decimal('400.00')  # 80000 * 0.5%
        assert result['cnam_employer'] == Decimal('1200.00')  # 80000 * 1.5%
        
        # Verify ITS spans multiple brackets
        assert result['its_tranche1'] > Decimal('0')  # First 9000
        assert result['its_tranche2'] > Decimal('0')  # Above 9000
        assert result['its_total'] > Decimal('10000')  # Significant amount for high earner
    
    def test_expatriate_vs_national_tax_comparison(self):
        """Test tax differences between expatriate and national employees"""
        # Setup identical employees except expatriate status
        base_employee_config = {
            'cnss_reimbursement_rate': Decimal('0'),
            'cnam_reimbursement_rate': Decimal('0'),
            'its_tranche1_reimbursement': Decimal('0'),
            'its_tranche2_reimbursement': Decimal('0'),
            'its_tranche3_reimbursement': Decimal('0'),
        }
        
        national_employee = Mock(**base_employee_config)
        national_employee.is_expatriate = False
        national_employee.is_subject_to_cnss.return_value = True
        national_employee.is_subject_to_cnam.return_value = True
        national_employee.is_subject_to_its.return_value = True
        
        expatriate_employee = Mock(**base_employee_config)
        expatriate_employee.is_expatriate = True
        expatriate_employee.is_subject_to_cnss.return_value = True
        expatriate_employee.is_subject_to_cnam.return_value = True
        expatriate_employee.is_subject_to_its.return_value = True
        
        taxable_salaries = {
            'cnss': Decimal('30000'),
            'cnam': Decimal('30000'),
            'its': Decimal('30000'),
        }
        
        service = TaxCalculationService()
        
        national_taxes = service.calculate_all_taxes(national_employee, taxable_salaries)
        expatriate_taxes = service.calculate_all_taxes(expatriate_employee, taxable_salaries)
        
        # CNSS and CNAM should be identical
        assert national_taxes['cnss_employee'] == expatriate_taxes['cnss_employee']
        assert national_taxes['cnss_employer'] == expatriate_taxes['cnss_employer']
        assert national_taxes['cnam_employee'] == expatriate_taxes['cnam_employee']
        assert national_taxes['cnam_employer'] == expatriate_taxes['cnam_employer']
        
        # ITS should be lower for expatriate (reduced first bracket rate)
        assert expatriate_taxes['its_total'] < national_taxes['its_total']
        assert expatriate_taxes['its_tranche1'] < national_taxes['its_tranche1']
        # Second bracket should be the same rate
        assert expatriate_taxes['its_tranche2'] == national_taxes['its_tranche2']
    
    def test_boundary_values_tax_calculations(self):
        """Test tax calculations at critical boundary values"""
        employee = Mock()
        employee.is_expatriate = False
        employee.cnss_reimbursement_rate = Decimal('0')
        employee.cnam_reimbursement_rate = Decimal('0')
        employee.its_tranche1_reimbursement = Decimal('0')
        employee.its_tranche2_reimbursement = Decimal('0')
        employee.its_tranche3_reimbursement = Decimal('0')
        employee.is_subject_to_cnss.return_value = True
        employee.is_subject_to_cnam.return_value = True
        employee.is_subject_to_its.return_value = True
        
        service = TaxCalculationService()
        
        # Test at CNSS ceiling
        cnss_ceiling_salaries = {
            'cnss': Decimal('15000'),  # Exactly at ceiling
            'cnam': Decimal('15000'),
            'its': Decimal('15000'),
        }
        
        result_at_ceiling = service.calculate_all_taxes(employee, cnss_ceiling_salaries)
        
        # Test above CNSS ceiling
        above_ceiling_salaries = {
            'cnss': Decimal('15001'),  # 1 MRU above ceiling
            'cnam': Decimal('15001'),
            'its': Decimal('15001'),
        }
        
        result_above_ceiling = service.calculate_all_taxes(employee, above_ceiling_salaries)
        
        # CNSS should be the same (capped)
        assert result_at_ceiling['cnss_employee'] == result_above_ceiling['cnss_employee']
        assert result_at_ceiling['cnss_employer'] == result_above_ceiling['cnss_employer']
        
        # CNAM should be different (no cap)
        assert result_above_ceiling['cnam_employee'] > result_at_ceiling['cnam_employee']
        assert result_above_ceiling['cnam_employer'] > result_at_ceiling['cnam_employer']
        
        # Test at ITS bracket boundary
        its_bracket_salaries = {
            'cnss': Decimal('9000'),  # At ITS first bracket limit
            'cnam': Decimal('9000'),
            'its': Decimal('9000'),
        }
        
        result_at_bracket = service.calculate_all_taxes(employee, its_bracket_salaries)
        
        # Should only have first bracket ITS
        assert result_at_bracket['its_tranche1'] > Decimal('0')
        assert result_at_bracket['its_tranche2'] == Decimal('0')
    
    def test_zero_and_negative_scenarios(self):
        """Test tax calculations with zero and edge case values"""
        employee = Mock()
        employee.is_expatriate = False
        employee.cnss_reimbursement_rate = Decimal('0')
        employee.cnam_reimbursement_rate = Decimal('0')
        employee.its_tranche1_reimbursement = Decimal('0')
        employee.its_tranche2_reimbursement = Decimal('0')
        employee.its_tranche3_reimbursement = Decimal('0')
        employee.is_subject_to_cnss.return_value = True
        employee.is_subject_to_cnam.return_value = True
        employee.is_subject_to_its.return_value = True
        
        service = TaxCalculationService()
        
        # Test with zero salaries
        zero_salaries = {
            'cnss': Decimal('0'),
            'cnam': Decimal('0'),
            'its': Decimal('0'),
        }
        
        result_zero = service.calculate_all_taxes(employee, zero_salaries)
        
        # All taxes should be zero
        assert result_zero['cnss_employee'] == Decimal('0.00')
        assert result_zero['cnss_employer'] == Decimal('0.00')
        assert result_zero['cnam_employee'] == Decimal('0.00')
        assert result_zero['cnam_employer'] == Decimal('0.00')
        assert result_zero['its_total'] == Decimal('0.00')
    
    def test_rounding_precision_verification(self):
        """Test that all tax calculations maintain proper decimal precision"""
        employee = Mock()
        employee.is_expatriate = False
        employee.cnss_reimbursement_rate = Decimal('0')
        employee.cnam_reimbursement_rate = Decimal('0')
        employee.its_tranche1_reimbursement = Decimal('0')
        employee.its_tranche2_reimbursement = Decimal('0')
        employee.its_tranche3_reimbursement = Decimal('0')
        employee.is_subject_to_cnss.return_value = True
        employee.is_subject_to_cnam.return_value = True
        employee.is_subject_to_its.return_value = True
        
        # Use amounts that will create decimal fractions
        taxable_salaries = {
            'cnss': Decimal('10001'),  # Creates 100.01 for CNSS
            'cnam': Decimal('10001'),  # Creates 50.005 for CNAM (rounds to 50.01)
            'its': Decimal('10001'),
        }
        
        service = TaxCalculationService()
        result = service.calculate_all_taxes(employee, taxable_salaries)
        
        # Verify all results have exactly 2 decimal places
        for key, value in result.items():
            if isinstance(value, Decimal):
                decimal_places = len(str(value).split('.')[-1]) if '.' in str(value) else 0
                assert decimal_places <= 2, f"{key} has more than 2 decimal places: {value}"
                
                # Verify proper rounding was applied
                assert value == value.quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)