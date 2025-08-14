from django.db import models
from decimal import Decimal
from .employee import Employee


class CNSSDeclaration(models.Model):
    """
    CNSS (Caisse Nationale de Sécurité Sociale) tax declarations
    Equivalent to Listenominativecnss.java
    """
    
    # Primary identification
    id = models.AutoField(primary_key=True)
    
    # Employee reference
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='cnss_declarations'
    )
    
    # Declaration details
    declaration_period = models.DateField()  # periode
    cnss_number = models.CharField(max_length=50, blank=True)  # noCnssemploye
    employee_name = models.CharField(max_length=200)  # nomEmploye
    
    # Working days breakdown by months
    working_days_month1 = models.CharField(max_length=10, blank=True)  # nbJour1erMois
    working_days_month2 = models.CharField(max_length=10, blank=True)  # nbJour2emeMois
    working_days_month3 = models.CharField(max_length=10, blank=True)  # nbJour3emeMois
    total_working_days = models.DecimalField(max_digits=5, decimal_places=2, default=0)  # totalNbJour
    
    # Financial details
    actual_remuneration = models.DecimalField(max_digits=15, decimal_places=2, default=0)  # remunerationReeles
    contribution_ceiling = models.DecimalField(max_digits=15, decimal_places=2, default=0)  # plafond
    
    # Employment dates
    hire_date = models.CharField(max_length=20, blank=True)  # dateEmbauche
    termination_date = models.CharField(max_length=20, blank=True)  # dateDebauche
    
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
    
    # Submission details
    submission_date = models.DateTimeField(blank=True, null=True)
    submission_reference = models.CharField(max_length=50, blank=True)
    
    # Calculation details
    cnss_contribution_employee = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    cnss_contribution_employer = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    total_cnss_contribution = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    
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
    
    def calculate_total_contribution(self):
        """Calculate total CNSS contribution"""
        self.total_cnss_contribution = self.cnss_contribution_employee + self.cnss_contribution_employer
        return self.total_cnss_contribution


class CNAMDeclaration(models.Model):
    """
    CNAM (Caisse Nationale d'Assurance Maladie) health insurance declarations
    Equivalent to Listenominativecnam.java
    """
    
    # Primary identification
    id = models.AutoField(primary_key=True)  # no
    
    # Employee reference
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='cnam_declarations'
    )
    
    # Declaration details
    declaration_period = models.DateField()  # periode
    employee_function_number = models.BigIntegerField(blank=True, null=True)  # matriculeFonc
    cnam_number = models.CharField(max_length=50, blank=True)  # noCnam
    nni = models.CharField(max_length=50, blank=True)  # nni (National ID)
    employee_name = models.CharField(max_length=200)  # nomEmploye
    
    # Employment dates
    entry_date = models.CharField(max_length=20, blank=True)  # dateEntre
    exit_date = models.CharField(max_length=20, blank=True)  # dateSortie
    
    # Taxable salary base by months
    taxable_base_month1 = models.DecimalField(max_digits=15, decimal_places=2, default=0)  # assieteSoumiseMois1
    taxable_base_month2 = models.DecimalField(max_digits=15, decimal_places=2, default=0)  # assieteSoumiseMois2
    taxable_base_month3 = models.DecimalField(max_digits=15, decimal_places=2, default=0)  # assieteSoumiseMois3
    
    # Working days by months
    working_days_month1 = models.DecimalField(max_digits=5, decimal_places=2, blank=True, null=True)  # nbJourMois1
    working_days_month2 = models.DecimalField(max_digits=5, decimal_places=2, blank=True, null=True)  # nbJourMois2
    working_days_month3 = models.DecimalField(max_digits=5, decimal_places=2, blank=True, null=True)  # nbJourMois3
    
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
    
    # Submission details
    submission_date = models.DateTimeField(blank=True, null=True)
    submission_reference = models.CharField(max_length=50, blank=True)
    
    # Calculation details
    total_taxable_base = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    total_working_days = models.DecimalField(max_digits=5, decimal_places=2, default=0)
    cnam_contribution_employee = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    cnam_contribution_employer = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    total_cnam_contribution = models.DecimalField(max_digits=15, decimal_places=2, default=0)
    
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
    
    def calculate_total_contribution(self):
        """Calculate total CNAM contribution"""
        self.total_cnam_contribution = self.cnam_contribution_employee + self.cnam_contribution_employer
        return self.total_cnam_contribution