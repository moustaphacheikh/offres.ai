# formula_engine.py
"""
Mathematical Expression Engine for Payroll Formula Evaluation
Converted from Java Calcul.class - evaluates mathematical expressions in salary formulas
"""

from decimal import Decimal, InvalidOperation
from typing import Union, Tuple, Optional
import re


class FormulaCalculationError(Exception):
    """Custom exception for formula calculation errors"""
    pass


class FormulaEngine:
    """
    Mathematical expression parser and evaluator
    Equivalent to Java Calcul.class
    
    Supports:
    - Basic arithmetic operations: +, -, *, /
    - Parentheses for grouping
    - Decimal numbers
    - Proper operator precedence
    - Error handling and reporting
    """
    
    def __init__(self):
        self.expression_string = ""
        self.result = Decimal('0')
        self.error_code = 0
        self.position = 0
        self.error_message = ""
        
    def calculate(self, expression: str) -> Decimal:
        """
        Main calculation method - evaluates mathematical expression
        
        Args:
            expression: Mathematical expression as string (e.g., "10.5 + 20 * 2")
            
        Returns:
            Decimal result of the calculation
            
        Raises:
            FormulaCalculationError: If expression is invalid
        """
        try:
            # Clean and prepare expression
            self.expression_string = self._prepare_expression(expression)
            self.position = 0
            self.error_code = 0
            self.error_message = ""
            
            # Parse and calculate
            self.result = self._parse_expression()
            
            # Verify expression was fully consumed
            if self.position != len(self.expression_string) - 1:  # -1 for the semicolon
                self.error_code = 10
                self.error_message = "La chaine ne se termine pas correctement"
                raise FormulaCalculationError(self.error_message)
                
            return self.result
            
        except Exception as e:
            if isinstance(e, FormulaCalculationError):
                raise
            else:
                raise FormulaCalculationError(f"Erreur de calcul: {str(e)}")
    
    def _prepare_expression(self, expression: str) -> str:
        """
        Prepare expression for parsing
        
        Args:
            expression: Raw expression string
            
        Returns:
            Cleaned expression with semicolon terminator
        """
        if not expression or not expression.strip():
            raise FormulaCalculationError("Expression vide")
            
        # Remove whitespace and add terminator
        cleaned = re.sub(r'\s+', '', expression.strip())
        return cleaned + ";"
    
    def _parse_expression(self) -> Decimal:
        """
        Parse addition and subtraction (lowest precedence)
        Equivalent to Java expression() method
        """
        result = self._parse_term()
        
        while (self.position < len(self.expression_string) and 
               self.expression_string[self.position] in ['+', '-']):
            
            operator = self.expression_string[self.position]
            self.position += 1
            
            if operator == '+':
                result += self._parse_term()
            elif operator == '-':
                result -= self._parse_term()
                
        return result
    
    def _parse_term(self) -> Decimal:
        """
        Parse multiplication and division (higher precedence)
        Equivalent to Java terme() method
        """
        result = self._parse_operand()
        
        while (self.position < len(self.expression_string) and 
               self.expression_string[self.position] in ['*', '/']):
            
            operator = self.expression_string[self.position]
            self.position += 1
            
            if operator == '*':
                result *= self._parse_operand()
            elif operator == '/':
                divisor = self._parse_operand()
                if divisor == 0:
                    self.error_code = 3
                    self.error_message = "Division par 0"
                    raise FormulaCalculationError(self.error_message)
                result /= divisor
                
        return result
    
    def _parse_operand(self) -> Decimal:
        """
        Parse operands (numbers or parenthesized expressions)
        Equivalent to Java operande() method
        """
        if self.position >= len(self.expression_string):
            raise FormulaCalculationError("Expression incomplète")
            
        if self.expression_string[self.position] == '(':
            # Handle parenthesized expression
            self.position += 1  # Skip opening parenthesis
            result = self._parse_expression()
            
            if (self.position >= len(self.expression_string) or 
                self.expression_string[self.position] != ')'):
                self.error_code = 2
                self.error_message = "Il manque une parenthèse fermante"
                raise FormulaCalculationError(self.error_message)
                
            self.position += 1  # Skip closing parenthesis
            return result
        else:
            # Handle number
            return self._parse_number()
    
    def _parse_number(self) -> Decimal:
        """
        Parse decimal numbers
        Equivalent to Java nombre() method
        """
        integer_part = self._parse_digits()
        
        # Check for decimal point
        if (self.position < len(self.expression_string) and 
            self.expression_string[self.position] == '.'):
            
            self.position += 1  # Skip decimal point
            decimal_part = self._parse_digits()
            
            # Check for second decimal point (error)
            if (self.position < len(self.expression_string) and 
                self.expression_string[self.position] == '.'):
                self.error_code = 5
                self.error_message = "Expression invalide"
                raise FormulaCalculationError(self.error_message)
                
            number_string = f"{integer_part}.{decimal_part}"
        else:
            number_string = integer_part
            
        try:
            return Decimal(number_string)
        except InvalidOperation:
            raise FormulaCalculationError(f"Nombre invalide: {number_string}")
    
    def _parse_digits(self) -> str:
        """
        Parse sequence of digits
        Equivalent to Java chiffre() method
        """
        digits = ""
        
        if (self.position >= len(self.expression_string) or 
            not self.expression_string[self.position].isdigit()):
            self.error_code = 1
            context = self.expression_string[:self.position + 1] + "<--"
            self.error_message = f"J'attendais un chiffre; caractère trouvé! {context}"
            raise FormulaCalculationError(self.error_message)
            
        while (self.position < len(self.expression_string) and 
               self.expression_string[self.position].isdigit()):
            digits += self.expression_string[self.position]
            self.position += 1
            
        return digits
    
    def get_result(self) -> Decimal:
        """Get last calculation result"""
        return self.result
    
    def get_error_code(self) -> int:
        """Get last error code"""
        return self.error_code
    
    def get_error_message(self) -> str:
        """Get last error message"""
        return self.error_message


class PayrollFormulaEvaluator:
    """
    Higher-level formula evaluator with payroll-specific functions
    Integrates with FonctionsPaie functions
    """
    
    def __init__(self):
        self.engine = FormulaEngine()
        self.variables = {}
        
    def set_variable(self, name: str, value: Union[Decimal, float, int]):
        """
        Set a variable value for formula evaluation
        
        Args:
            name: Variable name (e.g., 'F01', 'F02')
            value: Variable value
        """
        self.variables[name] = Decimal(str(value))
    
    def set_variables(self, variables: dict):
        """
        Set multiple variables at once
        
        Args:
            variables: Dictionary of variable name -> value pairs
        """
        for name, value in variables.items():
            self.set_variable(name, value)
    
    def evaluate_formula(self, formula: str, context: dict = None) -> Decimal:
        """
        Evaluate a payroll formula with variable substitution
        
        Args:
            formula: Formula string with variables (e.g., "F02*F01+F04")
            context: Optional context variables for this evaluation
            
        Returns:
            Decimal result of the formula evaluation
            
        Raises:
            FormulaCalculationError: If formula is invalid or variables missing
        """
        if context:
            # Temporarily set context variables
            original_vars = self.variables.copy()
            self.set_variables(context)
            
        try:
            # Substitute variables in formula
            substituted_formula = self._substitute_variables(formula)
            
            # Evaluate the mathematical expression
            result = self.engine.calculate(substituted_formula)
            
            return result
            
        finally:
            if context:
                # Restore original variables
                self.variables = original_vars
    
    def _substitute_variables(self, formula: str) -> str:
        """
        Substitute variables with their values in the formula
        
        Args:
            formula: Formula with variables
            
        Returns:
            Formula with variables replaced by values
        """
        # Pattern to match variable names (F01, F02, etc. or custom names)
        pattern = r'\b([A-Za-z]\w*)\b'
        
        def replace_variable(match):
            var_name = match.group(1)
            if var_name in self.variables:
                return str(self.variables[var_name])
            else:
                raise FormulaCalculationError(f"Variable non définie: {var_name}")
        
        return re.sub(pattern, replace_variable, formula)
    
    def validate_formula(self, formula: str) -> Tuple[bool, str]:
        """
        Validate a formula without evaluating it
        
        Args:
            formula: Formula to validate
            
        Returns:
            Tuple of (is_valid, error_message)
        """
        try:
            # Check for basic syntax
            if not formula or not formula.strip():
                return False, "Formule vide"
                
            # Check for balanced parentheses
            if not self._check_balanced_parentheses(formula):
                return False, "Parenthèses non équilibrées"
                
            # Check for valid characters
            valid_chars = set('0123456789+-*/().')
            for char in formula.replace(' ', ''):
                if not (char.isalnum() or char in valid_chars):
                    return False, f"Caractère invalide: {char}"
                    
            return True, ""
            
        except Exception as e:
            return False, str(e)
    
    def _check_balanced_parentheses(self, formula: str) -> bool:
        """Check if parentheses are balanced in the formula"""
        count = 0
        for char in formula:
            if char == '(':
                count += 1
            elif char == ')':
                count -= 1
                if count < 0:
                    return False
        return count == 0


# Utility functions for common formula operations
def safe_divide(dividend: Decimal, divisor: Decimal) -> Decimal:
    """
    Safe division that handles zero divisor
    
    Args:
        dividend: Number to divide
        divisor: Number to divide by
        
    Returns:
        Result of division or zero if divisor is zero
    """
    if divisor == 0:
        return Decimal('0')
    return dividend / divisor


def percentage(value: Decimal, percentage: Decimal) -> Decimal:
    """
    Calculate percentage of a value
    
    Args:
        value: Base value
        percentage: Percentage (e.g., 15 for 15%)
        
    Returns:
        Percentage of the value
    """
    return value * percentage / Decimal('100')


def round_currency(value: Decimal, decimal_places: int = 2) -> Decimal:
    """
    Round value to currency precision
    
    Args:
        value: Value to round
        decimal_places: Number of decimal places (default 2 for MRU)
        
    Returns:
        Rounded value
    """
    return value.quantize(Decimal('0.' + '0' * decimal_places))