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
   name = "Document",
   catalog = "eliyapaiebd",
   schema = "PAYROLL"
)
public class Document implements Serializable {
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
   @Column(
      name = "nom",
      length = 500
   )
   private String nom;
   @Column(
      name = "docFile",
      nullable = true
   )
   private byte[] docFile;
   @Column(
      name = "fileType",
      length = 500
   )
   private String fileType;
   @ManyToOne(
      fetch = FetchType.LAZY
   )
   @JoinColumn(
      name = "employe",
      nullable = false
   )
   private Employe employe;

   public Document() {
   }

   public Long getId() {
      return this.id;
   }

   public void setId(Long var1) {
      this.id = id;
   }

   public String getNom() {
      return this.nom;
   }

   public void setNom(String var1) {
      this.nom = nom;
   }

   public byte[] getDocFile() {
      return this.docFile;
   }

   public void setDocFile(byte[] var1) {
      this.docFile = docFile;
   }

   public Employe getEmploye() {
      return this.employe;
   }

   public void setEmploye(Employe var1) {
      this.employe = employe;
   }

   public String getFileType() {
      return this.fileType;
   }

   public void setFileType(String var1) {
      this.fileType = fileType;
   }
}
