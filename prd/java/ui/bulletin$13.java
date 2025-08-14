package com.mccmr.ui;

class bulletin$13 extends Thread {
   // $FF: synthetic field
   final bulletin this$0;

   bulletin$13(final bulletin var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar1.setIndeterminate(true);
      this.this$0.afficherListe();
      this.this$0.progressBar1.setIndeterminate(false);
   }
}
