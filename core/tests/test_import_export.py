"""
Tests for the import/export utilities.

This module contains comprehensive tests for the import/export functionality
including Excel and CSV processing, data validation, and integration tests.
"""

import tempfile
import os
from datetime import date, datetime
from decimal import Decimal
from django.test import TestCase
from django.db import transaction

from core.utils.import_export import (
    ImportExportManager,
    DataValidator,
    EmployeeImportExport,
    ImportResult,
    ExportResult,
    ProgressTracker,
    CSVProcessor,
)
from core.models import Employee, Department, Position


class DataValidatorTestCase(TestCase):
    """Test cases for DataValidator."""
    
    def test_validate_employee_data_valid(self):
        """Test validation of valid employee data."""
        data = {
            'last_name': 'Doe',
            'first_name': 'John',
            'email': 'john.doe@company.com',
            'phone': '+1-555-0123',
            'birth_date': '1990-01-15',
            'hire_date': '2023-01-01'
        }
        
        is_valid, errors = DataValidator.validate_employee_data(data)
        self.assertTrue(is_valid)
        self.assertEqual(len(errors), 0)
    
    def test_validate_employee_data_missing_required_fields(self):
        """Test validation with missing required fields."""
        data = {
            'first_name': 'John',
            # missing last_name
            'email': 'john.doe@company.com'
        }
        
        is_valid, errors = DataValidator.validate_employee_data(data)
        self.assertFalse(is_valid)
        self.assertIn('Missing required field: last_name', errors)
    
    def test_validate_employee_data_invalid_email(self):
        """Test validation with invalid email."""
        data = {
            'last_name': 'Doe',
            'first_name': 'John',
            'email': 'invalid-email'
        }
        
        is_valid, errors = DataValidator.validate_employee_data(data)
        self.assertFalse(is_valid)
        self.assertTrue(any('Invalid email format' in error for error in errors))
    
    def test_validate_payroll_element_data_valid(self):
        """Test validation of valid payroll element data."""
        data = {
            'label': 'Base Salary',
            'type': 'G',
            'rate': '1.0',
            'amount': '5000'
        }
        
        is_valid, errors = DataValidator.validate_payroll_element_data(data)
        self.assertTrue(is_valid)
        self.assertEqual(len(errors), 0)
    
    def test_validate_payroll_element_data_invalid_type(self):
        """Test validation with invalid payroll element type."""
        data = {
            'label': 'Base Salary',
            'type': 'X',  # Invalid type
        }
        
        is_valid, errors = DataValidator.validate_payroll_element_data(data)
        self.assertFalse(is_valid)
        self.assertIn("Invalid type: X. Must be 'G' or 'D'", errors)


class CSVProcessorTestCase(TestCase):
    """Test cases for CSV processing."""
    
    def test_csv_read_write_cycle(self):
        """Test reading and writing CSV files."""
        # Test data
        test_data = [
            {'name': 'John Doe', 'age': '30', 'city': 'New York'},
            {'name': 'Jane Smith', 'age': '25', 'city': 'Boston'},
        ]
        
        with tempfile.NamedTemporaryFile(mode='w', suffix='.csv', delete=False) as tmp_file:
            tmp_path = tmp_file.name
        
        try:
            # Write CSV
            success = CSVProcessor.export_to_csv(test_data, tmp_path)
            self.assertTrue(success)
            
            # Read CSV
            read_data, headers = CSVProcessor.read_csv_file(tmp_path)
            
            # Verify
            self.assertEqual(len(read_data), 2)
            self.assertEqual(headers, ['name', 'age', 'city'])
            self.assertEqual(read_data[0]['name'], 'John Doe')
            self.assertEqual(read_data[1]['age'], '25')
            
        finally:
            if os.path.exists(tmp_path):
                os.unlink(tmp_path)


class ProgressTrackerTestCase(TestCase):
    """Test cases for ProgressTracker."""
    
    def test_progress_tracking(self):
        """Test progress tracking functionality."""
        callback_calls = []
        
        def test_callback(current, total, message):
            callback_calls.append((current, total, message))
        
        tracker = ProgressTracker(100, test_callback)
        
        # Update progress
        tracker.update(10, "Processing...")
        tracker.update(40, "Still processing...")
        tracker.complete("Done!")
        
        # Verify callbacks were made
        self.assertGreater(len(callback_calls), 0)
        
        # Check final callback
        final_call = callback_calls[-1]
        self.assertEqual(final_call[0], 100)  # current
        self.assertEqual(final_call[1], 100)  # total
        self.assertEqual(final_call[2], "Done!")  # message


class EmployeeImportExportTestCase(TestCase):
    """Test cases for employee import/export functionality."""
    
    def setUp(self):
        """Set up test data."""
        # Create test department and position
        self.department = Department.objects.create(
            name="Test Department",
        )
        self.position = Position.objects.create(
            title="Test Position",
        )
    
    def test_employee_data_cleaning(self):
        """Test employee data cleaning functionality."""
        raw_data = {
            'last_name': '  Doe  ',
            'first_name': '  John  ',
            'birth_date': '1990-01-15',
            'hire_date': '2023/01/01',
            'children_count': '2',
            'is_active': 'TRUE'
        }
        
        cleaned = EmployeeImportExport._clean_employee_data(raw_data)
        
        self.assertEqual(cleaned['last_name'], 'Doe')
        self.assertEqual(cleaned['first_name'], 'John')
        self.assertEqual(cleaned['birth_date'], date(1990, 1, 15))
        self.assertEqual(cleaned['children_count'], 2)
        self.assertTrue(cleaned['is_active'])
    
    def test_date_parsing(self):
        """Test date parsing functionality."""
        # Test various date formats
        test_cases = [
            ('2023-01-15', date(2023, 1, 15)),
            ('15/01/2023', date(2023, 1, 15)),
            ('01/15/2023', date(2023, 1, 15)),
            ('2023-01-15 10:30:00', date(2023, 1, 15)),
            ('', None),
            (None, None),
        ]
        
        for date_str, expected in test_cases:
            result = EmployeeImportExport._parse_date(date_str)
            self.assertEqual(result, expected, f"Failed for input: {date_str}")
    
    def test_csv_employee_import_basic(self):
        """Test basic CSV employee import functionality."""
        # Create test CSV data
        csv_data = [
            {'last_name': 'Doe', 'first_name': 'John', 'employee_number': 'EMP001'},
            {'last_name': 'Smith', 'first_name': 'Jane', 'employee_number': 'EMP002'},
        ]
        
        with tempfile.NamedTemporaryFile(mode='w', suffix='.csv', delete=False) as tmp_file:
            tmp_path = tmp_file.name
        
        try:
            # Write test data to CSV
            CSVProcessor.export_to_csv(csv_data, tmp_path)
            
            # Test import
            result = EmployeeImportExport.import_employees(tmp_path)
            
            # Verify results
            self.assertTrue(result.success)
            self.assertEqual(result.total_records, 2)
            self.assertEqual(result.processed_records, 2)
            self.assertEqual(result.error_records, 0)
            
            # Verify employees were created
            self.assertEqual(Employee.objects.count(), 2)
            employee1 = Employee.objects.get(employee_number='EMP001')
            self.assertEqual(employee1.last_name, 'Doe')
            self.assertEqual(employee1.first_name, 'John')
            
        finally:
            if os.path.exists(tmp_path):
                os.unlink(tmp_path)
    
    def test_csv_employee_export_basic(self):
        """Test basic CSV employee export functionality."""
        # Create test employees
        Employee.objects.create(
            employee_number='EMP001',
            last_name='Doe',
            first_name='John',
            email='john.doe@test.com'
        )
        Employee.objects.create(
            employee_number='EMP002',
            last_name='Smith',
            first_name='Jane',
            email='jane.smith@test.com'
        )
        
        with tempfile.NamedTemporaryFile(suffix='.csv', delete=False) as tmp_file:
            tmp_path = tmp_file.name
        
        try:
            # Test export
            result = EmployeeImportExport.export_employees(tmp_path, 'csv')
            
            # Verify results
            self.assertTrue(result.success)
            self.assertEqual(result.total_records, 2)
            self.assertEqual(result.exported_records, 2)
            self.assertGreater(result.file_size, 0)
            
            # Verify file contents
            read_data, headers = CSVProcessor.read_csv_file(tmp_path)
            self.assertEqual(len(read_data), 2)
            
            # Find John Doe in the data
            john_row = next((row for row in read_data if row['employee_number'] == 'EMP001'), None)
            self.assertIsNotNone(john_row)
            self.assertEqual(john_row['last_name'], 'Doe')
            
        finally:
            if os.path.exists(tmp_path):
                os.unlink(tmp_path)


class ImportExportManagerTestCase(TestCase):
    """Test cases for ImportExportManager."""
    
    def test_manager_initialization(self):
        """Test manager initialization."""
        manager = ImportExportManager()
        self.assertIsNotNone(manager)
    
    def test_get_supported_formats(self):
        """Test getting supported formats."""
        manager = ImportExportManager()
        formats = manager.get_supported_formats()
        
        self.assertIn('csv', formats)
        # Note: Excel format availability depends on openpyxl installation
    
    def test_validate_file(self):
        """Test file validation."""
        manager = ImportExportManager()
        
        # Test non-existent file
        is_valid, errors = manager.validate_file('/nonexistent/file.csv')
        self.assertFalse(is_valid)
        self.assertTrue(any('does not exist' in error for error in errors))
        
        # Test valid temporary file
        with tempfile.NamedTemporaryFile(suffix='.csv', delete=False) as tmp_file:
            tmp_path = tmp_file.name
        
        try:
            is_valid, errors = manager.validate_file(tmp_path)
            self.assertTrue(is_valid)
            self.assertEqual(len(errors), 0)
            
        finally:
            if os.path.exists(tmp_path):
                os.unlink(tmp_path)
        
        # Test unsupported format
        with tempfile.NamedTemporaryFile(suffix='.txt', delete=False) as tmp_file:
            tmp_path = tmp_file.name
        
        try:
            is_valid, errors = manager.validate_file(tmp_path)
            self.assertFalse(is_valid)
            self.assertTrue(any('Unsupported file format' in error for error in errors))
            
        finally:
            if os.path.exists(tmp_path):
                os.unlink(tmp_path)


class ImportResultTestCase(TestCase):
    """Test cases for ImportResult."""
    
    def test_import_result_creation(self):
        """Test ImportResult creation and methods."""
        result = ImportResult(
            success=True,
            total_records=100,
            processed_records=95,
            error_records=5,
            errors=[{'row': 10, 'error': 'test error'}],
            warnings=[],
            created_objects=[],
            updated_objects=[],
            execution_time=1.5
        )
        
        # Test properties
        self.assertTrue(result.success)
        self.assertEqual(result.total_records, 100)
        self.assertEqual(result.processed_records, 95)
        
        # Test summary
        summary = result.get_summary()
        self.assertIn('95/100 processed', summary)
        self.assertIn('5 errors', summary)
        self.assertIn('1.50s', summary)
        
        # Test to_dict
        result_dict = result.to_dict()
        self.assertEqual(result_dict['success'], True)
        self.assertEqual(result_dict['total_records'], 100)


class ExportResultTestCase(TestCase):
    """Test cases for ExportResult."""
    
    def test_export_result_creation(self):
        """Test ExportResult creation and methods."""
        result = ExportResult(
            success=True,
            total_records=50,
            exported_records=50,
            file_path='/tmp/export.csv',
            file_size=1024,
            execution_time=0.5,
            format='csv',
            errors=[]
        )
        
        # Test properties
        self.assertTrue(result.success)
        self.assertEqual(result.total_records, 50)
        self.assertEqual(result.exported_records, 50)
        
        # Test summary
        summary = result.get_summary()
        self.assertIn('50/50 records exported', summary)
        self.assertIn('/tmp/export.csv', summary)
        self.assertIn('0.50s', summary)


class IntegrationTestCase(TestCase):
    """Integration tests for the complete import/export workflow."""
    
    def test_full_employee_workflow(self):
        """Test complete employee import/export workflow."""
        # Step 1: Create test data
        test_employees = [
            {
                'employee_number': 'EMP001',
                'last_name': 'Doe',
                'first_name': 'John',
                'email': 'john.doe@company.com',
                'hire_date': '2023-01-15',
                'is_active': 'TRUE'
            },
            {
                'employee_number': 'EMP002',
                'last_name': 'Smith',
                'first_name': 'Jane',
                'email': 'jane.smith@company.com',
                'hire_date': '2023-02-01',
                'is_active': 'TRUE'
            }
        ]
        
        # Step 2: Export to CSV
        with tempfile.NamedTemporaryFile(suffix='.csv', delete=False) as tmp_file:
            import_path = tmp_file.name
        
        try:
            CSVProcessor.export_to_csv(test_employees, import_path)
            
            # Step 3: Import from CSV
            import_result = EmployeeImportExport.import_employees(import_path)
            
            # Verify import
            self.assertTrue(import_result.success)
            self.assertEqual(import_result.total_records, 2)
            self.assertEqual(Employee.objects.count(), 2)
            
            # Step 4: Export back to CSV
            with tempfile.NamedTemporaryFile(suffix='.csv', delete=False) as tmp_file:
                export_path = tmp_file.name
            
            try:
                export_result = EmployeeImportExport.export_employees(export_path, 'csv')
                
                # Verify export
                self.assertTrue(export_result.success)
                self.assertEqual(export_result.total_records, 2)
                
                # Step 5: Verify exported data
                exported_data, headers = CSVProcessor.read_csv_file(export_path)
                self.assertEqual(len(exported_data), 2)
                
                # Find and verify specific employee
                john_data = next(
                    (row for row in exported_data if row['employee_number'] == 'EMP001'),
                    None
                )
                self.assertIsNotNone(john_data)
                self.assertEqual(john_data['last_name'], 'Doe')
                self.assertEqual(john_data['first_name'], 'John')
                
            finally:
                if os.path.exists(export_path):
                    os.unlink(export_path)
                    
        finally:
            if os.path.exists(import_path):
                os.unlink(import_path)