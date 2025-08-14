package com.mccmr.util;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmRappelSB extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmRappelSB(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"PERIODE", "MONTANT PAYE", "A PAYER", "RELICAT A RAPPELER"};
      this.columnClass = new Class[]{String.class, String.class, String.class, String.class};
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
      return this.columnClass[arg0];
   }

   public void addRow(String var1, String var2, String var3, String var4) {
      this.data.add(new Object[]{PERIODE, MONTANT_PAYE, A_PAYER, RELICAT_A_RAPPELER});
      this.fireTableDataChanged();
   }
}
