# employee_reports.py
"""
Comprehensive Employee Directory and Status Reporting System
Provides extensive HR reporting capabilities for employee management and organizational analysis

This module provides:
1. Employee Directory Reports - Complete employee listings with contact information
2. Employee Status Reports - Employment status tracking and contract management
3. Organizational Structure Reports - Hierarchy and department analysis
4. Employee Lifecycle Reports - Onboarding, terminations, and career progression
5. Document and Compliance Reports - Document tracking and regulatory compliance
6. Multi-format exports and French/Arabic localization support

Integration with Django admin for HR operations management.
"""

from decimal import Decimal
from datetime import date, datetime, timedelta
from typing import Dict, List, Optional, Any, Union, Tuple
from dataclasses import dataclass, field
from enum import Enum
import json
import csv
import io
from collections import defaultdict, OrderedDict
import calendar
from django.db.models import Q, Count, Sum, Avg, Max, Min
from django.db.models.functions import Coalesce
from django.utils import timezone
from django.core.exceptions import ValidationError

# Import models
from ..models.employee import Employee
from ..models.employee_relations import Child, Leave, Document, Diploma
from ..models.organizational import GeneralDirection, Direction, Department, Position
from ..models.reference import Activity, Origin, Bank
from ..models.compensation import SalaryGrade

# Import utilities
from ..utils.date_utils import (
    DateCalculator, DateFormatter, WorkingDayCalculator, 
    SeniorityCalculator, LeaveCalculator, ArabicDateFormatter,
    HolidayUtils, DateValidation
)
from ..utils.report_utils import (
    ReportFormatter, ReportContext, ReportDataValidator,
    ExportUtilities, MauritanianNumberConverter
)


class EmployeeStatusType(Enum):
    """Employee status classification"""
    ACTIVE = "active"
    INACTIVE = "inactive"
    ON_LEAVE = "on_leave"
    TERMINATED = "terminated"
    PROBATION = "probation"


class ContractStatusType(Enum):
    """Contract status classification"""
    ACTIVE = "active"
    EXPIRING_SOON = "expiring_soon"
    EXPIRED = "expired"
    RENEWED = "renewed"
    TERMINATED = "terminated"


class DocumentStatusType(Enum):
    """Document status classification"""
    VALID = "valid"
    EXPIRING_SOON = "expiring_soon"
    EXPIRED = "expired"
    MISSING = "missing"


class ReportType(Enum):
    """Employee report types"""
    DIRECTORY = "directory"
    STATUS = "status" 
    ORGANIZATIONAL = "organizational"
    LIFECYCLE = "lifecycle"
    COMPLIANCE = "compliance"
    CUSTOM = "custom"


class ExportFormat(Enum):
    """Export format options"""
    PDF = "pdf"
    EXCEL = "excel"
    CSV = "csv"
    JSON = "json"
    XML = "xml"


@dataclass
class EmployeeReportFilter:
    """Employee report filtering criteria"""
    # Basic filters
    employee_ids: Optional[List[int]] = None
    departments: Optional[List[int]] = None
    directions: Optional[List[int]] = None
    general_directions: Optional[List[int]] = None
    positions: Optional[List[int]] = None
    
    # Status filters
    employment_status: Optional[List[EmployeeStatusType]] = None
    contract_status: Optional[List[ContractStatusType]] = None
    is_active: Optional[bool] = None
    is_expatriate: Optional[bool] = None
    
    # Date filters
    hire_date_from: Optional[date] = None
    hire_date_to: Optional[date] = None
    termination_date_from: Optional[date] = None
    termination_date_to: Optional[date] = None
    
    # Document filters
    document_expiry_within_days: Optional[int] = None
    missing_documents: Optional[List[str]] = None
    
    # Age and seniority filters
    age_from: Optional[int] = None
    age_to: Optional[int] = None
    seniority_from: Optional[int] = None
    seniority_to: Optional[int] = None
    
    # Search filters
    search_text: Optional[str] = None
    include_photo: bool = False
    
    # Compliance filters
    missing_cnss: Optional[bool] = None
    missing_cnam: Optional[bool] = None
    missing_nni: Optional[bool] = None


@dataclass
class EmployeeSummaryData:
    """Summary data for an employee"""
    employee_id: int
    employee_number: str
    full_name: str
    first_name: str
    last_name: str
    national_id: str
    hire_date: Optional[date]
    termination_date: Optional[date]
    birth_date: Optional[date]
    
    # Organizational
    position_name: Optional[str]
    department_name: Optional[str] 
    direction_name: Optional[str]
    general_direction_name: Optional[str]
    
    # Contact information
    phone: Optional[str]
    email: Optional[str]
    address: Optional[str]
    
    # Employment details
    employment_status: str
    contract_type: Optional[str]
    contract_end_date: Optional[date]
    is_active: bool
    is_expatriate: bool
    
    # Social security
    cnss_number: Optional[str]
    cnam_number: Optional[str]
    cnss_date: Optional[date]
    
    # Banking
    bank_name: Optional[str]
    bank_account: Optional[str]
    payment_mode: Optional[str]
    
    # Calculated fields
    age_years: Optional[int] = None
    seniority_years: Optional[int] = None
    children_count: int = 0
    documents_count: int = 0
    expired_documents_count: int = 0
    
    # Status indicators
    has_expired_documents: bool = False
    contract_expiring_soon: bool = False
    missing_required_info: bool = False


class EmployeeDataExtractor:
    """Employee data extraction and processing utilities"""
    
    @staticmethod
    def get_base_employee_queryset(filters: EmployeeReportFilter = None):
        """
        Get base employee queryset with filters applied
        
        Args:
            filters: Optional filtering criteria
            
        Returns:
            Filtered employee queryset with related data
        """
        queryset = Employee.objects.select_related(
            'position', 'department', 'direction', 'general_direction',
            'salary_grade', 'activity', 'origin', 'bank'
        ).prefetch_related(
            'children', 'documents', 'diplomas', 'leaves'
        )
        
        if not filters:
            return queryset
        
        # Apply filters
        if filters.employee_ids:
            queryset = queryset.filter(id__in=filters.employee_ids)
        
        if filters.departments:
            queryset = queryset.filter(department_id__in=filters.departments)
        
        if filters.directions:
            queryset = queryset.filter(direction_id__in=filters.directions)
        
        if filters.general_directions:
            queryset = queryset.filter(general_direction_id__in=filters.general_directions)
        
        if filters.positions:
            queryset = queryset.filter(position_id__in=filters.positions)
        
        if filters.is_active is not None:
            queryset = queryset.filter(is_active=filters.is_active)
        
        if filters.is_expatriate is not None:
            queryset = queryset.filter(is_expatriate=filters.is_expatriate)
        
        # Date filters
        if filters.hire_date_from:
            queryset = queryset.filter(hire_date__gte=filters.hire_date_from)
        
        if filters.hire_date_to:
            queryset = queryset.filter(hire_date__lte=filters.hire_date_to)
        
        if filters.termination_date_from:
            queryset = queryset.filter(termination_date__gte=filters.termination_date_from)
        
        if filters.termination_date_to:
            queryset = queryset.filter(termination_date__lte=filters.termination_date_to)
        
        # Age filters
        if filters.age_from or filters.age_to:
            today = date.today()
            
            if filters.age_from:
                max_birth_date = DateCalculator.add_years(today, -filters.age_from)
                queryset = queryset.filter(birth_date__lte=max_birth_date)
            
            if filters.age_to:
                min_birth_date = DateCalculator.add_years(today, -filters.age_to - 1)
                queryset = queryset.filter(birth_date__gte=min_birth_date)
        
        # Seniority filters
        if filters.seniority_from or filters.seniority_to:
            today = date.today()
            
            if filters.seniority_from:
                max_hire_date = DateCalculator.add_years(today, -filters.seniority_from)
                queryset = queryset.filter(hire_date__lte=max_hire_date)
            
            if filters.seniority_to:
                min_hire_date = DateCalculator.add_years(today, -filters.seniority_to - 1)
                queryset = queryset.filter(hire_date__gte=min_hire_date)
        
        # Compliance filters
        if filters.missing_cnss:
            queryset = queryset.filter(
                Q(cnss_number__isnull=True) | Q(cnss_number__exact='')
            )
        
        if filters.missing_cnam:
            queryset = queryset.filter(
                Q(cnam_number__isnull=True) | Q(cnam_number__exact='')
            )
        
        if filters.missing_nni:
            queryset = queryset.filter(
                Q(national_id__isnull=True) | Q(national_id__exact='')
            )
        
        # Text search
        if filters.search_text:
            search_terms = filters.search_text.split()
            search_query = Q()
            
            for term in search_terms:
                term_query = (
                    Q(first_name__icontains=term) |
                    Q(last_name__icontains=term) |
                    Q(national_id__icontains=term) |
                    Q(cnss_number__icontains=term) |
                    Q(cnam_number__icontains=term) |
                    Q(email__icontains=term) |
                    Q(phone__icontains=term)
                )
                search_query &= term_query
            
            queryset = queryset.filter(search_query)
        
        return queryset
    
    @staticmethod
    def extract_employee_summary_data(employee: Employee) -> EmployeeSummaryData:
        """
        Extract summary data from employee instance
        
        Args:
            employee: Employee instance
            
        Returns:
            Employee summary data
        """
        # Calculate age and seniority
        age_years = None
        if employee.birth_date:
            age_years = DateCalculator.age_years(employee.birth_date)
        
        seniority_years = None
        if employee.hire_date:
            seniority_years = SeniorityCalculator.calculate_seniority_years(
                employee.hire_date
            )
        
        # Get counts
        children_count = employee.children.count()
        documents_count = employee.documents.count()
        expired_documents_count = employee.documents.filter(
            expiry_date__lt=timezone.now().date()
        ).count()
        
        # Status indicators
        has_expired_documents = expired_documents_count > 0
        contract_expiring_soon = False
        
        if employee.contract_end_date:
            days_to_expiry = (employee.contract_end_date - date.today()).days
            contract_expiring_soon = 0 <= days_to_expiry <= 90
        
        # Missing required info check
        missing_required_info = not all([
            employee.first_name,
            employee.last_name,
            employee.national_id,
            employee.cnss_number,
            employee.cnam_number
        ])
        
        # Determine employment status
        employment_status = EmployeeDataExtractor._get_employment_status(employee)
        
        return EmployeeSummaryData(
            employee_id=employee.id,
            employee_number=str(employee.id),
            full_name=employee.full_name,
            first_name=employee.first_name,
            last_name=employee.last_name,
            national_id=employee.national_id or '',
            hire_date=employee.hire_date,
            termination_date=employee.termination_date,
            birth_date=employee.birth_date,
            
            position_name=employee.position.name if employee.position else '',
            department_name=employee.department.name if employee.department else '',
            direction_name=employee.direction.name if employee.direction else '',
            general_direction_name=employee.general_direction.name if employee.general_direction else '',
            
            phone=employee.phone,
            email=employee.email,
            address=employee.address,
            
            employment_status=employment_status,
            contract_type=employee.contract_type,
            contract_end_date=employee.contract_end_date,
            is_active=employee.is_active,
            is_expatriate=employee.is_expatriate,
            
            cnss_number=employee.cnss_number,
            cnam_number=employee.cnam_number,
            cnss_date=employee.cnss_date,
            
            bank_name=employee.bank.name if employee.bank else '',
            bank_account=employee.bank_account,
            payment_mode=employee.payment_mode,
            
            age_years=age_years,
            seniority_years=seniority_years,
            children_count=children_count,
            documents_count=documents_count,
            expired_documents_count=expired_documents_count,
            
            has_expired_documents=has_expired_documents,
            contract_expiring_soon=contract_expiring_soon,
            missing_required_info=missing_required_info
        )
    
    @staticmethod
    def _get_employment_status(employee: Employee) -> str:
        """Determine employee employment status"""
        if not employee.is_active:
            if employee.termination_date:
                return EmployeeStatusType.TERMINATED.value
            else:
                return EmployeeStatusType.INACTIVE.value
        
        if employee.on_leave:
            return EmployeeStatusType.ON_LEAVE.value
        
        # Check if in probation period (first 6 months)
        if employee.hire_date:
            probation_end = DateCalculator.add_months(employee.hire_date, 6)
            if date.today() <= probation_end:
                return EmployeeStatusType.PROBATION.value
        
        return EmployeeStatusType.ACTIVE.value


class EmployeeDirectoryReports:
    """Employee directory and contact information reports"""
    
    def __init__(self, system_params=None):
        self.system_params = system_params
    
    def generate_complete_directory(self, filters: EmployeeReportFilter = None,
                                  include_photos: bool = False,
                                  locale: str = "fr") -> Dict[str, Any]:
        """
        Generate complete employee directory with contact information
        
        Args:
            filters: Optional filtering criteria
            include_photos: Whether to include employee photos
            locale: Localization ("fr" or "ar")
            
        Returns:
            Complete directory report data
        """
        # Get filtered employees
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        
        # Extract directory data
        directory_entries = []
        for employee in employees:
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            
            entry = {
                'employee_number': summary_data.employee_number,
                'full_name': summary_data.full_name,
                'position': summary_data.position_name,
                'department': summary_data.department_name,
                'direction': summary_data.direction_name,
                'phone': summary_data.phone or '',
                'email': summary_data.email or '',
                'address': summary_data.address or '',
                'hire_date': DateFormatter.format_for_display(
                    summary_data.hire_date, "french", locale
                ) if summary_data.hire_date else '',
                'status': self._localize_status(summary_data.employment_status, locale)
            }
            
            if include_photos and employee.photo:
                entry['photo'] = employee.photo
            
            directory_entries.append(entry)
        
        # Sort by name
        directory_entries.sort(key=lambda x: x['full_name'])
        
        # Generate summary statistics
        stats = self._generate_directory_stats(directory_entries)
        
        return {
            'report_type': 'employee_directory',
            'generation_date': datetime.now(),
            'locale': locale,
            'filters_applied': filters.__dict__ if filters else {},
            'statistics': stats,
            'directory_entries': directory_entries,
            'total_employees': len(directory_entries)
        }
    
    def generate_organizational_hierarchy(self, filters: EmployeeReportFilter = None,
                                        locale: str = "fr") -> Dict[str, Any]:
        """
        Generate organizational hierarchy report
        
        Args:
            filters: Optional filtering criteria
            locale: Localization ("fr" or "ar")
            
        Returns:
            Organizational hierarchy data
        """
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        
        # Group by organizational structure
        hierarchy = defaultdict(lambda: defaultdict(lambda: defaultdict(list)))
        
        for employee in employees:
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            
            general_dir = summary_data.general_direction_name or 'Non spécifiée'
            direction = summary_data.direction_name or 'Non spécifiée'
            department = summary_data.department_name or 'Non spécifié'
            
            hierarchy[general_dir][direction][department].append({
                'employee_number': summary_data.employee_number,
                'full_name': summary_data.full_name,
                'position': summary_data.position_name or 'Non spécifié',
                'employment_status': self._localize_status(summary_data.employment_status, locale),
                'hire_date': DateFormatter.format_for_display(
                    summary_data.hire_date, "french", locale
                ) if summary_data.hire_date else '',
                'seniority_years': summary_data.seniority_years or 0
            })
        
        # Convert to structured format with counts
        structured_hierarchy = []
        total_employees = 0
        
        for general_dir, directions in hierarchy.items():
            general_dir_data = {
                'name': general_dir,
                'directions': [],
                'employee_count': 0
            }
            
            for direction, departments in directions.items():
                direction_data = {
                    'name': direction,
                    'departments': [],
                    'employee_count': 0
                }
                
                for department, employees_list in departments.items():
                    department_data = {
                        'name': department,
                        'employees': sorted(employees_list, key=lambda x: x['full_name']),
                        'employee_count': len(employees_list)
                    }
                    
                    direction_data['departments'].append(department_data)
                    direction_data['employee_count'] += len(employees_list)
                
                direction_data['departments'].sort(key=lambda x: x['name'])
                general_dir_data['directions'].append(direction_data)
                general_dir_data['employee_count'] += direction_data['employee_count']
            
            general_dir_data['directions'].sort(key=lambda x: x['name'])
            structured_hierarchy.append(general_dir_data)
            total_employees += general_dir_data['employee_count']
        
        structured_hierarchy.sort(key=lambda x: x['name'])
        
        return {
            'report_type': 'organizational_hierarchy',
            'generation_date': datetime.now(),
            'locale': locale,
            'hierarchy': structured_hierarchy,
            'total_employees': total_employees,
            'summary_stats': self._generate_hierarchy_stats(structured_hierarchy)
        }
    
    def generate_department_employee_list(self, department_id: int = None,
                                        filters: EmployeeReportFilter = None,
                                        locale: str = "fr") -> Dict[str, Any]:
        """
        Generate department-specific employee listing
        
        Args:
            department_id: Specific department ID (optional)
            filters: Additional filtering criteria
            locale: Localization ("fr" or "ar")
            
        Returns:
            Department employee listing
        """
        # Apply department filter
        if department_id:
            if filters:
                filters.departments = [department_id]
            else:
                filters = EmployeeReportFilter(departments=[department_id])
        
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        
        # Group by department
        departments_data = defaultdict(list)
        
        for employee in employees:
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            dept_name = summary_data.department_name or 'Non spécifié'
            
            employee_data = {
                'employee_number': summary_data.employee_number,
                'full_name': summary_data.full_name,
                'position': summary_data.position_name or '',
                'phone': summary_data.phone or '',
                'email': summary_data.email or '',
                'hire_date': DateFormatter.format_for_display(
                    summary_data.hire_date, "french", locale
                ) if summary_data.hire_date else '',
                'seniority_years': summary_data.seniority_years or 0,
                'employment_status': self._localize_status(summary_data.employment_status, locale)
            }
            
            departments_data[dept_name].append(employee_data)
        
        # Structure the data
        department_listings = []
        for dept_name, employees_list in departments_data.items():
            employees_list.sort(key=lambda x: x['full_name'])
            
            department_listings.append({
                'department_name': dept_name,
                'employee_count': len(employees_list),
                'employees': employees_list,
                'average_seniority': sum(emp.get('seniority_years', 0) for emp in employees_list) / len(employees_list) if employees_list else 0
            })
        
        department_listings.sort(key=lambda x: x['department_name'])
        
        return {
            'report_type': 'department_employee_list',
            'generation_date': datetime.now(),
            'locale': locale,
            'department_listings': department_listings,
            'total_departments': len(department_listings),
            'total_employees': sum(dept['employee_count'] for dept in department_listings)
        }
    
    def _generate_directory_stats(self, directory_entries: List[Dict]) -> Dict[str, Any]:
        """Generate statistics for directory report"""
        if not directory_entries:
            return {}
        
        # Status distribution
        status_counts = defaultdict(int)
        for entry in directory_entries:
            status_counts[entry['status']] += 1
        
        # Department distribution
        dept_counts = defaultdict(int)
        for entry in directory_entries:
            dept_counts[entry['department'] or 'Non spécifié'] += 1
        
        return {
            'total_employees': len(directory_entries),
            'status_distribution': dict(status_counts),
            'department_distribution': dict(dept_counts),
            'employees_with_email': sum(1 for e in directory_entries if e['email']),
            'employees_with_phone': sum(1 for e in directory_entries if e['phone'])
        }
    
    def _generate_hierarchy_stats(self, hierarchy: List[Dict]) -> Dict[str, Any]:
        """Generate statistics for hierarchy report"""
        total_general_directions = len(hierarchy)
        total_directions = sum(len(gd['directions']) for gd in hierarchy)
        total_departments = sum(
            len(dir_data['departments']) 
            for gd in hierarchy 
            for dir_data in gd['directions']
        )
        
        return {
            'total_general_directions': total_general_directions,
            'total_directions': total_directions,
            'total_departments': total_departments,
            'average_employees_per_department': sum(
                dept['employee_count']
                for gd in hierarchy
                for dir_data in gd['directions']
                for dept in dir_data['departments']
            ) / total_departments if total_departments > 0 else 0
        }
    
    def _localize_status(self, status: str, locale: str = "fr") -> str:
        """Localize employment status"""
        if locale == "ar":
            status_translations = {
                'active': 'نشط',
                'inactive': 'غير نشط',
                'on_leave': 'في إجازة',
                'terminated': 'منتهي الخدمة',
                'probation': 'فترة تجريبية'
            }
        else:
            status_translations = {
                'active': 'Actif',
                'inactive': 'Inactif',
                'on_leave': 'En congé',
                'terminated': 'Licencié',
                'probation': 'En probation'
            }
        
        return status_translations.get(status, status)


class EmployeeStatusReports:
    """Employee status and contract management reports"""
    
    def __init__(self, system_params=None):
        self.system_params = system_params
    
    def generate_employment_status_report(self, filters: EmployeeReportFilter = None,
                                        locale: str = "fr") -> Dict[str, Any]:
        """
        Generate employment status tracking report
        
        Args:
            filters: Optional filtering criteria
            locale: Localization ("fr" or "ar")
            
        Returns:
            Employment status report data
        """
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        
        # Categorize by employment status
        status_groups = {
            EmployeeStatusType.ACTIVE.value: [],
            EmployeeStatusType.INACTIVE.value: [],
            EmployeeStatusType.ON_LEAVE.value: [],
            EmployeeStatusType.TERMINATED.value: [],
            EmployeeStatusType.PROBATION.value: []
        }
        
        for employee in employees:
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            status = summary_data.employment_status
            
            employee_data = {
                'employee_number': summary_data.employee_number,
                'full_name': summary_data.full_name,
                'department': summary_data.department_name or '',
                'position': summary_data.position_name or '',
                'hire_date': DateFormatter.format_for_display(
                    summary_data.hire_date, "french", locale
                ) if summary_data.hire_date else '',
                'termination_date': DateFormatter.format_for_display(
                    summary_data.termination_date, "french", locale
                ) if summary_data.termination_date else '',
                'seniority_years': summary_data.seniority_years or 0,
                'contract_type': summary_data.contract_type or '',
                'contract_end_date': DateFormatter.format_for_display(
                    summary_data.contract_end_date, "french", locale
                ) if summary_data.contract_end_date else '',
            }
            
            if status in status_groups:
                status_groups[status].append(employee_data)
        
        # Sort each group
        for status_list in status_groups.values():
            status_list.sort(key=lambda x: x['full_name'])
        
        # Generate summary statistics
        total_employees = sum(len(group) for group in status_groups.values())
        status_counts = {status: len(group) for status, group in status_groups.items()}
        
        return {
            'report_type': 'employment_status',
            'generation_date': datetime.now(),
            'locale': locale,
            'status_groups': {
                self._localize_status(status, locale): employees_list
                for status, employees_list in status_groups.items()
            },
            'summary_statistics': {
                'total_employees': total_employees,
                'status_counts': {
                    self._localize_status(status, locale): count
                    for status, count in status_counts.items()
                },
                'active_percentage': (status_counts[EmployeeStatusType.ACTIVE.value] / total_employees * 100) if total_employees > 0 else 0
            }
        }
    
    def generate_contract_status_report(self, expiry_within_days: int = 90,
                                      filters: EmployeeReportFilter = None,
                                      locale: str = "fr") -> Dict[str, Any]:
        """
        Generate contract status and expiration tracking report
        
        Args:
            expiry_within_days: Alert threshold for contract expiry
            filters: Optional filtering criteria
            locale: Localization ("fr" or "ar")
            
        Returns:
            Contract status report data
        """
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        
        today = date.today()
        expiry_threshold = today + timedelta(days=expiry_within_days)
        
        contract_categories = {
            'active': [],          # Active contracts not expiring soon
            'expiring_soon': [],   # Contracts expiring within threshold
            'expired': [],         # Expired contracts
            'indefinite': [],      # CDI contracts (no end date)
            'terminated': []       # Terminated contracts
        }
        
        for employee in employees:
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            
            employee_data = {
                'employee_number': summary_data.employee_number,
                'full_name': summary_data.full_name,
                'department': summary_data.department_name or '',
                'position': summary_data.position_name or '',
                'contract_type': summary_data.contract_type or 'Non spécifié',
                'hire_date': DateFormatter.format_for_display(
                    summary_data.hire_date, "french", locale
                ) if summary_data.hire_date else '',
                'contract_end_date': DateFormatter.format_for_display(
                    summary_data.contract_end_date, "french", locale
                ) if summary_data.contract_end_date else '',
                'days_to_expiry': None,
                'employment_status': self._localize_status(summary_data.employment_status, locale)
            }
            
            # Determine contract category
            if summary_data.termination_date:
                contract_categories['terminated'].append(employee_data)
            elif not summary_data.contract_end_date:
                contract_categories['indefinite'].append(employee_data)
            else:
                days_to_expiry = (summary_data.contract_end_date - today).days
                employee_data['days_to_expiry'] = days_to_expiry
                
                if days_to_expiry < 0:
                    contract_categories['expired'].append(employee_data)
                elif days_to_expiry <= expiry_within_days:
                    contract_categories['expiring_soon'].append(employee_data)
                else:
                    contract_categories['active'].append(employee_data)
        
        # Sort each category
        for category in contract_categories.values():
            category.sort(key=lambda x: x['full_name'])
        
        # Sort expiring soon by days to expiry
        contract_categories['expiring_soon'].sort(
            key=lambda x: x['days_to_expiry'] if x['days_to_expiry'] is not None else float('inf')
        )
        
        # Generate statistics
        total_employees = sum(len(group) for group in contract_categories.values())
        category_counts = {cat: len(employees) for cat, employees in contract_categories.items()}
        
        return {
            'report_type': 'contract_status',
            'generation_date': datetime.now(),
            'locale': locale,
            'expiry_threshold_days': expiry_within_days,
            'contract_categories': {
                self._localize_contract_category(category, locale): employees_list
                for category, employees_list in contract_categories.items()
            },
            'summary_statistics': {
                'total_employees': total_employees,
                'category_counts': {
                    self._localize_contract_category(cat, locale): count
                    for cat, count in category_counts.items()
                },
                'urgent_renewals_needed': category_counts['expiring_soon'] + category_counts['expired'],
                'contract_types_distribution': self._get_contract_types_distribution(employees)
            }
        }
    
    def generate_probation_tracking_report(self, filters: EmployeeReportFilter = None,
                                         locale: str = "fr") -> Dict[str, Any]:
        """
        Generate probation period tracking report
        
        Args:
            filters: Optional filtering criteria
            locale: Localization ("fr" or "ar")
            
        Returns:
            Probation tracking report data
        """
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        today = date.today()
        
        probation_categories = {
            'current_probation': [],      # Currently in probation
            'probation_ending_soon': [],  # Probation ending within 30 days
            'probation_completed': [],    # Recently completed probation (within 90 days)
            'probation_overdue': []       # Probation review overdue
        }
        
        for employee in employees:
            if not employee.hire_date:
                continue
            
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            
            # Calculate probation dates (6 months standard)
            probation_end_date = DateCalculator.add_months(employee.hire_date, 6)
            days_to_probation_end = (probation_end_date - today).days
            
            employee_data = {
                'employee_number': summary_data.employee_number,
                'full_name': summary_data.full_name,
                'department': summary_data.department_name or '',
                'position': summary_data.position_name or '',
                'hire_date': DateFormatter.format_for_display(
                    employee.hire_date, "french", locale
                ),
                'probation_end_date': DateFormatter.format_for_display(
                    probation_end_date, "french", locale
                ),
                'days_in_probation': (today - employee.hire_date).days,
                'days_to_probation_end': days_to_probation_end,
                'employment_status': self._localize_status(summary_data.employment_status, locale)
            }
            
            # Categorize based on probation status
            if days_to_probation_end > 0:
                if days_to_probation_end <= 30:
                    probation_categories['probation_ending_soon'].append(employee_data)
                else:
                    probation_categories['current_probation'].append(employee_data)
            elif days_to_probation_end >= -90:  # Completed within last 90 days
                probation_categories['probation_completed'].append(employee_data)
            else:  # Overdue review
                probation_categories['probation_overdue'].append(employee_data)
        
        # Sort categories
        for category in probation_categories.values():
            category.sort(key=lambda x: x['days_to_probation_end'])
        
        # Generate statistics
        total_tracked = sum(len(group) for group in probation_categories.values())
        
        return {
            'report_type': 'probation_tracking',
            'generation_date': datetime.now(),
            'locale': locale,
            'probation_categories': {
                self._localize_probation_category(category, locale): employees_list
                for category, employees_list in probation_categories.items()
            },
            'summary_statistics': {
                'total_tracked_employees': total_tracked,
                'current_probation_count': len(probation_categories['current_probation']),
                'ending_soon_count': len(probation_categories['probation_ending_soon']),
                'overdue_reviews_count': len(probation_categories['probation_overdue']),
                'reviews_needed_urgently': len(probation_categories['probation_ending_soon']) + len(probation_categories['probation_overdue'])
            }
        }
    
    def _get_contract_types_distribution(self, employees) -> Dict[str, int]:
        """Get distribution of contract types"""
        contract_counts = defaultdict(int)
        
        for employee in employees:
            contract_type = employee.contract_type or 'Non spécifié'
            contract_counts[contract_type] += 1
        
        return dict(contract_counts)
    
    def _localize_status(self, status: str, locale: str = "fr") -> str:
        """Localize employment status"""
        if locale == "ar":
            status_translations = {
                'active': 'نشط',
                'inactive': 'غير نشط', 
                'on_leave': 'في إجازة',
                'terminated': 'منتهي الخدمة',
                'probation': 'فترة تجريبية'
            }
        else:
            status_translations = {
                'active': 'Actif',
                'inactive': 'Inactif',
                'on_leave': 'En congé',
                'terminated': 'Licencié',
                'probation': 'En probation'
            }
        
        return status_translations.get(status, status)
    
    def _localize_contract_category(self, category: str, locale: str = "fr") -> str:
        """Localize contract category"""
        if locale == "ar":
            translations = {
                'active': 'عقود نشطة',
                'expiring_soon': 'عقود تنتهي قريباً',
                'expired': 'عقود منتهية',
                'indefinite': 'عقود دائمة',
                'terminated': 'عقود منهية'
            }
        else:
            translations = {
                'active': 'Contrats actifs',
                'expiring_soon': 'Contrats expirant bientôt',
                'expired': 'Contrats expirés',
                'indefinite': 'Contrats CDI',
                'terminated': 'Contrats résiliés'
            }
        
        return translations.get(category, category)
    
    def _localize_probation_category(self, category: str, locale: str = "fr") -> str:
        """Localize probation category"""
        if locale == "ar":
            translations = {
                'current_probation': 'في فترة التجريب حالياً',
                'probation_ending_soon': 'فترة التجريب تنتهي قريباً',
                'probation_completed': 'فترة التجريب مكتملة',
                'probation_overdue': 'مراجعة فترة التجريب متأخرة'
            }
        else:
            translations = {
                'current_probation': 'En période d\'essai',
                'probation_ending_soon': 'Fin de période d\'essai prochaine',
                'probation_completed': 'Période d\'essai terminée',
                'probation_overdue': 'Évaluation d\'essai en retard'
            }
        
        return translations.get(category, category)


class EmployeeLifecycleReports:
    """Employee lifecycle and career progression reports"""
    
    def __init__(self, system_params=None):
        self.system_params = system_params
    
    def generate_new_hire_report(self, period_start: date, period_end: date,
                                filters: EmployeeReportFilter = None,
                                locale: str = "fr") -> Dict[str, Any]:
        """
        Generate new hire report with onboarding tracking
        
        Args:
            period_start: Start of reporting period
            period_end: End of reporting period 
            filters: Optional filtering criteria
            locale: Localization ("fr" or "ar")
            
        Returns:
            New hire report data
        """
        # Add date filter for new hires
        if filters:
            filters.hire_date_from = period_start
            filters.hire_date_to = period_end
        else:
            filters = EmployeeReportFilter(
                hire_date_from=period_start,
                hire_date_to=period_end
            )
        
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        
        new_hires = []
        onboarding_stats = {
            'documents_complete': 0,
            'documents_incomplete': 0,
            'in_probation': 0,
            'probation_completed': 0
        }
        
        for employee in employees:
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            
            # Check document completeness
            required_docs = ['CONTRACT', 'ID_CARD', 'MEDICAL']
            available_docs = employee.documents.values_list('document_type', flat=True)
            documents_complete = all(doc in available_docs for doc in required_docs)
            
            if documents_complete:
                onboarding_stats['documents_complete'] += 1
            else:
                onboarding_stats['documents_incomplete'] += 1
            
            # Check probation status
            if summary_data.employment_status == EmployeeStatusType.PROBATION.value:
                onboarding_stats['in_probation'] += 1
            else:
                onboarding_stats['probation_completed'] += 1
            
            new_hire_data = {
                'employee_number': summary_data.employee_number,
                'full_name': summary_data.full_name,
                'department': summary_data.department_name or '',
                'position': summary_data.position_name or '',
                'hire_date': DateFormatter.format_for_display(
                    summary_data.hire_date, "french", locale
                ),
                'age_years': summary_data.age_years or 0,
                'documents_complete': documents_complete,
                'missing_documents': self._get_missing_documents(employee, required_docs),
                'probation_status': self._localize_status(summary_data.employment_status, locale),
                'phone': summary_data.phone or '',
                'email': summary_data.email or '',
                'contract_type': summary_data.contract_type or '',
                'onboarding_score': self._calculate_onboarding_score(employee, summary_data)
            }
            
            new_hires.append(new_hire_data)
        
        # Sort by hire date
        new_hires.sort(key=lambda x: x['hire_date'])
        
        # Generate monthly breakdown
        monthly_breakdown = self._generate_monthly_hiring_breakdown(
            employees, period_start, period_end, locale
        )
        
        return {
            'report_type': 'new_hire_report',
            'generation_date': datetime.now(),
            'locale': locale,
            'period_start': DateFormatter.format_for_display(period_start, "french", locale),
            'period_end': DateFormatter.format_for_display(period_end, "french", locale),
            'new_hires': new_hires,
            'onboarding_statistics': onboarding_stats,
            'monthly_breakdown': monthly_breakdown,
            'summary_statistics': {
                'total_new_hires': len(new_hires),
                'average_age': sum(hire.get('age_years', 0) for hire in new_hires) / len(new_hires) if new_hires else 0,
                'onboarding_completion_rate': (onboarding_stats['documents_complete'] / len(new_hires) * 100) if new_hires else 0
            }
        }
    
    def generate_termination_report(self, period_start: date, period_end: date,
                                  filters: EmployeeReportFilter = None,
                                  locale: str = "fr") -> Dict[str, Any]:
        """
        Generate termination report with exit analysis
        
        Args:
            period_start: Start of reporting period
            period_end: End of reporting period
            filters: Optional filtering criteria  
            locale: Localization ("fr" or "ar")
            
        Returns:
            Termination report data
        """
        # Add date filter for terminations
        if filters:
            filters.termination_date_from = period_start
            filters.termination_date_to = period_end
        else:
            filters = EmployeeReportFilter(
                termination_date_from=period_start,
                termination_date_to=period_end
            )
        
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        
        terminations = []
        termination_reasons = defaultdict(int)
        
        for employee in employees:
            if not employee.termination_date:
                continue
                
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            
            # Calculate tenure
            tenure_days = (employee.termination_date - employee.hire_date).days if employee.hire_date else 0
            tenure_years = tenure_days / 365.25
            
            reason = employee.termination_reason or 'Non spécifié'
            termination_reasons[reason] += 1
            
            termination_data = {
                'employee_number': summary_data.employee_number,
                'full_name': summary_data.full_name,
                'department': summary_data.department_name or '',
                'position': summary_data.position_name or '',
                'hire_date': DateFormatter.format_for_display(
                    summary_data.hire_date, "french", locale
                ),
                'termination_date': DateFormatter.format_for_display(
                    summary_data.termination_date, "french", locale
                ),
                'tenure_years': round(tenure_years, 1),
                'age_at_termination': summary_data.age_years or 0,
                'termination_reason': reason,
                'contract_type': summary_data.contract_type or '',
                'final_position': summary_data.position_name or '',
                'final_department': summary_data.department_name or ''
            }
            
            terminations.append(termination_data)
        
        # Sort by termination date
        terminations.sort(key=lambda x: x['termination_date'])
        
        # Generate statistics
        average_tenure = sum(t.get('tenure_years', 0) for t in terminations) / len(terminations) if terminations else 0
        
        # Department impact analysis
        dept_impact = self._analyze_department_turnover_impact(terminations)
        
        return {
            'report_type': 'termination_report',
            'generation_date': datetime.now(),
            'locale': locale,
            'period_start': DateFormatter.format_for_display(period_start, "french", locale),
            'period_end': DateFormatter.format_for_display(period_end, "french", locale),
            'terminations': terminations,
            'summary_statistics': {
                'total_terminations': len(terminations),
                'average_tenure_years': round(average_tenure, 1),
                'termination_reasons': dict(termination_reasons),
                'department_impact': dept_impact
            }
        }
    
    def generate_anniversary_report(self, anniversary_year: int = None,
                                  filters: EmployeeReportFilter = None,
                                  locale: str = "fr") -> Dict[str, Any]:
        """
        Generate employee anniversary and milestone tracking report
        
        Args:
            anniversary_year: Specific anniversary year to track (optional)
            filters: Optional filtering criteria
            locale: Localization ("fr" or "ar")
            
        Returns:
            Anniversary report data
        """
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        current_year = anniversary_year or date.today().year
        
        anniversaries = []
        milestone_categories = {
            'service_awards_1_year': [],
            'service_awards_5_years': [],
            'service_awards_10_years': [],
            'service_awards_15_years': [],
            'service_awards_20_years': [],
            'service_awards_25_years': [],
            'upcoming_milestones': []
        }
        
        for employee in employees:
            if not employee.hire_date:
                continue
                
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            
            # Calculate anniversary date for current year
            anniversary_date = employee.hire_date.replace(year=current_year)
            years_of_service = current_year - employee.hire_date.year
            
            if years_of_service <= 0:
                continue
            
            anniversary_data = {
                'employee_number': summary_data.employee_number,
                'full_name': summary_data.full_name,
                'department': summary_data.department_name or '',
                'position': summary_data.position_name or '',
                'hire_date': DateFormatter.format_for_display(
                    employee.hire_date, "french", locale
                ),
                'anniversary_date': DateFormatter.format_for_display(
                    anniversary_date, "french", locale
                ),
                'years_of_service': years_of_service,
                'is_milestone_year': years_of_service in [1, 5, 10, 15, 20, 25, 30],
                'milestone_type': self._get_milestone_type(years_of_service, locale),
                'employment_status': self._localize_status(summary_data.employment_status, locale)
            }
            
            anniversaries.append(anniversary_data)
            
            # Categorize by milestone
            if years_of_service == 1:
                milestone_categories['service_awards_1_year'].append(anniversary_data)
            elif years_of_service == 5:
                milestone_categories['service_awards_5_years'].append(anniversary_data)
            elif years_of_service == 10:
                milestone_categories['service_awards_10_years'].append(anniversary_data)
            elif years_of_service == 15:
                milestone_categories['service_awards_15_years'].append(anniversary_data)
            elif years_of_service == 20:
                milestone_categories['service_awards_20_years'].append(anniversary_data)
            elif years_of_service >= 25:
                milestone_categories['service_awards_25_years'].append(anniversary_data)
            
            # Check for upcoming milestones (next milestone year)
            next_milestone = self._get_next_milestone_year(years_of_service)
            if next_milestone and next_milestone - years_of_service <= 2:
                milestone_categories['upcoming_milestones'].append({
                    **anniversary_data,
                    'years_to_milestone': next_milestone - years_of_service,
                    'next_milestone_year': next_milestone
                })
        
        # Sort anniversaries by date
        anniversaries.sort(key=lambda x: x['anniversary_date'])
        
        # Sort milestone categories
        for category in milestone_categories.values():
            category.sort(key=lambda x: x['anniversary_date'])
        
        return {
            'report_type': 'anniversary_report',
            'generation_date': datetime.now(),
            'locale': locale,
            'anniversary_year': current_year,
            'anniversaries': anniversaries,
            'milestone_categories': {
                self._localize_milestone_category(cat, locale): employees_list
                for cat, employees_list in milestone_categories.items()
            },
            'summary_statistics': {
                'total_anniversaries': len(anniversaries),
                'milestone_anniversaries': sum(1 for a in anniversaries if a['is_milestone_year']),
                'longest_tenure': max((a['years_of_service'] for a in anniversaries), default=0),
                'average_tenure': sum(a['years_of_service'] for a in anniversaries) / len(anniversaries) if anniversaries else 0
            }
        }
    
    def _get_missing_documents(self, employee: Employee, required_docs: List[str]) -> List[str]:
        """Get list of missing required documents"""
        available_docs = set(employee.documents.values_list('document_type', flat=True))
        return [doc for doc in required_docs if doc not in available_docs]
    
    def _calculate_onboarding_score(self, employee: Employee, summary_data: EmployeeSummaryData) -> int:
        """Calculate onboarding completion score (0-100)"""
        score = 0
        
        # Basic information (30 points)
        if employee.first_name and employee.last_name:
            score += 10
        if employee.national_id:
            score += 10
        if employee.phone or employee.email:
            score += 10
        
        # Social security registration (30 points)
        if employee.cnss_number:
            score += 15
        if employee.cnam_number:
            score += 15
        
        # Documentation (25 points)
        required_docs = ['CONTRACT', 'ID_CARD', 'MEDICAL']
        available_docs = set(employee.documents.values_list('document_type', flat=True))
        doc_completion = len(available_docs.intersection(required_docs)) / len(required_docs)
        score += int(doc_completion * 25)
        
        # Banking information (15 points)
        if employee.bank_account:
            score += 15
        
        return min(score, 100)
    
    def _generate_monthly_hiring_breakdown(self, employees, period_start: date, 
                                         period_end: date, locale: str) -> List[Dict]:
        """Generate monthly breakdown of new hires"""
        monthly_data = defaultdict(int)
        
        for employee in employees:
            if employee.hire_date:
                month_key = employee.hire_date.strftime("%Y-%m")
                monthly_data[month_key] += 1
        
        breakdown = []
        current_date = period_start.replace(day=1)
        
        while current_date <= period_end:
            month_key = current_date.strftime("%Y-%m")
            month_name = DateFormatter.format_date_for_report(current_date, "period")
            
            breakdown.append({
                'month': month_name,
                'new_hires_count': monthly_data.get(month_key, 0)
            })
            
            # Move to next month
            if current_date.month == 12:
                current_date = current_date.replace(year=current_date.year + 1, month=1)
            else:
                current_date = current_date.replace(month=current_date.month + 1)
        
        return breakdown
    
    def _analyze_department_turnover_impact(self, terminations: List[Dict]) -> Dict[str, Any]:
        """Analyze department-level turnover impact"""
        dept_impact = defaultdict(lambda: {'count': 0, 'positions': set()})
        
        for termination in terminations:
            dept = termination['final_department']
            position = termination['final_position']
            
            dept_impact[dept]['count'] += 1
            dept_impact[dept]['positions'].add(position)
        
        # Convert to regular dict with position counts
        impact_analysis = {}
        for dept, data in dept_impact.items():
            impact_analysis[dept] = {
                'termination_count': data['count'],
                'unique_positions_affected': len(data['positions']),
                'positions_affected': list(data['positions'])
            }
        
        return impact_analysis
    
    def _get_milestone_type(self, years: int, locale: str = "fr") -> str:
        """Get milestone description for years of service"""
        if locale == "ar":
            milestones = {
                1: "سنة واحدة من الخدمة",
                5: "5 سنوات من الخدمة", 
                10: "10 سنوات من الخدمة",
                15: "15 سنة من الخدمة",
                20: "20 سنة من الخدمة",
                25: "25 سنة من الخدمة",
                30: "30 سنة من الخدمة"
            }
        else:
            milestones = {
                1: "1 an de service",
                5: "5 ans de service",
                10: "10 ans de service", 
                15: "15 ans de service",
                20: "20 ans de service",
                25: "25 ans de service",
                30: "30 ans de service"
            }
        
        return milestones.get(years, f"{years} ans de service")
    
    def _get_next_milestone_year(self, current_years: int) -> Optional[int]:
        """Get next milestone year"""
        milestones = [1, 5, 10, 15, 20, 25, 30]
        for milestone in milestones:
            if milestone > current_years:
                return milestone
        return None
    
    def _localize_milestone_category(self, category: str, locale: str = "fr") -> str:
        """Localize milestone category"""
        if locale == "ar":
            translations = {
                'service_awards_1_year': 'جوائز الخدمة - سنة واحدة',
                'service_awards_5_years': 'جوائز الخدمة - 5 سنوات',
                'service_awards_10_years': 'جوائز الخدمة - 10 سنوات',
                'service_awards_15_years': 'جوائز الخدمة - 15 سنة',
                'service_awards_20_years': 'جوائز الخدمة - 20 سنة',
                'service_awards_25_years': 'جوائز الخدمة - 25 سنة وأكثر',
                'upcoming_milestones': 'الإنجازات القادمة'
            }
        else:
            translations = {
                'service_awards_1_year': 'Prix de service - 1 an',
                'service_awards_5_years': 'Prix de service - 5 ans',
                'service_awards_10_years': 'Prix de service - 10 ans',
                'service_awards_15_years': 'Prix de service - 15 ans',
                'service_awards_20_years': 'Prix de service - 20 ans',
                'service_awards_25_years': 'Prix de service - 25 ans et plus',
                'upcoming_milestones': 'Jalons à venir'
            }
        
        return translations.get(category, category)
    
    def _localize_status(self, status: str, locale: str = "fr") -> str:
        """Localize employment status"""
        if locale == "ar":
            status_translations = {
                'active': 'نشط',
                'inactive': 'غير نشط',
                'on_leave': 'في إجازة',
                'terminated': 'منتهي الخدمة',
                'probation': 'فترة تجريبية'
            }
        else:
            status_translations = {
                'active': 'Actif',
                'inactive': 'Inactif', 
                'on_leave': 'En congé',
                'terminated': 'Licencié',
                'probation': 'En probation'
            }
        
        return status_translations.get(status, status)


class DocumentComplianceReports:
    """Document tracking and compliance reporting"""
    
    def __init__(self, system_params=None):
        self.system_params = system_params
    
    def generate_document_status_report(self, expiry_within_days: int = 90,
                                      filters: EmployeeReportFilter = None,
                                      locale: str = "fr") -> Dict[str, Any]:
        """
        Generate comprehensive document status and expiration tracking report
        
        Args:
            expiry_within_days: Alert threshold for document expiry
            filters: Optional filtering criteria
            locale: Localization ("fr" or "ar")
            
        Returns:
            Document status report data
        """
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        today = date.today()
        expiry_threshold = today + timedelta(days=expiry_within_days)
        
        document_status = {
            'valid': [],           # Documents that are valid and not expiring soon
            'expiring_soon': [],   # Documents expiring within threshold
            'expired': [],         # Expired documents
            'missing': []          # Employees missing required documents
        }
        
        # Required document types
        required_doc_types = ['CONTRACT', 'ID_CARD', 'PASSPORT', 'MEDICAL']
        
        for employee in employees:
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            employee_docs = employee.documents.all()
            
            # Check each required document type
            for doc_type in required_doc_types:
                docs_of_type = employee_docs.filter(document_type=doc_type)
                
                if not docs_of_type.exists():
                    # Missing document
                    document_status['missing'].append({
                        'employee_number': summary_data.employee_number,
                        'full_name': summary_data.full_name,
                        'department': summary_data.department_name or '',
                        'document_type': doc_type,
                        'status': 'missing',
                        'priority': 'high' if doc_type in ['CONTRACT', 'ID_CARD'] else 'medium'
                    })
                else:
                    # Check expiry status for existing documents
                    for doc in docs_of_type:
                        status = self._get_document_status(doc, today, expiry_threshold)
                        
                        doc_data = {
                            'employee_number': summary_data.employee_number,
                            'full_name': summary_data.full_name,
                            'department': summary_data.department_name or '',
                            'document_type': doc.document_type,
                            'document_name': doc.document_name,
                            'issue_date': DateFormatter.format_for_display(
                                doc.issue_date, "french", locale
                            ) if doc.issue_date else '',
                            'expiry_date': DateFormatter.format_for_display(
                                doc.expiry_date, "french", locale
                            ) if doc.expiry_date else '',
                            'days_to_expiry': (doc.expiry_date - today).days if doc.expiry_date else None,
                            'status': status
                        }
                        
                        document_status[status].append(doc_data)
            
            # Check for expatriate-specific documents
            if employee.is_expatriate or employee.is_foreign_employee:
                expat_docs = ['PASSPORT', 'VISA', 'WORK_PERMIT', 'RESIDENCE_CARD']
                for doc_type in expat_docs:
                    if not employee_docs.filter(document_type=doc_type).exists():
                        document_status['missing'].append({
                            'employee_number': summary_data.employee_number,
                            'full_name': summary_data.full_name,
                            'department': summary_data.department_name or '',
                            'document_type': doc_type,
                            'status': 'missing',
                            'priority': 'high',
                            'is_expatriate_doc': True
                        })
        
        # Sort each category
        for category in document_status.values():
            category.sort(key=lambda x: x['full_name'])
        
        # Sort expiring soon by days to expiry
        document_status['expiring_soon'].sort(
            key=lambda x: x['days_to_expiry'] if x['days_to_expiry'] is not None else float('inf')
        )
        
        # Generate statistics
        stats = self._generate_document_statistics(document_status, employees.count())
        
        return {
            'report_type': 'document_status',
            'generation_date': datetime.now(),
            'locale': locale,
            'expiry_threshold_days': expiry_within_days,
            'document_categories': {
                self._localize_document_category(category, locale): docs_list
                for category, docs_list in document_status.items()
            },
            'statistics': stats,
            'compliance_score': self._calculate_compliance_score(document_status, employees.count())
        }
    
    def generate_compliance_alerts_report(self, filters: EmployeeReportFilter = None,
                                        locale: str = "fr") -> Dict[str, Any]:
        """
        Generate compliance alerts and notifications report
        
        Args:
            filters: Optional filtering criteria
            locale: Localization ("fr" or "ar")
            
        Returns:
            Compliance alerts report data
        """
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        
        alerts = {
            'critical': [],    # Critical compliance issues
            'warning': [],     # Warning level issues
            'info': []         # Informational alerts
        }
        
        for employee in employees:
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            employee_alerts = self._check_employee_compliance(employee, summary_data)
            
            for alert in employee_alerts:
                alert.update({
                    'employee_number': summary_data.employee_number,
                    'full_name': summary_data.full_name,
                    'department': summary_data.department_name or '',
                    'position': summary_data.position_name or ''
                })
                
                alerts[alert['severity']].append(alert)
        
        # Sort by priority and employee name
        for category in alerts.values():
            category.sort(key=lambda x: (x.get('priority', 0), x['full_name']))
        
        # Generate alert summary
        alert_summary = {
            'total_alerts': sum(len(category) for category in alerts.values()),
            'critical_count': len(alerts['critical']),
            'warning_count': len(alerts['warning']),
            'info_count': len(alerts['info']),
            'employees_affected': len(set(alert['employee_number'] for category in alerts.values() for alert in category))
        }
        
        return {
            'report_type': 'compliance_alerts',
            'generation_date': datetime.now(),
            'locale': locale,
            'alert_categories': {
                self._localize_alert_category(category, locale): alerts_list
                for category, alerts_list in alerts.items()
            },
            'alert_summary': alert_summary,
            'action_required': alert_summary['critical_count'] > 0
        }
    
    def generate_nni_cnss_cnam_report(self, filters: EmployeeReportFilter = None,
                                     locale: str = "fr") -> Dict[str, Any]:
        """
        Generate NNI, CNSS, and CNAM registration compliance report
        
        Args:
            filters: Optional filtering criteria
            locale: Localization ("fr" or "ar")
            
        Returns:
            Registration compliance report data
        """
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        
        compliance_data = {
            'compliant': [],           # All registrations complete
            'missing_nni': [],         # Missing NNI
            'missing_cnss': [],        # Missing CNSS
            'missing_cnam': [],        # Missing CNAM
            'multiple_missing': []     # Missing multiple registrations
        }
        
        for employee in employees:
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            
            missing_registrations = []
            if not employee.national_id:
                missing_registrations.append('NNI')
            if not employee.cnss_number:
                missing_registrations.append('CNSS')
            if not employee.cnam_number:
                missing_registrations.append('CNAM')
            
            employee_data = {
                'employee_number': summary_data.employee_number,
                'full_name': summary_data.full_name,
                'department': summary_data.department_name or '',
                'position': summary_data.position_name or '',
                'hire_date': DateFormatter.format_for_display(
                    summary_data.hire_date, "french", locale
                ) if summary_data.hire_date else '',
                'nni': ReportFormatter.format_nni(employee.national_id or ''),
                'cnss_number': ReportFormatter.format_cnss_number(employee.cnss_number or ''),
                'cnam_number': ReportFormatter.format_cnam_number(employee.cnam_number or ''),
                'cnss_date': DateFormatter.format_for_display(
                    employee.cnss_date, "french", locale
                ) if employee.cnss_date else '',
                'missing_registrations': missing_registrations,
                'compliance_status': 'compliant' if not missing_registrations else 'non_compliant'
            }
            
            # Categorize employee
            if not missing_registrations:
                compliance_data['compliant'].append(employee_data)
            elif len(missing_registrations) > 1:
                compliance_data['multiple_missing'].append(employee_data)
            else:
                category_key = f"missing_{missing_registrations[0].lower()}"
                if category_key in compliance_data:
                    compliance_data[category_key].append(employee_data)
        
        # Sort each category
        for category in compliance_data.values():
            category.sort(key=lambda x: x['full_name'])
        
        # Calculate compliance statistics
        total_employees = sum(len(category) for category in compliance_data.values())
        compliance_stats = {
            'total_employees': total_employees,
            'compliant_count': len(compliance_data['compliant']),
            'non_compliant_count': total_employees - len(compliance_data['compliant']),
            'compliance_percentage': (len(compliance_data['compliant']) / total_employees * 100) if total_employees > 0 else 0,
            'missing_nni_count': len(compliance_data['missing_nni']) + len(compliance_data['multiple_missing']),
            'missing_cnss_count': len(compliance_data['missing_cnss']) + len(compliance_data['multiple_missing']),
            'missing_cnam_count': len(compliance_data['missing_cnam']) + len(compliance_data['multiple_missing'])
        }
        
        return {
            'report_type': 'registration_compliance',
            'generation_date': datetime.now(),
            'locale': locale,
            'compliance_categories': {
                self._localize_compliance_category(category, locale): employees_list
                for category, employees_list in compliance_data.items()
            },
            'compliance_statistics': compliance_stats,
            'action_items': self._generate_compliance_action_items(compliance_data, locale)
        }
    
    def _get_document_status(self, document: Document, today: date, 
                           expiry_threshold: date) -> str:
        """Determine document status based on expiry date"""
        if not document.expiry_date:
            return 'valid'  # No expiry date means indefinite validity
        
        if document.expiry_date < today:
            return 'expired'
        elif document.expiry_date <= expiry_threshold:
            return 'expiring_soon'
        else:
            return 'valid'
    
    def _generate_document_statistics(self, document_status: Dict, total_employees: int) -> Dict[str, Any]:
        """Generate document compliance statistics"""
        total_docs = sum(len(category) for category in document_status.values())
        
        return {
            'total_employees': total_employees,
            'total_documents_tracked': total_docs,
            'valid_documents': len(document_status['valid']),
            'expiring_soon': len(document_status['expiring_soon']),
            'expired_documents': len(document_status['expired']),
            'missing_documents': len(document_status['missing']),
            'compliance_percentage': (len(document_status['valid']) / total_docs * 100) if total_docs > 0 else 0,
            'urgent_action_needed': len(document_status['expired']) + len(document_status['expiring_soon'])
        }
    
    def _calculate_compliance_score(self, document_status: Dict, total_employees: int) -> int:
        """Calculate overall compliance score (0-100)"""
        if total_employees == 0:
            return 100
        
        total_docs = sum(len(category) for category in document_status.values())
        if total_docs == 0:
            return 0
        
        # Weight different statuses
        score = (
            len(document_status['valid']) * 100 +
            len(document_status['expiring_soon']) * 75 +
            len(document_status['expired']) * 25 +
            len(document_status['missing']) * 0
        ) / total_docs
        
        return int(score)
    
    def _check_employee_compliance(self, employee: Employee, 
                                 summary_data: EmployeeSummaryData) -> List[Dict[str, Any]]:
        """Check individual employee compliance issues"""
        alerts = []
        today = date.today()
        
        # Critical alerts
        if not employee.national_id:
            alerts.append({
                'type': 'missing_nni',
                'message': 'NNI manquant',
                'severity': 'critical',
                'priority': 10
            })
        
        if not employee.cnss_number:
            alerts.append({
                'type': 'missing_cnss',
                'message': 'Numéro CNSS manquant',
                'severity': 'critical',
                'priority': 9
            })
        
        if not employee.cnam_number:
            alerts.append({
                'type': 'missing_cnam',
                'message': 'Numéro CNAM manquant',
                'severity': 'critical',
                'priority': 8
            })
        
        # Warning alerts
        expired_docs = employee.documents.filter(expiry_date__lt=today).count()
        if expired_docs > 0:
            alerts.append({
                'type': 'expired_documents',
                'message': f'{expired_docs} document(s) expiré(s)',
                'severity': 'warning',
                'priority': 7
            })
        
        expiring_docs = employee.documents.filter(
            expiry_date__gte=today,
            expiry_date__lte=today + timedelta(days=30)
        ).count()
        if expiring_docs > 0:
            alerts.append({
                'type': 'expiring_documents',
                'message': f'{expiring_docs} document(s) expirant dans 30 jours',
                'severity': 'warning',
                'priority': 6
            })
        
        # Info alerts
        if not employee.phone and not employee.email:
            alerts.append({
                'type': 'missing_contact',
                'message': 'Informations de contact manquantes',
                'severity': 'info',
                'priority': 3
            })
        
        if employee.is_expatriate and not employee.passport_number:
            alerts.append({
                'type': 'missing_passport',
                'message': 'Numéro de passeport manquant pour expatrié',
                'severity': 'warning',
                'priority': 5
            })
        
        return alerts
    
    def _generate_compliance_action_items(self, compliance_data: Dict, locale: str) -> List[Dict[str, Any]]:
        """Generate action items for compliance issues"""
        action_items = []
        
        if compliance_data['missing_nni']:
            action_items.append({
                'priority': 'high',
                'action': 'Obtenir les NNI manquants' if locale == 'fr' else 'Get missing NNIs',
                'affected_employees': len(compliance_data['missing_nni']),
                'deadline': '15 jours'
            })
        
        if compliance_data['missing_cnss']:
            action_items.append({
                'priority': 'high',
                'action': 'Enregistrement CNSS' if locale == 'fr' else 'CNSS registration',
                'affected_employees': len(compliance_data['missing_cnss']),
                'deadline': '30 jours'
            })
        
        if compliance_data['missing_cnam']:
            action_items.append({
                'priority': 'high',
                'action': 'Enregistrement CNAM' if locale == 'fr' else 'CNAM registration',
                'affected_employees': len(compliance_data['missing_cnam']),
                'deadline': '30 jours'
            })
        
        return action_items
    
    def _localize_document_category(self, category: str, locale: str = "fr") -> str:
        """Localize document category"""
        if locale == "ar":
            translations = {
                'valid': 'وثائق صالحة',
                'expiring_soon': 'وثائق تنتهي قريباً',
                'expired': 'وثائق منتهية الصلاحية',
                'missing': 'وثائق مفقودة'
            }
        else:
            translations = {
                'valid': 'Documents valides',
                'expiring_soon': 'Documents expirant bientôt',
                'expired': 'Documents expirés',
                'missing': 'Documents manquants'
            }
        
        return translations.get(category, category)
    
    def _localize_alert_category(self, category: str, locale: str = "fr") -> str:
        """Localize alert category"""
        if locale == "ar":
            translations = {
                'critical': 'تنبيهات حرجة',
                'warning': 'تنبيهات تحذيرية',
                'info': 'تنبيهات إعلامية'
            }
        else:
            translations = {
                'critical': 'Alertes critiques',
                'warning': 'Avertissements',
                'info': 'Informations'
            }
        
        return translations.get(category, category)
    
    def _localize_compliance_category(self, category: str, locale: str = "fr") -> str:
        """Localize compliance category"""
        if locale == "ar":
            translations = {
                'compliant': 'متوافق',
                'missing_nni': 'NNI مفقود',
                'missing_cnss': 'CNSS مفقود',
                'missing_cnam': 'CNAM مفقود',
                'multiple_missing': 'تسجيلات متعددة مفقودة'
            }
        else:
            translations = {
                'compliant': 'Conforme',
                'missing_nni': 'NNI manquant',
                'missing_cnss': 'CNSS manquant',
                'missing_cnam': 'CNAM manquant',
                'multiple_missing': 'Plusieurs enregistrements manquants'
            }
        
        return translations.get(category, category)


class EmployeeReportManager:
    """Main employee reporting manager with export capabilities"""
    
    def __init__(self, system_params=None):
        self.system_params = system_params
        self.directory_reports = EmployeeDirectoryReports(system_params)
        self.status_reports = EmployeeStatusReports(system_params)
        self.lifecycle_reports = EmployeeLifecycleReports(system_params)
        self.compliance_reports = DocumentComplianceReports(system_params)
    
    def generate_report(self, report_type: ReportType, 
                       filters: EmployeeReportFilter = None,
                       **kwargs) -> Dict[str, Any]:
        """
        Generate employee report of specified type
        
        Args:
            report_type: Type of report to generate
            filters: Optional filtering criteria
            **kwargs: Additional parameters specific to report type
            
        Returns:
            Generated report data
        """
        locale = kwargs.get('locale', 'fr')
        
        if report_type == ReportType.DIRECTORY:
            if kwargs.get('report_subtype') == 'hierarchy':
                return self.directory_reports.generate_organizational_hierarchy(filters, locale)
            elif kwargs.get('report_subtype') == 'department':
                return self.directory_reports.generate_department_employee_list(
                    kwargs.get('department_id'), filters, locale
                )
            else:
                return self.directory_reports.generate_complete_directory(
                    filters, kwargs.get('include_photos', False), locale
                )
        
        elif report_type == ReportType.STATUS:
            if kwargs.get('report_subtype') == 'contracts':
                return self.status_reports.generate_contract_status_report(
                    kwargs.get('expiry_within_days', 90), filters, locale
                )
            elif kwargs.get('report_subtype') == 'probation':
                return self.status_reports.generate_probation_tracking_report(filters, locale)
            else:
                return self.status_reports.generate_employment_status_report(filters, locale)
        
        elif report_type == ReportType.LIFECYCLE:
            if kwargs.get('report_subtype') == 'new_hires':
                return self.lifecycle_reports.generate_new_hire_report(
                    kwargs.get('period_start'), kwargs.get('period_end'), filters, locale
                )
            elif kwargs.get('report_subtype') == 'terminations':
                return self.lifecycle_reports.generate_termination_report(
                    kwargs.get('period_start'), kwargs.get('period_end'), filters, locale
                )
            elif kwargs.get('report_subtype') == 'anniversaries':
                return self.lifecycle_reports.generate_anniversary_report(
                    kwargs.get('anniversary_year'), filters, locale
                )
            elif kwargs.get('report_subtype') == 'career_progression':
                return self.lifecycle_reports.generate_career_progression_report(filters, locale)
        
        elif report_type == ReportType.COMPLIANCE:
            if kwargs.get('report_subtype') == 'documents':
                return self.compliance_reports.generate_document_status_report(
                    kwargs.get('expiry_within_days', 90), filters, locale
                )
            elif kwargs.get('report_subtype') == 'alerts':
                return self.compliance_reports.generate_compliance_alerts_report(filters, locale)
            elif kwargs.get('report_subtype') == 'registrations':
                return self.compliance_reports.generate_nni_cnss_cnam_report(filters, locale)
        
        else:
            raise ValueError(f"Unsupported report type: {report_type}")
    
    def export_report(self, report_data: Dict[str, Any], 
                     export_format: ExportFormat,
                     **kwargs) -> Union[str, bytes]:
        """
        Export report data to specified format
        
        Args:
            report_data: Report data to export
            export_format: Export format
            **kwargs: Additional export parameters
            
        Returns:
            Exported data as string or bytes
        """
        if export_format == ExportFormat.CSV:
            return self._export_to_csv(report_data, **kwargs)
        elif export_format == ExportFormat.JSON:
            return self._export_to_json(report_data, **kwargs)
        elif export_format == ExportFormat.XML:
            return self._export_to_xml(report_data, **kwargs)
        elif export_format == ExportFormat.EXCEL:
            return self._export_to_excel(report_data, **kwargs)
        elif export_format == ExportFormat.PDF:
            return self._export_to_pdf(report_data, **kwargs)
        else:
            raise ValueError(f"Unsupported export format: {export_format}")
    
    def get_dashboard_summary(self, filters: EmployeeReportFilter = None) -> Dict[str, Any]:
        """
        Get summary statistics for HR dashboard
        
        Args:
            filters: Optional filtering criteria
            
        Returns:
            Dashboard summary data
        """
        employees = EmployeeDataExtractor.get_base_employee_queryset(filters)
        today = date.today()
        
        # Basic counts
        total_employees = employees.count()
        active_employees = employees.filter(is_active=True).count()
        inactive_employees = total_employees - active_employees
        
        # Status distribution
        status_counts = defaultdict(int)
        for employee in employees:
            summary_data = EmployeeDataExtractor.extract_employee_summary_data(employee)
            status_counts[summary_data.employment_status] += 1
        
        # Contract expiry alerts (next 90 days)
        expiry_threshold = today + timedelta(days=90)
        contracts_expiring = employees.filter(
            contract_end_date__lte=expiry_threshold,
            contract_end_date__gte=today,
            is_active=True
        ).count()
        
        # Document compliance
        total_docs = Document.objects.filter(employee__in=employees).count()
        expired_docs = Document.objects.filter(
            employee__in=employees,
            expiry_date__lt=today
        ).count()
        
        # Missing registrations
        missing_nni = employees.filter(
            Q(national_id__isnull=True) | Q(national_id__exact='')
        ).count()
        missing_cnss = employees.filter(
            Q(cnss_number__isnull=True) | Q(cnss_number__exact='')
        ).count()
        missing_cnam = employees.filter(
            Q(cnam_number__isnull=True) | Q(cnam_number__exact='')
        ).count()
        
        # Age distribution
        age_distribution = {
            '18-25': 0,
            '26-35': 0,
            '36-45': 0,
            '46-55': 0,
            '55+': 0
        }
        
        for employee in employees:
            if employee.birth_date:
                age = DateCalculator.age_years(employee.birth_date)
                if age <= 25:
                    age_distribution['18-25'] += 1
                elif age <= 35:
                    age_distribution['26-35'] += 1
                elif age <= 45:
                    age_distribution['36-45'] += 1
                elif age <= 55:
                    age_distribution['46-55'] += 1
                else:
                    age_distribution['55+'] += 1
        
        # Seniority distribution
        seniority_distribution = {
            '0-2 ans': 0,
            '2-5 ans': 0,
            '5-10 ans': 0,
            '10-15 ans': 0,
            '15+ ans': 0
        }
        
        for employee in employees:
            if employee.hire_date:
                years = SeniorityCalculator.calculate_seniority_years(employee.hire_date)
                if years < 2:
                    seniority_distribution['0-2 ans'] += 1
                elif years < 5:
                    seniority_distribution['2-5 ans'] += 1
                elif years < 10:
                    seniority_distribution['5-10 ans'] += 1
                elif years < 15:
                    seniority_distribution['10-15 ans'] += 1
                else:
                    seniority_distribution['15+ ans'] += 1
        
        return {
            'summary_date': today,
            'employee_counts': {
                'total': total_employees,
                'active': active_employees,
                'inactive': inactive_employees
            },
            'status_distribution': dict(status_counts),
            'age_distribution': age_distribution,
            'seniority_distribution': seniority_distribution,
            'alerts': {
                'contracts_expiring_soon': contracts_expiring,
                'expired_documents': expired_docs,
                'missing_nni': missing_nni,
                'missing_cnss': missing_cnss,
                'missing_cnam': missing_cnam
            },
            'compliance_metrics': {
                'document_compliance_rate': ((total_docs - expired_docs) / total_docs * 100) if total_docs > 0 else 100,
                'registration_compliance_rate': ((total_employees - missing_nni - missing_cnss - missing_cnam) / (total_employees * 3) * 100) if total_employees > 0 else 100
            }
        }
    
    def _export_to_csv(self, report_data: Dict[str, Any], **kwargs) -> str:
        """Export report to CSV format"""
        # Extract tabular data based on report type
        tabular_data = self._extract_tabular_data(report_data)
        
        if not tabular_data:
            return ""
        
        field_mapping = kwargs.get('field_mapping', {})
        delimiter = kwargs.get('delimiter', ';')
        
        return ExportUtilities.to_csv_format_enhanced(
            tabular_data, field_mapping, delimiter
        )
    
    def _export_to_json(self, report_data: Dict[str, Any], **kwargs) -> str:
        """Export report to JSON format"""
        include_metadata = kwargs.get('include_metadata', True)
        return ExportUtilities.to_json_format_enhanced(
            report_data, include_metadata
        )
    
    def _export_to_xml(self, report_data: Dict[str, Any], **kwargs) -> str:
        """Export report to XML format"""
        tabular_data = self._extract_tabular_data(report_data)
        root_element = kwargs.get('root_element', 'employee_report')
        row_element = kwargs.get('row_element', 'employee')
        
        return ExportUtilities.to_xml_format(
            tabular_data, root_element, row_element
        )
    
    def _export_to_excel(self, report_data: Dict[str, Any], **kwargs) -> Dict[str, Any]:
        """Export report to Excel format (returns structured data for Excel generation)"""
        tabular_data = self._extract_tabular_data(report_data)
        title = kwargs.get('title', report_data.get('report_type', 'Employee Report'))
        include_summary = kwargs.get('include_summary', True)
        
        return ExportUtilities.to_excel_structured_format(
            tabular_data, title, include_summary
        )
    
    def _export_to_pdf(self, report_data: Dict[str, Any], **kwargs) -> bytes:
        """Export report to PDF format (placeholder - would need PDF generation library)"""
        # This would require a PDF generation library like reportlab
        # For now, return structured data that could be used by PDF generator
        return json.dumps(report_data, indent=2, ensure_ascii=False, default=str).encode('utf-8')
    
    def _extract_tabular_data(self, report_data: Dict[str, Any]) -> List[Dict[str, Any]]:
        """Extract tabular data from report based on report type"""
        report_type = report_data.get('report_type', '')
        
        # Map report types to their main data keys
        data_key_mapping = {
            'employee_directory': 'directory_entries',
            'organizational_hierarchy': 'hierarchy',
            'department_employee_list': 'department_listings',
            'employment_status': 'status_groups',
            'contract_status': 'contract_categories',
            'probation_tracking': 'probation_categories',
            'new_hire_report': 'new_hires',
            'termination_report': 'terminations',
            'anniversary_report': 'anniversaries',
            'career_progression': 'progression_data',
            'document_status': 'document_categories',
            'compliance_alerts': 'alert_categories',
            'registration_compliance': 'compliance_categories'
        }
        
        data_key = data_key_mapping.get(report_type)
        if not data_key:
            return []
        
        data = report_data.get(data_key)
        if not data:
            return []
        
        # Handle different data structures
        if isinstance(data, list):
            return data
        elif isinstance(data, dict):
            # Flatten nested structures
            flattened = []
            for category, items in data.items():
                if isinstance(items, list):
                    for item in items:
                        if isinstance(item, dict):
                            item['category'] = category
                            flattened.append(item)
            return flattened
        
        return []


# Django Admin Integration Functions

def get_employee_directory_for_admin(filters=None, include_photos=False):
    """Admin convenience function for employee directory"""
    manager = EmployeeReportManager()
    filter_obj = EmployeeReportFilter() if not filters else filters
    return manager.directory_reports.generate_complete_directory(
        filter_obj, include_photos
    )

def get_employment_status_report_for_admin(filters=None):
    """Admin convenience function for employment status report"""
    manager = EmployeeReportManager()
    filter_obj = EmployeeReportFilter() if not filters else filters
    return manager.status_reports.generate_employment_status_report(filter_obj)

def get_contract_expiry_report_for_admin(expiry_days=90, filters=None):
    """Admin convenience function for contract expiry report"""
    manager = EmployeeReportManager()
    filter_obj = EmployeeReportFilter() if not filters else filters
    return manager.status_reports.generate_contract_status_report(
        expiry_days, filter_obj
    )

def get_document_compliance_report_for_admin(expiry_days=90, filters=None):
    """Admin convenience function for document compliance report"""
    manager = EmployeeReportManager()
    filter_obj = EmployeeReportFilter() if not filters else filters
    return manager.compliance_reports.generate_document_status_report(
        expiry_days, filter_obj
    )

def get_hr_dashboard_summary_for_admin(filters=None):
    """Admin convenience function for HR dashboard"""
    manager = EmployeeReportManager()
    filter_obj = EmployeeReportFilter() if not filters else filters
    return manager.get_dashboard_summary(filter_obj)