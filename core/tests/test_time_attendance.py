import pytest
from django.test import TestCase
from django.core.exceptions import ValidationError
from django.db import IntegrityError
from decimal import Decimal
from datetime import date, datetime, time
from django.utils import timezone

from core.models import (
    TimeClockData, DailyWork, WeeklyOvertime, WorkWeek, Employee,
    GeneralDirection, Direction, Department, Position
)


class TimeClockDataTestCase(TestCase):
    """Test cases for TimeClockData model"""
    
    def setUp(self):
        """Set up test data"""
        # Create organizational structure
        self.general_direction = GeneralDirection.objects.create(
            name="Test General Direction"
        )
        self.direction = Direction.objects.create(
            name="Test Direction",
            general_direction=self.general_direction
        )
        self.department = Department.objects.create(
            name="Test Department",
            direction=self.direction
        )
        self.position = Position.objects.create(
            name="Test Position"
        )
        
        # Create test employee
        self.employee = Employee.objects.create(
            first_name="John",
            last_name="Doe",
            hire_date=date(2023, 1, 1),
            position=self.position,
            department=self.department,
            direction=self.direction,
            general_direction=self.general_direction
        )
        
        self.test_timestamp = timezone.now()
    
    def test_timeclock_data_creation(self):
        """Test creating a TimeClockData record"""
        timeclock = TimeClockData.objects.create(
            employee=self.employee,
            timestamp=self.test_timestamp,
            punch_type='I',
            is_imported=False
        )
        
        self.assertEqual(timeclock.employee, self.employee)
        self.assertEqual(timeclock.timestamp, self.test_timestamp)
        self.assertEqual(timeclock.punch_type, 'I')
        self.assertFalse(timeclock.is_imported)
    
    def test_timeclock_data_str_representation(self):
        """Test string representation of TimeClockData"""
        timeclock = TimeClockData.objects.create(
            employee=self.employee,
            timestamp=self.test_timestamp,
            punch_type='I'
        )
        
        expected_str = f"{self.employee.full_name} - {self.test_timestamp.strftime('%Y-%m-%d %H:%M')} (IN)"
        self.assertEqual(str(timeclock), expected_str)
    
    def test_timeclock_data_punch_type_choices(self):
        """Test punch type choices validation"""
        # Valid punch types
        for punch_type in ['I', 'O']:
            timeclock = TimeClockData.objects.create(
                employee=self.employee,
                timestamp=self.test_timestamp,
                punch_type=punch_type
            )
            self.assertEqual(timeclock.punch_type, punch_type)
    
    def test_timeclock_data_cascade_delete(self):
        """Test that TimeClockData is deleted when employee is deleted"""
        timeclock = TimeClockData.objects.create(
            employee=self.employee,
            timestamp=self.test_timestamp,
            punch_type='I'
        )
        
        employee_id = self.employee.id
        self.employee.delete()
        
        # TimeClockData should be deleted due to CASCADE
        self.assertFalse(TimeClockData.objects.filter(id=timeclock.id).exists())
    
    def test_timeclock_data_ordering(self):
        """Test default ordering by timestamp (descending)"""
        timestamp1 = timezone.now()
        timestamp2 = timestamp1.replace(hour=timestamp1.hour + 1)
        
        timeclock1 = TimeClockData.objects.create(
            employee=self.employee,
            timestamp=timestamp1,
            punch_type='I'
        )
        timeclock2 = TimeClockData.objects.create(
            employee=self.employee,
            timestamp=timestamp2,
            punch_type='O'
        )
        
        timeclocks = list(TimeClockData.objects.all())
        self.assertEqual(timeclocks[0], timeclock2)  # Later timestamp first
        self.assertEqual(timeclocks[1], timeclock1)


class DailyWorkTestCase(TestCase):
    """Test cases for DailyWork model"""
    
    def setUp(self):
        """Set up test data"""
        # Create organizational structure
        self.general_direction = GeneralDirection.objects.create(
            name="Test General Direction"
        )
        self.direction = Direction.objects.create(
            name="Test Direction",
            general_direction=self.general_direction
        )
        self.department = Department.objects.create(
            name="Test Department",
            direction=self.direction
        )
        self.position = Position.objects.create(
            name="Test Position"
        )
        
        # Create test employee
        self.employee = Employee.objects.create(
            first_name="Jane",
            last_name="Smith",
            hire_date=date(2023, 1, 1),
            position=self.position,
            department=self.department,
            direction=self.direction,
            general_direction=self.general_direction
        )
        
        self.test_date = date(2024, 1, 15)
        self.test_period = date(2024, 1, 1)
    
    def test_daily_work_creation(self):
        """Test creating a DailyWork record"""
        daily_work = DailyWork.objects.create(
            employee=self.employee,
            period=self.test_period,
            work_date=self.test_date,
            day_hours=Decimal('8.00'),
            night_hours=Decimal('0.00'),
            meal_allowance_count=Decimal('1.00'),
            distance_allowance_count=Decimal('0.00'),
            holiday_100_percent=False,
            holiday_50_percent=False,
            external_site=False
        )
        
        self.assertEqual(daily_work.employee, self.employee)
        self.assertEqual(daily_work.period, self.test_period)
        self.assertEqual(daily_work.work_date, self.test_date)
        self.assertEqual(daily_work.day_hours, Decimal('8.00'))
        self.assertEqual(daily_work.night_hours, Decimal('0.00'))
    
    def test_daily_work_str_representation(self):
        """Test string representation of DailyWork"""
        daily_work = DailyWork.objects.create(
            employee=self.employee,
            period=self.test_period,
            work_date=self.test_date,
            day_hours=Decimal('8.00'),
            night_hours=Decimal('2.00')
        )
        
        expected_str = f"{self.employee.full_name} - {self.test_date} (10.00h)"
        self.assertEqual(str(daily_work), expected_str)
    
    def test_daily_work_total_hours_property(self):
        """Test total_hours calculated property"""
        daily_work = DailyWork.objects.create(
            employee=self.employee,
            period=self.test_period,
            work_date=self.test_date,
            day_hours=Decimal('6.50'),
            night_hours=Decimal('2.25')
        )
        
        self.assertEqual(daily_work.total_hours, Decimal('8.75'))
    
    def test_daily_work_has_holiday_work_property(self):
        """Test has_holiday_work calculated property"""
        # No holiday work
        daily_work = DailyWork.objects.create(
            employee=self.employee,
            period=self.test_period,
            work_date=self.test_date,
            day_hours=Decimal('8.00')
        )
        self.assertFalse(daily_work.has_holiday_work)
        
        # 100% holiday work
        daily_work.holiday_100_percent = True
        daily_work.save()
        self.assertTrue(daily_work.has_holiday_work)
        
        # 50% holiday work
        daily_work.holiday_100_percent = False
        daily_work.holiday_50_percent = True
        daily_work.save()
        self.assertTrue(daily_work.has_holiday_work)
    
    def test_daily_work_unique_constraint(self):
        """Test unique constraint on employee and work_date"""
        DailyWork.objects.create(
            employee=self.employee,
            period=self.test_period,
            work_date=self.test_date,
            day_hours=Decimal('8.00')
        )
        
        # Creating another record for same employee and date should fail
        with self.assertRaises(IntegrityError):
            DailyWork.objects.create(
                employee=self.employee,
                period=self.test_period,
                work_date=self.test_date,
                day_hours=Decimal('4.00')
            )
    
    def test_daily_work_cascade_delete(self):
        """Test that DailyWork is deleted when employee is deleted"""
        daily_work = DailyWork.objects.create(
            employee=self.employee,
            period=self.test_period,
            work_date=self.test_date,
            day_hours=Decimal('8.00')
        )
        
        self.employee.delete()
        
        # DailyWork should be deleted due to CASCADE
        self.assertFalse(DailyWork.objects.filter(id=daily_work.id).exists())
    
    def test_daily_work_negative_hours_validation(self):
        """Test that negative hours are not allowed"""
        daily_work = DailyWork(
            employee=self.employee,
            period=self.test_period,
            work_date=self.test_date,
            day_hours=Decimal('-1.00')
        )
        
        with self.assertRaises(ValidationError):
            daily_work.full_clean()


class WeeklyOvertimeTestCase(TestCase):
    """Test cases for WeeklyOvertime model"""
    
    def setUp(self):
        """Set up test data"""
        # Create organizational structure
        self.general_direction = GeneralDirection.objects.create(
            name="Test General Direction"
        )
        self.direction = Direction.objects.create(
            name="Test Direction",
            general_direction=self.general_direction
        )
        self.department = Department.objects.create(
            name="Test Department",
            direction=self.direction
        )
        self.position = Position.objects.create(
            name="Test Position"
        )
        
        # Create test employee
        self.employee = Employee.objects.create(
            first_name="Bob",
            last_name="Johnson",
            hire_date=date(2023, 1, 1),
            position=self.position,
            department=self.department,
            direction=self.direction,
            general_direction=self.general_direction
        )
        
        self.test_period = date(2024, 1, 1)
        self.week_start = date(2024, 1, 15)
        self.week_end = date(2024, 1, 21)
    
    def test_weekly_overtime_creation(self):
        """Test creating a WeeklyOvertime record"""
        weekly_ot = WeeklyOvertime.objects.create(
            employee=self.employee,
            period=self.test_period,
            week_start=self.week_start,
            week_end=self.week_end,
            overtime_115=Decimal('2.50'),
            overtime_140=Decimal('1.00'),
            overtime_150=Decimal('0.50'),
            overtime_200=Decimal('0.00'),
            meal_allowance_count=Decimal('3.00'),
            distance_allowance_count=Decimal('1.00')
        )
        
        self.assertEqual(weekly_ot.employee, self.employee)
        self.assertEqual(weekly_ot.period, self.test_period)
        self.assertEqual(weekly_ot.week_start, self.week_start)
        self.assertEqual(weekly_ot.week_end, self.week_end)
        self.assertEqual(weekly_ot.overtime_115, Decimal('2.50'))
    
    def test_weekly_overtime_str_representation(self):
        """Test string representation of WeeklyOvertime"""
        weekly_ot = WeeklyOvertime.objects.create(
            employee=self.employee,
            period=self.test_period,
            week_start=self.week_start,
            week_end=self.week_end
        )
        
        expected_str = f"{self.employee.full_name} - Week {self.week_start} to {self.week_end}"
        self.assertEqual(str(weekly_ot), expected_str)
    
    def test_weekly_overtime_total_hours_property(self):
        """Test total_overtime_hours calculated property"""
        weekly_ot = WeeklyOvertime.objects.create(
            employee=self.employee,
            period=self.test_period,
            week_start=self.week_start,
            week_end=self.week_end,
            overtime_115=Decimal('2.00'),
            overtime_140=Decimal('1.50'),
            overtime_150=Decimal('1.00'),
            overtime_200=Decimal('0.50')
        )
        
        expected_total = Decimal('5.00')  # 2.00 + 1.50 + 1.00 + 0.50
        self.assertEqual(weekly_ot.total_overtime_hours, expected_total)
    
    def test_weekly_overtime_has_overtime_property(self):
        """Test has_overtime calculated property"""
        # No overtime
        weekly_ot = WeeklyOvertime.objects.create(
            employee=self.employee,
            period=self.test_period,
            week_start=self.week_start,
            week_end=self.week_end
        )
        self.assertFalse(weekly_ot.has_overtime)
        
        # With overtime
        weekly_ot.overtime_115 = Decimal('2.00')
        weekly_ot.save()
        self.assertTrue(weekly_ot.has_overtime)
    
    def test_weekly_overtime_unique_constraint(self):
        """Test unique constraint on employee and week_start"""
        WeeklyOvertime.objects.create(
            employee=self.employee,
            period=self.test_period,
            week_start=self.week_start,
            week_end=self.week_end
        )
        
        # Creating another record for same employee and week_start should fail
        with self.assertRaises(IntegrityError):
            WeeklyOvertime.objects.create(
                employee=self.employee,
                period=self.test_period,
                week_start=self.week_start,
                week_end=date(2024, 1, 22)  # Different end date
            )
    
    def test_weekly_overtime_cascade_delete(self):
        """Test that WeeklyOvertime is deleted when employee is deleted"""
        weekly_ot = WeeklyOvertime.objects.create(
            employee=self.employee,
            period=self.test_period,
            week_start=self.week_start,
            week_end=self.week_end
        )
        
        self.employee.delete()
        
        # WeeklyOvertime should be deleted due to CASCADE
        self.assertFalse(WeeklyOvertime.objects.filter(id=weekly_ot.id).exists())


class WorkWeekTestCase(TestCase):
    """Test cases for WorkWeek model"""
    
    def test_work_week_creation(self):
        """Test creating WorkWeek records"""
        work_week = WorkWeek.objects.create(
            day='MONDAY',
            is_week_start=True,
            is_week_end=False,
            is_weekend=False
        )
        
        self.assertEqual(work_week.day, 'MONDAY')
        self.assertTrue(work_week.is_week_start)
        self.assertFalse(work_week.is_week_end)
        self.assertFalse(work_week.is_weekend)
    
    def test_work_week_str_representation(self):
        """Test string representation of WorkWeek"""
        # Weekday with no flags
        work_week = WorkWeek.objects.create(day='TUESDAY')
        self.assertEqual(str(work_week), 'Tuesday')
        
        # Weekend day
        weekend_day = WorkWeek.objects.create(
            day='SATURDAY',
            is_weekend=True
        )
        self.assertEqual(str(weekend_day), 'Saturday (Weekend)')
        
        # Week start day
        week_start = WorkWeek.objects.create(
            day='MONDAY',
            is_week_start=True
        )
        self.assertEqual(str(week_start), 'Monday (Week Start)')
        
        # Multiple flags
        complex_day = WorkWeek.objects.create(
            day='FRIDAY',
            is_week_end=True,
            is_weekend=True
        )
        self.assertEqual(str(complex_day), 'Friday (Week End, Weekend)')
    
    def test_work_week_day_choices(self):
        """Test valid day choices"""
        valid_days = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 
                      'FRIDAY', 'SATURDAY', 'SUNDAY']
        
        for day in valid_days:
            work_week = WorkWeek.objects.create(day=day)
            self.assertEqual(work_week.day, day)
    
    def test_work_week_get_weekend_days(self):
        """Test get_weekend_days class method"""
        # Create some weekend days
        WorkWeek.objects.create(day='SATURDAY', is_weekend=True)
        WorkWeek.objects.create(day='SUNDAY', is_weekend=True)
        WorkWeek.objects.create(day='MONDAY', is_weekend=False)
        
        weekend_days = WorkWeek.get_weekend_days()
        weekend_day_names = [day.day for day in weekend_days]
        
        self.assertIn('SATURDAY', weekend_day_names)
        self.assertIn('SUNDAY', weekend_day_names)
        self.assertNotIn('MONDAY', weekend_day_names)
        self.assertEqual(len(weekend_days), 2)
    
    def test_work_week_get_work_week_start(self):
        """Test get_work_week_start class method"""
        # No week start defined
        self.assertIsNone(WorkWeek.get_work_week_start())
        
        # Define week start
        monday = WorkWeek.objects.create(day='MONDAY', is_week_start=True)
        self.assertEqual(WorkWeek.get_work_week_start(), monday)
    
    def test_work_week_get_work_week_end(self):
        """Test get_work_week_end class method"""
        # No week end defined
        self.assertIsNone(WorkWeek.get_work_week_end())
        
        # Define week end
        friday = WorkWeek.objects.create(day='FRIDAY', is_week_end=True)
        self.assertEqual(WorkWeek.get_work_week_end(), friday)
    
    def test_work_week_ordering(self):
        """Test custom ordering by day of week"""
        # Create days in random order
        WorkWeek.objects.create(day='FRIDAY')
        WorkWeek.objects.create(day='MONDAY')
        WorkWeek.objects.create(day='WEDNESDAY')
        WorkWeek.objects.create(day='SUNDAY')
        
        days = list(WorkWeek.objects.all())
        day_names = [day.day for day in days]
        
        expected_order = ['MONDAY', 'WEDNESDAY', 'FRIDAY', 'SUNDAY']
        self.assertEqual(day_names, expected_order)
    
    def test_work_week_unique_constraint(self):
        """Test primary key constraint on day"""
        WorkWeek.objects.create(day='MONDAY')
        
        # Creating another MONDAY should fail
        with self.assertRaises(IntegrityError):
            WorkWeek.objects.create(day='MONDAY')


class TimeAttendanceIntegrationTestCase(TestCase):
    """Integration tests for Time & Attendance models"""
    
    def setUp(self):
        """Set up test data"""
        # Create organizational structure
        self.general_direction = GeneralDirection.objects.create(
            name="Test General Direction"
        )
        self.direction = Direction.objects.create(
            name="Test Direction",
            general_direction=self.general_direction
        )
        self.department = Department.objects.create(
            name="Test Department",
            direction=self.direction
        )
        self.position = Position.objects.create(
            name="Test Position"
        )
        
        # Create test employee
        self.employee = Employee.objects.create(
            first_name="Alice",
            last_name="Brown",
            hire_date=date(2023, 1, 1),
            position=self.position,
            department=self.department,
            direction=self.direction,
            general_direction=self.general_direction
        )
    
    def test_complete_time_attendance_workflow(self):
        """Test complete workflow from time clock to weekly overtime"""
        # 1. Create work week configuration
        WorkWeek.objects.create(day='MONDAY', is_week_start=True)
        WorkWeek.objects.create(day='FRIDAY', is_week_end=True)
        WorkWeek.objects.create(day='SATURDAY', is_weekend=True)
        WorkWeek.objects.create(day='SUNDAY', is_weekend=True)
        
        # 2. Create time clock data
        base_date = datetime(2024, 1, 15, 8, 0)  # Monday
        TimeClockData.objects.create(
            employee=self.employee,
            timestamp=timezone.make_aware(base_date),
            punch_type='I'
        )
        TimeClockData.objects.create(
            employee=self.employee,
            timestamp=timezone.make_aware(base_date.replace(hour=17)),
            punch_type='O'
        )
        
        # 3. Create daily work record
        daily_work = DailyWork.objects.create(
            employee=self.employee,
            period=date(2024, 1, 1),
            work_date=date(2024, 1, 15),
            day_hours=Decimal('8.00'),
            night_hours=Decimal('1.00'),
            meal_allowance_count=Decimal('1.00')
        )
        
        # 4. Create weekly overtime
        weekly_ot = WeeklyOvertime.objects.create(
            employee=self.employee,
            period=date(2024, 1, 1),
            week_start=date(2024, 1, 15),
            week_end=date(2024, 1, 21),
            overtime_115=Decimal('2.00'),
            overtime_140=Decimal('1.00')
        )
        
        # Verify relationships and data
        self.assertEqual(self.employee.timeclock_data.count(), 2)
        self.assertEqual(self.employee.daily_work_records.count(), 1)
        self.assertEqual(self.employee.weekly_overtime.count(), 1)
        
        # Verify calculations
        self.assertEqual(daily_work.total_hours, Decimal('9.00'))
        self.assertEqual(weekly_ot.total_overtime_hours, Decimal('3.00'))
        self.assertTrue(weekly_ot.has_overtime)
        
        # Verify work week configuration
        self.assertEqual(WorkWeek.get_work_week_start().day, 'MONDAY')
        self.assertEqual(WorkWeek.get_work_week_end().day, 'FRIDAY')
        self.assertEqual(WorkWeek.get_weekend_days().count(), 2)
    
    def test_employee_deletion_cascades(self):
        """Test that all time & attendance records are deleted when employee is deleted"""
        # Create records
        TimeClockData.objects.create(
            employee=self.employee,
            timestamp=timezone.now(),
            punch_type='I'
        )
        DailyWork.objects.create(
            employee=self.employee,
            period=date(2024, 1, 1),
            work_date=date(2024, 1, 15),
            day_hours=Decimal('8.00')
        )
        WeeklyOvertime.objects.create(
            employee=self.employee,
            period=date(2024, 1, 1),
            week_start=date(2024, 1, 15),
            week_end=date(2024, 1, 21)
        )
        
        # Verify records exist
        self.assertEqual(TimeClockData.objects.count(), 1)
        self.assertEqual(DailyWork.objects.count(), 1)
        self.assertEqual(WeeklyOvertime.objects.count(), 1)
        
        # Delete employee
        employee_id = self.employee.id
        self.employee.delete()
        
        # Verify cascade deletion
        self.assertEqual(TimeClockData.objects.count(), 0)
        self.assertEqual(DailyWork.objects.count(), 0)
        self.assertEqual(WeeklyOvertime.objects.count(), 0)