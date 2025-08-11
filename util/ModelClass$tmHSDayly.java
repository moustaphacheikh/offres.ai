package com.mccmr.util;

import com.mccmr.entity.Jour;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmHSDayly extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmHSDayly(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"JOUR", "DATE", "HJ", "HN", "PP", "PE", "F50%", "F100%", "SE"};
      this.columnClass = new Class[]{String.class, String.class, Double.class, Double.class, Double.class, Double.class, Boolean.class, Boolean.class, Boolean.class};
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

   public void addRow(Jour var1) {
      this.data.add(new Object[]{(new SimpleDateFormat("E")).format(rs.getDateJour()), (new SimpleDateFormat("dd/MM/yyyy")).format(rs.getDateJour()), rs.getNbHeureJour(), rs.getNbHeureNuit(), rs.getNbPrimePanier(), rs.getNbPrimeEloignement(), rs.isFerie50(), rs.isFerie100(), rs.isSiteExterne()});
      this.fireTableDataChanged();
   }
}
