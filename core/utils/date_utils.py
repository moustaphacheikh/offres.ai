# date_utils.py
"""
Date manipulation and formatting utilities
Converted from Java date utilities in GeneralLib.java and other date functions
"""

from datetime import datetime, date, timedelta
from dateutil.relativedelta import relativedelta
from typing import List, Optional, Tuple, Union
import calendar


class DateCalculator:
    """
    Date calculation utilities
    Equivalent to date manipulation methods in GeneralLib.java
    """
    
    @staticmethod
    def add_days(base_date: Union[date, datetime], days: int) -> date:
        """
        Add or subtract days from a date
        Equivalent to addRetriveDays method in GeneralLib.java
        
        Args:
            base_date: Base date
            days: Number of days to add (negative to subtract)
            
        Returns:
            New date after adding days
        """
        if isinstance(base_date, datetime):
            base_date = base_date.date()
        
        return base_date + timedelta(days=days)
    
    @staticmethod
    def add_months(base_date: Union[date, datetime], months: int) -> date:
        """
        Add or subtract months from a date
        
        Args:
            base_date: Base date
            months: Number of months to add (negative to subtract)
            
        Returns:
            New date after adding months
        """
        if isinstance(base_date, datetime):
            base_date = base_date.date()
        
        return base_date + relativedelta(months=months)
    
    @staticmethod
    def add_years(base_date: Union[date, datetime], years: int) -> date:
        """
        Add or subtract years from a date
        
        Args:
            base_date: Base date
            years: Number of years to add (negative to subtract)
            
        Returns:
            New date after adding years
        """
        if isinstance(base_date, datetime):
            base_date = base_date.date()
        
        return base_date + relativedelta(years=years)
    
    @staticmethod
    def get_days_between(start_date: Union[date, datetime], 
                        end_date: Union[date, datetime]) -> int:
        """
        Calculate number of days between two dates
        
        Args:
            start_date: Start date
            end_date: End date
            
        Returns:
            Number of days between dates
        """
        if isinstance(start_date, datetime):
            start_date = start_date.date()
        if isinstance(end_date, datetime):
            end_date = end_date.date()
        
        return (end_date - start_date).days
    
    @staticmethod
    def get_months_between(start_date: Union[date, datetime],
                          end_date: Union[date, datetime]) -> int:
        """
        Calculate number of months between two dates
        
        Args:
            start_date: Start date
            end_date: End date
            
        Returns:
            Number of months between dates
        """
        if isinstance(start_date, datetime):
            start_date = start_date.date()
        if isinstance(end_date, datetime):
            end_date = end_date.date()
        
        return (end_date.year - start_date.year) * 12 + end_date.month - start_date.month
    
    @staticmethod
    def get_years_between(start_date: Union[date, datetime],
                         end_date: Union[date, datetime]) -> int:
        """
        Calculate number of complete years between two dates
        
        Args:
            start_date: Start date
            end_date: End date
            
        Returns:
            Number of complete years between dates
        """
        if isinstance(start_date, datetime):
            start_date = start_date.date()
        if isinstance(end_date, datetime):
            end_date = end_date.date()
        
        years = end_date.year - start_date.year
        
        # Adjust if birthday hasn't occurred this year
        if (end_date.month, end_date.day) < (start_date.month, start_date.day):
            years -= 1
        
        return years


class PayrollPeriodUtils:
    """
    Payroll period calculation utilities
    """
    
    @staticmethod
    def get_period_start_end(period_date: Union[date, datetime]) -> Tuple[date, date]:
        """
        Get start and end dates for a payroll period (typically month-based)
        
        Args:
            period_date: Any date within the period
            
        Returns:
            Tuple of (period_start, period_end)
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        # First day of the month
        period_start = period_date.replace(day=1)
        
        # Last day of the month
        period_end = period_date.replace(day=calendar.monthrange(period_date.year, period_date.month)[1])
        
        return period_start, period_end
    
    @staticmethod
    def get_previous_period(period_date: Union[date, datetime]) -> date:
        """
        Get the previous payroll period
        
        Args:
            period_date: Current period date
            
        Returns:
            Previous period date (typically 28th of previous month)
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        # Go to previous month, set to 28th to ensure valid date
        prev_month = DateCalculator.add_months(period_date, -1)
        return prev_month.replace(day=28)
    
    @staticmethod
    def get_next_period(period_date: Union[date, datetime]) -> date:
        """
        Get the next payroll period
        
        Args:
            period_date: Current period date
            
        Returns:
            Next period date (typically 28th of next month)
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        # Go to next month, set to 28th to ensure valid date
        next_month = DateCalculator.add_months(period_date, 1)
        return next_month.replace(day=28)
    
    @staticmethod
    def get_period_working_days(period_date: Union[date, datetime],
                              exclude_weekends: bool = True,
                              holidays: List[date] = None) -> int:
        """
        Calculate working days in a payroll period
        
        Args:
            period_date: Period date
            exclude_weekends: Whether to exclude weekends
            holidays: List of holiday dates to exclude
            
        Returns:
            Number of working days
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        period_start, period_end = PayrollPeriodUtils.get_period_start_end(period_date)
        
        working_days = 0
        current_date = period_start
        holidays = holidays or []
        
        while current_date <= period_end:
            # Skip weekends if requested
            if exclude_weekends and current_date.weekday() >= 5:  # Saturday=5, Sunday=6
                current_date += timedelta(days=1)
                continue
            
            # Skip holidays
            if current_date in holidays:
                current_date += timedelta(days=1)
                continue
            
            working_days += 1
            current_date += timedelta(days=1)
        
        return working_days


class DateFormatter:
    """
    Date formatting utilities for different contexts
    """
    
    @staticmethod
    def format_for_display(date_obj: Union[date, datetime, None],
                          format_type: str = "standard") -> str:
        """
        Format date for display in UI
        
        Args:
            date_obj: Date to format
            format_type: Format type ("standard", "short", "long", "french")
            
        Returns:
            Formatted date string
        """
        if not date_obj:
            return ""
        
        if isinstance(date_obj, datetime):
            date_obj = date_obj.date()
        
        formats = {
            "standard": "%d/%m/%Y",
            "short": "%d/%m/%y",
            "long": "%d %B %Y",
            "french": "%d %B %Y",
            "iso": "%Y-%m-%d"
        }
        
        format_str = formats.get(format_type, "%d/%m/%Y")
        
        if format_type == "french":
            # French month names
            months_fr = [
                "janvier", "février", "mars", "avril", "mai", "juin",
                "juillet", "août", "septembre", "octobre", "novembre", "décembre"
            ]
            return f"{date_obj.day} {months_fr[date_obj.month - 1]} {date_obj.year}"
        
        return date_obj.strftime(format_str)
    
    @staticmethod
    def format_for_reports(date_obj: Union[date, datetime, None],
                          include_time: bool = False) -> str:
        """
        Format date for reports
        
        Args:
            date_obj: Date to format
            include_time: Whether to include time
            
        Returns:
            Formatted date string for reports
        """
        if not date_obj:
            return ""
        
        if isinstance(date_obj, date) and not isinstance(date_obj, datetime):
            return DateFormatter.format_for_display(date_obj, "french")
        
        if include_time:
            return date_obj.strftime("%d %B %Y à %H:%M")
        else:
            return DateFormatter.format_for_display(date_obj.date(), "french")
    
    @staticmethod
    def format_period_range(start_date: Union[date, datetime],
                           end_date: Union[date, datetime]) -> str:
        """
        Format a date range for display
        
        Args:
            start_date: Range start date
            end_date: Range end date
            
        Returns:
            Formatted date range
        """
        start_formatted = DateFormatter.format_for_display(start_date, "french")
        end_formatted = DateFormatter.format_for_display(end_date, "french")
        
        if start_formatted == end_formatted:
            return start_formatted
        
        return f"Du {start_formatted} au {end_formatted}"


class WorkingDayCalculator:
    """
    Working day and schedule calculations
    """
    
    @staticmethod
    def is_working_day(check_date: Union[date, datetime],
                      work_schedule: dict = None,
                      holidays: List[date] = None) -> bool:
        """
        Check if a date is a working day
        
        Args:
            check_date: Date to check
            work_schedule: Weekly schedule (Monday=0 to Sunday=6)
            holidays: List of holidays
            
        Returns:
            True if it's a working day
        """
        if isinstance(check_date, datetime):
            check_date = check_date.date()
        
        # Check if it's a holiday
        if holidays and check_date in holidays:
            return False
        
        # Default work schedule (Monday-Friday)
        if not work_schedule:
            work_schedule = {0: True, 1: True, 2: True, 3: True, 4: True, 5: False, 6: False}
        
        weekday = check_date.weekday()  # Monday=0, Sunday=6
        return work_schedule.get(weekday, False)
    
    @staticmethod
    def get_working_days_in_range(start_date: Union[date, datetime],
                                 end_date: Union[date, datetime],
                                 work_schedule: dict = None,
                                 holidays: List[date] = None) -> List[date]:
        """
        Get list of working days in a date range
        
        Args:
            start_date: Range start
            end_date: Range end
            work_schedule: Weekly schedule
            holidays: List of holidays
            
        Returns:
            List of working dates
        """
        if isinstance(start_date, datetime):
            start_date = start_date.date()
        if isinstance(end_date, datetime):
            end_date = end_date.date()
        
        working_days = []
        current_date = start_date
        
        while current_date <= end_date:
            if WorkingDayCalculator.is_working_day(current_date, work_schedule, holidays):
                working_days.append(current_date)
            current_date += timedelta(days=1)
        
        return working_days


class HolidayUtils:
    """
    Holiday calculation utilities for Mauritania
    """
    
    @staticmethod
    def get_fixed_holidays(year: int) -> List[date]:
        """
        Get fixed holidays for Mauritania
        
        Args:
            year: Year to get holidays for
            
        Returns:
            List of fixed holiday dates
        """
        return [
            date(year, 1, 1),   # New Year's Day
            date(year, 5, 1),   # Labor Day
            date(year, 7, 10),  # Mauritanian Armed Forces Day
            date(year, 11, 28), # Independence Day
        ]
    
    @staticmethod
    def is_ramadan_period(check_date: Union[date, datetime]) -> bool:
        """
        Check if date falls within Ramadan period
        Note: This is a simplified check - actual implementation would need
        Islamic calendar calculations
        
        Args:
            check_date: Date to check
            
        Returns:
            True if within Ramadan period
        """
        # This is a placeholder - actual implementation would use
        # Islamic calendar calculations or external API
        return False


class DateValidation:
    """
    Date validation utilities
    """
    
    @staticmethod
    def is_valid_hire_date(hire_date: Union[date, datetime],
                          birth_date: Union[date, datetime] = None) -> Tuple[bool, str]:
        """
        Validate employee hire date
        
        Args:
            hire_date: Proposed hire date
            birth_date: Employee birth date for age validation
            
        Returns:
            Tuple of (is_valid, error_message)
        """
        if isinstance(hire_date, datetime):
            hire_date = hire_date.date()
        if birth_date and isinstance(birth_date, datetime):
            birth_date = birth_date.date()
        
        today = date.today()
        
        # Cannot be in the future
        if hire_date > today:
            return False, "La date d'embauche ne peut pas être dans le futur"
        
        # Check minimum age if birth date provided
        if birth_date:
            age_at_hire = DateCalculator.get_years_between(birth_date, hire_date)
            if age_at_hire < 16:
                return False, "L'employé doit avoir au moins 16 ans à l'embauche"
        
        # Cannot be too far in the past (e.g., more than 50 years)
        if DateCalculator.get_years_between(hire_date, today) > 50:
            return False, "Date d'embauche trop ancienne"
        
        return True, ""
    
    @staticmethod
    def is_valid_period_date(period_date: Union[date, datetime]) -> Tuple[bool, str]:
        """
        Validate payroll period date
        
        Args:
            period_date: Period date to validate
            
        Returns:
            Tuple of (is_valid, error_message)
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        today = date.today()
        
        # Cannot be more than 2 years in the past
        if DateCalculator.get_years_between(period_date, today) > 2:
            return False, "Période trop ancienne"
        
        # Cannot be more than 1 year in the future
        if DateCalculator.get_years_between(today, period_date) > 1:
            return False, "Période trop éloignée dans le futur"
        
        return True, ""