package com.mccmr.util;

public class FichePaieDetail {
   private int codeRub;
   private String libelle;
   private String sens;
   private double base;
   private double nombre;
   private double montant;

   public FichePaieDetail() {
   }

   public int getCodeRub() {
      return this.codeRub;
   }

   public void setCodeRub(int var1) {
      this.codeRub = codeRub;
   }

   public String getLibelle() {
      return this.libelle;
   }

   public void setLibelle(String var1) {
      this.libelle = libelle;
   }

   public String getSens() {
      return this.sens;
   }

   public void setSens(String var1) {
      this.sens = sens;
   }

   public double getBase() {
      return this.base;
   }

   public void setBase(double var1) {
      this.base = base;
   }

   public double getNombre() {
      return this.nombre;
   }

   public void setNombre(double var1) {
      this.nombre = nombre;
   }

   public double getMontant() {
      return this.montant;
   }

   public void setMontant(double var1) {
      this.montant = montant;
   }
}
