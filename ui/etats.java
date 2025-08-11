package com.mccmr.ui;

import com.mccmr.entity.Diplome;
import com.mccmr.entity.Employe;
import com.mccmr.entity.EngagementsHistory;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Retenuesaecheances;
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
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
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

public class etats extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   List<EngagementsHistory> dlEngagementsHistory;
   private JButton btnExit;
   private JButton btnExport;
   private JButton btnRefresh;
   private JCheckBox cCheckAll;
   private JLabel jLabel20;
   private JLabel jLabel23;
   private JLabel jLabel24;
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
   private JComboBox<Object> tLangue;
   private JComboBox<Object> tMotif;
   private JComboBox<Object> tPeriode;

   public etats() {
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
      this.menu.remplirCombo("Periode", this.tPeriode);
      Query q = this.menu.entityManager.createQuery("Select p from Paie p");
      q.setMaxResults(1000000);
      this.dlPaie = q.getResultList();
      q = this.menu.entityManager.createQuery("Select p from EngagementsHistory p");
      q.setMaxResults(1000000);
      this.dlEngagementsHistory = q.getResultList();
   }

   private void afficherListeColonnes() {
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      Date periode = (Date)this.tPeriode.getSelectedItem();
      JTable var10000 = this.listeColonnesTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmCustomiseEtatPaie(var10003));
      if (motif != null && periode != null && this.tEtat.getSelectedItem().toString().compareTo("Masse salariale") != 0) {
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "MAT.", "MATRICULE", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "MAT. INT.", "MATRICULE INTERNE", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "GENRE", "GENRE DU SALARIE (F/M)", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "DATE. NAISS.", "DATE DE NAISSANCE", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "NNI", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CAT.", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "EMBAUCHE", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "LIEU TRAVAIL", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "NOM", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "PRENOM", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "DIRECTION GENERALE", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "DIRECTION", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "DEPARTEMENT", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "POSTE", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "SERVICE", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CAT1/UNITE/SCE.", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CAT2/DIVSION/LABO.", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "MODE PAYE", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "BANQUE", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "COMPTE BANQUE", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "NJT", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "PAIE DU", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "PAIE AU", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "NHS", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "FTE", "", -1);
         List<Rubriquepaie> dl = new ArrayList(this.menu.pc.dlRubriquepaie);
         Set<Rubrique> dl2 = ((Map)dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).sorted(Comparator.comparing((var0) -> var0.getRubrique().getId())).collect(Collectors.groupingBy((var0) -> var0.getRubrique()))).keySet();
         List<Rubrique> dlRub2 = new ArrayList(dl2);

         for(Rubrique rs : (List)dlRub2.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, rs.getLibelle(), rs.getLibelle(), rs.getId());
         }

         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "AUTR. IND.", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "BT", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "BNI", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNSS EMP.", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNSS MED.", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNSS PAT.", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNAM EMP.", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNAM PAT.", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "ITS", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "RCNSS", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "RCNAM", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "RITS", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "AUTRES RET", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "SAL. NET", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "NET A PAYER", "", -1);
      }

      this.menu.viewMessage(this.msgLabel, "", false);
      this.menu.viewMessage(this.msgLabel, "", false);
      this.progressBar.setMaximum(100);
      this.progressBar.setValue(0);
   }

   private void afficherListeColonnesAr() {
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      Date periode = (Date)this.tPeriode.getSelectedItem();
      this.menu.mc.getClass();
      JTable var10000 = this.listeColonnesTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmCustomiseEtatPaie(var10003));
      if (motif != null && periode != null && this.tEtat.getSelectedItem().toString().compareTo("Masse salariale") != 0) {
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0627\u0644\u062f\u0644\u064a\u0644.", "\u0627\u0644\u062f\u0644\u064a\u0644", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0627\u0644\u0641\u0623\u0629", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u062a\u0627\u0631\u064a\u062e \u0627\u0644\u0625\u0643\u062a\u062a\u0627\u0628", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0627\u0644\u0625\u0633\u0645 \u0627\u0644\u0643\u0627\u0645\u0644", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0627\u0644\u0648\u0638\u064a\u0641\u0629", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0627\u0644\u0642\u0637\u0627\u0639", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0646\u0648\u0639 \u0627\u0644\u062a\u0633\u062f\u064a\u062f", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0627\u0644\u0645\u0635\u0631\u0641", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0631\u0642\u0645 \u0627\u0644\u062d\u0633\u0627\u0628", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0639\u062f\u062f\u0623\u064a\u0627 \u0645 \u0627\u0644\u0639\u0645\u0644", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0645\u0646", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0625\u0644\u0649", "", -1);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0639.\u0633\u0627\u0639\u0627\u062a \u0625\u0636\u0627\u0641\u064a\u0629", "", -1);
         List<Rubriquepaie> dl = new ArrayList(this.menu.pc.dlRubriquepaie);

         for(Rubrique rs : ((Map)dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).collect(Collectors.groupingBy((var0) -> var0.getRubrique()))).keySet()) {
            ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, rs.getLibelle(), rs.getLibelle(), rs.getId());
         }

         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0639\u0644\u0627\u0648\u0627\u062a \u0623\u062e\u0631\u0649", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0627\u0644\u0623\u062c\u0631 \u0627\u0644\u0639\u0627\u0645", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u062f\u0648\u0646 \u0627\u0644\u0636\u0631\u0627\u0626\u0628", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u062d\u0635\u0629 \u0627\u0644\u0636\u0645\u0627\u0646 \u0627\u0644\u0625\u062c\u062a\u0645\u0627\u0639\u064a \u0644\u0644\u0639\u0627\u0645\u0644.", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u062d\u0635\u0629 \u0627\u0644\u0636\u0645\u0627\u0646  \u0627\u0644\u0625\u062c\u062a\u0645\u0627\u0639\u064a \u0637\u0634", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u062d\u0635\u0629 \u0627\u0644\u0636\u0645\u0627\u0646  \u0627\u0644\u0625\u062c\u062a\u0645\u0627\u0639\u064a \u0631\u0639", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNAM EMP.", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNAM PAT.", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "ITS \u0636\u0631\u064a\u0628\u0629 ", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNSS \u062a\u0639\u0648\u064a\u0636", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "CNAM \u062a\u0639\u0648\u064a\u0636", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "ITS \u062a\u0639\u0648\u064a\u0636", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0625\u0633\u062a\u0642\u0637\u0627\u0639\u0627\u062a \u0623\u062e\u0631\u0649", "", 0);
         ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).addRow(true, "\u0627\u0644\u0623\u062c\u0631 \u0627\u0644\u0635\u0627\u0641\u064a", "", 0);
      }

      this.menu.viewMessage(this.msgLabel, "", false);
      this.menu.viewMessage(this.msgLabel, "", false);
      this.progressBar.setMaximum(100);
      this.progressBar.setValue(0);
   }

   private void excelEtatPaie() throws IOException, WriteException {
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      Date periode = (Date)this.tPeriode.getSelectedItem();
      String motifTitre = motif.getNom().toUpperCase();
      String periodeTitre = (new SimpleDateFormat("MMMM yyyy")).format(periode).toUpperCase();
      String periodeShortTitre = (new SimpleDateFormat("MM-yyyy")).format(periode).toUpperCase();
      String fileName = "repport/EP_" + motifTitre + "_" + periodeShortTitre + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Etat paie");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      String titreDepartement = "";
      this.menu.we.addLabelTitre(excelSheet, 0, 1, "ETAT DE PAIE : " + periodeTitre + " / " + motifTitre + titreDepartement);
      WriteExcel var10000 = this.menu.we;
      SimpleDateFormat var10004 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var10000.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le : " + var10004.format(var10005));
      int row = 5;
      double totalGainsNA = (double)0.0F;
      double totalRetenuesNA = (double)0.0F;
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      int col = 0;

      for(int i = 0; i < this.listeColonnesTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(i, 0)) {
            String colName = ((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(i, 1).toString();
            this.menu.we.addLabelBoldBorderWrap(excelSheet, col, 4, colName);
            ++col;
         }
      }

      List<Paie> dl = this.dlPaie;
      dl = (List)dl.stream().filter((var3x) -> this.menu.df.format(var3x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode)) && var3x.getMotif().getId() == motif.getId()).sorted(Comparator.comparing((var0) -> var0.getEmploye().getId())).collect(Collectors.toList());
      this.menu.we.addLabelTitre(excelSheet, 10, 1, "NB SAL. : " + dl.size());
      int maxPB = dl.size() * this.listeColonnesTable.getRowCount() + this.listeColonnesTable.getRowCount();
      double totalNet = (double)0.0F;
      this.progressBar.setMaximum(maxPB);
      int valuePB = 0;
      this.progressBar.setValue(valuePB);

      for(Paie rs : dl) {
         if (rs.getBt() > (double)0.0F) {
            for(int k = 0; k < this.listeColonnesTable.getRowCount(); ++k) {
               int idRub = ((Number)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(k, 3)).intValue();
               if (!(Boolean)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(k, 0) && idRub > 0) {
                  Rubriquepaie rp = this.menu.pc.rubriquePaieById(rs.getEmploye(), this.menu.pc.rubriqueById(idRub), motif, periode);
                  double montantRubrique = (double)0.0F;
                  if (rp != null) {
                     montantRubrique = rp.getMontant();
                  }

                  if (this.menu.pc.rubriqueById(idRub).getSens().compareTo("G") == 0) {
                     totalGainsNA += montantRubrique;
                  } else {
                     totalRetenuesNA += montantRubrique;
                  }
               }
            }

            col = 0;

            for(int var36 = 0; var36 < this.listeColonnesTable.getRowCount(); ++var36) {
               if ((Boolean)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(var36, 0)) {
                  int index = ((Number)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(var36, 3)).intValue();
                  if (index < 0) {
                     if (this.tLangue.getSelectedItem().toString().equals("Fran\u00e7ais")) {
                        switch (((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(var36, 1).toString()) {
                           case "MAT.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, Integer.valueOf(rs.getEmploye().getId()).doubleValue());
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "MAT. INT.":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getEmploye().getIdPsservice());
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "GENRE":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getEmploye().getSexe());
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "DATE. NAISS.":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getEmploye().getDateNaissance() != null ? dateFormat.format(rs.getEmploye().getDateNaissance()) : "");
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "NNI":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getEmploye().getNni());
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "CAT.":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getCategorie());
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "EMBAUCHE":
                              this.menu.we.addLabelBorder(excelSheet, col, row, dateFormat.format(rs.getEmploye().getDateEmbauche()));
                              break;
                           case "NOM":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getEmploye().getNom());
                              this.menu.we.setColumnWidth(excelSheet, col, 30);
                              break;
                           case "PRENOM":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getEmploye().getPrenom());
                              this.menu.we.setColumnWidth(excelSheet, col, 30);
                              break;
                           case "LIEU TRAVAIL":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getEmploye().getLieuTravail());
                              this.menu.we.setColumnWidth(excelSheet, col, 30);
                              break;
                           case "POSTE":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getPoste());
                              this.menu.we.setColumnWidth(excelSheet, col, 20);
                              break;
                           case "DIRECTION GENERALE":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getDirectiongeneral());
                              this.menu.we.setColumnWidth(excelSheet, col, 20);
                              break;
                           case "DIRECTION":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getDirection());
                              this.menu.we.setColumnWidth(excelSheet, col, 20);
                              break;
                           case "DEPARTEMENT":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getDepartement());
                              this.menu.we.setColumnWidth(excelSheet, col, 20);
                              break;
                           case "SERVICE":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getActivite());
                              this.menu.we.setColumnWidth(excelSheet, col, 20);
                              break;
                           case "CAT1/UNITE/SCE.":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getClassification());
                              this.menu.we.setColumnWidth(excelSheet, col, 20);
                              break;
                           case "CAT2/DIVSION/LABO.":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getStatut());
                              this.menu.we.setColumnWidth(excelSheet, col, 20);
                              break;
                           case "MODE PAYE":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getModePaiement());
                              this.menu.we.setColumnWidth(excelSheet, col, 10);
                              break;
                           case "BANQUE":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getModePaiement().equalsIgnoreCase("Virement") ? rs.getBanque() : "-");
                              this.menu.we.setColumnWidth(excelSheet, col, 15);
                              break;
                           case "COMPTE BANQUE":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getNoCompteBanque());
                              this.menu.we.setColumnWidth(excelSheet, col, 20);
                              break;
                           case "NJT":
                              if (rs.getNjt() < (double)30.0F) {
                                 this.menu.we.addNumberBoldBorder(excelSheet, col, row, rs.getNjt());
                              }

                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getNjt());
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "PAIE DU":
                              this.menu.we.addLabelBorder(excelSheet, col, row, dateFormat.format(rs.getPaieDu()));
                              break;
                           case "PAIE AU":
                              this.menu.we.addLabelBorder(excelSheet, col, row, dateFormat.format(rs.getPaieAu()));
                              break;
                           case "NHS":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getNbrHs());
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "FTE":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getFte() != null ? Double.valueOf(rs.getFte()) : (double)0.0F);
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "AUTR. IND.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, totalGainsNA);
                              break;
                           case "BT":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getBt());
                              break;
                           case "BNI":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getBni());
                              break;
                           case "CNSS EMP.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getCnss());
                              break;
                           case "CNSS MED.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getCnss() * (double)2.0F);
                              break;
                           case "CNSS PAT.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getCnss() * (double)13.0F);
                              break;
                           case "CNAM EMP.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getCnam());
                              break;
                           case "CNAM PAT.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getCnam() / (double)4.0F * (double)5.0F);
                              break;
                           case "ITS":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getIts());
                              break;
                           case "RCNSS":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getRcnss());
                              break;
                           case "RCNAM":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getRcnam());
                              break;
                           case "RITS":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getRits());
                              break;
                           case "AUTRES RET":
                              this.menu.we.addNumberBorder(excelSheet, col, row, totalRetenuesNA);
                              break;
                           case "SAL. NET":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getNet() + rs.getRetenuesNet());
                              break;
                           case "NET A PAYER":
                              if (rs.getNet() >= (double)0.0F) {
                                 this.menu.we.addNumberBorder(excelSheet, col, row, rs.getNet());
                              } else {
                                 this.menu.we.addNumberBoldBorderGold(excelSheet, col, row, rs.getNet());
                              }

                              totalNet += rs.getNet();
                        }
                     } else {
                        switch (((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(var36, 1).toString()) {
                           case "\u0627\u0644\u062f\u0644\u064a\u0644.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, Integer.valueOf(rs.getEmploye().getId()).doubleValue());
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "\u0627\u0644\u0641\u0623\u0629":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getCategorie());
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "\u062a\u0627\u0631\u064a\u062e \u0627\u0644\u0625\u0643\u062a\u062a\u0627\u0628":
                              this.menu.we.addLabelBorder(excelSheet, col, row, dateFormat.format(rs.getEmploye().getDateEmbauche()));
                              break;
                           case "\u0627\u0644\u0625\u0633\u0645 \u0627\u0644\u0643\u0627\u0645\u0644":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getEmploye().getNom());
                              this.menu.we.setColumnWidth(excelSheet, col, 20);
                              break;
                           case "\u0627\u0644\u0648\u0638\u064a\u0641\u0629":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getPoste());
                              this.menu.we.setColumnWidth(excelSheet, col, 15);
                              break;
                           case "\u0627\u0644\u0642\u0637\u0627\u0639":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getDepartement());
                              this.menu.we.setColumnWidth(excelSheet, col, 15);
                              break;
                           case "\u0646\u0648\u0639 \u0627\u0644\u062a\u0633\u062f\u064a\u062f":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getModePaiement());
                              break;
                           case "\u0627\u0644\u0645\u0635\u0631\u0641":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getBanque());
                              this.menu.we.setColumnWidth(excelSheet, col, 15);
                              break;
                           case "\u0631\u0642\u0645 \u0627\u0644\u062d\u0633\u0627\u0628":
                              this.menu.we.addLabelBorder(excelSheet, col, row, rs.getNoCompteBanque());
                              this.menu.we.setColumnWidth(excelSheet, col, 20);
                              break;
                           case "\u0639\u062f\u062f\u0623\u064a\u0627 \u0645 \u0627\u0644\u0639\u0645\u0644":
                              if (rs.getNjt() < (double)30.0F) {
                                 this.menu.we.addNumberBoldBorder(excelSheet, col, row, rs.getNjt());
                              }

                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getNjt());
                              this.menu.we.setColumnWidth(excelSheet, col, 5);
                              break;
                           case "\u0645\u0646":
                              this.menu.we.addLabelBorder(excelSheet, col, row, dateFormat.format(rs.getPaieDu()));
                              break;
                           case "\u0625\u0644\u0649":
                              this.menu.we.addLabelBorder(excelSheet, col, row, dateFormat.format(rs.getPaieAu()));
                              break;
                           case "\u0639.\u0633\u0627\u0639\u0627\u062a \u0625\u0636\u0627\u0641\u064a\u0629":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getNbrHs());
                              break;
                           case "\u0639\u0644\u0627\u0648\u0627\u062a \u0623\u062e\u0631\u0649":
                              this.menu.we.addNumberBorder(excelSheet, col, row, totalGainsNA);
                              break;
                           case "\u0627\u0644\u0623\u062c\u0631 \u0627\u0644\u0639\u0627\u0645":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getBt());
                              break;
                           case "\u062f\u0648\u0646 \u0627\u0644\u0636\u0631\u0627\u0626\u0628":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getBni());
                              break;
                           case "\u062d\u0635\u0629 \u0627\u0644\u0636\u0645\u0627\u0646 \u0627\u0644\u0625\u062c\u062a\u0645\u0627\u0639\u064a \u0644\u0644\u0639\u0627\u0645\u0644.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getCnss());
                              break;
                           case "\u062d\u0635\u0629 \u0627\u0644\u0636\u0645\u0627\u0646  \u0627\u0644\u0625\u062c\u062a\u0645\u0627\u0639\u064a \u0637\u0634":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getCnss() * (double)2.0F);
                              break;
                           case "\u062d\u0635\u0629 \u0627\u0644\u0636\u0645\u0627\u0646  \u0627\u0644\u0625\u062c\u062a\u0645\u0627\u0639\u064a \u0631\u0639":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getCnss() * (double)13.0F);
                              break;
                           case "CNAM EMP.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getCnam());
                              break;
                           case "CNAM PAT.":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getCnam() / (double)4.0F * (double)5.0F);
                              break;
                           case "ITS \u0636\u0631\u064a\u0628\u0629 ":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getIts());
                              break;
                           case "CNSS \u062a\u0639\u0648\u064a\u0636":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getRcnss());
                              break;
                           case "CNAM \u062a\u0639\u0648\u064a\u0636":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getRcnam());
                              break;
                           case "ITS \u062a\u0639\u0648\u064a\u0636":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getRits());
                              break;
                           case "\u0625\u0633\u062a\u0642\u0637\u0627\u0639\u0627\u062a \u0623\u062e\u0631\u0649":
                              this.menu.we.addNumberBorder(excelSheet, col, row, totalRetenuesNA);
                              break;
                           case "\u0627\u0644\u0623\u062c\u0631 \u0627\u0644\u0635\u0627\u0641\u064a":
                              this.menu.we.addNumberBorder(excelSheet, col, row, rs.getNet());
                              totalNet += rs.getNet();
                        }
                     }
                  } else {
                     Rubriquepaie rp = this.menu.pc.rubriquePaieById(rs.getEmploye(), this.menu.pc.rubriqueById(index), motif, periode);
                     double montantRubrique = (double)0.0F;
                     if (rp != null) {
                        montantRubrique = rp.getMontant();
                     }

                     this.menu.we.addNumberBorder(excelSheet, col, row, montantRubrique);
                  }

                  ++valuePB;
                  this.progressBar.setValue(valuePB);
                  ++col;
               }
            }

            ++row;
         }
      }

      for(int j = 0; j < this.listeColonnesTable.getRowCount(); ++j) {
         int index = ((Number)((ModelClass.tmCustomiseEtatPaie)this.listeColonnesTable.getModel()).getValueAt(j, 3)).intValue();
         this.menu.we.addNumberBoldBorderSilver(excelSheet, j, row, this.menu.we.columnSum(excelSheet, j, row, 5, row - 1));
         ++valuePB;
         this.progressBar.setValue(valuePB);
      }

      var10000 = this.menu.we;
      int var10003 = row + 2;
      String var44 = this.menu.nl.convertirEnMRO((double)((long)totalNet), "FR");
      var10000.addLabelBold(excelSheet, 0, var10003, "Arr\u00eat\u00e9 \u00e0 la somme de :" + var44);
      this.menu.we.addLabelBold(excelSheet, 0, row + 4, this.menu.paramsGen.getSignataires());
      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void excelEtatCongeSimilator() throws IOException, WriteException {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      String periodeTitre = (new SimpleDateFormat("MMMM yyyy")).format(periode).toUpperCase();
      String periodeShortTitre = (new SimpleDateFormat("MM-yyyy")).format(periode).toUpperCase();
      String fileName = "repport/ESC__" + periodeShortTitre + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Etat paie");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      String titreDepartement = "";
      this.menu.we.addLabelTitre(excelSheet, 0, 1, "ETAT DE SIMULATION DE CONGES : " + periodeTitre + " / " + titreDepartement);
      WriteExcel var10000 = this.menu.we;
      SimpleDateFormat var10004 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var10000.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le : " + var10004.format(var10005));
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 0, 4, "ID SAL.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 1, 4, "PRENOM DU SALARIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 2, 4, "NOM DU SALARIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 3, 4, "DEPARTEMENT");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, 4, "DATE EMBAUCHE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 4, "POSTE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, 4, "CATEGORIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, 4, "N\u00b0CNSS");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 8, 4, "N\u00b0CNAM");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 9, 4, "BANQUE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 10, 4, "N\u00b0COMPTE BANQUE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 11, 4, "DER. DEPART");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 12, 4, "JOURS DUS");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 13, 4, "IND. CONGES BRUT");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 14, 4, "CNSS");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 15, 4, "CNSS MT");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 16, 4, "CNSS PAT.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 17, 4, "CNAM");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 18, 4, "CNAM PAT");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 19, 4, "ITS");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 20, 4, "NET");
      int row = 5;
      List<Employe> dataListInit = new ArrayList((Collection)this.menu.employeFrame.dataListInit.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList()));
      int maxPB = this.menu.employeFrame.dataListInit.size();
      this.progressBar.setMaximum(maxPB);
      int valuePB = 0;
      this.progressBar.setValue(valuePB);

      for(Employe employe : dataListInit) {
         double brutConge = (this.menu.pc.cumulTypeById(employe, "BI") + this.menu.pc.cumulTypeById(employe, "BNI")) / (double)12.0F;
         double cnssConge = !employe.isDetacheCnss() && this.menu.motifCNG.isEmployeSoumisCnss() ? this.menu.pc.CNSSm(brutConge, (double)1.0F, 2018) : (double)0.0F;
         double cnamConge = !employe.isDetacheCnam() && this.menu.motifCNG.isEmployeSoumisCnam() ? this.menu.pc.CNAMm(brutConge) : (double)0.0F;
         double itsConge = this.menu.pc.ITSm(2018, brutConge, cnssConge, cnamConge, brutConge, (double)0.0F, (double)1.0F, employe.isExpatrie());
         this.menu.we.addNumberBorder(excelSheet, 0, row, Integer.valueOf(employe.getId()).doubleValue());
         this.menu.we.addLabelBorder(excelSheet, 1, row, employe.getPrenom());
         this.menu.we.addLabelBorder(excelSheet, 2, row, employe.getNom());
         this.menu.we.addLabelBorder(excelSheet, 3, row, employe.getDepartement() == null ? " " : employe.getDepartement().getNom());
         this.menu.we.addLabelBorder(excelSheet, 4, row, this.menu.fdf.format(employe.getDateEmbauche()));
         this.menu.we.addLabelBorder(excelSheet, 5, row, employe.getPoste() == null ? " " : employe.getPoste().getNom());
         this.menu.we.addLabelBorder(excelSheet, 6, row, employe.getGrillesalairebase() == null ? " " : employe.getGrillesalairebase().getCategorie());
         this.menu.we.addLabelBorder(excelSheet, 7, row, employe.getNoCnss());
         this.menu.we.addLabelBorder(excelSheet, 8, row, employe.getNoCnam());
         this.menu.we.addLabelBorder(excelSheet, 9, row, employe.getBanque() == null ? " " : employe.getBanque().getNom());
         this.menu.we.addLabelBorder(excelSheet, 10, row, employe.getNoCompteBanque());
         this.menu.we.addLabelBorder(excelSheet, 11, row, this.menu.fdf.format(this.menu.pc.dernierDepart(employe)));
         this.menu.we.addNumberBorder(excelSheet, 12, row, Integer.valueOf(this.menu.pc.nbJrsCongeMerite(employe)).doubleValue());
         this.menu.we.addNumberBorder(excelSheet, 13, row, brutConge);
         this.menu.we.addNumberBorder(excelSheet, 14, row, cnssConge);
         this.menu.we.addNumberBorder(excelSheet, 15, row, cnssConge * (double)2.0F);
         this.menu.we.addNumberBorder(excelSheet, 16, row, cnssConge * (double)13.0F);
         this.menu.we.addNumberBorder(excelSheet, 17, row, cnamConge);
         this.menu.we.addNumberBorder(excelSheet, 18, row, cnamConge / (double)4.0F * (double)5.0F);
         this.menu.we.addNumberBorder(excelSheet, 19, row, itsConge);
         this.menu.we.addNumberBorder(excelSheet, 20, row, brutConge - cnssConge - cnamConge - itsConge);
         ++valuePB;
         this.progressBar.setValue(valuePB);
         ++row;
      }

      for(int i = 13; i <= 20; ++i) {
         this.menu.we.addNumberBoldBorderSilver(excelSheet, i, row, this.menu.we.columnSum(excelSheet, i, row, 5, row - 1));
         ++valuePB;
         this.progressBar.setValue(valuePB);
      }

      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void excelMasseSal() throws IOException, WriteException {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      String periodeTitre = (new SimpleDateFormat("MMMM yyyy")).format(periode).toUpperCase();
      String periodeShortTitre = (new SimpleDateFormat("MM-yyyy")).format(periode).toUpperCase();
      String titreDepartement = "";
      String fileName = "repport/MS_" + periodeShortTitre + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Etat paie");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      this.menu.we.addLabelTitre(excelSheet, 0, 1, "MASSE SALARIALE : " + periodeTitre + titreDepartement);
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
      dl = (List)dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).sorted(Comparator.comparing((var0) -> var0.getEmploye().getId())).collect(Collectors.toList());
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
         String var47 = rs.getEmploye().getPrenom();
         var10000.addLabelBorder(excelSheet, 2, row, var47 + " " + rs.getEmploye().getNom());
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

   private void excelListEng() throws IOException, WriteException {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      String fileName = "eng_history/LEng_" + (new SimpleDateFormat("MMMM-yyyy")).format(periode) + ".xls";
      this.menu.we.afficherExcel(fileName);
   }

   private void excelListEng0() throws IOException, WriteException {
      String fileName = "repport/LEng_" + (new SimpleDateFormat("MMMM-yyyy")).format(this.menu.paramsGen.getPeriodeCourante()) + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Liste Engagements");
      Date periode = (Date)this.tPeriode.getSelectedItem();
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      WriteExcel var10000 = this.menu.we;
      String var10004 = (new SimpleDateFormat("MMMM-yyyy")).format(periode).toUpperCase();
      var10000.addLabelTitre(excelSheet, 0, 1, "LIST ENGAGEMENT : " + var10004);
      var10000 = this.menu.we;
      SimpleDateFormat var13 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var10000.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var13.format(var10005));
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 0, 4, "ID SAL.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 1, 4, "PRENOM DU SALARIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 2, 4, "NOM DU SALARIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 3, 4, "DATE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, 4, "NOTE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 4, "CAPITAL");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, 4, "ENCOURS");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, 4, "TOTAL REG.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 8, 4, "ECHEANCE");
      int row = 5;
      List<EngagementsHistory> dl = this.dlEngagementsHistory;
      dl = (List)dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).sorted(Comparator.comparing((var0) -> var0.getIdSalarie())).collect(Collectors.toList());
      int maxPB = dl.size();
      this.progressBar.setMaximum(maxPB);
      int valuePB = 0;
      this.progressBar.setValue(valuePB);

      for(EngagementsHistory r : dl) {
         this.menu.we.addNumberBorder(excelSheet, 0, row, Integer.valueOf(r.getIdSalarie()).doubleValue());
         this.menu.we.addLabelBorder(excelSheet, 1, row, r.getPrenomSalarie());
         this.menu.we.addLabelBorder(excelSheet, 2, row, r.getNomSalarie());
         this.menu.we.addLabelBorder(excelSheet, 3, row, this.menu.fdf.format(r.getDateAccord()));
         this.menu.we.addLabelBorder(excelSheet, 4, row, r.getNote());
         this.menu.we.addNumberBorder(excelSheet, 5, row, r.getCapital());
         this.menu.we.addNumberBorder(excelSheet, 6, row, r.getEncours());
         this.menu.we.addNumberBorder(excelSheet, 7, row, r.getTotalRegle());
         this.menu.we.addNumberBorder(excelSheet, 8, row, r.getEcheance());
         ++valuePB;
         this.progressBar.setValue(valuePB);
         ++row;
      }

      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void excelListPersonnel() throws IOException, WriteException {
      String fileName = "repport/LP_" + (new SimpleDateFormat("MMMM-yyyy")).format(this.menu.paramsGen.getPeriodeCourante()) + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Liste Personnel");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      WriteExcel var10000 = this.menu.we;
      String var10004 = (new SimpleDateFormat("MMMM-yyyy")).format(this.menu.paramsGen.getPeriodeCourante()).toUpperCase();
      var10000.addLabelTitre(excelSheet, 0, 1, "LIST DU PERSONNEL : " + var10004);
      var10000 = this.menu.we;
      SimpleDateFormat var13 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var10000.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var13.format(var10005));
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 0, 4, "ID SAL.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 1, 4, "PRENOM DU SALARIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 2, 4, "NOM DU SALARIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 3, 4, "NOM DU PERE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, 4, "NOM DE LA MERE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 4, "DATE NAISS.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, 4, "LIEU NAISS.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, 4, "GENRE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 8, 4, "SF");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 9, 4, "NB ENF.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 10, 4, "NATIONALITE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 11, 4, "NNI");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 12, 4, "N\u00b0PASSPORT");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 13, 4, "PASSPORT L.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 14, 4, "PASSPORT E.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 15, 4, "TELEPHONE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 16, 4, "E-MAIL");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 17, 4, "N\u00b0PERMIS TRAVAIL");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 18, 4, "PERMIS L.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 19, 4, "PERMIS E.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 20, 4, "VISA DEBUT");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 21, 4, "VISA FIN");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 22, 4, "DEPARTEMENT");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 23, 4, "DATE EMBAUCHE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 24, 4, "POSTE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 25, 4, "CATEGORIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 26, 4, "ID POINTEUSE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 27, 4, "N\u00b0CNSS");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 28, 4, "N\u00b0CNAM");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 29, 4, "BANQUE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 30, 4, "N\u00b0COMPTE BANQUE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 31, 4, "ACTIF");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 32, 4, "UNITE/SCE.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 33, 4, "LABO./DIV.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 34, 4, "ACTIVITE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 35, 4, "BUDGET ANUEL");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 36, 4, "CUMUL AQUI");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 37, 4, "EQUART");
      int row = 5;
      int maxPB = this.menu.employeFrame.dataListInit.size();
      double budgetAqui = (double)0.0F;
      this.progressBar.setMaximum(maxPB);
      int valuePB = 0;
      this.progressBar.setValue(valuePB);

      for(Employe employe : this.menu.employeFrame.dataListInit) {
         this.menu.we.addNumberBorder(excelSheet, 0, row, Integer.valueOf(employe.getId()).doubleValue());
         this.menu.we.addLabelBorder(excelSheet, 1, row, employe.getPrenom());
         this.menu.we.addLabelBorder(excelSheet, 2, row, employe.getNom());
         this.menu.we.addLabelBorder(excelSheet, 3, row, employe.getPere());
         this.menu.we.addLabelBorder(excelSheet, 4, row, employe.getMere());
         this.menu.we.addLabelBorder(excelSheet, 5, row, employe.getDateNaissance() == null ? "-" : this.menu.fdf.format(employe.getDateNaissance()));
         this.menu.we.addLabelBorder(excelSheet, 6, row, employe.getLieuNaissance());
         this.menu.we.addLabelBorder(excelSheet, 7, row, employe.getSexe());
         this.menu.we.addLabelBorder(excelSheet, 8, row, employe.getSituationFamiliale());
         this.menu.we.addNumberBorder(excelSheet, 9, row, (double)this.menu.pc.enfantNbByEmp(employe));
         this.menu.we.addLabelBorder(excelSheet, 10, row, employe.getNationalite());
         this.menu.we.addLabelBorder(excelSheet, 11, row, employe.getNni());
         this.menu.we.addLabelBorder(excelSheet, 12, row, employe.getNoPassport());
         if (employe.getDateLivraisonPassport() != null) {
            this.menu.we.addLabelBorder(excelSheet, 13, row, this.menu.fdf.format(employe.getDateLivraisonPassport()));
         }

         if (employe.getDateExpirationPassport() != null) {
            this.menu.we.addLabelBorder(excelSheet, 14, row, this.menu.fdf.format(employe.getDateExpirationPassport()));
         }

         this.menu.we.addLabelBorder(excelSheet, 15, row, employe.getTelephone());
         this.menu.we.addLabelBorder(excelSheet, 16, row, employe.getEmail());
         this.menu.we.addLabelBorder(excelSheet, 17, row, employe.getNoPermiTravail());
         if (employe.getDateLivraisonPermiTravail() != null) {
            this.menu.we.addLabelBorder(excelSheet, 18, row, this.menu.fdf.format(employe.getDateLivraisonPermiTravail()));
         }

         if (employe.getDateExpirationPermiTravail() != null) {
            this.menu.we.addLabelBorder(excelSheet, 19, row, this.menu.fdf.format(employe.getDateExpirationPermiTravail()));
         }

         if (employe.getDateDebutVisa() != null) {
            this.menu.we.addLabelBorder(excelSheet, 20, row, this.menu.fdf.format(employe.getDateDebutVisa()));
         }

         if (employe.getDateFinVisa() != null) {
            this.menu.we.addLabelBorder(excelSheet, 21, row, this.menu.fdf.format(employe.getDateFinVisa()));
         }

         this.menu.we.addLabelBorder(excelSheet, 22, row, employe.getDepartement() == null ? " " : employe.getDepartement().getNom());
         this.menu.we.addLabelBorder(excelSheet, 23, row, employe.getDateEmbauche() != null ? this.menu.fdf.format(employe.getDateEmbauche()) : "-");
         this.menu.we.addLabelBorder(excelSheet, 24, row, employe.getPoste() == null ? " " : employe.getPoste().getNom());
         this.menu.we.addLabelBorder(excelSheet, 25, row, employe.getGrillesalairebase() == null ? " " : employe.getGrillesalairebase().getCategorie());
         this.menu.we.addNumberBorder(excelSheet, 26, row, (double)(employe.getIdSalariePointeuse() != null ? employe.getIdSalariePointeuse() : 0));
         this.menu.we.addLabelBorder(excelSheet, 27, row, employe.getNoCnss());
         this.menu.we.addLabelBorder(excelSheet, 28, row, employe.getNoCnam());
         this.menu.we.addLabelBorder(excelSheet, 29, row, employe.getBanque() == null ? " " : employe.getBanque().getNom());
         this.menu.we.addLabelBorder(excelSheet, 30, row, employe.getNoCompteBanque());
         this.menu.we.addLabelBorder(excelSheet, 31, row, employe.isActif() ? "Oui" : "Non");
         this.menu.we.addLabelBorder(excelSheet, 32, row, employe.getClassification());
         this.menu.we.addLabelBorder(excelSheet, 33, row, employe.getStatut());
         this.menu.we.addLabelBorder(excelSheet, 34, row, employe.getActivite() == null ? " " : employe.getActivite().getNom());
         this.menu.we.addNumberBorder(excelSheet, 35, row, employe.getBudgetannuel() != null ? employe.getBudgetannuel() : (double)0.0F);
         budgetAqui = this.menu.pc.cumulBTAnnuel(employe);
         this.menu.we.addNumberBorder(excelSheet, 36, row, budgetAqui);
         this.menu.we.addNumberBorder(excelSheet, 37, row, employe.getBudgetannuel() != null ? employe.getBudgetannuel() - budgetAqui : (double)0.0F);
         ++valuePB;
         this.progressBar.setValue(valuePB);
         ++row;
      }

      this.menu.we.addLabelBold(excelSheet, 0, row + 4, this.menu.paramsGen.getSignataires());
      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void excelListPersonnelDip() throws IOException, WriteException {
      String fileName = "repport/LP_" + (new SimpleDateFormat("MMMM-yyyy")).format(this.menu.paramsGen.getPeriodeCourante()) + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Liste Personnel");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      WriteExcel var10000 = this.menu.we;
      String var10004 = (new SimpleDateFormat("MMMM-yyyy")).format(this.menu.paramsGen.getPeriodeCourante()).toUpperCase();
      var10000.addLabelTitre(excelSheet, 0, 1, "LIST DU PERSONNEL / DIPLOME : " + var10004);
      var10000 = this.menu.we;
      SimpleDateFormat var15 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var10000.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var15.format(var10005));
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 0, 4, "ID SAL.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 1, 4, "PRENOM DU SALARIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 2, 4, "NOM DU SALARIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 3, 4, "DIPLOME");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, 4, "DATE OBT.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 4, "DEGRE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, 4, "ETABLISSEMENT");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, 4, "DOMAINE");
      int row = 5;
      Query q = this.menu.entityManager.createQuery("Select p from Diplome p");
      q.setMaxResults(1000000);
      List<Diplome> dlDiplome = q.getResultList();
      int maxPB = this.menu.employeFrame.dataListInit.size();
      this.progressBar.setMaximum(maxPB);
      int valuePB = 0;
      this.progressBar.setValue(valuePB);

      for(Employe employe : this.menu.employeFrame.dataListInit) {
         for(Diplome diplome : (List)dlDiplome.stream().filter((var1x) -> var1x.getEmploye().getId() == employe.getId()).sorted(Comparator.comparing((var0) -> var0.getEmploye().getId())).collect(Collectors.toList())) {
            this.menu.we.addNumberBorder(excelSheet, 0, row, (double)employe.getId() * (double)1.0F);
            this.menu.we.addLabelBorder(excelSheet, 1, row, employe.getPrenom());
            this.menu.we.addLabelBorder(excelSheet, 2, row, employe.getNom());
            this.menu.we.addLabelBorder(excelSheet, 3, row, diplome.getNom());
            this.menu.we.addLabelBorder(excelSheet, 4, row, this.menu.fdf.format(diplome.getDateObtention()));
            this.menu.we.addLabelBorder(excelSheet, 5, row, diplome.getDegre());
            this.menu.we.addLabelBorder(excelSheet, 6, row, diplome.getEtablissement());
            this.menu.we.addLabelBorder(excelSheet, 7, row, diplome.getDomaine());
            ++valuePB;
            this.progressBar.setValue(valuePB);
            ++row;
         }
      }

      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void excelListEngCourant(boolean var1) throws IOException, WriteException {
      String var10000 = (new SimpleDateFormat("MMMM-yyyy")).format(this.menu.paramsGen.getPeriodeCourante());
      String fileName = "repport/LEng_" + var10000 + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Liste Engagements");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      WriteExcel var14 = this.menu.we;
      String var10004 = (new SimpleDateFormat("MMMM-yyyy")).format(this.menu.paramsGen.getPeriodeCourante()).toUpperCase();
      var14.addLabelTitre(excelSheet, 0, 1, "LIST ENGAGEMENT : " + var10004);
      var14 = this.menu.we;
      var10004 = (new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss")).format(new Date());
      var14.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var10004);
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 0, 4, "ID SAL.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 1, 4, "PRENOM DU SALARIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 2, 4, "NOM DU SALARIE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 3, 4, "DATE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, 4, "NOTE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 4, "CAPITAL");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, 4, "ENCOURS");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, 4, "TOTAL REG.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 8, 4, "ECHEANCE");
      int row = 5;
      EntityManager var16 = this.menu.entityManager;
      int var10001 = this.menu.pc.usedRubID(16).getId();
      Query q = var16.createQuery("Select p from Retenuesaecheances p where p.rubrique = " + var10001 + (onlyActif ? " and p.solde=0 and p.active=1 " : "") + " order by p.employe");
      q.setMaxResults(1000000);
      List<Retenuesaecheances> dlRE = q.getResultList();
      int maxPB = this.menu.employeFrame.dataListInit.size();
      this.progressBar.setMaximum(maxPB);
      int valuePB = 0;
      this.progressBar.setValue(valuePB);
      if (onlyActif) {
         dlRE.stream().filter((var0) -> var0.getEmploye().isActif()).collect(Collectors.toList());
      }

      for(Retenuesaecheances r : dlRE) {
         if (r.getEmploye() != null) {
            double tr = this.menu.pc.totalReglementRetAE(r);
            this.menu.we.addNumberBorder(excelSheet, 0, row, Integer.valueOf(r.getEmploye().getId()).doubleValue());
            this.menu.we.addLabelBorder(excelSheet, 1, row, r.getEmploye().getPrenom());
            this.menu.we.addLabelBorder(excelSheet, 2, row, r.getEmploye().getNom());
            this.menu.we.addLabelBorder(excelSheet, 3, row, this.menu.fdf.format(r.getDateAccord()));
            this.menu.we.addLabelBorder(excelSheet, 4, row, r.getNote());
            this.menu.we.addNumberBorder(excelSheet, 5, row, r.getCapital());
            this.menu.we.addNumberBorder(excelSheet, 6, row, r.getCapital() - tr);
            this.menu.we.addNumberBorder(excelSheet, 7, row, tr);
            this.menu.we.addNumberBorder(excelSheet, 8, row, r.getEcheance());
            ++valuePB;
            this.progressBar.setValue(valuePB);
            ++row;
         }
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
      this.tPeriode = new JComboBox();
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
      this.jLabel24 = new JLabel();
      this.tLangue = new JComboBox();
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
      this.jLabel7.setText("Etats");
      this.jLabel7.setToolTipText("");
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addPreferredGap(ComponentPlacement.RELATED, 166, 32767).addComponent(this.btnExit, -2, 39, -2).addGap(19, 19, 19)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.tPeriode.setFont(new Font("Segoe UI Light", 0, 12));
      this.tPeriode.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriode.addActionListener(new 2(this));
      this.jLabel23.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel23.setForeground(new Color(0, 102, 153));
      this.jLabel23.setText("Motif");
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("P\u00e9riode");
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
      this.tEtat.setModel(new DefaultComboBoxModel(new String[]{"Etat de paie", "Masse salariale", "Engagements", "Simulation de cong\u00e9s", "Liste du personnel", "Dipl\u00f4mes/Formations"}));
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
      this.jLabel24.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel24.setForeground(new Color(0, 102, 153));
      this.jLabel24.setText("Langue");
      this.tLangue.setFont(new Font("Segoe UI Light", 0, 12));
      this.tLangue.setModel(new DefaultComboBoxModel(new String[]{"Fran\u00e7ais", "Arabe"}));
      this.tLangue.addActionListener(new 9(this));
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.progressBar, -2, 728, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addContainerGap().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.btnRefresh, -2, 35, -2).addGap(28, 28, 28).addComponent(this.cCheckAll, -1, -1, 32767).addGap(229, 229, 229)).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriode, 0, -1, 32767).addComponent(this.jLabel20, -2, 144, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tMotif, -2, 150, -2).addComponent(this.jLabel23, -2, 150, -2)).addGap(0, 0, 32767)))).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.btnExport, -2, 35, -2).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addGroup(pnlBodyLayout.createSequentialGroup().addGap(318, 318, 318).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tLangue, -2, 100, -2).addComponent(this.jLabel24, -2, 100, -2)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tEtat, -2, 250, -2).addComponent(this.jLabel25, -2, 250, -2))).addGroup(pnlBodyLayout.createSequentialGroup().addContainerGap().addComponent(this.panelEP, -2, -1, -2)))).addGap(0, 0, 32767))).addContainerGap()));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(0, 0, 0).addComponent(this.progressBar, -2, -1, -2).addGap(18, 18, 18).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel23).addGap(0, 0, 0).addComponent(this.tMotif, -2, 30, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel20).addGap(0, 0, 0).addComponent(this.tPeriode, -2, 30, -2))).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel25).addGap(0, 0, 0).addComponent(this.tEtat, -2, 30, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel24).addGap(0, 0, 0).addComponent(this.tLangue, -2, 30, -2))).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.panelEP, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.btnExport, -1, -1, 32767).addComponent(this.btnRefresh, -2, 35, -2).addComponent(this.cCheckAll, -1, -1, 32767)).addGap(5, 5, 5)));
      this.msgLabel.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jPanel1, -1, -1, 32767).addComponent(this.msgLabel, -1, -1, 32767).addComponent(this.pnlBody, -2, 725, 32767)).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -1, -1, 32767).addGap(0, 0, 0).addComponent(this.msgLabel, -2, 22, -2)));
      this.setSize(new Dimension(727, 612));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void btnExportActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      switch (this.tEtat.getSelectedItem().toString()) {
         case "Engagements":
            if (this.menu.pc.usedRubID(16) == null) {
               this.menu.viewMessage(this.msgLabel, "Rubrique systeme 16 manquante!", true);
               this.btnExport.setEnabled(true);
            } else {
               Thread t1 = new 10(this);
               t1.start();
            }
            break;
         case "Etat de paie":
            Thread t2 = new 11(this);
            t2.start();
            break;
         case "Simulation de cong\u00e9s":
            Thread t3 = new 12(this);
            t3.start();
            break;
         case "Masse salariale":
            Thread t4 = new 13(this);
            t4.start();
            break;
         case "Liste du personnel":
            Thread t5 = new 14(this);
            t5.start();
            break;
         case "Dipl\u00f4mes/Formations":
            Thread t6 = new 15(this);
            t6.start();
      }

   }

   private void tPeriodeActionPerformed(ActionEvent var1) {
      if (this.tPeriode.getItemCount() > 0 && this.tMotif.getItemCount() > 0) {
         this.afficherListeColonnes();
      }

   }

   private void tMotifActionPerformed(ActionEvent var1) {
      if (this.tPeriode.getItemCount() > 0 && this.tMotif.getItemCount() > 0) {
         this.afficherListeColonnes();
      }

   }

   private void tEtatActionPerformed(ActionEvent var1) {
      if (this.tEtat.getSelectedItem().equals("Etat de paie")) {
         this.panelEP.setVisible(true);
         if (this.tPeriode.getItemCount() > 0 && this.tMotif.getItemCount() > 0) {
            if (this.tLangue.getSelectedItem().toString().equals("Fran\u00e7ais")) {
               this.afficherListeColonnes();
            } else {
               this.afficherListeColonnesAr();
            }
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
      Thread t = new 16(this);
      t.start();
   }

   private void listeColonnesTableMouseClicked(MouseEvent var1) {
   }

   private void tLangueActionPerformed(ActionEvent var1) {
      if (this.tPeriode.getItemCount() > 0 && this.tMotif.getItemCount() > 0) {
         this.afficherListeColonnes();
      }

   }
}
