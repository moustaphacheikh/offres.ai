package com.mccmr.ui;

class repport$11 extends Thread {
   // $FF: synthetic field
   final repport this$0;

   repport$11(final repport var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.loadingPB.setIndeterminate(true);
      this.this$0.btnMenuEPC.setEnabled(false);
      this.this$0.menu.etatscumulesFrame.refresh();
      this.this$0.menu.openFrame(this.this$0.menu.etatscumulesFrame);
      this.this$0.btnMenuEPC.setEnabled(true);
      this.this$0.loadingPB.setIndeterminate(false);
   }
}
