package com.mccmr.util;

import com.mccmr.entity.Rubriquepaie;
import java.awt.Color;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class ModelClass$tmRubriquePaie extends AbstractTableModel {
   private Date currentPeriode;
   private Vector data;
   private String[] columnNames;
   private Class[] columnClass;
   // $FF: synthetic field
   final ModelClass this$0;

   public ModelClass$tmRubriquePaie(final ModelClass var1) {
      this.this$0 = this$0;
      this.data = new Vector();
      this.columnNames = new String[]{"ID RUB", "LIBELLE", "BASE", "NOMBRE", "MONTANT", "FIXE", "MOTIF", ">", "#"};
      this.columnClass = new Class[]{Integer.class, String.class, Double.class, Double.class, Double.class, Boolean.class, String.class, Color.class, Rubriquepaie.class};
   }

   public void setCurrentPeriode(Date var1) {
      this.currentPeriode = currentPeriode;
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

   public void addRow(Rubriquepaie var1) {
      Vector var10000 = this.data;
      Object[] var10001 = new Object[]{rs.getRubrique().getId(), null, null, null, null, null, null, null, null};
      String var10004 = rs.getRubrique().getLibelle();
      var10001[1] = "  " + var10004 + "    [" + rs.getRubrique().getSens() + (rs.getRubrique().getSens().equalsIgnoreCase("R") ? "/" + rs.getRubrique().getDeductionDu() : " //" + (rs.getRubrique().isIts() ? "its/" : (rs.getRubrique().isPlafone() ? "plaf./" : "")) + (rs.getRubrique().isCnss() ? "cnss/" : "") + (rs.getRubrique().isCnam() ? "cnam" : "")) + "]";
      var10001[2] = rs.getBase();
      var10001[3] = rs.getNombre();
      var10001[4] = rs.getMontant();
      var10001[5] = rs.isFixe();
      var10001[6] = rs.getMotif().getNom();
      var10001[7] = this.this$0.menu.pc.augmentationRubPaie(rs, this.currentPeriode) > (double)0.0F ? Color.RED : Color.WHITE;
      var10001[8] = rs;
      var10000.add(var10001);
      this.fireTableDataChanged();
   }
}
