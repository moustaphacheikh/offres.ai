package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(
   name = "utilisateurs",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Utilisateurs implements Serializable {
   @Id
   @Column(
      name = "login",
      unique = true,
      nullable = false,
      length = 15
   )
   private String login;
   @Column(
      name = "password",
      nullable = true,
      length = 100
   )
   private String password;
   @Column(
      name = "nomusager",
      nullable = false,
      length = 60
   )
   private String nomusager;
   @Temporal(TemporalType.TIMESTAMP)
   @Column(
      name = "dersession",
      nullable = false,
      length = 19
   )
   private Date dersession;
   @Column(
      name = "ajout",
      nullable = false
   )
   private boolean ajout;
   @Column(
      name = "maj",
      nullable = false
   )
   private boolean maj;
   @Column(
      name = "suppression",
      nullable = false
   )
   private boolean suppression;
   @Column(
      name = "parametre",
      nullable = false
   )
   private boolean parametre;
   @Column(
      name = "cloture",
      nullable = false
   )
   private boolean cloture;
   @Column(
      name = "securite",
      nullable = false
   )
   private boolean securite;
   @Column(
      name = "rubriquepaie",
      nullable = false
   )
   private boolean rubriquepaie;
   @Column(
      name = "grillesb",
      nullable = false
   )
   private boolean grillesb;
   @Column(
      name = "grillelog",
      nullable = false
   )
   private boolean grillelog;
   @Column(
      name = "originesal",
      nullable = false
   )
   private boolean originesal;
   @Column(
      name = "suppsal",
      nullable = false
   )
   private boolean suppsal;
   @Column(
      name = "motifpaie",
      nullable = false
   )
   private boolean motifpaie;
   @Column(
      name = "sal_identite",
      nullable = false
   )
   private boolean salIdentite;
   @Column(
      name = "sal_diplome",
      nullable = false
   )
   private boolean salDiplome;
   @Column(
      name = "sal_contrat",
      nullable = false
   )
   private boolean salContrat;
   @Column(
      name = "sal_retenueae",
      nullable = false
   )
   private boolean salRetenueae;
   @Column(
      name = "sal_conge",
      nullable = false
   )
   private boolean salConge;
   @Column(
      name = "sal_hs",
      nullable = false
   )
   private boolean salHs;
   @Column(
      name = "sal_paie",
      nullable = false
   )
   private boolean salPaie;
   @Column(
      name = "sal_add",
      nullable = false
   )
   private boolean salAdd;
   @Column(
      name = "sal_update",
      nullable = false
   )
   private boolean salUpdate;
   @Column(
      name = "sal_doc"
   )
   private boolean salDoc;
   @Column(
      name = "dashboard"
   )
   private boolean dashboard;

   public Utilisateurs() {
   }

   public String getLogin() {
      return this.login;
   }

   public void setLogin(String var1) {
      this.login = login;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = password;
   }

   public String getNomusager() {
      return this.nomusager;
   }

   public void setNomusager(String var1) {
      this.nomusager = nomusager;
   }

   public Date getDersession() {
      return this.dersession;
   }

   public void setDersession(Date var1) {
      this.dersession = dersession;
   }

   public boolean isAjout() {
      return this.ajout;
   }

   public void setAjout(boolean var1) {
      this.ajout = ajout;
   }

   public boolean isMaj() {
      return this.maj;
   }

   public void setMaj(boolean var1) {
      this.maj = maj;
   }

   public boolean isSuppression() {
      return this.suppression;
   }

   public void setSuppression(boolean var1) {
      this.suppression = suppression;
   }

   public boolean isParametre() {
      return this.parametre;
   }

   public void setParametre(boolean var1) {
      this.parametre = parametre;
   }

   public boolean isCloture() {
      return this.cloture;
   }

   public void setCloture(boolean var1) {
      this.cloture = cloture;
   }

   public boolean isSecurite() {
      return this.securite;
   }

   public void setSecurite(boolean var1) {
      this.securite = securite;
   }

   public boolean isRubriquepaie() {
      return this.rubriquepaie;
   }

   public void setRubriquepaie(boolean var1) {
      this.rubriquepaie = rubriquepaie;
   }

   public boolean isGrillesb() {
      return this.grillesb;
   }

   public void setGrillesb(boolean var1) {
      this.grillesb = grillesb;
   }

   public boolean isGrillelog() {
      return this.grillelog;
   }

   public void setGrillelog(boolean var1) {
      this.grillelog = grillelog;
   }

   public boolean isOriginesal() {
      return this.originesal;
   }

   public void setOriginesal(boolean var1) {
      this.originesal = originesal;
   }

   public boolean isSuppsal() {
      return this.suppsal;
   }

   public void setSuppsal(boolean var1) {
      this.suppsal = suppsal;
   }

   public boolean isMotifpaie() {
      return this.motifpaie;
   }

   public void setMotifpaie(boolean var1) {
      this.motifpaie = motifpaie;
   }

   public boolean isSalIdentite() {
      return this.salIdentite;
   }

   public void setSalIdentite(boolean var1) {
      this.salIdentite = salIdentite;
   }

   public boolean isSalDiplome() {
      return this.salDiplome;
   }

   public void setSalDiplome(boolean var1) {
      this.salDiplome = salDiplome;
   }

   public boolean isSalContrat() {
      return this.salContrat;
   }

   public void setSalContrat(boolean var1) {
      this.salContrat = salContrat;
   }

   public boolean isSalRetenueae() {
      return this.salRetenueae;
   }

   public void setSalRetenueae(boolean var1) {
      this.salRetenueae = salRetenueae;
   }

   public boolean isSalConge() {
      return this.salConge;
   }

   public void setSalConge(boolean var1) {
      this.salConge = salConge;
   }

   public boolean isSalHs() {
      return this.salHs;
   }

   public void setSalHs(boolean var1) {
      this.salHs = salHs;
   }

   public boolean isSalPaie() {
      return this.salPaie;
   }

   public void setSalPaie(boolean var1) {
      this.salPaie = salPaie;
   }

   public boolean isSalAdd() {
      return this.salAdd;
   }

   public void setSalAdd(boolean var1) {
      this.salAdd = salAdd;
   }

   public boolean isSalUpdate() {
      return this.salUpdate;
   }

   public void setSalUpdate(boolean var1) {
      this.salUpdate = salUpdate;
   }

   public boolean isDashboard() {
      return this.dashboard;
   }

   public void setDashboard(boolean var1) {
      this.dashboard = dashboard;
   }

   public boolean isSalDoc() {
      return this.salDoc;
   }

   public void setSalDoc(boolean var1) {
      this.salDoc = salDoc;
   }
}
