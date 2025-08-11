package com.mccmr.ui;

import com.mccmr.entity.Banque;
import java.io.IOException;
import java.util.Date;
import jxl.write.WriteException;

class virements$6 extends Thread {
   // $FF: synthetic field
   final virements this$0;

   virements$6(final virements var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnExport.setEnabled(false);

      try {
         this.this$0.excelVirements((Date)this.this$0.tPeriode.getSelectedItem(), ((Banque)this.this$0.tBanque.getSelectedItem()).getId());
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
