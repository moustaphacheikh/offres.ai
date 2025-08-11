package com.mccmr.ui;

class rubriques$31 extends Thread {
   // $FF: synthetic field
   final rubriques this$0;

   rubriques$31(final rubriques var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.afficherListe();
      this.this$0.progressBar.setIndeterminate(false);
   }
}
