package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(
   name = "Detailpiece",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Detailpiece implements Serializable {
   @Id
   @Column(
      name = "NUMLIGNE",
      unique = true,
      nullable = false
   )
   private long NUMLIGNE;
   @Column(
      name = "DATEOP"
   )
   @Temporal(TemporalType.DATE)
   private Date DATEOP;
   @Column(
      name = "JOURNAL"
   )
   private String JOURNAL;
   @Column(
      name = "COMPTE"
   )
   private String COMPTE;
   @Column(
      name = "LIBELLE"
   )
   private String LIBELLE;
   @Column(
      name = "MONTANT"
   )
   private Double MONTANT;
   @Column(
      name = "SENS"
   )
   private String SENS;
   @Column(
      name = "INTITULET"
   )
   private String INTITULET;
   @Column(
      name = "CVMRO_MONTANT"
   )
   private Double CVMRO_MONTANT;
   @Column(
      name = "DEVISE"
   )
   private String DEVISE;
   @Column(
      name = "COURS"
   )
   private Double COURS;
   @Column(
      name = "NUMERO_COURS"
   )
   private long NUMERO_COURS;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "NUPIECE"
   )
   private Masterpiece NUPIECE;

   public Detailpiece() {
   }

   public long getNUMLIGNE() {
      return this.NUMLIGNE;
   }

   public void setNUMLIGNE(long var1) {
      this.NUMLIGNE = NUMLIGNE;
   }

   public Date getDATEOP() {
      return this.DATEOP;
   }

   public void setDATEOP(Date var1) {
      this.DATEOP = DATEOP;
   }

   public String getJOURNAL() {
      return this.JOURNAL;
   }

   public void setJOURNAL(String var1) {
      this.JOURNAL = JOURNAL;
   }

   public String getCOMPTE() {
      return this.COMPTE;
   }

   public void setCOMPTE(String var1) {
      this.COMPTE = COMPTE;
   }

   public String getLIBELLE() {
      return this.LIBELLE;
   }

   public void setLIBELLE(String var1) {
      this.LIBELLE = LIBELLE;
   }

   public Double getMONTANT() {
      return this.MONTANT;
   }

   public void setMONTANT(Double var1) {
      this.MONTANT = MONTANT;
   }

   public String getSENS() {
      return this.SENS;
   }

   public void setSENS(String var1) {
      this.SENS = SENS;
   }

   public String getINTITULET() {
      return this.INTITULET;
   }

   public void setINTITULET(String var1) {
      this.INTITULET = INTITULET;
   }

   public Double getCVMRO_MONTANT() {
      return this.CVMRO_MONTANT;
   }

   public void setCVMRO_MONTANT(Double var1) {
      this.CVMRO_MONTANT = CVMRO_MONTANT;
   }

   public String getDEVISE() {
      return this.DEVISE;
   }

   public void setDEVISE(String var1) {
      this.DEVISE = DEVISE;
   }

   public Double getCOURS() {
      return this.COURS;
   }

   public void setCOURS(Double var1) {
      this.COURS = COURS;
   }

   public long getNUMERO_COURS() {
      return this.NUMERO_COURS;
   }

   public void setNUMERO_COURS(long var1) {
      this.NUMERO_COURS = NUMERO_COURS;
   }

   public Masterpiece getNUPIECE() {
      return this.NUPIECE;
   }

   public void setNUPIECE(Masterpiece var1) {
      this.NUPIECE = NUPIECE;
   }
}
