# 60 — Banking & payments

This module covers employee payment modes, banking master data, and the generation of bank transfer outputs used to pay net salaries by bank. It’s grounded in the legacy Java implementation and reports.

## Scope
- Payment modes supported: bank transfer, cash, cheque.
- Banking master data for banks and company bank details.
- Net salary “virements” export by period, bank, and payroll motif, with totals and signature block.
- Validation and exclusion rules for missing/invalid banking data.

Out of scope in this module: tax declarations (e.g., DTA) and social contributions remittance; these are documented in dedicated modules.

## Data model

Employee payment fields (legacy source: `entity/Employe.java`):
- banque: FK to bank (ManyToOne) used when mode = Virement.
- noCompteBanque: string(50), employee’s bank account number used on virement outputs.
- modePaiement: string(50), one of {"Virement", "Espèce", "Chèque"} (UI sources set/select these values).
- domicilie: boolean, indicates domiciliation status with the bank.

Payroll snapshot fields (legacy source: `entity/Paie.java`):
- banque: string(50), denormalized at payroll run time from employee.bank.nom.
- noCompteBanque: string(50), denormalized at payroll run time from employee.noCompteBanque.
- modePaiement: string(50), copied from employee.modePaiement.
- net: double, the net amount to pay; used to compute transfer totals.

Bank master (legacy source: `entity/Banque.java`):
- id: Integer (PK)
- nom: string(50)
- noCompteCompta: Long
- noChapitreCompta: long
- noCompteComptaCle: string

Company banking parameters (legacy source: `entity/Paramgen.java`):
- bankvirement: string, free-text label for the company’s bank used in virement letter header.
- comptevirement: string, the company account number to be debited (appears on the letter and report).

Notes
- UI enforces enabling/disabling bank, account, and domiciliation fields depending on the selected payment mode (only enabled for "Virement").
- No IBAN/RIB normalization is implemented in the legacy; account is treated as a display string.

## Payment modes

Accepted values and behavior (sources: `ui/EmployequeryFrame.java`, `ui/salarys.java`):
- Virement (bank transfer)
	- Requires: employee.banque and employee.noCompteBanque set.
	- Eligible for virements export and totals by bank.
	- UI: bank/account/domiciliation inputs enabled.
- Espèce (cash)
	- Not included in bank transfer export; handled as cash in accounting/statements.
- Chèque (cheque)
	- Not included in bank transfer export; handled separately if needed.

Validation
- If mode = Virement and either bank or account is missing at payroll time, the record must be excluded from bank exports and surfaced in a validation report/notification.
- Virements exports must filter on mode = "Virement" explicitly.

## Virements export (bank transfer statement)

Functional description
- Users select a payroll period, a bank, and a payroll motif; the system exports an Excel statement listing employees to be paid by transfer, with their ID, full name, NNI, account, and the net amount. A grand total and signatory block are included.

Legacy implementation references
- Screen logic and Excel export: `ui/virements.java`
	- Filters rows where: periode matches, paie.banque equals selected bank.nom, motif matches, and modePaiement equalsIgnoreCase("Virement").
	- Columns: ID employé, NOM ET PRENOM, NNI, N° COMPTE, MONTANT (sum of Paie.net grouped per employee when multiple rows occur in period/motif).
	- Total: column sum for MONTANT; add amount-in-words and signatories from parameters.
- Jasper virement report (PDF layout alternative): `report/virementbank.jrxml`
	- Parameters: PERIODE (Date), BANQUE (String)
	- SQL: SUM(paie.NET) grouped by employee and account; selects employee id, prenom+nom, NNI, paie.noCompteBanque, and Paramgen.bankvirement/comptevirement for letter text/headers.
	- Shows company name and period; includes “Objet: Virement de salaires” and indicates the company account to debit.

Required behavior in the new system
- Filtering:
	- Period = selected payroll period (month).
	- Bank = chosen bank (by id or name).
	- Motif = chosen payroll run/motif.
	- Mode = "Virement" only.
- Grouping and totals:
	- Aggregate multiple Paie rows per employee within the filtered set to one payment line by employee account.
	- Compute and show total amount for the export.
- Columns:
	- Employee ID, Full name, NNI, Bank account, Net amount.
- Headers/letter:
	- Company name from settings; optional letter elements and signatories pulled from parameters, including `Paramgen.bankvirement` and `Paramgen.comptevirement`.
- Output formats:
	- Excel (first-class) and PDF (report template) must both be supported.

Validation and exclusions
- Exclude any employee with mode = Virement but missing bank or account number; list them in a separate validation output with reasons.
- If multiple accounts are found for the same employee within the period (should not happen with current model), fail fast and report conflict.

## Accounting split (informational)
The system differentiates bank vs. non-bank payments in accounting views (source: `ui/compta.java`):
- Bank: net where mode = Virement and paie.banque = selected bank.
- Non-bank (cash/cheque): net where mode != Virement.

## Minimal contract for the virement export service
- Inputs: period (month), bank (id), motif (id), locale (for amount-in-words), company params.
- Output: workbook (Excel) or PDF stream; total amount; optional rejected-list with reasons.
- Errors: missing required selections; no eligible rows; invalid employee account data; permission denied.

## Edge cases
- Employee marked Virement but bank or account missing → exclude and report.
- Employee has multiple Paie rows in same period/motif → aggregate to one line.
- Zero-net employees → exclude from export unless explicitly required by business (legacy excludes by nature of net sum = 0; keep exclusion).
- Bank name changes between payroll calculation and export → use Paie.banque snapshot for consistency.

## Implementation notes (mapping to legacy)
- Employee fields: `Employe.banque`, `Employe.noCompteBanque`, `Employe.modePaiement`, `Employe.domicilie`.
- Payroll snapshot: `Paie.banque`, `Paie.noCompteBanque`, `Paie.modePaiement`, `Paie.net`.
- Params: `Paramgen.bankvirement`, `Paramgen.comptevirement`, `Paramgen.signataires`.
- UI toggles (enabling fields only for Virement): `salarys.tModePaiementActionPerformed`.
- Export logic & columns: `ui/virements.java`.
- PDF layout/SQL: `report/virementbank.jrxml`.

## Acceptance criteria
- A user can export virement statements for a selected period, bank, and motif; only mode = Virement rows are included; totals match the sum of included nets.
- Employees without bank or account while set to Virement are excluded and listed in a validation output.
- The export renders: employee id, full name, NNI, account, net; shows company header, period text, and signatories; includes company debit account from settings.
- Excel and PDF exports both succeed with identical row sets and totals.
- Accounting view can compute bank vs. non-bank totals consistent with the export.

## Open questions / assumptions
- Account format: treat as opaque string; no IBAN checksum or RIB parsing enforced (legacy does not implement this).
- Cheque handling: out of scope for this module; may require its own export/print in the future.
- Multibank per employee: not supported in the model; assume one account per employee.