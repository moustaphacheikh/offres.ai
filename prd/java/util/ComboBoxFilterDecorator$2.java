package com.mccmr.util;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.UIManager;

class ComboBoxFilterDecorator$2 implements FocusListener {
   // $FF: synthetic field
   final JLabel val$filterLabel;
   // $FF: synthetic field
   final ComboBoxFilterDecorator this$0;

   ComboBoxFilterDecorator$2(final ComboBoxFilterDecorator var1, final JLabel var2) {
      this.val$filterLabel = var2;
      this.this$0 = this$0;
   }

   public void focusGained(FocusEvent var1) {
      this.val$filterLabel.setBorder(BorderFactory.createLoweredBevelBorder());
   }

   public void focusLost(FocusEvent var1) {
      this.val$filterLabel.setBorder(UIManager.getBorder("TextField.border"));
      this.this$0.resetFilterComponent();
   }
}
