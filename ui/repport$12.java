package com.mccmr.ui;

class repport$12 extends Thread {
   // $FF: synthetic field
   final repport this$0;

   repport$12(final repport var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.loadingPB.setIndeterminate(true);
      this.this$0.btnMenuDM3.setEnabled(false);
      this.this$0.menu.comptaFrame.refresh();
      this.this$0.menu.openFrame(this.this$0.menu.comptaFrame);
      this.this$0.btnMenuDM3.setEnabled(true);
      this.this$0.loadingPB.setIndeterminate(false);
   }
}
