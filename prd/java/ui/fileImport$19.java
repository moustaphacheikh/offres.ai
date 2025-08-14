package com.mccmr.ui;

import com.mccmr.entity.Motif;

class fileImport$19 extends Thread {
   // $FF: synthetic field
   final fileImport this$0;

   fileImport$19(final fileImport var1) {
      this.this$0 = this$0;
   }

   public void run() {
      if (!this.this$0.tFile_RubriquePaie.getText().isEmpty()) {
         String inputFile = this.this$0.tFile_RubriquePaie.getText();
         this.this$0.menu.excel.setInputFile(inputFile);

         try {
            Motif motif = (Motif)this.this$0.tMotif.getSelectedItem();
            this.this$0.menu.excel.importeRubriques(this.this$0.tLineNum_DataBegin.getValue() != null ? ((Number)this.this$0.tLineNum_DataBegin.getValue()).intValue() : null, this.this$0.tColNum_IDSAL.getValue() != null ? ((Number)this.this$0.tColNum_IDSAL.getValue()).intValue() : null, this.this$0.tColNum_IDRUB.getValue() != null ? ((Number)this.this$0.tColNum_IDRUB.getValue()).intValue() : null, this.this$0.tColNum_BASE.getValue() != null ? ((Number)this.this$0.tColNum_BASE.getValue()).intValue() : null, this.this$0.tColNum_NBRE.getValue() != null ? ((Number)this.this$0.tColNum_NBRE.getValue()).intValue() : null, motif, this.this$0.cFixe.isSelected(), this.this$0.progressBar);
            this.this$0.afficherListe();
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Document import\u00e9 avec succ\u00e9", false);
         } catch (Exception e) {
            e.printStackTrace();
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Importation echou\u00e9. Veriffier que le fichier Excel est au format 97-2003 (.xls)", true);
         }
      } else {
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Aucun fichier selection\u00e9", true);
      }

   }
}
