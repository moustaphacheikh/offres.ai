# 20 — Employee & Organization

## Feature: Employee Profile Management
- Description: Create and maintain complete employee master records used by payroll, reporting, and declarations.
- User Story: As an HR administrator, I create/update employee profiles with identity, legal, employment, organizational, and banking data to enable accurate payroll and compliance outputs.
- Acceptance Criteria:
	- Required fields captured: id, prenom, nom, nni, dateEmbauche, dateAnciennete, grillesalairebase (categorie), poste, departement, direction, directiongeneral, origine, contratHeureSemaine, statut, classification (Source: entity/Employe.java). 
	- Legal identifiers: noCnss (+ dateCnss), noCnam recorded when applicable (Source: entity/Employe.java — noCnss, dateCnss, noCnam).
	- Banking: banque, noCompteBanque, modePaiement, domicilie available for virement scenarios (Source: entity/Employe.java — banque, noCompteBanque, modePaiement, domicilie; report/virementbank.jrxml expects account per employee).
	- Payslip-required values present: employe_id, employe_prenom, employe_nom, employe_nni, employe_noCNSS, employe_noCNAM, employe_dateEmbauche, employe_dateAnciennete, employe_idPsservice, employe_contratHeureSemaine (Source: report/bulletinPaie.jrxml — parameters).
	- Status flags maintained: actif, enConge, enDebauche, expatrie, psservice (Source: entity/Employe.java).
	- Attachments/links: Support children (Enfants), diplomas (Diplome), and documents (Document) relations (Source: entity/Employe.java — OneToMany sets).
- Business Rules:
	- Identity: id is unique PK; nni captured for banking/reporting (Source: entity/Employe.java — id, nni; report/virementbank.jrxml uses employe_nni).
	- Banking: If modePaiement is Virement or domicilie is true, banque and noCompteBanque must be set before closing payroll for the period (Source: entity/Employe.java banking fields; report/virementbank.jrxml).
	- Organization: An employee must be assigned to poste, departement, direction, directiongeneral, and activite to feed reports (Source: entity/Employe.java; report/bulletinPaie.jrxml — paie_poste, paie_direction, paie_directiongenerale, paie_departement, paie_activite).
	- Seniority: dateAnciennete drives seniority rates used in F04/F23 functions (Source: util/FonctionsPaie.java — F04_TauxAnciennete, F23_*; entity/Employe.java — dateAnciennete).
	- Work time: contratHeureSemaine informs hourly computations (Source: util/FonctionsPaie.java — F03_sbHoraire, F11_smigHoraire; entity/Employe.java — contratHeureSemaine).
	- Cumulatives: cumul12dminitial, cumulBrutImposableInitial, cumulBrutNonImposableInitial, cumulNJTInitial seed historical totals (Source: entity/Employe.java).
- Dependencies: Organizational structure and salary grid set up first (Source: entity/Poste.java, Departement.java, Direction.java, Directiongeneral.java, Activite.java, Grillesalairebase.java).

### Data dictionary (selected fields)
- Identity & contact: id (PK), prenom, nom, nni, telephone, email, adresse (Source: entity/Employe.java).
- Personal: dateNaissance, lieuNaissance, nationalite, sexe, situationFamiliale, nbEnfants (Source: entity/Employe.java — fields).
- Legal: noCnss, dateCnss, noCnam (Source: entity/Employe.java).
- Employment: dateEmbauche, dateDebauche, raisonDebauche, dateAnciennete, typeContrat, dateFinContrat, statut, classification (Source: entity/Employe.java).
- Organization: poste, departement, direction, directiongeneral, activite (ManyToOne) (Source: entity/Employe.java; entity/Poste.java, Departement.java, Direction.java, Directiongeneral.java, Activite.java).
- Salary grid: grillesalairebase with categorie, salaireBase, statut, niveau (Source: entity/Employe.java; entity/Grillesalairebase.java).
- Origin/region: origines with nbSmighorPourIndConges (Source: entity/Employe.java; entity/Origines.java — nbSmighorPourIndConges).
- Banking: banque (ManyToOne), noCompteBanque, modePaiement, domicilie (Source: entity/Employe.java; entity/Banque.java).
- Workload: contratHeureSemaine (hours/week) (Source: entity/Employe.java).
- Status/flags: actif, enConge, enDebauche, expatrie, psservice (Source: entity/Employe.java).
- Visa/permits: noPassport, dateLivraisonPassport, dateExpirationPassport, dateDebutVisa, dateFinVisa, noCarteSejour, noPermiTravail, dateLivraisonPermiTravail, dateExpirationPermiTravail (Source: entity/Employe.java).
- Cumulatives: cumulNJTInitial, cumulBrutImposableInitial, cumulBrutNonImposableInitial, cumul12dminitial, dernierDepartInitial (Source: entity/Employe.java).
- Payroll-related: tauxPsra, nbMoisPreavis, tauxAnciennete, tauxRemborssementCnss/Cnam, tauxRembItstranche1/2/3 (Source: entity/Employe.java; util/FonctionsPaie.java uses these).
- IDs for time/HR: idSalariePointeuse, idPsservice, password, photo (Source: entity/Employe.java).

## Feature: Organizational Structure Setup
- Description: Define hierarchy and master data used by employee assignment and payroll reporting.
- User Story: As an HR director, I configure general directions, directions, departments, positions, activities, and salary grids to structure the workforce and compensation.
- Acceptance Criteria:
	- Can create Directiongeneral, Direction, Departement, Poste, Activite with name fields (Source: entity/Directiongeneral.java, Direction.java, Departement.java, Poste.java, Activite.java — nom).
	- Salary grid supports categorie (PK), salaireBase, statut, niveau (Source: entity/Grillesalairebase.java — categorie, salaireBase, statut, niveau).
	- Employees can be assigned to each org unit (Source: entity/Employe.java — ManyToOne links).
	- Payslip header renders org fields for each payroll (Source: report/bulletinPaie.jrxml — paie_poste, paie_direction, paie_directiongenerale, paie_departement, paie_activite).
- Business Rules:
	- Names length constraints respected (nom length varies by entity: Poste up to 300 chars; others commonly 50) (Source: entity/Poste.java — nom length 300; typical 50 in others).
	- Salary base and category levels configured per internal policy; category code is the identifier (Source: entity/Grillesalairebase.java — categorie @Id, salaireBase, niveau).
	- On payroll run, org assignments may be snapshotted onto Paie for the period (Source: entity/Paie.java — poste, direction, directiongeneral, departement, activite string fields).
- Dependencies: None (foundational for employee records and payroll).

## Feature: Family and Dependent Management
- Description: Maintain dependents to support allowances and other calculations.
- User Story: As an HR clerk, I record children and family details to enable correct benefits and statutory calculations.
- Acceptance Criteria:
	- Children stored with name, birth date, parental link, gender (Source: entity/Enfants.java — nomEnfant, dateNaissanace, mereOuPere, genre).
	- Employee nbEnfants kept in sync or derived for functions (Source: entity/Employe.java — nbEnfants; util/FonctionsPaie.java — F22_NbEnfants uses Employe.nbEnfants).
	- Origin/region parameter influences allowance (Source: util/FonctionsPaie.java — F18_NbSmigRegion uses employe.origines.nbSmighorPourIndConges).
- Business Rules:
	- Update nbEnfants when adding/removing Enfants records; use nbEnfants as the source of truth for payroll functions (Source: util/FonctionsPaie.java — F22_NbEnfants).
	- Validate birth dates; enforce reasonable limits per company policy (assumption; not enforced in code).
- Dependencies: Employee profiles exist.

## Entities & Relationships (excerpt)
- Employe — ManyToOne to: Poste, Departement, Directiongeneral, Direction, Grillesalairebase, Activite, Origines; OneToMany to: Enfants, Paie, Rubriquepaie, Conges, Njtsalarie, Jour, Diplome, Document (Source: entity/Employe.java).
- Poste/Departement/Direction/Directiongeneral/Activite — master tables with nom; OneToMany back to Employe (Source: entity/*.java as listed).
- Grillesalairebase — categorie (PK), salaireBase, statut, niveau; OneToMany to Employe and Grillelogement (Source: entity/Grillesalairebase.java).
- Banque — referenced by Employe for payroll payments; includes accounting numbers (Source: entity/Banque.java; entity/Employe.java — banque).
- Constraints and validations:
	- Employe.id is PK; several booleans and budgetannuel marked non-null (Source: entity/Employe.java — budgetannuel not null; psservice/expatrie booleans not null).
	- Length constraints per column annotations (e.g., nom up to 100, email up to 200) (Source: entity/Employe.java — annotations).
	- Payroll and reports require presence of org, legal, and banking fields at run time (Source: report/bulletinPaie.jrxml; report/virementbank.jrxml).