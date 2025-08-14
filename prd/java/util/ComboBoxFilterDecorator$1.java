package com.mccmr.util;

import java.util.function.Consumer;

class ComboBoxFilterDecorator$1 implements Consumer<Boolean> {
   // $FF: synthetic field
   final ComboBoxFilterDecorator this$0;

   ComboBoxFilterDecorator$1(final ComboBoxFilterDecorator var1) {
      this.this$0 = this$0;
   }

   public void accept(Boolean var1) {
      if (aBoolean) {
         this.this$0.selectedItem = this.this$0.comboBox.getSelectedItem();
      } else {
         this.this$0.comboBox.setSelectedItem(this.this$0.selectedItem);
         this.this$0.filterEditor.setItem(this.this$0.selectedItem);
      }

   }
}
