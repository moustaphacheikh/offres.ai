package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.entity.Motif;
import com.mccmr.util.ModelClass;
import java.util.Date;

class EmployequeryFrame$27 extends Thread {
   // $FF: synthetic field
   final EmployequeryFrame this$0;

   EmployequeryFrame$27(final EmployequeryFrame var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      String suiteIdErr = "";
      Date periode = this.this$0.menu.paramsGen.getPeriodeCourante();
      Motif motif = (Motif)this.this$0.tMotif.getSelectedItem();

      for(int i = 0; i < this.this$0.resultTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 0)) {
            int idEmploye = ((Number)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 3)).intValue();
            Employe employe = this.this$0.menu.pc.employeById(idEmploye);
            double njt = ((Number)this.this$0.tNjt.getValue()).doubleValue();
            if (this.this$0.menu.pc.insertNJT(periode, employe, motif, njt)) {
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
