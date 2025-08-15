# test_file_processors.py
"""
Comprehensive test suite for file processing utilities
"""

import os
import io
import tempfile
from datetime import datetime
from unittest.mock import Mock, patch, MagicMock
from django.test import TestCase
from django.core.files.uploadedfile import SimpleUploadedFile
from django.core.files.storage import default_storage

from core.utils.file_processors import (
    FileUploadValidator, ImageProcessor, DocumentProcessor,
    FileStorageManager, FileSecurityManager, FileProcessorFactory,
    validate_and_process_upload, create_file_backup_with_metadata,
    get_comprehensive_file_info, FileProcessingError, FileValidationResult
)


class FileUploadValidatorTestCase(TestCase):
    """Test cases for FileUploadValidator"""
    
    def setUp(self):
        """Set up test fixtures"""
        # Create test files
        self.valid_image = SimpleUploadedFile(
            "test_image.jpg",
            b"fake image content",
            content_type="image/jpeg"
        )
        
        self.valid_pdf = SimpleUploadedFile(
            "test_document.pdf",
            b"%PDF-1.4 fake pdf content",
            content_type="application/pdf"
        )
        
        self.malicious_file = SimpleUploadedFile(
            "malicious.txt",
            b"<script>alert('malicious')</script>",
            content_type="text/plain"
        )
        
        self.oversized_file = SimpleUploadedFile(
            "large_file.txt",
            b"x" * (11 * 1024 * 1024),  # 11MB
            content_type="text/plain"
        )
    
    def test_validate_file_upload_valid_image(self):
        """Test validation of valid image file"""
        result = FileUploadValidator.validate_file_upload(
            self.valid_image, 'image'
        )
        
        self.assertIsInstance(result, FileValidationResult)
        self.assertTrue(result.is_valid)
        self.assertEqual(result.file_info['original_filename'], 'test_image.jpg')
        self.assertEqual(result.file_info['file_extension'], '.jpg')
        self.assertEqual(result.file_info['file_category'], 'image')
    
    def test_validate_file_upload_valid_document(self):
        """Test validation of valid document file"""
        result = FileUploadValidator.validate_file_upload(
            self.valid_pdf, 'document'
        )
        
        self.assertTrue(result.is_valid)
        self.assertEqual(result.file_info['file_extension'], '.pdf')
    
    def test_validate_file_upload_invalid_extension(self):
        """Test validation with invalid file extension"""
        invalid_file = SimpleUploadedFile(
            "test.xyz",
            b"fake content",
            content_type="application/octet-stream"
        )
        
        result = FileUploadValidator.validate_file_upload(
            invalid_file, 'document'
        )
        
        self.assertFalse(result.is_valid)
        self.assertTrue(any('extension' in error['field'] for error in result.errors))
    
    def test_validate_file_upload_oversized(self):
        """Test validation of oversized file"""
        result = FileUploadValidator.validate_file_upload(
            self.oversized_file, 'document'
        )
        
        self.assertFalse(result.is_valid)
        self.assertTrue(any('file_size' in error['field'] for error in result.errors))
    
    def test_validate_file_upload_security_threat(self):
        """Test validation with security threat"""
        result = FileUploadValidator.validate_file_upload(
            self.malicious_file, 'document', perform_security_scan=True
        )
        
        # May pass basic validation but should flag security concerns
        self.assertIn('security_scan', result.file_info)
    
    def test_sanitize_filename(self):
        """Test filename sanitization"""
        dangerous_filename = "../../dangerous<file>.txt"
        sanitized = FileUploadValidator._sanitize_filename(dangerous_filename)
        
        self.assertNotIn('..', sanitized)
        self.assertNotIn('<', sanitized)
        self.assertNotIn('>', sanitized)
    
    def test_format_file_size(self):
        """Test file size formatting"""
        # Test different sizes
        self.assertEqual(FileUploadValidator._format_file_size(512), "512.0 B")
        self.assertEqual(FileUploadValidator._format_file_size(1024), "1.0 KB")
        self.assertEqual(FileUploadValidator._format_file_size(1024 * 1024), "1.0 MB")
    
    def test_calculate_file_hash(self):
        """Test file hash calculation"""
        test_file = io.BytesIO(b"test content")
        hash_value = FileUploadValidator._calculate_file_hash(test_file)
        
        self.assertIsNotNone(hash_value)
        self.assertEqual(len(hash_value), 64)  # SHA256 hex length
    
    def test_security_scan(self):
        """Test security scanning functionality"""
        safe_file = io.BytesIO(b"This is safe content")
        unsafe_file = io.BytesIO(b"<script>alert('xss')</script>")
        
        safe_result = FileUploadValidator._perform_security_scan(safe_file)
        unsafe_result = FileUploadValidator._perform_security_scan(unsafe_file)
        
        self.assertTrue(safe_result['safe'])
        self.assertFalse(unsafe_result['safe'])


class ImageProcessorTestCase(TestCase):
    """Test cases for ImageProcessor"""
    
    def setUp(self):
        """Set up test fixtures"""
        # Create a simple test image in memory
        self.test_image_data = io.BytesIO()
        
        # Mock PIL Image for testing
        self.mock_image = Mock()
        self.mock_image.format = 'JPEG'
        self.mock_image.mode = 'RGB'
        self.mock_image.size = (800, 600)
        self.mock_image.width = 800
        self.mock_image.height = 600
    
    @patch('core.utils.file_processors.PIL_AVAILABLE', True)
    @patch('core.utils.file_processors.Image')
    @patch('core.utils.file_processors.default_storage')
    def test_process_employee_photo(self, mock_storage, mock_pil):
        """Test employee photo processing"""
        # Setup mocks
        mock_pil.open.return_value = self.mock_image
        mock_storage.save.return_value = 'test_path.jpg'
        
        result = ImageProcessor.process_employee_photo(
            self.test_image_data, 'EMP001', generate_thumbnails=True
        )
        
        self.assertTrue(result['success'])
        self.assertIn('processed_files', result)
        self.assertIn('thumbnails', result)
        self.assertIn('profile', result['processed_files'])
    
    @patch('core.utils.file_processors.PIL_AVAILABLE', True)
    @patch('core.utils.file_processors.Image')
    @patch('core.utils.file_processors.default_storage')
    def test_process_document_scan(self, mock_storage, mock_pil):
        """Test document scan processing"""
        # Setup mocks
        mock_pil.open.return_value = self.mock_image
        mock_storage.save.return_value = 'test_document.jpg'
        
        result = ImageProcessor.process_document_scan(
            self.test_image_data, 'id_card', enhance_quality=True
        )
        
        self.assertTrue(result['success'])
        self.assertEqual(result['document_type'], 'id_card')
        self.assertIn('processed_files', result)
    
    @patch('core.utils.file_processors.PIL_AVAILABLE', False)
    def test_process_without_pil(self):
        """Test image processing without PIL available"""
        with self.assertRaises(FileProcessingError):
            ImageProcessor.process_employee_photo(self.test_image_data)
    
    def test_extract_image_metadata(self):
        """Test image metadata extraction"""
        with patch('core.utils.file_processors.Image') as mock_pil:
            mock_image = Mock()
            mock_image.format = 'JPEG'
            mock_image.mode = 'RGB'
            mock_image.size = (800, 600)
            mock_image.width = 800
            mock_image.height = 600
            mock_image._getexif.return_value = None
            
            mock_pil.open.return_value = mock_image
            
            metadata = ImageProcessor._extract_image_metadata(self.test_image_data)
            
            self.assertIn('format', metadata)
            self.assertIn('size', metadata)
            self.assertEqual(metadata['format'], 'JPEG')


class DocumentProcessorTestCase(TestCase):
    """Test cases for DocumentProcessor"""
    
    def setUp(self):
        """Set up test fixtures"""
        self.test_pdf_data = io.BytesIO(b"%PDF-1.4 fake pdf content")
    
    @patch('core.utils.file_processors.PDF_AVAILABLE', True)
    @patch('core.utils.file_processors.fitz')
    def test_process_pdf_document(self, mock_fitz):
        """Test PDF document processing"""
        # Setup mock PDF document
        mock_doc = Mock()
        mock_doc.__len__ = Mock(return_value=2)
        mock_doc.metadata = {
            'title': 'Test Document',
            'author': 'Test Author'
        }
        
        mock_page = Mock()
        mock_page.get_text.return_value = "Sample text content"
        mock_page.widgets.return_value = []
        
        mock_doc.__getitem__ = Mock(return_value=mock_page)
        mock_fitz.open.return_value = mock_doc
        
        result = DocumentProcessor.process_pdf_document(
            self.test_pdf_data, extract_text=True
        )
        
        self.assertTrue(result['success'])
        self.assertEqual(result['page_count'], 2)
        self.assertIn('text_content', result)
        self.assertIn('metadata', result)
    
    @patch('core.utils.file_processors.PDF_AVAILABLE', False)
    def test_process_pdf_without_libraries(self):
        """Test PDF processing without required libraries"""
        with self.assertRaises(FileProcessingError):
            DocumentProcessor.process_pdf_document(self.test_pdf_data)
    
    @patch('core.utils.file_processors.PDF_AVAILABLE', True)
    @patch('core.utils.file_processors.fitz')
    def test_extract_pdf_text(self, mock_fitz):
        """Test PDF text extraction"""
        mock_doc = Mock()
        mock_doc.__len__ = Mock(return_value=1)
        
        mock_page = Mock()
        mock_page.get_text.return_value = "Extracted text content"
        mock_doc.__getitem__ = Mock(return_value=mock_page)
        mock_fitz.open.return_value = mock_doc
        
        text = DocumentProcessor.extract_pdf_text(self.test_pdf_data)
        
        self.assertEqual(text, "Extracted text content")
    
    def test_extract_pdf_metadata(self):
        """Test PDF metadata extraction"""
        with patch('core.utils.file_processors.PyPDF2') as mock_pypdf2:
            mock_reader = Mock()
            mock_reader.pages = [Mock(), Mock()]
            mock_reader.is_encrypted = False
            mock_reader.metadata = {
                '/Title': 'Test Title',
                '/Author': 'Test Author'
            }
            
            mock_pypdf2.PdfReader.return_value = mock_reader
            
            metadata = DocumentProcessor._extract_pdf_metadata(self.test_pdf_data)
            
            self.assertIn('page_count', metadata)
            self.assertEqual(metadata['page_count'], 2)
            self.assertIn('metadata', metadata)


class FileStorageManagerTestCase(TestCase):
    """Test cases for FileStorageManager"""
    
    def setUp(self):
        """Set up test fixtures"""
        self.test_file_path = "temp/test_file.txt"
        self.test_content = b"Test file content"
    
    @patch('core.utils.file_processors.default_storage')
    def test_organize_file_storage(self, mock_storage):
        """Test file organization"""
        mock_storage.exists.return_value = True
        mock_storage.open.return_value = io.BytesIO(self.test_content)
        mock_storage.save.return_value = "organized/path/test_file.txt"
        
        organized_path = FileStorageManager.organize_file_storage(
            self.test_file_path, 'employee_documents', 'EMP001'
        )
        
        self.assertIsNotNone(organized_path)
        mock_storage.save.assert_called()
        mock_storage.delete.assert_called_with(self.test_file_path)
    
    def test_cleanup_expired_files_dry_run(self):
        """Test cleanup in dry run mode"""
        result = FileStorageManager.cleanup_expired_files(dry_run=True)
        
        self.assertIn('total_files_checked', result)
        self.assertIn('files_to_delete', result)
        self.assertEqual(result['files_deleted'], 0)  # Dry run shouldn't delete
    
    @patch('core.utils.file_processors.default_storage')
    def test_create_file_backup(self, mock_storage):
        """Test file backup creation"""
        file_paths = ['file1.txt', 'file2.txt']
        
        # Mock file existence and content
        mock_storage.exists.return_value = True
        mock_file = Mock()
        mock_file.read.return_value = b"File content"
        mock_storage.open.return_value = mock_file
        mock_storage.save.return_value = "backups/backup.zip"
        
        backup_path = FileStorageManager.create_file_backup(file_paths, "test_backup")
        
        self.assertIsNotNone(backup_path)
        self.assertTrue(mock_storage.save.called)
    
    def test_get_storage_statistics(self):
        """Test storage statistics generation"""
        with patch('core.utils.file_processors.default_storage') as mock_storage:
            mock_storage.exists.return_value = False  # No files to avoid complex mocking
            
            stats = FileStorageManager.get_storage_statistics()
            
            self.assertIn('total_files', stats)
            self.assertIn('total_size', stats)
            self.assertIn('categories', stats)
    
    def test_retention_policies(self):
        """Test retention policy configuration"""
        policies = FileStorageManager.RETENTION_POLICIES
        
        self.assertIn('temporary_files', policies)
        self.assertIn('employee_documents', policies)
        self.assertEqual(policies['archives'], -1)  # Permanent storage


class FileSecurityManagerTestCase(TestCase):
    """Test cases for FileSecurityManager"""
    
    def setUp(self):
        """Set up test fixtures"""
        self.safe_content = io.BytesIO(b"This is safe content")
        self.malicious_content = io.BytesIO(b"<script>alert('malicious')</script>")
    
    def test_scan_file_for_threats_safe(self):
        """Test security scan of safe file"""
        result = FileSecurityManager.scan_file_for_threats(self.safe_content)
        
        self.assertTrue(result['safe'])
        self.assertEqual(len(result['threats_found']), 0)
        self.assertIn('file_hash', result)
    
    def test_scan_file_for_threats_malicious(self):
        """Test security scan of malicious file"""
        result = FileSecurityManager.scan_file_for_threats(self.malicious_content)
        
        self.assertFalse(result['safe'])
        self.assertGreater(len(result['threats_found']), 0)
    
    @patch('core.utils.file_processors.default_storage')
    def test_quarantine_suspicious_file(self, mock_storage):
        """Test file quarantine functionality"""
        threat_info = {
            'type': 'malware_signature',
            'description': 'Malicious content detected'
        }
        
        mock_storage.exists.return_value = True
        mock_file = Mock()
        mock_file.read.return_value = b"malicious content"
        mock_storage.open.return_value = mock_file
        mock_storage.save.return_value = "quarantine/file.txt"
        
        quarantine_path = FileSecurityManager.quarantine_suspicious_file(
            "suspicious_file.txt", threat_info
        )
        
        self.assertIsNotNone(quarantine_path)
        mock_storage.delete.assert_called_with("suspicious_file.txt")
    
    def test_malware_signatures(self):
        """Test malware signature detection"""
        signatures = FileSecurityManager.MALWARE_SIGNATURES
        
        self.assertGreater(len(signatures), 0)
        self.assertIn(b'<script', signatures)
        self.assertIn(b'<?php', signatures)


class FileProcessorFactoryTestCase(TestCase):
    """Test cases for FileProcessorFactory"""
    
    def test_create_processor_valid_types(self):
        """Test creation of valid processor types"""
        valid_types = [
            'upload_validator',
            'image_processor',
            'document_processor',
            'storage_manager',
            'security_manager'
        ]
        
        for processor_type in valid_types:
            processor = FileProcessorFactory.create_processor(processor_type)
            self.assertIsNotNone(processor)
    
    def test_create_processor_invalid_type(self):
        """Test creation with invalid processor type"""
        with self.assertRaises(FileProcessingError):
            FileProcessorFactory.create_processor('invalid_processor')


class ConvenienceFunctionsTestCase(TestCase):
    """Test cases for convenience functions"""
    
    def setUp(self):
        """Set up test fixtures"""
        self.test_file = SimpleUploadedFile(
            "test.jpg",
            b"fake image content",
            content_type="image/jpeg"
        )
    
    @patch('core.utils.file_processors.ImageProcessor.process_employee_photo')
    @patch('core.utils.file_processors.FileUploadValidator.validate_file_upload')
    def test_validate_and_process_upload(self, mock_validator, mock_processor):
        """Test complete upload validation and processing"""
        # Mock successful validation
        mock_validation_result = Mock()
        mock_validation_result.is_valid = True
        mock_validation_result.errors = []
        mock_validation_result.file_info = {'file_extension': '.jpg'}
        mock_validator.return_value = mock_validation_result
        
        # Mock successful processing
        mock_processing_result = {
            'success': True,
            'processed_files': {'profile': 'path/to/profile.jpg'},
            'errors': []
        }
        mock_processor.return_value = mock_processing_result
        
        result = validate_and_process_upload(
            self.test_file, 'image', 'EMP001', perform_processing=True
        )
        
        self.assertTrue(result['success'])
        self.assertIn('validation_result', result)
        self.assertIn('processing_result', result)
    
    @patch('core.utils.file_processors.FileStorageManager.create_file_backup')
    def test_create_file_backup_with_metadata(self, mock_create_backup):
        """Test backup creation with metadata"""
        mock_create_backup.return_value = "backup/path.zip"
        
        file_paths = ['file1.txt', 'file2.txt']
        metadata = {'backup_type': 'test'}
        
        result = create_file_backup_with_metadata(file_paths, metadata)
        
        self.assertTrue(result['success'])
        self.assertIn('backup_path', result)
        self.assertIn('metadata', result)
        self.assertEqual(result['metadata']['backup_type'], 'test')
    
    @patch('core.utils.file_processors.default_storage')
    def test_get_comprehensive_file_info(self, mock_storage):
        """Test comprehensive file information retrieval"""
        mock_storage.exists.return_value = True
        
        # Mock file stats
        mock_stats = Mock()
        mock_stats.st_size = 1024
        mock_stats.st_mtime = 1234567890
        mock_storage.stat.return_value = mock_stats
        
        # Mock file content for security scan
        mock_file = Mock()
        mock_file.read.return_value = b"safe content"
        mock_storage.open.return_value = mock_file
        
        file_info = get_comprehensive_file_info("test/file.txt")
        
        self.assertTrue(file_info['exists'])
        self.assertEqual(file_info['size'], 1024)
        self.assertIn('security_status', file_info)
        self.assertEqual(file_info['extension'], '.txt')


class FileValidationResultTestCase(TestCase):
    """Test cases for FileValidationResult"""
    
    def test_initialization(self):
        """Test FileValidationResult initialization"""
        result = FileValidationResult(
            is_valid=True,
            file_info={'test': 'info'}
        )
        
        self.assertTrue(result.is_valid)
        self.assertEqual(result.file_info['test'], 'info')
    
    def test_add_file_info(self):
        """Test adding file information"""
        result = FileValidationResult()
        result.add_file_info('test_key', 'test_value')
        
        self.assertEqual(result.file_info['test_key'], 'test_value')


class FileProcessingErrorTestCase(TestCase):
    """Test cases for FileProcessingError"""
    
    def test_initialization(self):
        """Test FileProcessingError initialization"""
        error = FileProcessingError(
            "Test error",
            error_code="TEST_ERROR",
            file_path="test/path.txt"
        )
        
        self.assertEqual(str(error), "Test error")
        self.assertEqual(error.error_code, "TEST_ERROR")
        self.assertEqual(error.file_path, "test/path.txt")


# Integration tests
class FileProcessingIntegrationTestCase(TestCase):
    """Integration tests for file processing workflow"""
    
    @patch('core.utils.file_processors.default_storage')
    @patch('core.utils.file_processors.PIL_AVAILABLE', True)
    @patch('core.utils.file_processors.Image')
    def test_complete_image_workflow(self, mock_pil, mock_storage):
        """Test complete image processing workflow"""
        # Create test image file
        test_image = SimpleUploadedFile(
            "employee_photo.jpg",
            b"fake image content",
            content_type="image/jpeg"
        )
        
        # Setup mocks
        mock_image = Mock()
        mock_image.format = 'JPEG'
        mock_image.mode = 'RGB'
        mock_image.size = (800, 600)
        mock_pil.open.return_value = mock_image
        mock_storage.save.return_value = 'saved/path.jpg'
        
        # Run complete workflow
        result = validate_and_process_upload(
            test_image, 'image', 'EMP001', perform_processing=True
        )
        
        self.assertTrue(result['success'])
        self.assertIsNotNone(result['validation_result'])
        
    def test_security_workflow(self):
        """Test security scanning workflow"""
        malicious_file = SimpleUploadedFile(
            "malicious.txt",
            b"<script>alert('xss')</script>",
            content_type="text/plain"
        )
        
        result = validate_and_process_upload(
            malicious_file, 'document', perform_processing=False
        )
        
        # File should be validated but may have security warnings
        self.assertIn('validation_result', result)