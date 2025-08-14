package com.mccmr.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "enfants",
   catalog = "eliyapaiebd",
   schema = "PAYROLL",
   uniqueConstraints = {@UniqueConstraint(
   columnNames = {"employe", "id"}
)}
)
public class Enfants implements Serializable {
   private long id;
   private Employe employe;
   private String nomEnfant;
   private Date dateNaissanace;
   private String mereOuPere;
   private String genre;

   public Enfants() {
   }

   public Enfants(long var1, Employe var3) {
      this.id = id;
      this.employe = employe;
   }

   public Enfants(long var1, Employe var3, String var4, Date var5, String var6, String var7) {
      this.id = id;
      this.employe = employe;
      this.nomEnfant = nomEnfant;
      this.dateNaissanace = dateNaissanace;
      this.mereOuPere = mereOuPere;
      this.genre = genre;
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
   public Long getId() {
      return this.id;
   }

   public void setId(long var1) {
      this.id = id;
   }

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "employe",
      nullable = false
   )
   public Employe getEmploye() {
      return this.employe;
   }

   public void setEmploye(Employe var1) {
      this.employe = employe;
   }

   @Column(
      name = "nomEnfant",
      length = 50
   )
   public String getNomEnfant() {
      return this.nomEnfant;
   }

   public void setNomEnfant(String var1) {
      this.nomEnfant = nomEnfant;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateNaissanace",
      length = 10
   )
   public Date getDateNaissanace() {
      return this.dateNaissanace;
   }

   public void setDateNaissanace(Date var1) {
      this.dateNaissanace = dateNaissanace;
   }

   @Column(
      name = "mereOuPere",
      length = 50
   )
   public String getMereOuPere() {
      return this.mereOuPere;
   }

   public void setMereOuPere(String var1) {
      this.mereOuPere = mereOuPere;
   }

   @Column(
      name = "genre",
      length = 50
   )
   public String getGenre() {
      return this.genre;
   }

   public void setGenre(String var1) {
      this.genre = genre;
   }
}
