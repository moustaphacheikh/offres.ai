# Advanced Mathematical Calculator Implementation Summary

## Overview

The `core/utils/calculator.py` module provides a comprehensive mathematical expression parser and evaluator specifically designed for payroll system needs. It offers safe mathematical evaluation without using Python's `eval()` function, ensuring security and reliability.

## Key Features

### 1. Mathematical Capabilities
- **Expression Parsing**: Advanced recursive descent parser with proper operator precedence
- **Safe Evaluation**: No use of `eval()` - custom parsing and evaluation engine
- **Operator Support**: `+`, `-`, `*`, `/`, `%`, `^` (power), unary operators
- **Parentheses**: Full support for grouped expressions with nested parentheses
- **Decimal Precision**: High-precision decimal arithmetic for currency calculations

### 2. Mathematical Functions Library
- **Trigonometric**: `sin()`, `cos()`, `tan()`, `asin()`, `acos()`, `atan()`, `sinh()`, `cosh()`, `tanh()`
- **Logarithmic**: `log()`, `log10()`, `log2()`, `exp()`
- **Power/Root**: `sqrt()`, `cbrt()`, `pow()`
- **Rounding**: `round()`, `floor()`, `ceil()`, `abs()`
- **Statistical**: `min()`, `max()`, `avg()`, `sum()`
- **Advanced**: `factorial()`, `gcd()`, `lcm()`

### 3. Variable Substitution
- **Dynamic Variables**: Support for variable names with underscores (e.g., `BASE_SALARY`, `OVERTIME_HOURS`)
- **Payroll Context**: Integration with payroll system for automatic variable resolution
- **Multiple Variables**: Set multiple variables at once for complex calculations
- **Constants**: Pre-defined mathematical constants (`PI`, `E`, `TAU`)

### 4. Payroll Function Support
- **F01-F24 Functions**: Integration with existing payroll functions from `payroll_calculations.py`
- **Custom Functions**: Ability to add custom functions for specific business logic
- **Error Handling**: Graceful handling of missing or invalid functions

### 5. Performance Optimization
- **Caching**: LRU cache with TTL for expression evaluation results
- **Threading Support**: Thread-safe operations for concurrent payroll processing
- **Performance Statistics**: Detailed metrics for cache hits, evaluation times, etc.

### 6. Error Handling
- **Detailed Error Messages**: Comprehensive error reporting with position information
- **Exception Types**: Specific exceptions for different error conditions
- **Validation**: Expression validation without evaluation for syntax checking

## Core Classes

### `MathematicalCalculator`
Main calculator class that provides the primary interface for expression evaluation.

```python
from core.utils.calculator import MathematicalCalculator

calc = MathematicalCalculator()
calc.set_variable('SALARY', 50000)
result = calc.evaluate('SALARY * 0.15 + sqrt(100)')  # Returns 7510
```

### `PayrollCalculatorIntegration`
Integration wrapper for seamless payroll system integration.

```python
from core.utils.calculator import PayrollCalculatorIntegration

integration = PayrollCalculatorIntegration(payroll_functions, system_parameters)
result = integration.evaluate_payroll_formula('BASE_SALARY * F04', employee, motif, period)
```

### `ExpressionLexer` & `ExpressionParser`
Core parsing components that tokenize and parse mathematical expressions with proper precedence.

### `MathematicalFunctions`
Static mathematical function library with comprehensive function support.

## Integration with Existing System

### Formula Engine Integration
The calculator integrates seamlessly with the existing `formula_engine.py`:

```python
from core.utils.calculator import create_payroll_calculator
from core.utils.payroll_calculations import PayrollFunctions

# Create integrated calculator
payroll_funcs = PayrollFunctions(system_parameters, payroll_calculator)
calc = create_payroll_calculator(payroll_funcs, system_parameters)

# Evaluate payroll formula
result = calc.evaluate_payroll_formula('F02 * F01 + (F03 * OVERTIME_HOURS * 1.5)', 
                                     employee, motif, period)
```

### Model Integration
Variables can be automatically resolved from Django models:

```python
calc.set_variables({
    'BASE_SALARY': employee.base_salary,
    'SENIORITY_RATE': employee.get_seniority_rate(),
    'OVERTIME_HOURS': attendance.overtime_hours,
})
```

## Utility Functions

### Safe Mathematical Operations
```python
from core.utils.calculator import safe_divide, percentage, round_currency

# Safe division with default value
result = safe_divide(salary, days_worked, 0)

# Percentage calculation
bonus = percentage(base_salary, 15.5)

# Currency rounding (MRU has no decimal places)
amount = round_currency(calculated_amount)
```

### Compound Interest Calculation
```python
from core.utils.calculator import compound_interest

future_value = compound_interest(principal=10000, rate=0.05, time=5, compounding_frequency=12)
```

## Usage Examples

### Basic Expression Evaluation
```python
calc = MathematicalCalculator()

# Basic arithmetic
result = calc.evaluate('2 + 3 * 4')  # 14

# Mathematical functions
result = calc.evaluate('sqrt(16) + max(1, 2, 3)')  # 7

# Complex expression
result = calc.evaluate('(10 + 5) * sin(PI/6) + log(E)')  # 8.5
```

### Payroll Calculation Example
```python
calc = MathematicalCalculator()

# Set employee data
calc.set_variables({
    'BASE_SALARY': 60000,
    'SENIORITY_YEARS': 8,
    'OVERTIME_HOURS': 12,
    'WORKING_DAYS': 22,
    'MEAL_ALLOWANCE': 150,
    'TRANSPORT_ALLOWANCE': 200
})

# Complex payroll formula
formula = '''
    BASE_SALARY + 
    (BASE_SALARY * min(0.30, SENIORITY_YEARS * 0.02)) + 
    ((BASE_SALARY / 173) * OVERTIME_HOURS * 1.5) +
    ((MEAL_ALLOWANCE + TRANSPORT_ALLOWANCE) * WORKING_DAYS) -
    round(BASE_SALARY * 0.015)
'''

net_salary = calc.evaluate(formula)
```

### Expression Validation
```python
is_valid, error_msg = calc.validate_expression('BASE_SALARY * RATE + sqrt(BONUS)')
if not is_valid:
    print(f"Invalid expression: {error_msg}")
```

## Performance Features

### Caching
```python
# Enable caching for repeated calculations
result1 = calc.evaluate('complex_formula', use_cache=True)
result2 = calc.evaluate('complex_formula', use_cache=True)  # Cache hit

# Get performance statistics
stats = calc.get_performance_stats()
print(f"Cache hit rate: {stats['hit_rate']:.1f}%")
```

### Bulk Operations
```python
# Evaluate multiple expressions efficiently
expressions = ['SALARY * 0.01', 'SALARY * 0.005', 'SALARY * 0.15']
results = [calc.evaluate(expr, use_cache=True) for expr in expressions]
```

## Error Handling

### Exception Types
- `CalculatorError`: Base exception for calculator errors
- `ExpressionParseError`: Invalid expression syntax
- `VariableNotFoundError`: Undefined variable reference
- `FunctionNotFoundError`: Unknown function call
- `DivisionByZeroError`: Division by zero attempt

### Error Handling Example
```python
try:
    result = calc.evaluate('SALARY / DAYS_WORKED')
except VariableNotFoundError as e:
    print(f"Missing variable: {e}")
except DivisionByZeroError as e:
    print(f"Division error: {e}")
except CalculatorError as e:
    print(f"Calculation error: {e}")
```

## Testing and Validation

### Built-in Testing
Run the calculator's built-in test suite:
```bash
python core/utils/calculator.py
```

### Custom Testing
```python
# Test with payroll scenario
from core.utils.calculator import test_calculator
test_calculator()

# Validation testing
expressions_to_test = [
    'BASE_SALARY * 0.15',
    '(SALARY + BONUS) * 0.045', 
    'max(0, SALARY - DEDUCTIONS)'
]

for expr in expressions_to_test:
    is_valid, error = calc.validate_expression(expr)
    print(f"{expr}: {'✓' if is_valid else '✗'} {error}")
```

## Integration Checklist

- [x] **Core Calculator**: Complete mathematical expression evaluator
- [x] **Function Library**: Comprehensive mathematical functions
- [x] **Variable Support**: Dynamic variable substitution with underscores
- [x] **Payroll Integration**: Seamless integration with existing payroll functions
- [x] **Error Handling**: Robust error handling and validation
- [x] **Performance**: Caching and optimization features
- [x] **Utility Functions**: Helper functions for common operations
- [x] **Documentation**: Complete usage examples and integration guides
- [x] **Testing**: Built-in test suite and validation examples

## Next Steps

1. **Django Integration**: Update existing formula evaluation in models to use the new calculator
2. **Admin Interface**: Add calculator-based formula validation in Django admin
3. **Performance Testing**: Conduct load testing with large payroll batches
4. **Function Extensions**: Add more payroll-specific functions as needed
5. **Caching Optimization**: Fine-tune caching parameters for production use

The calculator module is now ready for full integration with the Django payroll system and provides a robust foundation for all mathematical expression evaluation needs.