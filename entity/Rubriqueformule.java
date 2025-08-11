package com.mccmr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
   name = "rubriqueformule",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Rubriqueformule implements Serializable {
   private Long id;
   private Rubrique rubrique;
   private String partie;
   private String type;
   private String valText;
   private Double valNum;

   public Rubriqueformule() {
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
      name = "rubrique",
      nullable = false
   )
   public Rubrique getRubrique() {
      return this.rubrique;
   }

   public void setRubrique(Rubrique var1) {
      this.rubrique = rubrique;
   }

   @Column(
      name = "partie",
      nullable = false,
      length = 1
   )
   public String getPartie() {
      return this.partie;
   }

   public void setPartie(String var1) {
      this.partie = partie;
   }

   @Column(
      name = "type",
      nullable = false,
      length = 1
   )
   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = type;
   }

   @Column(
      name = "valText",
      length = 10
   )
   public String getValText() {
      return this.valText;
   }

   public void setValText(String var1) {
      this.valText = valText;
   }

   @Column(
      name = "valNum",
      precision = 22,
      scale = 0
   )
   public Double getValNum() {
      return this.valNum;
   }

   public void setValNum(Double var1) {
      this.valNum = valNum;
   }
}
