package com.mccmr.ui;

import com.mccmr.entity.Banque;
import com.mccmr.entity.Departement;
import com.mccmr.entity.Employe;
import com.mccmr.entity.Grillesalairebase;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Origines;
import com.mccmr.entity.Poste;
import com.mccmr.util.ModelClass;
import java.util.Date;

class EmployequeryFrame$30 extends Thread {
   // $FF: synthetic field
   final EmployequeryFrame this$0;

   EmployequeryFrame$30(final EmployequeryFrame var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      String suiteIdErr = "";
      Motif motifSN = this.this$0.menu.motifSN;

      for(int i = 0; i < this.this$0.resultTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 0)) {
            int idEmploye = ((Number)((ModelClass.tmEmployequery)this.this$0.resultTable.getModel()).getValueAt(i, 3)).intValue();
            Employe employe = this.this$0.menu.pc.employeById(idEmploye);
            if (this.this$0.cMAJActifOK.isSelected()) {
               employe.setActif(this.this$0.cMAJActif.isSelected());
            }

            if (this.this$0.cMAJAvancmentAutoCatOK.isSelected()) {
               employe.setAvancementCategorieAuto(this.this$0.cMAJAvancmentAutoCat.isSelected());
            }

            if (this.this$0.cMAJDetacheCNAMOK.isSelected()) {
               employe.setDetacheCnam(this.this$0.cMAJDetacheCNAM.isSelected());
            }

            if (this.this$0.cMAJDetacheCNSSOK.isSelected()) {
               employe.setDetacheCnss(this.this$0.cMAJDetacheCNSS.isSelected());
            }

            if (this.this$0.cMAJDomicilieOK.isSelected()) {
               employe.setDomicilie(this.this$0.cMAJDomicilie.isSelected());
            }

            if (this.this$0.cMAJEnCongesOK.isSelected()) {
               employe.setEnConge(this.this$0.cMAJEnConges.isSelected());
            }

            if (this.this$0.cMAJExonoreITSOK.isSelected()) {
               employe.setExonoreIts(this.this$0.cMAJExonoreITS.isSelected());
            }

            if (this.this$0.cMAJBanqueOK.isSelected()) {
               employe.setBanque((Banque)this.this$0.tMAJBanque.getSelectedItem());
            }

            if (this.this$0.cMAJCategorieOK.isSelected()) {
               Date periode = this.this$0.menu.paramsGen.getPeriodeCourante();
               employe.setGrillesalairebase((Grillesalairebase)this.this$0.tMAJCategorie.getSelectedItem());
               if (this.this$0.cAppSBCatgeorie.isSelected()) {
                  Double nombre = this.this$0.menu.fx.F01_NJT(employe, motifSN, periode);
                  Double base = ((Grillesalairebase)this.this$0.tMAJCategorie.getSelectedItem()).getSalaireBase() / (double)30.0F;
                  if (this.this$0.menu.pc.insertRubrique(periode, employe, this.this$0.menu.pc.usedRubID(1), motifSN, base, nombre, true, false)) {
                     this.this$0.menu.pc.updateRubriquePaie(periode, employe, motifSN);
                  }
               }
            }

            if (this.this$0.cMAJDateFinContratOK.isSelected()) {
               employe.setDateFinContrat(this.this$0.tMAJDateFinContrat.getDate());
            }

            if (this.this$0.cMAJPosteOK.isSelected()) {
               employe.setDepartement((Departement)this.this$0.tMAJDepartement.getSelectedItem());
            }

            if (this.this$0.cMAJHeureSemaineOK.isSelected()) {
               employe.setContratHeureSemaine(((Number)this.this$0.tMAJHeureSemaine.getValue()).doubleValue());
            }

            if (this.this$0.cMAJModePaiementOK.isSelected()) {
               employe.setModePaiement(this.this$0.tMAJModePaiement.getSelectedItem().toString());
            }

            if (this.this$0.cMAJNbMoisPreavisOK.isSelected()) {
               employe.setNbMoisPreavis((float)(Long)this.this$0.tMAJNbMoisPreavis.getValue());
            }

            if (this.this$0.cMAJPosteOK.isSelected()) {
               employe.setPoste((Poste)this.this$0.tMAJPoste.getSelectedItem());
            }

            if (this.this$0.cMAJSexeOK.isSelected()) {
               employe.setSexe(this.this$0.tMAJSexe.getSelectedItem().toString());
            }

            if (this.this$0.cMAJSituationFamOK.isSelected()) {
               employe.setSituationFamiliale(this.this$0.tMAJSituationFam.getSelectedItem().toString());
            }

            if (this.this$0.cMAJTypeContratOK.isSelected()) {
               employe.setTypeContrat(this.this$0.tMAJTypeContrat.getSelectedItem().toString());
            }

            if (this.this$0.cMAJZoneOrigineOK.isSelected()) {
               employe.setOrigines((Origines)this.this$0.tMAJZoneOrigine.getSelectedItem());
            }

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
