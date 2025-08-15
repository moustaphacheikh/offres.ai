from django.db import models
from django.core.exceptions import ValidationError


class Activity(models.Model):
    """Business activities - equivalent to Activite.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50, verbose_name="Nom")  # nom
    
    class Meta:
        db_table = 'activite'
        ordering = ['name']
        verbose_name = 'Activité'
        verbose_name_plural = 'Activités'
    
    def __str__(self):
        return self.name
    
    def clean(self):
        if self.name and self.name.strip() == "":
            raise ValidationError("Le nom ne peut pas être vide")
    
    @property
    def employee_count(self):
        """Return total number of active employees in this activity"""
        return self.employees.filter(actif=True).count()


class Bank(models.Model):
    """Banking institutions - equivalent to Banque.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50, verbose_name="Nom")  # nom
    accounting_account = models.BigIntegerField(blank=True, null=True, verbose_name="Compte comptable")  # noCompteCompta
    accounting_chapter = models.BigIntegerField(verbose_name="Chapitre comptable")  # noChapitreCompta - not nullable in Java
    accounting_key = models.CharField(max_length=50, verbose_name="Clé comptable")  # noCompteComptaCle - not nullable in Java
    
    class Meta:
        db_table = 'banque'
        ordering = ['name']
        verbose_name = 'Banque'
        verbose_name_plural = 'Banques'
    
    def __str__(self):
        return self.name
    
    def clean(self):
        if self.name and self.name.strip() == "":
            raise ValidationError("Le nom ne peut pas être vide")
        if self.accounting_key and self.accounting_key.strip() == "":
            raise ValidationError("La clé comptable ne peut pas être vide")


class Origin(models.Model):
    """Employee origins - equivalent to Origines.java"""
    id = models.AutoField(primary_key=True)
    label = models.CharField(max_length=100, verbose_name="Libellé")  # libelle
    smig_hours_for_leave_allowance = models.IntegerField(blank=True, null=True, verbose_name="Nb SMIG/h pour indemnité congés")  # nbSmighorPourIndConges
    
    class Meta:
        db_table = 'origines'
        ordering = ['label']
        verbose_name = 'Origine'
        verbose_name_plural = 'Origines'
    
    def __str__(self):
        return self.label
    
    def clean(self):
        if self.label and self.label.strip() == "":
            raise ValidationError("Le libellé ne peut pas être vide")
    
    @property
    def employee_count(self):
        """Return total number of active employees with this origin"""
        return self.employees.filter(actif=True).count()


class EmployeeStatus(models.Model):
    """Employee status types - equivalent to Statut.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=250, verbose_name="Nom")  # nom
    
    class Meta:
        db_table = 'statut'
        ordering = ['name']
        verbose_name = 'Statut employé'
        verbose_name_plural = 'Statuts employés'
    
    def __str__(self):
        return self.name
    
    def clean(self):
        if self.name and self.name.strip() == "":
            raise ValidationError("Le nom ne peut pas être vide")
    
    @property
    def has_salary_grids(self):
        """Check if this status has associated salary grids"""
        return hasattr(self, 'salary_grids') and self.salary_grids.exists()


class PayrollMotif(models.Model):
    """Payroll processing reasons - equivalent to Motif.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50, verbose_name="Nom")  # nom
    
    # Tax Applicability for Employees
    employee_subject_to_its = models.BooleanField(default=True, verbose_name="Employé soumis ITS")  # employeSoumisIts
    employee_subject_to_cnss = models.BooleanField(default=True, verbose_name="Employé soumis CNSS")  # employeSoumisCnss
    employee_subject_to_cnam = models.BooleanField(default=True, verbose_name="Employé soumis CNAM")  # employeSoumisCnam
    
    # Declaration Inclusion
    declaration_subject_to_its = models.BooleanField(default=True, verbose_name="Déclaration soumise ITS")  # declarationSoumisIts
    declaration_subject_to_cnss = models.BooleanField(default=True, verbose_name="Déclaration soumise CNSS")  # declarationSoumisCnss
    declaration_subject_to_cnam = models.BooleanField(default=True, verbose_name="Déclaration soumise CNAM")  # declarationSoumisCnam
    
    is_active = models.BooleanField(default=True, verbose_name="Actif")  # actif
    
    class Meta:
        db_table = 'motif'
        ordering = ['name']
        verbose_name = 'Motif paie'
        verbose_name_plural = 'Motifs paie'
    
    def __str__(self):
        return self.name
    
    def clean(self):
        if self.name and self.name.strip() == "":
            raise ValidationError("Le nom ne peut pas être vide")
    
    @property
    def is_tax_applicable(self):
        """Check if any tax is applicable for employees"""
        return (self.employee_subject_to_its or 
                self.employee_subject_to_cnss or 
                self.employee_subject_to_cnam)
    
    @property
    def is_declaration_applicable(self):
        """Check if any declaration is applicable"""
        return (self.declaration_subject_to_its or 
                self.declaration_subject_to_cnss or 
                self.declaration_subject_to_cnam)
    
    @property
    def payroll_count(self):
        """Return number of payrolls using this motif"""
        return self.payrolls.count() if hasattr(self, 'payrolls') else 0