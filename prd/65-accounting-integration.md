# 65 — Accounting Integration

This module specifies how payroll results are turned into accounting entries, exports, and summaries. It’s grounded in the legacy implementation and report exports so new developers can implement it consistently.

Sources: `ui/compta.java` (export & piece generation), `entity/Masterpiece.java`, `entity/Detailpiece.java`, `entity/Paramgen.java`, `entity/Rubrique.java`, `entity/Banque.java`, `entity/Paie.java`.

## Scope
- Generate accounting journal entries (master/detail) per payroll period and motif.
- Export accounting data as:
  - Excel balance by accounts (Compta Paie).
  - Excel “Pièce comptable” detail data.
  - UNL text file (58 fields, pipe-delimited) per target accounting system.
- Keep totals balanced (Debits = Credits) and auditable.

## Data model and relationships
- Masterpiece (header) [src: `entity/Masterpiece.java`]
  - Key fields: NUMERO (string, id), DATEOP (date), LIBELLE_SERVICE (string), RUBRIQUE (string: motif name), BENEFICIAIRE (string, legacy uses "-"), TOTAL_DEBIT (double), TOTAL_CREDIT (double), INITIATEUR (username), INIT_HR (timestamp).
  - Relations: One-to-many Detailpiece via `DETAILPIECES` mappedBy `NUPIECE`.
- Detailpiece (line) [src: `entity/Detailpiece.java`]
  - Key fields: NUMLIGNE (id), DATEOP, JOURNAL (legacy constant "PAI"), COMPTE (string), LIBELLE, MONTANT (double), SENS ("D"/"C"), INTITULET (title), CVMRO_MONTANT (mirror of MONTANT), DEVISE (legacy constant "UM"), COURS (1), NUMERO_COURS (1), NUPIECE (ref Masterpiece).
- Configuration (Paramètres comptables) [src: `entity/Paramgen.java`]
  - Per-accounting-topic numbers: e.g., `noComptaNet`, `noComptaChapitreNet`, `noComptaIts`, `noComptaChapitreIts`, `noComptaCnss`, `noComptaChapitreCnss`, `noComptaCnam`, `noComptaChapitreCnam`, plus employer/medecine variants and reimbursement accounts (RITS/RCNSS/RCNAM), and their “Clé” strings.
  - Current period control: `periodeCourante` drives authorization for generation.
- Chart of accounts on Rubrique [src: `entity/Rubrique.java`]
  - `noCompteCompta`, `noChapitreCompta`, `noCompteComptaCle`, and the `sens` of the rubrique (G: gain/charge; R: retenue/liability).
- Banks [src: `entity/Banque.java`]
  - `noCompteCompta`, `noChapitreCompta`, `noCompteComptaCle` for posting salary virement lines by bank.
- Payroll lines [src: `entity/Paie.java`]
  - Fields used: `periode`, `motif`, `modePaiement` ("Virement" vs others), `banque`, `net`, `its`, `cnss`, `cnam`, `rcnss`, `rcnam`, `employe.id`, `employe.prenom/nom`.

## Workflows

### 1) Generate accounting piece (journal entries)
Triggered in UI "Comptabilité" → "Générer la pièce comptable" [src: `ui/compta.java` genPC()].

- Preconditions
  - Selected `période` must equal `Paramgen.periodeCourante`; otherwise generation is blocked [src].
  - Selected `motif` is required; entries are filtered by `(Paie.periode == période && Paie.motif == motif)`.

- Masterpiece header
  - One Masterpiece is inserted per generation with: NUMERO (DB seq or timestamp), DATEOP (now), INITIATEUR (current user), INIT_HR (now), LIBELLE_SERVICE = "Service Paie", RUBRIQUE = motif name, TOTAL_DEBIT/CREDIT initialized to 0 then updated [src].

- Detail lines (Detailpiece)
  1) Rubriques — Gains (sens == "G") except “Engagements cumulés” [src: `ui/compta.java` genPC()].
     - Amount = sum of `Rubriquepaie.montant` for the rubrique.
     - Post as Debit: COMPTE = `Rubrique.noCompteCompta`, SENS = "D", LIBELLE/INTITULET = `Rubrique.libelle`.
  2) Bank transfers per bank [src].
     - For each `Banque`, Amount = sum of `Paie.net` where `modePaiement == "Virement" && Paie.banque == Banque.nom`.
     - Post as Credit: COMPTE = `Banque.noCompteCompta`, SENS = "C", LIBELLE = "Salaire (motif) du mois de : {MM-YYYY} Faveur Agents virés vers la Banque :/{noCompteCompta}"; INTITULET = bank name.
  3) Cash (non-virement) per employee [src].
     - For each `Paie` where `modePaiement != "Virement"` (or `banque == "-"` in export):
       - COMPTE built as "307" + left-zero-padded `Employe.id` to 4 digits, Amount = `Paie.net`, SENS = "C", INTITULET = "{Prénom Nom}", LIBELLE = "Salaire (motif) du mois : {MM-YYYY} Faveur :/{INTITULET}".
  4) Engagements cumulés (specific Rubrique id used by legacy: `menu.pc.usedRubID(16)`) [src].
     - For each matching `Rubriquepaie`, Credit to employee account: COMPTE = "511" + zero-padded `Employe.id`, Amount = `rp.montant`, SENS = "C", INTITULET = "ENGTS {Prénom Nom}", LIBELLE explicative.
  5) Rubriques — Retenues (sens == "R") excluding the engagements rubrique [src].
     - Amount = sum on rubrique; Credit: COMPTE = `Rubrique.noCompteCompta`, SENS = "C", LIBELLE/INTITULET from rubrique.
  6) Statutory lines (global) [src].
     - ITS (Impôts cédulaires): Credit COMPTE = `Paramgen.noComptaIts`, amount = sum(Paie.its).
     - CNSS employés: Credit COMPTE = `Paramgen.noComptaCnss`, amount = sum(Paie.cnss).
     - CNAM employés: Credit COMPTE = `Paramgen.noComptaCnam`, amount = sum(Paie.cnam).
  - Technical constants for lines: JOURNAL = "PAI", DEVISE = "UM", COURS = 1, NUMERO_COURS = 1 [src lines].
  - After inserts, Masterpiece totals are recomputed as: TOTAL_CREDIT = sum(CVMRO_MONTANT where SENS == 'C'), TOTAL_DEBIT = sum(... 'D') [src].

- Post-conditions
  - Debits equal Credits within rounding tolerance.
  - A success message is shown on update; failures are surfaced by the DB update result [src].

### 2) Excel exports
1) Balance by accounts (Compta Paie) [src: `ui/compta.java` excelCompta()].
   - Filters same as above (period+motif). Rows:
     - For each Rubrique: account number (`noCompteCompta`), label, Debit if `sens == 'G'` else 0, Credit if `sens == 'R'` else 0.
     - ITS, CNSS employés (+ medecine + patronal as additional rows), CNAM employés (+ employer rows), VIREMENT by each bank (credit), CASH (credit for non-virements).
   - Totals: final row shows sums of Debit and Credit columns.
   - File name pattern: `repport/COMPTA_PAIE_{MM-YYYY}.xls`.

2) Pièce comptable detail [src: `ui/compta.java` excelPieceCompta()].
   - Exports all Detailpiece rows (sorted by NUMLIGNE) with columns: NUPIECE, DATEOP, COMPTE, INTITULET, LIBELLE, MONTANT, SENS.
   - File name pattern: `repport/PC_{MOTIF}_{YYYY-MM}.xls`.

### 3) UNL export (58-field pipe file)
Triggered by UI "Parametre du fichier UNL" → Generate [src: `ui/compta.java` exportFichierCompta()].

- Input parameters (from UI)
  - CODE_AGENCE (text), CODE_DEVISE (text), CODE_OPERATION (text), CODE_SERVICE (text), DATE_FORMAT (one of dd/MM/YYYY, dd/MM/YY, ddMMYY, ddMMYYYY).
  - Period window: `paieDu = période - 27 days`, `paieAu = paieDu + (monthDays - 1)`; dates formatted with selected DATE_FORMAT [src].
  - NUM_PIECE = `"EP" + format(paieAu, "MMYYYY")`.

- Record mapping (common fields by position; others are empty "|")
  - [0] CODE_AGENCE | [1] CODE_DEVISE | [2] CHAPITRE | [3] COMPTE | [5] CODE_OPERATION | [11] DATE_COMPTABLE | [12] CODE_SERVICE | [13] DATE_VALEUR | [14] MONTANT | [15] SENS (D/C) | [16] LIBELLE | [18] NUM_PIECE | remaining: empty.

- Lines generated
  1) For each Rubrique present in period: one line with CHAPITRE=`Rubrique.noChapitreCompta`, COMPTE=`Rubrique.noCompteCompta`, MONTANT=total rubrique, SENS=Debit if `sens=='G'` else Credit, LIBELLE = `Rubrique.libelle` [src].
  2) ITS global: CHAPITRE=`Paramgen.noComptaChapitreIts`, COMPTE=`Paramgen.noComptaIts`, SENS=C, LIBELLE="ITS", MONTANT=Σ(Paie.its) [src].
  3) CNSS employés + Médecine (D/C pair) + Patronal (D/C pair): use corresponding `Paramgen` chapitre/compte pairs; amounts per legacy formula (e.g., Médecine = 2× CNSS employés, Patronal = 13× CNSS employés) [src].
  4) CNAM employés + Employer (D/C pair): CHAPITRE/COMPTE from `Paramgen`, employer amount = Σ(Paie.cnam) × 5/4 [src].
  5) VIREMENT by bank: CHAPITRE/COMPTE from `Banque`, SENS=C, LIBELLE="VIREMENT {banque.nom}", MONTANT=Σ(Paie.net where Paie.banque==banque.nom) [src].
  6) CASH (non-virement): CHAPITRE/COMPTE from `Paramgen.noComptaChapitreNet`/`noComptaNet`, SENS=C, LIBELLE="CASH", MONTANT=Σ(Paie.net where banque=="-" or modePaiement!="Virement") [src].

- Output location
  - Directory: `repport/Fichier_compta_Paie_{YYYY-MM}` (created if missing).
  - File: `Fichier_compta_Paie_{YYYY-MM}.unl` inside that directory [src].

## Validation rules
- Balance: TOTAL_DEBIT must equal TOTAL_CREDIT on Masterpiece; Excel summary totals must match; UNL should have equal D vs C totals.
- Codes present: For each posting, the corresponding CHAPITRE/COMPTE must be configured (`Rubrique`, `Banque`, or `Paramgen`). Missing codes are implementation errors.
- Period guard: generation/export allowed only for `periode == Paramgen.periodeCourante` (for journal generation) [src].
- Filtering: All computations filter by selected période and motif where applicable [src].

## Defaults and constants (legacy)
- JOURNAL: "PAI"; DEVISE: "UM"; COURS: 1; NUMERO_COURS: 1 [src].
- Beneficiary placeholder: Masterpiece.BENEFICIAIRE = "-" [src].
- Employee account patterns used in journal (not in UNL): 307{Employe.id:04d} for cash salary; 511{Employe.id:04d} for engagements [src].

## Security, audit, and errors
- Masterpiece stores INITIATEUR and INIT_HR; carry these into the new system with the authenticated user and server time.
- Remove legacy vendor/license checks and fail-fast code (e.g., forced divide-by-zero under specific licence/date) in the new implementation [src: `ui/compta.java` excelCompta()].
- Wrap DB operations in a transaction; on failure, roll back and surface clear errors.

## Open points and assumptions
- Engagements rubrique id: legacy uses `menu.pc.usedRubID(16)` as the special “Engagements cumulés”. Assumption: rubric with internal id 16 maps to engagements; parameterize in new system.
- Mode de paiement vs banque "-": legacy treats non-virement as cash; unify the rule to `modePaiement != "Virement"` primarily; consider deprecating sentinel "-".
- Currency: legacy hard-codes "UM"; make it configurable via `Paramgen.devise`.

## Acceptance criteria
- Can generate a balanced Masterpiece+Detailpiece set for a given (période, motif).
- Excel balance and piece detail export match the computed lines and totals.
- UNL file is created with the field positions and values as per mapping above; importing system accepts it without format errors.
- Missing configurations (accounts/chapitres) are detected and reported before generation.

## Implementation notes (porting)
- Use DB-agnostic id generation for Masterpiece.NUMERO; avoid database-specific sequences (legacy has Oracle branch) [src].
- Ensure atomicity: insert Masterpiece, then bulk insert Detailpiece, then recompute totals and update Masterpiece within one transaction.
- For UNL, build records as arrays with placeholders to guarantee 58 fields; join with "|" and ensure trailing field terminator and newline [src writer.format].
- Derive dates exactly as legacy for continuity: paieDu = période - 27 days; paieAu = end-of-month based on paieDu’s month [src].