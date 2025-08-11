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
   name = "conges",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Conges implements Serializable {
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
   private Long id;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "employe",
      nullable = false
   )
   private Employe employe;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "periode",
      nullable = false,
      length = 10
   )
   private Date periode;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "depart",
      nullable = false,
      length = 10
   )
   private Date depart;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "reprise",
      nullable = false,
      length = 10
   )
   private Date reprise;
   @Column(
      name = "note",
      nullable = true,
      length = 500
   )
   private String note;
   @Temporal(TemporalType.DATE)
   @Column(
      name = "repriseeff",
      nullable = false,
      length = 10
   )
   private Date repriseeff;

   public Conges() {
   }

   public Long getId() {
      return this.id;
   }

   public void setId(Long var1) {
      this.id = id;
   }

   public Employe getEmploye() {
      return this.employe;
   }

   public void setEmploye(Employe var1) {
      this.employe = employe;
   }

   public Date getPeriode() {
      return this.periode;
   }

   public void setPeriode(Date var1) {
      this.periode = periode;
   }

   public Date getDepart() {
      return this.depart;
   }

   public void setDepart(Date var1) {
      this.depart = depart;
   }

   public Date getReprise() {
      return this.reprise;
   }

   public void setReprise(Date var1) {
      this.reprise = reprise;
   }

   public String getNote() {
      return this.note;
   }

   public void setNote(String var1) {
      this.note = note;
   }

   public Date getRepriseeff() {
      return this.repriseeff;
   }

   public void setRepriseeff(Date var1) {
      this.repriseeff = repriseeff;
   }
}
