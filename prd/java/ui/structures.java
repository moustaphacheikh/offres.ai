package com.mccmr.ui;

import com.mccmr.entity.Activite;
import com.mccmr.entity.Banque;
import com.mccmr.entity.Departement;
import com.mccmr.entity.Direction;
import com.mccmr.entity.Directiongeneral;
import com.mccmr.entity.Grillelogement;
import com.mccmr.entity.Grillesalairebase;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Origines;
import com.mccmr.entity.Poste;
import com.mccmr.entity.Statut;
import com.mccmr.entity.Utilisateurs;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.ModelClass;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class structures extends JInternalFrame {
   public menu menu;
   public Directiongeneral selected_Directiongeneral = null;
   public Direction selected_Direction = null;
   public Departement selected_Departement = null;
   public Activite selected_Activite = null;
   public Poste selected_Poste = null;
   public Statut selected_Statut = null;
   public Grillesalairebase selected_Grillesalairebase = null;
   public Grillelogement selected_Grillelogement = null;
   public Origines selected_Origines = null;
   public Motif selected_Motif = null;
   public Banque selected_Banque = null;
   public Set<Directiongeneral> dataListInit_Directiongeneral;
   public Set<Direction> dataListInit_Direction;
   public Set<Departement> dataListInit_Departements;
   public Set<Activite> dataListInit_Activites;
   public Set<Poste> dataListInit_Postes;
   public Set<Statut> dataListInit_Statuts;
   public Set<Grillesalairebase> dataListInit_Grillesalairebases;
   public Set<Grillelogement> dataListInit_Grillelogements;
   public Set<Origines> dataListInit_Origines;
   public Set<Motif> dataListInit_Motifs;
   public Set<Banque> dataListInit_Banques;
   private JButton btnDelete_Activites;
   private JButton btnDelete_Banques;
   private JButton btnDelete_Departements;
   private JButton btnDelete_Direction;
   private JButton btnDelete_Directiongeneral;
   private JButton btnDelete_Grillesalairebases;
   private JButton btnDelete_Motifs;
   private JButton btnDelete_Origines;
   private JButton btnDelete_Postes;
   private JButton btnDelete_Statuts;
   private JButton btnExit;
   private JButton btnNew_Activites;
   private JButton btnNew_Banques;
   private JButton btnNew_Departements;
   private JButton btnNew_Direction;
   private JButton btnNew_Directiongeneral;
   private JButton btnNew_Grillesalairebases;
   private JButton btnNew_Motifs;
   private JButton btnNew_Origines;
   private JButton btnNew_Postes;
   private JButton btnNew_Statuts;
   private JButton btnSave_Activites;
   private JButton btnSave_Banques;
   private JButton btnSave_Departements;
   private JButton btnSave_Direction;
   private JButton btnSave_Directiongeneral;
   private JButton btnSave_Grillesalairebases;
   private JButton btnSave_Motifs;
   private JButton btnSave_Origines;
   private JButton btnSave_Postes;
   private JButton btnSave_Statuts;
   private JCheckBox cActif_Motifs;
   private JLabel jLabel10;
   private JLabel jLabel11;
   private JLabel jLabel12;
   private JLabel jLabel13;
   private JLabel jLabel14;
   private JLabel jLabel19;
   private JLabel jLabel20;
   private JLabel jLabel21;
   private JLabel jLabel22;
   private JLabel jLabel23;
   private JLabel jLabel24;
   private JLabel jLabel26;
   private JLabel jLabel28;
   private JLabel jLabel5;
   private JLabel jLabel6;
   private JLabel jLabel7;
   private JLabel jLabel8;
   private JLabel jLabel9;
   private JPanel jPanel1;
   private JPanel jPanel10;
   private JPanel jPanel11;
   private JPanel jPanel2;
   private JPanel jPanel3;
   private JPanel jPanel4;
   private JPanel jPanel5;
   private JPanel jPanel6;
   private JPanel jPanel7;
   private JPanel jPanel8;
   private JPanel jPanel9;
   private JScrollPane jScrollPane1;
   private JScrollPane jScrollPane10;
   private JScrollPane jScrollPane11;
   private JScrollPane jScrollPane12;
   private JScrollPane jScrollPane4;
   private JScrollPane jScrollPane5;
   private JScrollPane jScrollPane6;
   private JScrollPane jScrollPane7;
   private JScrollPane jScrollPane8;
   private JScrollPane jScrollPane9;
   private JSeparator jSeparator1;
   private JSeparator jSeparator10;
   private JSeparator jSeparator14;
   private JSeparator jSeparator16;
   private JSeparator jSeparator17;
   private JSeparator jSeparator18;
   private JSeparator jSeparator19;
   private JSeparator jSeparator2;
   private JSeparator jSeparator20;
   private JSeparator jSeparator21;
   private JSeparator jSeparator22;
   private JSeparator jSeparator3;
   private JSeparator jSeparator4;
   private JSeparator jSeparator5;
   private JSeparator jSeparator6;
   private JSeparator jSeparator7;
   private JSeparator jSeparator8;
   private JSeparator jSeparator9;
   private JLabel lblMsg;
   private JTable listTable_Activites;
   private JTable listTable_Banques;
   private JTable listTable_Departements;
   private JTable listTable_Direction;
   private JTable listTable_Directiongeneral;
   private JTable listTable_Grillesalairebases;
   private JTable listTable_Motifs;
   private JTable listTable_Origines;
   private JTable listTable_Postes;
   private JTable listTable_Statuts;
   private JPanel pnlActivites;
   private JPanel pnlActivites_Btn;
   private JPanel pnlActivites_Edit;
   private JPanel pnlActivites_List;
   private JPanel pnlBanques;
   private JPanel pnlBanques_Btn;
   private JPanel pnlBanques_Edit;
   private JPanel pnlBanques_List;
   private JTabbedPane pnlBody;
   private JPanel pnlDepartements;
   private JPanel pnlDepartements_Btn;
   private JPanel pnlDepartements_Edit;
   private JPanel pnlDepartements_List;
   private JPanel pnlDirection;
   private JPanel pnlDirectionGen;
   private JPanel pnlDirectionGen_Btn;
   private JPanel pnlDirectionGen_Edit;
   private JPanel pnlDirectionGen_List;
   private JPanel pnlDirection_Btn;
   private JPanel pnlDirection_Edit;
   private JPanel pnlDirection_List;
   private JPanel pnlFooter;
   private JPanel pnlGrillesalairebases;
   private JPanel pnlGrillesalairebases_Btn;
   private JPanel pnlGrillesalairebases_Edit;
   private JPanel pnlGrillesalairebases_List;
   private JPanel pnlMotifs;
   private JPanel pnlMotifs_Btn;
   private JPanel pnlMotifs_Edit;
   private JPanel pnlMotifs_List;
   private JPanel pnlOrigines;
   private JPanel pnlOrigines_Btn;
   private JPanel pnlOrigines_Edit;
   private JPanel pnlOrigines_List;
   private JPanel pnlPostes;
   private JPanel pnlPostes_Btn;
   private JPanel pnlPostes_Edit;
   private JPanel pnlPostes_List;
   private JPanel pnlStatuts;
   private JPanel pnlStatuts_Btn;
   private JPanel pnlStatuts_Edit;
   private JPanel pnlStatuts_List;
   private JTextField tCategorie_Grillesalairebases;
   private JCheckBox tDeclarationSoumisCnam_Motifs;
   private JCheckBox tDeclarationSoumisCnss_Motifs;
   private JCheckBox tDeclarationSoumisIts_Motifs;
   private JCheckBox tEmployeSoumisCnam_Motifs;
   private JCheckBox tEmployeSoumisCnss_Motifs;
   private JCheckBox tEmployeSoumisIts_Motifs;
   private JTextField tLibelle_Origines;
   private JFormattedTextField tNbSmighorPourIndConges_Origines;
   private JFormattedTextField tNiveau_Grillesalairebases;
   private JFormattedTextField tNoChapitreCompta_Banques;
   private JTextField tNoCompteComptaCle_Banques;
   private JFormattedTextField tNoCompteCompta_Banques;
   private JTextField tNomCategorie_Grillesalairebases;
   private JTextField tNom_Activites;
   private JTextField tNom_Banques;
   private JTextField tNom_Departemet;
   private JTextField tNom_Direction;
   private JTextField tNom_Directiongeneral;
   private JTextField tNom_Motifs;
   private JTextField tNom_Postes;
   private JTextField tNom_Statuts;
   private JFormattedTextField tSalaireBase_Grillesalairebases;
   private JComboBox<Object> tStatut_Grillesalairebases;

   public structures() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnDelete_Activites.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnDelete_Banques.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnDelete_Departements.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnDelete_Direction.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnDelete_Directiongeneral.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnDelete_Grillesalairebases.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnDelete_Motifs.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnDelete_Origines.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnDelete_Postes.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnDelete_Statuts.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DELETE, 27.0F, new Color(204, 0, 0), new Color(255, 255, 255)));
      this.btnNew_Activites.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNew_Banques.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNew_Departements.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNew_Direction.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNew_Directiongeneral.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNew_Grillesalairebases.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNew_Motifs.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNew_Origines.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNew_Postes.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnNew_Statuts.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Activites.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Activites.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Banques.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Departements.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Direction.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Directiongeneral.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Grillesalairebases.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Motifs.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Origines.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Postes.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSave_Statuts.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void RolesAction(Utilisateurs var1) {
      this.btnNew_Departements.setVisible(role.isAjout());
      this.btnSave_Departements.setVisible(role.isMaj());
      this.btnDelete_Departements.setVisible(role.isSuppression());
      this.pnlGrillesalairebases.setVisible(role.isGrillesb());
      this.pnlStatuts.setVisible(role.isGrillesb());
      this.pnlOrigines.setVisible(role.isOriginesal());
      this.pnlMotifs.setVisible(role.isMotifpaie());
   }

   public void refresh() {
      this.dataListUpdate();
      this.afficherListe_Directiongeneral();
      this.afficherListe_Direction();
      this.afficherListe_Departements();
      this.afficherListe_Activites();
      this.afficherListe_Postes();
      this.afficherListe_Statuts();
      this.afficherListe_Grillesalairebases();
      this.afficherListe_Origines();
      this.afficherListe_Motifs();
      this.afficherListe_Banques();
   }

   public void dataListUpdate() {
      this.dataListUpdate_Directiongeneral();
      this.dataListUpdate_Direction();
      this.dataListUpdate_Departements();
      this.dataListUpdate_Activites();
      this.dataListUpdate_Postes();
      this.dataListUpdate_Statuts();
      this.dataListUpdate_Grillesalairebases();
      this.dataListUpdate_Grillelogements();
      this.dataListUpdate_Origines();
      this.dataListUpdate_Motifs();
      this.dataListUpdate_Banques();
   }

   public void dataListUpdate_Directiongeneral() {
      Query q = this.menu.entityManager.createQuery("Select p from Directiongeneral p");
      q.setMaxResults(1000000);
      this.dataListInit_Directiongeneral = new HashSet(q.getResultList());
   }

   public void dataListUpdate_Direction() {
      Query q = this.menu.entityManager.createQuery("Select p from Direction p");
      q.setMaxResults(1000000);
      this.dataListInit_Direction = new HashSet(q.getResultList());
   }

   public void dataListUpdate_Departements() {
      Query q = this.menu.entityManager.createQuery("Select p from Departement p");
      q.setMaxResults(1000000);
      this.dataListInit_Departements = new HashSet(q.getResultList());
   }

   public void dataListUpdate_Activites() {
      Query q = this.menu.entityManager.createQuery("Select p from Activite p");
      q.setMaxResults(1000000);
      this.dataListInit_Activites = new HashSet(q.getResultList());
   }

   public void dataListUpdate_Postes() {
      Query q = this.menu.entityManager.createQuery("Select p from Poste p");
      q.setMaxResults(1000000);
      this.dataListInit_Postes = new HashSet(q.getResultList());
   }

   public void dataListUpdate_Statuts() {
      Query q = this.menu.entityManager.createQuery("Select p from Statut p");
      q.setMaxResults(1000000);
      this.dataListInit_Statuts = new HashSet(q.getResultList());
   }

   public void dataListUpdate_Grillesalairebases() {
      Query q = this.menu.entityManager.createQuery("Select p from Grillesalairebase p");
      q.setMaxResults(1000000);
      this.dataListInit_Grillesalairebases = new HashSet(q.getResultList());
   }

   public void dataListUpdate_Grillelogements() {
      Query q = this.menu.entityManager.createQuery("Select p from Grillelogement p");
      q.setMaxResults(1000000);
      this.dataListInit_Grillelogements = new HashSet(q.getResultList());
   }

   public void dataListUpdate_Origines() {
      Query q = this.menu.entityManager.createQuery("Select p from Origines p");
      q.setMaxResults(1000000);
      this.dataListInit_Origines = new HashSet(q.getResultList());
   }

   public void dataListUpdate_Motifs() {
      Query q = this.menu.entityManager.createQuery("Select p from Motif p");
      q.setMaxResults(1000000);
      this.dataListInit_Motifs = new HashSet(q.getResultList());
   }

   public void dataListUpdate_Banques() {
      Query q = this.menu.entityManager.createQuery("Select p from Banque p");
      q.setMaxResults(1000000);
      this.dataListInit_Banques = new HashSet(q.getResultList());
   }

   public void afficherListe_Directiongeneral() {
      this.listTable_Directiongeneral.removeAll();
      JTable var10000 = this.listTable_Directiongeneral;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmDirectiongeneral(var10003));
      List<Directiongeneral> dl = new ArrayList(this.dataListInit_Directiongeneral);

      for(Directiongeneral o : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
         ((ModelClass.tmDirectiongeneral)this.listTable_Directiongeneral.getModel()).addRow(o);
      }

      this.listTable_Directiongeneral.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable_Directiongeneral.getColumnModel().getColumn(0).setResizable(false);
      this.listTable_Directiongeneral.getColumnModel().getColumn(1).setPreferredWidth(1000);
      this.listTable_Directiongeneral.setRowHeight(30);
      this.listTable_Directiongeneral.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable_Directiongeneral.getModel());
      this.listTable_Directiongeneral.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   public void afficherListe_Direction() {
      this.listTable_Direction.removeAll();
      JTable var10000 = this.listTable_Direction;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmDirection(var10003));
      List<Direction> dl = new ArrayList(this.dataListInit_Direction);

      for(Direction o : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
         ((ModelClass.tmDirection)this.listTable_Direction.getModel()).addRow(o);
      }

      this.listTable_Direction.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable_Direction.getColumnModel().getColumn(0).setResizable(false);
      this.listTable_Direction.getColumnModel().getColumn(1).setPreferredWidth(1000);
      this.listTable_Direction.setRowHeight(30);
      this.listTable_Direction.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable_Direction.getModel());
      this.listTable_Direction.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   public void afficherListe_Departements() {
      this.listTable_Departements.removeAll();
      JTable var10000 = this.listTable_Departements;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmDepartement(var10003));
      List<Departement> dl = new ArrayList(this.dataListInit_Departements);

      for(Departement o : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
         ((ModelClass.tmDepartement)this.listTable_Departements.getModel()).addRow(o);
      }

      this.listTable_Departements.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable_Departements.getColumnModel().getColumn(0).setResizable(false);
      this.listTable_Departements.getColumnModel().getColumn(1).setPreferredWidth(1000);
      this.listTable_Departements.setRowHeight(30);
      this.listTable_Departements.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable_Departements.getModel());
      this.listTable_Departements.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   public void afficherListe_Activites() {
      this.listTable_Activites.removeAll();
      JTable var10000 = this.listTable_Activites;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmActivite(var10003));
      List<Activite> dl = new ArrayList(this.dataListInit_Activites);

      for(Activite o : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
         ((ModelClass.tmActivite)this.listTable_Activites.getModel()).addRow(o);
      }

      this.listTable_Activites.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable_Activites.getColumnModel().getColumn(0).setResizable(false);
      this.listTable_Activites.getColumnModel().getColumn(1).setPreferredWidth(1000);
      this.listTable_Activites.setRowHeight(30);
      this.listTable_Activites.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable_Activites.getModel());
      this.listTable_Activites.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   public void afficherListe_Postes() {
      this.listTable_Postes.removeAll();
      JTable var10000 = this.listTable_Postes;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmPoste(var10003));
      List<Poste> dl = new ArrayList(this.dataListInit_Postes);

      for(Poste o : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
         ((ModelClass.tmPoste)this.listTable_Postes.getModel()).addRow(o);
      }

      this.listTable_Postes.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable_Postes.getColumnModel().getColumn(0).setResizable(false);
      this.listTable_Postes.getColumnModel().getColumn(1).setPreferredWidth(1000);
      this.listTable_Postes.setRowHeight(30);
      this.listTable_Postes.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable_Postes.getModel());
      this.listTable_Postes.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   public void afficherListe_Statuts() {
      this.listTable_Statuts.removeAll();
      JTable var10000 = this.listTable_Statuts;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmStatut(var10003));
      this.menu.remplirCombo("Statut", this.tStatut_Grillesalairebases);
      List<Statut> dl = new ArrayList(this.dataListInit_Statuts);

      for(Statut o : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
         ((ModelClass.tmStatut)this.listTable_Statuts.getModel()).addRow(o);
      }

      this.listTable_Statuts.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable_Statuts.getColumnModel().getColumn(0).setResizable(false);
      this.listTable_Statuts.getColumnModel().getColumn(1).setPreferredWidth(1000);
      this.listTable_Statuts.setRowHeight(30);
      this.listTable_Statuts.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable_Statuts.getModel());
      this.listTable_Statuts.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   public void afficherListe_Grillesalairebases() {
      this.listTable_Grillesalairebases.removeAll();
      JTable var10000 = this.listTable_Grillesalairebases;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmGrilleSB(var10003));
      List<Grillesalairebase> dl = new ArrayList(this.dataListInit_Grillesalairebases);

      for(Grillesalairebase o : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getCategorie())).collect(Collectors.toList())) {
         ((ModelClass.tmGrilleSB)this.listTable_Grillesalairebases.getModel()).addRow(o);
      }

      this.listTable_Grillesalairebases.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable_Grillesalairebases.getColumnModel().getColumn(1).setPreferredWidth(20);
      this.listTable_Grillesalairebases.setRowHeight(30);
      this.listTable_Grillesalairebases.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable_Grillesalairebases.getModel());
      this.listTable_Grillesalairebases.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   public void afficherListe_Origines() {
      this.listTable_Origines.removeAll();
      JTable var10000 = this.listTable_Origines;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmOrigines(var10003));
      List<Origines> dl = new ArrayList(this.dataListInit_Origines);

      for(Origines o : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
         ((ModelClass.tmOrigines)this.listTable_Origines.getModel()).addRow(o);
      }

      this.listTable_Origines.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable_Origines.getColumnModel().getColumn(0).setResizable(false);
      this.listTable_Origines.getColumnModel().getColumn(1).setPreferredWidth(800);
      this.listTable_Origines.setRowHeight(30);
      this.listTable_Origines.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable_Origines.getModel());
      this.listTable_Origines.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   public void afficherListe_Motifs() {
      this.listTable_Motifs.removeAll();
      JTable var10000 = this.listTable_Motifs;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmMotif(var10003));
      List<Motif> dl = new ArrayList(this.dataListInit_Motifs);

      for(Motif o : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
         ((ModelClass.tmMotif)this.listTable_Motifs.getModel()).addRow(o);
      }

      this.listTable_Motifs.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable_Motifs.getColumnModel().getColumn(0).setResizable(false);
      this.listTable_Motifs.getColumnModel().getColumn(1).setPreferredWidth(500);
      this.listTable_Motifs.setRowHeight(30);
      this.listTable_Motifs.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable_Motifs.getModel());
      this.listTable_Motifs.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   public void afficherListe_Banques() {
      this.listTable_Banques.removeAll();
      JTable var10000 = this.listTable_Banques;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmBanque(var10003));
      List<Banque> dl = new ArrayList(this.dataListInit_Banques);

      for(Banque o : (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getId())).collect(Collectors.toList())) {
         ((ModelClass.tmBanque)this.listTable_Banques.getModel()).addRow(o);
      }

      this.listTable_Banques.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.listTable_Banques.getColumnModel().getColumn(0).setResizable(false);
      this.listTable_Banques.getColumnModel().getColumn(1).setPreferredWidth(400);
      this.listTable_Banques.getColumnModel().getColumn(2).setPreferredWidth(200);
      this.listTable_Banques.setRowHeight(30);
      this.listTable_Banques.setAutoCreateRowSorter(true);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable_Banques.getModel());
      this.listTable_Banques.setRowSorter(sorter);
      sorter.setSortsOnUpdates(true);
      ModelClass var10004 = this.menu.mc;
      Objects.requireNonNull(var10004);
      sorter.setComparator(0, new ModelClass.NumberComparator(var10004));
   }

   private String validateData_Directiongeneral() {
      String errMsg = "";
      if (this.tNom_Directiongeneral.getText().isEmpty()) {
         errMsg = "Le nom de la direction g\u00e9n\u00e9rale est obligatoire";
         this.tNom_Directiongeneral.requestFocus();
      }

      return errMsg;
   }

   private String validateData_Direction() {
      String errMsg = "";
      if (this.tNom_Direction.getText().isEmpty()) {
         errMsg = "Le nom de la direction est obligatoire";
         this.tNom_Direction.requestFocus();
      }

      return errMsg;
   }

   private String validateData_Departemets() {
      String errMsg = "";
      if (this.tNom_Departemet.getText().isEmpty()) {
         errMsg = "Le nom du d\u00e9partement est obligatoire";
         this.tNom_Departemet.requestFocus();
      }

      return errMsg;
   }

   private String validateData_Activites() {
      String errMsg = "";
      if (this.tNom_Activites.getText().isEmpty()) {
         errMsg = "Le nom de l'activit\u00e9 est obligatoire";
         this.tNom_Activites.requestFocus();
      }

      return errMsg;
   }

   private String validateData_Postes() {
      String errMsg = "";
      if (this.tNom_Postes.getText().isEmpty()) {
         errMsg = "Nom du poste est obligatoire";
         this.tNom_Postes.requestFocus();
      }

      return errMsg;
   }

   private String validateData_Statuts() {
      String errMsg = "";
      if (this.tNom_Statuts.getText().isEmpty()) {
         errMsg = "Nom du statut est obligatoire";
         this.tNom_Statuts.requestFocus();
      }

      return errMsg;
   }

   private String validateData_Grillesalairebases() {
      String errMsg = "";
      if (this.tNomCategorie_Grillesalairebases.getText().isEmpty()) {
         errMsg = "Le nom de la cat\u00e9gorie est obligatoire";
         this.tNomCategorie_Grillesalairebases.requestFocus();
      }

      if (this.tNiveau_Grillesalairebases.getText().isEmpty() || this.tNiveau_Grillesalairebases.getValue() == null) {
         errMsg = "Le niveau de la cat\u00e9gorie est obligatoire";
         this.tNiveau_Grillesalairebases.requestFocus();
      }

      if (this.tSalaireBase_Grillesalairebases.getText().isEmpty() || this.tSalaireBase_Grillesalairebases.getValue() == null) {
         errMsg = "Le salaire de base de la cat\u00e9gorie est obligatoire";
         this.tSalaireBase_Grillesalairebases.requestFocus();
      }

      return errMsg;
   }

   private String validateData_Origines() {
      String errMsg = "";
      if (this.tLibelle_Origines.getText().isEmpty()) {
         errMsg = "Le nom de l'origine est obligatoire";
         this.tLibelle_Origines.requestFocus();
      }

      if (this.tNbSmighorPourIndConges_Origines.getText().isEmpty() || this.tNbSmighorPourIndConges_Origines.getValue() == null) {
         errMsg = "Le nombre de SMIG est obligatoire";
         this.tNbSmighorPourIndConges_Origines.requestFocus();
      }

      return errMsg;
   }

   private String validateData_Motifs() {
      String errMsg = "";
      if (this.tNom_Motifs.getText().isEmpty()) {
         errMsg = "Le nom du motif est obligatoire";
         this.tNom_Motifs.requestFocus();
      }

      return errMsg;
   }

   private String validateData_Banques() {
      String errMsg = "";
      if (this.tNom_Banques.getText().isEmpty()) {
         errMsg = "Le nom de la banque est obligatoire";
         this.tNom_Banques.requestFocus();
      }

      return errMsg;
   }

   private void clearFields_Directiongeneral() {
      this.menu.viewMessage(this.lblMsg, "", false);
      this.selected_Directiongeneral = null;
      this.tNom_Directiongeneral.setText("");
      this.tNom_Directiongeneral.requestFocus();
      this.btnNew_Directiongeneral.setEnabled(true);
      this.btnSave_Directiongeneral.setEnabled(true);
      this.btnDelete_Directiongeneral.setEnabled(false);
   }

   private void clearFields_Direction() {
      this.menu.viewMessage(this.lblMsg, "", false);
      this.selected_Direction = null;
      this.tNom_Direction.setText("");
      this.tNom_Direction.requestFocus();
      this.btnNew_Direction.setEnabled(true);
      this.btnSave_Direction.setEnabled(true);
      this.btnDelete_Direction.setEnabled(false);
   }

   private void clearFields_Departemets() {
      this.menu.viewMessage(this.lblMsg, "", false);
      this.selected_Departement = null;
      this.tNom_Departemet.setText("");
      this.tNom_Departemet.requestFocus();
      this.btnNew_Departements.setEnabled(true);
      this.btnSave_Departements.setEnabled(true);
      this.btnDelete_Departements.setEnabled(false);
   }

   private void clearFields_Activites() {
      this.menu.viewMessage(this.lblMsg, "", false);
      this.selected_Activite = null;
      this.tNom_Activites.setText("");
      this.tNom_Activites.requestFocus();
      this.btnNew_Activites.setEnabled(true);
      this.btnSave_Activites.setEnabled(true);
      this.btnDelete_Activites.setEnabled(false);
   }

   private void clearFields_Postes() {
      this.menu.viewMessage(this.lblMsg, "", false);
      this.selected_Poste = null;
      this.tNom_Postes.setText("");
      this.tNom_Postes.requestFocus();
      this.btnNew_Postes.setEnabled(true);
      this.btnSave_Postes.setEnabled(true);
      this.btnDelete_Postes.setEnabled(false);
   }

   private void clearFields_Statuts() {
      this.menu.viewMessage(this.lblMsg, "", false);
      this.selected_Statut = null;
      this.tNom_Statuts.setText("");
      this.tNom_Statuts.requestFocus();
      this.btnNew_Statuts.setEnabled(true);
      this.btnSave_Statuts.setEnabled(true);
      this.btnDelete_Statuts.setEnabled(false);
   }

   private void clearFields_Grillesalairebases() {
      this.menu.viewMessage(this.lblMsg, "", false);
      this.selected_Grillesalairebase = null;
      this.tNiveau_Grillesalairebases.setValue((Object)null);
      this.tCategorie_Grillesalairebases.setText("");
      this.tSalaireBase_Grillesalairebases.setValue(0);
      this.tNiveau_Grillesalairebases.requestFocus();
      this.tNomCategorie_Grillesalairebases.setEnabled(true);
      this.tNiveau_Grillesalairebases.setEnabled(true);
      this.btnNew_Grillesalairebases.setEnabled(true);
      this.btnSave_Grillesalairebases.setEnabled(true);
      this.btnDelete_Grillesalairebases.setEnabled(false);
   }

   private void clearFields_Origines() {
      this.menu.viewMessage(this.lblMsg, "", false);
      this.selected_Origines = null;
      this.tLibelle_Origines.setText("");
      this.tLibelle_Origines.requestFocus();
      this.tNbSmighorPourIndConges_Origines.setValue(0);
      this.btnNew_Origines.setEnabled(true);
      this.btnSave_Origines.setEnabled(true);
      this.btnDelete_Origines.setEnabled(false);
   }

   private void clearFields_Motifs() {
      this.menu.viewMessage(this.lblMsg, "", false);
      this.selected_Motif = null;
      this.tNom_Motifs.setText("");
      this.tNom_Motifs.requestFocus();
      this.tEmployeSoumisCnss_Motifs.setSelected(false);
      this.tDeclarationSoumisCnss_Motifs.setSelected(false);
      this.tEmployeSoumisCnam_Motifs.setSelected(false);
      this.tDeclarationSoumisCnam_Motifs.setSelected(false);
      this.tEmployeSoumisIts_Motifs.setSelected(false);
      this.tDeclarationSoumisIts_Motifs.setSelected(false);
      this.btnNew_Motifs.setEnabled(true);
      this.btnSave_Motifs.setEnabled(true);
      this.btnDelete_Motifs.setEnabled(false);
   }

   private void clearFields_Banques() {
      this.menu.viewMessage(this.lblMsg, "", false);
      this.selected_Banque = null;
      this.tNom_Banques.setText("");
      this.tNom_Banques.requestFocus();
      this.tNoCompteCompta_Banques.setValue(0);
      this.tNoChapitreCompta_Banques.setValue(0);
      this.tNoCompteComptaCle_Banques.setText("");
      this.btnNew_Banques.setEnabled(true);
      this.btnSave_Banques.setEnabled(true);
      this.btnDelete_Banques.setEnabled(false);
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JTabbedPane();
      this.jPanel2 = new JPanel();
      this.pnlDirectionGen = new JPanel();
      this.pnlDirectionGen_Btn = new JPanel();
      this.btnDelete_Directiongeneral = new JButton();
      this.btnSave_Directiongeneral = new JButton();
      this.btnNew_Directiongeneral = new JButton();
      this.pnlDirectionGen_Edit = new JPanel();
      this.jSeparator21 = new JSeparator();
      this.tNom_Directiongeneral = new JTextField();
      this.jLabel19 = new JLabel();
      this.pnlDirectionGen_List = new JPanel();
      this.jScrollPane5 = new JScrollPane();
      this.listTable_Directiongeneral = new JTable();
      this.jPanel3 = new JPanel();
      this.pnlDirection = new JPanel();
      this.pnlDirection_Btn = new JPanel();
      this.btnDelete_Direction = new JButton();
      this.btnSave_Direction = new JButton();
      this.btnNew_Direction = new JButton();
      this.pnlDirection_Edit = new JPanel();
      this.jSeparator22 = new JSeparator();
      this.tNom_Direction = new JTextField();
      this.jLabel23 = new JLabel();
      this.pnlDirection_List = new JPanel();
      this.jScrollPane12 = new JScrollPane();
      this.listTable_Direction = new JTable();
      this.jPanel4 = new JPanel();
      this.pnlDepartements = new JPanel();
      this.pnlDepartements_Btn = new JPanel();
      this.btnDelete_Departements = new JButton();
      this.btnSave_Departements = new JButton();
      this.btnNew_Departements = new JButton();
      this.pnlDepartements_Edit = new JPanel();
      this.jSeparator1 = new JSeparator();
      this.tNom_Departemet = new JTextField();
      this.jLabel5 = new JLabel();
      this.pnlDepartements_List = new JPanel();
      this.jScrollPane4 = new JScrollPane();
      this.listTable_Departements = new JTable();
      this.jPanel5 = new JPanel();
      this.pnlActivites = new JPanel();
      this.pnlActivites_Btn = new JPanel();
      this.btnDelete_Activites = new JButton();
      this.btnSave_Activites = new JButton();
      this.btnNew_Activites = new JButton();
      this.pnlActivites_Edit = new JPanel();
      this.jSeparator2 = new JSeparator();
      this.tNom_Activites = new JTextField();
      this.jLabel6 = new JLabel();
      this.pnlActivites_List = new JPanel();
      this.jScrollPane11 = new JScrollPane();
      this.listTable_Activites = new JTable();
      this.jPanel6 = new JPanel();
      this.pnlPostes = new JPanel();
      this.pnlPostes_Btn = new JPanel();
      this.btnDelete_Postes = new JButton();
      this.btnSave_Postes = new JButton();
      this.btnNew_Postes = new JButton();
      this.pnlPostes_Edit = new JPanel();
      this.jSeparator4 = new JSeparator();
      this.tNom_Postes = new JTextField();
      this.jLabel9 = new JLabel();
      this.pnlPostes_List = new JPanel();
      this.jScrollPane1 = new JScrollPane();
      this.listTable_Postes = new JTable();
      this.jPanel7 = new JPanel();
      this.pnlStatuts = new JPanel();
      this.pnlStatuts_Btn = new JPanel();
      this.btnDelete_Statuts = new JButton();
      this.btnSave_Statuts = new JButton();
      this.btnNew_Statuts = new JButton();
      this.pnlStatuts_Edit = new JPanel();
      this.jSeparator5 = new JSeparator();
      this.tNom_Statuts = new JTextField();
      this.jLabel10 = new JLabel();
      this.pnlStatuts_List = new JPanel();
      this.jScrollPane8 = new JScrollPane();
      this.listTable_Statuts = new JTable();
      this.jPanel8 = new JPanel();
      this.pnlGrillesalairebases = new JPanel();
      this.pnlGrillesalairebases_Btn = new JPanel();
      this.btnDelete_Grillesalairebases = new JButton();
      this.btnSave_Grillesalairebases = new JButton();
      this.btnNew_Grillesalairebases = new JButton();
      this.pnlGrillesalairebases_Edit = new JPanel();
      this.jSeparator6 = new JSeparator();
      this.tNomCategorie_Grillesalairebases = new JTextField();
      this.jLabel11 = new JLabel();
      this.jLabel12 = new JLabel();
      this.jSeparator7 = new JSeparator();
      this.tNiveau_Grillesalairebases = new JFormattedTextField();
      this.tSalaireBase_Grillesalairebases = new JFormattedTextField();
      this.jSeparator8 = new JSeparator();
      this.jLabel13 = new JLabel();
      this.tCategorie_Grillesalairebases = new JTextField();
      this.jSeparator9 = new JSeparator();
      this.tStatut_Grillesalairebases = new JComboBox();
      this.jLabel14 = new JLabel();
      this.jSeparator10 = new JSeparator();
      this.pnlGrillesalairebases_List = new JPanel();
      this.jScrollPane6 = new JScrollPane();
      this.listTable_Grillesalairebases = new JTable();
      this.jPanel9 = new JPanel();
      this.pnlMotifs = new JPanel();
      this.pnlMotifs_Btn = new JPanel();
      this.btnDelete_Motifs = new JButton();
      this.btnSave_Motifs = new JButton();
      this.btnNew_Motifs = new JButton();
      this.pnlMotifs_Edit = new JPanel();
      this.jSeparator16 = new JSeparator();
      this.tNom_Motifs = new JTextField();
      this.jLabel24 = new JLabel();
      this.tEmployeSoumisCnss_Motifs = new JCheckBox();
      this.tDeclarationSoumisCnss_Motifs = new JCheckBox();
      this.tDeclarationSoumisCnam_Motifs = new JCheckBox();
      this.tEmployeSoumisCnam_Motifs = new JCheckBox();
      this.tDeclarationSoumisIts_Motifs = new JCheckBox();
      this.tEmployeSoumisIts_Motifs = new JCheckBox();
      this.cActif_Motifs = new JCheckBox();
      this.pnlMotifs_List = new JPanel();
      this.jScrollPane9 = new JScrollPane();
      this.listTable_Motifs = new JTable();
      this.jPanel10 = new JPanel();
      this.pnlOrigines = new JPanel();
      this.pnlOrigines_Btn = new JPanel();
      this.btnDelete_Origines = new JButton();
      this.btnSave_Origines = new JButton();
      this.btnNew_Origines = new JButton();
      this.pnlOrigines_Edit = new JPanel();
      this.jSeparator14 = new JSeparator();
      this.tLibelle_Origines = new JTextField();
      this.tNbSmighorPourIndConges_Origines = new JFormattedTextField();
      this.jSeparator17 = new JSeparator();
      this.jLabel21 = new JLabel();
      this.jLabel22 = new JLabel();
      this.pnlOrigines_List = new JPanel();
      this.jScrollPane7 = new JScrollPane();
      this.listTable_Origines = new JTable();
      this.jPanel11 = new JPanel();
      this.pnlBanques = new JPanel();
      this.pnlBanques_Btn = new JPanel();
      this.btnDelete_Banques = new JButton();
      this.btnSave_Banques = new JButton();
      this.btnNew_Banques = new JButton();
      this.pnlBanques_Edit = new JPanel();
      this.jSeparator3 = new JSeparator();
      this.tNom_Banques = new JTextField();
      this.jLabel8 = new JLabel();
      this.jLabel20 = new JLabel();
      this.tNoChapitreCompta_Banques = new JFormattedTextField();
      this.jSeparator19 = new JSeparator();
      this.jSeparator20 = new JSeparator();
      this.tNoCompteCompta_Banques = new JFormattedTextField();
      this.jLabel26 = new JLabel();
      this.tNoCompteComptaCle_Banques = new JTextField();
      this.jLabel28 = new JLabel();
      this.jSeparator18 = new JSeparator();
      this.pnlBanques_List = new JPanel();
      this.jScrollPane10 = new JScrollPane();
      this.listTable_Banques = new JTable();
      this.pnlFooter = new JPanel();
      this.lblMsg = new JLabel();
      this.setBackground(new Color(255, 255, 255));
      this.setBorder((Border)null);
      this.setAutoscrolls(true);
      this.setFont(new Font("Segoe UI Light", 0, 12));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(1000, 600));
      this.jPanel1.setBackground(new Color(255, 255, 255));
      this.jLabel7.setFont(new Font("Segoe UI Light", 0, 24));
      this.jLabel7.setForeground(new Color(0, 102, 153));
      this.jLabel7.setText("Stricture");
      this.jLabel7.setToolTipText("");
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addPreferredGap(ComponentPlacement.RELATED, 608, 32767).addComponent(this.btnExit, -2, 39, -2).addGap(0, 0, 0)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.pnlBody.setForeground(new Color(0, 102, 153));
      this.pnlBody.setFont(new Font("Segoe UI Light", 1, 14));
      this.jPanel2.setBackground((Color)null);
      this.jPanel2.setLayout(new AbsoluteLayout());
      this.pnlDirectionGen.setBackground(new Color(255, 255, 255));
      this.pnlDirectionGen.setOpaque(false);
      this.pnlDirectionGen_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete_Directiongeneral.setToolTipText("Supprimer");
      this.btnDelete_Directiongeneral.setContentAreaFilled(false);
      this.btnDelete_Directiongeneral.setCursor(new Cursor(12));
      this.btnDelete_Directiongeneral.setEnabled(false);
      this.btnDelete_Directiongeneral.setOpaque(true);
      this.btnDelete_Directiongeneral.addActionListener(new 2(this));
      this.btnSave_Directiongeneral.setToolTipText("Sauvegarder");
      this.btnSave_Directiongeneral.setContentAreaFilled(false);
      this.btnSave_Directiongeneral.setCursor(new Cursor(12));
      this.btnSave_Directiongeneral.setEnabled(false);
      this.btnSave_Directiongeneral.setOpaque(true);
      this.btnSave_Directiongeneral.addActionListener(new 3(this));
      this.btnNew_Directiongeneral.setToolTipText("Nouveau");
      this.btnNew_Directiongeneral.setContentAreaFilled(false);
      this.btnNew_Directiongeneral.setCursor(new Cursor(12));
      this.btnNew_Directiongeneral.setOpaque(true);
      this.btnNew_Directiongeneral.addActionListener(new 4(this));
      GroupLayout pnlDirectionGen_BtnLayout = new GroupLayout(this.pnlDirectionGen_Btn);
      this.pnlDirectionGen_Btn.setLayout(pnlDirectionGen_BtnLayout);
      pnlDirectionGen_BtnLayout.setHorizontalGroup(pnlDirectionGen_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlDirectionGen_BtnLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnNew_Directiongeneral, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave_Directiongeneral, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete_Directiongeneral, -2, 35, -2).addGap(19, 19, 19)));
      pnlDirectionGen_BtnLayout.setVerticalGroup(pnlDirectionGen_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirectionGen_BtnLayout.createSequentialGroup().addGroup(pnlDirectionGen_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete_Directiongeneral, -2, 35, -2).addComponent(this.btnSave_Directiongeneral, -2, 35, -2).addComponent(this.btnNew_Directiongeneral, -2, 35, -2)).addGap(11, 11, 11)));
      this.pnlDirectionGen_Edit.setBackground(new Color(255, 255, 255));
      this.tNom_Directiongeneral.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNom_Directiongeneral.setBorder((Border)null);
      this.tNom_Directiongeneral.addKeyListener(new 5(this));
      this.jLabel19.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel19.setForeground(new Color(0, 102, 153));
      this.jLabel19.setText("Nom");
      GroupLayout pnlDirectionGen_EditLayout = new GroupLayout(this.pnlDirectionGen_Edit);
      this.pnlDirectionGen_Edit.setLayout(pnlDirectionGen_EditLayout);
      pnlDirectionGen_EditLayout.setHorizontalGroup(pnlDirectionGen_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirectionGen_EditLayout.createSequentialGroup().addGap(26, 26, 26).addGroup(pnlDirectionGen_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator21).addComponent(this.jLabel19, -2, 310, -2).addComponent(this.tNom_Directiongeneral, -1, 412, 32767)).addContainerGap(657, 32767)));
      pnlDirectionGen_EditLayout.setVerticalGroup(pnlDirectionGen_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirectionGen_EditLayout.createSequentialGroup().addGap(4, 4, 4).addComponent(this.jLabel19).addGap(0, 0, 0).addComponent(this.tNom_Directiongeneral, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator21, -2, -1, -2).addContainerGap(12, 32767)));
      this.pnlDirectionGen_List.setBackground(new Color(255, 255, 255));
      this.listTable_Directiongeneral.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable_Directiongeneral.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable_Directiongeneral.setSelectionBackground(new Color(0, 102, 153));
      this.listTable_Directiongeneral.setShowGrid(false);
      this.listTable_Directiongeneral.addMouseListener(new 6(this));
      this.jScrollPane5.setViewportView(this.listTable_Directiongeneral);
      GroupLayout pnlDirectionGen_ListLayout = new GroupLayout(this.pnlDirectionGen_List);
      this.pnlDirectionGen_List.setLayout(pnlDirectionGen_ListLayout);
      pnlDirectionGen_ListLayout.setHorizontalGroup(pnlDirectionGen_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlDirectionGen_ListLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.jScrollPane5, -2, 1090, -2).addGap(17, 17, 17)));
      pnlDirectionGen_ListLayout.setVerticalGroup(pnlDirectionGen_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirectionGen_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane5, -2, 314, -2).addContainerGap(-1, 32767)));
      GroupLayout pnlDirectionGenLayout = new GroupLayout(this.pnlDirectionGen);
      this.pnlDirectionGen.setLayout(pnlDirectionGenLayout);
      pnlDirectionGenLayout.setHorizontalGroup(pnlDirectionGenLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirectionGenLayout.createSequentialGroup().addGroup(pnlDirectionGenLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.pnlDirectionGen_List, Alignment.LEADING, -2, 1101, 32767).addGroup(Alignment.LEADING, pnlDirectionGenLayout.createSequentialGroup().addContainerGap().addGroup(pnlDirectionGenLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.pnlDirectionGen_Btn, -1, -1, 32767).addComponent(this.pnlDirectionGen_Edit, -1, -1, 32767)))).addContainerGap(-1, 32767)));
      pnlDirectionGenLayout.setVerticalGroup(pnlDirectionGenLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirectionGenLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlDirectionGen_Edit, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlDirectionGen_Btn, -2, 40, -2).addGap(18, 18, 18).addComponent(this.pnlDirectionGen_List, -1, -1, 32767).addContainerGap()));
      this.jPanel2.add(this.pnlDirectionGen, new AbsoluteConstraints(0, 0, -1, -1));
      this.pnlBody.addTab("Direction g\u00e9n\u00e9rale", this.jPanel2);
      this.jPanel3.setBackground((Color)null);
      this.jPanel3.setLayout(new AbsoluteLayout());
      this.pnlDirection.setBackground(new Color(255, 255, 255));
      this.pnlDirection_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete_Direction.setToolTipText("Supprimer");
      this.btnDelete_Direction.setContentAreaFilled(false);
      this.btnDelete_Direction.setCursor(new Cursor(12));
      this.btnDelete_Direction.setEnabled(false);
      this.btnDelete_Direction.setOpaque(true);
      this.btnDelete_Direction.addActionListener(new 7(this));
      this.btnSave_Direction.setToolTipText("Sauvegarder");
      this.btnSave_Direction.setContentAreaFilled(false);
      this.btnSave_Direction.setCursor(new Cursor(12));
      this.btnSave_Direction.setEnabled(false);
      this.btnSave_Direction.setOpaque(true);
      this.btnSave_Direction.addActionListener(new 8(this));
      this.btnNew_Direction.setToolTipText("Nouveau");
      this.btnNew_Direction.setContentAreaFilled(false);
      this.btnNew_Direction.setCursor(new Cursor(12));
      this.btnNew_Direction.setOpaque(true);
      this.btnNew_Direction.addActionListener(new 9(this));
      GroupLayout pnlDirection_BtnLayout = new GroupLayout(this.pnlDirection_Btn);
      this.pnlDirection_Btn.setLayout(pnlDirection_BtnLayout);
      pnlDirection_BtnLayout.setHorizontalGroup(pnlDirection_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlDirection_BtnLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnNew_Direction, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave_Direction, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete_Direction, -2, 35, -2).addGap(21, 21, 21)));
      pnlDirection_BtnLayout.setVerticalGroup(pnlDirection_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirection_BtnLayout.createSequentialGroup().addGroup(pnlDirection_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete_Direction, -2, 35, -2).addComponent(this.btnSave_Direction, -2, 35, -2).addComponent(this.btnNew_Direction, -2, 35, -2)).addGap(11, 11, 11)));
      this.pnlDirection_Edit.setBackground(new Color(255, 255, 255));
      this.tNom_Direction.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNom_Direction.setBorder((Border)null);
      this.tNom_Direction.addKeyListener(new 10(this));
      this.jLabel23.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel23.setForeground(new Color(0, 102, 153));
      this.jLabel23.setText("Nom");
      GroupLayout pnlDirection_EditLayout = new GroupLayout(this.pnlDirection_Edit);
      this.pnlDirection_Edit.setLayout(pnlDirection_EditLayout);
      pnlDirection_EditLayout.setHorizontalGroup(pnlDirection_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirection_EditLayout.createSequentialGroup().addGap(26, 26, 26).addGroup(pnlDirection_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator22).addComponent(this.jLabel23, -2, 310, -2).addComponent(this.tNom_Direction, -1, 412, 32767)).addContainerGap(657, 32767)));
      pnlDirection_EditLayout.setVerticalGroup(pnlDirection_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirection_EditLayout.createSequentialGroup().addGap(4, 4, 4).addComponent(this.jLabel23).addGap(0, 0, 0).addComponent(this.tNom_Direction, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator22, -2, -1, -2).addContainerGap(12, 32767)));
      this.pnlDirection_List.setBackground(new Color(255, 255, 255));
      this.listTable_Direction.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable_Direction.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable_Direction.setSelectionBackground(new Color(0, 102, 153));
      this.listTable_Direction.setShowGrid(false);
      this.listTable_Direction.addMouseListener(new 11(this));
      this.jScrollPane12.setViewportView(this.listTable_Direction);
      GroupLayout pnlDirection_ListLayout = new GroupLayout(this.pnlDirection_List);
      this.pnlDirection_List.setLayout(pnlDirection_ListLayout);
      pnlDirection_ListLayout.setHorizontalGroup(pnlDirection_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirection_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane12, -2, 1087, -2).addContainerGap(8, 32767)));
      pnlDirection_ListLayout.setVerticalGroup(pnlDirection_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirection_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane12, -2, 318, -2).addContainerGap(-1, 32767)));
      GroupLayout pnlDirectionLayout = new GroupLayout(this.pnlDirection);
      this.pnlDirection.setLayout(pnlDirectionLayout);
      pnlDirectionLayout.setHorizontalGroup(pnlDirectionLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirectionLayout.createSequentialGroup().addGroup(pnlDirectionLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.pnlDirection_List, Alignment.LEADING, -1, -1, 32767).addGroup(Alignment.LEADING, pnlDirectionLayout.createSequentialGroup().addContainerGap().addGroup(pnlDirectionLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.pnlDirection_Edit, -1, -1, 32767).addComponent(this.pnlDirection_Btn, -1, -1, 32767)))).addContainerGap(-1, 32767)));
      pnlDirectionLayout.setVerticalGroup(pnlDirectionLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDirectionLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlDirection_Edit, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlDirection_Btn, -2, 40, -2).addGap(0, 0, 0).addComponent(this.pnlDirection_List, -2, -1, -2).addContainerGap()));
      this.jPanel3.add(this.pnlDirection, new AbsoluteConstraints(0, 0, -1, -1));
      this.pnlBody.addTab("Direction", this.jPanel3);
      this.jPanel4.setBackground((Color)null);
      this.jPanel4.setLayout(new AbsoluteLayout());
      this.pnlDepartements.setBackground(new Color(255, 255, 255));
      this.pnlDepartements_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete_Departements.setToolTipText("Supprimer");
      this.btnDelete_Departements.setContentAreaFilled(false);
      this.btnDelete_Departements.setCursor(new Cursor(12));
      this.btnDelete_Departements.setEnabled(false);
      this.btnDelete_Departements.setOpaque(true);
      this.btnDelete_Departements.addActionListener(new 12(this));
      this.btnSave_Departements.setToolTipText("Sauvegarder");
      this.btnSave_Departements.setContentAreaFilled(false);
      this.btnSave_Departements.setCursor(new Cursor(12));
      this.btnSave_Departements.setEnabled(false);
      this.btnSave_Departements.setOpaque(true);
      this.btnSave_Departements.addActionListener(new 13(this));
      this.btnNew_Departements.setToolTipText("Nouveau");
      this.btnNew_Departements.setContentAreaFilled(false);
      this.btnNew_Departements.setCursor(new Cursor(12));
      this.btnNew_Departements.setOpaque(true);
      this.btnNew_Departements.addActionListener(new 14(this));
      GroupLayout pnlDepartements_BtnLayout = new GroupLayout(this.pnlDepartements_Btn);
      this.pnlDepartements_Btn.setLayout(pnlDepartements_BtnLayout);
      pnlDepartements_BtnLayout.setHorizontalGroup(pnlDepartements_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlDepartements_BtnLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnNew_Departements, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave_Departements, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete_Departements, -2, 35, -2).addGap(25, 25, 25)));
      pnlDepartements_BtnLayout.setVerticalGroup(pnlDepartements_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDepartements_BtnLayout.createSequentialGroup().addGroup(pnlDepartements_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete_Departements, -2, 35, -2).addComponent(this.btnSave_Departements, -2, 35, -2).addComponent(this.btnNew_Departements, -2, 35, -2)).addGap(11, 11, 11)));
      this.pnlDepartements_Edit.setBackground(new Color(255, 255, 255));
      this.tNom_Departemet.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNom_Departemet.setBorder((Border)null);
      this.tNom_Departemet.addKeyListener(new 15(this));
      this.jLabel5.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel5.setForeground(new Color(0, 102, 153));
      this.jLabel5.setText("Nom");
      GroupLayout pnlDepartements_EditLayout = new GroupLayout(this.pnlDepartements_Edit);
      this.pnlDepartements_Edit.setLayout(pnlDepartements_EditLayout);
      pnlDepartements_EditLayout.setHorizontalGroup(pnlDepartements_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDepartements_EditLayout.createSequentialGroup().addGap(26, 26, 26).addGroup(pnlDepartements_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator1).addComponent(this.jLabel5, -2, 310, -2).addComponent(this.tNom_Departemet, -1, 412, 32767)).addContainerGap(646, 32767)));
      pnlDepartements_EditLayout.setVerticalGroup(pnlDepartements_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDepartements_EditLayout.createSequentialGroup().addGap(4, 4, 4).addComponent(this.jLabel5).addGap(0, 0, 0).addComponent(this.tNom_Departemet, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator1, -2, -1, -2).addContainerGap(12, 32767)));
      this.pnlDepartements_List.setBackground(new Color(255, 255, 255));
      this.listTable_Departements.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable_Departements.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable_Departements.setSelectionBackground(new Color(0, 102, 153));
      this.listTable_Departements.setShowGrid(false);
      this.listTable_Departements.addMouseListener(new 16(this));
      this.jScrollPane4.setViewportView(this.listTable_Departements);
      GroupLayout pnlDepartements_ListLayout = new GroupLayout(this.pnlDepartements_List);
      this.pnlDepartements_List.setLayout(pnlDepartements_ListLayout);
      pnlDepartements_ListLayout.setHorizontalGroup(pnlDepartements_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDepartements_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane4, -2, 1082, -2).addContainerGap(-1, 32767)));
      pnlDepartements_ListLayout.setVerticalGroup(pnlDepartements_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDepartements_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane4, -2, 314, -2).addContainerGap(-1, 32767)));
      GroupLayout pnlDepartementsLayout = new GroupLayout(this.pnlDepartements);
      this.pnlDepartements.setLayout(pnlDepartementsLayout);
      pnlDepartementsLayout.setHorizontalGroup(pnlDepartementsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDepartementsLayout.createSequentialGroup().addContainerGap().addGroup(pnlDepartementsLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.pnlDepartements_Edit, -1, -1, 32767).addComponent(this.pnlDepartements_Btn, -1, -1, 32767)).addContainerGap(17, 32767)).addGroup(pnlDepartementsLayout.createSequentialGroup().addComponent(this.pnlDepartements_List, -2, -1, -2).addGap(0, 0, 32767)));
      pnlDepartementsLayout.setVerticalGroup(pnlDepartementsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlDepartementsLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlDepartements_Edit, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlDepartements_Btn, -2, 40, -2).addGap(0, 0, 0).addComponent(this.pnlDepartements_List, -2, -1, -2).addContainerGap(-1, 32767)));
      this.jPanel4.add(this.pnlDepartements, new AbsoluteConstraints(0, 0, -1, -1));
      this.pnlBody.addTab("D\u00e9partements", this.jPanel4);
      this.jPanel5.setBackground((Color)null);
      this.jPanel5.setLayout(new AbsoluteLayout());
      this.pnlActivites.setBackground(new Color(255, 255, 255));
      this.pnlActivites_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete_Activites.setToolTipText("Supprimer");
      this.btnDelete_Activites.setContentAreaFilled(false);
      this.btnDelete_Activites.setCursor(new Cursor(12));
      this.btnDelete_Activites.setEnabled(false);
      this.btnDelete_Activites.setOpaque(true);
      this.btnDelete_Activites.addActionListener(new 17(this));
      this.btnSave_Activites.setToolTipText("Sauvegarder");
      this.btnSave_Activites.setContentAreaFilled(false);
      this.btnSave_Activites.setCursor(new Cursor(12));
      this.btnSave_Activites.setEnabled(false);
      this.btnSave_Activites.setOpaque(true);
      this.btnSave_Activites.addActionListener(new 18(this));
      this.btnNew_Activites.setToolTipText("Nouveau");
      this.btnNew_Activites.setContentAreaFilled(false);
      this.btnNew_Activites.setCursor(new Cursor(12));
      this.btnNew_Activites.setOpaque(true);
      this.btnNew_Activites.addActionListener(new 19(this));
      GroupLayout pnlActivites_BtnLayout = new GroupLayout(this.pnlActivites_Btn);
      this.pnlActivites_Btn.setLayout(pnlActivites_BtnLayout);
      pnlActivites_BtnLayout.setHorizontalGroup(pnlActivites_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlActivites_BtnLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnNew_Activites, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave_Activites, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete_Activites, -2, 35, -2).addGap(14, 14, 14)));
      pnlActivites_BtnLayout.setVerticalGroup(pnlActivites_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete_Activites, -2, 35, -2).addComponent(this.btnSave_Activites, -2, 35, -2).addComponent(this.btnNew_Activites, Alignment.TRAILING, -2, 35, -2));
      this.pnlActivites_Edit.setBackground(new Color(255, 255, 255));
      this.tNom_Activites.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNom_Activites.setBorder((Border)null);
      this.tNom_Activites.addKeyListener(new 20(this));
      this.jLabel6.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel6.setForeground(new Color(0, 102, 153));
      this.jLabel6.setText("Nom");
      GroupLayout pnlActivites_EditLayout = new GroupLayout(this.pnlActivites_Edit);
      this.pnlActivites_Edit.setLayout(pnlActivites_EditLayout);
      pnlActivites_EditLayout.setHorizontalGroup(pnlActivites_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivites_EditLayout.createSequentialGroup().addGap(26, 26, 26).addGroup(pnlActivites_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator2).addComponent(this.jLabel6, -2, 310, -2).addComponent(this.tNom_Activites, -1, 412, 32767)).addContainerGap(657, 32767)));
      pnlActivites_EditLayout.setVerticalGroup(pnlActivites_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivites_EditLayout.createSequentialGroup().addGap(4, 4, 4).addComponent(this.jLabel6).addGap(0, 0, 0).addComponent(this.tNom_Activites, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator2, -2, -1, -2).addContainerGap(12, 32767)));
      this.pnlActivites_List.setBackground(new Color(255, 255, 255));
      this.listTable_Activites.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable_Activites.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable_Activites.setSelectionBackground(new Color(0, 102, 153));
      this.listTable_Activites.setShowGrid(false);
      this.listTable_Activites.addMouseListener(new 21(this));
      this.jScrollPane11.setViewportView(this.listTable_Activites);
      GroupLayout pnlActivites_ListLayout = new GroupLayout(this.pnlActivites_List);
      this.pnlActivites_List.setLayout(pnlActivites_ListLayout);
      pnlActivites_ListLayout.setHorizontalGroup(pnlActivites_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivites_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane11, -1, 1089, 32767).addContainerGap()));
      pnlActivites_ListLayout.setVerticalGroup(pnlActivites_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivites_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane11, -2, 310, -2).addContainerGap(-1, 32767)));
      GroupLayout pnlActivitesLayout = new GroupLayout(this.pnlActivites);
      this.pnlActivites.setLayout(pnlActivitesLayout);
      pnlActivitesLayout.setHorizontalGroup(pnlActivitesLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivitesLayout.createSequentialGroup().addGroup(pnlActivitesLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.pnlActivites_List, Alignment.LEADING, -1, -1, 32767).addGroup(pnlActivitesLayout.createSequentialGroup().addContainerGap().addGroup(pnlActivitesLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.pnlActivites_Edit, -1, -1, 32767).addComponent(this.pnlActivites_Btn, -1, -1, 32767)))).addGap(16, 16, 16)));
      pnlActivitesLayout.setVerticalGroup(pnlActivitesLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlActivitesLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlActivites_Edit, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlActivites_Btn, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlActivites_List, -2, -1, -2).addGap(0, 0, 0)));
      this.jPanel5.add(this.pnlActivites, new AbsoluteConstraints(0, 0, -1, -1));
      this.pnlBody.addTab("Services", this.jPanel5);
      this.jPanel6.setBackground((Color)null);
      this.jPanel6.setLayout(new AbsoluteLayout());
      this.pnlPostes.setBackground(new Color(255, 255, 255));
      this.pnlPostes_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete_Postes.setToolTipText("Supprimer");
      this.btnDelete_Postes.setContentAreaFilled(false);
      this.btnDelete_Postes.setCursor(new Cursor(12));
      this.btnDelete_Postes.setEnabled(false);
      this.btnDelete_Postes.setOpaque(true);
      this.btnDelete_Postes.addActionListener(new 22(this));
      this.btnSave_Postes.setToolTipText("Sauvegarder");
      this.btnSave_Postes.setContentAreaFilled(false);
      this.btnSave_Postes.setCursor(new Cursor(12));
      this.btnSave_Postes.setEnabled(false);
      this.btnSave_Postes.setOpaque(true);
      this.btnSave_Postes.addActionListener(new 23(this));
      this.btnNew_Postes.setToolTipText("Nouveau");
      this.btnNew_Postes.setContentAreaFilled(false);
      this.btnNew_Postes.setCursor(new Cursor(12));
      this.btnNew_Postes.setOpaque(true);
      this.btnNew_Postes.addActionListener(new 24(this));
      GroupLayout pnlPostes_BtnLayout = new GroupLayout(this.pnlPostes_Btn);
      this.pnlPostes_Btn.setLayout(pnlPostes_BtnLayout);
      pnlPostes_BtnLayout.setHorizontalGroup(pnlPostes_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlPostes_BtnLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnNew_Postes, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave_Postes, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete_Postes, -2, 35, -2).addGap(18, 18, 18)));
      pnlPostes_BtnLayout.setVerticalGroup(pnlPostes_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete_Postes, Alignment.TRAILING, -2, 35, -2).addComponent(this.btnSave_Postes, -2, 35, -2).addComponent(this.btnNew_Postes, -2, 35, -2));
      this.pnlPostes_Edit.setBackground(new Color(255, 255, 255));
      this.tNom_Postes.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNom_Postes.setBorder((Border)null);
      this.tNom_Postes.addKeyListener(new 25(this));
      this.jLabel9.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel9.setForeground(new Color(0, 102, 153));
      this.jLabel9.setText("Nom");
      GroupLayout pnlPostes_EditLayout = new GroupLayout(this.pnlPostes_Edit);
      this.pnlPostes_Edit.setLayout(pnlPostes_EditLayout);
      pnlPostes_EditLayout.setHorizontalGroup(pnlPostes_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlPostes_EditLayout.createSequentialGroup().addGap(26, 26, 26).addGroup(pnlPostes_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator4).addComponent(this.jLabel9, -2, 310, -2).addComponent(this.tNom_Postes, -1, 412, 32767)).addContainerGap(652, 32767)));
      pnlPostes_EditLayout.setVerticalGroup(pnlPostes_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlPostes_EditLayout.createSequentialGroup().addGap(4, 4, 4).addComponent(this.jLabel9).addGap(0, 0, 0).addComponent(this.tNom_Postes, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator4, -2, -1, -2).addContainerGap(12, 32767)));
      this.pnlPostes_List.setBackground(new Color(255, 255, 255));
      this.listTable_Postes.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable_Postes.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable_Postes.setGridColor(new Color(0, 102, 153));
      this.listTable_Postes.setSelectionBackground(new Color(0, 102, 153));
      this.listTable_Postes.setShowGrid(false);
      this.listTable_Postes.addMouseListener(new 26(this));
      this.jScrollPane1.setViewportView(this.listTable_Postes);
      GroupLayout pnlPostes_ListLayout = new GroupLayout(this.pnlPostes_List);
      this.pnlPostes_List.setLayout(pnlPostes_ListLayout);
      pnlPostes_ListLayout.setHorizontalGroup(pnlPostes_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlPostes_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1, -2, 1078, -2).addContainerGap(12, 32767)));
      pnlPostes_ListLayout.setVerticalGroup(pnlPostes_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlPostes_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1, -2, 337, -2).addContainerGap(-1, 32767)));
      GroupLayout pnlPostesLayout = new GroupLayout(this.pnlPostes);
      this.pnlPostes.setLayout(pnlPostesLayout);
      pnlPostesLayout.setHorizontalGroup(pnlPostesLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlPostesLayout.createSequentialGroup().addGroup(pnlPostesLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.pnlPostes_List, Alignment.LEADING, -1, -1, 32767).addGroup(Alignment.LEADING, pnlPostesLayout.createSequentialGroup().addContainerGap().addGroup(pnlPostesLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.pnlPostes_Edit, -1, -1, 32767).addComponent(this.pnlPostes_Btn, -1, -1, 32767)))).addGap(21, 21, 21)));
      pnlPostesLayout.setVerticalGroup(pnlPostesLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlPostesLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlPostes_Edit, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlPostes_Btn, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlPostes_List, -1, -1, 32767).addContainerGap()));
      this.jPanel6.add(this.pnlPostes, new AbsoluteConstraints(0, 0, -1, -1));
      this.pnlBody.addTab("Postes", this.jPanel6);
      this.jPanel7.setBackground((Color)null);
      this.jPanel7.setLayout(new AbsoluteLayout());
      this.pnlStatuts.setBackground(new Color(255, 255, 255));
      this.pnlStatuts_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete_Statuts.setToolTipText("Supprimer");
      this.btnDelete_Statuts.setContentAreaFilled(false);
      this.btnDelete_Statuts.setCursor(new Cursor(12));
      this.btnDelete_Statuts.setEnabled(false);
      this.btnDelete_Statuts.setOpaque(true);
      this.btnDelete_Statuts.addActionListener(new 27(this));
      this.btnSave_Statuts.setToolTipText("Sauvegarder");
      this.btnSave_Statuts.setContentAreaFilled(false);
      this.btnSave_Statuts.setCursor(new Cursor(12));
      this.btnSave_Statuts.setEnabled(false);
      this.btnSave_Statuts.setOpaque(true);
      this.btnSave_Statuts.addActionListener(new 28(this));
      this.btnNew_Statuts.setToolTipText("Nouveau");
      this.btnNew_Statuts.setContentAreaFilled(false);
      this.btnNew_Statuts.setCursor(new Cursor(12));
      this.btnNew_Statuts.setOpaque(true);
      this.btnNew_Statuts.addActionListener(new 29(this));
      GroupLayout pnlStatuts_BtnLayout = new GroupLayout(this.pnlStatuts_Btn);
      this.pnlStatuts_Btn.setLayout(pnlStatuts_BtnLayout);
      pnlStatuts_BtnLayout.setHorizontalGroup(pnlStatuts_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlStatuts_BtnLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnNew_Statuts, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave_Statuts, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete_Statuts, -2, 35, -2).addGap(20, 20, 20)));
      pnlStatuts_BtnLayout.setVerticalGroup(pnlStatuts_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete_Statuts, -2, 35, -2).addComponent(this.btnSave_Statuts, -2, 35, -2).addComponent(this.btnNew_Statuts, Alignment.TRAILING, -2, 35, -2));
      this.pnlStatuts_Edit.setBackground(new Color(255, 255, 255));
      this.tNom_Statuts.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNom_Statuts.setBorder((Border)null);
      this.tNom_Statuts.addKeyListener(new 30(this));
      this.jLabel10.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel10.setForeground(new Color(0, 102, 153));
      this.jLabel10.setText("Nom");
      GroupLayout pnlStatuts_EditLayout = new GroupLayout(this.pnlStatuts_Edit);
      this.pnlStatuts_Edit.setLayout(pnlStatuts_EditLayout);
      pnlStatuts_EditLayout.setHorizontalGroup(pnlStatuts_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlStatuts_EditLayout.createSequentialGroup().addGap(26, 26, 26).addGroup(pnlStatuts_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator5).addComponent(this.jLabel10, -2, 310, -2).addComponent(this.tNom_Statuts, -1, 412, 32767)).addContainerGap(644, 32767)));
      pnlStatuts_EditLayout.setVerticalGroup(pnlStatuts_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlStatuts_EditLayout.createSequentialGroup().addGap(4, 4, 4).addComponent(this.jLabel10).addGap(0, 0, 0).addComponent(this.tNom_Statuts, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator5, -2, -1, -2).addContainerGap(12, 32767)));
      this.pnlStatuts_List.setBackground(new Color(255, 255, 255));
      this.listTable_Statuts.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable_Statuts.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable_Statuts.setSelectionBackground(new Color(0, 102, 153));
      this.listTable_Statuts.setShowGrid(false);
      this.listTable_Statuts.addMouseListener(new 31(this));
      this.jScrollPane8.setViewportView(this.listTable_Statuts);
      GroupLayout pnlStatuts_ListLayout = new GroupLayout(this.pnlStatuts_List);
      this.pnlStatuts_List.setLayout(pnlStatuts_ListLayout);
      pnlStatuts_ListLayout.setHorizontalGroup(pnlStatuts_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlStatuts_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane8, -2, 1070, -2).addContainerGap(12, 32767)));
      pnlStatuts_ListLayout.setVerticalGroup(pnlStatuts_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlStatuts_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane8, -2, 310, -2).addContainerGap(-1, 32767)));
      GroupLayout pnlStatutsLayout = new GroupLayout(this.pnlStatuts);
      this.pnlStatuts.setLayout(pnlStatutsLayout);
      pnlStatutsLayout.setHorizontalGroup(pnlStatutsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlStatutsLayout.createSequentialGroup().addGroup(pnlStatutsLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.pnlStatuts_List, Alignment.LEADING, -1, -1, 32767).addGroup(pnlStatutsLayout.createSequentialGroup().addContainerGap().addGroup(pnlStatutsLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.pnlStatuts_Edit, -1, -1, 32767).addComponent(this.pnlStatuts_Btn, -1, -1, 32767)))).addGap(34, 34, 34)));
      pnlStatutsLayout.setVerticalGroup(pnlStatutsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlStatutsLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlStatuts_Edit, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlStatuts_Btn, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlStatuts_List, -2, -1, -2)));
      this.jPanel7.add(this.pnlStatuts, new AbsoluteConstraints(0, 0, -1, -1));
      this.pnlBody.addTab("Statuts", this.jPanel7);
      this.jPanel8.setBackground((Color)null);
      this.jPanel8.setLayout(new AbsoluteLayout());
      this.pnlGrillesalairebases.setBackground(new Color(255, 255, 255));
      this.pnlGrillesalairebases_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete_Grillesalairebases.setToolTipText("Supprimer");
      this.btnDelete_Grillesalairebases.setContentAreaFilled(false);
      this.btnDelete_Grillesalairebases.setCursor(new Cursor(12));
      this.btnDelete_Grillesalairebases.setEnabled(false);
      this.btnDelete_Grillesalairebases.setOpaque(true);
      this.btnDelete_Grillesalairebases.addActionListener(new 32(this));
      this.btnSave_Grillesalairebases.setToolTipText("Sauvegarder");
      this.btnSave_Grillesalairebases.setContentAreaFilled(false);
      this.btnSave_Grillesalairebases.setCursor(new Cursor(12));
      this.btnSave_Grillesalairebases.setEnabled(false);
      this.btnSave_Grillesalairebases.setOpaque(true);
      this.btnSave_Grillesalairebases.addActionListener(new 33(this));
      this.btnNew_Grillesalairebases.setToolTipText("Nouveau");
      this.btnNew_Grillesalairebases.setContentAreaFilled(false);
      this.btnNew_Grillesalairebases.setCursor(new Cursor(12));
      this.btnNew_Grillesalairebases.setOpaque(true);
      this.btnNew_Grillesalairebases.addActionListener(new 34(this));
      GroupLayout pnlGrillesalairebases_BtnLayout = new GroupLayout(this.pnlGrillesalairebases_Btn);
      this.pnlGrillesalairebases_Btn.setLayout(pnlGrillesalairebases_BtnLayout);
      pnlGrillesalairebases_BtnLayout.setHorizontalGroup(pnlGrillesalairebases_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlGrillesalairebases_BtnLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnNew_Grillesalairebases, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave_Grillesalairebases, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete_Grillesalairebases, -2, 35, -2).addGap(30, 30, 30)));
      pnlGrillesalairebases_BtnLayout.setVerticalGroup(pnlGrillesalairebases_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete_Grillesalairebases, -2, 35, -2).addComponent(this.btnSave_Grillesalairebases, Alignment.TRAILING, -2, 35, -2).addComponent(this.btnNew_Grillesalairebases, -2, 35, -2));
      this.pnlGrillesalairebases_Edit.setBackground(new Color(255, 255, 255));
      this.tNomCategorie_Grillesalairebases.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNomCategorie_Grillesalairebases.setBorder((Border)null);
      this.tNomCategorie_Grillesalairebases.addCaretListener(new 35(this));
      this.tNomCategorie_Grillesalairebases.addKeyListener(new 36(this));
      this.jLabel11.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel11.setForeground(new Color(0, 102, 153));
      this.jLabel11.setText("Statut *");
      this.jLabel12.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel12.setForeground(new Color(0, 102, 153));
      this.jLabel12.setText("Niveau *");
      this.tNiveau_Grillesalairebases.setBorder((Border)null);
      this.tNiveau_Grillesalairebases.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tNiveau_Grillesalairebases.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNiveau_Grillesalairebases.addCaretListener(new 37(this));
      this.tSalaireBase_Grillesalairebases.setBorder((Border)null);
      this.tSalaireBase_Grillesalairebases.setFont(new Font("Segoe UI Light", 0, 16));
      this.jLabel13.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel13.setForeground(new Color(0, 102, 153));
      this.jLabel13.setText("Salaire de base *");
      this.tCategorie_Grillesalairebases.setEditable(false);
      this.tCategorie_Grillesalairebases.setFont(new Font("Segoe UI Light", 0, 16));
      this.tCategorie_Grillesalairebases.setBorder((Border)null);
      this.tCategorie_Grillesalairebases.addKeyListener(new 38(this));
      this.tStatut_Grillesalairebases.setFont(new Font("Segoe UI Light", 0, 16));
      this.tStatut_Grillesalairebases.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.jLabel14.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel14.setForeground(new Color(0, 102, 153));
      this.jLabel14.setText("Nom *");
      GroupLayout pnlGrillesalairebases_EditLayout = new GroupLayout(this.pnlGrillesalairebases_Edit);
      this.pnlGrillesalairebases_Edit.setLayout(pnlGrillesalairebases_EditLayout);
      pnlGrillesalairebases_EditLayout.setHorizontalGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlGrillesalairebases_EditLayout.createSequentialGroup().addGap(22, 22, 22).addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel11, Alignment.TRAILING, -2, 220, -2).addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.tStatut_Grillesalairebases, -2, 220, -2).addComponent(this.jSeparator10, -2, 219, -2))).addGap(27, 27, 27).addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel14, -1, 158, 32767).addComponent(this.tNomCategorie_Grillesalairebases).addComponent(this.jSeparator6)).addGap(18, 18, 18).addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.jLabel12, Alignment.LEADING, -1, 71, 32767).addComponent(this.tNiveau_Grillesalairebases, Alignment.LEADING).addComponent(this.jSeparator7)).addGap(27, 27, 27).addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator9).addComponent(this.tCategorie_Grillesalairebases, -1, 201, 32767)).addGap(18, 18, 18).addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel13, -2, 113, -2).addComponent(this.tSalaireBase_Grillesalairebases, -2, 113, -2).addComponent(this.jSeparator8, -2, 113, -2)).addGap(195, 195, 195)));
      pnlGrillesalairebases_EditLayout.setVerticalGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlGrillesalairebases_EditLayout.createSequentialGroup().addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlGrillesalairebases_EditLayout.createSequentialGroup().addComponent(this.jLabel13).addGap(0, 0, 0).addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.tSalaireBase_Grillesalairebases, -2, 30, -2).addComponent(this.tCategorie_Grillesalairebases, -2, 30, -2)).addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jSeparator8, -2, -1, -2).addComponent(this.jSeparator9, -2, 2, -2))).addGroup(pnlGrillesalairebases_EditLayout.createSequentialGroup().addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlGrillesalairebases_EditLayout.createSequentialGroup().addComponent(this.jLabel11).addGap(0, 0, 0).addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.tNomCategorie_Grillesalairebases, -2, 30, -2).addComponent(this.tStatut_Grillesalairebases, -2, 30, -2)).addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator10).addComponent(this.jSeparator6))).addGroup(pnlGrillesalairebases_EditLayout.createSequentialGroup().addGroup(pnlGrillesalairebases_EditLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel12).addComponent(this.jLabel14)).addGap(0, 0, 0).addComponent(this.tNiveau_Grillesalairebases, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator7, -2, -1, -2))).addGap(2, 2, 2))).addGap(0, 4, 32767)));
      this.pnlGrillesalairebases_List.setBackground(new Color(255, 255, 255));
      this.listTable_Grillesalairebases.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable_Grillesalairebases.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable_Grillesalairebases.setSelectionBackground(new Color(0, 102, 153));
      this.listTable_Grillesalairebases.setShowGrid(false);
      this.listTable_Grillesalairebases.addMouseListener(new 39(this));
      this.jScrollPane6.setViewportView(this.listTable_Grillesalairebases);
      GroupLayout pnlGrillesalairebases_ListLayout = new GroupLayout(this.pnlGrillesalairebases_List);
      this.pnlGrillesalairebases_List.setLayout(pnlGrillesalairebases_ListLayout);
      pnlGrillesalairebases_ListLayout.setHorizontalGroup(pnlGrillesalairebases_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlGrillesalairebases_ListLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.jScrollPane6, -2, 1058, -2).addGap(30, 30, 30)));
      pnlGrillesalairebases_ListLayout.setVerticalGroup(pnlGrillesalairebases_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlGrillesalairebases_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane6, -1, 367, 32767).addContainerGap()));
      GroupLayout pnlGrillesalairebasesLayout = new GroupLayout(this.pnlGrillesalairebases);
      this.pnlGrillesalairebases.setLayout(pnlGrillesalairebasesLayout);
      pnlGrillesalairebasesLayout.setHorizontalGroup(pnlGrillesalairebasesLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlGrillesalairebasesLayout.createSequentialGroup().addGroup(pnlGrillesalairebasesLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.pnlGrillesalairebases_List, -2, 1080, -2).addGroup(pnlGrillesalairebasesLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.pnlGrillesalairebases_Edit, -1, -1, 32767).addComponent(this.pnlGrillesalairebases_Btn, -1, -1, 32767))).addGap(37, 37, 37)));
      pnlGrillesalairebasesLayout.setVerticalGroup(pnlGrillesalairebasesLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlGrillesalairebasesLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlGrillesalairebases_Edit, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlGrillesalairebases_Btn, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlGrillesalairebases_List, -1, -1, 32767)));
      this.jPanel8.add(this.pnlGrillesalairebases, new AbsoluteConstraints(0, 0, -1, -1));
      this.pnlBody.addTab("Grille SB", this.jPanel8);
      this.jPanel9.setBackground((Color)null);
      this.pnlMotifs.setBackground(new Color(255, 255, 255));
      this.pnlMotifs_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete_Motifs.setToolTipText("Supprimer");
      this.btnDelete_Motifs.setContentAreaFilled(false);
      this.btnDelete_Motifs.setCursor(new Cursor(12));
      this.btnDelete_Motifs.setEnabled(false);
      this.btnDelete_Motifs.setOpaque(true);
      this.btnDelete_Motifs.addActionListener(new 40(this));
      this.btnSave_Motifs.setToolTipText("Sauvegarder");
      this.btnSave_Motifs.setContentAreaFilled(false);
      this.btnSave_Motifs.setCursor(new Cursor(12));
      this.btnSave_Motifs.setEnabled(false);
      this.btnSave_Motifs.setOpaque(true);
      this.btnSave_Motifs.addActionListener(new 41(this));
      this.btnNew_Motifs.setToolTipText("Nouveau");
      this.btnNew_Motifs.setContentAreaFilled(false);
      this.btnNew_Motifs.setCursor(new Cursor(12));
      this.btnNew_Motifs.setOpaque(true);
      this.btnNew_Motifs.addActionListener(new 42(this));
      GroupLayout pnlMotifs_BtnLayout = new GroupLayout(this.pnlMotifs_Btn);
      this.pnlMotifs_Btn.setLayout(pnlMotifs_BtnLayout);
      pnlMotifs_BtnLayout.setHorizontalGroup(pnlMotifs_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlMotifs_BtnLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnNew_Motifs, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave_Motifs, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete_Motifs, -2, 35, -2).addGap(26, 26, 26)));
      pnlMotifs_BtnLayout.setVerticalGroup(pnlMotifs_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete_Motifs, -2, 35, -2).addComponent(this.btnSave_Motifs, -2, 35, -2).addComponent(this.btnNew_Motifs, Alignment.TRAILING, -2, 35, -2));
      this.pnlMotifs_Edit.setBackground(new Color(255, 255, 255));
      this.tNom_Motifs.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNom_Motifs.setBorder((Border)null);
      this.tNom_Motifs.addKeyListener(new 43(this));
      this.jLabel24.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel24.setForeground(new Color(0, 102, 153));
      this.jLabel24.setText("Nom *");
      this.tEmployeSoumisCnss_Motifs.setBackground(new Color(255, 255, 255));
      this.tEmployeSoumisCnss_Motifs.setFont(new Font("SansSerif", 1, 11));
      this.tEmployeSoumisCnss_Motifs.setForeground(new Color(0, 102, 153));
      this.tEmployeSoumisCnss_Motifs.setText("CNSS / Salaire");
      this.tDeclarationSoumisCnss_Motifs.setBackground(new Color(255, 255, 255));
      this.tDeclarationSoumisCnss_Motifs.setFont(new Font("SansSerif", 1, 11));
      this.tDeclarationSoumisCnss_Motifs.setForeground(new Color(0, 102, 153));
      this.tDeclarationSoumisCnss_Motifs.setText("CNSS / D\u00e9cl.");
      this.tDeclarationSoumisCnam_Motifs.setBackground(new Color(255, 255, 255));
      this.tDeclarationSoumisCnam_Motifs.setFont(new Font("SansSerif", 1, 11));
      this.tDeclarationSoumisCnam_Motifs.setForeground(new Color(0, 102, 153));
      this.tDeclarationSoumisCnam_Motifs.setText("CNAM / D\u00e9cl.");
      this.tEmployeSoumisCnam_Motifs.setBackground(new Color(255, 255, 255));
      this.tEmployeSoumisCnam_Motifs.setFont(new Font("SansSerif", 1, 11));
      this.tEmployeSoumisCnam_Motifs.setForeground(new Color(0, 102, 153));
      this.tEmployeSoumisCnam_Motifs.setText("CNAM / Salaire");
      this.tDeclarationSoumisIts_Motifs.setBackground(new Color(255, 255, 255));
      this.tDeclarationSoumisIts_Motifs.setFont(new Font("SansSerif", 1, 11));
      this.tDeclarationSoumisIts_Motifs.setForeground(new Color(0, 102, 153));
      this.tDeclarationSoumisIts_Motifs.setText("ITS / D\u00e9cl.");
      this.tEmployeSoumisIts_Motifs.setBackground(new Color(255, 255, 255));
      this.tEmployeSoumisIts_Motifs.setFont(new Font("SansSerif", 1, 11));
      this.tEmployeSoumisIts_Motifs.setForeground(new Color(0, 102, 153));
      this.tEmployeSoumisIts_Motifs.setText("ITS / Salaire");
      this.cActif_Motifs.setBackground(new Color(255, 255, 255));
      this.cActif_Motifs.setFont(new Font("SansSerif", 1, 11));
      this.cActif_Motifs.setForeground(new Color(51, 153, 0));
      this.cActif_Motifs.setText("Actif");
      GroupLayout pnlMotifs_EditLayout = new GroupLayout(this.pnlMotifs_Edit);
      this.pnlMotifs_Edit.setLayout(pnlMotifs_EditLayout);
      pnlMotifs_EditLayout.setHorizontalGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlMotifs_EditLayout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel24, -2, 113, -2).addComponent(this.tNom_Motifs, -1, 230, 32767).addComponent(this.jSeparator16)).addGap(5, 5, 5).addComponent(this.cActif_Motifs).addGap(32, 32, 32).addComponent(this.tEmployeSoumisCnss_Motifs).addGap(10, 10, 10).addComponent(this.tDeclarationSoumisCnss_Motifs).addGap(28, 28, 28).addComponent(this.tEmployeSoumisCnam_Motifs).addGap(10, 10, 10).addComponent(this.tDeclarationSoumisCnam_Motifs, -2, 119, -2).addGap(30, 30, 30).addComponent(this.tEmployeSoumisIts_Motifs).addGap(10, 10, 10).addComponent(this.tDeclarationSoumisIts_Motifs).addContainerGap()));
      pnlMotifs_EditLayout.setVerticalGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGap(0, 0, 32767).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel24).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.tNom_Motifs, -2, 30, -2)).addGroup(Alignment.TRAILING, pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.tEmployeSoumisIts_Motifs).addComponent(this.tDeclarationSoumisIts_Motifs)).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.tEmployeSoumisCnss_Motifs).addComponent(this.tDeclarationSoumisCnss_Motifs).addComponent(this.cActif_Motifs)).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.tEmployeSoumisCnam_Motifs).addComponent(this.tDeclarationSoumisCnam_Motifs)))).addGap(1, 1, 1).addComponent(this.jSeparator16, -2, -1, -2).addGap(20, 20, 20)));
      this.pnlMotifs_List.setBackground(new Color(255, 255, 255));
      this.listTable_Motifs.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable_Motifs.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable_Motifs.setSelectionBackground(new Color(0, 102, 153));
      this.listTable_Motifs.setShowGrid(false);
      this.listTable_Motifs.addMouseListener(new 44(this));
      this.jScrollPane9.setViewportView(this.listTable_Motifs);
      GroupLayout pnlMotifs_ListLayout = new GroupLayout(this.pnlMotifs_List);
      this.pnlMotifs_List.setLayout(pnlMotifs_ListLayout);
      pnlMotifs_ListLayout.setHorizontalGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane9, -2, 1056, -2).addContainerGap(17, 32767)));
      pnlMotifs_ListLayout.setVerticalGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane9, -2, 342, -2)));
      GroupLayout pnlMotifsLayout = new GroupLayout(this.pnlMotifs);
      this.pnlMotifs.setLayout(pnlMotifsLayout);
      pnlMotifsLayout.setHorizontalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addGroup(pnlMotifsLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlMotifsLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlMotifs_List, -2, -1, -2)).addGroup(Alignment.LEADING, pnlMotifsLayout.createSequentialGroup().addGap(10, 10, 10).addGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.pnlMotifs_Btn, -1, -1, 32767).addComponent(this.pnlMotifs_Edit, -1, -1, 32767)))).addContainerGap(18, 32767)));
      pnlMotifsLayout.setVerticalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlMotifs_Edit, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlMotifs_Btn, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlMotifs_List, -1, -1, 32767).addContainerGap()));
      GroupLayout jPanel9Layout = new GroupLayout(this.jPanel9);
      this.jPanel9.setLayout(jPanel9Layout);
      jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addComponent(this.pnlMotifs, -2, -1, -2).addGap(0, 25, 32767)));
      jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addContainerGap().addComponent(this.pnlMotifs, -2, -1, -2).addContainerGap(-1, 32767)));
      this.pnlBody.addTab("Motifs", this.jPanel9);
      this.jPanel10.setBackground((Color)null);
      this.jPanel10.setLayout(new AbsoluteLayout());
      this.pnlOrigines.setBackground(new Color(255, 255, 255));
      this.pnlOrigines_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete_Origines.setToolTipText("Supprimer");
      this.btnDelete_Origines.setContentAreaFilled(false);
      this.btnDelete_Origines.setCursor(new Cursor(12));
      this.btnDelete_Origines.setEnabled(false);
      this.btnDelete_Origines.setOpaque(true);
      this.btnDelete_Origines.addActionListener(new 45(this));
      this.btnSave_Origines.setToolTipText("Sauvegarder");
      this.btnSave_Origines.setContentAreaFilled(false);
      this.btnSave_Origines.setCursor(new Cursor(12));
      this.btnSave_Origines.setEnabled(false);
      this.btnSave_Origines.setOpaque(true);
      this.btnSave_Origines.addActionListener(new 46(this));
      this.btnNew_Origines.setToolTipText("Nouveau");
      this.btnNew_Origines.setContentAreaFilled(false);
      this.btnNew_Origines.setCursor(new Cursor(12));
      this.btnNew_Origines.setOpaque(true);
      this.btnNew_Origines.addActionListener(new 47(this));
      GroupLayout pnlOrigines_BtnLayout = new GroupLayout(this.pnlOrigines_Btn);
      this.pnlOrigines_Btn.setLayout(pnlOrigines_BtnLayout);
      pnlOrigines_BtnLayout.setHorizontalGroup(pnlOrigines_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlOrigines_BtnLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnNew_Origines, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave_Origines, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete_Origines, -2, 35, -2).addGap(31, 31, 31)));
      pnlOrigines_BtnLayout.setVerticalGroup(pnlOrigines_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete_Origines, -2, 35, -2).addComponent(this.btnSave_Origines, -2, 35, -2).addComponent(this.btnNew_Origines, Alignment.TRAILING, -2, 35, -2));
      this.pnlOrigines_Edit.setBackground(new Color(255, 255, 255));
      this.tLibelle_Origines.setFont(new Font("Segoe UI Light", 0, 16));
      this.tLibelle_Origines.setBorder((Border)null);
      this.tLibelle_Origines.addKeyListener(new 48(this));
      this.tNbSmighorPourIndConges_Origines.setBorder((Border)null);
      this.tNbSmighorPourIndConges_Origines.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tNbSmighorPourIndConges_Origines.setFont(new Font("Segoe UI Light", 0, 16));
      this.jLabel21.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel21.setForeground(new Color(0, 102, 153));
      this.jLabel21.setText("Nombre de SMIG horaires *");
      this.jLabel22.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel22.setForeground(new Color(0, 102, 153));
      this.jLabel22.setText("Nom *");
      GroupLayout pnlOrigines_EditLayout = new GroupLayout(this.pnlOrigines_Edit);
      this.pnlOrigines_Edit.setLayout(pnlOrigines_EditLayout);
      pnlOrigines_EditLayout.setHorizontalGroup(pnlOrigines_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlOrigines_EditLayout.createSequentialGroup().addContainerGap().addGroup(pnlOrigines_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel22, -2, 113, -2).addComponent(this.tLibelle_Origines).addComponent(this.jSeparator14, -2, 384, -2)).addGap(65, 65, 65).addGroup(pnlOrigines_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel21, -1, -1, 32767).addComponent(this.tNbSmighorPourIndConges_Origines).addComponent(this.jSeparator17, -2, 164, -2)).addContainerGap(462, 32767)));
      pnlOrigines_EditLayout.setVerticalGroup(pnlOrigines_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlOrigines_EditLayout.createSequentialGroup().addGap(0, 0, 32767).addGroup(pnlOrigines_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlOrigines_EditLayout.createSequentialGroup().addGroup(pnlOrigines_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlOrigines_EditLayout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.tLibelle_Origines, -2, 30, -2)).addComponent(this.jLabel22)).addComponent(this.jSeparator14, -2, -1, -2)).addGroup(Alignment.TRAILING, pnlOrigines_EditLayout.createSequentialGroup().addComponent(this.jLabel21).addGap(0, 0, 0).addComponent(this.tNbSmighorPourIndConges_Origines, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator17, -2, 2, -2))).addContainerGap()));
      this.pnlOrigines_List.setBackground(new Color(255, 255, 255));
      this.listTable_Origines.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable_Origines.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable_Origines.setSelectionBackground(new Color(0, 102, 153));
      this.listTable_Origines.setShowGrid(false);
      this.listTable_Origines.addMouseListener(new 49(this));
      this.jScrollPane7.setViewportView(this.listTable_Origines);
      GroupLayout pnlOrigines_ListLayout = new GroupLayout(this.pnlOrigines_List);
      this.pnlOrigines_List.setLayout(pnlOrigines_ListLayout);
      pnlOrigines_ListLayout.setHorizontalGroup(pnlOrigines_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlOrigines_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane7, -2, 1079, -2).addContainerGap(-1, 32767)));
      pnlOrigines_ListLayout.setVerticalGroup(pnlOrigines_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlOrigines_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane7, -2, 310, -2).addContainerGap(-1, 32767)));
      GroupLayout pnlOriginesLayout = new GroupLayout(this.pnlOrigines);
      this.pnlOrigines.setLayout(pnlOriginesLayout);
      pnlOriginesLayout.setHorizontalGroup(pnlOriginesLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlOriginesLayout.createSequentialGroup().addGroup(pnlOriginesLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.pnlOrigines_List, -2, -1, -2).addGroup(pnlOriginesLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.pnlOrigines_Edit, -1, -1, 32767).addComponent(this.pnlOrigines_Btn, -1, -1, 32767))).addGap(28, 28, 28)));
      pnlOriginesLayout.setVerticalGroup(pnlOriginesLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlOriginesLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlOrigines_Edit, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlOrigines_Btn, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlOrigines_List, -2, -1, -2).addGap(0, 0, 0)));
      this.jPanel10.add(this.pnlOrigines, new AbsoluteConstraints(0, 0, -1, -1));
      this.pnlBody.addTab("Origines", this.jPanel10);
      this.jPanel11.setBackground((Color)null);
      this.jPanel11.setLayout(new AbsoluteLayout());
      this.pnlBanques.setBackground(new Color(255, 255, 255));
      this.pnlBanques_Btn.setBackground(new Color(255, 255, 255));
      this.btnDelete_Banques.setToolTipText("Supprimer");
      this.btnDelete_Banques.setContentAreaFilled(false);
      this.btnDelete_Banques.setCursor(new Cursor(12));
      this.btnDelete_Banques.setEnabled(false);
      this.btnDelete_Banques.setOpaque(true);
      this.btnDelete_Banques.addActionListener(new 50(this));
      this.btnSave_Banques.setToolTipText("Sauvegarder");
      this.btnSave_Banques.setContentAreaFilled(false);
      this.btnSave_Banques.setCursor(new Cursor(12));
      this.btnSave_Banques.setEnabled(false);
      this.btnSave_Banques.setOpaque(true);
      this.btnSave_Banques.addActionListener(new 51(this));
      this.btnNew_Banques.setToolTipText("Nouveau");
      this.btnNew_Banques.setContentAreaFilled(false);
      this.btnNew_Banques.setCursor(new Cursor(12));
      this.btnNew_Banques.setOpaque(true);
      this.btnNew_Banques.addActionListener(new 52(this));
      GroupLayout pnlBanques_BtnLayout = new GroupLayout(this.pnlBanques_Btn);
      this.pnlBanques_Btn.setLayout(pnlBanques_BtnLayout);
      pnlBanques_BtnLayout.setHorizontalGroup(pnlBanques_BtnLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBanques_BtnLayout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnNew_Banques, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSave_Banques, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnDelete_Banques, -2, 35, -2).addGap(25, 25, 25)));
      pnlBanques_BtnLayout.setVerticalGroup(pnlBanques_BtnLayout.createParallelGroup(Alignment.LEADING).addComponent(this.btnDelete_Banques, -2, 35, -2).addComponent(this.btnSave_Banques, -2, 35, -2).addComponent(this.btnNew_Banques, Alignment.TRAILING, -2, 35, -2));
      this.pnlBanques_Edit.setBackground(new Color(255, 255, 255));
      this.tNom_Banques.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNom_Banques.setBorder((Border)null);
      this.tNom_Banques.addKeyListener(new 53(this));
      this.jLabel8.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel8.setForeground(new Color(0, 102, 153));
      this.jLabel8.setText("Nom *");
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("Chapitre comptable");
      this.tNoChapitreCompta_Banques.setBorder((Border)null);
      this.tNoChapitreCompta_Banques.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tNoChapitreCompta_Banques.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNoChapitreCompta_Banques.addCaretListener(new 54(this));
      this.tNoCompteCompta_Banques.setBorder((Border)null);
      this.tNoCompteCompta_Banques.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
      this.tNoCompteCompta_Banques.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNoCompteCompta_Banques.addCaretListener(new 55(this));
      this.jLabel26.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel26.setForeground(new Color(0, 102, 153));
      this.jLabel26.setText("Compte comptable");
      this.tNoCompteComptaCle_Banques.setFont(new Font("Segoe UI Light", 0, 16));
      this.tNoCompteComptaCle_Banques.setBorder((Border)null);
      this.tNoCompteComptaCle_Banques.addKeyListener(new 56(this));
      this.jLabel28.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel28.setForeground(new Color(0, 102, 153));
      this.jLabel28.setText("Cl\u00e9");
      GroupLayout pnlBanques_EditLayout = new GroupLayout(this.pnlBanques_Edit);
      this.pnlBanques_Edit.setLayout(pnlBanques_EditLayout);
      pnlBanques_EditLayout.setHorizontalGroup(pnlBanques_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBanques_EditLayout.createSequentialGroup().addGap(26, 26, 26).addGroup(pnlBanques_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator3).addComponent(this.jLabel8, -2, 310, -2).addComponent(this.tNom_Banques, -1, 412, 32767)).addGap(63, 63, 63).addGroup(pnlBanques_EditLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlBanques_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator19).addComponent(this.tNoChapitreCompta_Banques, -1, 100, 32767)).addComponent(this.jLabel20)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pnlBanques_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jSeparator20).addComponent(this.tNoCompteCompta_Banques).addComponent(this.jLabel26, -2, 160, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(pnlBanques_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel28, -1, -1, 32767).addComponent(this.jSeparator18).addComponent(this.tNoCompteComptaCle_Banques, -2, 60, -2)).addContainerGap(224, 32767)));
      pnlBanques_EditLayout.setVerticalGroup(pnlBanques_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBanques_EditLayout.createSequentialGroup().addGroup(pnlBanques_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBanques_EditLayout.createSequentialGroup().addGap(13, 13, 13).addComponent(this.jLabel8).addGap(0, 0, 0).addComponent(this.tNom_Banques, -2, 30, -2)).addGroup(pnlBanques_EditLayout.createSequentialGroup().addContainerGap().addGroup(pnlBanques_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBanques_EditLayout.createSequentialGroup().addComponent(this.jLabel28).addGap(30, 30, 30).addComponent(this.jSeparator18, -2, 2, -2)).addGroup(pnlBanques_EditLayout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.tNoCompteComptaCle_Banques, -2, 30, -2)).addGroup(pnlBanques_EditLayout.createParallelGroup(Alignment.TRAILING).addGroup(pnlBanques_EditLayout.createSequentialGroup().addComponent(this.jLabel26).addGap(0, 0, 0).addComponent(this.tNoCompteCompta_Banques, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator20, -2, 2, -2)).addGroup(pnlBanques_EditLayout.createSequentialGroup().addComponent(this.jLabel20).addGap(0, 0, 0).addComponent(this.tNoChapitreCompta_Banques, -2, 30, -2).addGap(0, 0, 0).addComponent(this.jSeparator19, -2, 2, -2)))))).addGap(0, 0, 0).addComponent(this.jSeparator3, -2, -1, -2).addContainerGap(12, 32767)));
      this.pnlBanques_List.setBackground(new Color(255, 255, 255));
      this.listTable_Banques.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable_Banques.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable_Banques.setSelectionBackground(new Color(0, 102, 153));
      this.listTable_Banques.setShowGrid(false);
      this.listTable_Banques.addMouseListener(new 57(this));
      this.jScrollPane10.setViewportView(this.listTable_Banques);
      GroupLayout pnlBanques_ListLayout = new GroupLayout(this.pnlBanques_List);
      this.pnlBanques_List.setLayout(pnlBanques_ListLayout);
      pnlBanques_ListLayout.setHorizontalGroup(pnlBanques_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBanques_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane10, -2, 1069, -2).addContainerGap(14, 32767)));
      pnlBanques_ListLayout.setVerticalGroup(pnlBanques_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBanques_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane10, -2, 328, -2).addContainerGap(-1, 32767)));
      GroupLayout pnlBanquesLayout = new GroupLayout(this.pnlBanques);
      this.pnlBanques.setLayout(pnlBanquesLayout);
      pnlBanquesLayout.setHorizontalGroup(pnlBanquesLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBanquesLayout.createSequentialGroup().addGroup(pnlBanquesLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(this.pnlBanques_List, Alignment.LEADING, -1, -1, 32767).addGroup(pnlBanquesLayout.createSequentialGroup().addContainerGap().addGroup(pnlBanquesLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.pnlBanques_Edit, -1, -1, 32767).addComponent(this.pnlBanques_Btn, -1, -1, 32767)))).addContainerGap(18, 32767)));
      pnlBanquesLayout.setVerticalGroup(pnlBanquesLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBanquesLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlBanques_Edit, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBanques_Btn, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBanques_List, -1, -1, 32767).addContainerGap()));
      this.jPanel11.add(this.pnlBanques, new AbsoluteConstraints(0, 0, -1, -1));
      this.pnlBody.addTab("Banques", this.jPanel11);
      this.pnlFooter.setBackground(new Color(255, 255, 255));
      this.lblMsg.setFont(new Font("Segoe UI Light", 0, 12));
      this.lblMsg.setForeground(new Color(255, 0, 0));
      this.lblMsg.setToolTipText("");
      GroupLayout pnlFooterLayout = new GroupLayout(this.pnlFooter);
      this.pnlFooter.setLayout(pnlFooterLayout);
      pnlFooterLayout.setHorizontalGroup(pnlFooterLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlFooterLayout.createSequentialGroup().addComponent(this.lblMsg, -2, 1151, -2).addGap(0, 31, 32767)));
      pnlFooterLayout.setVerticalGroup(pnlFooterLayout.createParallelGroup(Alignment.LEADING).addComponent(this.lblMsg, Alignment.TRAILING, -1, 31, 32767));
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlFooter, -2, -1, -2).addComponent(this.jPanel1, -2, -1, -2).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.pnlBody, -2, 1128, -2)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlBody, -1, -1, -2).addGap(0, 0, 0).addComponent(this.pnlFooter, -2, -1, -2)));
      this.setSize(new Dimension(1151, 617));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void tNom_DepartemetKeyPressed(KeyEvent var1) {
   }

   private void btnDelete_DepartementsActionPerformed(ActionEvent var1) {
      if (this.selected_Departement != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.lblMsg, "", false);
            if (this.menu.gl.deleteOcurance(this.selected_Departement)) {
               this.menu.viewMessage(this.lblMsg, "Element supprim\u00e9", false);
               this.dataListUpdate_Departements();
               this.afficherListe_Departements();
               this.clearFields_Departemets();
            }
         }
      } else {
         this.menu.viewMessage(this.lblMsg, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSave_DepartementsActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.lblMsg, "", false);
      String msg = this.validateData_Departemets();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.lblMsg, msg, true);
      } else {
         boolean add = false;
         if (this.selected_Departement == null) {
            this.selected_Departement = new Departement();
            add = true;
         }

         this.selected_Departement.setNom(this.tNom_Departemet.getText());
         if (add) {
            if (this.menu.gl.insertOcurance(this.selected_Departement)) {
               this.menu.viewMessage(this.lblMsg, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(this.selected_Departement)) {
            this.menu.viewMessage(this.lblMsg, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate_Departements();
         this.afficherListe_Departements();
         this.clearFields_Departemets();
      }

   }

   private void btnNew_DepartementsActionPerformed(ActionEvent var1) {
      this.clearFields_Departemets();
   }

   private void btnDelete_ActivitesActionPerformed(ActionEvent var1) {
      if (this.selected_Activite != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.lblMsg, "", false);
            if (this.menu.gl.deleteOcurance(this.selected_Activite)) {
               this.menu.viewMessage(this.lblMsg, "Element supprim\u00e9", false);
               this.dataListUpdate_Activites();
               this.afficherListe_Activites();
               this.clearFields_Activites();
            }
         }
      } else {
         this.menu.viewMessage(this.lblMsg, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSave_ActivitesActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.lblMsg, "", false);
      String msg = this.validateData_Activites();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.lblMsg, msg, true);
      } else {
         boolean add = false;
         if (this.selected_Activite == null) {
            this.selected_Activite = new Activite();
            add = true;
         }

         this.selected_Activite.setNom(this.tNom_Activites.getText());
         if (add) {
            if (this.menu.gl.insertOcurance(this.selected_Activite)) {
               this.menu.viewMessage(this.lblMsg, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(this.selected_Activite)) {
            this.menu.viewMessage(this.lblMsg, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate_Activites();
         this.afficherListe_Activites();
         this.clearFields_Activites();
      }

   }

   private void btnNew_ActivitesActionPerformed(ActionEvent var1) {
      this.clearFields_Activites();
   }

   private void tNom_ActivitesKeyPressed(KeyEvent var1) {
   }

   private void btnDelete_BanquesActionPerformed(ActionEvent var1) {
      if (this.selected_Banque != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.lblMsg, "", false);
            if (this.menu.gl.deleteOcurance(this.selected_Banque)) {
               this.menu.viewMessage(this.lblMsg, "Element supprim\u00e9", false);
               this.dataListUpdate_Banques();
               this.afficherListe_Banques();
               this.clearFields_Banques();
            }
         }
      } else {
         this.menu.viewMessage(this.lblMsg, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSave_BanquesActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.lblMsg, "", false);
      String msg = this.validateData_Banques();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.lblMsg, msg, true);
      } else {
         boolean add = false;
         if (this.selected_Banque == null) {
            this.selected_Banque = new Banque();
            add = true;
         }

         this.selected_Banque.setNom(this.tNom_Banques.getText());
         this.selected_Banque.setNoChapitreCompta(((Number)this.tNoChapitreCompta_Banques.getValue()).longValue());
         this.selected_Banque.setNoCompteCompta(((Number)this.tNoCompteCompta_Banques.getValue()).longValue());
         this.selected_Banque.setNoCompteComptaCle(this.tNoCompteComptaCle_Banques.getText());
         if (add) {
            if (this.menu.gl.insertOcurance(this.selected_Banque)) {
               this.menu.viewMessage(this.lblMsg, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(this.selected_Banque)) {
            this.menu.viewMessage(this.lblMsg, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate_Banques();
         this.afficherListe_Banques();
         this.clearFields_Banques();
      }

   }

   private void btnNew_BanquesActionPerformed(ActionEvent var1) {
      this.clearFields_Banques();
   }

   private void tNom_BanquesKeyPressed(KeyEvent var1) {
   }

   private void btnDelete_PostesActionPerformed(ActionEvent var1) {
      if (this.selected_Poste != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.lblMsg, "", false);
            if (this.menu.gl.deleteOcurance(this.selected_Poste)) {
               this.menu.viewMessage(this.lblMsg, "Element supprim\u00e9", false);
               this.dataListUpdate_Postes();
               this.afficherListe_Postes();
               this.clearFields_Postes();
            }
         }
      } else {
         this.menu.viewMessage(this.lblMsg, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSave_PostesActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.lblMsg, "", false);
      String msg = this.validateData_Postes();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.lblMsg, msg, true);
      } else {
         boolean add = false;
         if (this.selected_Poste == null) {
            this.selected_Poste = new Poste();
            add = true;
         }

         this.selected_Poste.setNom(this.tNom_Postes.getText());
         if (add) {
            if (this.menu.gl.insertOcurance(this.selected_Poste)) {
               this.menu.viewMessage(this.lblMsg, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(this.selected_Poste)) {
            this.menu.viewMessage(this.lblMsg, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate_Postes();
         this.afficherListe_Postes();
         this.clearFields_Postes();
      }

   }

   private void btnNew_PostesActionPerformed(ActionEvent var1) {
      this.clearFields_Postes();
   }

   private void tNom_PostesKeyPressed(KeyEvent var1) {
   }

   private void btnDelete_StatutsActionPerformed(ActionEvent var1) {
      if (this.selected_Statut != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.lblMsg, "", false);
            if (this.menu.gl.deleteOcurance(this.selected_Statut)) {
               this.menu.viewMessage(this.lblMsg, "Element supprim\u00e9", false);
               this.dataListUpdate_Statuts();
               this.afficherListe_Statuts();
               this.clearFields_Statuts();
            }
         }
      } else {
         this.menu.viewMessage(this.lblMsg, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSave_StatutsActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.lblMsg, "", false);
      String msg = this.validateData_Statuts();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.lblMsg, msg, true);
      } else {
         boolean add = false;
         if (this.selected_Statut == null) {
            this.selected_Statut = new Statut();
            add = true;
         }

         this.selected_Statut.setNom(this.tNom_Statuts.getText());
         if (add) {
            if (this.menu.gl.insertOcurance(this.selected_Statut)) {
               this.menu.viewMessage(this.lblMsg, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(this.selected_Statut)) {
            this.menu.viewMessage(this.lblMsg, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate_Statuts();
         this.afficherListe_Statuts();
         this.clearFields_Statuts();
      }

   }

   private void btnNew_StatutsActionPerformed(ActionEvent var1) {
      this.clearFields_Statuts();
   }

   private void tNom_StatutsKeyPressed(KeyEvent var1) {
   }

   private void btnDelete_GrillesalairebasesActionPerformed(ActionEvent var1) {
      if (this.selected_Grillesalairebase != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.lblMsg, "", false);
            if (this.menu.gl.deleteOcurance(this.selected_Grillesalairebase)) {
               this.menu.viewMessage(this.lblMsg, "Element supprim\u00e9", false);
               this.dataListUpdate_Grillesalairebases();
               this.afficherListe_Grillesalairebases();
               this.clearFields_Grillesalairebases();
            }
         }
      } else {
         this.menu.viewMessage(this.lblMsg, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSave_GrillesalairebasesActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.lblMsg, "", false);
      String msg = this.validateData_Grillesalairebases();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.lblMsg, msg, true);
      } else {
         boolean add = false;
         if (this.selected_Grillesalairebase == null) {
            this.selected_Grillesalairebase = new Grillesalairebase();
            add = true;
         }

         this.selected_Grillesalairebase.setStatut((Statut)this.tStatut_Grillesalairebases.getSelectedItem());
         this.selected_Grillesalairebase.setNomCategorie(this.tNomCategorie_Grillesalairebases.getText());
         this.selected_Grillesalairebase.setNiveau(((Number)this.tNiveau_Grillesalairebases.getValue()).intValue());
         this.selected_Grillesalairebase.setCategorie(this.tCategorie_Grillesalairebases.getText());
         this.selected_Grillesalairebase.setSalaireBase(((Number)this.tSalaireBase_Grillesalairebases.getValue()).doubleValue());
         if (add) {
            if (this.menu.gl.insertOcurance(this.selected_Grillesalairebase)) {
               this.menu.viewMessage(this.lblMsg, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(this.selected_Grillesalairebase)) {
            this.menu.viewMessage(this.lblMsg, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate_Grillesalairebases();
         this.afficherListe_Grillesalairebases();
         this.clearFields_Grillesalairebases();
      }

   }

   private void btnNew_GrillesalairebasesActionPerformed(ActionEvent var1) {
      this.clearFields_Grillesalairebases();
   }

   private void tNomCategorie_GrillesalairebasesKeyPressed(KeyEvent var1) {
   }

   private void tCategorie_GrillesalairebasesKeyPressed(KeyEvent var1) {
   }

   private void btnDelete_OriginesActionPerformed(ActionEvent var1) {
      if (this.selected_Origines != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.lblMsg, "", false);
            if (this.menu.gl.deleteOcurance(this.selected_Origines)) {
               this.menu.viewMessage(this.lblMsg, "Element supprim\u00e9", false);
               this.dataListUpdate_Origines();
               this.afficherListe_Origines();
               this.clearFields_Origines();
            }
         }
      } else {
         this.menu.viewMessage(this.lblMsg, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSave_OriginesActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.lblMsg, "", false);
      String msg = this.validateData_Origines();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.lblMsg, msg, true);
      } else {
         boolean add = false;
         if (this.selected_Origines == null) {
            this.selected_Origines = new Origines();
            add = true;
         }

         this.selected_Origines.setLibelle(this.tLibelle_Origines.getText());
         this.selected_Origines.setNbSmighorPourIndConges(((Number)this.tNbSmighorPourIndConges_Origines.getValue()).intValue());
         if (add) {
            if (this.menu.gl.insertOcurance(this.selected_Origines)) {
               this.menu.viewMessage(this.lblMsg, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(this.selected_Origines)) {
            this.menu.viewMessage(this.lblMsg, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate_Origines();
         this.afficherListe_Origines();
         this.clearFields_Origines();
      }

   }

   private void btnNew_OriginesActionPerformed(ActionEvent var1) {
      this.clearFields_Origines();
   }

   private void tLibelle_OriginesKeyPressed(KeyEvent var1) {
   }

   private void btnDelete_MotifsActionPerformed(ActionEvent var1) {
      if (this.selected_Motif != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.lblMsg, "", false);
            if (this.menu.gl.deleteOcurance(this.selected_Motif)) {
               this.menu.viewMessage(this.lblMsg, "Element supprim\u00e9", false);
               this.dataListUpdate_Motifs();
               this.afficherListe_Motifs();
               this.clearFields_Motifs();
            }
         }
      } else {
         this.menu.viewMessage(this.lblMsg, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSave_MotifsActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.lblMsg, "", false);
      String msg = this.validateData_Motifs();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.lblMsg, msg, true);
      } else {
         boolean add = false;
         if (this.selected_Motif == null) {
            this.selected_Motif = new Motif();
            add = true;
         }

         this.selected_Motif.setNom(this.tNom_Motifs.getText());
         this.selected_Motif.setEmployeSoumisCnss(this.tEmployeSoumisCnss_Motifs.isSelected());
         this.selected_Motif.setDeclarationSoumisCnss(this.tDeclarationSoumisCnss_Motifs.isSelected());
         this.selected_Motif.setEmployeSoumisCnam(this.tEmployeSoumisCnam_Motifs.isSelected());
         this.selected_Motif.setDeclarationSoumisCnam(this.tDeclarationSoumisCnam_Motifs.isSelected());
         this.selected_Motif.setEmployeSoumisIts(this.tEmployeSoumisIts_Motifs.isSelected());
         this.selected_Motif.setDeclarationSoumisIts(this.tDeclarationSoumisIts_Motifs.isSelected());
         this.selected_Motif.setActif(this.cActif_Motifs.isSelected());
         if (add) {
            if (this.menu.gl.insertOcurance(this.selected_Motif)) {
               this.menu.viewMessage(this.lblMsg, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(this.selected_Motif)) {
            this.menu.viewMessage(this.lblMsg, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate_Motifs();
         this.afficherListe_Motifs();
         this.clearFields_Motifs();
      }

   }

   private void btnNew_MotifsActionPerformed(ActionEvent var1) {
      this.clearFields_Motifs();
   }

   private void tNom_MotifsKeyPressed(KeyEvent var1) {
   }

   private void tNomCategorie_GrillesalairebasesCaretUpdate(CaretEvent var1) {
      JTextField var10000 = this.tCategorie_Grillesalairebases;
      String var10001 = this.tNomCategorie_Grillesalairebases.getText();
      var10000.setText(var10001 + "-" + this.tNiveau_Grillesalairebases.getText());
   }

   private void tNiveau_GrillesalairebasesCaretUpdate(CaretEvent var1) {
      JTextField var10000 = this.tCategorie_Grillesalairebases;
      String var10001 = this.tNomCategorie_Grillesalairebases.getText();
      var10000.setText(var10001 + "-" + this.tNiveau_Grillesalairebases.getText());
   }

   private void listTable_PostesMouseClicked(MouseEvent var1) {
      int id = ((Number)this.listTable_Postes.getValueAt(this.listTable_Postes.getSelectedRow(), 0)).intValue();
      this.selected_Poste = (Poste)((Set)this.dataListInit_Postes.stream().filter((var1x) -> var1x.getId() == id).collect(Collectors.toSet())).toArray()[0];
      if (this.selected_Poste != null) {
         this.tNom_Postes.setText(this.selected_Poste.getNom());
         this.btnNew_Postes.setEnabled(true);
         this.btnSave_Postes.setEnabled(true);
         this.btnDelete_Postes.setEnabled(true);
      } else {
         this.menu.viewMessage(this.lblMsg, "Acucun resultat!", true);
      }

   }

   private void listTable_ActivitesMouseClicked(MouseEvent var1) {
      int id = ((Number)this.listTable_Activites.getValueAt(this.listTable_Activites.getSelectedRow(), 0)).intValue();
      this.selected_Activite = (Activite)((Set)this.dataListInit_Activites.stream().filter((var1x) -> var1x.getId() == id).collect(Collectors.toSet())).toArray()[0];
      if (this.selected_Activite != null) {
         this.tNom_Activites.setText(this.selected_Activite.getNom());
         this.btnNew_Activites.setEnabled(true);
         this.btnSave_Activites.setEnabled(true);
         this.btnDelete_Activites.setEnabled(true);
      } else {
         this.menu.viewMessage(this.lblMsg, "Acucun resultat!", true);
      }

   }

   private void listTable_StatutsMouseClicked(MouseEvent var1) {
      int id = ((Number)this.listTable_Statuts.getValueAt(this.listTable_Statuts.getSelectedRow(), 0)).intValue();
      this.selected_Statut = (Statut)((Set)this.dataListInit_Statuts.stream().filter((var1x) -> var1x.getId() == id).collect(Collectors.toSet())).toArray()[0];
      if (this.selected_Statut != null) {
         this.tNom_Statuts.setText(this.selected_Statut.getNom());
         this.btnNew_Statuts.setEnabled(true);
         this.btnSave_Statuts.setEnabled(true);
         this.btnDelete_Statuts.setEnabled(true);
      } else {
         this.menu.viewMessage(this.lblMsg, "Acucun resultat!", true);
      }

   }

   private void listTable_GrillesalairebasesMouseClicked(MouseEvent var1) {
      String id = this.listTable_Grillesalairebases.getValueAt(this.listTable_Grillesalairebases.getSelectedRow(), 0).toString();
      this.selected_Grillesalairebase = (Grillesalairebase)((Set)this.dataListInit_Grillesalairebases.stream().filter((var1x) -> var1x.getCategorie().equalsIgnoreCase(id)).collect(Collectors.toSet())).toArray()[0];
      if (this.selected_Grillesalairebase != null) {
         this.tStatut_Grillesalairebases.setSelectedItem(this.selected_Grillesalairebase.getStatut());
         this.tNomCategorie_Grillesalairebases.setText(this.selected_Grillesalairebase.getNomCategorie());
         this.tNiveau_Grillesalairebases.setValue(this.selected_Grillesalairebase.getNiveau());
         this.tCategorie_Grillesalairebases.setText(this.selected_Grillesalairebase.getCategorie());
         this.tSalaireBase_Grillesalairebases.setValue(this.selected_Grillesalairebase.getSalaireBase());
         this.tNomCategorie_Grillesalairebases.setEnabled(false);
         this.tNiveau_Grillesalairebases.setEnabled(false);
         this.btnNew_Grillesalairebases.setEnabled(true);
         this.btnSave_Grillesalairebases.setEnabled(true);
         this.btnDelete_Grillesalairebases.setEnabled(true);
      } else {
         this.menu.viewMessage(this.lblMsg, "Acucun resultat!", true);
      }

   }

   private void listTable_OriginesMouseClicked(MouseEvent var1) {
      int id = ((Number)this.listTable_Origines.getValueAt(this.listTable_Origines.getSelectedRow(), 0)).intValue();
      this.selected_Origines = (Origines)((Set)this.dataListInit_Origines.stream().filter((var1x) -> var1x.getId() == id).collect(Collectors.toSet())).toArray()[0];
      if (this.selected_Origines != null) {
         this.tLibelle_Origines.setText(this.selected_Origines.getLibelle());
         this.tNbSmighorPourIndConges_Origines.setValue(this.selected_Origines.getNbSmighorPourIndConges());
         this.btnNew_Origines.setEnabled(true);
         this.btnSave_Origines.setEnabled(true);
         this.btnDelete_Origines.setEnabled(true);
      } else {
         this.menu.viewMessage(this.lblMsg, "Acucun resultat!", true);
      }

   }

   private void listTable_MotifsMouseClicked(MouseEvent var1) {
      int id = ((Number)this.listTable_Motifs.getValueAt(this.listTable_Motifs.getSelectedRow(), 0)).intValue();
      this.selected_Motif = (Motif)((Set)this.dataListInit_Motifs.stream().filter((var1x) -> var1x.getId() == id).collect(Collectors.toSet())).toArray()[0];
      if (this.selected_Motif != null) {
         this.tNom_Motifs.setText(this.selected_Motif.getNom());
         this.tEmployeSoumisCnss_Motifs.setSelected(this.selected_Motif.isEmployeSoumisCnss());
         this.tDeclarationSoumisCnss_Motifs.setSelected(this.selected_Motif.isDeclarationSoumisCnss());
         this.tEmployeSoumisCnam_Motifs.setSelected(this.selected_Motif.isEmployeSoumisCnam());
         this.tDeclarationSoumisCnam_Motifs.setSelected(this.selected_Motif.isDeclarationSoumisCnam());
         this.tEmployeSoumisIts_Motifs.setSelected(this.selected_Motif.isEmployeSoumisIts());
         this.tDeclarationSoumisIts_Motifs.setSelected(this.selected_Motif.isDeclarationSoumisIts());
         this.cActif_Motifs.setSelected(this.selected_Motif.isActif());
         this.btnNew_Motifs.setEnabled(true);
         this.btnSave_Motifs.setEnabled(true);
         this.btnDelete_Motifs.setEnabled(true);
      } else {
         this.menu.viewMessage(this.lblMsg, "Acucun resultat!", true);
      }

   }

   private void listTable_BanquesMouseClicked(MouseEvent var1) {
      int id = ((Number)this.listTable_Banques.getValueAt(this.listTable_Banques.getSelectedRow(), 0)).intValue();
      this.selected_Banque = (Banque)((Set)this.dataListInit_Banques.stream().filter((var1x) -> var1x.getId() == id).collect(Collectors.toSet())).toArray()[0];
      if (this.selected_Banque != null) {
         this.tNom_Banques.setText(this.selected_Banque.getNom());
         this.tNoCompteCompta_Banques.setValue(this.selected_Banque.getNoCompteCompta());
         this.tNoChapitreCompta_Banques.setValue(this.selected_Banque.getNoChapitreCompta());
         this.tNoCompteComptaCle_Banques.setText(this.selected_Banque.getNoCompteComptaCle());
         this.btnNew_Banques.setEnabled(true);
         this.btnSave_Banques.setEnabled(true);
         this.btnDelete_Banques.setEnabled(true);
      } else {
         this.menu.viewMessage(this.lblMsg, "Acucun resultat!", true);
      }

   }

   private void listTable_DepartementsMouseClicked(MouseEvent var1) {
      int id = ((Number)this.listTable_Departements.getValueAt(this.listTable_Departements.getSelectedRow(), 0)).intValue();
      this.selected_Departement = (Departement)((Set)this.dataListInit_Departements.stream().filter((var1x) -> var1x.getId() == id).collect(Collectors.toSet())).toArray()[0];
      if (this.selected_Departement != null) {
         this.tNom_Departemet.setText(this.selected_Departement.getNom());
         this.btnNew_Departements.setEnabled(true);
         this.btnSave_Departements.setEnabled(true);
         this.btnDelete_Departements.setEnabled(true);
      } else {
         this.menu.viewMessage(this.lblMsg, "Acucun resultat!", true);
      }

   }

   private void tNoChapitreCompta_BanquesCaretUpdate(CaretEvent var1) {
   }

   private void tNoCompteCompta_BanquesCaretUpdate(CaretEvent var1) {
   }

   private void tNoCompteComptaCle_BanquesKeyPressed(KeyEvent var1) {
   }

   private void btnDelete_DirectiongeneralActionPerformed(ActionEvent var1) {
      if (this.selected_Directiongeneral != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.lblMsg, "", false);
            if (this.menu.gl.deleteOcurance(this.selected_Directiongeneral)) {
               this.menu.viewMessage(this.lblMsg, "Element supprim\u00e9", false);
               this.dataListUpdate_Directiongeneral();
               this.afficherListe_Directiongeneral();
               this.clearFields_Directiongeneral();
            }
         }
      } else {
         this.menu.viewMessage(this.lblMsg, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSave_DirectiongeneralActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.lblMsg, "", false);
      String msg = this.validateData_Directiongeneral();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.lblMsg, msg, true);
      } else {
         boolean add = false;
         if (this.selected_Directiongeneral == null) {
            this.selected_Directiongeneral = new Directiongeneral();
            add = true;
         }

         this.selected_Directiongeneral.setNom(this.tNom_Directiongeneral.getText());
         if (add) {
            if (this.menu.gl.insertOcurance(this.selected_Directiongeneral)) {
               this.menu.viewMessage(this.lblMsg, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(this.selected_Directiongeneral)) {
            this.menu.viewMessage(this.lblMsg, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate_Directiongeneral();
         this.afficherListe_Directiongeneral();
         this.clearFields_Directiongeneral();
      }

   }

   private void btnNew_DirectiongeneralActionPerformed(ActionEvent var1) {
      this.clearFields_Directiongeneral();
   }

   private void tNom_DirectiongeneralKeyPressed(KeyEvent var1) {
   }

   private void listTable_DirectiongeneralMouseClicked(MouseEvent var1) {
      int id = ((Number)this.listTable_Directiongeneral.getValueAt(this.listTable_Directiongeneral.getSelectedRow(), 0)).intValue();
      this.selected_Directiongeneral = (Directiongeneral)((Set)this.dataListInit_Directiongeneral.stream().filter((var1x) -> var1x.getId() == id).collect(Collectors.toSet())).toArray()[0];
      if (this.selected_Directiongeneral != null) {
         this.tNom_Directiongeneral.setText(this.selected_Directiongeneral.getNom());
         this.btnNew_Directiongeneral.setEnabled(true);
         this.btnSave_Directiongeneral.setEnabled(true);
         this.btnDelete_Directiongeneral.setEnabled(true);
      } else {
         this.menu.viewMessage(this.lblMsg, "Acucun resultat!", true);
      }

   }

   private void btnDelete_DirectionActionPerformed(ActionEvent var1) {
      if (this.selected_Direction != null) {
         int rep = JOptionPane.showConfirmDialog(this, " Confirmez vous la suppression ?", " Confirmation de supression", 0);
         if (rep == 0) {
            this.menu.viewMessage(this.lblMsg, "", false);
            if (this.menu.gl.deleteOcurance(this.selected_Direction)) {
               this.menu.viewMessage(this.lblMsg, "Element supprim\u00e9", false);
               this.dataListUpdate_Direction();
               this.afficherListe_Direction();
               this.clearFields_Direction();
            }
         }
      } else {
         this.menu.viewMessage(this.lblMsg, "Aucun element selection\u00e9", true);
      }

   }

   private void btnSave_DirectionActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.lblMsg, "", false);
      String msg = this.validateData_Direction();
      if (!msg.isEmpty()) {
         this.menu.viewMessage(this.lblMsg, msg, true);
      } else {
         boolean add = false;
         if (this.selected_Direction == null) {
            this.selected_Direction = new Direction();
            add = true;
         }

         this.selected_Direction.setNom(this.tNom_Direction.getText());
         if (add) {
            if (this.menu.gl.insertOcurance(this.selected_Direction)) {
               this.menu.viewMessage(this.lblMsg, "Element ajout\u00e9", false);
            } else {
               this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
            }
         } else if (this.menu.gl.updateOcurance(this.selected_Direction)) {
            this.menu.viewMessage(this.lblMsg, "Element modifi\u00e9", false);
         } else {
            this.menu.viewMessage(this.lblMsg, "Err: Op\u00e9ration echou\u00e9e", true);
         }

         this.dataListUpdate_Direction();
         this.afficherListe_Direction();
         this.clearFields_Direction();
      }

   }

   private void btnNew_DirectionActionPerformed(ActionEvent var1) {
      this.clearFields_Direction();
   }

   private void tNom_DirectionKeyPressed(KeyEvent var1) {
   }

   private void listTable_DirectionMouseClicked(MouseEvent var1) {
      int id = ((Number)this.listTable_Direction.getValueAt(this.listTable_Direction.getSelectedRow(), 0)).intValue();
      this.selected_Direction = (Direction)((Set)this.dataListInit_Direction.stream().filter((var1x) -> var1x.getId() == id).collect(Collectors.toSet())).toArray()[0];
      if (this.selected_Direction != null) {
         this.tNom_Direction.setText(this.selected_Direction.getNom());
         this.btnNew_Direction.setEnabled(true);
         this.btnSave_Direction.setEnabled(true);
         this.btnDelete_Direction.setEnabled(true);
      } else {
         this.menu.viewMessage(this.lblMsg, "Acucun resultat!", true);
      }

   }
}
