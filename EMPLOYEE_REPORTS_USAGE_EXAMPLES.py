#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Employee Reports Usage Examples
Comprehensive examples for using the employee reporting system

This file demonstrates:
1. Employee Directory Reports - Complete listings and organizational hierarchy
2. Employee Status Reports - Employment status and contract management
3. Employee Lifecycle Reports - Onboarding, terminations, anniversaries, career progression
4. Document and Compliance Reports - Document tracking and regulatory compliance
5. Export capabilities - Multiple formats (CSV, JSON, Excel, PDF)
6. Django Admin integration - HR operations management

Prerequisites:
- Django settings configured
- Employee models populated with data
- System parameters configured

Usage Examples:
    python manage.py shell
    exec(open('EMPLOYEE_REPORTS_USAGE_EXAMPLES.py').read())
"""

from datetime import date, datetime, timedelta
from decimal import Decimal

# Employee Reporting Imports
from core.reports.employee_reports import (
    EmployeeReportManager,
    EmployeeDirectoryReports,
    EmployeeStatusReports,
    EmployeeLifecycleReports,
    DocumentComplianceReports,
    EmployeeReportFilter,
    EmployeeStatusType,
    ReportType,
    ExportFormat,
    get_employee_directory_for_admin,
    get_employment_status_report_for_admin,
    get_contract_expiry_report_for_admin,
    get_document_compliance_report_for_admin,
    get_hr_dashboard_summary_for_admin,
)

def employee_directory_examples():
    """
    Example 1: Employee Directory Reports
    Complete employee listings with contact information and organizational hierarchy
    """
    print("=== Employee Directory Reports Examples ===\n")
    
    # Initialize the employee report manager
    employee_manager = EmployeeReportManager()
    
    # Example 1.1: Complete Employee Directory
    print("1.1 Complete Employee Directory")
    directory_report = employee_manager.generate_report(
        report_type=ReportType.DIRECTORY,
        include_photos=True,
        locale="fr"
    )
    
    print(f"Generated directory with {directory_report['total_employees']} employees")
    print(f"Statistics: {directory_report['statistics']}")
    
    # Example 1.2: Organizational Hierarchy Report
    print("\n1.2 Organizational Hierarchy Report")
    hierarchy_report = employee_manager.generate_report(
        report_type=ReportType.DIRECTORY,
        report_subtype='hierarchy',
        locale="fr"
    )
    
    print(f"Organizational structure with {hierarchy_report['total_employees']} employees")
    print(f"Summary stats: {hierarchy_report['summary_stats']}")
    
    # Example 1.3: Department-Specific Employee List
    print("\n1.3 Department-Specific Employee List")
    
    # Create filter for specific department
    dept_filter = EmployeeReportFilter(departments=[1])  # Department ID 1
    
    dept_report = employee_manager.generate_report(
        report_type=ReportType.DIRECTORY,
        report_subtype='department',
        filters=dept_filter,
        department_id=1
    )
    
    print(f"Department listings: {len(dept_report['department_listings'])} departments")
    
    return directory_report, hierarchy_report, dept_report

def quick_demo():
    """
    Quick demonstration of key employee reporting features
    """
    print("=== Quick Employee Reporting Demo ===\n")
    
    try:
        # Initialize manager
        employee_manager = EmployeeReportManager()
        
        # 1. Get HR dashboard summary
        print("1. HR Dashboard Summary")
        dashboard = employee_manager.get_dashboard_summary()
        print(f"Total employees: {dashboard['employee_counts']['total']}")
        print(f"Active: {dashboard['employee_counts']['active']}")
        
        # 2. Generate employee directory
        print("\n2. Employee Directory")
        directory = employee_manager.generate_report(ReportType.DIRECTORY)
        print(f"Directory entries: {directory['total_employees']}")
        
        # 3. Check contract status
        print("\n3. Contract Status Check")
        contracts = employee_manager.generate_report(
            ReportType.STATUS, 
            report_subtype='contracts',
            expiry_within_days=90
        )
        urgents = contracts['summary_statistics']['urgent_renewals_needed']
        print(f"Contracts needing urgent renewal: {urgents}")
        
        # 4. Compliance check
        print("\n4. Compliance Status")
        compliance = employee_manager.generate_report(
            ReportType.COMPLIANCE,
            report_subtype='registrations'
        )
        compliance_rate = compliance['compliance_statistics']['compliance_percentage']
        print(f"Registration compliance rate: {compliance_rate:.1f}%")
        
        print("\nQuick demo completed successfully!")
        
    except Exception as e:
        print(f"Demo error: {str(e)}")


if __name__ == "__main__":
    # Run quick demo by default
    quick_demo()