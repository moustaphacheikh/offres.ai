package com.mccmr.ui;

class menu$35 extends Thread {
   // $FF: synthetic field
   final menu this$0;

   menu$35(final menu var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.employequeryFrame.setEnabled(false);
      this.this$0.employequeryFrame.setVisible(true);
      this.this$0.jDesktopPane.remove(this.this$0.employequeryFrame);
      this.this$0.jDesktopPane.add(this.this$0.employequeryFrame);
      if (!this.this$0.firstRefresh) {
         this.this$0.employequeryFrame.refresh();
      }

      this.this$0.employequeryFrame.toFront();
      this.this$0.pnlMenu.setVisible(false);
      this.this$0.employequeryFrame.setEnabled(true);
      this.this$0.progressBar.setIndeterminate(false);
   }
}
