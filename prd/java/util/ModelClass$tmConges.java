package com.mccmr.util;

import com.mccmr.entity.Conges;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmConges extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmConges(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"PERIODE", "DEPART", "REPRISE", "REPRISE EFF.", "NOTE", "#"};
      this.columnClass = new Class[]{String.class, String.class, String.class, String.class, String.class, Conges.class};
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

   public void addRow(Conges var1) {
      this.data.add(new Object[]{this.this$0.menu.spf.format(rs.getPeriode()), this.this$0.menu.fdf.format(rs.getDepart()), this.this$0.menu.fdf.format(rs.getReprise()), this.this$0.menu.fdf.format(rs.getRepriseeff()), rs.getNote(), rs});
      this.fireTableDataChanged();
   }
}
