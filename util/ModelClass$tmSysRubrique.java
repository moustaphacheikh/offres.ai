package com.mccmr.util;

import com.mccmr.entity.Sysrubrique;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmSysRubrique extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmSysRubrique(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"ID SYS", "LIBELLE", "ID UTILISE"};
      this.columnClass = new Class[]{Integer.class, String.class, Integer.class};
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
      return columnIndex == 2;
   }

   public Class getColumnClass(int var1) {
      return this.columnClass[arg0];
   }

   public void addRow(Sysrubrique var1) {
      this.data.add(new Object[]{o.getIdSys(), o.getLibelle(), o.getIdCustum()});
      this.fireTableDataChanged();
   }
}
