package com.mccmr.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

class StatusBar$AngledLinesWindowsCornerIcon implements Icon {
   private final Color WHITE_LINE_COLOR;
   private final Color GRAY_LINE_COLOR;
   private static final int WIDTH = 13;
   private static final int HEIGHT = 13;
   // $FF: synthetic field
   final StatusBar this$0;

   StatusBar$AngledLinesWindowsCornerIcon(final StatusBar var1) {
      this.this$0 = this$0;
      this.WHITE_LINE_COLOR = new Color(255, 255, 255);
      this.GRAY_LINE_COLOR = new Color(172, 168, 153);
   }

   public int getIconHeight() {
      return 13;
   }

   public int getIconWidth() {
      return 13;
   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      g.setColor(this.WHITE_LINE_COLOR);
      g.drawLine(0, 12, 12, 0);
      g.drawLine(5, 12, 12, 5);
      g.drawLine(10, 12, 12, 10);
      g.setColor(this.GRAY_LINE_COLOR);
      g.drawLine(1, 12, 12, 1);
      g.drawLine(2, 12, 12, 2);
      g.drawLine(3, 12, 12, 3);
      g.drawLine(6, 12, 12, 6);
      g.drawLine(7, 12, 12, 7);
      g.drawLine(8, 12, 12, 8);
      g.drawLine(11, 12, 12, 11);
      g.drawLine(12, 12, 12, 12);
   }
}
