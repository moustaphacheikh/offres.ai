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
   name = "motif",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Motif implements Serializable {
   private int id;
   private String nom;
   private boolean employeSoumisIts;
   private boolean declarationSoumisIts;
   private boolean declarationSoumisCnss;
   private boolean declarationSoumisCnam;
   private boolean employeSoumisCnam;
   private boolean employeSoumisCnss;
   private boolean actif;
   private Set<Njtsalarie> njtsalaries = new HashSet(0);
   private Set<Rubriquepaie> rubriquepaies = new HashSet(0);
   private Set<Paie> paies = new HashSet(0);

   public Motif() {
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

   @Column(
      name = "employeSoumisITS",
      nullable = false
   )
   public boolean isEmployeSoumisIts() {
      return this.employeSoumisIts;
   }

   public void setEmployeSoumisIts(boolean var1) {
      this.employeSoumisIts = employeSoumisIts;
   }

   @Column(
      name = "declarationSoumisITS",
      nullable = false
   )
   public boolean isDeclarationSoumisIts() {
      return this.declarationSoumisIts;
   }

   public void setDeclarationSoumisIts(boolean var1) {
      this.declarationSoumisIts = declarationSoumisIts;
   }

   @Column(
      name = "declarationSoumisCNSS",
      nullable = false
   )
   public boolean isDeclarationSoumisCnss() {
      return this.declarationSoumisCnss;
   }

   public void setDeclarationSoumisCnss(boolean var1) {
      this.declarationSoumisCnss = declarationSoumisCnss;
   }

   @Column(
      name = "declarationSoumisCNAM",
      nullable = false
   )
   public boolean isDeclarationSoumisCnam() {
      return this.declarationSoumisCnam;
   }

   public void setDeclarationSoumisCnam(boolean var1) {
      this.declarationSoumisCnam = declarationSoumisCnam;
   }

   @Column(
      name = "employeSoumisCNAM",
      nullable = false
   )
   public boolean isEmployeSoumisCnam() {
      return this.employeSoumisCnam;
   }

   public void setEmployeSoumisCnam(boolean var1) {
      this.employeSoumisCnam = employeSoumisCnam;
   }

   @Column(
      name = "employeSoumisCNSS",
      nullable = false
   )
   public boolean isEmployeSoumisCnss() {
      return this.employeSoumisCnss;
   }

   public void setEmployeSoumisCnss(boolean var1) {
      this.employeSoumisCnss = employeSoumisCnss;
   }

   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "motif"
   )
   public Set<Njtsalarie> getNjtsalaries() {
      return this.njtsalaries;
   }

   public void setNjtsalaries(Set<Njtsalarie> var1) {
      this.njtsalaries = njtsalaries;
   }

   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "motif"
   )
   public Set<Rubriquepaie> getRubriquepaies() {
      return this.rubriquepaies;
   }

   public void setRubriquepaies(Set<Rubriquepaie> var1) {
      this.rubriquepaies = rubriquepaies;
   }

   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "motif"
   )
   public Set<Paie> getPaies() {
      return this.paies;
   }

   public void setPaies(Set<Paie> var1) {
      this.paies = paies;
   }

   @Column(
      name = "actif",
      nullable = false
   )
   public boolean isActif() {
      return this.actif;
   }

   public void setActif(boolean var1) {
      this.actif = actif;
   }
}
