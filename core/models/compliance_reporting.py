from django.db import models
from django.core.validators import MinValueValidator, MaxValueValidator
from decimal import Decimal
from .employee import Employee
from .payroll_processing import Payroll


class CNSSDeclaration(models.Model):
    """
    CNSS (Caisse Nationale de Sécurité Sociale) tax declarations
    Equivalent to Listenominativecnss.java
    """
    
    # Primary identification (matches Java Long id)
    id = models.BigAutoField(primary_key=True)
    
    # Employee reference
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='cnss_declarations'
    )
    
    # Payroll reference for traceability
    payroll = models.ForeignKey(
        Payroll,
        on_delete=models.CASCADE,
        related_name='cnss_declarations',
        blank=True,
        null=True,
        help_text="Source payroll record for this declaration"
    )
    
    # Declaration details
    declaration_period = models.DateField()  # periode
    cnss_number = models.CharField(max_length=50, blank=True)  # noCnssemploye
    employee_name = models.CharField(max_length=200)  # nomEmploye
    
    # Working days breakdown by months (matches Java String fields)
    working_days_month1 = models.CharField(max_length=10, blank=True)  # nbJour1erMois
    working_days_month2 = models.CharField(max_length=10, blank=True)  # nbJour2emeMois
    working_days_month3 = models.CharField(max_length=10, blank=True)  # nbJour3emeMois
    total_working_days = models.FloatField(default=0.0)  # totalNbJour (matches Java double)
    
    # Financial details
    actual_remuneration = models.FloatField(default=0.0)  # remunerationReeles (matches Java double)
    contribution_ceiling = models.FloatField(default=15000.0)  # plafond (matches Java default)
    
    # Employment dates (matches Java String fields)
    hire_date = models.CharField(max_length=20, blank=True)  # dateEmbauche
    termination_date = models.CharField(max_length=20, blank=True)  # dateDebauche
    
    # Employee exemption status
    is_cnss_exempt = models.BooleanField(
        default=False,
        help_text="Employee is exempt from CNSS contributions (detacheCnss)"
    )
    
    # Contribution rates and calculations
    employee_contribution_rate = models.DecimalField(
        max_digits=5, 
        decimal_places=2, 
        default=Decimal('1.00'),
        validators=[MinValueValidator(Decimal('0.00')), MaxValueValidator(Decimal('100.00'))],
        help_text="CNSS employee contribution rate (percentage)"
    )
    employer_contribution_rate = models.DecimalField(
        max_digits=5, 
        decimal_places=2, 
        default=Decimal('1.00'),
        validators=[MinValueValidator(Decimal('0.00')), MaxValueValidator(Decimal('100.00'))],
        help_text="CNSS employer contribution rate (percentage)"
    )
    
    # Contribution amounts
    cnss_contribution_employee = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    cnss_contribution_employer = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    total_cnss_contribution = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    
    # Reimbursement tracking
    employee_reimbursement_rate = models.DecimalField(
        max_digits=5,
        decimal_places=2,
        default=Decimal('0.00'),
        validators=[MinValueValidator(Decimal('0.00')), MaxValueValidator(Decimal('100.00'))],
        help_text="Employee CNSS reimbursement rate (percentage)"
    )
    reimbursement_amount = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="CNSS reimbursement amount (RCNSS)"
    )
    
    # Compliance status
    status = models.CharField(
        max_length=20,
        choices=[
            ('draft', 'Draft'),
            ('submitted', 'Submitted'),
            ('approved', 'Approved'),
            ('rejected', 'Rejected')
        ],
        default='draft'
    )
    
    # Batch processing
    submission_batch = models.CharField(
        max_length=50,
        blank=True,
        help_text="Batch identifier for grouped submissions"
    )
    
    # Submission details
    submission_date = models.DateTimeField(blank=True, null=True)
    submission_reference = models.CharField(max_length=50, blank=True)
    
    # Regulatory period type
    period_type = models.CharField(
        max_length=20,
        choices=[
            ('monthly', 'Monthly'),
            ('quarterly', 'Quarterly'),
            ('annual', 'Annual')
        ],
        default='quarterly',
        help_text="Type of reporting period"
    )
    
    # Notes and remarks
    remarks = models.TextField(blank=True)
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    created_by = models.CharField(max_length=100, blank=True)
    updated_by = models.CharField(max_length=100, blank=True)
    
    class Meta:
        db_table = 'listenominativecnss'
        ordering = ['-declaration_period', 'employee_name']
        unique_together = ['employee', 'declaration_period']
        
    def __str__(self):
        return f"CNSS Declaration - {self.employee_name} ({self.declaration_period})"
    
    @property
    def is_quarterly_declaration(self):
        """Check if this is a quarterly declaration with 3 months data"""
        return bool(self.working_days_month1 and self.working_days_month2 and self.working_days_month3)
    
    def calculate_cnss_contribution(self, tauxDevise=1.0, usedITS=2018):
        """
        Calculate CNSS contribution based on Java CNSSm() method
        Matches the calculation logic from PaieClass.java
        """
        if self.is_cnss_exempt:
            self.cnss_contribution_employee = 0
            self.cnss_contribution_employer = 0
            self.total_cnss_contribution = 0
            return 0
        
        # Apply exchange rate
        adjusted_remuneration = self.actual_remuneration * tauxDevise
        
        # Apply plafond ceiling (15000 from Java)
        if adjusted_remuneration <= self.contribution_ceiling:
            contribution_base = adjusted_remuneration
        else:
            contribution_base = self.contribution_ceiling
        
        # Calculate contributions (1% rate from Java)
        employee_contribution = contribution_base * (self.employee_contribution_rate / 100)
        employer_contribution = contribution_base * (self.employer_contribution_rate / 100)
        
        # Store calculated amounts
        self.cnss_contribution_employee = employee_contribution
        self.cnss_contribution_employer = employer_contribution
        self.total_cnss_contribution = employee_contribution + employer_contribution
        
        return self.total_cnss_contribution
    
    def calculate_cnss_reimbursement(self):
        """
        Calculate CNSS reimbursement based on Java RCNSSm() method
        """
        if self.is_cnss_exempt or self.employee_reimbursement_rate == 0:
            self.reimbursement_amount = 0
            return 0
        
        # Use same base as contribution calculation
        if self.actual_remuneration <= self.contribution_ceiling:
            reimbursement_base = self.actual_remuneration
        else:
            reimbursement_base = self.contribution_ceiling
        
        # Calculate reimbursement
        base_contribution = reimbursement_base / 100  # 1% rate
        self.reimbursement_amount = base_contribution * (self.employee_reimbursement_rate / 100)
        
        return self.reimbursement_amount
    
    def calculate_total_contribution(self):
        """Calculate total CNSS contribution"""
        self.total_cnss_contribution = self.cnss_contribution_employee + self.cnss_contribution_employer
        return self.total_cnss_contribution
    
    def validate_quarterly_data(self):
        """
        Validate that quarterly declaration has proper monthly data
        """
        if self.period_type == 'quarterly':
            return bool(self.working_days_month1 and self.working_days_month2 and self.working_days_month3)
        return True


class CNAMDeclaration(models.Model):
    """
    CNAM (Caisse Nationale d'Assurance Maladie) health insurance declarations
    Equivalent to Listenominativecnam.java
    """
    
    # Primary identification (matches Java long no)
    id = models.BigAutoField(primary_key=True)  # no
    
    # Employee reference
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='cnam_declarations'
    )
    
    # Payroll reference for traceability
    payroll = models.ForeignKey(
        Payroll,
        on_delete=models.CASCADE,
        related_name='cnam_declarations',
        blank=True,
        null=True,
        help_text="Source payroll record for this declaration"
    )
    
    # Declaration details
    declaration_period = models.DateField()  # periode
    employee_function_number = models.BigIntegerField(blank=True, null=True)  # matriculeFonc
    cnam_number = models.CharField(max_length=50, blank=True)  # noCnam
    nni = models.CharField(max_length=50, blank=True)  # nni (National ID)
    employee_name = models.CharField(max_length=200)  # nomEmploye
    
    # Employment dates (matches Java String fields)
    entry_date = models.CharField(max_length=20, blank=True)  # dateEntre
    exit_date = models.CharField(max_length=20, blank=True)  # dateSortie
    
    # Taxable salary base by months (matches Java double fields)
    taxable_base_month1 = models.FloatField(default=0.0)  # assieteSoumiseMois1
    taxable_base_month2 = models.FloatField(default=0.0)  # assieteSoumiseMois2
    taxable_base_month3 = models.FloatField(default=0.0)  # assieteSoumiseMois3
    
    # Working days by months (matches Java Double fields - nullable)
    working_days_month1 = models.FloatField(blank=True, null=True)  # nbJourMois1
    working_days_month2 = models.FloatField(blank=True, null=True)  # nbJourMois2
    working_days_month3 = models.FloatField(blank=True, null=True)  # nbJourMois3
    
    # Employee exemption status
    is_cnam_exempt = models.BooleanField(
        default=False,
        help_text="Employee is exempt from CNAM contributions (detacheCnam)"
    )
    
    # Contribution rates
    employee_contribution_rate = models.DecimalField(
        max_digits=5, 
        decimal_places=2, 
        default=Decimal('4.00'),
        validators=[MinValueValidator(Decimal('0.00')), MaxValueValidator(Decimal('100.00'))],
        help_text="CNAM employee contribution rate (percentage, default 4%)"
    )
    employer_contribution_rate = models.DecimalField(
        max_digits=5, 
        decimal_places=2, 
        default=Decimal('4.00'),
        validators=[MinValueValidator(Decimal('0.00')), MaxValueValidator(Decimal('100.00'))],
        help_text="CNAM employer contribution rate (percentage, default 4%)"
    )
    
    # Contribution amounts
    cnam_contribution_employee = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    cnam_contribution_employer = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    total_cnam_contribution = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    
    # Reimbursement tracking
    employee_reimbursement_rate = models.DecimalField(
        max_digits=5,
        decimal_places=2,
        default=Decimal('0.00'),
        validators=[MinValueValidator(Decimal('0.00')), MaxValueValidator(Decimal('100.00'))],
        help_text="Employee CNAM reimbursement rate (percentage)"
    )
    reimbursement_amount = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="CNAM reimbursement amount (RCNAM)"
    )
    
    # Compliance status
    status = models.CharField(
        max_length=20,
        choices=[
            ('draft', 'Draft'),
            ('submitted', 'Submitted'),
            ('approved', 'Approved'),
            ('rejected', 'Rejected')
        ],
        default='draft'
    )
    
    # Batch processing
    submission_batch = models.CharField(
        max_length=50,
        blank=True,
        help_text="Batch identifier for grouped submissions"
    )
    
    # Submission details
    submission_date = models.DateTimeField(blank=True, null=True)
    submission_reference = models.CharField(max_length=50, blank=True)
    
    # Regulatory period type
    period_type = models.CharField(
        max_length=20,
        choices=[
            ('monthly', 'Monthly'),
            ('quarterly', 'Quarterly'),
            ('annual', 'Annual')
        ],
        default='quarterly',
        help_text="Type of reporting period"
    )
    
    # Calculation details
    total_taxable_base = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    total_working_days = models.DecimalField(max_digits=5, decimal_places=2, default=0)
    
    # Notes and remarks
    remarks = models.TextField(blank=True)
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    created_by = models.CharField(max_length=100, blank=True)
    updated_by = models.CharField(max_length=100, blank=True)
    
    class Meta:
        db_table = 'listenominativecnam'
        ordering = ['-declaration_period', 'employee_name']
        unique_together = ['employee', 'declaration_period']
        
    def __str__(self):
        return f"CNAM Declaration - {self.employee_name} ({self.declaration_period})"
    
    @property
    def is_quarterly_declaration(self):
        """Check if this is a quarterly declaration with 3 months data"""
        return bool(self.taxable_base_month1 or self.taxable_base_month2 or self.taxable_base_month3)
    
    def calculate_totals(self):
        """Calculate total taxable base and working days"""
        self.total_taxable_base = (
            self.taxable_base_month1 + 
            self.taxable_base_month2 + 
            self.taxable_base_month3
        )
        
        working_days = [
            self.working_days_month1 or Decimal('0'),
            self.working_days_month2 or Decimal('0'),
            self.working_days_month3 or Decimal('0')
        ]
        self.total_working_days = sum(working_days)
        
        return {
            'total_taxable_base': self.total_taxable_base,
            'total_working_days': self.total_working_days
        }
    
    def calculate_cnam_contribution(self):
        """
        Calculate CNAM contribution based on Java CNAMm() method
        Matches the calculation logic from PaieClass.java (4% rate)
        """
        if self.is_cnam_exempt:
            self.cnam_contribution_employee = 0
            self.cnam_contribution_employer = 0
            self.total_cnam_contribution = 0
            return 0
        
        # Calculate total taxable base first
        self.calculate_totals()
        
        # Calculate contributions (4% rate from Java)
        employee_contribution = self.total_taxable_base * (self.employee_contribution_rate / 100)
        employer_contribution = self.total_taxable_base * (self.employer_contribution_rate / 100)
        
        # Store calculated amounts
        self.cnam_contribution_employee = employee_contribution
        self.cnam_contribution_employer = employer_contribution
        self.total_cnam_contribution = employee_contribution + employer_contribution
        
        return self.total_cnam_contribution
    
    def calculate_cnam_reimbursement(self):
        """
        Calculate CNAM reimbursement based on Java RCNAMm() method
        """
        if self.is_cnam_exempt or self.employee_reimbursement_rate == 0:
            self.reimbursement_amount = 0
            return 0
        
        # Use total taxable base for reimbursement calculation
        base_contribution = self.total_taxable_base * 0.04  # 4% rate
        self.reimbursement_amount = base_contribution * (self.employee_reimbursement_rate / 100)
        
        return self.reimbursement_amount
    
    def calculate_total_contribution(self):
        """Calculate total CNAM contribution"""
        self.total_cnam_contribution = self.cnam_contribution_employee + self.cnam_contribution_employer
        return self.total_cnam_contribution
    
    def validate_monthly_totals(self):
        """
        Validate that monthly taxable bases sum to total
        """
        calculated_total = (
            self.taxable_base_month1 + 
            self.taxable_base_month2 + 
            self.taxable_base_month3
        )
        return abs(calculated_total - self.total_taxable_base) < 0.01


class ITSDeclaration(models.Model):
    """
    ITS (Impôt sur Traitements et Salaires) - Income Tax on Wages and Salaries
    Based on ITS calculations found in PaieClass.java and Paramgen.java
    """
    
    # Primary identification
    id = models.BigAutoField(primary_key=True)
    
    # Employee reference
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='its_declarations'
    )
    
    # Payroll reference for traceability
    payroll = models.ForeignKey(
        Payroll,
        on_delete=models.CASCADE,
        related_name='its_declarations',
        blank=True,
        null=True,
        help_text="Source payroll record for this declaration"
    )
    
    # Declaration details
    declaration_period = models.DateField()
    employee_name = models.CharField(max_length=200)
    
    # Tax calculation parameters
    used_its_year = models.IntegerField(
        default=2018,
        help_text="ITS calculation year (usedITS from Java)"
    )
    
    # Employee exemption status
    is_its_exempt = models.BooleanField(
        default=False,
        help_text="Employee is exempt from ITS (exonoreIts)"
    )
    is_expatriate = models.BooleanField(
        default=False,
        help_text="Employee is expatriate (affects ITS calculation)"
    )
    
    # Income components
    gross_salary = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Gross salary subject to ITS"
    )
    benefits_in_kind = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Benefits in kind (AvantagesEnNature)"
    )
    taxable_income = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Total taxable income for ITS calculation"
    )
    
    # Deductions
    cnss_deduction = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="CNSS deduction (if deductionCnssdeIts enabled)"
    )
    cnam_deduction = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="CNAM deduction (if deductionCnamdeIts enabled)"
    )
    abatement = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Tax abatement amount"
    )
    
    # Tax calculation by tranches
    tranche1_base = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Income subject to tranche 1 rate (up to 9000)"
    )
    tranche1_rate = models.DecimalField(
        max_digits=5,
        decimal_places=2,
        default=Decimal('15.00'),
        help_text="Tranche 1 tax rate (15%)"
    )
    tranche1_tax = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Tax amount for tranche 1"
    )
    
    tranche2_base = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Income subject to tranche 2 rate"
    )
    tranche2_rate = models.DecimalField(
        max_digits=5,
        decimal_places=2,
        default=Decimal('20.00'),
        help_text="Tranche 2 tax rate (20%)"
    )
    tranche2_tax = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Tax amount for tranche 2"
    )
    
    tranche3_base = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Income subject to tranche 3 rate"
    )
    tranche3_rate = models.DecimalField(
        max_digits=5,
        decimal_places=2,
        default=Decimal('30.00'),
        help_text="Tranche 3 tax rate (30%)"
    )
    tranche3_tax = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Tax amount for tranche 3"
    )
    
    # Total tax amounts
    total_its_tax = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Total ITS tax amount"
    )
    
    # Reimbursement tracking
    tranche1_reimbursement_rate = models.DecimalField(
        max_digits=5,
        decimal_places=2,
        default=Decimal('0.00'),
        help_text="Tranche 1 reimbursement rate"
    )
    tranche2_reimbursement_rate = models.DecimalField(
        max_digits=5,
        decimal_places=2,
        default=Decimal('0.00'),
        help_text="Tranche 2 reimbursement rate"
    )
    tranche3_reimbursement_rate = models.DecimalField(
        max_digits=5,
        decimal_places=2,
        default=Decimal('0.00'),
        help_text="Tranche 3 reimbursement rate"
    )
    total_reimbursement = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=0,
        help_text="Total ITS reimbursement amount (RITS)"
    )
    
    # Configuration flags
    deduct_cnss_from_its = models.BooleanField(
        default=False,
        help_text="Whether to deduct CNSS from ITS calculation (deductionCnssdeIts)"
    )
    deduct_cnam_from_its = models.BooleanField(
        default=False,
        help_text="Whether to deduct CNAM from ITS calculation (deductionCnamdeIts)"
    )
    
    # Exchange rate
    exchange_rate = models.DecimalField(
        max_digits=10,
        decimal_places=4,
        default=Decimal('1.0000'),
        help_text="Exchange rate (tauxDevise)"
    )
    
    # ITS calculation mode
    its_mode = models.CharField(
        max_length=10,
        choices=[
            ('standard', 'Standard'),
            ('special', 'Special'),
            ('expatriate', 'Expatriate')
        ],
        default='standard',
        help_text="ITS calculation mode (modeITS)"
    )
    
    # Compliance status
    status = models.CharField(
        max_length=20,
        choices=[
            ('draft', 'Draft'),
            ('calculated', 'Calculated'),
            ('submitted', 'Submitted'),
            ('approved', 'Approved'),
            ('rejected', 'Rejected')
        ],
        default='draft'
    )
    
    # Batch processing
    submission_batch = models.CharField(
        max_length=50,
        blank=True,
        help_text="Batch identifier for grouped submissions"
    )
    
    # Submission details
    submission_date = models.DateTimeField(blank=True, null=True)
    submission_reference = models.CharField(max_length=50, blank=True)
    
    # Notes and remarks
    remarks = models.TextField(blank=True)
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    created_by = models.CharField(max_length=100, blank=True)
    updated_by = models.CharField(max_length=100, blank=True)
    
    class Meta:
        db_table = 'its_declaration'
        ordering = ['-declaration_period', 'employee_name']
        unique_together = ['employee', 'declaration_period']
        
    def __str__(self):
        return f"ITS Declaration - {self.employee_name} ({self.declaration_period})"
    
    def calculate_its_tax(self):
        """
        Calculate ITS tax based on Java ITS calculation methods
        Implements the progressive tax calculation from PaieClass.java
        """
        if self.is_its_exempt:
            self.total_its_tax = 0
            self.tranche1_tax = 0
            self.tranche2_tax = 0
            self.tranche3_tax = 0
            return 0
        
        # Calculate taxable income base
        taxable_base = self.gross_salary
        
        # Apply deductions if configured
        if self.deduct_cnss_from_its:
            taxable_base -= self.cnss_deduction
        if self.deduct_cnam_from_its:
            taxable_base -= self.cnam_deduction
        
        # Apply abatement
        taxable_base -= self.abatement
        
        # Handle benefits in kind (20% rule from Java)
        if self.benefits_in_kind > 0:
            sb_20_percent = (self.gross_salary - self.benefits_in_kind) * Decimal('0.2')
            if self.benefits_in_kind > sb_20_percent:
                taxable_base -= self.benefits_in_kind * Decimal('0.6')
        
        # Apply exchange rate
        taxable_base *= self.exchange_rate
        
        # Expatriate adjustment (half rates)
        rate_multiplier = Decimal('0.5') if self.is_expatriate else Decimal('1.0')
        
        # Calculate progressive tax
        remaining_income = max(taxable_base, 0)
        total_tax = Decimal('0')
        
        # Tranche 1: up to 9000 at 15%
        tranche1_ceiling = Decimal('9000')
        if remaining_income > 0:
            self.tranche1_base = min(remaining_income, tranche1_ceiling)
            self.tranche1_tax = self.tranche1_base * (self.tranche1_rate / 100) * rate_multiplier
            total_tax += self.tranche1_tax
            remaining_income -= self.tranche1_base
        
        # Tranche 2: 9001 to 27000 at 20%
        tranche2_ceiling = Decimal('18000')  # 27000 - 9000
        if remaining_income > 0:
            self.tranche2_base = min(remaining_income, tranche2_ceiling)
            self.tranche2_tax = self.tranche2_base * (self.tranche2_rate / 100) * rate_multiplier
            total_tax += self.tranche2_tax
            remaining_income -= self.tranche2_base
        
        # Tranche 3: above 27000 at 30%
        if remaining_income > 0:
            self.tranche3_base = remaining_income
            self.tranche3_tax = self.tranche3_base * (self.tranche3_rate / 100) * rate_multiplier
            total_tax += self.tranche3_tax
        
        self.total_its_tax = total_tax
        self.taxable_income = taxable_base
        
        return self.total_its_tax
    
    def calculate_its_reimbursement(self):
        """
        Calculate ITS reimbursement based on tranche rates
        """
        reimbursement = Decimal('0')
        
        # Tranche 1 reimbursement
        if self.tranche1_tax > 0:
            reimbursement += self.tranche1_tax * (self.tranche1_reimbursement_rate / 100)
        
        # Tranche 2 reimbursement
        if self.tranche2_tax > 0:
            reimbursement += self.tranche2_tax * (self.tranche2_reimbursement_rate / 100)
        
        # Tranche 3 reimbursement
        if self.tranche3_tax > 0:
            reimbursement += self.tranche3_tax * (self.tranche3_reimbursement_rate / 100)
        
        self.total_reimbursement = reimbursement
        return self.total_reimbursement