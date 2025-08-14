package com.mccmr.ui;

import java.util.Date;

class salarys$284 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$284(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnSaveNJT.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
      if (this.this$0.tNjt.getValue() != null) {
         try {
            Date periode = this.this$0.menu.paramsGen.getPeriodeCourante();
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
            double njt = ((Number)this.this$0.tNjt.getValue()).doubleValue();
            if (this.this$0.menu.pc.insertNJT(periode, this.this$0.selectedOne, this.this$0.selectedMotif, njt)) {
               this.this$0.menu.pc.updateRubriquePaie(periode, this.this$0.selectedOne, this.this$0.selectedMotif);
               this.this$0.afficherRubriquePaie();
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "NJT mis \u00e0 jour avec succ\u00e9!", false);
            } else {
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: NJT non valid\u00e9!", true);
            }
         } catch (Exception e) {
            e.printStackTrace();
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: NJT non valid\u00e9!", true);
         }
      } else {
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: NJT invalide!", true);
      }

      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.btnSaveNJT.setEnabled(true);
   }
}
