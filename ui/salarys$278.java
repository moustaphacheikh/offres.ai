package com.mccmr.ui;

import com.mccmr.entity.Rubriquemodel;
import java.util.ArrayList;

class salarys$278 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$278(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnSaveNJT.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      Double nombre = this.this$0.menu.fx.F01_NJT(this.this$0.selectedOne, this.this$0.menu.motifSN, this.this$0.menu.paramsGen.getPeriodeCourante());
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);

      for(Object o : new ArrayList(this.this$0.selectedOne.getPoste().getRubriquemodels())) {
         Rubriquemodel rs = (Rubriquemodel)o;
         Double base = rs.getMontant() / (double)30.0F;
         this.this$0.menu.pc.insertRubrique(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, rs.getRubrique(), this.this$0.menu.motifSN, base, nombre, true, false);
      }

      this.this$0.menu.viewMessage(this.this$0.msgLabel, "Model appliqu\u00e9 avec succ\u00e9!", false);
      this.this$0.afficherRubriquePaie();
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.btnSaveNJT.setEnabled(true);
   }
}
