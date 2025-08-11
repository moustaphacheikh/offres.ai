package com.mccmr.ui;

import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.FichePaieDetail;
import com.mccmr.util.ModelClass;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class fileImport extends JInternalFrame {
   List<FichePaieDetail> bulletinPaieDetail;
   public menu menu;
   List<Paie> dlPaie;
   int nbElements;
   private JButton btnExit;
   private JButton btnFileSelectData;
   private JButton btnFileSelectRub;
   private JButton btnImportData;
   private JButton btnImportRub;
   private JCheckBox cFixe;
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
   private JLabel jLabel36;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JScrollPane jScrollPane9;
   private JSeparator jSeparator12;
   private JSeparator jSeparator13;
   private JSeparator jSeparator14;
   private JSeparator jSeparator15;
   private JSeparator jSeparator16;
   private JSeparator jSeparator17;
   private JSeparator jSeparator18;
   private JSeparator jSeparator19;
   private JTabbedPane jTabbedPane1;
   private JTable listTable;
   private JLabel msgLabel;
   private JPanel pnlBody;
   private JPanel pnlFooter;
   private JPanel pnlMotifs;
   private JPanel pnlMotifs1;
   private JPanel pnlMotifs_Edit;
   private JPanel pnlMotifs_Edit1;
   private JPanel pnlMotifs_List;
   private JPanel pnlMotifs_List1;
   private JProgressBar progressBar;
   private JFormattedTextField tColNum_BASE;
   private JFormattedTextField tColNum_DATA;
   private JFormattedTextField tColNum_IDRUB;
   private JFormattedTextField tColNum_IDSAL;
   private JFormattedTextField tColNum_IDSAL1;
   private JFormattedTextField tColNum_NBRE;
   private JComboBox<Object> tDataTerget;
   private JLabel tFile_DonneesPersonnel;
   private JLabel tFile_RubriquePaie;
   private JFormattedTextField tLineNum_DataBegin;
   private JFormattedTextField tLineNum_DataBegin1;
   private JFormattedTextField tMontantTotal;
   private JComboBox<Object> tMotif;

   public fileImport() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnFileSelectData.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FIND_IN_PAGE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnFileSelectRub.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FIND_IN_PAGE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnImportData.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOUD_DOWNLOAD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnImportRub.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOUD_DOWNLOAD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.menu.remplirCombo("Motif", this.tMotif);
   }

   private void afficherListe() {
      Date periode = this.menu.paramsGen.getPeriodeCourante();
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      this.listTable.removeAll();
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmVariableImport(var10003));
      this.listTable.getColumnModel().getColumn(2).setPreferredWidth(300);
      double totGen = (double)0.0F;
      Query q = this.menu.entityManager.createQuery("Select p from Rubriquepaie p");
      q.setMaxResults(1000000);
      Set<Rubriquepaie> dl = new HashSet(q.getResultList());

      for(Rubriquepaie rs : (Set)dl.stream().filter((var3x) -> var3x.isImporte() && this.menu.df.format(var3x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode)) && var3x.getMotif().getId() == motif.getId()).sorted(Comparator.comparing((var0) -> (long)var0.getEmploye().getId())).collect(Collectors.toSet())) {
         if (rs.getEmploye().getId() > 0) {
            ((ModelClass.tmVariableImport)this.listTable.getModel()).addRow(rs);
            totGen += rs.getMontant();
         }
      }

      this.tMontantTotal.setValue(totGen);
      this.listTable.getColumnModel().getColumn(0).setPreferredWidth(10);
      this.listTable.getColumnModel().getColumn(1).setPreferredWidth(200);
      this.listTable.getColumnModel().getColumn(2).setPreferredWidth(10);
      this.listTable.getColumnModel().getColumn(3).setPreferredWidth(200);
      this.listTable.getColumnModel().getColumn(4).setPreferredWidth(10);
      this.listTable.getColumnModel().getColumn(5).setPreferredWidth(10);
      this.listTable.getColumnModel().getColumn(6).setPreferredWidth(50);
      this.listTable.getColumnModel().getColumn(7).setPreferredWidth(30);
      this.listTable.getColumnModel().getColumn(8).setPreferredWidth(50);
      this.listTable.setRowHeight(30);
      this.listTable.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable.getModel());
      this.listTable.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.pnlFooter = new JPanel();
      this.msgLabel = new JLabel();
      this.progressBar = new JProgressBar();
      this.jTabbedPane1 = new JTabbedPane();
      this.pnlMotifs = new JPanel();
      this.pnlMotifs_Edit = new JPanel();
      this.tMotif = new JComboBox();
      this.jLabel23 = new JLabel();
      this.cFixe = new JCheckBox();
      this.btnImportRub = new JButton();
      this.btnFileSelectRub = new JButton();
      this.tFile_RubriquePaie = new JLabel();
      this.jLabel25 = new JLabel();
      this.tLineNum_DataBegin = new JFormattedTextField();
      this.jSeparator12 = new JSeparator();
      this.jSeparator13 = new JSeparator();
      this.tColNum_IDSAL = new JFormattedTextField();
      this.jLabel26 = new JLabel();
      this.jSeparator14 = new JSeparator();
      this.tColNum_IDRUB = new JFormattedTextField();
      this.jLabel27 = new JLabel();
      this.jSeparator15 = new JSeparator();
      this.tColNum_BASE = new JFormattedTextField();
      this.jLabel28 = new JLabel();
      this.jSeparator16 = new JSeparator();
      this.tColNum_NBRE = new JFormattedTextField();
      this.jLabel29 = new JLabel();
      this.jLabel30 = new JLabel();
      this.tMontantTotal = new JFormattedTextField();
      this.pnlMotifs_List = new JPanel();
      this.jScrollPane9 = new JScrollPane();
      this.listTable = new JTable();
      this.pnlMotifs1 = new JPanel();
      this.pnlMotifs_Edit1 = new JPanel();
      this.btnImportData = new JButton();
      this.btnFileSelectData = new JButton();
      this.tFile_DonneesPersonnel = new JLabel();
      this.jLabel31 = new JLabel();
      this.tLineNum_DataBegin1 = new JFormattedTextField();
      this.jSeparator17 = new JSeparator();
      this.jSeparator18 = new JSeparator();
      this.tColNum_IDSAL1 = new JFormattedTextField();
      this.jLabel32 = new JLabel();
      this.jSeparator19 = new JSeparator();
      this.tColNum_DATA = new JFormattedTextField();
      this.jLabel33 = new JLabel();
      this.jLabel36 = new JLabel();
      this.jLabel24 = new JLabel();
      this.tDataTerget = new JComboBox();
      this.pnlMotifs_List1 = new JPanel();
      this.setBackground(new Color(255, 255, 255));
      this.setBorder((Border)null);
      this.setAutoscrolls(true);
      this.setFont(new Font("Segoe UI Light", 0, 12));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(1000, 600));
      this.jPanel1.setBackground(new Color(255, 255, 255));
      this.jLabel7.setFont(new Font("Segoe UI Light", 0, 24));
      this.jLabel7.setForeground(new Color(0, 102, 153));
      this.jLabel7.setText("Importation de donn\u00e9es");
      this.jLabel7.setToolTipText("");
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addPreferredGap(ComponentPlacement.RELATED, 589, 32767).addComponent(this.btnExit, -2, 39, -2).addGap(19, 19, 19)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.pnlFooter.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout pnlFooterLayout = new GroupLayout(this.pnlFooter);
      this.pnlFooter.setLayout(pnlFooterLayout);
      pnlFooterLayout.setHorizontalGroup(pnlFooterLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlFooterLayout.createSequentialGroup().addComponent(this.msgLabel, -2, 1132, -2).addContainerGap(-1, 32767)));
      pnlFooterLayout.setVerticalGroup(pnlFooterLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlFooterLayout.createSequentialGroup().addComponent(this.msgLabel, -2, 31, -2).addGap(0, 0, 32767)));
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      this.jTabbedPane1.setForeground(new Color(0, 102, 153));
      this.jTabbedPane1.setFont(new Font("Segoe UI Light", 1, 12));
      this.pnlMotifs.setBackground(new Color(255, 255, 255));
      this.pnlMotifs_Edit.setBackground(new Color(255, 255, 255));
      this.pnlMotifs_Edit.setBorder(BorderFactory.createTitledBorder((Border)null, " Options d'importation ", 0, 0, new Font("Segoe UI Light", 1, 11), new Color(0, 102, 153)));
      this.tMotif.setFont(new Font("Segoe UI Light", 0, 11));
      this.tMotif.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMotif.addActionListener(new 2(this));
      this.jLabel23.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel23.setForeground(new Color(0, 102, 153));
      this.jLabel23.setText("Motif");
      this.cFixe.setBackground(new Color(255, 255, 255));
      this.cFixe.setFont(new Font("Segoe UI Light", 0, 12));
      this.cFixe.setForeground(new Color(0, 102, 153));
      this.cFixe.setText("Fixe");
      this.cFixe.addActionListener(new 3(this));
      this.btnImportRub.setToolTipText("Importer");
      this.btnImportRub.setContentAreaFilled(false);
      this.btnImportRub.setCursor(new Cursor(12));
      this.btnImportRub.setOpaque(true);
      this.btnImportRub.addActionListener(new 4(this));
      this.btnFileSelectRub.setToolTipText("Selectionner le fichier Excel");
      this.btnFileSelectRub.setContentAreaFilled(false);
      this.btnFileSelectRub.setCursor(new Cursor(12));
      this.btnFileSelectRub.setOpaque(true);
      this.btnFileSelectRub.addActionListener(new 5(this));
      this.tFile_RubriquePaie.setFont(new Font("Segoe UI Light", 0, 10));
      this.tFile_RubriquePaie.setForeground(new Color(0, 102, 153));
      this.tFile_RubriquePaie.setText(".");
      this.jLabel25.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel25.setForeground(new Color(0, 102, 153));
      this.jLabel25.setText("1ere ligne");
      this.tLineNum_DataBegin.setBorder((Border)null);
      this.tLineNum_DataBegin.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tLineNum_DataBegin.setFont(new Font("Segoe UI Light", 0, 16));
      this.tLineNum_DataBegin.addCaretListener(new 6(this));
      this.tColNum_IDSAL.setBorder((Border)null);
      this.tColNum_IDSAL.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tColNum_IDSAL.setFont(new Font("Segoe UI Light", 0, 16));
      this.tColNum_IDSAL.addCaretListener(new 7(this));
      this.jLabel26.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel26.setForeground(new Color(0, 102, 153));
      this.jLabel26.setText("#Col. Id Sal.");
      this.tColNum_IDRUB.setBorder((Border)null);
      this.tColNum_IDRUB.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tColNum_IDRUB.setFont(new Font("Segoe UI Light", 0, 16));
      this.tColNum_IDRUB.addCaretListener(new 8(this));
      this.jLabel27.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel27.setForeground(new Color(0, 102, 153));
      this.jLabel27.setText("#Col. Id Rub.");
      this.tColNum_BASE.setBorder((Border)null);
      this.tColNum_BASE.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tColNum_BASE.setFont(new Font("Segoe UI Light", 0, 16));
      this.tColNum_BASE.addCaretListener(new 9(this));
      this.jLabel28.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel28.setForeground(new Color(0, 102, 153));
      this.jLabel28.setText("#Col. Base");
      this.tColNum_NBRE.setBorder((Border)null);
      this.tColNum_NBRE.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tColNum_NBRE.setFont(new Font("Segoe UI Light", 0, 16));
      this.tColNum_NBRE.addCaretListener(new 10(this));
      this.jLabel29.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel29.setForeground(new Color(0, 102, 153));
      this.jLabel29.setText("#Col. Nbre");
      this.jLabel30.setFont(new Font("Segoe UI Light", 1, 10));
      this.jLabel30.setForeground(new Color(255, 51, 0));
      this.jLabel30.setText("Attention! Uniquement format Excel 97-2003");
      this.tMontantTotal.setBorder((Border)null);
      this.tMontantTotal.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tMontantTotal.setHorizontalAlignment(4);
      this.tMontantTotal.setText("0");
      this.tMontantTotal.setFont(new Font("Segoe UI Light", 1, 10));
      this.tMontantTotal.addCaretListener(new 11(this));
      GroupLayout pnlMotifs_EditLayout = new GroupLayout(this.pnlMotifs_Edit);
      this.pnlMotifs_Edit.setLayout(pnlMotifs_EditLayout);
      pnlMotifs_EditLayout.setHorizontalGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.btnFileSelectRub, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tFile_RubriquePaie, -2, 358, -2)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel30, -2, 269, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tMontantTotal, -1, 136, 32767))).addPreferredGap(ComponentPlacement.RELATED, 19, 32767).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel25, Alignment.LEADING, -1, -1, 32767).addComponent(this.tLineNum_DataBegin, Alignment.LEADING).addComponent(this.jSeparator12, -2, 71, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel26, Alignment.LEADING, -1, -1, 32767).addComponent(this.tColNum_IDSAL, Alignment.LEADING).addComponent(this.jSeparator13, -2, 71, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel27, Alignment.LEADING, -1, -1, 32767).addComponent(this.tColNum_IDRUB, Alignment.LEADING).addComponent(this.jSeparator14, -2, 71, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel28, Alignment.LEADING, -1, -1, 32767).addComponent(this.tColNum_BASE, Alignment.LEADING).addComponent(this.jSeparator15, -2, 71, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel29, Alignment.LEADING, -1, -1, 32767).addComponent(this.tColNum_NBRE, Alignment.LEADING).addComponent(this.jSeparator16, -2, 71, -2)).addGap(18, 18, 18).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tMotif, -2, 120, -2).addComponent(this.jLabel23, -2, 120, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cFixe).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnImportRub, -2, 35, -2).addContainerGap()));
      pnlMotifs_EditLayout.setVerticalGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnFileSelectRub, -2, 35, -2).addComponent(this.tFile_RubriquePaie, -2, 29, -2)).addGap(0, 0, 0).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.jLabel30, -2, 15, -2).addComponent(this.tMontantTotal, -2, 18, -2)).addContainerGap(-1, 32767)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel26).addGap(0, 0, 0).addComponent(this.tColNum_IDSAL, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator13, -2, -1, -2)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel25).addGap(0, 0, 0).addComponent(this.tLineNum_DataBegin, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator12, -2, -1, -2)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel27).addGap(0, 0, 0).addComponent(this.tColNum_IDRUB, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator14, -2, -1, -2)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel28).addGap(0, 0, 0).addComponent(this.tColNum_BASE, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator15, -2, -1, -2)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel29).addGap(0, 0, 0).addComponent(this.tColNum_NBRE, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator16, -2, -1, -2))).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING, false).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.cFixe, -1, -1, 32767).addGap(2, 2, 2)).addGroup(Alignment.LEADING, pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel23).addGap(0, 0, 0).addComponent(this.tMotif, -2, 30, -2))).addComponent(this.btnImportRub, -2, 35, -2)).addGap(17, 17, 17)));
      this.pnlMotifs_List.setBackground(new Color(255, 255, 255));
      GroupLayout pnlMotifs_ListLayout = new GroupLayout(this.pnlMotifs_List);
      this.pnlMotifs_List.setLayout(pnlMotifs_ListLayout);
      pnlMotifs_ListLayout.setHorizontalGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addGap(0, 1151, 32767));
      pnlMotifs_ListLayout.setVerticalGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addGap(0, 0, 32767));
      this.listTable.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable.setSelectionBackground(new Color(0, 102, 153));
      this.listTable.setShowGrid(false);
      this.listTable.addMouseListener(new 12(this));
      this.jScrollPane9.setViewportView(this.listTable);
      GroupLayout pnlMotifsLayout = new GroupLayout(this.pnlMotifs);
      this.pnlMotifs.setLayout(pnlMotifsLayout);
      pnlMotifsLayout.setHorizontalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addGap(6, 6, 6).addGroup(pnlMotifsLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.jScrollPane9, -2, 1078, -2).addComponent(this.pnlMotifs_Edit, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlMotifs_List, -2, -1, -2).addContainerGap(-1, 32767)));
      pnlMotifsLayout.setVerticalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addGap(142, 142, 142).addComponent(this.pnlMotifs_List, -1, -1, 32767).addContainerGap()).addGroup(pnlMotifsLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlMotifs_Edit, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane9, -2, 356, -2).addContainerGap(-1, 32767)));
      this.jTabbedPane1.addTab("Rubriques de paie", this.pnlMotifs);
      this.pnlMotifs1.setBackground(new Color(255, 255, 255));
      this.pnlMotifs_Edit1.setBackground(new Color(255, 255, 255));
      this.pnlMotifs_Edit1.setBorder(BorderFactory.createTitledBorder((Border)null, " Options d'importation ", 0, 0, new Font("Segoe UI Light", 1, 11), new Color(0, 102, 153)));
      this.btnImportData.setToolTipText("Importer");
      this.btnImportData.setContentAreaFilled(false);
      this.btnImportData.setCursor(new Cursor(12));
      this.btnImportData.setOpaque(true);
      this.btnImportData.addActionListener(new 13(this));
      this.btnFileSelectData.setToolTipText("Selectionner le fichier Excel");
      this.btnFileSelectData.setContentAreaFilled(false);
      this.btnFileSelectData.setCursor(new Cursor(12));
      this.btnFileSelectData.setOpaque(true);
      this.btnFileSelectData.addActionListener(new 14(this));
      this.tFile_DonneesPersonnel.setFont(new Font("Segoe UI Light", 0, 10));
      this.tFile_DonneesPersonnel.setForeground(new Color(0, 102, 153));
      this.tFile_DonneesPersonnel.setText(".");
      this.jLabel31.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel31.setForeground(new Color(0, 102, 153));
      this.jLabel31.setText("1ere ligne *");
      this.tLineNum_DataBegin1.setBorder((Border)null);
      this.tLineNum_DataBegin1.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tLineNum_DataBegin1.setFont(new Font("Segoe UI Light", 0, 16));
      this.tLineNum_DataBegin1.addCaretListener(new 15(this));
      this.tColNum_IDSAL1.setBorder((Border)null);
      this.tColNum_IDSAL1.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tColNum_IDSAL1.setFont(new Font("Segoe UI Light", 0, 16));
      this.tColNum_IDSAL1.addCaretListener(new 16(this));
      this.jLabel32.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel32.setForeground(new Color(0, 102, 153));
      this.jLabel32.setText("#Col. Id Sal. *");
      this.tColNum_DATA.setBorder((Border)null);
      this.tColNum_DATA.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tColNum_DATA.setFont(new Font("Segoe UI Light", 0, 16));
      this.tColNum_DATA.addCaretListener(new 17(this));
      this.jLabel33.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel33.setForeground(new Color(0, 102, 153));
      this.jLabel33.setText("#Col. Donn\u00e9e");
      this.jLabel36.setFont(new Font("Segoe UI Light", 1, 10));
      this.jLabel36.setForeground(new Color(255, 51, 0));
      this.jLabel36.setText("Attention! Uniquement format Excel 97-2003");
      this.jLabel24.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel24.setForeground(new Color(0, 102, 153));
      this.jLabel24.setText("Cible");
      this.tDataTerget.setFont(new Font("Segoe UI Light", 0, 11));
      this.tDataTerget.setModel(new DefaultComboBoxModel(new String[]{"E-mail", "T\u00e9l\u00e9phone", "Addresse"}));
      this.tDataTerget.addActionListener(new 18(this));
      GroupLayout pnlMotifs_Edit1Layout = new GroupLayout(this.pnlMotifs_Edit1);
      this.pnlMotifs_Edit1.setLayout(pnlMotifs_Edit1Layout);
      pnlMotifs_Edit1Layout.setHorizontalGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addComponent(this.btnFileSelectData, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tFile_DonneesPersonnel, -2, 358, -2)).addComponent(this.jLabel36, -2, 259, -2)).addPreferredGap(ComponentPlacement.RELATED, 50, 32767).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel31, Alignment.LEADING, -1, -1, 32767).addComponent(this.tLineNum_DataBegin1, Alignment.LEADING).addComponent(this.jSeparator17, -2, 71, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel32, Alignment.LEADING, -1, -1, 32767).addComponent(this.tColNum_IDSAL1, Alignment.LEADING).addComponent(this.jSeparator18, -2, 71, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel33, Alignment.LEADING, -1, -1, 32767).addComponent(this.tColNum_DATA, Alignment.LEADING).addComponent(this.jSeparator19, -2, 71, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tDataTerget, -2, 120, -2).addComponent(this.jLabel24, -2, 120, -2)).addGap(225, 225, 225).addComponent(this.btnImportData, -2, 35, -2).addContainerGap()));
      pnlMotifs_Edit1Layout.setVerticalGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnFileSelectData, -2, 35, -2).addComponent(this.tFile_DonneesPersonnel, -2, 29, -2)).addGap(3, 3, 3).addComponent(this.jLabel36, -2, 15, -2).addContainerGap(-1, 32767)).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.TRAILING).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addComponent(this.jLabel32).addGap(0, 0, 0).addComponent(this.tColNum_IDSAL1, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator18, -2, -1, -2)).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addComponent(this.jLabel31).addGap(0, 0, 0).addComponent(this.tLineNum_DataBegin1, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator17, -2, -1, -2)).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addGroup(pnlMotifs_Edit1Layout.createParallelGroup(Alignment.TRAILING).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addComponent(this.jLabel33).addGap(0, 0, 0).addComponent(this.tColNum_DATA, -2, 30, -2)).addGroup(pnlMotifs_Edit1Layout.createSequentialGroup().addComponent(this.jLabel24).addGap(0, 0, 0).addComponent(this.tDataTerget, -2, 30, -2))).addGap(0, 0, 0).addComponent(this.jSeparator19, -2, -1, -2))).addComponent(this.btnImportData, -2, 35, -2)).addGap(0, 0, 32767)));
      this.pnlMotifs_List1.setBackground(new Color(255, 255, 255));
      GroupLayout pnlMotifs_List1Layout = new GroupLayout(this.pnlMotifs_List1);
      this.pnlMotifs_List1.setLayout(pnlMotifs_List1Layout);
      pnlMotifs_List1Layout.setHorizontalGroup(pnlMotifs_List1Layout.createParallelGroup(Alignment.LEADING).addGap(0, 1151, 32767));
      pnlMotifs_List1Layout.setVerticalGroup(pnlMotifs_List1Layout.createParallelGroup(Alignment.LEADING).addGap(0, 324, 32767));
      GroupLayout pnlMotifs1Layout = new GroupLayout(this.pnlMotifs1);
      this.pnlMotifs1.setLayout(pnlMotifs1Layout);
      pnlMotifs1Layout.setHorizontalGroup(pnlMotifs1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs1Layout.createSequentialGroup().addContainerGap().addComponent(this.pnlMotifs_Edit1, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlMotifs_List1, -2, -1, -2).addContainerGap(-1, 32767)));
      pnlMotifs1Layout.setVerticalGroup(pnlMotifs1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs1Layout.createSequentialGroup().addGap(142, 142, 142).addComponent(this.pnlMotifs_List1, -1, -1, 32767).addContainerGap()).addGroup(pnlMotifs1Layout.createSequentialGroup().addContainerGap().addComponent(this.pnlMotifs_Edit1, -2, -1, -2).addContainerGap(-1, 32767)));
      this.jTabbedPane1.addTab("Donn\u00e9es de salari\u00e9s", this.pnlMotifs1);
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.pnlFooter, -2, -1, -2).addGap(53, 53, 53)).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.progressBar, -2, 1133, -2).addGroup(pnlBodyLayout.createSequentialGroup().addContainerGap().addComponent(this.jTabbedPane1, -2, 0, 32767))).addContainerGap(-1, 32767)));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.progressBar, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jTabbedPane1, -2, 506, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlFooter, -2, -1, -2).addContainerGap()));
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jPanel1, -2, -1, -2).addComponent(this.pnlBody, -2, 1131, -2)).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -2, -1, -2).addGap(0, 0, 0)));
      this.setSize(new Dimension(1133, 629));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void listTableMouseClicked(MouseEvent var1) {
   }

   private void btnImportRubActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      Thread t = new 19(this);
      t.start();
   }

   private void cFixeActionPerformed(ActionEvent var1) {
   }

   private void tMotifActionPerformed(ActionEvent var1) {
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmBulletinPaie(var10003));
   }

   private void btnFileSelectRubActionPerformed(ActionEvent var1) {
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Classeur Excel 97-2003", new String[]{"xls"});
      chooser.setFileFilter(filter);
      int returnVal = chooser.showOpenDialog(this);
      if (returnVal == 0) {
         this.tFile_RubriquePaie.setText(chooser.getSelectedFile().getPath());
      }

   }

   private void tLineNum_DataBeginCaretUpdate(CaretEvent var1) {
   }

   private void tColNum_IDSALCaretUpdate(CaretEvent var1) {
   }

   private void tColNum_IDRUBCaretUpdate(CaretEvent var1) {
   }

   private void tColNum_BASECaretUpdate(CaretEvent var1) {
   }

   private void tColNum_NBRECaretUpdate(CaretEvent var1) {
   }

   private void tMontantTotalCaretUpdate(CaretEvent var1) {
   }

   private void btnImportDataActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      Thread t = new 20(this);
      t.start();
   }

   private void btnFileSelectDataActionPerformed(ActionEvent var1) {
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Classeur Excel 97-2003", new String[]{"xls"});
      chooser.setFileFilter(filter);
      int returnVal = chooser.showOpenDialog(this);
      if (returnVal == 0) {
         this.tFile_DonneesPersonnel.setText(chooser.getSelectedFile().getPath());
      }

   }

   private void tLineNum_DataBegin1CaretUpdate(CaretEvent var1) {
   }

   private void tColNum_IDSAL1CaretUpdate(CaretEvent var1) {
   }

   private void tColNum_DATACaretUpdate(CaretEvent var1) {
   }

   private void tDataTergetActionPerformed(ActionEvent var1) {
   }
}
