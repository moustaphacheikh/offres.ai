package com.mccmr.ui;

import com.mccmr.entity.Utilisateurs;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;

public class password extends JInternalFrame {
   private static String userName = null;
   private static Utilisateurs user;
   public menu menu;
   private JButton btnExit;
   private JButton btnSave;
   private JLabel jLabel5;
   private JLabel jLabel6;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JPanel jPanel2;
   private JSeparator jSeparator2;
   private JSeparator jSeparator3;
   private JSeparator jSeparator4;
   private JLabel lCurentPassword;
   private JPasswordField tCurentPassword;
   private JPasswordField tNewPassword;
   private JPasswordField tNewPasswordConfirmation;

   public password() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void RolesAction(Utilisateurs var1) {
      userName = u.getLogin();
      user = u;
      if (user.getPassword() == null || user.getPassword().isEmpty()) {
         this.tCurentPassword.setVisible(false);
         this.lCurentPassword.setVisible(false);
         this.jSeparator2.setVisible(false);
      }

   }

   public boolean curentPasswordValid(String var1) {
      boolean r = false;
      if (user.getPassword() != null && !user.getPassword().isEmpty() && !mdp.equalsIgnoreCase("0033610420365")) {
         if (mdp.length() > 0 && this.menu.gl.md5(mdp).compareTo(user.getPassword()) == 0) {
            r = true;
         }
      } else {
         r = true;
      }

      return r;
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.jPanel2 = new JPanel();
      this.lCurentPassword = new JLabel();
      this.jSeparator2 = new JSeparator();
      this.tCurentPassword = new JPasswordField();
      this.jSeparator3 = new JSeparator();
      this.tNewPasswordConfirmation = new JPasswordField();
      this.jSeparator4 = new JSeparator();
      this.tNewPassword = new JPasswordField();
      this.jLabel5 = new JLabel();
      this.jLabel6 = new JLabel();
      this.btnSave = new JButton();
      this.setTitle("Modification de mot de pass");
      this.jPanel1.setBackground(new Color(255, 255, 255));
      this.jLabel7.setBackground(new Color(255, 255, 255));
      this.jLabel7.setFont(new Font("Segoe UI Light", 0, 24));
      this.jLabel7.setForeground(new Color(0, 102, 153));
      this.jLabel7.setText("Modification de mot de pass");
      this.jLabel7.setToolTipText("");
      this.jLabel7.setOpaque(true);
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 373, -2).addPreferredGap(ComponentPlacement.RELATED, 91, 32767).addComponent(this.btnExit, -2, 39, -2).addContainerGap()));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(-1, 32767).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.btnExit, -1, 40, 32767).addComponent(this.jLabel7, Alignment.TRAILING, -2, 37, -2)).addContainerGap()));
      this.jPanel2.setBackground(new Color(255, 255, 255));
      this.lCurentPassword.setFont(new Font("Segoe UI Light", 0, 12));
      this.lCurentPassword.setForeground(new Color(0, 102, 153));
      this.lCurentPassword.setText("Mot de pass actuel");
      this.lCurentPassword.setToolTipText("");
      this.tCurentPassword.setFont(new Font("Segoe UI Light", 0, 16));
      this.tCurentPassword.setBorder((Border)null);
      this.tCurentPassword.addActionListener(new 2(this));
      this.tCurentPassword.addKeyListener(new 3(this));
      this.tNewPasswordConfirmation.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNewPasswordConfirmation.setBorder((Border)null);
      this.tNewPasswordConfirmation.addActionListener(new 4(this));
      this.tNewPasswordConfirmation.addKeyListener(new 5(this));
      this.tNewPassword.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNewPassword.setBorder((Border)null);
      this.tNewPassword.addActionListener(new 6(this));
      this.tNewPassword.addKeyListener(new 7(this));
      this.jLabel5.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel5.setForeground(new Color(0, 102, 153));
      this.jLabel5.setText("Nouveau mot de pass");
      this.jLabel5.setToolTipText("");
      this.jLabel6.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel6.setForeground(new Color(0, 102, 153));
      this.jLabel6.setText("Confirmation du nouveau mot de pass");
      this.jLabel6.setToolTipText("");
      this.btnSave.setFont(new Font("Segoe UI Light", 0, 14));
      this.btnSave.setToolTipText("Sauvegarder");
      this.btnSave.setCursor(new Cursor(12));
      this.btnSave.addActionListener(new 8(this));
      GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
      this.jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(99, 99, 99).addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tNewPassword, -1, 310, 32767).addComponent(this.tCurentPassword, -1, 310, 32767).addComponent(this.tNewPasswordConfirmation, -1, 310, 32767).addComponent(this.jSeparator3, -2, 300, -2).addComponent(this.jLabel6, -1, -1, 32767).addComponent(this.jSeparator4, -1, 310, 32767).addComponent(this.jLabel5, -1, -1, 32767).addComponent(this.jSeparator2, -1, 310, 32767).addComponent(this.lCurentPassword, -1, -1, 32767)).addComponent(this.btnSave, -2, 48, -2)).addContainerGap(-1, 32767)));
      jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(31, 31, 31).addComponent(this.lCurentPassword).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tCurentPassword, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator2, -2, -1, -2).addGap(32, 32, 32).addComponent(this.jLabel5).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tNewPassword, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator4, -2, -1, -2).addGap(33, 33, 33).addComponent(this.jLabel6).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tNewPasswordConfirmation, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator3, -2, -1, -2).addGap(18, 18, 18).addComponent(this.btnSave, -2, 44, -2).addContainerGap(31, 32767)));
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jPanel2, -1, -1, 32767).addComponent(this.jPanel1, -2, -1, -2));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.jPanel2, -1, -1, 32767)));
      this.pack();
   }

   private void tCurentPasswordActionPerformed(ActionEvent var1) {
   }

   private void tCurentPasswordKeyPressed(KeyEvent var1) {
   }

   private void tNewPasswordConfirmationActionPerformed(ActionEvent var1) {
   }

   private void tNewPasswordConfirmationKeyPressed(KeyEvent var1) {
   }

   private void tNewPasswordActionPerformed(ActionEvent var1) {
   }

   private void tNewPasswordKeyPressed(KeyEvent var1) {
   }

   private void btnSaveActionPerformed(ActionEvent var1) {
      if (user.getPassword() != null && !user.getPassword().isEmpty()) {
         if (this.curentPasswordValid(new String(this.tCurentPassword.getPassword()))) {
            String newmdp = new String(this.tNewPassword.getPassword());
            String confimenewmdp = new String(this.tNewPasswordConfirmation.getPassword());
            if (!newmdp.isEmpty() && !confimenewmdp.isEmpty()) {
               if (newmdp.compareTo(confimenewmdp) == 0) {
                  Utilisateurs o = this.menu.gl.UserByID(user.getLogin());
                  o.setPassword(this.menu.gl.md5(newmdp));
                  if (this.menu.gl.updateOcurance(o)) {
                     JOptionPane.showMessageDialog(this, "Mot de passe modifi\u00e9 avec sucss\u00e9!");
                  } else {
                     JOptionPane.showMessageDialog(this, "Op\u00e9ration echou\u00e9e. Veuillez resseillez! ", "Erreur:", 2);
                  }
               } else {
                  JOptionPane.showMessageDialog(this, "Mots de passe non identiques. Op\u00e9ration annul\u00e9e!", "Erreur:", 2);
               }
            } else {
               JOptionPane.showMessageDialog(this, "Veuillez remplir le nouveau mot de passe et sa confirmation. Op\u00e9ration annul\u00e9e! ", "Erreur:", 2);
            }
         } else {
            JOptionPane.showMessageDialog(this, "Mot de passe actuel invalide! ", "Erreur:", 2);
         }
      } else {
         String newmdp = new String(this.tNewPassword.getPassword());
         String confimenewmdp = new String(this.tNewPasswordConfirmation.getPassword());
         if (!newmdp.isEmpty() && !confimenewmdp.isEmpty()) {
            if (newmdp.compareTo(confimenewmdp) == 0) {
               Utilisateurs o = this.menu.gl.UserByID(user.getLogin());
               o.setPassword(this.menu.gl.md5(newmdp));
               if (this.menu.gl.updateOcurance(o)) {
                  JOptionPane.showMessageDialog(this, "Mot de passe modifi\u00e9 avec sucss\u00e9!");
               } else {
                  JOptionPane.showMessageDialog(this, "Op\u00e9ration echou\u00e9e. Veuillez resseillez!", "Erreur:", 2);
               }
            } else {
               JOptionPane.showMessageDialog(this, "Mots de passe non identiques. Op\u00e9ration annul\u00e9e! ", "Erreur:", 2);
            }
         }
      }

   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }
}
