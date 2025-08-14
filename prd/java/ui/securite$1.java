package com.mccmr.ui;

import javax.swing.JLabel;

class securite$1 extends Thread {
   // $FF: synthetic field
   final String val$id;
   // $FF: synthetic field
   final securite this$0;

   securite$1(final securite var1, final String var2) {
      this.val$id = var2;
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.progressBar.setStringPainted(false);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);

      try {
         this.this$0.selectedOne = this.this$0.menu.gl.UserByID(this.val$id);
         if (this.this$0.selectedOne != null) {
            JLabel var10000 = this.this$0.salarieLabel;
            String var10001 = this.this$0.selectedOne.getLogin();
            var10000.setText(" " + var10001 + " - " + this.this$0.selectedOne.getNomusager());
            this.this$0.jTabbedPane1.setSelectedIndex(1);
            this.this$0.btnSave.setEnabled(true);
            this.this$0.btnDelete.setEnabled(true);
            this.this$0.btnNew.setEnabled(true);
            this.this$0.tLogin.setEnabled(false);
            this.this$0.tLogin.setText(this.this$0.selectedOne.getLogin());
            this.this$0.tNom.setText(this.this$0.selectedOne.getNomusager());
            this.this$0.cAjout.setSelected(this.this$0.selectedOne.isAjout());
            this.this$0.cMaj.setSelected(this.this$0.selectedOne.isMaj());
            this.this$0.cSuppression.setSelected(this.this$0.selectedOne.isSuppression());
            this.this$0.cRubrique.setSelected(this.this$0.selectedOne.isRubriquepaie());
            this.this$0.cGrilleLog.setSelected(this.this$0.selectedOne.isGrillesb());
            this.this$0.cGrilleLog.setSelected(this.this$0.selectedOne.isGrillelog());
            this.this$0.cCloture.setSelected(this.this$0.selectedOne.isCloture());
            this.this$0.cParametre.setSelected(this.this$0.selectedOne.isParametre());
            this.this$0.cSecurite.setSelected(this.this$0.selectedOne.isSecurite());
            this.this$0.cMotifPaie.setSelected(this.this$0.selectedOne.isMotifpaie());
            this.this$0.cOrigineSal.setSelected(this.this$0.selectedOne.isOriginesal());
            this.this$0.cDashboard.setSelected(this.this$0.selectedOne.isDashboard());
            this.this$0.cSuppSalarie.setSelected(this.this$0.selectedOne.isSuppsal());
            this.this$0.cSal_Conges.setSelected(this.this$0.selectedOne.isSalConge());
            this.this$0.cSal_Contrat.setSelected(this.this$0.selectedOne.isSalContrat());
            this.this$0.cSal_Diplome.setSelected(this.this$0.selectedOne.isSalDiplome());
            this.this$0.cSal_Doc.setSelected(this.this$0.selectedOne.isSalDoc());
            this.this$0.cSal_HS.setSelected(this.this$0.selectedOne.isSalHs());
            this.this$0.cSal_Identite.setSelected(this.this$0.selectedOne.isSalIdentite());
            this.this$0.cSal_Paie.setSelected(this.this$0.selectedOne.isSalPaie());
            this.this$0.cSal_RetAE.setSelected(this.this$0.selectedOne.isSalRetenueae());
            this.this$0.cSal_Add.setSelected(this.this$0.selectedOne.isSalAdd());
            this.this$0.cSalUpdate.setSelected(this.this$0.selectedOne.isSalUpdate());
         } else {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Aucun resultat pour cet ID!", true);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.progressBar.setString("0%");
      this.this$0.progressBar.setStringPainted(true);
   }
}
