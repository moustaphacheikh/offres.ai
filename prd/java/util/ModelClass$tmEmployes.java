package com.mccmr.util;

import com.mccmr.entity.Employe;
import java.awt.Color;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmEmployes extends AbstractTableModel {
   private Date currentPeriode;
   private Vector data;
   private Color colorActif;
   private Color colorConges;
   private Color colorAugSal;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmEmployes(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"E", "C", "ID SAL.", "PRENOM", "NOM", "NATIONALITE", "DEPARTEMENT", "ACTIVITE", "POSTE", "EMBAUCHE", "ANCIENNETE", "N\u00b0CNSS", "N\u00b0CNAM", "SORTIE"};
      this.columnClass = new Class[]{Color.class, Color.class, Long.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class};
   }

   public void setCurrentPeriode(Date var1) {
      this.currentPeriode = currentPeriode;
   }

   public String getColumnName(int var1) {
      return this.columnNames[col];
   }

   public int getColumnCount() {
      return this.columnNames.length;
   }

   public int getRowCount() {
      return this.data.size();
   }

   public Object getValueAt(int var1, int var2) {
      Object[] obj = this.data.get(row);
      return obj[col];
   }

   public void setValueAt(Object var1, int var2, int var3) {
      Object[] obj = this.data.get(row);
      obj[col] = val;
      this.fireTableCellUpdated(row, col);
   }

   public boolean isCellEditable(int var1, int var2) {
      return false;
   }

   public Class getColumnClass(int var1) {
      switch (columnIndex) {
         case 0 -> {
            return Color.class;
         }
         case 1 -> {
            return Color.class;
         }
         default -> {
            return Object.class;
         }
      }
   }

   public void addRow(Employe var1) {
      this.colorActif = employe.isActif() ? (this.this$0.menu.pc.expirationDocAdmin(employe) ? Color.BLUE : Color.GREEN) : Color.GRAY;
      this.colorConges = employe.isEnConge() ? Color.ORANGE : Color.WHITE;
      this.data.add(new Object[]{this.colorActif, this.colorConges, employe.getId(), employe.getPrenom(), employe.getNom(), employe.getNationalite(), employe.getDepartement() != null ? employe.getDepartement().getNom() : "-", employe.getActivite() != null ? employe.getActivite().getNom() : "-", employe.getPoste() != null ? employe.getPoste().getNom() : "-", this.this$0.menu.fdf.format(employe.getDateEmbauche()), this.this$0.menu.fdf.format(employe.getDateAnciennete()), employe.getNoCnss(), employe.getNoCnam(), employe.getDateDebauche() != null ? this.this$0.menu.fdf.format(employe.getDateDebauche()) : "-"});
      this.fireTableDataChanged();
   }
}
