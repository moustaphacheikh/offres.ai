from django.db import models
from .organizational import GeneralDirection, Direction, Department, Position
from .reference import Activity, Origin, Bank
from .compensation import SalaryGrade


class Employee(models.Model):
    """Central employee record - equivalent to Employe.java with 80+ fields"""
    
    # Primary identification
    id = models.AutoField(primary_key=True)
    
    # Personal Information
    last_name = models.CharField(max_length=100)  # nom
    first_name = models.CharField(max_length=100)  # prenom
    father_name = models.CharField(max_length=200, blank=True)  # pere
    mother_name = models.CharField(max_length=200, blank=True)  # mere
    national_id = models.CharField(max_length=50, blank=True)  # nni
    birth_date = models.DateField(blank=True, null=True)  # dateNaissance
    birth_place = models.CharField(max_length=200, blank=True)  # lieuNaissance
    nationality = models.CharField(max_length=100, blank=True)  # nationalite
    gender = models.CharField(max_length=30, blank=True)  # sexe
    marital_status = models.CharField(max_length=30, blank=True)  # situationFamiliale
    children_count = models.IntegerField(blank=True, null=True)  # nbEnfants
    phone = models.CharField(max_length=200, blank=True)  # telephone
    email = models.EmailField(max_length=200, blank=True)  # email
    address = models.CharField(max_length=500, blank=True)  # adresse
    photo = models.BinaryField(blank=True, null=True)  # photo
    
    # Employment Details
    hire_date = models.DateField(blank=True, null=True)  # dateEmbauche
    termination_date = models.DateField(blank=True, null=True)  # dateDebauche
    termination_reason = models.CharField(max_length=500, blank=True)  # raisonDebauche
    seniority_date = models.DateField(blank=True, null=True)  # dateAnciennete
    is_active = models.BooleanField(default=True)  # actif
    on_leave = models.BooleanField(default=False)  # enConge
    in_termination = models.BooleanField(default=False)  # enDebauche
    origin_structure = models.CharField(max_length=200, blank=True)  # structureOrigine
    work_location = models.CharField(max_length=200, blank=True)  # lieuTravail
    contract_type = models.CharField(max_length=50, blank=True)  # typeContrat
    contract_end_date = models.DateField(blank=True, null=True)  # dateFinContrat
    classification = models.CharField(max_length=200, blank=True)  # classification
    status = models.CharField(max_length=50, blank=True)  # statut
    
    # Social Security & Tax Configuration
    cnss_number = models.CharField(max_length=50, blank=True)  # noCnss
    cnss_date = models.DateField(blank=True, null=True)  # dateCnss
    cnam_number = models.CharField(max_length=50, blank=True)  # noCnam
    cnss_detached = models.BooleanField(default=False)  # detacheCnss
    cnam_detached = models.BooleanField(default=False)  # detacheCnam
    is_domiciled = models.BooleanField(default=False)  # domicilie
    its_exempt = models.BooleanField(default=False)  # exonoreIts
    
    # Banking Information
    bank_account = models.CharField(max_length=50, blank=True)  # noCompteBanque
    payment_mode = models.CharField(max_length=50, blank=True)  # modePaiement
    
    # Salary & Benefits
    annual_budget = models.DecimalField(max_digits=22, decimal_places=2, default=0)  # budgetannuel
    contract_hours_per_week = models.DecimalField(max_digits=5, decimal_places=2, default=0)  # contratHeureSemaine
    seniority_rate = models.DecimalField(max_digits=5, decimal_places=2, default=0)  # tauxAnciennete
    psra_rate = models.DecimalField(max_digits=5, decimal_places=2, default=0)  # tauxPsra
    notice_months = models.FloatField(default=0)  # nbMoisPreavis
    
    # Cumulative Initial Values
    cumulative_days_initial = models.DecimalField(max_digits=5, decimal_places=2, default=0)  # cumulNjtinitial
    cumulative_taxable_initial = models.DecimalField(max_digits=15, decimal_places=2, default=0)  # cumulBrutImposableInitial
    cumulative_non_taxable_initial = models.DecimalField(max_digits=15, decimal_places=2, default=0)  # cumulBrutNonImposableInitial
    cumulative_12dm_initial = models.DecimalField(max_digits=15, decimal_places=2, default=0)  # cumul12dminitial
    last_departure_initial = models.DateField(blank=True, null=True)  # dernierDepartInitial
    
    # Tax Reimbursement Rates
    cnss_reimbursement_rate = models.DecimalField(max_digits=5, decimal_places=2, default=0)  # tauxRemborssementCnss
    cnam_reimbursement_rate = models.DecimalField(max_digits=5, decimal_places=2, default=0)  # tauxRemborssementCnam
    its_tranche1_reimbursement = models.DecimalField(max_digits=5, decimal_places=2, default=0)  # tauxRembItstranche1
    its_tranche2_reimbursement = models.DecimalField(max_digits=5, decimal_places=2, default=0)  # tauxRembItstranche2
    its_tranche3_reimbursement = models.DecimalField(max_digits=5, decimal_places=2, default=0)  # tauxRembItstranche3
    
    # Foreign Employee Information
    is_expatriate = models.BooleanField(default=False)  # expatrie
    passport_number = models.CharField(max_length=100, blank=True)  # noPassport
    passport_issue_date = models.DateField(blank=True, null=True)  # dateLivraisonPassport
    passport_expiry_date = models.DateField(blank=True, null=True)  # dateExpirationPassport
    visa_start_date = models.DateField(blank=True, null=True)  # dateDebutVisa
    visa_end_date = models.DateField(blank=True, null=True)  # dateFinVisa
    residence_card_number = models.CharField(max_length=100, blank=True)  # noCarteSejour
    work_permit_number = models.CharField(max_length=100, blank=True)  # noPermiTravail
    work_permit_issue_date = models.DateField(blank=True, null=True)  # dateLivraisonPermiTravail
    work_permit_expiry_date = models.DateField(blank=True, null=True)  # dateExpirationPermiTravail
    
    # Work Schedule Matrix (7 days × 3 shifts = 21 boolean fields)
    # Monday
    monday_day_shift = models.BooleanField(default=False)  # lundiDs
    monday_first_shift = models.BooleanField(default=False)  # lundiFs
    monday_weekend = models.BooleanField(default=False)  # lundiWe
    # Tuesday
    tuesday_day_shift = models.BooleanField(default=False)  # mardiDs
    tuesday_first_shift = models.BooleanField(default=False)  # mardiFs
    tuesday_weekend = models.BooleanField(default=False)  # mardiWe
    # Wednesday
    wednesday_day_shift = models.BooleanField(default=False)  # mercrediDs
    wednesday_first_shift = models.BooleanField(default=False)  # mercrediFs
    wednesday_weekend = models.BooleanField(default=False)  # mercrediWe
    # Thursday
    thursday_day_shift = models.BooleanField(default=False)  # jeudiDs
    thursday_first_shift = models.BooleanField(default=False)  # jeudiFs
    thursday_weekend = models.BooleanField(default=False)  # jeudiWe
    # Friday
    friday_day_shift = models.BooleanField(default=False)  # vendrediDs
    friday_first_shift = models.BooleanField(default=False)  # vendrediFs
    friday_weekend = models.BooleanField(default=False)  # vendrediWe
    # Saturday
    saturday_day_shift = models.BooleanField(default=False)  # samediDs
    saturday_first_shift = models.BooleanField(default=False)  # samediFs
    saturday_weekend = models.BooleanField(default=False)  # samediWe
    # Sunday
    sunday_day_shift = models.BooleanField(default=False)  # dimancheDs
    sunday_first_shift = models.BooleanField(default=False)  # dimancheFs
    sunday_weekend = models.BooleanField(default=False)  # dimancheWe
    
    # Special Fields
    timeclock_employee_id = models.IntegerField(blank=True, null=True)  # idSalariePointeuse
    category_date = models.DateField(blank=True, null=True)  # dateCategorie
    category_years = models.IntegerField(blank=True, null=True)  # nbAnnnesCategorie
    auto_category_advancement = models.BooleanField(default=False)  # avancementCategorieAuto
    payslip_note = models.CharField(max_length=600, blank=True)  # noteSurBulletin
    ps_service = models.BooleanField(default=False)  # psservice
    ps_service_id = models.CharField(max_length=50, blank=True)  # idPsservice
    password = models.CharField(max_length=50, blank=True)  # password
    
    # Organizational Relationships
    position = models.ForeignKey(
        Position, 
        on_delete=models.PROTECT, 
        blank=True, 
        null=True, 
        related_name='employees'
    )  # poste (fonction)
    department = models.ForeignKey(
        Department, 
        on_delete=models.PROTECT, 
        blank=True, 
        null=True, 
        related_name='employees'
    )  # departement
    general_direction = models.ForeignKey(
        GeneralDirection, 
        on_delete=models.PROTECT, 
        blank=True, 
        null=True, 
        related_name='employees'
    )  # directiongeneral
    direction = models.ForeignKey(
        Direction, 
        on_delete=models.PROTECT, 
        blank=True, 
        null=True, 
        related_name='employees'
    )  # direction
    salary_grade = models.ForeignKey(
        SalaryGrade, 
        on_delete=models.PROTECT, 
        blank=True, 
        null=True, 
        related_name='employees'
    )  # grillesalairebase (categorie)
    activity = models.ForeignKey(
        Activity, 
        on_delete=models.PROTECT, 
        blank=True, 
        null=True, 
        related_name='employees'
    )  # activite
    origin = models.ForeignKey(
        Origin, 
        on_delete=models.SET_NULL, 
        null=True, 
        blank=True, 
        related_name='employees'
    )  # origines
    bank = models.ForeignKey(
        Bank, 
        on_delete=models.SET_NULL, 
        null=True, 
        blank=True, 
        related_name='employees'
    )  # banque
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'employe'
        ordering = ['last_name', 'first_name']
    
    def __str__(self):
        return f"{self.first_name} {self.last_name}"
    
    @property
    def full_name(self):
        """Return full name for display purposes"""
        return f"{self.first_name} {self.last_name}".strip()
    
    @property
    def is_foreign_employee(self):
        """Check if employee is expatriate or has foreign documentation"""
        return self.is_expatriate or bool(
            self.passport_number or self.visa_start_date or 
            self.work_permit_number or self.residence_card_number
        )