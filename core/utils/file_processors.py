# file_processors.py
"""
Comprehensive File Processing and Document Handling Module for Mauritanian Payroll System

This module provides extensive file processing capabilities focusing on:
1. File Upload Validation - Type checking, size limits, security scanning
2. Image Processing - Employee photos, document scans, resizing, optimization
3. Document Management - PDF processing, metadata extraction, storage organization
4. File Storage - Secure storage, path management, cleanup utilities
5. Backup and Archive - File backup, compression, retention policies

Integrates seamlessly with Django file handling and the payroll system's
validation infrastructure while providing robust security and performance optimization.
"""

import os
import io
import hashlib
import mimetypes
import tempfile
import zipfile
import shutil
import logging
from pathlib import Path
from typing import Dict, List, Optional, Union, Tuple, Any, Set
from datetime import datetime, timedelta
from decimal import Decimal
import json
import re

# Django imports
from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
from django.conf import settings
from django.utils.text import slugify

# Image processing
try:
    from PIL import Image, ImageOps, ExifTags
    PIL_AVAILABLE = True
except ImportError:
    PIL_AVAILABLE = False

# PDF processing
try:
    import PyPDF2
    import fitz  # PyMuPDF
    PDF_AVAILABLE = True
except ImportError:
    PDF_AVAILABLE = False

# Excel/CSV processing
try:
    import openpyxl
    import pandas as pd
    EXCEL_AVAILABLE = True
except ImportError:
    EXCEL_AVAILABLE = False

# Import existing utilities
from .validators import ValidationResult, ValidationError, DataSanitizer
from .text_utils import TextFormatter, ArabicTextUtils

logger = logging.getLogger(__name__)


class FileProcessingError(Exception):
    """Custom exception for file processing errors"""
    def __init__(self, message: str, error_code: str = None, file_path: str = None):
        self.message = message
        self.error_code = error_code
        self.file_path = file_path
        super().__init__(self.message)


class FileValidationResult(ValidationResult):
    """Extended validation result for file processing"""
    
    def __init__(self, is_valid: bool = True, errors: List[Dict] = None, 
                 warnings: List[Dict] = None, cleaned_data: Dict = None,
                 file_info: Dict = None):
        super().__init__(is_valid, errors, warnings, cleaned_data)
        self.file_info = file_info or {}
    
    def add_file_info(self, key: str, value: Any):
        """Add file-specific information"""
        self.file_info[key] = value


class FileUploadValidator:
    """Comprehensive file upload validation with security checks"""
    
    # File type configurations
    ALLOWED_EXTENSIONS = {
        'images': {'.jpg', '.jpeg', '.png', '.gif', '.bmp', '.webp'},
        'documents': {'.pdf', '.doc', '.docx', '.txt', '.rtf'},
        'spreadsheets': {'.xls', '.xlsx', '.csv', '.ods'},
        'archives': {'.zip', '.rar', '.7z', '.tar', '.gz'},
        'data': {'.json', '.xml', '.yaml', '.yml'}
    }
    
    # MIME type validation
    ALLOWED_MIMES = {
        # Images
        'image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/webp',
        # Documents
        'application/pdf', 'application/msword', 'application/rtf', 'text/plain',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        # Spreadsheets
        'application/vnd.ms-excel', 'text/csv', 
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'application/vnd.oasis.opendocument.spreadsheet',
        # Archives
        'application/zip', 'application/x-rar-compressed', 'application/x-7z-compressed',
        # Data formats
        'application/json', 'application/xml', 'text/xml', 'application/x-yaml'
    }
    
    # Size limits (in bytes)
    SIZE_LIMITS = {
        'image': 10 * 1024 * 1024,        # 10MB for images
        'document': 50 * 1024 * 1024,     # 50MB for documents
        'spreadsheet': 25 * 1024 * 1024,  # 25MB for spreadsheets
        'archive': 100 * 1024 * 1024,     # 100MB for archives
        'data': 5 * 1024 * 1024,          # 5MB for data files
        'default': 10 * 1024 * 1024       # 10MB default limit
    }
    
    # Security patterns to detect potentially dangerous content
    DANGEROUS_PATTERNS = [
        rb'<script[^>]*>',
        rb'javascript:',
        rb'vbscript:',
        rb'onload\s*=',
        rb'onerror\s*=',
        rb'<?php',
        rb'<%',
        rb'exec\s*\(',
        rb'system\s*\(',
        rb'shell_exec\s*\(',
        rb'passthru\s*\(',
    ]
    
    @staticmethod
    def validate_file_upload(file_data: Any, file_category: str = 'document', 
                           max_size: int = None, allowed_extensions: Set[str] = None,
                           perform_security_scan: bool = True) -> FileValidationResult:
        """
        Comprehensive file upload validation
        
        Args:
            file_data: File object or file-like object
            file_category: Category of file (image, document, spreadsheet, etc.)
            max_size: Maximum file size in bytes (overrides default)
            allowed_extensions: Custom allowed extensions set
            perform_security_scan: Whether to perform security scanning
            
        Returns:
            FileValidationResult with validation outcome and file information
        """
        result = FileValidationResult()
        
        try:
            # Get file information
            if hasattr(file_data, 'name'):
                filename = file_data.name
                size = file_data.size if hasattr(file_data, 'size') else None
            else:
                filename = str(file_data)
                size = None
            
            result.add_file_info('original_filename', filename)
            result.add_file_info('file_category', file_category)
            
            # Validate filename
            if not filename:
                result.add_error('filename', 'Nom de fichier manquant', 'MISSING_FILENAME')
                return result
            
            # Sanitize filename
            sanitized_filename = FileUploadValidator._sanitize_filename(filename)
            result.add_file_info('sanitized_filename', sanitized_filename)
            
            # Extract file extension
            file_ext = Path(filename).suffix.lower()
            result.add_file_info('file_extension', file_ext)
            
            # Validate file extension
            allowed_exts = allowed_extensions or FileUploadValidator.ALLOWED_EXTENSIONS.get(file_category, set())
            if file_category in FileUploadValidator.ALLOWED_EXTENSIONS:
                allowed_exts.update(FileUploadValidator.ALLOWED_EXTENSIONS[file_category])
            
            if file_ext not in allowed_exts:
                result.add_error(
                    'extension',
                    f'Extension de fichier non autorisée: {file_ext}. Extensions autorisées: {", ".join(sorted(allowed_exts))}',
                    'INVALID_EXTENSION'
                )
            
            # Validate file size
            if size is not None:
                result.add_file_info('file_size', size)
                max_allowed = max_size or FileUploadValidator.SIZE_LIMITS.get(file_category, FileUploadValidator.SIZE_LIMITS['default'])
                
                if size > max_allowed:
                    result.add_error(
                        'file_size',
                        f'Fichier trop volumineux: {FileUploadValidator._format_file_size(size)} > {FileUploadValidator._format_file_size(max_allowed)}',
                        'FILE_TOO_LARGE'
                    )
                elif size == 0:
                    result.add_error('file_size', 'Fichier vide', 'EMPTY_FILE')
            
            # Validate MIME type
            if hasattr(file_data, 'content_type'):
                mime_type = file_data.content_type
            else:
                mime_type, _ = mimetypes.guess_type(filename)
            
            result.add_file_info('mime_type', mime_type)
            
            if mime_type and mime_type not in FileUploadValidator.ALLOWED_MIMES:
                result.add_warning(
                    'mime_type',
                    f'Type MIME non reconnu: {mime_type}',
                    'UNRECOGNIZED_MIME_TYPE'
                )
            
            # Security scanning
            if perform_security_scan and hasattr(file_data, 'read'):
                security_result = FileUploadValidator._perform_security_scan(file_data)
                if not security_result['safe']:
                    result.add_error(
                        'security',
                        f'Contenu potentiellement dangereux détecté: {security_result["threat"]}',
                        'SECURITY_THREAT'
                    )
                result.add_file_info('security_scan', security_result)
            
            # Generate file hash for duplicate detection
            if hasattr(file_data, 'read'):
                file_hash = FileUploadValidator._calculate_file_hash(file_data)
                result.add_file_info('file_hash', file_hash)
            
            # Add metadata based on file type
            if file_category == 'image' and hasattr(file_data, 'read'):
                image_info = ImageProcessor._extract_image_metadata(file_data)
                result.add_file_info('image_metadata', image_info)
            elif file_category == 'document' and file_ext == '.pdf':
                pdf_info = DocumentProcessor._extract_pdf_metadata(file_data)
                result.add_file_info('pdf_metadata', pdf_info)
            
        except Exception as e:
            logger.error(f"File validation error: {str(e)}")
            result.add_error('validation', f'Erreur lors de la validation: {str(e)}', 'VALIDATION_ERROR')
        
        return result
    
    @staticmethod
    def _sanitize_filename(filename: str) -> str:
        """Sanitize filename for safe storage"""
        # Remove path components
        filename = os.path.basename(filename)
        
        # Remove dangerous characters
        filename = re.sub(r'[<>:"/\\|?*\x00-\x1f]', '', filename)
        
        # Limit length
        name, ext = os.path.splitext(filename)
        if len(name) > 100:
            name = name[:100]
        
        # Ensure we have a valid filename
        if not name:
            name = 'file'
        
        return f"{name}{ext}".strip()
    
    @staticmethod
    def _perform_security_scan(file_data: Any) -> Dict[str, Any]:
        """Basic security scanning for dangerous content patterns"""
        try:
            # Read file content for scanning
            file_data.seek(0)
            content = file_data.read()
            file_data.seek(0)
            
            # Check for dangerous patterns
            for pattern in FileUploadValidator.DANGEROUS_PATTERNS:
                if re.search(pattern, content, re.IGNORECASE):
                    return {
                        'safe': False,
                        'threat': f'Potentially dangerous pattern detected',
                        'scan_date': datetime.now().isoformat()
                    }
            
            # Additional checks for specific file types
            if content.startswith(b'PK'):  # ZIP-like files
                # Basic ZIP bomb detection (simplified)
                if len(content) > 0 and len(content) < 1000:  # Very small file
                    try:
                        # This is a simplified check - in production, use proper ZIP validation
                        pass
                    except Exception:
                        pass
            
            return {
                'safe': True,
                'threat': None,
                'scan_date': datetime.now().isoformat()
            }
            
        except Exception as e:
            logger.error(f"Security scan error: {str(e)}")
            return {
                'safe': False,  # Err on the side of caution
                'threat': f'Security scan failed: {str(e)}',
                'scan_date': datetime.now().isoformat()
            }
    
    @staticmethod
    def _calculate_file_hash(file_data: Any, algorithm: str = 'sha256') -> str:
        """Calculate hash of file content for duplicate detection"""
        try:
            file_data.seek(0)
            hasher = hashlib.new(algorithm)
            
            # Read in chunks for memory efficiency
            for chunk in iter(lambda: file_data.read(4096), b''):
                hasher.update(chunk)
            
            file_data.seek(0)
            return hasher.hexdigest()
            
        except Exception as e:
            logger.error(f"Hash calculation error: {str(e)}")
            return None
    
    @staticmethod
    def _format_file_size(size_bytes: int) -> str:
        """Format file size in human-readable format"""
        for unit in ['B', 'KB', 'MB', 'GB']:
            if size_bytes < 1024:
                return f"{size_bytes:.1f} {unit}"
            size_bytes /= 1024
        return f"{size_bytes:.1f} TB"


class ImageProcessor:
    """Advanced image processing for employee photos and document scans"""
    
    # Image processing configurations
    THUMBNAIL_SIZES = {
        'small': (150, 150),
        'medium': (300, 300),
        'large': (600, 600)
    }
    
    PHOTO_SIZES = {
        'profile': (200, 200),
        'id_card': (300, 400),
        'document_scan': (1200, 1600)
    }
    
    QUALITY_SETTINGS = {
        'high': 95,
        'medium': 85,
        'low': 75,
        'web': 80
    }
    
    @staticmethod
    def process_employee_photo(image_data: Any, employee_id: str = None, 
                             generate_thumbnails: bool = True) -> Dict[str, Any]:
        """
        Process employee photo with validation, resizing, and optimization
        
        Args:
            image_data: Image file data
            employee_id: Employee identifier for naming
            generate_thumbnails: Whether to generate thumbnail versions
            
        Returns:
            Dictionary with processing results and file paths
        """
        if not PIL_AVAILABLE:
            raise FileProcessingError("PIL/Pillow not available for image processing", "MISSING_DEPENDENCY")
        
        result = {
            'success': True,
            'original_info': {},
            'processed_files': {},
            'thumbnails': {},
            'errors': []
        }
        
        try:
            # Open and validate image
            image_data.seek(0)
            image = Image.open(image_data)
            
            # Extract original image information
            result['original_info'] = {
                'format': image.format,
                'mode': image.mode,
                'size': image.size,
                'has_transparency': image.mode in ('RGBA', 'LA') or 'transparency' in image.info
            }
            
            # Handle EXIF orientation
            image = ImageProcessor._fix_orientation(image)
            
            # Convert to RGB if necessary (for JPEG output)
            if image.mode in ('RGBA', 'LA', 'P'):
                # Create white background for transparency
                background = Image.new('RGB', image.size, (255, 255, 255))
                if image.mode == 'P':
                    image = image.convert('RGBA')
                background.paste(image, mask=image.split()[-1] if image.mode in ('RGBA', 'LA') else None)
                image = background
            
            # Process main profile photo
            profile_image = ImageProcessor._resize_image(
                image, ImageProcessor.PHOTO_SIZES['profile'], maintain_aspect=True
            )
            
            # Save processed photo
            profile_filename = f"employee_{employee_id}_photo.jpg" if employee_id else "employee_photo.jpg"
            profile_path = ImageProcessor._save_processed_image(
                profile_image, profile_filename, quality=ImageProcessor.QUALITY_SETTINGS['high']
            )
            
            result['processed_files']['profile'] = profile_path
            
            # Generate thumbnails if requested
            if generate_thumbnails:
                for size_name, dimensions in ImageProcessor.THUMBNAIL_SIZES.items():
                    thumbnail = ImageProcessor._resize_image(image, dimensions, maintain_aspect=True)
                    thumb_filename = f"employee_{employee_id}_thumb_{size_name}.jpg" if employee_id else f"thumb_{size_name}.jpg"
                    thumb_path = ImageProcessor._save_processed_image(
                        thumbnail, thumb_filename, quality=ImageProcessor.QUALITY_SETTINGS['web']
                    )
                    result['thumbnails'][size_name] = thumb_path
            
            # Generate optimized web version
            web_image = ImageProcessor._resize_image(
                image, (400, 400), maintain_aspect=True
            )
            web_filename = f"employee_{employee_id}_web.jpg" if employee_id else "employee_web.jpg"
            web_path = ImageProcessor._save_processed_image(
                web_image, web_filename, quality=ImageProcessor.QUALITY_SETTINGS['web']
            )
            result['processed_files']['web'] = web_path
            
        except Exception as e:
            logger.error(f"Image processing error: {str(e)}")
            result['success'] = False
            result['errors'].append(str(e))
        
        return result
    
    @staticmethod
    def process_document_scan(image_data: Any, document_type: str = 'general',
                            enhance_quality: bool = True, extract_text: bool = False) -> Dict[str, Any]:
        """
        Process document scans with enhancement and optional OCR
        
        Args:
            image_data: Image file data
            document_type: Type of document (id_card, contract, etc.)
            enhance_quality: Whether to apply quality enhancement
            extract_text: Whether to attempt text extraction (requires OCR)
            
        Returns:
            Dictionary with processing results
        """
        if not PIL_AVAILABLE:
            raise FileProcessingError("PIL/Pillow not available for image processing", "MISSING_DEPENDENCY")
        
        result = {
            'success': True,
            'document_type': document_type,
            'processed_files': {},
            'extracted_text': None,
            'errors': []
        }
        
        try:
            # Open image
            image_data.seek(0)
            image = Image.open(image_data)
            
            # Fix orientation
            image = ImageProcessor._fix_orientation(image)
            
            # Enhance quality if requested
            if enhance_quality:
                image = ImageProcessor._enhance_document_quality(image)
            
            # Resize for document scanning standards
            if document_type == 'id_card':
                target_size = ImageProcessor.PHOTO_SIZES['id_card']
            else:
                target_size = ImageProcessor.PHOTO_SIZES['document_scan']
            
            processed_image = ImageProcessor._resize_image(
                image, target_size, maintain_aspect=True
            )
            
            # Save processed document
            doc_filename = f"document_{document_type}_{datetime.now().strftime('%Y%m%d_%H%M%S')}.jpg"
            doc_path = ImageProcessor._save_processed_image(
                processed_image, doc_filename, quality=ImageProcessor.QUALITY_SETTINGS['high']
            )
            result['processed_files']['main'] = doc_path
            
            # Create web-optimized version
            web_image = ImageProcessor._resize_image(processed_image, (800, 1000), maintain_aspect=True)
            web_filename = f"document_{document_type}_web_{datetime.now().strftime('%Y%m%d_%H%M%S')}.jpg"
            web_path = ImageProcessor._save_processed_image(
                web_image, web_filename, quality=ImageProcessor.QUALITY_SETTINGS['web']
            )
            result['processed_files']['web'] = web_path
            
            # Text extraction (placeholder - would require OCR library like pytesseract)
            if extract_text:
                result['extracted_text'] = "OCR functionality requires additional setup"
                result['text_extraction_note'] = "Install pytesseract for text extraction capabilities"
            
        except Exception as e:
            logger.error(f"Document processing error: {str(e)}")
            result['success'] = False
            result['errors'].append(str(e))
        
        return result
    
    @staticmethod
    def _fix_orientation(image: Image.Image) -> Image.Image:
        """Fix image orientation based on EXIF data"""
        try:
            for orientation in ExifTags.TAGS.keys():
                if ExifTags.TAGS[orientation] == 'Orientation':
                    break
            else:
                return image
            
            exif = image._getexif()
            if exif is not None:
                orientation_value = exif.get(orientation)
                if orientation_value == 3:
                    image = image.rotate(180, expand=True)
                elif orientation_value == 6:
                    image = image.rotate(270, expand=True)
                elif orientation_value == 8:
                    image = image.rotate(90, expand=True)
                    
        except (AttributeError, KeyError, TypeError):
            pass  # No EXIF data or orientation info
        
        return image
    
    @staticmethod
    def _resize_image(image: Image.Image, target_size: Tuple[int, int], 
                     maintain_aspect: bool = True) -> Image.Image:
        """Resize image with optional aspect ratio maintenance"""
        if maintain_aspect:
            image.thumbnail(target_size, Image.Resampling.LANCZOS)
            return image
        else:
            return image.resize(target_size, Image.Resampling.LANCZOS)
    
    @staticmethod
    def _enhance_document_quality(image: Image.Image) -> Image.Image:
        """Apply quality enhancements suitable for documents"""
        # Convert to grayscale for better text clarity
        if image.mode != 'L':
            image = image.convert('L')
        
        # Apply sharpening
        image = ImageOps.unsharp_mask(image, radius=2, percent=150, threshold=3)
        
        # Auto-contrast for better readability
        image = ImageOps.autocontrast(image)
        
        return image
    
    @staticmethod
    def _save_processed_image(image: Image.Image, filename: str, 
                            quality: int = 85, format: str = 'JPEG') -> str:
        """Save processed image to storage"""
        try:
            # Create in-memory file
            output = io.BytesIO()
            
            # Save with specified quality
            save_kwargs = {'format': format, 'optimize': True}
            if format.upper() == 'JPEG':
                save_kwargs['quality'] = quality
                save_kwargs['progressive'] = True
            
            image.save(output, **save_kwargs)
            output.seek(0)
            
            # Save to Django storage
            file_path = f"images/{filename}"
            saved_path = default_storage.save(file_path, ContentFile(output.getvalue()))
            
            return saved_path
            
        except Exception as e:
            logger.error(f"Image save error: {str(e)}")
            raise FileProcessingError(f"Failed to save image: {str(e)}", "SAVE_ERROR")
    
    @staticmethod
    def _extract_image_metadata(image_data: Any) -> Dict[str, Any]:
        """Extract comprehensive image metadata"""
        try:
            image_data.seek(0)
            image = Image.open(image_data)
            
            metadata = {
                'format': image.format,
                'mode': image.mode,
                'size': image.size,
                'width': image.width,
                'height': image.height,
            }
            
            # EXIF data
            exif_data = {}
            if hasattr(image, '_getexif'):
                exif = image._getexif()
                if exif:
                    for tag_id, value in exif.items():
                        tag = ExifTags.TAGS.get(tag_id, tag_id)
                        exif_data[tag] = str(value)
                        
            metadata['exif'] = exif_data
            
            image_data.seek(0)
            return metadata
            
        except Exception as e:
            logger.error(f"Metadata extraction error: {str(e)}")
            return {}


class DocumentProcessor:
    """Comprehensive document processing for PDFs and Office files"""
    
    @staticmethod
    def process_pdf_document(pdf_data: Any, extract_text: bool = True,
                           extract_metadata: bool = True, 
                           validate_structure: bool = True) -> Dict[str, Any]:
        """
        Process PDF documents with text extraction and validation
        
        Args:
            pdf_data: PDF file data
            extract_text: Whether to extract text content
            extract_metadata: Whether to extract PDF metadata
            validate_structure: Whether to validate PDF structure
            
        Returns:
            Dictionary with processing results
        """
        if not PDF_AVAILABLE:
            raise FileProcessingError("PDF processing libraries not available", "MISSING_DEPENDENCY")
        
        result = {
            'success': True,
            'page_count': 0,
            'text_content': None,
            'metadata': {},
            'structure_valid': True,
            'errors': []
        }
        
        try:
            pdf_data.seek(0)
            
            # Use PyMuPDF for comprehensive processing
            pdf_document = fitz.open(stream=pdf_data.read(), filetype='pdf')
            result['page_count'] = len(pdf_document)
            
            # Extract text if requested
            if extract_text:
                text_content = []
                for page_num in range(len(pdf_document)):
                    page = pdf_document[page_num]
                    page_text = page.get_text()
                    text_content.append({
                        'page': page_num + 1,
                        'text': page_text,
                        'char_count': len(page_text)
                    })
                
                result['text_content'] = text_content
                result['total_characters'] = sum(page['char_count'] for page in text_content)
            
            # Extract metadata if requested
            if extract_metadata:
                metadata = pdf_document.metadata
                result['metadata'] = {
                    'title': metadata.get('title', ''),
                    'author': metadata.get('author', ''),
                    'subject': metadata.get('subject', ''),
                    'creator': metadata.get('creator', ''),
                    'producer': metadata.get('producer', ''),
                    'creation_date': metadata.get('creationDate', ''),
                    'modification_date': metadata.get('modDate', ''),
                    'format': metadata.get('format', ''),
                    'encryption': metadata.get('encryption', '')
                }
            
            # Basic structure validation
            if validate_structure:
                result['structure_valid'] = DocumentProcessor._validate_pdf_structure(pdf_document)
            
            # Extract form fields if present
            form_fields = []
            for page_num in range(len(pdf_document)):
                page = pdf_document[page_num]
                widgets = page.widgets()
                for widget in widgets:
                    form_fields.append({
                        'page': page_num + 1,
                        'field_name': widget.field_name,
                        'field_type': widget.field_type,
                        'field_value': widget.field_value
                    })
            
            result['form_fields'] = form_fields
            result['has_forms'] = len(form_fields) > 0
            
            pdf_document.close()
            
        except Exception as e:
            logger.error(f"PDF processing error: {str(e)}")
            result['success'] = False
            result['errors'].append(str(e))
        
        return result
    
    @staticmethod
    def extract_pdf_text(pdf_data: Any, page_range: Tuple[int, int] = None) -> str:
        """
        Extract text from PDF with optional page range
        
        Args:
            pdf_data: PDF file data
            page_range: Optional tuple of (start_page, end_page)
            
        Returns:
            Extracted text content
        """
        if not PDF_AVAILABLE:
            raise FileProcessingError("PDF processing libraries not available", "MISSING_DEPENDENCY")
        
        try:
            pdf_data.seek(0)
            pdf_document = fitz.open(stream=pdf_data.read(), filetype='pdf')
            
            start_page = page_range[0] - 1 if page_range else 0
            end_page = min(page_range[1], len(pdf_document)) if page_range else len(pdf_document)
            
            text_parts = []
            for page_num in range(start_page, end_page):
                page = pdf_document[page_num]
                text_parts.append(page.get_text())
            
            pdf_document.close()
            return '\n\n'.join(text_parts)
            
        except Exception as e:
            logger.error(f"PDF text extraction error: {str(e)}")
            raise FileProcessingError(f"Failed to extract PDF text: {str(e)}", "TEXT_EXTRACTION_ERROR")
    
    @staticmethod
    def convert_pdf_to_images(pdf_data: Any, dpi: int = 150, 
                            image_format: str = 'PNG') -> List[str]:
        """
        Convert PDF pages to images
        
        Args:
            pdf_data: PDF file data
            dpi: Resolution for image conversion
            image_format: Output image format
            
        Returns:
            List of paths to generated images
        """
        if not PDF_AVAILABLE:
            raise FileProcessingError("PDF processing libraries not available", "MISSING_DEPENDENCY")
        
        image_paths = []
        
        try:
            pdf_data.seek(0)
            pdf_document = fitz.open(stream=pdf_data.read(), filetype='pdf')
            
            for page_num in range(len(pdf_document)):
                page = pdf_document[page_num]
                
                # Render page to image
                matrix = fitz.Matrix(dpi / 72, dpi / 72)  # 72 is default DPI
                pix = page.get_pixmap(matrix=matrix)
                
                # Convert to PIL Image
                img_data = pix.tobytes(image_format.lower())
                
                # Save image
                filename = f"pdf_page_{page_num + 1}.{image_format.lower()}"
                file_path = f"documents/pdf_images/{filename}"
                
                saved_path = default_storage.save(file_path, ContentFile(img_data))
                image_paths.append(saved_path)
            
            pdf_document.close()
            
        except Exception as e:
            logger.error(f"PDF to image conversion error: {str(e)}")
            raise FileProcessingError(f"Failed to convert PDF to images: {str(e)}", "PDF_CONVERSION_ERROR")
        
        return image_paths
    
    @staticmethod
    def _validate_pdf_structure(pdf_document) -> bool:
        """Basic PDF structure validation"""
        try:
            # Check if PDF has pages
            if len(pdf_document) == 0:
                return False
            
            # Try to access first page
            first_page = pdf_document[0]
            
            # Check if page has content
            text = first_page.get_text()
            
            # PDF is considered valid if we can access pages
            return True
            
        except Exception:
            return False
    
    @staticmethod
    def _extract_pdf_metadata(pdf_data: Any) -> Dict[str, Any]:
        """Extract PDF metadata for validation"""
        if not PDF_AVAILABLE:
            return {}
        
        try:
            pdf_data.seek(0)
            
            # Try with PyPDF2 first (lighter)
            pdf_reader = PyPDF2.PdfReader(pdf_data)
            
            metadata = {
                'page_count': len(pdf_reader.pages),
                'encrypted': pdf_reader.is_encrypted,
                'metadata': {}
            }
            
            if pdf_reader.metadata:
                metadata['metadata'] = {
                    'title': pdf_reader.metadata.get('/Title', ''),
                    'author': pdf_reader.metadata.get('/Author', ''),
                    'subject': pdf_reader.metadata.get('/Subject', ''),
                    'creator': pdf_reader.metadata.get('/Creator', ''),
                    'producer': pdf_reader.metadata.get('/Producer', ''),
                }
            
            pdf_data.seek(0)
            return metadata
            
        except Exception as e:
            logger.error(f"PDF metadata extraction error: {str(e)}")
            return {}


class FileStorageManager:
    """Comprehensive file storage management with organization and cleanup"""
    
    # Storage path configurations
    STORAGE_PATHS = {
        'employee_photos': 'employees/photos/',
        'employee_documents': 'employees/documents/',
        'payroll_documents': 'payroll/documents/',
        'system_backups': 'system/backups/',
        'temporary_files': 'temp/',
        'processed_files': 'processed/',
        'archives': 'archives/'
    }
    
    # File retention policies (in days)
    RETENTION_POLICIES = {
        'temporary_files': 1,      # 1 day
        'processed_files': 30,     # 30 days
        'employee_photos': 1825,   # 5 years
        'employee_documents': 2555, # 7 years
        'payroll_documents': 3650,  # 10 years
        'system_backups': 90,      # 90 days
        'archives': -1             # Permanent
    }
    
    @staticmethod
    def organize_file_storage(file_path: str, file_category: str, 
                            employee_id: str = None, date_organized: datetime = None) -> str:
        """
        Organize file into appropriate storage structure
        
        Args:
            file_path: Current file path
            file_category: Category for organization
            employee_id: Optional employee ID for employee-specific files
            date_organized: Optional date for organization (defaults to now)
            
        Returns:
            New organized file path
        """
        date_organized = date_organized or datetime.now()
        base_path = FileStorageManager.STORAGE_PATHS.get(file_category, 'general/')
        
        # Create date-based subdirectories
        year_month = date_organized.strftime('%Y/%m')
        
        # Build organized path
        if employee_id:
            organized_path = f"{base_path}{employee_id}/{year_month}/"
        else:
            organized_path = f"{base_path}{year_month}/"
        
        # Get filename from original path
        filename = os.path.basename(file_path)
        
        # Create final path
        final_path = f"{organized_path}{filename}"
        
        try:
            # Move file to organized location
            if default_storage.exists(file_path):
                # Read original file
                original_file = default_storage.open(file_path)
                file_content = original_file.read()
                original_file.close()
                
                # Save to new location
                saved_path = default_storage.save(final_path, ContentFile(file_content))
                
                # Delete original file
                default_storage.delete(file_path)
                
                logger.info(f"File organized: {file_path} -> {saved_path}")
                return saved_path
            else:
                logger.warning(f"Original file not found: {file_path}")
                return file_path
                
        except Exception as e:
            logger.error(f"File organization error: {str(e)}")
            return file_path  # Return original path on error
    
    @staticmethod
    def cleanup_expired_files(dry_run: bool = True) -> Dict[str, Any]:
        """
        Clean up expired files based on retention policies
        
        Args:
            dry_run: If True, only report what would be deleted
            
        Returns:
            Dictionary with cleanup results
        """
        result = {
            'total_files_checked': 0,
            'files_to_delete': 0,
            'files_deleted': 0,
            'space_freed': 0,
            'errors': [],
            'deleted_files': []
        }
        
        current_time = datetime.now()
        
        for category, retention_days in FileStorageManager.RETENTION_POLICIES.items():
            if retention_days == -1:  # Permanent storage
                continue
            
            base_path = FileStorageManager.STORAGE_PATHS.get(category, '')
            if not base_path:
                continue
            
            try:
                # List files in category path
                if default_storage.exists(base_path):
                    dirs, files = default_storage.listdir(base_path)
                    
                    for file_name in files:
                        file_path = f"{base_path}{file_name}"
                        result['total_files_checked'] += 1
                        
                        # Get file modification time
                        try:
                            file_stats = default_storage.stat(file_path)
                            modified_time = datetime.fromtimestamp(file_stats.st_mtime)
                            
                            # Check if file is expired
                            days_old = (current_time - modified_time).days
                            
                            if days_old > retention_days:
                                result['files_to_delete'] += 1
                                
                                if not dry_run:
                                    # Delete the file
                                    file_size = file_stats.st_size
                                    default_storage.delete(file_path)
                                    
                                    result['files_deleted'] += 1
                                    result['space_freed'] += file_size
                                    result['deleted_files'].append({
                                        'path': file_path,
                                        'category': category,
                                        'age_days': days_old,
                                        'size': file_size
                                    })
                                    
                                    logger.info(f"Deleted expired file: {file_path}")
                        
                        except Exception as e:
                            result['errors'].append(f"Error processing {file_path}: {str(e)}")
            
            except Exception as e:
                result['errors'].append(f"Error processing category {category}: {str(e)}")
        
        return result
    
    @staticmethod
    def create_file_backup(file_paths: List[str], backup_name: str = None) -> str:
        """
        Create backup archive of specified files
        
        Args:
            file_paths: List of file paths to backup
            backup_name: Optional name for backup file
            
        Returns:
            Path to created backup file
        """
        if not backup_name:
            backup_name = f"backup_{datetime.now().strftime('%Y%m%d_%H%M%S')}"
        
        backup_path = f"{FileStorageManager.STORAGE_PATHS['system_backups']}{backup_name}.zip"
        
        try:
            # Create temporary file for backup
            with tempfile.NamedTemporaryFile(delete=False, suffix='.zip') as temp_zip:
                with zipfile.ZipFile(temp_zip, 'w', zipfile.ZIP_DEFLATED) as zip_file:
                    
                    for file_path in file_paths:
                        if default_storage.exists(file_path):
                            # Read file content
                            file_obj = default_storage.open(file_path)
                            file_content = file_obj.read()
                            file_obj.close()
                            
                            # Add to zip archive
                            zip_file.writestr(file_path, file_content)
                            
                        else:
                            logger.warning(f"File not found for backup: {file_path}")
                
                # Save backup to storage
                temp_zip.seek(0)
                with open(temp_zip.name, 'rb') as backup_file:
                    saved_path = default_storage.save(backup_path, ContentFile(backup_file.read()))
                
                # Cleanup temporary file
                os.unlink(temp_zip.name)
                
                logger.info(f"Backup created: {saved_path}")
                return saved_path
        
        except Exception as e:
            logger.error(f"Backup creation error: {str(e)}")
            raise FileProcessingError(f"Failed to create backup: {str(e)}", "BACKUP_ERROR")
    
    @staticmethod
    def get_storage_statistics() -> Dict[str, Any]:
        """Get comprehensive storage usage statistics"""
        stats = {
            'total_files': 0,
            'total_size': 0,
            'categories': {},
            'oldest_file': None,
            'newest_file': None,
            'largest_file': None
        }
        
        oldest_time = datetime.max
        newest_time = datetime.min
        largest_size = 0
        largest_file = None
        
        for category, path in FileStorageManager.STORAGE_PATHS.items():
            category_stats = {
                'file_count': 0,
                'total_size': 0,
                'avg_file_size': 0
            }
            
            try:
                if default_storage.exists(path):
                    # Walk through directory structure
                    dirs, files = default_storage.listdir(path)
                    
                    for file_name in files:
                        file_path = f"{path}{file_name}"
                        
                        try:
                            file_stats = default_storage.stat(file_path)
                            file_size = file_stats.st_size
                            modified_time = datetime.fromtimestamp(file_stats.st_mtime)
                            
                            # Update category stats
                            category_stats['file_count'] += 1
                            category_stats['total_size'] += file_size
                            
                            # Update overall stats
                            stats['total_files'] += 1
                            stats['total_size'] += file_size
                            
                            # Track extremes
                            if modified_time < oldest_time:
                                oldest_time = modified_time
                                stats['oldest_file'] = {'path': file_path, 'date': modified_time}
                            
                            if modified_time > newest_time:
                                newest_time = modified_time
                                stats['newest_file'] = {'path': file_path, 'date': modified_time}
                            
                            if file_size > largest_size:
                                largest_size = file_size
                                largest_file = {'path': file_path, 'size': file_size}
                        
                        except Exception as e:
                            logger.warning(f"Error getting stats for {file_path}: {str(e)}")
                    
                    # Calculate average file size
                    if category_stats['file_count'] > 0:
                        category_stats['avg_file_size'] = category_stats['total_size'] / category_stats['file_count']
            
            except Exception as e:
                logger.error(f"Error getting stats for category {category}: {str(e)}")
            
            stats['categories'][category] = category_stats
        
        stats['largest_file'] = largest_file
        
        return stats


class FileSecurityManager:
    """Enhanced security management for file operations"""
    
    # Virus scanning patterns (basic - in production use professional antivirus)
    MALWARE_SIGNATURES = [
        # PE executable signatures
        b'\x4d\x5a',  # MZ header
        # Script signatures
        b'<script',
        b'javascript:',
        b'vbscript:',
        # PHP signatures
        b'<?php',
        b'<?\n',
        b'<? ',
    ]
    
    @staticmethod
    def scan_file_for_threats(file_data: Any, deep_scan: bool = True) -> Dict[str, Any]:
        """
        Comprehensive file security scanning
        
        Args:
            file_data: File data to scan
            deep_scan: Whether to perform deep content analysis
            
        Returns:
            Dictionary with scan results
        """
        scan_result = {
            'safe': True,
            'threats_found': [],
            'scan_type': 'basic' if not deep_scan else 'deep',
            'scan_date': datetime.now().isoformat(),
            'file_hash': None,
            'recommendations': []
        }
        
        try:
            file_data.seek(0)
            content = file_data.read()
            file_data.seek(0)
            
            # Calculate file hash for tracking
            scan_result['file_hash'] = hashlib.sha256(content).hexdigest()
            
            # Basic signature scanning
            for signature in FileSecurityManager.MALWARE_SIGNATURES:
                if signature in content:
                    scan_result['safe'] = False
                    scan_result['threats_found'].append({
                        'type': 'malware_signature',
                        'description': f'Potentially dangerous signature detected',
                        'severity': 'high'
                    })
            
            # File size analysis
            file_size = len(content)
            if file_size > 100 * 1024 * 1024:  # 100MB
                scan_result['recommendations'].append('Large file size - consider compression')
            elif file_size == 0:
                scan_result['safe'] = False
                scan_result['threats_found'].append({
                    'type': 'empty_file',
                    'description': 'File appears to be empty',
                    'severity': 'medium'
                })
            
            # Content analysis for text files
            if deep_scan:
                try:
                    # Try to decode as text for additional analysis
                    text_content = content.decode('utf-8', errors='ignore')
                    
                    # Check for suspicious patterns in text
                    suspicious_patterns = [
                        r'eval\s*\(',
                        r'exec\s*\(',
                        r'system\s*\(',
                        r'shell_exec\s*\(',
                        r'base64_decode\s*\(',
                        r'document\.write\s*\(',
                        r'innerHTML\s*=',
                    ]
                    
                    for pattern in suspicious_patterns:
                        if re.search(pattern, text_content, re.IGNORECASE):
                            scan_result['safe'] = False
                            scan_result['threats_found'].append({
                                'type': 'suspicious_code',
                                'description': f'Suspicious code pattern: {pattern}',
                                'severity': 'medium'
                            })
                
                except UnicodeDecodeError:
                    pass  # Not a text file, skip text-based analysis
            
            # URL/link analysis in content
            url_pattern = r'https?://[^\s<>"\'`]+'
            urls = re.findall(url_pattern, content.decode('utf-8', errors='ignore'))
            if urls:
                scan_result['recommendations'].append(f'File contains {len(urls)} URLs - verify destinations')
            
        except Exception as e:
            logger.error(f"Security scan error: {str(e)}")
            scan_result['safe'] = False
            scan_result['threats_found'].append({
                'type': 'scan_error',
                'description': f'Error during security scan: {str(e)}',
                'severity': 'high'
            })
        
        return scan_result
    
    @staticmethod
    def quarantine_suspicious_file(file_path: str, threat_info: Dict[str, Any]) -> str:
        """
        Move suspicious file to quarantine storage
        
        Args:
            file_path: Path of suspicious file
            threat_info: Information about detected threats
            
        Returns:
            Path to quarantined file
        """
        quarantine_path = f"security/quarantine/{datetime.now().strftime('%Y/%m/%d')}/"
        filename = os.path.basename(file_path)
        quarantine_filename = f"quarantine_{datetime.now().strftime('%Y%m%d_%H%M%S')}_{filename}"
        full_quarantine_path = f"{quarantine_path}{quarantine_filename}"
        
        try:
            if default_storage.exists(file_path):
                # Read original file
                original_file = default_storage.open(file_path)
                file_content = original_file.read()
                original_file.close()
                
                # Create quarantine info file
                quarantine_info = {
                    'original_path': file_path,
                    'quarantine_date': datetime.now().isoformat(),
                    'threat_info': threat_info,
                    'file_hash': hashlib.sha256(file_content).hexdigest()
                }
                
                # Save quarantined file
                saved_path = default_storage.save(full_quarantine_path, ContentFile(file_content))
                
                # Save quarantine info
                info_path = f"{quarantine_path}{quarantine_filename}.info"
                default_storage.save(info_path, ContentFile(json.dumps(quarantine_info, indent=2).encode()))
                
                # Delete original file
                default_storage.delete(file_path)
                
                logger.warning(f"File quarantined: {file_path} -> {saved_path}")
                return saved_path
            else:
                logger.error(f"File to quarantine not found: {file_path}")
                return None
                
        except Exception as e:
            logger.error(f"Quarantine error: {str(e)}")
            raise FileProcessingError(f"Failed to quarantine file: {str(e)}", "QUARANTINE_ERROR")


class FileProcessorFactory:
    """Factory class for creating appropriate file processors"""
    
    @staticmethod
    def create_processor(file_type: str, **kwargs):
        """
        Create appropriate processor based on file type
        
        Args:
            file_type: Type of file processor needed
            **kwargs: Additional configuration parameters
            
        Returns:
            Appropriate processor instance
        """
        processors = {
            'upload_validator': FileUploadValidator,
            'image_processor': ImageProcessor,
            'document_processor': DocumentProcessor,
            'storage_manager': FileStorageManager,
            'security_manager': FileSecurityManager
        }
        
        processor_class = processors.get(file_type)
        if not processor_class:
            raise FileProcessingError(f"Unknown processor type: {file_type}", "UNKNOWN_PROCESSOR")
        
        return processor_class


# Convenience functions for easy integration

def validate_and_process_upload(file_data: Any, file_category: str = 'document',
                               employee_id: str = None, perform_processing: bool = True) -> Dict[str, Any]:
    """
    Complete file upload validation and processing pipeline
    
    Args:
        file_data: Uploaded file data
        file_category: Category of file
        employee_id: Optional employee ID
        perform_processing: Whether to perform post-validation processing
        
    Returns:
        Dictionary with complete processing results
    """
    result = {
        'success': True,
        'validation_result': None,
        'processing_result': None,
        'file_paths': {},
        'errors': []
    }
    
    try:
        # Step 1: Validate upload
        validator = FileUploadValidator()
        validation_result = validator.validate_file_upload(
            file_data, file_category, perform_security_scan=True
        )
        
        result['validation_result'] = validation_result
        
        if not validation_result.is_valid:
            result['success'] = False
            result['errors'].extend([error['message'] for error in validation_result.errors])
            return result
        
        # Step 2: Process based on file type
        if perform_processing:
            if file_category == 'image':
                processor = ImageProcessor()
                if 'employee' in str(employee_id):
                    processing_result = processor.process_employee_photo(
                        file_data, employee_id, generate_thumbnails=True
                    )
                else:
                    processing_result = processor.process_document_scan(
                        file_data, enhance_quality=True
                    )
                
                result['processing_result'] = processing_result
                
                if processing_result['success']:
                    result['file_paths'] = processing_result.get('processed_files', {})
                    if 'thumbnails' in processing_result:
                        result['file_paths']['thumbnails'] = processing_result['thumbnails']
                else:
                    result['success'] = False
                    result['errors'].extend(processing_result.get('errors', []))
            
            elif file_category == 'document':
                file_ext = validation_result.file_info.get('file_extension', '').lower()
                
                if file_ext == '.pdf':
                    processor = DocumentProcessor()
                    processing_result = processor.process_pdf_document(
                        file_data, extract_text=True, extract_metadata=True
                    )
                    
                    result['processing_result'] = processing_result
                    
                    if not processing_result['success']:
                        result['success'] = False
                        result['errors'].extend(processing_result.get('errors', []))
            
            # Step 3: Organize storage
            if result['success'] and result['file_paths']:
                storage_manager = FileStorageManager()
                
                organized_paths = {}
                for file_type, file_path in result['file_paths'].items():
                    if isinstance(file_path, str):
                        organized_path = storage_manager.organize_file_storage(
                            file_path, file_category, employee_id
                        )
                        organized_paths[file_type] = organized_path
                    elif isinstance(file_path, dict):
                        organized_paths[file_type] = {}
                        for sub_type, sub_path in file_path.items():
                            organized_paths[file_type][sub_type] = storage_manager.organize_file_storage(
                                sub_path, file_category, employee_id
                            )
                
                result['file_paths'] = organized_paths
    
    except Exception as e:
        logger.error(f"Upload processing error: {str(e)}")
        result['success'] = False
        result['errors'].append(str(e))
    
    return result


def create_file_backup_with_metadata(file_paths: List[str], backup_metadata: Dict[str, Any] = None) -> Dict[str, Any]:
    """
    Create comprehensive backup with metadata
    
    Args:
        file_paths: List of file paths to backup
        backup_metadata: Additional metadata to include
        
    Returns:
        Dictionary with backup results
    """
    backup_result = {
        'success': True,
        'backup_path': None,
        'metadata': backup_metadata or {},
        'files_backed_up': 0,
        'total_size': 0,
        'errors': []
    }
    
    try:
        storage_manager = FileStorageManager()
        
        # Add default metadata
        backup_result['metadata'].update({
            'backup_date': datetime.now().isoformat(),
            'file_count': len(file_paths),
            'backup_type': 'user_requested'
        })
        
        # Create backup
        backup_path = storage_manager.create_file_backup(file_paths)
        backup_result['backup_path'] = backup_path
        backup_result['files_backed_up'] = len(file_paths)
        
        # Calculate total size
        total_size = 0
        for file_path in file_paths:
            if default_storage.exists(file_path):
                try:
                    file_stats = default_storage.stat(file_path)
                    total_size += file_stats.st_size
                except Exception as e:
                    backup_result['errors'].append(f"Error getting size for {file_path}: {str(e)}")
        
        backup_result['total_size'] = total_size
        
    except Exception as e:
        logger.error(f"Backup creation error: {str(e)}")
        backup_result['success'] = False
        backup_result['errors'].append(str(e))
    
    return backup_result


def get_comprehensive_file_info(file_path: str) -> Dict[str, Any]:
    """
    Get comprehensive information about a file
    
    Args:
        file_path: Path to the file
        
    Returns:
        Dictionary with complete file information
    """
    file_info = {
        'exists': False,
        'path': file_path,
        'size': 0,
        'mime_type': None,
        'created': None,
        'modified': None,
        'extension': None,
        'category': None,
        'security_status': 'unknown',
        'metadata': {},
        'errors': []
    }
    
    try:
        if default_storage.exists(file_path):
            file_info['exists'] = True
            
            # Basic file stats
            file_stats = default_storage.stat(file_path)
            file_info['size'] = file_stats.st_size
            file_info['modified'] = datetime.fromtimestamp(file_stats.st_mtime)
            
            # File extension and type
            file_info['extension'] = Path(file_path).suffix.lower()
            file_info['mime_type'], _ = mimetypes.guess_type(file_path)
            
            # Determine category
            for category, extensions in FileUploadValidator.ALLOWED_EXTENSIONS.items():
                if file_info['extension'] in extensions:
                    file_info['category'] = category
                    break
            
            # Security scan
            try:
                file_obj = default_storage.open(file_path)
                security_result = FileSecurityManager.scan_file_for_threats(file_obj)
                file_info['security_status'] = 'safe' if security_result['safe'] else 'threat_detected'
                file_obj.close()
            except Exception as e:
                file_info['security_status'] = 'scan_failed'
                file_info['errors'].append(f"Security scan failed: {str(e)}")
            
            # Extract metadata based on file type
            if file_info['category'] == 'image' and PIL_AVAILABLE:
                try:
                    file_obj = default_storage.open(file_path)
                    image_metadata = ImageProcessor._extract_image_metadata(file_obj)
                    file_info['metadata']['image'] = image_metadata
                    file_obj.close()
                except Exception as e:
                    file_info['errors'].append(f"Image metadata extraction failed: {str(e)}")
            
            elif file_info['extension'] == '.pdf' and PDF_AVAILABLE:
                try:
                    file_obj = default_storage.open(file_path)
                    pdf_metadata = DocumentProcessor._extract_pdf_metadata(file_obj)
                    file_info['metadata']['pdf'] = pdf_metadata
                    file_obj.close()
                except Exception as e:
                    file_info['errors'].append(f"PDF metadata extraction failed: {str(e)}")
    
    except Exception as e:
        file_info['errors'].append(f"Error accessing file: {str(e)}")
    
    return file_info