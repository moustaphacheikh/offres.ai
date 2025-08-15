from django.contrib.auth.models import AbstractUser
from django.core.validators import EmailValidator, MinValueValidator, MaxValueValidator
from django.db import models


class SystemParameters(models.Model):
    """Global system configuration parameters - equivalent to Paramgen.java"""
    
    # Primary Key
    id = models.AutoField(primary_key=True)
    
    # Company Information
    company_name = models.CharField(max_length=300)  # nomEntreprise
    company_activity = models.CharField(max_length=500, blank=True)  # activiteEntreprise
    company_manager = models.CharField(max_length=300, blank=True)  # responsableEntreprise
    manager_title = models.CharField(max_length=300, blank=True)  # qualiteResponsable
    company_logo = models.BinaryField(blank=True, null=True)  # logo
    
    # Contact Information
    telephone = models.CharField(max_length=30, blank=True)
    fax = models.CharField(max_length=30, blank=True)
    address = models.CharField(max_length=500, blank=True)  # adresse
    website = models.CharField(max_length=50, blank=True)  # siteweb
    email = models.EmailField(max_length=50, blank=True)
    city_headquarters = models.CharField(max_length=300, blank=True)  # villeSiege
    signatories = models.CharField(max_length=500, blank=True)  # signataires
    
    # Financial Configuration
    currency = models.CharField(max_length=50, blank=True)  # devise
    minimum_wage = models.DecimalField(max_digits=22, decimal_places=2, blank=True, null=True, validators=[MinValueValidator(0)])  # smig
    default_working_days = models.DecimalField(max_digits=22, decimal_places=2, validators=[MinValueValidator(0), MaxValueValidator(31)])  # njtDefault
    tax_abatement = models.DecimalField(max_digits=22, decimal_places=2, blank=True, null=True)  # abatement
    non_taxable_allowance_ceiling = models.DecimalField(max_digits=22, decimal_places=2, validators=[MinValueValidator(0)])  # plafonIndNonImposable
    installment_quota = models.DecimalField(max_digits=22, decimal_places=2, blank=True, null=True)  # quotaEcheanceRae
    
    # Period Management
    current_period = models.DateField()  # periodeCourante
    next_period = models.DateField()  # periodeSuivante
    closure_period = models.DateField()  # periodeCloture
    
    # Automation Settings
    auto_meal_allowance = models.BooleanField(default=False)  # primePanierAuto
    auto_seniority = models.BooleanField(default=False)  # ancienneteAuto
    auto_housing_allowance = models.BooleanField(default=False)  # indlogementAuto
    deduct_cnss_from_its = models.BooleanField(default=False)  # deductionCnssdeIts
    deduct_cnam_from_its = models.BooleanField(default=False)  # deductionCnamdeIts
    special_seniority = models.BooleanField(default=False)  # ancienneteSpeciale
    
    # Social Security and Tax Numbers
    cnss_number = models.CharField(max_length=10, blank=True)  # noCnss
    cnam_number = models.CharField(max_length=10, blank=True)  # noCnam
    its_number = models.CharField(max_length=10, blank=True)  # noIts
    used_its = models.IntegerField(default=0)  # usedIts
    its_reimbursement = models.BooleanField(default=False)  # remboursementIts
    its_mode = models.CharField(max_length=10, blank=True)  # modeITS
    
    # Accounting Integration Fields
    net_account = models.BigIntegerField()  # noComptaNet
    net_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreNet
    net_key = models.CharField(max_length=10, blank=True)  # noComptaCleNet
    
    its_account = models.BigIntegerField(blank=True, null=True)  # noComptaIts
    its_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreIts
    its_key = models.CharField(max_length=10, blank=True)  # noComptaCleIts
    
    cnss_account = models.BigIntegerField(blank=True, null=True)  # noComptaCnss
    cnss_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreCnss
    cnss_key = models.CharField(max_length=10, blank=True)  # noComptaCleCnss
    
    cnam_account = models.BigIntegerField(blank=True, null=True)  # noComptaCnam
    cnam_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreCnam
    cnam_key = models.CharField(max_length=10, blank=True)  # noComptaCleCnam
    
    # Banking and Transfers
    bank_transfer_bank = models.CharField(max_length=300, blank=True)  # bankvirement
    transfer_account = models.CharField(max_length=300, blank=True)  # comptevirement
    
    # System Information
    contract_expiry_alert_days = models.IntegerField(blank=True, null=True, validators=[MinValueValidator(1), MaxValueValidator(365)])  # delaiAlerteFinContrat
    last_update = models.DateTimeField(auto_now=True)  # dateMaj
    license_key = models.CharField(max_length=500, blank=True)  # licenceKey
    database_version = models.CharField(max_length=10, blank=True)  # bd
    
    def clean(self):
        """Custom validation for SystemParameters"""
        from django.core.exceptions import ValidationError
        
        if self.current_period and self.next_period:
            if self.current_period >= self.next_period:
                raise ValidationError('Next period must be after current period')
        
        if self.current_period and self.closure_period:
            if self.closure_period < self.current_period:
                raise ValidationError('Closure period cannot be before current period')
    
    def save(self, *args, **kwargs):
        """Override save to ensure only one SystemParameters instance exists"""
        if not self.pk and SystemParameters.objects.exists():
            # Update existing instance instead of creating new one
            existing = SystemParameters.objects.first()
            for field in self._meta.fields:
                if field.name != 'id':
                    setattr(existing, field.name, getattr(self, field.name))
            existing.save()
            self.pk = existing.pk
        else:
            super().save(*args, **kwargs)
    
    # Email Configuration
    smtp_host = models.CharField(max_length=100, blank=True)  # mailSmtpHost
    mail_user = models.CharField(max_length=100, blank=True)  # mailUser
    mail_password = models.CharField(max_length=100, blank=True)  # mailPassword
    smtp_port = models.CharField(max_length=100, blank=True)  # mailSmtpPort
    smtp_tls_enabled = models.BooleanField(default=False)  # mailSmtpTLSEnabled
    
    # Business Logic Settings
    apply_compensatory_allowance = models.BooleanField(default=False)  # appIndCompensatrice
    add_current_salary_to_leave_cumul = models.BooleanField(default=False)  # addCurrentSalInCumulCng
    deduct_commitments_on_leave = models.BooleanField(default=False)  # retEngOnConge
    
    # License Management
    customer_active_version = models.CharField(max_length=10, blank=True)  # custumerActiveVersion
    license_init_date = models.DateField(blank=True, null=True)  # dateInitLicence
    current_license_date = models.DateField(blank=True, null=True)  # dateCurentLicence
    license_periodicity = models.CharField(max_length=50, blank=True)  # licencePeriodicity
    
    # Additional Fields from Java entity
    name = models.CharField(max_length=300, blank=True)  # nom
    pub = models.CharField(max_length=300, blank=True)
    attendance_path = models.CharField(max_length=500, blank=True)  # cheminatt2000
    salary_code_number = models.CharField(max_length=100, blank=True)  # nbSalaryCode
    
    # Additional accounting fields for complex accounts structure
    rits_account = models.BigIntegerField(blank=True, null=True)  # noComptaRits
    rits_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreRits
    rits_key = models.CharField(max_length=10, blank=True)  # noComptaCleRits
    
    rcnss_account = models.BigIntegerField(blank=True, null=True)  # noComptaRcnss
    rcnss_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreRcnss
    rcnss_key = models.CharField(max_length=10, blank=True)  # noComptaCleRcnss
    
    rcnam_account = models.BigIntegerField(blank=True, null=True)  # noComptaRcnam
    rcnam_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreRcnam
    rcnam_key = models.CharField(max_length=10, blank=True)  # noComptaCleRcnam
    
    # Medical CNSS accounts
    cnss_med_credit_account = models.BigIntegerField(blank=True, null=True)  # noComptaCnssMedCredit
    cnss_med_credit_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreCnssMedCredit
    cnss_med_credit_key = models.CharField(max_length=10, blank=True)  # noComptaCleCnssMedCredit
    
    cnss_med_debit_account = models.BigIntegerField(blank=True, null=True)  # noComptaCnssMedDebit
    cnss_med_debit_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreCnssMedDebit
    cnss_med_debit_key = models.CharField(max_length=10, blank=True)  # noComptaCleCnssMedDebit
    
    # Patronal CNSS accounts
    cnss_pat_credit_account = models.BigIntegerField(blank=True, null=True)  # noComptaCnssPatCredit
    cnss_pat_credit_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreCnssPatCredit
    cnss_pat_credit_key = models.CharField(max_length=10, blank=True)  # noComptaCleCnssPatCredit
    
    cnss_pat_debit_account = models.BigIntegerField(blank=True, null=True)  # noComptaCnssPatDebit
    cnss_pat_debit_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreCnssPatDebit
    cnss_pat_debit_key = models.CharField(max_length=10, blank=True)  # noComptaCleCnssPatDebit
    
    # Patronal CNAM accounts
    cnam_pat_credit_account = models.BigIntegerField(blank=True, null=True)  # noComptaCnamPatCredit
    cnam_pat_credit_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreCnamPatCredit
    cnam_pat_credit_key = models.CharField(max_length=10, blank=True)  # noComptaCleCnamPatCredit
    
    cnam_pat_debit_account = models.BigIntegerField(blank=True, null=True)  # noComptaCnamPatDebit
    cnam_pat_debit_chapter = models.BigIntegerField(blank=True, null=True)  # noComptaChapitreCnamPatDebit
    cnam_pat_debit_key = models.CharField(max_length=10, blank=True)  # noComptaCleCnamPatDebit
    
    class Meta:
        db_table = 'paramgen'
        verbose_name = 'System Parameters'
        verbose_name_plural = 'System Parameters'
    
    def __str__(self):
        return f"System Parameters for {self.company_name}"


class User(AbstractUser):
    """System users with detailed permissions - equivalent to Utilisateurs.java"""
    
    # Override username field to match Java entity
    username = models.CharField(max_length=15, unique=True)  # login
    
    # Additional user information
    full_name = models.CharField(max_length=60)  # nomusager
    last_session = models.DateTimeField(auto_now=True)  # dersession
    
    # Fix reverse accessor clashes with Django's built-in User
    groups = models.ManyToManyField(
        'auth.Group',
        verbose_name='groups',
        blank=True,
        help_text='The groups this user belongs to.',
        related_name='payroll_users',
        related_query_name='payroll_user'
    )
    user_permissions = models.ManyToManyField(
        'auth.Permission',
        verbose_name='user permissions',
        blank=True,
        help_text='Specific permissions for this user.',
        related_name='payroll_users',
        related_query_name='payroll_user'
    )
    
    # Basic CRUD permissions
    can_add = models.BooleanField(default=False)  # ajout
    can_update = models.BooleanField(default=False)  # maj
    can_delete = models.BooleanField(default=False)  # suppression
    
    # Module Access Permissions
    can_access_personnel = models.BooleanField(default=False)  # sal_identite + sal_diplome + sal_contrat + sal_add + sal_update + sal_doc
    can_access_payroll = models.BooleanField(default=False)  # sal_paie
    can_access_attendance = models.BooleanField(default=False)  # sal_hs
    can_access_declarations = models.BooleanField(default=False)  # Based on general access patterns
    can_access_accounting = models.BooleanField(default=False)  # Based on general access patterns
    can_access_transfers = models.BooleanField(default=False)  # Based on general access patterns
    can_access_payslips = models.BooleanField(default=False)  # Based on general access patterns
    can_access_reports = models.BooleanField(default=False)  # Based on general access patterns
    can_access_statistics = models.BooleanField(default=False)  # Based on general access patterns
    can_access_security = models.BooleanField(default=False)  # securite
    can_access_parameters = models.BooleanField(default=False)  # parametre
    can_access_structures = models.BooleanField(default=False)  # Based on general access patterns
    can_access_elements = models.BooleanField(default=False)  # rubriquepaie
    can_access_closure = models.BooleanField(default=False)  # cloture
    can_access_annual_declarations = models.BooleanField(default=False)  # Based on general access patterns
    can_access_cumulative_reports = models.BooleanField(default=False)  # Based on general access patterns
    can_access_file_import = models.BooleanField(default=False)  # Based on general access patterns
    can_access_licensing = models.BooleanField(default=False)  # Based on general access patterns
    
    # Detailed Employee Management Permissions
    can_manage_employee_identity = models.BooleanField(default=False)  # sal_identite
    can_manage_employee_diplomas = models.BooleanField(default=False)  # sal_diplome
    can_manage_employee_contracts = models.BooleanField(default=False)  # sal_contrat
    can_manage_employee_deductions = models.BooleanField(default=False)  # sal_retenueae
    can_manage_employee_leave = models.BooleanField(default=False)  # sal_conge
    can_manage_employee_overtime = models.BooleanField(default=False)  # sal_hs
    can_manage_employee_payroll = models.BooleanField(default=False)  # sal_paie
    can_add_employees = models.BooleanField(default=False)  # sal_add
    can_update_employees = models.BooleanField(default=False)  # sal_update
    can_manage_employee_documents = models.BooleanField(default=False)  # sal_doc
    
    # Configuration Access Permissions
    can_manage_salary_grids = models.BooleanField(default=False)  # grillesb
    can_manage_housing_grids = models.BooleanField(default=False)  # grillelog
    can_manage_origins = models.BooleanField(default=False)  # originesal
    can_suppress_salaries = models.BooleanField(default=False)  # suppsal
    can_manage_payroll_motifs = models.BooleanField(default=False)  # motifpaie
    
    # Dashboard Access
    can_access_dashboard = models.BooleanField(default=False)  # dashboard
    
    class Meta:
        db_table = 'utilisateurs'
        ordering = ['full_name']
        
    def clean(self):
        """Custom validation for User"""
        from django.core.exceptions import ValidationError
        
        if not self.full_name.strip():
            raise ValidationError('Full name cannot be empty')
        
        if len(self.username) < 3:
            raise ValidationError('Username must be at least 3 characters long')
    
    def __str__(self):
        return f"{self.full_name} ({self.username})"
    
    def get_full_name(self):
        return self.full_name
    
    def get_short_name(self):
        return self.username
    
    @property
    def has_personnel_access(self):
        """Check if user has any personnel management permissions"""
        return (self.can_access_personnel or self.can_manage_employee_identity or 
                self.can_manage_employee_contracts or self.can_add_employees or 
                self.can_update_employees)
    
    @property
    def has_payroll_access(self):
        """Check if user has any payroll-related permissions"""
        return (self.can_access_payroll or self.can_manage_employee_payroll or 
                self.can_access_elements or self.can_access_reports)
    
    @property
    def has_admin_access(self):
        """Check if user has administrative permissions"""
        return (self.can_access_parameters or self.can_access_security or 
                self.can_access_structures or self.can_access_closure)
    
    def has_module_permission(self, module_name):
        """Check if user has permission to access a specific module"""
        permission_map = {
            'personnel': self.can_access_personnel,
            'payroll': self.can_access_payroll,
            'attendance': self.can_access_attendance,
            'declarations': self.can_access_declarations,
            'accounting': self.can_access_accounting,
            'transfers': self.can_access_transfers,
            'payslips': self.can_access_payslips,
            'reports': self.can_access_reports,
            'statistics': self.can_access_statistics,
            'security': self.can_access_security,
            'parameters': self.can_access_parameters,
            'structures': self.can_access_structures,
            'elements': self.can_access_elements,
            'closure': self.can_access_closure,
            'dashboard': self.can_access_dashboard,
        }
        return permission_map.get(module_name, False)