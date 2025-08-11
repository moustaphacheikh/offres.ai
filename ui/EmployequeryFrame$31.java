package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.entity.Motif;
import com.mccmr.util.ModelClass;

class EmployequeryFrame$31 extends Thread {
   // $FF: synthetic field
   final EmployequeryFrame this$0;

   EmployequeryFrame$31(final EmployequeryFrame var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      String suiteIdErr = "";
      Motif motifSN = this.this$0.menu.motifSN;

      for(int i = 0; i < this.this$0.resultTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 0)) {
            int idEmploye = ((Number)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 3)).intValue();
            Employe selectedOne = this.this$0.menu.pc.employeById(idEmploye);
            selectedOne.setLundiDs(this.this$0.cLUNBegin.isSelected());
            selectedOne.setLundiFs(this.this$0.cLUNEnd.isSelected());
            selectedOne.setLundiWe(this.this$0.cLUNwe.isSelected());
            selectedOne.setMardiDs(this.this$0.cMARBegin.isSelected());
            selectedOne.setMardiFs(this.this$0.cMAREnd.isSelected());
            selectedOne.setMardiWe(this.this$0.cMARwe.isSelected());
            selectedOne.setMercrediDs(this.this$0.cMERBegin.isSelected());
            selectedOne.setMercrediFs(this.this$0.cMEREnd.isSelected());
            selectedOne.setMercrediWe(this.this$0.cMERwe.isSelected());
            selectedOne.setJeudiDs(this.this$0.cJEUBegin.isSelected());
            selectedOne.setJeudiFs(this.this$0.cJEUEnd.isSelected());
            selectedOne.setJeudiWe(this.this$0.cJEUwe.isSelected());
            selectedOne.setVendrediDs(this.this$0.cVENBegin.isSelected());
            selectedOne.setVendrediFs(this.this$0.cVENEnd.isSelected());
            selectedOne.setVendrediWe(this.this$0.cVENwe.isSelected());
            selectedOne.setSamediDs(this.this$0.cSAMBegin.isSelected());
            selectedOne.setSamediFs(this.this$0.cSAMEnd.isSelected());
            selectedOne.setSamediWe(this.this$0.cSAMwe.isSelected());
            selectedOne.setDimancheDs(this.this$0.cDIMBegin.isSelected());
            selectedOne.setDimancheFs(this.this$0.cDIMEnd.isSelected());
            selectedOne.setDimancheWe(this.this$0.cDIMwe.isSelected());
            if (!this.this$0.menu.gl.updateOcurance(selectedOne)) {
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
