## 35 — Leave Management

This module defines how employee leave is recorded and how it interacts with attendance and payroll in the legacy system.

### Scope and goals
- Capture leave periods per employee and reflect “on leave” status in operations.
- Ensure attendance imports skip employees currently on leave.
- Support payroll runs for leave salary (CNG motif) with appropriate declaration flags and toggles.

### Data model (legacy source of truth)
- Conges (PAYROLL.conges)
  - id: Long, PK
  - employe: FK to Employe (not null)
  - periode: Date, payroll month the leave pertains to (not null)
  - depart: Date, scheduled leave start (not null)
  - reprise: Date, scheduled return date (not null)
  - repriseeff: Date, actual return date (not null)
  - note: String(500), optional
  - Source: entity/Conges.java
- Employe
  - enConge: boolean — operational flag set while the employee is on leave
  - Other related fields: dernierDepartInitial (historical), cumulNJTInitial (cumulative days), etc.
  - Source: entity/Employe.java
- Njtsalarie (PAYROLL.njtsalarie)
  - Unique(periode, employe, motif)
  - Used to track NJT by motif per period. Leave salary uses a dedicated motif (e.g., CNG)
  - Source: entity/Njtsalarie.java; entity/Motif.java
- Paramètres globaux
  - Paramgen.retEngOnConge: boolean — keep loan/retention installments during leave
  - Paramgen.addCurrentSalInCumulCng: boolean — include current salary in CNG cumulative
  - Origines.nbSmighorPourIndConges: integer — origin-based factor for leave indemnity
  - Sources: entity/Paramgen.java; entity/Origines.java

### Key behaviors and business rules
1) On-leave status and attendance
- Employees flagged enConge are excluded from attendance import and daily processing.
- Evidence: util/ReadExcel.java — if (employe.isActif() && !employe.isEnConge()) { ... }
- Rule: enConge must be true from depart through the day before repriseeff; it resets to false on/after repriseeff confirmation.

2) Payroll and motif (CNG)
- Leave payroll is processed under a specific motif (CNG) with declaration flags configured on Motif.
- Declaration switches (ITS/CNSS/CNAM) are driven by Motif booleans.
- Global toggles:
  - retEngOnConge: when true, Retenues à échéances continue during leave.
  - addCurrentSalInCumulCng: when true, the current period’s salary is included in CNG cumulative amounts.

3) Dates and validations
- Required fields: employe, periode, depart, reprise, repriseeff (Conges annotations mark dates non-null).
- Valid ranges: depart <= reprise; repriseeff >= depart.
- No overlapping leave for the same employee across intersecting dates.
- Leave period (periode) must align with the payroll month covering any day in [depart, reprise].
- Changes are blocked for dates in or before the closed payroll period (Paramgen.periodeCloture).

4) Regional/Origin parameterization
- nbSmighorPourIndConges from Origines participates in computing leave indemnity in payroll formulas.
- Reference: entity/Origines.java; formulas referenced in salary components module.

### Workflow
1. Create and approve leave
   - Create Conges with depart, reprise (planned), periode; set note if needed.
   - When leave starts, set employe.enConge = true.
2. Return from leave
   - Capture repriseeff (actual return); set employe.enConge = false effective that date.
3. Payroll impact
   - Run payroll with CNG motif for the leave period; declaration flags come from Motif; toggles from Paramgen apply.
   - Retention behavior during leave depends on Paramgen.retEngOnConge.

### Integrations
- Attendance import: must skip enConge employees (legacy behavior retained).
- Payroll engine: must support motif CNG and respect Paramgen toggles and Motif declaration flags.
- Reporting: bulletins and declarations should reflect motif-specific bases when CNG is used.

### Assumptions (where legacy code is silent)
- Accrual and balances: Legacy model doesn’t store leave balances; accrual calculation and balance enforcement are part of payroll/formula rules and HR policy. We will expose balance in the new system but compute it from NJT, tenure, and policy tables.
- repriseeff operationally may be unknown at creation time; although Conges marks it non-null, the new system should allow null until actual return, then enforce non-null on closeout.

### Acceptance criteria
- Recording a Conges sets and unsets enConge at the correct times and prevents attendance import during the leave.
- Payroll run supports CNG motif with correct declaration flags; Paramgen.retEngOnConge and addCurrentSalInCumulCng are honored.
- Validations prevent overlapping leave; required fields present; edits rejected for closed periods.

### Source references
- entity/Conges.java — fields: id, employe, periode, depart, reprise, repriseeff, note
- entity/Employe.java — enConge flag and relationships (Conges, Njtsalarie)
- entity/Njtsalarie.java — Unique(periode, employe, motif)
- entity/Motif.java — declaration flags for ITS/CNSS/CNAM
- entity/Paramgen.java — retEngOnConge, addCurrentSalInCumulCng
- entity/Origines.java — nbSmighorPourIndConges
- util/ReadExcel.java — attendance import skip rule using enConge