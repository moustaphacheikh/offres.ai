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
   name = "statut",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Statut implements Serializable {
   private Integer id;
   private String nom;
   private Set<Grillesalairebase> grillesalairebases = new HashSet(0);

   public Statut() {
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
      name = "nom",
      length = 250
   )
   public String getNom() {
      return this.nom;
   }

   public void setNom(String var1) {
      this.nom = nom;
   }

   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "statut"
   )
   public Set<Grillesalairebase> getGrillesalairebases() {
      return this.grillesalairebases;
   }

   public void setGrillesalairebases(Set<Grillesalairebase> var1) {
      this.grillesalairebases = grillesalairebases;
   }
}
