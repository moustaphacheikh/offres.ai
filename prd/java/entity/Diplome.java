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
   name = "diplome",
   catalog = "eliyapaiebd",
   schema = "PAYROLL",
   uniqueConstraints = {@UniqueConstraint(
   columnNames = {"employe", "id"}
)}
)
public class Diplome implements Serializable {
   private long id;
   private Employe employe;
   private String nom;
   private Date dateObtention;
   private String degre;
   private String etablissement;
   private String domaine;

   public Diplome() {
   }

   public Diplome(long var1, Employe var3) {
      this.id = id;
      this.employe = employe;
   }

   public Diplome(long var1, Employe var3, String var4, Date var5, String var6, String var7, String var8) {
      this.id = id;
      this.employe = employe;
      this.nom = nom;
      this.dateObtention = dateObtention;
      this.degre = degre;
      this.etablissement = etablissement;
      this.domaine = domaine;
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
      name = "nom"
   )
   public String getNom() {
      return this.nom;
   }

   public void setNom(String var1) {
      this.nom = nom;
   }

   @Temporal(TemporalType.DATE)
   @Column(
      name = "dateObtention",
      length = 10
   )
   public Date getDateObtention() {
      return this.dateObtention;
   }

   public void setDateObtention(Date var1) {
      this.dateObtention = dateObtention;
   }

   @Column(
      name = "degre",
      length = 50
   )
   public String getDegre() {
      return this.degre;
   }

   public void setDegre(String var1) {
      this.degre = degre;
   }

   @Column(
      name = "etablissement"
   )
   public String getEtablissement() {
      return this.etablissement;
   }

   public void setEtablissement(String var1) {
      this.etablissement = etablissement;
   }

   @Column(
      name = "domaine"
   )
   public String getDomaine() {
      return this.domaine;
   }

   public void setDomaine(String var1) {
      this.domaine = domaine;
   }
}
