package com.mccmr.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class FichePaie implements Serializable {
   private String paie_categorie;
   private double paie_BT;
   private double paie_BNI;
   private double paie_CNSS;
   private double paie_ITS;
   private double paie_retenuesBrut;
   private double paie_retenuesNet;
   private double paie_RITS;
   private double paie_net;
   private double paie_njt;
   private double paie_nbrHS;
   private double paie_CNAM;
   private String paie_periodeLettre;
   private String paie_noCompteBanque;
   private String paie_modePaiement;
   private boolean paie_domicilie;
   private Date paie_paieDu;
   private Date paie_paieAu;
   private double paie_biCNAM;
   private double paie_biCNSS;
   private double paie_RCNSS;
   private double paie_RCNAM;
   private double paie_biAVNAT;
   private String paie_notePaie;
   private String motif_nom;
   private double paie_contratHeureMois;
   private String paie_banque;
   private String paie_poste;
   private String paie_departement;
   private int employe_id;
   private String employe_nom;
   private String employe_noCNSS;
   private String employe_noCNAM;
   private double employe_contratHeureSemaine;
   private Date employe_dateAnciennete;
   private String employe_nni;
   private Date employe_dateEmbauche;
   private String employe_prenom;
   private String employe_idPsservice;
   private List<FichePaieDetail> fichePaieDetailList = new ArrayList();
   private JRBeanCollectionDataSource fichePaieDetailDataSource;

   public FichePaie() {
   }

   public String getPaie_categorie() {
      return this.paie_categorie;
   }

   public void setPaie_categorie(String var1) {
      this.paie_categorie = paie_categorie;
   }

   public double getPaie_BT() {
      return this.paie_BT;
   }

   public void setPaie_BT(double var1) {
      this.paie_BT = paie_BT;
   }

   public double getPaie_BNI() {
      return this.paie_BNI;
   }

   public void setPaie_BNI(double var1) {
      this.paie_BNI = paie_BNI;
   }

   public double getPaie_CNSS() {
      return this.paie_CNSS;
   }

   public void setPaie_CNSS(double var1) {
      this.paie_CNSS = paie_CNSS;
   }

   public double getPaie_ITS() {
      return this.paie_ITS;
   }

   public void setPaie_ITS(double var1) {
      this.paie_ITS = paie_ITS;
   }

   public double getPaie_retenuesBrut() {
      return this.paie_retenuesBrut;
   }

   public void setPaie_retenuesBrut(double var1) {
      this.paie_retenuesBrut = paie_retenuesBrut;
   }

   public double getPaie_retenuesNet() {
      return this.paie_retenuesNet;
   }

   public void setPaie_retenuesNet(double var1) {
      this.paie_retenuesNet = paie_retenuesNet;
   }

   public double getPaie_RITS() {
      return this.paie_RITS;
   }

   public void setPaie_RITS(double var1) {
      this.paie_RITS = paie_RITS;
   }

   public double getPaie_net() {
      return this.paie_net;
   }

   public void setPaie_net(double var1) {
      this.paie_net = paie_net;
   }

   public double getPaie_njt() {
      return this.paie_njt;
   }

   public void setPaie_njt(double var1) {
      this.paie_njt = paie_njt;
   }

   public double getPaie_nbrHS() {
      return this.paie_nbrHS;
   }

   public void setPaie_nbrHS(double var1) {
      this.paie_nbrHS = paie_nbrHS;
   }

   public double getPaie_CNAM() {
      return this.paie_CNAM;
   }

   public void setPaie_CNAM(double var1) {
      this.paie_CNAM = paie_CNAM;
   }

   public String getPaie_periodeLettre() {
      return this.paie_periodeLettre;
   }

   public void setPaie_periodeLettre(String var1) {
      this.paie_periodeLettre = paie_periodeLettre;
   }

   public String getPaie_noCompteBanque() {
      return this.paie_noCompteBanque;
   }

   public void setPaie_noCompteBanque(String var1) {
      this.paie_noCompteBanque = paie_noCompteBanque;
   }

   public String getPaie_modePaiement() {
      return this.paie_modePaiement;
   }

   public void setPaie_modePaiement(String var1) {
      this.paie_modePaiement = paie_modePaiement;
   }

   public boolean isPaie_domicilie() {
      return this.paie_domicilie;
   }

   public void setPaie_domicilie(boolean var1) {
      this.paie_domicilie = paie_domicilie;
   }

   public Date getPaie_paieDu() {
      return this.paie_paieDu;
   }

   public void setPaie_paieDu(Date var1) {
      this.paie_paieDu = paie_paieDu;
   }

   public Date getPaie_paieAu() {
      return this.paie_paieAu;
   }

   public void setPaie_paieAu(Date var1) {
      this.paie_paieAu = paie_paieAu;
   }

   public double getPaie_biCNAM() {
      return this.paie_biCNAM;
   }

   public void setPaie_biCNAM(double var1) {
      this.paie_biCNAM = paie_biCNAM;
   }

   public double getPaie_biCNSS() {
      return this.paie_biCNSS;
   }

   public void setPaie_biCNSS(double var1) {
      this.paie_biCNSS = paie_biCNSS;
   }

   public double getPaie_RCNSS() {
      return this.paie_RCNSS;
   }

   public void setPaie_RCNSS(double var1) {
      this.paie_RCNSS = paie_RCNSS;
   }

   public double getPaie_RCNAM() {
      return this.paie_RCNAM;
   }

   public void setPaie_RCNAM(double var1) {
      this.paie_RCNAM = paie_RCNAM;
   }

   public double getPaie_biAVNAT() {
      return this.paie_biAVNAT;
   }

   public void setPaie_biAVNAT(double var1) {
      this.paie_biAVNAT = paie_biAVNAT;
   }

   public String getPaie_notePaie() {
      return this.paie_notePaie;
   }

   public void setPaie_notePaie(String var1) {
      this.paie_notePaie = paie_notePaie;
   }

   public String getMotif_nom() {
      return this.motif_nom;
   }

   public void setMotif_nom(String var1) {
      this.motif_nom = motif_nom;
   }

   public double getPaie_contratHeureMois() {
      return this.paie_contratHeureMois;
   }

   public void setPaie_contratHeureMois(double var1) {
      this.paie_contratHeureMois = paie_contratHeureMois;
   }

   public String getPaie_banque() {
      return this.paie_banque;
   }

   public void setPaie_banque(String var1) {
      this.paie_banque = paie_banque;
   }

   public String getPaie_poste() {
      return this.paie_poste;
   }

   public void setPaie_poste(String var1) {
      this.paie_poste = paie_poste;
   }

   public String getPaie_departement() {
      return this.paie_departement;
   }

   public void setPaie_departement(String var1) {
      this.paie_departement = paie_departement;
   }

   public int getEmploye_id() {
      return this.employe_id;
   }

   public void setEmploye_id(int var1) {
      this.employe_id = employe_id;
   }

   public String getEmploye_nom() {
      return this.employe_nom;
   }

   public void setEmploye_nom(String var1) {
      this.employe_nom = employe_nom;
   }

   public String getEmploye_noCNSS() {
      return this.employe_noCNSS;
   }

   public void setEmploye_noCNSS(String var1) {
      this.employe_noCNSS = employe_noCNSS;
   }

   public String getEmploye_noCNAM() {
      return this.employe_noCNAM;
   }

   public void setEmploye_noCNAM(String var1) {
      this.employe_noCNAM = employe_noCNAM;
   }

   public double getEmploye_contratHeureSemaine() {
      return this.employe_contratHeureSemaine;
   }

   public void setEmploye_contratHeureSemaine(double var1) {
      this.employe_contratHeureSemaine = employe_contratHeureSemaine;
   }

   public Date getEmploye_dateAnciennete() {
      return this.employe_dateAnciennete;
   }

   public void setEmploye_dateAnciennete(Date var1) {
      this.employe_dateAnciennete = employe_dateAnciennete;
   }

   public String getEmploye_nni() {
      return this.employe_nni;
   }

   public void setEmploye_nni(String var1) {
      this.employe_nni = employe_nni;
   }

   public Date getEmploye_dateEmbauche() {
      return this.employe_dateEmbauche;
   }

   public void setEmploye_dateEmbauche(Date var1) {
      this.employe_dateEmbauche = employe_dateEmbauche;
   }

   public String getEmploye_prenom() {
      return this.employe_prenom;
   }

   public void setEmploye_prenom(String var1) {
      this.employe_prenom = employe_prenom;
   }

   public String getEmploye_idPsservice() {
      return this.employe_idPsservice;
   }

   public void setEmploye_idPsservice(String var1) {
      this.employe_idPsservice = employe_idPsservice;
   }

   public List<FichePaieDetail> getFichePaieDetailList() {
      return this.fichePaieDetailList;
   }

   public void setFichePaieDetailList(List<FichePaieDetail> var1) {
      this.fichePaieDetailList = fichePaieDetailList;
   }

   public JRBeanCollectionDataSource getFichePaieDetailDataSource() {
      return new JRBeanCollectionDataSource(this.fichePaieDetailList, false);
   }
}
