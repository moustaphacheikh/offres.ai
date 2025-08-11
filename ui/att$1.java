package com.mccmr.ui;

import com.mccmr.entity.Donneespointeuse;
import com.mccmr.entity.Employe;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class att$1 extends Thread {
   // $FF: synthetic field
   final att this$0;

   att$1(final att var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "Importation en cours...", false);

      try {
         List<List<String>> records = new ArrayList();
         Scanner scanner = new Scanner(new File(this.this$0.tFileName1.getText()));

         try {
            while(scanner.hasNextLine()) {
               records.add(this.this$0.getRecordFromLine(scanner.nextLine()));
            }
         } catch (Throwable var14) {
            try {
               scanner.close();
            } catch (Throwable var13) {
               var14.addSuppressed(var13);
            }

            throw var14;
         }

         scanner.close();
         long var16 = 0L;
         this.this$0.progressBar.setMinimum(1);
         this.this$0.progressBar.setMaximum(records.size());

         for(int var6 = 1; var6 < ((List)records).size(); ++var6) {
            Date dateHeure = (new SimpleDateFormat(this.this$0.tDateFormat.getSelectedItem().toString())).parse((String)((List)records.get(var6)).get(3));
            String inOut = ((String)((List)records.get(var6)).get(9)).toLowerCase().equalsIgnoreCase("checkIn") ? "I" : "O";
            if (dateHeure.after(this.this$0.menu.gl.addRetriveDays(this.this$0.tBeginDate.getDate(), -1))) {
               List var10002 = (List)records.get(var6);
               String heureJour = (new SimpleDateFormat("yyyy-MM-dd " + (String)var10002.get(4))).format(dateHeure);
               String idSalarie = (String)((List)records.get(var6)).get(1);
               Long idp = Long.parseLong(idSalarie);
               dateHeure = (new SimpleDateFormat("yyyy-MM-dd H:mm:ss")).parse(heureJour);
               if (this.this$0.cCheckInAuto.isSelected()) {
                  SimpleDateFormat var10000 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                  String var10003 = this.this$0.tCheckInAutoREF.getSelectedItem().toString();
                  Date dateRef = var10000.parse((new SimpleDateFormat("yyyy-MM-dd " + var10003 + ":00")).format(dateHeure));
                  inOut = dateHeure.before(dateRef) ? "I" : "O";
               }

               Employe employe = this.this$0.menu.pc.employeByIDP(idp);
               if (employe != null) {
                  Donneespointeuse dp = new Donneespointeuse();
                  dp.setEmploye(employe);
                  dp.setHeureJour(dateHeure);
                  dp.setVinOut(inOut);
                  dp.setImporte(true);
                  this.this$0.menu.gl.insertOcurance(dp);
                  ++var16;
               }
            }

            this.this$0.progressBar.setValue(var6);
         }

         this.this$0.menu.showMsg(this.this$0.menu.pointageFileImportFrame, var16 + " \u00e9lt(s)");
         if (this.this$0.tBeginDateView.isValid() && this.this$0.tEndDateView.isValid()) {
            this.this$0.afficherListe(this.this$0.tBeginDateView.getDate(), this.this$0.tEndDateView.getDate());
         } else {
            this.this$0.afficherListe((Date)null, (Date)null);
         }

         this.this$0.jTabbedPane1.setSelectedIndex(1);
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "Importation termin\u00e9e.", false);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }
}
