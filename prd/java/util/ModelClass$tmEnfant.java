package com.mccmr.util;

import com.mccmr.entity.Enfants;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmEnfant extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmEnfant(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"NOM DE L'ENFANT", "GENRE", "DATE NAISS.", "PERE/MERE", "#"};
      this.columnClass = new Class[]{String.class, String.class, String.class, String.class, Long.class};
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

   public void addRow(Enfants var1) {
      this.data.add(new Object[]{o.getNomEnfant(), o.getGenre(), (new SimpleDateFormat("dd/MM/yyyy")).format(o.getDateNaissanace()), o.getMereOuPere(), o.getId()});
      this.fireTableDataChanged();
   }
}
