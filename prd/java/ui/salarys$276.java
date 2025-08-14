package com.mccmr.ui;

import com.mccmr.entity.Motif;
import com.mccmr.entity.Rubrique;

class salarys$276 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$276(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.progressBar.setStringPainted(false);
      this.this$0.btnSaveRappelSB.setEnabled(false);

      try {
         if ((Double)this.this$0.tMontantRappel.getValue() != (double)0.0F) {
            double base = ((Number)this.this$0.tMontantRappel.getValue()).doubleValue();
            int idRub = ((Number)this.this$0.tIDRubRappel.getValue()).intValue();
            int idRubRappel = 1000 + idRub;
            Motif motif = (Motif)this.this$0.tMotifRappel.getSelectedItem();
            Rubrique rubRappel = this.this$0.menu.pc.rubriqueById(idRubRappel);
            Rubrique rubriqueARappeler = this.this$0.menu.pc.rubriqueById(idRub);
            if (rubRappel == null) {
               rubRappel = new Rubrique();
               rubRappel.setId(idRubRappel);
               rubRappel.setAvantagesNature(rubriqueARappeler.isAvantagesNature());
               rubRappel.setBaseAuto(false);
               rubRappel.setCnam(rubriqueARappeler.isCnam());
               rubRappel.setCnss(rubriqueARappeler.isCnss());
               rubRappel.setCumulable(rubriqueARappeler.isCumulable());
               rubRappel.setDeductionDu(rubriqueARappeler.getDeductionDu());
               rubRappel.setIts(rubriqueARappeler.isIts());
               rubRappel.setLibelle("RAPPEL-" + rubriqueARappeler.getLibelle());
               rubRappel.setNoCompteCompta(rubriqueARappeler.getNoCompteCompta());
               rubRappel.setNombreAuto(false);
               rubRappel.setPlafone(rubriqueARappeler.isPlafone());
               rubRappel.setSens("G");
               rubRappel.setNoChapitreCompta(rubriqueARappeler.getNoChapitreCompta());
               rubRappel.setNoCompteCompta(rubriqueARappeler.getNoCompteCompta());
               rubRappel.setNoCompteComptaCle("");
               rubRappel.setSys(false);
               this.this$0.menu.gl.insertOcurance(rubRappel);
            }

            if (this.this$0.menu.pc.insertRubrique(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, rubRappel, motif, base, (double)1.0F, false, false)) {
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Op\u00e9ration effectu\u00e9e avec succ\u00e9!", false);
               this.this$0.afficherRubriquePaie();
            } else {
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Op\u00e9ration echou\u00e9!", true);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.btnSaveRappelSB.setEnabled(true);
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.progressBar.setString("0%");
      this.this$0.progressBar.setStringPainted(true);
   }
}
