package com.mccmr.ui;

import java.util.Date;

class salarys$287 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$287(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnValoriserHS.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
      Date periodeCourante = this.this$0.menu.paramsGen.getPeriodeCourante();
      this.this$0.menu.pc.insertRubrique(periodeCourante, this.this$0.selectedOne, this.this$0.menu.pc.usedRubID(3), this.this$0.selectedMotif, (Double)null, ((Number)this.this$0.tTotHS115.getValue()).doubleValue(), false, false);
      this.this$0.menu.pc.insertRubrique(periodeCourante, this.this$0.selectedOne, this.this$0.menu.pc.usedRubID(4), this.this$0.selectedMotif, (Double)null, ((Number)this.this$0.tTotHS140.getValue()).doubleValue(), false, false);
      this.this$0.menu.pc.insertRubrique(periodeCourante, this.this$0.selectedOne, this.this$0.menu.pc.usedRubID(5), this.this$0.selectedMotif, (Double)null, ((Number)this.this$0.tTotHS150.getValue()).doubleValue(), false, false);
      this.this$0.menu.pc.insertRubrique(periodeCourante, this.this$0.selectedOne, this.this$0.menu.pc.usedRubID(6), this.this$0.selectedMotif, (Double)null, ((Number)this.this$0.tTotHS200.getValue()).doubleValue(), false, false);
      this.this$0.menu.pc.insertRubrique(periodeCourante, this.this$0.selectedOne, this.this$0.menu.pc.usedRubID(7), this.this$0.selectedMotif, (Double)null, ((Number)this.this$0.tTotPP.getValue()).doubleValue(), false, false);
      this.this$0.menu.pc.insertRubrique(periodeCourante, this.this$0.selectedOne, this.this$0.menu.pc.usedRubID(8), this.this$0.selectedMotif, (Double)null, ((Number)this.this$0.tTotPE.getValue()).doubleValue(), false, false);
      this.this$0.afficherRubriquePaie();
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "Valorisation d'HS valid\u00e9e avec succ\u00e9s!", false);
      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.btnValoriserHS.setEnabled(true);
   }
}
