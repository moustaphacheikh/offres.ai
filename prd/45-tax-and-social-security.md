## 45 — Tax & Social Security

This module covers statutory deductions and taxes: CNSS (social security), CNAM (health insurance), ITS (income tax), and their reimbursements, as implemented in the legacy system.

### Scope and goals
- Compute employee-side deductions and store in `Paie`: CNSS, CNAM, ITS, ITS per tranche.
- Compute reimbursement components: RCNSS, RCNAM, RITS using employee-specific rates.
- Support employer-side amounts in reports (derived from employee-side values; not persisted in `Paie`).
- Respect motif and employee flags (detached, exonerated, expatriate) and global parameters (abatement, ceilings, deduction toggles, ITS mode).

### Data in `Paie` (persisted)
- cnss, cnam — employee contributions
- rcnss, rcnam — employee reimbursements for CNSS/CNAM
- its, itsTranche1, itsTranche2, itsTranche3 — employee ITS and per-tranche breakdown
- biCnss, biCnam, biAvnat — bases used for CNSS/CNAM and advantages in-kind
- Sources: entity/Paie.java

### CNSS — Social Security (employee-side persisted, employer in reports)
- Base: sum of rubriques with `Rubrique.cnss=true` and `sens=G`, minus Brut-deducted retenues; motif/employee guards apply.
- Ceiling: base capped at 15,000 per month.
- Employee rate: 1% of capped base.
- Reimbursement (RCNSS): employee rate adjusted by `Employe.tauxRemborssementCnss`.
- Employer-side (for declarations/reports only):
  - “CNSS PAT.” = 13% of the same capped base (derived as `cnss * 13`).
  - “CNSS MED.” = 2% of capped base (derived as `cnss * 2`).
- Flags/guards:
  - Skip if `Employe.detacheCnss = true` or `Motif.employeSoumisCnss = false`.
- Sources: util/PaieClass.CNSSm, RCNSSm; ui/etats.java CNSS EMP./MED./PAT.; entity/Employe, entity/Motif

### CNAM — Health Insurance (employee-side persisted, employer in reports)
- Base: sum of rubriques with `Rubrique.cnam=true` and `sens=G`, minus Brut-deducted retenues; motif/employee guards apply.
- Employee rate: 4% of base (no ceiling).
- Reimbursement (RCNAM): employee CNAM times `Employe.tauxRemborssementCnam`.
- Employer-side (for declarations/reports only): “CNAM PAT.” = 5% of base (derived as `cnam * 5/4`).
- Flags/guards:
  - Skip if `Employe.detacheCnam = true` or `Motif.employeSoumisCnam = false`.
- Sources: util/PaieClass.CNAMm, RCNAMm; ui/etats.java CNAM EMP./PAT.; entity/Employe, entity/Motif

### ITS — Income Tax (employee-side persisted)
- Taxable base (monthly):
  - Start with sum of rubriques with `Rubrique.its=true` and `sens=G`.
  - Add any excess of non-ITS gains marked `plafone=true` over `Paramgen.plafonIndNonImposable`.
  - Subtract Brut-deducted retenues.
  - Apply abatement: `Paramgen.abatement`.
  - Optionally deduct employee CNSS/CNAM if `Paramgen.deductionCnssdeIts` / `Paramgen.deductionCnamdeIts` are true.
  - Advantages in-kind (AvNat): if AvNat > 20% of (SalaireBase - AvNat), deduct only 60% of AvNat; else deduct 100% of AvNat.
- Tranches and rates (monthly engine):
  - Thresholds: T1 = 9,000; T2 = 21,000.
  - T1 rate: 15%; for expatriates, halved to 7.5%.
  - T2 rate: 25% by default; if `Paramgen.modeITS == "T"`, 20%. Halved for expatriates.
  - T3 rate: 40% by default; if `Paramgen.modeITS == "T"`, 20%. Halved for expatriates.
- Reimbursement (RITS): computed per tranche using `Employe.tauxRembItstranche1/2/3` over the corresponding tranche amounts.
- Outputs: total ITS and its per-tranche amounts persisted in `Paie`.
- Flags/guards:
  - Skip if `Employe.exonoreIts = true` or `Motif.employeSoumisIts = false`.
- Sources: util/PaieClass.tranche1ITS/tranche2ITS/tranche3ITS/ITSm/RITSm; entity/Employe, entity/Motif, entity/Paramgen

### Bases snapshotting
- `Paie.biCnss` and `Paie.biCnam` store the employed bases after adjustments.
- `Paie.biAvnat` stores advantages-in-kind encountered.
- Source: util/PaieClass.paieCalcule (setting of biCnss/biCnam/biAvnat)

### Motifs and exemptions
- All deductions respect motif-level “soumis” flags (`Motif.employeSoumisIts/Cnss/Cnam`).
- Employee-level exemptions: `detacheCnss`, `detacheCnam`, `exonoreIts`, `expatrie` (affects ITS rates).
- Sources: entity/Motif.java; entity/Employe.java; util/PaieClass.paieCalcule

### Declarations and reporting
- Employee-side amounts (cnss, cnam, its, itsTranche1/2/3; rcnss, rcnam, rits) are persisted in `Paie` and used in reports.
- Employer amounts are derived in reports:
  - CNSS PAT. = cnss * 13; CNSS MED. = cnss * 2; CNAM PAT. = cnam * 5/4.
- Report templates: `report/declarationCNSS.jrxml`, `report/declarationCNAM.jrxml`, `report/dITS.jrxml`.
- Listenominative entities exist for per-employee listings (legacy): `entity/Listenominativecnss.java`, `entity/Listenominativecnam.java`, `entity/Listenominativecnss.java`.
- Sources: report/*.jrxml; entity/Listenominative*; ui/etats.java; entity/Paramgen (registration numbers and accounting mappings)

### Validations and constraints
- CNSS ceiling strictly applied to the base before rate.
- CNAM uncapped.
- ITS tranche computations use Paramgen toggles: `abatement`, `modeITS`, and deduction of CNSS/CNAM.
- Motif and employee flags gate inclusion; deductions not persisted when flags disable them.
- Advantages-in-kind handling per 20% rule before applying ITS.

### Acceptance criteria
- For a processed (employee, motif, period):
  - `Paie.cnss` equals 1% of min(base, 15000) when not detached and motif soumis.
  - `Paie.cnam` equals 4% of base when not detached and motif soumis.
  - `Paie.itsTranche1/2/3` match tranche functions with Paramgen settings and expatriate handling; `Paie.its` equals their sum.
  - `Paie.rcnss/rcnam/rits` reflect employee reimbursement rates.
  - Reported employer CNSS/CNAM figures derive from persisted employee contributions per formulas above.

### Source references
- entity/Paie.java — persisted fields
- entity/Employe.java — flags and reimbursement rates
- entity/Motif.java — soumis flags
- entity/Paramgen.java — abatement, deduction toggles, modeITS, plafonIndNonImposable, registration/mapping fields
- util/PaieClass.java — CNSSm/RCNSSm, CNAMm/RCNAMm, ITSm and tranche methods, taxable base assembly
- ui/etats.java — derivation and display of employer-side amounts in exports
- report/declarationCNSS.jrxml, report/declarationCNAM.jrxml, report/dITS.jrxml — declaration templates
- entity/Listenominativecnss.java, entity/Listenominativecnam.java — legacy listing entities