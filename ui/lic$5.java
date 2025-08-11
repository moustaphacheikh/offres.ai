package com.mccmr.ui;

import javax.swing.JFrame;

class lic$5 implements Runnable {
   lic$5() {
   }

   public void run() {
      lic dialog = new lic(new JFrame(), true);
      dialog.addWindowListener(new lic.5.1(this));
      dialog.setVisible(true);
   }
}
