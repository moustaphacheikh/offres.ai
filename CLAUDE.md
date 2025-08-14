# Django Payroll Project Migration

This Django payroll project is designed to migrate the legacy Java payroll system (prd) to a modern Django-based solution.

## Project Architecture

- **Models only**: Create Django models to represent the payroll data structure
- **Django admin**: Use built-in admin interface for data visualization and management  
- **pytest**: Write tests using the pytest framework
- **No views/templates**: Skip custom views and templates since Django admin handles the UI

## Migration Strategy

The core app will contain Django models that correspond to the Java entities from the legacy system. This migration focuses on:

1. Data modeling - Converting Java entities to Django models
2. Admin functionality - Leveraging Django's built-in admin for data management
3. Testing - Using pytest for comprehensive test coverage

This approach prioritizes data structure migration and administrative functionality over custom web interfaces.

## Testing Setup

Pytest has been configured for this Django project:

### Running Tests
```bash
DJANGO_SETTINGS_MODULE=payroll.settings pytest
```

### Configuration Files
- `pytest.ini` - Main pytest configuration
- `conftest.py` - Test fixtures and setup
- `tests/` - Test directory structure

### Test Commands
- Run all tests: `DJANGO_SETTINGS_MODULE=payroll.settings pytest`
- Run with verbose output: `DJANGO_SETTINGS_MODULE=payroll.settings pytest -v` 
- Run specific test file: `DJANGO_SETTINGS_MODULE=payroll.settings pytest tests/test_models.py`