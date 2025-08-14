package com.mccmr.util;

import com.mccmr.entity.Weekot;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmHSWeekly extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmHSWeekly(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"DU", "AU", "HS 115%", "HS 140%", "HS 150%", "HS 200%", "PP", "PE"};
      this.columnClass = new Class[]{String.class, String.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class};
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

   public void addRow(Weekot var1) {
      this.data.add(new Object[]{(new SimpleDateFormat("dd/MM/yyyy")).format(rs.getBeginweek()), (new SimpleDateFormat("dd/MM/yyyy")).format(rs.getEndweek()), rs.getOt115(), rs.getOt140(), rs.getOt150(), rs.getOt200(), rs.getNbPrimePanier(), rs.getNbPrimeEloignement()});
      this.fireTableDataChanged();
   }
}
