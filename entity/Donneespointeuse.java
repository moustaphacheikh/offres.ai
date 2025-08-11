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
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "donneespointeuse",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Donneespointeuse implements Serializable {
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
   private long id;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "employe",
      nullable = false
   )
   private Employe employe;
   @Temporal(TemporalType.TIMESTAMP)
   @Column(
      name = "heureJour",
      nullable = false,
      length = 30
   )
   private Date heureJour;
   @Column(
      name = "vinOut",
      length = 1
   )
   private String vinOut;
   @Column(
      name = "importe"
   )
   private boolean importe;

   public Donneespointeuse() {
   }

   public long getId() {
      return this.id;
   }

   public void setId(long var1) {
      this.id = id;
   }

   public Employe getEmploye() {
      return this.employe;
   }

   public void setEmploye(Employe var1) {
      this.employe = employe;
   }

   public Date getHeureJour() {
      return this.heureJour;
   }

   public void setHeureJour(Date var1) {
      this.heureJour = heureJour;
   }

   public String getVinOut() {
      return this.vinOut;
   }

   public void setVinOut(String var1) {
      this.vinOut = vinOut;
   }

   public boolean isImporte() {
      return this.importe;
   }

   public void setImporte(boolean var1) {
      this.importe = importe;
   }
}
