package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;

public class Listenominativecnam implements Serializable {
   private long no;
   private Date periode;
   private long matriculeFonc;
   private String noCnam;
   private String nni;
   private String nomEmploye;
   private String dateEntre;
   private String dateSortie;
   private double assieteSoumiseMois1;
   private double assieteSoumiseMois2;
   private double assieteSoumiseMois3;
   private Double nbJourMois1;
   private Double nbJourMois2;
   private Double nbJourMois3;

   public Listenominativecnam() {
   }

   public long getNo() {
      return this.no;
   }

   public void setNo(long var1) {
      this.no = no;
   }

   public Date getPeriode() {
      return this.periode;
   }

   public void setPeriode(Date var1) {
      this.periode = periode;
   }

   public long getMatriculeFonc() {
      return this.matriculeFonc;
   }

   public void setMatriculeFonc(long var1) {
      this.matriculeFonc = matriculeFonc;
   }

   public String getNoCnam() {
      return this.noCnam;
   }

   public void setNoCnam(String var1) {
      this.noCnam = noCnam;
   }

   public String getNni() {
      return this.nni;
   }

   public void setNni(String var1) {
      this.nni = nni;
   }

   public String getNomEmploye() {
      return this.nomEmploye;
   }

   public void setNomEmploye(String var1) {
      this.nomEmploye = nomEmploye;
   }

   public String getDateEntre() {
      return this.dateEntre;
   }

   public void setDateEntre(String var1) {
      this.dateEntre = dateEntre;
   }

   public String getDateSortie() {
      return this.dateSortie;
   }

   public void setDateSortie(String var1) {
      this.dateSortie = dateSortie;
   }

   public double getAssieteSoumiseMois1() {
      return this.assieteSoumiseMois1;
   }

   public void setAssieteSoumiseMois1(double var1) {
      this.assieteSoumiseMois1 = assieteSoumiseMois1;
   }

   public double getAssieteSoumiseMois2() {
      return this.assieteSoumiseMois2;
   }

   public void setAssieteSoumiseMois2(double var1) {
      this.assieteSoumiseMois2 = assieteSoumiseMois2;
   }

   public double getAssieteSoumiseMois3() {
      return this.assieteSoumiseMois3;
   }

   public void setAssieteSoumiseMois3(double var1) {
      this.assieteSoumiseMois3 = assieteSoumiseMois3;
   }

   public Double getNbJourMois1() {
      return this.nbJourMois1;
   }

   public void setNbJourMois1(Double var1) {
      this.nbJourMois1 = nbJourMois1;
   }

   public Double getNbJourMois2() {
      return this.nbJourMois2;
   }

   public void setNbJourMois2(Double var1) {
      this.nbJourMois2 = nbJourMois2;
   }

   public Double getNbJourMois3() {
      return this.nbJourMois3;
   }

   public void setNbJourMois3(Double var1) {
      this.nbJourMois3 = nbJourMois3;
   }
}
