package com.mccmr.ui;

import com.mccmr.entity.Paramgen;
import com.mccmr.util.ModelClass;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

class parametres$11 extends Thread {
   // $FF: synthetic field
   final parametres this$0;

   parametres$11(final parametres var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setValue(10);
      this.this$0.btnSave.setEnabled(false);
      Paramgen p = this.this$0.menu.paramsGen;
      p.setDateMaj(new Date());
      p.setActiviteEntreprise(this.this$0.tActiviteEntreprise.getText());
      p.setAdresse(this.this$0.tAdresse.getText());
      p.setAncienneteAuto(Boolean.valueOf(this.this$0.cAncienneteAuto.isSelected()));
      p.setBd(this.this$0.tBP.getText());
      p.setDeductionCnamdeIts(Boolean.valueOf(this.this$0.cDeductionCNAMdeITS.isSelected()));
      p.setDeductionCnssdeIts(Boolean.valueOf(this.this$0.cDeductionCNSSdeITS.isSelected()));
      p.setDelaiAlerteFinContrat(((Number)this.this$0.tDelaiAlerteFinContrat.getValue()).intValue());
      p.setEmail(this.this$0.tEmail.getText());
      p.setFax(this.this$0.tFax.getText());
      p.setIndlogementAuto(Boolean.valueOf(this.this$0.cIndlogementAuto.isSelected()));
      p.setPeriodeCloture(this.this$0.tPeriodeCloture.getDate());
      p.setNjtDefault((double)((Number)this.this$0.tNjtDefault.getValue()).intValue());
      p.setNoCnam(this.this$0.tCNAM.getText());
      p.setNoCnss(this.this$0.tCNSS.getText());
      p.setNoIts(this.this$0.tITS.getText());
      p.setNomEntreprise(this.this$0.tnomEntreprise.getText());
      p.setPlafonIndNonImposable(((Number)this.this$0.tPlafonIndNonImposable.getValue()).doubleValue());
      p.setPrimePanierAuto(Boolean.valueOf(this.this$0.cPrimePanierAuto.isSelected()));
      p.setQualiteResponsable(this.this$0.tQualiRespo.getText());
      p.setRemboursementIts(Boolean.valueOf(this.this$0.cRemboursementITS.isSelected()));
      p.setResponsableEntreprise(this.this$0.tRespoansable.getText());
      p.setSignataires(this.this$0.tSignataires.getText());
      p.setSiteweb(this.this$0.tSiteweb.getText());
      p.setSmig(((Number)this.this$0.tSMIG.getValue()).doubleValue());
      p.setTelephone(this.this$0.tTelephone.getText());
      p.setVilleSiege(this.this$0.tvilleSiege.getText());
      p.setUsedIts(2018);
      p.setCheminatt2000(this.this$0.tCheminAtt2000.getText());
      p.setBankvirement(this.this$0.tBankVirement.getText());
      p.setComptevirement(this.this$0.tCpteVirement.getText());
      p.setAbatement(((Number)this.this$0.tAbattement.getValue()).doubleValue());
      p.setQuotaEcheanceRae(((Number)this.this$0.tQuotaEcheanceRAE.getValue()).doubleValue());
      p.setRetEngOnConge(Boolean.valueOf(this.this$0.cRetEngOnConge.isSelected()));
      p.setDevise(this.this$0.tDevises.getText());
      p.setMailSmtpHost(this.this$0.tMailSmtpHost.getText());
      p.setMailUser(this.this$0.tMailUser.getText());
      p.setMailPassword(new String(this.this$0.tMailPassword.getPassword()));
      p.setMailSmtpPort(this.this$0.tMailSmtpPort.getText());
      p.setMailSmtpTLSEnabled(this.this$0.cMailSmtpTLSEnabled.isSelected());
      p.setNoComptaCnam(((Number)this.this$0.tNoComptaCNAM.getValue()).longValue());
      p.setNoComptaChapitreCnam(((Number)this.this$0.tNoComptaChapitreCnam.getValue()).longValue());
      p.setNoComptaCleCnam(this.this$0.tNoComptaCleCnam.getText());
      p.setNoComptaCnamPatCredit(((Number)this.this$0.tNoComptaCnamPatCredit.getValue()).longValue());
      p.setNoComptaChapitreCnamPatCredit(((Number)this.this$0.tNoComptaChapitreCnamPatCredit.getValue()).longValue());
      p.setNoComptaCleCnamPatCredit(this.this$0.tNoComptaCleCnamPatCredit.getText());
      p.setNoComptaCnamPatDebit(((Number)this.this$0.tNoComptaCnamPatDebit.getValue()).longValue());
      p.setNoComptaChapitreCnamPatDebit(((Number)this.this$0.tNoComptaChapitreCnamPatDebit.getValue()).longValue());
      p.setNoComptaCleCnamPatDebit(this.this$0.tNoComptaCleCnamPatDebit.getText());
      p.setNoComptaCnss(((Number)this.this$0.tNoComptaCnss.getValue()).longValue());
      p.setNoComptaChapitreCnss(((Number)this.this$0.tNoComptaChapitreCnss.getValue()).longValue());
      p.setNoComptaCleCnss(this.this$0.tNoComptaCleCnss.getText());
      p.setNoComptaCnssMedCredit(((Number)this.this$0.tNoComptaCnssMedCredit.getValue()).longValue());
      p.setNoComptaChapitreCnssMedCredit(((Number)this.this$0.tNoComptaChapitreCnssMedCredit.getValue()).longValue());
      p.setNoComptaCleCnssMedCredit(this.this$0.tNoComptaCleCnssMedCredit.getText());
      p.setNoComptaCnssMedDebit(((Number)this.this$0.tNoComptaCnssMedDebit.getValue()).longValue());
      p.setNoComptaChapitreCnssMedDebit(((Number)this.this$0.tNoComptaChapitreCnssMedDebit.getValue()).longValue());
      p.setNoComptaCleCnssMedDebit(this.this$0.tNoComptaCleCnssMedDebit.getText());
      p.setNoComptaCnssPatCredit(((Number)this.this$0.tNoComptaCnssPatCredit.getValue()).longValue());
      p.setNoComptaChapitreCnssPatCredit(((Number)this.this$0.tNoComptaChapitreCnssPatCredit.getValue()).longValue());
      p.setNoComptaCleCnssPatCredit(this.this$0.tNoComptaCleCnssPatCredit.getText());
      p.setNoComptaCnssPatDebit(((Number)this.this$0.tNoComptaCnssPatDebit.getValue()).longValue());
      p.setNoComptaChapitreCnssPatDebit(((Number)this.this$0.tNoComptaChapitreCnssPatDebit.getValue()).longValue());
      p.setNoComptaCleCnssPatDebit(this.this$0.tNoComptaCleCnssPatDebit.getText());
      p.setNoComptaIts(((Number)this.this$0.tNoComptaIts.getValue()).longValue());
      p.setNoComptaChapitreIts(((Number)this.this$0.tNoComptaChapitreIts.getValue()).longValue());
      p.setNoComptaCleIts(this.this$0.tNoComptaCleIts.getText());
      p.setNoComptaNet(((Number)this.this$0.tNoComptaNet.getValue()).longValue());
      p.setNoComptaChapitreNet(((Number)this.this$0.tNoComptaChapitreNet.getValue()).longValue());
      p.setNoComptaCleNet(this.this$0.tNoComptaCleNet.getText());
      p.setNoComptaRits(((Number)this.this$0.tNoComptaRITS.getValue()).longValue());
      p.setNoComptaChapitreRits(((Number)this.this$0.tNoComptaChapitreRits.getValue()).longValue());
      p.setNoComptaCleRits(this.this$0.tNoComptaCleRits.getText());
      p.setNoComptaRcnss(((Number)this.this$0.tNoComptaRcnss.getValue()).longValue());
      p.setNoComptaChapitreRcnss(((Number)this.this$0.tNoComptaChapitreRcnss.getValue()).longValue());
      p.setNoComptaCleRcnss(this.this$0.tNoComptaCleRcnss.getText());
      p.setNoComptaRcnam(((Number)this.this$0.tNoComptaRCNAM.getValue()).longValue());
      p.setNoComptaChapitreRcnam(((Number)this.this$0.tNoComptaChapitreRcnam.getValue()).longValue());
      p.setNoComptaCleRcnam(this.this$0.tNoComptaCleRcnam.getText());
      p.setAppIndCompensatrice(this.this$0.cAppIndCompensatrice.isSelected());
      p.setAddCurrentSalInCumulCng(this.this$0.cAddCurrentSalInCumulCng.isSelected());
      p.setModeITS(this.this$0.tModeITS.getSelectedItem().toString());

      try {
         Date periodeCourante = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(this.this$0.tPeriodeCourante.getDate()));
         Date periodeSuivante = this.this$0.menu.gl.addRetriveDays(periodeCourante, 30);
         periodeSuivante = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periodeSuivante));
         p.setPeriodeCourante(periodeCourante);
         p.setPeriodeSuivante(periodeSuivante);
      } catch (Exception e) {
         e.printStackTrace();
      }

      if (!this.this$0.tLogoDestFile.getText().isEmpty()) {
         File file = new File(this.this$0.tLogo.getText());
         byte[] imageData = new byte[(int)file.length()];

         try {
            if (file.exists()) {
               FileInputStream fileInputStream = new FileInputStream(file);
               fileInputStream.read(imageData);
               fileInputStream.close();
               p.setLogo(imageData);
            }
         } catch (Exception e) {
            this.this$0.menu.viewMessage(this.this$0.msgErrLabel, e.getMessage(), true);
         }
      }

      this.this$0.progressBar.setValue(50);

      for(int i = 0; i < this.this$0.sysRubriqueTable.getRowCount(); ++i) {
         int idRubSys = ((Number)((ModelClass.tmSysRubrique)this.this$0.sysRubriqueTable.getModel()).getValueAt(i, 0)).intValue();
         int idRubCustom = ((Number)((ModelClass.tmSysRubrique)this.this$0.sysRubriqueTable.getModel()).getValueAt(i, 2)).intValue();
         this.this$0.menu.gl.exQuery("UPDATE Sysrubrique SET idCustum=" + idRubCustom + " WHERE idSys=" + idRubSys);
      }

      if (this.this$0.menu.gl.updateOcurance(p)) {
         this.this$0.progressBar.setValue(100);
         this.this$0.menu.paramsGen = p;
         this.this$0.menu.showSuccessMsg();
      } else {
         this.this$0.menu.viewMessage(this.this$0.msgErrLabel, "Err.: Modifications non enregistr\u00e9es!", true);
      }

      this.this$0.btnSave.setEnabled(true);
   }
}
