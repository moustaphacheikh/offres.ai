package com.mccmr.ui;

import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.entity.Tranchesretenuesaecheances;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;

public class paie extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   List<Rubriquepaie> dlRubriquepaie;
   int nbElements;
   public Motif selectedMotif;
   private JButton btnCalPaie;
   private JButton btnDelateCurrentPaie;
   private JButton btnExit;
   private JCheckBox cAllMotifs;
   private JCheckBox cCorrectionEng;
   private JCheckBox cCorrectionRub;
   private JLabel jLabel20;
   private JLabel jLabel21;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JLabel motifLabel;
   private JLabel msgInfoLabel;
   private JLabel msgLabel;
   private JLabel periodeCouranteLabel;
   private JPanel pnlBody;
   private JProgressBar progressBar;
   private JLabel salarieLabel;
   private JLabel statusLabel;
   private JComboBox<Object> tMotif;
   private JDateChooser tPaieAu;
   private JDateChooser tPaieDu;

   public paie() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnCalPaie.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADJUST, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelateCurrentPaie.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CANCEL, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.menu.remplirCombo("Motif", this.tMotif);
      Date paieAu_default = this.menu.paramsGen.getPeriodeCourante();
      Date paieDu = this.menu.gl.addRetriveDays(paieAu_default, -27);
      Calendar cal = Calendar.getInstance();
      cal.setTime(paieDu);
      Date paieAu = this.menu.gl.addRetriveDays(paieDu, cal.getActualMaximum(5) - 1);
      this.tPaieAu.setDate(paieAu);
      this.tPaieDu.setDate(paieDu);
      this.periodeCouranteLabel.setText((new SimpleDateFormat("MMMM yyyy")).format(this.menu.paramsGen.getPeriodeCourante()).toUpperCase());
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.jLabel20 = new JLabel();
      this.progressBar = new JProgressBar();
      this.btnCalPaie = new JButton();
      this.tPaieDu = new JDateChooser();
      this.jLabel21 = new JLabel();
      this.tPaieAu = new JDateChooser();
      this.cCorrectionEng = new JCheckBox();
      this.cCorrectionRub = new JCheckBox();
      this.statusLabel = new JLabel();
      this.motifLabel = new JLabel();
      this.msgInfoLabel = new JLabel();
      this.salarieLabel = new JLabel();
      this.periodeCouranteLabel = new JLabel();
      this.cAllMotifs = new JCheckBox();
      this.tMotif = new JComboBox();
      this.btnDelateCurrentPaie = new JButton();
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
      this.jLabel7.setText("Calcul de paie");
      this.jLabel7.setToolTipText("");
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.setRequestFocusEnabled(false);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnExit, -2, 39, -2).addGap(19, 19, 19)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("P\u00e9riode du");
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      this.btnCalPaie.setBackground(new Color(255, 255, 255));
      this.btnCalPaie.setToolTipText("Calculer la paie");
      this.btnCalPaie.setContentAreaFilled(false);
      this.btnCalPaie.setCursor(new Cursor(12));
      this.btnCalPaie.setOpaque(true);
      this.btnCalPaie.setRequestFocusEnabled(false);
      this.btnCalPaie.addActionListener(new 2(this));
      this.tPaieDu.setBorder(BorderFactory.createLineBorder(new Color(48, 131, 185)));
      this.tPaieDu.setDateFormatString("dd/MM/yyyy");
      this.tPaieDu.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel21.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel21.setForeground(new Color(0, 102, 153));
      this.jLabel21.setText(" au");
      this.tPaieAu.setBorder(BorderFactory.createLineBorder(new Color(48, 131, 185)));
      this.tPaieAu.setDateFormatString("dd/MM/yyyy");
      this.tPaieAu.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCorrectionEng.setBackground(new Color(255, 255, 255));
      this.cCorrectionEng.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCorrectionEng.setForeground(new Color(0, 102, 153));
      this.cCorrectionEng.setText("Correction des engagements");
      this.cCorrectionEng.addActionListener(new 3(this));
      this.cCorrectionRub.setBackground(new Color(255, 255, 255));
      this.cCorrectionRub.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCorrectionRub.setForeground(new Color(0, 102, 153));
      this.cCorrectionRub.setText("Correction de rubriques de paie");
      this.cCorrectionRub.addActionListener(new 4(this));
      this.statusLabel.setFont(new Font("Segoe UI Light", 0, 12));
      this.statusLabel.setForeground(new Color(0, 102, 153));
      this.statusLabel.setText(".");
      this.motifLabel.setFont(new Font("Segoe UI Light", 0, 12));
      this.motifLabel.setForeground(new Color(0, 102, 153));
      this.motifLabel.setText(".");
      this.msgInfoLabel.setFont(new Font("Segoe UI Light", 0, 12));
      this.msgInfoLabel.setForeground(new Color(0, 102, 153));
      this.msgInfoLabel.setText(".");
      this.salarieLabel.setFont(new Font("Segoe UI Light", 0, 12));
      this.salarieLabel.setForeground(new Color(0, 102, 153));
      this.salarieLabel.setText(".");
      this.periodeCouranteLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.periodeCouranteLabel.setForeground(new Color(0, 102, 153));
      this.periodeCouranteLabel.setText(".");
      this.cAllMotifs.setBackground(new Color(255, 255, 255));
      this.cAllMotifs.setFont(new Font("Segoe UI Light", 0, 12));
      this.cAllMotifs.setForeground(new Color(0, 102, 153));
      this.cAllMotifs.setSelected(true);
      this.cAllMotifs.setText("Tous les motifs");
      this.cAllMotifs.addActionListener(new 5(this));
      this.tMotif.setFont(new Font("Segoe UI Light", 1, 12));
      this.tMotif.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMotif.setEnabled(false);
      this.tMotif.addActionListener(new 6(this));
      this.btnDelateCurrentPaie.setBackground(new Color(255, 255, 255));
      this.btnDelateCurrentPaie.setToolTipText("Supprimer la paie courante");
      this.btnDelateCurrentPaie.setContentAreaFilled(false);
      this.btnDelateCurrentPaie.setCursor(new Cursor(12));
      this.btnDelateCurrentPaie.setOpaque(true);
      this.btnDelateCurrentPaie.setRequestFocusEnabled(false);
      this.btnDelateCurrentPaie.addActionListener(new 7(this));
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addContainerGap().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.salarieLabel, Alignment.LEADING, -1, 631, 32767).addComponent(this.statusLabel, Alignment.LEADING, -1, -1, 32767)).addContainerGap(-1, 32767)).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.periodeCouranteLabel, -1, -1, 32767).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.motifLabel, -1, -1, 32767).addComponent(this.msgInfoLabel, -1, -1, 32767)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnDelateCurrentPaie, -2, 35, -2).addGap(103, 103, 103).addComponent(this.btnCalPaie, -2, 35, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.jLabel20, -2, 75, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tPaieDu, -2, 152, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jLabel21, -2, 27, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tPaieAu, -2, 152, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.cAllMotifs, -2, 143, -2).addGap(31, 31, 31).addComponent(this.tMotif, -2, 223, -2))).addGap(90, 90, 90).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.cCorrectionEng, -1, -1, 32767).addComponent(this.cCorrectionRub, -1, -1, 32767)))).addGap(26, 26, 26)))).addComponent(this.progressBar, -1, -1, 32767));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(0, 0, 0).addComponent(this.progressBar, -2, -1, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.periodeCouranteLabel, -2, 31, -2).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.cCorrectionRub).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPaieAu, -1, -1, 32767).addComponent(this.jLabel20, -1, -1, 32767).addComponent(this.tPaieDu, -1, -1, 32767).addComponent(this.jLabel21, -2, 25, -2))).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cCorrectionEng).addPreferredGap(ComponentPlacement.RELATED, 20, 32767)).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(pnlBodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.cAllMotifs).addComponent(this.tMotif, -2, 30, -2)).addPreferredGap(ComponentPlacement.UNRELATED))).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.statusLabel, -2, 24, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.salarieLabel, -2, 24, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnCalPaie, Alignment.TRAILING, -2, 35, -2).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addComponent(this.motifLabel, -2, 24, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.msgInfoLabel, -2, 24, -2)))).addComponent(this.btnDelateCurrentPaie, -2, 35, -2)).addContainerGap()));
      this.msgLabel.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.msgLabel, -2, 721, -2).addGap(0, 52, 32767)).addComponent(this.pnlBody, -1, -1, 32767).addComponent(this.jPanel1, -1, -1, 32767)).addGap(0, 0, 0)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlBody, -2, -1, -2).addGap(0, 0, 0).addComponent(this.msgLabel, -2, 22, -2)));
      this.setSize(new Dimension(773, 363));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void RubriquePaieCorrection() {
      try {
         Query q = this.menu.entityManager.createQuery("Select p from Rubriquepaie p");
         q.setMaxResults(1000000);
         Set<Rubriquepaie> dlRubriquepaie = new HashSet(q.getResultList());

         for(Rubriquepaie rs : (Set)dlRubriquepaie.stream().filter((var1x) -> var1x.getPeriode().before(this.menu.paramsGen.getPeriodeCourante())).collect(Collectors.toSet())) {
            if (this.menu.pc.paieById(rs.getEmploye(), rs.getMotif(), rs.getPeriode()) == null) {
               this.menu.gl.deleteOcurance(rs);
            }
         }

         for(Rubriquepaie rs : (Set)dlRubriquepaie.stream().filter((var1x) -> this.menu.df.format(var1x.getPeriode()).equalsIgnoreCase(this.menu.df.format(this.menu.paramsGen.getPeriodeCourante()))).collect(Collectors.toSet())) {
            if (rs.getEmploye() != null) {
               if (rs.getEmploye().isEnConge()) {
                  this.menu.gl.deleteOcurance(rs);
               }
            } else {
               this.menu.gl.deleteOcurance(rs);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private void EngagementCorrection() {
      Query q = this.menu.entityManager.createQuery("Select p from Tranchesretenuesaecheances p");
      q.setMaxResults(1000000);

      for(Tranchesretenuesaecheances rs : new HashSet(q.getResultList())) {
         if (this.menu.pc.paieById(rs.getRetenuesaecheances().getEmploye(), this.menu.motifSN, rs.getPeriode()) == null) {
            this.menu.gl.deleteOcurance(rs);
         }
      }

   }

   private void btnCalPaieActionPerformed(ActionEvent var1) {
      this.msgInfoLabel.setText("Initialisation ...");
      this.motifLabel.setText(".");
      this.salarieLabel.setText(".");
      this.progressBar.setValue(0);
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous le calcul de la paie g\u00e9n\u00e9rale ?", " Confirmation de calcul de paie g\u00e9n\u00e9rale", 0);
      if (rep == 0) {
         Thread t = new 8(this);
         t.start();
      }

   }

   private void showMessage(String var1) {
      JOptionPane.showMessageDialog(this, msg, "Errror!", 0);
   }

   private void cCorrectionEngActionPerformed(ActionEvent var1) {
   }

   private void cCorrectionRubActionPerformed(ActionEvent var1) {
   }

   private void cAllMotifsActionPerformed(ActionEvent var1) {
      this.tMotif.setEnabled(!this.cAllMotifs.isSelected());
   }

   private void tMotifActionPerformed(ActionEvent var1) {
   }

   private void btnDelateCurrentPaieActionPerformed(ActionEvent var1) {
      int rep = JOptionPane.showConfirmDialog(this, " Attantion. Confirmez-vous la suppression de la paie courante ?", " Confirmation", 0);
      if (rep == 0) {
         if (this.menu.dialect.toString().contains("Oracle")) {
            DateFormat var10001 = this.menu.df;
            this.menu.gl.exQuery("delete from Paie where periode=TO_DATE('" + var10001.format(this.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
         } else {
            DateFormat var3 = this.menu.df;
            this.menu.gl.exQuery("delete from Paie where periode='" + var3.format(this.menu.paramsGen.getPeriodeCourante()) + "'");
         }

         this.menu.showMsg(this, "Paie supprimr\u00e9e avec succ\u00e9");
      }

   }
}
