from django.db import models
from django.core.validators import DecimalValidator
from decimal import Decimal
from .employee import Employee


class MasterPiece(models.Model):
    """
    Accounting master record - equivalent to Masterpiece.java
    Headers for accounting journal entries with summary amounts
    """
    
    # Primary identification
    numero = models.CharField(
        max_length=50,
        primary_key=True,
        help_text="Document number (sequence or timestamp based)"
    )
    
    # Service and document information
    libelle_service = models.CharField(
        max_length=200,
        default="Service Paie",
        help_text="Service description"
    )
    dateop = models.DateField(
        help_text="Operation date"
    )
    rubrique = models.CharField(
        max_length=100,
        help_text="Motif name for this accounting period"
    )
    beneficiaire = models.CharField(
        max_length=200,
        default="-",
        help_text="Beneficiary (legacy uses '-')"
    )
    
    # Financial totals
    total_debit = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=Decimal('0.00'),
        help_text="Total debit amount"
    )
    total_credit = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        default=Decimal('0.00'),
        help_text="Total credit amount"
    )
    
    # Workflow and audit fields
    initiateur = models.CharField(
        max_length=100,
        help_text="User who initiated the accounting piece"
    )
    init_hr = models.DateTimeField(
        help_text="Timestamp when piece was initiated"
    )
    
    # Integration status
    status = models.CharField(
        max_length=20,
        choices=[
            ('DRAFT', 'Draft'),
            ('VALIDATED', 'Validated'),
            ('EXPORTED', 'Exported'),
            ('INTEGRATED', 'Integrated to external system')
        ],
        default='DRAFT',
        help_text="Integration status with external accounting systems"
    )
    
    # Period tracking
    period = models.CharField(
        max_length=20,
        help_text="Payroll period (YYYY-MM format)"
    )
    motif = models.CharField(
        max_length=100,
        help_text="Payroll motif for this piece"
    )
    
    # External system references
    external_reference = models.CharField(
        max_length=100,
        blank=True,
        help_text="Reference from external accounting system"
    )
    export_date = models.DateTimeField(
        blank=True,
        null=True,
        help_text="Date when exported to external system"
    )
    
    # Approval workflow
    approved_by = models.CharField(
        max_length=100,
        blank=True,
        help_text="User who approved the piece"
    )
    approval_date = models.DateTimeField(
        blank=True,
        null=True,
        help_text="Date of approval"
    )
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'masterpiece'
        ordering = ['-dateop', '-created_at']
        indexes = [
            models.Index(fields=['period', 'motif']),
            models.Index(fields=['dateop']),
            models.Index(fields=['status']),
        ]
    
    def __str__(self):
        return f"MP-{self.numero} ({self.period}/{self.motif})"
    
    @property
    def is_balanced(self):
        """Check if debits equal credits"""
        return abs(self.total_debit - self.total_credit) <= Decimal('0.01')
    
    @property
    def balance_difference(self):
        """Return the difference between debits and credits"""
        return self.total_debit - self.total_credit
    
    def recalculate_totals(self):
        """Recalculate total debit and credit from detail pieces"""
        details = self.detailpieces.all()
        self.total_debit = sum(
            d.montant for d in details if d.sens == 'D'
        ) or Decimal('0.00')
        self.total_credit = sum(
            d.montant for d in details if d.sens == 'C'
        ) or Decimal('0.00')
        self.save(update_fields=['total_debit', 'total_credit', 'updated_at'])


class DetailPiece(models.Model):
    """
    Accounting detail record - equivalent to Detailpiece.java
    Individual journal entry lines with account postings
    """
    
    # Primary identification
    numligne = models.BigAutoField(
        primary_key=True,
        help_text="Line number (auto-generated)"
    )
    
    # Master piece reference
    nupiece = models.ForeignKey(
        MasterPiece,
        on_delete=models.CASCADE,
        related_name='detailpieces',
        help_text="Reference to master piece"
    )
    
    # Transaction details
    dateop = models.DateField(
        help_text="Operation date"
    )
    journal = models.CharField(
        max_length=10,
        default="PAI",
        help_text="Journal code (default: PAI for payroll)"
    )
    
    # Account information
    compte = models.CharField(
        max_length=50,
        help_text="Account number"
    )
    libelle = models.CharField(
        max_length=300,
        help_text="Transaction description"
    )
    intitulet = models.CharField(
        max_length=200,
        help_text="Account title/name"
    )
    
    # Financial information
    montant = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        help_text="Transaction amount"
    )
    sens = models.CharField(
        max_length=1,
        choices=[
            ('D', 'Debit'),
            ('C', 'Credit')
        ],
        help_text="Transaction direction (D=Debit, C=Credit)"
    )
    
    # Currency information (legacy constants)
    cvmro_montant = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        help_text="Mirror amount (same as montant)"
    )
    devise = models.CharField(
        max_length=10,
        default="UM",
        help_text="Currency code (default: UM)"
    )
    cours = models.DecimalField(
        max_digits=10,
        decimal_places=4,
        default=Decimal('1.0000'),
        help_text="Exchange rate (default: 1.0000)"
    )
    numero_cours = models.IntegerField(
        default=1,
        help_text="Exchange rate number (default: 1)"
    )
    
    # Employee reference (for employee-specific accounts)
    employee = models.ForeignKey(
        Employee,
        on_delete=models.SET_NULL,
        blank=True,
        null=True,
        related_name='accounting_details',
        help_text="Employee reference for individual postings"
    )
    
    # Account categorization
    account_type = models.CharField(
        max_length=20,
        choices=[
            ('RUBRIQUE', 'Payroll rubrique'),
            ('BANK', 'Bank transfer'),
            ('CASH', 'Cash payment'),
            ('ENGAGEMENT', 'Employee engagement'),
            ('STATUTORY', 'Statutory obligation'),
            ('OTHER', 'Other')
        ],
        help_text="Type of account posting"
    )
    
    # Chapter information (for UNL export)
    chapitre = models.CharField(
        max_length=50,
        blank=True,
        help_text="Chapter code for external systems"
    )
    
    # Integration tracking
    exported = models.BooleanField(
        default=False,
        help_text="Whether this line was exported to external system"
    )
    export_reference = models.CharField(
        max_length=100,
        blank=True,
        help_text="Reference from external system export"
    )
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'detailpiece'
        ordering = ['numligne']
        indexes = [
            models.Index(fields=['nupiece', 'compte']),
            models.Index(fields=['dateop']),
            models.Index(fields=['sens']),
            models.Index(fields=['account_type']),
            models.Index(fields=['employee']),
        ]
    
    def __str__(self):
        direction = "Dr" if self.sens == 'D' else "Cr"
        return f"DP-{self.numligne}: {self.compte} {direction} {self.montant}"
    
    def save(self, *args, **kwargs):
        # Ensure cvmro_montant mirrors montant
        self.cvmro_montant = self.montant
        super().save(*args, **kwargs)
    
    @property
    def is_debit(self):
        """Check if this is a debit entry"""
        return self.sens == 'D'
    
    @property
    def is_credit(self):
        """Check if this is a credit entry"""
        return self.sens == 'C'
    
    @property
    def formatted_account(self):
        """Return formatted account for display"""
        if self.chapitre:
            return f"{self.chapitre}/{self.compte}"
        return self.compte