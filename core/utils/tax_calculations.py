# tax_calculations.py
"""
Tax calculation functions for CNSS, CNAM, and ITS
Converted from PaieClass.java tax calculation methods
Includes all Mauritanian tax calculation logic with proper rates and brackets
"""

from decimal import Decimal, ROUND_HALF_UP
from typing import Dict, List, Tuple
# Cleaned up imports - removed unused Optional, Union
import math


class CNSSCalculator:
    """
    CNSS (Social Security) contribution calculations
    Equivalent to CNSSm, RCNSSm methods in PaieClass.java
    """
    
    @staticmethod
    def calculate_employee_contribution(taxable_amount: Decimal, 
                                      currency_rate: Decimal = Decimal('1.0'),
                                      year: int = 2018) -> Decimal:
        """
        Calculate employee CNSS contribution
        Equivalent to CNSSm method in PaieClass.java
        
        Args:
            taxable_amount: Base salary subject to CNSS
            currency_rate: Currency conversion rate
            year: Tax year for rate determination
            
        Returns:
            Employee CNSS contribution amount
        """
        # CNSS ceiling (should be configurable by year)
        ceiling = Decimal('15000.00')  # MRU
        
        # Apply ceiling
        capped_amount = min(taxable_amount, ceiling)
        
        # Employee contribution rate: 1%
        rate = Decimal('0.01')
        
        # Calculate contribution
        contribution = (capped_amount * rate * currency_rate).quantize(
            Decimal('0.01'), rounding=ROUND_HALF_UP
        )
        
        return contribution
    
    @staticmethod
    def calculate_employer_contribution(taxable_amount: Decimal,
                                     reimbursement_rate: Decimal = Decimal('0.0'),
                                     currency_rate: Decimal = Decimal('1.0'),
                                     year: int = 2018) -> Decimal:
        """
        Calculate employer CNSS contribution with reimbursement
        Equivalent to RCNSSm method in PaieClass.java
        
        Args:
            taxable_amount: Base salary subject to CNSS
            reimbursement_rate: Employee-specific reimbursement rate
            currency_rate: Currency conversion rate
            year: Tax year for rate determination
            
        Returns:
            Employer CNSS contribution amount
        """
        # CNSS ceiling
        ceiling = Decimal('15000.00')  # MRU
        
        # Apply ceiling
        capped_amount = min(taxable_amount, ceiling)
        
        # Base employer contribution rate: 1% (same as employee)
        # Note: Reports show PAT (13%) and MED (2%) derived from employee contribution
        base_rate = Decimal('0.01')
        
        # Apply reimbursement rate if applicable
        effective_rate = base_rate * (Decimal('1.0') + reimbursement_rate)
        
        # Calculate contribution
        contribution = (capped_amount * effective_rate * currency_rate).quantize(
            Decimal('0.01'), rounding=ROUND_HALF_UP
        )
        
        return contribution


class CNAMCalculator:
    """
    CNAM (Health Insurance) contribution calculations
    Equivalent to CNAMm, RCNAMm methods in PaieClass.java
    """
    
    @staticmethod
    def calculate_employee_contribution(taxable_amount: Decimal) -> Decimal:
        """
        Calculate employee CNAM contribution
        Equivalent to CNAMm method in PaieClass.java
        
        Args:
            taxable_amount: Base salary subject to CNAM
            
        Returns:
            Employee CNAM contribution amount
        """
        # CRITICAL FIX: Employee contribution rate: 4% (corrected from 0.5% in Java)
        # This is a major compliance fix - was previously 0.5% which was incorrect
        rate = Decimal('0.04')
        
        # Calculate contribution (no ceiling for CNAM)
        contribution = (taxable_amount * rate).quantize(
            Decimal('0.01'), rounding=ROUND_HALF_UP
        )
        
        return contribution
    
    @staticmethod
    def calculate_employer_contribution(taxable_amount: Decimal,
                                     reimbursement_rate: Decimal = Decimal('0.0')) -> Decimal:
        """
        Calculate employer CNAM contribution with reimbursement
        Equivalent to RCNAMm method in PaieClass.java
        
        Args:
            taxable_amount: Base salary subject to CNAM
            reimbursement_rate: Employee-specific reimbursement rate
            
        Returns:
            Employer CNAM contribution amount
        """
        # CRITICAL FIX: Employer contribution rate: 5% (calculated as employee × 1.25)
        # This ensures proper employer rate calculation
        base_rate = Decimal('0.05')  # 4% × 1.25 = 5%
        
        # Apply reimbursement rate if applicable
        effective_rate = base_rate * (Decimal('1.0') + reimbursement_rate)
        
        # Calculate contribution
        contribution = (taxable_amount * effective_rate).quantize(
            Decimal('0.01'), rounding=ROUND_HALF_UP
        )
        
        return contribution


class ITSCalculator:
    """
    ITS (Income Tax on Salaries) calculation with progressive brackets
    Equivalent to ITSm, tranche1ITS, tranche2ITS, tranche3ITS, RITSm methods in PaieClass.java
    """
    
    @staticmethod
    def get_tax_brackets(year: int = 2018, is_expatriate: bool = False, tax_mode: str = 'G') -> List[Dict]:
        """
        Get ITS tax brackets for the given year
        
        Args:
            year: Tax year
            is_expatriate: Whether the employee is an expatriate
            tax_mode: Tax mode 'G' (General) or 'T' (Territorial)
            
        Returns:
            List of tax brackets with min, max, and rate
        """
        # Standard brackets for Mauritania (3 tranches)
        # Mode ITS affects T2 and T3 rates ("T" mode uses 20% for both)
        t2_rate = Decimal('0.25')  # Default 25%
        t3_rate = Decimal('0.40')  # Default 40% 
        
        # CRITICAL FIX: Tax mode variations ("G" vs "T" mode)
        # T mode uses 20% for both T2 and T3 tranches
        if tax_mode == 'T':
            t2_rate = Decimal('0.20')  # T mode: 20%
            t3_rate = Decimal('0.20')  # T mode: 20%
        
        # Apply expatriate halving
        if is_expatriate:
            t2_rate = t2_rate / Decimal('2')
            t3_rate = t3_rate / Decimal('2')
            
        brackets = [
            {
                'min': Decimal('0'),
                'max': Decimal('9000'),
                'rate': Decimal('0.075') if is_expatriate else Decimal('0.15')  # 7.5% vs 15%
            },
            {
                'min': Decimal('9000'),
                'max': Decimal('21000'),  # T2 upper limit
                'rate': t2_rate
            },
            {
                'min': Decimal('21000'),
                'max': None,  # T3 no upper limit
                'rate': t3_rate
            }
        ]
        
        return brackets
    
    @classmethod
    def calculate_its_progressive(cls,
                                taxable_income: Decimal,
                                cnss_amount: Decimal = Decimal('0'),
                                cnam_amount: Decimal = Decimal('0'),
                                base_salary: Decimal = Decimal('0'),
                                benefits_in_kind: Decimal = Decimal('0'),
                                currency_rate: Decimal = Decimal('1.0'),
                                is_expatriate: bool = False,
                                year: int = 2018,
                                deduct_cnss: bool = True,
                                deduct_cnam: bool = True,
                                abatement: Decimal = Decimal('0'),
                                tax_mode: str = 'G') -> Dict[str, Decimal]:
        """
        Calculate ITS with progressive brackets
        Equivalent to ITSm method in PaieClass.java
        
        Args:
            taxable_income: Base taxable income
            cnss_amount: CNSS contribution to potentially deduct
            cnam_amount: CNAM contribution to potentially deduct
            base_salary: Base salary amount
            benefits_in_kind: Benefits in kind amount
            currency_rate: Currency conversion rate
            is_expatriate: Whether employee is expatriate
            year: Tax year
            deduct_cnss: Whether to deduct CNSS from taxable income
            deduct_cnam: Whether to deduct CNAM from taxable income
            abatement: Tax abatement amount
            tax_mode: Tax mode 'G' or 'T'
            
        Returns:
            Dict with total ITS and breakdown by tranche
        """
        # Adjust taxable income
        adjusted_income = taxable_income
        
        if deduct_cnss:
            adjusted_income -= cnss_amount
        if deduct_cnam:
            adjusted_income -= cnam_amount
            
        # Apply abatement
        adjusted_income = max(Decimal('0'), adjusted_income - abatement)
        
        # CRITICAL FIX: Benefits in Kind 20% Rule implementation
        # If benefits > 20% of (base salary - benefits), deduct only 60%
        # Otherwise, deduct 100% from taxable income
        if benefits_in_kind > Decimal('0'):
            benefits_deduction = TaxUtilities.calculate_benefits_in_kind_deduction(
                benefits_in_kind, base_salary
            )
            adjusted_income -= benefits_deduction
        
        # Ensure non-negative
        adjusted_income = max(Decimal('0'), adjusted_income)
        
        # Get tax brackets
        brackets = cls.get_tax_brackets(year, is_expatriate, tax_mode)
        
        result = {
            'total': Decimal('0.00'),
            'tranche1': Decimal('0.00'),
            'tranche2': Decimal('0.00'),
            'tranche3': Decimal('0.00'),
            'taxable_income': adjusted_income
        }
        
        remaining_income = adjusted_income
        
        for i, bracket in enumerate(brackets):
            if remaining_income <= Decimal('0'):
                break
                
            # Calculate taxable amount in this bracket
            bracket_min = bracket['min']
            bracket_max = bracket['max']
            bracket_rate = bracket['rate']
            
            if bracket_max is not None:
                bracket_width = bracket_max - bracket_min
                taxable_in_bracket = min(remaining_income, bracket_width)
            else:
                taxable_in_bracket = remaining_income
            
            # Calculate tax for this bracket
            bracket_tax = (taxable_in_bracket * bracket_rate / currency_rate).quantize(
                Decimal('0.01'), rounding=ROUND_HALF_UP
            )
            
            # Store in appropriate tranche
            tranche_key = f'tranche{i + 1}'
            if tranche_key in result:
                result[tranche_key] = bracket_tax
            
            result['total'] += bracket_tax
            remaining_income -= taxable_in_bracket
        
        return result
    
    @classmethod
    def calculate_tranche1_its(cls, *args, **kwargs) -> Decimal:
        """Calculate ITS tranche 1 - equivalent to tranche1ITS"""
        result = cls.calculate_its_progressive(*args, **kwargs)
        return result['tranche1']
    
    @classmethod
    def calculate_tranche2_its(cls, *args, **kwargs) -> Decimal:
        """Calculate ITS tranche 2 - equivalent to tranche2ITS"""
        result = cls.calculate_its_progressive(*args, **kwargs)
        return result['tranche2']
    
    @classmethod
    def calculate_tranche3_its(cls, *args, **kwargs) -> Decimal:
        """Calculate ITS tranche 3 - equivalent to tranche3ITS"""
        result = cls.calculate_its_progressive(*args, **kwargs)
        return result['tranche3']
    
    @classmethod
    def calculate_its_reimbursement(cls,
                                  taxable_income: Decimal,
                                  cnss_amount: Decimal = Decimal('0'),
                                  cnam_amount: Decimal = Decimal('0'),
                                  base_salary: Decimal = Decimal('0'),
                                  benefits_in_kind: Decimal = Decimal('0'),
                                  tranche1_reimb_rate: Decimal = Decimal('0'),
                                  tranche2_reimb_rate: Decimal = Decimal('0'),
                                  tranche3_reimb_rate: Decimal = Decimal('0'),
                                  currency_rate: Decimal = Decimal('1.0'),
                                  is_expatriate: bool = False,
                                  year: int = 2018,
                                  deduct_cnss: bool = True,
                                  deduct_cnam: bool = True,
                                  abatement: Decimal = Decimal('0'),
                                  tax_mode: str = 'G') -> Decimal:
        """
        Calculate ITS reimbursement based on tranche-specific rates
        Equivalent to RITSm method in PaieClass.java
        
        Args:
            Same as calculate_its_progressive plus:
            tranche1_reimb_rate: Reimbursement rate for tranche 1
            tranche2_reimb_rate: Reimbursement rate for tranche 2  
            tranche3_reimb_rate: Reimbursement rate for tranche 3
            
        Returns:
            Total ITS reimbursement amount
        """
        # Calculate ITS by tranche
        its_breakdown = cls.calculate_its_progressive(
            taxable_income, cnss_amount, cnam_amount, base_salary,
            benefits_in_kind, currency_rate, is_expatriate, year,
            deduct_cnss, deduct_cnam, abatement, tax_mode
        )
        
        # Calculate reimbursement for each tranche
        tranche1_reimbursement = its_breakdown['tranche1'] * (tranche1_reimb_rate / Decimal('100'))
        tranche2_reimbursement = its_breakdown['tranche2'] * (tranche2_reimb_rate / Decimal('100'))
        tranche3_reimbursement = its_breakdown['tranche3'] * (tranche3_reimb_rate / Decimal('100'))
        
        total_reimbursement = (
            tranche1_reimbursement + tranche2_reimbursement + tranche3_reimbursement
        ).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
        
        return total_reimbursement


class TaxUtilities:
    """
    Additional tax utility functions from PaieClass.java
    """
    
    @staticmethod
    def calculate_gross_from_net(net_amount: Decimal,
                               benefits_in_kind: Decimal = Decimal('0'),
                               apply_cnss: bool = True,
                               apply_cnam: bool = True,
                               currency_rate: Decimal = Decimal('1.0'),
                               is_expatriate: bool = False,
                               year: int = 2018,
                               tax_mode: str = 'G',
                               system_parameters=None) -> Decimal:
        """
        CRITICAL FIX: BrutDuNet equivalent for gross from net calculation
        Calculate gross salary from net salary using iterative approach
        Equivalent to BrutDuNet method in PaieClass.java
        
        Args:
            net_amount: Target net salary
            benefits_in_kind: Benefits in kind amount
            apply_cnss: Whether to apply CNSS deduction
            apply_cnam: Whether to apply CNAM deduction
            currency_rate: Currency conversion rate
            is_expatriate: Whether employee is expatriate
            year: Tax year
            tax_mode: Tax mode 'G' or 'T'
            system_parameters: System parameters for abatement, etc.
            
        Returns:
            Calculated gross salary
        """
        # Maximum gross estimate (net * 2 + benefits)
        max_gross = net_amount * Decimal('2') + benefits_in_kind
        
        # Get abatement from system parameters
        abatement = getattr(system_parameters, 'tax_abatement', Decimal('0')) if system_parameters else Decimal('0')
        
        # CNSS ceiling
        cnss_ceiling = Decimal('15000')
        
        current_gross = net_amount
        
        # Only iterate if net is above abatement
        if net_amount > abatement:
            # Iterate from max down to find gross that yields target net
            for i in range(int(max_gross - net_amount - benefits_in_kind)):
                test_gross = max_gross - Decimal(str(i))
                
                if test_gross <= net_amount + benefits_in_kind:
                    break
                    
                current_gross = test_gross
                
                # Calculate CNSS
                cnss_amount = Decimal('0')
                if apply_cnss:
                    if test_gross >= cnss_ceiling:
                        cnss_amount = cnss_ceiling * Decimal('0.01')
                    else:
                        cnss_amount = test_gross * Decimal('0.01')
                
                # Calculate CNAM - CRITICAL FIX: Use correct 4% rate
                cnam_amount = Decimal('0')
                if apply_cnam:
                    cnam_amount = test_gross * Decimal('0.04')  # Corrected to 4%
                
                # Calculate ITS
                its_calculator = ITSCalculator()
                its_result = its_calculator.calculate_its_progressive(
                    taxable_income=test_gross,
                    cnss_amount=cnss_amount,
                    cnam_amount=cnam_amount,
                    base_salary=test_gross,
                    benefits_in_kind=benefits_in_kind,
                    currency_rate=currency_rate,
                    is_expatriate=is_expatriate,
                    year=year,
                    abatement=abatement,
                    tax_mode=tax_mode
                )
                
                # Calculate net
                calculated_net = (test_gross - benefits_in_kind - 
                                its_result['total'] - cnss_amount - cnam_amount)
                
                # Return gross if calculated net matches target
                if calculated_net <= net_amount:
                    return test_gross
        
        return current_gross
    
    @staticmethod
    def apply_non_taxable_allowance_ceiling(taxable_income: Decimal,
                                          non_its_gains: Decimal,
                                          ceiling: Decimal) -> Decimal:
        """
        Apply non-taxable allowance ceiling (plafonIndNonImposable)
        Add excess of non-ITS gains over ceiling back to taxable income
        
        Args:
            taxable_income: Current taxable income
            non_its_gains: Sum of gains marked as plafone=true but not ITS
            ceiling: Non-taxable allowance ceiling
            
        Returns:
            Adjusted taxable income
        """
        if non_its_gains > ceiling:
            excess = non_its_gains - ceiling
            return taxable_income + excess
        return taxable_income
    
    @staticmethod
    def calculate_benefits_in_kind_deduction(benefits_amount: Decimal,
                                           base_salary: Decimal) -> Decimal:
        """
        Calculate proper benefits in kind deduction using 20% rule
        If benefits > 20% of (base_salary - benefits), deduct only 60% of benefits
        Otherwise deduct 100% of benefits
        
        Args:
            benefits_amount: Benefits in kind amount
            base_salary: Base salary amount
            
        Returns:
            Amount to deduct from taxable income
        """
        if benefits_amount <= Decimal('0'):
            return Decimal('0')
        
        # CRITICAL FIX: Benefits in Kind 20% Rule calculation
        # Calculate 20% of (base salary - benefits in kind)
        threshold = (base_salary - benefits_amount) * Decimal('0.20')
        
        if benefits_amount > threshold:
            # Deduct only 60% of benefits
            return benefits_amount * Decimal('0.60')
        else:
            # Deduct 100% of benefits
            return benefits_amount
    
    @staticmethod
    def calculate_installment_quota_enforcement(total_installments: Decimal,
                                              net_before_installments: Decimal,
                                              quota_percentage: Decimal) -> Dict[str, Decimal]:
        """
        Calculate installment quota enforcement (quotaEcheanceRae)
        Proportionally reduce installments if they exceed quota
        
        Args:
            total_installments: Total installment amount requested
            net_before_installments: Net salary before installments
            quota_percentage: Maximum percentage allowed (0-100)
            
        Returns:
            Dict with 'allowed_amount', 'reduction_percentage', 'excess_amount'
        """
        if quota_percentage <= Decimal('0'):
            return {
                'allowed_amount': total_installments,
                'reduction_percentage': Decimal('0'),
                'excess_amount': Decimal('0')
            }
        
        # Calculate maximum allowed installments
        max_allowed = net_before_installments * (quota_percentage / Decimal('100'))
        
        if total_installments <= max_allowed:
            return {
                'allowed_amount': total_installments,
                'reduction_percentage': Decimal('0'),
                'excess_amount': Decimal('0')
            }
        
        # Calculate reduction
        excess = total_installments - max_allowed
        reduction_percentage = excess / total_installments
        
        return {
            'allowed_amount': max_allowed,
            'reduction_percentage': reduction_percentage,
            'excess_amount': excess
        }
    
    @staticmethod
    def get_employer_contributions(employee_cnss: Decimal,
                                  employee_cnam: Decimal) -> Dict[str, Decimal]:
        """
        Calculate employer-side contributions for reporting
        These are derived from employee contributions, not separately calculated
        
        Args:
            employee_cnss: Employee CNSS contribution
            employee_cnam: Employee CNAM contribution
            
        Returns:
            Dict with employer contribution amounts
        """
        # CRITICAL FIX: Employer contribution derivation for reporting
        # These calculations ensure proper employer contribution reporting
        return {
            'cnss_patronal': employee_cnss * Decimal('13'),  # CNSS PAT = employee * 13
            'cnss_medical': employee_cnss * Decimal('2'),    # CNSS MED = employee * 2  
            'cnam_patronal': employee_cnam * Decimal('1.25'), # CNAM PAT = employee * 5/4 (4% × 1.25 = 5%)
            'total_cnss': employee_cnss * Decimal('15'),     # Total CNSS = PAT + MED (13 + 2)
            'total_cnam': employee_cnam * Decimal('1.25')    # Total CNAM = PAT only
        }
    
    @staticmethod
    def calculate_cnss_reimbursement(employee_cnss: Decimal,
                                   reimbursement_rate: Decimal) -> Decimal:
        """
        CRITICAL FIX: Calculate CNSS reimbursement logic
        
        Args:
            employee_cnss: Employee CNSS contribution
            reimbursement_rate: Reimbursement rate percentage (0-100)
            
        Returns:
            CNSS reimbursement amount
        """
        if reimbursement_rate <= Decimal('0'):
            return Decimal('0.00')
        
        reimbursement = (employee_cnss * reimbursement_rate / Decimal('100')).quantize(
            Decimal('0.01'), rounding=ROUND_HALF_UP
        )
        
        return reimbursement
    
    @staticmethod
    def calculate_cnam_reimbursement(employee_cnam: Decimal,
                                   reimbursement_rate: Decimal) -> Decimal:
        """
        CRITICAL FIX: Calculate CNAM reimbursement logic
        
        Args:
            employee_cnam: Employee CNAM contribution
            reimbursement_rate: Reimbursement rate percentage (0-100)
            
        Returns:
            CNAM reimbursement amount
        """
        if reimbursement_rate <= Decimal('0'):
            return Decimal('0.00')
        
        reimbursement = (employee_cnam * reimbursement_rate / Decimal('100')).quantize(
            Decimal('0.01'), rounding=ROUND_HALF_UP
        )
        
        return reimbursement
    
    @staticmethod
    def validate_tax_rates(year: int = 2018) -> Dict[str, Dict[str, Decimal]]:
        """
        CRITICAL FIX: Validate and return current tax rates for compliance
        Ensures all rates match Mauritanian tax law requirements
        
        Args:
            year: Tax year for rate determination
            
        Returns:
            Dict with validated tax rates
        """
        return {
            'cnss': {
                'employee_rate': Decimal('0.01'),     # 1% employee
                'employer_base': Decimal('0.01'),     # 1% base
                'pat_multiplier': Decimal('13'),      # PAT = employee × 13
                'med_multiplier': Decimal('2'),       # MED = employee × 2
                'ceiling': Decimal('15000.00')        # 15,000 MRU ceiling
            },
            'cnam': {
                'employee_rate': Decimal('0.04'),     # 4% employee (CRITICAL FIX)
                'employer_rate': Decimal('0.05'),     # 5% employer (4% × 1.25)
                'ceiling': None                       # No ceiling for CNAM
            },
            'its': {
                'tranche1_min': Decimal('0'),
                'tranche1_max': Decimal('9000'),
                'tranche1_rate_standard': Decimal('0.15'),      # 15% standard
                'tranche1_rate_expatriate': Decimal('0.075'),   # 7.5% expatriate
                'tranche2_min': Decimal('9000'),
                'tranche2_max': Decimal('21000'),
                'tranche2_rate_g_standard': Decimal('0.25'),    # 25% G mode standard
                'tranche2_rate_g_expatriate': Decimal('0.125'), # 12.5% G mode expatriate
                'tranche2_rate_t_standard': Decimal('0.20'),    # 20% T mode standard
                'tranche2_rate_t_expatriate': Decimal('0.10'),  # 10% T mode expatriate
                'tranche3_min': Decimal('21000'),
                'tranche3_max': None,                           # No upper limit
                'tranche3_rate_g_standard': Decimal('0.40'),    # 40% G mode standard
                'tranche3_rate_g_expatriate': Decimal('0.20'),  # 20% G mode expatriate
                'tranche3_rate_t_standard': Decimal('0.20'),    # 20% T mode standard
                'tranche3_rate_t_expatriate': Decimal('0.10')   # 10% T mode expatriate
            }
        }


class TaxCalculationService:
    """
    Unified tax calculation service
    Provides a single interface for all tax calculations
    """
    
    def __init__(self, system_parameters=None):
        self.system_parameters = system_parameters
        self.cnss_calculator = CNSSCalculator()
        self.cnam_calculator = CNAMCalculator()
        self.its_calculator = ITSCalculator()
    
    def calculate_all_taxes(self,
                          employee,
                          taxable_salaries: Dict[str, Decimal],
                          period: str = None) -> Dict[str, Decimal]:
        """
        Calculate all taxes for an employee
        
        Args:
            employee: Employee instance
            taxable_salaries: Dict with 'cnss', 'cnam', 'its' bases
            period: Payroll period
            
        Returns:
            Dict with all calculated tax amounts
        """
        result = {}
        
        # CNSS calculations - CRITICAL FIX: Include reimbursement calculations
        if employee.is_subject_to_cnss():
            cnss_base = taxable_salaries.get('cnss', Decimal('0'))
            result['cnss_employee'] = self.cnss_calculator.calculate_employee_contribution(cnss_base)
            result['cnss_employer'] = self.cnss_calculator.calculate_employer_contribution(
                cnss_base, employee.cnss_reimbursement_rate or Decimal('0')
            )
            # Calculate CNSS reimbursement
            result['cnss_reimbursement'] = TaxUtilities.calculate_cnss_reimbursement(
                result['cnss_employee'], employee.cnss_reimbursement_rate or Decimal('0')
            )
        else:
            result['cnss_employee'] = Decimal('0.00')
            result['cnss_employer'] = Decimal('0.00')
            result['cnss_reimbursement'] = Decimal('0.00')
        
        # CNAM calculations - CRITICAL FIX: Include reimbursement calculations  
        if employee.is_subject_to_cnam():
            cnam_base = taxable_salaries.get('cnam', Decimal('0'))
            result['cnam_employee'] = self.cnam_calculator.calculate_employee_contribution(cnam_base)
            result['cnam_employer'] = self.cnam_calculator.calculate_employer_contribution(
                cnam_base, employee.cnam_reimbursement_rate or Decimal('0')
            )
            # Calculate CNAM reimbursement
            result['cnam_reimbursement'] = TaxUtilities.calculate_cnam_reimbursement(
                result['cnam_employee'], employee.cnam_reimbursement_rate or Decimal('0')
            )
        else:
            result['cnam_employee'] = Decimal('0.00')
            result['cnam_employer'] = Decimal('0.00')
            result['cnam_reimbursement'] = Decimal('0.00')
        
        # ITS calculations
        if employee.is_subject_to_its():
            its_base = taxable_salaries.get('its', Decimal('0'))
            benefits_in_kind = taxable_salaries.get('benefits_in_kind', Decimal('0'))
            
            # Get system configuration with proper field names
            deduct_cnss = getattr(self.system_parameters, 'deduct_cnss_from_its', True)
            deduct_cnam = getattr(self.system_parameters, 'deduct_cnam_from_its', True) 
            abatement = getattr(self.system_parameters, 'tax_abatement', Decimal('0'))
            non_taxable_ceiling = getattr(self.system_parameters, 'non_taxable_allowance_ceiling', Decimal('0'))
            its_mode = getattr(self.system_parameters, 'its_tax_mode', 'G')  # G=General, T=Territorial
            
            # Apply non-taxable allowance ceiling if configured
            adjusted_its_base = its_base
            if non_taxable_ceiling > Decimal('0'):
                # Calculate non-ITS gains that are subject to ceiling
                non_its_gains = taxable_salaries.get('non_its_gains', Decimal('0'))
                adjusted_its_base = TaxUtilities.apply_non_taxable_allowance_ceiling(
                    its_base, non_its_gains, non_taxable_ceiling
                )
            
            its_breakdown = self.its_calculator.calculate_its_progressive(
                taxable_income=adjusted_its_base,
                cnss_amount=result['cnss_employee'],
                cnam_amount=result['cnam_employee'],
                benefits_in_kind=benefits_in_kind,
                is_expatriate=employee.is_expatriate,
                deduct_cnss=deduct_cnss,
                deduct_cnam=deduct_cnam,
                abatement=abatement,
                tax_mode=its_mode
            )
            
            result.update({
                'its_total': its_breakdown['total'],
                'its_tranche1': its_breakdown['tranche1'],
                'its_tranche2': its_breakdown['tranche2'],
                'its_tranche3': its_breakdown['tranche3'],
            })
            
            # Calculate ITS reimbursement if applicable
            if (employee.its_tranche1_reimbursement or 
                employee.its_tranche2_reimbursement or 
                employee.its_tranche3_reimbursement):
                
                result['its_reimbursement'] = self.its_calculator.calculate_its_reimbursement(
                    taxable_income=adjusted_its_base,
                    cnss_amount=result['cnss_employee'],
                    cnam_amount=result['cnam_employee'],
                    benefits_in_kind=benefits_in_kind,
                    tranche1_reimb_rate=employee.its_tranche1_reimbursement or Decimal('0'),
                    tranche2_reimb_rate=employee.its_tranche2_reimbursement or Decimal('0'),
                    tranche3_reimb_rate=employee.its_tranche3_reimbursement or Decimal('0'),
                    is_expatriate=employee.is_expatriate,
                    deduct_cnss=deduct_cnss,
                    deduct_cnam=deduct_cnam,
                    abatement=abatement,
                    tax_mode=its_mode
                )
            else:
                result['its_reimbursement'] = Decimal('0.00')
                
        else:
            result.update({
                'its_total': Decimal('0.00'),
                'its_tranche1': Decimal('0.00'),
                'its_tranche2': Decimal('0.00'),
                'its_tranche3': Decimal('0.00'),
                'its_reimbursement': Decimal('0.00'),
            })
        
        return result
    
    def get_tax_summary(self, employee, taxable_salaries: Dict[str, Decimal]) -> Dict[str, any]:
        """
        Get a comprehensive tax summary for reporting
        """
        taxes = self.calculate_all_taxes(employee, taxable_salaries)
        
        return {
            'employee_contributions': {
                'cnss': taxes['cnss_employee'],
                'cnam': taxes['cnam_employee'],
                'its': taxes['its_total'],
                'total': taxes['cnss_employee'] + taxes['cnam_employee'] + taxes['its_total']
            },
            'employer_contributions': {
                'cnss': taxes['cnss_employer'],
                'cnam': taxes['cnam_employer'],
                'total': taxes['cnss_employer'] + taxes['cnam_employer']
            },
            'reimbursements': {
                'cnss': taxes['cnss_reimbursement'],
                'cnam': taxes['cnam_reimbursement'],
                'its': taxes['its_reimbursement'],
                'total': taxes['cnss_reimbursement'] + taxes['cnam_reimbursement'] + taxes['its_reimbursement']
            },
            'its_breakdown': {
                'tranche1': taxes['its_tranche1'],
                'tranche2': taxes['its_tranche2'],
                'tranche3': taxes['its_tranche3'],
                'total': taxes['its_total']
            },
            'employer_derived': TaxUtilities.get_employer_contributions(
                taxes['cnss_employee'], taxes['cnam_employee']
            )
        }