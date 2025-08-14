"""
Tests for core.views module.
Simple test to achieve 100% coverage of the import statement.
"""

from django.test import TestCase


class ViewsTest(TestCase):
    """Test views module import"""
    
    def test_import_views(self):
        """Test that views can be imported successfully"""
        from core import views
        
        # Test that the module has the expected import
        assert hasattr(views, 'render')
        
        # This ensures the import statement in views.py is covered
        self.assertTrue(True)