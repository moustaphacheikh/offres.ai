package com.mccmr.ui;

class fileImport$20 extends Thread {
   // $FF: synthetic field
   final fileImport this$0;

   fileImport$20(final fileImport var1) {
      this.this$0 = this$0;
   }

   public void run() {
      if (!this.this$0.tFile_DonneesPersonnel.getText().isEmpty()) {
         String inputFile = this.this$0.tFile_DonneesPersonnel.getText();
         this.this$0.menu.excel.setInputFile(inputFile);

         try {
            this.this$0.menu.excel.importeDonneesPersonnel(this.this$0.tLineNum_DataBegin1.getValue() != null ? ((Number)this.this$0.tLineNum_DataBegin1.getValue()).intValue() : null, this.this$0.tColNum_IDSAL1.getValue() != null ? ((Number)this.this$0.tColNum_IDSAL1.getValue()).intValue() : null, this.this$0.tColNum_DATA.getValue() != null ? ((Number)this.this$0.tColNum_DATA.getValue()).intValue() : null, this.this$0.tDataTerget.getSelectedItem().toString(), this.this$0.progressBar);
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
