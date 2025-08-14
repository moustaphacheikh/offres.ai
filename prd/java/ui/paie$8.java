package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.entity.Motif;
import java.text.DateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JLabel;

class paie$8 extends Thread {
   // $FF: synthetic field
   final paie this$0;

   paie$8(final paie var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnCalPaie.setEnabled(false);

      try {
         this.this$0.statusLabel.setText("Correction rubrique paie ...");
         if (this.this$0.cCorrectionRub.isSelected()) {
            this.this$0.RubriquePaieCorrection();
         }

         if (this.this$0.cCorrectionEng.isSelected()) {
            this.this$0.EngagementCorrection();
         }

         this.this$0.statusLabel.setText("Suppression paie courante ...");
         if (this.this$0.cAllMotifs.isSelected()) {
            String var10000 = "";
         } else {
            " and motif=" + ((Motif)this.this$0.tMotif.getSelectedItem()).getId();
         }

         if (this.this$0.menu.dialect.toString().contains("Oracle")) {
            DateFormat var10001 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Paie where periode=TO_DATE('" + var10001.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
         } else {
            DateFormat var14 = this.this$0.menu.df;
            this.this$0.menu.gl.exQuery("delete from Paie where periode='" + var14.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "'");
         }

         this.this$0.msgInfoLabel.setText(".");
         List<Motif> dl2;
         if (this.this$0.cAllMotifs.isSelected()) {
            dl2 = (List)this.this$0.menu.stricturesIF.dataListInit_Motifs.stream().filter((var0) -> var0.isActif()).sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList());
         } else {
            dl2 = (List)this.this$0.menu.stricturesIF.dataListInit_Motifs.stream().filter((var1) -> var1.getId() == ((Motif)this.this$0.tMotif.getSelectedItem()).getId()).sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList());
         }

         List<Employe> dl = (List)this.this$0.menu.employeFrame.dataListInit.stream().filter((var0) -> var0.isActif() && !var0.isEnConge()).sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList());
         int i = 0;
         int j = 0;
         this.this$0.progressBar.setMaximum(dl.size() * dl2.size());

         for(Employe emp : dl) {
            this.this$0.statusLabel.setText("Calcul ...[" + (j + 1) + "/" + dl.size() + "] ");
            Employe employe = emp;
            JLabel var13 = this.this$0.salarieLabel;
            int var15 = emp.getId();
            var13.setText(var15 + ": " + emp.getPrenom() + " " + emp.getNom());

            for(Motif rs2 : dl2) {
               this.this$0.motifLabel.setText(rs2.getNom());
               this.this$0.menu.pc.insertPaie(employe, rs2, this.this$0.tPaieDu.getDate(), this.this$0.tPaieAu.getDate());
               ++i;
               this.this$0.progressBar.setValue(i);
            }

            ++j;
         }

         this.this$0.msgInfoLabel.setText("Calcul termin\u00e9!");
         this.this$0.motifLabel.setText("Pr\u00eat!");
         this.this$0.salarieLabel.setText("Pr\u00eat!");
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.btnCalPaie.setEnabled(true);
   }
}
