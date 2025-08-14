package com.mccmr.util;

import com.mccmr.ui.menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;

class StatusBar$2 implements ActionListener {
   // $FF: synthetic field
   final JLabel val$label;
   // $FF: synthetic field
   final menu val$menu;
   // $FF: synthetic field
   final SimpleDateFormat val$sdf;
   // $FF: synthetic field
   final StatusBar this$0;

   StatusBar$2(final StatusBar var1, final JLabel var2, final menu var3, final SimpleDateFormat var4) {
      this.val$label = var2;
      this.val$menu = var3;
      this.val$sdf = var4;
      this.this$0 = this$0;
   }

   public void actionPerformed(ActionEvent var1) {
      JLabel var10000 = this.val$label;
      String var10001 = this.val$menu.paramsGen.getNomEntreprise();
      var10000.setText("SOCIETE: " + var10001 + " - PC: " + this.val$sdf.format(this.val$menu.paramsGen.getPeriodeCourante()));
   }
}
