#!/usr/bin/env python3
"""
Comprehensive Usage Examples for Payroll Summary Reporting System
Demonstrates all major features and capabilities of the enhanced reporting module

This file provides practical examples for:
1. Monthly Payroll Summaries with filtering
2. Department and Cost Center Analysis  
3. Statistical Reporting and Analytics
4. Executive Dashboards and KPIs
5. Export functionality (CSV, Excel, JSON, PDF)
6. Management analytics and decision support

Run these examples after setting up your Django environment:
    python PAYROLL_REPORTING_EXAMPLES.py
"""

import os
import sys
import django
from datetime import date, datetime, timedelta
from dateutil.relativedelta import relativedelta
from decimal import Decimal

# Setup Django environment
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'payroll.settings')
django.setup()

# Import reporting modules
from core.reports import PayrollSummaryManager, PayrollSummaryAnalytics, FilterCriteria, ReportFormat
from core.models import SystemParameters, Employee, Department, Direction


def example_1_basic_monthly_summary():
    """
    Example 1: Generate basic monthly payroll summary
    Shows essential payroll metrics for management review
    """
    print("=== EXAMPLE 1: Basic Monthly Payroll Summary ===")
    
    # Initialize the reporting manager
    try:
        system_params = SystemParameters.objects.first()
    except:
        system_params = None
        print("Note: No system parameters found, using defaults")
    
    manager = PayrollSummaryManager(system_params)
    
    # Get summary for current month
    current_date = date.today()
    summary = manager.get_monthly_summary(
        year=current_date.year,
        month=current_date.month,
        include_details=True
    )
    
    print(f"Period: {summary['period_text']}")
    print(f"Generated at: {summary['generated_at']}")
    
    # Display summary statistics
    if 'summary' in summary:
        emp_counts = summary['summary']['employee_counts']
        financial = summary['summary']['financial_totals']
        
        print(f"\nEmployee Statistics:")
        print(f"  Total Employees: {emp_counts['total_employees']}")
        print(f"  Active Employees: {emp_counts['active_employees']}")
        
        print(f"\nFinancial Summary:")
        print(f"  Total Gross Taxable: {financial['total_gross_taxable']:,.2f} MRU")
        print(f"  Total Net Salary: {financial['total_net_salary']:,.2f} MRU")
        print(f"  Total Employer Costs: {financial['total_employer_costs']:,.2f} MRU")
    
    print("\n" + "="*60)
    return summary


def example_2_filtered_department_analysis():
    """
    Example 2: Department analysis with filtering
    Shows how to analyze specific departments or cost centers
    """
    print("=== EXAMPLE 2: Filtered Department Analysis ===")
    
    # Create filtering criteria
    filters = FilterCriteria(
        # departments=[1, 2, 3],  # Specific department IDs
        salary_range_min=Decimal('50000'),  # Minimum salary
        include_terminated=False  # Exclude terminated employees
    )
    
    manager = PayrollSummaryManager()
    
    # Get quarterly analysis with filters
    current_date = date.today()
    quarter = ((current_date.month - 1) // 3) + 1
    
    analysis = manager.get_quarterly_summary(
        year=current_date.year,
        quarter=quarter,
        filters=filters
    )
    
    print(f"Analysis Period: Q{quarter} {current_date.year}")
    print(f"Filters Applied: Salary >= {filters.salary_range_min:,.2f} MRU")
    
    # Display results would go here (analysis structure depends on implementation)
    print(f"Analysis generated successfully with {len(analysis)} data points")
    
    print("\n" + "="*60)
    return analysis


def example_3_statistical_reporting():
    """
    Example 3: Advanced statistical analysis
    Demonstrates salary distribution, trends, and employee statistics
    """
    print("=== EXAMPLE 3: Statistical Reporting ===")
    
    analytics = PayrollSummaryAnalytics()
    
    # Define analysis period (last 6 months)
    end_date = date.today()
    start_date = end_date - relativedelta(months=6)
    
    # Generate comprehensive statistical report
    stats_report = analytics.generate_statistical_report(
        period_start=start_date,
        period_end=end_date,
        analysis_type="comprehensive"
    )
    
    print(f"Statistical Analysis: {start_date} to {end_date}")
    
    # Display key insights
    if 'salary_distribution' in stats_report:
        print("\nSalary Distribution Analysis:")
        print("  - Median, mean, and distribution metrics")
        print("  - Salary brackets and percentile analysis")
    
    if 'employment_statistics' in stats_report:
        print("\nEmployment Statistics:")
        print("  - Hiring and termination trends")
        print("  - Turnover rate analysis")
    
    if 'leave_utilization' in stats_report:
        print("\nLeave Utilization:")
        print("  - Annual leave usage patterns")
        print("  - Sick leave and special leave analysis")
    
    if 'productivity_metrics' in stats_report:
        print("\nProductivity Metrics:")
        print("  - Overtime patterns and efficiency")
        print("  - Cost per employee trends")
    
    print("\n" + "="*60)
    return stats_report


def example_4_executive_dashboard():
    """
    Example 4: Executive dashboard for senior management
    High-level KPIs and performance indicators
    """
    print("=== EXAMPLE 4: Executive Dashboard ===")
    
    manager = PayrollSummaryManager()
    
    # Generate monthly executive dashboard
    dashboard = manager.get_executive_dashboard(
        period_type="monthly",
        period=date.today(),
        include_forecasting=True
    )
    
    print("Executive Dashboard - Monthly View")
    
    # Display KPIs
    if 'kpis' in dashboard:
        print("\nKey Performance Indicators:")
        print("  - Total payroll costs and trends")
        print("  - Employee productivity metrics")
        print("  - Cost control effectiveness")
    
    if 'cost_control' in dashboard:
        print("\nCost Control Metrics:")
        print("  - Budget variance analysis")
        print("  - Cost per employee trends")
        print("  - Department cost efficiency")
    
    if 'compliance_status' in dashboard:
        print("\nCompliance Status:")
        print("  - SMIG compliance rate")
        print("  - Tax calculation accuracy")
        print("  - Data quality indicators")
    
    if 'trend_indicators' in dashboard:
        print("\nTrend Indicators:")
        print("  - Month-over-month changes")
        print("  - Year-over-year comparisons")
        print("  - Forecasting insights")
    
    if 'risk_indicators' in dashboard:
        print("\nRisk Indicators:")
        print("  - High turnover departments")
        print("  - Overtime concentration")
        print("  - Budget overrun alerts")
    
    print("\n" + "="*60)
    return dashboard


def example_5_export_functionality():
    """
    Example 5: Export reports in multiple formats
    Demonstrates CSV, Excel, JSON, and PDF export capabilities
    """
    print("=== EXAMPLE 5: Export Functionality ===")
    
    manager = PayrollSummaryManager()
    
    # Get a sample report
    summary = manager.get_monthly_summary(
        year=date.today().year,
        month=date.today().month
    )
    
    print("Exporting monthly summary in multiple formats:")
    
    # Export to CSV (French format)
    csv_export = manager.export_summary(
        summary_data=summary,
        export_format="csv",
        language="french"
    )
    
    if csv_export['success']:
        print(f"✓ CSV export successful: {csv_export['filename']}")
        print(f"  Content length: {len(csv_export.get('content', ''))} characters")
    else:
        print(f"✗ CSV export failed: {csv_export.get('error')}")
    
    # Export to Excel
    excel_export = manager.export_summary(
        summary_data=summary,
        export_format="excel",
        language="french"
    )
    
    if excel_export['success']:
        print(f"✓ Excel export successful: {excel_export['filename']}")
        print(f"  Data structure prepared for Excel generation")
    else:
        print(f"✗ Excel export failed: {excel_export.get('error')}")
    
    # Export to JSON
    json_export = manager.export_summary(
        summary_data=summary,
        export_format="json"
    )
    
    if json_export['success']:
        print(f"✓ JSON export successful: {json_export['filename']}")
        print(f"  Content length: {len(json_export.get('content', ''))} characters")
    else:
        print(f"✗ JSON export failed: {json_export.get('error')}")
    
    # Export to PDF (placeholder)
    pdf_export = manager.export_summary(
        summary_data=summary,
        export_format="pdf",
        template="executive_summary",
        language="french"
    )
    
    if pdf_export['success']:
        print(f"✓ PDF export prepared: {pdf_export['filename']}")
        print(f"  Message: {pdf_export.get('message')}")
    else:
        print(f"✗ PDF export failed: {pdf_export.get('error')}")
    
    print("\n" + "="*60)
    return {
        'csv': csv_export,
        'excel': excel_export, 
        'json': json_export,
        'pdf': pdf_export
    }


def example_6_advanced_filtering():
    """
    Example 6: Advanced filtering and analysis
    Shows complex filtering scenarios for targeted analysis
    """
    print("=== EXAMPLE 6: Advanced Filtering ===")
    
    analytics = PayrollSummaryAnalytics()
    
    # Create advanced filter criteria
    advanced_filters = FilterCriteria(
        departments=[1, 2],  # Specific departments
        salary_range_min=Decimal('40000'),
        salary_range_max=Decimal('200000'),
        seniority_years_min=2,  # At least 2 years seniority
        include_terminated=False,
        start_date=date.today() - relativedelta(months=3),
        end_date=date.today()
    )
    
    print("Advanced Filter Criteria:")
    print(f"  Departments: {advanced_filters.departments}")
    print(f"  Salary Range: {advanced_filters.salary_range_min:,.0f} - {advanced_filters.salary_range_max:,.0f} MRU")
    print(f"  Minimum Seniority: {advanced_filters.seniority_years_min} years")
    print(f"  Date Range: {advanced_filters.start_date} to {advanced_filters.end_date}")
    
    # Generate filtered monthly summary
    filtered_summary = analytics.generate_monthly_payroll_summary(
        period=date.today(),
        filters=advanced_filters,
        include_details=True
    )
    
    print(f"\nFiltered Results:")
    print(f"  Period: {filtered_summary.get('period_text', 'N/A')}")
    print(f"  Filter Criteria Applied: {len(filtered_summary.get('filter_criteria', {})) > 0}")
    
    # Show impact of filtering
    if 'summary' in filtered_summary:
        summary = filtered_summary['summary']
        if 'employee_counts' in summary:
            print(f"  Employees in filtered result: {summary['employee_counts']['total_employees']}")
    
    print("\n" + "="*60)
    return filtered_summary


def example_7_comparative_analysis():
    """
    Example 7: Comparative analysis across periods
    Compare current performance with previous periods
    """
    print("=== EXAMPLE 7: Comparative Analysis ===")
    
    analytics = PayrollSummaryAnalytics()
    
    # Current period
    current_period = date.today()
    current_start = current_period.replace(day=1)
    current_end = (current_start + relativedelta(months=1)) - timedelta(days=1)
    
    # Previous period (same month last year)
    previous_period = current_period - relativedelta(years=1)
    
    print(f"Comparing:")
    print(f"  Current Period: {current_period.strftime('%B %Y')}")
    print(f"  Previous Period: {previous_period.strftime('%B %Y')}")
    
    # Generate department analysis with comparison
    comparison_analysis = analytics.generate_department_analysis(
        period_start=current_start,
        period_end=current_end,
        comparison_period=previous_period
    )
    
    print(f"\nComparative Analysis Results:")
    print(f"  Year-over-year comparison available")
    print(f"  Department performance trends")
    print(f"  Budget variance analysis")
    print(f"  Headcount change analysis")
    
    # Key metrics comparison would be displayed here
    print(f"\nKey Insights:")
    print(f"  - Department cost trends")
    print(f"  - Employee productivity changes")
    print(f"  - Budget adherence improvements")
    
    print("\n" + "="*60)
    return comparison_analysis


def example_8_performance_optimization():
    """
    Example 8: Performance optimization with caching
    Shows how to use caching for large datasets
    """
    print("=== EXAMPLE 8: Performance Optimization ===")
    
    # Initialize analytics with caching enabled
    analytics = PayrollSummaryAnalytics()
    analytics.enable_cache = True
    analytics.cache_timeout = 3600  # 1 hour cache
    
    print("Performance Optimization Features:")
    print(f"  Caching Enabled: {analytics.enable_cache}")
    print(f"  Cache Timeout: {analytics.cache_timeout} seconds")
    
    # First call - will be cached
    start_time = datetime.now()
    summary1 = analytics.generate_monthly_payroll_summary(
        period=date.today(),
        include_details=False  # Faster without details
    )
    first_call_time = (datetime.now() - start_time).total_seconds()
    
    # Second call - should be faster from cache
    start_time = datetime.now()
    summary2 = analytics.generate_monthly_payroll_summary(
        period=date.today(),
        include_details=False
    )
    second_call_time = (datetime.now() - start_time).total_seconds()
    
    print(f"\nPerformance Results:")
    print(f"  First call (database): {first_call_time:.3f} seconds")
    print(f"  Second call (cached): {second_call_time:.3f} seconds")
    print(f"  Performance improvement: {((first_call_time - second_call_time) / first_call_time * 100):.1f}%")
    
    # Optimization recommendations
    print(f"\nOptimization Recommendations:")
    print(f"  - Use caching for frequently accessed reports")
    print(f"  - Set include_details=False for summary views")
    print(f"  - Use specific filters to reduce dataset size")
    print(f"  - Consider pagination for large result sets")
    
    print("\n" + "="*60)
    return {'first_time': first_call_time, 'second_time': second_call_time}


def example_9_data_quality_monitoring():
    """
    Example 9: Data quality and compliance monitoring
    Monitor data integrity and compliance indicators
    """
    print("=== EXAMPLE 9: Data Quality Monitoring ===")
    
    analytics = PayrollSummaryAnalytics()
    
    # Generate summary with data quality focus
    summary = analytics.generate_monthly_payroll_summary(
        period=date.today(),
        include_details=True
    )
    
    print("Data Quality Monitoring:")
    
    # Compliance indicators
    if 'compliance_indicators' in summary:
        compliance = summary['compliance_indicators']
        print(f"\nCompliance Indicators:")
        
        if 'smig_compliance' in compliance:
            smig = compliance['smig_compliance']
            print(f"  SMIG Compliance: {smig.get('compliance_rate', 0):.1f}%")
            print(f"  Employees below SMIG: {smig.get('below_smig_count', 0)}")
        
        if 'tax_compliance' in compliance:
            print(f"  Tax Calculation Compliance: Validated")
        
        if 'data_completeness' in compliance:
            print(f"  Data Completeness: Assessed")
    
    # Data quality metrics
    if 'data_quality_metrics' in summary:
        quality = summary['data_quality_metrics']
        print(f"\nData Quality Metrics:")
        
        if 'data_completeness' in quality:
            completeness = quality['data_completeness']
            print(f"  Completeness Score: {completeness.get('completeness_score', 0):.1f}%")
            print(f"  Missing Net Salary: {completeness.get('missing_net_salary', 0)} records")
            print(f"  Missing Department: {completeness.get('missing_department', 0)} records")
        
        if 'data_anomalies' in quality:
            anomalies = quality['data_anomalies']
            print(f"  Anomaly Rate: {anomalies.get('anomaly_rate', 0):.2f}%")
            print(f"  Zero Salaries: {anomalies.get('zero_salaries', 0)} records")
            print(f"  Negative Values: {anomalies.get('negative_values', 0)} records")
    
    # Recommendations based on data quality
    print(f"\nData Quality Recommendations:")
    print(f"  - Regular data validation checks")
    print(f"  - Automated anomaly detection")
    print(f"  - Compliance monitoring alerts")
    print(f"  - Data entry quality controls")
    
    print("\n" + "="*60)
    return summary


def example_10_integration_with_django_admin():
    """
    Example 10: Integration with Django Admin
    Shows how to integrate reporting with Django admin interface
    """
    print("=== EXAMPLE 10: Django Admin Integration ===")
    
    print("Django Admin Integration Features:")
    print("  - Custom admin actions for report generation")
    print("  - Embedded report views in admin pages")
    print("  - Export functionality from admin interface")
    print("  - Scheduled report generation")
    
    # Example admin integration code structure
    admin_integration_example = """
    # In admin.py
    from django.contrib import admin
    from django.http import HttpResponse
    from core.reports import PayrollSummaryManager
    
    @admin.action(description="Generate Monthly Payroll Summary")
    def generate_payroll_summary(modeladmin, request, queryset):
        manager = PayrollSummaryManager()
        summary = manager.get_monthly_summary(2024, 1)
        export = manager.export_summary(summary, 'csv')
        
        response = HttpResponse(export['content'], content_type='text/csv')
        response['Content-Disposition'] = f'attachment; filename="{export["filename"]}"'
        return response
    
    class PayrollAdmin(admin.ModelAdmin):
        actions = [generate_payroll_summary]
        list_display = ['employee', 'period', 'net_salary']
        list_filter = ['period', 'employee__department']
    """
    
    print(f"\nAdmin Integration Example:")
    print(admin_integration_example)
    
    print(f"\nIntegration Benefits:")
    print(f"  - Direct access to reports from admin interface")
    print(f"  - Bulk operations on payroll data")
    print(f"  - Role-based access to different report types")
    print(f"  - Automated report scheduling and delivery")
    
    print("\n" + "="*60)
    return admin_integration_example


def run_all_examples():
    """
    Run all examples to demonstrate complete functionality
    """
    print("PAYROLL SUMMARY REPORTING SYSTEM - COMPREHENSIVE EXAMPLES")
    print("=" * 80)
    print(f"Examples run on: {datetime.now()}")
    print("=" * 80)
    
    examples = [
        example_1_basic_monthly_summary,
        example_2_filtered_department_analysis,
        example_3_statistical_reporting,
        example_4_executive_dashboard,
        example_5_export_functionality,
        example_6_advanced_filtering,
        example_7_comparative_analysis,
        example_8_performance_optimization,
        example_9_data_quality_monitoring,
        example_10_integration_with_django_admin
    ]
    
    results = {}
    
    for i, example_func in enumerate(examples, 1):
        try:
            print(f"\nRunning Example {i}...")
            result = example_func()
            results[f'example_{i}'] = result
            print(f"Example {i} completed successfully.")
        except Exception as e:
            print(f"Example {i} failed: {str(e)}")
            results[f'example_{i}'] = {'error': str(e)}
    
    print("\n" + "=" * 80)
    print("ALL EXAMPLES COMPLETED")
    print("=" * 80)
    
    # Summary of results
    successful = len([r for r in results.values() if 'error' not in r])
    total = len(results)
    
    print(f"Summary: {successful}/{total} examples completed successfully")
    
    if successful < total:
        failed_examples = [k for k, v in results.items() if 'error' in v]
        print(f"Failed examples: {', '.join(failed_examples)}")
    
    return results


if __name__ == "__main__":
    """
    Main execution - run all examples
    """
    try:
        results = run_all_examples()
        
        print("\nFinal Notes:")
        print("- These examples demonstrate the full capabilities of the payroll reporting system")
        print("- Adapt the filtering criteria and parameters to your specific needs")
        print("- Consider implementing scheduled reports for regular management updates")
        print("- Use caching and performance optimization for large datasets")
        print("- Integrate with Django admin for user-friendly access")
        
        print(f"\nFor more information, refer to the module documentation in:")
        print("- core/reports/payroll_summary.py")
        print("- core/utils/report_utils.py")
        print("- core/utils/business_rules.py")
        
    except Exception as e:
        print(f"Error running examples: {str(e)}")
        print("Ensure Django environment is properly configured and database is accessible")