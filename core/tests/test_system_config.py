import pytest
from django.test import TestCase
from django.contrib.auth import get_user_model
from django.core.exceptions import ValidationError
from decimal import Decimal
from datetime import date, datetime
from core.models import SystemParameters, User


class SystemParametersModelTest(TestCase):
    def setUp(self):
        self.system_params = SystemParameters.objects.create(
            company_name="ELIYA Mining Corporation",
            company_activity="Mining and Mineral Extraction",
            company_manager="Ahmed Mohamed",
            manager_title="Chief Executive Officer",
            currency="MRU",
            minimum_wage=Decimal('30000.00'),
            default_working_days=Decimal('26.00'),
            tax_abatement=Decimal('10000.00'),
            non_taxable_allowance_ceiling=Decimal('50000.00'),
            current_period=date(2024, 1, 1),
            next_period=date(2024, 2, 1),
            closure_period=date(2023, 12, 31),
            net_account=12345678,
            cnss_number="12345",
            cnam_number="67890",
            its_number="11111"
        )
    
    def test_system_parameters_creation(self):
        """Test basic creation of SystemParameters"""
        self.assertEqual(self.system_params.company_name, "ELIYA Mining Corporation")
        self.assertEqual(self.system_params.company_activity, "Mining and Mineral Extraction")
        self.assertEqual(self.system_params.company_manager, "Ahmed Mohamed")
        self.assertEqual(self.system_params.manager_title, "Chief Executive Officer")
        self.assertEqual(self.system_params.currency, "MRU")
        self.assertEqual(self.system_params.minimum_wage, Decimal('30000.00'))
        self.assertEqual(self.system_params.default_working_days, Decimal('26.00'))
        self.assertEqual(self.system_params.tax_abatement, Decimal('10000.00'))
        self.assertEqual(self.system_params.non_taxable_allowance_ceiling, Decimal('50000.00'))
        self.assertEqual(self.system_params.current_period, date(2024, 1, 1))
        self.assertEqual(self.system_params.next_period, date(2024, 2, 1))
        self.assertEqual(self.system_params.closure_period, date(2023, 12, 31))
        self.assertEqual(self.system_params.net_account, 12345678)
        self.assertEqual(self.system_params.cnss_number, "12345")
        self.assertEqual(self.system_params.cnam_number, "67890")
        self.assertEqual(self.system_params.its_number, "11111")
    
    def test_system_parameters_str(self):
        """Test string representation"""
        expected = "System Parameters for ELIYA Mining Corporation"
        self.assertEqual(str(self.system_params), expected)
    
    def test_system_parameters_db_table(self):
        """Test correct database table name"""
        self.assertEqual(SystemParameters._meta.db_table, 'paramgen')
    
    def test_system_parameters_boolean_defaults(self):
        """Test boolean field defaults"""
        self.assertFalse(self.system_params.auto_meal_allowance)
        self.assertFalse(self.system_params.auto_seniority)
        self.assertFalse(self.system_params.auto_housing_allowance)
        self.assertFalse(self.system_params.deduct_cnss_from_its)
        self.assertFalse(self.system_params.deduct_cnam_from_its)
        self.assertFalse(self.system_params.special_seniority)
        self.assertFalse(self.system_params.its_reimbursement)
        self.assertFalse(self.system_params.smtp_tls_enabled)
        self.assertFalse(self.system_params.apply_compensatory_allowance)
        self.assertFalse(self.system_params.add_current_salary_to_leave_cumul)
        self.assertFalse(self.system_params.deduct_commitments_on_leave)
    
    def test_system_parameters_with_automation_settings(self):
        """Test creation with automation settings enabled"""
        params = SystemParameters.objects.create(
            company_name="Test Company",
            current_period=date(2024, 1, 1),
            next_period=date(2024, 2, 1),
            closure_period=date(2023, 12, 31),
            net_account=123456,
            default_working_days=Decimal('26.00'),
            non_taxable_allowance_ceiling=Decimal('50000.00'),
            auto_meal_allowance=True,
            auto_seniority=True,
            auto_housing_allowance=True,
            deduct_cnss_from_its=True,
            deduct_cnam_from_its=True
        )
        
        self.assertTrue(params.auto_meal_allowance)
        self.assertTrue(params.auto_seniority)
        self.assertTrue(params.auto_housing_allowance)
        self.assertTrue(params.deduct_cnss_from_its)
        self.assertTrue(params.deduct_cnam_from_its)
    
    def test_system_parameters_with_accounting_accounts(self):
        """Test creation with full accounting configuration"""
        params = SystemParameters.objects.create(
            company_name="Accounting Test Company",
            current_period=date(2024, 1, 1),
            next_period=date(2024, 2, 1),
            closure_period=date(2023, 12, 31),
            net_account=12345678,
            default_working_days=Decimal('26.00'),
            non_taxable_allowance_ceiling=Decimal('50000.00'),
            net_chapter=1,
            net_key="NET001",
            its_account=87654321,
            its_chapter=2,
            its_key="ITS001",
            cnss_account=11111111,
            cnss_chapter=3,
            cnss_key="CNSS001",
            cnam_account=22222222,
            cnam_chapter=4,
            cnam_key="CNAM001"
        )
        
        self.assertEqual(params.net_account, 12345678)
        self.assertEqual(params.net_chapter, 1)
        self.assertEqual(params.net_key, "NET001")
        self.assertEqual(params.its_account, 87654321)
        self.assertEqual(params.its_chapter, 2)
        self.assertEqual(params.its_key, "ITS001")
        self.assertEqual(params.cnss_account, 11111111)
        self.assertEqual(params.cnss_chapter, 3)
        self.assertEqual(params.cnss_key, "CNSS001")
        self.assertEqual(params.cnam_account, 22222222)
        self.assertEqual(params.cnam_chapter, 4)
        self.assertEqual(params.cnam_key, "CNAM001")
    
    def test_system_parameters_with_email_config(self):
        """Test creation with email configuration"""
        params = SystemParameters.objects.create(
            company_name="Email Test Company",
            current_period=date(2024, 1, 1),
            next_period=date(2024, 2, 1),
            closure_period=date(2023, 12, 31),
            net_account=123456,
            default_working_days=Decimal('26.00'),
            non_taxable_allowance_ceiling=Decimal('50000.00'),
            smtp_host="smtp.company.com",
            mail_user="payroll@company.com",
            mail_password="securepassword",
            smtp_port="587",
            smtp_tls_enabled=True
        )
        
        self.assertEqual(params.smtp_host, "smtp.company.com")
        self.assertEqual(params.mail_user, "payroll@company.com")
        self.assertEqual(params.mail_password, "securepassword")
        self.assertEqual(params.smtp_port, "587")
        self.assertTrue(params.smtp_tls_enabled)
    
    def test_system_parameters_last_update_auto(self):
        """Test that last_update is automatically set"""
        self.assertIsNotNone(self.system_params.last_update)
        self.assertIsInstance(self.system_params.last_update, datetime)


class UserModelTest(TestCase):
    def setUp(self):
        self.user = User.objects.create_user(
            username="testuser",
            password="testpassword123",
            email="test@company.com",
            full_name="Test User Full Name"
        )
    
    def test_user_creation(self):
        """Test basic user creation"""
        self.assertEqual(self.user.username, "testuser")
        self.assertEqual(self.user.full_name, "Test User Full Name")
        self.assertEqual(self.user.email, "test@company.com")
        self.assertTrue(self.user.check_password("testpassword123"))
    
    def test_user_str(self):
        """Test string representation"""
        expected = "Test User Full Name (testuser)"
        self.assertEqual(str(self.user), expected)
    
    def test_user_db_table(self):
        """Test correct database table name"""
        self.assertEqual(User._meta.db_table, 'utilisateurs')
    
    def test_user_ordering(self):
        """Test user ordering by full_name"""
        user2 = User.objects.create_user(
            username="anotheruser",
            password="password123",
            full_name="Another User"
        )
        user3 = User.objects.create_user(
            username="zuser",
            password="password123",
            full_name="Z User"
        )
        
        users = list(User.objects.all())
        self.assertEqual(users[0].full_name, "Another User")
        self.assertEqual(users[1].full_name, "Test User Full Name")
        self.assertEqual(users[2].full_name, "Z User")
    
    def test_user_permission_defaults(self):
        """Test that all permission fields default to False"""
        # Basic CRUD permissions
        self.assertFalse(self.user.can_add)
        self.assertFalse(self.user.can_update)
        self.assertFalse(self.user.can_delete)
        
        # Module access permissions
        self.assertFalse(self.user.can_access_personnel)
        self.assertFalse(self.user.can_access_payroll)
        self.assertFalse(self.user.can_access_attendance)
        self.assertFalse(self.user.can_access_declarations)
        self.assertFalse(self.user.can_access_accounting)
        self.assertFalse(self.user.can_access_transfers)
        self.assertFalse(self.user.can_access_payslips)
        self.assertFalse(self.user.can_access_reports)
        self.assertFalse(self.user.can_access_statistics)
        self.assertFalse(self.user.can_access_security)
        self.assertFalse(self.user.can_access_parameters)
        self.assertFalse(self.user.can_access_structures)
        self.assertFalse(self.user.can_access_elements)
        self.assertFalse(self.user.can_access_closure)
        self.assertFalse(self.user.can_access_annual_declarations)
        self.assertFalse(self.user.can_access_cumulative_reports)
        self.assertFalse(self.user.can_access_file_import)
        self.assertFalse(self.user.can_access_licensing)
        
        # Detailed employee management permissions
        self.assertFalse(self.user.can_manage_employee_identity)
        self.assertFalse(self.user.can_manage_employee_diplomas)
        self.assertFalse(self.user.can_manage_employee_contracts)
        self.assertFalse(self.user.can_manage_employee_deductions)
        self.assertFalse(self.user.can_manage_employee_leave)
        self.assertFalse(self.user.can_manage_employee_overtime)
        self.assertFalse(self.user.can_manage_employee_payroll)
        self.assertFalse(self.user.can_add_employees)
        self.assertFalse(self.user.can_update_employees)
        self.assertFalse(self.user.can_manage_employee_documents)
        
        # Configuration access permissions
        self.assertFalse(self.user.can_manage_salary_grids)
        self.assertFalse(self.user.can_manage_housing_grids)
        self.assertFalse(self.user.can_manage_origins)
        self.assertFalse(self.user.can_suppress_salaries)
        self.assertFalse(self.user.can_manage_payroll_motifs)
        
        # Dashboard access
        self.assertFalse(self.user.can_access_dashboard)
    
    def test_user_with_full_permissions(self):
        """Test creating user with full permissions"""
        admin_user = User.objects.create_user(
            username="adminuser",
            password="adminpass123",
            full_name="Administrator User",
            # Basic CRUD permissions
            can_add=True,
            can_update=True,
            can_delete=True,
            # Module access permissions
            can_access_personnel=True,
            can_access_payroll=True,
            can_access_attendance=True,
            can_access_declarations=True,
            can_access_accounting=True,
            can_access_security=True,
            can_access_parameters=True,
            can_access_elements=True,
            can_access_closure=True,
            can_access_dashboard=True,
            # Employee management permissions
            can_manage_employee_identity=True,
            can_manage_employee_payroll=True,
            can_add_employees=True,
            can_update_employees=True,
            # Configuration permissions
            can_manage_salary_grids=True,
            can_manage_housing_grids=True,
        )
        
        # Verify basic CRUD permissions
        self.assertTrue(admin_user.can_add)
        self.assertTrue(admin_user.can_update)
        self.assertTrue(admin_user.can_delete)
        
        # Verify module access permissions
        self.assertTrue(admin_user.can_access_personnel)
        self.assertTrue(admin_user.can_access_payroll)
        self.assertTrue(admin_user.can_access_attendance)
        self.assertTrue(admin_user.can_access_declarations)
        self.assertTrue(admin_user.can_access_accounting)
        self.assertTrue(admin_user.can_access_security)
        self.assertTrue(admin_user.can_access_parameters)
        self.assertTrue(admin_user.can_access_elements)
        self.assertTrue(admin_user.can_access_closure)
        self.assertTrue(admin_user.can_access_dashboard)
        
        # Verify employee management permissions
        self.assertTrue(admin_user.can_manage_employee_identity)
        self.assertTrue(admin_user.can_manage_employee_payroll)
        self.assertTrue(admin_user.can_add_employees)
        self.assertTrue(admin_user.can_update_employees)
        
        # Verify configuration permissions
        self.assertTrue(admin_user.can_manage_salary_grids)
        self.assertTrue(admin_user.can_manage_housing_grids)
    
    def test_user_get_full_name(self):
        """Test get_full_name method"""
        self.assertEqual(self.user.get_full_name(), "Test User Full Name")
    
    def test_user_get_short_name(self):
        """Test get_short_name method"""
        self.assertEqual(self.user.get_short_name(), "testuser")
    
    def test_user_username_max_length(self):
        """Test username field max length matches Java entity"""
        self.assertEqual(User._meta.get_field('username').max_length, 15)
    
    def test_user_full_name_max_length(self):
        """Test full_name field max length matches Java entity"""
        self.assertEqual(User._meta.get_field('full_name').max_length, 60)
    
    def test_user_last_session_auto_update(self):
        """Test that last_session is automatically updated"""
        self.assertIsNotNone(self.user.last_session)
        self.assertIsInstance(self.user.last_session, datetime)


class SystemConfigModelsIntegrationTest(TestCase):
    def test_system_parameters_singleton_pattern(self):
        """Test that typically only one SystemParameters instance exists"""
        # Create first instance
        params1 = SystemParameters.objects.create(
            company_name="Company One",
            current_period=date(2024, 1, 1),
            next_period=date(2024, 2, 1),
            closure_period=date(2023, 12, 31),
            net_account=123456,
            default_working_days=Decimal('26.00'),
            non_taxable_allowance_ceiling=Decimal('50000.00')
        )
        
        # Create second instance (allowed but typically wouldn't happen)
        params2 = SystemParameters.objects.create(
            company_name="Company Two",
            current_period=date(2024, 1, 1),
            next_period=date(2024, 2, 1),
            closure_period=date(2023, 12, 31),
            net_account=654321,
            default_working_days=Decimal('26.00'),
            non_taxable_allowance_ceiling=Decimal('50000.00')
        )
        
        # Both should exist
        self.assertEqual(SystemParameters.objects.count(), 2)
        self.assertEqual(params1.company_name, "Company One")
        self.assertEqual(params2.company_name, "Company Two")
    
    def test_user_system_parameters_relationship(self):
        """Test that users can be created independently of system parameters"""
        # Create system parameters
        SystemParameters.objects.create(
            company_name="Test Company",
            current_period=date(2024, 1, 1),
            next_period=date(2024, 2, 1),
            closure_period=date(2023, 12, 31),
            net_account=123456,
            default_working_days=Decimal('26.00'),
            non_taxable_allowance_ceiling=Decimal('50000.00')
        )
        
        # Create users
        user1 = User.objects.create_user(
            username="user1",
            password="pass123",
            full_name="User One"
        )
        user2 = User.objects.create_user(
            username="user2",
            password="pass123",
            full_name="User Two"
        )
        
        self.assertEqual(User.objects.count(), 2)
        self.assertEqual(SystemParameters.objects.count(), 1)
    
    def test_all_models_have_correct_db_tables(self):
        """Verify all models use correct database table names"""
        self.assertEqual(SystemParameters._meta.db_table, 'paramgen')
        self.assertEqual(User._meta.db_table, 'utilisateurs')
    
    def test_models_verbose_names(self):
        """Test verbose names for admin interface"""
        self.assertEqual(SystemParameters._meta.verbose_name, 'System Parameters')
        self.assertEqual(SystemParameters._meta.verbose_name_plural, 'System Parameters')
    
    def test_decimal_field_precision(self):
        """Test decimal fields have correct precision and scale"""
        minimum_wage_field = SystemParameters._meta.get_field('minimum_wage')
        self.assertEqual(minimum_wage_field.max_digits, 22)
        self.assertEqual(minimum_wage_field.decimal_places, 2)
        
        default_working_days_field = SystemParameters._meta.get_field('default_working_days')
        self.assertEqual(default_working_days_field.max_digits, 22)
        self.assertEqual(default_working_days_field.decimal_places, 2)
    
    def test_create_realistic_payroll_system_config(self):
        """Test creating a realistic payroll system configuration"""
        # Create system parameters
        params = SystemParameters.objects.create(
            company_name="ELIYA Mining Corporation",
            company_activity="Iron Ore Mining and Processing",
            company_manager="Ahmed Ould Mohamed",
            manager_title="General Manager",
            currency="MRU",
            minimum_wage=Decimal('30000.00'),
            default_working_days=Decimal('26.00'),
            tax_abatement=Decimal('10000.00'),
            non_taxable_allowance_ceiling=Decimal('50000.00'),
            current_period=date(2024, 1, 1),
            next_period=date(2024, 2, 1),
            closure_period=date(2023, 12, 31),
            net_account=4111000,
            cnss_number="12345",
            cnam_number="67890",
            its_number="11111",
            auto_meal_allowance=True,
            auto_seniority=True,
            smtp_host="mail.eliya.mr",
            mail_user="payroll@eliya.mr"
        )
        
        # Create admin user
        admin = User.objects.create_user(
            username="admin",
            password="admin123",
            full_name="System Administrator",
            is_superuser=True,
            is_staff=True,
            can_add=True,
            can_update=True,
            can_delete=True,
            can_access_personnel=True,
            can_access_payroll=True,
            can_access_security=True,
            can_access_parameters=True,
            can_access_dashboard=True
        )
        
        # Create HR user
        hr_user = User.objects.create_user(
            username="hruser",
            password="hr123",
            full_name="Human Resources Manager",
            can_access_personnel=True,
            can_manage_employee_identity=True,
            can_manage_employee_contracts=True,
            can_add_employees=True,
            can_update_employees=True,
            can_access_dashboard=True
        )
        
        # Create payroll user
        payroll_user = User.objects.create_user(
            username="payroll",
            password="pay123",
            full_name="Payroll Specialist",
            can_access_payroll=True,
            can_access_attendance=True,
            can_manage_employee_payroll=True,
            can_access_reports=True,
            can_access_dashboard=True
        )
        
        # Verify everything was created correctly
        self.assertEqual(SystemParameters.objects.count(), 1)
        self.assertEqual(User.objects.count(), 3)
        
        # Verify system parameters
        self.assertEqual(params.company_name, "ELIYA Mining Corporation")
        self.assertTrue(params.auto_meal_allowance)
        self.assertTrue(params.auto_seniority)
        
        # Verify user permissions
        self.assertTrue(admin.is_superuser)
        self.assertTrue(admin.can_access_security)
        self.assertTrue(hr_user.can_manage_employee_identity)
        self.assertFalse(hr_user.can_access_payroll)
        self.assertTrue(payroll_user.can_access_payroll)
        self.assertFalse(payroll_user.can_access_security)