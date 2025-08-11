package com.mccmr.ui;

import java.util.Date;

class att$49 extends Thread {
   // $FF: synthetic field
   final att this$0;

   att$49(final att var1) {
      this.this$0 = this$0;
   }

   public void run() {
      if (this.this$0.menu.pc.deletePointageFromDateAll(this.this$0.cSuppImporte.isSelected(), this.this$0.tBeginDate.getDate())) {
         this.this$0.progressBar.setIndeterminate(true);

         try {
            this.this$0.readPointageFromExcel(this.this$0.tBeginDate.getDate(), this.this$0.tDateFormat.getSelectedItem().toString(), this.this$0.tLineNum_DataBegin.getValue() != null ? ((Number)this.this$0.tLineNum_DataBegin.getValue()).intValue() : null, this.this$0.tColNum_IDSAL.getValue() != null ? ((Number)this.this$0.tColNum_IDSAL.getValue()).intValue() : null, this.this$0.tColNum_DATE.getValue() != null ? ((Number)this.this$0.tColNum_DATE.getValue()).intValue() : null, this.this$0.tColNum_TIME.getValue() != null ? ((Number)this.this$0.tColNum_TIME.getValue()).intValue() : null, this.this$0.tColNum_IO.getValue() != null ? ((Number)this.this$0.tColNum_IO.getValue()).intValue() : null);
            if (this.this$0.tBeginDateView.isValid() && this.this$0.tEndDateView.isValid()) {
               this.this$0.afficherListe(this.this$0.tBeginDateView.getDate(), this.this$0.tEndDateView.getDate());
            } else {
               this.this$0.afficherListe((Date)null, (Date)null);
            }

            this.this$0.jTabbedPane1.setSelectedIndex(1);
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Document import\u00e9 avec succ\u00e9", false);
            this.this$0.afficherListe((Date)null, (Date)null);
         } catch (Exception e) {
            e.printStackTrace();
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Importation echou\u00e9", true);
         }

         this.this$0.progressBar.setIndeterminate(false);
      }

   }
}
