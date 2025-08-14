package com.mccmr.util;

import com.mccmr.entity.Grillesalairebase;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmGrilleSB extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmGrilleSB(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"CATEGORIE", "CAT.", "NIVEAU", "STATUT", "SALAIRE DE BASE"};
      this.columnClass = new Class[]{String.class, String.class, Integer.class, String.class, Double.class};
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

   public void addRow(Grillesalairebase var1) {
      String statut = "-";
      if (rs.getStatut() != null) {
         statut = rs.getStatut().getNom();
      }

      this.data.add(new Object[]{rs.getCategorie(), rs.getNomCategorie(), rs.getNiveau(), statut, rs.getSalaireBase()});
      this.fireTableDataChanged();
   }
}
