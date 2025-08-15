"""
Comprehensive Tests for Security Module

Tests all security capabilities including:
- Password validation and strength checking
- Authentication with rate limiting and account lockout
- Authorization and permission checking
- License management and validation
- Session security and management
- Input sanitization and XSS prevention
- Audit logging and security event tracking
- JWT token management
"""

import pytest
import time
from datetime import datetime, timedelta
from django.test import TestCase, RequestFactory
from django.contrib.auth import get_user_model
from django.contrib.sessions.models import Session
from django.core.cache import cache
from django.utils import timezone

from core.utils.security import (
    SecurityConfig, PasswordValidator, AuthenticationManager, 
    AuthorizationManager, LicenseManager, SessionManager, 
    SecurityValidator, JWTManager, SecurityAuditLog,
    authenticate_user, check_permission, validate_license,
    sanitize_input, log_security_event
)

User = get_user_model()


class PasswordValidatorTest(TestCase):
    """Test password validation and strength checking"""
    
    def test_password_strength_validation(self):
        """Test comprehensive password strength validation"""
        # Weak password
        weak_result = PasswordValidator.validate_password_strength("123")
        self.assertFalse(weak_result.is_valid)
        self.assertTrue(any('PASSWORD_TOO_SHORT' in error.get('error_code', '') for error in weak_result.errors))
        
        # Medium strength password
        medium_result = PasswordValidator.validate_password_strength("Password123")
        self.assertTrue(medium_result.is_valid)
        score = medium_result.cleaned_data.get('password_score', 0)
        self.assertGreater(score, 60)
        
        # Strong password
        strong_result = PasswordValidator.validate_password_strength("SecureP@ssw0rd!2024")
        self.assertTrue(strong_result.is_valid)
        strong_score = strong_result.cleaned_data.get('password_score', 0)
        self.assertGreater(strong_score, 80)
        
    def test_password_character_requirements(self):
        """Test password character requirements"""
        # Missing uppercase
        result = PasswordValidator.validate_password_strength("password123!")
        self.assertFalse(result.is_valid)
        self.assertTrue(any('MISSING_UPPERCASE' in error.get('error_code', '') for error in result.errors))
        
        # Missing lowercase
        result = PasswordValidator.validate_password_strength("PASSWORD123!")
        self.assertFalse(result.is_valid)
        self.assertTrue(any('MISSING_LOWERCASE' in error.get('error_code', '') for error in result.errors))
        
        # Missing digits
        result = PasswordValidator.validate_password_strength("Password!")
        self.assertFalse(result.is_valid)
        self.assertTrue(any('MISSING_DIGIT' in error.get('error_code', '') for error in result.errors))
        
        # Missing special characters
        result = PasswordValidator.validate_password_strength("Password123")
        self.assertFalse(result.is_valid)
        self.assertTrue(any('MISSING_SPECIAL' in error.get('error_code', '') for error in result.errors))
    
    def test_password_user_context_validation(self):
        """Test password validation against user information"""
        user = User.objects.create_user(
            username='jdoe',
            email='john.doe@example.com',
            first_name='John',
            last_name='Doe'
        )
        
        # Password contains username
        result = PasswordValidator.validate_password_strength("jdoePassword123!", user)
        self.assertGreater(len(result.warnings), 0)
        
        # Password contains first name
        result = PasswordValidator.validate_password_strength("JohnSecure123!", user)
        recommendations = result.cleaned_data.get('recommendations', [])
        self.assertIn("Ne pas utiliser d'informations personnelles", recommendations)
    
    def test_secure_password_generation(self):
        """Test secure password generation"""
        password = PasswordValidator.generate_secure_password(12)
        self.assertEqual(len(password), 12)
        
        # Validate generated password meets requirements
        result = PasswordValidator.validate_password_strength(password)
        self.assertTrue(result.is_valid)
        score = result.cleaned_data.get('password_score', 0)
        self.assertGreater(score, SecurityConfig.PASSWORD_COMPLEXITY_SCORE_MIN)


class AuthenticationManagerTest(TestCase):
    """Test authentication management with security features"""
    
    def setUp(self):
        self.factory = RequestFactory()
        self.user = User.objects.create_user(
            username='testuser',
            password='TestPassword123!',
            email='test@example.com'
        )
        self.auth_manager = AuthenticationManager()
    
    def test_successful_authentication(self):
        """Test successful user authentication"""
        request = self.factory.get('/')
        
        result = self.auth_manager.authenticate_user('testuser', 'TestPassword123!', request)
        
        self.assertTrue(result['success'])
        self.assertEqual(result['user'], self.user)
        self.assertIsNone(result['error'])
    
    def test_failed_authentication(self):
        """Test failed authentication handling"""
        request = self.factory.get('/')
        
        result = self.auth_manager.authenticate_user('testuser', 'wrongpassword', request)
        
        self.assertFalse(result['success'])
        self.assertIsNone(result['user'])
        self.assertEqual(result['error_code'], 'INVALID_CREDENTIALS')
        self.assertIsNotNone(result['attempts_remaining'])
    
    def test_account_lockout(self):
        """Test account lockout after failed attempts"""
        request = self.factory.get('/')
        
        # Make maximum failed attempts
        for i in range(SecurityConfig.MAX_LOGIN_ATTEMPTS):
            result = self.auth_manager.authenticate_user('testuser', 'wrongpassword', request)
            if i < SecurityConfig.MAX_LOGIN_ATTEMPTS - 1:
                self.assertEqual(result['error_code'], 'INVALID_CREDENTIALS')
            else:
                self.assertEqual(result['error_code'], 'ACCOUNT_LOCKED')
        
        # Verify account is locked for subsequent attempts
        result = self.auth_manager.authenticate_user('testuser', 'TestPassword123!', request)
        self.assertEqual(result['error_code'], 'ACCOUNT_LOCKED')
        self.assertIsNotNone(result['locked_until'])
    
    def test_missing_credentials(self):
        """Test authentication with missing credentials"""
        request = self.factory.get('/')
        
        result = self.auth_manager.authenticate_user('', 'password', request)
        self.assertEqual(result['error_code'], 'MISSING_CREDENTIALS')
        
        result = self.auth_manager.authenticate_user('username', '', request)
        self.assertEqual(result['error_code'], 'MISSING_CREDENTIALS')
    
    def test_disabled_account_authentication(self):
        """Test authentication with disabled account"""
        self.user.is_active = False
        self.user.save()
        
        request = self.factory.get('/')
        result = self.auth_manager.authenticate_user('testuser', 'TestPassword123!', request)
        
        self.assertFalse(result['success'])
        self.assertEqual(result['error_code'], 'ACCOUNT_DISABLED')
    
    def test_password_change(self):
        """Test secure password change"""
        request = self.factory.post('/')
        
        result = self.auth_manager.change_password(
            self.user, 'TestPassword123!', 'NewSecureP@ssw0rd!', request
        )
        
        self.assertTrue(result.is_valid)
        self.assertTrue(result.cleaned_data.get('password_changed', False))
        
        # Verify old password no longer works
        auth_result = self.auth_manager.authenticate_user('testuser', 'TestPassword123!', request)
        self.assertFalse(auth_result['success'])
        
        # Verify new password works
        auth_result = self.auth_manager.authenticate_user('testuser', 'NewSecureP@ssw0rd!', request)
        self.assertTrue(auth_result['success'])


class AuthorizationManagerTest(TestCase):
    """Test authorization and permission management"""
    
    def setUp(self):
        self.user = User.objects.create_user(
            username='testuser',
            password='TestPassword123!'
        )
        self.auth_manager = AuthorizationManager()
    
    def test_superuser_permissions(self):
        """Test superuser has all permissions"""
        self.user.is_superuser = True
        self.user.save()
        
        self.assertTrue(self.auth_manager.check_permission(self.user, 'any.permission'))
        self.assertTrue(self.auth_manager.check_module_access(self.user, 'any_module'))
    
    def test_module_access_permissions(self):
        """Test module-specific access permissions"""
        # Grant payroll access
        self.user.can_access_payroll = True
        self.user.save()
        
        self.assertTrue(self.auth_manager.check_module_access(self.user, 'payroll'))
        self.assertFalse(self.auth_manager.check_module_access(self.user, 'security'))
    
    def test_custom_permission_mapping(self):
        """Test custom permission mapping"""
        # Grant employee management permission
        self.user.can_access_personnel = True
        self.user.save()
        
        self.assertTrue(self.auth_manager.check_permission(self.user, 'view_employee'))
        self.assertFalse(self.auth_manager.check_permission(self.user, 'view_reports'))
    
    def test_inactive_user_permissions(self):
        """Test inactive user has no permissions"""
        self.user.is_active = False
        self.user.is_superuser = True
        self.user.save()
        
        self.assertFalse(self.auth_manager.check_permission(self.user, 'any.permission'))
        self.assertFalse(self.auth_manager.check_module_access(self.user, 'any_module'))


class LicenseManagerTest(TestCase):
    """Test license management and validation"""
    
    def setUp(self):
        self.license_manager = LicenseManager()
    
    def test_license_key_generation_and_validation(self):
        """Test license key generation and validation"""
        # Generate license key
        expiry_date = datetime.now() + timedelta(days=365)
        features = ['payroll', 'reporting', 'analytics']
        
        license_key = self.license_manager.generate_license_key(50, expiry_date, features)
        self.assertIsInstance(license_key, str)
        self.assertGreater(len(license_key), 0)
        
        # Test license key decoding
        license_data = self.license_manager._decode_license_key(license_key)
        self.assertIsNotNone(license_data)
        self.assertEqual(license_data['max_users'], 50)
        self.assertEqual(license_data['features'], features)
    
    def test_user_limit_checking(self):
        """Test user limit validation"""
        # Create test users
        for i in range(3):
            User.objects.create_user(
                username=f'user{i}',
                password='TestPassword123!'
            )
        
        # Generate license with low user limit
        expiry_date = datetime.now() + timedelta(days=365)
        license_key = self.license_manager.generate_license_key(5, expiry_date, [])
        
        # Mock system parameters
        from core.models.system_config import SystemParameters
        params, created = SystemParameters.objects.get_or_create(
            id=1,
            defaults={
                'company_name': 'Test Company',
                'default_working_days': 22,
                'non_taxable_allowance_ceiling': 1000,
                'current_period': datetime.now().date(),
                'next_period': datetime.now().date() + timedelta(days=30),
                'closure_period': datetime.now().date(),
                'net_account': 123456,
                'license_key': license_key
            }
        )
        if not created:
            params.license_key = license_key
            params.save()
        
        # Test license validation
        result = self.license_manager.validate_license()
        self.assertTrue(result['valid'])
        self.assertEqual(result['users_allowed'], 5)
        self.assertTrue(self.license_manager.check_user_limit())
    
    def test_expired_license(self):
        """Test expired license handling"""
        # Generate expired license
        expiry_date = datetime.now() - timedelta(days=1)
        license_key = self.license_manager.generate_license_key(50, expiry_date, [])
        
        # Mock system parameters with expired license
        from core.models.system_config import SystemParameters
        params, created = SystemParameters.objects.get_or_create(
            id=1,
            defaults={
                'company_name': 'Test Company',
                'default_working_days': 22,
                'non_taxable_allowance_ceiling': 1000,
                'current_period': datetime.now().date(),
                'next_period': datetime.now().date() + timedelta(days=30),
                'closure_period': datetime.now().date(),
                'net_account': 123456,
                'license_key': license_key
            }
        )
        if not created:
            params.license_key = license_key
            params.save()
        
        result = self.license_manager.validate_license()
        self.assertFalse(result['valid'])
        self.assertTrue(result['expired'])
        self.assertIn('expirée', result['error'])


class SessionManagerTest(TestCase):
    """Test session management and security"""
    
    def setUp(self):
        self.factory = RequestFactory()
        self.user = User.objects.create_user(
            username='testuser',
            password='TestPassword123!'
        )
        self.session_manager = SessionManager()
    
    def test_secure_session_creation(self):
        """Test secure session creation"""
        request = self.factory.get('/')
        request.session = self.client.session
        
        session_key = self.session_manager.create_secure_session(self.user, request)
        
        self.assertIsNotNone(session_key)
        self.assertIn(self.user.id, self.session_manager.active_sessions)
        
        sessions = self.session_manager.active_sessions[self.user.id]
        self.assertEqual(len(sessions), 1)
        self.assertEqual(sessions[0]['session_key'], session_key)
    
    def test_session_validation(self):
        """Test session validation"""
        request = self.factory.get('/')
        request.session = self.client.session
        
        session_key = self.session_manager.create_secure_session(self.user, request)
        
        # Valid session should pass
        self.assertTrue(self.session_manager.validate_session(session_key, request))
    
    def test_session_termination(self):
        """Test session termination"""
        request = self.factory.get('/')
        request.session = self.client.session
        
        session_key = self.session_manager.create_secure_session(self.user, request)
        
        # Terminate session
        self.session_manager.terminate_session(session_key, 'TEST_TERMINATION')
        
        # Session should no longer be valid
        self.assertFalse(self.session_manager.validate_session(session_key, request))
    
    def test_concurrent_session_limits(self):
        """Test concurrent session limit enforcement"""
        # Create maximum allowed sessions
        for i in range(SecurityConfig.CONCURRENT_SESSIONS_MAX):
            request = self.factory.get('/')
            request.session = {}
            request.session.create = lambda: setattr(request.session, 'session_key', f'session_{i}')
            self.session_manager.create_secure_session(self.user, request)
        
        # Attempting to create one more should fail
        request = self.factory.get('/')
        request.session = {}
        request.session.create = lambda: setattr(request.session, 'session_key', 'session_overflow')
        
        with self.assertRaises(Exception):
            self.session_manager.create_secure_session(self.user, request)


class SecurityValidatorTest(TestCase):
    """Test input sanitization and security validation"""
    
    def test_text_sanitization(self):
        """Test general text input sanitization"""
        malicious_input = "<script>alert('XSS')</script>Hello World"
        sanitized = SecurityValidator.sanitize_input(malicious_input, 'text')
        
        self.assertNotIn('<script>', sanitized)
        self.assertIn('Hello World', sanitized)
    
    def test_sql_injection_prevention(self):
        """Test SQL injection prevention"""
        malicious_sql = "Robert'; DROP TABLE users; --"
        sanitized = SecurityValidator.sanitize_input(malicious_sql, 'sql')
        
        self.assertNotIn('DROP', sanitized)
        self.assertNotIn(';', sanitized)
        self.assertNotIn('--', sanitized)
    
    def test_html_sanitization(self):
        """Test HTML input sanitization"""
        malicious_html = """
        <script>alert('XSS')</script>
        <iframe src="javascript:alert('XSS')"></iframe>
        <div onclick="alert('XSS')">Click me</div>
        <p>Safe content</p>
        """
        
        sanitized = SecurityValidator.sanitize_input(malicious_html, 'html')
        
        self.assertNotIn('<script>', sanitized)
        self.assertNotIn('<iframe>', sanitized)
        self.assertNotIn('onclick=', sanitized)
        self.assertNotIn('javascript:', sanitized)
        self.assertIn('Safe content', sanitized)
    
    def test_javascript_sanitization(self):
        """Test JavaScript input sanitization"""
        malicious_js = """
        eval('alert("XSS")');
        setTimeout('alert("XSS")', 1000);
        document.write('<script>alert("XSS")</script>');
        var x = 'safe code';
        """
        
        sanitized = SecurityValidator.sanitize_input(malicious_js, 'javascript')
        
        self.assertNotIn('eval(', sanitized)
        self.assertNotIn('setTimeout(', sanitized)
        self.assertNotIn('document.write', sanitized)
        self.assertIn('safe code', sanitized)
    
    def test_ip_address_validation(self):
        """Test IP address validation"""
        # Valid IP addresses
        self.assertTrue(SecurityValidator.validate_ip_address('192.168.1.1'))
        self.assertTrue(SecurityValidator.validate_ip_address('10.0.0.1'))
        self.assertTrue(SecurityValidator.validate_ip_address('2001:db8::1'))
        
        # Invalid IP addresses
        self.assertFalse(SecurityValidator.validate_ip_address('256.1.1.1'))
        self.assertFalse(SecurityValidator.validate_ip_address('invalid'))
        self.assertFalse(SecurityValidator.validate_ip_address('192.168.1'))
    
    def test_safe_redirect_url_validation(self):
        """Test safe redirect URL validation"""
        # Safe relative URLs
        self.assertTrue(SecurityValidator.is_safe_redirect_url('/dashboard'))
        self.assertTrue(SecurityValidator.is_safe_redirect_url('/reports/payroll'))
        
        # Unsafe URLs
        self.assertFalse(SecurityValidator.is_safe_redirect_url('//evil.com'))
        self.assertFalse(SecurityValidator.is_safe_redirect_url('http://evil.com'))
        self.assertFalse(SecurityValidator.is_safe_redirect_url('javascript:alert("XSS")'))
        
        # Safe with allowed hosts
        allowed_hosts = ['mycompany.com', 'secure.mycompany.com']
        self.assertTrue(SecurityValidator.is_safe_redirect_url('https://mycompany.com/dashboard', allowed_hosts))
        self.assertFalse(SecurityValidator.is_safe_redirect_url('https://evil.com/dashboard', allowed_hosts))


class JWTManagerTest(TestCase):
    """Test JWT token management"""
    
    def setUp(self):
        self.user = User.objects.create_user(
            username='testuser',
            password='TestPassword123!'
        )
    
    def test_token_generation(self):
        """Test JWT token generation"""
        token = JWTManager.generate_token(self.user)
        self.assertIsInstance(token, str)
        self.assertGreater(len(token), 0)
    
    def test_token_validation(self):
        """Test JWT token validation"""
        token = JWTManager.generate_token(self.user)
        
        result = JWTManager.validate_token(token)
        
        self.assertTrue(result['valid'])
        self.assertEqual(result['user'], self.user)
        self.assertIsNotNone(result['payload'])
        self.assertIsNone(result['error'])
    
    def test_invalid_token_validation(self):
        """Test invalid token validation"""
        invalid_token = "invalid.token.here"
        
        result = JWTManager.validate_token(invalid_token)
        
        self.assertFalse(result['valid'])
        self.assertIsNone(result['user'])
        self.assertIsNotNone(result['error'])
    
    def test_token_refresh(self):
        """Test token refresh functionality"""
        refresh_token = JWTManager.generate_refresh_token(self.user)
        
        result = JWTManager.refresh_token(refresh_token)
        
        self.assertTrue(result['success'])
        self.assertIsNotNone(result['access_token'])
        self.assertIsNone(result['error'])
        
        # Verify new token is valid
        validation = JWTManager.validate_token(result['access_token'])
        self.assertTrue(validation['valid'])
        self.assertEqual(validation['user'], self.user)


class SecurityAuditLogTest(TestCase):
    """Test security audit logging"""
    
    def setUp(self):
        self.user = User.objects.create_user(
            username='testuser',
            password='TestPassword123!'
        )
    
    def test_audit_log_creation(self):
        """Test audit log entry creation"""
        SecurityAuditLog.create_log(
            event_type='LOGIN_SUCCESS',
            user=self.user,
            ip_address='192.168.1.1',
            user_agent='Mozilla/5.0 (Test Browser)',
            additional_data={'session_id': 'test_session'}
        )
        
        # Verify log entry was created
        logs = SecurityAuditLog.objects.filter(user=self.user, event_type='LOGIN_SUCCESS')
        self.assertEqual(logs.count(), 1)
        
        log = logs.first()
        self.assertEqual(log.ip_address, '192.168.1.1')
        self.assertEqual(log.additional_data['session_id'], 'test_session')
    
    def test_user_activity_retrieval(self):
        """Test user activity retrieval"""
        # Create multiple log entries
        for event_type in ['LOGIN_SUCCESS', 'DATA_ACCESS', 'LOGOUT']:
            SecurityAuditLog.create_log(event_type=event_type, user=self.user)
        
        activity = SecurityAuditLog.get_user_activity(self.user, days=30)
        self.assertEqual(activity.count(), 3)
    
    def test_security_events_filtering(self):
        """Test security event filtering"""
        # Create various log entries
        SecurityAuditLog.create_log(event_type='LOGIN_SUCCESS', user=self.user)
        SecurityAuditLog.create_log(event_type='LOGIN_FAILED', user=self.user)
        SecurityAuditLog.create_log(event_type='DATA_ACCESS', user=self.user)
        
        # Filter only login-related events
        login_events = SecurityAuditLog.get_security_events(['LOGIN_SUCCESS', 'LOGIN_FAILED'])
        self.assertEqual(login_events.count(), 2)
    
    def test_audit_log_cleanup(self):
        """Test old audit log cleanup"""
        # Create old log entry
        old_log = SecurityAuditLog.objects.create(
            event_type='LOGIN_SUCCESS',
            user=self.user,
            timestamp=timezone.now() - timedelta(days=400)
        )
        
        # Create recent log entry
        recent_log = SecurityAuditLog.objects.create(
            event_type='LOGIN_SUCCESS',
            user=self.user
        )
        
        # Cleanup old logs
        deleted_count = SecurityAuditLog.cleanup_old_logs(days=365)
        
        self.assertEqual(deleted_count, 1)
        self.assertFalse(SecurityAuditLog.objects.filter(id=old_log.id).exists())
        self.assertTrue(SecurityAuditLog.objects.filter(id=recent_log.id).exists())


class SecurityIntegrationTest(TestCase):
    """Integration tests for security utilities"""
    
    def setUp(self):
        self.factory = RequestFactory()
        self.user = User.objects.create_user(
            username='integrationuser',
            password='TestPassword123!'
        )
        cache.clear()  # Clear cache between tests
    
    def test_complete_authentication_flow(self):
        """Test complete authentication flow with security checks"""
        request = self.factory.post('/')
        
        # Test authentication
        result = authenticate_user('integrationuser', 'TestPassword123!', request)
        self.assertTrue(result['success'])
        
        # Verify audit log entry was created
        logs = SecurityAuditLog.objects.filter(
            user=self.user,
            event_type='LOGIN_SUCCESS'
        )
        self.assertEqual(logs.count(), 1)
    
    def test_permission_checking_with_logging(self):
        """Test permission checking with audit logging"""
        # Grant permission
        self.user.can_access_payroll = True
        self.user.save()
        
        # Check permission
        has_permission = check_permission(self.user, 'view_payroll')
        self.assertTrue(has_permission)
        
        # Check non-existent permission
        has_permission = check_permission(self.user, 'delete_everything')
        self.assertFalse(has_permission)
    
    def test_input_sanitization_integration(self):
        """Test input sanitization integration"""
        malicious_inputs = [
            "<script>alert('XSS')</script>",
            "'; DROP TABLE users; --",
            "javascript:alert('XSS')",
            "eval('malicious code')"
        ]
        
        for malicious_input in malicious_inputs:
            sanitized = sanitize_input(malicious_input, 'text')
            
            # Verify dangerous content is removed
            self.assertNotIn('<script>', sanitized)
            self.assertNotIn('javascript:', sanitized)
            self.assertNotIn('eval(', sanitized)
            self.assertNotIn('DROP TABLE', sanitized)
    
    def test_security_event_logging(self):
        """Test security event logging integration"""
        request = self.factory.get('/')
        
        log_security_event('TEST_EVENT', self.user, request, {'test': 'data'})
        
        # Verify log was created
        logs = SecurityAuditLog.objects.filter(
            user=self.user,
            event_type='TEST_EVENT'
        )
        self.assertEqual(logs.count(), 1)
        
        log = logs.first()
        self.assertEqual(log.additional_data['test'], 'data')
    
    def test_rate_limiting_integration(self):
        """Test rate limiting integration"""
        request = self.factory.post('/')
        
        # Make multiple rapid authentication attempts
        for i in range(SecurityConfig.RATE_LIMIT_LOGIN + 1):
            result = authenticate_user('nonexistent', 'wrongpassword', request)
            
            if i < SecurityConfig.RATE_LIMIT_LOGIN:
                self.assertEqual(result['error_code'], 'INVALID_CREDENTIALS')
            else:
                self.assertEqual(result['error_code'], 'RATE_LIMITED')


# Performance and stress tests
class SecurityPerformanceTest(TestCase):
    """Performance tests for security utilities"""
    
    def test_password_validation_performance(self):
        """Test password validation performance"""
        passwords = [f"TestPassword{i}!" for i in range(100)]
        
        start_time = time.time()
        for password in passwords:
            PasswordValidator.validate_password_strength(password)
        end_time = time.time()
        
        # Should complete 100 validations in reasonable time
        self.assertLess(end_time - start_time, 5.0)  # 5 seconds max
    
    def test_permission_checking_performance(self):
        """Test permission checking performance with caching"""
        self.user = User.objects.create_user(username='perfuser', password='test')
        auth_manager = AuthorizationManager()
        
        # Test permission checking performance
        start_time = time.time()
        for i in range(100):
            auth_manager.check_permission(self.user, 'test.permission')
        end_time = time.time()
        
        # Should benefit from caching
        self.assertLess(end_time - start_time, 1.0)  # 1 second max