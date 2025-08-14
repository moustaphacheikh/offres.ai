package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(
   name = "employe",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Employe implements Serializable {
   @Id
   @Column(
      name = "id",
      unique = true,
      nullable = false
   )
   private int id;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "fonction"
   )
   private Poste poste;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "departement"
   )
   private Departement departement;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "directiongeneral"
   )
   private Directiongeneral directiongeneral;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "direction"
   )
   private Direction direction;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "categorie"
   )
   private Grillesalairebase grillesalairebase;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "activite"
   )
   private Activite activite;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "origine"
   )
   private Origines origines;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "banque"
   )
   private Banque banque;
   @Column(
      name = "nom",
      length = 100
   )
   private String nom;
   @Column(
      name = "pere",
      length = 200
   )
   private String pere;
   @Column(
      name = "mere",
      length = 200
   )
   private String mere;
   @Column(
      name = "nni",
      length = 50
   )
   private String nni;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateNaissance",
      length = 10
   )
   private Date dateNaissance;
   @Column(
      name = "lieuNaissance",
      length = 200
   )
   private String lieuNaissance;
   @Column(
      name = "nationalite",
      length = 100
   )
   private String nationalite;
   @Column(
      name = "sexe",
      length = 30
   )
   private String sexe;
   @Column(
      name = "situationFamiliale",
      length = 30
   )
   private String situationFamiliale;
   @Column(
      name = "nbEnfants"
   )
   private Integer nbEnfants;
   @Column(
      name = "noCnss",
      length = 50
   )
   private String noCnss;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateCnss",
      length = 10
   )
   private Date dateCnss;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateEmbauche",
      length = 10
   )
   private Date dateEmbauche;
   @Column(
      name = "noCompteBanque",
      length = 50
   )
   private String noCompteBanque;
   @Column(
      name = "modePaiement",
      length = 50
   )
   private String modePaiement;
   @Column(
      name = "detacheCNSS",
      nullable = true
   )
   private boolean detacheCnss;
   @Column(
      name = "detacheCnam",
      nullable = true
   )
   private boolean detacheCnam;
   @Column(
      name = "noCnam",
      length = 50
   )
   private String noCnam;
   @Column(
      name = "domicilie",
      nullable = true
   )
   private boolean domicilie;
   @Column(
      name = "exonoreIts",
      nullable = true
   )
   private boolean exonoreIts;
   @Column(
      name = "cumulNJTInitial",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double cumulNjtinitial;
   @Column(
      name = "cumulBrutImposableInitial",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double cumulBrutImposableInitial;
   @Column(
      name = "cumulBrutNonImposableInitial",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double cumulBrutNonImposableInitial;
   @Column(
      name = "cumul12dminitial",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double cumul12dminitial;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dernierDepartInitial",
      length = 10
   )
   private Date dernierDepartInitial;
   @Column(
      name = "nbMoisPreavis",
      nullable = true,
      precision = 12,
      scale = 0
   )
   private float nbMoisPreavis;
   @Column(
      name = "tauxPsra",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double tauxPsra;
   @Column(
      name = "contratHeureSemaine",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double contratHeureSemaine;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateDebauche",
      length = 10
   )
   private Date dateDebauche;
   @Column(
      name = "raisonDebauche",
      length = 500
   )
   private String raisonDebauche;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateAnciennete",
      length = 10
   )
   private Date dateAnciennete;
   @Column(
      name = "actif",
      nullable = true
   )
   private boolean actif;
   @Column(
      name = "tauxAnciennete",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double tauxAnciennete;
   @Column(
      name = "enConge",
      nullable = true
   )
   private boolean enConge;
   @Column(
      name = "tauxRemborssementCnss",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double tauxRemborssementCnss;
   @Column(
      name = "tauxRemborssementCnam",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double tauxRemborssementCnam;
   @Column(
      name = "tauxRembItstranche1",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double tauxRembItstranche1;
   @Column(
      name = "tauxRembItstranche2",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double tauxRembItstranche2;
   @Column(
      name = "tauxRembItstranche3",
      nullable = true,
      precision = 22,
      scale = 0
   )
   private double tauxRembItstranche3;
   @Column(
      name = "idSalariePointeuse"
   )
   private Integer idSalariePointeuse;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateCategorie",
      length = 10
   )
   private Date dateCategorie;
   @Column(
      name = "nbAnnnesCategorie"
   )
   private Integer nbAnnnesCategorie;
   @Column(
      name = "avancementCategorieAuto",
      nullable = true
   )
   private boolean avancementCategorieAuto;
   @Column(
      name = "enDebauche",
      nullable = true
   )
   private boolean enDebauche;
   @Column(
      name = "structureOrigine",
      length = 200
   )
   private String structureOrigine;
   @Column(
      name = "lieuTravail",
      length = 200
   )
   private String lieuTravail;
   @Column(
      name = "typeContrat",
      length = 50
   )
   private String typeContrat;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateFinContrat",
      length = 10
   )
   private Date dateFinContrat;
   @Column(
      name = "noteSurBulletin",
      length = 600
   )
   private String noteSurBulletin;
   @Column(
      name = "noPassport",
      length = 100
   )
   private String noPassport;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateLivraisonPassport",
      length = 10
   )
   private Date dateLivraisonPassport;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateExpirationPassport",
      length = 10
   )
   private Date dateExpirationPassport;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateDebutVisa",
      length = 10
   )
   private Date dateDebutVisa;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateFinVisa",
      length = 10
   )
   private Date dateFinVisa;
   @Column(
      name = "noCarteSejour",
      length = 100
   )
   private String noCarteSejour;
   @Column(
      name = "noPermiTravail",
      length = 100
   )
   private String noPermiTravail;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateLivraisonPermiTravail",
      length = 10
   )
   private Date dateLivraisonPermiTravail;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateExpirationPermiTravail",
      length = 10
   )
   private Date dateExpirationPermiTravail;
   @Column(
      name = "telephone",
      length = 200
   )
   private String telephone;
   @Column(
      name = "email",
      length = 200
   )
   private String email;
   @Column(
      name = "adresse",
      length = 500
   )
   private String adresse;
   @Column(
      name = "idPsservice",
      length = 50
   )
   private String idPsservice;
   @Column(
      name = "password",
      length = 50
   )
   private String password;
   @Column(
      name = "psservice",
      nullable = false
   )
   private boolean psservice;
   @Column(
      name = "expatrie",
      nullable = false
   )
   private boolean expatrie;
   @Column(
      name = "prenom",
      length = 100
   )
   private String prenom;
   @Column(
      name = "statut",
      length = 50
   )
   private String statut;
   @Column(
      name = "classification",
      length = 200
   )
   private String classification;
   @Column(
      name = "budgetannuel",
      precision = 22,
      scale = 0,
      nullable = false
   )
   private Double budgetannuel;
   @Column(
      name = "lundi_DS",
      nullable = false
   )
   private boolean lundiDs;
   @Column(
      name = "lundi_Fs",
      nullable = false
   )
   private boolean lundiFs;
   @Column(
      name = "lundi_We",
      nullable = false
   )
   private boolean lundiWe;
   @Column(
      name = "mardi_Ds",
      nullable = false
   )
   private boolean mardiDs;
   @Column(
      name = "mardi_Fs",
      nullable = false
   )
   private boolean mardiFs;
   @Column(
      name = "mardi_We",
      nullable = false
   )
   private boolean mardiWe;
   @Column(
      name = "mercredi_Ds",
      nullable = false
   )
   private boolean mercrediDs;
   @Column(
      name = "mercredi_Fs",
      nullable = false
   )
   private boolean mercrediFs;
   @Column(
      name = "mercredi_We",
      nullable = false
   )
   private boolean mercrediWe;
   @Column(
      name = "jeudi_Ds",
      nullable = false
   )
   private boolean jeudiDs;
   @Column(
      name = "jeudi_Fs",
      nullable = false
   )
   private boolean jeudiFs;
   @Column(
      name = "jeudi_We",
      nullable = false
   )
   private boolean jeudiWe;
   @Column(
      name = "vendredi_Ds",
      nullable = false
   )
   private boolean vendrediDs;
   @Column(
      name = "vendredi_Fs",
      nullable = false
   )
   private boolean vendrediFs;
   @Column(
      name = "vendredi_We",
      nullable = false
   )
   private boolean vendrediWe;
   @Column(
      name = "samedi_Ds",
      nullable = false
   )
   private boolean samediDs;
   @Column(
      name = "samedi_Fs",
      nullable = false
   )
   private boolean samediFs;
   @Column(
      name = "samedi_We",
      nullable = false
   )
   private boolean samediWe;
   @Column(
      name = "dimanche_Ds",
      nullable = false
   )
   private boolean dimancheDs;
   @Column(
      name = "dimanche_Fs",
      nullable = false
   )
   private boolean dimancheFs;
   @Column(
      name = "dimanche_We",
      nullable = false
   )
   private boolean dimancheWe;
   @Column(
      name = "photo"
   )
   private byte[] photo;
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "employe"
   )
   private Set<Retenuesaecheances> retenuesaecheanceses = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "employe"
   )
   private Set<Paie> paies = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "employe"
   )
   private Set<Weekot> weekots = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "employe"
   )
   private Set<Rubriquepaie> rubriquepaies = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "employe"
   )
   private Set<Conges> congeses = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "employe"
   )
   private Set<Njtsalarie> njtsalaries = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "employe"
   )
   private Set<Jour> jours = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "employe"
   )
   private Set<Enfants> enfantss = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "employe"
   )
   private Set<Diplome> diplomes = new HashSet(0);
   @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "employe"
   )
   private Set<Document> documents = new HashSet(0);

   public Employe() {
   }

   public int getId() {
      return this.id;
   }

   public void setId(int var1) {
      this.id = id;
   }

   public Poste getPoste() {
      return this.poste;
   }

   public void setPoste(Poste var1) {
      this.poste = poste;
   }

   public Departement getDepartement() {
      return this.departement;
   }

   public void setDepartement(Departement var1) {
      this.departement = departement;
   }

   public Directiongeneral getDirectiongeneral() {
      return this.directiongeneral;
   }

   public void setDirectiongeneral(Directiongeneral var1) {
      this.directiongeneral = directiongeneral;
   }

   public Direction getDirection() {
      return this.direction;
   }

   public void setDirection(Direction var1) {
      this.direction = direction;
   }

   public Grillesalairebase getGrillesalairebase() {
      return this.grillesalairebase;
   }

   public void setGrillesalairebase(Grillesalairebase var1) {
      this.grillesalairebase = grillesalairebase;
   }

   public Activite getActivite() {
      return this.activite;
   }

   public void setActivite(Activite var1) {
      this.activite = activite;
   }

   public Origines getOrigines() {
      return this.origines;
   }

   public void setOrigines(Origines var1) {
      this.origines = origines;
   }

   public Banque getBanque() {
      return this.banque;
   }

   public void setBanque(Banque var1) {
      this.banque = banque;
   }

   public String getNom() {
      return this.nom;
   }

   public void setNom(String var1) {
      this.nom = nom;
   }

   public String getPere() {
      return this.pere;
   }

   public void setPere(String var1) {
      this.pere = pere;
   }

   public String getMere() {
      return this.mere;
   }

   public void setMere(String var1) {
      this.mere = mere;
   }

   public String getNni() {
      return this.nni;
   }

   public void setNni(String var1) {
      this.nni = nni;
   }

   public Date getDateNaissance() {
      return this.dateNaissance;
   }

   public void setDateNaissance(Date var1) {
      this.dateNaissance = dateNaissance;
   }

   public String getLieuNaissance() {
      return this.lieuNaissance;
   }

   public void setLieuNaissance(String var1) {
      this.lieuNaissance = lieuNaissance;
   }

   public String getNationalite() {
      return this.nationalite;
   }

   public void setNationalite(String var1) {
      this.nationalite = nationalite;
   }

   public String getSexe() {
      return this.sexe;
   }

   public void setSexe(String var1) {
      this.sexe = sexe;
   }

   public String getSituationFamiliale() {
      return this.situationFamiliale;
   }

   public void setSituationFamiliale(String var1) {
      this.situationFamiliale = situationFamiliale;
   }

   public Integer getNbEnfants() {
      return this.nbEnfants;
   }

   public void setNbEnfants(Integer var1) {
      this.nbEnfants = nbEnfants;
   }

   public String getNoCnss() {
      return this.noCnss;
   }

   public void setNoCnss(String var1) {
      this.noCnss = noCnss;
   }

   public Date getDateCnss() {
      return this.dateCnss;
   }

   public void setDateCnss(Date var1) {
      this.dateCnss = dateCnss;
   }

   public Date getDateEmbauche() {
      return this.dateEmbauche;
   }

   public void setDateEmbauche(Date var1) {
      this.dateEmbauche = dateEmbauche;
   }

   public String getNoCompteBanque() {
      return this.noCompteBanque;
   }

   public void setNoCompteBanque(String var1) {
      this.noCompteBanque = noCompteBanque;
   }

   public String getModePaiement() {
      return this.modePaiement;
   }

   public void setModePaiement(String var1) {
      this.modePaiement = modePaiement;
   }

   public boolean isDetacheCnss() {
      return this.detacheCnss;
   }

   public void setDetacheCnss(boolean var1) {
      this.detacheCnss = detacheCnss;
   }

   public boolean isDetacheCnam() {
      return this.detacheCnam;
   }

   public void setDetacheCnam(boolean var1) {
      this.detacheCnam = detacheCnam;
   }

   public String getNoCnam() {
      return this.noCnam;
   }

   public void setNoCnam(String var1) {
      this.noCnam = noCnam;
   }

   public boolean isDomicilie() {
      return this.domicilie;
   }

   public void setDomicilie(boolean var1) {
      this.domicilie = domicilie;
   }

   public boolean isExonoreIts() {
      return this.exonoreIts;
   }

   public void setExonoreIts(boolean var1) {
      this.exonoreIts = exonoreIts;
   }

   public double getCumulNjtinitial() {
      return this.cumulNjtinitial;
   }

   public void setCumulNjtinitial(double var1) {
      this.cumulNjtinitial = cumulNjtinitial;
   }

   public double getCumulBrutImposableInitial() {
      return this.cumulBrutImposableInitial;
   }

   public void setCumulBrutImposableInitial(double var1) {
      this.cumulBrutImposableInitial = cumulBrutImposableInitial;
   }

   public double getCumulBrutNonImposableInitial() {
      return this.cumulBrutNonImposableInitial;
   }

   public void setCumulBrutNonImposableInitial(double var1) {
      this.cumulBrutNonImposableInitial = cumulBrutNonImposableInitial;
   }

   public double getCumul12dminitial() {
      return this.cumul12dminitial;
   }

   public void setCumul12dminitial(double var1) {
      this.cumul12dminitial = cumul12dminitial;
   }

   public Date getDernierDepartInitial() {
      return this.dernierDepartInitial;
   }

   public void setDernierDepartInitial(Date var1) {
      this.dernierDepartInitial = dernierDepartInitial;
   }

   public float getNbMoisPreavis() {
      return this.nbMoisPreavis;
   }

   public void setNbMoisPreavis(float var1) {
      this.nbMoisPreavis = nbMoisPreavis;
   }

   public double getTauxPsra() {
      return this.tauxPsra;
   }

   public void setTauxPsra(double var1) {
      this.tauxPsra = tauxPsra;
   }

   public double getContratHeureSemaine() {
      return this.contratHeureSemaine;
   }

   public void setContratHeureSemaine(double var1) {
      this.contratHeureSemaine = contratHeureSemaine;
   }

   public Date getDateDebauche() {
      return this.dateDebauche;
   }

   public void setDateDebauche(Date var1) {
      this.dateDebauche = dateDebauche;
   }

   public String getRaisonDebauche() {
      return this.raisonDebauche;
   }

   public void setRaisonDebauche(String var1) {
      this.raisonDebauche = raisonDebauche;
   }

   public Date getDateAnciennete() {
      return this.dateAnciennete;
   }

   public void setDateAnciennete(Date var1) {
      this.dateAnciennete = dateAnciennete;
   }

   public boolean isActif() {
      return this.actif;
   }

   public void setActif(boolean var1) {
      this.actif = actif;
   }

   public double getTauxAnciennete() {
      return this.tauxAnciennete;
   }

   public void setTauxAnciennete(double var1) {
      this.tauxAnciennete = tauxAnciennete;
   }

   public boolean isEnConge() {
      return this.enConge;
   }

   public void setEnConge(boolean var1) {
      this.enConge = enConge;
   }

   public double getTauxRemborssementCnss() {
      return this.tauxRemborssementCnss;
   }

   public void setTauxRemborssementCnss(double var1) {
      this.tauxRemborssementCnss = tauxRemborssementCnss;
   }

   public double getTauxRemborssementCnam() {
      return this.tauxRemborssementCnam;
   }

   public void setTauxRemborssementCnam(double var1) {
      this.tauxRemborssementCnam = tauxRemborssementCnam;
   }

   public double getTauxRembItstranche1() {
      return this.tauxRembItstranche1;
   }

   public void setTauxRembItstranche1(double var1) {
      this.tauxRembItstranche1 = tauxRembItstranche1;
   }

   public double getTauxRembItstranche2() {
      return this.tauxRembItstranche2;
   }

   public void setTauxRembItstranche2(double var1) {
      this.tauxRembItstranche2 = tauxRembItstranche2;
   }

   public double getTauxRembItstranche3() {
      return this.tauxRembItstranche3;
   }

   public void setTauxRembItstranche3(double var1) {
      this.tauxRembItstranche3 = tauxRembItstranche3;
   }

   public Integer getIdSalariePointeuse() {
      return this.idSalariePointeuse;
   }

   public void setIdSalariePointeuse(Integer var1) {
      this.idSalariePointeuse = idSalariePointeuse;
   }

   public Date getDateCategorie() {
      return this.dateCategorie;
   }

   public void setDateCategorie(Date var1) {
      this.dateCategorie = dateCategorie;
   }

   public Integer getNbAnnnesCategorie() {
      return this.nbAnnnesCategorie;
   }

   public void setNbAnnnesCategorie(Integer var1) {
      this.nbAnnnesCategorie = nbAnnnesCategorie;
   }

   public boolean isAvancementCategorieAuto() {
      return this.avancementCategorieAuto;
   }

   public void setAvancementCategorieAuto(boolean var1) {
      this.avancementCategorieAuto = avancementCategorieAuto;
   }

   public boolean isEnDebauche() {
      return this.enDebauche;
   }

   public void setEnDebauche(boolean var1) {
      this.enDebauche = enDebauche;
   }

   public String getStructureOrigine() {
      return this.structureOrigine;
   }

   public void setStructureOrigine(String var1) {
      this.structureOrigine = structureOrigine;
   }

   public String getLieuTravail() {
      return this.lieuTravail;
   }

   public void setLieuTravail(String var1) {
      this.lieuTravail = lieuTravail;
   }

   public String getTypeContrat() {
      return this.typeContrat;
   }

   public void setTypeContrat(String var1) {
      this.typeContrat = typeContrat;
   }

   public Date getDateFinContrat() {
      return this.dateFinContrat;
   }

   public void setDateFinContrat(Date var1) {
      this.dateFinContrat = dateFinContrat;
   }

   public String getNoteSurBulletin() {
      return this.noteSurBulletin;
   }

   public void setNoteSurBulletin(String var1) {
      this.noteSurBulletin = noteSurBulletin;
   }

   public String getNoPassport() {
      return this.noPassport;
   }

   public void setNoPassport(String var1) {
      this.noPassport = noPassport;
   }

   public Date getDateLivraisonPassport() {
      return this.dateLivraisonPassport;
   }

   public void setDateLivraisonPassport(Date var1) {
      this.dateLivraisonPassport = dateLivraisonPassport;
   }

   public Date getDateExpirationPassport() {
      return this.dateExpirationPassport;
   }

   public void setDateExpirationPassport(Date var1) {
      this.dateExpirationPassport = dateExpirationPassport;
   }

   public Date getDateDebutVisa() {
      return this.dateDebutVisa;
   }

   public void setDateDebutVisa(Date var1) {
      this.dateDebutVisa = dateDebutVisa;
   }

   public Date getDateFinVisa() {
      return this.dateFinVisa;
   }

   public void setDateFinVisa(Date var1) {
      this.dateFinVisa = dateFinVisa;
   }

   public String getNoCarteSejour() {
      return this.noCarteSejour;
   }

   public void setNoCarteSejour(String var1) {
      this.noCarteSejour = noCarteSejour;
   }

   public String getNoPermiTravail() {
      return this.noPermiTravail;
   }

   public void setNoPermiTravail(String var1) {
      this.noPermiTravail = noPermiTravail;
   }

   public Date getDateLivraisonPermiTravail() {
      return this.dateLivraisonPermiTravail;
   }

   public void setDateLivraisonPermiTravail(Date var1) {
      this.dateLivraisonPermiTravail = dateLivraisonPermiTravail;
   }

   public Date getDateExpirationPermiTravail() {
      return this.dateExpirationPermiTravail;
   }

   public void setDateExpirationPermiTravail(Date var1) {
      this.dateExpirationPermiTravail = dateExpirationPermiTravail;
   }

   public String getTelephone() {
      return this.telephone;
   }

   public void setTelephone(String var1) {
      this.telephone = telephone;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String var1) {
      this.email = email;
   }

   public String getAdresse() {
      return this.adresse;
   }

   public void setAdresse(String var1) {
      this.adresse = adresse;
   }

   public String getIdPsservice() {
      return this.idPsservice;
   }

   public void setIdPsservice(String var1) {
      this.idPsservice = idPsservice;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = password;
   }

   public boolean isPsservice() {
      return this.psservice;
   }

   public void setPsservice(boolean var1) {
      this.psservice = psservice;
   }

   public boolean isExpatrie() {
      return this.expatrie;
   }

   public void setExpatrie(boolean var1) {
      this.expatrie = expatrie;
   }

   public String getPrenom() {
      return this.prenom;
   }

   public void setPrenom(String var1) {
      this.prenom = prenom;
   }

   public String getStatut() {
      return this.statut;
   }

   public void setStatut(String var1) {
      this.statut = statut;
   }

   public String getClassification() {
      return this.classification;
   }

   public void setClassification(String var1) {
      this.classification = classification;
   }

   public Double getBudgetannuel() {
      return this.budgetannuel;
   }

   public void setBudgetannuel(Double var1) {
      this.budgetannuel = budgetannuel;
   }

   public boolean isLundiDs() {
      return this.lundiDs;
   }

   public void setLundiDs(boolean var1) {
      this.lundiDs = lundiDs;
   }

   public boolean isLundiFs() {
      return this.lundiFs;
   }

   public void setLundiFs(boolean var1) {
      this.lundiFs = lundiFs;
   }

   public boolean isLundiWe() {
      return this.lundiWe;
   }

   public void setLundiWe(boolean var1) {
      this.lundiWe = lundiWe;
   }

   public boolean isMardiDs() {
      return this.mardiDs;
   }

   public void setMardiDs(boolean var1) {
      this.mardiDs = mardiDs;
   }

   public boolean isMardiFs() {
      return this.mardiFs;
   }

   public void setMardiFs(boolean var1) {
      this.mardiFs = mardiFs;
   }

   public boolean isMardiWe() {
      return this.mardiWe;
   }

   public void setMardiWe(boolean var1) {
      this.mardiWe = mardiWe;
   }

   public boolean isMercrediDs() {
      return this.mercrediDs;
   }

   public void setMercrediDs(boolean var1) {
      this.mercrediDs = mercrediDs;
   }

   public boolean isMercrediFs() {
      return this.mercrediFs;
   }

   public void setMercrediFs(boolean var1) {
      this.mercrediFs = mercrediFs;
   }

   public boolean isMercrediWe() {
      return this.mercrediWe;
   }

   public void setMercrediWe(boolean var1) {
      this.mercrediWe = mercrediWe;
   }

   public boolean isJeudiDs() {
      return this.jeudiDs;
   }

   public void setJeudiDs(boolean var1) {
      this.jeudiDs = jeudiDs;
   }

   public boolean isJeudiFs() {
      return this.jeudiFs;
   }

   public void setJeudiFs(boolean var1) {
      this.jeudiFs = jeudiFs;
   }

   public boolean isJeudiWe() {
      return this.jeudiWe;
   }

   public void setJeudiWe(boolean var1) {
      this.jeudiWe = jeudiWe;
   }

   public boolean isVendrediDs() {
      return this.vendrediDs;
   }

   public void setVendrediDs(boolean var1) {
      this.vendrediDs = vendrediDs;
   }

   public boolean isVendrediFs() {
      return this.vendrediFs;
   }

   public void setVendrediFs(boolean var1) {
      this.vendrediFs = vendrediFs;
   }

   public boolean isVendrediWe() {
      return this.vendrediWe;
   }

   public void setVendrediWe(boolean var1) {
      this.vendrediWe = vendrediWe;
   }

   public boolean isSamediDs() {
      return this.samediDs;
   }

   public void setSamediDs(boolean var1) {
      this.samediDs = samediDs;
   }

   public boolean isSamediFs() {
      return this.samediFs;
   }

   public void setSamediFs(boolean var1) {
      this.samediFs = samediFs;
   }

   public boolean isSamediWe() {
      return this.samediWe;
   }

   public void setSamediWe(boolean var1) {
      this.samediWe = samediWe;
   }

   public boolean isDimancheDs() {
      return this.dimancheDs;
   }

   public void setDimancheDs(boolean var1) {
      this.dimancheDs = dimancheDs;
   }

   public boolean isDimancheFs() {
      return this.dimancheFs;
   }

   public void setDimancheFs(boolean var1) {
      this.dimancheFs = dimancheFs;
   }

   public boolean isDimancheWe() {
      return this.dimancheWe;
   }

   public void setDimancheWe(boolean var1) {
      this.dimancheWe = dimancheWe;
   }

   public byte[] getPhoto() {
      return this.photo;
   }

   public void setPhoto(byte[] var1) {
      this.photo = photo;
   }

   public Set<Retenuesaecheances> getRetenuesaecheanceses() {
      return this.retenuesaecheanceses;
   }

   public void setRetenuesaecheanceses(Set<Retenuesaecheances> var1) {
      this.retenuesaecheanceses = retenuesaecheanceses;
   }

   public Set<Paie> getPaies() {
      return this.paies;
   }

   public void setPaies(Set<Paie> var1) {
      this.paies = paies;
   }

   public Set<Weekot> getWeekots() {
      return this.weekots;
   }

   public void setWeekots(Set<Weekot> var1) {
      this.weekots = weekots;
   }

   public Set<Rubriquepaie> getRubriquepaies() {
      return this.rubriquepaies;
   }

   public void setRubriquepaies(Set<Rubriquepaie> var1) {
      this.rubriquepaies = rubriquepaies;
   }

   public Set<Conges> getCongeses() {
      return this.congeses;
   }

   public void setCongeses(Set<Conges> var1) {
      this.congeses = congeses;
   }

   public Set<Njtsalarie> getNjtsalaries() {
      return this.njtsalaries;
   }

   public void setNjtsalaries(Set<Njtsalarie> var1) {
      this.njtsalaries = njtsalaries;
   }

   public Set<Jour> getJours() {
      return this.jours;
   }

   public void setJours(Set<Jour> var1) {
      this.jours = jours;
   }

   public Set<Enfants> getEnfantss() {
      return this.enfantss;
   }

   public void setEnfantss(Set<Enfants> var1) {
      this.enfantss = enfantss;
   }

   public Set<Diplome> getDiplomes() {
      return this.diplomes;
   }

   public void setDiplomes(Set<Diplome> var1) {
      this.diplomes = diplomes;
   }

   public Set<Document> getDocuments() {
      return this.documents;
   }

   public void setDocuments(Set<Document> var1) {
      this.documents = documents;
   }
}
