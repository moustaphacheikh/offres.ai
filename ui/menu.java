package com.mccmr.ui;

import com.mccmr.entity.Activite;
import com.mccmr.entity.Banque;
import com.mccmr.entity.Departement;
import com.mccmr.entity.Direction;
import com.mccmr.entity.Directiongeneral;
import com.mccmr.entity.Document;
import com.mccmr.entity.Grillesalairebase;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Origines;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Paramgen;
import com.mccmr.entity.Poste;
import com.mccmr.entity.Rubrique;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.entity.Statut;
import com.mccmr.entity.Utilisateurs;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.ComboBoxFilterDecorator;
import com.mccmr.util.CustomComboRenderer;
import com.mccmr.util.FonctionsPaie;
import com.mccmr.util.GeneralLib;
import com.mccmr.util.HibernateUtil;
import com.mccmr.util.Keyvaluedouble;
import com.mccmr.util.Licencekey;
import com.mccmr.util.ModelClass;
import com.mccmr.util.NombreEnLettres;
import com.mccmr.util.PaieClass;
import com.mccmr.util.ReadExcel;
import com.mccmr.util.WriteExcel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.hibernate.dialect.Dialect;
import org.jdesktop.layout.GroupLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class menu extends JFrame {
   public boolean demo;
   public boolean grantNewVersionAccess = true;
   public boolean newVersionIsFree = true;
   public String appVersion = null;
   public boolean firstRefresh = false;
   public EntityManager entityManager = HibernateUtil.getEntityManager();
   public Dialect dialect;
   public GeneralLib gl;
   public PaieClass pc;
   public ModelClass mc;
   public FonctionsPaie fx;
   public ReadExcel excel;
   public WriteExcel we;
   public NombreEnLettres nl = new NombreEnLettres();
   public NumberFormat nf;
   public int limiteSalarie;
   public Paramgen paramsGen;
   public DateFormat df;
   public DateFormat pf;
   public DateFormat spf;
   public DateFormat fdf;
   public DateFormat timeformat;
   public DateFormat filePeriodeDF;
   public DateFormat periodeShort;
   public DateFormat periodeExo;
   public Motif motifSN;
   public Motif motifCNG;
   public Utilisateurs user;
   public String userName;
   static int result = 0;
   public password passwordIF;
   public parametres parametresIF;
   public structures stricturesIF;
   public paie paiegeneraleFrame;
   public cloture clotureFrame;
   public fileImport fileImportFrame;
   public compta comptaFrame;
   public bulletin bulletinpaieFrame;
   public EmployequeryFrame employequeryFrame;
   public virements virementsFrame;
   public declarationsAnnuelles declarationsAnnuelleFrame;
   public declarations declarationsFrame;
   public etats etatsFrame;
   public etatsCumul etatscumulesFrame;
   public securite utilisateursFrame;
   public fx fxFrame;
   public rubriques rubriqueFrame;
   public salarys employeFrame;
   public statistiques statistiqueFrame;
   public att pointageFileImportFrame;
   public repport repport;
   public final ThisLocalizedWeek frenchWeek;
   private JButton btnMaskDashboard;
   private JButton btnMenu;
   private JButton btnMenuCalculPaie;
   private JButton btnMenuCloture;
   private JButton btnMenuEmployes;
   private JButton btnMenuExit;
   private JButton btnMenuFx;
   private JButton btnMenuImport;
   private JButton btnMenuParametres;
   private JButton btnMenuPointage;
   private JButton btnMenuPwd;
   private JButton btnMenuQuerySal;
   private JButton btnMenuRepport;
   private JButton btnMenuRubriques;
   private JButton btnMenuSecurity;
   private JButton btnMenuStatistiques;
   private JButton btnMenuStrictures;
   private JButton btnRefreshDashboard;
   private JButton btnShowDashboard;
   private JPanel chartPanelAge;
   private JPanel chartPanelAnc;
   private JPanel chartPanelByDep;
   private JPanel chartPanelEffByPoste;
   private JPanel chartPanelGen;
   private JPanel chartPanelGenre;
   private JDesktopPane jDesktopPane;
   private JLabel jLabel10;
   private JLabel jLabel11;
   private JLabel jLabel12;
   private JLabel jLabel2;
   private JLabel jLabel5;
   private JLabel jLabel7;
   private JLabel jLabel8;
   private JLabel jLabel9;
   private JPanel jPanel3;
   private JPanel jPanel4;
   private JSeparator jSeparator1;
   private JSeparator jSeparator2;
   private JLabel lbCurrentNet;
   private JLabel lbLicence;
   private JLabel lbNbSal;
   private JLabel lbNetPreviousPeriode1;
   private JLabel lbNetPreviousPeriode2;
   private JLabel lbNetPreviousPeriode3;
   private JLabel lbPC;
   private JLabel lbSBrutCurrent;
   private JLabel lbSBrutPrevious;
   private JLabel lbSalCount;
   private JLabel lbUser;
   private JLabel lbVersion;
   private JPanel pnlDashborad;
   private JPanel pnlMenu;
   private JProgressBar progressBar;

   public menu() {
      this.nf = NumberFormat.getNumberInstance(Locale.FRENCH);
      this.df = new SimpleDateFormat("yyyy-MM-dd");
      this.pf = new SimpleDateFormat("MMMM yyyy");
      this.spf = new SimpleDateFormat("MMM yyyy");
      this.fdf = new SimpleDateFormat("dd/MM/yyyy");
      this.timeformat = new SimpleDateFormat("H:mm:ss");
      this.filePeriodeDF = new SimpleDateFormat("MMMM-YYYY");
      this.periodeShort = new SimpleDateFormat("MM/YY");
      this.periodeExo = new SimpleDateFormat("YYYY");
      this.frenchWeek = new ThisLocalizedWeek(Locale.FRANCE);
      this.initComponents();
      this.setExtendedState(0);
      this.setLocationRelativeTo(this);
      this.initIcons();
   }

   private void initIcons() {
      this.btnShowDashboard.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DASHBOARD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMaskDashboard.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CANCEL, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnRefreshDashboard.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.REFRESH, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenu.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.MENU, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuCalculPaie.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADJUST, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuCloture.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ARCHIVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuEmployes.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.GROUP, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuFx.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FUNCTIONS, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuImport.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.IMPORT_EXPORT, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuParametres.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SETTINGS, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuPointage.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.TIMER, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuPwd.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.LOCK, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuQuerySal.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FILTER_LIST, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuRepport.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SHOW_CHART, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuRubriques.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BOOK, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuSecurity.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SECURITY, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuStatistiques.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PIE_CHART, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnMenuStrictures.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.QUEUE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void RolesAction(Utilisateurs var1) {
      if (versionCompare(this.paramsGen.getCustumerActiveVersion(), this.appVersion) == 1 && !this.appVersion.equalsIgnoreCase("0.0.0")) {
         this.showMsg(this, "ATTENTION! Votre base de donn\u00e9s ne peut plus \u00eatre ouverte avec une version ant\u00e9rieur \u00e0 " + this.paramsGen.getCustumerActiveVersion() + ". Veuillez installer une version plus r\u00e9cente.");
         System.exit(0);
      }

      this.lbVersion.setText(this.appVersion);
      this.lbVersion.setForeground(this.newVersionIsFree ? Color.BLACK : Color.ORANGE);
      String av = this.gl.onlineCurrentVersion();
      if (av != null && versionCompare(this.appVersion, av) == -1) {
         this.showMsg(this, "Une nouvelle version est disponible. Nous vous recommandons de l'installer.");
      }

      if (this.paramsGen.getLicenceKey() != null) {
         this.updateLicence(this.paramsGen.getLicenceKey());
         if (this.paramsGen.getCustumerActiveVersion() != null && !this.appVersion.equalsIgnoreCase("0.0.0")) {
            if (this.paramsGen.getCustumerActiveVersion().equalsIgnoreCase("0.0.0")) {
               this.showMsg(this, "ERROR: Votre licence est invalide. Veuillez consulter votre fournisseur de logiciel!");
               System.exit(0);
            } else if (!this.newVersionIsFree) {
               if (versionCompare(this.paramsGen.getCustumerActiveVersion(), this.appVersion) == -1) {
                  this.grantNewVersionAccess = false;
                  this.showMsg(this, "ATTENTION! Cette version est une version de d\u00e9monstration et n'est pas encore activ\u00e9e pour votre licence. Alors certaines fonctionalit\u00e9s ne seront pas accessibles.");
               } else {
                  this.updateToNewVersion();
               }
            } else {
               this.updateToNewVersion();
            }
         }
      }

      this.btnMenuImport.setVisible(role.isMaj());
      this.btnMenuPointage.setVisible(role.isSalHs());
      this.btnMenuCalculPaie.setVisible(role.isMaj());
      this.btnMenuParametres.setVisible(role.isParametre());
      this.btnMenuStrictures.setVisible(role.isParametre());
      this.btnMenuRubriques.setVisible(role.isRubriquepaie());
      this.btnMenuCloture.setVisible(role.isCloture());
      this.btnMenuSecurity.setVisible(role.isSecurite());
      this.pnlDashborad.setVisible(false);
      this.btnShowDashboard.setVisible(role.isDashboard());
      this.btnMaskDashboard.setVisible(role.isDashboard());
      this.btnRefreshDashboard.setVisible(role.isDashboard());
   }

   public void showSuccessMsg(String var1, int var2) {
      JOptionPane.showMessageDialog(this, msg, "", msgType);
   }

   public static Image scaleImage(Image var0, int var1, int var2) {
      BufferedImage img = new BufferedImage(width, height, 2);
      Graphics2D g = (Graphics2D)img.getGraphics();
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g.drawImage(source, 0, 0, width, height, (ImageObserver)null);
      g.dispose();
      return img;
   }

   public int updateLicence(String var1) {
      result = 0;
      if (this.gl.netIsAvailable()) {
         Licencekey lk = this.gl.codeLicencekey(licenceKey);
         if (lk != null) {
            this.paramsGen.setLicenceKey(licenceKey);
            this.paramsGen.setNomEntreprise(lk.getCompanytName());
            this.paramsGen.setNbSalaryCode(lk.getNbSalaryCode());
            this.paramsGen.setPub(lk.getPub());
            this.paramsGen.setCustumerActiveVersion(lk.getCustumerActiveVersion());
            this.paramsGen.setLicencePeriodicity(lk.getLicencePeriodicity());
            this.paramsGen.setDateInitLicence(lk.getDateInitLicence());
            this.paramsGen.setDateCurentLicence(lk.getDateCurentLicence());
            this.paramsGen.setLogo(new byte[0]);
            if (this.gl.updateOcurance(this.paramsGen)) {
               JLabel var10000 = this.lbLicence;
               String var10001 = this.paramsGen.getNomEntreprise();
               var10000.setText("Licence accord\u00e9e \u00e0: " + var10001 + " | Exp.: " + this.fdf.format(this.dateExpLicence()));
               if (this.dateExpLicence().before(this.paramsGen.getPeriodeCourante())) {
                  this.lbLicence.setForeground(Color.red);
               }

               this.gl.codeLicencekeyApply(licenceKey);
               result = 1;
            }
         } else {
            this.showErrMsg(this, "Code invalide");
         }
      } else {
         this.showErrMsg(this, "Pas de connexion internet. Activation impossible!");
         result = 0;
      }

      return result;
   }

   public void updateToNewVersion() {
      if (!this.appVersion.equalsIgnoreCase("0.0.0") && this.grantNewVersionAccess) {
         this.paramsGen.setCustumerActiveVersion(this.appVersion);
         this.paramsGen.setLogo(this.paramsGen.getLogo() != null ? this.paramsGen.getLogo() : new byte[0]);
         this.gl.updateOcurance(this.paramsGen);
      }

   }

   static int versionCompare(String var0, String var1) {
      int vnum1 = 0;
      int vnum2 = 0;
      int i = 0;

      for(int j = 0; i < v1.length() || j < v2.length(); ++j) {
         while(i < v1.length() && v1.charAt(i) != '.') {
            vnum1 = vnum1 * 10 + (v1.charAt(i) - 48);
            ++i;
         }

         while(j < v2.length() && v2.charAt(j) != '.') {
            vnum2 = vnum2 * 10 + (v2.charAt(j) - 48);
            ++j;
         }

         if (vnum1 > vnum2) {
            return 1;
         }

         if (vnum2 > vnum1) {
            return -1;
         }

         vnum2 = 0;
         vnum1 = 0;
         ++i;
      }

      return 0;
   }

   private void showDashboardData() {
      Thread t = new 1(this);
      t.start();
   }

   private PieDataset pieDataEffByPoste() {
      DefaultPieDataset dataset = new DefaultPieDataset();
      int effSal = 0;
      List<Keyvaluedouble> lst = new ArrayList();

      for(Poste dep : this.stricturesIF.dataListInit_Postes) {
         effSal = this.pc.countEmpByPoste(dep);
         if (effSal > 0) {
            lst.add(new Keyvaluedouble(dep.getNom(), (double)effSal));
         }
      }

      for(Keyvaluedouble kv : (List)lst.stream().sorted(Comparator.comparing(Keyvaluedouble::getValue).reversed()).collect(Collectors.toList())) {
         dataset.setValue(kv.getKey(), kv.getValue());
      }

      return dataset;
   }

   private PieDataset pieDataAnc() {
      DefaultPieDataset dataset = new DefaultPieDataset();
      double r = this.pc.countEmpByAnciennete(0, 5);
      dataset.setValue("<5 ans (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      r = this.pc.countEmpByAnciennete(5, 10);
      dataset.setValue("5-10 ans (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      r = this.pc.countEmpByAnciennete(10, 16);
      dataset.setValue("10-16 ans (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      r = this.pc.countEmpByAnciennete(16, 100);
      dataset.setValue(">16 ans (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      return dataset;
   }

   private PieDataset pieDataAges() {
      DefaultPieDataset dataset = new DefaultPieDataset();
      double r = this.pc.countEmpByTrancheAge(0, 25);
      dataset.setValue("<25 ans (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      r = this.pc.countEmpByTrancheAge(25, 35);
      dataset.setValue("25-35 ans (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      r = this.pc.countEmpByTrancheAge(35, 45);
      dataset.setValue("35-45 ans (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      r = this.pc.countEmpByTrancheAge(45, 6);
      dataset.setValue("45-60 ans (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      r = this.pc.countEmpByTrancheAge(60, 100);
      dataset.setValue(">60 ans (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      return dataset;
   }

   private PieDataset pieDataGenre() {
      DefaultPieDataset dataset = new DefaultPieDataset();
      double r = this.pc.countEmpByGenre("M");
      dataset.setValue("Hommes (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      r = this.pc.countEmpByGenre("F");
      dataset.setValue("Femmes (" + (int)Math.floor(r / (double)((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size() * (double)100.0F) + "%)", r);
      return dataset;
   }

   private DefaultCategoryDataset createDatasetGen() {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();

      try {
         for(int i = 1; i < 13; ++i) {
            SimpleDateFormat var10000 = new SimpleDateFormat("yyyy-MM-dd");
            String var10001 = (new SimpleDateFormat("yyyy")).format(this.paramsGen.getPeriodeCourante());
            Date periode = var10000.parse(var10001 + "-" + i + "-28");
            String periodeStr = (new SimpleDateFormat("MMM")).format(periode);
            dataset.addValue(this.pc.salBrutByPeriode(periode), "SAL. BRUT", periodeStr);
            dataset.addValue(this.pc.itsByPeriode(periode), "ITS", periodeStr);
            dataset.addValue(this.pc.cnssByPeriode(periode), "CNSS", periodeStr);
            dataset.addValue(this.pc.cnamByPeriode(periode), "CNAM", periodeStr);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return dataset;
   }

   private DefaultCategoryDataset createDatasetDep() {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();

      try {
         List<Keyvaluedouble> lst = new ArrayList();

         for(Departement dep : this.stricturesIF.dataListInit_Departements) {
            double mnt = this.pc.SNByPeriodeByDep(this.paramsGen.getPeriodeCourante(), dep.getNom());
            if (mnt > (double)0.0F) {
               lst.add(new Keyvaluedouble(dep.getNom(), mnt));
            }
         }

         for(Keyvaluedouble kv : (List)lst.stream().sorted(Comparator.comparing(Keyvaluedouble::getValue).reversed()).collect(Collectors.toList())) {
            int nbSal = this.pc.countEmpByDepCurrentPeriode(kv.getKey());
            String var10000 = kv.getKey();
            String key = var10000 + " (" + nbSal + " sal.)";
            dataset.addValue(kv.getValue(), "SALAIRE", key);
            dataset.addValue(this.pc.CNGByPeriodeByDep(this.paramsGen.getPeriodeCourante(), kv.getKey()), "CONGES", key);
            dataset.addValue(this.pc.BonusByPeriodeByDep(this.paramsGen.getPeriodeCourante(), kv.getKey()), "BONUS", key);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return dataset;
   }

   private void showSalInfos() {
      JLabel var10000 = this.lbSalCount;
      int var10001 = ((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size();
      var10000.setText(var10001 + " salari\u00e9s, dont " + ((List)this.pc.dlEmp.stream().filter((var0) -> var0.isActif() && var0.isEnConge()).collect(Collectors.toList())).size() + " en cong\u00e9s");

      try {
         Date periodePrecedente = this.gl.addRetriveDays(this.paramsGen.getPeriodeCourante(), -30);
         periodePrecedente = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periodePrecedente));
         double salBrutCurent = this.pc.salBrutByPeriode(this.paramsGen.getPeriodeCourante());
         double salBrutPrevious = this.pc.salBrutByPeriode(periodePrecedente);
         double salNetCurent = this.pc.salNetByPeriode(periodePrecedente);
         var10000 = this.lbCurrentNet;
         Object var17;
         if (salNetCurent > (double)10000.0F) {
            GeneralLib var16 = this.gl;
            var17 = GeneralLib.coolNumberFormat(salNetCurent);
         } else {
            var17 = salNetCurent;
         }

         String var18 = String.valueOf(var17);
         var10000.setText(var18 + " MRU");
         var10000 = this.lbCurrentNet;
         var18 = this.nf.format(salNetCurent);
         var10000.setToolTipText(var18 + " MRU");
         this.lbCurrentNet.setIcon(IconFontSwing.buildIcon(salBrutCurent > salBrutPrevious ? GoogleMaterialDesignIcons.TRENDING_UP : GoogleMaterialDesignIcons.TRENDING_DOWN, 27.0F, salBrutCurent > salBrutPrevious ? new Color(0, 153, 51) : new Color(255, 0, 0), new Color(255, 255, 255)));
         var10000 = this.lbSBrutCurrent;
         Object var21;
         if (salBrutCurent > (double)10000.0F) {
            GeneralLib var20 = this.gl;
            var21 = GeneralLib.coolNumberFormat(salBrutCurent);
         } else {
            var21 = salBrutCurent;
         }

         var10000.setText(String.valueOf(var21) + " MRU");
         var10000 = this.lbSBrutCurrent;
         String var22 = this.nf.format(salBrutCurent);
         var10000.setToolTipText(var22 + " MRU");
         var10000 = this.lbSBrutPrevious;
         Object var24;
         if (salBrutPrevious > (double)10000.0F) {
            GeneralLib var23 = this.gl;
            var24 = GeneralLib.coolNumberFormat(salBrutPrevious);
         } else {
            var24 = salBrutPrevious;
         }

         var10000.setText(String.valueOf(var24) + " MRU");
         var10000 = this.lbSBrutPrevious;
         String var25 = this.nf.format(salBrutPrevious);
         var10000.setToolTipText(var25 + " MRU");
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private void showChartEffByPoste() {
      JFreeChart chart = ChartFactory.createPieChart3D("", this.pieDataEffByPoste(), false, true, false);
      PiePlot plot = (PiePlot)chart.getPlot();
      plot.setLabelFont(new Font("SansSerif", 0, 8));
      plot.setNoDataMessage("Donn\u00e9es non disponibles!");
      plot.setCircular(true);
      plot.setIgnoreZeroValues(true);
      plot.setLabelGap(0.01);
      plot.setBackgroundPaint(Color.WHITE);
      plot.setBackgroundAlpha(0.2F);
      ChartPanel p = new ChartPanel(chart);
      this.chartPanelEffByPoste.removeAll();
      this.chartPanelEffByPoste.add(p, "Center");
      this.chartPanelEffByPoste.validate();
   }

   private void showChartGenre() {
      JFreeChart chart = ChartFactory.createPieChart3D("", this.pieDataGenre(), false, false, false);
      PiePlot plot = (PiePlot)chart.getPlot();
      plot.setLabelFont(new Font("SansSerif", 0, 10));
      plot.setNoDataMessage("Donn\u00e9es non disponibles!");
      plot.setCircular(true);
      plot.setIgnoreZeroValues(true);
      plot.setLabelGap(0.01);
      plot.setBackgroundPaint(Color.WHITE);
      plot.setBackgroundAlpha(0.2F);
      ChartPanel p = new ChartPanel(chart);
      this.chartPanelGenre.removeAll();
      this.chartPanelGenre.add(p, "Center");
      this.chartPanelGenre.validate();
   }

   private void showChartDep() {
      JFreeChart chart = ChartFactory.createBarChart3D("", "", "MRU", this.createDatasetDep(), PlotOrientation.HORIZONTAL, true, true, false);
      CategoryPlot plot = (CategoryPlot)chart.getPlot();
      plot.getRenderer().setSeriesPaint(0, new Color(0, 102, 153));
      plot.setBackgroundPaint(Color.WHITE);
      plot.setBackgroundAlpha(0.2F);
      ChartPanel p = new ChartPanel(chart);
      this.chartPanelByDep.removeAll();
      this.chartPanelByDep.add(p, "Center");
      this.chartPanelByDep.validate();
   }

   private void showChartGen() {
      JFreeChart chart = ChartFactory.createBarChart("", "", "MRU", this.createDatasetGen(), PlotOrientation.VERTICAL, true, true, false);
      CategoryPlot plot = (CategoryPlot)chart.getPlot();
      plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
      plot.setDomainGridlinesVisible(true);
      plot.setRangeGridlinesVisible(false);
      plot.setBackgroundPaint(Color.WHITE);
      plot.setBackgroundAlpha(0.2F);
      ChartPanel p = new ChartPanel(chart);
      this.chartPanelGen.removeAll();
      this.chartPanelGen.add(p, "Center");
      this.chartPanelGen.validate();
   }

   private void showChartAges() {
      JFreeChart chart = ChartFactory.createRingChart("", this.pieDataAges(), false, false, false);
      PiePlot plot = (PiePlot)chart.getPlot();
      plot.setLabelFont(new Font("SansSerif", 0, 10));
      plot.setNoDataMessage("Donn\u00e9es non disponibles!");
      plot.setCircular(true);
      plot.setIgnoreZeroValues(true);
      plot.setLabelGap(0.01);
      plot.setBackgroundPaint(Color.WHITE);
      plot.setBackgroundAlpha(0.2F);
      ChartPanel p = new ChartPanel(chart);
      this.chartPanelAge.removeAll();
      this.chartPanelAge.add(p, "Center");
      this.chartPanelAge.validate();
   }

   private void showChartAnc() {
      JFreeChart chart = ChartFactory.createRingChart("", this.pieDataAnc(), false, false, false);
      PiePlot plot = (PiePlot)chart.getPlot();
      plot.setLabelFont(new Font("SansSerif", 0, 10));
      plot.setNoDataMessage("Donn\u00e9es non disponibles!");
      plot.setCircular(true);
      plot.setIgnoreZeroValues(true);
      plot.setLabelGap(0.01);
      plot.setBackgroundPaint(Color.WHITE);
      plot.setBackgroundAlpha(0.2F);
      ChartPanel p = new ChartPanel(chart);
      this.chartPanelAnc.removeAll();
      this.chartPanelAnc.add(p, "Center");
      this.chartPanelAnc.validate();
   }

   public void openFrame(JInternalFrame var1) {
      form.setVisible(true);
      this.jDesktopPane.remove(form);
      this.jDesktopPane.add(form);
      form.toFront();
   }

   public boolean netIsAvailable() {
      try {
         URL url = new URL("http://www.google.com");
         URLConnection conn = url.openConnection();
         conn.connect();
         return true;
      } catch (MalformedURLException e) {
         throw new RuntimeException(e);
      } catch (IOException var4) {
         return false;
      }
   }

   public void openDocument(Document var1) {
      String fileExt = !pj.getFileType().equalsIgnoreCase("PNG") && !pj.getFileType().equalsIgnoreCase("JPG") && !pj.getFileType().equalsIgnoreCase("JPEG") && !pj.getFileType().equalsIgnoreCase("BMP") && !pj.getFileType().equalsIgnoreCase("GIF") && !pj.getFileType().equalsIgnoreCase("TIFF") ? pj.getFileType() : "image";
      if (fileExt.equals("image")) {
         this.openDocImg(pj);
      } else {
         this.openDocNoImg(pj);
      }

   }

   private void openDocImg(Document var1) {
      try {
         PDDocument document = new PDDocument();
         PDPage page = new PDPage(PDRectangle.A4);
         PDPageContentStream cos = new PDPageContentStream(document, page);
         int var10000 = pj.getEmploye().getId();
         String fileName = "temp/pj_" + var10000 + "_" + pj.getId() + ".pdf";
         byte[] buf = pj.getDocFile();
         File file = new File(fileName);
         file.deleteOnExit();
         PDImageXObject imageObject = PDImageXObject.createFromByteArray(document, buf, (String)null);
         float scale = 0.4F;
         cos.drawImage(imageObject, 100.0F, 400.0F, (float)imageObject.getWidth() * scale, (float)imageObject.getHeight() * scale);
         cos.close();
         document.addPage(page);
         document.save(fileName);
         document.close();
         Desktop.getDesktop().open(new File(fileName));
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private void openDocNoImg(Document var1) {
      try {
         byte[] buf = pj.getDocFile();
         int var10000 = pj.getEmploye().getId();
         String fileName = "temp/pj_" + var10000 + "_" + pj.getId() + "." + pj.getFileType();
         File file = new File(fileName);
         file.deleteOnExit();
         FileOutputStream fos = new FileOutputStream(fileName);
         fos.write(buf);
         fos.close();
         Desktop.getDesktop().open(new File(fileName));
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public void showSuccessMsg() {
      JOptionPane.showMessageDialog(this, "OPERATION TERMINEE AVEC SUCCE!", "", 1);
   }

   public void showMsg(Component var1, String var2) {
      JOptionPane.showMessageDialog(frame, msg, "", 1);
   }

   public void showDemoMsg() {
      JOptionPane.showMessageDialog(this, "Cette fonctionnalit\u00e9 n'est pas disponible dans la version de d\u00e9monstration.", "", 1);
   }

   public void showErrMsg(Component var1, String var2) {
      JOptionPane.showMessageDialog(frame, msg, "", 0);
   }

   public Date dateExpLicence() {
      Date r = this.gl.addRetriveDays(this.paramsGen.getDateCurentLicence(), 30);
      switch (this.paramsGen.getLicencePeriodicity()) {
         case "M" -> r = this.gl.addRetriveDays(this.paramsGen.getDateCurentLicence(), 30);
         case "T" -> r = this.gl.addRetriveDays(this.paramsGen.getDateCurentLicence(), 90);
         case "A" -> r = this.gl.addRetriveDays(this.paramsGen.getDateCurentLicence(), 365);
      }

      return r;
   }

   public void initStatusBar(String var1) {
      SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
      this.lbUser.setText("Bienvenue: " + userLogin);
      JLabel var10000 = this.lbPC;
      String var10001 = sdf.format(this.paramsGen.getPeriodeCourante());
      var10000.setText("P\u00e9riode courante: " + var10001);
      if (this.paramsGen.getLicenceKey() == null) {
         this.lbLicence.setText("VERSION DEMO");
         this.lbNbSal.setText("Nb. Sal.:3");
      } else {
         var10000 = this.lbLicence;
         var10001 = this.paramsGen.getNomEntreprise();
         var10000.setText("Licence accord\u00e9e \u00e0: " + var10001 + " | Exp.: " + this.fdf.format(this.dateExpLicence()));
         var10000 = this.lbNbSal;
         int var6 = this.employeFrame.dataListInit.size();
         var10000.setText("Nb. Sal.:" + var6 + "/" + this.limiteSalarie);
      }

   }

   public void RubriquePaieCorrectionBySal(long var1) {
      Query q = this.entityManager.createQuery("Select p from Rubriquepaie p where p.employe.id=" + salID);
      q.setMaxResults(1000000);
      Set<Rubriquepaie> dlRubriquepaie = new HashSet(q.getResultList());

      for(Rubriquepaie rs : (Set)dlRubriquepaie.stream().filter((var1x) -> var1x.getPeriode().before(this.paramsGen.getPeriodeCourante())).collect(Collectors.toSet())) {
         if (this.pc.paieById(rs.getEmploye(), rs.getMotif(), rs.getPeriode()) == null) {
            this.gl.deleteOcurance(rs);
         }
      }

      for(Rubriquepaie rs : (Set)dlRubriquepaie.stream().filter((var1x) -> this.df.format(var1x.getPeriode()).equalsIgnoreCase(this.df.format(this.paramsGen.getPeriodeCourante()))).collect(Collectors.toSet())) {
         if (rs.getEmploye() != null) {
            if (rs.getEmploye().isEnConge()) {
               this.gl.deleteOcurance(rs);
            }
         } else {
            this.gl.deleteOcurance(rs);
         }
      }

   }

   public static boolean objectFilter(Object var0, String var1) {
      return textToFilter.isEmpty() ? true : CustomComboRenderer.getDisplayText(o).toLowerCase().contains(textToFilter.toLowerCase());
   }

   public void remplirCombo(String var1, JComboBox<Object> var2) {
      combo.removeAllItems();
      Set<Paie> dlPaie = new HashSet(this.pc.dlPaie);
      switch (className) {
         case "Rubrique":
            combo.setModel(new DefaultComboBoxModel(((List)this.rubriqueFrame.dataListInit.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())).toArray(new Rubrique[this.rubriqueFrame.dataListInit.size()])));
            ComboBoxFilterDecorator<Rubrique> decorate = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate.getFilterTextSupplier()));
            break;
         case "RubriqueRet":
            List<Rubrique> dl = (List)this.rubriqueFrame.dataListInit.stream().filter((var0) -> var0.getSens().equalsIgnoreCase("R") && var0.getDeductionDu().equalsIgnoreCase("Net")).sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList());
            combo.setModel(new DefaultComboBoxModel(dl.toArray(new Rubrique[dl.size()])));
            ComboBoxFilterDecorator<Rubrique> decorate1 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate1.getFilterTextSupplier()));
            break;
         case "Motif":
            combo.setModel(new DefaultComboBoxModel(((List)this.stricturesIF.dataListInit_Motifs.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())).toArray(new Motif[this.stricturesIF.dataListInit_Motifs.size()])));
            ComboBoxFilterDecorator<Motif> decorate2 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate2.getFilterTextSupplier()));
            break;
         case "Poste":
            combo.setModel(new DefaultComboBoxModel(((List)this.stricturesIF.dataListInit_Postes.stream().sorted(Comparator.comparing((var0) -> var0.getNom())).collect(Collectors.toList())).toArray(new Poste[this.stricturesIF.dataListInit_Postes.size()])));
            ComboBoxFilterDecorator<Poste> decorate3 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate3.getFilterTextSupplier()));
            break;
         case "Directiongeneral":
            combo.setModel(new DefaultComboBoxModel(((List)this.stricturesIF.dataListInit_Directiongeneral.stream().sorted(Comparator.comparing((var0) -> var0.getNom())).collect(Collectors.toList())).toArray(new Directiongeneral[this.stricturesIF.dataListInit_Directiongeneral.size()])));
            ComboBoxFilterDecorator<Directiongeneral> decorate10 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate10.getFilterTextSupplier()));
            break;
         case "Direction":
            combo.setModel(new DefaultComboBoxModel(((List)this.stricturesIF.dataListInit_Direction.stream().sorted(Comparator.comparing((var0) -> var0.getNom())).collect(Collectors.toList())).toArray(new Direction[this.stricturesIF.dataListInit_Direction.size()])));
            ComboBoxFilterDecorator<Direction> decorate11 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate11.getFilterTextSupplier()));
            break;
         case "Departement":
            combo.setModel(new DefaultComboBoxModel(((List)this.stricturesIF.dataListInit_Departements.stream().sorted(Comparator.comparing((var0) -> var0.getNom())).collect(Collectors.toList())).toArray(new Departement[this.stricturesIF.dataListInit_Departements.size()])));
            ComboBoxFilterDecorator<Departement> decorate4 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate4.getFilterTextSupplier()));
            break;
         case "Banque":
            combo.setModel(new DefaultComboBoxModel(((List)this.stricturesIF.dataListInit_Banques.stream().sorted(Comparator.comparing((var0) -> var0.getNom())).collect(Collectors.toList())).toArray(new Banque[this.stricturesIF.dataListInit_Banques.size()])));
            ComboBoxFilterDecorator<Banque> decorate5 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate5.getFilterTextSupplier()));
            break;
         case "Activite":
            combo.setModel(new DefaultComboBoxModel(((List)this.stricturesIF.dataListInit_Activites.stream().sorted(Comparator.comparing((var0) -> var0.getNom())).collect(Collectors.toList())).toArray(new Activite[this.stricturesIF.dataListInit_Activites.size()])));
            ComboBoxFilterDecorator<Activite> decorate6 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate6.getFilterTextSupplier()));
            break;
         case "Statut":
            combo.setModel(new DefaultComboBoxModel(((List)this.stricturesIF.dataListInit_Statuts.stream().sorted(Comparator.comparing((var0) -> var0.getNom())).collect(Collectors.toList())).toArray(new Statut[this.stricturesIF.dataListInit_Statuts.size()])));
            ComboBoxFilterDecorator<Statut> decorate7 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate7.getFilterTextSupplier()));
            break;
         case "Grillesalairebase":
            combo.setModel(new DefaultComboBoxModel(((List)this.stricturesIF.dataListInit_Grillesalairebases.stream().sorted(Comparator.comparing((var0) -> var0.getCategorie())).collect(Collectors.toList())).toArray(new Grillesalairebase[this.stricturesIF.dataListInit_Grillesalairebases.size()])));
            ComboBoxFilterDecorator<Grillesalairebase> decorate8 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate8.getFilterTextSupplier()));
            break;
         case "Origines":
            combo.setModel(new DefaultComboBoxModel(((List)this.stricturesIF.dataListInit_Origines.stream().sorted(Comparator.comparing((var0) -> var0.getLibelle())).collect(Collectors.toList())).toArray(new Origines[this.stricturesIF.dataListInit_Origines.size()])));
            ComboBoxFilterDecorator<Origines> decorate9 = ComboBoxFilterDecorator.decorate(combo, CustomComboRenderer::getDisplayText, menu::objectFilter);
            combo.setRenderer(new CustomComboRenderer(decorate9.getFilterTextSupplier()));
            break;
         case "Exercice":
            JComboBoxRendererExo renderer = new JComboBoxRendererExo(this);
            combo.setRenderer(renderer);
            Query q;
            if (this.dialect.toString().contains("Oracle")) {
               q = this.entityManager.createQuery("Select p from Paie p group by to_date(p.periode, 'YYYY') order by to_date(p.periode, 'YYYY') desc");
            } else {
               q = this.entityManager.createQuery("Select p from Paie p group by year(p.periode) order by p.periode desc");
            }

            q.setMaxResults(1000000);

            for(Object o : q.getResultList()) {
               combo.addItem(((Paie)o).getPeriode());
            }
            break;
         case "Periode":
            JComboBoxRendererPeriode renderer2 = new JComboBoxRendererPeriode(this);
            combo.setRenderer(renderer2);
            combo.addItem(this.paramsGen.getPeriodeCourante());
            List<Date> dl2 = new ArrayList(((Map)dlPaie.stream().sorted(Comparator.comparing((var0) -> var0.getPeriode())).collect(Collectors.groupingBy((var0) -> var0.getPeriode()))).keySet());

            for(Date o : (List)dl2.stream().sorted((var0, var1x) -> var1x.compareTo(var0)).collect(Collectors.toList())) {
               combo.addItem(o);
            }
      }

   }

   public void viewMessage(JLabel var1, String var2, boolean var3) {
      if (err) {
         msgLabel.setForeground(new Color(204, 0, 0));
      } else {
         msgLabel.setForeground(new Color(0, 102, 0));
      }

      msgLabel.setText(message);
   }

   private void initComponents() {
      this.pnlMenu = new JPanel();
      this.btnMenuEmployes = new JButton();
      this.btnMenuParametres = new JButton();
      this.btnMenuStrictures = new JButton();
      this.btnMenuQuerySal = new JButton();
      this.btnMenuPointage = new JButton();
      this.btnMenuCalculPaie = new JButton();
      this.btnMenuCloture = new JButton();
      this.btnMenuStatistiques = new JButton();
      this.btnMenuRubriques = new JButton();
      this.btnMenuFx = new JButton();
      this.btnMenuImport = new JButton();
      this.btnMenuSecurity = new JButton();
      this.btnMenuRepport = new JButton();
      this.lbUser = new JLabel();
      this.btnMenuPwd = new JButton();
      this.jSeparator2 = new JSeparator();
      this.jPanel3 = new JPanel();
      this.jLabel2 = new JLabel();
      this.jLabel5 = new JLabel();
      this.btnMenu = new JButton();
      this.btnMenuExit = new JButton();
      this.lbVersion = new JLabel();
      this.btnShowDashboard = new JButton();
      this.btnRefreshDashboard = new JButton();
      this.btnMaskDashboard = new JButton();
      this.jPanel4 = new JPanel();
      this.lbLicence = new JLabel();
      this.lbNbSal = new JLabel();
      this.lbPC = new JLabel();
      this.jDesktopPane = new JDesktopPane();
      this.pnlDashborad = new JPanel();
      this.chartPanelGen = new JPanel();
      this.lbSBrutCurrent = new JLabel();
      this.jLabel7 = new JLabel();
      this.chartPanelAge = new JPanel();
      this.jLabel8 = new JLabel();
      this.chartPanelByDep = new JPanel();
      this.jLabel9 = new JLabel();
      this.chartPanelGenre = new JPanel();
      this.jLabel10 = new JLabel();
      this.chartPanelAnc = new JPanel();
      this.jLabel11 = new JLabel();
      this.lbSalCount = new JLabel();
      this.lbCurrentNet = new JLabel();
      this.lbSBrutPrevious = new JLabel();
      this.lbNetPreviousPeriode1 = new JLabel();
      this.lbNetPreviousPeriode2 = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.lbNetPreviousPeriode3 = new JLabel();
      this.jLabel12 = new JLabel();
      this.chartPanelEffByPoste = new JPanel();
      this.progressBar = new JProgressBar();
      this.setDefaultCloseOperation(3);
      this.pnlMenu.setBackground(new Color(0, 0, 51));
      this.btnMenuEmployes.setBackground(new Color(0, 0, 51));
      this.btnMenuEmployes.setForeground(new Color(255, 255, 255));
      this.btnMenuEmployes.setText("Employ\u00e9s");
      this.btnMenuEmployes.setToolTipText("Employ\u00e9s");
      this.btnMenuEmployes.setBorderPainted(false);
      this.btnMenuEmployes.setContentAreaFilled(false);
      this.btnMenuEmployes.setCursor(new Cursor(12));
      this.btnMenuEmployes.setHorizontalAlignment(2);
      this.btnMenuEmployes.setOpaque(true);
      this.btnMenuEmployes.addMouseListener(new 2(this));
      this.btnMenuEmployes.addActionListener(new 3(this));
      this.btnMenuParametres.setBackground(new Color(0, 0, 51));
      this.btnMenuParametres.setForeground(new Color(255, 255, 255));
      this.btnMenuParametres.setText("Param\u00e8tres");
      this.btnMenuParametres.setToolTipText("Param\u00e8tres");
      this.btnMenuParametres.setBorderPainted(false);
      this.btnMenuParametres.setContentAreaFilled(false);
      this.btnMenuParametres.setCursor(new Cursor(12));
      this.btnMenuParametres.setHorizontalAlignment(2);
      this.btnMenuParametres.setOpaque(true);
      this.btnMenuParametres.addMouseListener(new 4(this));
      this.btnMenuParametres.addActionListener(new 5(this));
      this.btnMenuStrictures.setBackground(new Color(0, 0, 51));
      this.btnMenuStrictures.setForeground(new Color(255, 255, 255));
      this.btnMenuStrictures.setText("Stricture");
      this.btnMenuStrictures.setToolTipText("Structure");
      this.btnMenuStrictures.setBorderPainted(false);
      this.btnMenuStrictures.setContentAreaFilled(false);
      this.btnMenuStrictures.setCursor(new Cursor(12));
      this.btnMenuStrictures.setHorizontalAlignment(2);
      this.btnMenuStrictures.setOpaque(true);
      this.btnMenuStrictures.addMouseListener(new 6(this));
      this.btnMenuStrictures.addActionListener(new 7(this));
      this.btnMenuQuerySal.setBackground(new Color(0, 0, 51));
      this.btnMenuQuerySal.setForeground(new Color(255, 255, 255));
      this.btnMenuQuerySal.setText("Traitement collectif");
      this.btnMenuQuerySal.setToolTipText("Traitement collectif");
      this.btnMenuQuerySal.setBorderPainted(false);
      this.btnMenuQuerySal.setContentAreaFilled(false);
      this.btnMenuQuerySal.setCursor(new Cursor(12));
      this.btnMenuQuerySal.setHorizontalAlignment(2);
      this.btnMenuQuerySal.setOpaque(true);
      this.btnMenuQuerySal.addMouseListener(new 8(this));
      this.btnMenuQuerySal.addActionListener(new 9(this));
      this.btnMenuPointage.setBackground(new Color(0, 0, 51));
      this.btnMenuPointage.setForeground(new Color(255, 255, 255));
      this.btnMenuPointage.setText("Pointage");
      this.btnMenuPointage.setToolTipText("Pointage");
      this.btnMenuPointage.setBorderPainted(false);
      this.btnMenuPointage.setContentAreaFilled(false);
      this.btnMenuPointage.setCursor(new Cursor(12));
      this.btnMenuPointage.setHorizontalAlignment(2);
      this.btnMenuPointage.setOpaque(true);
      this.btnMenuPointage.addMouseListener(new 10(this));
      this.btnMenuPointage.addActionListener(new 11(this));
      this.btnMenuCalculPaie.setBackground(new Color(0, 0, 51));
      this.btnMenuCalculPaie.setForeground(new Color(255, 255, 255));
      this.btnMenuCalculPaie.setText("Calcul de paie");
      this.btnMenuCalculPaie.setToolTipText("Calcul de paie");
      this.btnMenuCalculPaie.setBorderPainted(false);
      this.btnMenuCalculPaie.setContentAreaFilled(false);
      this.btnMenuCalculPaie.setCursor(new Cursor(12));
      this.btnMenuCalculPaie.setHorizontalAlignment(2);
      this.btnMenuCalculPaie.setOpaque(true);
      this.btnMenuCalculPaie.addMouseListener(new 12(this));
      this.btnMenuCalculPaie.addActionListener(new 13(this));
      this.btnMenuCloture.setBackground(new Color(0, 0, 51));
      this.btnMenuCloture.setForeground(new Color(255, 255, 255));
      this.btnMenuCloture.setText("Cl\u00f4ture de paie");
      this.btnMenuCloture.setToolTipText("Cl\u00f4ture de paie");
      this.btnMenuCloture.setBorderPainted(false);
      this.btnMenuCloture.setContentAreaFilled(false);
      this.btnMenuCloture.setCursor(new Cursor(12));
      this.btnMenuCloture.setHorizontalAlignment(2);
      this.btnMenuCloture.setOpaque(true);
      this.btnMenuCloture.addMouseListener(new 14(this));
      this.btnMenuCloture.addActionListener(new 15(this));
      this.btnMenuStatistiques.setBackground(new Color(0, 0, 51));
      this.btnMenuStatistiques.setForeground(new Color(255, 255, 255));
      this.btnMenuStatistiques.setText("Statistiques");
      this.btnMenuStatistiques.setToolTipText("Statistiques");
      this.btnMenuStatistiques.setBorderPainted(false);
      this.btnMenuStatistiques.setContentAreaFilled(false);
      this.btnMenuStatistiques.setCursor(new Cursor(12));
      this.btnMenuStatistiques.setHorizontalAlignment(2);
      this.btnMenuStatistiques.setOpaque(true);
      this.btnMenuStatistiques.addMouseListener(new 16(this));
      this.btnMenuStatistiques.addActionListener(new 17(this));
      this.btnMenuRubriques.setBackground(new Color(0, 0, 51));
      this.btnMenuRubriques.setForeground(new Color(255, 255, 255));
      this.btnMenuRubriques.setText("Rubriques de paie");
      this.btnMenuRubriques.setToolTipText("Rubriques de paie");
      this.btnMenuRubriques.setBorderPainted(false);
      this.btnMenuRubriques.setContentAreaFilled(false);
      this.btnMenuRubriques.setCursor(new Cursor(12));
      this.btnMenuRubriques.setHorizontalAlignment(2);
      this.btnMenuRubriques.setOpaque(true);
      this.btnMenuRubriques.addMouseListener(new 18(this));
      this.btnMenuRubriques.addActionListener(new 19(this));
      this.btnMenuFx.setBackground(new Color(0, 0, 51));
      this.btnMenuFx.setForeground(new Color(255, 255, 255));
      this.btnMenuFx.setText("Calculette de salaires");
      this.btnMenuFx.setToolTipText("Calculette de salaires");
      this.btnMenuFx.setBorderPainted(false);
      this.btnMenuFx.setContentAreaFilled(false);
      this.btnMenuFx.setCursor(new Cursor(12));
      this.btnMenuFx.setHorizontalAlignment(2);
      this.btnMenuFx.setOpaque(true);
      this.btnMenuFx.addMouseListener(new 20(this));
      this.btnMenuFx.addActionListener(new 21(this));
      this.btnMenuImport.setBackground(new Color(0, 0, 51));
      this.btnMenuImport.setForeground(new Color(255, 255, 255));
      this.btnMenuImport.setText("Importation");
      this.btnMenuImport.setToolTipText("Importation");
      this.btnMenuImport.setBorderPainted(false);
      this.btnMenuImport.setContentAreaFilled(false);
      this.btnMenuImport.setCursor(new Cursor(12));
      this.btnMenuImport.setHorizontalAlignment(2);
      this.btnMenuImport.addMouseListener(new 22(this));
      this.btnMenuImport.addActionListener(new 23(this));
      this.btnMenuSecurity.setBackground(new Color(0, 0, 51));
      this.btnMenuSecurity.setForeground(new Color(255, 255, 255));
      this.btnMenuSecurity.setText("Securit\u00e9");
      this.btnMenuSecurity.setToolTipText("Securit\u00e9");
      this.btnMenuSecurity.setBorderPainted(false);
      this.btnMenuSecurity.setContentAreaFilled(false);
      this.btnMenuSecurity.setCursor(new Cursor(12));
      this.btnMenuSecurity.setHorizontalAlignment(2);
      this.btnMenuSecurity.setOpaque(true);
      this.btnMenuSecurity.addMouseListener(new 24(this));
      this.btnMenuSecurity.addActionListener(new 25(this));
      this.btnMenuRepport.setBackground(new Color(0, 0, 51));
      this.btnMenuRepport.setForeground(new Color(255, 255, 255));
      this.btnMenuRepport.setText("Documents de paie");
      this.btnMenuRepport.setToolTipText("Documents de paie");
      this.btnMenuRepport.setBorderPainted(false);
      this.btnMenuRepport.setContentAreaFilled(false);
      this.btnMenuRepport.setCursor(new Cursor(12));
      this.btnMenuRepport.setHorizontalAlignment(2);
      this.btnMenuRepport.setOpaque(true);
      this.btnMenuRepport.addMouseListener(new 26(this));
      this.btnMenuRepport.addActionListener(new 27(this));
      this.lbUser.setFont(new Font("SansSerif", 0, 12));
      this.lbUser.setForeground(new Color(255, 255, 255));
      this.lbUser.setText("Usager");
      this.btnMenuPwd.setBackground(new Color(0, 0, 51));
      this.btnMenuPwd.setToolTipText("Mot de passe");
      this.btnMenuPwd.setBorderPainted(false);
      this.btnMenuPwd.setContentAreaFilled(false);
      this.btnMenuPwd.setCursor(new Cursor(12));
      this.btnMenuPwd.setOpaque(true);
      this.btnMenuPwd.addActionListener(new 28(this));
      GroupLayout pnlMenuLayout = new GroupLayout(this.pnlMenu);
      this.pnlMenu.setLayout(pnlMenuLayout);
      pnlMenuLayout.setHorizontalGroup(pnlMenuLayout.createParallelGroup(1).add(pnlMenuLayout.createSequentialGroup().addContainerGap().add(pnlMenuLayout.createParallelGroup(1).add(this.jSeparator2).add(2, pnlMenuLayout.createSequentialGroup().add(0, 0, 32767).add(pnlMenuLayout.createParallelGroup(1).add(pnlMenuLayout.createParallelGroup(1, false).add(this.btnMenuImport, -1, -1, 32767).add(this.btnMenuCalculPaie, -1, -1, 32767).add(this.btnMenuParametres, -1, -1, 32767).add(this.btnMenuSecurity, -1, -1, 32767).add(this.btnMenuPointage, -1, -1, 32767).add(this.btnMenuCloture, -1, -1, 32767).add(this.btnMenuRubriques, -1, -1, 32767).add(this.btnMenuStrictures, -1, -1, 32767).add(this.btnMenuEmployes, -2, 206, -2)).add(2, pnlMenuLayout.createParallelGroup(1).add(pnlMenuLayout.createParallelGroup(1, false).add(this.btnMenuRepport, -1, -1, 32767).add(this.btnMenuStatistiques, -1, -1, 32767).add(this.btnMenuFx, -2, 206, -2)).add(2, this.btnMenuQuerySal, -2, 206, -2)))).add(pnlMenuLayout.createSequentialGroup().add(this.lbUser, -2, 161, -2).addPreferredGap(0, -1, 32767).add(this.btnMenuPwd, -2, 39, -2))).addContainerGap()));
      pnlMenuLayout.setVerticalGroup(pnlMenuLayout.createParallelGroup(1).add(2, pnlMenuLayout.createSequentialGroup().addContainerGap().add(pnlMenuLayout.createParallelGroup(1).add(this.lbUser, -2, 39, -2).add(this.btnMenuPwd, -2, 39, -2)).add(6, 6, 6).add(this.jSeparator2, -2, 10, -2).addPreferredGap(0).add(this.btnMenuEmployes, -2, 40, -2).addPreferredGap(0).add(this.btnMenuImport, -2, 40, -2).addPreferredGap(0).add(this.btnMenuPointage, -2, 40, -2).addPreferredGap(0).add(this.btnMenuCalculPaie, -2, 40, -2).addPreferredGap(0).add(this.btnMenuRepport, -2, 40, -2).addPreferredGap(0).add(this.btnMenuStatistiques, -2, 40, -2).addPreferredGap(0).add(this.btnMenuCloture, -2, 40, -2).addPreferredGap(0).add(this.btnMenuFx, -2, 40, -2).addPreferredGap(0).add(this.btnMenuQuerySal, -2, 40, -2).addPreferredGap(0).add(this.btnMenuRubriques, -2, 40, -2).addPreferredGap(0).add(this.btnMenuStrictures, -2, 40, -2).add(4, 4, 4).add(this.btnMenuSecurity, -2, 40, -2).addPreferredGap(0).add(this.btnMenuParametres, -2, 40, -2).add(213, 213, 213)));
      this.jPanel3.setBackground(new Color(255, 255, 255));
      this.jLabel2.setFont(new Font("Segoe UI Light", 0, 36));
      this.jLabel2.setForeground(new Color(0, 102, 153));
      this.jLabel2.setText("E L I Y A-Paie");
      this.jLabel5.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel5.setText("Gestion simplifi\u00e9e de paie et GA de RH");
      this.btnMenu.setBorderPainted(false);
      this.btnMenu.setContentAreaFilled(false);
      this.btnMenu.setCursor(new Cursor(12));
      this.btnMenu.setOpaque(true);
      this.btnMenu.addActionListener(new 29(this));
      this.btnMenuExit.setBorderPainted(false);
      this.btnMenuExit.setContentAreaFilled(false);
      this.btnMenuExit.setCursor(new Cursor(12));
      this.btnMenuExit.setOpaque(true);
      this.btnMenuExit.addActionListener(new 30(this));
      this.lbVersion.setFont(new Font("Segoe UI Light", 1, 12));
      this.lbVersion.setHorizontalAlignment(2);
      this.lbVersion.setText("#.#.#");
      this.btnShowDashboard.setToolTipText("Dashboard");
      this.btnShowDashboard.setBorderPainted(false);
      this.btnShowDashboard.setContentAreaFilled(false);
      this.btnShowDashboard.setCursor(new Cursor(12));
      this.btnShowDashboard.setOpaque(true);
      this.btnShowDashboard.addActionListener(new 31(this));
      this.btnRefreshDashboard.setToolTipText("Actualiser le dashboard");
      this.btnRefreshDashboard.setBorderPainted(false);
      this.btnRefreshDashboard.setContentAreaFilled(false);
      this.btnRefreshDashboard.setCursor(new Cursor(12));
      this.btnRefreshDashboard.setOpaque(true);
      this.btnRefreshDashboard.addActionListener(new 32(this));
      this.btnMaskDashboard.setToolTipText("Masquer le dashboard");
      this.btnMaskDashboard.setBorderPainted(false);
      this.btnMaskDashboard.setContentAreaFilled(false);
      this.btnMaskDashboard.setCursor(new Cursor(12));
      this.btnMaskDashboard.setOpaque(true);
      this.btnMaskDashboard.addActionListener(new 33(this));
      GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
      this.jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(1).add(jPanel3Layout.createSequentialGroup().add(9, 9, 9).add(this.btnMenu, -2, 39, -2).add(18, 18, 18).add(jPanel3Layout.createParallelGroup(1, false).add(this.jLabel5, -1, -1, 32767).add(this.jLabel2)).addPreferredGap(0).add(this.lbVersion, -2, 79, -2).addPreferredGap(0, -1, 32767).add(jPanel3Layout.createParallelGroup(1).add(jPanel3Layout.createSequentialGroup().add(45, 45, 45).add(this.btnMaskDashboard, -2, 39, -2)).add(this.btnRefreshDashboard, -2, 39, -2)).addPreferredGap(0).add(this.btnShowDashboard, -2, 39, -2).addPreferredGap(0).add(this.btnMenuExit, -2, 39, -2).add(14, 14, 14)));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(1).add(jPanel3Layout.createSequentialGroup().add(jPanel3Layout.createParallelGroup(3).add(this.jLabel2, -2, 38, -2).add(this.lbVersion)).addPreferredGap(0, -1, 32767).add(this.jLabel5)).add(jPanel3Layout.createSequentialGroup().add(this.btnMenu, -2, 51, -2).add(0, 0, 32767)).add(2, jPanel3Layout.createSequentialGroup().add(14, 14, 14).add(jPanel3Layout.createParallelGroup(2, false).add(this.btnRefreshDashboard, -1, 39, 32767).add(this.btnMaskDashboard, -1, -1, 32767).add(this.btnMenuExit, -1, -1, 32767).add(this.btnShowDashboard, -1, -1, 32767)).addContainerGap()));
      this.jPanel4.setBackground(new Color(0, 0, 51));
      this.lbLicence.setFont(new Font("Segoe UI Light", 1, 12));
      this.lbLicence.setForeground(new Color(255, 255, 255));
      this.lbLicence.setHorizontalAlignment(4);
      this.lbLicence.setText("Licence");
      this.lbNbSal.setFont(new Font("Segoe UI Light", 0, 12));
      this.lbNbSal.setForeground(new Color(255, 255, 255));
      this.lbNbSal.setHorizontalAlignment(4);
      this.lbNbSal.setText("NbSal");
      this.lbPC.setFont(new Font("Segoe UI Light", 1, 12));
      this.lbPC.setForeground(new Color(255, 255, 255));
      this.lbPC.setText("Periode courante");
      GroupLayout jPanel4Layout = new GroupLayout(this.jPanel4);
      this.jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(1).add(2, jPanel4Layout.createSequentialGroup().addContainerGap().add(this.lbPC, -2, 303, -2).addPreferredGap(0, -1, 32767).add(jPanel4Layout.createParallelGroup(1).add(2, this.lbNbSal, -2, 525, -2).add(2, this.lbLicence, -2, 948, -2)).add(20, 20, 20)));
      jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(1).add(jPanel4Layout.createSequentialGroup().add(jPanel4Layout.createParallelGroup(1).add(jPanel4Layout.createSequentialGroup().add(this.lbLicence, -1, -1, 32767).add(8, 8, 8).add(this.lbNbSal)).add(this.lbPC, -2, 38, -2)).add(2, 2, 2)));
      this.jDesktopPane.setBackground(new Color(0, 102, 153));
      this.pnlDashborad.setBackground(new Color(255, 255, 255));
      this.chartPanelGen.setBackground(new Color(255, 255, 255));
      this.chartPanelGen.setBorder(BorderFactory.createEtchedBorder());
      this.chartPanelGen.setLayout(new BorderLayout());
      this.lbSBrutCurrent.setFont(new Font("SansSerif", 1, 18));
      this.lbSBrutCurrent.setHorizontalAlignment(4);
      this.lbSBrutCurrent.setText("#");
      this.jLabel7.setFont(new Font("SansSerif", 1, 12));
      this.jLabel7.setText("Tranches d'\u00e2ges");
      this.chartPanelAge.setBackground(new Color(255, 255, 255));
      this.chartPanelAge.setBorder(BorderFactory.createEtchedBorder());
      this.chartPanelAge.setLayout(new BorderLayout());
      this.jLabel8.setFont(new Font("SansSerif", 1, 12));
      this.jLabel8.setText("Salaire par d\u00e9partements de la p\u00e9riode courante");
      this.chartPanelByDep.setBackground(new Color(255, 255, 255));
      this.chartPanelByDep.setBorder(BorderFactory.createEtchedBorder());
      this.chartPanelByDep.setLayout(new BorderLayout());
      this.jLabel9.setFont(new Font("SansSerif", 1, 12));
      this.jLabel9.setText("Genres");
      this.chartPanelGenre.setBackground(new Color(255, 255, 255));
      this.chartPanelGenre.setBorder(BorderFactory.createEtchedBorder());
      this.chartPanelGenre.setLayout(new BorderLayout());
      this.jLabel10.setFont(new Font("SansSerif", 1, 12));
      this.jLabel10.setText("Anciennet\u00e9");
      this.chartPanelAnc.setBackground(new Color(255, 255, 255));
      this.chartPanelAnc.setBorder(BorderFactory.createEtchedBorder());
      this.chartPanelAnc.setLayout(new BorderLayout());
      this.jLabel11.setFont(new Font("SansSerif", 1, 12));
      this.jLabel11.setText("Masse salariale de l'exercice courant");
      this.lbSalCount.setFont(new Font("SansSerif", 1, 18));
      this.lbSalCount.setText("#");
      this.lbCurrentNet.setFont(new Font("SansSerif", 1, 18));
      this.lbCurrentNet.setHorizontalAlignment(0);
      this.lbCurrentNet.setText("#");
      this.lbSBrutPrevious.setFont(new Font("SansSerif", 1, 18));
      this.lbSBrutPrevious.setForeground(new Color(102, 102, 102));
      this.lbSBrutPrevious.setText("#");
      this.lbNetPreviousPeriode1.setFont(new Font("SansSerif", 1, 10));
      this.lbNetPreviousPeriode1.setHorizontalAlignment(4);
      this.lbNetPreviousPeriode1.setText("Sal. Brut P\u00e9riode courante");
      this.lbNetPreviousPeriode2.setFont(new Font("SansSerif", 1, 10));
      this.lbNetPreviousPeriode2.setForeground(new Color(102, 102, 102));
      this.lbNetPreviousPeriode2.setText("Sal. Brut P\u00e9riode pr\u00e9c\u00e9dente");
      this.jSeparator1.setOrientation(1);
      this.lbNetPreviousPeriode3.setFont(new Font("SansSerif", 1, 10));
      this.lbNetPreviousPeriode3.setHorizontalAlignment(0);
      this.lbNetPreviousPeriode3.setText("Salaire net courant");
      this.jLabel12.setFont(new Font("SansSerif", 1, 12));
      this.jLabel12.setText("Effectif par postes");
      this.chartPanelEffByPoste.setBackground(new Color(255, 255, 255));
      this.chartPanelEffByPoste.setBorder(BorderFactory.createEtchedBorder());
      this.chartPanelEffByPoste.setLayout(new BorderLayout());
      GroupLayout pnlDashboradLayout = new GroupLayout(this.pnlDashborad);
      this.pnlDashborad.setLayout(pnlDashboradLayout);
      pnlDashboradLayout.setHorizontalGroup(pnlDashboradLayout.createParallelGroup(1).add(pnlDashboradLayout.createSequentialGroup().add(10, 10, 10).add(pnlDashboradLayout.createParallelGroup(1).add(2, pnlDashboradLayout.createSequentialGroup().add(pnlDashboradLayout.createParallelGroup(1).add(this.jLabel12, -2, 314, -2).add(this.chartPanelEffByPoste, -2, 504, -2)).addPreferredGap(0).add(pnlDashboradLayout.createParallelGroup(1).add(this.jLabel10, -2, 297, -2).add(this.chartPanelAnc, -2, 339, -2)).addPreferredGap(0).add(pnlDashboradLayout.createParallelGroup(1).add(this.jLabel7, -2, 298, -2).add(this.chartPanelAge, -2, 337, -2)).addPreferredGap(0).add(pnlDashboradLayout.createParallelGroup(1).add(this.jLabel9, -2, 222, -2).add(this.chartPanelGenre, -1, -1, 32767)).add(14, 14, 14)).add(pnlDashboradLayout.createSequentialGroup().add(pnlDashboradLayout.createParallelGroup(1).add(this.jLabel8, -2, 586, -2).add(this.chartPanelByDep, -2, 586, -2)).add(pnlDashboradLayout.createParallelGroup(1).add(pnlDashboradLayout.createSequentialGroup().add(192, 192, 192).add(pnlDashboradLayout.createParallelGroup(2).add(this.lbNetPreviousPeriode3, -2, 298, -2).add(pnlDashboradLayout.createSequentialGroup().add(this.lbNetPreviousPeriode1, -2, 161, -2).addPreferredGap(0).add(this.jSeparator1, -2, 11, -2).add(5, 5, 5).add(pnlDashboradLayout.createParallelGroup(1, false).add(this.lbNetPreviousPeriode2, -1, 157, 32767).add(this.lbSBrutPrevious, -1, -1, 32767))).add(this.lbCurrentNet, -2, 298, -2)).add(0, 0, 32767)).add(pnlDashboradLayout.createSequentialGroup().addPreferredGap(0).add(pnlDashboradLayout.createParallelGroup(1).add(pnlDashboradLayout.createSequentialGroup().add(this.chartPanelGen, -1, -1, 32767).add(14, 14, 14)).add(this.jLabel11, -1, -1, 32767))))).add(pnlDashboradLayout.createSequentialGroup().add(this.lbSalCount, -2, 586, -2).add(224, 224, 224).add(this.lbSBrutCurrent, -2, 129, -2).add(0, 0, 32767))).addContainerGap()));
      pnlDashboradLayout.setVerticalGroup(pnlDashboradLayout.createParallelGroup(1).add(pnlDashboradLayout.createSequentialGroup().add(20, 20, 20).add(pnlDashboradLayout.createParallelGroup(1).add(this.lbSalCount, -2, 30, -2).add(2, this.lbCurrentNet, -2, 30, -2)).add(pnlDashboradLayout.createParallelGroup(1).add(pnlDashboradLayout.createSequentialGroup().add(10, 10, 10).add(this.jLabel8)).add(pnlDashboradLayout.createSequentialGroup().addPreferredGap(0).add(this.lbNetPreviousPeriode3, -2, 14, -2))).add(5, 5, 5).add(pnlDashboradLayout.createParallelGroup(1).add(pnlDashboradLayout.createSequentialGroup().add(pnlDashboradLayout.createParallelGroup(1).add(pnlDashboradLayout.createSequentialGroup().add(pnlDashboradLayout.createParallelGroup(3).add(this.lbSBrutCurrent, -2, 30, -2).add(this.lbSBrutPrevious, -2, 30, -2)).addPreferredGap(0).add(pnlDashboradLayout.createParallelGroup(3).add(this.lbNetPreviousPeriode1, -2, 14, -2).add(this.lbNetPreviousPeriode2, -2, 14, -2))).add(pnlDashboradLayout.createSequentialGroup().add(2, 2, 2).add(this.jSeparator1, -2, 66, -2))).add(54, 54, 54).add(this.jLabel11).addPreferredGap(0).add(this.chartPanelGen, -2, 257, -2)).add(this.chartPanelByDep, -2, 400, -2)).add(pnlDashboradLayout.createParallelGroup(1).add(pnlDashboradLayout.createSequentialGroup().addPreferredGap(0).add(this.jLabel12)).add(pnlDashboradLayout.createSequentialGroup().add(6, 6, 6).add(this.jLabel7)).add(pnlDashboradLayout.createSequentialGroup().add(10, 10, 10).add(pnlDashboradLayout.createParallelGroup(1).add(this.jLabel10).add(this.jLabel9)))).add(2, 2, 2).add(pnlDashboradLayout.createParallelGroup(1).add(this.chartPanelGenre, -2, 230, -2).add(this.chartPanelAnc, -2, 229, -2).add(this.chartPanelEffByPoste, -2, 230, -2).add(this.chartPanelAge, -2, 230, -2)).addContainerGap(-1, 32767)));
      this.jDesktopPane.setLayer(this.pnlDashborad, JLayeredPane.DEFAULT_LAYER);
      GroupLayout jDesktopPaneLayout = new GroupLayout(this.jDesktopPane);
      this.jDesktopPane.setLayout(jDesktopPaneLayout);
      jDesktopPaneLayout.setHorizontalGroup(jDesktopPaneLayout.createParallelGroup(1).add(2, jDesktopPaneLayout.createSequentialGroup().add(5, 5, 5).add(this.pnlDashborad, -1, -1, 32767).addContainerGap()));
      jDesktopPaneLayout.setVerticalGroup(jDesktopPaneLayout.createParallelGroup(1).add(jDesktopPaneLayout.createSequentialGroup().add(5, 5, 5).add(this.pnlDashborad, -1, -1, 32767).addContainerGap()));
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().add(this.pnlMenu, -2, -1, -2).add(0, 0, 0).add(layout.createParallelGroup(1).add(2, this.jPanel3, -1, -1, 32767).add(this.jPanel4, -1, -1, 32767).add(this.jDesktopPane).add(this.progressBar, -1, -1, 32767))));
      layout.setVerticalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().add(this.jPanel3, -2, -1, -2).add(0, 0, 0).add(this.jPanel4, -2, -1, -2).add(0, 0, 0).add(this.progressBar, -2, -1, -2).add(0, 0, 0).add(this.jDesktopPane)).add(this.pnlMenu, -1, -1, 32767));
      this.pack();
   }

   public void showLoading() {
   }

   public void dismissLoading() {
   }

   private void btnMenuEmployesActionPerformed(ActionEvent var1) {
      Thread t = new 34(this);
      t.start();
   }

   private void btnMenuActionPerformed(ActionEvent var1) {
      this.pnlMenu.setVisible(!this.pnlMenu.isVisible());
   }

   private void btnMenuPwdActionPerformed(ActionEvent var1) {
      this.passwordIF.setVisible(true);
      this.jDesktopPane.remove(this.passwordIF);
      this.jDesktopPane.add(this.passwordIF);
      this.passwordIF.toFront();
   }

   private void btnMenuParametresActionPerformed(ActionEvent var1) {
      this.parametresIF.setVisible(true);
      this.jDesktopPane.remove(this.parametresIF);
      this.jDesktopPane.add(this.parametresIF);
      this.parametresIF.refresh();
      this.parametresIF.toFront();
      this.pnlMenu.setVisible(false);
   }

   private void btnMenuStricturesActionPerformed(ActionEvent var1) {
      this.stricturesIF.setVisible(true);
      this.jDesktopPane.remove(this.stricturesIF);
      this.jDesktopPane.add(this.stricturesIF);
      this.stricturesIF.toFront();
      this.pnlMenu.setVisible(false);
   }

   private void btnMenuQuerySalActionPerformed(ActionEvent var1) {
      Thread t = new 35(this);
      t.start();
   }

   private void btnMenuPointageActionPerformed(ActionEvent var1) {
      if (this.demo) {
         this.showDemoMsg();
      } else {
         this.pointageFileImportFrame.setVisible(true);
         this.jDesktopPane.remove(this.pointageFileImportFrame);
         this.jDesktopPane.add(this.pointageFileImportFrame);
         this.pointageFileImportFrame.refresh();
         this.pointageFileImportFrame.toFront();
         this.pnlMenu.setVisible(false);
      }

   }

   private void btnMenuCalculPaieActionPerformed(ActionEvent var1) {
      this.paiegeneraleFrame.setVisible(true);
      this.jDesktopPane.remove(this.paiegeneraleFrame);
      this.jDesktopPane.add(this.paiegeneraleFrame);
      this.paiegeneraleFrame.refresh();
      this.paiegeneraleFrame.toFront();
      this.pnlMenu.setVisible(false);
   }

   private void btnMenuClotureActionPerformed(ActionEvent var1) {
      if (this.grantNewVersionAccess) {
         this.clotureFrame.refresh();
         this.clotureFrame.setVisible(true);
         this.jDesktopPane.remove(this.clotureFrame);
         this.jDesktopPane.add(this.clotureFrame);
         this.clotureFrame.toFront();
         this.pnlMenu.setVisible(false);
      } else {
         this.showDemoMsg();
      }

   }

   private void btnMenuStatistiquesActionPerformed(ActionEvent var1) {
      this.statistiqueFrame.setVisible(true);
      this.jDesktopPane.remove(this.statistiqueFrame);
      this.jDesktopPane.add(this.statistiqueFrame);
      this.statistiqueFrame.refresh();
      this.statistiqueFrame.toFront();
      this.pnlMenu.setVisible(false);
   }

   private void btnMenuRubriquesActionPerformed(ActionEvent var1) {
      this.rubriqueFrame.setVisible(true);
      this.jDesktopPane.remove(this.rubriqueFrame);
      this.jDesktopPane.add(this.rubriqueFrame);
      this.rubriqueFrame.refresh();
      this.rubriqueFrame.toFront();
      this.pnlMenu.setVisible(false);
   }

   private void btnMenuFxActionPerformed(ActionEvent var1) {
      this.fxFrame.refresh();
      this.fxFrame.setVisible(true);
      this.jDesktopPane.remove(this.fxFrame);
      this.jDesktopPane.add(this.fxFrame);
      this.fxFrame.toFront();
      this.pnlMenu.setVisible(false);
   }

   private void btnMenuImportActionPerformed(ActionEvent var1) {
      this.fileImportFrame.setVisible(true);
      this.jDesktopPane.remove(this.fileImportFrame);
      this.jDesktopPane.add(this.fileImportFrame);
      this.fileImportFrame.refresh();
      this.fileImportFrame.toFront();
      this.pnlMenu.setVisible(false);
   }

   private void btnMenuSecurityActionPerformed(ActionEvent var1) {
      this.utilisateursFrame.setVisible(true);
      this.jDesktopPane.remove(this.utilisateursFrame);
      this.jDesktopPane.add(this.utilisateursFrame);
      this.utilisateursFrame.refresh();
      this.utilisateursFrame.toFront();
      this.pnlMenu.setVisible(false);
   }

   private void btnMenuExitActionPerformed(ActionEvent var1) {
      System.exit(0);
   }

   private void btnMenuRepportActionPerformed(ActionEvent var1) {
      this.repport.setVisible(true);
      this.jDesktopPane.remove(this.repport);
      this.jDesktopPane.add(this.repport);
      this.repport.refresh();
      this.repport.toFront();
      this.pnlMenu.setVisible(false);
   }

   private void btnMaskDashboardActionPerformed(ActionEvent var1) {
      this.pnlDashborad.setVisible(false);
   }

   private void btnShowDashboardActionPerformed(ActionEvent var1) {
      this.pnlDashborad.setVisible(true);
      this.showDashboardData();
   }

   private void btnRefreshDashboardActionPerformed(ActionEvent var1) {
      this.showDashboardData();
   }

   private void btnMenuEmployesMouseEntered(MouseEvent var1) {
      this.btnMenuEmployes.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuEmployesMouseExited(MouseEvent var1) {
      this.btnMenuEmployes.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuImportMouseEntered(MouseEvent var1) {
      this.btnMenuImport.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuImportMouseExited(MouseEvent var1) {
      this.btnMenuImport.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuPointageMouseEntered(MouseEvent var1) {
      this.btnMenuPointage.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuPointageMouseExited(MouseEvent var1) {
      this.btnMenuPointage.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuCalculPaieMouseEntered(MouseEvent var1) {
      this.btnMenuCalculPaie.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuCalculPaieMouseExited(MouseEvent var1) {
      this.btnMenuCalculPaie.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuRepportMouseEntered(MouseEvent var1) {
      this.btnMenuRepport.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuRepportMouseExited(MouseEvent var1) {
      this.btnMenuRepport.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuStatistiquesMouseEntered(MouseEvent var1) {
      this.btnMenuStatistiques.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuStatistiquesMouseExited(MouseEvent var1) {
      this.btnMenuStatistiques.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuClotureMouseEntered(MouseEvent var1) {
      this.btnMenuCloture.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuClotureMouseExited(MouseEvent var1) {
      this.btnMenuCloture.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuFxMouseEntered(MouseEvent var1) {
      this.btnMenuFx.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuFxMouseExited(MouseEvent var1) {
      this.btnMenuFx.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuQuerySalMouseEntered(MouseEvent var1) {
      this.btnMenuQuerySal.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuQuerySalMouseExited(MouseEvent var1) {
      this.btnMenuQuerySal.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuRubriquesMouseEntered(MouseEvent var1) {
      this.btnMenuRubriques.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuRubriquesMouseExited(MouseEvent var1) {
      this.btnMenuRubriques.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuStricturesMouseEntered(MouseEvent var1) {
      this.btnMenuStrictures.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuStricturesMouseExited(MouseEvent var1) {
      this.btnMenuStrictures.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuSecurityMouseEntered(MouseEvent var1) {
      this.btnMenuSecurity.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuSecurityMouseExited(MouseEvent var1) {
      this.btnMenuSecurity.setBackground(new Color(0, 0, 51));
   }

   private void btnMenuParametresMouseEntered(MouseEvent var1) {
      this.btnMenuParametres.setBackground(new Color(0, 102, 153));
   }

   private void btnMenuParametresMouseExited(MouseEvent var1) {
      this.btnMenuParametres.setBackground(new Color(0, 0, 51));
   }

   public static void main(String[] var0) {
      try {
         for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Windows".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (ClassNotFoundException ex) {
         Logger.getLogger(menu.class.getName()).log(Level.SEVERE, (String)null, ex);
      } catch (InstantiationException ex) {
         Logger.getLogger(menu.class.getName()).log(Level.SEVERE, (String)null, ex);
      } catch (IllegalAccessException ex) {
         Logger.getLogger(menu.class.getName()).log(Level.SEVERE, (String)null, ex);
      } catch (UnsupportedLookAndFeelException ex) {
         Logger.getLogger(menu.class.getName()).log(Level.SEVERE, (String)null, ex);
      }

      EventQueue.invokeLater(new 36());
   }
}
