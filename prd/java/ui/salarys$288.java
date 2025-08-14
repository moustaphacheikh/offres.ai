package com.mccmr.ui;

import com.mccmr.util.GeneralLib;
import java.util.Date;

class salarys$288 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$288(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.buttonConges.setEnabled(false);
      if (!this.this$0.selectedOne.isEnConge()) {
         if (this.this$0.menu.dialect.toString().contains("Oracle")) {
            GeneralLib var10000 = this.this$0.menu.gl;
            int var10001 = this.this$0.selectedOne.getId();
            var10000.exQuery("delete from Paie where employe=" + var10001 + " and periode =TO_DATE('" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD')");
            var10000 = this.this$0.menu.gl;
            var10001 = this.this$0.selectedOne.getId();
            var10000.exQuery("delete from Rubriquepaie where employe=" + var10001 + " and periode =TO_DATE('" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD')");
            var10000 = this.this$0.menu.gl;
            var10001 = this.this$0.selectedOne.getId();
            var10000.exQuery("delete from Njtsalarie where employe=" + var10001 + " and periode = TO_DATE('" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD')");
            var10000 = this.this$0.menu.gl;
            var10001 = this.this$0.selectedOne.getId();
            var10000.exQuery("delete from Jour where employe=" + var10001 + " and periode = TO_DATE('" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD')");
            var10000 = this.this$0.menu.gl;
            var10001 = this.this$0.selectedOne.getId();
            var10000.exQuery("delete from Weekot where employe=" + var10001 + " and periode = TO_DATE('" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD')");
         } else {
            GeneralLib var6 = this.this$0.menu.gl;
            int var15 = this.this$0.selectedOne.getId();
            var6.exQuery("delete from Paie where employe=" + var15 + " and periode = '" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "'");
            var6 = this.this$0.menu.gl;
            var15 = this.this$0.selectedOne.getId();
            var6.exQuery("delete from Rubriquepaie where employe=" + var15 + " and periode = '" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "'");
            var6 = this.this$0.menu.gl;
            var15 = this.this$0.selectedOne.getId();
            var6.exQuery("delete from Njtsalarie where employe=" + var15 + " and periode = '" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "'");
            var6 = this.this$0.menu.gl;
            var15 = this.this$0.selectedOne.getId();
            var6.exQuery("delete from Jour where employe=" + var15 + " and periode = '" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "'");
            var6 = this.this$0.menu.gl;
            var15 = this.this$0.selectedOne.getId();
            var6.exQuery("delete from Weekot where employe=" + var15 + " and periode = '" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "'");
         }

         this.this$0.selectedOne.setEnConge(true);
      } else {
         this.this$0.selectedOne.setEnConge(false);
         this.this$0.menu.pc.insertNJT(this.this$0.menu.paramsGen.getPeriodeCourante(), this.this$0.selectedOne, this.this$0.menu.motifSN, this.this$0.menu.paramsGen.getNjtDefault());
         Date derPaiePeriode = this.this$0.menu.pc.dernierPaiePeriode(this.this$0.selectedOne);
         this.this$0.menu.pc.copyingRubriquePaieFixe(this.this$0.selectedOne, derPaiePeriode, this.this$0.menu.paramsGen.getPeriodeCourante());
      }

      if (this.this$0.menu.gl.updateOcurance(this.this$0.selectedOne)) {
         this.this$0.menu.showMsg(this.this$0.menu.employeFrame, "Op\u00e9ration termin\u00e9e");
         this.this$0.findByID(this.this$0.selectedOne.getId(), false);
         this.this$0.dataListInit.removeIf((var1x) -> var1x.getId() == this.this$0.selectedOne.getId());
         this.this$0.dataListInit.add(this.this$0.selectedOne);
         this.this$0.afficherListe();
      } else {
         this.this$0.menu.showErrMsg(this.this$0.menu.employeFrame, "Err: Op\u00e9ration echou\u00e9e");
      }

      this.this$0.buttonConges.setEnabled(true);
   }
}
