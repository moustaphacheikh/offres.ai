package com.mccmr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "banque",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Banque implements Serializable {
   private Integer id;
   private String nom;
   private Long noCompteCompta;
   private long noChapitreCompta;
   private String noCompteComptaCle;

   public Banque() {
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
      name = "Id",
      unique = true,
      nullable = false
   )
   public Integer getId() {
      return this.id;
   }

   public void setId(int var1) {
      this.id = id;
   }

   @Column(
      name = "nom",
      length = 50
   )
   public String getNom() {
      return this.nom;
   }

   public void setNom(String var1) {
      this.nom = nom;
   }

   @Column(
      name = "noCompteCompta"
   )
   public Long getNoCompteCompta() {
      return this.noCompteCompta;
   }

   public void setNoCompteCompta(Long var1) {
      this.noCompteCompta = noCompteCompta;
   }

   @Column(
      name = "noChapitreCompta",
      nullable = false
   )
   public long getNoChapitreCompta() {
      return this.noChapitreCompta;
   }

   public void setNoChapitreCompta(long var1) {
      this.noChapitreCompta = noChapitreCompta;
   }

   @Column(
      name = "noCompteComptaCle",
      nullable = false
   )
   public String getNoCompteComptaCle() {
      return this.noCompteComptaCle;
   }

   public void setNoCompteComptaCle(String var1) {
      this.noCompteComptaCle = noCompteComptaCle;
   }
}
