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
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "weekot",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Weekot implements Serializable {
   private Long id;
   private Employe employe;
   private Date periode;
   private Date beginweek;
   private Date endweek;
   private Double ot115;
   private Double ot140;
   private Double ot150;
   private Double ot200;
   private double nbPrimePanier;
   private double nbPrimeEloignement;
   private String note;

   public Weekot() {
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
      name = "beginweek",
      nullable = false,
      length = 10
   )
   public Date getBeginweek() {
      return this.beginweek;
   }

   public void setBeginweek(Date var1) {
      this.beginweek = beginweek;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "endweek",
      nullable = false,
      length = 10
   )
   public Date getEndweek() {
      return this.endweek;
   }

   public void setEndweek(Date var1) {
      this.endweek = endweek;
   }

   @Column(
      name = "ot115",
      precision = 22,
      scale = 0
   )
   public Double getOt115() {
      return this.ot115;
   }

   public void setOt115(Double var1) {
      this.ot115 = ot115;
   }

   @Column(
      name = "ot140",
      precision = 22,
      scale = 0
   )
   public Double getOt140() {
      return this.ot140;
   }

   public void setOt140(Double var1) {
      this.ot140 = ot140;
   }

   @Column(
      name = "ot150",
      precision = 22,
      scale = 0
   )
   public Double getOt150() {
      return this.ot150;
   }

   public void setOt150(Double var1) {
      this.ot150 = ot150;
   }

   @Column(
      name = "ot200",
      precision = 22,
      scale = 0
   )
   public Double getOt200() {
      return this.ot200;
   }

   public void setOt200(Double var1) {
      this.ot200 = ot200;
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
}
