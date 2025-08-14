package com.mccmr.util;

public class Calcul {
   String chaine;
   double resultat;
   int erreur;
   int i;
   String txtErr;

   public Calcul() {
   }

   public double calculer(String var1) {
      this.chaine = s + ";";
      this.resultat = this.expression();
      if (this.chaine.length() - 1 != this.i) {
         this.erreur = 10;
         this.txtErr = "La chaine ne se termine pas correctement";
      }

      return this.resultat;
   }

   private double expression() {
      double n = this.terme();

      while(this.chaine.charAt(this.i) == '+' || this.chaine.charAt(this.i) == '-') {
         if (this.chaine.charAt(this.i) == '+') {
            ++this.i;
            n += this.terme();
         } else if (this.chaine.charAt(this.i) == '-') {
            ++this.i;
            n -= this.terme();
         }
      }

      return n;
   }

   private double terme() {
      double n = this.operande();
      double n2 = (double)0.0F;

      while(this.chaine.charAt(this.i) == '*' || this.chaine.charAt(this.i) == '/') {
         if (this.chaine.charAt(this.i) == '*') {
            ++this.i;
            n *= this.operande();
         }

         if (this.chaine.charAt(this.i) == '/') {
            ++this.i;
            n2 = this.operande();
            if (n2 == (double)0.0F) {
               this.erreur = 3;
               this.txtErr = "Division par 0";
            }

            n /= n2;
         }
      }

      return n;
   }

   private double operande() {
      double n = (double)0.0F;
      if (this.chaine.charAt(this.i) != '(') {
         n = this.nombre();
      } else {
         ++this.i;
         n = this.expression();
         if (this.chaine.charAt(this.i) != ')') {
            this.erreur = 2;
            this.txtErr = "Il manque une paranth\u00e8se fermante:";
         }

         ++this.i;
      }

      return n;
   }

   private double nombre() {
      String AvantVirgule = this.chiffre();
      if (this.erreur == 0) {
         String ApresVirgule = "";
         if (this.chaine.charAt(this.i) != '.') {
            return Double.parseDouble(AvantVirgule);
         }

         ++this.i;
         ApresVirgule = this.chiffre();
         if (this.chaine.charAt(this.i) != '.') {
            return Double.parseDouble(AvantVirgule + "." + ApresVirgule);
         }

         this.erreur = 5;
         this.txtErr = "Expression invalide";
      }

      return (double)0.0F;
   }

   private String chiffre() {
      StringBuffer s = new StringBuffer();
      if (this.chaine.charAt(this.i) >= '0' && this.chaine.charAt(this.i) <= '9') {
         while(this.chaine.charAt(this.i) >= '0' && this.chaine.charAt(this.i) <= '9') {
            s.append(this.chaine.charAt(this.i++));
         }
      } else {
         this.erreur = 1;
         this.txtErr = "J'attendais un chiffre; caract\u00e8re trouv\u00e9!" + this.chaine.substring(0, 1 + this.i) + "<--";
      }

      return s.toString();
   }

   public double GetResultat() {
      return this.resultat;
   }

   public int GetErreur() {
      return this.erreur;
   }

   public String GettxtErr() {
      return this.txtErr;
   }
}
