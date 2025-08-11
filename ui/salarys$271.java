package com.mccmr.ui;

class salarys$271 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$271(final salarys var1) {
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
