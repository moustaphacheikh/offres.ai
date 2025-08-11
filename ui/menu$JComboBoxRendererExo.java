package com.mccmr.ui;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class menu$JComboBoxRendererExo extends JLabel implements ListCellRenderer {
   // $FF: synthetic field
   final menu this$0;

   public menu$JComboBoxRendererExo(final menu var1) {
      this.this$0 = this$0;
   }

   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      if (value == null) {
         this.setText("N/D");
      }

      if (value instanceof Date) {
         DateFormat dateFormat = new SimpleDateFormat("yyyy");
         Date rs = (Date)value;
         this.setText(dateFormat.format(rs).toUpperCase());
      }

      if (isSelected) {
         this.setBackground(list.getSelectionBackground());
      } else {
         this.setBackground(list.getBackground());
      }

      return this;
   }
}
