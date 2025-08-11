package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.entity.Paie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.WriteExcel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class declarationsAnnuelles extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   List<Employe> dlEmploye;
   private JButton btnExit;
   private JButton btnExport;
   private JButton btnRefresh;
   private JLabel jLabel20;
   private JLabel jLabel25;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JLabel msgLabel;
   private JPanel pnlBody;
   private JProgressBar progressBar;
   private JComboBox<Object> tEtat;
   private JComboBox<Object> tPeriode;

   public declarationsAnnuelles() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnRefresh.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.REFRESH, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExport.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      Query q = this.menu.entityManager.createQuery("Select p from Paie p");
      q.setMaxResults(1000000);
      this.dlPaie = q.getResultList();
      q = this.menu.entityManager.createQuery("Select p from Employe p");
      q.setMaxResults(1000000);
      this.dlEmploye = q.getResultList();
   }

   private void declarationAnuelleITS() throws IOException, WriteException {
      String yearNumber = this.tPeriode.getSelectedItem().toString();
      String fileName = "repport/DEC_AN_ITS_" + yearNumber + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "DEC. ANNUELLE ITS");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, "REBUPLIQUE ISLAMIQUE DE LA MAURITANIE");
      this.menu.we.addLabelTitre(excelSheet, 0, 1, "               MINISTERE DE FINANCES");
      this.menu.we.addLabelTitre(excelSheet, 0, 2, "     DIRECTION GENERALE DES IMPOTS");
      this.menu.we.addLabelTitre(excelSheet, 3, 4, "               DECLARATION ANNUELLE");
      this.menu.we.addLabelTitre(excelSheet, 3, 5, "        DES SALARIES DE L'EXERCICE : " + yearNumber);
      this.menu.we.addLabelTitre(excelSheet, 8, 0, "GPC : \u2026\u2026.");
      this.menu.we.addLabelTitre(excelSheet, 9, 0, "PCS : \u2026\u2026.");
      this.menu.we.addLabelTitre(excelSheet, 2, 7, "    I M P O T S   S U R   L E S   T R A I T E M E N T S   E T   S A L A I R E S");
      this.menu.we.addLabelTitre(excelSheet, 0, 9, "IDENTIFICATION DU CONTRIBUABLE");
      this.menu.we.addLabelTitre(excelSheet, 6, 9, "N\u00b0 CPTE CONTRIBUABLE (NIF) : " + this.menu.paramsGen.getNoIts());
      this.menu.we.addLabelTitre(excelSheet, 0, 11, "1  Nom ou Raison Sociale : " + this.menu.paramsGen.getNomEntreprise().toUpperCase());
      this.menu.we.addLabelTitre(excelSheet, 0, 12, "2  Activit\u00e9 Principale : " + this.menu.paramsGen.getActiviteEntreprise());
      this.menu.we.addLabelTitre(excelSheet, 0, 13, "3  Adresse G\u00e9ographique : " + this.menu.paramsGen.getAdresse());
      WriteExcel var10000 = this.menu.we;
      String var10004 = this.menu.paramsGen.getBd();
      var10000.addLabelTitre(excelSheet, 0, 14, "4  Adresse Postale : BP : " + var10004 + "   TEL : " + this.menu.paramsGen.getTelephone() + "  FAX : " + this.menu.paramsGen.getFax());
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 0, 17, "Nom et Pr\u00e9nom");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 1, 17, "Fonction");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 2, 17, "R\u00e9mun\u00e9ration brute annuelle totale");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 3, 17, "Cotisations");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, 17, "R\u00e9mun\u00e9ration brute hors cotisatio");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 17, "Avantage en nature imposable");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, 17, "Sommes affranchies de l'impots");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, 17, "Abattement de 6000");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 8, 17, "R\u00e9mun\u00e9ration imposable");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 9, 17, "ITS Retenue");
      this.menu.we.setColumnWidth(excelSheet, 0, 30);
      this.menu.we.setColumnWidth(excelSheet, 0, 20);
      int row = 18;
      this.progressBar.setMaximum(this.dlEmploye.size());
      int valuePB = 0;
      this.progressBar.setValue(valuePB);

      for(Employe employe : this.dlEmploye) {
         String poste_employe = "";
         if (employe.getPoste() != null) {
            poste_employe = employe.getPoste().getNom();
         }

         List<Paie> dl = (List)this.dlPaie.stream().filter((var2x) -> var2x.getEmploye().getId() == employe.getId() && this.menu.periodeExo.format(var2x.getPeriode()).equalsIgnoreCase(this.tPeriode.getSelectedItem().toString()) && var2x.getMotif().isDeclarationSoumisIts() && var2x.getBt() > (double)0.0F).collect(Collectors.toList());
         int nbPeriodes = ((List)dl.stream().filter((var0) -> var0.getMotif().getId() == 1).collect(Collectors.toList())).size();
         if (nbPeriodes > 0) {
            double ri = dl.stream().mapToDouble((var0) -> var0.getBt()).sum() - dl.stream().mapToDouble((var0) -> var0.getBni()).sum() - (double)(nbPeriodes * 6000);
            if (ri < (double)0.0F) {
               ri = (double)0.0F;
            }

            this.menu.we.addLabelBorder(excelSheet, 0, row, employe.getNom());
            this.menu.we.addLabelBorder(excelSheet, 1, row, poste_employe);
            this.menu.we.addNumberBorder(excelSheet, 2, row, dl.stream().mapToDouble((var0) -> var0.getBt()).sum());
            this.menu.we.addNumberBorder(excelSheet, 3, row, dl.stream().mapToDouble((var0) -> var0.getCnss()).sum() + dl.stream().mapToDouble((var0) -> var0.getCnam()).sum());
            this.menu.we.addNumberBorder(excelSheet, 4, row, dl.stream().mapToDouble((var0) -> var0.getBt()).sum() - dl.stream().mapToDouble((var0) -> var0.getCnss()).sum() - dl.stream().mapToDouble((var0) -> var0.getCnam()).sum());
            this.menu.we.addNumberBorder(excelSheet, 5, row, dl.stream().mapToDouble((var0) -> var0.getBiAvnat()).sum());
            this.menu.we.addNumberBorder(excelSheet, 6, row, dl.stream().mapToDouble((var0) -> var0.getBni()).sum());
            this.menu.we.addNumberBorder(excelSheet, 7, row, (double)nbPeriodes * (double)6000.0F);
            this.menu.we.addNumberBorder(excelSheet, 8, row, ri);
            this.menu.we.addNumberBorder(excelSheet, 9, row, dl.stream().mapToDouble((var0) -> var0.getIts()).sum());
            ++row;
         }

         ++valuePB;
         this.progressBar.setValue(valuePB);
      }

      this.menu.we.addNumberBoldBorderSilver(excelSheet, 2, row, this.menu.we.columnSum(excelSheet, 2, row, 18, row - 1));
      this.menu.we.addNumberBoldBorderSilver(excelSheet, 3, row, this.menu.we.columnSum(excelSheet, 3, row, 18, row - 1));
      this.menu.we.addNumberBoldBorderSilver(excelSheet, 4, row, this.menu.we.columnSum(excelSheet, 4, row, 18, row - 1));
      this.menu.we.addNumberBoldBorderSilver(excelSheet, 5, row, this.menu.we.columnSum(excelSheet, 5, row, 18, row - 1));
      this.menu.we.addNumberBoldBorderSilver(excelSheet, 6, row, this.menu.we.columnSum(excelSheet, 6, row, 18, row - 1));
      this.menu.we.addNumberBoldBorderSilver(excelSheet, 7, row, this.menu.we.columnSum(excelSheet, 7, row, 18, row - 1));
      this.menu.we.addNumberBoldBorderSilver(excelSheet, 8, row, this.menu.we.columnSum(excelSheet, 8, row, 18, row - 1));
      this.menu.we.addNumberBoldBorderSilver(excelSheet, 9, row, this.menu.we.columnSum(excelSheet, 9, row, 18, row - 1));
      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
      this.progressBar.setValue(99);
   }

   private void decTA() {
      List<Paie> dl = this.dlPaie;
      dl = (List)dl.stream().filter((var1x) -> this.menu.periodeExo.format(var1x.getPeriode()).equalsIgnoreCase(this.tPeriode.getSelectedItem().toString()) && var1x.getMotif().isDeclarationSoumisIts()).collect(Collectors.toList());
      double bt = dl.stream().mapToDouble((var0) -> var0.getBt()).sum();
      double bni = dl.stream().mapToDouble((var0) -> var0.getBni()).sum();
      double avnat = dl.stream().mapToDouble((var0) -> var0.getBiAvnat()).sum();
      Map<Object, Object> param = new HashMap();
      param.put("remunerationGlobale", bt);
      param.put("remunerationNonImposable", bni);
      param.put("avantagesEnNature", avnat);
      param.put("remunerationImposable", bt - bni);
      param.put("taxeApp", (bt - bni) * 0.006);
      param.put("periodeTaxe", this.tPeriode.getSelectedItem().toString());

      try {
         this.menu.gl.afficherReportParamOnly("dTA", param);
      } catch (Exception e) {
         e.printStackTrace();
         this.menu.viewMessage(this.msgLabel, "Error!", true);
      }

   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.tPeriode = new JComboBox();
      this.jLabel20 = new JLabel();
      this.progressBar = new JProgressBar();
      this.btnExport = new JButton();
      this.jLabel25 = new JLabel();
      this.tEtat = new JComboBox();
      this.btnRefresh = new JButton();
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
      this.jLabel7.setText("D\u00e9clarations annuelles");
      this.jLabel7.setToolTipText("");
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
      this.tPeriode.setModel(new DefaultComboBoxModel(new String[]{"2020", "2021", "2022", "2023", "2024"}));
      this.tPeriode.addActionListener(new 2(this));
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("Exercice");
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      this.btnExport.setToolTipText("Exporter la balance vers Excel");
      this.btnExport.setContentAreaFilled(false);
      this.btnExport.setCursor(new Cursor(12));
      this.btnExport.setOpaque(true);
      this.btnExport.addActionListener(new 3(this));
      this.jLabel25.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel25.setForeground(new Color(0, 102, 153));
      this.jLabel25.setText("Document");
      this.tEtat.setFont(new Font("Segoe UI Light", 0, 12));
      this.tEtat.setModel(new DefaultComboBoxModel(new String[]{"Taxe d'apprentissage exercice courant", "D\u00e9claration annuelle de l'ITS"}));
      this.tEtat.addActionListener(new 4(this));
      this.btnRefresh.setContentAreaFilled(false);
      this.btnRefresh.setCursor(new Cursor(12));
      this.btnRefresh.setOpaque(true);
      this.btnRefresh.addActionListener(new 5(this));
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.progressBar, -2, 728, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addContainerGap().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriode, 0, -1, 32767).addComponent(this.jLabel20, -2, 144, -2)).addComponent(this.jLabel25, -2, 250, -2).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.btnRefresh, -2, 35, -2).addGap(640, 640, 640).addComponent(this.btnExport, -2, 35, -2)).addComponent(this.tEtat, -2, 439, -2)).addGap(0, 0, 32767))).addContainerGap()));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(0, 0, 0).addComponent(this.progressBar, -2, -1, -2).addGap(18, 18, 18).addComponent(this.jLabel20).addGap(0, 0, 0).addComponent(this.tPeriode, -2, 30, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel25).addGap(0, 0, 0).addComponent(this.tEtat, -2, 30, -2).addGap(101, 101, 101).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.btnExport, -1, -1, 32767).addComponent(this.btnRefresh, Alignment.TRAILING, -2, 35, -2)).addGap(3, 3, 3)));
      this.msgLabel.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jPanel1, -1, -1, 32767).addComponent(this.msgLabel, -1, -1, 32767).addComponent(this.pnlBody, -2, 725, 32767)).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -2, -1, -2).addGap(0, 0, 0).addComponent(this.msgLabel, -2, 22, -2)));
      this.setSize(new Dimension(727, 374));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void btnExportActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      Thread t = new 6(this);
      t.start();
   }

   private void tPeriodeActionPerformed(ActionEvent var1) {
   }

   private void tEtatActionPerformed(ActionEvent var1) {
   }

   private void btnRefreshActionPerformed(ActionEvent var1) {
      Thread t = new 7(this);
      t.start();
   }
}
