from django.contrib import admin
from .models import (
    # Organizational
    GeneralDirection, Direction, Department, Position,
    # Employee
    Employee,
    # Time & Attendance
    TimeClockData, DailyWork, WeeklyOvertime, WorkWeek
)


# Organizational Structure
@admin.register(GeneralDirection)
class GeneralDirectionAdmin(admin.ModelAdmin):
    list_display = ('name', 'created_at')
    search_fields = ('name',)


@admin.register(Direction)
class DirectionAdmin(admin.ModelAdmin):
    list_display = ('name', 'general_direction', 'created_at')
    list_filter = ('general_direction',)
    search_fields = ('name',)


@admin.register(Department)
class DepartmentAdmin(admin.ModelAdmin):
    list_display = ('name', 'direction', 'created_at')
    list_filter = ('direction',)
    search_fields = ('name',)


@admin.register(Position)
class PositionAdmin(admin.ModelAdmin):
    list_display = ('name', 'created_at')
    search_fields = ('name',)


# Employee
@admin.register(Employee)
class EmployeeAdmin(admin.ModelAdmin):
    list_display = ('full_name', 'hire_date', 'position', 'department', 'is_active')
    list_filter = ('is_active', 'hire_date', 'department', 'position')
    search_fields = ('first_name', 'last_name', 'national_id', 'email')
    date_hierarchy = 'hire_date'
    ordering = ('last_name', 'first_name')


# Time & Attendance
@admin.register(TimeClockData)
class TimeClockDataAdmin(admin.ModelAdmin):
    list_display = ('employee', 'timestamp', 'punch_type', 'is_imported', 'created_at')
    list_filter = ('punch_type', 'is_imported', 'timestamp')
    search_fields = ('employee__first_name', 'employee__last_name')
    date_hierarchy = 'timestamp'
    ordering = ('-timestamp',)


@admin.register(DailyWork)
class DailyWorkAdmin(admin.ModelAdmin):
    list_display = ('employee', 'work_date', 'total_hours_display', 'holiday_100_percent', 'holiday_50_percent', 'external_site')
    list_filter = ('work_date', 'holiday_100_percent', 'holiday_50_percent', 'external_site')
    search_fields = ('employee__first_name', 'employee__last_name')
    date_hierarchy = 'work_date'
    ordering = ('-work_date',)
    
    def total_hours_display(self, obj):
        return f"{obj.total_hours}h"
    total_hours_display.short_description = 'Total Hours'


@admin.register(WeeklyOvertime)
class WeeklyOvertimeAdmin(admin.ModelAdmin):
    list_display = ('employee', 'week_start', 'week_end', 'total_overtime_display', 'has_overtime')
    list_filter = ('week_start', 'week_end')
    search_fields = ('employee__first_name', 'employee__last_name')
    date_hierarchy = 'week_start'
    ordering = ('-week_start',)
    
    def total_overtime_display(self, obj):
        return f"{obj.total_overtime_hours}h"
    total_overtime_display.short_description = 'Total OT Hours'


@admin.register(WorkWeek)
class WorkWeekAdmin(admin.ModelAdmin):
    list_display = ('day', 'is_week_start', 'is_week_end', 'is_weekend')
    list_filter = ('is_week_start', 'is_week_end', 'is_weekend')
    ordering = ('day',)
