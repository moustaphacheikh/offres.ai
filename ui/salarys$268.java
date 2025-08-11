package com.mccmr.ui;

import com.mccmr.entity.Retenuesaecheances;
import java.util.ArrayList;

class salarys$268 extends Thread {
   // $FF: synthetic field
   final long val$id;
   // $FF: synthetic field
   final salarys this$0;

   salarys$268(final salarys var1, final long var2) {
      this.val$id = var2;
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.btnDelete.setEnabled(false);
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
      this.this$0.menu.gl.exQuery("delete from Paie where employe=" + this.this$0.selectedOne.getId());
      this.this$0.menu.gl.exQuery("delete from Rubriquepaie where employe=" + this.this$0.selectedOne.getId());
      this.this$0.menu.gl.exQuery("delete from Njtsalarie where employe=" + this.this$0.selectedOne.getId());
      this.this$0.menu.gl.exQuery("delete from Enfants where employe=" + this.this$0.selectedOne.getId());
      this.this$0.menu.gl.exQuery("delete from Diplome where employe=" + this.this$0.selectedOne.getId());
      this.this$0.menu.gl.exQuery("delete from Jour where employe=" + this.this$0.selectedOne.getId());
      this.this$0.menu.gl.exQuery("delete from Weekot where employe=" + this.this$0.selectedOne.getId());
      this.this$0.menu.gl.exQuery("delete from Conges where employe=" + this.this$0.selectedOne.getId());

      for(Retenuesaecheances rs : new ArrayList(this.this$0.selectedOne.getRetenuesaecheanceses())) {
         this.this$0.menu.gl.exQuery("delete from Tranchesretenuesaecheances where retenueAEcheances=" + rs.getId());
      }

      this.this$0.menu.gl.exQuery("delete from Retenuesaecheances where employe=" + this.this$0.selectedOne.getId());
      if (this.this$0.menu.gl.deleteOcurance(this.this$0.selectedOne)) {
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Element supprim\u00e9", false);
         this.this$0.clearFields();
         this.this$0.dataListInit.removeIf((var2) -> (long)var2.getId() == id);
         this.this$0.afficherListe();
      }

      this.this$0.progressBar.setIndeterminate(false);
      this.this$0.btnDelete.setEnabled(true);
   }
}
