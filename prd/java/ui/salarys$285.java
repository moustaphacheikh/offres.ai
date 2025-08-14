package com.mccmr.ui;

class salarys$285 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$285(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      if (this.this$0.cOnMotif.isSelected()) {
         this.this$0.afficherRubriquePaie();
      }

      this.this$0.progressBar.setIndeterminate(false);
   }
}
