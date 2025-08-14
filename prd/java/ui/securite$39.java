package com.mccmr.ui;

class securite$39 extends Thread {
   // $FF: synthetic field
   final securite this$0;

   securite$39(final securite var1) {
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
