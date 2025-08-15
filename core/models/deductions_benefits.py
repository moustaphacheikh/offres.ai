from django.db import models
from django.core.exceptions import ValidationError
from decimal import Decimal
from .employee import Employee
from .payroll_elements import PayrollElement
from .reference import PayrollMotif


class InstallmentDeduction(models.Model):
    """
    Installment deductions model - equivalent to Retenuesaecheances.java
    
    Manages employee deductions that are paid in installments over time,
    such as salary advances, loans, or other recurring deductions.
    """
    
    id = models.AutoField(primary_key=True)
    
    # Foreign key relationships
    employee = models.ForeignKey(
        Employee, 
        on_delete=models.CASCADE, 
        db_column='employe',
        related_name='installment_deductions'
    )
    payroll_element = models.ForeignKey(
        PayrollElement, 
        on_delete=models.CASCADE, 
        db_column='rubrique',
        related_name='installment_deductions'
    )
    
    # Period and agreement details
    period = models.DateField(blank=True, null=True)  # periode
    agreement_date = models.DateField()  # DateAccord
    
    # Financial amounts (Java uses scale=0, suggesting integer precision)
    capital = models.DecimalField(
        max_digits=22, 
        decimal_places=0, 
        default=0
    )  # capital - total amount/principal
    installment_amount = models.DecimalField(
        max_digits=22, 
        decimal_places=0, 
        default=0
    )  # echeance - installment amount per period
    current_installment = models.DecimalField(
        max_digits=22, 
        decimal_places=0, 
        blank=True, 
        null=True
    )  # echeancecourante - current installment amount
    current_installment_change = models.DecimalField(
        max_digits=22, 
        decimal_places=0, 
        blank=True, 
        null=True
    )  # echeancecourantecng - current installment amount after change
    
    # Status fields
    is_active = models.BooleanField(default=True)  # active
    is_settled = models.BooleanField(default=False)  # solde - fully paid/settled
    
    # Additional information
    note = models.CharField(max_length=500, blank=True)  # note
    
    # Timestamps
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'retenuesaecheances'
        ordering = ['agreement_date', 'employee__last_name', 'employee__first_name']
        verbose_name = 'Installment Deduction'
        verbose_name_plural = 'Installment Deductions'
    
    def __str__(self):
        return f"{self.employee} - {self.payroll_element} ({self.capital})"
    
    def clean(self):
        """Validate deduction data"""
        if self.capital and self.capital < 0:
            raise ValidationError("Le capital ne peut pas être négatif")
        if self.installment_amount and self.installment_amount < 0:
            raise ValidationError("Le montant d'échéance ne peut pas être négatif")
        if self.current_installment and self.current_installment < 0:
            raise ValidationError("L'échéance courante ne peut pas être négative")
    
    @property
    def remaining_balance(self):
        """Calculate remaining balance based on paid tranches (equivalent to Java totalReglementRetAE logic)"""
        paid_amount = sum(
            tranche.amount for tranche in self.installment_tranches.all()
        )
        return max(Decimal('0'), self.capital - paid_amount)
    
    @property
    def total_paid(self):
        """Calculate total amount paid through tranches (equivalent to Java totalReglementRetAE)"""
        return sum(
            tranche.amount for tranche in self.installment_tranches.all()
        )
    
    @property
    def is_fully_settled(self):
        """Check if deduction is fully settled (equivalent to Java solde logic)"""
        return self.total_paid >= self.capital
    
    def calculate_outstanding_amount(self):
        """Calculate outstanding amount (equivalent to Java encoursRetenueAE logic)"""
        if self.is_settled:
            return Decimal('0')
        return self.capital - self.total_paid
    
    def auto_settle_if_complete(self):
        """Auto-settle if deduction is fully paid (equivalent to Java business logic)"""
        if self.is_fully_settled and not self.is_settled:
            self.is_settled = True
            self.save()
            return True
        return False
    
    @classmethod
    def get_active_deductions_for_employee(cls, employee):
        """Get active deductions for an employee (equivalent to Java empRetAE)"""
        return cls.objects.filter(
            employee=employee,
            is_active=True,
            is_settled=False
        )


class InstallmentTranche(models.Model):
    """
    Installment tranche model - equivalent to Tranchesretenuesaecheances.java
    
    Represents individual payment installments for a deduction,
    tracking payment status and amounts for each period.
    """
    
    id = models.AutoField(primary_key=True)
    
    # Foreign key relationship
    installment_deduction = models.ForeignKey(
        InstallmentDeduction,
        on_delete=models.CASCADE,
        db_column='retenueAEcheances',
        related_name='installment_tranches'
    )
    
    # Foreign key to PayrollMotif (from Java Motif entity)
    motif = models.ForeignKey(
        PayrollMotif,
        on_delete=models.SET_NULL,
        blank=True,
        null=True,
        related_name='installment_tranches',
        verbose_name="Motif"
    )  # motif - payroll processing reason
    
    # Period and payment details
    period = models.DateField()  # periode - the period this tranche applies to
    amount = models.DecimalField(
        max_digits=22, 
        decimal_places=0, 
        default=0
    )  # montantRegle - amount to be paid/deducted (matching Java precision)
    
    # Status and tracking
    is_paid = models.BooleanField(default=False)  # derived from system logic
    payment_date = models.DateField(blank=True, null=True)  # when payment was processed
    
    # Additional tracking fields
    sequence_number = models.IntegerField(default=1)  # tranche sequence number
    
    # Timestamps
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'tranchesretenuesaecheances'
        ordering = ['installment_deduction', 'period', 'sequence_number']
        verbose_name = 'Installment Tranche'
        verbose_name_plural = 'Installment Tranches'
        unique_together = ['installment_deduction', 'period', 'sequence_number']
    
    def __str__(self):
        return f"{self.installment_deduction.employee} - {self.period} - {self.amount}"
    
    def clean(self):
        """Validate tranche data"""
        if self.amount and self.amount < 0:
            raise ValidationError("Le montant de la tranche ne peut pas être négatif")
        if self.sequence_number and self.sequence_number < 1:
            raise ValidationError("Le numéro de séquence doit être positif")
    
    def mark_as_paid(self, payment_date=None):
        """Mark this tranche as paid (equivalent to Java payment processing)"""
        from django.utils import timezone
        self.is_paid = True
        self.payment_date = payment_date or timezone.now().date()
        self.save()
        
        # Auto-settle parent deduction if fully paid
        self.installment_deduction.auto_settle_if_complete()
    
    def mark_as_unpaid(self):
        """Mark this tranche as unpaid"""
        self.is_paid = False
        self.payment_date = None
        self.save()
    
    @classmethod
    def get_tranches_for_period(cls, employee, period):
        """Get all tranches for an employee in a specific period (equivalent to Java logic)"""
        return cls.objects.filter(
            installment_deduction__employee=employee,
            period=period
        ).select_related('installment_deduction', 'motif')
    
    @classmethod
    def create_tranche_for_period(cls, installment_deduction, period, amount=None, motif=None):
        """Create a new tranche for a period (equivalent to Java updateTrancheRetAE logic)"""
        # Delete existing tranche for same period if exists
        cls.objects.filter(
            installment_deduction=installment_deduction,
            period=period
        ).delete()
        
        # Create new tranche
        tranche = cls.objects.create(
            installment_deduction=installment_deduction,
            period=period,
            amount=amount or installment_deduction.current_installment or installment_deduction.installment_amount,
            motif=motif,
            sequence_number=cls.objects.filter(installment_deduction=installment_deduction).count() + 1
        )
        return tranche