# 55 — Installments and Loans (Retenues à échéances)

This module defines how recurring deductions (employee advances/loans and similar engagements) are configured, calculated, limited, and posted to payroll, with history tracking by period.

## Scope
- In scope: Fixed-amount installment deductions per period tied to a deduction rubric; multiple concurrent engagements per employee; proportional reduction to respect a net quota; optional deduction on leave; per-period history lines; automatic closure when capital fully repaid.
- Out of scope: Interest/fees, variable amortization schedules, external accounting entries. The legacy implementation does not compute interest.

## Data model (legacy ground truth)
- Retenuesaecheances
  - Fields: id, employe (FK), rubrique (FK, deduction rubric), periode (Date, start period), dateAccord (Date), capital (double), echeance (double, nominal installment), active (bool), solde (bool), note (string), echeancecourante (double, current-period deduction for normal salary), echeancecourantecng (double, current-period deduction for leave), tranchesretenuesaecheanceses (1..* tranche history).
  - Source: `entity/Retenuesaecheances.java`.
- Tranchesretenuesaecheances
  - Fields: id, retenuesaecheances (FK), periode (Date), montantRegle (double), motif (int).
  - Source: `entity/Tranchesretenuesaecheances.java`.
- Employee and rubric relations expose navigation collections: `Employe.getRetenuesaecheanceses()`, `Rubrique.getRetenuesaecheanceses()`.

## Configuration parameters
- quotaEcheanceRae (Double, percent): Maximum share of net-before-engagements available for installments in a period. If 0, no quota enforcement. Source: `entity/Paramgen.java` (fields and UI parameter binding).
- retEngOnConge (boolean): When true, engagements are also deducted during leave (motif id 2). When false, leave does not deduct engagements. Source: `entity/Paramgen.java` and UI `parametres`.

## Rubric prerequisites
- Installment rubrics are standard deduction rubrics with:
  - sens = 'R' (retention/deduction), and
  - fixe = true (fixed rubric created in payroll run), and
  - typically deducted from Net (DeductionDu == "Net") as totals are posted at net stage.
- Payroll aggregates all active engagements per employee by rubric before inserting the rubric line.

## Payroll processing rules (legacy behavior)
Processing occurs within the payroll calculation for motifs 1 and 2 (normal salary and leave). Source: `util/PaieClass.java`.

1) Initialize current-period amounts
- For each active, not-soldé Retenuesaecheances rs:
  - If motif == 1 (SN): set rs.echeancecourante = rs.echeance and add to totalEngagement.
  - If motif == 2 (CNG) and retEngOnConge: set rs.echeancecourantecng = rs.echeance and add to totalEngagement.
  - Persist the occurrence (updateOcurance).

2) Quota limit and proportional reduction
- Compute netAvantEng = SalaireBrut − CNSS − ITS + RITS + RCNSS. If quotaEcheanceRae > 0 and totalEngagement > quotiteCessible (netAvantEng × quota%):
  - cumDifPourCent = (totalEngagement − quotiteCessible) / totalEngagement.
  - For each active rs, reduce only the current-period amount proportionally:
    - motif 1: echeancecourante = max(0, echeance − echeance × cumDifPourCent).
    - motif 2: echeancecourantecng = max(0, echeance − echeance × cumDifPourCent).
  - Persist and reinsert the aggregated rubric totals.
  - Sources: `PaieClass.totalEchRetenueAE_SN`, `totalEchRetenueAE_CNG`, proportional reduction block.

3) Posting to payroll
- For each deduction rubric present in the employee’s fixed rubrics (sens 'R', fixe):
  - Insert a rubric line with montant = totalEchRetenueAE_SN(employe, rubrique) for motif 1.
  - If retEngOnConge and motif 2, insert montant = totalEchRetenueAE_CNG(employe, rubrique).
- These totals sum all current-period amounts by rubric across the employee’s active engagements.

4) History (tranche) generation and carry-forward
- After payroll, history rows are refreshed for the current period and the current-period amounts are reset:
  - Delete any existing `Tranchesretenuesaecheances` for the current period (defensive cleanup).
  - Insert one tranche with montantRegle = echeancecourante (motif 1). If echeancecourantecng > 0, insert another tranche for motif 2.
  - Reset rs.echeancecourante to rs.echeance and rs.echeancecourantecng to 0. Source: `updateTrancheRetAE`.

5) Balance update and auto-closing
- Compute totalReglementRetAE(rs) as the sum of tranche history. If capital − totalReglement <= 0 then solde = true and active may be left true/false per UI; if remaining capital is less than the nominal echeance, set next-period echeancecourante to the remaining balance for final settlement. Source: `updateRetenuesAE`.

## Creation and maintenance
- Insert/update: `insertRetAE(employe, rubrique, dateAccord, capital, echeance, active, note)` creates a new Retenuesaecheances or replaces an empty one. Defaults: solde=false, echeancecourante=echeance, echeancecourantecng=0. Links it to the employee. Source: `PaieClass.insertRetAE`.
- Active check: `retAEActive(employe, rubrique)` controls whether a rubric is considered when building the deduction rubric set.
- Lookup helpers: `empRetAE`, `retAEByID`, `trancheRAEById`.

## Validations and constraints
- An engagement must:
  - reference a deduction rubric (sens 'R');
  - have positive capital and echeance; and
  - be marked active to be processed.
- Quota enforcement applies only if Paramgen.quotaEcheanceRae > 0.
- Leave deduction occurs only if Paramgen.retEngOnConge is true for motif 2.
- Current-period amounts are clamped to zero if the proportional reduction would make them negative.

## Reporting and operations
- History and monitoring are based on `Tranchesretenuesaecheances` entries. The UI provides listings and exports; period cleanup may delete tranche rows before regeneration during close/reopen cycles. Sources: `ui/etats.java`, `ui/cloture.java`.

## Edge cases
- SalaireBrut <= 0: no engagement deduction occurs that period.
- Multiple concurrent engagements across different rubrics: each is reduced proportionally; payroll posts one line per rubric summing all engagements tied to it.
- Employee on leave (motif 2): processed only if retEngOnConge is true; amounts use echeancecourantecng.

## Migration notes (target system)
- Model tables: replicate `Retenuesaecheances` and `Tranchesretenuesaecheances` schemas.
- Batch flow:
  1) Before net computation, initialize current-period amounts for engagements based on motif and retEngOnConge.
  2) Apply quota reduction if configured.
  3) Insert deduction rubric rows per rubric as sums of current-period amounts.
  4) On finalize/close, write tranche history for the period and reset current-period amounts; update balances and close engagements when capital is fully paid.
- Admin settings: expose quotaEcheanceRae (%) and retEngOnConge in system parameters.

## References (legacy)
- Entities: `entity/Retenuesaecheances.java`, `entity/Tranchesretenuesaecheances.java`, `entity/Paramgen.java`.
- Payroll logic: `util/PaieClass.java` methods and blocks: retAEActive, empRetAE, totalEchRetenueAE_SN/CNG, insertRetAE, updateTrancheRetAE, updateRetenuesAE, proportional reduction in main payroll loop.
- UI/Reports: `ui/parametres*.java`, `ui/etats.java`, `ui/cloture*.java`.
