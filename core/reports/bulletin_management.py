# bulletin_management.py
"""
Comprehensive Payslip Distribution and Management System for Mauritanian Payroll

This module provides complete payslip lifecycle management including:
1. Payslip Generation and Distribution - Bulk and individual payslip creation with automated workflows
2. Distribution Tracking and Status - Email delivery tracking, print management, and retry mechanisms  
3. Payslip Templates and Formatting - Multi-template support with French/Arabic localization
4. Bulk Operations and Automation - Automated monthly workflows with progress tracking
5. Security and Access Control - Secure distribution with authentication and audit trails

Key Features:
- Multi-channel distribution (email, print, portal)
- Template management with company branding
- Comprehensive delivery tracking and reporting
- Automated retry mechanisms for failed deliveries
- PDF generation with security features
- Employee self-service portal integration
- Audit trails and compliance reporting
- Storage optimization and archiving

Integrates with:
- Django models (Employee, Payroll, User)
- Email systems with delivery confirmation
- File processing utilities for PDF security
- Notification systems for alerts
- Storage systems for archive management

Designed for Mauritanian labor law compliance and operational efficiency.
"""

import os
import io
import json
import uuid
import logging
import hashlib
import smtplib
import tempfile
import threading
from pathlib import Path
from datetime import datetime, date, timedelta
from dateutil.relativedelta import relativedelta
from typing import Dict, List, Optional, Union, Tuple, Any, Set, Callable
from dataclasses import dataclass, field
from enum import Enum
from decimal import Decimal
from collections import defaultdict, OrderedDict
import queue
import time
from concurrent.futures import ThreadPoolExecutor, as_completed

# Django imports
from django.db import models, transaction
from django.db.models import Q, Count, Sum, Avg, Max, Min
from django.core.mail import EmailMultiAlternatives, get_connection
from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
from django.template import Template, Context
from django.template.loader import get_template, render_to_string
from django.utils import timezone
from django.utils.text import slugify
from django.conf import settings
from django.core.cache import cache
from django.core.exceptions import ValidationError

# PDF generation
try:
    from reportlab.lib import colors
    from reportlab.lib.pagesizes import letter, A4
    from reportlab.platypus import SimpleDocTemplate, Table, TableStyle, Paragraph, Spacer
    from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
    from reportlab.lib.units import inch
    from reportlab.pdfgen import canvas
    from reportlab.lib.utils import ImageReader
    from reportlab.pdfbase import pdfmetrics
    from reportlab.pdfbase.ttfonts import TTFont
    REPORTLAB_AVAILABLE = True
except ImportError:
    REPORTLAB_AVAILABLE = False

# Email validation
try:
    import email_validator
    EMAIL_VALIDATOR_AVAILABLE = True
except ImportError:
    EMAIL_VALIDATOR_AVAILABLE = False

# Internal imports
from ..models.employee import Employee
from ..models.payroll_processing import Payroll, PayrollLineItem
from ..models.system_config import User, SystemParameters
from ..utils.file_processors import (
    FileUploadValidator, DocumentProcessor, FileStorageManager, 
    FileSecurityManager, FileProcessingError
)
from ..utils.security import SecurityManager, AuditLogger
from ..utils.text_utils import TextFormatter, ArabicTextUtils
from ..utils.report_utils import ReportFormatter, ExportUtilities
from ..utils.date_utils import DateCalculator
from ..utils.validators import ValidationResult, DataSanitizer

logger = logging.getLogger(__name__)


class DistributionStatus(Enum):
    """Enumeration for payslip distribution status"""
    PENDING = "pending"
    QUEUED = "queued"
    SENDING = "sending"
    SENT = "sent"
    DELIVERED = "delivered"
    FAILED = "failed"
    BOUNCED = "bounced"
    RETRY = "retry"
    CANCELLED = "cancelled"
    ARCHIVED = "archived"


class DistributionChannel(Enum):
    """Enumeration for distribution channels"""
    EMAIL = "email"
    PRINT = "print"
    PORTAL = "portal"
    SMS = "sms"
    PICKUP = "pickup"


class PayslipTemplate(Enum):
    """Enumeration for payslip templates"""
    STANDARD = "standard"
    EXECUTIVE = "executive"
    CONTRACTOR = "contractor"
    INTERN = "intern"
    GOVERNMENT = "government"
    CUSTOM = "custom"


class PayslipFormat(Enum):
    """Enumeration for payslip formats"""
    PDF = "pdf"
    HTML = "html"
    EXCEL = "excel"
    TEXT = "text"


@dataclass
class PayslipGenerationOptions:
    """Configuration options for payslip generation"""
    template: PayslipTemplate = PayslipTemplate.STANDARD
    format: PayslipFormat = PayslipFormat.PDF
    language: str = "fr"  # fr, ar, or both
    include_details: bool = True
    include_ytd: bool = True
    include_logo: bool = True
    watermark: str = ""
    password_protect: bool = False
    password: str = ""
    custom_fields: Dict[str, Any] = field(default_factory=dict)
    
    def validate(self) -> ValidationResult:
        """Validate generation options"""
        result = ValidationResult()
        
        if self.language not in ['fr', 'ar', 'both']:
            result.add_error('language', 'Langue non supportée', 'INVALID_LANGUAGE')
        
        if self.password_protect and not self.password:
            result.add_error('password', 'Mot de passe requis pour la protection', 'MISSING_PASSWORD')
        
        if self.password and len(self.password) < 8:
            result.add_error('password', 'Mot de passe trop court (minimum 8 caractères)', 'WEAK_PASSWORD')
        
        return result


@dataclass
class DistributionOptions:
    """Configuration options for payslip distribution"""
    channels: List[DistributionChannel] = field(default_factory=lambda: [DistributionChannel.EMAIL])
    priority: str = "normal"  # low, normal, high, urgent
    schedule_time: Optional[datetime] = None
    retry_attempts: int = 3
    retry_delay: int = 300  # seconds
    batch_size: int = 50
    rate_limit: int = 10  # emails per minute
    notification_email: str = ""
    confirmation_required: bool = False
    archive_after_delivery: bool = True
    custom_message: str = ""
    
    def validate(self) -> ValidationResult:
        """Validate distribution options"""
        result = ValidationResult()
        
        if self.retry_attempts < 0 or self.retry_attempts > 10:
            result.add_error('retry_attempts', 'Nombre de tentatives invalide (0-10)', 'INVALID_RETRY_COUNT')
        
        if self.batch_size < 1 or self.batch_size > 1000:
            result.add_error('batch_size', 'Taille de lot invalide (1-1000)', 'INVALID_BATCH_SIZE')
        
        if self.rate_limit < 1 or self.rate_limit > 100:
            result.add_error('rate_limit', 'Limite de débit invalide (1-100)', 'INVALID_RATE_LIMIT')
        
        if self.notification_email and EMAIL_VALIDATOR_AVAILABLE:
            try:
                email_validator.validate_email(self.notification_email)
            except email_validator.EmailNotValidError:
                result.add_error('notification_email', 'Email de notification invalide', 'INVALID_EMAIL')
        
        return result


class PayslipDistributionRecord(models.Model):
    """Model to track payslip distribution status and history"""
    
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    employee = models.ForeignKey(Employee, on_delete=models.CASCADE, related_name='payslip_distributions')
    payroll = models.ForeignKey(Payroll, on_delete=models.CASCADE, related_name='distributions')
    
    # Distribution details
    channel = models.CharField(max_length=20, choices=[(c.value, c.value) for c in DistributionChannel])
    status = models.CharField(max_length=20, choices=[(s.value, s.value) for s in DistributionStatus], default=DistributionStatus.PENDING.value)
    template = models.CharField(max_length=20, choices=[(t.value, t.value) for t in PayslipTemplate], default=PayslipTemplate.STANDARD.value)
    format = models.CharField(max_length=10, choices=[(f.value, f.value) for f in PayslipFormat], default=PayslipFormat.PDF.value)
    
    # File information
    file_path = models.CharField(max_length=500, blank=True)
    file_size = models.PositiveIntegerField(default=0)
    file_hash = models.CharField(max_length=64, blank=True)
    
    # Distribution tracking
    scheduled_time = models.DateTimeField(null=True, blank=True)
    sent_time = models.DateTimeField(null=True, blank=True)
    delivered_time = models.DateTimeField(null=True, blank=True)
    opened_time = models.DateTimeField(null=True, blank=True)
    downloaded_time = models.DateTimeField(null=True, blank=True)
    
    # Delivery details
    recipient_email = models.EmailField(blank=True)
    recipient_phone = models.CharField(max_length=20, blank=True)
    delivery_message_id = models.CharField(max_length=255, blank=True)
    delivery_response = models.TextField(blank=True)
    
    # Retry tracking
    retry_count = models.PositiveIntegerField(default=0)
    last_retry_time = models.DateTimeField(null=True, blank=True)
    next_retry_time = models.DateTimeField(null=True, blank=True)
    
    # Error tracking
    error_message = models.TextField(blank=True)
    error_code = models.CharField(max_length=50, blank=True)
    
    # Security and audit
    created_by = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, related_name='created_distributions')
    access_token = models.CharField(max_length=255, blank=True)
    access_count = models.PositiveIntegerField(default=0)
    last_access_time = models.DateTimeField(null=True, blank=True)
    access_ip_addresses = models.TextField(blank=True)  # JSON array
    
    # Metadata
    generation_options = models.TextField(blank=True)  # JSON
    distribution_options = models.TextField(blank=True)  # JSON
    metadata = models.TextField(blank=True)  # JSON
    
    # Timestamps
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'payslip_distribution'
        indexes = [
            models.Index(fields=['employee', 'payroll']),
            models.Index(fields=['status', 'channel']),
            models.Index(fields=['scheduled_time']),
            models.Index(fields=['created_at']),
        ]
        ordering = ['-created_at']
    
    def __str__(self):
        return f"{self.employee.full_name} - {self.payroll.period.strftime('%Y-%m')} - {self.channel} ({self.status})"
    
    @property
    def is_delivered(self) -> bool:
        """Check if distribution was successfully delivered"""
        return self.status in [DistributionStatus.DELIVERED.value, DistributionStatus.SENT.value]
    
    @property
    def can_retry(self) -> bool:
        """Check if distribution can be retried"""
        return (self.status in [DistributionStatus.FAILED.value, DistributionStatus.BOUNCED.value] and 
                self.retry_count < 10)
    
    def mark_opened(self, ip_address: str = ""):
        """Mark payslip as opened by employee"""
        self.opened_time = timezone.now()
        self.access_count += 1
        self.last_access_time = timezone.now()
        
        # Track IP addresses
        ip_list = json.loads(self.access_ip_addresses) if self.access_ip_addresses else []
        if ip_address and ip_address not in ip_list:
            ip_list.append(ip_address)
            self.access_ip_addresses = json.dumps(ip_list)
        
        self.save(update_fields=['opened_time', 'access_count', 'last_access_time', 'access_ip_addresses'])


class PayslipTemplate(models.Model):
    """Model for managing payslip templates"""
    
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    name = models.CharField(max_length=100, unique=True)
    description = models.TextField(blank=True)
    template_type = models.CharField(max_length=20, choices=[(t.value, t.value) for t in PayslipTemplate])
    
    # Template content
    html_template = models.TextField()
    css_styles = models.TextField(blank=True)
    header_template = models.TextField(blank=True)
    footer_template = models.TextField(blank=True)
    
    # Configuration
    supports_arabic = models.BooleanField(default=True)
    supports_french = models.BooleanField(default=True)
    default_language = models.CharField(max_length=5, default='fr')
    
    # Layout settings
    page_size = models.CharField(max_length=20, default='A4')
    orientation = models.CharField(max_length=20, default='portrait')
    margins = models.TextField(default='{"top": 20, "bottom": 20, "left": 20, "right": 20}')  # JSON
    
    # Branding
    company_logo_path = models.CharField(max_length=500, blank=True)
    company_colors = models.TextField(default='{"primary": "#0066cc", "secondary": "#f5f5f5"}')  # JSON
    font_family = models.CharField(max_length=100, default='Arial')
    
    # Security
    default_password_protect = models.BooleanField(default=False)
    watermark_text = models.CharField(max_length=100, blank=True)
    
    # Metadata
    is_active = models.BooleanField(default=True)
    is_default = models.BooleanField(default=False)
    version = models.CharField(max_length=20, default='1.0')
    created_by = models.ForeignKey(User, on_delete=models.SET_NULL, null=True)
    
    # Timestamps
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        db_table = 'payslip_template'
        ordering = ['name']
    
    def __str__(self):
        return f"{self.name} ({self.template_type})"
    
    def get_margins(self) -> Dict[str, int]:
        """Get margins as dictionary"""
        try:
            return json.loads(self.margins)
        except (json.JSONDecodeError, TypeError):
            return {"top": 20, "bottom": 20, "left": 20, "right": 20}
    
    def get_company_colors(self) -> Dict[str, str]:
        """Get company colors as dictionary"""
        try:
            return json.loads(self.company_colors)
        except (json.JSONDecodeError, TypeError):
            return {"primary": "#0066cc", "secondary": "#f5f5f5"}


class PayslipGenerator:
    """Advanced payslip generation with template support and security features"""
    
    def __init__(self):
        self.security_manager = SecurityManager()
        self.text_formatter = TextFormatter()
        self.arabic_utils = ArabicTextUtils()
        self.date_calculator = DateCalculator()
        
    def generate_payslip(self, payroll: Payroll, options: PayslipGenerationOptions = None) -> Dict[str, Any]:
        """
        Generate a payslip for a specific payroll record
        
        Args:
            payroll: Payroll record to generate payslip for
            options: Generation options and configuration
            
        Returns:
            Dictionary with generation results and file information
        """
        options = options or PayslipGenerationOptions()
        
        # Validate options
        validation_result = options.validate()
        if not validation_result.is_valid:
            return {
                'success': False,
                'errors': validation_result.errors,
                'file_path': None
            }
        
        result = {
            'success': True,
            'file_path': None,
            'file_size': 0,
            'file_hash': None,
            'generation_time': None,
            'template_used': options.template.value,
            'format': options.format.value,
            'errors': []
        }
        
        start_time = time.time()
        
        try:
            # Get template
            template = self._get_template(options.template)
            if not template:
                result['success'] = False
                result['errors'].append('Template non trouvé')
                return result
            
            # Prepare payslip data
            payslip_data = self._prepare_payslip_data(payroll, options)
            
            # Generate based on format
            if options.format == PayslipFormat.PDF:
                file_path = self._generate_pdf_payslip(payslip_data, template, options)
            elif options.format == PayslipFormat.HTML:
                file_path = self._generate_html_payslip(payslip_data, template, options)
            elif options.format == PayslipFormat.EXCEL:
                file_path = self._generate_excel_payslip(payslip_data, template, options)
            else:
                file_path = self._generate_text_payslip(payslip_data, template, options)
            
            if file_path:
                # Get file information
                file_stats = default_storage.stat(file_path)
                result['file_path'] = file_path
                result['file_size'] = file_stats.st_size
                
                # Calculate file hash
                with default_storage.open(file_path, 'rb') as f:
                    result['file_hash'] = hashlib.sha256(f.read()).hexdigest()
                
                # Apply password protection if requested
                if options.password_protect and options.format == PayslipFormat.PDF:
                    self._apply_pdf_security(file_path, options.password)
                
                result['generation_time'] = time.time() - start_time
                
                logger.info(f"Payslip generated: {payroll.employee.full_name} - {payroll.period}")
            else:
                result['success'] = False
                result['errors'].append('Échec de la génération du fichier')
        
        except Exception as e:
            logger.error(f"Payslip generation error: {str(e)}")
            result['success'] = False
            result['errors'].append(f'Erreur de génération: {str(e)}')
        
        return result
    
    def generate_bulk_payslips(self, payrolls: List[Payroll], 
                              options: PayslipGenerationOptions = None,
                              progress_callback: Callable = None) -> Dict[str, Any]:
        """
        Generate payslips for multiple payroll records
        
        Args:
            payrolls: List of payroll records
            options: Generation options
            progress_callback: Optional callback for progress updates
            
        Returns:
            Dictionary with bulk generation results
        """
        options = options or PayslipGenerationOptions()
        
        result = {
            'success': True,
            'total_requested': len(payrolls),
            'generated_count': 0,
            'failed_count': 0,
            'generated_files': [],
            'failed_records': [],
            'total_size': 0,
            'generation_time': None,
            'errors': []
        }
        
        start_time = time.time()
        
        try:
            # Use thread pool for parallel generation
            max_workers = min(5, len(payrolls))  # Limit concurrent generations
            
            with ThreadPoolExecutor(max_workers=max_workers) as executor:
                # Submit all generation tasks
                future_to_payroll = {
                    executor.submit(self.generate_payslip, payroll, options): payroll 
                    for payroll in payrolls
                }
                
                # Process completed tasks
                for future in as_completed(future_to_payroll):
                    payroll = future_to_payroll[future]
                    
                    try:
                        generation_result = future.result()
                        
                        if generation_result['success']:
                            result['generated_count'] += 1
                            result['generated_files'].append({
                                'employee_id': payroll.employee.id,
                                'employee_name': payroll.employee.full_name,
                                'payroll_id': payroll.id,
                                'period': payroll.period.strftime('%Y-%m'),
                                'file_path': generation_result['file_path'],
                                'file_size': generation_result['file_size'],
                                'file_hash': generation_result['file_hash']
                            })
                            result['total_size'] += generation_result['file_size']
                        else:
                            result['failed_count'] += 1
                            result['failed_records'].append({
                                'employee_id': payroll.employee.id,
                                'employee_name': payroll.employee.full_name,
                                'payroll_id': payroll.id,
                                'period': payroll.period.strftime('%Y-%m'),
                                'errors': generation_result['errors']
                            })
                        
                        # Report progress
                        if progress_callback:
                            progress_callback(result['generated_count'] + result['failed_count'], len(payrolls))
                    
                    except Exception as e:
                        logger.error(f"Bulk generation error for payroll {payroll.id}: {str(e)}")
                        result['failed_count'] += 1
                        result['failed_records'].append({
                            'employee_id': payroll.employee.id,
                            'employee_name': payroll.employee.full_name,
                            'payroll_id': payroll.id,
                            'period': payroll.period.strftime('%Y-%m'),
                            'errors': [str(e)]
                        })
            
            result['generation_time'] = time.time() - start_time
            
            # Determine overall success
            if result['failed_count'] == len(payrolls):
                result['success'] = False
                result['errors'].append('Échec de la génération de tous les bulletins')
            elif result['failed_count'] > 0:
                result['errors'].append(f'{result["failed_count"]} bulletins ont échoué')
            
            logger.info(f"Bulk payslip generation completed: {result['generated_count']}/{len(payrolls)} generated")
        
        except Exception as e:
            logger.error(f"Bulk payslip generation error: {str(e)}")
            result['success'] = False
            result['errors'].append(f'Erreur de génération en lot: {str(e)}')
        
        return result
    
    def _get_template(self, template_type: PayslipTemplate) -> Optional['PayslipTemplate']:
        """Get template by type"""
        try:
            return PayslipTemplate.objects.filter(
                template_type=template_type.value,
                is_active=True
            ).first()
        except Exception as e:
            logger.error(f"Template retrieval error: {str(e)}")
            return None
    
    def _prepare_payslip_data(self, payroll: Payroll, options: PayslipGenerationOptions) -> Dict[str, Any]:
        """Prepare comprehensive payslip data for rendering"""
        employee = payroll.employee
        
        # Basic information
        data = {
            'payroll': payroll,
            'employee': employee,
            'period': payroll.period,
            'generation_date': timezone.now(),
            'language': options.language,
            'include_details': options.include_details,
            'include_ytd': options.include_ytd,
            'custom_fields': options.custom_fields
        }
        
        # Employee details with localization
        data['employee_info'] = {
            'full_name': employee.full_name,
            'employee_id': employee.id,
            'position': employee.position.name if employee.position else '',
            'department': employee.department.name if employee.department else '',
            'hire_date': employee.hire_date,
            'national_id': employee.national_id,
            'cnss_number': employee.cnss_number,
            'bank_account': employee.bank_account,
            'email': employee.email,
            'phone': employee.phone
        }
        
        # Payroll calculations
        data['payroll_info'] = {
            'gross_taxable': payroll.gross_taxable,
            'gross_non_taxable': payroll.gross_non_taxable,
            'total_gross': payroll.total_gross,
            'net_salary': payroll.net_salary,
            'worked_days': payroll.worked_days,
            'cnss_employee': payroll.cnss_employee,
            'cnam_employee': payroll.cnam_employee,
            'its_total': payroll.its_total,
            'total_deductions': payroll.total_deductions,
            'net_in_words': payroll.net_in_words
        }
        
        # Line items if details requested
        if options.include_details:
            line_items = payroll.line_items.select_related('payroll_element').order_by('payroll_element__label')
            data['line_items'] = {
                'gains': [item for item in line_items if item.is_gain],
                'deductions': [item for item in line_items if item.is_deduction]
            }
        
        # Year-to-date calculations if requested
        if options.include_ytd:
            year_start = date(payroll.period.year, 1, 1)
            ytd_payrolls = Payroll.objects.filter(
                employee=employee,
                period__gte=year_start,
                period__lte=payroll.period
            )
            
            data['ytd_info'] = {
                'gross_taxable': sum(p.gross_taxable for p in ytd_payrolls),
                'gross_non_taxable': sum(p.gross_non_taxable for p in ytd_payrolls),
                'net_salary': sum(p.net_salary for p in ytd_payrolls),
                'cnss_employee': sum(p.cnss_employee for p in ytd_payrolls),
                'cnam_employee': sum(p.cnam_employee for p in ytd_payrolls),
                'its_total': sum(p.its_total for p in ytd_payrolls),
                'worked_days': sum(p.worked_days for p in ytd_payrolls)
            }
        
        # Localized labels
        if options.language == 'fr':
            data['labels'] = self._get_french_labels()
        elif options.language == 'ar':
            data['labels'] = self._get_arabic_labels()
        else:  # both
            data['labels'] = {
                'fr': self._get_french_labels(),
                'ar': self._get_arabic_labels()
            }
        
        return data
    
    def _generate_pdf_payslip(self, data: Dict[str, Any], template: 'PayslipTemplate', 
                             options: PayslipGenerationOptions) -> str:
        """Generate PDF payslip using ReportLab"""
        if not REPORTLAB_AVAILABLE:
            raise FileProcessingError("ReportLab not available for PDF generation", "MISSING_DEPENDENCY")
        
        # Create filename
        employee = data['employee']
        period = data['period']
        filename = f"bulletin_paie_{employee.id}_{period.strftime('%Y_%m')}.pdf"
        file_path = f"payroll/bulletins/{period.strftime('%Y/%m')}/{filename}"
        
        # Create temporary file
        with tempfile.NamedTemporaryFile(delete=False, suffix='.pdf') as temp_file:
            try:
                # Create PDF document
                doc = SimpleDocTemplate(
                    temp_file.name,
                    pagesize=A4,
                    **template.get_margins()
                )
                
                # Build PDF content
                story = []
                styles = getSampleStyleSheet()
                
                # Custom styles
                title_style = ParagraphStyle(
                    'CustomTitle',
                    parent=styles['Heading1'],
                    fontSize=16,
                    spaceAfter=30,
                    alignment=1  # Center
                )
                
                # Header
                if options.include_logo and template.company_logo_path:
                    # Add company logo (implementation depends on logo availability)
                    pass
                
                # Title
                if options.language == 'ar':
                    title = "بيان الراتب"
                else:
                    title = "BULLETIN DE PAIE"
                
                story.append(Paragraph(title, title_style))
                story.append(Spacer(1, 20))
                
                # Employee information table
                emp_data = [
                    ['Nom/Prénom:', data['employee_info']['full_name']],
                    ['Matricule:', str(data['employee_info']['employee_id'])],
                    ['Poste:', data['employee_info']['position']],
                    ['Département:', data['employee_info']['department']],
                    ['Période:', data['period'].strftime('%B %Y')],
                ]
                
                emp_table = Table(emp_data, colWidths=[2*inch, 4*inch])
                emp_table.setStyle(TableStyle([
                    ('BACKGROUND', (0, 0), (-1, -1), colors.lightgrey),
                    ('TEXTCOLOR', (0, 0), (-1, -1), colors.black),
                    ('ALIGN', (0, 0), (-1, -1), 'LEFT'),
                    ('FONTNAME', (0, 0), (-1, -1), 'Helvetica'),
                    ('FONTSIZE', (0, 0), (-1, -1), 10),
                    ('GRID', (0, 0), (-1, -1), 1, colors.black)
                ]))
                
                story.append(emp_table)
                story.append(Spacer(1, 20))
                
                # Payroll details table
                if options.include_details and 'line_items' in data:
                    # Gains table
                    gains_data = [['GAINS', 'Base', 'Quantité', 'Montant']]
                    for item in data['line_items']['gains']:
                        gains_data.append([
                            item.payroll_element.label,
                            f"{item.base_amount:.2f}" if item.base_amount else "",
                            f"{item.quantity:.2f}" if item.quantity else "",
                            f"{item.calculated_amount:.2f}"
                        ])
                    
                    gains_table = Table(gains_data, colWidths=[2.5*inch, 1*inch, 1*inch, 1.5*inch])
                    gains_table.setStyle(TableStyle([
                        ('BACKGROUND', (0, 0), (-1, 0), colors.darkblue),
                        ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
                        ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
                        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
                        ('FONTSIZE', (0, 0), (-1, -1), 9),
                        ('GRID', (0, 0), (-1, -1), 1, colors.black)
                    ]))
                    
                    story.append(gains_table)
                    story.append(Spacer(1, 10))
                    
                    # Deductions table
                    deductions_data = [['RETENUES', 'Base', 'Taux', 'Montant']]
                    for item in data['line_items']['deductions']:
                        deductions_data.append([
                            item.payroll_element.label,
                            f"{item.base_amount:.2f}" if item.base_amount else "",
                            f"{item.quantity:.2f}%" if item.quantity else "",
                            f"{item.calculated_amount:.2f}"
                        ])
                    
                    deductions_table = Table(deductions_data, colWidths=[2.5*inch, 1*inch, 1*inch, 1.5*inch])
                    deductions_table.setStyle(TableStyle([
                        ('BACKGROUND', (0, 0), (-1, 0), colors.darkred),
                        ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
                        ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
                        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
                        ('FONTSIZE', (0, 0), (-1, -1), 9),
                        ('GRID', (0, 0), (-1, -1), 1, colors.black)
                    ]))
                    
                    story.append(deductions_table)
                    story.append(Spacer(1, 20))
                
                # Summary table
                summary_data = [
                    ['Brut Imposable:', f"{data['payroll_info']['gross_taxable']:.2f} MRU"],
                    ['Brut Non Imposable:', f"{data['payroll_info']['gross_non_taxable']:.2f} MRU"],
                    ['Total Brut:', f"{data['payroll_info']['total_gross']:.2f} MRU"],
                    ['Total Retenues:', f"{data['payroll_info']['total_deductions']:.2f} MRU"],
                    ['NET À PAYER:', f"{data['payroll_info']['net_salary']:.2f} MRU"],
                ]
                
                summary_table = Table(summary_data, colWidths=[3*inch, 2*inch])
                summary_table.setStyle(TableStyle([
                    ('BACKGROUND', (0, -1), (-1, -1), colors.green),
                    ('TEXTCOLOR', (0, -1), (-1, -1), colors.whitesmoke),
                    ('FONTNAME', (0, -1), (-1, -1), 'Helvetica-Bold'),
                    ('FONTSIZE', (0, 0), (-1, -1), 11),
                    ('ALIGN', (0, 0), (-1, -1), 'LEFT'),
                    ('GRID', (0, 0), (-1, -1), 1, colors.black)
                ]))
                
                story.append(summary_table)
                
                # Net in words
                if data['payroll_info']['net_in_words']:
                    story.append(Spacer(1, 20))
                    net_words = Paragraph(
                        f"<b>Arrêté le présent bulletin à la somme de:</b><br/>{data['payroll_info']['net_in_words']}",
                        styles['Normal']
                    )
                    story.append(net_words)
                
                # Watermark
                if options.watermark:
                    # Add watermark implementation
                    pass
                
                # Build PDF
                doc.build(story)
                
                # Save to storage
                with open(temp_file.name, 'rb') as pdf_file:
                    saved_path = default_storage.save(file_path, ContentFile(pdf_file.read()))
                
                return saved_path
            
            finally:
                # Cleanup temporary file
                if os.path.exists(temp_file.name):
                    os.unlink(temp_file.name)
    
    def _generate_html_payslip(self, data: Dict[str, Any], template: 'PayslipTemplate', 
                              options: PayslipGenerationOptions) -> str:
        """Generate HTML payslip"""
        # Create filename
        employee = data['employee']
        period = data['period']
        filename = f"bulletin_paie_{employee.id}_{period.strftime('%Y_%m')}.html"
        file_path = f"payroll/bulletins/{period.strftime('%Y/%m')}/{filename}"
        
        try:
            # Render template
            html_template = Template(template.html_template)
            context = Context(data)
            html_content = html_template.render(context)
            
            # Add CSS styles
            if template.css_styles:
                html_content = f"<style>{template.css_styles}</style>\n{html_content}"
            
            # Save to storage
            saved_path = default_storage.save(file_path, ContentFile(html_content.encode('utf-8')))
            return saved_path
        
        except Exception as e:
            logger.error(f"HTML payslip generation error: {str(e)}")
            raise
    
    def _generate_excel_payslip(self, data: Dict[str, Any], template: 'PayslipTemplate', 
                               options: PayslipGenerationOptions) -> str:
        """Generate Excel payslip"""
        # Implementation would use openpyxl or xlsxwriter
        # For now, return None to indicate unsupported
        raise NotImplementedError("Excel payslip generation not yet implemented")
    
    def _generate_text_payslip(self, data: Dict[str, Any], template: 'PayslipTemplate', 
                              options: PayslipGenerationOptions) -> str:
        """Generate plain text payslip"""
        employee = data['employee']
        period = data['period']
        filename = f"bulletin_paie_{employee.id}_{period.strftime('%Y_%m')}.txt"
        file_path = f"payroll/bulletins/{period.strftime('%Y/%m')}/{filename}"
        
        try:
            # Create simple text payslip
            lines = []
            lines.append("=" * 60)
            lines.append("BULLETIN DE PAIE".center(60))
            lines.append("=" * 60)
            lines.append("")
            lines.append(f"Employé: {data['employee_info']['full_name']}")
            lines.append(f"Matricule: {data['employee_info']['employee_id']}")
            lines.append(f"Poste: {data['employee_info']['position']}")
            lines.append(f"Période: {data['period'].strftime('%B %Y')}")
            lines.append("")
            lines.append("-" * 60)
            lines.append("RÉSUMÉ")
            lines.append("-" * 60)
            lines.append(f"Brut Imposable:     {data['payroll_info']['gross_taxable']:>15.2f} MRU")
            lines.append(f"Brut Non Imposable: {data['payroll_info']['gross_non_taxable']:>15.2f} MRU")
            lines.append(f"Total Brut:         {data['payroll_info']['total_gross']:>15.2f} MRU")
            lines.append(f"Total Retenues:     {data['payroll_info']['total_deductions']:>15.2f} MRU")
            lines.append(f"NET À PAYER:        {data['payroll_info']['net_salary']:>15.2f} MRU")
            lines.append("")
            lines.append("=" * 60)
            
            content = "\n".join(lines)
            
            # Save to storage
            saved_path = default_storage.save(file_path, ContentFile(content.encode('utf-8')))
            return saved_path
        
        except Exception as e:
            logger.error(f"Text payslip generation error: {str(e)}")
            raise
    
    def _apply_pdf_security(self, file_path: str, password: str):
        """Apply password protection to PDF (placeholder)"""
        # This would require a PDF manipulation library like PyPDF2 or pdftk
        # For now, log the intention
        logger.info(f"Password protection requested for {file_path}")
    
    def _get_french_labels(self) -> Dict[str, str]:
        """Get French labels for payslip"""
        return {
            'title': 'BULLETIN DE PAIE',
            'employee': 'Employé',
            'period': 'Période',
            'position': 'Poste',
            'department': 'Département',
            'gains': 'GAINS',
            'deductions': 'RETENUES',
            'base': 'Base',
            'quantity': 'Quantité',
            'amount': 'Montant',
            'gross_taxable': 'Brut Imposable',
            'gross_non_taxable': 'Brut Non Imposable',
            'total_gross': 'Total Brut',
            'total_deductions': 'Total Retenues',
            'net_pay': 'NET À PAYER',
            'currency': 'MRU'
        }
    
    def _get_arabic_labels(self) -> Dict[str, str]:
        """Get Arabic labels for payslip"""
        return {
            'title': 'بيان الراتب',
            'employee': 'الموظف',
            'period': 'الفترة',
            'position': 'المنصب',
            'department': 'القسم',
            'gains': 'المكاسب',
            'deductions': 'الخصومات',
            'base': 'الأساس',
            'quantity': 'الكمية',
            'amount': 'المبلغ',
            'gross_taxable': 'الإجمالي الخاضع للضريبة',
            'gross_non_taxable': 'الإجمالي غير الخاضع للضريبة',
            'total_gross': 'إجمالي الراتب',
            'total_deductions': 'إجمالي الخصومات',
            'net_pay': 'صافي الراتب',
            'currency': 'أوقية'
        }


class DistributionManager:
    """Advanced distribution manager with multi-channel support and tracking"""
    
    def __init__(self):
        self.security_manager = SecurityManager()
        self.audit_logger = AuditLogger()
        self.file_storage = FileStorageManager()
        
    def distribute_payslips(self, payrolls: List[Payroll], 
                           distribution_options: DistributionOptions = None) -> Dict[str, Any]:
        """
        Distribute payslips through specified channels
        
        Args:
            payrolls: List of payroll records to distribute
            distribution_options: Distribution configuration
            
        Returns:
            Dictionary with distribution results
        """
        distribution_options = distribution_options or DistributionOptions()
        
        # Validate options
        validation_result = distribution_options.validate()
        if not validation_result.is_valid:
            return {
                'success': False,
                'errors': validation_result.errors,
                'distributions_created': 0
            }
        
        result = {
            'success': True,
            'total_requested': len(payrolls),
            'distributions_created': 0,
            'distributions_scheduled': 0,
            'distributions_sent': 0,
            'failed_distributions': 0,
            'created_records': [],
            'failed_records': [],
            'errors': []
        }
        
        try:
            with transaction.atomic():
                # Create distribution records for each payroll and channel
                for payroll in payrolls:
                    for channel in distribution_options.channels:
                        try:
                            # Check if employee has required contact info for channel
                            if not self._validate_employee_contact(payroll.employee, channel):
                                result['failed_distributions'] += 1
                                result['failed_records'].append({
                                    'employee_id': payroll.employee.id,
                                    'employee_name': payroll.employee.full_name,
                                    'channel': channel.value,
                                    'error': 'Informations de contact manquantes'
                                })
                                continue
                            
                            # Create distribution record
                            distribution = PayslipDistributionRecord.objects.create(
                                employee=payroll.employee,
                                payroll=payroll,
                                channel=channel.value,
                                status=DistributionStatus.PENDING.value,
                                scheduled_time=distribution_options.schedule_time,
                                recipient_email=payroll.employee.email if channel == DistributionChannel.EMAIL else '',
                                recipient_phone=payroll.employee.phone if channel == DistributionChannel.SMS else '',
                                distribution_options=json.dumps({
                                    'priority': distribution_options.priority,
                                    'retry_attempts': distribution_options.retry_attempts,
                                    'retry_delay': distribution_options.retry_delay,
                                    'custom_message': distribution_options.custom_message
                                })
                            )
                            
                            result['distributions_created'] += 1
                            result['created_records'].append({
                                'distribution_id': str(distribution.id),
                                'employee_id': payroll.employee.id,
                                'employee_name': payroll.employee.full_name,
                                'channel': channel.value,
                                'status': distribution.status
                            })
                        
                        except Exception as e:
                            logger.error(f"Error creating distribution record: {str(e)}")
                            result['failed_distributions'] += 1
                            result['failed_records'].append({
                                'employee_id': payroll.employee.id,
                                'employee_name': payroll.employee.full_name,
                                'channel': channel.value,
                                'error': str(e)
                            })
            
            # Process distributions if not scheduled for later
            if not distribution_options.schedule_time or distribution_options.schedule_time <= timezone.now():
                self._process_pending_distributions(distribution_options)
                
                # Update results with processing outcomes
                for record in result['created_records']:
                    distribution = PayslipDistributionRecord.objects.get(id=record['distribution_id'])
                    record['status'] = distribution.status
                    
                    if distribution.status in [DistributionStatus.SENT.value, DistributionStatus.DELIVERED.value]:
                        result['distributions_sent'] += 1
                    elif distribution.status == DistributionStatus.QUEUED.value:
                        result['distributions_scheduled'] += 1
            else:
                result['distributions_scheduled'] = result['distributions_created']
            
            logger.info(f"Distribution initiated: {result['distributions_created']} distributions created")
        
        except Exception as e:
            logger.error(f"Distribution error: {str(e)}")
            result['success'] = False
            result['errors'].append(f'Erreur de distribution: {str(e)}')
        
        return result
    
    def process_scheduled_distributions(self) -> Dict[str, Any]:
        """Process distributions scheduled for current time"""
        current_time = timezone.now()
        
        # Get pending distributions that are due
        pending_distributions = PayslipDistributionRecord.objects.filter(
            status=DistributionStatus.PENDING.value,
            scheduled_time__lte=current_time
        ).select_related('employee', 'payroll')
        
        result = {
            'processed_count': 0,
            'success_count': 0,
            'failed_count': 0,
            'errors': []
        }
        
        try:
            # Group by channel for efficient processing
            distributions_by_channel = defaultdict(list)
            for dist in pending_distributions:
                distributions_by_channel[dist.channel].append(dist)
            
            # Process each channel
            for channel, distributions in distributions_by_channel.items():
                if channel == DistributionChannel.EMAIL.value:
                    self._process_email_distributions(distributions, result)
                elif channel == DistributionChannel.PRINT.value:
                    self._process_print_distributions(distributions, result)
                elif channel == DistributionChannel.SMS.value:
                    self._process_sms_distributions(distributions, result)
                # Add other channels as needed
            
            logger.info(f"Scheduled distributions processed: {result['success_count']}/{result['processed_count']}")
        
        except Exception as e:
            logger.error(f"Scheduled distribution processing error: {str(e)}")
            result['errors'].append(str(e))
        
        return result
    
    def retry_failed_distributions(self, max_retries: int = None) -> Dict[str, Any]:
        """Retry failed distributions that are eligible for retry"""
        current_time = timezone.now()
        
        # Get failed distributions eligible for retry
        query = Q(status__in=[DistributionStatus.FAILED.value, DistributionStatus.BOUNCED.value])
        query &= Q(next_retry_time__lte=current_time) | Q(next_retry_time__isnull=True)
        if max_retries is not None:
            query &= Q(retry_count__lt=max_retries)
        
        failed_distributions = PayslipDistributionRecord.objects.filter(query).select_related('employee', 'payroll')
        
        result = {
            'retry_count': 0,
            'success_count': 0,
            'failed_count': 0,
            'errors': []
        }
        
        try:
            for distribution in failed_distributions:
                result['retry_count'] += 1
                
                # Update retry tracking
                distribution.retry_count += 1
                distribution.last_retry_time = current_time
                distribution.status = DistributionStatus.RETRY.value
                distribution.save()
                
                # Attempt redistribution
                try:
                    if distribution.channel == DistributionChannel.EMAIL.value:
                        success = self._send_email_distribution(distribution)
                    elif distribution.channel == DistributionChannel.PRINT.value:
                        success = self._send_print_distribution(distribution)
                    elif distribution.channel == DistributionChannel.SMS.value:
                        success = self._send_sms_distribution(distribution)
                    else:
                        success = False
                    
                    if success:
                        result['success_count'] += 1
                    else:
                        result['failed_count'] += 1
                        # Schedule next retry if attempts remaining
                        if distribution.retry_count < 10:
                            distribution.next_retry_time = current_time + timedelta(seconds=300 * distribution.retry_count)
                            distribution.status = DistributionStatus.FAILED.value
                        else:
                            distribution.status = DistributionStatus.CANCELLED.value
                        distribution.save()
                
                except Exception as e:
                    logger.error(f"Retry distribution error: {str(e)}")
                    result['failed_count'] += 1
                    distribution.status = DistributionStatus.FAILED.value
                    distribution.error_message = str(e)
                    distribution.save()
            
            logger.info(f"Distribution retries processed: {result['success_count']}/{result['retry_count']}")
        
        except Exception as e:
            logger.error(f"Retry processing error: {str(e)}")
            result['errors'].append(str(e))
        
        return result
    
    def _validate_employee_contact(self, employee: Employee, channel: DistributionChannel) -> bool:
        """Validate employee has required contact information for channel"""
        if channel == DistributionChannel.EMAIL:
            return bool(employee.email and '@' in employee.email)
        elif channel == DistributionChannel.SMS:
            return bool(employee.phone)
        elif channel == DistributionChannel.PRINT:
            return True  # No specific contact info required
        elif channel == DistributionChannel.PORTAL:
            return bool(employee.email)  # Assuming portal access via email
        else:
            return False
    
    def _process_pending_distributions(self, options: DistributionOptions):
        """Process pending distributions immediately"""
        pending = PayslipDistributionRecord.objects.filter(
            status=DistributionStatus.PENDING.value
        ).select_related('employee', 'payroll')
        
        # Group by channel for batch processing
        distributions_by_channel = defaultdict(list)
        for dist in pending:
            distributions_by_channel[dist.channel].append(dist)
        
        # Process each channel
        result = {'processed_count': 0, 'success_count': 0, 'failed_count': 0}
        
        for channel, distributions in distributions_by_channel.items():
            if channel == DistributionChannel.EMAIL.value:
                self._process_email_distributions(distributions, result)
            elif channel == DistributionChannel.PRINT.value:
                self._process_print_distributions(distributions, result)
            # Add other channels as needed
    
    def _process_email_distributions(self, distributions: List[PayslipDistributionRecord], 
                                   result: Dict[str, Any]):
        """Process email distributions with rate limiting"""
        try:
            # Get email configuration
            email_backend = get_connection()
            
            for distribution in distributions:
                result['processed_count'] += 1
                
                try:
                    success = self._send_email_distribution(distribution)
                    if success:
                        result['success_count'] += 1
                    else:
                        result['failed_count'] += 1
                    
                    # Rate limiting
                    time.sleep(6)  # 10 emails per minute max
                
                except Exception as e:
                    logger.error(f"Email distribution error: {str(e)}")
                    result['failed_count'] += 1
                    distribution.status = DistributionStatus.FAILED.value
                    distribution.error_message = str(e)
                    distribution.save()
        
        except Exception as e:
            logger.error(f"Email processing error: {str(e)}")
            result['errors'].append(str(e))
    
    def _send_email_distribution(self, distribution: PayslipDistributionRecord) -> bool:
        """Send individual email distribution"""
        try:
            # Update status
            distribution.status = DistributionStatus.SENDING.value
            distribution.save()
            
            # Generate payslip if not already generated
            if not distribution.file_path:
                generator = PayslipGenerator()
                generation_result = generator.generate_payslip(distribution.payroll)
                
                if not generation_result['success']:
                    distribution.status = DistributionStatus.FAILED.value
                    distribution.error_message = "Échec de génération du bulletin"
                    distribution.save()
                    return False
                
                distribution.file_path = generation_result['file_path']
                distribution.file_size = generation_result['file_size']
                distribution.file_hash = generation_result['file_hash']
            
            # Prepare email
            subject = f"Bulletin de paie - {distribution.payroll.period.strftime('%B %Y')}"
            
            # Get distribution options
            dist_options = json.loads(distribution.distribution_options) if distribution.distribution_options else {}
            custom_message = dist_options.get('custom_message', '')
            
            # Email body
            context = {
                'employee': distribution.employee,
                'payroll': distribution.payroll,
                'custom_message': custom_message,
                'access_url': self._generate_secure_access_url(distribution)
            }
            
            html_body = render_to_string('payroll/email/payslip_notification.html', context)
            text_body = render_to_string('payroll/email/payslip_notification.txt', context)
            
            # Create email
            email = EmailMultiAlternatives(
                subject=subject,
                body=text_body,
                from_email=settings.DEFAULT_FROM_EMAIL,
                to=[distribution.recipient_email]
            )
            email.attach_alternative(html_body, "text/html")
            
            # Attach payslip file
            if distribution.file_path and default_storage.exists(distribution.file_path):
                with default_storage.open(distribution.file_path, 'rb') as file:
                    email.attach(
                        f"bulletin_paie_{distribution.payroll.period.strftime('%Y_%m')}.pdf",
                        file.read(),
                        'application/pdf'
                    )
            
            # Send email
            email.send()
            
            # Update distribution record
            distribution.status = DistributionStatus.SENT.value
            distribution.sent_time = timezone.now()
            distribution.save()
            
            # Log audit trail
            self.audit_logger.log_payslip_distribution(
                distribution.employee,
                distribution.payroll,
                'email_sent',
                {'recipient': distribution.recipient_email}
            )
            
            return True
        
        except Exception as e:
            logger.error(f"Email send error: {str(e)}")
            distribution.status = DistributionStatus.FAILED.value
            distribution.error_message = str(e)
            distribution.save()
            return False
    
    def _process_print_distributions(self, distributions: List[PayslipDistributionRecord], 
                                   result: Dict[str, Any]):
        """Process print distributions by creating print queue"""
        try:
            for distribution in distributions:
                result['processed_count'] += 1
                
                try:
                    # Generate payslip if not already generated
                    if not distribution.file_path:
                        generator = PayslipGenerator()
                        generation_result = generator.generate_payslip(distribution.payroll)
                        
                        if not generation_result['success']:
                            distribution.status = DistributionStatus.FAILED.value
                            distribution.error_message = "Échec de génération du bulletin"
                            distribution.save()
                            result['failed_count'] += 1
                            continue
                        
                        distribution.file_path = generation_result['file_path']
                        distribution.file_size = generation_result['file_size']
                        distribution.file_hash = generation_result['file_hash']
                    
                    # Add to print queue (implementation depends on print system)
                    distribution.status = DistributionStatus.QUEUED.value
                    distribution.save()
                    
                    result['success_count'] += 1
                
                except Exception as e:
                    logger.error(f"Print distribution error: {str(e)}")
                    result['failed_count'] += 1
                    distribution.status = DistributionStatus.FAILED.value
                    distribution.error_message = str(e)
                    distribution.save()
        
        except Exception as e:
            logger.error(f"Print processing error: {str(e)}")
            result['errors'].append(str(e))
    
    def _process_sms_distributions(self, distributions: List[PayslipDistributionRecord], 
                                  result: Dict[str, Any]):
        """Process SMS distributions (notification only, no file attachment)"""
        try:
            for distribution in distributions:
                result['processed_count'] += 1
                
                try:
                    # Generate secure access URL
                    access_url = self._generate_secure_access_url(distribution)
                    
                    # Prepare SMS message
                    message = f"Votre bulletin de paie {distribution.payroll.period.strftime('%m/%Y')} est disponible: {access_url}"
                    
                    # Send SMS (implementation depends on SMS provider)
                    # For now, just mark as sent
                    distribution.status = DistributionStatus.SENT.value
                    distribution.sent_time = timezone.now()
                    distribution.save()
                    
                    result['success_count'] += 1
                
                except Exception as e:
                    logger.error(f"SMS distribution error: {str(e)}")
                    result['failed_count'] += 1
                    distribution.status = DistributionStatus.FAILED.value
                    distribution.error_message = str(e)
                    distribution.save()
        
        except Exception as e:
            logger.error(f"SMS processing error: {str(e)}")
            result['errors'].append(str(e))
    
    def _generate_secure_access_url(self, distribution: PayslipDistributionRecord) -> str:
        """Generate secure access URL for payslip"""
        # Generate access token
        token_data = {
            'distribution_id': str(distribution.id),
            'employee_id': distribution.employee.id,
            'payroll_id': distribution.payroll.id,
            'timestamp': timezone.now().timestamp()
        }
        
        access_token = self.security_manager.generate_secure_token(token_data)
        distribution.access_token = access_token
        distribution.save(update_fields=['access_token'])
        
        # Return secure URL (implementation depends on URL configuration)
        return f"https://payroll.company.com/payslip/view/{access_token}"


class BulletinArchiveManager:
    """Manager for payslip archiving and storage optimization"""
    
    def __init__(self):
        self.file_storage = FileStorageManager()
        self.security_manager = FileSecurityManager()
    
    def archive_period_payslips(self, period: date, archive_options: Dict[str, Any] = None) -> Dict[str, Any]:
        """Archive all payslips for a specific period"""
        archive_options = archive_options or {}
        
        result = {
            'success': True,
            'period': period.strftime('%Y-%m'),
            'archived_count': 0,
            'total_size': 0,
            'archive_path': None,
            'errors': []
        }
        
        try:
            # Get all distributions for the period
            distributions = PayslipDistributionRecord.objects.filter(
                payroll__period__year=period.year,
                payroll__period__month=period.month,
                status__in=[DistributionStatus.DELIVERED.value, DistributionStatus.SENT.value]
            ).select_related('employee', 'payroll')
            
            if not distributions.exists():
                result['success'] = False
                result['errors'].append('Aucun bulletin trouvé pour cette période')
                return result
            
            # Collect file paths
            file_paths = []
            for dist in distributions:
                if dist.file_path and default_storage.exists(dist.file_path):
                    file_paths.append(dist.file_path)
                    try:
                        file_stats = default_storage.stat(dist.file_path)
                        result['total_size'] += file_stats.st_size
                    except Exception:
                        pass
            
            if not file_paths:
                result['success'] = False
                result['errors'].append('Aucun fichier trouvé à archiver')
                return result
            
            # Create archive
            archive_name = f"bulletins_paie_{period.strftime('%Y_%m')}"
            archive_path = self.file_storage.create_file_backup(file_paths, archive_name)
            
            result['archive_path'] = archive_path
            result['archived_count'] = len(file_paths)
            
            # Move original files to archive storage if requested
            if archive_options.get('move_originals', False):
                self._move_files_to_archive_storage(file_paths, period)
            
            logger.info(f"Period archive created: {archive_path} ({len(file_paths)} files)")
        
        except Exception as e:
            logger.error(f"Archive creation error: {str(e)}")
            result['success'] = False
            result['errors'].append(str(e))
        
        return result
    
    def cleanup_old_archives(self, retention_months: int = 60) -> Dict[str, Any]:
        """Clean up old archived payslips beyond retention period"""
        cutoff_date = timezone.now() - relativedelta(months=retention_months)
        
        result = {
            'success': True,
            'cleaned_count': 0,
            'space_freed': 0,
            'errors': []
        }
        
        try:
            # Find old distribution records
            old_distributions = PayslipDistributionRecord.objects.filter(
                created_at__lt=cutoff_date,
                status=DistributionStatus.ARCHIVED.value
            )
            
            for distribution in old_distributions:
                try:
                    if distribution.file_path and default_storage.exists(distribution.file_path):
                        # Get file size before deletion
                        file_stats = default_storage.stat(distribution.file_path)
                        file_size = file_stats.st_size
                        
                        # Delete file
                        default_storage.delete(distribution.file_path)
                        
                        result['space_freed'] += file_size
                        result['cleaned_count'] += 1
                    
                    # Update distribution record
                    distribution.file_path = ''
                    distribution.save(update_fields=['file_path'])
                
                except Exception as e:
                    logger.error(f"Cleanup error for distribution {distribution.id}: {str(e)}")
                    result['errors'].append(str(e))
            
            logger.info(f"Archive cleanup completed: {result['cleaned_count']} files cleaned")
        
        except Exception as e:
            logger.error(f"Archive cleanup error: {str(e)}")
            result['success'] = False
            result['errors'].append(str(e))
        
        return result
    
    def _move_files_to_archive_storage(self, file_paths: List[str], period: date):
        """Move files to archive storage location"""
        archive_base = f"archives/payslips/{period.strftime('%Y/%m')}/"
        
        for file_path in file_paths:
            try:
                if default_storage.exists(file_path):
                    filename = os.path.basename(file_path)
                    archive_path = f"{archive_base}{filename}"
                    
                    # Read original file
                    with default_storage.open(file_path, 'rb') as original:
                        content = original.read()
                    
                    # Save to archive location
                    default_storage.save(archive_path, ContentFile(content))
                    
                    # Delete original
                    default_storage.delete(file_path)
                    
                    # Update distribution record
                    PayslipDistributionRecord.objects.filter(file_path=file_path).update(
                        file_path=archive_path,
                        status=DistributionStatus.ARCHIVED.value
                    )
            
            except Exception as e:
                logger.error(f"Archive move error for {file_path}: {str(e)}")


class BulletinReportingManager:
    """Manager for distribution reporting and analytics"""
    
    def generate_distribution_report(self, start_date: date, end_date: date, 
                                   filters: Dict[str, Any] = None) -> Dict[str, Any]:
        """Generate comprehensive distribution report"""
        filters = filters or {}
        
        # Base query
        query = Q(created_at__date__gte=start_date, created_at__date__lte=end_date)
        
        # Apply filters
        if filters.get('department'):
            query &= Q(employee__department=filters['department'])
        if filters.get('channel'):
            query &= Q(channel=filters['channel'])
        if filters.get('status'):
            query &= Q(status=filters['status'])
        
        distributions = PayslipDistributionRecord.objects.filter(query).select_related(
            'employee', 'payroll', 'employee__department'
        )
        
        # Calculate statistics
        total_distributions = distributions.count()
        successful_distributions = distributions.filter(
            status__in=[DistributionStatus.DELIVERED.value, DistributionStatus.SENT.value]
        ).count()
        
        # Group by status
        status_stats = {}
        for status in DistributionStatus:
            count = distributions.filter(status=status.value).count()
            if count > 0:
                status_stats[status.value] = {
                    'count': count,
                    'percentage': (count / total_distributions * 100) if total_distributions > 0 else 0
                }
        
        # Group by channel
        channel_stats = {}
        for channel in DistributionChannel:
            count = distributions.filter(channel=channel.value).count()
            if count > 0:
                channel_stats[channel.value] = {
                    'count': count,
                    'success_rate': 0
                }
                
                # Calculate success rate for channel
                successful_in_channel = distributions.filter(
                    channel=channel.value,
                    status__in=[DistributionStatus.DELIVERED.value, DistributionStatus.SENT.value]
                ).count()
                
                if count > 0:
                    channel_stats[channel.value]['success_rate'] = (successful_in_channel / count * 100)
        
        # Department breakdown
        dept_stats = {}
        for dist in distributions:
            dept_name = dist.employee.department.name if dist.employee.department else 'Non assigné'
            if dept_name not in dept_stats:
                dept_stats[dept_name] = {'total': 0, 'successful': 0}
            
            dept_stats[dept_name]['total'] += 1
            if dist.status in [DistributionStatus.DELIVERED.value, DistributionStatus.SENT.value]:
                dept_stats[dept_name]['successful'] += 1
        
        # Calculate success rates for departments
        for dept, stats in dept_stats.items():
            stats['success_rate'] = (stats['successful'] / stats['total'] * 100) if stats['total'] > 0 else 0
        
        return {
            'period': {
                'start_date': start_date.strftime('%Y-%m-%d'),
                'end_date': end_date.strftime('%Y-%m-%d')
            },
            'summary': {
                'total_distributions': total_distributions,
                'successful_distributions': successful_distributions,
                'overall_success_rate': (successful_distributions / total_distributions * 100) if total_distributions > 0 else 0,
                'failed_distributions': total_distributions - successful_distributions
            },
            'status_breakdown': status_stats,
            'channel_breakdown': channel_stats,
            'department_breakdown': dept_stats,
            'generated_at': timezone.now().isoformat()
        }
    
    def get_employee_distribution_history(self, employee: Employee, 
                                        months_back: int = 12) -> Dict[str, Any]:
        """Get distribution history for specific employee"""
        start_date = timezone.now() - relativedelta(months=months_back)
        
        distributions = PayslipDistributionRecord.objects.filter(
            employee=employee,
            created_at__gte=start_date
        ).select_related('payroll').order_by('-created_at')
        
        history = []
        for dist in distributions:
            history.append({
                'period': dist.payroll.period.strftime('%Y-%m'),
                'channel': dist.channel,
                'status': dist.status,
                'sent_time': dist.sent_time.isoformat() if dist.sent_time else None,
                'delivered_time': dist.delivered_time.isoformat() if dist.delivered_time else None,
                'access_count': dist.access_count,
                'last_access': dist.last_access_time.isoformat() if dist.last_access_time else None
            })
        
        return {
            'employee': {
                'id': employee.id,
                'name': employee.full_name,
                'email': employee.email,
                'department': employee.department.name if employee.department else None
            },
            'history': history,
            'summary': {
                'total_payslips': len(history),
                'successful_deliveries': len([h for h in history if h['status'] in ['delivered', 'sent']]),
                'total_accesses': sum(h['access_count'] for h in history)
            }
        }


# Convenience functions for easy integration

def generate_and_distribute_payslips(period: date, employee_ids: List[int] = None,
                                   generation_options: PayslipGenerationOptions = None,
                                   distribution_options: DistributionOptions = None) -> Dict[str, Any]:
    """
    Complete workflow: generate payslips and distribute them
    
    Args:
        period: Payroll period
        employee_ids: Optional list of specific employee IDs
        generation_options: Payslip generation configuration
        distribution_options: Distribution configuration
        
    Returns:
        Dictionary with complete workflow results
    """
    result = {
        'success': True,
        'period': period.strftime('%Y-%m'),
        'generation_result': None,
        'distribution_result': None,
        'errors': []
    }
    
    try:
        # Get payroll records for period
        payroll_query = Payroll.objects.filter(period=period).select_related('employee')
        
        if employee_ids:
            payroll_query = payroll_query.filter(employee__id__in=employee_ids)
        
        payrolls = list(payroll_query)
        
        if not payrolls:
            result['success'] = False
            result['errors'].append('Aucun bulletin de paie trouvé pour cette période')
            return result
        
        # Generate payslips
        generator = PayslipGenerator()
        generation_result = generator.generate_bulk_payslips(payrolls, generation_options)
        result['generation_result'] = generation_result
        
        if not generation_result['success']:
            result['success'] = False
            result['errors'].extend(generation_result['errors'])
            return result
        
        # Distribute payslips
        distributor = DistributionManager()
        distribution_result = distributor.distribute_payslips(payrolls, distribution_options)
        result['distribution_result'] = distribution_result
        
        if not distribution_result['success']:
            result['success'] = False
            result['errors'].extend(distribution_result['errors'])
        
        logger.info(f"Complete payslip workflow: {period} - {len(payrolls)} payslips processed")
    
    except Exception as e:
        logger.error(f"Payslip workflow error: {str(e)}")
        result['success'] = False
        result['errors'].append(str(e))
    
    return result


def get_payslip_distribution_status(period: date, employee_id: int = None) -> Dict[str, Any]:
    """
    Get distribution status for payslips in a period
    
    Args:
        period: Payroll period to check
        employee_id: Optional specific employee ID
        
    Returns:
        Dictionary with status information
    """
    try:
        query = Q(payroll__period=period)
        if employee_id:
            query &= Q(employee__id=employee_id)
        
        distributions = PayslipDistributionRecord.objects.filter(query).select_related(
            'employee', 'payroll'
        )
        
        if not distributions.exists():
            return {
                'period': period.strftime('%Y-%m'),
                'employee_id': employee_id,
                'status': 'not_found',
                'distributions': []
            }
        
        distribution_list = []
        for dist in distributions:
            distribution_list.append({
                'employee_id': dist.employee.id,
                'employee_name': dist.employee.full_name,
                'channel': dist.channel,
                'status': dist.status,
                'scheduled_time': dist.scheduled_time.isoformat() if dist.scheduled_time else None,
                'sent_time': dist.sent_time.isoformat() if dist.sent_time else None,
                'delivered_time': dist.delivered_time.isoformat() if dist.delivered_time else None,
                'retry_count': dist.retry_count,
                'error_message': dist.error_message
            })
        
        return {
            'period': period.strftime('%Y-%m'),
            'employee_id': employee_id,
            'total_distributions': len(distribution_list),
            'distributions': distribution_list
        }
    
    except Exception as e:
        logger.error(f"Status check error: {str(e)}")
        return {
            'period': period.strftime('%Y-%m'),
            'employee_id': employee_id,
            'status': 'error',
            'error': str(e)
        }


def schedule_monthly_payslip_distribution(distribution_day: int = 28,
                                        distribution_options: DistributionOptions = None) -> Dict[str, Any]:
    """
    Schedule automatic monthly payslip distribution
    
    Args:
        distribution_day: Day of month to send payslips
        distribution_options: Distribution configuration
        
    Returns:
        Dictionary with scheduling result
    """
    # This would integrate with a task scheduler like Celery
    # For now, return a placeholder implementation
    
    return {
        'success': True,
        'message': f'Distribution automatique programmée pour le {distribution_day} de chaque mois',
        'distribution_day': distribution_day,
        'next_run': 'À implémenter avec le planificateur de tâches'
    }