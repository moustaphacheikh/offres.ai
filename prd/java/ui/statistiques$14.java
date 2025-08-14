package com.mccmr.ui;

class statistiques$14 extends Thread {
   // $FF: synthetic field
   final statistiques this$0;

   statistiques$14(final statistiques var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      if (this.this$0.tPeriodeDu.getItemCount() > 0 && this.this$0.tPeriodeAu.getItemCount() > 0 && this.this$0.tMotif.getItemCount() > 0 && this.this$0.tRubrique.getItemCount() > 0) {
         this.this$0.afficherListe();
      }

      this.this$0.progressBar.setIndeterminate(false);
   }
}
