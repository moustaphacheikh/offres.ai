# validators.py
"""
Comprehensive Data Validation Module for Mauritanian Payroll System

This module provides extensive validation capabilities focusing on:
1. Employee Data Validation - Personal info, employment data, Mauritanian formats
2. Payroll Data Validation - Elements, amounts, calculations, periods  
3. Business Rule Validation - Age, seniority, leave, overtime compliance
4. Data Sanitization - Text, phone, email, currency normalization
5. Batch Validation - Bulk processing, cross-record checks

Integrates seamlessly with enhanced utilities (text_utils, date_utils, business_rules)
and implements Mauritanian-specific rules (NNI, phone formats, local compliance).
"""

from decimal import Decimal, InvalidOperation, ROUND_HALF_UP
from datetime import datetime, date, timedelta
from dateutil.relativedelta import relativedelta
from typing import Dict, List, Optional, Union, Tuple, Set
# Cleaned up imports - removed unused Any
import re
import unicodedata
import email.utils
import logging

# Import enhanced utilities
from .text_utils import (
    ValidationUtils, TextFormatter, ArabicTextUtils,
    LocalizationUtils, validate_mauritanian_identifier
)
from .date_utils import DateCalculator, SeniorityCalculator, LeaveCalculator
from .business_rules import PayrollBusinessRules

logger = logging.getLogger(__name__)


class ValidationError(Exception):
    """Custom exception for validation errors"""
    def __init__(self, message: str, field: str = None, error_code: str = None):
        self.message = message
        self.field = field
        self.error_code = error_code
        super().__init__(self.message)


class ValidationResult:
    """Container for validation results with detailed feedback"""
    
    def __init__(self, is_valid: bool = True, errors: List[Dict] = None, 
                 warnings: List[Dict] = None, cleaned_data: Dict = None):
        self.is_valid = is_valid
        self.errors = errors or []
        self.warnings = warnings or []
        self.cleaned_data = cleaned_data or {}
        
    def add_error(self, field: str, message: str, error_code: str = None, value: Any = None):
        """Add validation error"""
        self.is_valid = False
        self.errors.append({
            'field': field,
            'message': message,
            'error_code': error_code,
            'value': value
        })
        
    def add_warning(self, field: str, message: str, warning_code: str = None, value: Any = None):
        """Add validation warning"""
        self.warnings.append({
            'field': field,
            'message': message,
            'warning_code': warning_code,
            'value': value
        })
        
    def get_error_summary(self, locale: str = "FR") -> str:
        """Get formatted error summary"""
        if not self.errors:
            return ""
            
        if locale == "AR":
            header = f"وجدت {len(self.errors)} أخطاء في التحقق:"
        else:
            header = f"Trouvé {len(self.errors)} erreur(s) de validation:"
            
        error_lines = [header]
        for error in self.errors:
            field_name = error.get('field', 'Unknown')
            message = error.get('message', 'Erreur inconnue')
            error_lines.append(f"- {field_name}: {message}")
            
        return "\n".join(error_lines)


class EmployeeDataValidator:
    """Comprehensive employee data validation with Mauritanian specifics"""
    
    # Age limits for employment in Mauritania
    MIN_EMPLOYMENT_AGE = 16
    MAX_EMPLOYMENT_AGE = 65
    RETIREMENT_AGE = 60
    
    # Valid gender values
    VALID_GENDERS = {'M', 'F', 'MASCULIN', 'FEMININ', 'MALE', 'FEMALE'}
    
    # Valid marital status values  
    VALID_MARITAL_STATUS = {
        'CELIBATAIRE', 'MARIE', 'DIVORCE', 'VEUF', 'SINGLE', 'MARRIED', 'DIVORCED', 'WIDOWED'
    }
    
    # Valid contract types
    VALID_CONTRACT_TYPES = {
        'CDI', 'CDD', 'STAGE', 'CONSULTANT', 'TEMPORAIRE', 'PERMANENT', 'TEMPORARY'
    }
    
    @staticmethod
    def validate_personal_info(data: Dict[str, Any], locale: str = "FR") -> ValidationResult:
        """
        Validate personal information with Mauritanian standards
        
        Args:
            data: Dictionary containing personal information
            locale: Locale for error messages
            
        Returns:
            ValidationResult with validation outcome
        """
        result = ValidationResult()
        
        # Validate required fields
        required_fields = ['last_name', 'first_name']
        for field in required_fields:
            if not data.get(field):
                result.add_error(
                    field, 
                    LocalizationUtils.get_error_message('required_field', locale),
                    'REQUIRED_FIELD'
                )
        
        # Validate names
        if data.get('last_name'):
            cleaned_last_name = EmployeeDataValidator._validate_name_field(
                data['last_name'], 'last_name', result, locale
            )
            if cleaned_last_name:
                result.cleaned_data['last_name'] = cleaned_last_name
                
        if data.get('first_name'):
            cleaned_first_name = EmployeeDataValidator._validate_name_field(
                data['first_name'], 'first_name', result, locale
            )
            if cleaned_first_name:
                result.cleaned_data['first_name'] = cleaned_first_name
        
        # Validate National ID (NNI)
        if data.get('national_id'):
            nni_valid, nni_error = validate_mauritanian_identifier(
                data['national_id'], 'nni'
            )
            if not nni_valid:
                result.add_error('national_id', nni_error, 'INVALID_NNI')
            else:
                result.cleaned_data['national_id'] = ValidationUtils.format_nni(
                    data['national_id']
                )
        
        # Validate birth date and age
        if data.get('birth_date'):
            birth_date_result = EmployeeDataValidator._validate_birth_date(
                data['birth_date'], locale
            )
            if birth_date_result['valid']:
                result.cleaned_data['birth_date'] = birth_date_result['date']
                
                # Age-related warnings
                age = birth_date_result['age']
                if age < EmployeeDataValidator.MIN_EMPLOYMENT_AGE:
                    result.add_error(
                        'birth_date',
                        f"Âge minimum requis: {EmployeeDataValidator.MIN_EMPLOYMENT_AGE} ans" 
                        if locale == "FR" else f"الحد الأدنى للعمر: {EmployeeDataValidator.MIN_EMPLOYMENT_AGE} سنة",
                        'AGE_TOO_YOUNG'
                    )
                elif age > EmployeeDataValidator.MAX_EMPLOYMENT_AGE:
                    result.add_warning(
                        'birth_date',
                        f"Âge supérieur à la limite habituelle: {EmployeeDataValidator.MAX_EMPLOYMENT_AGE} ans"
                        if locale == "FR" else f"العمر يتجاوز الحد المعتاد: {EmployeeDataValidator.MAX_EMPLOYMENT_AGE} سنة",
                        'AGE_OVER_LIMIT'
                    )
                elif age >= EmployeeDataValidator.RETIREMENT_AGE:
                    result.add_warning(
                        'birth_date',
                        f"Âge de retraite atteint ({EmployeeDataValidator.RETIREMENT_AGE} ans)"
                        if locale == "FR" else f"وصل إلى سن التقاعد ({EmployeeDataValidator.RETIREMENT_AGE} سنة)",
                        'RETIREMENT_AGE'
                    )
            else:
                result.add_error('birth_date', birth_date_result['error'], 'INVALID_DATE')
        
        # Validate gender
        if data.get('gender'):
            gender_clean = data['gender'].upper().strip()
            if gender_clean not in EmployeeDataValidator.VALID_GENDERS:
                result.add_error(
                    'gender',
                    "Genre invalide" if locale == "FR" else "الجنس غير صحيح",
                    'INVALID_GENDER'
                )
            else:
                result.cleaned_data['gender'] = gender_clean
        
        # Validate marital status
        if data.get('marital_status'):
            marital_clean = data['marital_status'].upper().strip()
            if marital_clean not in EmployeeDataValidator.VALID_MARITAL_STATUS:
                result.add_error(
                    'marital_status',
                    "Situation familiale invalide" if locale == "FR" else "الحالة الاجتماعية غير صحيحة",
                    'INVALID_MARITAL_STATUS'
                )
            else:
                result.cleaned_data['marital_status'] = marital_clean
        
        # Validate children count
        if data.get('children_count') is not None:
            try:
                children = int(data['children_count'])
                if children < 0:
                    result.add_error(
                        'children_count',
                        "Nombre d'enfants ne peut être négatif" if locale == "FR" else "عدد الأطفال لا يمكن أن يكون سالباً",
                        'NEGATIVE_CHILDREN'
                    )
                elif children > 20:  # Reasonable upper limit
                    result.add_warning(
                        'children_count',
                        f"Nombre d'enfants élevé: {children}" if locale == "FR" else f"عدد كبير من الأطفال: {children}",
                        'HIGH_CHILDREN_COUNT'
                    )
                else:
                    result.cleaned_data['children_count'] = children
            except (ValueError, TypeError):
                result.add_error(
                    'children_count',
                    "Nombre d'enfants invalide" if locale == "FR" else "عدد الأطفال غير صحيح",
                    'INVALID_CHILDREN_COUNT'
                )
        
        # Validate phone
        if data.get('phone'):
            phone_valid, phone_error = validate_mauritanian_identifier(
                data['phone'], 'phone'
            )
            if not phone_valid:
                result.add_error('phone', phone_error, 'INVALID_PHONE')
            else:
                result.cleaned_data['phone'] = ValidationUtils.format_phone(
                    data['phone'], include_country_code=True
                )
                
                # Add operator info as warning/info
                operator = ValidationUtils.get_phone_operator(data['phone'])
                if operator:
                    result.add_warning(
                        'phone',
                        f"Opérateur détecté: {operator}" if locale == "FR" else f"الشبكة المكتشفة: {operator}",
                        'PHONE_OPERATOR_INFO'
                    )
        
        # Validate email
        if data.get('email'):
            email_result = EmployeeDataValidator._validate_email(data['email'])
            if email_result['valid']:
                result.cleaned_data['email'] = email_result['email']
            else:
                result.add_error('email', email_result['error'], 'INVALID_EMAIL')
        
        # Validate address
        if data.get('address'):
            cleaned_address = TextFormatter.clean_string(data['address'], preserve_case=True)
            if len(cleaned_address) > 500:
                result.add_warning(
                    'address',
                    "Adresse très longue, sera tronquée" if locale == "FR" else "العنوان طويل جداً، سيتم اختصاره",
                    'LONG_ADDRESS'
                )
                cleaned_address = cleaned_address[:500]
            result.cleaned_data['address'] = cleaned_address
        
        return result
    
    @staticmethod
    def validate_employment_info(data: Dict[str, Any], locale: str = "FR") -> ValidationResult:
        """
        Validate employment information and relationships
        
        Args:
            data: Dictionary containing employment information
            locale: Locale for error messages
            
        Returns:
            ValidationResult with validation outcome
        """
        result = ValidationResult()
        
        # Validate hire date
        if data.get('hire_date'):
            hire_date_result = EmployeeDataValidator._validate_employment_date(
                data['hire_date'], 'hire_date', locale
            )
            if hire_date_result['valid']:
                result.cleaned_data['hire_date'] = hire_date_result['date']
            else:
                result.add_error('hire_date', hire_date_result['error'], 'INVALID_HIRE_DATE')
        
        # Validate termination date (if provided)
        if data.get('termination_date'):
            term_date_result = EmployeeDataValidator._validate_employment_date(
                data['termination_date'], 'termination_date', locale
            )
            if term_date_result['valid']:
                result.cleaned_data['termination_date'] = term_date_result['date']
                
                # Check termination is after hire date
                if data.get('hire_date') and result.cleaned_data.get('hire_date'):
                    if term_date_result['date'] <= result.cleaned_data['hire_date']:
                        result.add_error(
                            'termination_date',
                            "Date de fin doit être après la date d'embauche" 
                            if locale == "FR" else "تاريخ الانتهاء يجب أن يكون بعد تاريخ التوظيف",
                            'INVALID_TERMINATION_SEQUENCE'
                        )
            else:
                result.add_error('termination_date', term_date_result['error'], 'INVALID_TERMINATION_DATE')
        
        # Validate seniority date
        if data.get('seniority_date'):
            seniority_result = EmployeeDataValidator._validate_employment_date(
                data['seniority_date'], 'seniority_date', locale
            )
            if seniority_result['valid']:
                result.cleaned_data['seniority_date'] = seniority_result['date']
                
                # Seniority date should not be after hire date
                if data.get('hire_date') and result.cleaned_data.get('hire_date'):
                    if seniority_result['date'] > result.cleaned_data['hire_date']:
                        result.add_warning(
                            'seniority_date',
                            "Date d'ancienneté après la date d'embauche" 
                            if locale == "FR" else "تاريخ الأقدمية بعد تاريخ التوظيف",
                            'SENIORITY_AFTER_HIRE'
                        )
            else:
                result.add_error('seniority_date', seniority_result['error'], 'INVALID_SENIORITY_DATE')
        
        # Validate contract type
        if data.get('contract_type'):
            contract_clean = data['contract_type'].upper().strip()
            if contract_clean not in EmployeeDataValidator.VALID_CONTRACT_TYPES:
                result.add_error(
                    'contract_type',
                    "Type de contrat invalide" if locale == "FR" else "نوع العقد غير صحيح",
                    'INVALID_CONTRACT_TYPE'
                )
            else:
                result.cleaned_data['contract_type'] = contract_clean
                
                # For CDD, contract end date should be provided
                if contract_clean == 'CDD' and not data.get('contract_end_date'):
                    result.add_warning(
                        'contract_end_date',
                        "Date de fin de contrat recommandée pour CDD" 
                        if locale == "FR" else "تاريخ انتهاء العقد مطلوب للعقد محدد المدة",
                        'MISSING_CONTRACT_END'
                    )
        
        # Validate contract end date
        if data.get('contract_end_date'):
            contract_end_result = EmployeeDataValidator._validate_employment_date(
                data['contract_end_date'], 'contract_end_date', locale
            )
            if contract_end_result['valid']:
                result.cleaned_data['contract_end_date'] = contract_end_result['date']
                
                # Contract end should be after hire date
                if data.get('hire_date') and result.cleaned_data.get('hire_date'):
                    if contract_end_result['date'] <= result.cleaned_data['hire_date']:
                        result.add_error(
                            'contract_end_date',
                            "Date de fin de contrat doit être après l'embauche" 
                            if locale == "FR" else "تاريخ انتهاء العقد يجب أن يكون بعد التوظيف",
                            'INVALID_CONTRACT_END_SEQUENCE'
                        )
            else:
                result.add_error('contract_end_date', contract_end_result['error'], 'INVALID_CONTRACT_END_DATE')
        
        # Validate social security numbers
        if data.get('cnss_number'):
            cnss_clean = TextFormatter.clean_string(str(data['cnss_number']))
            if len(cnss_clean) < 5 or len(cnss_clean) > 20:
                result.add_error(
                    'cnss_number',
                    "Numéro CNSS invalide" if locale == "FR" else "رقم الضمان الاجتماعي غير صحيح",
                    'INVALID_CNSS'
                )
            else:
                result.cleaned_data['cnss_number'] = cnss_clean
        
        if data.get('cnam_number'):
            cnam_clean = TextFormatter.clean_string(str(data['cnam_number']))
            if len(cnam_clean) < 5 or len(cnam_clean) > 20:
                result.add_error(
                    'cnam_number',
                    "Numéro CNAM invalide" if locale == "FR" else "رقم التأمين الصحي غير صحيح",
                    'INVALID_CNAM'
                )
            else:
                result.cleaned_data['cnam_number'] = cnam_clean
        
        return result
    
    @staticmethod
    def _validate_name_field(name: str, field_name: str, result: ValidationResult, locale: str) -> Optional[str]:
        """Validate and clean name fields"""
        if not name or not name.strip():
            return None
            
        # Clean the name
        cleaned_name = TextFormatter.clean_string(name, preserve_case=True)
        
        # Length validation
        if len(cleaned_name) < 2:
            result.add_error(
                field_name,
                "Nom trop court (minimum 2 caractères)" if locale == "FR" else "الاسم قصير جداً (أقل حد حرفان)",
                'NAME_TOO_SHORT'
            )
            return None
        elif len(cleaned_name) > 100:
            result.add_warning(
                field_name,
                "Nom très long, sera tronqué" if locale == "FR" else "الاسم طويل جداً، سيتم اختصاره",
                'NAME_TOO_LONG'
            )
            cleaned_name = cleaned_name[:100]
        
        # Pattern validation - allow letters, spaces, hyphens, apostrophes, and Arabic characters
        if not re.match(r"^[\w\s\-'\.]+$", cleaned_name, re.UNICODE):
            result.add_warning(
                field_name,
                "Nom contient des caractères inhabituels" if locale == "FR" else "الاسم يحتوي على أحرف غير عادية",
                'UNUSUAL_CHARACTERS'
            )
        
        return cleaned_name
    
    @staticmethod
    def _validate_birth_date(birth_date: Union[str, date, datetime], locale: str) -> Dict[str, Any]:
        """Validate birth date and calculate age"""
        try:
            if isinstance(birth_date, str):
                # Try multiple date formats
                for fmt in ['%Y-%m-%d', '%d/%m/%Y', '%d-%m-%Y']:
                    try:
                        parsed_date = datetime.strptime(birth_date, fmt).date()
                        break
                    except ValueError:
                        continue
                else:
                    return {
                        'valid': False,
                        'error': LocalizationUtils.get_error_message('invalid_date', locale)
                    }
            elif isinstance(birth_date, datetime):
                parsed_date = birth_date.date()
            elif isinstance(birth_date, date):
                parsed_date = birth_date
            else:
                return {
                    'valid': False,
                    'error': LocalizationUtils.get_error_message('invalid_date', locale)
                }
            
            # Check reasonable date range
            today = date.today()
            min_birth_date = today - timedelta(days=365 * 100)  # 100 years ago
            max_birth_date = today - timedelta(days=365 * 14)   # 14 years ago
            
            if parsed_date < min_birth_date:
                return {
                    'valid': False,
                    'error': "Date de naissance trop ancienne" if locale == "FR" else "تاريخ الميلاد قديم جداً"
                }
            elif parsed_date > max_birth_date:
                return {
                    'valid': False,
                    'error': "Date de naissance trop récente" if locale == "FR" else "تاريخ الميلاد حديث جداً"
                }
            
            # Calculate age
            age = (today - parsed_date).days // 365
            
            return {
                'valid': True,
                'date': parsed_date,
                'age': age
            }
            
        except Exception as e:
            return {
                'valid': False,
                'error': f"Erreur de format de date: {str(e)}" if locale == "FR" else f"خطأ في تنسيق التاريخ: {str(e)}"
            }
    
    @staticmethod
    def _validate_employment_date(emp_date: Union[str, date, datetime], field_name: str, locale: str) -> Dict[str, Any]:
        """Validate employment-related dates"""
        try:
            if isinstance(emp_date, str):
                # Try multiple date formats
                for fmt in ['%Y-%m-%d', '%d/%m/%Y', '%d-%m-%Y']:
                    try:
                        parsed_date = datetime.strptime(emp_date, fmt).date()
                        break
                    except ValueError:
                        continue
                else:
                    return {
                        'valid': False,
                        'error': LocalizationUtils.get_error_message('invalid_date', locale)
                    }
            elif isinstance(emp_date, datetime):
                parsed_date = emp_date.date()
            elif isinstance(emp_date, date):
                parsed_date = emp_date
            else:
                return {
                    'valid': False,
                    'error': LocalizationUtils.get_error_message('invalid_date', locale)
                }
            
            # Check reasonable date range for employment
            today = date.today()
            min_emp_date = date(1960, 1, 1)  # Company inception
            max_emp_date = today + timedelta(days=365)  # Allow future dates for planning
            
            if parsed_date < min_emp_date:
                return {
                    'valid': False,
                    'error': f"Date trop ancienne pour {field_name}" if locale == "FR" else f"التاريخ قديم جداً لـ {field_name}"
                }
            elif parsed_date > max_emp_date:
                return {
                    'valid': False,
                    'error': f"Date trop future pour {field_name}" if locale == "FR" else f"التاريخ في المستقبل البعيد لـ {field_name}"
                }
            
            return {
                'valid': True,
                'date': parsed_date
            }
            
        except Exception as e:
            return {
                'valid': False,
                'error': f"Erreur de format de date: {str(e)}" if locale == "FR" else f"خطأ في تنسيق التاريخ: {str(e)}"
            }
    
    @staticmethod
    def _validate_email(email: str) -> Dict[str, Any]:
        """Validate email address format"""
        if not email or not email.strip():
            return {'valid': False, 'error': 'Email vide'}
        
        email_clean = email.strip().lower()
        
        # Basic format validation
        try:
            # Use email.utils for initial validation
            parsed = email.utils.parseaddr(email_clean)
            if not parsed[1] or '@' not in parsed[1]:
                return {'valid': False, 'error': 'Format email invalide'}
            
            # More strict validation
            email_pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
            if not re.match(email_pattern, parsed[1]):
                return {'valid': False, 'error': 'Format email invalide'}
            
            # Check length
            if len(parsed[1]) > 254:  # RFC 5321 limit
                return {'valid': False, 'error': 'Email trop long'}
            
            return {'valid': True, 'email': parsed[1]}
            
        except Exception:
            return {'valid': False, 'error': 'Format email invalide'}


class PayrollDataValidator:
    """Comprehensive payroll data validation with business rules"""
    
    # Payroll limits and constraints
    MIN_SALARY = Decimal('0.01')
    MAX_SALARY = Decimal('10000000.00')  # 10M MRU
    MIN_PERCENTAGE = Decimal('0.00')
    MAX_PERCENTAGE = Decimal('100.00')
    
    @staticmethod
    def validate_salary_data(data: Dict[str, Any], locale: str = "FR") -> ValidationResult:
        """
        Validate salary and compensation data
        
        Args:
            data: Dictionary containing salary information
            locale: Locale for error messages
            
        Returns:
            ValidationResult with validation outcome
        """
        result = ValidationResult()
        
        # Validate base salary
        if data.get('base_salary') is not None:
            salary_result = PayrollDataValidator._validate_amount_field(
                data['base_salary'], 'base_salary', PayrollDataValidator.MIN_SALARY, 
                PayrollDataValidator.MAX_SALARY, locale
            )
            if salary_result['valid']:
                result.cleaned_data['base_salary'] = salary_result['amount']
            else:
                result.add_error('base_salary', salary_result['error'], 'INVALID_SALARY')
        
        # Validate salary grade
        if data.get('salary_grade') is not None:
            try:
                grade = int(data['salary_grade'])
                if grade < 1 or grade > 20:  # Typical grade range
                    result.add_error(
                        'salary_grade',
                        "Échelon salarial invalide (1-20)" if locale == "FR" else "درجة الراتب غير صحيحة (1-20)",
                        'INVALID_GRADE'
                    )
                else:
                    result.cleaned_data['salary_grade'] = grade
            except (ValueError, TypeError):
                result.add_error(
                    'salary_grade',
                    "Échelon salarial invalide" if locale == "FR" else "درجة الراتب غير صحيحة",
                    'INVALID_GRADE_FORMAT'
                )
        
        # Validate salary step
        if data.get('salary_step') is not None:
            try:
                step = int(data['salary_step'])
                if step < 1 or step > 10:  # Typical step range
                    result.add_error(
                        'salary_step',
                        "Échelon invalide (1-10)" if locale == "FR" else "الدرجة غير صحيحة (1-10)",
                        'INVALID_STEP'
                    )
                else:
                    result.cleaned_data['salary_step'] = step
            except (ValueError, TypeError):
                result.add_error(
                    'salary_step',
                    "Échelon invalide" if locale == "FR" else "الدرجة غير صحيحة",
                    'INVALID_STEP_FORMAT'
                )
        
        # Validate allowances
        allowance_fields = ['transport_allowance', 'housing_allowance', 'family_allowance', 'other_allowance']
        for field in allowance_fields:
            if data.get(field) is not None:
                allowance_result = PayrollDataValidator._validate_amount_field(
                    data[field], field, Decimal('0.00'), PayrollDataValidator.MAX_SALARY, locale
                )
                if allowance_result['valid']:
                    result.cleaned_data[field] = allowance_result['amount']
                else:
                    result.add_error(field, allowance_result['error'], 'INVALID_ALLOWANCE')
        
        return result
    
    @staticmethod
    def validate_payroll_element(data: Dict[str, Any], locale: str = "FR") -> ValidationResult:
        """
        Validate individual payroll element (rubrique)
        
        Args:
            data: Dictionary containing payroll element data
            locale: Locale for error messages
            
        Returns:
            ValidationResult with validation outcome
        """
        result = ValidationResult()
        
        # Validate element code
        if not data.get('code'):
            result.add_error(
                'code',
                LocalizationUtils.get_error_message('required_field', locale),
                'REQUIRED_FIELD'
            )
        else:
            code = str(data['code']).strip().upper()
            if not re.match(r'^[A-Z0-9_]{2,20}$', code):
                result.add_error(
                    'code',
                    "Code rubrique invalide (lettres, chiffres, _ seulement)" 
                    if locale == "FR" else "رمز العنصر غير صحيح (أحرف وأرقام و _ فقط)",
                    'INVALID_CODE_FORMAT'
                )
            else:
                result.cleaned_data['code'] = code
        
        # Validate element name
        if not data.get('name'):
            result.add_error(
                'name',
                LocalizationUtils.get_error_message('required_field', locale),
                'REQUIRED_FIELD'
            )
        else:
            name = TextFormatter.clean_string(data['name'])
            if len(name) < 3:
                result.add_error(
                    'name',
                    "Nom trop court (minimum 3 caractères)" if locale == "FR" else "الاسم قصير جداً (أقل حد 3 أحرف)",
                    'NAME_TOO_SHORT'
                )
            elif len(name) > 100:
                result.add_warning(
                    'name',
                    "Nom très long, sera tronqué" if locale == "FR" else "الاسم طويل جداً، سيتم اختصاره",
                    'NAME_TOO_LONG'
                )
                name = name[:100]
            result.cleaned_data['name'] = name
        
        # Validate element type
        valid_types = {'GAIN', 'RETENUE', 'COTISATION', 'INFORMATION'}
        if data.get('element_type'):
            element_type = data['element_type'].upper().strip()
            if element_type not in valid_types:
                result.add_error(
                    'element_type',
                    f"Type invalide. Valeurs autorisées: {', '.join(valid_types)}"
                    if locale == "FR" else f"النوع غير صحيح. القيم المسموحة: {', '.join(valid_types)}",
                    'INVALID_ELEMENT_TYPE'
                )
            else:
                result.cleaned_data['element_type'] = element_type
        
        # Validate calculation method
        valid_calc_methods = {'FIXED', 'PERCENTAGE', 'FORMULA', 'COMPUTED'}
        if data.get('calculation_method'):
            calc_method = data['calculation_method'].upper().strip()
            if calc_method not in valid_calc_methods:
                result.add_error(
                    'calculation_method',
                    f"Méthode de calcul invalide. Valeurs autorisées: {', '.join(valid_calc_methods)}"
                    if locale == "FR" else f"طريقة الحساب غير صحيحة. القيم المسموحة: {', '.join(valid_calc_methods)}",
                    'INVALID_CALCULATION_METHOD'
                )
            else:
                result.cleaned_data['calculation_method'] = calc_method
        
        # Validate amount/rate
        if data.get('amount') is not None:
            amount_result = PayrollDataValidator._validate_amount_field(
                data['amount'], 'amount', Decimal('-999999.99'), Decimal('999999.99'), locale
            )
            if amount_result['valid']:
                result.cleaned_data['amount'] = amount_result['amount']
            else:
                result.add_error('amount', amount_result['error'], 'INVALID_AMOUNT')
        
        if data.get('rate') is not None:
            rate_result = PayrollDataValidator._validate_percentage_field(
                data['rate'], 'rate', locale
            )
            if rate_result['valid']:
                result.cleaned_data['rate'] = rate_result['percentage']
            else:
                result.add_error('rate', rate_result['error'], 'INVALID_RATE')
        
        # Validate priority/order
        if data.get('calculation_order') is not None:
            try:
                order = int(data['calculation_order'])
                if order < 0 or order > 999:
                    result.add_error(
                        'calculation_order',
                        "Ordre de calcul invalide (0-999)" if locale == "FR" else "ترتيب الحساب غير صحيح (0-999)",
                        'INVALID_ORDER'
                    )
                else:
                    result.cleaned_data['calculation_order'] = order
            except (ValueError, TypeError):
                result.add_error(
                    'calculation_order',
                    "Ordre de calcul invalide" if locale == "FR" else "ترتيب الحساب غير صحيح",
                    'INVALID_ORDER_FORMAT'
                )
        
        return result
    
    @staticmethod
    def validate_payroll_period(data: Dict[str, Any], locale: str = "FR") -> ValidationResult:
        """
        Validate payroll period and related data
        
        Args:
            data: Dictionary containing period information
            locale: Locale for error messages
            
        Returns:
            ValidationResult with validation outcome
        """
        result = ValidationResult()
        
        # Validate period start and end dates
        if not data.get('period_start'):
            result.add_error(
                'period_start',
                LocalizationUtils.get_error_message('required_field', locale),
                'REQUIRED_FIELD'
            )
        else:
            start_result = PayrollDataValidator._validate_date_field(
                data['period_start'], 'period_start', locale
            )
            if start_result['valid']:
                result.cleaned_data['period_start'] = start_result['date']
            else:
                result.add_error('period_start', start_result['error'], 'INVALID_DATE')
        
        if not data.get('period_end'):
            result.add_error(
                'period_end',
                LocalizationUtils.get_error_message('required_field', locale),
                'REQUIRED_FIELD'
            )
        else:
            end_result = PayrollDataValidator._validate_date_field(
                data['period_end'], 'period_end', locale
            )
            if end_result['valid']:
                result.cleaned_data['period_end'] = end_result['date']
            else:
                result.add_error('period_end', end_result['error'], 'INVALID_DATE')
        
        # Validate period sequence
        if (result.cleaned_data.get('period_start') and 
            result.cleaned_data.get('period_end')):
            
            start_date = result.cleaned_data['period_start']
            end_date = result.cleaned_data['period_end']
            
            if end_date <= start_date:
                result.add_error(
                    'period_end',
                    "Date de fin doit être après la date de début" 
                    if locale == "FR" else "تاريخ الانتهاء يجب أن يكون بعد تاريخ البداية",
                    'INVALID_PERIOD_SEQUENCE'
                )
            
            # Check period length (typical payroll periods)
            period_days = (end_date - start_date).days
            if period_days < 1:
                result.add_error(
                    'period_end',
                    "Période trop courte" if locale == "FR" else "الفترة قصيرة جداً",
                    'PERIOD_TOO_SHORT'
                )
            elif period_days > 366:  # More than a year
                result.add_warning(
                    'period_end',
                    "Période très longue (plus d'un an)" if locale == "FR" else "فترة طويلة جداً (أكثر من سنة)",
                    'PERIOD_TOO_LONG'
                )
        
        # Validate year and month (if provided)
        if data.get('year'):
            try:
                year = int(data['year'])
                current_year = date.today().year
                if year < 2000 or year > current_year + 2:
                    result.add_error(
                        'year',
                        f"Année invalide ({2000}-{current_year + 2})" 
                        if locale == "FR" else f"السنة غير صحيحة ({2000}-{current_year + 2})",
                        'INVALID_YEAR'
                    )
                else:
                    result.cleaned_data['year'] = year
            except (ValueError, TypeError):
                result.add_error(
                    'year',
                    "Format d'année invalide" if locale == "FR" else "تنسيق السنة غير صحيح",
                    'INVALID_YEAR_FORMAT'
                )
        
        if data.get('month'):
            try:
                month = int(data['month'])
                if month < 1 or month > 12:
                    result.add_error(
                        'month',
                        "Mois invalide (1-12)" if locale == "FR" else "الشهر غير صحيح (1-12)",
                        'INVALID_MONTH'
                    )
                else:
                    result.cleaned_data['month'] = month
            except (ValueError, TypeError):
                result.add_error(
                    'month',
                    "Format de mois invalide" if locale == "FR" else "تنسيق الشهر غير صحيح",
                    'INVALID_MONTH_FORMAT'
                )
        
        return result
    
    @staticmethod
    def _validate_amount_field(amount: Any, field_name: str, min_amount: Decimal, 
                             max_amount: Decimal, locale: str) -> Dict[str, Any]:
        """Validate monetary amount field"""
        try:
            if amount is None or amount == '':
                return {'valid': True, 'amount': Decimal('0.00')}
            
            # Convert to Decimal
            if isinstance(amount, str):
                # Handle different decimal separators
                amount = amount.replace(',', '.').replace(' ', '')
            
            decimal_amount = Decimal(str(amount)).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
            
            # Range validation
            if decimal_amount < min_amount:
                return {
                    'valid': False,
                    'error': f"Montant trop faible (minimum: {min_amount})" 
                            if locale == "FR" else f"المبلغ صغير جداً (الحد الأدنى: {min_amount})"
                }
            elif decimal_amount > max_amount:
                return {
                    'valid': False,
                    'error': f"Montant trop élevé (maximum: {max_amount})" 
                            if locale == "FR" else f"المبلغ كبير جداً (الحد الأقصى: {max_amount})"
                }
            
            return {'valid': True, 'amount': decimal_amount}
            
        except (InvalidOperation, ValueError, TypeError) as e:
            return {
                'valid': False,
                'error': f"Format de montant invalide: {str(e)}" 
                        if locale == "FR" else f"تنسيق المبلغ غير صحيح: {str(e)}"
            }
    
    @staticmethod
    def _validate_percentage_field(percentage: Any, field_name: str, locale: str) -> Dict[str, Any]:
        """Validate percentage field"""
        try:
            if percentage is None or percentage == '':
                return {'valid': True, 'percentage': Decimal('0.00')}
            
            # Convert to Decimal
            if isinstance(percentage, str):
                percentage = percentage.replace(',', '.').replace(' ', '').replace('%', '')
            
            decimal_percentage = Decimal(str(percentage)).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
            
            # Range validation
            if decimal_percentage < PayrollDataValidator.MIN_PERCENTAGE:
                return {
                    'valid': False,
                    'error': f"Pourcentage trop faible (minimum: {PayrollDataValidator.MIN_PERCENTAGE}%)" 
                            if locale == "FR" else f"النسبة صغيرة جداً (الحد الأدنى: {PayrollDataValidator.MIN_PERCENTAGE}%)"
                }
            elif decimal_percentage > PayrollDataValidator.MAX_PERCENTAGE:
                return {
                    'valid': False,
                    'error': f"Pourcentage trop élevé (maximum: {PayrollDataValidator.MAX_PERCENTAGE}%)" 
                            if locale == "FR" else f"النسبة كبيرة جداً (الحد الأقصى: {PayrollDataValidator.MAX_PERCENTAGE}%)"
                }
            
            return {'valid': True, 'percentage': decimal_percentage}
            
        except (InvalidOperation, ValueError, TypeError) as e:
            return {
                'valid': False,
                'error': f"Format de pourcentage invalide: {str(e)}" 
                        if locale == "FR" else f"تنسيق النسبة غير صحيح: {str(e)}"
            }
    
    @staticmethod
    def _validate_date_field(date_value: Any, field_name: str, locale: str) -> Dict[str, Any]:
        """Validate date field"""
        try:
            if isinstance(date_value, str):
                # Try multiple date formats
                for fmt in ['%Y-%m-%d', '%d/%m/%Y', '%d-%m-%Y']:
                    try:
                        parsed_date = datetime.strptime(date_value, fmt).date()
                        break
                    except ValueError:
                        continue
                else:
                    return {
                        'valid': False,
                        'error': LocalizationUtils.get_error_message('invalid_date', locale)
                    }
            elif isinstance(date_value, datetime):
                parsed_date = date_value.date()
            elif isinstance(date_value, date):
                parsed_date = date_value
            else:
                return {
                    'valid': False,
                    'error': LocalizationUtils.get_error_message('invalid_date', locale)
                }
            
            return {'valid': True, 'date': parsed_date}
            
        except Exception as e:
            return {
                'valid': False,
                'error': f"Erreur de format de date: {str(e)}" 
                        if locale == "FR" else f"خطأ في تنسيق التاريخ: {str(e)}"
            }


class BusinessRuleValidator:
    """Validates business rules compliance for payroll operations"""
    
    def __init__(self, business_rules: PayrollBusinessRules = None):
        """Initialize with business rules engine"""
        self.business_rules = business_rules or PayrollBusinessRules()
    
    def validate_employment_eligibility(self, employee_data: Dict[str, Any], 
                                      locale: str = "FR") -> ValidationResult:
        """
        Validate employment eligibility based on business rules
        
        Args:
            employee_data: Employee information
            locale: Locale for error messages
            
        Returns:
            ValidationResult with compliance status
        """
        result = ValidationResult()
        
        # Age eligibility
        if employee_data.get('birth_date'):
            birth_date = employee_data['birth_date']
            if isinstance(birth_date, str):
                birth_date = datetime.strptime(birth_date, '%Y-%m-%d').date()
            
            age = (date.today() - birth_date).days // 365
            
            if age < EmployeeDataValidator.MIN_EMPLOYMENT_AGE:
                result.add_error(
                    'employment_eligibility',
                    f"Âge minimum requis: {EmployeeDataValidator.MIN_EMPLOYMENT_AGE} ans (actuel: {age})"
                    if locale == "FR" else f"الحد الأدنى للعمر: {EmployeeDataValidator.MIN_EMPLOYMENT_AGE} سنة (الحالي: {age})",
                    'AGE_TOO_YOUNG'
                )
            elif age > EmployeeDataValidator.MAX_EMPLOYMENT_AGE:
                result.add_warning(
                    'employment_eligibility',
                    f"Âge supérieur à la limite habituelle: {age} ans"
                    if locale == "FR" else f"العمر يتجاوز الحد المعتاد: {age} سنة",
                    'AGE_OVER_LIMIT'
                )
        
        # Contract type validation
        if employee_data.get('contract_type') == 'CDD':
            if not employee_data.get('contract_end_date'):
                result.add_error(
                    'contract_compliance',
                    "Date de fin de contrat obligatoire pour CDD"
                    if locale == "FR" else "تاريخ انتهاء العقد مطلوب للعقد محدد المدة",
                    'MISSING_CONTRACT_END'
                )
            else:
                # Check CDD duration limits (typically 2 years max in Mauritania)
                hire_date = employee_data.get('hire_date')
                end_date = employee_data.get('contract_end_date')
                
                if hire_date and end_date:
                    if isinstance(hire_date, str):
                        hire_date = datetime.strptime(hire_date, '%Y-%m-%d').date()
                    if isinstance(end_date, str):
                        end_date = datetime.strptime(end_date, '%Y-%m-%d').date()
                    
                    contract_duration = (end_date - hire_date).days
                    max_cdd_days = 365 * 2  # 2 years
                    
                    if contract_duration > max_cdd_days:
                        result.add_warning(
                            'contract_compliance',
                            f"Durée CDD dépassant 2 ans: {contract_duration} jours"
                            if locale == "FR" else f"مدة العقد محدد المدة تتجاوز سنتين: {contract_duration} يوماً",
                            'CDD_DURATION_LONG'
                        )
        
        # Social security compliance
        if employee_data.get('hire_date'):
            hire_date = employee_data['hire_date']
            if isinstance(hire_date, str):
                hire_date = datetime.strptime(hire_date, '%Y-%m-%d').date()
            
            # CNSS number should be provided within reasonable time
            if not employee_data.get('cnss_number'):
                days_since_hire = (date.today() - hire_date).days
                if days_since_hire > 90:  # 3 months grace period
                    result.add_warning(
                        'social_security_compliance',
                        "Numéro CNSS manquant depuis plus de 3 mois"
                        if locale == "FR" else "رقم الضمان الاجتماعي مفقود منذ أكثر من 3 أشهر",
                        'MISSING_CNSS'
                    )
        
        return result
    
    def validate_leave_entitlement(self, employee_data: Dict[str, Any], 
                                 leave_data: Dict[str, Any], locale: str = "FR") -> ValidationResult:
        """
        Validate leave entitlement based on seniority and business rules
        
        Args:
            employee_data: Employee information
            leave_data: Leave request data
            locale: Locale for error messages
            
        Returns:
            ValidationResult with entitlement validation
        """
        result = ValidationResult()
        
        # Calculate seniority for leave entitlement
        hire_date = employee_data.get('hire_date') or employee_data.get('seniority_date')
        if not hire_date:
            result.add_error(
                'leave_entitlement',
                "Date d'embauche ou d'ancienneté requise pour calculer les congés"
                if locale == "FR" else "تاريخ التوظيف أو الأقدمية مطلوب لحساب الإجازات",
                'MISSING_HIRE_DATE'
            )
            return result
        
        if isinstance(hire_date, str):
            hire_date = datetime.strptime(hire_date, '%Y-%m-%d').date()
        
        # Calculate leave entitlement using business rules
        try:
            leave_calculator = LeaveCalculator()
            
            # Basic annual leave calculation
            seniority_years = (date.today() - hire_date).days / 365.25
            annual_leave_days = leave_calculator.calculate_annual_leave(
                hire_date, date.today().year
            )
            
            # Validate requested leave days
            requested_days = leave_data.get('days_requested', 0)
            if requested_days > annual_leave_days:
                result.add_error(
                    'leave_days',
                    f"Jours demandés ({requested_days}) dépassent l'entitlement ({annual_leave_days})"
                    if locale == "FR" else f"الأيام المطلوبة ({requested_days}) تتجاوز الاستحقاق ({annual_leave_days})",
                    'INSUFFICIENT_LEAVE_BALANCE'
                )
            
            # Validate leave period
            leave_start = leave_data.get('start_date')
            leave_end = leave_data.get('end_date')
            
            if leave_start and leave_end:
                if isinstance(leave_start, str):
                    leave_start = datetime.strptime(leave_start, '%Y-%m-%d').date()
                if isinstance(leave_end, str):
                    leave_end = datetime.strptime(leave_end, '%Y-%m-%d').date()
                
                if leave_end <= leave_start:
                    result.add_error(
                        'leave_period',
                        "Date de fin doit être après la date de début"
                        if locale == "FR" else "تاريخ الانتهاء يجب أن يكون بعد تاريخ البداية",
                        'INVALID_LEAVE_PERIOD'
                    )
                
                # Check for reasonable leave duration
                leave_duration = (leave_end - leave_start).days + 1
                if leave_duration > 90:  # 3 months max continuous leave
                    result.add_warning(
                        'leave_duration',
                        f"Congé de longue durée: {leave_duration} jours"
                        if locale == "FR" else f"إجازة طويلة المدى: {leave_duration} يوماً",
                        'LONG_LEAVE_DURATION'
                    )
            
            result.cleaned_data['calculated_entitlement'] = annual_leave_days
            result.cleaned_data['seniority_years'] = seniority_years
            
        except Exception as e:
            result.add_error(
                'leave_calculation',
                f"Erreur de calcul des congés: {str(e)}"
                if locale == "FR" else f"خطأ في حساب الإجازات: {str(e)}",
                'LEAVE_CALCULATION_ERROR'
            )
        
        return result
    
    def validate_overtime_compliance(self, employee_data: Dict[str, Any], 
                                   overtime_data: Dict[str, Any], locale: str = "FR") -> ValidationResult:
        """
        Validate overtime compliance with labor law limits
        
        Args:
            employee_data: Employee information
            overtime_data: Overtime work data
            locale: Locale for error messages
            
        Returns:
            ValidationResult with compliance validation
        """
        result = ValidationResult()
        
        # Mauritanian labor law limits
        MAX_DAILY_OVERTIME = 4  # hours
        MAX_WEEKLY_OVERTIME = 20  # hours
        MAX_MONTHLY_OVERTIME = 80  # hours
        
        # Validate daily overtime
        daily_overtime = overtime_data.get('daily_hours', 0)
        if daily_overtime > MAX_DAILY_OVERTIME:
            result.add_error(
                'daily_overtime',
                f"Heures supplémentaires journalières dépassent la limite: {daily_overtime}h > {MAX_DAILY_OVERTIME}h"
                if locale == "FR" else f"ساعات العمل الإضافي اليومية تتجاوز الحد: {daily_overtime}س > {MAX_DAILY_OVERTIME}س",
                'DAILY_OVERTIME_EXCEEDED'
            )
        
        # Validate weekly overtime
        weekly_overtime = overtime_data.get('weekly_hours', 0)
        if weekly_overtime > MAX_WEEKLY_OVERTIME:
            result.add_error(
                'weekly_overtime',
                f"Heures supplémentaires hebdomadaires dépassent la limite: {weekly_overtime}h > {MAX_WEEKLY_OVERTIME}h"
                if locale == "FR" else f"ساعات العمل الإضافي الأسبوعية تتجاوز الحد: {weekly_overtime}س > {MAX_WEEKLY_OVERTIME}س",
                'WEEKLY_OVERTIME_EXCEEDED'
            )
        
        # Validate monthly overtime
        monthly_overtime = overtime_data.get('monthly_hours', 0)
        if monthly_overtime > MAX_MONTHLY_OVERTIME:
            result.add_error(
                'monthly_overtime',
                f"Heures supplémentaires mensuelles dépassent la limite: {monthly_overtime}h > {MAX_MONTHLY_OVERTIME}h"
                if locale == "FR" else f"ساعات العمل الإضافي الشهرية تتجاوز الحد: {monthly_overtime}س > {MAX_MONTHLY_OVERTIME}س",
                'MONTHLY_OVERTIME_EXCEEDED'
            )
        
        # Check overtime rate compliance
        overtime_rate = overtime_data.get('rate_multiplier', 1.0)
        if overtime_rate < 1.25:  # Minimum 25% premium
            result.add_error(
                'overtime_rate',
                f"Taux d'heures supplémentaires trop faible: {overtime_rate} < 1.25"
                if locale == "FR" else f"معدل العمل الإضافي منخفض جداً: {overtime_rate} < 1.25",
                'OVERTIME_RATE_TOO_LOW'
            )
        
        # Validate overtime justification for high amounts
        if monthly_overtime > 40:  # Half the legal limit
            if not overtime_data.get('justification'):
                result.add_warning(
                    'overtime_justification',
                    "Justification recommandée pour heures supplémentaires importantes"
                    if locale == "FR" else "التبرير مطلوب للعمل الإضافي الكبير",
                    'OVERTIME_JUSTIFICATION_NEEDED'
                )
        
        return result


class DataSanitizer:
    """Comprehensive data sanitization and normalization utilities"""
    
    @staticmethod
    def sanitize_text_input(text: str, max_length: int = None, 
                          preserve_case: bool = False, locale: str = "FR") -> str:
        """
        Sanitize text input with comprehensive cleaning
        
        Args:
            text: Input text to sanitize
            max_length: Maximum allowed length
            preserve_case: Whether to preserve original case
            locale: Locale for processing
            
        Returns:
            Sanitized text
        """
        if not text:
            return ""
        
        # Basic cleaning using enhanced text utilities
        cleaned = TextFormatter.clean_string(text, preserve_case)
        
        # Remove potentially dangerous characters
        # Keep Arabic and French characters
        safe_chars = re.compile(r'[^\w\s\-\'\".,;:!()\[\]{}/@#$%^&*+=<>?|\\`~\u0600-\u06FF\u00C0-\u017F]', re.UNICODE)
        cleaned = safe_chars.sub('', cleaned)
        
        # Normalize whitespace
        cleaned = ' '.join(cleaned.split())
        
        # Handle length limits
        if max_length and len(cleaned) > max_length:
            cleaned = cleaned[:max_length].rsplit(' ', 1)[0]  # Break at word boundary
            if locale == "AR":
                cleaned = ArabicTextUtils.wrap_rtl_text(cleaned)
        
        return cleaned
    
    @staticmethod
    def sanitize_numeric_input(value: Any, data_type: str = "decimal") -> Any:
        """
        Sanitize numeric input with type conversion
        
        Args:
            value: Input value to sanitize
            data_type: Target data type (int, decimal, percentage)
            
        Returns:
            Sanitized numeric value or None if invalid
        """
        if value is None or value == '':
            return None
        
        try:
            # Handle string inputs
            if isinstance(value, str):
                # Remove common formatting
                value = value.replace(',', '.').replace(' ', '').replace('%', '')
                
                # Remove currency symbols
                value = re.sub(r'[^\d\.\-\+]', '', value)
            
            if data_type == "int":
                return int(float(value))
            elif data_type == "decimal":
                return Decimal(str(value)).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
            elif data_type == "percentage":
                decimal_val = Decimal(str(value)).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
                # Ensure percentage is in reasonable range
                return max(Decimal('0.00'), min(decimal_val, Decimal('100.00')))
            else:
                return float(value)
                
        except (ValueError, TypeError, InvalidOperation):
            return None
    
    @staticmethod
    def sanitize_phone_number(phone: str) -> str:
        """
        Sanitize and format phone number for Mauritanian context
        
        Args:
            phone: Input phone number
            
        Returns:
            Sanitized and formatted phone number
        """
        if not phone:
            return ""
        
        # Use enhanced validation and formatting
        is_valid, error = validate_mauritanian_identifier(phone, 'phone')
        if is_valid:
            return ValidationUtils.format_phone(phone, include_country_code=True)
        else:
            # Basic cleaning for invalid numbers
            cleaned = re.sub(r'[^\d\+]', '', str(phone))
            return cleaned[:20]  # Limit length
    
    @staticmethod
    def sanitize_email(email: str) -> str:
        """
        Sanitize email address
        
        Args:
            email: Input email address
            
        Returns:
            Sanitized email address
        """
        if not email:
            return ""
        
        # Basic cleaning
        email = email.strip().lower()
        
        # Remove dangerous characters
        email = re.sub(r'[^\w\.\-\+@]', '', email)
        
        # Limit length
        if len(email) > 254:
            email = email[:254]
        
        return email
    
    @staticmethod
    def sanitize_identifier(identifier: str, id_type: str) -> str:
        """
        Sanitize various identifier types (NNI, tax numbers, etc.)
        
        Args:
            identifier: Input identifier
            id_type: Type of identifier
            
        Returns:
            Sanitized identifier
        """
        if not identifier:
            return ""
        
        identifier = str(identifier).strip().upper()
        
        if id_type == "nni":
            # Keep only digits and format
            identifier = re.sub(r'[^\d]', '', identifier)
            if len(identifier) == 10:
                return ValidationUtils.format_nni(identifier)
        elif id_type in ["cnss_number", "cnam_number"]:
            # Keep alphanumeric characters
            identifier = re.sub(r'[^\w]', '', identifier)
        elif id_type == "tax_number":
            # Keep digits only
            identifier = re.sub(r'[^\d]', '', identifier)
        
        return identifier[:20]  # Limit length


class BatchValidator:
    """Batch validation for bulk data processing with performance optimization"""
    
    def __init__(self, chunk_size: int = 100, max_errors: int = 1000):
        """
        Initialize batch validator
        
        Args:
            chunk_size: Number of records to process in each chunk
            max_errors: Maximum errors before stopping processing
        """
        self.chunk_size = chunk_size
        self.max_errors = max_errors
        self.error_count = 0
        
    def validate_employee_batch(self, employees: List[Dict[str, Any]], 
                              locale: str = "FR") -> Dict[str, Any]:
        """
        Validate batch of employee records
        
        Args:
            employees: List of employee data dictionaries
            locale: Locale for error messages
            
        Returns:
            Dictionary with validation results and statistics
        """
        results = {
            'total_records': len(employees),
            'valid_records': 0,
            'invalid_records': 0,
            'validation_results': [],
            'error_summary': {},
            'warnings_summary': {},
            'processing_stopped': False
        }
        
        # Process in chunks for memory efficiency
        for i in range(0, len(employees), self.chunk_size):
            chunk = employees[i:i + self.chunk_size]
            
            for idx, employee in enumerate(chunk):
                global_idx = i + idx
                
                # Validate personal info
                personal_result = EmployeeDataValidator.validate_personal_info(employee, locale)
                
                # Validate employment info
                employment_result = EmployeeDataValidator.validate_employment_info(employee, locale)
                
                # Combine results
                combined_result = self._combine_validation_results(
                    [personal_result, employment_result], f"employee_{global_idx}"
                )
                
                results['validation_results'].append({
                    'record_index': global_idx,
                    'employee_id': employee.get('id', f'record_{global_idx}'),
                    'result': combined_result
                })
                
                if combined_result.is_valid:
                    results['valid_records'] += 1
                else:
                    results['invalid_records'] += 1
                    self.error_count += len(combined_result.errors)
                    
                    # Track error patterns
                    for error in combined_result.errors:
                        error_code = error.get('error_code', 'UNKNOWN')
                        results['error_summary'][error_code] = results['error_summary'].get(error_code, 0) + 1
                
                # Track warning patterns
                for warning in combined_result.warnings:
                    warning_code = warning.get('warning_code', 'UNKNOWN')
                    results['warnings_summary'][warning_code] = results['warnings_summary'].get(warning_code, 0) + 1
                
                # Stop if too many errors
                if self.error_count >= self.max_errors:
                    results['processing_stopped'] = True
                    logger.warning(f"Batch validation stopped: exceeded max errors ({self.max_errors})")
                    return results
        
        return results
    
    def validate_payroll_batch(self, payroll_records: List[Dict[str, Any]], 
                             locale: str = "FR") -> Dict[str, Any]:
        """
        Validate batch of payroll records
        
        Args:
            payroll_records: List of payroll data dictionaries
            locale: Locale for error messages
            
        Returns:
            Dictionary with validation results and statistics
        """
        results = {
            'total_records': len(payroll_records),
            'valid_records': 0,
            'invalid_records': 0,
            'validation_results': [],
            'error_summary': {},
            'warnings_summary': {},
            'processing_stopped': False
        }
        
        for idx, record in enumerate(payroll_records):
            # Validate salary data
            salary_result = PayrollDataValidator.validate_salary_data(record, locale)
            
            # Validate payroll elements if present
            element_results = []
            if record.get('payroll_elements'):
                for element in record['payroll_elements']:
                    element_result = PayrollDataValidator.validate_payroll_element(element, locale)
                    element_results.append(element_result)
            
            # Validate period if present
            period_result = None
            if record.get('period_start') or record.get('period_end'):
                period_result = PayrollDataValidator.validate_payroll_period(record, locale)
            
            # Combine all results
            all_results = [salary_result] + element_results
            if period_result:
                all_results.append(period_result)
            
            combined_result = self._combine_validation_results(all_results, f"payroll_{idx}")
            
            results['validation_results'].append({
                'record_index': idx,
                'payroll_id': record.get('id', f'record_{idx}'),
                'result': combined_result
            })
            
            if combined_result.is_valid:
                results['valid_records'] += 1
            else:
                results['invalid_records'] += 1
                self.error_count += len(combined_result.errors)
                
                # Track error patterns
                for error in combined_result.errors:
                    error_code = error.get('error_code', 'UNKNOWN')
                    results['error_summary'][error_code] = results['error_summary'].get(error_code, 0) + 1
            
            # Track warning patterns
            for warning in combined_result.warnings:
                warning_code = warning.get('warning_code', 'UNKNOWN')
                results['warnings_summary'][warning_code] = results['warnings_summary'].get(warning_code, 0) + 1
            
            # Stop if too many errors
            if self.error_count >= self.max_errors:
                results['processing_stopped'] = True
                logger.warning(f"Batch validation stopped: exceeded max errors ({self.max_errors})")
                break
        
        return results
    
    def validate_cross_record_relationships(self, records: List[Dict[str, Any]], 
                                          locale: str = "FR") -> ValidationResult:
        """
        Validate relationships and constraints across multiple records
        
        Args:
            records: List of related records to validate
            locale: Locale for error messages
            
        Returns:
            ValidationResult with cross-record validation results
        """
        result = ValidationResult()
        
        # Track identifiers for uniqueness
        nni_set = set()
        cnss_set = set()
        email_set = set()
        employee_codes = set()
        
        for idx, record in enumerate(records):
            record_id = record.get('id', f'record_{idx}')
            
            # Check NNI uniqueness
            if record.get('national_id'):
                nni = record['national_id']
                if nni in nni_set:
                    result.add_error(
                        f'duplicate_nni_{record_id}',
                        f"NNI en double: {nni}" if locale == "FR" else f"رقم البطاقة مكرر: {nni}",
                        'DUPLICATE_NNI'
                    )
                else:
                    nni_set.add(nni)
            
            # Check CNSS uniqueness
            if record.get('cnss_number'):
                cnss = record['cnss_number']
                if cnss in cnss_set:
                    result.add_error(
                        f'duplicate_cnss_{record_id}',
                        f"Numéro CNSS en double: {cnss}" if locale == "FR" else f"رقم الضمان مكرر: {cnss}",
                        'DUPLICATE_CNSS'
                    )
                else:
                    cnss_set.add(cnss)
            
            # Check email uniqueness
            if record.get('email'):
                email = record['email'].lower()
                if email in email_set:
                    result.add_warning(
                        f'duplicate_email_{record_id}',
                        f"Email en double: {email}" if locale == "FR" else f"البريد الإلكتروني مكرر: {email}",
                        'DUPLICATE_EMAIL'
                    )
                else:
                    email_set.add(email)
            
            # Check employee code uniqueness
            if record.get('employee_code'):
                code = record['employee_code']
                if code in employee_codes:
                    result.add_error(
                        f'duplicate_code_{record_id}',
                        f"Code employé en double: {code}" if locale == "FR" else f"رمز الموظف مكرر: {code}",
                        'DUPLICATE_EMPLOYEE_CODE'
                    )
                else:
                    employee_codes.add(code)
        
        # Additional cross-record validations can be added here
        # (e.g., manager-subordinate relationships, department assignments, etc.)
        
        return result
    
    def _combine_validation_results(self, results: List[ValidationResult], 
                                  record_id: str) -> ValidationResult:
        """Combine multiple validation results into one"""
        combined = ValidationResult()
        
        for result in results:
            if not result.is_valid:
                combined.is_valid = False
            
            combined.errors.extend(result.errors)
            combined.warnings.extend(result.warnings)
            combined.cleaned_data.update(result.cleaned_data)
        
        return combined


# Convenience functions for easy integration

def validate_employee_data(employee_data: Dict[str, Any], locale: str = "FR") -> ValidationResult:
    """
    Convenience function for complete employee data validation
    
    Args:
        employee_data: Employee data dictionary
        locale: Locale for error messages
        
    Returns:
        ValidationResult with comprehensive validation
    """
    # Personal info validation
    personal_result = EmployeeDataValidator.validate_personal_info(employee_data, locale)
    
    # Employment info validation
    employment_result = EmployeeDataValidator.validate_employment_info(employee_data, locale)
    
    # Business rules validation
    business_validator = BusinessRuleValidator()
    eligibility_result = business_validator.validate_employment_eligibility(employee_data, locale)
    
    # Combine results
    combined = ValidationResult()
    
    for result in [personal_result, employment_result, eligibility_result]:
        if not result.is_valid:
            combined.is_valid = False
        combined.errors.extend(result.errors)
        combined.warnings.extend(result.warnings)
        combined.cleaned_data.update(result.cleaned_data)
    
    return combined


def validate_payroll_data(payroll_data: Dict[str, Any], locale: str = "FR") -> ValidationResult:
    """
    Convenience function for complete payroll data validation
    
    Args:
        payroll_data: Payroll data dictionary
        locale: Locale for error messages
        
    Returns:
        ValidationResult with comprehensive validation
    """
    # Salary data validation
    salary_result = PayrollDataValidator.validate_salary_data(payroll_data, locale)
    
    # Period validation
    period_result = PayrollDataValidator.validate_payroll_period(payroll_data, locale)
    
    # Elements validation
    element_results = []
    if payroll_data.get('elements'):
        for element in payroll_data['elements']:
            element_result = PayrollDataValidator.validate_payroll_element(element, locale)
            element_results.append(element_result)
    
    # Combine results
    combined = ValidationResult()
    all_results = [salary_result, period_result] + element_results
    
    for result in all_results:
        if not result.is_valid:
            combined.is_valid = False
        combined.errors.extend(result.errors)
        combined.warnings.extend(result.warnings)
        combined.cleaned_data.update(result.cleaned_data)
    
    return combined


def sanitize_user_input(data: Dict[str, Any], field_config: Dict[str, Dict] = None) -> Dict[str, Any]:
    """
    Convenience function for comprehensive data sanitization
    
    Args:
        data: Input data dictionary
        field_config: Configuration for field-specific sanitization
        
    Returns:
        Dictionary with sanitized data
    """
    sanitizer = DataSanitizer()
    sanitized = {}
    
    # Default field configurations
    default_config = {
        'text': {'max_length': 255, 'preserve_case': False},
        'name': {'max_length': 100, 'preserve_case': True},
        'email': {'type': 'email'},
        'phone': {'type': 'phone'},
        'nni': {'type': 'identifier', 'id_type': 'nni'},
        'amount': {'type': 'numeric', 'data_type': 'decimal'},
        'percentage': {'type': 'numeric', 'data_type': 'percentage'},
        'integer': {'type': 'numeric', 'data_type': 'int'}
    }
    
    field_config = field_config or {}
    
    for field_name, value in data.items():
        if value is None:
            sanitized[field_name] = None
            continue
        
        # Get field configuration
        config = field_config.get(field_name, {})
        field_type = config.get('type', 'text')
        
        if field_type == 'email':
            sanitized[field_name] = sanitizer.sanitize_email(value)
        elif field_type == 'phone':
            sanitized[field_name] = sanitizer.sanitize_phone_number(value)
        elif field_type == 'identifier':
            id_type = config.get('id_type', 'nni')
            sanitized[field_name] = sanitizer.sanitize_identifier(value, id_type)
        elif field_type == 'numeric':
            data_type = config.get('data_type', 'decimal')
            sanitized[field_name] = sanitizer.sanitize_numeric_input(value, data_type)
        else:  # text
            max_length = config.get('max_length', 255)
            preserve_case = config.get('preserve_case', False)
            sanitized[field_name] = sanitizer.sanitize_text_input(
                value, max_length, preserve_case
            )
    
    return sanitized