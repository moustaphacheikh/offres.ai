import pytest
from decimal import Decimal
from datetime import date, datetime
from django.core.exceptions import ValidationError
from django.db.utils import IntegrityError
from django.utils import timezone
from core.models import MasterPiece, DetailPiece, Employee, Department, Position


@pytest.mark.django_db
class TestMasterPiece:
    """Test cases for MasterPiece model"""
    
    def test_create_masterpiece(self):
        """Test creating a basic MasterPiece"""
        master = MasterPiece.objects.create(
            numero="MP-2024-001",
            libelle_service="Service Paie",
            dateop=date(2024, 1, 15),
            rubrique="Salaire Principal",
            beneficiaire="-",
            total_debit=Decimal('150000.00'),
            total_credit=Decimal('150000.00'),
            initiateur="admin",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Paie mensuelle"
        )
        
        assert master.numero == "MP-2024-001"
        assert master.libelle_service == "Service Paie"
        assert master.period == "2024-01"
        assert master.status == "DRAFT"  # Default status
        assert master.is_balanced is True
        assert master.balance_difference == Decimal('0.00')
    
    def test_masterpiece_str_representation(self):
        """Test string representation of MasterPiece"""
        master = MasterPiece.objects.create(
            numero="MP-2024-002",
            dateop=date(2024, 1, 15),
            rubrique="Test Rubrique",
            initiateur="test_user",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Test Motif"
        )
        
        expected = "MP-MP-2024-002 (2024-01/Test Motif)"
        assert str(master) == expected
    
    def test_balance_check_balanced(self):
        """Test balance check for balanced masterpiece"""
        master = MasterPiece.objects.create(
            numero="MP-2024-003",
            dateop=date(2024, 1, 15),
            rubrique="Test Rubrique",
            total_debit=Decimal('100000.00'),
            total_credit=Decimal('100000.00'),
            initiateur="test_user",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Test Motif"
        )
        
        assert master.is_balanced is True
        assert master.balance_difference == Decimal('0.00')
    
    def test_balance_check_unbalanced(self):
        """Test balance check for unbalanced masterpiece"""
        master = MasterPiece.objects.create(
            numero="MP-2024-004",
            dateop=date(2024, 1, 15),
            rubrique="Test Rubrique",
            total_debit=Decimal('100000.00'),
            total_credit=Decimal('95000.00'),
            initiateur="test_user",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Test Motif"
        )
        
        assert master.is_balanced is False
        assert master.balance_difference == Decimal('5000.00')
    
    def test_balance_within_tolerance(self):
        """Test balance check within rounding tolerance"""
        master = MasterPiece.objects.create(
            numero="MP-2024-005",
            dateop=date(2024, 1, 15),
            rubrique="Test Rubrique",
            total_debit=Decimal('100000.00'),
            total_credit=Decimal('99999.99'),
            initiateur="test_user",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Test Motif"
        )
        
        # Should be considered balanced (difference < 0.01)
        assert master.is_balanced is True
    
    def test_masterpiece_status_choices(self):
        """Test MasterPiece status field choices"""
        statuses = ['DRAFT', 'VALIDATED', 'EXPORTED', 'INTEGRATED']
        
        for status in statuses:
            master = MasterPiece.objects.create(
                numero=f"MP-2024-{status}",
                dateop=date(2024, 1, 15),
                rubrique="Test Rubrique",
                initiateur="test_user",
                init_hr=timezone.now(),
                period="2024-01",
                motif="Test Motif",
                status=status
            )
            assert master.status == status
    
    def test_recalculate_totals_empty(self):
        """Test recalculating totals with no detail pieces"""
        master = MasterPiece.objects.create(
            numero="MP-2024-006",
            dateop=date(2024, 1, 15),
            rubrique="Test Rubrique",
            total_debit=Decimal('100000.00'),
            total_credit=Decimal('100000.00'),
            initiateur="test_user",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Test Motif"
        )
        
        master.recalculate_totals()
        assert master.total_debit == Decimal('0.00')
        assert master.total_credit == Decimal('0.00')


@pytest.mark.django_db  
class TestDetailPiece:
    """Test cases for DetailPiece model"""
    
    @pytest.fixture
    def master_piece(self):
        """Fixture to create a test MasterPiece"""
        return MasterPiece.objects.create(
            numero="MP-TEST-001",
            dateop=date(2024, 1, 15),
            rubrique="Test Rubrique",
            initiateur="test_user",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Test Motif"
        )
    
    @pytest.fixture
    def test_employee(self):
        """Fixture to create a test Employee"""
        from core.models.organizational import GeneralDirection, Direction
        
        # Create organizational hierarchy
        general_direction = GeneralDirection.objects.create(name="General IT")
        direction = Direction.objects.create(
            name="Development Direction",
            general_direction=general_direction
        )
        department = Department.objects.create(
            name="Information Technology",
            direction=direction
        )
        position = Position.objects.create(
            name="Developer"
        )
        return Employee.objects.create(
            last_name="Test",
            first_name="Employee",
            hire_date=date(2024, 1, 1),
            position=position,
            department=department
        )
    
    def test_create_detailpiece(self, master_piece):
        """Test creating a basic DetailPiece"""
        detail = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            journal="PAI",
            compte="6410001",
            libelle="Salaire de base",
            intitulet="Compte salaire",
            montant=Decimal('50000.00'),
            sens='D',
            account_type='RUBRIQUE'
        )
        
        assert detail.nupiece == master_piece
        assert detail.journal == "PAI"
        assert detail.compte == "6410001"
        assert detail.sens == 'D'
        assert detail.montant == detail.cvmro_montant  # Auto-set
        assert detail.devise == "UM"  # Default
        assert detail.cours == Decimal('1.0000')  # Default
        assert detail.is_debit is True
        assert detail.is_credit is False
    
    def test_detailpiece_str_representation(self, master_piece):
        """Test string representation of DetailPiece"""
        detail = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="6410001",
            libelle="Test transaction",
            intitulet="Test account",
            montant=Decimal('25000.00'),
            sens='C'
        )
        
        expected = f"DP-{detail.numligne}: 6410001 Cr 25000.00"
        assert str(detail) == expected
    
    def test_debit_entry(self, master_piece):
        """Test creating a debit entry"""
        detail = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="6410001",
            libelle="Debit transaction",
            intitulet="Debit account",
            montant=Decimal('30000.00'),
            sens='D'
        )
        
        assert detail.is_debit is True
        assert detail.is_credit is False
        assert detail.sens == 'D'
    
    def test_credit_entry(self, master_piece):
        """Test creating a credit entry"""
        detail = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="4210001",
            libelle="Credit transaction",
            intitulet="Credit account",
            montant=Decimal('30000.00'),
            sens='C'
        )
        
        assert detail.is_debit is False
        assert detail.is_credit is True
        assert detail.sens == 'C'
    
    def test_cvmro_montant_auto_set(self, master_piece):
        """Test that cvmro_montant is automatically set to match montant"""
        detail = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="6410001",
            libelle="Test amount",
            intitulet="Test account",
            montant=Decimal('75000.00'),
            sens='D'
        )
        
        assert detail.cvmro_montant == detail.montant
        assert detail.cvmro_montant == Decimal('75000.00')
    
    def test_employee_reference(self, master_piece, test_employee):
        """Test DetailPiece with employee reference"""
        detail = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="307001",
            libelle="Individual salary",
            intitulet=f"{test_employee.first_name} {test_employee.last_name}",
            montant=Decimal('45000.00'),
            sens='C',
            employee=test_employee,
            account_type='CASH'
        )
        
        assert detail.employee == test_employee
        assert detail.account_type == 'CASH'
        assert test_employee.accounting_details.first() == detail
    
    def test_account_type_choices(self, master_piece):
        """Test DetailPiece account_type field choices"""
        account_types = ['RUBRIQUE', 'BANK', 'CASH', 'ENGAGEMENT', 'STATUTORY', 'OTHER']
        
        for i, account_type in enumerate(account_types):
            detail = DetailPiece.objects.create(
                nupiece=master_piece,
                dateop=date(2024, 1, 15),
                compte=f"640{i:04d}",
                libelle=f"Test {account_type}",
                intitulet=f"Account {account_type}",
                montant=Decimal('10000.00'),
                sens='D',
                account_type=account_type
            )
            assert detail.account_type == account_type
    
    def test_formatted_account_with_chapter(self, master_piece):
        """Test formatted_account property with chapter"""
        detail = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="6410001",
            chapitre="64",
            libelle="Test with chapter",
            intitulet="Test account",
            montant=Decimal('20000.00'),
            sens='D'
        )
        
        assert detail.formatted_account == "64/6410001"
    
    def test_formatted_account_without_chapter(self, master_piece):
        """Test formatted_account property without chapter"""
        detail = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="6410001",
            libelle="Test without chapter",
            intitulet="Test account",
            montant=Decimal('20000.00'),
            sens='D'
        )
        
        assert detail.formatted_account == "6410001"
    
    def test_export_tracking(self, master_piece):
        """Test export tracking fields"""
        detail = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="6410001",
            libelle="Export test",
            intitulet="Test account",
            montant=Decimal('15000.00'),
            sens='D',
            exported=True,
            export_reference="EXT-12345"
        )
        
        assert detail.exported is True
        assert detail.export_reference == "EXT-12345"


@pytest.mark.django_db
class TestMasterPieceDetailPieceRelationship:
    """Test relationship between MasterPiece and DetailPiece"""
    
    @pytest.fixture
    def master_piece(self):
        """Fixture to create a test MasterPiece"""
        return MasterPiece.objects.create(
            numero="MP-REL-001",
            dateop=date(2024, 1, 15),
            rubrique="Test Rubrique",
            initiateur="test_user",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Test Motif"
        )
    
    def test_masterpiece_detailpiece_relationship(self, master_piece):
        """Test the relationship between MasterPiece and DetailPiece"""
        # Create some detail pieces
        detail1 = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="6410001",
            libelle="First detail",
            intitulet="Account 1",
            montant=Decimal('30000.00'),
            sens='D'
        )
        
        detail2 = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="4210001",
            libelle="Second detail",
            intitulet="Account 2", 
            montant=Decimal('30000.00'),
            sens='C'
        )
        
        # Test forward relationship
        assert master_piece.detailpieces.count() == 2
        assert detail1 in master_piece.detailpieces.all()
        assert detail2 in master_piece.detailpieces.all()
        
        # Test reverse relationship
        assert detail1.nupiece == master_piece
        assert detail2.nupiece == master_piece
    
    def test_recalculate_totals_with_details(self, master_piece):
        """Test recalculating totals with actual detail pieces"""
        # Create detail pieces
        DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="6410001",
            libelle="Debit 1",
            intitulet="Account D1",
            montant=Decimal('40000.00'),
            sens='D'
        )
        
        DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="6410002",
            libelle="Debit 2", 
            intitulet="Account D2",
            montant=Decimal('20000.00'),
            sens='D'
        )
        
        DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="4210001",
            libelle="Credit 1",
            intitulet="Account C1",
            montant=Decimal('35000.00'),
            sens='C'
        )
        
        DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="4210002",
            libelle="Credit 2",
            intitulet="Account C2", 
            montant=Decimal('25000.00'),
            sens='C'
        )
        
        # Recalculate totals
        master_piece.recalculate_totals()
        
        # Check totals
        assert master_piece.total_debit == Decimal('60000.00')  # 40000 + 20000
        assert master_piece.total_credit == Decimal('60000.00')  # 35000 + 25000
        assert master_piece.is_balanced is True
    
    def test_cascade_delete(self, master_piece):
        """Test that deleting MasterPiece cascades to DetailPiece"""
        # Create detail pieces
        detail1 = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="6410001",
            libelle="Detail 1",
            intitulet="Account 1",
            montant=Decimal('10000.00'),
            sens='D'
        )
        
        detail2 = DetailPiece.objects.create(
            nupiece=master_piece,
            dateop=date(2024, 1, 15),
            compte="4210001",
            libelle="Detail 2", 
            intitulet="Account 2",
            montant=Decimal('10000.00'),
            sens='C'
        )
        
        detail1_id = detail1.numligne
        detail2_id = detail2.numligne
        
        # Delete master piece
        master_piece.delete()
        
        # Check that detail pieces are also deleted
        assert not DetailPiece.objects.filter(numligne=detail1_id).exists()
        assert not DetailPiece.objects.filter(numligne=detail2_id).exists()


@pytest.mark.django_db
class TestAccountingIntegrationConstraints:
    """Test database constraints and validation"""
    
    def test_masterpiece_unique_numero(self):
        """Test that numero must be unique for MasterPiece"""
        # Create first master piece
        MasterPiece.objects.create(
            numero="MP-UNIQUE-001",
            dateop=date(2024, 1, 15),
            rubrique="Test Rubrique",
            initiateur="test_user",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Test Motif"
        )
        
        # Try to create another with same numero
        with pytest.raises(IntegrityError):
            MasterPiece.objects.create(
                numero="MP-UNIQUE-001",  # Same numero
                dateop=date(2024, 1, 16),
                rubrique="Different Rubrique",
                initiateur="other_user",
                init_hr=timezone.now(),
                period="2024-01",
                motif="Different Motif"
            )
    
    def test_detailpiece_sens_choices(self):
        """Test that sens field only accepts valid choices"""
        master = MasterPiece.objects.create(
            numero="MP-SENS-001",
            dateop=date(2024, 1, 15),
            rubrique="Test Rubrique",
            initiateur="test_user",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Test Motif"
        )
        
        # Valid sens values should work
        for sens in ['D', 'C']:
            DetailPiece.objects.create(
                nupiece=master,
                dateop=date(2024, 1, 15),
                compte=f"641000{sens}",
                libelle=f"Test {sens}",
                intitulet=f"Account {sens}",
                montant=Decimal('10000.00'),
                sens=sens
            )
        
        # Invalid sens value should be handled by Django validation
        detail = DetailPiece(
            nupiece=master,
            dateop=date(2024, 1, 15),
            compte="6410003",
            libelle="Invalid sens",
            intitulet="Invalid account",
            montant=Decimal('10000.00'),
            sens='X'  # Invalid value
        )
        
        # This would normally raise ValidationError during form validation
        # But direct model creation may not enforce choices without full_clean()
        with pytest.raises(ValidationError):
            detail.full_clean()


@pytest.mark.django_db
class TestAccountingIntegrationIndexes:
    """Test that database indexes work properly"""
    
    def test_masterpiece_queries_by_period_motif(self):
        """Test queries by period and motif use index"""
        # Create test data
        for i in range(5):
            MasterPiece.objects.create(
                numero=f"MP-INDEX-{i:03d}",
                dateop=date(2024, 1, 15),
                rubrique="Test Rubrique",
                initiateur="test_user",
                init_hr=timezone.now(),
                period=f"2024-{i+1:02d}",
                motif="Test Motif"
            )
        
        # Query by period and motif
        results = MasterPiece.objects.filter(
            period="2024-03",
            motif="Test Motif"
        )
        
        assert results.count() == 1
        assert results.first().numero == "MP-INDEX-002"
    
    def test_detailpiece_queries_by_account_type(self):
        """Test queries by account_type use index"""
        master = MasterPiece.objects.create(
            numero="MP-INDEX-DETAIL",
            dateop=date(2024, 1, 15),
            rubrique="Test Rubrique",
            initiateur="test_user",
            init_hr=timezone.now(),
            period="2024-01",
            motif="Test Motif"
        )
        
        # Create details with different account types
        account_types = ['RUBRIQUE', 'BANK', 'CASH', 'STATUTORY']
        for i, account_type in enumerate(account_types):
            DetailPiece.objects.create(
                nupiece=master,
                dateop=date(2024, 1, 15),
                compte=f"640{i:04d}",
                libelle=f"Test {account_type}",
                intitulet=f"Account {account_type}",
                montant=Decimal('10000.00'),
                sens='D',
                account_type=account_type
            )
        
        # Query by account type
        bank_details = DetailPiece.objects.filter(account_type='BANK')
        assert bank_details.count() == 1
        assert bank_details.first().compte == "6400001"