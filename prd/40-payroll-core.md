## 40 — Payroll Core

This module defines the core payroll computation flow, data structures, and motif-specific rules used by the legacy implementation.

### Scope and goals
- Run monthly payroll per employee and per motif (e.g., SN=standard, CNG=leave) for the current period.
- Compute gross, taxable bases, contributions (CNSS, CNAM), ITS and reimbursement tranches, net, and aggregates.
- Persist one `Paie` record per (employee, motif, period) and one `Rubriquepaie` per (employee, rubrique, motif, period).

### Data model (legacy source of truth)
- Paie (PAYROLL.paie)
  - Keys/links: employe(FK), motif(FK), paramgen(FK), periode(Date)
  - Amounts: bt (Brut total), bni (Brut non imposable), cnss, cnam, its, itsTranche1/2/3, rits (remb. ITS), retenuesBrut, retenuesNet, net
  - Bases: biCnss, biCnam, biAvnat; cumulBi, cumulBni, cumulNjt
  - Other: njt, nbrHs, contratHeureMois, netEnLettre, periodeLettre, banque, noCompteBanque, modePaiement, domicilie
  - Org snapshots: poste, departement, direction, directiongeneral, categorie, statut, activite; dateDernierDepart, paieDu, paieAu
  - Source: entity/Paie.java; util/PaieClass.java — paieCalcule/insertPaie
- Rubriquepaie (PAYROLL.rubriquepaie)
  - Unique(periode, employe, rubrique, motif)
  - Fields: fixe (bool), base (Double), nombre (Double), montant (Double), importe (Bool)
  - Source: entity/Rubriquepaie.java
- Rubrique
  - Flags: sens=G|R, its, cnss, cnam, avantagesNature, plafone, cumulable, baseAuto, nombreAuto, deductionDu=Brut|Net; compta fields
  - Formulas: has many Rubriqueformule
  - Source: entity/Rubrique.java, entity/Rubriqueformule.java
- Sysrubrique
  - Maps system rubric IDs to customer `Rubrique` IDs (used via usedRubID(n) in code)
  - Typical mappings used: 1=base salary; 3/4/5/6=overtime; 19/20=augmentation compensation helpers
  - Source: entity/Sysrubrique.java; util/PaieClass.usedRubID
- Njtsalarie
  - Unique(periode, employe, motif); stores NJT per motif and period
  - Source: entity/Njtsalarie.java
- Motif
  - Per-motif tax/decl flags: employeSoumisIts/Cnss/Cnam; declarationSoumisIts/Cnss/Cnam; actif
  - Source: entity/Motif.java

### Core calculation flow (legacy behavior)
For each employee and motif in the period (Paramgen.periodeCourante):
1) Collect rubriques for (employee, motif, period) and compute amounts
   - If rubrique.baseAuto: base = baseRbrique(B-part) via formula engine
   - If rubrique.nombreAuto: nombre = nombreRbrique(N-part)
   - montant = round(base * nombre)
   - Source: util/PaieClass.updateRubriquePaie, baseRbrique/nombreRbrique
2) Derive totals and bases
   - SalaireBrut = sum(montant where sens=G)
   - RetenuesBrut = sum(montant where sens=R and deductionDu=Brut)
   - RetenuesNet = sum(montant where sens=R and deductionDu=Net)
   - Bases CNSS/CNAM/ITS/Avantages: per flags on `Rubrique`
   - Apply plafonIndNonImposable: excess of non-ITS gains added back to taxable
   - Source: util/PaieClass.paieCalcule
3) Contributions and tax
   - CNSSm/RCNSSm on remunerationSoumiCnss (employee and reimbursement portions), with ceiling; skip if Employe.detacheCnss or motif not soumis
   - CNAMm/RCNAMm on remunerationSoumiCnam; skip if Employe.detacheCnam or motif not soumis
   - ITS = sum of tranche1/2/3 with abatement and optional deduction of CNSS/CNAM (Paramgen.deductionCnssdeIts, deductionCnamdeIts); tranches affected by expatrie and Paramgen.modeITS
   - RITS via tranche reimbursements using employee rates
   - Net = Brut - CNSS - CNAM - ITS - RetenuesBrut - RetenuesNet - AvantagesEnNature + RCNSS + RCNAM + RITS
   - Sources: util/PaieClass.ITSm/trancheX, CNSSm/RCNSSm, CNAMm/RCNAMm; entity/Employe flags; entity/Paramgen
4) Engagements (Retenues à échéances)
   - If motif=SN(1): set current installment (echeancecourante); insert fixed retenue rubriques
   - If motif=CNG(2): if Paramgen.retEngOnConge, set echeancecourantecng and insert rubriques
   - Enforce Paramgen.quotaEcheanceRae as a % of net-before-engagement; proportionally reduce installments if needed
   - On period close, write tranches records and reset current installments; auto-suppress when capital settled
   - Source: util/PaieClass.paieCalcule, updateTrancheRetAE, updateRetenuesAE
5) NJT and hours
   - NJT pulled from Njtsalarie(F01) per motif; overtime totals from rubriques or day entries
   - contratHeureMois = Employe.contratHeureSemaine * 52 / 12
   - Source: util/FonctionsPaie.F01_NJT; util/PaieClass.decompterHS
6) Persist Paie
   - Create/update one Paie per (employee, motif, period) with computed fields and org snapshots
   - Guard: only if Employe.actif && !Employe.enConge && bt>0
   - Source: util/PaieClass.insertPaie

### Formula engine for rubriques
- Definition storage: `Rubriqueformule` entries per rubrique and part:
  - partie: 'B' for base, 'N' for nombre
  - type/value:
    - O: operator token (+,-,*,/)
    - N: numeric literal
    - F: function code (e.g., F01..F24)
    - R: reference to another rubrique id (inject its montant)
- Evaluation: concatenate tokens into an expression string and evaluate
- Built-in functions (subset):
  - F01_NJT, F02_sbJour, F03_sbHoraire, F04_TauxAnciennete, F05/F06/F07 cumul BI/BNI/RET since last depart, F08 cumul Brut 12 months, F09 salaire brut fixe, F10 SMIG, F11 SMIG horaire, F12/F13/F14 licenciement/retraite rates, F15 taux PSRA, F16 préavis months, F18 nb SMIG region (Origines), F19 taux présence, F20 base ind logement, F21 salaire net, F22 nb enfants, F23/F23X ancienneté spéciale, F24 augmentation salaire fixe
- Sources: entity/Rubriqueformule.java; util/PaieClass.formulRubrique, FonctionsPaie.java

### Motifs and interactions
- Motif SN (id=1): regular salary; engagements applied on echeancecourante
- Motif CNG (id=2): leave payroll; engagements only if Paramgen.retEngOnConge; cumulative bases may include or exclude current SN based on Paramgen.addCurrentSalInCumulCng
- Other motifs (bonuses, specials): supported through same `Rubriquepaie` and Motif flags
- Sources: util/PaieClass.CNGByPeriodeByDep/SNByPeriodeByDep; cumulTypeById; entity/Motif.java

### Global parameters (Paramgen) impacting payroll
- abatement (ITS abatement), modeITS (T variants for tranche rates), deductionCnssdeIts, deductionCnamdeIts
- periodeCourante/periodeSuivante/periodeCloture; smig; njtDefault
- quotaEcheanceRae (engagement cap %), retEngOnConge, appIndCompensatrice (augmentation compensating logic), addCurrentSalInCumulCng
- Sources: entity/Paramgen.java; util/PaieClass and FonctionsPaie usages

### Validations and constraints
- Uniqueness: one Rubriquepaie per (employe, rubrique, motif, periode); one Njtsalarie per (employe, motif, periode)
- Period closure: edits blocked at or before Paramgen.periodeCloture; tranches recorded on close
- Processing guards: skip non-active or on-leave employees; require positive `bt` to persist Paie
- Data integrity: Rubrique flags drive inclusion in bases; Motif and Employe flags (detache/exonore/expatrie) alter computations

### Integrations and outputs
- Attendance/time: NJT and hours pipeline feed formulas via functions and rubriques
- Payslips and reports: values surfaced via `Paie` fields and parameters; bank/virement and declarations consume `Paie` totals
- Banking: `Paie` stores bank/mode/IBAN snapshot for virement generation

### Acceptance criteria
- For a given period, running payroll produces one `Paie` per (employee, active motif) with consistent totals matching formula-driven rubriques and flags.
- CNSS/CNAM/ITS amounts honor Motif and Employe flags; ITS tranches reflect Paramgen toggles and expat handling.
- Engagement installments are posted per rules (SN always; CNG only if enabled) and respect `quotaEcheanceRae`.
- Persistence respects uniqueness and guards; on-leave employees aren’t processed in payroll core runs.

### Source references
- entity/Paie.java — persisted payroll totals and snapshots
- entity/Rubriquepaie.java — unique key and amount components
- entity/Rubrique.java, entity/Rubriqueformule.java — payroll items and formulas
- entity/Sysrubrique.java — mapping for system rubric IDs (usedRubID)
- entity/Njtsalarie.java — NJT per motif/period
- entity/Motif.java — tax/decl flags per motif
- entity/Paramgen.java — global toggles and rates
- util/PaieClass.java — core computation (paieCalcule, insertPaie, CNSS/ITS logic, engagements)
- util/FonctionsPaie.java — F01..F24 functions used in formulas
