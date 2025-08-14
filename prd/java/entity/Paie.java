package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "paie",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Paie implements Serializable {
   private Long id;
   private Employe employe;
   private Motif motif;
   private Paramgen paramgen;
   private Date periode;
   private String categorie;
   private double contratHeureMois;
   private double bt;
   private double bni;
   private double cnss;
   private double its;
   private double itsTranche1;
   private double itsTranche2;
   private double itsTranche3;
   private double retenuesBrut;
   private double retenuesNet;
   private double rits;
   private double net;
   private double njt;
   private Double nbrHs;
   private double cnam;
   private String netEnLettre;
   private String periodeLettre;
   private String banque;
   private String noCompteBanque;
   private String modePaiement;
   private boolean domicilie;
   private double cumulBi;
   private double cumulBni;
   private double cumulNjt;
   private String poste;
   private String directiongeneral;
   private String direction;
   private String departement;
   private Date dateDernierDepart;
   private Date paieDu;
   private Date paieAu;
   private double biCnam;
   private double biCnss;
   private double rcnss;
   private double rcnam;
   private double biAvnat;
   private String notePaie;
   private Double fte;
   private String statut;
   private String classification;
   private String activite;

   public Paie() {
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
   public Long getId() {
      return this.id;
   }

   public void setId(Long var1) {
      this.id = id;
   }

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "employe",
      nullable = false
   )
   public Employe getEmploye() {
      return this.employe;
   }

   public void setEmploye(Employe var1) {
      this.employe = employe;
   }

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "motif",
      nullable = false
   )
   public Motif getMotif() {
      return this.motif;
   }

   public void setMotif(Motif var1) {
      this.motif = motif;
   }

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "paramgen",
      nullable = false
   )
   public Paramgen getParamgen() {
      return this.paramgen;
   }

   public void setParamgen(Paramgen var1) {
      this.paramgen = paramgen;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "periode",
      nullable = false,
      length = 10
   )
   public Date getPeriode() {
      return this.periode;
   }

   public void setPeriode(Date var1) {
      this.periode = periode;
   }

   @Column(
      name = "categorie",
      length = 20
   )
   public String getCategorie() {
      return this.categorie;
   }

   public void setCategorie(String var1) {
      this.categorie = categorie;
   }

   @Column(
      name = "contratHeureMois",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getContratHeureMois() {
      return this.contratHeureMois;
   }

   public void setContratHeureMois(double var1) {
      this.contratHeureMois = contratHeureMois;
   }

   @Column(
      name = "BT",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getBt() {
      return this.bt;
   }

   public void setBt(double var1) {
      this.bt = bt;
   }

   @Column(
      name = "BNI",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getBni() {
      return this.bni;
   }

   public void setBni(double var1) {
      this.bni = bni;
   }

   @Column(
      name = "CNSS",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getCnss() {
      return this.cnss;
   }

   public void setCnss(double var1) {
      this.cnss = cnss;
   }

   @Column(
      name = "ITS",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getIts() {
      return this.its;
   }

   public void setIts(double var1) {
      this.its = its;
   }

   @Column(
      name = "itsTranche1",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getItsTranche1() {
      return this.itsTranche1;
   }

   public void setItsTranche1(double var1) {
      this.itsTranche1 = itsTranche1;
   }

   @Column(
      name = "itsTranche2",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getItsTranche2() {
      return this.itsTranche2;
   }

   public void setItsTranche2(double var1) {
      this.itsTranche2 = itsTranche2;
   }

   @Column(
      name = "itsTranche3",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getItsTranche3() {
      return this.itsTranche3;
   }

   public void setItsTranche3(double var1) {
      this.itsTranche3 = itsTranche3;
   }

   @Column(
      name = "retenuesBrut",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getRetenuesBrut() {
      return this.retenuesBrut;
   }

   public void setRetenuesBrut(double var1) {
      this.retenuesBrut = retenuesBrut;
   }

   @Column(
      name = "retenuesNet",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getRetenuesNet() {
      return this.retenuesNet;
   }

   public void setRetenuesNet(double var1) {
      this.retenuesNet = retenuesNet;
   }

   @Column(
      name = "RITS",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getRits() {
      return this.rits;
   }

   public void setRits(double var1) {
      this.rits = rits;
   }

   @Column(
      name = "net",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getNet() {
      return this.net;
   }

   public void setNet(double var1) {
      this.net = net;
   }

   @Column(
      name = "njt",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getNjt() {
      return this.njt;
   }

   public void setNjt(double var1) {
      this.njt = njt;
   }

   @Column(
      name = "nbrHS",
      precision = 22,
      scale = 0
   )
   public Double getNbrHs() {
      return this.nbrHs;
   }

   public void setNbrHs(Double var1) {
      this.nbrHs = nbrHs;
   }

   @Column(
      name = "CNAM",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getCnam() {
      return this.cnam;
   }

   public void setCnam(double var1) {
      this.cnam = cnam;
   }

   @Column(
      name = "netEnLettre"
   )
   public String getNetEnLettre() {
      return this.netEnLettre;
   }

   public void setNetEnLettre(String var1) {
      this.netEnLettre = netEnLettre;
   }

   @Column(
      name = "periodeLettre",
      length = 100
   )
   public String getPeriodeLettre() {
      return this.periodeLettre;
   }

   public void setPeriodeLettre(String var1) {
      this.periodeLettre = periodeLettre;
   }

   @Column(
      name = "banque",
      length = 50
   )
   public String getBanque() {
      return this.banque;
   }

   public void setBanque(String var1) {
      this.banque = banque;
   }

   @Column(
      name = "noCompteBanque",
      length = 50
   )
   public String getNoCompteBanque() {
      return this.noCompteBanque;
   }

   public void setNoCompteBanque(String var1) {
      this.noCompteBanque = noCompteBanque;
   }

   @Column(
      name = "modePaiement",
      length = 50
   )
   public String getModePaiement() {
      return this.modePaiement;
   }

   public void setModePaiement(String var1) {
      this.modePaiement = modePaiement;
   }

   @Column(
      name = "domicilie",
      nullable = false
   )
   public boolean isDomicilie() {
      return this.domicilie;
   }

   public void setDomicilie(boolean var1) {
      this.domicilie = domicilie;
   }

   @Column(
      name = "cumulBI",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getCumulBi() {
      return this.cumulBi;
   }

   public void setCumulBi(double var1) {
      this.cumulBi = cumulBi;
   }

   @Column(
      name = "cumulBNI",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getCumulBni() {
      return this.cumulBni;
   }

   public void setCumulBni(double var1) {
      this.cumulBni = cumulBni;
   }

   @Column(
      name = "cumulNJT",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getCumulNjt() {
      return this.cumulNjt;
   }

   public void setCumulNjt(double var1) {
      this.cumulNjt = cumulNjt;
   }

   @Column(
      name = "poste",
      length = 100
   )
   public String getPoste() {
      return this.poste;
   }

   public void setPoste(String var1) {
      this.poste = poste;
   }

   @Column(
      name = "departement",
      length = 100
   )
   public String getDepartement() {
      return this.departement;
   }

   public void setDepartement(String var1) {
      this.departement = departement;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateDernierDepart",
      length = 10
   )
   public Date getDateDernierDepart() {
      return this.dateDernierDepart;
   }

   public void setDateDernierDepart(Date var1) {
      this.dateDernierDepart = dateDernierDepart;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "paieDu",
      length = 10
   )
   public Date getPaieDu() {
      return this.paieDu;
   }

   public void setPaieDu(Date var1) {
      this.paieDu = paieDu;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "paieAu",
      length = 10
   )
   public Date getPaieAu() {
      return this.paieAu;
   }

   public void setPaieAu(Date var1) {
      this.paieAu = paieAu;
   }

   @Column(
      name = "biCNAM",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getBiCnam() {
      return this.biCnam;
   }

   public void setBiCnam(double var1) {
      this.biCnam = biCnam;
   }

   @Column(
      name = "biCNSS",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getBiCnss() {
      return this.biCnss;
   }

   public void setBiCnss(double var1) {
      this.biCnss = biCnss;
   }

   @Column(
      name = "RCNSS",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getRcnss() {
      return this.rcnss;
   }

   public void setRcnss(double var1) {
      this.rcnss = rcnss;
   }

   @Column(
      name = "RCNAM",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getRcnam() {
      return this.rcnam;
   }

   public void setRcnam(double var1) {
      this.rcnam = rcnam;
   }

   @Column(
      name = "biAVNAT",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getBiAvnat() {
      return this.biAvnat;
   }

   public void setBiAvnat(double var1) {
      this.biAvnat = biAvnat;
   }

   @Column(
      name = "notePaie"
   )
   public String getNotePaie() {
      return this.notePaie;
   }

   public void setNotePaie(String var1) {
      this.notePaie = notePaie;
   }

   @Column(
      name = "FTE",
      precision = 22,
      scale = 0
   )
   public Double getFte() {
      return this.fte;
   }

   public void setFte(Double var1) {
      this.fte = fte;
   }

   @Column(
      name = "statut"
   )
   public String getStatut() {
      return this.statut;
   }

   public void setStatut(String var1) {
      this.statut = statut;
   }

   @Column(
      name = "classification"
   )
   public String getClassification() {
      return this.classification;
   }

   public void setClassification(String var1) {
      this.classification = classification;
   }

   @Column(
      name = "activite",
      length = 100
   )
   public String getActivite() {
      return this.activite;
   }

   public void setActivite(String var1) {
      this.activite = activite;
   }

   @Column(
      name = "directiongeneral",
      length = 100
   )
   public String getDirectiongeneral() {
      return this.directiongeneral;
   }

   public void setDirectiongeneral(String var1) {
      this.directiongeneral = directiongeneral;
   }

   @Column(
      name = "direction",
      length = 100
   )
   public String getDirection() {
      return this.direction;
   }

   public void setDirection(String var1) {
      this.direction = direction;
   }
}
