from django.db import models
from .reference import EmployeeStatus


class SalaryGrade(models.Model):
    """Salary scales and grades - equivalent to Grillesalairebase.java"""
    category = models.CharField(max_length=50, primary_key=True)  # categorie (PK)
    base_salary = models.DecimalField(max_digits=22, decimal_places=2)  # salaireBase
    category_name = models.CharField(max_length=10)  # nomCategorie
    level = models.IntegerField()  # niveau
    status = models.ForeignKey(
        EmployeeStatus, 
        on_delete=models.PROTECT, 
        related_name='salary_grades'
    )  # statut
    
    class Meta:
        db_table = 'grillesalairebase'
        ordering = ['level', 'category']
    
    def __str__(self):
        return f"{self.category} - {self.category_name}"


class HousingGrid(models.Model):
    """Housing allowance grid - equivalent to Grillelogement.java"""
    id = models.AutoField(primary_key=True)
    salary_grade = models.ForeignKey(
        SalaryGrade, 
        on_delete=models.CASCADE, 
        related_name='housing_allowances'
    )  # grillesalairebase
    marital_status = models.CharField(max_length=1)  # situationFamiliale
    children_count = models.IntegerField()  # nbEnfants
    amount = models.DecimalField(max_digits=22, decimal_places=2, blank=True, null=True)  # montant
    
    class Meta:
        db_table = 'grillelogement'
        unique_together = ['salary_grade', 'marital_status', 'children_count']
        ordering = ['salary_grade', 'marital_status', 'children_count']
    
    def __str__(self):
        return f"{self.salary_grade.category} - {self.marital_status} - {self.children_count} children"