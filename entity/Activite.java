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
   name = "activite",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Activite implements Serializable {
   private int id;
   private String nom;
   private Set<Employe> employes = new HashSet(0);

   public Activite() {
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
   public int getId() {
      return this.id;
   }

   public void setId(int var1) {
      this.id = id;
   }

   @Column(
      name = "nom",
      length = 50
   )
   public String getNom() {
      return this.nom;
   }

   public void setNom(String var1) {
      this.nom = nom;
   }

   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "activite"
   )
   public Set<Employe> getEmployes() {
      return this.employes;
   }

   public void setEmployes(Set<Employe> var1) {
      this.employes = employes;
   }
}
