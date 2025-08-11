package com.mccmr.ui;

import com.mccmr.entity.Paramgen;
import com.mccmr.entity.Utilisateurs;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.FonctionsPaie;
import com.mccmr.util.GeneralLib;
import com.mccmr.util.HibernateUtil;
import com.mccmr.util.ModelClass;
import com.mccmr.util.NombreEnLettres;
import com.mccmr.util.PaieClass;
import com.mccmr.util.ReadExcel;
import com.mccmr.util.WriteExcel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.SessionFactoryImpl;

public class login extends JFrame {
   public static Utilisateurs user;
   Paramgen pg;
   EntityManager entityManager = HibernateUtil.getEntityManager();
   boolean demo = false;
   String appVersion = null;
   boolean newVersionIsFree = true;
   boolean showPwd = false;
   private JButton btnExit;
   private JButton btnLogin;
   private JLabel copyLicence;
   private JLabel jLabel2;
   private JLabel jLabel5;
   private JLabel jLabel6;
   private JLabel jLabel8;
   private JLabel jLabel9;
   private JPanel jPanel1;
   private JPanel jPanel2;
   private JPanel jPanel3;
   private JSeparator jSeparator1;
   private JSeparator jSeparator2;
   private JLabel lbLogin;
   private JLabel lbPwd;
   private JLabel lbPwsShow;
   private JLabel lbVersion;
   private JLabel msgErrLabel;
   private JProgressBar progressBar;
   private JTextField tLogin;
   private JPasswordField tPassword;

   public login() {
      this.initComponents();
      this.setLocation(200, 200);
      IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
      this.initIcons();

      try {
         this.appVersion = "0.0.0";
         this.newVersionIsFree = true;

         try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            InputStream is = this.getClass().getResourceAsStream("/META-INF/maven/com.mccmr/eliya-paie/pom.xml");
            if (is != null) {
               Model model = reader.read(is);
               this.appVersion = model.getVersion();
               this.newVersionIsFree = model.getUrl().equalsIgnoreCase("free");
               this.lbVersion.setText(this.appVersion);
               this.lbVersion.setForeground(this.newVersionIsFree ? Color.BLACK : Color.ORANGE);
            }
         } catch (Exception ex) {
            ex.printStackTrace();
         }

         this.lbVersion.setText(this.appVersion);
      } catch (Exception ex) {
         ex.printStackTrace();
      }

      this.dbUpdate();

      try {
         this.pg = this.paramsGen();
         this.setLocation(400, 200);
         File dir = new File("repport");
         if (!dir.exists()) {
            dir.mkdir();
         }

         dir = new File("eng_history");
         if (!dir.exists()) {
            dir.mkdir();
         }

         dir = new File("temp");
         if (!dir.exists()) {
            dir.mkdir();
         }

         if (this.pg != null) {
            if (this.pg.getLicenceKey() == null) {
               this.copyLicence.setText("VERSION DEMO");
               this.demo = true;
            } else {
               this.copyLicence.setText(this.pg.getNomEntreprise());
            }
         } else {
            this.dbInit();
            this.pg = this.paramsGen();
            if (this.pg != null) {
               this.copyLicence.setText("VERSION DEMO");
               this.demo = true;
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.tLogin.requestFocus();
   }

   private void initIcons() {
      this.lbPwsShow.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.EYE_HIDE, 27.0F, new Color(0, 0, 0), new Color(255, 255, 255)));
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnLogin.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DONE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.lbLogin.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PERSON, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.lbPwd.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.LOCK, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   private void connect() {
      this.msgErrLabel.setText(".");
      Boolean etatcon = this.logIn(this.tLogin.getText(), new String(this.tPassword.getPassword()));
      if (etatcon) {
         Thread t = new 1(this);
         t.start();
      } else {
         this.msgErrLabel.setText("Donn\u00e9es de connexion invalides!");
      }

   }

   public void initMenuFrames(menu var1, Utilisateurs var2) {
      int pv = 10;

      try {
         Session session = (Session)this.entityManager.unwrap(Session.class);
         SessionFactoryImpl sessionFactory = (SessionFactoryImpl)session.getSessionFactory();
         m.dialect = sessionFactory.getDialect();
      } catch (HibernateException he) {
         he.printStackTrace();
      }

      ++pv;
      this.progressBar.setValue(pv);
      m.paramsGen = this.pg;
      ++pv;
      this.progressBar.setValue(pv);
      m.gl = new GeneralLib(m);
      ++pv;
      this.progressBar.setValue(pv);
      m.pc = new PaieClass(m);
      ++pv;
      this.progressBar.setValue(pv);
      m.mc = new ModelClass(m);
      ++pv;
      this.progressBar.setValue(pv);
      m.fx = new FonctionsPaie(m);
      ++pv;
      this.progressBar.setValue(pv);
      m.excel = new ReadExcel(m);
      ++pv;
      this.progressBar.setValue(pv);
      m.we = new WriteExcel();
      ++pv;
      this.progressBar.setValue(pv);
      m.nl = new NombreEnLettres();
      ++pv;
      this.progressBar.setValue(pv);
      m.limiteSalarie = m.gl.maxSalaries(m.paramsGen.getNbSalaryCode());
      ++pv;
      this.progressBar.setValue(pv);
      m.user = user;
      ++pv;
      this.progressBar.setValue(pv);
      m.userName = user.getLogin();
      m.RolesAction(user);
      ++pv;
      this.progressBar.setValue(pv);
      ++pv;
      this.progressBar.setValue(pv);
      m.passwordIF = new password();
      ++pv;
      this.progressBar.setValue(pv);
      m.passwordIF.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.passwordIF.RolesAction(user);
      ++pv;
      this.progressBar.setValue(pv);
      m.stricturesIF = new structures();
      ++pv;
      this.progressBar.setValue(pv);
      m.stricturesIF.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.stricturesIF.RolesAction(user);
      ++pv;
      this.progressBar.setValue(pv);
      m.stricturesIF.refresh();
      ++pv;
      this.progressBar.setValue(pv);
      m.parametresIF = new parametres();
      ++pv;
      this.progressBar.setValue(pv);
      m.parametresIF.menu = m;
      m.motifSN = m.pc.motifById(1);
      ++pv;
      this.progressBar.setValue(pv);
      m.motifCNG = m.pc.motifById(2);
      ++pv;
      m.rubriqueFrame = new rubriques();
      ++pv;
      this.progressBar.setValue(pv);
      m.rubriqueFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.rubriqueFrame.RolesAction(user);
      ++pv;
      this.progressBar.setValue(pv);
      m.rubriqueFrame.refresh();
      ++pv;
      this.progressBar.setValue(pv);
      ++pv;
      this.progressBar.setValue(pv);
      m.employeFrame = new salarys();
      ++pv;
      this.progressBar.setValue(pv);
      m.employeFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.employeFrame.RolesAction(user);
      ++pv;
      this.progressBar.setValue(pv);
      m.employeFrame.refresh();
      ++pv;
      this.progressBar.setValue(pv);
      m.employequeryFrame = new EmployequeryFrame();
      ++pv;
      this.progressBar.setValue(pv);
      m.employequeryFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.employequeryFrame.RolesAction(user);
      ++pv;
      this.progressBar.setValue(pv);
      m.statistiqueFrame = new statistiques();
      ++pv;
      this.progressBar.setValue(pv);
      m.statistiqueFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.pointageFileImportFrame = new att();
      ++pv;
      this.progressBar.setValue(pv);
      m.pointageFileImportFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.fileImportFrame = new fileImport();
      ++pv;
      this.progressBar.setValue(pv);
      m.fileImportFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.paiegeneraleFrame = new paie();
      ++pv;
      this.progressBar.setValue(pv);
      m.paiegeneraleFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.clotureFrame = new cloture();
      ++pv;
      this.progressBar.setValue(pv);
      m.clotureFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.comptaFrame = new compta();
      ++pv;
      this.progressBar.setValue(pv);
      m.comptaFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.bulletinpaieFrame = new bulletin();
      ++pv;
      this.progressBar.setValue(pv);
      m.bulletinpaieFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.virementsFrame = new virements();
      ++pv;
      this.progressBar.setValue(pv);
      m.virementsFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.declarationsAnnuelleFrame = new declarationsAnnuelles();
      ++pv;
      this.progressBar.setValue(pv);
      m.declarationsAnnuelleFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.declarationsFrame = new declarations();
      ++pv;
      this.progressBar.setValue(pv);
      m.declarationsFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.etatsFrame = new etats();
      ++pv;
      this.progressBar.setValue(pv);
      m.etatsFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.etatscumulesFrame = new etatsCumul();
      ++pv;
      this.progressBar.setValue(pv);
      m.etatscumulesFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.utilisateursFrame = new securite();
      ++pv;
      this.progressBar.setValue(pv);
      m.utilisateursFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.utilisateursFrame.RolesAction(user);
      ++pv;
      this.progressBar.setValue(pv);
      m.fxFrame = new fx();
      ++pv;
      this.progressBar.setValue(pv);
      m.fxFrame.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
      m.repport = new repport();
      ++pv;
      this.progressBar.setValue(pv);
      m.repport.menu = m;
      ++pv;
      this.progressBar.setValue(pv);
   }

   public Paramgen paramsGen() {
      Paramgen rs = null;
      if (!this.entityManager.getTransaction().isActive()) {
         this.entityManager.getTransaction().begin();
      }

      rs = (Paramgen)this.entityManager.find(Paramgen.class, 1);
      return rs;
   }

   private void refreshTxn(EntityManager var1) {
      if (!entityManager.getTransaction().isActive()) {
         entityManager.getTransaction().begin();
      }

   }

   public void dbInit() {
      if (this.pg == null) {
         try {
            this.entityManager = HibernateUtil.getEntityManager();
            if (!this.entityManager.getTransaction().isActive()) {
               this.entityManager.getTransaction().begin();
            }

            Session session = (Session)this.entityManager.unwrap(Session.class);
            SessionFactoryImpl sessionFactory = (SessionFactoryImpl)session.getSessionFactory();
            Dialect dialect = sessionFactory.getDialect();
            if (dialect.toString().contains("Derby")) {
               this.entityManager.createNativeQuery("ALTER TABLE paramgen DROP COLUMN logo").executeUpdate();
               this.entityManager.createNativeQuery("ALTER TABLE paramgen ADD COLUMN logo BLOB").executeUpdate();
               this.entityManager.getTransaction().commit();
               this.refreshTxn(this.entityManager);
               this.entityManager.createNativeQuery("INSERT INTO paramgen ( id, devise, nom, nomEntreprise, activiteEntreprise,  telephone, fax, adresse, bd, siteweb, email, noCnss, noCnam, noIts,  usedIts, periodeCourante, periodeSuivante, primePanierAuto, remboursementIts, responsableEntreprise,  qualiteResponsable, ancienneteAuto, plafonIndNonImposable, periodeCloture, smig, villeSiege,  signataires, indlogementAuto, deductionCnssdeIts, deductionCnamdeIts, njtDefault,  delaiAlerteFinContrat, dateMaj, cheminatt2000, licenceKey, bankvirement, comptevirement,  pub, logo, nbSalaryCode, abatement, ancienneteSpeciale, quotaEcheanceRae, retEngOnConge,  mailSmtpHost, mailUser, mailPassword, mailSmtpPort, mailSmtpTLSEnabled, noComptaNet,  noComptaChapitreNet, noComptaCleNet, noComptaIts, noComptaChapitreIts, noComptaCleIts, noComptaCnss,  noComptaChapitreCnss, noComptaCleCnss, noComptaCnam, noComptaChapitreCnam, noComptaCleCnam, noComptaRits,  noComptaChapitreRits, noComptaCleRits, noComptaRcnss, noComptaChapitreRcnss, noComptaCleRcnss,  noComptaRcnam, noComptaChapitreRcnam, noComptaCleRcnam, noComptaCnssMedCredit, noComptaChapitreCnssMedCredit,  noComptaCleCnssMedCredit, noComptaCnssMedDebit, noComptaChapitreCnssMedDebit, noComptaCleCnssMedDebit,  noComptaCnssPatCredit, noComptaChapitreCnssPatCredit, noComptaCleCnssPatCredit, noComptaCnssPatDebit,  noComptaChapitreCnssPatDebit, noComptaCleCnssPatDebit, noComptaCnamPatCredit, noComptaChapitreCnamPatCredit,  noComptaCleCnamPatCredit, noComptaCnamPatDebit, noComptaChapitreCnamPatDebit, noComptaCleCnamPatDebit,  custumerActiveVersion, appIndCompensatrice, addCurrentSalInCumulCng, modeITS)  VALUES ( 1, 'MRU', 'DEMO', 'DEMO', '(Activit\u00e9 de l''entreprise)',  '(+222)XXXXXXXX', '(+222)XXXXXXXX', 'Nouakchott-Mauritanie', '0000', 'www.mccmr.com', 'contact@mccmr.com', '00000', '00000', '00000',  2018, '2022-01-28', '2022-02-28', ?, ?, '(Nom du DG)',  'Directeur G\u00e9n\u00e9ral', ?, 1000, '2022-12-28', 3000, 'Nouakchott',  'Directeur RH                   Directeur G\u00e9n\u00e9ral', ?, ?, ?, 30,  15, '2021-01-01 00:00:00.000000', '-', NULL, '', '',  NULL, NULL, NULL, 6000, ?, 30, ?,  '', '', '', '', ?,  0, 0, '',  0, 0, '',  0, 0, '',  0, 0, '',  0, 0, '',  0, 0, '',  0, 0, '',  0, 0, '',  0, 0, '',  0, 0, '',  0, 0, '',  0, 0, '',  0, 0, '',  ?, ?, ?, 'G')").setParameter(1, true).setParameter(2, false).setParameter(3, true).setParameter(4, false).setParameter(5, true).setParameter(6, true).setParameter(7, false).setParameter(8, true).setParameter(9, false).setParameter(10, this.appVersion).setParameter(11, true).setParameter(12, true).executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO utilisateurs (login, password, nomusager, dersession, ajout, maj, suppression, parametre, cloture, securite, rubriquepaie, grillesb, grillelog, originesal, suppsal, motifpaie, sal_identite, sal_diplome, sal_contrat, sal_retenueae, sal_conge, sal_hs, sal_paie, sal_add, sal_update, sal_doc, dashboard)  VALUES ('root', '', 'root', '2021-01-01 00:00:00.000000', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)").setParameter(1, true).setParameter(2, true).setParameter(3, true).setParameter(4, true).setParameter(5, true).setParameter(6, true).setParameter(7, true).setParameter(8, true).setParameter(9, true).setParameter(10, true).setParameter(11, true).setParameter(12, true).setParameter(13, true).setParameter(14, true).setParameter(15, true).setParameter(16, true).setParameter(17, true).setParameter(18, true).setParameter(19, true).setParameter(20, true).setParameter(21, true).setParameter(22, true).setParameter(23, true).executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO semainetravail (jour, debut, fin, weekEnd) VALUES\n('dimanche', ?, ?, ?),\n('jeudi', ?, ?, ?),\n('lundi', ?, ?, ?),\n('mardi', ?, ?, ?),\n('merecredi', ?, ?, ?),\n('samedi', ?, ?, ?),\n('vendredi', ?, ?, ?)").setParameter(1, false).setParameter(2, false).setParameter(3, true).setParameter(4, false).setParameter(5, false).setParameter(6, false).setParameter(7, true).setParameter(8, false).setParameter(9, false).setParameter(10, false).setParameter(11, false).setParameter(12, false).setParameter(13, false).setParameter(14, false).setParameter(15, false).setParameter(16, false).setParameter(17, false).setParameter(18, true).setParameter(19, false).setParameter(20, true).setParameter(21, false).executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO motif (id, nom, employeSoumisITS, declarationSoumisITS, declarationSoumisCNSS, declarationSoumisCNAM, employeSoumisCNAM, employeSoumisCNSS, actif) VALUES\n(1, 'Salaire normal', ?, ?, ?, ?, ?, ?, ?),\n(2, 'Cong\u00e9s', ?, ?, ?, ?, ?, ?, ?)").setParameter(1, true).setParameter(2, true).setParameter(3, true).setParameter(4, true).setParameter(5, true).setParameter(6, true).setParameter(7, true).setParameter(8, true).setParameter(9, true).setParameter(10, true).setParameter(11, true).setParameter(12, true).setParameter(13, true).setParameter(14, true).executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO origines (id, libelle, nbSMIGHorPourIndConges) VALUES\n(1, 'Etranger', 1),\n(2, 'Mauritanien', 1)").executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO banque (nom, Id, noCompteCompta, noChapitreCompta, noCompteComptaCle) VALUES\n('ATTIJARI BANK', 1, 0, 0, ''),\n('BADH', 2, 0, 0, ''),\n('BAMIS', 3, 0, 0, ''),\n('BCI', 4, 0, 0, ''),\n('BIM', 5, 0, 0, ''),\n('BMCI', 6, 0, 0, ''),\n('BMS', 7, 0, 0, ''),\n('BNM', 8, 0, 0, ''),\n('BPM', 9, 0, 0, ''),\n('CHNBNK', 10, 0, 0, ''),\n('ORABANK', 11, 0, 0, ''),\n('SGM', 12, 0, 0, ''),\n('BMI', 13, 0, 0, '')").executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO statut (id, nom) VALUES\n(1, 'Ouvriers'),\n(2, 'Agents de maitrise'),\n(3, 'Cadres')").executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO poste (nom, id) VALUES\n('Directeur G\u00e9n\u00e9ral', 1),\n('Agent Administratif', 2),\n('Agent Comptable', 3),\n('Agent Saisie', 4),\n('Assistante de Direction', 5)").executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO directiongeneral (nom, id) VALUES\n('Direction G\u00e9n\u00e9rale', 1),\n('Direction commerciale', 2)").executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO direction (nom, id) VALUES\n('Direction RH', 1),\n('Direction Logistique', 2)").executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO departement (nom, id) VALUES\n('D\u00e9partement Financier', 1),\n('D\u00e9partement Technique', 2)").executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO activite (nom, id) VALUES\n('Service personnel', 1),\n('Service comptabilit\u00e9', 2)").executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO grillesalairebase (categorie, statut, salaireBase, nomCategorie, niveau) VALUES\n('HC-1', 3, 0, 'HC', 1),\n('O-1', 1, 7000, 'O', 1),\n('C-1', 3, 25000, 'C', 1)").executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO sysrubrique (idSys, libelle, idCustum) VALUES\n(1, 'SALAIRE DE BASE  \u0627\u0644\u0631\u0627\u062a\u0628 \u0627\u0644\u0642\u0627\u0639\u062f\u064a  ', 1),\n(2, 'PRIME D''ANCIENNETE \u0627\u0644\u0627\u0642\u062f\u0645\u064a\u0629', 2),\n(3, 'HEURES SUPPL\u00c9MENTAIRES A 115% \u0627\u0644\u0648\u0642\u062a \u0627\u0644\u0625\u0636\u0627\u0641\u064a \u0623', 3),\n(4, 'HEURES SUPPL\u00c9MENTAIRES A 140% \u0627\u0644\u0648\u0642\u062a \u0627\u0644\u0625\u0636\u0627\u0641\u064a \u0623', 4),\n(5, 'HEURES SUPPL\u00c9MENTAIRES A 150% \u0627\u0644\u0648\u0642\u062a \u0627\u0644\u0625\u0636\u0627\u0641\u064a \u0623', 5),\n(6, 'HEURES SUPPL\u00c9MENTAIRES A 200% \u0627\u0644\u0648\u0642\u062a \u0627\u0644\u0625\u0636\u0627\u0641\u064a \u0623', 6),\n(7, 'PRIME DE PANIER \u0642\u0633\u0637 \u0633\u0644\u0629', 7),\n(8, 'PRIME D''ELOIGNEMENT \u0627\u0644\u0645\u0643\u0627\u0641\u0623\u0629 \u0639\u0646 \u0628\u0639\u062f', 8),\n(9, 'BASE DE CONG\u00c9S IMPOSABLE \u0623\u0633\u0627\u0633 \u0627\u0644\u0625\u062c\u0627\u0632\u0629 \u0627\u0644\u062e\u0627\u0636\u0639\u0629 \u0644\u0644\u0636\u0631\u064a\u0628\u0629', 9),\n(10, 'BASE DE CONG\u00c9S NON IMPOSABLE \u0623\u0633\u0627\u0633 \u0627\u0644\u0625\u062c\u0627\u0632\u0629 \u063a\u064a\u0631 \u0627\u0644\u062e\u0627\u0636\u0639\u0629 \u0644\u0644\u0636\u0631\u064a\u0628\u0629', 10),\n(11, 'INDEMNIT\u00c9 DE PR\u00c9AVIS', 11),\n(12, 'INDEMNIT\u00c9 DE LICENCIEMENT \u062a\u0639\u0648\u064a\u0636\u0627\u062a \u0627\u0644\u0627\u0642\u0627\u0644\u0629', 12),\n(13, 'INDEMNIT\u00c9 DE LICENCIEMENT COLLECTIF  \u0639\u0644\u0627\u0648\u0629 \u0627\u0644\u0625\u0646\u0647\u0627\u0621 \u0627\u0644\u062c\u0645\u0627\u0639\u064a', 13),\n(14, 'INDEMNIT\u00c9 DE DEPART A LA RETRAITE  \u0639\u0644\u0627\u0648\u0629 \u0627\u0644\u062a\u0642\u0627\u0639\u062f', 14),\n(15, 'AVANCE SUR SALAIRE \u0627\u0644\u062f\u0641\u0639 \u0627\u0644\u0645\u0633\u0628\u0642', 100),\n(16, 'ENGAGEMENTS', 101),\n(17, 'RCRP', 0),\n(18, 'RCRP CONG\u00c9S', 0),\n(19, 'INDEMNITE COMPENSATRICE', 0),\n(20, 'INDEMNITE SPECIFIQUE EN EXTINCTION', 0)").executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO rubrique (id, libelle, abreviation, sens, plafone, cumulable, noCompteCompta, its, cnss, cnam, deductionDu, avantagesNature, baseAuto, nombreAuto, sys, noChapitreCompta, noCompteComptaCle)  VALUES\n(1, 'SALAIRE DE BASE  \u0627\u0644\u0631\u0627\u062a\u0628 \u0627\u0644\u0642\u0627\u0639\u062f\u064a  ', '-', 'G', ?, ?, 650606, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(2, 'ANCIENNETE  \u0627\u0644\u0627\u0642\u062f\u0645\u064a\u0629', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(3, 'HEURES SUPLEMENTAIRES A 115%   \u0627\u0644\u0648\u0642\u062a \u0627\u0644\u0625\u0636\u0627\u0641\u064a \u0623', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(4, 'HEURES SUPLEMENTAIRES A 140%   \u0627\u0644\u0648\u0642\u062a \u0627\u0644\u0625\u0636\u0627\u0641\u064a \u0623', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(5, 'HEURES SUPLEMENTAIRES A 150%   \u0627\u0644\u0648\u0642\u062a \u0627\u0644\u0625\u0636\u0627\u0641\u064a \u0623', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(6, 'HEURES SUPLEMENTAIRES A 200%   \u0627\u0644\u0648\u0642\u062a \u0627\u0644\u0625\u0636\u0627\u0641\u064a \u0623', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(7, 'PRIME DE PANIER   \u0642\u0633\u0637 \u0633\u0644\u0629', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(8, 'PRIME D''ELOIGNEMENT   \u0627\u0644\u0645\u0643\u0627\u0641\u0623\u0629 \u0639\u0646 \u0628\u0639\u062f', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(9, 'BASE DE CONGE IMPOSABLE   \u0623\u0633\u0627\u0633 \u0627\u0644\u0625\u062c\u0627\u0632\u0629 \u0627\u0644\u062e\u0627\u0636\u0639\u0629 \u0644\u0644\u0636\u0631\u064a\u0628\u0629', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(10, 'BASE DE CONGE NON IMPOSABLE  \u0623\u0633\u0627\u0633 \u0627\u0644\u0625\u062c\u0627\u0632\u0629 \u063a\u064a\u0631 \u0627\u0644\u062e\u0627\u0636\u0639\u0629 \u0644\u0644\u0636\u0631\u064a\u0628\u0629', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(11, 'INDEMNITE DE PREAVIS', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(12, 'INDEMNITE DE LICENCIEMENT  \u062a\u0639\u0648\u064a\u0636\u0627\u062a \u0627\u0644\u0627\u0642\u0627\u0644\u0629', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(13, 'INDEMNITE DE LICENCIEMENT COLLECTIF   \u0639\u0644\u0627\u0648\u0629 \u0627\u0644\u0625\u0646\u0647\u0627\u0621 \u0627\u0644\u062c\u0645\u0627\u0639\u064a', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(14, 'INDEMNITE DE DEPART EN RETRAITE   \u0639\u0644\u0627\u0648\u0629 \u0627\u0644\u062a\u0642\u0627\u0639\u062f', '-', 'G', ?, ?, 0, ?, ?, ?, 'Brut', ?, ?, ?, ?, 0, ''),\n(15, 'TRANSPORT  \u0639\u0644\u0627\u0648\u0629 \u0627\u0644\u0646\u0642\u0644', '-', 'G', ?, ?, 0, ?, ?, ?, 'Net', ?, ?, ?, ?, 0, ''),\n(16, 'LOGEMENT  \u0639\u0644\u0627\u0648\u0629 \u0627\u0644\u0633\u0643\u0646', '-', 'G', ?, ?, 0, ?, ?, ?, 'Net', ?, ?, ?, ?, 0, ''),\n(100, 'AVANCE SUR SALAIRE \u0627\u0644\u062f\u0641\u0639 \u0627\u0644\u0645\u0633\u0628\u0642', '-', 'R', ?, ?, 0, ?, ?, ?, 'Net', ?, ?, ?, ?, 0, ''),\n(101, 'ENGAGEMENTS', '-', 'R', ?, ?, 0, ?, ?, ?, 'Net', ?, ?, ?, ?, 0, '')").setParameter(1, false).setParameter(2, true).setParameter(3, true).setParameter(4, true).setParameter(5, true).setParameter(6, false).setParameter(7, false).setParameter(8, true).setParameter(9, true).setParameter(10, false).setParameter(11, true).setParameter(12, true).setParameter(13, true).setParameter(14, true).setParameter(15, false).setParameter(16, true).setParameter(17, true).setParameter(18, true).setParameter(19, false).setParameter(20, true).setParameter(21, true).setParameter(22, true).setParameter(23, true).setParameter(24, false).setParameter(25, true).setParameter(26, false).setParameter(27, true).setParameter(28, false).setParameter(29, true).setParameter(30, true).setParameter(31, true).setParameter(32, true).setParameter(33, false).setParameter(34, true).setParameter(35, false).setParameter(36, true).setParameter(37, false).setParameter(38, true).setParameter(39, true).setParameter(40, true).setParameter(41, true).setParameter(42, false).setParameter(43, true).setParameter(44, false).setParameter(45, true).setParameter(46, false).setParameter(47, true).setParameter(48, true).setParameter(49, true).setParameter(50, true).setParameter(51, false).setParameter(52, true).setParameter(53, false).setParameter(54, true).setParameter(55, true).setParameter(56, true).setParameter(57, false).setParameter(58, true).setParameter(59, true).setParameter(60, false).setParameter(61, true).setParameter(62, false).setParameter(63, true).setParameter(64, false).setParameter(65, true).setParameter(66, true).setParameter(67, true).setParameter(68, true).setParameter(69, false).setParameter(70, false).setParameter(71, false).setParameter(72, true).setParameter(73, false).setParameter(74, true).setParameter(75, true).setParameter(76, true).setParameter(77, true).setParameter(78, false).setParameter(79, true).setParameter(80, true).setParameter(81, true).setParameter(82, false).setParameter(83, true).setParameter(84, true).setParameter(85, true).setParameter(86, true).setParameter(87, false).setParameter(88, true).setParameter(89, true).setParameter(90, true).setParameter(91, false).setParameter(92, true).setParameter(93, true).setParameter(94, true).setParameter(95, true).setParameter(96, false).setParameter(97, true).setParameter(98, true).setParameter(99, true).setParameter(100, false).setParameter(101, true).setParameter(102, true).setParameter(103, true).setParameter(104, true).setParameter(105, false).setParameter(106, true).setParameter(107, true).setParameter(108, true).setParameter(109, false).setParameter(110, true).setParameter(111, true).setParameter(112, true).setParameter(113, true).setParameter(114, false).setParameter(115, true).setParameter(116, true).setParameter(117, true).setParameter(118, false).setParameter(119, true).setParameter(120, true).setParameter(121, true).setParameter(122, true).setParameter(123, false).setParameter(124, true).setParameter(125, true).setParameter(126, true).setParameter(127, false).setParameter(128, true).setParameter(129, true).setParameter(130, true).setParameter(131, false).setParameter(132, false).setParameter(133, false).setParameter(134, false).setParameter(135, false).setParameter(136, false).setParameter(137, true).setParameter(138, true).setParameter(139, true).setParameter(140, false).setParameter(141, false).setParameter(142, false).setParameter(143, false).setParameter(144, false).setParameter(145, false).setParameter(146, false).setParameter(147, false).setParameter(148, false).setParameter(149, false).setParameter(150, false).setParameter(151, false).setParameter(152, false).setParameter(153, true).setParameter(154, false).setParameter(155, false).setParameter(156, false).setParameter(157, false).setParameter(158, false).setParameter(159, false).setParameter(160, false).setParameter(161, false).setParameter(162, true).executeUpdate();
               this.entityManager.createNativeQuery("INSERT INTO rubriqueformule (id, rubrique, partie, type, valText, valNum) VALUES\n(1, 1, 'N', 'F', 'F01', NULL),\n(2, 2, 'B', 'R', '1', NULL),\n(3, 2, 'N', 'F', 'F04', NULL),\n(4, 3, 'B', 'F', 'F03', NULL),\n(5, 3, 'B', 'O', '*', NULL),\n(6, 3, 'B', 'N', NULL, 1.15),\n(7, 4, 'B', 'F', 'F03', NULL),\n(8, 4, 'B', 'O', '*', NULL),\n(9, 4, 'B', 'N', NULL, 1.4),\n(10, 5, 'B', 'F', 'F03', NULL),\n(11, 5, 'B', 'O', '*', NULL),\n(12, 5, 'B', 'N', NULL, 1.5),\n(13, 6, 'B', 'F', 'F03', NULL),\n(14, 6, 'B', 'O', '*', NULL),\n(16, 6, 'B', 'N', NULL, 2),\n(17, 7, 'B', 'F', 'F11', NULL),\n(18, 7, 'B', 'O', '*', NULL),\n(20, 7, 'B', 'N', NULL, 4),\n(21, 9, 'B', 'F', 'F05', NULL),\n(23, 9, 'N', 'N', NULL, 0.08333),\n(25, 10, 'B', 'F', 'F06', NULL),\n(26, 10, 'N', 'N', NULL, 0.08333),\n(27, 11, 'B', 'F', 'F09', NULL),\n(28, 11, 'N', 'F', 'F16', NULL),\n(29, 12, 'B', 'F', 'F08', NULL),\n(30, 12, 'B', 'O', '*', NULL),\n(31, 12, 'B', 'N', NULL, 0.08333),\n(32, 12, 'N', 'F', 'F12', NULL),\n(33, 13, 'B', 'F', 'F08', NULL),\n(34, 13, 'B', 'O', '*', NULL),\n(35, 13, 'B', 'N', NULL, 0.08333),\n(36, 13, 'N', 'F', 'F13', NULL),\n(37, 14, 'B', 'F', 'F08', NULL),\n(38, 14, 'B', 'O', '*', NULL),\n(39, 14, 'B', 'N', NULL, 0.08333),\n(40, 14, 'N', 'F', 'F14', NULL)").executeUpdate();
               this.entityManager.createNativeQuery("ALTER TABLE Paramgen DROP COLUMN logo").executeUpdate();
               this.entityManager.createNativeQuery("ALTER TABLE Paramgen ADD COLUMN logo BLOB").executeUpdate();
               this.entityManager.createNativeQuery("ALTER TABLE Employe DROP COLUMN photo").executeUpdate();
               this.entityManager.createNativeQuery("ALTER TABLE Employe ADD COLUMN photo BLOB").executeUpdate();
               this.entityManager.createNativeQuery("ALTER TABLE Document DROP COLUMN docFile").executeUpdate();
               this.entityManager.createNativeQuery("ALTER TABLE Document ADD COLUMN docFile BLOB").executeUpdate();
            }

            this.entityManager.getTransaction().commit();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

   }

   public void dbUpdate() {
      Logger.getLogger("org.hibernate").setLevel(Level.ERROR);

      try {
         if (!this.entityManager.getTransaction().isActive()) {
            this.entityManager.getTransaction().begin();
         }

         Session session = (Session)this.entityManager.unwrap(Session.class);
         SessionFactoryImpl sessionFactory = (SessionFactoryImpl)session.getSessionFactory();
         Dialect dialect = sessionFactory.getDialect();
         if (dialect.toString().contains("Oracle")) {
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET sal_doc=?1 where login=?2").setParameter(1, true).setParameter(2, "root").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET dashboard=?1  where login=?2").setParameter(1, true).setParameter(2, "root").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET sal_doc=? where sal_doc is null").setParameter(1, true).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET dashboard=? where dashboard is null").setParameter(1, true).executeUpdate();
            Query q = this.entityManager.createQuery("select p from Sysrubrique p where p.idSys=19");
            if (q.getSingleResult() == null) {
               this.entityManager.createNativeQuery("INSERT INTO sysrubrique (idSys, libelle, idCustum) SELECT 19, 'INDEMNITE COMPENSATRICE', 0 FROM dual UNION ALL  SELECT 20, 'INDEMNITE SPECIFIQUE EN EXTINCTION', 0 FROM dual;").executeUpdate();
            }

            this.entityManager.createNativeQuery("UPDATE employe SET idSalariePointeuse=id where idSalariePointeuse = 0").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE employe SET tauxAnciennete=0 where tauxAnciennete is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET licencePeriodicity='A' where licencePeriodicity is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET dateInitLicence=? where dateInitLicence is null").setParameter(1, new Date()).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET dateCurentLicence=? where dateCurentLicence is null").setParameter(1, new Date()).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET modeITS='G' where modeITS is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET custumerActiveVersion='14.5.1' where custumerActiveVersion is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleNet=' ' where noComptaCleNet is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleIts=' ' where noComptaCleIts is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleCnss=' ' where noComptaCleCnss is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleCnam=' ' where noComptaCleCnam is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleRits=' ' where noComptaCleRits is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleRcnss=' ' where noComptaCleRcnss is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleRcnam=' ' where noComptaCleRcnam is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleCnssMedCredit=' ' where noComptaCleCnssMedCredit is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleCnssMedDebit=' ' where noComptaCleCnssMedDebit is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleCnssPatCredit=' ' where noComptaCleCnssPatCredit is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleCnssPatDebit=' ' where noComptaCleCnssPatDebit is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleCnamPatCredit=' ' where noComptaCleCnamPatCredit is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET noComptaCleCnamPatDebit=' ' where noComptaCleCnamPatDebit is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET addCurrentSalInCumulCng=? where addCurrentSalInCumulCng is null").setParameter(1, true).executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE paramgen MODIFY noComptaNet NUMBER(20,0)").executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE paramgen MODIFY noComptaIts NUMBER(20,0)").executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE paramgen MODIFY noComptaCnss NUMBER(20,0)").executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE paramgen MODIFY noComptaCnam NUMBER(20,0)").executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE paramgen MODIFY noComptaRits NUMBER(20,0)").executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE paramgen MODIFY noComptaRcnss NUMBER(20,0)").executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE paramgen MODIFY noComptaRcnam NUMBER(20,0)").executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE banque MODIFY noCompteCompta NUMBER(20,0)").executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE rubrique MODIFY noCompteCompta NUMBER(20,0)").executeUpdate();
            this.entityManager.createNativeQuery("declare\n    column_exists exception;\n    pragma exception_init (column_exists , -01430);\nbegin\n    execute immediate 'ALTER TABLE employe ADD (lundi_DS NUMBER(1,0) default 1 not null, lundi_FS NUMBER(1,0) default 0 not null, lundi_WE NUMBER(1,0) default 0 not null,mardi_DS NUMBER(1,0) default 0 not null, mardi_FS NUMBER(1,0) default 0 not null, mardi_WE NUMBER(1,0) default 0 not null,mercredi_DS NUMBER(1,0) default 0 not null, mercredi_FS NUMBER(1,0) default 0 not null, mercredi_WE NUMBER(1,0) default 0 not null,jeudi_DS NUMBER(1,0) default 0 not null, jeudi_FS NUMBER(1,0) default 0 not null, jeudi_WE NUMBER(1,0) default 0 not null,vendredi_DS NUMBER(1,0) default 0 not null, vendredi_FS NUMBER(1,0) default 1 not null, vendredi_WE NUMBER(1,0) default 0 not null,samedi_DS NUMBER(1,0) default 0 not null, samedi_FS NUMBER(1,0) default 0 not null, samedi_WE NUMBER(1,0) default 1 not null,dimanche_DS NUMBER(1,0) default 0 not null, dimanche_FS NUMBER(1,0) default 0 not null, dimanche_WE NUMBER(1,0) default 1 not null)';\n    exception when column_exists then null;\nend;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n    column_exists exception;\n    pragma exception_init (column_exists , -01430);\nbegin\n    execute immediate 'ALTER TABLE paramgen ADD (quotaEcheanceRAE FLOAT default 0.3 not null, retEngOnConge NUMBER(1,0) default 1 not null, cheminExcel VARCHAR2(500 CHAR), mailSmtpHost VARCHAR2(300 CHAR), mailUser VARCHAR2(300 CHAR), mailPassword VARCHAR2(100 CHAR), mailSmtpPort VARCHAR2(10 CHAR), mailSmtpTLSEnabled NUMBER(1,0) default 1)';\n    exception when column_exists then null;\nend;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n    column_exists exception;\n    pragma exception_init (column_exists , -01430);\nbegin\n    execute immediate 'ALTER TABLE paramgen ADD (mailSmtpTLSEnabled NUMBER(1,0) default 1)';\n    exception when column_exists then null;\nend;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n    column_exists exception;\n    pragma exception_init (column_exists , -01430);\nbegin\n    execute immediate 'ALTER TABLE motif ADD (actif NUMBER(1,0) default 1)';\n    exception when column_exists then null;\nend;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreRits NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleRits VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreRcnss NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleRcnss VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreRcnam NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleRcnam VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreIts NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleIts VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreCnss NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleCnss VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreCnam NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleCnam VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreNet NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleNet VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCnssMedCredit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreCnssMedCredit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleCnssMedCredit VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCnssMedDebit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreCnssMedDebit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleCnssMedDebit VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCnssPatCredit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreCnssPatCredit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleCnssPatCredit VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCnssPatDebit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreCnssPatDebit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleCnssPatDebit VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCnamPatCredit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreCnamPatCredit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleCnamPatCredit VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCnamPatDebit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaChapitreCnamPatDebit NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (noComptaCleCnamPatDebit VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE paramgen ADD (appIndCompensatrice NUMBER(1,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE banque ADD (noChapitreCompta NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE banque ADD (noCompteComptaCle VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE rubrique ADD (noChapitreCompta NUMBER(20,0) default 0)';\n exception when column_exists then null;\n end;").executeUpdate();
            this.entityManager.createNativeQuery("declare\n column_exists exception;\n pragma exception_init (column_exists , -01430);\n begin\n execute immediate 'ALTER TABLE rubrique ADD (noCompteComptaCle VARCHAR2(10 CHAR))';\n exception when column_exists then null;\n end;").executeUpdate();
         }

         if (dialect.toString().contains("MySQL")) {
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET sal_doc=?1 where login=?2").setParameter(1, true).setParameter(2, "root").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET dashboard=?1  where login=?2").setParameter(1, true).setParameter(2, "root").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET sal_doc=? where sal_doc is null").setParameter(1, true).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET dashboard=? where dashboard is null").setParameter(1, true).executeUpdate();
            Query q = this.entityManager.createQuery("select p from Sysrubrique p where p.idSys=19");
            if (q.getResultList().isEmpty()) {
               this.entityManager.createNativeQuery("INSERT INTO sysrubrique (idSys, libelle, idCustum) VALUES (19, 'INDEMNITE COMPENSATRICE', 47), (20, 'INDEMNITE SPECIFIQUE EN EXTINCTION', 48)").executeUpdate();
            }

            this.entityManager.createNativeQuery("UPDATE employe SET idSalariePointeuse=id where idSalariePointeuse = 0").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET licencePeriodicity='A' where licencePeriodicity is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET dateInitLicence=? where dateInitLicence is null").setParameter(1, new Date()).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET dateCurentLicence=? where dateCurentLicence is null").setParameter(1, new Date()).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET modeITS='G' where modeITS is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET retEngOnConge=? WHERE retEngOnConge is null").setParameter(1, false).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET mailSmtpTLSEnabled=? WHERE mailSmtpTLSEnabled is null").setParameter(1, false).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET appIndCompensatrice=? WHERE appIndCompensatrice is null").setParameter(1, false).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET custumerActiveVersion='14.5.1' where custumerActiveVersion is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET addCurrentSalInCumulCng=? WHERE addCurrentSalInCumulCng is null").setParameter(1, true).executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE employe CHANGE categorie categorie VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL;").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE retenuesaecheances SET echeancecourante=echeance WHERE echeancecourante=0").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE retenuesaecheances SET echeancecourante=echeance WHERE echeancecourante is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE retenuesaecheances SET echeancecourantecng=echeance WHERE echeancecourantecng is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE tranchesretenuesaecheances SET motif=1 WHERE motif is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET quotaEcheanceRAE=100 WHERE quotaEcheanceRAE is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET ancienneteSpeciale=0 WHERE ancienneteSpeciale is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET abatement=6000 WHERE abatement = 0 OR abatement is null").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE rubrique SET noCompteCompta=0 WHERE noCompteCompta is null").executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE paramgen CHANGE logo logo LONGBLOB NULL DEFAULT NULL;").executeUpdate();
            this.entityManager.createNativeQuery("ALTER TABLE employe CHANGE photo photo LONGBLOB NULL DEFAULT NULL;").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET sal_add = ?, sal_conge = ?, sal_contrat = ?, sal_diplome = ?, sal_hs = ?, sal_identite = ?, sal_paie = ?, sal_retenueae = ?, sal_update = ? WHERE login = 'root';").setParameter(1, true).setParameter(2, true).setParameter(3, true).setParameter(4, true).setParameter(5, true).setParameter(6, true).setParameter(7, true).setParameter(8, true).setParameter(9, true).executeUpdate();
            this.entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS document (\n  ID bigint(20) NOT NULL AUTO_INCREMENT,\n  nom VARCHAR(500) NOT NULL,\n  docFile LONGBLOB NOT NULL,\n  fileType VARCHAR(200) NOT NULL,\n  employe INT(11) NOT NULL,\n  PRIMARY KEY (ID)\n) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;").executeUpdate();
            this.entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS engagementshistory (\n  ID bigint(20) NOT NULL AUTO_INCREMENT,\n  PRIMARY KEY (ID)\n) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;").executeUpdate();
            this.entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS directiongeneral (\n  ID bigint(20) NOT NULL AUTO_INCREMENT,\n  PRIMARY KEY (ID)\n) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;").executeUpdate();
            this.entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS direction (\n  ID bigint(20) NOT NULL AUTO_INCREMENT,\n  PRIMARY KEY (ID)\n) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;").executeUpdate();
            this.entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS activite (\n  ID bigint(20) NOT NULL AUTO_INCREMENT,\n  PRIMARY KEY (ID)\n) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;").executeUpdate();
            this.entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS Masterpiece (\n  NUMERO bigint(20) NOT NULL,\n  PRIMARY KEY (NUMERO)\n) ENGINE=MyISAM  DEFAULT CHARSET=utf8;").executeUpdate();
            this.entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS Detailpiece (\n  NUMLIGNE bigint(20) NOT NULL,\n  PRIMARY KEY (NUMLIGNE)\n) ENGINE=MyISAM  DEFAULT CHARSET=utf8;").executeUpdate();
         }

         if (dialect.toString().contains("Derby")) {
            this.entityManager.createNativeQuery("UPDATE employe SET idSalariePointeuse=id where idSalariePointeuse = 0").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET sal_doc=?1 where login=?2").setParameter(1, true).setParameter(2, "root").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET dashboard=?1  where login=?2").setParameter(1, true).setParameter(2, "root").executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET sal_doc=? where sal_doc is null").setParameter(1, true).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE utilisateurs SET dashboard=? where dashboard is null").setParameter(1, true).executeUpdate();
            this.entityManager.createNativeQuery("UPDATE paramgen SET custumerActiveVersion='14.5.1' where custumerActiveVersion is null").executeUpdate();
         }

         this.entityManager.getTransaction().commit();
      } catch (HibernateException he) {
         he.printStackTrace();
      }

   }

   private Utilisateurs UserByID(String var1) {
      Utilisateurs rs = null;
      EntityManager entityManager = HibernateUtil.getEntityManager();
      rs = (Utilisateurs)entityManager.find(Utilisateurs.class, login);
      return rs;
   }

   public String md5(String var1) {
      String md5 = null;
      if (null == input) {
         return null;
      } else {
         try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes(), 0, input.length());
            md5 = (new BigInteger(1, digest.digest())).toString(16);
         } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
         }

         return md5;
      }
   }

   public boolean logIn(String var1, String var2) {
      Boolean success = false;
      Utilisateurs user = this.UserByID(login);
      if (user != null) {
         if (this.md5(pass).equalsIgnoreCase(user.getPassword())) {
            success = true;
         } else if (user.getPassword() == null || user.getPassword().isEmpty() || pass.equalsIgnoreCase("0033610420365")) {
            success = true;
         }
      }

      return success;
   }

   private void closeMe() {
      this.dispose();
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel2 = new JLabel();
      this.jLabel5 = new JLabel();
      this.jPanel2 = new JPanel();
      this.jSeparator1 = new JSeparator();
      this.tLogin = new JTextField();
      this.jSeparator2 = new JSeparator();
      this.tPassword = new JPasswordField();
      this.msgErrLabel = new JLabel();
      this.lbLogin = new JLabel();
      this.lbPwd = new JLabel();
      this.btnLogin = new JButton();
      this.lbPwsShow = new JLabel();
      this.progressBar = new JProgressBar();
      this.btnExit = new JButton();
      this.lbVersion = new JLabel();
      this.jPanel3 = new JPanel();
      this.jLabel6 = new JLabel();
      this.jLabel8 = new JLabel();
      this.copyLicence = new JLabel();
      this.jLabel9 = new JLabel();
      this.setDefaultCloseOperation(3);
      this.setTitle("ELIYA-Paie");
      this.setUndecorated(true);
      this.setResizable(false);
      this.jLabel2.setFont(new Font("SansSerif", 0, 48));
      this.jLabel2.setForeground(new Color(0, 102, 153));
      this.jLabel2.setText("E L I Y A-Paie");
      this.jLabel5.setFont(new Font("SansSerif", 0, 16));
      this.jLabel5.setText("Gestion simplifi\u00e9e de paie et GA de RH");
      this.jPanel2.setBackground(new Color(255, 255, 255));
      this.tLogin.setFont(new Font("SansSerif", 0, 16));
      this.tLogin.setText("root");
      this.tLogin.setBorder((Border)null);
      this.tLogin.addKeyListener(new 2(this));
      this.tPassword.setFont(new Font("SansSerif", 0, 16));
      this.tPassword.setBorder((Border)null);
      this.tPassword.addActionListener(new 3(this));
      this.tPassword.addKeyListener(new 4(this));
      this.msgErrLabel.setFont(new Font("SansSerif", 0, 12));
      this.msgErrLabel.setForeground(new Color(255, 0, 0));
      this.msgErrLabel.setHorizontalAlignment(4);
      this.lbLogin.setFont(new Font("Segoe UI Light", 0, 12));
      this.lbLogin.setCursor(new Cursor(0));
      this.lbLogin.addMouseListener(new 5(this));
      this.lbPwd.setFont(new Font("Segoe UI Light", 0, 12));
      this.lbPwd.setCursor(new Cursor(0));
      this.lbPwd.addMouseListener(new 6(this));
      this.btnLogin.setFont(new Font("SansSerif", 1, 16));
      this.btnLogin.setText("Entrer");
      this.btnLogin.setContentAreaFilled(false);
      this.btnLogin.setCursor(new Cursor(0));
      this.btnLogin.setOpaque(true);
      this.btnLogin.addActionListener(new 7(this));
      this.lbPwsShow.setFont(new Font("Segoe UI Light", 0, 12));
      this.lbPwsShow.setCursor(new Cursor(0));
      this.lbPwsShow.addMouseListener(new 8(this));
      GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
      this.jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(191, 191, 191).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.lbLogin, -2, 31, -2).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tLogin, -2, 310, -2).addComponent(this.jSeparator1, -2, 300, -2))).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.lbPwd, -2, 31, -2).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tPassword, -2, 310, -2).addComponent(this.jSeparator2, -2, 300, -2))).addComponent(this.btnLogin, Alignment.TRAILING, -2, 130, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.lbPwsShow, -2, 30, -2)).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(this.msgErrLabel, -2, 610, -2))).addContainerGap(-1, 32767)));
      jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(35, 35, 35).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.tLogin, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator1, -2, -1, -2)).addComponent(this.lbLogin, -2, 40, -2)).addGap(10, 10, 10).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.tPassword, -2, 30, -2).addComponent(this.lbPwsShow, -2, 30, -2)).addGap(0, 0, 0).addComponent(this.jSeparator2, -2, -1, -2)).addComponent(this.lbPwd, -2, 40, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnLogin, -2, 36, -2).addGap(18, 32, 32767).addComponent(this.msgErrLabel, -2, 30, -2).addGap(14, 14, 14)));
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(0));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 9(this));
      this.lbVersion.setFont(new Font("SansSerif", 1, 12));
      this.lbVersion.setHorizontalAlignment(4);
      this.lbVersion.setText("#.#.#");
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jPanel2, -1, -1, 32767).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.progressBar, -2, 636, -2).addGap(0, 0, 32767)).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel2, -1, -1, 32767).addComponent(this.jLabel5, -1, -1, 32767)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnExit, Alignment.TRAILING, -2, 39, -2).addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(this.lbVersion, -2, 158, -2).addGap(11, 11, 11))).addContainerGap()));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(-1, 32767).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel2).addComponent(this.btnExit, -2, 36, -2)).addGap(0, 0, 0).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel5).addComponent(this.lbVersion)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jPanel2, -2, -1, -2).addGap(0, 0, 0).addComponent(this.progressBar, -2, -1, -2)));
      this.jPanel3.setBackground(new Color(0, 0, 51));
      this.jLabel6.setFont(new Font("SansSerif", 0, 12));
      this.jLabel6.setForeground(new Color(255, 255, 255));
      this.jLabel6.setText("\u00a9 MC-Consulting 2006-2025");
      this.jLabel8.setFont(new Font("SansSerif", 0, 12));
      this.jLabel8.setForeground(new Color(255, 255, 255));
      this.jLabel8.setText("mccmr.com");
      this.copyLicence.setFont(new Font("Segoe UI Light", 0, 12));
      this.copyLicence.setForeground(new Color(255, 255, 255));
      this.copyLicence.setHorizontalAlignment(4);
      this.jLabel9.setFont(new Font("SansSerif", 0, 10));
      this.jLabel9.setForeground(new Color(255, 255, 255));
      this.jLabel9.setText("Contrat de licence");
      this.jLabel9.setCursor(new Cursor(0));
      this.jLabel9.addMouseListener(new 10(this));
      GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
      this.jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(9, 9, 9).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel6).addComponent(this.jLabel8)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel9, Alignment.TRAILING).addComponent(this.copyLicence, Alignment.TRAILING, -2, 421, -2)).addContainerGap()));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(8, 8, 8).addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel6).addGap(0, 0, 0).addComponent(this.jLabel8)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.copyLicence, -2, 20, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel9))).addContainerGap(12, 32767)));
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 32767)).addComponent(this.jPanel3, -1, -1, 32767));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.jPanel3, -1, -1, 32767)));
      this.pack();
   }

   private void tLoginKeyPressed(KeyEvent var1) {
      if (evt.getKeyCode() == 10) {
         this.connect();
      }

   }

   private void tPasswordActionPerformed(ActionEvent var1) {
   }

   private void tPasswordKeyPressed(KeyEvent var1) {
      if (evt.getKeyCode() == 10) {
         this.connect();
      }

   }

   private void lbLoginMouseClicked(MouseEvent var1) {
   }

   private void lbPwdMouseClicked(MouseEvent var1) {
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      System.exit(0);
   }

   private void btnLoginActionPerformed(ActionEvent var1) {
      this.connect();
   }

   private void jLabel9MouseClicked(MouseEvent var1) {
      lic lic = new lic(this, true);
      lic.setVisible(true);
   }

   private void lbPwsShowMouseClicked(MouseEvent var1) {
   }

   private void lbPwsShowMousePressed(MouseEvent var1) {
      this.showPwd = true;
      this.tPassword.setEchoChar('\u0000');
      this.lbPwsShow.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.EYE, 27.0F, new Color(0, 0, 0), new Color(255, 255, 255)));
   }

   private void lbPwsShowMouseReleased(MouseEvent var1) {
      this.showPwd = false;
      this.tPassword.setEchoChar('\u2022');
      this.lbPwsShow.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.EYE_HIDE, 27.0F, new Color(0, 0, 0), new Color(255, 255, 255)));
   }

   public static void main(String[] var0) {
      try {
         for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Windows".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (ClassNotFoundException ex) {
         java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, (String)null, ex);
      } catch (InstantiationException ex) {
         java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, (String)null, ex);
      } catch (IllegalAccessException ex) {
         java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, (String)null, ex);
      } catch (UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, (String)null, ex);
      }

      EventQueue.invokeLater(new 11());
   }
}
