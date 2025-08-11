package com.mccmr.ui;

class salarys$275 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$275(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.progressBar.setStringPainted(false);
      this.this$0.btnSaveHSDay.setEnabled(false);

      try {
         double primeEloinement = (double)0.0F;
         if (this.this$0.cSiteExterne.isSelected()) {
            primeEloinement = (double)1.0F;
         }

         this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
         if (this.this$0.menu.pc.insertJourHS(this.this$0.selectedOne, this.this$0.tDateJour.getDate(), ((Number)this.this$0.tHeuresJour.getValue()).doubleValue(), ((Number)this.this$0.tHeuresNuit.getValue()).doubleValue(), ((Number)this.this$0.tPrimesPanier.getValue()).doubleValue(), primeEloinement, this.this$0.cFerie100.isSelected(), this.this$0.cFerie50.isSelected(), this.this$0.cSiteExterne.isSelected(), this.this$0.tNoteJour.getText())) {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Op\u00e9ration effecut\u00e9e avec succ\u00e9!", false);
            this.this$0.afficherHeures();
         } else {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Op\u00e9ration echou\u00e9e!", true);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.btnSaveHSDay.setEnabled(true);
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.progressBar.setString("0%");
      this.this$0.progressBar.setStringPainted(true);
   }
}
