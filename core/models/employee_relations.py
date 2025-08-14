from django.db import models
from .employee import Employee


class Child(models.Model):
    """Employee children information - equivalent to Enfants.java
    
    Used for family allowance calculations and employee benefits.
    """
    id = models.AutoField(primary_key=True)
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='children'
    )
    child_name = models.CharField(max_length=100)  # nomEnfant
    birth_date = models.DateField()  # dateNaissanace (keeping original typo for consistency)
    parent_type = models.CharField(max_length=50)  # mereOuPere (mother or father)
    gender = models.CharField(max_length=10)  # genre
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'enfants'
        ordering = ['child_name']
        unique_together = ['employee', 'child_name', 'birth_date']
    
    def __str__(self):
        return f"{self.child_name} ({self.employee.full_name})"


class Leave(models.Model):
    """Employee leave records - equivalent to Conges.java
    
    Tracks employee vacation, sick leave, and other absences.
    """
    LEAVE_TYPE_CHOICES = [
        ('ANNUAL', 'Annual Leave'),
        ('SICK', 'Sick Leave'),
        ('MATERNITY', 'Maternity Leave'),
        ('PATERNITY', 'Paternity Leave'),
        ('COMPASSIONATE', 'Compassionate Leave'),
        ('UNPAID', 'Unpaid Leave'),
        ('OTHER', 'Other'),
    ]
    
    id = models.AutoField(primary_key=True)
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='leaves'
    )
    period = models.DateField()  # periode - leave period/year
    start_date = models.DateField()  # depart - departure date
    planned_return = models.DateField()  # reprise - planned return date
    actual_return = models.DateField(blank=True, null=True)  # repriseeff - actual return date
    notes = models.CharField(max_length=500, blank=True)  # note
    leave_type = models.CharField(
        max_length=20, 
        choices=LEAVE_TYPE_CHOICES, 
        default='ANNUAL'
    )
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'conges'
        ordering = ['-start_date']
    
    def __str__(self):
        return f"{self.employee.full_name} - {self.get_leave_type_display()} ({self.start_date})"
    
    @property
    def is_active(self):
        """Check if leave is currently active"""
        from django.utils import timezone
        today = timezone.now().date()
        return (
            self.start_date <= today and 
            (self.actual_return is None or self.actual_return >= today)
        )
    
    @property
    def planned_duration_days(self):
        """Calculate planned leave duration in days"""
        return (self.planned_return - self.start_date).days + 1
    
    @property
    def actual_duration_days(self):
        """Calculate actual leave duration in days"""
        if self.actual_return:
            return (self.actual_return - self.start_date).days + 1
        return None


class Document(models.Model):
    """Employee documents - equivalent to Document.java
    
    Manages employee document storage and metadata.
    """
    DOCUMENT_TYPE_CHOICES = [
        ('CONTRACT', 'Employment Contract'),
        ('ID_CARD', 'Identity Card'),
        ('PASSPORT', 'Passport'),
        ('DIPLOMA', 'Diploma/Certificate'),
        ('MEDICAL', 'Medical Certificate'),
        ('RESUME', 'Resume/CV'),
        ('REFERENCE', 'Reference Letter'),
        ('PHOTO', 'Employee Photo'),
        ('OTHER', 'Other Document'),
    ]
    
    id = models.AutoField(primary_key=True)
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='documents'
    )
    document_type = models.CharField(
        max_length=20, 
        choices=DOCUMENT_TYPE_CHOICES,
        default='OTHER'
    )  # Based on fileType but more structured
    document_name = models.CharField(max_length=500)  # nom
    file_path = models.CharField(max_length=1000, blank=True)  # File path instead of binary storage
    file_content = models.BinaryField(blank=True, null=True)  # docFile - for backwards compatibility
    file_type = models.CharField(max_length=100, blank=True)  # fileType - MIME type
    issue_date = models.DateField(blank=True, null=True)
    expiry_date = models.DateField(blank=True, null=True)
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'Document'  # Keeping original capitalization
        ordering = ['-created_at']
    
    def __str__(self):
        return f"{self.document_name} ({self.employee.full_name})"
    
    @property
    def is_expired(self):
        """Check if document is expired"""
        if not self.expiry_date:
            return False
        from django.utils import timezone
        return self.expiry_date < timezone.now().date()
    
    @property
    def expires_soon(self, days=30):
        """Check if document expires within specified days"""
        if not self.expiry_date:
            return False
        from django.utils import timezone
        from datetime import timedelta
        warning_date = timezone.now().date() + timedelta(days=days)
        return self.expiry_date <= warning_date


class Diploma(models.Model):
    """Employee diplomas and qualifications - equivalent to Diplome.java
    
    Tracks employee educational qualifications and certifications.
    """
    DEGREE_LEVEL_CHOICES = [
        ('HIGH_SCHOOL', 'High School'),
        ('DIPLOMA', 'Diploma'),
        ('BACHELOR', "Bachelor's Degree"),
        ('MASTER', "Master's Degree"),
        ('DOCTORATE', 'Doctorate/PhD'),
        ('CERTIFICATE', 'Professional Certificate'),
        ('OTHER', 'Other'),
    ]
    
    id = models.AutoField(primary_key=True)
    employee = models.ForeignKey(
        Employee,
        on_delete=models.CASCADE,
        related_name='diplomas'
    )
    diploma_name = models.CharField(max_length=200)  # nom
    institution = models.CharField(max_length=200)  # etablissement
    graduation_date = models.DateField()  # dateObtention
    level = models.CharField(
        max_length=20, 
        choices=DEGREE_LEVEL_CHOICES,
        default='OTHER'
    )  # degre - more structured
    field_of_study = models.CharField(max_length=200, blank=True)  # domaine
    
    # Additional fields for comprehensive tracking
    gpa_score = models.DecimalField(max_digits=4, decimal_places=2, blank=True, null=True)
    honors = models.CharField(max_length=100, blank=True)
    is_verified = models.BooleanField(default=False)
    verification_date = models.DateField(blank=True, null=True)
    
    # Audit fields
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'diplome'
        ordering = ['-graduation_date']
        unique_together = ['employee', 'diploma_name', 'institution', 'graduation_date']
    
    def __str__(self):
        return f"{self.diploma_name} - {self.institution} ({self.employee.full_name})"
    
    @property
    def years_since_graduation(self):
        """Calculate years since graduation"""
        from django.utils import timezone
        today = timezone.now().date()
        return today.year - self.graduation_date.year