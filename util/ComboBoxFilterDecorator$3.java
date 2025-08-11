package com.mccmr.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class ComboBoxFilterDecorator$3 extends KeyAdapter {
   // $FF: synthetic field
   final ComboBoxFilterDecorator this$0;

   ComboBoxFilterDecorator$3(final ComboBoxFilterDecorator var1) {
      this.this$0 = this$0;
   }

   public void keyPressed(KeyEvent var1) {
      char keyChar = e.getKeyChar();
      if (Character.isDefined(keyChar)) {
         int keyCode = e.getKeyCode();
         switch (keyCode) {
            case 8:
               this.this$0.filterEditor.removeCharAtEnd();
               break;
            case 10:
               this.this$0.selectedItem = this.this$0.comboBox.getSelectedItem();
               this.this$0.resetFilterComponent();
               return;
            case 27:
               this.this$0.resetFilterComponent();
               return;
            case 127:
               return;
            default:
               this.this$0.filterEditor.addChar(keyChar);
         }

         if (!this.this$0.comboBox.isPopupVisible()) {
            this.this$0.comboBox.showPopup();
         }

         if (this.this$0.filterEditor.isEditing() && this.this$0.filterEditor.getText().length() > 0) {
            this.this$0.applyFilter();
         } else {
            this.this$0.comboBox.hidePopup();
            this.this$0.resetFilterComponent();
         }

      }
   }
}
