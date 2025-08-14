package com.mccmr.ui;

import java.io.IOException;
import jxl.write.WriteException;

class compta$13 extends Thread {
   // $FF: synthetic field
   final compta this$0;

   compta$13(final compta var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnExportExcel.setEnabled(false);

      try {
         this.this$0.excelCompta();
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Document export\u00e9", false);
      } catch (IOException e) {
         e.printStackTrace();
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
      } catch (WriteException e) {
         e.printStackTrace();
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
      }

      this.this$0.btnExportExcel.setEnabled(true);
   }
}
