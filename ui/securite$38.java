package com.mccmr.ui;

class securite$38 extends Thread {
   // $FF: synthetic field
   final securite this$0;

   securite$38(final securite var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.afficherListe();
      this.this$0.progressBar.setIndeterminate(false);
   }
}
