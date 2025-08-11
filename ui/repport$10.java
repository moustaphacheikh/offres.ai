package com.mccmr.ui;

class repport$10 extends Thread {
   // $FF: synthetic field
   final repport this$0;

   repport$10(final repport var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.loadingPB.setIndeterminate(true);
      this.this$0.btnMenuEP.setEnabled(false);
      this.this$0.menu.etatsFrame.refresh();
      this.this$0.menu.openFrame(this.this$0.menu.etatsFrame);
      this.this$0.btnMenuEP.setEnabled(true);
      this.this$0.loadingPB.setIndeterminate(false);
   }
}
