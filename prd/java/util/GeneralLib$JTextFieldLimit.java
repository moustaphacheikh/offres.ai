package com.mccmr.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class GeneralLib$JTextFieldLimit extends PlainDocument {
   private int limit;
   private boolean toUppercase;
   // $FF: synthetic field
   final GeneralLib this$0;

   public GeneralLib$JTextFieldLimit(final GeneralLib var1, int var2) {
      this.this$0 = this$0;
      this.toUppercase = false;
      this.limit = limit;
   }

   public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
      if (str != null) {
         if (this.getLength() + str.length() <= this.limit) {
            if (this.toUppercase) {
               str = str.toUpperCase();
            }

            super.insertString(offset, str, attr);
         }

      }
   }
}
