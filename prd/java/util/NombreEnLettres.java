package com.mccmr.util;

import java.util.Locale;

public class NombreEnLettres {
   public static final String FR = "FR";
   public static final String BE = "BE";
   static String stx = "FR";

   public NombreEnLettres() {
   }

   public String convertirEnMRO(double var1, String var3) {
      String doubleEnString = String.format(Locale.FRENCH, "%.2f", nombre);
      String strEntiere = doubleEnString.substring(0, doubleEnString.length() - 3);
      String strFraction = doubleEnString.substring(doubleEnString.length() - 2, doubleEnString.length());
      String mot = "N/A";

      try {
         if (!strEntiere.equals("0")) {
            String var10000 = this.convertirEntier(Long.parseLong(strEntiere), syntaxe);
            mot = var10000 + " MRU" + (Long.parseLong(strEntiere) > 1L ? "" : "") + (!strFraction.equals("00") ? " et " : "");
         }

         if (!strFraction.equals("00")) {
            mot = mot + this.convertirEntier(Long.parseLong(strFraction), syntaxe) + " centime" + (Long.parseLong(strFraction) > 1L ? "s" : "");
         }
      } catch (Exception e) {
         mot = e.getMessage();
      }

      return mot;
   }

   public String convertirEntier(long var1, String var3) throws Exception {
      stx = syntaxe;
      if (nombre > 999999999999L) {
         throw new Exception("Le nombre demand\u0623\u00a9 est trop grand...");
      } else {
         return this.entierEnLettres(String.valueOf(nombre));
      }
   }

   private String entierEnLettres(String var1) {
      String mot = "";
      String MILLIARD = " milliard";
      String MILLION = " million";
      String MILLE = " mille";

      while(strNombre.length() > 0) {
         int longueur = strNombre.length();
         String groupe = "";
         String chaineAConvertir = "";
         if (longueur > 9) {
            groupe = " milliard";
         } else if (longueur > 6) {
            groupe = " million";
         } else {
            if (longueur <= 3) {
               return (mot + dechiffrage(strNombre)).trim();
            }

            groupe = " mille";
         }

         long test = (long)longueur;
         if ((long)longueur % 3L == 0L) {
            chaineAConvertir = strNombre.substring(0, 3);
            strNombre = strNombre.substring(3);
            if (Integer.parseInt(chaineAConvertir) > 1) {
               mot = mot + dechiffrage(chaineAConvertir) + groupe + (!groupe.equals(" mille") ? "s" : "");
            } else if (Integer.parseInt(chaineAConvertir) > 0) {
               mot = mot + groupe;
            }
         } else {
            if ((test - 1L) % 3L == 0L) {
               chaineAConvertir = strNombre.substring(0, 1);
               strNombre = strNombre.substring(1);
            } else {
               chaineAConvertir = strNombre.substring(0, 2);
               strNombre = strNombre.substring(2);
            }

            if (chaineAConvertir.equals("1") && groupe.equals(" mille")) {
               mot = mot + groupe;
            } else {
               mot = mot + sousDechiffrage(chaineAConvertir) + groupe + (Integer.parseInt(chaineAConvertir) > 1 && !groupe.equals(" mille") ? "s" : "");
            }
         }
      }

      return mot.trim();
   }

   private static String dechiffrage(String var0) {
      String plu = "s";
      String mot = "";
      String CENT = "cent";
      String chaineAConvertir = "";
      int longueur = strNombre.length();
      if ((long)longueur % 3L == 0L) {
         chaineAConvertir = strNombre.substring(0, 1);
         if (Integer.parseInt(chaineAConvertir) > 1) {
            mot = mot + sousDechiffrage(chaineAConvertir);
         }

         if (Integer.parseInt(chaineAConvertir) > 0) {
            mot = mot + " cent" + (Integer.parseInt(strNombre.substring(1)) <= 0 && Integer.parseInt(chaineAConvertir) > 1 ? "s" : "");
         }

         strNombre = strNombre.substring(1);
         if (Integer.parseInt(strNombre) > 0) {
            mot = mot + sousDechiffrage(strNombre);
         }

         return String.format("%s", mot);
      } else {
         return sousDechiffrage(strNombre);
      }
   }

   private static String sousDechiffrage(String var0) {
      String mot = "";
      String et = "";
      String[] tabnb = new String[]{"z\u00e9ro", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf", "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix-sept", "dix-huit", "dix-neuf"};
      String[] tabdix = new String[]{"vingt", "trente", "quarante", "cinquante", "soixante", "septante", "quatre-vingt", "nonante"};

      while(nombre.length() > 0) {
         int longueur = nombre.length();
         et = "";
         switch (longueur) {
            case 1:
               mot = mot + " " + tabnb[Integer.parseInt(nombre)] + et;
               nombre = "";
               break;
            case 2:
               if (Integer.parseInt(nombre) < 20) {
                  mot = mot + " " + tabnb[Integer.parseInt(nombre)] + et;
                  nombre = "";
               } else if (Integer.parseInt(nombre) >= 20) {
                  if (Integer.parseInt(nombre.substring(1, 2)) != 1 || (Integer.parseInt(nombre) >= 80 || !stx.equals("FR")) && !stx.equals("BE")) {
                     if (Integer.parseInt(nombre) == 80 && stx.equals("FR")) {
                        et = "s";
                     }
                  } else {
                     et = " et";
                  }

                  mot = mot + " " + tabdix[Integer.parseInt(nombre.substring(0, 1)) - ((Integer.parseInt(nombre.substring(0, 1)) == 7 || Integer.parseInt(nombre.substring(0, 1)) == 9) && stx.equals("FR") ? 3 : 2)] + et;
                  if ((Integer.parseInt(nombre) >= 70 || !stx.equals("FR")) && !stx.equals("BE")) {
                     if (Integer.parseInt(nombre) < 80) {
                        int var10000 = Integer.parseInt(nombre);
                        nombre = "" + (var10000 - 60);
                     } else if (Integer.parseInt(nombre) < 100) {
                        int var7 = Integer.parseInt(nombre);
                        nombre = "" + (var7 - 80);
                     }
                  } else {
                     nombre = nombre.substring(1, 2);
                  }
               }
         }
      }

      if (mot.trim().length() != tabnb[0].length()) {
         mot = mot.replaceFirst(" " + tabnb[0], "");
      }

      if (stx.equals("BE")) {
         mot = mot.replaceFirst(tabdix[6], "octante");
      }

      return String.format("%s", mot);
   }
}
