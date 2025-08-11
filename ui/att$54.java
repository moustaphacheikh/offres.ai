package com.mccmr.ui;

import java.io.IOException;
import jxl.write.WriteException;

class att$54 extends Thread {
   // $FF: synthetic field
   final att this$0;

   att$54(final att var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnExportMode2.setEnabled(false);

      try {
         this.this$0.exportExcelMode2(this.this$0.tBeginDateView.getDate(), this.this$0.tEndDateView.getDate());
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Document export\u00e9 vers Excel", false);
      } catch (IOException e) {
         e.printStackTrace();
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
      } catch (WriteException e) {
         e.printStackTrace();
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
      }

      this.this$0.btnExportMode2.setEnabled(true);
   }
}
