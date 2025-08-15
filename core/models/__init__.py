# Models package - each model will be in its own file
# Import all models here to make them available when importing from core.models

# Group 1: Organizational Structure
from .organizational import GeneralDirection, Direction, Department, Position

# Group 2: Reference/Lookup Tables  
from .reference import Activity, Bank, Origin, EmployeeStatus, PayrollMotif

# Group 3: Salary & Compensation
from .compensation import SalaryGrade, HousingGrid

# Group 4: Employee Core
from .employee import Employee

# Group 5: Employee Relations
from .employee_relations import Child, Leave, Document, Diploma

# Group 6: Payroll Elements
from .payroll_elements import PayrollElement, PayrollElementFormula, PayrollElementModel

# Group 7: Time & Attendance
from .time_attendance import TimeClockData, DailyWork, WeeklyOvertime, WorkWeek

# Group 8: System Configuration
from .system_config import SystemParameters, User

# Group 9: Payroll Processing
from .payroll_processing import Payroll, PayrollLineItem, WorkedDays

# Group 10: Deductions & Benefits
from .deductions_benefits import InstallmentDeduction, InstallmentTranche

# Group 11: Compliance & Reporting
from .compliance_reporting import CNSSDeclaration, CNAMDeclaration

# Group 12: Accounting Integration
from .accounting_integration import ExportFormat, MasterPiece, DetailPiece, AccountGenerator