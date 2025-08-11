package com.mccmr.ui;

class etatsCumul$12 extends Thread {
   // $FF: synthetic field
   final etatsCumul this$0;

   etatsCumul$12(final etatsCumul var1) {
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
