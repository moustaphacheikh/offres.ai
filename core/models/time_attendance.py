from django.db import models
from django.core.validators import MinValueValidator
from .employee import Employee


class TimeClockData(models.Model):
    """
    Raw time clock data - equivalent to Donneespointeuse.java
    Stores raw punch data from time clocks before processing
    """
    
    # Use BigAutoField for large ID values as in Java
    id = models.BigAutoField(primary_key=True)
    
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='timeclock_data'
    )
    
    # heureJour - timestamp of the punch
    timestamp = models.DateTimeField()
    
    # vinOut - punch type: "I" for IN, "O" for OUT
    punch_type = models.CharField(
        max_length=1,
        choices=[
            ('I', 'IN'),
            ('O', 'OUT'),
        ],
        blank=True
    )
    
    # importe - whether this data has been imported/processed
    is_imported = models.BooleanField(default=False)
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'donneespointeuse'
        ordering = ['-timestamp']
        
    def __str__(self):
        return f"{self.employee.full_name} - {self.timestamp.strftime('%Y-%m-%d %H:%M')} ({self.get_punch_type_display()})"


class DailyWork(models.Model):
    """
    Daily processed work records - equivalent to Jour.java
    Stores daily work hours and allowances after processing time clock data
    """
    
    id = models.BigAutoField(primary_key=True)
    
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='daily_work_records'
    )
    
    # periode - payroll period this record belongs to
    period = models.DateField()
    
    # dateJour - specific work date
    work_date = models.DateField()
    
    # nbHeureJour - day hours worked
    day_hours = models.DecimalField(
        max_digits=22,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)]
    )
    
    # nbHeureNuit - night hours worked  
    night_hours = models.DecimalField(
        max_digits=22,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)]
    )
    
    # nbPrimePanier - meal allowance count
    meal_allowance_count = models.DecimalField(
        max_digits=22,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)]
    )
    
    # nbPrimeEloignement - distance allowance count  
    distance_allowance_count = models.DecimalField(
        max_digits=22,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)]
    )
    
    # ferie100 - 100% holiday work
    holiday_100_percent = models.BooleanField(default=False)
    
    # ferie50 - 50% holiday work
    holiday_50_percent = models.BooleanField(default=False)
    
    # siteExterne - external site work
    external_site = models.BooleanField(default=False)
    
    # note - optional notes for the day
    notes = models.TextField(blank=True)
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'jour'
        ordering = ['-work_date']
        unique_together = [['employee', 'work_date']]
        
    def __str__(self):
        return f"{self.employee.full_name} - {self.work_date} ({self.day_hours + self.night_hours}h)"
    
    @property
    def total_hours(self):
        """Calculate total hours worked for the day"""
        return self.day_hours + self.night_hours
    
    @property 
    def has_holiday_work(self):
        """Check if any holiday work was performed"""
        return self.holiday_100_percent or self.holiday_50_percent


class WeeklyOvertime(models.Model):
    """
    Weekly overtime tracking - equivalent to Weekot.java
    Tracks different overtime rates for a week period
    """
    
    id = models.BigAutoField(primary_key=True)
    
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='weekly_overtime'
    )
    
    # periode - payroll period
    period = models.DateField()
    
    # beginweek - week start date
    week_start = models.DateField()
    
    # endweek - week end date
    week_end = models.DateField()
    
    # ot115 - overtime at 115% rate
    overtime_115 = models.DecimalField(
        max_digits=22,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)]
    )
    
    # ot140 - overtime at 140% rate  
    overtime_140 = models.DecimalField(
        max_digits=22,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)]
    )
    
    # ot150 - overtime at 150% rate
    overtime_150 = models.DecimalField(
        max_digits=22,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)]
    )
    
    # ot200 - overtime at 200% rate
    overtime_200 = models.DecimalField(
        max_digits=22,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)]
    )
    
    # nbPrimePanier - meal allowance count for the week
    meal_allowance_count = models.DecimalField(
        max_digits=22,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)]
    )
    
    # nbPrimeEloignement - distance allowance count for the week
    distance_allowance_count = models.DecimalField(
        max_digits=22,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)]
    )
    
    # note - optional notes for the week
    notes = models.TextField(blank=True)
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'weekot'
        ordering = ['-week_start']
        unique_together = [['employee', 'week_start']]
        
    def __str__(self):
        return f"{self.employee.full_name} - Week {self.week_start} to {self.week_end}"
    
    @property
    def total_overtime_hours(self):
        """Calculate total overtime hours for all rates"""
        return (self.overtime_115 + self.overtime_140 + 
                self.overtime_150 + self.overtime_200)
    
    @property
    def has_overtime(self):
        """Check if any overtime was worked"""
        return self.total_overtime_hours > 0


class WorkWeek(models.Model):
    """
    Work week definitions - equivalent to Semainetravail.java
    Defines which days are start/end of week and weekends
    Note: The Java version seems to be a configuration table for work week setup
    """
    
    # jour - day name as primary key
    day = models.CharField(
        max_length=50,
        primary_key=True,
        choices=[
            ('MONDAY', 'Monday'),
            ('TUESDAY', 'Tuesday'), 
            ('WEDNESDAY', 'Wednesday'),
            ('THURSDAY', 'Thursday'),
            ('FRIDAY', 'Friday'),
            ('SATURDAY', 'Saturday'),
            ('SUNDAY', 'Sunday'),
        ]
    )
    
    # debut - marks start of work week
    is_week_start = models.BooleanField(default=False)
    
    # fin - marks end of work week  
    is_week_end = models.BooleanField(default=False)
    
    # weekEnd - marks weekend day
    is_weekend = models.BooleanField(default=False)
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'semainetravail'
        ordering = [
            models.Case(
                models.When(day='MONDAY', then=models.Value(1)),
                models.When(day='TUESDAY', then=models.Value(2)),
                models.When(day='WEDNESDAY', then=models.Value(3)),
                models.When(day='THURSDAY', then=models.Value(4)),
                models.When(day='FRIDAY', then=models.Value(5)),
                models.When(day='SATURDAY', then=models.Value(6)),
                models.When(day='SUNDAY', then=models.Value(7)),
                output_field=models.IntegerField(),
            )
        ]
        
    def __str__(self):
        flags = []
        if self.is_week_start:
            flags.append('Week Start')
        if self.is_week_end:
            flags.append('Week End')
        if self.is_weekend:
            flags.append('Weekend')
            
        flag_str = f" ({', '.join(flags)})" if flags else ""
        return f"{self.get_day_display()}{flag_str}"
    
    @classmethod
    def get_weekend_days(cls):
        """Get all days marked as weekend"""
        return cls.objects.filter(is_weekend=True)
    
    @classmethod  
    def get_work_week_start(cls):
        """Get the day that starts the work week"""
        try:
            return cls.objects.get(is_week_start=True)
        except cls.DoesNotExist:
            return None
    
    @classmethod
    def get_work_week_end(cls):
        """Get the day that ends the work week"""
        try:
            return cls.objects.get(is_week_end=True)
        except cls.DoesNotExist:
            return None