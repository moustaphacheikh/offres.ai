#!/usr/bin/env python3
"""
Integration Example for Advanced Mathematical Calculator
Demonstrates integration with the existing payroll system and formula engine
"""

import sys
import os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from decimal import Decimal
from core.utils.calculator import (
    MathematicalCalculator,
    PayrollCalculatorIntegration,
    create_payroll_calculator,
    safe_divide,
    percentage,
    round_currency
)

def demo_basic_calculator():
    """Demonstrate basic calculator functionality"""
    print("=== Basic Calculator Functionality ===")
    
    calc = MathematicalCalculator()
    
    # Basic arithmetic with proper operator precedence
    expressions = [
        "2 + 3 * 4",           # 14
        "(2 + 3) * 4",         # 20
        "2 ^ 3 + 1",           # 9
        "10 / 3 * 2",          # 6.666...
        "sqrt(144) - 2",       # 10
        "max(5, 10, 3, 8)",    # 10
        "min(5, 10, 3, 8)",    # 3
        "abs(-15.5)",          # 15.5
        "round(3.7892, 2)",    # 3.79
        "floor(3.7)",          # 3
        "ceil(3.1)",           # 4
    ]
    
    for expr in expressions:
        try:
            result = calc.evaluate(expr)
            print(f"{expr:20} = {result}")
        except Exception as e:
            print(f"{expr:20} = ERROR: {str(e)}")
    
    print()

def demo_variable_substitution():
    """Demonstrate variable substitution functionality"""
    print("=== Variable Substitution ===")
    
    calc = MathematicalCalculator()
    
    # Set payroll-specific variables
    calc.set_variable('BASE_SALARY', Decimal('50000'))
    calc.set_variable('OVERTIME_RATE', Decimal('1.5'))
    calc.set_variable('OVERTIME_HOURS', Decimal('8'))
    calc.set_variable('SENIORITY_RATE', Decimal('0.15'))
    
    # Payroll formula examples
    formulas = [
        "BASE_SALARY",                                    # Base salary
        "BASE_SALARY * SENIORITY_RATE",                 # Seniority bonus
        "BASE_SALARY / 30",                             # Daily salary
        "(BASE_SALARY / 30) * OVERTIME_HOURS * OVERTIME_RATE",  # Overtime pay
        "BASE_SALARY + (BASE_SALARY * SENIORITY_RATE)", # Total with seniority
    ]
    
    for formula in formulas:
        try:
            result = calc.evaluate(formula)
            print(f"{formula:50} = {result}")
        except Exception as e:
            print(f"{formula:50} = ERROR: {str(e)}")
    
    print()

def demo_mathematical_functions():
    """Demonstrate mathematical function capabilities"""
    print("=== Mathematical Functions ===")
    
    calc = MathematicalCalculator()
    
    functions = [
        "sin(PI / 6)",        # 0.5
        "cos(0)",             # 1.0
        "tan(PI / 4)",        # 1.0
        "log(E)",             # 1.0
        "log10(100)",         # 2.0
        "log2(8)",            # 3.0
        "sqrt(25)",           # 5.0
        "pow(2, 8)",          # 256
        "exp(1)",             # e
        "factorial(5)",       # 120
        "gcd(48, 18)",        # 6
        "lcm(12, 18)",        # 36
        "avg(10, 20, 30)",    # 20
        "sum(1, 2, 3, 4, 5)", # 15
    ]
    
    for func in functions:
        try:
            result = calc.evaluate(func)
            print(f"{func:20} = {result}")
        except Exception as e:
            print(f"{func:20} = ERROR: {str(e)}")
    
    print()

def demo_payroll_integration():
    """Demonstrate payroll system integration"""
    print("=== Payroll Integration Example ===")
    
    # Mock payroll functions for demonstration
    class MockPayrollFunctions:
        def f01(self):  # Number of working days
            return Decimal('22')
        
        def f02(self):  # Daily base salary
            return Decimal('1000')
        
        def f03(self):  # Hourly base salary
            return Decimal('125')
        
        def f04(self):  # Seniority rate
            return Decimal('0.15')
    
    # Create integrated calculator
    payroll_funcs = MockPayrollFunctions()
    integration = PayrollCalculatorIntegration(payroll_funcs)
    
    # Test payroll formulas (these would be evaluated with actual employee context)
    payroll_formulas = [
        "1000 * 22",                  # Monthly salary
        "1000 * 22 * 0.15",          # Seniority bonus
        "125 * 8 * 1.5",             # Overtime calculation
        "max(0, 1000 - 500)",        # Net positive calculation
        "round(1000 * 0.045, 0)",    # CNSS contribution (rounded)
    ]
    
    for formula in payroll_formulas:
        try:
            result = integration.calculator.evaluate(formula)
            print(f"{formula:25} = {result}")
        except Exception as e:
            print(f"{formula:25} = ERROR: {str(e)}")
    
    print()

def demo_utility_functions():
    """Demonstrate utility functions"""
    print("=== Utility Functions ===")
    
    # Test utility functions
    print(f"safe_divide(100, 0, 0)     = {safe_divide(100, 0, 0)}")
    print(f"safe_divide(100, 4)        = {safe_divide(100, 4)}")
    print(f"percentage(50000, 15)      = {percentage(50000, 15)}")
    print(f"percentage(1000, 4.5)      = {percentage(1000, 4.5)}")
    print(f"round_currency(123.456)    = {round_currency(123.456)}")
    print(f"round_currency(123.456, 2) = {round_currency(123.456, 2)}")
    
    print()

def demo_expression_validation():
    """Demonstrate expression validation"""
    print("=== Expression Validation ===")
    
    calc = MathematicalCalculator()
    calc.set_variable('X', 10)
    
    test_expressions = [
        "2 + 3 * 4",           # Valid
        "(2 + 3) * 4",         # Valid
        "2 + 3 * ",            # Invalid - incomplete
        "((2 + 3) * 4",        # Invalid - unbalanced parentheses
        "2 ++ 3",              # Invalid - consecutive operators
        "X + Y",               # Invalid - undefined variable Y
        "sqrt(16)",            # Valid
        "unknown_func(5)",     # Invalid - unknown function
    ]
    
    for expr in test_expressions:
        is_valid, error_msg = calc.validate_expression(expr)
        status = "VALID" if is_valid else f"INVALID: {error_msg}"
        print(f"{expr:20} = {status}")
    
    print()

def demo_performance_features():
    """Demonstrate performance and caching features"""
    print("=== Performance and Caching ===")
    
    calc = MathematicalCalculator(cache_size=100, cache_ttl=300)
    
    # Perform same calculation multiple times
    expression = "sqrt(144) + log(E) * sin(PI/2)"
    
    # First evaluation (cache miss)
    result1 = calc.evaluate(expression, use_cache=True)
    
    # Second evaluation (cache hit)
    result2 = calc.evaluate(expression, use_cache=True)
    
    # Get performance stats
    stats = calc.get_performance_stats()
    
    print(f"Expression: {expression}")
    print(f"Result 1:   {result1}")
    print(f"Result 2:   {result2}")
    print(f"Cache hits: {stats['cache_hits']}")
    print(f"Cache misses: {stats['cache_misses']}")
    print(f"Hit rate:   {stats.get('hit_rate', 0):.1f}%")
    print()

def demo_complex_payroll_scenario():
    """Demonstrate complex payroll calculation scenario"""
    print("=== Complex Payroll Scenario ===")
    
    calc = MathematicalCalculator()
    
    # Employee data
    employee_data = {
        'BASE_SALARY': 60000,          # Base monthly salary
        'SENIORITY_YEARS': 8,          # Years of service
        'OVERTIME_HOURS': 12,          # Overtime hours this month
        'REGULAR_HOURS': 173,          # Regular monthly hours
        'ABSENCE_DAYS': 2,             # Days absent
        'MEAL_ALLOWANCE': 150,         # Daily meal allowance
        'TRANSPORT_ALLOWANCE': 200,    # Daily transport allowance
        'WORKING_DAYS': 22,            # Working days in month
    }
    
    calc.set_variables(employee_data)
    
    # Complex payroll formulas
    formulas = {
        'Daily Salary': 'BASE_SALARY / 30',
        'Hourly Rate': 'BASE_SALARY / REGULAR_HOURS',
        'Seniority Rate': 'min(0.30, SENIORITY_YEARS * 0.02)',
        'Seniority Bonus': 'BASE_SALARY * min(0.30, SENIORITY_YEARS * 0.02)',
        'Overtime Rate': 'BASE_SALARY / REGULAR_HOURS * 1.5',
        'Overtime Pay': '(BASE_SALARY / REGULAR_HOURS * 1.5) * OVERTIME_HOURS',
        'Absence Deduction': '(BASE_SALARY / 30) * ABSENCE_DAYS',
        'Total Allowances': '(MEAL_ALLOWANCE + TRANSPORT_ALLOWANCE) * (WORKING_DAYS - ABSENCE_DAYS)',
        'Gross Salary': 'BASE_SALARY + (BASE_SALARY * min(0.30, SENIORITY_YEARS * 0.02)) + ((BASE_SALARY / REGULAR_HOURS * 1.5) * OVERTIME_HOURS) - ((BASE_SALARY / 30) * ABSENCE_DAYS) + ((MEAL_ALLOWANCE + TRANSPORT_ALLOWANCE) * (WORKING_DAYS - ABSENCE_DAYS))',
        'CNSS Employee': 'round((BASE_SALARY + (BASE_SALARY * min(0.30, SENIORITY_YEARS * 0.02))) * 0.01)',
        'CNAM Employee': 'round((BASE_SALARY + (BASE_SALARY * min(0.30, SENIORITY_YEARS * 0.02))) * 0.005)',
        'Total Deductions': 'round((BASE_SALARY + (BASE_SALARY * min(0.30, SENIORITY_YEARS * 0.02))) * 0.015) + ((BASE_SALARY / 30) * ABSENCE_DAYS)',
    }
    
    results = {}
    for description, formula in formulas.items():
        try:
            result = calc.evaluate(formula)
            results[description] = result
            print(f"{description:20} = {result}")
        except Exception as e:
            print(f"{description:20} = ERROR: {str(e)}")
    
    # Calculate final net salary
    try:
        net_formula = f"{results.get('Gross Salary', 0)} - {results.get('Total Deductions', 0)}"
        net_salary = calc.evaluate(net_formula)
        print(f"{'Net Salary':20} = {net_salary}")
    except Exception as e:
        print(f"{'Net Salary':20} = ERROR: {str(e)}")
    
    print()

if __name__ == "__main__":
    print("Advanced Mathematical Calculator Integration Demo")
    print("=" * 60)
    print()
    
    demo_basic_calculator()
    demo_variable_substitution()
    demo_mathematical_functions()
    demo_payroll_integration()
    demo_utility_functions()
    demo_expression_validation()
    demo_performance_features()
    demo_complex_payroll_scenario()
    
    print("Demo completed successfully!")