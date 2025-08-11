package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.util.ModelClass;

class EmployequeryFrame$26 extends Thread {
   // $FF: synthetic field
   final EmployequeryFrame this$0;

   EmployequeryFrame$26(final EmployequeryFrame var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      String suiteIdErr = "";

      for(int i = 0; i < this.this$0.resultTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 0)) {
            int idEmploye = ((Number)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 3)).intValue();
            Employe employe = this.this$0.menu.pc.employeById(idEmploye);
            employe.setNoteSurBulletin(this.this$0.tNoteSurBulletin.getText());
            if (!this.this$0.menu.gl.updateOcurance(employe)) {
               suiteIdErr = suiteIdErr + " " + idEmploye + ", ";
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
