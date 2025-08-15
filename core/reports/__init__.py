# Reports package - comprehensive payroll reporting and analytics
"""
Core reporting package for Django payroll system

This package provides:
1. PayrollSummaryAnalytics - Core analytics engine
2. PayrollSummaryManager - High-level reporting interface
3. CumulativeDataAggregator - YTD and multi-period analysis engine
4. CumulativeReportManager - Comprehensive cumulative reporting interface
5. AccountingExportService - Enterprise accounting system integration
6. EmployeeReportManager - Comprehensive employee directory and HR reporting
7. Comprehensive filtering and export capabilities
8. Management analytics for decision-making
9. Year-to-date and trend analysis capabilities
10. Regulatory compliance tracking and reporting
11. Multi-format accounting exports (Sage, Ciel, UNL, Generic)
12. Employee lifecycle and document compliance reporting

Usage:
    from core.reports import PayrollSummaryManager, CumulativeReportManager, FilterCriteria
    from core.reports import AccountingExportService, AccountingSystemType
    from core.reports import EmployeeReportManager, EmployeeReportFilter, ReportType
    
    # Basic payroll reporting
    manager = PayrollSummaryManager(system_parameters)
    summary = manager.get_monthly_summary(2024, 1)
    export_result = manager.export_summary(summary, 'excel')
    
    # Cumulative and trend analysis
    cumulative_manager = CumulativeReportManager()
    ytd_report = cumulative_manager.generate_ytd_report(2024)
    comparison_report = cumulative_manager.generate_comparison_report(
        start_period=date(2024, 1, 1),
        end_period=date(2024, 12, 31),
        comparison_type='month_over_month'
    )
    
    # Employee HR reporting
    employee_manager = EmployeeReportManager()
    directory_report = employee_manager.generate_report(
        ReportType.DIRECTORY, include_photos=True
    )
    contract_report = employee_manager.generate_report(
        ReportType.STATUS, report_subtype='contracts', expiry_within_days=60
    )
    
    # Accounting system integration
    accounting_service = AccountingExportService()
    results = accounting_service.generate_and_export_payroll_accounting(
        period="2024-01",
        motif_name="Salaire Janvier", 
        user_name="admin",
        export_systems=[AccountingSystemType.SAGE, AccountingSystemType.CIEL],
        output_directory="/path/to/exports"
    )
"""

from .payroll_summary import (
    PayrollSummaryAnalytics,
    PayrollSummaryManager,
    FilterCriteria,
    ReportPeriodType,
    ReportFormat
)

from .cumulative_reports import (
    CumulativeDataAggregator,
    CumulativeReportManager,
    CumulativeReportPeriodType,
    PeriodSummary,
    VarianceAnalysis,
    TrendData,
    get_ytd_summary_for_admin,
    get_comparison_analysis_for_admin,
    get_employee_cumulative_for_admin,
    get_trend_analysis_for_admin,
    get_compliance_report_for_admin,
)

from .accounting_exports import (
    AccountingExportService,
    AccountingSystemType,
    ExportConfiguration,
    ValidationResult,
    ChartOfAccountsManager,
    JournalEntryGenerator,
    SageAccountingExporter,
    CielAccountingExporter,
    UNLExporter,
    GenericAccountingExporter,
    AccountMapping,
    ExportStatus,
    create_accounting_export_service,
    export_payroll_to_sage,
    export_payroll_to_ciel,
    export_payroll_to_unl,
)

from .employee_reports import (
    EmployeeReportManager,
    EmployeeDirectoryReports,
    EmployeeStatusReports,
    EmployeeLifecycleReports,
    DocumentComplianceReports,
    EmployeeReportFilter,
    EmployeeSummaryData,
    EmployeeStatusType,
    ContractStatusType,
    DocumentStatusType,
    ReportType,
    ExportFormat,
    get_employee_directory_for_admin,
    get_employment_status_report_for_admin,
    get_contract_expiry_report_for_admin,
    get_document_compliance_report_for_admin,
    get_hr_dashboard_summary_for_admin,
)

__all__ = [
    # Basic payroll reporting
    'PayrollSummaryAnalytics',
    'PayrollSummaryManager',
    'FilterCriteria', 
    'ReportPeriodType',
    'ReportFormat',
    
    # Cumulative and trend analysis
    'CumulativeDataAggregator',
    'CumulativeReportManager',
    'CumulativeReportPeriodType',
    'PeriodSummary',
    'VarianceAnalysis',
    'TrendData',
    
    # Admin convenience functions
    'get_ytd_summary_for_admin',
    'get_comparison_analysis_for_admin',
    'get_employee_cumulative_for_admin',
    'get_trend_analysis_for_admin',
    'get_compliance_report_for_admin',
    
    # Accounting system integration
    'AccountingExportService',
    'AccountingSystemType',
    'ExportConfiguration',
    'ValidationResult',
    'ChartOfAccountsManager',
    'JournalEntryGenerator',
    'SageAccountingExporter',
    'CielAccountingExporter',
    'UNLExporter',
    'GenericAccountingExporter',
    'AccountMapping',
    'ExportStatus',
    'create_accounting_export_service',
    'export_payroll_to_sage',
    'export_payroll_to_ciel',
    'export_payroll_to_unl',
    
    # Employee reporting and HR analytics
    'EmployeeReportManager',
    'EmployeeDirectoryReports',
    'EmployeeStatusReports',
    'EmployeeLifecycleReports',
    'DocumentComplianceReports',
    'EmployeeReportFilter',
    'EmployeeSummaryData',
    'EmployeeStatusType',
    'ContractStatusType',
    'DocumentStatusType',
    'ReportType',
    'ExportFormat',
    
    # Employee reporting admin convenience functions
    'get_employee_directory_for_admin',
    'get_employment_status_report_for_admin',
    'get_contract_expiry_report_for_admin',
    'get_document_compliance_report_for_admin',
    'get_hr_dashboard_summary_for_admin',
]