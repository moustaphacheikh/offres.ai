package com.mccmr.ui;

import java.io.IOException;
import jxl.write.WriteException;

class etats$13 extends Thread {
   // $FF: synthetic field
   final etats this$0;

   etats$13(final etats var1) {
      this.this$0 = this$0;
   }

   public void run() {
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

   }
}
