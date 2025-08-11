package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.util.ModelClass;
import java.util.Date;
import java.util.Objects;
import javax.swing.JTable;

class att$52 extends Thread {
   // $FF: synthetic field
   final att this$0;

   att$52(final att var1) {
      this.this$0 = this$0;
   }

   public void run() {
      Date periodeCourante = this.this$0.menu.paramsGen.getPeriodeCourante();
      this.this$0.progressBar.setMaximum(this.this$0.listTable.getRowCount());
      this.this$0.progressBar.setMinimum(0);
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.progressBar.setStringPainted(true);
      int valuePB = 0;
      this.this$0.progressBar.setValue(valuePB);
      long salariesCount = 0L;
      boolean ppAuto = this.this$0.menu.paramsGen.isPrimePanierAuto();
      this.this$0.listPointage.removeAll();
      JTable var10000 = this.this$0.listPointage;
      ModelClass var10003 = this.this$0.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmPointageIndividuel(var10003));
      this.this$0.listJT.removeAll();
      var10000 = this.this$0.listJT;
      var10003 = this.this$0.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmHSDayly(var10003));

      for(int i = 0; i < this.this$0.listTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmPointage)this.this$0.listTable.getModel()).getValueAt(i, 0)) {
            this.this$0.selectedIDP = ((Number)this.this$0.listTable.getValueAt(i, 1)).intValue();
            Employe e = this.this$0.menu.pc.employeByIDP((long)this.this$0.selectedIDP);
            if (this.this$0.cApplyNJT.isSelected()) {
               double nbJours = ((Number)this.this$0.listTable.getValueAt(i, 4)).doubleValue();
               if (this.this$0.menu.pc.insertNJT(periodeCourante, e, this.this$0.menu.motifSN, nbJours)) {
                  this.this$0.menu.pc.updateRubriquePaie(periodeCourante, e, this.this$0.menu.motifSN);
               }
            }

            if (this.this$0.cApplyNHT.isSelected()) {
               this.this$0.applyTiming(this.this$0.selectedIDP, this.this$0.tBeginDateView.getDate(), this.this$0.tEndDateView.getDate(), ppAuto);
            }

            if (this.this$0.cValorisation.isSelected()) {
               double[] tabHS = this.this$0.menu.pc.decompterHS(e, periodeCourante);
               this.this$0.menu.pc.valoriserHS(e, this.this$0.menu.motifSN, tabHS[2], tabHS[3], tabHS[4], tabHS[5], tabHS[6], tabHS[7]);
            }

            ++salariesCount;
         }

         this.this$0.progressBar.setValue(i);
      }

      this.this$0.progressBar.setValue(100);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, salariesCount + " Salari\u00e9(s) trait\u00e9(s) avec sucss\u00e9!", false);
   }
}
