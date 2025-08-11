package com.mccmr.ui;

import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.util.ModelClass;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class bulletin$11 extends Thread {
   // $FF: synthetic field
   final bulletin this$0;

   bulletin$11(final bulletin var1) {
      this.this$0 = this$0;
   }

   public void run() {
      if (this.this$0.menu.gl.netIsAvailable()) {
         this.this$0.progressBar1.setMaximum(this.this$0.menu.gl.selectedLinesSize(this.this$0.listTable));
         int valuePB = 0;
         this.this$0.progressBar1.setValue(valuePB);
         this.this$0.menu.viewMessage(this.this$0.msgLabel, "", false);
         String username = this.this$0.menu.paramsGen.getMailUser();
         String password = this.this$0.menu.paramsGen.getMailPassword();
         Properties props = new Properties();
         props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.starttls.enable", this.this$0.menu.paramsGen.isMailSmtpTLSEnabled() ? "true" : "false");
         props.put("mail.smtp.host", this.this$0.menu.paramsGen.getMailSmtpHost());
         props.put("mail.smtp.port", this.this$0.menu.paramsGen.getMailSmtpPort());
         Session session = Session.getInstance(props, new bulletin.11.1(this, username, password));

         try {
            Motif motif = (Motif)this.this$0.tMotif.getSelectedItem();
            Date periode = (Date)this.this$0.tPeriode.getSelectedItem();
            String var10002 = motif.getNom();
            File periodeDir = new File("repport/Paie_" + var10002 + "_" + this.this$0.menu.filePeriodeDF.format(periode));
            if (!periodeDir.exists()) {
               periodeDir.mkdir();
            }

            for(int i = 0; i < this.this$0.listTable.getRowCount(); ++i) {
               if ((Boolean)((ModelClass.tmBulletinPaie)this.this$0.listTable.getModel()).getValueAt(i, 0)) {
                  int idEmploye = ((Number)((ModelClass.tmBulletinPaie)this.this$0.listTable.getModel()).getValueAt(i, 1)).intValue();
                  Paie paie = (Paie)((List)this.this$0.dlPaie.stream().filter((var4x) -> var4x.getEmploye().getId() == idEmploye && var4x.getMotif().getId() == motif.getId() && this.this$0.menu.df.format(var4x.getPeriode()).equalsIgnoreCase(this.this$0.menu.df.format(periode))).collect(Collectors.toList())).get(0);
                  if (paie != null && paie.getEmploye().getEmail() != null && paie.getEmploye().getEmail().length() > 4) {
                     try {
                        this.this$0.menu.gl.exportPDFReport(this.this$0.menu.paramsGen.getLicenceKey() != null && this.this$0.menu.paramsGen.getLicenceKey().equalsIgnoreCase("EX05L-P08WH-ZP20B-VPJGA") ? "bulletinPaie_imrop" : "bulletinPaie", this.this$0.setFichePaieParams(paie), "M" + paie.getEmploye().getId(), periode, motif.getNom(), (File)null, false);
                        String var10000 = (new File("")).getAbsolutePath();
                        String filename = var10000 + "/" + String.valueOf(periodeDir) + "/BP_" + motif.getNom() + "_" + this.this$0.menu.filePeriodeDF.format(periode) + "_M" + paie.getEmploye().getId() + ".pdf";
                        File f = new File(filename);
                        if (f.exists()) {
                           Message message = new MimeMessage(session);
                           message.setFrom(new InternetAddress(this.this$0.menu.paramsGen.getMailUser(), "ELIYA-Paie"));
                           message.setRecipients(RecipientType.TO, InternetAddress.parse(paie.getEmploye().getEmail()));
                           String var10001 = motif.getNom();
                           message.setSubject("Bulletin de Paie - " + var10001 + " " + this.this$0.menu.filePeriodeDF.format(periode) + " M-" + paie.getEmploye().getId());
                           BodyPart messageBodyPart1 = new MimeBodyPart();
                           var10001 = paie.getEmploye().getSexe().equalsIgnoreCase("M") ? "Mr. " : "Mme/Mlle. ";
                           messageBodyPart1.setText("Bonjour " + var10001 + paie.getEmploye().getPrenom() + " " + paie.getEmploye().getNom() + ", \r\nVous avez en pi\u00e8ce jointe votre bulletin de paie: " + motif.getNom() + " " + this.this$0.menu.filePeriodeDF.format(periode).toUpperCase() + ".\r\nCordialement.\r\n\r\nService RH\r\n" + this.this$0.menu.paramsGen.getNomEntreprise());
                           MimeBodyPart messageBodyPart2 = new MimeBodyPart();
                           DataSource source = new FileDataSource(filename);
                           messageBodyPart2.setDataHandler(new DataHandler(source));
                           var10001 = motif.getNom();
                           messageBodyPart2.setFileName("BP_" + var10001 + "_" + this.this$0.menu.filePeriodeDF.format(periode) + "_M" + paie.getEmploye().getId() + ".pdf");
                           Multipart multipart = new MimeMultipart();
                           multipart.addBodyPart(messageBodyPart1);
                           multipart.addBodyPart(messageBodyPart2);
                           message.setContent(multipart);
                           Transport.send(message);
                        }

                        ++valuePB;
                        this.this$0.progressBar1.setValue(valuePB);
                     } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                     }
                  }
               }
            }

            this.this$0.menu.viewMessage(this.this$0.msgLabel, "Op\u00e9ration termin\u00e9e avec succ\u00e9es", false);
         } catch (Exception ex) {
            this.this$0.menu.viewMessage(this.this$0.msgLabel, ex.toString(), true);
            ex.printStackTrace();
         }
      } else {
         this.this$0.menu.showErrMsg(this.this$0.menu, "Pas connection internet!");
      }

   }
}
