package com.mccmr.ui;

import com.mccmr.entity.Rubrique;

class salarys$282 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$282(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.progressBar.setStringPainted(false);
      this.this$0.btnDelPaie.setEnabled(false);

      try {
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
         Rubrique rubrique = (Rubrique)this.this$0.tRubriques.getSelectedItem();
         if (this.this$0.menu.pc.retAEActive(this.this$0.selectedOne, rubrique)) {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Retenue ger\u00e9e automatiquement!", true);
         } else {
            this.this$0.menu.pc.insertRubrique(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, rubrique, this.this$0.selectedMotif, (double)0.0F, (double)0.0F, true, false);
            this.this$0.menu.pc.updateRubriquePaie(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, this.this$0.selectedMotif);
            this.this$0.menu.pc.updateRubriquePaie(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, this.this$0.selectedMotif);
            this.this$0.afficherRubriquePaie();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.btnDelPaie.setEnabled(true);
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.progressBar.setString("0%");
      this.this$0.progressBar.setStringPainted(true);
   }
}
