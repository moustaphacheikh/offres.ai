package com.mccmr.ui;

import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.util.ModelClass;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

class bulletin$12 extends Thread {
   // $FF: synthetic field
   final bulletin this$0;

   bulletin$12(final bulletin var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar1.setMaximum(this.this$0.menu.gl.selectedLinesSize(this.this$0.listTable));
      int valuePB = 0;
      this.this$0.progressBar1.setValue(valuePB);
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);

      try {
         Motif motif = (Motif)this.this$0.tMotif.getSelectedItem();
         Date periode = (Date)this.this$0.tPeriode.getSelectedItem();

         for(int i = 0; i < this.this$0.listTable.getRowCount(); ++i) {
            if ((Boolean)((ModelClass.tmBulletinPaie)this.this$0.listTable.getModel()).getValueAt(i, 0)) {
               int idEmploye = ((Number)((ModelClass.tmBulletinPaie)this.this$0.listTable.getModel()).getValueAt(i, 1)).intValue();
               Paie paie = (Paie)((List)this.this$0.dlPaie.stream().filter((var4x) -> var4x.getEmploye().getId() == idEmploye && var4x.getMotif().getId() == motif.getId() && this.this$0.menu.df.format(var4x.getPeriode()).equalsIgnoreCase(this.this$0.menu.df.format(periode))).collect(Collectors.toList())).get(0);
               if (paie != null) {
                  this.this$0.menu.gl.exportPDFReport(this.this$0.menu.paramsGen.getLicenceKey() != null && this.this$0.menu.paramsGen.getLicenceKey().equalsIgnoreCase("EX05L-P08WH-ZP20B-VPJGA") ? "bulletinPaie_imrop" : "bulletinPaie", this.this$0.setFichePaieParams(paie), "M" + paie.getEmploye().getId(), periode, motif.getNom(), (File)null, true);
                  ++valuePB;
                  this.this$0.progressBar1.setValue(valuePB);
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }
}
