package com.mccmr.util;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

class ComboBoxFilterDecorator$4 implements PopupMenuListener {
   // $FF: synthetic field
   final ComboBoxFilterDecorator this$0;

   ComboBoxFilterDecorator$4(final ComboBoxFilterDecorator var1) {
      this.this$0 = this$0;
   }

   public void popupMenuWillBecomeVisible(PopupMenuEvent var1) {
   }

   public void popupMenuWillBecomeInvisible(PopupMenuEvent var1) {
      this.this$0.resetFilterComponent();
   }

   public void popupMenuCanceled(PopupMenuEvent var1) {
      this.this$0.resetFilterComponent();
   }
}
