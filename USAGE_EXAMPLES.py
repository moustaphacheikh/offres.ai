#!/usr/bin/env python3
"""
Comprehensive usage examples for the Django Payroll Import/Export System.

This file demonstrates all the capabilities of the import/export system
including basic operations, advanced features, error handling, and
performance optimization.

Run this file to see examples in action (after setting up Django environment):
    python manage.py shell < USAGE_EXAMPLES.py
"""

import os
import tempfile
import time
from datetime import datetime, date

# Import the main import/export system
from core.utils.import_export import (
    ImportExportManager,
    EmployeeImportExport,
    PayrollElementImportExport,
    AttendanceImportExport,
    DataValidator,
    CSVProcessor,
    ExcelProcessor,
    ProgressTracker,
    ImportResult,
    ExportResult,
    create_employee_template,
    import_employees_from_excel,
    export_employees_to_excel
)

# Import enhanced features
from core.utils.import_export_enhancements import (
    MemoryOptimizer,
    EnhancedProgressTracker,
    RollbackManager,
    BatchValidationEngine,
    ChunkedFileProcessor,
    get_system_performance_report
)


def example_1_basic_employee_import():
    """Example 1: Basic employee data import from CSV."""
    print("=" * 60)
    print("Example 1: Basic Employee Import from CSV")
    print("=" * 60)
    
    # Create sample employee data
    sample_data = [
        {
            'employee_number': 'EMP001',
            'last_name': 'Doe',
            'first_name': 'John',
            'father_name': 'Robert Doe',
            'mother_name': 'Jane Doe',
            'national_id': '1234567890',
            'birth_date': '1990-01-15',
            'hire_date': '2023-01-01',
            'email': 'john.doe@company.com',
            'phone': '+1-555-0123',
            'is_active': 'TRUE'
        },
        {
            'employee_number': 'EMP002',
            'last_name': 'Smith',
            'first_name': 'Jane',
            'father_name': 'William Smith',
            'mother_name': 'Mary Smith',
            'national_id': '9876543210',
            'birth_date': '1985-05-20',
            'hire_date': '2023-02-01',
            'email': 'jane.smith@company.com',
            'phone': '+1-555-0124',
            'is_active': 'TRUE'
        }
    ]
    
    # Create temporary CSV file
    with tempfile.NamedTemporaryFile(mode='w', suffix='.csv', delete=False) as tmp_file:
        csv_path = tmp_file.name
    
    try:
        # Export sample data to CSV
        success = CSVProcessor.export_to_csv(sample_data, csv_path)
        print(f"Created test CSV file: {success}")
        
        # Progress callback
        def progress_callback(current, total, message):
            percentage = (current / total) * 100
            print(f"  Progress: {percentage:.1f}% ({current}/{total}) - {message}")
        
        # Import employees
        result = EmployeeImportExport.import_employees(csv_path, progress_callback)
        
        # Display results
        print(f"\nImport Results:")
        print(f"  Success: {result.success}")
        print(f"  Total Records: {result.total_records}")
        print(f"  Processed: {result.processed_records}")
        print(f"  Errors: {result.error_records}")
        print(f"  Created: {len(result.created_objects)}")
        print(f"  Updated: {len(result.updated_objects)}")
        print(f"  Execution Time: {result.execution_time:.2f} seconds")
        
        if result.errors:
            print(f"\nErrors encountered:")
            for error in result.errors:
                print(f"  Row {error['row']}: {error['errors']}")
    
    finally:
        # Clean up
        if os.path.exists(csv_path):
            os.unlink(csv_path)
    
    print("\n")


def example_2_excel_template_creation():
    """Example 2: Create Excel templates for different data types."""
    print("=" * 60)
    print("Example 2: Excel Template Creation")
    print("=" * 60)
    
    if not hasattr(ExcelProcessor, '__init__'):
        print("Excel processing not available (openpyxl not installed)")
        return
    
    try:
        processor = ExcelProcessor()
        
        # Create templates for different data types
        templates = ['employee', 'payroll_element', 'attendance']
        
        for template_type in templates:
            template_path = f"/tmp/{template_type}_template.xlsx"
            
            success = processor.create_excel_template(template_type, template_path)
            
            if success:
                file_size = os.path.getsize(template_path) if os.path.exists(template_path) else 0
                print(f"  ✓ Created {template_type} template: {template_path} ({file_size} bytes)")
            else:
                print(f"  ✗ Failed to create {template_type} template")
        
        print(f"\nTemplates created successfully!")
        print(f"Use these templates to format your import data correctly.")
    
    except Exception as e:
        print(f"Template creation failed: {str(e)}")
    
    print("\n")


def example_3_data_validation():
    """Example 3: Comprehensive data validation."""
    print("=" * 60)
    print("Example 3: Data Validation")
    print("=" * 60)
    
    # Sample data with intentional errors
    test_data = [
        # Valid record
        {
            'last_name': 'Valid',
            'first_name': 'Employee',
            'email': 'valid@company.com',
            'phone': '+1-555-0100',
            'birth_date': '1990-01-01',
            'hire_date': '2023-01-01'
        },
        # Missing required field
        {
            'first_name': 'Missing',
            'email': 'missing@company.com'
            # Missing last_name (required)
        },
        # Invalid email
        {
            'last_name': 'Invalid',
            'first_name': 'Email',
            'email': 'invalid-email-format'
        },
        # Invalid date
        {
            'last_name': 'Invalid',
            'first_name': 'Date',
            'email': 'date@company.com',
            'birth_date': 'not-a-date'
        }
    ]
    
    print(f"Validating {len(test_data)} records...")
    
    # Individual validation
    for idx, record in enumerate(test_data):
        is_valid, errors = DataValidator.validate_employee_data(record)
        print(f"\nRecord {idx + 1}:")
        print(f"  Valid: {is_valid}")
        if errors:
            for error in errors:
                print(f"  Error: {error}")
    
    # Batch validation using enhanced validator
    print(f"\n--- Batch Validation ---")
    validation_result = BatchValidationEngine.validate_batch_comprehensive(
        test_data, 'employee'
    )
    
    print(f"Batch Validation Summary:")
    print(f"  Total Records: {validation_result['total_records']}")
    print(f"  Valid Records: {validation_result['valid_records']}")
    print(f"  Error Records: {validation_result['error_records']}")
    print(f"  Warning Records: {validation_result['warning_records']}")
    
    if validation_result['duplicate_errors']:
        print(f"  Duplicate Issues: {len(validation_result['duplicate_errors'])}")
    
    print("\n")


def example_4_memory_monitoring():
    """Example 4: Memory monitoring and optimization."""
    print("=" * 60)
    print("Example 4: Memory Monitoring and Optimization")
    print("=" * 60)
    
    # Get system performance report
    performance = get_system_performance_report()
    
    print(f"System Performance Report:")
    print(f"  Timestamp: {performance['timestamp']}")
    print(f"  Memory Usage: {performance['memory_usage']}")
    print(f"  Psutil Available: {performance['psutil_available']}")
    
    if performance['recommendations']:
        print(f"  Recommendations:")
        for rec in performance['recommendations']:
            print(f"    - {rec}")
    
    # Test optimal chunk size calculation
    test_file_sizes = [1, 10, 50, 100, 500]  # MB
    
    print(f"\nOptimal Chunk Size Recommendations:")
    print(f"{'File Size (MB)':<15} {'Chunk Size':<15} {'Memory %':<15}")
    print("-" * 45)
    
    for file_size in test_file_sizes:
        chunk_size = MemoryOptimizer.get_optimal_chunk_size(file_size)
        memory_percent = 10.0  # Default percentage
        print(f"{file_size:<15} {chunk_size:<15} {memory_percent:<15}")
    
    # Memory monitoring during simulated processing
    print(f"\nMemory monitoring during simulated processing:")
    
    for i in range(5):
        memory_info = MemoryOptimizer.monitor_memory()
        should_pause = MemoryOptimizer.should_pause_processing()
        
        print(f"  Sample {i+1}: Memory: {memory_info.get('rss_mb', 0):.1f}MB, "
              f"System: {memory_info.get('system_percent', 0):.1f}%, "
              f"Should Pause: {should_pause}")
        
        time.sleep(0.1)  # Brief pause
    
    print("\n")


def example_5_enhanced_progress_tracking():
    """Example 5: Enhanced progress tracking with performance metrics."""
    print("=" * 60)
    print("Example 5: Enhanced Progress Tracking")
    print("=" * 60)
    
    # Create enhanced progress tracker
    total_items = 1000
    
    def enhanced_callback(current, total, message):
        percentage = (current / total) * 100
        print(f"  Enhanced Progress: {percentage:6.1f}% - {message}")
    
    tracker = EnhancedProgressTracker(total_items, enhanced_callback)
    
    print(f"Starting processing of {total_items} items...")
    
    # Simulate processing with various scenarios
    for i in range(total_items):
        # Simulate work
        if i % 100 == 0:
            time.sleep(0.01)  # Simulate slower processing occasionally
        
        # Simulate errors and warnings
        is_error = i % 150 == 0  # Every 150th item is an error
        is_warning = i % 100 == 0  # Every 100th item is a warning
        
        message = f"Processing item {i+1}"
        if is_error:
            message += " [ERROR]"
        elif is_warning:
            message += " [WARNING]"
        
        tracker.update(1, message, error=is_error, warning=is_warning)
    
    tracker.complete("Enhanced processing completed")
    
    # Get comprehensive performance summary
    performance_summary = tracker.get_performance_summary()
    
    print(f"\nPerformance Summary:")
    print(f"  Total Processed: {performance_summary['total_processed']}")
    print(f"  Execution Time: {performance_summary['elapsed_seconds']:.2f} seconds")
    print(f"  Processing Rate: {performance_summary['processing_rate']:.1f} items/sec")
    print(f"  Error Count: {performance_summary['error_count']}")
    print(f"  Warning Count: {performance_summary['warning_count']}")
    
    if performance_summary.get('memory_tracking'):
        memory_info = performance_summary['memory_tracking']
        print(f"  Memory Tracking:")
        print(f"    Peak Memory: {memory_info.get('peak_memory_mb', 0):.1f}MB")
        print(f"    Avg Memory: {memory_info.get('avg_memory_mb', 0):.1f}MB")
        print(f"    Samples: {memory_info.get('memory_samples', 0)}")
    
    if performance_summary.get('rate_tracking'):
        rate_info = performance_summary['rate_tracking']
        print(f"  Rate Tracking:")
        print(f"    Peak Rate: {rate_info.get('peak_rate', 0):.1f} items/sec")
        print(f"    Avg Rate: {rate_info.get('avg_rate', 0):.1f} items/sec")
    
    print("\n")


def example_6_rollback_management():
    """Example 6: Rollback management for failed operations."""
    print("=" * 60)
    print("Example 6: Rollback Management")
    print("=" * 60)
    
    rollback_manager = RollbackManager()
    
    print("Demonstrating rollback management...")
    
    try:
        with rollback_manager.transaction_savepoint("test_operation"):
            print("  Creating savepoint: test_operation")
            
            # Simulate tracking operations
            rollback_manager.add_rollback_data(
                'create', 'Employee', 'EMP001', 
                {'name': 'Test Employee', 'status': 'active'}
            )
            
            rollback_manager.add_rollback_data(
                'update', 'Employee', 'EMP002',
                {'original_status': 'inactive', 'new_status': 'active'}
            )
            
            print("  Tracked 2 operations for potential rollback")
            
            # Simulate an error condition
            if True:  # Change to False to test success path
                raise Exception("Simulated error to trigger rollback")
            
            print("  Operations completed successfully")
    
    except Exception as e:
        print(f"  Exception caught: {str(e)}")
        print("  Rollback was performed automatically")
    
    # Get rollback summary
    rollback_summary = rollback_manager.get_rollback_summary()
    
    print(f"\nRollback Summary:")
    print(f"  Total Rollback Items: {rollback_summary['total_rollback_items']}")
    print(f"  Created Objects Tracked: {rollback_summary['created_objects']}")
    print(f"  Modified Objects Tracked: {rollback_summary['modified_objects']}")
    print(f"  Savepoints Used: {rollback_summary['savepoints_used']}")
    
    print("\n")


def example_7_export_operations():
    """Example 7: Advanced export operations."""
    print("=" * 60)
    print("Example 7: Export Operations")
    print("=" * 60)
    
    # Note: This example assumes there are some employees in the database
    # In a real scenario, you'd first import or create some test data
    
    print("Attempting to export employee data...")
    
    try:
        # Export all employees
        with tempfile.NamedTemporaryFile(suffix='.csv', delete=False) as tmp_file:
            export_path = tmp_file.name
        
        result = EmployeeImportExport.export_employees(export_path, 'csv')
        
        print(f"Export Results:")
        print(f"  Success: {result.success}")
        print(f"  Total Records: {result.total_records}")
        print(f"  Exported Records: {result.exported_records}")
        print(f"  File Path: {result.file_path}")
        print(f"  File Size: {result.file_size} bytes")
        print(f"  Execution Time: {result.execution_time:.2f} seconds")
        print(f"  Format: {result.format}")
        
        if result.success and os.path.exists(export_path):
            # Read and display first few lines
            with open(export_path, 'r') as f:
                lines = f.readlines()[:5]  # First 5 lines
                print(f"\nFirst few lines of exported file:")
                for i, line in enumerate(lines):
                    print(f"  {i+1}: {line.strip()}")
        
        # Clean up
        if os.path.exists(export_path):
            os.unlink(export_path)
    
    except Exception as e:
        print(f"Export failed: {str(e)}")
    
    print("\n")


def example_8_integration_manager():
    """Example 8: Using the main ImportExportManager."""
    print("=" * 60)
    print("Example 8: ImportExportManager Integration")
    print("=" * 60)
    
    manager = ImportExportManager()
    
    # Check supported formats
    supported_formats = manager.get_supported_formats()
    print(f"Supported formats: {supported_formats}")
    
    # Create a test file for validation
    test_data = [
        {'last_name': 'Test', 'first_name': 'Employee', 'employee_number': 'TEST001'}
    ]
    
    with tempfile.NamedTemporaryFile(suffix='.csv', delete=False) as tmp_file:
        test_file_path = tmp_file.name
    
    try:
        # Create test file
        CSVProcessor.export_to_csv(test_data, test_file_path)
        
        # Validate file
        is_valid, validation_errors = manager.validate_file(test_file_path, check_content=True)
        
        print(f"\nFile Validation:")
        print(f"  File: {test_file_path}")
        print(f"  Valid: {is_valid}")
        
        if validation_errors:
            print(f"  Validation Errors:")
            for error in validation_errors:
                print(f"    - {error}")
        
        if is_valid:
            # Test import through manager
            def manager_progress(current, total, message):
                print(f"  Manager Progress: {current}/{total} - {message}")
            
            result = manager.import_data('employee', test_file_path, manager_progress)
            
            print(f"\nManager Import Results:")
            print(f"  {result.get_summary()}")
    
    finally:
        if os.path.exists(test_file_path):
            os.unlink(test_file_path)
    
    print("\n")


def example_9_chunked_processing():
    """Example 9: Chunked processing for large files (simulation)."""
    print("=" * 60)
    print("Example 9: Chunked Processing Simulation")
    print("=" * 60)
    
    # Create chunked processor
    processor = ChunkedFileProcessor(chunk_size=100, memory_threshold=85.0)
    
    print("Simulating chunked processing...")
    
    # Define chunk processing function
    def process_chunk_simulation(chunk_data, data_type, progress_callback):
        """Simulate processing a chunk of data."""
        print(f"    Processing chunk with {len(chunk_data)} records of type {data_type}")
        
        # Simulate some processing time
        time.sleep(0.1)
        
        # Simulate some errors
        error_count = len(chunk_data) // 20  # 5% error rate
        processed_count = len(chunk_data) - error_count
        
        return {
            'processed': processed_count,
            'errors': error_count,
            'error_list': [f"Simulated error {i}" for i in range(error_count)],
            'warning_list': []
        }
    
    # Simulate large file processing
    # Note: In real usage, this would read actual file chunks
    print("  Note: This is a simulation - real implementation would read file chunks")
    
    # Create fake file path for demonstration
    fake_file_path = "/tmp/large_file_simulation.xlsx"
    
    try:
        # This is just for demonstration - real implementation would handle actual files
        print(f"  Would process file: {fake_file_path}")
        print(f"  Chunk size: {processor.chunk_size}")
        print(f"  Memory threshold: {processor.memory_threshold}%")
        
        # Simulate the process
        print(f"  Simulating chunked processing...")
        
        # Create sample result
        result = {
            'success': True,
            'total_processed': 1000,
            'total_errors': 50,
            'errors': ['Sample error 1', 'Sample error 2'],
            'warnings': [],
            'chunks_processed': 10,
            'execution_time': 5.2,
            'rollback_summary': processor.rollback_manager.get_rollback_summary()
        }
        
        print(f"\nChunked Processing Results (simulated):")
        print(f"  Success: {result['success']}")
        print(f"  Total Processed: {result['total_processed']}")
        print(f"  Total Errors: {result['total_errors']}")
        print(f"  Chunks Processed: {result['chunks_processed']}")
        print(f"  Execution Time: {result['execution_time']} seconds")
        
    except Exception as e:
        print(f"  Chunked processing simulation error: {str(e)}")
    
    print("\n")


def example_10_comprehensive_workflow():
    """Example 10: Complete workflow from validation to import."""
    print("=" * 60)
    print("Example 10: Comprehensive Import Workflow")
    print("=" * 60)
    
    # Step 1: Create sample data with mixed quality
    sample_data = [
        # Good records
        {
            'employee_number': 'WF001',
            'last_name': 'Johnson',
            'first_name': 'Michael',
            'email': 'michael.johnson@company.com',
            'hire_date': '2023-01-15',
            'is_active': 'TRUE'
        },
        {
            'employee_number': 'WF002',
            'last_name': 'Davis',
            'first_name': 'Sarah',
            'email': 'sarah.davis@company.com',
            'hire_date': '2023-02-01',
            'is_active': 'TRUE'
        },
        # Record with warning (future hire date)
        {
            'employee_number': 'WF003',
            'last_name': 'Wilson',
            'first_name': 'Robert',
            'email': 'robert.wilson@company.com',
            'hire_date': '2025-01-01',  # Future date - should trigger warning
            'is_active': 'TRUE'
        },
        # Bad record (missing required field)
        {
            'employee_number': 'WF004',
            'first_name': 'Invalid',
            'email': 'invalid@company.com'
            # Missing last_name
        }
    ]
    
    print(f"Step 1: Created {len(sample_data)} sample records")
    
    # Step 2: Create temporary file
    with tempfile.NamedTemporaryFile(suffix='.csv', delete=False) as tmp_file:
        workflow_file_path = tmp_file.name
    
    try:
        # Step 3: Export to CSV
        success = CSVProcessor.export_to_csv(sample_data, workflow_file_path)
        print(f"Step 2: Created CSV file: {success}")
        
        # Step 4: System performance check
        performance = get_system_performance_report()
        print(f"Step 3: System performance check completed")
        
        # Step 5: File validation
        manager = ImportExportManager()
        is_valid, file_errors = manager.validate_file(workflow_file_path, check_content=True)
        print(f"Step 4: File validation - Valid: {is_valid}")
        
        if file_errors:
            print("  File validation errors:")
            for error in file_errors:
                print(f"    - {error}")
            return
        
        # Step 6: Data validation
        print("Step 5: Performing comprehensive data validation...")
        
        # Read data for validation
        data_rows, headers = CSVProcessor.read_csv_file(workflow_file_path)
        
        validation_result = BatchValidationEngine.validate_batch_comprehensive(
            data_rows, 'employee'
        )
        
        print(f"  Validation Results:")
        print(f"    Total: {validation_result['total_records']}")
        print(f"    Valid: {validation_result['valid_records']}")
        print(f"    Errors: {validation_result['error_records']}")
        print(f"    Warnings: {validation_result['warning_records']}")
        
        # Step 7: Decision point
        if validation_result['error_records'] > 0:
            print("  ❌ Validation failed - showing errors:")
            for error in validation_result['errors'][:3]:
                print(f"    Row {error['row']}: {error['errors']}")
            print("  Please fix errors before proceeding with import.")
            return
        
        # Step 8: Proceed with import
        print("Step 6: Proceeding with import...")
        
        def workflow_progress(current, total, message):
            percentage = (current / total) * 100
            print(f"  Import Progress: {percentage:6.1f}% - {message}")
        
        result = manager.import_data('employee', workflow_file_path, workflow_progress)
        
        # Step 9: Results analysis
        print(f"Step 7: Import completed")
        print(f"  Success: {result.success}")
        print(f"  Processed: {result.processed_records}/{result.total_records}")
        print(f"  Errors: {result.error_records}")
        print(f"  Created: {len(result.created_objects)}")
        print(f"  Updated: {len(result.updated_objects)}")
        print(f"  Time: {result.execution_time:.2f}s")
        
        if hasattr(result, 'memory_usage') and result.memory_usage:
            print(f"  Memory: {result.memory_usage:.1f}MB")
        
        # Step 10: Summary report
        print(f"\nStep 8: Workflow Summary")
        print(f"  ✓ File validation passed")
        print(f"  ✓ Data validation completed")
        print(f"  {'✓' if result.success else '✗'} Import {'succeeded' if result.success else 'failed'}")
        print(f"  📊 {result.processed_records} employees processed")
        
        if result.errors:
            print(f"  ⚠️ {len(result.errors)} errors encountered")
    
    finally:
        # Cleanup
        if os.path.exists(workflow_file_path):
            os.unlink(workflow_file_path)
    
    print("\n")


def main():
    """Run all examples."""
    print("Django Payroll Import/Export System - Comprehensive Examples")
    print("=" * 70)
    print(f"Started at: {datetime.now()}")
    print("\n")
    
    # List of all examples
    examples = [
        example_1_basic_employee_import,
        example_2_excel_template_creation,
        example_3_data_validation,
        example_4_memory_monitoring,
        example_5_enhanced_progress_tracking,
        example_6_rollback_management,
        example_7_export_operations,
        example_8_integration_manager,
        example_9_chunked_processing,
        example_10_comprehensive_workflow
    ]
    
    try:
        for i, example_func in enumerate(examples, 1):
            print(f"Running Example {i}/{len(examples)}: {example_func.__name__}")
            try:
                example_func()
            except Exception as e:
                print(f"❌ Example {i} failed: {str(e)}")
                print("Continuing with next example...\n")
            
            # Brief pause between examples
            time.sleep(0.5)
    
    except KeyboardInterrupt:
        print("\n⏹️ Examples interrupted by user")
    
    print("=" * 70)
    print(f"Examples completed at: {datetime.now()}")
    print("=" * 70)


if __name__ == "__main__":
    # Note: This file is designed to be run within a Django environment
    # Run with: python manage.py shell < USAGE_EXAMPLES.py
    
    print("📋 Import/Export Usage Examples")
    print("To run these examples:")
    print("1. Set up your Django environment")
    print("2. Run: python manage.py shell < USAGE_EXAMPLES.py")
    print("3. Or import individual examples in Django shell")
    print("\nExample functions available:")
    print("- example_1_basic_employee_import()")
    print("- example_2_excel_template_creation()")
    print("- example_3_data_validation()")
    print("- example_4_memory_monitoring()")
    print("- example_5_enhanced_progress_tracking()")
    print("- example_6_rollback_management()")
    print("- example_7_export_operations()")
    print("- example_8_integration_manager()")
    print("- example_9_chunked_processing()")
    print("- example_10_comprehensive_workflow()")
    print("- main()  # Run all examples")
    
    # Uncomment the next line to run all examples automatically
    # main()