package com.mccmr.ui;

import com.mccmr.entity.Motif;
import com.mccmr.util.GeneralLib;
import javax.swing.event.CaretEvent;

class salarys$280 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$280(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.progressBar.setStringPainted(false);
      this.this$0.btnCalPaie.setEnabled(false);

      try {
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
         if (this.this$0.menu.dialect.toString().contains("Oracle")) {
            GeneralLib var10000 = this.this$0.menu.gl;
            int var10001 = this.this$0.selectedOne.getId();
            var10000.exQuery("delete from Paie where employe=" + var10001 + " and periode=TO_DATE('" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
         } else {
            GeneralLib var4 = this.this$0.menu.gl;
            int var5 = this.this$0.selectedOne.getId();
            var4.exQuery("delete from Paie where employe=" + var5 + " and periode='" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "' ");
         }

         for(Motif motif : this.this$0.menu.stricturesIF.dataListInit_Motifs) {
            this.this$0.menu.pc.insertPaie(this.this$0.selectedOne, motif, this.this$0.tPaieDu.getDate(), this.this$0.tPaieAu.getDate());
         }

         this.this$0.tBudgetAnuelCaretUpdate((CaretEvent)null);
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Paie valid\u00e9e avec succ\u00e9!", false);
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.btnCalPaie.setEnabled(true);
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.progressBar.setString("0%");
      this.this$0.progressBar.setStringPainted(true);
   }
}
