package com.mccmr.ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class lic extends JDialog {
   public static final int RET_CANCEL = 0;
   public static final int RET_OK = 1;
   private JButton cancelButton;
   private JScrollPane jScrollPane1;
   private JTextArea jTextArea1;
   private JButton okButton;
   private int returnStatus = 0;

   public lic(Frame var1, boolean var2) {
      super(parent, modal);
      this.initComponents();
      String cancelName = "cancel";
      InputMap inputMap = this.getRootPane().getInputMap(1);
      inputMap.put(KeyStroke.getKeyStroke(27, 0), cancelName);
      ActionMap actionMap = this.getRootPane().getActionMap();
      actionMap.put(cancelName, new 1(this));
   }

   public int getReturnStatus() {
      return this.returnStatus;
   }

   private void initComponents() {
      this.okButton = new JButton();
      this.cancelButton = new JButton();
      this.jScrollPane1 = new JScrollPane();
      this.jTextArea1 = new JTextArea();
      this.addWindowListener(new 2(this));
      this.okButton.setText("OK");
      this.okButton.addActionListener(new 3(this));
      this.cancelButton.setText("Cancel");
      this.cancelButton.addActionListener(new 4(this));
      this.jTextArea1.setEditable(false);
      this.jTextArea1.setColumns(20);
      this.jTextArea1.setFont(new Font("SansSerif", 0, 12));
      this.jTextArea1.setLineWrap(true);
      this.jTextArea1.setRows(5);
      this.jTextArea1.setText("\n\n\nCONTRAT DE LICENCE D \u0301UTILISATION DE LOGICIEL\n\nENTRE LES SOUSSIGN\u00c9S :\nMC-CONSULTING\nRC: 57662, NIF: 11106312\nD\u2019une part,\n\nEt\n\nNON DE LA RAISON SOCIALE MENSIONNEE SUR LA FENETRE DE CONNEXION\nCi-dessous nomm\u00e9 \u00ab L\u2019Acqu\u00e9reur \u00bb\nD\u2019autre part,\n\n\nCOMPTE TENU que L\u2019Acqu\u00e9reur souhaite utiliser le Logiciel dans les conditions \u00e9tablies au pr\u00e9sent Contrat ;\n\nIl a \u00e9t\u00e9 convenu ce qui suit :\n\nDISPOSITION 1 : Objet du contrat\nMC-CONSULTING c\u00e8de \u00e0 l'Acqu\u00e9reur le droit d\u2019utilisation du logiciel de gestion de paie nomm\u00e9 ELIYA-Paie.\nOn entend par \u00ab logiciel \u00bb, la solution informatique de gestion en mode licence, les mises \u00e0 jour, les extensions de logiciel et les services correspondants.\n\n\nDISPOSITION 2 : Licence du logiciel\n\n2.1 Licence\nLe Fournisseur de licence accorde \u00e0 l\u2019Acqu\u00e9reur une licence non exclusive et non transf\u00e9rable pour :\n\n(1) \tutiliser le Logiciel, exclusivement pour des op\u00e9rations internes \u00e0 l\u2019emplacement et dans l\u2019environnement Class\u00e9, et\n\n(2) \tcopier le Logiciel uniquement dans le but de cr\u00e9er des fichiers ou de produire des copies de sauvegarde, \u00e0 condition de reproduire dans toutes les copies, tous les titres, les marques commerciales et copyrights, la propri\u00e9t\u00e9 intellectuelle, ainsi que les droits patrimoniaux et restreints, toutes les copies \u00e9tant r\u00e9gies par les termes de ce Contrat.\n\n       2.2 Distribution\n L\u2019Acqu\u00e9reur de la licence s\u2019interdit :\n\n(1)\tde mettre \u00e0 disposition de tiers et de distribuer le Logiciel, tant de fa\u00e7on partielle que totale, que ce soit par cession, sous-licence ou par toute autre m\u00e9thode ;\n\n(2) \tde copier, adapter, faire de l\u2019ing\u00e9nierie inverse, d\u00e9compiler, d\u00e9sassembler ou modifier\nle Logiciel, tant de fa\u00e7on partielle que totale ; ou\n\n(3) \td\u2019utiliser le Logiciel pour op\u00e9rer dans un environnement de centres d\u2019affaires, de sous-\ntraitance ou d\u2019usage partag\u00e9 de ressources, ni de permettre l\u2019acc\u00e8s de tiers au Logiciel.\n\n\nDISPOSITION 3 : Obligations de MC-CONSULTING\nMC-CONSULTING s\u2019oblige vis \u00e0 vis l\u2019Acqu\u00e9reur au respect d\u2019une obligation d\u2019assistance et de support technique consistant en une assistance en mati\u00e8re technique par la fourniture de recommandations et mises \u00e0 jour.\n\nDISPOSITION 4 : Garanties et responsabilit\u00e9s\nMC-CONSULTING d\u00e9clare \u00eatre titulaire de l\u2019ensemble des droits qui lui sont n\u00e9cessaires pour la signature du pr\u00e9sent contrat. Il garantit \u00e0 l\u2019Acqu\u00e9reur contre toute action en contrefa\u00e7on dont il serait l\u2019objet du fait de l\u2019utilisation de ses logiciels.\n\nDISPOSITION 5: Litiges \nTout litige survenant dans le cadre de l\u2019ex\u00e9cution du pr\u00e9sent contrat sera r\u00e9gl\u00e9 \u00e0 l\u2019amiable entre les parties, en cas de persistance il pourra \u00eatre soumis aux tribunaux de Nouakchott (Mauritanie) qui sont tous comp\u00e9tents.\n");
      this.jTextArea1.setWrapStyleWord(true);
      this.jScrollPane1.setViewportView(this.jTextArea1);
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(729, 32767).addComponent(this.okButton, -2, 67, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cancelButton).addContainerGap()).addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1, -2, 901, -2).addContainerGap(-1, 32767))));
      layout.linkSize(0, new Component[]{this.cancelButton, this.okButton});
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(581, 32767).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.cancelButton).addComponent(this.okButton)).addContainerGap()).addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(12, 12, 12).addComponent(this.jScrollPane1, -2, 559, -2).addContainerGap(45, 32767))));
      this.getRootPane().setDefaultButton(this.okButton);
      this.pack();
   }

   private void okButtonActionPerformed(ActionEvent var1) {
      this.doClose(1);
   }

   private void cancelButtonActionPerformed(ActionEvent var1) {
      this.doClose(0);
   }

   private void closeDialog(WindowEvent var1) {
      this.doClose(0);
   }

   private void doClose(int var1) {
      this.returnStatus = retStatus;
      this.setVisible(false);
      this.dispose();
   }

   public static void main(String[] var0) {
      try {
         for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (ClassNotFoundException ex) {
         Logger.getLogger(lic.class.getName()).log(Level.SEVERE, (String)null, ex);
      } catch (InstantiationException ex) {
         Logger.getLogger(lic.class.getName()).log(Level.SEVERE, (String)null, ex);
      } catch (IllegalAccessException ex) {
         Logger.getLogger(lic.class.getName()).log(Level.SEVERE, (String)null, ex);
      } catch (UnsupportedLookAndFeelException ex) {
         Logger.getLogger(lic.class.getName()).log(Level.SEVERE, (String)null, ex);
      }

      EventQueue.invokeLater(new 5());
   }
}
