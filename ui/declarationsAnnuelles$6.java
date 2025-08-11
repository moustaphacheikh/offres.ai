package com.mccmr.ui;

import java.io.IOException;
import jxl.write.WriteException;

class declarationsAnnuelles$6 extends Thread {
   // $FF: synthetic field
   final declarationsAnnuelles this$0;

   declarationsAnnuelles$6(final declarationsAnnuelles var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
      switch (this.this$0.tEtat.getSelectedItem().toString()) {
         case "D\u00e9claration annuelle de l'ITS":
            try {
               this.this$0.declarationAnuelleITS();
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Document export\u00e9 vers Excel", false);
            } catch (IOException e) {
               e.printStackTrace();
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
            } catch (WriteException e) {
               e.printStackTrace();
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
            }
            break;
         case "Taxe d'apprentissage exercice courant":
            this.this$0.decTA();
      }

      this.this$0.progressBar.setIndeterminate(false);
   }
}
