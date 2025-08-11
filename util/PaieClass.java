package com.mccmr.util;

import com.mccmr.entity.Activite;
import com.mccmr.entity.Banque;
import com.mccmr.entity.Conges;
import com.mccmr.entity.Departement;
import com.mccmr.entity.Diplome;
import com.mccmr.entity.Donneespointeuse;
import com.mccmr.entity.Employe;
import com.mccmr.entity.Enfants;
import com.mccmr.entity.Grillesalairebase;
import com.mccmr.entity.Jour;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Njtsalarie;
import com.mccmr.entity.Origines;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Paramgen;
import com.mccmr.entity.Poste;
import com.mccmr.entity.Retenuesaecheances;
import com.mccmr.entity.Rubrique;
import com.mccmr.entity.Rubriqueformule;
import com.mccmr.entity.Rubriquemodel;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.entity.Semainetravail;
import com.mccmr.entity.Statut;
import com.mccmr.entity.Sysrubrique;
import com.mccmr.entity.Tranchesretenuesaecheances;
import com.mccmr.entity.Weekot;
import com.mccmr.ui.menu;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.HibernateException;

public class PaieClass {
   public menu menu;
   public List<Rubriquepaie> dlRubriquepaie;
   public List<Njtsalarie> dlNjtsalarie;
   public List<Paie> dlPaie;
   public List<Employe> dlEmp;

   public PaieClass(menu var1) {
      this.menu = menu;
      this.refresh();
   }

   public void refresh() {
      Query q = this.menu.entityManager.createQuery("Select p from Employe p");
      q.setMaxResults(1000000);
      this.dlEmp = q.getResultList();
      q = this.menu.entityManager.createQuery("Select p from Rubriquepaie p");
      q.setMaxResults(1000000);
      this.dlRubriquepaie = q.getResultList();
      q = this.menu.entityManager.createQuery("Select p from Paie p");
      q.setMaxResults(1000000);
      this.dlPaie = q.getResultList();
   }

   public int countEmpByPoste(Poste var1) {
      int r = 0;

      try {
         List<Employe> dl = (List)this.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList());
         r = ((List)dl.stream().filter((var1x) -> var1x.getPoste().getId() == poste.getId()).collect(Collectors.toList())).size();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public int countEmpByDepCurrentPeriode(String var1) {
      int r = 0;

      try {
         r = ((List)this.dlPaie.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equals(this.menu.df.format(this.menu.paramsGen.getPeriodeCourante())) && var2x.getDepartement().equalsIgnoreCase(dep) && var2x.getMotif().getId() == 1).collect(Collectors.toList())).size();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double countEmpByGenre(String var1) {
      double r = (double)0.0F;

      try {
         List<Employe> dl = (List)this.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList());
         r = (double)((List)dl.stream().filter((var1x) -> var1x.getSexe().equalsIgnoreCase(genre)).collect(Collectors.toList())).size();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double countEmpByAnciennete(int var1, int var2) {
      double r = (double)0.0F;

      try {
         List<Employe> dl = (List)this.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList());
         r = (double)((List)dl.stream().filter((var3x) -> this.menu.gl.ageYears(var3x.getDateAnciennete()) >= (long)minAge && this.menu.gl.ageYears(var3x.getDateAnciennete()) <= (long)maxAge).collect(Collectors.toList())).size();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double countEmpByTrancheAge(int var1, int var2) {
      double r = (double)0.0F;

      try {
         List<Employe> dl = (List)this.dlEmp.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList());
         r = (double)((List)dl.stream().filter((var3x) -> this.menu.gl.ageYears(var3x.getDateNaissance()) >= (long)minAge && this.menu.gl.ageYears(var3x.getDateNaissance()) <= (long)maxAge).collect(Collectors.toList())).size();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double CNGByPeriodeByDep(Date var1, String var2) {
      double r = (double)0.0F;

      try {
         List<Paie> dl = (List)this.dlPaie.stream().filter((var3x) -> this.menu.df.format(var3x.getPeriode()).equals(this.menu.df.format(periode)) && var3x.getDepartement().equalsIgnoreCase(dep) && var3x.getMotif().getId() == 2).collect(Collectors.toList());
         r = dl.stream().mapToDouble((var0) -> var0.getBt()).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double SNByPeriodeByDep(Date var1, String var2) {
      double r = (double)0.0F;

      try {
         List<Paie> dl = (List)this.dlPaie.stream().filter((var3x) -> this.menu.df.format(var3x.getPeriode()).equals(this.menu.df.format(periode)) && var3x.getDepartement().equalsIgnoreCase(dep) && var3x.getMotif().getId() == 1).collect(Collectors.toList());
         r = dl.stream().mapToDouble((var0) -> var0.getBt()).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double BonusByPeriodeByDep(Date var1, String var2) {
      double r = (double)0.0F;

      try {
         List<Paie> dl = (List)this.dlPaie.stream().filter((var3x) -> this.menu.df.format(var3x.getPeriode()).equals(this.menu.df.format(periode)) && var3x.getDepartement().equalsIgnoreCase(dep) && var3x.getMotif().getId() != 1 && var3x.getMotif().getId() != 2).collect(Collectors.toList());
         r = dl.stream().mapToDouble((var0) -> var0.getBt()).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double HSByPeriodeBeyDep(Date var1, String var2) {
      double r = (double)0.0F;

      try {
         List<Rubriquepaie> dl = (List)this.dlRubriquepaie.stream().filter((var3x) -> this.menu.df.format(var3x.getPeriode()).equals(this.menu.df.format(periode)) && var3x.getEmploye().getDepartement().getNom().equalsIgnoreCase(dep) && (var3x.getRubrique().getId() == this.usedRubID(3).getId() || var3x.getRubrique().getId() == this.usedRubID(4).getId() || var3x.getRubrique().getId() == this.usedRubID(5).getId() || var3x.getRubrique().getId() == this.usedRubID(6).getId())).collect(Collectors.toList());
         r = dl.stream().mapToDouble((var0) -> var0.getMontant()).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double salBrutByDepartementCurrentPeriode(Departement var1) {
      double r = (double)0.0F;

      try {
         List<Paie> dl = (List)this.dlPaie.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equals(this.menu.df.format(this.menu.paramsGen.getPeriodeCourante())) && var2x.getDepartement().equalsIgnoreCase(dep.getNom())).collect(Collectors.toList());
         r = dl.stream().mapToDouble((var0) -> var0.getBt()).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double salNetByPeriode(Date var1) {
      double r = (double)0.0F;

      try {
         List<Paie> dl = (List)this.dlPaie.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equals(this.menu.df.format(periode))).collect(Collectors.toList());
         r = dl.stream().mapToDouble((var0) -> var0.getNet()).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double salBrutByPeriode(Date var1) {
      double r = (double)0.0F;

      try {
         List<Paie> dl = (List)this.dlPaie.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equals(this.menu.df.format(periode))).collect(Collectors.toList());
         r = dl.stream().mapToDouble((var0) -> var0.getBt()).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double itsByPeriode(Date var1) {
      double r = (double)0.0F;

      try {
         List<Paie> dl = (List)this.dlPaie.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equals(this.menu.df.format(periode))).collect(Collectors.toList());
         r = dl.stream().mapToDouble((var0) -> var0.getIts()).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double cnssByPeriode(Date var1) {
      double r = (double)0.0F;

      try {
         List<Paie> dl = (List)this.dlPaie.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equals(this.menu.df.format(periode))).collect(Collectors.toList());
         r = dl.stream().mapToDouble((var0) -> var0.getCnss() * (double)16.0F).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double cnamByPeriode(Date var1) {
      double r = (double)0.0F;

      try {
         List<Paie> dl = (List)this.dlPaie.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equals(this.menu.df.format(periode))).collect(Collectors.toList());
         r = dl.stream().mapToDouble((var0) -> var0.getCnam() / (double)4.0F * (double)9.0F).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public List<Weekot> empWeekots(Employe var1) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         q = this.menu.entityManager.createQuery("Select r from Weekot r where r.employe=" + employe.getId());
      } else {
         q = this.menu.entityManager.createQuery("Select r from Weekot r where r.employe=" + employe.getId());
      }

      return q.getResultList();
   }

   public List<Jour> empJours(Employe var1) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         q = this.menu.entityManager.createQuery("Select r from Jour r where r.employe=" + employe.getId());
      } else {
         q = this.menu.entityManager.createQuery("Select r from Jour r where r.employe=" + employe.getId());
      }

      return q.getResultList();
   }

   public List<Conges> empConges(Employe var1) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         q = this.menu.entityManager.createQuery("Select r from Conges r where r.employe=" + employe.getId());
      } else {
         q = this.menu.entityManager.createQuery("Select r from Conges r where r.employe=" + employe.getId());
      }

      return q.getResultList();
   }

   public List<Enfants> empEnfants(Employe var1) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         q = this.menu.entityManager.createQuery("Select r from Enfants r where r.employe=" + employe.getId());
      } else {
         q = this.menu.entityManager.createQuery("Select r from Enfants r where r.employe=" + employe.getId());
      }

      return q.getResultList();
   }

   public List<Diplome> empDiplomes(Employe var1) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         q = this.menu.entityManager.createQuery("Select r from Diplome r where r.employe=" + employe.getId());
      } else {
         q = this.menu.entityManager.createQuery("Select r from Diplome r where r.employe=" + employe.getId());
      }

      return q.getResultList();
   }

   public List<Retenuesaecheances> empRetAE(Employe var1) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         q = this.menu.entityManager.createQuery("Select r from Retenuesaecheances r where r.employe=" + employe.getId());
      } else {
         q = this.menu.entityManager.createQuery("Select r from Retenuesaecheances r where r.employe=" + employe.getId());
      }

      return q.getResultList();
   }

   public Set<Rubriquepaie> empRubriquepaie(Employe var1, Motif var2, Date var3) {
      String motifCond = motif == null ? "" : " and p.motif.id=" + motif.getId();
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         q = this.menu.entityManager.createQuery("Select p from Rubriquepaie p where p.employe.id=" + employe.getId() + motifCond + " and p.periode=TO_DATE('" + this.menu.df.format(periode) + "','YYYY-MM-DD') ");
      } else {
         q = this.menu.entityManager.createQuery("Select p from Rubriquepaie p where p.employe.id=" + employe.getId() + motifCond + " and p.periode='" + this.menu.df.format(periode) + "' ");
      }

      Set<Rubriquepaie> dl = new HashSet(q.getResultList());
      return dl;
   }

   public Set<Rubriquepaie> empRubriquepaieGainFixe(Employe var1, Motif var2, Date var3) {
      String motifCond = motif == null ? "" : " and p.motif=" + motif.getId();
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         q = this.menu.entityManager.createQuery("Select p from Rubriquepaie p where p.fixe=?1 and p.employe=" + employe.getId() + motifCond + " and p.periode=TO_DATE('" + this.menu.df.format(periode) + "','YYYY-MM-DD') ").setParameter(1, true);
      } else {
         q = this.menu.entityManager.createQuery("Select p from Rubriquepaie p where p.fixe=?1 and p.employe=" + employe.getId() + motifCond + " and p.periode='" + this.menu.df.format(periode) + "' ").setParameter(1, true);
      }

      Set<Rubriquepaie> dl = new HashSet(q.getResultList());
      return dl;
   }

   public Paie paieCalcule(Employe var1, Motif var2, Date var3, Date var4) {
      Date periode = this.menu.paramsGen.getPeriodeCourante();
      DateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
      Double tauxDevise = (double)1.0F;
      Double remExedentPlafon = (double)0.0F;
      Double mntITS = (double)0.0F;
      Double tranche1ITS = (double)0.0F;
      Double tranche2ITS = (double)0.0F;
      Double tranche3ITS = (double)0.0F;
      Double mntCNSS = (double)0.0F;
      Double mntCNAM = (double)0.0F;
      Double mntRITS = (double)0.0F;
      Double mntRCNSS = (double)0.0F;
      Double mntRCNAM = (double)0.0F;
      Double mntNET = (double)0.0F;
      Double mntSB = (double)0.0F;
      Double heuresMens = employe.getContratHeureSemaine() * (double)52.0F / (double)12.0F;
      if (this.menu.paramsGen.isAppIndCompensatrice() && this.usedRubID(19) != null && this.usedRubID(20) != null) {
         double mntAugSalFixe = this.augmentationSalaire(employe, periode);
         if (mntAugSalFixe > (double)0.0F) {
            Date periodePrecedente = this.menu.gl.addRetriveDays(periode, -30);
            Rubriquepaie rSys19 = this.rubriquePaieById(employe, this.usedRubID(19), motif, periode);

            try {
               periodePrecedente = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periodePrecedente));
               Rubriquepaie rSys19Prec = this.rubriquePaieById(employe, this.usedRubID(19), motif, periodePrecedente);
               if (rSys19 != null) {
                  if (rSys19Prec != null) {
                     double restR19Mnt = rSys19Prec.getBase() - mntAugSalFixe / rSys19Prec.getNombre();
                     if (restR19Mnt <= (double)0.0F) {
                        this.menu.pc.insertRubrique(this.menu.paramsGen.getPeriodeCourante(), employe, rSys19.getRubrique(), rSys19.getMotif(), (double)0.0F, (double)0.0F, true, false);
                        Rubriquepaie rSys20 = this.rubriquePaieById(employe, this.usedRubID(20), motif, periode);
                        Rubriquepaie rSys20Prec = this.rubriquePaieById(employe, this.usedRubID(20), motif, periodePrecedente);
                        if (rSys20 != null) {
                           double restR20Mnt = rSys20Prec.getBase() - mntAugSalFixe / rSys20Prec.getNombre();
                           if (restR20Mnt <= (double)0.0F) {
                              this.menu.pc.insertRubrique(this.menu.paramsGen.getPeriodeCourante(), employe, rSys20.getRubrique(), rSys20.getMotif(), (double)0.0F, (double)0.0F, true, false);
                           } else {
                              rSys20.setBase(restR20Mnt);
                              rSys20.setMontant(restR20Mnt * rSys20.getNombre());
                              this.menu.gl.updateOcurance(rSys20);
                           }
                        }
                     } else {
                        rSys19.setBase(restR19Mnt);
                        rSys19.setMontant(restR19Mnt * rSys19.getNombre());
                        this.menu.gl.updateOcurance(rSys19);
                     }
                  }
               } else {
                  Rubriquepaie rSys20 = this.rubriquePaieById(employe, this.usedRubID(20), motif, periode);
                  if (rSys20 != null) {
                     Rubriquepaie rSys20Prec = this.rubriquePaieById(employe, this.usedRubID(20), motif, periodePrecedente);
                     if (rSys20Prec != null) {
                        double restR20Mnt = rSys20Prec.getBase() - mntAugSalFixe / rSys20.getNombre();
                        if (restR20Mnt <= (double)0.0F) {
                           this.menu.pc.insertRubrique(this.menu.paramsGen.getPeriodeCourante(), employe, rSys20.getRubrique(), rSys20.getMotif(), (double)0.0F, (double)0.0F, true, false);
                        } else {
                           rSys20.setBase(restR20Mnt);
                           rSys20.setMontant(restR20Mnt * rSys20.getNombre());
                           this.menu.gl.updateOcurance(rSys20);
                        }
                     }
                  }
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }

      Set<Rubriquepaie> dlSN = this.menu.pc.empRubriquepaie(employe, this.menu.motifSN, this.menu.paramsGen.getPeriodeCourante());
      Set<Rubriquepaie> dl = this.menu.pc.empRubriquepaie(employe, motif, this.menu.paramsGen.getPeriodeCourante());
      Double SalaireBrut = dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("G")).mapToDouble((var0) -> var0.getMontant()).sum();
      Double RetenuesBruts = dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("R") && var0.getRubrique().getDeductionDu().equalsIgnoreCase("Brut")).mapToDouble((var0) -> var0.getMontant()).sum();
      Double RetenuesNet = dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("R") && var0.getRubrique().getDeductionDu().equalsIgnoreCase("Net")).mapToDouble((var0) -> var0.getMontant()).sum();
      Double RemunerationSoumiCnam = dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("G") && var0.getRubrique().isCnam()).mapToDouble((var0) -> var0.getMontant()).sum();
      Double RemunerationSoumiCnss = dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("G") && var0.getRubrique().isCnss()).mapToDouble((var0) -> var0.getMontant()).sum();
      Double AvantagesEnNature = dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("G") && var0.getRubrique().isAvantagesNature()).mapToDouble((var0) -> var0.getMontant()).sum();
      Double RemunerationImposable = dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("G") && var0.getRubrique().isIts()).mapToDouble((var0) -> var0.getMontant()).sum();
      Double remNonImpPlafone = dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("G") && !var0.getRubrique().isIts() && var0.getRubrique().isPlafone()).mapToDouble((var0) -> var0.getMontant()).sum();
      if (remNonImpPlafone > this.menu.paramsGen.getPlafonIndNonImposable()) {
         remExedentPlafon = remNonImpPlafone - this.menu.paramsGen.getPlafonIndNonImposable();
      }

      RemunerationImposable = RemunerationImposable + remExedentPlafon - RetenuesBruts;
      Double RemunerationNonImposable = SalaireBrut - RemunerationImposable;
      int usedITS = 2018;
      Rubriquepaie rp = this.rubriquePaieById(employe, this.usedRubID(1), motif, periode);
      if (rp != null) {
         mntSB = rp.getMontant();
      }

      if (!employe.isDetacheCnss() && motif.isEmployeSoumisCnss()) {
         RemunerationSoumiCnss = RemunerationSoumiCnss - RetenuesBruts;
         mntCNSS = this.CNSSm(RemunerationSoumiCnss, tauxDevise, usedITS);
         mntRCNSS = this.RCNSSm(RemunerationSoumiCnss, employe.getTauxRemborssementCnss(), tauxDevise, usedITS);
      }

      if (!employe.isDetacheCnam() && motif.isEmployeSoumisCnam()) {
         RemunerationSoumiCnam = RemunerationSoumiCnam - RetenuesBruts;
         mntCNAM = this.CNAMm(RemunerationSoumiCnam);
         mntRCNAM = this.RCNAMm(RemunerationSoumiCnam, employe.getTauxRemborssementCnam());
      }

      if (!employe.isExonoreIts() && motif.isEmployeSoumisIts()) {
         mntITS = this.ITSm(usedITS, RemunerationImposable, mntCNSS, mntCNAM, mntSB, AvantagesEnNature, tauxDevise, employe.isExpatrie());
         tranche1ITS = this.tranche1ITS(usedITS, RemunerationImposable, mntCNSS, mntCNAM, mntSB, AvantagesEnNature, tauxDevise, employe.isExpatrie());
         tranche2ITS = this.tranche2ITS(usedITS, RemunerationImposable, mntCNSS, mntCNAM, mntSB, AvantagesEnNature, tauxDevise, employe.isExpatrie());
         tranche3ITS = this.tranche3ITS(usedITS, RemunerationImposable, mntCNSS, mntCNAM, mntSB, AvantagesEnNature, tauxDevise, employe.isExpatrie());
         mntRITS = this.RITSm(usedITS, RemunerationImposable, mntCNSS, mntCNAM, mntSB, AvantagesEnNature, employe.getTauxRembItstranche1(), employe.getTauxRembItstranche2(), employe.getTauxRembItstranche3(), tauxDevise, employe.isExpatrie());
      }

      if ((motif.getId() == 1 || motif.getId() == 2) && SalaireBrut > (double)0.0F) {
         Set<Rubriquepaie> dlRetEch = (Set)dlSN.stream().filter((var2x) -> this.retAEActive(employe, var2x.getRubrique()) && var2x.getRubrique().getSens().equalsIgnoreCase("R") && var2x.isFixe()).collect(Collectors.toSet());
         List<Retenuesaecheances> dl2 = this.empRetAE(employe);
         dl2 = (List)dl2.stream().filter((var0) -> !var0.isSolde() && var0.isActive()).collect(Collectors.toList());
         Double totalEngagement = (double)0.0F;

         for(Retenuesaecheances rs : dl2) {
            if (motif.getId() == 1) {
               rs.setEcheancecourante(rs.getEcheance());
               totalEngagement = totalEngagement + rs.getEcheance();
            }

            if (motif.getId() == 2 && this.menu.paramsGen.isRetEngOnConge()) {
               rs.setEcheancecourantecng(rs.getEcheance());
               totalEngagement = totalEngagement + rs.getEcheance();
            }

            this.menu.gl.updateOcurance(rs);
         }

         for(Rubriquepaie rs : dlRetEch) {
            if (motif.getId() == 1) {
               this.insertRubrique(this.menu.paramsGen.getPeriodeCourante(), employe, rs.getRubrique(), motif, this.totalEchRetenueAE_SN(employe, rs.getRubrique()), (double)1.0F, true, false);
            }

            if (motif.getId() == 2 && this.menu.paramsGen.isRetEngOnConge()) {
               this.insertRubrique(this.menu.paramsGen.getPeriodeCourante(), employe, rs.getRubrique(), motif, this.totalEchRetenueAE_CNG(employe, rs.getRubrique()), (double)1.0F, true, false);
            }
         }

         Double netAvantEng = SalaireBrut - mntCNSS - mntITS + mntRITS + mntRCNSS;
         if (this.menu.paramsGen.getQuotaEcheanceRae() > (double)0.0F) {
            Double quotiteCessible = netAvantEng * this.menu.paramsGen.getQuotaEcheanceRae() / (double)100.0F;
            if (totalEngagement > quotiteCessible) {
               Double cumDifPourCent = (totalEngagement - quotiteCessible) / totalEngagement;

               for(Retenuesaecheances rs : (List)dl2.stream().filter((var0) -> !var0.isSolde() && var0.isActive()).collect(Collectors.toList())) {
                  if (motif.getId() == 1) {
                     rs.setEcheancecourante(rs.getEcheance() - rs.getEcheance() * cumDifPourCent);
                     if (rs.getEcheancecourante() < (double)0.0F) {
                        rs.setEcheancecourante((double)0.0F);
                     }
                  }

                  if (motif.getId() == 2) {
                     rs.setEcheancecourantecng(rs.getEcheance() - rs.getEcheance() * cumDifPourCent);
                     if (rs.getEcheancecourantecng() < (double)0.0F) {
                        rs.setEcheancecourantecng((double)0.0F);
                     }
                  }

                  this.menu.gl.updateOcurance(rs);
                  if (motif.getId() == 1) {
                     this.insertRubrique(this.menu.paramsGen.getPeriodeCourante(), employe, rs.getRubrique(), motif, this.totalEchRetenueAE_SN(employe, rs.getRubrique()), (double)1.0F, true, false);
                  }

                  if (motif.getId() == 2 && this.menu.paramsGen.isRetEngOnConge()) {
                     this.insertRubrique(this.menu.paramsGen.getPeriodeCourante(), employe, rs.getRubrique(), motif, this.totalEchRetenueAE_CNG(employe, rs.getRubrique()), (double)1.0F, true, false);
                  }
               }
            }
         }

         dl = this.menu.pc.empRubriquepaie(employe, motif, this.menu.paramsGen.getPeriodeCourante());
         RetenuesNet = dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("R") && var0.getRubrique().getDeductionDu().equalsIgnoreCase("Net")).mapToDouble((var0) -> var0.getMontant()).sum();
      }

      mntNET = SalaireBrut - mntCNSS - mntCNAM - mntITS - RetenuesBruts - RetenuesNet - AvantagesEnNature + mntRITS + mntRCNSS + mntRCNAM;
      Double heuresSuppTotal = this.decompterHS(employe, periode)[8];
      if (heuresSuppTotal == (double)0.0F && this.usedRubID(3) != null && this.usedRubID(4) != null && this.usedRubID(5) != null && this.usedRubID(6) != null) {
         rp = this.rubriquePaieById(employe, this.usedRubID(3), motif, periode);
         if (rp != null) {
            heuresSuppTotal = heuresSuppTotal + rp.getNombre();
         }

         rp = this.rubriquePaieById(employe, this.usedRubID(4), motif, periode);
         if (rp != null) {
            heuresSuppTotal = heuresSuppTotal + rp.getNombre();
         }

         rp = this.rubriquePaieById(employe, this.usedRubID(5), motif, periode);
         if (rp != null) {
            heuresSuppTotal = heuresSuppTotal + rp.getNombre();
         }

         rp = this.rubriquePaieById(employe, this.usedRubID(6), motif, periode);
         if (rp != null) {
            heuresSuppTotal = heuresSuppTotal + rp.getNombre();
         }
      }

      Paie o = new Paie();

      try {
         o.setBt(SalaireBrut);
         o.setBiAvnat(AvantagesEnNature);
         o.setBiCnam(RemunerationSoumiCnam);
         o.setBiCnss(RemunerationSoumiCnss);
         o.setBni(RemunerationNonImposable);
         o.setCnam(mntCNAM);
         o.setCnss(mntCNSS);
         o.setRcnam(mntRCNAM);
         o.setRcnss(mntRCNSS);
         o.setRetenuesBrut(RetenuesBruts);
         o.setRits(mntRITS);
         o.setNbrHs(heuresSuppTotal);
         o.setNjt(this.menu.fx.F01_NJT(employe, motif, periode));
         o.setContratHeureMois(heuresMens);
         o.setIts(mntITS);
         o.setItsTranche1(tranche1ITS);
         o.setItsTranche2(tranche2ITS);
         o.setItsTranche3(tranche3ITS);
         o.setCumulBi(this.cumulTypeById(employe, "BI"));
         o.setCumulBni(this.cumulTypeById(employe, "BNI"));
         o.setCumulNjt((double)0.0F);
         o.setFte(this.FTE_EmpByPeriode(employe, periode));
         o.setDateDernierDepart(this.dernierDepart(employe));
         o.setDomicilie(employe.isDomicilie());
         o.setEmploye(employe);
         o.setModePaiement(employe.getModePaiement());
         o.setMotif(motif);
         String var10001;
         if (mntNET > (double)0.0F) {
            var10001 = this.menu.nl.convertirEnMRO(mntNET, "FR");
         } else {
            NombreEnLettres var71 = this.menu.nl;
            var10001 = "MOINS " + var71.convertirEnMRO(Math.abs(mntNET), "FR");
         }

         o.setNetEnLettre(var10001);
         o.setNoCompteBanque(employe.getNoCompteBanque());
         o.setNotePaie(employe.getNoteSurBulletin());
         o.setPaieAu(paieAu);
         o.setPaieDu(paieDu);
         o.setPeriode(periode);
         o.setPeriodeLettre(dateFormat.format(periode).toUpperCase());
         o.setParamgen(this.menu.paramsGen);
         o.setDirectiongeneral(employe.getDirectiongeneral() != null ? employe.getDirectiongeneral().getNom() : "-");
         o.setDirection(employe.getDirection() != null ? employe.getDirection().getNom() : "-");
         o.setDepartement(employe.getDepartement() != null ? employe.getDepartement().getNom() : "-");
         o.setPoste(employe.getPoste() != null ? employe.getPoste().getNom() : "-");
         o.setBanque(employe.getBanque() != null ? employe.getBanque().getNom() : "-");
         o.setCategorie(employe.getGrillesalairebase() != null ? employe.getGrillesalairebase().getCategorie() : "-");
         o.setClassification("-");
         o.setStatut(employe.getStatut());
         o.setActivite(employe.getActivite() != null ? employe.getActivite().getNom() : "-");
         o.setRetenuesNet(RetenuesNet);
         o.setNet(mntNET);
      } catch (Exception ex) {
         ex.printStackTrace();
      }

      return o;
   }

   public Tranchesretenuesaecheances trancheRAEById(Retenuesaecheances var1, Date var2) {
      Set<Tranchesretenuesaecheances> dl = new HashSet(retenuesaecheances.getTranchesretenuesaecheanceses());
      Set var4 = (Set)dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).collect(Collectors.toSet());
      return var4.isEmpty() ? null : (Tranchesretenuesaecheances)var4.toArray()[0];
   }

   public Retenuesaecheances retAEByID(Employe var1, Rubrique var2, Date var3) {
      List<Retenuesaecheances> dl = this.empRetAE(employe);
      dl = (List)dl.stream().filter((var3x) -> var3x.getRubrique().getId() == rubrique.getId() && this.menu.df.format(var3x.getDateAccord()).equalsIgnoreCase(this.menu.df.format(dateAccord))).collect(Collectors.toList());
      return dl.isEmpty() ? null : (Retenuesaecheances)dl.toArray()[0];
   }

   public Retenuesaecheances retAEByID(Employe var1, long var2) {
      List<Retenuesaecheances> dl = this.empRetAE(employe);
      dl = (List)dl.stream().filter((var2x) -> var2x.getId() == retenueae_id).collect(Collectors.toList());
      return dl.isEmpty() ? null : (Retenuesaecheances)dl.toArray()[0];
   }

   public Semainetravail infosJourById(Date var1, Employe var2) {
      Semainetravail rs = new Semainetravail();
      String dayName = (new SimpleDateFormat("EEEE", Locale.FRANCE)).format(dayDate);
      rs.setJour(dayName);
      switch (dayName) {
         case "lundi":
            rs.setDebut(employe.isLundiDs());
            rs.setFin(employe.isLundiFs());
            rs.setWeekEnd(employe.isLundiWe());
            break;
         case "mardi":
            rs.setDebut(employe.isMardiDs());
            rs.setFin(employe.isMardiFs());
            rs.setWeekEnd(employe.isMardiWe());
            break;
         case "mercredi":
            rs.setDebut(employe.isMercrediDs());
            rs.setFin(employe.isMercrediFs());
            rs.setWeekEnd(employe.isMercrediWe());
            break;
         case "jeudi":
            rs.setDebut(employe.isJeudiDs());
            rs.setFin(employe.isJeudiFs());
            rs.setWeekEnd(employe.isJeudiWe());
            break;
         case "vendredi":
            rs.setDebut(employe.isVendrediDs());
            rs.setFin(employe.isVendrediFs());
            rs.setWeekEnd(employe.isVendrediWe());
            break;
         case "samedi":
            rs.setDebut(employe.isSamediDs());
            rs.setFin(employe.isSamediFs());
            rs.setWeekEnd(employe.isSamediWe());
            break;
         case "dimanche":
            rs.setDebut(employe.isDimancheDs());
            rs.setFin(employe.isDimancheFs());
            rs.setWeekEnd(employe.isDimancheWe());
      }

      return rs;
   }

   public Employe employeByIdPsservice(String var1) {
      return (Employe)this.menu.employeFrame.dataListInit.stream().filter((var1x) -> var1x.getIdPsservice().equalsIgnoreCase(id)).findAny().orElse((Object)null);
   }

   public Employe employeById(int var1) {
      Employe rs = null;
      rs = (Employe)this.menu.entityManager.find(Employe.class, id);
      return rs;
   }

   public Donneespointeuse pointageByIDP(int var1) {
      Donneespointeuse rs = null;
      rs = (Donneespointeuse)this.menu.entityManager.find(Donneespointeuse.class, id);
      return rs;
   }

   public Diplome diplomeByEmp(long var1) {
      Diplome rs = null;
      rs = (Diplome)this.menu.entityManager.find(Diplome.class, id);
      return rs;
   }

   public Enfants enfantByEmp(Long var1) {
      Enfants rs = null;
      rs = (Enfants)this.menu.entityManager.find(Enfants.class, id);
      return rs;
   }

   public int enfantNbByEmp(Employe var1) {
      return employe.getEnfantss().size();
   }

   public Employe employeByIDP(Long var1) {
      Query q = this.menu.entityManager.createQuery("Select p from Employe p where p.idSalariePointeuse=?1").setParameter(1, ((Number)id).intValue());
      q.setMaxResults(1000000);
      return q.getResultList().isEmpty() ? null : (Employe)q.getResultList().get(0);
   }

   public Motif motifById(int var1) {
      Motif rs = null;
      rs = (Motif)this.menu.entityManager.find(Motif.class, id);
      return rs;
   }

   public Motif motifByNom(String var1) {
      return (Motif)this.menu.stricturesIF.dataListInit_Motifs.stream().filter((var1x) -> var1x.getNom().equalsIgnoreCase(nom)).findAny().orElse((Object)null);
   }

   public Banque banqueById(int var1) {
      Banque rs = null;
      rs = (Banque)this.menu.entityManager.find(Banque.class, id);
      return rs;
   }

   public Banque banqueByNom(String var1) {
      return (Banque)this.menu.stricturesIF.dataListInit_Banques.stream().filter((var1x) -> var1x.getNom().equalsIgnoreCase(nom)).findAny().orElse((Object)null);
   }

   public Departement departementByID(long var1) {
      Departement rs = null;
      rs = (Departement)this.menu.entityManager.find(Departement.class, id);
      return rs;
   }

   public Departement departementByNom(String var1) {
      return (Departement)this.menu.stricturesIF.dataListInit_Departements.stream().filter((var1x) -> var1x.getNom().equalsIgnoreCase(nom)).findAny().orElse((Object)null);
   }

   public Statut statutById(Long var1) {
      Statut rs = null;
      rs = (Statut)this.menu.entityManager.find(Statut.class, id);
      return rs;
   }

   public Statut statutByNom(String var1) {
      return (Statut)this.menu.stricturesIF.dataListInit_Statuts.stream().filter((var1x) -> var1x.getNom().equalsIgnoreCase(nom)).findAny().orElse((Object)null);
   }

   public Poste posteByID(Long var1) {
      Poste rs = null;
      rs = (Poste)this.menu.entityManager.find(Poste.class, id);
      return rs;
   }

   public Poste posteByNom(String var1) {
      return (Poste)this.menu.stricturesIF.dataListInit_Postes.stream().filter((var1x) -> var1x.getNom().equalsIgnoreCase(nom)).findAny().orElse((Object)null);
   }

   public Grillesalairebase grillesalbaseByNom(String var1) {
      return (Grillesalairebase)this.menu.stricturesIF.dataListInit_Grillesalairebases.stream().filter((var1x) -> var1x.getCategorie().equalsIgnoreCase(categorie)).findAny().orElse((Object)null);
   }

   public Origines origineById(long var1) {
      Origines rs = null;
      rs = (Origines)this.menu.entityManager.find(Origines.class, id);
      return rs;
   }

   public Activite activiteByID(Long var1) {
      Activite rs = null;
      rs = (Activite)this.menu.entityManager.find(Activite.class, id);
      return rs;
   }

   public Rubrique usedRubID(int var1) {
      Query q = this.menu.entityManager.createQuery("Select p from Sysrubrique p where p.idSys=" + idSysRubrique);
      return q.getSingleResult() == null ? null : this.rubriqueById(((Sysrubrique)q.getSingleResult()).getIdCustum());
   }

   public Jour jourById(Employe var1, Date var2) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         EntityManager var10000 = this.menu.entityManager;
         int var10001 = employe.getId();
         q = var10000.createQuery("Select p from Jour p where p.employe=" + var10001 + " and p.dateJour=TO_DATE('" + this.menu.df.format(dateJour) + "','YYYY-MM-DD')");
      } else {
         EntityManager var4 = this.menu.entityManager;
         int var5 = employe.getId();
         q = var4.createQuery("Select p from Jour p where p.employe=" + var5 + " and p.dateJour='" + this.menu.df.format(dateJour) + "'");
      }

      return !q.getResultList().isEmpty() ? (Jour)q.getSingleResult() : null;
   }

   public Weekot weekById(Employe var1, Date var2) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         EntityManager var10000 = this.menu.entityManager;
         int var10001 = employe.getId();
         q = var10000.createQuery("Select p from Weekot p where p.employe=" + var10001 + " and p.beginweek=TO_DATE('" + this.menu.df.format(weekBeginDate) + "','YYYY-MM-DD')");
      } else {
         EntityManager var4 = this.menu.entityManager;
         int var5 = employe.getId();
         q = var4.createQuery("Select p from Weekot p where p.employe=" + var5 + " and p.beginweek='" + this.menu.df.format(weekBeginDate) + "'");
      }

      return !q.getResultList().isEmpty() ? (Weekot)q.getSingleResult() : null;
   }

   public Conges congesById(Employe var1, Date var2) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         EntityManager var10000 = this.menu.entityManager;
         int var10001 = employe.getId();
         q = var10000.createQuery("Select p from Conges p where p.employe=" + var10001 + " and p.depart=TO_DATE('" + this.menu.df.format(dateDepart) + "','YYYY-MM-DD')");
      } else {
         EntityManager var4 = this.menu.entityManager;
         int var5 = employe.getId();
         q = var4.createQuery("Select p from Conges p where p.employe=" + var5 + " and p.depart='" + this.menu.df.format(dateDepart) + "'");
      }

      return !q.getResultList().isEmpty() ? (Conges)q.getSingleResult() : null;
   }

   public Grillesalairebase categorieSuivante(String var1, int var2) {
      Query q = this.menu.entityManager.createQuery("Select p from Grillesalairebase p where p.nomCategorie='" + nomCategorie + "' and p.niveau=" + (niveauCourant + 1));
      return !q.getResultList().isEmpty() ? (Grillesalairebase)q.getSingleResult() : null;
   }

   public Rubriquemodel rubModelByID(long var1) {
      Rubriquemodel rs = null;
      rs = (Rubriquemodel)this.menu.entityManager.find(Rubriquemodel.class, id);
      return rs;
   }

   public Paie paieById(Employe var1, Motif var2, Date var3) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         EntityManager var10000 = this.menu.entityManager;
         int var10001 = employe.getId();
         q = var10000.createQuery("Select p from Paie p where p.employe=" + var10001 + " and p.motif=" + motif.getId() + " and p.periode=TO_DATE('" + this.menu.df.format(periode) + "','YYYY-MM-DD')");
      } else {
         EntityManager var5 = this.menu.entityManager;
         int var6 = employe.getId();
         q = var5.createQuery("Select p from Paie p where p.employe=" + var6 + " and p.motif=" + motif.getId() + " and p.periode='" + this.menu.df.format(periode) + "'");
      }

      return !q.getResultList().isEmpty() ? (Paie)q.getSingleResult() : null;
   }

   public Rubrique rubriqueById(int var1) {
      Rubrique rs = null;
      rs = (Rubrique)this.menu.entityManager.find(Rubrique.class, id);
      return rs;
   }

   public Rubriquepaie rubriquePaieByIdSimple(Employe var1, int var2, int var3, Date var4) {
      return (Rubriquepaie)employe.getRubriquepaies().stream().filter((var4x) -> var4x.getRubrique().getId() == rubriqueID && var4x.getMotif().getId() == motifID && this.menu.df.format(var4x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).findAny().orElse((Object)null);
   }

   public Rubriquepaie rubriquePaieById(Employe var1, Rubrique var2, Motif var3, Date var4) {
      Query q;
      if (this.menu.dialect.toString().contains("Oracle")) {
         EntityManager var10000 = this.menu.entityManager;
         int var10001 = employe.getId();
         q = var10000.createQuery("Select p from Rubriquepaie p where p.employe=" + var10001 + " and p.rubrique=" + rubrique.getId() + " and p.motif=" + motif.getId() + " and p.periode=TO_DATE('" + this.menu.df.format(periode) + "','YYYY-MM-DD')");
      } else {
         EntityManager var6 = this.menu.entityManager;
         int var7 = employe.getId();
         q = var6.createQuery("Select p from Rubriquepaie p where p.employe=" + var7 + " and p.rubrique=" + rubrique.getId() + " and p.motif=" + motif.getId() + " and p.periode='" + this.menu.df.format(periode) + "'");
      }

      return !q.getResultList().isEmpty() ? (Rubriquepaie)q.getSingleResult() : null;
   }

   public Njtsalarie njtSalarieById(Employe var1, Motif var2, Date var3) {
      Njtsalarie r = null;
      if (employe != null) {
         Query q;
         if (this.menu.dialect.toString().contains("Oracle")) {
            EntityManager var10000 = this.menu.entityManager;
            int var10001 = employe.getId();
            q = var10000.createQuery("Select p from Njtsalarie p where p.employe=" + var10001 + " and p.motif=" + motif.getId() + " and p.periode=TO_DATE('" + this.menu.df.format(periode) + "','YYYY-MM-DD')");
         } else {
            EntityManager var6 = this.menu.entityManager;
            int var7 = employe.getId();
            q = var6.createQuery("Select p from Njtsalarie p where p.employe=" + var7 + " and p.motif=" + motif.getId() + " and p.periode='" + this.menu.df.format(periode) + "'");
         }

         r = !q.getResultList().isEmpty() ? (Njtsalarie)q.getSingleResult() : null;
      }

      return r;
   }

   public double augmentationRubPaie(Rubriquepaie var1, Date var2) {
      double res = (double)0.0F;
      if (this.menu.paramsGen.isAppIndCompensatrice() && this.usedRubID(19) != null && this.usedRubID(20) != null && rubPaie.getRubrique().getSens().equalsIgnoreCase("G") && rubPaie.isFixe() && rubPaie.getRubrique().isNombreAuto() && rubPaie.getRubrique().getId() != this.usedRubID(19).getId() && rubPaie.getRubrique().getId() != this.usedRubID(20).getId()) {
         double njtCourant = this.menu.fx.F01_NJT(rubPaie.getEmploye(), rubPaie.getMotif(), periode);
         double mntRubCourant = rubPaie.getMontant();
         Date periodePrecedente = this.menu.gl.addRetriveDays(periode, -30);

         try {
            periodePrecedente = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periodePrecedente));
            double njtPrecedent = this.menu.fx.F01_NJT(rubPaie.getEmploye(), rubPaie.getMotif(), periodePrecedente);
            double mntRubPrecendent = (double)0.0F;
            Rubriquepaie rpPrecedente = this.rubriquePaieById(rubPaie.getEmploye(), rubPaie.getRubrique(), rubPaie.getMotif(), periodePrecedente);
            if (rubPaie.getRubrique().getSens().equalsIgnoreCase("G") && rubPaie.isFixe()) {
               if (rpPrecedente != null) {
                  if (njtPrecedent == njtCourant) {
                     mntRubPrecendent = rpPrecedente.getMontant();
                  } else {
                     double nombre = rpPrecedente.getNombre();
                     double base = rpPrecedente.getBase();
                     if (rubPaie.getRubrique().getId() == this.usedRubID(2).getId()) {
                        if (rpPrecedente.getRubrique().isBaseAuto()) {
                           base = this.baseRbrique(periode, rubPaie.getRubrique(), rubPaie.getEmploye(), rubPaie.getMotif());
                        }
                     } else {
                        if (rpPrecedente.getRubrique().isNombreAuto()) {
                           nombre = this.nombreRbrique(periode, rubPaie.getRubrique(), rubPaie.getEmploye(), rubPaie.getMotif());
                        }

                        if (rpPrecedente.getRubrique().isBaseAuto()) {
                           base = this.baseRbrique(periode, rubPaie.getRubrique(), rubPaie.getEmploye(), rubPaie.getMotif());
                        }
                     }

                     mntRubPrecendent = (double)Math.round(base * nombre);
                  }
               } else {
                  mntRubPrecendent = (double)0.0F;
               }

               if (mntRubCourant > mntRubPrecendent) {
                  res = mntRubCourant - mntRubPrecendent;
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      return res > (double)0.0F ? res : (double)0.0F;
   }

   public double augmentationSalaire(Employe var1, Date var2) {
      double res = (double)0.0F;

      for(Rubriquepaie rs : new ArrayList(this.menu.pc.empRubriquepaieGainFixe(employe, this.menu.motifSN, this.menu.paramsGen.getPeriodeCourante()))) {
         if (this.usedRubID(19) != null && this.usedRubID(20) != null) {
            if (rs.getRubrique().getId() != this.usedRubID(19).getId() && rs.getRubrique().getId() != this.usedRubID(20).getId()) {
               res += this.augmentationRubPaie(rs, periode);
            }
         } else {
            res += this.augmentationRubPaie(rs, periode);
         }
      }

      return res > (double)0.0F ? res : (double)0.0F;
   }

   public void updateRubriquePaie(Date var1, Employe var2, Motif var3) {
      if (motif.getId() == 1) {
         for(Rubriquepaie rs : new ArrayList(this.menu.pc.empRubriquepaie(employe, motif, periode))) {
            if (rs.getRubrique().isNombreAuto()) {
               double nombre = this.nombreRbrique(periode, rs.getRubrique(), employe, motif);
               rs.setNombre(nombre);
               double mnt = nombre * rs.getBase();
               rs.setMontant((double)Math.round(mnt));
            }

            if (rs.getRubrique().isBaseAuto()) {
               double base = this.baseRbrique(periode, rs.getRubrique(), employe, motif);
               rs.setBase(base);
               double mnt = base * rs.getNombre();
               rs.setMontant((double)Math.round(mnt));
            }

            this.menu.gl.updateOcurance(rs);
         }
      }

   }

   public void updateSal(Employe var1) {
      Rubrique surSalRub = this.rubriqueById(10);
      if (surSalRub != null && employe.getTauxRemborssementCnss() > (double)0.0F && employe.getTauxRemborssementCnam() > (double)0.0F && employe.getTauxRembItstranche1() > (double)0.0F && employe.getTauxRembItstranche2() > (double)0.0F && employe.getTauxRembItstranche3() > (double)0.0F) {
         double njtCourrant = this.menu.fx.F01_NJT(employe, this.menu.motifSN, this.menu.paramsGen.getPeriodeCourante());
         this.insertNJT(this.menu.paramsGen.getPeriodeCourante(), employe, this.menu.motifSN, (double)30.0F);
         Paie paie = this.menu.pc.paieCalcule(employe, this.menu.motifSN, this.menu.paramsGen.getPeriodeCourante(), this.menu.paramsGen.getPeriodeCourante());
         double netActuelAvecRembourssement = paie.getNet();
         double newNet = (double)0.0F;
         Rubriquepaie rp = this.rubriquePaieById(employe, surSalRub, this.menu.motifSN, this.menu.paramsGen.getPeriodeCourante());
         double surSal = rp != null ? rp.getMontant() : (double)0.0F;
         employe.setTauxRembItstranche1((double)0.0F);
         employe.setTauxRembItstranche2((double)0.0F);
         employe.setTauxRembItstranche3((double)0.0F);
         employe.setTauxRemborssementCnss((double)0.0F);
         employe.setTauxRemborssementCnam((double)0.0F);
         this.menu.gl.updateOcurance(employe);

         do {
            surSal += (netActuelAvecRembourssement - newNet) / (double)10.0F;
            this.insertRubrique(this.menu.paramsGen.getPeriodeCourante(), employe, surSalRub, this.menu.motifSN, surSal / (double)30.0F, (double)30.0F, true, false);
            paie = this.menu.pc.paieCalcule(employe, this.menu.motifSN, this.menu.paramsGen.getPeriodeCourante(), this.menu.paramsGen.getPeriodeCourante());
            newNet = paie.getNet();
         } while(newNet < netActuelAvecRembourssement);

         this.insertNJT(this.menu.paramsGen.getPeriodeCourante(), employe, this.menu.motifSN, njtCourrant);
      }

   }

   public void copyingRubriquePaieFixe(Employe var1, Date var2, Date var3) {
      List<Rubriquepaie> dl = new ArrayList(this.menu.pc.empRubriquepaie(employe, this.menu.motifSN, periodeFrom));

      for(Rubriquepaie rs : (List)dl.stream().filter((var0) -> var0.isFixe()).sorted(Comparator.comparing((var0) -> var0.getRubrique().getId())).collect(Collectors.toList())) {
         this.insertRubrique(periodeTo, employe, rs.getRubrique(), rs.getMotif(), rs.getBase(), rs.getNombre(), rs.isFixe(), false);
      }

   }

   public boolean insertRubrique(Date var1, Employe var2, Rubrique var3, Motif var4, Double var5, Double var6, Boolean var7, Boolean var8) {
      boolean res = false;
      boolean add = true;
      if (rubrique != null) {
         try {
            long oldID = 1L;
            Rubriquepaie o = this.rubriquePaieById(employe, rubrique, motif, periode);
            if (base != null && base == (double)0.0F) {
               if (o != null) {
                  res = this.menu.gl.deleteOcurance(o);
               }
            } else {
               if (rubrique.isBaseAuto()) {
                  base = this.baseRbrique(periode, rubrique, employe, motif);
               }

               if (rubrique.isNombreAuto()) {
                  nombre = this.nombreRbrique(periode, rubrique, employe, motif);
               }

               double montant = base * nombre;
               montant = (double)Math.round(montant);
               if (o == null) {
                  o = new Rubriquepaie();
               } else {
                  add = false;
                  oldID = o.getId();
               }

               if (montant > (double)0.0F) {
                  o.setImporte(importe);
                  o.setBase(base);
                  o.setEmploye(employe);
                  o.setFixe(fixe);
                  o.setMontant(montant > (double)0.0F ? montant : (double)0.0F);
                  o.setMotif(motif);
                  o.setNombre(nombre);
                  o.setPeriode(periode);
                  o.setRubrique(rubrique);
                  if (add) {
                     res = this.menu.gl.insertOcurance(o);
                     if (!res) {
                        res = this.menu.gl.updateOcurance(o);
                        if (res) {
                        }
                     }
                  } else {
                     o.setId(oldID);
                     res = this.menu.gl.updateOcurance(o);
                     if (res) {
                     }
                  }
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      return res;
   }

   public boolean insertNJT(Date var1, Employe var2, Motif var3, Double var4) {
      boolean res = false;
      boolean add = true;
      if (njt != null) {
         try {
            Njtsalarie o = this.njtSalarieById(employe, motif, periode);
            if (o == null) {
               o = new Njtsalarie();
            } else {
               add = false;
            }

            o.setEmploye(employe);
            o.setMotif(motif);
            o.setNjt(njt);
            o.setPeriode(periode);
            if (add) {
               res = this.menu.gl.insertOcurance(o);
            } else {
               res = this.menu.gl.updateOcurance(o);
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      return res;
   }

   public boolean insertPaie(Employe var1, Motif var2, Date var3, Date var4) {
      boolean r = false;
      boolean add = true;
      long oldID = 0L;
      Paie o = this.paieById(employe, motif, this.menu.paramsGen.getPeriodeCourante());
      if (o != null) {
         oldID = o.getId();
         add = false;
      }

      o = this.paieCalcule(employe, motif, paieDu, paieAu);
      if (o.getEmploye().isActif() && !o.getEmploye().isEnConge() && o.getBt() > (double)0.0F) {
         if (add) {
            r = this.menu.gl.insertOcurance(o);
         } else {
            o.setId(oldID);
            r = this.menu.gl.updateOcurance(o);
         }
      }

      return r;
   }

   public boolean insertConges(Employe var1, Date var2, Date var3, Date var4, String var5, Conges var6) {
      boolean res = false;
      boolean add = true;
      Conges o = conges == null ? this.congesById(employe, dateDepart) : conges;
      if (o == null) {
         o = new Conges();
      } else {
         add = false;
      }

      o.setEmploye(employe);
      o.setDepart(dateDepart);
      o.setNote(note);
      o.setReprise(dateReprise);
      o.setRepriseeff(dateRepriseEff);
      o.setPeriode(this.menu.paramsGen.getPeriodeCourante());
      if (add) {
         res = this.menu.gl.insertOcurance(o);
      } else {
         res = this.menu.gl.updateOcurance(o);
      }

      return res;
   }

   public boolean insertRetAE(Employe var1, Rubrique var2, Date var3, double var4, double var6, boolean var8, String var9) {
      Retenuesaecheances o = this.retAEByID(employe, rubrique, dateAccord);
      boolean isNew = true;
      if (o == null) {
         o = new Retenuesaecheances();
      } else if (o.getTranchesretenuesaecheanceses().isEmpty()) {
         isNew = false;
         long id = o.getId();
         employe.getRetenuesaecheanceses().removeIf((var2x) -> var2x.getId() == id);
      }

      o.setActive(active);
      o.setCapital(capital);
      o.setDateAccord(dateAccord);
      o.setEcheance(echeance);
      o.setEcheancecourante(echeance);
      o.setEcheancecourantecng((double)0.0F);
      o.setEmploye(employe);
      o.setRubrique(rubrique);
      o.setSolde(false);
      o.setNote(note);
      o.setPeriode(this.menu.paramsGen.getPeriodeCourante());
      boolean r;
      if (isNew) {
         r = this.menu.gl.insertOcurance(o);
      } else {
         r = this.menu.gl.updateOcurance(o);
      }

      if (r) {
         employe.getRetenuesaecheanceses().add(o);
      }

      return r;
   }

   public boolean insertJourHS(Employe var1, Date var2, double var3, double var5, double var7, double var9, boolean var11, boolean var12, boolean var13, String var14) {
      Jour o = this.jourById(employe, dateJour);
      if (o == null) {
         o = new Jour();
      }

      o.setDateJour(dateJour);
      o.setEmploye(employe);
      o.setFerie100(ferie100);
      o.setFerie50(ferie50);
      o.setNbHeureJour(heuresJour);
      o.setNbHeureNuit(heuresNuit);
      o.setNbPrimePanier(primesPanier);
      o.setNbPrimeEloignement(primesEloignement);
      o.setNote(note);
      o.setPeriode(this.menu.paramsGen.getPeriodeCourante());
      o.setSiteExterne(siteExterne);
      boolean r = this.menu.gl.insertOcurance(o);
      if (r) {
         employe.getJours().add(o);
      }

      return r;
   }

   public boolean insertWeekHS(Employe var1, Date var2, Date var3, double var4, double var6, double var8, double var10, double var12, double var14, String var16) {
      Weekot o = this.weekById(employe, beginWeek);
      if (o == null) {
         o = new Weekot();
      }

      o.setBeginweek(beginWeek);
      o.setEmploye(employe);
      o.setEndweek(endWeek);
      o.setOt115(weekOT115);
      o.setOt140(weekOT140);
      o.setOt150(weekOT150);
      o.setOt200(weekOT200);
      o.setNbPrimePanier(primesPanier);
      o.setNbPrimeEloignement(primesEloignement);
      o.setNote(note);
      o.setPeriode(this.menu.paramsGen.getPeriodeCourante());
      boolean r = this.menu.gl.updateOcurance(o);
      if (r) {
         employe.getWeekots().remove(o);
         employe.getWeekots().add(o);
      }

      return r;
   }

   public int nbJrsCongeMerite(Employe var1) {
      int r = 0;
      double nbMois = (double)0.0F;
      Date dd = this.dernierDepart(employe);
      nbMois = (double)this.menu.gl.differenceDateMois(dd, this.menu.paramsGen.getPeriodeSuivante());
      r = (int)Math.floor(nbMois) * 2;
      return r;
   }

   public double salBrutCourant(Employe var1, Motif var2) {
      List<Rubriquepaie> dl = new ArrayList(this.menu.pc.empRubriquepaie(employe, motif, this.menu.paramsGen.getPeriodeCourante()));
      return dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("G")).mapToDouble((var0) -> var0.getMontant()).sum();
   }

   public double salBrutPrecedent(Employe var1, Motif var2) {
      double res = (double)0.0F;
      Date periodePrecedente = this.menu.gl.addRetriveDays(this.menu.paramsGen.getPeriodeCourante(), -30);

      try {
         periodePrecedente = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periodePrecedente));
         List<Rubriquepaie> dl = new ArrayList(this.menu.pc.empRubriquepaie(employe, motif, periodePrecedente));
         res = dl.stream().filter((var0) -> var0.getRubrique().getSens().equalsIgnoreCase("G")).mapToDouble((var0) -> var0.getMontant()).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return res;
   }

   public double totalSalaireNet(Employe var1) {
      return employe.getPaies().stream().filter((var1x) -> this.menu.df.format(var1x.getPeriode()).equalsIgnoreCase(this.menu.df.format(this.menu.paramsGen.getPeriodeCourante()))).mapToDouble((var0) -> var0.getNet()).sum();
   }

   public double totalReglementRetAE(Retenuesaecheances var1) {
      List<Tranchesretenuesaecheances> dl = new ArrayList(retenuesaecheances.getTranchesretenuesaecheanceses());
      return dl.stream().mapToDouble((var0) -> var0.getMontantRegle()).sum();
   }

   public boolean retAEActive(Employe var1, Rubrique var2) {
      boolean res = false;

      try {
         List<Retenuesaecheances> dl = new ArrayList(employe.getRetenuesaecheanceses());
         List var6 = (List)dl.stream().filter((var1x) -> var1x.getRubrique().getId() == rubrique.getId() && !var1x.isSolde() && var1x.isActive()).collect(Collectors.toList());
         res = !var6.isEmpty();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return res;
   }

   public double totalReglementRetenuesAE_Salarie(Employe var1) {
      List<Retenuesaecheances> dl = new ArrayList(employe.getRetenuesaecheanceses());
      return dl.stream().mapToDouble((var1x) -> this.totalReglementRetAE(var1x)).sum();
   }

   public double totalEchRetenueAE_SN(Employe var1, Rubrique var2) {
      List<Retenuesaecheances> dl = new ArrayList(employe.getRetenuesaecheanceses());
      return dl.stream().filter((var1x) -> !var1x.isSolde() && var1x.isActive() && var1x.getRubrique().getId() == rubrique.getId()).mapToDouble((var0) -> var0.getEcheancecourante()).sum();
   }

   public double totalEchRetenueAE_CNG(Employe var1, Rubrique var2) {
      List<Retenuesaecheances> dl = new ArrayList(employe.getRetenuesaecheanceses());
      return dl.stream().filter((var1x) -> !var1x.isSolde() && var1x.isActive() && var1x.getRubrique().getId() == rubrique.getId()).mapToDouble((var0) -> var0.getEcheancecourantecng()).sum();
   }

   public double encoursRetenueAE(Employe var1) {
      List<Retenuesaecheances> dl = new ArrayList(employe.getRetenuesaecheanceses());
      return dl.stream().filter((var0) -> !var0.isSolde()).mapToDouble((var0) -> var0.getCapital()).sum() - this.totalReglementRetenuesAE_Salarie(employe);
   }

   public void updateTrancheRetAE(Employe var1) {
      Date periode = this.menu.paramsGen.getPeriodeCourante();
      List<Retenuesaecheances> dl = new ArrayList(employe.getRetenuesaecheanceses());

      for(Retenuesaecheances rs : (List)dl.stream().filter((var0) -> !var0.isSolde() && var0.isActive()).collect(Collectors.toList())) {
         if (rs != null && periode != null) {
            if (this.menu.dialect.toString().contains("Oracle")) {
               GeneralLib var10000 = this.menu.gl;
               Long var10001 = rs.getId();
               var10000.exQuery("DELETE FROM Tranchesretenuesaecheances WHERE retenueAEcheances=" + var10001 + " AND periode=TO_DATE('" + this.menu.df.format(periode) + "','YYYY-MM-DD')");
            } else {
               GeneralLib var10 = this.menu.gl;
               Long var11 = rs.getId();
               var10.exQuery("DELETE FROM Tranchesretenuesaecheances WHERE retenueAEcheances=" + var11 + " AND periode='" + this.menu.df.format(periode) + "'");
            }

            Tranchesretenuesaecheances tranche = new Tranchesretenuesaecheances();
            tranche.setRetenuesaecheances(rs);
            tranche.setPeriode(periode);
            tranche.setMotif(1);
            tranche.setMontantRegle(rs.getEcheancecourante());
            this.menu.gl.insertOcurance(tranche);
            if (rs.getEcheancecourantecng() != null) {
               if (rs.getEcheancecourantecng() > (double)0.0F) {
                  tranche = new Tranchesretenuesaecheances();
                  tranche.setRetenuesaecheances(rs);
                  tranche.setPeriode(periode);
                  tranche.setMotif(2);
                  tranche.setMontantRegle(rs.getEcheancecourantecng());
                  this.menu.gl.insertOcurance(tranche);
               }
            } else {
               rs.setEcheancecourantecng((double)0.0F);
            }

            rs.setEcheancecourante(rs.getEcheance());
            rs.setEcheancecourantecng((double)0.0F);
            this.menu.gl.updateOcurance(rs);
         }
      }

   }

   public void updateRetenuesAE(Employe var1) {
      Date periodeSuivante = this.menu.paramsGen.getPeriodeSuivante();
      List<Retenuesaecheances> dl = new ArrayList(employe.getRetenuesaecheanceses());

      for(Retenuesaecheances rs : (List)dl.stream().filter((var0) -> !var0.isSolde() && var0.isActive()).collect(Collectors.toList())) {
         if (!rs.isSolde() && rs.isActive()) {
            double totalReg = this.totalReglementRetAE(rs);
            if (totalReg >= rs.getCapital()) {
               rs.setSolde(true);
               this.menu.gl.updateOcurance(rs);
               Rubriquepaie rs4 = this.rubriquePaieById(employe, rs.getRubrique(), this.menu.motifSN, periodeSuivante);
               if (rs4 != null) {
                  this.menu.gl.deleteOcurance(rs4);
               }
            }

            double restDuCapital = rs.getCapital() - totalReg;
            if (restDuCapital <= rs.getEcheance()) {
               rs.setEcheance(restDuCapital);
               rs.setEcheancecourante(restDuCapital);
               this.menu.gl.updateOcurance(rs);
            }

            this.insertRubrique(periodeSuivante, employe, rs.getRubrique(), this.menu.motifSN, this.totalEchRetenueAE_SN(employe, rs.getRubrique()), (double)1.0F, true, false);
         }
      }

   }

   public Double CNAMm(double var1) {
      double r = montant * (double)4.0F / (double)100.0F;
      return r > (double)0.0F ? r : (double)0.0F;
   }

   public Double RCNAMm(double var1, double var3) {
      double r = montant * (double)4.0F / (double)100.0F;
      r *= taux / (double)100.0F;
      return r > (double)0.0F ? r : (double)0.0F;
   }

   public Double CNSSm(double var1, double var3, int var5) {
      double plafonCnss = (double)0.0F;
      montant *= (double)1.0F;
      plafonCnss = (double)15000.0F;
      double r;
      if (montant <= plafonCnss) {
         r = montant / (double)100.0F;
      } else {
         r = plafonCnss / (double)100.0F;
      }

      return Long.valueOf(Double.valueOf(r).longValue()).doubleValue();
   }

   public Double RCNSSm(double var1, double var3, double var5, int var7) {
      double plafonCnss = (double)0.0F;
      plafonCnss = (double)15000.0F;
      double r;
      if (montant <= plafonCnss) {
         r = montant / (double)100.0F;
      } else {
         r = plafonCnss / (double)100.0F;
      }

      r *= taux / (double)100.0F;
      return Long.valueOf(Double.valueOf(r).longValue()).doubleValue();
   }

   public Double tranche1ITS(int var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
      double abattement = this.menu.paramsGen.getAbatement();
      double tranche1 = (double)0.0F;
      double taux1 = 0.15;
      double r1 = (double)0.0F;
      double x_cnss = (double)0.0F;
      double x_cnam = (double)0.0F;
      if (this.menu.paramsGen.isDeductionCnssdeIts()) {
         x_cnss = montantCNSS;
      }

      if (this.menu.paramsGen.isDeductionCnamdeIts()) {
         x_cnam = montantCNAM;
      }

      tranche1 = (double)9000.0F;
      taux1 = 0.15;
      if (expatrie) {
         taux1 /= (double)2.0F;
      }

      double ri = montant - abattement - x_cnss - x_cnam;
      double sb20pourCent = (salaireBase - avantagesNature) * 0.2;
      if (avantagesNature > sb20pourCent) {
         ri -= avantagesNature * 0.6;
      } else {
         ri -= avantagesNature;
      }

      if (ri <= tranche1) {
         r1 = ri * taux1;
      } else {
         r1 = tranche1 * taux1;
      }

      double r = r1;
      if (r1 < (double)0.0F) {
         r = (double)0.0F;
      }

      return r / tauxDevise;
   }

   public Double tranche2ITS(int var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
      double abattement = this.menu.paramsGen.getAbatement();
      double tranche1 = (double)0.0F;
      double tranche2 = (double)0.0F;
      double taux2 = (double)0.0F;
      double r2 = (double)0.0F;
      double x_cnss = (double)0.0F;
      double x_cnam = (double)0.0F;
      if (this.menu.paramsGen.isDeductionCnssdeIts()) {
         x_cnss = montantCNSS;
      }

      if (this.menu.paramsGen.isDeductionCnamdeIts()) {
         x_cnam = montantCNAM;
      }

      tranche1 = (double)9000.0F;
      tranche2 = (double)21000.0F;
      taux2 = (double)0.25F;
      if (this.menu.paramsGen.getModeITS().equalsIgnoreCase("T")) {
         taux2 = 0.2;
      }

      if (expatrie) {
         taux2 /= (double)2.0F;
      }

      double ri = montant - abattement - x_cnss - x_cnam;
      double sb20pourCent = (salaireBase - avantagesNature) * 0.2;
      if (avantagesNature > sb20pourCent) {
         ri -= avantagesNature * 0.6;
      } else {
         ri -= avantagesNature;
      }

      if (ri > tranche1) {
         if (ri <= tranche2) {
            r2 = (ri - tranche1) * taux2;
         } else {
            r2 = (tranche2 - tranche1) * taux2;
         }
      }

      double r = r2;
      if (r2 < (double)0.0F) {
         r = (double)0.0F;
      }

      return r / tauxDevise;
   }

   public Double tranche3ITS(int var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
      double abattement = this.menu.paramsGen.getAbatement();
      double tranche1 = (double)0.0F;
      double tranche2 = (double)0.0F;
      double taux3 = (double)0.0F;
      double r3 = (double)0.0F;
      double x_cnss = (double)0.0F;
      double x_cnam = (double)0.0F;
      double r = (double)0.0F;
      if (this.menu.paramsGen.isDeductionCnssdeIts()) {
         x_cnss = montantCNSS;
      }

      if (this.menu.paramsGen.isDeductionCnamdeIts()) {
         x_cnam = montantCNAM;
      }

      tranche1 = (double)9000.0F;
      tranche2 = (double)21000.0F;
      taux3 = 0.4;
      if (this.menu.paramsGen.getModeITS().equalsIgnoreCase("T")) {
         taux3 = 0.2;
      }

      if (expatrie) {
         taux3 /= (double)2.0F;
      }

      double ri = montant - abattement - x_cnss - x_cnam;
      double sb20pourCent = (salaireBase - avantagesNature) * 0.2;
      if (avantagesNature > sb20pourCent) {
         ri -= avantagesNature * 0.6;
      } else {
         ri -= avantagesNature;
      }

      if (ri > tranche1 && ri > tranche2) {
         r3 = (ri - tranche2) * taux3;
      }

      r = r3;
      if (r3 < (double)0.0F) {
         r = (double)0.0F;
      }

      return r / tauxDevise;
   }

   public Double ITSm(int var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
      double r1 = this.tranche1ITS(usedITS, montant, montantCNSS, montantCNAM, salaireBase, avantagesNature, tauxDevise, expatrie);
      double r2 = this.tranche2ITS(usedITS, montant, montantCNSS, montantCNAM, salaireBase, avantagesNature, tauxDevise, expatrie);
      double r3 = this.tranche3ITS(usedITS, montant, montantCNSS, montantCNAM, salaireBase, avantagesNature, tauxDevise, expatrie);
      double r = r1 + r2 + r3;
      if (r < (double)0.0F) {
         r = (double)0.0F;
      }

      return Long.valueOf(Double.valueOf(r).longValue()).doubleValue();
   }

   public Double RITSm(int var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, boolean var20) {
      double r1 = this.tranche1ITS(usedITS, montant, montantCNSS, montantCNAM, salaireBase, avantagesNature, tauxDevise, expatrie);
      double r2 = this.tranche2ITS(usedITS, montant, montantCNSS, montantCNAM, salaireBase, avantagesNature, tauxDevise, expatrie);
      double r3 = this.tranche3ITS(usedITS, montant, montantCNSS, montantCNAM, salaireBase, avantagesNature, tauxDevise, expatrie);
      r1 *= rembTaux1 / (double)100.0F;
      r2 *= rembTaux2 / (double)100.0F;
      r3 *= rembTaux3 / (double)100.0F;
      double r = r1 + r2 + r3;
      if (r < (double)0.0F) {
         r = (double)0.0F;
      }

      return Long.valueOf(Double.valueOf(r).longValue()).doubleValue();
   }

   public Double BrutDuNet(double var1, double var3, double var5, boolean var7, boolean var8, int var9, double var10, boolean var12) {
      double salBrutMax = montant * (double)2.0F + avNat;
      double abattement = this.menu.paramsGen.getAbatement();
      double plafonCnss = (double)0.0F;
      double x_cnss = (double)0.0F;
      double x_cnam = (double)0.0F;
      double salBrut = montant;
      plafonCnss = (double)15000.0F;
      if (montant > abattement) {
         for(double j = salBrutMax; j > montant + avNat; --j) {
            salBrut = j;
            if (i_cnss) {
               if (j >= plafonCnss) {
                  x_cnss = plafonCnss / (double)100.0F;
               }

               if (j < plafonCnss) {
                  x_cnss = j * 0.01;
               }
            }

            if (i_cnam) {
               x_cnam = j * 0.04;
            }

            double net = j - avNat - this.ITSm(usedITS, j, x_cnss, x_cnam, j, avNat, tauxDevise, expatrie) - x_cnss - x_cnam;
            if (net <= montant) {
               return j;
            }
         }
      }

      return salBrut;
   }

   public Double Operer(Double var1, Double var2, String var3) {
      Double r = (double)0.0F;
      switch (operateur) {
         case "-" -> r = v1 - v2;
         case "+" -> r = v1 + v2;
         case "*" -> r = v1 * v2;
         case "/" -> r = v1 / v2;
      }

      return r;
   }

   public String formulRubrique(Rubrique var1, Employe var2, Motif var3, String var4, Date var5) {
      DecimalFormat df = new DecimalFormat("#.#");
      df.setMaximumFractionDigits(5);
      df.setMinimumFractionDigits(5);
      df.setDecimalSeparatorAlwaysShown(true);
      String s = "";
      List<Rubriqueformule> dl = new ArrayList(rubrique.getRubriqueformules());
      List<Rubriqueformule> dl2 = (List)dl.stream().filter((var1x) -> var1x.getPartie().equalsIgnoreCase(partie)).sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList());
      if (!dl2.isEmpty()) {
         for(Rubriqueformule rs : dl2) {
            if (rs.getType().equals("O") && rs.getValText() != null) {
               s = s + rs.getValText();
            }

            if (rs.getType().equals("N") && rs.getValNum() != null && rs.getValNum() > (double)0.0F) {
               s = s + rs.getValNum();
            }

            if (rs.getType().equals("F") && rs.getValText() != null && !rs.getValText().isEmpty()) {
               Double rFonction = this.menu.fx.calFonction(rs.getValText(), employe, motif, periode);
               s = s + rFonction;
            }

            if (rs.getType().equals("R") && rs.getValText() != null && !rs.getValText().isEmpty()) {
               Rubriquepaie rp = this.rubriquePaieById(employe, this.rubriqueById(new Integer(rs.getValText())), motif, periode);
               Double rRubriquepaie = (double)0.0F;
               if (rp != null) {
                  rRubriquepaie = rp.getMontant();
               }

               s = s + rRubriquepaie;
            }
         }
      }

      return s;
   }

   public Boolean formulRubriqueOnSB(Rubrique var1, String var2) {
      String rf = this.rubriqueFormuleStr(rubrique, partie);
      boolean r = rf.contains("F02") || rf.contains("F03") || rf.contains("R1");
      return r;
   }

   public String rubriqueFormuleStr(Rubrique var1, String var2) {
      String s = "";
      List<Rubriqueformule> dl = new ArrayList(rubrique.getRubriqueformules());

      for(Rubriqueformule rs : (List)dl.stream().filter((var1x) -> var1x.getPartie().equalsIgnoreCase(partie)).sorted(Comparator.comparing(Rubriqueformule::getId)).collect(Collectors.toList())) {
         if (rs.getType().equalsIgnoreCase("N")) {
            s = s + " " + rs.getValNum().toString();
         } else if (rs.getType().equalsIgnoreCase("R")) {
            s = s + " R" + rs.getValText();
         } else {
            s = s + " " + rs.getValText();
         }
      }

      return s;
   }

   public Double baseRbrique(Date var1, Rubrique var2, Employe var3, Motif var4) {
      String s = this.formulRubrique(rubrique, employe, motif, "B", periode);
      return (new Calcul()).calculer(s);
   }

   public Double nombreRbrique(Date var1, Rubrique var2, Employe var3, Motif var4) {
      String s = this.formulRubrique(rubrique, employe, motif, "N", periode);
      return (new Calcul()).calculer(s);
   }

   public Date periodeByMY(int var1, int var2) {
      Date r = null;

      try {
         r = (new SimpleDateFormat("yyyy-MM-dd")).parse(year + "-" + month + "-28");
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public Date dateByString(String var1) {
      Date r = new Date();
      if (dateString.length() == 10) {
         try {
            r = (new SimpleDateFormat("dd/MM/yyyy")).parse(dateString);
            r = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-dd")).format(r));
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      return r;
   }

   public String employeCategorie(Employe var1) {
      return employe.getGrillesalairebase() == null ? "N/D" : employe.getGrillesalairebase().getNomCategorie();
   }

   public int maxProgressBarr(String var1) {
      int r = 0;
      int nbMotifs = 0;
      int nbSalaries = 0;
      switch (operation) {
         case "Paie":
            nbMotifs = ((List)this.menu.stricturesIF.dataListInit_Motifs.stream().filter((var0) -> var0.isActif()).collect(Collectors.toList())).size();
            nbSalaries = ((List)this.menu.employeFrame.dataListInit.stream().filter((var0) -> var0.isActif() && !var0.isEnConge()).collect(Collectors.toList())).size();
            r = nbMotifs * nbSalaries;
            break;
         case "Cloture":
            nbSalaries = ((List)this.dlPaie.stream().filter((var1x) -> this.menu.df.format(var1x.getPeriode()).equalsIgnoreCase(this.menu.df.format(this.menu.paramsGen.getPeriodeCourante()))).collect(Collectors.toList())).size();
            r = 4 * nbSalaries;
      }

      return r;
   }

   public int niveauMinCategorie(String var1) {
      return (Integer)this.menu.stricturesIF.dataListInit_Grillesalairebases.stream().filter((var1x) -> var1x.getNomCategorie().equalsIgnoreCase(nomCategorie)).map((var0) -> var0.getNiveau()).min(Integer::compareTo).orElse(1);
   }

   public int niveauMaxCategorie(String var1) {
      return (Integer)this.menu.stricturesIF.dataListInit_Grillesalairebases.stream().filter((var1x) -> var1x.getNomCategorie().equalsIgnoreCase(nomCategorie)).map((var0) -> var0.getNiveau()).max(Integer::compareTo).orElse(1);
   }

   public Date lastDateJourById(Employe var1) {
      List<Jour> dl = new ArrayList(employe.getJours());
      return (Date)dl.stream().filter((var0) -> var0.getDateJour() != null).map(Jour::getDateJour).max(Date::compareTo).orElse(new Date());
   }

   public Date lastDateWeekById(Employe var1) {
      List<Weekot> dl = new ArrayList(employe.getWeekots());
      return (Date)dl.stream().filter((var0) -> var0.getBeginweek() != null).map(Weekot::getBeginweek).max(Date::compareTo).orElse(new Date());
   }

   public Double montantCumuleRubriquePaieById(Employe var1, Rubrique var2, Motif var3, Date var4, Date var5) {
      List<Rubriquepaie> dl = new ArrayList(employe.getRubriquepaies());
      return dl.stream().filter((var5x) -> var5x.getMotif().getId() == motif.getId() && var5x.getRubrique().getId() == rubrique.getId() && var5x.getPeriode().after(this.menu.gl.addRetriveDays(periodeDu, -15)) && var5x.getPeriode().before(this.menu.gl.addRetriveDays(periodeAu, 15))).mapToDouble((var0) -> var0.getMontant()).sum();
   }

   public double cumulHeureEmpByPeriode(Employe var1, Date var2) {
      List<Jour> dl = new ArrayList(employe.getJours());
      return dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).mapToDouble((var0) -> var0.getNbHeureJour() + var0.getNbHeureNuit()).sum();
   }

   public int paieCountById(Employe var1) {
      List<Paie> dl = new ArrayList(employe.getPaies());
      return ((List)dl.stream().collect(Collectors.toList())).size();
   }

   public int paieCountByIdAfterPeriode(Employe var1, Date var2) {
      List<Paie> dl = new ArrayList(employe.getPaies());
      return ((List)dl.stream().filter((var2x) -> var2x.getPeriode().after(this.menu.gl.addRetriveDays(periode, -15))).collect(Collectors.toList())).size();
   }

   public double FTE_EmpByPeriode(Employe var1, Date var2) {
      double r = (double)1.0F;
      r = !this.estNouveauRecru(employe) && !this.estSortantAvantLeDernierJour(employe, periode) ? (double)1.0F : (double)0.5F;
      return r;
   }

   public boolean estNouveauRecru(Employe var1) {
      return this.paieCountById(employe) <= 1;
   }

   public boolean estSortantAvantLeDernierJour(Employe var1, Date var2) {
      return this.paieCountByIdAfterPeriode(employe, periode) == 0;
   }

   public Date firstMRU() {
      Date r = null;

      try {
         r = (new SimpleDateFormat("dd/MM/yyyy 00:00:00.0000")).parse("01/01/2018 00:00:00.0000");
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double cumulTypeById(Employe var1, String var2) {
      Double res = (double)0.0F;
      Paramgen pg = this.menu.paramsGen;
      Date periodeDD = this.dernierDepart(employe);
      Conges conge = this.congesById(employe, periodeDD);
      double baseCongesImposable = (double)0.0F;
      double baseCongesNonImposable = (double)0.0F;
      double cumulBIinit = (double)0.0F;
      double cumulBNIinit = (double)0.0F;
      double biCourant = (double)0.0F;
      double bniCourant = (double)0.0F;
      if (!this.menu.paramsGen.isAddCurrentSalInCumulCng()) {
         Paie paieCourant = this.paieById(employe, this.menu.motifSN, this.menu.paramsGen.getPeriodeCourante());
         if (paieCourant != null) {
            bniCourant = paieCourant.getBni();
            biCourant = paieCourant.getBt() - paieCourant.getBni();
         }
      }

      List<Paie> dl = new ArrayList(employe.getPaies());
      if (conge != null) {
         res = dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(conge.getPeriode())) && var2x.getMotif().getId() == 2).mapToDouble((var0) -> var0.getBt() - var0.getBni()).sum();
         baseCongesImposable = res;
         res = dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(conge.getPeriode())) && var2x.getMotif().getId() == 2).mapToDouble((var0) -> var0.getBni()).sum();
         baseCongesNonImposable = res;
      } else {
         cumulBIinit = employe.getCumulBrutImposableInitial();
         cumulBNIinit = employe.getCumulBrutNonImposableInitial();
      }

      res = (double)0.0F;
      List var28 = (List)dl.stream().filter((var3x) -> var3x.getPeriode().after(this.menu.gl.addRetriveDays(periodeDD, 15)) && var3x.getPeriode().before(this.menu.gl.addRetriveDays(pg.getPeriodeSuivante(), 15)) && var3x.getMotif().getId() != 2).collect(Collectors.toList());
      var28 = (List)var28.stream().sorted(Comparator.comparing(Paie::getPeriode)).collect(Collectors.toList());

      for(Paie var21 : var28) {
         ;
      }

      switch (type) {
         case "RET":
            res = var28.stream().mapToDouble((var0) -> var0.getRetenuesBrut()).sum();
            break;
         case "BI":
            res = var28.stream().mapToDouble((var0) -> var0.getBt() - var0.getBni()).sum();
            res = res + baseCongesImposable + cumulBIinit - biCourant;
            break;
         case "BNI":
            res = var28.stream().mapToDouble((var0) -> var0.getBni()).sum();
            res = res + baseCongesNonImposable + cumulBNIinit - bniCourant;
      }

      return res;
   }

   public double cumulBTAnnuel(Employe var1) {
      Date perCourant = this.menu.paramsGen.getPeriodeCourante();
      double r = (double)0.0F;

      try {
         SimpleDateFormat var10000 = new SimpleDateFormat("yyyy-MM-dd");
         Integer var10001 = this.menu.gl.yearFromDate(perCourant);
         Date fistPeriode = var10000.parse(var10001 + "-01-01");
         List<Paie> dl = new ArrayList(employe.getPaies());
         r = dl.stream().filter((var1x) -> var1x.getPeriode().after(fistPeriode)).mapToDouble((var0) -> var0.getBt()).sum();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public double cumulBrut12DerMois(Employe var1, Date var2, Date var3) {
      List<Paie> dl = new ArrayList(employe.getPaies());
      return dl.stream().filter((var3x) -> var3x.getPeriode().after(this.menu.gl.addRetriveDays(periodeBegin, -15)) && var3x.getPeriode().before(this.menu.gl.addRetriveDays(periodeEnd, 15))).mapToDouble((var0) -> var0.getBt()).sum();
   }

   public Date dernierDepart(Employe var1) {
      Date res = employe.getDateEmbauche();

      try {
         List<Conges> dl = new ArrayList(employe.getCongeses());
         res = (Date)dl.stream().filter((var0) -> var0.getDepart() != null).map(Conges::getDepart).max(Date::compareTo).orElse(employe.getDernierDepartInitial() != null ? employe.getDernierDepartInitial() : employe.getDateEmbauche());
      } catch (Exception e) {
         e.printStackTrace();
      }

      return res;
   }

   public Date dernierReprise(Employe var1) {
      Date res = employe.getDateEmbauche();

      try {
         List<Conges> dl = new ArrayList(employe.getCongeses());
         res = (Date)dl.stream().filter((var0) -> var0.getReprise() != null).map(Conges::getReprise).max(Date::compareTo).orElse(employe.getDernierDepartInitial() != null ? employe.getDernierDepartInitial() : employe.getDateEmbauche());
      } catch (Exception e) {
         e.printStackTrace();
      }

      return res;
   }

   public Date dernierPaiePeriode(Employe var1) {
      List<Paie> dl = new ArrayList(employe.getPaies());
      return (Date)dl.stream().filter((var0) -> var0.getPeriode() != null).map(Paie::getPeriode).max(Date::compareTo).orElse(this.menu.paramsGen.getPeriodeCourante());
   }

   public Double cumulNJTapresMoisCloture(Employe var1) {
      double r = employe.getCumulNjtinitial();
      return r;
   }

   public Double cumul12DerniersMois(Employe var1) {
      double r = (double)0.0F;
      return r;
   }

   public double totalRetenueSalariePeriode(Employe var1, Date var2) {
      List<Rubriquepaie> dl = new ArrayList(employe.getRubriquepaies());
      return dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode)) && var2x.getRubrique().getSens().equalsIgnoreCase("R")).mapToDouble((var0) -> var0.getMontant()).sum();
   }

   public double[] decompterHS(Employe var1, Date var2) {
      double[] res = new double[]{(double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F};

      try {
         Query q = this.menu.entityManager.createQuery("Select p from Jour p where p.employe.id=?1 and p.periode=?2").setParameter(1, employe.getId()).setParameter(2, periode);
         q.setMaxResults(1000000);
         List<Jour> dl = q.getResultList();
         dl = (List)dl.stream().sorted(Comparator.comparing(Jour::getDateJour)).collect(Collectors.toList());
         res = this.decompterHSList(employe, dl);
      } catch (Exception e) {
         e.printStackTrace();
      }

      return res;
   }

   public double[] decompterHSList(Employe var1, List<Jour> var2) {
      double[] res = new double[]{(double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F};

      try {
         double TOTHJOUR = (double)0.0F;
         double THN = (double)0.0F;
         double nHS15 = (double)0.0F;
         double nHS40 = (double)0.0F;
         double nHS50 = (double)0.0F;
         double nHS100 = (double)0.0F;
         double nHS = (double)0.0F;
         double nbPP = (double)0.0F;
         double nbPE = (double)0.0F;
         Double x_hsem = employe.getContratHeureSemaine();

         for(Jour rs : dl) {
            nbPP += rs.getNbPrimePanier();
            if (rs.isFerie50()) {
               nHS50 += rs.getNbHeureJour();
               nHS100 += rs.getNbHeureNuit();
            }

            if (rs.isFerie100()) {
               nHS100 += rs.getNbHeureJour();
               nHS100 += rs.getNbHeureNuit();
            }

            if (!rs.isFerie100() && !rs.isFerie50()) {
               TOTHJOUR += rs.getNbHeureJour();
               THN += rs.getNbHeureNuit();
               nHS50 += rs.getNbHeureNuit();
            }

            if (rs.isSiteExterne()) {
               ++nbPE;
            }

            if (this.infosJourById(rs.getDateJour(), employe).isFin()) {
               double nhs = TOTHJOUR - x_hsem;
               if (nhs > (double)0.0F) {
                  if (x_hsem < (double)48.0F) {
                     if (nhs <= (double)8.0F) {
                        nHS15 += nhs;
                     } else {
                        nHS15 += (double)8.0F;
                        double reste_hs = nhs - (double)8.0F;
                        if (reste_hs <= (double)6.0F) {
                           nHS40 += reste_hs;
                        } else {
                           nHS40 += (double)6.0F;
                           reste_hs = nhs - (double)8.0F - (double)6.0F;
                           if (reste_hs > (double)0.0F) {
                              nHS50 += reste_hs;
                           }
                        }
                     }
                  } else if (x_hsem < (double)54.0F) {
                     if (nhs <= (double)6.0F) {
                        nHS40 += nhs;
                     } else {
                        nHS40 += (double)6.0F;
                        double reste_hs = nhs - (double)6.0F;
                        if (reste_hs > (double)0.0F) {
                           nHS50 += reste_hs;
                        }
                     }
                  } else {
                     nHS50 += nhs;
                  }
               }

               TOTHJOUR = (double)0.0F;
               THN = (double)0.0F;
            }
         }

         nHS = nHS15 + nHS40 + nHS50 + nHS100;
         double[] r = new double[]{TOTHJOUR, THN, nHS15, nHS40, nHS50, nHS100, nbPP, nbPE, nHS};
         res = r;
      } catch (Exception e) {
         e.printStackTrace();
      }

      return res;
   }

   public double[] decompterHS_(Employe var1, Date var2) {
      double[] res = new double[]{(double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F};

      try {
         double TOTHJOUR = (double)0.0F;
         double THN = (double)0.0F;
         double nHS15 = (double)0.0F;
         double nHS40 = (double)0.0F;
         double nHS50 = (double)0.0F;
         double nHS100 = (double)0.0F;
         double nHS = (double)0.0F;
         double nbPP = (double)0.0F;
         double nbPE = (double)0.0F;
         Double x_hsem = employe.getContratHeureSemaine();
         List<Jour> dl = new ArrayList(employe.getJours());

         for(Jour rs : (List)dl.stream().filter((var2x) -> this.menu.df.format(var2x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).sorted(Comparator.comparing(Jour::getDateJour)).collect(Collectors.toList())) {
            nbPP += rs.getNbPrimePanier();
            if (rs.isFerie50()) {
               nHS50 += rs.getNbHeureJour();
               nHS100 += rs.getNbHeureNuit();
            }

            if (rs.isFerie100()) {
               nHS100 += rs.getNbHeureJour();
               nHS100 += rs.getNbHeureNuit();
            }

            if (!rs.isFerie100() && !rs.isFerie50()) {
               TOTHJOUR += rs.getNbHeureJour();
               THN += rs.getNbHeureNuit();
               nHS50 += rs.getNbHeureNuit();
            }

            if (rs.isSiteExterne()) {
               ++nbPE;
            }

            if (this.infosJourById(rs.getDateJour(), employe).isFin()) {
               double nhs = TOTHJOUR - x_hsem;
               if (nhs > (double)0.0F) {
                  if (x_hsem < (double)48.0F) {
                     if (nhs <= (double)8.0F) {
                        nHS15 += nhs;
                     } else {
                        nHS15 += (double)8.0F;
                        double reste_hs = nhs - (double)8.0F;
                        if (reste_hs <= (double)6.0F) {
                           nHS40 += reste_hs;
                        } else {
                           nHS40 += (double)6.0F;
                           reste_hs = nhs - (double)8.0F - (double)6.0F;
                           if (reste_hs > (double)0.0F) {
                              nHS50 += reste_hs;
                           }
                        }
                     }
                  } else if (x_hsem < (double)54.0F) {
                     if (nhs <= (double)6.0F) {
                        nHS40 += nhs;
                     } else {
                        nHS40 += (double)6.0F;
                        double reste_hs = nhs - (double)6.0F;
                        if (reste_hs > (double)0.0F) {
                           nHS50 += reste_hs;
                        }
                     }
                  } else {
                     nHS50 += nhs;
                  }
               }

               TOTHJOUR = (double)0.0F;
               THN = (double)0.0F;
            }
         }

         nHS = nHS15 + nHS40 + nHS50 + nHS100;
         double[] r = new double[]{TOTHJOUR, THN, nHS15, nHS40, nHS50, nHS100, nbPP, nbPE, nHS};
         res = r;
      } catch (Exception e) {
         e.printStackTrace();
      }

      return res;
   }

   public double[] decompterHSIntervall(Employe var1, Date var2, Date var3) {
      double[] res = new double[]{(double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F};

      try {
         Query q = this.menu.entityManager.createQuery("Select p from Jour p where p.employe.id=?1 and p.dateJour >=?2 and p.dateJour <=?3").setParameter(1, employe.getId()).setParameter(2, beginDate).setParameter(3, endDate);
         q.setMaxResults(1000000);
         List<Jour> dl = q.getResultList();
         dl = (List)dl.stream().sorted(Comparator.comparing(Jour::getDateJour)).collect(Collectors.toList());
         res = this.decompterHSList(employe, dl);
      } catch (Exception e) {
         e.printStackTrace();
      }

      return res;
   }

   public boolean deleteJourFromDate(Date var1, long var2) {
      Date periodeCourante = this.menu.paramsGen.getPeriodeCourante();
      boolean r = false;

      try {
         if (this.menu.dialect.toString().contains("Oracle")) {
            this.menu.gl.exQuery("DELETE FROM Jour WHERE employe=" + idSalarie + " AND dateJour>=TO_DATE('" + this.menu.df.format(beginDate) + "','YYYY-MM-DD') AND periode=TO_DATE('" + this.menu.df.format(periodeCourante) + "','YYYY-MM-DD')");
         } else {
            this.menu.gl.exQuery("DELETE FROM Jour WHERE employe=" + idSalarie + " AND dateJour>='" + this.menu.df.format(beginDate) + "' AND periode='" + this.menu.df.format(periodeCourante) + "'");
         }

         r = true;
      } catch (HibernateException he) {
         he.printStackTrace();
      }

      return r;
   }

   public boolean valoriserHS(Employe var1, Motif var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      boolean r = true;
      Date periodeCourante = this.menu.paramsGen.getPeriodeCourante();
      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(3), motif, (Double)null, nHS115, false, false)) {
         r = false;
      }

      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(4), motif, (Double)null, nHS140, false, false)) {
         r = false;
      }

      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(5), motif, (Double)null, nHS150, false, false)) {
         r = false;
      }

      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(6), motif, (Double)null, nHS200, false, false)) {
         r = false;
      }

      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(7), motif, (Double)null, nPP, false, false)) {
         r = false;
      }

      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(8), motif, (Double)null, nPE, false, false)) {
         r = false;
      }

      return r;
   }

   public boolean deleteValoriserHS(Employe var1) {
      boolean r = true;
      Date periodeCourante = this.menu.paramsGen.getPeriodeCourante();
      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(3), this.menu.motifSN, (double)0.0F, (double)0.0F, false, false)) {
         r = false;
      }

      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(4), this.menu.motifSN, (double)0.0F, (double)0.0F, false, false)) {
         r = false;
      }

      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(5), this.menu.motifSN, (double)0.0F, (double)0.0F, false, false)) {
         r = false;
      }

      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(6), this.menu.motifSN, (double)0.0F, (double)0.0F, false, false)) {
         r = false;
      }

      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(7), this.menu.motifSN, (double)0.0F, (double)0.0F, false, false)) {
         r = false;
      }

      if (!this.insertRubrique(periodeCourante, employe, this.usedRubID(8), this.menu.motifSN, (double)0.0F, (double)0.0F, false, false)) {
         r = false;
      }

      return r;
   }

   public boolean expirationDocAdmin(Employe var1) {
      boolean r = false;
      if (employe != null) {
         if (employe.getDateExpirationPassport() != null && this.menu.gl.differenceDateJour(new Date(), employe.getDateExpirationPassport()) <= 10L) {
            r = true;
         }

         if (employe.isExpatrie()) {
            if (employe.getDateExpirationPermiTravail() != null && this.menu.gl.differenceDateJour(new Date(), employe.getDateExpirationPermiTravail()) <= 10L) {
               r = true;
            }

            if (employe.getDateFinVisa() != null && this.menu.gl.differenceDateJour(new Date(), employe.getDateFinVisa()) <= 10L) {
               r = true;
            }
         }
      }

      return r;
   }

   public boolean deletePointageFromDate(Date var1, long var2) {
      boolean r = false;

      try {
         if (!this.menu.entityManager.getTransaction().isActive()) {
            this.menu.entityManager.getTransaction().begin();
         }

         this.menu.entityManager.createNativeQuery("Deletw from Donneespointeuse p where p.idSalarie=?1 and p.heureJour >=?2").setParameter(1, idSalariePointeuse).setParameter(2, beginDate).executeUpdate();
         this.menu.entityManager.flush();
         this.menu.entityManager.getTransaction().commit();
         r = true;
      } catch (Exception e) {
         e.printStackTrace();
         this.menu.entityManager.getTransaction().rollback();
      }

      return r;
   }

   public boolean deletePointageFromDateAll(boolean var1, Date var2) {
      boolean r = false;

      try {
         if (!this.menu.entityManager.getTransaction().isActive()) {
            this.menu.entityManager.getTransaction().begin();
         }

         if (uniquementImporte) {
            this.menu.entityManager.createNativeQuery("Delete from Donneespointeuse p where p.importe=?1 and p.heureJour >=?2").setParameter(1, true).setParameter(2, beginDate).executeUpdate();
         } else {
            this.menu.entityManager.createNativeQuery("Delete from Donneespointeuse p where p.heureJour >=?1").setParameter(1, beginDate).executeUpdate();
            this.menu.entityManager.createNativeQuery("Delete from Jour p where p.dateJour >=?1").setParameter(1, beginDate).executeUpdate();
         }

         this.menu.entityManager.flush();
         this.menu.entityManager.getTransaction().commit();
         r = true;
      } catch (Exception e) {
         e.printStackTrace();
         this.menu.entityManager.getTransaction().rollback();
      }

      return r;
   }
}
