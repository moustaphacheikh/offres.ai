# 97 — Data Model & Relationships

This module documents the canonical data model used by payroll, grounded in the legacy JPA entities. It lists core entities, keys, foreign keys, cardinalities, and notable unique constraints. Field names and relationships come from the entity files under `entity/`.

## Core entities (purpose, key fields)
- Employe — master employee record; links to org, bank, category, and accumulators. PK: `id:int`. FKs: `poste`, `departement`, `direction`, `directiongeneral`, `grillesalairebase`, `activite`, `origines`, `banque`. Source: `entity/Employe.java`.
- Paramgen — global payroll parameters, periods, toggles, accounting numbers. PK: `id:int`. Source: `entity/Paramgen.java`.
- Rubrique — pay element definition with flags (ITS/CNSS/CNAM, base/nombre auto, plafonné, cumulable). PK: `id:int`. Source: `entity/Rubrique.java`.
- Rubriqueformule — per-rubric formula fragments. PK: `id:Long`. FK: `rubrique`. Source: `entity/Rubriqueformule.java`.
- Rubriquepaie — per-employee, per-period rubric line. PK: `id:Long`. FKs: `employe`, `rubrique`, `motif`. Unique: `(periode, employe, rubrique, motif)`. Source: `entity/Rubriquepaie.java`.
- Paie — per-employee pay header for a period/motif (BI, BNI, CNSS, CNAM, ITS, net, cumulatives, org snapshots). PK: `id:Long`. FKs: `employe`, `motif`, `paramgen`. Source: `entity/Paie.java`.
- Motif — payroll context/mode (applies contribution/tax switches). PK: `id:int`. Backrefs to `Paie`, `Rubriquepaie`, `Njtsalarie`. Source: `entity/Motif.java`.
- Njtsalarie — effective working days for an employee in a period/motif. PK: `id:Long`. FKs: `employe`, `motif`. Unique: `(periode, employe, motif)`. Source: `entity/Njtsalarie.java`.
- Semainetravail — week structure flags; PK: `jour:String`. Source: `entity/Semainetravail.java`.
- Retenuesaecheances — engagements/loans schedule by rubric. PK: `id:Long`. FKs: `employe`, `rubrique`. Child: `Tranchesretenuesaecheances`. Source: `entity/Retenuesaecheances.java`.
- Tranchesretenuesaecheances — monthly slices for engagements. PK: `id:Long`. FK: `retenuesaecheances`. Source: `entity/Tranchesretenuesaecheances.java`.
- Grillesalairebase — salary grid by `categorie` and `statut`. PK: `categorie:String`. FK: `statut`. Source: `entity/Grillesalairebase.java`.
- Grillelogement — housing grid by category/family/enfants. PK: `id:Int`. FK: `grillesalairebase`. Unique: `(categorie, situationFamiliale, nbEnfants)`. Source: `entity/Grillelogement.java`.
- Poste — job/position, parent of `Rubriquemodel` and referenced by `Employe`. PK: `id:Int`. Source: `entity/Poste.java`.
- Rubriquemodel — default rubrics/amounts attached to a position. PK: `id:long`. FKs: `poste`, `rubrique`. Source: `entity/Rubriquemodel.java`.
- Departement, Direction, Directiongeneral — org units referenced by `Employe`. PKs: `id:Int`. Sources: `entity/Departement.java`, `entity/Direction.java`, `entity/Directiongeneral.java`.
- Statut — employment status, referenced by `Grillesalairebase`. PK: `id:Int`. Source: `entity/Statut.java`.
- Banque — bank catalog referenced by `Employe`. PK: `id:Int`. Source: `entity/Banque.java`.
- Masterpiece — accounting header (piece). PK: `NUMERO:String`. Child: `Detailpiece`. Source: `entity/Masterpiece.java`.
- Detailpiece — accounting line; FK: `NUPIECE → Masterpiece`. PK: `NUMLIGNE:long`. Source: `entity/Detailpiece.java`.

## Relationships and cardinalities
- Employe ↔ Org
	- Employe → Poste (many-to-one)
	- Employe → Departement (many-to-one)
	- Employe → Direction (many-to-one)
	- Employe → Directiongeneral (many-to-one)
	- Employe → Grillesalairebase (many-to-one)
	- Employe → Activite/Origines/Banque (many-to-one)
- Payroll lines and headers
	- Employe → Rubriquepaie (one-to-many). Each line also references Rubrique and Motif.
	- Employe → Paie (one-to-many). Each header references Motif and Paramgen.
	- Motif → Paie, Rubriquepaie, Njtsalarie (one-to-many backrefs).
- Time and attendance
	- Employe → Njtsalarie (one-to-many), unique by `(periode, employe, motif)`.
	- Semainetravail is standalone (PK `jour`), used for schedule semantics.
- Leave
	- Employe → Conges (one-to-many). Source: `entity/Conges.java`.
- Engagements/loans
	- Employe → Retenuesaecheances (one-to-many) and Rubrique → Retenuesaecheances (one-to-many).
	- Retenuesaecheances → Tranchesretenuesaecheances (one-to-many).
- Configuration
	- Rubrique → Rubriqueformule (one-to-many).
	- Poste → Rubriquemodel (one-to-many). Rubriquemodel → Rubrique (many-to-one).
- Grids and housing
	- Statut → Grillesalairebase (one-to-many).
	- Grillesalairebase → Employe (one-to-many) and → Grillelogement (one-to-many).
- Accounting
	- Masterpiece → Detailpiece (one-to-many). Detailpiece → Masterpiece (many-to-one).

## Keys and constraints (selected)
- Primary keys
	- `Employe.id:int`, `Rubrique.id:int`, `Paramgen.id:int`, `Paie.id:Long`, `Rubriquepaie.id:Long`, `Rubriqueformule.id:Long`, `Njtsalarie.id:Long`, `Retenuesaecheances.id:Long`, `Tranchesretenuesaecheances.id:Long`, `Conges.id:Long`, `Poste.id:Int`, `Departement.id:Int`, `Direction.id:Int`, `Directiongeneral.id:Int`, `Statut.id:Int`, `Banque.id:Int`, `Grillesalairebase.categorie:String`, `Grillelogement.id:Int`, `Masterpiece.NUMERO:String`, `Detailpiece.NUMLIGNE:long`, `Semainetravail.jour:String`.
- Unique constraints
	- Rubriquepaie: `(periode, employe, rubrique, motif)` unique. Source: `entity/Rubriquepaie.java`.
	- Njtsalarie: `(periode, employe, motif)` unique. Source: `entity/Njtsalarie.java`.
	- Grillelogement: `(categorie, situationFamiliale, nbEnfants)` unique. Source: `entity/Grillelogement.java`.
- Nullability/highlights
	- Most FK fields in `Paie`, `Rubriquepaie`, `Conges`, `Rubriqueformule` are `nullable=false` per annotations; honor referential integrity in DB/migrations.
	- `Employe` holds booleans affecting payroll applicability (`detacheCnss`, `detacheCnam`, `exonoreIts`, etc.) but does not imply cascaded deletes; prefer restrict-on-delete.

## Temporal model
- Many entities carry `periode:DATE` (Paie, Rubriquepaie, Njtsalarie, Conges, Retenues/Tranches). Comparisons and uniqueness are scoped to that period.
- `Paramgen` holds the current/succeeding period and closure (`periodeCourante`, `periodeSuivante`, `periodeCloture`). `Paie` also stores `paieDu`/`paieAu` for display.

## Aggregates
- Payroll run aggregate
	- Root: `Paie` (header) per `(employe, motif, periode)`.
	- Children: `Rubriquepaie` lines for the same tuple, attendance (`Njtsalarie`) and any `Retenuesaecheances` slice for the period.
- Accounting aggregate
	- Root: `Masterpiece` (accounting piece) with `Detailpiece` lines, often generated from payroll totals.

## Grounding references
- Employees and org: `entity/Employe.java`, `entity/Poste.java`, `entity/Departement.java`, `entity/Direction.java`, `entity/Directiongeneral.java`
- Payroll: `entity/Paie.java`, `entity/Rubriquepaie.java`, `entity/Rubrique.java`, `entity/Rubriqueformule.java`, `entity/Motif.java`, `entity/Paramgen.java`
- Time/leave: `entity/Njtsalarie.java`, `entity/Semainetravail.java`, `entity/Conges.java`
- Grids/housing: `entity/Grillesalairebase.java`, `entity/Grillelogement.java`, `entity/Statut.java`
- Engagements: `entity/Retenuesaecheances.java`, `entity/Tranchesretenuesaecheances.java`
- Banking/accounting: `entity/Banque.java`, `entity/Masterpiece.java`, `entity/Detailpiece.java`

## Change log (this revision)
- Rewrote with explicit PKs, FKs, cardinalities, and unique constraints grounded in entity annotations.
- Clarified payroll aggregates (Paie header with Rubriquepaie lines) and temporal scoping by `periode`.
- Added organizational, grid, housing, engagements, attendance, and accounting relationships.
