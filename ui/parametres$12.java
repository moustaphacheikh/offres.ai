package com.mccmr.ui;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class parametres$12 extends Thread {
   // $FF: synthetic field
   final parametres this$0;

   parametres$12(final parametres var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.progressBar.setIndeterminate(true);
      this.this$0.jButton1.setEnabled(false);
      if (this.this$0.menu.netIsAvailable()) {
         String username = this.this$0.tMailUser.getText();
         String password = new String(this.this$0.tMailPassword.getPassword());
         Properties props = new Properties();
         props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.starttls.enable", this.this$0.cMailSmtpTLSEnabled.isSelected() ? "true" : "false");
         props.put("mail.smtp.host", this.this$0.tMailSmtpHost.getText());
         props.put("mail.smtp.port", this.this$0.tMailSmtpPort.getText());
         props.put("mail.smtp.ssl.trust", this.this$0.tMailSmtpHost.getText());
         Session session = Session.getInstance(props, new parametres.12.1(this, username, password));
         session.setDebug(true);

         try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "ELIYA-Paie"));
            message.setRecipients(RecipientType.TO, InternetAddress.parse(this.this$0.tEmailTest.getText()));
            message.setSubject("ELIYA-Paie Test");
            String msg = "Ceci est un message de <b style='color:red;'>TEST</b> de la configuration de la messagerie";
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);
            this.this$0.showMsg("Test reusie!", 1);
         } catch (Exception e) {
            this.this$0.showMsg(e.toString(), 0);
            this.this$0.jButton1.setEnabled(true);
            throw new RuntimeException(e);
         }
      } else {
         this.this$0.showMsg("Pas connection internet!", 0);
      }

      this.this$0.jButton1.setEnabled(true);
      this.this$0.progressBar.setIndeterminate(false);
   }
}
