# calculator.py
"""
Advanced Mathematical Expression Calculator for Payroll Systems
Provides comprehensive mathematical expression parsing, evaluation, and optimization
with full integration for payroll calculation needs.

Key Features:
- Safe mathematical expression parsing without eval()
- Advanced operator precedence and function support
- Variable substitution from payroll context
- Mathematical function library (sin, cos, log, sqrt, etc.)
- Payroll-specific functions (F01-F24)
- Decimal precision for currency calculations
- Performance optimization and caching
- Error handling with detailed messages
"""

import re
import math
import logging
import operator
import threading
from decimal import Decimal, InvalidOperation, ROUND_HALF_UP, getcontext
from typing import Union, Dict, List, Optional, Callable, Tuple, Set
# Removed unused import: Any
from functools import lru_cache, wraps
from collections import OrderedDict, defaultdict
import time
from datetime import datetime, date

# Set decimal context for high precision currency calculations
getcontext().prec = 28  # High precision for financial calculations


class CalculatorError(Exception):
    """Base exception for calculator errors"""
    pass


class ExpressionParseError(CalculatorError):
    """Raised when expression cannot be parsed"""
    pass


class VariableNotFoundError(CalculatorError):
    """Raised when a variable is not found in context"""
    pass


class FunctionNotFoundError(CalculatorError):
    """Raised when a function is not found"""
    pass


class DivisionByZeroError(CalculatorError):
    """Raised when division by zero occurs"""
    pass


class Token:
    """Represents a token in the expression"""
    
    def __init__(self, token_type: str, value: str, position: int = 0):
        self.type = token_type  # NUMBER, OPERATOR, FUNCTION, VARIABLE, LPAREN, RPAREN
        self.value = value
        self.position = position
    
    def __repr__(self):
        return f"Token({self.type}, {self.value}, {self.position})"


class ExpressionLexer:
    """
    Tokenizes mathematical expressions with support for:
    - Numbers (integers and decimals)
    - Operators (+, -, *, /, ^, %, etc.)
    - Functions (sin, cos, F01, etc.)
    - Variables (dynamic substitution)
    - Parentheses for grouping
    """
    
    # Token patterns
    TOKEN_PATTERNS = [
        ('NUMBER', r'\d+\.?\d*'),
        ('FUNCTION', r'[A-Za-z][A-Za-z0-9_]*(?=\()'),  # Function followed by (
        ('VARIABLE', r'[A-Za-z][A-Za-z0-9_]*'),  # Variables like F01, BASE_SALARY, etc.
        ('POWER', r'\^|\*\*'),
        ('MULTIPLY', r'\*'),
        ('DIVIDE', r'/'),
        ('MODULO', r'%'),
        ('PLUS', r'\+'),
        ('MINUS', r'-'),
        ('LPAREN', r'\('),
        ('RPAREN', r'\)'),
        ('COMMA', r','),
        ('WHITESPACE', r'\s+'),
    ]
    
    def __init__(self):
        self.pattern = '|'.join(f'(?P<{name}>{pattern})' for name, pattern in self.TOKEN_PATTERNS)
        self.regex = re.compile(self.pattern)
    
    def tokenize(self, expression: str) -> List[Token]:
        """
        Tokenize the mathematical expression
        
        Args:
            expression: Mathematical expression string
            
        Returns:
            List of tokens
            
        Raises:
            ExpressionParseError: If invalid characters found
        """
        tokens = []
        position = 0
        
        for match in self.regex.finditer(expression):
            token_type = match.lastgroup
            token_value = match.group()
            
            if token_type == 'WHITESPACE':
                continue  # Skip whitespace
            
            if token_type == 'POWER':
                token_type = 'OPERATOR'
                token_value = '^'  # Normalize power operator
            elif token_type in ['MULTIPLY', 'DIVIDE', 'MODULO', 'PLUS', 'MINUS']:
                token_type = 'OPERATOR'
            
            tokens.append(Token(token_type, token_value, match.start()))
            position = match.end()
        
        # Check if entire expression was tokenized
        if position < len(expression):
            invalid_char = expression[position]
            raise ExpressionParseError(f"Invalid character '{invalid_char}' at position {position}")
        
        return tokens


class MathematicalFunctions:
    """
    Mathematical function library with comprehensive function support
    """
    
    @staticmethod
    def sin(x: Decimal) -> Decimal:
        """Sine function"""
        return Decimal(str(math.sin(float(x))))
    
    @staticmethod
    def cos(x: Decimal) -> Decimal:
        """Cosine function"""
        return Decimal(str(math.cos(float(x))))
    
    @staticmethod
    def tan(x: Decimal) -> Decimal:
        """Tangent function"""
        return Decimal(str(math.tan(float(x))))
    
    @staticmethod
    def asin(x: Decimal) -> Decimal:
        """Arc sine function"""
        return Decimal(str(math.asin(float(x))))
    
    @staticmethod
    def acos(x: Decimal) -> Decimal:
        """Arc cosine function"""
        return Decimal(str(math.acos(float(x))))
    
    @staticmethod
    def atan(x: Decimal) -> Decimal:
        """Arc tangent function"""
        return Decimal(str(math.atan(float(x))))
    
    @staticmethod
    def sinh(x: Decimal) -> Decimal:
        """Hyperbolic sine function"""
        return Decimal(str(math.sinh(float(x))))
    
    @staticmethod
    def cosh(x: Decimal) -> Decimal:
        """Hyperbolic cosine function"""
        return Decimal(str(math.cosh(float(x))))
    
    @staticmethod
    def tanh(x: Decimal) -> Decimal:
        """Hyperbolic tangent function"""
        return Decimal(str(math.tanh(float(x))))
    
    @staticmethod
    def log(x: Decimal, base: Optional[Decimal] = None) -> Decimal:
        """Natural logarithm or logarithm with base"""
        if x <= 0:
            raise CalculatorError("Logarithm of non-positive number")
        
        if base is None:
            return Decimal(str(math.log(float(x))))
        else:
            if base <= 0 or base == 1:
                raise CalculatorError("Invalid logarithm base")
            return Decimal(str(math.log(float(x), float(base))))
    
    @staticmethod
    def log10(x: Decimal) -> Decimal:
        """Base-10 logarithm"""
        if x <= 0:
            raise CalculatorError("Logarithm of non-positive number")
        return Decimal(str(math.log10(float(x))))
    
    @staticmethod
    def log2(x: Decimal) -> Decimal:
        """Base-2 logarithm"""
        if x <= 0:
            raise CalculatorError("Logarithm of non-positive number")
        return Decimal(str(math.log2(float(x))))
    
    @staticmethod
    def sqrt(x: Decimal) -> Decimal:
        """Square root function"""
        if x < 0:
            raise CalculatorError("Square root of negative number")
        return Decimal(str(math.sqrt(float(x))))
    
    @staticmethod
    def cbrt(x: Decimal) -> Decimal:
        """Cube root function"""
        return Decimal(str(math.pow(float(x), 1/3)))
    
    @staticmethod
    def exp(x: Decimal) -> Decimal:
        """Exponential function (e^x)"""
        return Decimal(str(math.exp(float(x))))
    
    @staticmethod
    def pow(base: Decimal, exponent: Decimal) -> Decimal:
        """Power function"""
        return Decimal(str(math.pow(float(base), float(exponent))))
    
    @staticmethod
    def abs(x: Decimal) -> Decimal:
        """Absolute value function"""
        return abs(x)
    
    @staticmethod
    def floor(x: Decimal) -> Decimal:
        """Floor function"""
        return Decimal(str(math.floor(float(x))))
    
    @staticmethod
    def ceil(x: Decimal) -> Decimal:
        """Ceiling function"""
        return Decimal(str(math.ceil(float(x))))
    
    @staticmethod
    def round(x: Decimal, digits: Decimal = Decimal('0')) -> Decimal:
        """Round function"""
        digits_int = int(digits)
        if digits_int == 0:
            return x.quantize(Decimal('1'), rounding=ROUND_HALF_UP)
        else:
            return x.quantize(Decimal('0.' + '0' * digits_int), rounding=ROUND_HALF_UP)
    
    @staticmethod
    def min(*args) -> Decimal:
        """Minimum function"""
        if not args:
            raise CalculatorError("min() requires at least one argument")
        return min(args)
    
    @staticmethod
    def max(*args) -> Decimal:
        """Maximum function"""
        if not args:
            raise CalculatorError("max() requires at least one argument")
        return max(args)
    
    @staticmethod
    def sum(*args) -> Decimal:
        """Sum function"""
        return sum(args, Decimal('0'))
    
    @staticmethod
    def avg(*args) -> Decimal:
        """Average function"""
        if not args:
            raise CalculatorError("avg() requires at least one argument")
        return sum(args, Decimal('0')) / Decimal(str(len(args)))
    
    @staticmethod
    def factorial(n: Decimal) -> Decimal:
        """Factorial function"""
        n_int = int(n)
        if n != n_int or n_int < 0:
            raise CalculatorError("Factorial requires non-negative integer")
        return Decimal(str(math.factorial(n_int)))
    
    @staticmethod
    def gcd(*args) -> Decimal:
        """Greatest common divisor"""
        if len(args) < 2:
            raise CalculatorError("gcd() requires at least two arguments")
        
        result = int(args[0])
        for arg in args[1:]:
            result = math.gcd(result, int(arg))
        
        return Decimal(str(result))
    
    @staticmethod
    def lcm(*args) -> Decimal:
        """Least common multiple"""
        if len(args) < 2:
            raise CalculatorError("lcm() requires at least two arguments")
        
        def lcm_two(a, b):
            return abs(a * b) // math.gcd(a, b)
        
        result = int(args[0])
        for arg in args[1:]:
            result = lcm_two(result, int(arg))
        
        return Decimal(str(result))


class ExpressionParser:
    """
    Recursive descent parser for mathematical expressions with proper operator precedence
    
    Grammar:
    expression    := term (('+' | '-') term)*
    term          := factor (('*' | '/' | '%') factor)*
    factor        := power ('^' power)*
    power         := unary
    unary         := ('+' | '-')? atom
    atom          := number | function_call | variable | '(' expression ')'
    function_call := FUNCTION '(' [expression (',' expression)*] ')'
    """
    
    # Operator precedence (higher number = higher precedence)
    PRECEDENCE = {
        '+': 1, '-': 1,           # Addition/Subtraction
        '*': 2, '/': 2, '%': 2,   # Multiplication/Division/Modulo
        '^': 3,                   # Exponentiation
        'unary+': 4, 'unary-': 4  # Unary operators
    }
    
    # Operator functions
    OPERATORS = {
        '+': operator.add,
        '-': operator.sub,
        '*': operator.mul,
        '/': operator.truediv,
        '%': operator.mod,
        '^': operator.pow,
    }
    
    def __init__(self, calculator=None):
        self.tokens: List[Token] = []
        self.position = 0
        self.calculator = calculator
        self.math_functions = MathematicalFunctions()
    
    def parse(self, tokens: List[Token]) -> Decimal:
        """
        Parse tokens into an expression tree and evaluate
        
        Args:
            tokens: List of tokens from lexer
            
        Returns:
            Evaluated result as Decimal
            
        Raises:
            ExpressionParseError: If expression is malformed
        """
        self.tokens = tokens
        self.position = 0
        
        if not tokens:
            raise ExpressionParseError("Empty expression")
        
        result = self._parse_expression()
        
        # Ensure all tokens were consumed
        if self.position < len(self.tokens):
            raise ExpressionParseError(f"Unexpected token at position {self.tokens[self.position].position}")
        
        return result
    
    def _current_token(self) -> Optional[Token]:
        """Get current token"""
        if self.position < len(self.tokens):
            return self.tokens[self.position]
        return None
    
    def _consume_token(self, expected_type: str = None) -> Token:
        """Consume and return current token"""
        if self.position >= len(self.tokens):
            raise ExpressionParseError("Unexpected end of expression")
        
        token = self.tokens[self.position]
        
        if expected_type and token.type != expected_type:
            raise ExpressionParseError(f"Expected {expected_type}, got {token.type} at position {token.position}")
        
        self.position += 1
        return token
    
    def _parse_expression(self) -> Decimal:
        """Parse addition and subtraction (lowest precedence)"""
        result = self._parse_term()
        
        while (self._current_token() and 
               self._current_token().type == 'OPERATOR' and 
               self._current_token().value in ['+', '-']):
            
            operator_token = self._consume_token('OPERATOR')
            right = self._parse_term()
            
            if operator_token.value == '+':
                result = result + right
            else:  # operator_token.value == '-'
                result = result - right
        
        return result
    
    def _parse_term(self) -> Decimal:
        """Parse multiplication, division, and modulo"""
        result = self._parse_factor()
        
        while (self._current_token() and 
               self._current_token().type == 'OPERATOR' and 
               self._current_token().value in ['*', '/', '%']):
            
            operator_token = self._consume_token('OPERATOR')
            right = self._parse_factor()
            
            if operator_token.value == '*':
                result = result * right
            elif operator_token.value == '/':
                if right == 0:
                    raise DivisionByZeroError("Division by zero")
                result = result / right
            else:  # operator_token.value == '%'
                if right == 0:
                    raise DivisionByZeroError("Modulo by zero")
                result = result % right
        
        return result
    
    def _parse_factor(self) -> Decimal:
        """Parse exponentiation (right-associative)"""
        result = self._parse_unary()
        
        if (self._current_token() and 
            self._current_token().type == 'OPERATOR' and 
            self._current_token().value == '^'):
            
            self._consume_token('OPERATOR')  # Consume '^'
            right = self._parse_factor()  # Right-associative recursion
            
            try:
                # Use Decimal power method for better precision
                result = result ** right
            except (InvalidOperation, OverflowError) as e:
                raise CalculatorError(f"Power operation error: {str(e)}")
        
        return result
    
    def _parse_unary(self) -> Decimal:
        """Parse unary operators"""
        if (self._current_token() and 
            self._current_token().type == 'OPERATOR' and 
            self._current_token().value in ['+', '-']):
            
            operator_token = self._consume_token('OPERATOR')
            operand = self._parse_unary()  # Allow chaining unary operators
            
            if operator_token.value == '-':
                return -operand
            else:  # operator_token.value == '+'
                return operand
        
        return self._parse_atom()
    
    def _parse_atom(self) -> Decimal:
        """Parse atomic expressions (numbers, variables, functions, parentheses)"""
        current = self._current_token()
        
        if not current:
            raise ExpressionParseError("Unexpected end of expression")
        
        if current.type == 'NUMBER':
            return self._parse_number()
        elif current.type == 'FUNCTION':
            return self._parse_function_call()
        elif current.type == 'VARIABLE':
            return self._parse_variable()
        elif current.type == 'LPAREN':
            return self._parse_parenthesized_expression()
        else:
            raise ExpressionParseError(f"Unexpected token {current.type} at position {current.position}")
    
    def _parse_number(self) -> Decimal:
        """Parse numeric literals"""
        token = self._consume_token('NUMBER')
        try:
            return Decimal(token.value)
        except InvalidOperation:
            raise ExpressionParseError(f"Invalid number format: {token.value}")
    
    def _parse_variable(self) -> Decimal:
        """Parse variable references"""
        token = self._consume_token('VARIABLE')
        
        if self.calculator and hasattr(self.calculator, 'get_variable'):
            try:
                return self.calculator.get_variable(token.value)
            except VariableNotFoundError:
                raise VariableNotFoundError(f"Variable '{token.value}' not found")
        else:
            raise VariableNotFoundError(f"Variable '{token.value}' not found - no calculator context")
    
    def _parse_function_call(self) -> Decimal:
        """Parse function calls with arguments"""
        function_token = self._consume_token('FUNCTION')
        function_name = function_token.value.lower()
        
        # Consume opening parenthesis
        self._consume_token('LPAREN')
        
        # Parse arguments
        args = []
        if (self._current_token() and 
            self._current_token().type != 'RPAREN'):
            
            # Parse first argument
            args.append(self._parse_expression())
            
            # Parse additional arguments
            while (self._current_token() and 
                   self._current_token().type == 'COMMA'):
                self._consume_token('COMMA')
                args.append(self._parse_expression())
        
        # Consume closing parenthesis
        self._consume_token('RPAREN')
        
        # Execute function
        return self._execute_function(function_name, args)
    
    def _parse_parenthesized_expression(self) -> Decimal:
        """Parse parenthesized expressions"""
        self._consume_token('LPAREN')
        result = self._parse_expression()
        self._consume_token('RPAREN')
        return result
    
    def _execute_function(self, function_name: str, args: List[Decimal]) -> Decimal:
        """
        Execute a function call
        
        Args:
            function_name: Name of the function
            args: List of arguments
            
        Returns:
            Function result
            
        Raises:
            FunctionNotFoundError: If function not found
            CalculatorError: If function execution fails
        """
        # Check for payroll functions first (F01-F24)
        if self.calculator and function_name.upper().startswith('F') and len(function_name) == 3:
            return self.calculator.execute_payroll_function(function_name.upper(), args)
        
        # Check mathematical functions
        if hasattr(self.math_functions, function_name):
            func = getattr(self.math_functions, function_name)
            try:
                return func(*args)
            except TypeError as e:
                raise CalculatorError(f"Wrong number of arguments for function '{function_name}': {str(e)}")
            except Exception as e:
                raise CalculatorError(f"Error executing function '{function_name}': {str(e)}")
        
        # Check calculator custom functions
        if self.calculator and hasattr(self.calculator, 'execute_custom_function'):
            return self.calculator.execute_custom_function(function_name, args)
        
        raise FunctionNotFoundError(f"Function '{function_name}' not found")


class CalculatorCache:
    """
    Thread-safe cache for expression evaluation results
    """
    
    def __init__(self, max_size: int = 1000, ttl_seconds: int = 300):
        self.max_size = max_size
        self.ttl_seconds = ttl_seconds
        self._cache = OrderedDict()
        self._timestamps = {}
        self._lock = threading.RLock()
        self._hits = 0
        self._misses = 0
    
    def get(self, key: str) -> Optional[Decimal]:
        """Get cached result if still valid"""
        with self._lock:
            if key not in self._cache:
                self._misses += 1
                return None
            
            # Check TTL
            if time.time() - self._timestamps[key] > self.ttl_seconds:
                del self._cache[key]
                del self._timestamps[key]
                self._misses += 1
                return None
            
            # Move to end (LRU)
            self._cache.move_to_end(key)
            self._hits += 1
            return self._cache[key]
    
    def put(self, key: str, value: Decimal):
        """Cache a result"""
        with self._lock:
            # Remove oldest entries if cache is full
            while len(self._cache) >= self.max_size:
                oldest_key = next(iter(self._cache))
                del self._cache[oldest_key]
                del self._timestamps[oldest_key]
            
            self._cache[key] = value
            self._timestamps[key] = time.time()
    
    def clear(self):
        """Clear all cached results"""
        with self._lock:
            self._cache.clear()
            self._timestamps.clear()
            self._hits = 0
            self._misses = 0
    
    def get_stats(self) -> Dict[str, Any]:
        """Get cache statistics"""
        with self._lock:
            total_requests = self._hits + self._misses
            hit_rate = (self._hits / total_requests * 100) if total_requests > 0 else 0
            
            return {
                'size': len(self._cache),
                'max_size': self.max_size,
                'hits': self._hits,
                'misses': self._misses,
                'hit_rate': round(hit_rate, 2),
                'ttl_seconds': self.ttl_seconds
            }


class MathematicalCalculator:
    """
    Advanced mathematical expression calculator with comprehensive features:
    - Safe expression parsing without eval()
    - Variable substitution from payroll context
    - Mathematical and payroll function support
    - Caching and optimization
    - Error handling with detailed messages
    """
    
    def __init__(self, payroll_context=None, cache_size: int = 1000, cache_ttl: int = 300):
        self.payroll_context = payroll_context
        self.variables = {}
        self.custom_functions = {}
        self.lexer = ExpressionLexer()
        self.parser = ExpressionParser(self)
        self.cache = CalculatorCache(cache_size, cache_ttl)
        self.logger = logging.getLogger(__name__)
        
        # Performance statistics
        self._stats = {
            'evaluations': 0,
            'cache_hits': 0,
            'cache_misses': 0,
            'errors': 0,
            'parsing_time': 0.0,
            'evaluation_time': 0.0
        }
        
        # Initialize standard constants
        self.set_variable('PI', Decimal(str(math.pi)))
        self.set_variable('E', Decimal(str(math.e)))
        self.set_variable('TAU', Decimal(str(2 * math.pi)))
    
    def evaluate(self, expression: str, variables: Dict[str, Union[Decimal, float, int]] = None,
                 use_cache: bool = True) -> Decimal:
        """
        Evaluate a mathematical expression
        
        Args:
            expression: Mathematical expression string
            variables: Optional variables for this evaluation
            use_cache: Whether to use caching
            
        Returns:
            Evaluated result as Decimal
            
        Raises:
            CalculatorError: If evaluation fails
        """
        start_time = time.time()
        self._stats['evaluations'] += 1
        
        try:
            # Normalize expression
            normalized_expr = self._normalize_expression(expression)
            
            # Check cache first
            cache_key = None
            if use_cache:
                cache_key = self._generate_cache_key(normalized_expr, variables)
                cached_result = self.cache.get(cache_key)
                if cached_result is not None:
                    self._stats['cache_hits'] += 1
                    return cached_result
                self._stats['cache_misses'] += 1
            
            # Set temporary variables
            original_vars = self.variables.copy()
            if variables:
                for name, value in variables.items():
                    self.set_variable(name, value)
            
            try:
                # Parse and evaluate
                parse_start = time.time()
                tokens = self.lexer.tokenize(normalized_expr)
                self._stats['parsing_time'] += time.time() - parse_start
                
                eval_start = time.time()
                result = self.parser.parse(tokens)
                self._stats['evaluation_time'] += time.time() - eval_start
                
                # Cache result
                if use_cache and cache_key:
                    self.cache.put(cache_key, result)
                
                return result
                
            finally:
                # Restore original variables
                if variables:
                    self.variables = original_vars
        
        except Exception as e:
            self._stats['errors'] += 1
            self.logger.error(f"Error evaluating expression '{expression}': {str(e)}")
            raise CalculatorError(f"Expression evaluation failed: {str(e)}")
        
        finally:
            self._stats['evaluation_time'] += time.time() - start_time
    
    def set_variable(self, name: str, value: Union[Decimal, float, int]):
        """Set a variable value"""
        self.variables[name] = Decimal(str(value))
    
    def get_variable(self, name: str) -> Decimal:
        """Get a variable value"""
        if name in self.variables:
            return self.variables[name]
        
        # Try to get from payroll context
        if self.payroll_context and hasattr(self.payroll_context, 'get_variable'):
            try:
                return Decimal(str(self.payroll_context.get_variable(name)))
            except Exception:
                pass
        
        raise VariableNotFoundError(f"Variable '{name}' not found")
    
    def set_variables(self, variables: Dict[str, Union[Decimal, float, int]]):
        """Set multiple variables"""
        for name, value in variables.items():
            self.set_variable(name, value)
    
    def add_custom_function(self, name: str, func: Callable):
        """Add a custom function"""
        self.custom_functions[name.lower()] = func
    
    def execute_custom_function(self, name: str, args: List[Decimal]) -> Decimal:
        """Execute a custom function"""
        if name.lower() in self.custom_functions:
            func = self.custom_functions[name.lower()]
            try:
                result = func(*args)
                return Decimal(str(result))
            except Exception as e:
                raise CalculatorError(f"Error executing custom function '{name}': {str(e)}")
        
        raise FunctionNotFoundError(f"Custom function '{name}' not found")
    
    def execute_payroll_function(self, function_code: str, args: List[Decimal]) -> Decimal:
        """Execute a payroll function (F01-F24)"""
        if self.payroll_context and hasattr(self.payroll_context, 'execute_payroll_function'):
            try:
                return self.payroll_context.execute_payroll_function(function_code, args)
            except Exception as e:
                raise CalculatorError(f"Error executing payroll function '{function_code}': {str(e)}")
        
        # Default implementation returns 0 for missing payroll functions
        self.logger.warning(f"Payroll function '{function_code}' not available, returning 0")
        return Decimal('0')
    
    def validate_expression(self, expression: str) -> Tuple[bool, str]:
        """
        Validate an expression without evaluating it
        
        Args:
            expression: Expression to validate
            
        Returns:
            Tuple of (is_valid, error_message)
        """
        try:
            normalized_expr = self._normalize_expression(expression)
            tokens = self.lexer.tokenize(normalized_expr)
            
            # Create a temporary parser for validation
            validator_parser = ExpressionParser(self)
            
            # Try to parse (but don't evaluate)
            # We'll use a mock evaluation that doesn't require actual variable values
            saved_vars = self.variables.copy()
            
            # Set all variables to 0 for validation
            missing_vars = set()
            temp_tokens = []
            
            for token in tokens:
                if token.type == 'VARIABLE' and token.value not in self.variables:
                    missing_vars.add(token.value)
                    self.variables[token.value] = Decimal('0')
                temp_tokens.append(token)
            
            try:
                validator_parser.parse(temp_tokens)
                
                # Check for undefined variables in actual usage
                if missing_vars:
                    return False, f"Undefined variables: {', '.join(sorted(missing_vars))}"
                
                return True, ""
                
            finally:
                # Restore original variables
                self.variables = saved_vars
                for var in missing_vars:
                    if var in self.variables:
                        del self.variables[var]
        
        except Exception as e:
            return False, str(e)
    
    def get_expression_dependencies(self, expression: str) -> Dict[str, List[str]]:
        """
        Get all dependencies (variables and functions) from an expression
        
        Args:
            expression: Expression to analyze
            
        Returns:
            Dictionary with 'variables' and 'functions' lists
        """
        dependencies = {
            'variables': [],
            'functions': []
        }
        
        try:
            normalized_expr = self._normalize_expression(expression)
            tokens = self.lexer.tokenize(normalized_expr)
            
            for token in tokens:
                if token.type == 'VARIABLE' and token.value not in dependencies['variables']:
                    dependencies['variables'].append(token.value)
                elif token.type == 'FUNCTION' and token.value not in dependencies['functions']:
                    dependencies['functions'].append(token.value)
        
        except Exception as e:
            self.logger.warning(f"Error analyzing expression dependencies: {str(e)}")
        
        return dependencies
    
    def optimize_expression(self, expression: str) -> str:
        """
        Optimize an expression by pre-evaluating constant sub-expressions
        
        Args:
            expression: Expression to optimize
            
        Returns:
            Optimized expression string
        """
        # This is a simplified optimization - in practice, you might want
        # more sophisticated constant folding and algebraic simplification
        try:
            # For now, just normalize the expression
            return self._normalize_expression(expression)
        except Exception:
            return expression
    
    def _normalize_expression(self, expression: str) -> str:
        """Normalize expression for consistent parsing"""
        if not expression or not expression.strip():
            raise ExpressionParseError("Empty expression")
        
        # Remove extra whitespace
        normalized = ' '.join(expression.split())
        
        # Replace common variations
        normalized = normalized.replace('**', '^')  # Power operator
        
        return normalized
    
    def _generate_cache_key(self, expression: str, variables: Dict = None) -> str:
        """Generate a cache key for the expression and variables"""
        key_parts = [expression]
        
        if variables:
            # Sort variables for consistent key generation
            var_items = sorted(variables.items())
            key_parts.extend([f"{k}={v}" for k, v in var_items])
        
        # Include current variables that might affect the result
        current_vars = sorted(self.variables.items())
        key_parts.extend([f"ctx_{k}={v}" for k, v in current_vars])
        
        return "|".join(key_parts)
    
    def get_performance_stats(self) -> Dict[str, Any]:
        """Get performance statistics"""
        stats = self._stats.copy()
        stats.update(self.cache.get_stats())
        
        if stats['evaluations'] > 0:
            stats['avg_parsing_time'] = stats['parsing_time'] / stats['evaluations']
            stats['avg_evaluation_time'] = stats['evaluation_time'] / stats['evaluations']
        
        return stats
    
    def clear_cache(self):
        """Clear the expression cache"""
        self.cache.clear()
    
    def reset_stats(self):
        """Reset performance statistics"""
        self._stats = {
            'evaluations': 0,
            'cache_hits': 0,
            'cache_misses': 0,
            'errors': 0,
            'parsing_time': 0.0,
            'evaluation_time': 0.0
        }


class PayrollCalculatorIntegration:
    """
    Integration wrapper for payroll system integration
    Provides seamless integration with existing formula_engine.py
    """
    
    def __init__(self, payroll_functions=None, system_parameters=None):
        self.calculator = MathematicalCalculator()
        self.payroll_functions = payroll_functions
        self.system_parameters = system_parameters or {}
        
        # Set up payroll context
        if payroll_functions:
            self.calculator.payroll_context = PayrollFunctionContext(payroll_functions)
        
        # Initialize system parameters as variables
        for key, value in self.system_parameters.items():
            try:
                self.calculator.set_variable(key.upper(), Decimal(str(value)))
            except (ValueError, TypeError):
                pass  # Skip non-numeric parameters
    
    def evaluate_payroll_formula(self, formula: str, employee=None, motif=None, period=None) -> Decimal:
        """
        Evaluate a payroll formula with employee context
        
        Args:
            formula: Formula string
            employee: Employee instance
            motif: Payroll motif
            period: Calculation period
            
        Returns:
            Evaluated result
        """
        # Build context variables for this evaluation
        context_vars = {}
        
        if self.payroll_functions and employee and motif and period:
            # Add payroll function values
            for i in range(1, 25):  # F01-F24
                func_code = f"F{i:02d}"
                try:
                    if hasattr(self.payroll_functions, func_code.lower()):
                        func_method = getattr(self.payroll_functions, func_code.lower())
                        if callable(func_method):
                            value = func_method(employee, motif, period)
                            context_vars[func_code] = Decimal(str(value))
                except Exception as e:
                    self.calculator.logger.warning(f"Error getting function {func_code}: {str(e)}")
                    context_vars[func_code] = Decimal('0')
        
        return self.calculator.evaluate(formula, context_vars)
    
    def validate_payroll_formula(self, formula: str) -> Tuple[bool, str]:
        """Validate a payroll formula"""
        return self.calculator.validate_expression(formula)
    
    def get_formula_dependencies(self, formula: str) -> Dict[str, List[str]]:
        """Get formula dependencies"""
        return self.calculator.get_expression_dependencies(formula)


class PayrollFunctionContext:
    """
    Context for payroll function execution
    """
    
    def __init__(self, payroll_functions):
        self.payroll_functions = payroll_functions
    
    def execute_payroll_function(self, function_code: str, args: List[Decimal]) -> Decimal:
        """Execute a payroll function"""
        if not self.payroll_functions:
            return Decimal('0')
        
        # Map function code to method name
        func_name = function_code.lower()  # f01, f02, etc.
        
        if hasattr(self.payroll_functions, func_name):
            func_method = getattr(self.payroll_functions, func_name)
            if callable(func_method):
                try:
                    # Payroll functions typically don't take additional args
                    # They get their context from the payroll system
                    return Decimal(str(func_method()))
                except Exception as e:
                    raise CalculatorError(f"Error executing payroll function {function_code}: {str(e)}")
        
        raise FunctionNotFoundError(f"Payroll function {function_code} not found")


# Utility functions for common mathematical operations
def safe_divide(dividend: Union[Decimal, float, int], 
                divisor: Union[Decimal, float, int], 
                default: Union[Decimal, float, int] = 0) -> Decimal:
    """Safe division with default value for zero divisor"""
    dividend = Decimal(str(dividend))
    divisor = Decimal(str(divisor))
    default = Decimal(str(default))
    
    if divisor == 0:
        return default
    return dividend / divisor


def percentage(value: Union[Decimal, float, int], 
               percent: Union[Decimal, float, int]) -> Decimal:
    """Calculate percentage of a value"""
    value = Decimal(str(value))
    percent = Decimal(str(percent))
    return value * percent / Decimal('100')


def round_currency(value: Union[Decimal, float, int], 
                  decimal_places: int = 0) -> Decimal:
    """Round value to currency precision"""
    value = Decimal(str(value))
    if decimal_places == 0:
        return value.quantize(Decimal('1'), rounding=ROUND_HALF_UP)
    else:
        quantizer = Decimal('0.' + '0' * decimal_places)
        return value.quantize(quantizer, rounding=ROUND_HALF_UP)


def compound_interest(principal: Union[Decimal, float, int],
                     rate: Union[Decimal, float, int],
                     time: Union[Decimal, float, int],
                     compounding_frequency: int = 1) -> Decimal:
    """Calculate compound interest"""
    principal = Decimal(str(principal))
    rate = Decimal(str(rate))
    time = Decimal(str(time))
    freq = Decimal(str(compounding_frequency))
    
    # A = P(1 + r/n)^(nt)
    rate_per_period = rate / freq
    exponent = freq * time
    
    return principal * ((1 + rate_per_period) ** exponent)


# Factory function for easy integration
def create_payroll_calculator(payroll_functions=None, system_parameters=None, 
                            cache_size: int = 1000, cache_ttl: int = 300) -> PayrollCalculatorIntegration:
    """
    Factory function to create a configured payroll calculator
    
    Args:
        payroll_functions: PayrollFunctions instance
        system_parameters: System parameters dict
        cache_size: Cache size for expression results
        cache_ttl: Cache TTL in seconds
        
    Returns:
        Configured PayrollCalculatorIntegration instance
    """
    integration = PayrollCalculatorIntegration(payroll_functions, system_parameters)
    integration.calculator.cache.max_size = cache_size
    integration.calculator.cache.ttl_seconds = cache_ttl
    return integration


# Example usage and testing functions
def test_calculator():
    """Test the calculator with various expressions"""
    calc = MathematicalCalculator()
    
    test_cases = [
        "2 + 3 * 4",  # Should be 14
        "(2 + 3) * 4",  # Should be 20
        "2 ^ 3 + 1",  # Should be 9
        "sqrt(16)",  # Should be 4
        "sin(PI / 2)",  # Should be 1
        "log(E)",  # Should be 1
        "max(1, 2, 3, 4, 5)",  # Should be 5
        "min(1, 2, 3, 4, 5)",  # Should be 1
        "avg(1, 2, 3, 4, 5)",  # Should be 3
    ]
    
    for expr in test_cases:
        try:
            result = calc.evaluate(expr)
            print(f"{expr} = {result}")
        except Exception as e:
            print(f"{expr} = ERROR: {str(e)}")


if __name__ == "__main__":
    test_calculator()