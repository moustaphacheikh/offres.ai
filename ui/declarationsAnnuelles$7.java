package com.mccmr.ui;

class declarationsAnnuelles$7 extends Thread {
   // $FF: synthetic field
   final declarationsAnnuelles this$0;

   declarationsAnnuelles$7(final declarationsAnnuelles var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.btnRefresh.setEnabled(false);
      this.this$0.refresh();
      this.this$0.btnRefresh.setEnabled(true);
      this.this$0.progressBar.setIndeterminate(false);
   }
}
