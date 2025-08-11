package com.mccmr.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(
   name = "rubrique",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Rubrique implements Serializable {
   @Id
   @Column(
      name = "id",
      unique = true,
      nullable = false
   )
   private int id;
   @Column(
      name = "libelle",
      nullable = false,
      length = 500
   )
   private String libelle;
   @Column(
      name = "abreviation"
   )
   private String abreviation;
   @Column(
      name = "sens",
      nullable = false,
      length = 1
   )
   private String sens;
   @Column(
      name = "plafone",
      nullable = false
   )
   private boolean plafone;
   @Column(
      name = "cumulable",
      nullable = false
   )
   private boolean cumulable;
   @Column(
      name = "noCompteCompta",
      nullable = false
   )
   private long noCompteCompta;
   @Column(
      name = "noChapitreCompta",
      nullable = false
   )
   private long noChapitreCompta;
   @Column(
      name = "noCompteComptaCle",
      nullable = false,
      length = 10
   )
   private String noCompteComptaCle;
   @Column(
      name = "its",
      nullable = false
   )
   private boolean its;
   @Column(
      name = "cnss",
      nullable = false
   )
   private boolean cnss;
   @Column(
      name = "cnam",
      nullable = false
   )
   private boolean cnam;
   @Column(
      name = "deductionDu",
      nullable = false,
      length = 10
   )
   private String deductionDu;
   @Column(
      name = "avantagesNature",
      nullable = false
   )
   private boolean avantagesNature;
   @Column(
      name = "baseAuto",
      nullable = false
   )
   private boolean baseAuto;
   @Column(
      name = "nombreAuto",
      nullable = false
   )
   private boolean nombreAuto;
   @Column(
      name = "sys",
      nullable = false
   )
   private boolean sys;
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "rubrique"
   )
   private Set<Rubriquepaie> rubriquepaies = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "rubrique"
   )
   private Set<Retenuesaecheances> retenuesaecheanceses = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "rubrique"
   )
   private Set<Rubriqueformule> rubriqueformules = new HashSet(0);

   public Rubrique() {
   }

   public int getId() {
      return this.id;
   }

   public void setId(int var1) {
      this.id = id;
   }

   public String getLibelle() {
      return this.libelle;
   }

   public void setLibelle(String var1) {
      this.libelle = libelle;
   }

   public String getAbreviation() {
      return this.abreviation;
   }

   public void setAbreviation(String var1) {
      this.abreviation = abreviation;
   }

   public String getSens() {
      return this.sens;
   }

   public void setSens(String var1) {
      this.sens = sens;
   }

   public boolean isPlafone() {
      return this.plafone;
   }

   public void setPlafone(boolean var1) {
      this.plafone = plafone;
   }

   public boolean isCumulable() {
      return this.cumulable;
   }

   public void setCumulable(boolean var1) {
      this.cumulable = cumulable;
   }

   public long getNoCompteCompta() {
      return this.noCompteCompta;
   }

   public void setNoCompteCompta(long var1) {
      this.noCompteCompta = noCompteCompta;
   }

   public long getNoChapitreCompta() {
      return this.noChapitreCompta;
   }

   public void setNoChapitreCompta(long var1) {
      this.noChapitreCompta = noChapitreCompta;
   }

   public String getNoCompteComptaCle() {
      return this.noCompteComptaCle;
   }

   public void setNoCompteComptaCle(String var1) {
      this.noCompteComptaCle = noCompteComptaCle;
   }

   public boolean isIts() {
      return this.its;
   }

   public void setIts(boolean var1) {
      this.its = its;
   }

   public boolean isCnss() {
      return this.cnss;
   }

   public void setCnss(boolean var1) {
      this.cnss = cnss;
   }

   public boolean isCnam() {
      return this.cnam;
   }

   public void setCnam(boolean var1) {
      this.cnam = cnam;
   }

   public String getDeductionDu() {
      return this.deductionDu;
   }

   public void setDeductionDu(String var1) {
      this.deductionDu = deductionDu;
   }

   public boolean isAvantagesNature() {
      return this.avantagesNature;
   }

   public void setAvantagesNature(boolean var1) {
      this.avantagesNature = avantagesNature;
   }

   public boolean isBaseAuto() {
      return this.baseAuto;
   }

   public void setBaseAuto(boolean var1) {
      this.baseAuto = baseAuto;
   }

   public boolean isNombreAuto() {
      return this.nombreAuto;
   }

   public void setNombreAuto(boolean var1) {
      this.nombreAuto = nombreAuto;
   }

   public boolean isSys() {
      return this.sys;
   }

   public void setSys(boolean var1) {
      this.sys = sys;
   }

   public Set<Rubriquepaie> getRubriquepaies() {
      return this.rubriquepaies;
   }

   public void setRubriquepaies(Set<Rubriquepaie> var1) {
      this.rubriquepaies = rubriquepaies;
   }

   public Set<Retenuesaecheances> getRetenuesaecheanceses() {
      return this.retenuesaecheanceses;
   }

   public void setRetenuesaecheanceses(Set<Retenuesaecheances> var1) {
      this.retenuesaecheanceses = retenuesaecheanceses;
   }

   public Set<Rubriqueformule> getRubriqueformules() {
      return this.rubriqueformules;
   }

   public void setRubriqueformules(Set<Rubriqueformule> var1) {
      this.rubriqueformules = rubriqueformules;
   }
}
