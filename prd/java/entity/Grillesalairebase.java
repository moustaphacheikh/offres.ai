package com.mccmr.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(
   name = "grillesalairebase",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Grillesalairebase implements Serializable {
   private String categorie;
   private Statut statut;
   private double salaireBase;
   private String nomCategorie;
   private int niveau;
   private Set<Employe> employes = new HashSet(0);
   private Set<Grillelogement> grillelogements = new HashSet(0);

   public Grillesalairebase() {
   }

   public Grillesalairebase(String var1, double var2, String var4, int var5) {
      this.categorie = categorie;
      this.salaireBase = salaireBase;
      this.nomCategorie = nomCategorie;
      this.niveau = niveau;
   }

   public Grillesalairebase(String var1, Statut var2, double var3, String var5, int var6, Set<Employe> var7, Set<Grillelogement> var8) {
      this.categorie = categorie;
      this.statut = statut;
      this.salaireBase = salaireBase;
      this.nomCategorie = nomCategorie;
      this.niveau = niveau;
      this.employes = employes;
      this.grillelogements = grillelogements;
   }

   @Id
   @Column(
      name = "categorie",
      unique = true,
      nullable = false,
      length = 50
   )
   public String getCategorie() {
      return this.categorie;
   }

   public void setCategorie(String var1) {
      this.categorie = categorie;
   }

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "statut"
   )
   public Statut getStatut() {
      return this.statut;
   }

   public void setStatut(Statut var1) {
      this.statut = statut;
   }

   @Column(
      name = "salaireBase",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getSalaireBase() {
      return this.salaireBase;
   }

   public void setSalaireBase(double var1) {
      this.salaireBase = salaireBase;
   }

   @Column(
      name = "nomCategorie",
      nullable = false,
      length = 10
   )
   public String getNomCategorie() {
      return this.nomCategorie;
   }

   public void setNomCategorie(String var1) {
      this.nomCategorie = nomCategorie;
   }

   @Column(
      name = "niveau",
      nullable = false
   )
   public int getNiveau() {
      return this.niveau;
   }

   public void setNiveau(int var1) {
      this.niveau = niveau;
   }

   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "grillesalairebase"
   )
   public Set<Employe> getEmployes() {
      return this.employes;
   }

   public void setEmployes(Set<Employe> var1) {
      this.employes = employes;
   }

   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "grillesalairebase"
   )
   public Set<Grillelogement> getGrillelogements() {
      return this.grillelogements;
   }

   public void setGrillelogements(Set<Grillelogement> var1) {
      this.grillelogements = grillelogements;
   }
}
