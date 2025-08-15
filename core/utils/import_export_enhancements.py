"""
Enhanced utilities for the Django payroll import/export system.

This module contains advanced enhancements for memory optimization, rollback mechanisms,
chunked processing, and performance monitoring that can be integrated into the main
import_export.py file.

These enhancements address:
- Memory optimization for large files
- Enhanced progress tracking with performance metrics
- Rollback mechanisms for failed imports
- Batch validation with comprehensive reporting
- Chunked processing for very large datasets
"""

import os
import time
import threading
import traceback
from contextlib import contextmanager
from datetime import datetime
from typing import Dict, List, Optional, Callable, Any, Iterator, Tuple

try:
    import psutil
    PSUTIL_AVAILABLE = True
except ImportError:
    PSUTIL_AVAILABLE = False

from django.db import transaction
from django.core.exceptions import ValidationError


class MemoryOptimizer:
    """Memory optimization utilities for large file processing."""
    
    @staticmethod
    def get_optimal_chunk_size(file_size_mb: float, available_memory_percent: float = 10.0) -> int:
        """
        Calculate optimal chunk size for processing based on file size and available memory.
        
        Args:
            file_size_mb: Size of file in MB
            available_memory_percent: Percentage of available memory to use (default: 10%)
            
        Returns:
            Optimal chunk size for processing
        """
        if not PSUTIL_AVAILABLE:
            # Conservative fallback when psutil is not available
            if file_size_mb < 10:
                return 1000
            elif file_size_mb < 50:
                return 500
            else:
                return 100
        
        try:
            # Get system memory info
            memory = psutil.virtual_memory()
            available_memory_mb = (memory.available / (1024 * 1024)) * (available_memory_percent / 100)
            
            # Calculate based on estimated memory usage per record
            estimated_memory_per_record = 0.5  # KB per record (rough estimate)
            max_records_by_memory = int(available_memory_mb * 1024 / estimated_memory_per_record)
            
            # Calculate based on file size
            estimated_records_per_mb = 100  # Rough estimate
            total_estimated_records = int(file_size_mb * estimated_records_per_mb)
            
            # Choose conservative chunk size
            optimal_size = min(max_records_by_memory, total_estimated_records // 10, 2000)
            return max(100, optimal_size)  # Minimum 100 records per chunk
            
        except Exception:
            # Fallback calculation
            return max(100, min(1000, int(1000 / max(1, file_size_mb / 10))))
    
    @staticmethod
    def monitor_memory() -> Dict[str, float]:
        """
        Monitor current memory usage.
        
        Returns:
            Dictionary with memory usage statistics
        """
        if not PSUTIL_AVAILABLE:
            return {'rss_mb': 0, 'vms_mb': 0, 'percent': 0, 'available_mb': 0}
        
        try:
            process = psutil.Process()
            memory_info = process.memory_info()
            system_memory = psutil.virtual_memory()
            
            return {
                'rss_mb': memory_info.rss / (1024 * 1024),
                'vms_mb': memory_info.vms / (1024 * 1024),
                'percent': process.memory_percent(),
                'available_mb': system_memory.available / (1024 * 1024),
                'system_percent': system_memory.percent
            }
        except Exception:
            return {'rss_mb': 0, 'vms_mb': 0, 'percent': 0, 'available_mb': 0}
    
    @staticmethod
    def should_pause_processing(memory_threshold: float = 85.0) -> bool:
        """
        Check if processing should be paused due to high memory usage.
        
        Args:
            memory_threshold: Memory usage percentage threshold
            
        Returns:
            True if processing should be paused
        """
        memory_info = MemoryOptimizer.monitor_memory()
        return memory_info.get('system_percent', 0) > memory_threshold


class RollbackManager:
    """Manages rollback operations for failed imports."""
    
    def __init__(self):
        self.savepoints = []
        self.rollback_data = []
        self.created_objects = []  # Track objects created during import
        self.modified_objects = []  # Track objects modified during import
    
    @contextmanager
    def transaction_savepoint(self, name: str):
        """
        Create a transaction savepoint for rollback.
        
        Args:
            name: Name identifier for the savepoint
        """
        try:
            savepoint = transaction.savepoint()
            self.savepoints.append((name, savepoint))
            yield
            transaction.savepoint_commit(savepoint)
        except Exception as e:
            if self.savepoints:
                _, savepoint = self.savepoints[-1]
                transaction.savepoint_rollback(savepoint)
                self._log_rollback(name, str(e))
            raise
        finally:
            if self.savepoints:
                self.savepoints.pop()
    
    def add_rollback_data(self, operation: str, model_name: str, pk: Any, data: Dict):
        """
        Add data for potential rollback.
        
        Args:
            operation: Type of operation ('create', 'update', 'delete')
            model_name: Name of the model
            pk: Primary key of the object
            data: Original data for rollback
        """
        self.rollback_data.append({
            'operation': operation,
            'model': model_name,
            'pk': pk,
            'data': data,
            'timestamp': datetime.now()
        })
    
    def track_created_object(self, obj: Any):
        """Track an object that was created during import."""
        self.created_objects.append({
            'model': obj.__class__.__name__,
            'pk': obj.pk,
            'timestamp': datetime.now()
        })
    
    def track_modified_object(self, obj: Any, original_data: Dict):
        """Track an object that was modified during import."""
        self.modified_objects.append({
            'model': obj.__class__.__name__,
            'pk': obj.pk,
            'original_data': original_data,
            'timestamp': datetime.now()
        })
    
    def perform_rollback(self) -> bool:
        """
        Perform manual rollback of operations (for non-transactional rollbacks).
        
        Returns:
            True if rollback was successful
        """
        try:
            # This would implement manual rollback logic
            # In practice, we prefer transaction rollbacks
            for item in reversed(self.rollback_data):
                self._log_rollback_item(item)
            return True
        except Exception as e:
            print(f"Rollback failed: {str(e)}")
            return False
    
    def _log_rollback(self, name: str, error: str):
        """Log rollback operation."""
        print(f"ROLLBACK: Savepoint '{name}' rolled back due to error: {error}")
    
    def _log_rollback_item(self, item: Dict):
        """Log individual rollback item."""
        print(f"ROLLBACK: {item['operation']} on {item['model']} {item['pk']}")
    
    def get_rollback_summary(self) -> Dict[str, Any]:
        """Get summary of rollback operations."""
        return {
            'total_rollback_items': len(self.rollback_data),
            'created_objects': len(self.created_objects),
            'modified_objects': len(self.modified_objects),
            'savepoints_used': len(self.savepoints)
        }


class EnhancedProgressTracker:
    """Enhanced progress tracking with memory monitoring and performance metrics."""
    
    def __init__(self, total_items: int, callback: Optional[Callable[[int, int, str], None]] = None):
        self.total_items = total_items
        self.processed_items = 0
        self.callback = callback
        self.start_time = datetime.now()
        self.last_update = self.start_time
        self.error_count = 0
        self.warning_count = 0
        self.memory_snapshots = []
        self.processing_rates = []
        self._lock = threading.Lock()
        self.memory_optimizer = MemoryOptimizer()
    
    def update(self, increment: int = 1, message: str = "", error: bool = False, warning: bool = False):
        """
        Update progress with enhanced tracking.
        
        Args:
            increment: Number of items processed
            message: Progress message
            error: Whether this update represents an error
            warning: Whether this update represents a warning
        """
        with self._lock:
            self.processed_items += increment
            if error:
                self.error_count += 1
            if warning:
                self.warning_count += 1
            
            current_time = datetime.now()
            
            # Update every 1% or every 5 seconds
            percentage = (self.processed_items / self.total_items) * 100 if self.total_items > 0 else 0
            time_since_last = (current_time - self.last_update).total_seconds()
            
            if percentage % 1 < 0.1 or time_since_last >= 5:
                # Capture performance metrics
                self._capture_performance_metrics(current_time)
                
                if self.callback:
                    enhanced_message = self._format_enhanced_message(message, percentage)
                    self.callback(self.processed_items, self.total_items, enhanced_message)
                
                self.last_update = current_time
    
    def _capture_performance_metrics(self, current_time: datetime):
        """Capture current performance metrics."""
        # Memory snapshot
        memory_info = self.memory_optimizer.monitor_memory()
        if memory_info['rss_mb'] > 0:  # Only add if we have valid data
            self.memory_snapshots.append((current_time, memory_info))
        
        # Processing rate
        elapsed = (current_time - self.start_time).total_seconds()
        if elapsed > 0:
            rate = self.processed_items / elapsed
            self.processing_rates.append((current_time, rate))
    
    def _format_enhanced_message(self, message: str, percentage: float) -> str:
        """Format message with enhanced information."""
        base_message = f"{message} ({percentage:.1f}%)"
        
        if self.error_count > 0 or self.warning_count > 0:
            base_message += f" [Errors: {self.error_count}, Warnings: {self.warning_count}]"
        
        # Add memory info if available
        if self.memory_snapshots:
            latest_memory = self.memory_snapshots[-1][1]
            if latest_memory.get('rss_mb', 0) > 100:  # Only show if significant memory usage
                base_message += f" [Memory: {latest_memory['rss_mb']:.0f}MB]"
        
        return base_message
    
    def complete(self, message: str = "Completed"):
        """Mark processing as complete with final summary."""
        with self._lock:
            final_message = self._format_completion_message(message)
            if self.callback:
                self.callback(self.total_items, self.total_items, final_message)
    
    def _format_completion_message(self, message: str) -> str:
        """Format completion message with summary."""
        elapsed = (datetime.now() - self.start_time).total_seconds()
        rate = self.processed_items / elapsed if elapsed > 0 else 0
        
        summary_parts = [
            message,
            f"Rate: {rate:.1f}/sec",
            f"Errors: {self.error_count}",
            f"Warnings: {self.warning_count}"
        ]
        
        return " | ".join(summary_parts)
    
    def get_performance_summary(self) -> Dict[str, Any]:
        """Get comprehensive performance summary."""
        elapsed = (datetime.now() - self.start_time).total_seconds()
        rate = self.processed_items / elapsed if elapsed > 0 else 0
        
        memory_info = {}
        if self.memory_snapshots:
            memory_values = [snapshot[1]['rss_mb'] for snapshot in self.memory_snapshots if snapshot[1]['rss_mb'] > 0]
            if memory_values:
                memory_info = {
                    'peak_memory_mb': max(memory_values),
                    'avg_memory_mb': sum(memory_values) / len(memory_values),
                    'min_memory_mb': min(memory_values),
                    'memory_samples': len(memory_values)
                }
        
        rate_info = {}
        if self.processing_rates:
            rates = [rate[1] for rate in self.processing_rates]
            rate_info = {
                'peak_rate': max(rates),
                'avg_rate': sum(rates) / len(rates),
                'min_rate': min(rates)
            }
        
        return {
            'elapsed_seconds': elapsed,
            'processing_rate': rate,
            'items_per_second': rate,
            'error_count': self.error_count,
            'warning_count': self.warning_count,
            'memory_tracking': memory_info,
            'rate_tracking': rate_info,
            'total_processed': self.processed_items
        }


class BatchValidationEngine:
    """Enhanced batch validation with comprehensive reporting."""
    
    VALIDATION_RULES = {
        'employee': {
            'required_fields': ['last_name', 'first_name'],
            'unique_fields': ['employee_number', 'national_id', 'email'],
            'date_fields': ['birth_date', 'hire_date', 'termination_date'],
            'numeric_fields': ['children_count'],
            'email_fields': ['email'],
            'phone_fields': ['phone'],
            'boolean_fields': ['is_active']
        },
        'payroll_element': {
            'required_fields': ['label'],
            'unique_fields': ['code'],
            'choice_fields': {'type': ['G', 'D']},
            'numeric_fields': ['rate', 'amount', 'ceiling'],
            'boolean_fields': ['has_ceiling', 'is_cumulative']
        },
        'attendance': {
            'required_fields': ['employee_number', 'date'],
            'date_fields': ['date'],
            'numeric_fields': ['hours_worked', 'regular_hours', 'overtime_hours']
        }
    }
    
    @classmethod
    def validate_batch_comprehensive(cls, data_list: List[Dict[str, Any]], 
                                   data_type: str, 
                                   progress_callback: Optional[Callable] = None) -> Dict[str, Any]:
        """
        Perform comprehensive batch validation with detailed reporting.
        
        Args:
            data_list: List of data records to validate
            data_type: Type of data being validated
            progress_callback: Optional progress callback
            
        Returns:
            Comprehensive validation summary
        """
        total_records = len(data_list)
        if total_records == 0:
            return cls._empty_validation_result()
        
        # Initialize tracking
        progress = EnhancedProgressTracker(total_records, progress_callback) if progress_callback else None
        
        valid_records = 0
        error_records = 0
        warning_records = 0
        errors = []
        warnings = []
        
        # Track duplicates and statistics
        duplicate_tracker = {}
        field_statistics = {}
        business_rule_violations = []
        
        # Get validation rules
        rules = cls.VALIDATION_RULES.get(data_type, {})
        unique_fields = rules.get('unique_fields', [])
        
        # First pass: individual record validation
        for idx, record in enumerate(data_list):
            row_errors = []
            row_warnings = []
            
            # Individual record validation
            record_valid, record_errors = cls._validate_single_record(record, data_type, rules)
            row_errors.extend(record_errors)
            
            # Collect field statistics
            cls._update_field_statistics(record, field_statistics)
            
            # Track for duplicate detection
            cls._track_for_duplicates(record, unique_fields, duplicate_tracker, idx)
            
            # Business rule validation
            business_violations = cls._validate_business_rules(record, data_type, idx)
            business_rule_violations.extend(business_violations)
            row_errors.extend([v['error'] for v in business_violations])
            
            # Categorize result
            if row_errors:
                error_records += 1
                errors.append({
                    'row': idx + 2,  # +2 for 1-based indexing and header
                    'errors': row_errors,
                    'data': record
                })
            elif row_warnings:
                warning_records += 1
                warnings.append({
                    'row': idx + 2,
                    'warnings': row_warnings,
                    'data': record
                })
            else:
                valid_records += 1
            
            if progress:
                progress.update(1, f"Validating record {idx + 1}", 
                              error=bool(row_errors), warning=bool(row_warnings))
        
        # Second pass: cross-record validation (duplicates)
        duplicate_errors = cls._detect_duplicates(duplicate_tracker, errors)
        
        if progress:
            progress.complete("Validation completed")
        
        return {
            'total_records': total_records,
            'valid_records': valid_records,
            'error_records': error_records,
            'warning_records': warning_records,
            'errors': errors,
            'warnings': warnings,
            'duplicate_errors': duplicate_errors,
            'duplicate_summary': {field: len(values) for field, values in duplicate_tracker.items()},
            'field_statistics': field_statistics,
            'business_rule_violations': business_rule_violations,
            'validation_rules_applied': rules,
            'validation_timestamp': datetime.now()
        }
    
    @classmethod
    def _empty_validation_result(cls) -> Dict[str, Any]:
        """Return empty validation result."""
        return {
            'total_records': 0,
            'valid_records': 0,
            'error_records': 0,
            'warning_records': 0,
            'errors': [],
            'warnings': [],
            'duplicate_errors': [],
            'duplicate_summary': {},
            'field_statistics': {},
            'business_rule_violations': [],
            'validation_rules_applied': {},
            'validation_timestamp': datetime.now()
        }
    
    @classmethod
    def _validate_single_record(cls, record: Dict[str, Any], data_type: str, rules: Dict) -> Tuple[bool, List[str]]:
        """Validate a single record against rules."""
        errors = []
        
        # Check required fields
        for field in rules.get('required_fields', []):
            if not record.get(field):
                errors.append(f"Missing required field: {field}")
        
        # Validate data types and formats
        # (This would include the existing validation logic from DataValidator)
        
        return len(errors) == 0, errors
    
    @classmethod
    def _update_field_statistics(cls, record: Dict[str, Any], statistics: Dict):
        """Update field statistics for reporting."""
        for field, value in record.items():
            if field not in statistics:
                statistics[field] = {
                    'non_empty_count': 0,
                    'empty_count': 0,
                    'unique_values': set(),
                    'data_types': set()
                }
            
            if value is not None and str(value).strip():
                statistics[field]['non_empty_count'] += 1
                statistics[field]['unique_values'].add(str(value))
                statistics[field]['data_types'].add(type(value).__name__)
            else:
                statistics[field]['empty_count'] += 1
    
    @classmethod
    def _track_for_duplicates(cls, record: Dict[str, Any], unique_fields: List[str], 
                            tracker: Dict, row_idx: int):
        """Track record for duplicate detection."""
        for field in unique_fields:
            if record.get(field):
                field_value = str(record[field]).strip().lower()
                if field_value:
                    if field not in tracker:
                        tracker[field] = {}
                    
                    if field_value not in tracker[field]:
                        tracker[field][field_value] = []
                    tracker[field][field_value].append(row_idx)
    
    @classmethod
    def _detect_duplicates(cls, tracker: Dict, existing_errors: List[Dict]) -> List[Dict]:
        """Detect and report duplicates."""
        duplicate_errors = []
        
        for field, values in tracker.items():
            for value, row_indices in values.items():
                if len(row_indices) > 1:
                    duplicate_errors.append({
                        'field': field,
                        'value': value,
                        'rows': [idx + 2 for idx in row_indices],  # +2 for 1-based + header
                        'count': len(row_indices)
                    })
        
        return duplicate_errors
    
    @classmethod
    def _validate_business_rules(cls, record: Dict[str, Any], data_type: str, row_idx: int) -> List[Dict]:
        """Validate business-specific rules."""
        violations = []
        
        if data_type == 'employee':
            # Age validation
            if record.get('birth_date') and record.get('hire_date'):
                try:
                    # This would include age validation logic
                    pass
                except Exception as e:
                    violations.append({
                        'row': row_idx + 2,
                        'rule': 'age_validation',
                        'error': f"Age validation failed: {str(e)}"
                    })
        
        return violations


class ChunkedFileProcessor:
    """Processor for handling very large files with chunked processing."""
    
    def __init__(self, chunk_size: int = 1000, memory_threshold: float = 85.0):
        self.chunk_size = chunk_size
        self.memory_threshold = memory_threshold
        self.memory_optimizer = MemoryOptimizer()
        self.rollback_manager = RollbackManager()
    
    def process_large_file_chunked(self, file_path: str, data_type: str,
                                  process_chunk_func: Callable,
                                  progress_callback: Optional[Callable] = None) -> Dict[str, Any]:
        """
        Process large file in chunks with memory management.
        
        Args:
            file_path: Path to the file to process
            data_type: Type of data being processed
            process_chunk_func: Function to process each chunk
            progress_callback: Optional progress callback
            
        Returns:
            Comprehensive processing results
        """
        start_time = datetime.now()
        total_processed = 0
        total_errors = 0
        all_errors = []
        all_warnings = []
        
        try:
            # Determine optimal chunk size based on file size
            file_size_mb = os.path.getsize(file_path) / (1024 * 1024)
            optimal_chunk_size = self.memory_optimizer.get_optimal_chunk_size(file_size_mb)
            actual_chunk_size = min(self.chunk_size, optimal_chunk_size)
            
            print(f"Processing large file ({file_size_mb:.1f}MB) with chunk size: {actual_chunk_size}")
            
            # Process file in chunks
            chunk_number = 0
            for chunk_data in self._read_file_in_chunks(file_path, actual_chunk_size):
                chunk_number += 1
                
                # Check memory usage before processing chunk
                if self.memory_optimizer.should_pause_processing(self.memory_threshold):
                    print(f"High memory usage detected, pausing for GC...")
                    time.sleep(2)  # Allow garbage collection
                
                # Process chunk with rollback protection
                with self.rollback_manager.transaction_savepoint(f"chunk_{chunk_number}"):
                    chunk_result = process_chunk_func(chunk_data, data_type, progress_callback)
                    
                    total_processed += chunk_result.get('processed', 0)
                    total_errors += chunk_result.get('errors', 0)
                    all_errors.extend(chunk_result.get('error_list', []))
                    all_warnings.extend(chunk_result.get('warning_list', []))
                
                # Memory monitoring
                memory_info = self.memory_optimizer.monitor_memory()
                if memory_info.get('system_percent', 0) > 90:
                    print(f"Critical memory usage: {memory_info['system_percent']:.1f}%")
            
            execution_time = (datetime.now() - start_time).total_seconds()
            
            return {
                'success': total_errors == 0,
                'total_processed': total_processed,
                'total_errors': total_errors,
                'errors': all_errors,
                'warnings': all_warnings,
                'chunks_processed': chunk_number,
                'execution_time': execution_time,
                'rollback_summary': self.rollback_manager.get_rollback_summary()
            }
            
        except Exception as e:
            return {
                'success': False,
                'error': str(e),
                'traceback': traceback.format_exc(),
                'total_processed': total_processed,
                'execution_time': (datetime.now() - start_time).total_seconds()
            }
    
    def _read_file_in_chunks(self, file_path: str, chunk_size: int) -> Iterator[List[Dict]]:
        """
        Read file in chunks. This is a placeholder that would need to be
        implemented based on the specific file format.
        """
        # This would be implemented to read Excel/CSV files in chunks
        # For now, this is a placeholder
        yield []


# Integration utility functions
def create_enhanced_import_manager():
    """Create an import manager with all enhanced features."""
    class EnhancedImportExportManager:
        def __init__(self):
            self.memory_optimizer = MemoryOptimizer()
            self.batch_validator = BatchValidationEngine()
            self.chunked_processor = ChunkedFileProcessor()
        
        def validate_file_comprehensive(self, file_path: str, data_type: str) -> Dict[str, Any]:
            """Perform comprehensive file validation."""
            # Implementation would integrate with existing file reading
            pass
        
        def import_with_enhancements(self, file_path: str, data_type: str, 
                                   chunk_processing: bool = False) -> Dict[str, Any]:
            """Import with all enhancements enabled."""
            # Implementation would integrate enhanced features
            pass
    
    return EnhancedImportExportManager()


def get_system_performance_report() -> Dict[str, Any]:
    """Get comprehensive system performance report."""
    memory_info = MemoryOptimizer.monitor_memory()
    
    report = {
        'timestamp': datetime.now(),
        'memory_usage': memory_info,
        'psutil_available': PSUTIL_AVAILABLE,
        'recommendations': []
    }
    
    # Add recommendations based on system state
    if memory_info.get('system_percent', 0) > 80:
        report['recommendations'].append("High system memory usage detected - consider smaller batch sizes")
    
    if memory_info.get('available_mb', 0) < 500:
        report['recommendations'].append("Low available memory - enable chunked processing for large files")
    
    return report


if __name__ == "__main__":
    # Example usage
    print("Enhanced Import/Export Utilities")
    print("-" * 40)
    
    # System performance check
    performance = get_system_performance_report()
    print(f"System Memory: {performance['memory_usage'].get('system_percent', 0):.1f}%")
    print(f"Available Memory: {performance['memory_usage'].get('available_mb', 0):.1f}MB")
    
    if performance['recommendations']:
        print("\nRecommendations:")
        for rec in performance['recommendations']:
            print(f"- {rec}")