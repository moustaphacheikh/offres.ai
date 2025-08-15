# payroll_calculations.py
"""
Core payroll calculation functions converted from Java
Equivalent to FonctionsPaie.java and core methods from PaieClass.java
"""

from decimal import Decimal, ROUND_HALF_UP
from datetime import datetime, date
# Cleaned up imports - removed unused timedelta
from django.utils import timezone
from typing import Dict, List, Optional, Union
from .formula_engine import PayrollFormulaEvaluator, FormulaCalculationError
from .date_utils import DateCalculator
import math


class PayrollFunctions:
    """
    24 Core Payroll Functions (F01-F24)
    Converted from Java FonctionsPaie.class
    These are the standard Mauritanian payroll calculation functions
    """
    
    def __init__(self, system_parameters, payroll_calculator):
        self.system_parameters = system_parameters
        self.pc = payroll_calculator  # Reference to main payroll calculator
        
    def F01_NJT(self, employee, motif, period) -> Decimal:
        """
        F01 - Nombre de Jours Travaillés (Number of Working Days)
        Gets the actual working days from time tracking
        """
        njt_record = self.pc.get_njt_record(employee, motif, period)
        return Decimal(str(njt_record.njt)) if njt_record else Decimal('0.00')
    
    def F02_sbJour(self, employee, motif, period) -> Decimal:
        """
        F02 - Salaire de Base Journalier (Daily Base Salary)
        Gets the daily base salary from salary component ID 1
        """
        rubrique_record = self.pc.get_rubrique_paie_record(
            employee, self.pc.get_used_rub_id(1), motif, period
        )
        return Decimal(str(rubrique_record.base)) if rubrique_record else Decimal('0.00')
    
    def F03_sbHoraire(self, employee, motif, period) -> Decimal:
        """
        F03 - Salaire de Base Horaire (Hourly Base Salary)
        Calculates hourly rate from daily salary and contract hours
        """
        daily_salary = self.F02_sbJour(employee, motif, period)
        if daily_salary > 0 and employee.contract_hours_per_week:
            monthly_salary = daily_salary * Decimal('30')
            monthly_hours = Decimal(str(employee.contract_hours_per_week)) * Decimal('52') / Decimal('12')
            return monthly_salary / monthly_hours if monthly_hours > 0 else Decimal('0.00')
        return Decimal('0.00')
    
    def F04_TauxAnciennete(self, employee, period) -> Decimal:
        """
        F04 - Taux d'Ancienneté Standard (Standard Seniority Rate)
        Progressive seniority bonus: 2% per year up to 30% maximum at 16+ years
        """
        if not employee.seniority_date:
            return Decimal('0.00')
            
        seniority_years = DateCalculator.get_years_between(employee.seniority_date, period)
        
        if seniority_years >= 16:
            return Decimal('0.30')  # 30% maximum
        elif seniority_years >= 15:
            return Decimal('0.29')  # 29% for 15th year
        elif seniority_years >= 14:
            return Decimal('0.28')  # 28% for 14th year
        elif seniority_years >= 1:
            return Decimal(str(seniority_years)) * Decimal('0.02')  # 2% per year
        else:
            return Decimal('0.00')
    
    def F23_TauxAncienneteSpeciale(self, employee, period) -> Decimal:
        """
        F23 - Taux d'Ancienneté Spéciale (Special Seniority Rate)
        Like F04 but continues growing at 1% per year beyond 16 years
        """
        if not employee.seniority_date:
            return Decimal('0.00')
            
        seniority_years = DateCalculator.get_years_between(employee.seniority_date, period)
        
        if seniority_years >= 16:
            return Decimal('0.30') + Decimal(str(seniority_years - 16)) * Decimal('0.01')
        elif seniority_years >= 15:
            return Decimal('0.29')
        elif seniority_years >= 14:
            return Decimal('0.28')
        elif seniority_years >= 1:
            return Decimal(str(seniority_years)) * Decimal('0.02')
        else:
            return Decimal('0.00')
    
    def F23X_TauxAncienneteSpeciale(self, employee, period) -> Decimal:
        """
        F23X - Taux d'Ancienneté Spéciale Alternative (Alternative Special Seniority)
        3% per year with 2% increases beyond 15 years
        """
        if not employee.seniority_date:
            return Decimal('0.00')
            
        seniority_years = DateCalculator.get_years_between(employee.seniority_date, period)
        
        if seniority_years >= 15:
            return Decimal('0.45') + Decimal(str(seniority_years - 15)) * Decimal('0.02')
        elif seniority_years >= 1:
            return Decimal(str(seniority_years)) * Decimal('0.03')
        else:
            return Decimal('0.00')
    
    def F05_cumulBIDerDepart(self, employee) -> Decimal:
        """
        F05 - Cumul Brut Imposable Depuis Dernier Départ
        Cumulative taxable gross income since last departure
        """
        return self.pc.get_cumulative_amount_by_type(employee, "BI")
    
    def F06_cumulBNIDerDepart(self, employee) -> Decimal:
        """
        F06 - Cumul Brut Non Imposable Depuis Dernier Départ
        Cumulative non-taxable gross income since last departure
        """
        return self.pc.get_cumulative_amount_by_type(employee, "BNI")
    
    def F07_cumulRETDerDepart(self, employee) -> Decimal:
        """
        F07 - Cumul Retenues Depuis Dernier Départ
        Cumulative deductions since last departure
        """
        return self.pc.get_cumulative_amount_by_type(employee, "RET")
    
    def F08_cumulBrut12DerMois(self, employee) -> Decimal:
        """
        F08 - Cumul Brut 12 Derniers Mois (12-month gross salary cumulative)
        Used for end-of-service benefits calculation
        """
        # Calculate period 13 months ago (390 days)
        start_period = DateCalculator.add_days(self.system_parameters.current_period, -390)
        # Set to 28th of month to ensure valid date
        start_period = start_period.replace(day=28)
        
        cumulative_gross = self.pc.get_cumulative_gross_12_months(
            employee, start_period, self.system_parameters.current_period
        )
        
        # Add initial cumulative amount from employee record
        return cumulative_gross + Decimal(str(employee.cumul_12_months_initial or 0))
    
    def F09_salaireBrutMensuelFixe(self, employee, period) -> Decimal:
        """
        F09 - Salaire Brut Mensuel Fixe (Fixed Monthly Gross Salary)
        Sum of all fixed gain components for regular salary motif
        """
        return self.pc.get_fixed_monthly_gross_salary(employee, period)
    
    def F10_smig(self) -> Decimal:
        """
        F10 - SMIG (Minimum Wage)
        Returns current minimum wage from system parameters
        """
        return Decimal(str(self.system_parameters.smig or 0))
    
    def F11_smigHoraire(self, employee) -> Decimal:
        """
        F11 - SMIG Horaire (Hourly Minimum Wage)
        Calculates hourly minimum wage based on contract hours
        """
        if employee.contract_hours_per_week:
            return self.F10_smig() * Decimal(str(employee.contract_hours_per_week)) * Decimal('4')
        return Decimal('0.00')
    
    def F12_TauxLicenciement(self, employee, period) -> Decimal:
        """
        F12 - Taux de Licenciement (Dismissal/Severance Rate)
        Mauritanian labor law severance calculation
        Years 1-5: 0.25 months per year
        Years 5-10: 1.25 base + 0.3 per additional year
        Years 10+: 2.75 base + 0.35 per additional year
        """
        if not employee.seniority_date:
            return Decimal('0.00')
            
        # Add 2 days for calculation precision (172800000L milliseconds = 2 days)
        seniority_days = DateCalculator.get_days_between(employee.seniority_date, period) + 2
        seniority_years = seniority_days / 365.0
        
        if 1.0 <= seniority_years < 5.0:
            return Decimal(str(0.25 * seniority_years))
        elif 5.0 <= seniority_years < 10.0:
            return Decimal('1.25') + Decimal(str((seniority_years - 5.0) * 0.3))
        elif seniority_years >= 10.0:
            return Decimal('2.75') + Decimal(str((seniority_years - 10.0) * 0.35))
        else:
            return Decimal('0.00')
    
    def F13_TauxLicenciementCollectif(self, employee, period) -> Decimal:
        """
        F13 - Taux de Licenciement Collectif (Collective Dismissal Rate)
        Higher rates for collective layoffs
        Years 1-5: 0.3 months per year
        Years 5-10: 1.5 base + 0.4 per additional year
        Years 10+: 3.5 base + 0.5 per additional year
        """
        if not employee.seniority_date:
            return Decimal('0.00')
            
        seniority_days = DateCalculator.get_days_between(employee.seniority_date, period) + 2
        seniority_years = seniority_days / 365.0
        
        if 1.0 < seniority_years <= 5.0:
            return Decimal(str(0.3 * seniority_years))
        elif 5.0 < seniority_years <= 10.0:
            return Decimal('1.5') + Decimal(str((seniority_years - 5.0) * 0.4))
        elif seniority_years > 10.0:
            return Decimal('3.5') + Decimal(str((seniority_years - 10.0) * 0.5))
        else:
            return Decimal('0.00')
    
    def F14_TauxRetraite(self, employee, period) -> Decimal:
        """
        F14 - Taux de Retraite (Retirement Benefits Rate)
        Percentage of dismissal rate based on years of service
        Years 1-5: 30% of dismissal rate
        Years 5-10: 50% of dismissal rate
        Years 10-20: 75% of dismissal rate
        Years 20+: 100% of dismissal rate
        """
        if not employee.seniority_date:
            return Decimal('0.00')
            
        seniority_days = DateCalculator.get_days_between(employee.seniority_date, period) + 2
        seniority_years = seniority_days / 365.0
        dismissal_rate = self.F12_TauxLicenciement(employee, period)
        
        if 1.0 < seniority_years <= 5.0:
            return Decimal('0.3') * dismissal_rate
        elif 5.0 < seniority_years <= 10.0:
            return Decimal('0.5') * dismissal_rate
        elif 10.0 < seniority_years <= 20.0:
            return Decimal('0.75') * dismissal_rate
        elif seniority_years > 20.0:
            return Decimal('1.0') * dismissal_rate
        else:
            return Decimal('0.00')
    
    def F15_TauxPSRA(self, employee) -> Decimal:
        """
        F15 - Taux PSRA (PSRA Rate)
        Returns employee-specific PSRA rate
        """
        return Decimal(str(employee.psra_rate or 0))
    
    def F16_TauxPreavis(self, employee) -> Decimal:
        """
        F16 - Taux Préavis (Notice Period Rate)
        Returns number of months for notice period
        """
        return Decimal(str(employee.notice_months or 0))
    
    def F17_CumulNJTMC(self, employee) -> Decimal:
        """
        F17 - Cumul NJT MC (Cumulative Working Days MC)
        Currently returns 0 - placeholder for future implementation
        """
        return Decimal('0.00')
    
    def F18_NbSmigRegion(self, employee) -> Decimal:
        """
        F18 - Nombre SMIG Région (Regional SMIG Number)
        Regional multiplier for vacation calculations
        """
        if employee.origin and hasattr(employee.origin, 'smig_hours_for_vacation_allowance'):
            return Decimal(str(employee.origin.smig_hours_for_vacation_allowance or 0))
        return Decimal('0.00')
    
    def F19_TauxPresence(self, employee, period) -> Decimal:
        """
        F19 - Taux de Présence (Attendance Rate)
        Calculates attendance rate as months worked / 12
        """
        if not employee.seniority_date:
            return Decimal('0.00')
            
        days_worked = DateCalculator.get_days_between(employee.seniority_date, period)
        months_worked = days_worked / 30.0
        attendance_rate = months_worked / 12.0
        
        return Decimal(str(min(1.0, attendance_rate)))
    
    def F20_BaseIndLogement(self, employee) -> Decimal:
        """
        F20 - Base Indemnité Logement (Housing Allowance Base)
        Gets housing allowance based on salary grade and family situation
        """
        return self.pc.get_housing_allowance_base(employee)
    
    def F21_salaireNet(self, employee, period) -> Decimal:
        """
        F21 - Salaire Net (Net Salary)
        Calculates net salary after all deductions (CNSS, CNAM, ITS)
        """
        gross_salary = self.F09_salaireBrutMensuelFixe(employee, period)
        
        if gross_salary <= 0:
            return Decimal('0.00')
        
        # Calculate social contributions
        cnss_amount = self.pc.calculate_cnss_employee(gross_salary, Decimal('1.0'), 2018)
        cnam_amount = self.pc.calculate_cnam_employee(gross_salary)
        
        # Calculate ITS
        its_amount = self.pc.calculate_its_total(
            2018, gross_salary, cnss_amount, cnam_amount, 
            gross_salary, Decimal('0.0'), Decimal('1.0'), False
        )
        
        net_salary = gross_salary - its_amount - cnss_amount - cnam_amount
        return max(Decimal('0.00'), net_salary)
    
    def F22_NbEnfants(self, employee) -> Decimal:
        """
        F22 - Nombre d'Enfants (Number of Children)
        Returns number of children for family allowances
        """
        return Decimal(str(employee.number_of_children or 0))
    
    def F24_augmentationSalaireFixe(self, employee, period) -> Decimal:
        """
        F24 - Augmentation Salaire Fixe (Fixed Salary Increase)
        Gets salary increase amount from system
        """
        return self.pc.get_salary_increase(employee, period)
    
    def calculate_function(self, function_code: str, employee, motif, period) -> Decimal:
        """
        Main dispatcher for all payroll functions
        Equivalent to Java calFonction method
        
        Args:
            function_code: Function code (F01, F02, etc.)
            employee: Employee instance
            motif: Payroll motif
            period: Calculation period
            
        Returns:
            Calculated value for the function
        """
        if not function_code:
            return Decimal('0.00')
            
        function_map = {
            'F01': lambda: self.F01_NJT(employee, motif, period),
            'F02': lambda: self.F02_sbJour(employee, motif, period),
            'F03': lambda: self.F03_sbHoraire(employee, motif, period),
            'F04': lambda: self.F04_TauxAnciennete(employee, period),
            'F05': lambda: self.F05_cumulBIDerDepart(employee),
            'F06': lambda: self.F06_cumulBNIDerDepart(employee),
            'F07': lambda: self.F07_cumulRETDerDepart(employee),
            'F08': lambda: self.F08_cumulBrut12DerMois(employee),
            'F09': lambda: self.F09_salaireBrutMensuelFixe(employee, period),
            'F10': lambda: self.F10_smig(),
            'F11': lambda: self.F11_smigHoraire(employee),
            'F12': lambda: self.F12_TauxLicenciement(employee, period),
            'F13': lambda: self.F13_TauxLicenciementCollectif(employee, period),
            'F14': lambda: self.F14_TauxRetraite(employee, period),
            'F15': lambda: self.F15_TauxPSRA(employee),
            'F16': lambda: self.F16_TauxPreavis(employee),
            'F17': lambda: self.F17_CumulNJTMC(employee),
            'F18': lambda: self.F18_NbSmigRegion(employee),
            'F19': lambda: self.F19_TauxPresence(employee, period),
            'F20': lambda: self.F20_BaseIndLogement(employee),
            'F21': lambda: self.F21_salaireNet(employee, period),
            'F22': lambda: self.F22_NbEnfants(employee),
            'F23': lambda: self.F23_TauxAncienneteSpeciale(employee, period),
            'F24': lambda: self.F24_augmentationSalaireFixe(employee, period),
        }
        
        function_callable = function_map.get(function_code)
        if function_callable:
            try:
                return function_callable()
            except Exception as e:
                # Log error and return 0
                print(f"Error calculating {function_code}: {str(e)}")
                return Decimal('0.00')
        
        return Decimal('0.00')


class PayrollCalculator:
    """Core payroll calculation engine converted from PaieClass.paieCalcule"""
    
    def __init__(self, system_parameters):
        self.system_parameters = system_parameters
        self.payroll_functions = PayrollFunctions(system_parameters, self)
    
    def calculate_payroll(self, employee, motif, period_start, period_end):
        """
        Main payroll calculation method - equivalent to PaieClass.paieCalcule
        
        Args:
            employee: Employee instance
            motif: PayrollMotif instance
            period_start: Start date of payroll period
            period_end: End date of payroll period
            
        Returns:
            Dict with all calculated payroll values
        """
        result = {
            'gross_taxable': Decimal('0.00'),
            'gross_non_taxable': Decimal('0.00'),
            'cnss_employee': Decimal('0.00'),
            'cnam_employee': Decimal('0.00'),
            'its_total': Decimal('0.00'),
            'its_tranche1': Decimal('0.00'),
            'its_tranche2': Decimal('0.00'),
            'its_tranche3': Decimal('0.00'),
            'net_salary': Decimal('0.00'),
            'employer_cnss': Decimal('0.00'),
            'employer_cnam': Decimal('0.00'),
            'benefits_in_kind': Decimal('0.00'),
        }
        
        # Get all payroll line items for this employee/period
        line_items = self._get_payroll_line_items(employee, motif, period_start)
        
        # Calculate gross amounts
        gross_gains = sum(item.amount for item in line_items 
                         if item.payroll_element.type == 'G')
        gross_deductions = sum(item.amount for item in line_items 
                             if item.payroll_element.type == 'R' 
                             and item.payroll_element.deduction_from == 'Brut')
        net_deductions = sum(item.amount for item in line_items 
                           if item.payroll_element.type == 'R' 
                           and item.payroll_element.deduction_from == 'Net')
        
        # Calculate tax bases
        cnss_base = sum(item.amount for item in line_items 
                       if item.payroll_element.type == 'G' 
                       and item.payroll_element.affects_cnss)
        cnam_base = sum(item.amount for item in line_items 
                       if item.payroll_element.type == 'G' 
                       and item.payroll_element.affects_cnam)
        its_base = sum(item.amount for item in line_items 
                      if item.payroll_element.type == 'G' 
                      and item.payroll_element.affects_its)
        benefits_in_kind = sum(item.amount for item in line_items 
                             if item.payroll_element.type == 'G' 
                             and item.payroll_element.is_benefit_in_kind)
        
        # Apply deductions to bases
        cnss_base = max(Decimal('0.00'), cnss_base - gross_deductions)
        cnam_base = max(Decimal('0.00'), cnam_base - gross_deductions)
        its_base = max(Decimal('0.00'), its_base - gross_deductions)
        
        # Calculate contributions
        if employee.is_subject_to_cnss() and motif.employee_subject_to_cnss:
            result['cnss_employee'] = self._calculate_cnss_employee(cnss_base)
            result['employer_cnss'] = self._calculate_cnss_employer(cnss_base, employee)
        
        if employee.is_subject_to_cnam() and motif.employee_subject_to_cnam:
            result['cnam_employee'] = self._calculate_cnam_employee(cnam_base)
            result['employer_cnam'] = self._calculate_cnam_employer(cnam_base, employee)
        
        if employee.is_subject_to_its() and motif.employee_subject_to_its:
            its_calculation = self._calculate_its(
                its_base, result['cnss_employee'], result['cnam_employee'],
                benefits_in_kind, employee.is_expatriate
            )
            result['its_total'] = its_calculation['total']
            result['its_tranche1'] = its_calculation['tranche1']
            result['its_tranche2'] = its_calculation['tranche2']
            result['its_tranche3'] = its_calculation['tranche3']
        
        # Calculate final amounts
        result['gross_taxable'] = its_base
        result['gross_non_taxable'] = gross_gains - its_base
        result['benefits_in_kind'] = benefits_in_kind
        
        # Calculate net salary
        result['net_salary'] = (
            gross_gains 
            - result['cnss_employee'] 
            - result['cnam_employee'] 
            - result['its_total'] 
            - gross_deductions 
            - net_deductions 
            - benefits_in_kind
        )
        
        return result
    
    def _get_payroll_line_items(self, employee, motif, period):
        """Get payroll line items for calculation"""
        # This would query the PayrollLineItem model
        # Placeholder - actual implementation would fetch from database
        return []
    
    def _calculate_cnss_employee(self, base_amount):
        """Calculate employee CNSS contribution"""
        ceiling = self.system_parameters.get_cnss_ceiling()
        taxable_amount = min(base_amount, ceiling)
        rate = self.system_parameters.get_cnss_rate_employee()
        return (taxable_amount * rate).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
    
    def _calculate_cnss_employer(self, base_amount, employee):
        """Calculate employer CNSS contribution"""
        ceiling = self.system_parameters.get_cnss_ceiling()
        taxable_amount = min(base_amount, ceiling)
        base_rate = self.system_parameters.get_cnss_rate_employer()
        
        # Apply reimbursement rate if applicable
        if employee.cnss_reimbursement_rate:
            rate = base_rate * (Decimal('1.00') + employee.cnss_reimbursement_rate)
        else:
            rate = base_rate
            
        return (taxable_amount * rate).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
    
    def _calculate_cnam_employee(self, base_amount):
        """Calculate employee CNAM contribution"""
        rate = self.system_parameters.get_cnam_rate_employee()
        return (base_amount * rate).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
    
    def _calculate_cnam_employer(self, base_amount, employee):
        """Calculate employer CNAM contribution"""
        base_rate = self.system_parameters.get_cnam_rate_employer()
        
        # Apply reimbursement rate if applicable
        if employee.cnam_reimbursement_rate:
            rate = base_rate * (Decimal('1.00') + employee.cnam_reimbursement_rate)
        else:
            rate = base_rate
            
        return (base_amount * rate).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
    
    def _calculate_its(self, taxable_income, cnss_amount, cnam_amount, benefits_in_kind, is_expatriate=False):
        """
        Calculate ITS (Income Tax on Salaries) with progressive brackets
        Equivalent to ITSm, tranche1ITS, tranche2ITS, tranche3ITS methods
        """
        # Adjust taxable income
        if self.system_parameters.deduct_cnss_from_its:
            taxable_income -= cnss_amount
        if self.system_parameters.deduct_cnam_from_its:
            taxable_income -= cnam_amount
        
        # Apply abatement
        taxable_income = max(Decimal('0.00'), taxable_income - self.system_parameters.tax_abatement)
        
        # Get tax brackets
        brackets = self.system_parameters.get_its_brackets()
        
        result = {
            'total': Decimal('0.00'),
            'tranche1': Decimal('0.00'),
            'tranche2': Decimal('0.00'),
            'tranche3': Decimal('0.00'),
        }
        
        remaining_income = taxable_income
        tranche_names = ['tranche1', 'tranche2', 'tranche3']
        
        for i, bracket in enumerate(brackets):
            if remaining_income <= 0:
                break
            
            bracket_min = Decimal(str(bracket['min']))
            bracket_max = Decimal(str(bracket['max'])) if bracket['max'] != float('inf') else None
            bracket_rate = Decimal(str(bracket['rate']))
            
            # Adjust rate for expatriates on first bracket
            if i == 0 and is_expatriate:
                bracket_rate = Decimal('0.075')  # 7.5% for expatriates vs 15% for nationals
            
            # Calculate taxable amount in this bracket
            if bracket_max:
                bracket_width = bracket_max - bracket_min
                taxable_in_bracket = min(remaining_income, bracket_width)
            else:
                taxable_in_bracket = remaining_income
            
            # Calculate tax for this bracket
            bracket_tax = (taxable_in_bracket * bracket_rate).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
            
            # Store in appropriate tranche
            if i < len(tranche_names):
                result[tranche_names[i]] = bracket_tax
            
            result['total'] += bracket_tax
            remaining_income -= taxable_in_bracket
        
        return result


class PayrollFunctionsStatic:
    """
    Payroll calculation functions F01-F24 converted from FonctionsPaie.java
    Static methods version
    """
    
    @staticmethod
    def F01_NJT(employee, motif, period):
        """
        F01: Get worked days (Nombre de Jours Travaillés)
        Equivalent to F01_NJT in FonctionsPaie.java
        """
        try:
            from core.models import WorkedDays  # Import here to avoid circular imports
            worked_days = WorkedDays.objects.get(
                employee=employee, 
                motif=motif, 
                period=period
            )
            return worked_days.worked_days
        except (ImportError, AttributeError):
            # If WorkedDays model doesn't exist or is not accessible
            return Decimal('0.00')
        except WorkedDays.DoesNotExist:
            return Decimal('0.00')
    
    @staticmethod
    def F02_sbJour(employee, motif, period, payroll_elements=None):
        """
        F02: Calculate daily base salary
        Equivalent to F02_sbJour in FonctionsPaie.java
        """
        # Get base salary element (typically rubric ID 1)
        if payroll_elements:
            base_element = next((pe for pe in payroll_elements if pe.id == 1), None)
            if base_element:
                return base_element.base_amount
        
        # Fallback to salary grade
        if employee.salary_grade:
            default_days = 26  # Could be configurable
            return employee.salary_grade.base_salary / default_days
        
        return Decimal('0.00')
    
    @staticmethod
    def F03_sbHoraire(employee, motif, period, payroll_elements=None):
        """
        F03: Calculate hourly base salary
        Equivalent to F03_sbHoraire in FonctionsPaie.java
        """
        daily_salary = PayrollFunctionsStatic.F02_sbJour(employee, motif, period, payroll_elements)
        if daily_salary > 0 and employee.contract_hours_per_week > 0:
            monthly_salary = daily_salary * 30
            monthly_hours = employee.contract_hours_per_week * Decimal('52') / Decimal('12')
            return monthly_salary / monthly_hours
        
        return Decimal('0.00')
    
    @staticmethod
    def F04_TauxAnciennete(employee, period):
        """
        F04: Calculate seniority rate
        Equivalent to F04_TauxAnciennete in FonctionsPaie.java
        """
        return employee.get_seniority_rate(period)
    
    @staticmethod
    def F23_TauxAncienneteSpeciale(employee, period):
        """
        F23: Calculate special seniority rate
        Equivalent to F23_TauxAncienneteSpeciale in FonctionsPaie.java
        """
        return employee.get_seniority_rate(period, special_calculation=True)
    
    @staticmethod
    def F24_augmentationSalaireFixe(employee, period, system_params):
        """
        F24: Calculate fixed salary increase
        Equivalent to F24_augmentationSalaireFixe in FonctionsPaie.java
        """
        # This would implement the salary increase calculation logic
        # based on system parameters and employee data
        return Decimal('0.00')  # Placeholder


class OvertimeCalculator:
    """
    Enhanced overtime calculation utilities with complex weekly breakdown and holiday rates
    Equivalent to overtime logic in Java payroll system
    """
    
    @staticmethod
    def calculate_weekly_overtime_breakdown(weekly_hours: List[Decimal], standard_daily_hours: Decimal = Decimal('8'),
                                          standard_weekly_hours: Decimal = Decimal('40'),
                                          holiday_days: List[bool] = None) -> Dict[str, Decimal]:
        """
        Calculate overtime with complex weekly breakdown including daily and weekly limits
        
        Args:
            weekly_hours: List of hours worked each day (7 days)
            standard_daily_hours: Standard hours per day (default 8)
            standard_weekly_hours: Standard hours per week (default 40)
            holiday_days: List of booleans indicating holiday days
            
        Returns:
            Dict with detailed overtime breakdown
        """
        if holiday_days is None:
            holiday_days = [False] * 7
            
        # Initialize results
        result = {
            'regular_hours': Decimal('0.00'),
            'daily_ot_115': Decimal('0.00'),  # Daily OT up to 2 hours at 115%
            'daily_ot_140': Decimal('0.00'),  # Daily OT 2-6 hours at 140%
            'daily_ot_150': Decimal('0.00'),  # Daily OT 6+ hours at 150%
            'weekly_ot_150': Decimal('0.00'), # Weekly OT beyond 40h at 150%
            'holiday_ot_200': Decimal('0.00'), # Holiday work at 200%
            'sunday_ot_175': Decimal('0.00'),  # Sunday work at 175%
            'night_ot_125': Decimal('0.00'),   # Night work at 125%
            'total_hours': Decimal('0.00'),
            'total_regular_equivalent': Decimal('0.00')
        }
        
        daily_regular_hours = Decimal('0.00')
        daily_overtime_hours = Decimal('0.00')
        
        # Process each day
        for day_index, hours in enumerate(weekly_hours):
            if hours <= 0:
                continue
                
            result['total_hours'] += hours
            is_holiday = holiday_days[day_index] if day_index < len(holiday_days) else False
            is_sunday = day_index == 6  # Assuming Sunday is index 6
            
            if is_holiday:
                # All holiday hours at 200%
                result['holiday_ot_200'] += hours
                result['total_regular_equivalent'] += hours * Decimal('2.00')
            elif is_sunday:
                # Sunday work at 175%
                result['sunday_ot_175'] += hours
                result['total_regular_equivalent'] += hours * Decimal('1.75')
            else:
                # Regular weekday processing
                if hours <= standard_daily_hours:
                    # Within daily standard hours
                    daily_regular_hours += hours
                    result['regular_hours'] += hours
                    result['total_regular_equivalent'] += hours
                else:
                    # Daily overtime
                    regular_portion = standard_daily_hours
                    overtime_portion = hours - standard_daily_hours
                    
                    daily_regular_hours += regular_portion
                    daily_overtime_hours += overtime_portion
                    result['regular_hours'] += regular_portion
                    result['total_regular_equivalent'] += regular_portion
                    
                    # Distribute daily overtime by brackets
                    remaining_ot = overtime_portion
                    
                    # First 2 hours of daily OT at 115%
                    if remaining_ot > 0:
                        bracket_115 = min(remaining_ot, Decimal('2.00'))
                        result['daily_ot_115'] += bracket_115
                        result['total_regular_equivalent'] += bracket_115 * Decimal('1.15')
                        remaining_ot -= bracket_115
                    
                    # Next 4 hours (hours 3-6) at 140%
                    if remaining_ot > 0:
                        bracket_140 = min(remaining_ot, Decimal('4.00'))
                        result['daily_ot_140'] += bracket_140
                        result['total_regular_equivalent'] += bracket_140 * Decimal('1.40')
                        remaining_ot -= bracket_140
                    
                    # Remaining hours (6+) at 150%
                    if remaining_ot > 0:
                        result['daily_ot_150'] += remaining_ot
                        result['total_regular_equivalent'] += remaining_ot * Decimal('1.50')
        
        # Calculate weekly overtime (beyond 40 hours for the week)
        total_regular_and_daily_ot = daily_regular_hours + daily_overtime_hours
        if total_regular_and_daily_ot > standard_weekly_hours:
            weekly_overtime = total_regular_and_daily_ot - standard_weekly_hours
            result['weekly_ot_150'] += weekly_overtime
            result['total_regular_equivalent'] += weekly_overtime * Decimal('1.50')
        
        return result
    
    @staticmethod
    def calculate_overtime_rates(total_hours_worked, standard_hours=8):
        """
        Legacy method for simple overtime calculation (maintained for compatibility)
        """
        overtime_hours = max(Decimal('0.00'), total_hours_worked - standard_hours)
        
        result = {
            'ot_115': Decimal('0.00'),  # First 8 OT hours at 115%
            'ot_140': Decimal('0.00'),  # Hours 9-14 at 140%
            'ot_150': Decimal('0.00'),  # Hours 15+ at 150%
            'ot_200': Decimal('0.00'),  # Holiday overtime at 200%
        }
        
        remaining_ot = overtime_hours
        
        # First bracket: up to 8 hours at 115%
        if remaining_ot > 0:
            ot_115 = min(remaining_ot, Decimal('8.00'))
            result['ot_115'] = ot_115
            remaining_ot -= ot_115
        
        # Second bracket: hours 9-14 at 140%
        if remaining_ot > 0:
            ot_140 = min(remaining_ot, Decimal('6.00'))
            result['ot_140'] = ot_140
            remaining_ot -= ot_140
        
        # Third bracket: hours 15+ at 150%
        if remaining_ot > 0:
            result['ot_150'] = remaining_ot
        
        return result
    
    @staticmethod
    def calculate_overtime_amounts(overtime_rates, hourly_rate):
        """Calculate overtime payment amounts"""
        return {
            'ot_115_amount': overtime_rates['ot_115'] * hourly_rate * Decimal('1.15'),
            'ot_140_amount': overtime_rates['ot_140'] * hourly_rate * Decimal('1.40'),
            'ot_150_amount': overtime_rates['ot_150'] * hourly_rate * Decimal('1.50'),
            'ot_200_amount': overtime_rates['ot_200'] * hourly_rate * Decimal('2.00'),
        }
    
    @staticmethod
    def calculate_night_differential(hours_worked: Decimal, night_start_hour: int = 22, 
                                   night_end_hour: int = 6, differential_rate: Decimal = Decimal('0.25')) -> Decimal:
        """
        Calculate night work differential (typically 25% additional)
        
        Args:
            hours_worked: Total hours worked during night period
            night_start_hour: Hour when night period starts (default 22:00)
            night_end_hour: Hour when night period ends (default 06:00)
            differential_rate: Additional rate for night work (default 25%)
            
        Returns:
            Additional payment for night work
        """
        if hours_worked <= 0:
            return Decimal('0.00')
            
        # Night period is typically 22:00 to 06:00 (8 hours)
        night_period_hours = (24 + night_end_hour - night_start_hour) % 24
        
        # Cap night hours to actual night period
        night_hours = min(hours_worked, Decimal(str(night_period_hours)))
        
        return night_hours * differential_rate


class InstallmentCalculator:
    """
    Enhanced installment and loan calculation utilities with quota cessible logic
    Equivalent to installment logic in PaieClass.paieCalcule
    """
    
    @staticmethod
    def calculate_quota_cessible(net_before_installments: Decimal, quota_percentage: Decimal,
                               minimum_net_required: Decimal = None) -> Decimal:
        """
        Calculate the maximum deductible amount based on quota cessible
        
        Args:
            net_before_installments: Net salary before installment deductions
            quota_percentage: Maximum percentage that can be deducted (typically 30-40%)
            minimum_net_required: Minimum net salary that must remain (optional)
            
        Returns:
            Maximum allowable installment amount
        """
        quota_amount = net_before_installments * quota_percentage / Decimal('100')
        
        if minimum_net_required:
            # Ensure minimum net is preserved
            max_deduction_for_minimum = net_before_installments - minimum_net_required
            quota_amount = min(quota_amount, max_deduction_for_minimum)
        
        return max(Decimal('0.00'), quota_amount)
    
    @staticmethod
    def adjust_installments_for_quota(installments: List[Dict], quota_cessible: Decimal,
                                    priority_order: List[str] = None) -> Dict[str, Union[List[Dict], Decimal]]:
        """
        Adjust installments when they exceed the quota cessible with priority system
        
        Args:
            installments: List of installment dictionaries with 'amount', 'type', 'priority'
            quota_cessible: Maximum allowable total installment amount
            priority_order: Order of priority for installment types (optional)
            
        Returns:
            Dict with adjusted installments, total deducted, and carry-forward amounts
        """
        if not installments:
            return {
                'adjusted_installments': [],
                'total_deducted': Decimal('0.00'),
                'total_carry_forward': Decimal('0.00'),
                'quota_exceeded': False
            }
        
        total_requested = sum(Decimal(str(installment['amount'])) for installment in installments)
        
        if total_requested <= quota_cessible:
            return {
                'adjusted_installments': installments,
                'total_deducted': total_requested,
                'total_carry_forward': Decimal('0.00'),
                'quota_exceeded': False
            }
        
        # Sort by priority if specified
        if priority_order:
            sorted_installments = sorted(installments, 
                                       key=lambda x: priority_order.index(x.get('type', 'other')) 
                                       if x.get('type', 'other') in priority_order else 999)
        else:
            # Default priority: court orders, garnishments, loans, advances
            default_priority = ['court_order', 'garnishment', 'loan', 'advance', 'other']
            sorted_installments = sorted(installments,
                                       key=lambda x: default_priority.index(x.get('type', 'other'))
                                       if x.get('type', 'other') in default_priority else 999)
        
        adjusted_installments = []
        remaining_quota = quota_cessible
        total_carry_forward = Decimal('0.00')
        
        # Process installments by priority
        for installment in sorted_installments:
            requested_amount = Decimal(str(installment['amount']))
            
            if remaining_quota >= requested_amount:
                # Full amount can be deducted
                adjusted_installment = installment.copy()
                adjusted_installments.append(adjusted_installment)
                remaining_quota -= requested_amount
            elif remaining_quota > 0:
                # Partial amount can be deducted
                adjusted_installment = installment.copy()
                adjusted_installment['amount'] = remaining_quota
                adjusted_installment['carry_forward'] = requested_amount - remaining_quota
                adjusted_installments.append(adjusted_installment)
                total_carry_forward += requested_amount - remaining_quota
                remaining_quota = Decimal('0.00')
            else:
                # No quota remaining - full carry forward
                adjusted_installment = installment.copy()
                adjusted_installment['amount'] = Decimal('0.00')
                adjusted_installment['carry_forward'] = requested_amount
                adjusted_installments.append(adjusted_installment)
                total_carry_forward += requested_amount
        
        return {
            'adjusted_installments': adjusted_installments,
            'total_deducted': quota_cessible - remaining_quota,
            'total_carry_forward': total_carry_forward,
            'quota_exceeded': True,
            'remaining_quota': remaining_quota
        }
    
    @staticmethod
    def calculate_proportional_reduction(installments: List[Dict], quota_cessible: Decimal) -> List[Dict]:
        """
        Apply proportional reduction to all installments when quota is exceeded
        Alternative to priority-based adjustment
        
        Args:
            installments: List of installment dictionaries
            quota_cessible: Maximum allowable total installment amount
            
        Returns:
            List of adjusted installments with proportional reductions
        """
        total_requested = sum(Decimal(str(installment['amount'])) for installment in installments)
        
        if total_requested <= quota_cessible:
            return installments  # No adjustment needed
        
        # Calculate reduction ratio
        reduction_ratio = quota_cessible / total_requested
        
        adjusted_installments = []
        for installment in installments:
            original_amount = Decimal(str(installment['amount']))
            reduced_amount = original_amount * reduction_ratio
            
            adjusted_installment = installment.copy()
            adjusted_installment['amount'] = reduced_amount.quantize(Decimal('0.01'))
            adjusted_installment['original_amount'] = original_amount
            adjusted_installment['reduction_applied'] = original_amount - reduced_amount
            adjusted_installments.append(adjusted_installment)
        
        return adjusted_installments
    
    @staticmethod
    def validate_installment_limits(installments: List[Dict], salary_info: Dict) -> Dict[str, Union[bool, List[str]]]:
        """
        Validate installments against legal and policy limits
        
        Args:
            installments: List of installment dictionaries
            salary_info: Dict with salary information (gross, net, etc.)
            
        Returns:
            Dict with validation results and any violations
        """
        violations = []
        warnings = []
        
        total_installments = sum(Decimal(str(inst['amount'])) for inst in installments)
        net_salary = Decimal(str(salary_info.get('net_salary', 0)))
        gross_salary = Decimal(str(salary_info.get('gross_salary', 0)))
        
        # Check total installments vs net salary (typically max 40%)
        if net_salary > 0:
            installment_ratio = (total_installments / net_salary) * 100
            if installment_ratio > 40:
                violations.append(f"Total installments ({installment_ratio:.1f}%) exceed 40% of net salary")
            elif installment_ratio > 30:
                warnings.append(f"Total installments ({installment_ratio:.1f}%) exceed recommended 30% of net salary")
        
        # Check individual installment limits
        for installment in installments:
            inst_type = installment.get('type', 'other')
            amount = Decimal(str(installment['amount']))
            
            if inst_type == 'advance' and gross_salary > 0:
                # Salary advances typically limited to 50% of gross
                if amount > gross_salary * Decimal('0.5'):
                    violations.append(f"Salary advance exceeds 50% of gross salary")
                    
            elif inst_type == 'loan' and net_salary > 0:
                # Loan installments typically limited to 25% of net
                if amount > net_salary * Decimal('0.25'):
                    warnings.append(f"Loan installment exceeds recommended 25% of net salary")
        
        return {
            'is_valid': len(violations) == 0,
            'violations': violations,
            'warnings': warnings,
            'total_installment_percentage': (total_installments / net_salary * 100) if net_salary > 0 else 0
        }
    
    # ========== MAURITANIAN TAX CALCULATION METHODS ==========
    # Converted from PaieClass.java
    
    def calculate_cnss_employee(self, base_amount: Decimal, exchange_rate: Decimal = Decimal('1.0'), 
                               year: int = 2018) -> Decimal:
        """
        Calculate employee CNSS contribution (1%)
        Equivalent to CNSSm method in PaieClass.java
        
        Args:
            base_amount: CNSS taxable amount
            exchange_rate: Currency exchange rate (default 1.0)
            year: Tax year (default 2018)
            
        Returns:
            Employee CNSS contribution amount
        """
        cnss_ceiling = Decimal('15000.00')  # MRU 15,000 monthly ceiling
        
        # Apply exchange rate
        adjusted_amount = base_amount * exchange_rate
        
        # Apply ceiling
        taxable_amount = min(adjusted_amount, cnss_ceiling)
        
        # Calculate 1% contribution
        contribution = taxable_amount / Decimal('100')
        
        return max(Decimal('0.00'), contribution).quantize(Decimal('0.01'))
    
    def calculate_cnss_employer(self, base_amount: Decimal, reimbursement_rate: Decimal = Decimal('0.0'),
                               exchange_rate: Decimal = Decimal('1.0'), year: int = 2018) -> Decimal:
        """
        Calculate employer CNSS contribution with reimbursement
        Equivalent to RCNSSm method in PaieClass.java
        
        Args:
            base_amount: CNSS taxable amount
            reimbursement_rate: Employer reimbursement rate percentage
            exchange_rate: Currency exchange rate
            year: Tax year
            
        Returns:
            Employer CNSS reimbursement amount
        """
        cnss_ceiling = Decimal('15000.00')
        
        # Apply exchange rate and ceiling
        adjusted_amount = base_amount * exchange_rate
        taxable_amount = min(adjusted_amount, cnss_ceiling)
        
        # Base employer contribution (1% like employee)
        base_contribution = taxable_amount / Decimal('100')
        
        # Apply reimbursement rate
        reimbursement = base_contribution * (reimbursement_rate / Decimal('100'))
        
        return max(Decimal('0.00'), reimbursement).quantize(Decimal('0.01'))
    
    def calculate_cnam_employee(self, base_amount: Decimal) -> Decimal:
        """
        Calculate employee CNAM contribution (4%)
        Equivalent to CNAMm method in PaieClass.java
        
        Args:
            base_amount: CNAM taxable amount (no ceiling)
            
        Returns:
            Employee CNAM contribution amount
        """
        contribution = base_amount * Decimal('0.04')  # 4% rate
        return max(Decimal('0.00'), contribution).quantize(Decimal('0.01'))
    
    def calculate_cnam_employer(self, base_amount: Decimal, reimbursement_rate: Decimal = Decimal('0.0')) -> Decimal:
        """
        Calculate employer CNAM contribution (5% = employee 4% × 1.25)
        Equivalent to RCNAMm method in PaieClass.java
        
        Args:
            base_amount: CNAM taxable amount
            reimbursement_rate: Employer reimbursement rate percentage
            
        Returns:
            Employer CNAM contribution amount
        """
        # Employer rate is 5% (1.25 times the employee rate of 4%)
        employee_contribution = base_amount * Decimal('0.04')
        employer_contribution = employee_contribution * Decimal('1.25')  # 5% total
        
        # Apply reimbursement rate if applicable
        if reimbursement_rate > 0:
            reimbursement = employer_contribution * (reimbursement_rate / Decimal('100'))
            return max(Decimal('0.00'), reimbursement).quantize(Decimal('0.01'))
        
        return max(Decimal('0.00'), employer_contribution).quantize(Decimal('0.01'))
    
    def calculate_its_tranche1(self, year: int, taxable_income: Decimal, cnss_amount: Decimal, 
                             cnam_amount: Decimal, base_salary: Decimal, benefits_in_kind: Decimal,
                             exchange_rate: Decimal = Decimal('1.0'), is_expatriate: bool = False) -> Decimal:
        """
        Calculate ITS Tranche 1 (0 - MRU 9,000 at 15% or 7.5% for expatriates)
        Equivalent to tranche1ITS method in PaieClass.java
        
        Args:
            year: Tax year
            taxable_income: Gross taxable income
            cnss_amount: CNSS deduction amount
            cnam_amount: CNAM deduction amount  
            base_salary: Base salary amount
            benefits_in_kind: Benefits in kind amount
            exchange_rate: Currency exchange rate
            is_expatriate: Whether employee is expatriate
            
        Returns:
            ITS Tranche 1 amount
        """
        abatement = Decimal(str(getattr(self.system_parameters, 'tax_abatement', 0) or 0))
        tranche1_limit = Decimal('9000.00')  # MRU 9,000
        rate = Decimal('0.15')  # 15% for nationals
        
        if is_expatriate:
            rate = Decimal('0.075')  # 7.5% for expatriates
        
        # Apply deductions if configured
        deduct_cnss = getattr(self.system_parameters, 'deduct_cnss_from_its', True)
        deduct_cnam = getattr(self.system_parameters, 'deduct_cnam_from_its', True)
        
        x_cnss = cnss_amount if deduct_cnss else Decimal('0.00')
        x_cnam = cnam_amount if deduct_cnam else Decimal('0.00')
        
        # Calculate taxable base
        taxable_base = taxable_income - x_cnss - x_cnam - abatement
        
        # Benefits in kind special treatment
        taxable_base = self._apply_benefits_in_kind_deduction(taxable_base, benefits_in_kind, base_salary)
        
        if taxable_base <= 0:
            return Decimal('0.00')
        
        # Calculate tax on tranche 1 portion
        tranche1_taxable = min(taxable_base, tranche1_limit)
        tax_amount = tranche1_taxable * rate
        
        return max(Decimal('0.00'), tax_amount).quantize(Decimal('0.01'))
    
    def calculate_its_tranche2(self, year: int, taxable_income: Decimal, cnss_amount: Decimal,
                             cnam_amount: Decimal, base_salary: Decimal, benefits_in_kind: Decimal,
                             exchange_rate: Decimal = Decimal('1.0'), is_expatriate: bool = False) -> Decimal:
        """
        Calculate ITS Tranche 2 (MRU 9,001 - 21,000 at 25% or 12.5% for expatriates)
        Equivalent to tranche2ITS method in PaieClass.java
        """
        abatement = Decimal(str(getattr(self.system_parameters, 'tax_abatement', 0) or 0))
        tranche1_limit = Decimal('9000.00')
        tranche2_limit = Decimal('21000.00')
        rate = Decimal('0.25')  # 25% for nationals
        
        # Check for alternative rate mode
        its_mode = getattr(self.system_parameters, 'its_mode', None)
        if its_mode == 'T':
            rate = Decimal('0.20')  # 20% in "T" mode
        
        if is_expatriate:
            rate = Decimal('0.125')  # 12.5% for expatriates (half of 25%)
        
        # Apply deductions
        deduct_cnss = getattr(self.system_parameters, 'deduct_cnss_from_its', True)
        deduct_cnam = getattr(self.system_parameters, 'deduct_cnam_from_its', True)
        
        x_cnss = cnss_amount if deduct_cnss else Decimal('0.00')
        x_cnam = cnam_amount if deduct_cnam else Decimal('0.00')
        
        # Calculate taxable base  
        taxable_base = taxable_income - x_cnss - x_cnam - abatement
        
        # Benefits in kind special treatment
        taxable_base = self._apply_benefits_in_kind_deduction(taxable_base, benefits_in_kind, base_salary)
        
        if taxable_base <= tranche1_limit:
            return Decimal('0.00')  # No tax in tranche 2
        
        # Calculate tax on tranche 2 portion
        tranche2_taxable = min(taxable_base - tranche1_limit, tranche2_limit - tranche1_limit)
        tax_amount = tranche2_taxable * rate
        
        return max(Decimal('0.00'), tax_amount).quantize(Decimal('0.01'))
    
    def calculate_its_tranche3(self, year: int, taxable_income: Decimal, cnss_amount: Decimal,
                             cnam_amount: Decimal, base_salary: Decimal, benefits_in_kind: Decimal,
                             exchange_rate: Decimal = Decimal('1.0'), is_expatriate: bool = False) -> Decimal:
        """
        Calculate ITS Tranche 3 (MRU 21,001+ at 40% or 20% for expatriates)
        Equivalent to tranche3ITS method in PaieClass.java
        """
        abatement = Decimal(str(getattr(self.system_parameters, 'tax_abatement', 0) or 0))
        tranche1_limit = Decimal('9000.00')
        tranche2_limit = Decimal('21000.00')
        rate = Decimal('0.40')  # 40% for nationals
        
        # Check for alternative rate mode
        its_mode = getattr(self.system_parameters, 'its_mode', None)
        if its_mode == 'T':
            rate = Decimal('0.20')  # 20% in "T" mode
        
        if is_expatriate:
            rate = Decimal('0.20')  # 20% for expatriates (half of 40%)
        
        # Apply deductions
        deduct_cnss = getattr(self.system_parameters, 'deduct_cnss_from_its', True)
        deduct_cnam = getattr(self.system_parameters, 'deduct_cnam_from_its', True)
        
        x_cnss = cnss_amount if deduct_cnss else Decimal('0.00')
        x_cnam = cnam_amount if deduct_cnam else Decimal('0.00')
        
        # Calculate taxable base
        taxable_base = taxable_income - x_cnss - x_cnam - abatement
        
        # Benefits in kind special treatment
        taxable_base = self._apply_benefits_in_kind_deduction(taxable_base, benefits_in_kind, base_salary)
        
        if taxable_base <= tranche2_limit:
            return Decimal('0.00')  # No tax in tranche 3
        
        # Calculate tax on tranche 3 portion (unlimited)
        tranche3_taxable = taxable_base - tranche2_limit
        tax_amount = tranche3_taxable * rate
        
        return max(Decimal('0.00'), tax_amount).quantize(Decimal('0.01'))
    
    def calculate_its_total(self, year: int, taxable_income: Decimal, cnss_amount: Decimal,
                           cnam_amount: Decimal, base_salary: Decimal, benefits_in_kind: Decimal,
                           exchange_rate: Decimal = Decimal('1.0'), is_expatriate: bool = False) -> Decimal:
        """
        Calculate total ITS (sum of all tranches)
        Equivalent to ITSm method in PaieClass.java
        
        Returns:
            Total ITS amount across all three tranches
        """
        tranche1 = self.calculate_its_tranche1(
            year, taxable_income, cnss_amount, cnam_amount, 
            base_salary, benefits_in_kind, exchange_rate, is_expatriate
        )
        
        tranche2 = self.calculate_its_tranche2(
            year, taxable_income, cnss_amount, cnam_amount,
            base_salary, benefits_in_kind, exchange_rate, is_expatriate  
        )
        
        tranche3 = self.calculate_its_tranche3(
            year, taxable_income, cnss_amount, cnam_amount,
            base_salary, benefits_in_kind, exchange_rate, is_expatriate
        )
        
        total_its = tranche1 + tranche2 + tranche3
        
        return max(Decimal('0.00'), total_its).quantize(Decimal('0.01'))
    
    def calculate_its_reimbursement(self, year: int, taxable_income: Decimal, cnss_amount: Decimal,
                                   cnam_amount: Decimal, base_salary: Decimal, benefits_in_kind: Decimal,
                                   reimb_rate_t1: Decimal, reimb_rate_t2: Decimal, reimb_rate_t3: Decimal,
                                   exchange_rate: Decimal = Decimal('1.0'), is_expatriate: bool = False) -> Decimal:
        """
        Calculate ITS employer reimbursement
        Equivalent to RITSm method in PaieClass.java
        
        Args:
            reimb_rate_t1: Tranche 1 reimbursement rate percentage
            reimb_rate_t2: Tranche 2 reimbursement rate percentage  
            reimb_rate_t3: Tranche 3 reimbursement rate percentage
            
        Returns:
            Total ITS reimbursement amount
        """
        tranche1_tax = self.calculate_its_tranche1(
            year, taxable_income, cnss_amount, cnam_amount,
            base_salary, benefits_in_kind, exchange_rate, is_expatriate
        )
        
        tranche2_tax = self.calculate_its_tranche2(
            year, taxable_income, cnss_amount, cnam_amount,
            base_salary, benefits_in_kind, exchange_rate, is_expatriate
        )
        
        tranche3_tax = self.calculate_its_tranche3(
            year, taxable_income, cnss_amount, cnam_amount,
            base_salary, benefits_in_kind, exchange_rate, is_expatriate
        )
        
        # Apply reimbursement rates
        tranche1_reimb = tranche1_tax * (reimb_rate_t1 / Decimal('100'))
        tranche2_reimb = tranche2_tax * (reimb_rate_t2 / Decimal('100'))
        tranche3_reimb = tranche3_tax * (reimb_rate_t3 / Decimal('100'))
        
        total_reimbursement = tranche1_reimb + tranche2_reimb + tranche3_reimb
        
        return max(Decimal('0.00'), total_reimbursement).quantize(Decimal('0.01'))
    
    def calculate_gross_from_net(self, net_target: Decimal, employee, year: int = 2018, 
                                is_expatriate: bool = False, benefits_in_kind: Decimal = Decimal('0.00'),
                                max_iterations: int = 50, tolerance: Decimal = Decimal('0.01')) -> Dict[str, Decimal]:
        """
        Calculate gross salary required to achieve a target net salary (BrutDuNet)
        Uses iterative approximation method equivalent to Java BrutDuNet function
        
        Args:
            net_target: Target net salary to achieve
            employee: Employee instance for rates and ceilings
            year: Tax year for calculations
            is_expatriate: Whether employee is expatriate
            benefits_in_kind: Benefits in kind amount
            max_iterations: Maximum iterations for convergence
            tolerance: Acceptable difference for convergence
            
        Returns:
            Dict with gross amount and calculation breakdown
        """
        # Initial guess - start with net target * 1.5 as rough estimate
        gross_estimate = net_target * Decimal('1.5')
        
        for iteration in range(max_iterations):
            # Calculate all deductions based on current gross estimate
            cnss_amount = self.calculate_cnss_employee(gross_estimate, Decimal('1.0'), year)
            cnam_amount = self.calculate_cnam_employee(gross_estimate)
            
            # Calculate ITS
            its_amount = self.calculate_its_total(
                year, gross_estimate, cnss_amount, cnam_amount,
                gross_estimate, benefits_in_kind, Decimal('1.0'), is_expatriate
            )
            
            # Calculate resulting net salary
            calculated_net = gross_estimate - cnss_amount - cnam_amount - its_amount - benefits_in_kind
            
            # Check convergence
            difference = abs(calculated_net - net_target)
            if difference <= tolerance:
                return {
                    'gross_salary': gross_estimate.quantize(Decimal('0.01')),
                    'net_salary': calculated_net.quantize(Decimal('0.01')),
                    'cnss_employee': cnss_amount,
                    'cnam_employee': cnam_amount,
                    'its_total': its_amount,
                    'benefits_in_kind': benefits_in_kind,
                    'iterations': iteration + 1,
                    'converged': True
                }
            
            # Adjust gross estimate based on difference
            adjustment_factor = net_target / calculated_net if calculated_net > 0 else Decimal('1.1')
            gross_estimate = gross_estimate * adjustment_factor
            
            # Safety bounds to prevent runaway calculations
            if gross_estimate > net_target * Decimal('10'):
                gross_estimate = net_target * Decimal('10')
            elif gross_estimate < net_target:
                gross_estimate = net_target * Decimal('1.1')
        
        # Return best estimate if convergence not achieved
        cnss_amount = self.calculate_cnss_employee(gross_estimate, Decimal('1.0'), year)
        cnam_amount = self.calculate_cnam_employee(gross_estimate)
        its_amount = self.calculate_its_total(
            year, gross_estimate, cnss_amount, cnam_amount,
            gross_estimate, benefits_in_kind, Decimal('1.0'), is_expatriate
        )
        calculated_net = gross_estimate - cnss_amount - cnam_amount - its_amount - benefits_in_kind
        
        return {
            'gross_salary': gross_estimate.quantize(Decimal('0.01')),
            'net_salary': calculated_net.quantize(Decimal('0.01')),
            'cnss_employee': cnss_amount,
            'cnam_employee': cnam_amount,
            'its_total': its_amount,
            'benefits_in_kind': benefits_in_kind,
            'iterations': max_iterations,
            'converged': False
        }

    def _apply_benefits_in_kind_deduction(self, taxable_base: Decimal, benefits_in_kind: Decimal, base_salary: Decimal) -> Decimal:
        """
        Apply benefits in kind deduction logic:
        - If benefits > 20% of (base salary - benefits), deduct only 60%
        - Otherwise, deduct 100% from taxable income
        
        Args:
            taxable_base: Current taxable income base
            benefits_in_kind: Benefits in kind amount
            base_salary: Base salary amount
            
        Returns:
            Adjusted taxable base after benefits deduction
        """
        if benefits_in_kind <= 0 or base_salary <= 0:
            return taxable_base
            
        # Calculate adjusted base salary (excluding benefits)
        adjusted_base_salary = base_salary - benefits_in_kind
        
        if adjusted_base_salary <= 0:
            return taxable_base
            
        # Check if benefits exceed 20% threshold
        benefit_ratio = benefits_in_kind / adjusted_base_salary
        
        if benefit_ratio > Decimal('0.20'):
            # Benefits exceed 20% threshold, deduct only 60%
            deductible_benefits = benefits_in_kind * Decimal('0.60')
        else:
            # Benefits within 20% threshold, deduct 100%
            deductible_benefits = benefits_in_kind
            
        return taxable_base - deductible_benefits

    # ========== UTILITY METHODS FOR PAYROLL CALCULATION ==========
    
    def get_njt_record(self, employee, motif, period):
        """Get worked days record - placeholder for database query"""
        # This would be implemented with actual Django model queries
        return None
    
    def get_rubrique_paie_record(self, employee, rubric_id, motif, period):
        """Get payroll element record - placeholder for database query"""
        return None
    
    def get_used_rub_id(self, system_id):
        """Get system rubric mapping - placeholder"""
        return system_id
    
    def get_cumulative_amount_by_type(self, employee, type_code):
        """Get cumulative amounts by type - placeholder"""
        return Decimal('0.00')
    
    def get_cumulative_gross_12_months(self, employee, start_period, end_period):
        """Get 12-month cumulative gross - placeholder"""
        return Decimal('0.00')
    
    def get_fixed_monthly_gross_salary(self, employee, period):
        """Get fixed monthly gross salary - placeholder"""
        return Decimal('0.00')
    
    def get_housing_allowance_base(self, employee):
        """Get housing allowance base - placeholder"""
        return Decimal('0.00')
    
    def get_salary_increase(self, employee, period):
        """Get salary increase amount - placeholder"""
        return Decimal('0.00')


class PayrollValidationError(Exception):
    """Custom exception for payroll calculation validation errors"""
    pass


class PayrollValidator:
    """
    Comprehensive payroll calculation validation utilities
    Ensures data integrity and catches calculation errors
    """
    
    @staticmethod
    def validate_employee_data(employee) -> Dict[str, Union[bool, List[str]]]:
        """
        Validate employee data required for payroll calculations
        
        Args:
            employee: Employee instance
            
        Returns:
            Dict with validation status and any errors found
        """
        errors = []
        warnings = []
        
        # Required fields validation
        if not hasattr(employee, 'employee_id') or not employee.employee_id:
            errors.append("Employee ID is required")
            
        if not hasattr(employee, 'hire_date') or not employee.hire_date:
            errors.append("Hire date is required for seniority calculations")
            
        # Salary grade validation
        if hasattr(employee, 'salary_grade') and employee.salary_grade:
            if employee.salary_grade.base_salary <= 0:
                errors.append("Base salary must be greater than zero")
        else:
            warnings.append("No salary grade assigned - may affect calculations")
            
        # Contract hours validation
        if hasattr(employee, 'contract_hours_per_week'):
            if employee.contract_hours_per_week and employee.contract_hours_per_week > 80:
                warnings.append("Contract hours per week exceed 80 - verify correctness")
            elif employee.contract_hours_per_week and employee.contract_hours_per_week < 1:
                errors.append("Contract hours per week must be at least 1")
        
        return {
            'is_valid': len(errors) == 0,
            'errors': errors,
            'warnings': warnings
        }
    
    @staticmethod
    def validate_calculation_inputs(taxable_income: Decimal, cnss_amount: Decimal = None,
                                   cnam_amount: Decimal = None, benefits_in_kind: Decimal = None) -> bool:
        """
        Validate inputs for tax calculations
        
        Args:
            taxable_income: Gross taxable income
            cnss_amount: CNSS contribution amount
            cnam_amount: CNAM contribution amount
            benefits_in_kind: Benefits in kind amount
            
        Returns:
            True if inputs are valid, False otherwise
            
        Raises:
            PayrollValidationError: If validation fails
        """
        if taxable_income < 0:
            raise PayrollValidationError("Taxable income cannot be negative")
            
        if cnss_amount is not None and cnss_amount < 0:
            raise PayrollValidationError("CNSS amount cannot be negative")
            
        if cnam_amount is not None and cnam_amount < 0:
            raise PayrollValidationError("CNAM amount cannot be negative")
            
        if benefits_in_kind is not None and benefits_in_kind < 0:
            raise PayrollValidationError("Benefits in kind cannot be negative")
            
        return True
    
    @staticmethod
    def validate_calculation_results(calculation_result: Dict) -> Dict[str, Union[bool, List[str]]]:
        """
        Validate payroll calculation results for reasonableness
        
        Args:
            calculation_result: Dictionary with calculation results
            
        Returns:
            Dict with validation status and any issues found
        """
        issues = []
        warnings = []
        
        gross_taxable = calculation_result.get('gross_taxable', Decimal('0'))
        net_salary = calculation_result.get('net_salary', Decimal('0'))
        its_total = calculation_result.get('its_total', Decimal('0'))
        cnss_employee = calculation_result.get('cnss_employee', Decimal('0'))
        cnam_employee = calculation_result.get('cnam_employee', Decimal('0'))
        
        # Basic validation
        if gross_taxable < 0:
            issues.append("Gross taxable income is negative")
            
        if net_salary < 0:
            issues.append("Net salary is negative")
            
        # Reasonableness checks
        if gross_taxable > 0:
            total_deductions = its_total + cnss_employee + cnam_employee
            deduction_ratio = total_deductions / gross_taxable
            
            if deduction_ratio > Decimal('0.6'):  # 60%
                warnings.append(f"Total deductions ({deduction_ratio*100:.1f}%) exceed 60% of gross salary")
                
            if its_total > gross_taxable * Decimal('0.4'):  # 40%
                warnings.append("ITS amount exceeds 40% of gross salary - verify calculation")
                
            if cnss_employee > gross_taxable * Decimal('0.02'):  # 2%
                warnings.append("CNSS amount exceeds expected 1% rate - verify ceiling application")
                
            if cnam_employee > gross_taxable * Decimal('0.05'):  # 5%
                warnings.append("CNAM amount exceeds expected 4% rate")
        
        return {
            'is_valid': len(issues) == 0,
            'issues': issues,
            'warnings': warnings
        }


class PayrollCalculationSummary:
    """
    Generate comprehensive summaries of payroll calculations
    Useful for debugging and reporting
    """
    
    @staticmethod
    def generate_calculation_summary(employee, calculation_result: Dict) -> Dict[str, Union[str, Decimal, Dict]]:
        """
        Generate a comprehensive summary of payroll calculations
        
        Args:
            employee: Employee instance
            calculation_result: Calculation results dictionary
            
        Returns:
            Detailed summary dictionary
        """
        return {
            'employee_info': {
                'employee_id': getattr(employee, 'employee_id', 'N/A'),
                'name': f"{getattr(employee, 'first_name', '')} {getattr(employee, 'last_name', '')}".strip(),
                'is_expatriate': getattr(employee, 'is_expatriate', False),
                'salary_grade': str(getattr(employee, 'salary_grade', 'N/A')),
                'hire_date': getattr(employee, 'hire_date', None),
            },
            'calculation_breakdown': {
                'gross_taxable': calculation_result.get('gross_taxable', Decimal('0')),
                'gross_non_taxable': calculation_result.get('gross_non_taxable', Decimal('0')),
                'benefits_in_kind': calculation_result.get('benefits_in_kind', Decimal('0')),
                'total_gross': calculation_result.get('gross_taxable', Decimal('0')) + 
                             calculation_result.get('gross_non_taxable', Decimal('0')),
            },
            'deductions': {
                'cnss_employee': calculation_result.get('cnss_employee', Decimal('0')),
                'cnam_employee': calculation_result.get('cnam_employee', Decimal('0')),
                'its_tranche1': calculation_result.get('its_tranche1', Decimal('0')),
                'its_tranche2': calculation_result.get('its_tranche2', Decimal('0')),
                'its_tranche3': calculation_result.get('its_tranche3', Decimal('0')),
                'its_total': calculation_result.get('its_total', Decimal('0')),
                'total_deductions': (
                    calculation_result.get('cnss_employee', Decimal('0')) +
                    calculation_result.get('cnam_employee', Decimal('0')) +
                    calculation_result.get('its_total', Decimal('0'))
                ),
            },
            'employer_costs': {
                'employer_cnss': calculation_result.get('employer_cnss', Decimal('0')),
                'employer_cnam': calculation_result.get('employer_cnam', Decimal('0')),
                'total_employer_costs': (
                    calculation_result.get('employer_cnss', Decimal('0')) +
                    calculation_result.get('employer_cnam', Decimal('0'))
                ),
            },
            'final_amounts': {
                'net_salary': calculation_result.get('net_salary', Decimal('0')),
                'total_cost_to_employer': (
                    calculation_result.get('gross_taxable', Decimal('0')) +
                    calculation_result.get('gross_non_taxable', Decimal('0')) +
                    calculation_result.get('employer_cnss', Decimal('0')) +
                    calculation_result.get('employer_cnam', Decimal('0'))
                ),
            }
        }