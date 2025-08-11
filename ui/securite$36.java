package com.mccmr.ui;

class securite$36 extends Thread {
   // $FF: synthetic field
   final securite this$0;

   securite$36(final securite var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnDelete.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
      String id = this.this$0.selectedOne.getLogin();
      if (this.this$0.menu.gl.deleteOcurance(this.this$0.selectedOne)) {
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Element supprim\u00e9", false);
         this.this$0.clearFields();
         this.this$0.dataListInit.removeIf((var1x) -> var1x.getLogin().equalsIgnoreCase(id));
         this.this$0.afficherListe();
      }

      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.btnDelete.setEnabled(true);
   }
}
