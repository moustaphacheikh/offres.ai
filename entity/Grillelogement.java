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
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "grillelogement",
   catalog = "eliyapaiebd",
   schema = "PAYROLL",
   uniqueConstraints = {@UniqueConstraint(
   columnNames = {"categorie", "situationFamiliale", "nbEnfants"}
)}
)
public class Grillelogement implements Serializable {
   private Integer id;
   private Grillesalairebase grillesalairebase;
   private String situationFamiliale;
   private int nbEnfants;
   private Double montant;

   public Grillelogement() {
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
   public Integer getId() {
      return this.id;
   }

   public void setId(Integer var1) {
      this.id = id;
   }

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "categorie",
      nullable = false
   )
   public Grillesalairebase getGrillesalairebase() {
      return this.grillesalairebase;
   }

   public void setGrillesalairebase(Grillesalairebase var1) {
      this.grillesalairebase = grillesalairebase;
   }

   @Column(
      name = "situationFamiliale",
      nullable = false,
      length = 1
   )
   public String getSituationFamiliale() {
      return this.situationFamiliale;
   }

   public void setSituationFamiliale(String var1) {
      this.situationFamiliale = situationFamiliale;
   }

   @Column(
      name = "nbEnfants",
      nullable = false
   )
   public int getNbEnfants() {
      return this.nbEnfants;
   }

   public void setNbEnfants(int var1) {
      this.nbEnfants = nbEnfants;
   }

   @Column(
      name = "montant",
      precision = 22,
      scale = 0
   )
   public Double getMontant() {
      return this.montant;
   }

   public void setMontant(Double var1) {
      this.montant = montant;
   }
}
