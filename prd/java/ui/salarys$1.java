package com.mccmr.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

class salarys$1 extends Thread {
   // $FF: synthetic field
   final int val$id;
   // $FF: synthetic field
   final boolean val$refresh;
   // $FF: synthetic field
   final salarys this$0;

   salarys$1(final salarys var1, final int var2, final boolean var3) {
      this.val$id = var2;
      this.val$refresh = var3;
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.progressBar.setStringPainted(false);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);

      try {
         this.this$0.selectedOne = this.this$0.menu.pc.employeById(this.val$id);
         if (this.val$refresh) {
            this.this$0.menu.entityManager.refresh(this.this$0.selectedOne);
         }

         if (this.this$0.selectedOne != null) {
            if (this.this$0.selectedOne.isActif()) {
               if (this.this$0.selectedOne.isEnConge()) {
                  this.this$0.salarieLabel.setBackground(Color.orange);
               } else {
                  this.this$0.salarieLabel.setBackground(Color.green);
               }
            } else {
               this.this$0.salarieLabel.setBackground(Color.GRAY);
            }

            int var10001 = this.val$id;
            this.this$0.salarieLabel.setText(" " + var10001 + " - " + this.this$0.selectedOne.getPrenom() + " " + this.this$0.selectedOne.getNom());
            this.this$0.detailPanel.setSelectedIndex(2);
            this.this$0.pPaie_TabbedPane.setSelectedIndex(0);
            this.this$0.jTabbedPane1.setSelectedIndex(1);
            this.this$0.btnSave.setEnabled(true);
            this.this$0.btnDelete.setEnabled(true);
            this.this$0.btnNew.setEnabled(true);
            this.this$0.afficherRubriquePaie();
            this.this$0.afficherNJT();
            this.this$0.afficherConges();
            this.this$0.afficherRetenuesAE();
            this.this$0.afficherEnfant();
            this.this$0.afficherDiplome();
            this.this$0.afficherHeures();
            this.this$0.afficherRubriqueSurSB();
            this.this$0.afficherListeDocuments();
            this.this$0.tBudgetAnuel.setValue(this.this$0.selectedOne.getBudgetannuel());
            this.this$0.tNoteSurBulletin.setText(this.this$0.selectedOne.getNoteSurBulletin());
            this.this$0.tId.setValue(this.this$0.selectedOne.getId());
            this.this$0.tId.setEnabled(false);
            this.this$0.tIdPsservice.setText(this.this$0.selectedOne.getIdPsservice());
            this.this$0.tIDSalariePointeuse.setValue(this.this$0.selectedOne.getIdSalariePointeuse());
            this.this$0.cPsservice.setSelected(this.this$0.selectedOne.isPsservice());
            this.this$0.tPrenom.setText(this.this$0.selectedOne.getPrenom());
            this.this$0.tNom.setText(this.this$0.selectedOne.getNom());
            this.this$0.tPere.setText(this.this$0.selectedOne.getPere());
            this.this$0.tMere.setText(this.this$0.selectedOne.getMere());
            this.this$0.cActif.setSelected(this.this$0.selectedOne.isActif());
            this.this$0.buttonConges.setEnabled(this.this$0.selectedOne.isActif());
            this.this$0.buttonConges.setText(this.this$0.selectedOne.isEnConge() ? "Reprise" : "En Conges");
            this.this$0.buttonConges.setBackground(this.this$0.selectedOne.isEnConge() ? Color.green : Color.YELLOW);
            this.this$0.tZoneOrigine.setSelectedItem(this.this$0.selectedOne.getOrigines() != null ? this.this$0.selectedOne.getOrigines() : this.this$0.tZoneOrigine.getItemAt(0));
            this.this$0.tDateNaiss.setDate(this.this$0.selectedOne.getDateNaissance());
            this.this$0.tLieuNaiss.setText(this.this$0.selectedOne.getLieuNaissance());
            this.this$0.tNationalite.setText(this.this$0.selectedOne.getNationalite());
            this.this$0.tSexe.setSelectedItem(this.this$0.selectedOne.getSexe());
            this.this$0.tSituationFam.setSelectedItem(this.this$0.selectedOne.getSituationFamiliale());
            this.this$0.tNNI.setText(this.this$0.selectedOne.getNni());
            this.this$0.tNbEnfants.setValue(this.this$0.selectedOne.getEnfantss().size());
            this.this$0.tTelephone.setText(this.this$0.selectedOne.getTelephone());
            this.this$0.tAdresse.setText(this.this$0.selectedOne.getAdresse());
            this.this$0.tEmail.setText(this.this$0.selectedOne.getEmail());
            this.this$0.tNoPassport.setText(this.this$0.selectedOne.getNoPassport());
            this.this$0.tDateLPassport.setDate(this.this$0.selectedOne.getDateLivraisonPassport());
            this.this$0.tDateEPassport.setDate(this.this$0.selectedOne.getDateExpirationPassport());
            this.this$0.cExpatrie.setSelected(this.this$0.selectedOne.isExpatrie());
            this.this$0.tNoPermiTravail.setText(this.this$0.selectedOne.getNoPermiTravail());
            this.this$0.tDateLPermiTravail.setDate(this.this$0.selectedOne.getDateLivraisonPermiTravail());
            this.this$0.tDateEPermiTravail.setDate(this.this$0.selectedOne.getDateExpirationPassport());
            this.this$0.tNoCarteSejour.setText(this.this$0.selectedOne.getNoCarteSejour() != null ? this.this$0.selectedOne.getNoCarteSejour().toString() : "");
            this.this$0.tDateLVisa.setDate(this.this$0.selectedOne.getDateDebutVisa());
            this.this$0.tDateEVisa.setDate(this.this$0.selectedOne.getDateFinVisa());
            if (this.this$0.selectedOne.getPhoto() != null && this.this$0.selectedOne.getPhoto().length > 0) {
               byte[] bAvatar = this.this$0.selectedOne.getPhoto();
               String fileName = "photo.png";
               File file = new File(fileName);
               file.deleteOnExit();

               try {
                  FileOutputStream fos = new FileOutputStream(fileName);
                  fos.write(bAvatar);
                  fos.close();
                  file = new File(fileName);
                  BufferedImage img = ImageIO.read(file);
                  ImageIcon icon = new ImageIcon(img);
                  menu var10000 = this.this$0.menu;
                  Image zoom = menu.scaleImage(icon.getImage(), 270, 330);
                  Icon iconScaled = new ImageIcon(zoom);
                  this.this$0.lbPhoto.setIcon(iconScaled);
                  this.this$0.lbPhoto.revalidate();
                  this.this$0.lbPhoto.repaint();
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }

            this.this$0.tTypeContrat.setSelectedItem(this.this$0.selectedOne.getTypeContrat());
            this.this$0.tDateEmbauche.setDate(this.this$0.selectedOne.getDateEmbauche());
            this.this$0.tDateAnciennete.setDate(this.this$0.selectedOne.getDateAnciennete());
            this.this$0.tTauxAnc.setValue(this.this$0.menu.fx.F04_TauxAnciennete(this.this$0.selectedOne, this.this$0.menu.paramsGen.getPeriodeCourante()));
            this.this$0.tDateDebauche.setDate(this.this$0.selectedOne.getDateDebauche());
            this.this$0.tBanque.setSelectedItem(this.this$0.selectedOne.getBanque() != null ? this.this$0.selectedOne.getBanque() : this.this$0.tBanque.getItemAt(0));
            this.this$0.tDirectiongeneral.setSelectedItem(this.this$0.selectedOne.getDirectiongeneral() != null ? this.this$0.selectedOne.getDirectiongeneral() : this.this$0.tDirectiongeneral.getItemAt(0));
            this.this$0.tDirection.setSelectedItem(this.this$0.selectedOne.getDirection() != null ? this.this$0.selectedOne.getDirection() : this.this$0.tDirection.getItemAt(0));
            this.this$0.tDepartement.setSelectedItem(this.this$0.selectedOne.getDepartement() != null ? this.this$0.selectedOne.getDepartement() : this.this$0.tDepartement.getItemAt(0));
            this.this$0.tActivite.setSelectedItem(this.this$0.selectedOne.getActivite() != null ? this.this$0.selectedOne.getActivite() : this.this$0.tActivite.getItemAt(0));
            this.this$0.tPoste.setSelectedItem(this.this$0.selectedOne.getPoste() != null ? this.this$0.selectedOne.getPoste() : this.this$0.tPoste.getItemAt(0));
            this.this$0.tCategorie.setSelectedItem(this.this$0.selectedOne.getGrillesalairebase() != null ? this.this$0.selectedOne.getGrillesalairebase() : this.this$0.tCategorie.getItemAt(0));
            this.this$0.tModePaiement.setSelectedItem(this.this$0.selectedOne.getModePaiement());
            this.this$0.tClassification.setText(this.this$0.selectedOne.getClassification());
            this.this$0.tStatut.setText(this.this$0.selectedOne.getStatut());
            this.this$0.tRaisonDebauche.setText(this.this$0.selectedOne.getRaisonDebauche());
            this.this$0.tNbMoisPreavis.setValue(this.this$0.selectedOne.getNbMoisPreavis());
            this.this$0.tLieuTravail.setText(this.this$0.selectedOne.getLieuTravail());
            this.this$0.tCumul12DMinitial.setValue(this.this$0.selectedOne.getCumul12dminitial());
            this.this$0.tCumul12DM.setValue(this.this$0.menu.fx.F08_cumulBrut12DerMois(this.this$0.selectedOne));
            this.this$0.tCumulBrutImposableInitial.setValue(this.this$0.selectedOne.getCumulBrutImposableInitial());
            this.this$0.tCumulBrutImposable.setValue(this.this$0.menu.pc.cumulTypeById(this.this$0.selectedOne, "BI"));
            this.this$0.tCumulBrutNonImposableInitial.setValue(this.this$0.selectedOne.getCumulBrutNonImposableInitial());
            this.this$0.tCumulBrutNonImposable.setValue(this.this$0.menu.pc.cumulTypeById(this.this$0.selectedOne, "BNI"));
            this.this$0.tCumulRet.setValue(this.this$0.menu.pc.cumulTypeById(this.this$0.selectedOne, "RET"));
            this.this$0.tDateCategorie.setDate(this.this$0.selectedOne.getDateCategorie());
            this.this$0.tDateFinContrat.setDate(this.this$0.selectedOne.getDateFinContrat());
            this.this$0.tDernierDepartInitial.setDate(this.this$0.selectedOne.getDernierDepartInitial());
            this.this$0.tDernierDepart.setDate(this.this$0.menu.pc.dernierDepart(this.this$0.selectedOne));
            this.this$0.tHeureSemaine.setValue(this.this$0.selectedOne.getContratHeureSemaine());
            this.this$0.tNbAnneesCat.setValue(this.this$0.selectedOne.getNbAnnnesCategorie());
            this.this$0.tNoCNAM.setText(this.this$0.selectedOne.getNoCnam());
            this.this$0.tNoCNSS.setText(this.this$0.selectedOne.getNoCnss());
            this.this$0.tDateCNSS.setDate(this.this$0.selectedOne.getDateCnss());
            this.this$0.tNoCompteBanque.setText(this.this$0.selectedOne.getNoCompteBanque());
            this.this$0.tStrictureOrigine.setText(this.this$0.selectedOne.getStructureOrigine());
            this.this$0.tTauxPSRA.setValue(this.this$0.selectedOne.getTauxPsra());
            this.this$0.tTauxRembCNAM.setValue(this.this$0.selectedOne.getTauxRemborssementCnam());
            this.this$0.tTauxRembCNSS.setValue(this.this$0.selectedOne.getTauxRemborssementCnss());
            this.this$0.tauxRembITStranche1.setValue(this.this$0.selectedOne.getTauxRembItstranche1());
            this.this$0.tauxRembITStranche2.setValue(this.this$0.selectedOne.getTauxRembItstranche2());
            this.this$0.tauxRembITStranche3.setValue(this.this$0.selectedOne.getTauxRembItstranche3());
            this.this$0.cAvancmentAutoCat.setSelected(this.this$0.selectedOne.isAvancementCategorieAuto());
            this.this$0.cDetacheCNAM.setSelected(this.this$0.selectedOne.isDetacheCnam());
            this.this$0.cDetacheCNSS.setSelected(this.this$0.selectedOne.isDetacheCnss());
            this.this$0.cDomicilie.setSelected(this.this$0.selectedOne.isDomicilie());
            this.this$0.cExonoreITS.setSelected(this.this$0.selectedOne.isExonoreIts());
            this.this$0.cLUNBegin.setSelected(this.this$0.selectedOne.isLundiDs());
            this.this$0.cLUNEnd.setSelected(this.this$0.selectedOne.isLundiFs());
            this.this$0.cLUNwe.setSelected(this.this$0.selectedOne.isLundiWe());
            this.this$0.cMARBegin.setSelected(this.this$0.selectedOne.isMardiDs());
            this.this$0.cMAREnd.setSelected(this.this$0.selectedOne.isMardiFs());
            this.this$0.cMARwe.setSelected(this.this$0.selectedOne.isMardiWe());
            this.this$0.cMERBegin.setSelected(this.this$0.selectedOne.isMercrediDs());
            this.this$0.cMEREnd.setSelected(this.this$0.selectedOne.isMercrediFs());
            this.this$0.cMERwe.setSelected(this.this$0.selectedOne.isMercrediWe());
            this.this$0.cJEUBegin.setSelected(this.this$0.selectedOne.isJeudiDs());
            this.this$0.cJEUEnd.setSelected(this.this$0.selectedOne.isJeudiFs());
            this.this$0.cJEUwe.setSelected(this.this$0.selectedOne.isJeudiWe());
            this.this$0.cVENBegin.setSelected(this.this$0.selectedOne.isVendrediDs());
            this.this$0.cVENEnd.setSelected(this.this$0.selectedOne.isVendrediFs());
            this.this$0.cVENwe.setSelected(this.this$0.selectedOne.isVendrediWe());
            this.this$0.cSAMBegin.setSelected(this.this$0.selectedOne.isSamediDs());
            this.this$0.cSAMEnd.setSelected(this.this$0.selectedOne.isSamediFs());
            this.this$0.cSAMwe.setSelected(this.this$0.selectedOne.isSamediWe());
            this.this$0.cDIMBegin.setSelected(this.this$0.selectedOne.isDimancheDs());
            this.this$0.cDIMEnd.setSelected(this.this$0.selectedOne.isDimancheFs());
            this.this$0.cDIMwe.setSelected(this.this$0.selectedOne.isDimancheWe());
            this.this$0.pIdentite_TabbedPane.setEnabledAt(1, true);
            this.this$0.pIdentite_TabbedPane.setEnabledAt(2, this.this$0.menu.user.isSalDiplome());
            this.this$0.pIdentite_TabbedPane.setEnabledAt(3, true);
            this.this$0.detailPanel.setEnabledAt(2, this.this$0.menu.user.isSalPaie());
            this.this$0.detailPanel.setEnabledAt(3, this.this$0.menu.user.isSalConge());
            this.this$0.detailPanel.setEnabledAt(4, this.this$0.menu.user.isSalRetenueae());
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
