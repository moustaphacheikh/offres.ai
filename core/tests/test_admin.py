"""
Tests for Django admin interface functionality.

This module tests admin customizations, actions, and display methods
to ensure complete coverage of admin.py functionality.
"""

from django.test import TestCase, RequestFactory
from django.contrib.admin.sites import AdminSite
from django.contrib.auth import get_user_model
from django.utils import timezone
from datetime import timedelta
from decimal import Decimal

from core.admin import (
    GeneralDirectionAdmin, DirectionAdmin, DepartmentAdmin, PositionAdmin,
    EmployeeAdmin, TimeClockDataAdmin, DailyWorkAdmin, WeeklyOvertimeAdmin, 
    WorkWeekAdmin, CNSSDeclarationAdmin, CNAMDeclarationAdmin,
    MasterPieceAdmin, DetailPieceAdmin, DetailPieceInline
)
from core.models import (
    GeneralDirection, Direction, Department, Position, Employee,
    TimeClockData, DailyWork, WeeklyOvertime, WorkWeek,
    CNSSDeclaration, CNAMDeclaration, MasterPiece, DetailPiece,
    EmployeeStatus, PayrollMotif, Activity, Bank, Origin
)

User = get_user_model()


class BaseAdminTestCase(TestCase):
    """Base test case with common setup for admin tests"""
    
    def setUp(self):
        """Set up test data"""
        self.factory = RequestFactory()
        self.user = User.objects.create_user(
            username='testuser',
            password='testpass123',
            email='test@example.com'
        )
        
        # Create test organizational structure
        self.general_direction = GeneralDirection.objects.create(name="Test General Direction")
        self.direction = Direction.objects.create(
            name="Test Direction",
            general_direction=self.general_direction
        )
        self.department = Department.objects.create(
            name="Test Department",
            direction=self.direction
        )
        self.position = Position.objects.create(name="Test Position")
        
        # Create employee status and other required references
        self.employee_status = EmployeeStatus.objects.create(
            name="Active"
        )
        self.activity = Activity.objects.create(name="IT")
        self.bank = Bank.objects.create(name="Test Bank")
        self.origin = Origin.objects.create(label="Nouakchott")
        
        # Create test employee
        self.employee = Employee.objects.create(
            first_name="John",
            last_name="Doe",
            hire_date=timezone.now().date(),
            position=self.position,
            department=self.department,
            status=self.employee_status,
            activity=self.activity,
            bank=self.bank,
            origin=self.origin
        )


class DailyWorkAdminTest(BaseAdminTestCase):
    """Test DailyWorkAdmin functionality"""
    
    def setUp(self):
        super().setUp()
        self.admin = DailyWorkAdmin(DailyWork, AdminSite())
        self.daily_work = DailyWork.objects.create(
            employee=self.employee,
            period=timezone.now().date(),
            work_date=timezone.now().date(),
            day_hours=Decimal('8.00'),
            night_hours=Decimal('2.00')
        )
    
    def test_total_hours_display(self):
        """Test total_hours_display method"""
        display = self.admin.total_hours_display(self.daily_work)
        expected = f"{self.daily_work.total_hours}h"
        self.assertEqual(display, expected)


class WeeklyOvertimeAdminTest(BaseAdminTestCase):
    """Test WeeklyOvertimeAdmin functionality"""
    
    def setUp(self):
        super().setUp()
        self.admin = WeeklyOvertimeAdmin(WeeklyOvertime, AdminSite())
        today = timezone.now().date()
        self.weekly_overtime = WeeklyOvertime.objects.create(
            employee=self.employee,
            period=today,
            week_start=today,
            week_end=today + timedelta(days=6),
            overtime_150=Decimal('5.00'),
            overtime_200=Decimal('3.00')
        )
    
    def test_total_overtime_display(self):
        """Test total_overtime_display method"""
        display = self.admin.total_overtime_display(self.weekly_overtime)
        expected = f"{self.weekly_overtime.total_overtime_hours}h"
        self.assertEqual(display, expected)


class CNSSDeclarationAdminTest(BaseAdminTestCase):
    """Test CNSSDeclarationAdmin functionality"""
    
    def setUp(self):
        super().setUp()
        self.admin = CNSSDeclarationAdmin(CNSSDeclaration, AdminSite())
        self.cnss_declaration = CNSSDeclaration.objects.create(
            employee=self.employee,
            employee_name="John Doe",
            cnss_number="123456789",
            declaration_period=timezone.now().date(),
            working_days_month1=22,
            working_days_month2=21,
            working_days_month3=20,
            actual_remuneration=Decimal('50000.00')
        )
    
    def test_save_model_new_object(self):
        """Test save_model for new objects sets created_by"""
        request = self.factory.post('/')
        request.user = self.user
        
        new_declaration = CNSSDeclaration(
            employee=self.employee,
            employee_name="Jane Doe",
            cnss_number="987654321",
            declaration_period=timezone.now().date() + timedelta(days=32),  # Different period
            actual_remuneration=Decimal('45000.00')
        )
        
        self.admin.save_model(request, new_declaration, None, change=False)
        
        self.assertEqual(new_declaration.created_by, self.user.username)
        self.assertEqual(new_declaration.updated_by, self.user.username)
    
    def test_save_model_existing_object(self):
        """Test save_model for existing objects sets updated_by only"""
        request = self.factory.post('/')
        request.user = self.user
        
        original_created_by = self.cnss_declaration.created_by
        self.admin.save_model(request, self.cnss_declaration, None, change=True)
        
        self.assertEqual(self.cnss_declaration.created_by, original_created_by)
        self.assertEqual(self.cnss_declaration.updated_by, self.user.username)


class CNAMDeclarationAdminTest(BaseAdminTestCase):
    """Test CNAMDeclarationAdmin functionality"""
    
    def setUp(self):
        super().setUp()
        self.admin = CNAMDeclarationAdmin(CNAMDeclaration, AdminSite())
        self.cnam_declaration = CNAMDeclaration.objects.create(
            employee=self.employee,
            employee_name="John Doe",
            cnam_number="CNAM123456",
            nni="1234567890",
            declaration_period=timezone.now().date(),
            employee_function_number=1001,
            taxable_base_month1=Decimal('25000.00'),
            working_days_month1=22
        )
    
    def test_save_model_new_object(self):
        """Test save_model for new objects sets created_by"""
        request = self.factory.post('/')
        request.user = self.user
        
        new_declaration = CNAMDeclaration(
            employee=self.employee,
            employee_name="Jane Doe",
            cnam_number="CNAM789012",
            nni="0987654321",
            declaration_period=timezone.now().date() + timedelta(days=32),  # Different period
            employee_function_number=1002
        )
        
        self.admin.save_model(request, new_declaration, None, change=False)
        
        self.assertEqual(new_declaration.created_by, self.user.username)
        self.assertEqual(new_declaration.updated_by, self.user.username)
    
    def test_save_model_existing_object(self):
        """Test save_model for existing objects sets updated_by only"""
        request = self.factory.post('/')
        request.user = self.user
        
        original_created_by = self.cnam_declaration.created_by
        self.admin.save_model(request, self.cnam_declaration, None, change=True)
        
        self.assertEqual(self.cnam_declaration.created_by, original_created_by)
        self.assertEqual(self.cnam_declaration.updated_by, self.user.username)


class MasterPieceAdminTest(BaseAdminTestCase):
    """Test MasterPieceAdmin functionality"""
    
    def setUp(self):
        super().setUp()
        self.admin = MasterPieceAdmin(MasterPiece, AdminSite())
        self.payroll_motif = PayrollMotif.objects.create(
            name="Regular Payroll"
        )
        self.master_piece = MasterPiece.objects.create(
            numero="MP002",  # Different numero
            period="202312",
            motif="Regular Payroll",
            rubrique="Test Rubrique",
            dateop=timezone.now().date(),
            libelle_service="Test Service",
            total_debit=Decimal('100000.00'),
            total_credit=Decimal('100000.00'),
            initiateur="Test User",
            init_hr=timezone.now()
        )
    
    def test_recalculate_totals_action(self):
        """Test recalculate_totals admin action"""
        request = self.factory.post('/')
        request.user = self.user
        request._messages = []  # Mock messages framework
        
        queryset = MasterPiece.objects.filter(numero=self.master_piece.numero)
        
        # Mock the message_user method
        def mock_message_user(request, message):
            request._messages.append(message)
        
        self.admin.message_user = mock_message_user
        
        self.admin.recalculate_totals(request, queryset)
        
        self.assertEqual(len(request._messages), 1)
        self.assertIn("Recalculated totals for 1 master pieces", request._messages[0])
    
    def test_mark_as_validated_action(self):
        """Test mark_as_validated admin action"""
        request = self.factory.post('/')
        request.user = self.user
        request._messages = []
        
        queryset = MasterPiece.objects.filter(numero=self.master_piece.numero)
        
        def mock_message_user(request, message):
            request._messages.append(message)
        
        self.admin.message_user = mock_message_user
        
        self.admin.mark_as_validated(request, queryset)
        
        self.master_piece.refresh_from_db()
        self.assertEqual(self.master_piece.status, 'VALIDATED')
        self.assertEqual(len(request._messages), 1)
        self.assertIn("Marked 1 master pieces as validated", request._messages[0])
    
    def test_mark_as_exported_action(self):
        """Test mark_as_exported admin action"""
        request = self.factory.post('/')
        request.user = self.user
        request._messages = []
        
        queryset = MasterPiece.objects.filter(numero=self.master_piece.numero)
        
        def mock_message_user(request, message):
            request._messages.append(message)
        
        self.admin.message_user = mock_message_user
        
        self.admin.mark_as_exported(request, queryset)
        
        self.master_piece.refresh_from_db()
        self.assertEqual(self.master_piece.status, 'EXPORTED')
        self.assertEqual(len(request._messages), 1)
        self.assertIn("Marked 1 master pieces as exported", request._messages[0])


class DetailPieceAdminTest(BaseAdminTestCase):
    """Test DetailPieceAdmin functionality"""
    
    def setUp(self):
        super().setUp()
        self.admin = DetailPieceAdmin(DetailPiece, AdminSite())
        self.payroll_motif = PayrollMotif.objects.create(
            name="Regular Payroll"
        )
        self.master_piece = MasterPiece.objects.create(
            numero="MP003",
            period="202312",
            motif="Regular Payroll",
            rubrique="Test Rubrique",
            dateop=timezone.now().date(),
            libelle_service="Test Service",
            total_debit=Decimal('100000.00'),
            total_credit=Decimal('100000.00'),
            initiateur="Test User",
            init_hr=timezone.now()
        )
        self.detail_piece = DetailPiece.objects.create(
            nupiece=self.master_piece,
            dateop=timezone.now().date(),
            compte="411001",
            libelle="Test Account",
            montant=Decimal('50000.00'),
            sens='D',
            employee=self.employee
        )
    
    def test_mark_as_exported_action(self):
        """Test mark_as_exported admin action"""
        request = self.factory.post('/')
        request.user = self.user
        request._messages = []
        
        queryset = DetailPiece.objects.filter(numligne=self.detail_piece.numligne)
        
        def mock_message_user(request, message):
            request._messages.append(message)
        
        self.admin.message_user = mock_message_user
        
        self.admin.mark_as_exported(request, queryset)
        
        self.detail_piece.refresh_from_db()
        self.assertTrue(self.detail_piece.exported)
        self.assertEqual(len(request._messages), 1)
        self.assertIn("Marked 1 detail pieces as exported", request._messages[0])
    
    def test_mark_as_not_exported_action(self):
        """Test mark_as_not_exported admin action"""
        # First mark as exported
        self.detail_piece.exported = True
        self.detail_piece.export_reference = "EXP001"
        self.detail_piece.save()
        
        request = self.factory.post('/')
        request.user = self.user
        request._messages = []
        
        queryset = DetailPiece.objects.filter(numligne=self.detail_piece.numligne)
        
        def mock_message_user(request, message):
            request._messages.append(message)
        
        self.admin.message_user = mock_message_user
        
        self.admin.mark_as_not_exported(request, queryset)
        
        self.detail_piece.refresh_from_db()
        self.assertFalse(self.detail_piece.exported)
        self.assertEqual(self.detail_piece.export_reference, '')
        self.assertEqual(len(request._messages), 1)
        self.assertIn("Marked 1 detail pieces as not exported", request._messages[0])
    
    def test_get_queryset_optimization(self):
        """Test get_queryset includes select_related optimization"""
        request = self.factory.get('/')
        request.user = self.user
        
        queryset = self.admin.get_queryset(request)
        
        # Check that the queryset has select_related applied
        self.assertIn('nupiece', queryset.query.select_related)
        self.assertIn('employee', queryset.query.select_related)


class DetailPieceInlineTest(BaseAdminTestCase):
    """Test DetailPieceInline functionality"""
    
    def setUp(self):
        super().setUp()
        self.inline = DetailPieceInline(DetailPiece, AdminSite())
        self.payroll_motif = PayrollMotif.objects.create(
            name="Regular Payroll"
        )
        self.master_piece = MasterPiece.objects.create(
            numero="MP004",
            period="202312",
            motif="Regular Payroll",
            rubrique="Test Rubrique",
            dateop=timezone.now().date(),
            libelle_service="Test Service",
            total_debit=Decimal('100000.00'),
            total_credit=Decimal('100000.00'),
            initiateur="Test User",
            init_hr=timezone.now()
        )
        self.detail_piece = DetailPiece.objects.create(
            nupiece=self.master_piece,
            dateop=timezone.now().date(),
            compte="411001",
            libelle="Test Account",
            montant=Decimal('50000.00'),
            sens='D',
            employee=self.employee
        )
    
    def test_get_queryset_optimization(self):
        """Test get_queryset includes select_related optimization"""
        request = self.factory.get('/')
        request.user = self.user
        
        queryset = self.inline.get_queryset(request)
        
        # Check that the queryset has select_related applied
        self.assertIn('employee', queryset.query.select_related)


class AdminIntegrationTest(BaseAdminTestCase):
    """Integration tests for admin functionality"""
    
    def test_all_admin_classes_registered(self):
        """Test that all expected admin classes are properly configured"""
        from django.contrib import admin
        
        # Check key models are registered
        self.assertIn(Employee, admin.site._registry)
        self.assertIn(MasterPiece, admin.site._registry)
        self.assertIn(DetailPiece, admin.site._registry)
        self.assertIn(CNSSDeclaration, admin.site._registry)
        self.assertIn(CNAMDeclaration, admin.site._registry)
    
    def test_admin_display_methods_work(self):
        """Test that admin display methods don't raise errors"""
        # Test DailyWork admin
        daily_work = DailyWork.objects.create(
            employee=self.employee,
            period=timezone.now().date(),
            work_date=timezone.now().date(),
            day_hours=Decimal('8.00')
        )
        
        daily_admin = DailyWorkAdmin(DailyWork, AdminSite())
        display = daily_admin.total_hours_display(daily_work)
        self.assertIsInstance(display, str)
        self.assertIn('h', display)
        
        # Test WeeklyOvertime admin
        today = timezone.now().date()
        weekly_overtime = WeeklyOvertime.objects.create(
            employee=self.employee,
            period=today,
            week_start=today,
            week_end=today + timedelta(days=6),
            overtime_150=Decimal('2.00')
        )
        
        overtime_admin = WeeklyOvertimeAdmin(WeeklyOvertime, AdminSite())
        display = overtime_admin.total_overtime_display(weekly_overtime)
        self.assertIsInstance(display, str)
        self.assertIn('h', display)
    
    def test_admin_readonly_fields(self):
        """Test that readonly fields are properly configured"""
        cnss_admin = CNSSDeclarationAdmin(CNSSDeclaration, AdminSite())
        self.assertIn('created_at', cnss_admin.readonly_fields)
        self.assertIn('updated_at', cnss_admin.readonly_fields)
        
        cnam_admin = CNAMDeclarationAdmin(CNAMDeclaration, AdminSite())
        self.assertIn('created_at', cnam_admin.readonly_fields)
        self.assertIn('updated_at', cnam_admin.readonly_fields)
        
        master_admin = MasterPieceAdmin(MasterPiece, AdminSite())
        self.assertIn('balance_difference', master_admin.readonly_fields)
        self.assertIn('is_balanced', master_admin.readonly_fields)
        self.assertIn('created_at', master_admin.readonly_fields)
        self.assertIn('updated_at', master_admin.readonly_fields)
    
    def test_admin_fieldsets(self):
        """Test that fieldsets are properly configured"""
        cnss_admin = CNSSDeclarationAdmin(CNSSDeclaration, AdminSite())
        self.assertTrue(hasattr(cnss_admin, 'fieldsets'))
        self.assertGreater(len(cnss_admin.fieldsets), 0)
        
        cnam_admin = CNAMDeclarationAdmin(CNAMDeclaration, AdminSite())
        self.assertTrue(hasattr(cnam_admin, 'fieldsets'))
        self.assertGreater(len(cnam_admin.fieldsets), 0)
        
        master_admin = MasterPieceAdmin(MasterPiece, AdminSite())
        self.assertTrue(hasattr(master_admin, 'fieldsets'))
        self.assertGreater(len(master_admin.fieldsets), 0)
    
    def test_admin_actions_exist(self):
        """Test that admin actions are properly configured"""
        master_admin = MasterPieceAdmin(MasterPiece, AdminSite())
        actions = [action.__name__ if callable(action) else action for action in master_admin.actions]
        self.assertIn('recalculate_totals', actions)
        self.assertIn('mark_as_validated', actions)
        self.assertIn('mark_as_exported', actions)
        
        detail_admin = DetailPieceAdmin(DetailPiece, AdminSite())
        actions = [action.__name__ if callable(action) else action for action in detail_admin.actions]
        self.assertIn('mark_as_exported', actions)
        self.assertIn('mark_as_not_exported', actions)
    
    def test_inline_configuration(self):
        """Test that inline admin is properly configured"""
        master_admin = MasterPieceAdmin(MasterPiece, AdminSite())
        self.assertTrue(hasattr(master_admin, 'inlines'))
        self.assertIn(DetailPieceInline, master_admin.inlines)
        
        inline = DetailPieceInline(DetailPiece, AdminSite())
        self.assertEqual(inline.extra, 0)
        self.assertIn('cvmro_montant', inline.readonly_fields)
        self.assertIn('formatted_account', inline.readonly_fields)