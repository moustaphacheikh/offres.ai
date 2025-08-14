package com.mccmr.ui;

import com.mccmr.entity.Donneespointeuse;
import com.mccmr.entity.Employe;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

class att$2 extends Thread {
   // $FF: synthetic field
   final att this$0;

   att$2(final att var1) {
      this.this$0 = this$0;
   }

   public void run() {
      Integer i = 1;
      this.this$0.menu.viewMessage(this.this$0.msgLabel, "Importation en cours...", false);

      try {
         Class.forName("com.mysql.jdbc.Driver");
         Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/imvs4200?serverTimezone=UTC", "root", "");
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery("select * from attlog");
         long rowCount = 0L;
         this.this$0.progressBar.setMinimum(1);
         this.this$0.progressBar.setMaximum(rs.getFetchSize());
         i = 1;

         while(rs.next()) {
            Date dateHeure = rs.getDate("authDateTime");
            String inOut = rs.getString("direction");
            if (dateHeure.after(this.this$0.menu.gl.addRetriveDays(this.this$0.tBeginDate.getDate(), -1))) {
               String heureJour = (new SimpleDateFormat("yyyy-MM-dd " + String.valueOf(rs.getTime("authTime")))).format(dateHeure);
               String idSalarie = rs.getString("id");
               Long idp = Long.parseLong(idSalarie);
               Date var17 = (new SimpleDateFormat("yyyy-MM-dd H:mm:ss")).parse(heureJour);
               if (this.this$0.cCheckInAuto.isSelected()) {
                  SimpleDateFormat var10000 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                  String var10003 = this.this$0.tCheckInAutoREF.getSelectedItem().toString();
                  Date dateRef = var10000.parse((new SimpleDateFormat("yyyy-MM-dd " + var10003 + ":00")).format(var17));
                  inOut = var17.before(dateRef) ? "I" : "O";
               }

               Employe employe = this.this$0.menu.pc.employeByIDP(idp);
               if (employe != null) {
                  Donneespointeuse dp = new Donneespointeuse();
                  dp.setEmploye(employe);
                  dp.setHeureJour(var17);
                  dp.setVinOut(inOut);
                  dp.setImporte(true);
                  this.this$0.menu.gl.insertOcurance(dp);
                  ++rowCount;
               }
            }

            i = i + 1;
            this.this$0.progressBar.setValue(i);
         }

         this.this$0.menu.showMsg(this.this$0.menu.pointageFileImportFrame, rowCount + " \u00e9lt(s)");
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
