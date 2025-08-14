import pytest
from decimal import Decimal
from datetime import date, datetime
from django.db import IntegrityError
from django.core.exceptions import ValidationError
from core.models import (
    Employee, CNSSDeclaration, CNAMDeclaration,
    GeneralDirection, Direction, Department, Position,
    Activity, Origin, Bank, SalaryGrade
)


@pytest.mark.django_db
class TestCNSSDeclaration:
    """Test suite for CNSSDeclaration model"""

    @pytest.fixture
    def employee(self):
        """Create a test employee"""
        general_direction = GeneralDirection.objects.create(
            name="Test General Direction"
        )
        direction = Direction.objects.create(
            name="Test Direction",
            general_direction=general_direction
        )
        department = Department.objects.create(
            name="Test Department",
            direction=direction
        )
        position = Position.objects.create(
            name="Test Position"
        )
        
        return Employee.objects.create(
            first_name="John",
            last_name="Doe",
            cnss_number="12345678",
            cnam_number="87654321",
            hire_date=date(2023, 1, 1),
            position=position,
            department=department,
            direction=direction,
            general_direction=general_direction
        )

    @pytest.fixture
    def cnss_declaration_data(self, employee):
        """Sample CNSS declaration data"""
        return {
            'employee': employee,
            'declaration_period': date(2024, 3, 31),
            'cnss_number': '12345678',
            'employee_name': 'John Doe',
            'working_days_month1': '22',
            'working_days_month2': '20',
            'working_days_month3': '23',
            'total_working_days': Decimal('65.00'),
            'actual_remuneration': Decimal('150000.00'),
            'contribution_ceiling': Decimal('120000.00'),
            'hire_date': '01/01/2023',
            'cnss_contribution_employee': Decimal('3600.00'),
            'cnss_contribution_employer': Decimal('7200.00')
        }

    def test_cnss_declaration_creation(self, cnss_declaration_data):
        """Test creating a CNSS declaration"""
        declaration = CNSSDeclaration.objects.create(**cnss_declaration_data)
        
        assert declaration.id is not None
        assert declaration.employee.first_name == "John"
        assert declaration.cnss_number == "12345678"
        assert declaration.declaration_period == date(2024, 3, 31)
        assert declaration.status == 'draft'
        assert declaration.total_working_days == Decimal('65.00')
        assert declaration.actual_remuneration == Decimal('150000.00')

    def test_cnss_declaration_str_representation(self, cnss_declaration_data):
        """Test string representation of CNSS declaration"""
        declaration = CNSSDeclaration.objects.create(**cnss_declaration_data)
        expected = "CNSS Declaration - John Doe (2024-03-31)"
        assert str(declaration) == expected

    def test_cnss_declaration_db_table(self):
        """Test that CNSS declaration uses correct database table"""
        assert CNSSDeclaration._meta.db_table == 'listenominativecnss'

    def test_cnss_declaration_ordering(self, employee):
        """Test CNSS declaration ordering"""
        # Create declarations with different dates and names
        CNSSDeclaration.objects.create(
            employee=employee,
            declaration_period=date(2024, 1, 31),
            employee_name="Alice Smith"
        )
        CNSSDeclaration.objects.create(
            employee=employee,
            declaration_period=date(2024, 3, 31),
            employee_name="Bob Johnson"
        )
        
        declarations = list(CNSSDeclaration.objects.all())
        # Should be ordered by -declaration_period, employee_name
        assert declarations[0].declaration_period == date(2024, 3, 31)
        assert declarations[1].declaration_period == date(2024, 1, 31)

    def test_cnss_declaration_unique_constraint(self, employee):
        """Test unique constraint on employee and declaration_period"""
        period = date(2024, 3, 31)
        
        # Create first declaration
        CNSSDeclaration.objects.create(
            employee=employee,
            declaration_period=period,
            employee_name="John Doe"
        )
        
        # Attempt to create duplicate should fail
        with pytest.raises(IntegrityError):
            CNSSDeclaration.objects.create(
                employee=employee,
                declaration_period=period,
                employee_name="John Doe Duplicate"
            )

    def test_cnss_is_quarterly_declaration(self, cnss_declaration_data):
        """Test is_quarterly_declaration property"""
        declaration = CNSSDeclaration.objects.create(**cnss_declaration_data)
        assert declaration.is_quarterly_declaration is True
        
        # Test with only partial months
        declaration.working_days_month3 = ""
        assert declaration.is_quarterly_declaration is False

    def test_cnss_calculate_total_contribution(self, cnss_declaration_data):
        """Test calculate_total_contribution method"""
        declaration = CNSSDeclaration.objects.create(**cnss_declaration_data)
        total = declaration.calculate_total_contribution()
        
        expected = Decimal('3600.00') + Decimal('7200.00')  # employee + employer
        assert total == expected
        assert declaration.total_cnss_contribution == expected

    def test_cnss_cascade_delete(self, cnss_declaration_data):
        """Test that CNSS declaration is deleted when employee is deleted"""
        declaration = CNSSDeclaration.objects.create(**cnss_declaration_data)
        employee = declaration.employee
        
        assert CNSSDeclaration.objects.count() == 1
        employee.delete()
        assert CNSSDeclaration.objects.count() == 0

    def test_cnss_status_choices(self, cnss_declaration_data):
        """Test status field choices"""
        declaration = CNSSDeclaration.objects.create(**cnss_declaration_data)
        
        # Test valid statuses
        for status, _ in declaration._meta.get_field('status').choices:
            declaration.status = status
            declaration.full_clean()  # Should not raise ValidationError
        
        # Test invalid status
        declaration.status = 'invalid_status'
        with pytest.raises(ValidationError):
            declaration.full_clean()


@pytest.mark.django_db
class TestCNAMDeclaration:
    """Test suite for CNAMDeclaration model"""

    @pytest.fixture
    def employee(self):
        """Create a test employee"""
        general_direction = GeneralDirection.objects.create(
            name="Test General Direction"
        )
        direction = Direction.objects.create(
            name="Test Direction",
            general_direction=general_direction
        )
        department = Department.objects.create(
            name="Test Department",
            direction=direction
        )
        position = Position.objects.create(
            name="Test Position"
        )
        
        return Employee.objects.create(
            first_name="Jane",
            last_name="Smith",
            cnss_number="11111111",
            cnam_number="22222222",
            national_id="1234567890",
            hire_date=date(2023, 1, 1),
            position=position,
            department=department,
            direction=direction,
            general_direction=general_direction
        )

    @pytest.fixture
    def cnam_declaration_data(self, employee):
        """Sample CNAM declaration data"""
        return {
            'employee': employee,
            'declaration_period': date(2024, 3, 31),
            'employee_function_number': 12345,
            'cnam_number': '22222222',
            'nni': '1234567890',
            'employee_name': 'Jane Smith',
            'entry_date': '01/01/2023',
            'taxable_base_month1': Decimal('50000.00'),
            'taxable_base_month2': Decimal('52000.00'),
            'taxable_base_month3': Decimal('48000.00'),
            'working_days_month1': Decimal('22.00'),
            'working_days_month2': Decimal('20.00'),
            'working_days_month3': Decimal('23.00'),
            'cnam_contribution_employee': Decimal('750.00'),
            'cnam_contribution_employer': Decimal('1500.00')
        }

    def test_cnam_declaration_creation(self, cnam_declaration_data):
        """Test creating a CNAM declaration"""
        declaration = CNAMDeclaration.objects.create(**cnam_declaration_data)
        
        assert declaration.id is not None
        assert declaration.employee.first_name == "Jane"
        assert declaration.cnam_number == "22222222"
        assert declaration.nni == "1234567890"
        assert declaration.declaration_period == date(2024, 3, 31)
        assert declaration.status == 'draft'
        assert declaration.taxable_base_month1 == Decimal('50000.00')

    def test_cnam_declaration_str_representation(self, cnam_declaration_data):
        """Test string representation of CNAM declaration"""
        declaration = CNAMDeclaration.objects.create(**cnam_declaration_data)
        expected = "CNAM Declaration - Jane Smith (2024-03-31)"
        assert str(declaration) == expected

    def test_cnam_declaration_db_table(self):
        """Test that CNAM declaration uses correct database table"""
        assert CNAMDeclaration._meta.db_table == 'listenominativecnam'

    def test_cnam_declaration_ordering(self, employee):
        """Test CNAM declaration ordering"""
        # Create declarations with different dates
        CNAMDeclaration.objects.create(
            employee=employee,
            declaration_period=date(2024, 1, 31),
            employee_name="Alice Smith"
        )
        CNAMDeclaration.objects.create(
            employee=employee,
            declaration_period=date(2024, 3, 31),
            employee_name="Bob Johnson"
        )
        
        # Note: Need to create second employee to avoid unique constraint
        employee2 = Employee.objects.create(
            first_name="Bob",
            last_name="Johnson",
            cnss_number="33333333",
            cnam_number="44444444"
        )
        CNAMDeclaration.objects.create(
            employee=employee2,
            declaration_period=date(2024, 3, 31),
            employee_name="Bob Johnson"
        )
        
        declarations = list(CNAMDeclaration.objects.all())
        # Should be ordered by -declaration_period, employee_name
        assert declarations[0].declaration_period == date(2024, 3, 31)

    def test_cnam_declaration_unique_constraint(self, employee):
        """Test unique constraint on employee and declaration_period"""
        period = date(2024, 3, 31)
        
        # Create first declaration
        CNAMDeclaration.objects.create(
            employee=employee,
            declaration_period=period,
            employee_name="Jane Smith"
        )
        
        # Attempt to create duplicate should fail
        with pytest.raises(IntegrityError):
            CNAMDeclaration.objects.create(
                employee=employee,
                declaration_period=period,
                employee_name="Jane Smith Duplicate"
            )

    def test_cnam_is_quarterly_declaration(self, cnam_declaration_data):
        """Test is_quarterly_declaration property"""
        declaration = CNAMDeclaration.objects.create(**cnam_declaration_data)
        assert declaration.is_quarterly_declaration is True
        
        # Test with no taxable base
        declaration.taxable_base_month1 = Decimal('0')
        declaration.taxable_base_month2 = Decimal('0')
        declaration.taxable_base_month3 = Decimal('0')
        assert declaration.is_quarterly_declaration is False

    def test_cnam_calculate_totals(self, cnam_declaration_data):
        """Test calculate_totals method"""
        declaration = CNAMDeclaration.objects.create(**cnam_declaration_data)
        totals = declaration.calculate_totals()
        
        expected_taxable = Decimal('50000.00') + Decimal('52000.00') + Decimal('48000.00')
        expected_days = Decimal('22.00') + Decimal('20.00') + Decimal('23.00')
        
        assert totals['total_taxable_base'] == expected_taxable
        assert totals['total_working_days'] == expected_days
        assert declaration.total_taxable_base == expected_taxable
        assert declaration.total_working_days == expected_days

    def test_cnam_calculate_totals_with_nulls(self, cnam_declaration_data):
        """Test calculate_totals method with null working days"""
        cnam_declaration_data['working_days_month2'] = None
        declaration = CNAMDeclaration.objects.create(**cnam_declaration_data)
        totals = declaration.calculate_totals()
        
        expected_days = Decimal('22.00') + Decimal('0.00') + Decimal('23.00')
        assert totals['total_working_days'] == expected_days

    def test_cnam_calculate_total_contribution(self, cnam_declaration_data):
        """Test calculate_total_contribution method"""
        declaration = CNAMDeclaration.objects.create(**cnam_declaration_data)
        total = declaration.calculate_total_contribution()
        
        expected = Decimal('750.00') + Decimal('1500.00')  # employee + employer
        assert total == expected
        assert declaration.total_cnam_contribution == expected

    def test_cnam_cascade_delete(self, cnam_declaration_data):
        """Test that CNAM declaration is deleted when employee is deleted"""
        declaration = CNAMDeclaration.objects.create(**cnam_declaration_data)
        employee = declaration.employee
        
        assert CNAMDeclaration.objects.count() == 1
        employee.delete()
        assert CNAMDeclaration.objects.count() == 0

    def test_cnam_status_choices(self, cnam_declaration_data):
        """Test status field choices"""
        declaration = CNAMDeclaration.objects.create(**cnam_declaration_data)
        
        # Test valid statuses
        for status, _ in declaration._meta.get_field('status').choices:
            declaration.status = status
            declaration.full_clean()  # Should not raise ValidationError

    def test_cnam_employee_function_number_field(self, cnam_declaration_data):
        """Test employee_function_number field accepts large integers"""
        cnam_declaration_data['employee_function_number'] = 9999999999999
        declaration = CNAMDeclaration.objects.create(**cnam_declaration_data)
        assert declaration.employee_function_number == 9999999999999


@pytest.mark.django_db
class TestComplianceReportingRelationships:
    """Test relationships between compliance models and other models"""

    @pytest.fixture
    def setup_employee_with_declarations(self):
        """Create employee with both CNSS and CNAM declarations"""
        general_direction = GeneralDirection.objects.create(
            name="Test General Direction"
        )
        direction = Direction.objects.create(
            name="Test Direction",
            general_direction=general_direction
        )
        department = Department.objects.create(
            name="Test Department",
            direction=direction
        )
        position = Position.objects.create(
            name="Test Position"
        )
        
        employee = Employee.objects.create(
            first_name="Test",
            last_name="Employee",
            cnss_number="99999999",
            cnam_number="88888888",
            hire_date=date(2023, 1, 1),
            position=position,
            department=department,
            direction=direction,
            general_direction=general_direction
        )
        
        cnss_declaration = CNSSDeclaration.objects.create(
            employee=employee,
            declaration_period=date(2024, 3, 31),
            employee_name="Test Employee",
            cnss_number="99999999"
        )
        
        cnam_declaration = CNAMDeclaration.objects.create(
            employee=employee,
            declaration_period=date(2024, 3, 31),
            employee_name="Test Employee",
            cnam_number="88888888"
        )
        
        return {
            'employee': employee,
            'cnss_declaration': cnss_declaration,
            'cnam_declaration': cnam_declaration
        }

    def test_employee_reverse_relationships(self, setup_employee_with_declarations):
        """Test reverse relationships from Employee to declarations"""
        data = setup_employee_with_declarations
        employee = data['employee']
        
        # Test CNSS declarations relationship
        cnss_declarations = employee.cnss_declarations.all()
        assert cnss_declarations.count() == 1
        assert cnss_declarations.first().cnss_number == "99999999"
        
        # Test CNAM declarations relationship
        cnam_declarations = employee.cnam_declarations.all()
        assert cnam_declarations.count() == 1
        assert cnam_declarations.first().cnam_number == "88888888"

    def test_multiple_declarations_per_employee(self, setup_employee_with_declarations):
        """Test that employee can have multiple declarations for different periods"""
        employee = setup_employee_with_declarations['employee']
        
        # Add another quarter's declarations
        CNSSDeclaration.objects.create(
            employee=employee,
            declaration_period=date(2024, 6, 30),
            employee_name="Test Employee",
            cnss_number="99999999"
        )
        
        CNAMDeclaration.objects.create(
            employee=employee,
            declaration_period=date(2024, 6, 30),
            employee_name="Test Employee",
            cnam_number="88888888"
        )
        
        assert employee.cnss_declarations.count() == 2
        assert employee.cnam_declarations.count() == 2

    def test_declaration_audit_fields(self):
        """Test audit fields in declarations"""
        general_direction = GeneralDirection.objects.create(
            name="Test General Direction"
        )
        employee = Employee.objects.create(
            first_name="Audit",
            last_name="Test",
            general_direction=general_direction
        )
        
        cnss_declaration = CNSSDeclaration.objects.create(
            employee=employee,
            declaration_period=date(2024, 3, 31),
            employee_name="Audit Test",
            created_by="test_user",
            updated_by="test_user"
        )
        
        assert cnss_declaration.created_at is not None
        assert cnss_declaration.updated_at is not None
        assert cnss_declaration.created_by == "test_user"
        assert cnss_declaration.updated_by == "test_user"