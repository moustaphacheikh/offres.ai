from django.db import models
from django.core.exceptions import ValidationError
from decimal import Decimal
from .organizational import Position


class PayrollElement(models.Model):
    """
    Payroll calculation elements/rubrics - equivalent to Rubrique.java
    
    Defines the different elements that can be used in payroll calculations,
    such as salaries, bonuses, deductions, taxes, etc.
    """
    
    # Type choices for payroll elements
    TYPE_CHOICES = [
        ('G', 'Gain'),        # Earnings/gains
        ('D', 'Deduction'),   # Deductions
    ]
    
    id = models.AutoField(primary_key=True)
    label = models.CharField(max_length=500)  # libelle
    abbreviation = models.CharField(max_length=255, blank=True)  # abreviation
    type = models.CharField(max_length=1, choices=TYPE_CHOICES)  # sens (G=Gain, D=Deduction)
    
    # Boolean flags for payroll calculation rules
    has_ceiling = models.BooleanField(default=False)  # plafone
    is_cumulative = models.BooleanField(default=False)  # cumulable
    
    # Tax and social security applicability
    affects_its = models.BooleanField(default=False)  # its (Income Tax)
    affects_cnss = models.BooleanField(default=False)  # cnss (Social Security)
    affects_cnam = models.BooleanField(default=False)  # cnam (Health Insurance)
    
    # Additional flags
    is_benefit_in_kind = models.BooleanField(default=False)  # avantagesNature
    auto_base_calculation = models.BooleanField(default=False)  # baseAuto
    auto_quantity_calculation = models.BooleanField(default=False)  # nombreAuto
    
    # Accounting integration
    accounting_account = models.BigIntegerField(default=0)  # noCompteCompta
    accounting_chapter = models.BigIntegerField(default=0)  # noChapitreCompta
    accounting_key = models.CharField(max_length=10, blank=True)  # noCompteComptaCle
    
    # System and deduction fields
    deduction_from = models.CharField(max_length=10, blank=True)  # deductionDu
    is_active = models.BooleanField(default=True)  # sys (system/active flag)
    
    class Meta:
        db_table = 'rubrique'
        ordering = ['label']
        verbose_name = 'Payroll Element'
        verbose_name_plural = 'Payroll Elements'
    
    def __str__(self):
        return f"{self.label} ({self.get_type_display()})"
    
    def clean(self):
        """Validate the payroll element"""
        if self.label and self.label.strip() == "":
            raise ValidationError("Label cannot be empty")
        if self.type not in ['G', 'D']:
            raise ValidationError("Type must be either 'G' (Gain) or 'D' (Deduction)")
    
    @property
    def is_gain(self):
        """Check if this element is a gain/earning"""
        return self.type == 'G'
    
    @property
    def is_deduction(self):
        """Check if this element is a deduction"""
        return self.type == 'D'
    
    @property
    def affects_social_security(self):
        """Check if this element affects any social security calculation"""
        return self.affects_cnss or self.affects_cnam
    
    @property
    def affects_taxes(self):
        """Check if this element affects tax calculations"""
        return self.affects_its
    
    @property
    def has_formulas(self):
        """Check if this element has calculation formulas"""
        return self.formulas.exists()
    
    @property
    def base_formulas(self):
        """Get base calculation formulas"""
        return self.formulas.filter(section='B')
    
    @property
    def number_formulas(self):
        """Get number/quantity calculation formulas"""
        return self.formulas.filter(section='N')
    
    def get_position_templates(self):
        """Get all position templates using this payroll element"""
        return self.position_models.select_related('position')
    
    def get_default_amount_for_position(self, position):
        """Get the default amount for a specific position"""
        try:
            template = self.position_models.get(position=position)
            return template.amount
        except PayrollElementModel.DoesNotExist:
            return Decimal('0')
    
    def create_position_template(self, position, amount):
        """Create a new position template for this element"""
        template, created = PayrollElementModel.objects.get_or_create(
            position=position,
            payroll_element=self,
            defaults={'amount': amount}
        )
        return template, created


class PayrollElementFormula(models.Model):
    """
    Formula components for payroll calculations - equivalent to Rubriqueformule.java
    
    Defines the mathematical formulas used to calculate payroll element values.
    Each formula component can be an operator, number, function, or reference to another rubric.
    """
    
    # Section choices - which part of the calculation this component belongs to
    SECTION_CHOICES = [
        ('B', 'Base'),    # Base calculation
        ('N', 'Number'),  # Number/quantity calculation
    ]
    
    # Component type choices
    COMPONENT_TYPE_CHOICES = [
        ('O', 'Operator'),           # Mathematical operator (+, -, *, /)
        ('N', 'Number'),             # Numeric value
        ('F', 'Function'),           # Function call
        ('R', 'Rubrique Reference'), # Reference to another payroll element
    ]
    
    id = models.AutoField(primary_key=True)
    payroll_element = models.ForeignKey(
        PayrollElement, 
        on_delete=models.CASCADE,
        related_name='formulas'
    )  # rubrique
    
    section = models.CharField(max_length=1, choices=SECTION_CHOICES)  # partie
    component_type = models.CharField(max_length=1, choices=COMPONENT_TYPE_CHOICES)  # type
    
    # Values - only one should be populated based on component_type
    text_value = models.CharField(max_length=10, blank=True, null=True)  # valText
    numeric_value = models.DecimalField(max_digits=22, decimal_places=0, blank=True, null=True)  # valNum
    
    class Meta:
        db_table = 'rubriqueformule'
        ordering = ['payroll_element', 'section', 'id']
        verbose_name = 'Payroll Element Formula'
        verbose_name_plural = 'Payroll Element Formulas'
    
    def __str__(self):
        value = self.text_value if self.text_value else self.numeric_value
        return f"{self.payroll_element.label} - {self.get_section_display()}: {self.get_component_type_display()} ({value})"
    
    def clean(self):
        """Validate that only one value field is populated"""
        if self.text_value and self.numeric_value is not None:
            raise ValidationError("Only one of text_value or numeric_value should be populated")
        if not self.text_value and self.numeric_value is None:
            raise ValidationError("Either text_value or numeric_value must be populated")
    
    @property
    def value(self):
        """Get the actual value regardless of type"""
        return self.text_value if self.text_value else self.numeric_value
    
    def is_operator(self):
        """Check if this component is an operator"""
        return self.component_type == 'O'
    
    def is_number(self):
        """Check if this component is a number"""
        return self.component_type == 'N'
    
    def is_function(self):
        """Check if this component is a function"""
        return self.component_type == 'F'
    
    def is_rubrique_reference(self):
        """Check if this component is a reference to another rubrique"""
        return self.component_type == 'R'


class PayrollElementModel(models.Model):
    """
    Position-based payroll element templates - equivalent to Rubriquemodel.java
    
    Defines default payroll elements and amounts for specific positions.
    Used as templates when creating employee payroll based on their position.
    """
    
    id = models.BigAutoField(primary_key=True)
    
    # Foreign key relationships
    position = models.ForeignKey(
        Position,
        on_delete=models.CASCADE,
        db_column='poste',
        related_name='payroll_element_models',
        blank=True,
        null=True
    )  # poste - optional, can be null for general templates
    
    payroll_element = models.ForeignKey(
        PayrollElement,
        on_delete=models.CASCADE,
        db_column='rubrique',
        related_name='position_models'
    )  # rubrique
    
    # Default amount for this position/element combination
    amount = models.DecimalField(
        max_digits=22,
        decimal_places=0,
        default=0
    )  # montant
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'rubriquemodel'
        unique_together = ['position', 'payroll_element']
        ordering = ['position__name', 'payroll_element__label']
        verbose_name = 'Payroll Element Model'
        verbose_name_plural = 'Payroll Element Models'
    
    def __str__(self):
        position_name = self.position.name if self.position else 'General'
        return f"{position_name} - {self.payroll_element.label}: {self.amount}"
    
    def clean(self):
        """Validate the model"""
        if self.amount < 0:
            raise ValidationError("Amount cannot be negative")
    
    @property
    def is_general_template(self):
        """Check if this is a general template (not position-specific)"""
        return self.position is None
    
    @property
    def is_position_specific(self):
        """Check if this is specific to a position"""
        return self.position is not None