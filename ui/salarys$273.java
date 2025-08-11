package com.mccmr.ui;

import com.mccmr.entity.Rubrique;
import com.mccmr.entity.Rubriquepaie;

class salarys$273 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$273(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnSaveConges.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.progressBar.setStringPainted(false);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);

      try {
         if (this.this$0.menu.pc.insertConges(this.this$0.selectedOne, this.this$0.tDateDepart.getDate(), this.this$0.tDateReprise.getDate(), this.this$0.tRepriseEff.getDate(), this.this$0.tNoteConges.getText(), this.this$0.selectedConges)) {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Op\u00e9ration effecut\u00e9e avec succ\u00e9e!", false);
            this.this$0.afficherConges();
            this.this$0.tDernierDepart.setDate(this.this$0.menu.pc.dernierDepart(this.this$0.selectedOne));
            this.this$0.tCumulBrutNonImposableInitial.setValue(this.this$0.selectedOne.getCumulBrutNonImposableInitial());
            this.this$0.tCumulBrutImposableInitial.setValue(this.this$0.selectedOne.getCumulBrutImposableInitial());
            this.this$0.tCumulBrutImposable.setValue(this.this$0.menu.pc.cumulTypeById(this.this$0.selectedOne, "BI"));
            this.this$0.tCumulBrutNonImposable.setValue(this.this$0.menu.pc.cumulTypeById(this.this$0.selectedOne, "BNI"));
            this.this$0.tCumulRet.setValue(this.this$0.menu.pc.cumulTypeById(this.this$0.selectedOne, "RET"));
            if (this.this$0.cDroitsCongesAuto.isSelected()) {
               this.this$0.menu.pc.insertRubrique(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, this.this$0.menu.pc.usedRubID(9), this.this$0.menu.motifCNG, (Double)null, (Double)null, false, false);
               this.this$0.menu.pc.insertRubrique(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, this.this$0.menu.pc.usedRubID(10), this.this$0.menu.motifCNG, (Double)null, (Double)null, false, false);
               Rubrique rcrpRubSN = this.this$0.menu.pc.usedRubID(17);
               Rubrique rcrpRubCG = this.this$0.menu.pc.usedRubID(18);
               if (rcrpRubSN != null && rcrpRubCG != null) {
                  Rubriquepaie rcrp = this.this$0.menu.pc.rubriquePaieById(this.this$0.selectedOne, rcrpRubSN, this.this$0.menu.motifSN, this.this$0.menu.paramsGen.getPeriodeCourante());
                  if (rcrp != null) {
                     this.this$0.menu.pc.insertRubrique(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, rcrpRubCG, this.this$0.menu.motifCNG, rcrp.getBase(), rcrp.getNombre(), false, false);
                  }
               }

               this.this$0.afficherRubriquePaie();
            }
         } else {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Op\u00e9ration echou\u00e9e!", true);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.progressBar.setString("0%");
      this.this$0.progressBar.setStringPainted(true);
      this.this$0.btnSaveConges.setEnabled(true);
   }
}
