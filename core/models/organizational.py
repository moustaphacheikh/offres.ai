from django.db import models
from django.core.exceptions import ValidationError


class GeneralDirection(models.Model):
    """Top-level organizational unit - equivalent to Directiongeneral.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50, verbose_name="Nom")  # nom
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'directiongeneral'
        ordering = ['name']
        verbose_name = "Direction Générale"
        verbose_name_plural = "Directions Générales"
    
    def __str__(self):
        return self.name
    
    def clean(self):
        if self.name and self.name.strip() == "":
            raise ValidationError("Le nom ne peut pas être vide")
    
    @property
    def employee_count(self):
        """Return total number of employees across all directions"""
        return sum(direction.employee_count for direction in self.directions.all())


class Direction(models.Model):
    """Mid-level organizational unit - equivalent to Direction.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50, verbose_name="Nom")  # nom
    # Note: Based on Employee.java analysis, Direction appears to be independent of GeneralDirection
    # Employees can belong to both Direction and GeneralDirection independently
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'direction'
        ordering = ['name']
        verbose_name = "Direction"
        verbose_name_plural = "Directions"
    
    def __str__(self):
        return self.name
    
    def clean(self):
        if self.name and self.name.strip() == "":
            raise ValidationError("Le nom ne peut pas être vide")
    
    @property
    def employee_count(self):
        """Return total number of employees in this direction"""
        return self.employees.filter(actif=True).count()
    
    @property
    def department_count(self):
        """Return number of departments in this direction"""
        return self.departments.count()


class Department(models.Model):
    """Operational department - equivalent to Departement.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50, verbose_name="Nom")  # nom
    # Note: Based on Employee.java analysis, Department appears to be independent of Direction
    # Employees can belong to both Department and Direction independently
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'departement'
        ordering = ['name']
        verbose_name = "Département"
        verbose_name_plural = "Départements"
    
    def __str__(self):
        return self.name
    
    def clean(self):
        if self.name and self.name.strip() == "":
            raise ValidationError("Le nom ne peut pas être vide")
    
    @property
    def employee_count(self):
        """Return total number of active employees in this department"""
        return self.employees.filter(actif=True).count()


class Position(models.Model):
    """Job positions - equivalent to Poste.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=300, verbose_name="Nom")  # nom
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'poste'
        ordering = ['name']
        verbose_name = "Poste"
        verbose_name_plural = "Postes"
    
    def __str__(self):
        return self.name
    
    def clean(self):
        if self.name and self.name.strip() == "":
            raise ValidationError("Le nom ne peut pas être vide")
    
    @property
    def employee_count(self):
        """Return total number of active employees in this position"""
        return self.employees.filter(actif=True).count()
    
    @property
    def has_rubrique_models(self):
        """Check if this position has associated salary rubrique models"""
        return self.rubrique_models.exists()