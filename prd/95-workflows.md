# 95 — Workflows

This module defines end-to-end operational workflows grounded in the legacy implementation. Each step below cites concrete sources in the legacy UI, entities, and reports to ensure the PRD remains executable.

## Primary: Complete Monthly Payroll Processing

Overview
- Inputs: Attendance device exports (CSV/Excel), employee master data, payroll parameters (period courante, motifs), bank details.
- Systems: Attendance import and valorisation, payroll calculation, payments, and declarations.
- Core entities: `Paie`, `Employe`, `Jour`, `Donneespointeuse`, `Motif`, `Paramgen`.

1) Time Data Processing — Import attendance
- Import sources and formats
	- CSV from terminals: HIKVISION supported; ZKTeco placeholder. UI offers device type select. [ui/att.java]
	- Excel 97-2003 (.xls) import with column mapping and first row index. Time parsing expects "hh:mm:ss a" converted to 24h. [ui/att.java]
	- Direct device import thread for HIKVISION. [ui/att.java]
- Options and controls
	- Date format selection (dd/MM/yyyy, MM/dd/yyyy, yyyy-MM-dd, yyMMdd, ddMMyy). [ui/att.java]
	- Auto CheckIn/CheckOut identification around a reference hour. [ui/att.java]
	- If OUT missing, either ignore the day or use a default OUT time (configurable). [ui/att.java]
- Persistence
	- On import, previous imported pointage from date T can be deleted; new rows inserted into `Donneespointeuse`. [ui/att.java]
- Outputs
	- `Donneespointeuse` rows covering the selected window.
	- Optional Excel exports: EHS Mode 1 and Mode 2 with detailed punches and daily aggregates. Files under `repport/`. [ui/att.java]

2) Attendance Review and Valorisation — Build daily hours and overtime
- Apply timing to compute and insert per-day rows (`Jour`)
	- For each employee/day, derive total hours; split into day vs night (after 22:00 considered night). [ui/att.java]
	- Weekend flag and prime panier computation, with optional automatic rules. [ui/att.java]
	- Insert/replace `Jour` for the current period and selected date window. [ui/att.java]
- Overtime breakdown
	- Compute HS buckets (115%, 140%, 150%, 200%) and totals via `pc.decompterHSIntervall` / `pc.decompterHS`. [ui/att.java]
- Review UI
	- Lists: employees with pointage; detailed punches; per-day JT with HJ/HN/PP/ferié flags; HS totals. [ui/att.java]

3) Payroll Calculation — Execute the monthly run
- UI: "Calcul de paie". [ui/paie.java]
- Parameters
	- Payroll period window defaults to 28–30 day month around `paramsGen.periodeCourante` with `tPaieDu`/`tPaieAu`. [ui/paie.java]
	- Motif filter: "Tous les motifs" or a specific motif. [ui/paie.java]
	- Optional cleanup toggles prior to run: "Correction des engagements" and "Correction de rubriques de paie". [ui/paie.java]
		- EngagementCorrection: deletes `Tranchesretenuesaecheances` slices that have no matching `Paie` base for period. [ui/paie.java]
		- RubriquePaieCorrection: deletes `Rubriquepaie` entries for past periods without `Paie` or current-period entries for employees en congé or null employee. [ui/paie.java]
- Execution
	- Confirmation modal; long-running thread updates a progress bar and status labels. [ui/paie.java]
- Persistence and deletion
	- Produces `Paie` rows for employees for the period/motif(s). [ui/paie.java, entity/Paie.java]
	- "Supprimer la paie courante" deletes `Paie` rows of `periodeCourante` (SQL differs for Oracle vs others). [ui/paie.java]

4) Review & Validation
- Smoke checks after run (functional requirement)
	- Totals consistency: `sum(Paie.net)` equals payment totals; CNSS/CNAM/ITS aggregates match declarations for the period. [ui/declarations.java]
	- Spot-check sample employees: `njt`, `bt/bni`, HS valorisation reflected in `nbrHs`. [entity/Paie.java, ui/att.java]
	- Errors/exceptions: employees en congé should not carry stray `Rubriquepaie`. [ui/paie.java]

5) Payment Processing — Bank transfers
- UI: "Virements bancaires". [ui/virements.java]
- Filters and inputs
	- Select Période, Motif, Banque. Uses rows where `modePaiement == "Virement"`. [ui/virements.java]
- Generation
	- Produces Excel `repport/VB_{BANQUE}_{MM-YYYY}.xls` with columns: ID EMPL., NOM/PRENOM, NNI, N° COMPTE, MONTANT. [ui/virements.java]
	- Amount is sum of `Paie.net` across rows for the employee meeting filters. [ui/virements.java]
- Signatures/footer and total row; "Arrêté à la somme de ..." in MRO. [ui/virements.java]

6) Compliance Reporting — Monthly and quarterly
- UI: "Déclarations mensuelles". [ui/declarations.java]
- Supported documents
	- ITS (mensuelle): computes totals and tranches (T1/T2/T3), abattement 6,000 par salarié et par période de base (motif id==1). [ui/declarations.java]
	- CNSS (trimestrielle): caps monthly BI per employee at 15,000; computes parts 1% (salarié), 2% (médecine), 13% (patronal), total 16%. Options to include only personnel déclaré (with No CNSS). Generates Jasper report and Excel nominative list. [ui/declarations.java]
	- CNAM (trimestrielle): computes assiettes soumises and NJT by month; generates Jasper report and Excel nominative list. [ui/declarations.java]
- Parameters passed to Jasper reports include: période, année, trimestre, masses salariales, effectifs, dates limites. [ui/declarations.java, report/*.jrxml]

7) Compliance Reporting — Annual
- UI: "Déclarations annuelles". [ui/declarationsAnnuelles.java]
- Supported documents
	- Déclaration annuelle ITS: Excel `repport/DEC_AN_ITS_{YYYY}.xls` with per-employee annual totals (BT, CNSS+CNAM, BNI, AVNAT, abattements, RI, ITS). [ui/declarationsAnnuelles.java]
	- Taxe d'apprentissage (exercice courant): Jasper `dTA.jrxml` using remuneration imposable and TA = 0.6%. [ui/declarationsAnnuelles.java, report/dTA.jrxml]

8) Period Closure
- Close the month after payments and declarations are generated and validated. Update `Paramgen.periodeCourante` to the next month. Ensure archived outputs (bank, declarations, bulletins) are retained under `repport/` (Excel) and `report/` (Jasper exports). [assumption; system behavior inferred from usage]

Alternative paths
- Corrections/reprocessing
	- Use deletion of current payroll then rerun after fixing pointage/JT or parameters. [ui/paie.java, ui/att.java]
- Partial subsets
	- Filter by Motif and, when applicable, by bank for payments. Department-level partials are supported in exports (Mode 2 includes dept/service in Excel) but payroll engine operates at period/motif scope. [ui/att.java, ui/virements.java]
- Retroactive adjustments
	- See "Advanced: Retroactive Payroll Adjustments"; corrections to JT (NJT/HS) and re-run affect ITS/CNSS/CNAM bases and payments. [ui/att.java, ui/paie.java]

Source mapping
- Attendance import/valorisation: `ui/att.java`; Entities: `entity/Donneespointeuse.java`, `entity/Jour.java`.
- Payroll run and cleanup: `ui/paie.java`; Entities: `entity/Paie.java`, `entity/Rubriquepaie.java`, `entity/Tranchesretenuesaecheances.java`.
- Payments: `ui/virements.java`; Reports: Excel via `util/WriteExcel`.
- Declarations (monthly/quarterly): `ui/declarations.java`; Reports: `report/declarationCNSS.jrxml`, `report/declarationCNAM.jrxml`, `report/dITS.jrxml` and Excel LN outputs.
- Declarations (annual): `ui/declarationsAnnuelles.java`; Reports: `report/dTA.jrxml` and Excel ITS annuel.

## Secondary: New Employee Setup (Onboarding)

Scope: Minimum data required so the employee appears correctly in payroll, payments, and declarations.
- Create employee with identity and classification fields; assign department/service/poste and motif eligibility. [implicit from `entity/Employe.java` and `entity/Paie.java` fields]
- Banking: bank name and account number used for virements; `modePaiement` if salary by transfer. [entity/Paie.java]
- Social numbers: CNSS/INAM/NNI for declarations and nominative lists. [ui/declarations.java]
- Verify in attendance pane that the employee is associated to pointage ID (IDP) if using terminals. [ui/att.java]

## Advanced: Document Lifecycle Management

Documents produced by these workflows and their lifecycle
- Payroll artifacts: bulletins, virements Excel, declarations (Jasper/Excel), nominative lists. [report/*.jrxml, ui/*]
- Requirements
	- Naming: keep legacy patterns (e.g., `VB_{BANQUE}_{MM-YYYY}.xls`, `LN_CNSS_{MMMM-YYYY}.xls`, `LN_CNAM_TRIM_{MMMM-YYYY}.xls`, `DEC_AN_ITS_{YYYY}.xls`). [ui/virements.java, ui/declarations.java, ui/declarationsAnnuelles.java]
	- Storage: under `repport/` for Excel and `report/` for compiled Jasper outputs. [ui/*]
	- Signatures/headers: carry company info from `Paramgen`. [ui/*]

## Advanced: Retroactive Payroll Adjustments

When errors are found post-run, adjust inputs and re-run deterministically.
- Typical corrections
	- Fix pointage anomalies (add OUT, adjust hours), then re-apply JT (HJ/HN/HS) and recompute HS buckets. [ui/att.java]
	- Toggle cleanup (engagements/rubriques) to remove stale slices. [ui/paie.java]
- Recompute payroll
	- Delete current period `Paie` if needed, then compute again for the corrected period/motif(s). [ui/paie.java]
- Downstream effects
	- Regenerate bank virement Excel and re-issue ITS/CNSS/CNAM declarations for impacted periods. [ui/virements.java, ui/declarations.java]

## Advanced: Licensing & System Administration

Operational dependencies
- Period parameterization (`Paramgen.periodeCourante`) drives default windows and reporting periods. [ui/paie.java, ui/declarations.java]
- Localization and company identifiers (No CNSS, No CNAM, NIF/ITS) must be set for report headers and totals. [ui/declarations.java, ui/declarationsAnnuelles.java]

---

Change log (reconciled with legacy code)
- Replaced outline with concrete, code-backed steps for attendance import, valorisation, payroll run, payments, and declarations.
- Added cleanup behaviors (engagements/rubriques) prior to payroll run and delete-current-payroll operation.
- Specified file naming conventions and columns for virement exports and nominative lists.
- Documented CNSS cap (15,000), ITS abattement (6,000 per base period), and TA = 0.6% as implemented.
- Added edge cases: missing OUT handling, congé cleanup, Oracle vs non-Oracle SQL differences on deletion.

Notes and gaps
- No dedicated legacy UI found for leave requests/approvals; current behavior only cleans related rubriques during payroll. Future workflow spec for leave should be covered in the leave module. [search outcome]
- Device import for ZKTeco is a stub; keep as backlog item if required.
