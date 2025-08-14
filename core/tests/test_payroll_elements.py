import pytest
from decimal import Decimal
from django.test import TestCase
from django.core.exceptions import ValidationError
from core.models import PayrollElement, PayrollElementFormula


class PayrollElementModelTest(TestCase):
    def setUp(self):
        self.salary_element = PayrollElement.objects.create(
            label="Salaire de Base",
            abbreviation="SAL_BASE",
            type="G",
            has_ceiling=False,
            is_cumulative=True,
            affects_its=True,
            affects_cnss=True,
            affects_cnam=True,
            accounting_account=12345,
            accounting_chapter=67890,
            accounting_key="SAL001",
            is_active=True
        )
    
    def test_payroll_element_creation(self):
        """Test basic payroll element creation"""
        self.assertEqual(self.salary_element.label, "Salaire de Base")
        self.assertEqual(self.salary_element.abbreviation, "SAL_BASE")
        self.assertEqual(self.salary_element.type, "G")
        self.assertFalse(self.salary_element.has_ceiling)
        self.assertTrue(self.salary_element.is_cumulative)
        self.assertTrue(self.salary_element.affects_its)
        self.assertTrue(self.salary_element.affects_cnss)
        self.assertTrue(self.salary_element.affects_cnam)
        self.assertEqual(self.salary_element.accounting_account, 12345)
        self.assertEqual(self.salary_element.accounting_chapter, 67890)
        self.assertEqual(self.salary_element.accounting_key, "SAL001")
        self.assertTrue(self.salary_element.is_active)
    
    def test_payroll_element_type_choices(self):
        """Test type field choices"""
        # Test Gain type
        gain_element = PayrollElement.objects.create(
            label="Bonus",
            type="G"
        )
        self.assertEqual(gain_element.type, "G")
        self.assertEqual(gain_element.get_type_display(), "Gain")
        
        # Test Deduction type
        deduction_element = PayrollElement.objects.create(
            label="Tax Deduction",
            type="D"
        )
        self.assertEqual(deduction_element.type, "D")
        self.assertEqual(deduction_element.get_type_display(), "Deduction")
    
    def test_payroll_element_defaults(self):
        """Test default values"""
        element = PayrollElement.objects.create(
            label="Test Element",
            type="G"
        )
        
        self.assertFalse(element.has_ceiling)
        self.assertFalse(element.is_cumulative)
        self.assertFalse(element.affects_its)
        self.assertFalse(element.affects_cnss)
        self.assertFalse(element.affects_cnam)
        self.assertFalse(element.is_benefit_in_kind)
        self.assertFalse(element.auto_base_calculation)
        self.assertFalse(element.auto_quantity_calculation)
        self.assertEqual(element.accounting_account, 0)
        self.assertEqual(element.accounting_chapter, 0)
        self.assertEqual(element.accounting_key, "")
        self.assertEqual(element.deduction_from, "")
        self.assertTrue(element.is_active)
    
    def test_payroll_element_str(self):
        """Test string representation"""
        expected = "Salaire de Base (Gain)"
        self.assertEqual(str(self.salary_element), expected)
    
    def test_payroll_element_ordering(self):
        """Test model ordering"""
        e1 = PayrollElement.objects.create(label="Z Element", type="G")
        e2 = PayrollElement.objects.create(label="A Element", type="D")
        
        elements = list(PayrollElement.objects.all())
        self.assertEqual(elements[0].label, "A Element")
        self.assertEqual(elements[1].label, "Salaire de Base")
        self.assertEqual(elements[2].label, "Z Element")
    
    def test_db_table(self):
        """Test database table name"""
        self.assertEqual(PayrollElement._meta.db_table, 'rubrique')
    
    def test_payroll_element_verbose_names(self):
        """Test verbose names"""
        self.assertEqual(PayrollElement._meta.verbose_name, 'Payroll Element')
        self.assertEqual(PayrollElement._meta.verbose_name_plural, 'Payroll Elements')
    
    def test_boolean_flag_combinations(self):
        """Test various combinations of boolean flags"""
        element = PayrollElement.objects.create(
            label="Complex Element",
            type="D",
            has_ceiling=True,
            is_cumulative=False,
            affects_its=True,
            affects_cnss=False,
            affects_cnam=True,
            is_benefit_in_kind=True,
            auto_base_calculation=False,
            auto_quantity_calculation=True,
            is_active=False
        )
        
        self.assertTrue(element.has_ceiling)
        self.assertFalse(element.is_cumulative)
        self.assertTrue(element.affects_its)
        self.assertFalse(element.affects_cnss)
        self.assertTrue(element.affects_cnam)
        self.assertTrue(element.is_benefit_in_kind)
        self.assertFalse(element.auto_base_calculation)
        self.assertTrue(element.auto_quantity_calculation)
        self.assertFalse(element.is_active)


class PayrollElementFormulaModelTest(TestCase):
    def setUp(self):
        self.payroll_element = PayrollElement.objects.create(
            label="Test Payroll Element",
            type="G"
        )
        
        self.formula = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="B",
            component_type="N",
            numeric_value=Decimal('1000.50')
        )
    
    def test_formula_creation(self):
        """Test basic formula creation"""
        self.assertEqual(self.formula.payroll_element, self.payroll_element)
        self.assertEqual(self.formula.section, "B")
        self.assertEqual(self.formula.component_type, "N")
        self.assertEqual(self.formula.numeric_value, Decimal('1000.50'))
        self.assertIsNone(self.formula.text_value)
    
    def test_formula_section_choices(self):
        """Test section field choices"""
        # Test Base section
        base_formula = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="B",
            component_type="N",
            numeric_value=Decimal('100')
        )
        self.assertEqual(base_formula.section, "B")
        self.assertEqual(base_formula.get_section_display(), "Base")
        
        # Test Number section
        number_formula = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="N",
            component_type="N",
            numeric_value=Decimal('50')
        )
        self.assertEqual(number_formula.section, "N")
        self.assertEqual(number_formula.get_section_display(), "Number")
    
    def test_formula_component_type_choices(self):
        """Test component_type field choices"""
        # Test Operator
        operator_formula = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="B",
            component_type="O",
            text_value="+"
        )
        self.assertEqual(operator_formula.get_component_type_display(), "Operator")
        
        # Test Number
        number_formula = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="B",
            component_type="N",
            numeric_value=Decimal('123.45')
        )
        self.assertEqual(number_formula.get_component_type_display(), "Number")
        
        # Test Function
        function_formula = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="B",
            component_type="F",
            text_value="MAX"
        )
        self.assertEqual(function_formula.get_component_type_display(), "Function")
        
        # Test Rubrique Reference
        ref_formula = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="B",
            component_type="R",
            text_value="REF001"
        )
        self.assertEqual(ref_formula.get_component_type_display(), "Rubrique Reference")
    
    def test_formula_with_text_value(self):
        """Test formula with text value"""
        text_formula = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="B",
            component_type="O",
            text_value="*"
        )
        
        self.assertEqual(text_formula.text_value, "*")
        self.assertIsNone(text_formula.numeric_value)
    
    def test_formula_with_numeric_value(self):
        """Test formula with numeric value"""
        numeric_formula = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="N",
            component_type="N",
            numeric_value=Decimal('2500.75')
        )
        
        self.assertEqual(numeric_formula.numeric_value, Decimal('2500.75'))
        self.assertIsNone(numeric_formula.text_value)
    
    def test_formula_str(self):
        """Test string representation"""
        expected = "Test Payroll Element - Base: Number (1000.50)"
        self.assertEqual(str(self.formula), expected)
        
        # Test with text value
        text_formula = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="B",
            component_type="O",
            text_value="+"
        )
        expected_text = "Test Payroll Element - Base: Operator (+)"
        self.assertEqual(str(text_formula), expected_text)
    
    def test_formula_ordering(self):
        """Test model ordering"""
        element2 = PayrollElement.objects.create(label="Another Element", type="D")
        
        f1 = PayrollElementFormula.objects.create(
            payroll_element=element2,
            section="N",
            component_type="N",
            numeric_value=Decimal('100')
        )
        f2 = PayrollElementFormula.objects.create(
            payroll_element=self.payroll_element,
            section="N",
            component_type="N", 
            numeric_value=Decimal('200')
        )
        
        formulas = list(PayrollElementFormula.objects.all())
        # Should be ordered by payroll_element label, then section, then id
        # "Another Element" comes before "Test Payroll Element" alphabetically
        self.assertEqual(formulas[0], f1)  # Another Element - Number section
        self.assertEqual(formulas[1], self.formula)  # Test Payroll Element - Base section (created first)
        self.assertEqual(formulas[2], f2)  # Test Payroll Element - Number section (created later)
    
    def test_db_table(self):
        """Test database table name"""
        self.assertEqual(PayrollElementFormula._meta.db_table, 'rubriqueformule')
    
    def test_formula_verbose_names(self):
        """Test verbose names"""
        self.assertEqual(PayrollElementFormula._meta.verbose_name, 'Payroll Element Formula')
        self.assertEqual(PayrollElementFormula._meta.verbose_name_plural, 'Payroll Element Formulas')
    
    def test_formula_foreign_key_relationship(self):
        """Test foreign key relationship"""
        self.assertEqual(self.formula.payroll_element.id, self.payroll_element.id)
        self.assertEqual(self.formula.payroll_element.label, "Test Payroll Element")
        
        # Test reverse relationship
        formulas = self.payroll_element.formulas.all()
        self.assertEqual(len(formulas), 1)
        self.assertEqual(formulas[0], self.formula)
    
    def test_formula_cascade_delete(self):
        """Test that formulas are deleted when payroll element is deleted"""
        formula_id = self.formula.id
        
        # Verify formula exists
        self.assertTrue(PayrollElementFormula.objects.filter(id=formula_id).exists())
        
        # Delete payroll element
        self.payroll_element.delete()
        
        # Verify formula is also deleted
        self.assertFalse(PayrollElementFormula.objects.filter(id=formula_id).exists())


class PayrollElementsIntegrationTest(TestCase):
    def test_complex_payroll_element_with_multiple_formulas(self):
        """Test creating a payroll element with multiple formula components"""
        
        # Create a complex payroll element (e.g., overtime calculation)
        overtime_element = PayrollElement.objects.create(
            label="Heures Supplémentaires",
            abbreviation="HS",
            type="G",
            has_ceiling=True,
            affects_its=True,
            affects_cnss=True,
            affects_cnam=False,
            auto_base_calculation=True,
            is_active=True
        )
        
        # Create base calculation formula: Base_Salary * 1.5
        base_ref = PayrollElementFormula.objects.create(
            payroll_element=overtime_element,
            section="B",
            component_type="R",
            text_value="SAL_BASE"
        )
        
        multiply_op = PayrollElementFormula.objects.create(
            payroll_element=overtime_element,
            section="B",
            component_type="O",
            text_value="*"
        )
        
        rate_factor = PayrollElementFormula.objects.create(
            payroll_element=overtime_element,
            section="B",
            component_type="N",
            numeric_value=Decimal('1.5')
        )
        
        # Create number calculation formula: Hours worked
        hours_worked = PayrollElementFormula.objects.create(
            payroll_element=overtime_element,
            section="N",
            component_type="N",
            numeric_value=Decimal('10')
        )
        
        # Verify all formulas are created and linked correctly
        self.assertEqual(overtime_element.formulas.count(), 4)
        
        base_formulas = overtime_element.formulas.filter(section="B")
        number_formulas = overtime_element.formulas.filter(section="N")
        
        self.assertEqual(base_formulas.count(), 3)
        self.assertEqual(number_formulas.count(), 1)
        
        # Test string representations
        self.assertIn("Heures Supplémentaires", str(overtime_element))
        self.assertIn("Gain", str(overtime_element))
    
    def test_multiple_payroll_elements_creation(self):
        """Test creating multiple payroll elements together"""
        
        # Create various payroll elements
        salary = PayrollElement.objects.create(
            label="Salaire de Base",
            type="G",
            affects_its=True,
            affects_cnss=True,
            affects_cnam=True
        )
        
        bonus = PayrollElement.objects.create(
            label="Prime de Performance", 
            type="G",
            affects_its=True,
            is_benefit_in_kind=False
        )
        
        tax_deduction = PayrollElement.objects.create(
            label="Retenue ITS",
            type="D",
            affects_its=False  # Tax itself doesn't affect ITS
        )
        
        social_deduction = PayrollElement.objects.create(
            label="Cotisation CNSS",
            type="D",
            affects_cnss=False  # Social security itself doesn't affect CNSS
        )
        
        # Verify counts
        self.assertEqual(PayrollElement.objects.count(), 4)
        self.assertEqual(PayrollElement.objects.filter(type="G").count(), 2)
        self.assertEqual(PayrollElement.objects.filter(type="D").count(), 2)
        
        # Verify tax applicability flags
        gains = PayrollElement.objects.filter(type="G")
        for gain in gains:
            if "Salaire" in gain.label:
                self.assertTrue(gain.affects_its)
                self.assertTrue(gain.affects_cnss)
                self.assertTrue(gain.affects_cnam)
            elif "Prime" in gain.label:
                self.assertTrue(gain.affects_its)
                self.assertFalse(gain.affects_cnss)
                self.assertFalse(gain.affects_cnam)
    
    def test_all_models_have_correct_db_tables(self):
        """Verify all payroll element models use correct database table names"""
        self.assertEqual(PayrollElement._meta.db_table, 'rubrique')
        self.assertEqual(PayrollElementFormula._meta.db_table, 'rubriqueformule')
    
    def test_formula_with_edge_case_values(self):
        """Test formulas with edge case numeric values"""
        element = PayrollElement.objects.create(
            label="Test Element",
            type="G"
        )
        
        # Test zero value
        zero_formula = PayrollElementFormula.objects.create(
            payroll_element=element,
            section="B",
            component_type="N",
            numeric_value=Decimal('0.00')
        )
        self.assertEqual(zero_formula.numeric_value, Decimal('0.00'))
        
        # Test very large value
        large_formula = PayrollElementFormula.objects.create(
            payroll_element=element,
            section="B",
            component_type="N",
            numeric_value=Decimal('999999999.99')
        )
        self.assertEqual(large_formula.numeric_value, Decimal('999999999.99'))
        
        # Test negative value  
        negative_formula = PayrollElementFormula.objects.create(
            payroll_element=element,
            section="B",
            component_type="N",
            numeric_value=Decimal('-500.25')
        )
        self.assertEqual(negative_formula.numeric_value, Decimal('-500.25'))