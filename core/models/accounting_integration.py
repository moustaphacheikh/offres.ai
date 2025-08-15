from django.db import models
from django.core.validators import DecimalValidator, RegexValidator
from django.core.exceptions import ValidationError
from decimal import Decimal
from .employee import Employee
import re


class ExportFormat(models.Model):
    """
    Configuration for different export formats to external accounting systems
    """
    name = models.CharField(
        max_length=50,
        unique=True,
        help_text="Export format name (e.g., 'UNL', 'CSV', 'XML')"
    )
    file_extension = models.CharField(
        max_length=10,
        default=".unl",
        help_text="File extension for exported files"
    )
    delimiter = models.CharField(
        max_length=5,
        default="|",
        help_text="Field delimiter for text exports"
    )
    date_format = models.CharField(
        max_length=20,
        choices=[
            ('dd/MM/yyyy', 'DD/MM/YYYY'),
            ('dd/MM/yy', 'DD/MM/YY'),
            ('ddMMyyyy', 'DDMMYYYY'),
            ('ddMMyy', 'DDMMYY'),
            ('yyyy-MM-dd', 'YYYY-MM-DD'),
        ],
        default='dd/MM/yyyy',
        help_text="Date format for exports"
    )
    is_active = models.BooleanField(
        default=True,
        help_text="Whether this export format is available"
    )
    
    # Export parameters
    agency_code = models.CharField(
        max_length=20,
        blank=True,
        help_text="Default agency code for exports"
    )
    currency_code = models.CharField(
        max_length=10,
        default="UM",
        help_text="Currency code for exports"
    )
    operation_code = models.CharField(
        max_length=20,
        blank=True,
        help_text="Operation code for external system"
    )
    service_code = models.CharField(
        max_length=20,
        blank=True,
        help_text="Service code for external system"
    )
    
    class Meta:
        db_table = 'export_format'
        ordering = ['name']
    
    def __str__(self):
        return f"{self.name} ({self.file_extension})"


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
    export_format = models.ForeignKey(
        ExportFormat,
        on_delete=models.SET_NULL,
        blank=True,
        null=True,
        related_name='exported_pieces',
        help_text="Export format used for external system integration"
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
    
    # Batch processing
    batch_reference = models.CharField(
        max_length=100,
        blank=True,
        help_text="Batch processing reference"
    )
    processing_errors = models.TextField(
        blank=True,
        help_text="Processing errors and warnings"
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
            models.Index(fields=['export_format']),
            models.Index(fields=['batch_reference']),
        ]
        constraints = [
            models.CheckConstraint(
                check=models.Q(total_debit__gte=0) & models.Q(total_credit__gte=0),
                name='positive_totals'
            ),
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
    
    @property
    def can_be_exported(self):
        """Check if piece can be exported to external system"""
        return (
            self.status in ['VALIDATED', 'EXPORTED'] and
            self.is_balanced and
            self.detailpieces.exists()
        )
    
    @property
    def export_filename(self):
        """Generate export filename based on format and period"""
        if self.export_format:
            base_name = f"PC_{self.motif}_{self.period.replace('-', '')}"
            return f"{base_name}{self.export_format.file_extension}"
        return f"PC_{self.motif}_{self.period.replace('-', '')}.unl"
    
    def clean(self):
        """Validate model data"""
        super().clean()
        if self.approval_date and not self.approved_by:
            raise ValidationError("Approved pieces must have an approver")
        if self.export_date and self.status == 'DRAFT':
            raise ValidationError("Draft pieces cannot be exported")
    
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
    
    def mark_as_exported(self, export_format=None, batch_ref=None):
        """Mark piece as exported to external system"""
        from django.utils import timezone
        self.status = 'EXPORTED'
        self.export_date = timezone.now()
        if export_format:
            self.export_format = export_format
        if batch_ref:
            self.batch_reference = batch_ref
        self.save(update_fields=['status', 'export_date', 'export_format', 'batch_reference', 'updated_at'])
    
    def add_processing_error(self, error_message):
        """Add error message to processing errors"""
        if self.processing_errors:
            self.processing_errors += f"\n{error_message}"
        else:
            self.processing_errors = error_message
        self.save(update_fields=['processing_errors', 'updated_at'])


class AccountGenerator:
    """
    Utility class for generating account numbers following the legacy patterns
    """
    
    @staticmethod
    def employee_cash_account(employee_id):
        """Generate cash account for employee (307 + padded ID)"""
        return f"307{str(employee_id).zfill(4)}"
    
    @staticmethod
    def employee_engagement_account(employee_id):
        """Generate engagement account for employee (511 + padded ID)"""
        return f"511{str(employee_id).zfill(4)}"
    
    @staticmethod
    def validate_account_format(account_number):
        """Validate account number format"""
        if not account_number:
            return False
        # Basic validation - should be alphanumeric
        return re.match(r'^[A-Z0-9]{3,20}$', account_number.upper()) is not None


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
        help_text="Account number",
        validators=[
            RegexValidator(
                regex=r'^[A-Z0-9]{1,50}$',
                message="Account must contain only alphanumeric characters",
                flags=re.IGNORECASE
            )
        ]
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
            ('ITS', 'Income tax'),
            ('CNSS', 'CNSS contribution'),
            ('CNAM', 'CNAM contribution'),
            ('OTHER', 'Other')
        ],
        default='OTHER',
        help_text="Type of account posting"
    )
    
    # Chapter information (for UNL export)
    chapitre = models.CharField(
        max_length=50,
        blank=True,
        help_text="Chapter code for external systems",
        validators=[
            RegexValidator(
                regex=r'^[A-Z0-9]*$',
                message="Chapter must contain only alphanumeric characters",
                flags=re.IGNORECASE
            )
        ]
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
    export_line_number = models.IntegerField(
        blank=True,
        null=True,
        help_text="Line number in exported file"
    )
    
    # Additional payroll integration fields
    rubrique_id = models.IntegerField(
        blank=True,
        null=True,
        help_text="Original rubrique ID from payroll system"
    )
    employee_net_amount = models.DecimalField(
        max_digits=15,
        decimal_places=2,
        blank=True,
        null=True,
        help_text="Employee net amount for individual postings"
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
            models.Index(fields=['exported']),
            models.Index(fields=['chapitre']),
            models.Index(fields=['rubrique_id']),
        ]
        constraints = [
            models.CheckConstraint(
                check=models.Q(montant__gte=0),
                name='positive_amount'
            ),
            models.CheckConstraint(
                check=models.Q(cvmro_montant__gte=0),
                name='positive_cvmro_amount'
            ),
        ]
    
    def __str__(self):
        direction = "Dr" if self.sens == 'D' else "Cr"
        return f"DP-{self.numligne}: {self.compte} {direction} {self.montant}"
    
    def clean(self):
        """Validate model data"""
        super().clean()
        if self.montant <= 0:
            raise ValidationError("Amount must be positive")
        if self.employee and self.account_type not in ['CASH', 'ENGAGEMENT']:
            # Only cash and engagement accounts should have employee references
            pass  # Allow for flexibility but could add validation
        if not AccountGenerator.validate_account_format(self.compte):
            raise ValidationError("Invalid account number format")
    
    def save(self, *args, **kwargs):
        # Ensure cvmro_montant mirrors montant
        self.cvmro_montant = self.montant
        
        # Auto-generate employee accounts if needed
        if self.employee and not self.compte:
            if self.account_type == 'ENGAGEMENT':
                self.compte = AccountGenerator.employee_engagement_account(self.employee.id)
            elif self.account_type == 'CASH':
                self.compte = AccountGenerator.employee_cash_account(self.employee.id)
        
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
    
    @property
    def full_account_display(self):
        """Return full account display with title"""
        base = self.formatted_account
        if self.intitulet and self.intitulet != self.compte:
            return f"{base} - {self.intitulet}"
        return base
    
    @property
    def export_data(self):
        """Return data formatted for export"""
        return {
            'chapitre': self.chapitre or '',
            'compte': self.compte,
            'montant': float(self.montant),
            'sens': self.sens,
            'libelle': self.libelle,
            'intitulet': self.intitulet,
            'devise': self.devise,
            'cours': float(self.cours),
            'dateop': self.dateop,
            'journal': self.journal,
        }
    
    def mark_as_exported(self, reference=None, line_number=None):
        """Mark detail piece as exported"""
        self.exported = True
        if reference:
            self.export_reference = reference
        if line_number:
            self.export_line_number = line_number
        self.save(update_fields=['exported', 'export_reference', 'export_line_number', 'updated_at'])
    
    @classmethod
    def create_rubrique_entry(cls, master_piece, rubrique, amount, sens='D'):
        """Create a detail piece for a payroll rubrique"""
        return cls.objects.create(
            nupiece=master_piece,
            dateop=master_piece.dateop,
            compte=str(rubrique.no_compte_compta) if hasattr(rubrique, 'no_compte_compta') else '',
            chapitre=str(rubrique.no_chapitre_compta) if hasattr(rubrique, 'no_chapitre_compta') else '',
            libelle=rubrique.libelle if hasattr(rubrique, 'libelle') else 'Payroll Entry',
            intitulet=rubrique.libelle if hasattr(rubrique, 'libelle') else 'Payroll Entry',
            montant=amount,
            sens=sens,
            account_type='RUBRIQUE',
            rubrique_id=rubrique.id if hasattr(rubrique, 'id') else None,
        )
    
    @classmethod
    def create_employee_cash_entry(cls, master_piece, employee, amount, motif=""):
        """Create a cash payment entry for an employee"""
        account = AccountGenerator.employee_cash_account(employee.id)
        libelle = f"Salaire ({motif}) - {employee.prenom} {employee.nom}" if motif else f"Paiement - {employee.prenom} {employee.nom}"
        
        return cls.objects.create(
            nupiece=master_piece,
            dateop=master_piece.dateop,
            compte=account,
            libelle=libelle,
            intitulet=f"{employee.prenom} {employee.nom}",
            montant=amount,
            sens='C',
            account_type='CASH',
            employee=employee,
            employee_net_amount=amount,
        )
    
    @classmethod
    def create_bank_transfer_entry(cls, master_piece, bank_account, amount, bank_name, motif=""):
        """Create a bank transfer entry"""
        libelle = f"Virement {bank_name} ({motif})" if motif else f"Virement {bank_name}"
        
        return cls.objects.create(
            nupiece=master_piece,
            dateop=master_piece.dateop,
            compte=bank_account,
            libelle=libelle,
            intitulet=bank_name,
            montant=amount,
            sens='C',
            account_type='BANK',
        )