package com.mccmr.ui;

import com.mccmr.entity.Motif;

class salarys$286 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$286(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.selectedMotif = (Motif)this.this$0.tMotif.getSelectedItem();
      if (this.this$0.cOnMotif.isSelected()) {
         this.this$0.afficherRubriquePaie();
      }

      this.this$0.progressBar.setIndeterminate(false);
   }
}
