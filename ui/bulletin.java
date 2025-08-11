package com.mccmr.ui;

import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.FichePaieDetail;
import com.mccmr.util.ModelClass;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class bulletin extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   int nbElements;
   private JButton btnExit;
   private JButton btnExportPDF;
   private JButton btnPrint;
   private JButton btnSendByMail;
   private JButton btnShow;
   private JCheckBox cCheckAll;
   private JCheckBox cPrinterDialog;
   private JLabel jLabel20;
   private JLabel jLabel23;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JScrollPane jScrollPane9;
   private JTable listTable;
   private JLabel msgLabel;
   private JPanel pnlBody;
   private JPanel pnlFooter;
   private JPanel pnlMotifs;
   private JPanel pnlMotifs_Edit;
   private JPanel pnlMotifs_List;
   private JProgressBar progressBar1;
   private JComboBox<Object> tMotif;
   private JLabel tNbElements;
   private JComboBox<Object> tPeriode;

   public bulletin() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExportPDF.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FOLDER_SHARED, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnPrint.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PRINT, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnSendByMail.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.EMAIL, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnShow.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.GET_APP, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.menu.remplirCombo("Motif", this.tMotif);
      this.menu.remplirCombo("Periode", this.tPeriode);
      Query q = this.menu.entityManager.createQuery("Select p from Paie p");
      q.setMaxResults(1000000);
      this.dlPaie = q.getResultList();
   }

   private void afficherListe() {
      Motif motif = (Motif)this.tMotif.getSelectedItem();
      Date periode = (Date)this.tPeriode.getSelectedItem();
      this.nbElements = 0;
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmBulletinPaie(var10003));
      if (motif != null && periode != null) {
         List<Paie> dl = this.dlPaie;

         for(Paie rs : (List)dl.stream().filter((var3x) -> this.menu.df.format(var3x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode)) && var3x.getMotif().getId() == motif.getId()).sorted(Comparator.comparing((var0) -> var0.getEmploye().getId())).collect(Collectors.toList())) {
            ((ModelClass.tmBulletinPaie)this.listTable.getModel()).addRow(rs);
            ++this.nbElements;
         }

         this.listTable.getColumnModel().getColumn(0).setPreferredWidth(5);
         this.listTable.getColumnModel().getColumn(1).setPreferredWidth(50);
         this.listTable.getColumnModel().getColumn(2).setPreferredWidth(200);
         this.listTable.setRowHeight(30);
         this.listTable.setAutoCreateRowSorter(true);
         TableRowSorter<TableModel> sorter = new TableRowSorter(this.listTable.getModel());
         this.listTable.setRowSorter(sorter);
         sorter.setSortsOnUpdates(true);
         this.menu.mc.getClass();
         ModelClass var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(1, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(2, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(3, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(4, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(5, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(6, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(7, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(8, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(9, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(10, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(11, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(12, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(13, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(14, new ModelClass.NumberComparator(var10004));
         this.menu.mc.getClass();
         var10004 = this.menu.mc;
         Objects.requireNonNull(var10004);
         sorter.setComparator(15, new ModelClass.NumberComparator(var10004));
         this.tNbElements.setText("0/" + this.nbElements);
      }

      this.menu.viewMessage(this.msgLabel, "", false);
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.pnlMotifs = new JPanel();
      this.pnlMotifs_Edit = new JPanel();
      this.tPeriode = new JComboBox();
      this.jLabel20 = new JLabel();
      this.tMotif = new JComboBox();
      this.jLabel23 = new JLabel();
      this.cCheckAll = new JCheckBox();
      this.btnShow = new JButton();
      this.btnExportPDF = new JButton();
      this.btnSendByMail = new JButton();
      this.tNbElements = new JLabel();
      this.btnPrint = new JButton();
      this.cPrinterDialog = new JCheckBox();
      this.pnlMotifs_List = new JPanel();
      this.jScrollPane9 = new JScrollPane();
      this.listTable = new JTable();
      this.pnlFooter = new JPanel();
      this.msgLabel = new JLabel();
      this.progressBar1 = new JProgressBar();
      this.setBackground(new Color(255, 255, 255));
      this.setBorder((Border)null);
      this.setAutoscrolls(true);
      this.setFont(new Font("Segoe UI Light", 0, 12));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(1000, 600));
      this.jPanel1.setBackground(new Color(255, 255, 255));
      this.jLabel7.setFont(new Font("Segoe UI Light", 0, 24));
      this.jLabel7.setForeground(new Color(0, 102, 153));
      this.jLabel7.setText("Bulletins de paie");
      this.jLabel7.setToolTipText("");
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addGap(572, 572, 572).addComponent(this.btnExit, -2, 39, -2).addGap(19, 19, 19)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.pnlMotifs.setBackground(new Color(255, 255, 255));
      this.pnlMotifs_Edit.setBackground(new Color(255, 255, 255));
      this.tPeriode.setFont(new Font("Segoe UI Light", 0, 12));
      this.tPeriode.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriode.addActionListener(new 2(this));
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("P\u00e9riode");
      this.tMotif.setFont(new Font("Segoe UI Light", 0, 12));
      this.tMotif.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tMotif.addActionListener(new 3(this));
      this.jLabel23.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel23.setForeground(new Color(0, 102, 153));
      this.jLabel23.setText("Motif");
      this.cCheckAll.setBackground(new Color(255, 255, 255));
      this.cCheckAll.setFont(new Font("Segoe UI Light", 0, 12));
      this.cCheckAll.setForeground(new Color(0, 102, 153));
      this.cCheckAll.setText("Tout cocher");
      this.cCheckAll.addActionListener(new 4(this));
      this.btnShow.setBackground(new Color(255, 255, 255));
      this.btnShow.setToolTipText("Afficher");
      this.btnShow.setContentAreaFilled(false);
      this.btnShow.setCursor(new Cursor(12));
      this.btnShow.setOpaque(true);
      this.btnShow.addActionListener(new 5(this));
      this.btnExportPDF.setBackground(new Color(255, 255, 255));
      this.btnExportPDF.setToolTipText("Generer les PDF");
      this.btnExportPDF.setContentAreaFilled(false);
      this.btnExportPDF.setCursor(new Cursor(12));
      this.btnExportPDF.setOpaque(true);
      this.btnExportPDF.addActionListener(new 6(this));
      this.btnSendByMail.setBackground(new Color(255, 255, 255));
      this.btnSendByMail.setToolTipText("Envoyer par e-mail");
      this.btnSendByMail.setContentAreaFilled(false);
      this.btnSendByMail.setCursor(new Cursor(12));
      this.btnSendByMail.setOpaque(true);
      this.btnSendByMail.addActionListener(new 7(this));
      this.tNbElements.setFont(new Font("Segoe UI Light", 1, 10));
      this.tNbElements.setForeground(new Color(0, 102, 153));
      this.tNbElements.setText("...");
      this.btnPrint.setBackground(new Color(255, 255, 255));
      this.btnPrint.setToolTipText("Imprimer");
      this.btnPrint.setContentAreaFilled(false);
      this.btnPrint.setCursor(new Cursor(12));
      this.btnPrint.setOpaque(true);
      this.btnPrint.addActionListener(new 8(this));
      this.cPrinterDialog.setBackground(new Color(255, 255, 255));
      this.cPrinterDialog.setFont(new Font("Segoe UI Light", 0, 12));
      this.cPrinterDialog.setForeground(new Color(0, 102, 153));
      this.cPrinterDialog.setSelected(true);
      this.cPrinterDialog.setText("Imprimante par d\u00e9faut");
      this.cPrinterDialog.addActionListener(new 9(this));
      GroupLayout pnlMotifs_EditLayout = new GroupLayout(this.pnlMotifs_Edit);
      this.pnlMotifs_Edit.setLayout(pnlMotifs_EditLayout);
      pnlMotifs_EditLayout.setHorizontalGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addContainerGap().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriode, 0, 144, 32767).addComponent(this.jLabel20, -1, -1, 32767)).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tMotif, -2, 300, -2).addComponent(this.jLabel23, -2, 300, -2)).addGap(61, 61, 61).addComponent(this.btnShow, -2, 35, -2).addGap(18, 18, 18).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tNbElements, -1, -1, 32767).addComponent(this.cCheckAll)).addGap(27, 27, 27).addComponent(this.btnExportPDF, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnSendByMail, -2, 35, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnPrint, -2, 35, -2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.cPrinterDialog, -2, 190, -2).addGap(0, 250, 32767)));
      pnlMotifs_EditLayout.setVerticalGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.TRAILING).addComponent(this.cPrinterDialog, -2, 17, -2).addComponent(this.btnSendByMail, -2, 35, -2).addComponent(this.btnExportPDF, -2, 35, -2).addComponent(this.btnShow, -2, 35, -2).addComponent(this.btnPrint, -2, 35, -2).addComponent(this.cCheckAll).addGroup(pnlMotifs_EditLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel23).addGap(0, 0, 0).addComponent(this.tMotif, -2, 30, -2)).addGroup(pnlMotifs_EditLayout.createSequentialGroup().addComponent(this.jLabel20).addGap(0, 0, 0).addComponent(this.tPeriode, -2, 30, -2)))).addGap(0, 0, 0).addComponent(this.tNbElements)));
      this.pnlMotifs_List.setBackground(new Color(255, 255, 255));
      this.listTable.setFont(new Font("Segoe UI Light", 0, 13));
      this.listTable.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
      this.listTable.setSelectionBackground(new Color(0, 102, 153));
      this.listTable.setShowGrid(false);
      this.listTable.addMouseListener(new 10(this));
      this.jScrollPane9.setViewportView(this.listTable);
      GroupLayout pnlMotifs_ListLayout = new GroupLayout(this.pnlMotifs_List);
      this.pnlMotifs_List.setLayout(pnlMotifs_ListLayout);
      pnlMotifs_ListLayout.setHorizontalGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane9, -2, 1114, -2).addContainerGap(27, 32767)));
      pnlMotifs_ListLayout.setVerticalGroup(pnlMotifs_ListLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifs_ListLayout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane9, -2, -1, -2).addGap(0, 0, 0)));
      GroupLayout pnlMotifsLayout = new GroupLayout(this.pnlMotifs);
      this.pnlMotifs.setLayout(pnlMotifsLayout);
      pnlMotifsLayout.setHorizontalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlMotifs_List, -2, -1, -2).addGroup(pnlMotifsLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlMotifs_Edit, -2, -1, -2))).addContainerGap(-1, 32767)));
      pnlMotifsLayout.setVerticalGroup(pnlMotifsLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlMotifsLayout.createSequentialGroup().addContainerGap().addComponent(this.pnlMotifs_Edit, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pnlMotifs_List, -1, -1, 32767).addGap(0, 0, 0)));
      this.pnlFooter.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout pnlFooterLayout = new GroupLayout(this.pnlFooter);
      this.pnlFooter.setLayout(pnlFooterLayout);
      pnlFooterLayout.setHorizontalGroup(pnlFooterLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlFooterLayout.createSequentialGroup().addComponent(this.msgLabel, -1, 1132, 32767).addContainerGap()));
      pnlFooterLayout.setVerticalGroup(pnlFooterLayout.createParallelGroup(Alignment.LEADING).addComponent(this.msgLabel, -1, 21, 32767));
      this.progressBar1.setBackground(new Color(204, 204, 204));
      this.progressBar1.setForeground(new Color(0, 102, 153));
      this.progressBar1.setBorderPainted(false);
      this.progressBar1.setOpaque(true);
      this.progressBar1.setStringPainted(true);
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.pnlFooter, -2, -1, -2).addGap(53, 53, 53)).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlMotifs, -2, 1125, -2).addComponent(this.progressBar1, -2, 1133, -2)).addContainerGap(-1, 32767)));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.progressBar1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlMotifs, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlFooter, -2, -1, -2).addContainerGap()));
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.pnlBody, -2, 1148, -2).addComponent(this.jPanel1, -2, -1, -2)).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -2, -1, -2).addGap(0, 0, 0)));
      this.setSize(new Dimension(1133, 629));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void btnSendByMailActionPerformed(ActionEvent var1) {
      this.btnSendByMail.setEnabled(false);
      Thread t = new 11(this);
      t.start();
      this.btnSendByMail.setEnabled(true);
   }

   Map<Object, Object> setFichePaieParams(Paie var1) {
      Map<Object, Object> param = new HashMap();
      List<Rubriquepaie> dl = new ArrayList(this.menu.pc.empRubriquepaie(paie.getEmploye(), paie.getMotif(), paie.getPeriode()));
      List var5 = (List)dl.stream().sorted(Comparator.comparing((var0) -> var0.getRubrique().getId())).collect(Collectors.toList());
      List<FichePaieDetail> bulletinPaieDetail = new ArrayList();
      var5.forEach((var1x) -> {
         FichePaieDetail pd = new FichePaieDetail();
         pd.setCodeRub(var1x.getRubrique().getId());
         pd.setLibelle(var1x.getRubrique().getLibelle());
         pd.setSens(var1x.getRubrique().getSens());
         pd.setBase(var1x.getBase());
         pd.setNombre(var1x.getNombre());
         pd.setMontant(var1x.getMontant());
         bulletinPaieDetail.add(pd);
      });
      param.put("paie_categorie", paie.getCategorie());
      param.put("paie_BT", paie.getBt());
      param.put("paie_BNI", paie.getBni());
      param.put("paie_CNSS", paie.getCnss());
      param.put("paie_ITS", paie.getIts());
      param.put("paie_retenuesBrut", paie.getRetenuesBrut());
      param.put("paie_retenuesNet", paie.getRetenuesNet());
      param.put("paie_RITS", paie.getRits());
      param.put("paie_net", paie.getNet());
      param.put("paie_njt", paie.getNjt());
      param.put("paie_nbrHS", paie.getNbrHs());
      param.put("paie_CNAM", paie.getCnam());
      param.put("paie_periodeLettre", paie.getPeriodeLettre());
      param.put("paie_noCompteBanque", paie.getNoCompteBanque());
      param.put("paie_modePaiement", paie.getModePaiement());
      param.put("paie_domicilie", paie.isDomicilie());
      param.put("paie_paieDu", paie.getPaieDu());
      param.put("paie_paieAu", paie.getPaieAu());
      param.put("paie_biCNAM", paie.getBiCnam());
      param.put("paie_biCNSS", paie.getBiCnss());
      param.put("paie_RCNSS", paie.getRcnss());
      param.put("paie_RCNAM", paie.getRcnam());
      param.put("paie_biAVNAT", paie.getBiAvnat());
      param.put("paie_notePaie", paie.getNotePaie());
      param.put("motif_nom", paie.getMotif().getNom());
      param.put("paie_contratHeureMois", paie.getContratHeureMois());
      param.put("paie_banque", paie.getBanque());
      param.put("paie_poste", paie.getPoste());
      param.put("paie_directiongenerale", paie.getDirectiongeneral());
      param.put("paie_direction", paie.getDirection());
      param.put("paie_departement", paie.getDepartement());
      param.put("paie_activite", paie.getActivite());
      param.put("employe_id", paie.getEmploye().getId());
      param.put("employe_prenom", paie.getEmploye().getPrenom());
      param.put("employe_nom", paie.getEmploye().getNom());
      param.put("employe_noCNSS", paie.getEmploye().getNoCnss());
      param.put("employe_noCNAM", paie.getEmploye().getNoCnam());
      param.put("employe_contratHeureSemaine", paie.getEmploye().getContratHeureSemaine());
      param.put("employe_dateAnciennete", paie.getEmploye().getDateAnciennete());
      param.put("employe_idPsservice", paie.getEmploye().getIdPsservice());
      param.put("employe_nni", paie.getEmploye().getNni());
      param.put("employe_dateEmbauche", paie.getEmploye().getDateEmbauche());
      param.put("employe_idPsservice", paie.getEmploye().getIdPsservice());
      param.put("employe_indice", "" + paie.getEmploye().getTauxPsra());
      param.put("DS1", bulletinPaieDetail);
      return param;
   }

   private void btnExportPDFActionPerformed(ActionEvent var1) {
      Thread t = new 12(this);
      t.start();
   }

   private void listTableMouseClicked(MouseEvent var1) {
      JLabel var10000 = this.tNbElements;
      int var10001 = this.menu.gl.selectedLinesSize(this.listTable);
      var10000.setText(var10001 + "/" + this.nbElements);
   }

   private void btnShowActionPerformed(ActionEvent var1) {
      Thread t = new 13(this);
      t.start();
   }

   private void cCheckAllActionPerformed(ActionEvent var1) {
      for(int i = 0; i < this.listTable.getRowCount(); ++i) {
         ((ModelClass.tmBulletinPaie)this.listTable.getModel()).setValueAt(this.cCheckAll.isSelected(), i, 0);
         JLabel var10000 = this.tNbElements;
         int var10001 = this.menu.gl.selectedLinesSize(this.listTable);
         var10000.setText(var10001 + "/" + this.nbElements);
      }

   }

   private void tPeriodeActionPerformed(ActionEvent var1) {
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmBulletinPaie(var10003));
   }

   private void tMotifActionPerformed(ActionEvent var1) {
      JTable var10000 = this.listTable;
      ModelClass var10003 = this.menu.mc;
      Objects.requireNonNull(var10003);
      var10000.setModel(new ModelClass.tmBulletinPaie(var10003));
   }

   private void btnPrintActionPerformed(ActionEvent var1) {
      Thread t = new 14(this);
      t.start();
   }

   private void cPrinterDialogActionPerformed(ActionEvent var1) {
   }
}
