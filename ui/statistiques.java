package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Rubrique;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.CustomTableRenderer;
import com.mccmr.util.ModelClass;
import com.mccmr.util.WriteExcel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class statistiques extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   private JButton btnExit;
   private JButton btnExport;
   private JButton btnShow;
   private JCheckBox cCheckAll;
   private JCheckBox cPrinterDialog;
   private JCheckBox cPrinterDialog1;
   private JLabel jLabel20;
   private JLabel jLabel21;
   private JLabel jLabel22;
   private JLabel jLabel23;
   private JLabel jLabel32;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JScrollPane jScrollPane9;
   private JSeparator jSeparator17;
   private JTable listTable;
   private JLabel msgLabel;
   private JPanel pnlBody;
   private JPanel pnlFooter;
   private JPanel pnlMotifs;
   private JPanel pnlMotifs_Edit;
   private JPanel pnlMotifs_List;
   private JProgressBar progressBar;
   private JFormattedTextField tMontantTotal;
   private JComboBox<Object> tMotif;
   private JLabel tNbElements;
   private JComboBox<Object> tPeriodeAu;
   private JComboBox<Object> tPeriodeDu;
   private JComboBox<Object> tRubrique;

   public statistiques() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExport.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnShow.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.GET_APP, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.menu.remplirCombo("Motif", this.tMotif);
      this.menu.remplirCombo("Rubrique", this.tRubrique);
      this.menu.remplirCombo("Periode", this.tPeriodeDu);
      this.menu.remplirCombo("Periode", this.tPeriodeAu);
   }

   private void afficherListe() {
      Rubrique rubrique = (Rubrique)this.tRubrique.getSelectedItem();
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmEmployeStat(var10003));
      this.listTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      this.listTable.getColumnModel().getColumn(2).setPreferredWidth(300);
      if (motif != null && this.tPeriodeDu.getSelectedItem() != null && this.tPeriodeAu.getSelectedItem() != null) {
         int valuePB = 0;
         int maxPB = this.menu.employeFrame.dataListInit.size();
         this.progressBar.setMaximum(maxPB);
         this.progressBar.setValue(valuePB);
         SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
         double totGen = (double)0.0F;

         for(Employe rs : this.menu.employeFrame.dataListInit) {
            String dteSortie = rs.getDateDebauche() != null ? df2.format(rs.getDateDebauche()) : "-";
            double mnt = this.menu.pc.montantCumuleRubriquePaieById(rs, rubrique, motif, (Date)this.tPeriodeDu.getSelectedItem(), (Date)this.tPeriodeAu.getSelectedItem());
            if (mnt != (double)0.0F) {
               ((ModelClass.tmEmployeStat)this.listTable.getModel()).addRow(rs.getId(), rs.getPrenom() + " " + rs.getNom(), df2.format(rs.getDateEmbauche()), dteSortie, mnt);
               totGen += mnt;
            }

            ++valuePB;
            this.progressBar.setValue(valuePB);
         }

         this.tMontantTotal.setValue(totGen);
         this.listTable.getColumnModel().getColumn(0).setPreferredWidth(5);
         this.listTable.getColumnModel().getColumn(1).setPreferredWidth(10);
         this.listTable.getColumnModel().getColumn(2).setPreferredWidth(500);
         this.listTable.getColumnModel().getColumn(3).setPreferredWidth(50);
         this.listTable.getColumnModel().getColumn(4).setPreferredWidth(50);
         this.listTable.getColumnModel().getColumn(5).setPreferredWidth(100);
         this.listTable.setRowHeight(30);
         this.listTable.setAutoCreateRowSorter(true);
         TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable.getModel());
         this.listTable.setRowSorter(sorter);
         sorter.setSortsOnUpdates(true);
         this.menu.mc.getClass();
         ModelClass var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(1, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(5, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(3, new ModelClass.DateComparator(var10004));
      }

   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.pnlMotifs = new JPanel();
      this.pnlMotifs_Edit = new JPanel();
      this.tRubrique = new JComboBox();
      this.jLabel20 = new JLabel();
      this.tMotif = new JComboBox();
      this.jLabel23 = new JLabel();
      this.cCheckAll = new JCheckBox();
      this.btnShow = new JButton();
      this.tNbElements = new JLabel();
      this.btnExport = new JButton();
      this.cPrinterDialog = new JCheckBox();
      this.jLabel21 = new JLabel();
      this.tPeriodeDu = new JComboBox();
      this.cPrinterDialog1 = new JCheckBox();
      this.jLabel32 = new JLabel();
      this.jSeparator17 = new JSeparator();
      this.tMontantTotal = new JFormattedTextField();
      this.jLabel22 = new JLabel();
      this.tPeriodeAu = new JComboBox();
      this.pnlMotifs_List = new JPanel();
      this.jScrollPane9 = new JScrollPane();
      this.listTable = new JTable();
      this.pnlFooter = new JPanel();
      this.msgLabel = new JLabel();
      this.progressBar = new JProgressBar();
      this.setBackground(new Color(255, 255, 255));
      this.setBorder((Border)null);
      this.setAutoscrolls(true);
      this.setFont(new Font("Segoe UI Light", 0, 12));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(1000, 600));
      this.jPanel1.setBackground(new Color(255, 255, 255));
      this.jLabel7.setFont(new Font("Segoe UI Light", 0, 24));
      this.jLabel7.setForeground(new Color(0, 102, 153));
      this.jLabel7.setText("Statistiques");
      this.jLabel7.setToolTipText("");
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addGap(572, 572, 572).addComponent(this.btnExit, -2, 39, -2).addGap(19, 19, 19)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.pnlMotifs.setBackground(new Color(255, 255, 255));
      this.pnlMotifs_Edit.setBackground(new Color(255, 255, 255));
      this.tRubrique.setFont(new Font("Segoe UI Light", 0, 12));
      this.tRubrique.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tRubrique.addActionListener(new 2(this));
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("Rubrique");
      this.tMotif.setFont(new Font("Segoe UI Light", 0, 12));
      this.tMotif.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMotif.addActionListener(new 3(this));
      this.jLabel23.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel23.setForeground(new Color(0, 102, 153));
      this.jLabel23.setText("Motif");
      this.cCheckAll.setBackground(new Color(255, 255, 255));
      this.cCheckAll.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCheckAll.setForeground(new Color(0, 102, 153));
      this.cCheckAll.setText("Tout cocher");
      this.cCheckAll.addActionListener(new 4(this));
      this.btnShow.setBackground(new Color(255, 255, 255));
      this.btnShow.setToolTipText("Afficher");
      this.btnShow.setContentAreaFilled(false);
      this.btnShow.setCursor(new Cursor(12));
      this.btnShow.setOpaque(true);
      this.btnShow.addActionListener(new 5(this));
      this.tNbElements.setFont(new Font("Segoe UI Light", 1, 10));
      this.tNbElements.setForeground(new Color(0, 102, 153));
      this.tNbElements.setText("...");
      this.btnExport.setBackground(new Color(255, 255, 255));
      this.btnExport.setToolTipText("Exporter");
      this.btnExport.setContentAreaFilled(false);
      this.btnExport.setCursor(new Cursor(12));
      this.btnExport.setOpaque(true);
      this.btnExport.addActionListener(new 6(this));
      this.cPrinterDialog.setBackground(new Color(255, 255, 255));
      this.cPrinterDialog.setFont(new Font("Segoe UI Light", 0, 12));
      this.cPrinterDialog.setForeground(new Color(0, 102, 153));
      this.cPrinterDialog.setSelected(true);
      this.cPrinterDialog.setText("Personnel actif");
      this.cPrinterDialog.addActionListener(new 7(this));
      this.jLabel21.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel21.setForeground(new Color(0, 102, 153));
      this.jLabel21.setText("P\u00e9riode d\u00e9but");
      this.tPeriodeDu.setFont(new Font("Segoe UI Light", 0, 12));
      this.tPeriodeDu.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriodeDu.addActionListener(new 8(this));
      this.cPrinterDialog1.setBackground(new Color(255, 255, 255));
      this.cPrinterDialog1.setFont(new Font("Segoe UI Light", 0, 12));
      this.cPrinterDialog1.setForeground(new Color(0, 102, 153));
      this.cPrinterDialog1.setSelected(true);
      this.cPrinterDialog1.setText("Personnel en cong\u00e9s");
      this.cPrinterDialog1.addActionListener(new 9(this));
      this.jLabel32.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel32.setForeground(new Color(0, 102, 153));
      this.jLabel32.setText("Total");
      this.tMontantTotal.setEditable(false);
      this.tMontantTotal.setBorder((Border)null);
      this.tMontantTotal.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tMontantTotal.setHorizontalAlignment(0);
      this.tMontantTotal.setFont(new Font("Segoe UI Light", 1, 14));
      this.tMontantTotal.addCaretListener(new 10(this));
      this.tMontantTotal.addFocusListener(new 11(this));
      this.jLabel22.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel22.setForeground(new Color(0, 102, 153));
      this.jLabel22.setText("P\u00e9riode fin");
      this.tPeriodeAu.setFont(new Font("Segoe UI Light", 0, 12));
      this.tPeriodeAu.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriodeAu.addActionListener(new 12(this));
      GroupLayout pnlMotifs_EditLayout = new GroupLayout(this.pnlMotifs_Edit);
      this.pnlMotifs_Edit.setLayout(pnlMotifs_EditLayout);
      pnlMotifs_EditLayout.setHorizontalGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tRubrique, -2, 250, -2).addComponent(this.jLabel20, -2, 250, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tMotif, -2, 150, -2).addComponent(this.jLabel23, -2, 150, -2)).addGap(18, 18, 18).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriodeDu, 0, -1, 32767).addComponent(this.jLabel21, -2, 144, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriodeAu, 0, -1, 32767).addComponent(this.jLabel22, -2, 144, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cPrinterDialog).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cPrinterDialog1).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnExport, -2, 35, -2).addGap(5, 5, 5)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tNbElements, -1, -1, 32767).addComponent(this.cCheckAll)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel32, Alignment.LEADING, -2, 150, -2).addComponent(this.tMontantTotal, Alignment.LEADING, -2, 150, -2).addComponent(this.jSeparator17, -2, 150, -2)))).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnShow, -2, 35, -2).addGap(34, 34, 34)));
      pnlMotifs_EditLayout.setVerticalGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel21).addGap(0, 0, 0).addComponent(this.tPeriodeDu, -2, 30, -2)).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel23).addGap(0, 0, 0).addComponent(this.tMotif, -2, 30, -2)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel20).addGap(0, 0, 0).addComponent(this.tRubrique, -2, 30, -2)))).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel22).addGap(0, 0, 0).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tPeriodeAu, -2, 30, -2).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.cPrinterDialog, -2, 17, -2).addComponent(this.cPrinterDialog1)).addComponent(this.btnExport, -2, 35, -2).addComponent(this.btnShow, -2, 35, -2)))).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cCheckAll).addGap(0, 0, 0).addComponent(this.tNbElements).addContainerGap(-1, 32767)).addGroup(Alignment.TRAILING, pnlMotifs_EditLayout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel32).addGap(0, 0, 0).addComponent(this.tMontantTotal, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator17, -2, -1, -2).addContainerGap()))));
      this.pnlMotifs_List.setBackground(new Color(255, 255, 255));
      this.listTable.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable.setSelectionBackground(new Color(0, 102, 153));
      this.listTable.setShowGrid(false);
      this.listTable.addMouseListener(new 13(this));
      this.jScrollPane9.setViewportView(this.listTable);
      GroupLayout pnlMotifs_ListLayout = new GroupLayout(this.pnlMotifs_List);
      this.pnlMotifs_List.setLayout(pnlMotifs_ListLayout);
      pnlMotifs_ListLayout.setHorizontalGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane9, -2, 1114, -2).addContainerGap(27, 32767)));
      pnlMotifs_ListLayout.setVerticalGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_ListLayout.createSequentialGroup().addComponent(this.jScrollPane9, -2, 391, -2).addGap(0, 0, 32767)));
      GroupLayout pnlMotifsLayout = new GroupLayout(this.pnlMotifs);
      this.pnlMotifs.setLayout(pnlMotifsLayout);
      pnlMotifsLayout.setHorizontalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlMotifs_List, -2, -1, -2).addGroup(pnlMotifsLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlMotifs_Edit, -2, 1116, -2))).addContainerGap(-1, 32767)));
      pnlMotifsLayout.setVerticalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlMotifs_Edit, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlMotifs_List, -2, -1, -2).addGap(0, 0, 32767)));
      this.pnlFooter.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout pnlFooterLayout = new GroupLayout(this.pnlFooter);
      this.pnlFooter.setLayout(pnlFooterLayout);
      pnlFooterLayout.setHorizontalGroup(pnlFooterLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlFooterLayout.createSequentialGroup().addComponent(this.msgLabel, -1, 1132, 32767).addContainerGap()));
      pnlFooterLayout.setVerticalGroup(pnlFooterLayout.createParallelGroup(Alignment.LEADING).addComponent(this.msgLabel, -1, 21, 32767));
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.pnlFooter, -2, -1, -2).addGap(53, 53, 53)).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlMotifs, -2, 1125, -2).addComponent(this.progressBar, -2, 1133, -2)).addContainerGap(-1, 32767)));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.progressBar, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlMotifs, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlFooter, -2, -1, -2).addContainerGap()));
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlBody, -2, 1148, -2).addComponent(this.jPanel1, -2, -1, -2)).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -2, -1, -2).addGap(0, 0, 0)));
      this.setSize(new Dimension(1133, 629));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void listTableMouseClicked(MouseEvent var1) {
      JLabel var10000 = this.tNbElements;
      int var10001 = this.selectedLinesSize(this.listTable);
      var10000.setText(var10001 + "/" + this.listTable.getRowCount());
   }

   private void btnShowActionPerformed(ActionEvent var1) {
      Thread t = new 14(this);
      t.start();
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

   private void cCheckAllActionPerformed(ActionEvent var1) {
      for(int i = 0; i < this.listTable.getRowCount(); ++i) {
         ((ModelClass.tmEmployeStat)this.listTable.getModel()).setValueAt(this.cCheckAll.isSelected(), i, 0);
         JLabel var10000 = this.tNbElements;
         int var10001 = this.selectedLinesSize(this.listTable);
         var10000.setText(var10001 + "/" + this.listTable.getRowCount());
      }

   }

   private void tRubriqueActionPerformed(ActionEvent var1) {
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmBulletinPaie(var10003));
   }

   private void tMotifActionPerformed(ActionEvent var1) {
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmBulletinPaie(var10003));
   }

   private void btnExportActionPerformed(ActionEvent var1) {
      Thread t = new 15(this);
      t.start();
   }

   private void toExcel() throws IOException, WriteException {
      String periodeDu = (new SimpleDateFormat("MMMM-yyyy")).format((Date)this.tPeriodeDu.getSelectedItem()).toUpperCase();
      String periodeAu = (new SimpleDateFormat("MMMM-yyyy")).format((Date)this.tPeriodeAu.getSelectedItem()).toUpperCase();
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      String motifTitre = motif.getNom().toUpperCase();
      String fileName = "repport/STAT_" + motifTitre + "_Du" + periodeDu + "Au" + periodeAu + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Etat paie");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, "STATISTIQUES ELEMENT DE SALAIRE DU " + periodeDu + " AU " + periodeAu + " / " + motifTitre);
      WriteExcel var10000 = this.menu.we;
      SimpleDateFormat var10004 = new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss");
      Date var10005 = new Date();
      var10000.addLabelBold(excelSheet, 0, 1, "Edit\u00e9 le : " + var10004.format(var10005));
      this.menu.we.addLabelTitre(excelSheet, 0, 3, "ELEMENT");
      this.menu.we.addLabelTitre(excelSheet, 1, 3, ((Rubrique)this.tRubrique.getSelectedItem()).getLibelle());
      int row = 5;
      int col = 0;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, row, "ID");
      ++col;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col, row, "NOM ET PRENOM");
      ++col;
      this.menu.we.addLabelBoldBorder(excelSheet, col, row, "EMBAUCHE");
      ++col;
      this.menu.we.addLabelBoldBorder(excelSheet, col, row, "SORTIE");
      ++col;
      this.menu.we.addLabelBoldBorder(excelSheet, col, row, "MONTANT");
      this.menu.we.setColumnWidth(excelSheet, 0, 10);
      this.menu.we.setColumnWidth(excelSheet, 1, 30);
      this.menu.we.setColumnWidth(excelSheet, 2, 10);
      this.menu.we.setColumnWidth(excelSheet, 3, 10);
      this.menu.we.setColumnWidth(excelSheet, 4, 15);
      ++row;

      try {
         int valuePB = 0;
         int maxPB = this.listTable.getRowCount();
         this.progressBar.setMaximum(maxPB);
         this.progressBar.setValue(valuePB);

         for(int i = 0; i < this.listTable.getRowCount(); ++i) {
            if ((Boolean)((ModelClass.tmEmployeStat)this.listTable.getModel()).getValueAt(i, 0)) {
               this.menu.we.addNumberBorder(excelSheet, 0, row, ((Number)((ModelClass.tmEmployeStat)this.listTable.getModel()).getValueAt(i, 1)).doubleValue());
               this.menu.we.addLabelBorder(excelSheet, 1, row, ((ModelClass.tmEmployeStat)this.listTable.getModel()).getValueAt(i, 2).toString());
               this.menu.we.addLabelBorder(excelSheet, 2, row, ((ModelClass.tmEmployeStat)this.listTable.getModel()).getValueAt(i, 3).toString());
               this.menu.we.addLabelBorder(excelSheet, 3, row, ((ModelClass.tmEmployeStat)this.listTable.getModel()).getValueAt(i, 4).toString());
               this.menu.we.addNumberBorder(excelSheet, 4, row, (Double)((ModelClass.tmEmployeStat)this.listTable.getModel()).getValueAt(i, 5));
               ++row;
               ++valuePB;
               this.progressBar.setValue(valuePB);
            }
         }
      } catch (RowsExceededException e) {
         e.printStackTrace();
         this.menu.viewMessage(this.msgLabel, "Nombre de ligne non autorise!", true);
      }

      this.menu.we.addNumberBoldBorderSilver(excelSheet, 4, row, this.menu.we.columnSum(excelSheet, 4, row, 6, row - 1));
      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void cPrinterDialogActionPerformed(ActionEvent var1) {
   }

   private void tPeriodeDuActionPerformed(ActionEvent var1) {
   }

   private void cPrinterDialog1ActionPerformed(ActionEvent var1) {
   }

   private void tMontantTotalCaretUpdate(CaretEvent var1) {
   }

   private void tMontantTotalFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tMontantTotal);
   }

   private void tPeriodeAuActionPerformed(ActionEvent var1) {
   }
}
