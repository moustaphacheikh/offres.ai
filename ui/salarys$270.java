package com.mccmr.ui;

class salarys$270 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$270(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.afficherListe();
      this.this$0.progressBar.setIndeterminate(false);
   }
}
