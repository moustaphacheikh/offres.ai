import pytest
from django.test import TestCase
from django.db import IntegrityError
from datetime import date, datetime
from decimal import Decimal
from core.models import (
    Payroll, PayrollLineItem, WorkedDays,
    Employee, PayrollMotif, SystemParameters, PayrollElement,
    GeneralDirection, Direction, Department, Position,
    Activity, Bank, Origin, EmployeeStatus, SalaryGrade, User
)


class PayrollProcessingModelTest(TestCase):
    def setUp(self):
        """Create basic test data for payroll processing tests"""
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
        
        # Create employee
        self.employee = Employee.objects.create(
            first_name="Ahmed",
            last_name="Mohamed",
            hire_date=date(2020, 1, 15),
            position=self.position,
            department=self.department,
            direction=self.direction,
            general_direction=self.general_direction,
            salary_grade=self.salary_grade,
            bank=self.bank,
            bank_account="123456789",
            payment_mode="Virement",
            is_domiciled=True
        )
        
        # Create payroll motif
        self.payroll_motif = PayrollMotif.objects.create(
            name="Salaire Normal",
            employee_subject_to_its=True,
            employee_subject_to_cnss=True,
            employee_subject_to_cnam=True
        )
        
        # Create system parameters
        self.system_params = SystemParameters.objects.create(
            company_name="MCCMR Test",
            current_period=date(2024, 1, 1),
            next_period=date(2024, 2, 1),
            closure_period=date(2023, 12, 31),
            default_working_days=22,
            non_taxable_allowance_ceiling=10000,
            net_account=411000
        )
        
        # Create payroll elements
        self.salary_element = PayrollElement.objects.create(
            label="Salaire de Base",
            abbreviation="SAL_BASE",
            type="G",  # Gain
            affects_its=True,
            affects_cnss=True,
            affects_cnam=True
        )
        
        self.cnss_element = PayrollElement.objects.create(
            label="CNSS Employé",
            abbreviation="CNSS_EMP",
            type="D",  # Deduction
            affects_cnss=True
        )
        
        # Create user for processing
        self.user = User.objects.create_user(
            username="admin",
            full_name="Administrator",
            password="testpass123"
        )


class PayrollModelTest(PayrollProcessingModelTest):
    """Test cases for the Payroll model"""
    
    def test_payroll_creation(self):
        """Test basic payroll creation with required fields"""
        payroll = Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31),
            gross_taxable=Decimal('75000.00'),
            gross_non_taxable=Decimal('15000.00'),
            net_salary=Decimal('62000.00'),
            worked_days=Decimal('22.0')
        )
        
        self.assertEqual(payroll.employee, self.employee)
        self.assertEqual(payroll.motif, self.payroll_motif)
        self.assertEqual(payroll.parameters, self.system_params)
        self.assertEqual(payroll.period, date(2024, 1, 31))
        self.assertEqual(payroll.gross_taxable, Decimal('75000.00'))
        self.assertEqual(payroll.gross_non_taxable, Decimal('15000.00'))
        self.assertEqual(payroll.net_salary, Decimal('62000.00'))
        self.assertEqual(payroll.worked_days, Decimal('22.0'))
        
    def test_payroll_str_representation(self):
        """Test string representation of payroll"""
        payroll = Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31)
        )
        
        expected_str = f"{self.employee.full_name} - 2024-01 ({self.payroll_motif.name})"
        self.assertEqual(str(payroll), expected_str)
        
    def test_payroll_unique_constraint(self):
        """Test unique constraint on employee, period, and motif"""
        # Create first payroll
        Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31)
        )
        
        # Try to create duplicate payroll - should raise IntegrityError
        with self.assertRaises(IntegrityError):
            Payroll.objects.create(
                employee=self.employee,
                motif=self.payroll_motif,
                parameters=self.system_params,
                period=date(2024, 1, 31)
            )
    
    def test_total_gross_property(self):
        """Test total gross calculation property"""
        payroll = Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31),
            gross_taxable=Decimal('75000.00'),
            gross_non_taxable=Decimal('15000.00')
        )
        
        self.assertEqual(payroll.total_gross, Decimal('90000.00'))
    
    def test_total_deductions_property(self):
        """Test total deductions calculation property"""
        payroll = Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31),
            cnss_employee=Decimal('3750.00'),
            cnam_employee=Decimal('1500.00'),
            its_total=Decimal('12000.00'),
            gross_deductions=Decimal('2000.00'),
            net_deductions=Decimal('1000.00')
        )
        
        expected_total = Decimal('20250.00')  # 3750 + 1500 + 12000 + 2000 + 1000
        self.assertEqual(payroll.total_deductions, expected_total)
    
    def test_employer_contributions_total_property(self):
        """Test employer contributions total calculation"""
        payroll = Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31),
            rcnss=Decimal('7500.00'),
            rcnam=Decimal('3000.00')
        )
        
        self.assertEqual(payroll.employer_contributions_total, Decimal('10500.00'))
    
    def test_update_denormalized_fields(self):
        """Test updating denormalized fields from employee data"""
        payroll = Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31)
        )
        
        # Check that denormalized fields are populated correctly
        self.assertEqual(payroll.position_name, self.position.name)
        self.assertEqual(payroll.department_name, self.department.name)
        self.assertEqual(payroll.direction_name, self.direction.name)
        self.assertEqual(payroll.general_direction_name, self.general_direction.name)
        self.assertEqual(payroll.bank_name, self.bank.name)
        self.assertEqual(payroll.bank_account_number, self.employee.bank_account)
        self.assertEqual(payroll.payment_mode, self.employee.payment_mode)
        self.assertEqual(payroll.is_domiciled, self.employee.is_domiciled)
    
    def test_payroll_with_processing_user(self):
        """Test payroll with processing user information"""
        payroll = Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31),
            processed_by=self.user
        )
        
        self.assertEqual(payroll.processed_by, self.user)


class PayrollLineItemModelTest(PayrollProcessingModelTest):
    """Test cases for the PayrollLineItem model"""
    
    def setUp(self):
        super().setUp()
        self.payroll = Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31)
        )
    
    def test_payroll_line_item_creation(self):
        """Test basic payroll line item creation"""
        line_item = PayrollLineItem.objects.create(
            payroll=self.payroll,
            employee=self.employee,
            payroll_element=self.salary_element,
            motif=self.payroll_motif,
            base_amount=Decimal('75000.00'),
            quantity=Decimal('1.0'),
            calculated_amount=Decimal('75000.00')
        )
        
        self.assertEqual(line_item.payroll, self.payroll)
        self.assertEqual(line_item.employee, self.employee)
        self.assertEqual(line_item.payroll_element, self.salary_element)
        self.assertEqual(line_item.motif, self.payroll_motif)
        self.assertEqual(line_item.base_amount, Decimal('75000.00'))
        self.assertEqual(line_item.quantity, Decimal('1.0'))
        self.assertEqual(line_item.calculated_amount, Decimal('75000.00'))
    
    def test_line_item_str_representation(self):
        """Test string representation of payroll line item"""
        line_item = PayrollLineItem.objects.create(
            payroll=self.payroll,
            employee=self.employee,
            payroll_element=self.salary_element,
            motif=self.payroll_motif,
            calculated_amount=Decimal('75000.00')
        )
        
        expected_str = f"{self.employee.full_name} - {self.salary_element.label}: 75000.00"
        self.assertEqual(str(line_item), expected_str)
    
    def test_unique_constraint_payroll_element(self):
        """Test unique constraint on payroll and payroll_element"""
        # Create first line item
        PayrollLineItem.objects.create(
            payroll=self.payroll,
            employee=self.employee,
            payroll_element=self.salary_element,
            motif=self.payroll_motif
        )
        
        # Try to create duplicate line item - should raise IntegrityError
        with self.assertRaises(IntegrityError):
            PayrollLineItem.objects.create(
                payroll=self.payroll,
                employee=self.employee,
                payroll_element=self.salary_element,
                motif=self.payroll_motif
            )
    
    def test_is_gain_property(self):
        """Test is_gain property for earnings"""
        line_item = PayrollLineItem.objects.create(
            payroll=self.payroll,
            employee=self.employee,
            payroll_element=self.salary_element,  # Type 'G'
            motif=self.payroll_motif
        )
        
        self.assertTrue(line_item.is_gain)
        self.assertFalse(line_item.is_deduction)
    
    def test_is_deduction_property(self):
        """Test is_deduction property for deductions"""
        line_item = PayrollLineItem.objects.create(
            payroll=self.payroll,
            employee=self.employee,
            payroll_element=self.cnss_element,  # Type 'D'
            motif=self.payroll_motif
        )
        
        self.assertTrue(line_item.is_deduction)
        self.assertFalse(line_item.is_gain)
    
    def test_calculate_amount_with_base_and_quantity(self):
        """Test amount calculation with both base amount and quantity"""
        line_item = PayrollLineItem.objects.create(
            payroll=self.payroll,
            employee=self.employee,
            payroll_element=self.salary_element,
            motif=self.payroll_motif,
            base_amount=Decimal('3500.00'),
            quantity=Decimal('22.0')
        )
        
        calculated = line_item.calculate_amount()
        self.assertEqual(calculated, Decimal('77000.00'))  # 3500 * 22
        self.assertEqual(line_item.calculated_amount, Decimal('77000.00'))
    
    def test_calculate_amount_with_base_only(self):
        """Test amount calculation with base amount only"""
        line_item = PayrollLineItem.objects.create(
            payroll=self.payroll,
            employee=self.employee,
            payroll_element=self.salary_element,
            motif=self.payroll_motif,
            base_amount=Decimal('75000.00')
        )
        
        calculated = line_item.calculate_amount()
        self.assertEqual(calculated, Decimal('75000.00'))
        self.assertEqual(line_item.calculated_amount, Decimal('75000.00'))
    
    def test_calculate_amount_with_quantity_only(self):
        """Test amount calculation with quantity only"""
        line_item = PayrollLineItem.objects.create(
            payroll=self.payroll,
            employee=self.employee,
            payroll_element=self.salary_element,
            motif=self.payroll_motif,
            quantity=Decimal('5000.00')
        )
        
        calculated = line_item.calculate_amount()
        self.assertEqual(calculated, Decimal('5000.00'))
        self.assertEqual(line_item.calculated_amount, Decimal('5000.00'))


class WorkedDaysModelTest(PayrollProcessingModelTest):
    """Test cases for the WorkedDays model"""
    
    def test_worked_days_creation(self):
        """Test basic worked days creation"""
        worked_days = WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 1, 31),
            worked_days=Decimal('22.0')
        )
        
        self.assertEqual(worked_days.employee, self.employee)
        self.assertEqual(worked_days.motif, self.payroll_motif)
        self.assertEqual(worked_days.period, date(2024, 1, 31))
        self.assertEqual(worked_days.worked_days, Decimal('22.0'))
    
    def test_worked_days_str_representation(self):
        """Test string representation of worked days"""
        worked_days = WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 1, 31),
            worked_days=Decimal('20.5')
        )
        
        expected_str = f"{self.employee.full_name} - 2024-01: 20.5 days ({self.payroll_motif.name})"
        self.assertEqual(str(worked_days), expected_str)
    
    def test_unique_constraint_employee_motif_period(self):
        """Test unique constraint on employee, motif, and period"""
        # Create first worked days record
        WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 1, 31),
            worked_days=Decimal('22.0')
        )
        
        # Try to create duplicate record - should raise IntegrityError
        with self.assertRaises(IntegrityError):
            WorkedDays.objects.create(
                employee=self.employee,
                motif=self.payroll_motif,
                period=date(2024, 1, 31),
                worked_days=Decimal('20.0')
            )
    
    def test_is_full_time_equivalent_property(self):
        """Test is_full_time_equivalent property"""
        # Full-time employee (22 days)
        full_time = WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 1, 31),
            worked_days=Decimal('22.0')
        )
        self.assertTrue(full_time.is_full_time_equivalent)
        
        # Part-time employee (15 days)
        part_time = WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 2, 29),
            worked_days=Decimal('15.0')
        )
        self.assertFalse(part_time.is_full_time_equivalent)
        
        # Overtime employee (25 days)
        overtime = WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 3, 31),
            worked_days=Decimal('25.0')
        )
        self.assertTrue(overtime.is_full_time_equivalent)
    
    def test_work_ratio_property(self):
        """Test work ratio calculation"""
        # Full-time employee (22 days) - ratio should be 1.0
        full_time = WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 1, 31),
            worked_days=Decimal('22.0')
        )
        self.assertEqual(full_time.work_ratio, 1.0)
        
        # Half-time employee (11 days) - ratio should be 0.5
        half_time = WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 2, 29),
            worked_days=Decimal('11.0')
        )
        self.assertEqual(half_time.work_ratio, 0.5)
        
        # Overtime employee (26.4 days) - ratio should be 1.2
        overtime = WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 3, 31),
            worked_days=Decimal('26.4')
        )
        self.assertEqual(overtime.work_ratio, 1.2)
        
        # No work (0 days) - ratio should be 0.0
        no_work = WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 4, 30),
            worked_days=Decimal('0.0')
        )
        self.assertEqual(no_work.work_ratio, 0.0)


class PayrollProcessingIntegrationTest(PayrollProcessingModelTest):
    """Integration tests for payroll processing models working together"""
    
    def test_complete_payroll_processing_workflow(self):
        """Test complete workflow with all three models"""
        # 1. Create worked days record
        worked_days = WorkedDays.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            period=date(2024, 1, 31),
            worked_days=Decimal('22.0')
        )
        
        # 2. Create payroll record
        payroll = Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31),
            worked_days=worked_days.worked_days,
            gross_taxable=Decimal('75000.00'),
            net_salary=Decimal('62000.00'),
            processed_by=self.user
        )
        
        # 3. Create payroll line items
        salary_item = PayrollLineItem.objects.create(
            payroll=payroll,
            employee=self.employee,
            payroll_element=self.salary_element,
            motif=self.payroll_motif,
            base_amount=Decimal('75000.00'),
            quantity=Decimal('1.0'),
            calculated_amount=Decimal('75000.00')
        )
        
        cnss_item = PayrollLineItem.objects.create(
            payroll=payroll,
            employee=self.employee,
            payroll_element=self.cnss_element,
            motif=self.payroll_motif,
            base_amount=Decimal('75000.00'),
            quantity=Decimal('0.05'),  # 5% CNSS rate
            calculated_amount=Decimal('3750.00')
        )
        
        # Verify relationships and calculations
        self.assertEqual(payroll.worked_days, worked_days.worked_days)
        self.assertEqual(payroll.line_items.count(), 2)
        
        # Verify line items
        gains = [item for item in payroll.line_items.all() if item.is_gain]
        deductions = [item for item in payroll.line_items.all() if item.is_deduction]
        
        self.assertEqual(len(gains), 1)
        self.assertEqual(len(deductions), 1)
        self.assertEqual(gains[0].calculated_amount, Decimal('75000.00'))
        self.assertEqual(deductions[0].calculated_amount, Decimal('3750.00'))
    
    def test_payroll_relationships(self):
        """Test foreign key relationships and cascade behavior"""
        # Create payroll with line items
        payroll = Payroll.objects.create(
            employee=self.employee,
            motif=self.payroll_motif,
            parameters=self.system_params,
            period=date(2024, 1, 31)
        )
        
        line_item = PayrollLineItem.objects.create(
            payroll=payroll,
            employee=self.employee,
            payroll_element=self.salary_element,
            motif=self.payroll_motif
        )
        
        # Verify reverse relationships
        self.assertIn(payroll, self.employee.payrolls.all())
        self.assertIn(line_item, payroll.line_items.all())
        self.assertIn(line_item, self.employee.payroll_line_items.all())
        
        # Test cascade delete
        payroll_id = payroll.id
        line_item_id = line_item.id
        
        # Delete payroll should cascade delete line items
        payroll.delete()
        
        # Verify payroll and line item are deleted
        self.assertFalse(Payroll.objects.filter(id=payroll_id).exists())
        self.assertFalse(PayrollLineItem.objects.filter(id=line_item_id).exists())