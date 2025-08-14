package com.mccmr.ui;

import java.util.Date;

public class salarys$Cumul {
   private Date date;
   private String libelle;
   private double montant;
   // $FF: synthetic field
   final salarys this$0;

   public Date getDate() {
      return this.date;
   }

   public void setDate(Date var1) {
      this.date = date;
   }

   public salarys$Cumul(final salarys var1, Date var2, String var3, double var4) {
      this.this$0 = this$0;
      this.date = date;
      this.libelle = libelle;
      this.montant = montant;
   }

   public salarys$Cumul(final salarys var1) {
      this.this$0 = this$0;
   }

   public String getLibelle() {
      return this.libelle;
   }

   public void setLibelle(String var1) {
      this.libelle = libelle;
   }

   public double getMontant() {
      return this.montant;
   }

   public void setMontant(double var1) {
      this.montant = montant;
   }
}
