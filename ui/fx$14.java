package com.mccmr.ui;

import com.mccmr.entity.Paramgen;

class fx$14 extends Thread {
   // $FF: synthetic field
   final fx this$0;

   fx$14(final fx var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      Paramgen p = this.this$0.menu.paramsGen;
      this.this$0.clearFields();
      boolean i_cnss = this.this$0.appCNSS.isSelected();
      boolean i_cnam = this.this$0.appCNAM.isSelected();
      double r = (double)0.0F;
      double mont = ((Number)this.this$0.tMontantX.getValue()).doubleValue();
      double avNat = ((Number)this.this$0.tAvNat.getValue()).doubleValue();
      double salBase = ((Number)this.this$0.tSalBase.getValue()).doubleValue();
      double x_cnss = (double)0.0F;
      double x_cnam = (double)0.0F;
      double tauxDevise = (double)1.0F;
      int usedITS = p.getUsedIts();
      if (i_cnss) {
         x_cnss = this.this$0.menu.pc.CNSSm(mont, tauxDevise, usedITS);
      }

      if (i_cnam) {
         x_cnam = this.this$0.menu.pc.CNAMm(mont);
      }

      switch (this.this$0.tFonction.getSelectedItem().toString()) {
         case "Salaire net":
            r = mont - avNat - this.this$0.menu.pc.ITSm(usedITS, mont, x_cnss, x_cnam, salBase, avNat, tauxDevise, this.this$0.cExpatrie.isSelected()) - x_cnss - x_cnam;
            break;
         case "Retenue de l'ITS":
            r = this.this$0.menu.pc.ITSm(usedITS, mont, x_cnss, x_cnam, salBase, avNat, tauxDevise, this.this$0.cExpatrie.isSelected());
            break;
         case "Retenue de la CNSS":
            r = this.this$0.menu.pc.CNSSm(mont, tauxDevise, usedITS);
            break;
         case "Retenue de la CNAM":
            r = this.this$0.menu.pc.CNAMm(mont);
            break;
         case "Le salaire brut qui donne comme net le montant (x)":
            r = this.this$0.menu.pc.BrutDuNet(mont, salBase, avNat, i_cnss, i_cnam, usedITS, tauxDevise, this.this$0.cExpatrie.isSelected());
            break;
         case "Charges employeur qui donne comme net le montant (x)":
            double x_bn = this.this$0.menu.pc.BrutDuNet(mont, salBase, avNat, i_cnss, i_cnam, usedITS, tauxDevise, this.this$0.cExpatrie.isSelected());
            double x_cnss_patronal = i_cnss ? this.this$0.menu.pc.CNSSm(x_bn, tauxDevise, usedITS) * (double)16.0F : (double)0.0F;
            double x_cnam_patronal = i_cnam ? x_bn * 0.09 : (double)0.0F;
            r = x_bn + x_cnss_patronal + x_cnam_patronal;
      }

      this.this$0.tResultat1.setValue(r);
      this.this$0.progressBar.setIndeterminate(false);
   }
}
