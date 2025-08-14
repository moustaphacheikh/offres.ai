package com.mccmr.ui;

import com.mccmr.entity.Rubrique;

class salarys$283 extends Thread {
   // $FF: synthetic field
   final Rubrique val$rubrique;
   // $FF: synthetic field
   final salarys this$0;

   salarys$283(final salarys var1, final Rubrique var2) {
      this.val$rubrique = var2;
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnSavePaie.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.progressBar.setStringPainted(false);
      double base = ((Number)this.this$0.tBase.getValue()).doubleValue();
      double nombre = ((Number)this.this$0.tNombre.getValue()).doubleValue();

      try {
         if (this.this$0.menu.pc.retAEActive(this.this$0.selectedOne, this.val$rubrique)) {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Retenue ger\u00e9e automatiquement!", true);
            this.this$0.progressBar.setIndeterminate(false);
            this.this$0.progressBar.setString("0%");
            this.this$0.progressBar.setStringPainted(true);
            this.this$0.btnSavePaie.setEnabled(true);
         } else if (this.this$0.menu.pc.insertRubrique(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, this.val$rubrique, this.this$0.selectedMotif, base, nombre, this.this$0.cFixe.isSelected(), false)) {
            if (!this.val$rubrique.getRubriqueformules().isEmpty()) {
               this.this$0.menu.pc.updateRubriquePaie(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, this.this$0.selectedMotif);
            }

            this.this$0.afficherRubriquePaie();
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Validation de la rubrique avec succ\u00e9!", false);
         } else {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Rubrique non valid\u00e9e!", true);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.btnSavePaie.setEnabled(true);
   }
}
