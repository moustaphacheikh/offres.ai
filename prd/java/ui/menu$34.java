package com.mccmr.ui;

class menu$34 extends Thread {
   // $FF: synthetic field
   final menu this$0;

   menu$34(final menu var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.btnMenuEmployes.setEnabled(false);
      this.this$0.employeFrame.setVisible(true);
      if (!this.this$0.firstRefresh) {
         this.this$0.employeFrame.refresh();
         this.this$0.employeFrame.afficherListe();
      }

      this.this$0.jDesktopPane.remove(this.this$0.employeFrame);
      this.this$0.jDesktopPane.add(this.this$0.employeFrame);
      this.this$0.employeFrame.toFront();
      this.this$0.pnlMenu.setVisible(false);
      this.this$0.btnMenuEmployes.setEnabled(true);
      this.this$0.progressBar.setIndeterminate(false);
   }
}
