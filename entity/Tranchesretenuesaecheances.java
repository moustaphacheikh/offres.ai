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
   name = "tranchesretenuesaecheances",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Tranchesretenuesaecheances implements Serializable {
   private Long id;
   private Retenuesaecheances retenuesaecheances;
   private Date periode;
   private double montantRegle;
   private Integer motif;

   public Tranchesretenuesaecheances() {
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
      name = "retenueAEcheances"
   )
   public Retenuesaecheances getRetenuesaecheances() {
      return this.retenuesaecheances;
   }

   public void setRetenuesaecheances(Retenuesaecheances var1) {
      this.retenuesaecheances = retenuesaecheances;
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
      name = "montantRegle",
      nullable = false,
      precision = 22,
      scale = 0
   )
   public double getMontantRegle() {
      return this.montantRegle;
   }

   public void setMontantRegle(double var1) {
      this.montantRegle = montantRegle;
   }

   @Column(
      name = "motif"
   )
   public Integer getMotif() {
      return this.motif;
   }

   public void setMotif(Integer var1) {
      this.motif = motif;
   }
}
