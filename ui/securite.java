package com.mccmr.ui;

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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

public class securite extends JInternalFrame {
   public menu menu;
   public Utilisateurs selectedOne = null;
   public List<Utilisateurs> dataList;
   public List<Utilisateurs> dataListInit;
   private JButton btnDelete;
   private JButton btnExit;
   private JButton btnNew;
   private JButton btnRefresh;
   private JButton btnSave;
   private JCheckBox cAjout;
   private JCheckBox cCloture;
   private JCheckBox cDashboard;
   private JCheckBox cGrilleLog;
   private JCheckBox cGrilleSB;
   private JCheckBox cMaj;
   private JCheckBox cMotifPaie;
   private JCheckBox cOrigineSal;
   private JCheckBox cParametre;
   private JCheckBox cRubrique;
   private JCheckBox cSalUpdate;
   private JCheckBox cSal_Add;
   private JCheckBox cSal_Conges;
   private JCheckBox cSal_Contrat;
   private JCheckBox cSal_Diplome;
   private JCheckBox cSal_Doc;
   private JCheckBox cSal_HS;
   private JCheckBox cSal_Identite;
   private JCheckBox cSal_Paie;
   private JCheckBox cSal_RetAE;
   private JCheckBox cSecurite;
   private JCheckBox cSuppSalarie;
   private JCheckBox cSuppression;
   private JTabbedPane detailPanel1;
   private JLabel jLabel14;
   private JLabel jLabel15;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JPanel jPanel14;
   private JPanel jPanel2;
   private JPanel jPanel29;
   private JScrollPane jScrollPane9;
   private JSeparator jSeparator1;
   private JSeparator jSeparator11;
   private JSeparator jSeparator21;
   private JTabbedPane jTabbedPane1;
   private JLabel lbNbSalaries;
   private JLabel lbSearch;
   private JTable listTable;
   private JLabel msgLabel;
   private JPanel pnlActivites_Btn;
   private JPanel pnlBody;
   private JPanel pnlMotifs;
   private JPanel pnlMotifs1;
   private JProgressBar progressBar;
   private JLabel salarieLabel;
   private JTextField searchField;
   private JTextField tLogin;
   private JTextField tLogo;
   private JTextField tLogoDestFile;
   private JTextField tNom;

   public securite() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.lbSearch.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SEARCH, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnRefresh.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.REFRESH, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelete.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnNew.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.clearFields();
      this.jTabbedPane1.setSelectedIndex(0);
      this.dataListUpdate();
      this.afficherListe();
      this.menu.firstRefresh = true;
   }

   public void dataListUpdate() {
      Query q = this.menu.entityManager.createQuery("Select p from Utilisateurs p");
      q.setMaxResults(1000000);
      this.dataListInit = q.getResultList();
   }

   public void RolesAction(Utilisateurs var1) {
      this.btnNew.setVisible(role.isAjout());
      this.btnDelete.setVisible(role.isSuppression());
      this.btnSave.setVisible(role.isMaj() || role.isAjout());
   }

   public void afficherListe() {
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmUsers(var10003));
      this.listTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
      this.dataList = (List)this.dataListInit.stream().filter((var0) -> !var0.getLogin().contains("root")).collect(Collectors.toList());
      if (!this.searchField.getText().isEmpty()) {
         this.dataList = (List)this.dataList.stream().filter((var1) -> var1.getLogin().equalsIgnoreCase(this.searchField.getText()) || var1.getNomusager().contains(this.searchField.getText().toLowerCase())).collect(Collectors.toList());
      }

      this.dataList = (List)this.dataList.stream().sorted(Comparator.comparing((var0) -> var0.getLogin())).collect(Collectors.toList());

      for(Utilisateurs rs : this.dataList) {
         ((ModelClass.tmUsers)this.listTable.getModel()).addRow(rs);
      }

      this.listTable.getColumnModel().getColumn(0).setPreferredWidth(50);
      this.listTable.getColumnModel().getColumn(1).setPreferredWidth(300);
      this.listTable.setRowHeight(30);
      this.listTable.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable.getModel());
      this.listTable.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      this.lbNbSalaries.setText((new Integer(((List)this.dataListInit.stream().collect(Collectors.toList())).size())).toString());
   }

   private void clearFields() {
      this.menu.viewMessage(this.msgLabel, "", false);
      this.btnSave.setEnabled(true);
      this.btnDelete.setEnabled(false);
      this.selectedOne = null;
      this.salarieLabel.setText("");
      this.tNom.setText("");
      this.tLogin.setText("");
      this.tLogin.setEnabled(true);
      this.tLogin.setText("");
      this.tNom.setText("-");
      this.cAjout.setSelected(false);
      this.cMaj.setSelected(false);
      this.cCloture.setSelected(false);
      this.cGrilleLog.setSelected(false);
      this.cMotifPaie.setSelected(false);
      this.cOrigineSal.setSelected(false);
      this.cParametre.setSelected(false);
      this.cRubrique.setSelected(false);
      this.cSecurite.setSelected(false);
      this.cSuppSalarie.setSelected(false);
      this.cSuppression.setSelected(false);
      this.cGrilleSB.setSelected(false);
   }

   private String validateData_Salary() {
      String errMsg = "";
      if (this.tNom.getText().isEmpty()) {
         errMsg = "Nom de l'usager obligatoire";
         this.tNom.requestFocus();
      }

      if (this.tLogin.getText().isEmpty()) {
         errMsg = "Login obligatoire";
         this.tLogin.requestFocus();
      }

      return errMsg;
   }

   private void showMsgDialog(Object[] var1) {
      JOptionPane.showMessageDialog(this, tab, " Infos paie courante ", 1);
   }

   private void findByID(String var1) {
      Thread t = new 1(this, id);
      t.start();
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.progressBar = new JProgressBar();
      this.jTabbedPane1 = new JTabbedPane();
      this.pnlMotifs = new JPanel();
      this.jScrollPane9 = new JScrollPane();
      this.listTable = new JTable();
      this.jSeparator1 = new JSeparator();
      this.searchField = new JTextField();
      this.lbSearch = new JLabel();
      this.lbNbSalaries = new JLabel();
      this.btnRefresh = new JButton();
      this.pnlMotifs1 = new JPanel();
      this.pnlActivites_Btn = new JPanel();
      this.btnDelete = new JButton();
      this.btnSave = new JButton();
      this.btnNew = new JButton();
      this.tLogo = new JTextField();
      this.tLogoDestFile = new JTextField();
      this.salarieLabel = new JLabel();
      this.jPanel2 = new JPanel();
      this.jLabel14 = new JLabel();
      this.tLogin = new JTextField();
      this.jSeparator11 = new JSeparator();
      this.jLabel15 = new JLabel();
      this.tNom = new JTextField();
      this.jSeparator21 = new JSeparator();
      this.detailPanel1 = new JTabbedPane();
      this.jPanel14 = new JPanel();
      this.cAjout = new JCheckBox();
      this.cMaj = new JCheckBox();
      this.cSuppression = new JCheckBox();
      this.cRubrique = new JCheckBox();
      this.cGrilleSB = new JCheckBox();
      this.cGrilleLog = new JCheckBox();
      this.cOrigineSal = new JCheckBox();
      this.cMotifPaie = new JCheckBox();
      this.cCloture = new JCheckBox();
      this.cParametre = new JCheckBox();
      this.cSecurite = new JCheckBox();
      this.cDashboard = new JCheckBox();
      this.jPanel29 = new JPanel();
      this.cSal_Paie = new JCheckBox();
      this.cSalUpdate = new JCheckBox();
      this.cSuppSalarie = new JCheckBox();
      this.cSal_Add = new JCheckBox();
      this.cSal_HS = new JCheckBox();
      this.cSal_Conges = new JCheckBox();
      this.cSal_RetAE = new JCheckBox();
      this.cSal_Contrat = new JCheckBox();
      this.cSal_Diplome = new JCheckBox();
      this.cSal_Identite = new JCheckBox();
      this.cSal_Doc = new JCheckBox();
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
      this.jLabel7.setText("Securit\u00e9");
      this.jLabel7.setToolTipText("");
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 2(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnExit, -2, 39, -2).addContainerGap()));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -2, 37, -2));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      this.jTabbedPane1.setForeground(new Color(0, 102, 153));
      this.jTabbedPane1.setFont(new Font("Segoe UI Light", 1, 12));
      this.pnlMotifs.setBackground(new Color(255, 255, 255));
      this.listTable.setFont(new Font("Segoe UI Light", 0, 12));
      this.listTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable.setSelectionBackground(new Color(0, 102, 153));
      this.listTable.setShowGrid(false);
      this.listTable.addMouseListener(new 3(this));
      this.jScrollPane9.setViewportView(this.listTable);
      this.searchField.setFont(new Font("Segoe UI Light", 1, 12));
      this.searchField.setBorder((Border)null);
      this.searchField.addCaretListener(new 4(this));
      this.searchField.addKeyListener(new 5(this));
      this.lbSearch.setFont(new Font("Segoe UI Light", 0, 12));
      this.lbSearch.setCursor(new Cursor(12));
      this.lbSearch.addMouseListener(new 6(this));
      this.lbNbSalaries.setBackground(new Color(196, 198, 194));
      this.lbNbSalaries.setFont(new Font("Segoe UI Light", 1, 12));
      this.lbNbSalaries.setHorizontalAlignment(0);
      this.lbNbSalaries.setText("0");
      this.lbNbSalaries.setToolTipText("Total salari\u00e9s");
      this.lbNbSalaries.setOpaque(true);
      this.btnRefresh.setBackground(new Color(255, 255, 255));
      this.btnRefresh.setToolTipText("Actualiser");
      this.btnRefresh.setContentAreaFilled(false);
      this.btnRefresh.setOpaque(true);
      this.btnRefresh.addActionListener(new 7(this));
      GroupLayout pnlMotifsLayout = new GroupLayout(this.pnlMotifs);
      this.pnlMotifs.setLayout(pnlMotifsLayout);
      pnlMotifsLayout.setHorizontalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addContainerGap().addGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jScrollPane9).addGroup(pnlMotifsLayout.createSequentialGroup().addComponent(this.lbSearch, -2, 31, -2).addGap(0, 0, 0).addGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addComponent(this.searchField, -2, 310, -2).addComponent(this.jSeparator1, -2, 300, -2)).addGap(159, 159, 159).addComponent(this.lbNbSalaries, -2, 49, -2).addPreferredGap(ComponentPlacement.RELATED, 346, 32767).addComponent(this.btnRefresh, -2, 35, -2))).addContainerGap()));
      pnlMotifsLayout.setVerticalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addContainerGap().addGroup(pnlMotifsLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlMotifsLayout.createSequentialGroup().addGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.lbSearch, -1, -1, 32767).addComponent(this.searchField, -2, 30, -2)).addGap(0, 0, 0).addComponent(this.jSeparator1, -2, -1, -2)).addGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnRefresh, -2, 35, -2).addComponent(this.lbNbSalaries, -2, 25, -2))).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane9, -2, 318, -2).addContainerGap(-1, 32767)));
      this.jTabbedPane1.addTab("Liste", this.pnlMotifs);
      this.pnlMotifs1.setBackground(new Color(255, 255, 255));
      this.pnlActivites_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete.setBackground(new Color(255, 255, 255));
      this.btnDelete.setToolTipText("Supprimer");
      this.btnDelete.setContentAreaFilled(false);
      this.btnDelete.setCursor(new Cursor(12));
      this.btnDelete.setOpaque(true);
      this.btnDelete.addActionListener(new 8(this));
      this.btnSave.setBackground(new Color(255, 255, 255));
      this.btnSave.setToolTipText("Sauvegarder");
      this.btnSave.setContentAreaFilled(false);
      this.btnSave.setCursor(new Cursor(12));
      this.btnSave.setOpaque(true);
      this.btnSave.addActionListener(new 9(this));
      this.btnNew.setBackground(new Color(255, 255, 255));
      this.btnNew.setToolTipText("Nouveau");
      this.btnNew.setContentAreaFilled(false);
      this.btnNew.setCursor(new Cursor(12));
      this.btnNew.setOpaque(true);
      this.btnNew.addActionListener(new 10(this));
      this.tLogo.setEditable(false);
      this.tLogo.setFont(new Font("Tahoma", 0, 5));
      this.tLogo.setHorizontalAlignment(0);
      this.tLogo.setBorder((Border)null);
      this.tLogoDestFile.setEditable(false);
      this.tLogoDestFile.setFont(new Font("Tahoma", 0, 5));
      this.tLogoDestFile.setHorizontalAlignment(0);
      this.tLogoDestFile.setBorder((Border)null);
      GroupLayout pnlActivites_BtnLayout = new GroupLayout(this.pnlActivites_Btn);
      this.pnlActivites_Btn.setLayout(pnlActivites_BtnLayout);
      pnlActivites_BtnLayout.setHorizontalGroup(pnlActivites_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlActivites_BtnLayout.createSequentialGroup().addGap(378, 378, 378).addComponent(this.tLogoDestFile, -2, 116, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnNew, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete, -2, 35, -2).addGap(121, 121, 121)).addGroup(pnlActivites_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivites_BtnLayout.createSequentialGroup().addGap(377, 377, 377).addComponent(this.tLogo, -2, 116, -2).addContainerGap(612, 32767))));
      pnlActivites_BtnLayout.setVerticalGroup(pnlActivites_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivites_BtnLayout.createSequentialGroup().addGroup(pnlActivites_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete, -2, 35, -2).addComponent(this.btnSave, -2, 35, -2).addComponent(this.btnNew, -2, 35, -2).addGroup(pnlActivites_BtnLayout.createSequentialGroup().addContainerGap().addComponent(this.tLogoDestFile, -2, 8, -2))).addContainerGap()).addGroup(pnlActivites_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivites_BtnLayout.createSequentialGroup().addGap(20, 20, 20).addComponent(this.tLogo, -2, 8, -2).addContainerGap(-1, 32767))));
      this.salarieLabel.setBackground(new Color(255, 255, 255));
      this.salarieLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.salarieLabel.setText(".");
      this.salarieLabel.setOpaque(true);
      this.jPanel2.setBackground(new Color(255, 255, 255));
      this.jPanel2.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel14.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel14.setForeground(new Color(0, 102, 153));
      this.jLabel14.setText("Login");
      this.tLogin.setFont(new Font("Segoe UI Light", 1, 12));
      this.tLogin.setBorder((Border)null);
      this.tLogin.addKeyListener(new 11(this));
      this.jLabel15.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel15.setForeground(new Color(0, 102, 153));
      this.jLabel15.setText("Pr\u00e9nom et nom");
      this.tNom.setFont(new Font("Segoe UI Light", 1, 12));
      this.tNom.setBorder((Border)null);
      this.tNom.addKeyListener(new 12(this));
      this.detailPanel1.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      this.detailPanel1.setForeground(new Color(0, 102, 153));
      this.detailPanel1.setFont(new Font("Segoe UI Light", 1, 11));
      this.jPanel14.setBackground(new Color(255, 255, 255));
      this.jPanel14.setFont(new Font("Segoe UI Light", 0, 12));
      this.cAjout.setBackground(new Color(255, 255, 255));
      this.cAjout.setFont(new Font("Segoe UI Light", 0, 12));
      this.cAjout.setForeground(new Color(0, 102, 153));
      this.cAjout.setText("Ajout");
      this.cAjout.addActionListener(new 13(this));
      this.cMaj.setBackground(new Color(255, 255, 255));
      this.cMaj.setFont(new Font("Segoe UI Light", 0, 12));
      this.cMaj.setForeground(new Color(0, 102, 153));
      this.cMaj.setText("Modification");
      this.cMaj.addActionListener(new 14(this));
      this.cSuppression.setBackground(new Color(255, 255, 255));
      this.cSuppression.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSuppression.setForeground(new Color(0, 102, 153));
      this.cSuppression.setText("Suppression");
      this.cSuppression.addActionListener(new 15(this));
      this.cRubrique.setBackground(new Color(255, 255, 255));
      this.cRubrique.setFont(new Font("Segoe UI Light", 0, 12));
      this.cRubrique.setForeground(new Color(0, 102, 153));
      this.cRubrique.setText("Rubrique de paie");
      this.cRubrique.addActionListener(new 16(this));
      this.cGrilleSB.setBackground(new Color(255, 255, 255));
      this.cGrilleSB.setFont(new Font("Segoe UI Light", 0, 12));
      this.cGrilleSB.setForeground(new Color(0, 102, 153));
      this.cGrilleSB.setText("Grille salaire de base");
      this.cGrilleSB.addActionListener(new 17(this));
      this.cGrilleLog.setBackground(new Color(255, 255, 255));
      this.cGrilleLog.setFont(new Font("Segoe UI Light", 0, 12));
      this.cGrilleLog.setForeground(new Color(0, 102, 153));
      this.cGrilleLog.setText("Grille ind. logement");
      this.cGrilleLog.addActionListener(new 18(this));
      this.cOrigineSal.setBackground(new Color(255, 255, 255));
      this.cOrigineSal.setFont(new Font("Segoe UI Light", 0, 12));
      this.cOrigineSal.setForeground(new Color(0, 102, 153));
      this.cOrigineSal.setText("Origine de salari\u00e9s");
      this.cOrigineSal.addActionListener(new 19(this));
      this.cMotifPaie.setBackground(new Color(255, 255, 255));
      this.cMotifPaie.setFont(new Font("Segoe UI Light", 0, 12));
      this.cMotifPaie.setForeground(new Color(0, 102, 153));
      this.cMotifPaie.setText("Motif de paie");
      this.cMotifPaie.addActionListener(new 20(this));
      this.cCloture.setBackground(new Color(255, 255, 255));
      this.cCloture.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCloture.setForeground(new Color(0, 102, 153));
      this.cCloture.setText("Cl\u00f4ture de paie");
      this.cCloture.addActionListener(new 21(this));
      this.cParametre.setBackground(new Color(255, 255, 255));
      this.cParametre.setFont(new Font("Segoe UI Light", 0, 12));
      this.cParametre.setForeground(new Color(0, 102, 153));
      this.cParametre.setText("Param\u00e8tres");
      this.cParametre.addActionListener(new 22(this));
      this.cSecurite.setBackground(new Color(255, 255, 255));
      this.cSecurite.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSecurite.setForeground(new Color(0, 102, 153));
      this.cSecurite.setText("Securit\u00e9");
      this.cSecurite.addActionListener(new 23(this));
      this.cDashboard.setBackground(new Color(255, 255, 255));
      this.cDashboard.setFont(new Font("Segoe UI Light", 0, 12));
      this.cDashboard.setForeground(new Color(0, 102, 153));
      this.cDashboard.setText("Dashboard");
      this.cDashboard.addActionListener(new 24(this));
      GroupLayout jPanel14Layout = new GroupLayout(this.jPanel14);
      this.jPanel14.setLayout(jPanel14Layout);
      jPanel14Layout.setHorizontalGroup(jPanel14Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel14Layout.createSequentialGroup().addContainerGap().addGroup(jPanel14Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cAjout, -2, 130, -2).addComponent(this.cMaj, -2, 130, -2).addComponent(this.cSuppression, -2, 130, -2)).addGap(18, 18, 18).addGroup(jPanel14Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.cRubrique, -2, 150, -2).addComponent(this.cGrilleSB, -1, -1, 32767).addComponent(this.cGrilleLog, -1, -1, 32767)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel14Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cMotifPaie, -2, 130, -2).addComponent(this.cCloture, -2, 130, -2).addComponent(this.cOrigineSal)).addGap(45, 45, 45).addGroup(jPanel14Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cParametre, -2, 130, -2).addComponent(this.cSecurite, -2, 130, -2).addComponent(this.cDashboard, -2, 130, -2)).addContainerGap(228, 32767)));
      jPanel14Layout.setVerticalGroup(jPanel14Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel14Layout.createSequentialGroup().addGap(26, 26, 26).addGroup(jPanel14Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel14Layout.createSequentialGroup().addComponent(this.cParametre, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cSecurite, -2, 33, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cDashboard, -2, 33, -2)).addGroup(jPanel14Layout.createSequentialGroup().addComponent(this.cOrigineSal, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cMotifPaie, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cCloture, -2, 33, -2)).addGroup(jPanel14Layout.createSequentialGroup().addComponent(this.cRubrique, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cGrilleSB, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cGrilleLog, -2, 33, -2)).addGroup(jPanel14Layout.createSequentialGroup().addComponent(this.cAjout, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cMaj, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cSuppression, -2, 33, -2))).addGap(90, 90, 90)));
      this.detailPanel1.addTab("G\u00e9n\u00e9ral", this.jPanel14);
      this.jPanel29.setBackground(new Color(255, 255, 255));
      this.jPanel29.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSal_Paie.setBackground(new Color(255, 255, 255));
      this.cSal_Paie.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSal_Paie.setForeground(new Color(0, 102, 153));
      this.cSal_Paie.setText("Paie");
      this.cSal_Paie.addActionListener(new 25(this));
      this.cSalUpdate.setBackground(new Color(255, 255, 255));
      this.cSalUpdate.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSalUpdate.setForeground(new Color(0, 102, 153));
      this.cSalUpdate.setText("Modification de salari\u00e9");
      this.cSalUpdate.addActionListener(new 26(this));
      this.cSuppSalarie.setBackground(new Color(255, 255, 255));
      this.cSuppSalarie.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSuppSalarie.setForeground(new Color(0, 102, 153));
      this.cSuppSalarie.setText("Suppression de salari\u00e9");
      this.cSuppSalarie.addActionListener(new 27(this));
      this.cSal_Add.setBackground(new Color(255, 255, 255));
      this.cSal_Add.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSal_Add.setForeground(new Color(0, 102, 153));
      this.cSal_Add.setText("Ajout de salari\u00e9");
      this.cSal_Add.addActionListener(new 28(this));
      this.cSal_HS.setBackground(new Color(255, 255, 255));
      this.cSal_HS.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSal_HS.setForeground(new Color(0, 102, 153));
      this.cSal_HS.setText("Heures supp.");
      this.cSal_HS.addActionListener(new 29(this));
      this.cSal_Conges.setBackground(new Color(255, 255, 255));
      this.cSal_Conges.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSal_Conges.setForeground(new Color(0, 102, 153));
      this.cSal_Conges.setText("Cong\u00e9s");
      this.cSal_Conges.addActionListener(new 30(this));
      this.cSal_RetAE.setBackground(new Color(255, 255, 255));
      this.cSal_RetAE.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSal_RetAE.setForeground(new Color(0, 102, 153));
      this.cSal_RetAE.setText("Retenues \u00e0 ech.");
      this.cSal_RetAE.addActionListener(new 31(this));
      this.cSal_Contrat.setBackground(new Color(255, 255, 255));
      this.cSal_Contrat.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSal_Contrat.setForeground(new Color(0, 102, 153));
      this.cSal_Contrat.setText("Contrat");
      this.cSal_Contrat.addActionListener(new 32(this));
      this.cSal_Diplome.setBackground(new Color(255, 255, 255));
      this.cSal_Diplome.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSal_Diplome.setForeground(new Color(0, 102, 153));
      this.cSal_Diplome.setText("Dipl\u00f4mes");
      this.cSal_Diplome.addActionListener(new 33(this));
      this.cSal_Identite.setBackground(new Color(255, 255, 255));
      this.cSal_Identite.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSal_Identite.setForeground(new Color(0, 102, 153));
      this.cSal_Identite.setText("Identit\u00e9");
      this.cSal_Identite.addActionListener(new 34(this));
      this.cSal_Doc.setBackground(new Color(255, 255, 255));
      this.cSal_Doc.setFont(new Font("Segoe UI Light", 0, 12));
      this.cSal_Doc.setForeground(new Color(0, 102, 153));
      this.cSal_Doc.setText("Documents");
      this.cSal_Doc.addActionListener(new 35(this));
      GroupLayout jPanel29Layout = new GroupLayout(this.jPanel29);
      this.jPanel29.setLayout(jPanel29Layout);
      jPanel29Layout.setHorizontalGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel29Layout.createSequentialGroup().addContainerGap().addGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cSal_Identite, -2, 130, -2).addComponent(this.cSal_Diplome, -2, 130, -2).addComponent(this.cSal_Contrat, -2, 130, -2)).addGap(18, 18, 18).addGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cSal_HS, -2, 130, -2).addComponent(this.cSal_Conges, -2, 130, -2).addComponent(this.cSal_RetAE, -2, 130, -2)).addGap(32, 32, 32).addGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.cSalUpdate, Alignment.TRAILING, -1, -1, 32767).addComponent(this.cSuppSalarie, Alignment.TRAILING, -1, -1, 32767).addComponent(this.cSal_Add, -1, -1, 32767)).addGap(45, 45, 45).addGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addComponent(this.cSal_Paie, -2, 130, -2).addComponent(this.cSal_Doc, -2, 130, -2)).addContainerGap(210, 32767)));
      jPanel29Layout.setVerticalGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel29Layout.createSequentialGroup().addGap(26, 26, 26).addGroup(jPanel29Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel29Layout.createSequentialGroup().addGroup(jPanel29Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.cSal_Add, -2, 33, -2).addComponent(this.cSal_Doc, -2, 33, -2)).addGap(18, 18, 18).addGroup(jPanel29Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.cSalUpdate, -2, 33, -2).addComponent(this.cSal_Paie, -2, 33, -2)).addGap(18, 18, 18).addComponent(this.cSuppSalarie, -2, 33, -2)).addGroup(jPanel29Layout.createSequentialGroup().addComponent(this.cSal_HS, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cSal_Conges, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cSal_RetAE, -2, 33, -2)).addGroup(jPanel29Layout.createSequentialGroup().addComponent(this.cSal_Identite, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cSal_Diplome, -2, 33, -2).addGap(18, 18, 18).addComponent(this.cSal_Contrat, -2, 33, -2))).addGap(90, 90, 90)));
      this.detailPanel1.addTab("Salari\u00e9s", this.jPanel29);
      GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
      this.jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.detailPanel1, -2, 887, -2).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator11, -2, 150, -2).addComponent(this.tLogin, -2, 150, -2).addComponent(this.jLabel14, -2, 150, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator21, -2, 150, -2).addComponent(this.tNom, -2, 150, -2).addComponent(this.jLabel15, -2, 150, -2)))).addContainerGap(33, 32767)));
      jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel14).addGap(0, 0, 0).addComponent(this.tLogin, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator11, -2, 2, -2)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel15).addGap(0, 0, 0).addComponent(this.tNom, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator21, -2, 2, -2))).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.detailPanel1, -2, 213, -2).addContainerGap(-1, 32767)));
      GroupLayout pnlMotifs1Layout = new GroupLayout(this.pnlMotifs1);
      this.pnlMotifs1.setLayout(pnlMotifs1Layout);
      pnlMotifs1Layout.setHorizontalGroup(pnlMotifs1Layout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs1Layout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlActivites_Btn, Alignment.TRAILING, -1, -1, 32767).addComponent(this.salarieLabel, -2, 765, -2).addComponent(this.jPanel2, -2, -1, -2)).addContainerGap()));
      pnlMotifs1Layout.setVerticalGroup(pnlMotifs1Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlMotifs1Layout.createSequentialGroup().addComponent(this.salarieLabel, -2, 31, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jPanel2, -2, -1, -2).addGap(2, 2, 2).addComponent(this.pnlActivites_Btn, -2, -1, -2)));
      this.jTabbedPane1.addTab("D\u00e9tail", this.pnlMotifs1);
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.msgLabel, -2, 820, -2).addGap(0, 143, 32767)).addComponent(this.jTabbedPane1, Alignment.TRAILING, -2, 0, 32767).addComponent(this.progressBar, -1, -1, 32767)).addGap(0, 0, 0)));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.progressBar, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jTabbedPane1, -2, -1, -2).addGap(8, 8, 8).addComponent(this.msgLabel, -2, 25, -2).addContainerGap(-1, 32767)));
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlBody, -1, -1, 32767).addComponent(this.jPanel1, -1, -1, 32767));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -2, 459, -2).addContainerGap(39, 32767)));
      this.setSize(new Dimension(963, 535));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void listTableMouseClicked(MouseEvent var1) {
      String selectedID = this.listTable.getValueAt(this.listTable.getSelectedRow(), 0).toString();
      this.clearFields();
      this.findByID(selectedID);
   }

   private void searchFieldKeyPressed(KeyEvent var1) {
   }

   private void lbSearchMouseClicked(MouseEvent var1) {
   }

   private void btnDeleteActionPerformed(ActionEvent var1) {
      if (this.selectedOne != null) {
         int rep = JOptionPane.showConfirmDialog(this, " ATTETION! Confirmez vous la suppression ?", " Demande de confirmation ", 0);
         if (rep == 0) {
            Thread t = new 36(this);
            t.start();
         }
      } else {
         this.menu.viewMessage(this.msgLabel, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSaveActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      String msg = this.validateData_Salary();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.msgLabel, msg, true);
      } else {
         Thread t = new 37(this);
         t.start();
      }

   }

   private void btnNewActionPerformed(ActionEvent var1) {
      this.clearFields();
   }

   private void searchFieldCaretUpdate(CaretEvent var1) {
      Thread t = new 38(this);
      t.start();
   }

   private void btnRefreshActionPerformed(ActionEvent var1) {
      Thread t = new 39(this);
      t.start();
   }

   private void tLoginKeyPressed(KeyEvent var1) {
   }

   private void tNomKeyPressed(KeyEvent var1) {
   }

   private void cAjoutActionPerformed(ActionEvent var1) {
   }

   private void cMajActionPerformed(ActionEvent var1) {
   }

   private void cSuppressionActionPerformed(ActionEvent var1) {
   }

   private void cRubriqueActionPerformed(ActionEvent var1) {
   }

   private void cGrilleSBActionPerformed(ActionEvent var1) {
   }

   private void cGrilleLogActionPerformed(ActionEvent var1) {
   }

   private void cOrigineSalActionPerformed(ActionEvent var1) {
   }

   private void cMotifPaieActionPerformed(ActionEvent var1) {
   }

   private void cClotureActionPerformed(ActionEvent var1) {
   }

   private void cParametreActionPerformed(ActionEvent var1) {
   }

   private void cSecuriteActionPerformed(ActionEvent var1) {
   }

   private void cSal_PaieActionPerformed(ActionEvent var1) {
   }

   private void cSalUpdateActionPerformed(ActionEvent var1) {
   }

   private void cSuppSalarieActionPerformed(ActionEvent var1) {
   }

   private void cSal_AddActionPerformed(ActionEvent var1) {
   }

   private void cSal_HSActionPerformed(ActionEvent var1) {
   }

   private void cSal_CongesActionPerformed(ActionEvent var1) {
   }

   private void cSal_RetAEActionPerformed(ActionEvent var1) {
   }

   private void cSal_ContratActionPerformed(ActionEvent var1) {
   }

   private void cSal_DiplomeActionPerformed(ActionEvent var1) {
   }

   private void cSal_IdentiteActionPerformed(ActionEvent var1) {
   }

   private void cSal_DocActionPerformed(ActionEvent var1) {
   }

   private void cDashboardActionPerformed(ActionEvent var1) {
   }
}
