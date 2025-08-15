"""
Core utilities package for the Django payroll system.

This package contains utility modules converted from the Java payroll system,
providing essential business logic for calculations, formatting, and data processing.
"""

# Date and time utilities
from .date_utils import (
    DateCalculator,
    PayrollPeriodUtils,
    DateFormatter,
    WorkingDayCalculator,
    HolidayUtils,
    DateValidation
)

# Formula engine for payroll calculations
from .formula_engine import (
    FormulaEngine,
    FormulaCalculationError,
    PayrollFormulaEvaluator
)

# Payroll calculation logic
from .payroll_calculations import (
    PayrollFunctions,
    PayrollCalculator,
    OvertimeCalculator,
    InstallmentCalculator
)

# Tax calculation utilities
from .tax_calculations import (
    CNSSCalculator,
    CNAMCalculator,
    ITSCalculator,
    TaxCalculationService
)

# Text formatting and conversion
from .text_utils import (
    NumberToTextConverter,
    TextFormatter,
    ReportTextProcessor,
    ValidationUtils,
    ArabicTextUtils,
    LocalizationUtils,
    DocumentNumberGenerator
)

# Report generation utilities
from .report_utils import (
    PayslipReportData,
    DeclarationReportData,
    BankTransferReportData,
    AttendanceReportData,
    ReportFormatter,
    ReportContext,
    ReportDataValidator,
    ExportUtilities
)

# Import/Export utilities
from .import_export import (
    ImportResult,
    ExportResult,
    ProgressTracker,
    DataValidator,
    ExcelProcessor,
    CSVProcessor,
    EmployeeImportExport,
    PayrollElementImportExport,
    AttendanceImportExport,
    ImportExportManager,
    import_employees_from_excel,
    export_employees_to_excel,
    create_employee_template
)

# Main utility classes for convenient access
__all__ = [
    # Date utilities
    'DateCalculator',
    'PayrollPeriodUtils', 
    'DateFormatter',
    'WorkingDayCalculator',
    'HolidayUtils',
    'DateValidation',
    
    # Formula engine
    'FormulaEngine',
    'FormulaCalculationError',
    'PayrollFormulaEvaluator',
    
    # Payroll calculations
    'PayrollFunctions',
    'PayrollCalculator',
    'OvertimeCalculator',
    'InstallmentCalculator',
    
    # Tax calculations
    'CNSSCalculator',
    'CNAMCalculator',
    'ITSCalculator',
    'TaxCalculationService',
    
    # Text utilities
    'NumberToTextConverter',
    'TextFormatter',
    'ReportTextProcessor',
    'ValidationUtils',
    'ArabicTextUtils',
    'LocalizationUtils',
    'DocumentNumberGenerator',
    
    # Report utilities
    'PayslipReportData',
    'DeclarationReportData',
    'BankTransferReportData',
    'AttendanceReportData',
    'ReportFormatter',
    'ReportContext',
    'ReportDataValidator',
    'ExportUtilities',
    
    # Import/Export utilities
    'ImportResult',
    'ExportResult',
    'ProgressTracker',
    'DataValidator',
    'ExcelProcessor',
    'CSVProcessor',
    'EmployeeImportExport',
    'PayrollElementImportExport',
    'AttendanceImportExport',
    'ImportExportManager',
    'import_employees_from_excel',
    'export_employees_to_excel',
    'create_employee_template',
]

# Version info
__version__ = '1.0.0'
__author__ = 'Django Payroll Migration Team'
__description__ = 'Utility package for Django payroll system (migrated from Java)'