"""
Tests for core.utils.date_utils module.

This module provides comprehensive test coverage for all date utility classes
including DateCalculator, PayrollPeriodUtils, DateFormatter, WorkingDayCalculator,
HolidayUtils, and DateValidation.
"""

import pytest
from datetime import date, datetime, timedelta
from unittest.mock import patch

from core.utils.date_utils import (
    DateCalculator,
    PayrollPeriodUtils,
    DateFormatter,
    WorkingDayCalculator,
    HolidayUtils,
    DateValidation
)


class TestDateCalculator:
    """Test DateCalculator class methods"""
    
    def test_add_days_with_date(self):
        """Test adding days to a date object"""
        base_date = date(2023, 6, 15)
        
        # Add positive days
        result = DateCalculator.add_days(base_date, 10)
        expected = date(2023, 6, 25)
        assert result == expected
        
        # Add negative days (subtract)
        result = DateCalculator.add_days(base_date, -10)
        expected = date(2023, 6, 5)
        assert result == expected
        
        # Add zero days
        result = DateCalculator.add_days(base_date, 0)
        assert result == base_date
    
    def test_add_days_with_datetime(self):
        """Test adding days to a datetime object"""
        base_datetime = datetime(2023, 6, 15, 14, 30, 0)
        
        result = DateCalculator.add_days(base_datetime, 5)
        expected = date(2023, 6, 20)
        assert result == expected
        assert isinstance(result, date)
    
    def test_add_months_with_date(self):
        """Test adding months to a date"""
        base_date = date(2023, 6, 15)
        
        # Add positive months
        result = DateCalculator.add_months(base_date, 3)
        expected = date(2023, 9, 15)
        assert result == expected
        
        # Add negative months (subtract)
        result = DateCalculator.add_months(base_date, -2)
        expected = date(2023, 4, 15)
        assert result == expected
        
        # Cross year boundary
        result = DateCalculator.add_months(base_date, 8)
        expected = date(2024, 2, 15)
        assert result == expected
    
    def test_add_months_with_datetime(self):
        """Test adding months to a datetime object"""
        base_datetime = datetime(2023, 6, 15, 14, 30, 0)
        
        result = DateCalculator.add_months(base_datetime, 1)
        expected = date(2023, 7, 15)
        assert result == expected
        assert isinstance(result, date)
    
    def test_add_years_with_date(self):
        """Test adding years to a date"""
        base_date = date(2023, 6, 15)
        
        # Add positive years
        result = DateCalculator.add_years(base_date, 2)
        expected = date(2025, 6, 15)
        assert result == expected
        
        # Add negative years (subtract)
        result = DateCalculator.add_years(base_date, -1)
        expected = date(2022, 6, 15)
        assert result == expected
        
        # Test leap year handling
        leap_date = date(2020, 2, 29)
        result = DateCalculator.add_years(leap_date, 1)
        expected = date(2021, 2, 28)  # February 29 doesn't exist in 2021
        assert result == expected
    
    def test_add_years_with_datetime(self):
        """Test adding years to a datetime object"""
        base_datetime = datetime(2023, 6, 15, 14, 30, 0)
        
        result = DateCalculator.add_years(base_datetime, 5)
        expected = date(2028, 6, 15)
        assert result == expected
        assert isinstance(result, date)
    
    def test_get_days_between_dates(self):
        """Test calculating days between two dates"""
        start_date = date(2023, 6, 1)
        end_date = date(2023, 6, 15)
        
        result = DateCalculator.get_days_between(start_date, end_date)
        assert result == 14
        
        # Reverse order (negative result)
        result = DateCalculator.get_days_between(end_date, start_date)
        assert result == -14
        
        # Same dates
        result = DateCalculator.get_days_between(start_date, start_date)
        assert result == 0
    
    def test_get_days_between_datetime_objects(self):
        """Test calculating days between datetime objects"""
        start_datetime = datetime(2023, 6, 1, 10, 0, 0)
        end_datetime = datetime(2023, 6, 5, 15, 30, 0)
        
        result = DateCalculator.get_days_between(start_datetime, end_datetime)
        assert result == 4
    
    def test_get_months_between(self):
        """Test calculating months between dates"""
        start_date = date(2023, 1, 15)
        end_date = date(2023, 6, 15)
        
        result = DateCalculator.get_months_between(start_date, end_date)
        assert result == 5
        
        # Cross year boundary
        start_date = date(2022, 10, 15)
        end_date = date(2023, 3, 15)
        result = DateCalculator.get_months_between(start_date, end_date)
        assert result == 5
        
        # Negative result
        result = DateCalculator.get_months_between(end_date, start_date)
        assert result == -5
    
    def test_get_months_between_datetime_objects(self):
        """Test calculating months between datetime objects"""
        start_datetime = datetime(2023, 1, 15, 10, 0, 0)
        end_datetime = datetime(2023, 4, 15, 15, 30, 0)
        
        result = DateCalculator.get_months_between(start_datetime, end_datetime)
        assert result == 3
    
    def test_get_years_between(self):
        """Test calculating complete years between dates"""
        start_date = date(2020, 6, 15)
        end_date = date(2023, 6, 15)
        
        # Exact years
        result = DateCalculator.get_years_between(start_date, end_date)
        assert result == 3
        
        # Birthday hasn't occurred yet
        end_date = date(2023, 6, 14)
        result = DateCalculator.get_years_between(start_date, end_date)
        assert result == 2
        
        # Birthday has occurred
        end_date = date(2023, 6, 16)
        result = DateCalculator.get_years_between(start_date, end_date)
        assert result == 3
        
        # Same year
        start_date = date(2023, 1, 1)
        end_date = date(2023, 12, 31)
        result = DateCalculator.get_years_between(start_date, end_date)
        assert result == 0
    
    def test_get_years_between_datetime_objects(self):
        """Test calculating years between datetime objects"""
        start_datetime = datetime(2020, 6, 15, 10, 0, 0)
        end_datetime = datetime(2023, 6, 15, 15, 30, 0)
        
        result = DateCalculator.get_years_between(start_datetime, end_datetime)
        assert result == 3


class TestPayrollPeriodUtils:
    """Test PayrollPeriodUtils class methods"""
    
    def test_get_period_start_end(self):
        """Test getting period start and end dates"""
        # Mid-month date
        period_date = date(2023, 6, 15)
        start, end = PayrollPeriodUtils.get_period_start_end(period_date)
        
        assert start == date(2023, 6, 1)
        assert end == date(2023, 6, 30)
        
        # First day of month
        period_date = date(2023, 2, 1)
        start, end = PayrollPeriodUtils.get_period_start_end(period_date)
        
        assert start == date(2023, 2, 1)
        assert end == date(2023, 2, 28)  # Non-leap year
        
        # Last day of month
        period_date = date(2023, 12, 31)
        start, end = PayrollPeriodUtils.get_period_start_end(period_date)
        
        assert start == date(2023, 12, 1)
        assert end == date(2023, 12, 31)
    
    def test_get_period_start_end_datetime(self):
        """Test getting period dates with datetime input"""
        period_datetime = datetime(2023, 6, 15, 14, 30, 0)
        start, end = PayrollPeriodUtils.get_period_start_end(period_datetime)
        
        assert start == date(2023, 6, 1)
        assert end == date(2023, 6, 30)
        assert isinstance(start, date)
        assert isinstance(end, date)
    
    def test_get_period_start_end_leap_year(self):
        """Test period calculation in leap year"""
        period_date = date(2020, 2, 15)  # Leap year
        start, end = PayrollPeriodUtils.get_period_start_end(period_date)
        
        assert start == date(2020, 2, 1)
        assert end == date(2020, 2, 29)
    
    def test_get_previous_period(self):
        """Test getting previous payroll period"""
        current_period = date(2023, 6, 15)
        prev_period = PayrollPeriodUtils.get_previous_period(current_period)
        
        assert prev_period == date(2023, 5, 28)
        
        # January (cross year boundary)
        current_period = date(2023, 1, 15)
        prev_period = PayrollPeriodUtils.get_previous_period(current_period)
        
        assert prev_period == date(2022, 12, 28)
    
    def test_get_previous_period_datetime(self):
        """Test getting previous period with datetime input"""
        current_datetime = datetime(2023, 6, 15, 14, 30, 0)
        prev_period = PayrollPeriodUtils.get_previous_period(current_datetime)
        
        assert prev_period == date(2023, 5, 28)
        assert isinstance(prev_period, date)
    
    def test_get_next_period(self):
        """Test getting next payroll period"""
        current_period = date(2023, 6, 15)
        next_period = PayrollPeriodUtils.get_next_period(current_period)
        
        assert next_period == date(2023, 7, 28)
        
        # December (cross year boundary)
        current_period = date(2023, 12, 15)
        next_period = PayrollPeriodUtils.get_next_period(current_period)
        
        assert next_period == date(2024, 1, 28)
    
    def test_get_next_period_datetime(self):
        """Test getting next period with datetime input"""
        current_datetime = datetime(2023, 6, 15, 14, 30, 0)
        next_period = PayrollPeriodUtils.get_next_period(current_datetime)
        
        assert next_period == date(2023, 7, 28)
        assert isinstance(next_period, date)
    
    def test_get_period_working_days_exclude_weekends(self):
        """Test calculating working days excluding weekends"""
        # June 2023: starts on Thursday, ends on Friday
        period_date = date(2023, 6, 15)
        working_days = PayrollPeriodUtils.get_period_working_days(
            period_date, exclude_weekends=True
        )
        
        # June 2023 has 22 working days (30 days - 8 weekend days)
        assert working_days == 22
    
    def test_get_period_working_days_include_weekends(self):
        """Test calculating working days including weekends"""
        period_date = date(2023, 6, 15)
        working_days = PayrollPeriodUtils.get_period_working_days(
            period_date, exclude_weekends=False
        )
        
        # June 2023 has 30 days total
        assert working_days == 30
    
    def test_get_period_working_days_with_holidays(self):
        """Test calculating working days with holidays"""
        period_date = date(2023, 5, 15)
        holidays = [date(2023, 5, 1)]  # Labor Day
        
        working_days = PayrollPeriodUtils.get_period_working_days(
            period_date, exclude_weekends=True, holidays=holidays
        )
        
        # May 2023 has 23 working days minus 1 holiday = 22
        assert working_days == 22
    
    def test_get_period_working_days_datetime(self):
        """Test working days calculation with datetime input"""
        period_datetime = datetime(2023, 6, 15, 14, 30, 0)
        working_days = PayrollPeriodUtils.get_period_working_days(
            period_datetime, exclude_weekends=True
        )
        
        assert working_days == 22


class TestDateFormatter:
    """Test DateFormatter class methods"""
    
    def test_format_for_display_standard(self):
        """Test standard date formatting"""
        test_date = date(2023, 6, 15)
        
        result = DateFormatter.format_for_display(test_date, "standard")
        assert result == "15/06/2023"
        
        # Default format (should be standard)
        result = DateFormatter.format_for_display(test_date)
        assert result == "15/06/2023"
    
    def test_format_for_display_short(self):
        """Test short date formatting"""
        test_date = date(2023, 6, 15)
        
        result = DateFormatter.format_for_display(test_date, "short")
        assert result == "15/06/23"
    
    def test_format_for_display_long(self):
        """Test long date formatting"""
        test_date = date(2023, 6, 15)
        
        result = DateFormatter.format_for_display(test_date, "long")
        assert result == "15 June 2023"
    
    def test_format_for_display_french(self):
        """Test French date formatting"""
        test_date = date(2023, 6, 15)
        
        result = DateFormatter.format_for_display(test_date, "french")
        assert result == "15 juin 2023"
        
        # Test all months
        for month, expected_name in enumerate([
            "janvier", "février", "mars", "avril", "mai", "juin",
            "juillet", "août", "septembre", "octobre", "novembre", "décembre"
        ], 1):
            test_date = date(2023, month, 1)
            result = DateFormatter.format_for_display(test_date, "french")
            assert expected_name in result
    
    def test_format_for_display_iso(self):
        """Test ISO date formatting"""
        test_date = date(2023, 6, 15)
        
        result = DateFormatter.format_for_display(test_date, "iso")
        assert result == "2023-06-15"
    
    def test_format_for_display_datetime(self):
        """Test formatting datetime objects"""
        test_datetime = datetime(2023, 6, 15, 14, 30, 0)
        
        result = DateFormatter.format_for_display(test_datetime, "standard")
        assert result == "15/06/2023"
    
    def test_format_for_display_none(self):
        """Test formatting None date"""
        result = DateFormatter.format_for_display(None)
        assert result == ""
        
        result = DateFormatter.format_for_display(None, "french")
        assert result == ""
    
    def test_format_for_display_invalid_format(self):
        """Test formatting with invalid format type"""
        test_date = date(2023, 6, 15)
        
        result = DateFormatter.format_for_display(test_date, "invalid")
        assert result == "15/06/2023"  # Should default to standard
    
    def test_format_for_reports_date_only(self):
        """Test report formatting for date objects"""
        test_date = date(2023, 6, 15)
        
        result = DateFormatter.format_for_reports(test_date)
        assert result == "15 juin 2023"
        
        result = DateFormatter.format_for_reports(test_date, include_time=False)
        assert result == "15 juin 2023"
    
    def test_format_for_reports_datetime_no_time(self):
        """Test report formatting for datetime without time"""
        test_datetime = datetime(2023, 6, 15, 14, 30, 0)
        
        result = DateFormatter.format_for_reports(test_datetime, include_time=False)
        assert result == "15 juin 2023"
    
    def test_format_for_reports_datetime_with_time(self):
        """Test report formatting for datetime with time"""
        test_datetime = datetime(2023, 6, 15, 14, 30, 0)
        
        result = DateFormatter.format_for_reports(test_datetime, include_time=True)
        assert result == "15 June 2023 à 14:30"
    
    def test_format_for_reports_none(self):
        """Test report formatting for None"""
        result = DateFormatter.format_for_reports(None)
        assert result == ""
        
        result = DateFormatter.format_for_reports(None, include_time=True)
        assert result == ""
    
    def test_format_period_range_different_dates(self):
        """Test formatting date ranges with different dates"""
        start_date = date(2023, 6, 1)
        end_date = date(2023, 6, 30)
        
        result = DateFormatter.format_period_range(start_date, end_date)
        assert result == "Du 1 juin 2023 au 30 juin 2023"
    
    def test_format_period_range_same_dates(self):
        """Test formatting date ranges with same dates"""
        same_date = date(2023, 6, 15)
        
        result = DateFormatter.format_period_range(same_date, same_date)
        assert result == "15 juin 2023"
    
    def test_format_period_range_datetime_objects(self):
        """Test formatting date ranges with datetime objects"""
        start_datetime = datetime(2023, 6, 1, 10, 0, 0)
        end_datetime = datetime(2023, 6, 30, 18, 0, 0)
        
        result = DateFormatter.format_period_range(start_datetime, end_datetime)
        assert result == "Du 1 juin 2023 au 30 juin 2023"


class TestWorkingDayCalculator:
    """Test WorkingDayCalculator class methods"""
    
    def test_is_working_day_default_schedule(self):
        """Test working day check with default schedule (Mon-Fri)"""
        # Monday
        monday = date(2023, 6, 5)
        assert WorkingDayCalculator.is_working_day(monday) is True
        
        # Friday
        friday = date(2023, 6, 9)
        assert WorkingDayCalculator.is_working_day(friday) is True
        
        # Saturday
        saturday = date(2023, 6, 10)
        assert WorkingDayCalculator.is_working_day(saturday) is False
        
        # Sunday
        sunday = date(2023, 6, 11)
        assert WorkingDayCalculator.is_working_day(sunday) is False
    
    def test_is_working_day_custom_schedule(self):
        """Test working day check with custom schedule"""
        # Custom schedule: Tuesday-Saturday
        custom_schedule = {0: False, 1: True, 2: True, 3: True, 4: True, 5: True, 6: False}
        
        # Monday (not working in custom schedule)
        monday = date(2023, 6, 5)
        assert WorkingDayCalculator.is_working_day(monday, custom_schedule) is False
        
        # Tuesday (working in custom schedule)
        tuesday = date(2023, 6, 6)
        assert WorkingDayCalculator.is_working_day(tuesday, custom_schedule) is True
        
        # Saturday (working in custom schedule)
        saturday = date(2023, 6, 10)
        assert WorkingDayCalculator.is_working_day(saturday, custom_schedule) is True
    
    def test_is_working_day_with_holidays(self):
        """Test working day check with holidays"""
        # Monday that is a holiday
        monday_holiday = date(2023, 7, 10)  # Mauritanian Armed Forces Day
        holidays = [monday_holiday]
        
        assert WorkingDayCalculator.is_working_day(monday_holiday, holidays=holidays) is False
        
        # Regular Monday (no holiday)
        regular_monday = date(2023, 6, 5)
        assert WorkingDayCalculator.is_working_day(regular_monday, holidays=holidays) is True
    
    def test_is_working_day_datetime(self):
        """Test working day check with datetime input"""
        monday_datetime = datetime(2023, 6, 5, 14, 30, 0)
        assert WorkingDayCalculator.is_working_day(monday_datetime) is True
        
        saturday_datetime = datetime(2023, 6, 10, 14, 30, 0)
        assert WorkingDayCalculator.is_working_day(saturday_datetime) is False
    
    def test_get_working_days_in_range_default(self):
        """Test getting working days in range with default schedule"""
        # Week of June 5-11, 2023 (Mon-Sun)
        start_date = date(2023, 6, 5)
        end_date = date(2023, 6, 11)
        
        working_days = WorkingDayCalculator.get_working_days_in_range(start_date, end_date)
        
        # Should return Mon-Fri (5 days)
        assert len(working_days) == 5
        assert working_days[0] == date(2023, 6, 5)  # Monday
        assert working_days[-1] == date(2023, 6, 9)  # Friday
    
    def test_get_working_days_in_range_custom_schedule(self):
        """Test getting working days with custom schedule"""
        # Custom schedule: only Tuesday and Thursday
        custom_schedule = {0: False, 1: True, 2: False, 3: True, 4: False, 5: False, 6: False}
        
        start_date = date(2023, 6, 5)  # Monday
        end_date = date(2023, 6, 11)   # Sunday
        
        working_days = WorkingDayCalculator.get_working_days_in_range(
            start_date, end_date, custom_schedule
        )
        
        # Should return only Tuesday and Thursday
        assert len(working_days) == 2
        assert working_days[0] == date(2023, 6, 6)  # Tuesday
        assert working_days[1] == date(2023, 6, 8)  # Thursday
    
    def test_get_working_days_in_range_with_holidays(self):
        """Test getting working days with holidays"""
        start_date = date(2023, 6, 5)  # Monday
        end_date = date(2023, 6, 9)    # Friday
        holidays = [date(2023, 6, 7)]  # Wednesday
        
        working_days = WorkingDayCalculator.get_working_days_in_range(
            start_date, end_date, holidays=holidays
        )
        
        # Should return Mon, Tue, Thu, Fri (4 days, excluding Wed holiday)
        assert len(working_days) == 4
        assert date(2023, 6, 7) not in working_days
    
    def test_get_working_days_in_range_datetime(self):
        """Test getting working days with datetime inputs"""
        start_datetime = datetime(2023, 6, 5, 10, 0, 0)
        end_datetime = datetime(2023, 6, 9, 18, 0, 0)
        
        working_days = WorkingDayCalculator.get_working_days_in_range(
            start_datetime, end_datetime
        )
        
        assert len(working_days) == 5
        assert all(isinstance(day, date) for day in working_days)
    
    def test_get_working_days_in_range_single_day(self):
        """Test getting working days for single day range"""
        single_date = date(2023, 6, 5)  # Monday
        
        working_days = WorkingDayCalculator.get_working_days_in_range(
            single_date, single_date
        )
        
        assert len(working_days) == 1
        assert working_days[0] == single_date


class TestHolidayUtils:
    """Test HolidayUtils class methods"""
    
    def test_get_fixed_holidays(self):
        """Test getting fixed holidays for Mauritania"""
        year = 2023
        holidays = HolidayUtils.get_fixed_holidays(year)
        
        expected_holidays = [
            date(2023, 1, 1),   # New Year's Day
            date(2023, 5, 1),   # Labor Day
            date(2023, 7, 10),  # Mauritanian Armed Forces Day
            date(2023, 11, 28), # Independence Day
        ]
        
        assert len(holidays) == 4
        assert holidays == expected_holidays
    
    def test_get_fixed_holidays_different_year(self):
        """Test getting fixed holidays for different year"""
        year = 2024
        holidays = HolidayUtils.get_fixed_holidays(year)
        
        # Check that the year is correct
        assert all(holiday.year == 2024 for holiday in holidays)
        
        # Check specific dates
        assert date(2024, 1, 1) in holidays
        assert date(2024, 5, 1) in holidays
        assert date(2024, 7, 10) in holidays
        assert date(2024, 11, 28) in holidays
    
    def test_is_ramadan_period(self):
        """Test Ramadan period check (placeholder implementation)"""
        test_date = date(2023, 6, 15)
        
        # Current implementation always returns False (placeholder)
        result = HolidayUtils.is_ramadan_period(test_date)
        assert result is False
    
    def test_is_ramadan_period_datetime(self):
        """Test Ramadan period check with datetime input"""
        test_datetime = datetime(2023, 6, 15, 14, 30, 0)
        
        result = HolidayUtils.is_ramadan_period(test_datetime)
        assert result is False


class TestDateValidation:
    """Test DateValidation class methods"""
    
    def test_is_valid_hire_date_valid(self):
        """Test valid hire date scenarios"""
        # Valid hire date (yesterday)
        yesterday = date.today() - timedelta(days=1)
        is_valid, message = DateValidation.is_valid_hire_date(yesterday)
        
        assert is_valid is True
        assert message == ""
    
    def test_is_valid_hire_date_future(self):
        """Test hire date in the future"""
        tomorrow = date.today() + timedelta(days=1)
        is_valid, message = DateValidation.is_valid_hire_date(tomorrow)
        
        assert is_valid is False
        assert "futur" in message.lower()
    
    def test_is_valid_hire_date_too_old(self):
        """Test hire date too far in the past"""
        old_date = date.today() - timedelta(days=365 * 51)  # 51 years ago
        is_valid, message = DateValidation.is_valid_hire_date(old_date)
        
        assert is_valid is False
        assert "ancienne" in message.lower()
    
    def test_is_valid_hire_date_with_birth_date_valid(self):
        """Test valid hire date with birth date"""
        birth_date = date(1990, 1, 1)
        hire_date = date(2010, 1, 1)  # 20 years old at hire
        
        is_valid, message = DateValidation.is_valid_hire_date(hire_date, birth_date)
        
        assert is_valid is True
        assert message == ""
    
    def test_is_valid_hire_date_with_birth_date_too_young(self):
        """Test hire date with employee too young"""
        birth_date = date(2010, 1, 1)
        hire_date = date(2020, 1, 1)  # 10 years old at hire
        
        is_valid, message = DateValidation.is_valid_hire_date(hire_date, birth_date)
        
        assert is_valid is False
        assert "16 ans" in message
    
    def test_is_valid_hire_date_datetime(self):
        """Test hire date validation with datetime inputs"""
        yesterday_datetime = datetime.combine(
            date.today() - timedelta(days=1), 
            datetime.min.time()
        )
        
        is_valid, message = DateValidation.is_valid_hire_date(yesterday_datetime)
        
        assert is_valid is True
        assert message == ""
    
    def test_is_valid_period_date_valid(self):
        """Test valid period date scenarios"""
        # Current month
        current_date = date.today()
        is_valid, message = DateValidation.is_valid_period_date(current_date)
        
        assert is_valid is True
        assert message == ""
        
        # 6 months ago
        six_months_ago = DateCalculator.add_months(current_date, -6)
        is_valid, message = DateValidation.is_valid_period_date(six_months_ago)
        
        assert is_valid is True
        assert message == ""
    
    def test_is_valid_period_date_too_old(self):
        """Test period date too far in the past"""
        old_date = DateCalculator.add_years(date.today(), -3)
        is_valid, message = DateValidation.is_valid_period_date(old_date)
        
        assert is_valid is False
        assert "ancienne" in message.lower()
    
    def test_is_valid_period_date_too_future(self):
        """Test period date too far in the future"""
        future_date = DateCalculator.add_years(date.today(), 2)
        is_valid, message = DateValidation.is_valid_period_date(future_date)
        
        assert is_valid is False
        assert "futur" in message.lower()
    
    def test_is_valid_period_date_datetime(self):
        """Test period date validation with datetime input"""
        current_datetime = datetime.now()
        is_valid, message = DateValidation.is_valid_period_date(current_datetime)
        
        assert is_valid is True
        assert message == ""


class TestDateUtilsIntegration:
    """Integration tests for date utilities"""
    
    def test_payroll_workflow_integration(self):
        """Test complete payroll date workflow"""
        # Set up payroll period
        current_date = date(2023, 6, 15)
        period_start, period_end = PayrollPeriodUtils.get_period_start_end(current_date)
        
        # Validate period
        is_valid, _ = DateValidation.is_valid_period_date(current_date)
        assert is_valid is True
        
        # Calculate working days
        working_days = PayrollPeriodUtils.get_period_working_days(current_date)
        assert working_days > 0
        
        # Format for display
        period_display = DateFormatter.format_period_range(period_start, period_end)
        assert "juin 2023" in period_display
        
        # Get holidays
        holidays = HolidayUtils.get_fixed_holidays(2023)
        assert len(holidays) == 4
    
    def test_employee_hire_date_workflow(self):
        """Test employee hire date validation workflow"""
        birth_date = date(1985, 3, 15)
        hire_date = date(2010, 6, 1)
        
        # Validate hire date
        is_valid, message = DateValidation.is_valid_hire_date(hire_date, birth_date)
        assert is_valid is True
        
        # Calculate years of service
        today = date.today()
        years_service = DateCalculator.get_years_between(hire_date, today)
        assert years_service >= 0
        
        # Format hire date for display
        hire_display = DateFormatter.format_for_display(hire_date, "french")
        assert "juin 2010" in hire_display
    
    def test_working_day_calculation_with_holidays(self):
        """Test working day calculations with holidays"""
        # June 2023 period
        period_date = date(2023, 6, 15)
        holidays = HolidayUtils.get_fixed_holidays(2023)
        
        # Get working days in range
        period_start, period_end = PayrollPeriodUtils.get_period_start_end(period_date)
        working_days_list = WorkingDayCalculator.get_working_days_in_range(
            period_start, period_end, holidays=holidays
        )
        
        # Calculate working days count
        working_days_count = PayrollPeriodUtils.get_period_working_days(
            period_date, exclude_weekends=True, holidays=holidays
        )
        
        assert len(working_days_list) == working_days_count
        
        # Verify no holidays in working days
        for holiday in holidays:
            if period_start <= holiday <= period_end:
                assert holiday not in working_days_list