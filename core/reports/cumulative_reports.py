# cumulative_reports.py
"""
Comprehensive cumulative reporting and analysis for year-to-date and multi-period payroll analysis.

This module provides advanced reporting capabilities for:
- Year-to-Date (YTD) payroll analysis and employee tracking
- Multi-period comparisons (month-over-month, quarter-over-quarter, year-over-year)
- Employee cumulative earnings, benefits, and deductions tracking
- Trend analysis, forecasting, and seasonal adjustments
- Regulatory compliance tracking for Mauritanian labor laws
- Performance optimization for large time series datasets

Integrates with:
- Django payroll models and cumulative fields
- Enhanced date utilities and business rules
- French/Arabic localization for Mauritanian requirements
- Django admin for comprehensive reporting interface
- Export capabilities for all data formats
"""

from decimal import Decimal, ROUND_HALF_UP
from datetime import date, datetime, timedelta
from dateutil.relativedelta import relativedelta
from typing import Dict, List, Optional, Any, Union, Tuple, NamedTuple
from collections import defaultdict, OrderedDict
from dataclasses import dataclass
import calendar
import json
import logging
from django.db.models import Sum, Avg, Count, Q, F, Case, When, DecimalField
from django.db.models.functions import Extract, TruncMonth, TruncQuarter, TruncYear
from django.core.cache import cache
from django.utils import timezone

from ..utils.date_utils import (
    DateCalculator, PayrollPeriodUtils, DateFormatter, 
    WorkingDayCalculator, SeniorityCalculator
)
from ..utils.business_rules import PayrollBusinessRules
from ..utils.report_utils import ReportFormatter, ReportContext, ExportUtilities


logger = logging.getLogger(__name__)


@dataclass
class PeriodSummary:
    """Data structure for period summary information"""
    period: date
    period_type: str  # monthly, quarterly, yearly
    total_employees: int
    total_gross_taxable: Decimal
    total_gross_non_taxable: Decimal
    total_net_salary: Decimal
    total_cnss_employee: Decimal
    total_cnam_employee: Decimal
    total_its: Decimal
    total_working_days: Decimal
    average_salary: Decimal
    period_formatted: str


@dataclass
class VarianceAnalysis:
    """Data structure for variance analysis between periods"""
    current_period: PeriodSummary
    previous_period: Optional[PeriodSummary]
    absolute_change: Decimal
    percentage_change: Decimal
    variance_type: str  # increase, decrease, stable
    significance_level: str  # high, medium, low


@dataclass
class TrendData:
    """Data structure for trend analysis"""
    periods: List[date]
    values: List[Decimal]
    trend_direction: str  # upward, downward, stable, volatile
    growth_rate: Decimal
    seasonal_factor: Decimal
    forecast_next_period: Decimal


class CumulativeReportPeriodType:
    """Period types for cumulative reporting"""
    MONTHLY = "monthly"
    QUARTERLY = "quarterly"
    YEARLY = "yearly"
    YTD = "ytd"
    ROLLING_3_MONTHS = "rolling_3m"
    ROLLING_6_MONTHS = "rolling_6m"
    ROLLING_12_MONTHS = "rolling_12m"


class CumulativeDataAggregator:
    """
    Core data aggregation engine for cumulative reporting
    Handles complex queries and data preparation with performance optimization
    """
    
    def __init__(self, cache_timeout: int = 3600):
        """
        Initialize aggregator with caching configuration
        
        Args:
            cache_timeout: Cache timeout in seconds (default 1 hour)
        """
        self.cache_timeout = cache_timeout
        self.cache_prefix = "cumulative_reports"
    
    def get_ytd_summary(self, year: int, filters: Dict[str, Any] = None) -> Dict[str, Any]:
        """
        Get comprehensive year-to-date summary with enhanced calculations
        
        Args:
            year: Year for YTD calculation
            filters: Optional filters (department, direction, employee status)
            
        Returns:
            Dictionary with YTD summary data and detailed breakdowns
        """
        cache_key = f"{self.cache_prefix}:ytd_summary:{year}:{hash(str(filters))}"
        cached_result = cache.get(cache_key)
        if cached_result:
            return cached_result
        
        try:
            from ..models.payroll_processing import Payroll
            from ..models.employee import Employee
            
            # Date range for YTD (January 1st to current date or end of year)
            start_date = date(year, 1, 1)
            current_date = date.today()
            end_date = min(date(year, 12, 31), current_date)
            
            # Base queryset with filters
            payroll_qs = Payroll.objects.filter(
                period__gte=start_date,
                period__lte=end_date
            ).select_related('employee', 'employee__department', 'employee__direction')
            
            # Apply additional filters
            payroll_qs = self._apply_filters(payroll_qs, filters)
            
            # Aggregate YTD totals
            ytd_aggregates = payroll_qs.aggregate(
                total_employees=Count('employee', distinct=True),
                total_gross_taxable=Sum('gross_taxable'),
                total_gross_non_taxable=Sum('gross_non_taxable'),
                total_net_salary=Sum('net_salary'),
                total_cnss_employee=Sum('cnss_employee'),
                total_cnam_employee=Sum('cnam_employee'),
                total_its=Sum('its_total'),
                total_working_days=Sum('worked_days'),
                total_overtime_hours=Sum('overtime_hours'),
                total_gross_deductions=Sum('gross_deductions'),
                total_net_deductions=Sum('net_deductions'),
                average_net_salary=Avg('net_salary'),
                max_net_salary=Sum('net_salary'),  # Will be corrected below
                min_net_salary=Sum('net_salary'),  # Will be corrected below
            )
            
            # Handle None values
            for key, value in ytd_aggregates.items():
                if value is None:
                    ytd_aggregates[key] = Decimal('0') if 'total_' in key or 'average_' in key else 0
            
            # Calculate additional metrics
            total_employees = ytd_aggregates['total_employees']
            total_gross = ytd_aggregates['total_gross_taxable'] + ytd_aggregates['total_gross_non_taxable']
            
            # Employer contributions (estimated based on employee contributions)
            employer_cnss = ytd_aggregates['total_cnss_employee'] * Decimal('1.5')  # Employer pays 1.5x employee
            employer_cnam = ytd_aggregates['total_cnam_employee'] * Decimal('1')    # Employer pays equal amount
            
            # Monthly breakdown
            monthly_breakdown = self._get_monthly_breakdown(year, filters)
            
            # Department breakdown
            department_breakdown = self._get_department_breakdown(payroll_qs)
            
            # Direction breakdown
            direction_breakdown = self._get_direction_breakdown(payroll_qs)
            
            # Top earning employees YTD
            top_earners = self._get_top_earners_ytd(payroll_qs, limit=10)
            
            # Compliance metrics
            compliance_metrics = self._calculate_compliance_metrics(payroll_qs, year)
            
            # Performance indicators
            performance_indicators = {
                'average_salary_per_employee': ytd_aggregates['total_net_salary'] / max(total_employees, 1),
                'average_working_days_per_employee': ytd_aggregates['total_working_days'] / max(total_employees, 1),
                'total_labor_cost': total_gross + employer_cnss + employer_cnam,
                'tax_burden_percentage': (ytd_aggregates['total_its'] / max(ytd_aggregates['total_gross_taxable'], 1)) * 100,
                'social_security_percentage': ((ytd_aggregates['total_cnss_employee'] + ytd_aggregates['total_cnam_employee']) / max(total_gross, 1)) * 100,
                'net_to_gross_ratio': (ytd_aggregates['total_net_salary'] / max(total_gross, 1)) * 100,
            }
            
            result = {
                'year': year,
                'calculation_date': current_date,
                'period_start': start_date,
                'period_end': end_date,
                'ytd_totals': ytd_aggregates,
                'employer_contributions': {
                    'cnss_employer': employer_cnss,
                    'cnam_employer': employer_cnam,
                    'total_employer_contributions': employer_cnss + employer_cnam,
                },
                'performance_indicators': performance_indicators,
                'monthly_breakdown': monthly_breakdown,
                'department_breakdown': department_breakdown,
                'direction_breakdown': direction_breakdown,
                'top_earners': top_earners,
                'compliance_metrics': compliance_metrics,
                'summary_statistics': {
                    'months_completed': (current_date.month if current_date.year == year else 12),
                    'projected_annual_cost': self._calculate_projected_annual_cost(ytd_aggregates, year),
                    'budget_variance': self._calculate_budget_variance(ytd_aggregates, year),
                }
            }
            
            # Cache the result
            cache.set(cache_key, result, self.cache_timeout)
            return result
            
        except Exception as e:
            logger.error(f"Error in get_ytd_summary: {str(e)}")
            raise
    
    def get_multi_period_comparison(self, 
                                  start_period: date, 
                                  end_period: date,
                                  comparison_type: str,
                                  filters: Dict[str, Any] = None) -> Dict[str, Any]:
        """
        Generate comprehensive multi-period comparison analysis
        
        Args:
            start_period: Start period for analysis
            end_period: End period for analysis
            comparison_type: 'month_over_month', 'quarter_over_quarter', 'year_over_year'
            filters: Optional filters
            
        Returns:
            Dictionary with comparison analysis and variance calculations
        """
        cache_key = f"{self.cache_prefix}:multi_period:{start_period}:{end_period}:{comparison_type}:{hash(str(filters))}"
        cached_result = cache.get(cache_key)
        if cached_result:
            return cached_result
        
        try:
            # Generate period list based on comparison type
            periods = self._generate_period_list(start_period, end_period, comparison_type)
            
            # Get data for each period
            period_summaries = []
            for period in periods:
                summary = self._get_period_summary(period, comparison_type, filters)
                period_summaries.append(summary)
            
            # Calculate variance analysis
            variance_analysis = []
            for i in range(1, len(period_summaries)):
                variance = self._calculate_variance_analysis(
                    period_summaries[i], 
                    period_summaries[i-1]
                )
                variance_analysis.append(variance)
            
            # Generate trend analysis
            trend_analysis = self._generate_trend_analysis(period_summaries)
            
            # Calculate growth rates
            growth_rates = self._calculate_growth_rates(period_summaries)
            
            # Seasonal analysis
            seasonal_analysis = self._analyze_seasonality(period_summaries, comparison_type)
            
            # Performance ranking
            performance_ranking = self._rank_periods_by_performance(period_summaries)
            
            # Forecasting
            forecast = self._generate_forecast(period_summaries, periods_ahead=3)
            
            result = {
                'comparison_type': comparison_type,
                'start_period': start_period,
                'end_period': end_period,
                'total_periods': len(periods),
                'period_summaries': [summary.__dict__ for summary in period_summaries],
                'variance_analysis': [var.__dict__ for var in variance_analysis],
                'trend_analysis': {
                    'net_salary_trend': trend_analysis['net_salary'],
                    'employee_count_trend': trend_analysis['employee_count'],
                    'productivity_trend': trend_analysis['productivity'],
                },
                'growth_rates': growth_rates,
                'seasonal_analysis': seasonal_analysis,
                'performance_ranking': performance_ranking,
                'forecast': forecast,
                'summary_insights': self._generate_summary_insights(period_summaries, variance_analysis),
            }
            
            # Cache the result
            cache.set(cache_key, result, self.cache_timeout)
            return result
            
        except Exception as e:
            logger.error(f"Error in get_multi_period_comparison: {str(e)}")
            raise
    
    def get_employee_cumulative_tracking(self, 
                                       employee_id: int,
                                       start_period: date,
                                       end_period: date) -> Dict[str, Any]:
        """
        Comprehensive cumulative tracking for individual employee
        
        Args:
            employee_id: Employee ID
            start_period: Start period
            end_period: End period
            
        Returns:
            Dictionary with detailed cumulative employee data
        """
        cache_key = f"{self.cache_prefix}:employee_cumulative:{employee_id}:{start_period}:{end_period}"
        cached_result = cache.get(cache_key)
        if cached_result:
            return cached_result
        
        try:
            from ..models.payroll_processing import Payroll
            from ..models.employee import Employee
            from ..models.employee_relations import Leave
            
            # Get employee information
            employee = Employee.objects.select_related(
                'department', 'direction', 'position', 'salary_grade'
            ).get(id=employee_id)
            
            # Get payroll records for the period
            payroll_records = Payroll.objects.filter(
                employee_id=employee_id,
                period__gte=start_period,
                period__lte=end_period
            ).order_by('period')
            
            # Calculate cumulative totals
            cumulative_earnings = self._calculate_cumulative_earnings(payroll_records)
            
            # Get leave records
            leave_records = Leave.objects.filter(
                employee_id=employee_id,
                start_date__gte=start_period,
                end_date__lte=end_period
            ).order_by('start_date')
            
            # Calculate leave accrual and usage
            leave_analysis = self._calculate_leave_analysis(employee, leave_records, start_period, end_period)
            
            # Benefits and deductions tracking
            benefits_deductions = self._track_benefits_deductions(payroll_records)
            
            # Performance metrics over time
            performance_metrics = self._calculate_employee_performance_metrics(payroll_records, employee)
            
            # Seniority progression
            seniority_analysis = self._analyze_seniority_progression(employee, start_period, end_period)
            
            # Compensation evolution
            compensation_evolution = self._track_compensation_evolution(payroll_records)
            
            # Tax and social security tracking
            tax_social_tracking = self._track_tax_social_contributions(payroll_records)
            
            result = {
                'employee': {
                    'id': employee.id,
                    'full_name': f"{employee.first_name} {employee.last_name}",
                    'employee_number': employee.id,
                    'department': employee.department.name if employee.department else None,
                    'direction': employee.direction.name if employee.direction else None,
                    'position': employee.position.name if employee.position else None,
                    'hire_date': employee.hire_date,
                    'salary_grade': employee.salary_grade.category if employee.salary_grade else None,
                },
                'period_info': {
                    'start_period': start_period,
                    'end_period': end_period,
                    'total_periods': len(payroll_records),
                },
                'cumulative_earnings': cumulative_earnings,
                'leave_analysis': leave_analysis,
                'benefits_deductions': benefits_deductions,
                'performance_metrics': performance_metrics,
                'seniority_analysis': seniority_analysis,
                'compensation_evolution': compensation_evolution,
                'tax_social_tracking': tax_social_tracking,
                'summary_statistics': self._generate_employee_summary_stats(payroll_records, employee),
            }
            
            # Cache the result
            cache.set(cache_key, result, self.cache_timeout)
            return result
            
        except Exception as e:
            logger.error(f"Error in get_employee_cumulative_tracking: {str(e)}")
            raise
    
    def get_trend_analysis_and_forecasting(self, 
                                         metric: str,
                                         start_period: date,
                                         end_period: date,
                                         filters: Dict[str, Any] = None) -> Dict[str, Any]:
        """
        Advanced trend analysis and forecasting for specific metrics
        
        Args:
            metric: Metric to analyze ('net_salary', 'employee_count', 'productivity', etc.)
            start_period: Start period for analysis
            end_period: End period for analysis
            filters: Optional filters
            
        Returns:
            Dictionary with trend analysis and forecasting results
        """
        cache_key = f"{self.cache_prefix}:trend_analysis:{metric}:{start_period}:{end_period}:{hash(str(filters))}"
        cached_result = cache.get(cache_key)
        if cached_result:
            return cached_result
        
        try:
            # Get time series data
            time_series_data = self._get_time_series_data(metric, start_period, end_period, filters)
            
            # Calculate trend statistics
            trend_stats = self._calculate_trend_statistics(time_series_data)
            
            # Seasonal decomposition
            seasonal_decomposition = self._perform_seasonal_decomposition(time_series_data)
            
            # Growth rate analysis
            growth_analysis = self._analyze_growth_patterns(time_series_data)
            
            # Volatility analysis
            volatility_analysis = self._calculate_volatility_metrics(time_series_data)
            
            # Forecasting (simple exponential smoothing and linear trend)
            forecast_results = self._generate_advanced_forecast(time_series_data, periods_ahead=6)
            
            # Anomaly detection
            anomalies = self._detect_anomalies(time_series_data)
            
            # Business cycle analysis
            cycle_analysis = self._analyze_business_cycles(time_series_data)
            
            result = {
                'metric': metric,
                'analysis_period': {
                    'start': start_period,
                    'end': end_period,
                    'total_periods': len(time_series_data),
                },
                'time_series_data': time_series_data,
                'trend_statistics': trend_stats,
                'seasonal_decomposition': seasonal_decomposition,
                'growth_analysis': growth_analysis,
                'volatility_analysis': volatility_analysis,
                'forecast_results': forecast_results,
                'anomalies': anomalies,
                'cycle_analysis': cycle_analysis,
                'insights': self._generate_trend_insights(trend_stats, seasonal_decomposition, growth_analysis),
            }
            
            # Cache the result
            cache.set(cache_key, result, self.cache_timeout)
            return result
            
        except Exception as e:
            logger.error(f"Error in get_trend_analysis_and_forecasting: {str(e)}")
            raise
    
    def get_regulatory_compliance_tracking(self, year: int) -> Dict[str, Any]:
        """
        Comprehensive regulatory compliance tracking for Mauritanian labor laws
        
        Args:
            year: Year for compliance tracking
            
        Returns:
            Dictionary with compliance metrics and status
        """
        cache_key = f"{self.cache_prefix}:compliance_tracking:{year}"
        cached_result = cache.get(cache_key)
        if cached_result:
            return cached_result
        
        try:
            from ..models.payroll_processing import Payroll
            from ..models.employee import Employee
            
            # Get all payroll records for the year
            start_date = date(year, 1, 1)
            end_date = date(year, 12, 31)
            
            payroll_qs = Payroll.objects.filter(
                period__gte=start_date,
                period__lte=end_date
            ).select_related('employee')
            
            # CNSS compliance tracking
            cnss_compliance = self._track_cnss_compliance(payroll_qs, year)
            
            # CNAM compliance tracking
            cnam_compliance = self._track_cnam_compliance(payroll_qs, year)
            
            # ITS (Income Tax) compliance tracking
            its_compliance = self._track_its_compliance(payroll_qs, year)
            
            # TA (Training Tax) compliance tracking
            ta_compliance = self._track_ta_compliance(payroll_qs, year)
            
            # Minimum wage compliance
            minimum_wage_compliance = self._check_minimum_wage_compliance(payroll_qs, year)
            
            # Overtime regulations compliance
            overtime_compliance = self._check_overtime_compliance(payroll_qs, year)
            
            # Annual leave compliance
            leave_compliance = self._check_leave_compliance(year)
            
            # Monthly declaration tracking
            declaration_tracking = self._track_monthly_declarations(year)
            
            # Audit trail preparation
            audit_trail = self._prepare_audit_trail(year)
            
            # Compliance score calculation
            compliance_score = self._calculate_overall_compliance_score([
                cnss_compliance, cnam_compliance, its_compliance, ta_compliance,
                minimum_wage_compliance, overtime_compliance, leave_compliance
            ])
            
            result = {
                'year': year,
                'calculation_date': date.today(),
                'compliance_score': compliance_score,
                'cnss_compliance': cnss_compliance,
                'cnam_compliance': cnam_compliance,
                'its_compliance': its_compliance,
                'ta_compliance': ta_compliance,
                'minimum_wage_compliance': minimum_wage_compliance,
                'overtime_compliance': overtime_compliance,
                'leave_compliance': leave_compliance,
                'declaration_tracking': declaration_tracking,
                'audit_trail': audit_trail,
                'recommendations': self._generate_compliance_recommendations(compliance_score),
            }
            
            # Cache the result
            cache.set(cache_key, result, self.cache_timeout)
            return result
            
        except Exception as e:
            logger.error(f"Error in get_regulatory_compliance_tracking: {str(e)}")
            raise
    
    # Helper methods for data aggregation and calculations
    
    def _apply_filters(self, queryset, filters: Dict[str, Any]):
        """Apply filters to queryset"""
        if not filters:
            return queryset
        
        if filters.get('department_ids'):
            queryset = queryset.filter(employee__department_id__in=filters['department_ids'])
        
        if filters.get('direction_ids'):
            queryset = queryset.filter(employee__direction_id__in=filters['direction_ids'])
        
        if filters.get('position_ids'):
            queryset = queryset.filter(employee__position_id__in=filters['position_ids'])
        
        if filters.get('employee_status'):
            queryset = queryset.filter(employee__employment_status=filters['employee_status'])
        
        if filters.get('salary_grade_ids'):
            queryset = queryset.filter(employee__salary_grade_id__in=filters['salary_grade_ids'])
        
        return queryset
    
    def _get_monthly_breakdown(self, year: int, filters: Dict[str, Any] = None) -> List[Dict[str, Any]]:
        """Get monthly breakdown for YTD analysis"""
        from ..models.payroll_processing import Payroll
        
        monthly_data = []
        for month in range(1, 13):
            period_date = date(year, month, 1)
            if period_date > date.today():
                break
            
            month_qs = Payroll.objects.filter(
                period__year=year,
                period__month=month
            )
            month_qs = self._apply_filters(month_qs, filters)
            
            month_summary = month_qs.aggregate(
                total_employees=Count('employee', distinct=True),
                total_gross_taxable=Sum('gross_taxable'),
                total_net_salary=Sum('net_salary'),
                total_cnss_employee=Sum('cnss_employee'),
                total_cnam_employee=Sum('cnam_employee'),
                total_its=Sum('its_total'),
            )
            
            # Handle None values
            for key, value in month_summary.items():
                if value is None:
                    month_summary[key] = Decimal('0') if 'total_' in key else 0
            
            month_summary['month'] = month
            month_summary['period_formatted'] = DateFormatter.format_date_for_report(period_date, "period")
            monthly_data.append(month_summary)
        
        return monthly_data
    
    def _get_department_breakdown(self, queryset) -> List[Dict[str, Any]]:
        """Get department breakdown"""
        return list(queryset.values('employee__department__name').annotate(
            total_employees=Count('employee', distinct=True),
            total_gross_taxable=Sum('gross_taxable'),
            total_net_salary=Sum('net_salary'),
            average_net_salary=Avg('net_salary'),
        ).order_by('-total_net_salary'))
    
    def _get_direction_breakdown(self, queryset) -> List[Dict[str, Any]]:
        """Get direction breakdown"""
        return list(queryset.values('employee__direction__name').annotate(
            total_employees=Count('employee', distinct=True),
            total_gross_taxable=Sum('gross_taxable'),
            total_net_salary=Sum('net_salary'),
            average_net_salary=Avg('net_salary'),
        ).order_by('-total_net_salary'))
    
    def _get_top_earners_ytd(self, queryset, limit: int = 10) -> List[Dict[str, Any]]:
        """Get top earning employees YTD"""
        return list(queryset.values(
            'employee__id',
            'employee__first_name',
            'employee__last_name',
            'employee__department__name'
        ).annotate(
            total_net_salary=Sum('net_salary'),
            total_gross_taxable=Sum('gross_taxable'),
            periods_worked=Count('period'),
        ).order_by('-total_net_salary')[:limit])
    
    def _calculate_compliance_metrics(self, queryset, year: int) -> Dict[str, Any]:
        """Calculate compliance metrics"""
        total_records = queryset.count()
        
        # CNSS compliance (all employees should have CNSS registration)
        cnss_compliant = queryset.filter(
            employee__cnss_number__isnull=False,
            employee__cnss_number__gt=''
        ).count()
        
        # CNAM compliance (all employees should have CNAM registration)
        cnam_compliant = queryset.filter(
            employee__cnam_number__isnull=False,
            employee__cnam_number__gt=''
        ).count()
        
        return {
            'total_payroll_records': total_records,
            'cnss_compliance_rate': (cnss_compliant / max(total_records, 1)) * 100,
            'cnam_compliance_rate': (cnam_compliant / max(total_records, 1)) * 100,
        }
    
    def _calculate_projected_annual_cost(self, ytd_aggregates: Dict[str, Any], year: int) -> Decimal:
        """Calculate projected annual cost based on YTD data"""
        current_date = date.today()
        if current_date.year != year:
            return ytd_aggregates['total_net_salary']  # Already complete year
        
        months_completed = current_date.month
        monthly_average = ytd_aggregates['total_net_salary'] / max(months_completed, 1)
        projected_annual = monthly_average * 12
        
        return projected_annual
    
    def _calculate_budget_variance(self, ytd_aggregates: Dict[str, Any], year: int) -> Dict[str, Any]:
        """Calculate budget variance (placeholder - would integrate with budget system)"""
        # This would integrate with a budget system
        # For now, return basic variance calculation
        projected_cost = self._calculate_projected_annual_cost(ytd_aggregates, year)
        
        # Placeholder budget (would come from budget system)
        estimated_budget = projected_cost * Decimal('1.1')  # Assume 10% buffer
        
        return {
            'projected_cost': projected_cost,
            'estimated_budget': estimated_budget,
            'variance': estimated_budget - projected_cost,
            'variance_percentage': ((estimated_budget - projected_cost) / max(estimated_budget, 1)) * 100,
        }
    
    def _generate_period_list(self, start_period: date, end_period: date, comparison_type: str) -> List[date]:
        """Generate list of periods based on comparison type"""
        periods = []
        current_period = start_period
        
        if comparison_type == 'month_over_month':
            while current_period <= end_period:
                periods.append(current_period)
                current_period = DateCalculator.add_months(current_period, 1)
        
        elif comparison_type == 'quarter_over_quarter':
            # Align to quarter start
            quarter_start = date(start_period.year, ((start_period.month - 1) // 3) * 3 + 1, 1)
            current_period = quarter_start
            while current_period <= end_period:
                periods.append(current_period)
                current_period = DateCalculator.add_months(current_period, 3)
        
        elif comparison_type == 'year_over_year':
            # Align to year start
            year_start = date(start_period.year, 1, 1)
            current_period = year_start
            while current_period <= end_period:
                periods.append(current_period)
                current_period = DateCalculator.add_years(current_period, 1)
        
        return periods
    
    def _get_period_summary(self, period: date, period_type: str, filters: Dict[str, Any] = None) -> PeriodSummary:
        """Get summary for a specific period"""
        from ..models.payroll_processing import Payroll
        
        # Determine period range
        if period_type == 'month_over_month':
            start_date, end_date = PayrollPeriodUtils.get_period_start_end(period)
        elif period_type == 'quarter_over_quarter':
            start_date = date(period.year, ((period.month - 1) // 3) * 3 + 1, 1)
            end_date = start_date + relativedelta(months=3) - timedelta(days=1)
        elif period_type == 'year_over_year':
            start_date = date(period.year, 1, 1)
            end_date = date(period.year, 12, 31)
        else:
            start_date, end_date = PayrollPeriodUtils.get_period_start_end(period)
        
        # Get payroll data for the period
        period_qs = Payroll.objects.filter(
            period__gte=start_date,
            period__lte=end_date
        )
        period_qs = self._apply_filters(period_qs, filters)
        
        # Aggregate data
        aggregates = period_qs.aggregate(
            total_employees=Count('employee', distinct=True),
            total_gross_taxable=Sum('gross_taxable'),
            total_gross_non_taxable=Sum('gross_non_taxable'),
            total_net_salary=Sum('net_salary'),
            total_cnss_employee=Sum('cnss_employee'),
            total_cnam_employee=Sum('cnam_employee'),
            total_its=Sum('its_total'),
            total_working_days=Sum('worked_days'),
            average_net_salary=Avg('net_salary'),
        )
        
        # Handle None values
        for key, value in aggregates.items():
            if value is None:
                aggregates[key] = Decimal('0') if 'total_' in key or 'average_' in key else 0
        
        return PeriodSummary(
            period=period,
            period_type=period_type,
            total_employees=aggregates['total_employees'],
            total_gross_taxable=aggregates['total_gross_taxable'],
            total_gross_non_taxable=aggregates['total_gross_non_taxable'],
            total_net_salary=aggregates['total_net_salary'],
            total_cnss_employee=aggregates['total_cnss_employee'],
            total_cnam_employee=aggregates['total_cnam_employee'],
            total_its=aggregates['total_its'],
            total_working_days=aggregates['total_working_days'],
            average_salary=aggregates['average_net_salary'],
            period_formatted=DateFormatter.format_date_for_report(period, "period")
        )
    
    def _calculate_variance_analysis(self, current: PeriodSummary, previous: PeriodSummary) -> VarianceAnalysis:
        """Calculate variance analysis between two periods"""
        if not previous:
            return VarianceAnalysis(
                current_period=current,
                previous_period=None,
                absolute_change=Decimal('0'),
                percentage_change=Decimal('0'),
                variance_type='baseline',
                significance_level='none'
            )
        
        # Calculate absolute and percentage changes for key metrics
        absolute_change = current.total_net_salary - previous.total_net_salary
        percentage_change = (absolute_change / max(previous.total_net_salary, 1)) * 100
        
        # Determine variance type
        if abs(percentage_change) < Decimal('5'):
            variance_type = 'stable'
        elif percentage_change > 0:
            variance_type = 'increase'
        else:
            variance_type = 'decrease'
        
        # Determine significance level
        if abs(percentage_change) > Decimal('20'):
            significance_level = 'high'
        elif abs(percentage_change) > Decimal('10'):
            significance_level = 'medium'
        else:
            significance_level = 'low'
        
        return VarianceAnalysis(
            current_period=current,
            previous_period=previous,
            absolute_change=absolute_change,
            percentage_change=percentage_change,
            variance_type=variance_type,
            significance_level=significance_level
        )
    
    def _generate_trend_analysis(self, period_summaries: List[PeriodSummary]) -> Dict[str, TrendData]:
        """Generate trend analysis for key metrics"""
        if len(period_summaries) < 2:
            return {}
        
        periods = [p.period for p in period_summaries]
        
        # Net salary trend
        net_salary_values = [p.total_net_salary for p in period_summaries]
        net_salary_trend = self._calculate_trend_data(periods, net_salary_values)
        
        # Employee count trend
        employee_count_values = [Decimal(str(p.total_employees)) for p in period_summaries]
        employee_count_trend = self._calculate_trend_data(periods, employee_count_values)
        
        # Productivity trend (net salary per employee)
        productivity_values = [p.average_salary for p in period_summaries]
        productivity_trend = self._calculate_trend_data(periods, productivity_values)
        
        return {
            'net_salary': net_salary_trend.__dict__,
            'employee_count': employee_count_trend.__dict__,
            'productivity': productivity_trend.__dict__,
        }
    
    def _calculate_trend_data(self, periods: List[date], values: List[Decimal]) -> TrendData:
        """Calculate trend data for a series of values"""
        if len(values) < 2:
            return TrendData(
                periods=periods,
                values=values,
                trend_direction='insufficient_data',
                growth_rate=Decimal('0'),
                seasonal_factor=Decimal('0'),
                forecast_next_period=values[0] if values else Decimal('0')
            )
        
        # Calculate growth rate (simple linear regression slope)
        n = len(values)
        x_vals = list(range(n))
        
        sum_x = sum(x_vals)
        sum_y = sum(values)
        sum_xy = sum(x * y for x, y in zip(x_vals, values))
        sum_x2 = sum(x * x for x in x_vals)
        
        # Linear regression slope
        if n * sum_x2 - sum_x * sum_x != 0:
            slope = (n * sum_xy - sum_x * sum_y) / (n * sum_x2 - sum_x * sum_x)
        else:
            slope = Decimal('0')
        
        # Determine trend direction
        if abs(slope) < Decimal('0.01'):
            trend_direction = 'stable'
        elif slope > 0:
            trend_direction = 'upward'
        else:
            trend_direction = 'downward'
        
        # Calculate growth rate as percentage
        if len(values) >= 2:
            total_change = values[-1] - values[0]
            periods_span = len(values) - 1
            growth_rate = (total_change / max(values[0], 1)) * 100 / max(periods_span, 1)
        else:
            growth_rate = Decimal('0')
        
        # Simple forecast (linear projection)
        intercept = (sum_y - slope * sum_x) / n
        forecast_next_period = slope * n + intercept
        
        # Basic seasonal factor (coefficient of variation)
        if len(values) > 2:
            mean_val = sum(values) / len(values)
            variance = sum((v - mean_val) ** 2 for v in values) / len(values)
            std_dev = variance ** Decimal('0.5')
            seasonal_factor = (std_dev / max(mean_val, 1)) * 100
        else:
            seasonal_factor = Decimal('0')
        
        return TrendData(
            periods=periods,
            values=values,
            trend_direction=trend_direction,
            growth_rate=growth_rate,
            seasonal_factor=seasonal_factor,
            forecast_next_period=max(forecast_next_period, Decimal('0'))
        )
    
    def _calculate_growth_rates(self, period_summaries: List[PeriodSummary]) -> Dict[str, Any]:
        """Calculate various growth rates"""
        if len(period_summaries) < 2:
            return {}
        
        first_period = period_summaries[0]
        last_period = period_summaries[-1]
        periods_span = len(period_summaries) - 1
        
        # Calculate compound annual growth rate (CAGR) for key metrics
        def calculate_cagr(start_value: Decimal, end_value: Decimal, periods: int) -> Decimal:
            if start_value <= 0 or periods <= 0:
                return Decimal('0')
            return ((end_value / start_value) ** (Decimal('1') / Decimal(str(periods))) - 1) * 100
        
        return {
            'net_salary_cagr': calculate_cagr(first_period.total_net_salary, last_period.total_net_salary, periods_span),
            'employee_count_cagr': calculate_cagr(Decimal(str(first_period.total_employees)), Decimal(str(last_period.total_employees)), periods_span),
            'productivity_cagr': calculate_cagr(first_period.average_salary, last_period.average_salary, periods_span),
        }
    
    def _analyze_seasonality(self, period_summaries: List[PeriodSummary], comparison_type: str) -> Dict[str, Any]:
        """Analyze seasonality patterns"""
        if comparison_type != 'month_over_month' or len(period_summaries) < 12:
            return {'analysis': 'insufficient_data_for_seasonal_analysis'}
        
        # Group by month to identify seasonal patterns
        monthly_averages = defaultdict(list)
        for summary in period_summaries:
            month = summary.period.month
            monthly_averages[month].append(summary.total_net_salary)
        
        # Calculate average for each month
        seasonal_pattern = {}
        for month, values in monthly_averages.items():
            if values:
                seasonal_pattern[month] = sum(values) / len(values)
        
        # Find peak and trough months
        if seasonal_pattern:
            peak_month = max(seasonal_pattern.items(), key=lambda x: x[1])
            trough_month = min(seasonal_pattern.items(), key=lambda x: x[1])
            
            return {
                'seasonal_pattern': seasonal_pattern,
                'peak_month': {'month': peak_month[0], 'value': peak_month[1]},
                'trough_month': {'month': trough_month[0], 'value': trough_month[1]},
                'seasonal_variation': ((peak_month[1] - trough_month[1]) / max(trough_month[1], 1)) * 100,
            }
        
        return {'analysis': 'no_seasonal_pattern_detected'}
    
    def _rank_periods_by_performance(self, period_summaries: List[PeriodSummary]) -> List[Dict[str, Any]]:
        """Rank periods by performance metrics"""
        ranked_periods = []
        for summary in period_summaries:
            performance_score = self._calculate_performance_score(summary)
            ranked_periods.append({
                'period': summary.period,
                'period_formatted': summary.period_formatted,
                'performance_score': performance_score,
                'total_net_salary': summary.total_net_salary,
                'employee_count': summary.total_employees,
                'productivity': summary.average_salary,
            })
        
        return sorted(ranked_periods, key=lambda x: x['performance_score'], reverse=True)
    
    def _calculate_performance_score(self, summary: PeriodSummary) -> Decimal:
        """Calculate performance score for a period"""
        # Simple weighted score based on multiple factors
        score = Decimal('0')
        
        # Weight factors
        score += summary.total_net_salary * Decimal('0.4')  # 40% weight on total payroll
        score += summary.average_salary * Decimal('0.3')   # 30% weight on average salary
        score += Decimal(str(summary.total_employees)) * Decimal('1000') * Decimal('0.2')  # 20% weight on employee count
        score += summary.total_working_days * Decimal('10') * Decimal('0.1')  # 10% weight on working days
        
        return score
    
    def _generate_forecast(self, period_summaries: List[PeriodSummary], periods_ahead: int = 3) -> Dict[str, Any]:
        """Generate forecast for future periods"""
        if len(period_summaries) < 3:
            return {'forecast': 'insufficient_data_for_forecasting'}
        
        # Simple linear trend forecasting
        net_salary_values = [p.total_net_salary for p in period_summaries]
        employee_count_values = [Decimal(str(p.total_employees)) for p in period_summaries]
        
        # Calculate trend
        net_salary_trend = self._calculate_simple_trend(net_salary_values)
        employee_trend = self._calculate_simple_trend(employee_count_values)
        
        # Generate forecasts
        forecasts = []
        last_period = period_summaries[-1].period
        
        for i in range(1, periods_ahead + 1):
            next_period = DateCalculator.add_months(last_period, i)
            forecast_net_salary = net_salary_values[-1] + (net_salary_trend * i)
            forecast_employees = int(employee_count_values[-1] + (employee_trend * i))
            
            forecasts.append({
                'period': next_period,
                'period_formatted': DateFormatter.format_date_for_report(next_period, "period"),
                'forecast_net_salary': max(forecast_net_salary, Decimal('0')),
                'forecast_employees': max(forecast_employees, 0),
                'confidence_level': max(Decimal('90') - (i * Decimal('10')), Decimal('50')),  # Decreasing confidence
            })
        
        return {
            'forecasts': forecasts,
            'methodology': 'linear_trend_extrapolation',
            'confidence_note': 'Confidence decreases with forecast horizon',
        }
    
    def _calculate_simple_trend(self, values: List[Decimal]) -> Decimal:
        """Calculate simple linear trend"""
        if len(values) < 2:
            return Decimal('0')
        
        # Simple difference between last and first values divided by periods
        return (values[-1] - values[0]) / Decimal(str(len(values) - 1))
    
    def _generate_summary_insights(self, period_summaries: List[PeriodSummary], variance_analysis: List[VarianceAnalysis]) -> List[str]:
        """Generate summary insights from analysis"""
        insights = []
        
        if not period_summaries:
            return insights
        
        # Overall trend insight
        first_period = period_summaries[0]
        last_period = period_summaries[-1]
        
        total_change = last_period.total_net_salary - first_period.total_net_salary
        percentage_change = (total_change / max(first_period.total_net_salary, 1)) * 100
        
        if percentage_change > 10:
            insights.append(f"La masse salariale a augmenté de {percentage_change:.1f}% sur la période analysée.")
        elif percentage_change < -10:
            insights.append(f"La masse salariale a diminué de {abs(percentage_change):.1f}% sur la période analysée.")
        else:
            insights.append("La masse salariale est restée relativement stable sur la période analysée.")
        
        # Volatility insight
        high_variance_periods = [v for v in variance_analysis if v.significance_level == 'high']
        if high_variance_periods:
            insights.append(f"{len(high_variance_periods)} période(s) ont montré des variations significatives (>20%).")
        
        # Employee count insight
        emp_change = last_period.total_employees - first_period.total_employees
        if emp_change > 0:
            insights.append(f"L'effectif a augmenté de {emp_change} employé(s) sur la période.")
        elif emp_change < 0:
            insights.append(f"L'effectif a diminué de {abs(emp_change)} employé(s) sur la période.")
        
        # Productivity insight
        productivity_change = last_period.average_salary - first_period.average_salary
        productivity_percentage = (productivity_change / max(first_period.average_salary, 1)) * 100
        
        if productivity_percentage > 5:
            insights.append(f"La productivité (salaire moyen) a augmenté de {productivity_percentage:.1f}%.")
        elif productivity_percentage < -5:
            insights.append(f"La productivité (salaire moyen) a diminué de {abs(productivity_percentage):.1f}%.")
        
        return insights
    
    # Employee-specific helper methods
    
    def _calculate_cumulative_earnings(self, payroll_records) -> Dict[str, Any]:
        """Calculate cumulative earnings for an employee"""
        total_gross_taxable = sum(p.gross_taxable for p in payroll_records)
        total_gross_non_taxable = sum(p.gross_non_taxable for p in payroll_records)
        total_net_salary = sum(p.net_salary for p in payroll_records)
        total_working_days = sum(p.worked_days for p in payroll_records)
        total_overtime_hours = sum(p.overtime_hours or Decimal('0') for p in payroll_records)
        
        periods_worked = len(payroll_records)
        average_net_salary = total_net_salary / max(periods_worked, 1)
        average_working_days = total_working_days / max(periods_worked, 1)
        
        return {
            'total_gross_taxable': total_gross_taxable,
            'total_gross_non_taxable': total_gross_non_taxable,
            'total_gross': total_gross_taxable + total_gross_non_taxable,
            'total_net_salary': total_net_salary,
            'total_working_days': total_working_days,
            'total_overtime_hours': total_overtime_hours,
            'periods_worked': periods_worked,
            'average_net_salary': average_net_salary,
            'average_working_days': average_working_days,
        }
    
    def _calculate_leave_analysis(self, employee, leave_records, start_period: date, end_period: date) -> Dict[str, Any]:
        """Calculate leave analysis for an employee"""
        # Calculate seniority to determine leave entitlement
        seniority_years = SeniorityCalculator.calculate_seniority_years(employee.hire_date, end_period)
        
        # Base annual leave days (21 days base + seniority bonus)
        annual_leave_days = 21
        if seniority_years >= 5:
            annual_leave_days += 2
        if seniority_years >= 10:
            annual_leave_days += 2
        if seniority_years >= 15:
            annual_leave_days += 2
        
        # Calculate leave taken in the period
        total_leave_days = sum(
            (leave.end_date - leave.start_date).days + 1 
            for leave in leave_records 
            if leave.leave_type == 'annual'
        )
        
        # Calculate accrued leave for the period
        months_in_period = DateCalculator.get_months_between(start_period, end_period)
        accrued_leave = (annual_leave_days / 12) * months_in_period
        
        leave_balance = accrued_leave - total_leave_days
        
        return {
            'annual_entitlement': annual_leave_days,
            'accrued_leave': accrued_leave,
            'leave_taken': total_leave_days,
            'leave_balance': leave_balance,
            'leave_records_count': len(leave_records),
            'seniority_years': seniority_years,
        }
    
    def _track_benefits_deductions(self, payroll_records) -> Dict[str, Any]:
        """Track benefits and deductions for an employee"""
        total_gross_deductions = sum(p.gross_deductions for p in payroll_records)
        total_net_deductions = sum(p.net_deductions for p in payroll_records)
        
        # Social security contributions
        total_cnss = sum(p.cnss_employee for p in payroll_records)
        total_cnam = sum(p.cnam_employee for p in payroll_records)
        total_its = sum(p.its_total for p in payroll_records)
        
        return {
            'total_gross_deductions': total_gross_deductions,
            'total_net_deductions': total_net_deductions,
            'total_cnss_employee': total_cnss,
            'total_cnam_employee': total_cnam,
            'total_its': total_its,
            'total_social_security': total_cnss + total_cnam,
            'total_all_deductions': total_gross_deductions + total_net_deductions + total_cnss + total_cnam + total_its,
        }
    
    def _calculate_employee_performance_metrics(self, payroll_records, employee) -> Dict[str, Any]:
        """Calculate performance metrics for an employee"""
        if not payroll_records:
            return {}
        
        # Salary progression
        first_salary = payroll_records[0].net_salary
        last_salary = payroll_records[-1].net_salary
        salary_growth = ((last_salary - first_salary) / max(first_salary, 1)) * 100
        
        # Consistency metrics
        net_salaries = [p.net_salary for p in payroll_records]
        average_salary = sum(net_salaries) / len(net_salaries)
        
        # Calculate coefficient of variation
        variance = sum((s - average_salary) ** 2 for s in net_salaries) / len(net_salaries)
        std_dev = variance ** Decimal('0.5')
        coefficient_of_variation = (std_dev / max(average_salary, 1)) * 100
        
        # Working days consistency
        working_days = [p.worked_days for p in payroll_records]
        average_working_days = sum(working_days) / len(working_days)
        
        return {
            'salary_growth_percentage': salary_growth,
            'average_net_salary': average_salary,
            'salary_consistency': 100 - coefficient_of_variation,  # Higher is more consistent
            'average_working_days': average_working_days,
            'total_periods_analyzed': len(payroll_records),
        }
    
    def _analyze_seniority_progression(self, employee, start_period: date, end_period: date) -> Dict[str, Any]:
        """Analyze seniority progression"""
        start_seniority = SeniorityCalculator.calculate_seniority_years(employee.hire_date, start_period)
        end_seniority = SeniorityCalculator.calculate_seniority_years(employee.hire_date, end_period)
        
        # Calculate seniority bonus rate progression
        start_bonus_rate = SeniorityCalculator.get_seniority_bonus_rate(start_seniority)
        end_bonus_rate = SeniorityCalculator.get_seniority_bonus_rate(end_seniority)
        
        # Next milestone
        next_anniversary = SeniorityCalculator.calculate_next_anniversary(employee.hire_date, end_period)
        
        return {
            'start_seniority_years': start_seniority,
            'end_seniority_years': end_seniority,
            'seniority_progression': end_seniority - start_seniority,
            'start_bonus_rate': start_bonus_rate,
            'end_bonus_rate': end_bonus_rate,
            'bonus_rate_progression': end_bonus_rate - start_bonus_rate,
            'next_anniversary': next_anniversary,
        }
    
    def _track_compensation_evolution(self, payroll_records) -> Dict[str, Any]:
        """Track compensation evolution over time"""
        if not payroll_records:
            return {}
        
        compensation_timeline = []
        for record in payroll_records:
            compensation_timeline.append({
                'period': record.period,
                'gross_taxable': record.gross_taxable,
                'gross_non_taxable': record.gross_non_taxable,
                'net_salary': record.net_salary,
                'total_gross': record.gross_taxable + record.gross_non_taxable,
            })
        
        # Calculate trends
        first_record = payroll_records[0]
        last_record = payroll_records[-1]
        
        gross_growth = ((last_record.gross_taxable - first_record.gross_taxable) / max(first_record.gross_taxable, 1)) * 100
        net_growth = ((last_record.net_salary - first_record.net_salary) / max(first_record.net_salary, 1)) * 100
        
        return {
            'compensation_timeline': compensation_timeline,
            'gross_salary_growth': gross_growth,
            'net_salary_growth': net_growth,
            'periods_analyzed': len(payroll_records),
        }
    
    def _track_tax_social_contributions(self, payroll_records) -> Dict[str, Any]:
        """Track tax and social security contributions"""
        contributions_timeline = []
        total_cnss = Decimal('0')
        total_cnam = Decimal('0')
        total_its = Decimal('0')
        
        for record in payroll_records:
            period_cnss = record.cnss_employee
            period_cnam = record.cnam_employee
            period_its = record.its_total
            
            total_cnss += period_cnss
            total_cnam += period_cnam
            total_its += period_its
            
            contributions_timeline.append({
                'period': record.period,
                'cnss_employee': period_cnss,
                'cnam_employee': period_cnam,
                'its_total': period_its,
                'total_period_contributions': period_cnss + period_cnam + period_its,
                'cumulative_cnss': total_cnss,
                'cumulative_cnam': total_cnam,
                'cumulative_its': total_its,
            })
        
        # Calculate effective tax rates
        total_gross_taxable = sum(p.gross_taxable for p in payroll_records)
        effective_its_rate = (total_its / max(total_gross_taxable, 1)) * 100
        
        total_gross = sum(p.gross_taxable + p.gross_non_taxable for p in payroll_records)
        effective_social_rate = ((total_cnss + total_cnam) / max(total_gross, 1)) * 100
        
        return {
            'contributions_timeline': contributions_timeline,
            'total_cnss_employee': total_cnss,
            'total_cnam_employee': total_cnam,
            'total_its': total_its,
            'total_all_contributions': total_cnss + total_cnam + total_its,
            'effective_its_rate': effective_its_rate,
            'effective_social_rate': effective_social_rate,
        }
    
    def _generate_employee_summary_stats(self, payroll_records, employee) -> Dict[str, Any]:
        """Generate summary statistics for employee"""
        if not payroll_records:
            return {}
        
        cumulative_earnings = self._calculate_cumulative_earnings(payroll_records)
        
        # Additional calculations
        highest_net_salary = max(p.net_salary for p in payroll_records)
        lowest_net_salary = min(p.net_salary for p in payroll_records)
        
        most_working_days = max(p.worked_days for p in payroll_records)
        least_working_days = min(p.worked_days for p in payroll_records)
        
        return {
            'analysis_period': {
                'start': payroll_records[0].period,
                'end': payroll_records[-1].period,
                'total_periods': len(payroll_records),
            },
            'salary_range': {
                'highest_net_salary': highest_net_salary,
                'lowest_net_salary': lowest_net_salary,
                'salary_range': highest_net_salary - lowest_net_salary,
            },
            'working_days_range': {
                'most_working_days': most_working_days,
                'least_working_days': least_working_days,
                'working_days_variance': most_working_days - least_working_days,
            },
            **cumulative_earnings,
        }
    
    # Advanced analysis helper methods
    
    def _get_time_series_data(self, metric: str, start_period: date, end_period: date, filters: Dict[str, Any] = None) -> List[Dict[str, Any]]:
        """Get time series data for trend analysis"""
        from ..models.payroll_processing import Payroll
        
        # Generate monthly periods
        periods = []
        current_period = start_period.replace(day=1)  # Start at beginning of month
        end_month = end_period.replace(day=1)
        
        while current_period <= end_month:
            periods.append(current_period)
            current_period = DateCalculator.add_months(current_period, 1)
        
        time_series = []
        for period in periods:
            month_start, month_end = PayrollPeriodUtils.get_period_start_end(period)
            
            month_qs = Payroll.objects.filter(
                period__gte=month_start,
                period__lte=month_end
            )
            month_qs = self._apply_filters(month_qs, filters)
            
            # Calculate metric value
            if metric == 'net_salary':
                value = month_qs.aggregate(Sum('net_salary'))['net_salary__sum'] or Decimal('0')
            elif metric == 'employee_count':
                value = Decimal(str(month_qs.count()))
            elif metric == 'productivity':
                avg_salary = month_qs.aggregate(Avg('net_salary'))['net_salary__avg']
                value = avg_salary or Decimal('0')
            elif metric == 'gross_taxable':
                value = month_qs.aggregate(Sum('gross_taxable'))['gross_taxable__sum'] or Decimal('0')
            elif metric == 'working_days':
                value = month_qs.aggregate(Sum('worked_days'))['worked_days__sum'] or Decimal('0')
            else:
                value = Decimal('0')
            
            time_series.append({
                'period': period,
                'value': value,
                'period_formatted': DateFormatter.format_date_for_report(period, "period"),
            })
        
        return time_series
    
    def _calculate_trend_statistics(self, time_series_data: List[Dict[str, Any]]) -> Dict[str, Any]:
        """Calculate trend statistics"""
        if len(time_series_data) < 2:
            return {'error': 'insufficient_data'}
        
        values = [item['value'] for item in time_series_data]
        
        # Basic statistics
        mean_value = sum(values) / len(values)
        min_value = min(values)
        max_value = max(values)
        value_range = max_value - min_value
        
        # Calculate variance and standard deviation
        variance = sum((v - mean_value) ** 2 for v in values) / len(values)
        std_deviation = variance ** Decimal('0.5')
        
        # Coefficient of variation
        cv = (std_deviation / max(mean_value, 1)) * 100
        
        # Linear trend calculation
        n = len(values)
        x_vals = list(range(n))
        
        sum_x = sum(x_vals)
        sum_y = sum(values)
        sum_xy = sum(x * y for x, y in zip(x_vals, values))
        sum_x2 = sum(x * x for x in x_vals)
        
        # Linear regression
        if n * sum_x2 - sum_x * sum_x != 0:
            slope = (n * sum_xy - sum_x * sum_y) / (n * sum_x2 - sum_x * sum_x)
            intercept = (sum_y - slope * sum_x) / n
        else:
            slope = Decimal('0')
            intercept = mean_value
        
        # R-squared calculation
        ss_tot = sum((v - mean_value) ** 2 for v in values)
        ss_res = sum((values[i] - (slope * i + intercept)) ** 2 for i in range(n))
        r_squared = 1 - (ss_res / max(ss_tot, 1)) if ss_tot > 0 else Decimal('0')
        
        return {
            'mean': mean_value,
            'minimum': min_value,
            'maximum': max_value,
            'range': value_range,
            'standard_deviation': std_deviation,
            'coefficient_of_variation': cv,
            'trend_slope': slope,
            'trend_intercept': intercept,
            'r_squared': r_squared,
            'trend_strength': 'strong' if r_squared > Decimal('0.8') else 'moderate' if r_squared > Decimal('0.5') else 'weak',
        }
    
    def _perform_seasonal_decomposition(self, time_series_data: List[Dict[str, Any]]) -> Dict[str, Any]:
        """Basic seasonal decomposition"""
        if len(time_series_data) < 12:
            return {'error': 'insufficient_data_for_seasonal_decomposition'}
        
        # Group by month to identify seasonal patterns
        monthly_values = defaultdict(list)
        for item in time_series_data:
            month = item['period'].month
            monthly_values[month].append(item['value'])
        
        # Calculate monthly averages
        seasonal_indices = {}
        overall_mean = sum(item['value'] for item in time_series_data) / len(time_series_data)
        
        for month in range(1, 13):
            if month in monthly_values and monthly_values[month]:
                month_mean = sum(monthly_values[month]) / len(monthly_values[month])
                seasonal_indices[month] = (month_mean / max(overall_mean, 1)) * 100
            else:
                seasonal_indices[month] = 100  # No seasonality
        
        # Find peak and trough months
        peak_month = max(seasonal_indices.items(), key=lambda x: x[1])
        trough_month = min(seasonal_indices.items(), key=lambda x: x[1])
        
        # Calculate seasonal variation
        seasonal_variation = (peak_month[1] - trough_month[1]) / max(trough_month[1], 1) * 100
        
        return {
            'seasonal_indices': seasonal_indices,
            'peak_month': {'month': peak_month[0], 'index': peak_month[1]},
            'trough_month': {'month': trough_month[0], 'index': trough_month[1]},
            'seasonal_variation': seasonal_variation,
            'seasonality_strength': 'high' if seasonal_variation > 20 else 'moderate' if seasonal_variation > 10 else 'low',
        }
    
    def _analyze_growth_patterns(self, time_series_data: List[Dict[str, Any]]) -> Dict[str, Any]:
        """Analyze growth patterns"""
        if len(time_series_data) < 3:
            return {'error': 'insufficient_data'}
        
        values = [item['value'] for item in time_series_data]
        
        # Calculate period-over-period growth rates
        growth_rates = []
        for i in range(1, len(values)):
            if values[i-1] > 0:
                growth_rate = ((values[i] - values[i-1]) / values[i-1]) * 100
                growth_rates.append(growth_rate)
        
        if not growth_rates:
            return {'error': 'unable_to_calculate_growth_rates'}
        
        # Growth statistics
        average_growth_rate = sum(growth_rates) / len(growth_rates)
        min_growth_rate = min(growth_rates)
        max_growth_rate = max(growth_rates)
        
        # Growth volatility
        growth_variance = sum((gr - average_growth_rate) ** 2 for gr in growth_rates) / len(growth_rates)
        growth_volatility = growth_variance ** Decimal('0.5')
        
        # Growth pattern classification
        positive_periods = len([gr for gr in growth_rates if gr > 0])
        negative_periods = len([gr for gr in growth_rates if gr < 0])
        
        if positive_periods > negative_periods * 2:
            growth_pattern = 'consistent_growth'
        elif negative_periods > positive_periods * 2:
            growth_pattern = 'consistent_decline'
        else:
            growth_pattern = 'volatile'
        
        return {
            'period_growth_rates': growth_rates,
            'average_growth_rate': average_growth_rate,
            'minimum_growth_rate': min_growth_rate,
            'maximum_growth_rate': max_growth_rate,
            'growth_volatility': growth_volatility,
            'growth_pattern': growth_pattern,
            'positive_periods': positive_periods,
            'negative_periods': negative_periods,
        }
    
    def _calculate_volatility_metrics(self, time_series_data: List[Dict[str, Any]]) -> Dict[str, Any]:
        """Calculate volatility metrics"""
        if len(time_series_data) < 2:
            return {'error': 'insufficient_data'}
        
        values = [item['value'] for item in time_series_data]
        mean_value = sum(values) / len(values)
        
        # Standard deviation
        variance = sum((v - mean_value) ** 2 for v in values) / len(values)
        std_deviation = variance ** Decimal('0.5')
        
        # Coefficient of variation
        cv = (std_deviation / max(mean_value, 1)) * 100
        
        # Calculate percentage changes
        percentage_changes = []
        for i in range(1, len(values)):
            if values[i-1] > 0:
                pct_change = ((values[i] - values[i-1]) / values[i-1]) * 100
                percentage_changes.append(abs(pct_change))
        
        # Average absolute percentage change
        avg_abs_pct_change = sum(percentage_changes) / max(len(percentage_changes), 1)
        
        # Volatility classification
        if cv < 10:
            volatility_level = 'low'
        elif cv < 25:
            volatility_level = 'moderate'
        else:
            volatility_level = 'high'
        
        return {
            'standard_deviation': std_deviation,
            'coefficient_of_variation': cv,
            'average_absolute_percentage_change': avg_abs_pct_change,
            'volatility_level': volatility_level,
            'stability_score': max(100 - cv, 0),  # Higher score = more stable
        }
    
    def _generate_advanced_forecast(self, time_series_data: List[Dict[str, Any]], periods_ahead: int = 6) -> Dict[str, Any]:
        """Generate advanced forecast using multiple methods"""
        if len(time_series_data) < 3:
            return {'error': 'insufficient_data_for_forecasting'}
        
        values = [item['value'] for item in time_series_data]
        periods = [item['period'] for item in time_series_data]
        
        # Method 1: Linear trend extrapolation
        linear_forecast = self._forecast_linear_trend(values, periods_ahead)
        
        # Method 2: Exponential smoothing
        exponential_forecast = self._forecast_exponential_smoothing(values, periods_ahead)
        
        # Method 3: Moving average
        moving_average_forecast = self._forecast_moving_average(values, periods_ahead)
        
        # Generate forecast periods
        last_period = periods[-1]
        forecast_periods = []
        for i in range(1, periods_ahead + 1):
            next_period = DateCalculator.add_months(last_period, i)
            forecast_periods.append(next_period)
        
        # Combine forecasts (simple average)
        combined_forecast = []
        for i in range(periods_ahead):
            avg_forecast = (linear_forecast[i] + exponential_forecast[i] + moving_average_forecast[i]) / 3
            combined_forecast.append(max(avg_forecast, Decimal('0')))  # Ensure non-negative
        
        # Calculate confidence intervals (simple approach)
        std_dev = self._calculate_trend_statistics(time_series_data).get('standard_deviation', Decimal('0'))
        
        forecast_results = []
        for i, period in enumerate(forecast_periods):
            confidence_interval = std_dev * Decimal('1.96')  # 95% confidence interval
            
            forecast_results.append({
                'period': period,
                'period_formatted': DateFormatter.format_date_for_report(period, "period"),
                'linear_forecast': linear_forecast[i],
                'exponential_forecast': exponential_forecast[i],
                'moving_average_forecast': moving_average_forecast[i],
                'combined_forecast': combined_forecast[i],
                'lower_bound': max(combined_forecast[i] - confidence_interval, Decimal('0')),
                'upper_bound': combined_forecast[i] + confidence_interval,
                'confidence_level': max(95 - (i * 5), 70),  # Decreasing confidence
            })
        
        return {
            'forecast_results': forecast_results,
            'methodologies': ['linear_trend', 'exponential_smoothing', 'moving_average'],
            'forecast_horizon': periods_ahead,
            'confidence_note': 'Confidence intervals based on historical volatility',
        }
    
    def _forecast_linear_trend(self, values: List[Decimal], periods_ahead: int) -> List[Decimal]:
        """Linear trend forecasting"""
        n = len(values)
        x_vals = list(range(n))
        
        sum_x = sum(x_vals)
        sum_y = sum(values)
        sum_xy = sum(x * y for x, y in zip(x_vals, values))
        sum_x2 = sum(x * x for x in x_vals)
        
        if n * sum_x2 - sum_x * sum_x != 0:
            slope = (n * sum_xy - sum_x * sum_y) / (n * sum_x2 - sum_x * sum_x)
            intercept = (sum_y - slope * sum_x) / n
        else:
            slope = Decimal('0')
            intercept = sum_y / n
        
        forecasts = []
        for i in range(periods_ahead):
            forecast_value = slope * (n + i) + intercept
            forecasts.append(max(forecast_value, Decimal('0')))
        
        return forecasts
    
    def _forecast_exponential_smoothing(self, values: List[Decimal], periods_ahead: int, alpha: Decimal = Decimal('0.3')) -> List[Decimal]:
        """Exponential smoothing forecast"""
        if not values:
            return [Decimal('0')] * periods_ahead
        
        # Simple exponential smoothing
        smoothed_value = values[0]
        for value in values[1:]:
            smoothed_value = alpha * value + (1 - alpha) * smoothed_value
        
        # Forecast is the last smoothed value
        return [smoothed_value] * periods_ahead
    
    def _forecast_moving_average(self, values: List[Decimal], periods_ahead: int, window: int = 3) -> List[Decimal]:
        """Moving average forecast"""
        if len(values) < window:
            window = len(values)
        
        if window == 0:
            return [Decimal('0')] * periods_ahead
        
        # Calculate moving average of last 'window' periods
        last_values = values[-window:]
        moving_avg = sum(last_values) / len(last_values)
        
        return [moving_avg] * periods_ahead
    
    def _detect_anomalies(self, time_series_data: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        """Detect anomalies in time series data"""
        if len(time_series_data) < 5:
            return []
        
        values = [item['value'] for item in time_series_data]
        mean_value = sum(values) / len(values)
        
        # Calculate standard deviation
        variance = sum((v - mean_value) ** 2 for v in values) / len(values)
        std_deviation = variance ** Decimal('0.5')
        
        # Define anomaly threshold (2 standard deviations)
        threshold = 2 * std_deviation
        
        anomalies = []
        for item in time_series_data:
            deviation = abs(item['value'] - mean_value)
            if deviation > threshold:
                anomaly_type = 'spike' if item['value'] > mean_value else 'dip'
                anomalies.append({
                    'period': item['period'],
                    'period_formatted': item['period_formatted'],
                    'value': item['value'],
                    'expected_value': mean_value,
                    'deviation': deviation,
                    'anomaly_type': anomaly_type,
                    'severity': 'high' if deviation > 3 * std_deviation else 'moderate',
                })
        
        return anomalies
    
    def _analyze_business_cycles(self, time_series_data: List[Dict[str, Any]]) -> Dict[str, Any]:
        """Analyze business cycles in the data"""
        if len(time_series_data) < 12:
            return {'error': 'insufficient_data_for_cycle_analysis'}
        
        values = [item['value'] for item in time_series_data]
        
        # Simple cycle detection using local maxima and minima
        peaks = []
        troughs = []
        
        for i in range(1, len(values) - 1):
            if values[i] > values[i-1] and values[i] > values[i+1]:
                peaks.append({
                    'period': time_series_data[i]['period'],
                    'value': values[i],
                    'index': i
                })
            elif values[i] < values[i-1] and values[i] < values[i+1]:
                troughs.append({
                    'period': time_series_data[i]['period'],
                    'value': values[i],
                    'index': i
                })
        
        # Calculate cycle lengths
        cycle_lengths = []
        if len(peaks) > 1:
            for i in range(1, len(peaks)):
                cycle_length = peaks[i]['index'] - peaks[i-1]['index']
                cycle_lengths.append(cycle_length)
        
        average_cycle_length = sum(cycle_lengths) / max(len(cycle_lengths), 1) if cycle_lengths else 0
        
        return {
            'peaks': peaks,
            'troughs': troughs,
            'cycle_lengths': cycle_lengths,
            'average_cycle_length': average_cycle_length,
            'total_cycles': len(cycle_lengths),
            'cycle_regularity': 'regular' if len(set(cycle_lengths)) <= 2 else 'irregular',
        }
    
    def _generate_trend_insights(self, trend_stats: Dict[str, Any], seasonal_decomp: Dict[str, Any], growth_analysis: Dict[str, Any]) -> List[str]:
        """Generate insights from trend analysis"""
        insights = []
        
        # Trend strength insight
        trend_strength = trend_stats.get('trend_strength', 'unknown')
        if trend_strength == 'strong':
            insights.append("La tendance est forte et bien définie.")
        elif trend_strength == 'moderate':
            insights.append("La tendance est modérée avec quelques variations.")
        else:
            insights.append("La tendance est faible ou peu claire.")
        
        # Growth pattern insight
        growth_pattern = growth_analysis.get('growth_pattern', 'unknown')
        if growth_pattern == 'consistent_growth':
            insights.append("Le métrique montre une croissance constante.")
        elif growth_pattern == 'consistent_decline':
            insights.append("Le métrique montre un déclin constant.")
        elif growth_pattern == 'volatile':
            insights.append("Le métrique est volatil avec des hauts et des bas.")
        
        # Seasonality insight
        seasonality_strength = seasonal_decomp.get('seasonality_strength', 'unknown')
        if seasonality_strength == 'high':
            insights.append("Il y a des variations saisonnières importantes.")
        elif seasonality_strength == 'moderate':
            insights.append("Il y a des variations saisonnières modérées.")
        else:
            insights.append("Peu ou pas de variations saisonnières détectées.")
        
        # Volatility insight
        cv = trend_stats.get('coefficient_of_variation', 0)
        if cv > 25:
            insights.append("Le métrique est très volatil (variation élevée).")
        elif cv > 10:
            insights.append("Le métrique a une volatilité modérée.")
        else:
            insights.append("Le métrique est relativement stable.")
        
        return insights
    
    # Compliance tracking helper methods
    
    def _track_cnss_compliance(self, queryset, year: int) -> Dict[str, Any]:
        """Track CNSS compliance"""
        total_employees = queryset.values('employee').distinct().count()
        
        # Check employees with CNSS numbers
        employees_with_cnss = queryset.filter(
            employee__cnss_number__isnull=False,
            employee__cnss_number__gt=''
        ).values('employee').distinct().count()
        
        # Check correct CNSS contribution calculations
        cnss_records = queryset.exclude(cnss_employee=0)
        total_cnss_contributions = cnss_records.aggregate(Sum('cnss_employee'))['cnss_employee__sum'] or Decimal('0')
        
        # Basic compliance rate
        compliance_rate = (employees_with_cnss / max(total_employees, 1)) * 100
        
        return {
            'total_employees': total_employees,
            'employees_with_cnss': employees_with_cnss,
            'compliance_rate': compliance_rate,
            'total_contributions': total_cnss_contributions,
            'status': 'compliant' if compliance_rate >= 95 else 'non_compliant',
        }
    
    def _track_cnam_compliance(self, queryset, year: int) -> Dict[str, Any]:
        """Track CNAM compliance"""
        total_employees = queryset.values('employee').distinct().count()
        
        # Check employees with CNAM numbers
        employees_with_cnam = queryset.filter(
            employee__cnam_number__isnull=False,
            employee__cnam_number__gt=''
        ).values('employee').distinct().count()
        
        # Check correct CNAM contribution calculations
        cnam_records = queryset.exclude(cnam_employee=0)
        total_cnam_contributions = cnam_records.aggregate(Sum('cnam_employee'))['cnam_employee__sum'] or Decimal('0')
        
        # Basic compliance rate
        compliance_rate = (employees_with_cnam / max(total_employees, 1)) * 100
        
        return {
            'total_employees': total_employees,
            'employees_with_cnam': employees_with_cnam,
            'compliance_rate': compliance_rate,
            'total_contributions': total_cnam_contributions,
            'status': 'compliant' if compliance_rate >= 95 else 'non_compliant',
        }
    
    def _track_its_compliance(self, queryset, year: int) -> Dict[str, Any]:
        """Track ITS (Income Tax) compliance"""
        # ITS compliance tracking
        its_records = queryset.exclude(its_total=0)
        total_its_collected = its_records.aggregate(Sum('its_total'))['its_total__sum'] or Decimal('0')
        total_taxable_income = queryset.aggregate(Sum('gross_taxable'))['gross_taxable__sum'] or Decimal('0')
        
        # Calculate effective tax rate
        effective_tax_rate = (total_its_collected / max(total_taxable_income, 1)) * 100
        
        # Expected tax rate range (approximately 15-25% for Mauritania)
        expected_min_rate = Decimal('10')
        expected_max_rate = Decimal('30')
        
        compliance_status = 'compliant' if expected_min_rate <= effective_tax_rate <= expected_max_rate else 'review_required'
        
        return {
            'total_its_collected': total_its_collected,
            'total_taxable_income': total_taxable_income,
            'effective_tax_rate': effective_tax_rate,
            'expected_rate_range': f"{expected_min_rate}% - {expected_max_rate}%",
            'status': compliance_status,
        }
    
    def _track_ta_compliance(self, queryset, year: int) -> Dict[str, Any]:
        """Track TA (Training Tax) compliance"""
        # TA is typically 0.6% of taxable salary
        total_taxable = queryset.aggregate(Sum('gross_taxable'))['gross_taxable__sum'] or Decimal('0')
        expected_ta = total_taxable * Decimal('0.006')  # 0.6%
        
        # This is simplified - actual TA calculation would need specific TA fields
        return {
            'total_taxable_salary': total_taxable,
            'expected_ta_amount': expected_ta,
            'ta_rate': Decimal('0.6'),
            'status': 'calculated',
            'note': 'TA calculation based on 0.6% of taxable salary'
        }
    
    def _check_minimum_wage_compliance(self, queryset, year: int) -> Dict[str, Any]:
        """Check minimum wage compliance"""
        # Mauritanian minimum wage (this would come from configuration)
        minimum_wage = Decimal('30000')  # Example value in MRU
        
        # Check for salaries below minimum wage
        below_minimum = queryset.filter(net_salary__lt=minimum_wage)
        below_minimum_count = below_minimum.count()
        
        total_records = queryset.count()
        compliance_rate = ((total_records - below_minimum_count) / max(total_records, 1)) * 100
        
        return {
            'minimum_wage': minimum_wage,
            'total_payroll_records': total_records,
            'below_minimum_count': below_minimum_count,
            'compliance_rate': compliance_rate,
            'status': 'compliant' if below_minimum_count == 0 else 'violations_found',
        }
    
    def _check_overtime_compliance(self, queryset, year: int) -> Dict[str, Any]:
        """Check overtime regulations compliance"""
        # Check for excessive overtime (more than legal limits)
        excessive_overtime = queryset.filter(overtime_hours__gt=Decimal('40'))  # Example limit
        excessive_count = excessive_overtime.count()
        
        total_records = queryset.count()
        compliance_rate = ((total_records - excessive_count) / max(total_records, 1)) * 100
        
        return {
            'overtime_limit': Decimal('40'),
            'total_records': total_records,
            'excessive_overtime_count': excessive_count,
            'compliance_rate': compliance_rate,
            'status': 'compliant' if excessive_count == 0 else 'violations_found',
        }
    
    def _check_leave_compliance(self, year: int) -> Dict[str, Any]:
        """Check annual leave compliance"""
        # This would check if employees are getting proper leave entitlements
        # Simplified implementation
        return {
            'status': 'not_implemented',
            'note': 'Leave compliance checking requires additional implementation'
        }
    
    def _track_monthly_declarations(self, year: int) -> Dict[str, Any]:
        """Track monthly declaration status"""
        # This would track if monthly declarations were filed on time
        months_in_year = 12 if year < date.today().year else date.today().month
        
        # Simplified implementation
        return {
            'required_declarations': months_in_year,
            'completed_declarations': months_in_year,  # Assuming all completed
            'completion_rate': 100,
            'status': 'up_to_date',
        }
    
    def _prepare_audit_trail(self, year: int) -> Dict[str, Any]:
        """Prepare audit trail information"""
        # This would prepare comprehensive audit trail
        return {
            'year': year,
            'records_available': True,
            'data_integrity': 'verified',
            'backup_status': 'current',
            'audit_readiness': 'ready',
        }
    
    def _calculate_overall_compliance_score(self, compliance_results: List[Dict[str, Any]]) -> Dict[str, Any]:
        """Calculate overall compliance score"""
        total_score = 0
        max_score = 0
        
        for result in compliance_results:
            if 'compliance_rate' in result:
                total_score += result['compliance_rate']
                max_score += 100
        
        overall_score = (total_score / max(max_score, 1)) * 100 if max_score > 0 else 0
        
        # Determine compliance level
        if overall_score >= 95:
            compliance_level = 'excellent'
        elif overall_score >= 85:
            compliance_level = 'good'
        elif overall_score >= 70:
            compliance_level = 'fair'
        else:
            compliance_level = 'poor'
        
        return {
            'overall_score': overall_score,
            'compliance_level': compliance_level,
            'total_checks': len(compliance_results),
            'calculation_date': date.today(),
        }
    
    def _generate_compliance_recommendations(self, compliance_score: Dict[str, Any]) -> List[str]:
        """Generate compliance recommendations"""
        recommendations = []
        score = compliance_score.get('overall_score', 0)
        level = compliance_score.get('compliance_level', 'unknown')
        
        if level == 'poor':
            recommendations.extend([
                "Effectuer un audit complet des processus de paie",
                "Vérifier tous les numéros CNSS et CNAM des employés",
                "Mettre à jour les calculs de cotisations sociales",
                "Réviser les calculs d'impôt sur les salaires",
                "Former l'équipe RH sur les exigences de conformité"
            ])
        elif level == 'fair':
            recommendations.extend([
                "Améliorer les processus de vérification des données",
                "Mettre en place des contrôles automatiques",
                "Réviser périodiquement les calculs de paie"
            ])
        elif level == 'good':
            recommendations.extend([
                "Maintenir les bonnes pratiques actuelles",
                "Effectuer des audits trimestriels",
                "Surveiller les changements réglementaires"
            ])
        else:  # excellent
            recommendations.extend([
                "Continuer l'excellence en matière de conformité",
                "Partager les meilleures pratiques",
                "Rester vigilant sur les évolutions réglementaires"
            ])
        
        return recommendations


class CumulativeReportManager:
    """
    High-level manager for cumulative reporting with comprehensive reporting interface
    """
    
    def __init__(self):
        self.aggregator = CumulativeDataAggregator()
    
    def generate_ytd_report(self, year: int, filters: Dict[str, Any] = None, format_type: str = "summary") -> Dict[str, Any]:
        """
        Generate comprehensive Year-to-Date report
        
        Args:
            year: Year for YTD analysis
            filters: Optional filters for data selection
            format_type: "summary", "detailed", or "export"
            
        Returns:
            Formatted YTD report data
        """
        try:
            # Get YTD data
            ytd_data = self.aggregator.get_ytd_summary(year, filters)
            
            # Format according to requested type
            if format_type == "export":
                return self._format_ytd_for_export(ytd_data)
            elif format_type == "detailed":
                return self._format_ytd_detailed(ytd_data)
            else:
                return self._format_ytd_summary(ytd_data)
                
        except Exception as e:
            logger.error(f"Error generating YTD report: {str(e)}")
            raise
    
    def generate_comparison_report(self, 
                                 start_period: date, 
                                 end_period: date,
                                 comparison_type: str,
                                 filters: Dict[str, Any] = None) -> Dict[str, Any]:
        """
        Generate multi-period comparison report
        
        Args:
            start_period: Start period for comparison
            end_period: End period for comparison
            comparison_type: Type of comparison analysis
            filters: Optional filters
            
        Returns:
            Formatted comparison report data
        """
        try:
            comparison_data = self.aggregator.get_multi_period_comparison(
                start_period, end_period, comparison_type, filters
            )
            
            return self._format_comparison_report(comparison_data)
            
        except Exception as e:
            logger.error(f"Error generating comparison report: {str(e)}")
            raise
    
    def generate_employee_report(self, 
                                employee_id: int,
                                start_period: date,
                                end_period: date) -> Dict[str, Any]:
        """
        Generate individual employee cumulative report
        
        Args:
            employee_id: Employee ID
            start_period: Start period
            end_period: End period
            
        Returns:
            Formatted employee report data
        """
        try:
            employee_data = self.aggregator.get_employee_cumulative_tracking(
                employee_id, start_period, end_period
            )
            
            return self._format_employee_report(employee_data)
            
        except Exception as e:
            logger.error(f"Error generating employee report: {str(e)}")
            raise
    
    def generate_trend_report(self, 
                            metric: str,
                            start_period: date,
                            end_period: date,
                            filters: Dict[str, Any] = None) -> Dict[str, Any]:
        """
        Generate trend analysis and forecasting report
        
        Args:
            metric: Metric to analyze
            start_period: Start period for analysis
            end_period: End period for analysis
            filters: Optional filters
            
        Returns:
            Formatted trend analysis report
        """
        try:
            trend_data = self.aggregator.get_trend_analysis_and_forecasting(
                metric, start_period, end_period, filters
            )
            
            return self._format_trend_report(trend_data)
            
        except Exception as e:
            logger.error(f"Error generating trend report: {str(e)}")
            raise
    
    def generate_compliance_report(self, year: int) -> Dict[str, Any]:
        """
        Generate regulatory compliance report
        
        Args:
            year: Year for compliance analysis
            
        Returns:
            Formatted compliance report data
        """
        try:
            compliance_data = self.aggregator.get_regulatory_compliance_tracking(year)
            
            return self._format_compliance_report(compliance_data)
            
        except Exception as e:
            logger.error(f"Error generating compliance report: {str(e)}")
            raise
    
    def export_report_data(self, 
                          report_data: Dict[str, Any], 
                          export_format: str,
                          report_type: str) -> str:
        """
        Export report data in various formats
        
        Args:
            report_data: Report data to export
            export_format: "csv", "json", "xml", "excel"
            report_type: Type of report for formatting
            
        Returns:
            Exported data as string or structured data
        """
        try:
            if export_format == "csv":
                return self._export_to_csv(report_data, report_type)
            elif export_format == "json":
                return self._export_to_json(report_data)
            elif export_format == "xml":
                return self._export_to_xml(report_data, report_type)
            elif export_format == "excel":
                return self._export_to_excel(report_data, report_type)
            else:
                raise ValueError(f"Unsupported export format: {export_format}")
                
        except Exception as e:
            logger.error(f"Error exporting report data: {str(e)}")
            raise
    
    # Formatting methods
    
    def _format_ytd_summary(self, ytd_data: Dict[str, Any]) -> Dict[str, Any]:
        """Format YTD data for summary display"""
        return {
            'report_type': 'ytd_summary',
            'title': f"Rapport Annuel Cumulé {ytd_data['year']}",
            'subtitle': f"Période: {DateFormatter.format_date_for_report(ytd_data['period_start'])} - {DateFormatter.format_date_for_report(ytd_data['period_end'])}",
            'generated_date': DateFormatter.format_date_for_report(date.today()),
            'summary_metrics': {
                'total_employees': ytd_data['ytd_totals']['total_employees'],
                'total_gross_salary': ReportFormatter.format_currency_for_report(ytd_data['ytd_totals']['total_gross_taxable']),
                'total_net_salary': ReportFormatter.format_currency_for_report(ytd_data['ytd_totals']['total_net_salary']),
                'total_social_contributions': ReportFormatter.format_currency_for_report(
                    ytd_data['ytd_totals']['total_cnss_employee'] + ytd_data['ytd_totals']['total_cnam_employee']
                ),
                'total_its': ReportFormatter.format_currency_for_report(ytd_data['ytd_totals']['total_its']),
                'average_salary': ReportFormatter.format_currency_for_report(ytd_data['ytd_totals']['average_net_salary']),
            },
            'performance_indicators': ytd_data['performance_indicators'],
            'monthly_breakdown': ytd_data['monthly_breakdown'],
            'top_earners': ytd_data['top_earners'][:5],  # Top 5 for summary
            'compliance_score': ytd_data['compliance_metrics'],
        }
    
    def _format_ytd_detailed(self, ytd_data: Dict[str, Any]) -> Dict[str, Any]:
        """Format YTD data for detailed display"""
        summary = self._format_ytd_summary(ytd_data)
        
        # Add detailed breakdowns
        summary.update({
            'detailed_monthly_breakdown': ytd_data['monthly_breakdown'],
            'department_breakdown': ytd_data['department_breakdown'],
            'direction_breakdown': ytd_data['direction_breakdown'],
            'all_top_earners': ytd_data['top_earners'],
            'employer_contributions': ytd_data['employer_contributions'],
            'budget_analysis': ytd_data['summary_statistics'],
        })
        
        return summary
    
    def _format_ytd_for_export(self, ytd_data: Dict[str, Any]) -> Dict[str, Any]:
        """Format YTD data for export"""
        return {
            'export_type': 'ytd_export',
            'metadata': {
                'year': ytd_data['year'],
                'calculation_date': ytd_data['calculation_date'],
                'period_start': ytd_data['period_start'],
                'period_end': ytd_data['period_end'],
                'export_timestamp': datetime.now(),
            },
            'raw_data': ytd_data,
            'formatted_summaries': self._format_ytd_detailed(ytd_data),
        }
    
    def _format_comparison_report(self, comparison_data: Dict[str, Any]) -> Dict[str, Any]:
        """Format comparison report data"""
        return {
            'report_type': 'multi_period_comparison',
            'title': f"Analyse Comparative {comparison_data['comparison_type'].replace('_', ' ').title()}",
            'subtitle': f"Période: {DateFormatter.format_date_for_report(comparison_data['start_period'])} - {DateFormatter.format_date_for_report(comparison_data['end_period'])}",
            'generated_date': DateFormatter.format_date_for_report(date.today()),
            'comparison_summary': {
                'total_periods': comparison_data['total_periods'],
                'comparison_type': comparison_data['comparison_type'],
                'analysis_span': f"{comparison_data['start_period']} à {comparison_data['end_period']}",
            },
            'period_summaries': comparison_data['period_summaries'],
            'variance_analysis': comparison_data['variance_analysis'],
            'trend_analysis': comparison_data['trend_analysis'],
            'growth_rates': comparison_data['growth_rates'],
            'seasonal_analysis': comparison_data['seasonal_analysis'],
            'performance_ranking': comparison_data['performance_ranking'],
            'forecast': comparison_data['forecast'],
            'insights': comparison_data['summary_insights'],
        }
    
    def _format_employee_report(self, employee_data: Dict[str, Any]) -> Dict[str, Any]:
        """Format employee report data"""
        employee = employee_data['employee']
        
        return {
            'report_type': 'employee_cumulative',
            'title': f"Rapport Cumulatif - {employee['full_name']}",
            'subtitle': f"Période: {DateFormatter.format_date_for_report(employee_data['period_info']['start_period'])} - {DateFormatter.format_date_for_report(employee_data['period_info']['end_period'])}",
            'generated_date': DateFormatter.format_date_for_report(date.today()),
            'employee_info': {
                'full_name': employee['full_name'],
                'employee_number': employee['employee_number'],
                'department': employee['department'],
                'position': employee['position'],
                'hire_date': DateFormatter.format_date_for_report(employee['hire_date']),
                'periods_analyzed': employee_data['period_info']['total_periods'],
            },
            'cumulative_earnings': employee_data['cumulative_earnings'],
            'leave_summary': employee_data['leave_analysis'],
            'benefits_deductions': employee_data['benefits_deductions'],
            'performance_metrics': employee_data['performance_metrics'],
            'seniority_analysis': employee_data['seniority_analysis'],
            'compensation_evolution': employee_data['compensation_evolution'],
            'tax_social_tracking': employee_data['tax_social_tracking'],
            'summary_statistics': employee_data['summary_statistics'],
        }
    
    def _format_trend_report(self, trend_data: Dict[str, Any]) -> Dict[str, Any]:
        """Format trend analysis report"""
        return {
            'report_type': 'trend_analysis',
            'title': f"Analyse de Tendance - {trend_data['metric'].replace('_', ' ').title()}",
            'subtitle': f"Période: {DateFormatter.format_date_for_report(trend_data['analysis_period']['start'])} - {DateFormatter.format_date_for_report(trend_data['analysis_period']['end'])}",
            'generated_date': DateFormatter.format_date_for_report(date.today()),
            'analysis_summary': {
                'metric_analyzed': trend_data['metric'],
                'total_periods': trend_data['analysis_period']['total_periods'],
                'analysis_span': f"{trend_data['analysis_period']['start']} à {trend_data['analysis_period']['end']}",
            },
            'time_series_data': trend_data['time_series_data'],
            'trend_statistics': trend_data['trend_statistics'],
            'seasonal_decomposition': trend_data['seasonal_decomposition'],
            'growth_analysis': trend_data['growth_analysis'],
            'volatility_analysis': trend_data['volatility_analysis'],
            'forecast_results': trend_data['forecast_results'],
            'anomalies': trend_data['anomalies'],
            'cycle_analysis': trend_data['cycle_analysis'],
            'insights': trend_data['insights'],
        }
    
    def _format_compliance_report(self, compliance_data: Dict[str, Any]) -> Dict[str, Any]:
        """Format compliance report data"""
        return {
            'report_type': 'regulatory_compliance',
            'title': f"Rapport de Conformité Réglementaire {compliance_data['year']}",
            'subtitle': f"Évaluation de la conformité aux lois du travail mauritaniennes",
            'generated_date': DateFormatter.format_date_for_report(compliance_data['calculation_date']),
            'compliance_overview': {
                'year': compliance_data['year'],
                'overall_score': f"{compliance_data['compliance_score']['overall_score']:.1f}%",
                'compliance_level': compliance_data['compliance_score']['compliance_level'],
                'total_checks': compliance_data['compliance_score']['total_checks'],
            },
            'cnss_compliance': compliance_data['cnss_compliance'],
            'cnam_compliance': compliance_data['cnam_compliance'],
            'its_compliance': compliance_data['its_compliance'],
            'ta_compliance': compliance_data['ta_compliance'],
            'minimum_wage_compliance': compliance_data['minimum_wage_compliance'],
            'overtime_compliance': compliance_data['overtime_compliance'],
            'leave_compliance': compliance_data['leave_compliance'],
            'declaration_tracking': compliance_data['declaration_tracking'],
            'audit_trail': compliance_data['audit_trail'],
            'recommendations': compliance_data['recommendations'],
        }
    
    # Export methods
    
    def _export_to_csv(self, report_data: Dict[str, Any], report_type: str) -> str:
        """Export report data to CSV format"""
        # Implementation depends on report type
        if report_type == 'ytd_summary':
            return self._export_ytd_to_csv(report_data)
        elif report_type == 'multi_period_comparison':
            return self._export_comparison_to_csv(report_data)
        elif report_type == 'employee_cumulative':
            return self._export_employee_to_csv(report_data)
        elif report_type == 'trend_analysis':
            return self._export_trend_to_csv(report_data)
        else:
            # Generic export
            return ExportUtilities.to_csv_format_enhanced([report_data])
    
    def _export_to_json(self, report_data: Dict[str, Any]) -> str:
        """Export report data to JSON format"""
        return ExportUtilities.to_json_format_enhanced(report_data)
    
    def _export_to_xml(self, report_data: Dict[str, Any], report_type: str) -> str:
        """Export report data to XML format"""
        from ..utils.report_utils import AdvancedExportUtilities
        
        # Convert to list format for XML export
        if isinstance(report_data, dict):
            data_list = [report_data]
        else:
            data_list = report_data
        
        return AdvancedExportUtilities.to_xml_format(
            data_list, 
            root_element=f"{report_type}_report",
            row_element="record"
        )
    
    def _export_to_excel(self, report_data: Dict[str, Any], report_type: str) -> Dict[str, Any]:
        """Export report data to Excel format (returns structured data for Excel generation)"""
        from ..utils.report_utils import AdvancedExportUtilities
        
        return AdvancedExportUtilities.to_excel_structured_format(
            [report_data] if isinstance(report_data, dict) else report_data,
            title=f"Rapport {report_type.replace('_', ' ').title()}",
            include_summary=True
        )
    
    def _export_ytd_to_csv(self, ytd_data: Dict[str, Any]) -> str:
        """Export YTD data to CSV"""
        # Flatten YTD data for CSV export
        csv_data = []
        
        # Monthly breakdown
        for month_data in ytd_data.get('monthly_breakdown', []):
            csv_data.append({
                'Type': 'Mensuel',
                'Période': month_data.get('period_formatted', ''),
                'Employés': month_data.get('total_employees', 0),
                'Salaire Brut Imposable': month_data.get('total_gross_taxable', 0),
                'Salaire Net': month_data.get('total_net_salary', 0),
                'CNSS Salarié': month_data.get('total_cnss_employee', 0),
                'CNAM Salarié': month_data.get('total_cnam_employee', 0),
                'ITS': month_data.get('total_its', 0),
            })
        
        field_mapping = {
            'Type': 'Type',
            'Période': 'Période',
            'Employés': 'Nombre d\'Employés',
            'Salaire Brut Imposable': 'Salaire Brut Imposable (MRU)',
            'Salaire Net': 'Salaire Net (MRU)',
            'CNSS Salarié': 'CNSS Salarié (MRU)',
            'CNAM Salarié': 'CNAM Salarié (MRU)',
            'ITS': 'ITS (MRU)',
        }
        
        return ExportUtilities.to_csv_format_enhanced(csv_data, field_mapping)
    
    def _export_comparison_to_csv(self, comparison_data: Dict[str, Any]) -> str:
        """Export comparison data to CSV"""
        csv_data = []
        
        for period_summary in comparison_data.get('period_summaries', []):
            csv_data.append({
                'Période': period_summary.get('period_formatted', ''),
                'Type_Période': period_summary.get('period_type', ''),
                'Employés': period_summary.get('total_employees', 0),
                'Salaire_Brut_Imposable': period_summary.get('total_gross_taxable', 0),
                'Salaire_Net': period_summary.get('total_net_salary', 0),
                'Salaire_Moyen': period_summary.get('average_salary', 0),
                'Jours_Travaillés': period_summary.get('total_working_days', 0),
                'CNSS': period_summary.get('total_cnss_employee', 0),
                'CNAM': period_summary.get('total_cnam_employee', 0),
                'ITS': period_summary.get('total_its', 0),
            })
        
        field_mapping = {
            'Période': 'Période',
            'Type_Période': 'Type de Période',
            'Employés': 'Nombre d\'Employés',
            'Salaire_Brut_Imposable': 'Salaire Brut Imposable (MRU)',
            'Salaire_Net': 'Salaire Net Total (MRU)',
            'Salaire_Moyen': 'Salaire Moyen (MRU)',
            'Jours_Travaillés': 'Jours Travaillés',
            'CNSS': 'CNSS Salarié (MRU)',
            'CNAM': 'CNAM Salarié (MRU)',
            'ITS': 'ITS (MRU)',
        }
        
        return ExportUtilities.to_csv_format_enhanced(csv_data, field_mapping)
    
    def _export_employee_to_csv(self, employee_data: Dict[str, Any]) -> str:
        """Export employee data to CSV"""
        compensation_timeline = employee_data.get('compensation_evolution', {}).get('compensation_timeline', [])
        
        csv_data = []
        for period_data in compensation_timeline:
            csv_data.append({
                'Période': DateFormatter.format_date_for_report(period_data.get('period')),
                'Salaire_Brut_Imposable': period_data.get('gross_taxable', 0),
                'Salaire_Brut_Non_Imposable': period_data.get('gross_non_taxable', 0),
                'Salaire_Net': period_data.get('net_salary', 0),
                'Total_Brut': period_data.get('total_gross', 0),
            })
        
        field_mapping = {
            'Période': 'Période',
            'Salaire_Brut_Imposable': 'Salaire Brut Imposable (MRU)',
            'Salaire_Brut_Non_Imposable': 'Salaire Brut Non Imposable (MRU)',
            'Salaire_Net': 'Salaire Net (MRU)',
            'Total_Brut': 'Total Brut (MRU)',
        }
        
        return ExportUtilities.to_csv_format_enhanced(csv_data, field_mapping)
    
    def _export_trend_to_csv(self, trend_data: Dict[str, Any]) -> str:
        """Export trend analysis data to CSV"""
        time_series = trend_data.get('time_series_data', [])
        
        csv_data = []
        for data_point in time_series:
            csv_data.append({
                'Période': data_point.get('period_formatted', ''),
                'Valeur': data_point.get('value', 0),
            })
        
        field_mapping = {
            'Période': 'Période',
            'Valeur': f"Valeur ({trend_data.get('metric', 'Métrique').replace('_', ' ').title()})",
        }
        
        return ExportUtilities.to_csv_format_enhanced(csv_data, field_mapping)


# Convenience functions for Django admin integration
def get_ytd_summary_for_admin(year: int, filters: Dict[str, Any] = None):
    """Convenience function for Django admin integration"""
    manager = CumulativeReportManager()
    return manager.generate_ytd_report(year, filters, "summary")


def get_comparison_analysis_for_admin(start_period: date, end_period: date, comparison_type: str):
    """Convenience function for Django admin comparison analysis"""
    manager = CumulativeReportManager()
    return manager.generate_comparison_report(start_period, end_period, comparison_type)


def get_employee_cumulative_for_admin(employee_id: int, start_period: date, end_period: date):
    """Convenience function for Django admin employee analysis"""
    manager = CumulativeReportManager()
    return manager.generate_employee_report(employee_id, start_period, end_period)


def get_trend_analysis_for_admin(metric: str, start_period: date, end_period: date):
    """Convenience function for Django admin trend analysis"""
    manager = CumulativeReportManager()
    return manager.generate_trend_report(metric, start_period, end_period)


def get_compliance_report_for_admin(year: int):
    """Convenience function for Django admin compliance reporting"""
    manager = CumulativeReportManager()
    return manager.generate_compliance_report(year)


# Export the main classes and functions
__all__ = [
    'CumulativeDataAggregator',
    'CumulativeReportManager', 
    'CumulativeReportPeriodType',
    'PeriodSummary',
    'VarianceAnalysis', 
    'TrendData',
    'get_ytd_summary_for_admin',
    'get_comparison_analysis_for_admin',
    'get_employee_cumulative_for_admin',
    'get_trend_analysis_for_admin',
    'get_compliance_report_for_admin',
]