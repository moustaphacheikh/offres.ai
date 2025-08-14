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
   name = "rubriquepaie",
   catalog = "eliyapaiebd",
   schema = "PAYROLL",
   uniqueConstraints = {@UniqueConstraint(
   columnNames = {"periode", "employe", "rubrique", "motif"}
)}
)
public class Rubriquepaie implements Serializable {
   private Long id;
   private Employe employe;
   private Rubrique rubrique;
   private Motif motif;
   private Date periode;
   private boolean fixe;
   private double montant;
   private Double nombre;
   private Double base;
   private Boolean importe;

   public Rubriquepaie() {
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
      name = "rubrique",
      nullable = false
   )
   public Rubrique getRubrique() {
      return this.rubrique;
   }

   public void setRubrique(Rubrique var1) {
      this.rubrique = rubrique;
   }

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "motif",
      nullable = false
   )
   public Motif getMotif() {
      return this.motif;
   }

   public void setMotif(Motif var1) {
      this.motif = motif;
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

   @Column(
      name = "fixe",
      nullable = false
   )
   public boolean isFixe() {
      return this.fixe;
   }

   public void setFixe(boolean var1) {
      this.fixe = fixe;
   }

   @Column(
      name = "montant",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getMontant() {
      return this.montant;
   }

   public void setMontant(double var1) {
      this.montant = montant;
   }

   @Column(
      name = "nombre",
      precision = 22,
      scale = 0
   )
   public Double getNombre() {
      return this.nombre;
   }

   public void setNombre(Double var1) {
      this.nombre = nombre;
   }

   @Column(
      name = "base",
      precision = 22,
      scale = 0
   )
   public Double getBase() {
      return this.base;
   }

   public void setBase(Double var1) {
      this.base = base;
   }

   @Column(
      name = "importe",
      nullable = false
   )
   public Boolean isImporte() {
      return this.importe;
   }

   public void setImporte(Boolean var1) {
      this.importe = importe;
   }
}
