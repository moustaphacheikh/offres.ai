package com.mccmr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "rubriquemodel",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Rubriquemodel implements Serializable {
   private long id;
   private Poste poste;
   private Rubrique rubrique;
   private double montant;

   public Rubriquemodel() {
   }

   public Rubriquemodel(long var1, Rubrique var3, double var4) {
      this.id = id;
      this.rubrique = rubrique;
      this.montant = montant;
   }

   public Rubriquemodel(long var1, Poste var3, Rubrique var4, double var5) {
      this.id = id;
      this.poste = poste;
      this.rubrique = rubrique;
      this.montant = montant;
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
      name = "Id",
      unique = true,
      nullable = false
   )
   public long getId() {
      return this.id;
   }

   public void setId(long var1) {
      this.id = id;
   }

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "poste"
   )
   public Poste getPoste() {
      return this.poste;
   }

   public void setPoste(Poste var1) {
      this.poste = poste;
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
}
