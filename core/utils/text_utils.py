# text_utils.py
"""
Enhanced text and number formatting utilities for Mauritanian payroll system
Converted and enhanced from NombreEnLettres.java with comprehensive bilingual support

Enhanced Features:
- French and Arabic number-to-words conversion with gender support
- Arabic dual/plural forms and proper grammar rules
- Mauritanian Ouguiya (MRU) currency formatting
- Enhanced Mauritanian validation (NNI, phone, bank accounts)
- RTL text handling for Arabic with bidirectional markers
- Belgian French syntax support (septante, octante, nonante)
- Arabic-Indic numeral conversion and normalization
- Mixed language text handling
- Document and report formatting utilities
"""

import re
import unicodedata
from decimal import Decimal
from datetime import datetime, date
from typing import Dict, List, Union, Tuple
# Cleaned up imports - removed unused Optional, locale


class NumberToTextConverter:
    """
    Enhanced number to text converter supporting French and Arabic with gender support
    Equivalent to NombreEnLettres.java with comprehensive bilingual enhancements
    """
    
    # French number words
    ONES_FR = [
        "", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf",
        "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize",
        "dix-sept", "dix-huit", "dix-neuf"
    ]
    
    TENS_FR = [
        "", "", "vingt", "trente", "quarante", "cinquante",
        "soixante", "soixante-dix", "quatre-vingt", "quatre-vingt-dix"
    ]
    
    # Belgian variations (septante, octante, nonante)
    TENS_BE = [
        "", "", "vingt", "trente", "quarante", "cinquante",
        "soixante", "septante", "octante", "nonante"
    ]
    
    SCALES_FR = [
        "", "mille", "million", "milliard", "billion"
    ]
    
    # Enhanced Arabic number words with gender support
    ONES_AR_MASC = [
        "", "واحد", "اثنان", "ثلاثة", "أربعة", "خمسة", "ستة", "سبعة", "ثمانية", "تسعة",
        "عشرة", "أحد عشر", "اثنا عشر", "ثلاثة عشر", "أربعة عشر", "خمسة عشر", "ستة عشر",
        "سبعة عشر", "ثمانية عشر", "تسعة عشر"
    ]
    
    ONES_AR_FEM = [
        "", "واحدة", "اثنتان", "ثلاث", "أربع", "خمس", "ست", "سبع", "ثماني", "تسع",
        "عشر", "إحدى عشرة", "اثنتا عشرة", "ثلاث عشرة", "أربع عشرة", "خمس عشرة", "ست عشرة",
        "سبع عشرة", "ثماني عشرة", "تسع عشرة"
    ]
    
    TENS_AR = [
        "", "", "عشرون", "ثلاثون", "أربعون", "خمسون",
        "ستون", "سبعون", "ثمانون", "تسعون"
    ]
    
    SCALES_AR = [
        "", "ألف", "مليون", "مليار", "بليون"
    ]
    
    # Arabic scale gender and plurality rules
    SCALES_AR_DUAL = [
        "", "ألفان", "مليونان", "ملياران", "بليونان"
    ]
    
    SCALES_AR_PLURAL = [
        "", "آلاف", "ملايين", "مليارات", "بلايين"
    ]
    
    # Arabic hundreds with gender
    HUNDREDS_AR_MASC = [
        "", "مائة", "مائتان", "ثلاثمائة", "أربعمائة", "خمسمائة", "ستمائة", 
        "سبعمائة", "ثمانمائة", "تسعمائة"
    ]
    
    HUNDREDS_AR_FEM = [
        "", "مائة", "مائتان", "ثلاثمائة", "أربعمائة", "خمسمائة", "ستمائة", 
        "سبعمائة", "ثمانمائة", "تسعمائة"
    ]

    def __init__(self, syntax="FR", gender="MASC"):
        """
        Initialize converter with syntax and gender preference
        
        Args:
            syntax: "FR" (French), "BE" (Belgian), "AR" (Arabic)
            gender: "MASC" (masculine) or "FEM" (feminine) - for Arabic
        """
        self.syntax = syntax
        self.gender = gender
        
        # Set appropriate word arrays based on syntax
        if syntax == "BE":
            self.ones = self.ONES_FR
            self.tens = self.TENS_BE
            self.scales = self.SCALES_FR
        elif syntax == "AR":
            self.ones = self.ONES_AR_MASC if gender == "MASC" else self.ONES_AR_FEM
            self.tens = self.TENS_AR  
            self.scales = self.SCALES_AR
        else:  # Default FR
            self.ones = self.ONES_FR
            self.tens = self.TENS_FR
            self.scales = self.SCALES_FR
    
    def convert_to_mru(self, amount: Decimal, syntax: str = None, 
                      include_currency_name: bool = True, gender: str = None) -> str:
        """
        Convert amount to MRU text format with enhanced error handling
        Equivalent to convertirEnMRO method in NombreEnLettres.java
        
        Args:
            amount: Decimal amount to convert
            syntax: Language syntax (FR/BE/AR), defaults to instance syntax
            include_currency_name: Whether to include "MRU" in output
            gender: Gender for Arabic conversion
            
        Returns:
            Text representation of amount in MRU
        """
        if syntax is None:
            syntax = self.syntax
        if gender is None:
            gender = self.gender
            
        try:
            # Validate input
            if amount is None:
                raise ValueError("Amount cannot be None")
            
            # Convert to proper decimal if needed
            if not isinstance(amount, Decimal):
                amount = Decimal(str(amount))
            
            # Format to 2 decimal places
            amount_str = f"{amount:.2f}"
            
            # Split integer and fractional parts
            parts = amount_str.split('.')
            integer_part = parts[0]
            fractional_part = parts[1] if len(parts) > 1 else "00"
            
            result_parts = []
            
            # Convert integer part
            if integer_part != "0":
                integer_text = self.convert_integer(int(integer_part), syntax, gender)
                if include_currency_name:
                    if syntax == "AR":
                        # Arabic: number + currency with proper agreement
                        integer_value = int(integer_part)
                        if integer_value == 1:
                            result_parts.append("أوقية موريتانية واحدة")
                        elif integer_value == 2:
                            result_parts.append("أوقيتان موريتانيتان")
                        elif 3 <= integer_value <= 10:
                            result_parts.append(f"{integer_text} أوقيات موريتانية")
                        else:
                            result_parts.append(f"{integer_text} أوقية موريتانية")
                    else:
                        result_parts.append(f"{integer_text} MRU")
                else:
                    result_parts.append(integer_text)
            
            # Convert fractional part (khoums - 1/5 of MRU)
            if fractional_part != "00":
                fractional_value = int(fractional_part)
                fractional_text = self.convert_integer(fractional_value, syntax, "FEM" if syntax == "AR" else gender)
                
                if syntax == "AR":
                    # Arabic khoums (خُمس) with proper grammar
                    if fractional_value == 1:
                        result_parts.append(f"{fractional_text} خُمس")
                    elif fractional_value == 2:
                        result_parts.append("خُمسان")
                    elif 3 <= fractional_value <= 10:
                        result_parts.append(f"{fractional_text} أخماس")
                    else:
                        result_parts.append(f"{fractional_text} خُمساً")
                else:
                    # French centimes
                    if fractional_value > 1:
                        result_parts.append(f"{fractional_text} centimes")
                    else:
                        result_parts.append(f"{fractional_text} centime")
            
            if not result_parts:
                if syntax == "AR":
                    return "صفر أوقية موريتانية" if include_currency_name else "صفر"
                else:
                    return "zéro MRU" if include_currency_name else "zéro"
            
            # Join parts appropriately for language
            if syntax == "AR":
                return " و ".join(result_parts)  # Arabic "and"
            else:
                return " et ".join(result_parts)  # French "and"
            
        except Exception as e:
            error_msg = f"Erreur de conversion: {str(e)}"
            if syntax == "AR":
                error_msg = f"خطأ في التحويل: {str(e)}"
            return error_msg
    
    def convert_integer(self, number: int, syntax: str = None, gender: str = None) -> str:
        """
        Convert integer to text with language support
        
        Args:
            number: Integer to convert
            syntax: Language syntax (FR/BE/AR), defaults to instance syntax
            gender: Gender for Arabic conversion
            
        Returns:
            Text representation of number
        """
        if syntax is None:
            syntax = self.syntax
        if gender is None:
            gender = self.gender
            
        # Check limits
        if number > 999999999999:
            if syntax == "AR":
                raise ValueError("الرقم المطلوب كبير جداً...")
            else:
                raise ValueError("Le nombre demandé est trop grand...")
        
        if number == 0:
            if syntax == "AR":
                return "صفر"
            else:
                return "zéro"
        
        if number < 0:
            if syntax == "AR":
                return f"سالب {self._integer_to_letters(str(abs(number)), syntax, gender)}"
            else:
                return f"moins {self._integer_to_letters(str(abs(number)), syntax, gender)}"
        
        return self._integer_to_letters(str(number), syntax, gender)
    
    def _integer_to_letters(self, number_str: str, syntax: str = "FR", gender: str = "MASC") -> str:
        """
        Convert integer string to letters with language support
        """
        if not number_str or number_str == "0":
            if syntax == "AR":
                return "صفر"
            else:
                return "zéro"
        
        # Pad with zeros to make groups of 3
        while len(number_str) % 3 != 0:
            number_str = "0" + number_str
        
        # Split into groups of 3 digits
        groups = []
        for i in range(0, len(number_str), 3):
            groups.append(number_str[i:i+3])
        
        result_parts = []
        num_groups = len(groups)
        
        for i, group in enumerate(groups):
            group_value = int(group)
            if group_value == 0:
                continue
            
            group_text = self._convert_hundreds(group_value, syntax, gender)
            scale_index = num_groups - i - 1
            
            if scale_index > 0 and scale_index < len(self.scales):
                scale_word = self.scales[scale_index]
                
                # Handle Arabic number agreement rules
                if syntax == "AR" and scale_index > 0:
                    if group_value == 1:
                        result_parts.append(scale_word)
                    elif group_value == 2:
                        # Arabic dual form
                        dual_scale = self.SCALES_AR_DUAL[scale_index] if scale_index < len(self.SCALES_AR_DUAL) else f"{scale_word}ان"
                        result_parts.append(dual_scale)
                    elif 3 <= group_value <= 10:
                        # Arabic plural for 3-10 (scale first, then number)
                        plural_scale = self.SCALES_AR_PLURAL[scale_index] if scale_index < len(self.SCALES_AR_PLURAL) else f"{scale_word}ات"
                        result_parts.append(f"{plural_scale} {group_text}")
                    else:
                        # 11+: number + singular scale
                        result_parts.append(f"{group_text} {scale_word}")
                        
                # Handle French/Belgian scales
                elif scale_word == "mille" and group_value == 1:
                    result_parts.append("mille")
                elif scale_word in ["million", "milliard", "billion"]:
                    if group_value > 1:
                        result_parts.append(f"{group_text} {scale_word}s")
                    else:
                        result_parts.append(f"{group_text} {scale_word}")
                else:
                    result_parts.append(f"{group_text} {scale_word}")
            else:
                result_parts.append(group_text)
        
        return " ".join(result_parts)
    
    def _convert_hundreds(self, number: int, syntax: str = "FR", gender: str = "MASC") -> str:
        """Convert a number from 0-999 to text with language and gender support"""
        if number == 0:
            return ""
        
        result = []
        
        # Hundreds
        hundreds = number // 100
        remainder = number % 100
        
        if hundreds > 0:
            if syntax == "AR":
                # Arabic hundreds with gender sensitivity
                if hundreds == 1:
                    result.append("مائة")
                elif hundreds == 2:
                    result.append("مائتان")
                elif hundreds <= 9:
                    ones_ar = self.ONES_AR_MASC if gender == "MASC" else self.ONES_AR_FEM
                    result.append(f"{ones_ar[hundreds]} مائة")
                else:
                    ones_ar = self.ONES_AR_MASC if gender == "MASC" else self.ONES_AR_FEM
                    result.append(f"{ones_ar[hundreds]} مائة")
            else:
                # French/Belgian hundreds
                if hundreds == 1:
                    result.append("cent")
                else:
                    # Add 's' to 'cent' only if remainder is 0 (exactly 200, 300, etc.)
                    if remainder == 0:
                        result.append(f"{self.ONES_FR[hundreds]} cents")
                    else:
                        result.append(f"{self.ONES_FR[hundreds]} cent")
        
        # Tens and ones
        if remainder > 0:
            if remainder < 20:
                if syntax == "AR":
                    ones_ar = self.ONES_AR_MASC if gender == "MASC" else self.ONES_AR_FEM
                    result.append(ones_ar[remainder])
                else:
                    result.append(self.ones[remainder])
            else:
                tens_digit = remainder // 10
                ones_digit = remainder % 10
                
                if syntax == "AR":
                    # Arabic tens handling with proper conjunction
                    if ones_digit == 0:
                        result.append(self.TENS_AR[tens_digit])
                    else:
                        # Arabic: ones first, then conjunction "و" (wa), then tens
                        ones_ar = self.ONES_AR_MASC if gender == "MASC" else self.ONES_AR_FEM
                        result.append(f"{ones_ar[ones_digit]} و{self.TENS_AR[tens_digit]}")
                        
                elif syntax == "BE":
                    # Belgian: septante, octante, nonante
                    if ones_digit == 0:
                        result.append(self.TENS_BE[tens_digit])
                    elif ones_digit == 1 and tens_digit in [2, 3, 4, 5, 6, 7]:
                        result.append(f"{self.TENS_BE[tens_digit]}-et-un")
                    else:
                        result.append(f"{self.TENS_BE[tens_digit]}-{self.ONES_FR[ones_digit]}")
                        
                else:  # French
                    # French: special handling for 70, 80, 90
                    if tens_digit in [7, 9]:  # soixante-dix, quatre-vingt-dix
                        if tens_digit == 7:
                            if ones_digit == 0:
                                result.append("soixante-dix")
                            else:
                                result.append(f"soixante-{self.ONES_FR[ones_digit + 10]}")
                        else:  # tens_digit == 9
                            if ones_digit == 0:
                                result.append("quatre-vingt-dix")
                            else:
                                result.append(f"quatre-vingt-{self.ONES_FR[ones_digit + 10]}")
                    elif tens_digit == 8:  # quatre-vingt
                        if ones_digit == 0:
                            result.append("quatre-vingts")
                        else:
                            result.append(f"quatre-vingt-{self.ONES_FR[ones_digit]}")
                    else:
                        if ones_digit == 0:
                            result.append(self.TENS_FR[tens_digit])
                        elif ones_digit == 1 and tens_digit in [2, 3, 4, 5, 6]:
                            result.append(f"{self.TENS_FR[tens_digit]}-et-un")
                        else:
                            result.append(f"{self.TENS_FR[tens_digit]}-{self.ONES_FR[ones_digit]}")
        
        return " ".join(result)


class TextFormatter:
    """Enhanced text formatting utilities with comprehensive multilingual and local support"""
    
    # Currency symbols and names
    CURRENCY_SYMBOLS = {
        "MRU": "أوقية",  # Mauritanian Ouguiya in Arabic
        "EUR": "€",
        "USD": "$",
        "MAD": "د.م.",  # Moroccan Dirham
        "XOF": "FCFA",   # West African CFA Franc
    }
    
    # French month names
    FRENCH_MONTHS = [
        "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    ]
    
    # Arabic month names
    ARABIC_MONTHS = [
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
    ]
    
    # Arabic day names
    ARABIC_DAYS = [
        "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت", "الأحد"
    ]

    @staticmethod
    def format_currency(amount: Decimal, currency: str = "MRU", 
                       include_decimals: bool = True, locale_format: str = "FR") -> str:
        """
        Enhanced currency formatting with locale support
        
        Args:
            amount: Amount to format
            currency: Currency code
            include_decimals: Whether to include decimal places
            locale_format: Locale for formatting (FR/AR)
            
        Returns:
            Formatted currency string
        """
        if amount is None:
            return "0.00 MRU"
            
        try:
            if locale_format == "AR":
                # Arabic number formatting (right-to-left)
                if include_decimals:
                    formatted = f"{amount:,.2f}"
                    # Convert to Arabic-Indic numerals
                    formatted = TextFormatter._convert_to_arabic_numerals(formatted)
                else:
                    formatted = f"{int(amount):,}"
                    formatted = TextFormatter._convert_to_arabic_numerals(formatted)
                
                # Use Arabic currency name if available
                currency_symbol = TextFormatter.CURRENCY_SYMBOLS.get(currency, currency)
                return f"{formatted} {currency_symbol}"
            else:
                # French formatting
                if include_decimals:
                    formatted = f"{amount:,.2f}"
                else:
                    formatted = f"{int(amount):,}"
                
                return f"{formatted} {currency}"
                
        except Exception:
            return f"0.00 {currency}"
    
    @staticmethod
    def _convert_to_arabic_numerals(text: str) -> str:
        """Convert Western numerals to Arabic-Indic numerals"""
        arabic_digits = "٠١٢٣٤٥٦٧٨٩"
        western_digits = "0123456789"
        
        for western, arabic in zip(western_digits, arabic_digits):
            text = text.replace(western, arabic)
        
        return text
    
    @staticmethod
    def _convert_from_arabic_numerals(text: str) -> str:
        """Convert Arabic-Indic numerals to Western numerals"""
        arabic_digits = "٠١٢٣٤٥٦٧٨٩"
        western_digits = "0123456789"
        
        for arabic, western in zip(arabic_digits, western_digits):
            text = text.replace(arabic, western)
        
        return text
    
    @staticmethod
    def normalize_numerals(text: str, target_format: str = "western") -> str:
        """
        Normalize numerals in text to specified format
        
        Args:
            text: Text containing numerals
            target_format: "western" or "arabic"
            
        Returns:
            Text with normalized numerals
        """
        if not text:
            return text
            
        if target_format == "arabic":
            return TextFormatter._convert_to_arabic_numerals(text)
        else:
            return TextFormatter._convert_from_arabic_numerals(text)
    
    @staticmethod
    def format_percentage(value: Decimal, decimal_places: int = 2, locale_format: str = "FR") -> str:
        """
        Enhanced percentage formatting with locale support
        """
        if value is None:
            return "0%"
            
        try:
            percentage = value * 100
            formatted = f"{percentage:.{decimal_places}f}%"
            
            if locale_format == "AR":
                formatted = TextFormatter._convert_to_arabic_numerals(formatted)
                return f"{formatted}"
            
            return formatted
        except Exception:
            return "0%"
    
    @staticmethod
    def format_employee_name(first_name: str, last_name: str, 
                           format_type: str = "last_first", locale_format: str = "FR") -> str:
        """
        Enhanced employee name formatting with locale support
        """
        if not first_name and not last_name:
            return ""
            
        # Clean names
        first_clean = TextFormatter.clean_string(first_name or "")
        last_clean = TextFormatter.clean_string(last_name or "")
        
        if locale_format == "AR":
            # Arabic name formatting (family name first is common)
            if format_type == "last_first":
                return f"{last_clean} {first_clean}"
            elif format_type == "first_last":
                return f"{first_clean} {last_clean}"
            elif format_type == "initials":
                first_initial = first_clean[0] if first_clean else ""
                last_initial = last_clean[0] if last_clean else ""
                return f"{first_initial}.{last_initial}."
            else:  # full
                return f"{first_clean} {last_clean}"
        else:
            # French formatting
            if format_type == "last_first":
                return f"{last_clean.upper()}, {first_clean.title()}"
            elif format_type == "first_last":
                return f"{first_clean.title()} {last_clean.upper()}"
            elif format_type == "initials":
                first_initial = first_clean[0].upper() if first_clean else ""
                last_initial = last_clean[0].upper() if last_clean else ""
                return f"{first_initial}.{last_initial}."
            else:  # full
                return f"{first_clean} {last_clean}"
    
    @staticmethod
    def truncate_text(text: str, max_length: int, suffix: str = "...") -> str:
        """Truncate text to specified length"""
        if len(text) <= max_length:
            return text
        
        return text[:max_length - len(suffix)] + suffix
    
    @staticmethod
    def clean_string(text: str, preserve_case: bool = False) -> str:
        """
        Enhanced string cleaning with Unicode support
        """
        if not text:
            return ""
        
        # Normalize Unicode (important for Arabic text)
        text = unicodedata.normalize('NFKC', text)
        
        # Remove extra whitespace
        text = " ".join(text.split())
        
        # Remove control characters but preserve Arabic
        text = ''.join(char for char in text if unicodedata.category(char)[0] != 'C')
        
        if not preserve_case:
            # Only title case for Latin scripts
            if text and ord(text[0]) < 256:  # Basic Latin
                return text.title()
        
        return text
    
    @staticmethod
    def format_date(date_obj: Union[date, datetime], format_type: str = "dd/mm/yyyy", 
                   locale_format: str = "FR") -> str:
        """
        Enhanced date formatting with locale support
        """
        if not date_obj:
            return ""
            
        try:
            if format_type == "month_year":
                if locale_format == "AR":
                    month_name = TextFormatter.ARABIC_MONTHS[date_obj.month - 1]
                    year = TextFormatter._convert_to_arabic_numerals(str(date_obj.year))
                    return f"{month_name} {year}"
                else:
                    month_name = TextFormatter.FRENCH_MONTHS[date_obj.month - 1]
                    return f"{month_name} {date_obj.year}"
            elif format_type == "dd/mm/yyyy":
                formatted = date_obj.strftime("%d/%m/%Y")
                if locale_format == "AR":
                    return TextFormatter._convert_to_arabic_numerals(formatted)
                return formatted
            elif format_type == "full_datetime":
                formatted = date_obj.strftime("%d/%m/%Y à %H:%M:%S")
                if locale_format == "AR":
                    return TextFormatter._convert_to_arabic_numerals(formatted)
                return formatted
            else:
                formatted = date_obj.strftime(format_type)
                if locale_format == "AR":
                    return TextFormatter._convert_to_arabic_numerals(formatted)
                return formatted
        except Exception:
            return str(date_obj)
    
    @staticmethod
    def format_document_number(prefix: str, number: int, padding: int = 6) -> str:
        """Format document reference numbers"""
        return f"{prefix}-{number:0{padding}d}"
    
    @staticmethod
    def format_address_mauritanian(address_lines: List[str], include_country: bool = True) -> str:
        """
        Format address according to Mauritanian postal standards
        """
        clean_lines = [TextFormatter.clean_string(line) for line in address_lines if line and line.strip()]
        
        if include_country and clean_lines:
            if "mauritani" not in clean_lines[-1].lower():
                clean_lines.append("Mauritanie")
        
        return "\n".join(clean_lines)
    
    @staticmethod
    def text_direction_marker(text: str) -> str:
        """
        Add RTL/LTR direction markers for mixed text
        """
        if not text:
            return text
            
        # Check if text contains Arabic characters
        has_arabic = any('\u0600' <= char <= '\u06FF' for char in text)
        has_latin = any('\u0020' <= char <= '\u007F' for char in text)
        
        if has_arabic and has_latin:
            # Mixed text - add markers
            return f"\u202E{text}\u202C"  # RLE + text + PDF
        elif has_arabic:
            # Pure Arabic - RTL marker
            return f"\u200F{text}"  # RLM + text
        else:
            # Latin text - LTR marker
            return f"\u200E{text}"  # LRM + text


class ValidationUtils:
    """Enhanced validation utilities for Mauritanian context"""
    
    # Enhanced Mauritanian mobile prefixes with operator mapping
    MAURITANIAN_MOBILE_PREFIXES = {
        # Mauritel
        "22": "Mauritel", "23": "Mauritel", "24": "Mauritel", "25": "Mauritel",
        "26": "Mauritel", "27": "Mauritel", "28": "Mauritel", "29": "Mauritel",
        # Mattel
        "32": "Mattel", "33": "Mattel", "34": "Mattel", "35": "Mattel",
        "36": "Mattel", "37": "Mattel", "38": "Mattel", "39": "Mattel",
        # Chinguitel
        "42": "Chinguitel", "43": "Chinguitel", "44": "Chinguitel", "45": "Chinguitel",
        "46": "Chinguitel", "47": "Chinguitel", "48": "Chinguitel", "49": "Chinguitel"
    }
    
    # Fixed line prefixes
    MAURITANIAN_FIXED_PREFIXES = ["45", "46", "47", "48", "49"]
    
    # Bank code patterns for Mauritanian banks
    MAURITANIAN_BANK_CODES = [
        "011", "012", "013", "014", "015", "016", "017", "018", "019",
        "021", "022", "023", "024", "025", "026", "027", "028", "029",
        "031", "032", "033", "034", "035", "036", "037", "038", "039"
    ]

    @staticmethod
    def is_valid_nni(nni: str) -> bool:
        """
        Enhanced Mauritanian National ID (NNI) validation
        """
        if not nni:
            return False
        
        # Remove spaces and check if 10 digits
        nni_clean = nni.replace(" ", "").replace("-", "")
        
        if not nni_clean.isdigit() or len(nni_clean) != 10:
            return False
        
        # Basic format validation
        # First digit should not be 0
        if nni_clean[0] == '0':
            return False
        
        # Additional validation rules could be added here
        return True
    
    @staticmethod
    def is_valid_nni_checksum(nni: str) -> bool:
        """
        Validate NNI with checksum algorithm (placeholder for actual implementation)
        """
        if not ValidationUtils.is_valid_nni(nni):
            return False
            
        # Placeholder for actual NNI checksum algorithm
        # This would need to be implemented based on official specifications
        return True
    
    @staticmethod
    def is_valid_phone(phone: str) -> bool:
        """
        Enhanced Mauritanian phone number validation
        """
        if not phone:
            return False
        
        # Remove common separators
        phone_clean = phone.replace(" ", "").replace("-", "").replace("+", "").replace("(", "").replace(")", "")
        
        if not phone_clean.isdigit():
            return False
        
        # Check for Mauritanian formats
        if len(phone_clean) == 8:
            # Local mobile format: 12 34 56 78
            prefix = phone_clean[:2]
            return prefix in ValidationUtils.MAURITANIAN_MOBILE_PREFIXES
        elif len(phone_clean) == 11 and phone_clean.startswith("222"):
            # International format: +222 12 34 56 78
            prefix = phone_clean[3:5]
            return prefix in ValidationUtils.MAURITANIAN_MOBILE_PREFIXES
        elif len(phone_clean) == 12 and phone_clean.startswith("222"):
            # Alternative international format
            prefix = phone_clean[3:5]
            return prefix in ValidationUtils.MAURITANIAN_MOBILE_PREFIXES
        elif len(phone_clean) == 7:
            # Fixed line format: 45 25 123
            return phone_clean.startswith(tuple(ValidationUtils.MAURITANIAN_FIXED_PREFIXES))
        elif len(phone_clean) == 10 and phone_clean.startswith("222"):
            # Alternative format: 222 45 25 123 (fixed line)
            return phone_clean[3:5] in ValidationUtils.MAURITANIAN_FIXED_PREFIXES
        
        return False
    
    @staticmethod
    def is_valid_bank_account(account: str, bank_code: str = None) -> bool:
        """
        Validate Mauritanian bank account number
        """
        if not account:
            return False
        
        # Remove spaces and dashes
        account_clean = account.replace(" ", "").replace("-", "")
        
        if not account_clean.isdigit():
            return False
        
        # Basic length validation (adjust based on Mauritanian standards)
        if len(account_clean) not in [10, 11, 12, 13, 14]:
            return False
        
        # Validate bank code if provided
        if bank_code:
            bank_code_clean = bank_code.replace(" ", "")
            if bank_code_clean not in ValidationUtils.MAURITANIAN_BANK_CODES:
                return False
        
        return True
    
    @staticmethod
    def is_valid_company_registration(reg_number: str) -> bool:
        """
        Validate Mauritanian company registration number
        """
        if not reg_number:
            return False
        
        # Remove spaces and common separators
        reg_clean = reg_number.replace(" ", "").replace("-", "").replace("/", "")
        
        # Basic validation - should be alphanumeric
        if not reg_clean.isalnum():
            return False
        
        # Length check (adjust based on Mauritanian standards)
        if len(reg_clean) < 6 or len(reg_clean) > 15:
            return False
        
        return True
    
    @staticmethod
    def is_valid_tax_number(tax_number: str) -> bool:
        """
        Validate Mauritanian tax number
        """
        if not tax_number:
            return False
        
        # Remove spaces and separators
        tax_clean = tax_number.replace(" ", "").replace("-", "")
        
        # Should be numeric
        if not tax_clean.isdigit():
            return False
        
        # Length validation (adjust based on actual requirements)
        if len(tax_clean) not in [8, 9, 10, 11]:
            return False
        
        return True
    
    @staticmethod
    def format_nni(nni: str) -> str:
        """Format NNI for display"""
        if not nni:
            return ""
        
        nni_clean = nni.replace(" ", "")
        if len(nni_clean) == 10:
            return f"{nni_clean[:4]} {nni_clean[4:6]} {nni_clean[6:]}"
        
        return nni_clean
    
    @staticmethod
    def format_phone(phone: str, include_country_code: bool = False) -> str:
        """
        Enhanced phone number formatting for Mauritanian numbers
        """
        if not phone:
            return ""
        
        phone_clean = phone.replace(" ", "").replace("-", "").replace("+", "").replace("(", "").replace(")", "")
        
        # Remove leading zeros
        phone_clean = phone_clean.lstrip('0')
        
        # Handle different formats
        if len(phone_clean) == 8:
            # Mobile format: 12 34 56 78
            formatted = f"{phone_clean[:2]} {phone_clean[2:4]} {phone_clean[4:6]} {phone_clean[6:]}"
            return f"+222 {formatted}" if include_country_code else formatted
        elif len(phone_clean) == 7:
            # Fixed line format: 45 25 123
            formatted = f"{phone_clean[:2]} {phone_clean[2:4]} {phone_clean[4:]}"
            return f"+222 {formatted}" if include_country_code else formatted
        elif len(phone_clean) >= 10 and phone_clean.startswith("222"):
            # International format already present
            number_part = phone_clean[3:]
            if len(number_part) == 8:
                return f"+222 {number_part[:2]} {number_part[2:4]} {number_part[4:6]} {number_part[6:]}"
            elif len(number_part) == 7:
                return f"+222 {number_part[:2]} {number_part[2:4]} {number_part[4:]}"
        
        return phone_clean
    
    @staticmethod
    def get_phone_operator(phone: str) -> str:
        """
        Get the mobile operator for a Mauritanian phone number
        """
        if not ValidationUtils.is_valid_phone(phone):
            return ""
            
        phone_clean = phone.replace(" ", "").replace("-", "").replace("+", "")
        
        # Extract prefix
        if len(phone_clean) == 8:
            prefix = phone_clean[:2]
        elif len(phone_clean) >= 10 and phone_clean.startswith("222"):
            prefix = phone_clean[3:5]
        else:
            return ""
            
        return ValidationUtils.MAURITANIAN_MOBILE_PREFIXES.get(prefix, "")


class ArabicTextUtils:
    """Specialized utilities for Arabic text processing and RTL support"""
    
    @staticmethod
    def normalize_arabic_text(text: str) -> str:
        """
        Normalize Arabic text for consistent processing
        """
        if not text:
            return ""
        
        # Unicode normalization
        text = unicodedata.normalize('NFKC', text)
        
        # Replace various Arabic letter forms with standard forms
        replacements = {
            'ي': 'ى',  # Replace Yeh with Alef Maksura where appropriate
            'ك': 'ك',  # Standardize Kaf
            'ء': 'ء',  # Standardize Hamza
            'أ': 'أ',  # Hamza above Alef
            'إ': 'إ',  # Hamza below Alef
            'آ': 'آ',  # Alef with Madda
        }
        
        for old, new in replacements.items():
            text = text.replace(old, new)
        
        return text
    
    @staticmethod
    def remove_arabic_diacritics(text: str) -> str:
        """
        Remove Arabic diacritical marks
        """
        if not text:
            return ""
        
        # Arabic diacritics Unicode range
        diacritics = re.compile(r'[\u064B-\u0652\u0670\u0640]')
        return diacritics.sub('', text)
    
    @staticmethod
    def is_arabic_text(text: str) -> bool:
        """
        Check if text contains Arabic characters
        """
        if not text:
            return False
        
        return bool(re.search(r'[\u0600-\u06FF]', text))
    
    @staticmethod
    def wrap_rtl_text(text: str, force_direction: bool = True) -> str:
        """
        Wrap text with RTL directional marks if needed
        """
        if not text:
            return text
        
        if ArabicTextUtils.is_arabic_text(text) or force_direction:
            return f"\u202B{text}\u202C"  # Right-to-Left Embedding + Pop Directional Formatting
        
        return text
    
    @staticmethod
    def get_arabic_ordinal(number: int, gender: str = "MASC") -> str:
        """
        Get Arabic ordinal number (first, second, etc.)
        """
        ordinals_masc = {
            1: "الأول", 2: "الثاني", 3: "الثالث", 4: "الرابع", 5: "الخامس",
            6: "السادس", 7: "السابع", 8: "الثامن", 9: "التاسع", 10: "العاشر"
        }
        
        ordinals_fem = {
            1: "الأولى", 2: "الثانية", 3: "الثالثة", 4: "الرابعة", 5: "الخامسة",
            6: "السادسة", 7: "السابعة", 8: "الثامنة", 9: "التاسعة", 10: "العاشرة"
        }
        
        ordinals = ordinals_masc if gender == "MASC" else ordinals_fem
        return ordinals.get(number, str(number))


class LocalizationUtils:
    """Utilities for handling French/Arabic localization in Mauritanian context"""
    
    CURRENCY_NAMES = {
        "FR": {
            "MRU": "Ouguiya Mauritanienne",
            "EUR": "Euro",
            "USD": "Dollar Américain",
            "XOF": "Franc CFA",
            "MAD": "Dirham Marocain"
        },
        "AR": {
            "MRU": "أوقية موريتانية",
            "EUR": "يورو", 
            "USD": "دولار أمريكي",
            "XOF": "فرنك غرب أفريقيا",
            "MAD": "درهم مغربي"
        }
    }
    
    ERROR_MESSAGES = {
        "FR": {
            "required_field": "Ce champ est obligatoire",
            "invalid_format": "Format invalide",
            "invalid_nni": "Numéro NNI invalide",
            "invalid_phone": "Numéro de téléphone invalide",
            "invalid_amount": "Montant invalide",
            "invalid_date": "Date invalide",
            "invalid_email": "Adresse e-mail invalide"
        },
        "AR": {
            "required_field": "هذا الحقل مطلوب",
            "invalid_format": "تنسيق غير صحيح",
            "invalid_nni": "رقم البطاقة الوطنية غير صحيح",
            "invalid_phone": "رقم الهاتف غير صحيح", 
            "invalid_amount": "المبلغ غير صحيح",
            "invalid_date": "التاريخ غير صحيح",
            "invalid_email": "عنوان البريد الإلكتروني غير صحيح"
        }
    }
    
    REPORT_LABELS = {
        "FR": {
            "total": "Total",
            "subtotal": "Sous-total",
            "tax": "Taxe",
            "net_salary": "Salaire Net",
            "gross_salary": "Salaire Brut",
            "deductions": "Retenues",
            "allowances": "Indemnités"
        },
        "AR": {
            "total": "المجموع",
            "subtotal": "المجموع الجزئي",
            "tax": "الضريبة",
            "net_salary": "الراتب الصافي",
            "gross_salary": "الراتب الإجمالي",
            "deductions": "الاستقطاعات",
            "allowances": "البدلات"
        }
    }
    
    @staticmethod
    def get_currency_name(currency_code: str, locale: str = "FR") -> str:
        """Get localized currency name"""
        names = LocalizationUtils.CURRENCY_NAMES.get(locale, {})
        return names.get(currency_code, currency_code)
    
    @staticmethod
    def get_error_message(error_key: str, locale: str = "FR") -> str:
        """Get localized error message"""
        messages = LocalizationUtils.ERROR_MESSAGES.get(locale, {})
        return messages.get(error_key, error_key)
    
    @staticmethod
    def get_report_label(label_key: str, locale: str = "FR") -> str:
        """Get localized report label"""
        labels = LocalizationUtils.REPORT_LABELS.get(locale, {})
        return labels.get(label_key, label_key)
    
    @staticmethod
    def format_mixed_text(french_text: str, arabic_text: str) -> str:
        """
        Format mixed French/Arabic text with proper direction markers
        """
        if not arabic_text:
            return french_text or ""
        if not french_text:
            return ArabicTextUtils.wrap_rtl_text(arabic_text)
        
        # Mixed text with proper direction markers
        french_part = f"\u200E{french_text}"  # LRM + French
        arabic_part = f"\u200F{arabic_text}"  # RLM + Arabic
        
        return f"{french_part} - {arabic_part}"


class ReportTextProcessor:
    """Specialized text processing for report generation"""
    
    @staticmethod
    def format_report_header(company_name: str, report_title: str, 
                           period: str, locale: str = "FR") -> Dict[str, str]:
        """
        Format complete report header with all components
        """
        header = {
            "company": TextFormatter.clean_string(company_name),
            "title": report_title,
            "period": period,
            "formatted": f"{company_name}\n{report_title}\n{period}"
        }
        
        if locale == "AR":
            header["formatted"] = ArabicTextUtils.wrap_rtl_text(header["formatted"])
        
        return header
    
    @staticmethod
    def format_amount_in_words(amount: Decimal, currency: str = "MRU", 
                             locale: str = "FR", gender: str = "FEM") -> str:
        """
        Format amount in words for reports
        """
        converter = NumberToTextConverter(syntax=locale, gender=gender)
        return converter.convert_to_mru(amount, locale)
    
    @staticmethod
    def sanitize_for_pdf(text: str) -> str:
        """
        Sanitize text for PDF generation
        """
        if not text:
            return ""
        
        # Remove or replace problematic characters for PDF
        text = text.replace('\x00', '')  # Remove null bytes
        text = re.sub(r'[\x01-\x08\x0B-\x0C\x0E-\x1F\x7F]', '', text)  # Remove control chars
        
        return text
    
    @staticmethod
    def truncate_for_report_field(text: str, field_type: str) -> str:
        """
        Truncate text based on report field constraints
        """
        limits = {
            "name": 50,
            "short_name": 25,
            "address_line": 60,
            "description": 100,
            "note": 200,
            "title": 80,
            "employee_id": 20,
            "department": 40
        }
        
        max_length = limits.get(field_type, 100)
        return TextFormatter.truncate_text(text, max_length)


class DocumentNumberGenerator:
    """Enhanced document number generation utilities"""
    
    @staticmethod
    def generate_payroll_reference(period: date, employee_id: str, doc_type: str = "BULL") -> str:
        """
        Generate standardized payroll document reference
        """
        year_month = period.strftime("%Y%m")
        return f"{doc_type}-{year_month}-{employee_id}"
    
    @staticmethod
    def generate_report_reference(report_type: str, period: date, sequence: int = 1) -> str:
        """
        Generate standardized report reference
        """
        year_month = period.strftime("%Y%m")
        return f"{report_type}-{year_month}-{sequence:03d}"
    
    @staticmethod
    def generate_declaration_reference(tax_type: str, period: date, company_code: str) -> str:
        """
        Generate tax declaration reference
        """
        year_month = period.strftime("%Y%m")
        return f"DECL-{tax_type}-{year_month}-{company_code}"


# Enhanced factory functions for easy access
def create_text_converter(language: str = "FR", gender: str = "MASC") -> NumberToTextConverter:
    """
    Factory function to create appropriate text converter
    
    Args:
        language: Language code (FR, BE, AR)
        gender: Gender for Arabic conversion (MASC, FEM)
        
    Returns:
        Configured NumberToTextConverter instance
    """
    return NumberToTextConverter(syntax=language, gender=gender)


def format_mauritanian_currency(amount: Decimal, in_words: bool = False, 
                               locale: str = "FR", gender: str = "FEM") -> str:
    """
    Convenience function for Mauritanian currency formatting
    
    Args:
        amount: Amount to format
        in_words: Whether to return amount in words
        locale: Locale for formatting
        gender: Gender for Arabic conversion (currencies are typically feminine)
        
    Returns:
        Formatted currency string
    """
    if in_words:
        converter = NumberToTextConverter(syntax=locale, gender=gender)
        return converter.convert_to_mru(amount, locale)
    else:
        return TextFormatter.format_currency(amount, "MRU", True, locale)


def validate_mauritanian_identifier(identifier: str, id_type: str) -> Tuple[bool, str]:
    """
    Comprehensive validation for Mauritanian identifiers
    
    Args:
        identifier: Identifier to validate
        id_type: Type of identifier (nni, phone, bank_account, tax_number)
        
    Returns:
        Tuple of (is_valid, error_message)
    """
    validators = {
        "nni": ValidationUtils.is_valid_nni,
        "phone": ValidationUtils.is_valid_phone,
        "bank_account": ValidationUtils.is_valid_bank_account,
        "tax_number": ValidationUtils.is_valid_tax_number,
        "company_registration": ValidationUtils.is_valid_company_registration
    }
    
    validator = validators.get(id_type)
    if not validator:
        return False, f"Unknown identifier type: {id_type}"
    
    try:
        is_valid = validator(identifier)
        if is_valid:
            return True, ""
        else:
            return False, LocalizationUtils.get_error_message(f"invalid_{id_type}")
    except Exception as e:
        return False, str(e)


def convert_numerals(text: str, from_format: str = "western", to_format: str = "arabic") -> str:
    """
    Convert numerals between Western and Arabic-Indic formats
    
    Args:
        text: Text containing numerals
        from_format: Source format ("western" or "arabic")
        to_format: Target format ("western" or "arabic")
        
    Returns:
        Text with converted numerals
    """
    if from_format == to_format:
        return text
    
    if to_format == "arabic":
        return TextFormatter._convert_to_arabic_numerals(text)
    else:
        return TextFormatter._convert_from_arabic_numerals(text)


def format_bilingual_text(french_text: str, arabic_text: str, 
                         format_type: str = "side_by_side") -> str:
    """
    Format bilingual text with proper RTL support
    
    Args:
        french_text: French text
        arabic_text: Arabic text
        format_type: "side_by_side", "stacked", or "bracketed"
        
    Returns:
        Properly formatted bilingual text
    """
    if not arabic_text:
        return french_text or ""
    if not french_text:
        return ArabicTextUtils.wrap_rtl_text(arabic_text)
    
    if format_type == "stacked":
        return f"{french_text}\n{ArabicTextUtils.wrap_rtl_text(arabic_text)}"
    elif format_type == "bracketed":
        return f"{french_text} ({ArabicTextUtils.wrap_rtl_text(arabic_text)})"
    else:  # side_by_side
        return LocalizationUtils.format_mixed_text(french_text, arabic_text)


def create_report_summary(title: str, period: date, locale: str = "FR") -> Dict[str, str]:
    """
    Create standardized report summary
    
    Args:
        title: Report title
        period: Report period
        locale: Locale for formatting
        
    Returns:
        Report summary components
    """
    period_text = TextFormatter.format_date(period, "month_year", locale)
    
    return {
        "title": title,
        "period": period_text,
        "formatted_title": f"{title} - {period_text}",
        "locale": locale
    }