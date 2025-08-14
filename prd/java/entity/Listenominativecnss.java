package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;

public class Listenominativecnss implements Serializable {
   private Long id;
   private Date periode;
   private String noCnssemploye;
   private String nomEmploye;
   private String nbJour1erMois;
   private String nbJour2emeMois;
   private String nbJour3emeMois;
   private double totalNbJour;
   private double remunerationReeles;
   private double plafond;
   private String dateEmbauche;
   private String dateDebauche;

   public Listenominativecnss() {
   }

   public Long getId() {
      return this.id;
   }

   public void setId(Long var1) {
      this.id = id;
   }

   public Date getPeriode() {
      return this.periode;
   }

   public void setPeriode(Date var1) {
      this.periode = periode;
   }

   public String getNoCnssemploye() {
      return this.noCnssemploye;
   }

   public void setNoCnssemploye(String var1) {
      this.noCnssemploye = noCnssemploye;
   }

   public String getNomEmploye() {
      return this.nomEmploye;
   }

   public void setNomEmploye(String var1) {
      this.nomEmploye = nomEmploye;
   }

   public String getNbJour1erMois() {
      return this.nbJour1erMois;
   }

   public void setNbJour1erMois(String var1) {
      this.nbJour1erMois = nbJour1erMois;
   }

   public String getNbJour2emeMois() {
      return this.nbJour2emeMois;
   }

   public void setNbJour2emeMois(String var1) {
      this.nbJour2emeMois = nbJour2emeMois;
   }

   public String getNbJour3emeMois() {
      return this.nbJour3emeMois;
   }

   public void setNbJour3emeMois(String var1) {
      this.nbJour3emeMois = nbJour3emeMois;
   }

   public double getTotalNbJour() {
      return this.totalNbJour;
   }

   public void setTotalNbJour(double var1) {
      this.totalNbJour = totalNbJour;
   }

   public double getRemunerationReeles() {
      return this.remunerationReeles;
   }

   public void setRemunerationReeles(double var1) {
      this.remunerationReeles = remunerationReeles;
   }

   public double getPlafond() {
      return this.plafond;
   }

   public void setPlafond(double var1) {
      this.plafond = plafond;
   }

   public String getDateEmbauche() {
      return this.dateEmbauche;
   }

   public void setDateEmbauche(String var1) {
      this.dateEmbauche = dateEmbauche;
   }

   public String getDateDebauche() {
      return this.dateDebauche;
   }

   public void setDateDebauche(String var1) {
      this.dateDebauche = dateDebauche;
   }
}
