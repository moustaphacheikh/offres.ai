"""
Tests for core.utils.formula_engine module.

This module provides comprehensive test coverage for the FormulaEngine,
PayrollFormulaEvaluator classes and utility functions for mathematical
expression parsing and evaluation.
"""

import pytest
from decimal import Decimal

from core.utils.formula_engine import (
    FormulaEngine,
    FormulaCalculationError,
    PayrollFormulaEvaluator,
    safe_divide,
    percentage,
    round_currency
)


class TestFormulaEngine:
    """Test FormulaEngine class methods"""
    
    def setup_method(self):
        """Set up test instance"""
        self.engine = FormulaEngine()
    
    def test_calculate_simple_addition(self):
        """Test simple addition calculation"""
        result = self.engine.calculate("10 + 5")
        assert result == Decimal('15')
        
        result = self.engine.calculate("100.5 + 25.25")
        assert result == Decimal('125.75')
    
    def test_calculate_simple_subtraction(self):
        """Test simple subtraction calculation"""
        result = self.engine.calculate("20 - 8")
        assert result == Decimal('12')
        
        result = self.engine.calculate("100.75 - 25.25")
        assert result == Decimal('75.50')
    
    def test_calculate_simple_multiplication(self):
        """Test simple multiplication calculation"""
        result = self.engine.calculate("6 * 7")
        assert result == Decimal('42')
        
        result = self.engine.calculate("12.5 * 4")
        assert result == Decimal('50.0')
    
    def test_calculate_simple_division(self):
        """Test simple division calculation"""
        result = self.engine.calculate("15 / 3")
        assert result == Decimal('5')
        
        result = self.engine.calculate("100 / 4")
        assert result == Decimal('25')
    
    def test_calculate_division_by_zero(self):
        """Test division by zero error handling"""
        with pytest.raises(FormulaCalculationError) as exc_info:
            self.engine.calculate("10 / 0")
        
        assert "Division par 0" in str(exc_info.value)
        assert self.engine.get_error_code() == 3
    
    def test_calculate_operator_precedence(self):
        """Test operator precedence (multiplication/division before addition/subtraction)"""
        # 2 + 3 * 4 should be 2 + 12 = 14, not (2 + 3) * 4 = 20
        result = self.engine.calculate("2 + 3 * 4")
        assert result == Decimal('14')
        
        # 20 - 4 / 2 should be 20 - 2 = 18, not (20 - 4) / 2 = 8
        result = self.engine.calculate("20 - 4 / 2")
        assert result == Decimal('18')
        
        # 10 * 2 + 5 should be 20 + 5 = 25
        result = self.engine.calculate("10 * 2 + 5")
        assert result == Decimal('25')
    
    def test_calculate_with_parentheses(self):
        """Test calculations with parentheses"""
        # (2 + 3) * 4 should be 5 * 4 = 20
        result = self.engine.calculate("(2 + 3) * 4")
        assert result == Decimal('20')
        
        # (20 - 4) / 2 should be 16 / 2 = 8
        result = self.engine.calculate("(20 - 4) / 2")
        assert result == Decimal('8')
        
        # Nested parentheses: ((10 + 5) * 2) - 5 = (15 * 2) - 5 = 30 - 5 = 25
        result = self.engine.calculate("((10 + 5) * 2) - 5")
        assert result == Decimal('25')
    
    def test_calculate_complex_expression(self):
        """Test complex mathematical expressions"""
        # Complex expression with mixed operations and parentheses
        result = self.engine.calculate("(100 + 50) * 2 / 3 - 25")
        # (150) * 2 / 3 - 25 = 300 / 3 - 25 = 100 - 25 = 75
        assert result == Decimal('75')
        
        # Another complex expression
        result = self.engine.calculate("10.5 + (20.25 - 5.75) * 2")
        # 10.5 + (14.5) * 2 = 10.5 + 29 = 39.5
        assert result == Decimal('39.5')
    
    def test_calculate_decimal_numbers(self):
        """Test calculations with decimal numbers"""
        result = self.engine.calculate("10.25 + 5.75")
        assert result == Decimal('16.00')
        
        result = self.engine.calculate("100.125 * 2")
        assert result == Decimal('200.250')
        
        result = self.engine.calculate("15.5 / 2.5")
        assert result == Decimal('6.2')
    
    def test_calculate_with_whitespace(self):
        """Test calculations with various whitespace"""
        result = self.engine.calculate("  10  +  5  ")
        assert result == Decimal('15')
        
        result = self.engine.calculate("20*3-5")  # No spaces
        assert result == Decimal('55')
        
        result = self.engine.calculate("( 10 + 5 ) * 2")  # Spaces around parentheses
        assert result == Decimal('30')
    
    def test_calculate_empty_expression(self):
        """Test error handling for empty expressions"""
        with pytest.raises(FormulaCalculationError) as exc_info:
            self.engine.calculate("")
        
        assert "Expression vide" in str(exc_info.value)
        
        with pytest.raises(FormulaCalculationError):
            self.engine.calculate("   ")  # Whitespace only
    
    def test_calculate_invalid_characters(self):
        """Test error handling for invalid characters"""
        with pytest.raises(FormulaCalculationError) as exc_info:
            self.engine.calculate("10 + a")
        
        assert "J'attendais un chiffre" in str(exc_info.value)
        assert self.engine.get_error_code() == 1
    
    def test_calculate_unbalanced_parentheses(self):
        """Test error handling for unbalanced parentheses"""
        # Missing closing parenthesis
        with pytest.raises(FormulaCalculationError) as exc_info:
            self.engine.calculate("(10 + 5")
        
        assert "Il manque une parenthèse fermante" in str(exc_info.value)
        assert self.engine.get_error_code() == 2
        
        # Extra closing parenthesis
        with pytest.raises(FormulaCalculationError):
            self.engine.calculate("10 + 5)")
    
    def test_calculate_invalid_decimal(self):
        """Test error handling for invalid decimal numbers"""
        # Double decimal point
        with pytest.raises(FormulaCalculationError) as exc_info:
            self.engine.calculate("10.5.2 + 5")
        
        assert "Expression invalide" in str(exc_info.value)
        assert self.engine.get_error_code() == 5
    
    def test_calculate_incomplete_expression(self):
        """Test error handling for incomplete expressions"""
        with pytest.raises(FormulaCalculationError):
            self.engine.calculate("10 +")
        
        with pytest.raises(FormulaCalculationError):
            self.engine.calculate("* 5")
    
    def test_error_state_management(self):
        """Test error state management"""
        # Start with clean state
        assert self.engine.get_error_code() == 0
        assert self.engine.get_error_message() == ""
        
        # Successful calculation should not set error
        result = self.engine.calculate("10 + 5")
        assert result == Decimal('15')
        assert self.engine.get_error_code() == 0
        
        # Failed calculation should set error
        try:
            self.engine.calculate("10 / 0")
        except FormulaCalculationError:
            pass
        
        assert self.engine.get_error_code() == 3
        assert "Division par 0" in self.engine.get_error_message()
    
    def test_get_result(self):
        """Test getting calculation result"""
        result = self.engine.calculate("25 * 4")
        assert result == Decimal('100')
        assert self.engine.get_result() == Decimal('100')


class TestPayrollFormulaEvaluator:
    """Test PayrollFormulaEvaluator class methods"""
    
    def setup_method(self):
        """Set up test instance"""
        self.evaluator = PayrollFormulaEvaluator()
    
    def test_set_variable(self):
        """Test setting individual variables"""
        self.evaluator.set_variable("F01", 1000)
        self.evaluator.set_variable("F02", 25.5)
        self.evaluator.set_variable("RATE", Decimal('0.15'))
        
        assert self.evaluator.variables["F01"] == Decimal('1000')
        assert self.evaluator.variables["F02"] == Decimal('25.5')
        assert self.evaluator.variables["RATE"] == Decimal('0.15')
    
    def test_set_variables(self):
        """Test setting multiple variables at once"""
        variables = {
            "F01": 1000,
            "F02": 500,
            "F03": 25.0,
            "RATE": 0.15
        }
        
        self.evaluator.set_variables(variables)
        
        assert self.evaluator.variables["F01"] == Decimal('1000')
        assert self.evaluator.variables["F02"] == Decimal('500')
        assert self.evaluator.variables["F03"] == Decimal('25.0')
        assert self.evaluator.variables["RATE"] == Decimal('0.15')
    
    def test_evaluate_formula_simple(self):
        """Test evaluating simple formulas with variables"""
        self.evaluator.set_variable("F01", 1000)
        self.evaluator.set_variable("F02", 500)
        
        # Simple addition
        result = self.evaluator.evaluate_formula("F01 + F02")
        assert result == Decimal('1500')
        
        # Simple multiplication
        result = self.evaluator.evaluate_formula("F01 * 2")
        assert result == Decimal('2000')
    
    def test_evaluate_formula_complex(self):
        """Test evaluating complex formulas"""
        self.evaluator.set_variables({
            "F01": 1000,  # Base salary
            "F02": 200,   # Housing allowance
            "F03": 0.15,  # Tax rate
            "F04": 50     # Transport allowance
        })
        
        # Calculate gross salary: base + housing + transport
        result = self.evaluator.evaluate_formula("F01 + F02 + F04")
        assert result == Decimal('1250')
        
        # Calculate tax: (base + housing) * tax_rate
        result = self.evaluator.evaluate_formula("(F01 + F02) * F03")
        assert result == Decimal('180.0')
        
        # Calculate net salary: gross - tax
        result = self.evaluator.evaluate_formula("F01 + F02 + F04 - (F01 + F02) * F03")
        assert result == Decimal('1070.0')
    
    def test_evaluate_formula_with_context(self):
        """Test evaluating formulas with context variables"""
        # Set default variables
        self.evaluator.set_variables({
            "F01": 1000,
            "F02": 200
        })
        
        # Use context to override some variables
        context = {
            "F02": 300,  # Override housing allowance
            "F03": 100   # Add new variable
        }
        
        result = self.evaluator.evaluate_formula("F01 + F02 + F03", context)
        assert result == Decimal('1400')  # 1000 + 300 + 100
        
        # Verify original variables are restored
        assert self.evaluator.variables["F02"] == Decimal('200')
        assert "F03" not in self.evaluator.variables
    
    def test_evaluate_formula_undefined_variable(self):
        """Test error handling for undefined variables"""
        self.evaluator.set_variable("F01", 1000)
        
        with pytest.raises(FormulaCalculationError) as exc_info:
            self.evaluator.evaluate_formula("F01 + F02")  # F02 not defined
        
        assert "Variable non définie: F02" in str(exc_info.value)
    
    def test_evaluate_formula_invalid_syntax(self):
        """Test error handling for invalid formula syntax"""
        self.evaluator.set_variable("F01", 1000)
        
        with pytest.raises(FormulaCalculationError):
            self.evaluator.evaluate_formula("F01 + + F01")  # Double operator
        
        with pytest.raises(FormulaCalculationError):
            self.evaluator.evaluate_formula("F01 / 0")  # Division by zero
    
    def test_validate_formula_valid(self):
        """Test formula validation for valid formulas"""
        valid_formulas = [
            "F01 + F02",
            "(F01 + F02) * F03",
            "10.5 + 20",
            "BASE * RATE / 100",
            "((A + B) * C) - D"
        ]
        
        for formula in valid_formulas:
            is_valid, message = self.evaluator.validate_formula(formula)
            assert is_valid is True, f"Formula '{formula}' should be valid"
            assert message == ""
    
    def test_validate_formula_invalid(self):
        """Test formula validation for invalid formulas"""
        # Empty formula
        is_valid, message = self.evaluator.validate_formula("")
        assert is_valid is False
        assert "Formule vide" in message
        
        # Unbalanced parentheses
        is_valid, message = self.evaluator.validate_formula("(F01 + F02")
        assert is_valid is False
        assert "Parenthèses non équilibrées" in message
        
        # Invalid characters
        is_valid, message = self.evaluator.validate_formula("F01 @ F02")
        assert is_valid is False
        assert "Caractère invalide" in message
    
    def test_variable_substitution(self):
        """Test variable substitution in formulas"""
        self.evaluator.set_variables({
            "F01": 1000,
            "F02": 200,
            "RATE": 0.15
        })
        
        # Test substitution
        substituted = self.evaluator._substitute_variables("F01 + F02 * RATE")
        assert substituted == "1000 + 200 * 0.15"
        
        # Test with parentheses
        substituted = self.evaluator._substitute_variables("(F01 + F02) * RATE")
        assert substituted == "(1000 + 200) * 0.15"
    
    def test_check_balanced_parentheses(self):
        """Test parentheses balancing check"""
        # Balanced parentheses
        assert self.evaluator._check_balanced_parentheses("(a + b)") is True
        assert self.evaluator._check_balanced_parentheses("((a + b) * c)") is True
        assert self.evaluator._check_balanced_parentheses("a + (b * c) + d") is True
        
        # Unbalanced parentheses
        assert self.evaluator._check_balanced_parentheses("(a + b") is False
        assert self.evaluator._check_balanced_parentheses("a + b)") is False
        assert self.evaluator._check_balanced_parentheses("((a + b)") is False
        assert self.evaluator._check_balanced_parentheses(")a + b(") is False


class TestUtilityFunctions:
    """Test utility functions"""
    
    def test_safe_divide_normal(self):
        """Test safe division with normal divisor"""
        result = safe_divide(Decimal('100'), Decimal('4'))
        assert result == Decimal('25')
        
        result = safe_divide(Decimal('15.5'), Decimal('2.5'))
        assert result == Decimal('6.2')
    
    def test_safe_divide_by_zero(self):
        """Test safe division by zero"""
        result = safe_divide(Decimal('100'), Decimal('0'))
        assert result == Decimal('0')
        
        result = safe_divide(Decimal('-50'), Decimal('0'))
        assert result == Decimal('0')
    
    def test_percentage_calculation(self):
        """Test percentage calculations"""
        # 15% of 1000
        result = percentage(Decimal('1000'), Decimal('15'))
        assert result == Decimal('150')
        
        # 25.5% of 200
        result = percentage(Decimal('200'), Decimal('25.5'))
        assert result == Decimal('51.0')
        
        # 100% of any value should be the value itself
        result = percentage(Decimal('500'), Decimal('100'))
        assert result == Decimal('500')
        
        # 0% of any value should be 0
        result = percentage(Decimal('1000'), Decimal('0'))
        assert result == Decimal('0')
    
    def test_round_currency_default(self):
        """Test currency rounding with default 2 decimal places"""
        # Already 2 decimal places
        result = round_currency(Decimal('100.50'))
        assert result == Decimal('100.50')
        
        # More than 2 decimal places - should round
        result = round_currency(Decimal('100.567'))
        assert result == Decimal('100.57')
        
        result = round_currency(Decimal('100.563'))
        assert result == Decimal('100.56')
        
        # Less than 2 decimal places - should add zeros
        result = round_currency(Decimal('100.5'))
        assert result == Decimal('100.50')
        
        result = round_currency(Decimal('100'))
        assert result == Decimal('100.00')
    
    def test_round_currency_custom_precision(self):
        """Test currency rounding with custom decimal places"""
        # 3 decimal places
        result = round_currency(Decimal('100.5678'), 3)
        assert result == Decimal('100.568')
        
        # 1 decimal place
        result = round_currency(Decimal('100.567'), 1)
        assert result == Decimal('100.6')
        
        # 0 decimal places (whole numbers)
        result = round_currency(Decimal('100.567'), 0)
        assert result == Decimal('101')


class TestFormulaEngineIntegration:
    """Integration tests for formula engine components"""
    
    def test_payroll_calculation_workflow(self):
        """Test complete payroll calculation workflow"""
        evaluator = PayrollFormulaEvaluator()
        
        # Set up employee data
        evaluator.set_variables({
            "F01": 50000,   # Base salary (MRU)
            "F02": 10000,   # Housing allowance
            "F03": 5000,    # Transport allowance
            "F04": 0.10,    # CNSS employee rate (10%)
            "F05": 0.12,    # CNSS employer rate (12%)
            "F06": 0.05,    # CNAM rate (5%)
            "F07": 22,      # Working days in month
            "F08": 8        # Hours per day
        })
        
        # Calculate gross salary
        gross_salary = evaluator.evaluate_formula("F01 + F02 + F03")
        assert gross_salary == Decimal('65000')
        
        # Calculate CNSS employee contribution
        cnss_employee = evaluator.evaluate_formula("F01 * F04")
        assert cnss_employee == Decimal('5000.0')
        
        # Calculate CNSS employer contribution
        cnss_employer = evaluator.evaluate_formula("F01 * F05")
        assert cnss_employer == Decimal('6000.0')
        
        # Calculate CNAM contribution
        cnam_contrib = evaluator.evaluate_formula("F01 * F06")
        assert cnam_contrib == Decimal('2500.0')
        
        # Calculate net salary
        net_salary = evaluator.evaluate_formula("(F01 + F02 + F03) - (F01 * F04) - (F01 * F06)")
        assert net_salary == Decimal('57500.0')
        
        # Calculate total working hours
        total_hours = evaluator.evaluate_formula("F07 * F08")
        assert total_hours == Decimal('176')
    
    def test_formula_with_conditional_logic_simulation(self):
        """Test simulating conditional logic with formulas"""
        evaluator = PayrollFormulaEvaluator()
        
        # Simulate overtime calculation
        evaluator.set_variables({
            "HOURS_WORKED": 45,
            "NORMAL_HOURS": 40,
            "HOURLY_RATE": 250,
            "OVERTIME_RATE": 1.5
        })
        
        # Calculate normal pay
        normal_pay = evaluator.evaluate_formula("NORMAL_HOURS * HOURLY_RATE")
        assert normal_pay == Decimal('10000')
        
        # Calculate overtime hours (would need conditional in real scenario)
        # For test, we manually calculate: 45 - 40 = 5 hours overtime
        overtime_hours = evaluator.evaluate_formula("HOURS_WORKED - NORMAL_HOURS")
        assert overtime_hours == Decimal('5')
        
        # Calculate overtime pay
        overtime_pay = evaluator.evaluate_formula("(HOURS_WORKED - NORMAL_HOURS) * HOURLY_RATE * OVERTIME_RATE")
        assert overtime_pay == Decimal('1875.0')
        
        # Total pay
        total_pay = evaluator.evaluate_formula("NORMAL_HOURS * HOURLY_RATE + (HOURS_WORKED - NORMAL_HOURS) * HOURLY_RATE * OVERTIME_RATE")
        assert total_pay == Decimal('11875.0')
    
    def test_currency_precision_in_calculations(self):
        """Test that currency calculations maintain proper precision"""
        evaluator = PayrollFormulaEvaluator()
        
        evaluator.set_variables({
            "SALARY": 33333.33,  # Salary with decimals
            "TAX_RATE": 0.15     # 15% tax rate
        })
        
        # Calculate tax
        tax = evaluator.evaluate_formula("SALARY * TAX_RATE")
        expected_tax = Decimal('4999.9995')  # 33333.33 * 0.15
        
        # Round to currency precision
        tax_rounded = round_currency(tax)
        assert tax_rounded == Decimal('5000.00')
        
        # Calculate net salary
        net_salary = evaluator.evaluate_formula("SALARY - SALARY * TAX_RATE")
        net_salary_rounded = round_currency(net_salary)
        assert net_salary_rounded == Decimal('28333.33')
    
    def test_error_handling_in_complex_scenarios(self):
        """Test error handling in complex calculation scenarios"""
        evaluator = PayrollFormulaEvaluator()
        
        evaluator.set_variables({
            "BASE_SALARY": 50000,
            "ZERO_VALUE": 0
        })
        
        # Test division by zero in complex formula
        with pytest.raises(FormulaCalculationError):
            evaluator.evaluate_formula("BASE_SALARY / ZERO_VALUE")
        
        # Test undefined variable in complex formula
        with pytest.raises(FormulaCalculationError):
            evaluator.evaluate_formula("BASE_SALARY + UNDEFINED_VAR * 2")
        
        # Test invalid syntax in complex formula
        with pytest.raises(FormulaCalculationError):
            evaluator.evaluate_formula("BASE_SALARY + (50000 * ")  # Unclosed parenthesis