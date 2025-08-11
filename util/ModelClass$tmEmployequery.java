package com.mccmr.util;

import java.awt.Color;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmEmployequery extends AbstractTableModel {
   private Color colorActif;
   private Color colorConges;
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmEmployequery(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"", "E", "C", "ID", "NOM ET PRENOM", "SEXE", "NATIONALITE", "ORIGINE", "SIT. FAMILIALE", "EMBAUCHE", "NJT/SN", "DEPARTEMENT", "POSTE", "CATEGORIE", "DET. CNSS", "DET. CNAM", "EXON. ITS", "MODE PAIE", "BANQUE", "DOMICILIE", "CONTRAT", "SORTIE", "EN DEBAUCHE"};
      this.columnClass = new Class[]{Boolean.class, Color.class, Color.class, Integer.class, String.class, String.class, String.class, String.class, String.class, String.class, Double.class, String.class, String.class, String.class, Boolean.class, Boolean.class, Boolean.class, String.class, String.class, String.class, Boolean.class, String.class, String.class, Boolean.class};
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
      return columnIndex == 0;
   }

   public Class getColumnClass(int var1) {
      switch (columnIndex) {
         case 0:
            return Boolean.class;
         case 1:
            return Color.class;
         case 2:
            return Color.class;
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 17:
         case 18:
         case 20:
         case 21:
         default:
            return Object.class;
         case 14:
            return Boolean.class;
         case 15:
            return Boolean.class;
         case 16:
            return Boolean.class;
         case 19:
            return Boolean.class;
         case 22:
            return Boolean.class;
      }
   }

   public void addRow(Boolean var1, Boolean var2, Integer var3, String var4, String var5, String var6, String var7, String var8, String var9, Double var10, String var11, String var12, String var13, Boolean var14, Boolean var15, Boolean var16, String var17, String var18, Boolean var19, String var20, String var21, Boolean var22) {
      if (actif) {
         this.colorActif = Color.GREEN;
      } else {
         this.colorActif = Color.RED;
      }

      if (enConge) {
         this.colorConges = Color.ORANGE;
      } else {
         this.colorConges = Color.WHITE;
      }

      this.data.add(new Object[]{false, this.colorActif, this.colorConges, id, nomEmploye, sexe, nationalite, origine, sitFamiliale, embauche, njt, departement, poste, categorie, detCnss, detCnam, exoIts, modePaie, banque, domicilie, typeContrat, sortie, enDebauche});
      this.fireTableDataChanged();
   }
}
