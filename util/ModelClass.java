package com.mccmr.util;

import com.mccmr.ui.menu;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ModelClass {
   public menu menu;
   DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
   DateFormat dfp = new SimpleDateFormat("MMM yyyy");

   public ModelClass(menu var1) {
      this.menu = menu;
   }
}
