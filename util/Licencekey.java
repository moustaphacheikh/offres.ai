package com.mccmr.util;

import java.util.Date;

public class Licencekey {
   private long id;
   private String companytName;
   private String nbSalaryCode;
   private String custumerActiveVersion;
   private String pub;
   private Date dateInitLicence;
   private Date dateCurentLicence;
   private String licencePeriodicity;

   public String getNbSalaryCode() {
      return this.nbSalaryCode;
   }

   public void setNbSalaryCode(String var1) {
      this.nbSalaryCode = nbSalaryCode;
   }

   public Licencekey() {
   }

   public long getId() {
      return this.id;
   }

   public void setId(long var1) {
      this.id = id;
   }

   public String getCompanytName() {
      return this.companytName;
   }

   public void setCompanytName(String var1) {
      this.companytName = companytName;
   }

   public String getCustumerActiveVersion() {
      return this.custumerActiveVersion;
   }

   public void setCustumerActiveVersion(String var1) {
      this.custumerActiveVersion = custumerActiveVersion;
   }

   public String getPub() {
      return this.pub;
   }

   public void setPub(String var1) {
      this.pub = pub;
   }

   public Date getDateInitLicence() {
      return this.dateInitLicence;
   }

   public void setDateInitLicence(Date var1) {
      this.dateInitLicence = dateInitLicence;
   }

   public Date getDateCurentLicence() {
      return this.dateCurentLicence;
   }

   public void setDateCurentLicence(Date var1) {
      this.dateCurentLicence = dateCurentLicence;
   }

   public String getLicencePeriodicity() {
      return this.licencePeriodicity;
   }

   public void setLicencePeriodicity(String var1) {
      this.licencePeriodicity = licencePeriodicity;
   }
}
