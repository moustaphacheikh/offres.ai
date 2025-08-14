import pytest
from django.test import TestCase
from core.models import Activity, Bank, Origin, EmployeeStatus, PayrollMotif


class ActivityModelTest(TestCase):
    def setUp(self):
        self.activity = Activity.objects.create(
            name="Mining Operations"
        )
    
    def test_activity_creation(self):
        self.assertEqual(self.activity.name, "Mining Operations")
    
    def test_activity_str(self):
        self.assertEqual(str(self.activity), "Mining Operations")
    
    def test_activity_ordering(self):
        a1 = Activity.objects.create(name="Z Activity")
        a2 = Activity.objects.create(name="A Activity")
        
        activities = list(Activity.objects.all())
        self.assertEqual(activities[0].name, "A Activity")
        self.assertEqual(activities[1].name, "Mining Operations")
        self.assertEqual(activities[2].name, "Z Activity")
    
    def test_db_table(self):
        self.assertEqual(Activity._meta.db_table, 'activite')


class BankModelTest(TestCase):
    def setUp(self):
        self.bank = Bank.objects.create(
            name="Banque Mauritanienne",
            accounting_account=12345,
            accounting_chapter=678,
            accounting_key="BM001"
        )
    
    def test_bank_creation(self):
        self.assertEqual(self.bank.name, "Banque Mauritanienne")
        self.assertEqual(self.bank.accounting_account, 12345)
        self.assertEqual(self.bank.accounting_chapter, 678)
        self.assertEqual(self.bank.accounting_key, "BM001")
    
    def test_bank_str(self):
        self.assertEqual(str(self.bank), "Banque Mauritanienne")
    
    def test_bank_optional_fields(self):
        bank = Bank.objects.create(name="Simple Bank")
        self.assertIsNone(bank.accounting_account)
        self.assertIsNone(bank.accounting_chapter)
        self.assertEqual(bank.accounting_key, "")
    
    def test_db_table(self):
        self.assertEqual(Bank._meta.db_table, 'banque')


class OriginModelTest(TestCase):
    def setUp(self):
        self.origin = Origin.objects.create(
            label="Recrutement Local",
            smig_hours_for_leave_allowance=40
        )
    
    def test_origin_creation(self):
        self.assertEqual(self.origin.label, "Recrutement Local")
        self.assertEqual(self.origin.smig_hours_for_leave_allowance, 40)
    
    def test_origin_str(self):
        self.assertEqual(str(self.origin), "Recrutement Local")
    
    def test_origin_optional_fields(self):
        origin = Origin.objects.create(label="Simple Origin")
        self.assertIsNone(origin.smig_hours_for_leave_allowance)
    
    def test_db_table(self):
        self.assertEqual(Origin._meta.db_table, 'origines')


class EmployeeStatusModelTest(TestCase):
    def setUp(self):
        self.status = EmployeeStatus.objects.create(
            name="Permanent Full-Time"
        )
    
    def test_status_creation(self):
        self.assertEqual(self.status.name, "Permanent Full-Time")
    
    def test_status_str(self):
        self.assertEqual(str(self.status), "Permanent Full-Time")
    
    def test_status_ordering(self):
        s1 = EmployeeStatus.objects.create(name="Z Status")
        s2 = EmployeeStatus.objects.create(name="A Status")
        
        statuses = list(EmployeeStatus.objects.all())
        self.assertEqual(statuses[0].name, "A Status")
        self.assertEqual(statuses[1].name, "Permanent Full-Time")
        self.assertEqual(statuses[2].name, "Z Status")
    
    def test_db_table(self):
        self.assertEqual(EmployeeStatus._meta.db_table, 'statut')


class PayrollMotifModelTest(TestCase):
    def setUp(self):
        self.motif = PayrollMotif.objects.create(
            name="Regular Monthly"
        )
    
    def test_motif_creation_with_defaults(self):
        self.assertEqual(self.motif.name, "Regular Monthly")
        self.assertTrue(self.motif.employee_subject_to_its)
        self.assertTrue(self.motif.employee_subject_to_cnss)
        self.assertTrue(self.motif.employee_subject_to_cnam)
        self.assertTrue(self.motif.declaration_subject_to_its)
        self.assertTrue(self.motif.declaration_subject_to_cnss)
        self.assertTrue(self.motif.declaration_subject_to_cnam)
        self.assertTrue(self.motif.is_active)
    
    def test_motif_creation_with_custom_values(self):
        motif = PayrollMotif.objects.create(
            name="Bonus Payment",
            employee_subject_to_its=False,
            employee_subject_to_cnss=False,
            employee_subject_to_cnam=True,
            declaration_subject_to_its=False,
            declaration_subject_to_cnss=False,
            declaration_subject_to_cnam=True,
            is_active=False
        )
        
        self.assertFalse(motif.employee_subject_to_its)
        self.assertFalse(motif.employee_subject_to_cnss)
        self.assertTrue(motif.employee_subject_to_cnam)
        self.assertFalse(motif.declaration_subject_to_its)
        self.assertFalse(motif.declaration_subject_to_cnss)
        self.assertTrue(motif.declaration_subject_to_cnam)
        self.assertFalse(motif.is_active)
    
    def test_motif_str(self):
        self.assertEqual(str(self.motif), "Regular Monthly")
    
    def test_db_table(self):
        self.assertEqual(PayrollMotif._meta.db_table, 'motif')


class ReferenceModelsIntegrationTest(TestCase):
    def test_all_reference_models_creation(self):
        """Test creating all reference models together"""
        activity = Activity.objects.create(name="Mining")
        bank = Bank.objects.create(name="Test Bank")
        origin = Origin.objects.create(label="Local Hire")
        status = EmployeeStatus.objects.create(name="Active")
        motif = PayrollMotif.objects.create(name="Monthly")
        
        self.assertEqual(Activity.objects.count(), 1)
        self.assertEqual(Bank.objects.count(), 1)
        self.assertEqual(Origin.objects.count(), 1)
        self.assertEqual(EmployeeStatus.objects.count(), 1)
        self.assertEqual(PayrollMotif.objects.count(), 1)
    
    def test_all_models_have_correct_db_tables(self):
        """Verify all models use correct database table names"""
        self.assertEqual(Activity._meta.db_table, 'activite')
        self.assertEqual(Bank._meta.db_table, 'banque')
        self.assertEqual(Origin._meta.db_table, 'origines')
        self.assertEqual(EmployeeStatus._meta.db_table, 'statut')
        self.assertEqual(PayrollMotif._meta.db_table, 'motif')