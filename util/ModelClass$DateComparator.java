package com.mccmr.util;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ModelClass$DateComparator implements Comparator<String> {
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$DateComparator(final ModelClass var1) {
      this.this$0 = this$0;
   }

   public int compare(String var1, String var2) {
      int r = 0;
      Date d1 = new Date();
      Date d2 = new Date();

      try {
         if (c1.length() > 5) {
            d1 = (new SimpleDateFormat("dd/MM/yy")).parse(c1);
         }

         if (c2.length() > 5) {
            d2 = (new SimpleDateFormat("dd/MM/yy")).parse(c2);
         }

         r = d1.compareTo(d2);
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }
}
