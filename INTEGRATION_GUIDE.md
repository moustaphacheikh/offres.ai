# Import/Export System Integration Guide

## Overview

This guide explains how to integrate the enhanced import/export capabilities with the existing Django payroll system. The system consists of two main components:

1. **`core/utils/import_export.py`** - The main import/export system (already implemented)
2. **`core/utils/import_export_enhancements.py`** - Advanced enhancements for large-scale operations

## File Structure

```
core/utils/
├── import_export.py              # Main import/export system (1,384 lines)
├── import_export_enhancements.py # Enhanced utilities (new file)
└── __init__.py                   # Package initialization
```

## Quick Start Examples

### 1. Basic Import Operation

```python
from core.utils.import_export import ImportExportManager

# Initialize manager
manager = ImportExportManager()

# Progress callback
def progress_callback(current, total, message):
    print(f"Progress: {current}/{total} ({(current/total)*100:.1f}%) - {message}")

# Import employees
result = manager.import_data('employee', 'employees.xlsx', progress_callback)
print(f"Import result: {result.get_summary()}")
```

### 2. Enhanced Import with Memory Optimization

```python
from core.utils.import_export import ImportExportManager
from core.utils.import_export_enhancements import (
    MemoryOptimizer, EnhancedProgressTracker, RollbackManager
)

# Check system performance first
memory_info = MemoryOptimizer.monitor_memory()
print(f"System memory usage: {memory_info.get('system_percent', 0):.1f}%")

# Enhanced progress tracking
def enhanced_progress_callback(current, total, message):
    percentage = (current / total) * 100
    print(f"Enhanced Progress: {percentage:.1f}% - {message}")

# Enhanced import with memory monitoring
manager = ImportExportManager()
tracker = EnhancedProgressTracker(1000, enhanced_progress_callback)

result = manager.import_data('employee', 'large_employees.xlsx', enhanced_progress_callback)
performance_summary = tracker.get_performance_summary()
print(f"Performance: {performance_summary}")
```

### 3. Batch Validation Before Import

```python
from core.utils.import_export_enhancements import BatchValidationEngine
from core.utils.import_export import CSVProcessor, ExcelProcessor

# Read data for validation
file_path = 'employees.xlsx'
processor = ExcelProcessor()
data_rows, headers = processor.read_excel_file(file_path)

# Comprehensive batch validation
validation_result = BatchValidationEngine.validate_batch_comprehensive(
    data_rows, 'employee', progress_callback
)

print(f"Validation Summary:")
print(f"- Total records: {validation_result['total_records']}")
print(f"- Valid records: {validation_result['valid_records']}")
print(f"- Error records: {validation_result['error_records']}")
print(f"- Duplicate issues: {len(validation_result['duplicate_errors'])}")

# Only proceed with import if validation passes
if validation_result['error_records'] == 0:
    # Proceed with import
    manager = ImportExportManager()
    result = manager.import_data('employee', file_path, progress_callback)
else:
    print("Validation failed. Please fix errors before importing:")
    for error in validation_result['errors'][:5]:
        print(f"Row {error['row']}: {error['errors']}")
```

### 4. Large File Processing with Chunking

```python
from core.utils.import_export_enhancements import ChunkedFileProcessor

def process_chunk(chunk_data, data_type, progress_callback):
    """Process a single chunk of data."""
    # This would use the regular import system for each chunk
    manager = ImportExportManager()
    
    # Create temporary file for chunk
    import tempfile
    with tempfile.NamedTemporaryFile(suffix='.csv', delete=False) as tmp_file:
        chunk_path = tmp_file.name
    
    try:
        # Export chunk to temporary file
        CSVProcessor.export_to_csv(chunk_data, chunk_path)
        
        # Import chunk
        result = manager.import_data(data_type, chunk_path, progress_callback)
        
        return {
            'processed': result.processed_records,
            'errors': result.error_records,
            'error_list': result.errors,
            'warning_list': result.warnings
        }
    finally:
        os.unlink(chunk_path)

# Process very large file
processor = ChunkedFileProcessor(chunk_size=500)
result = processor.process_large_file_chunked(
    'very_large_employees.xlsx',
    'employee',
    process_chunk,
    progress_callback
)

print(f"Chunked processing result: {result}")
```

## Advanced Features Usage

### Template Creation with Enhancements

```python
from core.utils.import_export import create_all_templates
from core.utils.import_export_enhancements import get_system_performance_report

# Check system status
performance = get_system_performance_report()
print(f"System status: {performance}")

# Create all templates
template_results = create_all_templates('./import_templates/')
print(f"Template creation results: {template_results}")
```

### Export with Advanced Filtering

```python
from core.utils.import_export import EmployeeImportExport

# Export with complex filters
filters = {
    'is_active': True,
    'department__name__in': ['IT', 'Finance', 'HR'],
    'hire_date__gte': '2020-01-01'
}

result = EmployeeImportExport.export_employees(
    'filtered_employees.xlsx',
    format='excel',
    filters=filters
)

print(f"Export completed: {result.get_summary()}")
```

### Django Admin Integration

```python
# In admin.py
from core.utils.import_export import make_import_action, make_export_action

class EmployeeAdmin(admin.ModelAdmin):
    actions = [
        make_import_action('employee'),
        make_export_action('employee'),
    ]
    
    list_display = ['employee_number', 'last_name', 'first_name', 'is_active']
    list_filter = ['is_active', 'department', 'hire_date']
```

## Error Handling and Recovery

### Rollback Management

```python
from core.utils.import_export_enhancements import RollbackManager

rollback_manager = RollbackManager()

try:
    with rollback_manager.transaction_savepoint("employee_import"):
        # Perform import operations
        result = manager.import_data('employee', file_path, progress_callback)
        
        if result.error_records > 0:
            raise Exception(f"Import had {result.error_records} errors")
            
except Exception as e:
    print(f"Import failed, rollback performed: {str(e)}")
    summary = rollback_manager.get_rollback_summary()
    print(f"Rollback summary: {summary}")
```

### Comprehensive Error Reporting

```python
from core.utils.import_export import get_import_summary_report

# After import
result = manager.import_data('employee', file_path, progress_callback)

# Generate comprehensive report
report = get_import_summary_report(result)
print(report)

# Save report to file
with open('import_report.txt', 'w') as f:
    f.write(report)
```

## Performance Optimization

### Memory Monitoring

```python
from core.utils.import_export_enhancements import MemoryOptimizer

# Before processing large files
memory_info = MemoryOptimizer.monitor_memory()
optimal_chunk_size = MemoryOptimizer.get_optimal_chunk_size(100.0)  # 100MB file

print(f"Recommended chunk size for 100MB file: {optimal_chunk_size}")
print(f"Current memory usage: {memory_info}")

# Monitor during processing
if MemoryOptimizer.should_pause_processing():
    print("High memory usage detected - pausing processing")
    time.sleep(2)
```

### Performance Tracking

```python
from core.utils.import_export_enhancements import EnhancedProgressTracker

tracker = EnhancedProgressTracker(10000)

# Simulate processing with performance tracking
for i in range(10000):
    # Simulate work
    time.sleep(0.001)
    
    # Update with error/warning tracking
    is_error = i % 100 == 0  # Every 100th item is an error
    is_warning = i % 50 == 0  # Every 50th item is a warning
    
    tracker.update(1, f"Processing item {i}", error=is_error, warning=is_warning)

tracker.complete("Processing finished")
summary = tracker.get_performance_summary()
print(f"Performance summary: {summary}")
```

## Configuration and Setup

### Dependencies Installation

```bash
# Required dependencies (already in requirements if using existing system)
pip install openpyxl django

# Optional for enhanced features
pip install psutil chardet
```

### Django Settings

```python
# settings.py
IMPORT_EXPORT_SETTINGS = {
    'MAX_FILE_SIZE_MB': 100,
    'DEFAULT_CHUNK_SIZE': 1000,
    'MEMORY_THRESHOLD_PERCENT': 85,
    'ENABLE_ROLLBACK': True,
    'LOG_LEVEL': 'INFO',
}
```

### Logging Configuration

```python
# settings.py
LOGGING = {
    'version': 1,
    'disable_existing_loggers': False,
    'handlers': {
        'import_export_file': {
            'level': 'INFO',
            'class': 'logging.FileHandler',
            'filename': 'logs/import_export.log',
        },
    },
    'loggers': {
        'core.utils.import_export': {
            'handlers': ['import_export_file'],
            'level': 'INFO',
            'propagate': True,
        },
    },
}
```

## Testing and Validation

### Test Data Creation

```python
# Create test data for validation
def create_test_employee_data():
    """Create test employee data for validation."""
    return [
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

# Test import process
test_data = create_test_employee_data()
processor = CSVProcessor()
processor.export_to_csv(test_data, 'test_employees.csv')

result = manager.import_data('employee', 'test_employees.csv', progress_callback)
assert result.success
assert result.processed_records == 2
```

### Validation Testing

```python
# Test validation engine
test_data_with_errors = [
    {'last_name': 'Doe'},  # Missing first_name
    {'last_name': 'Smith', 'first_name': 'Jane', 'email': 'invalid-email'},  # Invalid email
    {'last_name': 'Brown', 'first_name': 'Bob', 'employee_number': 'EMP001'},  # Duplicate number
]

validation_result = BatchValidationEngine.validate_batch_comprehensive(
    test_data_with_errors, 'employee'
)

assert validation_result['error_records'] == 3
assert len(validation_result['duplicate_errors']) > 0
```

## Troubleshooting

### Common Issues

1. **Memory Issues with Large Files**
   ```python
   # Solution: Use chunked processing
   processor = ChunkedFileProcessor(chunk_size=100)
   result = processor.process_large_file_chunked(...)
   ```

2. **Import Failures**
   ```python
   # Solution: Enable rollback and validation
   validation_result = BatchValidationEngine.validate_batch_comprehensive(data, 'employee')
   if validation_result['error_records'] == 0:
       # Proceed with import
       pass
   ```

3. **Performance Issues**
   ```python
   # Solution: Monitor and optimize
   memory_info = MemoryOptimizer.monitor_memory()
   if memory_info['system_percent'] > 80:
       # Reduce batch size or enable chunked processing
       pass
   ```

### Debug Mode

```python
# Enable debug logging
import logging
logging.getLogger('core.utils.import_export').setLevel(logging.DEBUG)

# Use enhanced progress tracking for detailed monitoring
tracker = EnhancedProgressTracker(total_items, progress_callback)
```

## Production Deployment

### Recommendations

1. **File Size Limits**: Configure appropriate limits in production
2. **Memory Monitoring**: Set up system monitoring for memory usage
3. **Error Logging**: Implement comprehensive error logging
4. **Backup Strategy**: Always backup data before large imports
5. **Performance Testing**: Test with production-sized datasets

### Monitoring Dashboard

```python
def get_import_export_dashboard():
    """Get dashboard data for monitoring."""
    return {
        'system_performance': get_system_performance_report(),
        'recent_imports': get_recent_import_history(),
        'error_summary': get_error_summary(),
        'memory_usage': MemoryOptimizer.monitor_memory()
    }
```

This integration guide provides a complete roadmap for using the enhanced import/export system in production while maintaining compatibility with the existing codebase.