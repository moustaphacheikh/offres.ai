package com.mccmr.ui;

import java.io.IOException;
import java.util.Date;
import jxl.write.WriteException;

class etats$10 extends Thread {
   // $FF: synthetic field
   final etats this$0;

   etats$10(final etats var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnExport.setEnabled(false);

      try {
         if (this.this$0.menu.df.format((Date)this.this$0.tPeriode.getSelectedItem()).equalsIgnoreCase(this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()))) {
            this.this$0.excelListEngCourant(true);
         } else {
            this.this$0.excelListEng();
         }

         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Document export\u00e9 vers Excel", false);
      } catch (IOException e) {
         e.printStackTrace();
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
      } catch (WriteException e) {
         e.printStackTrace();
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
      }

      this.this$0.btnExport.setEnabled(true);
   }
}
