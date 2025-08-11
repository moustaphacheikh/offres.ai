package com.mccmr.ui;

import com.mccmr.entity.Utilisateurs;

class login$1 extends Thread {
   // $FF: synthetic field
   final login this$0;

   login$1(final login var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setValue(1);
      Utilisateurs user = this.this$0.UserByID(this.this$0.tLogin.getText());
      menu m = new menu();
      m.demo = this.this$0.demo;
      m.appVersion = this.this$0.appVersion;
      m.newVersionIsFree = this.this$0.newVersionIsFree;
      this.this$0.initMenuFrames(m, user);
      this.this$0.progressBar.setValue(100);
      m.setVisible(true);
      m.initStatusBar(user.getLogin());
      this.this$0.closeMe();
   }
}
