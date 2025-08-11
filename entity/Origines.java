package com.mccmr.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "origines",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Origines implements Serializable {
   private Integer id;
   private String libelle;
   private Integer nbSmighorPourIndConges;
   private Set<Employe> employes = new HashSet(0);

   public Origines() {
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

   @Column(
      name = "libelle",
      length = 100
   )
   public String getLibelle() {
      return this.libelle;
   }

   public void setLibelle(String var1) {
      this.libelle = libelle;
   }

   @Column(
      name = "nbSMIGHorPourIndConges"
   )
   public Integer getNbSmighorPourIndConges() {
      return this.nbSmighorPourIndConges;
   }

   public void setNbSmighorPourIndConges(Integer var1) {
      this.nbSmighorPourIndConges = nbSmighorPourIndConges;
   }

   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "origines"
   )
   public Set<Employe> getEmployes() {
      return this.employes;
   }

   public void setEmployes(Set<Employe> var1) {
      this.employes = employes;
   }
}
