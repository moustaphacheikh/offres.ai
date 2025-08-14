package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(
   name = "Masterpiece",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Masterpiece implements Serializable {
   @Id
   @Column(
      name = "NUMERO",
      unique = true,
      nullable = false
   )
   private String NUMERO;
   @Column(
      name = "LIBELLE_SERVICE"
   )
   private String LIBELLE_SERVICE;
   @Column(
      name = "DATEOP"
   )
   @Temporal(TemporalType.DATE)
   private Date DATEOP;
   @Column(
      name = "RUBRIQUE"
   )
   private String RUBRIQUE;
   @Column(
      name = "BENEFICIAIRE"
   )
   private String BENEFICIAIRE;
   @Column(
      name = "TOTAL_DEBIT"
   )
   private Double TOTAL_DEBIT;
   @Column(
      name = "TOTAL_CREDIT"
   )
   private Double TOTAL_CREDIT;
   @Column(
      name = "INITIATEUR"
   )
   private String INITIATEUR;
   @Column(
      name = "INIT_HR"
   )
   private String INIT_HR;
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "NUPIECE"
   )
   private Set<Detailpiece> DETAILPIECES = new HashSet(0);

   public Masterpiece() {
   }

   public String getNUMERO() {
      return this.NUMERO;
   }

   public void setNUMERO(String var1) {
      this.NUMERO = NUMERO;
   }

   public String getLIBELLE_SERVICE() {
      return this.LIBELLE_SERVICE;
   }

   public void setLIBELLE_SERVICE(String var1) {
      this.LIBELLE_SERVICE = LIBELLE_SERVICE;
   }

   public Date getDATEOP() {
      return this.DATEOP;
   }

   public void setDATEOP(Date var1) {
      this.DATEOP = DATEOP;
   }

   public String getRUBRIQUE() {
      return this.RUBRIQUE;
   }

   public void setRUBRIQUE(String var1) {
      this.RUBRIQUE = RUBRIQUE;
   }

   public String getBENEFICIAIRE() {
      return this.BENEFICIAIRE;
   }

   public void setBENEFICIAIRE(String var1) {
      this.BENEFICIAIRE = BENEFICIAIRE;
   }

   public Double getTOTAL_DEBIT() {
      return this.TOTAL_DEBIT;
   }

   public void setTOTAL_DEBIT(Double var1) {
      this.TOTAL_DEBIT = TOTAL_DEBIT;
   }

   public Double getTOTAL_CREDIT() {
      return this.TOTAL_CREDIT;
   }

   public void setTOTAL_CREDIT(Double var1) {
      this.TOTAL_CREDIT = TOTAL_CREDIT;
   }

   public String getINITIATEUR() {
      return this.INITIATEUR;
   }

   public void setINITIATEUR(String var1) {
      this.INITIATEUR = INITIATEUR;
   }

   public String getINIT_HR() {
      return this.INIT_HR;
   }

   public void setINIT_HR(String var1) {
      this.INIT_HR = INIT_HR;
   }

   public Set<Detailpiece> getDETAILPIECES() {
      return this.DETAILPIECES;
   }

   public void setDETAILPIECES(Set<Detailpiece> var1) {
      this.DETAILPIECES = DETAILPIECES;
   }
}
