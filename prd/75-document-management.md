# 75 — Document Management

Scope: Manage employee-related documents (IDs, contracts, certificates) with secure storage, metadata, and lifecycle controls. Ground truth reflects the legacy Java implementation; target design extends capabilities for the Django port.

## Legacy baseline (as-is)
- Data model: `Document` entity stores one file per row.
  - Fields: `id` (PK), `nom` (name), `docFile` (binary blob), `fileType` (MIME/extension), `employe` (ManyToOne) — Source: `entity/Document.java`.
  - Relationship: `Employe` has `Set<Document> documents` — Source: `entity/Employe.java`.
- Behaviors/UI: No explicit legacy UI tied to `Document` found; storage is DB BLOB with filename and type. No versioning/expiry/signature/workflow.

Implications: The legacy supports basic per-employee file attachment only. Everything else in this module is a target enhancement.

## Target objectives (to-be)
- Upload, store, and retrieve documents per employee with metadata and preview/download.
- Categorize documents by type (e.g., Passport, Visa, Work Permit, Contract, Diploma, CNSS Card).
- Optional expiry tracking with alerts (especially for expatriate docs). Note: Legacy employee fields already track passport/visa/work-permit dates; documents will have their own optional `expires_on`.
- Version control: keep prior versions and audit trail of changes.
- Access control: restrict by role and employee visibility; log access.
- Bulk operations: import/export metadata, batch upload via CSV/ZIP.

## Data model (target)
- Document
  - id (UUID/int)
  - employee_id (FK → Employee)
  - name (string)
  - type (enum/string; examples below)
  - file_url or storage_key (string); store files on disk/object storage, not DB blobs
  - file_mime (string), file_size (int)
  - issued_on (date, optional)
  - expires_on (date, optional)
  - issuer (string, optional)
  - status (active | expired | superseded)
  - checksum (sha256, optional for integrity)
  - created_at, created_by, updated_at, updated_by
- DocumentVersion
  - id, document_id (FK)
  - version_no (int), file_url, file_mime, file_size, checksum, created_at, created_by, notes
- DocumentRequirement (configuration)
  - id, employee_type/contract_type/origine/expatrie (flags/filters)
  - document_type (enum), required (bool), renew_before_days (int), retention_years (int)

Notes:
- Legacy mapping: `docFile` (BLOB) → move to external storage; store URL/key in DB. Keep `name`/`fileType` for backward compatibility during migration.
- Relationship maintained: Employee 1—N Document (Source: `entity/Employe.java`).

### Document type catalog (initial)
- Identity: Passport, National ID (NNI), Residence Permit, Work Permit, CNSS Card, CNAM Card
- Employment: Offer Letter, Contract, Addendum, Termination Letter
- Compliance: Medical Certificate, Diploma/Training, Tax Certificate, Others (free-text)

## Workflows
1) Upload
   - From employee profile → Documents tab → Select type, set metadata, upload file, save as version 1.
   - Validate extension/MIME, max size, and duplicate by checksum.
2) Update/Version
   - Replace file → new `DocumentVersion`; mark previous as superseded; update `Document.status`.
3) Expiry tracking
   - Nightly job evaluates `expires_on` and sends reminders at configurable thresholds (e.g., 60/30/7 days). For expatriates, cross-check with `Employe.dateExpirationPassport`, `dateFinVisa`, `dateExpirationPermiTravail` (Source: `entity/Employe.java`).
4) Access/Download
   - Only authorized roles can view/download; all access logged with user/time.
5) Bulk
   - CSV metadata + ZIP of files; map by employee identifier.

## Permissions and security
- RBAC: HR admins (full), HR staff (read/upload), Managers (read within org), Employee self-service (own docs subset, if enabled).
- PII protection: store files outside repo; use signed URLs or authenticated streaming.
- Validation: file-type whitelist (PDF, JPG, PNG, DOCX), max size (configurable), virus scan hook (optional).
- Audit: create/update/delete/version and download events logged.

## UI (target)
- Employee profile → Documents tab:
  - Table: Type, Name, Issued on, Expires on, Status, Version, Size, Actions (Preview/Download/Replace/Delete).
  - Filters: by type, status (expired/expiring soon), has/has not.
  - Badges: Expired (red), Expiring soon (amber), OK (green).

## Integrations and dependencies
- Depends on: Employee record, authentication/authorization, scheduler for reminders, storage backend (filesystem/S3-equivalent), email/notifications.
- Alignment with legacy: preserves employee linkage and basic metadata; extends storage beyond DB blobs.

## Migration
- Export legacy `Document.docFile` to files; populate `file_url`, `file_mime`, `file_size`, `checksum`.
- Keep `name`=`nom` and `type`=`fileType` mappings; link to employee by legacy FK.

## Acceptance criteria
- Upload/download works for permitted roles; validations enforced; audit logged.
- Create/replace preserves prior versions; history viewable.
- Expiry flags computed; reminders sent per configuration.
- Bulk import supports at least CSV+ZIP; errors reported per-row.
- Storage is external to DB; metadata searchable and filterable.

## Source references (legacy)
- `entity/Document.java`: fields id, nom, docFile, fileType, employe (ManyToOne)
- `entity/Employe.java`: `Set<Document> documents`; expatriate validity fields (passport/visa/work-permit dates)
- No dedicated legacy UI found for documents.

## Change log
- Reconciled with legacy: documented actual fields/relations; confirmed lack of versioning/expiry/workflows.
- Added target data model (Document, DocumentVersion, DocumentRequirement), workflows, and security.
- Specified migration from DB BLOBs to file storage with metadata.
