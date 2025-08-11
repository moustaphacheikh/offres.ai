package com.mccmr.ui;

class att$50 extends Thread {
   // $FF: synthetic field
   final att this$0;

   att$50(final att var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.afficherListe(this.this$0.tBeginDateView.getDate(), this.this$0.tEndDateView.getDate());
      this.this$0.progressBar.setIndeterminate(false);
   }
}
