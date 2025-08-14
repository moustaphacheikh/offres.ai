import pytest
from django.test import TestCase
from django.db import IntegrityError
from core.models import EmployeeStatus, SalaryGrade, HousingGrid


class SalaryGradeModelTest(TestCase):
    def setUp(self):
        self.status = EmployeeStatus.objects.create(name="Permanent")
        self.salary_grade = SalaryGrade.objects.create(
            category="A1",
            base_salary=50000.00,
            category_name="Senior",
            level=5,
            status=self.status
        )
    
    def test_salary_grade_creation(self):
        self.assertEqual(self.salary_grade.category, "A1")
        self.assertEqual(self.salary_grade.base_salary, 50000.00)
        self.assertEqual(self.salary_grade.category_name, "Senior")
        self.assertEqual(self.salary_grade.level, 5)
        self.assertEqual(self.salary_grade.status, self.status)
    
    def test_salary_grade_str(self):
        self.assertEqual(str(self.salary_grade), "A1 - Senior")
    
    def test_salary_grade_primary_key(self):
        """Test that category is the primary key"""
        self.assertEqual(self.salary_grade.pk, "A1")
    
    def test_salary_grade_ordering(self):
        status2 = EmployeeStatus.objects.create(name="Temporary")
        sg1 = SalaryGrade.objects.create(
            category="B1", level=2, base_salary=30000, 
            category_name="Junior", status=status2
        )
        sg2 = SalaryGrade.objects.create(
            category="A2", level=3, base_salary=40000, 
            category_name="Mid", status=status2
        )
        
        grades = list(SalaryGrade.objects.all())
        # Should order by level first, then category
        self.assertEqual(grades[0].category, "B1")  # level 2
        self.assertEqual(grades[1].category, "A2")  # level 3  
        self.assertEqual(grades[2].category, "A1")  # level 5
    
    def test_salary_grade_relationship_with_status(self):
        """Test the relationship with EmployeeStatus"""
        self.assertIn(self.salary_grade, self.status.salary_grades.all())
    
    def test_db_table(self):
        self.assertEqual(SalaryGrade._meta.db_table, 'grillesalairebase')


class HousingGridModelTest(TestCase):
    def setUp(self):
        self.status = EmployeeStatus.objects.create(name="Permanent")
        self.salary_grade = SalaryGrade.objects.create(
            category="A1",
            base_salary=50000.00,
            category_name="Senior",
            level=5,
            status=self.status
        )
        self.housing_grid = HousingGrid.objects.create(
            salary_grade=self.salary_grade,
            marital_status="M",
            children_count=2,
            amount=5000.00
        )
    
    def test_housing_grid_creation(self):
        self.assertEqual(self.housing_grid.salary_grade, self.salary_grade)
        self.assertEqual(self.housing_grid.marital_status, "M")
        self.assertEqual(self.housing_grid.children_count, 2)
        self.assertEqual(self.housing_grid.amount, 5000.00)
    
    def test_housing_grid_str(self):
        expected = "A1 - M - 2 children"
        self.assertEqual(str(self.housing_grid), expected)
    
    def test_housing_grid_optional_amount(self):
        """Test housing grid with no amount"""
        housing = HousingGrid.objects.create(
            salary_grade=self.salary_grade,
            marital_status="S",
            children_count=0
        )
        self.assertIsNone(housing.amount)
    
    def test_housing_grid_unique_together(self):
        """Test the unique constraint on salary_grade, marital_status, children_count"""
        with self.assertRaises(IntegrityError):
            HousingGrid.objects.create(
                salary_grade=self.salary_grade,
                marital_status="M",
                children_count=2,  # Same combination as setUp
                amount=6000.00
            )
    
    def test_housing_grid_relationship(self):
        """Test the relationship with SalaryGrade"""
        self.assertIn(self.housing_grid, self.salary_grade.housing_allowances.all())
    
    def test_housing_grid_cascade_delete(self):
        """Test cascade delete when salary grade is deleted"""
        housing_id = self.housing_grid.id
        self.salary_grade.delete()
        
        with self.assertRaises(HousingGrid.DoesNotExist):
            HousingGrid.objects.get(id=housing_id)
    
    def test_housing_grid_ordering(self):
        """Test ordering by salary_grade, marital_status, children_count"""
        # Test that the ordering works within the same salary grade
        h1 = HousingGrid.objects.create(
            salary_grade=self.salary_grade, marital_status="S", children_count=0, amount=2000
        )
        h2 = HousingGrid.objects.create(
            salary_grade=self.salary_grade, marital_status="D", children_count=1, amount=1000
        )
        h3 = HousingGrid.objects.create(
            salary_grade=self.salary_grade, marital_status="M", children_count=0, amount=4000
        )
        
        grids = list(HousingGrid.objects.all())
        # Should order by marital_status, then children_count within same salary grade
        # Original from setUp: A1-M-2
        # D comes before M comes before S alphabetically
        expected_order = [
            (self.salary_grade.pk, "D", 1),  # h2
            (self.salary_grade.pk, "M", 0),  # h3
            (self.salary_grade.pk, "M", 2),  # from setUp
            (self.salary_grade.pk, "S", 0),  # h1
        ]
        
        actual_order = [
            (g.salary_grade.pk, g.marital_status, g.children_count) 
            for g in grids
        ]
        
        self.assertEqual(actual_order, expected_order)
    
    def test_db_table(self):
        self.assertEqual(HousingGrid._meta.db_table, 'grillelogement')


class CompensationIntegrationTest(TestCase):
    def test_complete_compensation_setup(self):
        """Test creating a complete compensation structure"""
        # Create statuses
        permanent_status = EmployeeStatus.objects.create(name="Permanent")
        contract_status = EmployeeStatus.objects.create(name="Contract")
        
        # Create salary grades
        grade_a1 = SalaryGrade.objects.create(
            category="A1", level=5, base_salary=80000, 
            category_name="Senior", status=permanent_status
        )
        grade_b1 = SalaryGrade.objects.create(
            category="B1", level=3, base_salary=50000, 
            category_name="Mid", status=contract_status
        )
        
        # Create housing grids for different scenarios
        # Single, no children
        HousingGrid.objects.create(
            salary_grade=grade_a1, marital_status="S", 
            children_count=0, amount=2000
        )
        # Married, 2 children
        HousingGrid.objects.create(
            salary_grade=grade_a1, marital_status="M", 
            children_count=2, amount=8000
        )
        # Different grade
        HousingGrid.objects.create(
            salary_grade=grade_b1, marital_status="M", 
            children_count=1, amount=4000
        )
        
        # Verify relationships
        self.assertEqual(SalaryGrade.objects.count(), 2)
        self.assertEqual(HousingGrid.objects.count(), 3)
        self.assertEqual(grade_a1.housing_allowances.count(), 2)
        self.assertEqual(grade_b1.housing_allowances.count(), 1)
    
    def test_database_constraints(self):
        """Test various database constraints and relationships"""
        status = EmployeeStatus.objects.create(name="Test Status")
        
        # Test that category must be unique (it's the PK)
        grade1 = SalaryGrade.objects.create(
            category="TEST", level=1, base_salary=1000, 
            category_name="Test", status=status
        )
        
        with self.assertRaises(IntegrityError):
            SalaryGrade.objects.create(
                category="TEST",  # Same category
                level=2, base_salary=2000, 
                category_name="Test2", status=status
            )
    
    def test_models_meta_properties(self):
        """Test Meta class properties are correct"""
        # SalaryGrade
        self.assertEqual(SalaryGrade._meta.db_table, 'grillesalairebase')
        self.assertEqual(SalaryGrade._meta.ordering, ['level', 'category'])
        
        # HousingGrid
        self.assertEqual(HousingGrid._meta.db_table, 'grillelogement')
        self.assertEqual(
            HousingGrid._meta.unique_together, 
            (('salary_grade', 'marital_status', 'children_count'),)
        )
        self.assertEqual(
            HousingGrid._meta.ordering, 
            ['salary_grade', 'marital_status', 'children_count']
        )