package com.mccmr.ui;

class EmployequeryFrame$29 extends Thread {
   // $FF: synthetic field
   final EmployequeryFrame this$0;

   EmployequeryFrame$29(final EmployequeryFrame var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.afficherResultat();
      this.this$0.progressBar.setIndeterminate(false);
   }
}
