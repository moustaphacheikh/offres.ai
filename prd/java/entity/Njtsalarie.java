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
   name = "njtsalarie",
   catalog = "eliyapaiebd",
   schema = "PAYROLL",
   uniqueConstraints = {@UniqueConstraint(
   columnNames = {"periode", "employe", "motif"}
)}
)
public class Njtsalarie implements Serializable {
   private Long id;
   private Employe employe;
   private Motif motif;
   private Date periode;
   private Double njt;

   public Njtsalarie() {
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

   public void setId(Long var1) {
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

   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "motif",
      nullable = false
   )
   public Motif getMotif() {
      return this.motif;
   }

   public void setMotif(Motif var1) {
      this.motif = motif;
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
      name = "njt",
      precision = 22,
      scale = 0
   )
   public double getNjt() {
      return this.njt;
   }

   public void setNjt(Double var1) {
      this.njt = njt;
   }
}
