package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.util.PaieClass;
import java.text.NumberFormat;
import javax.swing.JLabel;

class salarys$281 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$281(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnInfosPaie.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      Motif motif = (Motif)this.this$0.tMotif.getSelectedItem();
      Paie paie = this.this$0.menu.pc.paieCalcule(this.this$0.selectedOne, motif, this.this$0.tPaieDu.getDate(), this.this$0.tPaieAu.getDate());
      JLabel label1 = new JLabel("PERIODE : " + paie.getPeriodeLettre());
      JLabel label2 = new JLabel("MOTIF : " + motif.getNom());
      NumberFormat var10002 = this.this$0.menu.nf;
      JLabel label3 = new JLabel("NJT : " + var10002.format(paie.getNjt()));
      var10002 = this.this$0.menu.nf;
      JLabel label4 = new JLabel("BT : " + var10002.format(paie.getBt()));
      var10002 = this.this$0.menu.nf;
      JLabel label5 = new JLabel("BNI : " + var10002.format(paie.getBni()));
      String var24 = this.this$0.menu.nf.format(paie.getBt() - paie.getBni() - paie.getRetenuesBrut());
      JLabel label16 = new JLabel("BI (BT - BNI - RET BRUT) : " + var24);
      NumberFormat var25 = this.this$0.menu.nf;
      JLabel label6 = new JLabel("AV NAT : " + var25.format(paie.getBiAvnat()));
      var25 = this.this$0.menu.nf;
      JLabel label7 = new JLabel("RET BRUT : " + var25.format(paie.getRetenuesBrut()));
      var25 = this.this$0.menu.nf;
      JLabel label8 = new JLabel("ITS : " + var25.format(paie.getIts()));
      var25 = this.this$0.menu.nf;
      JLabel label9 = new JLabel("CNSS : " + var25.format(paie.getCnss()));
      var25 = this.this$0.menu.nf;
      JLabel label10 = new JLabel("CNAM : " + var25.format(paie.getCnam()));
      var25 = this.this$0.menu.nf;
      JLabel label11 = new JLabel("RET NET : " + var25.format(paie.getRetenuesNet()));
      var25 = this.this$0.menu.nf;
      JLabel label12 = new JLabel("RITS : " + var25.format(paie.getRits()));
      var25 = this.this$0.menu.nf;
      JLabel label13 = new JLabel("RCNSS : " + var25.format(paie.getRcnss()));
      var25 = this.this$0.menu.nf;
      JLabel label14 = new JLabel("RCNAM : " + var25.format(paie.getRcnam()));
      var25 = this.this$0.menu.nf;
      JLabel label15 = new JLabel("NET PAIE : " + var25.format(paie.getNet()));
      JLabel label17 = new JLabel("==========================");
      PaieClass var35 = this.this$0.menu.pc;
      Employe var10003 = this.this$0.selectedOne;
      JLabel label18 = new JLabel("AUG. SAL. : " + var35.augmentationSalaire(var10003, this.this$0.menu.paramsGen.getPeriodeCourante()));
      this.this$0.menu.gl.formatLebleMsgBox(label1);
      this.this$0.menu.gl.formatLebleMsgBox(label2);
      this.this$0.menu.gl.formatLebleMsgBox(label3);
      this.this$0.menu.gl.formatLebleMsgBox(label4);
      this.this$0.menu.gl.formatLebleMsgBox(label5);
      this.this$0.menu.gl.formatLebleMsgBox(label16);
      this.this$0.menu.gl.formatLebleMsgBox(label6);
      this.this$0.menu.gl.formatLebleMsgBox(label7);
      this.this$0.menu.gl.formatLebleMsgBox(label8);
      this.this$0.menu.gl.formatLebleMsgBox(label9);
      this.this$0.menu.gl.formatLebleMsgBox(label10);
      this.this$0.menu.gl.formatLebleMsgBox(label11);
      this.this$0.menu.gl.formatLebleMsgBox(label12);
      this.this$0.menu.gl.formatLebleMsgBox(label13);
      this.this$0.menu.gl.formatLebleMsgBox(label14);
      this.this$0.menu.gl.formatLebleMsgBox(label15);
      this.this$0.menu.gl.formatLebleMsgBox(label17);
      this.this$0.menu.gl.formatLebleMsgBox(label18);
      Object[] tab = new Object[]{label1, label2, label3, label4, label5, label16, label6, label7, label8, label9, label10, label11, label12, label13, label14, label15, label17, label18};
      this.this$0.showMsgDialog(tab);
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.btnInfosPaie.setEnabled(true);
   }
}
