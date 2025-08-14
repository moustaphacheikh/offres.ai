package com.mccmr.util;

import com.mccmr.entity.Employe;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Njtsalarie;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.ui.menu;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FonctionsPaie {
   public menu menu;

   public FonctionsPaie(menu var1) {
      this.menu = menu;
   }

   public Double F24_augmentationSalaireFixe(Employe var1, Date var2) {
      double r = (double)0.0F;
      return this.menu.pc.augmentationSalaire(employe, periode);
   }

   public double F01_NJT(Employe var1, Motif var2, Date var3) {
      Njtsalarie njt = this.menu.pc.njtSalarieById(employe, motif, periode);
      return njt != null ? njt.getNjt() : (double)0.0F;
   }

   public Double F02_sbJour(Employe var1, Motif var2, Date var3) {
      double r = (double)0.0F;
      Rubriquepaie rp = this.menu.pc.rubriquePaieById(employe, this.menu.pc.usedRubID(1), motif, periode);
      if (rp != null) {
         r = rp.getBase();
      }

      return r;
   }

   public Double F03_sbHoraire(Employe var1, Motif var2, Date var3) {
      double r = (double)0.0F;
      double sb = (double)0.0F;
      double hMois = (double)0.0F;
      Rubriquepaie o = this.menu.pc.rubriquePaieById(employe, this.menu.pc.usedRubID(1), motif, periode);
      if (o != null) {
         sb = o.getBase() * (double)30.0F;
         hMois = employe.getContratHeureSemaine() * (double)52.0F / (double)12.0F;
         r = sb / hMois;
      }

      return r;
   }

   public Double F04_TauxAnciennete(Employe var1, Date var2) {
      Double r = (double)0.0F;
      int annee = 0;
      double nj = (double)0.0F;
      long MILLISECONDS_PER_DAY = 86400000L;
      nj = (double)((periode.getTime() - employe.getDateAnciennete().getTime()) / 86400000L);
      double nb_jr_apres_un_an = nj - (double)365.0F;
      Double x = nb_jr_apres_un_an / (double)365.0F;
      annee = (int)Math.floor(x);
      if (annee >= 14) {
         if (annee >= 14 && annee < 15) {
            r = 0.28;
         }

         if (annee >= 15 && annee < 16) {
            r = 0.29;
         }

         if (annee >= 16) {
            r = 0.3;
         }
      } else {
         r = (double)annee * 0.02;
      }

      return r;
   }

   public Double F23_TauxAncienneteSpeciale(Employe var1, Date var2) {
      Double r = (double)0.0F;
      int annee = 0;
      double nj = (double)0.0F;
      long MILLISECONDS_PER_DAY = 86400000L;
      nj = (double)((periode.getTime() - employe.getDateAnciennete().getTime()) / 86400000L);
      double nb_jr_apres_un_an = nj - (double)365.0F;
      Double x = nb_jr_apres_un_an / (double)365.0F;
      annee = (int)Math.floor(x);
      if (annee >= 14) {
         if (annee >= 14 && annee < 15) {
            r = 0.28;
         }

         if (annee >= 15 && annee < 16) {
            r = 0.29;
         }

         if (annee >= 16) {
            r = 0.3 + (double)(annee - 16) * 0.01;
         }
      } else {
         r = (double)annee * 0.02;
      }

      return r;
   }

   public Double F23X_TauxAncienneteSpeciale(Employe var1, Date var2) {
      Double r = (double)0.0F;
      int annee = 0;
      double nj = (double)0.0F;
      long MILLISECONDS_PER_DAY = 86400000L;
      nj = (double)((periode.getTime() - employe.getDateAnciennete().getTime()) / 86400000L);
      double nb_jr_apres_un_an = nj - (double)365.0F;
      Double x = nb_jr_apres_un_an / (double)365.0F;
      annee = (int)Math.floor(x);
      if (annee >= 1) {
         if (annee > 15) {
            r = 0.44999999999999996 + (double)(annee - 15) * 0.02;
         } else {
            r = (double)annee * 0.03;
         }
      }

      return r;
   }

   public double F05_cumulBIDerDepart(Employe var1) {
      double r = (double)0.0F;
      r = this.menu.pc.cumulTypeById(employe, "BI");
      return r;
   }

   public Double F06_cumulBNIDerDepart(Employe var1) {
      double r = (double)0.0F;
      r = this.menu.pc.cumulTypeById(employe, "BNI");
      return r;
   }

   public Double F07_cumulRETDerDepart(Employe var1) {
      double r = (double)0.0F;
      r = this.menu.pc.cumulTypeById(employe, "RET");
      return r;
   }

   public Double F08_cumulBrut12DerMois(Employe var1) {
      Date periode = this.menu.gl.addRetriveDays(this.menu.paramsGen.getPeriodeCourante(), -390);

      try {
         periode = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periode));
      } catch (Exception e) {
         e.printStackTrace();
      }

      double r = this.menu.pc.cumulBrut12DerMois(employe, periode, this.menu.paramsGen.getPeriodeCourante()) + employe.getCumul12dminitial();
      return r;
   }

   public Double F09_salaireBrutMensuelFixe(Employe var1, Date var2) {
      List<Rubriquepaie> dl = new ArrayList(this.menu.pc.empRubriquepaie(employe, this.menu.motifSN, periode));
      return dl.stream().filter((var1x) -> var1x.isFixe() && var1x.getMotif().getId() == 1 && var1x.getRubrique().getSens().equalsIgnoreCase("G") && this.menu.df.format(var1x.getPeriode()).equalsIgnoreCase(this.menu.df.format(this.menu.paramsGen.getPeriodeCourante()))).mapToDouble((var0) -> var0.getMontant()).sum();
   }

   public Double F10_smig() {
      return this.menu.paramsGen.getSmig();
   }

   public Double F11_smigHoraire(Employe var1) {
      return this.F10_smig() * employe.getContratHeureSemaine() * (double)4.0F;
   }

   public Double F12_TauxLicenciement(Employe var1, Date var2) {
      Double r = (double)0.0F;
      long MILLISECONDS_PER_DAY = 86400000L;
      Date dateAnciennete = employe.getDateAnciennete();
      double nj = (double)((periode.getTime() - dateAnciennete.getTime() + 172800000L) / 86400000L);
      double annee = nj / (double)365.0F;
      if (annee >= (double)1.0F && annee < (double)5.0F) {
         r = (double)0.25F * annee;
      }

      if (annee >= (double)5.0F && annee < (double)10.0F) {
         r = (double)1.25F + (annee - (double)5.0F) * 0.3;
      }

      if (annee >= (double)10.0F) {
         r = (double)2.75F + (annee - (double)10.0F) * 0.35;
      }

      return r;
   }

   public Double F13_TauxLicenciementCollectif(Employe var1, Date var2) {
      Double r = (double)0.0F;
      long MILLISECONDS_PER_DAY = 86400000L;
      Date dateAnciennete = employe.getDateAnciennete();
      double nj = (double)((periode.getTime() - dateAnciennete.getTime() + 172800000L) / 86400000L);
      double annee = nj / (double)365.0F;
      if (annee > (double)1.0F && annee <= (double)5.0F) {
         r = 0.3 * annee;
      }

      if (annee > (double)5.0F && annee <= (double)10.0F) {
         r = (double)1.5F + (annee - (double)5.0F) * 0.4;
      }

      if (annee > (double)10.0F) {
         r = (double)3.5F + (annee - (double)10.0F) * (double)0.5F;
      }

      return r;
   }

   public Double F14_TauxRetraite(Employe var1, Date var2) {
      Double r = (double)0.0F;
      long MILLISECONDS_PER_DAY = 86400000L;
      Date dateAnciennete = employe.getDateAnciennete();
      double nj = (double)((periode.getTime() - dateAnciennete.getTime() + 172800000L) / 86400000L);
      double annee = nj / (double)365.0F;
      double txLic = this.F12_TauxLicenciement(employe, periode);
      if (annee > (double)1.0F && annee <= (double)5.0F) {
         r = 0.3 * txLic;
      }

      if (annee > (double)5.0F && annee <= (double)10.0F) {
         r = (double)0.5F * txLic;
      }

      if (annee > (double)10.0F && annee <= (double)20.0F) {
         r = (double)0.75F * txLic;
      }

      if (annee > (double)20.0F) {
         r = (double)1.0F * txLic;
      }

      return r;
   }

   public Double F15_TauxPSRA(Employe var1) {
      return employe.getTauxPsra();
   }

   public Float F16_TauxPreavis(Employe var1) {
      return employe.getNbMoisPreavis();
   }

   public Double F17_CumulNJTMC(Employe var1) {
      double r = (double)0.0F;
      return r;
   }

   public Integer F18_NbSmigRegion(Employe var1) {
      return employe.getOrigines().getNbSmighorPourIndConges();
   }

   public Double F19_TauxPresence(Employe var1, Date var2) {
      Double r = (double)0.0F;
      long MILLISECONDS_PER_DAY = 86400000L;
      double nj = (double)((periode.getTime() - employe.getDateAnciennete().getTime()) / 86400000L);
      double nbMois = nj / (double)30.0F;
      r = nbMois / (double)12.0F;
      return r;
   }

   public Double F20_BaseIndLogement(Employe var1) {
      return (Double)this.menu.stricturesIF.dataListInit_Grillelogements.stream().filter((var1x) -> var1x.getGrillesalairebase().getCategorie().equalsIgnoreCase(employe.getGrillesalairebase().getCategorie()) && var1x.getSituationFamiliale().equalsIgnoreCase(employe.getSituationFamiliale()) && var1x.getNbEnfants() <= employe.getEnfantss().size()).map((var0) -> var0.getMontant()).max(Double::compareTo).orElse((double)0.0F);
   }

   public Double F21_salaireNet(Employe var1, Date var2) {
      double r = (double)0.0F;
      double sbmf = this.F09_salaireBrutMensuelFixe(employe, periode);
      double x_cnss = (double)0.0F;
      double x_cnam = (double)0.0F;
      int usedITS = 2018;
      x_cnss = this.menu.pc.CNSSm(sbmf, (double)1.0F, usedITS);
      x_cnam = this.menu.pc.CNAMm(sbmf);
      r = sbmf - this.menu.pc.ITSm(usedITS, sbmf, x_cnss, x_cnam, sbmf, (double)0.0F, (double)1.0F, false) - x_cnss - x_cnam;
      return r;
   }

   public double F22_NbEnfants(Employe var1) {
      return (double)employe.getNbEnfants();
   }

   public double calFonction(String var1, Employe var2, Motif var3, Date var4) {
      double r = (double)0.0F;
      if (codeFonc != null) {
         switch (codeFonc) {
            case "F01" -> r = this.F01_NJT(employe, motif, periode);
            case "F02" -> r = this.F02_sbJour(employe, this.menu.motifSN, periode);
            case "F03" -> r = this.F03_sbHoraire(employe, this.menu.motifSN, periode);
            case "F04" -> r = this.F04_TauxAnciennete(employe, periode);
            case "F05" -> r = this.F05_cumulBIDerDepart(employe);
            case "F06" -> r = this.F06_cumulBNIDerDepart(employe);
            case "F07" -> r = this.F07_cumulRETDerDepart(employe);
            case "F08" -> r = this.F08_cumulBrut12DerMois(employe);
            case "F09" -> r = this.F09_salaireBrutMensuelFixe(employe, periode);
            case "F10" -> r = this.F10_smig();
            case "F11" -> r = this.F11_smigHoraire(employe);
            case "F12" -> r = this.F12_TauxLicenciement(employe, periode);
            case "F13" -> r = this.F13_TauxLicenciementCollectif(employe, periode);
            case "F14" -> r = this.F14_TauxRetraite(employe, periode);
            case "F15" -> r = this.F15_TauxPSRA(employe);
            case "F16" -> r = (double)this.F16_TauxPreavis(employe);
            case "F17" -> r = this.F17_CumulNJTMC(employe);
            case "F18" -> r = (double)this.F18_NbSmigRegion(employe);
            case "F19" -> r = this.F19_TauxPresence(employe, periode);
            case "F20" -> r = this.F20_BaseIndLogement(employe);
            case "F21" -> r = this.F21_salaireNet(employe, periode);
            case "F22" -> r = this.F22_NbEnfants(employe);
            case "F23" -> r = this.F23_TauxAncienneteSpeciale(employe, periode);
            case "F24" -> r = this.F24_augmentationSalaireFixe(employe, periode);
         }
      }

      return r;
   }
}
