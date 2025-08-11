package com.mccmr.ui;

import com.mccmr.entity.Paie;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;

public class repport extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   List<Rubriquepaie> dlRubriquepaie;
   int nbElements;
   private JButton btnExit;
   private JButton btnMenuBS;
   private JButton btnMenuDA;
   private JButton btnMenuDM;
   private JButton btnMenuDM3;
   private JButton btnMenuEP;
   private JButton btnMenuEPC;
   private JButton btnMenuVB;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JProgressBar loadingPB;
   private JLabel msgLabel;
   private JPanel pnlBody;

   public repport() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.btnMenuBS = new JButton();
      this.btnMenuEP = new JButton();
      this.btnMenuEPC = new JButton();
      this.btnMenuDM = new JButton();
      this.btnMenuDA = new JButton();
      this.btnMenuVB = new JButton();
      this.btnMenuDM3 = new JButton();
      this.msgLabel = new JLabel();
      this.loadingPB = new JProgressBar();
      this.setBackground(new Color(255, 255, 255));
      this.setBorder((Border)null);
      this.setAutoscrolls(true);
      this.setFont(new Font("Segoe UI Light", 0, 12));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(1000, 600));
      this.jPanel1.setBackground(new Color(255, 255, 255));
      this.jLabel7.setFont(new Font("Segoe UI Light", 0, 24));
      this.jLabel7.setForeground(new Color(0, 102, 153));
      this.jLabel7.setText("Rapports");
      this.jLabel7.setToolTipText("");
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnExit, -2, 39, -2).addGap(19, 19, 19)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.btnMenuBS.setBackground(new Color(255, 255, 255));
      this.btnMenuBS.setFont(new Font("Segoe UI Light", 0, 14));
      this.btnMenuBS.setText("Bulletins de paie");
      this.btnMenuBS.setContentAreaFilled(false);
      this.btnMenuBS.setCursor(new Cursor(12));
      this.btnMenuBS.setOpaque(true);
      this.btnMenuBS.addActionListener(new 2(this));
      this.btnMenuEP.setBackground(new Color(255, 255, 255));
      this.btnMenuEP.setFont(new Font("Segoe UI Light", 0, 14));
      this.btnMenuEP.setText("Etats de paie");
      this.btnMenuEP.setContentAreaFilled(false);
      this.btnMenuEP.setCursor(new Cursor(12));
      this.btnMenuEP.setOpaque(true);
      this.btnMenuEP.addActionListener(new 3(this));
      this.btnMenuEPC.setBackground(new Color(255, 255, 255));
      this.btnMenuEPC.setFont(new Font("Segoe UI Light", 0, 14));
      this.btnMenuEPC.setText("Etats de paie cumul\u00e9s");
      this.btnMenuEPC.setContentAreaFilled(false);
      this.btnMenuEPC.setCursor(new Cursor(12));
      this.btnMenuEPC.setOpaque(true);
      this.btnMenuEPC.addActionListener(new 4(this));
      this.btnMenuDM.setBackground(new Color(255, 255, 255));
      this.btnMenuDM.setFont(new Font("Segoe UI Light", 0, 14));
      this.btnMenuDM.setText("D\u00e9clarations mensuelles");
      this.btnMenuDM.setContentAreaFilled(false);
      this.btnMenuDM.setCursor(new Cursor(12));
      this.btnMenuDM.setOpaque(true);
      this.btnMenuDM.addActionListener(new 5(this));
      this.btnMenuDA.setBackground(new Color(255, 255, 255));
      this.btnMenuDA.setFont(new Font("Segoe UI Light", 0, 14));
      this.btnMenuDA.setText("D\u00e9clarations annuelles");
      this.btnMenuDA.setContentAreaFilled(false);
      this.btnMenuDA.setCursor(new Cursor(12));
      this.btnMenuDA.setOpaque(true);
      this.btnMenuDA.addActionListener(new 6(this));
      this.btnMenuVB.setBackground(new Color(255, 255, 255));
      this.btnMenuVB.setFont(new Font("Segoe UI Light", 0, 14));
      this.btnMenuVB.setText("Virements bancaires");
      this.btnMenuVB.setContentAreaFilled(false);
      this.btnMenuVB.setCursor(new Cursor(12));
      this.btnMenuVB.setOpaque(true);
      this.btnMenuVB.addActionListener(new 7(this));
      this.btnMenuDM3.setBackground(new Color(255, 255, 255));
      this.btnMenuDM3.setFont(new Font("Segoe UI Light", 0, 14));
      this.btnMenuDM3.setText("Comptabilit\u00e9");
      this.btnMenuDM3.setContentAreaFilled(false);
      this.btnMenuDM3.setCursor(new Cursor(12));
      this.btnMenuDM3.setOpaque(true);
      this.btnMenuDM3.addActionListener(new 8(this));
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(25, 25, 25).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.btnMenuEPC, -2, 250, -2).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnMenuDM, Alignment.TRAILING, -2, 250, -2).addComponent(this.btnMenuBS, -2, 250, -2).addComponent(this.btnMenuEP, Alignment.TRAILING, -2, 250, -2)).addComponent(this.btnMenuVB, -2, 250, -2).addComponent(this.btnMenuDM3, -2, 250, -2)).addGap(79, 79, 79).addComponent(this.btnMenuDA, -2, 250, -2))).addContainerGap(113, 32767)));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(39, 39, 39).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.btnMenuBS, -2, 62, -2).addGap(7, 7, 7).addComponent(this.btnMenuEP, -2, 62, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnMenuDM, -2, 62, -2).addGap(6, 6, 6).addComponent(this.btnMenuVB, -2, 62, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnMenuDM3, -2, 62, -2).addContainerGap(57, 32767)).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.btnMenuDA, -2, 62, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnMenuEPC, -2, 62, -2).addGap(255, 255, 255)))));
      this.msgLabel.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      this.loadingPB.setBackground(new Color(204, 204, 204));
      this.loadingPB.setForeground(new Color(0, 102, 153));
      this.loadingPB.setBorderPainted(false);
      this.loadingPB.setOpaque(true);
      this.loadingPB.setStringPainted(true);
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jPanel1, Alignment.LEADING, -1, -1, 32767).addComponent(this.msgLabel, Alignment.LEADING, -1, -1, 32767).addGroup(Alignment.LEADING, layout.createSequentialGroup().addGap(11, 11, 11).addComponent(this.pnlBody, -2, -1, -2))).addContainerGap(-1, 32767)).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.loadingPB, -2, 710, -2).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.loadingPB, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -2, -1, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.msgLabel, -2, 22, -2).addContainerGap(-1, 32767)));
      this.setSize(new Dimension(717, 502));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void btnMenuBSActionPerformed(ActionEvent var1) {
      Thread t = new 9(this);
      t.start();
   }

   private void btnMenuEPActionPerformed(ActionEvent var1) {
      Thread t = new 10(this);
      t.start();
   }

   private void btnMenuEPCActionPerformed(ActionEvent var1) {
      Thread t = new 11(this);
      t.start();
   }

   private void btnMenuDMActionPerformed(ActionEvent var1) {
      this.menu.declarationsFrame.refresh();
      this.menu.openFrame(this.menu.declarationsFrame);
   }

   private void btnMenuDAActionPerformed(ActionEvent var1) {
      this.menu.declarationsAnnuelleFrame.refresh();
      this.menu.openFrame(this.menu.declarationsAnnuelleFrame);
   }

   private void btnMenuVBActionPerformed(ActionEvent var1) {
      this.menu.virementsFrame.refresh();
      this.menu.openFrame(this.menu.virementsFrame);
   }

   private void btnMenuDM3ActionPerformed(ActionEvent var1) {
      Thread t = new 12(this);
      t.start();
   }
}
