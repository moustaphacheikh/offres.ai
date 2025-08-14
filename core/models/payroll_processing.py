from django.db import models
from .employee import Employee
from .reference import PayrollMotif
from .payroll_elements import PayrollElement
from .system_config import SystemParameters, User


class Payroll(models.Model):
    """
    Main payroll records - equivalent to Paie.java
    
    Represents a complete payroll calculation for an employee in a specific period.
    Contains all calculated amounts for gross pay, taxes, deductions, and net pay.
    """
    
    # Primary Key
    id = models.AutoField(primary_key=True)
    
    # Foreign Key Relations
    employee = models.ForeignKey(
        Employee,
        on_delete=models.PROTECT,
        related_name='payrolls'
    )  # employe
    motif = models.ForeignKey(
        PayrollMotif,
        on_delete=models.PROTECT,
        related_name='payrolls'
    )  # motif
    parameters = models.ForeignKey(
        SystemParameters,
        on_delete=models.PROTECT,
        related_name='payrolls'
    )  # paramgen
    
    # Period and Classification
    period = models.DateField()  # periode
    category = models.CharField(max_length=20, blank=True)  # categorie
    contract_hours_month = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # contratHeureMois
    
    # Salary Calculations
    gross_taxable = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # bt (Brut Taxable)
    gross_non_taxable = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # bni (Brut Non Imposable)
    net_salary = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # net
    worked_days = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # njt (Nombre Jours Travaillés)
    overtime_hours = models.DecimalField(max_digits=22, decimal_places=2, blank=True, null=True)  # nbrHs (Nombre Heures Supplémentaires)
    
    # Tax Calculations - Employee Contributions
    cnss_employee = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # cnss
    cnam_employee = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # cnam
    its_total = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # its (Impôt Total)
    its_tranche1 = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # itsTranche1
    its_tranche2 = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # itsTranche2
    its_tranche3 = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # itsTranche3
    
    # Employer Contributions 
    rcnss = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # rcnss (Retenue CNSS patronale)
    rcnam = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # rcnam (Retenue CNAM patronale)
    rits = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # rits (Retenue ITS)
    
    # Base Amounts for Social Security
    cnss_base = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # biCnss (Base Imposable CNSS)
    cnam_base = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # biCnam (Base Imposable CNAM)
    avnat_base = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # biAvnat (Base Imposable Avantages Nature)
    
    # Deductions
    gross_deductions = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # retenuesBrut
    net_deductions = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # retenuesNet
    
    # Cumulative Tracking
    cumulative_taxable = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # cumulBi (Cumul Brut Imposable)
    cumulative_non_taxable = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # cumulBni (Cumul Brut Non Imposable)
    cumulative_days = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # cumulNjt (Cumul Nombre Jours Travaillés)
    
    # Display Fields
    net_in_words = models.TextField(blank=True)  # netEnLettre
    period_in_words = models.CharField(max_length=100, blank=True)  # periodeLettre
    
    # Banking Information (denormalized from employee)
    bank_name = models.CharField(max_length=50, blank=True)  # banque
    bank_account_number = models.CharField(max_length=50, blank=True)  # noCompteBanque
    payment_mode = models.CharField(max_length=50, blank=True)  # modePaiement
    is_domiciled = models.BooleanField(default=False)  # domicilie
    
    # Denormalized Organizational Fields (for performance in reports)
    position_name = models.CharField(max_length=100, blank=True)  # poste
    department_name = models.CharField(max_length=100, blank=True)  # departement
    direction_name = models.CharField(max_length=100, blank=True)  # direction
    general_direction_name = models.CharField(max_length=100, blank=True)  # directiongeneral
    
    # Period Range
    payroll_from_date = models.DateField(blank=True, null=True)  # paieDu
    payroll_to_date = models.DateField(blank=True, null=True)  # paieAu
    last_departure_date = models.DateField(blank=True, null=True)  # dateDernierDepart
    
    # Additional Fields
    note = models.TextField(blank=True)  # notePaie
    fte = models.DecimalField(max_digits=22, decimal_places=2, blank=True, null=True)  # fte (Full-Time Equivalent)
    status = models.CharField(max_length=50, blank=True)  # statut
    classification = models.CharField(max_length=200, blank=True)  # classification
    activity = models.CharField(max_length=100, blank=True)  # activite
    
    # Processing Information
    processed_by = models.ForeignKey(
        User,
        on_delete=models.PROTECT,
        related_name='processed_payrolls',
        blank=True,
        null=True
    )
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'paie'
        unique_together = ['employee', 'period', 'motif']
        ordering = ['-period', 'employee__last_name', 'employee__first_name']
        verbose_name = 'Payroll'
        verbose_name_plural = 'Payrolls'
    
    def __str__(self):
        return f"{self.employee.full_name} - {self.period.strftime('%Y-%m')} ({self.motif.name})"
    
    @property
    def total_gross(self):
        """Calculate total gross salary (taxable + non-taxable)"""
        return self.gross_taxable + self.gross_non_taxable
    
    @property
    def total_deductions(self):
        """Calculate total deductions (taxes + social security + other deductions)"""
        return (self.cnss_employee + self.cnam_employee + self.its_total + 
                self.gross_deductions + self.net_deductions)
    
    @property
    def employer_contributions_total(self):
        """Calculate total employer contributions"""
        return self.rcnss + self.rcnam
    
    def update_denormalized_fields(self):
        """Update denormalized fields from related employee data"""
        if self.employee:
            self.position_name = self.employee.position.name if self.employee.position else ''
            self.department_name = self.employee.department.name if self.employee.department else ''
            self.direction_name = self.employee.direction.name if self.employee.direction else ''
            self.general_direction_name = self.employee.general_direction.name if self.employee.general_direction else ''
            self.bank_name = self.employee.bank.name if self.employee.bank else ''
            self.bank_account_number = self.employee.bank_account
            self.payment_mode = self.employee.payment_mode
            self.is_domiciled = self.employee.is_domiciled
            self.classification = self.employee.classification
            self.activity = self.employee.activity.name if self.employee.activity else ''
            self.status = self.employee.status
    
    def save(self, *args, **kwargs):
        """Override save to update denormalized fields"""
        self.update_denormalized_fields()
        super().save(*args, **kwargs)


class PayrollLineItem(models.Model):
    """
    Individual payroll line items - equivalent to Rubriquepaie.java
    
    Represents individual calculation elements (salary components, deductions, benefits)
    that make up the total payroll for an employee in a specific period.
    """
    
    # Primary Key
    id = models.AutoField(primary_key=True)
    
    # Foreign Key Relations
    payroll = models.ForeignKey(
        Payroll,
        on_delete=models.CASCADE,
        related_name='line_items'
    )  # This links to the payroll record (derived from period/employee/motif)
    employee = models.ForeignKey(
        Employee,
        on_delete=models.PROTECT,
        related_name='payroll_line_items'
    )  # employe
    payroll_element = models.ForeignKey(
        PayrollElement,
        on_delete=models.PROTECT,
        related_name='line_items'
    )  # rubrique
    motif = models.ForeignKey(
        PayrollMotif,
        on_delete=models.PROTECT,
        related_name='line_items'
    )  # motif
    
    # Calculation Values
    base_amount = models.DecimalField(max_digits=22, decimal_places=2, blank=True, null=True)  # base
    quantity = models.DecimalField(max_digits=22, decimal_places=2, blank=True, null=True)  # nombre
    calculated_amount = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # montant
    
    # Flags
    is_fixed = models.BooleanField(default=False)  # fixe
    is_imported = models.BooleanField(default=False)  # importe
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'rubriquepaie'
        unique_together = ['payroll', 'payroll_element']
        ordering = ['payroll', 'payroll_element__label']
        verbose_name = 'Payroll Line Item'
        verbose_name_plural = 'Payroll Line Items'
    
    def __str__(self):
        return f"{self.payroll.employee.full_name} - {self.payroll_element.label}: {self.calculated_amount}"
    
    @property
    def is_gain(self):
        """Check if this line item is a gain/earning"""
        return self.payroll_element.type == 'G'
    
    @property
    def is_deduction(self):
        """Check if this line item is a deduction"""
        return self.payroll_element.type == 'D'
    
    def calculate_amount(self):
        """
        Calculate the amount based on base_amount and quantity
        This is a simplified calculation - in real implementation, 
        this would use the formula engine
        """
        if self.base_amount and self.quantity:
            self.calculated_amount = self.base_amount * self.quantity
        elif self.base_amount:
            self.calculated_amount = self.base_amount
        elif self.quantity:
            # If only quantity is provided, use it as the amount
            self.calculated_amount = self.quantity
        
        return self.calculated_amount


class WorkedDays(models.Model):
    """
    Days worked tracking - equivalent to Njtsalarie.java
    
    Tracks the number of days worked by an employee for a specific period and motif.
    This is used for attendance tracking and payroll calculations.
    """
    
    # Primary Key
    id = models.AutoField(primary_key=True)
    
    # Foreign Key Relations
    employee = models.ForeignKey(
        Employee,
        on_delete=models.PROTECT,
        related_name='worked_days_records'
    )  # employe
    motif = models.ForeignKey(
        PayrollMotif,
        on_delete=models.PROTECT,
        related_name='worked_days_records'
    )  # motif
    
    # Period and Days
    period = models.DateField()  # periode
    worked_days = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # njt
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'njtsalarie'
        unique_together = ['employee', 'motif', 'period']
        ordering = ['-period', 'employee__last_name', 'employee__first_name']
        verbose_name = 'Worked Days'
        verbose_name_plural = 'Worked Days'
    
    def __str__(self):
        return f"{self.employee.full_name} - {self.period.strftime('%Y-%m')}: {self.worked_days} days ({self.motif.name})"
    
    @property
    def is_full_time_equivalent(self):
        """
        Check if worked days represent full-time work
        Assuming standard 22 working days per month
        """
        return self.worked_days >= 22
    
    @property
    def work_ratio(self):
        """
        Calculate work ratio compared to standard month (22 days)
        Returns a decimal between 0 and 1 (or higher for overtime)
        """
        if self.worked_days:
            return float(self.worked_days) / 22.0
        return 0.0