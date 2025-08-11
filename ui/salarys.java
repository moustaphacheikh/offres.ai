package com.mccmr.ui;

import com.mccmr.entity.Conges;
import com.mccmr.entity.Diplome;
import com.mccmr.entity.Document;
import com.mccmr.entity.Employe;
import com.mccmr.entity.Enfants;
import com.mccmr.entity.Jour;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Paramgen;
import com.mccmr.entity.Retenuesaecheances;
import com.mccmr.entity.Rubrique;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.entity.Tranchesretenuesaecheances;
import com.mccmr.entity.Utilisateurs;
import com.mccmr.entity.Weekot;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.CustomTableRenderer;
import com.mccmr.util.FichePaieDetail;
import com.mccmr.util.ModelClass;
import com.mccmr.util.WriteExcel;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.io.FilenameUtils;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class salarys extends JInternalFrame {
   public menu menu;
   public Motif selectedMotif;
   public Date selectedPeriode;
   public Employe selectedOne = null;
   public Document selectedDoc = null;
   public Conges selectedConges = null;
   public Rubriquepaie selectedPaie;
   public List<Employe> dataList;
   public List<Employe> dataListInit;
   private Retenuesaecheances selectedRAE;
   JFileChooser fileChooser;
   String photoPath;
   private JButton appSBCatButton;
   private JButton btnAppEng;
   private JButton btnCalPaie;
   private JButton btnDelConges;
   private JButton btnDelDiplome;
   private JButton btnDelDoc;
   private JButton btnDelEnf;
   private JButton btnDelEng;
   private JButton btnDelHSDay;
   private JButton btnDelHSWeek;
   private JButton btnDelPaie;
   private JButton btnDelete;
   private JButton btnExcelCumul_Conges;
   private JButton btnExit;
   private JButton btnInfosPaie;
   private JButton btnNew;
   private JButton btnNew2;
   private JButton btnNewConges;
   private JButton btnNewDiplome;
   private JButton btnNewDoc;
   private JButton btnNewEnf;
   private JButton btnNewEng;
   private JButton btnPrintPaie;
   private JButton btnRefresh;
   private JButton btnRefreshItem;
   private JButton btnSave;
   private JButton btnSaveConges;
   private JButton btnSaveDiplome;
   private JButton btnSaveDoc;
   private JButton btnSaveEmpNote;
   private JButton btnSaveEnf;
   private JButton btnSaveEng;
   private JButton btnSaveHSDay;
   private JButton btnSaveHSWeek;
   private JButton btnSaveNJT;
   private JButton btnSavePaie;
   private JButton btnSaveRappelSB;
   private JButton btnSearchDoc;
   private JButton btnValoriserHS;
   private JButton buttonConges;
   private JButton buttonConges1;
   private JCheckBox cActif;
   private JCheckBox cActiveRetAE;
   private JCheckBox cAvancmentAutoCat;
   private JCheckBox cDIMBegin;
   private JCheckBox cDIMEnd;
   private JCheckBox cDIMwe;
   private JCheckBox cDetacheCNAM;
   private JCheckBox cDetacheCNSS;
   private JCheckBox cDomicilie;
   private JCheckBox cDroitsCongesAuto;
   private JCheckBox cExonoreITS;
   private JCheckBox cExpatrie;
   private JCheckBox cF50Auto;
   private JCheckBox cFerie100;
   private JCheckBox cFerie50;
   private JCheckBox cFixe;
   private JCheckBox cJEUBegin;
   private JCheckBox cJEUEnd;
   private JCheckBox cJEUwe;
   private JCheckBox cLUNBegin;
   private JCheckBox cLUNEnd;
   private JCheckBox cLUNwe;
   private JCheckBox cMARBegin;
   private JCheckBox cMAREnd;
   private JCheckBox cMARwe;
   private JCheckBox cMERBegin;
   private JCheckBox cMEREnd;
   private JCheckBox cMERwe;
   private JCheckBox cOnMotif;
   private JCheckBox cPPAuto;
   private JCheckBox cPersonnelActif;
   private JCheckBox cPsservice;
   private JCheckBox cSAMBegin;
   private JCheckBox cSAMEnd;
   private JCheckBox cSAMwe;
   private JCheckBox cSiteExterne;
   private JCheckBox cVENBegin;
   private JCheckBox cVENEnd;
   private JCheckBox cVENwe;
   private JTable congesTable;
   private JTabbedPane detailPanel;
   private JTable heuresTable;
   private JTable historiqueRetAETable;
   private JLabel jLabel100;
   private JLabel jLabel101;
   private JLabel jLabel102;
   private JLabel jLabel103;
   private JLabel jLabel104;
   private JLabel jLabel105;
   private JLabel jLabel106;
   private JLabel jLabel107;
   private JLabel jLabel108;
   private JLabel jLabel109;
   private JLabel jLabel11;
   private JLabel jLabel110;
   private JLabel jLabel111;
   private JLabel jLabel112;
   private JLabel jLabel113;
   private JLabel jLabel114;
   private JLabel jLabel115;
   private JLabel jLabel116;
   private JLabel jLabel117;
   private JLabel jLabel118;
   private JLabel jLabel119;
   private JLabel jLabel120;
   private JLabel jLabel121;
   private JLabel jLabel122;
   private JLabel jLabel123;
   private JLabel jLabel124;
   private JLabel jLabel125;
   private JLabel jLabel126;
   private JLabel jLabel127;
   private JLabel jLabel128;
   private JLabel jLabel129;
   private JLabel jLabel13;
   private JLabel jLabel130;
   private JLabel jLabel131;
   private JLabel jLabel132;
   private JLabel jLabel133;
   private JLabel jLabel134;
   private JLabel jLabel135;
   private JLabel jLabel136;
   private JLabel jLabel137;
   private JLabel jLabel138;
   private JLabel jLabel139;
   private JLabel jLabel14;
   private JLabel jLabel140;
   private JLabel jLabel141;
   private JLabel jLabel142;
   private JLabel jLabel143;
   private JLabel jLabel144;
   private JLabel jLabel15;
   private JLabel jLabel153;
   private JLabel jLabel154;
   private JLabel jLabel16;
   private JLabel jLabel17;
   private JLabel jLabel19;
   private JLabel jLabel20;
   private JLabel jLabel21;
   private JLabel jLabel22;
   private JLabel jLabel23;
   private JLabel jLabel24;
   private JLabel jLabel25;
   private JLabel jLabel26;
   private JLabel jLabel27;
   private JLabel jLabel28;
   private JLabel jLabel29;
   private JLabel jLabel30;
   private JLabel jLabel31;
   private JLabel jLabel32;
   private JLabel jLabel33;
   private JLabel jLabel34;
   private JLabel jLabel35;
   private JLabel jLabel36;
   private JLabel jLabel37;
   private JLabel jLabel38;
   private JLabel jLabel39;
   private JLabel jLabel40;
   private JLabel jLabel41;
   private JLabel jLabel42;
   private JLabel jLabel43;
   private JLabel jLabel44;
   private JLabel jLabel45;
   private JLabel jLabel46;
   private JLabel jLabel47;
   private JLabel jLabel48;
   private JLabel jLabel49;
   private JLabel jLabel50;
   private JLabel jLabel51;
   private JLabel jLabel52;
   private JLabel jLabel53;
   private JLabel jLabel54;
   private JLabel jLabel55;
   private JLabel jLabel56;
   private JLabel jLabel57;
   private JLabel jLabel58;
   private JLabel jLabel59;
   private JLabel jLabel6;
   private JLabel jLabel60;
   private JLabel jLabel61;
   private JLabel jLabel62;
   private JLabel jLabel63;
   private JLabel jLabel64;
   private JLabel jLabel65;
   private JLabel jLabel66;
   private JLabel jLabel67;
   private JLabel jLabel68;
   private JLabel jLabel69;
   private JLabel jLabel7;
   private JLabel jLabel70;
   private JLabel jLabel71;
   private JLabel jLabel72;
   private JLabel jLabel73;
   private JLabel jLabel74;
   private JLabel jLabel75;
   private JLabel jLabel76;
   private JLabel jLabel77;
   private JLabel jLabel78;
   private JLabel jLabel79;
   private JLabel jLabel8;
   private JLabel jLabel80;
   private JLabel jLabel81;
   private JLabel jLabel82;
   private JLabel jLabel83;
   private JLabel jLabel84;
   private JLabel jLabel85;
   private JLabel jLabel86;
   private JLabel jLabel87;
   private JLabel jLabel88;
   private JLabel jLabel89;
   private JLabel jLabel9;
   private JLabel jLabel90;
   private JLabel jLabel91;
   private JLabel jLabel92;
   private JLabel jLabel93;
   private JLabel jLabel94;
   private JLabel jLabel95;
   private JLabel jLabel96;
   private JLabel jLabel97;
   private JLabel jLabel98;
   private JLabel jLabel99;
   private JPanel jPanel1;
   private JPanel jPanel10;
   private JPanel jPanel11;
   private JPanel jPanel12;
   private JPanel jPanel13;
   private JPanel jPanel14;
   private JPanel jPanel15;
   private JPanel jPanel16;
   private JPanel jPanel17;
   private JPanel jPanel18;
   private JPanel jPanel19;
   private JPanel jPanel20;
   private JPanel jPanel21;
   private JPanel jPanel22;
   private JPanel jPanel23;
   private JPanel jPanel24;
   private JPanel jPanel25;
   private JPanel jPanel26;
   private JPanel jPanel27;
   private JPanel jPanel28;
   private JPanel jPanel29;
   private JPanel jPanel3;
   private JScrollPane jScrollPane10;
   private JScrollPane jScrollPane11;
   private JScrollPane jScrollPane12;
   private JScrollPane jScrollPane13;
   private JScrollPane jScrollPane14;
   private JScrollPane jScrollPane15;
   private JScrollPane jScrollPane16;
   private JScrollPane jScrollPane17;
   private JScrollPane jScrollPane18;
   private JScrollPane jScrollPane19;
   private JScrollPane jScrollPane9;
   private JSeparator jSeparator17;
   private JSeparator jSeparator77;
   private JSeparator jSeparator92;
   private JSeparator jSeparator93;
   private JTabbedPane jTabbedPane1;
   private JTabbedPane jTabbedPane2;
   private JTabbedPane jTabbedPane3;
   private JFormattedTextField lbECCNG;
   private JFormattedTextField lbEncours;
   private JTextField lbFilePath;
   private JLabel lbNbSalaries;
   private JLabel lbNbSalariesActifs;
   private JLabel lbNbSalariesEnConges;
   private JFormattedTextField lbPeriodeInit;
   private JLabel lbPhoto;
   private JLabel lbSearch;
   private JTable listDocuments;
   private JTable listTable;
   private JTable listTableDiplome;
   private JTable listTableEnfant;
   private JLabel msgLabel;
   private JPanel pConges;
   private JPanel pContrat;
   private JTabbedPane pContrat_TabbedPane;
   private JPanel pDetail;
   private JPanel pDiplomes;
   private JPanel pDocuments;
   private JPanel pDonneesId;
   private JPanel pDonneesPro;
   private JPanel pEnfants;
   private JPanel pEngagement;
   private JPanel pIdentite;
   private JTabbedPane pIdentite_TabbedPane;
   private JPanel pList;
   private JPanel pPaie;
   private JTabbedPane pPaie_TabbedPane;
   private JPanel pTaxes;
   private JPanel pVariables;
   private JTabbedPane panelHS;
   private JTabbedPane panelHS1;
   private JPanel pnlActivites_Btn1;
   private JPanel pnlBody;
   private JPanel pointageHSPanel;
   private JProgressBar progressBar;
   private JPanel rappelSBPanel;
   private JTable rappelTable;
   private JTable retAETable;
   private JTable rubriquePaieTable;
   private JTable rubriquesSurSBTable;
   private JLabel salarieLabel;
   private JTextField searchField;
   private JComboBox<Object> tActivite;
   private JTextField tAdresse;
   private JFormattedTextField tBARelicat;
   private JComboBox<Object> tBanque;
   private JFormattedTextField tBase;
   private JDateChooser tBeginWeek;
   private JFormattedTextField tBudgetAnuel;
   private JFormattedTextField tCapital;
   private JComboBox<Object> tCategorie;
   private JTextField tClassification;
   private JFormattedTextField tCumul12DM;
   private JFormattedTextField tCumul12DMinitial;
   private JFormattedTextField tCumulBrutImposable;
   private JFormattedTextField tCumulBrutImposableInitial;
   private JFormattedTextField tCumulBrutNonImposable;
   private JFormattedTextField tCumulBrutNonImposableInitial;
   private JFormattedTextField tCumulRet;
   private JDateChooser tDateAccord;
   private JDateChooser tDateAnciennete;
   private JDateChooser tDateCNSS;
   private JDateChooser tDateCategorie;
   private JDateChooser tDateDebauche;
   private JDateChooser tDateDebauche3;
   private JDateChooser tDateDepart;
   private JDateChooser tDateEPassport;
   private JDateChooser tDateEPermiTravail;
   private JDateChooser tDateEVisa;
   private JDateChooser tDateEmbauche;
   private JDateChooser tDateFinContrat;
   private JDateChooser tDateJour;
   private JDateChooser tDateLPassport;
   private JDateChooser tDateLPermiTravail;
   private JDateChooser tDateLVisa;
   private JDateChooser tDateNaiss;
   private JDateChooser tDateNaissEnf;
   private JDateChooser tDateObtDiplome;
   private JDateChooser tDateReprise;
   private JDateChooser tDebutRappel;
   private JTextField tDegreDiplome;
   private JComboBox<Object> tDepartement;
   private JDateChooser tDernierDepart;
   private JDateChooser tDernierDepartInitial;
   private JTextField tDestFile;
   private JFormattedTextField tDiplomeID;
   private JComboBox<Object> tDirection;
   private JComboBox<Object> tDirectiongeneral;
   private JTextField tDomaineDiplome;
   private JFormattedTextField tEcheance;
   private JTextField tEmail;
   private JDateChooser tEndWeek;
   private JFormattedTextField tEnfantID;
   private JTextField tEtablissementDiplome;
   private JComboBox<Object> tGenreEnf;
   private JFormattedTextField tHeureSemaine;
   private JFormattedTextField tHeuresJour;
   private JFormattedTextField tHeuresNuit;
   private JFormattedTextField tIDRubRappel;
   private JFormattedTextField tIDSalariePointeuse;
   private JFormattedTextField tId;
   private JTextField tIdPsservice;
   private JTextField tLieuNaiss;
   private JTextField tLieuTravail;
   private JTextField tMere;
   private JComboBox<Object> tModeDecompte;
   private JComboBox<Object> tModePaiement;
   private JFormattedTextField tMontant;
   private JFormattedTextField tMontantRappel;
   private JComboBox<Object> tMotif;
   private JComboBox<Object> tMotifRappel;
   private JTextField tNNI;
   private JTextField tNationalite;
   private JFormattedTextField tNbAnneesCat;
   private JFormattedTextField tNbEnfants;
   private JFormattedTextField tNbMoisPreavis;
   private JFormattedTextField tNjt;
   private JTextField tNoCNAM;
   private JTextField tNoCNSS;
   private JTextField tNoCarteSejour;
   private JTextField tNoCompteBanque;
   private JTextField tNoPassport;
   private JTextField tNoPermiTravail;
   private JTextField tNom;
   private JTextField tNomDiplome;
   private JTextField tNomDocument;
   private JTextField tNomEnfant;
   private JFormattedTextField tNombre;
   private JTextField tNoteConges;
   private JTextField tNoteJour;
   private JTextField tNoteRetAE;
   private JTextField tNoteSurBulletin;
   private JDateChooser tPaieAu;
   private JDateChooser tPaieDu;
   private JTextField tPere;
   private JTextField tPereMereEnf;
   private JComboBox<Object> tPeriode;
   private JComboBox<Object> tPeriodeRappel;
   private JComboBox<Object> tPoste;
   private JTextField tPrenom;
   private JFormattedTextField tPrimesPanier;
   private JTextField tRaisonDebauche;
   private JDateChooser tRepriseEff;
   private JComboBox<Object> tRubriqueRetAE;
   private JComboBox<Object> tRubriques;
   private JComboBox<Object> tSexe;
   private JComboBox<Object> tSituationFam;
   private JTextField tStatut;
   private JTextField tStrictureOrigine;
   private JFormattedTextField tTauxAnc;
   private JFormattedTextField tTauxAnc4;
   private JFormattedTextField tTauxPSRA;
   private JFormattedTextField tTauxRembCNAM;
   private JFormattedTextField tTauxRembCNSS;
   private JTextField tTelephone;
   private JFormattedTextField tTotHJ;
   private JFormattedTextField tTotHN;
   private JFormattedTextField tTotHS;
   private JFormattedTextField tTotHS115;
   private JFormattedTextField tTotHS140;
   private JFormattedTextField tTotHS150;
   private JFormattedTextField tTotHS200;
   private JFormattedTextField tTotPE;
   private JFormattedTextField tTotPP;
   private JFormattedTextField tTotReg;
   private JLabel tTotalGainsFix;
   private JLabel tTotalGainsVar;
   private JLabel tTotalRetenues;
   private JComboBox<Object> tTypeContrat;
   private JFormattedTextField tWeekOT115;
   private JFormattedTextField tWeekOT140;
   private JFormattedTextField tWeekOT150;
   private JFormattedTextField tWeekOT200;
   private JFormattedTextField tWeekPE;
   private JFormattedTextField tWeekPP;
   private JComboBox<Object> tZoneOrigine;
   private JFormattedTextField tauxRembITStranche1;
   private JFormattedTextField tauxRembITStranche2;
   private JFormattedTextField tauxRembITStranche3;

   public salarys() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.lbSearch.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SEARCH, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnRefresh.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.REFRESH, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnRefreshItem.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.REFRESH, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelete.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnNew.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNew2.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnPrintPaie.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PRINT, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNewEnf.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSaveEnf.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelEnf.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnNewDiplome.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSaveDiplome.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelDiplome.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnNewConges.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSaveConges.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelConges.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnExcelCumul_Conges.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNewEng.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSaveEng.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelEng.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnAppEng.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CHECK, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSearchDoc.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FIND_IN_PAGE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNewDoc.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSaveDoc.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelDoc.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnSaveNJT.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSavePaie.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelPaie.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnInfosPaie.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.INFO, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnCalPaie.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADJUST, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnPrintPaie.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PRINT, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSaveEmpNote.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSaveRappelSB.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSaveHSDay.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelHSDay.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnSaveHSWeek.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelHSWeek.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnValoriserHS.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CHECK, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.clearFields();
      this.menu.remplirCombo("Periode", this.tPeriode);
      this.menu.remplirCombo("Periode", this.tPeriodeRappel);
      this.menu.remplirCombo("Rubrique", this.tRubriques);
      this.menu.remplirCombo("RubriqueRet", this.tRubriqueRetAE);
      this.menu.remplirCombo("Motif", this.tMotif);
      this.menu.remplirCombo("Motif", this.tMotifRappel);
      this.menu.remplirCombo("Origines", this.tZoneOrigine);
      this.menu.remplirCombo("Grillesalairebase", this.tCategorie);
      this.menu.remplirCombo("Directiongeneral", this.tDirectiongeneral);
      this.menu.remplirCombo("Direction", this.tDirection);
      this.menu.remplirCombo("Departement", this.tDepartement);
      this.menu.remplirCombo("Poste", this.tPoste);
      this.menu.remplirCombo("Banque", this.tBanque);
      this.menu.remplirCombo("Activite", this.tActivite);
      this.selectedMotif = this.menu.motifSN;
      this.selectedPeriode = this.menu.paramsGen.getPeriodeCourante();
      this.tPeriode.setSelectedItem(this.selectedPeriode);
      this.jTabbedPane1.setSelectedIndex(0);
      Date paieAu_default = this.menu.paramsGen.getPeriodeCourante();
      Date paieDu = this.menu.gl.addRetriveDays(paieAu_default, -27);
      Calendar cal = Calendar.getInstance();
      cal.setTime(paieDu);
      Date paieAu = this.menu.gl.addRetriveDays(paieDu, cal.getActualMaximum(5) - 1);
      this.tPaieAu.setDate(paieAu);
      this.tPaieDu.setDate(paieDu);
      this.tDateReprise.setDate(this.menu.paramsGen.getPeriodeCourante());
      this.tRepriseEff.setDate(this.menu.paramsGen.getPeriodeCourante());
      this.tDateDepart.setDate(this.menu.gl.addRetriveDays(this.tPaieAu.getDate(), -27));
      this.tDateAccord.setDate(this.menu.gl.addRetriveDays(this.menu.paramsGen.getPeriodeCourante(), -27));
      this.tDateObtDiplome.setDate(new Date());
      this.tDateNaissEnf.setDate(new Date());
      this.cF50Auto.setSelected(true);
      this.tDateJour.setDate(this.menu.gl.addRetriveDays(this.menu.paramsGen.getPeriodeCourante(), -27));
      this.tBeginWeek.setDate(this.menu.gl.addRetriveDays(this.menu.paramsGen.getPeriodeCourante(), -27));
      this.tEndWeek.setDate(this.menu.gl.addRetriveDays(this.tBeginWeek.getDate(), 6));
      this.panelHS.setEnabledAt(1, !this.tModeDecompte.getSelectedItem().toString().equalsIgnoreCase("Quotidien"));
      this.panelHS.setEnabledAt(0, this.tModeDecompte.getSelectedItem().toString().equalsIgnoreCase("Quotidien"));
      this.dataListUpdate();
      this.afficherListe();
      this.menu.firstRefresh = true;
   }

   public void dataListUpdate() {
      Query q = this.menu.entityManager.createQuery("Select p from Employe p");
      q.setMaxResults(1000000);
      this.dataListInit = q.getResultList();
   }

   public void RolesAction(Utilisateurs var1) {
      this.btnNew.setVisible(role.isSalAdd());
      this.btnDelete.setVisible(role.isSuppsal());
      this.btnSave.setVisible(role.isSalUpdate());
      this.btnSaveConges.setVisible(role.isMaj());
      this.btnSavePaie.setVisible(role.isMaj());
      this.btnSaveEng.setVisible(role.isMaj());
      this.btnSaveNJT.setVisible(role.isMaj());
      this.btnCalPaie.setVisible(role.isMaj());
      this.btnDelConges.setVisible(role.isSuppression());
      this.btnDelPaie.setVisible(role.isSuppression());
      this.btnDelEng.setVisible(role.isSuppression());
      this.btnDelHSDay.setVisible(role.isSuppression());
      this.btnSaveHSDay.setVisible(role.isMaj());
      this.btnValoriserHS.setVisible(role.isMaj());
      this.btnSaveRappelSB.setVisible(role.isMaj());
      this.appSBCatButton.setVisible(role.isMaj());
      this.pIdentite_TabbedPane.setEnabledAt(0, role.isSalIdentite());
      this.pIdentite_TabbedPane.setEnabledAt(1, role.isSalIdentite());
      this.pIdentite_TabbedPane.setEnabledAt(2, role.isSalDiplome());
      this.pIdentite_TabbedPane.setEnabledAt(3, role.isSalDoc());
      this.detailPanel.setEnabledAt(1, role.isSalContrat());
      this.pPaie_TabbedPane.setEnabledAt(0, role.isSalPaie());
      this.pPaie_TabbedPane.setEnabledAt(1, role.isSalPaie() && !this.menu.demo);
      this.pPaie_TabbedPane.setEnabledAt(2, role.isSalHs());
      this.detailPanel.setEnabledAt(3, role.isSalConge());
      this.detailPanel.setEnabledAt(4, role.isSalRetenueae());
   }

   public void afficherListe() {
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmEmployes(var10003));
      this.listTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      this.dataList = this.dataListInit;
      if (this.cPersonnelActif.isSelected()) {
         this.dataList = (List)this.dataList.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList());
      }

      if (!this.searchField.getText().isEmpty()) {
         this.dataList = (List)this.dataList.stream().filter((var1) -> (new Integer(var1.getId())).toString().equalsIgnoreCase(this.searchField.getText()) || var1.getPrenom().toLowerCase().contains(this.searchField.getText().toLowerCase()) || var1.getNom().toLowerCase().contains(this.searchField.getText().toLowerCase())).collect(Collectors.toList());
      }

      this.dataList = (List)this.dataList.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList());
      ((ModelClass.tmEmployes)this.listTable.getModel()).setCurrentPeriode(this.menu.paramsGen.getPeriodeCourante());

      for(Employe rs : this.dataList) {
         ((ModelClass.tmEmployes)this.listTable.getModel()).addRow(rs);
      }

      this.listTable.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable.getColumnModel().getColumn(1).setPreferredWidth(5);
      this.listTable.getColumnModel().getColumn(2).setPreferredWidth(50);
      this.listTable.getColumnModel().getColumn(3).setPreferredWidth(100);
      this.listTable.getColumnModel().getColumn(4).setPreferredWidth(100);
      this.listTable.setRowHeight(30);
      TableColumn var4 = this.listTable.getColumnModel().getColumn(0);
      var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var4.setCellRenderer(new ModelClass.ColorCellRenderer(var10003));
      var4 = this.listTable.getColumnModel().getColumn(1);
      var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var4.setCellRenderer(new ModelClass.ColorCellRenderer(var10003));
      this.listTable.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable.getModel());
      this.listTable.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.ColorComparator(var10004));
      var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(1, new ModelClass.ColorComparator(var10004));
      var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(2, new ModelClass.NumberComparator(var10004));
      var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(9, new ModelClass.DateComparator(var10004));
      var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(10, new ModelClass.DateComparator(var10004));
      var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(11, new ModelClass.NumberStringComparator(var10004));
      var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(12, new ModelClass.NumberStringComparator(var10004));
      var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(13, new ModelClass.DateComparator(var10004));
      this.lbNbSalaries.setText((new Integer(((List)this.dataListInit.stream().collect(Collectors.toList())).size())).toString());
      this.lbNbSalariesActifs.setText((new Integer(((List)this.dataListInit.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size())).toString());
      this.lbNbSalariesEnConges.setText((new Integer(((List)this.dataListInit.stream().filter((var0) -> var0.isActif() && var0.isEnConge()).collect(Collectors.toList())).size())).toString());
   }

   private void afficherRubriqueSurSB() {
      JTable var10000 = this.rubriquesSurSBTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmRubSurSB(var10003));
      this.rubriquesSurSBTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      Rubrique sb = this.menu.pc.usedRubID(1);
      if (sb != null) {
         List<Rubrique> dl = new ArrayList(this.menu.rubriqueFrame.dataListInit);

         for(Rubrique rs : (List)dl.stream().filter((var2x) -> var2x.getSens().equalsIgnoreCase("G") && (this.menu.pc.formulRubriqueOnSB(var2x, "B") || this.menu.pc.formulRubriqueOnSB(var2x, "N") || var2x.getId() == sb.getId())).sorted(Comparator.comparing(Rubrique::getId)).collect(Collectors.toList())) {
            ((ModelClass.tmRubSurSB)this.rubriquesSurSBTable.getModel()).addRow(rs);
         }
      }

      var10000 = this.rappelTable;
      var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmRappelSB(var10003));
      this.rappelTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
   }

   public void afficherHeures() {
      JTable var10000 = this.heuresTable;
      Object var10001;
      if (this.tModeDecompte.getSelectedItem().toString().equalsIgnoreCase("Quotidien")) {
         ModelClass var10003 = this.menu.mc;
         Objects.requireNonNull(var10003);
         var10001 = new ModelClass.tmHSDayly(var10003);
      } else {
         ModelClass var22 = this.menu.mc;
         Objects.requireNonNull(var22);
         var10001 = new ModelClass.tmHSWeekly(var22);
      }

      var10000.setModel((TableModel)var10001);
      this.heuresTable.getColumnModel().getColumn(0).setPreferredWidth(this.tModeDecompte.getSelectedItem().toString().equalsIgnoreCase("Quotidien") ? 70 : 80);
      this.heuresTable.getColumnModel().getColumn(1).setPreferredWidth(this.tModeDecompte.getSelectedItem().toString().equalsIgnoreCase("Quotidien") ? 100 : 80);
      this.heuresTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      if (this.selectedOne != null && this.selectedPeriode != null) {
         if (this.tModeDecompte.getSelectedItem().toString().equalsIgnoreCase("Quotidien")) {
            new ArrayList();
            Query q = this.menu.entityManager.createQuery("Select p from Jour p where p.employe.id = " + this.selectedOne.getId());
            q.setMaxResults(1000000);
            List var1 = q.getResultList();

            for(Jour rs : (List)var1.stream().filter((var1x) -> this.menu.df.format(var1x.getPeriode()).equalsIgnoreCase(this.menu.df.format(this.selectedPeriode))).sorted(Comparator.comparing(Jour::getDateJour)).collect(Collectors.toList())) {
               ((ModelClass.tmHSDayly)this.heuresTable.getModel()).addRow(rs);
            }

            if (this.heuresTable.getRowCount() != 0) {
               this.tDateJour.setDate(this.menu.gl.addRetriveDays(this.menu.pc.lastDateJourById(this.selectedOne), 1));
            }

            double[] tabHS = this.menu.pc.decompterHS(this.selectedOne, this.selectedPeriode);
            Double totHS = tabHS[2] + tabHS[3] + tabHS[4] + tabHS[5];
            this.tTotHJ.setValue(tabHS[0]);
            this.tTotHN.setValue(tabHS[1]);
            this.tTotHS115.setValue(tabHS[2]);
            this.tTotHS140.setValue(tabHS[3]);
            this.tTotHS150.setValue(tabHS[4]);
            this.tTotHS200.setValue(tabHS[5]);
            this.tTotHS.setValue(totHS);
            this.tTotPP.setValue(tabHS[6]);
            this.tTotPE.setValue(tabHS[7]);
         } else {
            double totWeekOT115 = (double)0.0F;
            double totWeekOT140 = (double)0.0F;
            double totWeekOT150 = (double)0.0F;
            double totWeekOT200 = (double)0.0F;
            double totWeekPP = (double)0.0F;
            double totWeekPE = (double)0.0F;
            List<Weekot> dl = new ArrayList(this.selectedOne.getWeekots());

            for(Weekot rs : (List)dl.stream().filter((var1x) -> this.menu.df.format(var1x.getPeriode()).equalsIgnoreCase(this.menu.df.format(this.selectedPeriode))).sorted(Comparator.comparing(Weekot::getBeginweek)).collect(Collectors.toList())) {
               ((ModelClass.tmHSWeekly)this.heuresTable.getModel()).addRow(rs);
               totWeekOT115 += rs.getOt115();
               totWeekOT140 += rs.getOt140();
               totWeekOT150 += rs.getOt150();
               totWeekOT200 += rs.getOt200();
               totWeekPP += rs.getNbPrimePanier();
               totWeekPE += rs.getNbPrimeEloignement();
            }

            if (this.heuresTable.getRowCount() != 0) {
               this.tBeginWeek.setDate(this.menu.gl.addRetriveDays(this.menu.pc.lastDateWeekById(this.selectedOne), 7));
               this.tEndWeek.setDate(this.menu.gl.addRetriveDays(this.tBeginWeek.getDate(), 6));
            }

            this.tTotHS115.setValue(totWeekOT115);
            this.tTotHS140.setValue(totWeekOT140);
            this.tTotHS150.setValue(totWeekOT150);
            this.tTotHS200.setValue(totWeekOT200);
            this.tTotPP.setValue(totWeekPP);
            this.tTotPE.setValue(totWeekPE);
         }
      }

   }

   private void afficherDiplome() {
      JTable var10000 = this.listTableDiplome;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmDiplome(var10003));
      this.listTableDiplome.setDefaultRenderer(Object.class, new CustomTableRenderer());
      new ArrayList();
      Query q = this.menu.entityManager.createQuery("Select p from Diplome p where p.employe.id = " + this.selectedOne.getId());
      q.setMaxResults(1000000);
      List var1 = q.getResultList();

      for(Diplome rs : (List)var1.stream().sorted(Comparator.comparing(Diplome::getDateObtention)).collect(Collectors.toList())) {
         ((ModelClass.tmDiplome)this.listTableDiplome.getModel()).addRow(rs);
      }

      this.listTableDiplome.getColumnModel().getColumn(0).setPreferredWidth(200);
      this.listTableDiplome.getColumnModel().getColumn(1).setPreferredWidth(30);
      this.listTableDiplome.getColumnModel().getColumn(2).setPreferredWidth(50);
      this.listTableDiplome.getColumnModel().getColumn(3).setPreferredWidth(100);
      this.listTableDiplome.getColumnModel().getColumn(4).setPreferredWidth(100);
      this.listTableDiplome.getColumnModel().getColumn(5).setPreferredWidth(5);
      this.listTableDiplome.setRowHeight(20);
   }

   private void afficherEnfant() {
      JTable var10000 = this.listTableEnfant;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmEnfant(var10003));
      this.listTableEnfant.setDefaultRenderer(Object.class, new CustomTableRenderer());
      new ArrayList();
      Query q = this.menu.entityManager.createQuery("Select p from Enfants p where p.employe.id = " + this.selectedOne.getId());
      q.setMaxResults(1000000);
      List var1 = q.getResultList();

      for(Enfants rs : (List)var1.stream().sorted(Comparator.comparing(Enfants::getNomEnfant)).collect(Collectors.toList())) {
         ((ModelClass.tmEnfant)this.listTableEnfant.getModel()).addRow(rs);
      }

      this.listTableEnfant.getColumnModel().getColumn(0).setPreferredWidth(300);
      this.listTableEnfant.getColumnModel().getColumn(1).setPreferredWidth(30);
      this.listTableEnfant.getColumnModel().getColumn(2).setPreferredWidth(50);
      this.listTableEnfant.getColumnModel().getColumn(3).setPreferredWidth(200);
      this.listTableEnfant.getColumnModel().getColumn(4).setPreferredWidth(5);
      this.listTableEnfant.setRowHeight(20);
   }

   private void afficherConges() {
      JTable var10000 = this.congesTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmConges(var10003));
      this.congesTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      if (this.selectedOne != null) {
         this.btnExcelCumul_Conges.setEnabled(true);
         new ArrayList();
         Query q = this.menu.entityManager.createQuery("Select p from Conges p where p.employe.id = " + this.selectedOne.getId());
         q.setMaxResults(1000000);
         List var1 = q.getResultList();

         for(Conges rs : (List)var1.stream().sorted(Comparator.comparing((var0) -> var0.getDepart())).collect(Collectors.toList())) {
            ((ModelClass.tmConges)this.congesTable.getModel()).addRow(rs);
         }
      }

      this.congesTable.setRowHeight(20);
      this.congesTable.getColumnModel().getColumn(0).setPreferredWidth(80);
      this.congesTable.getColumnModel().getColumn(1).setPreferredWidth(80);
      this.congesTable.getColumnModel().getColumn(2).setPreferredWidth(80);
      this.congesTable.getColumnModel().getColumn(3).setPreferredWidth(80);
      this.congesTable.getColumnModel().getColumn(4).setPreferredWidth(200);
      this.congesTable.getColumnModel().getColumn(5).setPreferredWidth(5);
   }

   private void afficherRubriquePaie() {
      Double totalGainsFix = (double)0.0F;
      Double totalGainsVar = (double)0.0F;
      Double totalRetenues = (double)0.0F;
      JTable var10000 = this.rubriquePaieTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmRubriquePaie(var10003));
      this.rubriquePaieTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      if (this.selectedOne != null && this.selectedPeriode != null) {
         List<Rubriquepaie> dl = new ArrayList(this.menu.pc.empRubriquepaie(this.selectedOne, this.cOnMotif.isSelected() ? this.selectedMotif : null, this.selectedPeriode));
         List var7 = (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getRubrique().getId())).collect(Collectors.toList());
         ((ModelClass.tmRubriquePaie)this.rubriquePaieTable.getModel()).setCurrentPeriode(this.selectedPeriode);

         for(Rubriquepaie rs : var7) {
            ((ModelClass.tmRubriquePaie)this.rubriquePaieTable.getModel()).addRow(rs);
            if (rs.getRubrique().getSens().equalsIgnoreCase("G")) {
               if (rs.isFixe()) {
                  totalGainsFix = totalGainsFix + rs.getMontant();
               } else {
                  totalGainsVar = totalGainsVar + rs.getMontant();
               }
            } else {
               totalRetenues = totalRetenues + rs.getMontant();
            }
         }
      }

      this.rubriquePaieTable.getColumnModel().getColumn(0).setPreferredWidth(10);
      this.rubriquePaieTable.getColumnModel().getColumn(1).setPreferredWidth(320);
      this.rubriquePaieTable.getColumnModel().getColumn(2).setPreferredWidth(30);
      this.rubriquePaieTable.getColumnModel().getColumn(3).setPreferredWidth(30);
      this.rubriquePaieTable.getColumnModel().getColumn(4).setPreferredWidth(40);
      this.rubriquePaieTable.getColumnModel().getColumn(5).setPreferredWidth(10);
      this.rubriquePaieTable.getColumnModel().getColumn(6).setPreferredWidth(50);
      this.rubriquePaieTable.getColumnModel().getColumn(7).setPreferredWidth(2);
      this.rubriquePaieTable.getColumnModel().getColumn(8).setPreferredWidth(0);
      this.rubriquePaieTable.getColumnModel().getColumn(8).setResizable(false);
      TableColumn var8 = this.rubriquePaieTable.getColumnModel().getColumn(7);
      var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var8.setCellRenderer(new ModelClass.ColorCellRenderer(var10003));
      this.rubriquePaieTable.setRowHeight(20);
      this.tTotalGainsFix.setText(this.menu.nf.format(totalGainsFix));
      this.tTotalGainsVar.setText(this.menu.nf.format(totalGainsVar));
      this.tTotalRetenues.setText(this.menu.nf.format(totalRetenues));
   }

   private void afficherHistoriqueRetAE() {
      JTable var10000 = this.historiqueRetAETable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmHistoRetAE(var10003));
      this.historiqueRetAETable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      if (this.selectedOne != null && this.selectedRAE != null) {
         new ArrayList();
         Query q = this.menu.entityManager.createQuery("Select p from Tranchesretenuesaecheances p where p.retenuesaecheances.id = " + this.selectedRAE.getId());
         q.setMaxResults(1000000);
         List var1 = q.getResultList();

         for(Tranchesretenuesaecheances rs : (List)var1.stream().sorted(Comparator.comparing(Tranchesretenuesaecheances::getPeriode)).collect(Collectors.toList())) {
            ((ModelClass.tmHistoRetAE)this.historiqueRetAETable.getModel()).addRow(rs);
         }
      }

      this.historiqueRetAETable.setRowHeight(20);
      this.historiqueRetAETable.getColumnModel().getColumn(0).setPreferredWidth(80);
   }

   private void afficherRetenuesAE() {
      JTable var10000 = this.retAETable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmRetAE(var10003));
      this.retAETable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      if (this.selectedOne != null) {
         new ArrayList();
         Query q = this.menu.entityManager.createQuery("Select p from Retenuesaecheances p where p.employe.id = " + this.selectedOne.getId());
         q.setMaxResults(1000000);
         List var1 = q.getResultList();

         for(Retenuesaecheances rs : (List)var1.stream().sorted(Comparator.comparing((var0) -> var0.getDateAccord())).collect(Collectors.toList())) {
            ((ModelClass.tmRetAE)this.retAETable.getModel()).addRow(rs);
         }

         this.retAETable.setRowHeight(30);
         this.retAETable.getColumnModel().getColumn(0).setPreferredWidth(60);
         this.retAETable.getColumnModel().getColumn(1).setPreferredWidth(100);
         this.retAETable.getColumnModel().getColumn(2).setPreferredWidth(100);
         this.retAETable.getColumnModel().getColumn(6).setPreferredWidth(30);
         this.retAETable.getColumnModel().getColumn(7).setPreferredWidth(30);
         this.retAETable.getColumnModel().getColumn(8).setPreferredWidth(2);
      }

      this.afficherHistoriqueRetAE();
   }

   private void afficherNJT() {
      if (this.selectedOne != null && this.selectedMotif != null && this.selectedPeriode != null) {
         double njt = this.menu.fx.F01_NJT(this.selectedOne, this.selectedMotif, this.selectedPeriode);
         this.tNjt.setValue(njt);
      } else {
         this.tNjt.setValue((double)0.0F);
      }

   }

   public void afficherListeDocuments() {
      this.listDocuments.setModel(new tmDocuments(this));
      this.listDocuments.setDefaultRenderer(Object.class, new CustomTableRenderer());
      new ArrayList();
      if (this.selectedOne != null) {
         Query q = this.menu.entityManager.createQuery("Select p from Document p where p.employe.id = " + this.selectedOne.getId());
         q.setMaxResults(1000000);

         for(Document o : q.getResultList()) {
            ((tmDocuments)this.listDocuments.getModel()).addRow(o);
         }

         this.listDocuments.getColumnModel().getColumn(0).setPreferredWidth(300);
         this.listDocuments.getColumnModel().getColumn(1).setPreferredWidth(5);
         this.listDocuments.getColumnModel().getColumn(2).setPreferredWidth(0);
         this.listDocuments.getColumnModel().getColumn(2).setResizable(false);
         this.listDocuments.setRowHeight(30);
         this.listDocuments.setAutoCreateRowSorter(true);
         TableRowSorter<TableModel> sorter = new TableRowSorter(this.listDocuments.getModel());
         this.listDocuments.setRowSorter(sorter);
         sorter.setSortsOnUpdates(true);
      }

   }

   private void clearFields() {
      this.menu.viewMessage(this.msgLabel, "", false);
      this.lbPhoto.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.IMAGE, 155.0F, new Color(238, 238, 238), new Color(238, 238, 238)));
      this.btnExcelCumul_Conges.setEnabled(false);
      long lastID = this.dataListInit != null ? (long)(Integer)this.dataListInit.stream().map((var0) -> var0.getId()).max(Integer::compareTo).orElse(0) : 0L;
      this.detailPanel.setSelectedIndex(0);
      this.pIdentite_TabbedPane.setSelectedIndex(0);
      this.tId.setValue(lastID + 1L);
      this.btnSave.setEnabled(true);
      this.btnDelete.setEnabled(false);
      this.btnInfosPaie.setEnabled(true);
      this.tRubriqueRetAE.setEnabled(true);
      this.btnInfosPaie.setEnabled(true);
      this.btnCalPaie.setEnabled(true);
      this.btnPrintPaie.setEnabled(true);
      this.selectedOne = null;
      this.selectedOne = null;
      this.selectedRAE = null;
      this.tNjt.setValue(0);
      this.tBase.setValue(0);
      this.tNombre.setValue(0);
      this.tMontant.setValue(0);
      this.cFixe.setSelected(false);
      this.salarieLabel.setText("");
      this.cActif.setSelected(true);
      this.tNoCarteSejour.setText("");
      this.tBudgetAnuel.setValue(0);
      this.tId.setEnabled(true);
      this.tAdresse.setText("");
      this.tDateEPassport.setDate(new Date());
      this.tDateEPermiTravail.setDate(new Date());
      this.tDateEVisa.setDate(new Date());
      this.tDateLPassport.setDate(new Date());
      this.tDateLPermiTravail.setDate(new Date());
      this.tDateLVisa.setDate(new Date());
      this.tDateNaiss.setDate(new Date());
      this.tEmail.setText("");
      this.tIDSalariePointeuse.setValue(0);
      this.tIdPsservice.setText("");
      this.tMere.setText("");
      this.tNNI.setText("");
      this.tNationalite.setText("");
      this.tNbEnfants.setValue(0);
      this.tNoPassport.setText("");
      this.tNoPermiTravail.setText("");
      this.tNom.setText("");
      this.tPrenom.setText("");
      this.tNoteSurBulletin.setText("");
      this.tPere.setText("");
      this.tTelephone.setText("");
      this.tEnfantID.setValue(0);
      this.tIdPsservice.setText("0");
      this.tCumul12DMinitial.setValue(0);
      this.tCumulBrutImposableInitial.setValue(0);
      this.tCumulBrutNonImposableInitial.setValue(0);
      this.tDateCNSS.setDate(new Date());
      this.tDateAnciennete.setDate(new Date());
      this.tDateCategorie.setDate(new Date());
      this.tDateEmbauche.setDate(new Date());
      this.tDateFinContrat.setDate(new Date());
      this.tDernierDepartInitial.setDate(new Date());
      this.tHeureSemaine.setValue(40);
      this.tLieuTravail.setText("");
      this.tNbAnneesCat.setValue(0);
      this.tNbMoisPreavis.setValue(0);
      this.tNoCNAM.setText("");
      this.tNoCNSS.setText("");
      this.tNoCompteBanque.setText("");
      this.tStrictureOrigine.setText("");
      this.tTauxPSRA.setValue(0);
      this.tTauxRembCNAM.setValue(0);
      this.tTauxRembCNSS.setValue(0);
      this.tauxRembITStranche1.setValue(0);
      this.tauxRembITStranche2.setValue(0);
      this.tauxRembITStranche3.setValue(0);
      this.cAvancmentAutoCat.setSelected(false);
      this.cDetacheCNAM.setSelected(false);
      this.cDetacheCNSS.setSelected(false);
      this.cDomicilie.setSelected(false);
      this.cExonoreITS.setSelected(false);
      this.tDateCNSS.setDate(new Date());
      this.tRubriqueRetAE.setEnabled(true);
      this.tDateAccord.setDate(new Date());
      this.cActiveRetAE.setSelected(true);
      this.tCapital.setValue(0);
      this.tCapital.setEditable(true);
      this.tEcheance.setValue(0);
      this.tNoteRetAE.setText("");
      this.tHeuresJour.setValue(0);
      this.tHeuresNuit.setValue(0);
      this.tPrimesPanier.setValue(0);
      this.tTotHJ.setValue(0);
      this.tTotHN.setValue(0);
      this.tTotHS115.setValue(0);
      this.tTotHS140.setValue(0);
      this.tTotHS150.setValue(0);
      this.tTotHS200.setValue(0);
      this.tTotHS.setValue(0);
      this.tTotPP.setValue(0);
      this.tTotPE.setValue(0);
      this.tWeekOT115.setValue(0);
      this.tWeekOT140.setValue(0);
      this.tWeekOT150.setValue(0);
      this.tWeekOT200.setValue(0);
      this.tWeekPP.setValue(0);
      this.tWeekPE.setValue(0);
      this.pIdentite_TabbedPane.setEnabledAt(1, false);
      this.pIdentite_TabbedPane.setEnabledAt(2, false);
      this.pIdentite_TabbedPane.setEnabledAt(3, false);
      this.detailPanel.setEnabledAt(2, false);
      this.detailPanel.setEnabledAt(3, false);
      this.detailPanel.setEnabledAt(4, false);
   }

   private void clearRAE() {
      this.selectedRAE = null;
      this.tRubriqueRetAE.setEnabled(true);
      this.tDateAccord.setDate(new Date());
      this.cActiveRetAE.setSelected(true);
      this.tCapital.setValue(0);
      this.tCapital.setEditable(true);
      this.tEcheance.setValue(0);
      this.tNoteRetAE.setText("");
      this.lbEncours.setText("");
      this.lbPeriodeInit.setText("");
      this.lbECCNG.setText("");
      this.lbECCNG.setText("");
   }

   private String validateData_Salary() {
      String errMsg = "";
      if (this.tId.getText().isEmpty()) {
         errMsg = "ID du salari\u00e9 obligatoire";
         this.tId.requestFocus();
      }

      if (this.tNom.getText().isEmpty()) {
         errMsg = "Pr\u00e9nom du salari\u00e9 obligatoire";
         this.tNom.requestFocus();
      }

      if (this.tPrenom.getText().isEmpty()) {
         errMsg = "Nom du salari\u00e9 obligatoire";
         this.tPrenom.requestFocus();
      }

      if (this.tDateEmbauche.getDate().toString().isEmpty()) {
         errMsg = "Date de recrutement obligatoire";
         this.tDateEmbauche.requestFocus();
      }

      if (this.tDateAnciennete.getDate().toString().isEmpty()) {
         errMsg = "Date d'anciennet\u00e9 obligatoire";
         this.tDateAnciennete.requestFocus();
      }

      return errMsg;
   }

   private String validateDataEnf() {
      String errMsg = "";
      if (this.tNomEnfant.getText().isEmpty()) {
         errMsg = "Pr\u00e9nom de l'enfant obligatoire";
         this.tNomEnfant.requestFocus();
      }

      if (this.tPereMereEnf.getText().isEmpty()) {
         errMsg = "Nom du p\u00e8re/m\u00e8re de l'enfant obligatoire";
         this.tPereMereEnf.requestFocus();
      }

      if (this.tDateNaissEnf.getDate().toString().isEmpty()) {
         errMsg = "Date de naissance de l'enfant obligatoire";
         this.tDateNaissEnf.requestFocus();
      }

      return errMsg;
   }

   private String validateDataDiplome() {
      String errMsg = "";
      if (this.tNomDiplome.getText().isEmpty()) {
         errMsg = "Nom du dipl\u00f4me obligatoire";
         this.tNomDiplome.requestFocus();
      }

      if (this.tDateObtDiplome.getDate().toString().isEmpty()) {
         errMsg = "Date d'obtention du dipl\u00f4me obligatoire";
         this.tDateObtDiplome.requestFocus();
      }

      return errMsg;
   }

   private void showMsgDialog(Object[] var1) {
      JOptionPane.showMessageDialog(this, tab, " Infos paie courante ", 1);
   }

   private void setEdit() {
      Rubrique rubrique = (Rubrique)this.tRubriques.getSelectedItem();
      if (rubrique != null) {
         double base = (double)0.0F;
         double nombre = (double)1.0F;
         if (this.selectedOne != null) {
            if (rubrique.isBaseAuto()) {
               base = this.menu.pc.baseRbrique(this.menu.paramsGen.getPeriodeCourante(), rubrique, this.selectedOne, this.selectedMotif);
            }

            if (rubrique.isNombreAuto()) {
               nombre = this.menu.pc.nombreRbrique(this.menu.paramsGen.getPeriodeCourante(), rubrique, this.selectedOne, this.selectedMotif);
            }
         }

         this.tBase.setValue(base);
         this.tNombre.setValue(nombre);
         this.calMontant();
         if (rubrique.isNombreAuto()) {
            this.tNombre.setEditable(false);
         } else {
            this.tNombre.setEditable(true);
         }

         if (rubrique.isBaseAuto()) {
            this.tBase.setEditable(false);
         } else {
            this.tBase.setEditable(true);
         }
      }

   }

   private void calMontant() {
      double base = (double)0.0F;
      double nombre = (double)0.0F;
      if (this.tNombre.getValue() != null && this.tBase.getValue() != null) {
         nombre = ((Number)this.tNombre.getValue()).doubleValue();
         base = ((Number)this.tBase.getValue()).doubleValue();
      }

      double mnt = nombre * base;
      this.tMontant.setValue(mnt);
   }

   private void findByID(int var1, boolean var2) {
      Thread t = new 1(this, id, refresh);
      t.start();
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.msgLabel = new JLabel();
      this.pnlBody = new JPanel();
      this.progressBar = new JProgressBar();
      this.jTabbedPane1 = new JTabbedPane();
      this.pList = new JPanel();
      this.jScrollPane9 = new JScrollPane();
      this.listTable = new JTable();
      this.searchField = new JTextField();
      this.lbSearch = new JLabel();
      this.lbNbSalariesEnConges = new JLabel();
      this.lbNbSalariesActifs = new JLabel();
      this.lbNbSalaries = new JLabel();
      this.cPersonnelActif = new JCheckBox();
      this.btnRefresh = new JButton();
      this.btnNew2 = new JButton();
      this.pDetail = new JPanel();
      this.detailPanel = new JTabbedPane();
      this.pIdentite = new JPanel();
      this.pIdentite_TabbedPane = new JTabbedPane();
      this.pDonneesId = new JPanel();
      this.jLabel33 = new JLabel();
      this.tId = new JFormattedTextField();
      this.jLabel34 = new JLabel();
      this.jLabel35 = new JLabel();
      this.tIDSalariePointeuse = new JFormattedTextField();
      this.jLabel14 = new JLabel();
      this.tPrenom = new JTextField();
      this.jLabel15 = new JLabel();
      this.tNom = new JTextField();
      this.cActif = new JCheckBox();
      this.jLabel16 = new JLabel();
      this.tMere = new JTextField();
      this.jLabel17 = new JLabel();
      this.tPere = new JTextField();
      this.jLabel36 = new JLabel();
      this.tDateNaiss = new JDateChooser();
      this.tIdPsservice = new JTextField();
      this.jLabel19 = new JLabel();
      this.tNationalite = new JTextField();
      this.jLabel37 = new JLabel();
      this.tZoneOrigine = new JComboBox();
      this.jLabel38 = new JLabel();
      this.tSexe = new JComboBox();
      this.jLabel39 = new JLabel();
      this.tSituationFam = new JComboBox();
      this.jLabel40 = new JLabel();
      this.tNbEnfants = new JFormattedTextField();
      this.jLabel43 = new JLabel();
      this.tDateLPassport = new JDateChooser();
      this.jLabel44 = new JLabel();
      this.tDateEPassport = new JDateChooser();
      this.jLabel46 = new JLabel();
      this.tDateLVisa = new JDateChooser();
      this.jLabel47 = new JLabel();
      this.tDateEVisa = new JDateChooser();
      this.jLabel49 = new JLabel();
      this.tDateLPermiTravail = new JDateChooser();
      this.jLabel50 = new JLabel();
      this.tDateEPermiTravail = new JDateChooser();
      this.cExpatrie = new JCheckBox();
      this.jLabel20 = new JLabel();
      this.tLieuNaiss = new JTextField();
      this.jLabel21 = new JLabel();
      this.tNNI = new JTextField();
      this.jLabel22 = new JLabel();
      this.tNoPassport = new JTextField();
      this.jLabel61 = new JLabel();
      this.tNoCarteSejour = new JTextField();
      this.jLabel62 = new JLabel();
      this.tNoPermiTravail = new JTextField();
      this.tTelephone = new JTextField();
      this.jLabel63 = new JLabel();
      this.jLabel64 = new JLabel();
      this.tEmail = new JTextField();
      this.jLabel65 = new JLabel();
      this.tAdresse = new JTextField();
      this.jPanel14 = new JPanel();
      this.lbPhoto = new JLabel();
      this.pEnfants = new JPanel();
      this.jScrollPane15 = new JScrollPane();
      this.listTableEnfant = new JTable();
      this.jPanel23 = new JPanel();
      this.jLabel104 = new JLabel();
      this.tNomEnfant = new JTextField();
      this.btnNewEnf = new JButton();
      this.btnSaveEnf = new JButton();
      this.btnDelEnf = new JButton();
      this.tEnfantID = new JFormattedTextField();
      this.jLabel107 = new JLabel();
      this.tPereMereEnf = new JTextField();
      this.jLabel118 = new JLabel();
      this.tDateNaissEnf = new JDateChooser();
      this.jLabel119 = new JLabel();
      this.tGenreEnf = new JComboBox();
      this.pDiplomes = new JPanel();
      this.jPanel24 = new JPanel();
      this.jLabel105 = new JLabel();
      this.tNomDiplome = new JTextField();
      this.btnNewDiplome = new JButton();
      this.btnSaveDiplome = new JButton();
      this.btnDelDiplome = new JButton();
      this.tDiplomeID = new JFormattedTextField();
      this.jLabel113 = new JLabel();
      this.tDomaineDiplome = new JTextField();
      this.jLabel120 = new JLabel();
      this.tDateObtDiplome = new JDateChooser();
      this.jLabel103 = new JLabel();
      this.tDegreDiplome = new JTextField();
      this.jLabel114 = new JLabel();
      this.tEtablissementDiplome = new JTextField();
      this.jScrollPane16 = new JScrollPane();
      this.listTableDiplome = new JTable();
      this.pDocuments = new JPanel();
      this.jPanel29 = new JPanel();
      this.jLabel123 = new JLabel();
      this.tNomDocument = new JTextField();
      this.jSeparator77 = new JSeparator();
      this.btnNewDoc = new JButton();
      this.btnSaveDoc = new JButton();
      this.btnDelDoc = new JButton();
      this.btnSearchDoc = new JButton();
      this.tDestFile = new JTextField();
      this.lbFilePath = new JTextField();
      this.jScrollPane19 = new JScrollPane();
      this.listDocuments = new JTable();
      this.pContrat = new JPanel();
      this.pContrat_TabbedPane = new JTabbedPane();
      this.pDonneesPro = new JPanel();
      this.jLabel56 = new JLabel();
      this.tDepartement = new JComboBox();
      this.jLabel57 = new JLabel();
      this.tActivite = new JComboBox();
      this.jLabel58 = new JLabel();
      this.tPoste = new JComboBox();
      this.jLabel59 = new JLabel();
      this.tBanque = new JComboBox();
      this.jLabel60 = new JLabel();
      this.tCategorie = new JComboBox();
      this.jLabel66 = new JLabel();
      this.tClassification = new JTextField();
      this.jLabel67 = new JLabel();
      this.tStatut = new JTextField();
      this.jLabel45 = new JLabel();
      this.tDateDebauche = new JDateChooser();
      this.jLabel68 = new JLabel();
      this.tModePaiement = new JComboBox();
      this.jLabel69 = new JLabel();
      this.tDirectiongeneral = new JComboBox();
      this.jLabel70 = new JLabel();
      this.tDirection = new JComboBox();
      this.cPsservice = new JCheckBox();
      this.jLabel71 = new JLabel();
      this.tRaisonDebauche = new JTextField();
      this.jLabel31 = new JLabel();
      this.tBudgetAnuel = new JFormattedTextField();
      this.tNoCompteBanque = new JTextField();
      this.jLabel72 = new JLabel();
      this.cDomicilie = new JCheckBox();
      this.jLabel73 = new JLabel();
      this.tTypeContrat = new JComboBox();
      this.jLabel48 = new JLabel();
      this.tDateEmbauche = new JDateChooser();
      this.jLabel51 = new JLabel();
      this.tDateAnciennete = new JDateChooser();
      this.jLabel41 = new JLabel();
      this.tTauxAnc = new JFormattedTextField();
      this.jLabel52 = new JLabel();
      this.tDateFinContrat = new JDateChooser();
      this.jLabel74 = new JLabel();
      this.tLieuTravail = new JTextField();
      this.buttonConges1 = new JButton();
      this.jLabel53 = new JLabel();
      this.tDateCategorie = new JDateChooser();
      this.appSBCatButton = new JButton();
      this.cAvancmentAutoCat = new JCheckBox();
      this.jLabel42 = new JLabel();
      this.tNbAnneesCat = new JFormattedTextField();
      this.jLabel54 = new JLabel();
      this.tHeureSemaine = new JFormattedTextField();
      this.jLabel55 = new JLabel();
      this.tNbMoisPreavis = new JFormattedTextField();
      this.jLabel75 = new JLabel();
      this.tTauxPSRA = new JFormattedTextField();
      this.pTaxes = new JPanel();
      this.jPanel16 = new JPanel();
      this.cDetacheCNAM = new JCheckBox();
      this.jLabel76 = new JLabel();
      this.tNoCNAM = new JTextField();
      this.jLabel77 = new JLabel();
      this.tDateDebauche3 = new JDateChooser();
      this.jLabel78 = new JLabel();
      this.tTauxRembCNAM = new JFormattedTextField();
      this.jPanel17 = new JPanel();
      this.cDetacheCNSS = new JCheckBox();
      this.jLabel80 = new JLabel();
      this.tNoCNSS = new JTextField();
      this.jLabel81 = new JLabel();
      this.tDateCNSS = new JDateChooser();
      this.jLabel82 = new JLabel();
      this.tTauxRembCNSS = new JFormattedTextField();
      this.jLabel83 = new JLabel();
      this.tStrictureOrigine = new JTextField();
      this.jPanel18 = new JPanel();
      this.cExonoreITS = new JCheckBox();
      this.jLabel85 = new JLabel();
      this.tauxRembITStranche3 = new JFormattedTextField();
      this.jLabel86 = new JLabel();
      this.tauxRembITStranche2 = new JFormattedTextField();
      this.jLabel87 = new JLabel();
      this.tauxRembITStranche1 = new JFormattedTextField();
      this.jPanel3 = new JPanel();
      this.jLabel79 = new JLabel();
      this.tDernierDepartInitial = new JDateChooser();
      this.jLabel84 = new JLabel();
      this.tCumulBrutImposableInitial = new JFormattedTextField();
      this.tCumulBrutNonImposableInitial = new JFormattedTextField();
      this.jLabel88 = new JLabel();
      this.jLabel89 = new JLabel();
      this.tCumul12DMinitial = new JFormattedTextField();
      this.jPanel15 = new JPanel();
      this.jLabel6 = new JLabel();
      this.cLUNwe = new JCheckBox();
      this.cMARwe = new JCheckBox();
      this.cDIMEnd = new JCheckBox();
      this.cSAMEnd = new JCheckBox();
      this.cJEUwe = new JCheckBox();
      this.cVENwe = new JCheckBox();
      this.cMAREnd = new JCheckBox();
      this.cMEREnd = new JCheckBox();
      this.cJEUEnd = new JCheckBox();
      this.cVENEnd = new JCheckBox();
      this.cMERwe = new JCheckBox();
      this.cSAMwe = new JCheckBox();
      this.cLUNBegin = new JCheckBox();
      this.cMARBegin = new JCheckBox();
      this.cMERBegin = new JCheckBox();
      this.cJEUBegin = new JCheckBox();
      this.cVENBegin = new JCheckBox();
      this.cSAMBegin = new JCheckBox();
      this.cDIMBegin = new JCheckBox();
      this.cLUNEnd = new JCheckBox();
      this.cDIMwe = new JCheckBox();
      this.jLabel8 = new JLabel();
      this.jLabel9 = new JLabel();
      this.jLabel11 = new JLabel();
      this.pPaie = new JPanel();
      this.pPaie_TabbedPane = new JTabbedPane();
      this.pVariables = new JPanel();
      this.jPanel11 = new JPanel();
      this.jLabel23 = new JLabel();
      this.tMotif = new JComboBox();
      this.cOnMotif = new JCheckBox();
      this.tPaieDu = new JDateChooser();
      this.tPaieAu = new JDateChooser();
      this.jLabel24 = new JLabel();
      this.jLabel25 = new JLabel();
      this.jLabel26 = new JLabel();
      this.tNjt = new JFormattedTextField();
      this.btnSaveNJT = new JButton();
      this.buttonConges = new JButton();
      this.jPanel12 = new JPanel();
      this.jScrollPane10 = new JScrollPane();
      this.rubriquePaieTable = new JTable();
      this.jLabel27 = new JLabel();
      this.tRubriques = new JComboBox();
      this.jLabel28 = new JLabel();
      this.tBase = new JFormattedTextField();
      this.jLabel29 = new JLabel();
      this.tNombre = new JFormattedTextField();
      this.tMontant = new JFormattedTextField();
      this.jLabel30 = new JLabel();
      this.cFixe = new JCheckBox();
      this.btnSavePaie = new JButton();
      this.btnDelPaie = new JButton();
      this.jPanel13 = new JPanel();
      this.tNoteSurBulletin = new JTextField();
      this.jLabel13 = new JLabel();
      this.tBARelicat = new JFormattedTextField();
      this.jSeparator17 = new JSeparator();
      this.jLabel32 = new JLabel();
      this.btnSaveEmpNote = new JButton();
      this.tTotalGainsFix = new JLabel();
      this.tTotalGainsVar = new JLabel();
      this.tTotalRetenues = new JLabel();
      this.jPanel10 = new JPanel();
      this.btnPrintPaie = new JButton();
      this.btnCalPaie = new JButton();
      this.btnInfosPaie = new JButton();
      this.rappelSBPanel = new JPanel();
      this.panelHS1 = new JTabbedPane();
      this.jPanel27 = new JPanel();
      this.tDebutRappel = new JDateChooser();
      this.jLabel121 = new JLabel();
      this.jLabel125 = new JLabel();
      this.tMotifRappel = new JComboBox();
      this.jLabel154 = new JLabel();
      this.tPeriodeRappel = new JComboBox();
      this.jPanel28 = new JPanel();
      this.jLabel153 = new JLabel();
      this.tIDRubRappel = new JFormattedTextField();
      this.jScrollPane18 = new JScrollPane();
      this.rubriquesSurSBTable = new JTable();
      this.jScrollPane17 = new JScrollPane();
      this.rappelTable = new JTable();
      this.jLabel122 = new JLabel();
      this.tMontantRappel = new JFormattedTextField();
      this.btnSaveRappelSB = new JButton();
      this.jLabel144 = new JLabel();
      this.pointageHSPanel = new JPanel();
      this.panelHS = new JTabbedPane();
      this.jPanel25 = new JPanel();
      this.tDateJour = new JDateChooser();
      this.jLabel106 = new JLabel();
      this.cFerie50 = new JCheckBox();
      this.jLabel117 = new JLabel();
      this.tNoteJour = new JTextField();
      this.btnSaveHSDay = new JButton();
      this.btnDelHSDay = new JButton();
      this.jLabel134 = new JLabel();
      this.tPrimesPanier = new JFormattedTextField();
      this.jLabel135 = new JLabel();
      this.tHeuresNuit = new JFormattedTextField();
      this.jSeparator92 = new JSeparator();
      this.cFerie100 = new JCheckBox();
      this.cSiteExterne = new JCheckBox();
      this.jLabel136 = new JLabel();
      this.tHeuresJour = new JFormattedTextField();
      this.jSeparator93 = new JSeparator();
      this.jPanel26 = new JPanel();
      this.jLabel116 = new JLabel();
      this.tBeginWeek = new JDateChooser();
      this.jLabel137 = new JLabel();
      this.tEndWeek = new JDateChooser();
      this.jLabel138 = new JLabel();
      this.jLabel139 = new JLabel();
      this.tWeekOT200 = new JFormattedTextField();
      this.tWeekOT150 = new JFormattedTextField();
      this.tWeekOT140 = new JFormattedTextField();
      this.tWeekOT115 = new JFormattedTextField();
      this.jLabel140 = new JLabel();
      this.jLabel141 = new JLabel();
      this.jLabel142 = new JLabel();
      this.tWeekPP = new JFormattedTextField();
      this.jLabel143 = new JLabel();
      this.tWeekPE = new JFormattedTextField();
      this.btnDelHSWeek = new JButton();
      this.btnSaveHSWeek = new JButton();
      this.jScrollPane14 = new JScrollPane();
      this.heuresTable = new JTable();
      this.pnlActivites_Btn1 = new JPanel();
      this.jLabel126 = new JLabel();
      this.tTotPP = new JFormattedTextField();
      this.jLabel127 = new JLabel();
      this.tTotHS = new JFormattedTextField();
      this.tTotHS200 = new JFormattedTextField();
      this.jLabel128 = new JLabel();
      this.tTotHS150 = new JFormattedTextField();
      this.jLabel129 = new JLabel();
      this.tTotHS140 = new JFormattedTextField();
      this.jLabel130 = new JLabel();
      this.tTotHS115 = new JFormattedTextField();
      this.jLabel131 = new JLabel();
      this.tTotHJ = new JFormattedTextField();
      this.jLabel132 = new JLabel();
      this.tTotHN = new JFormattedTextField();
      this.jLabel133 = new JLabel();
      this.cF50Auto = new JCheckBox();
      this.cPPAuto = new JCheckBox();
      this.tModeDecompte = new JComboBox();
      this.jLabel115 = new JLabel();
      this.jLabel124 = new JLabel();
      this.tTotPE = new JFormattedTextField();
      this.btnValoriserHS = new JButton();
      this.pConges = new JPanel();
      this.jTabbedPane2 = new JTabbedPane();
      this.jPanel19 = new JPanel();
      this.tDateDepart = new JDateChooser();
      this.jLabel95 = new JLabel();
      this.tDateReprise = new JDateChooser();
      this.jLabel96 = new JLabel();
      this.tRepriseEff = new JDateChooser();
      this.jLabel97 = new JLabel();
      this.cDroitsCongesAuto = new JCheckBox();
      this.jLabel98 = new JLabel();
      this.tNoteConges = new JTextField();
      this.btnNewConges = new JButton();
      this.btnSaveConges = new JButton();
      this.btnDelConges = new JButton();
      this.jPanel20 = new JPanel();
      this.jLabel90 = new JLabel();
      this.jLabel91 = new JLabel();
      this.tCumulBrutImposable = new JFormattedTextField();
      this.jLabel92 = new JLabel();
      this.tCumulBrutNonImposable = new JFormattedTextField();
      this.jLabel93 = new JLabel();
      this.tCumul12DM = new JFormattedTextField();
      this.jLabel94 = new JLabel();
      this.tCumulRet = new JFormattedTextField();
      this.btnExcelCumul_Conges = new JButton();
      this.tDernierDepart = new JDateChooser();
      this.jScrollPane11 = new JScrollPane();
      this.congesTable = new JTable();
      this.pEngagement = new JPanel();
      this.jTabbedPane3 = new JTabbedPane();
      this.jPanel21 = new JPanel();
      this.tDateAccord = new JDateChooser();
      this.jLabel99 = new JLabel();
      this.cActiveRetAE = new JCheckBox();
      this.jLabel102 = new JLabel();
      this.tNoteRetAE = new JTextField();
      this.btnNewEng = new JButton();
      this.btnSaveEng = new JButton();
      this.btnDelEng = new JButton();
      this.jLabel100 = new JLabel();
      this.tRubriqueRetAE = new JComboBox();
      this.jLabel101 = new JLabel();
      this.tCapital = new JFormattedTextField();
      this.jLabel108 = new JLabel();
      this.tEcheance = new JFormattedTextField();
      this.jLabel109 = new JLabel();
      this.lbEncours = new JFormattedTextField();
      this.jLabel110 = new JLabel();
      this.lbPeriodeInit = new JFormattedTextField();
      this.jLabel111 = new JLabel();
      this.lbECCNG = new JFormattedTextField();
      this.jLabel112 = new JLabel();
      this.tTauxAnc4 = new JFormattedTextField();
      this.btnAppEng = new JButton();
      this.jPanel22 = new JPanel();
      this.jScrollPane13 = new JScrollPane();
      this.historiqueRetAETable = new JTable();
      this.tTotReg = new JFormattedTextField();
      this.jScrollPane12 = new JScrollPane();
      this.retAETable = new JTable();
      this.salarieLabel = new JLabel();
      this.tPeriode = new JComboBox();
      this.btnRefreshItem = new JButton();
      this.btnSave = new JButton();
      this.btnNew = new JButton();
      this.btnDelete = new JButton();
      this.setBackground(new Color(255, 255, 255));
      this.setBorder((Border)null);
      this.setAutoscrolls(true);
      this.setFont(new Font("Segoe UI Light", 0, 12));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(1000, 600));
      this.jPanel1.setBackground(new Color(255, 255, 255));
      this.jLabel7.setFont(new Font("Segoe UI Light", 0, 24));
      this.jLabel7.setForeground(new Color(0, 102, 153));
      this.jLabel7.setText("Salari\u00e9s");
      this.jLabel7.setToolTipText("");
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 2(this));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 135, -2).addGap(28, 28, 28).addComponent(this.msgLabel, -2, 885, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnExit, -2, 39, -2).addGap(16, 16, 16)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -2, 37, -2).addComponent(this.msgLabel, -2, 31, -2));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.pnlBody.setLayout(new AbsoluteLayout());
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      this.pnlBody.add(this.progressBar, new AbsoluteConstraints(0, 0, 1352, -1));
      this.jTabbedPane1.setForeground(new Color(0, 102, 153));
      this.jTabbedPane1.setFont(new Font("Segoe UI Light", 1, 12));
      this.pList.setBackground(new Color(255, 255, 255));
      this.listTable.setFont(new Font("Segoe UI Light", 0, 12));
      this.listTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable.setSelectionBackground(new Color(0, 102, 153));
      this.listTable.setShowGrid(false);
      this.listTable.addMouseListener(new 3(this));
      this.jScrollPane9.setViewportView(this.listTable);
      this.searchField.setFont(new Font("Segoe UI Light", 1, 12));
      this.searchField.setBorder(BorderFactory.createEtchedBorder());
      this.searchField.addCaretListener(new 4(this));
      this.searchField.addKeyListener(new 5(this));
      this.lbSearch.setFont(new Font("Segoe UI Light", 0, 12));
      this.lbSearch.setCursor(new Cursor(12));
      this.lbSearch.addMouseListener(new 6(this));
      this.lbNbSalariesEnConges.setBackground(new Color(255, 204, 0));
      this.lbNbSalariesEnConges.setFont(new Font("Segoe UI Light", 1, 12));
      this.lbNbSalariesEnConges.setHorizontalAlignment(0);
      this.lbNbSalariesEnConges.setText("0");
      this.lbNbSalariesEnConges.setToolTipText("Total salari\u00e9s en cong\u00e9s");
      this.lbNbSalariesEnConges.setOpaque(true);
      this.lbNbSalariesActifs.setBackground(new Color(102, 204, 0));
      this.lbNbSalariesActifs.setFont(new Font("Segoe UI Light", 1, 12));
      this.lbNbSalariesActifs.setHorizontalAlignment(0);
      this.lbNbSalariesActifs.setText("0");
      this.lbNbSalariesActifs.setToolTipText("Totale salari\u00e9s actif");
      this.lbNbSalariesActifs.setOpaque(true);
      this.lbNbSalaries.setBackground(new Color(196, 198, 194));
      this.lbNbSalaries.setFont(new Font("Segoe UI Light", 1, 12));
      this.lbNbSalaries.setHorizontalAlignment(0);
      this.lbNbSalaries.setText("0");
      this.lbNbSalaries.setToolTipText("Total salari\u00e9s");
      this.lbNbSalaries.setOpaque(true);
      this.cPersonnelActif.setBackground(new Color(255, 255, 255));
      this.cPersonnelActif.setFont(new Font("Segoe UI Light", 0, 12));
      this.cPersonnelActif.setForeground(new Color(0, 102, 153));
      this.cPersonnelActif.setSelected(true);
      this.cPersonnelActif.setText("Salari\u00e9s actifs");
      this.cPersonnelActif.addActionListener(new 7(this));
      this.btnRefresh.setToolTipText("Actualiser");
      this.btnRefresh.setContentAreaFilled(false);
      this.btnRefresh.setCursor(new Cursor(12));
      this.btnRefresh.setOpaque(true);
      this.btnRefresh.addActionListener(new 8(this));
      this.btnNew2.setToolTipText("Actualiser");
      this.btnNew2.setContentAreaFilled(false);
      this.btnNew2.setCursor(new Cursor(12));
      this.btnNew2.setOpaque(true);
      this.btnNew2.addActionListener(new 9(this));
      GroupLayout pListLayout = new GroupLayout(this.pList);
      this.pList.setLayout(pListLayout);
      pListLayout.setHorizontalGroup(pListLayout.createParallelGroup(Alignment.LEADING).addGroup(pListLayout.createSequentialGroup().addContainerGap().addGroup(pListLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jScrollPane9, -2, 1328, -2).addGroup(pListLayout.createSequentialGroup().addComponent(this.lbSearch, -2, 31, -2).addGap(0, 0, 0).addComponent(this.searchField, -2, 310, -2).addGap(96, 96, 96).addComponent(this.cPersonnelActif, -2, 130, -2).addGap(248, 248, 248).addComponent(this.lbNbSalaries, -2, 49, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.lbNbSalariesActifs, -2, 49, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.lbNbSalariesEnConges, -2, 49, -2).addGap(18, 18, 18).addComponent(this.btnNew2, -2, 35, -2).addGap(18, 18, 18).addComponent(this.btnRefresh, -2, 35, -2))).addContainerGap(-1, 32767)));
      pListLayout.setVerticalGroup(pListLayout.createParallelGroup(Alignment.LEADING).addGroup(pListLayout.createSequentialGroup().addContainerGap().addGroup(pListLayout.createParallelGroup(Alignment.TRAILING).addGroup(pListLayout.createParallelGroup(Alignment.LEADING).addGroup(pListLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.lbSearch, -1, -1, 32767).addGroup(pListLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.searchField, -2, 30, -2).addComponent(this.lbNbSalariesEnConges, -2, 25, -2).addComponent(this.lbNbSalariesActifs, -2, 25, -2).addComponent(this.lbNbSalaries, -2, 25, -2).addComponent(this.cPersonnelActif))).addComponent(this.btnRefresh, Alignment.TRAILING, -2, 35, -2)).addComponent(this.btnNew2, -2, 35, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane9, -2, 547, -2).addContainerGap(-1, 32767)));
      this.jTabbedPane1.addTab("Liste", this.pList);
      this.pDetail.setBackground(new Color(255, 255, 255));
      this.detailPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      this.detailPanel.setForeground(new Color(0, 102, 153));
      this.detailPanel.setFont(new Font("SansSerif", 1, 11));
      this.pIdentite.setBackground(new Color(255, 255, 255));
      this.pIdentite.setFont(new Font("Segoe UI Light", 0, 12));
      this.pIdentite_TabbedPane.setForeground(new Color(0, 102, 153));
      this.pIdentite_TabbedPane.setFont(new Font("SansSerif", 1, 11));
      this.pDonneesId.setBackground(new Color(255, 255, 255));
      this.pDonneesId.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel33.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel33.setForeground(new Color(0, 102, 153));
      this.jLabel33.setText("ID Salari\u00e9");
      this.tId.setBorder(BorderFactory.createEtchedBorder());
      this.tId.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tId.setHorizontalAlignment(0);
      this.tId.setFont(new Font("Segoe UI Light", 0, 16));
      this.tId.addCaretListener(new 10(this));
      this.tId.addFocusListener(new 11(this));
      this.jLabel34.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel34.setForeground(new Color(0, 102, 153));
      this.jLabel34.setText("Mat. interne");
      this.jLabel35.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel35.setForeground(new Color(0, 102, 153));
      this.jLabel35.setText("ID Pointeuse");
      this.tIDSalariePointeuse.setBorder(BorderFactory.createEtchedBorder());
      this.tIDSalariePointeuse.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tIDSalariePointeuse.setHorizontalAlignment(0);
      this.tIDSalariePointeuse.setFont(new Font("Segoe UI Light", 1, 12));
      this.tIDSalariePointeuse.addCaretListener(new 12(this));
      this.tIDSalariePointeuse.addFocusListener(new 13(this));
      this.jLabel14.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel14.setForeground(new Color(0, 102, 153));
      this.jLabel14.setText("Pr\u00e9nom");
      this.tPrenom.setFont(new Font("Segoe UI Light", 1, 12));
      this.tPrenom.setText("CHEIKH MOHAMED LEMINE");
      this.tPrenom.setBorder(BorderFactory.createEtchedBorder());
      this.tPrenom.addKeyListener(new 14(this));
      this.jLabel15.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel15.setForeground(new Color(0, 102, 153));
      this.jLabel15.setText("Nom");
      this.tNom.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNom.setBorder(BorderFactory.createEtchedBorder());
      this.tNom.addKeyListener(new 15(this));
      this.cActif.setBackground(new Color(255, 255, 255));
      this.cActif.setFont(new Font("Segoe UI Light", 1, 12));
      this.cActif.setForeground(new Color(0, 102, 153));
      this.cActif.setText("Actif");
      this.cActif.addActionListener(new 16(this));
      this.jLabel16.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel16.setForeground(new Color(0, 102, 153));
      this.jLabel16.setText("M\u00e8re");
      this.tMere.setFont(new Font("Segoe UI Light", 1, 12));
      this.tMere.setBorder(BorderFactory.createEtchedBorder());
      this.tMere.addKeyListener(new 17(this));
      this.jLabel17.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel17.setForeground(new Color(0, 102, 153));
      this.jLabel17.setText("P\u00e8re");
      this.tPere.setFont(new Font("Segoe UI Light", 1, 12));
      this.tPere.setBorder(BorderFactory.createEtchedBorder());
      this.tPere.addKeyListener(new 18(this));
      this.jLabel36.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel36.setForeground(new Color(0, 102, 153));
      this.jLabel36.setText("Date de naissance");
      this.tDateNaiss.setBorder(BorderFactory.createEtchedBorder());
      this.tDateNaiss.setDateFormatString("dd/MM/yy");
      this.tDateNaiss.setFont(new Font("Segoe UI Light", 1, 12));
      this.tIdPsservice.setFont(new Font("Segoe UI Light", 1, 12));
      this.tIdPsservice.setHorizontalAlignment(0);
      this.tIdPsservice.setBorder(BorderFactory.createEtchedBorder());
      this.tIdPsservice.addKeyListener(new 19(this));
      this.jLabel19.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel19.setForeground(new Color(0, 102, 153));
      this.jLabel19.setText("Nationalit\u00e9");
      this.tNationalite.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNationalite.setBorder(BorderFactory.createEtchedBorder());
      this.tNationalite.addKeyListener(new 20(this));
      this.jLabel37.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel37.setForeground(new Color(0, 102, 153));
      this.jLabel37.setText("Origine");
      this.tZoneOrigine.setFont(new Font("Segoe UI Light", 1, 12));
      this.tZoneOrigine.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tZoneOrigine.setBorder(BorderFactory.createEtchedBorder());
      this.tZoneOrigine.addActionListener(new 21(this));
      this.jLabel38.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel38.setForeground(new Color(0, 102, 153));
      this.jLabel38.setText("Genre");
      this.tSexe.setFont(new Font("Segoe UI Light", 1, 12));
      this.tSexe.setModel(new DefaultComboBoxModel(new String[]{"F", "M"}));
      this.tSexe.setBorder(BorderFactory.createEtchedBorder());
      this.tSexe.addActionListener(new 22(this));
      this.jLabel39.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel39.setForeground(new Color(0, 102, 153));
      this.jLabel39.setText("Sit. Fam.");
      this.tSituationFam.setFont(new Font("Segoe UI Light", 1, 12));
      this.tSituationFam.setModel(new DefaultComboBoxModel(new String[]{"C", "M", "D", "V"}));
      this.tSituationFam.setBorder(BorderFactory.createEtchedBorder());
      this.tSituationFam.addActionListener(new 23(this));
      this.jLabel40.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel40.setForeground(new Color(0, 102, 153));
      this.jLabel40.setText("Nbr. Enfants");
      this.tNbEnfants.setBorder((Border)null);
      this.tNbEnfants.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tNbEnfants.setHorizontalAlignment(0);
      this.tNbEnfants.setEnabled(false);
      this.tNbEnfants.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNbEnfants.addCaretListener(new 24(this));
      this.tNbEnfants.addFocusListener(new 25(this));
      this.jLabel43.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel43.setForeground(new Color(0, 102, 153));
      this.jLabel43.setText("Date de d\u00e9livrance");
      this.tDateLPassport.setBorder(BorderFactory.createEtchedBorder());
      this.tDateLPassport.setDateFormatString("dd/MM/yy");
      this.tDateLPassport.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel44.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel44.setForeground(new Color(0, 102, 153));
      this.jLabel44.setText("Date d'expiration");
      this.tDateEPassport.setBorder(BorderFactory.createEtchedBorder());
      this.tDateEPassport.setDateFormatString("dd/MM/yy");
      this.tDateEPassport.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel46.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel46.setForeground(new Color(0, 102, 153));
      this.jLabel46.setText("Date de d\u00e9livrance");
      this.tDateLVisa.setBorder(BorderFactory.createEtchedBorder());
      this.tDateLVisa.setDateFormatString("dd/MM/yy");
      this.tDateLVisa.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel47.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel47.setForeground(new Color(0, 102, 153));
      this.jLabel47.setText("Date d'expiration");
      this.tDateEVisa.setBorder(BorderFactory.createEtchedBorder());
      this.tDateEVisa.setDateFormatString("dd/MM/yy");
      this.tDateEVisa.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel49.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel49.setForeground(new Color(0, 102, 153));
      this.jLabel49.setText("Date de d\u00e9livrance");
      this.tDateLPermiTravail.setBorder(BorderFactory.createEtchedBorder());
      this.tDateLPermiTravail.setDateFormatString("dd/MM/yy");
      this.tDateLPermiTravail.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel50.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel50.setForeground(new Color(0, 102, 153));
      this.jLabel50.setText("Date d'expiration");
      this.tDateEPermiTravail.setBorder(BorderFactory.createEtchedBorder());
      this.tDateEPermiTravail.setDateFormatString("dd/MM/yy");
      this.tDateEPermiTravail.setFont(new Font("Segoe UI Light", 1, 12));
      this.cExpatrie.setBackground(new Color(255, 255, 255));
      this.cExpatrie.setFont(new Font("Segoe UI Light", 0, 12));
      this.cExpatrie.setForeground(new Color(0, 102, 153));
      this.cExpatrie.setText("Expatri\u00e9");
      this.cExpatrie.addActionListener(new 26(this));
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("Lieu de naissance");
      this.tLieuNaiss.setFont(new Font("Segoe UI Light", 1, 12));
      this.tLieuNaiss.setBorder(BorderFactory.createEtchedBorder());
      this.tLieuNaiss.addKeyListener(new 27(this));
      this.jLabel21.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel21.setForeground(new Color(0, 102, 153));
      this.jLabel21.setText("NNI");
      this.tNNI.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNNI.setHorizontalAlignment(0);
      this.tNNI.setBorder(BorderFactory.createEtchedBorder());
      this.tNNI.addKeyListener(new 28(this));
      this.jLabel22.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel22.setForeground(new Color(0, 102, 153));
      this.jLabel22.setText("Num. Passport");
      this.tNoPassport.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNoPassport.setHorizontalAlignment(0);
      this.tNoPassport.setBorder(BorderFactory.createEtchedBorder());
      this.tNoPassport.addKeyListener(new 29(this));
      this.jLabel61.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel61.setForeground(new Color(0, 102, 153));
      this.jLabel61.setText("Num. Visa/S\u00e9jour");
      this.tNoCarteSejour.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNoCarteSejour.setHorizontalAlignment(0);
      this.tNoCarteSejour.setBorder(BorderFactory.createEtchedBorder());
      this.tNoCarteSejour.addKeyListener(new 30(this));
      this.jLabel62.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel62.setForeground(new Color(0, 102, 153));
      this.jLabel62.setText("Num.  Permi de travail");
      this.tNoPermiTravail.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNoPermiTravail.setHorizontalAlignment(0);
      this.tNoPermiTravail.setBorder(BorderFactory.createEtchedBorder());
      this.tNoPermiTravail.addKeyListener(new 31(this));
      this.tTelephone.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTelephone.setBorder(BorderFactory.createEtchedBorder());
      this.tTelephone.addKeyListener(new 32(this));
      this.jLabel63.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel63.setForeground(new Color(0, 102, 153));
      this.jLabel63.setText("T\u00e9l\u00e9phone");
      this.jLabel64.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel64.setForeground(new Color(0, 102, 153));
      this.jLabel64.setText("E-mail");
      this.tEmail.setFont(new Font("Segoe UI Light", 1, 12));
      this.tEmail.setBorder(BorderFactory.createEtchedBorder());
      this.tEmail.addKeyListener(new 33(this));
      this.jLabel65.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel65.setForeground(new Color(0, 102, 153));
      this.jLabel65.setText("Adresse");
      this.tAdresse.setFont(new Font("Segoe UI Light", 1, 12));
      this.tAdresse.setBorder(BorderFactory.createEtchedBorder());
      this.tAdresse.addKeyListener(new 34(this));
      this.jPanel14.setBackground(new Color(255, 255, 255));
      this.lbPhoto.setBackground(new Color(255, 255, 255));
      this.lbPhoto.setHorizontalAlignment(0);
      this.lbPhoto.setBorder(BorderFactory.createEtchedBorder());
      this.lbPhoto.setOpaque(true);
      this.lbPhoto.addMouseListener(new 35(this));
      GroupLayout jPanel14Layout = new GroupLayout(this.jPanel14);
      this.jPanel14.setLayout(jPanel14Layout);
      jPanel14Layout.setHorizontalGroup(jPanel14Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel14Layout.createSequentialGroup().addContainerGap().addComponent(this.lbPhoto, -1, 271, 32767).addContainerGap()));
      jPanel14Layout.setVerticalGroup(jPanel14Layout.createParallelGroup(Alignment.LEADING).addComponent(this.lbPhoto, -2, 332, -2));
      GroupLayout pDonneesIdLayout = new GroupLayout(this.pDonneesId);
      this.pDonneesId.setLayout(pDonneesIdLayout);
      pDonneesIdLayout.setHorizontalGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesIdLayout.createSequentialGroup().addContainerGap().addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesIdLayout.createSequentialGroup().addGroup(pDonneesIdLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel33, Alignment.LEADING, -2, 60, -2).addComponent(this.tId, Alignment.LEADING, -2, 60, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel34, -2, 100, -2).addComponent(this.tIdPsservice, -1, 104, 32767)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tIDSalariePointeuse, -2, 70, -2).addComponent(this.jLabel35, -2, 80, -2)).addGap(18, 18, 18).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel14, -2, 150, -2).addGap(0, 0, 32767)).addComponent(this.tPrenom)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel15, -2, 150, -2).addComponent(this.tNom, -2, 205, -2)).addGap(35, 35, 35).addComponent(this.cActif).addGap(48, 48, 48)).addGroup(pDonneesIdLayout.createSequentialGroup().addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tTelephone, -2, 200, -2).addComponent(this.jLabel63, -2, 200, -2)).addGap(31, 31, 31).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tEmail, -2, 250, -2).addComponent(this.jLabel64, -2, 250, -2)).addGap(18, 18, 18).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tAdresse, -2, 430, -2).addComponent(this.jLabel65, -2, 430, -2))).addGroup(pDonneesIdLayout.createSequentialGroup().addGroup(pDonneesIdLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.jLabel38, Alignment.LEADING, -2, 50, -2).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.tSexe, -2, -1, -2).addGap(12, 12, 12)).addComponent(this.cExpatrie, Alignment.LEADING, -2, 84, -2)).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesIdLayout.createSequentialGroup().addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel39, -2, 50, -2).addComponent(this.tSituationFam, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tNbEnfants, -2, 70, -2).addComponent(this.jLabel40)).addGap(30, 30, 30).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tNNI, -2, 160, -2).addComponent(this.jLabel21, -2, 160, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tNoPassport, -2, 130, -2).addComponent(this.jLabel22, -2, 130, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel43, -1, -1, 32767).addComponent(this.tDateLPassport, -2, 121, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tDateEPassport, -2, 121, -2).addComponent(this.jLabel44, -2, 121, -2))).addGroup(Alignment.TRAILING, pDonneesIdLayout.createSequentialGroup().addGap(18, 18, 18).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tNoCarteSejour, -2, 140, -2).addComponent(this.jLabel61, -2, 140, -2)).addGap(12, 12, 12).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel46, -1, -1, 32767).addComponent(this.tDateLVisa, -2, 121, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel47, -1, -1, 32767).addComponent(this.tDateEVisa, -2, 121, -2)).addGap(25, 25, 25).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tNoPermiTravail, -2, 140, -2).addComponent(this.jLabel62, -2, 140, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel49, -1, -1, 32767).addComponent(this.tDateLPermiTravail, -2, 121, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel50, -1, -1, 32767).addComponent(this.tDateEPermiTravail, -2, 121, -2))))).addGroup(pDonneesIdLayout.createSequentialGroup().addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel36, -1, -1, 32767).addComponent(this.tDateNaiss, -2, 121, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tLieuNaiss, -2, 160, -2).addComponent(this.jLabel20, -2, 160, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tMere, -2, 160, -2).addComponent(this.jLabel16, -2, 160, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPere, -2, 160, -2).addComponent(this.jLabel17, -2, 160, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tNationalite, -2, 160, -2).addComponent(this.jLabel19, -2, 160, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel37, -1, -1, 32767).addComponent(this.tZoneOrigine, -2, 164, -2)))).addComponent(this.jPanel14, -2, -1, -2).addGap(66, 66, 66)));
      pDonneesIdLayout.setVerticalGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesIdLayout.createSequentialGroup().addGroup(pDonneesIdLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.jPanel14, -2, -1, -2).addGroup(Alignment.LEADING, pDonneesIdLayout.createSequentialGroup().addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesIdLayout.createSequentialGroup().addGap(12, 12, 12).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel34).addComponent(this.jLabel35).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel33).addGap(0, 0, 0).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.tId, -2, 30, -2).addComponent(this.tIdPsservice, -2, 30, -2).addComponent(this.tIDSalariePointeuse, -2, 30, -2).addComponent(this.tPrenom, -2, 30, -2).addComponent(this.tNom, -2, 30, -2))).addComponent(this.jLabel14).addGroup(pDonneesIdLayout.createSequentialGroup().addGap(15, 15, 15).addComponent(this.cActif, -1, -1, 32767))).addGap(29, 29, 29).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel36).addGap(0, 0, 0).addComponent(this.tDateNaiss, -2, 32, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel20).addGap(0, 0, 0).addComponent(this.tLieuNaiss, -2, 30, -2)).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.TRAILING).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel16).addGap(0, 0, 0).addComponent(this.tMere, -2, 30, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel17).addGap(0, 0, 0).addComponent(this.tPere, -2, 30, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel19).addGap(0, 0, 0).addComponent(this.tNationalite, -2, 30, -2))))).addGroup(pDonneesIdLayout.createSequentialGroup().addGap(13, 13, 13).addComponent(this.jLabel15)).addGroup(Alignment.TRAILING, pDonneesIdLayout.createSequentialGroup().addContainerGap().addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel37).addGroup(pDonneesIdLayout.createSequentialGroup().addGap(18, 18, 18).addComponent(this.tZoneOrigine, -2, 29, -2))).addGap(2, 2, 2))).addGap(30, 30, 30).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.TRAILING).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel38).addGap(0, 0, 0).addComponent(this.tSexe, -2, 30, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel39).addGap(0, 0, 0).addComponent(this.tSituationFam, -2, 30, -2))).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel40).addGap(0, 0, 0).addComponent(this.tNbEnfants, -2, 30, -2)).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.TRAILING).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel21).addGap(0, 0, 0).addComponent(this.tNNI, -2, 30, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel22).addGap(0, 0, 0).addComponent(this.tNoPassport, -2, 30, -2).addGap(2, 2, 2))).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.tDateEPassport, -2, 32, -2).addGroup(pDonneesIdLayout.createSequentialGroup().addGroup(pDonneesIdLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel43).addComponent(this.jLabel44)).addGap(0, 0, 0).addComponent(this.tDateLPassport, -2, 32, -2))))).addGap(24, 24, 24).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addComponent(this.cExpatrie, -2, 46, -2).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel46).addGap(0, 0, 0).addComponent(this.tDateLVisa, -2, 32, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel47).addGap(0, 0, 0).addComponent(this.tDateEVisa, -2, 32, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel49).addGap(0, 0, 0).addComponent(this.tDateLPermiTravail, -2, 32, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel50).addGap(0, 0, 0).addComponent(this.tDateEPermiTravail, -2, 32, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel61).addGap(0, 0, 0).addComponent(this.tNoCarteSejour, -2, 30, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel62).addGap(0, 0, 0).addComponent(this.tNoPermiTravail, -2, 30, -2))).addGap(20, 20, 20).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.TRAILING).addGroup(pDonneesIdLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel63).addGap(0, 0, 0).addComponent(this.tTelephone, -2, 30, -2)).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel64).addGap(0, 0, 0).addComponent(this.tEmail, -2, 30, -2))).addGroup(pDonneesIdLayout.createSequentialGroup().addComponent(this.jLabel65).addGap(0, 0, 0).addComponent(this.tAdresse, -2, 30, -2))))).addGap(22, 22, 22)));
      this.pIdentite_TabbedPane.addTab("Donn\u00e9es d'identification", this.pDonneesId);
      this.pEnfants.setBackground(new Color(255, 255, 255));
      this.pEnfants.setFont(new Font("Segoe UI Light", 0, 12));
      this.listTableEnfant.setFont(new Font("SansSerif", 1, 11));
      this.listTableEnfant.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTableEnfant.setSelectionBackground(new Color(0, 102, 153));
      this.listTableEnfant.setShowGrid(false);
      this.listTableEnfant.addMouseListener(new 36(this));
      this.jScrollPane15.setViewportView(this.listTableEnfant);
      this.jPanel23.setBackground(new Color(255, 255, 255));
      this.jPanel23.setBorder(BorderFactory.createEtchedBorder());
      this.jLabel104.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel104.setForeground(new Color(0, 102, 153));
      this.jLabel104.setText("Pr\u00e9nom de l'enfant");
      this.tNomEnfant.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNomEnfant.setBorder(BorderFactory.createEtchedBorder());
      this.tNomEnfant.addKeyListener(new 37(this));
      this.btnNewEnf.setToolTipText("Nouveau");
      this.btnNewEnf.setContentAreaFilled(false);
      this.btnNewEnf.setCursor(new Cursor(12));
      this.btnNewEnf.setOpaque(true);
      this.btnNewEnf.addActionListener(new 38(this));
      this.btnSaveEnf.setToolTipText("Sauvegarder");
      this.btnSaveEnf.setContentAreaFilled(false);
      this.btnSaveEnf.setCursor(new Cursor(12));
      this.btnSaveEnf.setOpaque(true);
      this.btnSaveEnf.addActionListener(new 39(this));
      this.btnDelEnf.setToolTipText("Supprimer");
      this.btnDelEnf.setContentAreaFilled(false);
      this.btnDelEnf.setCursor(new Cursor(12));
      this.btnDelEnf.setOpaque(true);
      this.btnDelEnf.addActionListener(new 40(this));
      this.tEnfantID.setBorder((Border)null);
      this.tEnfantID.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tEnfantID.setEnabled(false);
      this.tEnfantID.setFont(new Font("Segoe UI Light", 1, 12));
      this.tEnfantID.addCaretListener(new 41(this));
      this.tEnfantID.addFocusListener(new 42(this));
      this.jLabel107.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel107.setForeground(new Color(0, 102, 153));
      this.jLabel107.setText("M\u00e8re/P\u00e8re");
      this.tPereMereEnf.setFont(new Font("Segoe UI Light", 1, 12));
      this.tPereMereEnf.setBorder(BorderFactory.createEtchedBorder());
      this.tPereMereEnf.addKeyListener(new 43(this));
      this.jLabel118.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel118.setForeground(new Color(0, 102, 153));
      this.jLabel118.setText("Date de naissance");
      this.tDateNaissEnf.setBorder(BorderFactory.createEtchedBorder());
      this.tDateNaissEnf.setDateFormatString("dd/MM/yy");
      this.tDateNaissEnf.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel119.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel119.setForeground(new Color(0, 102, 153));
      this.jLabel119.setText("Genre");
      this.tGenreEnf.setFont(new Font("Segoe UI Light", 1, 12));
      this.tGenreEnf.setModel(new DefaultComboBoxModel(new String[]{"F", "M"}));
      this.tGenreEnf.setBorder(BorderFactory.createEtchedBorder());
      this.tGenreEnf.addActionListener(new 44(this));
      GroupLayout jPanel23Layout = new GroupLayout(this.jPanel23);
      this.jPanel23.setLayout(jPanel23Layout);
      jPanel23Layout.setHorizontalGroup(jPanel23Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel23Layout.createSequentialGroup().addContainerGap().addGroup(jPanel23Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel23Layout.createSequentialGroup().addGap(0, 0, 32767).addGroup(jPanel23Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel107, -2, 320, -2).addGroup(jPanel23Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel23Layout.createSequentialGroup().addComponent(this.btnNewEnf, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSaveEnf, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelEnf, -2, 35, -2)).addComponent(this.tPereMereEnf, -2, 298, -2))).addGap(6, 6, 6)).addGroup(jPanel23Layout.createSequentialGroup().addGroup(jPanel23Layout.createParallelGroup(Alignment.LEADING, false).addGroup(jPanel23Layout.createSequentialGroup().addGroup(jPanel23Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel118, -1, -1, 32767).addComponent(this.tDateNaissEnf, -2, 121, -2)).addGap(18, 18, 18).addGroup(jPanel23Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tGenreEnf, 0, -1, 32767).addComponent(this.jLabel119, -2, 38, -2))).addComponent(this.tNomEnfant).addComponent(this.tEnfantID, -2, 100, -2).addComponent(this.jLabel104, -1, 300, 32767)).addGap(26, 26, 26)))));
      jPanel23Layout.setVerticalGroup(jPanel23Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel23Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel104).addGap(0, 0, 0).addComponent(this.tNomEnfant, -2, 30, -2).addGap(20, 20, 20).addGroup(jPanel23Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel23Layout.createSequentialGroup().addComponent(this.jLabel118).addGap(0, 0, 0).addComponent(this.tDateNaissEnf, -2, 32, -2)).addGroup(jPanel23Layout.createSequentialGroup().addComponent(this.jLabel119).addGap(0, 0, 0).addComponent(this.tGenreEnf, -2, 30, -2))).addGap(18, 18, 18).addComponent(this.jLabel107).addGap(0, 0, 0).addComponent(this.tPereMereEnf, -2, 30, -2).addGap(52, 52, 52).addGroup(jPanel23Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelEnf, -2, 35, -2).addComponent(this.btnSaveEnf, -2, 35, -2).addComponent(this.btnNewEnf, -2, 35, -2).addComponent(this.tEnfantID, -2, 30, -2)).addGap(197, 197, 197)));
      GroupLayout pEnfantsLayout = new GroupLayout(this.pEnfants);
      this.pEnfants.setLayout(pEnfantsLayout);
      pEnfantsLayout.setHorizontalGroup(pEnfantsLayout.createParallelGroup(Alignment.LEADING).addGroup(pEnfantsLayout.createSequentialGroup().addContainerGap().addComponent(this.jPanel23, -2, 324, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane15, -2, 893, -2).addContainerGap()));
      pEnfantsLayout.setVerticalGroup(pEnfantsLayout.createParallelGroup(Alignment.LEADING).addGroup(pEnfantsLayout.createSequentialGroup().addContainerGap().addGroup(pEnfantsLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jScrollPane15, -2, 469, -2).addComponent(this.jPanel23, -2, -1, -2))));
      this.pIdentite_TabbedPane.addTab("Enfants", this.pEnfants);
      this.pDiplomes.setBackground(new Color(255, 255, 255));
      this.pDiplomes.setFont(new Font("Segoe UI Light", 0, 12));
      this.jPanel24.setBackground(new Color(255, 255, 255));
      this.jPanel24.setBorder(BorderFactory.createEtchedBorder());
      this.jLabel105.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel105.setForeground(new Color(0, 102, 153));
      this.jLabel105.setText("Nom du dipl\u00f4me/Formation");
      this.tNomDiplome.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNomDiplome.setBorder(BorderFactory.createEtchedBorder());
      this.tNomDiplome.addKeyListener(new 45(this));
      this.btnNewDiplome.setToolTipText("Nouveau");
      this.btnNewDiplome.setContentAreaFilled(false);
      this.btnNewDiplome.setCursor(new Cursor(12));
      this.btnNewDiplome.setOpaque(true);
      this.btnNewDiplome.addActionListener(new 46(this));
      this.btnSaveDiplome.setToolTipText("Sauvegarder");
      this.btnSaveDiplome.setContentAreaFilled(false);
      this.btnSaveDiplome.setCursor(new Cursor(12));
      this.btnSaveDiplome.setOpaque(true);
      this.btnSaveDiplome.addActionListener(new 47(this));
      this.btnDelDiplome.setToolTipText("Supprimer");
      this.btnDelDiplome.setContentAreaFilled(false);
      this.btnDelDiplome.setCursor(new Cursor(12));
      this.btnDelDiplome.setOpaque(true);
      this.btnDelDiplome.addActionListener(new 48(this));
      this.tDiplomeID.setBorder((Border)null);
      this.tDiplomeID.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tDiplomeID.setEnabled(false);
      this.tDiplomeID.setFont(new Font("Segoe UI Light", 0, 12));
      this.tDiplomeID.addCaretListener(new 49(this));
      this.tDiplomeID.addFocusListener(new 50(this));
      this.jLabel113.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel113.setForeground(new Color(0, 102, 153));
      this.jLabel113.setText("Domaine");
      this.tDomaineDiplome.setFont(new Font("Segoe UI Light", 1, 12));
      this.tDomaineDiplome.setBorder(BorderFactory.createEtchedBorder());
      this.tDomaineDiplome.addKeyListener(new 51(this));
      this.jLabel120.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel120.setForeground(new Color(0, 102, 153));
      this.jLabel120.setText("Date d'obtention");
      this.tDateObtDiplome.setBorder(BorderFactory.createEtchedBorder());
      this.tDateObtDiplome.setDateFormatString("dd/MM/yy");
      this.tDateObtDiplome.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel103.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel103.setForeground(new Color(0, 102, 153));
      this.jLabel103.setText("Degr\u00e9");
      this.tDegreDiplome.setFont(new Font("Segoe UI Light", 1, 12));
      this.tDegreDiplome.setBorder(BorderFactory.createEtchedBorder());
      this.tDegreDiplome.addKeyListener(new 52(this));
      this.jLabel114.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel114.setForeground(new Color(0, 102, 153));
      this.jLabel114.setText("Etablissement");
      this.tEtablissementDiplome.setFont(new Font("Segoe UI Light", 1, 12));
      this.tEtablissementDiplome.setBorder(BorderFactory.createEtchedBorder());
      this.tEtablissementDiplome.addKeyListener(new 53(this));
      GroupLayout jPanel24Layout = new GroupLayout(this.jPanel24);
      this.jPanel24.setLayout(jPanel24Layout);
      jPanel24Layout.setHorizontalGroup(jPanel24Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel24Layout.createSequentialGroup().addContainerGap().addGroup(jPanel24Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel24Layout.createSequentialGroup().addComponent(this.tDiplomeID, -2, 100, -2).addGap(91, 91, 91).addComponent(this.btnNewDiplome, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSaveDiplome, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelDiplome, -2, 35, -2)).addGroup(jPanel24Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tNomDiplome, -2, 320, -2).addComponent(this.jLabel105, -2, 320, -2).addGroup(jPanel24Layout.createSequentialGroup().addGroup(jPanel24Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel120, -1, -1, 32767).addComponent(this.tDateObtDiplome, -2, 121, -2)).addGap(18, 18, 18).addGroup(jPanel24Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel103, -2, 150, -2).addComponent(this.tDegreDiplome, -2, 150, -2))).addComponent(this.tEtablissementDiplome, -2, 320, -2).addComponent(this.jLabel114, -2, 320, -2).addComponent(this.tDomaineDiplome, -2, 320, -2).addComponent(this.jLabel113, -2, 320, -2))).addGap(26, 26, 26)));
      jPanel24Layout.setVerticalGroup(jPanel24Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel24Layout.createSequentialGroup().addGap(20, 20, 20).addComponent(this.jLabel105).addGap(0, 0, 0).addComponent(this.tNomDiplome, -2, 30, -2).addGap(20, 20, 20).addGroup(jPanel24Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel24Layout.createSequentialGroup().addComponent(this.jLabel120).addGap(0, 0, 0).addComponent(this.tDateObtDiplome, -2, 32, -2)).addComponent(this.jLabel103).addGroup(jPanel24Layout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.tDegreDiplome, -2, 30, -2))).addGap(18, 18, 18).addComponent(this.jLabel113).addGap(0, 0, 0).addComponent(this.tDomaineDiplome, -2, 30, -2).addGap(22, 22, 22).addComponent(this.jLabel114).addGap(0, 0, 0).addComponent(this.tEtablissementDiplome, -2, 30, -2).addGap(27, 27, 27).addGroup(jPanel24Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelDiplome, -2, 35, -2).addComponent(this.btnSaveDiplome, -2, 35, -2).addComponent(this.btnNewDiplome, -2, 35, -2).addComponent(this.tDiplomeID, Alignment.TRAILING, -2, 30, -2)).addGap(135, 135, 135)));
      this.listTableDiplome.setFont(new Font("Segoe UI Light", 0, 11));
      this.listTableDiplome.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTableDiplome.setSelectionBackground(new Color(0, 102, 153));
      this.listTableDiplome.setShowGrid(false);
      this.listTableDiplome.addMouseListener(new 54(this));
      this.jScrollPane16.setViewportView(this.listTableDiplome);
      GroupLayout pDiplomesLayout = new GroupLayout(this.pDiplomes);
      this.pDiplomes.setLayout(pDiplomesLayout);
      pDiplomesLayout.setHorizontalGroup(pDiplomesLayout.createParallelGroup(Alignment.LEADING).addGroup(pDiplomesLayout.createSequentialGroup().addContainerGap().addComponent(this.jPanel24, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane16, -2, 839, -2).addGap(119, 119, 119)));
      pDiplomesLayout.setVerticalGroup(pDiplomesLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pDiplomesLayout.createSequentialGroup().addContainerGap().addGroup(pDiplomesLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jScrollPane16).addComponent(this.jPanel24, -1, -1, 32767)).addContainerGap()));
      this.pIdentite_TabbedPane.addTab("Dipl\u00f4me/Formations", this.pDiplomes);
      this.jPanel29.setBackground(new Color(255, 255, 255));
      this.jLabel123.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel123.setForeground(new Color(0, 102, 153));
      this.jLabel123.setText("Nom du document");
      this.tNomDocument.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNomDocument.setBorder(BorderFactory.createEtchedBorder());
      this.tNomDocument.addKeyListener(new 55(this));
      this.btnNewDoc.setToolTipText("Nouveau");
      this.btnNewDoc.setContentAreaFilled(false);
      this.btnNewDoc.setCursor(new Cursor(12));
      this.btnNewDoc.setOpaque(true);
      this.btnNewDoc.addActionListener(new 56(this));
      this.btnSaveDoc.setToolTipText("Sauvegarder");
      this.btnSaveDoc.setContentAreaFilled(false);
      this.btnSaveDoc.setCursor(new Cursor(12));
      this.btnSaveDoc.setOpaque(true);
      this.btnSaveDoc.addActionListener(new 57(this));
      this.btnDelDoc.setToolTipText("Supprimer");
      this.btnDelDoc.setContentAreaFilled(false);
      this.btnDelDoc.setCursor(new Cursor(12));
      this.btnDelDoc.setOpaque(true);
      this.btnDelDoc.addActionListener(new 58(this));
      this.btnSearchDoc.setToolTipText("Parcourir...");
      this.btnSearchDoc.setContentAreaFilled(false);
      this.btnSearchDoc.setCursor(new Cursor(12));
      this.btnSearchDoc.setOpaque(true);
      this.btnSearchDoc.addActionListener(new 59(this));
      this.tDestFile.setEditable(false);
      this.tDestFile.setFont(new Font("Tahoma", 0, 8));
      this.tDestFile.setHorizontalAlignment(0);
      this.lbFilePath.setEditable(false);
      this.lbFilePath.setFont(new Font("Tahoma", 0, 8));
      this.lbFilePath.setHorizontalAlignment(0);
      GroupLayout jPanel29Layout = new GroupLayout(this.jPanel29);
      this.jPanel29.setLayout(jPanel29Layout);
      jPanel29Layout.setHorizontalGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel29Layout.createSequentialGroup().addContainerGap().addGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel29Layout.createSequentialGroup().addGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tNomDocument, -2, 395, -2).addComponent(this.jSeparator77).addComponent(this.jLabel123, -1, -1, 32767)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnSearchDoc, -2, 35, -2).addGap(12, 12, 12)).addGroup(Alignment.TRAILING, jPanel29Layout.createSequentialGroup().addGap(9, 9, 9).addGroup(jPanel29Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.lbFilePath, -2, 268, -2).addComponent(this.tDestFile, -2, 268, -2)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnNewDoc, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSaveDoc, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelDoc, -2, 35, -2))).addContainerGap()));
      jPanel29Layout.setVerticalGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel29Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel123).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.btnSearchDoc, -1, -1, 32767).addComponent(this.tNomDocument, -1, 35, 32767)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jSeparator77, -2, 0, -2).addGap(196, 196, 196).addGroup(jPanel29Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelDoc, -2, 35, -2).addComponent(this.btnSaveDoc, -2, 35, -2).addComponent(this.btnNewDoc, -2, 35, -2)).addGroup(jPanel29Layout.createSequentialGroup().addComponent(this.lbFilePath, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tDestFile, -2, -1, -2))).addGap(39, 39, 39)));
      this.listDocuments.setFont(new Font("Segoe UI Light", 0, 11));
      this.listDocuments.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listDocuments.setSelectionBackground(new Color(0, 102, 153));
      this.listDocuments.setShowGrid(false);
      this.listDocuments.addMouseListener(new 60(this));
      this.jScrollPane19.setViewportView(this.listDocuments);
      GroupLayout pDocumentsLayout = new GroupLayout(this.pDocuments);
      this.pDocuments.setLayout(pDocumentsLayout);
      pDocumentsLayout.setHorizontalGroup(pDocumentsLayout.createParallelGroup(Alignment.LEADING).addGroup(pDocumentsLayout.createSequentialGroup().addContainerGap().addComponent(this.jPanel29, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane19, -2, 686, -2).addContainerGap()));
      pDocumentsLayout.setVerticalGroup(pDocumentsLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pDocumentsLayout.createSequentialGroup().addContainerGap().addGroup(pDocumentsLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.jScrollPane19, -2, 463, -2).addComponent(this.jPanel29, -1, -1, 32767)).addContainerGap()));
      this.pIdentite_TabbedPane.addTab("Documents", this.pDocuments);
      GroupLayout pIdentiteLayout = new GroupLayout(this.pIdentite);
      this.pIdentite.setLayout(pIdentiteLayout);
      pIdentiteLayout.setHorizontalGroup(pIdentiteLayout.createParallelGroup(Alignment.LEADING).addComponent(this.pIdentite_TabbedPane, -2, 1326, -2));
      pIdentiteLayout.setVerticalGroup(pIdentiteLayout.createParallelGroup(Alignment.LEADING).addComponent(this.pIdentite_TabbedPane, -2, -1, -2));
      this.detailPanel.addTab("Identit\u00e9", this.pIdentite);
      this.pContrat.setBackground(new Color(255, 255, 255));
      this.pContrat.setFont(new Font("Segoe UI Light", 0, 12));
      this.pContrat_TabbedPane.setForeground(new Color(0, 102, 153));
      this.pContrat_TabbedPane.setFont(new Font("SansSerif", 1, 11));
      this.pDonneesPro.setBackground(new Color(255, 255, 255));
      this.pDonneesPro.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel56.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel56.setForeground(new Color(0, 102, 153));
      this.jLabel56.setText("D\u00e9partement");
      this.tDepartement.setFont(new Font("Segoe UI Light", 1, 12));
      this.tDepartement.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tDepartement.setBorder(BorderFactory.createEtchedBorder());
      this.tDepartement.addActionListener(new 61(this));
      this.jLabel57.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel57.setForeground(new Color(0, 102, 153));
      this.jLabel57.setText("Service / Activit\u00e9");
      this.tActivite.setFont(new Font("Segoe UI Light", 1, 12));
      this.tActivite.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tActivite.setBorder(BorderFactory.createEtchedBorder());
      this.tActivite.addActionListener(new 62(this));
      this.jLabel58.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel58.setForeground(new Color(0, 102, 153));
      this.jLabel58.setText("Poste");
      this.tPoste.setFont(new Font("Segoe UI Light", 1, 12));
      this.tPoste.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPoste.setBorder(BorderFactory.createEtchedBorder());
      this.tPoste.addActionListener(new 63(this));
      this.jLabel59.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel59.setForeground(new Color(0, 102, 153));
      this.jLabel59.setText("Banque");
      this.tBanque.setFont(new Font("Segoe UI Light", 1, 12));
      this.tBanque.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tBanque.setBorder(BorderFactory.createEtchedBorder());
      this.tBanque.addActionListener(new 64(this));
      this.jLabel60.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel60.setForeground(new Color(0, 102, 153));
      this.jLabel60.setText("Cat\u00e9gorie");
      this.tCategorie.setFont(new Font("Segoe UI Light", 1, 12));
      this.tCategorie.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tCategorie.addActionListener(new 65(this));
      this.jLabel66.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel66.setForeground(new Color(0, 102, 153));
      this.jLabel66.setText("Cat1/Unite/Sce.");
      this.tClassification.setFont(new Font("Segoe UI Light", 1, 12));
      this.tClassification.setBorder(BorderFactory.createEtchedBorder());
      this.tClassification.addKeyListener(new 66(this));
      this.jLabel67.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel67.setForeground(new Color(0, 102, 153));
      this.jLabel67.setText("Cat2/Labo./Divison");
      this.tStatut.setFont(new Font("Segoe UI Light", 1, 12));
      this.tStatut.setBorder(BorderFactory.createEtchedBorder());
      this.tStatut.addKeyListener(new 67(this));
      this.jLabel45.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel45.setForeground(new Color(0, 102, 153));
      this.jLabel45.setText("Date de sortie");
      this.tDateDebauche.setBorder(BorderFactory.createEtchedBorder());
      this.tDateDebauche.setDateFormatString("dd/MM/yy");
      this.tDateDebauche.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel68.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel68.setForeground(new Color(0, 102, 153));
      this.jLabel68.setText("Mode de payement");
      this.tModePaiement.setFont(new Font("Segoe UI Light", 1, 12));
      this.tModePaiement.setModel(new DefaultComboBoxModel(new String[]{"Virement", "Esp\u00e8ce", "Ch\u00e8que"}));
      this.tModePaiement.setBorder(BorderFactory.createEtchedBorder());
      this.tModePaiement.addActionListener(new 68(this));
      this.jLabel69.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel69.setForeground(new Color(0, 102, 153));
      this.jLabel69.setText("Direction g\u00e9n\u00e9rale");
      this.tDirectiongeneral.setFont(new Font("Segoe UI Light", 1, 12));
      this.tDirectiongeneral.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tDirectiongeneral.setBorder(BorderFactory.createEtchedBorder());
      this.tDirectiongeneral.addActionListener(new 69(this));
      this.jLabel70.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel70.setForeground(new Color(0, 102, 153));
      this.jLabel70.setText("Direction");
      this.tDirection.setFont(new Font("Segoe UI Light", 1, 12));
      this.tDirection.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tDirection.setBorder(BorderFactory.createEtchedBorder());
      this.tDirection.addActionListener(new 70(this));
      this.cPsservice.setBackground(new Color(255, 255, 255));
      this.cPsservice.setFont(new Font("Segoe UI Light", 0, 12));
      this.cPsservice.setForeground(new Color(0, 102, 153));
      this.cPsservice.setText("Bulletin en ligne");
      this.cPsservice.addActionListener(new 71(this));
      this.jLabel71.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel71.setForeground(new Color(0, 102, 153));
      this.jLabel71.setText("Raison de d\u00e9bauche");
      this.tRaisonDebauche.setFont(new Font("Segoe UI Light", 1, 12));
      this.tRaisonDebauche.setBorder(BorderFactory.createEtchedBorder());
      this.tRaisonDebauche.addKeyListener(new 72(this));
      this.jLabel31.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel31.setForeground(new Color(0, 102, 153));
      this.jLabel31.setText("Budget anuel");
      this.tBudgetAnuel.setBorder(BorderFactory.createEtchedBorder());
      this.tBudgetAnuel.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tBudgetAnuel.setHorizontalAlignment(4);
      this.tBudgetAnuel.setFont(new Font("Segoe UI Light", 1, 12));
      this.tBudgetAnuel.addCaretListener(new 73(this));
      this.tBudgetAnuel.addFocusListener(new 74(this));
      this.tNoCompteBanque.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNoCompteBanque.setText("888888888888888888");
      this.tNoCompteBanque.setBorder(BorderFactory.createEtchedBorder());
      this.tNoCompteBanque.addKeyListener(new 75(this));
      this.jLabel72.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel72.setForeground(new Color(0, 102, 153));
      this.jLabel72.setText("Num. Compte");
      this.cDomicilie.setBackground(new Color(255, 255, 255));
      this.cDomicilie.setFont(new Font("Segoe UI Light", 0, 12));
      this.cDomicilie.setForeground(new Color(0, 102, 153));
      this.cDomicilie.setText("Domicil\u00e9");
      this.cDomicilie.addActionListener(new 76(this));
      this.jLabel73.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel73.setForeground(new Color(0, 102, 153));
      this.jLabel73.setText("Type contrat");
      this.tTypeContrat.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTypeContrat.setModel(new DefaultComboBoxModel(new String[]{"CDD", "CDI", "FONC.", "AUX."}));
      this.tTypeContrat.addActionListener(new 77(this));
      this.jLabel48.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel48.setForeground(new Color(0, 102, 153));
      this.jLabel48.setText("Recrutement");
      this.tDateEmbauche.setBorder(BorderFactory.createEtchedBorder());
      this.tDateEmbauche.setDateFormatString("dd/MM/yy");
      this.tDateEmbauche.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel51.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel51.setForeground(new Color(0, 102, 153));
      this.jLabel51.setText("Anciennet\u00e9");
      this.tDateAnciennete.setBorder(BorderFactory.createEtchedBorder());
      this.tDateAnciennete.setDateFormatString("dd/MM/yy");
      this.tDateAnciennete.setFont(new Font("Segoe UI Light", 1, 12));
      this.tDateAnciennete.addFocusListener(new 78(this));
      this.jLabel41.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel41.setForeground(new Color(0, 102, 153));
      this.jLabel41.setText("Taux anc.");
      this.tTauxAnc.setBorder(BorderFactory.createEtchedBorder());
      this.tTauxAnc.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tTauxAnc.setHorizontalAlignment(0);
      this.tTauxAnc.setEnabled(false);
      this.tTauxAnc.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTauxAnc.addCaretListener(new 79(this));
      this.tTauxAnc.addFocusListener(new 80(this));
      this.jLabel52.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel52.setForeground(new Color(0, 102, 153));
      this.jLabel52.setText("Fin de contrat");
      this.tDateFinContrat.setBorder(BorderFactory.createEtchedBorder());
      this.tDateFinContrat.setDateFormatString("dd/MM/yy");
      this.tDateFinContrat.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel74.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel74.setForeground(new Color(0, 102, 153));
      this.jLabel74.setText("Lieu de travail");
      this.tLieuTravail.setFont(new Font("Segoe UI Light", 1, 12));
      this.tLieuTravail.setBorder(BorderFactory.createEtchedBorder());
      this.tLieuTravail.addKeyListener(new 81(this));
      this.buttonConges1.setFont(new Font("Segoe UI Light", 1, 12));
      this.buttonConges1.setText("Appliquer le Mod\u00e8l");
      this.buttonConges1.setBorder(BorderFactory.createBevelBorder(0));
      this.buttonConges1.addActionListener(new 82(this));
      this.jLabel53.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel53.setForeground(new Color(0, 102, 153));
      this.jLabel53.setText("Date cat\u00e9gorie");
      this.tDateCategorie.setBorder(BorderFactory.createEtchedBorder());
      this.tDateCategorie.setDateFormatString("dd/MM/yy");
      this.tDateCategorie.setFont(new Font("Segoe UI Light", 1, 12));
      this.appSBCatButton.setFont(new Font("Segoe UI Light", 1, 12));
      this.appSBCatButton.setText("Appliquer le salaire de base");
      this.appSBCatButton.setBorder(BorderFactory.createBevelBorder(0));
      this.appSBCatButton.addActionListener(new 83(this));
      this.cAvancmentAutoCat.setBackground(new Color(255, 255, 255));
      this.cAvancmentAutoCat.setFont(new Font("Segoe UI Light", 0, 12));
      this.cAvancmentAutoCat.setForeground(new Color(0, 102, 153));
      this.cAvancmentAutoCat.setText("Avancement auto.");
      this.cAvancmentAutoCat.addActionListener(new 84(this));
      this.jLabel42.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel42.setForeground(new Color(0, 102, 153));
      this.jLabel42.setText("Nb. ann\u00e9es");
      this.tNbAnneesCat.setBorder(BorderFactory.createEtchedBorder());
      this.tNbAnneesCat.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tNbAnneesCat.setHorizontalAlignment(0);
      this.tNbAnneesCat.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNbAnneesCat.addCaretListener(new 85(this));
      this.tNbAnneesCat.addFocusListener(new 86(this));
      this.jLabel54.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel54.setForeground(new Color(0, 102, 153));
      this.jLabel54.setText("Heures/Semaine");
      this.tHeureSemaine.setBorder(BorderFactory.createEtchedBorder());
      this.tHeureSemaine.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tHeureSemaine.setHorizontalAlignment(0);
      this.tHeureSemaine.setFont(new Font("Segoe UI Light", 1, 12));
      this.tHeureSemaine.addCaretListener(new 87(this));
      this.tHeureSemaine.addFocusListener(new 88(this));
      this.jLabel55.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel55.setForeground(new Color(0, 102, 153));
      this.jLabel55.setText("Nb. Mois Pr\u00e9avis");
      this.tNbMoisPreavis.setBorder(BorderFactory.createEtchedBorder());
      this.tNbMoisPreavis.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tNbMoisPreavis.setHorizontalAlignment(0);
      this.tNbMoisPreavis.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNbMoisPreavis.addCaretListener(new 89(this));
      this.tNbMoisPreavis.addFocusListener(new 90(this));
      this.jLabel75.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel75.setForeground(new Color(0, 102, 153));
      this.jLabel75.setText("Indice");
      this.tTauxPSRA.setBorder(BorderFactory.createEtchedBorder());
      this.tTauxPSRA.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tTauxPSRA.setHorizontalAlignment(0);
      this.tTauxPSRA.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTauxPSRA.addCaretListener(new 91(this));
      this.tTauxPSRA.addFocusListener(new 92(this));
      GroupLayout pDonneesProLayout = new GroupLayout(this.pDonneesPro);
      this.pDonneesPro.setLayout(pDonneesProLayout);
      pDonneesProLayout.setHorizontalGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesProLayout.createSequentialGroup().addGap(10, 10, 10).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesProLayout.createSequentialGroup().addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel58, -2, 300, -2).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.tPoste, -2, 300, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.buttonConges1, -2, 164, -2))).addGap(63, 63, 63).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tCategorie, -2, 150, -2).addComponent(this.jLabel60, -2, 150, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel53, -2, 100, -2).addComponent(this.tDateCategorie, -2, 120, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.appSBCatButton, -2, 197, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.cAvancmentAutoCat).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tNbAnneesCat, -2, 80, -2).addComponent(this.jLabel42, -2, 80, -2))).addGroup(pDonneesProLayout.createSequentialGroup().addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesProLayout.createSequentialGroup().addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel45, -2, 100, -2).addComponent(this.tDateDebauche, -2, 120, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel71, -2, 200, -2).addComponent(this.tRaisonDebauche, -2, 400, -2)).addGap(57, 57, 57).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel31, -2, 120, -2).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.tBudgetAnuel, -2, 149, -2).addGap(50, 50, 50).addComponent(this.cPsservice, -2, 195, -2)))).addGroup(pDonneesProLayout.createSequentialGroup().addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tTypeContrat, 0, -1, 32767).addComponent(this.jLabel73, -1, 121, 32767)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel48, -2, 100, -2).addComponent(this.tDateEmbauche, -2, 120, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel51, -2, 100, -2).addComponent(this.tDateAnciennete, -2, 120, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel41, -1, -1, 32767).addComponent(this.tTauxAnc, -2, 71, -2)).addGap(71, 71, 71).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tDateFinContrat, -1, -1, 32767).addComponent(this.jLabel52, -2, 120, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel74, -2, 160, -2).addComponent(this.tLieuTravail, -2, 285, -2))).addGroup(pDonneesProLayout.createSequentialGroup().addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tDirectiongeneral, -2, 200, -2).addComponent(this.jLabel69, -2, 200, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tDirection, -2, 200, -2).addComponent(this.jLabel70, -2, 200, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tDepartement, -2, 200, -2).addComponent(this.jLabel56, -2, 200, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tActivite, -2, 200, -2).addComponent(this.jLabel57, -2, 200, -2)).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesProLayout.createSequentialGroup().addGap(218, 218, 218).addGroup(pDonneesProLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.tTauxPSRA, -2, 100, -2).addComponent(this.jLabel75, -2, 100, -2))).addGroup(pDonneesProLayout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tClassification, -2, 200, -2).addComponent(this.jLabel66, -2, 200, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tStatut, -2, 200, -2).addComponent(this.jLabel67, -2, 200, -2))))).addGroup(pDonneesProLayout.createSequentialGroup().addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tModePaiement, 0, -1, 32767).addComponent(this.jLabel68, -2, 143, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel59, -2, 150, -2).addComponent(this.tBanque, -2, 200, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel72, -2, 150, -2).addComponent(this.tNoCompteBanque, -2, 185, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cDomicilie).addGap(33, 33, 33).addGroup(pDonneesProLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel55, Alignment.LEADING, -2, 100, -2).addComponent(this.tNbMoisPreavis, Alignment.LEADING, -2, 100, -2)).addGap(29, 29, 29).addGroup(pDonneesProLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel54, Alignment.LEADING, -2, 100, -2).addComponent(this.tHeureSemaine, Alignment.LEADING, -2, 100, -2)))).addGap(0, 0, 32767))).addGap(52, 52, 52)));
      pDonneesProLayout.setVerticalGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesProLayout.createSequentialGroup().addGap(37, 37, 37).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesProLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel41).addComponent(this.jLabel52).addComponent(this.jLabel51).addComponent(this.jLabel74)).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pDonneesProLayout.createSequentialGroup().addGap(21, 21, 21).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tDateFinContrat, -2, 32, -2).addComponent(this.tLieuTravail, -2, 30, -2))).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesProLayout.createSequentialGroup().addGroup(pDonneesProLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel48).addComponent(this.jLabel73)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tDateAnciennete, -2, 32, -2).addComponent(this.tDateEmbauche, -2, 32, -2).addComponent(this.tTauxAnc, -2, 30, -2))).addComponent(this.tTypeContrat, Alignment.TRAILING, -2, 30, -2)))).addGap(28, 28, 28).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel69).addGap(0, 0, 0).addComponent(this.tDirectiongeneral, -2, 30, -2)).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel70).addGap(0, 0, 0).addComponent(this.tDirection, -2, 30, -2)).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel56).addGap(0, 0, 0).addComponent(this.tDepartement, -2, 30, -2)).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel57).addGap(0, 0, 0).addComponent(this.tActivite, -2, 30, -2)).addGroup(pDonneesProLayout.createParallelGroup(Alignment.TRAILING).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel66).addGap(0, 0, 0).addComponent(this.tClassification, -2, 30, -2)).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel67).addGap(0, 0, 0).addComponent(this.tStatut, -2, 30, -2)))).addGap(27, 27, 27).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesProLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel53).addComponent(this.jLabel42)).addGroup(pDonneesProLayout.createParallelGroup(Alignment.TRAILING).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel60).addGap(0, 0, 0).addComponent(this.tCategorie, -2, 30, -2)).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel58).addGap(0, 0, 0).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPoste).addComponent(this.buttonConges1, -2, 30, -2))).addComponent(this.tDateCategorie, -2, 32, -2).addGroup(pDonneesProLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.appSBCatButton, -2, 32, -2).addComponent(this.cAvancmentAutoCat, -2, 31, -2).addComponent(this.tNbAnneesCat, -2, 30, -2)))).addGap(43, 43, 43).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel55).addGroup(pDonneesProLayout.createParallelGroup(Alignment.TRAILING).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tModePaiement, -2, 30, -2).addGroup(pDonneesProLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.tBanque, -2, 30, -2).addComponent(this.tNoCompteBanque, -2, 30, -2)).addGroup(pDonneesProLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.cDomicilie, -2, 31, -2).addComponent(this.tNbMoisPreavis, -2, 30, -2))).addGroup(pDonneesProLayout.createSequentialGroup().addGroup(pDonneesProLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel72).addComponent(this.jLabel59).addComponent(this.jLabel68)).addGap(33, 33, 33))).addGroup(pDonneesProLayout.createParallelGroup(Alignment.TRAILING).addGroup(pDonneesProLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.tHeureSemaine, -2, 30, -2).addComponent(this.tTauxPSRA, -2, 30, -2)).addGroup(pDonneesProLayout.createSequentialGroup().addGroup(pDonneesProLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel54).addComponent(this.jLabel75)).addGap(33, 33, 33)))).addGap(26, 26, 26).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pDonneesProLayout.createParallelGroup(Alignment.LEADING).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel71).addGap(0, 0, 0).addComponent(this.tRaisonDebauche, -2, 30, -2)).addGroup(pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel45).addGap(0, 0, 0).addComponent(this.tDateDebauche, -2, 32, -2))).addGroup(Alignment.TRAILING, pDonneesProLayout.createSequentialGroup().addComponent(this.jLabel31).addGap(0, 0, 0).addGroup(pDonneesProLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.cPsservice, -1, -1, 32767).addComponent(this.tBudgetAnuel, -1, 30, 32767)).addGap(2, 2, 2))).addGap(76, 76, 76)));
      this.pContrat_TabbedPane.addTab("Donn\u00e9es professionnelles", this.pDonneesPro);
      this.pTaxes.setBackground(new Color(255, 255, 255));
      this.pTaxes.setFont(new Font("Segoe UI Light", 0, 12));
      this.jPanel16.setBackground(new Color(255, 255, 255));
      this.jPanel16.setBorder(BorderFactory.createTitledBorder((Border)null, "CNAM", 0, 0, new Font("Segoe UI Light", 1, 11)));
      this.jPanel16.setForeground(new Color(0, 102, 153));
      this.cDetacheCNAM.setBackground(new Color(255, 255, 255));
      this.cDetacheCNAM.setFont(new Font("Segoe UI Light", 1, 12));
      this.cDetacheCNAM.setForeground(new Color(0, 102, 153));
      this.cDetacheCNAM.setText("Detach\u00e9 CNAM");
      this.cDetacheCNAM.addActionListener(new 93(this));
      this.jLabel76.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel76.setForeground(new Color(0, 102, 153));
      this.jLabel76.setText("Num. CNAM");
      this.tNoCNAM.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNoCNAM.setBorder(BorderFactory.createEtchedBorder());
      this.tNoCNAM.addKeyListener(new 94(this));
      this.jLabel77.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel77.setForeground(new Color(0, 102, 153));
      this.jLabel77.setText("Date d\u00e9claration");
      this.tDateDebauche3.setBorder(BorderFactory.createEtchedBorder());
      this.tDateDebauche3.setDateFormatString("dd/MM/yy");
      this.tDateDebauche3.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel78.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel78.setForeground(new Color(0, 102, 153));
      this.jLabel78.setText("Taux remb. (%)");
      this.tTauxRembCNAM.setBorder(BorderFactory.createEtchedBorder());
      this.tTauxRembCNAM.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tTauxRembCNAM.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTauxRembCNAM.addCaretListener(new 95(this));
      this.tTauxRembCNAM.addFocusListener(new 96(this));
      GroupLayout jPanel16Layout = new GroupLayout(this.jPanel16);
      this.jPanel16.setLayout(jPanel16Layout);
      jPanel16Layout.setHorizontalGroup(jPanel16Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel16Layout.createSequentialGroup().addContainerGap().addComponent(this.cDetacheCNAM, -2, 128, -2).addGap(18, 18, 18).addGroup(jPanel16Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tNoCNAM, -2, 160, -2).addComponent(this.jLabel76, -2, 160, -2)).addGap(18, 18, 18).addGroup(jPanel16Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel77, -2, 100, -2).addComponent(this.tDateDebauche3, -2, 120, -2)).addGap(18, 18, 18).addGroup(jPanel16Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel78, Alignment.LEADING, -2, 150, -2).addComponent(this.tTauxRembCNAM, Alignment.LEADING, -2, 150, -2)).addContainerGap()));
      jPanel16Layout.setVerticalGroup(jPanel16Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel16Layout.createSequentialGroup().addContainerGap().addGroup(jPanel16Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.cDetacheCNAM, Alignment.LEADING, -2, 46, -2).addGroup(Alignment.LEADING, jPanel16Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel16Layout.createSequentialGroup().addComponent(this.jLabel78).addGap(2, 2, 2).addComponent(this.tTauxRembCNAM, -2, 30, -2)).addGroup(jPanel16Layout.createSequentialGroup().addComponent(this.jLabel77).addGap(0, 0, 0).addComponent(this.tDateDebauche3, -2, 32, -2)).addGroup(jPanel16Layout.createSequentialGroup().addComponent(this.jLabel76).addGap(0, 0, 0).addComponent(this.tNoCNAM, -2, 30, -2)))).addContainerGap(10, 32767)));
      this.jPanel17.setBackground(new Color(255, 255, 255));
      this.jPanel17.setBorder(BorderFactory.createTitledBorder((Border)null, "CNSS", 0, 0, new Font("Segoe UI Light", 1, 11)));
      this.jPanel17.setForeground(new Color(0, 102, 153));
      this.cDetacheCNSS.setBackground(new Color(255, 255, 255));
      this.cDetacheCNSS.setFont(new Font("Segoe UI Light", 1, 12));
      this.cDetacheCNSS.setForeground(new Color(0, 102, 153));
      this.cDetacheCNSS.setText("Detach\u00e9 CNSS");
      this.cDetacheCNSS.addActionListener(new 97(this));
      this.jLabel80.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel80.setForeground(new Color(0, 102, 153));
      this.jLabel80.setText("Num. CNSS");
      this.tNoCNSS.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNoCNSS.setBorder(BorderFactory.createEtchedBorder());
      this.tNoCNSS.addKeyListener(new 98(this));
      this.jLabel81.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel81.setForeground(new Color(0, 102, 153));
      this.jLabel81.setText("Date d\u00e9claration");
      this.tDateCNSS.setBorder(BorderFactory.createEtchedBorder());
      this.tDateCNSS.setDateFormatString("dd/MM/yy");
      this.tDateCNSS.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel82.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel82.setForeground(new Color(0, 102, 153));
      this.jLabel82.setText("Taux remb. (%)");
      this.tTauxRembCNSS.setBorder(BorderFactory.createEtchedBorder());
      this.tTauxRembCNSS.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tTauxRembCNSS.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTauxRembCNSS.addCaretListener(new 99(this));
      this.tTauxRembCNSS.addFocusListener(new 100(this));
      this.jLabel83.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel83.setForeground(new Color(0, 102, 153));
      this.jLabel83.setText("Stricture d'origine");
      this.tStrictureOrigine.setFont(new Font("Segoe UI Light", 0, 12));
      this.tStrictureOrigine.setBorder(BorderFactory.createEtchedBorder());
      this.tStrictureOrigine.addKeyListener(new 101(this));
      GroupLayout jPanel17Layout = new GroupLayout(this.jPanel17);
      this.jPanel17.setLayout(jPanel17Layout);
      jPanel17Layout.setHorizontalGroup(jPanel17Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel17Layout.createSequentialGroup().addContainerGap().addGroup(jPanel17Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel17Layout.createSequentialGroup().addGroup(jPanel17Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tStrictureOrigine, -2, 500, -2).addComponent(this.jLabel83, -2, 500, -2)).addContainerGap()).addGroup(Alignment.TRAILING, jPanel17Layout.createSequentialGroup().addComponent(this.cDetacheCNSS, -2, 128, -2).addGap(18, 18, 18).addGroup(jPanel17Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tNoCNSS, -2, 160, -2).addComponent(this.jLabel80, -2, 160, -2)).addGap(18, 18, 18).addGroup(jPanel17Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel81, -2, 100, -2).addComponent(this.tDateCNSS, -2, 120, -2)).addGap(18, 18, 18).addGroup(jPanel17Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel82, Alignment.LEADING, -2, 150, -2).addComponent(this.tTauxRembCNSS, Alignment.LEADING, -2, 150, -2)).addGap(466, 466, 466)))));
      jPanel17Layout.setVerticalGroup(jPanel17Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel17Layout.createSequentialGroup().addContainerGap().addGroup(jPanel17Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel17Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel17Layout.createSequentialGroup().addComponent(this.jLabel81).addGap(0, 0, 0).addComponent(this.tDateCNSS, -2, 32, -2)).addGroup(jPanel17Layout.createSequentialGroup().addComponent(this.jLabel80).addGap(0, 0, 0).addComponent(this.tNoCNSS, -2, 30, -2)).addComponent(this.cDetacheCNSS, Alignment.LEADING, -2, 46, -2)).addGroup(jPanel17Layout.createSequentialGroup().addComponent(this.jLabel82).addGap(0, 0, 0).addComponent(this.tTauxRembCNSS, -2, 30, -2))).addGap(4, 4, 4).addComponent(this.jLabel83).addGap(0, 0, 0).addComponent(this.tStrictureOrigine, -2, 30, -2).addGap(17, 17, 17)));
      this.jPanel18.setBackground(new Color(255, 255, 255));
      this.jPanel18.setBorder(BorderFactory.createTitledBorder((Border)null, "ITS", 0, 0, new Font("Segoe UI Light", 1, 11)));
      this.jPanel18.setForeground(new Color(0, 102, 153));
      this.cExonoreITS.setBackground(new Color(255, 255, 255));
      this.cExonoreITS.setFont(new Font("Segoe UI Light", 1, 12));
      this.cExonoreITS.setForeground(new Color(0, 102, 153));
      this.cExonoreITS.setText("Exonor\u00e9 ITS");
      this.cExonoreITS.addActionListener(new 102(this));
      this.jLabel85.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel85.setForeground(new Color(0, 102, 153));
      this.jLabel85.setText("Taux remb. 3\u00e8me Tranche (%)");
      this.tauxRembITStranche3.setBorder(BorderFactory.createEtchedBorder());
      this.tauxRembITStranche3.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tauxRembITStranche3.setFont(new Font("Segoe UI Light", 1, 12));
      this.tauxRembITStranche3.addCaretListener(new 103(this));
      this.tauxRembITStranche3.addFocusListener(new 104(this));
      this.jLabel86.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel86.setForeground(new Color(0, 102, 153));
      this.jLabel86.setText("Taux remb. 2\u00e8me Tranche (%)");
      this.tauxRembITStranche2.setBorder(BorderFactory.createEtchedBorder());
      this.tauxRembITStranche2.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tauxRembITStranche2.setFont(new Font("Segoe UI Light", 1, 12));
      this.tauxRembITStranche2.addCaretListener(new 105(this));
      this.tauxRembITStranche2.addFocusListener(new 106(this));
      this.jLabel87.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel87.setForeground(new Color(0, 102, 153));
      this.jLabel87.setText("Taux remb. 1\u00e8re Tranche (%)");
      this.tauxRembITStranche1.setBorder(BorderFactory.createEtchedBorder());
      this.tauxRembITStranche1.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tauxRembITStranche1.setFont(new Font("Segoe UI Light", 1, 12));
      this.tauxRembITStranche1.addCaretListener(new 107(this));
      this.tauxRembITStranche1.addFocusListener(new 108(this));
      GroupLayout jPanel18Layout = new GroupLayout(this.jPanel18);
      this.jPanel18.setLayout(jPanel18Layout);
      jPanel18Layout.setHorizontalGroup(jPanel18Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel18Layout.createSequentialGroup().addContainerGap().addComponent(this.cExonoreITS, -2, 128, -2).addGap(18, 18, 18).addGroup(jPanel18Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tauxRembITStranche1).addComponent(this.jLabel87, -2, 186, -2)).addGap(42, 42, 42).addGroup(jPanel18Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel86, -1, -1, 32767).addComponent(this.tauxRembITStranche2, -2, 189, -2)).addGap(43, 43, 43).addGroup(jPanel18Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel85, -1, -1, 32767).addComponent(this.tauxRembITStranche3, -2, 183, -2)).addContainerGap()));
      jPanel18Layout.setVerticalGroup(jPanel18Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel18Layout.createSequentialGroup().addGap(8, 8, 8).addGroup(jPanel18Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel18Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel18Layout.createSequentialGroup().addComponent(this.jLabel85).addGap(0, 0, 0).addComponent(this.tauxRembITStranche3, -2, 30, -2)).addGroup(jPanel18Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel18Layout.createSequentialGroup().addComponent(this.jLabel87).addGap(0, 0, 0).addComponent(this.tauxRembITStranche1, -2, 30, -2)).addGroup(jPanel18Layout.createSequentialGroup().addComponent(this.jLabel86).addGap(0, 0, 0).addComponent(this.tauxRembITStranche2, -2, 30, -2)))).addComponent(this.cExonoreITS, -2, 46, -2)).addGap(9, 9, 9)));
      GroupLayout pTaxesLayout = new GroupLayout(this.pTaxes);
      this.pTaxes.setLayout(pTaxesLayout);
      pTaxesLayout.setHorizontalGroup(pTaxesLayout.createParallelGroup(Alignment.LEADING).addGroup(pTaxesLayout.createSequentialGroup().addContainerGap().addGroup(pTaxesLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jPanel18, -1, -1, 32767).addGroup(pTaxesLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jPanel16, Alignment.LEADING, -1, -1, 32767).addComponent(this.jPanel17, -2, 1062, -2))).addContainerGap(252, 32767)));
      pTaxesLayout.setVerticalGroup(pTaxesLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pTaxesLayout.createSequentialGroup().addGap(5, 5, 5).addComponent(this.jPanel17, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jPanel16, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jPanel18, -2, -1, -2).addGap(18, 18, 18)));
      this.pContrat_TabbedPane.addTab("Taxes et cotisations", this.pTaxes);
      this.jPanel3.setBackground(new Color(255, 255, 255));
      this.jLabel79.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel79.setForeground(new Color(0, 102, 153));
      this.jLabel79.setText("Dernier d\u00e9part Cng.");
      this.tDernierDepartInitial.setBorder(BorderFactory.createEtchedBorder());
      this.tDernierDepartInitial.setDateFormatString("dd/MM/yy");
      this.tDernierDepartInitial.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel84.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel84.setForeground(new Color(0, 102, 153));
      this.jLabel84.setText("Cumul BI Init.");
      this.tCumulBrutImposableInitial.setBorder(BorderFactory.createEtchedBorder());
      this.tCumulBrutImposableInitial.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tCumulBrutImposableInitial.setHorizontalAlignment(4);
      this.tCumulBrutImposableInitial.setFont(new Font("Segoe UI Light", 1, 12));
      this.tCumulBrutImposableInitial.addCaretListener(new 109(this));
      this.tCumulBrutImposableInitial.addFocusListener(new 110(this));
      this.tCumulBrutNonImposableInitial.setBorder(BorderFactory.createEtchedBorder());
      this.tCumulBrutNonImposableInitial.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tCumulBrutNonImposableInitial.setHorizontalAlignment(4);
      this.tCumulBrutNonImposableInitial.setFont(new Font("Segoe UI Light", 1, 12));
      this.tCumulBrutNonImposableInitial.addCaretListener(new 111(this));
      this.tCumulBrutNonImposableInitial.addFocusListener(new 112(this));
      this.jLabel88.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel88.setForeground(new Color(0, 102, 153));
      this.jLabel88.setText("Cumul BNI Init.");
      this.jLabel89.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel89.setForeground(new Color(0, 102, 153));
      this.jLabel89.setText("Cumul 12 Der. Mois");
      this.tCumul12DMinitial.setBorder(BorderFactory.createEtchedBorder());
      this.tCumul12DMinitial.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tCumul12DMinitial.setHorizontalAlignment(4);
      this.tCumul12DMinitial.setFont(new Font("Segoe UI Light", 1, 12));
      this.tCumul12DMinitial.addCaretListener(new 113(this));
      this.tCumul12DMinitial.addFocusListener(new 114(this));
      this.jPanel15.setBackground(new Color(255, 255, 255));
      this.jPanel15.setBorder(BorderFactory.createTitledBorder((Border)null, "Configuration de la semaine", 0, 0, new Font("Segoe UI Light", 1, 11)));
      this.jPanel15.setForeground(new Color(0, 102, 153));
      this.jLabel6.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel6.setForeground(new Color(0, 102, 153));
      this.jLabel6.setText("                                  LUN     MAR      MER      JEU      VEN      SAM     DIM");
      this.cLUNwe.setBackground(new Color(255, 255, 255));
      this.cLUNwe.setFont(new Font("Segoe UI Light", 0, 12));
      this.cLUNwe.setForeground(new Color(0, 102, 153));
      this.cLUNwe.addActionListener(new 115(this));
      this.cMARwe.setBackground(new Color(255, 255, 255));
      this.cMARwe.setFont(new Font("Segoe UI Light", 0, 12));
      this.cMARwe.setForeground(new Color(0, 102, 153));
      this.cMARwe.addActionListener(new 116(this));
      this.cDIMEnd.setBackground(new Color(255, 255, 255));
      this.cDIMEnd.setFont(new Font("Segoe UI Light", 0, 12));
      this.cDIMEnd.setForeground(new Color(0, 102, 153));
      this.cDIMEnd.addActionListener(new 117(this));
      this.cSAMEnd.setBackground(new Color(255, 255, 255));
      this.cSAMEnd.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSAMEnd.setForeground(new Color(0, 102, 153));
      this.cSAMEnd.addActionListener(new 118(this));
      this.cJEUwe.setBackground(new Color(255, 255, 255));
      this.cJEUwe.setFont(new Font("Segoe UI Light", 0, 12));
      this.cJEUwe.setForeground(new Color(0, 102, 153));
      this.cJEUwe.addActionListener(new 119(this));
      this.cVENwe.setBackground(new Color(255, 255, 255));
      this.cVENwe.setFont(new Font("Segoe UI Light", 0, 12));
      this.cVENwe.setForeground(new Color(0, 102, 153));
      this.cVENwe.addActionListener(new 120(this));
      this.cMAREnd.setBackground(new Color(255, 255, 255));
      this.cMAREnd.setFont(new Font("Segoe UI Light", 0, 12));
      this.cMAREnd.setForeground(new Color(0, 102, 153));
      this.cMAREnd.addActionListener(new 121(this));
      this.cMEREnd.setBackground(new Color(255, 255, 255));
      this.cMEREnd.setFont(new Font("Segoe UI Light", 0, 12));
      this.cMEREnd.setForeground(new Color(0, 102, 153));
      this.cMEREnd.addActionListener(new 122(this));
      this.cJEUEnd.setBackground(new Color(255, 255, 255));
      this.cJEUEnd.setFont(new Font("Segoe UI Light", 0, 12));
      this.cJEUEnd.setForeground(new Color(0, 102, 153));
      this.cJEUEnd.addActionListener(new 123(this));
      this.cVENEnd.setBackground(new Color(255, 255, 255));
      this.cVENEnd.setFont(new Font("Segoe UI Light", 0, 12));
      this.cVENEnd.setForeground(new Color(0, 102, 153));
      this.cVENEnd.addActionListener(new 124(this));
      this.cMERwe.setBackground(new Color(255, 255, 255));
      this.cMERwe.setFont(new Font("Segoe UI Light", 0, 12));
      this.cMERwe.setForeground(new Color(0, 102, 153));
      this.cMERwe.addActionListener(new 125(this));
      this.cSAMwe.setBackground(new Color(255, 255, 255));
      this.cSAMwe.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSAMwe.setForeground(new Color(0, 102, 153));
      this.cSAMwe.addActionListener(new 126(this));
      this.cLUNBegin.setBackground(new Color(255, 255, 255));
      this.cLUNBegin.setFont(new Font("Segoe UI Light", 0, 12));
      this.cLUNBegin.setForeground(new Color(0, 102, 153));
      this.cLUNBegin.addActionListener(new 127(this));
      this.cMARBegin.setBackground(new Color(255, 255, 255));
      this.cMARBegin.setFont(new Font("Segoe UI Light", 0, 12));
      this.cMARBegin.setForeground(new Color(0, 102, 153));
      this.cMARBegin.addActionListener(new 128(this));
      this.cMERBegin.setBackground(new Color(255, 255, 255));
      this.cMERBegin.setFont(new Font("Segoe UI Light", 0, 12));
      this.cMERBegin.setForeground(new Color(0, 102, 153));
      this.cMERBegin.addActionListener(new 129(this));
      this.cJEUBegin.setBackground(new Color(255, 255, 255));
      this.cJEUBegin.setFont(new Font("Segoe UI Light", 0, 12));
      this.cJEUBegin.setForeground(new Color(0, 102, 153));
      this.cJEUBegin.addActionListener(new 130(this));
      this.cVENBegin.setBackground(new Color(255, 255, 255));
      this.cVENBegin.setFont(new Font("Segoe UI Light", 0, 12));
      this.cVENBegin.setForeground(new Color(0, 102, 153));
      this.cVENBegin.addActionListener(new 131(this));
      this.cSAMBegin.setBackground(new Color(255, 255, 255));
      this.cSAMBegin.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSAMBegin.setForeground(new Color(0, 102, 153));
      this.cSAMBegin.addActionListener(new 132(this));
      this.cDIMBegin.setBackground(new Color(255, 255, 255));
      this.cDIMBegin.setFont(new Font("Segoe UI Light", 0, 12));
      this.cDIMBegin.setForeground(new Color(0, 102, 153));
      this.cDIMBegin.addActionListener(new 133(this));
      this.cLUNEnd.setBackground(new Color(255, 255, 255));
      this.cLUNEnd.setFont(new Font("Segoe UI Light", 0, 12));
      this.cLUNEnd.setForeground(new Color(0, 102, 153));
      this.cLUNEnd.addActionListener(new 134(this));
      this.cDIMwe.setBackground(new Color(255, 255, 255));
      this.cDIMwe.setFont(new Font("Segoe UI Light", 0, 12));
      this.cDIMwe.setForeground(new Color(0, 102, 153));
      this.cDIMwe.addActionListener(new 135(this));
      this.jLabel8.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel8.setForeground(new Color(0, 102, 153));
      this.jLabel8.setText("D\u00e9but de samaine");
      this.jLabel9.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel9.setForeground(new Color(0, 102, 153));
      this.jLabel9.setText("Find de semaine");
      this.jLabel11.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel11.setForeground(new Color(0, 102, 153));
      this.jLabel11.setText("Week-end");
      GroupLayout jPanel15Layout = new GroupLayout(this.jPanel15);
      this.jPanel15.setLayout(jPanel15Layout);
      jPanel15Layout.setHorizontalGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel15Layout.createSequentialGroup().addContainerGap().addGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel6, -2, 480, -2).addGroup(jPanel15Layout.createSequentialGroup().addGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel8, -1, 126, 32767).addComponent(this.jLabel9, -1, -1, 32767).addComponent(this.jLabel11, -1, -1, 32767)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cLUNBegin, -2, 40, -2).addComponent(this.cLUNEnd, -2, 40, -2).addComponent(this.cLUNwe, -2, 40, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel15Layout.createSequentialGroup().addComponent(this.cMAREnd, -2, 40, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cMEREnd, -2, 40, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cJEUEnd, -2, 40, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cVENEnd, -2, 40, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cSAMEnd, -2, 40, -2)).addGroup(jPanel15Layout.createSequentialGroup().addComponent(this.cMARwe, -2, 40, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cMERwe, -2, 40, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cJEUwe, -2, 40, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cVENwe, -2, 40, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cSAMwe, -2, 40, -2)).addGroup(jPanel15Layout.createSequentialGroup().addComponent(this.cMARBegin, -2, 40, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cMERBegin, -2, 40, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cJEUBegin, -2, 40, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cVENBegin, -2, 40, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cSAMBegin, -2, 40, -2))).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cDIMBegin, -2, 40, -2).addComponent(this.cDIMEnd, -2, 40, -2).addComponent(this.cDIMwe, -2, 42, -2)))).addContainerGap(-1, 32767)));
      jPanel15Layout.setVerticalGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel15Layout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jLabel6).addGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel15Layout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel15Layout.createSequentialGroup().addGroup(jPanel15Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.cMARBegin, -2, 30, -2).addComponent(this.cLUNBegin, -2, 30, -2).addComponent(this.cMERBegin, -2, 30, -2).addComponent(this.cJEUBegin, -2, 30, -2).addComponent(this.cVENBegin, -2, 30, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel15Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.cLUNEnd, -2, 30, -2).addComponent(this.cMAREnd, -2, 30, -2).addComponent(this.cMEREnd, -2, 30, -2).addComponent(this.cJEUEnd, -2, 30, -2).addComponent(this.cVENEnd, -2, 30, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel15Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.cLUNwe, -2, 30, -2).addComponent(this.cMARwe, -2, 30, -2).addComponent(this.cMERwe, -2, 30, -2).addComponent(this.cJEUwe, -2, 30, -2).addComponent(this.cVENwe, -2, 30, -2))).addGroup(jPanel15Layout.createSequentialGroup().addGroup(jPanel15Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.cDIMBegin, -2, 30, -2).addComponent(this.cSAMBegin, -2, 30, -2)).addGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel15Layout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cSAMEnd, -2, 30, -2)).addComponent(this.cDIMEnd, -2, 30, -2)).addGap(0, 0, 0).addGroup(jPanel15Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cSAMwe, -2, 30, -2).addComponent(this.cDIMwe, -2, 30, -2))))).addGroup(jPanel15Layout.createSequentialGroup().addComponent(this.jLabel8, -2, 32, -2).addGap(0, 0, 0).addComponent(this.jLabel9, -2, 32, -2).addGap(0, 0, 0).addComponent(this.jLabel11, -2, 32, -2)))));
      GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
      this.jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(17, 17, 17).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jPanel15, -2, -1, -2).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tDernierDepartInitial, -1, 152, 32767).addComponent(this.jLabel79, -1, -1, 32767)).addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel84, -2, 120, -2).addComponent(this.tCumulBrutImposableInitial, -2, 120, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel88, -2, 120, -2).addComponent(this.tCumulBrutNonImposableInitial, -2, 120, -2)).addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel89, -2, 120, -2).addComponent(this.tCumul12DMinitial, -2, 120, -2)))).addContainerGap(743, 32767)));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel84).addGap(0, 0, 0).addComponent(this.tCumulBrutImposableInitial, -2, 30, -2).addGap(37, 37, 37)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel88).addGap(0, 0, 0).addComponent(this.tCumulBrutNonImposableInitial, -2, 30, -2).addGap(37, 37, 37))).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel79).addGap(0, 0, 0).addComponent(this.tDernierDepartInitial, -2, 32, -2)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel89).addGap(0, 0, 0).addComponent(this.tCumul12DMinitial, -2, 30, -2))).addGap(35, 35, 35))).addComponent(this.jPanel15, -2, -1, -2).addContainerGap(229, 32767)));
      this.pContrat_TabbedPane.addTab("Autres", this.jPanel3);
      GroupLayout pContratLayout = new GroupLayout(this.pContrat);
      this.pContrat.setLayout(pContratLayout);
      pContratLayout.setHorizontalGroup(pContratLayout.createParallelGroup(Alignment.LEADING).addGroup(pContratLayout.createSequentialGroup().addContainerGap().addComponent(this.pContrat_TabbedPane, -2, 1320, -2).addContainerGap()));
      pContratLayout.setVerticalGroup(pContratLayout.createParallelGroup(Alignment.LEADING).addComponent(this.pContrat_TabbedPane));
      this.detailPanel.addTab("Contrat", this.pContrat);
      this.pPaie.setBackground(new Color(255, 255, 255));
      this.pPaie.setFont(new Font("Segoe UI Light", 0, 12));
      this.pPaie_TabbedPane.setForeground(new Color(0, 102, 153));
      this.pPaie_TabbedPane.setFont(new Font("SansSerif", 1, 11));
      this.pVariables.setBackground(new Color(255, 255, 255));
      this.pVariables.setFont(new Font("Segoe UI Light", 0, 12));
      this.jPanel11.setBackground(new Color(255, 255, 255));
      this.jLabel23.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel23.setForeground(new Color(0, 102, 153));
      this.jLabel23.setText("Motif");
      this.tMotif.setFont(new Font("Segoe UI Light", 1, 12));
      this.tMotif.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMotif.addActionListener(new 136(this));
      this.cOnMotif.setBackground(new Color(255, 255, 255));
      this.cOnMotif.setFont(new Font("Segoe UI Light", 0, 12));
      this.cOnMotif.setForeground(new Color(0, 102, 153));
      this.cOnMotif.setText("Sur Motif");
      this.cOnMotif.addActionListener(new 137(this));
      this.tPaieDu.setBorder(BorderFactory.createEtchedBorder());
      this.tPaieDu.setDateFormatString("dd/MM/yy");
      this.tPaieDu.setFont(new Font("Segoe UI Light", 1, 12));
      this.tPaieAu.setBorder(BorderFactory.createEtchedBorder());
      this.tPaieAu.setDateFormatString("dd/MM/yy");
      this.tPaieAu.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel24.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel24.setForeground(new Color(0, 102, 153));
      this.jLabel24.setText("Paie du");
      this.jLabel25.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel25.setForeground(new Color(0, 102, 153));
      this.jLabel25.setText("Paie au");
      this.jLabel26.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel26.setForeground(new Color(0, 102, 153));
      this.jLabel26.setText("NJT");
      this.tNjt.setBorder(BorderFactory.createEtchedBorder());
      this.tNjt.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tNjt.setHorizontalAlignment(0);
      this.tNjt.setToolTipText("Npmbre de jours travaill\u00e9s");
      this.tNjt.setFont(new Font("Segoe UI Light", 1, 14));
      this.tNjt.addCaretListener(new 138(this));
      this.tNjt.addFocusListener(new 139(this));
      this.btnSaveNJT.setToolTipText("Sauvegarder le NJT");
      this.btnSaveNJT.setContentAreaFilled(false);
      this.btnSaveNJT.setCursor(new Cursor(12));
      this.btnSaveNJT.setOpaque(true);
      this.btnSaveNJT.addActionListener(new 140(this));
      this.buttonConges.setToolTipText("Sauvegarder le NJT");
      this.buttonConges.setContentAreaFilled(false);
      this.buttonConges.setCursor(new Cursor(12));
      this.buttonConges.setOpaque(true);
      this.buttonConges.addActionListener(new 141(this));
      GroupLayout jPanel11Layout = new GroupLayout(this.jPanel11);
      this.jPanel11.setLayout(jPanel11Layout);
      jPanel11Layout.setHorizontalGroup(jPanel11Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel11Layout.createSequentialGroup().addContainerGap().addGroup(jPanel11Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tMotif, 0, 285, 32767).addComponent(this.jLabel23, -1, -1, 32767)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cOnMotif, -2, 92, -2).addGap(100, 100, 100).addGroup(jPanel11Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel24, -1, -1, 32767).addComponent(this.tPaieDu, -2, 120, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel11Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel25, -2, 120, -2).addComponent(this.tPaieAu, -2, 120, -2)).addGap(18, 18, 18).addComponent(this.buttonConges, -2, 210, -2).addGap(63, 63, 63).addGroup(jPanel11Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel26, -2, 71, -2).addGroup(jPanel11Layout.createSequentialGroup().addComponent(this.tNjt, -2, 71, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnSaveNJT, -2, 35, -2))).addGap(84, 84, 84)));
      jPanel11Layout.setVerticalGroup(jPanel11Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel11Layout.createSequentialGroup().addContainerGap(15, 32767).addGroup(jPanel11Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel11Layout.createSequentialGroup().addComponent(this.jLabel26).addGap(2, 2, 2).addGroup(jPanel11Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.btnSaveNJT, -1, -1, 32767).addComponent(this.tNjt, -2, 30, -2))).addGroup(jPanel11Layout.createSequentialGroup().addGroup(jPanel11Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel24).addComponent(this.jLabel25)).addGap(2, 2, 2).addGroup(jPanel11Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPaieAu, -1, -1, 32767).addComponent(this.tPaieDu, -1, -1, 32767).addComponent(this.buttonConges, -2, 32, -2))).addGroup(jPanel11Layout.createSequentialGroup().addComponent(this.jLabel23).addGap(2, 2, 2).addGroup(jPanel11Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tMotif, -1, 30, 32767).addComponent(this.cOnMotif, -1, -1, 32767)))).addGap(3, 3, 3)));
      this.jPanel12.setBackground(new Color(255, 255, 255));
      this.jPanel12.setBorder(BorderFactory.createEtchedBorder());
      this.rubriquePaieTable.setFont(new Font("Segoe UI Light", 1, 11));
      this.rubriquePaieTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.rubriquePaieTable.setSelectionBackground(new Color(0, 102, 153));
      this.rubriquePaieTable.setShowGrid(false);
      this.rubriquePaieTable.addMouseListener(new 142(this));
      this.jScrollPane10.setViewportView(this.rubriquePaieTable);
      this.jLabel27.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel27.setForeground(new Color(0, 102, 153));
      this.jLabel27.setText("Rubrique");
      this.tRubriques.setFont(new Font("Segoe UI Light", 1, 12));
      this.tRubriques.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tRubriques.addFocusListener(new 143(this));
      this.tRubriques.addActionListener(new 144(this));
      this.jLabel28.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel28.setForeground(new Color(0, 102, 153));
      this.jLabel28.setText("Base");
      this.tBase.setBorder(BorderFactory.createEtchedBorder());
      this.tBase.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tBase.setHorizontalAlignment(4);
      this.tBase.setFont(new Font("Segoe UI Light", 1, 14));
      this.tBase.addCaretListener(new 145(this));
      this.tBase.addFocusListener(new 146(this));
      this.jLabel29.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel29.setForeground(new Color(0, 102, 153));
      this.jLabel29.setText("Nombre");
      this.tNombre.setBorder(BorderFactory.createEtchedBorder());
      this.tNombre.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tNombre.setHorizontalAlignment(4);
      this.tNombre.setFont(new Font("Segoe UI Light", 1, 14));
      this.tNombre.addCaretListener(new 147(this));
      this.tNombre.addFocusListener(new 148(this));
      this.tMontant.setEditable(false);
      this.tMontant.setBorder(BorderFactory.createEtchedBorder());
      this.tMontant.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tMontant.setHorizontalAlignment(4);
      this.tMontant.setFont(new Font("Segoe UI Light", 1, 14));
      this.tMontant.addCaretListener(new 149(this));
      this.jLabel30.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel30.setForeground(new Color(0, 102, 153));
      this.jLabel30.setText("Montant");
      this.cFixe.setBackground(new Color(255, 255, 255));
      this.cFixe.setFont(new Font("Segoe UI Light", 1, 12));
      this.cFixe.setForeground(new Color(0, 102, 153));
      this.cFixe.setText("Fixe");
      this.cFixe.addActionListener(new 150(this));
      this.btnSavePaie.setToolTipText("Sauvegarder la rubrique de paie");
      this.btnSavePaie.setContentAreaFilled(false);
      this.btnSavePaie.setCursor(new Cursor(12));
      this.btnSavePaie.setOpaque(true);
      this.btnSavePaie.addActionListener(new 151(this));
      this.btnDelPaie.setToolTipText("Supprimer  la rubrique de paie");
      this.btnDelPaie.setContentAreaFilled(false);
      this.btnDelPaie.setCursor(new Cursor(12));
      this.btnDelPaie.setOpaque(true);
      this.btnDelPaie.addActionListener(new 152(this));
      GroupLayout jPanel12Layout = new GroupLayout(this.jPanel12);
      this.jPanel12.setLayout(jPanel12Layout);
      jPanel12Layout.setHorizontalGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel12Layout.createSequentialGroup().addGap(5, 5, 5).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING, false).addGroup(jPanel12Layout.createSequentialGroup().addGroup(jPanel12Layout.createParallelGroup(Alignment.TRAILING, false).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.tRubriques, -2, 427, -2).addGap(18, 18, 18)).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.jLabel27, -1, -1, 32767).addGap(110, 110, 110))).addGroup(jPanel12Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel28, Alignment.LEADING, -2, 150, -2).addComponent(this.tBase, Alignment.LEADING, -2, 150, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel12Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel29, Alignment.LEADING, -2, 150, -2).addComponent(this.tNombre, Alignment.LEADING, -2, 150, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel12Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel30, Alignment.LEADING, -2, 150, -2).addComponent(this.tMontant, Alignment.LEADING, -2, 150, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cFixe).addGap(20, 20, 20).addComponent(this.btnSavePaie, -2, 40, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnDelPaie, -2, 40, -2)).addComponent(this.jScrollPane10, -2, 1108, -2)).addGap(28, 28, 28)));
      jPanel12Layout.setVerticalGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel12Layout.createSequentialGroup().addGap(4, 4, 4).addGroup(jPanel12Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.jLabel27).addGap(0, 0, 0).addComponent(this.tRubriques, -2, 30, -2)).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.jLabel28).addGap(0, 0, 0).addComponent(this.tBase, -2, 30, -2))).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.jLabel29).addGap(0, 0, 0).addGroup(jPanel12Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tNombre, -2, 30, -2).addComponent(this.tMontant, -2, 30, -2))).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.jLabel30).addGap(0, 0, 0).addComponent(this.cFixe, -2, 33, -2)).addComponent(this.btnSavePaie, -2, 40, -2).addComponent(this.btnDelPaie, -2, 40, -2)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jScrollPane10, -2, 253, -2).addContainerGap()));
      this.jPanel13.setBackground(new Color(255, 255, 255));
      this.jPanel13.setBorder(BorderFactory.createEtchedBorder());
      this.tNoteSurBulletin.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNoteSurBulletin.setBorder(BorderFactory.createEtchedBorder());
      this.tNoteSurBulletin.addKeyListener(new 153(this));
      this.jLabel13.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel13.setForeground(new Color(0, 102, 153));
      this.jLabel13.setText("Note sur bulletin de paie");
      this.tBARelicat.setEditable(false);
      this.tBARelicat.setBorder(BorderFactory.createEtchedBorder());
      this.tBARelicat.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tBARelicat.setHorizontalAlignment(4);
      this.tBARelicat.setFont(new Font("Segoe UI Light", 1, 14));
      this.tBARelicat.addCaretListener(new 154(this));
      this.jLabel32.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel32.setForeground(new Color(0, 102, 153));
      this.jLabel32.setText("Cons./Budget  (%)");
      this.btnSaveEmpNote.setToolTipText("Sauvegarder la note sur le bulletin de paie");
      this.btnSaveEmpNote.setContentAreaFilled(false);
      this.btnSaveEmpNote.setCursor(new Cursor(12));
      this.btnSaveEmpNote.setOpaque(true);
      this.btnSaveEmpNote.addActionListener(new 155(this));
      this.tTotalGainsFix.setBackground(new Color(255, 255, 255));
      this.tTotalGainsFix.setFont(new Font("Segoe UI Light", 1, 11));
      this.tTotalGainsFix.setForeground(new Color(0, 102, 255));
      this.tTotalGainsFix.setHorizontalAlignment(0);
      this.tTotalGainsFix.setText("0");
      this.tTotalGainsFix.setToolTipText("Total gain fixe");
      this.tTotalGainsFix.setOpaque(true);
      this.tTotalGainsVar.setBackground(new Color(255, 255, 255));
      this.tTotalGainsVar.setFont(new Font("Segoe UI Light", 1, 11));
      this.tTotalGainsVar.setHorizontalAlignment(0);
      this.tTotalGainsVar.setText("0");
      this.tTotalGainsVar.setToolTipText("Total gain variable");
      this.tTotalGainsVar.setOpaque(true);
      this.tTotalRetenues.setBackground(new Color(255, 255, 255));
      this.tTotalRetenues.setFont(new Font("Segoe UI Light", 1, 11));
      this.tTotalRetenues.setForeground(new Color(255, 51, 0));
      this.tTotalRetenues.setHorizontalAlignment(0);
      this.tTotalRetenues.setText("0");
      this.tTotalRetenues.setToolTipText("Total retenues");
      this.tTotalRetenues.setOpaque(true);
      GroupLayout jPanel13Layout = new GroupLayout(this.jPanel13);
      this.jPanel13.setLayout(jPanel13Layout);
      jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel13Layout.createSequentialGroup().addContainerGap(-1, 32767).addGroup(jPanel13Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tNoteSurBulletin).addComponent(this.jLabel13, -1, 600, 32767)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSaveEmpNote, -2, 35, -2).addGap(29, 29, 29).addGroup(jPanel13Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel13Layout.createSequentialGroup().addComponent(this.jSeparator17, -2, 187, -2).addGap(397, 397, 397)).addGroup(jPanel13Layout.createSequentialGroup().addComponent(this.tTotalGainsFix, -2, 85, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.tTotalGainsVar, -2, 85, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.tTotalRetenues, -2, 85, -2).addGap(44, 44, 44).addGroup(jPanel13Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tBARelicat, -2, 110, -2).addComponent(this.jLabel32, -2, 110, -2)).addGap(187, 187, 187)))));
      jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel13Layout.createSequentialGroup().addGroup(jPanel13Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel13Layout.createSequentialGroup().addComponent(this.jLabel32).addGap(0, 0, 0).addComponent(this.tBARelicat, -2, 30, -2)).addGroup(jPanel13Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel13Layout.createSequentialGroup().addContainerGap().addGroup(jPanel13Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tTotalGainsFix, -2, 25, -2).addComponent(this.tTotalGainsVar, -2, 25, -2).addComponent(this.tTotalRetenues, -2, 25, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jSeparator17, -2, 0, -2)).addGroup(jPanel13Layout.createSequentialGroup().addGap(4, 4, 4).addGroup(jPanel13Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.btnSaveEmpNote, -2, 35, -2).addGroup(jPanel13Layout.createSequentialGroup().addComponent(this.jLabel13).addGap(0, 0, 0).addComponent(this.tNoteSurBulletin, -2, 30, -2).addGap(2, 2, 2)))))).addGap(15, 15, 15)));
      this.jPanel10.setBackground(new Color(255, 255, 255));
      this.jPanel10.setBorder(BorderFactory.createEtchedBorder());
      this.btnPrintPaie.setToolTipText("Bulletin de paie");
      this.btnPrintPaie.setContentAreaFilled(false);
      this.btnPrintPaie.setCursor(new Cursor(12));
      this.btnPrintPaie.setOpaque(true);
      this.btnPrintPaie.addActionListener(new 156(this));
      this.btnCalPaie.setToolTipText("Calculer la paie");
      this.btnCalPaie.setContentAreaFilled(false);
      this.btnCalPaie.setCursor(new Cursor(12));
      this.btnCalPaie.setOpaque(true);
      this.btnCalPaie.addActionListener(new 157(this));
      this.btnInfosPaie.setToolTipText("Informations actuelles de la paie");
      this.btnInfosPaie.setContentAreaFilled(false);
      this.btnInfosPaie.setCursor(new Cursor(12));
      this.btnInfosPaie.setOpaque(true);
      this.btnInfosPaie.addActionListener(new 158(this));
      GroupLayout jPanel10Layout = new GroupLayout(this.jPanel10);
      this.jPanel10.setLayout(jPanel10Layout);
      jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel10Layout.createSequentialGroup().addGap(14, 14, 14).addGroup(jPanel10Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnInfosPaie, -2, 40, -2).addGroup(jPanel10Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnCalPaie, -2, 40, -2).addComponent(this.btnPrintPaie, -2, 40, -2))).addGap(16, 16, 16)));
      jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel10Layout.createSequentialGroup().addContainerGap().addComponent(this.btnInfosPaie, -2, 40, -2).addGap(18, 18, 18).addComponent(this.btnCalPaie, -2, 40, -2).addGap(18, 18, 18).addComponent(this.btnPrintPaie, -2, 40, -2).addGap(151, 151, 151)));
      GroupLayout pVariablesLayout = new GroupLayout(this.pVariables);
      this.pVariables.setLayout(pVariablesLayout);
      pVariablesLayout.setHorizontalGroup(pVariablesLayout.createParallelGroup(Alignment.LEADING).addGroup(pVariablesLayout.createSequentialGroup().addContainerGap().addGroup(pVariablesLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jPanel13, -2, 0, 32767).addGroup(pVariablesLayout.createSequentialGroup().addComponent(this.jPanel12, -2, 1142, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jPanel10, -2, -1, -2)).addComponent(this.jPanel11, -1, -1, 32767)).addGap(92, 92, 92)));
      pVariablesLayout.setVerticalGroup(pVariablesLayout.createParallelGroup(Alignment.LEADING).addGroup(pVariablesLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jPanel11, -2, -1, -2).addGap(2, 2, 2).addGroup(pVariablesLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jPanel12, -2, -1, -2).addComponent(this.jPanel10, -2, -1, -2)).addGap(2, 2, 2).addComponent(this.jPanel13, -2, -1, -2).addGap(8, 8, 8)));
      this.pPaie_TabbedPane.addTab("Variables", this.pVariables);
      this.rappelSBPanel.setBackground(new Color(255, 255, 255));
      this.panelHS1.setForeground(new Color(0, 102, 153));
      this.panelHS1.setFont(new Font("Segoe UI Light", 1, 12));
      this.jPanel27.setBackground(new Color(255, 255, 255));
      this.tDebutRappel.setBorder(BorderFactory.createEtchedBorder());
      this.tDebutRappel.setDateFormatString("EEEE dd/MM/yy");
      this.tDebutRappel.setFont(new Font("Segoe UI Light", 1, 14));
      this.tDebutRappel.addPropertyChangeListener(new 159(this));
      this.jLabel121.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel121.setForeground(new Color(0, 102, 153));
      this.jLabel121.setText("D\u00e9but du rappel");
      this.jLabel125.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel125.setForeground(new Color(0, 102, 153));
      this.jLabel125.setText("Motif");
      this.tMotifRappel.setFont(new Font("Segoe UI Light", 1, 12));
      this.tMotifRappel.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMotifRappel.addActionListener(new 160(this));
      this.jLabel154.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel154.setForeground(new Color(0, 102, 153));
      this.jLabel154.setText("P\u00e9riode du rappel");
      this.tPeriodeRappel.setFont(new Font("Segoe UI Light", 0, 12));
      this.tPeriodeRappel.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriodeRappel.addActionListener(new 161(this));
      GroupLayout jPanel27Layout = new GroupLayout(this.jPanel27);
      this.jPanel27.setLayout(jPanel27Layout);
      jPanel27Layout.setHorizontalGroup(jPanel27Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel27Layout.createSequentialGroup().addContainerGap().addGroup(jPanel27Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel27Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel125, -1, -1, 32767).addComponent(this.jLabel121, Alignment.LEADING, -2, 100, -2).addComponent(this.tDebutRappel, Alignment.LEADING, -2, 174, -2).addComponent(this.tMotifRappel, 0, 285, 32767)).addGroup(jPanel27Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriodeRappel, 0, -1, 32767).addComponent(this.jLabel154, -2, 144, -2))).addContainerGap()));
      jPanel27Layout.setVerticalGroup(jPanel27Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel27Layout.createSequentialGroup().addGap(9, 9, 9).addComponent(this.jLabel121).addGap(0, 0, 0).addComponent(this.tDebutRappel, -2, 32, -2).addGap(18, 18, 18).addComponent(this.jLabel125).addGap(0, 0, 0).addComponent(this.tMotifRappel, -2, 30, -2).addGap(18, 18, 18).addComponent(this.jLabel154).addGap(0, 0, 0).addComponent(this.tPeriodeRappel, -2, 30, -2).addGap(0, 0, 0)));
      this.panelHS1.addTab("Param\u00e8tres du rappel", this.jPanel27);
      this.jPanel28.setBackground(new Color(255, 255, 255));
      this.jLabel153.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel153.setForeground(new Color(0, 102, 153));
      this.jLabel153.setHorizontalAlignment(2);
      this.jLabel153.setText("ID Rub.");
      this.tIDRubRappel.setEditable(false);
      this.tIDRubRappel.setBorder(BorderFactory.createEtchedBorder());
      this.tIDRubRappel.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tIDRubRappel.setHorizontalAlignment(0);
      this.tIDRubRappel.setFont(new Font("Segoe UI Light", 1, 14));
      this.tIDRubRappel.addCaretListener(new 162(this));
      this.tIDRubRappel.addFocusListener(new 163(this));
      this.rubriquesSurSBTable.setFont(new Font("Segoe UI Light", 0, 11));
      this.rubriquesSurSBTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.rubriquesSurSBTable.setSelectionBackground(new Color(0, 102, 153));
      this.rubriquesSurSBTable.setShowGrid(false);
      this.rubriquesSurSBTable.addMouseListener(new 164(this));
      this.jScrollPane18.setViewportView(this.rubriquesSurSBTable);
      GroupLayout jPanel28Layout = new GroupLayout(this.jPanel28);
      this.jPanel28.setLayout(jPanel28Layout);
      jPanel28Layout.setHorizontalGroup(jPanel28Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel28Layout.createSequentialGroup().addGroup(jPanel28Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel28Layout.createSequentialGroup().addGap(286, 286, 286).addGroup(jPanel28Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel153, Alignment.LEADING, -2, 50, -2).addComponent(this.tIDRubRappel, Alignment.LEADING, -2, 50, -2))).addGroup(jPanel28Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane18, -2, 357, -2))).addGap(26, 26, 26)));
      jPanel28Layout.setVerticalGroup(jPanel28Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel28Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane18, -2, 258, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel153).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tIDRubRappel, -2, 30, -2).addContainerGap(-1, 32767)));
      this.panelHS1.addTab("Rubriques cibl\u00e9es", this.jPanel28);
      this.rappelTable.setFont(new Font("Segoe UI Light", 0, 11));
      this.rappelTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.rappelTable.setSelectionBackground(new Color(0, 102, 153));
      this.rappelTable.setShowGrid(false);
      this.rappelTable.addMouseListener(new 165(this));
      this.jScrollPane17.setViewportView(this.rappelTable);
      this.jLabel122.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel122.setForeground(new Color(0, 102, 153));
      this.jLabel122.setText("Montant total du rappel");
      this.tMontantRappel.setEditable(false);
      this.tMontantRappel.setBorder(BorderFactory.createEtchedBorder());
      this.tMontantRappel.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tMontantRappel.setHorizontalAlignment(4);
      this.tMontantRappel.setFont(new Font("Segoe UI Light", 1, 14));
      this.tMontantRappel.addCaretListener(new 166(this));
      this.btnSaveRappelSB.setToolTipText("Sauvegarder");
      this.btnSaveRappelSB.setContentAreaFilled(false);
      this.btnSaveRappelSB.setCursor(new Cursor(12));
      this.btnSaveRappelSB.setOpaque(true);
      this.btnSaveRappelSB.addActionListener(new 167(this));
      this.jLabel144.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel144.setForeground(new Color(0, 102, 153));
      this.jLabel144.setText("Liste des rappels par p\u00e9riode");
      GroupLayout rappelSBPanelLayout = new GroupLayout(this.rappelSBPanel);
      this.rappelSBPanel.setLayout(rappelSBPanelLayout);
      rappelSBPanelLayout.setHorizontalGroup(rappelSBPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(rappelSBPanelLayout.createSequentialGroup().addContainerGap().addGroup(rappelSBPanelLayout.createParallelGroup(Alignment.TRAILING).addGroup(rappelSBPanelLayout.createSequentialGroup().addComponent(this.btnSaveRappelSB, -2, 35, -2).addGap(18, 18, 18).addGroup(rappelSBPanelLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel122, Alignment.LEADING, -2, 150, -2).addComponent(this.tMontantRappel, Alignment.LEADING, -2, 150, -2))).addGroup(rappelSBPanelLayout.createSequentialGroup().addComponent(this.panelHS1, -2, 389, -2).addGap(12, 12, 12).addGroup(rappelSBPanelLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel144, -2, 201, -2).addComponent(this.jScrollPane17, -2, 606, -2)))).addGap(33, 33, 33)));
      rappelSBPanelLayout.setVerticalGroup(rappelSBPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, rappelSBPanelLayout.createSequentialGroup().addContainerGap().addGroup(rappelSBPanelLayout.createParallelGroup(Alignment.TRAILING, false).addGroup(rappelSBPanelLayout.createSequentialGroup().addComponent(this.jLabel144).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane17, -2, 341, -2)).addComponent(this.panelHS1)).addPreferredGap(ComponentPlacement.RELATED).addGroup(rappelSBPanelLayout.createParallelGroup(Alignment.TRAILING).addGroup(rappelSBPanelLayout.createSequentialGroup().addComponent(this.jLabel122).addGap(0, 0, 0).addComponent(this.tMontantRappel, -2, 30, -2)).addComponent(this.btnSaveRappelSB, -2, 35, -2)).addGap(46, 46, 46)));
      this.pPaie_TabbedPane.addTab("Rappel / Sal. Base", this.rappelSBPanel);
      this.pointageHSPanel.setBackground(new Color(255, 255, 255));
      this.pointageHSPanel.setFont(new Font("Segoe UI Light", 0, 12));
      this.panelHS.setForeground(new Color(0, 102, 153));
      this.panelHS.setFont(new Font("SansSerif", 1, 11));
      this.panelHS.addMouseListener(new 168(this));
      this.jPanel25.setBackground(new Color(255, 255, 255));
      this.tDateJour.setBorder(BorderFactory.createEtchedBorder());
      this.tDateJour.setDateFormatString("EEEE dd/MM/yyyy");
      this.tDateJour.setFont(new Font("Segoe UI Light", 1, 14));
      this.tDateJour.addPropertyChangeListener(new 169(this));
      this.jLabel106.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel106.setForeground(new Color(0, 102, 153));
      this.jLabel106.setText("Date du jour");
      this.cFerie50.setBackground(new Color(255, 255, 255));
      this.cFerie50.setFont(new Font("Segoe UI Light", 1, 12));
      this.cFerie50.setForeground(new Color(0, 102, 153));
      this.cFerie50.setText("Feri\u00e9 150%");
      this.cFerie50.addActionListener(new 170(this));
      this.jLabel117.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel117.setForeground(new Color(0, 102, 153));
      this.jLabel117.setText("Note");
      this.tNoteJour.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNoteJour.setBorder(BorderFactory.createEtchedBorder());
      this.tNoteJour.addKeyListener(new 171(this));
      this.btnSaveHSDay.setToolTipText("Sauvegarder");
      this.btnSaveHSDay.setContentAreaFilled(false);
      this.btnSaveHSDay.setCursor(new Cursor(12));
      this.btnSaveHSDay.setOpaque(true);
      this.btnSaveHSDay.addActionListener(new 172(this));
      this.btnDelHSDay.setToolTipText("Supprimer");
      this.btnDelHSDay.setContentAreaFilled(false);
      this.btnDelHSDay.setCursor(new Cursor(12));
      this.btnDelHSDay.setOpaque(true);
      this.btnDelHSDay.addActionListener(new 173(this));
      this.jLabel134.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel134.setForeground(new Color(0, 102, 153));
      this.jLabel134.setText("Prime de panier");
      this.tPrimesPanier.setBorder(BorderFactory.createEtchedBorder());
      this.tPrimesPanier.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00"))));
      this.tPrimesPanier.setHorizontalAlignment(0);
      this.tPrimesPanier.setFont(new Font("Segoe UI Light", 1, 14));
      this.tPrimesPanier.setValue(0);
      this.tPrimesPanier.addCaretListener(new 174(this));
      this.tPrimesPanier.addFocusListener(new 175(this));
      this.tPrimesPanier.addKeyListener(new 176(this));
      this.jLabel135.setFont(new Font("Segoe UI Light", 1, 10));
      this.jLabel135.setForeground(new Color(0, 102, 153));
      this.jLabel135.setText("HN (de 22h \u00e0 6h)");
      this.tHeuresNuit.setBorder(BorderFactory.createEtchedBorder());
      this.tHeuresNuit.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00"))));
      this.tHeuresNuit.setHorizontalAlignment(0);
      this.tHeuresNuit.setFont(new Font("Segoe UI Light", 1, 14));
      this.tHeuresNuit.setValue(0);
      this.tHeuresNuit.addCaretListener(new 177(this));
      this.tHeuresNuit.addFocusListener(new 178(this));
      this.tHeuresNuit.addKeyListener(new 179(this));
      this.cFerie100.setBackground(new Color(255, 255, 255));
      this.cFerie100.setFont(new Font("Segoe UI Light", 1, 12));
      this.cFerie100.setForeground(new Color(0, 102, 153));
      this.cFerie100.setText("Feri\u00e9 200%");
      this.cFerie100.addActionListener(new 180(this));
      this.cSiteExterne.setBackground(new Color(255, 255, 255));
      this.cSiteExterne.setFont(new Font("Segoe UI Light", 1, 12));
      this.cSiteExterne.setForeground(new Color(0, 102, 153));
      this.cSiteExterne.setText("Site externe");
      this.cSiteExterne.addActionListener(new 181(this));
      this.jLabel136.setFont(new Font("Segoe UI Light", 1, 10));
      this.jLabel136.setForeground(new Color(0, 102, 153));
      this.jLabel136.setText("HJ (de 6h \u00e0 22h)");
      this.tHeuresJour.setBorder(BorderFactory.createEtchedBorder());
      this.tHeuresJour.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00"))));
      this.tHeuresJour.setHorizontalAlignment(0);
      this.tHeuresJour.setToolTipText("Heures de jour (de 6h \u00e0 22h)");
      this.tHeuresJour.setFont(new Font("Segoe UI Light", 1, 14));
      this.tHeuresJour.setValue(0);
      this.tHeuresJour.addCaretListener(new 182(this));
      this.tHeuresJour.addFocusListener(new 183(this));
      this.tHeuresJour.addActionListener(new 184(this));
      this.tHeuresJour.addKeyListener(new 185(this));
      GroupLayout jPanel25Layout = new GroupLayout(this.jPanel25);
      this.jPanel25.setLayout(jPanel25Layout);
      jPanel25Layout.setHorizontalGroup(jPanel25Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel25Layout.createSequentialGroup().addContainerGap().addGroup(jPanel25Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel25Layout.createSequentialGroup().addGroup(jPanel25Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel106, -2, 100, -2).addGroup(jPanel25Layout.createSequentialGroup().addComponent(this.cFerie50).addGap(18, 18, 18).addComponent(this.cFerie100).addGap(18, 18, 18).addComponent(this.cSiteExterne)).addComponent(this.jSeparator93, -2, 150, -2).addComponent(this.tDateJour, -2, 309, -2).addGroup(jPanel25Layout.createSequentialGroup().addComponent(this.jLabel117, -2, 300, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jSeparator92, -2, 150, -2)).addComponent(this.tNoteJour, -2, 420, -2)).addContainerGap()).addGroup(Alignment.TRAILING, jPanel25Layout.createSequentialGroup().addGroup(jPanel25Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel25Layout.createSequentialGroup().addGap(335, 335, 335).addComponent(this.btnSaveHSDay, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelHSDay, -2, 35, -2)).addGroup(jPanel25Layout.createSequentialGroup().addGroup(jPanel25Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel136, -2, 100, -2).addComponent(this.tHeuresJour, -2, 100, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel25Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.tHeuresNuit, Alignment.LEADING, -2, 100, -2).addComponent(this.jLabel135, Alignment.LEADING, -2, 100, -2)).addGap(100, 100, 100).addGroup(jPanel25Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel134, Alignment.LEADING, -2, 100, -2).addComponent(this.tPrimesPanier, Alignment.LEADING, -2, 100, -2)))).addGap(45, 45, 45)))));
      jPanel25Layout.setVerticalGroup(jPanel25Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel25Layout.createSequentialGroup().addComponent(this.jLabel106).addGap(0, 0, 0).addComponent(this.tDateJour, -2, 32, -2).addGap(2, 2, 2).addGroup(jPanel25Layout.createParallelGroup(Alignment.BASELINE, false).addComponent(this.cFerie50, -1, -1, 32767).addComponent(this.cFerie100).addComponent(this.cSiteExterne)).addGap(18, 18, 18).addGroup(jPanel25Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel25Layout.createSequentialGroup().addGroup(jPanel25Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel25Layout.createSequentialGroup().addComponent(this.jLabel135).addGap(0, 0, 0).addComponent(this.tHeuresNuit, -2, 30, -2)).addGroup(jPanel25Layout.createSequentialGroup().addComponent(this.jLabel136).addGap(0, 0, 0).addComponent(this.tHeuresJour, -2, 30, -2))).addGap(0, 0, 0).addComponent(this.jSeparator93, -2, 0, -2)).addGroup(jPanel25Layout.createSequentialGroup().addComponent(this.jLabel134).addGap(0, 0, 0).addComponent(this.tPrimesPanier, -2, 30, -2))).addGap(20, 20, 20).addComponent(this.jLabel117).addGap(0, 0, 0).addComponent(this.tNoteJour, -2, 30, -2).addGap(5, 5, 5).addGroup(jPanel25Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelHSDay, -2, 35, -2).addComponent(this.btnSaveHSDay, -2, 35, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jSeparator92, -2, 0, -2).addGap(103, 103, 103)));
      this.panelHS.addTab("Mode quotidien", this.jPanel25);
      this.jPanel26.setBackground(new Color(255, 255, 255));
      this.jLabel116.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel116.setForeground(new Color(0, 102, 153));
      this.jLabel116.setText("D\u00e9but de la semaine");
      this.tBeginWeek.setBorder(BorderFactory.createEtchedBorder());
      this.tBeginWeek.setDateFormatString("EEEE dd/MM/yyyy");
      this.tBeginWeek.setFont(new Font("Segoe UI Light", 1, 14));
      this.tBeginWeek.addFocusListener(new 186(this));
      this.jLabel137.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel137.setForeground(new Color(0, 102, 153));
      this.jLabel137.setText("Fin de la semanine");
      this.tEndWeek.setBorder(BorderFactory.createEtchedBorder());
      this.tEndWeek.setDateFormatString("EEEE dd/MM/yyyy");
      this.tEndWeek.setFont(new Font("Segoe UI Light", 1, 14));
      this.jLabel138.setFont(new Font("Segoe UI Light", 1, 11));
      this.jLabel138.setForeground(new Color(0, 102, 153));
      this.jLabel138.setHorizontalAlignment(0);
      this.jLabel138.setText("HS 150%");
      this.jLabel139.setFont(new Font("Segoe UI Light", 1, 11));
      this.jLabel139.setForeground(new Color(0, 102, 153));
      this.jLabel139.setHorizontalAlignment(0);
      this.jLabel139.setText("HS 140%");
      this.tWeekOT200.setBackground(new Color(255, 0, 102));
      this.tWeekOT200.setBorder(BorderFactory.createEtchedBorder());
      this.tWeekOT200.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tWeekOT200.setHorizontalAlignment(0);
      this.tWeekOT200.setFont(new Font("Segoe UI Light", 1, 14));
      this.tWeekOT200.addCaretListener(new 187(this));
      this.tWeekOT200.addFocusListener(new 188(this));
      this.tWeekOT150.setBackground(new Color(0, 204, 204));
      this.tWeekOT150.setBorder(BorderFactory.createEtchedBorder());
      this.tWeekOT150.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tWeekOT150.setHorizontalAlignment(0);
      this.tWeekOT150.setFont(new Font("Segoe UI Light", 1, 14));
      this.tWeekOT150.addCaretListener(new 189(this));
      this.tWeekOT150.addFocusListener(new 190(this));
      this.tWeekOT140.setBackground(new Color(255, 204, 51));
      this.tWeekOT140.setBorder(BorderFactory.createEtchedBorder());
      this.tWeekOT140.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tWeekOT140.setHorizontalAlignment(0);
      this.tWeekOT140.setFont(new Font("Segoe UI Light", 1, 14));
      this.tWeekOT140.addCaretListener(new 191(this));
      this.tWeekOT140.addFocusListener(new 192(this));
      this.tWeekOT115.setBackground(new Color(102, 204, 0));
      this.tWeekOT115.setBorder(BorderFactory.createEtchedBorder());
      this.tWeekOT115.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tWeekOT115.setHorizontalAlignment(0);
      this.tWeekOT115.setFont(new Font("Segoe UI Light", 1, 14));
      this.tWeekOT115.addCaretListener(new 193(this));
      this.tWeekOT115.addFocusListener(new 194(this));
      this.jLabel140.setFont(new Font("Segoe UI Light", 1, 11));
      this.jLabel140.setForeground(new Color(0, 102, 153));
      this.jLabel140.setHorizontalAlignment(0);
      this.jLabel140.setText("HS 200%");
      this.jLabel141.setFont(new Font("Segoe UI Light", 1, 11));
      this.jLabel141.setForeground(new Color(0, 102, 153));
      this.jLabel141.setHorizontalAlignment(0);
      this.jLabel141.setText("HS 115%");
      this.jLabel142.setFont(new Font("Segoe UI Light", 1, 11));
      this.jLabel142.setForeground(new Color(0, 102, 153));
      this.jLabel142.setHorizontalAlignment(0);
      this.jLabel142.setText("Tot. PP");
      this.tWeekPP.setBorder(BorderFactory.createEtchedBorder());
      this.tWeekPP.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tWeekPP.setHorizontalAlignment(0);
      this.tWeekPP.setFont(new Font("Segoe UI Light", 1, 14));
      this.tWeekPP.addCaretListener(new 195(this));
      this.tWeekPP.addFocusListener(new 196(this));
      this.jLabel143.setFont(new Font("Segoe UI Light", 1, 11));
      this.jLabel143.setForeground(new Color(0, 102, 153));
      this.jLabel143.setHorizontalAlignment(0);
      this.jLabel143.setText("Tot. PE");
      this.tWeekPE.setBorder(BorderFactory.createEtchedBorder());
      this.tWeekPE.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tWeekPE.setHorizontalAlignment(0);
      this.tWeekPE.setFont(new Font("Segoe UI Light", 1, 14));
      this.tWeekPE.addCaretListener(new 197(this));
      this.tWeekPE.addFocusListener(new 198(this));
      this.btnDelHSWeek.setToolTipText("Supprimer");
      this.btnDelHSWeek.setContentAreaFilled(false);
      this.btnDelHSWeek.setCursor(new Cursor(12));
      this.btnDelHSWeek.setOpaque(true);
      this.btnDelHSWeek.addActionListener(new 199(this));
      this.btnSaveHSWeek.setToolTipText("Sauvegarder");
      this.btnSaveHSWeek.setContentAreaFilled(false);
      this.btnSaveHSWeek.setCursor(new Cursor(12));
      this.btnSaveHSWeek.setOpaque(true);
      this.btnSaveHSWeek.addActionListener(new 200(this));
      GroupLayout jPanel26Layout = new GroupLayout(this.jPanel26);
      this.jPanel26.setLayout(jPanel26Layout);
      jPanel26Layout.setHorizontalGroup(jPanel26Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel26Layout.createSequentialGroup().addContainerGap().addGroup(jPanel26Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel116).addComponent(this.jLabel137, -2, 122, -2).addGroup(jPanel26Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.tBeginWeek, Alignment.LEADING, -1, 326, 32767).addComponent(this.tEndWeek, Alignment.LEADING, -1, -1, 32767)).addGroup(jPanel26Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel26Layout.createSequentialGroup().addComponent(this.btnSaveHSWeek, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelHSWeek, -2, 35, -2)).addGroup(jPanel26Layout.createSequentialGroup().addGroup(jPanel26Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel141, Alignment.LEADING, -2, 65, -2).addComponent(this.tWeekOT115, Alignment.LEADING, -2, 65, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel26Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel139, Alignment.LEADING, -2, 65, -2).addComponent(this.tWeekOT140, Alignment.LEADING, -2, 65, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel26Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel138, Alignment.LEADING, -2, 65, -2).addComponent(this.tWeekOT150, Alignment.LEADING, -2, 65, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel26Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel140, Alignment.LEADING, -2, 65, -2).addComponent(this.tWeekOT200, Alignment.LEADING, -2, 65, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel26Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel142, Alignment.LEADING, -2, 65, -2).addComponent(this.tWeekPP, Alignment.LEADING, -2, 65, -2)).addGap(2, 2, 2).addGroup(jPanel26Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel143, Alignment.LEADING, -2, 65, -2).addComponent(this.tWeekPE, Alignment.LEADING, -2, 65, -2))))).addGap(29, 29, 29)));
      jPanel26Layout.setVerticalGroup(jPanel26Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel26Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel116).addGap(0, 0, 0).addComponent(this.tBeginWeek, -2, 32, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel137).addGap(0, 0, 0).addComponent(this.tEndWeek, -2, 32, -2).addGap(18, 18, 18).addGroup(jPanel26Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel26Layout.createSequentialGroup().addComponent(this.jLabel140).addGap(0, 0, 0).addComponent(this.tWeekOT200, -2, 30, -2)).addGroup(jPanel26Layout.createSequentialGroup().addComponent(this.jLabel138).addGap(0, 0, 0).addComponent(this.tWeekOT150, -2, 30, -2)).addGroup(jPanel26Layout.createSequentialGroup().addComponent(this.jLabel139).addGap(0, 0, 0).addComponent(this.tWeekOT140, -2, 30, -2)).addGroup(jPanel26Layout.createSequentialGroup().addComponent(this.jLabel141).addGap(0, 0, 0).addComponent(this.tWeekOT115, -2, 30, -2)).addGroup(jPanel26Layout.createSequentialGroup().addComponent(this.jLabel142).addGap(0, 0, 0).addComponent(this.tWeekPP, -2, 30, -2)).addGroup(jPanel26Layout.createSequentialGroup().addComponent(this.jLabel143).addGap(0, 0, 0).addComponent(this.tWeekPE, -2, 30, -2))).addGap(32, 32, 32).addGroup(jPanel26Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelHSWeek, -2, 35, -2).addComponent(this.btnSaveHSWeek, -2, 35, -2)).addContainerGap()));
      this.panelHS.addTab("Mode hebdomadaire", this.jPanel26);
      this.heuresTable.setFont(new Font("Segoe UI Light", 0, 11));
      this.heuresTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.heuresTable.setSelectionBackground(new Color(0, 102, 153));
      this.heuresTable.setShowGrid(false);
      this.heuresTable.addMouseListener(new 201(this));
      this.jScrollPane14.setViewportView(this.heuresTable);
      this.pnlActivites_Btn1.setBackground(new Color(255, 255, 255));
      this.jLabel126.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel126.setForeground(new Color(0, 102, 153));
      this.jLabel126.setHorizontalAlignment(0);
      this.jLabel126.setText("Tot. PP");
      this.tTotPP.setBorder(BorderFactory.createEtchedBorder());
      this.tTotPP.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotPP.setHorizontalAlignment(0);
      this.tTotPP.setEnabled(false);
      this.tTotPP.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotPP.addCaretListener(new 202(this));
      this.tTotPP.addFocusListener(new 203(this));
      this.jLabel127.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel127.setForeground(new Color(0, 102, 153));
      this.jLabel127.setHorizontalAlignment(0);
      this.jLabel127.setText("Tot. HS");
      this.tTotHS.setBorder(BorderFactory.createEtchedBorder());
      this.tTotHS.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHS.setHorizontalAlignment(0);
      this.tTotHS.setEnabled(false);
      this.tTotHS.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHS.addCaretListener(new 204(this));
      this.tTotHS.addFocusListener(new 205(this));
      this.tTotHS200.setEditable(false);
      this.tTotHS200.setBackground(new Color(255, 0, 102));
      this.tTotHS200.setBorder(BorderFactory.createEtchedBorder());
      this.tTotHS200.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHS200.setHorizontalAlignment(0);
      this.tTotHS200.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHS200.addCaretListener(new 206(this));
      this.tTotHS200.addFocusListener(new 207(this));
      this.jLabel128.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel128.setForeground(new Color(0, 102, 153));
      this.jLabel128.setHorizontalAlignment(0);
      this.jLabel128.setText("HS 200%");
      this.tTotHS150.setEditable(false);
      this.tTotHS150.setBackground(new Color(0, 204, 204));
      this.tTotHS150.setBorder(BorderFactory.createEtchedBorder());
      this.tTotHS150.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHS150.setHorizontalAlignment(0);
      this.tTotHS150.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHS150.addCaretListener(new 208(this));
      this.tTotHS150.addFocusListener(new 209(this));
      this.jLabel129.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel129.setForeground(new Color(0, 102, 153));
      this.jLabel129.setHorizontalAlignment(0);
      this.jLabel129.setText("HS 150%");
      this.tTotHS140.setEditable(false);
      this.tTotHS140.setBackground(new Color(255, 204, 51));
      this.tTotHS140.setBorder(BorderFactory.createEtchedBorder());
      this.tTotHS140.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHS140.setHorizontalAlignment(0);
      this.tTotHS140.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHS140.addCaretListener(new 210(this));
      this.tTotHS140.addFocusListener(new 211(this));
      this.jLabel130.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel130.setForeground(new Color(0, 102, 153));
      this.jLabel130.setHorizontalAlignment(0);
      this.jLabel130.setText("HS 140%");
      this.tTotHS115.setEditable(false);
      this.tTotHS115.setBackground(new Color(102, 204, 0));
      this.tTotHS115.setBorder(BorderFactory.createEtchedBorder());
      this.tTotHS115.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHS115.setHorizontalAlignment(0);
      this.tTotHS115.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHS115.addCaretListener(new 212(this));
      this.tTotHS115.addFocusListener(new 213(this));
      this.jLabel131.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel131.setForeground(new Color(0, 102, 153));
      this.jLabel131.setHorizontalAlignment(0);
      this.jLabel131.setText("HS 115%");
      this.tTotHJ.setBorder(BorderFactory.createEtchedBorder());
      this.tTotHJ.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHJ.setHorizontalAlignment(0);
      this.tTotHJ.setEnabled(false);
      this.tTotHJ.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHJ.addCaretListener(new 214(this));
      this.tTotHJ.addFocusListener(new 215(this));
      this.jLabel132.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel132.setForeground(new Color(0, 102, 153));
      this.jLabel132.setHorizontalAlignment(0);
      this.jLabel132.setText("Tot. HJ");
      this.tTotHN.setBorder(BorderFactory.createEtchedBorder());
      this.tTotHN.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHN.setHorizontalAlignment(0);
      this.tTotHN.setEnabled(false);
      this.tTotHN.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHN.addCaretListener(new 216(this));
      this.tTotHN.addFocusListener(new 217(this));
      this.jLabel133.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel133.setForeground(new Color(0, 102, 153));
      this.jLabel133.setHorizontalAlignment(0);
      this.jLabel133.setText("Tot. HN");
      GroupLayout pnlActivites_Btn1Layout = new GroupLayout(this.pnlActivites_Btn1);
      this.pnlActivites_Btn1.setLayout(pnlActivites_Btn1Layout);
      pnlActivites_Btn1Layout.setHorizontalGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlActivites_Btn1Layout.createSequentialGroup().addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel132, Alignment.LEADING, -2, 65, -2).addComponent(this.tTotHJ, Alignment.LEADING, -2, 65, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel133, Alignment.LEADING, -2, 65, -2).addComponent(this.tTotHN, Alignment.LEADING, -2, 65, -2)).addGap(3, 3, 3).addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel131, Alignment.LEADING, -2, 65, -2).addComponent(this.tTotHS115, Alignment.LEADING, -2, 65, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel130, Alignment.LEADING, -2, 65, -2).addComponent(this.tTotHS140, Alignment.LEADING, -2, 65, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel129, Alignment.LEADING, -2, 65, -2).addComponent(this.tTotHS150, Alignment.LEADING, -2, 65, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel128, Alignment.LEADING, -2, 65, -2).addComponent(this.tTotHS200, Alignment.LEADING, -2, 65, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel127, Alignment.LEADING, -2, 65, -2).addComponent(this.tTotHS, Alignment.LEADING, -2, 65, -2)).addGap(18, 18, 18).addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel126, Alignment.LEADING, -2, 65, -2).addComponent(this.tTotPP, Alignment.LEADING, -2, 65, -2))));
      pnlActivites_Btn1Layout.setVerticalGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivites_Btn1Layout.createSequentialGroup().addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivites_Btn1Layout.createSequentialGroup().addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.TRAILING).addGroup(pnlActivites_Btn1Layout.createSequentialGroup().addComponent(this.jLabel132).addGap(0, 0, 0).addComponent(this.tTotHJ, -2, 30, -2)).addGroup(pnlActivites_Btn1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivites_Btn1Layout.createSequentialGroup().addComponent(this.jLabel126).addGap(0, 0, 0).addComponent(this.tTotPP, -2, 30, -2)).addGroup(pnlActivites_Btn1Layout.createSequentialGroup().addComponent(this.jLabel127).addGap(0, 0, 0).addComponent(this.tTotHS, -2, 30, -2)).addGroup(pnlActivites_Btn1Layout.createSequentialGroup().addComponent(this.jLabel128).addGap(0, 0, 0).addComponent(this.tTotHS200, -2, 30, -2)).addGroup(pnlActivites_Btn1Layout.createSequentialGroup().addComponent(this.jLabel129).addGap(0, 0, 0).addComponent(this.tTotHS150, -2, 30, -2)).addGroup(pnlActivites_Btn1Layout.createSequentialGroup().addComponent(this.jLabel130).addGap(0, 0, 0).addComponent(this.tTotHS140, -2, 30, -2)).addGroup(pnlActivites_Btn1Layout.createSequentialGroup().addComponent(this.jLabel131).addGap(0, 0, 0).addComponent(this.tTotHS115, -2, 30, -2)))).addGap(3, 3, 3)).addGroup(pnlActivites_Btn1Layout.createSequentialGroup().addComponent(this.jLabel133).addGap(0, 0, 0).addComponent(this.tTotHN, -2, 30, -2))).addGap(0, 0, 32767)));
      this.cF50Auto.setBackground(new Color(255, 255, 255));
      this.cF50Auto.setFont(new Font("Segoe UI Light", 1, 12));
      this.cF50Auto.setForeground(new Color(0, 102, 153));
      this.cF50Auto.setText("Feri\u00e9 150% auto");
      this.cF50Auto.addActionListener(new 218(this));
      this.cPPAuto.setBackground(new Color(255, 255, 255));
      this.cPPAuto.setFont(new Font("Segoe UI Light", 1, 12));
      this.cPPAuto.setForeground(new Color(0, 102, 153));
      this.cPPAuto.setText("Prime de panier auto");
      this.cPPAuto.addActionListener(new 219(this));
      this.tModeDecompte.setFont(new Font("Segoe UI Light", 1, 12));
      this.tModeDecompte.setModel(new DefaultComboBoxModel(new String[]{"Quotidien", "Hebdomadaire"}));
      this.tModeDecompte.addActionListener(new 220(this));
      this.jLabel115.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel115.setForeground(new Color(0, 102, 153));
      this.jLabel115.setText("Mode de d\u00e9compte");
      this.jLabel124.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel124.setForeground(new Color(0, 102, 153));
      this.jLabel124.setHorizontalAlignment(0);
      this.jLabel124.setText("Tot. PE");
      this.tTotPE.setBorder(BorderFactory.createEtchedBorder());
      this.tTotPE.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotPE.setHorizontalAlignment(0);
      this.tTotPE.setEnabled(false);
      this.tTotPE.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotPE.addCaretListener(new 221(this));
      this.tTotPE.addFocusListener(new 222(this));
      this.btnValoriserHS.setFont(new Font("SansSerif", 1, 13));
      this.btnValoriserHS.setForeground(new Color(0, 102, 153));
      this.btnValoriserHS.setText("Valoriser HS");
      this.btnValoriserHS.setToolTipText("Valoriser les HS en montants");
      this.btnValoriserHS.setContentAreaFilled(false);
      this.btnValoriserHS.setCursor(new Cursor(12));
      this.btnValoriserHS.setOpaque(true);
      this.btnValoriserHS.addActionListener(new 223(this));
      GroupLayout pointageHSPanelLayout = new GroupLayout(this.pointageHSPanel);
      this.pointageHSPanel.setLayout(pointageHSPanelLayout);
      pointageHSPanelLayout.setHorizontalGroup(pointageHSPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(pointageHSPanelLayout.createSequentialGroup().addContainerGap().addGroup(pointageHSPanelLayout.createParallelGroup(Alignment.LEADING, false).addGroup(pointageHSPanelLayout.createSequentialGroup().addGroup(pointageHSPanelLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel115, Alignment.LEADING, -2, 139, -2).addComponent(this.tModeDecompte, Alignment.LEADING, -2, 139, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pointageHSPanelLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.cPPAuto).addComponent(this.cF50Auto, -2, 152, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnValoriserHS, -1, -1, 32767)).addComponent(this.panelHS, -2, 487, -2)).addGap(5, 5, 5).addGroup(pointageHSPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(pointageHSPanelLayout.createSequentialGroup().addComponent(this.pnlActivites_Btn1, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(pointageHSPanelLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel124, Alignment.LEADING, -2, 65, -2).addComponent(this.tTotPE, Alignment.LEADING, -2, 65, -2))).addComponent(this.jScrollPane14, -2, 716, -2)).addGap(156, 156, 156)));
      pointageHSPanelLayout.setVerticalGroup(pointageHSPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(pointageHSPanelLayout.createSequentialGroup().addContainerGap().addGroup(pointageHSPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(pointageHSPanelLayout.createSequentialGroup().addGroup(pointageHSPanelLayout.createParallelGroup(Alignment.LEADING, false).addGroup(pointageHSPanelLayout.createSequentialGroup().addComponent(this.cPPAuto).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.cF50Auto)).addGroup(pointageHSPanelLayout.createSequentialGroup().addComponent(this.jLabel115).addGap(0, 0, 0).addComponent(this.tModeDecompte, -2, 30, -2)).addComponent(this.btnValoriserHS, -1, -1, 32767)).addGap(5, 5, 5).addComponent(this.panelHS)).addGroup(pointageHSPanelLayout.createSequentialGroup().addComponent(this.jScrollPane14, -2, 406, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(pointageHSPanelLayout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlActivites_Btn1, -1, -1, 32767).addGroup(pointageHSPanelLayout.createSequentialGroup().addComponent(this.jLabel124).addGap(0, 0, 0).addComponent(this.tTotPE, -2, 30, -2).addGap(0, 0, 32767))))).addContainerGap()));
      this.pPaie_TabbedPane.addTab("Pointage HS", this.pointageHSPanel);
      GroupLayout pPaieLayout = new GroupLayout(this.pPaie);
      this.pPaie.setLayout(pPaieLayout);
      pPaieLayout.setHorizontalGroup(pPaieLayout.createParallelGroup(Alignment.LEADING).addGroup(pPaieLayout.createSequentialGroup().addComponent(this.pPaie_TabbedPane, -2, 1320, 32767).addContainerGap()));
      pPaieLayout.setVerticalGroup(pPaieLayout.createParallelGroup(Alignment.LEADING).addGroup(pPaieLayout.createSequentialGroup().addContainerGap().addComponent(this.pPaie_TabbedPane)));
      this.detailPanel.addTab("Paie", this.pPaie);
      this.pConges.setBackground(new Color(255, 255, 255));
      this.pConges.setFont(new Font("Segoe UI Light", 0, 12));
      this.jTabbedPane2.setForeground(new Color(0, 102, 153));
      this.jTabbedPane2.setFont(new Font("Segoe UI Light", 1, 12));
      this.jPanel19.setBackground(new Color(255, 255, 255));
      this.tDateDepart.setBorder(BorderFactory.createEtchedBorder());
      this.tDateDepart.setDateFormatString("dd/MM/yyyy");
      this.tDateDepart.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel95.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel95.setForeground(new Color(0, 102, 153));
      this.jLabel95.setText("D\u00e9part");
      this.tDateReprise.setBorder(BorderFactory.createEtchedBorder());
      this.tDateReprise.setDateFormatString("dd/MM/yyyy");
      this.tDateReprise.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel96.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel96.setForeground(new Color(0, 102, 153));
      this.jLabel96.setText("Reprise pr\u00e9vue");
      this.tRepriseEff.setBorder(BorderFactory.createEtchedBorder());
      this.tRepriseEff.setDateFormatString("dd/MM/yyyy");
      this.tRepriseEff.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel97.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel97.setForeground(new Color(0, 102, 153));
      this.jLabel97.setText("Reprise effective");
      this.cDroitsCongesAuto.setBackground(new Color(255, 255, 255));
      this.cDroitsCongesAuto.setFont(new Font("Segoe UI Light", 1, 12));
      this.cDroitsCongesAuto.setForeground(new Color(0, 102, 153));
      this.cDroitsCongesAuto.setText("Droits de cong\u00e9s auto.");
      this.cDroitsCongesAuto.addActionListener(new 224(this));
      this.jLabel98.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel98.setForeground(new Color(0, 102, 153));
      this.jLabel98.setText("Note");
      this.tNoteConges.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNoteConges.setBorder(BorderFactory.createEtchedBorder());
      this.tNoteConges.addKeyListener(new 225(this));
      this.btnNewConges.setToolTipText("Nouveau");
      this.btnNewConges.setContentAreaFilled(false);
      this.btnNewConges.setCursor(new Cursor(12));
      this.btnNewConges.setOpaque(true);
      this.btnNewConges.addActionListener(new 226(this));
      this.btnSaveConges.setToolTipText("Sauvegarder");
      this.btnSaveConges.setContentAreaFilled(false);
      this.btnSaveConges.setCursor(new Cursor(12));
      this.btnSaveConges.setOpaque(true);
      this.btnSaveConges.addActionListener(new 227(this));
      this.btnDelConges.setToolTipText("Supprimer");
      this.btnDelConges.setContentAreaFilled(false);
      this.btnDelConges.setCursor(new Cursor(12));
      this.btnDelConges.setOpaque(true);
      this.btnDelConges.addActionListener(new 228(this));
      GroupLayout jPanel19Layout = new GroupLayout(this.jPanel19);
      this.jPanel19.setLayout(jPanel19Layout);
      jPanel19Layout.setHorizontalGroup(jPanel19Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel19Layout.createSequentialGroup().addContainerGap().addGroup(jPanel19Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel19Layout.createSequentialGroup().addGap(0, 0, 32767).addGroup(jPanel19Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel95, -2, 121, -2).addComponent(this.tDateDepart, -2, 121, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel19Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel96, -2, 121, -2).addComponent(this.tDateReprise, -2, 121, -2)).addGap(18, 18, 18).addGroup(jPanel19Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel97, -2, 121, -2).addComponent(this.tRepriseEff, -2, 121, -2)).addGap(38, 38, 38)).addGroup(jPanel19Layout.createSequentialGroup().addGroup(jPanel19Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel19Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel19Layout.createSequentialGroup().addComponent(this.btnNewConges, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSaveConges, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelConges, -2, 35, -2)).addComponent(this.jLabel98, -2, 300, -2)).addComponent(this.tNoteConges, -2, 396, -2).addComponent(this.cDroitsCongesAuto, -2, 183, -2)).addContainerGap(-1, 32767)))));
      jPanel19Layout.setVerticalGroup(jPanel19Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel19Layout.createSequentialGroup().addContainerGap().addGroup(jPanel19Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel19Layout.createSequentialGroup().addComponent(this.jLabel97).addGap(0, 0, 0).addComponent(this.tRepriseEff, -2, 32, -2)).addGroup(jPanel19Layout.createSequentialGroup().addComponent(this.jLabel96).addGap(0, 0, 0).addComponent(this.tDateReprise, -2, 32, -2)).addGroup(jPanel19Layout.createSequentialGroup().addComponent(this.jLabel95).addGap(0, 0, 0).addComponent(this.tDateDepart, -2, 32, -2))).addGap(34, 34, 34).addComponent(this.cDroitsCongesAuto, -2, 30, -2).addGap(18, 18, 18).addComponent(this.jLabel98).addGap(0, 0, 0).addComponent(this.tNoteConges, -2, 30, -2).addGap(62, 62, 62).addGroup(jPanel19Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelConges, -2, 35, -2).addComponent(this.btnSaveConges, -2, 35, -2).addComponent(this.btnNewConges, -2, 35, -2)).addContainerGap()));
      this.jTabbedPane2.addTab("D\u00e9tail", this.jPanel19);
      this.jPanel20.setBackground(new Color(255, 255, 255));
      this.jLabel90.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel90.setForeground(new Color(0, 102, 153));
      this.jLabel90.setText("Dernier d\u00e9part");
      this.jLabel91.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel91.setForeground(new Color(0, 102, 153));
      this.jLabel91.setText("Cumul BI depuis le der. d\u00e9part");
      this.tCumulBrutImposable.setBorder(BorderFactory.createEtchedBorder());
      this.tCumulBrutImposable.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tCumulBrutImposable.setHorizontalAlignment(4);
      this.tCumulBrutImposable.setEnabled(false);
      this.tCumulBrutImposable.setFont(new Font("Segoe UI Light", 1, 12));
      this.tCumulBrutImposable.addCaretListener(new 229(this));
      this.tCumulBrutImposable.addFocusListener(new 230(this));
      this.jLabel92.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel92.setForeground(new Color(0, 102, 153));
      this.jLabel92.setText("Cumul BNI depuis le der. d\u00e9part");
      this.tCumulBrutNonImposable.setBorder(BorderFactory.createEtchedBorder());
      this.tCumulBrutNonImposable.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tCumulBrutNonImposable.setHorizontalAlignment(4);
      this.tCumulBrutNonImposable.setEnabled(false);
      this.tCumulBrutNonImposable.setFont(new Font("Segoe UI Light", 1, 12));
      this.tCumulBrutNonImposable.addCaretListener(new 231(this));
      this.tCumulBrutNonImposable.addFocusListener(new 232(this));
      this.jLabel93.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel93.setForeground(new Color(0, 102, 153));
      this.jLabel93.setText("Cumul 12 Der. Mois");
      this.tCumul12DM.setBorder(BorderFactory.createEtchedBorder());
      this.tCumul12DM.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tCumul12DM.setHorizontalAlignment(4);
      this.tCumul12DM.setEnabled(false);
      this.tCumul12DM.setFont(new Font("Segoe UI Light", 1, 12));
      this.tCumul12DM.addCaretListener(new 233(this));
      this.tCumul12DM.addFocusListener(new 234(this));
      this.jLabel94.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel94.setForeground(new Color(0, 102, 153));
      this.jLabel94.setText("Cumul Retenues depuis le der. d\u00e9part");
      this.tCumulRet.setBorder(BorderFactory.createEtchedBorder());
      this.tCumulRet.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tCumulRet.setHorizontalAlignment(4);
      this.tCumulRet.setEnabled(false);
      this.tCumulRet.setFont(new Font("Segoe UI Light", 1, 12));
      this.tCumulRet.addCaretListener(new 235(this));
      this.tCumulRet.addFocusListener(new 236(this));
      this.btnExcelCumul_Conges.setToolTipText("D\u00e9tail du cumul");
      this.btnExcelCumul_Conges.setContentAreaFilled(false);
      this.btnExcelCumul_Conges.setCursor(new Cursor(12));
      this.btnExcelCumul_Conges.setOpaque(true);
      this.btnExcelCumul_Conges.addActionListener(new 237(this));
      this.tDernierDepart.setBorder(BorderFactory.createEtchedBorder());
      this.tDernierDepart.setDateFormatString("dd/MM/yyyy");
      this.tDernierDepart.setEnabled(false);
      this.tDernierDepart.setFont(new Font("Segoe UI Light", 1, 12));
      GroupLayout jPanel20Layout = new GroupLayout(this.jPanel20);
      this.jPanel20.setLayout(jPanel20Layout);
      jPanel20Layout.setHorizontalGroup(jPanel20Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel20Layout.createSequentialGroup().addContainerGap().addGroup(jPanel20Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel20Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel90, -2, 150, -2).addComponent(this.tCumulBrutImposable, -1, 240, 32767).addComponent(this.tCumulBrutNonImposable, -1, 240, 32767).addComponent(this.jLabel93, -2, 150, -2).addComponent(this.tCumul12DM, -1, 240, 32767).addComponent(this.tCumulRet, -1, 240, 32767).addComponent(this.jLabel91).addComponent(this.jLabel92).addComponent(this.jLabel94, -2, 229, -2).addComponent(this.tDernierDepart, -1, -1, 32767)).addComponent(this.btnExcelCumul_Conges, -2, 35, -2)).addGap(117, 117, 117)));
      jPanel20Layout.setVerticalGroup(jPanel20Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel20Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel90).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tDernierDepart, -2, 32, -2).addGap(16, 16, 16).addComponent(this.jLabel91).addGap(0, 0, 0).addComponent(this.tCumulBrutImposable, -2, 30, -2).addGap(21, 21, 21).addComponent(this.jLabel92).addGap(0, 0, 0).addComponent(this.tCumulBrutNonImposable, -2, 30, -2).addGap(21, 21, 21).addComponent(this.jLabel94).addGap(0, 0, 0).addComponent(this.tCumulRet, -2, 30, -2).addGap(41, 41, 41).addComponent(this.jLabel93).addGap(0, 0, 0).addComponent(this.tCumul12DM, -2, 30, -2).addGap(18, 18, 18).addComponent(this.btnExcelCumul_Conges, -2, 35, -2).addContainerGap()));
      this.jTabbedPane2.addTab("Situation des cumuls", this.jPanel20);
      this.congesTable.setFont(new Font("Segoe UI Light", 0, 11));
      this.congesTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.congesTable.setSelectionBackground(new Color(0, 102, 153));
      this.congesTable.setShowGrid(false);
      this.congesTable.addMouseListener(new 238(this));
      this.jScrollPane11.setViewportView(this.congesTable);
      GroupLayout pCongesLayout = new GroupLayout(this.pConges);
      this.pConges.setLayout(pCongesLayout);
      pCongesLayout.setHorizontalGroup(pCongesLayout.createParallelGroup(Alignment.LEADING).addGroup(pCongesLayout.createSequentialGroup().addContainerGap().addComponent(this.jTabbedPane2, -2, 437, -2).addGap(2, 2, 2).addComponent(this.jScrollPane11, -2, 841, -2).addGap(21, 21, 21)));
      pCongesLayout.setVerticalGroup(pCongesLayout.createParallelGroup(Alignment.LEADING).addGroup(pCongesLayout.createSequentialGroup().addContainerGap().addGroup(pCongesLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jTabbedPane2).addComponent(this.jScrollPane11, -2, 498, -2)).addContainerGap()));
      this.detailPanel.addTab("Cong\u00e9s", this.pConges);
      this.pEngagement.setBackground(new Color(255, 255, 255));
      this.pEngagement.setFont(new Font("Segoe UI Light", 0, 12));
      this.jTabbedPane3.setForeground(new Color(0, 102, 153));
      this.jTabbedPane3.setFont(new Font("Segoe UI Light", 1, 12));
      this.jPanel21.setBackground(new Color(255, 255, 255));
      this.tDateAccord.setBorder(BorderFactory.createEtchedBorder());
      this.tDateAccord.setDateFormatString("dd/MM/yy");
      this.tDateAccord.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel99.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel99.setForeground(new Color(0, 102, 153));
      this.jLabel99.setText("Date d'accord");
      this.cActiveRetAE.setBackground(new Color(255, 255, 255));
      this.cActiveRetAE.setFont(new Font("Segoe UI Light", 1, 12));
      this.cActiveRetAE.setForeground(new Color(0, 102, 153));
      this.cActiveRetAE.setText("Actif");
      this.cActiveRetAE.addActionListener(new 239(this));
      this.jLabel102.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel102.setForeground(new Color(0, 102, 153));
      this.jLabel102.setText("Note");
      this.tNoteRetAE.setFont(new Font("Segoe UI Light", 0, 12));
      this.tNoteRetAE.setBorder((Border)null);
      this.tNoteRetAE.addKeyListener(new 240(this));
      this.btnNewEng.setToolTipText("Nouveau");
      this.btnNewEng.setContentAreaFilled(false);
      this.btnNewEng.setCursor(new Cursor(12));
      this.btnNewEng.setOpaque(true);
      this.btnNewEng.addActionListener(new 241(this));
      this.btnSaveEng.setToolTipText("Sauvegarder");
      this.btnSaveEng.setContentAreaFilled(false);
      this.btnSaveEng.setCursor(new Cursor(12));
      this.btnSaveEng.setOpaque(true);
      this.btnSaveEng.addActionListener(new 242(this));
      this.btnDelEng.setToolTipText("Supprimer");
      this.btnDelEng.setContentAreaFilled(false);
      this.btnDelEng.setCursor(new Cursor(12));
      this.btnDelEng.setOpaque(true);
      this.btnDelEng.addActionListener(new 243(this));
      this.jLabel100.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel100.setForeground(new Color(0, 102, 153));
      this.jLabel100.setText("Retenue");
      this.tRubriqueRetAE.setFont(new Font("Segoe UI Light", 0, 12));
      this.tRubriqueRetAE.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tRubriqueRetAE.addFocusListener(new 244(this));
      this.tRubriqueRetAE.addActionListener(new 245(this));
      this.jLabel101.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel101.setForeground(new Color(0, 102, 153));
      this.jLabel101.setText("Capital");
      this.tCapital.setBorder(BorderFactory.createEtchedBorder());
      this.tCapital.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tCapital.setFont(new Font("Segoe UI Light", 1, 12));
      this.tCapital.addCaretListener(new 246(this));
      this.tCapital.addFocusListener(new 247(this));
      this.jLabel108.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel108.setForeground(new Color(0, 102, 153));
      this.jLabel108.setText("Ech\u00e9ance");
      this.tEcheance.setBorder(BorderFactory.createEtchedBorder());
      this.tEcheance.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tEcheance.setFont(new Font("Segoe UI Light", 1, 12));
      this.tEcheance.addCaretListener(new 248(this));
      this.tEcheance.addFocusListener(new 249(this));
      this.jLabel109.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel109.setForeground(new Color(0, 102, 153));
      this.jLabel109.setText("Encours");
      this.lbEncours.setBorder(BorderFactory.createEtchedBorder());
      this.lbEncours.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.lbEncours.setHorizontalAlignment(4);
      this.lbEncours.setEnabled(false);
      this.lbEncours.setFont(new Font("Segoe UI Light", 1, 12));
      this.lbEncours.addCaretListener(new 250(this));
      this.lbEncours.addFocusListener(new 251(this));
      this.jLabel110.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel110.setForeground(new Color(0, 102, 153));
      this.jLabel110.setText("P\u00e9riode initiale");
      this.lbPeriodeInit.setBorder(BorderFactory.createEtchedBorder());
      this.lbPeriodeInit.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.lbPeriodeInit.setHorizontalAlignment(0);
      this.lbPeriodeInit.setEnabled(false);
      this.lbPeriodeInit.setFont(new Font("Segoe UI Light", 1, 12));
      this.lbPeriodeInit.addCaretListener(new 252(this));
      this.lbPeriodeInit.addFocusListener(new 253(this));
      this.jLabel111.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel111.setForeground(new Color(0, 102, 153));
      this.jLabel111.setText("Ech. Cour. SN");
      this.lbECCNG.setBorder(BorderFactory.createEtchedBorder());
      this.lbECCNG.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.lbECCNG.setHorizontalAlignment(4);
      this.lbECCNG.setEnabled(false);
      this.lbECCNG.setFont(new Font("Segoe UI Light", 1, 12));
      this.lbECCNG.addCaretListener(new 254(this));
      this.lbECCNG.addFocusListener(new 255(this));
      this.jLabel112.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel112.setForeground(new Color(0, 102, 153));
      this.jLabel112.setText("Ech. Cour. CNG.");
      this.tTauxAnc4.setBorder(BorderFactory.createEtchedBorder());
      this.tTauxAnc4.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tTauxAnc4.setHorizontalAlignment(4);
      this.tTauxAnc4.setEnabled(false);
      this.tTauxAnc4.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTauxAnc4.addCaretListener(new 256(this));
      this.tTauxAnc4.addFocusListener(new 257(this));
      this.btnAppEng.setText("Appliquer");
      this.btnAppEng.setToolTipText("Appliquer la retenue sur la paie courante");
      this.btnAppEng.setContentAreaFilled(false);
      this.btnAppEng.setCursor(new Cursor(12));
      this.btnAppEng.setOpaque(true);
      this.btnAppEng.addActionListener(new 258(this));
      GroupLayout jPanel21Layout = new GroupLayout(this.jPanel21);
      this.jPanel21.setLayout(jPanel21Layout);
      jPanel21Layout.setHorizontalGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel21Layout.createSequentialGroup().addContainerGap().addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel21Layout.createSequentialGroup().addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tNoteRetAE, -2, 320, -2).addGroup(jPanel21Layout.createSequentialGroup().addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel99, -2, 100, -2).addComponent(this.tDateAccord, -2, 161, -2)).addGap(18, 18, 18).addComponent(this.cActiveRetAE)).addGroup(jPanel21Layout.createSequentialGroup().addGroup(jPanel21Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel101, Alignment.LEADING, -2, 120, -2).addComponent(this.tCapital, Alignment.LEADING, -2, 120, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel108, -2, 80, -2).addComponent(this.tEcheance, -2, 106, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel109, -2, 100, -2).addComponent(this.lbEncours, -2, 131, -2))).addComponent(this.jLabel102, -2, 320, -2)).addGap(32, 32, 32)).addGroup(jPanel21Layout.createSequentialGroup().addGroup(jPanel21Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel110, Alignment.LEADING, -2, 100, -2).addComponent(this.lbPeriodeInit, Alignment.LEADING, -2, 100, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel111, -2, 100, -2).addComponent(this.lbECCNG, -1, 106, 32767)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel112, -2, 100, -2).addGroup(jPanel21Layout.createSequentialGroup().addComponent(this.tTauxAnc4).addGap(67, 67, 67)))).addGroup(jPanel21Layout.createSequentialGroup().addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel100, -2, 324, -2).addComponent(this.tRubriqueRetAE, -2, 369, -2)).addGap(0, 0, 32767)).addGroup(Alignment.TRAILING, jPanel21Layout.createSequentialGroup().addGap(0, 0, 32767).addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel21Layout.createSequentialGroup().addComponent(this.btnAppEng, -2, 150, -2).addGap(251, 251, 251)).addGroup(Alignment.TRAILING, jPanel21Layout.createSequentialGroup().addComponent(this.btnNewEng, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSaveEng, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelEng, -2, 35, -2).addGap(67, 67, 67)))))));
      jPanel21Layout.setVerticalGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel21Layout.createSequentialGroup().addContainerGap().addGroup(jPanel21Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel21Layout.createSequentialGroup().addComponent(this.jLabel99).addGap(0, 0, 0).addComponent(this.tDateAccord, -2, 32, -2)).addComponent(this.cActiveRetAE, -2, 46, -2)).addGap(20, 20, 20).addComponent(this.jLabel100).addGap(0, 0, 0).addComponent(this.tRubriqueRetAE, -2, 30, -2).addGap(20, 20, 20).addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel21Layout.createSequentialGroup().addComponent(this.jLabel101).addGap(0, 0, 0).addComponent(this.tCapital, -2, 30, -2)).addGroup(Alignment.TRAILING, jPanel21Layout.createSequentialGroup().addComponent(this.jLabel108).addGap(0, 0, 0).addComponent(this.tEcheance, -2, 30, -2))).addGroup(jPanel21Layout.createSequentialGroup().addComponent(this.jLabel109).addGap(0, 0, 0).addComponent(this.lbEncours, -2, 30, -2))).addGap(20, 20, 20).addComponent(this.jLabel102).addGap(0, 0, 0).addComponent(this.tNoteRetAE, -2, 30, -2).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel21Layout.createSequentialGroup().addComponent(this.jLabel110).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.lbPeriodeInit, -2, 28, -2)).addGroup(jPanel21Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel21Layout.createSequentialGroup().addComponent(this.jLabel112).addGap(32, 32, 32)).addGroup(jPanel21Layout.createSequentialGroup().addComponent(this.jLabel111).addGap(0, 0, 0).addGroup(jPanel21Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.lbECCNG, -2, 28, -2).addComponent(this.tTauxAnc4, -2, 30, -2))))).addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel21Layout.createSequentialGroup().addGap(20, 20, 20).addComponent(this.btnAppEng, -2, 35, -2)).addGroup(jPanel21Layout.createSequentialGroup().addGap(18, 18, 18).addGroup(jPanel21Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelEng, -2, 35, -2).addComponent(this.btnSaveEng, -2, 35, -2).addComponent(this.btnNewEng, -2, 35, -2)))).addContainerGap()));
      this.jTabbedPane3.addTab("D\u00e9tail", this.jPanel21);
      this.jPanel22.setBackground(new Color(255, 255, 255));
      this.historiqueRetAETable.setFont(new Font("Segoe UI Light", 0, 11));
      this.historiqueRetAETable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.historiqueRetAETable.setSelectionBackground(new Color(0, 102, 153));
      this.historiqueRetAETable.setShowGrid(false);
      this.historiqueRetAETable.addMouseListener(new 259(this));
      this.jScrollPane13.setViewportView(this.historiqueRetAETable);
      this.tTotReg.setBorder((Border)null);
      this.tTotReg.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tTotReg.setHorizontalAlignment(4);
      this.tTotReg.setEnabled(false);
      this.tTotReg.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotReg.addCaretListener(new 260(this));
      this.tTotReg.addFocusListener(new 261(this));
      GroupLayout jPanel22Layout = new GroupLayout(this.jPanel22);
      this.jPanel22.setLayout(jPanel22Layout);
      jPanel22Layout.setHorizontalGroup(jPanel22Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel22Layout.createSequentialGroup().addGroup(jPanel22Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.tTotReg, -2, 216, -2).addComponent(this.jScrollPane13, -2, 379, -2)).addGap(28, 28, 28)));
      jPanel22Layout.setVerticalGroup(jPanel22Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel22Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane13, -2, 386, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.tTotReg, -2, 30, -2).addGap(16, 16, 16)));
      this.jTabbedPane3.addTab("Historique de r\u00e8glements", this.jPanel22);
      this.retAETable.setFont(new Font("Segoe UI Light", 0, 11));
      this.retAETable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.retAETable.setSelectionBackground(new Color(0, 102, 153));
      this.retAETable.setShowGrid(false);
      this.retAETable.addMouseListener(new 262(this));
      this.jScrollPane12.setViewportView(this.retAETable);
      GroupLayout pEngagementLayout = new GroupLayout(this.pEngagement);
      this.pEngagement.setLayout(pEngagementLayout);
      pEngagementLayout.setHorizontalGroup(pEngagementLayout.createParallelGroup(Alignment.LEADING).addGroup(pEngagementLayout.createSequentialGroup().addContainerGap().addComponent(this.jTabbedPane3, -2, 407, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jScrollPane12, -2, 856, -2).addGap(52, 52, 52)));
      pEngagementLayout.setVerticalGroup(pEngagementLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pEngagementLayout.createSequentialGroup().addContainerGap(-1, 32767).addGroup(pEngagementLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jTabbedPane3, -2, 475, -2).addComponent(this.jScrollPane12)).addGap(29, 29, 29)));
      this.detailPanel.addTab("Engagements", this.pEngagement);
      this.salarieLabel.setBackground(new Color(255, 255, 255));
      this.salarieLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.salarieLabel.setText(".");
      this.salarieLabel.setOpaque(true);
      this.tPeriode.setFont(new Font("Segoe UI Light", 1, 12));
      this.tPeriode.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriode.addActionListener(new 263(this));
      this.btnRefreshItem.setToolTipText("Actualiser");
      this.btnRefreshItem.setContentAreaFilled(false);
      this.btnRefreshItem.setCursor(new Cursor(12));
      this.btnRefreshItem.setOpaque(true);
      this.btnRefreshItem.addActionListener(new 264(this));
      this.btnSave.setToolTipText("Sauvegarder");
      this.btnSave.setContentAreaFilled(false);
      this.btnSave.setCursor(new Cursor(12));
      this.btnSave.setOpaque(true);
      this.btnSave.addActionListener(new 265(this));
      this.btnNew.setToolTipText("Nouveau salari\u00e9");
      this.btnNew.setContentAreaFilled(false);
      this.btnNew.setCursor(new Cursor(12));
      this.btnNew.setOpaque(true);
      this.btnNew.addActionListener(new 266(this));
      this.btnDelete.setToolTipText("Supprimer");
      this.btnDelete.setContentAreaFilled(false);
      this.btnDelete.setCursor(new Cursor(12));
      this.btnDelete.setOpaque(true);
      this.btnDelete.addActionListener(new 267(this));
      GroupLayout pDetailLayout = new GroupLayout(this.pDetail);
      this.pDetail.setLayout(pDetailLayout);
      pDetailLayout.setHorizontalGroup(pDetailLayout.createParallelGroup(Alignment.LEADING).addGroup(pDetailLayout.createSequentialGroup().addContainerGap().addGroup(pDetailLayout.createParallelGroup(Alignment.LEADING).addComponent(this.detailPanel, -2, 1309, -2).addGroup(pDetailLayout.createSequentialGroup().addComponent(this.salarieLabel, -2, 529, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnRefreshItem, -2, 40, -2).addGap(44, 44, 44).addComponent(this.tPeriode, -2, 220, -2).addGap(270, 270, 270).addComponent(this.btnNew, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete, -2, 35, -2))).addGap(5, 5, 5)));
      pDetailLayout.setVerticalGroup(pDetailLayout.createParallelGroup(Alignment.LEADING).addGroup(pDetailLayout.createSequentialGroup().addContainerGap().addGroup(pDetailLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tPeriode, -2, 35, -2).addComponent(this.btnDelete, -2, 35, -2).addComponent(this.btnSave, -2, 35, -2).addComponent(this.btnNew, -2, 35, -2).addGroup(pDetailLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.salarieLabel, Alignment.LEADING, -1, -1, 32767).addComponent(this.btnRefreshItem, Alignment.LEADING, -1, 34, 32767))).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.detailPanel, -2, -1, -2).addContainerGap(-1, 32767)));
      this.jTabbedPane1.addTab("D\u00e9tail", this.pDetail);
      this.pnlBody.add(this.jTabbedPane1, new AbsoluteConstraints(6, 20, -1, 635));
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jPanel1, -1, -1, 32767).addGroup(layout.createSequentialGroup().addComponent(this.pnlBody, -2, -1, -2).addGap(0, 0, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -2, -1, -2).addGap(2, 2, 2)));
      this.setSize(new Dimension(1351, 716));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void listTableMouseClicked(MouseEvent var1) {
      int selectedID = ((Number)this.listTable.getValueAt(this.listTable.getSelectedRow(), 2)).intValue();
      this.clearFields();
      this.findByID(Integer.valueOf(selectedID), false);
   }

   private void searchFieldKeyPressed(KeyEvent var1) {
   }

   private void lbSearchMouseClicked(MouseEvent var1) {
   }

   private void btnDeleteActionPerformed(ActionEvent var1) {
      if (this.selectedOne != null) {
         int rep0 = JOptionPane.showConfirmDialog(this, " ATTETION! Le suppression du salari\u00e9 surpprime tout son historique! ", " ATTENTION! ", 2);
         if (rep0 == 0) {
            int rep = JOptionPane.showConfirmDialog(this, " ATTETION! Confirmez vous la suppression ?", " Demande de confirmation ", 0);
            if (rep == 0) {
               long id = (long)this.selectedOne.getId();
               Thread t = new 268(this, id);
               t.start();
            }
         }
      } else {
         this.menu.viewMessage(this.msgLabel, "Aucun element selection\u00e9", true);
      }

   }

   public void setSalaryPhoto(Employe var1, String var2) {
      byte[] imageData = new byte[0];
      if (photoPath != null) {
         File file = new File(photoPath);
         imageData = new byte[(int)file.length()];

         try {
            if (file.exists()) {
               FileInputStream fileInputStream = new FileInputStream(file);
               fileInputStream.read(imageData);
               fileInputStream.close();
               emp.setPhoto(imageData);
            }
         } catch (Exception e) {
            this.menu.viewMessage(this.msgLabel, e.getMessage(), true);
         }
      }

   }

   private void btnSaveActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      String msg = this.validateData_Salary();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.msgLabel, msg, true);
      } else {
         Thread t = new 269(this);
         t.start();
      }

   }

   private void btnNewActionPerformed(ActionEvent var1) {
      this.clearFields();
   }

   private void searchFieldCaretUpdate(CaretEvent var1) {
      Thread t = new 270(this);
      t.start();
   }

   private void btnRefreshActionPerformed(ActionEvent var1) {
      Thread t = new 271(this);
      t.start();
   }

   private void cPersonnelActifActionPerformed(ActionEvent var1) {
      Thread t = new 272(this);
      t.start();
   }

   private void tPeriodeActionPerformed(ActionEvent var1) {
      this.progressBar.setIndeterminate(true);
      this.selectedPeriode = (Date)this.tPeriode.getSelectedItem();
      this.afficherRubriquePaie();
      this.afficherHeures();
      this.progressBar.setIndeterminate(false);
   }

   private void cDetacheCNAMActionPerformed(ActionEvent var1) {
   }

   private void tNoCNAMKeyPressed(KeyEvent var1) {
   }

   private void tTauxRembCNAMCaretUpdate(CaretEvent var1) {
   }

   private void tTauxRembCNAMFocusLost(FocusEvent var1) {
   }

   private void cDetacheCNSSActionPerformed(ActionEvent var1) {
   }

   private void tNoCNSSKeyPressed(KeyEvent var1) {
   }

   private void tTauxRembCNSSCaretUpdate(CaretEvent var1) {
   }

   private void tTauxRembCNSSFocusLost(FocusEvent var1) {
   }

   private void tStrictureOrigineKeyPressed(KeyEvent var1) {
   }

   private void cExonoreITSActionPerformed(ActionEvent var1) {
   }

   private void tauxRembITStranche3CaretUpdate(CaretEvent var1) {
   }

   private void tauxRembITStranche3FocusLost(FocusEvent var1) {
   }

   private void tauxRembITStranche2CaretUpdate(CaretEvent var1) {
   }

   private void tauxRembITStranche2FocusLost(FocusEvent var1) {
   }

   private void tauxRembITStranche1CaretUpdate(CaretEvent var1) {
   }

   private void tauxRembITStranche1FocusLost(FocusEvent var1) {
   }

   private void congesTableMouseClicked(MouseEvent var1) {
      if (this.congesTable.getRowCount() > 0) {
         this.selectedConges = (Conges)this.congesTable.getValueAt(this.congesTable.getSelectedRow(), 5);
         this.tDateDepart.setDate(this.selectedConges.getDepart());
         this.tDateReprise.setDate(this.selectedConges.getReprise());
         this.tRepriseEff.setDate(this.selectedConges.getRepriseeff());
         this.tNoteConges.setText(this.selectedConges.getNote());
         this.btnSaveConges.setEnabled(this.selectedConges.getPeriode().equals(this.menu.paramsGen.getPeriodeCourante()));
         this.btnDelConges.setEnabled(this.selectedConges.getPeriode().equals(this.menu.paramsGen.getPeriodeCourante()));
      }

   }

   private void tCumulBrutImposableCaretUpdate(CaretEvent var1) {
   }

   private void tCumulBrutImposableFocusLost(FocusEvent var1) {
   }

   private void tCumulBrutNonImposableCaretUpdate(CaretEvent var1) {
   }

   private void tCumulBrutNonImposableFocusLost(FocusEvent var1) {
   }

   private void tCumul12DMCaretUpdate(CaretEvent var1) {
   }

   private void tCumul12DMFocusLost(FocusEvent var1) {
   }

   private void tCumulRetCaretUpdate(CaretEvent var1) {
   }

   private void tCumulRetFocusLost(FocusEvent var1) {
   }

   private void cDroitsCongesAutoActionPerformed(ActionEvent var1) {
   }

   private void tNoteCongesKeyPressed(KeyEvent var1) {
   }

   private void btnNewCongesActionPerformed(ActionEvent var1) {
      this.selectedConges = null;
      this.tDateDepart.setDate(new Date());
      this.tDateReprise.setDate(new Date());
      this.tDateReprise.setDate(new Date());
      this.tNoteConges.setText("");
      this.btnDelConges.setEnabled(false);
      this.btnSaveConges.setEnabled(true);
   }

   private void btnSaveCongesActionPerformed(ActionEvent var1) {
      Thread t = new 273(this);
      t.start();
   }

   private void btnDelCongesActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 274(this);
         t.start();
      }

   }

   private void cActiveRetAEActionPerformed(ActionEvent var1) {
   }

   private void tNoteRetAEKeyPressed(KeyEvent var1) {
   }

   private void btnNewEngActionPerformed(ActionEvent var1) {
      this.clearRAE();
   }

   private void btnSaveEngActionPerformed(ActionEvent var1) {
      this.btnSaveEng.setEnabled(false);
      Rubrique rubrique = (Rubrique)this.tRubriqueRetAE.getSelectedItem();
      if (this.menu.pc.insertRetAE(this.selectedOne, rubrique, this.tDateAccord.getDate(), ((Number)this.tCapital.getValue()).doubleValue(), ((Number)this.tEcheance.getValue()).doubleValue(), this.cActiveRetAE.isSelected(), this.tNoteRetAE.getText())) {
         this.menu.viewMessage(this.msgLabel, "Op\u00e9ration effecut\u00e9e avec succ\u00e9!", false);
         this.afficherRetenuesAE();
      } else {
         this.menu.viewMessage(this.msgLabel, "Err: Op\u00e9ration echou\u00e9e!", true);
      }

      this.btnSaveEng.setEnabled(true);
   }

   private void btnDelEngActionPerformed(ActionEvent var1) {
      if (this.selectedRAE != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Demande de confirmation ", 0);
         if (rep == 0) {
            this.btnDelEng.setEnabled(false);
            this.menu.gl.exQuery("delete from Tranchesretenuesaecheances where retenueAEcheances=" + this.selectedRAE.getId());
            if (this.menu.gl.deleteOcurance(this.selectedRAE)) {
               this.selectedOne.getRetenuesaecheanceses().remove(this.selectedRAE);
               this.afficherRetenuesAE();
            }

            this.btnDelEng.setEnabled(true);
         }
      }

   }

   private void retAETableMouseClicked(MouseEvent var1) {
      if (this.retAETable.getRowCount() > 0) {
         this.selectedRAE = this.menu.pc.retAEByID(this.selectedOne, ((Number)this.retAETable.getValueAt(this.retAETable.getSelectedRow(), 8)).longValue());
         if (this.selectedRAE != null) {
            double tr = this.menu.pc.totalReglementRetAE(this.selectedRAE);
            this.tRubriqueRetAE.setSelectedItem(this.selectedRAE.getRubrique());
            this.tRubriqueRetAE.setEnabled(false);
            this.tDateAccord.setDate(this.selectedRAE.getDateAccord());
            this.cActiveRetAE.setSelected(this.selectedRAE.isActive());
            this.tCapital.setValue(this.selectedRAE.getCapital());
            this.tEcheance.setValue(this.selectedRAE.getEcheance());
            this.tNoteRetAE.setText(this.selectedRAE.getNote());
            this.lbEncours.setText(this.menu.nf.format(this.selectedRAE.getCapital() - tr));
            this.lbPeriodeInit.setText(this.menu.spf.format(this.selectedRAE.getPeriode()));
            this.lbECCNG.setText(this.menu.nf.format(this.selectedRAE.getEcheancecourante()));
            this.lbECCNG.setText(this.menu.nf.format(this.selectedRAE.getEcheancecourantecng()));
            this.tTotReg.setValue(tr);
            this.afficherHistoriqueRetAE();
            this.tCapital.setEditable(this.menu.pc.totalReglementRetAE(this.selectedRAE) > (double)0.0F || this.menu.userName.equalsIgnoreCase("root"));
            this.btnSaveEng.setEnabled(!this.selectedRAE.isSolde());
            this.btnDelEng.setEnabled(!this.selectedRAE.isSolde());
         }
      }

   }

   private void tRubriqueRetAEFocusLost(FocusEvent var1) {
   }

   private void tRubriqueRetAEActionPerformed(ActionEvent var1) {
   }

   private void tCapitalCaretUpdate(CaretEvent var1) {
   }

   private void tCapitalFocusLost(FocusEvent var1) {
   }

   private void tEcheanceCaretUpdate(CaretEvent var1) {
   }

   private void tEcheanceFocusLost(FocusEvent var1) {
   }

   private void lbEncoursCaretUpdate(CaretEvent var1) {
   }

   private void lbEncoursFocusLost(FocusEvent var1) {
   }

   private void lbPeriodeInitCaretUpdate(CaretEvent var1) {
   }

   private void lbPeriodeInitFocusLost(FocusEvent var1) {
   }

   private void lbECCNGCaretUpdate(CaretEvent var1) {
   }

   private void lbECCNGFocusLost(FocusEvent var1) {
   }

   private void tTauxAnc4CaretUpdate(CaretEvent var1) {
   }

   private void tTauxAnc4FocusLost(FocusEvent var1) {
   }

   private void historiqueRetAETableMouseClicked(MouseEvent var1) {
   }

   private void tTotRegCaretUpdate(CaretEvent var1) {
   }

   private void tTotRegFocusLost(FocusEvent var1) {
   }

   private void tNomEnfantKeyPressed(KeyEvent var1) {
   }

   private void btnNewEnfActionPerformed(ActionEvent var1) {
      this.tEnfantID.setValue(0);
      this.tDateNaissEnf.setDate(new Date());
      this.tNomEnfant.setText("");
      this.tPereMereEnf.setText("");
   }

   private void btnSaveEnfActionPerformed(ActionEvent var1) {
      if (this.validateDataEnf().compareTo("") != 0) {
         this.menu.viewMessage(this.msgLabel, this.validateDataEnf(), true);
      } else if (this.tEnfantID.getValue() != null) {
         Enfants rs = this.menu.pc.enfantByEmp(((Number)this.tEnfantID.getValue()).longValue());
         if (rs == null) {
            rs = new Enfants();
            rs.setEmploye(this.selectedOne);
         }

         rs.setDateNaissanace(this.tDateNaissEnf.getDate());
         rs.setGenre(this.tGenreEnf.getSelectedItem().toString());
         rs.setMereOuPere(this.tPereMereEnf.getText());
         rs.setNomEnfant(this.tNomEnfant.getText());
         this.menu.gl.updateOcurance(rs);
         this.selectedOne.getEnfantss().remove(rs);
         this.selectedOne.getEnfantss().add(rs);
         this.selectedOne.setNbEnfants(this.selectedOne.getEnfantss().size());
         this.menu.gl.updateOcurance(this.selectedOne);
         this.afficherEnfant();
      }

   }

   private void btnDelEnfActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression des lignes selectionn\u00e9es ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Enfants rs = this.menu.pc.enfantByEmp(((Number)this.tEnfantID.getValue()).longValue());
         if (rs != null) {
            this.menu.gl.deleteOcurance(rs);
            this.selectedOne.getEnfantss().remove(rs);
            this.afficherEnfant();
         }
      }

   }

   private void tEnfantIDCaretUpdate(CaretEvent var1) {
   }

   private void tEnfantIDFocusLost(FocusEvent var1) {
   }

   private void listTableEnfantMouseClicked(MouseEvent var1) {
      long idEnfant = (long)((Number)this.listTableEnfant.getValueAt(this.listTableEnfant.getSelectedRow(), 4)).intValue();
      Enfants rs = this.menu.pc.enfantByEmp(idEnfant);
      if (rs != null) {
         this.tEnfantID.setValue(rs.getId());
         this.tDateNaissEnf.setDate(rs.getDateNaissanace());
         this.tNomEnfant.setText(rs.getNomEnfant());
         this.tGenreEnf.setSelectedItem(rs.getGenre());
         this.tPereMereEnf.setText(rs.getMereOuPere());
      }

   }

   private void tPereMereEnfKeyPressed(KeyEvent var1) {
   }

   private void tGenreEnfActionPerformed(ActionEvent var1) {
   }

   private void tNomDiplomeKeyPressed(KeyEvent var1) {
   }

   private void btnNewDiplomeActionPerformed(ActionEvent var1) {
      this.tDiplomeID.setValue(0);
      this.tNomDiplome.setText("");
      this.tDateObtDiplome.setDate(new Date());
      this.tDegreDiplome.setText("");
      this.tEtablissementDiplome.setText("");
      this.tDomaineDiplome.setText("");
   }

   private void btnSaveDiplomeActionPerformed(ActionEvent var1) {
      if (this.validateDataDiplome().compareTo("") != 0) {
         this.menu.viewMessage(this.msgLabel, this.validateDataDiplome(), true);
      } else if (this.tDiplomeID.getValue() != null) {
         Diplome rs = this.menu.pc.diplomeByEmp(((Number)this.tDiplomeID.getValue()).longValue());
         if (rs == null) {
            rs = new Diplome();
            rs.setEmploye(this.selectedOne);
         }

         rs.setNom(this.tNomDiplome.getText());
         rs.setDateObtention(this.tDateObtDiplome.getDate());
         rs.setDegre(this.tDegreDiplome.getText());
         rs.setDomaine(this.tDomaineDiplome.getText());
         rs.setEtablissement(this.tEtablissementDiplome.getText());
         this.menu.gl.updateOcurance(rs);
         this.selectedOne.getDiplomes().remove(rs);
         this.selectedOne.getDiplomes().add(rs);
         this.afficherDiplome();
      }

   }

   private void btnDelDiplomeActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression des lignes selectionn\u00e9es ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Diplome rs = this.menu.pc.diplomeByEmp((long)((Number)this.tDiplomeID.getValue()).intValue());
         if (rs != null) {
            this.menu.gl.deleteOcurance(rs);
            this.selectedOne.getDiplomes().remove(rs);
            this.afficherDiplome();
         }
      }

   }

   private void tDiplomeIDCaretUpdate(CaretEvent var1) {
   }

   private void tDiplomeIDFocusLost(FocusEvent var1) {
   }

   private void tDomaineDiplomeKeyPressed(KeyEvent var1) {
   }

   private void tDegreDiplomeKeyPressed(KeyEvent var1) {
   }

   private void tEtablissementDiplomeKeyPressed(KeyEvent var1) {
   }

   private void btnAppEngActionPerformed(ActionEvent var1) {
      Rubrique rubrique = (Rubrique)this.tRubriqueRetAE.getSelectedItem();
      if (this.menu.pc.insertRubrique(this.menu.paramsGen.getPeriodeCourante(), this.selectedOne, rubrique, this.menu.motifSN, this.menu.pc.totalEchRetenueAE_SN(this.selectedOne, rubrique), (double)1.0F, true, false)) {
         this.afficherRubriquePaie();
         this.menu.viewMessage(this.msgLabel, "Op\u00e9ration effecut\u00e9e avec succ\u00e9!", false);
      } else {
         this.menu.viewMessage(this.msgLabel, "Err: Op\u00e9ration echou\u00e9e!", true);
      }

   }

   private void cFerie50ActionPerformed(ActionEvent var1) {
   }

   private void tNoteJourKeyPressed(KeyEvent var1) {
      if (evt.getKeyCode() == 10) {
         this.btnSaveHSDayActionPerformed((ActionEvent)null);
      }

   }

   private void btnSaveHSDayActionPerformed(ActionEvent var1) {
      Thread t = new 275(this);
      t.start();
   }

   private void btnDelHSDayActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Date dateJour = new Date();

         for(int i = 0; i < this.heuresTable.getRowCount(); ++i) {
            if (this.heuresTable.isRowSelected(i)) {
               String s = this.heuresTable.getValueAt(i, 1).toString();

               try {
                  dateJour = (new SimpleDateFormat("dd/MM/yyyy")).parse(s);
               } catch (Exception e) {
                  e.printStackTrace();
               }

               Jour rs = this.menu.pc.jourById(this.selectedOne, dateJour);
               this.menu.gl.deleteOcurance(rs);
               this.selectedOne.getJours().remove(rs);
            }
         }

         this.afficherHeures();
      }

   }

   private void tTotPECaretUpdate(CaretEvent var1) {
   }

   private void tTotPEFocusLost(FocusEvent var1) {
   }

   private void heuresTableMouseClicked(MouseEvent var1) {
      if (this.tModeDecompte.getSelectedItem().toString().equalsIgnoreCase("Quotidien") && this.heuresTable.getRowCount() > 0) {
         Date dateJour = new Date();
         String s = this.heuresTable.getValueAt(this.heuresTable.getSelectedRow(), 1).toString();

         try {
            dateJour = (new SimpleDateFormat("dd/MM/yyyy")).parse(s);
         } catch (Exception e) {
            e.printStackTrace();
         }

         Jour rs = this.menu.pc.jourById(this.selectedOne, dateJour);
         this.tDateJour.setDate(rs.getDateJour());
         this.cFerie100.setSelected(rs.isFerie100());
         this.cFerie50.setSelected(rs.isFerie50());
         this.cSiteExterne.setSelected(rs.isSiteExterne());
         this.tHeuresJour.setValue(rs.getNbHeureJour());
         this.tHeuresNuit.setValue(rs.getNbHeureNuit());
         this.tPrimesPanier.setValue(rs.getNbPrimePanier());
         this.tNoteJour.setText(rs.getNote());
      }

   }

   private void tTotPPCaretUpdate(CaretEvent var1) {
   }

   private void tTotPPFocusLost(FocusEvent var1) {
   }

   private void tTotHSCaretUpdate(CaretEvent var1) {
   }

   private void tTotHSFocusLost(FocusEvent var1) {
   }

   private void tTotHS200CaretUpdate(CaretEvent var1) {
   }

   private void tTotHS200FocusLost(FocusEvent var1) {
   }

   private void tTotHS150CaretUpdate(CaretEvent var1) {
   }

   private void tTotHS150FocusLost(FocusEvent var1) {
   }

   private void tTotHS140CaretUpdate(CaretEvent var1) {
   }

   private void tTotHS140FocusLost(FocusEvent var1) {
   }

   private void tTotHS115CaretUpdate(CaretEvent var1) {
   }

   private void tTotHS115FocusLost(FocusEvent var1) {
   }

   private void tTotHJCaretUpdate(CaretEvent var1) {
   }

   private void tTotHJFocusLost(FocusEvent var1) {
   }

   private void tTotHNCaretUpdate(CaretEvent var1) {
   }

   private void tTotHNFocusLost(FocusEvent var1) {
   }

   private void tPrimesPanierCaretUpdate(CaretEvent var1) {
   }

   private void tPrimesPanierFocusLost(FocusEvent var1) {
   }

   private void tHeuresNuitCaretUpdate(CaretEvent var1) {
   }

   private void tHeuresNuitFocusLost(FocusEvent var1) {
   }

   private void cF50AutoActionPerformed(ActionEvent var1) {
   }

   private void cPPAutoActionPerformed(ActionEvent var1) {
   }

   private void cFerie100ActionPerformed(ActionEvent var1) {
   }

   private void cSiteExterneActionPerformed(ActionEvent var1) {
   }

   private void tHeuresJourCaretUpdate(CaretEvent var1) {
   }

   private void tHeuresJourFocusLost(FocusEvent var1) {
   }

   private void tModeDecompteActionPerformed(ActionEvent var1) {
      this.panelHS.setSelectedIndex(this.tModeDecompte.getSelectedItem().toString().equalsIgnoreCase("Quotidien") ? 0 : 1);
      this.panelHS.setEnabledAt(1, !this.tModeDecompte.getSelectedItem().toString().equalsIgnoreCase("Quotidien"));
      this.panelHS.setEnabledAt(0, this.tModeDecompte.getSelectedItem().toString().equalsIgnoreCase("Quotidien"));
      this.afficherHeures();
   }

   private void tWeekOT200CaretUpdate(CaretEvent var1) {
   }

   private void tWeekOT200FocusLost(FocusEvent var1) {
   }

   private void tWeekOT150CaretUpdate(CaretEvent var1) {
   }

   private void tWeekOT150FocusLost(FocusEvent var1) {
   }

   private void tWeekOT140CaretUpdate(CaretEvent var1) {
   }

   private void tWeekOT140FocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tWeekOT140);
      if (((Number)this.tWeekOT140.getValue()).doubleValue() > (double)6.0F) {
         this.tWeekOT140.setValue(6);
      }

   }

   private void tWeekOT115CaretUpdate(CaretEvent var1) {
   }

   private void tWeekOT115FocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tWeekOT115);
      if (((Number)this.tWeekOT115.getValue()).doubleValue() > (double)8.0F) {
         this.tWeekOT115.setValue(8);
      }

   }

   private void tWeekPPCaretUpdate(CaretEvent var1) {
   }

   private void tWeekPPFocusLost(FocusEvent var1) {
   }

   private void tWeekPECaretUpdate(CaretEvent var1) {
   }

   private void tWeekPEFocusLost(FocusEvent var1) {
   }

   private void btnSaveHSWeekActionPerformed(ActionEvent var1) {
      double primeEloinement = (double)0.0F;
      if (this.cSiteExterne.isSelected()) {
         primeEloinement = (double)1.0F;
      }

      try {
         this.menu.viewMessage(this.msgLabel, "", false);
         if (this.menu.pc.insertWeekHS(this.selectedOne, this.tBeginWeek.getDate(), this.tEndWeek.getDate(), ((Number)this.tWeekOT115.getValue()).doubleValue(), ((Number)this.tWeekOT140.getValue()).doubleValue(), ((Number)this.tWeekOT150.getValue()).doubleValue(), ((Number)this.tWeekOT200.getValue()).doubleValue(), ((Number)this.tPrimesPanier.getValue()).doubleValue(), primeEloinement, this.tNoteJour.getText())) {
            this.tWeekOT115.setValue(0);
            this.tWeekOT140.setValue(0);
            this.tWeekOT150.setValue(0);
            this.tWeekOT200.setValue(0);
            this.tWeekPP.setValue(0);
            this.tWeekPE.setValue(0);
            this.menu.viewMessage(this.msgLabel, "Op\u00e9ration effecut\u00e9e avec succ\u00e9!", false);
            this.afficherHeures();
         } else {
            this.menu.viewMessage(this.msgLabel, "Err: Op\u00e9ration echou\u00e9e!", true);
         }
      } catch (NullPointerException e) {
         e.printStackTrace();
      }

   }

   private void btnDelHSWeekActionPerformed(ActionEvent var1) {
   }

   private void tBeginWeekFocusLost(FocusEvent var1) {
      this.tEndWeek.setDate(this.menu.gl.addRetriveDays(this.tBeginWeek.getDate(), 8));
   }

   private void tHeuresJourActionPerformed(ActionEvent var1) {
      this.addPP();
   }

   private void tDateJourPropertyChange(PropertyChangeEvent var1) {
      this.infosJour();
   }

   private void tHeuresJourKeyPressed(KeyEvent var1) {
      if (evt.getKeyCode() == 10) {
         this.btnSaveHSDayActionPerformed((ActionEvent)null);
      }

   }

   private void tHeuresNuitKeyPressed(KeyEvent var1) {
      if (evt.getKeyCode() == 10) {
         this.btnSaveHSDayActionPerformed((ActionEvent)null);
      }

   }

   private void tPrimesPanierKeyPressed(KeyEvent var1) {
      if (evt.getKeyCode() == 10) {
         this.btnSaveHSDayActionPerformed((ActionEvent)null);
      }

   }

   private void tDebutRappelPropertyChange(PropertyChangeEvent var1) {
   }

   private void tIDRubRappelCaretUpdate(CaretEvent var1) {
   }

   private void tIDRubRappelFocusLost(FocusEvent var1) {
   }

   private void rappelTableMouseClicked(MouseEvent var1) {
   }

   private void tMotifRappelActionPerformed(ActionEvent var1) {
   }

   private void tPeriodeRappelActionPerformed(ActionEvent var1) {
   }

   private void tMontantRappelCaretUpdate(CaretEvent var1) {
   }

   private void btnSaveRappelSBActionPerformed(ActionEvent var1) {
      Thread t = new 276(this);
      t.start();
   }

   private void rubriquesSurSBTableMouseClicked(MouseEvent var1) {
      JTable var10000 = this.rappelTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmRappelSB(var10003));
      this.rappelTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      if (this.selectedOne != null && this.tDebutRappel.getDate() != null && this.tPeriodeRappel.getItemCount() > 1) {
         Date periodeRappel = (Date)this.tPeriodeRappel.getSelectedItem();
         Rubrique idRub = this.menu.pc.rubriqueById(((Number)this.rubriquesSurSBTable.getValueAt(this.rubriquesSurSBTable.getSelectedRow(), 0)).intValue());
         Motif idMotif = (Motif)this.tMotifRappel.getSelectedItem();
         this.tIDRubRappel.setValue(idRub.getId());
         double montantRapel = (double)0.0F;
         List<Rubriquepaie> dl = new ArrayList(this.selectedOne.getRubriquepaies());

         for(Rubriquepaie rs : (List)dl.stream().filter((var3x) -> var3x.getMotif().getId() == idMotif.getId() && var3x.getRubrique().getId() == idRub.getId() && var3x.getPeriode().after(this.tDebutRappel.getDate())).sorted(Comparator.comparing(Rubriquepaie::getPeriode)).collect(Collectors.toList())) {
            double base = rs.getBase();
            double nombre = rs.getNombre();
            if (rs.getRubrique().isBaseAuto() && this.menu.pc.formulRubriqueOnSB(idRub, "B")) {
               base = this.menu.pc.baseRbrique(periodeRappel, idRub, this.selectedOne, idMotif);
            }

            if (rs.getRubrique().isNombreAuto() && this.menu.pc.formulRubriqueOnSB(idRub, "N")) {
               nombre = this.menu.pc.nombreRbrique(periodeRappel, idRub, this.selectedOne, idMotif);
            }

            double montantApayer;
            if (idRub == this.menu.pc.usedRubID(1)) {
               base = this.menu.pc.rubriquePaieById(this.selectedOne, this.menu.pc.usedRubID(1), idMotif, periodeRappel).getBase();
               montantApayer = base * rs.getNombre();
            } else {
               montantApayer = base * nombre;
            }

            double relicatApayer = montantApayer - rs.getMontant();
            montantRapel += relicatApayer;
            ((ModelClass.tmRappelSB)this.rappelTable.getModel()).addRow(this.menu.pf.format(rs.getPeriode()), this.menu.nf.format(rs.getMontant()), this.menu.nf.format(montantApayer), this.menu.nf.format(relicatApayer));
         }

         this.tMontantRappel.setValue(montantRapel);
      }

   }

   private void btnRefreshItemActionPerformed(ActionEvent var1) {
      if (this.selectedOne != null) {
         this.findByID(this.selectedOne.getId(), true);
      }

   }

   private void toExcel() throws IOException, WriteException {
      String periodeDu = this.menu.fdf.format(this.tDernierDepart.getDate());
      String periodeAu = this.menu.fdf.format(new Date());
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      String fileName = "repport/CUMUL_CNG_MAT" + this.selectedOne.getId() + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Etat paie");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, "CUMUL DE CONGES DU " + periodeDu + " AU " + periodeAu);
      WriteExcel var10000 = this.menu.we;
      SimpleDateFormat var10004 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var10000.addLabelBold(excelSheet, 0, 1, "Edit\u00e9 le : " + var10004.format(var10005));
      this.menu.we.addLabelTitre(excelSheet, 0, 3, "Matricule");
      this.menu.we.addNumberBold(excelSheet, 1, 3, (double)this.selectedOne.getId());
      this.menu.we.addLabelTitre(excelSheet, 0, 4, "DATE NAISS.");
      this.menu.we.addLabelTitre(excelSheet, 1, 4, this.menu.fdf.format(this.selectedOne.getDateNaissance()));
      this.menu.we.addLabelTitre(excelSheet, 0, 5, "DATE EMBAUCHE");
      this.menu.we.addLabelTitre(excelSheet, 1, 5, this.menu.fdf.format(this.selectedOne.getDateEmbauche()));
      this.menu.we.addLabelTitre(excelSheet, 0, 6, "NOM ET PRENOM");
      var10000 = this.menu.we;
      String var29 = this.selectedOne.getPrenom();
      var10000.addLabelTitre(excelSheet, 1, 6, var29 + " " + this.selectedOne.getNom());
      List<Cumul> listCumul = new ArrayList();
      Double res = (double)0.0F;
      Paramgen pg = this.menu.paramsGen;
      Date periodeDD = this.tDernierDepart.getDate();
      Conges conge = this.menu.pc.congesById(this.selectedOne, periodeDD);
      List<Paie> dlInit = new ArrayList(this.selectedOne.getPaies());
      if (conge != null) {
         res = dlInit.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(conge.getPeriode())) && var2x.getMotif().getId() == 2).mapToDouble((var0) -> var0.getBt()).sum();
         listCumul.add(new Cumul(this, periodeDD, "DDC AU " + this.menu.fdf.format(periodeDD), res));
      }

      if (!this.menu.paramsGen.isAddCurrentSalInCumulCng()) {
         Paie paieCourant = this.menu.pc.paieById(this.selectedOne, this.menu.motifSN, this.menu.paramsGen.getPeriodeCourante());
         if (paieCourant != null) {
            Date var30 = paieCourant.getPeriode();
            String var33 = paieCourant.getMotif().getNom();
            listCumul.add(new Cumul(this, var30, var33 + " " + this.menu.pf.format(paieCourant.getPeriode()), paieCourant.getBt()));
         }
      }

      for(Paie rs : (List)dlInit.stream().filter((var3x) -> var3x.getMotif().getId() == 1 && var3x.getPeriode().after(this.menu.gl.addRetriveDays(periodeDD, 15)) && var3x.getPeriode().before(this.menu.gl.addRetriveDays(pg.getPeriodeSuivante(), 15))).sorted(Comparator.comparing(Paie::getPeriode)).collect(Collectors.toList())) {
         Date var31 = rs.getPeriode();
         String var34 = rs.getMotif().getNom();
         listCumul.add(new Cumul(this, var31, var34 + " " + this.menu.pf.format(rs.getPeriode()), rs.getBt()));
      }

      for(Paie rs : (List)dlInit.stream().filter((var3x) -> var3x.getMotif().getId() != 1 && var3x.getMotif().getId() != 2 && var3x.getPeriode().after(this.menu.gl.addRetriveDays(periodeDD, 15)) && var3x.getPeriode().before(this.menu.gl.addRetriveDays(pg.getPeriodeSuivante(), 15))).sorted(Comparator.comparing(Paie::getPeriode)).collect(Collectors.toList())) {
         List<Rubriquepaie> dlRP = new ArrayList(this.menu.pc.empRubriquepaie(rs.getEmploye(), rs.getMotif(), rs.getPeriode()));

         for(Rubriquepaie rp : (List)dlRP.stream().sorted(Comparator.comparing((var0) -> var0.getRubrique().getId())).collect(Collectors.toList())) {
            Date var32 = rs.getPeriode();
            String var35 = rp.getRubrique().getLibelle();
            listCumul.add(new Cumul(this, var32, var35 + " / " + rs.getMotif().getNom() + " " + this.menu.pf.format(rs.getPeriode()), rp.getMontant()));
         }
      }

      int row = 8;

      for(Cumul c : (List)listCumul.stream().sorted(Comparator.comparing(Cumul::getDate)).collect(Collectors.toList())) {
         this.menu.we.addLabelBoldBorder(excelSheet, 0, row, c.getLibelle());
         this.menu.we.addNumberBorder(excelSheet, 1, row, c.getMontant());
         ++row;
      }

      this.menu.we.addNumberBoldBorderSilver(excelSheet, 1, row, this.menu.we.columnSum(excelSheet, 1, row, 8, row - 1));
      this.menu.we.setColumnWidth(excelSheet, 0, 30);
      this.menu.we.setColumnWidth(excelSheet, 1, 10);
      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void btnExcelCumul_CongesActionPerformed(ActionEvent var1) {
      Thread t = new 277(this);
      t.start();
   }

   private void listTableDiplomeMouseClicked(MouseEvent var1) {
      int idDiplome = ((Number)this.listTableDiplome.getValueAt(this.listTableDiplome.getSelectedRow(), 5)).intValue();
      Diplome rs = this.menu.pc.diplomeByEmp((long)idDiplome);
      if (rs != null) {
         this.tDiplomeID.setValue(rs.getId());
         this.tNomDiplome.setText(rs.getNom());
         this.tDateObtDiplome.setDate(rs.getDateObtention());
         this.tDegreDiplome.setText(rs.getDegre());
         this.tEtablissementDiplome.setText(rs.getEtablissement());
         this.tDomaineDiplome.setText(rs.getDomaine());
      }

   }

   private void tNomDocumentKeyPressed(KeyEvent var1) {
   }

   private void btnNewDocActionPerformed(ActionEvent var1) {
      this.selectedDoc = new Document();
      this.selectedDoc.setEmploye(this.selectedOne);
      this.tNomDocument.setText("");
   }

   private void btnSaveDocActionPerformed(ActionEvent var1) {
      if (this.menu.grantNewVersionAccess) {
         this.selectedDoc.setNom(this.tNomDocument.getText());
         if (this.menu.gl.insertOcurance(this.selectedDoc)) {
            this.afficherListeDocuments();
         } else {
            this.menu.viewMessage(this.msgLabel, "Err: Op\u00e9ration echou\u00e9e!  \u0641\u0634\u0644 \u0641\u064a \u0627\u0644\u0639\u0645\u0644\u064a\u0629: \u062e\u0637\u0623", true);
         }
      } else {
         this.menu.showDemoMsg();
      }

   }

   private void btnDelDocActionPerformed(ActionEvent var1) {
   }

   private void listDocumentsMouseClicked(MouseEvent var1) {
      this.selectedDoc = (Document)((tmDocuments)this.listDocuments.getModel()).getValueAt(this.listDocuments.getRowSorter().convertRowIndexToModel(this.listDocuments.getSelectedRow()), 2);
      this.menu.openDocument(this.selectedDoc);
   }

   private void tDepartementActionPerformed(ActionEvent var1) {
   }

   private void tActiviteActionPerformed(ActionEvent var1) {
   }

   private void tPosteActionPerformed(ActionEvent var1) {
   }

   private void tBanqueActionPerformed(ActionEvent var1) {
   }

   private void tCategorieActionPerformed(ActionEvent var1) {
   }

   private void tClassificationKeyPressed(KeyEvent var1) {
   }

   private void tStatutKeyPressed(KeyEvent var1) {
   }

   private void tModePaiementActionPerformed(ActionEvent var1) {
      this.tBanque.setEnabled(this.tModePaiement.getSelectedItem().toString().equalsIgnoreCase("Virement"));
      this.tNoCompteBanque.setEnabled(this.tModePaiement.getSelectedItem().toString().equalsIgnoreCase("Virement"));
      this.cDomicilie.setEnabled(this.tModePaiement.getSelectedItem().toString().equalsIgnoreCase("Virement"));
   }

   private void tDirectiongeneralActionPerformed(ActionEvent var1) {
   }

   private void tDirectionActionPerformed(ActionEvent var1) {
   }

   private void cPsserviceActionPerformed(ActionEvent var1) {
   }

   private void tRaisonDebaucheKeyPressed(KeyEvent var1) {
   }

   private void tBudgetAnuelCaretUpdate(CaretEvent var1) {
      if (this.tBudgetAnuel.getValue() != null && this.selectedOne != null && ((Number)this.tBudgetAnuel.getValue()).doubleValue() > (double)0.0F) {
         double cba = this.menu.pc.cumulBTAnnuel(this.selectedOne);
         double ratio = cba / ((Number)this.tBudgetAnuel.getValue()).doubleValue() * (double)100.0F;
         this.tBARelicat.setValue(ratio);
      }

   }

   private void tBudgetAnuelFocusLost(FocusEvent var1) {
   }

   private void tNoCompteBanqueKeyPressed(KeyEvent var1) {
   }

   private void cDomicilieActionPerformed(ActionEvent var1) {
   }

   private void tTypeContratActionPerformed(ActionEvent var1) {
      if (this.tTypeContrat.getSelectedItem().toString().equalsIgnoreCase("CDD")) {
         this.tDateFinContrat.setEnabled(true);
      } else {
         this.tDateFinContrat.setEnabled(false);
      }

   }

   private void tDateAncienneteFocusLost(FocusEvent var1) {
   }

   private void tTauxAncCaretUpdate(CaretEvent var1) {
   }

   private void tTauxAncFocusLost(FocusEvent var1) {
   }

   private void tLieuTravailKeyPressed(KeyEvent var1) {
   }

   private void buttonConges1ActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous le changement de salaire de base ?", " Demande de confirmation", 0);
      if (rep == 0) {
         Thread t = new 278(this);
         t.start();
      }

   }

   private void appSBCatButtonActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous le changement de salaire de base ?", " Demande de confirmation", 0);
      if (rep == 0) {
         Thread t = new 279(this);
         t.start();
      }

   }

   private void cAvancmentAutoCatActionPerformed(ActionEvent var1) {
   }

   private void tNbAnneesCatCaretUpdate(CaretEvent var1) {
   }

   private void tNbAnneesCatFocusLost(FocusEvent var1) {
   }

   private void tHeureSemaineCaretUpdate(CaretEvent var1) {
   }

   private void tHeureSemaineFocusLost(FocusEvent var1) {
   }

   private void tNbMoisPreavisCaretUpdate(CaretEvent var1) {
   }

   private void tNbMoisPreavisFocusLost(FocusEvent var1) {
   }

   private void tTauxPSRACaretUpdate(CaretEvent var1) {
   }

   private void tTauxPSRAFocusLost(FocusEvent var1) {
   }

   private void cLUNweActionPerformed(ActionEvent var1) {
   }

   private void cMARweActionPerformed(ActionEvent var1) {
   }

   private void cDIMEndActionPerformed(ActionEvent var1) {
   }

   private void cSAMEndActionPerformed(ActionEvent var1) {
   }

   private void cJEUweActionPerformed(ActionEvent var1) {
   }

   private void cVENweActionPerformed(ActionEvent var1) {
   }

   private void cMAREndActionPerformed(ActionEvent var1) {
   }

   private void cMEREndActionPerformed(ActionEvent var1) {
   }

   private void cJEUEndActionPerformed(ActionEvent var1) {
   }

   private void cVENEndActionPerformed(ActionEvent var1) {
   }

   private void cMERweActionPerformed(ActionEvent var1) {
   }

   private void cSAMweActionPerformed(ActionEvent var1) {
   }

   private void cLUNBeginActionPerformed(ActionEvent var1) {
   }

   private void cMARBeginActionPerformed(ActionEvent var1) {
   }

   private void cMERBeginActionPerformed(ActionEvent var1) {
   }

   private void cJEUBeginActionPerformed(ActionEvent var1) {
   }

   private void cVENBeginActionPerformed(ActionEvent var1) {
   }

   private void cSAMBeginActionPerformed(ActionEvent var1) {
   }

   private void cDIMBeginActionPerformed(ActionEvent var1) {
   }

   private void cLUNEndActionPerformed(ActionEvent var1) {
   }

   private void cDIMweActionPerformed(ActionEvent var1) {
   }

   private void tCumulBrutImposableInitialCaretUpdate(CaretEvent var1) {
   }

   private void tCumulBrutImposableInitialFocusLost(FocusEvent var1) {
   }

   private void tCumulBrutNonImposableInitialCaretUpdate(CaretEvent var1) {
   }

   private void tCumulBrutNonImposableInitialFocusLost(FocusEvent var1) {
   }

   private void tCumul12DMinitialCaretUpdate(CaretEvent var1) {
   }

   private void tCumul12DMinitialFocusLost(FocusEvent var1) {
   }

   private void btnPrintPaieActionPerformed(ActionEvent var1) {
      if (this.selectedOne != null) {
         List<Rubriquepaie> dl = new ArrayList(this.menu.pc.empRubriquepaie(this.selectedOne, this.selectedMotif, this.selectedPeriode));
         List var7 = (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getRubrique().getId())).collect(Collectors.toList());
         List<FichePaieDetail> bulletinPaieDetail = new ArrayList();
         var7.forEach((var1x) -> {
            FichePaieDetail pd = new FichePaieDetail();
            pd.setCodeRub(var1x.getRubrique().getId());
            pd.setLibelle(var1x.getRubrique().getLibelle());
            pd.setSens(var1x.getRubrique().getSens());
            pd.setBase(var1x.getBase());
            pd.setNombre(var1x.getNombre());
            pd.setMontant(var1x.getMontant());
            bulletinPaieDetail.add(pd);
         });
         Paie paie = this.menu.pc.paieById(this.selectedOne, this.selectedMotif, this.selectedPeriode);
         if (paie != null) {
            try {
               Map<Object, Object> param = new HashMap();
               param.put("paie_categorie", paie.getCategorie());
               param.put("paie_BT", paie.getBt());
               param.put("paie_BNI", paie.getBni());
               param.put("paie_CNSS", paie.getCnss());
               param.put("paie_ITS", paie.getIts());
               param.put("paie_retenuesBrut", paie.getRetenuesBrut());
               param.put("paie_retenuesNet", paie.getRetenuesNet());
               param.put("paie_RITS", paie.getRits());
               param.put("paie_net", paie.getNet());
               param.put("paie_njt", paie.getNjt());
               param.put("paie_nbrHS", paie.getNbrHs());
               param.put("paie_CNAM", paie.getCnam());
               param.put("paie_periodeLettre", paie.getPeriodeLettre());
               param.put("paie_noCompteBanque", paie.getNoCompteBanque());
               param.put("paie_modePaiement", paie.getModePaiement());
               param.put("paie_domicilie", paie.isDomicilie());
               param.put("paie_paieDu", paie.getPaieDu());
               param.put("paie_paieAu", paie.getPaieAu());
               param.put("paie_biCNAM", paie.getBiCnam());
               param.put("paie_biCNSS", paie.getBiCnss());
               param.put("paie_RCNSS", paie.getRcnss());
               param.put("paie_RCNAM", paie.getRcnam());
               param.put("paie_biAVNAT", paie.getBiAvnat());
               param.put("paie_notePaie", paie.getNotePaie());
               param.put("motif_nom", paie.getMotif().getNom());
               param.put("paie_contratHeureMois", paie.getContratHeureMois());
               param.put("paie_banque", paie.getBanque());
               param.put("paie_poste", paie.getPoste());
               param.put("paie_activite", paie.getActivite());
               param.put("paie_departement", paie.getDepartement());
               param.put("paie_directiongenerale", paie.getDirectiongeneral());
               param.put("paie_direction", paie.getDirection());
               param.put("employe_id", this.selectedOne.getId());
               param.put("employe_prenom", this.selectedOne.getPrenom());
               param.put("employe_nom", this.selectedOne.getNom());
               param.put("employe_noCNSS", this.selectedOne.getNoCnss());
               param.put("employe_noCNAM", this.selectedOne.getNoCnam());
               param.put("employe_contratHeureSemaine", this.selectedOne.getContratHeureSemaine());
               param.put("employe_dateAnciennete", this.selectedOne.getDateAnciennete());
               param.put("employe_idPsservice", this.selectedOne.getIdPsservice());
               param.put("employe_nni", this.selectedOne.getNni());
               param.put("employe_dateEmbauche", this.selectedOne.getDateEmbauche());
               param.put("employe_idPsservice", this.selectedOne.getIdPsservice());
               param.put("employe_indice", "" + this.selectedOne.getTauxPsra());
               param.put("DS1", bulletinPaieDetail);
               this.menu.gl.afficherReportParamOnly(this.menu.paramsGen.getLicenceKey() != null && this.menu.paramsGen.getLicenceKey().equalsIgnoreCase("EX05L-P08WH-ZP20B-VPJGA") ? "bulletinPaie_imrop" : "bulletinPaie", param);
            } catch (Exception e) {
               e.printStackTrace();
            }
         } else {
            this.menu.viewMessage(this.msgLabel, "Aucune paie trouv\u00e9e pour le motif et la periode selection\u00e9s!", true);
         }
      } else {
         this.menu.viewMessage(this.msgLabel, "Aucun salari\u00e9 selection\u00e9!", true);
      }

   }

   private void btnCalPaieActionPerformed(ActionEvent var1) {
      Thread t = new 280(this);
      t.start();
   }

   private void btnInfosPaieActionPerformed(ActionEvent var1) {
      Thread t = new 281(this);
      t.start();
   }

   private void btnSaveEmpNoteActionPerformed(ActionEvent var1) {
      if (this.selectedOne != null) {
         this.selectedOne.setNoteSurBulletin(this.tNoteSurBulletin.getText());
         if (this.menu.gl.updateOcurance(this.selectedOne)) {
            this.menu.viewMessage(this.msgLabel, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.msgLabel, "Err: Op\u00e9ration echou\u00e9e", true);
         }
      } else {
         this.menu.viewMessage(this.msgLabel, "Err: Aucun salari\u00e9 selection\u00e9", true);
      }

   }

   private void tBARelicatCaretUpdate(CaretEvent var1) {
      this.tBARelicat.setBackground(((Number)this.tBARelicat.getValue()).doubleValue() < (double)0.0F ? Color.red : Color.GREEN);
   }

   private void tNoteSurBulletinKeyPressed(KeyEvent var1) {
   }

   private void btnDelPaieActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 282(this);
         t.start();
      }

   }

   private void btnSavePaieActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      Rubrique rubrique = (Rubrique)this.tRubriques.getSelectedItem();
      if (rubrique != null) {
         Thread t = new 283(this, rubrique);
         t.start();
      } else {
         this.menu.viewMessage(this.msgLabel, "Aucune rubrique selection\u00e9e!", true);
      }

   }

   private void cFixeActionPerformed(ActionEvent var1) {
   }

   private void tMontantCaretUpdate(CaretEvent var1) {
   }

   private void tNombreFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tNombre);
      this.calMontant();
   }

   private void tNombreCaretUpdate(CaretEvent var1) {
   }

   private void tBaseFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tBase);
      this.calMontant();
   }

   private void tBaseCaretUpdate(CaretEvent var1) {
   }

   private void tRubriquesActionPerformed(ActionEvent var1) {
      this.setEdit();
   }

   private void tRubriquesFocusLost(FocusEvent var1) {
   }

   private void rubriquePaieTableMouseClicked(MouseEvent var1) {
      if (this.rubriquePaieTable.getRowCount() > 0) {
         this.selectedPaie = (Rubriquepaie)this.rubriquePaieTable.getValueAt(this.rubriquePaieTable.getSelectedRow(), 8);
         if (this.selectedPaie != null) {
            this.selectedMotif = this.selectedPaie.getMotif();
            this.tMotif.setSelectedItem(this.selectedMotif);
            this.tRubriques.setSelectedItem(this.selectedPaie.getRubrique());
            this.cFixe.setSelected(this.selectedPaie.isFixe());
            this.tMontant.setText(this.menu.nf.format(this.selectedPaie.getMontant()));
            this.tNombre.setValue(this.selectedPaie.getNombre());
            this.tBase.setValue(this.selectedPaie.getBase());
            this.btnDelPaie.setEnabled(!this.selectedPaie.getPeriode().before(this.selectedPeriode));
         }
      }

   }

   private void btnSaveNJTActionPerformed(ActionEvent var1) {
      Thread t = new 284(this);
      t.start();
   }

   private void tNjtFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tNjt);
   }

   private void tNjtCaretUpdate(CaretEvent var1) {
   }

   private void cOnMotifActionPerformed(ActionEvent var1) {
      Thread t = new 285(this);
      t.start();
   }

   private void tMotifActionPerformed(ActionEvent var1) {
      Thread t = new 286(this);
      t.start();
   }

   private void btnSearchDocActionPerformed(ActionEvent var1) {
      JFileChooser chooser = new JFileChooser();
      int returnVal = chooser.showOpenDialog(this);
      if (returnVal == 0) {
         this.lbFilePath.setText(chooser.getSelectedFile().getPath());

         try {
            File file = chooser.getSelectedFile();
            byte[] fileData = new byte[(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(fileData);
            fis.close();
            this.selectedDoc.setDocFile(fileData);
            this.selectedDoc.setFileType(FilenameUtils.getExtension(file.getName()));
         } catch (IOException e) {
            this.menu.viewMessage(this.msgLabel, e.getMessage(), true);
         }
      }

   }

   private void btnValoriserHSActionPerformed(ActionEvent var1) {
      if (this.tTotHS.getValue() != null) {
         Thread t = new 287(this);
         t.start();
      } else {
         this.menu.viewMessage(this.msgLabel, "Pas d'heures supplementaires!", true);
      }

   }

   private void buttonCongesActionPerformed(ActionEvent var1) {
      if (this.selectedOne != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la r\u00e9prise du salari\u00e9 ?", " Demande de confirmation ", 0);
         if (rep == 0) {
            Thread t = new 288(this);
            t.start();
         }
      }

   }

   private void btnNew2ActionPerformed(ActionEvent var1) {
      this.btnNewActionPerformed((ActionEvent)null);
      this.jTabbedPane1.setSelectedIndex(1);
   }

   private void tAdresseKeyPressed(KeyEvent var1) {
   }

   private void tEmailKeyPressed(KeyEvent var1) {
   }

   private void tTelephoneKeyPressed(KeyEvent var1) {
   }

   private void tNoPermiTravailKeyPressed(KeyEvent var1) {
   }

   private void tNoCarteSejourKeyPressed(KeyEvent var1) {
   }

   private void tNoPassportKeyPressed(KeyEvent var1) {
   }

   private void tNNIKeyPressed(KeyEvent var1) {
   }

   private void tLieuNaissKeyPressed(KeyEvent var1) {
   }

   private void cExpatrieActionPerformed(ActionEvent var1) {
      this.tNoPermiTravail.setEnabled(this.cExpatrie.isSelected());
      this.tDateLPermiTravail.setEnabled(this.cExpatrie.isSelected());
      this.tDateEPermiTravail.setEnabled(this.cExpatrie.isSelected());
      this.tNoCarteSejour.setEnabled(this.cExpatrie.isSelected());
      this.tDateLVisa.setEnabled(this.cExpatrie.isSelected());
      this.tDateEVisa.setEnabled(this.cExpatrie.isSelected());
   }

   private void tNbEnfantsFocusLost(FocusEvent var1) {
   }

   private void tNbEnfantsCaretUpdate(CaretEvent var1) {
   }

   private void tSituationFamActionPerformed(ActionEvent var1) {
   }

   private void tSexeActionPerformed(ActionEvent var1) {
   }

   private void tZoneOrigineActionPerformed(ActionEvent var1) {
   }

   private void tNationaliteKeyPressed(KeyEvent var1) {
   }

   private void tIdPsserviceKeyPressed(KeyEvent var1) {
   }

   private void tPereKeyPressed(KeyEvent var1) {
   }

   private void tMereKeyPressed(KeyEvent var1) {
   }

   private void cActifActionPerformed(ActionEvent var1) {
   }

   private void tNomKeyPressed(KeyEvent var1) {
   }

   private void tPrenomKeyPressed(KeyEvent var1) {
   }

   private void tIDSalariePointeuseFocusLost(FocusEvent var1) {
   }

   private void tIDSalariePointeuseCaretUpdate(CaretEvent var1) {
   }

   private void tIdFocusLost(FocusEvent var1) {
   }

   private void tIdCaretUpdate(CaretEvent var1) {
   }

   private void lbPhotoMouseClicked(MouseEvent var1) {
      if (this.fileChooser == null) {
         this.fileChooser = new JFileChooser();
      }

      FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier image", new String[]{"PNG", "JPG", "JPEG", "BMP", "GIF", "TIFF"});
      this.fileChooser.setFileFilter(filter);
      if (this.fileChooser.showOpenDialog(this) == 0) {
         this.photoPath = this.fileChooser.getSelectedFile().getPath();
         File file = this.fileChooser.getSelectedFile();

         try {
            BufferedImage img = ImageIO.read(file);
            ImageIcon icon = new ImageIcon(img);
            menu var10000 = this.menu;
            Image zoom = com.mccmr.ui.menu.scaleImage(icon.getImage(), 270, 330);
            Icon iconScaled = new ImageIcon(zoom);
            this.lbPhoto.setIcon(iconScaled);
            this.lbPhoto.revalidate();
            this.lbPhoto.repaint();
         } catch (IOException e) {
            this.menu.viewMessage(this.msgLabel, e.getMessage(), true);
         }
      }

   }

   private void panelHSMouseClicked(MouseEvent var1) {
   }

   private void addPP() {
      double nbPP = (double)0.0F;
      if (this.cPPAuto.isSelected()) {
         if (((Number)this.tHeuresJour.getValue()).doubleValue() >= (double)9.0F) {
            ++nbPP;
         }

         if (((Number)this.tHeuresNuit.getValue()).doubleValue() >= (double)6.0F) {
            ++nbPP;
         }

         this.tPrimesPanier.setValue(nbPP);
      }

   }

   private void infosJour() {
      if (this.selectedOne != null && this.tDateJour.getDate() != null && this.cF50Auto.isSelected()) {
         this.cFerie50.setSelected(this.menu.pc.infosJourById(this.tDateJour.getDate(), this.selectedOne).isWeekEnd());
      }

   }
}
