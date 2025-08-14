package com.mccmr.util;

import java.util.Comparator;

public class ModelClass$NumberStringComparator implements Comparator<String> {
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$NumberStringComparator(final ModelClass var1) {
      this.this$0 = this$0;
   }

   public int compare(String var1, String var2) {
      if (c1.length() == 0) {
         c1 = "0";
      }

      if (c2.length() == 0) {
         c2 = "0";
      }

      return (new Integer(c1)).compareTo(new Integer(c2));
   }
}
