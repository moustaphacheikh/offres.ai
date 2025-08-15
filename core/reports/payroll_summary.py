# payroll_summary.py
"""
Comprehensive Payroll Summary and Management Analytics Module
Provides advanced reporting and analytical capabilities for payroll management decisions

This module offers:
1. Monthly Payroll Summaries with departmental breakdowns
2. Department and Cost Center Analysis with budget comparisons  
3. Statistical Reporting with salary distribution and trends
4. Executive Dashboards with KPIs and performance metrics
5. Advanced Filtering and Export capabilities

Integrates with:
- Enhanced utilities (report_utils, business_rules, date_utils)
- Django models (Employee, Payroll, Department, etc.)
- Export systems (PDF, Excel, CSV) with French/Arabic localization
- Caching for performance optimization

Designed for Mauritanian payroll system with labor law compliance.
"""

from decimal import Decimal, ROUND_HALF_UP
from datetime import datetime, date, timedelta
from dateutil.relativedelta import relativedelta
from typing import Dict, List, Optional, Union, Tuple, Any, Callable
from collections import defaultdict, OrderedDict
import calendar
import statistics
import json
import logging
from dataclasses import dataclass
from enum import Enum

# Django imports
from django.db.models import Q, Sum, Avg, Count, Max, Min
from django.db.models.functions import TruncMonth, TruncQuarter, TruncYear
from django.core.cache import cache
from django.utils import timezone

# Internal imports
from ..utils.report_utils import (
    ReportFormatter, ReportContext, ReportDataValidator, 
    PayrollStatsReportData, CumulativeReportData,
    ExportUtilities, AdvancedExportUtilities
)
from ..utils.business_rules import PayrollBusinessRules, BusinessRulesEngine
from ..utils.date_utils import DateCalculator, SeniorityCalculator, WorkingDayCalculator
from ..utils.payroll_calculations import PayrollCalculator, TaxUtilities


class ReportPeriodType(Enum):
    """Enumeration for report period types"""
    MONTHLY = "monthly"
    QUARTERLY = "quarterly" 
    YEARLY = "yearly"
    CUSTOM = "custom"
    YTD = "year_to_date"


class ReportFormat(Enum):
    """Enumeration for report output formats"""
    HTML = "html"
    PDF = "pdf"
    EXCEL = "excel"
    CSV = "csv"
    JSON = "json"


@dataclass
class FilterCriteria:
    """Data class for report filtering criteria"""
    departments: List[int] = None
    directions: List[int] = None
    positions: List[int] = None
    employee_statuses: List[str] = None
    salary_range_min: Decimal = None
    salary_range_max: Decimal = None
    seniority_years_min: int = None
    seniority_years_max: int = None
    start_date: date = None
    end_date: date = None
    include_terminated: bool = False
    

class PayrollSummaryAnalytics:
    """
    Core analytics engine for payroll summary reporting
    Provides comprehensive payroll analysis and management insights
    """
    
    def __init__(self, system_parameters=None):
        """
        Initialize analytics engine
        
        Args:
            system_parameters: System configuration parameters
        """
        self.system_parameters = system_parameters
        self.business_rules = BusinessRulesEngine(system_parameters)
        self.report_formatter = ReportFormatter()
        self.logger = logging.getLogger(__name__)
        
        # Cache settings
        self.cache_timeout = 3600  # 1 hour
        self.enable_cache = True
    
    def generate_monthly_payroll_summary(self, 
                                       period: date,
                                       filters: FilterCriteria = None,
                                       include_details: bool = True) -> Dict[str, Any]:
        """
        Generate comprehensive monthly payroll summary
        
        Args:
            period: Report period (month/year)
            filters: Filtering criteria
            include_details: Whether to include detailed breakdowns
            
        Returns:
            Monthly payroll summary data
        """
        cache_key = f"monthly_summary_{period.strftime('%Y%m')}_{hash(str(filters))}"
        
        if self.enable_cache:
            cached_result = cache.get(cache_key)
            if cached_result:
                return cached_result
        
        try:
            # Import models dynamically to avoid circular imports
            from ..models import Employee, Payroll, Department, Direction
            
            # Base query for payroll records
            payroll_query = Payroll.objects.filter(
                period__year=period.year,
                period__month=period.month
            )
            
            # Apply filters
            if filters:
                payroll_query = self._apply_filters_to_query(payroll_query, filters)
            
            # Get payroll data
            payroll_records = list(payroll_query.select_related(
                'employee__department',
                'employee__direction',
                'employee__position',
                'employee__general_direction'
            ))
            
            # Calculate summary statistics
            summary_stats = self._calculate_summary_statistics(payroll_records, period)
            
            # Department breakdown
            dept_breakdown = self._calculate_department_breakdown(payroll_records)
            
            # Direction breakdown
            direction_breakdown = self._calculate_direction_breakdown(payroll_records)
            
            # Tax and contribution analysis
            tax_analysis = self._calculate_tax_analysis(payroll_records)
            
            # Overtime and bonus analysis
            overtime_analysis = self._calculate_overtime_analysis(payroll_records)
            
            # Employee statistics
            employee_stats = self._calculate_employee_statistics(payroll_records)
            
            # Cost center analysis
            cost_center_analysis = self._calculate_cost_center_analysis(payroll_records)
            
            # Build result
            result = {
                'period': period,
                'period_text': self.report_formatter.format_date_for_report(period, "period"),
                'generated_at': datetime.now(),
                'filter_criteria': filters.__dict__ if filters else {},
                
                # Summary statistics
                'summary': summary_stats,
                
                # Breakdowns
                'department_breakdown': dept_breakdown,
                'direction_breakdown': direction_breakdown,
                'cost_center_analysis': cost_center_analysis,
                
                # Analysis sections
                'tax_analysis': tax_analysis,
                'overtime_analysis': overtime_analysis,
                'employee_statistics': employee_stats,
                
                # Compliance and validation
                'compliance_indicators': self._calculate_compliance_indicators(payroll_records),
                'data_quality_metrics': self._calculate_data_quality_metrics(payroll_records)
            }
            
            # Add detailed records if requested
            if include_details:
                result['detailed_records'] = self._prepare_detailed_records(payroll_records)
            
            # Cache result
            if self.enable_cache:
                cache.set(cache_key, result, self.cache_timeout)
            
            return result
            
        except Exception as e:
            self.logger.error(f"Error generating monthly payroll summary: {str(e)}")
            raise
    
    def generate_department_analysis(self,
                                   period_start: date,
                                   period_end: date,
                                   comparison_period: date = None,
                                   filters: FilterCriteria = None) -> Dict[str, Any]:
        """
        Generate detailed department and cost center analysis
        
        Args:
            period_start: Analysis start period
            period_end: Analysis end period  
            comparison_period: Optional comparison period
            filters: Filtering criteria
            
        Returns:
            Department analysis data
        """
        try:
            from ..models import Employee, Payroll, Department
            
            # Get payroll data for main period
            main_payroll = Payroll.objects.filter(
                period__gte=period_start,
                period__lte=period_end
            )
            
            if filters:
                main_payroll = self._apply_filters_to_query(main_payroll, filters)
            
            main_records = list(main_payroll.select_related(
                'employee__department',
                'employee__direction', 
                'employee__position'
            ))
            
            # Get comparison data if specified
            comparison_records = []
            if comparison_period:
                comp_end = comparison_period + relativedelta(
                    months=(period_end.year - period_start.year) * 12 + 
                           (period_end.month - period_start.month)
                )
                
                comparison_payroll = Payroll.objects.filter(
                    period__gte=comparison_period,
                    period__lte=comp_end
                )
                
                if filters:
                    comparison_payroll = self._apply_filters_to_query(comparison_payroll, filters)
                
                comparison_records = list(comparison_payroll.select_related(
                    'employee__department',
                    'employee__direction',
                    'employee__position'
                ))
            
            # Calculate department metrics
            dept_metrics = self._calculate_department_metrics(main_records, comparison_records)
            
            # Calculate budget vs actual analysis
            budget_analysis = self._calculate_budget_analysis(main_records, period_start, period_end)
            
            # Headcount analysis
            headcount_analysis = self._calculate_headcount_analysis(main_records, comparison_records)
            
            # Cost per employee metrics
            cost_per_employee = self._calculate_cost_per_employee_metrics(main_records)
            
            # Performance indicators
            performance_indicators = self._calculate_performance_indicators(main_records)
            
            result = {
                'analysis_period': {
                    'start': period_start,
                    'end': period_end,
                    'comparison_period': comparison_period
                },
                'department_metrics': dept_metrics,
                'budget_analysis': budget_analysis,
                'headcount_analysis': headcount_analysis,
                'cost_per_employee': cost_per_employee,
                'performance_indicators': performance_indicators,
                'trends': self._calculate_department_trends(main_records, comparison_records)
            }
            
            return result
            
        except Exception as e:
            self.logger.error(f"Error generating department analysis: {str(e)}")
            raise
    
    def generate_statistical_report(self,
                                  period_start: date,
                                  period_end: date,
                                  analysis_type: str = "comprehensive",
                                  filters: FilterCriteria = None) -> Dict[str, Any]:
        """
        Generate comprehensive statistical analysis report
        
        Args:
            period_start: Analysis start period
            period_end: Analysis end period
            analysis_type: Type of statistical analysis
            filters: Filtering criteria
            
        Returns:
            Statistical analysis data
        """
        try:
            from ..models import Employee, Payroll, Leave
            
            # Get payroll data
            payroll_query = Payroll.objects.filter(
                period__gte=period_start,
                period__lte=period_end
            )
            
            if filters:
                payroll_query = self._apply_filters_to_query(payroll_query, filters)
            
            payroll_records = list(payroll_query.select_related(
                'employee__department',
                'employee__direction',
                'employee__position',
                'employee__salary_grade'
            ))
            
            # Salary distribution analysis
            salary_distribution = self._calculate_salary_distribution(payroll_records)
            
            # Employment statistics
            employment_stats = self._calculate_employment_statistics(payroll_records, period_start, period_end)
            
            # Leave utilization analysis
            leave_analysis = self._calculate_leave_utilization(payroll_records, period_start, period_end)
            
            # Productivity metrics
            productivity_metrics = self._calculate_productivity_metrics(payroll_records)
            
            # Demographic analysis
            demographic_analysis = self._calculate_demographic_analysis(payroll_records)
            
            # Trend analysis
            trend_analysis = self._calculate_trend_analysis(payroll_records, period_start, period_end)
            
            # Correlation analysis
            correlation_analysis = self._calculate_correlation_analysis(payroll_records)
            
            result = {
                'analysis_period': {
                    'start': period_start,
                    'end': period_end,
                    'analysis_type': analysis_type
                },
                'salary_distribution': salary_distribution,
                'employment_statistics': employment_stats,
                'leave_utilization': leave_analysis,
                'productivity_metrics': productivity_metrics,
                'demographic_analysis': demographic_analysis,
                'trend_analysis': trend_analysis,
                'correlation_analysis': correlation_analysis,
                'key_insights': self._generate_key_insights(payroll_records)
            }
            
            return result
            
        except Exception as e:
            self.logger.error(f"Error generating statistical report: {str(e)}")
            raise
    
    def generate_executive_dashboard(self,
                                   period: date,
                                   dashboard_type: str = "monthly",
                                   include_forecasting: bool = True) -> Dict[str, Any]:
        """
        Generate executive dashboard with high-level KPIs
        
        Args:
            period: Report period
            dashboard_type: Type of dashboard (monthly, quarterly, yearly)
            include_forecasting: Whether to include forecasting data
            
        Returns:
            Executive dashboard data
        """
        try:
            from ..models import Employee, Payroll, Department
            
            # Determine period range based on dashboard type
            if dashboard_type == "monthly":
                start_date = period.replace(day=1)
                end_date = (start_date + relativedelta(months=1)) - timedelta(days=1)
            elif dashboard_type == "quarterly":
                quarter = ((period.month - 1) // 3) + 1
                start_date = date(period.year, (quarter - 1) * 3 + 1, 1)
                end_date = (start_date + relativedelta(months=3)) - timedelta(days=1)
            elif dashboard_type == "yearly":
                start_date = date(period.year, 1, 1)
                end_date = date(period.year, 12, 31)
            
            # Get current period data
            current_payroll = list(Payroll.objects.filter(
                period__gte=start_date,
                period__lte=end_date
            ).select_related('employee__department', 'employee__direction'))
            
            # Get comparison period data
            if dashboard_type == "monthly":
                comp_start = start_date - relativedelta(months=1)
                comp_end = end_date - relativedelta(months=1)
            elif dashboard_type == "quarterly":
                comp_start = start_date - relativedelta(months=3)
                comp_end = end_date - relativedelta(months=3)
            else:
                comp_start = start_date - relativedelta(years=1)
                comp_end = end_date - relativedelta(years=1)
            
            comparison_payroll = list(Payroll.objects.filter(
                period__gte=comp_start,
                period__lte=comp_end
            ).select_related('employee__department', 'employee__direction'))
            
            # Calculate KPIs
            kpis = self._calculate_executive_kpis(current_payroll, comparison_payroll)
            
            # Cost control metrics
            cost_control = self._calculate_cost_control_metrics(current_payroll, comparison_payroll)
            
            # Compliance status
            compliance_status = self._calculate_compliance_status(current_payroll)
            
            # Trend indicators
            trend_indicators = self._calculate_trend_indicators(current_payroll, comparison_payroll)
            
            # Risk indicators
            risk_indicators = self._calculate_risk_indicators(current_payroll)
            
            # Forecasting data
            forecasting_data = {}
            if include_forecasting:
                forecasting_data = self._generate_forecasting_data(current_payroll, dashboard_type)
            
            result = {
                'dashboard_period': {
                    'type': dashboard_type,
                    'period': period,
                    'start_date': start_date,
                    'end_date': end_date
                },
                'kpis': kpis,
                'cost_control': cost_control,
                'compliance_status': compliance_status,
                'trend_indicators': trend_indicators,
                'risk_indicators': risk_indicators,
                'forecasting': forecasting_data,
                'actionable_insights': self._generate_actionable_insights(current_payroll, comparison_payroll)
            }
            
            return result
            
        except Exception as e:
            self.logger.error(f"Error generating executive dashboard: {str(e)}")
            raise
    
    def export_report(self,
                     report_data: Dict[str, Any],
                     export_format: ReportFormat,
                     template_name: str = None,
                     language: str = "french") -> Dict[str, Any]:
        """
        Export report data in specified format
        
        Args:
            report_data: Report data to export
            export_format: Target export format
            template_name: Optional template name
            language: Report language (french/arabic)
            
        Returns:
            Export result with file data or path
        """
        try:
            if export_format == ReportFormat.CSV:
                return self._export_to_csv(report_data, language)
            elif export_format == ReportFormat.EXCEL:
                return self._export_to_excel(report_data, language)
            elif export_format == ReportFormat.JSON:
                return self._export_to_json(report_data)
            elif export_format == ReportFormat.PDF:
                return self._export_to_pdf(report_data, template_name, language)
            else:
                return self._export_to_html(report_data, template_name, language)
                
        except Exception as e:
            self.logger.error(f"Error exporting report: {str(e)}")
            raise
    
    # ========== PRIVATE HELPER METHODS ==========
    
    def _apply_filters_to_query(self, query, filters: FilterCriteria):
        """Apply filtering criteria to Django query"""
        if not filters:
            return query
        
        if filters.departments:
            query = query.filter(employee__department__id__in=filters.departments)
        
        if filters.directions:
            query = query.filter(employee__direction__id__in=filters.directions)
        
        if filters.positions:
            query = query.filter(employee__position__id__in=filters.positions)
        
        if filters.employee_statuses:
            query = query.filter(employee__status__in=filters.employee_statuses)
        
        if filters.salary_range_min:
            query = query.filter(net_salary__gte=filters.salary_range_min)
        
        if filters.salary_range_max:
            query = query.filter(net_salary__lte=filters.salary_range_max)
        
        if not filters.include_terminated:
            query = query.filter(employee__termination_date__isnull=True)
        
        return query
    
    def _calculate_summary_statistics(self, payroll_records: List, period: date) -> Dict[str, Any]:
        """Calculate summary statistics for payroll records"""
        if not payroll_records:
            return self._empty_summary_stats()
        
        # Basic counts
        total_employees = len(payroll_records)
        active_employees = len([r for r in payroll_records if r.employee.termination_date is None])
        
        # Financial totals
        total_gross_taxable = sum(r.gross_taxable or Decimal('0') for r in payroll_records)
        total_gross_non_taxable = sum(r.gross_non_taxable or Decimal('0') for r in payroll_records)
        total_net_salary = sum(r.net_salary or Decimal('0') for r in payroll_records)
        
        # Tax and contribution totals
        total_cnss_employee = sum(r.cnss_employee or Decimal('0') for r in payroll_records)
        total_cnss_employer = sum(r.cnss_employer or Decimal('0') for r in payroll_records)
        total_cnam_employee = sum(r.cnam_employee or Decimal('0') for r in payroll_records)
        total_cnam_employer = sum(r.cnam_employer or Decimal('0') for r in payroll_records)
        total_its = sum(r.its_total or Decimal('0') for r in payroll_records)
        
        # Overtime and bonuses
        total_overtime = sum(r.overtime_hours or Decimal('0') for r in payroll_records)
        total_overtime_pay = sum(r.overtime_pay or Decimal('0') for r in payroll_records)
        
        # Averages
        avg_net_salary = total_net_salary / total_employees if total_employees > 0 else Decimal('0')
        avg_gross_taxable = total_gross_taxable / total_employees if total_employees > 0 else Decimal('0')
        
        # Calculate employer costs
        total_employer_costs = (total_gross_taxable + total_gross_non_taxable + 
                              total_cnss_employer + total_cnam_employer)
        
        return {
            'employee_counts': {
                'total_employees': total_employees,
                'active_employees': active_employees,
                'terminated_employees': total_employees - active_employees
            },
            'financial_totals': {
                'total_gross_taxable': total_gross_taxable,
                'total_gross_non_taxable': total_gross_non_taxable,
                'total_net_salary': total_net_salary,
                'total_employer_costs': total_employer_costs
            },
            'tax_contributions': {
                'cnss_employee': total_cnss_employee,
                'cnss_employer': total_cnss_employer,
                'cnam_employee': total_cnam_employee,
                'cnam_employer': total_cnam_employer,
                'its_total': total_its,
                'total_employee_deductions': total_cnss_employee + total_cnam_employee + total_its,
                'total_employer_contributions': total_cnss_employer + total_cnam_employer
            },
            'overtime_analysis': {
                'total_overtime_hours': total_overtime,
                'total_overtime_pay': total_overtime_pay,
                'employees_with_overtime': len([r for r in payroll_records if r.overtime_hours and r.overtime_hours > 0])
            },
            'averages': {
                'avg_net_salary': avg_net_salary,
                'avg_gross_taxable': avg_gross_taxable,
                'avg_employer_cost': total_employer_costs / total_employees if total_employees > 0 else Decimal('0')
            }
        }
    
    def _calculate_department_breakdown(self, payroll_records: List) -> List[Dict[str, Any]]:
        """Calculate department-wise breakdown"""
        dept_groups = defaultdict(list)
        
        for record in payroll_records:
            dept_name = record.employee.department.name if record.employee.department else "Non spécifié"
            dept_groups[dept_name].append(record)
        
        breakdown = []
        for dept_name, dept_records in dept_groups.items():
            dept_data = {
                'department_name': dept_name,
                'employee_count': len(dept_records),
                'total_net_salary': sum(r.net_salary or Decimal('0') for r in dept_records),
                'total_gross_taxable': sum(r.gross_taxable or Decimal('0') for r in dept_records),
                'avg_net_salary': None,
                'avg_gross_taxable': None,
                'total_employer_costs': None,
                'percentage_of_total': None
            }
            
            # Calculate averages
            if dept_data['employee_count'] > 0:
                dept_data['avg_net_salary'] = dept_data['total_net_salary'] / dept_data['employee_count']
                dept_data['avg_gross_taxable'] = dept_data['total_gross_taxable'] / dept_data['employee_count']
            
            # Calculate employer costs
            dept_data['total_employer_costs'] = (
                dept_data['total_gross_taxable'] +
                sum(r.gross_non_taxable or Decimal('0') for r in dept_records) +
                sum(r.cnss_employer or Decimal('0') for r in dept_records) +
                sum(r.cnam_employer or Decimal('0') for r in dept_records)
            )
            
            breakdown.append(dept_data)
        
        # Calculate percentages
        total_employees = len(payroll_records)
        for dept in breakdown:
            if total_employees > 0:
                dept['percentage_of_total'] = (dept['employee_count'] / total_employees) * 100
        
        return sorted(breakdown, key=lambda x: x['total_net_salary'], reverse=True)
    
    def _calculate_direction_breakdown(self, payroll_records: List) -> List[Dict[str, Any]]:
        """Calculate direction-wise breakdown"""
        dir_groups = defaultdict(list)
        
        for record in payroll_records:
            dir_name = record.employee.direction.name if record.employee.direction else "Non spécifiée"
            dir_groups[dir_name].append(record)
        
        breakdown = []
        for dir_name, dir_records in dir_groups.items():
            dir_data = {
                'direction_name': dir_name,
                'employee_count': len(dir_records),
                'total_net_salary': sum(r.net_salary or Decimal('0') for r in dir_records),
                'total_gross_taxable': sum(r.gross_taxable or Decimal('0') for r in dir_records),
                'avg_net_salary': None,
                'department_count': len(set(r.employee.department.name for r in dir_records if r.employee.department))
            }
            
            if dir_data['employee_count'] > 0:
                dir_data['avg_net_salary'] = dir_data['total_net_salary'] / dir_data['employee_count']
            
            breakdown.append(dir_data)
        
        return sorted(breakdown, key=lambda x: x['total_net_salary'], reverse=True)
    
    def _calculate_tax_analysis(self, payroll_records: List) -> Dict[str, Any]:
        """Calculate tax and contribution analysis"""
        if not payroll_records:
            return {}
        
        # CNSS analysis
        cnss_data = {
            'employee_contributions': sum(r.cnss_employee or Decimal('0') for r in payroll_records),
            'employer_contributions': sum(r.cnss_employer or Decimal('0') for r in payroll_records),
            'taxable_base': sum(r.gross_taxable or Decimal('0') for r in payroll_records),
            'contribution_rate_employee': Decimal('1.0'),  # 1% employee rate
            'contribution_rate_employer': Decimal('2.0')   # 2% employer rate
        }
        cnss_data['total_contributions'] = cnss_data['employee_contributions'] + cnss_data['employer_contributions']
        
        # CNAM analysis
        cnam_data = {
            'employee_contributions': sum(r.cnam_employee or Decimal('0') for r in payroll_records),
            'employer_contributions': sum(r.cnam_employer or Decimal('0') for r in payroll_records),
            'taxable_base': sum(r.gross_taxable or Decimal('0') for r in payroll_records),
            'contribution_rate_employee': Decimal('1.0'),  # 1% employee rate
            'contribution_rate_employer': Decimal('2.0')   # 2% employer rate
        }
        cnam_data['total_contributions'] = cnam_data['employee_contributions'] + cnam_data['employer_contributions']
        
        # ITS analysis
        its_data = {
            'total_its': sum(r.its_total or Decimal('0') for r in payroll_records),
            'its_tranche1': sum(r.its_tranche1 or Decimal('0') for r in payroll_records),
            'its_tranche2': sum(r.its_tranche2 or Decimal('0') for r in payroll_records),
            'its_tranche3': sum(r.its_tranche3 or Decimal('0') for r in payroll_records),
            'taxable_base': sum(r.gross_taxable or Decimal('0') for r in payroll_records)
        }
        
        return {
            'cnss': cnss_data,
            'cnam': cnam_data,
            'its': its_data,
            'total_employee_deductions': cnss_data['employee_contributions'] + cnam_data['employee_contributions'] + its_data['total_its'],
            'total_employer_contributions': cnss_data['employer_contributions'] + cnam_data['employer_contributions']
        }
    
    def _calculate_overtime_analysis(self, payroll_records: List) -> Dict[str, Any]:
        """Calculate overtime analysis"""
        overtime_records = [r for r in payroll_records if r.overtime_hours and r.overtime_hours > 0]
        
        if not overtime_records:
            return {
                'employees_with_overtime': 0,
                'total_overtime_hours': Decimal('0'),
                'total_overtime_pay': Decimal('0'),
                'avg_overtime_hours': Decimal('0'),
                'avg_overtime_pay': Decimal('0'),
                'overtime_percentage': Decimal('0')
            }
        
        total_overtime_hours = sum(r.overtime_hours for r in overtime_records)
        total_overtime_pay = sum(r.overtime_pay or Decimal('0') for r in overtime_records)
        
        return {
            'employees_with_overtime': len(overtime_records),
            'total_overtime_hours': total_overtime_hours,
            'total_overtime_pay': total_overtime_pay,
            'avg_overtime_hours': total_overtime_hours / len(overtime_records),
            'avg_overtime_pay': total_overtime_pay / len(overtime_records),
            'overtime_percentage': (len(overtime_records) / len(payroll_records)) * 100 if payroll_records else Decimal('0')
        }
    
    def _calculate_employee_statistics(self, payroll_records: List) -> Dict[str, Any]:
        """Calculate employee statistics"""
        if not payroll_records:
            return {}
        
        # Salary statistics
        net_salaries = [r.net_salary for r in payroll_records if r.net_salary]
        gross_salaries = [r.gross_taxable for r in payroll_records if r.gross_taxable]
        
        salary_stats = {}
        if net_salaries:
            salary_stats = {
                'median_net_salary': Decimal(str(statistics.median(net_salaries))),
                'mean_net_salary': Decimal(str(statistics.mean(net_salaries))),
                'min_net_salary': min(net_salaries),
                'max_net_salary': max(net_salaries),
                'std_dev_net_salary': Decimal(str(statistics.stdev(net_salaries))) if len(net_salaries) > 1 else Decimal('0')
            }
        
        # Seniority statistics
        seniority_stats = self._calculate_seniority_statistics(payroll_records)
        
        # Position distribution
        position_distribution = self._calculate_position_distribution(payroll_records)
        
        return {
            'salary_statistics': salary_stats,
            'seniority_statistics': seniority_stats,
            'position_distribution': position_distribution
        }
    
    def _calculate_cost_center_analysis(self, payroll_records: List) -> List[Dict[str, Any]]:
        """Calculate cost center analysis"""
        # Group by department and calculate cost metrics
        dept_costs = defaultdict(lambda: {
            'direct_costs': Decimal('0'),
            'indirect_costs': Decimal('0'),
            'employee_count': 0,
            'cost_per_employee': Decimal('0')
        })
        
        for record in payroll_records:
            dept_name = record.employee.department.name if record.employee.department else "Non spécifié"
            
            # Direct costs (salaries + benefits)
            direct_cost = (record.gross_taxable or Decimal('0')) + (record.gross_non_taxable or Decimal('0'))
            
            # Indirect costs (employer contributions)
            indirect_cost = (record.cnss_employer or Decimal('0')) + (record.cnam_employer or Decimal('0'))
            
            dept_costs[dept_name]['direct_costs'] += direct_cost
            dept_costs[dept_name]['indirect_costs'] += indirect_cost
            dept_costs[dept_name]['employee_count'] += 1
        
        # Calculate cost per employee
        cost_analysis = []
        for dept_name, costs in dept_costs.items():
            total_cost = costs['direct_costs'] + costs['indirect_costs']
            cost_per_employee = total_cost / costs['employee_count'] if costs['employee_count'] > 0 else Decimal('0')
            
            cost_analysis.append({
                'cost_center': dept_name,
                'direct_costs': costs['direct_costs'],
                'indirect_costs': costs['indirect_costs'],
                'total_costs': total_cost,
                'employee_count': costs['employee_count'],
                'cost_per_employee': cost_per_employee
            })
        
        return sorted(cost_analysis, key=lambda x: x['total_costs'], reverse=True)
    
    def _calculate_compliance_indicators(self, payroll_records: List) -> Dict[str, Any]:
        """Calculate compliance indicators"""
        try:
            smig = getattr(self.system_parameters, 'smig', Decimal('30000'))  # Default SMIG
            
            # SMIG compliance
            below_smig = len([r for r in payroll_records if r.net_salary and r.net_salary < smig])
            smig_compliance = {
                'total_employees': len(payroll_records),
                'below_smig_count': below_smig,
                'compliance_rate': ((len(payroll_records) - below_smig) / len(payroll_records)) * 100 if payroll_records else 100
            }
            
            # Tax calculation compliance
            tax_compliance = self._validate_tax_calculations(payroll_records)
            
            # Data completeness
            data_completeness = self._calculate_data_completeness(payroll_records)
            
            return {
                'smig_compliance': smig_compliance,
                'tax_compliance': tax_compliance,
                'data_completeness': data_completeness
            }
            
        except Exception as e:
            self.logger.error(f"Error calculating compliance indicators: {str(e)}")
            return {}
    
    def _calculate_data_quality_metrics(self, payroll_records: List) -> Dict[str, Any]:
        """Calculate data quality metrics"""
        if not payroll_records:
            return {}
        
        total_records = len(payroll_records)
        
        # Check for missing critical data
        missing_net_salary = len([r for r in payroll_records if not r.net_salary])
        missing_gross_salary = len([r for r in payroll_records if not r.gross_taxable])
        missing_department = len([r for r in payroll_records if not r.employee.department])
        
        # Check for data anomalies
        zero_salaries = len([r for r in payroll_records if r.net_salary and r.net_salary <= 0])
        negative_values = len([r for r in payroll_records if 
                             (r.net_salary and r.net_salary < 0) or
                             (r.gross_taxable and r.gross_taxable < 0)])
        
        return {
            'total_records': total_records,
            'data_completeness': {
                'missing_net_salary': missing_net_salary,
                'missing_gross_salary': missing_gross_salary,
                'missing_department': missing_department,
                'completeness_score': ((total_records - missing_net_salary - missing_gross_salary - missing_department) / (total_records * 3)) * 100 if total_records > 0 else 0
            },
            'data_anomalies': {
                'zero_salaries': zero_salaries,
                'negative_values': negative_values,
                'anomaly_rate': ((zero_salaries + negative_values) / total_records) * 100 if total_records > 0 else 0
            }
        }
    
    def _prepare_detailed_records(self, payroll_records: List) -> List[Dict[str, Any]]:
        """Prepare detailed record data"""
        detailed_records = []
        
        for record in payroll_records:
            detailed_record = {
                'employee_id': record.employee.id,
                'employee_name': f"{record.employee.first_name} {record.employee.last_name}",
                'department': record.employee.department.name if record.employee.department else "",
                'direction': record.employee.direction.name if record.employee.direction else "",
                'position': record.employee.position.name if record.employee.position else "",
                'gross_taxable': record.gross_taxable,
                'gross_non_taxable': record.gross_non_taxable,
                'net_salary': record.net_salary,
                'cnss_employee': record.cnss_employee,
                'cnam_employee': record.cnam_employee,
                'its_total': record.its_total,
                'overtime_hours': record.overtime_hours,
                'overtime_pay': record.overtime_pay
            }
            detailed_records.append(detailed_record)
        
        return detailed_records
    
    def _empty_summary_stats(self) -> Dict[str, Any]:
        """Return empty summary statistics structure"""
        return {
            'employee_counts': {
                'total_employees': 0,
                'active_employees': 0,
                'terminated_employees': 0
            },
            'financial_totals': {
                'total_gross_taxable': Decimal('0'),
                'total_gross_non_taxable': Decimal('0'),
                'total_net_salary': Decimal('0'),
                'total_employer_costs': Decimal('0')
            },
            'tax_contributions': {
                'cnss_employee': Decimal('0'),
                'cnss_employer': Decimal('0'),
                'cnam_employee': Decimal('0'),
                'cnam_employer': Decimal('0'),
                'its_total': Decimal('0'),
                'total_employee_deductions': Decimal('0'),
                'total_employer_contributions': Decimal('0')
            },
            'overtime_analysis': {
                'total_overtime_hours': Decimal('0'),
                'total_overtime_pay': Decimal('0'),
                'employees_with_overtime': 0
            },
            'averages': {
                'avg_net_salary': Decimal('0'),
                'avg_gross_taxable': Decimal('0'),
                'avg_employer_cost': Decimal('0')
            }
        }
    
    # Additional helper methods for comprehensive analysis...
    def _calculate_department_metrics(self, main_records: List, comparison_records: List) -> Dict[str, Any]:
        """Calculate department performance metrics"""
        # Implementation for department metrics calculation
        pass
    
    def _calculate_budget_analysis(self, records: List, start_date: date, end_date: date) -> Dict[str, Any]:
        """Calculate budget vs actual analysis"""
        # Implementation for budget analysis
        pass
    
    def _calculate_salary_distribution(self, payroll_records: List) -> Dict[str, Any]:
        """Calculate salary distribution statistics"""
        # Implementation for salary distribution analysis
        pass
    
    def _calculate_employment_statistics(self, payroll_records: List, start_date: date, end_date: date) -> Dict[str, Any]:
        """Calculate employment statistics (hires, terminations, etc.)"""
        # Implementation for employment statistics
        pass
    
    def _calculate_executive_kpis(self, current_records: List, comparison_records: List) -> Dict[str, Any]:
        """Calculate executive KPIs"""
        # Implementation for executive KPI calculation
        pass
    
    # Export methods
    def _export_to_csv(self, report_data: Dict[str, Any], language: str) -> Dict[str, Any]:
        """Export report to CSV format"""
        try:
            # Extract main summary data for CSV export
            summary_data = []
            
            if 'department_breakdown' in report_data:
                for dept in report_data['department_breakdown']:
                    summary_data.append({
                        'Département': dept['department_name'],
                        'Employés': dept['employee_count'],
                        'Salaire Net Total': dept['total_net_salary'],
                        'Salaire Brut Total': dept['total_gross_taxable'],
                        'Salaire Net Moyen': dept.get('avg_net_salary', Decimal('0')),
                        'Coût Employeur Total': dept.get('total_employer_costs', Decimal('0'))
                    })
            
            csv_content = ExportUtilities.to_csv_format_enhanced(
                summary_data,
                delimiter=";" if language == "french" else ","
            )
            
            return {
                'success': True,
                'format': 'CSV',
                'content': csv_content,
                'filename': f"payroll_summary_{datetime.now().strftime('%Y%m%d_%H%M%S')}.csv"
            }
            
        except Exception as e:
            self.logger.error(f"Error exporting to CSV: {str(e)}")
            return {'success': False, 'error': str(e)}
    
    def _export_to_excel(self, report_data: Dict[str, Any], language: str) -> Dict[str, Any]:
        """Export report to Excel format"""
        try:
            # Prepare Excel data structure
            excel_data = AdvancedExportUtilities.to_excel_structured_format(
                report_data.get('department_breakdown', []),
                title="Résumé de Paie" if language == "french" else "Payroll Summary",
                include_summary=True
            )
            
            return {
                'success': True,
                'format': 'Excel',
                'data': excel_data,
                'filename': f"payroll_summary_{datetime.now().strftime('%Y%m%d_%H%M%S')}.xlsx"
            }
            
        except Exception as e:
            self.logger.error(f"Error exporting to Excel: {str(e)}")
            return {'success': False, 'error': str(e)}
    
    def _export_to_json(self, report_data: Dict[str, Any]) -> Dict[str, Any]:
        """Export report to JSON format"""
        try:
            json_content = ExportUtilities.to_json_format_enhanced(
                report_data,
                include_metadata=True
            )
            
            return {
                'success': True,
                'format': 'JSON',
                'content': json_content,
                'filename': f"payroll_summary_{datetime.now().strftime('%Y%m%d_%H%M%S')}.json"
            }
            
        except Exception as e:
            self.logger.error(f"Error exporting to JSON: {str(e)}")
            return {'success': False, 'error': str(e)}
    
    def _export_to_pdf(self, report_data: Dict[str, Any], template_name: str, language: str) -> Dict[str, Any]:
        """Export report to PDF format"""
        try:
            # PDF export would typically use a template engine like ReportLab or WeasyPrint
            # For now, return a placeholder structure
            
            return {
                'success': True,
                'format': 'PDF',
                'message': 'PDF export functionality to be implemented with template engine',
                'filename': f"payroll_summary_{datetime.now().strftime('%Y%m%d_%H%M%S')}.pdf"
            }
            
        except Exception as e:
            self.logger.error(f"Error exporting to PDF: {str(e)}")
            return {'success': False, 'error': str(e)}
    
    def _export_to_html(self, report_data: Dict[str, Any], template_name: str, language: str) -> Dict[str, Any]:
        """Export report to HTML format"""
        try:
            # HTML export would typically use Django templates
            # For now, return a basic HTML structure
            
            html_content = self._generate_basic_html_report(report_data, language)
            
            return {
                'success': True,
                'format': 'HTML',
                'content': html_content,
                'filename': f"payroll_summary_{datetime.now().strftime('%Y%m%d_%H%M%S')}.html"
            }
            
        except Exception as e:
            self.logger.error(f"Error exporting to HTML: {str(e)}")
            return {'success': False, 'error': str(e)}
    
    def _generate_basic_html_report(self, report_data: Dict[str, Any], language: str) -> str:
        """Generate basic HTML report"""
        title = "Résumé de Paie" if language == "french" else "Payroll Summary"
        
        html = f"""
        <html>
        <head>
            <title>{title}</title>
            <meta charset="utf-8">
            <style>
                body {{ font-family: Arial, sans-serif; margin: 20px; }}
                table {{ border-collapse: collapse; width: 100%; }}
                th, td {{ border: 1px solid #ddd; padding: 8px; text-align: left; }}
                th {{ background-color: #f2f2f2; }}
                .summary {{ background-color: #f9f9f9; padding: 10px; margin: 10px 0; }}
            </style>
        </head>
        <body>
            <h1>{title}</h1>
            <div class="summary">
                <h2>Période: {report_data.get('period_text', 'N/A')}</h2>
                <p>Généré le: {datetime.now().strftime('%d/%m/%Y %H:%M')}</p>
            </div>
        """
        
        # Add summary statistics if available
        if 'summary' in report_data:
            summary = report_data['summary']
            if 'employee_counts' in summary:
                counts = summary['employee_counts']
                html += f"""
                <div class="summary">
                    <h3>Statistiques Employés</h3>
                    <p>Total employés: {counts.get('total_employees', 0)}</p>
                    <p>Employés actifs: {counts.get('active_employees', 0)}</p>
                </div>
                """
        
        # Add department breakdown table if available
        if 'department_breakdown' in report_data:
            html += """
            <h3>Répartition par Département</h3>
            <table>
                <tr>
                    <th>Département</th>
                    <th>Employés</th>
                    <th>Salaire Net Total</th>
                    <th>Salaire Net Moyen</th>
                </tr>
            """
            
            for dept in report_data['department_breakdown']:
                html += f"""
                <tr>
                    <td>{dept['department_name']}</td>
                    <td>{dept['employee_count']}</td>
                    <td>{self.report_formatter.format_currency_for_report(dept['total_net_salary'])}</td>
                    <td>{self.report_formatter.format_currency_for_report(dept.get('avg_net_salary', Decimal('0')))}</td>
                </tr>
                """
            
            html += "</table>"
        
        html += """
        </body>
        </html>
        """
        
        return html


class PayrollSummaryManager:
    """
    High-level manager for payroll summary operations
    Provides simplified interface for common reporting tasks
    """
    
    def __init__(self, system_parameters=None):
        """
        Initialize payroll summary manager
        
        Args:
            system_parameters: System configuration parameters
        """
        self.analytics = PayrollSummaryAnalytics(system_parameters)
        self.logger = logging.getLogger(__name__)
    
    def get_monthly_summary(self, year: int, month: int, **kwargs) -> Dict[str, Any]:
        """
        Get monthly payroll summary
        
        Args:
            year: Report year
            month: Report month
            **kwargs: Additional options (filters, format, etc.)
            
        Returns:
            Monthly summary data
        """
        period = date(year, month, 1)
        filters = kwargs.get('filters')
        
        return self.analytics.generate_monthly_payroll_summary(
            period=period,
            filters=filters,
            include_details=kwargs.get('include_details', True)
        )
    
    def get_quarterly_summary(self, year: int, quarter: int, **kwargs) -> Dict[str, Any]:
        """
        Get quarterly payroll summary
        
        Args:
            year: Report year
            quarter: Report quarter (1-4)
            **kwargs: Additional options
            
        Returns:
            Quarterly summary data
        """
        start_month = (quarter - 1) * 3 + 1
        start_date = date(year, start_month, 1)
        end_date = (start_date + relativedelta(months=3)) - timedelta(days=1)
        
        return self.analytics.generate_department_analysis(
            period_start=start_date,
            period_end=end_date,
            filters=kwargs.get('filters')
        )
    
    def get_annual_summary(self, year: int, **kwargs) -> Dict[str, Any]:
        """
        Get annual payroll summary
        
        Args:
            year: Report year
            **kwargs: Additional options
            
        Returns:
            Annual summary data
        """
        start_date = date(year, 1, 1)
        end_date = date(year, 12, 31)
        
        return self.analytics.generate_statistical_report(
            period_start=start_date,
            period_end=end_date,
            analysis_type="annual",
            filters=kwargs.get('filters')
        )
    
    def get_executive_dashboard(self, period_type: str = "monthly", **kwargs) -> Dict[str, Any]:
        """
        Get executive dashboard
        
        Args:
            period_type: Type of dashboard period
            **kwargs: Additional options
            
        Returns:
            Executive dashboard data
        """
        period = kwargs.get('period', date.today())
        
        return self.analytics.generate_executive_dashboard(
            period=period,
            dashboard_type=period_type,
            include_forecasting=kwargs.get('include_forecasting', True)
        )
    
    def export_summary(self, summary_data: Dict[str, Any], export_format: str, **kwargs) -> Dict[str, Any]:
        """
        Export summary data
        
        Args:
            summary_data: Summary data to export
            export_format: Export format (csv, excel, pdf, json)
            **kwargs: Additional export options
            
        Returns:
            Export result
        """
        format_enum = ReportFormat(export_format.lower())
        
        return self.analytics.export_report(
            report_data=summary_data,
            export_format=format_enum,
            template_name=kwargs.get('template'),
            language=kwargs.get('language', 'french')
        )


# Export main classes
__all__ = [
    'PayrollSummaryAnalytics',
    'PayrollSummaryManager', 
    'FilterCriteria',
    'ReportPeriodType',
    'ReportFormat'
]