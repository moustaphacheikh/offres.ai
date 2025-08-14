package com.mccmr.ui;

import com.mccmr.entity.Listenominativecnam;
import com.mccmr.entity.Listenominativecnss;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.List;
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
import javax.swing.JSeparator;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class fx extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   List<Motif> dlMotif;
   List<Listenominativecnss> lnCnssDS;
   List<Listenominativecnam> lnCnamDS;
   private JCheckBox appCNAM;
   private JCheckBox appCNSS;
   private JButton btnCalcul;
   private JButton btnExit;
   private JCheckBox cExpatrie;
   private JLabel jLabel25;
   private JLabel jLabel32;
   private JLabel jLabel33;
   private JLabel jLabel34;
   private JLabel jLabel35;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JSeparator jSeparator17;
   private JSeparator jSeparator18;
   private JSeparator jSeparator19;
   private JSeparator jSeparator20;
   private JLabel msgLabel;
   private JPanel pnlBody;
   private JProgressBar progressBar;
   private JFormattedTextField tAvNat;
   private JComboBox<Object> tFonction;
   private JFormattedTextField tMontantX;
   private JFormattedTextField tResultat1;
   private JFormattedTextField tSalBase;

   public fx() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnCalcul.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.GET_APP, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.tMontantX.setValue(0);
      this.clearFields();
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.progressBar = new JProgressBar();
      this.jLabel25 = new JLabel();
      this.tFonction = new JComboBox();
      this.jLabel32 = new JLabel();
      this.tMontantX = new JFormattedTextField();
      this.jSeparator17 = new JSeparator();
      this.jLabel33 = new JLabel();
      this.tAvNat = new JFormattedTextField();
      this.jSeparator18 = new JSeparator();
      this.jLabel34 = new JLabel();
      this.tSalBase = new JFormattedTextField();
      this.jSeparator19 = new JSeparator();
      this.appCNSS = new JCheckBox();
      this.appCNAM = new JCheckBox();
      this.cExpatrie = new JCheckBox();
      this.btnCalcul = new JButton();
      this.jSeparator20 = new JSeparator();
      this.tResultat1 = new JFormattedTextField();
      this.jLabel35 = new JLabel();
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
      this.jLabel7.setText("Calculette de salaires");
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
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      this.jLabel25.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel25.setForeground(new Color(0, 102, 153));
      this.jLabel25.setText("Fonction");
      this.tFonction.setFont(new Font("Segoe UI Light", 1, 12));
      this.tFonction.setModel(new DefaultComboBoxModel(new String[]{"Salaire net", "Retenue de la CNSS", "Retenue de la CNAM", "Retenue de l'ITS", "Le salaire brut qui donne comme net le montant (x)", "Charges employeur qui donne comme net le montant (x)"}));
      this.tFonction.addActionListener(new 2(this));
      this.jLabel32.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel32.setForeground(new Color(0, 102, 153));
      this.jLabel32.setText("Montant (x)");
      this.tMontantX.setBorder((Border)null);
      this.tMontantX.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tMontantX.setHorizontalAlignment(0);
      this.tMontantX.setFont(new Font("Segoe UI Light", 1, 14));
      this.tMontantX.addCaretListener(new 3(this));
      this.tMontantX.addFocusListener(new 4(this));
      this.jLabel33.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel33.setForeground(new Color(0, 102, 153));
      this.jLabel33.setText("Avantages en nature");
      this.tAvNat.setBorder((Border)null);
      this.tAvNat.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tAvNat.setHorizontalAlignment(0);
      this.tAvNat.setFont(new Font("Segoe UI Light", 1, 14));
      this.tAvNat.addCaretListener(new 5(this));
      this.tAvNat.addFocusListener(new 6(this));
      this.jLabel34.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel34.setForeground(new Color(0, 102, 153));
      this.jLabel34.setText("Salaire de base");
      this.tSalBase.setBorder((Border)null);
      this.tSalBase.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tSalBase.setHorizontalAlignment(0);
      this.tSalBase.setFont(new Font("Segoe UI Light", 1, 14));
      this.tSalBase.addCaretListener(new 7(this));
      this.tSalBase.addFocusListener(new 8(this));
      this.appCNSS.setBackground(new Color(255, 255, 255));
      this.appCNSS.setFont(new Font("Segoe UI Light", 1, 12));
      this.appCNSS.setForeground(new Color(0, 102, 153));
      this.appCNSS.setText("Declar\u00e9 \u00e0 la CNSS");
      this.appCNSS.addActionListener(new 9(this));
      this.appCNAM.setBackground(new Color(255, 255, 255));
      this.appCNAM.setFont(new Font("Segoe UI Light", 1, 12));
      this.appCNAM.setForeground(new Color(0, 102, 153));
      this.appCNAM.setText("Declar\u00e9 \u00e0 la CNAM");
      this.appCNAM.addActionListener(new 10(this));
      this.cExpatrie.setBackground(new Color(255, 255, 255));
      this.cExpatrie.setFont(new Font("Segoe UI Light", 1, 12));
      this.cExpatrie.setForeground(new Color(0, 102, 153));
      this.cExpatrie.setText("Salari\u00e9 expatri\u00e9");
      this.cExpatrie.addActionListener(new 11(this));
      this.btnCalcul.setBackground(new Color(255, 255, 255));
      this.btnCalcul.setToolTipText("Sauvegarder");
      this.btnCalcul.setContentAreaFilled(false);
      this.btnCalcul.setCursor(new Cursor(12));
      this.btnCalcul.setOpaque(true);
      this.btnCalcul.addActionListener(new 12(this));
      this.tResultat1.setEditable(false);
      this.tResultat1.setBorder((Border)null);
      this.tResultat1.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
      this.tResultat1.setHorizontalAlignment(0);
      this.tResultat1.setFont(new Font("Segoe UI Light", 1, 36));
      this.tResultat1.addCaretListener(new 13(this));
      this.jLabel35.setFont(new Font("Segoe UI Light", 1, 12));
      this.jLabel35.setForeground(new Color(0, 102, 153));
      this.jLabel35.setHorizontalAlignment(0);
      this.jLabel35.setText("R\u00e9sultat");
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.progressBar, -2, 728, -2).addContainerGap()).addGroup(pnlBodyLayout.createSequentialGroup().addContainerGap().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.appCNSS, -2, 140, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.appCNAM, -2, 166, -2).addGap(18, 18, 18).addComponent(this.cExpatrie, -2, 164, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel25, Alignment.LEADING, -1, -1, 32767).addComponent(this.tFonction, Alignment.LEADING, 0, -1, 32767).addGroup(Alignment.LEADING, pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel32, Alignment.LEADING, -2, 150, -2).addComponent(this.tMontantX, Alignment.LEADING, -2, 150, -2).addComponent(this.jSeparator17, -2, 150, -2)).addGap(58, 58, 58).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel33, Alignment.LEADING, -2, 150, -2).addComponent(this.tAvNat, Alignment.LEADING, -2, 150, -2).addComponent(this.jSeparator18, -2, 150, -2)).addGap(41, 41, 41).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel34, Alignment.LEADING, -2, 150, -2).addComponent(this.tSalBase, Alignment.LEADING, -2, 150, -2).addComponent(this.jSeparator19, -2, 150, -2)))).addGap(18, 18, 18).addComponent(this.btnCalcul, -2, 35, -2)).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel35, Alignment.LEADING, -2, 600, -2).addComponent(this.tResultat1, Alignment.LEADING, -2, 600, -2).addComponent(this.jSeparator20, -2, 600, -2))).addContainerGap(-1, 32767)));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(0, 0, 0).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.progressBar, -2, -1, -2).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(18, 18, 18).addComponent(this.jLabel32).addGap(0, 0, 0).addComponent(this.tMontantX, -2, 30, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.jLabel34).addGap(0, 0, 0).addComponent(this.tSalBase, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator19, -2, -1, -2)))).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel33).addGap(0, 0, 0).addComponent(this.tAvNat, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator18, -2, -1, -2))).addGap(0, 0, 0).addComponent(this.jSeparator17, -2, -1, -2).addGap(18, 18, 18).addGroup(pnlBodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.appCNSS).addComponent(this.appCNAM).addComponent(this.cExpatrie)).addGap(18, 18, 18).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel25).addGap(0, 0, 0).addComponent(this.tFonction, -2, 30, -2)).addComponent(this.btnCalcul, -2, 35, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jLabel35).addGap(0, 0, 0).addComponent(this.tResultat1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.jSeparator20, -2, -1, -2).addContainerGap(18, 32767)));
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

   private void tFonctionActionPerformed(ActionEvent var1) {
   }

   private void appCNSSActionPerformed(ActionEvent var1) {
   }

   private void appCNAMActionPerformed(ActionEvent var1) {
   }

   private void cExpatrieActionPerformed(ActionEvent var1) {
   }

   private void tMontantXCaretUpdate(CaretEvent var1) {
   }

   private void tAvNatCaretUpdate(CaretEvent var1) {
   }

   private void tSalBaseCaretUpdate(CaretEvent var1) {
   }

   private void clearFields() {
      this.tResultat1.setText("");
      this.tAvNat.setValue(0);
      this.tSalBase.setValue(0);
   }

   private void btnCalculActionPerformed(ActionEvent var1) {
      Thread t2 = new 14(this);
      t2.start();
   }

   private void tResultat1CaretUpdate(CaretEvent var1) {
   }

   private void tMontantXFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tMontantX);
   }

   private void tAvNatFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tAvNat);
   }

   private void tSalBaseFocusLost(FocusEvent var1) {
      this.menu.gl.validateValue(this.tSalBase);
   }
}
