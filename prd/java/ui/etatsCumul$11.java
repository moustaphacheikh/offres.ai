package com.mccmr.ui;

import java.io.IOException;
import jxl.write.WriteException;

class etatsCumul$11 extends Thread {
   // $FF: synthetic field
   final etatsCumul this$0;

   etatsCumul$11(final etatsCumul var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnExport.setEnabled(false);

      try {
         this.this$0.excelMasseSal();
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
