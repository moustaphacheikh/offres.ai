package com.mccmr.ui;

import com.mccmr.entity.Banque;
import com.mccmr.entity.Departement;
import com.mccmr.entity.Employe;
import com.mccmr.entity.Grillesalairebase;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Njtsalarie;
import com.mccmr.entity.Origines;
import com.mccmr.entity.Poste;
import com.mccmr.entity.Rubrique;
import com.mccmr.entity.Utilisateurs;
import com.mccmr.util.ModelClass;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class EmployequeryFrame extends JInternalFrame {
   public menu menu;
   public List<Employe> dataList;
   private JFormattedTextField MAJtNbAnneesCat;
   private JCheckBox cActif;
   private JCheckBox cAppSBCatgeorie;
   private JCheckBox cCheckAll;
   private JRadioButton cCondActif;
   private JRadioButton cCondBanque;
   private JRadioButton cCondCNAM;
   private JRadioButton cCondCNSS;
   private JRadioButton cCondCategorie;
   private JRadioButton cCondDepartement;
   private JRadioButton cCondDomicilie;
   private JRadioButton cCondEmbauche;
   private JRadioButton cCondEnConges;
   private JRadioButton cCondEnDebauche;
   private JRadioButton cCondID;
   private JRadioButton cCondITS;
   private JRadioButton cCondModePaie;
   private JRadioButton cCondOrigine;
   private JRadioButton cCondPoste;
   private JRadioButton cCondSexe;
   private JRadioButton cCondSitFam;
   private JRadioButton cCondSortie;
   private JRadioButton cCondTypeContrat;
   private JCheckBox cDIMBegin;
   private JCheckBox cDIMEnd;
   private JCheckBox cDIMwe;
   private JCheckBox cDetacheCNAM;
   private JCheckBox cDetacheCNSS;
   private JCheckBox cDomicilie;
   private JCheckBox cDroitsConges;
   private JCheckBox cEnConges;
   private JCheckBox cEnDebauche;
   private JCheckBox cExonoreITS;
   private JCheckBox cFixe;
   private JCheckBox cIndLicenciement;
   private JCheckBox cIndLicenciementCollectif;
   private JCheckBox cIndPreavis;
   private JCheckBox cIndRetraite;
   private JCheckBox cJEUBegin;
   private JCheckBox cJEUEnd;
   private JCheckBox cJEUwe;
   private JCheckBox cLUNBegin;
   private JCheckBox cLUNEnd;
   private JCheckBox cLUNwe;
   private JCheckBox cMAJActif;
   private JRadioButton cMAJActifOK;
   private JCheckBox cMAJAvancmentAutoCat;
   private JRadioButton cMAJAvancmentAutoCatOK;
   private JRadioButton cMAJBanqueOK;
   private JRadioButton cMAJCategorieOK;
   private JRadioButton cMAJDateFinContratOK;
   private JRadioButton cMAJDepartementOK1;
   private JCheckBox cMAJDetacheCNAM;
   private JRadioButton cMAJDetacheCNAMOK;
   private JCheckBox cMAJDetacheCNSS;
   private JRadioButton cMAJDetacheCNSSOK;
   private JCheckBox cMAJDomicilie;
   private JRadioButton cMAJDomicilieOK;
   private JCheckBox cMAJEnConges;
   private JRadioButton cMAJEnCongesOK;
   private JCheckBox cMAJExonoreITS;
   private JRadioButton cMAJExonoreITSOK;
   private JRadioButton cMAJHeureSemaineOK;
   private JRadioButton cMAJModePaiementOK;
   private JRadioButton cMAJNbMoisPreavisOK;
   private JRadioButton cMAJPosteOK;
   private JRadioButton cMAJSexeOK;
   private JRadioButton cMAJSituationFamOK;
   private JRadioButton cMAJTypeContratOK;
   private JRadioButton cMAJZoneOrigineOK;
   private JCheckBox cMARBegin;
   private JCheckBox cMAREnd;
   private JCheckBox cMARwe;
   private JCheckBox cMERBegin;
   private JCheckBox cMEREnd;
   private JCheckBox cMERwe;
   private JCheckBox cSAMBegin;
   private JCheckBox cSAMEnd;
   private JCheckBox cSAMwe;
   private JCheckBox cSolderRetenues;
   private JCheckBox cSuppRubrique;
   private JCheckBox cVENBegin;
   private JCheckBox cVENEnd;
   private JCheckBox cVENwe;
   private JButton jButton2;
   private JLabel jLabel11;
   private JLabel jLabel12;
   private JLabel jLabel13;
   private JLabel jLabel137;
   private JLabel jLabel14;
   private JLabel jLabel150;
   private JLabel jLabel151;
   private JLabel jLabel152;
   private JLabel jLabel153;
   private JLabel jLabel154;
   private JLabel jLabel2;
   private JLabel jLabel27;
   private JLabel jLabel28;
   private JLabel jLabel3;
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
   private JLabel jLabel4;
   private JLabel jLabel40;
   private JLabel jLabel46;
   private JLabel jLabel47;
   private JLabel jLabel48;
   private JLabel jLabel49;
   private JLabel jLabel5;
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
   private JLabel jLabel90;
   private JLabel jLabel91;
   private JLabel jLabel92;
   private JLabel jLabel93;
   private JLabel jLabel95;
   private JPanel jPanel1;
   private JPanel jPanel12;
   private JPanel jPanel2;
   private JPanel jPanel3;
   private JPanel jPanel4;
   private JPanel jPanel5;
   private JPanel jPanel6;
   private JPanel jPanel7;
   private JScrollPane jScrollPane4;
   private JScrollPane jScrollPane5;
   private JScrollPane jScrollPane7;
   private JSeparator jSeparator1;
   private JSeparator jSeparator2;
   private JSeparator jSeparator3;
   private JSeparator jSeparator4;
   private JSeparator jSeparator5;
   private JSeparator jSeparator6;
   private JTabbedPane jTabbedPane1;
   private JLabel msgErrLabel;
   private JLabel msgInfoLabel;
   private JProgressBar progressBar;
   private JButton resultButton;
   private JTable resultTable;
   private JComboBox tBanque;
   private JFormattedTextField tBase;
   private JComboBox tCategorie;
   private JDateChooser tDateDepart;
   private JDateChooser tDateReprise;
   private JComboBox tDecompteType;
   private JComboBox tDepartement;
   private JDateChooser tEmbaucheAu;
   private JDateChooser tEmbaucheDu;
   private JFormattedTextField tIdAu;
   private JFormattedTextField tIdDu;
   private JComboBox tMAJBanque;
   private JComboBox tMAJCategorie;
   private JDateChooser tMAJDateFinContrat;
   private JComboBox tMAJDepartement;
   private JFormattedTextField tMAJHeureSemaine;
   private JComboBox tMAJModePaiement;
   private JFormattedTextField tMAJNbMoisPreavis;
   private JComboBox tMAJPoste;
   private JComboBox tMAJSexe;
   private JComboBox tMAJSituationFam;
   private JComboBox tMAJTypeContrat;
   private JComboBox tMAJZoneOrigine;
   private JComboBox tModePaiement;
   private JComboBox tMotif;
   private JComboBox tMotifMouvement;
   private JFormattedTextField tNbResults;
   private JFormattedTextField tNjt;
   private JFormattedTextField tNombre;
   private JTextArea tNoteConges;
   private JTextArea tNoteSurBulletin;
   private JComboBox tPoste;
   private JComboBox tRubrique;
   private JComboBox tSexe;
   private JComboBox tSituationFam;
   private JDateChooser tSortieAu;
   private JDateChooser tSortieDu;
   private JComboBox tTypeContrat;
   private JComboBox tZoneOrigine;
   private JButton validerDecompteButton;
   private JButton validerMajButton;
   private JButton validerMajButton1;
   private JButton validerMsgPaieCouranteButton;
   private JButton validerNjtButton;
   private JButton validerRubriquePaieButton;

   public EmployequeryFrame() {
      this.initComponents();
   }

   public void refresh() {
      this.menu.remplirCombo("Grillesalairebase", this.tCategorie);
      this.menu.remplirCombo("Departement", this.tDepartement);
      this.menu.remplirCombo("Poste", this.tPoste);
      this.menu.remplirCombo("Origines", this.tZoneOrigine);
      this.menu.remplirCombo("Banque", this.tBanque);
      this.menu.remplirCombo("Rubrique", this.tRubrique);
      this.menu.remplirCombo("Motif", this.tMotif);
      this.menu.remplirCombo("Grillesalairebase", this.tMAJCategorie);
      this.menu.remplirCombo("Departement", this.tMAJDepartement);
      this.menu.remplirCombo("Poste", this.tMAJPoste);
      this.menu.remplirCombo("Origines", this.tMAJZoneOrigine);
      this.menu.remplirCombo("Banque", this.tMAJBanque);
      this.tDateDepart.setDate(new Date());
      this.tDateReprise.setDate(new Date());
   }

   public void RolesAction(Utilisateurs var1) {
      if (!role.isMaj()) {
         this.validerDecompteButton.setVisible(false);
         this.validerMajButton.setVisible(false);
         this.validerMsgPaieCouranteButton.setVisible(false);
         this.validerNjtButton.setVisible(false);
         this.validerRubriquePaieButton.setVisible(false);
      }

   }

   private void initComponents() {
      this.msgInfoLabel = new JLabel();
      this.msgErrLabel = new JLabel();
      this.jTabbedPane1 = new JTabbedPane();
      this.jPanel1 = new JPanel();
      this.tEmbaucheDu = new JDateChooser();
      this.jLabel71 = new JLabel();
      this.tEmbaucheAu = new JDateChooser();
      this.cEnConges = new JCheckBox();
      this.cEnDebauche = new JCheckBox();
      this.tSortieDu = new JDateChooser();
      this.jLabel72 = new JLabel();
      this.tSortieAu = new JDateChooser();
      this.tIdDu = new JFormattedTextField();
      this.tIdAu = new JFormattedTextField();
      this.jLabel3 = new JLabel();
      this.jLabel30 = new JLabel();
      this.tTypeContrat = new JComboBox();
      this.jLabel33 = new JLabel();
      this.jLabel34 = new JLabel();
      this.jLabel35 = new JLabel();
      this.tCategorie = new JComboBox();
      this.tPoste = new JComboBox();
      this.tDepartement = new JComboBox();
      this.jLabel46 = new JLabel();
      this.tModePaiement = new JComboBox();
      this.jLabel47 = new JLabel();
      this.tBanque = new JComboBox();
      this.cDomicilie = new JCheckBox();
      this.cDetacheCNSS = new JCheckBox();
      this.cDetacheCNAM = new JCheckBox();
      this.cExonoreITS = new JCheckBox();
      this.jLabel38 = new JLabel();
      this.tZoneOrigine = new JComboBox();
      this.jLabel12 = new JLabel();
      this.tSituationFam = new JComboBox();
      this.jLabel11 = new JLabel();
      this.tSexe = new JComboBox();
      this.jSeparator1 = new JSeparator();
      this.jSeparator2 = new JSeparator();
      this.jLabel2 = new JLabel();
      this.jLabel4 = new JLabel();
      this.jLabel5 = new JLabel();
      this.cActif = new JCheckBox();
      this.jSeparator6 = new JSeparator();
      this.resultButton = new JButton();
      this.cCondEnDebauche = new JRadioButton();
      this.cCondEnConges = new JRadioButton();
      this.cCondActif = new JRadioButton();
      this.cCondEmbauche = new JRadioButton();
      this.cCondSortie = new JRadioButton();
      this.cCondID = new JRadioButton();
      this.cCondDepartement = new JRadioButton();
      this.cCondPoste = new JRadioButton();
      this.cCondCategorie = new JRadioButton();
      this.cCondDomicilie = new JRadioButton();
      this.cCondBanque = new JRadioButton();
      this.cCondOrigine = new JRadioButton();
      this.cCondTypeContrat = new JRadioButton();
      this.cCondITS = new JRadioButton();
      this.cCondCNSS = new JRadioButton();
      this.cCondCNAM = new JRadioButton();
      this.cCondModePaie = new JRadioButton();
      this.cCondSitFam = new JRadioButton();
      this.cCondSexe = new JRadioButton();
      this.jPanel4 = new JPanel();
      this.jLabel31 = new JLabel();
      this.tMAJTypeContrat = new JComboBox();
      this.cMAJDetacheCNSS = new JCheckBox();
      this.cMAJDetacheCNAM = new JCheckBox();
      this.cMAJExonoreITS = new JCheckBox();
      this.tMAJSexe = new JComboBox();
      this.jLabel13 = new JLabel();
      this.tMAJSituationFam = new JComboBox();
      this.jLabel14 = new JLabel();
      this.jLabel39 = new JLabel();
      this.tMAJZoneOrigine = new JComboBox();
      this.tMAJBanque = new JComboBox();
      this.jLabel48 = new JLabel();
      this.jLabel49 = new JLabel();
      this.tMAJModePaiement = new JComboBox();
      this.cMAJDomicilie = new JCheckBox();
      this.tMAJCategorie = new JComboBox();
      this.jLabel36 = new JLabel();
      this.jLabel37 = new JLabel();
      this.tMAJPoste = new JComboBox();
      this.tMAJDepartement = new JComboBox();
      this.jLabel40 = new JLabel();
      this.cMAJEnConges = new JCheckBox();
      this.cMAJActif = new JCheckBox();
      this.jLabel27 = new JLabel();
      this.tMAJHeureSemaine = new JFormattedTextField();
      this.jLabel28 = new JLabel();
      this.tMAJNbMoisPreavis = new JFormattedTextField();
      this.jLabel32 = new JLabel();
      this.tMAJDateFinContrat = new JDateChooser();
      this.cMAJAvancmentAutoCat = new JCheckBox();
      this.MAJtNbAnneesCat = new JFormattedTextField();
      this.validerMajButton = new JButton();
      this.cAppSBCatgeorie = new JCheckBox();
      this.cMAJActifOK = new JRadioButton();
      this.cMAJEnCongesOK = new JRadioButton();
      this.cMAJPosteOK = new JRadioButton();
      this.cMAJDepartementOK1 = new JRadioButton();
      this.cMAJCategorieOK = new JRadioButton();
      this.cMAJDomicilieOK = new JRadioButton();
      this.cMAJBanqueOK = new JRadioButton();
      this.cMAJDateFinContratOK = new JRadioButton();
      this.cMAJNbMoisPreavisOK = new JRadioButton();
      this.cMAJExonoreITSOK = new JRadioButton();
      this.cMAJSexeOK = new JRadioButton();
      this.cMAJZoneOrigineOK = new JRadioButton();
      this.cMAJSituationFamOK = new JRadioButton();
      this.cMAJDetacheCNSSOK = new JRadioButton();
      this.cMAJAvancmentAutoCatOK = new JRadioButton();
      this.cMAJHeureSemaineOK = new JRadioButton();
      this.cMAJModePaiementOK = new JRadioButton();
      this.cMAJDetacheCNAMOK = new JRadioButton();
      this.cMAJTypeContratOK = new JRadioButton();
      this.jPanel7 = new JPanel();
      this.jPanel12 = new JPanel();
      this.jLabel90 = new JLabel();
      this.jLabel91 = new JLabel();
      this.jLabel92 = new JLabel();
      this.jLabel93 = new JLabel();
      this.jLabel137 = new JLabel();
      this.jLabel150 = new JLabel();
      this.jLabel151 = new JLabel();
      this.jLabel152 = new JLabel();
      this.jLabel153 = new JLabel();
      this.jLabel154 = new JLabel();
      this.cDIMBegin = new JCheckBox();
      this.cDIMEnd = new JCheckBox();
      this.cDIMwe = new JCheckBox();
      this.cLUNBegin = new JCheckBox();
      this.cLUNEnd = new JCheckBox();
      this.cLUNwe = new JCheckBox();
      this.cMARBegin = new JCheckBox();
      this.cMAREnd = new JCheckBox();
      this.cMARwe = new JCheckBox();
      this.cMERBegin = new JCheckBox();
      this.cMEREnd = new JCheckBox();
      this.cMERwe = new JCheckBox();
      this.cJEUBegin = new JCheckBox();
      this.cJEUEnd = new JCheckBox();
      this.cJEUwe = new JCheckBox();
      this.cVENBegin = new JCheckBox();
      this.cVENEnd = new JCheckBox();
      this.cVENwe = new JCheckBox();
      this.cSAMBegin = new JCheckBox();
      this.cSAMEnd = new JCheckBox();
      this.cSAMwe = new JCheckBox();
      this.validerMajButton1 = new JButton();
      this.jPanel3 = new JPanel();
      this.jLabel67 = new JLabel();
      this.tRubrique = new JComboBox();
      this.cFixe = new JCheckBox();
      this.jLabel66 = new JLabel();
      this.tMotif = new JComboBox();
      this.jLabel68 = new JLabel();
      this.tBase = new JFormattedTextField();
      this.jLabel70 = new JLabel();
      this.tNombre = new JFormattedTextField();
      this.cSuppRubrique = new JCheckBox();
      this.validerRubriquePaieButton = new JButton();
      this.jSeparator3 = new JSeparator();
      this.tNjt = new JFormattedTextField();
      this.jLabel73 = new JLabel();
      this.validerNjtButton = new JButton();
      this.jSeparator4 = new JSeparator();
      this.jLabel69 = new JLabel();
      this.jScrollPane4 = new JScrollPane();
      this.tNoteSurBulletin = new JTextArea();
      this.validerMsgPaieCouranteButton = new JButton();
      this.jSeparator5 = new JSeparator();
      this.jPanel5 = new JPanel();
      this.jLabel74 = new JLabel();
      this.tDateDepart = new JDateChooser();
      this.jLabel75 = new JLabel();
      this.jScrollPane5 = new JScrollPane();
      this.tNoteConges = new JTextArea();
      this.jPanel6 = new JPanel();
      this.cDroitsConges = new JCheckBox();
      this.cIndPreavis = new JCheckBox();
      this.cIndLicenciement = new JCheckBox();
      this.cIndLicenciementCollectif = new JCheckBox();
      this.cSolderRetenues = new JCheckBox();
      this.cIndRetraite = new JCheckBox();
      this.validerDecompteButton = new JButton();
      this.jLabel95 = new JLabel();
      this.tDateReprise = new JDateChooser();
      this.tDecompteType = new JComboBox();
      this.tMotifMouvement = new JComboBox();
      this.jLabel76 = new JLabel();
      this.jPanel2 = new JPanel();
      this.jButton2 = new JButton();
      this.jScrollPane7 = new JScrollPane();
      this.resultTable = new JTable();
      this.jLabel7 = new JLabel();
      this.tNbResults = new JFormattedTextField();
      this.cCheckAll = new JCheckBox();
      this.progressBar = new JProgressBar();
      this.setClosable(true);
      this.setIconifiable(true);
      this.setTitle("ELIYA PAIE::Requettes");
      this.msgInfoLabel.setForeground(new Color(0, 102, 0));
      this.msgInfoLabel.setText(".");
      this.msgErrLabel.setForeground(new Color(204, 0, 0));
      this.msgErrLabel.setText(".");
      this.jTabbedPane1.setBorder(BorderFactory.createEtchedBorder());
      this.jTabbedPane1.setFont(new Font("Segoe UI Light", 0, 13));
      this.tEmbaucheDu.setDateFormatString("dd/MM/yyyy");
      this.jLabel71.setHorizontalAlignment(0);
      this.jLabel71.setText("au");
      this.tEmbaucheAu.setDateFormatString("dd/MM/yyyy");
      this.cEnConges.setText("En cong\u00e9s");
      this.cEnDebauche.setText("En d\u00e9bauche");
      this.tSortieDu.setDateFormatString("dd/MM/yyyy");
      this.jLabel72.setHorizontalAlignment(0);
      this.jLabel72.setText("au");
      this.tSortieAu.setDateFormatString("dd/MM/yyyy");
      this.tIdDu.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tIdAu.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.jLabel3.setHorizontalAlignment(0);
      this.jLabel3.setText("au");
      this.jLabel30.setText("Type contrat");
      this.tTypeContrat.setModel(new DefaultComboBoxModel(new String[]{"CDI", "CDD"}));
      this.jLabel33.setText("Departement");
      this.jLabel34.setText("Poste");
      this.jLabel35.setText("Cat\u00e9gorie");
      this.tCategorie.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPoste.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.jLabel46.setText("Mode de paye");
      this.tModePaiement.setModel(new DefaultComboBoxModel(new String[]{"Esp\u00e8ce", "Ch\u00e8que", "Virement"}));
      this.jLabel47.setText("Banque");
      this.tBanque.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.cDomicilie.setText("Domicil\u00e9");
      this.cDetacheCNSS.setText("Detach\u00e9 CNSS");
      this.cDetacheCNAM.setText("Detach\u00e9 CNAM");
      this.cExonoreITS.setText("Exonor\u00e9 ITS");
      this.jLabel38.setText("Zone d'origine");
      this.tZoneOrigine.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.jLabel12.setText("Situation familiale");
      this.tSituationFam.setModel(new DefaultComboBoxModel(new String[]{"C", "M", "D", "V"}));
      this.jLabel11.setText("Sexe");
      this.tSexe.setModel(new DefaultComboBoxModel(new String[]{"F", "M"}));
      this.jLabel2.setText("Embauche du");
      this.jLabel4.setText("Sortie du");
      this.jLabel5.setText("ID du");
      this.cActif.setBackground(new Color(153, 153, 153));
      this.cActif.setText("Actif");
      this.resultButton.setFont(new Font("Segoe UI Light", 0, 13));
      this.resultButton.setText("Afficher le resultat");
      this.resultButton.addActionListener(new 1(this));
      this.cCondEnDebauche.setBackground(new Color(0, 102, 0));
      this.cCondEnConges.setBackground(new Color(0, 102, 0));
      this.cCondActif.setBackground(new Color(0, 102, 0));
      this.cCondEmbauche.setBackground(new Color(0, 102, 0));
      this.cCondSortie.setBackground(new Color(0, 102, 0));
      this.cCondID.setBackground(new Color(0, 102, 0));
      this.cCondDepartement.setBackground(new Color(0, 102, 0));
      this.cCondPoste.setBackground(new Color(0, 102, 0));
      this.cCondCategorie.setBackground(new Color(0, 102, 0));
      this.cCondDomicilie.setBackground(new Color(0, 102, 0));
      this.cCondBanque.setBackground(new Color(0, 102, 0));
      this.cCondOrigine.setBackground(new Color(0, 102, 0));
      this.cCondTypeContrat.setBackground(new Color(0, 102, 0));
      this.cCondITS.setBackground(new Color(0, 102, 0));
      this.cCondCNSS.setBackground(new Color(0, 102, 0));
      this.cCondCNAM.setBackground(new Color(0, 102, 0));
      this.cCondModePaie.setBackground(new Color(0, 102, 0));
      this.cCondSitFam.setBackground(new Color(0, 102, 0));
      this.cCondSexe.setBackground(new Color(0, 102, 0));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jSeparator6, -2, 401, -2).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel12, -2, 92, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tSituationFam, -2, 61, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cCondSitFam).addGap(36, 36, 36).addComponent(this.jLabel11).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tSexe, -2, 52, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cCondSexe))).addContainerGap(-1, 32767)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jSeparator1).addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel4, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel2, Alignment.LEADING, -2, 75, 32767).addComponent(this.jLabel5, -1, -1, 32767)).addGap(0, 0, 32767).addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tIdDu, -2, 101, -2).addComponent(this.tSortieDu, -2, 121, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel3, -1, -1, 32767).addComponent(this.jLabel72, -2, 26, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.tIdAu, -2, 98, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.cCondID)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.tSortieAu, -2, 120, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cCondSortie)))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.tEmbaucheDu, -2, 121, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel71, -2, 26, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.tEmbaucheAu, -2, 120, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cCondEmbauche))).addGap(27, 27, 27)).addComponent(this.jSeparator2, Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.cActif, -2, 70, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cCondActif).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.cEnConges, -2, 90, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cCondEnConges).addGap(35, 35, 35).addComponent(this.cEnDebauche).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cCondEnDebauche)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel33, -2, 89, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tDepartement, -2, 260, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cCondDepartement).addGap(0, 0, 32767)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel38, -1, -1, 32767).addComponent(this.jLabel46, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel35, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel34, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel47, Alignment.LEADING, -1, -1, 32767)).addComponent(this.jLabel30)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.tTypeContrat, -2, 85, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cCondTypeContrat)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.tZoneOrigine, Alignment.LEADING, 0, 260, 32767).addComponent(this.tBanque, Alignment.LEADING, 0, -1, 32767)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cCondOrigine).addComponent(this.cCondBanque))).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.tModePaiement, -2, 151, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cCondModePaie).addGap(22, 22, 22).addComponent(this.cDomicilie, -2, 71, -2)).addComponent(this.tCategorie, -2, 261, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cCondCategorie).addComponent(this.cCondDomicilie))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.tPoste, 0, -1, 32767).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cCondPoste).addGap(3, 3, 3))))).addGap(23, 23, 23))).addGap(703, 703, 703)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.cDetacheCNSS).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cCondCNSS).addGap(29, 29, 29).addComponent(this.cDetacheCNAM).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cCondCNAM).addGap(34, 34, 34).addComponent(this.cExonoreITS).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cCondITS)).addComponent(this.resultButton, -2, 438, -2)).addGap(0, 0, 32767)))));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cActif).addComponent(this.cCondActif)).addComponent(this.cEnDebauche).addComponent(this.cCondEnDebauche).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.cCondEnConges).addGap(2, 2, 2))).addGap(6, 6, 6).addComponent(this.jSeparator1, -2, 10, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tEmbaucheAu, -2, -1, -2).addComponent(this.tEmbaucheDu, -2, -1, -2).addComponent(this.jLabel2).addComponent(this.jLabel71).addComponent(this.cCondEmbauche)).addGap(10, 10, 10).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel72).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel4).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tSortieDu, -1, -1, 32767).addComponent(this.tSortieAu, -1, -1, 32767)).addComponent(this.cCondSortie, Alignment.TRAILING)).addGap(10, 10, 10).addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tIdDu, -2, -1, -2).addComponent(this.tIdAu, -2, -1, -2).addComponent(this.jLabel5).addComponent(this.jLabel3)).addComponent(this.cCondID)))).addGap(6, 6, 6).addComponent(this.jSeparator2, -2, 5, -2).addGap(6, 6, 6).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel33).addComponent(this.tDepartement, -2, 25, -2)).addComponent(this.cCondDepartement)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel34).addComponent(this.tPoste, -2, 25, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cCondCategorie, Alignment.TRAILING).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel35).addComponent(this.tCategorie, -2, 25, -2)))).addComponent(this.cCondPoste)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cDomicilie).addComponent(this.cCondDomicilie)).addGap(18, 18, 18)).addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(this.cCondModePaie).addGap(8, 8, 8))).addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel47).addComponent(this.tBanque, -2, 25, -2)).addComponent(this.cCondBanque)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel38).addComponent(this.tZoneOrigine, -2, 25, -2)).addComponent(this.cCondOrigine))).addGroup(jPanel1Layout.createSequentialGroup().addGap(1, 1, 1).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tModePaiement, -2, 25, -2).addComponent(this.jLabel46))))).addComponent(this.cEnConges)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tTypeContrat, -2, 25, -2).addGroup(jPanel1Layout.createSequentialGroup().addGap(7, 7, 7).addComponent(this.jLabel30)).addComponent(this.cCondTypeContrat)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cDetacheCNAM, Alignment.TRAILING).addComponent(this.cDetacheCNSS)).addComponent(this.cCondCNSS).addComponent(this.cCondCNAM).addComponent(this.cExonoreITS)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jSeparator6, -2, 10, -2).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(6, 6, 6).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel12).addComponent(this.tSituationFam, -2, 25, -2))).addGroup(jPanel1Layout.createSequentialGroup().addGap(6, 6, 6).addComponent(this.cCondSitFam)).addGroup(jPanel1Layout.createSequentialGroup().addGap(4, 4, 4).addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.cCondSexe).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tSexe, -2, 25, -2).addComponent(this.jLabel11)))))).addComponent(this.cCondITS)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.resultButton, -2, 31, -2).addContainerGap(-1, 32767)));
      this.jTabbedPane1.addTab("Filtre", this.jPanel1);
      this.jPanel4.setForeground(new Color(0, 102, 51));
      this.jLabel31.setText("Type contrat");
      this.tMAJTypeContrat.setModel(new DefaultComboBoxModel(new String[]{"CDI", "CDD"}));
      this.cMAJDetacheCNSS.setText("Detach\u00e9 CNSS");
      this.cMAJDetacheCNAM.setText("Detach\u00e9 CNAM");
      this.cMAJExonoreITS.setText("Exonor\u00e9 ITS");
      this.tMAJSexe.setModel(new DefaultComboBoxModel(new String[]{"F", "M"}));
      this.jLabel13.setText("Sexe");
      this.tMAJSituationFam.setModel(new DefaultComboBoxModel(new String[]{"C", "M", "D", "V"}));
      this.jLabel14.setText("Situation familiale");
      this.jLabel39.setText("Zone d'origine");
      this.tMAJZoneOrigine.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMAJBanque.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.jLabel48.setText("Banque");
      this.jLabel49.setText("Mode de paye");
      this.tMAJModePaiement.setModel(new DefaultComboBoxModel(new String[]{"Esp\u00e8ce", "Ch\u00e8que", "Virement"}));
      this.cMAJDomicilie.setText("Domicil\u00e9");
      this.tMAJCategorie.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.jLabel36.setText("Cat\u00e9gorie");
      this.jLabel37.setText("Poste");
      this.tMAJPoste.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMAJDepartement.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.jLabel40.setText("Departement");
      this.cMAJEnConges.setText("En cong\u00e9s");
      this.cMAJActif.setText("Actif");
      this.jLabel27.setText("Heures/semaine");
      this.tMAJHeureSemaine.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tMAJHeureSemaine.setHorizontalAlignment(4);
      this.tMAJHeureSemaine.setFont(new Font("Tahoma", 0, 10));
      this.tMAJHeureSemaine.addFocusListener(new 2(this));
      this.jLabel28.setText("Nb mois de pr\u00e9avis");
      this.tMAJNbMoisPreavis.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tMAJNbMoisPreavis.setHorizontalAlignment(4);
      this.tMAJNbMoisPreavis.setFont(new Font("Tahoma", 0, 10));
      this.tMAJNbMoisPreavis.addFocusListener(new 3(this));
      this.jLabel32.setText("Fin  de contrat");
      this.tMAJDateFinContrat.setDateFormatString("dd/MM/yyyy");
      this.tMAJDateFinContrat.setFont(new Font("Tahoma", 0, 10));
      this.cMAJAvancmentAutoCat.setText("Avancement auto sur");
      this.cMAJAvancmentAutoCat.addActionListener(new 4(this));
      this.MAJtNbAnneesCat.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.MAJtNbAnneesCat.setHorizontalAlignment(4);
      this.MAJtNbAnneesCat.setFont(new Font("Tahoma", 0, 10));
      this.MAJtNbAnneesCat.addFocusListener(new 5(this));
      this.validerMajButton.setFont(new Font("Segoe UI Light", 0, 13));
      this.validerMajButton.setText("Valider");
      this.validerMajButton.addActionListener(new 6(this));
      this.cAppSBCatgeorie.setText("App. SB");
      this.cMAJActifOK.setBackground(new Color(0, 102, 0));
      this.cMAJEnCongesOK.setBackground(new Color(0, 102, 0));
      this.cMAJPosteOK.setBackground(new Color(0, 102, 0));
      this.cMAJDepartementOK1.setBackground(new Color(0, 102, 0));
      this.cMAJCategorieOK.setBackground(new Color(0, 102, 0));
      this.cMAJDomicilieOK.setBackground(new Color(0, 102, 0));
      this.cMAJBanqueOK.setBackground(new Color(0, 102, 0));
      this.cMAJDateFinContratOK.setBackground(new Color(0, 102, 0));
      this.cMAJNbMoisPreavisOK.setBackground(new Color(0, 102, 0));
      this.cMAJExonoreITSOK.setBackground(new Color(0, 102, 0));
      this.cMAJSexeOK.setBackground(new Color(0, 102, 0));
      this.cMAJZoneOrigineOK.setBackground(new Color(0, 102, 0));
      this.cMAJSituationFamOK.setBackground(new Color(0, 102, 0));
      this.cMAJDetacheCNSSOK.setBackground(new Color(0, 102, 0));
      this.cMAJAvancmentAutoCatOK.setBackground(new Color(0, 102, 0));
      this.cMAJHeureSemaineOK.setBackground(new Color(0, 102, 0));
      this.cMAJModePaiementOK.setBackground(new Color(0, 102, 0));
      this.cMAJDetacheCNAMOK.setBackground(new Color(0, 102, 0));
      this.cMAJTypeContratOK.setBackground(new Color(0, 102, 0));
      GroupLayout jPanel4Layout = new GroupLayout(this.jPanel4);
      this.jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel49, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel48, Alignment.LEADING, -2, 89, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.tMAJBanque, 0, -1, 32767).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cMAJBanqueOK)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.tMAJModePaiement, -2, 151, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cMAJModePaiementOK).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.cMAJDomicilie).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cMAJDomicilieOK)))).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel36, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel37, Alignment.LEADING, -1, -1, 32767)).addComponent(this.jLabel31)).addGap(31, 31, 31)).addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addComponent(this.jLabel40, -2, 89, -2).addPreferredGap(ComponentPlacement.RELATED))).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.cMAJAvancmentAutoCat, -2, 140, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.MAJtNbAnneesCat, -2, 52, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cMAJAvancmentAutoCatOK).addGap(0, 0, 32767)).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.tMAJTypeContrat, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cMAJTypeContratOK).addGap(19, 19, 19).addComponent(this.jLabel32, -2, 73, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tMAJDateFinContrat, -2, 0, 32767)).addComponent(this.tMAJPoste, 0, -1, 32767).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.tMAJCategorie, -2, 205, -2).addGap(18, 18, 18).addComponent(this.cAppSBCatgeorie).addGap(0, 0, 32767)).addComponent(this.tMAJDepartement, 0, -1, 32767).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.cMAJActif).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cMAJActifOK).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.cMAJEnConges).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cMAJEnCongesOK))).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cMAJDepartementOK1).addComponent(this.cMAJPosteOK).addComponent(this.cMAJCategorieOK).addComponent(this.cMAJDateFinContratOK))))).addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.validerMajButton, -2, 85, -2)).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel14, -2, 92, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tMAJSituationFam, -2, 61, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cMAJSituationFamOK).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel13).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tMAJSexe, -2, 52, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel27, -2, 89, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tMAJHeureSemaine, -2, 56, -2).addGap(6, 6, 6).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addComponent(this.cMAJHeureSemaineOK).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel28, -2, 97, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tMAJNbMoisPreavis, -2, 62, -2)).addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.cMAJDetacheCNAM).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cMAJDetacheCNAMOK).addGap(18, 18, 18).addComponent(this.cMAJExonoreITS)))).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.cMAJDetacheCNSS).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cMAJDetacheCNSSOK).addGap(0, 0, 32767)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel39, -2, 92, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tMAJZoneOrigine, 0, -1, 32767))).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cMAJNbMoisPreavisOK).addComponent(this.cMAJExonoreITSOK).addComponent(this.cMAJSexeOK).addComponent(this.cMAJZoneOrigineOK)))).addGap(668, 668, 668)));
      jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGap(31, 31, 31).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cMAJEnConges).addComponent(this.cMAJActif).addComponent(this.cMAJActifOK, Alignment.TRAILING)).addComponent(this.cMAJEnCongesOK)).addGap(18, 18, 18).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING, false).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tMAJDepartement, -2, 25, -2).addComponent(this.jLabel40)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel37).addComponent(this.tMAJPoste, -2, 25, -2))).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.cMAJDepartementOK1).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.cMAJPosteOK))).addGap(6, 6, 6).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel36).addComponent(this.tMAJCategorie, -2, 25, -2).addComponent(this.cAppSBCatgeorie, -2, 15, -2)).addComponent(this.cMAJCategorieOK)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.cMAJAvancmentAutoCat).addComponent(this.MAJtNbAnneesCat, -2, -1, -2)).addComponent(this.cMAJAvancmentAutoCatOK)).addGap(7, 7, 7).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel49).addComponent(this.tMAJModePaiement, -2, 25, -2)).addComponent(this.cMAJDomicilie).addComponent(this.cMAJDomicilieOK)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.cMAJModePaiementOK).addGap(3, 3, 3))).addGap(4, 4, 4).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel48).addComponent(this.tMAJBanque, -2, 25, -2))).addComponent(this.cMAJBanqueOK)).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGap(11, 11, 11).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel31).addComponent(this.tMAJTypeContrat, -2, -1, -2))).addGroup(jPanel4Layout.createSequentialGroup().addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel32).addComponent(this.tMAJDateFinContrat, -2, -1, -2)).addComponent(this.cMAJDateFinContratOK, Alignment.TRAILING))).addGroup(jPanel4Layout.createSequentialGroup().addGap(11, 11, 11).addComponent(this.cMAJTypeContratOK))).addGap(5, 5, 5).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel27).addComponent(this.tMAJHeureSemaine, -2, -1, -2)).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel28).addComponent(this.tMAJNbMoisPreavis, -2, -1, -2)).addComponent(this.cMAJNbMoisPreavisOK).addComponent(this.cMAJHeureSemaineOK)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cMAJDetacheCNAM).addComponent(this.cMAJDetacheCNSS).addComponent(this.cMAJExonoreITS).addComponent(this.cMAJExonoreITSOK).addComponent(this.cMAJDetacheCNSSOK, Alignment.TRAILING)).addComponent(this.cMAJDetacheCNAMOK)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tMAJSexe, -2, 25, -2).addComponent(this.jLabel13).addComponent(this.jLabel14).addComponent(this.tMAJSituationFam, -2, 25, -2).addComponent(this.cMAJSituationFamOK)).addComponent(this.cMAJSexeOK)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.cMAJZoneOrigineOK).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel39).addComponent(this.tMAJZoneOrigine, -2, 25, -2))).addPreferredGap(ComponentPlacement.RELATED, 30, 32767).addComponent(this.validerMajButton, -2, 35, -2).addContainerGap(-1, 32767)));
      this.jTabbedPane1.addTab("Mise \u00e0 jour", this.jPanel4);
      this.jPanel12.setBorder(BorderFactory.createEtchedBorder());
      this.jLabel90.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel90.setText("DIM");
      this.jLabel91.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel91.setForeground(new Color(0, 102, 0));
      this.jLabel91.setText("Debut de la semaine");
      this.jLabel92.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel92.setForeground(new Color(255, 153, 0));
      this.jLabel92.setText("Fin de la semaine");
      this.jLabel93.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel93.setForeground(new Color(204, 0, 0));
      this.jLabel93.setText("Week-end");
      this.jLabel137.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel137.setText("LUN");
      this.jLabel150.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel150.setText("MAR");
      this.jLabel151.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel151.setText("MER");
      this.jLabel152.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel152.setText("JEU");
      this.jLabel153.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel153.setText("VEN");
      this.jLabel154.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel154.setText("SAM");
      GroupLayout jPanel12Layout = new GroupLayout(this.jPanel12);
      this.jPanel12.setLayout(jPanel12Layout);
      jPanel12Layout.setHorizontalGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel12Layout.createSequentialGroup().addContainerGap(-1, 32767).addGroup(jPanel12Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel92, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel91, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel93, -1, -1, 32767)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING, false).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.cDIMwe, -2, 20, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767)).addGroup(jPanel12Layout.createSequentialGroup().addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel90, -2, 32, -2).addComponent(this.cDIMEnd, -2, 20, -2).addComponent(this.cDIMBegin, -2, 20, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cLUNEnd, -2, 20, -2).addComponent(this.cLUNBegin, -2, 20, -2).addComponent(this.cLUNwe, -2, 20, -2).addComponent(this.jLabel137, -2, 35, -2)))).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel150, -2, 35, -2).addComponent(this.cMAREnd, -2, 20, -2).addComponent(this.cMARBegin, -2, 20, -2).addComponent(this.cMARwe, -2, 20, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.jLabel151, -2, 32, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jLabel152, -2, 32, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel153, -2, 32, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel154, -2, 32, -2)).addGroup(jPanel12Layout.createSequentialGroup().addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cMEREnd, -2, 20, -2).addComponent(this.cMERBegin, -2, 20, -2).addComponent(this.cMERwe, -2, 20, -2)).addGap(18, 18, 18).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cJEUEnd, -2, 20, -2).addComponent(this.cJEUBegin, -2, 20, -2).addComponent(this.cJEUwe, -2, 20, -2)).addGap(18, 18, 18).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cVENEnd, -2, 20, -2).addComponent(this.cVENBegin, -2, 20, -2).addComponent(this.cVENwe, -2, 20, -2)).addGap(18, 18, 18).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cSAMEnd, -2, 20, -2).addComponent(this.cSAMBegin, -2, 20, -2).addComponent(this.cSAMwe, -2, 20, -2))))));
      jPanel12Layout.setVerticalGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel12Layout.createSequentialGroup().addGap(34, 34, 34).addGroup(jPanel12Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel90).addComponent(this.jLabel137).addComponent(this.jLabel150).addComponent(this.jLabel151).addComponent(this.jLabel152).addComponent(this.jLabel153).addComponent(this.jLabel154)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.cMARBegin).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cMAREnd).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cMARwe)).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.cMERBegin).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cMEREnd).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cMERwe)).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.cJEUBegin).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cJEUEnd).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cJEUwe)).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.cVENBegin).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cVENEnd).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cVENwe)).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.cSAMBegin).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cSAMEnd).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cSAMwe)).addGroup(jPanel12Layout.createSequentialGroup().addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cDIMBegin).addComponent(this.jLabel91)).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel12Layout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.jLabel92)).addGroup(jPanel12Layout.createSequentialGroup().addGap(1, 1, 1).addComponent(this.cDIMEnd))).addGap(18, 18, 18).addComponent(this.jLabel93)).addGroup(jPanel12Layout.createSequentialGroup().addComponent(this.cLUNBegin).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cLUNEnd).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel12Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cDIMwe).addComponent(this.cLUNwe)))).addContainerGap(76, 32767)));
      this.validerMajButton1.setFont(new Font("Segoe UI Light", 0, 13));
      this.validerMajButton1.setText("Valider");
      this.validerMajButton1.addActionListener(new 7(this));
      GroupLayout jPanel7Layout = new GroupLayout(this.jPanel7);
      this.jPanel7.setLayout(jPanel7Layout);
      jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel12, -2, -1, -2).addContainerGap(-1, 32767)).addGroup(Alignment.TRAILING, jPanel7Layout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.validerMajButton1, -2, 85, -2).addContainerGap()));
      jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel12, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.validerMajButton1, -2, 35, -2).addContainerGap(248, 32767)));
      this.jTabbedPane1.addTab("Config. de la semaine", this.jPanel7);
      this.jPanel3.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      this.jLabel67.setText("Rubrique");
      this.tRubrique.addActionListener(new 8(this));
      this.cFixe.setText("Fixe");
      this.cFixe.setHorizontalAlignment(4);
      this.cFixe.setHorizontalTextPosition(4);
      this.jLabel66.setText("Motif");
      this.tMotif.addActionListener(new 9(this));
      this.jLabel68.setText("Base");
      this.tBase.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("0"))));
      this.tBase.setHorizontalAlignment(4);
      this.jLabel70.setText("Nombre");
      this.tNombre.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("0"))));
      this.tNombre.setHorizontalAlignment(4);
      this.cSuppRubrique.setForeground(new Color(204, 0, 0));
      this.cSuppRubrique.setText("Suppression");
      this.cSuppRubrique.setHorizontalAlignment(4);
      this.cSuppRubrique.setHorizontalTextPosition(4);
      this.cSuppRubrique.addActionListener(new 10(this));
      this.validerRubriquePaieButton.setFont(new Font("Segoe UI Light", 0, 13));
      this.validerRubriquePaieButton.setText("Valider");
      this.validerRubriquePaieButton.addActionListener(new 11(this));
      this.tNjt.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00"))));
      this.tNjt.setHorizontalAlignment(4);
      this.tNjt.addActionListener(new 12(this));
      this.tNjt.addFocusListener(new 13(this));
      this.jLabel73.setHorizontalAlignment(4);
      this.jLabel73.setText("NJT du Motif selectionn\u00e9");
      this.validerNjtButton.setFont(new Font("Segoe UI Light", 0, 13));
      this.validerNjtButton.setText("Valider");
      this.validerNjtButton.addActionListener(new 14(this));
      this.jLabel69.setText("Message paie courante");
      this.tNoteSurBulletin.setColumns(20);
      this.tNoteSurBulletin.setFont(new Font("Arial", 0, 10));
      this.tNoteSurBulletin.setRows(5);
      this.jScrollPane4.setViewportView(this.tNoteSurBulletin);
      this.validerMsgPaieCouranteButton.setFont(new Font("Segoe UI Light", 0, 13));
      this.validerMsgPaieCouranteButton.setText("Valider");
      this.validerMsgPaieCouranteButton.addActionListener(new 15(this));
      GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
      this.jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel69, -2, 113, -2).addGap(0, 0, 32767)).addGroup(Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel70, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel68, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel67, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel66, Alignment.LEADING, -1, -1, 32767)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.tRubrique, -2, 320, -2)).addComponent(this.tMotif, -2, 191, -2)).addGap(21, 21, 21)).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.tNombre, Alignment.LEADING, -2, 191, -2).addComponent(this.tBase, Alignment.LEADING, -2, 191, -2).addComponent(this.cSuppRubrique, Alignment.LEADING, -2, 106, -2)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addComponent(this.cFixe, -2, 54, -2).addGap(21, 21, 21)).addComponent(this.validerRubriquePaieButton, Alignment.TRAILING, -2, 83, -2))))).addComponent(this.jScrollPane4, Alignment.LEADING).addComponent(this.jSeparator3, Alignment.LEADING).addComponent(this.jSeparator4).addComponent(this.jSeparator5).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel73, -2, 217, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tNjt, -2, 63, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.validerNjtButton, -2, 85, -2)).addGroup(jPanel3Layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.validerMsgPaieCouranteButton, -2, 90, -2))).addGap(784, 784, 784)))));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel66).addComponent(this.tMotif, Alignment.TRAILING, -2, 25, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jSeparator5, -2, 10, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel67).addComponent(this.tRubrique, -2, 25, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tBase, -2, -1, -2).addComponent(this.jLabel68).addComponent(this.cFixe)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tNombre, -2, -1, -2).addComponent(this.jLabel70, -2, 17, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.validerRubriquePaieButton, -2, 35, -2).addComponent(this.cSuppRubrique)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jSeparator3, -2, 10, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.validerNjtButton, -2, 35, -2).addComponent(this.tNjt, -2, -1, -2).addComponent(this.jLabel73)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jSeparator4, -2, 10, -2).addGap(1, 1, 1).addComponent(this.jLabel69).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane4, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.validerMsgPaieCouranteButton, -2, 35, -2).addContainerGap(85, 32767)));
      this.jTabbedPane1.addTab("Paie", this.jPanel3);
      this.jLabel74.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel74.setHorizontalAlignment(2);
      this.jLabel74.setText("Date d\u00e9part");
      this.tDateDepart.setDateFormatString("dd/MM/yyyy");
      this.jLabel75.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel75.setText("Note");
      this.tNoteConges.setColumns(20);
      this.tNoteConges.setFont(new Font("Arial", 0, 10));
      this.tNoteConges.setRows(5);
      this.jScrollPane5.setViewportView(this.tNoteConges);
      this.jPanel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "R\u00e9muneration"));
      this.cDroitsConges.setText("Droits de cong\u00e9s");
      this.cIndPreavis.setText("Indemnit\u00e9 de pr\u00e9avis");
      this.cIndLicenciement.setText("Indemnit\u00e9 de licenci\u00e9ment");
      this.cIndLicenciementCollectif.setText("Indemnit\u00e9 de licenci\u00e9ment collectif");
      this.cIndLicenciementCollectif.addActionListener(new 16(this));
      this.cSolderRetenues.setText("Solder toutes les retenues \u00e0 ech\u00e9ances");
      this.cIndRetraite.setText("Indemnit\u00e9 de d\u00e9part \u00e0 la retraite");
      GroupLayout jPanel6Layout = new GroupLayout(this.jPanel6);
      this.jPanel6.setLayout(jPanel6Layout);
      jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cDroitsConges).addComponent(this.cIndPreavis).addComponent(this.cIndLicenciement).addComponent(this.cIndLicenciementCollectif).addComponent(this.cIndRetraite).addComponent(this.cSolderRetenues, -2, 329, -2)).addContainerGap(-1, 32767)));
      jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.cDroitsConges).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cIndPreavis).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cIndLicenciement).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cIndLicenciementCollectif).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cIndRetraite).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cSolderRetenues)));
      this.validerDecompteButton.setFont(new Font("Segoe UI Light", 0, 13));
      this.validerDecompteButton.setText("Valider");
      this.validerDecompteButton.addActionListener(new 17(this));
      this.jLabel95.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel95.setText("Reprise");
      this.tDateReprise.setDateFormatString("dd/MM/yyyy");
      this.tDecompteType.setFont(new Font("Segoe UI Light", 0, 13));
      this.tDecompteType.setModel(new DefaultComboBoxModel(new String[]{"D\u00e9compte de cong\u00e9s", "D\u00e9compte de sortie"}));
      this.tDecompteType.addActionListener(new 18(this));
      this.tMotifMouvement.setModel(new DefaultComboBoxModel(new String[]{"MUTATION (-)", "LICENCIEMENT", "DEMISSION", "RETRAITE", "DECES"}));
      this.tMotifMouvement.setEnabled(false);
      this.tMotifMouvement.addActionListener(new 19(this));
      this.jLabel76.setFont(new Font("Segoe UI Light", 0, 13));
      this.jLabel76.setHorizontalAlignment(2);
      this.jLabel76.setText("Motif");
      GroupLayout jPanel5Layout = new GroupLayout(this.jPanel5);
      this.jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.jPanel6, Alignment.LEADING, -1, -1, 32767).addGroup(jPanel5Layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.validerDecompteButton, -2, 81, -2).addGap(31, 31, 31)).addGroup(Alignment.LEADING, jPanel5Layout.createSequentialGroup().addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel74, -2, 74, -2).addComponent(this.jLabel75, -2, 74, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jScrollPane5).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.tDateDepart, -2, 120, -2).addGap(18, 18, 18).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.jLabel76, -2, 36, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tMotifMouvement, -2, 180, -2)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.jLabel95, -1, -1, 32767).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tDateReprise, -2, 120, -2).addGap(29, 29, 29)))))).addGroup(Alignment.LEADING, jPanel5Layout.createSequentialGroup().addComponent(this.tDecompteType, -2, 179, -2).addGap(0, 0, 32767))).addGap(627, 627, 627)));
      jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap(-1, 32767).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tMotifMouvement, Alignment.TRAILING, -2, 25, -2).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tDecompteType, -2, 25, -2).addComponent(this.jLabel76))).addGap(18, 18, 18).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tDateDepart, Alignment.TRAILING, -2, -1, -2).addComponent(this.tDateReprise, Alignment.TRAILING, -2, -1, -2).addComponent(this.jLabel95)).addComponent(this.jLabel74)).addGap(34, 34, 34).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel5Layout.createSequentialGroup().addComponent(this.jScrollPane5, -2, -1, -2).addGap(18, 18, 18)).addGroup(Alignment.TRAILING, jPanel5Layout.createSequentialGroup().addComponent(this.jLabel75).addGap(50, 50, 50))).addComponent(this.jPanel6, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.validerDecompteButton, -2, 37, -2).addGap(103, 103, 103)));
      this.jTabbedPane1.addTab("D\u00e9compte Cong\u00e9s/Sortie", this.jPanel5);
      this.jPanel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Resultat de recherche", 0, 0, new Font("Segoe UI Light", 0, 13)));
      this.jButton2.setFont(new Font("Segoe UI Light", 0, 13));
      this.jButton2.setText("Actualiser");
      this.jButton2.addActionListener(new 20(this));
      this.resultTable.setModel(new DefaultTableModel(new Object[0][], (Object[])(new String[0])));
      this.resultTable.setAutoResizeMode(4);
      this.resultTable.addMouseListener(new 21(this));
      this.jScrollPane7.setViewportView(this.resultTable);
      this.jLabel7.setText("salari\u00e9(s) trouv\u00e9(s)");
      this.tNbResults.setEditable(false);
      this.tNbResults.setBorder(BorderFactory.createEtchedBorder());
      this.tNbResults.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance())));
      this.tNbResults.setHorizontalAlignment(4);
      this.tNbResults.setFont(new Font("Tahoma", 1, 12));
      this.tNbResults.addActionListener(new 22(this));
      this.tNbResults.addFocusListener(new 23(this));
      this.cCheckAll.setText("Tout cocher");
      this.cCheckAll.addActionListener(new 24(this));
      GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
      this.jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jScrollPane7, -2, 684, -2).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.tNbResults, -2, 49, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jLabel7, -2, 111, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cCheckAll, -2, 189, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.progressBar, -1, -1, 32767).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jButton2, -2, 101, -2))).addContainerGap(-1, 32767)));
      jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jScrollPane7, -2, 470, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jButton2, -1, -1, 32767).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGap(0, 0, 32767).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tNbResults, -2, 26, -2).addComponent(this.jLabel7).addComponent(this.cCheckAll)).addComponent(this.progressBar, Alignment.TRAILING, -2, 34, -2)))).addContainerGap()));
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(-1, 32767).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.msgInfoLabel, -1, -1, 32767).addComponent(this.jTabbedPane1, -2, 470, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.msgErrLabel, Alignment.TRAILING, -1, -1, 32767).addComponent(this.jPanel2, Alignment.TRAILING, -1, -1, 32767))));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(14, 14, 14).addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jPanel2, -1, -1, 32767).addGroup(layout.createSequentialGroup().addComponent(this.jTabbedPane1, -2, 575, -2).addGap(0, 4, 32767))).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.msgErrLabel).addComponent(this.msgInfoLabel)).addContainerGap()));
      this.pack();
   }

   private void tNbResultsFocusLost(FocusEvent var1) {
   }

   private void tNbResultsActionPerformed(ActionEvent var1) {
   }

   private void resultTableMouseClicked(MouseEvent var1) {
   }

   private void jButton2ActionPerformed(ActionEvent var1) {
      this.refresh();
   }

   private void validerDecompteButtonActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous ce d\u00e9compte collectif ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 25(this);
         t.start();
      }

   }

   private void cIndLicenciementCollectifActionPerformed(ActionEvent var1) {
   }

   private void validerMsgPaieCouranteButtonActionPerformed(ActionEvent var1) {
      this.msgInfoLabel.setText(".");
      this.msgErrLabel.setText(".");
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la validation du message ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 26(this);
         t.start();
      }

   }

   private void validerNjtButtonActionPerformed(ActionEvent var1) {
      this.msgInfoLabel.setText(".");
      this.msgErrLabel.setText(".");
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la validation du NJT ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 27(this);
         t.start();
      }

   }

   private void tNjtFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tNjt);
   }

   private void tNjtActionPerformed(ActionEvent var1) {
   }

   private void validerRubriquePaieButtonActionPerformed(ActionEvent var1) {
      this.msgInfoLabel.setText(".");
      this.msgErrLabel.setText(".");
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la validation de cette paie ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 28(this);
         t.start();
      }

   }

   private void cSuppRubriqueActionPerformed(ActionEvent var1) {
   }

   private void tMotifActionPerformed(ActionEvent var1) {
   }

   private void tRubriqueActionPerformed(ActionEvent var1) {
      this.setEdit((Rubrique)this.tRubrique.getSelectedItem());
   }

   private void resultButtonActionPerformed(ActionEvent var1) {
      Thread t = new 29(this);
      t.start();
   }

   private void cCheckAllActionPerformed(ActionEvent var1) {
      for(int i = 0; i < this.resultTable.getRowCount(); ++i) {
         if (this.cCheckAll.isSelected()) {
            ((ModelClass.tmEmployequery)this.resultTable.getModel()).setValueAt(true, i, 0);
         } else {
            ((ModelClass.tmEmployequery)this.resultTable.getModel()).setValueAt(false, i, 0);
         }
      }

   }

   private void validerMajButtonActionPerformed(ActionEvent var1) {
      this.msgInfoLabel.setText(".");
      this.msgErrLabel.setText(".");
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la sauvegarde des modifications ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 30(this);
         t.start();
      }

   }

   private void MAJtNbAnneesCatFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.MAJtNbAnneesCat);
   }

   private void cMAJAvancmentAutoCatActionPerformed(ActionEvent var1) {
   }

   private void tMAJNbMoisPreavisFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tMAJNbMoisPreavis);
   }

   private void tMAJHeureSemaineFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tMAJHeureSemaine);
   }

   private void tDecompteTypeActionPerformed(ActionEvent var1) {
      if (this.tDecompteType.getSelectedItem().toString().compareTo("D\u00e9compte de cong\u00e9s") == 0) {
         this.tDateReprise.setEnabled(true);
         this.tMotifMouvement.setEnabled(false);
      } else {
         this.tDateReprise.setEnabled(false);
         this.tMotifMouvement.setEnabled(true);
      }

   }

   private void tMotifMouvementActionPerformed(ActionEvent var1) {
   }

   private void validerMajButton1ActionPerformed(ActionEvent var1) {
      this.msgInfoLabel.setText(".");
      this.msgErrLabel.setText(".");
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la sauvegarde des modifications ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 31(this);
         t.start();
      }

   }

   private void setEdit(Rubrique var1) {
      if (rubrique != null) {
         if (rubrique.isNombreAuto()) {
            this.tNombre.setEditable(false);
            this.tNombre.setBackground(Color.LIGHT_GRAY);
         } else {
            this.tNombre.setEditable(true);
            this.tNombre.setBackground(Color.WHITE);
         }

         if (rubrique.isBaseAuto()) {
            this.tBase.setEditable(false);
            this.tBase.setBackground(Color.LIGHT_GRAY);
         } else {
            this.tBase.setEditable(true);
            this.tBase.setBackground(Color.WHITE);
         }
      }

   }

   private void afficherResultat() {
      this.menu.mc.getClass();
      JTable var10000 = this.resultTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmEmployequery(var10003));
      this.resultTable.setGridColor(new Color(0, 102, 153));
      this.resultTable.setSelectionBackground(new Color(0, 102, 153));
      this.resultTable.setShowGrid(false);
      SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
      this.msgErrLabel.setText(".");
      Date periodeCourante = this.menu.paramsGen.getPeriodeCourante();
      this.dataList = this.menu.employeFrame.dataListInit;
      this.dataList = (List)this.dataList.stream().filter((var1x) -> (!this.cCondActif.isSelected() || var1x.isActif() == this.cActif.isSelected()) && (!this.cCondBanque.isSelected() || var1x.getBanque().getId() == ((Banque)this.tBanque.getSelectedItem()).getId()) && (!this.cCondCNAM.isSelected() || var1x.isDetacheCnam() == this.cDetacheCNAM.isSelected()) && (!this.cCondCNSS.isSelected() || var1x.isDetacheCnss() == this.cDetacheCNSS.isSelected()) && (!this.cCondCategorie.isSelected() || var1x.getGrillesalairebase().getCategorie().equals(((Grillesalairebase)this.tCategorie.getSelectedItem()).getCategorie())) && (!this.cCondDepartement.isSelected() || var1x.getDepartement().getId() == ((Departement)this.tDepartement.getSelectedItem()).getId()) && (!this.cCondDomicilie.isSelected() || var1x.isDomicilie() == this.cDomicilie.isSelected()) && (!this.cCondEmbauche.isSelected() || var1x.getDateEmbauche().after(this.tEmbaucheDu.getDate()) && var1x.getDateEmbauche().before(this.tEmbaucheAu.getDate())) && (!this.cCondEnConges.isSelected() || var1x.isEnConge() == this.cEnConges.isSelected()) && (!this.cCondEnDebauche.isSelected() || var1x.isEnDebauche() == this.cEnDebauche.isSelected()) && (!this.cCondID.isSelected() || (long)var1x.getId() >= ((Number)this.tIdDu.getValue()).longValue() && (long)var1x.getId() <= ((Number)this.tIdAu.getValue()).longValue()) && (!this.cCondITS.isSelected() || var1x.isExonoreIts() == this.cExonoreITS.isSelected()) && (!this.cCondSitFam.isSelected() || var1x.getModePaiement().equals(this.tModePaiement.getSelectedItem().toString())) && (!this.cCondOrigine.isSelected() || var1x.getOrigines().getId() == ((Origines)this.tZoneOrigine.getSelectedItem()).getId()) && (!this.cCondPoste.isSelected() || var1x.getPoste().getId() == ((Poste)this.tPoste.getSelectedItem()).getId()) && (!this.cCondSitFam.isSelected() || var1x.getSituationFamiliale().equals(this.tSituationFam.getSelectedItem().toString())) && (!this.cCondSexe.isSelected() || var1x.getSexe().equals(this.tSexe.getSelectedItem().toString())) && (!this.cCondTypeContrat.isSelected() || var1x.getTypeContrat().equals(this.tTypeContrat.getSelectedItem().toString())) && (!this.cCondSortie.isSelected() || var1x.getDateDebauche().after(this.tSortieDu.getDate()) && var1x.getDateDebauche().before(this.tSortieAu.getDate()))).sorted(Comparator.comparing(Employe::getId)).collect(Collectors.toList());
      this.tNbResults.setValue(this.dataList.size());
      Motif motifSN = this.menu.motifSN;

      for(Employe rs : this.dataList) {
         String dteSortie = rs.getDateDebauche() != null ? df2.format(rs.getDateDebauche()) : "-";
         String departement = rs.getDepartement() != null ? rs.getDepartement().getNom() : "-";
         String poste = rs.getPoste() != null ? rs.getPoste().getNom() : "-";
         String categorie = rs.getGrillesalairebase() != null ? rs.getGrillesalairebase().getCategorie() : "-";
         String banque = rs.getBanque() != null && rs.getBanque().getId() > 0 ? rs.getBanque().getNom() : "-";
         String origine = rs.getOrigines() != null ? rs.getOrigines().getLibelle() : "-";
         Njtsalarie njts = this.menu.pc.njtSalarieById(rs, motifSN, periodeCourante);
         double njt = njts != null ? njts.getNjt() : (double)0.0F;
         ((ModelClass.tmEmployequery)this.resultTable.getModel()).addRow(rs.isActif(), rs.isEnConge(), rs.getId(), rs.getNom(), rs.getSexe(), rs.getNationalite(), origine, rs.getSituationFamiliale(), df2.format(rs.getDateEmbauche()), njt, departement, poste, categorie, rs.isDetacheCnss(), rs.isDetacheCnam(), rs.isExonoreIts(), rs.getModePaiement(), banque, rs.isDomicilie(), rs.getTypeContrat(), dteSortie, rs.isEnDebauche());
      }

      this.resultTable.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.resultTable.getColumnModel().getColumn(1).setPreferredWidth(5);
      this.resultTable.getColumnModel().getColumn(2).setPreferredWidth(5);
      this.resultTable.getColumnModel().getColumn(3).setPreferredWidth(50);
      this.resultTable.getColumnModel().getColumn(4).setPreferredWidth(200);
      this.resultTable.setRowHeight(30);
      this.menu.mc.getClass();
      this.cCheckAll.setSelected(false);
   }
}
