package com.mccmr.ui;

class repport$9 extends Thread {
   // $FF: synthetic field
   final repport this$0;

   repport$9(final repport var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.loadingPB.setIndeterminate(true);
      this.this$0.btnMenuBS.setEnabled(false);
      this.this$0.menu.bulletinpaieFrame.refresh();
      this.this$0.menu.openFrame(this.this$0.menu.bulletinpaieFrame);
      this.this$0.btnMenuBS.setEnabled(true);
      this.this$0.loadingPB.setIndeterminate(false);
   }
}
