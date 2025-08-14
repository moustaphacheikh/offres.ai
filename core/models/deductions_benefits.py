from django.db import models
from .employee import Employee
from .payroll_elements import PayrollElement


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
    
    # Financial amounts
    capital = models.DecimalField(
        max_digits=22, 
        decimal_places=2, 
        default=0
    )  # capital - total amount/principal
    installment_amount = models.DecimalField(
        max_digits=22, 
        decimal_places=2, 
        default=0
    )  # echeance - installment amount per period
    current_installment = models.DecimalField(
        max_digits=22, 
        decimal_places=2, 
        blank=True, 
        null=True
    )  # echeancecourante - current installment amount
    current_installment_change = models.DecimalField(
        max_digits=22, 
        decimal_places=2, 
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
    
    @property
    def remaining_balance(self):
        """Calculate remaining balance based on paid tranches"""
        paid_amount = sum(
            tranche.amount for tranche in self.installment_tranches.filter(is_paid=True)
        )
        return max(0, float(self.capital) - float(paid_amount))
    
    @property
    def total_paid(self):
        """Calculate total amount paid through tranches"""
        return float(sum(
            tranche.amount for tranche in self.installment_tranches.filter(is_paid=True)
        ))


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
    
    # Period and payment details
    period = models.DateField()  # periode - the period this tranche applies to
    amount = models.DecimalField(
        max_digits=22, 
        decimal_places=2, 
        default=0
    )  # montantRegle - amount to be paid/deducted
    
    # Status and tracking
    is_paid = models.BooleanField(default=False)  # derived from system logic
    payment_date = models.DateField(blank=True, null=True)  # when payment was processed
    motif = models.IntegerField(blank=True, null=True)  # motif - reason code
    
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
    
    def mark_as_paid(self, payment_date=None):
        """Mark this tranche as paid"""
        from django.utils import timezone
        self.is_paid = True
        self.payment_date = payment_date or timezone.now().date()
        self.save()
    
    def mark_as_unpaid(self):
        """Mark this tranche as unpaid"""
        self.is_paid = False
        self.payment_date = None
        self.save()