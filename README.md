# offres.mr: Enhanced Technical Analysis
## Product Requirements Document (PRD) - Detailed Implementation Report

### Executive Summary

offres.mr is a comprehensive enterprise-grade payroll management system with sophisticated tax calculations, multi-device time tracking, and regulatory compliance for Mauritanian/French business environments.

**Critical Security Alert**: This system contains severe security vulnerabilities including hardcoded backdoor passwords that require immediate attention.

---

## 1. Data Architecture Analysis

### 1.1 Entity Model Overview (37+ entities)

**Core Employee Entity (`Employe.java`) - 72 fields:**
- Personal information (12 fields including NNI, birth details)
- Work schedule matrix (21 boolean fields for 7-day patterns)
- Immigration/expatriate management (10 fields)
- Employment details (15 fields including contracts, dates)
- Social security/tax configuration (8 fields)

**Critical Bug Found in ALL Entities:**
```java
// CRITICAL BUG - Prevents data persistence
public void setId(Integer var1) {
    this.id = id;  // Should be: this.id = var1;
}
```

---

## 2. Security Vulnerabilities (CRITICAL)

### 2.1 Authentication Bypass
```java
// Hardcoded master password in login.java
if (pass.equalsIgnoreCase("0033610420365")) {
    success = true; // BACKDOOR ACCESS
}
```

### 2.2 Security Issues
- **MD5 password hashing** (cryptographically broken)
- **Empty password vulnerability** (null passwords allowed)
- **Hardcoded database credentials**
- **Default root account** with empty password

**Immediate Actions Required:**
1. Remove hardcoded master password
2. Upgrade to bcrypt/Argon2 hashing
3. Fix empty password vulnerability
4. Secure database configuration

---

## 3. Payroll Processing Engine

### 3.1 Tax Calculation Algorithms

**CNSS (Social Security):**
- Rate: 1% of gross salary
- Ceiling: 15,000 MRU maximum
- Formula: `MIN(gross_salary, 15000) * 1%`

**CNAM (Health Insurance):**
- Rate: 4% of gross salary (uncapped)
- Formula: `gross_salary * 4%`

**ITS (Income Tax) - Progressive:**
- Bracket 1: 0-9,000 MRU at 15% (7.5% for expatriates)
- Bracket 2: 9,001+ at 20%
- Bracket 3: Higher threshold at 25%

### 3.2 Overtime Rates
- **115%**: First 8 overtime hours
- **140%**: Hours 9-14
- **150%**: Hours 15+ and night work
- **200%**: Holiday work

---

## 4. Time and Attendance

### 4.1 Device Integration
- **HIKVISION**: CSV-based import
- **ZKTecho**: Direct database connection
- **Excel/CSV**: Manual import capabilities

### 4.2 Time Processing
- Night shift handling (22:00+ differential)
- Overnight shift calculations
- Holiday premium processing (50% and 100%)
- Automatic meal/distance allowances

---

## 5. Reporting System

### 5.1 JasperReports Templates
- **bulletinPaie.jrxml**: Standard payslips
- **declarationCNSS.jrxml**: Social security declarations
- **declarationCNAM.jrxml**: Health insurance declarations
- **dITS.jrxml**: Income tax declarations
- **virementbank.jrxml**: Bank transfer files

### 5.2 Export Formats
- PDF with metadata branding
- Excel with advanced formatting
- UNL format for accounting systems
- CSV for data exchange

---

## 6. Financial Integration

### 6.1 Banking System
- Multi-bank payment support
- UNL file generation (58 fields)
- Bank transfer automation
- Account reconciliation

### 6.2 Accounting Integration
- Double-entry bookkeeping (`Masterpiece`/`Detailpiece`)
- Chart of accounts mapping
- Automatic journal entry generation
- Financial reporting compliance

---

## Key Findings and Recommendations

### Strengths
1. **Comprehensive Functionality**: Complete payroll management suite
2. **Regulatory Compliance**: Built-in Mauritanian tax law compliance
3. **Advanced Calculations**: Sophisticated overtime and tax algorithms
4. **Professional Reporting**: High-quality document generation
5. **Multi-Device Integration**: Flexible time clock connectivity

### Critical Issues
1. **Security Vulnerabilities**: Multiple critical security flaws
2. **Setter Bug Pattern**: Data persistence failures across all entities
3. **Performance Concerns**: Binary data storage in main entities
4. **Missing Audit Trails**: No user/timestamp tracking

### Immediate Actions Required
1. **Security Overhaul**: Remove backdoors, upgrade authentication
2. **Bug Fixes**: Correct setter method implementations
3. **Performance Optimization**: Relocate binary data storage
4. **Audit Enhancement**: Add comprehensive logging system

The system demonstrates sophisticated payroll management capabilities but requires urgent security remediation before production deployment.