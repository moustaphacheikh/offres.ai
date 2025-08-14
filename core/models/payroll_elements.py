from django.db import models


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
    numeric_value = models.DecimalField(max_digits=22, decimal_places=2, blank=True, null=True)  # valNum
    
    class Meta:
        db_table = 'rubriqueformule'
        ordering = ['payroll_element', 'section', 'id']
        verbose_name = 'Payroll Element Formula'
        verbose_name_plural = 'Payroll Element Formulas'
    
    def __str__(self):
        value = self.text_value if self.text_value else self.numeric_value
        return f"{self.payroll_element.label} - {self.get_section_display()}: {self.get_component_type_display()} ({value})"