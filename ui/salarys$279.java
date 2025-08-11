package com.mccmr.ui;

import com.mccmr.entity.Grillesalairebase;

class salarys$279 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$279(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnSaveNJT.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      Double nombre = this.this$0.menu.fx.F01_NJT(this.this$0.selectedOne, this.this$0.menu.motifSN, this.this$0.menu.paramsGen.getPeriodeCourante());
      Grillesalairebase gsb = (Grillesalairebase)this.this$0.tCategorie.getSelectedItem();
      Double base = gsb.getSalaireBase() / (double)30.0F;
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
      if (this.this$0.menu.pc.insertRubrique(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, this.this$0.menu.pc.usedRubID(1), this.this$0.menu.motifSN, base, nombre, true, false)) {
         this.this$0.menu.pc.updateRubriquePaie(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, this.this$0.menu.motifSN);
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "SB mis \u00e0 jour avec succ\u00e9!", false);
         this.this$0.afficherRubriquePaie();
      } else {
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: mis \u00e0 jour SB!", true);
      }

      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.btnSaveNJT.setEnabled(true);
   }
}
