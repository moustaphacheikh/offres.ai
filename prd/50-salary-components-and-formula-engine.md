## 50 — Salary Components & Formula Engine

This module defines how salary components (rubriques) are configured and how their values are computed via the legacy formula engine.

### Scope and goals
- Configure rubriques as earnings (G) or deductions (R) with flags controlling inclusion in ITS/CNSS/CNAM and advantages in-kind.
- Support automatic calculation of base and/or number via formulas built from tokens and built-in functions.
- Allow cross-rubrique references and employee/motif/period-sensitive function outputs.

### Data model (legacy source of truth)
- Rubrique
  - Fields: id, libelle, abreviation, sens(G|R), its, cnss, cnam, avantagesNature, plafone, cumulable, deductionDu(Brut|Net), baseAuto, nombreAuto, sys, accounting fields (noCompteCompta, noChapitreCompta, noCompteComptaCle)
  - Relationships: has many Rubriqueformule (B and N parts), Rubriquepaie, Retenuesaecheances
  - Source: entity/Rubrique.java
- Rubriqueformule
  - Fields: rubrique(FK), partie(B|N), type(O|N|F|R), valText, valNum
  - Semantics:
    - O: operator literal (+,-,*,/, parentheses in valText)
    - N: numeric literal in valNum
    - F: function code in valText (e.g., "F01", "F09")
    - R: referenced rubrique id in valText (numeric string)
  - Source: entity/Rubriqueformule.java
- Rubriquepaie
  - Fields: employe, rubrique, motif, periode, base, nombre, montant, fixe, importe
  - Unique: one row per (employe, rubrique, motif, periode)
  - Source: entity/Rubriquepaie.java; util/PaieClass.insertRubrique
- Sysrubrique
  - Maps system rubric IDs to customer rubric IDs used programmatically (usedRubID)
  - Known IDs in use: 1=Salaire de base; 3/4/5/6=HS 115/140/150/200; 7=Prime Panier; 8=Prime Eloignement; 19/20=Augmentation compensation helpers
  - Source: entity/Sysrubrique.java; util/PaieClass.usedRubID
- Rubriquemodel
  - Template for default rubriques assignment to employees (used for mass setup/copy)
  - Source: entity/Rubriquemodel.java

### Formula storage and evaluation
- Two-part formulas per rubrique:
  - Partie B (Base): computes unit base amount
  - Partie N (Nombre): computes quantity/multiplier
- Expression building:
  - Engine concatenates tokens in order of `Rubriqueformule` by id within the part to form a math string
  - Token semantics: O appends operators; N appends numeric; F injects function result; R injects referenced rubrique montant
  - Example (Base): N=0.2 * R=1 → "0.2*<montant rub 1>"
- Evaluation:
  - Math string evaluated by util/Calcul.calculer(s)
  - Resulting base/number are doubles
  - Amount rounding: montant = round(base * nombre) to nearest unit (Math.round)
- Sources: util/PaieClass.formulRubrique, rubriqueFormuleStr, baseRbrique, nombreRbrique, insertRubrique; util/Calcul.java

### Built-in functions (F01–F24) and meanings
- F01_NJT(employe, motif, periode): NJT from `Njtsalarie`; 0 if missing
- F02_sbJour(employe, motif, periode): base salary per day (Rubrique sys id 1 base)
- F03_sbHoraire(employe, motif, periode): hourly base = (sbJour*30) / contratHeureMois
- F04_TauxAnciennete(employe, periode): seniority rate (2%/yr up to 14; caps 28–30% after)
- F05_cumulBIDerDepart(employe): cumulative BI since last depart
- F06_cumulBNIDerDepart(employe): cumulative BNI since last depart
- F07_cumulRETDerDepart(employe): cumulative brut-deducted retenues since last depart
- F08_cumulBrut12DerMois(employe): brut of last 12 months + initial
- F09_salaireBrutMensuelFixe(employe, periode): sum of fixed gain rubriques for SN in current period
- F10_smig(): SMIG from Paramgen
- F11_smigHoraire(employe): smig hourly = SMIG * contratHeureSemaine * 4
- F12_TauxLicenciement(employe, periode): dismissal rate by seniority bands
- F13_TauxLicenciementCollectif(employe, periode): collective dismissal rate by seniority bands
- F14_TauxRetraite(employe, periode): retirement rate based on F12
- F15_TauxPSRA(employe): PSRA rate from employee
- F16_TauxPreavis(employe): preavis months from employee
- F17_CumulNJTMC(employe): reserved; returns 0 in legacy
- F18_NbSmigRegion(employe): nb SMIG from `Origines` for region/dept policy
- F19_TauxPresence(employe, periode): months since anciennete / 12
- F20_BaseIndLogement(employe): base from `Grillelogement` by category, family, children
- F21_salaireNet(employe, periode): net computed from F09 with CNSS/CNAM/ITS on base
- F22_NbEnfants(employe): number of children
- F23_TauxAncienneteSpeciale(employe, periode): seniority special progression (>=16 years +1%/yr)
- F24_augmentationSalaireFixe(employe, periode): total increase on fixed gains across rubriques
- Sources: util/FonctionsPaie.java

### Dependencies and ordering
- Cross-references (R) read the referenced rubrique montant for the same (employee, motif, period). Ensure referenced rubriques are present and computed first in workflows.
- Detection of SB-dependent rubriques: `formulRubriqueOnSB` flags rubriques whose formula contains F02/F03 or R1; used to recompute when base salary changes.
- Automatic recomputation: `updateRubriquePaie` recalculates base/nombre for motif SN; manual order matters for expressions with R tokens.
- Sources: util/PaieClass.formulRubriqueOnSB, updateRubriquePaie; ui/salarys.java usage

### Rounding, constraints, and persistence rules
- Rounding: montant = round(base*nombre) to integer currency units (MRU).
- Insert/update logic:
  - If base provided equals 0, existing row is deleted.
  - If computed montant <= 0, row isn’t inserted/updated.
  - Unique per (employe, rubrique, motif, periode); updates preserve id.
- Fixed vs variable: `Rubriquepaie.fixe = true` marks fixed gains and is used by F09.
- Import flag: `Rubriquepaie.importe` marks imported/manual entries.
- Sources: util/PaieClass.insertRubrique

### System mappings and special behavior
- usedRubID(1): Base salary — cornerstone for many formulas
- usedRubID(3..6): Overtime counters 115/140/150/200 — quantities typically set, base from F03
- usedRubID(7/8): Panier/Eloignement primes — quantities from attendance (outside engine)
- usedRubID(19/20): Augmentation compensation helpers — used by compensation logic when Paramgen.appIndCompensatrice is enabled
- Sources: util/PaieClass.usedRubID, augmentationSalaire/augmentationRubPaie

### Validation and governance
- Avoid cycles: engine doesn’t detect circular references; R tokens must not form loops.
- Safe defaults: missing referenced rubrique or function data yields 0.
- Testing: UI exposes `rubriqueFormuleStr` for inspection; evaluate via baseRbrique/nombreRbrique; no separate sandbox.
- Versioning/audit: not implemented in legacy; propose to add in target system if needed.

### Acceptance criteria
- A rubrique with baseAuto and/or nombreAuto computes values via its formula and persists a rounded montant.
- F01–F24 functions return values consistent with employee, motif, and period context.
- Cross-referenced rubriques resolve to their current-period montants; order-dependent rubriques are stable within the payroll update workflow.
- Accounting fields available per rubrique for downstream exports.

### Source references
- entity/Rubrique.java — flags and accounting fields
- entity/Rubriqueformule.java — tokenized formula storage
- entity/Rubriquepaie.java — persisted amounts and flags
- entity/Rubriquemodel.java — templates for default assignment
- entity/Sysrubrique.java — mapping for system rubriques
- util/PaieClass.java — formula build/eval, rounding, SB dependency, insert/update rules
- util/FonctionsPaie.java — F01..F24 function implementations
- util/Calcul.java — math expression evaluator
- ui/rubriques.java, ui/salarys.java — UI interactions and previews
