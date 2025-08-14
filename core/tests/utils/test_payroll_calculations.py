"""
Tests for core.utils.payroll_calculations module.

This module provides comprehensive test coverage for the PayrollFunctions,
PayrollCalculator, OvertimeCalculator, and InstallmentCalculator classes
that implement Mauritanian payroll calculation logic.
"""

import pytest
from decimal import Decimal, ROUND_HALF_UP
from datetime import date, datetime
from unittest.mock import Mock, MagicMock, patch

from core.utils.payroll_calculations import (
    PayrollCalculator,
    OvertimeCalculator,
    InstallmentCalculator
)


class MockSystemParameters:
    """Mock system parameters for testing"""
    
    def __init__(self):
        # Tax settings
        self.taux_cnss_employe = Decimal('0.01')  # 1%
        self.taux_cnss_employeur = Decimal('0.02')  # 2%
        self.taux_cnam_employe = Decimal('0.04')  # 4%
        self.taux_cnam_employeur = Decimal('0.055')  # 5.5%
        self.plafond_cnss = Decimal('15000')  # MRU 15,000
        
        # ITS tax brackets
        self.its_tranche1_limit = Decimal('9000')
        self.its_tranche2_limit = Decimal('21000')
        self.its_tranche1_rate = Decimal('0.15')  # 15%
        self.its_tranche2_rate = Decimal('0.25')  # 25%
        self.its_tranche3_rate = Decimal('0.40')  # 40%
        self.its_expatriate_reduction = Decimal('0.50')  # 50% reduction
        
        # Seniority settings
        self.taux_anciennete_standard = Decimal('0.02')  # 2% per year
        self.taux_anciennete_max = Decimal('0.30')  # 30% max
        self.anciennete_max_years = 16
        
        # Benefits in kind
        self.bik_abattement_threshold = Decimal('0.20')  # 20%
        self.bik_abattement_rate = Decimal('0.60')  # 60%
        
        # SMIG (minimum wage)
        self.smig_mensuel = Decimal('30000')  # MRU 30,000
        self.heures_travail_mensuel = Decimal('173')  # Standard monthly hours


class MockEmployee:
    """Mock employee for testing"""
    
    def __init__(self):
        self.id = 1
        self.first_name = "John"
        self.last_name = "Doe"
        self.hire_date = date(2020, 1, 1)
        self.birth_date = date(1985, 6, 15)
        self.contract_hours = Decimal('8')
        self.base_salary = Decimal('50000')
        self.is_expatriate = False
        self.number_of_children = 2


class MockPayrollCalculator:
    """Mock payroll calculator for testing"""
    
    def __init__(self):
        self.system_parameters = MockSystemParameters()
        
    def get_njt_record(self, employee, motif, period):
        """Mock NJT (working days) record"""
        record = Mock()
        record.njt = 22  # Standard working days
        return record
    
    def get_rubrique_paie_record(self, employee, rubrique_id, motif, period):
        """Mock salary component record"""
        record = Mock()
        record.base = employee.base_salary / 30  # Daily salary
        record.quantite = 1
        record.montant = record.base
        return record
    
    def get_used_rub_id(self, component_id):
        """Mock salary component ID mapping"""
        return component_id


class TestPayrollFunctions:
    """Test PayrollFunctions class methods (F01-F24)"""
    
    def setup_method(self):
        """Set up test instances"""
        self.system_parameters = MockSystemParameters()
        self.payroll_calculator = MockPayrollCalculator()
        self.functions = PayrollFunctions(self.system_parameters, self.payroll_calculator)
        self.employee = MockEmployee()
        self.motif = Mock()
        self.period = "202312"
    
    def test_F01_NJT_with_record(self):
        """Test F01 - Number of Working Days with existing record"""
        result = self.functions.F01_NJT(self.employee, self.motif, self.period)
        assert result == Decimal('22')
    
    def test_F01_NJT_without_record(self):
        """Test F01 - Number of Working Days without record"""
        self.payroll_calculator.get_njt_record = Mock(return_value=None)
        result = self.functions.F01_NJT(self.employee, self.motif, self.period)
        assert result == Decimal('0.00')
    
    def test_F02_sbJour_with_record(self):
        """Test F02 - Daily Base Salary with existing record"""
        result = self.functions.F02_sbJour(self.employee, self.motif, self.period)
        expected = self.employee.base_salary / 30
        assert result == expected
    
    def test_F02_sbJour_without_record(self):
        """Test F02 - Daily Base Salary without record"""
        self.payroll_calculator.get_rubrique_paie_record = Mock(return_value=None)
        result = self.functions.F02_sbJour(self.employee, self.motif, self.period)
        assert result == Decimal('0.00')
    
    def test_F03_sbHoraire_normal_case(self):
        """Test F03 - Hourly Base Salary normal calculation"""
        # Mock F02 to return daily salary
        with patch.object(self.functions, 'F02_sbJour', return_value=Decimal('1666.67')):
            result = self.functions.F03_sbHoraire(self.employee, self.motif, self.period)
            expected = Decimal('1666.67') / self.employee.contract_hours
            assert result == expected
    
    def test_F03_sbHoraire_zero_hours(self):
        """Test F03 - Hourly Base Salary with zero contract hours"""
        self.employee.contract_hours = Decimal('0')
        with patch.object(self.functions, 'F02_sbJour', return_value=Decimal('1666.67')):
            result = self.functions.F03_sbHoraire(self.employee, self.motif, self.period)
            assert result == Decimal('0.00')
    
    def test_F04_TauxAnciennete_normal_case(self):
        """Test F04 - Standard Seniority Rate calculation"""
        # Employee hired in 2020, calculating for 2023 = 3 years
        current_date = date(2023, 12, 31)
        
        with patch('core.utils.payroll_calculations.date') as mock_date:
            mock_date.today.return_value = current_date
            result = self.functions.F04_TauxAnciennete(self.employee, self.motif, self.period)
            
            # 3 years * 2% = 6%
            expected = Decimal('0.06')
            assert result == expected
    
    def test_F04_TauxAnciennete_max_cap(self):
        """Test F04 - Seniority Rate with maximum cap"""
        # Employee hired 20 years ago (exceeds 16-year cap)
        self.employee.hire_date = date(2000, 1, 1)
        current_date = date(2023, 12, 31)
        
        with patch('core.utils.payroll_calculations.date') as mock_date:
            mock_date.today.return_value = current_date
            result = self.functions.F04_TauxAnciennete(self.employee, self.motif, self.period)
            
            # Should cap at 30% (16 years * 2%)
            expected = Decimal('0.30')
            assert result == expected
    
    def test_F04_TauxAnciennete_no_hire_date(self):
        """Test F04 - Seniority Rate with no hire date"""
        self.employee.hire_date = None
        result = self.functions.F04_TauxAnciennete(self.employee, self.motif, self.period)
        assert result == Decimal('0.00')
    
    def test_F05_to_F08_cumulative_functions(self):
        """Test cumulative functions F05-F08 (should return 0 in basic implementation)"""
        assert self.functions.F05_cumulBIDerDepart(self.employee, self.motif, self.period) == Decimal('0.00')
        assert self.functions.F06_cumulBNIDerDepart(self.employee, self.motif, self.period) == Decimal('0.00')
        assert self.functions.F07_cumulRETDerDepart(self.employee, self.motif, self.period) == Decimal('0.00')
        assert self.functions.F08_cumulBrut12DerMois(self.employee, self.motif, self.period) == Decimal('0.00')
    
    def test_F09_salaireBrutMensuelFixe(self):
        """Test F09 - Fixed Monthly Gross Salary"""
        result = self.functions.F09_salaireBrutMensuelFixe(self.employee, self.motif, self.period)
        assert result == self.employee.base_salary
    
    def test_F10_smig(self):
        """Test F10 - Minimum Wage (SMIG)"""
        result = self.functions.F10_smig(self.employee, self.motif, self.period)
        assert result == self.system_parameters.smig_mensuel
    
    def test_F11_smigHoraire(self):
        """Test F11 - Hourly Minimum Wage"""
        result = self.functions.F11_smigHoraire(self.employee, self.motif, self.period)
        expected = self.system_parameters.smig_mensuel / self.system_parameters.heures_travail_mensuel
        assert result == expected
    
    def test_F12_TauxLicenciement_early_years(self):
        """Test F12 - Dismissal Rate for early years (progressive)"""
        # 3 years of service
        current_date = date(2023, 12, 31)
        
        with patch('core.utils.payroll_calculations.date') as mock_date:
            mock_date.today.return_value = current_date
            result = self.functions.F12_TauxLicenciement(self.employee, self.motif, self.period)
            
            # Progressive rate for 3 years should be calculated
            assert result >= Decimal('0')
    
    def test_F13_TauxLicenciementCollectif(self):
        """Test F13 - Collective Dismissal Rate (higher than individual)"""
        current_date = date(2023, 12, 31)
        
        with patch('core.utils.payroll_calculations.date') as mock_date:
            mock_date.today.return_value = current_date
            individual_rate = self.functions.F12_TauxLicenciement(self.employee, self.motif, self.period)
            collective_rate = self.functions.F13_TauxLicenciementCollectif(self.employee, self.motif, self.period)
            
            # Collective rate should be higher than individual
            assert collective_rate >= individual_rate
    
    def test_F14_TauxRetraite(self):
        """Test F14 - Retirement Benefits Rate"""
        result = self.functions.F14_TauxRetraite(self.employee, self.motif, self.period)
        # Should be based on dismissal rate
        assert result >= Decimal('0')
    
    def test_F15_to_F17_special_functions(self):
        """Test special functions F15-F17"""
        # These return 0 in basic implementation
        assert self.functions.F15_TauxPSRA(self.employee, self.motif, self.period) == Decimal('0.00')
        assert self.functions.F16_TauxPreavis(self.employee, self.motif, self.period) == Decimal('0.00')
        assert self.functions.F17_CumulNJTMC(self.employee, self.motif, self.period) == Decimal('0.00')
    
    def test_F18_NbSmigRegion(self):
        """Test F18 - Regional SMIG Number"""
        # Default implementation
        result = self.functions.F18_NbSmigRegion(self.employee, self.motif, self.period)
        assert result == Decimal('1.00')
    
    def test_F19_TauxPresence_full_year(self):
        """Test F19 - Attendance Rate for full year"""
        # Full year employment (should be 1.0)
        result = self.functions.F19_TauxPresence(self.employee, self.motif, self.period)
        assert result <= Decimal('1.0')
    
    def test_F20_BaseIndLogement(self):
        """Test F20 - Housing Allowance Base"""
        result = self.functions.F20_BaseIndLogement(self.employee, self.motif, self.period)
        # Should return some housing allowance base
        assert result >= Decimal('0')
    
    def test_F21_salaireNet(self):
        """Test F21 - Net Salary calculation"""
        # Mock other functions that F21 depends on
        with patch.object(self.functions, 'F09_salaireBrutMensuelFixe', return_value=Decimal('50000')):
            result = self.functions.F21_salaireNet(self.employee, self.motif, self.period)
            # Net salary should be less than gross due to deductions
            assert result <= Decimal('50000')
            assert result > Decimal('0')
    
    def test_F22_NbEnfants(self):
        """Test F22 - Number of Children"""
        result = self.functions.F22_NbEnfants(self.employee, self.motif, self.period)
        assert result == Decimal(str(self.employee.number_of_children))
    
    def test_F23_TauxAncienneteSpeciale(self):
        """Test F23 - Special Seniority Rate (no cap)"""
        current_date = date(2023, 12, 31)
        
        with patch('core.utils.payroll_calculations.date') as mock_date:
            mock_date.today.return_value = current_date
            result = self.functions.F23_TauxAncienneteSpeciale(self.employee, self.motif, self.period)
            
            # Should continue growing beyond 16 years (no cap)
            assert result >= Decimal('0')
    
    def test_F24_augmentationSalaireFixe(self):
        """Test F24 - Fixed Salary Increase"""
        result = self.functions.F24_augmentationSalaireFixe(self.employee, self.motif, self.period)
        # Default implementation returns 0
        assert result == Decimal('0.00')
    
    def test_F23X_TauxAncienneteSpeciale_alternative(self):
        """Test F23X - Alternative Special Seniority Rate"""
        current_date = date(2023, 12, 31)
        
        with patch('core.utils.payroll_calculations.date') as mock_date:
            mock_date.today.return_value = current_date
            result = self.functions.F23X_TauxAncienneteSpeciale(self.employee, self.motif, self.period)
            
            # Different calculation than F23
            assert result >= Decimal('0')
    
    def test_calculate_function_dispatcher(self):
        """Test the function dispatcher method"""
        # Test valid function codes
        result_f01 = self.functions.calculate_function("F01", self.employee, self.motif, self.period)
        assert result_f01 >= Decimal('0')
        
        result_f10 = self.functions.calculate_function("F10", self.employee, self.motif, self.period)
        assert result_f10 == self.system_parameters.smig_mensuel
        
        # Test invalid function code
        result_invalid = self.functions.calculate_function("F99", self.employee, self.motif, self.period)
        assert result_invalid == Decimal('0.00')
    
    def test_all_functions_handle_none_employee(self):
        """Test that all functions handle None employee gracefully"""
        none_employee = None
        
        # Test a few key functions
        assert self.functions.F01_NJT(none_employee, self.motif, self.period) == Decimal('0.00')
        assert self.functions.F02_sbJour(none_employee, self.motif, self.period) == Decimal('0.00')
        assert self.functions.F10_smig(none_employee, self.motif, self.period) == self.system_parameters.smig_mensuel


class TestPayrollCalculator:
    """Test PayrollCalculator class methods"""
    
    def setup_method(self):
        """Set up test instances"""
        self.system_parameters = MockSystemParameters()
        self.calculator = PayrollCalculator(self.system_parameters)
        self.employee = MockEmployee()
    
    def test_calculate_cnss_employee_normal(self):
        """Test CNSS employee calculation within ceiling"""
        base_salary = Decimal('10000')  # Below ceiling
        result = self.calculator._calculate_cnss_employee(base_salary)
        expected = base_salary * self.system_parameters.taux_cnss_employe
        assert result == expected
    
    def test_calculate_cnss_employee_ceiling(self):
        """Test CNSS employee calculation at ceiling"""
        base_salary = Decimal('20000')  # Above ceiling
        result = self.calculator._calculate_cnss_employee(base_salary)
        expected = self.system_parameters.plafond_cnss * self.system_parameters.taux_cnss_employe
        assert result == expected
    
    def test_calculate_cnss_employer(self):
        """Test CNSS employer calculation"""
        base_salary = Decimal('10000')
        result = self.calculator._calculate_cnss_employer(base_salary)
        expected = base_salary * self.system_parameters.taux_cnss_employeur
        assert result == expected
    
    def test_calculate_cnam_employee(self):
        """Test CNAM employee calculation (no ceiling)"""
        base_salary = Decimal('25000')  # Above CNSS ceiling
        result = self.calculator._calculate_cnam_employee(base_salary)
        expected = base_salary * self.system_parameters.taux_cnam_employe
        assert result == expected
    
    def test_calculate_cnam_employer(self):
        """Test CNAM employer calculation"""
        base_salary = Decimal('25000')
        result = self.calculator._calculate_cnam_employer(base_salary)
        expected = base_salary * self.system_parameters.taux_cnam_employeur
        assert result == expected
    
    def test_calculate_its_tranche1_only(self):
        """Test ITS calculation for Tranche 1 only"""
        taxable_salary = Decimal('5000')  # Below first bracket limit
        result = self.calculator._calculate_its(taxable_salary, is_expatriate=False)
        expected = taxable_salary * self.system_parameters.its_tranche1_rate
        assert result == expected
    
    def test_calculate_its_tranche2(self):
        """Test ITS calculation spanning Tranche 1 and 2"""
        taxable_salary = Decimal('15000')  # Between first and second bracket
        result = self.calculator._calculate_its(taxable_salary, is_expatriate=False)
        
        # Tranche 1: 9000 * 15%
        tranche1 = self.system_parameters.its_tranche1_limit * self.system_parameters.its_tranche1_rate
        # Tranche 2: (15000 - 9000) * 25%
        tranche2 = (taxable_salary - self.system_parameters.its_tranche1_limit) * self.system_parameters.its_tranche2_rate
        expected = tranche1 + tranche2
        
        assert result == expected
    
    def test_calculate_its_all_tranches(self):
        """Test ITS calculation spanning all tranches"""
        taxable_salary = Decimal('30000')  # Above all brackets
        result = self.calculator._calculate_its(taxable_salary, is_expatriate=False)
        
        # Tranche 1: 9000 * 15%
        tranche1 = self.system_parameters.its_tranche1_limit * self.system_parameters.its_tranche1_rate
        # Tranche 2: (21000 - 9000) * 25%
        tranche2 = (self.system_parameters.its_tranche2_limit - self.system_parameters.its_tranche1_limit) * self.system_parameters.its_tranche2_rate
        # Tranche 3: (30000 - 21000) * 40%
        tranche3 = (taxable_salary - self.system_parameters.its_tranche2_limit) * self.system_parameters.its_tranche3_rate
        expected = tranche1 + tranche2 + tranche3
        
        assert result == expected
    
    def test_calculate_its_expatriate_reduction(self):
        """Test ITS calculation with expatriate reduction"""
        taxable_salary = Decimal('10000')
        result_expatriate = self.calculator._calculate_its(taxable_salary, is_expatriate=True)
        result_national = self.calculator._calculate_its(taxable_salary, is_expatriate=False)
        
        # Expatriate should pay 50% less
        expected_expatriate = result_national * self.system_parameters.its_expatriate_reduction
        assert result_expatriate == expected_expatriate


class TestOvertimeCalculator:
    """Test OvertimeCalculator class methods"""
    
    def setup_method(self):
        """Set up test instances"""
        self.calculator = OvertimeCalculator()
        self.hourly_rate = Decimal('300')  # MRU 300 per hour
    
    def test_calculate_overtime_rates_no_overtime(self):
        """Test overtime calculation with no overtime hours"""
        result = self.calculator.calculate_overtime_rates(Decimal('0'))
        expected = {
            'hours_115': Decimal('0'),
            'hours_140': Decimal('0'),
            'hours_150': Decimal('0'),
            'total_hours': Decimal('0')
        }
        assert result == expected
    
    def test_calculate_overtime_rates_first_bracket(self):
        """Test overtime calculation within first bracket (115%)"""
        overtime_hours = Decimal('5')  # Within first 8 hours
        result = self.calculator.calculate_overtime_rates(overtime_hours)
        expected = {
            'hours_115': Decimal('5'),
            'hours_140': Decimal('0'),
            'hours_150': Decimal('0'),
            'total_hours': Decimal('5')
        }
        assert result == expected
    
    def test_calculate_overtime_rates_second_bracket(self):
        """Test overtime calculation spanning first and second brackets"""
        overtime_hours = Decimal('12')  # 8 hours at 115% + 4 hours at 140%
        result = self.calculator.calculate_overtime_rates(overtime_hours)
        expected = {
            'hours_115': Decimal('8'),
            'hours_140': Decimal('4'),
            'hours_150': Decimal('0'),
            'total_hours': Decimal('12')
        }
        assert result == expected
    
    def test_calculate_overtime_rates_all_brackets(self):
        """Test overtime calculation spanning all brackets"""
        overtime_hours = Decimal('20')  # 8 + 6 + 6 hours
        result = self.calculator.calculate_overtime_rates(overtime_hours)
        expected = {
            'hours_115': Decimal('8'),
            'hours_140': Decimal('6'),
            'hours_150': Decimal('6'),
            'total_hours': Decimal('20')
        }
        assert result == expected
    
    def test_calculate_overtime_amounts(self):
        """Test conversion of overtime hours to payment amounts"""
        overtime_breakdown = {
            'hours_115': Decimal('8'),
            'hours_140': Decimal('4'),
            'hours_150': Decimal('2'),
            'total_hours': Decimal('14')
        }
        
        result = self.calculator.calculate_overtime_amounts(overtime_breakdown, self.hourly_rate)
        
        expected = {
            'amount_115': Decimal('8') * self.hourly_rate * Decimal('1.15'),
            'amount_140': Decimal('4') * self.hourly_rate * Decimal('1.40'),
            'amount_150': Decimal('2') * self.hourly_rate * Decimal('1.50'),
            'total_amount': Decimal('0')  # Will be calculated as sum
        }
        expected['total_amount'] = expected['amount_115'] + expected['amount_140'] + expected['amount_150']
        
        assert result == expected
    
    def test_calculate_holiday_overtime(self):
        """Test holiday overtime calculation at 200%"""
        holiday_hours = Decimal('8')
        result = self.calculator.calculate_holiday_overtime(holiday_hours, self.hourly_rate)
        expected = holiday_hours * self.hourly_rate * Decimal('2.00')  # 200%
        assert result == expected


class TestInstallmentCalculator:
    """Test InstallmentCalculator class methods"""
    
    def setup_method(self):
        """Set up test instances"""
        self.calculator = InstallmentCalculator()
        self.net_salary = Decimal('40000')  # MRU 40,000 net
    
    def test_calculate_quota_cessible_standard(self):
        """Test quota cessible calculation (1/3 of net salary)"""
        result = self.calculator.calculate_quota_cessible(self.net_salary)
        expected = self.net_salary / Decimal('3')  # 1/3 of net salary
        assert result == expected
    
    def test_calculate_quota_cessible_zero_salary(self):
        """Test quota cessible with zero net salary"""
        result = self.calculator.calculate_quota_cessible(Decimal('0'))
        assert result == Decimal('0')
    
    def test_adjust_installments_for_quota_within_limit(self):
        """Test installment adjustment when within quota"""
        quota = Decimal('10000')
        installments = [
            {'amount': Decimal('3000'), 'priority': 1},
            {'amount': Decimal('2000'), 'priority': 2},
            {'amount': Decimal('4000'), 'priority': 3}
        ]
        
        result = self.calculator.adjust_installments_for_quota(installments, quota)
        
        # Total is 9000, within quota of 10000, so no adjustment
        assert sum(inst['adjusted_amount'] for inst in result) == Decimal('9000')
    
    def test_adjust_installments_for_quota_exceeds_limit(self):
        """Test installment adjustment when exceeding quota"""
        quota = Decimal('5000')
        installments = [
            {'amount': Decimal('3000'), 'priority': 1},
            {'amount': Decimal('2000'), 'priority': 2},
            {'amount': Decimal('4000'), 'priority': 3}
        ]
        
        result = self.calculator.adjust_installments_for_quota(installments, quota)
        
        # Should prioritize by priority and cap at quota
        total_adjusted = sum(inst['adjusted_amount'] for inst in result)
        assert total_adjusted <= quota
        
        # Priority 1 should be fully satisfied
        priority_1 = next(inst for inst in result if inst['priority'] == 1)
        assert priority_1['adjusted_amount'] == Decimal('3000')
    
    def test_calculate_cnss_employee_mauritanian(self):
        """Test CNSS employee calculation for Mauritanian system"""
        base_salary = Decimal('50000')
        result = self.calculator.calculate_cnss_employee(base_salary)
        
        # 1% up to ceiling of MRU 15,000
        expected = min(base_salary, Decimal('15000')) * Decimal('0.01')
        assert result == expected
    
    def test_calculate_cnss_employer_with_reimbursement(self):
        """Test CNSS employer calculation with reimbursement rates"""
        base_salary = Decimal('50000')
        reimbursement_rate = Decimal('0.80')  # 80% reimbursement
        
        result = self.calculator.calculate_cnss_employer(base_salary, reimbursement_rate)
        
        # Should account for reimbursement
        gross_contribution = min(base_salary, Decimal('15000')) * Decimal('0.02')
        expected = gross_contribution * (Decimal('1') - reimbursement_rate)
        assert result == expected
    
    def test_calculate_cnam_employee_no_ceiling(self):
        """Test CNAM employee calculation (no ceiling)"""
        base_salary = Decimal('100000')  # High salary
        result = self.calculator.calculate_cnam_employee(base_salary)
        
        # 4% with no ceiling
        expected = base_salary * Decimal('0.04')
        assert result == expected
    
    def test_calculate_its_tranche_calculations(self):
        """Test individual ITS tranche calculations"""
        # Test Tranche 1
        result_t1 = self.calculator.calculate_its_tranche1(Decimal('5000'), is_expatriate=False)
        expected_t1 = Decimal('5000') * Decimal('0.15')
        assert result_t1 == expected_t1
        
        # Test Tranche 1 expatriate
        result_t1_exp = self.calculator.calculate_its_tranche1(Decimal('5000'), is_expatriate=True)
        expected_t1_exp = Decimal('5000') * Decimal('0.075')  # 7.5%
        assert result_t1_exp == expected_t1_exp
        
        # Test Tranche 2
        result_t2 = self.calculator.calculate_its_tranche2(Decimal('15000'), is_expatriate=False)
        # Only the portion above 9000 is taxed at Tranche 2 rate
        taxable_t2 = Decimal('15000') - Decimal('9000')
        expected_t2 = taxable_t2 * Decimal('0.25')
        assert result_t2 == expected_t2
        
        # Test Tranche 3
        result_t3 = self.calculator.calculate_its_tranche3(Decimal('30000'), is_expatriate=False)
        # Only the portion above 21000 is taxed at Tranche 3 rate
        taxable_t3 = Decimal('30000') - Decimal('21000')
        expected_t3 = taxable_t3 * Decimal('0.40')
        assert result_t3 == expected_t3
    
    def test_calculate_its_total_integration(self):
        """Test total ITS calculation integrating all tranches"""
        salary = Decimal('30000')
        result = self.calculator.calculate_its_total(salary, is_expatriate=False)
        
        # Should equal sum of individual tranches
        t1 = self.calculator.calculate_its_tranche1(salary, False)
        t2 = self.calculator.calculate_its_tranche2(salary, False)
        t3 = self.calculator.calculate_its_tranche3(salary, False)
        expected = t1 + t2 + t3
        
        assert result == expected
    
    def test_calculate_its_reimbursement(self):
        """Test ITS employer reimbursement calculation"""
        its_amount = Decimal('5000')
        reimbursement_rate = Decimal('0.60')  # 60% reimbursement
        
        result = self.calculator.calculate_its_reimbursement(its_amount, reimbursement_rate)
        expected = its_amount * reimbursement_rate
        assert result == expected


class TestPayrollCalculationsIntegration:
    """Integration tests for payroll calculations"""
    
    def setup_method(self):
        """Set up integration test environment"""
        self.system_parameters = MockSystemParameters()
        self.payroll_calculator = MockPayrollCalculator()
        self.functions = PayrollFunctions(self.system_parameters, self.payroll_calculator)
        self.employee = MockEmployee()
        self.motif = Mock()
        self.period = "202312"
    
    def test_complete_payroll_calculation_workflow(self):
        """Test complete payroll calculation from start to finish"""
        # Step 1: Get working days
        working_days = self.functions.F01_NJT(self.employee, self.motif, self.period)
        assert working_days > Decimal('0')
        
        # Step 2: Get daily salary
        daily_salary = self.functions.F02_sbJour(self.employee, self.motif, self.period)
        assert daily_salary > Decimal('0')
        
        # Step 3: Get gross monthly salary
        gross_salary = self.functions.F09_salaireBrutMensuelFixe(self.employee, self.motif, self.period)
        assert gross_salary == self.employee.base_salary
        
        # Step 4: Calculate seniority rate
        seniority_rate = self.functions.F04_TauxAnciennete(self.employee, self.motif, self.period)
        assert seniority_rate >= Decimal('0')
        
        # Step 5: Calculate net salary (includes all deductions)
        net_salary = self.functions.F21_salaireNet(self.employee, self.motif, self.period)
        assert net_salary <= gross_salary  # Net should be less than gross
        assert net_salary > Decimal('0')
    
    def test_tax_calculation_integration(self):
        """Test integration of all tax calculations"""
        calculator = PayrollCalculator(self.system_parameters)
        base_salary = Decimal('50000')
        
        # Calculate all taxes
        cnss_employee = calculator._calculate_cnss_employee(base_salary)
        cnss_employer = calculator._calculate_cnss_employer(base_salary)
        cnam_employee = calculator._calculate_cnam_employee(base_salary)
        cnam_employer = calculator._calculate_cnam_employer(base_salary)
        its_amount = calculator._calculate_its(base_salary, is_expatriate=False)
        
        # Verify all calculations are positive
        assert cnss_employee > Decimal('0')
        assert cnss_employer > Decimal('0')
        assert cnam_employee > Decimal('0')
        assert cnam_employer > Decimal('0')
        assert its_amount > Decimal('0')
        
        # Verify CNSS ceiling is applied
        assert cnss_employee == Decimal('150')  # 15000 * 1%
        assert cnss_employer == Decimal('300')  # 15000 * 2%
        
        # Verify CNAM has no ceiling
        assert cnam_employee == base_salary * Decimal('0.04')
        assert cnam_employer == base_salary * Decimal('0.055')
    
    def test_overtime_integration_with_payroll(self):
        """Test overtime calculations integrated with regular payroll"""
        overtime_calc = OvertimeCalculator()
        
        # Employee worked 15 hours of overtime
        overtime_hours = Decimal('15')
        hourly_rate = Decimal('250')
        
        # Calculate overtime breakdown
        overtime_rates = overtime_calc.calculate_overtime_rates(overtime_hours)
        overtime_amounts = overtime_calc.calculate_overtime_amounts(overtime_rates, hourly_rate)
        
        # Verify the breakdown
        assert overtime_rates['hours_115'] == Decimal('8')   # First 8 hours
        assert overtime_rates['hours_140'] == Decimal('6')   # Next 6 hours
        assert overtime_rates['hours_150'] == Decimal('1')   # Remaining 1 hour
        
        # Calculate total overtime payment
        total_overtime = overtime_amounts['total_amount']
        assert total_overtime > overtime_hours * hourly_rate  # Should be more than regular rate
    
    def test_installment_quota_integration(self):
        """Test installment calculations with quota cessible"""
        installment_calc = InstallmentCalculator()
        
        # Employee with net salary and multiple deductions
        net_salary = Decimal('35000')
        quota = installment_calc.calculate_quota_cessible(net_salary)
        
        # Multiple installments/deductions
        installments = [
            {'amount': Decimal('8000'), 'priority': 1, 'type': 'loan'},
            {'amount': Decimal('5000'), 'priority': 2, 'type': 'advance'},
            {'amount': Decimal('3000'), 'priority': 3, 'type': 'other'}
        ]
        
        # Adjust for quota
        adjusted_installments = installment_calc.adjust_installments_for_quota(installments, quota)
        
        # Verify quota is respected
        total_adjusted = sum(inst['adjusted_amount'] for inst in adjusted_installments)
        expected_quota = net_salary / Decimal('3')  # 1/3 of net
        assert total_adjusted <= expected_quota
    
    def test_expatriate_vs_national_calculations(self):
        """Test calculation differences between expatriate and national employees"""
        calculator = PayrollCalculator(self.system_parameters)
        base_salary = Decimal('60000')
        
        # Calculate ITS for national employee
        its_national = calculator._calculate_its(base_salary, is_expatriate=False)
        
        # Calculate ITS for expatriate employee
        its_expatriate = calculator._calculate_its(base_salary, is_expatriate=True)
        
        # Expatriate should pay 50% of what national pays
        expected_expatriate = its_national * Decimal('0.50')
        assert its_expatriate == expected_expatriate
        
        # Other taxes should be the same
        cnss_national = calculator._calculate_cnss_employee(base_salary)
        cnss_expatriate = calculator._calculate_cnss_employee(base_salary)
        assert cnss_national == cnss_expatriate
    
    def test_boundary_value_testing(self):
        """Test calculations at boundary values"""
        calculator = PayrollCalculator(self.system_parameters)
        
        # Test at CNSS ceiling
        cnss_ceiling = Decimal('15000')
        cnss_at_ceiling = calculator._calculate_cnss_employee(cnss_ceiling)
        cnss_above_ceiling = calculator._calculate_cnss_employee(cnss_ceiling + Decimal('1'))
        assert cnss_at_ceiling == cnss_above_ceiling  # Should be capped
        
        # Test at ITS bracket boundaries
        its_bracket1_limit = Decimal('9000')
        its_bracket2_limit = Decimal('21000')
        
        its_at_bracket1 = calculator._calculate_its(its_bracket1_limit, False)
        its_above_bracket1 = calculator._calculate_its(its_bracket1_limit + Decimal('1'), False)
        assert its_above_bracket1 > its_at_bracket1
        
        its_at_bracket2 = calculator._calculate_its(its_bracket2_limit, False)
        its_above_bracket2 = calculator._calculate_its(its_bracket2_limit + Decimal('1'), False)
        assert its_above_bracket2 > its_at_bracket2
    
    def test_error_handling_and_edge_cases(self):
        """Test error handling and edge cases"""
        # Test with zero values
        assert self.functions.F01_NJT(None, self.motif, self.period) == Decimal('0.00')
        assert self.functions.F02_sbJour(self.employee, None, self.period) == Decimal('0.00')
        
        # Test invalid function codes
        invalid_result = self.functions.calculate_function("INVALID", self.employee, self.motif, self.period)
        assert invalid_result == Decimal('0.00')
        
        # Test with negative values (should handle gracefully)
        calculator = PayrollCalculator(self.system_parameters)
        negative_salary = Decimal('-1000')
        cnss_negative = calculator._calculate_cnss_employee(negative_salary)
        assert cnss_negative == Decimal('0')  # Should not be negative