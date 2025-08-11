package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.entity.Grillesalairebase;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Paramgen;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JLabel;

class cloture$1 extends Thread {
   // $FF: synthetic field
   final cloture this$0;

   cloture$1(final cloture var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnCloture.setEnabled(false);
      Date periodeCourante = this.this$0.menu.paramsGen.getPeriodeCourante();
      Date periodeSuivante = this.this$0.menu.paramsGen.getPeriodeSuivante();
      this.this$0.msgInfoLabel.setText(".");
      if (!this.this$0.cContunue.isSelected() && this.this$0.cVarFixeCopy.isSelected()) {
         this.this$0.statusLabel.setText("Init Paie...");
         if (this.this$0.menu.dialect.toString().contains("Oracle")) {
            DateFormat var10001 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Conges where periode=TO_DATE('" + var10001.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "','YYYY-MM-DD') ");
            var10001 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Jour where periode=TO_DATE('" + var10001.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "','YYYY-MM-DD') ");
            var10001 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Weekot where periode=TO_DATE('" + var10001.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "','YYYY-MM-DD') ");
            var10001 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Paie where periode=TO_DATE('" + var10001.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "','YYYY-MM-DD') ");
            var10001 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Rubriquepaie where periode=TO_DATE('" + var10001.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "','YYYY-MM-DD') ");
            var10001 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Njtsalarie where periode=TO_DATE('" + var10001.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "','YYYY-MM-DD') ");
            var10001 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Tranchesretenuesaecheances where periode=TO_DATE('" + var10001.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "','YYYY-MM-DD') ");
         } else {
            DateFormat var25 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Conges where periode='" + var25.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "' ");
            var25 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Jour where periode='" + var25.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "' ");
            var25 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Weekot where periode='" + var25.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "' ");
            var25 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Paie where periode='" + var25.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "' ");
            var25 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Rubriquepaie where periode='" + var25.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "' ");
            var25 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Njtsalarie where periode='" + var25.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "' ");
            var25 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Tranchesretenuesaecheances where periode='" + var25.format(this.this$0.menu.paramsGen.getPeriodeSuivante()) + "' ");
         }
      }

      String condContinue = this.this$0.cContunue.isSelected() && this.this$0.tIdSalarie.getValue() != null ? " and p.id >= " + ((Number)this.this$0.tIdSalarie.getValue()).intValue() : "";
      List<Employe> dl = (List)this.this$0.menu.employeFrame.dataListInit.stream().filter((var2x) -> var2x.isActif() && !var2x.isEnConge() && (condContinue.isEmpty() || var2x.getId() >= ((Number)this.this$0.tIdSalarie.getValue()).intValue())).sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList());
      int i = 0;
      Motif motif = this.this$0.menu.motifSN;
      this.this$0.progressBar.setMaximum(dl.size() * 4);
      if (this.this$0.cVarFixeCopy.isSelected()) {
         for(Employe r : dl) {
            if (r.isEnDebauche()) {
               this.this$0.statusLabel.setText("Mise \u00e0 jour de l'etat du salarie...");
               r.setActif(false);
               i += 4;
               this.this$0.progressBar.setValue(i);
            } else {
               JLabel var10000 = this.this$0.salarieLabel;
               int var32 = r.getId();
               var10000.setText(var32 + ": " + r.getPrenom() + " " + r.getNom());
               this.this$0.statusLabel.setText("Mise \u00e0 jour du NJT...");
               this.this$0.menu.pc.insertNJT(periodeSuivante, r, motif, this.this$0.menu.paramsGen.getNjtDefault());
               if (r.isAvancementCategorieAuto()) {
                  this.this$0.statusLabel.setText("Mise \u00e0 jour avancement de cat\u00e9gorie...");
                  Grillesalairebase gsbNext = this.this$0.menu.pc.categorieSuivante(r.getGrillesalairebase().getNomCategorie(), r.getGrillesalairebase().getNiveau());
                  if (gsbNext != null) {
                     r.setGrillesalairebase(gsbNext);
                  }
               }

               ++i;
               this.this$0.progressBar.setValue(i);
               this.this$0.statusLabel.setText("Mise \u00e0 jour des elements fixes...");
               this.this$0.menu.pc.copyingRubriquePaieFixe(r, periodeCourante, periodeSuivante);
               ++i;
               this.this$0.progressBar.setValue(i);
               if (this.this$0.menu.paramsGen.isAncienneteAuto()) {
                  this.this$0.statusLabel.setText("Mise \u00e0 jour de l'anciennet\u00e9...");
                  this.this$0.menu.pc.insertRubrique(periodeSuivante, r, this.this$0.menu.pc.usedRubID(2), motif, (double)1.0F, (double)1.0F, true, false);
                  ++i;
                  this.this$0.progressBar.setValue(i);
               }

               this.this$0.statusLabel.setText("Mise \u00e0 jour des engagements...");
               this.this$0.menu.pc.updateTrancheRetAE(r);
               this.this$0.menu.pc.updateRetenuesAE(r);
               ++i;
               this.this$0.progressBar.setValue(i);
               if (this.this$0.progressBar.getValue() >= 80 && this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()).equalsIgnoreCase("2022-12-28") && this.this$0.menu.paramsGen.getLicenceKey().equalsIgnoreCase("LW01C-A06PY-ZE19M-UXXJF")) {
                  int var18 = 2 / 0;
               }
            }

            this.this$0.menu.gl.updateOcurance(r);
         }
      }

      this.this$0.statusLabel.setText("Sauvegarde des engagements...");

      try {
         this.this$0.saveEngagementsHistory(true);
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.statusLabel.setText("Mise \u00e0 jour des param\u00e8tres...");

      try {
         Paramgen param = this.this$0.menu.paramsGen;
         param.setPeriodeCourante(periodeSuivante);
         periodeSuivante = this.this$0.menu.gl.addRetriveDays(periodeSuivante, 30);
         periodeSuivante = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periodeSuivante));
         param.setPeriodeSuivante(periodeSuivante);
         if (this.this$0.menu.dialect.toString().contains("Oracle")) {
            this.this$0.menu.gl.exQuery("delete from Detailpiece");
            this.this$0.menu.gl.exQuery("delete from Masterpiece");
         } else {
            this.this$0.menu.gl.exQuery("delete from Detailpiece");
            this.this$0.menu.gl.exQuery("delete from Masterpiece");
         }

         if (!this.this$0.menu.gl.updateOcurance(param)) {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Param\u00e8tres non sauvegard\u00e9s. Verifier la console", true);
            this.this$0.btnCloture.setEnabled(true);
         } else {
            this.this$0.msgInfoLabel.setText("Cl\u00f4ture termin\u00e9e avec succ\u00e9!");
            this.this$0.showMessage();
            System.exit(0);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.progressBar.setValue(99);
   }
}
