"""
Comprehensive System Configuration Management for Mauritanian Payroll System

This module provides centralized configuration management with capabilities for:
1. System Settings Management - Centralized access to all payroll parameters
2. Parameter Validation - Business rule validation for configuration values
3. Configuration Backup/Restore - Backup and restore of system configurations
4. Environment Management - Different configs for dev/test/prod environments
5. Hot Configuration Reload - Runtime configuration updates without restart

Features:
- Centralized access to SystemParameters model
- Configuration validation with business rules
- Default value management for missing configurations
- Configuration change tracking and audit
- Environment-specific configuration override
- Configuration backup and restore utilities
- Performance optimization with caching
- Thread-safe configuration access
- Configuration versioning and rollback
- Integration with Django settings and utilities
"""

import os
import json
import logging
import threading
from datetime import datetime, date, timedelta
from decimal import Decimal
from typing import Dict, Any, Optional, List, Union, Tuple
from pathlib import Path
from django.conf import settings
from django.core.cache import cache
from django.core.exceptions import ValidationError, ObjectDoesNotExist
from django.db import transaction
from django.utils import timezone

# Import project-specific utilities
from .validators import ValidationResult, EmployeeDataValidator, PayrollDataValidator
from .text_utils import ValidationUtils, LocalizationUtils
from .security import SecurityManager

logger = logging.getLogger(__name__)


class ConfigurationError(Exception):
    """Custom exception for configuration-related errors"""
    def __init__(self, message: str, config_key: str = None, error_code: str = None):
        self.message = message
        self.config_key = config_key
        self.error_code = error_code
        super().__init__(self.message)


class ConfigurationCache:
    """Thread-safe configuration caching with TTL and invalidation"""
    
    def __init__(self, default_ttl: int = 300):  # 5 minutes default TTL
        self.default_ttl = default_ttl
        self._cache_lock = threading.RLock()
        self._cache_data = {}
        self._cache_timestamps = {}
        self._cache_keys = set()
    
    def get(self, key: str, default=None) -> Any:
        """Get cached value with TTL check"""
        with self._cache_lock:
            if key not in self._cache_data:
                return default
            
            # Check TTL
            if self._is_expired(key):
                self._remove_key(key)
                return default
            
            return self._cache_data[key]
    
    def set(self, key: str, value: Any, ttl: Optional[int] = None) -> None:
        """Set cached value with TTL"""
        with self._cache_lock:
            self._cache_data[key] = value
            self._cache_timestamps[key] = datetime.now()
            self._cache_keys.add(key)
            
            # Store TTL if specified
            if ttl is not None:
                self._cache_data[f"{key}_ttl"] = ttl
    
    def invalidate(self, key: str) -> None:
        """Invalidate specific cache key"""
        with self._cache_lock:
            self._remove_key(key)
    
    def clear(self) -> None:
        """Clear all cached data"""
        with self._cache_lock:
            self._cache_data.clear()
            self._cache_timestamps.clear()
            self._cache_keys.clear()
    
    def get_stats(self) -> Dict[str, Any]:
        """Get cache statistics"""
        with self._cache_lock:
            return {
                'total_keys': len(self._cache_keys),
                'memory_usage_kb': len(str(self._cache_data)) / 1024,
                'oldest_entry': min(self._cache_timestamps.values()) if self._cache_timestamps else None,
                'newest_entry': max(self._cache_timestamps.values()) if self._cache_timestamps else None
            }
    
    def _is_expired(self, key: str) -> bool:
        """Check if cache entry is expired"""
        if key not in self._cache_timestamps:
            return True
        
        ttl = self._cache_data.get(f"{key}_ttl", self.default_ttl)
        elapsed = (datetime.now() - self._cache_timestamps[key]).seconds
        return elapsed > ttl
    
    def _remove_key(self, key: str) -> None:
        """Remove key from all cache structures"""
        self._cache_data.pop(key, None)
        self._cache_data.pop(f"{key}_ttl", None)
        self._cache_timestamps.pop(key, None)
        self._cache_keys.discard(key)


class ConfigurationValidator:
    """Validates configuration values against business rules"""
    
    # Configuration validation rules
    VALIDATION_RULES = {
        'company_name': {
            'required': True,
            'min_length': 2,
            'max_length': 300
        },
        'minimum_wage': {
            'type': 'decimal',
            'min_value': Decimal('0.01'),
            'max_value': Decimal('1000000.00')
        },
        'default_working_days': {
            'type': 'decimal',
            'min_value': Decimal('1.0'),
            'max_value': Decimal('31.0')
        },
        'contract_expiry_alert_days': {
            'type': 'int',
            'min_value': 1,
            'max_value': 365
        },
        'current_period': {
            'type': 'date',
            'required': True
        },
        'next_period': {
            'type': 'date',
            'required': True,
            'after': 'current_period'
        },
        'closure_period': {
            'type': 'date',
            'after_or_equal': 'current_period'
        },
        'currency': {
            'choices': ['MRU', 'EUR', 'USD', 'MRO']
        },
        'email': {
            'type': 'email'
        },
        'cnss_number': {
            'min_length': 5,
            'max_length': 20
        },
        'cnam_number': {
            'min_length': 5,
            'max_length': 20
        },
        'its_number': {
            'min_length': 5,
            'max_length': 20
        }
    }
    
    @classmethod
    def validate_config(cls, key: str, value: Any, all_config: Dict[str, Any] = None) -> ValidationResult:
        """
        Validate a configuration value against business rules
        
        Args:
            key: Configuration key
            value: Configuration value to validate
            all_config: All configuration for cross-field validation
            
        Returns:
            ValidationResult with validation outcome
        """
        result = ValidationResult()
        rules = cls.VALIDATION_RULES.get(key, {})
        
        if not rules:
            # No specific rules, assume valid
            result.cleaned_data[key] = value
            return result
        
        # Required field validation
        if rules.get('required', False) and (value is None or value == ''):
            result.add_error(
                key,
                f"Configuration '{key}' is required",
                'REQUIRED_CONFIG'
            )
            return result
        
        # Skip further validation if value is None/empty and not required
        if value is None or value == '':
            result.cleaned_data[key] = value
            return result
        
        # Type validation
        field_type = rules.get('type', 'string')
        
        if field_type == 'decimal':
            try:
                decimal_value = Decimal(str(value))
                
                # Range validation for decimals
                if 'min_value' in rules and decimal_value < rules['min_value']:
                    result.add_error(
                        key,
                        f"Value {decimal_value} is below minimum {rules['min_value']}",
                        'VALUE_TOO_LOW'
                    )
                
                if 'max_value' in rules and decimal_value > rules['max_value']:
                    result.add_error(
                        key,
                        f"Value {decimal_value} exceeds maximum {rules['max_value']}",
                        'VALUE_TOO_HIGH'
                    )
                
                if result.is_valid:
                    result.cleaned_data[key] = decimal_value
                    
            except (ValueError, TypeError) as e:
                result.add_error(
                    key,
                    f"Invalid decimal value: {str(e)}",
                    'INVALID_DECIMAL'
                )
        
        elif field_type == 'int':
            try:
                int_value = int(value)
                
                # Range validation for integers
                if 'min_value' in rules and int_value < rules['min_value']:
                    result.add_error(
                        key,
                        f"Value {int_value} is below minimum {rules['min_value']}",
                        'VALUE_TOO_LOW'
                    )
                
                if 'max_value' in rules and int_value > rules['max_value']:
                    result.add_error(
                        key,
                        f"Value {int_value} exceeds maximum {rules['max_value']}",
                        'VALUE_TOO_HIGH'
                    )
                
                if result.is_valid:
                    result.cleaned_data[key] = int_value
                    
            except (ValueError, TypeError) as e:
                result.add_error(
                    key,
                    f"Invalid integer value: {str(e)}",
                    'INVALID_INTEGER'
                )
        
        elif field_type == 'date':
            try:
                if isinstance(value, str):
                    # Try multiple date formats
                    for fmt in ['%Y-%m-%d', '%d/%m/%Y', '%d-%m-%Y']:
                        try:
                            date_value = datetime.strptime(value, fmt).date()
                            break
                        except ValueError:
                            continue
                    else:
                        raise ValueError("No matching date format")
                elif isinstance(value, datetime):
                    date_value = value.date()
                elif isinstance(value, date):
                    date_value = value
                else:
                    raise ValueError("Invalid date type")
                
                # Date relationship validation
                if all_config and 'after' in rules:
                    compare_key = rules['after']
                    compare_date = all_config.get(compare_key)
                    if compare_date and date_value <= compare_date:
                        result.add_error(
                            key,
                            f"Date must be after {compare_key}",
                            'DATE_SEQUENCE_ERROR'
                        )
                
                if all_config and 'after_or_equal' in rules:
                    compare_key = rules['after_or_equal']
                    compare_date = all_config.get(compare_key)
                    if compare_date and date_value < compare_date:
                        result.add_error(
                            key,
                            f"Date must be on or after {compare_key}",
                            'DATE_SEQUENCE_ERROR'
                        )
                
                if result.is_valid:
                    result.cleaned_data[key] = date_value
                    
            except (ValueError, TypeError) as e:
                result.add_error(
                    key,
                    f"Invalid date value: {str(e)}",
                    'INVALID_DATE'
                )
        
        elif field_type == 'email':
            email_result = EmployeeDataValidator._validate_email(str(value))
            if email_result['valid']:
                result.cleaned_data[key] = email_result['email']
            else:
                result.add_error(key, email_result['error'], 'INVALID_EMAIL')
        
        else:  # string type
            str_value = str(value)
            
            # Length validation
            if 'min_length' in rules and len(str_value) < rules['min_length']:
                result.add_error(
                    key,
                    f"Value too short (minimum {rules['min_length']} characters)",
                    'VALUE_TOO_SHORT'
                )
            
            if 'max_length' in rules and len(str_value) > rules['max_length']:
                result.add_error(
                    key,
                    f"Value too long (maximum {rules['max_length']} characters)",
                    'VALUE_TOO_LONG'
                )
            
            # Choice validation
            if 'choices' in rules and str_value not in rules['choices']:
                result.add_error(
                    key,
                    f"Invalid choice. Must be one of: {', '.join(rules['choices'])}",
                    'INVALID_CHOICE'
                )
            
            if result.is_valid:
                result.cleaned_data[key] = str_value
        
        return result


class ConfigurationBackup:
    """Manages configuration backup and restore operations"""
    
    def __init__(self, backup_dir: Optional[str] = None):
        self.backup_dir = Path(backup_dir or settings.BASE_DIR / 'config_backups')
        self.backup_dir.mkdir(exist_ok=True)
        
        # Ensure security manager is available
        self.security_manager = SecurityManager()
    
    def create_backup(self, backup_name: Optional[str] = None) -> str:
        """
        Create a backup of current configuration
        
        Args:
            backup_name: Optional custom backup name
            
        Returns:
            Path to backup file
        """
        if backup_name is None:
            backup_name = f"config_backup_{datetime.now().strftime('%Y%m%d_%H%M%S')}"
        
        backup_file = self.backup_dir / f"{backup_name}.json"
        
        try:
            # Import here to avoid circular imports
            from core.models.system_config import SystemParameters
            
            # Get current configuration
            try:
                system_params = SystemParameters.objects.first()
                if not system_params:
                    raise ConfigurationError("No system parameters found to backup")
                
                # Convert model to dictionary
                config_data = {}
                for field in system_params._meta.fields:
                    field_name = field.name
                    field_value = getattr(system_params, field_name)
                    
                    # Handle special field types for JSON serialization
                    if isinstance(field_value, (date, datetime)):
                        config_data[field_name] = field_value.isoformat()
                    elif isinstance(field_value, Decimal):
                        config_data[field_name] = str(field_value)
                    elif isinstance(field_value, bytes):
                        # Skip binary data like logos
                        config_data[field_name] = None
                    else:
                        config_data[field_name] = field_value
                
                # Add metadata
                backup_metadata = {
                    'backup_date': datetime.now().isoformat(),
                    'backup_name': backup_name,
                    'django_version': settings.DEBUG,  # Use as proxy for environment
                    'config_version': '1.0',
                    'total_fields': len(config_data)
                }
                
                full_backup = {
                    'metadata': backup_metadata,
                    'configuration': config_data
                }
                
                # Write backup file
                with open(backup_file, 'w', encoding='utf-8') as f:
                    json.dump(full_backup, f, indent=2, ensure_ascii=False)
                
                logger.info(f"Configuration backup created: {backup_file}")
                return str(backup_file)
                
            except Exception as e:
                logger.error(f"Failed to create configuration backup: {str(e)}")
                raise ConfigurationError(f"Backup creation failed: {str(e)}")
                
        except Exception as e:
            logger.error(f"Configuration backup error: {str(e)}")
            raise ConfigurationError(f"Backup operation failed: {str(e)}")
    
    def restore_backup(self, backup_file: str, validate_before_restore: bool = True) -> bool:
        """
        Restore configuration from backup file
        
        Args:
            backup_file: Path to backup file
            validate_before_restore: Whether to validate before restoring
            
        Returns:
            True if restore was successful
        """
        try:
            backup_path = Path(backup_file)
            if not backup_path.exists():
                raise ConfigurationError(f"Backup file not found: {backup_file}")
            
            # Load backup data
            with open(backup_path, 'r', encoding='utf-8') as f:
                backup_data = json.load(f)
            
            if 'configuration' not in backup_data:
                raise ConfigurationError("Invalid backup file format")
            
            config_data = backup_data['configuration']
            
            # Validate configuration if requested
            if validate_before_restore:
                validator = ConfigurationValidator()
                validation_errors = []
                
                for key, value in config_data.items():
                    if key == 'id':  # Skip ID field
                        continue
                    
                    validation_result = validator.validate_config(key, value, config_data)
                    if not validation_result.is_valid:
                        validation_errors.extend(validation_result.errors)
                
                if validation_errors:
                    error_msg = "Validation errors in backup: " + "; ".join([
                        f"{err['field']}: {err['message']}" for err in validation_errors
                    ])
                    raise ConfigurationError(error_msg)
            
            # Import here to avoid circular imports
            from core.models.system_config import SystemParameters
            
            # Restore configuration
            with transaction.atomic():
                try:
                    system_params = SystemParameters.objects.first()
                    if not system_params:
                        system_params = SystemParameters()
                    
                    # Update fields from backup
                    for field_name, field_value in config_data.items():
                        if field_name == 'id':  # Skip ID
                            continue
                        
                        # Handle field type conversion
                        field = system_params._meta.get_field(field_name)
                        
                        if hasattr(field, 'to_python') and field_value is not None:
                            try:
                                converted_value = field.to_python(field_value)
                                setattr(system_params, field_name, converted_value)
                            except ValidationError:
                                # Skip invalid field values
                                logger.warning(f"Skipped invalid field value: {field_name}={field_value}")
                                continue
                        else:
                            setattr(system_params, field_name, field_value)
                    
                    # Save restored configuration
                    system_params.full_clean()
                    system_params.save()
                    
                    logger.info(f"Configuration restored from backup: {backup_file}")
                    return True
                    
                except Exception as e:
                    logger.error(f"Failed to restore configuration: {str(e)}")
                    raise ConfigurationError(f"Restore failed: {str(e)}")
                    
        except Exception as e:
            logger.error(f"Configuration restore error: {str(e)}")
            raise ConfigurationError(f"Restore operation failed: {str(e)}")
    
    def list_backups(self) -> List[Dict[str, Any]]:
        """
        List available backup files with metadata
        
        Returns:
            List of backup information dictionaries
        """
        backups = []
        
        for backup_file in self.backup_dir.glob("*.json"):
            try:
                with open(backup_file, 'r', encoding='utf-8') as f:
                    backup_data = json.load(f)
                
                metadata = backup_data.get('metadata', {})
                file_stats = backup_file.stat()
                
                backup_info = {
                    'filename': backup_file.name,
                    'full_path': str(backup_file),
                    'backup_name': metadata.get('backup_name', backup_file.stem),
                    'backup_date': metadata.get('backup_date'),
                    'file_size': file_stats.st_size,
                    'file_modified': datetime.fromtimestamp(file_stats.st_mtime).isoformat(),
                    'config_version': metadata.get('config_version'),
                    'total_fields': metadata.get('total_fields', 0)
                }
                
                backups.append(backup_info)
                
            except Exception as e:
                logger.warning(f"Failed to read backup metadata from {backup_file}: {str(e)}")
                continue
        
        # Sort by backup date (newest first)
        backups.sort(key=lambda x: x.get('backup_date', ''), reverse=True)
        return backups


class ConfigurationManager:
    """
    Comprehensive Configuration Manager for Mauritanian Payroll System
    
    Provides centralized configuration management with caching, validation,
    backup/restore, and environment-specific overrides.
    """
    
    _instance = None
    _lock = threading.Lock()
    
    # Configuration defaults
    DEFAULT_CONFIG = {
        'currency': 'MRU',
        'default_working_days': Decimal('26.00'),
        'minimum_wage': Decimal('30000.00'),  # 30,000 MRU
        'contract_expiry_alert_days': 30,
        'auto_meal_allowance': False,
        'auto_seniority': False,
        'auto_housing_allowance': False,
        'smtp_port': '587',
        'smtp_tls_enabled': True,
        'apply_compensatory_allowance': True,
        'deduct_cnss_from_its': True,
        'deduct_cnam_from_its': True
    }
    
    def __new__(cls):
        """Singleton pattern implementation"""
        if cls._instance is None:
            with cls._lock:
                if cls._instance is None:
                    cls._instance = super().__new__(cls)
        return cls._instance
    
    def __init__(self):
        if hasattr(self, '_initialized'):
            return
        
        self._initialized = True
        self.cache = ConfigurationCache()
        self.validator = ConfigurationValidator()
        self.backup_manager = ConfigurationBackup()
        self._config_lock = threading.RLock()
        self._change_listeners = []
        self._environment = self._detect_environment()
        
        logger.info(f"Configuration Manager initialized for environment: {self._environment}")
    
    def get_config(self, key: str, default: Any = None, use_cache: bool = True) -> Any:
        """
        Get configuration value with caching and fallback to defaults
        
        Args:
            key: Configuration key
            default: Default value if key not found
            use_cache: Whether to use caching
            
        Returns:
            Configuration value
        """
        try:
            # Check cache first
            if use_cache:
                cache_key = f"config_{key}_{self._environment}"
                cached_value = self.cache.get(cache_key)
                if cached_value is not None:
                    return cached_value
            
            # Get from database
            with self._config_lock:
                from core.models.system_config import SystemParameters
                
                try:
                    system_params = SystemParameters.objects.first()
                    if system_params and hasattr(system_params, key):
                        value = getattr(system_params, key)
                        
                        # Cache the value
                        if use_cache and value is not None:
                            self.cache.set(f"config_{key}_{self._environment}", value)
                        
                        return value if value is not None else default
                    
                except ObjectDoesNotExist:
                    pass
            
            # Fallback to default configuration
            if key in self.DEFAULT_CONFIG:
                return self.DEFAULT_CONFIG[key]
            
            return default
            
        except Exception as e:
            logger.error(f"Error getting configuration '{key}': {str(e)}")
            return default
    
    def set_config(self, key: str, value: Any, validate: bool = True, 
                   notify_listeners: bool = True) -> bool:
        """
        Set configuration value with validation and change notification
        
        Args:
            key: Configuration key
            value: Configuration value
            validate: Whether to validate the value
            notify_listeners: Whether to notify change listeners
            
        Returns:
            True if configuration was set successfully
        """
        try:
            with self._config_lock:
                # Validate configuration value
                if validate:
                    # Get current config for cross-field validation
                    current_config = self._get_all_config_dict()
                    current_config[key] = value
                    
                    validation_result = self.validator.validate_config(key, value, current_config)
                    if not validation_result.is_valid:
                        error_messages = [err['message'] for err in validation_result.errors]
                        raise ConfigurationError(
                            f"Validation failed for '{key}': {'; '.join(error_messages)}",
                            key,
                            'VALIDATION_FAILED'
                        )
                    
                    # Use cleaned value from validation
                    if key in validation_result.cleaned_data:
                        value = validation_result.cleaned_data[key]
                
                # Update database
                from core.models.system_config import SystemParameters
                
                system_params = SystemParameters.objects.first()
                if not system_params:
                    system_params = SystemParameters()
                
                # Store old value for change notification
                old_value = getattr(system_params, key, None) if hasattr(system_params, key) else None
                
                # Set new value
                if hasattr(system_params, key):
                    setattr(system_params, key, value)
                    system_params.save()
                    
                    # Invalidate cache
                    cache_key = f"config_{key}_{self._environment}"
                    self.cache.invalidate(cache_key)
                    
                    # Notify change listeners
                    if notify_listeners and old_value != value:
                        self._notify_config_change(key, old_value, value)
                    
                    logger.info(f"Configuration updated: {key} = {value}")
                    return True
                else:
                    logger.warning(f"Configuration key '{key}' not found in SystemParameters model")
                    return False
                    
        except Exception as e:
            logger.error(f"Error setting configuration '{key}': {str(e)}")
            raise ConfigurationError(f"Failed to set configuration '{key}': {str(e)}", key)
    
    def get_all_config(self, use_cache: bool = True) -> Dict[str, Any]:
        """
        Get all configuration values as dictionary
        
        Args:
            use_cache: Whether to use caching
            
        Returns:
            Dictionary of all configuration values
        """
        try:
            # Check cache first
            if use_cache:
                cache_key = f"all_config_{self._environment}"
                cached_config = self.cache.get(cache_key)
                if cached_config is not None:
                    return cached_config
            
            config = self._get_all_config_dict()
            
            # Cache the result
            if use_cache:
                self.cache.set(f"all_config_{self._environment}", config)
            
            return config
            
        except Exception as e:
            logger.error(f"Error getting all configuration: {str(e)}")
            return self.DEFAULT_CONFIG.copy()
    
    def validate_all_config(self) -> ValidationResult:
        """
        Validate all configuration values
        
        Returns:
            ValidationResult with overall validation status
        """
        result = ValidationResult()
        
        try:
            config = self._get_all_config_dict()
            
            for key, value in config.items():
                if key == 'id':  # Skip ID field
                    continue
                
                field_result = self.validator.validate_config(key, value, config)
                
                if not field_result.is_valid:
                    result.is_valid = False
                    result.errors.extend(field_result.errors)
                
                result.warnings.extend(field_result.warnings)
                result.cleaned_data.update(field_result.cleaned_data)
            
            return result
            
        except Exception as e:
            result.add_error(
                'validation_error',
                f"Configuration validation failed: {str(e)}",
                'VALIDATION_ERROR'
            )
            return result
    
    def reload_config(self) -> bool:
        """
        Reload configuration from database and clear cache
        
        Returns:
            True if reload was successful
        """
        try:
            with self._config_lock:
                # Clear cache
                self.cache.clear()
                
                # Force reload from database
                self._get_all_config_dict()
                
                logger.info("Configuration reloaded successfully")
                return True
                
        except Exception as e:
            logger.error(f"Error reloading configuration: {str(e)}")
            return False
    
    def backup_config(self, backup_name: Optional[str] = None) -> str:
        """
        Create backup of current configuration
        
        Args:
            backup_name: Optional backup name
            
        Returns:
            Path to backup file
        """
        return self.backup_manager.create_backup(backup_name)
    
    def restore_config(self, backup_file: str, validate: bool = True) -> bool:
        """
        Restore configuration from backup
        
        Args:
            backup_file: Path to backup file
            validate: Whether to validate before restoring
            
        Returns:
            True if restore was successful
        """
        try:
            success = self.backup_manager.restore_backup(backup_file, validate)
            
            if success:
                # Clear cache after restore
                self.cache.clear()
                
                # Notify listeners of configuration change
                self._notify_config_change('*', None, None, change_type='restore')
            
            return success
            
        except Exception as e:
            logger.error(f"Error restoring configuration: {str(e)}")
            raise ConfigurationError(f"Configuration restore failed: {str(e)}")
    
    def list_backups(self) -> List[Dict[str, Any]]:
        """
        List available configuration backups
        
        Returns:
            List of backup information
        """
        return self.backup_manager.list_backups()
    
    def get_environment_config(self, key: str, environment: str = None) -> Any:
        """
        Get configuration value for specific environment
        
        Args:
            key: Configuration key
            environment: Environment name (defaults to current)
            
        Returns:
            Environment-specific configuration value
        """
        env = environment or self._environment
        
        # Check for environment-specific override in Django settings
        env_key = f"{key.upper()}_{env.upper()}"
        if hasattr(settings, env_key):
            return getattr(settings, env_key)
        
        # Fallback to regular configuration
        return self.get_config(key)
    
    def add_change_listener(self, callback) -> None:
        """
        Add listener for configuration changes
        
        Args:
            callback: Function to call when configuration changes
        """
        if callback not in self._change_listeners:
            self._change_listeners.append(callback)
    
    def remove_change_listener(self, callback) -> None:
        """
        Remove configuration change listener
        
        Args:
            callback: Callback function to remove
        """
        if callback in self._change_listeners:
            self._change_listeners.remove(callback)
    
    def get_cache_stats(self) -> Dict[str, Any]:
        """
        Get configuration cache statistics
        
        Returns:
            Cache statistics dictionary
        """
        return self.cache.get_stats()
    
    def clear_cache(self) -> None:
        """Clear configuration cache"""
        self.cache.clear()
        logger.info("Configuration cache cleared")
    
    def export_config(self, export_path: str, include_sensitive: bool = False) -> bool:
        """
        Export configuration to JSON file
        
        Args:
            export_path: Path for export file
            include_sensitive: Whether to include sensitive data
            
        Returns:
            True if export was successful
        """
        try:
            config = self._get_all_config_dict()
            
            # Remove sensitive data if requested
            if not include_sensitive:
                sensitive_fields = [
                    'mail_password', 'license_key', 'smtp_password',
                    'database_password', 'secret_key'
                ]
                for field in sensitive_fields:
                    if field in config:
                        config[field] = '[HIDDEN]'
            
            # Add export metadata
            export_data = {
                'export_date': datetime.now().isoformat(),
                'environment': self._environment,
                'export_version': '1.0',
                'include_sensitive': include_sensitive,
                'configuration': config
            }
            
            with open(export_path, 'w', encoding='utf-8') as f:
                json.dump(export_data, f, indent=2, ensure_ascii=False, default=str)
            
            logger.info(f"Configuration exported to: {export_path}")
            return True
            
        except Exception as e:
            logger.error(f"Error exporting configuration: {str(e)}")
            return False
    
    def _get_all_config_dict(self) -> Dict[str, Any]:
        """Get all configuration as dictionary from database"""
        try:
            from core.models.system_config import SystemParameters
            
            system_params = SystemParameters.objects.first()
            if not system_params:
                # Return defaults if no configuration exists
                return self.DEFAULT_CONFIG.copy()
            
            config = {}
            for field in system_params._meta.fields:
                field_name = field.name
                field_value = getattr(system_params, field_name)
                config[field_name] = field_value
            
            return config
            
        except Exception as e:
            logger.error(f"Error loading configuration from database: {str(e)}")
            return self.DEFAULT_CONFIG.copy()
    
    def _detect_environment(self) -> str:
        """Detect current environment (dev/test/prod)"""
        if hasattr(settings, 'ENVIRONMENT'):
            return settings.ENVIRONMENT.lower()
        
        if settings.DEBUG:
            return 'development'
        
        # Check for test environment
        import sys
        if 'test' in sys.argv:
            return 'test'
        
        return 'production'
    
    def _notify_config_change(self, key: str, old_value: Any, new_value: Any, 
                            change_type: str = 'update') -> None:
        """Notify all registered listeners of configuration change"""
        for listener in self._change_listeners:
            try:
                listener(key, old_value, new_value, change_type)
            except Exception as e:
                logger.error(f"Error notifying configuration change listener: {str(e)}")


# Singleton instance
config_manager = ConfigurationManager()


# Convenience functions for easy integration
def get_config(key: str, default: Any = None) -> Any:
    """Get configuration value"""
    return config_manager.get_config(key, default)


def set_config(key: str, value: Any, validate: bool = True) -> bool:
    """Set configuration value"""
    return config_manager.set_config(key, value, validate)


def reload_config() -> bool:
    """Reload configuration from database"""
    return config_manager.reload_config()


def validate_config() -> ValidationResult:
    """Validate all configuration values"""
    return config_manager.validate_all_config()


def backup_config(backup_name: str = None) -> str:
    """Create configuration backup"""
    return config_manager.backup_config(backup_name)


def restore_config(backup_file: str, validate: bool = True) -> bool:
    """Restore configuration from backup"""
    return config_manager.restore_config(backup_file, validate)


# Configuration change decorator
def on_config_change(config_key: str = None):
    """
    Decorator to register function as configuration change listener
    
    Args:
        config_key: Specific configuration key to listen for (None for all)
    """
    def decorator(func):
        def listener(key, old_value, new_value, change_type):
            if config_key is None or key == config_key:
                func(key, old_value, new_value, change_type)
        
        config_manager.add_change_listener(listener)
        return func
    
    return decorator