package com.mccmr.ui;

import com.mccmr.entity.Utilisateurs;
import java.util.Date;

class securite$37 extends Thread {
   // $FF: synthetic field
   final securite this$0;

   securite$37(final securite var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnSave.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      boolean add = false;
      if (this.this$0.selectedOne == null) {
         this.this$0.selectedOne = new Utilisateurs();
         add = true;
      }

      this.this$0.selectedOne.setLogin(this.this$0.tLogin.getText());
      this.this$0.selectedOne.setNomusager(this.this$0.tNom.getText());
      this.this$0.selectedOne.setPassword(add ? "" : this.this$0.selectedOne.getPassword());
      this.this$0.selectedOne.setDersession(new Date());
      this.this$0.selectedOne.setAjout(this.this$0.cAjout.isSelected());
      this.this$0.selectedOne.setCloture(this.this$0.cCloture.isSelected());
      this.this$0.selectedOne.setGrillelog(this.this$0.cGrilleLog.isSelected());
      this.this$0.selectedOne.setGrillesb(this.this$0.cGrilleSB.isSelected());
      this.this$0.selectedOne.setMaj(this.this$0.cMaj.isSelected());
      this.this$0.selectedOne.setMotifpaie(this.this$0.cMotifPaie.isSelected());
      this.this$0.selectedOne.setOriginesal(this.this$0.cOrigineSal.isSelected());
      this.this$0.selectedOne.setParametre(this.this$0.cParametre.isSelected());
      this.this$0.selectedOne.setRubriquepaie(this.this$0.cRubrique.isSelected());
      this.this$0.selectedOne.setSecurite(this.this$0.cSecurite.isSelected());
      this.this$0.selectedOne.setSuppression(this.this$0.cSuppression.isSelected());
      this.this$0.selectedOne.setSuppsal(this.this$0.cSuppSalarie.isSelected());
      this.this$0.selectedOne.setSalConge(this.this$0.cSal_Conges.isSelected());
      this.this$0.selectedOne.setSalContrat(this.this$0.cSal_Contrat.isSelected());
      this.this$0.selectedOne.setSalDiplome(this.this$0.cSal_Diplome.isSelected());
      this.this$0.selectedOne.setSalHs(this.this$0.cSal_HS.isSelected());
      this.this$0.selectedOne.setSalIdentite(this.this$0.cSal_Identite.isSelected());
      this.this$0.selectedOne.setSalPaie(this.this$0.cSal_Paie.isSelected());
      this.this$0.selectedOne.setSalRetenueae(this.this$0.cSal_RetAE.isSelected());
      this.this$0.selectedOne.setSalAdd(this.this$0.cSal_Add.isSelected());
      this.this$0.selectedOne.setSalUpdate(this.this$0.cSalUpdate.isSelected());
      this.this$0.selectedOne.setDashboard(this.this$0.cDashboard.isSelected());
      this.this$0.selectedOne.setSalDoc(this.this$0.cSal_Doc.isSelected());
      if (add) {
         if (this.this$0.menu.gl.insertOcurance(this.this$0.selectedOne)) {
            this.this$0.menu.showMsg(this.this$0.menu.utilisateursFrame, "Le mot de passe par d\u00e9faut est vide.");
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Element ajout\u00e9", false);
         } else {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Op\u00e9ration echou\u00e9e", true);
         }
      } else if (this.this$0.menu.gl.updateOcurance(this.this$0.selectedOne)) {
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Element modifi\u00e9", false);
      } else {
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Op\u00e9ration echou\u00e9e", true);
      }

      this.this$0.dataListInit.removeIf((var1x) -> var1x.getLogin().equalsIgnoreCase(this.this$0.selectedOne.getLogin()));
      this.this$0.dataListInit.add(this.this$0.selectedOne);
      this.this$0.afficherListe();
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.btnSave.setEnabled(true);
   }
}
