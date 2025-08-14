package com.mccmr.ui;

class compta$14 extends Thread {
   // $FF: synthetic field
   final compta this$0;

   compta$14(final compta var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.genPCButton.setEnabled(false);
      this.this$0.ProgressBar.setIndeterminate(true);
      this.this$0.genPC();
      this.this$0.ProgressBar.setIndeterminate(false);
      this.this$0.genPCButton.setEnabled(true);
   }
}
