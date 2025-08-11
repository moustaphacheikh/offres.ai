package com.mccmr.util;

import com.mccmr.entity.Utilisateurs;
import java.awt.Color;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmUsers extends AbstractTableModel {
   private Date currentPeriode;
   private Vector data;
   private Color colorActif;
   private Color colorConges;
   private Color colorAugSal;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmUsers(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"LOGIN", "NOM D'UTILISATEUR"};
      this.columnClass = new Class[]{String.class, String.class};
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

   public void addRow(Utilisateurs var1) {
      this.data.add(new Object[]{u.getLogin(), u.getNomusager()});
      this.fireTableDataChanged();
   }
}
