#!/usr/bin/env python3
"""
Standalone Calculator Demo
Demonstrates the calculator capabilities without Django dependencies
"""

import sys
import os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from decimal import Decimal

# Import calculator directly to avoid Django dependencies in __init__.py
from core.utils.calculator import (
    MathematicalCalculator,
    safe_divide,
    percentage,
    round_currency
)

def main():
    print("Advanced Mathematical Calculator Demo")
    print("=" * 50)
    
    # Create calculator instance
    calc = MathematicalCalculator()
    
    print("\n1. Basic Arithmetic Operations")
    print("-" * 30)
    expressions = [
        "2 + 3 * 4",           # 14
        "(2 + 3) * 4",         # 20
        "2 ^ 3 + 1",           # 9
        "10 / 3",              # 3.333...
        "17 % 5",              # 2
    ]
    
    for expr in expressions:
        result = calc.evaluate(expr)
        print(f"{expr:15} = {result}")
    
    print("\n2. Mathematical Functions")
    print("-" * 30)
    functions = [
        "sqrt(16)",            # 4
        "max(1, 5, 3)",        # 5
        "min(1, 5, 3)",        # 1
        "abs(-15)",            # 15
        "round(3.14159, 2)",   # 3.14
        "sin(PI/2)",           # 1
        "cos(0)",              # 1
        "log(E)",              # 1
    ]
    
    for func in functions:
        result = calc.evaluate(func)
        print(f"{func:20} = {result}")
    
    print("\n3. Variable Substitution")
    print("-" * 30)
    
    # Set payroll variables
    calc.set_variable('BASE_SALARY', 50000)
    calc.set_variable('OVERTIME_HOURS', 8)
    calc.set_variable('OVERTIME_RATE', 1.5)
    calc.set_variable('SENIORITY_RATE', 0.15)
    
    payroll_formulas = [
        "BASE_SALARY",
        "BASE_SALARY / 30",
        "BASE_SALARY * SENIORITY_RATE", 
        "(BASE_SALARY/173) * OVERTIME_HOURS * OVERTIME_RATE",
    ]
    
    for formula in payroll_formulas:
        result = calc.evaluate(formula)
        print(f"{formula:40} = {result}")
    
    print("\n4. Complex Payroll Calculation")
    print("-" * 30)
    
    # Employee scenario
    calc.set_variables({
        'MONTHLY_SALARY': 60000,
        'DAYS_WORKED': 20,
        'TOTAL_DAYS': 22,
        'OVERTIME_HOURS': 12,
        'HOURLY_RATE': 347,  # 60000/173
        'SENIORITY_BONUS_RATE': 0.18,
        'MEAL_ALLOWANCE_DAILY': 150,
        'TRANSPORT_ALLOWANCE_DAILY': 200,
    })
    
    complex_formulas = {
        'Base Pay': 'MONTHLY_SALARY * (DAYS_WORKED / TOTAL_DAYS)',
        'Seniority Bonus': 'MONTHLY_SALARY * SENIORITY_BONUS_RATE',
        'Overtime Pay': 'HOURLY_RATE * OVERTIME_HOURS * 1.5',
        'Allowances': '(MEAL_ALLOWANCE_DAILY + TRANSPORT_ALLOWANCE_DAILY) * DAYS_WORKED',
        'CNSS Contribution': 'round(MONTHLY_SALARY * 0.01)',
        'CNAM Contribution': 'round(MONTHLY_SALARY * 0.005)',
    }
    
    results = {}
    for name, formula in complex_formulas.items():
        result = calc.evaluate(formula)
        results[name] = result
        print(f"{name:20} = {result}")
    
    # Calculate totals
    gross_pay = (results['Base Pay'] + results['Seniority Bonus'] + 
                results['Overtime Pay'] + results['Allowances'])
    total_deductions = results['CNSS Contribution'] + results['CNAM Contribution']
    net_pay = gross_pay - total_deductions
    
    print(f"\n{'Gross Pay':20} = {gross_pay}")
    print(f"{'Total Deductions':20} = {total_deductions}")
    print(f"{'Net Pay':20} = {net_pay}")
    
    print("\n5. Utility Functions")
    print("-" * 30)
    print(f"safe_divide(100, 0, default=0)  = {safe_divide(100, 0, 0)}")
    print(f"safe_divide(100, 4)             = {safe_divide(100, 4)}")
    print(f"percentage(50000, 15)           = {percentage(50000, 15)}")
    print(f"round_currency(1234.567)        = {round_currency(1234.567)}")
    print(f"round_currency(1234.567, 2)     = {round_currency(1234.567, 2)}")
    
    print("\n6. Expression Validation")
    print("-" * 30)
    
    test_expressions = [
        "2 + 3 * 4",           # Valid
        "(2 + 3 * 4",          # Invalid - unbalanced parentheses
        "2 ++ 3",              # Invalid - consecutive operators
        "UNDEFINED_VAR + 5",   # Invalid - undefined variable
        "sqrt(-1)",            # Invalid - square root of negative
        "1 / 0",               # Invalid - division by zero
    ]
    
    for expr in test_expressions:
        is_valid, error = calc.validate_expression(expr)
        status = "✓ VALID" if is_valid else f"✗ INVALID: {error}"
        print(f"{expr:25} = {status}")
    
    print("\n7. Performance Statistics")
    print("-" * 30)
    
    # Perform some calculations to generate stats
    for i in range(10):
        calc.evaluate("sqrt(144) + max(1, 2, 3)")
    
    stats = calc.get_performance_stats()
    print(f"Total evaluations: {stats['evaluations']}")
    print(f"Cache hits:        {stats['cache_hits']}")  
    print(f"Cache misses:      {stats['cache_misses']}")
    print(f"Hit rate:          {stats.get('hit_rate', 0):.1f}%")
    print(f"Average eval time: {stats.get('avg_evaluation_time', 0)*1000:.2f}ms")
    
    print("\n✓ Calculator demo completed successfully!")

if __name__ == "__main__":
    main()