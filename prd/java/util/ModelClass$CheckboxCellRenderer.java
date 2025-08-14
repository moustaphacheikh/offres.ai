package com.mccmr.util;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ModelClass$CheckboxCellRenderer extends DefaultTableCellRenderer {
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$CheckboxCellRenderer(final ModelClass var1) {
      this.this$0 = this$0;
   }

   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      return new JCheckBox("", (Icon)null, (Boolean)value);
   }
}
