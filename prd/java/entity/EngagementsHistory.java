package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "engagementsHistory",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class EngagementsHistory implements Serializable {
   private int id;
   private Date periode;
   private int idSalarie;
   private String prenomSalarie;
   private String nomSalarie;
   private Date dateAccord;
   private String note;
   private double capital;
   private double encours;
   private double totalRegle;
   private double echeance;

   public EngagementsHistory() {
   }

   @Id
   @GeneratedValue(
      generator = "incrementor"
   )
   @GenericGenerator(
      name = "incrementor",
      strategy = "increment"
   )
   @Column(
      name = "id",
      unique = true,
      nullable = false
   )
   public int getId() {
      return this.id;
   }

   public void setId(int var1) {
      this.id = id;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "periode",
      nullable = false,
      length = 10
   )
   public Date getPeriode() {
      return this.periode;
   }

   public void setPeriode(Date var1) {
      this.periode = periode;
   }

   @Column(
      name = "idSalarie"
   )
   public int getIdSalarie() {
      return this.idSalarie;
   }

   public void setIdSalarie(int var1) {
      this.idSalarie = idSalarie;
   }

   @Column(
      name = "prenomSalarie",
      nullable = true,
      length = 50
   )
   public String getPrenomSalarie() {
      return this.prenomSalarie;
   }

   public void setPrenomSalarie(String var1) {
      this.prenomSalarie = prenomSalarie;
   }

   @Column(
      name = "nomSalarie",
      nullable = true,
      length = 50
   )
   public String getNomSalarie() {
      return this.nomSalarie;
   }

   public void setNomSalarie(String var1) {
      this.nomSalarie = nomSalarie;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateAccord",
      length = 10
   )
   public Date getDateAccord() {
      return this.dateAccord;
   }

   public void setDateAccord(Date var1) {
      this.dateAccord = dateAccord;
   }

   @Column(
      name = "note",
      length = 500
   )
   public String getNote() {
      return this.note;
   }

   public void setNote(String var1) {
      this.note = note;
   }

   @Column(
      name = "capital",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getCapital() {
      return this.capital;
   }

   public void setCapital(double var1) {
      this.capital = capital;
   }

   @Column(
      name = "encours",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getEncours() {
      return this.encours;
   }

   public void setEncours(double var1) {
      this.encours = encours;
   }

   @Column(
      name = "totalRegle",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getTotalRegle() {
      return this.totalRegle;
   }

   public void setTotalRegle(double var1) {
      this.totalRegle = totalRegle;
   }

   @Column(
      name = "echeance",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getEcheance() {
      return this.echeance;
   }

   public void setEcheance(double var1) {
      this.echeance = echeance;
   }
}
