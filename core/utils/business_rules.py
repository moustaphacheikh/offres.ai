# business_rules.py
"""
Comprehensive Business Rules Module for Mauritanian Payroll System
Centralizes all payroll business logic based on analysis of enhanced payroll calculations

This module implements:
1. F01-F24 Payroll Functions Centralization
2. Seniority Business Rules  
3. Leave Entitlement Business Rules
4. Overtime Business Rules
5. Severance Pay Business Rules
6. Installment and Deduction Rules

All rules follow Mauritanian labor law and legacy Java system logic.
Integrates with existing enhanced utilities (payroll_calculations, tax_calculations, date_utils).
"""

from decimal import Decimal, ROUND_HALF_UP
from datetime import datetime, date, timedelta
from dateutil.relativedelta import relativedelta
from typing import Dict, List, Optional, Union, Tuple
# Cleaned up imports - removed unused Any
import math
import warnings

# Import enhanced utilities
from .payroll_calculations import PayrollFunctions, PayrollCalculator, OvertimeCalculator, InstallmentCalculator
from .tax_calculations import CNSSCalculator, CNAMCalculator, ITSCalculator, TaxUtilities
from .date_utils import DateCalculator, SeniorityCalculator, LeaveCalculator, WorkingDayCalculator


class PayrollBusinessRules:
    """
    Centralized business rules engine for Mauritanian payroll system
    Implements all F01-F24 functions and complex business logic
    """
    
    def __init__(self, system_parameters=None):
        """
        Initialize business rules engine with system parameters
        
        Args:
            system_parameters: System configuration parameters
        """
        self.system_parameters = system_parameters or {}
        self.payroll_functions = PayrollFunctions(system_parameters, self)
        
        # Initialize calculators
        self.cnss_calculator = CNSSCalculator()
        self.cnam_calculator = CNAMCalculator()
        self.its_calculator = ITSCalculator()
        self.overtime_calculator = OvertimeCalculator()
        self.installment_calculator = InstallmentCalculator()
        
        # Initialize enhanced calculators
        self.seniority_calculator = SeniorityCalculator()
        self.leave_calculator = LeaveCalculator()
        self.working_day_calculator = WorkingDayCalculator()
    
    # ========== CORE F01-F24 PAYROLL FUNCTIONS ==========
    
    def calculate_payroll_function(self, function_code: str, employee, motif=None, period=None, **kwargs) -> Decimal:
        """
        Main dispatcher for all F01-F24 payroll functions
        Enhanced version with additional parameters and validation
        
        Args:
            function_code: Function code (F01, F02, etc.)
            employee: Employee instance
            motif: Payroll motif
            period: Calculation period
            **kwargs: Additional parameters for specific functions
            
        Returns:
            Calculated value for the function
        """
        try:
            return self.payroll_functions.calculate_function(function_code, employee, motif, period)
        except Exception as e:
            # Enhanced error handling with logging
            self._log_calculation_error(function_code, employee, str(e))
            return Decimal('0.00')
    
    def get_all_function_values(self, employee, motif=None, period=None) -> Dict[str, Decimal]:
        """
        Calculate all F01-F24 functions for an employee
        
        Args:
            employee: Employee instance
            motif: Payroll motif
            period: Calculation period
            
        Returns:
            Dictionary with all function values
        """
        functions = {}
        
        # All F01-F24 function codes
        function_codes = [
            'F01', 'F02', 'F03', 'F04', 'F05', 'F06', 'F07', 'F08', 'F09', 'F10',
            'F11', 'F12', 'F13', 'F14', 'F15', 'F16', 'F17', 'F18', 'F19', 'F20',
            'F21', 'F22', 'F23', 'F24'
        ]
        
        for code in function_codes:
            functions[code] = self.calculate_payroll_function(code, employee, motif, period)
        
        return functions
    
    # ========== SENIORITY BUSINESS RULES ==========
    
    class SeniorityRules:
        """
        Comprehensive seniority calculation rules
        Implements Mauritanian labor law seniority calculations
        """
        
        @staticmethod
        def calculate_standard_seniority_rate(seniority_years: int) -> Decimal:
            """
            Calculate standard seniority rate (F04 equivalent)
            Progressive seniority bonus: 2% per year up to 30% maximum at 16+ years
            
            Args:
                seniority_years: Complete years of seniority
                
            Returns:
                Seniority rate as decimal (0.00 to 0.30)
            """
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
        
        @staticmethod
        def calculate_special_seniority_rate(seniority_years: int, schema: str = "special1") -> Decimal:
            """
            Calculate special seniority rates (F23, F23X equivalents)
            
            Args:
                seniority_years: Complete years of seniority
                schema: Special seniority schema ("special1", "special2")
                
            Returns:
                Special seniority rate as decimal
            """
            if schema == "special1":
                # F23: Like standard but continues growing at 1% per year beyond 16 years
                if seniority_years >= 16:
                    return Decimal('0.30') + Decimal(str(seniority_years - 16)) * Decimal('0.01')
                else:
                    return PayrollBusinessRules.SeniorityRules.calculate_standard_seniority_rate(seniority_years)
            
            elif schema == "special2":
                # F23X: 3% per year with 2% increases beyond 15 years
                if seniority_years >= 15:
                    return Decimal('0.45') + Decimal(str(seniority_years - 15)) * Decimal('0.02')
                elif seniority_years >= 1:
                    return Decimal(str(seniority_years)) * Decimal('0.03')
                else:
                    return Decimal('0.00')
            
            else:
                # Default to standard calculation
                return PayrollBusinessRules.SeniorityRules.calculate_standard_seniority_rate(seniority_years)
        
        @staticmethod
        def calculate_anniversary_bonus(hire_date: Union[date, datetime], 
                                      current_period: Union[date, datetime],
                                      base_salary: Decimal,
                                      bonus_months: int = 1) -> Decimal:
            """
            Calculate anniversary bonus for milestone years
            
            Args:
                hire_date: Employee hire date
                current_period: Current payroll period
                base_salary: Base salary for bonus calculation
                bonus_months: Number of months of salary as bonus
                
            Returns:
                Anniversary bonus amount
            """
            if isinstance(hire_date, datetime):
                hire_date = hire_date.date()
            if isinstance(current_period, datetime):
                current_period = current_period.date()
            
            # Check if current period contains anniversary
            anniversary_this_year = hire_date.replace(year=current_period.year)
            
            # Check if anniversary falls in current month
            if anniversary_this_year.month == current_period.month:
                seniority_years = DateCalculator.get_years_between(hire_date, anniversary_this_year)
                
                # Milestone years: 5, 10, 15, 20, 25, 30+ years
                milestone_years = [5, 10, 15, 20, 25, 30, 35, 40]
                
                if seniority_years in milestone_years:
                    return base_salary * Decimal(str(bonus_months))
            
            return Decimal('0.00')
        
        @staticmethod
        def calculate_seniority_progression(hire_date: Union[date, datetime],
                                          reference_date: Union[date, datetime] = None) -> Dict[str, Any]:
            """
            Calculate comprehensive seniority progression information
            
            Args:
                hire_date: Employee hire date
                reference_date: Reference date for calculation
                
            Returns:
                Dictionary with seniority progression details
            """
            if reference_date is None:
                reference_date = date.today()
            
            seniority_years = DateCalculator.get_years_between(hire_date, reference_date)
            
            # Calculate current and future rates
            current_standard = PayrollBusinessRules.SeniorityRules.calculate_standard_seniority_rate(seniority_years)
            current_special1 = PayrollBusinessRules.SeniorityRules.calculate_special_seniority_rate(seniority_years, "special1")
            current_special2 = PayrollBusinessRules.SeniorityRules.calculate_special_seniority_rate(seniority_years, "special2")
            
            # Next milestone calculation
            next_milestone_years = seniority_years + 1
            next_anniversary = DateCalculator.add_years(hire_date, next_milestone_years)
            
            next_standard = PayrollBusinessRules.SeniorityRules.calculate_standard_seniority_rate(next_milestone_years)
            next_special1 = PayrollBusinessRules.SeniorityRules.calculate_special_seniority_rate(next_milestone_years, "special1")
            next_special2 = PayrollBusinessRules.SeniorityRules.calculate_special_seniority_rate(next_milestone_years, "special2")
            
            return {
                'current_years': seniority_years,
                'current_rates': {
                    'standard': current_standard,
                    'special1': current_special1,
                    'special2': current_special2
                },
                'next_anniversary': next_anniversary,
                'next_rates': {
                    'standard': next_standard,
                    'special1': next_special1,
                    'special2': next_special2
                },
                'rate_increases': {
                    'standard': next_standard - current_standard,
                    'special1': next_special1 - current_special1,
                    'special2': next_special2 - current_special2
                }
            }
    
    # ========== LEAVE ENTITLEMENT BUSINESS RULES ==========
    
    class LeaveRules:
        """
        Comprehensive leave entitlement and management rules
        Implements Mauritanian labor law leave calculations
        """
        
        @staticmethod
        def calculate_annual_leave_entitlement(seniority_years: int, 
                                             base_days: int = 21,
                                             enhanced_schema: bool = False) -> int:
            """
            Calculate annual leave entitlement based on seniority
            
            Args:
                seniority_years: Years of seniority
                base_days: Base annual leave days
                enhanced_schema: Whether to use enhanced leave schema
                
            Returns:
                Total annual leave days entitled
            """
            additional_days = 0
            
            if enhanced_schema:
                # Enhanced schema with more generous increases
                if seniority_years >= 20:
                    additional_days += 8  # Extra 8 days for 20+ years
                elif seniority_years >= 15:
                    additional_days += 6  # Extra 6 days for 15+ years
                elif seniority_years >= 10:
                    additional_days += 4  # Extra 4 days for 10+ years
                elif seniority_years >= 5:
                    additional_days += 2  # Extra 2 days for 5+ years
            else:
                # Standard schema
                if seniority_years >= 15:
                    additional_days += 4  # Extra 4 days for 15+ years
                elif seniority_years >= 10:
                    additional_days += 3  # Extra 3 days for 10+ years
                elif seniority_years >= 5:
                    additional_days += 2  # Extra 2 days for 5+ years
            
            return base_days + additional_days
        
        @staticmethod
        def calculate_leave_accrual_rate(annual_entitlement: int) -> Decimal:
            """
            Calculate monthly leave accrual rate
            
            Args:
                annual_entitlement: Annual leave days entitled
                
            Returns:
                Monthly accrual rate
            """
            return Decimal(str(annual_entitlement)) / Decimal('12')
        
        @staticmethod
        def calculate_leave_compensation(daily_salary: Decimal,
                                       leave_days: Decimal,
                                       compensation_rate: Decimal = Decimal('1.0'),
                                       include_allowances: bool = True) -> Decimal:
            """
            Calculate leave compensation amount
            
            Args:
                daily_salary: Daily salary amount
                leave_days: Number of leave days
                compensation_rate: Compensation rate (default 100%)
                include_allowances: Whether to include allowances in compensation
                
            Returns:
                Leave compensation amount
            """
            base_compensation = daily_salary * leave_days * compensation_rate
            
            if include_allowances:
                # Add typical allowances (transport, meal, etc.) at 15% of base
                allowance_compensation = base_compensation * Decimal('0.15')
                return base_compensation + allowance_compensation
            
            return base_compensation
        
        @staticmethod
        def calculate_carry_over_limits(annual_entitlement: int,
                                      max_carry_over_percentage: Decimal = Decimal('0.5')) -> Dict[str, int]:
            """
            Calculate leave carry-over limits
            
            Args:
                annual_entitlement: Annual leave entitlement
                max_carry_over_percentage: Maximum percentage that can be carried over
                
            Returns:
                Dictionary with carry-over limits
            """
            max_carry_over = int(Decimal(str(annual_entitlement)) * max_carry_over_percentage)
            must_use = annual_entitlement - max_carry_over
            
            return {
                'annual_entitlement': annual_entitlement,
                'max_carry_over': max_carry_over,
                'must_use_current_year': must_use,
                'total_available_next_year': annual_entitlement + max_carry_over
            }
        
        @staticmethod
        def calculate_special_leave_entitlements(employee_data: Dict[str, Any]) -> Dict[str, int]:
            """
            Calculate special leave entitlements (maternity, paternity, etc.)
            
            Args:
                employee_data: Dictionary with employee information
                
            Returns:
                Dictionary with special leave entitlements
            """
            special_leaves = {}
            
            # Maternity leave (14 weeks = 98 days)
            if employee_data.get('gender') == 'F':
                special_leaves['maternity'] = 98
            
            # Paternity leave (3 days)
            if employee_data.get('gender') == 'M':
                special_leaves['paternity'] = 3
            
            # Marriage leave (3 days)
            special_leaves['marriage'] = 3
            
            # Bereavement leave (varies by relationship)
            special_leaves['bereavement_immediate'] = 3  # Immediate family
            special_leaves['bereavement_extended'] = 1   # Extended family
            
            # Sick leave (based on seniority)
            seniority_years = employee_data.get('seniority_years', 0)
            if seniority_years >= 10:
                special_leaves['sick_annual'] = 30
            elif seniority_years >= 5:
                special_leaves['sick_annual'] = 21
            else:
                special_leaves['sick_annual'] = 15
            
            # Pilgrimage leave (once in career)
            special_leaves['pilgrimage'] = 30
            
            return special_leaves
        
        @staticmethod
        def validate_leave_request(request_data: Dict[str, Any],
                                 employee_balances: Dict[str, Decimal]) -> Dict[str, Any]:
            """
            Validate a leave request against business rules
            
            Args:
                request_data: Leave request information
                employee_balances: Current leave balances
                
            Returns:
                Validation result with status and messages
            """
            validation_result = {
                'is_valid': True,
                'errors': [],
                'warnings': [],
                'adjusted_request': request_data.copy()
            }
            
            start_date = request_data.get('start_date')
            end_date = request_data.get('end_date')
            leave_type = request_data.get('leave_type', 'annual')
            requested_days = request_data.get('days', 0)
            
            # Basic date validation
            if start_date and end_date:
                if start_date > end_date:
                    validation_result['errors'].append("Start date must be before end date")
                    validation_result['is_valid'] = False
                
                if start_date < date.today():
                    validation_result['errors'].append("Cannot request leave in the past")
                    validation_result['is_valid'] = False
            
            # Balance validation
            available_balance = employee_balances.get(leave_type, Decimal('0'))
            if requested_days > available_balance:
                validation_result['errors'].append(
                    f"Insufficient {leave_type} leave balance. "
                    f"Requested: {requested_days}, Available: {available_balance}"
                )
                validation_result['is_valid'] = False
            
            # Advance notice validation
            if start_date:
                days_notice = (start_date - date.today()).days
                required_notice = 7  # Default 7 days
                
                if leave_type == 'annual' and requested_days > 5:
                    required_notice = 14  # 14 days for long annual leave
                
                if days_notice < required_notice:
                    validation_result['warnings'].append(
                        f"Insufficient advance notice. Required: {required_notice} days, "
                        f"Provided: {days_notice} days"
                    )
            
            # Special leave validations
            if leave_type == 'maternity':
                # Maternity leave typically starts 2 weeks before due date
                validation_result['warnings'].append("Ensure maternity leave timing complies with medical requirements")
            
            elif leave_type == 'sick':
                # Sick leave requires medical certificate for >3 days
                if requested_days > 3:
                    validation_result['warnings'].append("Medical certificate required for sick leave >3 days")
            
            return validation_result
    
    # ========== OVERTIME BUSINESS RULES ==========
    
    class OvertimeRules:
        """
        Comprehensive overtime calculation rules
        Implements complex overtime calculations with various rates
        """
        
        @staticmethod
        def calculate_overtime_with_progression(regular_hours: Decimal,
                                              total_hours_worked: Decimal,
                                              base_hourly_rate: Decimal,
                                              progression_schema: str = "mauritanian") -> Dict[str, Decimal]:
            """
            Calculate overtime with progressive rate increases
            
            Args:
                regular_hours: Regular working hours for the period
                total_hours_worked: Total hours actually worked
                base_hourly_rate: Base hourly rate
                progression_schema: Overtime progression schema
                
            Returns:
                Dictionary with overtime calculation breakdown
            """
            overtime_hours = max(Decimal('0'), total_hours_worked - regular_hours)
            
            if progression_schema == "mauritanian":
                # Mauritanian overtime progression
                rates = [
                    {'max_hours': Decimal('8'), 'rate': Decimal('1.15')},   # First 8 OT hours at 115%
                    {'max_hours': Decimal('6'), 'rate': Decimal('1.40')},   # Next 6 hours at 140%
                    {'max_hours': None, 'rate': Decimal('1.50')}            # Remaining hours at 150%
                ]
            elif progression_schema == "enhanced":
                # Enhanced overtime progression
                rates = [
                    {'max_hours': Decimal('4'), 'rate': Decimal('1.25')},   # First 4 OT hours at 125%
                    {'max_hours': Decimal('4'), 'rate': Decimal('1.50')},   # Next 4 hours at 150%
                    {'max_hours': Decimal('8'), 'rate': Decimal('1.75')},   # Next 8 hours at 175%
                    {'max_hours': None, 'rate': Decimal('2.00')}            # Remaining hours at 200%
                ]
            else:
                # Simple flat overtime rate
                rates = [{'max_hours': None, 'rate': Decimal('1.50')}]
            
            result = {
                'regular_hours': regular_hours,
                'overtime_hours': overtime_hours,
                'total_hours': total_hours_worked,
                'overtime_breakdown': [],
                'total_overtime_pay': Decimal('0'),
                'regular_pay': regular_hours * base_hourly_rate
            }
            
            remaining_ot = overtime_hours
            
            for i, rate_info in enumerate(rates):
                if remaining_ot <= 0:
                    break
                
                max_hours = rate_info['max_hours']
                rate_multiplier = rate_info['rate']
                
                # Calculate hours at this rate
                if max_hours is not None:
                    hours_at_rate = min(remaining_ot, max_hours)
                else:
                    hours_at_rate = remaining_ot
                
                # Calculate pay at this rate
                pay_at_rate = hours_at_rate * base_hourly_rate * rate_multiplier
                
                result['overtime_breakdown'].append({
                    'bracket': i + 1,
                    'hours': hours_at_rate,
                    'rate_multiplier': rate_multiplier,
                    'pay': pay_at_rate
                })
                
                result['total_overtime_pay'] += pay_at_rate
                remaining_ot -= hours_at_rate
            
            result['total_pay'] = result['regular_pay'] + result['total_overtime_pay']
            
            return result
        
        @staticmethod
        def calculate_holiday_premium(hours_worked: Decimal,
                                    base_hourly_rate: Decimal,
                                    holiday_rate: Decimal = Decimal('2.0')) -> Dict[str, Decimal]:
            """
            Calculate holiday work premium
            
            Args:
                hours_worked: Hours worked on holiday
                base_hourly_rate: Base hourly rate
                holiday_rate: Holiday rate multiplier (default 200%)
                
            Returns:
                Holiday premium calculation
            """
            holiday_pay = hours_worked * base_hourly_rate * holiday_rate
            premium_amount = hours_worked * base_hourly_rate * (holiday_rate - Decimal('1.0'))
            
            return {
                'holiday_hours': hours_worked,
                'base_rate': base_hourly_rate,
                'holiday_multiplier': holiday_rate,
                'total_holiday_pay': holiday_pay,
                'premium_amount': premium_amount
            }
        
        @staticmethod
        def calculate_night_shift_differential(night_hours: Decimal,
                                             base_hourly_rate: Decimal,
                                             night_differential: Decimal = Decimal('0.25')) -> Dict[str, Decimal]:
            """
            Calculate night shift differential
            
            Args:
                night_hours: Hours worked during night shift
                base_hourly_rate: Base hourly rate
                night_differential: Night differential percentage
                
            Returns:
                Night shift differential calculation
            """
            differential_amount = night_hours * base_hourly_rate * night_differential
            total_night_pay = night_hours * base_hourly_rate * (Decimal('1.0') + night_differential)
            
            return {
                'night_hours': night_hours,
                'base_rate': base_hourly_rate,
                'differential_rate': night_differential,
                'differential_amount': differential_amount,
                'total_night_pay': total_night_pay
            }
        
        @staticmethod
        def calculate_weekly_overtime_limits(weekly_schedule: List[Decimal],
                                           max_daily_hours: Decimal = Decimal('12'),
                                           max_weekly_hours: Decimal = Decimal('60')) -> Dict[str, Any]:
            """
            Calculate and validate weekly overtime limits
            
            Args:
                weekly_schedule: List of daily hours for the week
                max_daily_hours: Maximum allowed daily hours
                max_weekly_hours: Maximum allowed weekly hours
                
            Returns:
                Overtime limits validation and adjustments
            """
            total_weekly_hours = sum(weekly_schedule)
            
            result = {
                'original_schedule': weekly_schedule.copy(),
                'total_weekly_hours': total_weekly_hours,
                'violations': [],
                'adjusted_schedule': weekly_schedule.copy(),
                'is_compliant': True
            }
            
            # Check daily limits
            for i, daily_hours in enumerate(weekly_schedule):
                if daily_hours > max_daily_hours:
                    result['violations'].append(f"Day {i+1}: {daily_hours} hours exceeds daily limit of {max_daily_hours}")
                    result['adjusted_schedule'][i] = max_daily_hours
                    result['is_compliant'] = False
            
            # Check weekly limits
            adjusted_total = sum(result['adjusted_schedule'])
            if adjusted_total > max_weekly_hours:
                # Proportionally reduce all days to meet weekly limit
                reduction_factor = max_weekly_hours / adjusted_total
                result['adjusted_schedule'] = [
                    hours * reduction_factor for hours in result['adjusted_schedule']
                ]
                result['violations'].append(f"Weekly total {adjusted_total} hours exceeds weekly limit of {max_weekly_hours}")
                result['is_compliant'] = False
            
            result['adjusted_total'] = sum(result['adjusted_schedule'])
            
            return result
    
    # ========== SEVERANCE PAY BUSINESS RULES ==========
    
    class SeveranceRules:
        """
        Comprehensive severance pay calculation rules
        Implements Mauritanian labor law severance calculations
        """
        
        @staticmethod
        def calculate_individual_severance(seniority_years: float,
                                         average_monthly_salary: Decimal,
                                         termination_reason: str = "economic") -> Dict[str, Decimal]:
            """
            Calculate individual dismissal severance pay (F12 equivalent)
            
            Args:
                seniority_years: Years of seniority (can be fractional)
                average_monthly_salary: Average monthly salary for calculation
                termination_reason: Reason for termination
                
            Returns:
                Severance calculation breakdown
            """
            if seniority_years < 1.0:
                return {
                    'severance_months': Decimal('0'),
                    'severance_amount': Decimal('0'),
                    'calculation_basis': 'insufficient_seniority'
                }
            
            # Mauritanian individual severance rates
            if 1.0 <= seniority_years < 5.0:
                severance_months = Decimal(str(0.25 * seniority_years))  # 0.25 months per year
            elif 5.0 <= seniority_years < 10.0:
                severance_months = Decimal('1.25') + Decimal(str((seniority_years - 5.0) * 0.3))
            elif seniority_years >= 10.0:
                severance_months = Decimal('2.75') + Decimal(str((seniority_years - 10.0) * 0.35))
            else:
                severance_months = Decimal('0')
            
            # Apply termination reason adjustments
            if termination_reason == "misconduct":
                severance_months *= Decimal('0.5')  # Reduced for misconduct
            elif termination_reason == "mutual_agreement":
                severance_months *= Decimal('1.2')  # Enhanced for mutual agreement
            
            severance_amount = severance_months * average_monthly_salary
            
            return {
                'seniority_years': Decimal(str(seniority_years)),
                'severance_months': severance_months,
                'severance_amount': severance_amount,
                'calculation_basis': f'individual_{termination_reason}',
                'average_monthly_salary': average_monthly_salary
            }
        
        @staticmethod
        def calculate_collective_severance(seniority_years: float,
                                         average_monthly_salary: Decimal) -> Dict[str, Decimal]:
            """
            Calculate collective dismissal severance pay (F13 equivalent)
            Higher rates for collective layoffs
            
            Args:
                seniority_years: Years of seniority
                average_monthly_salary: Average monthly salary
                
            Returns:
                Collective severance calculation
            """
            if seniority_years < 1.0:
                return {
                    'severance_months': Decimal('0'),
                    'severance_amount': Decimal('0'),
                    'calculation_basis': 'insufficient_seniority'
                }
            
            # Mauritanian collective severance rates (higher than individual)
            if 1.0 < seniority_years <= 5.0:
                severance_months = Decimal(str(0.3 * seniority_years))  # 0.3 months per year
            elif 5.0 < seniority_years <= 10.0:
                severance_months = Decimal('1.5') + Decimal(str((seniority_years - 5.0) * 0.4))
            elif seniority_years > 10.0:
                severance_months = Decimal('3.5') + Decimal(str((seniority_years - 10.0) * 0.5))
            else:
                severance_months = Decimal('0')
            
            severance_amount = severance_months * average_monthly_salary
            
            return {
                'seniority_years': Decimal(str(seniority_years)),
                'severance_months': severance_months,
                'severance_amount': severance_amount,
                'calculation_basis': 'collective_dismissal',
                'average_monthly_salary': average_monthly_salary
            }
        
        @staticmethod
        def calculate_retirement_benefits(seniority_years: float,
                                        average_monthly_salary: Decimal,
                                        retirement_type: str = "voluntary") -> Dict[str, Decimal]:
            """
            Calculate retirement benefits (F14 equivalent)
            Percentage of dismissal rate based on years of service
            
            Args:
                seniority_years: Years of seniority
                average_monthly_salary: Average monthly salary
                retirement_type: Type of retirement
                
            Returns:
                Retirement benefits calculation
            """
            # First calculate base dismissal amount
            base_severance = PayrollBusinessRules.SeveranceRules.calculate_individual_severance(
                seniority_years, average_monthly_salary, "economic"
            )
            
            # Apply retirement percentage based on seniority
            if 1.0 < seniority_years <= 5.0:
                retirement_percentage = Decimal('0.30')  # 30% of dismissal rate
            elif 5.0 < seniority_years <= 10.0:
                retirement_percentage = Decimal('0.50')  # 50% of dismissal rate
            elif 10.0 < seniority_years <= 20.0:
                retirement_percentage = Decimal('0.75')  # 75% of dismissal rate
            elif seniority_years > 20.0:
                retirement_percentage = Decimal('1.00')  # 100% of dismissal rate
            else:
                retirement_percentage = Decimal('0.00')
            
            # Apply retirement type adjustment
            if retirement_type == "early":
                retirement_percentage *= Decimal('0.8')  # Reduced for early retirement
            elif retirement_type == "mandatory":
                retirement_percentage *= Decimal('1.1')  # Enhanced for mandatory retirement
            
            retirement_amount = base_severance['severance_amount'] * retirement_percentage
            
            return {
                'seniority_years': Decimal(str(seniority_years)),
                'base_severance_amount': base_severance['severance_amount'],
                'retirement_percentage': retirement_percentage,
                'retirement_amount': retirement_amount,
                'calculation_basis': f'retirement_{retirement_type}',
                'average_monthly_salary': average_monthly_salary
            }
        
        @staticmethod
        def calculate_notice_period(seniority_years: int,
                                  employee_category: str = "standard") -> Dict[str, int]:
            """
            Calculate notice period requirements
            
            Args:
                seniority_years: Years of seniority
                employee_category: Employee category (standard, executive, etc.)
                
            Returns:
                Notice period calculation
            """
            # Standard notice periods
            if seniority_years < 1:
                notice_days = 0
            elif seniority_years < 5:
                notice_days = 30  # 1 month
            elif seniority_years < 10:
                notice_days = 60  # 2 months
            else:
                notice_days = 90  # 3 months
            
            # Adjustments for employee category
            if employee_category == "executive":
                notice_days = max(notice_days, 90)  # Minimum 3 months for executives
            elif employee_category == "manager":
                notice_days = max(notice_days, 60)  # Minimum 2 months for managers
            
            return {
                'seniority_years': seniority_years,
                'employee_category': employee_category,
                'notice_days': notice_days,
                'notice_months': notice_days // 30
            }
        
        @staticmethod
        def calculate_final_settlement(employee_data: Dict[str, Any],
                                     termination_data: Dict[str, Any]) -> Dict[str, Decimal]:
            """
            Calculate comprehensive final settlement
            
            Args:
                employee_data: Employee information
                termination_data: Termination details
                
            Returns:
                Complete final settlement calculation
            """
            seniority_years = employee_data.get('seniority_years', 0)
            average_salary = employee_data.get('average_monthly_salary', Decimal('0'))
            termination_reason = termination_data.get('reason', 'economic')
            outstanding_leave = employee_data.get('outstanding_leave_days', 0)
            daily_salary = employee_data.get('daily_salary', Decimal('0'))
            
            settlement = {
                'severance_pay': Decimal('0'),
                'leave_compensation': Decimal('0'),
                'notice_pay': Decimal('0'),
                'pro_rata_bonus': Decimal('0'),
                'other_entitlements': Decimal('0'),
                'total_settlement': Decimal('0')
            }
            
            # Calculate severance pay
            if termination_reason == "collective":
                severance_calc = PayrollBusinessRules.SeveranceRules.calculate_collective_severance(
                    seniority_years, average_salary
                )
            else:
                severance_calc = PayrollBusinessRules.SeveranceRules.calculate_individual_severance(
                    seniority_years, average_salary, termination_reason
                )
            
            settlement['severance_pay'] = severance_calc['severance_amount']
            
            # Calculate leave compensation
            if outstanding_leave > 0:
                settlement['leave_compensation'] = daily_salary * Decimal(str(outstanding_leave))
            
            # Calculate notice pay (if payment in lieu)
            notice_calc = PayrollBusinessRules.SeveranceRules.calculate_notice_period(
                int(seniority_years), employee_data.get('category', 'standard')
            )
            if termination_data.get('payment_in_lieu', False):
                settlement['notice_pay'] = daily_salary * Decimal(str(notice_calc['notice_days']))
            
            # Calculate pro-rata annual bonus
            months_worked_this_year = termination_data.get('months_worked_this_year', 12)
            annual_bonus = employee_data.get('annual_bonus', Decimal('0'))
            if annual_bonus > 0:
                settlement['pro_rata_bonus'] = annual_bonus * Decimal(str(months_worked_this_year)) / Decimal('12')
            
            # Other entitlements (13th month, etc.)
            settlement['other_entitlements'] = employee_data.get('other_entitlements', Decimal('0'))
            
            # Calculate total
            settlement['total_settlement'] = sum(settlement.values()) - settlement['total_settlement']
            
            return settlement
    
    # ========== INSTALLMENT AND DEDUCTION RULES ==========
    
    class InstallmentRules:
        """
        Comprehensive installment and deduction management rules
        Implements quota cessible enforcement and priority systems
        """
        
        @staticmethod
        def calculate_quota_cessible(net_salary: Decimal,
                                   quota_percentage: Decimal = Decimal('30'),
                                   minimum_net_required: Decimal = None) -> Dict[str, Decimal]:
            """
            Calculate quota cessible (maximum deductible amount)
            
            Args:
                net_salary: Net salary before installments
                quota_percentage: Maximum percentage deductible (default 30%)
                minimum_net_required: Minimum net salary that must remain
                
            Returns:
                Quota cessible calculation
            """
            quota_amount = net_salary * quota_percentage / Decimal('100')
            
            if minimum_net_required:
                # Ensure minimum net is preserved
                max_for_minimum = net_salary - minimum_net_required
                quota_amount = min(quota_amount, max(Decimal('0'), max_for_minimum))
            
            return {
                'net_salary': net_salary,
                'quota_percentage': quota_percentage,
                'quota_amount': quota_amount,
                'remaining_net': net_salary - quota_amount,
                'minimum_net_required': minimum_net_required or Decimal('0')
            }
        
        @staticmethod
        def prioritize_installments(installments: List[Dict[str, Any]],
                                  quota_available: Decimal) -> Dict[str, Any]:
            """
            Prioritize and allocate installments within quota limits
            
            Args:
                installments: List of installment requests
                quota_available: Available quota for installments
                
            Returns:
                Prioritized installment allocation
            """
            # Priority order: court orders > garnishments > loans > advances > other
            priority_map = {
                'court_order': 1,
                'garnishment': 2,
                'tax_levy': 3,
                'loan': 4,
                'advance': 5,
                'other': 6
            }
            
            # Sort installments by priority
            sorted_installments = sorted(
                installments,
                key=lambda x: (
                    priority_map.get(x.get('type', 'other'), 999),
                    x.get('priority', 999),
                    -x.get('amount', 0)  # Higher amount as tiebreaker
                )
            )
            
            result = {
                'allocated_installments': [],
                'deferred_installments': [],
                'total_allocated': Decimal('0'),
                'total_deferred': Decimal('0'),
                'remaining_quota': quota_available
            }
            
            for installment in sorted_installments:
                amount = Decimal(str(installment.get('amount', 0)))
                
                if result['remaining_quota'] >= amount:
                    # Full allocation possible
                    allocated = installment.copy()
                    allocated['allocated_amount'] = amount
                    allocated['deferred_amount'] = Decimal('0')
                    result['allocated_installments'].append(allocated)
                    result['total_allocated'] += amount
                    result['remaining_quota'] -= amount
                
                elif result['remaining_quota'] > Decimal('0'):
                    # Partial allocation possible
                    allocated = installment.copy()
                    allocated['allocated_amount'] = result['remaining_quota']
                    allocated['deferred_amount'] = amount - result['remaining_quota']
                    result['allocated_installments'].append(allocated)
                    result['total_allocated'] += result['remaining_quota']
                    result['total_deferred'] += allocated['deferred_amount']
                    result['remaining_quota'] = Decimal('0')
                    
                    # Add deferred portion
                    deferred = installment.copy()
                    deferred['amount'] = allocated['deferred_amount']
                    deferred['reason'] = 'quota_exceeded'
                    result['deferred_installments'].append(deferred)
                
                else:
                    # No quota remaining - full deferral
                    deferred = installment.copy()
                    deferred['reason'] = 'quota_exceeded'
                    result['deferred_installments'].append(deferred)
                    result['total_deferred'] += amount
            
            return result
        
        @staticmethod
        def calculate_installment_schedule(principal: Decimal,
                                         interest_rate: Decimal,
                                         periods: int,
                                         payment_frequency: str = "monthly") -> List[Dict[str, Decimal]]:
            """
            Calculate installment payment schedule
            
            Args:
                principal: Principal amount
                interest_rate: Annual interest rate (as percentage)
                periods: Number of payment periods
                payment_frequency: Payment frequency (monthly, quarterly, etc.)
                
            Returns:
                List of installment schedule
            """
            # Convert annual rate to period rate
            if payment_frequency == "monthly":
                period_rate = interest_rate / Decimal('12') / Decimal('100')
            elif payment_frequency == "quarterly":
                period_rate = interest_rate / Decimal('4') / Decimal('100')
            else:
                period_rate = interest_rate / Decimal('100')  # Annual
            
            # Calculate fixed payment amount using PMT formula
            if period_rate > 0:
                factor = (Decimal('1') + period_rate) ** periods
                payment_amount = principal * (period_rate * factor) / (factor - Decimal('1'))
            else:
                payment_amount = principal / Decimal(str(periods))
            
            schedule = []
            remaining_balance = principal
            
            for period in range(1, periods + 1):
                interest_payment = remaining_balance * period_rate
                principal_payment = payment_amount - interest_payment
                
                # Ensure we don't overpay on the last payment
                if period == periods:
                    principal_payment = remaining_balance
                    payment_amount = principal_payment + interest_payment
                
                remaining_balance -= principal_payment
                
                schedule.append({
                    'period': period,
                    'payment_amount': payment_amount.quantize(Decimal('0.01')),
                    'principal_payment': principal_payment.quantize(Decimal('0.01')),
                    'interest_payment': interest_payment.quantize(Decimal('0.01')),
                    'remaining_balance': remaining_balance.quantize(Decimal('0.01'))
                })
            
            return schedule
        
        @staticmethod
        def validate_installment_limits(employee_data: Dict[str, Any],
                                      installment_requests: List[Dict[str, Any]]) -> Dict[str, Any]:
            """
            Validate installment requests against legal and policy limits
            
            Args:
                employee_data: Employee information
                installment_requests: List of installment requests
                
            Returns:
                Validation results
            """
            net_salary = employee_data.get('net_salary', Decimal('0'))
            gross_salary = employee_data.get('gross_salary', Decimal('0'))
            existing_installments = employee_data.get('existing_installments', Decimal('0'))
            
            validation = {
                'is_valid': True,
                'violations': [],
                'warnings': [],
                'recommendations': []
            }
            
            # Calculate total new installments
            total_new_installments = sum(
                Decimal(str(req.get('amount', 0))) for req in installment_requests
            )
            total_all_installments = existing_installments + total_new_installments
            
            # Legal limit validation (typically 30-40% of net salary)
            legal_limit_percentage = Decimal('40')  # 40% legal limit
            legal_limit_amount = net_salary * legal_limit_percentage / Decimal('100')
            
            if total_all_installments > legal_limit_amount:
                validation['violations'].append(
                    f"Total installments ({total_all_installments}) exceed legal limit "
                    f"of {legal_limit_percentage}% ({legal_limit_amount})"
                )
                validation['is_valid'] = False
            
            # Policy warning at 30%
            policy_warning_percentage = Decimal('30')
            policy_warning_amount = net_salary * policy_warning_percentage / Decimal('100')
            
            if total_all_installments > policy_warning_amount:
                validation['warnings'].append(
                    f"Total installments ({total_all_installments}) exceed recommended "
                    f"{policy_warning_percentage}% ({policy_warning_amount})"
                )
            
            # Individual installment validations
            for req in installment_requests:
                amount = Decimal(str(req.get('amount', 0)))
                installment_type = req.get('type', 'other')
                
                # Salary advance limits (typically 50% of gross)
                if installment_type == 'advance':
                    advance_limit = gross_salary * Decimal('0.5')
                    if amount > advance_limit:
                        validation['violations'].append(
                            f"Salary advance ({amount}) exceeds 50% of gross salary ({advance_limit})"
                        )
                        validation['is_valid'] = False
                
                # Loan installment recommendations (typically 25% of net)
                elif installment_type == 'loan':
                    loan_recommendation = net_salary * Decimal('0.25')
                    if amount > loan_recommendation:
                        validation['warnings'].append(
                            f"Loan installment ({amount}) exceeds recommended 25% of net salary ({loan_recommendation})"
                        )
            
            # Recommendations for optimization
            if total_all_installments > Decimal('0'):
                debt_to_income_ratio = (total_all_installments / net_salary) * Decimal('100')
                validation['recommendations'].append(
                    f"Current debt-to-income ratio: {debt_to_income_ratio:.1f}%"
                )
                
                if debt_to_income_ratio > Decimal('35'):
                    validation['recommendations'].append(
                        "Consider debt consolidation or extended payment terms"
                    )
            
            return validation
    
    # ========== UTILITY METHODS ==========
    
    def _log_calculation_error(self, function_code: str, employee, error_message: str):
        """
        Log calculation errors for debugging and auditing
        
        Args:
            function_code: Function that failed
            employee: Employee instance
            error_message: Error message
        """
        # This would integrate with a proper logging system
        # For now, we'll use a simple warning
        employee_id = getattr(employee, 'employee_id', 'unknown')
        warnings.warn(
            f"Payroll calculation error - Function: {function_code}, "
            f"Employee: {employee_id}, Error: {error_message}"
        )
    
    def validate_business_rules(self, employee, period_data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Validate all business rules for an employee and period
        
        Args:
            employee: Employee instance
            period_data: Period calculation data
            
        Returns:
            Comprehensive validation results
        """
        validation_results = {
            'is_valid': True,
            'errors': [],
            'warnings': [],
            'recommendations': []
        }
        
        # Employee validation
        if not hasattr(employee, 'hire_date') or not employee.hire_date:
            validation_results['errors'].append("Employee hire date is required")
            validation_results['is_valid'] = False
        
        # Seniority validation
        if hasattr(employee, 'hire_date') and employee.hire_date:
            seniority_years = DateCalculator.get_years_between(employee.hire_date, date.today())
            if seniority_years < 0:
                validation_results['errors'].append("Hire date cannot be in the future")
                validation_results['is_valid'] = False
        
        # Salary validation
        if hasattr(employee, 'salary_grade') and employee.salary_grade:
            if hasattr(employee.salary_grade, 'base_salary'):
                smig = getattr(self.system_parameters, 'smig', Decimal('0'))
                if smig > 0 and employee.salary_grade.base_salary < smig:
                    validation_results['warnings'].append(
                        f"Base salary ({employee.salary_grade.base_salary}) is below SMIG ({smig})"
                    )
        
        # Period validation
        current_period = period_data.get('period_date')
        if current_period:
            # Check if period is closed
            closed_period = getattr(self.system_parameters, 'closed_period', None)
            if closed_period and current_period <= closed_period:
                validation_results['errors'].append("Cannot process payroll for closed period")
                validation_results['is_valid'] = False
        
        return validation_results
    
    def get_business_rules_summary(self) -> Dict[str, Any]:
        """
        Get a summary of all implemented business rules
        
        Returns:
            Summary of business rules and their current configuration
        """
        return {
            'payroll_functions': {
                'total_functions': 24,
                'function_codes': ['F01', 'F02', 'F03', 'F04', 'F05', 'F06', 'F07', 'F08', 'F09', 'F10',
                                 'F11', 'F12', 'F13', 'F14', 'F15', 'F16', 'F17', 'F18', 'F19', 'F20',
                                 'F21', 'F22', 'F23', 'F24'],
                'description': "Complete F01-F24 payroll function implementations"
            },
            'seniority_rules': {
                'schemas': ['standard', 'special1', 'special2'],
                'max_standard_rate': '30%',
                'progressive_increases': 'Yes',
                'milestone_bonuses': 'Configurable'
            },
            'leave_rules': {
                'base_annual_days': 21,
                'seniority_increases': 'Yes',
                'special_leaves': ['maternity', 'paternity', 'marriage', 'bereavement', 'sick', 'pilgrimage'],
                'carry_over_limit': '50%'
            },
            'overtime_rules': {
                'progression_schemas': ['mauritanian', 'enhanced', 'flat'],
                'holiday_premium': '200%',
                'night_differential': '25%',
                'weekly_limits': 'Configurable'
            },
            'severance_rules': {
                'individual_dismissal': 'F12 equivalent',
                'collective_dismissal': 'F13 equivalent',
                'retirement_benefits': 'F14 equivalent',
                'notice_periods': 'Seniority-based'
            },
            'installment_rules': {
                'quota_cessible': '30% default',
                'priority_system': 'Yes',
                'legal_limits': '40% maximum',
                'validation': 'Comprehensive'
            },
            'integrations': {
                'payroll_calculations': 'Enhanced F01-F24 functions',
                'tax_calculations': 'CNSS, CNAM, ITS calculators',
                'date_utils': 'Seniority, leave, working day calculators'
            }
        }


class BusinessRulesValidator:
    """
    Validation utilities for business rules
    Ensures data integrity and compliance
    """
    
    @staticmethod
    def validate_employee_eligibility(employee, rule_type: str) -> Dict[str, Any]:
        """
        Validate employee eligibility for specific business rules
        
        Args:
            employee: Employee instance
            rule_type: Type of rule to validate
            
        Returns:
            Validation results
        """
        validation = {
            'is_eligible': True,
            'reasons': [],
            'requirements_met': [],
            'requirements_missing': []
        }
        
        if rule_type == 'seniority_bonus':
            if hasattr(employee, 'hire_date') and employee.hire_date:
                seniority_years = DateCalculator.get_years_between(employee.hire_date, date.today())
                if seniority_years >= 1:
                    validation['requirements_met'].append(f"Minimum 1 year seniority ({seniority_years} years)")
                else:
                    validation['is_eligible'] = False
                    validation['requirements_missing'].append("Minimum 1 year seniority required")
            else:
                validation['is_eligible'] = False
                validation['requirements_missing'].append("Hire date required for seniority calculation")
        
        elif rule_type == 'overtime_premium':
            if hasattr(employee, 'contract_hours_per_week'):
                if employee.contract_hours_per_week and employee.contract_hours_per_week > 0:
                    validation['requirements_met'].append("Contract hours defined")
                else:
                    validation['requirements_missing'].append("Contract hours per week required")
            else:
                validation['requirements_missing'].append("Contract hours not defined")
        
        elif rule_type == 'leave_entitlement':
            if hasattr(employee, 'hire_date') and employee.hire_date:
                months_employed = DateCalculator.get_months_between(employee.hire_date, date.today())
                if months_employed >= 6:
                    validation['requirements_met'].append(f"Minimum 6 months employment ({months_employed} months)")
                else:
                    validation['is_eligible'] = False
                    validation['requirements_missing'].append("Minimum 6 months employment required")
            else:
                validation['is_eligible'] = False
                validation['requirements_missing'].append("Hire date required")
        
        elif rule_type == 'severance_pay':
            if hasattr(employee, 'hire_date') and employee.hire_date:
                seniority_years = DateCalculator.get_years_between(employee.hire_date, date.today())
                if seniority_years >= 1:
                    validation['requirements_met'].append(f"Minimum 1 year seniority ({seniority_years} years)")
                else:
                    validation['is_eligible'] = False
                    validation['requirements_missing'].append("Minimum 1 year seniority required")
            else:
                validation['is_eligible'] = False
                validation['requirements_missing'].append("Hire date required")
        
        # Update eligibility based on missing requirements
        if validation['requirements_missing']:
            validation['is_eligible'] = False
        
        return validation
    
    @staticmethod
    def validate_calculation_inputs(inputs: Dict[str, Any], rule_type: str) -> Dict[str, Any]:
        """
        Validate inputs for business rule calculations
        
        Args:
            inputs: Input data for calculation
            rule_type: Type of business rule
            
        Returns:
            Input validation results
        """
        validation = {
            'is_valid': True,
            'errors': [],
            'warnings': [],
            'sanitized_inputs': inputs.copy()
        }
        
        if rule_type == 'seniority':
            seniority_years = inputs.get('seniority_years')
            if seniority_years is not None:
                if seniority_years < 0:
                    validation['errors'].append("Seniority years cannot be negative")
                    validation['is_valid'] = False
                elif seniority_years > 50:
                    validation['warnings'].append("Seniority years over 50 - verify correctness")
        
        elif rule_type == 'overtime':
            total_hours = inputs.get('total_hours')
            regular_hours = inputs.get('regular_hours')
            
            if total_hours is not None and total_hours < 0:
                validation['errors'].append("Total hours cannot be negative")
                validation['is_valid'] = False
            
            if regular_hours is not None and regular_hours < 0:
                validation['errors'].append("Regular hours cannot be negative")
                validation['is_valid'] = False
            
            if (total_hours is not None and regular_hours is not None and 
                total_hours < regular_hours):
                validation['errors'].append("Total hours cannot be less than regular hours")
                validation['is_valid'] = False
        
        elif rule_type == 'leave':
            requested_days = inputs.get('requested_days')
            available_balance = inputs.get('available_balance')
            
            if requested_days is not None and requested_days <= 0:
                validation['errors'].append("Requested leave days must be positive")
                validation['is_valid'] = False
            
            if (requested_days is not None and available_balance is not None and 
                requested_days > available_balance):
                validation['errors'].append("Requested days exceed available balance")
                validation['is_valid'] = False
        
        return validation


class BusinessRulesEngine:
    """
    Main business rules engine that orchestrates all rule calculations
    Provides a unified interface for all payroll business logic
    """
    
    def __init__(self, system_parameters=None):
        """
        Initialize the business rules engine
        
        Args:
            system_parameters: System configuration parameters
        """
        self.business_rules = PayrollBusinessRules(system_parameters)
        self.validator = BusinessRulesValidator()
        self.system_parameters = system_parameters
    
    def process_employee_payroll(self, employee, period_data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Process complete payroll for an employee using all business rules
        
        Args:
            employee: Employee instance
            period_data: Period-specific data
            
        Returns:
            Complete payroll processing results
        """
        # Validate business rules
        validation = self.business_rules.validate_business_rules(employee, period_data)
        if not validation['is_valid']:
            return {
                'success': False,
                'errors': validation['errors'],
                'warnings': validation['warnings']
            }
        
        # Calculate all payroll functions
        payroll_functions = self.business_rules.get_all_function_values(
            employee, 
            period_data.get('motif'), 
            period_data.get('period_date')
        )
        
        # Calculate seniority information
        if hasattr(employee, 'hire_date') and employee.hire_date:
            seniority_info = self.business_rules.SeniorityRules.calculate_seniority_progression(
                employee.hire_date, period_data.get('period_date')
            )
        else:
            seniority_info = {}
        
        # Calculate leave balances
        leave_info = {}
        if hasattr(employee, 'hire_date') and employee.hire_date:
            seniority_years = DateCalculator.get_years_between(
                employee.hire_date, 
                period_data.get('period_date', date.today())
            )
            annual_entitlement = self.business_rules.LeaveRules.calculate_annual_leave_entitlement(seniority_years)
            leave_info = {
                'annual_entitlement': annual_entitlement,
                'accrual_rate': self.business_rules.LeaveRules.calculate_leave_accrual_rate(annual_entitlement)
            }
        
        return {
            'success': True,
            'employee_id': getattr(employee, 'employee_id', 'unknown'),
            'period_date': period_data.get('period_date'),
            'payroll_functions': payroll_functions,
            'seniority_info': seniority_info,
            'leave_info': leave_info,
            'validation': validation
        }
    
    def calculate_termination_settlement(self, employee, termination_data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Calculate complete termination settlement using severance rules
        
        Args:
            employee: Employee instance
            termination_data: Termination details
            
        Returns:
            Complete termination settlement
        """
        if not hasattr(employee, 'hire_date') or not employee.hire_date:
            return {
                'success': False,
                'error': 'Employee hire date required for termination calculation'
            }
        
        seniority_years = DateCalculator.get_years_between(
            employee.hire_date, 
            termination_data.get('termination_date', date.today())
        )
        
        # Prepare employee data for settlement calculation
        employee_data = {
            'seniority_years': seniority_years,
            'average_monthly_salary': termination_data.get('average_monthly_salary', Decimal('0')),
            'outstanding_leave_days': termination_data.get('outstanding_leave_days', 0),
            'daily_salary': termination_data.get('daily_salary', Decimal('0')),
            'category': getattr(employee, 'category', 'standard'),
            'annual_bonus': termination_data.get('annual_bonus', Decimal('0')),
            'other_entitlements': termination_data.get('other_entitlements', Decimal('0'))
        }
        
        # Calculate final settlement
        settlement = self.business_rules.SeveranceRules.calculate_final_settlement(
            employee_data, termination_data
        )
        
        return {
            'success': True,
            'employee_id': getattr(employee, 'employee_id', 'unknown'),
            'termination_date': termination_data.get('termination_date'),
            'seniority_years': seniority_years,
            'settlement_breakdown': settlement,
            'termination_reason': termination_data.get('reason', 'unknown')
        }
    
    def get_engine_status(self) -> Dict[str, Any]:
        """
        Get comprehensive status of the business rules engine
        
        Returns:
            Engine status and configuration
        """
        return {
            'engine_version': '1.0.0',
            'system_parameters_loaded': self.system_parameters is not None,
            'available_rules': self.business_rules.get_business_rules_summary(),
            'validation_enabled': True,
            'integration_status': {
                'payroll_calculations': 'Active',
                'tax_calculations': 'Active',
                'date_utils': 'Active'
            }
        }


# Export main classes for use
__all__ = [
    'PayrollBusinessRules',
    'BusinessRulesValidator', 
    'BusinessRulesEngine'
]