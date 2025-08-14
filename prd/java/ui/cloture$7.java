package com.mccmr.ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class cloture$7 extends FocusAdapter {
   // $FF: synthetic field
   final cloture this$0;

   cloture$7(final cloture var1) {
      this.this$0 = this$0;
   }

   public void focusLost(FocusEvent var1) {
      this.this$0.tIdSalarieFocusLost(evt);
   }
}
