package com.mccmr.ui;

import java.io.IOException;
import jxl.write.WriteException;

class declarations$7 extends Thread {
   // $FF: synthetic field
   final int val$month;
   // $FF: synthetic field
   final declarations this$0;

   declarations$7(final declarations var1, final int var2) {
      this.val$month = var2;
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
      switch (this.this$0.tEtat.getSelectedItem().toString()) {
         case "D\u00e9claration de l'ITS":
            this.this$0.decITS();
            break;
         case "D\u00e9claration de la CNSS":
            if (this.val$month % 3 == 0) {
               this.this$0.decCNSS();
            } else {
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Cette d\u00e9claration est trimestrielle!", true);
            }
            break;
         case "Liste nominative CNSS - Excel":
            if (this.val$month % 3 == 0) {
               if (this.this$0.lnCnssDS == null) {
                  this.this$0.menu.viewMessage(this.this$0.msgLabel, "Afficher la declaration de la CNSS avant la liste.", false);
               } else {
                  try {
                     this.this$0.listeNominativeCNSSExcel();
                     this.this$0.menu.viewMessage(this.this$0.msgLabel, "Document export\u00e9 vers Excel", false);
                  } catch (IOException e) {
                     e.printStackTrace();
                     this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
                  } catch (WriteException e) {
                     e.printStackTrace();
                     this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
                  }
               }
            } else {
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Cette d\u00e9claration est trimestrielle!", true);
            }
            break;
         case "D\u00e9claration de la CNAM":
            if (this.val$month % 3 == 0) {
               this.this$0.decCNAM();
            } else {
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Cette d\u00e9claration est trimestrielle!", true);
            }
            break;
         case "Liste nominative CNAM - Excel":
            if (this.val$month % 3 == 0) {
               if (this.this$0.lnCnamDS == null) {
                  this.this$0.menu.viewMessage(this.this$0.msgLabel, "Afficher la declaration de la CNAM avant la liste.", false);
               } else {
                  try {
                     this.this$0.listeNominativeCNAMExcel();
                     this.this$0.menu.viewMessage(this.this$0.msgLabel, "Document export\u00e9 vers Excel", false);
                  } catch (IOException e) {
                     e.printStackTrace();
                     this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
                  } catch (WriteException e) {
                     e.printStackTrace();
                     this.this$0.menu.viewMessage(this.this$0.msgLabel, "Err: Exportation echou\u00e9", true);
                  }
               }
            } else {
               this.this$0.menu.viewMessage(this.this$0.msgLabel, "Cette d\u00e9claration est trimestrielle!", true);
            }
      }

      this.this$0.progressBar.setIndeterminate(false);
   }
}
