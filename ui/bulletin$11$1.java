package com.mccmr.ui;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

class bulletin$11$1 extends Authenticator {
   // $FF: synthetic field
   final String val$username;
   // $FF: synthetic field
   final String val$password;
   // $FF: synthetic field
   final bulletin.11 this$1;

   bulletin$11$1(final bulletin.11 var1, final String var2, final String var3) {
      this.val$username = var2;
      this.val$password = var3;
      this.this$1 = this$1;
   }

   protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(this.val$username, this.val$password);
   }
}
