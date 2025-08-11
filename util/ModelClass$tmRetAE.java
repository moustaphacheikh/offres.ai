package com.mccmr.util;

import com.mccmr.entity.Retenuesaecheances;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmRetAE extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   double totalReg;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmRetAE(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"DATE ACCORD", "RETENUE", "NOTE", "CAPITALE", "ECHEANCE", "ENCOURS", "A", "S", "#"};
      this.columnClass = new Class[]{String.class, String.class, String.class, Double.class, Double.class, Double.class, Boolean.class, Boolean.class, Long.class};
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

   public void addRow(Retenuesaecheances var1) {
      this.totalReg = this.this$0.menu.pc.totalReglementRetAE(rs);
      this.data.add(new Object[]{this.this$0.menu.fdf.format(rs.getDateAccord()), rs.getRubrique().getLibelle(), rs.getNote(), rs.getCapital(), rs.getEcheance(), rs.getCapital() - this.totalReg, rs.isActive(), rs.isSolde(), rs.getId()});
      this.fireTableDataChanged();
   }
}
