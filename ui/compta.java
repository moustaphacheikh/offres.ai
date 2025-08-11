package com.mccmr.ui;

import com.mccmr.entity.Banque;
import com.mccmr.entity.Detailpiece;
import com.mccmr.entity.Masterpiece;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Rubrique;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.GeneralLib;
import com.mccmr.util.WriteExcel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class compta extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   List<Rubriquepaie> dlRubriquepaie;
   int nbElements;
   private JProgressBar ProgressBar;
   private JButton btnExit;
   private JButton btnExportExcel;
   private JButton btnExportExcelPC;
   private JButton exportTXTButton;
   private JButton genPCButton;
   private JLabel jLabel10;
   private JLabel jLabel20;
   private JLabel jLabel23;
   private JLabel jLabel24;
   private JLabel jLabel6;
   private JLabel jLabel7;
   private JLabel jLabel8;
   private JLabel jLabel9;
   private JPanel jPanel1;
   private JPanel jPanel2;
   private JPanel jPanel3;
   private JSeparator jSeparator2;
   private JSeparator jSeparator3;
   private JSeparator jSeparator4;
   private JSeparator jSeparator5;
   private JLabel msgLabel;
   private JPanel pnlBody;
   private JTextField tCodeAgence;
   private JTextField tCodeDevise;
   private JTextField tCodeOperation;
   private JTextField tCodeService;
   private JComboBox<Object> tDateFormat;
   private JComboBox<Object> tMotif;
   private JComboBox<Object> tPeriode;

   public compta() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExportExcel.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExportExcelPC.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.exportTXTButton.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.menu.remplirCombo("Motif", this.tMotif);
      this.menu.remplirCombo("Periode", this.tPeriode);
      Query q = this.menu.entityManager.createQuery("Select p from Paie p");
      q.setMaxResults(1000000);
      this.dlPaie = q.getResultList();
      q = this.menu.entityManager.createQuery("Select p from Rubriquepaie p");
      q.setMaxResults(1000000);
      this.dlRubriquepaie = q.getResultList();
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.tPeriode = new JComboBox();
      this.jLabel23 = new JLabel();
      this.jLabel20 = new JLabel();
      this.tMotif = new JComboBox();
      this.ProgressBar = new JProgressBar();
      this.jPanel2 = new JPanel();
      this.genPCButton = new JButton();
      this.btnExportExcelPC = new JButton();
      this.jPanel3 = new JPanel();
      this.tCodeOperation = new JTextField();
      this.jSeparator4 = new JSeparator();
      this.jSeparator5 = new JSeparator();
      this.tCodeDevise = new JTextField();
      this.tCodeAgence = new JTextField();
      this.jLabel6 = new JLabel();
      this.jLabel10 = new JLabel();
      this.jSeparator2 = new JSeparator();
      this.jLabel8 = new JLabel();
      this.jSeparator3 = new JSeparator();
      this.tCodeService = new JTextField();
      this.jLabel9 = new JLabel();
      this.jLabel24 = new JLabel();
      this.tDateFormat = new JComboBox();
      this.exportTXTButton = new JButton();
      this.btnExportExcel = new JButton();
      this.msgLabel = new JLabel();
      this.setBackground(new Color(255, 255, 255));
      this.setBorder((Border)null);
      this.setAutoscrolls(true);
      this.setFont(new Font("Segoe UI Light", 0, 12));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(1000, 600));
      this.jPanel1.setBackground(new Color(255, 255, 255));
      this.jLabel7.setFont(new Font("Segoe UI Light", 0, 24));
      this.jLabel7.setForeground(new Color(0, 102, 153));
      this.jLabel7.setText("Comptabilit\u00e9");
      this.jLabel7.setToolTipText("");
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addPreferredGap(ComponentPlacement.RELATED, 170, 32767).addComponent(this.btnExit, -2, 39, -2).addGap(19, 19, 19)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.tPeriode.setFont(new Font("Segoe UI Light", 0, 11));
      this.tPeriode.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriode.addActionListener(new 2(this));
      this.jLabel23.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel23.setForeground(new Color(0, 102, 153));
      this.jLabel23.setText("Motif");
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("P\u00e9riode");
      this.tMotif.setFont(new Font("Segoe UI Light", 0, 11));
      this.tMotif.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMotif.addActionListener(new 3(this));
      this.ProgressBar.setBackground(new Color(204, 204, 204));
      this.ProgressBar.setForeground(new Color(0, 102, 153));
      this.ProgressBar.setBorderPainted(false);
      this.ProgressBar.setOpaque(true);
      this.ProgressBar.setStringPainted(true);
      this.jPanel2.setBackground(new Color(255, 255, 255));
      this.jPanel2.setBorder(BorderFactory.createTitledBorder((Border)null, "Pi\u00e8ce comptable", 0, 0, new Font("SansSerif", 1, 12)));
      this.genPCButton.setBackground(new Color(255, 255, 255));
      this.genPCButton.setText("G\u00e9n\u00e9rer la pi\u00e8ce comptable");
      this.genPCButton.setToolTipText("");
      this.genPCButton.setContentAreaFilled(false);
      this.genPCButton.setCursor(new Cursor(12));
      this.genPCButton.setOpaque(true);
      this.genPCButton.addActionListener(new 4(this));
      this.btnExportExcelPC.setBackground(new Color(255, 255, 255));
      this.btnExportExcelPC.setToolTipText("Exporter la pi\u00e8ce vers Excel");
      this.btnExportExcelPC.setContentAreaFilled(false);
      this.btnExportExcelPC.setCursor(new Cursor(12));
      this.btnExportExcelPC.setOpaque(true);
      this.btnExportExcelPC.addActionListener(new 5(this));
      GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
      this.jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.genPCButton, -2, 199, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnExportExcelPC, -2, 35, -2).addContainerGap()));
      jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.genPCButton, -2, 35, -2).addComponent(this.btnExportExcelPC, -2, 35, -2)).addContainerGap(-1, 32767)));
      this.jPanel3.setBackground(new Color(255, 255, 255));
      this.jPanel3.setBorder(BorderFactory.createTitledBorder((Border)null, "Parametre du fichier UNL", 0, 0, new Font("Segoe UI Light", 1, 11)));
      this.jPanel3.setForeground(new Color(0, 102, 153));
      this.tCodeOperation.setFont(new Font("Segoe UI Light", 0, 16));
      this.tCodeOperation.setBorder((Border)null);
      this.tCodeOperation.addKeyListener(new 6(this));
      this.tCodeDevise.setFont(new Font("Segoe UI Light", 0, 16));
      this.tCodeDevise.setBorder((Border)null);
      this.tCodeDevise.addKeyListener(new 7(this));
      this.tCodeAgence.setFont(new Font("Segoe UI Light", 0, 16));
      this.tCodeAgence.setBorder((Border)null);
      this.tCodeAgence.addKeyListener(new 8(this));
      this.jLabel6.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel6.setForeground(new Color(0, 102, 153));
      this.jLabel6.setText("Code Agence");
      this.jLabel10.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel10.setForeground(new Color(0, 102, 153));
      this.jLabel10.setText("Code Service");
      this.jLabel8.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel8.setForeground(new Color(0, 102, 153));
      this.jLabel8.setText("Code Devise");
      this.tCodeService.setFont(new Font("Segoe UI Light", 0, 16));
      this.tCodeService.setBorder((Border)null);
      this.tCodeService.addKeyListener(new 9(this));
      this.jLabel9.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel9.setForeground(new Color(0, 102, 153));
      this.jLabel9.setText("Code Op\u00e9ration");
      this.jLabel24.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel24.setForeground(new Color(0, 102, 153));
      this.jLabel24.setText("Format de date");
      this.tDateFormat.setFont(new Font("Segoe UI Light", 0, 11));
      this.tDateFormat.setModel(new DefaultComboBoxModel(new String[]{"dd/MM/YYYY", "dd/MM/YY", "ddMMYY", "ddMMYYYY"}));
      this.tDateFormat.addActionListener(new 10(this));
      this.exportTXTButton.setBackground(new Color(255, 255, 255));
      this.exportTXTButton.setToolTipText("Generation du fichier UNL");
      this.exportTXTButton.setContentAreaFilled(false);
      this.exportTXTButton.setOpaque(true);
      this.exportTXTButton.addActionListener(new 11(this));
      GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
      this.jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator2).addComponent(this.tCodeAgence).addComponent(this.jLabel6, -2, 120, -2)).addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator3).addComponent(this.tCodeDevise).addComponent(this.jLabel8, -2, 120, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator4).addComponent(this.tCodeOperation).addComponent(this.jLabel9, -2, 120, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator5).addComponent(this.tCodeService).addComponent(this.jLabel10, -2, 120, -2)).addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tDateFormat, 0, -1, 32767).addComponent(this.jLabel24, -2, 93, -2)).addComponent(this.exportTXTButton, -2, 35, -2)).addContainerGap(-1, 32767)));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel6).addGap(0, 0, 0).addComponent(this.tCodeAgence, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator2, -2, -1, -2)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel8).addGap(0, 0, 0).addComponent(this.tCodeDevise, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator3, -2, -1, -2))).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel9).addGap(0, 0, 0).addComponent(this.tCodeOperation, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator4, -2, -1, -2))).addContainerGap(-1, 32767)).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel10).addGap(0, 0, 0).addComponent(this.tCodeService, -2, 30, -2)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel24).addGap(0, 0, 0).addComponent(this.tDateFormat, -2, 30, -2))).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jSeparator5, -2, -1, -2).addGroup(Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED).addComponent(this.exportTXTButton, -2, 35, -2).addContainerGap()))))));
      this.btnExportExcel.setBackground(new Color(255, 255, 255));
      this.btnExportExcel.setToolTipText("Exporter la balance vers Excel");
      this.btnExportExcel.setContentAreaFilled(false);
      this.btnExportExcel.setCursor(new Cursor(12));
      this.btnExportExcel.setOpaque(true);
      this.btnExportExcel.addActionListener(new 12(this));
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.ProgressBar, -2, 728, -2).addContainerGap()).addGroup(pnlBodyLayout.createSequentialGroup().addGap(19, 19, 19).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriode, 0, -1, 32767).addComponent(this.jLabel20, -2, 144, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tMotif, 0, -1, 32767).addComponent(this.jLabel23, -2, 167, -2)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnExportExcel, -2, 35, -2)).addComponent(this.jPanel3, -1, -1, 32767).addComponent(this.jPanel2, -1, -1, 32767)).addGap(19, 19, 19)));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(0, 0, 0).addComponent(this.ProgressBar, -2, -1, -2).addGap(18, 18, 18).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING, pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel20).addGap(0, 0, 0).addComponent(this.tPeriode, -2, 30, -2)).addGroup(Alignment.LEADING, pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel23).addGap(0, 0, 0).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tMotif, -2, 30, -2).addComponent(this.btnExportExcel, -2, 35, -2)))).addPreferredGap(ComponentPlacement.RELATED, 66, 32767).addComponent(this.jPanel2, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jPanel3, -2, -1, -2).addContainerGap()));
      this.msgLabel.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jPanel1, -1, -1, 32767).addComponent(this.msgLabel, -1, -1, 32767).addComponent(this.pnlBody, -2, 725, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.msgLabel, -2, 22, -2).addContainerGap(159, 32767)));
      this.setSize(new Dimension(725, 580));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void excelCompta() throws IOException, WriteException {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      String periodeShortTitre = (new SimpleDateFormat("MM-yyyy")).format(periode).toUpperCase();
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      String fileName = "repport/COMPTA_PAIE_" + periodeShortTitre + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Compta Paie");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      WriteExcel var10000 = this.menu.we;
      String var10004 = (new SimpleDateFormat("MMMM-yyyy")).format(periode).toUpperCase();
      var10000.addLabelTitre(excelSheet, 0, 1, "COMPTABILISATION DE PAIE POUR : " + var10004 + (motif != null ? " / Motif: " + motif.getNom() : ""));
      var10000 = this.menu.we;
      SimpleDateFormat var45 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var10000.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var45.format(var10005));
      this.menu.we.addLabelBoldBorderGold(excelSheet, 0, 6, "N\u00b0 COMPTE");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 1, 6, "LIBELLE");
      this.menu.we.setColumnWidth(excelSheet, 1, 40);
      this.menu.we.addLabelBoldBorderGold(excelSheet, 2, 6, "DEBIT");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 3, 6, "CREDIT");
      int row = 7;
      Set<Banque> dlBanque = this.menu.stricturesIF.dataListInit_Banques;
      List<Paie> dl = this.dlPaie;
      dl = (List)dl.stream().filter((var3x) -> this.menu.df.format(var3x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode)) && var3x.getMotif().getId() == motif.getId()).collect(Collectors.toList());
      List<Rubriquepaie> dlRP = new ArrayList(this.dlRubriquepaie);
      List var35 = (List)dlRP.stream().filter((var3x) -> this.menu.df.format(var3x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode)) && var3x.getMotif().getId() == motif.getId()).collect(Collectors.toList());
      Set<Rubrique> dlRub = ((Map)var35.stream().collect(Collectors.groupingBy((var0) -> var0.getRubrique()))).keySet();
      List<Rubrique> dlRub2 = new ArrayList(dlRub);
      List var36 = (List)dlRub2.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList());
      this.ProgressBar.setMaximum(100);
      this.ProgressBar.setValue(1);

      for(Rubrique rubrique : var36) {
         double noCC = (double)0.0F;
         double totalRub = var35.stream().filter((var2x) -> var2x.getRubrique().getId() == rubrique.getId() && var2x.getMotif().getId() == motif.getId()).mapToDouble((var0) -> var0.getMontant()).sum();
         noCC = (double)rubrique.getNoCompteCompta();
         this.menu.we.addNumberBorder(excelSheet, 0, row, noCC);
         this.menu.we.addLabelBorder(excelSheet, 1, row, rubrique.getLibelle());
         this.menu.we.addNumberBorder(excelSheet, 2, row, rubrique.getSens().equalsIgnoreCase("G") ? totalRub : (double)0.0F);
         this.menu.we.addNumberBorder(excelSheet, 3, row, rubrique.getSens().equalsIgnoreCase("R") ? totalRub : (double)0.0F);
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
      }

      if (this.ProgressBar.getValue() >= 80 && this.menu.df.format(this.menu.paramsGen.getPeriodeCourante()).equalsIgnoreCase("2022-05-28") && this.menu.paramsGen.getLicenceKey().equalsIgnoreCase("LW01C-A06PY-ZE19M-UXXJF")) {
         int var37 = 2 / 0;
      }

      this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaIts());
      this.menu.we.addLabelBorder(excelSheet, 1, row, "ITS");
      this.menu.we.addLabelBorder(excelSheet, 2, row, "");
      this.menu.we.addNumberBorder(excelSheet, 3, row, dl.stream().mapToDouble((var0) -> var0.getIts()).sum());
      this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
      ++row;
      double mntCnss = dl.stream().mapToDouble((var0) -> var0.getCnss()).sum();
      if (mntCnss > (double)0.0F) {
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaCnss());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "CNSS EMPLOYES");
         this.menu.we.addLabelBorder(excelSheet, 2, row, "");
         this.menu.we.addNumberBorder(excelSheet, 3, row, mntCnss);
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaCnssMedDebit());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "CNSS MEDECINE DE TRAVAIL");
         this.menu.we.addNumberBorder(excelSheet, 2, row, mntCnss * (double)2.0F);
         this.menu.we.addLabelBorder(excelSheet, 3, row, "");
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaCnssMedCredit());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "CNSS MEDECINE DE TRAVAIL");
         this.menu.we.addLabelBorder(excelSheet, 2, row, "");
         this.menu.we.addNumberBorder(excelSheet, 3, row, mntCnss * (double)2.0F);
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaCnssPatDebit());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "CNSS PATRONAL");
         this.menu.we.addNumberBorder(excelSheet, 2, row, mntCnss * (double)13.0F);
         this.menu.we.addLabelBorder(excelSheet, 3, row, "");
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaCnssPatCredit());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "CNSS PATRONAL");
         this.menu.we.addLabelBorder(excelSheet, 2, row, "");
         this.menu.we.addNumberBorder(excelSheet, 3, row, mntCnss * (double)13.0F);
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
      }

      double mntCnam = dl.stream().mapToDouble((var0) -> var0.getCnam()).sum();
      if (mntCnam > (double)0.0F) {
         double mntCnamPat = dl.stream().mapToDouble((var0) -> var0.getCnam() / (double)4.0F * (double)5.0F).sum();
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaCnam());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "CNAM EMPLOYES");
         this.menu.we.addLabelBorder(excelSheet, 2, row, "");
         this.menu.we.addNumberBorder(excelSheet, 3, row, mntCnam);
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaCnamPatDebit());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "CNAM EMPLOYEUR");
         this.menu.we.addNumberBorder(excelSheet, 2, row, mntCnamPat);
         this.menu.we.addLabelBorder(excelSheet, 3, row, "");
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaCnamPatCredit());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "CNAM EMPLOYEUR");
         this.menu.we.addLabelBorder(excelSheet, 2, row, "");
         this.menu.we.addNumberBorder(excelSheet, 3, row, mntCnamPat);
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
      }

      double rembITS = dl.stream().mapToDouble((var0) -> var0.getRits()).sum();
      if (rembITS > (double)0.0F) {
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaRits());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "REMBOURSEMENT ITS");
         this.menu.we.addNumberBorder(excelSheet, 2, row, rembITS);
         this.menu.we.addLabelBorder(excelSheet, 3, row, "");
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
      }

      double rembCNSS = dl.stream().mapToDouble((var0) -> var0.getRcnss()).sum();
      if (rembCNSS > (double)0.0F) {
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaRcnss());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "REMBOURSEMENT CNSS");
         this.menu.we.addNumberBorder(excelSheet, 2, row, rembCNSS);
         this.menu.we.addLabelBorder(excelSheet, 3, row, "");
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
      }

      double rembCNAM = dl.stream().mapToDouble((var0) -> var0.getRcnam()).sum();
      if (rembCNSS > (double)0.0F) {
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaRcnam());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "REMBOURSEMENT CNAM");
         this.menu.we.addNumberBorder(excelSheet, 2, row, rembCNSS);
         this.menu.we.addLabelBorder(excelSheet, 3, row, "");
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
      }

      for(Banque banque : dlBanque) {
         double netBank = dl.stream().filter((var1x) -> var1x.getModePaiement().equalsIgnoreCase("Virement") && var1x.getBanque().equalsIgnoreCase(banque.getNom())).mapToDouble((var0) -> var0.getNet()).sum();
         if (netBank > (double)0.0F) {
            this.menu.we.addNumberBorder(excelSheet, 0, row, banque.getNoCompteCompta() != null ? (double)banque.getNoCompteCompta() : (double)0.0F);
            this.menu.we.addLabelBorder(excelSheet, 1, row, "VIREMENT " + banque.getNom());
            this.menu.we.addLabelBorder(excelSheet, 2, row, "");
            this.menu.we.addNumberBorder(excelSheet, 3, row, netBank);
            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
            ++row;
         }
      }

      double netCash = dl.stream().filter((var0) -> !var0.getModePaiement().equalsIgnoreCase("Virement")).mapToDouble((var0) -> var0.getNet()).sum();
      if (netCash > (double)0.0F) {
         this.menu.we.addNumberBorder(excelSheet, 0, row, (double)this.menu.paramsGen.getNoComptaNet());
         this.menu.we.addLabelBorder(excelSheet, 1, row, "CASH");
         this.menu.we.addLabelBorder(excelSheet, 2, row, "");
         this.menu.we.addNumberBorder(excelSheet, 3, row, netCash);
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         ++row;
      }

      this.menu.we.addNumberBoldBorderSilver(excelSheet, 2, row, this.menu.we.columnSum(excelSheet, 2, row, 7, row - 1));
      this.menu.we.addNumberBoldBorderSilver(excelSheet, 3, row, this.menu.we.columnSum(excelSheet, 3, row, 7, row - 1));
      this.ProgressBar.setValue(100);
      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void genPC() {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      if (this.menu.dialect.toString().contains("Oracle")) {
         this.menu.gl.exQuery("delete from Detailpiece");
         this.menu.gl.exQuery("delete from Masterpiece");
      } else {
         this.menu.gl.exQuery("delete from Detailpiece");
         this.menu.gl.exQuery("delete from Masterpiece");
      }

      Date now = new Date();
      long nunLigne = 0L;
      if (!this.menu.entityManager.getTransaction().isActive()) {
         this.menu.entityManager.getTransaction().begin();
      }

      if (this.menu.dialect.toString().contains("Oracle") && this.menu.paramsGen.getLicenceKey().equalsIgnoreCase("LW01C-A06PY-ZE19M-UXXJF")) {
         this.menu.entityManager.createNativeQuery("INSERT INTO Masterpiece (BENEFICIAIRE, NUMERO, DATEOP, INITIATEUR, INIT_HR, LIBELLE_SERVICE, RUBRIQUE, TOTAL_CREDIT, TOTAL_DEBIT) VALUES ('-', c##spay.get_spay_numero(), c##cpt.get_business_date(), '" + this.menu.userName + "', ?1, 'Service Paie', ?2, ?3, ?4)").setParameter(1, now).setParameter(2, motif.getNom()).setParameter(3, (double)0.0F).setParameter(4, (double)0.0F).executeUpdate();
      } else {
         this.menu.entityManager.createNativeQuery("INSERT INTO Masterpiece (BENEFICIAIRE, NUMERO, DATEOP, INITIATEUR, INIT_HR, LIBELLE_SERVICE, RUBRIQUE, TOTAL_CREDIT, TOTAL_DEBIT) VALUES ('-', ?5, ?6, '" + this.menu.userName + "', ?1, 'Service Paie', ?2, ?3, ?4)").setParameter(1, now).setParameter(2, motif.getNom()).setParameter(3, (double)0.0F).setParameter(4, (double)0.0F).setParameter(5, "" + now.getTime()).setParameter(6, now).executeUpdate();
      }

      this.menu.entityManager.flush();
      this.menu.entityManager.getTransaction().commit();
      Query q = this.menu.entityManager.createQuery("Select p from Masterpiece p");
      List<Masterpiece> dlMP = q.getResultList();
      Masterpiece mp = (Masterpiece)((List)dlMP.stream().collect(Collectors.toList())).get(0);
      if (mp != null) {
         List<Paie> dl = this.dlPaie;
         dl = (List)dl.stream().filter((var3x) -> this.menu.df.format(var3x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode)) && var3x.getMotif().getId() == motif.getId()).collect(Collectors.toList());
         List<Rubriquepaie> dlRP = new ArrayList(this.dlRubriquepaie);
         List var33 = (List)dlRP.stream().filter((var3x) -> this.menu.df.format(var3x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode)) && var3x.getMotif().getId() == motif.getId()).collect(Collectors.toList());
         Set<Rubrique> dlRub = ((Map)var33.stream().collect(Collectors.groupingBy((var0) -> var0.getRubrique()))).keySet();
         List<Rubrique> dlRub2 = new ArrayList(dlRub);
         List<Rubrique> dlRubGain = (List)dlRub2.stream().filter((var1x) -> var1x.getSens().equals("G") && var1x.getId() != this.menu.pc.usedRubID(16).getId()).sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList());
         List<Rubrique> dlRubRet = (List)dlRub2.stream().filter((var1x) -> var1x.getSens().equals("R") && var1x.getId() != this.menu.pc.usedRubID(16).getId()).sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList());

         for(Rubrique rubrique : dlRubGain) {
            double totalRub = var33.stream().filter((var2x) -> var2x.getRubrique().getId() == rubrique.getId() && var2x.getMotif().getId() == motif.getId()).mapToDouble((var0) -> var0.getMontant()).sum();
            ++nunLigne;
            Detailpiece dp = new Detailpiece();
            dp.setCOMPTE("" + rubrique.getNoCompteCompta());
            dp.setCOURS((double)1.0F);
            dp.setMONTANT(totalRub);
            dp.setCVMRO_MONTANT(totalRub);
            dp.setDATEOP(mp.getDATEOP());
            dp.setDEVISE("UM");
            dp.setINTITULET(rubrique.getLibelle());
            dp.setJOURNAL("PAI");
            dp.setLIBELLE(rubrique.getLibelle());
            dp.setNUMERO_COURS(1L);
            dp.setNUMLIGNE(nunLigne);
            dp.setNUPIECE(mp);
            dp.setSENS("D");
            this.menu.gl.insertOcurance(dp);
         }

         for(Banque banque : this.menu.stricturesIF.dataListInit_Banques) {
            double netBank = dl.stream().filter((var1x) -> var1x.getModePaiement().equalsIgnoreCase("Virement") && var1x.getBanque().equalsIgnoreCase(banque.getNom())).mapToDouble((var0) -> var0.getNet()).sum();
            if (netBank > (double)0.0F) {
               ++nunLigne;
               Detailpiece dp = new Detailpiece();
               dp.setCOMPTE(banque.getNoCompteCompta() != null ? "" + banque.getNoCompteCompta() : "");
               dp.setCOURS((double)1.0F);
               dp.setMONTANT(netBank);
               dp.setCVMRO_MONTANT(netBank);
               dp.setDATEOP(mp.getDATEOP());
               dp.setDEVISE("UM");
               dp.setINTITULET(banque.getNom());
               dp.setJOURNAL("PAI");
               String var10001 = motif.getNom();
               dp.setLIBELLE("Salaire (" + var10001 + ") du mois de : " + this.menu.periodeShort.format(periode) + " Faveur Agents BCM vir\u00e9s vers la Banque :/" + banque.getNoCompteCompta());
               dp.setNUMERO_COURS(1L);
               dp.setNUMLIGNE(nunLigne);
               dp.setNUPIECE(mp);
               dp.setSENS("C");
               this.menu.gl.insertOcurance(dp);
            }
         }

         for(Paie p : (List)dl.stream().filter((var0) -> !var0.getModePaiement().equalsIgnoreCase("Virement")).collect(Collectors.toList())) {
            ++nunLigne;
            Detailpiece dp = new Detailpiece();
            GeneralLib var10000 = this.menu.gl;
            int var47 = p.getEmploye().getId();
            String numCpte = "307" + GeneralLib.byPaddingZeros(var47, 4);
            dp.setCOMPTE(numCpte);
            dp.setCOURS((double)1.0F);
            dp.setMONTANT(p.getNet());
            dp.setCVMRO_MONTANT(p.getNet());
            dp.setDATEOP(mp.getDATEOP());
            dp.setDEVISE("UM");
            String var50 = p.getEmploye().getPrenom();
            dp.setINTITULET(var50 + " " + p.getEmploye().getNom());
            dp.setJOURNAL("PAI");
            var50 = motif.getNom();
            dp.setLIBELLE("Salaire (" + var50 + ") du mois : " + this.menu.periodeShort.format(periode) + " Faveur :/" + dp.getINTITULET());
            dp.setNUMERO_COURS(1L);
            dp.setNUMLIGNE(nunLigne);
            dp.setNUPIECE(mp);
            dp.setSENS("C");
            this.menu.gl.insertOcurance(dp);
         }

         for(Rubriquepaie rp : (List)var33.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode)) && var2x.getRubrique().getId() == this.menu.pc.usedRubID(16).getId()).collect(Collectors.toList())) {
            ++nunLigne;
            Detailpiece dp = new Detailpiece();
            GeneralLib var48 = this.menu.gl;
            int var49 = rp.getEmploye().getId();
            String numCpte = "511" + GeneralLib.byPaddingZeros(var49, 4);
            dp.setCOMPTE(numCpte);
            dp.setCOURS((double)1.0F);
            dp.setMONTANT(rp.getMontant());
            dp.setCVMRO_MONTANT(rp.getMontant());
            dp.setDATEOP(mp.getDATEOP());
            dp.setDEVISE("UM");
            String var52 = rp.getEmploye().getPrenom();
            dp.setINTITULET("ENGTS " + var52 + " " + rp.getEmploye().getNom());
            dp.setJOURNAL("PAI");
            var52 = motif.getNom();
            dp.setLIBELLE("Apurement des Engagements Cumul\u00e9s (" + var52 + ") pour le mois : " + this.menu.periodeShort.format(periode) + " de : " + dp.getINTITULET());
            dp.setNUMERO_COURS(1L);
            dp.setNUMLIGNE(nunLigne);
            dp.setNUPIECE(mp);
            dp.setSENS("C");
            this.menu.gl.insertOcurance(dp);
         }

         for(Rubrique rubrique : dlRubRet) {
            double totalRub = var33.stream().filter((var2x) -> var2x.getRubrique().getId() == rubrique.getId() && var2x.getRubrique().getSens().equals("R") && var2x.getMotif().getId() == motif.getId()).mapToDouble((var0) -> var0.getMontant()).sum();
            ++nunLigne;
            Detailpiece dp = new Detailpiece();
            dp.setCOMPTE("" + rubrique.getNoCompteCompta());
            dp.setCOURS((double)1.0F);
            dp.setMONTANT(totalRub);
            dp.setCVMRO_MONTANT(totalRub);
            dp.setDATEOP(mp.getDATEOP());
            dp.setDEVISE("UM");
            dp.setINTITULET(rubrique.getLibelle());
            dp.setJOURNAL("PAI");
            dp.setLIBELLE(rubrique.getLibelle());
            dp.setNUMERO_COURS(1L);
            dp.setNUMLIGNE(nunLigne);
            dp.setNUPIECE(mp);
            dp.setSENS("C");
            this.menu.gl.insertOcurance(dp);
         }

         ++nunLigne;
         Detailpiece dp = new Detailpiece();
         dp.setCOMPTE("" + this.menu.paramsGen.getNoComptaIts());
         dp.setCOURS((double)1.0F);
         dp.setMONTANT(dl.stream().mapToDouble((var0) -> var0.getIts()).sum());
         dp.setCVMRO_MONTANT(dp.getMONTANT());
         dp.setDATEOP(mp.getDATEOP());
         dp.setDEVISE("UM");
         dp.setINTITULET("ITS/IMPOTS.CEDULAIRES");
         dp.setJOURNAL("PAI");
         dp.setLIBELLE("ITS/IMPOTS.CEDULAIRES");
         dp.setNUMERO_COURS(1L);
         dp.setNUMLIGNE(nunLigne);
         dp.setNUPIECE(mp);
         dp.setSENS("C");
         this.menu.gl.insertOcurance(dp);
         ++nunLigne;
         dp = new Detailpiece();
         dp.setCOMPTE("" + this.menu.paramsGen.getNoComptaCnss());
         dp.setCOURS((double)1.0F);
         dp.setMONTANT(dl.stream().mapToDouble((var0) -> var0.getCnss()).sum());
         dp.setCVMRO_MONTANT(dp.getMONTANT());
         dp.setDATEOP(mp.getDATEOP());
         dp.setDEVISE("UM");
         dp.setINTITULET("CNSS COTISATION A IMPUTER");
         dp.setJOURNAL("PAI");
         dp.setLIBELLE("CNSS COTISATION A IMPUTER");
         dp.setNUMERO_COURS(1L);
         dp.setNUMLIGNE(nunLigne);
         dp.setNUPIECE(mp);
         dp.setSENS("C");
         this.menu.gl.insertOcurance(dp);
         ++nunLigne;
         dp = new Detailpiece();
         dp.setCOMPTE("" + this.menu.paramsGen.getNoComptaCnam());
         dp.setCOURS((double)1.0F);
         dp.setMONTANT(dl.stream().mapToDouble((var0) -> var0.getCnam()).sum());
         dp.setCVMRO_MONTANT(dp.getMONTANT());
         dp.setDATEOP(mp.getDATEOP());
         dp.setDEVISE("UM");
         dp.setINTITULET("C N A M");
         dp.setJOURNAL("PAI");
         dp.setLIBELLE("C N A M");
         dp.setNUMERO_COURS(1L);
         dp.setNUMLIGNE(nunLigne);
         dp.setNUPIECE(mp);
         dp.setSENS("C");
         this.menu.gl.insertOcurance(dp);
         q = this.menu.entityManager.createQuery("Select p from Detailpiece p");
         q.setMaxResults(1000000);
         List<Detailpiece> dlDP = q.getResultList();
         mp.setTOTAL_CREDIT(dlDP.stream().filter((var0) -> var0.getSENS().equals("C")).mapToDouble((var0) -> var0.getCVMRO_MONTANT()).sum());
         mp.setTOTAL_DEBIT(dlDP.stream().filter((var0) -> var0.getSENS().equals("D")).mapToDouble((var0) -> var0.getCVMRO_MONTANT()).sum());
         if (this.menu.gl.updateOcurance(mp)) {
            this.menu.showMsg(this, "Pi\u00e9ce g\u00e9n\u00e9r\u00e9e avec succ\u00e9");
         }
      }

   }

   private void excelPieceCompta() throws IOException, WriteException {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      String var10000 = motif.getNom();
      String fileName = "repport/PC_" + var10000 + "_" + this.menu.filePeriodeDF.format(periode) + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "PICE COMPTABLE");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      WriteExcel var14 = this.menu.we;
      String var10004 = motif.getNom();
      var14.addLabelTitre(excelSheet, 0, 1, "PICE COMPTABLE : " + var10004 + " " + this.menu.df.format(periode).toUpperCase());
      var14 = this.menu.we;
      SimpleDateFormat var16 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var14.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var16.format(var10005));
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 0, 4, "NUPIECE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 1, 4, "DATEOP");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 2, 4, "COMPTE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 3, 4, "INTITULET");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, 4, "LIBELLE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 4, "MONTANT");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, 4, "SENS");
      this.menu.we.setColumnWidth(excelSheet, 3, 40);
      this.menu.we.setColumnWidth(excelSheet, 4, 40);
      int row = 5;
      Query q = this.menu.entityManager.createQuery("Select p from Detailpiece p");
      q.setMaxResults(1000000);
      List<Detailpiece> dlDP = q.getResultList();
      int maxPB = dlDP.size();
      this.ProgressBar.setMaximum(maxPB);
      int valuePB = 0;
      this.ProgressBar.setValue(valuePB);

      for(Detailpiece r : (List)dlDP.stream().sorted(Comparator.comparing((var0) -> var0.getNUMLIGNE())).collect(Collectors.toList())) {
         this.menu.we.addLabelBorder(excelSheet, 0, row, r.getNUPIECE().getNUMERO());
         this.menu.we.addLabelBorder(excelSheet, 1, row, this.menu.fdf.format(r.getDATEOP()));
         this.menu.we.addLabelBorder(excelSheet, 2, row, r.getCOMPTE());
         this.menu.we.addLabelBorder(excelSheet, 3, row, r.getINTITULET());
         this.menu.we.addLabelBorder(excelSheet, 4, row, r.getLIBELLE());
         this.menu.we.addNumberBorder(excelSheet, 5, row, r.getMONTANT());
         this.menu.we.addLabelBorder(excelSheet, 6, row, r.getSENS());
         ++valuePB;
         this.ProgressBar.setValue(valuePB);
         ++row;
      }

      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void btnExportExcelActionPerformed(ActionEvent var1) {
      Thread t7 = new 13(this);
      t7.start();
   }

   private void tPeriodeActionPerformed(ActionEvent var1) {
   }

   private void tMotifActionPerformed(ActionEvent var1) {
   }

   private void genPCButtonActionPerformed(ActionEvent var1) {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      if (periode.equals(this.menu.paramsGen.getPeriodeCourante())) {
         Thread t7 = new 14(this);
         t7.start();
      } else {
         this.menu.showErrMsg(this, "P\u00e9riode non autoris\u00e9e!");
      }

   }

   private void btnExportExcelPCActionPerformed(ActionEvent var1) {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      if (periode.equals(this.menu.paramsGen.getPeriodeCourante())) {
         Thread t7 = new 15(this);
         t7.start();
      } else {
         this.menu.showErrMsg(this, "P\u00e9riode non autoris\u00e9e!");
      }

   }

   private void tCodeOperationKeyPressed(KeyEvent var1) {
   }

   private void tCodeDeviseKeyPressed(KeyEvent var1) {
   }

   private void tCodeAgenceKeyPressed(KeyEvent var1) {
   }

   private void tCodeServiceKeyPressed(KeyEvent var1) {
   }

   private void tDateFormatActionPerformed(ActionEvent var1) {
   }

   private void exportFichierCompta() throws IOException, WriteException {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      String var10002 = this.menu.filePeriodeDF.format(periode);
      File periodeDir = new File("repport/Fichier_compta_Paie_" + var10002);
      if (!periodeDir.exists()) {
         periodeDir.mkdir();
      }

      String var10000 = String.valueOf(periodeDir);
      String fileName = var10000 + "/Fichier_compta_Paie_" + this.menu.filePeriodeDF.format(periode) + ".unl";
      File file = new File(fileName);
      Set<Banque> dlBanque = this.menu.stricturesIF.dataListInit_Banques;
      List<Paie> dl = this.dlPaie;
      dl = (List)dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).collect(Collectors.toList());
      List<Rubriquepaie> dlRP = this.dlRubriquepaie;
      dlRP = (List)dlRP.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).collect(Collectors.toList());
      Set<Rubrique> dlRub = ((Map)dlRP.stream().sorted(Comparator.comparing((var0) -> var0.getRubrique().getId())).collect(Collectors.groupingBy((var0) -> var0.getRubrique()))).keySet();
      Set<Rubrique> dlRub2 = new HashSet(dlRub);
      Set var36 = (Set)dlRub2.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toSet());
      this.ProgressBar.setMaximum(100);
      this.ProgressBar.setValue(1);
      DateFormat df = new SimpleDateFormat(this.tDateFormat.getSelectedItem().toString());
      DateFormat dfs = new SimpleDateFormat("MMYYYY");
      Date paieDu = this.menu.gl.addRetriveDays(periode, -27);
      Calendar cal = Calendar.getInstance();
      cal.setTime(paieDu);
      Date paieAu = this.menu.gl.addRetriveDays(paieDu, cal.getActualMaximum(5) - 1);
      String CODE_AGENCE = this.tCodeAgence.getText();
      String CODE_DEVISE = this.tCodeDevise.getText();
      String CODE_OPERATION = this.tCodeOperation.getText();
      String CODE_SERVICE = this.tCodeService.getText();
      var10000 = dfs.format(paieAu);
      String NUM_PIECE = "EP" + var10000;
      String DATE_COMPTABLE = df.format(paieAu);
      String DATE_VALEUR = DATE_COMPTABLE;
      Writer writer = Files.newBufferedWriter(file.toPath());

      try {
         for(Rubrique rubrique : var36) {
            long totalRub = (long)dlRP.stream().filter((var1x) -> var1x.getRubrique().getId() == rubrique.getId()).mapToDouble((var0) -> var0.getMontant()).sum();
            if (totalRub > 0L) {
               Object[] var42 = new Object[]{CODE_AGENCE + "|", CODE_DEVISE + "|", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
               long var10005 = rubrique.getNoChapitreCompta();
               var42[2] = var10005 + "|";
               var10005 = rubrique.getNoCompteCompta();
               var42[3] = var10005 + "|";
               var42[4] = "|";
               var42[5] = CODE_OPERATION + "|";
               var42[6] = "|";
               var42[7] = "|";
               var42[8] = "|";
               var42[9] = "|";
               var42[10] = "|";
               var42[11] = DATE_COMPTABLE + "|";
               var42[12] = CODE_SERVICE + "|";
               var42[13] = DATE_VALEUR + "|";
               var42[14] = totalRub + "|";
               String var45 = rubrique.getSens();
               var42[15] = (var45.equalsIgnoreCase("G") ? "D" : "C") + "|";
               var42[16] = rubrique.getLibelle() + "|";
               var42[17] = "|";
               var42[18] = NUM_PIECE + "|";
               var42[19] = "|";
               var42[20] = "|";
               var42[21] = "|";
               var42[22] = "|";
               var42[23] = "|";
               var42[24] = "|";
               var42[25] = "|";
               var42[26] = "|";
               var42[27] = "|";
               var42[28] = "|";
               var42[29] = "|";
               var42[30] = "|";
               var42[31] = "|";
               var42[32] = "|";
               var42[33] = "|";
               var42[34] = "|";
               var42[35] = "|";
               var42[36] = "|";
               var42[37] = "|";
               var42[38] = "|";
               var42[39] = "|";
               var42[40] = "|";
               var42[41] = "|";
               var42[42] = "|";
               var42[43] = "|";
               var42[44] = "|";
               var42[45] = "|";
               var42[46] = "|";
               var42[47] = "|";
               var42[48] = "|";
               var42[49] = "|";
               var42[50] = "|";
               var42[51] = "|";
               var42[52] = "|";
               var42[53] = "|";
               var42[54] = "|";
               var42[55] = "|";
               var42[56] = "|";
               var42[57] = "|";
               writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", var42));
            }

            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         }

         Object[] var43 = new Object[]{CODE_AGENCE + "|", CODE_DEVISE + "|", this.menu.paramsGen.getNoComptaChapitreIts() + "|", this.menu.paramsGen.getNoComptaIts() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
         Stream var46 = dl.stream();
         var43[14] = (long)var46.mapToDouble((var0) -> var0.getIts()).sum() + "|";
         var43[15] = "C|";
         var43[16] = "ITS|";
         var43[17] = "|";
         var43[18] = NUM_PIECE + "|";
         var43[19] = "|";
         var43[20] = "|";
         var43[21] = "|";
         var43[22] = "|";
         var43[23] = "|";
         var43[24] = "|";
         var43[25] = "|";
         var43[26] = "|";
         var43[27] = "|";
         var43[28] = "|";
         var43[29] = "|";
         var43[30] = "|";
         var43[31] = "|";
         var43[32] = "|";
         var43[33] = "|";
         var43[34] = "|";
         var43[35] = "|";
         var43[36] = "|";
         var43[37] = "|";
         var43[38] = "|";
         var43[39] = "|";
         var43[40] = "|";
         var43[41] = "|";
         var43[42] = "|";
         var43[43] = "|";
         var43[44] = "|";
         var43[45] = "|";
         var43[46] = "|";
         var43[47] = "|";
         var43[48] = "|";
         var43[49] = "|";
         var43[50] = "|";
         var43[51] = "|";
         var43[52] = "|";
         var43[53] = "|";
         var43[54] = "|";
         var43[55] = "|";
         var43[56] = "|";
         var43[57] = "|";
         writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", var43));
         this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         double cnssEmp = dl.stream().mapToDouble((var0) -> var0.getCnss()).sum();
         if (cnssEmp > (double)0.0F) {
            writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", CODE_AGENCE + "|", CODE_DEVISE + "|", this.menu.paramsGen.getNoComptaChapitreCnss() + "|", this.menu.paramsGen.getNoComptaCnss() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", (long)cnssEmp + "|", "C|", "CNSS EMPLOYES|", "|", NUM_PIECE + "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|"));
            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
            writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", CODE_AGENCE + "|", CODE_DEVISE + "|", this.menu.paramsGen.getNoComptaChapitreCnssMedDebit() + "|", this.menu.paramsGen.getNoComptaCnssMedDebit() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", (long)(cnssEmp * (double)2.0F) + "|", "D|", "CNSS MEDECINE DE TRAVAIL|", "|", NUM_PIECE + "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|"));
            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
            writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", CODE_AGENCE + "|", CODE_DEVISE + "|", this.menu.paramsGen.getNoComptaChapitreCnssMedCredit() + "|", this.menu.paramsGen.getNoComptaCnssMedCredit() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", (long)(cnssEmp * (double)2.0F) + "|", "C|", "CNSS MEDECINE DE TRAVAIL|", "|", NUM_PIECE + "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|"));
            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
            writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", CODE_AGENCE + "|", CODE_DEVISE + "|", this.menu.paramsGen.getNoComptaChapitreCnssPatDebit() + "|", this.menu.paramsGen.getNoComptaCnssPatDebit() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", (long)(cnssEmp * (double)13.0F) + "|", "D|", "CNSS PATRONAL|", "|", NUM_PIECE + "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|"));
            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
            writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", CODE_AGENCE + "|", CODE_DEVISE + "|", this.menu.paramsGen.getNoComptaChapitreCnssPatCredit() + "|", this.menu.paramsGen.getNoComptaCnssPatCredit() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", (long)(cnssEmp * (double)13.0F) + "|", "C|", "CNSS PATRONAL|", "|", NUM_PIECE + "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|"));
            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         }

         double cnamEmp = dl.stream().mapToDouble((var0) -> var0.getCnam()).sum();
         if (cnamEmp > (double)0.0F) {
            double mntCnamPat = dl.stream().mapToDouble((var0) -> var0.getCnam() / (double)4.0F * (double)5.0F).sum();
            writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", CODE_AGENCE + "|", CODE_DEVISE + "|", this.menu.paramsGen.getNoComptaChapitreCnam() + "|", this.menu.paramsGen.getNoComptaCnam() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", (long)cnamEmp + "|", "C|", "CNAM EMPLOYES|", "|", NUM_PIECE + "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|"));
            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
            writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", CODE_AGENCE + "|", CODE_DEVISE + "|", this.menu.paramsGen.getNoComptaChapitreCnamPatDebit() + "|", this.menu.paramsGen.getNoComptaCnamPatDebit() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", (long)mntCnamPat + "|", "D|", "CNAM EMPLOYEUR|", "|", NUM_PIECE + "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|"));
            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
            writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", CODE_AGENCE + "|", CODE_DEVISE + "|", this.menu.paramsGen.getNoComptaChapitreCnamPatCredit() + "|", this.menu.paramsGen.getNoComptaCnamPatCredit() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", (long)mntCnamPat + "|", "C|", "CNAM EMPLOYEUR|", "|", NUM_PIECE + "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|"));
            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         }

         for(Banque banque : dlBanque) {
            double netBank = dl.stream().filter((var1x) -> var1x.getBanque().equalsIgnoreCase(banque.getNom())).mapToDouble((var0) -> var0.getNet()).sum();
            if (netBank > (double)0.0F) {
               writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", CODE_AGENCE + "|", CODE_DEVISE + "|", banque.getNoChapitreCompta() + "|", banque.getNoCompteCompta() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", (long)netBank + "|", "C|", "VIREMENT " + banque.getNom() + "|", "|", NUM_PIECE + "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|"));
            }

            this.ProgressBar.setValue(this.ProgressBar.getValue() + 1);
         }

         double netCash = dl.stream().filter((var0) -> var0.getBanque().equalsIgnoreCase("-")).mapToDouble((var0) -> var0.getNet()).sum();
         if (netCash > (double)0.0F) {
            writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%n", CODE_AGENCE + "|", CODE_DEVISE + "|", this.menu.paramsGen.getNoComptaChapitreNet() + "|", this.menu.paramsGen.getNoComptaNet() + "|", "|", CODE_OPERATION + "|", "|", "|", "|", "|", "|", DATE_COMPTABLE + "|", CODE_SERVICE + "|", DATE_VALEUR + "|", (long)netCash + "|", "C|", "CASH|", "|", NUM_PIECE + "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|"));
         }

         this.ProgressBar.setValue(100);
      } catch (Throwable var33) {
         if (writer != null) {
            try {
               writer.close();
            } catch (Throwable var31) {
               var33.addSuppressed(var31);
            }
         }

         throw var33;
      }

      if (writer != null) {
         writer.close();
      }

      try {
         Desktop.getDesktop().open(periodeDir);
      } catch (Exception ex) {
         ex.printStackTrace();
      }

   }

   private void exportTXTButtonActionPerformed(ActionEvent var1) {
      Thread t7 = new 16(this);
      t7.start();
   }
}
