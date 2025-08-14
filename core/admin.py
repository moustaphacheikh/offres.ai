from django.contrib import admin
from .models import (
    # Organizational
    GeneralDirection, Direction, Department, Position,
    # Employee
    Employee,
    # Time & Attendance
    TimeClockData, DailyWork, WeeklyOvertime, WorkWeek,
    # Compliance & Reporting
    CNSSDeclaration, CNAMDeclaration,
    # Accounting Integration
    MasterPiece, DetailPiece
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


# Compliance & Reporting
@admin.register(CNSSDeclaration)
class CNSSDeclarationAdmin(admin.ModelAdmin):
    list_display = ('employee_name', 'cnss_number', 'declaration_period', 'status', 'total_working_days', 'actual_remuneration', 'submission_date')
    list_filter = ('status', 'declaration_period', 'submission_date')
    search_fields = ('employee_name', 'cnss_number', 'employee__first_name', 'employee__last_name')
    date_hierarchy = 'declaration_period'
    ordering = ('-declaration_period', 'employee_name')
    
    fieldsets = (
        ('Employee Information', {
            'fields': ('employee', 'employee_name', 'cnss_number')
        }),
        ('Declaration Details', {
            'fields': ('declaration_period', 'working_days_month1', 'working_days_month2', 'working_days_month3', 'total_working_days')
        }),
        ('Financial Information', {
            'fields': ('actual_remuneration', 'contribution_ceiling', 'cnss_contribution_employee', 'cnss_contribution_employer', 'total_cnss_contribution')
        }),
        ('Employment Dates', {
            'fields': ('hire_date', 'termination_date'),
            'classes': ('collapse',)
        }),
        ('Status & Submission', {
            'fields': ('status', 'submission_date', 'submission_reference')
        }),
        ('Additional Information', {
            'fields': ('remarks',),
            'classes': ('collapse',)
        }),
        ('Audit', {
            'fields': ('created_by', 'updated_by', 'created_at', 'updated_at'),
            'classes': ('collapse',)
        })
    )
    
    readonly_fields = ('created_at', 'updated_at')
    
    def save_model(self, request, obj, form, change):
        if not change:  # Creating new object
            obj.created_by = request.user.username
        obj.updated_by = request.user.username
        super().save_model(request, obj, form, change)


@admin.register(CNAMDeclaration)
class CNAMDeclarationAdmin(admin.ModelAdmin):
    list_display = ('employee_name', 'cnam_number', 'nni', 'declaration_period', 'status', 'total_taxable_base', 'total_working_days', 'submission_date')
    list_filter = ('status', 'declaration_period', 'submission_date')
    search_fields = ('employee_name', 'cnam_number', 'nni', 'employee__first_name', 'employee__last_name')
    date_hierarchy = 'declaration_period'
    ordering = ('-declaration_period', 'employee_name')
    
    fieldsets = (
        ('Employee Information', {
            'fields': ('employee', 'employee_name', 'employee_function_number', 'cnam_number', 'nni')
        }),
        ('Declaration Details', {
            'fields': ('declaration_period', 'entry_date', 'exit_date')
        }),
        ('Monthly Breakdown', {
            'fields': (
                ('taxable_base_month1', 'working_days_month1'),
                ('taxable_base_month2', 'working_days_month2'), 
                ('taxable_base_month3', 'working_days_month3')
            )
        }),
        ('Totals', {
            'fields': ('total_taxable_base', 'total_working_days', 'cnam_contribution_employee', 'cnam_contribution_employer', 'total_cnam_contribution')
        }),
        ('Status & Submission', {
            'fields': ('status', 'submission_date', 'submission_reference')
        }),
        ('Additional Information', {
            'fields': ('remarks',),
            'classes': ('collapse',)
        }),
        ('Audit', {
            'fields': ('created_by', 'updated_by', 'created_at', 'updated_at'),
            'classes': ('collapse',)
        })
    )
    
    readonly_fields = ('created_at', 'updated_at')
    
    def save_model(self, request, obj, form, change):
        if not change:  # Creating new object
            obj.created_by = request.user.username
        obj.updated_by = request.user.username
        super().save_model(request, obj, form, change)


# Accounting Integration
@admin.register(MasterPiece)
class MasterPieceAdmin(admin.ModelAdmin):
    list_display = ('numero', 'period', 'motif', 'dateop', 'total_debit', 'total_credit', 'status', 'is_balanced', 'initiateur')
    list_filter = ('status', 'period', 'motif', 'dateop')
    search_fields = ('numero', 'period', 'motif', 'rubrique', 'initiateur')
    date_hierarchy = 'dateop'
    ordering = ('-dateop', '-created_at')
    readonly_fields = ('balance_difference', 'is_balanced', 'created_at', 'updated_at')
    
    fieldsets = (
        ('Document Information', {
            'fields': ('numero', 'libelle_service', 'dateop', 'rubrique', 'beneficiaire')
        }),
        ('Period & Motif', {
            'fields': ('period', 'motif')
        }),
        ('Financial Summary', {
            'fields': ('total_debit', 'total_credit', 'balance_difference', 'is_balanced')
        }),
        ('Workflow & Approval', {
            'fields': ('status', 'initiateur', 'init_hr', 'approved_by', 'approval_date')
        }),
        ('Integration Tracking', {
            'fields': ('external_reference', 'export_date'),
            'classes': ('collapse',)
        }),
        ('Audit', {
            'fields': ('created_at', 'updated_at'),
            'classes': ('collapse',)
        })
    )
    
    actions = ['recalculate_totals', 'mark_as_validated', 'mark_as_exported']
    
    def recalculate_totals(self, request, queryset):
        """Recalculate totals for selected master pieces"""
        for master_piece in queryset:
            master_piece.recalculate_totals()
        self.message_user(request, f"Recalculated totals for {queryset.count()} master pieces.")
    recalculate_totals.short_description = "Recalculate totals from detail pieces"
    
    def mark_as_validated(self, request, queryset):
        """Mark selected master pieces as validated"""
        count = queryset.update(status='VALIDATED')
        self.message_user(request, f"Marked {count} master pieces as validated.")
    mark_as_validated.short_description = "Mark as validated"
    
    def mark_as_exported(self, request, queryset):
        """Mark selected master pieces as exported"""
        count = queryset.update(status='EXPORTED')
        self.message_user(request, f"Marked {count} master pieces as exported.")
    mark_as_exported.short_description = "Mark as exported"


class DetailPieceInline(admin.TabularInline):
    """Inline admin for DetailPiece within MasterPiece"""
    model = DetailPiece
    extra = 0
    readonly_fields = ('cvmro_montant', 'formatted_account')
    fields = ('compte', 'chapitre', 'formatted_account', 'libelle', 'intitulet', 'montant', 'sens', 'account_type', 'employee', 'exported')
    
    def get_queryset(self, request):
        return super().get_queryset(request).select_related('employee')


# Update MasterPiece admin to include DetailPiece inline
MasterPieceAdmin.inlines = [DetailPieceInline]


@admin.register(DetailPiece)
class DetailPieceAdmin(admin.ModelAdmin):
    list_display = ('numligne', 'nupiece', 'dateop', 'formatted_account', 'libelle', 'montant', 'sens', 'account_type', 'employee', 'exported')
    list_filter = ('sens', 'account_type', 'dateop', 'exported', 'journal')
    search_fields = ('compte', 'chapitre', 'libelle', 'intitulet', 'nupiece__numero', 'employee__first_name', 'employee__last_name')
    date_hierarchy = 'dateop'
    ordering = ('numligne',)
    readonly_fields = ('cvmro_montant', 'formatted_account', 'created_at', 'updated_at')
    
    fieldsets = (
        ('Master Piece Reference', {
            'fields': ('nupiece',)
        }),
        ('Transaction Details', {
            'fields': ('dateop', 'journal', 'compte', 'chapitre', 'formatted_account')
        }),
        ('Account Information', {
            'fields': ('libelle', 'intitulet', 'account_type')
        }),
        ('Financial Information', {
            'fields': ('montant', 'cvmro_montant', 'sens')
        }),
        ('Currency Information', {
            'fields': ('devise', 'cours', 'numero_cours'),
            'classes': ('collapse',)
        }),
        ('Employee Reference', {
            'fields': ('employee',),
            'classes': ('collapse',)
        }),
        ('Integration Tracking', {
            'fields': ('exported', 'export_reference'),
            'classes': ('collapse',)
        }),
        ('Audit', {
            'fields': ('created_at', 'updated_at'),
            'classes': ('collapse',)
        })
    )
    
    actions = ['mark_as_exported', 'mark_as_not_exported']
    
    def mark_as_exported(self, request, queryset):
        """Mark selected detail pieces as exported"""
        count = queryset.update(exported=True)
        self.message_user(request, f"Marked {count} detail pieces as exported.")
    mark_as_exported.short_description = "Mark as exported"
    
    def mark_as_not_exported(self, request, queryset):
        """Mark selected detail pieces as not exported"""
        count = queryset.update(exported=False, export_reference='')
        self.message_user(request, f"Marked {count} detail pieces as not exported.")
    mark_as_not_exported.short_description = "Mark as not exported"
    
    def get_queryset(self, request):
        return super().get_queryset(request).select_related('nupiece', 'employee')
