package com.mccmr.ui;

class declarations$8 extends Thread {
   // $FF: synthetic field
   final declarations this$0;

   declarations$8(final declarations var1) {
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
