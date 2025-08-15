# date_utils.py
"""
Date manipulation and formatting utilities
Converted from Java date utilities in GeneralLib.java and other date functions
"""

from datetime import datetime, date, timedelta
from dateutil.relativedelta import relativedelta
from typing import List, Optional, Tuple, Union, Dict, Any
import calendar
import math
import warnings

# Islamic calendar support (for production use hijri-converter package)
try:
    from hijri_converter import Hijri, Gregorian
    HIJRI_AVAILABLE = True
except ImportError:
    HIJRI_AVAILABLE = False


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
    
    @staticmethod
    def difference_date_hours(start_date: Union[date, datetime], 
                             end_date: Union[date, datetime]) -> float:
        """
        Calculate hour difference between two dates
        Equivalent to differenceDateHeure method in GeneralLib.java
        
        Args:
            start_date: Start date/datetime
            end_date: End date/datetime
            
        Returns:
            Number of hours between dates
        """
        if isinstance(start_date, date) and not isinstance(start_date, datetime):
            start_date = datetime.combine(start_date, datetime.min.time())
        if isinstance(end_date, date) and not isinstance(end_date, datetime):
            end_date = datetime.combine(end_date, datetime.min.time())
        
        UNE_HEURE = 3600000.0  # milliseconds in an hour
        time_diff_ms = (end_date.timestamp() - start_date.timestamp()) * 1000
        return time_diff_ms / UNE_HEURE
    
    @staticmethod
    def difference_date_months(start_date: Union[date, datetime], 
                              end_date: Union[date, datetime]) -> int:
        """
        Calculate month difference between two dates with business logic
        Equivalent to differenceDateMois method in GeneralLib.java
        
        Args:
            start_date: Start date
            end_date: End date
            
        Returns:
            Number of months between dates (business calculation)
        """
        if isinstance(start_date, datetime):
            start_date = start_date.date()
        if isinstance(end_date, datetime):
            end_date = end_date.date()
        
        UNE_HEURE = 3600000  # milliseconds in an hour
        time_diff_ms = (end_date - start_date).total_seconds() * 1000 + UNE_HEURE
        return int((time_diff_ms / UNE_HEURE) / 24 / 30)
    
    @staticmethod
    def difference_date_days(start_date: Union[date, datetime], 
                            end_date: Union[date, datetime]) -> int:
        """
        Calculate day difference between two dates with business logic
        Equivalent to differenceDateJour method in GeneralLib.java
        
        Args:
            start_date: Start date
            end_date: End date
            
        Returns:
            Number of days between dates (business calculation)
        """
        if isinstance(start_date, datetime):
            start_date = start_date.date()
        if isinstance(end_date, datetime):
            end_date = end_date.date()
        
        UNE_HEURE = 3600000  # milliseconds in an hour
        time_diff_ms = (end_date - start_date).total_seconds() * 1000 + UNE_HEURE
        return int((time_diff_ms / UNE_HEURE) / 24)
    
    @staticmethod
    def age_years(birth_date: Union[date, datetime]) -> int:
        """
        Calculate age in years from birth date
        Equivalent to ageYears method in GeneralLib.java
        
        Args:
            birth_date: Birth date
            
        Returns:
            Age in complete years
        """
        if isinstance(birth_date, datetime):
            birth_date = birth_date.date()
        
        now = date.today()
        days_lived = (now - birth_date).days
        return int(math.floor(days_lived / 365))
    
    @staticmethod
    def year_from_date(period_date: Union[date, datetime]) -> int:
        """
        Extract year from date
        Equivalent to yearFromDate method in GeneralLib.java
        
        Args:
            period_date: Date to extract year from
            
        Returns:
            Year as integer
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        return period_date.year
    
    @staticmethod
    def quarter_number(period_date: Union[date, datetime]) -> int:
        """
        Calculate quarter number for a given date
        Equivalent to quarterNumber method in GeneralLib.java
        
        Args:
            period_date: Date to calculate quarter for
            
        Returns:
            Quarter number (1-4)
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        month = period_date.month - 1  # 0-based month (Java Calendar.MONTH)
        
        if 0 <= month <= 2:
            return 1
        elif 3 <= month <= 5:
            return 2
        elif 6 <= month <= 8:
            return 3
        else:
            return 4


class PayrollPeriodUtils:
    """
    Payroll period calculation utilities
    Based on Java payroll system logic using 28th day standardization
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
        Uses 28th day standardization as per Java system
        
        Args:
            period_date: Current period date
            
        Returns:
            Previous period date (28th of previous month)
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        # Go to previous month, set to 28th to ensure valid date (Java logic)
        prev_month = DateCalculator.add_months(period_date, -1)
        return prev_month.replace(day=28)
    
    @staticmethod
    def get_next_period(period_date: Union[date, datetime]) -> date:
        """
        Get the next payroll period
        Uses 28th day standardization as per Java system
        
        Args:
            period_date: Current period date
            
        Returns:
            Next period date (28th of next month)
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        # Go to next month, set to 28th to ensure valid date (Java logic)
        next_month = DateCalculator.add_months(period_date, 1)
        return next_month.replace(day=28)
    
    @staticmethod
    def normalize_period_date(period_date: Union[date, datetime]) -> date:
        """
        Normalize a period date to 28th day for consistency
        Based on Java payroll system standardization
        
        Args:
            period_date: Period date to normalize
            
        Returns:
            Normalized period date (28th of same month)
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        try:
            return period_date.replace(day=28)
        except ValueError:
            # Handle February in non-leap years
            return period_date.replace(day=calendar.monthrange(period_date.year, period_date.month)[1])
    
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
    
    @staticmethod
    def get_period_for_year_month(year: int, month: int) -> date:
        """
        Get a standardized period date for a given year and month
        
        Args:
            year: Year
            month: Month (1-12)
            
        Returns:
            Standardized period date (28th day)
        """
        try:
            return date(year, month, 28)
        except ValueError:
            # Handle February in non-leap years
            return date(year, month, calendar.monthrange(year, month)[1])
    
    @staticmethod
    def is_same_period(date1: Union[date, datetime], date2: Union[date, datetime]) -> bool:
        """
        Check if two dates are in the same payroll period (same month/year)
        
        Args:
            date1: First date
            date2: Second date
            
        Returns:
            True if same period, False otherwise
        """
        if isinstance(date1, datetime):
            date1 = date1.date()
        if isinstance(date2, datetime):
            date2 = date2.date()
        
        return date1.year == date2.year and date1.month == date2.month


class DateFormatter:
    """
    Date formatting utilities for different contexts
    Supports French and Arabic localization for Mauritanian payroll system
    """
    
    # French month names
    MONTHS_FRENCH = [
        "janvier", "février", "mars", "avril", "mai", "juin",
        "juillet", "août", "septembre", "octobre", "novembre", "décembre"
    ]
    
    # Arabic month names (Gregorian calendar)
    MONTHS_ARABIC = [
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
    ]
    
    # Islamic month names (Hijri calendar)
    HIJRI_MONTHS = [
        "محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الثانية",
        "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    ]
    
    @staticmethod
    def format_for_display(date_obj: Union[date, datetime, None],
                          format_type: str = "standard",
                          locale: str = "fr") -> str:
        """
        Format date for display in UI with locale support
        
        Args:
            date_obj: Date to format
            format_type: Format type ("standard", "short", "long", "french", "arabic")
            locale: Locale ("fr" for French, "ar" for Arabic)
            
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
            "arabic": "%d %B %Y",
            "iso": "%Y-%m-%d"
        }
        
        format_str = formats.get(format_type, "%d/%m/%Y")
        
        if format_type in ["french", "long"] or locale == "fr":
            return f"{date_obj.day} {DateFormatter.MONTHS_FRENCH[date_obj.month - 1]} {date_obj.year}"
        elif format_type == "arabic" or locale == "ar":
            return f"{date_obj.day} {DateFormatter.MONTHS_ARABIC[date_obj.month - 1]} {date_obj.year}"
        
        return date_obj.strftime(format_str)
    
    @staticmethod
    def format_for_reports(date_obj: Union[date, datetime, None],
                          include_time: bool = False,
                          locale: str = "fr") -> str:
        """
        Format date for reports with locale support
        
        Args:
            date_obj: Date to format
            include_time: Whether to include time
            locale: Locale ("fr" for French, "ar" for Arabic)
            
        Returns:
            Formatted date string for reports
        """
        if not date_obj:
            return ""
        
        if isinstance(date_obj, date) and not isinstance(date_obj, datetime):
            return DateFormatter.format_for_display(date_obj, "french", locale)
        
        if include_time:
            time_indicator = "à" if locale == "fr" else "في"
            formatted_date = DateFormatter.format_for_display(date_obj.date(), "french", locale)
            return f"{formatted_date} {time_indicator} {date_obj.strftime('%H:%M')}"
        else:
            return DateFormatter.format_for_display(date_obj.date(), "french", locale)
    
    @staticmethod
    def format_period_range(start_date: Union[date, datetime],
                           end_date: Union[date, datetime],
                           locale: str = "fr") -> str:
        """
        Format a date range for display with locale support
        
        Args:
            start_date: Range start date
            end_date: Range end date
            locale: Locale ("fr" for French, "ar" for Arabic)
            
        Returns:
            Formatted date range
        """
        start_formatted = DateFormatter.format_for_display(start_date, "french", locale)
        end_formatted = DateFormatter.format_for_display(end_date, "french", locale)
        
        if start_formatted == end_formatted:
            return start_formatted
        
        if locale == "ar":
            return f"من {start_formatted} إلى {end_formatted}"
        else:
            return f"Du {start_formatted} au {end_formatted}"
    
    @staticmethod
    def format_period_letter(period_date: Union[date, datetime],
                            locale: str = "fr") -> str:
        """
        Format period date for letters and official documents
        Based on Java 'periodeLettre' formatting
        
        Args:
            period_date: Period date
            locale: Locale ("fr" for French, "ar" for Arabic)
            
        Returns:
            Formatted period string for letters
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        if locale == "ar":
            month_name = DateFormatter.MONTHS_ARABIC[period_date.month - 1]
            return f"{month_name} {period_date.year}"
        else:
            month_name = DateFormatter.MONTHS_FRENCH[period_date.month - 1]
            return f"{month_name.title()} {period_date.year}"
    
    @staticmethod
    def format_payroll_period(period_date: Union[date, datetime],
                             locale: str = "fr") -> str:
        """
        Format payroll period for display in payroll documents
        
        Args:
            period_date: Period date
            locale: Locale ("fr" for French, "ar" for Arabic)
            
        Returns:
            Formatted payroll period string
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        period_start, period_end = PayrollPeriodUtils.get_period_start_end(period_date)
        
        if locale == "ar":
            start_formatted = DateFormatter.format_for_display(period_start, "arabic", locale)
            end_formatted = DateFormatter.format_for_display(period_end, "arabic", locale)
            return f"فترة من {start_formatted} إلى {end_formatted}"
        else:
            start_formatted = DateFormatter.format_for_display(period_start, "french", locale)
            end_formatted = DateFormatter.format_for_display(period_end, "french", locale)
            return f"Période du {start_formatted} au {end_formatted}"


class WorkingDayCalculator:
    """
    Working day and schedule calculations
    Based on Mauritanian work week and NJT (Nombre de Jours Travaillés) calculations
    """
    
    # Mauritanian work week (Sunday-Thursday)
    MAURITANIAN_WORK_SCHEDULE = {
        0: True,   # Monday
        1: True,   # Tuesday  
        2: True,   # Wednesday
        3: True,   # Thursday
        4: False,  # Friday (weekend) - Jumu'ah day
        5: False,  # Saturday (weekend)
        6: True,   # Sunday (working day in Mauritania)
    }
    
    # Western work week (Monday-Friday) for comparison
    WESTERN_WORK_SCHEDULE = {
        0: True,   # Monday
        1: True,   # Tuesday  
        2: True,   # Wednesday
        3: True,   # Thursday
        4: True,   # Friday
        5: False,  # Saturday (weekend)
        6: False,  # Sunday (weekend)
    }
    
    @staticmethod
    def is_working_day(check_date: Union[date, datetime],
                      work_schedule: dict = None,
                      holidays: List[date] = None) -> bool:
        """
        Check if a date is a working day
        
        Args:
            check_date: Date to check
            work_schedule: Weekly schedule (Monday=0 to Sunday=6), defaults to Mauritanian
            holidays: List of holidays
            
        Returns:
            True if it's a working day
        """
        if isinstance(check_date, datetime):
            check_date = check_date.date()
        
        # Check if it's a holiday
        if holidays and check_date in holidays:
            return False
        
        # Default to Mauritanian work schedule
        if not work_schedule:
            work_schedule = WorkingDayCalculator.MAURITANIAN_WORK_SCHEDULE
        
        weekday = check_date.weekday()  # Monday=0, Sunday=6
        return work_schedule.get(weekday, False)
    
    @staticmethod
    def is_weekend(check_date: Union[date, datetime],
                  work_schedule: dict = None) -> bool:
        """
        Check if a date is a weekend day based on work schedule
        
        Args:
            check_date: Date to check
            work_schedule: Weekly schedule (Monday=0 to Sunday=6), defaults to Mauritanian
            
        Returns:
            True if it's a weekend day
        """
        if isinstance(check_date, datetime):
            check_date = check_date.date()
        
        # Default to Mauritanian work schedule
        if not work_schedule:
            work_schedule = WorkingDayCalculator.MAURITANIAN_WORK_SCHEDULE
        
        weekday = check_date.weekday()  # Monday=0, Sunday=6
        return not work_schedule.get(weekday, False)
    
    @staticmethod
    def get_weekend_days() -> List[int]:
        """
        Get weekend days for Mauritanian work schedule
        
        Returns:
            List of weekend weekdays (4=Friday, 5=Saturday)
        """
        return [4, 5]  # Friday and Saturday
    
    @staticmethod
    def get_business_days_between(start_date: Union[date, datetime],
                                end_date: Union[date, datetime],
                                work_schedule: dict = None,
                                holidays: List[date] = None) -> int:
        """
        Calculate business days between two dates
        
        Args:
            start_date: Start date
            end_date: End date
            work_schedule: Weekly schedule
            holidays: List of holidays
            
        Returns:
            Number of business days
        """
        working_days = WorkingDayCalculator.get_working_days_in_range(
            start_date, end_date, work_schedule, holidays
        )
        return len(working_days)
    
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
    
    @staticmethod
    def calculate_njt_for_period(period_date: Union[date, datetime],
                                work_schedule: dict = None,
                                holidays: List[date] = None,
                                default_njt: int = 30) -> int:
        """
        Calculate NJT (Nombre de Jours Travaillés) for a payroll period
        Based on Java NJT calculation logic
        
        Args:
            period_date: Period date
            work_schedule: Weekly schedule
            holidays: List of holidays 
            default_njt: Default NJT value
            
        Returns:
            Number of working days (NJT) for the period
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        period_start, period_end = PayrollPeriodUtils.get_period_start_end(period_date)
        working_days = WorkingDayCalculator.get_working_days_in_range(
            period_start, period_end, work_schedule, holidays
        )
        
        # Return calculated working days or default if none found
        return len(working_days) if working_days else default_njt
    
    @staticmethod
    def get_working_hours_for_period(period_date: Union[date, datetime],
                                   hours_per_day: float = 8.0,
                                   work_schedule: dict = None,
                                   holidays: List[date] = None) -> float:
        """
        Calculate total working hours for a payroll period
        
        Args:
            period_date: Period date
            hours_per_day: Working hours per day
            work_schedule: Weekly schedule
            holidays: List of holidays
            
        Returns:
            Total working hours for the period
        """
        njt = WorkingDayCalculator.calculate_njt_for_period(
            period_date, work_schedule, holidays
        )
        return njt * hours_per_day


class IslamicCalendarUtils:
    """
    Islamic calendar utilities for Mauritanian payroll system
    Provides Islamic date conversion and holiday calculations
    """
    
    # Islamic month names in Arabic
    HIJRI_MONTHS_ARABIC = [
        "محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الثانية",
        "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    ]
    
    # Islamic month names in French transliteration
    HIJRI_MONTHS_FRENCH = [
        "Mouharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani", "Joumada al-Oula", "Joumada al-Thania",
        "Rajab", "Sha'ban", "Ramadan", "Shawwal", "Dhou al-Qi'da", "Dhou al-Hijja"
    ]
    
    @staticmethod
    def convert_to_hijri(gregorian_date: Union[date, datetime]) -> Optional[Tuple[int, int, int]]:
        """
        Convert Gregorian date to Hijri (Islamic) date
        
        Args:
            gregorian_date: Gregorian date to convert
            
        Returns:
            Tuple of (hijri_year, hijri_month, hijri_day) or None if conversion fails
        """
        if isinstance(gregorian_date, datetime):
            gregorian_date = gregorian_date.date()
        
        if not HIJRI_AVAILABLE:
            warnings.warn("hijri-converter package not available. Install it for Islamic calendar support.")
            return None
        
        try:
            gregorian_obj = Gregorian(gregorian_date.year, gregorian_date.month, gregorian_date.day)
            hijri_date = gregorian_obj.to_hijri()
            return (hijri_date.year, hijri_date.month, hijri_date.day)
        except Exception:
            return None
    
    @staticmethod
    def convert_from_hijri(hijri_year: int, hijri_month: int, hijri_day: int) -> Optional[date]:
        """
        Convert Hijri (Islamic) date to Gregorian date
        
        Args:
            hijri_year: Hijri year
            hijri_month: Hijri month (1-12)
            hijri_day: Hijri day
            
        Returns:
            Gregorian date or None if conversion fails
        """
        if not HIJRI_AVAILABLE:
            warnings.warn("hijri-converter package not available. Install it for Islamic calendar support.")
            return None
        
        try:
            hijri_date = Hijri(hijri_year, hijri_month, hijri_day)
            gregorian_date = hijri_date.to_gregorian()
            return date(gregorian_date.year, gregorian_date.month, gregorian_date.day)
        except Exception:
            return None
    
    @staticmethod
    def format_hijri_date(gregorian_date: Union[date, datetime], locale: str = "ar") -> str:
        """
        Format date in Hijri calendar
        
        Args:
            gregorian_date: Gregorian date to format
            locale: Language locale ("ar" for Arabic, "fr" for French)
            
        Returns:
            Formatted Hijri date string
        """
        hijri_tuple = IslamicCalendarUtils.convert_to_hijri(gregorian_date)
        if not hijri_tuple:
            return ""
        
        hijri_year, hijri_month, hijri_day = hijri_tuple
        
        if locale == "ar":
            month_name = IslamicCalendarUtils.HIJRI_MONTHS_ARABIC[hijri_month - 1]
            return f"{hijri_day} {month_name} {hijri_year} هـ"
        else:
            month_name = IslamicCalendarUtils.HIJRI_MONTHS_FRENCH[hijri_month - 1]
            return f"{hijri_day} {month_name} {hijri_year} H"
    
    @staticmethod
    def is_ramadan_month(gregorian_date: Union[date, datetime]) -> bool:
        """
        Check if a Gregorian date falls in Ramadan month
        
        Args:
            gregorian_date: Date to check
            
        Returns:
            True if the date is in Ramadan month
        """
        hijri_tuple = IslamicCalendarUtils.convert_to_hijri(gregorian_date)
        if not hijri_tuple:
            # Fallback to estimation if hijri-converter not available
            return HolidayUtils.is_ramadan_period(gregorian_date)
        
        _, hijri_month, _ = hijri_tuple
        return hijri_month == 9  # Ramadan is the 9th month in Islamic calendar
    
    @staticmethod
    def get_islamic_holidays_for_year(gregorian_year: int) -> List[Tuple[date, str, str]]:
        """
        Get Islamic holidays for a Gregorian year
        
        Args:
            gregorian_year: Gregorian year
            
        Returns:
            List of tuples (date, arabic_name, french_name)
        """
        holidays = []
        
        if not HIJRI_AVAILABLE:
            # Fallback to estimated dates
            estimated = HolidayUtils.get_estimated_islamic_holidays(gregorian_year)
            holiday_names = [
                ("عيد الفطر", "Eid al-Fitr"),
                ("عيد الأضحى", "Eid al-Adha"),
                ("رأس السنة الهجرية", "Nouvel An Islamique"),
                ("المولد النبوي", "Mawlid al-Nabi")
            ]
            for i, holiday_date in enumerate(estimated):
                if i < len(holiday_names):
                    arabic_name, french_name = holiday_names[i]
                    holidays.append((holiday_date, arabic_name, french_name))
            return holidays
        
        # Calculate major Islamic holidays using proper Islamic calendar
        try:
            # Get the current Hijri year for the Gregorian year
            gregorian_start = date(gregorian_year, 1, 1)
            hijri_start = IslamicCalendarUtils.convert_to_hijri(gregorian_start)
            
            if hijri_start:
                hijri_year = hijri_start[0]
                
                # Major Islamic holidays
                islamic_holidays = [
                    (1, 1, "رأس السنة الهجرية", "Nouvel An Islamique"),  # Islamic New Year
                    (3, 12, "المولد النبوي", "Mawlid al-Nabi"),  # Prophet's Birthday
                    (10, 1, "عيد الفطر", "Eid al-Fitr"),  # End of Ramadan
                    (12, 10, "عيد الأضحى", "Eid al-Adha"),  # Festival of Sacrifice
                ]
                
                for month, day, arabic_name, french_name in islamic_holidays:
                    gregorian_date = IslamicCalendarUtils.convert_from_hijri(hijri_year, month, day)
                    if gregorian_date and gregorian_date.year == gregorian_year:
                        holidays.append((gregorian_date, arabic_name, french_name))
                    
                    # Also check next Hijri year for holidays that might fall in same Gregorian year
                    gregorian_date_next = IslamicCalendarUtils.convert_from_hijri(hijri_year + 1, month, day)
                    if gregorian_date_next and gregorian_date_next.year == gregorian_year:
                        holidays.append((gregorian_date_next, arabic_name, french_name))
        
        except Exception:
            pass
        
        return sorted(holidays, key=lambda x: x[0])


class ArabicDateFormatter:
    """
    Arabic date formatting utilities with RTL support
    """
    
    # Arabic numerals (Eastern Arabic)
    ARABIC_NUMERALS = {
        '0': '٠', '1': '١', '2': '٢', '3': '٣', '4': '٤',
        '5': '٥', '6': '٦', '7': '٧', '8': '٨', '9': '٩'
    }
    
    # Arabic day names
    ARABIC_DAYS = [
        "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت", "الأحد"
    ]
    
    @staticmethod
    def convert_to_arabic_numerals(text: str) -> str:
        """
        Convert Western numerals to Arabic numerals
        
        Args:
            text: Text containing Western numerals
            
        Returns:
            Text with Arabic numerals
        """
        for western, arabic in ArabicDateFormatter.ARABIC_NUMERALS.items():
            text = text.replace(western, arabic)
        return text
    
    @staticmethod
    def format_arabic_date(date_obj: Union[date, datetime],
                          use_arabic_numerals: bool = True,
                          include_day_name: bool = False) -> str:
        """
        Format date in Arabic with proper RTL formatting
        
        Args:
            date_obj: Date to format
            use_arabic_numerals: Whether to use Arabic numerals
            include_day_name: Whether to include day name
            
        Returns:
            Arabic formatted date
        """
        if isinstance(date_obj, datetime):
            date_obj = date_obj.date()
        
        # Get Arabic month name
        month_name = DateFormatter.MONTHS_ARABIC[date_obj.month - 1]
        
        # Format date
        formatted = f"{date_obj.day} {month_name} {date_obj.year}"
        
        # Add day name if requested
        if include_day_name:
            day_name = ArabicDateFormatter.ARABIC_DAYS[date_obj.weekday()]
            formatted = f"{day_name}، {formatted}"
        
        # Convert to Arabic numerals if requested
        if use_arabic_numerals:
            formatted = ArabicDateFormatter.convert_to_arabic_numerals(formatted)
        
        return formatted
    
    @staticmethod
    def format_bilingual_date(date_obj: Union[date, datetime],
                            separator: str = " / ") -> str:
        """
        Format date in both French and Arabic
        
        Args:
            date_obj: Date to format
            separator: Separator between languages
            
        Returns:
            Bilingual formatted date
        """
        french = DateFormatter.format_for_display(date_obj, "french", "fr")
        arabic = ArabicDateFormatter.format_arabic_date(date_obj)
        return f"{french}{separator}{arabic}"


class HolidayUtils:
    """
    Holiday calculation utilities for Mauritania
    Includes Islamic holidays and national holidays
    """
    
    # Fixed national holidays for Mauritania
    MAURITANIAN_FIXED_HOLIDAYS = {
        (1, 1): "Nouvel An",  # New Year's Day
        (5, 1): "Fête du Travail",  # Labor Day
        (5, 25): "Fête de l'Afrique",  # Africa Day
        (7, 10): "Fête des Forces Armées",  # Armed Forces Day
        (11, 28): "Fête de l'Indépendance",  # Independence Day
    }
    
    @staticmethod
    def get_fixed_holidays(year: int) -> List[date]:
        """
        Get fixed holidays for Mauritania
        
        Args:
            year: Year to get holidays for
            
        Returns:
            List of fixed holiday dates
        """
        holidays = []
        for (month, day), name in HolidayUtils.MAURITANIAN_FIXED_HOLIDAYS.items():
            try:
                holidays.append(date(year, month, day))
            except ValueError:
                # Handle leap year issues
                continue
        return holidays
    
    @staticmethod
    def get_all_holidays(year: int) -> List[date]:
        """
        Get all holidays (fixed + estimated Islamic holidays) for Mauritania
        
        Args:
            year: Year to get holidays for
            
        Returns:
            List of all holiday dates
        """
        holidays = HolidayUtils.get_fixed_holidays(year)
        
        # Add estimated Islamic holidays (these would need proper Islamic calendar calculation)
        # These are approximations and should be replaced with proper Islamic calendar library
        islamic_holidays = HolidayUtils.get_estimated_islamic_holidays(year)
        holidays.extend(islamic_holidays)
        
        return sorted(holidays)
    
    @staticmethod
    def get_estimated_islamic_holidays(year: int) -> List[date]:
        """
        Get estimated Islamic holidays for a given year
        Note: This uses approximations. For production, use proper Islamic calendar library
        
        Args:
            year: Gregorian year
            
        Returns:
            List of estimated Islamic holiday dates
        """
        # These are rough estimates and should be replaced with proper calculations
        # Islamic calendar is lunar and shifts ~11 days earlier each Gregorian year
        
        # Base dates for 2024 (these would need to be updated annually)
        if year == 2024:
            return [
                date(2024, 4, 10),  # Eid al-Fitr (estimated)
                date(2024, 6, 16),  # Eid al-Adha (estimated)
                date(2024, 7, 7),   # Islamic New Year (estimated)
                date(2024, 9, 15),  # Mawlid al-Nabi (estimated)
            ]
        elif year == 2025:
            return [
                date(2025, 3, 30),  # Eid al-Fitr (estimated)
                date(2025, 6, 6),   # Eid al-Adha (estimated)
                date(2025, 6, 26),  # Islamic New Year (estimated)
                date(2025, 9, 4),   # Mawlid al-Nabi (estimated)
            ]
        else:
            # For other years, return empty list - would need proper calculation
            return []
    
    @staticmethod
    def get_company_specific_holidays(year: int, company_id: Optional[int] = None) -> List[date]:
        """
        Get company-specific holidays for a given year
        
        Args:
            year: Year to get holidays for
            company_id: Company identifier for custom holidays
            
        Returns:
            List of company-specific holiday dates
        """
        # This would be implemented to fetch from database or configuration
        # For now, return empty list
        return []
    
    @staticmethod
    def get_mauritanian_holidays_with_details(year: int) -> List[Dict[str, Any]]:
        """
        Get detailed holiday information for Mauritania
        
        Args:
            year: Year to get holidays for
            
        Returns:
            List of holiday dictionaries with details
        """
        holidays = []
        
        # Fixed holidays
        for (month, day), name in HolidayUtils.MAURITANIAN_FIXED_HOLIDAYS.items():
            try:
                holiday_date = date(year, month, day)
                holidays.append({
                    'date': holiday_date,
                    'name_fr': name,
                    'name_ar': HolidayUtils._get_arabic_holiday_name(name),
                    'type': 'fixed',
                    'is_working_day': False
                })
            except ValueError:
                continue
        
        # Islamic holidays
        islamic_holidays = IslamicCalendarUtils.get_islamic_holidays_for_year(year)
        for holiday_date, arabic_name, french_name in islamic_holidays:
            holidays.append({
                'date': holiday_date,
                'name_fr': french_name,
                'name_ar': arabic_name,
                'type': 'islamic',
                'is_working_day': False
            })
        
        return sorted(holidays, key=lambda x: x['date'])
    
    @staticmethod
    def _get_arabic_holiday_name(french_name: str) -> str:
        """
        Get Arabic translation of French holiday name
        
        Args:
            french_name: French holiday name
            
        Returns:
            Arabic holiday name
        """
        arabic_names = {
            "Nouvel An": "رأس السنة الميلادية",
            "Fête du Travail": "عيد العمال",
            "Fête de l'Afrique": "يوم أفريقيا",
            "Fête des Forces Armées": "عيد القوات المسلحة",
            "Fête de l'Indépendance": "عيد الاستقلال"
        }
        return arabic_names.get(french_name, french_name)
    
    @staticmethod
    def is_holiday(check_date: Union[date, datetime]) -> bool:
        """
        Check if a date is a holiday in Mauritania
        
        Args:
            check_date: Date to check
            
        Returns:
            True if it's a holiday
        """
        if isinstance(check_date, datetime):
            check_date = check_date.date()
        
        holidays = HolidayUtils.get_all_holidays(check_date.year)
        return check_date in holidays
    
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
        if isinstance(check_date, datetime):
            check_date = check_date.date()
        
        # Simplified estimation for Ramadan 2024/2025
        # In production, use proper Islamic calendar library like hijri-converter
        year = check_date.year
        
        if year == 2024:
            # Ramadan 2024: approximately March 11 - April 9
            ramadan_start = date(2024, 3, 11)
            ramadan_end = date(2024, 4, 9)
            return ramadan_start <= check_date <= ramadan_end
        elif year == 2025:
            # Ramadan 2025: approximately February 28 - March 29
            ramadan_start = date(2025, 2, 28)
            ramadan_end = date(2025, 3, 29)
            return ramadan_start <= check_date <= ramadan_end
        
        return False
    
    @staticmethod
    def get_holiday_name(check_date: Union[date, datetime], locale: str = "fr") -> Optional[str]:
        """
        Get the name of a holiday if the date is a holiday
        
        Args:
            check_date: Date to check
            locale: Language locale ("fr" for French, "ar" for Arabic)
            
        Returns:
            Holiday name if it's a holiday, None otherwise
        """
        if isinstance(check_date, datetime):
            check_date = check_date.date()
        
        # Check fixed holidays
        month_day = (check_date.month, check_date.day)
        if month_day in HolidayUtils.MAURITANIAN_FIXED_HOLIDAYS:
            holiday_name = HolidayUtils.MAURITANIAN_FIXED_HOLIDAYS[month_day]
            if locale == "ar":
                # Arabic translations (simplified)
                arabic_names = {
                    "Nouvel An": "رأس السنة الميلادية",
                    "Fête du Travail": "عيد العمال",
                    "Fête de l'Afrique": "يوم أفريقيا",
                    "Fête des Forces Armées": "عيد القوات المسلحة",
                    "Fête de l'Indépendance": "عيد الاستقلال"
                }
                return arabic_names.get(holiday_name, holiday_name)
            return holiday_name
        
        # Check if it's during Ramadan
        if HolidayUtils.is_ramadan_period(check_date):
            return "رمضان المبارك" if locale == "ar" else "Ramadan"
        
        return None


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
    
    @staticmethod
    def is_valid_document_date(document_date: Union[date, datetime],
                             document_type: str = "general") -> Tuple[bool, str]:
        """
        Validate document date (birth certificate, ID, etc.)
        
        Args:
            document_date: Document date to validate
            document_type: Type of document ("birth", "id", "contract", "general")
            
        Returns:
            Tuple of (is_valid, error_message)
        """
        if isinstance(document_date, datetime):
            document_date = document_date.date()
        
        today = date.today()
        
        if document_type == "birth":
            # Birth certificate cannot be in future
            if document_date > today:
                return False, "Date de naissance ne peut pas être dans le futur"
            
            # Cannot be more than 100 years old
            if DateCalculator.get_years_between(document_date, today) > 100:
                return False, "Date de naissance trop ancienne"
        
        elif document_type == "id":
            # ID cannot be in future
            if document_date > today:
                return False, "Date de délivrance ne peut pas être dans le futur"
        
        elif document_type == "contract":
            # Contract can be in near future (up to 6 months)
            if document_date > DateCalculator.add_months(today, 6):
                return False, "Date de contrat trop éloignée dans le futur"
        
        return True, ""


class SeniorityCalculator:
    """
    Seniority and anciennete calculation utilities
    Based on Java employee seniority calculations
    """
    
    @staticmethod
    def calculate_seniority_years(hire_date: Union[date, datetime],
                                reference_date: Union[date, datetime] = None) -> int:
        """
        Calculate seniority in complete years
        Equivalent to Java anciennete calculations
        
        Args:
            hire_date: Employee hire date
            reference_date: Reference date (defaults to today)
            
        Returns:
            Complete years of seniority
        """
        if isinstance(hire_date, datetime):
            hire_date = hire_date.date()
        if reference_date is None:
            reference_date = date.today()
        elif isinstance(reference_date, datetime):
            reference_date = reference_date.date()
        
        return DateCalculator.get_years_between(hire_date, reference_date)
    
    @staticmethod
    def calculate_seniority_months(hire_date: Union[date, datetime],
                                 reference_date: Union[date, datetime] = None) -> int:
        """
        Calculate seniority in complete months
        
        Args:
            hire_date: Employee hire date
            reference_date: Reference date (defaults to today)
            
        Returns:
            Complete months of seniority
        """
        if isinstance(hire_date, datetime):
            hire_date = hire_date.date()
        if reference_date is None:
            reference_date = date.today()
        elif isinstance(reference_date, datetime):
            reference_date = reference_date.date()
        
        return DateCalculator.get_months_between(hire_date, reference_date)
    
    @staticmethod
    def get_seniority_bonus_rate(seniority_years: int) -> float:
        """
        Calculate seniority bonus rate based on years of service
        Based on Mauritanian labor law and payroll practices
        
        Args:
            seniority_years: Years of seniority
            
        Returns:
            Bonus rate as percentage (0.0 to 1.0)
        """
        # Mauritanian seniority bonus structure (example)
        if seniority_years < 2:
            return 0.0
        elif seniority_years < 5:
            return 0.05  # 5% after 2 years
        elif seniority_years < 10:
            return 0.10  # 10% after 5 years
        elif seniority_years < 15:
            return 0.15  # 15% after 10 years
        elif seniority_years < 20:
            return 0.20  # 20% after 15 years
        else:
            return 0.25  # 25% after 20+ years
    
    @staticmethod
    def calculate_next_anniversary(hire_date: Union[date, datetime],
                                 reference_date: Union[date, datetime] = None) -> date:
        """
        Calculate next employment anniversary date
        
        Args:
            hire_date: Employee hire date
            reference_date: Reference date (defaults to today)
            
        Returns:
            Next anniversary date
        """
        if isinstance(hire_date, datetime):
            hire_date = hire_date.date()
        if reference_date is None:
            reference_date = date.today()
        elif isinstance(reference_date, datetime):
            reference_date = reference_date.date()
        
        # Calculate this year's anniversary
        current_year_anniversary = hire_date.replace(year=reference_date.year)
        
        # If this year's anniversary has passed, get next year's
        if current_year_anniversary <= reference_date:
            return hire_date.replace(year=reference_date.year + 1)
        else:
            return current_year_anniversary
    
    @staticmethod
    def calculate_enhanced_seniority(hire_date: Union[date, datetime],
                                   reference_date: Union[date, datetime] = None,
                                   include_fractional: bool = False) -> Dict[str, Union[int, float]]:
        """
        Calculate enhanced seniority information
        
        Args:
            hire_date: Employee hire date
            reference_date: Reference date (defaults to today)
            include_fractional: Whether to include fractional parts
            
        Returns:
            Dictionary with seniority details
        """
        if isinstance(hire_date, datetime):
            hire_date = hire_date.date()
        if reference_date is None:
            reference_date = date.today()
        elif isinstance(reference_date, datetime):
            reference_date = reference_date.date()
        
        years = SeniorityCalculator.calculate_seniority_years(hire_date, reference_date)
        months = SeniorityCalculator.calculate_seniority_months(hire_date, reference_date)
        days = DateCalculator.get_days_between(hire_date, reference_date)
        
        result = {
            'years': years,
            'months': months,
            'days': days,
            'total_months': months,
            'bonus_rate': SeniorityCalculator.get_seniority_bonus_rate(years),
            'next_anniversary': SeniorityCalculator.calculate_next_anniversary(hire_date, reference_date)
        }
        
        if include_fractional:
            result['fractional_years'] = days / 365.25
            result['fractional_months'] = days / 30.44  # Average days per month
        
        return result
    
    @staticmethod
    def get_milestone_dates(hire_date: Union[date, datetime]) -> List[Dict[str, Any]]:
        """
        Get employment milestone dates
        
        Args:
            hire_date: Employee hire date
            
        Returns:
            List of milestone information
        """
        if isinstance(hire_date, datetime):
            hire_date = hire_date.date()
        
        milestones = []
        milestone_years = [1, 2, 5, 10, 15, 20, 25, 30]
        
        for years in milestone_years:
            milestone_date = DateCalculator.add_years(hire_date, years)
            bonus_rate = SeniorityCalculator.get_seniority_bonus_rate(years)
            
            milestones.append({
                'years': years,
                'date': milestone_date,
                'bonus_rate': bonus_rate,
                'is_past': milestone_date <= date.today(),
                'description_fr': f"{years} ans de service",
                'description_ar': f"{years} سنوات من الخدمة"
            })
        
        return milestones


class LeaveCalculator:
    """
    Leave and vacation calculation utilities
    Based on Java 'conges' calculations
    """
    
    @staticmethod
    def calculate_annual_leave_days(seniority_years: int,
                                  base_days: int = 21) -> int:
        """
        Calculate annual leave entitlement based on seniority
        Based on Mauritanian labor law
        
        Args:
            seniority_years: Years of seniority
            base_days: Base annual leave days
            
        Returns:
            Total annual leave days
        """
        # Mauritanian leave calculation
        additional_days = 0
        
        # Additional days based on seniority
        if seniority_years >= 5:
            additional_days += 2
        if seniority_years >= 10:
            additional_days += 2
        if seniority_years >= 15:
            additional_days += 2
        
        return base_days + additional_days
    
    @staticmethod
    def calculate_leave_accrual(period_date: Union[date, datetime],
                              annual_days: int = 21) -> float:
        """
        Calculate leave accrual for a payroll period
        
        Args:
            period_date: Payroll period date
            annual_days: Annual leave days
            
        Returns:
            Leave days accrued for the period
        """
        # Monthly accrual = annual days / 12 months
        return annual_days / 12.0
    
    @staticmethod
    def calculate_leave_balance(hire_date: Union[date, datetime],
                              taken_days: float,
                              reference_date: Union[date, datetime] = None) -> float:
        """
        Calculate current leave balance
        
        Args:
            hire_date: Employee hire date
            taken_days: Days taken this year
            reference_date: Reference date (defaults to today)
            
        Returns:
            Current leave balance
        """
        if reference_date is None:
            reference_date = date.today()
        
        seniority_years = SeniorityCalculator.calculate_seniority_years(hire_date, reference_date)
        annual_entitlement = LeaveCalculator.calculate_annual_leave_days(seniority_years)
        
        # Calculate accrued days so far this year
        year_start = reference_date.replace(month=1, day=1)
        months_passed = DateCalculator.get_months_between(year_start, reference_date)
        accrued_days = (annual_entitlement / 12.0) * months_passed
        
        return accrued_days - taken_days
    
    @staticmethod
    def calculate_leave_compensation(daily_wage: float,
                                   leave_days: float,
                                   compensation_rate: float = 1.0) -> float:
        """
        Calculate leave compensation amount
        
        Args:
            daily_wage: Employee daily wage
            leave_days: Number of leave days
            compensation_rate: Compensation rate (1.0 = 100%)
            
        Returns:
            Leave compensation amount
        """
        return daily_wage * leave_days * compensation_rate
    
    @staticmethod
    def is_leave_period_valid(start_date: Union[date, datetime],
                            end_date: Union[date, datetime],
                            available_balance: float) -> Tuple[bool, str]:
        """
        Validate leave period request
        
        Args:
            start_date: Leave start date
            end_date: Leave end date
            available_balance: Available leave balance
            
        Returns:
            Tuple of (is_valid, error_message)
        """
        if isinstance(start_date, datetime):
            start_date = start_date.date()
        if isinstance(end_date, datetime):
            end_date = end_date.date()
        
        if start_date > end_date:
            return False, "La date de début doit être antérieure à la date de fin"
        
        if start_date < date.today():
            return False, "Les congés ne peuvent pas être pris dans le passé"
        
        requested_days = (end_date - start_date).days + 1
        
        if requested_days > available_balance:
            return False, f"Solde insuffisant. Demandé: {requested_days}, Disponible: {available_balance}"
        
        # Check minimum advance notice (e.g., 7 days)
        if (start_date - date.today()).days < 7:
            return False, "Un préavis d'au moins 7 jours est requis"
        
        return True, ""
    
    @staticmethod
    def calculate_enhanced_leave_balance(hire_date: Union[date, datetime],
                                       taken_days: float,
                                       carried_over: float = 0.0,
                                       reference_date: Union[date, datetime] = None) -> Dict[str, float]:
        """
        Calculate enhanced leave balance with detailed breakdown
        
        Args:
            hire_date: Employee hire date
            taken_days: Days taken this year
            carried_over: Days carried over from previous year
            reference_date: Reference date (defaults to today)
            
        Returns:
            Dictionary with detailed leave balance information
        """
        if reference_date is None:
            reference_date = date.today()
        
        seniority_years = SeniorityCalculator.calculate_seniority_years(hire_date, reference_date)
        annual_entitlement = LeaveCalculator.calculate_annual_leave_days(seniority_years)
        
        # Calculate accrued days so far this year
        year_start = reference_date.replace(month=1, day=1)
        months_passed = DateCalculator.get_months_between(year_start, reference_date)
        accrued_days = (annual_entitlement / 12.0) * months_passed
        
        # Calculate balance
        total_available = accrued_days + carried_over
        current_balance = total_available - taken_days
        
        return {
            'annual_entitlement': annual_entitlement,
            'accrued_days': accrued_days,
            'carried_over': carried_over,
            'total_available': total_available,
            'taken_days': taken_days,
            'current_balance': current_balance,
            'remaining_accrual': annual_entitlement - accrued_days,
            'seniority_years': seniority_years
        }
    
    @staticmethod
    def calculate_leave_projection(hire_date: Union[date, datetime],
                                 current_balance: float,
                                 target_date: Union[date, datetime]) -> Dict[str, float]:
        """
        Calculate projected leave balance for a future date
        
        Args:
            hire_date: Employee hire date
            current_balance: Current leave balance
            target_date: Target date for projection
            
        Returns:
            Dictionary with projected leave information
        """
        if isinstance(target_date, datetime):
            target_date = target_date.date()
        
        today = date.today()
        
        if target_date <= today:
            return {'projected_balance': current_balance, 'additional_accrual': 0.0}
        
        # Calculate months between now and target date
        months_to_target = DateCalculator.get_months_between(today, target_date)
        
        # Calculate seniority and annual entitlement
        seniority_years = SeniorityCalculator.calculate_seniority_years(hire_date, target_date)
        annual_entitlement = LeaveCalculator.calculate_annual_leave_days(seniority_years)
        
        # Calculate additional accrual
        additional_accrual = (annual_entitlement / 12.0) * months_to_target
        projected_balance = current_balance + additional_accrual
        
        return {
            'projected_balance': projected_balance,
            'additional_accrual': additional_accrual,
            'months_to_target': months_to_target,
            'annual_entitlement': annual_entitlement
        }


class BusinessDateCalculator:
    """
    Advanced business date calculations for Mauritanian payroll
    Handles complex business logic and document lifecycle management
    """
    
    @staticmethod
    def calculate_contract_expiration(start_date: Union[date, datetime],
                                    duration_months: int,
                                    auto_renew: bool = False) -> Dict[str, Any]:
        """
        Calculate contract expiration dates and renewal information
        
        Args:
            start_date: Contract start date
            duration_months: Contract duration in months
            auto_renew: Whether contract auto-renews
            
        Returns:
            Dictionary with contract date information
        """
        if isinstance(start_date, datetime):
            start_date = start_date.date()
        
        end_date = DateCalculator.add_months(start_date, duration_months)
        today = date.today()
        
        # Calculate notice periods
        notice_30_days = DateCalculator.add_days(end_date, -30)
        notice_60_days = DateCalculator.add_days(end_date, -60)
        notice_90_days = DateCalculator.add_days(end_date, -90)
        
        # Determine status
        days_to_expiry = DateCalculator.get_days_between(today, end_date)
        
        if days_to_expiry < 0:
            status = "expired"
        elif days_to_expiry <= 30:
            status = "critical"
        elif days_to_expiry <= 60:
            status = "warning"
        elif days_to_expiry <= 90:
            status = "notice"
        else:
            status = "active"
        
        return {
            'start_date': start_date,
            'end_date': end_date,
            'duration_months': duration_months,
            'days_to_expiry': days_to_expiry,
            'status': status,
            'auto_renew': auto_renew,
            'notice_dates': {
                '30_days': notice_30_days,
                '60_days': notice_60_days,
                '90_days': notice_90_days
            },
            'next_renewal_date': DateCalculator.add_months(end_date, duration_months) if auto_renew else None
        }
    
    @staticmethod
    def calculate_age_with_business_logic(birth_date: Union[date, datetime],
                                        reference_date: Union[date, datetime] = None) -> Dict[str, Any]:
        """
        Calculate age with additional business logic for payroll
        
        Args:
            birth_date: Birth date
            reference_date: Reference date (defaults to today)
            
        Returns:
            Dictionary with age information and business logic
        """
        if isinstance(birth_date, datetime):
            birth_date = birth_date.date()
        if reference_date is None:
            reference_date = date.today()
        elif isinstance(reference_date, datetime):
            reference_date = reference_date.date()
        
        age_years = DateCalculator.get_years_between(birth_date, reference_date)
        age_months = DateCalculator.get_months_between(birth_date, reference_date)
        age_days = DateCalculator.get_days_between(birth_date, reference_date)
        
        # Business logic for different age categories
        category = "adult"
        if age_years < 16:
            category = "minor"
        elif age_years < 18:
            category = "youth"
        elif age_years >= 60:
            category = "senior"
        
        # Calculate next birthday
        next_birthday = birth_date.replace(year=reference_date.year)
        if next_birthday <= reference_date:
            next_birthday = birth_date.replace(year=reference_date.year + 1)
        
        return {
            'age_years': age_years,
            'age_months': age_months,
            'age_days': age_days,
            'category': category,
            'is_minor': age_years < 18,
            'is_retirement_age': age_years >= 60,
            'next_birthday': next_birthday,
            'days_to_birthday': DateCalculator.get_days_between(reference_date, next_birthday)
        }
    
    @staticmethod
    def calculate_payroll_cutoff_dates(period_date: Union[date, datetime],
                                     cutoff_day: int = 25) -> Dict[str, date]:
        """
        Calculate payroll processing cutoff dates
        
        Args:
            period_date: Payroll period date
            cutoff_day: Day of month for cutoff (default 25th)
            
        Returns:
            Dictionary with cutoff dates
        """
        if isinstance(period_date, datetime):
            period_date = period_date.date()
        
        period_start, period_end = PayrollPeriodUtils.get_period_start_end(period_date)
        
        # Data cutoff date (usually 25th of the month)
        try:
            data_cutoff = period_date.replace(day=cutoff_day)
        except ValueError:
            # Handle months with fewer days
            data_cutoff = period_date.replace(day=calendar.monthrange(period_date.year, period_date.month)[1])
        
        # Processing dates
        processing_start = DateCalculator.add_days(data_cutoff, 1)
        pay_date = DateCalculator.add_days(period_end, 5)  # 5 days after period end
        
        # Ensure pay date is a working day
        while WorkingDayCalculator.is_weekend(pay_date):
            pay_date = DateCalculator.add_days(pay_date, 1)
        
        return {
            'period_start': period_start,
            'period_end': period_end,
            'data_cutoff': data_cutoff,
            'processing_start': processing_start,
            'pay_date': pay_date,
            'reporting_deadline': DateCalculator.add_days(pay_date, 10)
        }