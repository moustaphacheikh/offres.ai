package com.mccmr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
   name = "sysrubrique",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Sysrubrique implements Serializable {
   @Id
   @Column(
      name = "idSys",
      unique = true,
      nullable = false
   )
   private int idSys;
   @Column(
      name = "libelle",
      nullable = false,
      length = 500
   )
   private String libelle;
   @Column(
      name = "idCustum",
      nullable = false
   )
   private int idCustum;

   public Sysrubrique() {
   }

   public Sysrubrique(int var1, String var2, int var3) {
      this.idSys = idSys;
      this.libelle = libelle;
      this.idCustum = idCustum;
   }

   public int getIdSys() {
      return this.idSys;
   }

   public void setIdSys(int var1) {
      this.idSys = idSys;
   }

   public String getLibelle() {
      return this.libelle;
   }

   public void setLibelle(String var1) {
      this.libelle = libelle;
   }

   public int getIdCustum() {
      return this.idCustum;
   }

   public void setIdCustum(int var1) {
      this.idCustum = idCustum;
   }
}
