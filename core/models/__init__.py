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
from .payroll_elements import PayrollElement, PayrollElementFormula

# Group 7: Time & Attendance
from .time_attendance import TimeClockData, DailyWork, WeeklyOvertime, WorkWeek