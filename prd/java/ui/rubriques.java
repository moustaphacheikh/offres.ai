package com.mccmr.ui;

import com.mccmr.entity.Poste;
import com.mccmr.entity.Rubrique;
import com.mccmr.entity.Rubriqueformule;
import com.mccmr.entity.Rubriquemodel;
import com.mccmr.entity.Utilisateurs;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.CustomTableRenderer;
import com.mccmr.util.ModelClass;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Query;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.hibernate.HibernateException;

public class rubriques extends JInternalFrame {
   public menu menu;
   public static Rubrique selectedOne;
   public List<Rubrique> dataList;
   public List<Rubrique> dataListInit;
   private JButton FBConstanteAddButton;
   private JButton FBFonctionAddButton;
   private JButton FBOperateurAddButton;
   private JButton FBRubriqueAddButton;
   private JTabbedPane TabbedPane;
   private JButton bsFBButton;
   private JButton bsFNButton;
   private JButton btnAddModelMnt;
   private JButton btnDelModelMnt;
   private JButton btnDelete;
   private JButton btnExit;
   private JButton btnNew;
   private JButton btnSave;
   private JCheckBox cAvantagesNature;
   private JCheckBox cBaseAuto;
   private JCheckBox cCNAM;
   private JCheckBox cCNSS;
   private JCheckBox cCumulable;
   private JCheckBox cITS;
   private JCheckBox cNombreAuto;
   private JCheckBox cPlafone;
   private JLabel jLabel11;
   private JLabel jLabel12;
   private JLabel jLabel13;
   private JLabel jLabel14;
   private JLabel jLabel15;
   private JLabel jLabel16;
   private JLabel jLabel18;
   private JLabel jLabel19;
   private JLabel jLabel20;
   private JLabel jLabel21;
   private JLabel jLabel22;
   private JLabel jLabel23;
   private JLabel jLabel24;
   private JLabel jLabel25;
   private JLabel jLabel26;
   private JLabel jLabel27;
   private JLabel jLabel28;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JPanel jPanel2;
   private JPanel jPanel3;
   private JScrollPane jScrollPane10;
   private JScrollPane jScrollPane9;
   private JSeparator jSeparator1;
   private JSeparator jSeparator10;
   private JSeparator jSeparator12;
   private JSeparator jSeparator14;
   private JSeparator jSeparator7;
   private JSeparator jSeparator8;
   private JLabel lFormuleBase;
   private JLabel lFormuleNombre;
   private JLabel lbSearch;
   private JTable listTable;
   private JLabel msgLabel;
   private JPanel pnlBody;
   private JPanel pnlMotifs;
   private JPanel pnlMotifs_Edit;
   private JPanel pnlMotifs_List;
   private JProgressBar progressBar;
   private JTextField searchField;
   private JFormattedTextField tChapitreCompta;
   private JFormattedTextField tCode;
   private JComboBox<Object> tDeductionDu;
   private JComboBox<Object> tFBRubriques;
   private JComboBox<Object> tFBoperateurs;
   private JFormattedTextField tFNombre;
   private JComboBox<Object> tFRFonctions;
   private JFormattedTextField tFormuleBase;
   private JFormattedTextField tFormuleNombre;
   private JTextField tLibelle;
   private JFormattedTextField tMntMens;
   private JFormattedTextField tNoCompteCompta;
   private JTextField tNoCompteComptaCle;
   private JComboBox<Object> tPartieFormule;
   private JComboBox<Object> tPoste;
   private JComboBox<Object> tSens;
   private JTable tableRubModel;
   private JButton viderFBButton;
   private JButton viderFNButton;

   public rubriques() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.lbSearch.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SEARCH, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelete.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnNew.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.FBOperateurAddButton.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.FBFonctionAddButton.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.FBRubriqueAddButton.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.FBConstanteAddButton.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.viderFBButton.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.REMOVE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.bsFBButton.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BACKSPACE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.viderFNButton.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.REMOVE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.bsFNButton.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BACKSPACE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnAddModelMnt.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelModelMnt.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.dataListUpdate();
      this.afficherListe();
      this.clearFields();
      this.menu.remplirCombo("Rubrique", this.tFBRubriques);
      this.menu.remplirCombo("Poste", this.tPoste);
   }

   public void dataListUpdate() {
      Query q = this.menu.entityManager.createQuery("Select p from Rubrique p");
      q.setMaxResults(1000000);
      this.dataListInit = q.getResultList();
   }

   public void RolesAction(Utilisateurs var1) {
      if (!role.isAjout()) {
         this.btnSave.setVisible(false);
      }

      if (!role.isMaj()) {
         this.btnSave.setVisible(false);
         this.FBConstanteAddButton.setVisible(false);
         this.FBOperateurAddButton.setVisible(false);
         this.FBFonctionAddButton.setVisible(false);
         this.FBRubriqueAddButton.setVisible(false);
      }

      if (!role.isSuppression()) {
         this.btnDelete.setVisible(false);
         this.viderFBButton.setVisible(false);
         this.viderFNButton.setVisible(false);
      }

   }

   private void clearFields() {
      selectedOne = null;
      this.tCode.setEnabled(true);
      this.tCode.setValue((Object)null);
      this.tNoCompteCompta.setValue(0L);
      this.tChapitreCompta.setValue(0L);
      this.tNoCompteComptaCle.setText("");
      this.tLibelle.setText("");
      this.cAvantagesNature.setSelected(false);
      this.cBaseAuto.setSelected(false);
      this.cCNAM.setSelected(false);
      this.cCNSS.setSelected(false);
      this.cCumulable.setSelected(false);
      this.cITS.setSelected(false);
      this.cNombreAuto.setSelected(false);
      this.cPlafone.setSelected(false);
      this.tCode.requestFocus();
      this.btnNew.setEnabled(true);
      this.btnSave.setEnabled(true);
      this.btnDelete.setEnabled(false);
   }

   private void afficherFormule(String var1) {
      String s = this.menu.pc.rubriqueFormuleStr(selectedOne, partie);
      if (partie.equalsIgnoreCase("B")) {
         this.tFormuleBase.setText(s);
         this.lFormuleBase.setText(s);
      }

      if (partie.equalsIgnoreCase("N")) {
         this.tFormuleNombre.setText(s);
         this.lFormuleNombre.setText(s);
      }

   }

   private void afficherListe() {
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmRubrique(var10003));
      this.listTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      this.dataList = this.dataListInit;
      if (!this.searchField.getText().isEmpty()) {
         this.dataList = (List)this.dataList.stream().filter((var1) -> var1.getLibelle().toLowerCase().contains(this.searchField.getText().toLowerCase())).collect(Collectors.toList());
      }

      this.dataList = (List)this.dataList.stream().sorted(Comparator.comparing(Rubrique::getId)).collect(Collectors.toList());

      for(Rubrique o : this.dataList) {
         ((ModelClass.tmRubrique)this.listTable.getModel()).addRow(o);
      }

      this.listTable.getColumnModel().getColumn(0).setPreferredWidth(60);
      this.listTable.getColumnModel().getColumn(1).setPreferredWidth(300);
      this.listTable.getColumnModel().getColumn(10).setPreferredWidth(200);
      this.listTable.setRowHeight(30);
      this.listTable.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable.getModel());
      this.listTable.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      this.menu.mc.getClass();
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   private String validateData() {
      String errMsg = "";
      if (this.tLibelle.getText().isEmpty()) {
         errMsg = "Intitul\u00e9 obligatoire";
         this.tLibelle.requestFocus();
      }

      if (this.tCode.getValue() == null) {
         errMsg = "Code obligatoire";
         this.tCode.requestFocus();
      }

      if (this.tNoCompteCompta.getValue() == null) {
         errMsg = "N\u00b0 Cpte Comptable obligatoire";
         this.tNoCompteCompta.requestFocus();
      }

      return errMsg;
   }

   private void findByID(int var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      this.tCode.setEnabled(false);
      selectedOne = this.menu.pc.rubriqueById(codeRub);
      if (selectedOne != null) {
         this.tCode.setValue(selectedOne.getId());
         this.tLibelle.setText(selectedOne.getLibelle());
         this.tSens.setSelectedItem(selectedOne.getSens());
         this.cCumulable.setSelected(selectedOne.isCumulable());
         this.cAvantagesNature.setSelected(selectedOne.isAvantagesNature());
         this.cITS.setSelected(selectedOne.isIts());
         this.cCNSS.setSelected(selectedOne.isCnss());
         this.cCNAM.setSelected(selectedOne.isCnam());
         this.cPlafone.setSelected(selectedOne.isPlafone());
         this.tDeductionDu.setSelectedItem(selectedOne.getDeductionDu());
         this.cBaseAuto.setSelected(selectedOne.isBaseAuto());
         this.cNombreAuto.setSelected(selectedOne.isNombreAuto());
         this.tNoCompteCompta.setValue(selectedOne.getNoCompteCompta());
         this.tChapitreCompta.setValue(selectedOne.getNoChapitreCompta());
         this.tNoCompteComptaCle.setText(selectedOne.getNoCompteComptaCle());
         this.afficherFormule("B");
         this.afficherFormule("N");
         this.afficherRubModel();
         this.btnNew.setEnabled(true);
         this.btnSave.setEnabled(true);
         this.btnDelete.setEnabled(((List)this.menu.pc.dlRubriquepaie.stream().filter((var0) -> var0.getRubrique().getId() == selectedOne.getId()).collect(Collectors.toList())).isEmpty());
      }

   }

   private void afficherRubModel() {
      JTable var10000 = this.tableRubModel;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmRubModel(var10003));
      this.tableRubModel.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.tableRubModel.getColumnModel().getColumn(1).setPreferredWidth(250);
      this.tableRubModel.getColumnModel().getColumn(2).setPreferredWidth(80);
      this.tableRubModel.setRowHeight(20);
      if (selectedOne.getId() >= 0) {
         try {
            Query q = this.menu.entityManager.createQuery("Select p from Rubriquemodel p where p.rubrique=" + selectedOne.getId() + " order by p.id");
            q.setMaxResults(1000000);

            for(Object o : q.getResultList()) {
               Rubriquemodel rs = (Rubriquemodel)o;
               ((ModelClass.tmRubModel)this.tableRubModel.getModel()).addRow(rs);
            }
         } catch (HibernateException he) {
            he.printStackTrace();
         }
      }

   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.pnlMotifs = new JPanel();
      this.pnlMotifs_List = new JPanel();
      this.jScrollPane9 = new JScrollPane();
      this.listTable = new JTable();
      this.jSeparator1 = new JSeparator();
      this.searchField = new JTextField();
      this.lbSearch = new JLabel();
      this.TabbedPane = new JTabbedPane();
      this.pnlMotifs_Edit = new JPanel();
      this.btnNew = new JButton();
      this.btnSave = new JButton();
      this.btnDelete = new JButton();
      this.jLabel12 = new JLabel();
      this.tCode = new JFormattedTextField();
      this.jSeparator7 = new JSeparator();
      this.jSeparator8 = new JSeparator();
      this.jLabel13 = new JLabel();
      this.jSeparator10 = new JSeparator();
      this.tSens = new JComboBox();
      this.jLabel11 = new JLabel();
      this.tDeductionDu = new JComboBox();
      this.jLabel14 = new JLabel();
      this.jLabel15 = new JLabel();
      this.tChapitreCompta = new JFormattedTextField();
      this.jLabel16 = new JLabel();
      this.cITS = new JCheckBox();
      this.cCNSS = new JCheckBox();
      this.cCNAM = new JCheckBox();
      this.cPlafone = new JCheckBox();
      this.cAvantagesNature = new JCheckBox();
      this.cCumulable = new JCheckBox();
      this.cBaseAuto = new JCheckBox();
      this.cNombreAuto = new JCheckBox();
      this.lFormuleBase = new JLabel();
      this.lFormuleNombre = new JLabel();
      this.jLabel26 = new JLabel();
      this.tNoCompteCompta = new JFormattedTextField();
      this.jLabel28 = new JLabel();
      this.tLibelle = new JTextField();
      this.tNoCompteComptaCle = new JTextField();
      this.jPanel2 = new JPanel();
      this.tPartieFormule = new JComboBox();
      this.jLabel18 = new JLabel();
      this.tFormuleNombre = new JFormattedTextField();
      this.tFormuleBase = new JFormattedTextField();
      this.jLabel19 = new JLabel();
      this.jLabel20 = new JLabel();
      this.viderFBButton = new JButton();
      this.viderFNButton = new JButton();
      this.tFBRubriques = new JComboBox();
      this.jLabel21 = new JLabel();
      this.jLabel23 = new JLabel();
      this.tFBoperateurs = new JComboBox();
      this.FBOperateurAddButton = new JButton();
      this.tFRFonctions = new JComboBox();
      this.jLabel24 = new JLabel();
      this.FBFonctionAddButton = new JButton();
      this.FBRubriqueAddButton = new JButton();
      this.jSeparator12 = new JSeparator();
      this.tFNombre = new JFormattedTextField();
      this.jLabel25 = new JLabel();
      this.FBConstanteAddButton = new JButton();
      this.bsFBButton = new JButton();
      this.bsFNButton = new JButton();
      this.jPanel3 = new JPanel();
      this.tPoste = new JComboBox();
      this.jLabel22 = new JLabel();
      this.jScrollPane10 = new JScrollPane();
      this.tableRubModel = new JTable();
      this.jLabel27 = new JLabel();
      this.jSeparator14 = new JSeparator();
      this.tMntMens = new JFormattedTextField();
      this.btnAddModelMnt = new JButton();
      this.btnDelModelMnt = new JButton();
      this.progressBar = new JProgressBar();
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
      this.jLabel7.setText("Rubriques de paie");
      this.jLabel7.setToolTipText("");
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addPreferredGap(ComponentPlacement.RELATED, 589, 32767).addComponent(this.btnExit, -2, 39, -2).addGap(19, 19, 19)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.pnlMotifs.setBackground(new Color(255, 255, 255));
      this.pnlMotifs_List.setBackground(new Color(255, 255, 255));
      this.listTable.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable.setSelectionBackground(new Color(0, 102, 153));
      this.listTable.setShowGrid(false);
      this.listTable.addMouseListener(new 2(this));
      this.jScrollPane9.setViewportView(this.listTable);
      this.searchField.setFont(new Font("Segoe UI Light", 1, 12));
      this.searchField.setBorder((Border)null);
      this.searchField.addCaretListener(new 3(this));
      this.searchField.addKeyListener(new 4(this));
      this.lbSearch.setFont(new Font("Segoe UI Light", 0, 12));
      this.lbSearch.setCursor(new Cursor(12));
      this.lbSearch.addMouseListener(new 5(this));
      GroupLayout pnlMotifs_ListLayout = new GroupLayout(this.pnlMotifs_List);
      this.pnlMotifs_List.setLayout(pnlMotifs_ListLayout);
      pnlMotifs_ListLayout.setHorizontalGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlMotifs_ListLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.jScrollPane9, -2, 1114, -2).addContainerGap()).addGroup(pnlMotifs_ListLayout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.lbSearch, -2, 31, -2).addGap(0, 0, 0).addGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addComponent(this.searchField, -2, 310, -2).addComponent(this.jSeparator1, -2, 300, -2)).addContainerGap(-1, 32767)));
      pnlMotifs_ListLayout.setVerticalGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlMotifs_ListLayout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.lbSearch, -1, -1, 32767).addComponent(this.searchField, -2, 30, -2)).addGap(0, 0, 0).addComponent(this.jSeparator1, -2, 6, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane9, -1, 229, 32767).addContainerGap()));
      this.TabbedPane.setForeground(new Color(0, 102, 153));
      this.TabbedPane.setFont(new Font("Segoe UI Light", 1, 12));
      this.pnlMotifs_Edit.setBackground(new Color(255, 255, 255));
      this.btnNew.setBackground(new Color(255, 255, 255));
      this.btnNew.setContentAreaFilled(false);
      this.btnNew.setCursor(new Cursor(12));
      this.btnNew.setOpaque(true);
      this.btnNew.addActionListener(new 6(this));
      this.btnSave.setBackground(new Color(255, 255, 255));
      this.btnSave.setContentAreaFilled(false);
      this.btnSave.setCursor(new Cursor(12));
      this.btnSave.setEnabled(false);
      this.btnSave.setOpaque(true);
      this.btnSave.addActionListener(new 7(this));
      this.btnDelete.setBackground(new Color(255, 255, 255));
      this.btnDelete.setContentAreaFilled(false);
      this.btnDelete.setCursor(new Cursor(12));
      this.btnDelete.setEnabled(false);
      this.btnDelete.setOpaque(true);
      this.btnDelete.addActionListener(new 8(this));
      this.jLabel12.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel12.setForeground(new Color(0, 102, 153));
      this.jLabel12.setText("Code *");
      this.tCode.setBorder((Border)null);
      this.tCode.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tCode.setFont(new Font("Segoe UI Light", 0, 16));
      this.tCode.addCaretListener(new 9(this));
      this.jLabel13.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel13.setForeground(new Color(0, 102, 153));
      this.jLabel13.setText("Libell\u00e9 *");
      this.tSens.setFont(new Font("Segoe UI Light", 0, 16));
      this.tSens.setModel(new DefaultComboBoxModel(new String[]{"G", "R"}));
      this.tSens.addActionListener(new 10(this));
      this.jLabel11.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel11.setForeground(new Color(0, 102, 153));
      this.jLabel11.setText("Sens *");
      this.tDeductionDu.setFont(new Font("Segoe UI Light", 0, 16));
      this.tDeductionDu.setModel(new DefaultComboBoxModel(new String[]{"Net", "Brut"}));
      this.jLabel14.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel14.setForeground(new Color(0, 102, 153));
      this.jLabel14.setText("Sur");
      this.jLabel15.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel15.setForeground(new Color(0, 102, 153));
      this.jLabel15.setText("/");
      this.tChapitreCompta.setBorder((Border)null);
      this.tChapitreCompta.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tChapitreCompta.setFont(new Font("Segoe UI Light", 0, 16));
      this.tChapitreCompta.addCaretListener(new 11(this));
      this.jLabel16.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel16.setForeground(new Color(0, 102, 153));
      this.jLabel16.setText("Chapitre comptable");
      this.cITS.setBackground(new Color(255, 255, 255));
      this.cITS.setFont(new Font("Segoe UI Light", 0, 12));
      this.cITS.setForeground(new Color(0, 102, 153));
      this.cITS.setText("ITS");
      this.cITS.addActionListener(new 12(this));
      this.cCNSS.setBackground(new Color(255, 255, 255));
      this.cCNSS.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCNSS.setForeground(new Color(0, 102, 153));
      this.cCNSS.setText("CNSS");
      this.cCNAM.setBackground(new Color(255, 255, 255));
      this.cCNAM.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCNAM.setForeground(new Color(0, 102, 153));
      this.cCNAM.setText("CNAM");
      this.cPlafone.setBackground(new Color(255, 255, 255));
      this.cPlafone.setFont(new Font("Segoe UI Light", 0, 12));
      this.cPlafone.setForeground(new Color(0, 102, 153));
      this.cPlafone.setText("Plafon\u00e9");
      this.cAvantagesNature.setBackground(new Color(255, 255, 255));
      this.cAvantagesNature.setFont(new Font("Segoe UI Light", 0, 12));
      this.cAvantagesNature.setForeground(new Color(0, 102, 153));
      this.cAvantagesNature.setText("Avantage en nature");
      this.cCumulable.setBackground(new Color(255, 255, 255));
      this.cCumulable.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCumulable.setForeground(new Color(0, 102, 153));
      this.cCumulable.setText("Cumulable");
      this.cBaseAuto.setBackground(new Color(255, 255, 255));
      this.cBaseAuto.setFont(new Font("Segoe UI Light", 0, 12));
      this.cBaseAuto.setForeground(new Color(0, 102, 153));
      this.cBaseAuto.setText("Base auto");
      this.cNombreAuto.setBackground(new Color(255, 255, 255));
      this.cNombreAuto.setFont(new Font("Segoe UI Light", 0, 12));
      this.cNombreAuto.setForeground(new Color(0, 102, 153));
      this.cNombreAuto.setText("Nombre auto");
      this.lFormuleBase.setFont(new Font("Segoe UI Light", 1, 12));
      this.lFormuleBase.setForeground(new Color(51, 51, 51));
      this.lFormuleBase.setText("...");
      this.lFormuleNombre.setFont(new Font("Segoe UI Light", 1, 12));
      this.lFormuleNombre.setForeground(new Color(51, 51, 51));
      this.lFormuleNombre.setText("...");
      this.jLabel26.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel26.setForeground(new Color(0, 102, 153));
      this.jLabel26.setText("Compte comptable");
      this.tNoCompteCompta.setBorder((Border)null);
      this.tNoCompteCompta.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tNoCompteCompta.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNoCompteCompta.addCaretListener(new 13(this));
      this.jLabel28.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel28.setForeground(new Color(0, 102, 153));
      this.jLabel28.setText("Cl\u00e9");
      this.tLibelle.setFont(new Font("Segoe UI Light", 0, 16));
      this.tLibelle.setBorder((Border)null);
      this.tLibelle.addKeyListener(new 14(this));
      this.tNoCompteComptaCle.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNoCompteComptaCle.setBorder((Border)null);
      this.tNoCompteComptaCle.addKeyListener(new 15(this));
      GroupLayout pnlMotifs_EditLayout = new GroupLayout(this.pnlMotifs_Edit);
      this.pnlMotifs_Edit.setLayout(pnlMotifs_EditLayout);
      pnlMotifs_EditLayout.setHorizontalGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel12, Alignment.LEADING, -1, -1, 32767).addComponent(this.tCode, Alignment.LEADING).addComponent(this.jSeparator7, -2, 71, -2)).addGap(18, 18, 18).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel13, -1, -1, 32767).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jSeparator10, -2, 450, -2).addComponent(this.tLibelle, -2, 365, -2)).addGap(0, 0, 32767))).addGap(18, 18, 18).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel11, -1, -1, 32767).addComponent(this.tSens, -2, 74, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel15).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel14, -2, 74, -2).addComponent(this.tDeductionDu, -2, -1, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel16, -1, -1, 32767).addComponent(this.tChapitreCompta)).addGap(24, 24, 24).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tNoCompteCompta, -2, 121, -2).addComponent(this.jLabel26)).addGap(2, 2, 2).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel28, -1, -1, 32767).addComponent(this.tNoCompteComptaCle, -2, 60, -2)).addGap(874, 874, 874).addComponent(this.jSeparator8).addContainerGap(-1, 32767)).addGroup(Alignment.TRAILING, pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.cITS).addGap(18, 18, 18).addComponent(this.cPlafone).addGap(30, 30, 30).addComponent(this.cCNSS).addGap(31, 31, 31).addComponent(this.cCNAM).addGap(29, 29, 29).addComponent(this.cAvantagesNature).addGap(18, 18, 18).addComponent(this.cCumulable).addContainerGap(1337, 32767)).addGroup(Alignment.TRAILING, pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.cBaseAuto, Alignment.LEADING, -1, -1, 32767).addComponent(this.cNombreAuto, Alignment.LEADING, -1, -1, 32767)).addGap(30, 30, 30).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.lFormuleBase, -1, 697, 32767).addComponent(this.lFormuleNombre, -1, -1, 32767)).addGap(18, 18, 18).addComponent(this.btnNew, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnDelete, -2, 35, -2).addContainerGap(-1, 32767)))));
      pnlMotifs_EditLayout.setVerticalGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGap(13, 13, 13).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel12).addGap(0, 0, 0).addComponent(this.tCode, -2, 30, -2)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel13).addGap(0, 0, 0).addComponent(this.tLibelle, -2, 30, -2)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGap(46, 46, 46).addComponent(this.jSeparator8, -2, 0, -2))).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator10, -1, 5, 32767).addComponent(this.jSeparator7, -2, 0, 32767)).addGap(38, 38, 38).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.cITS).addComponent(this.cCNSS).addComponent(this.cCNAM).addComponent(this.cPlafone).addComponent(this.cAvantagesNature).addComponent(this.cCumulable))).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGap(1, 1, 1).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel11).addGap(0, 0, 0).addComponent(this.tSens, -2, 30, -2)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel14).addGap(0, 0, 0).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel15, -2, 29, -2).addComponent(this.tDeductionDu, -2, 30, -2))).addGroup(Alignment.TRAILING, pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel16).addGap(0, 0, 0).addComponent(this.tChapitreCompta, -2, 30, -2).addGap(1, 1, 1))).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel28).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel26).addGap(0, 0, 0).addComponent(this.tNoCompteCompta, -2, 30, -2))).addGap(1, 1, 1))).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.tNoCompteComptaCle, -2, 30, -2))))).addGap(16, 16, 16).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.cBaseAuto).addComponent(this.lFormuleBase, -2, 25, -2)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.cNombreAuto).addComponent(this.lFormuleNombre, -2, 25, -2))).addComponent(this.btnDelete, -2, 35, -2).addComponent(this.btnSave, -2, 35, -2).addComponent(this.btnNew, -2, 35, -2)).addGap(10, 10, 10)));
      this.TabbedPane.addTab("Rubrique", this.pnlMotifs_Edit);
      this.jPanel2.setBackground(new Color(255, 255, 255));
      this.tPartieFormule.setFont(new Font("Segoe UI Light", 0, 12));
      this.tPartieFormule.setModel(new DefaultComboBoxModel(new String[]{"B", "N"}));
      this.jLabel18.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel18.setForeground(new Color(0, 102, 153));
      this.jLabel18.setText("Partie");
      this.tFormuleNombre.setEditable(false);
      this.tFormuleNombre.setBackground(new Color(235, 234, 234));
      this.tFormuleNombre.setBorder((Border)null);
      this.tFormuleNombre.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tFormuleNombre.setFont(new Font("Segoe UI Light", 0, 16));
      this.tFormuleNombre.addCaretListener(new 16(this));
      this.tFormuleBase.setEditable(false);
      this.tFormuleBase.setBackground(new Color(235, 234, 234));
      this.tFormuleBase.setBorder((Border)null);
      this.tFormuleBase.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tFormuleBase.setFont(new Font("Segoe UI Light", 0, 16));
      this.tFormuleBase.addCaretListener(new 17(this));
      this.jLabel19.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel19.setForeground(new Color(0, 102, 153));
      this.jLabel19.setText("Base");
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("Nombre");
      this.viderFBButton.setBackground(new Color(255, 255, 255));
      this.viderFBButton.setToolTipText("Vider la fomule");
      this.viderFBButton.setContentAreaFilled(false);
      this.viderFBButton.setCursor(new Cursor(12));
      this.viderFBButton.setOpaque(true);
      this.viderFBButton.addActionListener(new 18(this));
      this.viderFNButton.setBackground(new Color(255, 255, 255));
      this.viderFNButton.setToolTipText("Vider la fomule");
      this.viderFNButton.setContentAreaFilled(false);
      this.viderFNButton.setCursor(new Cursor(12));
      this.viderFNButton.setOpaque(true);
      this.viderFNButton.addActionListener(new 19(this));
      this.tFBRubriques.setFont(new Font("Segoe UI Light", 0, 12));
      this.tFBRubriques.setModel(new DefaultComboBoxModel(new String[]{"B", "N"}));
      this.jLabel21.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel21.setForeground(new Color(0, 102, 153));
      this.jLabel21.setText("Rubrique");
      this.jLabel23.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel23.setForeground(new Color(0, 102, 153));
      this.jLabel23.setText("Op\u00e9rateur");
      this.tFBoperateurs.setFont(new Font("Segoe UI Light", 0, 12));
      this.tFBoperateurs.setModel(new DefaultComboBoxModel(new String[]{"(", ")", "+", "-", "/", "*"}));
      this.FBOperateurAddButton.setBackground(new Color(255, 255, 255));
      this.FBOperateurAddButton.setContentAreaFilled(false);
      this.FBOperateurAddButton.setCursor(new Cursor(12));
      this.FBOperateurAddButton.setOpaque(true);
      this.FBOperateurAddButton.addActionListener(new 20(this));
      this.tFRFonctions.setFont(new Font("Segoe UI Light", 0, 12));
      this.tFRFonctions.setModel(new DefaultComboBoxModel(new String[]{"F01: Nombre de jours travaill\u00e9s", "F02: Salaire de base journalier", "F03: Salaire de base horaire", "F04: Taux anciennet\u00e9", "F05: Cumul BI apr\u00e8s dernier d\u00e9part", "F06: Cumul BNI apr\u00e8s dernier d\u00e9part", "F07: Cumul retenues apr\u00e8s dernier d\u00e9part", "F08: Cumul brut de 12 derniers mois", "F09: Salaire brut mensuel fixe", "F10: SMIG", "F11: SMIG Horaire", "F12: Taux de licenciement", "F13: Taux de licenciement collectif", "F14: Taux de retraite", "F15: Indice", "F16: Taux de pr\u00e9avis", "F17: Cumul NJT apr\u00e8s le mois de cl\u00f4ture", "F18: Nombre de SMIG / Origines", "F19: Taux de pr\u00e9sence", "F20: Base indemnit\u00e9 de logement", "F21: Salaire net", "F22: Nombre d'enfants", "F23: Taux anciennet\u00e9 speciale", "F24: Augmentation de salaire fixe"}));
      this.jLabel24.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel24.setForeground(new Color(0, 102, 153));
      this.jLabel24.setText("Fonction");
      this.FBFonctionAddButton.setBackground(new Color(255, 255, 255));
      this.FBFonctionAddButton.setContentAreaFilled(false);
      this.FBFonctionAddButton.setCursor(new Cursor(12));
      this.FBFonctionAddButton.setOpaque(true);
      this.FBFonctionAddButton.addActionListener(new 21(this));
      this.FBRubriqueAddButton.setBackground(new Color(255, 255, 255));
      this.FBRubriqueAddButton.setContentAreaFilled(false);
      this.FBRubriqueAddButton.setCursor(new Cursor(12));
      this.FBRubriqueAddButton.setOpaque(true);
      this.FBRubriqueAddButton.addActionListener(new 22(this));
      this.tFNombre.setBorder((Border)null);
      this.tFNombre.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tFNombre.setFont(new Font("Segoe UI Light", 0, 16));
      this.tFNombre.addCaretListener(new 23(this));
      this.jLabel25.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel25.setForeground(new Color(0, 102, 153));
      this.jLabel25.setText("Constante");
      this.FBConstanteAddButton.setBackground(new Color(255, 255, 255));
      this.FBConstanteAddButton.setContentAreaFilled(false);
      this.FBConstanteAddButton.setCursor(new Cursor(12));
      this.FBConstanteAddButton.setOpaque(true);
      this.FBConstanteAddButton.addActionListener(new 24(this));
      this.bsFBButton.setBackground(new Color(255, 255, 255));
      this.bsFBButton.setToolTipText("Supprimer le dernier \u00e9l\u00e9ment");
      this.bsFBButton.setContentAreaFilled(false);
      this.bsFBButton.setCursor(new Cursor(12));
      this.bsFBButton.setOpaque(true);
      this.bsFBButton.addActionListener(new 25(this));
      this.bsFNButton.setBackground(new Color(255, 255, 255));
      this.bsFNButton.setToolTipText("Supprimer le dernier \u00e9l\u00e9ment");
      this.bsFNButton.setContentAreaFilled(false);
      this.bsFNButton.setCursor(new Cursor(12));
      this.bsFNButton.setOpaque(true);
      this.bsFNButton.addActionListener(new 26(this));
      GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
      this.jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel19, Alignment.LEADING, -1, -1, 32767).addComponent(this.jLabel20, -1, 74, 32767)).addGap(6, 6, 6)).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel18, Alignment.LEADING, -1, -1, 32767).addComponent(this.tPartieFormule, Alignment.LEADING, 0, 74, 32767)).addPreferredGap(ComponentPlacement.UNRELATED))).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tFormuleNombre, -1, 866, 32767).addComponent(this.tFormuleBase)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING, false).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.bsFBButton, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.viderFBButton, -2, 35, -2)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.bsFNButton, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.viderFNButton, -2, 35, -2)))).addGroup(jPanel2Layout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED, 33, 32767).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel23, -1, -1, 32767).addComponent(this.tFBoperateurs, -2, 74, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.FBOperateurAddButton, -2, 35, -2).addGap(28, 28, 28).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel24, -1, -1, 32767).addComponent(this.tFRFonctions, -2, 282, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.FBFonctionAddButton, -2, 35, -2).addGap(36, 36, 36).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel21, -1, -1, 32767).addComponent(this.tFBRubriques, -2, 264, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.FBRubriqueAddButton, -2, 35, -2).addGap(37, 37, 37).addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel25, Alignment.LEADING, -1, -1, 32767).addComponent(this.tFNombre, Alignment.LEADING).addComponent(this.jSeparator12, -2, 71, -2)))).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.FBConstanteAddButton, -2, 35, -2).addGap(23, 23, 23)));
      jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel25).addGap(0, 0, 0).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tFNombre, -2, 30, -2).addComponent(this.FBConstanteAddButton, -2, 30, -2))).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.tFBoperateurs, -2, 30, -2).addComponent(this.FBOperateurAddButton, -2, 30, -2).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel23).addGap(36, 36, 36))).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel21).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.tFBRubriques, -2, 30, -2).addComponent(this.FBRubriqueAddButton, -2, 30, -2))).addComponent(this.tFRFonctions, -2, 30, -2).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel24).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.FBFonctionAddButton, -2, 30, -2)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel18).addGap(6, 6, 6).addComponent(this.tPartieFormule, -2, 30, -2))).addGap(1, 1, 1)))).addGap(0, 0, 0).addComponent(this.jSeparator12, -2, -1, -2).addGap(4, 4, 4).addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING, false).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel19, -2, 30, -2).addGap(11, 11, 11).addComponent(this.jLabel20, -2, 30, -2)).addGroup(jPanel2Layout.createSequentialGroup().addGap(2, 2, 2).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tFormuleBase, -2, 30, -2).addComponent(this.viderFBButton, -2, 32, -2).addComponent(this.bsFBButton, -2, 32, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tFormuleNombre).addComponent(this.viderFNButton, -2, 0, 32767).addComponent(this.bsFNButton, Alignment.TRAILING, -2, 0, 32767)))).addContainerGap()));
      this.TabbedPane.addTab("Formule", this.jPanel2);
      this.jPanel3.setBackground(new Color(255, 255, 255));
      this.tPoste.setFont(new Font("Segoe UI Light", 0, 16));
      this.tPoste.setModel(new DefaultComboBoxModel(new String[]{"B", "N"}));
      this.jLabel22.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel22.setForeground(new Color(0, 102, 153));
      this.jLabel22.setText("Poste");
      this.tableRubModel.setFont(new Font("Segoe UI Light", 0, 11));
      this.tableRubModel.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.tableRubModel.setSelectionBackground(new Color(0, 102, 153));
      this.tableRubModel.setShowGrid(false);
      this.tableRubModel.addMouseListener(new 27(this));
      this.jScrollPane10.setViewportView(this.tableRubModel);
      this.jLabel27.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel27.setForeground(new Color(0, 102, 153));
      this.jLabel27.setText("Montant Mensuel");
      this.tMntMens.setBorder((Border)null);
      this.tMntMens.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tMntMens.setFont(new Font("Segoe UI Light", 0, 16));
      this.tMntMens.addCaretListener(new 28(this));
      this.btnAddModelMnt.setBackground(new Color(255, 255, 255));
      this.btnAddModelMnt.setContentAreaFilled(false);
      this.btnAddModelMnt.setCursor(new Cursor(12));
      this.btnAddModelMnt.setOpaque(true);
      this.btnAddModelMnt.addActionListener(new 29(this));
      this.btnDelModelMnt.setBackground(new Color(255, 255, 255));
      this.btnDelModelMnt.setContentAreaFilled(false);
      this.btnDelModelMnt.setCursor(new Cursor(12));
      this.btnDelModelMnt.setOpaque(true);
      this.btnDelModelMnt.addActionListener(new 30(this));
      GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
      this.jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPoste, 0, 311, 32767).addComponent(this.jLabel22, -1, -1, 32767)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel27, Alignment.LEADING, -1, -1, 32767).addComponent(this.tMntMens, Alignment.LEADING).addComponent(this.jSeparator14, -2, 120, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnAddModelMnt, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnDelModelMnt, -2, 35, -2).addGap(18, 18, 18).addComponent(this.jScrollPane10, -2, 484, -2).addGap(119, 119, 119)));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel22).addComponent(this.jLabel27)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.tMntMens).addGap(0, 0, 0).addComponent(this.jSeparator14, -2, -1, -2)).addGroup(jPanel3Layout.createSequentialGroup().addGap(12, 12, 12).addComponent(this.tPoste, -2, 30, -2))).addGap(138, 138, 138)).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jScrollPane10, -2, 195, -2).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.btnAddModelMnt, -2, 30, -2).addComponent(this.btnDelModelMnt, -2, 0, 32767))).addContainerGap(-1, 32767)))));
      this.TabbedPane.addTab("Model", this.jPanel3);
      GroupLayout pnlMotifsLayout = new GroupLayout(this.pnlMotifs);
      this.pnlMotifs.setLayout(pnlMotifsLayout);
      pnlMotifsLayout.setHorizontalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addComponent(this.pnlMotifs_List, -2, -1, -2).addGap(0, 0, 32767)).addGroup(pnlMotifsLayout.createSequentialGroup().addContainerGap().addComponent(this.TabbedPane, -2, 1110, -2).addContainerGap(-1, 32767)));
      pnlMotifsLayout.setVerticalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addComponent(this.TabbedPane, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlMotifs_List, -1, -1, 32767)));
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlMotifs, -2, 1125, -2).addComponent(this.progressBar, -2, 1133, -2)).addContainerGap(-1, 32767)));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.progressBar, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlMotifs, -2, -1, -2)));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jPanel1, -2, -1, -2).addComponent(this.pnlBody, -2, 1131, -2).addComponent(this.msgLabel, -2, 1134, -2)).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlBody, -2, -1, -2).addGap(0, 0, 0).addComponent(this.msgLabel, -2, 22, -2).addGap(0, 0, 0)));
      this.setSize(new Dimension(1133, 632));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void listTableMouseClicked(MouseEvent var1) {
      this.clearFields();
      this.findByID(((Number)this.listTable.getValueAt(this.listTable.getSelectedRow(), 0)).intValue());
   }

   private void tFormuleNombreCaretUpdate(CaretEvent var1) {
   }

   private void tFormuleBaseCaretUpdate(CaretEvent var1) {
   }

   private void addToFormule(String var1, Double var2, String var3) {
      Rubriqueformule o = new Rubriqueformule();
      if (type.equals("N")) {
         o.setValNum(valNum);
      } else {
         o.setValText(valText);
      }

      o.setRubrique(selectedOne);
      o.setType(type);
      o.setPartie(this.tPartieFormule.getSelectedItem().toString());
      this.menu.viewMessage(this.msgLabel, "", false);
      if (!this.menu.gl.insertOcurance(o)) {
         this.menu.viewMessage(this.msgLabel, "Err: Op\u00e9ration echou\u00e9e", true);
      } else {
         selectedOne.getRubriqueformules().add(o);
      }

   }

   private void viderFormule(String var1) {
      List<Rubriqueformule> dl = new ArrayList(selectedOne.getRubriqueformules());

      for(Rubriqueformule rs : (List)dl.stream().filter((var1x) -> var1x.getPartie().equalsIgnoreCase(partie)).sorted(Comparator.comparing(Rubriqueformule::getId)).collect(Collectors.toList())) {
         this.menu.gl.deleteOcurance(rs);
         selectedOne.getRubriqueformules().remove(rs);
      }

   }

   private void backspaceFormule(String var1) {
      List<Rubriqueformule> dl = new ArrayList(selectedOne.getRubriqueformules());
      List var4 = (List)dl.stream().filter((var1x) -> var1x.getPartie().equalsIgnoreCase(partie)).sorted(Comparator.comparing(Rubriqueformule::getId)).collect(Collectors.toList());
      Rubriqueformule rs = var4.size() > 0 ? (Rubriqueformule)var4.get(var4.size() - 1) : (Rubriqueformule)var4.get(0);
      this.menu.gl.deleteOcurance(rs);
      selectedOne.getRubriqueformules().remove(rs);
      this.afficherFormule(partie);
   }

   private void viderFBButtonActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         this.viderFormule("B");
         this.tFormuleBase.setText("");
      }

   }

   private void viderFNButtonActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         this.viderFormule("N");
         this.tFormuleNombre.setText("");
      }

   }

   private void FBOperateurAddButtonActionPerformed(ActionEvent var1) {
      this.addToFormule(this.tFBoperateurs.getSelectedItem().toString(), (Double)null, "O");
      this.afficherFormule("B");
      this.afficherFormule("N");
   }

   private void FBFonctionAddButtonActionPerformed(ActionEvent var1) {
      this.addToFormule(this.tFRFonctions.getSelectedItem().toString().substring(0, 3), (Double)null, "F");
      this.afficherFormule("B");
      this.afficherFormule("N");
   }

   private void FBRubriqueAddButtonActionPerformed(ActionEvent var1) {
      Rubrique r = (Rubrique)this.tFBRubriques.getSelectedItem();
      Integer i = r.getId();
      this.addToFormule(i.toString(), (Double)null, "R");
      this.afficherFormule("B");
      this.afficherFormule("N");
   }

   private void tFNombreCaretUpdate(CaretEvent var1) {
   }

   private void FBConstanteAddButtonActionPerformed(ActionEvent var1) {
      if (this.tFNombre.getValue() != null) {
         this.addToFormule((String)null, ((Number)this.tFNombre.getValue()).doubleValue(), "N");
         this.afficherFormule("B");
         this.afficherFormule("N");
      } else {
         this.menu.viewMessage(this.msgLabel, "Err: Nombre invalide", true);
         this.tFNombre.requestFocus();
      }

   }

   private void btnAddModelMntActionPerformed(ActionEvent var1) {
      if (this.tMntMens.getValue() != null) {
         Poste poste = (Poste)this.tPoste.getSelectedItem();
         Rubriquemodel rm = new Rubriquemodel();
         rm.setMontant(((Number)this.tMntMens.getValue()).doubleValue());
         rm.setPoste(poste);
         rm.setRubrique(selectedOne);
         if (this.menu.gl.insertOcurance(rm)) {
            this.menu.viewMessage(this.msgLabel, "Op\u00e9ration effecut\u00e9e avec succ\u00e9!", false);
            this.afficherRubModel();
         } else {
            this.menu.viewMessage(this.msgLabel, "Err: Op\u00e9ration echou\u00e9e!", true);
         }
      } else {
         this.menu.viewMessage(this.msgLabel, "Err: Montant invalide!", true);
      }

   }

   private void tMntMensCaretUpdate(CaretEvent var1) {
   }

   private void tableRubModelMouseClicked(MouseEvent var1) {
   }

   private void btnDelModelMntActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Demande de confirmation ", 0);
      if (rep == 0) {
         Rubriquemodel rs = this.menu.pc.rubModelByID(((Number)this.tableRubModel.getValueAt(this.tableRubModel.getSelectedRow(), 0)).longValue());
         if (this.menu.gl.deleteOcurance(rs)) {
            this.afficherRubModel();
         }
      }

   }

   private void bsFBButtonActionPerformed(ActionEvent var1) {
      this.backspaceFormule("B");
   }

   private void bsFNButtonActionPerformed(ActionEvent var1) {
      this.backspaceFormule("N");
   }

   private void tNoCompteComptaCleKeyPressed(KeyEvent var1) {
   }

   private void tLibelleKeyPressed(KeyEvent var1) {
   }

   private void tNoCompteComptaCaretUpdate(CaretEvent var1) {
   }

   private void cITSActionPerformed(ActionEvent var1) {
      this.cPlafone.setEnabled(!this.cITS.isSelected());
   }

   private void tChapitreComptaCaretUpdate(CaretEvent var1) {
   }

   private void tSensActionPerformed(ActionEvent var1) {
      this.tDeductionDu.setEnabled(this.tSens.getSelectedItem().equals("R"));
      this.cITS.setEnabled(this.tSens.getSelectedItem().equals("G"));
      this.cCNSS.setEnabled(this.tSens.getSelectedItem().equals("G"));
      this.cCNAM.setEnabled(this.tSens.getSelectedItem().equals("G"));
      this.cAvantagesNature.setEnabled(this.tSens.getSelectedItem().equals("G"));
      this.cPlafone.setEnabled(this.tSens.getSelectedItem().equals("G"));
   }

   private void tCodeCaretUpdate(CaretEvent var1) {
   }

   private void btnDeleteActionPerformed(ActionEvent var1) {
      if (selectedOne != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.msgLabel, "", false);
            if (this.menu.gl.deleteOcurance(selectedOne)) {
               this.menu.viewMessage(this.msgLabel, "Element supprim\u00e9", false);
               this.dataListUpdate();
               this.afficherListe();
               this.clearFields();
            }
         }
      } else {
         this.menu.viewMessage(this.msgLabel, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSaveActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      String msg = this.validateData();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.msgLabel, msg, true);
      } else {
         boolean add = false;
         if (selectedOne == null) {
            selectedOne = new Rubrique();
            add = true;
         }

         selectedOne.setId(new Integer(this.tCode.getText()));
         selectedOne.setAvantagesNature(this.cAvantagesNature.isSelected());
         selectedOne.setBaseAuto(this.cBaseAuto.isSelected());
         selectedOne.setCnam(this.cCNAM.isSelected());
         selectedOne.setCnss(this.cCNSS.isSelected());
         selectedOne.setCumulable(this.cCumulable.isSelected());
         selectedOne.setDeductionDu(this.tDeductionDu.getSelectedItem().toString());
         selectedOne.setIts(this.cITS.isSelected());
         selectedOne.setLibelle(this.tLibelle.getText());
         selectedOne.setNoCompteCompta(((Number)this.tNoCompteCompta.getValue()).longValue());
         selectedOne.setNoChapitreCompta(((Number)this.tChapitreCompta.getValue()).longValue());
         selectedOne.setNoCompteComptaCle(this.tNoCompteComptaCle.getText());
         selectedOne.setNombreAuto(this.cNombreAuto.isSelected());
         selectedOne.setPlafone(this.cPlafone.isSelected());
         selectedOne.setSens(this.tSens.getSelectedItem().toString());
         if (add) {
            if (this.menu.gl.insertOcurance(selectedOne)) {
               this.menu.viewMessage(this.msgLabel, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.msgLabel, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(selectedOne)) {
            this.menu.viewMessage(this.msgLabel, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.msgLabel, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate();
         this.afficherListe();
         this.clearFields();
      }

   }

   private void btnNewActionPerformed(ActionEvent var1) {
      this.clearFields();
      this.tCode.setValue((Integer)this.dataListInit.stream().map(Rubrique::getId).max(Integer::compareTo).orElse(1) + 1);
      this.TabbedPane.setSelectedIndex(0);
   }

   private void searchFieldCaretUpdate(CaretEvent var1) {
      Thread t = new 31(this);
      t.start();
   }

   private void searchFieldKeyPressed(KeyEvent var1) {
   }

   private void lbSearchMouseClicked(MouseEvent var1) {
   }
}
