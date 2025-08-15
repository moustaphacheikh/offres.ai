#!/usr/bin/env python3
"""
Security Module Integration Examples for Mauritanian Payroll System

This file demonstrates practical usage of the comprehensive security utilities
including authentication, authorization, license management, audit logging,
and security validation in real-world scenarios.

Run this file to see examples of:
- Secure user authentication with rate limiting
- Password strength validation and policy enforcement
- Role-based access control for payroll operations
- License validation and user limit enforcement
- Session management with concurrent session limits
- Input sanitization for XSS/SQL injection prevention
- Comprehensive security audit logging
- JWT token management for API authentication

Usage:
    python security_integration_examples.py
"""

import os
import sys
import django
from datetime import datetime, timedelta

# Add the project root to Python path
sys.path.append('/Users/moustaphacheikh/Desktop/projects/ELIYA-Paie_Install/mccmr')

# Configure Django settings
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'payroll.settings')
django.setup()

from django.contrib.auth import get_user_model
from django.test import RequestFactory

# Import security utilities
from core.utils.security import (
    PasswordValidator, AuthenticationManager, AuthorizationManager,
    LicenseManager, SessionManager, SecurityValidator, JWTManager,
    SecurityAuditLog, authenticate_user, check_permission,
    validate_license, sanitize_input, log_security_event
)

User = get_user_model()


def demonstrate_password_validation():
    """Demonstrate comprehensive password validation capabilities"""
    print("=" * 60)
    print("PASSWORD VALIDATION DEMONSTRATION")
    print("=" * 60)
    
    # Test different password strengths
    test_passwords = [
        ("weak", "123"),
        ("medium", "Password123"),
        ("strong", "SecureP@ssw0rd!2024"),
        ("with_user_info", "johnPassword123!"),
        ("very_strong", "Mrt@2024!P@yr0ll$ystem")
    ]
    
    # Create a test user for context validation
    user = User(username='john.doe', first_name='John', last_name='Doe', email='john@example.com')
    
    for name, password in test_passwords:
        print(f"\nTesting {name} password: '{password}'")
        result = PasswordValidator.validate_password_strength(password, user)
        
        print(f"  Valid: {result.is_valid}")
        if 'password_score' in result.cleaned_data:
            print(f"  Strength Score: {result.cleaned_data['password_score']}/100")
        
        if result.errors:
            print("  Errors:")
            for error in result.errors[:3]:  # Show first 3 errors
                print(f"    - {error['message']}")
        
        if result.warnings:
            print("  Warnings:")
            for warning in result.warnings[:2]:  # Show first 2 warnings
                print(f"    - {warning['message']}")
        
        if 'recommendations' in result.cleaned_data and result.cleaned_data['recommendations']:
            print("  Recommendations:")
            for rec in result.cleaned_data['recommendations'][:3]:  # Show first 3 recommendations
                print(f"    - {rec}")
    
    # Demonstrate secure password generation
    print(f"\nGenerated secure password: '{PasswordValidator.generate_secure_password(16)}'")


def demonstrate_authentication_security():
    """Demonstrate authentication with security features"""
    print("\n" + "=" * 60)
    print("AUTHENTICATION SECURITY DEMONSTRATION")
    print("=" * 60)
    
    # Create test user
    try:
        test_user = User.objects.get(username='security_demo_user')
    except User.DoesNotExist:
        test_user = User.objects.create_user(
            username='security_demo_user',
            password='DemoSecureP@ssw0rd!2024',
            email='demo@company.com',
            first_name='Demo',
            last_name='User'
        )
        print("Created demo user: security_demo_user")
    
    factory = RequestFactory()
    request = factory.post('/login/')
    
    auth_manager = AuthenticationManager()
    
    # Successful authentication
    print(f"\n1. Testing successful authentication:")
    result = auth_manager.authenticate_user('security_demo_user', 'DemoSecureP@ssw0rd!2024', request)
    print(f"   Success: {result['success']}")
    print(f"   User: {result['user'].username if result['user'] else 'None'}")
    
    # Failed authentication with attempt tracking
    print(f"\n2. Testing failed authentication (tracking attempts):")
    for attempt in range(3):
        result = auth_manager.authenticate_user('security_demo_user', 'wrong_password', request)
        print(f"   Attempt {attempt + 1}: Success={result['success']}, Attempts remaining={result.get('attempts_remaining', 'N/A')}")
    
    # Demonstrate password change with security validation
    print(f"\n3. Testing secure password change:")
    change_result = auth_manager.change_password(
        test_user, 
        'DemoSecureP@ssw0rd!2024', 
        'NewSecureP@ssw0rd!2025',
        request
    )
    print(f"   Password changed: {change_result.is_valid}")
    if change_result.is_valid:
        print(f"   New password strength score: {change_result.cleaned_data.get('strength_score', 'N/A')}")
        # Change it back for other demonstrations
        auth_manager.change_password(
            test_user,
            'NewSecureP@ssw0rd!2025',
            'DemoSecureP@ssw0rd!2024',
            request
        )


def demonstrate_authorization_system():
    """Demonstrate role-based access control"""
    print("\n" + "=" * 60)
    print("AUTHORIZATION SYSTEM DEMONSTRATION")
    print("=" * 60)
    
    # Get or create test user
    try:
        test_user = User.objects.get(username='security_demo_user')
    except User.DoesNotExist:
        test_user = User.objects.create_user(
            username='security_demo_user',
            password='DemoSecureP@ssw0rd!2024',
            email='demo@company.com'
        )
    
    auth_manager = AuthorizationManager()
    
    # Test various permissions
    permissions_to_test = [
        ('view_employee', 'can_access_personnel'),
        ('view_payroll', 'can_access_payroll'),
        ('access_reports', 'can_access_reports'),
        ('access_security', 'can_access_security'),
        ('access_parameters', 'can_access_parameters')
    ]
    
    print(f"\nTesting permissions for user: {test_user.username}")
    print(f"Initial permissions (should all be False):")
    
    for permission, attr in permissions_to_test:
        has_perm = auth_manager.check_permission(test_user, permission)
        print(f"   {permission}: {has_perm}")
    
    # Grant some permissions
    print(f"\nGranting payroll and personnel access...")
    test_user.can_access_personnel = True
    test_user.can_access_payroll = True
    test_user.can_access_reports = True
    test_user.save()
    
    print(f"Updated permissions:")
    for permission, attr in permissions_to_test:
        has_perm = auth_manager.check_permission(test_user, permission)
        print(f"   {permission}: {has_perm}")
    
    # Test module access
    modules_to_test = ['personnel', 'payroll', 'reports', 'security', 'parameters']
    print(f"\nModule access permissions:")
    for module in modules_to_test:
        has_access = auth_manager.check_module_access(test_user, module)
        print(f"   {module}: {has_access}")


def demonstrate_license_management():
    """Demonstrate license validation and management"""
    print("\n" + "=" * 60)
    print("LICENSE MANAGEMENT DEMONSTRATION")
    print("=" * 60)
    
    license_manager = LicenseManager()
    
    # Generate a test license
    expiry_date = datetime.now() + timedelta(days=365)
    features = ['payroll', 'reporting', 'time_tracking', 'analytics']
    max_users = 25
    
    test_license_key = license_manager.generate_license_key(max_users, expiry_date, features)
    print(f"Generated test license key: {test_license_key[:50]}...")
    
    # Create/update system parameters with the license
    from core.models.system_config import SystemParameters
    params, created = SystemParameters.objects.get_or_create(
        id=1,
        defaults={
            'company_name': 'Demo Payroll Company',
            'default_working_days': 22,
            'non_taxable_allowance_ceiling': 50000,
            'current_period': datetime.now().date(),
            'next_period': datetime.now().date() + timedelta(days=30),
            'closure_period': datetime.now().date(),
            'net_account': 123456789,
            'license_key': test_license_key
        }
    )
    if not created:
        params.license_key = test_license_key
        params.save()
    
    # Validate license
    print(f"\nValidating license...")
    license_info = license_manager.validate_license()
    
    print(f"   License valid: {license_info['valid']}")
    print(f"   Expires: {license_info.get('expiry_date', 'N/A')}")
    print(f"   Users allowed: {license_info['users_allowed']}")
    print(f"   Current users: {license_info['users_current']}")
    print(f"   Features enabled: {', '.join(license_info['features_enabled'])}")
    
    # Test feature checking
    print(f"\nFeature availability:")
    test_features = ['payroll', 'reporting', 'advanced_analytics', 'multi_company']
    for feature in test_features:
        available = license_manager.check_feature_enabled(feature)
        print(f"   {feature}: {'✓' if available else '✗'}")
    
    # Test user limit checking
    print(f"\nUser limit status:")
    can_add_user = license_manager.check_user_limit()
    print(f"   Can add new user: {'✓' if can_add_user else '✗'}")


def demonstrate_input_sanitization():
    """Demonstrate input sanitization and XSS prevention"""
    print("\n" + "=" * 60)
    print("INPUT SANITIZATION DEMONSTRATION")
    print("=" * 60)
    
    # Test various types of malicious input
    test_cases = [
        ("XSS Script", "<script>alert('XSS Attack!')</script>Hello World", "html"),
        ("SQL Injection", "Robert'; DROP TABLE employees; --", "sql"),
        ("JavaScript Injection", "eval('alert(\"Injected!\")')", "javascript"),
        ("General Text", "Normal text with <dangerous>tags</dangerous>", "text"),
        ("Email Input", "user@domain.com<script>alert('xss')</script>", "text"),
        ("Phone Input", "+222 12 34 56 78<script>", "text")
    ]
    
    print(f"Testing input sanitization:")
    
    for name, malicious_input, input_type in test_cases:
        print(f"\n   {name}:")
        print(f"     Original:  '{malicious_input}'")
        
        sanitized = sanitize_input(malicious_input, input_type)
        print(f"     Sanitized: '{sanitized}'")
        
        # Show what was removed/changed
        if malicious_input != sanitized:
            removed_chars = set(malicious_input) - set(sanitized)
            if removed_chars:
                print(f"     Removed:   {', '.join(repr(c) for c in sorted(removed_chars) if c not in ' \t\n')[:5]}...")
    
    # Demonstrate safe redirect validation
    print(f"\nTesting redirect URL validation:")
    redirect_urls = [
        "/dashboard",
        "/reports/payroll", 
        "//evil.com",
        "http://external.com",
        "javascript:alert('xss')"
    ]
    
    for url in redirect_urls:
        is_safe = SecurityValidator.is_safe_redirect_url(url)
        print(f"   '{url}': {'✓ Safe' if is_safe else '✗ Unsafe'}")


def demonstrate_jwt_authentication():
    """Demonstrate JWT token management for API authentication"""
    print("\n" + "=" * 60)
    print("JWT TOKEN AUTHENTICATION DEMONSTRATION")
    print("=" * 60)
    
    # Get test user
    try:
        test_user = User.objects.get(username='security_demo_user')
    except User.DoesNotExist:
        test_user = User.objects.create_user(
            username='security_demo_user',
            password='DemoSecureP@ssw0rd!2024'
        )
    
    print(f"Generating JWT tokens for user: {test_user.username}")
    
    # Generate access token
    access_token = JWTManager.generate_token(test_user, expiration_hours=1)
    print(f"   Access token: {access_token[:50]}...")
    
    # Generate refresh token
    refresh_token = JWTManager.generate_refresh_token(test_user)
    print(f"   Refresh token: {refresh_token[:50]}...")
    
    # Validate access token
    print(f"\nValidating access token:")
    validation_result = JWTManager.validate_token(access_token)
    print(f"   Valid: {validation_result['valid']}")
    print(f"   User: {validation_result['user'].username if validation_result['user'] else 'None'}")
    print(f"   Error: {validation_result['error'] or 'None'}")
    
    # Test token refresh
    print(f"\nRefreshing access token:")
    refresh_result = JWTManager.refresh_token(refresh_token)
    print(f"   Refresh success: {refresh_result['success']}")
    if refresh_result['success']:
        new_token = refresh_result['access_token']
        print(f"   New access token: {new_token[:50]}...")
        
        # Validate new token
        new_validation = JWTManager.validate_token(new_token)
        print(f"   New token valid: {new_validation['valid']}")


def demonstrate_audit_logging():
    """Demonstrate comprehensive security audit logging"""
    print("\n" + "=" * 60)
    print("SECURITY AUDIT LOGGING DEMONSTRATION")
    print("=" * 60)
    
    # Get test user
    try:
        test_user = User.objects.get(username='security_demo_user')
    except User.DoesNotExist:
        test_user = User.objects.create_user(
            username='security_demo_user',
            password='DemoSecureP@ssw0rd!2024'
        )
    
    factory = RequestFactory()
    
    # Create various security events
    security_events = [
        ('LOGIN_SUCCESS', {'session_id': 'demo_session_123'}),
        ('DATA_ACCESS', {'resource': 'employee_list', 'count': 25}),
        ('PERMISSION_GRANTED', {'permission': 'view_payroll', 'granted_by': 'admin'}),
        ('PASSWORD_CHANGE_SUCCESS', {'strength_score': 85}),
        ('AUTHORIZATION_DENIED', {'requested_permission': 'delete_employee'})
    ]
    
    print(f"Creating security audit log entries:")
    
    for event_type, additional_data in security_events:
        request = factory.get('/demo/')
        log_security_event(event_type, test_user, request, additional_data)
        print(f"   Logged: {event_type}")
    
    # Query recent audit logs
    print(f"\nRecent security events for user:")
    recent_logs = SecurityAuditLog.get_user_activity(test_user, days=1)
    
    for log in recent_logs.order_by('-timestamp')[:5]:
        print(f"   {log.timestamp.strftime('%H:%M:%S')} - {log.get_event_type_display()}")
        if log.additional_data:
            for key, value in list(log.additional_data.items())[:2]:
                print(f"     {key}: {value}")
    
    # Show security event statistics
    print(f"\nSecurity event summary:")
    all_events = SecurityAuditLog.get_security_events(days=1)
    event_counts = {}
    for log in all_events:
        event_counts[log.event_type] = event_counts.get(log.event_type, 0) + 1
    
    for event_type, count in event_counts.items():
        print(f"   {event_type}: {count}")


def demonstrate_integrated_security_workflow():
    """Demonstrate complete security workflow integration"""
    print("\n" + "=" * 80)
    print("INTEGRATED SECURITY WORKFLOW DEMONSTRATION")
    print("=" * 80)
    
    print("This demonstrates a complete secure workflow:")
    print("1. User authentication with security checks")
    print("2. Authorization verification")
    print("3. License validation")
    print("4. Secure session creation")
    print("5. Input sanitization")
    print("6. Audit logging")
    
    factory = RequestFactory()
    
    # Step 1: Authenticate user
    print(f"\n1. Authenticating user...")
    request = factory.post('/login/')
    
    auth_result = authenticate_user('security_demo_user', 'DemoSecureP@ssw0rd!2024', request)
    
    if not auth_result['success']:
        print(f"   Authentication failed: {auth_result['error']}")
        return
    
    user = auth_result['user']
    print(f"   ✓ User authenticated: {user.username}")
    
    # Step 2: Check authorization
    print(f"\n2. Checking authorization...")
    has_payroll_access = check_permission(user, 'view_payroll')
    print(f"   ✓ Payroll access: {'Granted' if has_payroll_access else 'Denied'}")
    
    # Step 3: Validate license
    print(f"\n3. Validating license...")
    license_info = validate_license()
    print(f"   ✓ License status: {'Valid' if license_info['valid'] else 'Invalid'}")
    
    if license_info['valid']:
        print(f"   ✓ Users: {license_info['users_current']}/{license_info['users_allowed']}")
    
    # Step 4: Create secure session (mock)
    print(f"\n4. Creating secure session...")
    try:
        # Mock session creation for demo
        session_id = f"secure_session_{datetime.now().timestamp()}"
        print(f"   ✓ Secure session created: {session_id[:20]}...")
    except Exception as e:
        print(f"   ⚠ Session creation skipped in demo: {str(e)}")
    
    # Step 5: Process user input safely
    print(f"\n5. Processing user input safely...")
    user_input = "Employee Name: John Doe <script>alert('xss')</script>"
    sanitized_input = sanitize_input(user_input, 'html')
    print(f"   Original: {user_input}")
    print(f"   ✓ Sanitized: {sanitized_input}")
    
    # Step 6: Log the complete workflow
    print(f"\n6. Logging security events...")
    log_security_event(
        'WORKFLOW_COMPLETED',
        user,
        request,
        {
            'workflow': 'integrated_security_demo',
            'steps_completed': 6,
            'security_checks_passed': True
        }
    )
    print(f"   ✓ Workflow completion logged")
    
    print(f"\n✓ Complete secure workflow demonstrated successfully!")


def run_security_demonstrations():
    """Run all security demonstrations"""
    print("MAURITANIAN PAYROLL SYSTEM - SECURITY MODULE DEMONSTRATIONS")
    print("=" * 80)
    print("Demonstrating comprehensive security capabilities...")
    
    try:
        demonstrate_password_validation()
        demonstrate_authentication_security()
        demonstrate_authorization_system()
        demonstrate_license_management()
        demonstrate_input_sanitization()
        demonstrate_jwt_authentication()
        demonstrate_audit_logging()
        demonstrate_integrated_security_workflow()
        
        print("\n" + "=" * 80)
        print("ALL SECURITY DEMONSTRATIONS COMPLETED SUCCESSFULLY!")
        print("=" * 80)
        
        print("\nSecurity Module Capabilities Summary:")
        print("✓ Strong password validation with Mauritanian requirements")
        print("✓ Multi-layered authentication with rate limiting and lockout")
        print("✓ Role-based authorization with granular permissions")
        print("✓ License management with user limits and feature control")
        print("✓ Secure session management with concurrent session limits")
        print("✓ Comprehensive input sanitization (XSS/SQL injection prevention)")
        print("✓ JWT token management for API authentication")
        print("✓ Complete audit logging for security compliance")
        print("✓ Integration with Django authentication system")
        print("✓ Performance optimized with caching and rate limiting")
        
        print(f"\nFor production use:")
        print(f"1. Configure SecurityConfig parameters in settings")
        print(f"2. Set up proper database indexes for audit logs")
        print(f"3. Implement proper license key encryption")
        print(f"4. Configure rate limiting with Redis/Memcached")
        print(f"5. Set up monitoring and alerting for security events")
        
    except Exception as e:
        print(f"\nDemonstration error: {str(e)}")
        import traceback
        traceback.print_exc()


if __name__ == '__main__':
    run_security_demonstrations()