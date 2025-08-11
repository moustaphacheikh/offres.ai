package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "retenuesaecheances",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Retenuesaecheances implements Serializable {
   private Long id;
   private Employe employe;
   private Rubrique rubrique;
   private Date periode;
   private Date dateAccord;
   private double capital;
   private double echeance;
   private boolean active;
   private boolean solde;
   private String note;
   private Double echeancecourante;
   private Double echeancecourantecng;
   private Set<Tranchesretenuesaecheances> tranchesretenuesaecheanceses = new HashSet(0);

   public Retenuesaecheances() {
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

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "rubrique"
   )
   public Rubrique getRubrique() {
      return this.rubrique;
   }

   public void setRubrique(Rubrique var1) {
      this.rubrique = rubrique;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "periode",
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
      name = "DateAccord",
      nullable = false,
      length = 10
   )
   public Date getDateAccord() {
      return this.dateAccord;
   }

   public void setDateAccord(Date var1) {
      this.dateAccord = dateAccord;
   }

   @Column(
      name = "capital",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getCapital() {
      return this.capital;
   }

   public void setCapital(double var1) {
      this.capital = capital;
   }

   @Column(
      name = "echeance",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getEcheance() {
      return this.echeance;
   }

   public void setEcheance(double var1) {
      this.echeance = echeance;
   }

   @Column(
      name = "active",
      nullable = false
   )
   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean var1) {
      this.active = active;
   }

   @Column(
      name = "solde",
      nullable = false
   )
   public boolean isSolde() {
      return this.solde;
   }

   public void setSolde(boolean var1) {
      this.solde = solde;
   }

   @Column(
      name = "note",
      length = 500
   )
   public String getNote() {
      return this.note;
   }

   public void setNote(String var1) {
      this.note = note;
   }

   @Column(
      name = "echeancecourante",
      precision = 22,
      scale = 0
   )
   public Double getEcheancecourante() {
      return this.echeancecourante;
   }

   public void setEcheancecourante(Double var1) {
      this.echeancecourante = echeancecourante;
   }

   @Column(
      name = "echeancecourantecng",
      precision = 22,
      scale = 0
   )
   public Double getEcheancecourantecng() {
      return this.echeancecourantecng;
   }

   public void setEcheancecourantecng(Double var1) {
      this.echeancecourantecng = echeancecourantecng;
   }

   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "retenuesaecheances"
   )
   public Set<Tranchesretenuesaecheances> getTranchesretenuesaecheanceses() {
      return this.tranchesretenuesaecheanceses;
   }

   public void setTranchesretenuesaecheanceses(Set<Tranchesretenuesaecheances> var1) {
      this.tranchesretenuesaecheanceses = tranchesretenuesaecheanceses;
   }
}
