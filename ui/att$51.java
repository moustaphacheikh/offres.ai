package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.util.ModelClass;
import java.util.Date;

class att$51 extends Thread {
   // $FF: synthetic field
   final att this$0;

   att$51(final att var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setMaximum(this.this$0.listTable.getRowCount());
      this.this$0.progressBar.setMinimum(0);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
      long salariesCount = 0L;

      for(int i = 0; i < this.this$0.listTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmPointage)this.this$0.listTable.getModel()).getValueAt(i, 0)) {
            this.this$0.selectedIDP = ((Number)this.this$0.listTable.getValueAt(i, 3)).intValue();
            Employe e = this.this$0.menu.pc.employeByIDP((long)this.this$0.selectedIDP);
            this.this$0.menu.pc.deleteValoriserHS(e);
            if (this.this$0.menu.pc.deletePointageFromDate(this.this$0.tBeginDateView.getDate(), (long)this.this$0.selectedIDP)) {
               this.this$0.menu.pc.deleteJourFromDate(this.this$0.tBeginDateView.getDate(), (long)e.getId());
               ++salariesCount;
            }
         }

         this.this$0.progressBar.setValue(i);
      }

      this.this$0.progressBar.setValue(100);
      this.this$0.afficherListe((Date)null, (Date)null);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, salariesCount + " Salari\u00e9(s) vid\u00e9(s) avec sucss\u00e9!", false);
   }
}
