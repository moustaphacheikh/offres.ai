from django.db import models
from django.core.validators import MinValueValidator
from .reference import EmployeeStatus


class SalaryGrade(models.Model):
    """Salary scales and grades - equivalent to Grillesalairebase.java"""
    category = models.CharField(
        max_length=50, 
        primary_key=True,
        help_text="Salary grade category identifier"
    )  # categorie (PK)
    base_salary = models.DecimalField(
        max_digits=22, 
        decimal_places=0,  # Matches Java precision=22, scale=0
        validators=[MinValueValidator(0)],
        help_text="Base salary amount for this grade"
    )  # salaireBase
    category_name = models.CharField(
        max_length=10,
        help_text="Short display name for the category"
    )  # nomCategorie
    level = models.IntegerField(
        validators=[MinValueValidator(1)],
        help_text="Hierarchical level of this salary grade"
    )  # niveau
    status = models.ForeignKey(
        EmployeeStatus, 
        on_delete=models.PROTECT, 
        related_name='salary_grades',
        help_text="Employee status this salary grade applies to"
    )  # statut
    
    class Meta:
        db_table = 'grillesalairebase'
        ordering = ['level', 'category']
        verbose_name = "Salary Grade"
        verbose_name_plural = "Salary Grades"
    
    def __str__(self):
        return f"{self.category} - {self.category_name} (Level {self.level})"
    
    def get_housing_allowance(self, marital_status, children_count):
        """
        Get housing allowance for this salary grade based on marital status and children count.
        
        Args:
            marital_status: Single character - C, M, D, or V
            children_count: Number of children
            
        Returns:
            HousingGrid instance or None if not found
        """
        try:
            return self.housing_allowances.get(
                marital_status=marital_status,
                children_count=children_count
            )
        except HousingGrid.DoesNotExist:
            return None
    
    def get_housing_allowance_amount(self, marital_status, children_count):
        """
        Get housing allowance amount for this salary grade.
        
        Returns:
            Decimal amount or 0 if no allowance found
        """
        housing_grid = self.get_housing_allowance(marital_status, children_count)
        return housing_grid.amount if housing_grid and housing_grid.amount else 0


class HousingGrid(models.Model):
    """Housing allowance grid - equivalent to Grillelogement.java"""
    
    # Marital status choices based on Java UI code
    MARITAL_STATUS_CHOICES = [
        ('C', 'Célibataire'),  # Single
        ('M', 'Marié'),        # Married
        ('D', 'Divorcé'),      # Divorced
        ('V', 'Veuf/Veuve'),   # Widowed
    ]
    
    id = models.AutoField(primary_key=True)
    salary_grade = models.ForeignKey(
        SalaryGrade, 
        on_delete=models.CASCADE, 
        related_name='housing_allowances',
        help_text="Salary grade this housing allowance applies to"
    )  # grillesalairebase
    marital_status = models.CharField(
        max_length=1,
        choices=MARITAL_STATUS_CHOICES,
        help_text="Marital status of the employee"
    )  # situationFamiliale
    children_count = models.IntegerField(
        validators=[MinValueValidator(0)],
        help_text="Number of children"
    )  # nbEnfants
    amount = models.DecimalField(
        max_digits=22, 
        decimal_places=0,  # Matches Java precision=22, scale=0
        blank=True, 
        null=True,
        validators=[MinValueValidator(0)],
        help_text="Housing allowance amount"
    )  # montant
    
    class Meta:
        db_table = 'grillelogement'
        unique_together = ['salary_grade', 'marital_status', 'children_count']
        ordering = ['salary_grade', 'marital_status', 'children_count']
        verbose_name = "Housing Allowance Grid"
        verbose_name_plural = "Housing Allowance Grids"
        indexes = [
            models.Index(fields=['salary_grade', 'marital_status', 'children_count']),
        ]
    
    def __str__(self):
        status_display = self.get_marital_status_display()
        return f"{self.salary_grade.category} - {status_display} - {self.children_count} children"
    
    def clean(self):
        """Custom validation for housing grid entries"""
        from django.core.exceptions import ValidationError
        
        if self.amount is not None and self.amount < 0:
            raise ValidationError("Housing allowance amount cannot be negative")
    
    @property
    def effective_amount(self):
        """Return the effective amount, defaulting to 0 if None"""
        return self.amount if self.amount is not None else 0


# Alias for backward compatibility (in case SalaryGrid was referenced elsewhere)
SalaryGrid = SalaryGrade