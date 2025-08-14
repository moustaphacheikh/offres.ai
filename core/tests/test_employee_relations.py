import pytest
from django.test import TestCase
from django.db import IntegrityError
from django.utils import timezone
from datetime import date, timedelta
from core.models import (
    Employee, Child, Leave, Document, Diploma,
    GeneralDirection, Direction, Department, Position,
    Activity, Bank, Origin, EmployeeStatus, SalaryGrade
)


class EmployeeRelationsTestBase(TestCase):
    """Base test class with shared setup for employee relations tests"""
    
    def setUp(self):
        """Create basic test data for employee relationships"""
        # Create organizational structure
        self.general_direction = GeneralDirection.objects.create(name="Direction Générale")
        self.direction = Direction.objects.create(
            name="Direction Technique", 
            general_direction=self.general_direction
        )
        self.department = Department.objects.create(
            name="Département IT", 
            direction=self.direction
        )
        self.position = Position.objects.create(name="Développeur Senior")
        
        # Create reference data
        self.activity = Activity.objects.create(name="Mining Operations")
        self.bank = Bank.objects.create(name="Banque Mauritanienne")
        self.origin = Origin.objects.create(label="Recrutement Local")
        self.status = EmployeeStatus.objects.create(name="Permanent")
        self.salary_grade = SalaryGrade.objects.create(
            category="A1", 
            level=5, 
            base_salary=50000, 
            category_name="Senior", 
            status=self.status
        )
        
        # Create test employees
        self.employee1 = Employee.objects.create(
            first_name="Ahmed",
            last_name="Mohamed",
            hire_date=date(2020, 1, 15),
            position=self.position,
            department=self.department,
            salary_grade=self.salary_grade
        )
        
        self.employee2 = Employee.objects.create(
            first_name="Fatima",
            last_name="Ali",
            hire_date=date(2019, 6, 1),
            position=self.position,
            department=self.department
        )


class ChildModelTest(EmployeeRelationsTestBase):
    """Test Child model functionality"""
    
    def test_child_basic_creation(self):
        """Test basic child creation with required fields"""
        child = Child.objects.create(
            employee=self.employee1,
            child_name="Omar Ahmed",
            birth_date=date(2015, 3, 20),
            parent_type="Père",
            gender="M"
        )
        
        self.assertEqual(child.child_name, "Omar Ahmed")
        self.assertEqual(child.birth_date, date(2015, 3, 20))
        self.assertEqual(child.parent_type, "Père")
        self.assertEqual(child.gender, "M")
        self.assertEqual(child.employee, self.employee1)
        
    def test_child_str_representation(self):
        """Test string representation of child"""
        child = Child.objects.create(
            employee=self.employee1,
            child_name="Aicha Ahmed",
            birth_date=date(2018, 8, 15),
            parent_type="Père",
            gender="F"
        )
        
        expected_str = "Aicha Ahmed (Ahmed Mohamed)"
        self.assertEqual(str(child), expected_str)
        
    def test_child_employee_relationship(self):
        """Test foreign key relationship with employee"""
        child1 = Child.objects.create(
            employee=self.employee1,
            child_name="Child One",
            birth_date=date(2016, 1, 1),
            parent_type="Père",
            gender="M"
        )
        
        child2 = Child.objects.create(
            employee=self.employee1,
            child_name="Child Two",
            birth_date=date(2018, 6, 15),
            parent_type="Mère",
            gender="F"
        )
        
        # Test forward relationship
        self.assertEqual(child1.employee, self.employee1)
        self.assertEqual(child2.employee, self.employee1)
        
        # Test reverse relationship
        children = self.employee1.children.all()
        self.assertEqual(children.count(), 2)
        self.assertIn(child1, children)
        self.assertIn(child2, children)
        
    def test_child_cascade_deletion(self):
        """Test that children are deleted when employee is deleted"""
        child = Child.objects.create(
            employee=self.employee1,
            child_name="Test Child",
            birth_date=date(2017, 5, 10),
            parent_type="Père",
            gender="M"
        )
        
        child_id = child.id
        self.assertTrue(Child.objects.filter(id=child_id).exists())
        
        # Delete employee should cascade to children
        self.employee1.delete()
        self.assertFalse(Child.objects.filter(id=child_id).exists())
        
    def test_child_unique_constraint(self):
        """Test unique constraint on employee, child_name, birth_date"""
        # Create first child
        Child.objects.create(
            employee=self.employee1,
            child_name="Unique Child",
            birth_date=date(2016, 1, 1),
            parent_type="Père",
            gender="M"
        )
        
        # Try to create duplicate - should fail
        with self.assertRaises(IntegrityError):
            Child.objects.create(
                employee=self.employee1,
                child_name="Unique Child",
                birth_date=date(2016, 1, 1),
                parent_type="Mère",
                gender="F"
            )
            
    def test_child_ordering(self):
        """Test default ordering by child_name"""
        child_b = Child.objects.create(
            employee=self.employee1,
            child_name="Beta Child",
            birth_date=date(2016, 1, 1),
            parent_type="Père",
            gender="M"
        )
        
        child_a = Child.objects.create(
            employee=self.employee1,
            child_name="Alpha Child",
            birth_date=date(2017, 1, 1),
            parent_type="Père",
            gender="F"
        )
        
        children = list(Child.objects.all())
        self.assertEqual(children[0], child_a)  # Alpha comes first
        self.assertEqual(children[1], child_b)  # Beta comes second
        
    def test_child_audit_fields(self):
        """Test automatic audit fields"""
        child = Child.objects.create(
            employee=self.employee1,
            child_name="Audit Test",
            birth_date=date(2019, 1, 1),
            parent_type="Père",
            gender="M"
        )
        
        self.assertIsInstance(child.created_at, timezone.datetime)
        self.assertIsInstance(child.updated_at, timezone.datetime)
        
        # Test updated_at changes on save
        original_updated = child.updated_at
        child.child_name = "Updated Name"
        child.save()
        self.assertGreater(child.updated_at, original_updated)


class LeaveModelTest(EmployeeRelationsTestBase):
    """Test Leave model functionality"""
    
    def test_leave_basic_creation(self):
        """Test basic leave creation with required fields"""
        leave = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 7, 1),
            planned_return=date(2023, 7, 15),
            leave_type='ANNUAL'
        )
        
        self.assertEqual(leave.employee, self.employee1)
        self.assertEqual(leave.period, date(2023, 1, 1))
        self.assertEqual(leave.start_date, date(2023, 7, 1))
        self.assertEqual(leave.planned_return, date(2023, 7, 15))
        self.assertEqual(leave.leave_type, 'ANNUAL')
        
    def test_leave_str_representation(self):
        """Test string representation of leave"""
        leave = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 8, 1),
            planned_return=date(2023, 8, 10),
            leave_type='SICK'
        )
        
        expected_str = "Ahmed Mohamed - Sick Leave (2023-08-01)"
        self.assertEqual(str(leave), expected_str)
        
    def test_leave_types(self):
        """Test all leave type choices"""
        leave_types = [
            'ANNUAL', 'SICK', 'MATERNITY', 'PATERNITY', 
            'COMPASSIONATE', 'UNPAID', 'OTHER'
        ]
        
        for leave_type in leave_types:
            leave = Leave.objects.create(
                employee=self.employee1,
                period=date(2023, 1, 1),
                start_date=date(2023, 6, 1),
                planned_return=date(2023, 6, 5),
                leave_type=leave_type
            )
            self.assertEqual(leave.leave_type, leave_type)
            leave.delete()  # Clean up for next iteration
            
    def test_leave_with_notes(self):
        """Test leave creation with notes"""
        notes = "Medical appointment and recovery time required"
        leave = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 5, 1),
            planned_return=date(2023, 5, 3),
            notes=notes,
            leave_type='SICK'
        )
        
        self.assertEqual(leave.notes, notes)
        
    def test_leave_actual_return(self):
        """Test leave with actual return date"""
        leave = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 4, 1),
            planned_return=date(2023, 4, 10),
            actual_return=date(2023, 4, 8),  # Returned early
            leave_type='ANNUAL'
        )
        
        self.assertEqual(leave.actual_return, date(2023, 4, 8))
        
    def test_leave_planned_duration_days(self):
        """Test planned duration calculation"""
        leave = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 6, 1),
            planned_return=date(2023, 6, 5),  # 5 days total
            leave_type='ANNUAL'
        )
        
        self.assertEqual(leave.planned_duration_days, 5)
        
    def test_leave_actual_duration_days(self):
        """Test actual duration calculation"""
        # Leave with actual return
        leave_with_return = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 6, 1),
            planned_return=date(2023, 6, 10),
            actual_return=date(2023, 6, 7),  # 7 days actual
            leave_type='ANNUAL'
        )
        
        self.assertEqual(leave_with_return.actual_duration_days, 7)
        
        # Leave without actual return
        leave_without_return = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 7, 1),
            planned_return=date(2023, 7, 10),
            leave_type='ANNUAL'
        )
        
        self.assertIsNone(leave_without_return.actual_duration_days)
        
    def test_leave_is_active_property(self):
        """Test is_active property logic"""
        today = timezone.now().date()
        
        # Active leave (started today, no actual return)
        active_leave = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=today,
            planned_return=today + timedelta(days=5),
            leave_type='ANNUAL'
        )
        
        self.assertTrue(active_leave.is_active)
        
        # Completed leave (has actual return in past)
        completed_leave = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=today - timedelta(days=10),
            planned_return=today - timedelta(days=5),
            actual_return=today - timedelta(days=6),
            leave_type='SICK'
        )
        
        self.assertFalse(completed_leave.is_active)
        
    def test_leave_ordering(self):
        """Test default ordering by start_date (descending)"""
        leave1 = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 6, 1),
            planned_return=date(2023, 6, 5),
            leave_type='ANNUAL'
        )
        
        leave2 = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 8, 1),
            planned_return=date(2023, 8, 5),
            leave_type='SICK'
        )
        
        leaves = list(Leave.objects.all())
        self.assertEqual(leaves[0], leave2)  # August (newer) first
        self.assertEqual(leaves[1], leave1)  # June (older) second


class DocumentModelTest(EmployeeRelationsTestBase):
    """Test Document model functionality"""
    
    def test_document_basic_creation(self):
        """Test basic document creation with required fields"""
        document = Document.objects.create(
            employee=self.employee1,
            document_type='CONTRACT',
            document_name="Employment Contract 2020",
        )
        
        self.assertEqual(document.employee, self.employee1)
        self.assertEqual(document.document_type, 'CONTRACT')
        self.assertEqual(document.document_name, "Employment Contract 2020")
        
    def test_document_str_representation(self):
        """Test string representation of document"""
        document = Document.objects.create(
            employee=self.employee1,
            document_name="Passport Copy",
            document_type='PASSPORT'
        )
        
        expected_str = "Passport Copy (Ahmed Mohamed)"
        self.assertEqual(str(document), expected_str)
        
    def test_document_types(self):
        """Test all document type choices"""
        doc_types = [
            'CONTRACT', 'ID_CARD', 'PASSPORT', 'DIPLOMA', 
            'MEDICAL', 'RESUME', 'REFERENCE', 'PHOTO', 'OTHER'
        ]
        
        for doc_type in doc_types:
            document = Document.objects.create(
                employee=self.employee1,
                document_type=doc_type,
                document_name=f"Test {doc_type} Document"
            )
            self.assertEqual(document.document_type, doc_type)
            document.delete()  # Clean up for next iteration
            
    def test_document_with_file_info(self):
        """Test document creation with file information"""
        document = Document.objects.create(
            employee=self.employee1,
            document_type='PHOTO',
            document_name="Employee Profile Photo",
            file_path="/documents/employees/ahmed_mohamed_photo.jpg",
            file_type="image/jpeg",
        )
        
        self.assertEqual(document.file_path, "/documents/employees/ahmed_mohamed_photo.jpg")
        self.assertEqual(document.file_type, "image/jpeg")
        
    def test_document_with_dates(self):
        """Test document with issue and expiry dates"""
        document = Document.objects.create(
            employee=self.employee1,
            document_type='PASSPORT',
            document_name="Employee Passport",
            issue_date=date(2020, 1, 15),
            expiry_date=date(2030, 1, 15)
        )
        
        self.assertEqual(document.issue_date, date(2020, 1, 15))
        self.assertEqual(document.expiry_date, date(2030, 1, 15))
        
    def test_document_is_expired_property(self):
        """Test is_expired property logic"""
        today = timezone.now().date()
        
        # Expired document
        expired_doc = Document.objects.create(
            employee=self.employee1,
            document_name="Expired License",
            expiry_date=today - timedelta(days=1)
        )
        
        self.assertTrue(expired_doc.is_expired)
        
        # Valid document
        valid_doc = Document.objects.create(
            employee=self.employee1,
            document_name="Valid Passport",
            expiry_date=today + timedelta(days=365)
        )
        
        self.assertFalse(valid_doc.is_expired)
        
        # Document without expiry date
        no_expiry_doc = Document.objects.create(
            employee=self.employee1,
            document_name="Contract"
        )
        
        self.assertFalse(no_expiry_doc.is_expired)
        
    def test_document_expires_soon_property(self):
        """Test expires_soon property logic"""
        today = timezone.now().date()
        
        # Document expiring in 15 days (within default 30 days warning)
        expires_soon_doc = Document.objects.create(
            employee=self.employee1,
            document_name="Expiring Soon",
            expiry_date=today + timedelta(days=15)
        )
        
        self.assertTrue(expires_soon_doc.expires_soon)
        
        # Document expiring in 60 days (beyond 30 days warning)
        valid_doc = Document.objects.create(
            employee=self.employee1,
            document_name="Valid for Long",
            expiry_date=today + timedelta(days=60)
        )
        
        self.assertFalse(valid_doc.expires_soon)
        
        # Document without expiry date
        no_expiry_doc = Document.objects.create(
            employee=self.employee1,
            document_name="No Expiry Contract"
        )
        
        self.assertFalse(no_expiry_doc.expires_soon)
        
    def test_document_ordering(self):
        """Test default ordering by created_at (descending)"""
        doc1 = Document.objects.create(
            employee=self.employee1,
            document_name="First Document"
        )
        
        doc2 = Document.objects.create(
            employee=self.employee1,
            document_name="Second Document"
        )
        
        documents = list(Document.objects.all())
        self.assertEqual(documents[0], doc2)  # Newer first
        self.assertEqual(documents[1], doc1)  # Older second


class DiplomaModelTest(EmployeeRelationsTestBase):
    """Test Diploma model functionality"""
    
    def test_diploma_basic_creation(self):
        """Test basic diploma creation with required fields"""
        diploma = Diploma.objects.create(
            employee=self.employee1,
            diploma_name="Master in Computer Science",
            institution="University of Nouakchott",
            graduation_date=date(2018, 6, 15),
            level='MASTER'
        )
        
        self.assertEqual(diploma.diploma_name, "Master in Computer Science")
        self.assertEqual(diploma.institution, "University of Nouakchott")
        self.assertEqual(diploma.graduation_date, date(2018, 6, 15))
        self.assertEqual(diploma.level, 'MASTER')
        
    def test_diploma_str_representation(self):
        """Test string representation of diploma"""
        diploma = Diploma.objects.create(
            employee=self.employee1,
            diploma_name="Bachelor in Engineering",
            institution="École Polytechnique",
            graduation_date=date(2016, 7, 1)
        )
        
        expected_str = "Bachelor in Engineering - École Polytechnique (Ahmed Mohamed)"
        self.assertEqual(str(diploma), expected_str)
        
    def test_diploma_degree_levels(self):
        """Test all degree level choices"""
        degree_levels = [
            'HIGH_SCHOOL', 'DIPLOMA', 'BACHELOR', 'MASTER', 
            'DOCTORATE', 'CERTIFICATE', 'OTHER'
        ]
        
        for level in degree_levels:
            diploma = Diploma.objects.create(
                employee=self.employee1,
                diploma_name=f"Test {level} Degree",
                institution="Test Institution",
                graduation_date=date(2020, 1, 1),
                level=level
            )
            self.assertEqual(diploma.level, level)
            diploma.delete()  # Clean up for next iteration
            
    def test_diploma_with_optional_fields(self):
        """Test diploma creation with all optional fields"""
        diploma = Diploma.objects.create(
            employee=self.employee1,
            diploma_name="PhD in Computer Science",
            institution="MIT",
            graduation_date=date(2022, 5, 20),
            level='DOCTORATE',
            field_of_study="Artificial Intelligence",
            gpa_score=3.85,
            honors="Summa Cum Laude",
            is_verified=True,
            verification_date=date(2022, 6, 1)
        )
        
        self.assertEqual(diploma.field_of_study, "Artificial Intelligence")
        self.assertEqual(diploma.gpa_score, 3.85)
        self.assertEqual(diploma.honors, "Summa Cum Laude")
        self.assertTrue(diploma.is_verified)
        self.assertEqual(diploma.verification_date, date(2022, 6, 1))
        
    def test_diploma_years_since_graduation_property(self):
        """Test years_since_graduation property"""
        current_year = timezone.now().year
        graduation_year = current_year - 5
        
        diploma = Diploma.objects.create(
            employee=self.employee1,
            diploma_name="Bachelor Degree",
            institution="Test University",
            graduation_date=date(graduation_year, 6, 1)
        )
        
        self.assertEqual(diploma.years_since_graduation, 5)
        
    def test_diploma_unique_constraint(self):
        """Test unique constraint on employee, diploma_name, institution, graduation_date"""
        # Create first diploma
        Diploma.objects.create(
            employee=self.employee1,
            diploma_name="Unique Diploma",
            institution="Unique University",
            graduation_date=date(2020, 1, 1)
        )
        
        # Try to create duplicate - should fail
        with self.assertRaises(IntegrityError):
            Diploma.objects.create(
                employee=self.employee1,
                diploma_name="Unique Diploma",
                institution="Unique University",
                graduation_date=date(2020, 1, 1)
            )
            
    def test_diploma_ordering(self):
        """Test default ordering by graduation_date (descending)"""
        diploma1 = Diploma.objects.create(
            employee=self.employee1,
            diploma_name="Bachelor Degree",
            institution="University A",
            graduation_date=date(2016, 6, 1)
        )
        
        diploma2 = Diploma.objects.create(
            employee=self.employee1,
            diploma_name="Master Degree",
            institution="University B",
            graduation_date=date(2018, 6, 1)
        )
        
        diplomas = list(Diploma.objects.all())
        self.assertEqual(diplomas[0], diploma2)  # 2018 (newer) first
        self.assertEqual(diplomas[1], diploma1)  # 2016 (older) second


class EmployeeRelationsIntegrationTest(EmployeeRelationsTestBase):
    """Test integration between employee and relation models"""
    
    def test_employee_with_all_relations(self):
        """Test employee with children, leaves, documents, and diplomas"""
        # Create child
        child = Child.objects.create(
            employee=self.employee1,
            child_name="Child Test",
            birth_date=date(2015, 1, 1),
            parent_type="Père",
            gender="M"
        )
        
        # Create leave
        leave = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 7, 1),
            planned_return=date(2023, 7, 15),
            leave_type='ANNUAL'
        )
        
        # Create document
        document = Document.objects.create(
            employee=self.employee1,
            document_name="Employee Contract",
            document_type='CONTRACT'
        )
        
        # Create diploma
        diploma = Diploma.objects.create(
            employee=self.employee1,
            diploma_name="Engineering Degree",
            institution="University",
            graduation_date=date(2016, 1, 1)
        )
        
        # Test reverse relationships
        self.assertEqual(self.employee1.children.count(), 1)
        self.assertEqual(self.employee1.leaves.count(), 1)
        self.assertEqual(self.employee1.documents.count(), 1)
        self.assertEqual(self.employee1.diplomas.count(), 1)
        
        # Test specific instances
        self.assertEqual(self.employee1.children.first(), child)
        self.assertEqual(self.employee1.leaves.first(), leave)
        self.assertEqual(self.employee1.documents.first(), document)
        self.assertEqual(self.employee1.diplomas.first(), diploma)
        
    def test_cascade_deletion_all_relations(self):
        """Test that all relations are deleted when employee is deleted"""
        # Create relations
        child = Child.objects.create(
            employee=self.employee1,
            child_name="Test Child",
            birth_date=date(2015, 1, 1),
            parent_type="Père",
            gender="M"
        )
        
        leave = Leave.objects.create(
            employee=self.employee1,
            period=date(2023, 1, 1),
            start_date=date(2023, 7, 1),
            planned_return=date(2023, 7, 15)
        )
        
        document = Document.objects.create(
            employee=self.employee1,
            document_name="Test Document"
        )
        
        diploma = Diploma.objects.create(
            employee=self.employee1,
            diploma_name="Test Diploma",
            institution="Test University",
            graduation_date=date(2016, 1, 1)
        )
        
        # Store IDs before deletion
        child_id = child.id
        leave_id = leave.id
        document_id = document.id
        diploma_id = diploma.id
        
        # Verify they exist
        self.assertTrue(Child.objects.filter(id=child_id).exists())
        self.assertTrue(Leave.objects.filter(id=leave_id).exists())
        self.assertTrue(Document.objects.filter(id=document_id).exists())
        self.assertTrue(Diploma.objects.filter(id=diploma_id).exists())
        
        # Delete employee
        self.employee1.delete()
        
        # Verify all relations are deleted
        self.assertFalse(Child.objects.filter(id=child_id).exists())
        self.assertFalse(Leave.objects.filter(id=leave_id).exists())
        self.assertFalse(Document.objects.filter(id=document_id).exists())
        self.assertFalse(Diploma.objects.filter(id=diploma_id).exists())
        
    def test_multiple_employees_relations(self):
        """Test that relations are properly isolated between employees"""
        # Create relations for employee1
        child1 = Child.objects.create(
            employee=self.employee1,
            child_name="Employee1 Child",
            birth_date=date(2015, 1, 1),
            parent_type="Père",
            gender="M"
        )
        
        # Create relations for employee2
        child2 = Child.objects.create(
            employee=self.employee2,
            child_name="Employee2 Child",
            birth_date=date(2017, 1, 1),
            parent_type="Mère",
            gender="F"
        )
        
        # Test isolation
        self.assertEqual(self.employee1.children.count(), 1)
        self.assertEqual(self.employee2.children.count(), 1)
        self.assertEqual(self.employee1.children.first(), child1)
        self.assertEqual(self.employee2.children.first(), child2)
        
        # Delete employee1 should not affect employee2 relations
        self.employee1.delete()
        self.assertEqual(self.employee2.children.count(), 1)
        self.assertTrue(Child.objects.filter(id=child2.id).exists())