package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Rubrique;
import com.mccmr.util.ModelClass;
import java.util.Date;

class EmployequeryFrame$28 extends Thread {
   // $FF: synthetic field
   final EmployequeryFrame this$0;

   EmployequeryFrame$28(final EmployequeryFrame var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      Double base = null;
      Double nombre = null;
      Date periode = this.this$0.menu.paramsGen.getPeriodeCourante();
      Rubrique rub = (Rubrique)this.this$0.tRubrique.getSelectedItem();
      Motif motif = (Motif)this.this$0.tMotif.getSelectedItem();
      String suiteIdErr = "";
      if (this.this$0.cSuppRubrique.isSelected()) {
         base = (double)0.0F;
         nombre = (double)0.0F;
         this.this$0.cFixe.setSelected(false);
      } else {
         if (!rub.isBaseAuto()) {
            base = ((Number)this.this$0.tBase.getValue()).doubleValue();
         }

         if (!rub.isNombreAuto()) {
            nombre = ((Number)this.this$0.tNombre.getValue()).doubleValue();
         }
      }

      for(int i = 0; i < this.this$0.resultTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 0)) {
            Employe employe = this.this$0.menu.pc.employeById(((Number)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 3)).intValue());
            if (this.this$0.menu.pc.insertRubrique(periode, employe, rub, motif, base, nombre, this.this$0.cFixe.isSelected(), false) && !rub.isBaseAuto()) {
               this.this$0.menu.pc.updateRubriquePaie(periode, employe, motif);
               this.this$0.menu.pc.updateRubriquePaie(periode, employe, motif);
            }
         }
      }

      if (suiteIdErr.length() == 0) {
         this.this$0.msgInfoLabel.setText("Op\u00e9ration reussie avec succ\u00e9!");
      } else {
         this.this$0.msgErrLabel.setText("Err: Operation echou\u00e9e sur IDs: " + suiteIdErr);
      }

      this.this$0.afficherResultat();
      this.this$0.progressBar.setIndeterminate(false);
   }
}
