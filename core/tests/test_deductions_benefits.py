import pytest
from decimal import Decimal
from datetime import date, timedelta
from django.core.exceptions import ValidationError
from django.db import IntegrityError
from django.utils import timezone

from core.models import (
    InstallmentDeduction, 
    InstallmentTranche,
    Employee, 
    PayrollElement,
    GeneralDirection,
    Direction,
    Department,
    Position,
    Bank,
    Origin,
    Activity,
    EmployeeStatus,
    SalaryGrade
)


@pytest.fixture
def sample_general_direction():
    """Create a sample general direction for testing"""
    return GeneralDirection.objects.create(
        name="IT General Direction"
    )


@pytest.fixture 
def sample_direction(sample_general_direction):
    """Create a sample direction for testing"""
    return Direction.objects.create(
        name="Software Development",
        general_direction=sample_general_direction
    )


@pytest.fixture
def sample_department(sample_direction):
    """Create a sample department for testing"""
    return Department.objects.create(
        name="Backend Development",
        direction=sample_direction
    )


@pytest.fixture
def sample_position():
    """Create a sample position for testing"""
    return Position.objects.create(
        name="Software Engineer"
    )


@pytest.fixture
def sample_bank():
    """Create a sample bank for testing"""
    return Bank.objects.create(
        name="Test Bank"
    )


@pytest.fixture
def sample_origin():
    """Create a sample origin for testing"""
    return Origin.objects.create(label="Internal")


@pytest.fixture
def sample_activity():
    """Create a sample activity for testing"""
    return Activity.objects.create(name="Development")


@pytest.fixture
def sample_employee_status():
    """Create a sample employee status for testing"""
    return EmployeeStatus.objects.create(name="Permanent")


@pytest.fixture
def sample_salary_grade(sample_employee_status):
    """Create a sample salary grade for testing"""
    return SalaryGrade.objects.create(
        category="A1",
        base_salary=50000,
        category_name="A1",
        level=1,
        status=sample_employee_status
    )


@pytest.fixture
def sample_employee(sample_department, sample_position, sample_bank, 
                   sample_origin, sample_activity, sample_salary_grade):
    """Create a sample employee for testing"""
    return Employee.objects.create(
        last_name="Doe",
        first_name="John",
        hire_date=date.today() - timedelta(days=365),
        department=sample_department,
        position=sample_position,
        bank=sample_bank,
        origin=sample_origin,
        activity=sample_activity,
        salary_grade=sample_salary_grade
    )


@pytest.fixture
def sample_payroll_element():
    """Create a sample payroll element for testing"""
    return PayrollElement.objects.create(
        label="Salary Advance",
        abbreviation="SAL_ADV",
        type="D",  # Deduction
        is_active=True
    )


@pytest.fixture
def sample_installment_deduction(sample_employee, sample_payroll_element):
    """Create a sample installment deduction for testing"""
    return InstallmentDeduction.objects.create(
        employee=sample_employee,
        payroll_element=sample_payroll_element,
        agreement_date=date.today(),
        capital=Decimal('10000.00'),
        installment_amount=Decimal('1000.00'),
        is_active=True,
        note="Monthly salary advance repayment"
    )


@pytest.fixture
def sample_installment_tranche(sample_installment_deduction):
    """Create a sample installment tranche for testing"""
    return InstallmentTranche.objects.create(
        installment_deduction=sample_installment_deduction,
        period=date.today(),
        amount=Decimal('1000.00'),
        sequence_number=1
    )


@pytest.mark.django_db
class TestInstallmentDeduction:
    """Test cases for InstallmentDeduction model"""
    
    def test_create_installment_deduction(self, sample_employee, sample_payroll_element):
        """Test creating a basic installment deduction"""
        deduction = InstallmentDeduction.objects.create(
            employee=sample_employee,
            payroll_element=sample_payroll_element,
            agreement_date=date.today(),
            capital=Decimal('5000.00'),
            installment_amount=Decimal('500.00')
        )
        
        assert deduction.id is not None
        assert deduction.employee == sample_employee
        assert deduction.payroll_element == sample_payroll_element
        assert deduction.capital == Decimal('5000.00')
        assert deduction.installment_amount == Decimal('500.00')
        assert deduction.is_active is True
        assert deduction.is_settled is False
    
    def test_installment_deduction_str_representation(self, sample_installment_deduction):
        """Test string representation of installment deduction"""
        expected = f"{sample_installment_deduction.employee} - {sample_installment_deduction.payroll_element} ({sample_installment_deduction.capital})"
        assert str(sample_installment_deduction) == expected
    
    def test_installment_deduction_default_values(self, sample_employee, sample_payroll_element):
        """Test default values for installment deduction fields"""
        deduction = InstallmentDeduction.objects.create(
            employee=sample_employee,
            payroll_element=sample_payroll_element,
            agreement_date=date.today()
        )
        
        assert deduction.capital == 0
        assert deduction.installment_amount == 0
        assert deduction.is_active is True
        assert deduction.is_settled is False
        assert deduction.note == ""
    
    def test_remaining_balance_with_no_tranches(self, sample_installment_deduction):
        """Test remaining balance calculation with no paid tranches"""
        assert sample_installment_deduction.remaining_balance == float(sample_installment_deduction.capital)
    
    def test_remaining_balance_with_paid_tranches(self, sample_installment_deduction):
        """Test remaining balance calculation with some paid tranches"""
        # Create some tranches
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today() - timedelta(days=30),
            amount=Decimal('1000.00'),
            is_paid=True,
            sequence_number=1
        )
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today(),
            amount=Decimal('1000.00'),
            is_paid=True,
            sequence_number=2
        )
        
        expected_remaining = float(sample_installment_deduction.capital) - 2000.00
        assert sample_installment_deduction.remaining_balance == expected_remaining
    
    def test_total_paid_property(self, sample_installment_deduction):
        """Test total paid calculation"""
        # Create paid and unpaid tranches
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today() - timedelta(days=60),
            amount=Decimal('1000.00'),
            is_paid=True,
            sequence_number=1
        )
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today() - timedelta(days=30),
            amount=Decimal('1000.00'),
            is_paid=True,
            sequence_number=2
        )
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today(),
            amount=Decimal('1000.00'),
            is_paid=False,  # This should not be counted
            sequence_number=3
        )
        
        assert sample_installment_deduction.total_paid == 2000.00


@pytest.mark.django_db
class TestInstallmentTranche:
    """Test cases for InstallmentTranche model"""
    
    def test_create_installment_tranche(self, sample_installment_deduction):
        """Test creating a basic installment tranche"""
        tranche = InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today(),
            amount=Decimal('500.00'),
            sequence_number=1
        )
        
        assert tranche.id is not None
        assert tranche.installment_deduction == sample_installment_deduction
        assert tranche.period == date.today()
        assert tranche.amount == Decimal('500.00')
        assert tranche.is_paid is False
        assert tranche.payment_date is None
        assert tranche.sequence_number == 1
    
    def test_installment_tranche_str_representation(self, sample_installment_tranche):
        """Test string representation of installment tranche"""
        expected = f"{sample_installment_tranche.installment_deduction.employee} - {sample_installment_tranche.period} - {sample_installment_tranche.amount}"
        assert str(sample_installment_tranche) == expected
    
    def test_installment_tranche_default_values(self, sample_installment_deduction):
        """Test default values for installment tranche fields"""
        tranche = InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today()
        )
        
        assert tranche.amount == 0
        assert tranche.is_paid is False
        assert tranche.payment_date is None
        assert tranche.sequence_number == 1
    
    def test_mark_as_paid_method(self, sample_installment_tranche):
        """Test marking tranche as paid"""
        payment_date = date.today()
        sample_installment_tranche.mark_as_paid(payment_date)
        
        sample_installment_tranche.refresh_from_db()
        assert sample_installment_tranche.is_paid is True
        assert sample_installment_tranche.payment_date == payment_date
    
    def test_mark_as_paid_without_date(self, sample_installment_tranche):
        """Test marking tranche as paid without specifying date"""
        sample_installment_tranche.mark_as_paid()
        
        sample_installment_tranche.refresh_from_db()
        assert sample_installment_tranche.is_paid is True
        assert sample_installment_tranche.payment_date == timezone.now().date()
    
    def test_mark_as_unpaid_method(self, sample_installment_tranche):
        """Test marking tranche as unpaid"""
        # First mark as paid
        sample_installment_tranche.mark_as_paid()
        sample_installment_tranche.refresh_from_db()
        assert sample_installment_tranche.is_paid is True
        
        # Then mark as unpaid
        sample_installment_tranche.mark_as_unpaid()
        sample_installment_tranche.refresh_from_db()
        assert sample_installment_tranche.is_paid is False
        assert sample_installment_tranche.payment_date is None
    
    def test_unique_together_constraint(self, sample_installment_deduction):
        """Test unique together constraint for installment deduction, period, and sequence number"""
        # Create first tranche
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today(),
            amount=Decimal('1000.00'),
            sequence_number=1
        )
        
        # Try to create duplicate - should raise IntegrityError
        with pytest.raises(IntegrityError):
            InstallmentTranche.objects.create(
                installment_deduction=sample_installment_deduction,
                period=date.today(),
                amount=Decimal('500.00'),
                sequence_number=1  # Same sequence number for same period
            )


@pytest.mark.django_db
class TestInstallmentDeductionRelationships:
    """Test cases for InstallmentDeduction relationships"""
    
    def test_employee_relationship(self, sample_employee, sample_payroll_element):
        """Test employee foreign key relationship"""
        deduction = InstallmentDeduction.objects.create(
            employee=sample_employee,
            payroll_element=sample_payroll_element,
            agreement_date=date.today(),
            capital=Decimal('3000.00')
        )
        
        assert deduction.employee == sample_employee
        assert deduction in sample_employee.installment_deductions.all()
    
    def test_payroll_element_relationship(self, sample_employee, sample_payroll_element):
        """Test payroll element foreign key relationship"""
        deduction = InstallmentDeduction.objects.create(
            employee=sample_employee,
            payroll_element=sample_payroll_element,
            agreement_date=date.today(),
            capital=Decimal('3000.00')
        )
        
        assert deduction.payroll_element == sample_payroll_element
        assert deduction in sample_payroll_element.installment_deductions.all()
    
    def test_cascade_deletion_employee(self, sample_installment_deduction, sample_employee):
        """Test that deleting employee cascades to installment deductions"""
        deduction_id = sample_installment_deduction.id
        sample_employee.delete()
        
        assert not InstallmentDeduction.objects.filter(id=deduction_id).exists()
    
    def test_cascade_deletion_payroll_element(self, sample_installment_deduction, sample_payroll_element):
        """Test that deleting payroll element cascades to installment deductions"""
        deduction_id = sample_installment_deduction.id
        sample_payroll_element.delete()
        
        assert not InstallmentDeduction.objects.filter(id=deduction_id).exists()


@pytest.mark.django_db
class TestInstallmentTrancheRelationships:
    """Test cases for InstallmentTranche relationships"""
    
    def test_installment_deduction_relationship(self, sample_installment_deduction):
        """Test installment deduction foreign key relationship"""
        tranche = InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today(),
            amount=Decimal('1000.00')
        )
        
        assert tranche.installment_deduction == sample_installment_deduction
        assert tranche in sample_installment_deduction.installment_tranches.all()
    
    def test_cascade_deletion_installment_deduction(self, sample_installment_tranche, sample_installment_deduction):
        """Test that deleting installment deduction cascades to tranches"""
        tranche_id = sample_installment_tranche.id
        sample_installment_deduction.delete()
        
        assert not InstallmentTranche.objects.filter(id=tranche_id).exists()


class TestInstallmentDeductionMeta:
    """Test cases for InstallmentDeduction Meta configuration"""
    
    def test_database_table_name(self):
        """Test that the correct database table name is used"""
        assert InstallmentDeduction._meta.db_table == 'retenuesaecheances'
    
    def test_verbose_names(self):
        """Test verbose names are set correctly"""
        assert InstallmentDeduction._meta.verbose_name == 'Installment Deduction'
        assert InstallmentDeduction._meta.verbose_name_plural == 'Installment Deductions'
    
    def test_ordering(self):
        """Test that ordering is set correctly"""
        expected_ordering = ['agreement_date', 'employee__last_name', 'employee__first_name']
        assert InstallmentDeduction._meta.ordering == expected_ordering


class TestInstallmentTrancheMeta:
    """Test cases for InstallmentTranche Meta configuration"""
    
    def test_database_table_name(self):
        """Test that the correct database table name is used"""
        assert InstallmentTranche._meta.db_table == 'tranchesretenuesaecheances'
    
    def test_verbose_names(self):
        """Test verbose names are set correctly"""
        assert InstallmentTranche._meta.verbose_name == 'Installment Tranche'
        assert InstallmentTranche._meta.verbose_name_plural == 'Installment Tranches'
    
    def test_ordering(self):
        """Test that ordering is set correctly"""
        expected_ordering = ['installment_deduction', 'period', 'sequence_number']
        assert InstallmentTranche._meta.ordering == expected_ordering
    
    def test_unique_together(self):
        """Test unique together constraint is configured"""
        expected_unique = ['installment_deduction', 'period', 'sequence_number']
        assert list(InstallmentTranche._meta.unique_together[0]) == expected_unique


@pytest.mark.django_db
class TestInstallmentDeductionBusinessLogic:
    """Test business logic and calculations"""
    
    def test_fully_paid_installment(self, sample_installment_deduction):
        """Test installment that has been fully paid"""
        # Create tranches that add up to the full capital
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today() - timedelta(days=90),
            amount=Decimal('2500.00'),
            is_paid=True,
            sequence_number=1
        )
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today() - timedelta(days=60),
            amount=Decimal('2500.00'),
            is_paid=True,
            sequence_number=2
        )
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today() - timedelta(days=30),
            amount=Decimal('2500.00'),
            is_paid=True,
            sequence_number=3
        )
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today(),
            amount=Decimal('2500.00'),
            is_paid=True,
            sequence_number=4
        )
        
        assert sample_installment_deduction.total_paid == 10000.00
        assert sample_installment_deduction.remaining_balance == 0.00
    
    def test_partial_payment_scenario(self, sample_installment_deduction):
        """Test installment with partial payments"""
        # Create mix of paid and unpaid tranches
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today() - timedelta(days=60),
            amount=Decimal('1500.00'),
            is_paid=True,
            sequence_number=1
        )
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today() - timedelta(days=30),
            amount=Decimal('1500.00'),
            is_paid=True,
            sequence_number=2
        )
        InstallmentTranche.objects.create(
            installment_deduction=sample_installment_deduction,
            period=date.today(),
            amount=Decimal('1500.00'),
            is_paid=False,  # Not yet paid
            sequence_number=3
        )
        
        assert sample_installment_deduction.total_paid == 3000.00
        assert sample_installment_deduction.remaining_balance == 7000.00