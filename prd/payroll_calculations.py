# payroll_calculations.py
"""
Core payroll calculation functions converted from Java
Equivalent to FonctionsPaie.java and core methods from PaieClass.java
"""

from decimal import Decimal, ROUND_HALF_UP
from datetime import datetime, timedelta
from django.utils import timezone
from typing import Dict, List, Optional, Union


class PayrollCalculator:
    """Core payroll calculation engine converted from PaieClass.paieCalcule"""
    
    def __init__(self, system_parameters):
        self.system_parameters = system_parameters
    
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


class PayrollFunctions:
    """
    Payroll calculation functions F01-F24 converted from FonctionsPaie.java
    """
    
    @staticmethod
    def F01_NJT(employee, motif, period):
        """
        F01: Get worked days (Nombre de Jours Travaillés)
        Equivalent to F01_NJT in FonctionsPaie.java
        """
        try:
            from .models import WorkedDays  # Import here to avoid circular imports
            worked_days = WorkedDays.objects.get(
                employee=employee, 
                motif=motif, 
                period=period
            )
            return worked_days.worked_days
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
        daily_salary = PayrollFunctions.F02_sbJour(employee, motif, period, payroll_elements)
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
    """Overtime calculation utilities"""
    
    @staticmethod
    def calculate_overtime_rates(total_hours_worked, standard_hours=8):
        """
        Calculate overtime hours by rate brackets
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


class InstallmentCalculator:
    """
    Installment and loan calculation utilities
    Equivalent to installment logic in PaieClass.paieCalcule
    """
    
    @staticmethod
    def calculate_quota_cessible(net_before_installments, quota_percentage):
        """
        Calculate the maximum deductible amount based on quota cessible
        """
        return net_before_installments * quota_percentage / Decimal('100')
    
    @staticmethod
    def adjust_installments_for_quota(installments, quota_cessible):
        """
        Adjust installments when they exceed the quota cessible
        """
        total_installments = sum(installment['amount'] for installment in installments)
        
        if total_installments <= quota_cessible:
            return installments  # No adjustment needed
        
        # Calculate reduction ratio
        reduction_ratio = (total_installments - quota_cessible) / total_installments
        
        # Apply reduction to each installment
        adjusted_installments = []
        for installment in installments:
            original_amount = installment['amount']
            reduced_amount = original_amount - (original_amount * reduction_ratio)
            adjusted_installment = installment.copy()
            adjusted_installment['amount'] = max(Decimal('0.00'), reduced_amount)
            adjusted_installments.append(adjusted_installment)
        
        return adjusted_installments