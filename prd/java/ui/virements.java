package com.mccmr.ui;

import com.mccmr.entity.Banque;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.WriteExcel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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

public class virements extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   List<Rubriquepaie> dlRubriquepaie;
   int nbElements;
   private JProgressBar ProgressBar;
   private JButton btnExit;
   private JButton btnExport;
   private JLabel jLabel20;
   private JLabel jLabel23;
   private JLabel jLabel24;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JLabel msgLabel;
   private JPanel pnlBody;
   private JComboBox<Object> tBanque;
   private JComboBox<Object> tMotif;
   private JComboBox<Object> tPeriode;

   public virements() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExport.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.menu.remplirCombo("Periode", this.tPeriode);
      this.menu.remplirCombo("Banque", this.tBanque);
      this.menu.remplirCombo("Motif", this.tMotif);
      Query q = this.menu.entityManager.createQuery("Select p from Paie p");
      q.setMaxResults(1000000);
      this.dlPaie = q.getResultList();
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.tPeriode = new JComboBox();
      this.jLabel23 = new JLabel();
      this.jLabel20 = new JLabel();
      this.tBanque = new JComboBox();
      this.ProgressBar = new JProgressBar();
      this.btnExport = new JButton();
      this.jLabel24 = new JLabel();
      this.tMotif = new JComboBox();
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
      this.jLabel7.setText("Virements bancaires");
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
      this.tPeriode.setFont(new Font("Segoe UI Light", 0, 11));
      this.tPeriode.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriode.addActionListener(new 2(this));
      this.jLabel23.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel23.setForeground(new Color(0, 102, 153));
      this.jLabel23.setText("Banque");
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("P\u00e9riode");
      this.tBanque.setFont(new Font("Segoe UI Light", 0, 11));
      this.tBanque.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tBanque.addActionListener(new 3(this));
      this.ProgressBar.setBackground(new Color(204, 204, 204));
      this.ProgressBar.setForeground(new Color(0, 102, 153));
      this.ProgressBar.setBorderPainted(false);
      this.ProgressBar.setOpaque(true);
      this.ProgressBar.setStringPainted(true);
      this.btnExport.setBackground(new Color(255, 255, 255));
      this.btnExport.setToolTipText("Exporter la balance vers Excel");
      this.btnExport.setContentAreaFilled(false);
      this.btnExport.setCursor(new Cursor(12));
      this.btnExport.setOpaque(true);
      this.btnExport.addActionListener(new 4(this));
      this.jLabel24.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel24.setForeground(new Color(0, 102, 153));
      this.jLabel24.setText("Motif");
      this.tMotif.setFont(new Font("Segoe UI Light", 0, 11));
      this.tMotif.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMotif.addActionListener(new 5(this));
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.ProgressBar, -2, 728, -2).addContainerGap()).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnExport, -2, 35, -2).addGap(28, 28, 28)).addGroup(pnlBodyLayout.createSequentialGroup().addGap(19, 19, 19).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tBanque, 0, -1, 32767).addComponent(this.jLabel23, -2, 300, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriode, 0, -1, 32767).addComponent(this.jLabel20, -2, 144, -2)).addGap(18, 18, 18).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tMotif, 0, -1, 32767).addComponent(this.jLabel24, -2, 167, -2)))).addContainerGap(-1, 32767)));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addContainerGap().addComponent(this.ProgressBar, -2, -1, -2).addGap(18, 18, 18).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel20).addGap(0, 0, 0).addComponent(this.tPeriode, -2, 30, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel24).addGap(0, 0, 0).addComponent(this.tMotif, -2, 30, -2))).addGap(18, 18, 18).addComponent(this.jLabel23).addGap(0, 0, 0).addComponent(this.tBanque, -2, 30, -2).addPreferredGap(ComponentPlacement.RELATED, 70, 32767).addComponent(this.btnExport, -2, 35, -2).addContainerGap()));
      this.msgLabel.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jPanel1, -1, -1, 32767).addComponent(this.msgLabel, -1, -1, 32767).addComponent(this.pnlBody, -2, 725, 32767)).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -2, -1, -2).addGap(0, 0, 0).addComponent(this.msgLabel, -2, 22, -2)));
      this.setSize(new Dimension(727, 363));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void excelVirements(Date var1, int var2) throws IOException, WriteException {
      String periodeShortTitre = (new SimpleDateFormat("MM-yyyy")).format(periode).toUpperCase();
      Banque banque = this.menu.pc.banqueById(banque_id);
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      String var10000 = banque.getNom().replace("/", "-");
      String fileName = "repport/VB_" + var10000 + "_" + periodeShortTitre + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Virements");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      WriteExcel var21 = this.menu.we;
      String var10004 = (new SimpleDateFormat("MMMM-yyyy")).format(periode).toUpperCase();
      var21.addLabelTitre(excelSheet, 0, 1, "ETAT DE VIREMENTS BANCAIRES : " + var10004 + " (" + String.valueOf(motif) + ")");
      var21 = this.menu.we;
      SimpleDateFormat var25 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var21.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var25.format(var10005));
      this.menu.we.addLabelTitre(excelSheet, 0, 4, "Banque / Agence : " + banque.getNom());
      this.menu.we.addLabelBoldBorderGold(excelSheet, 0, 6, "ID EMPL.");
      this.menu.we.addLabelBoldBorderGold(excelSheet, 1, 6, "NOM ET PRENOM");
      this.menu.we.setColumnWidth(excelSheet, 1, 40);
      this.menu.we.addLabelBoldBorderGold(excelSheet, 2, 6, "NNI");
      this.menu.we.setColumnWidth(excelSheet, 2, 15);
      this.menu.we.addLabelBoldBorderGold(excelSheet, 3, 6, "N\u00b0COMPTE");
      this.menu.we.setColumnWidth(excelSheet, 3, 15);
      this.menu.we.addLabelBoldBorderGold(excelSheet, 4, 6, "MONTANT");
      int row = 7;
      List<Paie> dl = this.dlPaie;
      dl = (List)dl.stream().filter((var4x) -> this.menu.df.format(var4x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode)) && var4x.getBanque().equalsIgnoreCase(banque.getNom()) && var4x.getMotif().getId() == motif.getId() && var4x.getModePaiement().equalsIgnoreCase("Virement")).sorted(Comparator.comparing((var0) -> var0.getEmploye().getId())).collect(Collectors.toList());
      int maxPB = dl.size() * 5;
      this.ProgressBar.setMaximum(maxPB);
      int valuePB = 0;
      this.ProgressBar.setValue(valuePB);

      for(Paie rs : dl) {
         this.menu.we.addNumberBorder(excelSheet, 0, row, Integer.valueOf(rs.getEmploye().getId()).doubleValue());
         ++valuePB;
         this.ProgressBar.setValue(valuePB);
         var21 = this.menu.we;
         String var26 = rs.getEmploye().getPrenom();
         var21.addLabelBorder(excelSheet, 1, row, var26 + " " + rs.getEmploye().getNom());
         ++valuePB;
         this.ProgressBar.setValue(valuePB);
         this.menu.we.addLabelBorder(excelSheet, 2, row, rs.getEmploye().getNni());
         ++valuePB;
         this.ProgressBar.setValue(valuePB);
         this.menu.we.addLabelBorder(excelSheet, 3, row, rs.getNoCompteBanque());
         ++valuePB;
         this.ProgressBar.setValue(valuePB);
         this.menu.we.addNumberBorder(excelSheet, 4, row, dl.stream().filter((var1x) -> var1x.getEmploye().getId() == rs.getEmploye().getId()).mapToDouble((var0) -> var0.getNet()).sum());
         ++valuePB;
         this.ProgressBar.setValue(valuePB);
         ++row;
      }

      double totalNet = this.menu.we.columnSum(excelSheet, 4, row, 7, row - 1);
      this.menu.we.addNumberBoldBorderSilver(excelSheet, 4, row, totalNet);
      var21 = this.menu.we;
      int var10003 = row + 2;
      String var27 = this.menu.nl.convertirEnMRO((double)((long)totalNet), "FR");
      var21.addLabelBold(excelSheet, 0, var10003, "Arr\u00eat\u00e9 \u00e0 la somme de :" + var27);
      this.menu.we.addLabelBold(excelSheet, 0, row + 4, this.menu.paramsGen.getSignataires());
      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void btnExportActionPerformed(ActionEvent var1) {
      Thread t7 = new 6(this);
      t7.start();
   }

   private void tPeriodeActionPerformed(ActionEvent var1) {
   }

   private void tBanqueActionPerformed(ActionEvent var1) {
   }

   private void tMotifActionPerformed(ActionEvent var1) {
   }
}
