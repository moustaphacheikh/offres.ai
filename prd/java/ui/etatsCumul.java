package com.mccmr.ui;

import com.mccmr.entity.EngagementsHistory;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Rubrique;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.ModelClass;
import com.mccmr.util.WriteExcel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class etatsCumul extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   List<EngagementsHistory> dlEngagementsHistory;
   private JButton btnExit;
   private JButton btnExport;
   private JButton btnRefresh;
   private JCheckBox cCheckAll;
   private JLabel jLabel20;
   private JLabel jLabel21;
   private JLabel jLabel23;
   private JLabel jLabel25;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JScrollPane jScrollPane11;
   private JTable listeColonnesTable;
   private JLabel msgLabel;
   private JPanel panelEP;
   private JPanel pnlBody;
   private JProgressBar progressBar;
   private JComboBox<Object> tEtat;
   private JComboBox<Object> tMotif;
   private JComboBox<Object> tPeriodeAu;
   private JComboBox<Object> tPeriodeDu;

   public etatsCumul() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnRefresh.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.REFRESH, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExport.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.menu.remplirCombo("Motif", this.tMotif);
      this.menu.remplirCombo("Periode", this.tPeriodeDu);
      this.menu.remplirCombo("Periode", this.tPeriodeAu);
      Query q = this.menu.entityManager.createQuery("Select p from Paie p");
      q.setMaxResults(1000000);
      this.dlPaie = q.getResultList();
      this.afficherListeColonnes();
   }

   private void afficherListeColonnes() {
      if (this.tMotif.getSelectedItem() != null) {
         Motif motif = (Motif)this.tMotif.getSelectedItem();
         JTable var10000 = this.listeColonnesTable;
         ModelClass var10003 = this.menu.mc;
         Objects.requireNonNull(var10003);
         var10000.setModel(new ModelClass.tmCustomiseEtatPaie(var10003));
         if (motif != null && !this.tEtat.getSelectedItem().toString().equals("Masse salariale")) {
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "MAT.", "MATRICULE", -1);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "EMBAUCHE", "", -1);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "NOM ET PRENOM", "", -1);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "NJT", "", -1);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "NHS", "", -1);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "FTE", "", -1);
            Set<Rubrique> dl2 = ((Map)this.menu.pc.dlRubriquepaie.stream().filter((var1x) -> var1x.getMotif().getId() == motif.getId()).sorted(Comparator.comparing((var0) -> var0.getRubrique().getId())).collect(Collectors.groupingBy((var0) -> var0.getRubrique()))).keySet();
            List<Rubrique> dlRub2 = new ArrayList(dl2);

            for(Rubrique rs : (List)dlRub2.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
               ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, rs.getLibelle(), rs.getLibelle(), rs.getId());
            }

            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "AUTR. IND.", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "BT", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "BNI", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNSS EMP.", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNSS MED.", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNSS PAT.", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNAM EMP.", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNAM PAT.", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "ITS", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "RCNSS", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "RCNAM", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "RITS", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "AUTRES RET", "", 0);
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "NET A PAYER", "", 0);
         }

         this.progressBar.setMaximum(100);
         this.progressBar.setValue(0);
      }

   }

   private void excelEtatPaie() throws IOException, WriteException {
      String periodeDu = (new SimpleDateFormat("MM-yyyy")).format((Date)this.tPeriodeDu.getSelectedItem()).toUpperCase();
      String periodeAu = (new SimpleDateFormat("MM-yyyy")).format((Date)this.tPeriodeAu.getSelectedItem()).toUpperCase();
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      String motifTitre = motif.getNom().toUpperCase();
      String fileName = "repport/EP_" + motifTitre + "_Du" + periodeDu + "Au" + periodeAu + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Etat paie");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, "ETAT DE PAIE CUMULE DU " + periodeDu + " AU " + periodeAu + " / " + motifTitre);
      WriteExcel var10000 = this.menu.we;
      SimpleDateFormat var10004 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var10000.addLabelBold(excelSheet, 0, 1, "Edit\u00e9 le : " + var10004.format(var10005));
      int row = 5;
      double totalGainsNA = (double)0.0F;
      double totalRetenuesNA = (double)0.0F;
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      int col = 0;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 4, "PERIODE");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 4, "MOTIF");
      ++col;
      int showingRows = 0;

      for(int i = 0; i < this.listeColonnesTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(i, 0)) {
            String colName = ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(i, 1).toString();
            this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 4, colName);
            ++col;
            ++showingRows;
         }
      }

      List<Paie> dl = this.dlPaie;
      Date beginDate = this.menu.gl.addRetriveDays((Date)this.tPeriodeDu.getSelectedItem(), -28);
      Date endDate = this.menu.gl.addRetriveDays((Date)this.tPeriodeAu.getSelectedItem(), 28);
      dl = (List)dl.stream().filter((var3x) -> var3x.getPeriode().after(beginDate) && var3x.getPeriode().before(endDate) && var3x.getMotif().getId() == motif.getId()).sorted(Comparator.comparing((var0) -> var0.getEmploye().getId())).collect(Collectors.toList());
      int maxPB = dl.size() * showingRows;
      this.progressBar.setMaximum(maxPB);
      int valuePB = 0;
      this.progressBar.setValue(valuePB);

      for(Paie rs : dl) {
         col = 0;
         this.menu.we.addLabelBorder(excelSheet, col, row, this.menu.pf.format(rs.getPeriode()).toUpperCase());
         ++col;
         this.menu.we.addLabelBorder(excelSheet, col, row, rs.getMotif().getNom());
         ++col;

         for(int k = 0; k < showingRows; ++k) {
            if (!(Boolean)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(k, 0) && ((Number)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(k, 3)).intValue() > 0) {
               Rubrique rub = this.menu.pc.rubriqueById(((Number)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(k, 3)).intValue());
               Rubriquepaie rp = this.menu.pc.rubriquePaieById(rs.getEmploye(), rub, rs.getMotif(), rs.getPeriode());
               double montantRubrique = rp != null ? rp.getMontant() : (double)0.0F;
               if (rub.getSens().compareTo("G") == 0) {
                  totalGainsNA += montantRubrique;
               } else {
                  totalRetenuesNA += montantRubrique;
               }
            }
         }

         for(int var39 = 0; var39 < this.listeColonnesTable.getRowCount(); ++var39) {
            if ((Boolean)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(var39, 0)) {
               int index = ((Number)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(var39, 3)).intValue();
               if (index <= 0) {
                  switch (((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(var39, 1).toString()) {
                     case "MAT.":
                        this.menu.we.addNumberBorder(excelSheet, col, row, (double)rs.getEmploye().getId());
                        this.menu.we.setColumnWidth(excelSheet, col, 5);
                        break;
                     case "EMBAUCHE":
                        this.menu.we.addLabelBorder(excelSheet, col, row, dateFormat.format(rs.getEmploye().getDateEmbauche()));
                        break;
                     case "NOM ET PRENOM":
                        var10000 = this.menu.we;
                        String var46 = rs.getEmploye().getPrenom();
                        var10000.addLabelBorder(excelSheet, col, row, var46 + " " + rs.getEmploye().getNom());
                        this.menu.we.setColumnWidth(excelSheet, col, 30);
                        break;
                     case "NJT":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getNjt()).sum());
                        this.menu.we.setColumnWidth(excelSheet, col, 5);
                        break;
                     case "NHS":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getNbrHs()).sum());
                        this.menu.we.setColumnWidth(excelSheet, col, 5);
                        break;
                     case "FTE":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getFte()).sum());
                        this.menu.we.setColumnWidth(excelSheet, col, 5);
                        break;
                     case "AUTR. IND.":
                        this.menu.we.addNumberBorder(excelSheet, col, row, totalGainsNA);
                        break;
                     case "BT":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getBt()).sum());
                        break;
                     case "BNI":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getBni()).sum());
                        break;
                     case "CNSS EMP.":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getCnss()).sum());
                        break;
                     case "CNSS MED.":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getCnss()).sum() * (double)2.0F);
                        break;
                     case "CNSS PAT.":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getCnss()).sum() * (double)13.0F);
                        break;
                     case "CNAM EMP.":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getCnam()).sum());
                        break;
                     case "CNAM PAT.":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getCnam()).sum() / (double)4.0F * (double)5.0F);
                        break;
                     case "ITS":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getIts()).sum());
                        break;
                     case "RCNSS":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getRcnss()).sum());
                        break;
                     case "RCNAM":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getRcnam()).sum());
                        break;
                     case "RITS":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getRits()).sum());
                        break;
                     case "AUTRES RET":
                        this.menu.we.addNumberBorder(excelSheet, col, row, totalRetenuesNA);
                        break;
                     case "NET A PAYER":
                        this.menu.we.addNumberBorder(excelSheet, col, row, dl.stream().filter((var2x) -> var2x.getEmploye().getId() == rs.getEmploye().getId() && var2x.getMotif().getId() == rs.getMotif().getId() && this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(rs.getPeriode()))).mapToDouble((var0) -> var0.getNet()).sum());
                  }
               } else {
                  Rubrique rub = this.menu.pc.rubriqueById(((Number)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(var39, 3)).intValue());
                  Rubriquepaie rp = this.menu.pc.rubriquePaieById(rs.getEmploye(), rub, rs.getMotif(), rs.getPeriode());
                  double montantRubrique = rp != null ? rp.getMontant() : (double)0.0F;
                  this.menu.we.addNumberBorder(excelSheet, col, row, montantRubrique);
               }

               ++col;
            }

            ++valuePB;
            this.progressBar.setValue(valuePB);
         }

         ++row;
      }

      for(int j = 0; j < this.listeColonnesTable.getRowCount(); ++j) {
         int index = ((Number)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(j, 3)).intValue();
         if (index >= 0) {
         }

         ++valuePB;
         this.progressBar.setValue(valuePB);
      }

      this.menu.we.addLabelBold(excelSheet, 0, row + 4, this.menu.paramsGen.getSignataires());
      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void excelMasseSal() throws IOException, WriteException {
      String periodeDu = (new SimpleDateFormat("MM-yyyy")).format((Date)this.tPeriodeDu.getSelectedItem()).toUpperCase();
      String periodeAu = (new SimpleDateFormat("MM-yyyy")).format((Date)this.tPeriodeAu.getSelectedItem()).toUpperCase();
      String fileName = "repport/MS__DU_" + periodeDu + "_AU_" + periodeAu + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Etat paie");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      this.menu.we.addLabelTitre(excelSheet, 0, 1, "MASSE SALARIALE :  Du " + periodeDu + "Au " + periodeAu);
      WriteExcel var10000 = this.menu.we;
      SimpleDateFormat var10004 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var10000.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var10004.format(var10005));
      this.menu.we.addLabelBoldBorderGold(excelSheet, 0, 4, "MAT.");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 1, 4, "EMBAUCHE");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 2, 4, "NOM ET PRENOM");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 3, 4, "POSTE");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 4, 4, "NET");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 5, 4, "CNSS EMP.");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 6, 4, "CNSS MT.");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 7, 4, "CNSS PAT.");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 8, 4, "CNAM EMP.");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 9, 4, "CNAM PAT.");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 10, 4, "ITS");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 11, 4, "BRUT TOTAL");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 12, 4, "TAX APP.");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 13, 4, "MASSE SAL.");
      this.menu.we.setColumnWidth(excelSheet, 0, 5);
      this.menu.we.setColumnWidth(excelSheet, 2, 30);
      this.menu.we.setColumnWidth(excelSheet, 3, 20);
      int row = 5;
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      List<Paie> dl = this.dlPaie;
      dl = (List)dl.stream().filter((var1x) -> var1x.getPeriode().after((Date)this.tPeriodeDu.getSelectedItem()) && var1x.getPeriode().before((Date)this.tPeriodeAu.getSelectedItem())).sorted(Comparator.comparing((var0) -> var0.getEmploye().getId())).collect(Collectors.toList());
      int maxPB = dl.size() * 16;
      this.progressBar.setMaximum(maxPB);
      int valuePB = 0;
      this.progressBar.setValue(valuePB);

      for(Paie rs : dl) {
         double cnssEmp = dl.stream().mapToDouble((var0) -> var0.getCnss()).sum();
         double cnssMT = cnssEmp * (double)2.0F;
         double cnssPatronal = cnssEmp * (double)13.0F;
         double cnamEmp = dl.stream().mapToDouble((var0) -> var0.getCnam()).sum();
         double cnamPatronal = cnamEmp / (double)4.0F * (double)5.0F;
         double brutGeneral = dl.stream().mapToDouble((var0) -> var0.getBt()).sum() + cnssMT + cnssPatronal + cnamPatronal;
         double taxeApp = brutGeneral * 0.006;
         double masseSal = brutGeneral + taxeApp;
         this.menu.we.addNumberBorder(excelSheet, 0, row, Integer.valueOf(rs.getEmploye().getId()).doubleValue());
         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addLabelBorder(excelSheet, 1, row, dateFormat.format(rs.getEmploye().getDateEmbauche()));
         ++valuePB;
         this.progressBar.setValue(valuePB);
         var10000 = this.menu.we;
         String var45 = rs.getEmploye().getPrenom();
         var10000.addLabelBorder(excelSheet, 2, row, var45 + " " + rs.getEmploye().getNom());
         ++valuePB;
         this.progressBar.setValue(valuePB);
         if (rs.getEmploye().getPoste() != null) {
            this.menu.we.addLabelBorder(excelSheet, 3, row, rs.getEmploye().getPoste().getNom());
         }

         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 4, row, dl.stream().mapToDouble((var0) -> var0.getNet()).sum());
         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 5, row, cnssEmp);
         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 6, row, cnssMT);
         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 7, row, cnssPatronal);
         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 8, row, cnamEmp);
         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 9, row, cnamPatronal);
         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 10, row, dl.stream().mapToDouble((var0) -> var0.getIts()).sum());
         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 11, row, brutGeneral);
         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 12, row, taxeApp);
         ++valuePB;
         this.progressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 13, row, masseSal);
         ++valuePB;
         this.progressBar.setValue(valuePB);
         ++row;
      }

      for(int i = 4; i <= 13; ++i) {
         this.menu.we.addNumberBoldBorderSilver(excelSheet, i, row, this.menu.we.columnSum(excelSheet, i, row, 5, row - 1));
         ++valuePB;
         this.progressBar.setValue(valuePB);
      }

      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.tPeriodeDu = new JComboBox();
      this.jLabel23 = new JLabel();
      this.jLabel20 = new JLabel();
      this.tMotif = new JComboBox();
      this.progressBar = new JProgressBar();
      this.panelEP = new JPanel();
      this.jScrollPane11 = new JScrollPane();
      this.listeColonnesTable = new JTable();
      this.btnExport = new JButton();
      this.jLabel25 = new JLabel();
      this.tEtat = new JComboBox();
      this.cCheckAll = new JCheckBox();
      this.btnRefresh = new JButton();
      this.tPeriodeAu = new JComboBox();
      this.jLabel21 = new JLabel();
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
      this.jLabel7.setText("Etats de paies cumul\u00e9es");
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
      this.tPeriodeDu.setFont(new Font("Segoe UI Light", 0, 12));
      this.tPeriodeDu.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriodeDu.addActionListener(new 2(this));
      this.jLabel23.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel23.setForeground(new Color(0, 102, 153));
      this.jLabel23.setText("Motif");
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("P\u00e9riode d\u00e9but");
      this.tMotif.setFont(new Font("Segoe UI Light", 0, 12));
      this.tMotif.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMotif.addActionListener(new 3(this));
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      this.panelEP.setBackground(new Color(255, 255, 255));
      this.panelEP.setBorder(BorderFactory.createTitledBorder((Border)null, "Colonnes du classseur Excel \u00e0 afficher", 0, 0, new Font("Segoe UI Light", 1, 11)));
      this.panelEP.setForeground(new Color(0, 102, 153));
      this.listeColonnesTable.setFont(new Font("Segoe UI Light", 0, 11));
      this.listeColonnesTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listeColonnesTable.setSelectionBackground(new Color(0, 102, 153));
      this.listeColonnesTable.setShowGrid(false);
      this.listeColonnesTable.addMouseListener(new 4(this));
      this.jScrollPane11.setViewportView(this.listeColonnesTable);
      GroupLayout panelEPLayout = new GroupLayout(this.panelEP);
      this.panelEP.setLayout(panelEPLayout);
      panelEPLayout.setHorizontalGroup(panelEPLayout.createParallelGroup(Alignment.LEADING).addGroup(panelEPLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane11, -1, 676, 32767).addContainerGap()));
      panelEPLayout.setVerticalGroup(panelEPLayout.createParallelGroup(Alignment.LEADING).addGroup(panelEPLayout.createSequentialGroup().addComponent(this.jScrollPane11, -1, 340, 32767).addContainerGap()));
      this.btnExport.setBackground(new Color(255, 255, 255));
      this.btnExport.setToolTipText("Exporter la balance vers Excel");
      this.btnExport.setContentAreaFilled(false);
      this.btnExport.setCursor(new Cursor(12));
      this.btnExport.setOpaque(true);
      this.btnExport.addActionListener(new 5(this));
      this.jLabel25.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel25.setForeground(new Color(0, 102, 153));
      this.jLabel25.setText("Document");
      this.tEtat.setFont(new Font("Segoe UI Light", 0, 12));
      this.tEtat.setModel(new DefaultComboBoxModel(new String[]{"Etat de paie", "Masse salariale"}));
      this.tEtat.addActionListener(new 6(this));
      this.cCheckAll.setBackground(new Color(255, 255, 255));
      this.cCheckAll.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCheckAll.setForeground(new Color(0, 102, 153));
      this.cCheckAll.setSelected(true);
      this.cCheckAll.setText("Tout cocher");
      this.cCheckAll.addActionListener(new 7(this));
      this.btnRefresh.setBackground(new Color(255, 255, 255));
      this.btnRefresh.setContentAreaFilled(false);
      this.btnRefresh.setCursor(new Cursor(12));
      this.btnRefresh.setOpaque(true);
      this.btnRefresh.addActionListener(new 8(this));
      this.tPeriodeAu.setFont(new Font("Segoe UI Light", 0, 12));
      this.tPeriodeAu.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriodeAu.addActionListener(new 9(this));
      this.jLabel21.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel21.setForeground(new Color(0, 102, 153));
      this.jLabel21.setText("P\u00e9riode fin");
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.progressBar, -2, 728, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addContainerGap().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.btnRefresh, -2, 35, -2).addGap(28, 28, 28).addComponent(this.cCheckAll, -1, -1, 32767).addGap(229, 229, 229)).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.btnExport, -2, 35, -2).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.panelEP, -2, -1, -2).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriodeDu, 0, -1, 32767).addComponent(this.jLabel20, -2, 144, -2)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriodeAu, 0, -1, 32767).addComponent(this.jLabel21, -2, 144, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tMotif, -2, 150, -2).addComponent(this.jLabel23, -2, 150, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tEtat, -2, 250, -2).addComponent(this.jLabel25, -2, 250, -2))))).addGap(0, 0, 32767))))).addContainerGap()));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(0, 0, 0).addComponent(this.progressBar, -2, -1, -2).addGap(18, 18, 18).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel23).addGap(0, 0, 0).addComponent(this.tMotif, -2, 30, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel20).addGap(0, 0, 0).addComponent(this.tPeriodeDu, -2, 30, -2))).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel25).addGap(0, 0, 0).addComponent(this.tEtat, -2, 30, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel21).addGap(0, 0, 0).addComponent(this.tPeriodeAu, -2, 30, -2))).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.panelEP, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.btnExport, -1, -1, 32767).addComponent(this.btnRefresh, -2, 35, -2).addComponent(this.cCheckAll, -1, -1, 32767)).addGap(2, 2, 2)));
      this.msgLabel.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jPanel1, -1, -1, 32767).addComponent(this.msgLabel, -1, -1, 32767).addComponent(this.pnlBody, -2, 725, 32767)).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlBody, -2, -1, -2).addGap(2, 2, 2).addComponent(this.msgLabel, -2, 22, -2)));
      this.setSize(new Dimension(727, 550));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void btnExportActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      switch (this.tEtat.getSelectedItem().toString()) {
         case "Etat de paie":
            if (this.menu.pc.usedRubID(16) == null) {
               this.menu.viewMessage(this.msgLabel, "Rubrique systeme 16 manquante!", true);
               this.btnExport.setEnabled(true);
            } else {
               Thread t1 = new 10(this);
               t1.start();
            }
            break;
         case "Masse salariale":
            Thread t2 = new 11(this);
            t2.start();
      }

   }

   private void tPeriodeDuActionPerformed(ActionEvent var1) {
      if (this.tPeriodeDu.getItemCount() > 0 && this.tMotif.getItemCount() > 0) {
         this.afficherListeColonnes();
      }

   }

   private void tMotifActionPerformed(ActionEvent var1) {
      if (this.tPeriodeDu.getItemCount() > 0 && this.tMotif.getItemCount() > 0) {
         this.afficherListeColonnes();
      }

   }

   private void tEtatActionPerformed(ActionEvent var1) {
      if (this.tEtat.getSelectedItem().equals("Etat de paie")) {
         this.panelEP.setVisible(true);
         if (this.tPeriodeDu.getItemCount() > 0 && this.tMotif.getItemCount() > 0) {
            this.afficherListeColonnes();
         }
      } else {
         this.panelEP.setVisible(false);
      }

   }

   private void cCheckAllActionPerformed(ActionEvent var1) {
      for(int i = 0; i < this.listeColonnesTable.getRowCount(); ++i) {
         if (this.cCheckAll.isSelected()) {
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).setValueAt(true, i, 0);
         } else {
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).setValueAt(false, i, 0);
         }
      }

   }

   private void btnRefreshActionPerformed(ActionEvent var1) {
      Thread t = new 12(this);
      t.start();
   }

   private void listeColonnesTableMouseClicked(MouseEvent var1) {
   }

   private void tPeriodeAuActionPerformed(ActionEvent var1) {
   }
}
