package com.mccmr.util;

import com.mccmr.entity.Rubriquepaie;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmVariableImport extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmVariableImport(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"ID SAL", "NOM ET PRENOM", "ID RUB", "INTITULE RUB.", "MOTIF", "FIXE", "BASE", "NOMBRE", "MONTANT"};
      this.columnClass = new Class[]{Long.class, String.class, Long.class, String.class, Long.class, Boolean.class, Double.class, Double.class, Double.class};
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

   public void addRow(Rubriquepaie var1) {
      this.data.add(new Object[]{o.getEmploye().getId(), o.getEmploye().getNom(), o.getRubrique().getId(), o.getRubrique().getLibelle(), o.getMotif().getId(), o.isFixe(), o.getBase(), o.getNombre(), o.getMontant()});
      this.fireTableDataChanged();
   }
}
