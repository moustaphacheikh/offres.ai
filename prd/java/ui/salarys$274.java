package com.mccmr.ui;

import com.mccmr.util.GeneralLib;

class salarys$274 extends Thread {
   // $FF: synthetic field
   final salarys this$0;

   salarys$274(final salarys var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnDelConges.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.progressBar.setStringPainted(false);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);

      try {
         if (this.this$0.selectedConges != null) {
            if (this.this$0.menu.gl.deleteOcurance(this.this$0.selectedConges)) {
               if (this.this$0.menu.dialect.toString().contains("Oracle")) {
                  GeneralLib var10000 = this.this$0.menu.gl;
                  int var10001 = this.this$0.selectedOne.getId();
                  var10000.exQuery("delete from Paie where motif=2 and employe=" + var10001 + " and periode=TO_DATE('" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
                  var10000 = this.this$0.menu.gl;
                  var10001 = this.this$0.selectedOne.getId();
                  var10000.exQuery("delete from Rubriquepaie where motif=2 and employe=" + var10001 + " and periode=TO_DATE('" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "','YYYY-MM-DD') ");
               } else {
                  GeneralLib var4 = this.this$0.menu.gl;
                  int var7 = this.this$0.selectedOne.getId();
                  var4.exQuery("delete from Paie where motif=2 and employe=" + var7 + " and periode='" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "' ");
                  var4 = this.this$0.menu.gl;
                  var7 = this.this$0.selectedOne.getId();
                  var4.exQuery("delete from Rubriquepaie where motif=2 and employe=" + var7 + " and periode='" + this.this$0.menu.df.format(this.this$0.menu.paramsGen.getPeriodeCourante()) + "' ");
               }

               this.this$0.afficherConges();
               this.this$0.afficherRubriquePaie();
               this.this$0.tDernierDepart.setDate(this.this$0.menu.pc.dernierDepart(this.this$0.selectedOne));
               this.this$0.tCumulBrutNonImposableInitial.setValue(this.this$0.selectedOne.getCumulBrutNonImposableInitial());
               this.this$0.tCumulBrutImposableInitial.setValue(this.this$0.selectedOne.getCumulBrutImposableInitial());
               this.this$0.tCumulBrutImposable.setValue(this.this$0.menu.pc.cumulTypeById(this.this$0.selectedOne, "BI"));
               this.this$0.tCumulBrutNonImposable.setValue(this.this$0.menu.pc.cumulTypeById(this.this$0.selectedOne, "BNI"));
               this.this$0.tCumulRet.setValue(this.this$0.menu.pc.cumulTypeById(this.this$0.selectedOne, "RET"));
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Op\u00e9ration effecut\u00e9e avec succ\u00e9!", false);
            } else {
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Op\u00e9ration echou\u00e9e!", true);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.progressBar.setString("0%");
      this.this$0.progressBar.setStringPainted(true);
      this.this$0.btnDelConges.setEnabled(true);
   }
}
