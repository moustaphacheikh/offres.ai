package com.mccmr.util;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ModelClass$BoolCellRenderer extends DefaultTableCellRenderer {
   private Icon falseImage;
   private Icon trueImage;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$BoolCellRenderer(final ModelClass var1) {
      this.this$0 = this$0;
      this.falseImage = new ImageIcon("non.png");
      this.trueImage = new ImageIcon("oui.png");
   }

   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      Boolean val = (Boolean)value;
      this.setText("");
      if (val) {
         this.setIcon(this.trueImage);
      }

      return this;
   }
}
