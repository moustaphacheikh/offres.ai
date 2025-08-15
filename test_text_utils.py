#!/usr/bin/env python3
"""
Demonstration script for the enhanced text_utils module
Shows the comprehensive bilingual support and new features
"""

import sys
import importlib.util
from decimal import Decimal

# Import text_utils directly to avoid dependencies
spec = importlib.util.spec_from_file_location('text_utils', './core/utils/text_utils.py')
text_utils = importlib.util.module_from_spec(spec)
spec.loader.exec_module(text_utils)


def test_number_to_words():
    """Test number-to-words conversion with different syntaxes"""
    print("=== NUMBER TO WORDS CONVERSION ===")
    
    # Test numbers
    test_numbers = [75, 123, 1234, 2500]
    
    for num in test_numbers:
        # French
        converter_fr = text_utils.NumberToTextConverter('FR')
        french_result = converter_fr.convert_integer(num)
        
        # Belgian (with septante, octante, nonante)
        converter_be = text_utils.NumberToTextConverter('BE')
        belgian_result = converter_be.convert_integer(num)
        
        # Arabic (masculine)
        converter_ar = text_utils.NumberToTextConverter('AR', 'MASC')
        arabic_result = converter_ar.convert_integer(num)
        
        print(f"\n{num}:")
        print(f"  French:  {french_result}")
        print(f"  Belgian: {belgian_result}")
        print(f"  Arabic:  {arabic_result}")


def test_currency_conversion():
    """Test Mauritanian currency conversion"""
    print("\n\n=== MAURITANIAN CURRENCY CONVERSION ===")
    
    amounts = [Decimal('123.45'), Decimal('1000.00'), Decimal('2500.75')]
    
    for amount in amounts:
        # French
        converter_fr = text_utils.NumberToTextConverter('FR')
        french_currency = converter_fr.convert_to_mru(amount)
        
        # Arabic (feminine for currency)
        converter_ar = text_utils.NumberToTextConverter('AR', 'FEM')
        arabic_currency = converter_ar.convert_to_mru(amount)
        
        print(f"\n{amount} MRU:")
        print(f"  French: {french_currency}")
        print(f"  Arabic: {arabic_currency}")


def test_phone_validation():
    """Test enhanced Mauritanian phone validation"""
    print("\n\n=== MAURITANIAN PHONE VALIDATION ===")
    
    test_phones = [
        '22345678',  # Mauritel
        '32456789',  # Mattel  
        '42234567',  # Chinguitel
        '45251234',  # Fixed line
        '12345678',  # Invalid
        '+222 22 34 56 78'  # International format
    ]
    
    for phone in test_phones:
        valid = text_utils.ValidationUtils.is_valid_phone(phone)
        operator = text_utils.ValidationUtils.get_phone_operator(phone) if valid else 'N/A'
        formatted = text_utils.ValidationUtils.format_phone(phone, True) if valid else 'Invalid format'
        
        print(f"\n{phone}:")
        print(f"  Valid: {valid}")
        print(f"  Operator: {operator}")
        print(f"  Formatted: {formatted}")


def test_arabic_features():
    """Test Arabic text processing features"""
    print("\n\n=== ARABIC TEXT PROCESSING ===")
    
    test_texts = [
        "مرحبا بكم",  # Arabic
        "Hello World",  # English
        "Bonjour مرحبا",  # Mixed
    ]
    
    for text in test_texts:
        is_arabic = text_utils.ArabicTextUtils.is_arabic_text(text)
        normalized = text_utils.ArabicTextUtils.normalize_arabic_text(text)
        with_rtl = text_utils.ArabicTextUtils.wrap_rtl_text(text)
        
        print(f"\nText: {text}")
        print(f"  Is Arabic: {is_arabic}")
        print(f"  Normalized: {normalized}")
        print(f"  With RTL markers: {repr(with_rtl)}")


def test_numeral_conversion():
    """Test Arabic-Indic numeral conversion"""
    print("\n\n=== NUMERAL CONVERSION ===")
    
    test_numbers = ["123", "2024", "45.67"]
    
    for num in test_numbers:
        arabic_numerals = text_utils.convert_numerals(num, "western", "arabic")
        back_to_western = text_utils.convert_numerals(arabic_numerals, "arabic", "western")
        
        print(f"\n{num}:")
        print(f"  Arabic numerals: {arabic_numerals}")
        print(f"  Back to Western: {back_to_western}")


def test_bilingual_formatting():
    """Test bilingual text formatting"""
    print("\n\n=== BILINGUAL TEXT FORMATTING ===")
    
    french_texts = ["Salaire Net", "Retenues", "Total"]
    arabic_texts = ["الراتب الصافي", "الاستقطاعات", "المجموع"]
    
    for fr, ar in zip(french_texts, arabic_texts):
        side_by_side = text_utils.format_bilingual_text(fr, ar, "side_by_side")
        stacked = text_utils.format_bilingual_text(fr, ar, "stacked")
        bracketed = text_utils.format_bilingual_text(fr, ar, "bracketed")
        
        print(f"\nFrench: {fr} | Arabic: {ar}")
        print(f"  Side by side: {side_by_side}")
        print(f"  Stacked: {repr(stacked)}")
        print(f"  Bracketed: {bracketed}")


def test_validation_utilities():
    """Test various Mauritanian validation utilities"""
    print("\n\n=== MAURITANIAN VALIDATION UTILITIES ===")
    
    # NNI validation
    test_nnis = ['1234567890', '0123456789', '12345', 'abc1234567']
    print("\nNNI Validation:")
    for nni in test_nnis:
        valid = text_utils.ValidationUtils.is_valid_nni(nni)
        formatted = text_utils.ValidationUtils.format_nni(nni) if valid else 'Invalid'
        print(f"  {nni}: Valid={valid}, Formatted={formatted}")
    
    # Bank account validation
    test_accounts = ['1234567890123', '123456789', 'abc123', '12345678901234']
    print("\nBank Account Validation:")
    for account in test_accounts:
        valid = text_utils.ValidationUtils.is_valid_bank_account(account)
        print(f"  {account}: Valid={valid}")


def main():
    """Run all demonstrations"""
    print("🇲🇷 ENHANCED TEXT_UTILS FOR MAURITANIAN PAYROLL SYSTEM")
    print("=" * 60)
    
    try:
        test_number_to_words()
        test_currency_conversion()
        test_phone_validation()
        test_arabic_features()
        test_numeral_conversion()
        test_bilingual_formatting()
        test_validation_utilities()
        
        print("\n\n✅ ALL DEMONSTRATIONS COMPLETED SUCCESSFULLY!")
        print("\nKey Features Enhanced:")
        print("• French and Arabic number-to-words with gender support")
        print("• Belgian French syntax (septante, octante, nonante)")
        print("• Arabic dual/plural forms and grammar rules")
        print("• Enhanced Mauritanian phone/NNI validation")
        print("• RTL text support with bidirectional markers")
        print("• Arabic-Indic numeral conversion")
        print("• Comprehensive bilingual text formatting")
        print("• MRU currency with proper Arabic grammar")
        
    except Exception as e:
        print(f"\n❌ Error during demonstration: {e}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()