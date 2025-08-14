package com.mccmr.ui;

import com.mccmr.entity.Activite;
import com.mccmr.entity.Banque;
import com.mccmr.entity.Departement;
import com.mccmr.entity.Direction;
import com.mccmr.entity.Directiongeneral;
import com.mccmr.entity.Employe;
import com.mccmr.entity.Grillesalairebase;
import com.mccmr.entity.Origines;
import com.mccmr.entity.Poste;

class salarys$269 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$269(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnSave.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      boolean add = false;
      if (this.this$0.selectedOne == null) {
         this.this$0.selectedOne = new Employe();
         add = true;
      }

      this.this$0.selectedOne.setId(((Number)this.this$0.tId.getValue()).intValue());
      this.this$0.selectedOne.setIdSalariePointeuse(((Number)this.this$0.tIDSalariePointeuse.getValue()).intValue());
      this.this$0.selectedOne.setIdPsservice(this.this$0.tIdPsservice.getText());
      this.this$0.selectedOne.setActif(this.this$0.cActif.isSelected());
      this.this$0.selectedOne.setPrenom(this.this$0.tPrenom.getText());
      this.this$0.selectedOne.setNom(this.this$0.tNom.getText());
      this.this$0.selectedOne.setDateNaissance(this.this$0.tDateNaiss.getDate());
      this.this$0.selectedOne.setLieuNaissance(this.this$0.tLieuNaiss.getText());
      this.this$0.selectedOne.setMere(this.this$0.tMere.getText());
      this.this$0.selectedOne.setPere(this.this$0.tPere.getText());
      this.this$0.selectedOne.setNationalite(this.this$0.tNationalite.getText());
      this.this$0.selectedOne.setSexe(this.this$0.tSexe.getSelectedItem().toString());
      this.this$0.selectedOne.setSituationFamiliale(this.this$0.tSituationFam.getSelectedItem().toString());
      this.this$0.selectedOne.setNbEnfants(this.this$0.selectedOne.getEnfantss().size());
      this.this$0.selectedOne.setNni(this.this$0.tNNI.getText());
      this.this$0.selectedOne.setOrigines((Origines)this.this$0.tZoneOrigine.getSelectedItem());
      this.this$0.selectedOne.setNoPassport(this.this$0.tNoPassport.getText());
      this.this$0.selectedOne.setDateLivraisonPassport(this.this$0.tDateLPassport.getDate());
      this.this$0.selectedOne.setDateExpirationPassport(this.this$0.tDateEPassport.getDate());
      this.this$0.selectedOne.setExpatrie(this.this$0.cExpatrie.isSelected());
      this.this$0.selectedOne.setNoCarteSejour(this.this$0.tNoCarteSejour.getText());
      this.this$0.selectedOne.setDateDebutVisa(this.this$0.tDateLVisa.getDate());
      this.this$0.selectedOne.setDateFinVisa(this.this$0.tDateEVisa.getDate());
      this.this$0.selectedOne.setNoPermiTravail(this.this$0.tNoPermiTravail.getText());
      this.this$0.selectedOne.setDateLivraisonPermiTravail(this.this$0.tDateLPermiTravail.getDate());
      this.this$0.selectedOne.setDateExpirationPermiTravail(this.this$0.tDateEPermiTravail.getDate());
      this.this$0.selectedOne.setTelephone(this.this$0.tTelephone.getText());
      this.this$0.selectedOne.setEmail(this.this$0.tEmail.getText());
      this.this$0.selectedOne.setAdresse(this.this$0.tAdresse.getText());
      this.this$0.selectedOne.setTypeContrat(this.this$0.tTypeContrat.getSelectedItem().toString());
      this.this$0.selectedOne.setDateEmbauche(this.this$0.tDateEmbauche.getDate());
      this.this$0.selectedOne.setDateAnciennete(this.this$0.tDateAnciennete.getDate());
      this.this$0.selectedOne.setTauxAnciennete((double)0.0F);
      this.this$0.selectedOne.setDateFinContrat(this.this$0.tDateFinContrat.getDate());
      this.this$0.selectedOne.setLieuTravail(this.this$0.tLieuTravail.getText());
      this.this$0.selectedOne.setDateDebauche(this.this$0.tDateDebauche.getDate());
      this.this$0.selectedOne.setRaisonDebauche(this.this$0.tRaisonDebauche.getText());
      this.this$0.selectedOne.setDirectiongeneral((Directiongeneral)this.this$0.tDirectiongeneral.getSelectedItem());
      this.this$0.selectedOne.setDirection((Direction)this.this$0.tDirection.getSelectedItem());
      this.this$0.selectedOne.setDepartement((Departement)this.this$0.tDepartement.getSelectedItem());
      this.this$0.selectedOne.setActivite((Activite)this.this$0.tActivite.getSelectedItem());
      this.this$0.selectedOne.setPoste((Poste)this.this$0.tPoste.getSelectedItem());
      this.this$0.selectedOne.setGrillesalairebase((Grillesalairebase)this.this$0.tCategorie.getSelectedItem());
      this.this$0.selectedOne.setDateCategorie(this.this$0.tDateCategorie.getDate());
      this.this$0.selectedOne.setAvancementCategorieAuto(this.this$0.cAvancmentAutoCat.isSelected());
      this.this$0.selectedOne.setNbAnnnesCategorie(((Number)this.this$0.tNbAnneesCat.getValue()).intValue());
      this.this$0.selectedOne.setClassification(this.this$0.tClassification.getText());
      this.this$0.selectedOne.setStatut(this.this$0.tStatut.getText());
      this.this$0.selectedOne.setNbMoisPreavis(((Number)this.this$0.tNbMoisPreavis.getValue()).floatValue());
      this.this$0.selectedOne.setTauxPsra(this.this$0.tTauxPSRA.getText().isEmpty() ? (double)0.0F : ((Number)this.this$0.tTauxPSRA.getValue()).doubleValue());
      this.this$0.selectedOne.setModePaiement(this.this$0.tModePaiement.getSelectedItem().toString());
      this.this$0.selectedOne.setBanque((Banque)this.this$0.tBanque.getSelectedItem());
      this.this$0.selectedOne.setDomicilie(this.this$0.cDomicilie.isSelected());
      this.this$0.selectedOne.setNoCompteBanque(this.this$0.tNoCompteBanque.getText());
      this.this$0.selectedOne.setDernierDepartInitial(this.this$0.tDernierDepartInitial.getDate());
      this.this$0.selectedOne.setCumul12dminitial(((Number)this.this$0.tCumul12DMinitial.getValue()).doubleValue());
      this.this$0.selectedOne.setCumulBrutImposableInitial(((Number)this.this$0.tCumulBrutImposableInitial.getValue()).doubleValue());
      this.this$0.selectedOne.setCumulBrutNonImposableInitial(((Number)this.this$0.tCumulBrutNonImposableInitial.getValue()).doubleValue());
      this.this$0.selectedOne.setCumulNjtinitial((double)0.0F);
      this.this$0.selectedOne.setBudgetannuel(((Number)this.this$0.tBudgetAnuel.getValue()).doubleValue());
      this.this$0.selectedOne.setPsservice(this.this$0.cPsservice.isSelected());
      this.this$0.selectedOne.setContratHeureSemaine(((Number)this.this$0.tHeureSemaine.getValue()).doubleValue());
      this.this$0.selectedOne.setLundiDs(this.this$0.cLUNBegin.isSelected());
      this.this$0.selectedOne.setLundiFs(this.this$0.cLUNEnd.isSelected());
      this.this$0.selectedOne.setLundiWe(this.this$0.cLUNwe.isSelected());
      this.this$0.selectedOne.setMardiDs(this.this$0.cMARBegin.isSelected());
      this.this$0.selectedOne.setMardiFs(this.this$0.cMAREnd.isSelected());
      this.this$0.selectedOne.setMardiWe(this.this$0.cMARwe.isSelected());
      this.this$0.selectedOne.setMercrediDs(this.this$0.cMERBegin.isSelected());
      this.this$0.selectedOne.setMercrediFs(this.this$0.cMEREnd.isSelected());
      this.this$0.selectedOne.setMercrediWe(this.this$0.cMERwe.isSelected());
      this.this$0.selectedOne.setJeudiDs(this.this$0.cJEUBegin.isSelected());
      this.this$0.selectedOne.setJeudiFs(this.this$0.cJEUEnd.isSelected());
      this.this$0.selectedOne.setJeudiWe(this.this$0.cJEUwe.isSelected());
      this.this$0.selectedOne.setVendrediDs(this.this$0.cVENBegin.isSelected());
      this.this$0.selectedOne.setVendrediFs(this.this$0.cVENEnd.isSelected());
      this.this$0.selectedOne.setVendrediWe(this.this$0.cVENwe.isSelected());
      this.this$0.selectedOne.setSamediDs(this.this$0.cSAMBegin.isSelected());
      this.this$0.selectedOne.setSamediFs(this.this$0.cSAMEnd.isSelected());
      this.this$0.selectedOne.setSamediWe(this.this$0.cSAMwe.isSelected());
      this.this$0.selectedOne.setDimancheDs(this.this$0.cDIMBegin.isSelected());
      this.this$0.selectedOne.setDimancheFs(this.this$0.cDIMEnd.isSelected());
      this.this$0.selectedOne.setDimancheWe(this.this$0.cDIMwe.isSelected());
      this.this$0.selectedOne.setDetacheCnss(this.this$0.cDetacheCNSS.isSelected());
      this.this$0.selectedOne.setNoCnss(this.this$0.tNoCNSS.getText());
      this.this$0.selectedOne.setDateCnss(this.this$0.tNoCNSS.getText().isEmpty() ? this.this$0.tDateEmbauche.getDate() : this.this$0.tDateCNSS.getDate());
      this.this$0.selectedOne.setTauxRemborssementCnss(((Number)this.this$0.tTauxRembCNSS.getValue()).doubleValue());
      this.this$0.selectedOne.setStructureOrigine(this.this$0.tStrictureOrigine.getText());
      this.this$0.selectedOne.setDetacheCnam(this.this$0.cDetacheCNAM.isSelected());
      this.this$0.selectedOne.setNoCnam(this.this$0.tNoCNAM.getText());
      this.this$0.selectedOne.setTauxRemborssementCnam(((Number)this.this$0.tTauxRembCNAM.getValue()).doubleValue());
      this.this$0.selectedOne.setExonoreIts(this.this$0.cExonoreITS.isSelected());
      this.this$0.selectedOne.setTauxRembItstranche1(((Number)this.this$0.tauxRembITStranche1.getValue()).doubleValue());
      this.this$0.selectedOne.setTauxRembItstranche2(((Number)this.this$0.tauxRembITStranche2.getValue()).doubleValue());
      this.this$0.selectedOne.setTauxRembItstranche3(((Number)this.this$0.tauxRembITStranche3.getValue()).doubleValue());
      this.this$0.setSalaryPhoto(this.this$0.selectedOne, this.this$0.photoPath);
      if (add) {
         this.this$0.selectedOne.setEnDebauche(false);
         this.this$0.selectedOne.setNoteSurBulletin("");
         if (this.this$0.dataListInit.size() >= this.this$0.menu.limiteSalarie) {
            this.this$0.menu.showErrMsg(this.this$0.menu.employeFrame, "Limite de version. Veuillez metre \u00e0 jour votre version ELIYA Paie!");
         } else if (this.this$0.menu.gl.insertOcurance(this.this$0.selectedOne)) {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Element ajout\u00e9", false);
         } else {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Op\u00e9ration echou\u00e9e", true);
         }
      } else if (this.this$0.menu.gl.updateOcurance(this.this$0.selectedOne)) {
         int newId = ((Number)this.this$0.tId.getValue()).intValue();
         this.this$0.menu.gl.exQuery("update Enfants set employe =" + newId + " where employe=" + this.this$0.selectedOne.getId());
         this.this$0.menu.gl.exQuery("update Diplome set employe =" + newId + " where employe=" + this.this$0.selectedOne.getId());
         this.this$0.menu.gl.exQuery("update Jour set employe =" + newId + " where employe=" + this.this$0.selectedOne.getId());
         this.this$0.menu.gl.exQuery("update Weekot set employe =" + newId + " where employe=" + this.this$0.selectedOne.getId());
         this.this$0.menu.gl.exQuery("update Paie set employe =" + newId + " where employe=" + this.this$0.selectedOne.getId());
         this.this$0.menu.gl.exQuery("update Rubriquepaie set employe =" + newId + " where employe=" + this.this$0.selectedOne.getId());
         this.this$0.menu.gl.exQuery("update Njtsalarie set employe =" + newId + " where employe=" + this.this$0.selectedOne.getId());
         this.this$0.menu.gl.exQuery("update Conges set employe =" + newId + " where employe=" + this.this$0.selectedOne.getId());
         this.this$0.menu.gl.exQuery("update Retenuesaecheances set employe =" + newId + " where employe=" + this.this$0.selectedOne.getId());
         this.this$0.menu.gl.exQuery("update Employe set id =" + newId + " where id=" + this.this$0.selectedOne.getId());
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Element modifi\u00e9", false);
      } else {
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Op\u00e9ration echou\u00e9e", true);
      }

      this.this$0.dataListInit.removeIf((var1x) -> var1x.getId() == this.this$0.selectedOne.getId());
      this.this$0.dataListInit.add(this.this$0.selectedOne);
      this.this$0.afficherListe();
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.btnSave.setEnabled(true);
   }
}
