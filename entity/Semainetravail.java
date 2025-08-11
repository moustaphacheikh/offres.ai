package com.mccmr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
   name = "semainetravail",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Semainetravail implements Serializable {
   @Id
   @Column(
      name = "jour",
      unique = true,
      nullable = false,
      length = 50
   )
   private String jour;
   @Column(
      name = "debut",
      nullable = false
   )
   private boolean debut;
   @Column(
      name = "fin",
      nullable = false
   )
   private boolean fin;
   @Column(
      name = "weekEnd",
      nullable = false
   )
   private boolean weekEnd;

   public Semainetravail() {
   }

   public String getJour() {
      return this.jour;
   }

   public void setJour(String var1) {
      this.jour = jour;
   }

   public boolean isDebut() {
      return this.debut;
   }

   public void setDebut(boolean var1) {
      this.debut = debut;
   }

   public boolean isFin() {
      return this.fin;
   }

   public void setFin(boolean var1) {
      this.fin = fin;
   }

   public boolean isWeekEnd() {
      return this.weekEnd;
   }

   public void setWeekEnd(boolean var1) {
      this.weekEnd = weekEnd;
   }
}
