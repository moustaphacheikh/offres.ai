# 96 — Business Rules & Calculations

This module specifies how payroll is computed from legacy-grounded data structures. It formalizes inputs/outputs, validations, and algorithms while deferring rates/parameters to configuration (rubriques and formulas) rather than hardcoding.

## Engine contract
- Inputs
	- Global parameters: `Paramgen` (e.g., `smig`, `abatement`, `ancienneteAuto`, `ancienneteSpeciale`, `primePanierAuto`, `indlogementAuto`, `deductionCnssdeIts`, `deductionCnamdeIts`, `remboursementIts`, `modeITS`, `njtDefault`). Source: `entity/Paramgen.java`.
	- Employee master: `Employe` (e.g., `dateEmbauche`, `dateAnciennete`, `tauxAnciennete`, `expatrie`, `domicilie`, `detacheCnss`, `detacheCnam`, `exonoreIts`, `nbEnfants`, `situationFamiliale`, `grillesalairebase`, `contratHeureSemaine`). Source: `entity/Employe.java`.
	- Rubric catalog: `Rubrique` with flags (`its`, `cnss`, `cnam`, `avantagesNature`, `baseAuto`, `nombreAuto`, `plafone`, `cumulable`, `deductionDu`, `sys`) and per-rubric formulas in `Rubriqueformule` (`partie`, `type`, `valText`, `valNum`). Sources: `entity/Rubrique.java`, `entity/Rubriqueformule.java`.
	- Period inputs: Imported/fixed values per employee and rubric in `Rubriquepaie` (`fixe`, `importe`, `base`, `nombre`, `montant`, `motif`, `periode`) and attendance/NJT (`Njtsalarie.njt`, `Semainetravail` schedule). Sources: `entity/Rubriquepaie.java`, `entity/Njtsalarie.java`, `entity/Semainetravail.java`.
	- Engagements/loans and slices: `Retenuesaecheances` and `Tranchesretenuesaecheances`. Source: `entity/Retenuesaecheances.java`, `entity/Tranchesretenuesaecheances.java`.
- Output
	- Calculated `Rubriquepaie` rows per employee/period, each with consistent base/number/amount and correct flags for taxation and contributions. Totals feed payslip and declarations.

Notes
- Rates, caps, and tables are encoded in rubriques and formulas. Do not hardcode them in the engine. Where law changes, update configuration not code.
- Calculation order respects system rubriques first (`Rubrique.sys == true`) and inter-rubric dependencies expressed in formulas. Imported/fixed values take precedence over auto-calculated fields unless overridden by an explicit rule.

## Validation rules (grounded)
- Global/period
	- Payroll period inside `[Paramgen.periodeCourante, Paramgen.periodeSuivante)` and not after `Paramgen.periodeCloture`.
	- `Paramgen.smig` > 0; `Paramgen.abatement` defined; `Paramgen.njtDefault` > 0.
- Employee
	- Unique `Employe.id` and mandatory identity fields. `dateEmbauche` ≤ payroll period end; if `dateAnciennete` present, use it for seniority base.
	- If `exonoreIts == true`, skip ITS for that employee. If `detacheCnss`/`detacheCnam == true`, skip respective contributions.
	- If `expatrie == true`, apply expatriate tax mode if required by configuration (`Paramgen.modeITS`). If `domicilie == false`, apply non-resident treatment per config.
- Rubriques and inputs
	- For each `Rubriquepaie` unique constraint `(periode, employe, rubrique, motif)` must hold.
	- If `Rubrique.baseAuto == true` or `Rubrique.nombreAuto == true`, missing base/nombre is permitted; otherwise they are required if the formula references them.
	- Enforce `Rubrique.plafone` and `Rubrique.cumulable` semantics via formula/cap evaluation.

## Calculations
All percentages and thresholds are configurable via `Rubrique`/`Rubriqueformule` unless stated. Below are the algorithmic rules and data sources.

### Base salary and grids
- The employee’s base salary may be provided by a fixed `Rubriquepaie` row (e.g., a base-salary rubric) or derived from `Grillesalairebase`/classification attached to `Employe.grillesalairebase`. Ensure resulting base respects `Paramgen.smig`.
- If attendance affects pay, use `Njtsalarie.njt` (or default `Paramgen.njtDefault`) to prorate monthly base: baseProrated = baseMonthly × (NJT_effective / NJT_reference).

### Seniority (Ancienneté)
- Activation
	- If `Paramgen.ancienneteAuto == true`, compute seniority automatically; otherwise, allow `Employe.tauxAnciennete` to override or be set manually via a rubric.
	- If `Paramgen.ancienneteSpeciale == true`, use the configured special schema (see below) instead of the standard.
- Standard schema
	- For each completed year of service since `dateAnciennete` (fallback `dateEmbauche`), accrue a percentage up to a cap. Typical legacy PRD values: 2%/year for first 14 years, then step increases up to 30% at 16+ years.
	- Implement as formula-based: seniorityRate = f(yearsOfService, configuredSteps). Amount = seniorityRate × baseForSeniority.
- Special schemas (supported by configuration)
	- Special 1: 30% base after 16 years + 1%/additional year.
	- Special 2: 3%/year up to 15 years, then 45% + 2%/additional year.
	- Support selecting schema via rubric/formula or `Paramgen` flag; never hardcode in engine.

### Allowances and benefits
- Housing allowance
	- If `Paramgen.indlogementAuto == true`, compute from `Grillelogement` using employee category (`Employe.grillesalairebase`), `situationFamiliale`, and `nbEnfants`. Otherwise, allow manual `Rubriquepaie` import.
- Family/children, transport, meal, site allowances
	- Compute based on rubriques’ formulas referencing `Employe.nbEnfants`, work site, or working days. `Paramgen.primePanierAuto` enables auto meal allowance per working day schedule (`Semainetravail` and attendance).
- Benefits in kind (`Rubrique.avantagesNature == true`)
	- Include in taxable or contribution bases according to rubric flags and formulas.

### Overtime and attendance
- Attendance/NJT
	- Effective NJT comes from `Njtsalarie.njt` for the period and motif; fallback to `Paramgen.njtDefault` when missing.
- Overtime
	- Compute overtime quantities from timekeeping (WeekOT/attendance, outside this module) and apply rubric progression (e.g., 115% → 140% → 150% → 200%) via configured rubriques.

### Social security contributions
- CNSS (employee and employer)
	- Apply only if `Rubrique.cnss == true` for the rubric and employee is not detached (`Employe.detacheCnss == false`). Rates and ceilings are modeled as rubric formulas/caps. If government reimbursement is enabled (`Paramgen.remboursementIts` or dedicated CNSS reimbursement rubriques), record employer portions accordingly.
- CNAM (employee and employer)
	- Apply only if `Rubrique.cnam == true` and `Employe.detacheCnam == false`. Rates are configured in rubriques; no engine hardcoding.

### Income tax (ITS)
- Applicability
	- Evaluate only if `Paramgen.usedITS != 0` and `Employe.exonoreIts == false`.
	- Resident/non-resident and national/expatriate treatment driven by `Employe.expatrie`, `Employe.domicilie`, and `Paramgen.modeITS`.
- Taxable base
	- Start from gross taxable rubriques (`Rubrique.its == true`).
	- If `Paramgen.deductionCnssdeIts == true`, subtract employee CNSS from the taxable base; if `Paramgen.deductionCnamdeIts == true`, subtract employee CNAM.
	- Apply `Paramgen.abatement` as the general abatement deduction per rules.
- Calculation
	- Use progressive tranches encoded in rubriques/formulas; support monthly or annual mode per `Paramgen.modeITS`. Apply reimbursements if configured (`Paramgen.remboursementIts`).

### Engagements/loans (Retenues à échéances)
- Generate deductions from `Retenuesaecheances` schedules. For each active engagement in the period, create the period slice from `Tranchesretenuesaecheances` or compute via remaining capital × rate or fixed installment per the rubric’s formula.
- If `Paramgen.retEngOnConge == false` and employee `enConge == true`, defer the period’s deduction unless configured otherwise.

### Net pay and rounding
- Net pay = Sum(earnings) − Sum(deductions) where rubric sense/flags define sign and inclusion.
- Apply rounding rules defined in rubriques (e.g., cent rounding) or global setting; default to currency minor unit rounding if unspecified.

## Conditional logic (summary)
- Contributions and tax switches: `Employe.detacheCnss`, `Employe.detacheCnam`, `Employe.exonoreIts`, `Employe.expatrie`, `Employe.domicilie`, and `Paramgen.usedITS`/`modeITS` drive applicability.
- Auto vs manual amounts: `Rubrique.baseAuto`, `Rubrique.nombreAuto`, and `Rubriquepaie.fixe`/`importe` decide precedence of imported values vs computed ones.
- Leave impact: use NJT/attendance to prorate base; engagements may be deferred with `Paramgen.retEngOnConge`.

## State transitions
- Employee: Active → On Leave → Terminated (fields: `Employe.enConge`, `Employe.enDebauche`, `Employe.actif`). Downstream rubriques and engagements react accordingly.
- Payroll: Draft → Calculated → Approved → Paid → Closed (closing reconciles with `Paramgen.periodeCloture`).
- Reporting: Generated → Reviewed → Submitted → Archived (CNSS/CNAM/ITS declarations use rubric totals and flags).

## Grounding references
- Global params: `entity/Paramgen.java`
- Employee master: `entity/Employe.java`
- Rubrics and formulas: `entity/Rubrique.java`, `entity/Rubriqueformule.java`
- Period lines: `entity/Rubriquepaie.java`
- Attendance/NJT: `entity/Njtsalarie.java`, `entity/Semainetravail.java`
- Engagements: `entity/Retenuesaecheances.java`, `entity/Tranchesretenuesaecheances.java`

## Change log (this revision)
- Rewrote module with explicit inputs/outputs and precedence rules.
- Replaced hardcoded rates with configuration-driven statement tied to `Rubrique`/`Rubriqueformule`.
- Anchored applicability switches to actual fields (`detacheCnss`, `detacheCnam`, `exonoreIts`, `usedITS`, `modeITS`).
- Detailed seniority computation with `ancienneteAuto`/`ancienneteSpeciale` and override via `tauxAnciennete`.
- Added validations for uniqueness and required fields in `Rubriquepaie` and global parameter sanity checks.
- Documented NJT defaulting and allowance automation flags (`primePanierAuto`, `indlogementAuto`).
