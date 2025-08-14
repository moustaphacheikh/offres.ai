# 90 — System Administration and Localization

Scope: Administration modules to configure global parameters, security/roles, licensing, closure controls, accounting mappings, branding, SMTP, and localization. This PRD is reconciled with the legacy Java implementation and defines the ground truth for the Django port.

Sources of truth in legacy:
- Entity: `entity/Paramgen.java` (all system parameters)
- Entity: `entity/Utilisateurs.java` (users/roles/permissions)
- UI: `ui/parametres.java` (system parameters admin)
- UI: `ui/securite.java` (users/roles admin)
- UI: `ui/cloture.java` (payroll period closure and rollback)
- Util: `util/NombreEnLettres.java` (number-to-words, FR/BE grammar for MRU)
- Reports: `report/*.jrxml` (branding, date/currency localization)

Assumptions for Django port:
- Admin is role-gated. All writes require an “admin” permission. Audit every change.
- Calculations read parameters at calculation time; parameter changes affect subsequent periods only (no retroactive recompute unless explicitly triggered).
- Email credentials and license secrets will be stored in a secure secrets backend, not in plaintext DB.

## System Configuration (Paramgen)

Admin UI sections and parameters (mapped to `Paramgen` fields; legacy UI in `ui/parametres.java`).

1) Company info and branding
- nomEntreprise, activiteEntreprise, adresse, bd (BP), telephone, fax, siteweb, email
- noCnss, noCnam, noIts (identifiers rendered on declarations)
- villeSiege, signataires, responsableEntreprise, qualiteResponsable
- logo (binary), pub (footer/banner text), bankvirement, comptevirement
- Behavior: Logo upload previews and is persisted as blob; used in reports like `report/bulletinPaie.jrxml`.

2) Period controls and device
- devise (display only in UI; legacy default: MRU)
- periodeCourante (current payroll month), periodeSuivante (next), periodeCloture (date)
- njtDefault (default working days), delaiAlerteFinContrat (days)
- smig (SMIG), plafonIndNonImposable (RNI ceiling), abatement (abatement rate)
- modeITS ("G" or "T"; tax mode), usedIts (flag)
- Behavior: `periodeCourante` is locked if any Paie exists (see `parametres.refresh()`); changes apply prospectively.

3) Options and automation flags
- primePanierAuto, ancienneteAuto, indlogementAuto, remboursementIts
- deductionCnssdeIts, deductionCnamdeIts
- appIndCompensatrice (apply compensatory allowance)
- addCurrentSalInCumulCng (include current month salary in leave cumulative)
- retEngOnConge (deduct commitments during leave)
- Behavior: Flags toggle calculation rules; reflected in payroll processing.

4) SMTP and notifications
- mailSmtpHost, mailSmtpPort, mailUser, mailPassword, mailSmtpTLSEnabled
- UI supports a test email and a “defaults” button (legacy fills `mail.mccmr.com`, port 25, TLS off).
- Security: Do not store plaintext in production; use a secrets backend. Expose masked values only in UI.

5) Accounting mappings (GL export and virement)
- For each component: Compte, Chapitre, Clé fields (e.g., Net, ITS, CNSS, CNAM, RITS, RCNSS, RCNAM; CNSS Méd. D/C; CNSS Pat. D/C; CNAM Pat. D/C).
- Fields: noComptaNet/Chapitre/Cle, noComptaIts/Chapitre/Cle, noComptaCnss/…, noComptaCnam/…, noComptaRits/…, noComptaRcnss/…, noComptaRcnam/…, and CNSS/CNAM variants.
- Behavior: Mandatory for accounting export/virement; format and length validated per accounting system.

6) Licensing and activation
- licenceKey, dateInitLicence, dateCurentLicence, licencePeriodicity, custumerActiveVersion, nbSalaryCode
- UI Activation: Enter code; on activation success, buttons toggle; on deactivation, system reverts to “VERSION DEMO”.
- Enforcement: Closure is blocked if license expired (see `ui/cloture.java` check before closing).

7) Misc
- cheminatt2000 (time-clock import path)
- quotaEcheanceRae (engagements quota % of net)

Validation and business rules
- Required fields: nomEntreprise, periodeCourante, periodeSuivante, periodeCloture, smig, njtDefault, abatement, quotaEcheanceRae, accounting accounts where exports used.
- Period fields must be consistent (courante < suivante). Changing `periodeCourante` while Paie exists is disabled.
- Numeric fields are non-negative. Keys and account codes conform to target ERP patterns.
- SMTP credentials validated via test send; errors don’t persist credentials.
- Every save updates `dateMaj` and writes an audit log entry.

Acceptance criteria
- Admin can view/edit all parameters in grouped tabs (Company, Options, Accounting, Overtime/week config, Signatories, SysRubriques, Activation).
- Changes persist, are audited, and affect only subsequent calculations.
- License activation/deactivation flows match legacy behavior.
- Test email works with TLS toggle. Secrets are masked on read.

Out of scope
- Actual GL posting; covered in Accounting/Integration module.

## Security and Roles (Utilisateurs)

Data model mapping (`entity/Utilisateurs.java`), UI `ui/securite.java`:
- Identity: login (PK), password (hashed in new system), nomusager, dersession.
- Global CRUD flags: ajout, maj, suppression.
- Admin areas: parametre (system params), securite (user admin), cloture (period close), dashboard.
- Master data and payroll scopes: rubriquepaie, grillesb, grillelog, originesal, suppsal, motifpaie.
- Employee permissions: salIdentite, salDiplome, salContrat, salRetenueae, salConge, salHs, salPaie, salAdd, salUpdate, salDoc.

Behavior and rules
- Role-gated UI: buttons for New/Delete/Save are visible/enabled according to flags (see `RolesAction`).
- Cannot manage the reserved user “root” from UI listing.
- Minimal password policy enforced in new system; store hashes; provide reset flow.
- All user changes are audited (who, when, what; include before/after for boolean flags).

Acceptance criteria
- Admin can search, create, update, delete users with appropriate rights.
- Permission flags map 1:1 to backend booleans; UI and API enforce them consistently.
- Attempting restricted actions without permission is rejected and logged.

## Payroll Period Closure and Rollback

UI and flow per `ui/cloture.java`:
- Display current period as “MMMM yyyy” (uppercase, locale FR).
- Actions: Clôturer, Annuler dernière clôture, Continue closure from employee # (optional), Copy fixed rubriques (default on).
- Preconditions: License must be valid; otherwise block action with message.
- On closure: perform period closing steps; show progress; on success, prompt restart of application.
- On cancel last closure: hard-delete current period data from tables Conges, Jour, Weekot, Paie, Rubriquepaie, Njtsalarie, Tranchesretenuesaecheances; set `periodeCourante` to previous month (28th), `periodeSuivante` to old `periodeCourante`; persist; prompt restart.

Acceptance criteria
- Closure and rollback respect license validity guard.
- Continue-from-employee and copy-fixed options are honored during processing.
- After rollback, all listed tables contain no rows for the canceled period; parameters updated accordingly.

## Localization and Formatting

Languages
- Primary: French (FR). Support Arabic (AR) for reports/UI text where specified; ensure fonts in reports and UI support Arabic glyphs. Text direction for AR in UI forms is optional; in reports, align blocks appropriately.

Dates and numbers
- UI date pickers use “dd/MM/yyyy”. Reports use “MMMM yyyy” and “dd/MM/yyyy à HH:mm:ss” with locale FR.
- Currency: MRU (devise). Number formatting uses French grouping/decimal separators in UI and reports.

Number-to-words (util/NombreEnLettres)
- Convert double to words in MRU with centimes.
- Grammar: mille (no plural), million(s), milliard(s); “quatre-vingt(s)” rule in FR; Belgium syntax option (septante, nonante, optionally octante).
- Output pattern: “<entier> MRU” + [“ et <fraction> centime(s)” when fraction > 0].
- Acceptance: Payroll net en lettres on bulletins matches this grammar.

Branding
- Company logo stored as blob and rendered in reports; preview in admin.
- Ensure image scaling preserves aspect ratio; provide recommended dimensions (e.g., 100×100 preview in UI; higher-res in reports).

## API and Admin UX (Django target)

Admin endpoints
- GET/PUT /api/admin/paramgen: read/update parameters (role: parametre && maj). Secrets fields are write-only; GET masks them.
- POST /api/admin/email/test: send test email (role: parametre).
- POST /api/admin/license/activate, POST /api/admin/license/deactivate (role: parametre).
- POST /api/admin/closure/close, POST /api/admin/closure/cancel, POST /api/admin/closure/continue (role: cloture).
- GET/POST/PUT/DELETE /api/admin/users (role: securite).

Validation
- Server-side validation mirrors business rules above; transactional updates; audit logging middleware records user, timestamp, and diff.

Admin UI
- Tabs per legacy: Infos Entreprise, Options, Comptabilité, Heures supp./Semaine, Divers (signataires), SysRubriques, Activation.
- Security screen: user list with search, detail with grouped permissions. Root user hidden.

## Audit, Logging, and Backups
- Each write creates an audit record with: actor, timestamp, entity, before/after, IP.
- Backup prompt before major changes (closure, license deactivate) and before bulk deletes (rollback); scheduled backups recommended.

## Non-functional
- Access control: Enforced server-side; UI hints never replace authorization checks.
- Secrets: Stored via environment or secret manager; never stored or rendered in plaintext.
- Localization: Avoid string literals in code; use i18n catalogs; verify RTL rendering in AR contexts.

Open questions
- Confirm whether Arabic UI is required beyond reports; if yes, define exact screens and RTL support scope.
- Confirm accounting account format/length constraints per target ERP for field validation.
- Confirm whether “usedITS” and “modeITS” combinations have additional constraints.
