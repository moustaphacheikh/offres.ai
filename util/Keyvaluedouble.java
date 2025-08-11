package com.mccmr.util;

import java.io.Serializable;

public class Keyvaluedouble implements Serializable {
   private String key;
   private double value;

   public Keyvaluedouble() {
   }

   public Keyvaluedouble(String var1, double var2) {
      this.key = key;
      this.value = value;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String var1) {
      this.key = key;
   }

   public double getValue() {
      return this.value;
   }

   public void setValue(double var1) {
      this.value = value;
   }
}
