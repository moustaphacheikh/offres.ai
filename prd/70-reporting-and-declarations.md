# 70 — Reporting & Declarations

This module covers payslip rendering, statutory declarations (CNSS, CNAM, ITS, TA), and management/bank reports. Legacy ground-truth: JasperReports templates under `report/` and data provided by entities like `Employe`, `Paramgen`, `Listenominativecnss`, `Listenominativecnam`.

Sources (authoritative):
- Payslip: `report/bulletinPaie.jrxml` (already referenced in Payroll module)
- CNSS: `report/declarationCNSS.jrxml`, dataset fields from `entity/Listenominativecnss.java`
- CNAM: `report/declarationCNAM.jrxml`, fields align to `entity/Listenominativecnam.java`
- ITS (monthly DGI): `report/dITS.jrxml` and `report/dITSOld.jrxml`
- TA (Taxe d'Apprentissage, annual): `report/dTA.jrxml`
- Virement bancaire: `report/virementbank.jrxml`
- Employer/company parameters consumed in all reports: `entity/Paramgen.java`

Assumptions (explicit):
- Where legacy uses free-text placeholders for dates/signatures/quittance, new app will render inputs but not persist them unless explicitly required by a workflow (out of scope here).
- Monetary values use MRU; formatting pattern `#,##0.00` where decimals are meaningful; otherwise `#,##0`.

## Feature: Payslip Generation
- Description: Produce individual bulletins with itemized earnings, deductions, employer shares, and totals.
- Acceptance Criteria:
	- Layout parity with `bulletinPaie.jrxml` for data elements and totals; company header from `Paramgen`.
	- Mandatory identifiers on slip: Employee NNI, CNSS/CNAM numbers, period, matricule interne, poste/dept.
	- Net to pay shown both numeric and in words (French). See existing conversion utility in legacy UI; replicate.
- Business Rules:
	- Source fields from payroll engine outputs (rubriques, bases, cotisations); totals consistent with engine.
	- If bank payment: display IBAN/compte if available (`Paie.noCompteBanque`).
	- Internationalization: French and Arabic labels as present in templates are nice-to-have; core is French.
- Dependencies: Validated payroll run for a period; company parameters completed in `Paramgen`.

## Feature: Regulatory Declarations

### CNSS Déclaration Trimestrielle
- Template: `report/declarationCNSS.jrxml`
- Parameters required (must be computed by backend):
	- DECLARATIONCNSSTRIM_PERIODE (Timestamp), DECLARATIONCNSSTRIM_ANNEE (Int), DECLARATIONCNSSTRIM_TRIMESTRE (Int)
	- DECLARATIONCNSSTRIM_PLAFONDTOT (Double)
	- DECLARATIONCNSSTRIM_PARTIEPATR (Double) = plafondTot * 0.13 per template label “x 13%”
	- DECLARATIONCNSSTRIM_PARTIEEMPL (Double) = plafondTot * 0.01
	- DECLARATIONCNSSTRIM_PARTIEMEDE (Double) = plafondTot * 0.02
	- DECLARATIONCNSSTRIM_TOTAL (Double) = PARTIEPATR + PARTIEEMPL + PARTIEMEDE
	- DECLARATIONCNSSTRIM_NBSAL (Int) = Count of distinct employees in DS1
	- Employer header params from Paramgen: NOMENTREPRISE, ACTIVITEENTREPRISE, TELEPHONE, FAX, ADRESSE, BD, SITEWEB, EMAIL, NOCNSS, VILLESIEGE
- Detail dataset (Collection DS1) element shape matches `Listenominativecnss`:
	- noCnssemploye, nomEmploye, nbJour1erMois, nbJour2emeMois, nbJour3emeMois, totalNbJour (Double), remunerationReeles (Double), plafond (Double), dateEmbauche, dateDebauche
- Validations:
	- Sum(remunerationReeles) equals TOTAL_RI variable; Sum(plafond) equals TOTAL_PLAF; both drive header amounts; cross-check before render.
	- For each employee, `totalNbJour = parseInt(nbJour1erMois)+…` if not provided; ensure consistent type.
	- Period consistency: all DS1 rows belong to same trimestre.

### CNAM Déclaration Trimestrielle (DTS)
- Template: `report/declarationCNAM.jrxml`
- Parameters:
	- DECLARATIONCNAMTRIM_ANNEE (Int), DECLARATIONCNAMTRIM_TRIMESTRE (Int)
	- DECLARATIONCNAMTRIM_MASSE_S1, _S2, _S3 (Double): salary mass subject to CNAM per month
	- DECLARATIONCNAMTRIM_EFFECTIF (Int)
	- DECLARATIONCNAMTRIM_DATE_LIMIT (String) displayed reminder
	- Month captions: DECLARATIONCNAMTRIM_MOIS1/2/3 (String)
	- Employer: PARAMGEN_* including PARAMGEN_NOCNAM and VILLESIEGE
- Calculations enforced by template:
	- Part patronale = (S1+S2+S3) * 0.05; Part salariale = (S1+S2+S3) * 0.04; Cotisation sociale total = (S1+S2+S3)*0.09
- Validations:
	- EFFECTIF equals count of nominative lines for the period.
	- Sum of per-employee assiettes by month matches S1/S2/S3.
	- Optional nominative support can use `Listenominativecnam` fields: no, noCnam, nni, nomEmploye, dates, assieteSoumiseMois1..3, nbJourMois1..3.

### ITS (Impôt sur les Traitements et Salaires) — Mensuel
- Templates: `report/dITS.jrxml` (current) and `report/dITSOld.jrxml` (legacy layout). Use current layout by default.
- Parameters (from template):
	- DECLARATIONITS_PERIODE (Date), DECLARATIONITS_NBSALARIES (Int)
	- DECLARATIONITS_REMUNERATIONBRU (Double)
	- DECLARATIONITS_AVANTAGESNATURE (Double)
	- DECLARATIONITS_ABATEMENTFORFET (Double) = 6000 MRU × NBSALARIES
	- DECLARATIONITS_REMUNERATIONNON (Double)
	- DECLARATIONITS_REMUNERATIONIMP (Double) = (BRU + AVANTAGES) - (ABATTEMENT + NON)
	- DECLARATIONITS_REMUNERATIONT1/T2/T3 (Double) — tax due per bracket as computed by engine
	- DECLARATIONITS_TOTALITS (Double) = T1+T2+T3
	- Employer header: PARAMGEN_NOMENTREPRISE, ACTIVITEENTREPRISE, ADRESSE, BD, TELEPHONE, EMAIL, VILLESIEGE, NOITS
- Validations:
	- BRU, AVANTAGES, NON, IMP are consistent: IMP = BRU+AVANTAGES-NON-ABATTEMENT; non-negative.
	- NBSALARIES equals count of employees with taxable pay in month.
	- T1/T2/T3 match brackets in the taxation module; TOTALITS equals sum; compare to sum of per-employee ITS.

### Taxe d’Apprentissage (TA) — Annuelle
- Template: `report/dTA.jrxml`
- Parameters:
	- periodeTaxe (String date like 31/12/yyyy)
	- remunerationImposable (Double), avantagesEnNature (Double)
	- remunerationGlobale, remunerationNonImposable (present but not always displayed)
	- Montant imposable = remunerationImposable + avantagesEnNature (mapped to template expression)
	- Taux = 0.6% (template label); taxeApp (Double) = Montant imposable × 0.006
	- Employer: PARAMGEN_* and NOITS
- Validations:
	- Cross-check that remunerationImposable equals yearly taxable base from payroll.
	- Ensure taxeApp recomputes to displayed value within rounding tolerance.

### Virements bancaires — Mensuel par banque
- Template: `report/virementbank.jrxml`
- Inputs:
	- Parameters: PERIODE (Date), BANQUE (String)
	- SQL fields returned: salaire (SUM NET), paie_noCompteBanque, employe_nni, employe_nom, employe_prenom, employe_id, and `paramgen_*` header fields including bankvirement, comptevirement, nomEntreprise, logoSociete
- Output:
	- One line per employee with account; grouped by employee and account; summary TOTAL of all salaires for the bank
- Validations:
	- Only include employees where `Paie.banque = BANQUE` and `Paie.periode = PERIODE`.
	- Account field present; otherwise line is excluded or flagged.

## Feature: Management Reporting
- Description: Analytical reports and exports (PDF/Excel/CSV) to support HR and finance.
- Acceptance Criteria:
	- Parameterized by period, department, direction, employment status.
	- Export to PDF for formatted reports; CSV/Excel for data extracts; UNL if required by downstream tooling.
	- Provide trend and comparative views across months/quarters; drill-down to employee level.
- Business Rules:
	- Enforce access control: user sees only authorized org units and anonymized sensitive fields where appropriate.
	- Data sourced from authoritative payroll tables; recomputation avoided in reports when possible.

## Cross-cutting
- Number/date formatting per templates (`#,##0` and `#,##0.00`; `dd/MM/yyyy`).
- Employer identity and numbers (CNSS, CNAM, NIF/NOITS, address, ville) come from `Paramgen` and must be maintained before any declaration.
- Arabic labels exist in ITS templates; French is primary; do not hard-fail if Arabic fonts are unavailable.
