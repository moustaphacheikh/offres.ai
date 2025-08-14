package com.mccmr.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JLabel;

class StatusBar$1 implements ActionListener {
   // $FF: synthetic field
   final JLabel val$welcomedate;
   // $FF: synthetic field
   final StatusBar this$0;

   StatusBar$1(final StatusBar var1, final JLabel var2) {
      this.val$welcomedate = var2;
      this.this$0 = this$0;
   }

   public void actionPerformed(ActionEvent var1) {
      Date now = new Date();
      String ss = DateFormat.getDateTimeInstance().format(now);
      this.val$welcomedate.setText(ss);
      this.val$welcomedate.setToolTipText("Aujourd'hui est le :" + ss);
   }
}
