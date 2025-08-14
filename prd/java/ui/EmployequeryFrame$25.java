package com.mccmr.ui;

import com.mccmr.entity.Conges;
import com.mccmr.entity.Employe;
import com.mccmr.entity.Motif;
import com.mccmr.util.ModelClass;
import java.util.Date;

class EmployequeryFrame$25 extends Thread {
   // $FF: synthetic field
   final EmployequeryFrame this$0;

   EmployequeryFrame$25(final EmployequeryFrame var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      String suiteIdErr = "";
      Date periode = this.this$0.menu.paramsGen.getPeriodeCourante();
      Motif motifConge = this.this$0.menu.motifCNG;
      Motif motifSN = this.this$0.menu.motifSN;

      for(int i = 0; i < this.this$0.resultTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 0)) {
            Employe employe = this.this$0.menu.pc.employeById(((Number)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 3)).intValue());
            if (this.this$0.tDecompteType.getSelectedItem().toString().compareTo("D\u00e9compte de cong\u00e9s") == 0) {
               if (this.this$0.cDroitsConges.isSelected()) {
                  this.this$0.menu.pc.insertRubrique(periode, employe, this.this$0.menu.pc.usedRubID(9), motifSN, (Double)null, (Double)null, false, false);
                  this.this$0.menu.pc.insertRubrique(periode, employe, this.this$0.menu.pc.usedRubID(10), motifSN, (Double)null, (Double)null, false, false);
               }

               if (this.this$0.menu.pc.insertConges(employe, this.this$0.tDateDepart.getDate(), this.this$0.tDateReprise.getDate(), this.this$0.tDateReprise.getDate(), this.this$0.tNoteConges.getText(), (Conges)null)) {
               }
            } else {
               employe.setEnDebauche(true);
               employe.setDateDebauche(this.this$0.tDateDepart.getDate());
               if (this.this$0.menu.gl.updateOcurance(employe)) {
                  if (this.this$0.cIndPreavis.isSelected()) {
                     this.this$0.menu.pc.insertRubrique(periode, employe, this.this$0.menu.pc.usedRubID(11), motifConge, (Double)null, (Double)null, false, false);
                  }

                  if (this.this$0.cIndLicenciement.isSelected()) {
                     this.this$0.menu.pc.insertRubrique(periode, employe, this.this$0.menu.pc.usedRubID(12), motifConge, (Double)null, (Double)null, false, false);
                  }

                  if (this.this$0.cIndLicenciementCollectif.isSelected()) {
                     this.this$0.menu.pc.insertRubrique(periode, employe, this.this$0.menu.pc.usedRubID(13), motifConge, (Double)null, (Double)null, false, false);
                  }

                  if (this.this$0.cIndRetraite.isSelected()) {
                     this.this$0.menu.pc.insertRubrique(periode, employe, this.this$0.menu.pc.usedRubID(14), motifConge, (Double)null, (Double)null, false, false);
                  }

                  if (this.this$0.cDroitsConges.isSelected()) {
                     this.this$0.menu.pc.insertRubrique(periode, employe, this.this$0.menu.pc.usedRubID(9), motifConge, (Double)null, (Double)null, false, false);
                     this.this$0.menu.pc.insertRubrique(periode, employe, this.this$0.menu.pc.usedRubID(10), motifConge, (Double)null, (Double)null, false, false);
                  }
               }
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
