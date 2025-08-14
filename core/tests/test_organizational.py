import pytest
from django.test import TestCase
from core.models import GeneralDirection, Direction, Department, Position


class GeneralDirectionModelTest(TestCase):
    def setUp(self):
        self.general_direction = GeneralDirection.objects.create(
            name="Direction Générale"
        )
    
    def test_general_direction_creation(self):
        self.assertEqual(self.general_direction.name, "Direction Générale")
        self.assertTrue(self.general_direction.created_at)
    
    def test_general_direction_str(self):
        self.assertEqual(str(self.general_direction), "Direction Générale")
    
    def test_general_direction_ordering(self):
        gd1 = GeneralDirection.objects.create(name="B Direction")
        gd2 = GeneralDirection.objects.create(name="A Direction")
        
        directions = list(GeneralDirection.objects.all())
        self.assertEqual(directions[0].name, "A Direction")
        self.assertEqual(directions[1].name, "B Direction")


class DirectionModelTest(TestCase):
    def setUp(self):
        self.general_direction = GeneralDirection.objects.create(
            name="Direction Générale"
        )
        self.direction = Direction.objects.create(
            name="Direction Technique",
            general_direction=self.general_direction
        )
    
    def test_direction_creation(self):
        self.assertEqual(self.direction.name, "Direction Technique")
        self.assertEqual(self.direction.general_direction, self.general_direction)
        self.assertTrue(self.direction.created_at)
    
    def test_direction_str(self):
        self.assertEqual(str(self.direction), "Direction Technique")
    
    def test_direction_relationship(self):
        self.assertEqual(self.general_direction.directions.first(), self.direction)
    
    def test_cascade_delete(self):
        direction_id = self.direction.id
        self.general_direction.delete()
        
        with self.assertRaises(Direction.DoesNotExist):
            Direction.objects.get(id=direction_id)


class DepartmentModelTest(TestCase):
    def setUp(self):
        self.general_direction = GeneralDirection.objects.create(
            name="Direction Générale"
        )
        self.direction = Direction.objects.create(
            name="Direction Technique",
            general_direction=self.general_direction
        )
        self.department = Department.objects.create(
            name="Département IT",
            direction=self.direction
        )
    
    def test_department_creation(self):
        self.assertEqual(self.department.name, "Département IT")
        self.assertEqual(self.department.direction, self.direction)
        self.assertTrue(self.department.created_at)
    
    def test_department_str(self):
        self.assertEqual(str(self.department), "Département IT")
    
    def test_department_relationship(self):
        self.assertEqual(self.direction.departments.first(), self.department)
    
    def test_cascade_delete(self):
        department_id = self.department.id
        self.direction.delete()
        
        with self.assertRaises(Department.DoesNotExist):
            Department.objects.get(id=department_id)


class PositionModelTest(TestCase):
    def setUp(self):
        self.position = Position.objects.create(
            name="Développeur Senior"
        )
    
    def test_position_creation(self):
        self.assertEqual(self.position.name, "Développeur Senior")
        self.assertTrue(self.position.created_at)
    
    def test_position_str(self):
        self.assertEqual(str(self.position), "Développeur Senior")
    
    def test_position_ordering(self):
        p1 = Position.objects.create(name="Z Position")
        p2 = Position.objects.create(name="A Position")
        
        positions = list(Position.objects.all())
        self.assertEqual(positions[0].name, "A Position")
        self.assertEqual(positions[1].name, "Développeur Senior")
        self.assertEqual(positions[2].name, "Z Position")


class OrganizationalIntegrationTest(TestCase):
    def test_complete_organizational_hierarchy(self):
        """Test the complete organizational hierarchy"""
        # Create hierarchy
        general_direction = GeneralDirection.objects.create(
            name="Direction Générale des Mines"
        )
        
        direction = Direction.objects.create(
            name="Direction Technique",
            general_direction=general_direction
        )
        
        department = Department.objects.create(
            name="Département Extraction",
            direction=direction
        )
        
        position = Position.objects.create(
            name="Ingénieur Minier"
        )
        
        # Test relationships
        self.assertEqual(direction.general_direction, general_direction)
        self.assertEqual(department.direction, direction)
        
        # Test reverse relationships
        self.assertIn(direction, general_direction.directions.all())
        self.assertIn(department, direction.departments.all())
        
        # Test database table names
        self.assertEqual(GeneralDirection._meta.db_table, 'directiongeneral')
        self.assertEqual(Direction._meta.db_table, 'direction')
        self.assertEqual(Department._meta.db_table, 'departement')
        self.assertEqual(Position._meta.db_table, 'poste')