package com.mccmr.ui;

import java.io.IOException;
import jxl.write.WriteException;

class statistiques$15 extends Thread {
   // $FF: synthetic field
   final statistiques this$0;

   statistiques$15(final statistiques var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);

      try {
         this.this$0.toExcel();
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Document export\u00e9 vers Excel", false);
      } catch (IOException e) {
         e.printStackTrace();
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
      } catch (WriteException e) {
         e.printStackTrace();
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
      }

      this.this$0.progressBar.setIndeterminate(false);
   }
}
