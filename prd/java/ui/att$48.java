package com.mccmr.ui;

class att$48 extends Thread {
   // $FF: synthetic field
   final att this$0;

   att$48(final att var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.menu.pc.deletePointageFromDateAll(this.this$0.cSuppImporte.isSelected(), this.this$0.tBeginDate.getDate());
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "Op\u00e9ration termin\u00e9e avec sucss\u00e9!", false);
   }
}
