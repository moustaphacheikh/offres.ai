# 00 — Product Overview

## Purpose

ELIYA Paie is a payroll management system tailored to Mauritanian regulations, covering CNSS, CNAM, and ITS with end-to-end payroll processing, employee lifecycle management, time/attendance import, and official reporting. Branding appears as “ELIYA Paie” in reports (Source: report/bulletinPaie.jrxml — footer text “ELIYA PAIE - www.mccmr.com”). Compliance artifacts and fields are first-class in the data model (Source: entity/Paie.java — fields CNSS, CNAM, ITS).

### Scope and regulatory focus
- National scope: Mauritania (CNSS, CNAM, ITS) (Source: entity/Paie.java — CNSS, CNAM, ITS fields; report/declarationCNSS.jrxml).
- System-wide parameters: SMIG, abatement, period controls, NJT default, license, and bank virement settings (Source: entity/Paramgen.java — fields smig, abatement, periodeCourante, periodeSuivante, njtDefault, licenceKey, bankvirement, comptevirement).

## Core Value Proposition

- Automated tax and social contributions
	- CNSS, CNAM, ITS tracked on each payroll (Source: entity/Paie.java — CNSS, CNAM, ITS).
	- ITS/CNSS/CNAM calculations referenced via processing layer calls (Source: util/FonctionsPaie.java — F21_salaireNet uses CNSSm, CNAMm, ITSm through menu.pc; also Paramgen.usedIts to control ITS mode).

- Complete employee lifecycle
	- Rich employee model with hiring, seniority, termination, leave flags (Source: entity/Employe.java — dateEmbauche, dateAnciennete, dateDebauche, enDebauche, actif, enConge).
	- Organizational structure and banking info (Source: entity/Employe.java — directiongeneral, direction, departement, poste, banque, noCompteBanque, modePaiement).

- Time and attendance integration
	- Excel-based import into Donneespointeuse and payroll rubrics (Source: util/ReadExcel.java — read(), importeRubriques(), importeDonneesPersonnel()).

- Professional reporting
	- JasperReports-based payslip and declarations (Source: report/bulletinPaie.jrxml, report/declarationCNSS.jrxml).
	- Bank transfer statement by bank/period (Source: report/virementbank.jrxml — parameters PERIODE, BANQUE; fields paie_noCompteBanque, employe_nni, salaire).

- Configurable formula engine (24 predefined functions)
	- F01–F24 covering NJT, base salary, seniority, SMIG, severance rates, net salary, etc. (Source: util/FonctionsPaie.java — methods F01_NJT … F24_augmentationSalaireFixe and switch in calFonction).

- Banking integration
	- Per-employee bank account, domiciliation, and bank name tracked and surfaced in payroll and bank transfer report (Source: entity/Paie.java — banque, noCompteBanque, modePaiement, domicilie; report/virementbank.jrxml).

## Target Users

- HR Managers: Onboarding, modifications, termination, contracts, and categories (Source: entity/Employe.java — typeContrat, dateFinContrat, grillesalairebase).
- Payroll Administrators: Payroll cycle, rubrics, taxes, and nets (Source: entity/Paie.java — BT, BNI, net, retenuesBrut/Net; util/FonctionsPaie.java).
- Finance Teams: Bank transfers and costs aggregation (Source: report/virementbank.jrxml — group/total; entity/Paie.java — cumulBi, cumulBni, cumulNjt).
- Compliance Officers: CNSS/CNAM/ITS declarations and lists (Source: report/declarationCNSS.jrxml; entity/Paramgen.java — noCnss, noCnam).
- Time/Attendance Operators: Imports from time clock exports and Excel masters (Source: util/ReadExcel.java).
- System Administrators: Global settings, license, SMTP, branding (Source: entity/Paramgen.java — licenceKey, mailSmtpHost/User/Password/Port, logo, pub).

### Key Entities (high level)
- Employe: Personal, contract, org, banking, status, seniority (Source: entity/Employe.java).
- Paie: Periodic payroll amounts and classifications (Source: entity/Paie.java — periode, categorie, BT, BNI, CNSS, ITS, net, njt).
- Paramgen: Global payroll and legal parameters (Source: entity/Paramgen.java — smig, usedIts, abatement, deduction flags).
- Rubriquepaie/Rubrique: Rubric configuration and entries that compose totals (Source: util/FonctionsPaie.java — usage of Rubriquepaie in F02–F03/F09).

### Operational Highlights
- Period control and rollover (Source: entity/Paramgen.java — periodeCourante, periodeSuivante, periodeCloture).
- Seniority and compensation rules embedded (Source: util/FonctionsPaie.java — F04/F23/F23X).
- Payslip outputs include identifiers and org lines (Source: report/bulletinPaie.jrxml — employe_id, employe_nni, direction, departement, poste, NJT).