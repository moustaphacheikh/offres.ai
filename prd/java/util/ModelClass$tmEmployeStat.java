package com.mccmr.util;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmEmployeStat extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmEmployeStat(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"", "ID", "NOM ET PRENOM", "EMBAUCHE", "SORTIE", "MONTANT"};
      this.columnClass = new Class[]{Boolean.class, Integer.class, String.class, String.class, String.class, Double.class};
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
      return this.columnClass[arg0];
   }

   public void addRow(Integer var1, String var2, String var3, String var4, Double var5) {
      this.data.add(new Object[]{false, id, nomEmploye, embauche, sortie, montant});
      this.fireTableDataChanged();
   }
}
