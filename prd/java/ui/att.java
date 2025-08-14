package com.mccmr.ui;

import com.mccmr.entity.Donneespointeuse;
import com.mccmr.entity.Employe;
import com.mccmr.entity.Jour;
import com.mccmr.entity.Paie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.ModelClass;
import com.mccmr.util.WriteExcel;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.hibernate.HibernateException;

public class att extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   int selectedIDP;
   private JButton btnAfficherPointage;
   private JButton btnApplyPointage;
   private JButton btnDeleteAll;
   private JButton btnDeletePointage;
   private JButton btnExit;
   private JButton btnExportMode1;
   private JButton btnExportMode2;
   private JButton btnFileSelectCVS;
   private JButton btnFileSelectExcel;
   private JButton btnImportExcel;
   private JButton btnImportFromDeviceCVS;
   private JButton btnImportFromDeviceDB;
   private JCheckBox cApplyNHT;
   private JCheckBox cApplyNJT;
   private JCheckBox cCheckAll;
   private JCheckBox cCheckInAuto;
   private JCheckBox cIgnorDayWithoutOUT;
   private JCheckBox cSuppImporte;
   private JCheckBox cValorisation;
   private JLabel jLabel127;
   private JLabel jLabel128;
   private JLabel jLabel129;
   private JLabel jLabel130;
   private JLabel jLabel131;
   private JLabel jLabel132;
   private JLabel jLabel133;
   private JLabel jLabel20;
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
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JPanel jPanel2;
   private JPanel jPanel3;
   private JPanel jPanel4;
   private JPanel jPanel5;
   private JPanel jPanel6;
   private JPanel jPanel7;
   private JScrollPane jScrollPane10;
   private JScrollPane jScrollPane11;
   private JScrollPane jScrollPane9;
   private JTabbedPane jTabbedPane1;
   private JTabbedPane jTabbedPane2;
   private JTable listJT;
   private JTable listPointage;
   private JTable listTable;
   private JLabel msgLabel;
   private JPanel pnlMotifs_Edit1;
   private JProgressBar progressBar;
   private JDateChooser tBeginDate;
   private JDateChooser tBeginDateView;
   private JComboBox<Object> tCheckInAutoREF;
   private JFormattedTextField tColNum_DATE;
   private JFormattedTextField tColNum_IDSAL;
   private JFormattedTextField tColNum_IO;
   private JFormattedTextField tColNum_TIME;
   private JComboBox<Object> tDateFormat;
   private JComboBox<Object> tDefaultOUTTime;
   private JComboBox<Object> tDeviceType;
   private JDateChooser tEndDateView;
   private JLabel tFileName;
   private JLabel tFileName1;
   private JFormattedTextField tLineNum_DataBegin;
   private JFormattedTextField tTotHJ;
   private JFormattedTextField tTotHN;
   private JFormattedTextField tTotHS;
   private JFormattedTextField tTotHS115;
   private JFormattedTextField tTotHS140;
   private JFormattedTextField tTotHS150;
   private JFormattedTextField tTotHS200;

   public att() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnApplyPointage.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExportMode1.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExportMode2.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnAfficherPointage.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.GET_APP, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnImportFromDeviceDB.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOUD_DOWNLOAD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnImportFromDeviceCVS.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOUD_DOWNLOAD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnImportExcel.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOUD_DOWNLOAD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDeleteAll.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnDeletePointage.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnFileSelectExcel.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FIND_IN_PAGE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnFileSelectCVS.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FIND_IN_PAGE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.tBeginDate.setDate(new Date());
      Date paieAu_default = this.menu.paramsGen.getPeriodeCourante();
      Date paieDu = this.menu.gl.addRetriveDays(paieAu_default, -27);
      Calendar cal = Calendar.getInstance();
      cal.setTime(paieDu);
      Date paieAu = this.menu.gl.addRetriveDays(paieDu, cal.getActualMaximum(5) - 1);
      this.tBeginDateView.setDate(paieDu);
      this.tEndDateView.setDate(paieAu);
      this.listTable.removeAll();
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmPointage(var10003));
      this.listPointage.removeAll();
      var10000 = this.listPointage;
      var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmPointageIndividuel(var10003));
   }

   private List<String> getRecordFromLine(String var1) {
      List<String> values = new ArrayList();
      Scanner rowScanner = new Scanner(line);

      try {
         rowScanner.useDelimiter(",");

         while(rowScanner.hasNext()) {
            values.add(rowScanner.next());
         }
      } catch (Throwable var7) {
         try {
            rowScanner.close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      rowScanner.close();
      return values;
   }

   private boolean readPointageFromCSV_HKVISION() {
      boolean r = false;
      Thread t = new 1(this);
      t.start();
      return r;
   }

   private void importDataHIKVISION() {
      Thread t = new 2(this);
      t.start();
   }

   private void importDataZKTecho() {
   }

   private void exportExcelMode1(Date var1, Date var2) throws IOException, WriteException {
      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
      String var10000 = df.format(begingDate);
      String fileName = "repport/EHS_MODE1_" + var10000 + "_AU_" + df.format(endDate) + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Pointage");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      WriteExcel var40 = this.menu.we;
      String var10004 = df.format(begingDate);
      var40.addLabelTitre(excelSheet, 0, 1, "ETAT DE POINTAGE DU " + var10004 + " AU " + df.format(endDate));
      var40 = this.menu.we;
      SimpleDateFormat var43 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var40.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var43.format(var10005));
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 0, 6, "ID EMPL.");
      this.menu.we.setColumnWidth(excelSheet, 0, 6);
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 1, 6, "NOM ET PRENOM");
      this.menu.we.setColumnWidth(excelSheet, 1, 15);
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 2, 6, "IDP");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 3, 6, "NJP");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, 6, "DATE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 6, "HEURE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, 6, "I/O");
      int row = 7;
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat jf = new SimpleDateFormat("E");

      try {
         int maxPB = this.listTable.getSelectedRowCount();
         this.progressBar.setMaximum(maxPB);
         int valuePB = 0;
         this.progressBar.setValue(valuePB);

         for(int i = 0; i < this.listTable.getRowCount(); ++i) {
            if ((Boolean)((ModelClass.tmPointage)this.listTable.getModel()).getValueAt(i, 0)) {
               this.selectedIDP = ((Number)this.listTable.getValueAt(i, 3)).intValue();
               Employe e = this.menu.pc.employeByIDP((long)this.selectedIDP);
               this.menu.we.addNumberBorder(excelSheet, 0, row, Integer.valueOf(e.getId()).doubleValue());
               var40 = this.menu.we;
               String var44 = e.getPrenom();
               var40.addLabelBorder(excelSheet, 1, row, var44 + " " + e.getNom());
               this.menu.we.addNumberBorder(excelSheet, 2, row, e.getIdSalariePointeuse().doubleValue());
               this.menu.we.addNumberBorder(excelSheet, 3, row, ((Number)this.listTable.getValueAt(i, 4)).doubleValue());
               Query q = this.menu.entityManager.createQuery("Select p from Donneespointeuse p where p.employe.id=?1 and p.heureJour >=?2 and p.heureJour <=?3").setParameter(1, e.getId()).setParameter(2, begingDate).setParameter(3, endDate);
               q.setMaxResults(1000000);
               List<Donneespointeuse> dl = q.getResultList();

               for(Donneespointeuse dp : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getHeureJour())).collect(Collectors.toList())) {
                  this.menu.we.addLabelBorder(excelSheet, 4, row, (new SimpleDateFormat("dd/MM/yyyy")).format(dp.getHeureJour()));
                  this.menu.we.addLabelBorder(excelSheet, 5, row, (new SimpleDateFormat("HH:mm")).format(dp.getHeureJour()));
                  this.menu.we.addLabelBorder(excelSheet, 6, row, dp.getVinOut().equalsIgnoreCase("I") ? "ENTREE" : "SORTIE");
                  ++row;
               }

               ++row;
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, row, "JOUR");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, row, "DATE");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, row, "HJ");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, row, "HN");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 8, row, "PP");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 9, row, "F50%");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 10, row, "F100%");
               ++row;
               double totHJ = (double)0.0F;
               double totHN = (double)0.0F;
               double totPP = (double)0.0F;
               q = this.menu.entityManager.createQuery("Select p from Jour p where p.employe.id=?1 and p.dateJour >=?2 and p.dateJour <=?3").setParameter(1, e.getId()).setParameter(2, begingDate).setParameter(3, endDate);
               q.setMaxResults(1000000);
               List<Jour> dlJr = q.getResultList();

               for(Jour jr : (List)dlJr.stream().sorted(Comparator.comparing((var0) -> var0.getDateJour())).collect(Collectors.toList())) {
                  totHJ += jr.getNbHeureJour();
                  totHN += jr.getNbHeureNuit();
                  totPP += jr.getNbPrimePanier();
                  this.menu.we.addLabelBorder(excelSheet, 4, row, jf.format(jr.getDateJour()));
                  this.menu.we.addLabelBorder(excelSheet, 5, row, sdfs.format(jr.getDateJour()));
                  this.menu.we.addNumberBorder(excelSheet, 6, row, jr.getNbHeureJour());
                  this.menu.we.addNumberBorder(excelSheet, 7, row, jr.getNbHeureNuit());
                  this.menu.we.addNumberBorder(excelSheet, 8, row, jr.getNbPrimePanier());
                  this.menu.we.addLabelBorder(excelSheet, 9, row, jr.isFerie50() ? "OUI" : "NON");
                  this.menu.we.addLabelBorder(excelSheet, 10, row, jr.isFerie100() ? "OUI" : "NON");
                  ++row;
               }

               this.menu.we.addNumberBoldBorderSilver(excelSheet, 6, row, totHJ);
               this.menu.we.addNumberBoldBorderSilver(excelSheet, 7, row, totHN);
               this.menu.we.addNumberBoldBorderSilver(excelSheet, 8, row, totPP);
               ++row;
               ++row;
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, row, "HS115%");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, row, "HS140%");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, row, "HS150%");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, row, "HS200%");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 8, row, "THS");
               this.menu.we.addLabelBoldBorderWrap(excelSheet, 9, row, "PP");
               ++row;
               double[] tabHS = this.menu.pc.decompterHSIntervall(e, begingDate, endDate);
               double totHS = tabHS[2] + tabHS[3] + tabHS[4] + tabHS[5];
               this.menu.we.addNumberBorder(excelSheet, 4, row, tabHS[2]);
               this.menu.we.addNumberBorder(excelSheet, 5, row, tabHS[3]);
               this.menu.we.addNumberBorder(excelSheet, 6, row, tabHS[4]);
               this.menu.we.addNumberBorder(excelSheet, 7, row, tabHS[5]);
               this.menu.we.addNumberBorder(excelSheet, 8, row, totHS);
               this.menu.we.addNumberBorder(excelSheet, 9, row, tabHS[6]);
               ++row;
               ++row;
               ++valuePB;
               this.progressBar.setValue(valuePB);
            }
         }
      } catch (HibernateException he) {
         he.printStackTrace();
      }

      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void exportExcelMode2(Date var1, Date var2) throws IOException, WriteException {
      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
      String var10000 = df.format(begingDate);
      String fileName = "repport/EHS_MODE2_" + var10000 + "_AU_" + df.format(endDate) + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Pointage");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      WriteExcel var86 = this.menu.we;
      String var10004 = df.format(begingDate);
      var86.addLabelTitre(excelSheet, 0, 1, "ETAT DE POINTAGE DU " + var10004 + " AU " + df.format(endDate));
      var86 = this.menu.we;
      SimpleDateFormat var89 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var86.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var89.format(var10005));
      int row = 7;
      int row2 = 7;
      int col = 0;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "ID EMPL.");
      this.menu.we.setColumnWidth(excelSheet, col, 6);
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "NOM ET PRENOM");
      this.menu.we.setColumnWidth(excelSheet, col, 15);
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "DEPARTEMENT");
      this.menu.we.setColumnWidth(excelSheet, col, 15);
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "SERVICE");
      this.menu.we.setColumnWidth(excelSheet, col, 15);
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "POSTE");
      this.menu.we.setColumnWidth(excelSheet, col, 15);
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "IDP");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "NJP");
      this.menu.we.setColumnWidth(excelSheet, col, 5);
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "DATE");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "HEURE");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "I/O");
      this.menu.we.setColumnWidth(excelSheet, col, 7);
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "JOUR");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "DATE");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "HJ");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "HN");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "PP");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "F50%");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "F100%");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "HS115%");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "HS140%");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "HS150%");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "HS200%");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 6, "TOTAL HS");
      SimpleDateFormat jf = new SimpleDateFormat("EEEE");

      try {
         int maxPB = this.listTable.getSelectedRowCount();
         this.progressBar.setMaximum(maxPB);
         int valuePB = 0;
         this.progressBar.setValue(valuePB);

         for(int i = 0; i < this.listTable.getRowCount(); ++i) {
            if ((Boolean)((ModelClass.tmPointage)this.listTable.getModel()).getValueAt(i, 0)) {
               this.selectedIDP = ((Number)this.listTable.getValueAt(i, 3)).intValue();
               Employe e = this.menu.pc.employeByIDP((long)this.selectedIDP);
               Query q = this.menu.entityManager.createQuery("Select p from Donneespointeuse p where p.employe.id=?1 and p.heureJour >=?2 and p.heureJour <=?3").setParameter(1, e.getId()).setParameter(2, begingDate).setParameter(3, endDate);
               q.setMaxResults(1000000);
               List<Donneespointeuse> dl = q.getResultList();

               for(Donneespointeuse dp : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getHeureJour())).collect(Collectors.toList())) {
                  col = 0;
                  this.menu.we.addNumberBorder(excelSheet, col, row, Integer.valueOf(e.getId()).doubleValue());
                  ++col;
                  var86 = this.menu.we;
                  String var90 = e.getPrenom();
                  var86.addLabelBorder(excelSheet, col, row, var90 + " " + e.getNom());
                  ++col;
                  this.menu.we.addLabelBorder(excelSheet, col, row, e.getDepartement().getNom());
                  ++col;
                  this.menu.we.addLabelBorder(excelSheet, col, row, e.getActivite() != null ? e.getActivite().getNom() : "");
                  ++col;
                  this.menu.we.addLabelBorder(excelSheet, col, row, e.getPoste().getNom());
                  ++col;
                  this.menu.we.addNumberBorder(excelSheet, col, row, e.getIdSalariePointeuse().doubleValue());
                  ++col;
                  this.menu.we.addNumberBorder(excelSheet, col, row, ((Number)this.listTable.getValueAt(i, 4)).doubleValue());
                  ++col;
                  this.menu.we.addLabelBorder(excelSheet, col, row, (new SimpleDateFormat("dd/MM/yyyy")).format(dp.getHeureJour()));
                  ++col;
                  this.menu.we.addLabelBorder(excelSheet, col, row, (new SimpleDateFormat("HH:mm")).format(dp.getHeureJour()));
                  ++col;
                  this.menu.we.addLabelBorder(excelSheet, col, row, dp.getVinOut().equalsIgnoreCase("I") ? "ENTREE" : "SORTIE");
                  ++col;
                  ++row;
               }

               int row3 = row2;
               double totHJ = (double)0.0F;
               double totHN = (double)0.0F;
               double totPP = (double)0.0F;
               q = this.menu.entityManager.createQuery("Select p from Jour p where p.employe.id=?1 and p.dateJour >=?2 and p.dateJour <=?3").setParameter(1, e.getId()).setParameter(2, begingDate).setParameter(3, endDate);
               q.setMaxResults(1000000);
               List<Jour> dlJr = q.getResultList();

               for(Jour jr : (List)dlJr.stream().sorted(Comparator.comparing((var0) -> var0.getDateJour())).collect(Collectors.toList())) {
                  col = 10;
                  totHJ += jr.getNbHeureJour();
                  totHN += jr.getNbHeureNuit();
                  totPP += jr.getNbPrimePanier();
                  this.menu.we.addLabelBorder(excelSheet, col, row3, jf.format(jr.getDateJour()).toUpperCase());
                  ++col;
                  this.menu.we.addLabelBorder(excelSheet, col, row3, this.menu.fdf.format(jr.getDateJour()));
                  ++col;
                  this.menu.we.addNumberBorder(excelSheet, col, row3, jr.getNbHeureJour());
                  ++col;
                  this.menu.we.addNumberBorder(excelSheet, col, row3, jr.getNbHeureNuit());
                  ++col;
                  this.menu.we.addNumberBorder(excelSheet, col, row3, jr.getNbPrimePanier());
                  ++col;
                  this.menu.we.addLabelBorder(excelSheet, col, row3, jr.isFerie50() ? "OUI" : "NON");
                  ++col;
                  this.menu.we.addLabelBorder(excelSheet, col, row3, jr.isFerie100() ? "OUI" : "NON");
                  ++col;
                  ++row3;
               }

               col = 12;
               this.menu.we.addNumberBoldBorderSilver(excelSheet, col, row3, totHJ);
               ++col;
               this.menu.we.addNumberBoldBorderSilver(excelSheet, col, row3, totHN);
               ++col;
               this.menu.we.addNumberBoldBorderSilver(excelSheet, col, row3, totPP);
               ++col;
               double[] tabHS = this.menu.pc.decompterHSIntervall(e, begingDate, endDate);
               double totHS = tabHS[2] + tabHS[3] + tabHS[4] + tabHS[5];
               col = 17;
               this.menu.we.addNumberBorder(excelSheet, col, row2, tabHS[2]);
               ++col;
               this.menu.we.addNumberBorder(excelSheet, col, row2, tabHS[3]);
               ++col;
               this.menu.we.addNumberBorder(excelSheet, col, row2, tabHS[4]);
               ++col;
               this.menu.we.addNumberBorder(excelSheet, col, row2, tabHS[5]);
               ++col;
               this.menu.we.addNumberBoldBorderSilver(excelSheet, col, row2, totHS);
               int var84 = row2 + 1;
               ++valuePB;
               this.progressBar.setValue(valuePB);
               row2 = row;
            }
         }
      } catch (HibernateException he) {
         he.printStackTrace();
      }

      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void afficherListe(Date var1, Date var2) {
      if (begingDate != null && endDate != null) {
         this.listTable.removeAll();
         JTable var10000 = this.listTable;
         ModelClass var10003 = this.menu.mc;
         Objects.requireNonNull(var10003);
         var10000.setModel(new ModelClass.tmPointage(var10003));
         Query q = this.menu.entityManager.createQuery("Select p from Donneespointeuse p where p.vinOut=?1").setParameter(1, "I");
         q.setMaxResults(1000000);
         List<Donneespointeuse> dl = q.getResultList();
         dl = (List)dl.stream().filter((var0) -> var0.getHeureJour() != null).collect(Collectors.toList());
         Set<Employe> setEmployes = ((Map)dl.stream().filter((var2x) -> var2x.getHeureJour().after(begingDate) && var2x.getHeureJour().before(endDate)).sorted(Comparator.comparing((var0) -> var0.getEmploye().getId())).collect(Collectors.groupingBy((var0) -> var0.getEmploye()))).keySet();
         List<Employe> dlEmployes = new ArrayList(setEmployes);

         for(Employe emp : (List)dlEmployes.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
            ((ModelClass.tmPointage)this.listTable.getModel()).addRow(this.cCheckAll.isSelected(), emp.getId(), emp.getPrenom() + " " + emp.getNom(), emp.getIdSalariePointeuse(), this.nbJrsBySalarie(emp, begingDate, endDate));
         }

         this.listTable.getColumnModel().getColumn(0).setPreferredWidth(20);
         this.listTable.getColumnModel().getColumn(1).setPreferredWidth(40);
         this.listTable.getColumnModel().getColumn(2).setPreferredWidth(400);
         this.listTable.setRowHeight(30);
      } else {
         this.menu.showErrMsg(this, "Veuillez fournir les dates d\u00e9but et fin");
      }

   }

   private void afficherListePointage(int var1, Date var2, Date var3) {
      this.listPointage.removeAll();
      JTable var10000 = this.listPointage;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmPointageIndividuel(var10003));
      Query q = this.menu.entityManager.createQuery("Select p from Donneespointeuse p where p.employe.id=?1 and p.heureJour >=?2 and p.heureJour <=?3").setParameter(1, employeID).setParameter(2, beginDate).setParameter(3, endDate);
      q.setMaxResults(1000000);
      List<Donneespointeuse> dl = q.getResultList();

      for(Donneespointeuse dp : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getHeureJour())).collect(Collectors.toList())) {
         ((ModelClass.tmPointageIndividuel)this.listPointage.getModel()).addRow(dp);
      }

      this.listPointage.getColumnModel().getColumn(0).setPreferredWidth(80);
      this.listPointage.getColumnModel().getColumn(1).setPreferredWidth(80);
      this.listPointage.getColumnModel().getColumn(2).setPreferredWidth(50);
      this.listPointage.setRowHeight(20);
   }

   private void afficherListeJT(Employe var1, Date var2, Date var3) {
      this.listJT.removeAll();
      JTable var10000 = this.listJT;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmHSDayly(var10003));
      Query q = this.menu.entityManager.createQuery("Select p from Jour p where p.employe.id=?1 and p.dateJour >=?2").setParameter(1, employe.getId()).setParameter(2, beginDate);
      q.setMaxResults(1000000);
      List<Jour> dl = q.getResultList();

      for(Jour rs : (List)dl.stream().sorted(Comparator.comparing(Jour::getDateJour)).collect(Collectors.toList())) {
         ((ModelClass.tmHSDayly)this.listJT.getModel()).addRow(rs);
      }

      double[] tabHS = this.menu.pc.decompterHS(employe, this.menu.paramsGen.getPeriodeCourante());
      Double totHS = tabHS[2] + tabHS[3] + tabHS[4] + tabHS[5];
      this.tTotHJ.setValue(tabHS[0]);
      this.tTotHN.setValue(tabHS[1]);
      this.tTotHS115.setValue(tabHS[2]);
      this.tTotHS140.setValue(tabHS[3]);
      this.tTotHS150.setValue(tabHS[4]);
      this.tTotHS200.setValue(tabHS[5]);
      this.tTotHS.setValue(totHS);
   }

   public long nbJrsBySalarie(Employe var1, Date var2, Date var3) {
      Query q = this.menu.entityManager.createQuery("Select p from Donneespointeuse p where p.vinOut='I' and p.employe.id=?1 and p.heureJour >=?2 and p.heureJour <=?3").setParameter(1, employe.getId()).setParameter(2, begingDate).setParameter(3, endDate);
      q.setMaxResults(1000000);
      List<Donneespointeuse> dl = q.getResultList();
      Set<Donneespointeuse> setDP = ((Map)dl.stream().sorted(Comparator.comparing((var0) -> var0.getEmploye().getId())).collect(Collectors.groupingBy((var1x) -> this.menu.fdf.format(var1x.getHeureJour())))).keySet();
      return (long)setDP.size();
   }

   private Date inHourDay(int var1, Date var2) {
      Query q = this.menu.entityManager.createQuery("Select p from Donneespointeuse p where p.vinOut=?1 and p.employe.id=?2").setParameter(1, "I").setParameter(2, idSalarie);
      q.setMaxResults(1000000);
      List<Donneespointeuse> dl = q.getResultList();
      return (Date)dl.stream().filter((var2x) -> this.menu.df.format(var2x.getHeureJour()).equalsIgnoreCase(this.menu.df.format(dayDate))).map(Donneespointeuse::getHeureJour).min(Date::compareTo).orElse((Object)null);
   }

   private Date outHourDay(int var1, Date var2) {
      Query q = this.menu.entityManager.createQuery("Select p from Donneespointeuse p where p.vinOut=?1 and p.employe.id=?2").setParameter(1, "O").setParameter(2, idSalarie);
      q.setMaxResults(1000000);
      List<Donneespointeuse> dl = q.getResultList();
      return (Date)dl.stream().filter((var2x) -> this.menu.fdf.format(var2x.getHeureJour()).equalsIgnoreCase(this.menu.fdf.format(dayDate))).map(Donneespointeuse::getHeureJour).max(Date::compareTo).orElse((Object)null);
   }

   private void applyTiming(int var1, Date var2, Date var3, boolean var4) {
      Employe employe = this.menu.pc.employeById(idSalarie);
      if (employe != null) {
         try {
            if (!this.menu.entityManager.getTransaction().isActive()) {
               this.menu.entityManager.getTransaction().begin();
            }

            if (this.menu.dialect.toString().contains("Oracle")) {
               this.menu.entityManager.createQuery("DELETE from Jour p where p.employe.id=" + idSalarie + " and p.periode=TO_DATE('" + this.menu.df.format(this.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD')").executeUpdate();
            } else {
               this.menu.entityManager.createQuery("DELETE from Jour p where p.employe.id=" + idSalarie + " and p.periode='" + this.menu.df.format(this.menu.paramsGen.getPeriodeCourante()) + "'").executeUpdate();
            }

            this.menu.entityManager.getTransaction().commit();
         } catch (Exception e) {
            e.printStackTrace();
            this.menu.entityManager.getTransaction().rollback();
         }

         Query q = this.menu.entityManager.createQuery("Select p from Donneespointeuse p where p.employe.id=?1 and p.heureJour >=?2 and p.heureJour <=?3").setParameter(1, employe.getId()).setParameter(2, beginDate).setParameter(3, endDate);
         q.setMaxResults(1000000);
         List<Donneespointeuse> dl = q.getResultList();

         for(Donneespointeuse o : (List)dl.stream().sorted(Comparator.comparing(Donneespointeuse::getHeureJour)).collect(Collectors.toList())) {
            double nbrHoures = (double)0.0F;
            double dayHoures = (double)0.0F;
            double nightHoures = (double)0.0F;
            double pp = (double)0.0F;
            Date dayDate = o.getHeureJour();

            try {
               Date inHoure = this.inHourDay(idSalarie, dayDate);
               if (inHoure != null) {
                  Date outHoure = this.outHourDay(idSalarie, inHoure);
                  if (outHoure == null && !this.cIgnorDayWithoutOUT.isSelected()) {
                     outHoure = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse((new SimpleDateFormat("yyyy-MM-dd " + this.tDefaultOUTTime.getSelectedItem().toString() + ":59")).format(inHoure));
                  }

                  if (outHoure != null) {
                     nbrHoures = this.menu.gl.differenceDateHeure(inHoure, outHoure);
                     if (nbrHoures < (double)0.0F) {
                        Date date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse((new SimpleDateFormat("yyyy-MM-dd 23:59:59")).format(inHoure));
                        double nbHrAvant00 = this.menu.gl.differenceDateHeure(inHoure, date);
                        double nbHrApres00 = this.menu.gl.differenceDateHeure(date, outHoure);
                        nbrHoures = nbHrAvant00 + nbHrApres00;
                     }

                     Date dayEnd = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse((new SimpleDateFormat("yyyy-MM-dd 22:00:00")).format(outHoure));
                     if (outHoure.after(dayEnd)) {
                        nightHoures = this.menu.gl.differenceDateHeure(dayEnd, outHoure);
                        dayHoures = nbrHoures - nightHoures;
                     } else {
                        dayHoures = nbrHoures;
                     }

                     boolean weekEnd = this.menu.pc.infosJourById(dayDate, employe).isWeekEnd();
                     if (ppAuto) {
                        if (Math.abs(dayHoures) >= (double)9.0F) {
                           pp = (double)1.0F;
                        }

                        if (Math.abs(nightHoures) >= (double)6.0F) {
                           ++pp;
                        }
                     }

                     BigDecimal bd = (new BigDecimal(dayHoures)).setScale(2, RoundingMode.HALF_UP);
                     dayHoures = bd.doubleValue();
                     bd = (new BigDecimal(nightHoures)).setScale(2, RoundingMode.HALF_UP);
                     nightHoures = bd.doubleValue();
                     if (this.menu.pc.insertJourHS(employe, dayDate, Math.abs(dayHoures), Math.abs(nightHoures), pp, (double)0.0F, false, weekEnd, false, "Import\u00e9 de la pointeuse")) {
                     }
                  }
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }

   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.progressBar = new JProgressBar();
      this.jTabbedPane1 = new JTabbedPane();
      this.jPanel2 = new JPanel();
      this.jPanel5 = new JPanel();
      this.btnImportFromDeviceCVS = new JButton();
      this.btnFileSelectCVS = new JButton();
      this.tFileName1 = new JLabel();
      this.jPanel6 = new JPanel();
      this.btnImportExcel = new JButton();
      this.btnFileSelectExcel = new JButton();
      this.jLabel30 = new JLabel();
      this.tFileName = new JLabel();
      this.jLabel25 = new JLabel();
      this.tLineNum_DataBegin = new JFormattedTextField();
      this.jLabel26 = new JLabel();
      this.tColNum_IDSAL = new JFormattedTextField();
      this.jLabel27 = new JLabel();
      this.tColNum_DATE = new JFormattedTextField();
      this.jLabel28 = new JLabel();
      this.tColNum_TIME = new JFormattedTextField();
      this.jLabel29 = new JLabel();
      this.tColNum_IO = new JFormattedTextField();
      this.btnDeleteAll = new JButton();
      this.cSuppImporte = new JCheckBox();
      this.jLabel24 = new JLabel();
      this.tBeginDate = new JDateChooser();
      this.jPanel7 = new JPanel();
      this.btnImportFromDeviceDB = new JButton();
      this.tDeviceType = new JComboBox();
      this.cCheckInAuto = new JCheckBox();
      this.tCheckInAutoREF = new JComboBox();
      this.jLabel20 = new JLabel();
      this.tDateFormat = new JComboBox();
      this.jPanel3 = new JPanel();
      this.jScrollPane9 = new JScrollPane();
      this.listTable = new JTable();
      this.pnlMotifs_Edit1 = new JPanel();
      this.btnAfficherPointage = new JButton();
      this.jLabel31 = new JLabel();
      this.tBeginDateView = new JDateChooser();
      this.tEndDateView = new JDateChooser();
      this.jLabel32 = new JLabel();
      this.btnDeletePointage = new JButton();
      this.btnApplyPointage = new JButton();
      this.cApplyNJT = new JCheckBox();
      this.cValorisation = new JCheckBox();
      this.cApplyNHT = new JCheckBox();
      this.cCheckAll = new JCheckBox();
      this.btnExportMode1 = new JButton();
      this.btnExportMode2 = new JButton();
      this.cIgnorDayWithoutOUT = new JCheckBox();
      this.jLabel33 = new JLabel();
      this.tDefaultOUTTime = new JComboBox();
      this.jTabbedPane2 = new JTabbedPane();
      this.jScrollPane10 = new JScrollPane();
      this.listPointage = new JTable();
      this.jPanel4 = new JPanel();
      this.jScrollPane11 = new JScrollPane();
      this.listJT = new JTable();
      this.jLabel132 = new JLabel();
      this.tTotHJ = new JFormattedTextField();
      this.jLabel133 = new JLabel();
      this.tTotHN = new JFormattedTextField();
      this.jLabel131 = new JLabel();
      this.tTotHS115 = new JFormattedTextField();
      this.jLabel130 = new JLabel();
      this.tTotHS140 = new JFormattedTextField();
      this.jLabel129 = new JLabel();
      this.tTotHS150 = new JFormattedTextField();
      this.jLabel128 = new JLabel();
      this.tTotHS200 = new JFormattedTextField();
      this.jLabel127 = new JLabel();
      this.tTotHS = new JFormattedTextField();
      this.msgLabel = new JLabel();
      this.setBackground(new Color(255, 255, 255));
      this.setBorder((Border)null);
      this.setAutoscrolls(true);
      this.setFont(new Font("Segoe UI Light", 0, 12));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(1000, 600));
      this.jPanel1.setBackground(new Color(255, 255, 255));
      this.jLabel7.setFont(new Font("SansSerif", 0, 24));
      this.jLabel7.setForeground(new Color(0, 102, 153));
      this.jLabel7.setText("Pointage automatiques");
      this.jLabel7.setToolTipText("");
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 3(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 288, -2).addGap(775, 775, 775).addComponent(this.btnExit, -2, 39, -2).addGap(19, 19, 19)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      this.jTabbedPane1.setFont(new Font("SansSerif", 1, 12));
      this.jPanel2.setBackground(new Color(255, 255, 255));
      this.jPanel5.setBackground(new Color(255, 255, 255));
      this.jPanel5.setBorder(BorderFactory.createTitledBorder((Border)null, "Depuis le fichier CSV de terminal de pointage", 0, 0, new Font("SansSerif", 2, 12)));
      this.btnImportFromDeviceCVS.setToolTipText("Importer");
      this.btnImportFromDeviceCVS.setCursor(new Cursor(12));
      this.btnImportFromDeviceCVS.setOpaque(true);
      this.btnImportFromDeviceCVS.addActionListener(new 4(this));
      this.btnFileSelectCVS.setToolTipText("Selectionner le fichier Excel");
      this.btnFileSelectCVS.setContentAreaFilled(false);
      this.btnFileSelectCVS.setCursor(new Cursor(12));
      this.btnFileSelectCVS.setOpaque(true);
      this.btnFileSelectCVS.addActionListener(new 5(this));
      this.tFileName1.setFont(new Font("Segoe UI Light", 0, 10));
      this.tFileName1.setForeground(new Color(0, 102, 153));
      this.tFileName1.setText(".");
      GroupLayout jPanel5Layout = new GroupLayout(this.jPanel5);
      this.jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(this.btnFileSelectCVS, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tFileName1, -2, 422, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnImportFromDeviceCVS, -2, 35, -2).addContainerGap()));
      jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnFileSelectCVS, -2, 35, -2).addComponent(this.tFileName1, -2, 29, -2).addComponent(this.btnImportFromDeviceCVS, -2, 35, -2)).addContainerGap(-1, 32767)));
      this.jPanel6.setBackground(new Color(255, 255, 255));
      this.jPanel6.setBorder(BorderFactory.createTitledBorder((Border)null, "Depuis un fichier Excel", 0, 0, new Font("SansSerif", 2, 12)));
      this.btnImportExcel.setToolTipText("Importer");
      this.btnImportExcel.setCursor(new Cursor(12));
      this.btnImportExcel.setOpaque(true);
      this.btnImportExcel.addActionListener(new 6(this));
      this.btnFileSelectExcel.setToolTipText("Selectionner le fichier Excel");
      this.btnFileSelectExcel.setCursor(new Cursor(12));
      this.btnFileSelectExcel.setOpaque(true);
      this.btnFileSelectExcel.addActionListener(new 7(this));
      this.jLabel30.setFont(new Font("Segoe UI Light", 1, 10));
      this.jLabel30.setForeground(new Color(255, 51, 0));
      this.jLabel30.setText("Attention! Uniquement format Excel 97-2003");
      this.tFileName.setFont(new Font("Segoe UI Light", 0, 10));
      this.tFileName.setForeground(new Color(0, 102, 153));
      this.tFileName.setText(".");
      this.jLabel25.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel25.setForeground(new Color(0, 102, 153));
      this.jLabel25.setText("1\u00e8re ligne");
      this.tLineNum_DataBegin.setBorder(BorderFactory.createEtchedBorder(0));
      this.tLineNum_DataBegin.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tLineNum_DataBegin.setFont(new Font("Segoe UI Light", 0, 16));
      this.tLineNum_DataBegin.addCaretListener(new 8(this));
      this.jLabel26.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel26.setForeground(new Color(0, 102, 153));
      this.jLabel26.setText("#Col. Id Sal.");
      this.tColNum_IDSAL.setBorder(BorderFactory.createEtchedBorder(0));
      this.tColNum_IDSAL.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tColNum_IDSAL.setFont(new Font("Segoe UI Light", 0, 16));
      this.tColNum_IDSAL.addCaretListener(new 9(this));
      this.jLabel27.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel27.setForeground(new Color(0, 102, 153));
      this.jLabel27.setText("#Col. Date");
      this.tColNum_DATE.setBorder(BorderFactory.createEtchedBorder(0));
      this.tColNum_DATE.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tColNum_DATE.setFont(new Font("Segoe UI Light", 0, 16));
      this.tColNum_DATE.addCaretListener(new 10(this));
      this.jLabel28.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel28.setForeground(new Color(0, 102, 153));
      this.jLabel28.setText("#Col. Temps");
      this.tColNum_TIME.setBorder(BorderFactory.createEtchedBorder(0));
      this.tColNum_TIME.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tColNum_TIME.setFont(new Font("Segoe UI Light", 0, 16));
      this.tColNum_TIME.addCaretListener(new 11(this));
      this.jLabel29.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel29.setForeground(new Color(0, 102, 153));
      this.jLabel29.setText("#Col. In/Out");
      this.tColNum_IO.setBorder(BorderFactory.createEtchedBorder(0));
      this.tColNum_IO.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tColNum_IO.setFont(new Font("Segoe UI Light", 0, 16));
      this.tColNum_IO.addCaretListener(new 12(this));
      GroupLayout jPanel6Layout = new GroupLayout(this.jPanel6);
      this.jPanel6.setLayout(jPanel6Layout);
      jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addGap(6, 6, 6).addGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.btnFileSelectExcel, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tFileName, -2, 202, -2)).addComponent(this.jLabel30, -2, 269, -2)).addGap(297, 297, 297).addGroup(jPanel6Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel25, Alignment.LEADING, -1, -1, 32767).addComponent(this.tLineNum_DataBegin, Alignment.LEADING, -2, 58, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel26, Alignment.LEADING, -1, -1, 32767).addComponent(this.tColNum_IDSAL, Alignment.LEADING, -2, 69, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel27, Alignment.LEADING, -1, -1, 32767).addComponent(this.tColNum_DATE, Alignment.LEADING, -2, 76, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel28, -1, -1, 32767).addComponent(this.tColNum_TIME, -2, 74, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel29, -1, -1, 32767).addComponent(this.tColNum_IO, -2, 70, -2)).addPreferredGap(ComponentPlacement.RELATED, 98, 32767).addComponent(this.btnImportExcel, -2, 35, -2).addGap(16, 16, 16)));
      jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.btnImportExcel, -2, 35, -2).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel26).addGap(0, 0, 0).addComponent(this.tColNum_IDSAL, -2, 30, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel25).addGap(0, 0, 0).addComponent(this.tLineNum_DataBegin, -2, 30, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel27).addGap(0, 0, 0).addComponent(this.tColNum_DATE, -2, 30, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel28).addGap(0, 0, 0).addComponent(this.tColNum_TIME, -2, 30, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel29).addGap(0, 0, 0).addComponent(this.tColNum_IO, -2, 30, -2)).addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnFileSelectExcel, -2, 35, -2).addComponent(this.tFileName, -2, 29, -2)).addGap(0, 0, 0).addComponent(this.jLabel30, -2, 15, -2))).addContainerGap(15, 32767)));
      this.btnDeleteAll.setToolTipText("Supprimer tout le pointage import\u00e9");
      this.btnDeleteAll.setContentAreaFilled(false);
      this.btnDeleteAll.setCursor(new Cursor(12));
      this.btnDeleteAll.setOpaque(true);
      this.btnDeleteAll.addActionListener(new 13(this));
      this.cSuppImporte.setBackground(new Color(255, 255, 255));
      this.cSuppImporte.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSuppImporte.setText("Uniquement le pointage import\u00e9");
      this.cSuppImporte.addActionListener(new 14(this));
      this.jLabel24.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel24.setForeground(new Color(0, 102, 153));
      this.jLabel24.setText("A compter du");
      this.tBeginDate.setBorder(BorderFactory.createLineBorder(new Color(48, 131, 185)));
      this.tBeginDate.setDateFormatString("dd/MM/yyyy H:mm:ss");
      this.tBeginDate.setFont(new Font("Segoe UI Light", 0, 12));
      this.jPanel7.setBackground(new Color(255, 255, 255));
      this.jPanel7.setBorder(BorderFactory.createTitledBorder((Border)null, "Depuis le terminal de pointage", 0, 0, new Font("SansSerif", 2, 12)));
      this.btnImportFromDeviceDB.setToolTipText("Importer");
      this.btnImportFromDeviceDB.setCursor(new Cursor(12));
      this.btnImportFromDeviceDB.setOpaque(true);
      this.btnImportFromDeviceDB.addActionListener(new 15(this));
      this.tDeviceType.setFont(new Font("Segoe UI Light", 0, 12));
      this.tDeviceType.setModel(new DefaultComboBoxModel(new String[]{"HIKVISION", "ZKTecho"}));
      this.tDeviceType.addActionListener(new 16(this));
      GroupLayout jPanel7Layout = new GroupLayout(this.jPanel7);
      this.jPanel7.setLayout(jPanel7Layout);
      jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(this.tDeviceType, -2, 250, -2).addPreferredGap(ComponentPlacement.RELATED, 795, 32767).addComponent(this.btnImportFromDeviceDB, -2, 35, -2).addContainerGap()));
      jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tDeviceType, -2, -1, -2).addComponent(this.btnImportFromDeviceDB, -2, 35, -2)).addContainerGap(-1, 32767)));
      this.cCheckInAuto.setBackground(new Color(255, 255, 255));
      this.cCheckInAuto.setFont(new Font("Segoe UI Light", 1, 12));
      this.cCheckInAuto.setText("CheckIn Avant / CheckOUT apr\u00e8s");
      this.cCheckInAuto.addActionListener(new 17(this));
      this.tCheckInAutoREF.setFont(new Font("Segoe UI Light", 0, 12));
      this.tCheckInAutoREF.setModel(new DefaultComboBoxModel(new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"}));
      this.tCheckInAutoREF.addActionListener(new 18(this));
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("Format de date importation");
      this.tDateFormat.setFont(new Font("Segoe UI Light", 0, 12));
      this.tDateFormat.setModel(new DefaultComboBoxModel(new String[]{"dd/MM/yyyy", "MM/dd/yyyy", "yyyy-MM-dd", "yyMMdd", "ddMMyy"}));
      this.tDateFormat.addActionListener(new 19(this));
      GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
      this.jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(25, 25, 25).addComponent(this.jLabel24, -2, 91, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tBeginDate, -2, 196, -2).addGap(18, 18, 18).addComponent(this.cCheckInAuto, -2, 253, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tCheckInAutoREF, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tDateFormat, -2, 250, -2).addComponent(this.jLabel20, -2, 250, -2)).addContainerGap()).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel5, -1, -1, 32767)).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.cSuppImporte, -2, 241, -2).addGap(46, 46, 46).addComponent(this.btnDeleteAll, -2, 35, -2).addGap(14, 14, 14)).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel6, -1, -1, 32767)).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel7, -1, -1, 32767))));
      jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGap(20, 20, 20).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel24, -1, -1, 32767).addComponent(this.tBeginDate, Alignment.TRAILING, -2, -1, -2)).addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.tCheckInAutoREF, -2, -1, -2).addComponent(this.cCheckInAuto))).addGroup(jPanel2Layout.createSequentialGroup().addGap(1, 1, 1).addComponent(this.jLabel20).addGap(3, 3, 3).addComponent(this.tDateFormat, -2, -1, -2))).addPreferredGap(ComponentPlacement.RELATED, 106, 32767).addComponent(this.jPanel5, -2, -1, -2).addGap(36, 36, 36).addComponent(this.jPanel6, -2, -1, -2).addGap(80, 80, 80).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDeleteAll, Alignment.TRAILING, -2, 35, -2).addComponent(this.cSuppImporte, Alignment.TRAILING, -2, 35, -2)).addContainerGap()).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(76, 76, 76).addComponent(this.jPanel7, -2, -1, -2).addContainerGap(347, 32767))));
      this.jTabbedPane1.addTab("Importation", this.jPanel2);
      this.jPanel3.setBackground(new Color(255, 255, 255));
      this.listTable.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable.setSelectionBackground(new Color(0, 102, 153));
      this.listTable.setShowGrid(false);
      this.listTable.addMouseListener(new 20(this));
      this.jScrollPane9.setViewportView(this.listTable);
      this.pnlMotifs_Edit1.setBackground(new Color(255, 255, 255));
      this.pnlMotifs_Edit1.setBorder(BorderFactory.createEtchedBorder());
      this.btnAfficherPointage.setToolTipText("Afficher");
      this.btnAfficherPointage.setContentAreaFilled(false);
      this.btnAfficherPointage.setCursor(new Cursor(12));
      this.btnAfficherPointage.setOpaque(true);
      this.btnAfficherPointage.addActionListener(new 21(this));
      this.jLabel31.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel31.setText("D\u00e9but");
      this.tBeginDateView.setBorder(BorderFactory.createLineBorder(new Color(48, 131, 185)));
      this.tBeginDateView.setDateFormatString("dd/MM/yyyy H:mm");
      this.tBeginDateView.setFont(new Font("Segoe UI Light", 0, 12));
      this.tEndDateView.setBorder(BorderFactory.createLineBorder(new Color(48, 131, 185)));
      this.tEndDateView.setDateFormatString("dd/MM/yyyy H:mm");
      this.tEndDateView.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel32.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel32.setText("Fin de journ\u00e9e pour les jours sans fin");
      this.btnDeletePointage.setToolTipText("Supprimer");
      this.btnDeletePointage.setContentAreaFilled(false);
      this.btnDeletePointage.setCursor(new Cursor(12));
      this.btnDeletePointage.setOpaque(true);
      this.btnDeletePointage.addActionListener(new 22(this));
      this.btnApplyPointage.setToolTipText("Appliquer le pointage");
      this.btnApplyPointage.setContentAreaFilled(false);
      this.btnApplyPointage.setCursor(new Cursor(12));
      this.btnApplyPointage.setOpaque(true);
      this.btnApplyPointage.addActionListener(new 23(this));
      this.cApplyNJT.setBackground(new Color(255, 255, 255));
      this.cApplyNJT.setFont(new Font("Segoe UI Light", 0, 12));
      this.cApplyNJT.setText("Appliquer NJT");
      this.cApplyNJT.addActionListener(new 24(this));
      this.cValorisation.setBackground(new Color(255, 255, 255));
      this.cValorisation.setFont(new Font("Segoe UI Light", 0, 12));
      this.cValorisation.setText("Valoriser HS");
      this.cValorisation.addActionListener(new 25(this));
      this.cApplyNHT.setBackground(new Color(255, 255, 255));
      this.cApplyNHT.setFont(new Font("Segoe UI Light", 0, 12));
      this.cApplyNHT.setText("D\u00e9compte HS");
      this.cApplyNHT.addActionListener(new 26(this));
      this.cCheckAll.setBackground(new Color(255, 255, 255));
      this.cCheckAll.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCheckAll.setSelected(true);
      this.cCheckAll.setText("Tout cocher");
      this.cCheckAll.addActionListener(new 27(this));
      this.btnExportMode1.setToolTipText("Exporter vers Excel Mode 1");
      this.btnExportMode1.setContentAreaFilled(false);
      this.btnExportMode1.setCursor(new Cursor(12));
      this.btnExportMode1.setOpaque(true);
      this.btnExportMode1.addActionListener(new 28(this));
      this.btnExportMode2.setToolTipText("Exporter vers Excel Mode 2");
      this.btnExportMode2.setContentAreaFilled(false);
      this.btnExportMode2.setCursor(new Cursor(12));
      this.btnExportMode2.setOpaque(true);
      this.btnExportMode2.addActionListener(new 29(this));
      this.cIgnorDayWithoutOUT.setBackground(new Color(255, 255, 255));
      this.cIgnorDayWithoutOUT.setFont(new Font("Segoe UI Light", 1, 12));
      this.cIgnorDayWithoutOUT.setText("Ingnorer les jours sans fin");
      this.cIgnorDayWithoutOUT.addActionListener(new 30(this));
      this.jLabel33.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel33.setText("Fin");
      this.tDefaultOUTTime.setFont(new Font("Segoe UI Light", 0, 12));
      this.tDefaultOUTTime.setModel(new DefaultComboBoxModel(new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"}));
      this.tDefaultOUTTime.addActionListener(new 31(this));
      GroupLayout pnlMotifs_Edit1Layout = new GroupLayout(this.pnlMotifs_Edit1);
      this.pnlMotifs_Edit1.setLayout(pnlMotifs_Edit1Layout);
      pnlMotifs_Edit1Layout.setHorizontalGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addComponent(this.cCheckAll, -2, 110, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.cIgnorDayWithoutOUT, -2, 210, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jLabel32, -2, 239, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.tDefaultOUTTime, -2, -1, -2)).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addComponent(this.jLabel31, -2, 48, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tBeginDateView, -2, 177, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel33, -2, 36, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tEndDateView, -2, 179, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnAfficherPointage, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cApplyNJT).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cApplyNHT, -2, 120, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cValorisation, -2, 118, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnApplyPointage, -2, 35, -2).addGap(30, 30, 30).addComponent(this.btnExportMode1, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnExportMode2, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnDeletePointage, -2, 35, -2))).addContainerGap()));
      pnlMotifs_Edit1Layout.setVerticalGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlMotifs_Edit1Layout.createSequentialGroup().addGap(5, 5, 5).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel33, Alignment.TRAILING, -1, -1, 32767).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addGap(12, 12, 12).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.TRAILING).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.cApplyNJT).addComponent(this.cApplyNHT).addComponent(this.cValorisation)).addComponent(this.btnAfficherPointage, -2, 35, -2).addComponent(this.btnApplyPointage, -2, 35, -2).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDeletePointage, -2, 35, -2).addComponent(this.btnExportMode1, -2, 35, -2).addComponent(this.btnExportMode2, -2, 35, -2))).addGap(0, 0, 32767)).addGroup(Alignment.TRAILING, pnlMotifs_Edit1Layout.createSequentialGroup().addGap(22, 22, 22).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tEndDateView, Alignment.TRAILING, -2, -1, -2).addGroup(Alignment.TRAILING, pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel31, -1, -1, 32767).addComponent(this.tBeginDateView, -2, -1, -2))))).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.cCheckAll, -2, 30, -2).addComponent(this.cIgnorDayWithoutOUT).addComponent(this.jLabel32, -1, -1, 32767).addComponent(this.tDefaultOUTTime, -2, -1, -2)).addGap(12, 12, 12)));
      this.listPointage.setFont(new Font("Segoe UI Light", 0, 13));
      this.listPointage.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listPointage.setSelectionBackground(new Color(0, 102, 153));
      this.listPointage.setShowGrid(false);
      this.listPointage.addMouseListener(new 32(this));
      this.jScrollPane10.setViewportView(this.listPointage);
      this.jTabbedPane2.addTab("Pointage", this.jScrollPane10);
      this.listJT.setFont(new Font("Segoe UI Light", 0, 13));
      this.listJT.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listJT.setSelectionBackground(new Color(0, 102, 153));
      this.listJT.setShowGrid(false);
      this.listJT.addMouseListener(new 33(this));
      this.jScrollPane11.setViewportView(this.listJT);
      this.jLabel132.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel132.setForeground(new Color(0, 102, 153));
      this.jLabel132.setHorizontalAlignment(0);
      this.jLabel132.setText("Tot. HJ");
      this.tTotHJ.setBorder((Border)null);
      this.tTotHJ.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHJ.setHorizontalAlignment(0);
      this.tTotHJ.setEnabled(false);
      this.tTotHJ.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHJ.addCaretListener(new 34(this));
      this.tTotHJ.addFocusListener(new 35(this));
      this.jLabel133.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel133.setForeground(new Color(0, 102, 153));
      this.jLabel133.setHorizontalAlignment(0);
      this.jLabel133.setText("Tot. HN");
      this.tTotHN.setBorder((Border)null);
      this.tTotHN.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHN.setHorizontalAlignment(0);
      this.tTotHN.setEnabled(false);
      this.tTotHN.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHN.addCaretListener(new 36(this));
      this.tTotHN.addFocusListener(new 37(this));
      this.jLabel131.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel131.setForeground(new Color(0, 102, 153));
      this.jLabel131.setHorizontalAlignment(0);
      this.jLabel131.setText("HS 115%");
      this.tTotHS115.setEditable(false);
      this.tTotHS115.setBackground(new Color(102, 204, 0));
      this.tTotHS115.setBorder((Border)null);
      this.tTotHS115.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHS115.setHorizontalAlignment(0);
      this.tTotHS115.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHS115.addCaretListener(new 38(this));
      this.tTotHS115.addFocusListener(new 39(this));
      this.jLabel130.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel130.setForeground(new Color(0, 102, 153));
      this.jLabel130.setHorizontalAlignment(0);
      this.jLabel130.setText("HS 140%");
      this.tTotHS140.setEditable(false);
      this.tTotHS140.setBackground(new Color(255, 204, 51));
      this.tTotHS140.setBorder((Border)null);
      this.tTotHS140.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHS140.setHorizontalAlignment(0);
      this.tTotHS140.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHS140.addCaretListener(new 40(this));
      this.tTotHS140.addFocusListener(new 41(this));
      this.jLabel129.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel129.setForeground(new Color(0, 102, 153));
      this.jLabel129.setHorizontalAlignment(0);
      this.jLabel129.setText("HS 150%");
      this.tTotHS150.setEditable(false);
      this.tTotHS150.setBackground(new Color(0, 204, 204));
      this.tTotHS150.setBorder((Border)null);
      this.tTotHS150.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHS150.setHorizontalAlignment(0);
      this.tTotHS150.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHS150.addCaretListener(new 42(this));
      this.tTotHS150.addFocusListener(new 43(this));
      this.jLabel128.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel128.setForeground(new Color(0, 102, 153));
      this.jLabel128.setHorizontalAlignment(0);
      this.jLabel128.setText("HS 200%");
      this.tTotHS200.setEditable(false);
      this.tTotHS200.setBackground(new Color(255, 0, 102));
      this.tTotHS200.setBorder((Border)null);
      this.tTotHS200.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHS200.setHorizontalAlignment(0);
      this.tTotHS200.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHS200.addCaretListener(new 44(this));
      this.tTotHS200.addFocusListener(new 45(this));
      this.jLabel127.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel127.setForeground(new Color(0, 102, 153));
      this.jLabel127.setHorizontalAlignment(0);
      this.jLabel127.setText("Tot. HS");
      this.tTotHS.setBorder((Border)null);
      this.tTotHS.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tTotHS.setHorizontalAlignment(0);
      this.tTotHS.setEnabled(false);
      this.tTotHS.setFont(new Font("Segoe UI Light", 1, 12));
      this.tTotHS.addCaretListener(new 46(this));
      this.tTotHS.addFocusListener(new 47(this));
      GroupLayout jPanel4Layout = new GroupLayout(this.jPanel4);
      this.jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel132, Alignment.LEADING, -2, 60, -2).addComponent(this.tTotHJ, Alignment.LEADING, -2, 60, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel133, Alignment.LEADING, -2, 60, -2).addComponent(this.tTotHN, Alignment.LEADING, -2, 60, -2)).addGap(3, 3, 3).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel131, Alignment.LEADING, -2, 60, -2).addComponent(this.tTotHS115, Alignment.LEADING, -2, 60, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel130, Alignment.LEADING, -2, 60, -2).addComponent(this.tTotHS140, Alignment.LEADING, -2, 60, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel129, Alignment.LEADING, -2, 60, -2).addComponent(this.tTotHS150, Alignment.LEADING, -2, 60, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel128, Alignment.LEADING, -2, 60, -2).addComponent(this.tTotHS200, Alignment.LEADING, -2, 60, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel127, Alignment.LEADING, -2, 60, -2).addComponent(this.tTotHS, Alignment.LEADING, -2, 60, -2)).addGap(69, 69, 69)).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.jScrollPane11, -2, 516, -2).addContainerGap())));
      jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addGap(268, 268, 268).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel132).addGap(0, 0, 0).addComponent(this.tTotHJ, -2, 30, -2)).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel127).addGap(0, 0, 0).addComponent(this.tTotHS, -2, 30, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel128).addGap(0, 0, 0).addComponent(this.tTotHS200, -2, 30, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel129).addGap(0, 0, 0).addComponent(this.tTotHS150, -2, 30, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel130).addGap(0, 0, 0).addComponent(this.tTotHS140, -2, 30, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel131).addGap(0, 0, 0).addComponent(this.tTotHS115, -2, 30, -2)))).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel133).addGap(0, 0, 0).addComponent(this.tTotHN, -2, 30, -2))).addContainerGap()).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.jScrollPane11, -2, 260, -2).addContainerGap(53, 32767))));
      this.jTabbedPane2.addTab("JT", this.jPanel4);
      GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
      this.jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlMotifs_Edit1, -1, -1, 32767).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jScrollPane9, -2, 543, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jTabbedPane2))).addContainerGap()));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addGap(5, 5, 5).addComponent(this.pnlMotifs_Edit1, -2, 102, -2).addGap(6, 6, 6).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jScrollPane9, -2, 365, -2).addComponent(this.jTabbedPane2, Alignment.TRAILING, -2, 365, -2)).addContainerGap()));
      this.jTabbedPane1.addTab("Pointage", this.jPanel3);
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(5, 5, 5).addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jTabbedPane1).addComponent(this.msgLabel, -1, -1, 32767)).addGap(5, 5, 5)).addComponent(this.jPanel1, -1, -1, 32767).addComponent(this.progressBar, -1, -1, 32767));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.progressBar, -2, -1, -2).addGap(5, 5, 5).addComponent(this.jTabbedPane1, -2, 536, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.msgLabel, -2, 30, -2).addContainerGap()));
      this.setSize(new Dimension(1135, 629));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   public int selectedLinesSize(JTable var1) {
      int r = 0;

      for(int i = 0; i < listTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmEmployeStat)listTable.getModel()).getValueAt(i, 0)) {
            ++r;
         }
      }

      return r;
   }

   private void tDateFormatActionPerformed(ActionEvent var1) {
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmBulletinPaie(var10003));
   }

   private void btnImportFromDeviceCVSActionPerformed(ActionEvent var1) {
      switch (this.tDeviceType.getSelectedItem().toString()) {
         case "HIKVISION" -> this.readPointageFromCSV_HKVISION();
         case "ZKTecho" -> this.importDataZKTecho();
      }

   }

   private void btnDeleteAllActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 48(this);
         t.start();
      }

   }

   public static String convertTo24Hour(String var0) {
      DateFormat f1 = new SimpleDateFormat("hh:mm:ss a");
      Date d = null;

      try {
         d = f1.parse(Time);
      } catch (ParseException e) {
         e.printStackTrace();
      }

      DateFormat f2 = new SimpleDateFormat("HH:mm");
      String x = f2.format(d);
      return x;
   }

   public boolean readPointageFromExcel(Date var1, String var2, Integer var3, Integer var4, Integer var5, Integer var6, Integer var7) throws IOException {
      boolean r = false;
      String inputFile = this.tFileName.getText();
      File inputWorkbook = new File(inputFile);
      new SimpleDateFormat(dateFormat);

      try {
         Workbook w = Workbook.getWorkbook(inputWorkbook);
         Sheet sheet = w.getSheet(0);
         if (this.menu.pc.deletePointageFromDateAll(true, beginDate)) {
            for(int i = lineNumBiginData; i < sheet.getRows(); ++i) {
               try {
                  if (sheet.getCell(colNumIDSAL, i).getContents().toString().length() != 0 && sheet.getCell(colNumIDDATE, i).getContents().toString().length() != 0 && sheet.getCell(colNumIDTIME, i).getContents().toString().length() != 0 && sheet.getCell(colNumIDIO, i).getContents().toString().length() != 0) {
                     String idSalarie = sheet.getCell(colNumIDSAL, i).getContents();
                     idSalarie = idSalarie.replace(",", "");
                     idSalarie = idSalarie.replace(" ", "");
                     Long idp = Long.parseLong(idSalarie);
                     Employe emp = this.menu.pc.employeByIDP(idp);
                     if (emp != null) {
                        String var10000 = sheet.getCell(colNumIDDATE, i).getContents().toString();
                        String dateString = var10000 + " " + convertTo24Hour(sheet.getCell(colNumIDTIME, i).getContents().toString());
                        Donneespointeuse dp = new Donneespointeuse();
                        dp.setEmploye(emp);

                        try {
                           dp.setHeureJour((new SimpleDateFormat("dd/MM/yyyy HH:mm")).parse(dateString));
                           dp.setVinOut(sheet.getCell(colNumIDIO, i).getContents().toString());
                           dp.setImporte(true);
                           this.menu.gl.insertOcurance(dp);
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                     }
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      } catch (BiffException e) {
         e.printStackTrace();
      }

      return r;
   }

   private void btnImportExcelActionPerformed(ActionEvent var1) {
      this.msgLabel.setText("");
      this.msgLabel.setForeground(new Color(0, 153, 0));
      if (!this.tFileName.getText().isEmpty()) {
         Thread t = new 49(this);
         t.start();
      } else {
         this.msgLabel.setText("Err: Aucun fichier selection\u00e9");
         this.msgLabel.setForeground(Color.RED);
      }

   }

   private void btnFileSelectExcelActionPerformed(ActionEvent var1) {
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Classeur Excel 97-2003", new String[]{"xls"});
      chooser.setFileFilter(filter);
      int returnVal = chooser.showOpenDialog(this);
      if (returnVal == 0) {
         this.tFileName.setText(chooser.getSelectedFile().getPath());
      }

   }

   private void tLineNum_DataBeginCaretUpdate(CaretEvent var1) {
   }

   private void tColNum_IDSALCaretUpdate(CaretEvent var1) {
   }

   private void tColNum_DATECaretUpdate(CaretEvent var1) {
   }

   private void tColNum_TIMECaretUpdate(CaretEvent var1) {
   }

   private void tColNum_IOCaretUpdate(CaretEvent var1) {
   }

   private void cSuppImporteActionPerformed(ActionEvent var1) {
   }

   private void listTableMouseClicked(MouseEvent var1) {
      int employeID = ((Number)this.listTable.getValueAt(this.listTable.getSelectedRow(), 1)).intValue();
      this.afficherListePointage(employeID, this.tBeginDateView.getDate(), this.tEndDateView.getDate());
      Employe employe = this.menu.pc.employeById(employeID);
      this.afficherListeJT(employe, this.tBeginDateView.getDate(), this.tEndDateView.getDate());
   }

   private void btnAfficherPointageActionPerformed(ActionEvent var1) {
      Thread t = new 50(this);
      t.start();
   }

   private void btnDeletePointageActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 51(this);
         t.start();
      }

   }

   private void btnApplyPointageActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous l'application du pointage ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Thread t = new 52(this);
         t.start();
      }

   }

   private void cApplyNJTActionPerformed(ActionEvent var1) {
   }

   private void cValorisationActionPerformed(ActionEvent var1) {
   }

   private void cApplyNHTActionPerformed(ActionEvent var1) {
   }

   private void cCheckAllActionPerformed(ActionEvent var1) {
      for(int i = 0; i < this.listTable.getRowCount(); ++i) {
         ((ModelClass.tmPointage)this.listTable.getModel()).setValueAt(this.cCheckAll.isSelected(), i, 0);
      }

   }

   private void listPointageMouseClicked(MouseEvent var1) {
   }

   private void btnExportMode1ActionPerformed(ActionEvent var1) {
      Thread t = new 53(this);
      t.start();
   }

   private void btnExportMode2ActionPerformed(ActionEvent var1) {
      Thread t = new 54(this);
      t.start();
   }

   private void btnImportFromDeviceDBActionPerformed(ActionEvent var1) {
      switch (this.tDeviceType.getSelectedItem().toString()) {
         case "HIKVISION" -> this.importDataHIKVISION();
         case "ZKTecho" -> this.importDataZKTecho();
      }

   }

   private void tDeviceTypeActionPerformed(ActionEvent var1) {
   }

   private void btnFileSelectCVSActionPerformed(ActionEvent var1) {
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV", new String[]{"csv"});
      chooser.setFileFilter(filter);
      int returnVal = chooser.showOpenDialog(this);
      if (returnVal == 0) {
         this.tFileName1.setText(chooser.getSelectedFile().getPath());
      }

   }

   private void cIgnorDayWithoutOUTActionPerformed(ActionEvent var1) {
      this.tDefaultOUTTime.setEnabled(!this.cIgnorDayWithoutOUT.isSelected());
   }

   private void tDefaultOUTTimeActionPerformed(ActionEvent var1) {
   }

   private void listJTMouseClicked(MouseEvent var1) {
   }

   private void tTotHJCaretUpdate(CaretEvent var1) {
   }

   private void tTotHJFocusLost(FocusEvent var1) {
   }

   private void tTotHNCaretUpdate(CaretEvent var1) {
   }

   private void tTotHNFocusLost(FocusEvent var1) {
   }

   private void tTotHS115CaretUpdate(CaretEvent var1) {
   }

   private void tTotHS115FocusLost(FocusEvent var1) {
   }

   private void tTotHS140CaretUpdate(CaretEvent var1) {
   }

   private void tTotHS140FocusLost(FocusEvent var1) {
   }

   private void tTotHS150CaretUpdate(CaretEvent var1) {
   }

   private void tTotHS150FocusLost(FocusEvent var1) {
   }

   private void tTotHS200CaretUpdate(CaretEvent var1) {
   }

   private void tTotHS200FocusLost(FocusEvent var1) {
   }

   private void tTotHSCaretUpdate(CaretEvent var1) {
   }

   private void tTotHSFocusLost(FocusEvent var1) {
   }

   private void tCheckInAutoREFActionPerformed(ActionEvent var1) {
   }

   private void cCheckInAutoActionPerformed(ActionEvent var1) {
   }
}
