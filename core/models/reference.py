from django.db import models


class Activity(models.Model):
    """Business activities - equivalent to Activite.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50)  # nom
    
    class Meta:
        db_table = 'activite'
        ordering = ['name']
        verbose_name_plural = 'Activities'
    
    def __str__(self):
        return self.name


class Bank(models.Model):
    """Banking institutions - equivalent to Banque.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50)  # nom
    accounting_account = models.BigIntegerField(blank=True, null=True)  # noCompteCompta
    accounting_chapter = models.BigIntegerField(blank=True, null=True)  # noChapitreCompta
    accounting_key = models.CharField(max_length=50, blank=True)  # noCompteComptaCle
    
    class Meta:
        db_table = 'banque'
        ordering = ['name']
    
    def __str__(self):
        return self.name


class Origin(models.Model):
    """Employee origins - equivalent to Origines.java"""
    id = models.AutoField(primary_key=True)
    label = models.CharField(max_length=100)  # libelle
    smig_hours_for_leave_allowance = models.IntegerField(blank=True, null=True)  # nbSmighorPourIndConges
    
    class Meta:
        db_table = 'origines'
        ordering = ['label']
    
    def __str__(self):
        return self.label


class EmployeeStatus(models.Model):
    """Employee status types - equivalent to Statut.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=250)  # nom
    
    class Meta:
        db_table = 'statut'
        ordering = ['name']
        verbose_name_plural = 'Employee Statuses'
    
    def __str__(self):
        return self.name


class PayrollMotif(models.Model):
    """Payroll processing reasons - equivalent to Motif.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50)  # nom
    
    # Tax Applicability for Employees
    employee_subject_to_its = models.BooleanField(default=True)  # employeSoumisIts
    employee_subject_to_cnss = models.BooleanField(default=True)  # employeSoumisCnss
    employee_subject_to_cnam = models.BooleanField(default=True)  # employeSoumisCnam
    
    # Declaration Inclusion
    declaration_subject_to_its = models.BooleanField(default=True)  # declarationSoumisIts
    declaration_subject_to_cnss = models.BooleanField(default=True)  # declarationSoumisCnss
    declaration_subject_to_cnam = models.BooleanField(default=True)  # declarationSoumisCnam
    
    is_active = models.BooleanField(default=True)  # actif
    
    class Meta:
        db_table = 'motif'
        ordering = ['name']
    
    def __str__(self):
        return self.name