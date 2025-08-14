# tax_calculations.py
"""
Tax calculation functions for CNSS, CNAM, and ITS
Converted from PaieClass.java tax calculation methods
"""

from decimal import Decimal, ROUND_HALF_UP
from typing import Dict, List, Optional, Tuple


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
        
        # Base employer contribution rate: 2%
        base_rate = Decimal('0.02')
        
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
        # Employee contribution rate: 0.5%
        rate = Decimal('0.005')
        
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
        # Base employer contribution rate: 1.5%
        base_rate = Decimal('0.015')
        
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
    def get_tax_brackets(year: int = 2018, is_expatriate: bool = False) -> List[Dict]:
        """
        Get ITS tax brackets for the given year
        
        Args:
            year: Tax year
            is_expatriate: Whether the employee is an expatriate
            
        Returns:
            List of tax brackets with min, max, and rate
        """
        # Standard brackets for Mauritania (rates may vary by year)
        brackets = [
            {
                'min': Decimal('0'),
                'max': Decimal('9000'),
                'rate': Decimal('0.075') if is_expatriate else Decimal('0.15')  # 7.5% vs 15%
            },
            {
                'min': Decimal('9000'),
                'max': None,  # No upper limit
                'rate': Decimal('0.20')  # 20%
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
                                abatement: Decimal = Decimal('0')) -> Dict[str, Decimal]:
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
        
        # Get tax brackets
        brackets = cls.get_tax_brackets(year, is_expatriate)
        
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
            bracket_tax = (taxable_in_bracket * bracket_rate * currency_rate).quantize(
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
                                  abatement: Decimal = Decimal('0')) -> Decimal:
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
            deduct_cnss, deduct_cnam, abatement
        )
        
        # Calculate reimbursement for each tranche
        tranche1_reimbursement = its_breakdown['tranche1'] * tranche1_reimb_rate
        tranche2_reimbursement = its_breakdown['tranche2'] * tranche2_reimb_rate
        tranche3_reimbursement = its_breakdown['tranche3'] * tranche3_reimb_rate
        
        total_reimbursement = (
            tranche1_reimbursement + tranche2_reimbursement + tranche3_reimbursement
        ).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
        
        return total_reimbursement


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
        
        # CNSS calculations
        if employee.is_subject_to_cnss():
            cnss_base = taxable_salaries.get('cnss', Decimal('0'))
            result['cnss_employee'] = self.cnss_calculator.calculate_employee_contribution(cnss_base)
            result['cnss_employer'] = self.cnss_calculator.calculate_employer_contribution(
                cnss_base, employee.cnss_reimbursement_rate or Decimal('0')
            )
        else:
            result['cnss_employee'] = Decimal('0.00')
            result['cnss_employer'] = Decimal('0.00')
        
        # CNAM calculations
        if employee.is_subject_to_cnam():
            cnam_base = taxable_salaries.get('cnam', Decimal('0'))
            result['cnam_employee'] = self.cnam_calculator.calculate_employee_contribution(cnam_base)
            result['cnam_employer'] = self.cnam_calculator.calculate_employer_contribution(
                cnam_base, employee.cnam_reimbursement_rate or Decimal('0')
            )
        else:
            result['cnam_employee'] = Decimal('0.00')
            result['cnam_employer'] = Decimal('0.00')
        
        # ITS calculations
        if employee.is_subject_to_its():
            its_base = taxable_salaries.get('its', Decimal('0'))
            benefits_in_kind = taxable_salaries.get('benefits_in_kind', Decimal('0'))
            
            # Get system configuration
            deduct_cnss = getattr(self.system_parameters, 'deduct_cnss_from_its', True)
            deduct_cnam = getattr(self.system_parameters, 'deduct_cnam_from_its', True)
            abatement = getattr(self.system_parameters, 'tax_abatement', Decimal('0'))
            
            its_breakdown = self.its_calculator.calculate_its_progressive(
                taxable_income=its_base,
                cnss_amount=result['cnss_employee'],
                cnam_amount=result['cnam_employee'],
                benefits_in_kind=benefits_in_kind,
                is_expatriate=employee.is_expatriate,
                deduct_cnss=deduct_cnss,
                deduct_cnam=deduct_cnam,
                abatement=abatement
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
                    taxable_income=its_base,
                    cnss_amount=result['cnss_employee'],
                    cnam_amount=result['cnam_employee'],
                    benefits_in_kind=benefits_in_kind,
                    tranche1_reimb_rate=employee.its_tranche1_reimbursement or Decimal('0'),
                    tranche2_reimb_rate=employee.its_tranche2_reimbursement or Decimal('0'),
                    tranche3_reimb_rate=employee.its_tranche3_reimbursement or Decimal('0'),
                    is_expatriate=employee.is_expatriate,
                    deduct_cnss=deduct_cnss,
                    deduct_cnam=deduct_cnam,
                    abatement=abatement
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
                'its': taxes['its_reimbursement'],
                'total': taxes['its_reimbursement']
            },
            'its_breakdown': {
                'tranche1': taxes['its_tranche1'],
                'tranche2': taxes['its_tranche2'],
                'tranche3': taxes['its_tranche3'],
                'total': taxes['its_total']
            }
        }