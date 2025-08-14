package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "jour",
   catalog = "eliyapaiebd",
   schema = "PAYROLL",
   uniqueConstraints = {@UniqueConstraint(
   columnNames = {"employe", "dateJour"}
)}
)
public class Jour implements Serializable {
   private Long id;
   private Employe employe;
   private Date periode;
   private Date dateJour;
   private Double nbHeureJour;
   private Double nbHeureNuit;
   private double nbPrimePanier;
   private double nbPrimeEloignement;
   private String note;
   private boolean ferie100;
   private boolean siteExterne;
   private boolean ferie50;

   public Jour() {
   }

   @Id
   @GeneratedValue(
      generator = "incrementor"
   )
   @GenericGenerator(
      name = "incrementor",
      strategy = "increment"
   )
   @Column(
      name = "id",
      unique = true,
      nullable = false
   )
   public Long getId() {
      return this.id;
   }

   public void setId(Long var1) {
      this.id = id;
   }

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "employe"
   )
   public Employe getEmploye() {
      return this.employe;
   }

   public void setEmploye(Employe var1) {
      this.employe = employe;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "periode",
      nullable = false,
      length = 10
   )
   public Date getPeriode() {
      return this.periode;
   }

   public void setPeriode(Date var1) {
      this.periode = periode;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateJour",
      nullable = false,
      length = 10
   )
   public Date getDateJour() {
      return this.dateJour;
   }

   public void setDateJour(Date var1) {
      this.dateJour = dateJour;
   }

   @Column(
      name = "nbHeureJour",
      precision = 22,
      scale = 0
   )
   public Double getNbHeureJour() {
      return this.nbHeureJour;
   }

   public void setNbHeureJour(Double var1) {
      this.nbHeureJour = nbHeureJour;
   }

   @Column(
      name = "nbHeureNuit",
      precision = 22,
      scale = 0
   )
   public Double getNbHeureNuit() {
      return this.nbHeureNuit;
   }

   public void setNbHeureNuit(Double var1) {
      this.nbHeureNuit = nbHeureNuit;
   }

   @Column(
      name = "nbPrimePanier",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getNbPrimePanier() {
      return this.nbPrimePanier;
   }

   public void setNbPrimePanier(double var1) {
      this.nbPrimePanier = nbPrimePanier;
   }

   @Column(
      name = "nbPrimeEloignement",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getNbPrimeEloignement() {
      return this.nbPrimeEloignement;
   }

   public void setNbPrimeEloignement(double var1) {
      this.nbPrimeEloignement = nbPrimeEloignement;
   }

   @Column(
      name = "note"
   )
   public String getNote() {
      return this.note;
   }

   public void setNote(String var1) {
      this.note = note;
   }

   @Column(
      name = "ferie100",
      nullable = false
   )
   public boolean isFerie100() {
      return this.ferie100;
   }

   public void setFerie100(boolean var1) {
      this.ferie100 = ferie100;
   }

   @Column(
      name = "siteExterne",
      nullable = false
   )
   public boolean isSiteExterne() {
      return this.siteExterne;
   }

   public void setSiteExterne(boolean var1) {
      this.siteExterne = siteExterne;
   }

   @Column(
      name = "ferie50",
      nullable = false
   )
   public boolean isFerie50() {
      return this.ferie50;
   }

   public void setFerie50(boolean var1) {
      this.ferie50 = ferie50;
   }
}
