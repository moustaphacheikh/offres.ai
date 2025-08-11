package com.mccmr.ui;

import com.mccmr.entity.Paie;
import com.mccmr.entity.Paramgen;
import com.mccmr.entity.Retenuesaecheances;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.WriteExcel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class cloture extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   List<Rubriquepaie> dlRubriquepaie;
   int nbElements;
   private JButton btnCancelLastCloture;
   private JButton btnCloture;
   private JButton btnExit;
   private JCheckBox cContunue;
   private JCheckBox cVarFixeCopy;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JSeparator jSeparator18;
   private JLabel motifLabel;
   private JLabel msgInfoLabel;
   private JLabel msgLabel;
   private JLabel periodeCouranteLabel;
   private JPanel pnlBody;
   private JProgressBar progressBar;
   private JLabel salarieLabel;
   private JLabel statusLabel;
   private JFormattedTextField tIdSalarie;

   public cloture() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnCloture.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ARCHIVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnCancelLastCloture.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.UNARCHIVE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.periodeCouranteLabel.setText((new SimpleDateFormat("MMMM yyyy")).format(this.menu.paramsGen.getPeriodeCourante()).toUpperCase());
   }

   private void saveEngagementsHistory(boolean var1) throws IOException, WriteException {
      if (this.menu.pc.usedRubID(16) != null) {
         String var10000 = (new SimpleDateFormat("MMMM-yyyy")).format(this.menu.paramsGen.getPeriodeCourante());
         String fileName = "eng_history/LEng_" + var10000 + ".xls";
         WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
         WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "Liste Engagements");
         this.menu.we.addLabelTitre(excelSheet, 0, 0, this.menu.paramsGen.getNomEntreprise().toUpperCase());
         WriteExcel var17 = this.menu.we;
         String var10004 = (new SimpleDateFormat("MMMM-yyyy")).format(this.menu.paramsGen.getPeriodeCourante()).toUpperCase();
         var17.addLabelTitre(excelSheet, 0, 1, "LIST ENGAGEMENTS : " + var10004);
         var17 = this.menu.we;
         var10004 = (new SimpleDateFormat("dd/MM/yyyy \u00e0 HH:mm:ss")).format(new Date());
         var17.addLabelBold(excelSheet, 0, 2, "Edit\u00e9 le: " + var10004);
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 0, 4, "ID SAL.");
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 1, 4, "PRENOM DU SALARIE");
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 2, 4, "NOM DU SALARIE");
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 3, 4, "DATE");
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, 4, "NOTE");
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 4, "CAPITAL");
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, 4, "ENCOURS");
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, 4, "TOTAL REG.");
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 8, 4, "ECH. COURANTE SN");
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 9, 4, "ECH. COURANTE CNGE");
         this.menu.we.addLabelBoldBorderWrap(excelSheet, 10, 4, "RET. MENS.");
         int row = 5;
         EntityManager var19 = this.menu.entityManager;
         int var10001 = this.menu.pc.usedRubID(16).getId();
         Query q = var19.createQuery("Select p from Retenuesaecheances p where p.rubrique = " + var10001 + (onlyActif ? " and p.solde=?1 and p.active=?2 " : "") + " order by p.employe").setParameter(1, false).setParameter(2, true);
         q.setMaxResults(1000000);
         List<Retenuesaecheances> dlRE = q.getResultList();
         if (onlyActif) {
            dlRE.stream().filter((var0) -> var0.getEmploye() != null && var0.getEmploye().isActif()).collect(Collectors.toList());
         }

         int maxPB = dlRE.size();
         this.progressBar.setMaximum(maxPB);
         int valuePB = 0;
         this.progressBar.setValue(valuePB);

         for(Retenuesaecheances r : dlRE) {
            double tr = this.menu.pc.totalReglementRetAE(r);
            double retMens = (double)0.0F;
            Rubriquepaie ra = this.menu.pc.rubriquePaieById(r.getEmploye(), r.getRubrique(), this.menu.motifSN, this.menu.paramsGen.getPeriodeCourante());
            if (ra != null) {
               retMens = ra.getMontant();
            }

            this.menu.we.addNumberBorder(excelSheet, 0, row, (double)r.getEmploye().getId());
            this.menu.we.addLabelBorder(excelSheet, 1, row, r.getEmploye().getPrenom());
            this.menu.we.addLabelBorder(excelSheet, 2, row, r.getEmploye().getNom());
            this.menu.we.addLabelBorder(excelSheet, 3, row, this.menu.fdf.format(r.getDateAccord()));
            this.menu.we.addLabelBorder(excelSheet, 4, row, r.getNote());
            this.menu.we.addNumberBorder(excelSheet, 5, row, r.getCapital());
            this.menu.we.addNumberBorder(excelSheet, 6, row, r.getCapital() - tr);
            this.menu.we.addNumberBorder(excelSheet, 7, row, tr);
            this.menu.we.addNumberBorder(excelSheet, 8, row, r.getEcheancecourante());
            this.menu.we.addNumberBorder(excelSheet, 9, row, r.getEcheancecourantecng());
            this.menu.we.addNumberBorder(excelSheet, 10, row, retMens);
            ++valuePB;
            this.progressBar.setValue(valuePB);
            ++row;
         }

         workbook.write();
         workbook.close();
      }

   }

   private void cloturer() {
      int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la cl\u00f4ture de la p\u00e9riode courante ?", " Confirmation de cl\u00f4ture", 0);
      if (rep == 0) {
         Thread t = new 1(this);
         t.start();
      }

   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.progressBar = new JProgressBar();
      this.btnCloture = new JButton();
      this.statusLabel = new JLabel();
      this.motifLabel = new JLabel();
      this.msgInfoLabel = new JLabel();
      this.salarieLabel = new JLabel();
      this.periodeCouranteLabel = new JLabel();
      this.btnCancelLastCloture = new JButton();
      this.cContunue = new JCheckBox();
      this.jSeparator18 = new JSeparator();
      this.tIdSalarie = new JFormattedTextField();
      this.cVarFixeCopy = new JCheckBox();
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
      this.jLabel7.setText("Cl\u00f4ture de paie");
      this.jLabel7.setToolTipText("");
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 2(this));
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
      this.btnCloture.setBackground(new Color(255, 255, 255));
      this.btnCloture.setToolTipText("Cl\u00f4turer");
      this.btnCloture.setContentAreaFilled(false);
      this.btnCloture.setCursor(new Cursor(12));
      this.btnCloture.setOpaque(true);
      this.btnCloture.addActionListener(new 3(this));
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
      this.btnCancelLastCloture.setBackground(new Color(255, 255, 255));
      this.btnCancelLastCloture.setToolTipText("Annuler la derni\u00e8re cl\u00f4ture");
      this.btnCancelLastCloture.setContentAreaFilled(false);
      this.btnCancelLastCloture.setCursor(new Cursor(12));
      this.btnCancelLastCloture.setOpaque(true);
      this.btnCancelLastCloture.addActionListener(new 4(this));
      this.cContunue.setBackground(new Color(255, 255, 255));
      this.cContunue.setFont(new Font("Segoe UI Light", 1, 12));
      this.cContunue.setForeground(new Color(0, 102, 153));
      this.cContunue.setText("Continuer la cl\u00f4ture pr\u00e9cedente \u00e0 partir du salari\u00e9 #");
      this.cContunue.addActionListener(new 5(this));
      this.tIdSalarie.setBorder((Border)null);
      this.tIdSalarie.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tIdSalarie.setHorizontalAlignment(0);
      this.tIdSalarie.setEnabled(false);
      this.tIdSalarie.setFont(new Font("Segoe UI Light", 0, 12));
      this.tIdSalarie.addCaretListener(new 6(this));
      this.tIdSalarie.addFocusListener(new 7(this));
      this.cVarFixeCopy.setBackground(new Color(255, 255, 255));
      this.cVarFixeCopy.setFont(new Font("Segoe UI Light", 1, 12));
      this.cVarFixeCopy.setForeground(new Color(0, 102, 153));
      this.cVarFixeCopy.setSelected(true);
      this.cVarFixeCopy.setText("Copy des rubriques de paie fixe");
      this.cVarFixeCopy.addActionListener(new 8(this));
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlBodyLayout.createSequentialGroup().addContainerGap().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.periodeCouranteLabel, -1, -1, 32767).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.motifLabel, -2, 500, -2).addComponent(this.msgInfoLabel, -2, 500, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnCancelLastCloture, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.btnCloture, -2, 35, -2).addGap(8, 8, 8))).addGap(26, 26, 26)).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.statusLabel, -2, 500, -2).addComponent(this.salarieLabel, -2, 500, -2).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.cContunue, -2, 383, -2).addGap(27, 27, 27).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.tIdSalarie, Alignment.LEADING, -2, 60, -2).addComponent(this.jSeparator18, -2, 60, -2))).addComponent(this.cVarFixeCopy, -2, 318, -2)).addGap(0, 0, 32767)))).addGroup(pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.progressBar, -2, 728, -2))).addContainerGap()));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(0, 0, 0).addComponent(this.progressBar, -2, -1, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.periodeCouranteLabel, -2, 31, -2).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.cContunue, -2, 34, -2).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.tIdSalarie, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator18, -2, -1, -2))).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cVarFixeCopy, -2, 34, -2).addPreferredGap(ComponentPlacement.RELATED, 60, 32767).addComponent(this.salarieLabel, -2, 24, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.statusLabel, -2, 24, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.btnCloture, -2, 35, -2).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.motifLabel, -2, 24, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.msgInfoLabel, -2, 24, -2)).addComponent(this.btnCancelLastCloture, -2, 35, -2)).addContainerGap()));
      this.msgLabel.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jPanel1, -1, -1, 32767).addComponent(this.msgLabel, -1, -1, 32767).addComponent(this.pnlBody, -2, 725, 32767)).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlBody, -1, -1, 32767).addGap(0, 0, 0).addComponent(this.msgLabel, -2, 38, -2)));
      this.setSize(new Dimension(727, 424));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void btnClotureActionPerformed(ActionEvent var1) {
      if (this.menu.dateExpLicence().before(this.menu.paramsGen.getPeriodeCourante())) {
         this.menu.showErrMsg(this, "Votre licence a expir\u00e9e");
      } else {
         this.cloturer();
      }

   }

   private void showMessage() {
      JOptionPane.showMessageDialog(this, "Cl\u00f4ture termin\u00e9e avec succ\u00e9. L'application sera arr\u00eater. Veuillez d\u00e9marer ELIYA Paie \u00e0 nouveau!");
   }

   private void btnCancelLastClotureActionPerformed(ActionEvent var1) {
      if (this.menu.dateExpLicence().before(this.menu.paramsGen.getPeriodeCourante())) {
         this.menu.showErrMsg(this, "Votre licence a expir\u00e9e");
      } else {
         int rep = JOptionPane.showConfirmDialog(this, " Attantion. Cette op\u00e9ration va supprimer toutes les donn\u00e9es de la paie courante. Confirmez vous l'anulation de la derni\u00e8re cl\u00f4ture ?", " Confirmation de l'anulation de la derni\u00e8re cl\u00f4ture", 0);
         if (rep == 0) {
            this.statusLabel.setText("Init Paie...");
            if (this.menu.dialect.toString().contains("Oracle")) {
               DateFormat var10001 = this.menu.df;
               this.menu.gl.exQuery("delete from Conges where periode=TO_DATE('" + var10001.format(this.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
               var10001 = this.menu.df;
               this.menu.gl.exQuery("delete from Jour where periode=TO_DATE('" + var10001.format(this.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
               var10001 = this.menu.df;
               this.menu.gl.exQuery("delete from Weekot where periode=TO_DATE('" + var10001.format(this.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
               var10001 = this.menu.df;
               this.menu.gl.exQuery("delete from Paie where periode=TO_DATE('" + var10001.format(this.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
               var10001 = this.menu.df;
               this.menu.gl.exQuery("delete from Rubriquepaie where periode=TO_DATE('" + var10001.format(this.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
               var10001 = this.menu.df;
               this.menu.gl.exQuery("delete from Njtsalarie where periode=TO_DATE('" + var10001.format(this.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
               var10001 = this.menu.df;
               this.menu.gl.exQuery("delete from Tranchesretenuesaecheances where periode=TO_DATE('" + var10001.format(this.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
            } else {
               DateFormat var13 = this.menu.df;
               this.menu.gl.exQuery("delete from Conges where periode='" + var13.format(this.menu.paramsGen.getPeriodeCourante()) + "' ");
               var13 = this.menu.df;
               this.menu.gl.exQuery("delete from Jour where periode='" + var13.format(this.menu.paramsGen.getPeriodeCourante()) + "' ");
               var13 = this.menu.df;
               this.menu.gl.exQuery("delete from Weekot where periode='" + var13.format(this.menu.paramsGen.getPeriodeCourante()) + "' ");
               var13 = this.menu.df;
               this.menu.gl.exQuery("delete from Paie where periode='" + var13.format(this.menu.paramsGen.getPeriodeCourante()) + "' ");
               var13 = this.menu.df;
               this.menu.gl.exQuery("delete from Rubriquepaie where periode='" + var13.format(this.menu.paramsGen.getPeriodeCourante()) + "' ");
               var13 = this.menu.df;
               this.menu.gl.exQuery("delete from Njtsalarie where periode='" + var13.format(this.menu.paramsGen.getPeriodeCourante()) + "' ");
               var13 = this.menu.df;
               this.menu.gl.exQuery("delete from Tranchesretenuesaecheances where periode='" + var13.format(this.menu.paramsGen.getPeriodeCourante()) + "' ");
            }

            Paramgen param = this.menu.paramsGen;
            param.setPeriodeSuivante(this.menu.paramsGen.getPeriodeCourante());
            Date periodeCourante = this.menu.gl.addRetriveDays(this.menu.paramsGen.getPeriodeCourante(), -30);

            try {
               periodeCourante = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periodeCourante));
            } catch (Exception e) {
               e.printStackTrace();
            }

            param.setPeriodeCourante(periodeCourante);
            this.menu.gl.updateOcurance(param);
            this.msgInfoLabel.setText("Cl\u00f4ture termin\u00e9e!");
            JOptionPane.showMessageDialog(this, "L'application sera arr\u00eater. Veuillez d\u00e9marer ELIYA Paie \u00e0 nouveau!");
            System.exit(0);
         }
      }

   }

   private void cContunueActionPerformed(ActionEvent var1) {
      this.tIdSalarie.setEnabled(this.cContunue.isSelected());
   }

   private void tIdSalarieCaretUpdate(CaretEvent var1) {
   }

   private void tIdSalarieFocusLost(FocusEvent var1) {
   }

   private void cVarFixeCopyActionPerformed(ActionEvent var1) {
   }
}
