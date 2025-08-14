import pytest
from django.test import TestCase
from django.db import IntegrityError
from datetime import date, datetime
from core.models import (
    Employee, GeneralDirection, Direction, Department, Position,
    Activity, Bank, Origin, EmployeeStatus, SalaryGrade
)


class EmployeeModelTest(TestCase):
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
        
        # Create basic employee
        self.employee = Employee.objects.create(
            first_name="Ahmed",
            last_name="Mohamed",
            hire_date=date(2020, 1, 15),
            position=self.position,
            department=self.department,
            salary_grade=self.salary_grade
        )

    def test_employee_basic_creation(self):
        """Test basic employee creation with minimal required fields"""
        employee = Employee.objects.create(
            first_name="Fatima",
            last_name="Ali"
        )
        self.assertEqual(employee.first_name, "Fatima")
        self.assertEqual(employee.last_name, "Ali")
        self.assertTrue(employee.is_active)
        self.assertFalse(employee.on_leave)
        self.assertFalse(employee.in_termination)
        
    def test_employee_str(self):
        """Test string representation"""
        self.assertEqual(str(self.employee), "Ahmed Mohamed")
        
    def test_employee_full_name_property(self):
        """Test full_name property"""
        self.assertEqual(self.employee.full_name, "Ahmed Mohamed")
        
        # Test with empty names
        employee = Employee.objects.create(first_name="", last_name="Single")
        self.assertEqual(employee.full_name, "Single")

    def test_employee_personal_information(self):
        """Test personal information fields"""
        employee = Employee.objects.create(
            first_name="Omar",
            last_name="Hassan",
            father_name="Hassan Mohamed",
            mother_name="Aicha Ali",
            national_id="1234567890123",
            birth_date=date(1985, 3, 20),
            birth_place="Nouakchott",
            nationality="Mauritanian",
            gender="M",
            marital_status="Married",
            children_count=2,
            phone="+222 45 67 89 10",
            email="omar.hassan@company.mr",
            address="Avenue Charles de Gaulle, Nouakchott"
        )
        
        self.assertEqual(employee.national_id, "1234567890123")
        self.assertEqual(employee.birth_date, date(1985, 3, 20))
        self.assertEqual(employee.nationality, "Mauritanian")
        self.assertEqual(employee.children_count, 2)

    def test_employee_employment_details(self):
        """Test employment-related fields"""
        employee = Employee.objects.create(
            first_name="Mariam",
            last_name="Ould",
            hire_date=date(2021, 6, 1),
            seniority_date=date(2021, 6, 1),
            contract_type="CDI",
            contract_end_date=None,
            classification="Engineer Grade 1",
            status="Active",
            origin_structure="External Hire",
            work_location="Nouakchott Office"
        )
        
        self.assertEqual(employee.contract_type, "CDI")
        self.assertEqual(employee.classification, "Engineer Grade 1")
        self.assertTrue(employee.is_active)

    def test_employee_social_security_fields(self):
        """Test social security and tax configuration"""
        employee = Employee.objects.create(
            first_name="Said",
            last_name="Ould Mohamed",
            cnss_number="123456789",
            cnss_date=date(2020, 1, 1),
            cnam_number="987654321",
            cnss_detached=False,
            cnam_detached=False,
            is_domiciled=True,
            its_exempt=False
        )
        
        self.assertEqual(employee.cnss_number, "123456789")
        self.assertEqual(employee.cnam_number, "987654321")
        self.assertTrue(employee.is_domiciled)
        self.assertFalse(employee.its_exempt)

    def test_employee_banking_information(self):
        """Test banking and payment fields"""
        employee = Employee.objects.create(
            first_name="Khadija",
            last_name="Mint",
            bank_account="123456789012",
            payment_mode="Bank Transfer",
            bank=self.bank
        )
        
        self.assertEqual(employee.bank_account, "123456789012")
        self.assertEqual(employee.payment_mode, "Bank Transfer")
        self.assertEqual(employee.bank, self.bank)

    def test_employee_salary_benefits(self):
        """Test salary and benefits calculations"""
        employee = Employee.objects.create(
            first_name="Mohamed",
            last_name="Ould Ahmed",
            annual_budget=600000.00,
            contract_hours_per_week=40.0,
            seniority_rate=0.15,
            psra_rate=0.10,
            notice_months=3.0
        )
        
        self.assertEqual(employee.annual_budget, 600000.00)
        self.assertEqual(employee.contract_hours_per_week, 40.0)
        self.assertEqual(employee.seniority_rate, 0.15)

    def test_employee_work_schedule_matrix(self):
        """Test the 21 boolean fields for work schedule (7 days × 3 shifts)"""
        employee = Employee.objects.create(
            first_name="Aminata",
            last_name="Ba",
            # Monday
            monday_day_shift=True,
            monday_first_shift=False,
            monday_weekend=False,
            # Tuesday  
            tuesday_day_shift=True,
            tuesday_first_shift=False,
            tuesday_weekend=False,
            # Set some weekend work
            saturday_weekend=True,
            sunday_weekend=True
        )
        
        self.assertTrue(employee.monday_day_shift)
        self.assertFalse(employee.monday_weekend)
        self.assertTrue(employee.saturday_weekend)
        self.assertTrue(employee.sunday_weekend)

    def test_employee_foreign_employee_information(self):
        """Test expatriate and foreign employee fields"""
        employee = Employee.objects.create(
            first_name="John",
            last_name="Smith",
            is_expatriate=True,
            passport_number="P123456789",
            passport_issue_date=date(2019, 5, 15),
            passport_expiry_date=date(2029, 5, 15),
            visa_start_date=date(2020, 1, 1),
            visa_end_date=date(2025, 1, 1),
            residence_card_number="RC987654321",
            work_permit_number="WP111222333",
            work_permit_issue_date=date(2020, 1, 1),
            work_permit_expiry_date=date(2025, 1, 1)
        )
        
        self.assertTrue(employee.is_expatriate)
        self.assertEqual(employee.passport_number, "P123456789")
        self.assertTrue(employee.is_foreign_employee)

    def test_is_foreign_employee_property(self):
        """Test the is_foreign_employee property logic"""
        # Test expatriate
        expat = Employee.objects.create(
            first_name="Marie", 
            last_name="Dupont", 
            is_expatriate=True
        )
        self.assertTrue(expat.is_foreign_employee)
        
        # Test with passport but not expatriate
        foreign_worker = Employee.objects.create(
            first_name="Carlos", 
            last_name="Garcia", 
            passport_number="ES123456789"
        )
        self.assertTrue(foreign_worker.is_foreign_employee)
        
        # Test local employee
        local = Employee.objects.create(first_name="Aicha", last_name="Ould")
        self.assertFalse(local.is_foreign_employee)

    def test_employee_organizational_relationships(self):
        """Test foreign key relationships with organizational models"""
        employee = Employee.objects.create(
            first_name="Moussa",
            last_name="Diallo",
            position=self.position,
            department=self.department,
            direction=self.direction,
            general_direction=self.general_direction,
            salary_grade=self.salary_grade,
            activity=self.activity,
            origin=self.origin,
            bank=self.bank
        )
        
        self.assertEqual(employee.position, self.position)
        self.assertEqual(employee.department, self.department)
        self.assertEqual(employee.salary_grade, self.salary_grade)
        
        # Test reverse relationships
        self.assertIn(employee, self.position.employees.all())
        self.assertIn(employee, self.department.employees.all())

    def test_employee_cumulative_fields(self):
        """Test cumulative calculation fields"""
        employee = Employee.objects.create(
            first_name="Fatou",
            last_name="Diop",
            cumulative_days_initial=250.5,
            cumulative_taxable_initial=1500000.00,
            cumulative_non_taxable_initial=50000.00,
            cumulative_12dm_initial=120000.00,
            last_departure_initial=date(2021, 12, 15)
        )
        
        self.assertEqual(employee.cumulative_days_initial, 250.5)
        self.assertEqual(employee.cumulative_taxable_initial, 1500000.00)

    def test_employee_tax_reimbursement_rates(self):
        """Test tax reimbursement rate fields"""
        employee = Employee.objects.create(
            first_name="Boubou",
            last_name="Kane",
            cnss_reimbursement_rate=0.01,
            cnam_reimbursement_rate=0.005,
            its_tranche1_reimbursement=0.15,
            its_tranche2_reimbursement=0.20,
            its_tranche3_reimbursement=0.25
        )
        
        self.assertEqual(employee.cnss_reimbursement_rate, 0.01)
        self.assertEqual(employee.its_tranche2_reimbursement, 0.20)

    def test_employee_special_fields(self):
        """Test special system fields"""
        employee = Employee.objects.create(
            first_name="Aissata",
            last_name="Tall",
            timeclock_employee_id=12345,
            category_date=date(2020, 1, 1),
            category_years=5,
            auto_category_advancement=True,
            payslip_note="Special allowance for remote work",
            ps_service=True,
            ps_service_id="PS001",
            password="temp123"
        )
        
        self.assertEqual(employee.timeclock_employee_id, 12345)
        self.assertTrue(employee.auto_category_advancement)
        self.assertEqual(employee.payslip_note, "Special allowance for remote work")

    def test_employee_audit_fields(self):
        """Test automatic audit fields"""
        employee = Employee.objects.create(
            first_name="Abdou", 
            last_name="Sow"
        )
        
        self.assertIsInstance(employee.created_at, datetime)
        self.assertIsInstance(employee.updated_at, datetime)
        
        # Test that updated_at changes on save
        original_updated = employee.updated_at
        employee.last_name = "Sow Updated"
        employee.save()
        self.assertGreater(employee.updated_at, original_updated)

    def test_employee_ordering(self):
        """Test default ordering by last_name, first_name"""
        e1 = Employee.objects.create(first_name="Zara", last_name="Alpha")
        e2 = Employee.objects.create(first_name="Adam", last_name="Beta") 
        e3 = Employee.objects.create(first_name="Ben", last_name="Alpha")
        
        employees = list(Employee.objects.all())
        # Should order: Alpha (Ben), Alpha (Zara), Beta (Adam), Mohamed (Ahmed - from setUp)
        self.assertEqual(employees[0].last_name, "Alpha")
        self.assertEqual(employees[0].first_name, "Ben")  # Ben Alpha comes first
        self.assertEqual(employees[1].first_name, "Zara")  # Zara Alpha comes second

    def test_employee_db_table(self):
        """Test correct database table name"""
        self.assertEqual(Employee._meta.db_table, 'employe')

    def test_employee_protect_relationships(self):
        """Test PROTECT constraints prevent deletion of referenced objects"""
        employee = Employee.objects.create(
            first_name="Test",
            last_name="Employee", 
            position=self.position,
            salary_grade=self.salary_grade
        )
        
        # Should not be able to delete position if employee references it
        with self.assertRaises(Exception):  # Could be ProtectedError or IntegrityError
            self.position.delete()

    def test_employee_set_null_relationships(self):
        """Test SET_NULL relationships (origin, bank)"""
        employee = Employee.objects.create(
            first_name="Test",
            last_name="Employee",
            origin=self.origin,
            bank=self.bank
        )
        
        # Deleting origin should set employee.origin to NULL
        self.origin.delete()
        employee.refresh_from_db()
        self.assertIsNone(employee.origin)
        
        # Employee should still exist
        self.assertTrue(Employee.objects.filter(id=employee.id).exists())


class EmployeeComplexScenariosTest(TestCase):
    """Test complex employee scenarios and business logic"""
    
    def setUp(self):
        """Set up data for complex tests"""
        self.status = EmployeeStatus.objects.create(name="Permanent")
        self.salary_grade = SalaryGrade.objects.create(
            category="A1", level=5, base_salary=50000, 
            category_name="Senior", status=self.status
        )
    
    def test_employee_lifecycle(self):
        """Test complete employee lifecycle"""
        # Hiring
        employee = Employee.objects.create(
            first_name="Lifecycle",
            last_name="Test",
            hire_date=date(2020, 1, 1),
            is_active=True
        )
        
        # Promotion
        employee.salary_grade = self.salary_grade
        employee.seniority_date = date(2020, 1, 1)
        employee.save()
        
        # Leave
        employee.on_leave = True
        employee.save()
        
        # Return from leave  
        employee.on_leave = False
        employee.save()
        
        # Termination
        employee.in_termination = True
        employee.termination_date = date(2023, 12, 31)
        employee.termination_reason = "End of contract"
        employee.is_active = False
        employee.save()
        
        self.assertFalse(employee.is_active)
        self.assertTrue(employee.in_termination)
        self.assertEqual(employee.termination_reason, "End of contract")

    def test_multiple_work_schedules(self):
        """Test different work schedule combinations"""
        # Full-time day worker
        day_worker = Employee.objects.create(
            first_name="Day",
            last_name="Worker",
            monday_day_shift=True, tuesday_day_shift=True,
            wednesday_day_shift=True, thursday_day_shift=True,
            friday_day_shift=True
        )
        
        # Night shift worker
        night_worker = Employee.objects.create(
            first_name="Night", 
            last_name="Worker",
            monday_first_shift=True, tuesday_first_shift=True,
            wednesday_first_shift=True, thursday_first_shift=True,
            friday_first_shift=True
        )
        
        # Weekend worker
        weekend_worker = Employee.objects.create(
            first_name="Weekend",
            last_name="Worker", 
            saturday_weekend=True, sunday_weekend=True
        )
        
        # Verify schedules
        self.assertTrue(day_worker.monday_day_shift)
        self.assertTrue(night_worker.friday_first_shift)
        self.assertTrue(weekend_worker.saturday_weekend)