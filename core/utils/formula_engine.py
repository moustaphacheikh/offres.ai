# formula_engine.py
"""
Enhanced Mathematical Expression Engine for Payroll Formula Evaluation
Converted from Java Calcul.class and PaieClass.java - complete formula system
Supports payroll element formulas with functions, variables, and cross-references
"""

# Standard library imports
import re
import logging
import time
import threading
from decimal import Decimal, InvalidOperation, ROUND_HALF_UP
from typing import Union, Tuple, Optional, Dict, List, Set, Any
from functools import lru_cache
from collections import defaultdict, OrderedDict

# Cleaned up imports - removed unused datetime, date


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


class PayrollFormulaBuilder:
    """
    Complete payroll formula building system
    Equivalent to PaieClass.formulRubrique, baseRbrique, nombreRbrique
    """
    
    def __init__(self, payroll_functions=None, system_parameters=None):
        self.payroll_functions = payroll_functions
        self.system_parameters = system_parameters
        self.logger = logging.getLogger(__name__)
        self._formula_cache = {}
        self._dependency_graph = defaultdict(set)
        self._reverse_dependency_graph = defaultdict(set)
        self._evaluation_order_cache = {}
        self._cache_lock = threading.RLock()
        self._formula_optimizer = FormulaOptimizer()
        self._system_mapper = SystemRubricMapper()
        
    def build_formula_string(self, payroll_element, employee, motif, section: str, period) -> str:
        """
        Build formula string from components - equivalent to PaieClass.formulRubrique
        
        Args:
            payroll_element: PayrollElement instance
            employee: Employee instance
            motif: PayrollMotif instance
            section: 'B' for base, 'N' for number
            period: Calculation period
            
        Returns:
            Complete mathematical expression string
        """
        cache_key = f"{payroll_element.id}_{section}_{employee.id}_{motif.id}_{period}"
        
        if cache_key in self._formula_cache:
            return self._formula_cache[cache_key]
        
        try:
            # Get formula components for this section
            formula_components = self._get_formula_components(payroll_element, section)
            
            if not formula_components:
                self._formula_cache[cache_key] = "0"
                return "0"
            
            # Build the expression string
            expression_parts = []
            for component in formula_components:
                part = self._process_formula_component(component, employee, motif, period)
                if part is not None:
                    expression_parts.append(str(part))
            
            # Join parts and validate
            expression = ''.join(expression_parts)
            if not expression.strip():
                expression = "0"
            
            self._formula_cache[cache_key] = expression
            return expression
            
        except Exception as e:
            self.logger.error(f"Error building formula for element {payroll_element.id}: {str(e)}")
            self._formula_cache[cache_key] = "0"
            return "0"
    
    def _get_formula_components(self, payroll_element, section):
        """Get formula components ordered by ID"""
        try:
            # This would be implemented with actual Django model queries
            # Using hasattr to check if we have a proper Django model
            if hasattr(payroll_element, 'formulas'):
                return payroll_element.formulas.filter(section=section).order_by('id')
            else:
                # Mock implementation for testing
                return []
        except Exception:
            return []
    
    def _process_formula_component(self, component, employee, motif, period):
        """
        Process individual formula component based on type
        
        Args:
            component: PayrollElementFormula instance
            employee: Employee instance
            motif: PayrollMotif instance
            period: Calculation period
            
        Returns:
            Processed component value
        """
        try:
            component_type = getattr(component, 'component_type', 'O')
            
            if component_type == 'O':  # Operator
                return getattr(component, 'text_value', '')
                
            elif component_type == 'N':  # Number
                numeric_value = getattr(component, 'numeric_value', 0)
                if numeric_value > 0:
                    return str(numeric_value)
                return None
                
            elif component_type == 'F':  # Function
                function_code = getattr(component, 'text_value', '')
                if self.payroll_functions and hasattr(self.payroll_functions, 'calculate_function'):
                    result = self.payroll_functions.calculate_function(function_code, employee, motif, period)
                    return str(result)
                return "0"
                
            elif component_type == 'R':  # Reference to another rubrique
                rubrique_id = getattr(component, 'text_value', '')
                amount = self._get_rubrique_amount(employee, rubrique_id, motif, period)
                return str(amount)
                
        except Exception as e:
            self.logger.warning(f"Error processing component {component}: {str(e)}")
            return "0"
        
        return "0"
    
    def _get_rubrique_amount(self, employee, rubrique_id: str, motif, period) -> Decimal:
        """
        Get amount from referenced rubrique - equivalent to R token resolution
        
        Args:
            employee: Employee instance
            rubrique_id: ID of referenced payroll element
            motif: PayrollMotif instance
            period: Calculation period
            
        Returns:
            Amount from referenced rubrique or 0 if not found
        """
        try:
            # Enhanced R token resolution with system mapping
            actual_rubrique_id = self._system_mapper.get_system_rubric_id(int(rubrique_id))
            if actual_rubrique_id is None:
                actual_rubrique_id = int(rubrique_id)
            
            # Check if we have a payroll calculator instance for cross-reference
            if hasattr(self, '_payroll_calculator') and self._payroll_calculator:
                return self._payroll_calculator.get_rubrique_amount(
                    employee, actual_rubrique_id, motif, period
                )
            
            # Fallback: query the PayrollLineItem model directly
            # This would be implemented with actual Django model queries
            return self._query_rubrique_amount(employee, actual_rubrique_id, motif, period)
            
        except Exception as e:
            self.logger.warning(f"Error getting rubrique amount for {rubrique_id}: {str(e)}")
            return Decimal('0.00')
    
    def _query_rubrique_amount(self, employee, rubrique_id: int, motif, period) -> Decimal:
        """Query rubrique amount from database - to be implemented with Django models"""
        try:
            # This would be implemented with actual Django model queries
            # from core.models import PayrollLineItem
            # line_item = PayrollLineItem.objects.filter(
            #     employee=employee,
            #     payroll_element_id=rubrique_id,
            #     payroll_motif=motif,
            #     period=period
            # ).first()
            # return line_item.amount if line_item else Decimal('0.00')
            return Decimal('0.00')
        except Exception:
            return Decimal('0.00')
    
    def set_payroll_calculator(self, payroll_calculator):
        """Set reference to main payroll calculator for cross-references"""
        self._payroll_calculator = payroll_calculator
    
    def calculate_base(self, payroll_element, employee, motif, period) -> Decimal:
        """
        Calculate base amount - equivalent to PaieClass.baseRbrique
        
        Args:
            payroll_element: PayrollElement instance
            employee: Employee instance
            motif: PayrollMotif instance
            period: Calculation period
            
        Returns:
            Calculated base amount
        """
        try:
            formula_string = self.build_formula_string(payroll_element, employee, motif, 'B', period)
            engine = FormulaEngine()
            return engine.calculate(formula_string)
        except Exception as e:
            self.logger.error(f"Error calculating base for element {payroll_element.id}: {str(e)}")
            return Decimal('0.00')
    
    def calculate_number(self, payroll_element, employee, motif, period) -> Decimal:
        """
        Calculate number/quantity - equivalent to PaieClass.nombreRbrique
        
        Args:
            payroll_element: PayrollElement instance
            employee: Employee instance
            motif: PayrollMotif instance
            period: Calculation period
            
        Returns:
            Calculated number/quantity
        """
        try:
            formula_string = self.build_formula_string(payroll_element, employee, motif, 'N', period)
            engine = FormulaEngine()
            return engine.calculate(formula_string)
        except Exception as e:
            self.logger.error(f"Error calculating number for element {payroll_element.id}: {str(e)}")
            return Decimal('0.00')
    
    def get_formula_string_for_inspection(self, payroll_element, section: str) -> str:
        """
        Get readable formula string for inspection - equivalent to rubriqueFormuleStr
        
        Args:
            payroll_element: PayrollElement instance
            section: 'B' for base, 'N' for number
            
        Returns:
            Human-readable formula string
        """
        try:
            formula_components = self._get_formula_components(payroll_element, section)
            if not formula_components:
                return "0"
            
            parts = []
            for component in formula_components:
                component_type = getattr(component, 'component_type', 'O')
                
                if component_type == 'N':
                    numeric_value = getattr(component, 'numeric_value', 0)
                    parts.append(f" {numeric_value}")
                elif component_type == 'R':
                    rubrique_ref = getattr(component, 'text_value', '')
                    parts.append(f" R{rubrique_ref}")
                else:
                    text_value = getattr(component, 'text_value', '')
                    parts.append(f" {text_value}")
            
            return ''.join(parts).strip()
            
        except Exception as e:
            self.logger.error(f"Error getting formula string for inspection: {str(e)}")
            return "Error"
    
    def check_formula_depends_on_base_salary(self, payroll_element, section: str) -> bool:
        """
        Check if formula depends on base salary - equivalent to formulRubriqueOnSB
        
        Args:
            payroll_element: PayrollElement instance
            section: 'B' for base, 'N' for number
            
        Returns:
            True if formula contains F02, F03, or R1 references
        """
        try:
            formula_string = self.get_formula_string_for_inspection(payroll_element, section)
            return any(token in formula_string for token in ['F02', 'F03', 'R1'])
        except Exception:
            return False
    
    def clear_cache(self):
        """Clear the formula cache"""
        self._formula_cache.clear()


class PayrollFormulaEvaluator:
    """
    Enhanced formula evaluator with complete payroll integration
    Combines mathematical evaluation with payroll functions and cross-references
    """
    
    def __init__(self, payroll_functions=None, system_parameters=None):
        self.engine = FormulaEngine()
        self.formula_builder = PayrollFormulaBuilder(payroll_functions, system_parameters)
        self.variables = {}
        self.context_stack = []  # For nested evaluation contexts
        self.logger = logging.getLogger(__name__)
        self._circular_dependency_detector = CircularDependencyDetector()
        self._evaluation_cache = OrderedDict()
        self._max_cache_size = 1000
        self._cache_ttl = 300  # 5 minutes
        self._performance_stats = {
            'cache_hits': 0,
            'cache_misses': 0,
            'evaluations': 0,
            'errors': 0
        }
        
        # Initialize with payroll functions F01-F24
        self._initialize_payroll_functions()
        
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
    
    def evaluate_payroll_element(self, payroll_element, employee, motif, period) -> Dict[str, Decimal]:
        """
        Evaluate complete payroll element with base and number calculations
        
        Args:
            payroll_element: PayrollElement instance
            employee: Employee instance
            motif: PayrollMotif instance
            period: Calculation period
            
        Returns:
            Dictionary with base, number, and amount calculations
        """
        try:
            base = Decimal('0.00')
            number = Decimal('1.00')
            
            # Calculate base if auto-calculated
            if getattr(payroll_element, 'auto_base_calculation', False):
                base = self.formula_builder.calculate_base(payroll_element, employee, motif, period)
            
            # Calculate number if auto-calculated
            if getattr(payroll_element, 'auto_quantity_calculation', False):
                number = self.formula_builder.calculate_number(payroll_element, employee, motif, period)
            
            # Calculate final amount with rounding
            amount = self._round_amount(base * number)
            
            return {
                'base': base,
                'number': number,
                'amount': amount
            }
            
        except Exception as e:
            self.logger.error(f"Error evaluating payroll element {payroll_element.id}: {str(e)}")
            return {
                'base': Decimal('0.00'),
                'number': Decimal('1.00'),
                'amount': Decimal('0.00')
            }
    
    def _round_amount(self, amount: Decimal) -> Decimal:
        """
        Round amount to nearest currency unit (MRU)
        Equivalent to Math.round in Java
        """
        return amount.quantize(Decimal('1'), rounding=ROUND_HALF_UP)
    
    def push_context(self, context: dict):
        """Push a new evaluation context onto the stack"""
        self.context_stack.append(self.variables.copy())
        self.set_variables(context)
    
    def pop_context(self):
        """Pop the current evaluation context from the stack"""
        if self.context_stack:
            self.variables = self.context_stack.pop()
    
    def evaluate_with_dependencies(self, payroll_elements: List, employee, motif, period) -> Dict[int, Dict[str, Decimal]]:
        """
        Evaluate multiple payroll elements handling cross-dependencies
        
        Args:
            payroll_elements: List of PayrollElement instances
            employee: Employee instance
            motif: PayrollMotif instance
            period: Calculation period
            
        Returns:
            Dictionary mapping element IDs to their calculation results
        """
        results = {}
        evaluated = set()
        pending = {pe.id: pe for pe in payroll_elements}
        
        # Process elements until all are evaluated or circular dependency detected
        max_iterations = len(payroll_elements) * 2
        iteration = 0
        
        while pending and iteration < max_iterations:
            iteration += 1
            made_progress = False
            
            for element_id in list(pending.keys()):
                element = pending[element_id]
                
                # Check if dependencies are satisfied
                if self._are_dependencies_satisfied(element, evaluated):
                    # Evaluate this element
                    result = self.evaluate_payroll_element(element, employee, motif, period)
                    results[element_id] = result
                    evaluated.add(element_id)
                    del pending[element_id]
                    made_progress = True
            
            if not made_progress and pending:
                # Circular dependency or missing references - evaluate remaining with 0 for missing refs
                self.logger.warning(f"Circular dependency detected in elements: {list(pending.keys())}")
                for element_id, element in pending.items():
                    result = self.evaluate_payroll_element(element, employee, motif, period)
                    results[element_id] = result
                break
        
        return results
    
    def _are_dependencies_satisfied(self, element, evaluated: set) -> bool:
        """
        Check if all dependencies for an element are already evaluated
        
        Args:
            element: PayrollElement instance
            evaluated: Set of already evaluated element IDs
            
        Returns:
            True if all dependencies are satisfied
        """
        try:
            # Check base formula dependencies
            if getattr(element, 'auto_base_calculation', False):
                base_deps = self._extract_dependencies(element, 'B')
                if not base_deps.issubset(evaluated):
                    return False
            
            # Check number formula dependencies
            if getattr(element, 'auto_quantity_calculation', False):
                number_deps = self._extract_dependencies(element, 'N')
                if not number_deps.issubset(evaluated):
                    return False
            
            return True
            
        except Exception:
            return True  # If we can't determine dependencies, proceed with evaluation
    
    def _extract_dependencies(self, element, section: str) -> set:
        """
        Extract rubrique dependencies (R tokens) from formula
        
        Args:
            element: PayrollElement instance
            section: 'B' for base, 'N' for number
            
        Returns:
            Set of dependent element IDs
        """
        try:
            formula_string = self.formula_builder.get_formula_string_for_inspection(element, section)
            
            # Extract R tokens (e.g., R1, R2, R10)
            r_pattern = r'R(\d+)'
            matches = re.findall(r_pattern, formula_string)
            return {int(match) for match in matches}
            
        except Exception:
            return set()
    
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
        Enhanced formula validation including function and reference checks
        
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
                
            # Enhanced character validation including functions and references
            valid_chars = set('0123456789+-*/().')
            i = 0
            while i < len(formula):
                char = formula[i]
                if char.isspace():
                    i += 1
                    continue
                    
                if char.isalnum() or char in valid_chars:
                    i += 1
                elif char == 'F':
                    # Check for function format (F01, F02, etc.)
                    if i + 2 < len(formula) and formula[i+1:i+3].isdigit():
                        i += 3
                    else:
                        return False, f"Format de fonction invalide à la position {i}"
                elif char == 'R':
                    # Check for reference format (R1, R2, etc.)
                    j = i + 1
                    while j < len(formula) and formula[j].isdigit():
                        j += 1
                    if j == i + 1:
                        return False, f"Format de référence invalide à la position {i}"
                    i = j
                else:
                    return False, f"Caractère invalide: {char} à la position {i}"
            
            # Additional semantic validation
            validation_result = self._validate_formula_semantics(formula)
            if not validation_result[0]:
                return validation_result
                
            return True, ""
            
        except Exception as e:
            return False, str(e)
    
    def _validate_formula_semantics(self, formula: str) -> Tuple[bool, str]:
        """
        Validate formula semantics (function codes, variable references)
        
        Args:
            formula: Formula to validate
            
        Returns:
            Tuple of (is_valid, error_message)
        """
        try:
            # Check function codes
            function_pattern = r'F(\d{2})'
            functions = re.findall(function_pattern, formula)
            
            valid_functions = {str(i).zfill(2) for i in range(1, 25)}  # F01-F24
            for func_num in functions:
                if func_num not in valid_functions:
                    return False, f"Code de fonction invalide: F{func_num}"
            
            # Check for operator sequence validity
            if self._has_invalid_operator_sequences(formula):
                return False, "Séquence d'opérateurs invalide"
            
            return True, ""
            
        except Exception as e:
            return False, f"Erreur de validation sémantique: {str(e)}"
    
    def _has_invalid_operator_sequences(self, formula: str) -> bool:
        """Check for invalid operator sequences like ++, --, etc."""
        # Remove whitespace and non-operator characters for operator sequence check
        operators_only = re.sub(r'[^+\-*/()]', '', formula)
        
        # Check for consecutive operators (except for unary minus)
        invalid_sequences = ['++', '--', '**', '//', '+-', '-+', '*+', '/+', '*-', '/-']
        return any(seq in operators_only for seq in invalid_sequences)
    
    def validate_element_formulas(self, payroll_element) -> Dict[str, Tuple[bool, str]]:
        """
        Validate all formulas for a payroll element
        
        Args:
            payroll_element: PayrollElement instance
            
        Returns:
            Dictionary with validation results for base and number formulas
        """
        results = {}
        
        try:
            # Validate base formula
            base_formula = self.formula_builder.get_formula_string_for_inspection(payroll_element, 'B')
            if base_formula and base_formula != "0":
                results['base'] = self.validate_formula(base_formula)
            else:
                results['base'] = (True, "")
            
            # Validate number formula
            number_formula = self.formula_builder.get_formula_string_for_inspection(payroll_element, 'N')
            if number_formula and number_formula != "0":
                results['number'] = self.validate_formula(number_formula)
            else:
                results['number'] = (True, "")
            
        except Exception as e:
            error_msg = f"Erreur lors de la validation: {str(e)}"
            results['base'] = (False, error_msg)
            results['number'] = (False, error_msg)
        
        return results
    
    def get_formula_dependencies(self, payroll_element) -> Dict[str, List[str]]:
        """
        Get all dependencies for a payroll element's formulas
        
        Args:
            payroll_element: PayrollElement instance
            
        Returns:
            Dictionary with dependencies for base and number formulas
        """
        dependencies = {'base': [], 'number': []}
        
        try:
            # Get base dependencies
            base_formula = self.formula_builder.get_formula_string_for_inspection(payroll_element, 'B')
            dependencies['base'] = self._extract_all_dependencies(base_formula)
            
            # Get number dependencies
            number_formula = self.formula_builder.get_formula_string_for_inspection(payroll_element, 'N')
            dependencies['number'] = self._extract_all_dependencies(number_formula)
            
        except Exception as e:
            self.logger.error(f"Error extracting dependencies: {str(e)}")
        
        return dependencies
    
    def _extract_all_dependencies(self, formula: str) -> List[str]:
        """
        Extract all dependencies from a formula (functions and references)
        
        Args:
            formula: Formula string
            
        Returns:
            List of dependency identifiers
        """
        dependencies = []
        
        try:
            # Extract function dependencies (F01, F02, etc.)
            function_pattern = r'F(\d{2})'
            functions = re.findall(function_pattern, formula)
            dependencies.extend([f"F{f}" for f in functions])
            
            # Extract reference dependencies (R1, R2, etc.)
            reference_pattern = r'R(\d+)'
            references = re.findall(reference_pattern, formula)
            dependencies.extend([f"R{r}" for r in references])
            
        except Exception as e:
            self.logger.warning(f"Error extracting dependencies from formula: {str(e)}")
        
        return dependencies
    
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
    
    def _initialize_payroll_functions(self):
        """Initialize payroll function evaluations if available"""
        if self.formula_builder.payroll_functions:
            # Store reference for function evaluation
            self._payroll_functions_instance = self.formula_builder.payroll_functions
        else:
            self._payroll_functions_instance = None
    
    def evaluate_formula_with_caching(self, formula: str, cache_key: str = None, context: dict = None) -> Decimal:
        """
        Enhanced formula evaluation with LRU caching and performance tracking
        
        Args:
            formula: Formula string to evaluate
            cache_key: Optional cache key for result caching
            context: Optional context variables
            
        Returns:
            Decimal result of the formula evaluation
        """
        self._performance_stats['evaluations'] += 1
        
        # Check cache first if cache_key provided
        if cache_key:
            cached_result = self._get_cached_result(cache_key)
            if cached_result is not None:
                self._performance_stats['cache_hits'] += 1
                return cached_result
            self._performance_stats['cache_misses'] += 1
        
        try:
            # Evaluate formula
            result = self.evaluate_formula(formula, context)
            
            # Cache result if cache_key provided
            if cache_key:
                self._cache_result(cache_key, result)
            
            return result
            
        except Exception as e:
            self._performance_stats['errors'] += 1
            self.logger.error(f"Error evaluating formula '{formula}': {str(e)}")
            raise
    
    def _get_cached_result(self, cache_key: str) -> Optional[Decimal]:
        """Get cached result if still valid"""
        if cache_key not in self._evaluation_cache:
            return None
        
        cached_entry = self._evaluation_cache[cache_key]
        current_time = time.time()
        
        if current_time > cached_entry['expiry']:
            # Cache expired
            del self._evaluation_cache[cache_key]
            return None
        
        # Move to end for LRU
        self._evaluation_cache.move_to_end(cache_key)
        return cached_entry['result']
    
    def _cache_result(self, cache_key: str, result: Decimal):
        """Cache evaluation result with TTL and LRU eviction"""
        # Remove oldest entries if cache is full
        while len(self._evaluation_cache) >= self._max_cache_size:
            oldest_key = next(iter(self._evaluation_cache))
            del self._evaluation_cache[oldest_key]
        
        expiry_time = time.time() + self._cache_ttl
        self._evaluation_cache[cache_key] = {
            'result': result,
            'expiry': expiry_time
        }
    
    def get_performance_stats(self) -> Dict[str, int]:
        """Get performance statistics"""
        stats = self._performance_stats.copy()
        stats['cache_size'] = len(self._evaluation_cache)
        stats['hit_ratio'] = (
            self._performance_stats['cache_hits'] / 
            max(1, self._performance_stats['cache_hits'] + self._performance_stats['cache_misses'])
        ) * 100
        return stats
    
    def clear_caches(self):
        """Clear all caches and reset performance stats"""
        self._evaluation_cache.clear()
        self.formula_builder.clear_cache()
        self._performance_stats = {
            'cache_hits': 0,
            'cache_misses': 0,
            'evaluations': 0,
            'errors': 0
        }
    
    def bulk_evaluate_elements(self, payroll_elements: List, employee, motif, period, 
                             use_dependency_optimization: bool = True) -> Dict[int, Dict[str, Decimal]]:
        """
        Enhanced bulk evaluation with dependency optimization and parallel processing capability
        
        Args:
            payroll_elements: List of PayrollElement instances
            employee: Employee instance
            motif: PayrollMotif instance
            period: Calculation period
            use_dependency_optimization: Whether to use dependency-based ordering
            
        Returns:
            Dictionary mapping element IDs to their calculation results
        """
        if use_dependency_optimization:
            return self._evaluate_with_dependency_optimization(payroll_elements, employee, motif, period)
        else:
            return self.evaluate_with_dependencies(payroll_elements, employee, motif, period)
    
    def _evaluate_with_dependency_optimization(self, payroll_elements: List, employee, motif, period) -> Dict[int, Dict[str, Decimal]]:
        """
        Evaluate payroll elements with advanced dependency optimization
        
        Args:
            payroll_elements: List of PayrollElement instances
            employee: Employee instance
            motif: PayrollMotif instance
            period: Calculation period
            
        Returns:
            Dictionary mapping element IDs to their calculation results
        """
        # Build dependency graph
        dependency_graph = {}
        for element in payroll_elements:
            deps = self._extract_dependencies(element, 'B').union(
                self._extract_dependencies(element, 'N')
            )
            dependency_graph[element.id] = deps
        
        # Get optimal evaluation order
        evaluation_order = self._circular_dependency_detector.get_evaluation_order(dependency_graph)
        
        # Evaluate in order
        results = {}
        element_map = {pe.id: pe for pe in payroll_elements}
        
        for element_id in evaluation_order:
            if element_id in element_map:
                element = element_map[element_id]
                result = self.evaluate_payroll_element(element, employee, motif, period)
                results[element_id] = result
        
        return results


# ========== CIRCULAR DEPENDENCY DETECTION ==========

class CircularDependencyDetector:
    """
    Circular dependency detection for payroll element formulas
    Uses topological sorting to detect and resolve dependency cycles
    """
    
    def __init__(self):
        self.logger = logging.getLogger(__name__)
    
    def detect_cycles(self, dependency_graph: Dict[int, Set[int]]) -> List[List[int]]:
        """
        Detect circular dependencies in the dependency graph
        
        Args:
            dependency_graph: Graph where keys depend on values in their sets
            
        Returns:
            List of cycles (each cycle is a list of element IDs)
        """
        cycles = []
        visited = set()
        rec_stack = set()
        
        def dfs_visit(node: int, path: List[int]):
            if node in rec_stack:
                # Found a cycle
                cycle_start = path.index(node)
                cycle = path[cycle_start:] + [node]
                cycles.append(cycle)
                return
            
            if node in visited:
                return
            
            visited.add(node)
            rec_stack.add(node)
            path.append(node)
            
            for neighbor in dependency_graph.get(node, set()):
                dfs_visit(neighbor, path.copy())
            
            rec_stack.remove(node)
        
        for node in dependency_graph:
            if node not in visited:
                dfs_visit(node, [])
        
        return cycles
    
    def get_evaluation_order(self, dependency_graph: Dict[int, Set[int]]) -> List[int]:
        """
        Get topological order for evaluation (dependencies first)
        
        Args:
            dependency_graph: Graph where keys depend on values in their sets
            
        Returns:
            List of element IDs in evaluation order
        """
        # Create reverse graph (dependents)
        reverse_graph = defaultdict(set)
        in_degree = defaultdict(int)
        
        # Initialize all nodes
        all_nodes = set(dependency_graph.keys())
        for deps in dependency_graph.values():
            all_nodes.update(deps)
        
        for node in all_nodes:
            in_degree[node] = 0
        
        # Build reverse graph and calculate in-degrees
        for node, dependencies in dependency_graph.items():
            for dep in dependencies:
                reverse_graph[dep].add(node)
                in_degree[node] += 1
        
        # Kahn's algorithm for topological sorting
        queue = [node for node in all_nodes if in_degree[node] == 0]
        result = []
        
        while queue:
            node = queue.pop(0)
            result.append(node)
            
            for dependent in reverse_graph[node]:
                in_degree[dependent] -= 1
                if in_degree[dependent] == 0:
                    queue.append(dependent)
        
        # Check if all nodes were processed (no cycles)
        if len(result) != len(all_nodes):
            cycles = self.detect_cycles(dependency_graph)
            self.logger.warning(f"Circular dependencies detected: {cycles}")
            
            # Add remaining nodes (those in cycles) to the end
            remaining = all_nodes - set(result)
            result.extend(remaining)
        
        return result
    
    def break_cycles(self, dependency_graph: Dict[int, Set[int]], 
                    priority_elements: Set[int] = None) -> Dict[int, Set[int]]:
        """
        Break circular dependencies by removing edges based on priority
        
        Args:
            dependency_graph: Original dependency graph
            priority_elements: Set of high-priority elements to prefer
            
        Returns:
            Modified dependency graph without cycles
        """
        cycles = self.detect_cycles(dependency_graph)
        if not cycles:
            return dependency_graph
        
        modified_graph = {k: v.copy() for k, v in dependency_graph.items()}
        priority_elements = priority_elements or set()
        
        for cycle in cycles:
            # Find best edge to remove (prefer removing dependencies of lower priority elements)
            best_edge = None
            min_priority_score = float('inf')
            
            for i in range(len(cycle) - 1):
                from_node = cycle[i]
                to_node = cycle[i + 1]
                
                # Calculate priority score (lower is better for removal)
                priority_score = 0
                if from_node not in priority_elements:
                    priority_score += 1
                if to_node in priority_elements:
                    priority_score += 1
                
                if priority_score < min_priority_score:
                    min_priority_score = priority_score
                    best_edge = (from_node, to_node)
            
            # Remove the best edge
            if best_edge:
                from_node, to_node = best_edge
                if from_node in modified_graph and to_node in modified_graph[from_node]:
                    modified_graph[from_node].remove(to_node)
                    self.logger.info(f"Broke cycle by removing dependency: {from_node} -> {to_node}")
        
        return modified_graph


# ========== ENHANCED INTEGRATION LAYER ==========

class PayrollIntegrationLayer:
    """
    Integration layer between formula engine and payroll processing
    Handles coordination between formula evaluation and payroll calculations
    """
    
    def __init__(self, payroll_functions=None, system_parameters=None):
        self.formula_evaluator = PayrollFormulaEvaluator(payroll_functions, system_parameters)
        self.dependency_detector = CircularDependencyDetector()
        self.logger = logging.getLogger(__name__)
        self._element_cache = {}
        self._dependency_cache = {}
    
    def process_payroll_batch(self, employees: List, payroll_elements: List, 
                            motif, period) -> Dict[int, Dict[int, Dict[str, Decimal]]]:
        """
        Process payroll for multiple employees with optimized evaluation
        
        Args:
            employees: List of Employee instances
            payroll_elements: List of PayrollElement instances
            motif: PayrollMotif instance
            period: Calculation period
            
        Returns:
            Nested dict: {employee_id: {element_id: {base, number, amount}}}
        """
        results = {}
        
        # Build dependency graph once for all employees
        dependency_graph = self._build_dependency_graph(payroll_elements)
        evaluation_order = self.dependency_detector.get_evaluation_order(dependency_graph)
        
        for employee in employees:
            try:
                employee_results = self._process_employee_payroll(
                    employee, payroll_elements, motif, period, evaluation_order
                )
                results[employee.id] = employee_results
                
            except Exception as e:
                self.logger.error(f"Error processing payroll for employee {employee.id}: {str(e)}")
                results[employee.id] = {}
        
        return results
    
    def _process_employee_payroll(self, employee, payroll_elements: List, motif, period, 
                                evaluation_order: List[int]) -> Dict[int, Dict[str, Decimal]]:
        """Process payroll for a single employee using dependency order"""
        results = {}
        element_map = {pe.id: pe for pe in payroll_elements}
        
        # Set up cross-reference context for this employee
        self.formula_evaluator.formula_builder.set_payroll_calculator(self)
        
        for element_id in evaluation_order:
            if element_id in element_map:
                element = element_map[element_id]
                try:
                    # Create cache key for this evaluation
                    cache_key = f"emp_{employee.id}_elem_{element_id}_motif_{motif.id}_period_{period}"
                    
                    # Evaluate with caching
                    result = self.formula_evaluator.evaluate_formula_with_caching(
                        "", cache_key, self._build_evaluation_context(employee, element, motif, period)
                    )
                    
                    # Actually evaluate the element properly
                    element_result = self.formula_evaluator.evaluate_payroll_element(
                        element, employee, motif, period
                    )
                    results[element_id] = element_result
                    
                    # Store result for cross-references
                    self._store_element_result(employee, element, motif, period, element_result)
                    
                except Exception as e:
                    self.logger.error(f"Error evaluating element {element_id} for employee {employee.id}: {str(e)}")
                    results[element_id] = {'base': Decimal('0.00'), 'number': Decimal('1.00'), 'amount': Decimal('0.00')}
        
        return results
    
    def _build_dependency_graph(self, payroll_elements: List) -> Dict[int, Set[int]]:
        """Build dependency graph for payroll elements"""
        graph = {}
        
        for element in payroll_elements:
            dependencies = set()
            
            # Get dependencies from formula evaluator
            element_deps = self.formula_evaluator.get_formula_dependencies(element)
            
            # Extract R token dependencies (references to other elements)
            for section_deps in element_deps.values():
                for dep in section_deps:
                    if dep.startswith('R'):
                        try:
                            ref_id = int(dep[1:])  # Remove 'R' prefix
                            dependencies.add(ref_id)
                        except ValueError:
                            continue
            
            graph[element.id] = dependencies
        
        return graph
    
    def _build_evaluation_context(self, employee, element, motif, period) -> Dict[str, Decimal]:
        """Build evaluation context with payroll functions"""
        context = {}
        
        # Add payroll function values if available
        if self.formula_evaluator._payroll_functions_instance:
            pf = self.formula_evaluator._payroll_functions_instance
            
            # Standard payroll functions F01-F24
            context.update({
                'F01': pf.F01_NJT(employee, motif, period),
                'F02': pf.F02_sbJour(employee, motif, period),
                'F03': pf.F03_sbHoraire(employee, motif, period),
                'F04': pf.F04_TauxAnciennete(employee, period),
                # Add more functions as needed...
            })
        
        return context
    
    def _store_element_result(self, employee, element, motif, period, result):
        """Store element result for cross-references"""
        key = f"{employee.id}_{element.id}_{motif.id}_{period}"
        self._element_cache[key] = result['amount']
    
    def get_rubrique_amount(self, employee, rubrique_id: int, motif, period) -> Decimal:
        """Get rubrique amount for cross-references"""
        key = f"{employee.id}_{rubrique_id}_{motif.id}_{period}"
        return self._element_cache.get(key, Decimal('0.00'))


# ========== SYSTEM RUBRIC MAPPING AND UTILITIES ==========

class SystemRubricMapper:
    """
    System rubric mapping - equivalent to PaieClass.usedRubID
    Maps logical system IDs to actual payroll element IDs
    """
    
    def __init__(self):
        self._mapping_cache = {}
        self.logger = logging.getLogger(__name__)
    
    @lru_cache(maxsize=128)
    def get_system_rubric_id(self, system_id: int) -> Optional[int]:
        """
        Get actual rubric ID for system ID - equivalent to usedRubID
        
        Args:
            system_id: System rubric ID (1=Base Salary, 3-6=Overtime, etc.)
            
        Returns:
            Actual PayrollElement ID or None if not found
        """
        try:
            # This would query the SystemRubric model in actual implementation
            # For now, return default mapping
            default_mapping = {
                1: 1,   # Base salary
                2: 2,   # Variable component
                3: 3,   # Overtime 115%
                4: 4,   # Overtime 140%
                5: 5,   # Overtime 150%
                6: 6,   # Overtime 200%
                7: 7,   # Meal allowance
                8: 8,   # Distance allowance
                19: 19, # Compensation increase helper
                20: 20, # Compensation increase helper 2
            }
            
            return default_mapping.get(system_id)
            
        except Exception as e:
            self.logger.error(f"Error getting system rubric ID {system_id}: {str(e)}")
            return None
    
    def update_mapping(self, system_id: int, actual_id: int):
        """Update the system rubric mapping"""
        self._mapping_cache[system_id] = actual_id
        # Clear LRU cache to force refresh
        self.get_system_rubric_id.cache_clear()
    
    def clear_cache(self):
        """Clear the mapping cache"""
        self._mapping_cache.clear()
        self.get_system_rubric_id.cache_clear()


class PayrollAmountCalculator:
    """
    Payroll amount calculation utilities
    Handles rounding, currency conversion, and amount adjustments
    """
    
    @staticmethod
    def round_payroll_amount(amount: Decimal) -> Decimal:
        """
        Round payroll amount to nearest MRU (Mauritanian Ouguiya)
        Equivalent to Math.round in Java - rounds to nearest integer
        
        Args:
            amount: Amount to round
            
        Returns:
            Rounded amount
        """
        return amount.quantize(Decimal('1'), rounding=ROUND_HALF_UP)
    
    @staticmethod
    def calculate_percentage(base_amount: Decimal, percentage: Decimal) -> Decimal:
        """
        Calculate percentage of an amount with proper rounding
        
        Args:
            base_amount: Base amount
            percentage: Percentage rate (e.g., 15.5 for 15.5%)
            
        Returns:
            Calculated percentage amount
        """
        result = base_amount * percentage / Decimal('100')
        return PayrollAmountCalculator.round_payroll_amount(result)
    
    @staticmethod
    def apply_exchange_rate(amount: Decimal, rate: Decimal = Decimal('1.0')) -> Decimal:
        """
        Apply currency exchange rate
        
        Args:
            amount: Original amount
            rate: Exchange rate (default 1.0)
            
        Returns:
            Amount after exchange rate application
        """
        return PayrollAmountCalculator.round_payroll_amount(amount * rate)
    
    @staticmethod
    def safe_divide(dividend: Decimal, divisor: Decimal, default: Decimal = Decimal('0.00')) -> Decimal:
        """
        Safe division with default value for zero divisor
        
        Args:
            dividend: Number to divide
            divisor: Number to divide by
            default: Default value when divisor is zero
            
        Returns:
            Division result or default value
        """
        if divisor == 0:
            return default
        return dividend / divisor
    
    @staticmethod
    def ensure_positive(amount: Decimal) -> Decimal:
        """
        Ensure amount is not negative
        
        Args:
            amount: Amount to check
            
        Returns:
            Maximum of amount and zero
        """
        return max(Decimal('0.00'), amount)


class FormulaOptimizer:
    """
    Formula optimization and caching utilities
    Provides performance enhancements for repeated formula evaluations
    """
    
    def __init__(self, max_cache_size: int = 1000):
        self.max_cache_size = max_cache_size
        self._evaluation_cache = {}
        self._dependency_cache = {}
        self.logger = logging.getLogger(__name__)
    
    @lru_cache(maxsize=500)
    def is_static_formula(self, formula_string: str) -> bool:
        """
        Check if formula contains only static values (no functions or references)
        
        Args:
            formula_string: Formula to analyze
            
        Returns:
            True if formula is static (contains only numbers and operators)
        """
        # Remove whitespace and check for function/reference patterns
        clean_formula = re.sub(r'\s+', '', formula_string)
        
        # Check for functions (F01, F02, etc.) or references (R1, R2, etc.)
        has_functions = bool(re.search(r'F\d+', clean_formula))
        has_references = bool(re.search(r'R\d+', clean_formula))
        
        return not (has_functions or has_references)
    
    def cache_evaluation_result(self, cache_key: str, result: Decimal, ttl_seconds: int = 300):
        """
        Cache evaluation result with TTL
        
        Args:
            cache_key: Unique key for this evaluation
            result: Calculated result
            ttl_seconds: Time to live in seconds
        """
        if len(self._evaluation_cache) >= self.max_cache_size:
            # Simple LRU - remove oldest entry
            oldest_key = next(iter(self._evaluation_cache))
            del self._evaluation_cache[oldest_key]
        
        import time
        expiry_time = time.time() + ttl_seconds
        self._evaluation_cache[cache_key] = {
            'result': result,
            'expiry': expiry_time
        }
    
    def get_cached_result(self, cache_key: str) -> Optional[Decimal]:
        """
        Get cached evaluation result if still valid
        
        Args:
            cache_key: Cache key
            
        Returns:
            Cached result or None if not found/expired
        """
        if cache_key not in self._evaluation_cache:
            return None
        
        import time
        cached_entry = self._evaluation_cache[cache_key]
        
        if time.time() > cached_entry['expiry']:
            # Cache expired
            del self._evaluation_cache[cache_key]
            return None
        
        return cached_entry['result']
    
    def clear_cache(self):
        """Clear all caches"""
        self._evaluation_cache.clear()
        self._dependency_cache.clear()
        self.is_static_formula.cache_clear()
    
    def get_cache_stats(self) -> Dict[str, Any]:
        """Get cache statistics"""
        import time
        current_time = time.time()
        expired_count = 0
        
        for entry in self._evaluation_cache.values():
            if current_time > entry['expiry']:
                expired_count += 1
        
        return {
            'cache_size': len(self._evaluation_cache),
            'max_cache_size': self.max_cache_size,
            'expired_entries': expired_count,
            'static_formula_cache_size': self.is_static_formula.cache_info().currsize if hasattr(self.is_static_formula, 'cache_info') else 0
        }
    
    def cleanup_expired_entries(self):
        """Remove expired cache entries"""
        import time
        current_time = time.time()
        expired_keys = []
        
        for key, entry in self._evaluation_cache.items():
            if current_time > entry['expiry']:
                expired_keys.append(key)
        
        for key in expired_keys:
            del self._evaluation_cache[key]
        
        return len(expired_keys)


# ========== ENHANCED UTILITY FUNCTIONS ==========

def safe_divide(dividend: Decimal, divisor: Decimal, default: Decimal = Decimal('0.00')) -> Decimal:
    """
    Enhanced safe division with configurable default value
    
    Args:
        dividend: Number to divide
        divisor: Number to divide by
        default: Default value when divisor is zero
        
    Returns:
        Result of division or default value if divisor is zero
    """
    if divisor == 0:
        return default
    return dividend / divisor


def percentage(value: Decimal, percentage: Decimal) -> Decimal:
    """
    Calculate percentage of a value with proper rounding
    
    Args:
        value: Base value
        percentage: Percentage (e.g., 15 for 15%)
        
    Returns:
        Percentage of the value, rounded to currency precision
    """
    result = value * percentage / Decimal('100')
    return PayrollAmountCalculator.round_payroll_amount(result)


def round_currency(value: Decimal, decimal_places: int = 0) -> Decimal:
    """
    Round value to currency precision (MRU has no decimal places)
    
    Args:
        value: Value to round
        decimal_places: Number of decimal places (default 0 for MRU)
        
    Returns:
        Rounded value
    """
    if decimal_places == 0:
        return PayrollAmountCalculator.round_payroll_amount(value)
    else:
        return value.quantize(Decimal('0.' + '0' * decimal_places), rounding=ROUND_HALF_UP)


def validate_formula_syntax(formula: str) -> Tuple[bool, str]:
    """
    Quick formula syntax validation utility
    
    Args:
        formula: Formula string to validate
        
    Returns:
        Tuple of (is_valid, error_message)
    """
    evaluator = PayrollFormulaEvaluator()
    return evaluator.validate_formula(formula)


def extract_formula_functions(formula: str) -> List[str]:
    """
    Extract all function codes from a formula
    
    Args:
        formula: Formula string
        
    Returns:
        List of function codes (e.g., ['F01', 'F02'])
    """
    pattern = r'F(\d{2})'
    matches = re.findall(pattern, formula)
    return [f'F{match}' for match in matches]


def extract_formula_references(formula: str) -> List[str]:
    """
    Extract all rubrique references from a formula
    
    Args:
        formula: Formula string
        
    Returns:
        List of reference codes (e.g., ['R1', 'R2'])
    """
    pattern = r'R(\d+)'
    matches = re.findall(pattern, formula)
    return [f'R{match}' for match in matches]


@lru_cache(maxsize=100)
def get_formula_complexity_score(formula: str) -> int:
    """
    Calculate complexity score for a formula (for optimization decisions)
    
    Args:
        formula: Formula string
        
    Returns:
        Complexity score (higher = more complex)
    """
    if not formula or formula.strip() == "0":
        return 0
    
    score = 0
    
    # Base complexity from length
    score += len(formula) // 10
    
    # Complexity from operators
    operators = ['+', '-', '*', '/', '(', ')']
    score += sum(formula.count(op) for op in operators)
    
    # Complexity from functions
    score += len(extract_formula_functions(formula)) * 3
    
    # Complexity from references
    score += len(extract_formula_references(formula)) * 2
    
    return score


# ========== FORMULA ENGINE FACTORY ==========

class FormulaEngineFactory:
    """
    Factory class for creating configured formula engine instances
    Provides easy integration with different payroll system configurations
    """
    
    @staticmethod
    def create_standard_evaluator(payroll_functions=None, system_parameters=None) -> PayrollFormulaEvaluator:
        """
        Create a standard payroll formula evaluator with default settings
        
        Args:
            payroll_functions: PayrollFunctions instance
            system_parameters: System parameters dict
            
        Returns:
            Configured PayrollFormulaEvaluator instance
        """
        evaluator = PayrollFormulaEvaluator(payroll_functions, system_parameters)
        
        # Set standard payroll function mappings if available
        if payroll_functions:
            try:
                # Initialize with all available functions
                for i in range(1, 25):  # F01-F24
                    func_name = f"F{i:02d}"
                    if hasattr(payroll_functions, func_name + "_function") or hasattr(payroll_functions, func_name.lower()):
                        evaluator.set_variable(func_name, Decimal('0'))
            except Exception:
                pass  # Ignore if function discovery fails
        
        return evaluator
    
    @staticmethod
    def create_integration_layer(payroll_functions=None, system_parameters=None) -> PayrollIntegrationLayer:
        """
        Create a payroll integration layer for batch processing
        
        Args:
            payroll_functions: PayrollFunctions instance
            system_parameters: System parameters dict
            
        Returns:
            Configured PayrollIntegrationLayer instance
        """
        return PayrollIntegrationLayer(payroll_functions, system_parameters)
    
    @staticmethod
    def create_formula_builder(payroll_functions=None, system_parameters=None) -> PayrollFormulaBuilder:
        """
        Create a payroll formula builder for individual element processing
        
        Args:
            payroll_functions: PayrollFunctions instance
            system_parameters: System parameters dict
            
        Returns:
            Configured PayrollFormulaBuilder instance
        """
        return PayrollFormulaBuilder(payroll_functions, system_parameters)
    
    @staticmethod
    def create_optimized_evaluator(max_cache_size: int = 1000, cache_ttl: int = 300,
                                 payroll_functions=None, system_parameters=None) -> PayrollFormulaEvaluator:
        """
        Create an optimized formula evaluator with custom cache settings
        
        Args:
            max_cache_size: Maximum number of cached results
            cache_ttl: Cache time-to-live in seconds
            payroll_functions: PayrollFunctions instance
            system_parameters: System parameters dict
            
        Returns:
            Configured PayrollFormulaEvaluator instance with optimized caching
        """
        evaluator = PayrollFormulaEvaluator(payroll_functions, system_parameters)
        evaluator._max_cache_size = max_cache_size
        evaluator._cache_ttl = cache_ttl
        
        return evaluator
    
    @staticmethod
    def validate_system_configuration(payroll_functions=None, system_parameters=None) -> Dict[str, Any]:
        """
        Validate system configuration for formula processing
        
        Args:
            payroll_functions: PayrollFunctions instance to validate
            system_parameters: System parameters to validate
            
        Returns:
            Validation results with warnings and recommendations
        """
        results = {
            'valid': True,
            'warnings': [],
            'recommendations': [],
            'function_coverage': {},
            'parameter_coverage': {}
        }
        
        # Validate payroll functions
        if payroll_functions:
            expected_functions = [f"F{i:02d}" for i in range(1, 25)]
            available_functions = []
            
            for func_code in expected_functions:
                # Check if function method exists
                func_method_name = f"{func_code}_{func_code.replace('F', '').lstrip('0') or '0'}"
                if hasattr(payroll_functions, func_method_name) or hasattr(payroll_functions, func_code.lower()):
                    available_functions.append(func_code)
            
            results['function_coverage'] = {
                'available': available_functions,
                'missing': list(set(expected_functions) - set(available_functions)),
                'coverage_percentage': len(available_functions) / len(expected_functions) * 100
            }
            
            if len(available_functions) < len(expected_functions):
                results['warnings'].append(f"Missing {len(expected_functions) - len(available_functions)} payroll functions")
        else:
            results['warnings'].append("No payroll functions instance provided")
            results['function_coverage']['coverage_percentage'] = 0
        
        # Validate system parameters
        if system_parameters:
            expected_params = ['base_currency', 'rounding_precision', 'calculation_mode']
            available_params = [param for param in expected_params if param in system_parameters]
            
            results['parameter_coverage'] = {
                'available': available_params,
                'missing': list(set(expected_params) - set(available_params))
            }
            
            if len(available_params) < len(expected_params):
                results['warnings'].append(f"Missing {len(expected_params) - len(available_params)} system parameters")
        else:
            results['warnings'].append("No system parameters provided")
            results['parameter_coverage'] = {
                'available': [], 
                'missing': ['base_currency', 'rounding_precision', 'calculation_mode']
            }
        
        # Add recommendations
        if results['function_coverage'].get('coverage_percentage', 0) < 80:
            results['recommendations'].append("Consider implementing missing payroll functions for full compatibility")
        
        if not system_parameters:
            results['recommendations'].append("Provide system parameters for optimal performance")
        
        # Set overall validity
        results['valid'] = len(results['warnings']) == 0
        
        return results