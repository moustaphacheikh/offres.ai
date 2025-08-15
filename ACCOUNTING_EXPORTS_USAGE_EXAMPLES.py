#!/usr/bin/env python3
"""
Comprehensive Usage Examples for Accounting Exports Module

This file demonstrates how to use the accounting_exports module for enterprise
accounting system integration with various payroll export formats.

Features demonstrated:
1. Journal entry generation from payroll data
2. Chart of accounts management and validation
3. Multi-system export (Sage, Ciel, UNL, Generic)
4. Balance validation and reconciliation
5. Error handling and status tracking
6. Custom configuration and mapping
"""

import os
import sys
from datetime import date, datetime
from decimal import Decimal
from pathlib import Path

# Add the project root to Python path for imports
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

# Example imports (in actual Django app, these would be regular imports)
try:
    from core.reports.accounting_exports import (
        AccountingExportService,
        AccountingSystemType, 
        ExportConfiguration,
        ValidationResult,
        ChartOfAccountsManager,
        JournalEntryGenerator,
        AccountMapping,
        create_accounting_export_service,
        export_payroll_to_sage,
        export_payroll_to_ciel,
        export_payroll_to_unl
    )
except ImportError as e:
    print(f"Import error: {e}")
    print("This example file should be run in a Django environment with the models available")
    sys.exit(1)


def example_1_basic_sage_export():
    """
    Example 1: Basic Sage Accounting Export
    
    Demonstrates simple export to Sage accounting system format
    """
    print("=" * 60)
    print("EXAMPLE 1: Basic Sage Accounting Export")
    print("=" * 60)
    
    # Simple Sage export using convenience function
    result = export_payroll_to_sage(
        period="2024-01",
        motif_name="Salaire Janvier",
        user_name="admin",
        output_directory="/tmp/accounting_exports/sage"
    )
    
    if result.is_valid:
        print("✓ Sage export completed successfully")
        print(f"  Total Debit: {result.total_debit}")
        print(f"  Total Credit: {result.total_credit}")
        print(f"  Balance: {result.balance_difference}")
    else:
        print("✗ Sage export failed")
        for error in result.errors:
            print(f"  Error: {error}")
    
    if result.warnings:
        print("Warnings:")
        for warning in result.warnings:
            print(f"  Warning: {warning}")


def example_2_multi_system_export():
    """
    Example 2: Multi-System Export
    
    Demonstrates exporting to multiple accounting systems simultaneously
    """
    print("\n" + "=" * 60)
    print("EXAMPLE 2: Multi-System Export")
    print("=" * 60)
    
    # Create service instance
    service = create_accounting_export_service()
    
    # Configure export parameters
    export_params = {
        # UNL-specific parameters
        'unl_params': {
            'CODE_AGENCE': 'AG001',
            'CODE_DEVISE': 'UM', 
            'CODE_OPERATION': 'PAIE',
            'CODE_SERVICE': 'SRV001',
            'DATE_FORMAT': 'dd/MM/yyyy'
        },
        
        # Sage-specific configuration
        'sage_config': ExportConfiguration(
            system_type=AccountingSystemType.SAGE,
            file_extension=".csv",
            delimiter=",",
            header_required=True,
            encoding="utf-8"
        ),
        
        # Ciel-specific configuration
        'ciel_config': ExportConfiguration(
            system_type=AccountingSystemType.CIEL,
            file_extension=".txt",
            delimiter="\t",
            date_format="ddMMyyyy",
            encoding="utf-8"
        ),
        
        # Generic export format
        'generic_format': 'XML',
        'generic_config': ExportConfiguration(
            system_type=AccountingSystemType.GENERIC,
            file_extension=".xml",
            encoding="utf-8"
        )
    }
    
    # Export to all systems
    results = service.generate_and_export_payroll_accounting(
        period="2024-01",
        motif_name="Salaire Janvier",
        user_name="admin",
        export_systems=[
            AccountingSystemType.SAGE,
            AccountingSystemType.CIEL,
            AccountingSystemType.UNL,
            AccountingSystemType.GENERIC
        ],
        output_directory="/tmp/accounting_exports/multi",
        export_params=export_params
    )
    
    # Display results for each system
    for system_name, result in results.items():
        print(f"\n{system_name} Export:")
        if result.is_valid:
            print(f"  ✓ Success - Debit: {result.total_debit}, Credit: {result.total_credit}")
        else:
            print(f"  ✗ Failed")
            for error in result.errors:
                print(f"    - {error}")


def example_3_chart_of_accounts_management():
    """
    Example 3: Chart of Accounts Management
    
    Demonstrates custom chart of accounts configuration and validation
    """
    print("\n" + "=" * 60)
    print("EXAMPLE 3: Chart of Accounts Management")
    print("=" * 60)
    
    # Create chart manager
    chart_manager = ChartOfAccountsManager()
    
    # Add custom account mappings
    custom_mappings = {
        'BONUS_EXPENSE': AccountMapping(
            account_code='6415',
            account_name='Employee Bonuses',
            account_type='EXPENSE',
            debit_account=True,
            chapter_code='64',
            description='Annual and performance bonuses'
        ),
        
        'TRAINING_EXPENSE': AccountMapping(
            account_code='6416',
            account_name='Training Expenses',
            account_type='EXPENSE', 
            debit_account=True,
            chapter_code='64',
            description='Employee training and development costs'
        ),
        
        'BANK_CHARGES': AccountMapping(
            account_code='627',
            account_name='Bank Transfer Charges',
            account_type='EXPENSE',
            debit_account=True,
            chapter_code='62',
            description='Bank fees for salary transfers'
        )
    }
    
    # Add mappings to chart manager
    for key, mapping in custom_mappings.items():
        chart_manager.add_account_mapping(key, mapping)
        print(f"✓ Added account mapping: {key} -> {mapping.account_code}")
    
    # Validate account codes
    test_accounts = ['6415', '6416', '627', 'INVALID123', '']
    
    print("\nAccount Validation Results:")
    for account_code in test_accounts:
        validation = chart_manager.validate_account_code(account_code)
        status = "✓ Valid" if validation.is_valid else "✗ Invalid"
        print(f"  {account_code:<12} {status}")
        
        if validation.errors:
            for error in validation.errors:
                print(f"    Error: {error}")
        if validation.warnings:
            for warning in validation.warnings:
                print(f"    Warning: {warning}")
    
    # Demonstrate employee account generation
    print("\nEmployee Account Generation:")
    for employee_id in [1, 25, 100, 1234]:
        cash_account = chart_manager.get_employee_account('CASH', employee_id)
        engagement_account = chart_manager.get_employee_account('ENGAGEMENT', employee_id)
        print(f"  Employee {employee_id:4d}: Cash={cash_account}, Engagement={engagement_account}")


def example_4_journal_entry_generation():
    """
    Example 4: Manual Journal Entry Generation
    
    Demonstrates creating journal entries with custom configuration
    """
    print("\n" + "=" * 60)
    print("EXAMPLE 4: Manual Journal Entry Generation")
    print("=" * 60)
    
    # This example would normally work with actual Django models
    # For demonstration, we'll show the structure and expected usage
    
    print("Journal Entry Generation Process:")
    print("1. Create ChartOfAccountsManager")
    print("2. Initialize JournalEntryGenerator")
    print("3. Generate payroll entries for period")
    print("4. Validate balance and totals")
    print("5. Export to desired formats")
    
    chart_manager = ChartOfAccountsManager()
    journal_generator = JournalEntryGenerator(chart_manager)
    
    print(f"\n✓ Chart manager created with {len(chart_manager.account_mappings)} default mappings")
    print(f"✓ Journal generator initialized with tolerance: {journal_generator.validation_tolerance}")
    
    # Example of what the generation process would look like:
    print("\nExpected Journal Entry Types:")
    entry_types = [
        "Payroll Rubrique Entries (Gains)",
        "Bank Transfer Entries",
        "Cash Payment Entries", 
        "Employee Engagement Entries",
        "Payroll Deduction Entries (Retenues)",
        "Statutory Liability Entries (ITS, CNSS, CNAM)"
    ]
    
    for i, entry_type in enumerate(entry_types, 1):
        print(f"  {i}. {entry_type}")


def example_5_unl_export_configuration():
    """
    Example 5: UNL Export Configuration
    
    Demonstrates detailed UNL format export with custom parameters
    """
    print("\n" + "=" * 60)
    print("EXAMPLE 5: UNL Export Configuration")
    print("=" * 60)
    
    # Detailed UNL export parameters
    unl_params = {
        'CODE_AGENCE': 'MCCMR001',
        'CODE_DEVISE': 'UM',
        'CODE_OPERATION': 'PAIE_MENSUELLE',
        'CODE_SERVICE': 'SERVICE_PAIE',
        'DATE_FORMAT': 'dd/MM/yyyy'
    }
    
    print("UNL Export Parameters:")
    for key, value in unl_params.items():
        print(f"  {key:<15}: {value}")
    
    # Export using convenience function
    result = export_payroll_to_unl(
        period="2024-01",
        motif_name="Salaire Janvier",
        user_name="admin", 
        output_directory="/tmp/accounting_exports/unl",
        unl_params=unl_params
    )
    
    print(f"\nUNL Export Result: {'Success' if result.is_valid else 'Failed'}")
    
    if not result.is_valid:
        print("Errors:")
        for error in result.errors:
            print(f"  - {error}")
    
    # Demonstrate UNL field mapping
    print("\nUNL Field Mapping (58 fields):")
    field_mappings = {
        0: "CODE_AGENCE",
        1: "CODE_DEVISE", 
        2: "CHAPITRE",
        3: "COMPTE",
        5: "CODE_OPERATION",
        11: "DATE_COMPTABLE",
        12: "CODE_SERVICE",
        13: "DATE_VALEUR",
        14: "MONTANT",
        15: "SENS (D/C)",
        16: "LIBELLE",
        18: "NUM_PIECE"
    }
    
    for position, description in field_mappings.items():
        print(f"  Field {position:2d}: {description}")
    
    print(f"  Fields 19-57: Empty (padding)")


def example_6_error_handling_and_validation():
    """
    Example 6: Error Handling and Validation
    
    Demonstrates comprehensive error handling and validation scenarios
    """
    print("\n" + "=" * 60)
    print("EXAMPLE 6: Error Handling and Validation")
    print("=" * 60)
    
    service = create_accounting_export_service()
    
    # Test various error scenarios
    print("Testing Error Scenarios:")
    
    # 1. Invalid period format
    print("\n1. Invalid Period Format:")
    try:
        results = service.generate_and_export_payroll_accounting(
            period="invalid-period",
            motif_name="Test Motif",
            user_name="admin",
            export_systems=[AccountingSystemType.SAGE],
            output_directory="/tmp/test"
        )
        
        for system, result in results.items():
            if not result.is_valid:
                print(f"  {system}: Expected error - {result.errors[0] if result.errors else 'Unknown error'}")
    except Exception as e:
        print(f"  Caught exception: {e}")
    
    # 2. Empty motif name
    print("\n2. Empty Motif Name:")
    try:
        results = service.generate_and_export_payroll_accounting(
            period="2024-01",
            motif_name="",
            user_name="admin",
            export_systems=[AccountingSystemType.CIEL],
            output_directory="/tmp/test"
        )
        
        for system, result in results.items():
            if not result.is_valid:
                print(f"  {system}: Expected error - {result.errors[0] if result.errors else 'Unknown error'}")
    except Exception as e:
        print(f"  Caught exception: {e}")
    
    # 3. Invalid output directory
    print("\n3. Invalid Output Directory:")
    try:
        results = service.generate_and_export_payroll_accounting(
            period="2024-01",
            motif_name="Test Motif",
            user_name="admin",
            export_systems=[AccountingSystemType.UNL],
            output_directory="/root/restricted_directory"  # Likely to fail
        )
        
        for system, result in results.items():
            if not result.is_valid:
                print(f"  {system}: Expected error - {result.errors[0] if result.errors else 'Unknown error'}")
    except Exception as e:
        print(f"  Caught exception: {e}")
    
    # 4. Chart of accounts validation
    print("\n4. Chart of Accounts Validation:")
    chart_manager = ChartOfAccountsManager()
    
    test_mappings = ['SALARY_EXPENSE', 'INVALID_MAPPING', 'BONUS_EXPENSE']
    validation_result = service.validate_chart_of_accounts(test_mappings)
    
    print(f"  Validation result: {'✓ Valid' if validation_result.is_valid else '✗ Invalid'}")
    
    if validation_result.errors:
        print("  Errors:")
        for error in validation_result.errors:
            print(f"    - {error}")
    
    if validation_result.warnings:
        print("  Warnings:")
        for warning in validation_result.warnings:
            print(f"    - {warning}")


def example_7_export_summary_and_reconciliation():
    """
    Example 7: Export Summary and Reconciliation
    
    Demonstrates how to get export summaries and perform reconciliation
    """
    print("\n" + "=" * 60)
    print("EXAMPLE 7: Export Summary and Reconciliation")
    print("=" * 60)
    
    service = create_accounting_export_service()
    
    # This example demonstrates the structure of export summaries
    # In actual usage, you would have a real MasterPiece object
    
    print("Export Summary Components:")
    print("1. Master Piece Information:")
    print("   - Piece Number")
    print("   - Period and Motif")
    print("   - Operation Date")
    print("   - Total Debit/Credit")
    print("   - Balance Status")
    print("   - Export Status")
    
    print("\n2. Detail Summary:")
    print("   - Total number of entries")
    print("   - Debit vs Credit entry counts")
    print("   - Account types breakdown")
    print("   - Employee-specific entries count")
    
    print("\n3. Validation Results:")
    print("   - Balance validation")
    print("   - Account code validation")
    print("   - Error and warning summary")
    
    # Example validation result structure
    sample_validation = ValidationResult(is_valid=True)
    sample_validation.total_debit = Decimal('100000.00')
    sample_validation.total_credit = Decimal('100000.00')
    sample_validation.balance_difference = Decimal('0.00')
    sample_validation.add_warning("Some employee accounts use default patterns")
    
    print(f"\nSample Validation Result:")
    print(f"  Valid: {sample_validation.is_valid}")
    print(f"  Total Debit: {sample_validation.total_debit}")
    print(f"  Total Credit: {sample_validation.total_credit}")
    print(f"  Balance Difference: {sample_validation.balance_difference}")
    print(f"  Errors: {len(sample_validation.errors)}")
    print(f"  Warnings: {len(sample_validation.warnings)}")
    
    if sample_validation.warnings:
        for warning in sample_validation.warnings:
            print(f"    Warning: {warning}")


def example_8_custom_export_configuration():
    """
    Example 8: Custom Export Configuration
    
    Demonstrates advanced export configuration for different systems
    """
    print("\n" + "=" * 60)
    print("EXAMPLE 8: Custom Export Configuration")
    print("=" * 60)
    
    # Custom configurations for different accounting systems
    configurations = {
        'sage_custom': ExportConfiguration(
            system_type=AccountingSystemType.SAGE,
            file_extension=".csv",
            delimiter=";",  # Semicolon instead of comma
            date_format="yyyy-MM-dd",  # ISO date format
            currency_code="EUR",  # Euro instead of UM
            encoding="utf-8-sig",  # UTF-8 with BOM
            header_required=True,
            custom_fields={
                'company_code': 'MCCMR',
                'fiscal_year': '2024',
                'export_version': '2.1'
            }
        ),
        
        'ciel_custom': ExportConfiguration(
            system_type=AccountingSystemType.CIEL,
            file_extension=".imp",
            delimiter="|",
            date_format="ddMMyy",
            currency_code="UM",
            encoding="iso-8859-1",  # Latin-1 encoding
            custom_fields={
                'software_version': 'CIEL_2024',
                'import_format': 'STANDARD'
            }
        ),
        
        'unl_custom': ExportConfiguration(
            system_type=AccountingSystemType.UNL,
            file_extension=".dat",
            delimiter="|",
            date_format="ddMMyyyy",
            field_count=58,
            currency_code="UM",
            agency_code="CENTRAL",
            operation_code="PAYROLL",
            service_code="HR_SERVICE",
            custom_fields={
                'system_version': '3.0',
                'batch_id': datetime.now().strftime('%Y%m%d%H%M')
            }
        )
    }
    
    print("Custom Export Configurations:")
    for name, config in configurations.items():
        print(f"\n{name.upper()}:")
        print(f"  System Type: {config.system_type.value}")
        print(f"  File Extension: {config.file_extension}")
        print(f"  Delimiter: '{config.delimiter}'")
        print(f"  Date Format: {config.date_format}")
        print(f"  Currency: {config.currency_code}")
        print(f"  Encoding: {config.encoding}")
        
        if config.custom_fields:
            print(f"  Custom Fields:")
            for key, value in config.custom_fields.items():
                print(f"    {key}: {value}")
    
    # Demonstrate usage with custom configuration
    print(f"\nUsage Example with Custom Sage Configuration:")
    print(f"service = create_accounting_export_service()")
    print(f"results = service.generate_and_export_payroll_accounting(")
    print(f"    period='2024-01',")
    print(f"    motif_name='Salaire Janvier',")
    print(f"    user_name='admin',")
    print(f"    export_systems=[AccountingSystemType.SAGE],")
    print(f"    output_directory='/exports',")
    print(f"    export_params={{'sage_config': configurations['sage_custom']}}")
    print(f")")


def main():
    """
    Main function to run all examples
    """
    print("COMPREHENSIVE ACCOUNTING EXPORTS USAGE EXAMPLES")
    print("=" * 60)
    print("Demonstrating enterprise accounting system integration capabilities")
    print("for Django payroll system with multiple export formats.")
    print()
    
    try:
        # Run all examples
        example_1_basic_sage_export()
        example_2_multi_system_export()
        example_3_chart_of_accounts_management()
        example_4_journal_entry_generation()
        example_5_unl_export_configuration()
        example_6_error_handling_and_validation()
        example_7_export_summary_and_reconciliation()
        example_8_custom_export_configuration()
        
        print("\n" + "=" * 60)
        print("ALL EXAMPLES COMPLETED SUCCESSFULLY")
        print("=" * 60)
        print("\nNote: Some examples demonstrate structure and usage patterns")
        print("rather than actual exports due to database dependencies.")
        print("\nIn a real Django environment with populated models,")
        print("these examples would generate actual accounting export files.")
        
    except Exception as e:
        print(f"\nError running examples: {e}")
        print("This is expected when running outside a Django environment")
        print("with the required models and data.")


if __name__ == "__main__":
    main()