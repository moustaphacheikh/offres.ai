# FILE_PROCESSOR_EXAMPLES.py
"""
Usage Examples for File Processing Utilities in Mauritanian Payroll System

This file demonstrates comprehensive usage of the file processing utilities
with practical examples for employee photos, documents, and system files.
"""

from django.core.files.uploadedfile import SimpleUploadedFile
from core.utils.file_processors import (
    FileUploadValidator, ImageProcessor, DocumentProcessor,
    FileStorageManager, FileSecurityManager, FileProcessorFactory,
    validate_and_process_upload, create_file_backup_with_metadata,
    get_comprehensive_file_info
)


def example_employee_photo_processing():
    """Example: Process employee photo with thumbnails and validation"""
    print("=== Employee Photo Processing Example ===")
    
    # Simulate uploaded employee photo
    employee_photo = SimpleUploadedFile(
        "ahmed_hassan_photo.jpg",
        b"fake image content for demonstration",
        content_type="image/jpeg"
    )
    
    # Complete processing pipeline
    result = validate_and_process_upload(
        file_data=employee_photo,
        file_category='image',
        employee_id='EMP001',
        perform_processing=True
    )
    
    if result['success']:
        print("✅ Employee photo processed successfully!")
        print(f"Processed files: {result['file_paths']}")
        
        # Access individual file paths
        if 'profile' in result['file_paths']:
            print(f"Profile photo: {result['file_paths']['profile']}")
        
        if 'thumbnails' in result['file_paths']:
            print(f"Thumbnails: {result['file_paths']['thumbnails']}")
            
    else:
        print("❌ Photo processing failed:")
        for error in result['errors']:
            print(f"  - {error}")
    
    return result


def example_document_validation_and_processing():
    """Example: Validate and process employee documents"""
    print("\n=== Document Processing Example ===")
    
    # Simulate uploaded PDF document
    employee_contract = SimpleUploadedFile(
        "contrat_ahmed_hassan.pdf",
        b"%PDF-1.4 fake pdf content for demonstration",
        content_type="application/pdf"
    )
    
    # Step 1: Validate the document
    validator = FileUploadValidator()
    validation_result = validator.validate_file_upload(
        employee_contract, 
        'document',
        perform_security_scan=True
    )
    
    print(f"Document validation: {'✅ Valid' if validation_result.is_valid else '❌ Invalid'}")
    print(f"File info: {validation_result.file_info}")
    
    if validation_result.warnings:
        print("⚠️ Warnings:")
        for warning in validation_result.warnings:
            print(f"  - {warning['message']}")
    
    # Step 2: Process PDF if valid
    if validation_result.is_valid:
        try:
            processor = DocumentProcessor()
            pdf_result = processor.process_pdf_document(
                employee_contract,
                extract_text=True,
                extract_metadata=True
            )
            
            if pdf_result['success']:
                print(f"📄 PDF processed: {pdf_result['page_count']} pages")
                print(f"Metadata: {pdf_result['metadata']}")
                
                if pdf_result.get('text_content'):
                    total_chars = sum(page['char_count'] for page in pdf_result['text_content'])
                    print(f"Text extracted: {total_chars} characters")
                    
        except Exception as e:
            print(f"❌ PDF processing error: {e}")
    
    return validation_result


def example_security_scanning():
    """Example: Security scanning of uploaded files"""
    print("\n=== Security Scanning Example ===")
    
    # Test files with different security profiles
    test_files = [
        {
            'name': 'safe_document.txt',
            'content': b'This is a safe text document with normal content.',
            'expected': 'safe'
        },
        {
            'name': 'suspicious_file.txt',
            'content': b'<script>alert("potentially malicious")</script>',
            'expected': 'threat'
        },
        {
            'name': 'large_file.txt',
            'content': b'x' * (5 * 1024 * 1024),  # 5MB
            'expected': 'warning'
        }
    ]
    
    security_manager = FileSecurityManager()
    
    for test_file in test_files:
        print(f"\nScanning: {test_file['name']}")
        
        file_obj = SimpleUploadedFile(
            test_file['name'],
            test_file['content'],
            content_type="text/plain"
        )
        
        # Perform security scan
        scan_result = security_manager.scan_file_for_threats(
            file_obj, deep_scan=True
        )
        
        status_icon = "✅" if scan_result['safe'] else "🚨"
        print(f"{status_icon} Security status: {'Safe' if scan_result['safe'] else 'Threat detected'}")
        
        if scan_result['threats_found']:
            print("Threats found:")
            for threat in scan_result['threats_found']:
                print(f"  - {threat['type']}: {threat['description']} (Severity: {threat['severity']})")
        
        if scan_result.get('recommendations'):
            print("Recommendations:")
            for rec in scan_result['recommendations']:
                print(f"  - {rec}")
        
        print(f"File hash: {scan_result.get('file_hash', 'N/A')[:16]}...")


def example_file_organization_and_storage():
    """Example: File organization and storage management"""
    print("\n=== File Organization Example ===")
    
    storage_manager = FileStorageManager()
    
    # Example: Organize employee documents
    test_files = [
        ('temp/employee_contract.pdf', 'employee_documents', 'EMP001'),
        ('temp/payroll_report.pdf', 'payroll_documents', None),
        ('temp/employee_photo.jpg', 'employee_photos', 'EMP002')
    ]
    
    organized_files = []
    
    for file_path, category, employee_id in test_files:
        print(f"\nOrganizing: {file_path} -> {category}")
        
        try:
            # This would normally move actual files
            organized_path = f"organized/{category}/{employee_id or 'general'}/{file_path.split('/')[-1]}"
            organized_files.append(organized_path)
            
            print(f"✅ Organized to: {organized_path}")
            
        except Exception as e:
            print(f"❌ Organization failed: {e}")
    
    # Example: Get storage statistics
    print("\n--- Storage Statistics ---")
    stats = {
        'total_files': 150,
        'total_size': 2.5 * 1024 * 1024 * 1024,  # 2.5GB
        'categories': {
            'employee_photos': {'file_count': 45, 'total_size': 50 * 1024 * 1024},
            'employee_documents': {'file_count': 89, 'total_size': 1.8 * 1024 * 1024 * 1024},
            'payroll_documents': {'file_count': 16, 'total_size': 650 * 1024 * 1024}
        }
    }
    
    print(f"Total files: {stats['total_files']}")
    print(f"Total size: {stats['total_size'] / (1024**3):.2f} GB")
    
    for category, cat_stats in stats['categories'].items():
        size_mb = cat_stats['total_size'] / (1024**2)
        print(f"  {category}: {cat_stats['file_count']} files, {size_mb:.1f} MB")
    
    return organized_files


def example_backup_and_archive():
    """Example: Create backups and archives"""
    print("\n=== Backup and Archive Example ===")
    
    # Files to backup
    critical_files = [
        'employees/documents/EMP001/contract.pdf',
        'employees/photos/EMP001/profile.jpg',
        'payroll/documents/2024/01/payroll_summary.pdf',
        'system/config/payroll_settings.json'
    ]
    
    # Create backup with metadata
    backup_metadata = {
        'backup_type': 'monthly_employee_data',
        'created_by': 'admin_user',
        'department': 'HR',
        'retention_period': '7_years'
    }
    
    print("Creating backup archive...")
    backup_result = create_file_backup_with_metadata(
        critical_files, 
        backup_metadata
    )
    
    if backup_result['success']:
        print(f"✅ Backup created: {backup_result['backup_path']}")
        print(f"Files backed up: {backup_result['files_backed_up']}")
        print(f"Total size: {backup_result['total_size'] / (1024**2):.2f} MB")
        print(f"Backup metadata: {backup_result['metadata']}")
    else:
        print("❌ Backup failed:")
        for error in backup_result['errors']:
            print(f"  - {error}")
    
    return backup_result


def example_file_cleanup_and_maintenance():
    """Example: File cleanup and maintenance operations"""
    print("\n=== File Cleanup and Maintenance Example ===")
    
    storage_manager = FileStorageManager()
    
    # Simulate cleanup operation (dry run)
    print("Performing cleanup analysis (dry run)...")
    cleanup_result = {
        'total_files_checked': 245,
        'files_to_delete': 18,
        'files_deleted': 0,  # Dry run
        'space_freed': 0,
        'errors': [],
        'deleted_files': []
    }
    
    print(f"📊 Cleanup Analysis Results:")
    print(f"  Files checked: {cleanup_result['total_files_checked']}")
    print(f"  Files eligible for deletion: {cleanup_result['files_to_delete']}")
    print(f"  Estimated space to free: {45.2} MB")
    
    # Show what would be deleted
    example_expired_files = [
        {'path': 'temp/upload_20240101_123456.jpg', 'age_days': 35, 'category': 'temporary_files'},
        {'path': 'processed/temp_resize_20240115.jpg', 'age_days': 45, 'category': 'processed_files'},
        {'path': 'system/backups/backup_20231201.zip', 'age_days': 95, 'category': 'system_backups'}
    ]
    
    print("\n📋 Files eligible for cleanup:")
    for file_info in example_expired_files:
        print(f"  - {file_info['path']} ({file_info['age_days']} days old)")
    
    # Show retention policies
    print("\n📝 Current Retention Policies:")
    for category, days in storage_manager.RETENTION_POLICIES.items():
        if days == -1:
            print(f"  {category}: Permanent storage")
        else:
            print(f"  {category}: {days} days")


def example_comprehensive_file_info():
    """Example: Get comprehensive information about files"""
    print("\n=== Comprehensive File Information Example ===")
    
    # Example files to analyze
    test_files = [
        'employees/photos/EMP001/profile.jpg',
        'employees/documents/EMP001/contract.pdf',
        'payroll/reports/monthly_summary.xlsx'
    ]
    
    for file_path in test_files:
        print(f"\n📄 Analyzing: {file_path}")
        
        # This would normally get real file info
        file_info = {
            'exists': True,
            'path': file_path,
            'size': 2048576,  # 2MB
            'mime_type': 'image/jpeg' if 'jpg' in file_path else 'application/pdf',
            'extension': file_path.split('.')[-1],
            'category': 'image' if 'photos' in file_path else 'document',
            'security_status': 'safe',
            'metadata': {}
        }
        
        print(f"  Size: {file_info['size'] / (1024**2):.2f} MB")
        print(f"  Type: {file_info['mime_type']}")
        print(f"  Category: {file_info['category']}")
        print(f"  Security: {file_info['security_status']}")
        
        if file_info['category'] == 'image':
            file_info['metadata']['image'] = {
                'width': 800,
                'height': 600,
                'format': 'JPEG',
                'mode': 'RGB'
            }
            print(f"  Dimensions: {file_info['metadata']['image']['width']}x{file_info['metadata']['image']['height']}")
        
        elif 'pdf' in file_path:
            file_info['metadata']['pdf'] = {
                'page_count': 3,
                'encrypted': False,
                'title': 'Employee Contract',
                'author': 'HR Department'
            }
            print(f"  Pages: {file_info['metadata']['pdf']['page_count']}")
            print(f"  Title: {file_info['metadata']['pdf']['title']}")


def example_batch_processing():
    """Example: Batch processing of multiple files"""
    print("\n=== Batch Processing Example ===")
    
    # Simulate batch of employee files
    employee_files = [
        {
            'file_name': 'ahmed_hassan_photo.jpg',
            'employee_id': 'EMP001',
            'file_type': 'photo',
            'content': b'fake image content'
        },
        {
            'file_name': 'fatima_mint_contract.pdf',
            'employee_id': 'EMP002', 
            'file_type': 'contract',
            'content': b'%PDF fake content'
        },
        {
            'file_name': 'mohamed_ould_id.jpg',
            'employee_id': 'EMP003',
            'file_type': 'id_document', 
            'content': b'fake id scan content'
        }
    ]
    
    print(f"Processing batch of {len(employee_files)} files...")
    
    batch_results = {
        'total_processed': 0,
        'successful': 0,
        'failed': 0,
        'results': []
    }
    
    for file_info in employee_files:
        print(f"\n📁 Processing: {file_info['file_name']} for {file_info['employee_id']}")
        
        # Simulate processing
        file_obj = SimpleUploadedFile(
            file_info['file_name'],
            file_info['content'],
            content_type='image/jpeg' if 'jpg' in file_info['file_name'] else 'application/pdf'
        )
        
        # Determine category
        category = 'image' if file_info['file_type'] in ['photo', 'id_document'] else 'document'
        
        # Process file
        try:
            result = validate_and_process_upload(
                file_obj, 
                category, 
                file_info['employee_id'],
                perform_processing=True
            )
            
            batch_results['total_processed'] += 1
            
            if result['success']:
                batch_results['successful'] += 1
                print(f"  ✅ Success")
            else:
                batch_results['failed'] += 1
                print(f"  ❌ Failed: {', '.join(result['errors'])}")
            
            batch_results['results'].append({
                'file': file_info['file_name'],
                'employee': file_info['employee_id'],
                'success': result['success'],
                'errors': result.get('errors', [])
            })
            
        except Exception as e:
            batch_results['failed'] += 1
            print(f"  ❌ Exception: {e}")
    
    # Batch summary
    print(f"\n📊 Batch Processing Summary:")
    print(f"  Total files: {batch_results['total_processed']}")
    print(f"  Successful: {batch_results['successful']}")
    print(f"  Failed: {batch_results['failed']}")
    print(f"  Success rate: {(batch_results['successful']/batch_results['total_processed']*100):.1f}%")
    
    return batch_results


def example_advanced_image_processing():
    """Example: Advanced image processing features"""
    print("\n=== Advanced Image Processing Example ===")
    
    # Simulate high-resolution employee photo
    high_res_photo = SimpleUploadedFile(
        "employee_high_res.jpg",
        b"fake high resolution image content",
        content_type="image/jpeg"
    )
    
    print("Processing high-resolution employee photo...")
    
    # This would normally use the actual ImageProcessor
    processing_result = {
        'success': True,
        'original_info': {
            'format': 'JPEG',
            'mode': 'RGB',
            'size': (3024, 4032),  # High resolution
            'has_transparency': False
        },
        'processed_files': {
            'profile': 'employees/photos/EMP001/profile.jpg',
            'web': 'employees/photos/EMP001/web.jpg'
        },
        'thumbnails': {
            'small': 'employees/photos/EMP001/thumb_small.jpg',
            'medium': 'employees/photos/EMP001/thumb_medium.jpg', 
            'large': 'employees/photos/EMP001/thumb_large.jpg'
        }
    }
    
    if processing_result['success']:
        print("✅ Image processing completed!")
        
        original = processing_result['original_info']
        print(f"📐 Original: {original['size'][0]}x{original['size'][1]} {original['format']}")
        
        print("📁 Generated files:")
        for file_type, path in processing_result['processed_files'].items():
            print(f"  - {file_type.title()}: {path}")
        
        print("🖼️ Thumbnails:")
        for size, path in processing_result['thumbnails'].items():
            dimensions = {'small': '150x150', 'medium': '300x300', 'large': '600x600'}[size]
            print(f"  - {size.title()} ({dimensions}): {path}")
    
    return processing_result


def run_all_examples():
    """Run all file processing examples"""
    print("🚀 File Processing Utilities - Comprehensive Examples")
    print("=" * 60)
    
    examples = [
        example_employee_photo_processing,
        example_document_validation_and_processing,
        example_security_scanning,
        example_file_organization_and_storage,
        example_backup_and_archive,
        example_file_cleanup_and_maintenance,
        example_comprehensive_file_info,
        example_batch_processing,
        example_advanced_image_processing
    ]
    
    for example_func in examples:
        try:
            example_func()
        except Exception as e:
            print(f"\n❌ Error in {example_func.__name__}: {e}")
    
    print("\n" + "=" * 60)
    print("✅ All examples completed!")
    print("\n💡 Key Features Demonstrated:")
    print("  • File upload validation with security scanning")
    print("  • Image processing with thumbnails and optimization")
    print("  • PDF document processing and text extraction")
    print("  • Secure file storage and organization")
    print("  • Automated backup and archival")
    print("  • File cleanup and maintenance")
    print("  • Comprehensive metadata extraction")
    print("  • Batch processing capabilities")
    print("  • Integration with Django file handling")


if __name__ == "__main__":
    # Run examples when script is executed directly
    run_all_examples()