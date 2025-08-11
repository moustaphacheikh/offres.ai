# offres.mr: Comprehensive Payroll Management System
## Product Requirements Document (PRD)

### Executive Summary

offres.mr is a comprehensive enterprise-grade payroll management system designed for French-speaking organizations, particularly suited for Mauritanian and French business environments. The system provides end-to-end payroll processing, employee management, time tracking, benefits administration, and regulatory compliance capabilities.

**Key System Capabilities:**
- Multi-entity payroll processing with complex tax calculations
- Integrated time and attendance management
- Comprehensive employee lifecycle management
- Advanced reporting and analytics
- Regulatory compliance for CNSS, CNAM, and ITS declarations
- Banking integration and payment processing
- Role-based security and user management

### System Architecture Overview

**Technology Stack:**
- **Frontend**: Java Swing desktop application
- **Backend**: Java with Hibernate/JPA persistence
- **Database**: Relational database (eliyapaiebd.PAYROLL schema)
- **Reporting**: JasperReports integration with PDF/Excel export
- **UI Framework**: Custom Material Design icons and modern UI components

**Core Architectural Patterns:**
- Model-View-Controller (MVC) architecture
- Entity-Relationship data modeling
- Event-driven user interface
- Service layer abstraction for business logic

---

## 1. Data Architecture and Entity Models

### 1.1 Database Schema Architecture

**Core Database Configuration:**
- **Database Name**: `eliyapaiebd`
- **Schema**: `PAYROLL`
- **ORM Framework**: Hibernate JPA with 3NF normalization + strategic denormalization
- **Architecture Pattern**: Domain-Driven Design with rich entity models

### 1.2 Central Employee Entity (`Employe.java`)

**Primary Key**: Manual assignment (`id` - int) for business control
**Complete Field Analysis (72 fields total):**

**Personal Information (12 fields):**
```java
String nom (100 chars)              // Last name
String prenom (100 chars)           // First name  
String pere (200 chars)             // Father's name
String mere (200 chars)             // Mother's name
String nni (50 chars)               // National ID number
Date dateNaissance                  // Birth date
String lieuNaissance (200 chars)    // Birth place
String nationalite (100 chars)      // Nationality
String sexe (30 chars)              // Gender
String situationFamiliale (30 chars) // Marital status
Integer nbEnfants                   // Number of children
byte[] photo                        // Binary photo storage
```

**Employment Details (15 fields):**
```java
Date dateEmbauche                   // Hire date
Date dateAnciennete                 // Seniority date
Date dateDebauche                   // Termination date
String raisonDebauche (500 chars)   // Termination reason
String structureOrigine (200 chars) // Origin structure
String lieuTravail (200 chars)      // Work location
String typeContrat (50 chars)       // Contract type
Date dateFinContrat                 // Contract end date
boolean actif                       // Active status
boolean enConge                     // On leave status
boolean enDebauche                  // In termination process
String statut (50 chars)            // Employee status
String classification (200 chars)   // Classification
Double budgetannuel                 // Annual budget
double contratHeureSemaine          // Contract hours per week
```

**Work Schedule Matrix (21 boolean fields):**
```java
// Pattern: Day/FirstShift/Weekend for all 7 days
boolean lundiDs, lundiFs, lundiWe     // Monday variations
boolean mardiDs, mardiFs, mardiWe     // Tuesday variations
boolean mercrediDs, mercrediFs, mercrediWe // Wednesday variations
// ... continues for all 7 days
```

**Immigration/Expatriate Management (10 fields):**
```java
boolean expatrie                    // Expatriate status
String noPassport (100 chars)       // Passport number
Date dateLivraisonPassport          // Passport issue date
Date dateExpirationPassport         // Passport expiry date
Date dateDebutVisa                  // Visa start date
Date dateFinVisa                    // Visa end date
String noCarteSejour (100 chars)    // Residence permit number
String noPermiTravail (100 chars)   // Work permit number
Date dateLivraisonPermiTravail      // Work permit issue date
Date dateExpirationPermiTravail     // Work permit expiry date
```

### 1.3 Payroll Processing Entities

**Paie Entity - Strategic Denormalization:**
```java
@Entity
@Table(name = "paie", uniqueConstraints = {@UniqueConstraint(columnNames = {"periode", "employe", "motif"})})
public class Paie {
    // Core calculation fields
    double bt (Brut Total, required)
    double bni (Brut Non Imposable, required)
    double cnss, cnam, its (Tax amounts, required)
    double itsTranche1, itsTranche2, itsTranche3 (Progressive tax breakdown)
    double net (Final net salary, required)
    double njt (Working days, required)
    
    // Denormalized organizational data for performance
    String poste (100 chars)
    String departement (100 chars)
    String directiongeneral (100 chars)
    String direction (100 chars)
    String activite (100 chars)
    
    // Cumulative tracking
    double cumulBi, cumulBni, cumulNjt
}
```

**Rubrique Entity - Payroll Elements:**
```java
@Entity
public class Rubrique {
    String libelle (500 chars, required)    // Description
    String sens (1 char, required)          // "G" gain, other deduction
    boolean plafone (required)              // Subject to ceiling
    boolean cumulable (required)            // Accumulation allowed
    boolean its, cnss, cnam (required)      // Tax applicability
    boolean avantagesNature (required)      // Benefits in kind
    boolean baseAuto, nombreAuto (required) // Auto-calculation flags
    
    // Accounting integration (3 fields)
    long noCompteCompta, noChapitreCompta
    String noCompteComptaCle (10 chars)
}
```

### 1.4 Complex Entity Relationships

**Employee Entity Relationships (13 collections):**
```java
Set<Paie> paies                          // Payroll records
Set<Rubriquepaie> rubriquepaies          // Payroll line items
Set<Conges> congeses                     // Leave records
Set<Retenuesaecheances> retenuesaecheanceses // Installment deductions
Set<Weekot> weekots                      // Weekly overtime
Set<Njtsalarie> njtsalaries              // Working days records
Set<Jour> jours                          // Daily time records
Set<Enfants> enfantss                    // Children information
Set<Diplome> diplomes                    // Education/qualifications
Set<Document> documents                  // Document attachments
```

**Critical Setter Bug Pattern (Found in ALL entities):**
```java
// CRITICAL BUG - Prevents proper data persistence
public void setId(Integer var1) {
    this.id = id;  // Should be: this.id = var1;
}
```

### 1.5 Advanced Configuration Entity

**Paramgen Entity - System-Wide Configuration (81+ fields):**
```java
@Entity
@Table(name = "paramgen")
public class Paramgen {
    // Company information (15 fields)
    String nomEntreprise (300 chars), activiteEntreprise (500 chars)
    String responsableEntreprise (300 chars), qualiteResponsable (300 chars)
    byte[] logo // Binary logo storage
    
    // Financial configuration (10 fields)
    Double smig (required)              // Minimum wage
    double njtDefault (required)        // Default working days
    Double abatement (required)         // Tax abatement
    
    // Period management (3 fields)
    Date periodeCourante, periodeSuivante, periodeCloture
    
    // Automation flags (8 fields)
    boolean primePanierAuto             // Auto meal allowance
    boolean ancienneteAuto              // Auto seniority calculation
    boolean deductionCnssdeIts          // Deduct CNSS from ITS
    boolean deductionCnamdeIts          // Deduct CNAM from ITS
    
    // Accounting integration (30+ fields)
    Long noComptaNet, noComptaIts, noComptaCnss, noComptaCnam
    String noComptaCleNet, noComptaCleIts, noComptaCleCnss, noComptaCleCnam
    // Multiple variations for patronal, medical, reimbursement accounts
    
    // Licensing system (5 fields)
    String licenceKey (500 chars)
    Date dateInitLicence, dateCurentLicence
    String licencePeriodicity, custumerActiveVersion
}
```

### 1.6 Formula Engine Architecture

**Rubriqueformule Entity - Dynamic Calculations:**
```java
@Entity
public class Rubriqueformule {
    Rubrique rubrique (Many-to-One, required)
    String partie (1 char, required)    // "B" base, "N" number
    String type (1 char, required)      // "O" operator, "N" number, "F" function, "R" rubrique reference
    String valText (10 chars)           // Text value or function code
    Double valNum                       // Numeric value
}
```

**Supported Formula Types:**
- **Mathematical Operations**: +, -, *, /, parentheses
- **Function References**: 24 predefined payroll functions
- **Rubrique Cross-References**: Link to other payroll elements
- **Constants and Variables**: Fixed values and employee-specific data

### 1.7 Time Management Entities

**Donneespointeuse - Raw Time Clock Data:**
```java
@Entity
public class Donneespointeuse {
    long id (auto-generated)
    Employe employe (required)
    Date heureJour (timestamp)           // Precise punch time
    String vinOut                        // "I" IN, "O" OUT
    boolean importe                      // Import tracking
}
```

**Jour - Processed Daily Records:**
```java
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"employe", "dateJour"})})
public class Jour {
    Double nbHeureJour, nbHeureNuit     // Day/night hour separation
    double nbPrimePanier                // Meal allowance count
    double nbPrimeEloignement           // Distance allowance count
    boolean ferie100, ferie50           // Holiday pay rates
    boolean siteExterne                 // External site work
}
```

### 1.8 Advanced Financial Entities

**Retenuesaecheances - Installment Deductions:**
```java
@Entity
public class Retenuesaecheances {
    double capital (required)           // Total deduction amount
    double echeance (required)          // Installment amount
    Double echeancecourante             // Current installment
    Double echeancecourantecng          // Leave period adjustment
    boolean active, solde               // Status tracking
    Set<Tranchesretenuesaecheances> tranches // Payment history
}
```

### 1.9 Architectural Strengths & Concerns

**Design Strengths:**
- Comprehensive domain coverage (37+ entities)
- Regulatory compliance built into structure
- Flexible formula system for complex calculations
- Audit trail through period-based data
- Performance optimization via strategic denormalization

**Critical Issues:**
- **Setter Bug Pattern**: All entities have incorrect setter implementations
- **Binary Data Storage**: Photos stored in main entity (performance impact)
- **Missing Audit Fields**: No created/modified timestamps or user tracking
- **String-based Accounting**: Should use typed entities for accounting integration

---

## 2. Authentication and Security System

### 2.1 Critical Security Vulnerabilities

**CRITICAL SECURITY ISSUES:**

**1. Hardcoded Master Password (CRITICAL):**
```java
// In login.java - Universal bypass password
public boolean logIn(String login, String pass) {
    if (user.getPassword() == null || user.getPassword().isEmpty() || 
        pass.equalsIgnoreCase("0033610420365")) {
        success = true; // BACKDOOR ACCESS
    }
    return success;
}
```

**2. MD5 Password Hashing (HIGH):**
```java
public String md5(String input) {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    digest.update(input.getBytes(), 0, input.length());
    md5 = (new BigInteger(1, digest.digest())).toString(16);
    return md5; // Cryptographically broken hashing
}
```

**3. Database Security Issues:**
```xml
<!-- Hardcoded credentials in persistence.xml -->
<property name="hibernate.connection.username" value="PAYROLL"/>
<property name="hibernate.connection.password" value="PAYROLL"/>
```

### 2.2 User Management Implementation

**Utilisateurs Entity (`Utilisateurs.java`):**
```java
@Entity
@Table(name = "utilisateurs", catalog = "eliyapaiebd", schema = "PAYROLL")
public class Utilisateurs {
    @Column(name = "login", unique = true, nullable = false, length = 15)
    private String login;
    
    @Column(name = "password", nullable = true, length = 100)
    private String password; // Nullable password field
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dersession; // Last session tracking
    
    // 22 granular permission flags
    private boolean ajout, maj, suppression, parametre, cloture, securite;
    private boolean rubriquepaie, grillesb, grillelog, originesal, motifpaie;
    private boolean salIdentite, salDiplome, salContrat, salRetenueae;
    private boolean salConge, salHs, salPaie, salAdd, salUpdate, salDoc;
    private boolean dashboard;
}
```

### 2.3 Granular Permission System (22 distinct permissions)

**General System Permissions:**
- `ajout` - Create records
- `maj` - Modify records  
- `suppression` - Delete records
- `parametre` - System configuration access
- `cloture` - Payroll closure operations
- `securite` - Security administration
- `dashboard` - Dashboard access

**Module-Specific Permissions:**
- `rubriquepaie` - Payroll items management
- `grillesb` - Salary scale management
- `grillelog` - Housing allowance scales
- `originesal` - Employee origin management
- `motifpaie` - Payroll motif management

**Employee Management Permissions:**
- `salIdentite` - Employee identity management
- `salDiplome` - Employee diploma management
- `salContrat` - Employee contract management
- `salRetenueae` - Employee deductions management
- `salConge` - Employee leave management
- `salHs` - Employee overtime management
- `salPaie` - Employee payroll access
- `salAdd` - Add new employees
- `salUpdate` - Update employee information
- `salDoc` - Employee document management

### 2.4 Security Implementation Issues

**Authentication Flow Problems:**
```java
// Empty password vulnerability
if (user.getPassword() == null || user.getPassword().isEmpty()) {
    success = true; // Allows login without password
}

// Case-insensitive password comparison (timing attack vulnerability)
if (this.md5(pass).equalsIgnoreCase(user.getPassword())) {
    success = true;
}
```

**Default Root Account:**
- System creates default root user with empty password
- Root account has all privileges enabled
- Special handling for root user bypasses normal security

### 2.5 License Management System

**Licencekey Entity (`Licencekey.java`):**
```java
public class Licencekey {
    private String companytName;
    private String nbSalaryCode;        // Employee count limit
    private String custumerActiveVersion;
    private Date dateInitLicence;
    private Date dateCurentLicence;
    private String licencePeriodicity;
}
```

**License Validation Process:**
- Online license validation through HTTPS
- Employee count enforcement
- Version compatibility checking
- Demo mode fallback when unlicensed
- Plain text license storage (security concern)

### 2.6 Security Recommendations

**Immediate Actions (Critical):**
1. **Remove hardcoded master password** `"0033610420365"`
2. **Upgrade password hashing** from MD5 to bcrypt/PBKDF2/Argon2
3. **Secure database configuration** - remove hardcoded credentials
4. **Fix empty password vulnerability**

**Security Enhancements:**
1. **Implement session management** with timeout and invalidation
2. **Add audit logging** for all security events
3. **Implement CSRF protection**
4. **Add server-side authorization checks**
5. **Strengthen license protection** with cryptographic validation

---

## 3. Payroll Processing Engine

### 3.1 Core Calculation Workflow (`PaieClass.java`)

**Main Payroll Calculation Method:**
```java
public Paie paieCalcule(Employe employe, Motif motif, Date paieDu, Date paieAu) {
    // 1. Initialize calculation variables
    Double mntITS = 0.0, mntCNSS = 0.0, mntCNAM = 0.0, mntNET = 0.0;
    Double heuresMens = employe.getContratHeureSemaine() * 52.0 / 12.0;
    
    // 2. Process salary rubrics (gains and deductions)
    Set<Rubriquepaie> dl = empRubriquepaie(employe, motif, periode);
    Double SalaireBrut = dl.stream()
        .filter(r -> r.getRubrique().getSens().equalsIgnoreCase("G"))
        .mapToDouble(r -> r.getMontant()).sum();
    
    // 3. Calculate tax bases
    Double RemunerationImposable = dl.stream()
        .filter(r -> r.getRubrique().getSens().equalsIgnoreCase("G") && r.getRubrique().isIts())
        .mapToDouble(r -> r.getMontant()).sum();
    
    // 4. Apply tax calculations
    if (!employe.isDetacheCnss() && motif.isEmployeSoumisCnss()) {
        mntCNSS = CNSSm(RemunerationSoumiCnss, tauxDevise, usedITS);
    }
    
    // 5. Calculate net salary
    mntNET = SalaireBrut - mntCNSS - mntCNAM - mntITS - RetenuesBruts 
             - RetenuesNet - AvantagesEnNature + mntRITS + mntRCNSS + mntRCNAM;
    
    return paieObject;
}
```

### 3.2 Tax Calculation Algorithms with Exact Formulas

**CNSS (Social Security) - 1% with 15,000 MRU ceiling:**
```java
public Double CNSSm(double montant, double tauxDevise, int usedITS) {
    double plafonCnss = 15000.0;  // Fixed ceiling
    double r;
    if (montant <= plafonCnss) {
        r = montant / 100.0;  // 1% rate
    } else {
        r = plafonCnss / 100.0;  // Apply ceiling = 150 MRU max
    }
    return r;
}
```

**CNAM (Health Insurance) - 4% uncapped:**
```java
public Double CNAMm(double montant) {
    double r = montant * 4.0 / 100.0;  // 4% rate, no ceiling
    return r > 0.0 ? r : 0.0;
}
```

**ITS (Income Tax) - Progressive Three-Bracket System:**
```java
// Bracket 1: 15% rate up to 9,000 MRU (7.5% for expatriates)
public Double tranche1ITS(int usedITS, double montant, double montantCNSS, 
                         double montantCNAM, double salaireBase, double avantagesNature, 
                         double tauxDevise, boolean expatrie) {
    double tranche1 = 9000.0;
    double taux1 = 0.15;  // 15% standard rate
    if (expatrie) taux1 /= 2.0; // 7.5% for expatriates
    
    double ri = montant - abattement - x_cnss - x_cnam;
    
    // Benefits in kind calculation (20% rule)
    double sb20pourCent = (salaireBase - avantagesNature) * 0.2;
    if (avantagesNature > sb20pourCent) {
        ri -= avantagesNature * 0.6; // 60% deduction for high benefits
    } else {
        ri -= avantagesNature; // Full deduction
    }
    
    return ri <= tranche1 ? ri * taux1 : tranche1 * taux1; // Max: 1,350 MRU
}

// Bracket 2: 20% rate from 9,001 to second threshold
// Bracket 3: 25% rate above second threshold
```

**Complete ITS Formula:**
```
NET_TAXABLE = GROSS_TAXABLE - 6000_ABATEMENT - CNSS_DEDUCTION - CNAM_DEDUCTION - BENEFITS_ADJUSTMENT
ITS_TOTAL = (TRANCHE1 * 15%) + (TRANCHE2 * 20%) + (TRANCHE3 * 25%)
```

### 3.3 Overtime Calculation Engine

**Multi-Rate Overtime System:**
```java
public double[] decompterHSList(Employe employe, List<Jour> dl) {
    double nHS15 = 0.0;   // 115% rate (normal + 15% premium)
    double nHS40 = 0.0;   // 140% rate (normal + 40% premium)  
    double nHS50 = 0.0;   // 150% rate (normal + 50% premium)
    double nHS100 = 0.0;  // 200% rate (normal + 100% premium)
    
    for (Jour rs : dl) {
        // Holiday premium handling
        if (rs.isFerie50()) {
            nHS50 += rs.getNbHeureJour();    // 50% holiday premium
            nHS100 += rs.getNbHeureNuit();   // 100% night holiday premium
        }
        
        // Weekly overtime calculation at week end
        if (isEndOfWeek) {
            double nhs = TOTHJOUR - x_hsem;  // Net overtime hours
            if (nhs > 0.0) {
                if (x_hsem < 48.0) {  // Standard week threshold
                    if (nhs <= 8.0) {
                        nHS15 += nhs;  // First 8 hours at 115%
                    } else {
                        nHS15 += 8.0;
                        double reste_hs = nhs - 8.0;
                        if (reste_hs <= 6.0) {
                            nHS40 += reste_hs;  // Next 6 hours at 140%
                        } else {
                            nHS40 += 6.0;
                            nHS50 += (nhs - 14.0);  // Additional hours at 150%
                        }
                    }
                }
            }
        }
    }
    
    return new double[]{TOTHJOUR, THN, nHS15, nHS40, nHS50, nHS100, nbPP, nbPE, nHS};
}
```

**Overtime Rate Structure:**
- **115%**: First 8 overtime hours per week
- **140%**: Hours 9-14 of weekly overtime
- **150%**: Hours 15+ and night work premiums
- **200%**: Holiday work and special circumstances

### 3.4 Advanced Formula Engine (`Calcul.java`)

**Mathematical Expression Evaluator:**
```java
public class Calcul {
    public double calculer(String expression) {
        this.chaine = expression + ";";
        this.resultat = this.expression();
        return this.resultat;
    }
    
    private double expression() {
        double n = this.terme();
        while(this.chaine.charAt(this.i) == '+' || this.chaine.charAt(this.i) == '-') {
            if (this.chaine.charAt(this.i) == '+') {
                n += this.terme();
            } else {
                n -= this.terme();
            }
        }
        return n;
    }
}
```

**Formula Capabilities:**
- Basic arithmetic operations (+, -, *, /)
- Parentheses support for complex expressions
- Error handling for division by zero
- Integration with 24 predefined payroll functions

### 3.5 Payroll Functions Library (`FonctionsPaie.java`)

**Key Calculation Functions:**

**Seniority Benefits (2% per year up to 14 years):**
```java
public Double F04_TauxAnciennete(Employe employe, Date periode) {
    double nj = (periode.getTime() - employe.getDateAnciennete().getTime()) / 86400000L;
    double nb_jr_apres_un_an = nj - 365.0;
    Double x = nb_jr_apres_un_an / 365.0;
    int annee = (int)Math.floor(x);
    
    if (annee >= 14) {
        if (annee >= 14 && annee < 15) return 0.28;
        if (annee >= 15 && annee < 16) return 0.29;
        if (annee >= 16) return 0.3;
    } else {
        return (double)annee * 0.02; // 2% per year
    }
}
```

**Severance Benefits:**
```java
public Double F12_TauxLicenciement(Employe employe, Date periode) {
    double annee = calculateServiceYears(employe, periode);
    
    if (annee >= 1.0 && annee < 5.0) {
        return 0.25 * annee; // 0.25 months per year
    }
    if (annee >= 5.0 && annee < 10.0) {
        return 1.25 + (annee - 5.0) * 0.3; // Enhanced rate
    }
    if (annee >= 10.0) {
        return 2.75 + (annee - 10.0) * 0.35; // Premium rate
    }
    return 0.0;
}
```

### 3.6 Net Salary Calculation Formula

**Complete Net Pay Computation:**
```
NET_SALARY = GROSS_TOTAL 
           - CNSS_EMPLOYEE 
           - CNAM_EMPLOYEE 
           - ITS_TOTAL 
           - GROSS_DEDUCTIONS 
           - NET_DEDUCTIONS 
           - BENEFITS_IN_KIND
           + ITS_REIMBURSEMENT 
           + CNSS_REIMBURSEMENT 
           + CNAM_REIMBURSEMENT
```

---

## 4. Time and Attendance Management

### 4.1 Time Tracking Features

**Multi-Device Integration:**
- HIKVISION time clock support
- ZKTecho device integration
- Excel/CSV import capabilities
- Manual time entry options

**Attendance Data Processing:**
- Raw punch data capture (IN/OUT)
- Daily work summary generation
- Overtime calculation automation
- Holiday and premium tracking

### 4.2 Work Schedule Management

**Schedule Configuration:**
- Weekly work pattern setup
- Employee-specific schedules
- Holiday calendar management
- External site work tracking

**Time Calculation Engine:**
- Regular hours computation
- Night shift differentials
- Weekend work handling
- Holiday pay calculations

---

## 5. Employee Management System

### 5.1 Employee Registration and Lifecycle

**Comprehensive Employee Profiles:**
- Personal information (50+ fields)
- Employment details and contracts
- Bank account and payment preferences
- Tax and social security configurations
- Work schedule assignments
- Document management

**Employee Search and Query:**
- Advanced search interface
- Multi-criteria filtering
- Export capabilities
- Bulk operations support

### 5.2 Organizational Structure Management

**Hierarchical Organization:**
- Department management
- Direction and general direction levels
- Position and job role definitions
- Cost center assignments

---

## 6. Benefits and Deductions Management

### 6.1 Benefits Administration

**Payroll Element Management:**
- Configurable benefits and deductions
- Formula-based calculations
- Automatic vs manual processing
- Employee-specific overrides

**Leave Management:**
- Vacation balance tracking
- Leave period management
- Integration with payroll
- Effective vs planned dates

### 6.2 Calculation Rules Engine

**Formula Builder System:**
- 24+ predefined functions
- Mathematical operators
- Conditional logic support
- Employee data references

---

## 7. Reporting and Analytics

### 7.1 Report Categories

**Payroll Reports:**
- Pay slips (bulletinPaie.jrxml)
- Salary statements
- Deduction summaries
- Tax withholding reports

**Compliance Reports:**
- CNSS declarations
- CNAM submissions
- ITS tax reports
- Annual declarations

**Management Reports:**
- Employee statistics
- Payroll analytics
- Departmental summaries
- Cumulative reports

### 7.2 Export Capabilities

**Output Formats:**
- PDF generation via JasperReports
- Excel exports with formatting
- CSV data exports
- Print-ready statements

---

## 8. Declarations and Compliance

### 8.1 Tax Declaration System

**Regulatory Compliance:**
- Quarterly CNSS declarations
- Monthly CNAM submissions
- Annual ITS declarations
- TA (Apprenticeship Tax) reporting

**Automated Calculations:**
- Tax bracket computations
- Social security contributions
- Medical insurance premiums
- Regulatory rate applications

### 8.2 Compliance Features

**Data Validation:**
- Calculation verification
- Regulatory rate validation
- Employee eligibility checks
- Period-based processing

---

## 9. Financial Integration

### 9.1 Banking and Payments

**Bank Integration:**
- Multiple bank account support
- Payment method configuration
- Bank transfer file generation
- UNL format export for banking systems

**Payment Processing:**
- Direct deposit (Virement)
- Cash payments
- Bank account management
- Payment reconciliation

### 9.2 Accounting Integration

**Double-Entry Accounting:**
- Automatic journal entry creation
- Chart of accounts integration
- Payroll expense allocation
- Financial reporting compliance

---

## 10. System Administration

### 10.1 Configuration Management

**System Parameters:**
- Company information setup
- Tax rate configurations
- Payroll period management
- Regional compliance settings

**Administrative Tools:**
- User management interface
- Permission administration
- System backup and restore
- Data import/export utilities

### 10.2 Data Management

**Import/Export Capabilities:**
- Excel data import
- CSV file processing
- Database backup/restore
- Configuration migration

---

## Technical Specifications

### 10.3 Integration Capabilities

**External System Integration:**
- Time clock device connectivity
- Banking system interfaces
- Accounting software integration
- Government reporting systems

**API and Data Exchange:**
- Excel import/export
- CSV data processing
- PDF report generation
- Database connectivity

### 10.4 Security Considerations

**Current Security Measures:**
- Role-based access control
- User session management
- Permission-based UI restrictions
- Basic audit trails

**Security Recommendations:**
- Upgrade from MD5 to modern password hashing
- Remove hardcoded backdoor passwords
- Implement session timeout
- Add comprehensive audit logging
- Enhance password complexity requirements

---

## System Requirements

### Functional Requirements

**Core Modules:**
1. Employee Management
2. Payroll Processing
3. Time and Attendance
4. Benefits Administration
5. Reporting and Analytics
6. Compliance and Declarations
7. Financial Integration
8. System Administration

### Non-Functional Requirements

**Performance:**
- Support for enterprise-scale employee counts
- Efficient batch processing capabilities
- Responsive desktop application

**Compliance:**
- French/Mauritanian labor law compliance
- Tax authority integration
- Social security reporting
- Data protection compliance

**Scalability:**
- Multi-company support
- Configurable tax regimes
- Extensible calculation engine
- Modular architecture

---

## Conclusion

offres.mr represents a mature, feature-rich payroll management system specifically designed for French-speaking business environments. The system demonstrates sophisticated understanding of French/Mauritanian payroll requirements, tax calculations, and regulatory compliance needs.

**Key Strengths:**
- Comprehensive payroll calculation engine
- Integrated time and attendance management
- Extensive reporting capabilities
- Multi-device time clock integration
- Regulatory compliance automation

**Areas for Enhancement:**
- Security infrastructure modernization
- API development for external integrations
- Mobile interface development
- Cloud deployment capabilities
- Enhanced audit and compliance tracking

The system provides a solid foundation for enterprise payroll management with room for modern security and architectural improvements.