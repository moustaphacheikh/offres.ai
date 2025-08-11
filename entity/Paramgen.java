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
   name = "paramgen",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Paramgen implements Serializable {
   @Id
   @Column(
      name = "id",
      unique = true,
      nullable = false
   )
   private int id;
   @Column(
      name = "devise",
      nullable = true,
      length = 50
   )
   private String devise;
   @Column(
      name = "nom",
      length = 300
   )
   private String nom;
   @Column(
      name = "nomEntreprise",
      nullable = false,
      length = 300
   )
   private String nomEntreprise;
   @Column(
      name = "activiteEntreprise",
      length = 500
   )
   private String activiteEntreprise;
   @Column(
      name = "telephone",
      length = 30
   )
   private String telephone;
   @Column(
      name = "fax",
      length = 30
   )
   private String fax;
   @Column(
      name = "adresse",
      length = 500
   )
   private String adresse;
   @Column(
      name = "bd",
      length = 10
   )
   private String bd;
   @Column(
      name = "siteweb",
      length = 50
   )
   private String siteweb;
   @Column(
      name = "email",
      length = 50
   )
   private String email;
   @Column(
      name = "noCnss",
      length = 10
   )
   private String noCnss;
   @Column(
      name = "noCnam",
      length = 10
   )
   private String noCnam;
   @Column(
      name = "noIts",
      length = 10
   )
   private String noIts;
   @Column(
      name = "usedITS",
      nullable = false
   )
   private int usedIts;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "periodeCourante",
      nullable = false,
      length = 10
   )
   private Date periodeCourante;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "periodeSuivante",
      nullable = false,
      length = 10
   )
   private Date periodeSuivante;
   @Column(
      name = "primePanierAuto"
   )
   private boolean primePanierAuto;
   @Column(
      name = "remboursementIts"
   )
   private boolean remboursementIts;
   @Column(
      name = "responsableEntreprise",
      length = 300
   )
   private String responsableEntreprise;
   @Column(
      name = "qualiteResponsable",
      length = 300
   )
   private String qualiteResponsable;
   @Column(
      name = "ancienneteAuto"
   )
   private boolean ancienneteAuto;
   @Column(
      name = "plafonIndNonImposable",
      nullable = false,
      precision = 22,
      scale = 0
   )
   private double plafonIndNonImposable;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "periodeCloture",
      nullable = false,
      length = 10
   )
   private Date periodeCloture;
   @Column(
      name = "smig",
      nullable = false,
      precision = 22,
      scale = 0
   )
   private Double smig;
   @Column(
      name = "villeSiege",
      length = 300
   )
   private String villeSiege;
   @Column(
      name = "signataires",
      length = 500
   )
   private String signataires;
   @Column(
      name = "indlogementAuto"
   )
   private boolean indlogementAuto;
   @Column(
      name = "deductionCnssdeIts"
   )
   private boolean deductionCnssdeIts;
   @Column(
      name = "deductionCnamdeIts"
   )
   private boolean deductionCnamdeIts;
   @Column(
      name = "njtDefault",
      nullable = false,
      precision = 22,
      scale = 0
   )
   private double njtDefault;
   @Column(
      name = "delaiAlerteFinContrat"
   )
   private Integer delaiAlerteFinContrat;
   @Temporal(TemporalType.TIMESTAMP)
   @Column(
      name = "dateMaj",
      nullable = false,
      length = 19
   )
   private Date dateMaj;
   @Column(
      name = "cheminatt2000",
      length = 500
   )
   private String cheminatt2000;
   @Column(
      name = "licenceKey",
      length = 500
   )
   private String licenceKey;
   @Column(
      name = "bankvirement",
      length = 300
   )
   private String bankvirement;
   @Column(
      name = "comptevirement",
      length = 300
   )
   private String comptevirement;
   @Column(
      name = "pub",
      length = 300
   )
   private String pub;
   @Column(
      name = "logo"
   )
   private byte[] logo;
   @Column(
      name = "nbSalaryCode",
      length = 100
   )
   private String nbSalaryCode;
   @Column(
      name = "abatement",
      nullable = false,
      precision = 22,
      scale = 0
   )
   private Double abatement;
   @Column(
      name = "ancienneteSpeciale"
   )
   private boolean ancienneteSpeciale;
   @Column(
      name = "quotaEcheanceRae",
      nullable = false,
      precision = 22,
      scale = 0
   )
   private Double quotaEcheanceRae;
   @Column(
      name = "retEngOnConge"
   )
   private boolean retEngOnConge;
   @Column(
      name = "mailSmtpHost",
      nullable = true,
      length = 100
   )
   private String mailSmtpHost;
   @Column(
      name = "mailUser",
      nullable = true,
      length = 100
   )
   private String mailUser;
   @Column(
      name = "mailPassword",
      nullable = true,
      length = 100
   )
   private String mailPassword;
   @Column(
      name = "mailSmtpPort",
      nullable = true,
      length = 100
   )
   private String mailSmtpPort;
   @Column(
      name = "mailSmtpTLSEnabled",
      nullable = true,
      length = 100
   )
   private boolean mailSmtpTLSEnabled;
   @Column(
      name = "noComptaNet",
      nullable = false
   )
   private Long noComptaNet;
   @Column(
      name = "noComptaChapitreNet"
   )
   private Long noComptaChapitreNet;
   @Column(
      name = "noComptaCleNet",
      length = 10
   )
   private String noComptaCleNet;
   @Column(
      name = "noComptaIts"
   )
   private Long noComptaIts;
   @Column(
      name = "noComptaChapitreIts"
   )
   private Long noComptaChapitreIts;
   @Column(
      name = "noComptaCleIts",
      length = 10
   )
   private String noComptaCleIts;
   @Column(
      name = "noComptaCnss"
   )
   private Long noComptaCnss;
   @Column(
      name = "noComptaChapitreCnss"
   )
   private Long noComptaChapitreCnss;
   @Column(
      name = "noComptaCleCnss",
      length = 10
   )
   private String noComptaCleCnss;
   @Column(
      name = "noComptaCnam"
   )
   private Long noComptaCnam;
   @Column(
      name = "noComptaChapitreCnam"
   )
   private Long noComptaChapitreCnam;
   @Column(
      name = "noComptaCleCnam",
      length = 10
   )
   private String noComptaCleCnam;
   @Column(
      name = "noComptaRits"
   )
   private Long noComptaRits;
   @Column(
      name = "noComptaChapitreRits"
   )
   private Long noComptaChapitreRits;
   @Column(
      name = "noComptaCleRits",
      length = 10
   )
   private String noComptaCleRits;
   @Column(
      name = "noComptaRcnss"
   )
   private Long noComptaRcnss;
   @Column(
      name = "noComptaChapitreRcnss"
   )
   private Long noComptaChapitreRcnss;
   @Column(
      name = "noComptaCleRcnss",
      length = 10
   )
   private String noComptaCleRcnss;
   @Column(
      name = "noComptaRcnam"
   )
   private Long noComptaRcnam;
   @Column(
      name = "noComptaChapitreRcnam"
   )
   private Long noComptaChapitreRcnam;
   @Column(
      name = "noComptaCleRcnam",
      length = 10
   )
   private String noComptaCleRcnam;
   @Column(
      name = "noComptaCnssMedCredit"
   )
   private Long noComptaCnssMedCredit;
   @Column(
      name = "noComptaChapitreCnssMedCredit"
   )
   private Long noComptaChapitreCnssMedCredit;
   @Column(
      name = "noComptaCleCnssMedCredit",
      length = 10
   )
   private String noComptaCleCnssMedCredit;
   @Column(
      name = "noComptaCnssMedDebit"
   )
   private Long noComptaCnssMedDebit;
   @Column(
      name = "noComptaChapitreCnssMedDebit"
   )
   private Long noComptaChapitreCnssMedDebit;
   @Column(
      name = "noComptaCleCnssMedDebit",
      length = 10
   )
   private String noComptaCleCnssMedDebit;
   @Column(
      name = "noComptaCnssPatCredit"
   )
   private Long noComptaCnssPatCredit;
   @Column(
      name = "noComptaChapitreCnssPatCredit"
   )
   private Long noComptaChapitreCnssPatCredit;
   @Column(
      name = "noComptaCleCnssPatCredit",
      length = 10
   )
   private String noComptaCleCnssPatCredit;
   @Column(
      name = "noComptaCnssPatDebit"
   )
   private Long noComptaCnssPatDebit;
   @Column(
      name = "noComptaChapitreCnssPatDebit"
   )
   private Long noComptaChapitreCnssPatDebit;
   @Column(
      name = "noComptaCleCnssPatDebit",
      length = 10
   )
   private String noComptaCleCnssPatDebit;
   @Column(
      name = "noComptaCnamPatCredit"
   )
   private Long noComptaCnamPatCredit;
   @Column(
      name = "noComptaChapitreCnamPatCredit"
   )
   private Long noComptaChapitreCnamPatCredit;
   @Column(
      name = "noComptaCleCnamPatCredit",
      length = 10
   )
   private String noComptaCleCnamPatCredit;
   @Column(
      name = "noComptaCnamPatDebit"
   )
   private Long noComptaCnamPatDebit;
   @Column(
      name = "noComptaChapitreCnamPatDebit"
   )
   private Long noComptaChapitreCnamPatDebit;
   @Column(
      name = "noComptaCleCnamPatDebit",
      length = 10
   )
   private String noComptaCleCnamPatDebit;
   @Column(
      name = "custumerActiveVersion",
      nullable = true,
      length = 10
   )
   private String custumerActiveVersion;
   @Column(
      name = "appIndCompensatrice",
      nullable = true
   )
   private Boolean appIndCompensatrice;
   @Column(
      name = "addCurrentSalInCumulCng",
      nullable = true
   )
   private Boolean addCurrentSalInCumulCng;
   @Column(
      name = "modeITS",
      nullable = true,
      length = 10
   )
   private String modeITS;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateInitLicence"
   )
   private Date dateInitLicence;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateCurentLicence"
   )
   private Date dateCurentLicence;
   @Column(
      name = "licencePeriodicity"
   )
   private String licencePeriodicity;

   public Paramgen() {
   }

   public int getId() {
      return this.id;
   }

   public void setId(int var1) {
      this.id = id;
   }

   public String getDevise() {
      return this.devise;
   }

   public void setDevise(String var1) {
      this.devise = devise;
   }

   public String getNom() {
      return this.nom;
   }

   public void setNom(String var1) {
      this.nom = nom;
   }

   public String getNomEntreprise() {
      return this.nomEntreprise;
   }

   public void setNomEntreprise(String var1) {
      this.nomEntreprise = nomEntreprise;
   }

   public String getActiviteEntreprise() {
      return this.activiteEntreprise;
   }

   public void setActiviteEntreprise(String var1) {
      this.activiteEntreprise = activiteEntreprise;
   }

   public String getTelephone() {
      return this.telephone;
   }

   public void setTelephone(String var1) {
      this.telephone = telephone;
   }

   public String getFax() {
      return this.fax;
   }

   public void setFax(String var1) {
      this.fax = fax;
   }

   public String getAdresse() {
      return this.adresse;
   }

   public void setAdresse(String var1) {
      this.adresse = adresse;
   }

   public String getBd() {
      return this.bd;
   }

   public void setBd(String var1) {
      this.bd = bd;
   }

   public String getSiteweb() {
      return this.siteweb;
   }

   public void setSiteweb(String var1) {
      this.siteweb = siteweb;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String var1) {
      this.email = email;
   }

   public String getNoCnss() {
      return this.noCnss;
   }

   public void setNoCnss(String var1) {
      this.noCnss = noCnss;
   }

   public String getNoCnam() {
      return this.noCnam;
   }

   public void setNoCnam(String var1) {
      this.noCnam = noCnam;
   }

   public String getNoIts() {
      return this.noIts;
   }

   public void setNoIts(String var1) {
      this.noIts = noIts;
   }

   public int getUsedIts() {
      return this.usedIts;
   }

   public void setUsedIts(int var1) {
      this.usedIts = usedIts;
   }

   public Date getPeriodeCourante() {
      return this.periodeCourante;
   }

   public void setPeriodeCourante(Date var1) {
      this.periodeCourante = periodeCourante;
   }

   public Date getPeriodeSuivante() {
      return this.periodeSuivante;
   }

   public void setPeriodeSuivante(Date var1) {
      this.periodeSuivante = periodeSuivante;
   }

   public boolean isPrimePanierAuto() {
      return this.primePanierAuto;
   }

   public void setPrimePanierAuto(boolean var1) {
      this.primePanierAuto = primePanierAuto;
   }

   public boolean isRemboursementIts() {
      return this.remboursementIts;
   }

   public void setRemboursementIts(boolean var1) {
      this.remboursementIts = remboursementIts;
   }

   public String getResponsableEntreprise() {
      return this.responsableEntreprise;
   }

   public void setResponsableEntreprise(String var1) {
      this.responsableEntreprise = responsableEntreprise;
   }

   public String getQualiteResponsable() {
      return this.qualiteResponsable;
   }

   public void setQualiteResponsable(String var1) {
      this.qualiteResponsable = qualiteResponsable;
   }

   public boolean isAncienneteAuto() {
      return this.ancienneteAuto;
   }

   public void setAncienneteAuto(boolean var1) {
      this.ancienneteAuto = ancienneteAuto;
   }

   public double getPlafonIndNonImposable() {
      return this.plafonIndNonImposable;
   }

   public void setPlafonIndNonImposable(double var1) {
      this.plafonIndNonImposable = plafonIndNonImposable;
   }

   public Date getPeriodeCloture() {
      return this.periodeCloture;
   }

   public void setPeriodeCloture(Date var1) {
      this.periodeCloture = periodeCloture;
   }

   public Double getSmig() {
      return this.smig;
   }

   public void setSmig(Double var1) {
      this.smig = smig;
   }

   public String getVilleSiege() {
      return this.villeSiege;
   }

   public void setVilleSiege(String var1) {
      this.villeSiege = villeSiege;
   }

   public String getSignataires() {
      return this.signataires;
   }

   public void setSignataires(String var1) {
      this.signataires = signataires;
   }

   public boolean isIndlogementAuto() {
      return this.indlogementAuto;
   }

   public void setIndlogementAuto(boolean var1) {
      this.indlogementAuto = indlogementAuto;
   }

   public boolean isDeductionCnssdeIts() {
      return this.deductionCnssdeIts;
   }

   public void setDeductionCnssdeIts(boolean var1) {
      this.deductionCnssdeIts = deductionCnssdeIts;
   }

   public boolean isDeductionCnamdeIts() {
      return this.deductionCnamdeIts;
   }

   public void setDeductionCnamdeIts(boolean var1) {
      this.deductionCnamdeIts = deductionCnamdeIts;
   }

   public double getNjtDefault() {
      return this.njtDefault;
   }

   public void setNjtDefault(double var1) {
      this.njtDefault = njtDefault;
   }

   public Integer getDelaiAlerteFinContrat() {
      return this.delaiAlerteFinContrat;
   }

   public void setDelaiAlerteFinContrat(Integer var1) {
      this.delaiAlerteFinContrat = delaiAlerteFinContrat;
   }

   public Date getDateMaj() {
      return this.dateMaj;
   }

   public void setDateMaj(Date var1) {
      this.dateMaj = dateMaj;
   }

   public String getCheminatt2000() {
      return this.cheminatt2000;
   }

   public void setCheminatt2000(String var1) {
      this.cheminatt2000 = cheminatt2000;
   }

   public String getLicenceKey() {
      return this.licenceKey;
   }

   public void setLicenceKey(String var1) {
      this.licenceKey = licenceKey;
   }

   public String getBankvirement() {
      return this.bankvirement;
   }

   public void setBankvirement(String var1) {
      this.bankvirement = bankvirement;
   }

   public String getComptevirement() {
      return this.comptevirement;
   }

   public void setComptevirement(String var1) {
      this.comptevirement = comptevirement;
   }

   public String getPub() {
      return this.pub;
   }

   public void setPub(String var1) {
      this.pub = pub;
   }

   public byte[] getLogo() {
      return this.logo;
   }

   public void setLogo(byte[] var1) {
      this.logo = logo;
   }

   public String getNbSalaryCode() {
      return this.nbSalaryCode;
   }

   public void setNbSalaryCode(String var1) {
      this.nbSalaryCode = nbSalaryCode;
   }

   public Double getAbatement() {
      return this.abatement;
   }

   public void setAbatement(Double var1) {
      this.abatement = abatement;
   }

   public boolean isAncienneteSpeciale() {
      return this.ancienneteSpeciale;
   }

   public void setAncienneteSpeciale(boolean var1) {
      this.ancienneteSpeciale = ancienneteSpeciale;
   }

   public Double getQuotaEcheanceRae() {
      return this.quotaEcheanceRae;
   }

   public void setQuotaEcheanceRae(Double var1) {
      this.quotaEcheanceRae = quotaEcheanceRae;
   }

   public boolean isRetEngOnConge() {
      return this.retEngOnConge;
   }

   public void setRetEngOnConge(boolean var1) {
      this.retEngOnConge = retEngOnConge;
   }

   public String getMailSmtpHost() {
      return this.mailSmtpHost;
   }

   public void setMailSmtpHost(String var1) {
      this.mailSmtpHost = mailSmtpHost;
   }

   public String getMailUser() {
      return this.mailUser;
   }

   public void setMailUser(String var1) {
      this.mailUser = mailUser;
   }

   public String getMailPassword() {
      return this.mailPassword;
   }

   public void setMailPassword(String var1) {
      this.mailPassword = mailPassword;
   }

   public String getMailSmtpPort() {
      return this.mailSmtpPort;
   }

   public void setMailSmtpPort(String var1) {
      this.mailSmtpPort = mailSmtpPort;
   }

   public boolean isMailSmtpTLSEnabled() {
      return this.mailSmtpTLSEnabled;
   }

   public void setMailSmtpTLSEnabled(boolean var1) {
      this.mailSmtpTLSEnabled = mailSmtpTLSEnabled;
   }

   public Long getNoComptaNet() {
      return this.noComptaNet;
   }

   public void setNoComptaNet(Long var1) {
      this.noComptaNet = noComptaNet;
   }

   public Long getNoComptaChapitreNet() {
      return this.noComptaChapitreNet;
   }

   public void setNoComptaChapitreNet(Long var1) {
      this.noComptaChapitreNet = noComptaChapitreNet;
   }

   public String getNoComptaCleNet() {
      return this.noComptaCleNet;
   }

   public void setNoComptaCleNet(String var1) {
      this.noComptaCleNet = noComptaCleNet;
   }

   public Long getNoComptaIts() {
      return this.noComptaIts;
   }

   public void setNoComptaIts(Long var1) {
      this.noComptaIts = noComptaIts;
   }

   public Long getNoComptaChapitreIts() {
      return this.noComptaChapitreIts;
   }

   public void setNoComptaChapitreIts(Long var1) {
      this.noComptaChapitreIts = noComptaChapitreIts;
   }

   public String getNoComptaCleIts() {
      return this.noComptaCleIts;
   }

   public void setNoComptaCleIts(String var1) {
      this.noComptaCleIts = noComptaCleIts;
   }

   public Long getNoComptaCnss() {
      return this.noComptaCnss;
   }

   public void setNoComptaCnss(Long var1) {
      this.noComptaCnss = noComptaCnss;
   }

   public Long getNoComptaChapitreCnss() {
      return this.noComptaChapitreCnss;
   }

   public void setNoComptaChapitreCnss(Long var1) {
      this.noComptaChapitreCnss = noComptaChapitreCnss;
   }

   public String getNoComptaCleCnss() {
      return this.noComptaCleCnss;
   }

   public void setNoComptaCleCnss(String var1) {
      this.noComptaCleCnss = noComptaCleCnss;
   }

   public Long getNoComptaCnam() {
      return this.noComptaCnam;
   }

   public void setNoComptaCnam(Long var1) {
      this.noComptaCnam = noComptaCnam;
   }

   public Long getNoComptaChapitreCnam() {
      return this.noComptaChapitreCnam;
   }

   public void setNoComptaChapitreCnam(Long var1) {
      this.noComptaChapitreCnam = noComptaChapitreCnam;
   }

   public String getNoComptaCleCnam() {
      return this.noComptaCleCnam;
   }

   public void setNoComptaCleCnam(String var1) {
      this.noComptaCleCnam = noComptaCleCnam;
   }

   public Long getNoComptaRits() {
      return this.noComptaRits;
   }

   public void setNoComptaRits(Long var1) {
      this.noComptaRits = noComptaRits;
   }

   public Long getNoComptaChapitreRits() {
      return this.noComptaChapitreRits;
   }

   public void setNoComptaChapitreRits(Long var1) {
      this.noComptaChapitreRits = noComptaChapitreRits;
   }

   public String getNoComptaCleRits() {
      return this.noComptaCleRits;
   }

   public void setNoComptaCleRits(String var1) {
      this.noComptaCleRits = noComptaCleRits;
   }

   public Long getNoComptaRcnss() {
      return this.noComptaRcnss;
   }

   public void setNoComptaRcnss(Long var1) {
      this.noComptaRcnss = noComptaRcnss;
   }

   public Long getNoComptaChapitreRcnss() {
      return this.noComptaChapitreRcnss;
   }

   public void setNoComptaChapitreRcnss(Long var1) {
      this.noComptaChapitreRcnss = noComptaChapitreRcnss;
   }

   public String getNoComptaCleRcnss() {
      return this.noComptaCleRcnss;
   }

   public void setNoComptaCleRcnss(String var1) {
      this.noComptaCleRcnss = noComptaCleRcnss;
   }

   public Long getNoComptaRcnam() {
      return this.noComptaRcnam;
   }

   public void setNoComptaRcnam(Long var1) {
      this.noComptaRcnam = noComptaRcnam;
   }

   public Long getNoComptaChapitreRcnam() {
      return this.noComptaChapitreRcnam;
   }

   public void setNoComptaChapitreRcnam(Long var1) {
      this.noComptaChapitreRcnam = noComptaChapitreRcnam;
   }

   public String getNoComptaCleRcnam() {
      return this.noComptaCleRcnam;
   }

   public void setNoComptaCleRcnam(String var1) {
      this.noComptaCleRcnam = noComptaCleRcnam;
   }

   public Long getNoComptaCnssMedCredit() {
      return this.noComptaCnssMedCredit;
   }

   public void setNoComptaCnssMedCredit(Long var1) {
      this.noComptaCnssMedCredit = noComptaCnssMedCredit;
   }

   public Long getNoComptaChapitreCnssMedCredit() {
      return this.noComptaChapitreCnssMedCredit;
   }

   public void setNoComptaChapitreCnssMedCredit(Long var1) {
      this.noComptaChapitreCnssMedCredit = noComptaChapitreCnssMedCredit;
   }

   public String getNoComptaCleCnssMedCredit() {
      return this.noComptaCleCnssMedCredit;
   }

   public void setNoComptaCleCnssMedCredit(String var1) {
      this.noComptaCleCnssMedCredit = noComptaCleCnssMedCredit;
   }

   public Long getNoComptaCnssMedDebit() {
      return this.noComptaCnssMedDebit;
   }

   public void setNoComptaCnssMedDebit(Long var1) {
      this.noComptaCnssMedDebit = noComptaCnssMedDebit;
   }

   public Long getNoComptaChapitreCnssMedDebit() {
      return this.noComptaChapitreCnssMedDebit;
   }

   public void setNoComptaChapitreCnssMedDebit(Long var1) {
      this.noComptaChapitreCnssMedDebit = noComptaChapitreCnssMedDebit;
   }

   public String getNoComptaCleCnssMedDebit() {
      return this.noComptaCleCnssMedDebit;
   }

   public void setNoComptaCleCnssMedDebit(String var1) {
      this.noComptaCleCnssMedDebit = noComptaCleCnssMedDebit;
   }

   public Long getNoComptaCnssPatCredit() {
      return this.noComptaCnssPatCredit;
   }

   public void setNoComptaCnssPatCredit(Long var1) {
      this.noComptaCnssPatCredit = noComptaCnssPatCredit;
   }

   public Long getNoComptaChapitreCnssPatCredit() {
      return this.noComptaChapitreCnssPatCredit;
   }

   public void setNoComptaChapitreCnssPatCredit(Long var1) {
      this.noComptaChapitreCnssPatCredit = noComptaChapitreCnssPatCredit;
   }

   public String getNoComptaCleCnssPatCredit() {
      return this.noComptaCleCnssPatCredit;
   }

   public void setNoComptaCleCnssPatCredit(String var1) {
      this.noComptaCleCnssPatCredit = noComptaCleCnssPatCredit;
   }

   public Long getNoComptaCnssPatDebit() {
      return this.noComptaCnssPatDebit;
   }

   public void setNoComptaCnssPatDebit(Long var1) {
      this.noComptaCnssPatDebit = noComptaCnssPatDebit;
   }

   public Long getNoComptaChapitreCnssPatDebit() {
      return this.noComptaChapitreCnssPatDebit;
   }

   public void setNoComptaChapitreCnssPatDebit(Long var1) {
      this.noComptaChapitreCnssPatDebit = noComptaChapitreCnssPatDebit;
   }

   public String getNoComptaCleCnssPatDebit() {
      return this.noComptaCleCnssPatDebit;
   }

   public void setNoComptaCleCnssPatDebit(String var1) {
      this.noComptaCleCnssPatDebit = noComptaCleCnssPatDebit;
   }

   public Long getNoComptaCnamPatCredit() {
      return this.noComptaCnamPatCredit;
   }

   public void setNoComptaCnamPatCredit(Long var1) {
      this.noComptaCnamPatCredit = noComptaCnamPatCredit;
   }

   public Long getNoComptaChapitreCnamPatCredit() {
      return this.noComptaChapitreCnamPatCredit;
   }

   public void setNoComptaChapitreCnamPatCredit(Long var1) {
      this.noComptaChapitreCnamPatCredit = noComptaChapitreCnamPatCredit;
   }

   public String getNoComptaCleCnamPatCredit() {
      return this.noComptaCleCnamPatCredit;
   }

   public void setNoComptaCleCnamPatCredit(String var1) {
      this.noComptaCleCnamPatCredit = noComptaCleCnamPatCredit;
   }

   public Long getNoComptaCnamPatDebit() {
      return this.noComptaCnamPatDebit;
   }

   public void setNoComptaCnamPatDebit(Long var1) {
      this.noComptaCnamPatDebit = noComptaCnamPatDebit;
   }

   public Long getNoComptaChapitreCnamPatDebit() {
      return this.noComptaChapitreCnamPatDebit;
   }

   public void setNoComptaChapitreCnamPatDebit(Long var1) {
      this.noComptaChapitreCnamPatDebit = noComptaChapitreCnamPatDebit;
   }

   public String getNoComptaCleCnamPatDebit() {
      return this.noComptaCleCnamPatDebit;
   }

   public void setNoComptaCleCnamPatDebit(String var1) {
      this.noComptaCleCnamPatDebit = noComptaCleCnamPatDebit;
   }

   public boolean isAppIndCompensatrice() {
      return this.appIndCompensatrice;
   }

   public void setAppIndCompensatrice(boolean var1) {
      this.appIndCompensatrice = appIndCompensatrice;
   }

   public boolean isAddCurrentSalInCumulCng() {
      return this.addCurrentSalInCumulCng;
   }

   public void setAddCurrentSalInCumulCng(boolean var1) {
      this.addCurrentSalInCumulCng = addCurrentSalInCumulCng;
   }

   public String getCustumerActiveVersion() {
      return this.custumerActiveVersion;
   }

   public void setCustumerActiveVersion(String var1) {
      this.custumerActiveVersion = custumerActiveVersion;
   }

   public String getModeITS() {
      return this.modeITS;
   }

   public void setModeITS(String var1) {
      this.modeITS = modeITS;
   }

   public Date getDateInitLicence() {
      return this.dateInitLicence;
   }

   public void setDateInitLicence(Date var1) {
      this.dateInitLicence = dateInitLicence;
   }

   public Date getDateCurentLicence() {
      return this.dateCurentLicence;
   }

   public void setDateCurentLicence(Date var1) {
      this.dateCurentLicence = dateCurentLicence;
   }

   public String getLicencePeriodicity() {
      return this.licencePeriodicity;
   }

   public void setLicencePeriodicity(String var1) {
      this.licencePeriodicity = licencePeriodicity;
   }
}
