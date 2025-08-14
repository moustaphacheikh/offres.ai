# text_utils.py
"""
Text and number formatting utilities
Converted from NombreEnLettres.java and other text formatting utilities
"""

from decimal import Decimal
from typing import Dict, List, Optional


class NumberToTextConverter:
    """
    Convert numbers to text in French (Mauritanian context)
    Equivalent to NombreEnLettres.java
    """
    
    # French number words
    ONES = [
        "", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf",
        "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize",
        "dix-sept", "dix-huit", "dix-neuf"
    ]
    
    TENS = [
        "", "", "vingt", "trente", "quarante", "cinquante",
        "soixante", "soixante-dix", "quatre-vingt", "quatre-vingt-dix"
    ]
    
    SCALES = [
        "", "mille", "million", "milliard", "billion"
    ]
    
    def __init__(self, syntax="FR"):
        self.syntax = syntax
    
    def convert_to_mru(self, amount: Decimal, syntax: str = "FR") -> str:
        """
        Convert amount to MRU text format
        Equivalent to convertirEnMRO method in NombreEnLettres.java
        
        Args:
            amount: Decimal amount to convert
            syntax: Language syntax (FR for French)
            
        Returns:
            Text representation of amount in MRU
        """
        try:
            # Format to 2 decimal places
            amount_str = f"{amount:.2f}"
            
            # Split integer and fractional parts
            parts = amount_str.split('.')
            integer_part = parts[0]
            fractional_part = parts[1]
            
            result_parts = []
            
            # Convert integer part
            if integer_part != "0":
                integer_text = self.convert_integer(int(integer_part), syntax)
                if int(integer_part) > 1:
                    result_parts.append(f"{integer_text} MRU")
                else:
                    result_parts.append(f"{integer_text} MRU")
            
            # Convert fractional part (centimes)
            if fractional_part != "00":
                fractional_text = self.convert_integer(int(fractional_part), syntax)
                if int(fractional_part) > 1:
                    result_parts.append(f"{fractional_text} centimes")
                else:
                    result_parts.append(f"{fractional_text} centime")
            
            if not result_parts:
                return "zéro MRU"
            
            return " et ".join(result_parts)
            
        except Exception as e:
            return f"Erreur: {str(e)}"
    
    def convert_integer(self, number: int, syntax: str = "FR") -> str:
        """
        Convert integer to French text
        Equivalent to convertirEntier method in NombreEnLettres.java
        
        Args:
            number: Integer to convert
            syntax: Language syntax
            
        Returns:
            Text representation of number
        """
        if number > 999999999999:
            raise ValueError("Le nombre demandé est trop grand...")
        
        if number == 0:
            return "zéro"
        
        return self._integer_to_letters(str(number))
    
    def _integer_to_letters(self, number_str: str) -> str:
        """
        Convert integer string to letters
        Equivalent to entierEnLettres method in NombreEnLettres.java
        """
        if not number_str or number_str == "0":
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
            
            group_text = self._convert_hundreds(group_value)
            scale_index = num_groups - i - 1
            
            if scale_index > 0:
                if scale_index < len(self.SCALES):
                    scale_word = self.SCALES[scale_index]
                    if scale_word == "mille" and group_value == 1:
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
            else:
                result_parts.append(group_text)
        
        return " ".join(result_parts)
    
    def _convert_hundreds(self, number: int) -> str:
        """Convert a number from 0-999 to text"""
        if number == 0:
            return ""
        
        result = []
        
        # Hundreds
        hundreds = number // 100
        remainder = number % 100
        if hundreds > 0:
            if hundreds == 1:
                result.append("cent")
            else:
                # Add 's' to 'cent' only if remainder is 0 (e.g., exactly 200, 300, etc.)
                if remainder == 0:
                    result.append(f"{self.ONES[hundreds]} cents")
                else:
                    result.append(f"{self.ONES[hundreds]} cent")
        
        # Tens and ones
        remainder = number % 100
        if remainder > 0:
            if remainder < 20:
                result.append(self.ONES[remainder])
            else:
                tens = remainder // 10
                ones = remainder % 10
                
                if tens in [7, 9]:  # soixante-dix, quatre-vingt-dix
                    if tens == 7:
                        if ones == 0:
                            result.append("soixante-dix")
                        else:
                            result.append(f"soixante-{self.ONES[ones + 10]}")
                    else:  # tens == 9
                        if ones == 0:
                            result.append("quatre-vingt-dix")
                        else:
                            result.append(f"quatre-vingt-{self.ONES[ones + 10]}")
                elif tens == 8:  # quatre-vingt
                    if ones == 0:
                        result.append("quatre-vingts")
                    else:
                        result.append(f"quatre-vingt-{self.ONES[ones]}")
                else:
                    if ones == 0:
                        result.append(self.TENS[tens])
                    elif ones == 1 and tens in [2, 3, 4, 5, 6]:
                        result.append(f"{self.TENS[tens]}-et-un")
                    else:
                        result.append(f"{self.TENS[tens]}-{self.ONES[ones]}")
        
        return " ".join(result)


class TextFormatter:
    """General text formatting utilities"""
    
    @staticmethod
    def format_currency(amount: Decimal, currency: str = "MRU", 
                       include_decimals: bool = True) -> str:
        """
        Format currency amount for display
        
        Args:
            amount: Amount to format
            currency: Currency code
            include_decimals: Whether to include decimal places
            
        Returns:
            Formatted currency string
        """
        if include_decimals:
            formatted = f"{amount:,.2f}"
        else:
            formatted = f"{int(amount):,}"
        
        return f"{formatted} {currency}"
    
    @staticmethod
    def format_percentage(value: Decimal, decimal_places: int = 2) -> str:
        """
        Format percentage for display
        
        Args:
            value: Decimal value (e.g., 0.15 for 15%)
            decimal_places: Number of decimal places
            
        Returns:
            Formatted percentage string
        """
        percentage = value * 100
        return f"{percentage:.{decimal_places}f}%"
    
    @staticmethod
    def format_employee_name(first_name: str, last_name: str, 
                           format_type: str = "last_first") -> str:
        """
        Format employee name consistently
        
        Args:
            first_name: Employee first name
            last_name: Employee last name
            format_type: "last_first", "first_last", or "initials"
            
        Returns:
            Formatted name string
        """
        if format_type == "last_first":
            return f"{last_name.upper()}, {first_name.title()}"
        elif format_type == "first_last":
            return f"{first_name.title()} {last_name.upper()}"
        elif format_type == "initials":
            first_initial = first_name[0].upper() if first_name else ""
            last_initial = last_name[0].upper() if last_name else ""
            return f"{first_initial}.{last_initial}."
        else:
            return f"{first_name} {last_name}"
    
    @staticmethod
    def truncate_text(text: str, max_length: int, suffix: str = "...") -> str:
        """
        Truncate text to specified length
        
        Args:
            text: Text to truncate
            max_length: Maximum length
            suffix: Suffix to add when truncated
            
        Returns:
            Truncated text
        """
        if len(text) <= max_length:
            return text
        
        return text[:max_length - len(suffix)] + suffix
    
    @staticmethod
    def clean_string(text: str) -> str:
        """
        Clean string for database storage and display
        
        Args:
            text: Text to clean
            
        Returns:
            Cleaned text
        """
        if not text:
            return ""
        
        # Remove extra whitespace
        text = " ".join(text.split())
        
        # Title case
        return text.title()


class ReportTextUtils:
    """Text utilities specific to report generation"""
    
    @staticmethod
    def format_period_text(period_date, format_type: str = "month_year") -> str:
        """
        Format period for report headers
        
        Args:
            period_date: Date object
            format_type: Format type ("month_year", "full_date", etc.)
            
        Returns:
            Formatted period text
        """
        if format_type == "month_year":
            months = [
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
            ]
            return f"{months[period_date.month - 1]} {period_date.year}"
        elif format_type == "full_date":
            return period_date.strftime("%d/%m/%Y")
        else:
            return str(period_date)
    
    @staticmethod
    def format_report_title(title: str, employee_name: str = None, 
                          period: str = None) -> str:
        """
        Format report title with optional employee and period
        
        Args:
            title: Base title
            employee_name: Optional employee name
            period: Optional period
            
        Returns:
            Formatted report title
        """
        parts = [title]
        
        if employee_name:
            parts.append(f"- {employee_name}")
        
        if period:
            parts.append(f"({period})")
        
        return " ".join(parts)
    
    @staticmethod
    def format_address_block(address_lines: List[str]) -> str:
        """
        Format multi-line address for reports
        
        Args:
            address_lines: List of address lines
            
        Returns:
            Formatted address block
        """
        # Filter out empty lines and clean
        clean_lines = [line.strip() for line in address_lines if line.strip()]
        return "<br>".join(clean_lines)
    
    @staticmethod
    def format_signature_block(name: str, title: str, date: str = None) -> Dict[str, str]:
        """
        Format signature block for reports
        
        Args:
            name: Signatory name
            title: Signatory title
            date: Optional date
            
        Returns:
            Dict with signature components
        """
        return {
            'name': name,
            'title': title,
            'date': date or "",
            'formatted': f"{title}<br>{name}" + (f"<br>{date}" if date else "")
        }


class ValidationUtils:
    """Text validation utilities"""
    
    @staticmethod
    def is_valid_nni(nni: str) -> bool:
        """
        Validate Mauritanian National ID (NNI)
        
        Args:
            nni: NNI string to validate
            
        Returns:
            True if valid NNI format
        """
        if not nni:
            return False
        
        # Remove spaces and check if 10 digits
        nni_clean = nni.replace(" ", "")
        return nni_clean.isdigit() and len(nni_clean) == 10
    
    @staticmethod
    def is_valid_phone(phone: str) -> bool:
        """
        Validate Mauritanian phone number
        
        Args:
            phone: Phone number to validate
            
        Returns:
            True if valid phone format
        """
        if not phone:
            return False
        
        # Remove common separators
        phone_clean = phone.replace(" ", "").replace("-", "").replace("+", "")
        
        # Check basic patterns (simplified)
        return phone_clean.isdigit() and len(phone_clean) in [8, 11, 12]
    
    @staticmethod
    def format_nni(nni: str) -> str:
        """
        Format NNI for display
        
        Args:
            nni: Raw NNI string
            
        Returns:
            Formatted NNI
        """
        if not nni:
            return ""
        
        nni_clean = nni.replace(" ", "")
        if len(nni_clean) == 10:
            return f"{nni_clean[:4]} {nni_clean[4:6]} {nni_clean[6:]}"
        
        return nni_clean
    
    @staticmethod
    def format_phone(phone: str) -> str:
        """
        Format phone number for display
        
        Args:
            phone: Raw phone number
            
        Returns:
            Formatted phone number
        """
        if not phone:
            return ""
        
        phone_clean = phone.replace(" ", "").replace("-", "")
        
        # Basic Mauritanian mobile format
        if len(phone_clean) == 8:
            return f"{phone_clean[:2]} {phone_clean[2:4]} {phone_clean[4:6]} {phone_clean[6:]}"
        
        return phone_clean