"""
Comprehensive Security Module for Mauritanian Payroll System

This module provides robust security capabilities for the Django payroll system, focusing on:

1. **Authentication Utilities** - Strong password validation, session management, multi-factor auth
2. **Authorization Framework** - Role-based access control, permission checking, resource protection
3. **License Management** - Software licensing validation, user limits, feature restrictions
4. **Security Validation** - Input sanitization, injection prevention, XSS protection
5. **Audit Logging** - Security event tracking, login monitoring, access logging
6. **Session Security** - Token management, session tracking, concurrent session limits
7. **Rate Limiting** - Brute force protection, API throttling, request limiting

Integrates with Django's authentication system while providing Mauritanian-specific
security requirements and comprehensive audit trails for payroll data protection.
"""

import hashlib
import hmac
import secrets
import jwt
import logging
import threading
import time
import re
import ipaddress
from datetime import datetime, timedelta, timezone
from typing import Dict, List, Optional, Union, Tuple, Set
# Removed unused import: Any
from decimal import Decimal
from collections import defaultdict, deque

from django.conf import settings
from django.contrib.auth import get_user_model, authenticate
from django.contrib.auth.models import Permission, Group
from django.contrib.sessions.models import Session
from django.core.cache import cache
from django.core.exceptions import ValidationError, PermissionDenied
from django.db import models, transaction
from django.utils import timezone as django_timezone
from django.http import HttpRequest
from django.contrib.auth.password_validation import validate_password
from django.contrib.auth.hashers import make_password, check_password

from .validators import ValidationResult, DataSanitizer
from .text_utils import TextFormatter

logger = logging.getLogger(__name__)
User = get_user_model()

# Thread-local storage for request context
_thread_local = threading.local()


class SecurityError(Exception):
    """Base exception for security-related errors"""
    def __init__(self, message: str, error_code: str = None, user_id: int = None):
        self.message = message
        self.error_code = error_code
        self.user_id = user_id
        super().__init__(self.message)


class AuthenticationError(SecurityError):
    """Exception for authentication failures"""
    pass


class AuthorizationError(SecurityError):
    """Exception for authorization failures"""
    pass


class LicenseError(SecurityError):
    """Exception for license validation failures"""
    pass


class RateLimitError(SecurityError):
    """Exception for rate limiting violations"""
    pass


class SecurityConfig:
    """Central configuration for security parameters"""
    
    # Password Policy
    PASSWORD_MIN_LENGTH = 8
    PASSWORD_MAX_LENGTH = 128
    PASSWORD_REQUIRE_UPPERCASE = True
    PASSWORD_REQUIRE_LOWERCASE = True
    PASSWORD_REQUIRE_DIGITS = True
    PASSWORD_REQUIRE_SPECIAL = True
    PASSWORD_SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?"
    PASSWORD_HISTORY_COUNT = 5
    PASSWORD_MAX_AGE_DAYS = 90
    PASSWORD_COMPLEXITY_SCORE_MIN = 60
    
    # Account Security
    MAX_LOGIN_ATTEMPTS = 5
    ACCOUNT_LOCKOUT_DURATION = 30  # minutes
    SESSION_TIMEOUT = 60  # minutes
    CONCURRENT_SESSIONS_MAX = 3
    
    # Mauritanian Specific
    NNI_VALIDATION_REQUIRED = True
    PHONE_VALIDATION_REQUIRED = True
    
    # Rate Limiting
    RATE_LIMIT_LOGIN = 10  # attempts per minute
    RATE_LIMIT_API = 100   # requests per minute
    RATE_LIMIT_WINDOW = 60  # seconds
    
    # Audit Settings
    AUDIT_RETENTION_DAYS = 365
    LOG_SECURITY_EVENTS = True
    LOG_DATA_ACCESS = True
    
    # License Management
    LICENSE_CHECK_INTERVAL = 24  # hours
    USER_LIMIT_WARNING_THRESHOLD = 0.9  # 90% of limit
    
    # JWT Settings
    JWT_SECRET_KEY = getattr(settings, 'SECRET_KEY', 'fallback-key')
    JWT_ALGORITHM = 'HS256'
    JWT_EXPIRATION_HOURS = 24
    JWT_REFRESH_EXPIRATION_DAYS = 7


class PasswordValidator:
    """Enhanced password validation for Mauritanian payroll system"""
    
    @staticmethod
    def validate_password_strength(password: str, user: User = None) -> ValidationResult:
        """
        Comprehensive password strength validation
        
        Args:
            password: Password to validate
            user: User object for additional context validation
            
        Returns:
            ValidationResult with validation outcome and recommendations
        """
        result = ValidationResult()
        score = 0
        recommendations = []
        
        if not password:
            result.add_error(
                'password',
                "Le mot de passe est requis",
                'REQUIRED_FIELD'
            )
            return result
        
        # Length validation
        if len(password) < SecurityConfig.PASSWORD_MIN_LENGTH:
            result.add_error(
                'password',
                f"Mot de passe trop court (minimum {SecurityConfig.PASSWORD_MIN_LENGTH} caractères)",
                'PASSWORD_TOO_SHORT'
            )
        elif len(password) > SecurityConfig.PASSWORD_MAX_LENGTH:
            result.add_error(
                'password',
                f"Mot de passe trop long (maximum {SecurityConfig.PASSWORD_MAX_LENGTH} caractères)",
                'PASSWORD_TOO_LONG'
            )
        else:
            score += min(20, len(password) - SecurityConfig.PASSWORD_MIN_LENGTH)
        
        # Character requirements
        has_upper = bool(re.search(r'[A-Z]', password))
        has_lower = bool(re.search(r'[a-z]', password))
        has_digit = bool(re.search(r'\d', password))
        has_special = bool(re.search(f'[{re.escape(SecurityConfig.PASSWORD_SPECIAL_CHARS)}]', password))
        
        if SecurityConfig.PASSWORD_REQUIRE_UPPERCASE and not has_upper:
            result.add_error(
                'password',
                "Le mot de passe doit contenir au moins une lettre majuscule",
                'MISSING_UPPERCASE'
            )
            recommendations.append("Ajouter des lettres majuscules")
        else:
            score += 15
        
        if SecurityConfig.PASSWORD_REQUIRE_LOWERCASE and not has_lower:
            result.add_error(
                'password',
                "Le mot de passe doit contenir au moins une lettre minuscule",
                'MISSING_LOWERCASE'
            )
            recommendations.append("Ajouter des lettres minuscules")
        else:
            score += 15
        
        if SecurityConfig.PASSWORD_REQUIRE_DIGITS and not has_digit:
            result.add_error(
                'password',
                "Le mot de passe doit contenir au moins un chiffre",
                'MISSING_DIGIT'
            )
            recommendations.append("Ajouter des chiffres")
        else:
            score += 15
        
        if SecurityConfig.PASSWORD_REQUIRE_SPECIAL and not has_special:
            result.add_error(
                'password',
                f"Le mot de passe doit contenir au moins un caractère spécial ({SecurityConfig.PASSWORD_SPECIAL_CHARS[:10]}...)",
                'MISSING_SPECIAL'
            )
            recommendations.append("Ajouter des caractères spéciaux")
        else:
            score += 15
        
        # Complexity analysis
        unique_chars = len(set(password.lower()))
        score += min(10, unique_chars - 4)
        
        # Pattern analysis
        if re.search(r'(.)\1{2,}', password):  # Repeated characters
            score -= 10
            recommendations.append("Éviter les caractères répétés")
        
        if re.search(r'(012|123|234|345|456|567|678|789|890)', password):  # Sequential numbers
            score -= 15
            recommendations.append("Éviter les séquences numériques")
        
        if re.search(r'(abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz)', password.lower()):
            score -= 15
            recommendations.append("Éviter les séquences alphabétiques")
        
        # Dictionary words (basic check for common patterns)
        common_patterns = ['password', 'motdepasse', '123456', 'qwerty', 'admin', 'mauritanie']
        for pattern in common_patterns:
            if pattern in password.lower():
                score -= 20
                recommendations.append("Éviter les mots courants")
                break
        
        # User context validation
        if user:
            # Check against user information
            user_info = [
                user.username.lower() if user.username else '',
                user.first_name.lower() if user.first_name else '',
                user.last_name.lower() if user.last_name else '',
                user.email.split('@')[0].lower() if user.email else ''
            ]
            
            for info in user_info:
                if info and len(info) > 2 and info in password.lower():
                    score -= 25
                    recommendations.append("Ne pas utiliser d'informations personnelles")
                    break
        
        # Final score calculation
        score = max(0, min(100, score))
        result.cleaned_data['password_score'] = score
        result.cleaned_data['recommendations'] = recommendations
        
        if score < SecurityConfig.PASSWORD_COMPLEXITY_SCORE_MIN:
            result.add_warning(
                'password',
                f"Mot de passe faible (score: {score}/{SecurityConfig.PASSWORD_COMPLEXITY_SCORE_MIN})",
                'WEAK_PASSWORD'
            )
        
        return result
    
    @staticmethod
    def check_password_history(user: User, new_password: str) -> bool:
        """
        Check if password was used recently
        
        Args:
            user: User object
            new_password: New password to check
            
        Returns:
            True if password is acceptable, False if recently used
        """
        try:
            # Get recent passwords from audit log
            recent_passwords = SecurityAuditLog.objects.filter(
                user=user,
                event_type='PASSWORD_CHANGE',
                timestamp__gte=django_timezone.now() - timedelta(days=365)
            ).order_by('-timestamp')[:SecurityConfig.PASSWORD_HISTORY_COUNT]
            
            for log_entry in recent_passwords:
                old_password_hash = log_entry.additional_data.get('old_password_hash')
                if old_password_hash and check_password(new_password, old_password_hash):
                    return False
            
            return True
        except Exception:
            # If we can't check history, allow the password
            return True
    
    @staticmethod
    def generate_secure_password(length: int = 12) -> str:
        """
        Generate a cryptographically secure password
        
        Args:
            length: Password length
            
        Returns:
            Secure password string
        """
        if length < SecurityConfig.PASSWORD_MIN_LENGTH:
            length = SecurityConfig.PASSWORD_MIN_LENGTH
        
        # Ensure we have at least one character from each required category
        password_chars = []
        
        if SecurityConfig.PASSWORD_REQUIRE_UPPERCASE:
            password_chars.append(secrets.choice('ABCDEFGHIJKLMNOPQRSTUVWXYZ'))
        if SecurityConfig.PASSWORD_REQUIRE_LOWERCASE:
            password_chars.append(secrets.choice('abcdefghijklmnopqrstuvwxyz'))
        if SecurityConfig.PASSWORD_REQUIRE_DIGITS:
            password_chars.append(secrets.choice('0123456789'))
        if SecurityConfig.PASSWORD_REQUIRE_SPECIAL:
            password_chars.append(secrets.choice(SecurityConfig.PASSWORD_SPECIAL_CHARS))
        
        # Fill remaining length with random characters from all allowed categories
        all_chars = ''
        if SecurityConfig.PASSWORD_REQUIRE_UPPERCASE:
            all_chars += 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
        if SecurityConfig.PASSWORD_REQUIRE_LOWERCASE:
            all_chars += 'abcdefghijklmnopqrstuvwxyz'
        if SecurityConfig.PASSWORD_REQUIRE_DIGITS:
            all_chars += '0123456789'
        if SecurityConfig.PASSWORD_REQUIRE_SPECIAL:
            all_chars += SecurityConfig.PASSWORD_SPECIAL_CHARS
        
        for _ in range(length - len(password_chars)):
            password_chars.append(secrets.choice(all_chars))
        
        # Shuffle the password to avoid predictable patterns
        secrets.SystemRandom().shuffle(password_chars)
        
        return ''.join(password_chars)


class AuthenticationManager:
    """Enhanced authentication management with security features"""
    
    def __init__(self):
        self.failed_attempts = defaultdict(lambda: deque(maxlen=SecurityConfig.MAX_LOGIN_ATTEMPTS))
        self.locked_accounts = {}
    
    def authenticate_user(self, username: str, password: str, request: HttpRequest = None) -> Dict[str, Any]:
        """
        Authenticate user with enhanced security checks
        
        Args:
            username: Username to authenticate
            password: Password to verify
            request: HTTP request for IP tracking
            
        Returns:
            Authentication result dictionary
        """
        result = {
            'success': False,
            'user': None,
            'error': None,
            'error_code': None,
            'locked_until': None,
            'attempts_remaining': None
        }
        
        # Sanitize username
        username = DataSanitizer.sanitize_text_input(username, max_length=150)
        
        if not username or not password:
            result['error'] = "Nom d'utilisateur et mot de passe requis"
            result['error_code'] = 'MISSING_CREDENTIALS'
            self._log_security_event('LOGIN_MISSING_CREDENTIALS', None, request)
            return result
        
        # Check if account is locked
        if self._is_account_locked(username):
            locked_until = self.locked_accounts.get(username)
            result['error'] = "Compte verrouillé en raison de tentatives de connexion multiples"
            result['error_code'] = 'ACCOUNT_LOCKED'
            result['locked_until'] = locked_until
            self._log_security_event('LOGIN_LOCKED_ACCOUNT_ATTEMPT', None, request, {'username': username})
            return result
        
        # Check rate limiting
        client_ip = self._get_client_ip(request)
        if not self._check_rate_limit('login', client_ip):
            result['error'] = "Trop de tentatives de connexion. Veuillez réessayer plus tard"
            result['error_code'] = 'RATE_LIMITED'
            self._log_security_event('LOGIN_RATE_LIMITED', None, request, {'ip': client_ip})
            return result
        
        # Attempt authentication
        try:
            user = authenticate(username=username, password=password)
            
            if user is None:
                # Failed authentication
                self._record_failed_attempt(username)
                attempts_left = SecurityConfig.MAX_LOGIN_ATTEMPTS - len(self.failed_attempts[username])
                result['attempts_remaining'] = max(0, attempts_left)
                
                if len(self.failed_attempts[username]) >= SecurityConfig.MAX_LOGIN_ATTEMPTS:
                    self._lock_account(username)
                    result['error'] = "Compte verrouillé en raison de tentatives de connexion multiples"
                    result['error_code'] = 'ACCOUNT_LOCKED'
                    result['locked_until'] = self.locked_accounts[username]
                else:
                    result['error'] = f"Identifiants invalides. {attempts_left} tentatives restantes"
                    result['error_code'] = 'INVALID_CREDENTIALS'
                
                self._log_security_event('LOGIN_FAILED', None, request, {
                    'username': username,
                    'attempts_remaining': attempts_left
                })
                return result
            
            # Check if user account is active
            if not user.is_active:
                result['error'] = "Compte utilisateur désactivé"
                result['error_code'] = 'ACCOUNT_DISABLED'
                self._log_security_event('LOGIN_DISABLED_ACCOUNT', user, request)
                return result
            
            # Check password expiry
            if self._is_password_expired(user):
                result['error'] = "Mot de passe expiré. Changement requis"
                result['error_code'] = 'PASSWORD_EXPIRED'
                result['user'] = user
                self._log_security_event('LOGIN_PASSWORD_EXPIRED', user, request)
                return result
            
            # Successful authentication
            self._clear_failed_attempts(username)
            result['success'] = True
            result['user'] = user
            
            # Update user login information
            user.last_login = django_timezone.now()
            if hasattr(user, 'last_session'):
                user.last_session = django_timezone.now()
            user.save(update_fields=['last_login', 'last_session'] if hasattr(user, 'last_session') else ['last_login'])
            
            self._log_security_event('LOGIN_SUCCESS', user, request)
            
            return result
            
        except Exception as e:
            logger.error(f"Authentication error for user {username}: {str(e)}")
            result['error'] = "Erreur d'authentification système"
            result['error_code'] = 'SYSTEM_ERROR'
            self._log_security_event('LOGIN_SYSTEM_ERROR', None, request, {
                'username': username,
                'error': str(e)
            })
            return result
    
    def change_password(self, user: User, old_password: str, new_password: str, request: HttpRequest = None) -> ValidationResult:
        """
        Change user password with security validation
        
        Args:
            user: User object
            old_password: Current password
            new_password: New password
            request: HTTP request for auditing
            
        Returns:
            ValidationResult with change outcome
        """
        result = ValidationResult()
        
        # Verify current password
        if not check_password(old_password, user.password):
            result.add_error(
                'old_password',
                "Mot de passe actuel incorrect",
                'INVALID_CURRENT_PASSWORD'
            )
            self._log_security_event('PASSWORD_CHANGE_INVALID_CURRENT', user, request)
            return result
        
        # Validate new password strength
        password_validation = PasswordValidator.validate_password_strength(new_password, user)
        if not password_validation.is_valid:
            result.errors.extend(password_validation.errors)
            result.warnings.extend(password_validation.warnings)
            return result
        
        # Check password history
        if not PasswordValidator.check_password_history(user, new_password):
            result.add_error(
                'new_password',
                "Ce mot de passe a été utilisé récemment. Choisissez un mot de passe différent",
                'PASSWORD_RECENTLY_USED'
            )
            return result
        
        # Change password
        try:
            old_password_hash = user.password
            user.set_password(new_password)
            user.save()
            
            # Log the change with old password hash for history checking
            self._log_security_event('PASSWORD_CHANGE_SUCCESS', user, request, {
                'old_password_hash': old_password_hash,
                'strength_score': password_validation.cleaned_data.get('password_score', 0)
            })
            
            result.cleaned_data['password_changed'] = True
            result.cleaned_data['strength_score'] = password_validation.cleaned_data.get('password_score', 0)
            
        except Exception as e:
            result.add_error(
                'password',
                f"Erreur lors du changement de mot de passe: {str(e)}",
                'CHANGE_ERROR'
            )
            self._log_security_event('PASSWORD_CHANGE_ERROR', user, request, {'error': str(e)})
        
        return result
    
    def _is_account_locked(self, username: str) -> bool:
        """Check if account is currently locked"""
        if username not in self.locked_accounts:
            return False
        
        locked_until = self.locked_accounts[username]
        if datetime.now() >= locked_until:
            # Lock has expired
            del self.locked_accounts[username]
            self._clear_failed_attempts(username)
            return False
        
        return True
    
    def _lock_account(self, username: str):
        """Lock account for specified duration"""
        lock_until = datetime.now() + timedelta(minutes=SecurityConfig.ACCOUNT_LOCKOUT_DURATION)
        self.locked_accounts[username] = lock_until
        logger.warning(f"Account locked: {username} until {lock_until}")
    
    def _record_failed_attempt(self, username: str):
        """Record failed login attempt"""
        self.failed_attempts[username].append(datetime.now())
    
    def _clear_failed_attempts(self, username: str):
        """Clear failed attempts for username"""
        if username in self.failed_attempts:
            del self.failed_attempts[username]
    
    def _is_password_expired(self, user: User) -> bool:
        """Check if user's password has expired"""
        if SecurityConfig.PASSWORD_MAX_AGE_DAYS <= 0:
            return False
        
        try:
            # Look for last password change in audit log
            last_change = SecurityAuditLog.objects.filter(
                user=user,
                event_type__in=['PASSWORD_CHANGE_SUCCESS', 'USER_CREATED']
            ).order_by('-timestamp').first()
            
            if not last_change:
                return False
            
            expiry_date = last_change.timestamp + timedelta(days=SecurityConfig.PASSWORD_MAX_AGE_DAYS)
            return django_timezone.now() > expiry_date
            
        except Exception:
            return False
    
    def _check_rate_limit(self, action: str, identifier: str) -> bool:
        """Check if action is rate limited for identifier"""
        cache_key = f"rate_limit:{action}:{identifier}"
        current_count = cache.get(cache_key, 0)
        
        limit = SecurityConfig.RATE_LIMIT_LOGIN if action == 'login' else SecurityConfig.RATE_LIMIT_API
        
        if current_count >= limit:
            return False
        
        # Increment counter
        cache.set(cache_key, current_count + 1, SecurityConfig.RATE_LIMIT_WINDOW)
        return True
    
    def _get_client_ip(self, request: HttpRequest) -> str:
        """Extract client IP from request"""
        if not request:
            return 'unknown'
        
        # Check for forwarded IP first
        forwarded_ip = request.META.get('HTTP_X_FORWARDED_FOR')
        if forwarded_ip:
            return forwarded_ip.split(',')[0].strip()
        
        return request.META.get('REMOTE_ADDR', 'unknown')
    
    def _log_security_event(self, event_type: str, user: User, request: HttpRequest, additional_data: Dict = None):
        """Log security event for audit trail"""
        try:
            SecurityAuditLog.create_log(
                event_type=event_type,
                user=user,
                ip_address=self._get_client_ip(request) if request else None,
                user_agent=request.META.get('HTTP_USER_AGENT') if request else None,
                additional_data=additional_data or {}
            )
        except Exception as e:
            logger.error(f"Failed to log security event {event_type}: {str(e)}")


class JWTManager:
    """JWT token management for API authentication"""
    
    @staticmethod
    def generate_token(user: User, expiration_hours: int = None) -> str:
        """
        Generate JWT token for user
        
        Args:
            user: User object
            expiration_hours: Token expiration in hours
            
        Returns:
            JWT token string
        """
        if expiration_hours is None:
            expiration_hours = SecurityConfig.JWT_EXPIRATION_HOURS
        
        payload = {
            'user_id': user.id,
            'username': user.username,
            'exp': datetime.utcnow() + timedelta(hours=expiration_hours),
            'iat': datetime.utcnow(),
            'type': 'access'
        }
        
        return jwt.encode(payload, SecurityConfig.JWT_SECRET_KEY, algorithm=SecurityConfig.JWT_ALGORITHM)
    
    @staticmethod
    def generate_refresh_token(user: User) -> str:
        """Generate refresh token for user"""
        payload = {
            'user_id': user.id,
            'username': user.username,
            'exp': datetime.utcnow() + timedelta(days=SecurityConfig.JWT_REFRESH_EXPIRATION_DAYS),
            'iat': datetime.utcnow(),
            'type': 'refresh'
        }
        
        return jwt.encode(payload, SecurityConfig.JWT_SECRET_KEY, algorithm=SecurityConfig.JWT_ALGORITHM)
    
    @staticmethod
    def validate_token(token: str) -> Dict[str, Any]:
        """
        Validate JWT token
        
        Args:
            token: JWT token to validate
            
        Returns:
            Validation result dictionary
        """
        result = {
            'valid': False,
            'user': None,
            'payload': None,
            'error': None
        }
        
        try:
            payload = jwt.decode(token, SecurityConfig.JWT_SECRET_KEY, algorithms=[SecurityConfig.JWT_ALGORITHM])
            
            # Get user
            user = User.objects.get(id=payload['user_id'])
            if not user.is_active:
                result['error'] = 'User account is disabled'
                return result
            
            result['valid'] = True
            result['user'] = user
            result['payload'] = payload
            
        except jwt.ExpiredSignatureError:
            result['error'] = 'Token has expired'
        except jwt.InvalidTokenError:
            result['error'] = 'Invalid token'
        except User.DoesNotExist:
            result['error'] = 'User not found'
        except Exception as e:
            result['error'] = f'Token validation error: {str(e)}'
        
        return result
    
    @staticmethod
    def refresh_token(refresh_token: str) -> Dict[str, Any]:
        """
        Refresh access token using refresh token
        
        Args:
            refresh_token: Refresh token
            
        Returns:
            New token or error result
        """
        result = {
            'success': False,
            'access_token': None,
            'error': None
        }
        
        validation = JWTManager.validate_token(refresh_token)
        if not validation['valid']:
            result['error'] = validation['error']
            return result
        
        payload = validation['payload']
        if payload.get('type') != 'refresh':
            result['error'] = 'Invalid token type'
            return result
        
        # Generate new access token
        user = validation['user']
        result['access_token'] = JWTManager.generate_token(user)
        result['success'] = True
        
        return result


class AuthorizationManager:
    """Role-based access control and permission management"""
    
    def __init__(self):
        self.permission_cache = {}
        self.cache_timeout = 300  # 5 minutes
    
    def check_permission(self, user: User, permission: str, resource: Any = None) -> bool:
        """
        Check if user has specific permission
        
        Args:
            user: User object
            permission: Permission string (e.g., 'payroll.view_employee')
            resource: Optional resource object for resource-level permissions
            
        Returns:
            True if user has permission, False otherwise
        """
        if not user or not user.is_active:
            return False
        
        # Superuser bypass
        if user.is_superuser:
            return True
        
        # Check cache first
        cache_key = f"perm:{user.id}:{permission}"
        if resource:
            cache_key += f":{hash(str(resource))}"
        
        cached_result = self.permission_cache.get(cache_key)
        if cached_result and cached_result['expires'] > time.time():
            return cached_result['result']
        
        # Check permission
        has_permission = self._check_user_permission(user, permission, resource)
        
        # Cache result
        self.permission_cache[cache_key] = {
            'result': has_permission,
            'expires': time.time() + self.cache_timeout
        }
        
        return has_permission
    
    def check_module_access(self, user: User, module: str) -> bool:
        """
        Check if user can access specific module
        
        Args:
            user: User object
            module: Module name
            
        Returns:
            True if user has access, False otherwise
        """
        if not user or not user.is_active:
            return False
        
        if user.is_superuser:
            return True
        
        # Use the permission method from User model
        if hasattr(user, 'has_module_permission'):
            return user.has_module_permission(module)
        
        return False
    
    def get_user_permissions(self, user: User) -> Set[str]:
        """
        Get all permissions for user
        
        Args:
            user: User object
            
        Returns:
            Set of permission strings
        """
        if not user or not user.is_active:
            return set()
        
        permissions = set()
        
        # Get user permissions
        user_permissions = user.user_permissions.all()
        permissions.update([f"{p.content_type.app_label}.{p.codename}" for p in user_permissions])
        
        # Get group permissions
        for group in user.groups.all():
            group_permissions = group.permissions.all()
            permissions.update([f"{p.content_type.app_label}.{p.codename}" for p in group_permissions])
        
        return permissions
    
    def require_permission(self, permission: str, resource: Any = None):
        """
        Decorator to require specific permission
        
        Args:
            permission: Required permission
            resource: Optional resource object
            
        Returns:
            Decorator function
        """
        def decorator(func):
            def wrapper(request, *args, **kwargs):
                if not hasattr(request, 'user'):
                    raise AuthorizationError("Authentication required", 'AUTH_REQUIRED')
                
                if not self.check_permission(request.user, permission, resource):
                    SecurityAuditLog.create_log(
                        event_type='AUTHORIZATION_DENIED',
                        user=request.user,
                        ip_address=self._get_client_ip(request),
                        additional_data={'permission': permission}
                    )
                    raise AuthorizationError(
                        f"Permission denied: {permission}",
                        'PERMISSION_DENIED',
                        request.user.id
                    )
                
                return func(request, *args, **kwargs)
            return wrapper
        return decorator
    
    def _check_user_permission(self, user: User, permission: str, resource: Any = None) -> bool:
        """Internal permission checking logic"""
        try:
            # Parse permission string
            if '.' in permission:
                app_label, codename = permission.split('.', 1)
            else:
                codename = permission
                app_label = 'core'  # Default app
            
            # Check Django permission system
            if user.has_perm(f"{app_label}.{codename}"):
                return True
            
            # Check custom payroll permissions (from User model)
            custom_permission_map = {
                'view_employee': 'can_access_personnel',
                'add_employee': 'can_add_employees',
                'change_employee': 'can_update_employees',
                'view_payroll': 'can_access_payroll',
                'change_payroll': 'can_manage_employee_payroll',
                'view_reports': 'can_access_reports',
                'view_dashboard': 'can_access_dashboard',
                'access_parameters': 'can_access_parameters',
                'access_security': 'can_access_security'
            }
            
            if codename in custom_permission_map:
                attr_name = custom_permission_map[codename]
                return getattr(user, attr_name, False)
            
            return False
            
        except Exception as e:
            logger.error(f"Permission check error for {user.username}: {str(e)}")
            return False
    
    def _get_client_ip(self, request):
        """Extract client IP from request"""
        forwarded_ip = request.META.get('HTTP_X_FORWARDED_FOR')
        if forwarded_ip:
            return forwarded_ip.split(',')[0].strip()
        return request.META.get('REMOTE_ADDR', 'unknown')


class LicenseManager:
    """Software licensing and user limit management"""
    
    def __init__(self):
        self.license_cache = {}
        self.last_check = None
    
    def validate_license(self) -> Dict[str, Any]:
        """
        Validate software license
        
        Returns:
            License validation result
        """
        result = {
            'valid': False,
            'expired': False,
            'users_allowed': 0,
            'users_current': 0,
            'features_enabled': [],
            'expiry_date': None,
            'error': None
        }
        
        try:
            # Get system parameters
            from core.models.system_config import SystemParameters
            params = SystemParameters.objects.first()
            
            if not params or not params.license_key:
                result['error'] = 'Aucune licence configurée'
                return result
            
            # Decode license key (simplified - in production use proper encryption)
            license_data = self._decode_license_key(params.license_key)
            
            if not license_data:
                result['error'] = 'Clé de licence invalide'
                return result
            
            # Check expiry
            expiry_date = license_data.get('expiry_date')
            if expiry_date and datetime.now() > expiry_date:
                result['expired'] = True
                result['error'] = 'Licence expirée'
                result['expiry_date'] = expiry_date
                return result
            
            # Check user limits
            users_allowed = license_data.get('max_users', 10)
            users_current = User.objects.filter(is_active=True).count()
            
            result['valid'] = True
            result['users_allowed'] = users_allowed
            result['users_current'] = users_current
            result['features_enabled'] = license_data.get('features', [])
            result['expiry_date'] = expiry_date
            
            # Warning if near user limit
            if users_current >= users_allowed * SecurityConfig.USER_LIMIT_WARNING_THRESHOLD:
                result['warning'] = f'Approche de la limite utilisateur: {users_current}/{users_allowed}'
            
            # Update cache
            self.license_cache = result
            self.last_check = datetime.now()
            
            return result
            
        except Exception as e:
            logger.error(f"License validation error: {str(e)}")
            result['error'] = f'Erreur de validation de licence: {str(e)}'
            return result
    
    def check_user_limit(self) -> bool:
        """
        Check if new user can be added
        
        Returns:
            True if under limit, False otherwise
        """
        license_info = self.get_license_info()
        if not license_info['valid']:
            return False
        
        return license_info['users_current'] < license_info['users_allowed']
    
    def check_feature_enabled(self, feature: str) -> bool:
        """
        Check if specific feature is enabled in license
        
        Args:
            feature: Feature name to check
            
        Returns:
            True if feature is enabled, False otherwise
        """
        license_info = self.get_license_info()
        if not license_info['valid']:
            return False
        
        return feature in license_info['features_enabled']
    
    def get_license_info(self) -> Dict[str, Any]:
        """
        Get current license information (cached)
        
        Returns:
            License information dictionary
        """
        # Check if we need to refresh cache
        if (not self.last_check or 
            datetime.now() - self.last_check > timedelta(hours=SecurityConfig.LICENSE_CHECK_INTERVAL)):
            return self.validate_license()
        
        return self.license_cache
    
    def _decode_license_key(self, license_key: str) -> Dict[str, Any]:
        """
        Decode license key (simplified implementation)
        
        Args:
            license_key: Encoded license key
            
        Returns:
            License data dictionary or None if invalid
        """
        try:
            # In a real implementation, this would use proper encryption/signing
            # For now, we'll use a simple format: base64 encoded JSON
            import base64
            import json
            
            decoded_bytes = base64.b64decode(license_key.encode())
            license_data = json.loads(decoded_bytes.decode())
            
            # Convert date strings to datetime objects
            if 'expiry_date' in license_data:
                license_data['expiry_date'] = datetime.fromisoformat(license_data['expiry_date'])
            
            return license_data
            
        except Exception:
            return None
    
    def generate_license_key(self, max_users: int, expiry_date: datetime, 
                           features: List[str]) -> str:
        """
        Generate license key (for testing/administration)
        
        Args:
            max_users: Maximum number of users
            expiry_date: License expiry date
            features: List of enabled features
            
        Returns:
            Encoded license key
        """
        import base64
        import json
        
        license_data = {
            'max_users': max_users,
            'expiry_date': expiry_date.isoformat(),
            'features': features,
            'issued_date': datetime.now().isoformat()
        }
        
        json_data = json.dumps(license_data)
        encoded_key = base64.b64encode(json_data.encode()).decode()
        
        return encoded_key


class SessionManager:
    """Enhanced session management with security features"""
    
    def __init__(self):
        self.active_sessions = defaultdict(list)
    
    def create_secure_session(self, user: User, request: HttpRequest) -> str:
        """
        Create secure session for user
        
        Args:
            user: User object
            request: HTTP request
            
        Returns:
            Session key
        """
        # Check concurrent session limit
        if not self._check_session_limit(user):
            raise SecurityError(
                "Limite de sessions simultanées atteinte",
                'SESSION_LIMIT_EXCEEDED',
                user.id
            )
        
        # Create Django session
        request.session.create()
        session_key = request.session.session_key
        
        # Store session info
        session_info = {
            'session_key': session_key,
            'user_id': user.id,
            'ip_address': self._get_client_ip(request),
            'user_agent': request.META.get('HTTP_USER_AGENT', ''),
            'created': datetime.now(),
            'last_activity': datetime.now()
        }
        
        self.active_sessions[user.id].append(session_info)
        
        # Log session creation
        SecurityAuditLog.create_log(
            event_type='SESSION_CREATED',
            user=user,
            ip_address=session_info['ip_address'],
            user_agent=session_info['user_agent'],
            additional_data={'session_key': session_key[:8] + '...'}  # Partial key for logging
        )
        
        return session_key
    
    def validate_session(self, session_key: str, request: HttpRequest) -> bool:
        """
        Validate session security
        
        Args:
            session_key: Session key to validate
            request: HTTP request
            
        Returns:
            True if session is valid, False otherwise
        """
        try:
            session = Session.objects.get(session_key=session_key)
            
            # Check expiry
            if session.expire_date < django_timezone.now():
                return False
            
            # Get session data
            session_data = session.get_decoded()
            user_id = session_data.get('_auth_user_id')
            
            if not user_id:
                return False
            
            # Check if user still exists and is active
            try:
                user = User.objects.get(id=user_id, is_active=True)
            except User.DoesNotExist:
                return False
            
            # Update last activity
            self._update_session_activity(user, session_key)
            
            # Check session timeout
            if self._is_session_expired(user, session_key):
                self.terminate_session(session_key, 'SESSION_TIMEOUT')
                return False
            
            return True
            
        except Session.DoesNotExist:
            return False
        except Exception as e:
            logger.error(f"Session validation error: {str(e)}")
            return False
    
    def terminate_session(self, session_key: str, reason: str = 'USER_LOGOUT'):
        """
        Terminate session securely
        
        Args:
            session_key: Session to terminate
            reason: Termination reason
        """
        try:
            session = Session.objects.get(session_key=session_key)
            session_data = session.get_decoded()
            user_id = session_data.get('_auth_user_id')
            
            # Remove from active sessions
            if user_id and user_id in self.active_sessions:
                self.active_sessions[user_id] = [
                    s for s in self.active_sessions[user_id]
                    if s['session_key'] != session_key
                ]
            
            # Delete Django session
            session.delete()
            
            # Log termination
            user = None
            if user_id:
                try:
                    user = User.objects.get(id=user_id)
                except User.DoesNotExist:
                    pass
            
            SecurityAuditLog.create_log(
                event_type='SESSION_TERMINATED',
                user=user,
                additional_data={
                    'session_key': session_key[:8] + '...',
                    'reason': reason
                }
            )
            
        except Session.DoesNotExist:
            pass
        except Exception as e:
            logger.error(f"Session termination error: {str(e)}")
    
    def terminate_user_sessions(self, user: User, except_session: str = None):
        """
        Terminate all sessions for user
        
        Args:
            user: User object
            except_session: Session key to keep active (optional)
        """
        # Get all sessions for user
        sessions = Session.objects.filter(
            session_data__contains=f'"_auth_user_id":"{user.id}"'
        )
        
        terminated_count = 0
        for session in sessions:
            if except_session and session.session_key == except_session:
                continue
            
            self.terminate_session(session.session_key, 'ADMIN_TERMINATION')
            terminated_count += 1
        
        # Clear from active sessions
        if user.id in self.active_sessions:
            if except_session:
                self.active_sessions[user.id] = [
                    s for s in self.active_sessions[user.id]
                    if s['session_key'] == except_session
                ]
            else:
                del self.active_sessions[user.id]
        
        logger.info(f"Terminated {terminated_count} sessions for user {user.username}")
    
    def _check_session_limit(self, user: User) -> bool:
        """Check if user is under session limit"""
        current_count = len(self.active_sessions[user.id])
        return current_count < SecurityConfig.CONCURRENT_SESSIONS_MAX
    
    def _update_session_activity(self, user: User, session_key: str):
        """Update last activity time for session"""
        for session_info in self.active_sessions[user.id]:
            if session_info['session_key'] == session_key:
                session_info['last_activity'] = datetime.now()
                break
    
    def _is_session_expired(self, user: User, session_key: str) -> bool:
        """Check if session has expired due to inactivity"""
        for session_info in self.active_sessions[user.id]:
            if session_info['session_key'] == session_key:
                last_activity = session_info['last_activity']
                timeout = timedelta(minutes=SecurityConfig.SESSION_TIMEOUT)
                return datetime.now() - last_activity > timeout
        return True
    
    def _get_client_ip(self, request: HttpRequest) -> str:
        """Extract client IP from request"""
        forwarded_ip = request.META.get('HTTP_X_FORWARDED_FOR')
        if forwarded_ip:
            return forwarded_ip.split(',')[0].strip()
        return request.META.get('REMOTE_ADDR', 'unknown')


class SecurityValidator:
    """Input sanitization and security validation utilities"""
    
    @staticmethod
    def sanitize_input(data: str, input_type: str = 'text') -> str:
        """
        Sanitize input to prevent injection attacks
        
        Args:
            data: Input data to sanitize
            input_type: Type of input (text, html, sql, etc.)
            
        Returns:
            Sanitized input
        """
        if not data:
            return ''
        
        if input_type == 'sql':
            return SecurityValidator._sanitize_sql(data)
        elif input_type == 'html':
            return SecurityValidator._sanitize_html(data)
        elif input_type == 'javascript':
            return SecurityValidator._sanitize_javascript(data)
        else:
            return SecurityValidator._sanitize_text(data)
    
    @staticmethod
    def _sanitize_text(data: str) -> str:
        """Sanitize general text input"""
        # Remove null bytes and control characters
        data = ''.join(char for char in data if ord(char) >= 32 or char in '\t\n\r')
        
        # Limit length
        if len(data) > 10000:
            data = data[:10000]
        
        # Use existing text utilities
        return TextFormatter.clean_string(data, preserve_case=True)
    
    @staticmethod
    def _sanitize_sql(data: str) -> str:
        """Sanitize SQL input to prevent injection"""
        # Remove dangerous SQL keywords and characters
        dangerous_patterns = [
            r"('|(\\'))",  # Single quotes
            r'("|(\\""))',  # Double quotes
            r'(;|\\;)',     # Semicolons
            r'(--|#)',      # Comments
            r'\b(SELECT|INSERT|UPDATE|DELETE|DROP|UNION|ALTER|CREATE)\b',  # SQL keywords
            r'\b(EXEC|EXECUTE|SP_|XP_)\b',  # Stored procedures
            r'(\*|%)',      # Wildcards
        ]
        
        sanitized = data
        for pattern in dangerous_patterns:
            sanitized = re.sub(pattern, '', sanitized, flags=re.IGNORECASE)
        
        return sanitized.strip()
    
    @staticmethod
    def _sanitize_html(data: str) -> str:
        """Sanitize HTML input to prevent XSS"""
        # Remove script tags and event handlers
        dangerous_patterns = [
            r'<script[^>]*>.*?</script>',
            r'<iframe[^>]*>.*?</iframe>',
            r'<object[^>]*>.*?</object>',
            r'<embed[^>]*>.*?</embed>',
            r'on\w+\s*=',  # Event handlers like onclick, onload, etc.
            r'javascript:',
            r'vbscript:',
            r'data:text/html',
        ]
        
        sanitized = data
        for pattern in dangerous_patterns:
            sanitized = re.sub(pattern, '', sanitized, flags=re.IGNORECASE | re.DOTALL)
        
        return sanitized
    
    @staticmethod
    def _sanitize_javascript(data: str) -> str:
        """Sanitize JavaScript input"""
        # Remove dangerous JavaScript patterns
        dangerous_patterns = [
            r'eval\s*\(',
            r'setTimeout\s*\(',
            r'setInterval\s*\(',
            r'Function\s*\(',
            r'document\.write',
            r'innerHTML',
            r'outerHTML',
            r'document\.cookie',
            r'window\.location',
        ]
        
        sanitized = data
        for pattern in dangerous_patterns:
            sanitized = re.sub(pattern, '', sanitized, flags=re.IGNORECASE)
        
        return sanitized
    
    @staticmethod
    def validate_ip_address(ip_address: str) -> bool:
        """
        Validate IP address format
        
        Args:
            ip_address: IP address to validate
            
        Returns:
            True if valid, False otherwise
        """
        try:
            ipaddress.ip_address(ip_address)
            return True
        except ValueError:
            return False
    
    @staticmethod
    def is_safe_redirect_url(url: str, allowed_hosts: List[str] = None) -> bool:
        """
        Check if redirect URL is safe
        
        Args:
            url: URL to check
            allowed_hosts: List of allowed hosts
            
        Returns:
            True if safe, False otherwise
        """
        if not url:
            return False
        
        # Check for absolute URLs
        if url.startswith(('http://', 'https://')):
            from urllib.parse import urlparse
            parsed = urlparse(url)
            
            if allowed_hosts:
                return parsed.hostname in allowed_hosts
            else:
                # If no allowed hosts specified, only allow same domain
                return False
        
        # Relative URLs starting with / are generally safe
        if url.startswith('/'):
            return not url.startswith('//')  # Avoid protocol-relative URLs
        
        return False


class SecurityAuditLog(models.Model):
    """Security audit log model for tracking security events"""
    
    EVENT_TYPES = [
        ('LOGIN_SUCCESS', 'Successful Login'),
        ('LOGIN_FAILED', 'Failed Login'),
        ('LOGIN_LOCKED_ACCOUNT_ATTEMPT', 'Locked Account Login Attempt'),
        ('LOGIN_MISSING_CREDENTIALS', 'Missing Credentials'),
        ('LOGIN_RATE_LIMITED', 'Rate Limited Login'),
        ('LOGIN_DISABLED_ACCOUNT', 'Disabled Account Login'),
        ('LOGIN_PASSWORD_EXPIRED', 'Password Expired Login'),
        ('LOGIN_SYSTEM_ERROR', 'System Error During Login'),
        ('LOGOUT', 'User Logout'),
        ('PASSWORD_CHANGE_SUCCESS', 'Successful Password Change'),
        ('PASSWORD_CHANGE_INVALID_CURRENT', 'Invalid Current Password'),
        ('PASSWORD_CHANGE_ERROR', 'Password Change Error'),
        ('SESSION_CREATED', 'Session Created'),
        ('SESSION_TERMINATED', 'Session Terminated'),
        ('AUTHORIZATION_DENIED', 'Authorization Denied'),
        ('PERMISSION_GRANTED', 'Permission Granted'),
        ('USER_CREATED', 'User Created'),
        ('USER_UPDATED', 'User Updated'),
        ('USER_DELETED', 'User Deleted'),
        ('USER_ACTIVATED', 'User Activated'),
        ('USER_DEACTIVATED', 'User Deactivated'),
        ('DATA_ACCESS', 'Data Access'),
        ('DATA_MODIFICATION', 'Data Modification'),
        ('SECURITY_VIOLATION', 'Security Violation'),
        ('LICENSE_CHECK', 'License Check'),
        ('LICENSE_VIOLATION', 'License Violation'),
    ]
    
    id = models.AutoField(primary_key=True)
    event_type = models.CharField(max_length=50, choices=EVENT_TYPES)
    timestamp = models.DateTimeField(default=django_timezone.now, db_index=True)
    user = models.ForeignKey(User, null=True, blank=True, on_delete=models.SET_NULL)
    ip_address = models.GenericIPAddressField(null=True, blank=True)
    user_agent = models.TextField(blank=True)
    additional_data = models.JSONField(default=dict, blank=True)
    
    class Meta:
        db_table = 'security_audit_log'
        ordering = ['-timestamp']
        indexes = [
            models.Index(fields=['timestamp']),
            models.Index(fields=['event_type']),
            models.Index(fields=['user', 'timestamp']),
        ]
    
    def __str__(self):
        user_str = f" by {self.user.username}" if self.user else ""
        return f"{self.get_event_type_display()} at {self.timestamp}{user_str}"
    
    @classmethod
    def create_log(cls, event_type: str, user: User = None, ip_address: str = None,
                   user_agent: str = None, additional_data: Dict = None):
        """
        Create audit log entry
        
        Args:
            event_type: Type of event
            user: User associated with event
            ip_address: Client IP address
            user_agent: User agent string
            additional_data: Additional event data
        """
        try:
            cls.objects.create(
                event_type=event_type,
                user=user,
                ip_address=ip_address,
                user_agent=user_agent[:1000] if user_agent else '',  # Limit length
                additional_data=additional_data or {}
            )
        except Exception as e:
            logger.error(f"Failed to create audit log: {str(e)}")
    
    @classmethod
    def get_user_activity(cls, user: User, days: int = 30) -> models.QuerySet:
        """Get recent activity for user"""
        since = django_timezone.now() - timedelta(days=days)
        return cls.objects.filter(user=user, timestamp__gte=since)
    
    @classmethod
    def get_security_events(cls, event_types: List[str] = None, days: int = 7) -> models.QuerySet:
        """Get recent security events"""
        since = django_timezone.now() - timedelta(days=days)
        queryset = cls.objects.filter(timestamp__gte=since)
        
        if event_types:
            queryset = queryset.filter(event_type__in=event_types)
        
        return queryset
    
    @classmethod
    def cleanup_old_logs(cls, days: int = None):
        """Clean up old audit logs"""
        if days is None:
            days = SecurityConfig.AUDIT_RETENTION_DAYS
        
        cutoff_date = django_timezone.now() - timedelta(days=days)
        deleted_count, _ = cls.objects.filter(timestamp__lt=cutoff_date).delete()
        
        if deleted_count > 0:
            logger.info(f"Cleaned up {deleted_count} old audit log entries")
        
        return deleted_count


# Global security manager instances
authentication_manager = AuthenticationManager()
authorization_manager = AuthorizationManager()
license_manager = LicenseManager()
session_manager = SessionManager()

# Convenience functions
def authenticate_user(username: str, password: str, request: HttpRequest = None) -> Dict[str, Any]:
    """Authenticate user with security checks"""
    return authentication_manager.authenticate_user(username, password, request)

def check_permission(user: User, permission: str, resource: Any = None) -> bool:
    """Check user permission"""
    return authorization_manager.check_permission(user, permission, resource)

def validate_license() -> Dict[str, Any]:
    """Validate software license"""
    return license_manager.validate_license()

def create_secure_session(user: User, request: HttpRequest) -> str:
    """Create secure session"""
    return session_manager.create_secure_session(user, request)

def sanitize_input(data: str, input_type: str = 'text') -> str:
    """Sanitize input data"""
    return SecurityValidator.sanitize_input(data, input_type)

def log_security_event(event_type: str, user: User = None, request: HttpRequest = None, additional_data: Dict = None):
    """Log security event"""
    ip_address = None
    user_agent = None
    
    if request:
        forwarded_ip = request.META.get('HTTP_X_FORWARDED_FOR')
        if forwarded_ip:
            ip_address = forwarded_ip.split(',')[0].strip()
        else:
            ip_address = request.META.get('REMOTE_ADDR')
        user_agent = request.META.get('HTTP_USER_AGENT')
    
    SecurityAuditLog.create_log(event_type, user, ip_address, user_agent, additional_data)