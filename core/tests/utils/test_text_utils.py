"""
Tests for core.utils.text_utils module.

This module provides comprehensive test coverage for the NumberToTextConverter,
TextFormatter, ReportTextUtils, and ValidationUtils classes that implement
text formatting and validation utilities for the Mauritanian payroll system.
"""

import pytest
from decimal import Decimal
from datetime import date

from core.utils.text_utils import (
    NumberToTextConverter,
    TextFormatter,
    ReportTextUtils,
    ValidationUtils
)


class TestNumberToTextConverter:
    """Test NumberToTextConverter class methods"""
    
    def setup_method(self):
        """Set up test instance"""
        self.converter = NumberToTextConverter()
    
    def test_convert_to_mru_zero(self):
        """Test converting zero to MRU text"""
        result = self.converter.convert_to_mru(Decimal('0.00'))
        assert result == "zéro MRU"
    
    def test_convert_to_mru_integer_amounts(self):
        """Test converting integer amounts to MRU text"""
        # Single unit
        result = self.converter.convert_to_mru(Decimal('1.00'))
        assert result == "un MRU"
        
        # Multiple units
        result = self.converter.convert_to_mru(Decimal('5.00'))
        assert result == "cinq MRU"
        
        # Larger amount
        result = self.converter.convert_to_mru(Decimal('100.00'))
        assert result == "cent MRU"
    
    def test_convert_to_mru_with_centimes(self):
        """Test converting amounts with centimes to MRU text"""
        # Single centime
        result = self.converter.convert_to_mru(Decimal('0.01'))
        assert result == "un centime"
        
        # Multiple centimes
        result = self.converter.convert_to_mru(Decimal('0.25'))
        assert result == "vingt-cinq centimes"
        
        # MRU and centimes
        result = self.converter.convert_to_mru(Decimal('5.50'))
        assert result == "cinq MRU et cinquante centimes"
        
        # Large amount with centimes
        result = self.converter.convert_to_mru(Decimal('1234.56'))
        assert "mille deux cent trente-quatre MRU et cinquante-six centimes" in result
    
    def test_convert_to_mru_thousands(self):
        """Test converting thousands to MRU text"""
        result = self.converter.convert_to_mru(Decimal('1000.00'))
        assert "mille MRU" in result
        
        result = self.converter.convert_to_mru(Decimal('2500.00'))
        assert "deux mille cinq cents MRU" in result
    
    def test_convert_to_mru_millions(self):
        """Test converting millions to MRU text"""
        result = self.converter.convert_to_mru(Decimal('1000000.00'))
        assert "un million MRU" in result
        
        result = self.converter.convert_to_mru(Decimal('2500000.00'))
        assert "deux millions cinq cent mille MRU" in result
    
    def test_convert_to_mru_error_handling(self):
        """Test error handling in MRU conversion"""
        # Should handle invalid input gracefully
        result = self.converter.convert_to_mru(None)
        assert "Erreur" in result
    
    def test_convert_integer_basic_numbers(self):
        """Test converting basic integers to French text"""
        # Basic numbers 0-19
        assert self.converter.convert_integer(0) == "zéro"
        assert self.converter.convert_integer(1) == "un"
        assert self.converter.convert_integer(10) == "dix"
        assert self.converter.convert_integer(15) == "quinze"
        assert self.converter.convert_integer(19) == "dix-neuf"
    
    def test_convert_integer_tens(self):
        """Test converting tens to French text"""
        assert self.converter.convert_integer(20) == "vingt"
        assert self.converter.convert_integer(30) == "trente"
        assert self.converter.convert_integer(50) == "cinquante"
        assert self.converter.convert_integer(60) == "soixante"
    
    def test_convert_integer_special_tens(self):
        """Test converting special French tens (70, 80, 90)"""
        # 70-79 (soixante-dix pattern)
        assert self.converter.convert_integer(70) == "soixante-dix"
        assert self.converter.convert_integer(71) == "soixante-onze"
        assert self.converter.convert_integer(79) == "soixante-dix-neuf"
        
        # 80-89 (quatre-vingt pattern)
        assert self.converter.convert_integer(80) == "quatre-vingts"
        assert self.converter.convert_integer(81) == "quatre-vingt-un"
        assert self.converter.convert_integer(89) == "quatre-vingt-neuf"
        
        # 90-99 (quatre-vingt-dix pattern)
        assert self.converter.convert_integer(90) == "quatre-vingt-dix"
        assert self.converter.convert_integer(91) == "quatre-vingt-onze"
        assert self.converter.convert_integer(99) == "quatre-vingt-dix-neuf"
    
    def test_convert_integer_hundreds(self):
        """Test converting hundreds to French text"""
        assert self.converter.convert_integer(100) == "cent"
        assert self.converter.convert_integer(200) == "deux cents"
        assert self.converter.convert_integer(300) == "trois cents"
        assert self.converter.convert_integer(101) == "cent un"
        assert self.converter.convert_integer(250) == "deux cent cinquante"
    
    def test_convert_integer_thousands(self):
        """Test converting thousands to French text"""
        assert self.converter.convert_integer(1000) == "mille"
        assert self.converter.convert_integer(2000) == "deux mille"
        assert self.converter.convert_integer(1001) == "mille un"
        assert self.converter.convert_integer(1234) == "mille deux cent trente-quatre"
    
    def test_convert_integer_millions(self):
        """Test converting millions to French text"""
        assert self.converter.convert_integer(1000000) == "un million"
        assert self.converter.convert_integer(2000000) == "deux millions"
        assert self.converter.convert_integer(1000001) == "un million un"
        assert self.converter.convert_integer(1234567) == "un million deux cent trente-quatre mille cinq cent soixante-sept"
    
    def test_convert_integer_large_numbers(self):
        """Test converting very large numbers"""
        # Test billion
        result = self.converter.convert_integer(1000000000)
        assert "milliard" in result
        
        # Test above limit
        with pytest.raises(ValueError, match="trop grand"):
            self.converter.convert_integer(1000000000000)
    
    def test_convert_integer_edge_cases(self):
        """Test edge cases in integer conversion"""
        # Test with various combinations
        assert self.converter.convert_integer(21) == "vingt-et-un"
        assert self.converter.convert_integer(31) == "trente-et-un"
        assert self.converter.convert_integer(51) == "cinquante-et-un"
        assert self.converter.convert_integer(61) == "soixante-et-un"
    
    def test_convert_hundreds_method(self):
        """Test the _convert_hundreds helper method"""
        # Test direct hundreds conversion
        assert self.converter._convert_hundreds(0) == ""
        assert self.converter._convert_hundreds(1) == "un"
        assert self.converter._convert_hundreds(100) == "cent"
        assert self.converter._convert_hundreds(101) == "cent un"
        assert self.converter._convert_hundreds(999) == "neuf cent quatre-vingt-dix-neuf"


class TestTextFormatter:
    """Test TextFormatter class methods"""
    
    def test_format_currency_with_decimals(self):
        """Test currency formatting with decimal places"""
        result = TextFormatter.format_currency(Decimal('1234.56'))
        assert result == "1,234.56 MRU"
        
        result = TextFormatter.format_currency(Decimal('0.01'))
        assert result == "0.01 MRU"
        
        result = TextFormatter.format_currency(Decimal('1000000.00'))
        assert result == "1,000,000.00 MRU"
    
    def test_format_currency_without_decimals(self):
        """Test currency formatting without decimal places"""
        result = TextFormatter.format_currency(Decimal('1234.56'), include_decimals=False)
        assert result == "1,234 MRU"
        
        result = TextFormatter.format_currency(Decimal('1000000.99'), include_decimals=False)
        assert result == "1,000,000 MRU"
    
    def test_format_currency_different_currency(self):
        """Test currency formatting with different currency codes"""
        result = TextFormatter.format_currency(Decimal('1234.56'), currency="USD")
        assert result == "1,234.56 USD"
        
        result = TextFormatter.format_currency(Decimal('1234.56'), currency="EUR")
        assert result == "1,234.56 EUR"
    
    def test_format_percentage_basic(self):
        """Test basic percentage formatting"""
        result = TextFormatter.format_percentage(Decimal('0.15'))
        assert result == "15.00%"
        
        result = TextFormatter.format_percentage(Decimal('0.5'))
        assert result == "50.00%"
        
        result = TextFormatter.format_percentage(Decimal('1.0'))
        assert result == "100.00%"
    
    def test_format_percentage_custom_decimals(self):
        """Test percentage formatting with custom decimal places"""
        result = TextFormatter.format_percentage(Decimal('0.15'), decimal_places=1)
        assert result == "15.0%"
        
        result = TextFormatter.format_percentage(Decimal('0.15'), decimal_places=0)
        assert result == "15%"
        
        result = TextFormatter.format_percentage(Decimal('0.12345'), decimal_places=3)
        assert result == "12.345%"
    
    def test_format_employee_name_last_first(self):
        """Test employee name formatting in last, first format"""
        result = TextFormatter.format_employee_name("john", "doe", "last_first")
        assert result == "DOE, John"
        
        result = TextFormatter.format_employee_name("marie-claire", "smith-jones", "last_first")
        assert result == "SMITH-JONES, Marie-Claire"
    
    def test_format_employee_name_first_last(self):
        """Test employee name formatting in first last format"""
        result = TextFormatter.format_employee_name("john", "doe", "first_last")
        assert result == "John DOE"
        
        result = TextFormatter.format_employee_name("marie-claire", "smith-jones", "first_last")
        assert result == "Marie-Claire SMITH-JONES"
    
    def test_format_employee_name_initials(self):
        """Test employee name formatting as initials"""
        result = TextFormatter.format_employee_name("john", "doe", "initials")
        assert result == "J.D."
        
        result = TextFormatter.format_employee_name("marie-claire", "smith-jones", "initials")
        assert result == "M.S."
    
    def test_format_employee_name_default(self):
        """Test employee name formatting with default/invalid format"""
        result = TextFormatter.format_employee_name("john", "doe", "invalid")
        assert result == "john doe"
        
        result = TextFormatter.format_employee_name("john", "doe")  # No format specified
        assert result == "john doe"
    
    def test_format_employee_name_edge_cases(self):
        """Test employee name formatting edge cases"""
        # Empty names
        result = TextFormatter.format_employee_name("", "doe", "initials")
        assert result == ".D."
        
        result = TextFormatter.format_employee_name("john", "", "initials")
        assert result == "J.."
        
        # Single character names
        result = TextFormatter.format_employee_name("j", "d", "initials")
        assert result == "J.D."
    
    def test_truncate_text_no_truncation(self):
        """Test text truncation when no truncation is needed"""
        text = "Short text"
        result = TextFormatter.truncate_text(text, 20)
        assert result == text
    
    def test_truncate_text_with_truncation(self):
        """Test text truncation when truncation is needed"""
        text = "This is a very long text that needs to be truncated"
        result = TextFormatter.truncate_text(text, 20)
        assert len(result) == 20
        assert result.endswith("...")
        assert result == "This is a very lo..."
    
    def test_truncate_text_custom_suffix(self):
        """Test text truncation with custom suffix"""
        text = "This is a long text"
        result = TextFormatter.truncate_text(text, 15, suffix="[...]")
        assert result.endswith("[...]")
        assert len(result) == 15
    
    def test_truncate_text_exact_length(self):
        """Test text truncation at exact length"""
        text = "Exactly twenty chars"
        result = TextFormatter.truncate_text(text, 20)
        assert result == text  # Should not truncate
        
        result = TextFormatter.truncate_text(text, 19)
        assert result.endswith("...")
        assert len(result) == 19
    
    def test_clean_string_basic(self):
        """Test basic string cleaning"""
        result = TextFormatter.clean_string("  hello   world  ")
        assert result == "Hello World"
        
        result = TextFormatter.clean_string("UPPERCASE text")
        assert result == "Uppercase Text"
        
        result = TextFormatter.clean_string("lowercase text")
        assert result == "Lowercase Text"
    
    def test_clean_string_edge_cases(self):
        """Test string cleaning edge cases"""
        # Empty string
        result = TextFormatter.clean_string("")
        assert result == ""
        
        # None input
        result = TextFormatter.clean_string(None)
        assert result == ""
        
        # Only whitespace
        result = TextFormatter.clean_string("   ")
        assert result == ""
        
        # Multiple spaces
        result = TextFormatter.clean_string("word1    word2     word3")
        assert result == "Word1 Word2 Word3"


class TestReportTextUtils:
    """Test ReportTextUtils class methods"""
    
    def test_format_period_text_month_year(self):
        """Test period formatting in month-year format"""
        test_date = date(2023, 6, 15)
        result = ReportTextUtils.format_period_text(test_date, "month_year")
        assert result == "Juin 2023"
        
        # Test all months
        months = [
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        ]
        
        for month_num, month_name in enumerate(months, 1):
            test_date = date(2023, month_num, 1)
            result = ReportTextUtils.format_period_text(test_date, "month_year")
            assert result == f"{month_name} 2023"
    
    def test_format_period_text_full_date(self):
        """Test period formatting in full date format"""
        test_date = date(2023, 6, 15)
        result = ReportTextUtils.format_period_text(test_date, "full_date")
        assert result == "15/06/2023"
    
    def test_format_period_text_default(self):
        """Test period formatting with default/invalid format"""
        test_date = date(2023, 6, 15)
        result = ReportTextUtils.format_period_text(test_date, "invalid")
        assert "2023-06-15" in result  # String representation of date
    
    def test_format_report_title_basic(self):
        """Test basic report title formatting"""
        result = ReportTextUtils.format_report_title("Payroll Report")
        assert result == "Payroll Report"
    
    def test_format_report_title_with_employee(self):
        """Test report title formatting with employee name"""
        result = ReportTextUtils.format_report_title("Payroll Report", employee_name="John Doe")
        assert result == "Payroll Report - John Doe"
    
    def test_format_report_title_with_period(self):
        """Test report title formatting with period"""
        result = ReportTextUtils.format_report_title("Payroll Report", period="June 2023")
        assert result == "Payroll Report (June 2023)"
    
    def test_format_report_title_complete(self):
        """Test report title formatting with all components"""
        result = ReportTextUtils.format_report_title(
            "Payroll Report", 
            employee_name="John Doe", 
            period="June 2023"
        )
        assert result == "Payroll Report - John Doe (June 2023)"
    
    def test_format_address_block_basic(self):
        """Test basic address block formatting"""
        address_lines = [
            "123 Main Street",
            "Nouakchott",
            "Mauritania"
        ]
        
        result = ReportTextUtils.format_address_block(address_lines)
        assert result == "123 Main Street<br>Nouakchott<br>Mauritania"
    
    def test_format_address_block_with_empty_lines(self):
        """Test address block formatting with empty lines"""
        address_lines = [
            "123 Main Street",
            "",
            "  ",  # Whitespace only
            "Nouakchott",
            "Mauritania"
        ]
        
        result = ReportTextUtils.format_address_block(address_lines)
        assert result == "123 Main Street<br>Nouakchott<br>Mauritania"
    
    def test_format_address_block_with_whitespace(self):
        """Test address block formatting with leading/trailing whitespace"""
        address_lines = [
            "  123 Main Street  ",
            "   Nouakchott   ",
            "  Mauritania  "
        ]
        
        result = ReportTextUtils.format_address_block(address_lines)
        assert result == "123 Main Street<br>Nouakchott<br>Mauritania"
    
    def test_format_signature_block_basic(self):
        """Test basic signature block formatting"""
        result = ReportTextUtils.format_signature_block("John Doe", "HR Manager")
        
        assert result['name'] == "John Doe"
        assert result['title'] == "HR Manager"
        assert result['date'] == ""
        assert result['formatted'] == "HR Manager<br>John Doe"
    
    def test_format_signature_block_with_date(self):
        """Test signature block formatting with date"""
        result = ReportTextUtils.format_signature_block(
            "John Doe", 
            "HR Manager", 
            "15/06/2023"
        )
        
        assert result['name'] == "John Doe"
        assert result['title'] == "HR Manager"
        assert result['date'] == "15/06/2023"
        assert result['formatted'] == "HR Manager<br>John Doe<br>15/06/2023"


class TestValidationUtils:
    """Test ValidationUtils class methods"""
    
    def test_is_valid_nni_valid_format(self):
        """Test NNI validation with valid formats"""
        # Valid 10-digit NNI
        assert ValidationUtils.is_valid_nni("1234567890") is True
        
        # Valid NNI with spaces
        assert ValidationUtils.is_valid_nni("1234 56 7890") is True
        assert ValidationUtils.is_valid_nni("1234567890") is True
    
    def test_is_valid_nni_invalid_format(self):
        """Test NNI validation with invalid formats"""
        # Too short
        assert ValidationUtils.is_valid_nni("123456789") is False
        
        # Too long
        assert ValidationUtils.is_valid_nni("12345678901") is False
        
        # Contains non-digits
        assert ValidationUtils.is_valid_nni("123456789a") is False
        assert ValidationUtils.is_valid_nni("123-456-7890") is False
        
        # Empty or None
        assert ValidationUtils.is_valid_nni("") is False
        assert ValidationUtils.is_valid_nni(None) is False
    
    def test_is_valid_phone_valid_format(self):
        """Test phone validation with valid formats"""
        # 8-digit local number
        assert ValidationUtils.is_valid_phone("12345678") is True
        
        # 11-digit international format
        assert ValidationUtils.is_valid_phone("22212345678") is True
        
        # 12-digit with country code
        assert ValidationUtils.is_valid_phone("222123456789") is True
        
        # With separators
        assert ValidationUtils.is_valid_phone("1234-5678") is True
        assert ValidationUtils.is_valid_phone("1234 5678") is True
        assert ValidationUtils.is_valid_phone("+222 1234 5678") is True
    
    def test_is_valid_phone_invalid_format(self):
        """Test phone validation with invalid formats"""
        # Too short
        assert ValidationUtils.is_valid_phone("1234567") is False
        
        # Too long
        assert ValidationUtils.is_valid_phone("1234567890123") is False
        
        # Contains letters
        assert ValidationUtils.is_valid_phone("123456a8") is False
        
        # Empty or None
        assert ValidationUtils.is_valid_phone("") is False
        assert ValidationUtils.is_valid_phone(None) is False
    
    def test_format_nni_valid(self):
        """Test NNI formatting with valid input"""
        result = ValidationUtils.format_nni("1234567890")
        assert result == "1234 56 7890"
        
        # Already formatted
        result = ValidationUtils.format_nni("1234 56 7890")
        assert result == "1234 56 7890"
    
    def test_format_nni_invalid(self):
        """Test NNI formatting with invalid input"""
        # Wrong length
        result = ValidationUtils.format_nni("123456789")
        assert result == "123456789"  # Returns as-is
        
        # Empty or None
        result = ValidationUtils.format_nni("")
        assert result == ""
        
        result = ValidationUtils.format_nni(None)
        assert result == ""
    
    def test_format_phone_valid(self):
        """Test phone formatting with valid input"""
        # 8-digit number
        result = ValidationUtils.format_phone("12345678")
        assert result == "12 34 56 78"
        
        # Already formatted
        result = ValidationUtils.format_phone("12 34 56 78")
        assert result == "12 34 56 78"
        
        # With dashes
        result = ValidationUtils.format_phone("12-34-56-78")
        assert result == "12 34 56 78"
    
    def test_format_phone_invalid(self):
        """Test phone formatting with invalid input"""
        # Wrong length
        result = ValidationUtils.format_phone("1234567")
        assert result == "1234567"  # Returns as-is
        
        # Long number
        result = ValidationUtils.format_phone("22212345678")
        assert result == "22212345678"  # Returns as-is for long numbers
        
        # Empty or None
        result = ValidationUtils.format_phone("")
        assert result == ""
        
        result = ValidationUtils.format_phone(None)
        assert result == ""


class TestTextUtilsIntegration:
    """Integration tests for text utilities"""
    
    def test_payroll_document_generation_workflow(self):
        """Test complete payroll document text generation workflow"""
        # Employee information
        first_name = "jean-claude"
        last_name = "dubois"
        employee_name = TextFormatter.format_employee_name(first_name, last_name, "last_first")
        
        # Period information
        period_date = date(2023, 6, 15)
        period_text = ReportTextUtils.format_period_text(period_date, "month_year")
        
        # Salary amount
        salary_amount = Decimal('75000.50')
        salary_text = TextFormatter.format_currency(salary_amount)
        salary_words = NumberToTextConverter().convert_to_mru(salary_amount)
        
        # Tax rate
        tax_rate = Decimal('0.15')
        tax_rate_text = TextFormatter.format_percentage(tax_rate)
        
        # Report title
        report_title = ReportTextUtils.format_report_title(
            "Bulletin de Paie", 
            employee_name=employee_name, 
            period=period_text
        )
        
        # Verify all components
        assert "DUBOIS, Jean-Claude" in employee_name
        assert "Juin 2023" in period_text
        assert "75,000.50 MRU" in salary_text
        assert "soixante-quinze mille MRU et cinquante centimes" in salary_words
        assert "15.00%" in tax_rate_text
        assert "Bulletin de Paie - DUBOIS, Jean-Claude (Juin 2023)" in report_title
    
    def test_employee_data_validation_workflow(self):
        """Test employee data validation and formatting workflow"""
        # Raw employee data
        raw_nni = "1234567890"
        raw_phone = "12345678"
        raw_name_first = "  marie-claire  "
        raw_name_last = "  SMITH-JONES  "
        
        # Validate and format
        nni_valid = ValidationUtils.is_valid_nni(raw_nni)
        phone_valid = ValidationUtils.is_valid_phone(raw_phone)
        
        formatted_nni = ValidationUtils.format_nni(raw_nni)
        formatted_phone = ValidationUtils.format_phone(raw_phone)
        
        cleaned_first = TextFormatter.clean_string(raw_name_first)
        cleaned_last = TextFormatter.clean_string(raw_name_last)
        
        employee_name = TextFormatter.format_employee_name(
            cleaned_first, cleaned_last, "first_last"
        )
        
        # Verify validation and formatting
        assert nni_valid is True
        assert phone_valid is True
        assert formatted_nni == "1234 56 7890"
        assert formatted_phone == "12 34 56 78"
        assert cleaned_first == "Marie-Claire"
        assert cleaned_last == "Smith-Jones"
        assert employee_name == "Marie-Claire SMITH-JONES"
    
    def test_large_amount_text_conversion(self):
        """Test text conversion for very large amounts"""
        converter = NumberToTextConverter()
        
        # Test millions
        amount_millions = Decimal('5000000.75')
        result_millions = converter.convert_to_mru(amount_millions)
        assert "cinq millions MRU" in result_millions
        assert "soixante-quinze centimes" in result_millions
        
        # Test complex large number
        amount_complex = Decimal('1234567.89')
        result_complex = converter.convert_to_mru(amount_complex)
        assert "un million" in result_complex
        assert "deux cent trente-quatre mille" in result_complex
        assert "cinq cent soixante-sept MRU" in result_complex
        assert "quatre-vingt-neuf centimes" in result_complex
    
    def test_report_formatting_complete_scenario(self):
        """Test complete report formatting scenario"""
        # Company information
        company_address = [
            "ENTREPRISE SARL",
            "123 Avenue de l'Indépendance",
            "",  # Empty line to test filtering
            "Nouakchott, Mauritanie",
            "  "  # Whitespace line to test filtering
        ]
        
        formatted_address = ReportTextUtils.format_address_block(company_address)
        
        # Report title and period
        title = ReportTextUtils.format_report_title(
            "État de Paie Mensuel",
            period="Juin 2023"
        )
        
        # Signature block
        signature = ReportTextUtils.format_signature_block(
            "Ahmed Mohamed", 
            "Directeur des Ressources Humaines",
            "30/06/2023"
        )
        
        # Verify complete formatting
        expected_address = "ENTREPRISE SARL<br>123 Avenue de l'Indépendance<br>Nouakchott, Mauritanie"
        assert formatted_address == expected_address
        assert title == "État de Paie Mensuel (Juin 2023)"
        assert signature['formatted'] == "Directeur des Ressources Humaines<br>Ahmed Mohamed<br>30/06/2023"
    
    def test_text_utilities_edge_cases_handling(self):
        """Test text utilities handle edge cases gracefully"""
        converter = NumberToTextConverter()
        
        # Test edge cases that should not crash
        try:
            # Very large number (should raise ValueError)
            with pytest.raises(ValueError):
                converter.convert_integer(10**15)
            
            # Zero amount
            result_zero = converter.convert_to_mru(Decimal('0.00'))
            assert result_zero == "zéro MRU"
            
            # Very small amount
            result_small = converter.convert_to_mru(Decimal('0.01'))
            assert result_small == "un centime"
            
            # Invalid employee names
            name_result = TextFormatter.format_employee_name("", "", "last_first")
            assert name_result == ", "  # Handles empty gracefully
            
            # Invalid phone/NNI
            nni_result = ValidationUtils.format_nni("abc")
            assert nni_result == "abc"  # Returns as-is for invalid format
            
            phone_result = ValidationUtils.format_phone("invalid")
            assert phone_result == "invalid"  # Returns as-is for invalid format
            
        except Exception as e:
            pytest.fail(f"Text utilities should handle edge cases gracefully, but got: {e}")
    
    def test_french_language_accuracy(self):
        """Test accuracy of French language number conversion"""
        converter = NumberToTextConverter()
        
        # Test specific French number rules
        test_cases = [
            (21, "vingt-et-un"),
            (31, "trente-et-un"),
            (41, "quarante-et-un"),
            (51, "cinquante-et-un"),
            (61, "soixante-et-un"),
            (71, "soixante-onze"),  # Not "soixante-et-onze"
            (80, "quatre-vingts"),  # Plural form
            (81, "quatre-vingt-un"),  # Singular form
            (91, "quatre-vingt-onze"),
            (100, "cent"),
            (101, "cent un"),
            (200, "deux cents"),  # Plural
            (201, "deux cent un"),  # Singular
            (1000, "mille"),
            (1001, "mille un"),
            (2000, "deux mille")
        ]
        
        for number, expected in test_cases:
            result = converter.convert_integer(number)
            assert result == expected, f"Expected '{expected}' for {number}, got '{result}'"