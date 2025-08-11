package com.mccmr.util;

import com.mccmr.entity.Paie;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmBulletinPaie extends AbstractTableModel {
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmBulletinPaie(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"AFF.", "ID EMPLOYE", "NOM ET PRENOM", "NJT", "NHS", "SAL. BRUT", "RI", "RET BRUT", "RET NET", "CNSS", "CNAM", "ITS", "R-CNSS", "R-CNAM", "R-ITS", "NET"};
      this.columnClass = new Class[]{Boolean.class, Integer.class, String.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class};
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

   public void addRow(Paie var1) {
      double mntRi = rs.getBt() - rs.getBni();
      Vector var10000 = this.data;
      Object[] var10001 = new Object[]{false, rs.getEmploye().getId(), null, null, null, null, null, null, null, null, null, null, null, null, null, null};
      String var10004 = rs.getEmploye().getPrenom();
      var10001[2] = var10004 + " " + rs.getEmploye().getNom();
      var10001[3] = rs.getNjt();
      var10001[4] = rs.getNbrHs();
      var10001[5] = rs.getBt();
      var10001[6] = mntRi;
      var10001[7] = rs.getRetenuesBrut();
      var10001[8] = rs.getRetenuesNet();
      var10001[9] = rs.getCnss();
      var10001[10] = rs.getCnam();
      var10001[11] = rs.getIts();
      var10001[12] = rs.getRcnss();
      var10001[13] = rs.getRcnam();
      var10001[14] = rs.getRits();
      var10001[15] = rs.getNet();
      var10000.add(var10001);
      this.fireTableDataChanged();
   }
}
