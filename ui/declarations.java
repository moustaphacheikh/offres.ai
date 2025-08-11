package com.mccmr.ui;

import com.mccmr.entity.Employe;
import com.mccmr.entity.Listenominativecnam;
import com.mccmr.entity.Listenominativecnss;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.icon.GoogleMaterialDesignIcons;
import com.mccmr.icon.IconFontSwing;
import com.mccmr.util.WriteExcel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class declarations extends JInternalFrame {
   public menu menu;
   List<Paie> dlPaie;
   List<Motif> dlMotif;
   List<Listenominativecnss> lnCnssDS;
   List<Listenominativecnam> lnCnamDS;
   private JButton btnExit;
   private JButton btnExport;
   private JButton btnRefresh;
   private JCheckBox cCNSSPersonnelDeclare;
   private JLabel jLabel20;
   private JLabel jLabel25;
   private JLabel jLabel7;
   private JPanel jPanel1;
   private JLabel msgLabel;
   private JPanel pnlBody;
   private JProgressBar progressBar;
   private JComboBox<Object> tEtat;
   private JComboBox<Object> tPeriode;
   private JPanel tabbedPaneOptions;

   public declarations() {
      this.initComponents();
      this.initIcons();
   }

   private void initIcons() {
      this.btnExit.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnRefresh.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.REFRESH, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
      this.btnExport.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_NEW, 27.0F, new Color(0, 0, 51), new Color(255, 255, 255)));
   }

   public void refresh() {
      this.menu.remplirCombo("Periode", this.tPeriode);
      Query q = this.menu.entityManager.createQuery("Select p from Paie p");
      q.setMaxResults(1000000);
      this.dlPaie = q.getResultList();
      this.dlMotif = new ArrayList(this.menu.stricturesIF.dataListInit_Motifs);
      this.tabbedPaneOptions.setVisible(false);
   }

   private void decCNSS() {
      Double plafonTotal = (double)0.0F;
      Double cnss1erePeriode = (double)0.0F;
      Double cnss2emePeriode = (double)0.0F;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date periode3emeMoisTrim = (Date)this.tPeriode.getSelectedItem();
      Date periode2emeMoisTrim = this.menu.gl.addRetriveDays(periode3emeMoisTrim, -30);
      Date periode1erMoisTrim = this.menu.gl.addRetriveDays(periode2emeMoisTrim, -30);
      List<Employe> dl = this.menu.employeFrame.dataListInit;
      dl = (List)dl.stream().filter((var1x) -> !var1x.isDetacheCnss() && (!this.cCNSSPersonnelDeclare.isSelected() || !var1x.getNoCnss().isEmpty())).sorted(Comparator.comparing(Employe::getId)).collect(Collectors.toList());

      try {
         periode2emeMoisTrim = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periode2emeMoisTrim));
         periode1erMoisTrim = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periode1erMoisTrim));
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.lnCnssDS = new ArrayList();
      int nbSal = this.lnCnssDS.size();

      for(Employe rs : dl) {
         Double plafon = (double)0.0F;
         Double ri = (double)0.0F;
         Double njt3emePeriode = (double)0.0F;
         Double njt2emePeriode = njt3emePeriode;
         Double njt1erePeriode = njt3emePeriode;
         String njt3emePeriodeStr = "0";
         String njt2emePeriodeStr = njt3emePeriodeStr;
         String njt1erePeriodeStr = njt3emePeriodeStr;
         Paie paieByPeriode = this.menu.pc.paieById(rs, this.menu.motifSN, periode1erMoisTrim);
         if (paieByPeriode != null) {
            njt1erePeriode = paieByPeriode.getNjt();
            njt1erePeriodeStr = njt1erePeriode.toString();
         }

         paieByPeriode = this.menu.pc.paieById(rs, this.menu.motifSN, periode2emeMoisTrim);
         if (paieByPeriode != null) {
            njt2emePeriode = paieByPeriode.getNjt();
            njt2emePeriodeStr = njt2emePeriode.toString();
         }

         paieByPeriode = this.menu.pc.paieById(rs, this.menu.motifSN, periode3emeMoisTrim);
         if (paieByPeriode != null) {
            njt3emePeriode = paieByPeriode.getNjt();
            njt3emePeriodeStr = njt3emePeriode.toString();
         }

         Double totNjt = njt1erePeriode + njt2emePeriode + njt3emePeriode;
         if (rs.getDateEmbauche().before(periode1erMoisTrim) && rs.isActif()) {
            if (njt1erePeriode == (double)0.0F) {
               njt1erePeriodeStr = "CS";
            }

            if (njt2emePeriode == (double)0.0F) {
               njt2emePeriodeStr = "CS";
            }

            if (njt3emePeriode == (double)0.0F) {
               njt3emePeriodeStr = "CS";
            }
         }

         for(Motif rs2 : this.dlMotif) {
            if (rs2.isDeclarationSoumisCnss()) {
               paieByPeriode = this.menu.pc.paieById(rs, rs2, periode1erMoisTrim);
               if (paieByPeriode != null) {
                  cnss1erePeriode = cnss1erePeriode + paieByPeriode.getCnss();
               }

               paieByPeriode = this.menu.pc.paieById(rs, rs2, periode2emeMoisTrim);
               if (paieByPeriode != null) {
                  cnss2emePeriode = cnss2emePeriode + paieByPeriode.getCnss();
               }

               Double biCnss = (double)0.0F;
               paieByPeriode = this.menu.pc.paieById(rs, rs2, periode1erMoisTrim);
               if (paieByPeriode != null) {
                  biCnss = paieByPeriode.getBiCnss();
               }

               ri = ri + biCnss;
               if (biCnss >= (double)15000.0F) {
                  plafon = plafon + (double)15000.0F;
               } else {
                  plafon = plafon + biCnss;
               }

               biCnss = (double)0.0F;
               paieByPeriode = this.menu.pc.paieById(rs, rs2, periode2emeMoisTrim);
               if (paieByPeriode != null) {
                  biCnss = paieByPeriode.getBiCnss();
               }

               ri = ri + biCnss;
               if (biCnss >= (double)15000.0F) {
                  plafon = plafon + (double)15000.0F;
               } else {
                  plafon = plafon + biCnss;
               }

               biCnss = (double)0.0F;
               paieByPeriode = this.menu.pc.paieById(rs, rs2, periode3emeMoisTrim);
               if (paieByPeriode != null) {
                  biCnss = paieByPeriode.getBiCnss();
               }

               ri = ri + biCnss;
               if (biCnss >= (double)15000.0F) {
                  plafon = plafon + (double)15000.0F;
               } else {
                  plafon = plafon + biCnss;
               }
            }
         }

         plafonTotal = plafonTotal + plafon;
         if (ri > (double)0.0F && plafon > (double)0.0F) {
            String dateCNSS = this.menu.fdf.format(rs.getDateCnss() != null ? rs.getDateCnss() : rs.getDateEmbauche());
            String var10000 = rs.getPrenom().replace("'", "''");
            String salName = var10000 + " " + rs.getNom().replace("'", "''");
            Listenominativecnss ln = new Listenominativecnss();
            ln.setDateDebauche(rs.getDateDebauche() != null ? this.menu.fdf.format(rs.getDateDebauche()) : "-");
            ln.setDateEmbauche(dateCNSS);
            ln.setNbJour1erMois(njt1erePeriodeStr);
            ln.setNbJour2emeMois(njt2emePeriodeStr);
            ln.setNbJour3emeMois(njt3emePeriodeStr);
            ln.setNoCnssemploye(rs.getNoCnss());
            ln.setNomEmploye(salName + " - (" + rs.getId() + ")");
            ln.setPlafond(plafon);
            ln.setRemunerationReeles(ri);
            ln.setTotalNbJour(totNjt);
            this.lnCnssDS.add(ln);
         }
      }

      int anneeDec = new Integer((new SimpleDateFormat("yyyy")).format((Date)this.tPeriode.getSelectedItem()));
      int noTrimestre = new Integer((new SimpleDateFormat("M")).format((Date)this.tPeriode.getSelectedItem())) / 3;
      Double partieEmploye = plafonTotal / (double)100.0F;
      Double partieMedecine = partieEmploye * (double)2.0F;
      Double partiePatronal = partieEmploye * (double)13.0F;
      Double total = partieEmploye * (double)16.0F;
      cnss1erePeriode = cnss1erePeriode * (double)16.0F;
      cnss2emePeriode = cnss2emePeriode * (double)16.0F;
      Double aVerser = total - cnss1erePeriode - cnss2emePeriode;
      this.menu.viewMessage(this.msgLabel, "", false);
      Map<Object, Object> param = new HashMap();
      param.put("DECLARATIONCNSSTRIM_PERIODE", sdf.format((Date)this.tPeriode.getSelectedItem()));
      param.put("DECLARATIONCNSSTRIM_ANNEE", anneeDec);
      param.put("DECLARATIONCNSSTRIM_TRIMESTRE", noTrimestre);
      param.put("DECLARATIONCNSSTRIM_PLAFONDTOT", plafonTotal);
      param.put("DECLARATIONCNSSTRIM_CNSS1EREPE", cnss1erePeriode);
      param.put("DECLARATIONCNSSTRIM_CNSS2EMEPE", cnss2emePeriode);
      param.put("DECLARATIONCNSSTRIM_PARTIEEMPL", partieEmploye);
      param.put("DECLARATIONCNSSTRIM_PARTIEPATR", partiePatronal);
      param.put("DECLARATIONCNSSTRIM_PARTIEMEDE", partieMedecine);
      param.put("DECLARATIONCNSSTRIM_TOTAL", total);
      param.put("DECLARATIONCNSSTRIM_AVERSER", aVerser);
      param.put("DECLARATIONCNSSTRIM_NBSAL", this.lnCnssDS.size());
      param.put("DS1", this.lnCnssDS);

      try {
         this.menu.gl.afficherReportParamOnly("declarationCNSS", param);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private void decCNAM() {
      String date_limite_rappel_str = " ";
      Double masse_salariale_soumise1_total = (double)0.0F;
      Double masse_salariale_soumise2_total = (double)0.0F;
      Double masse_salariale_soumise3_total = (double)0.0F;
      int no = 0;
      Date periode3emeMoisTrim = (Date)this.tPeriode.getSelectedItem();
      Date periode2emeMoisTrim = this.menu.gl.addRetriveDays(periode3emeMoisTrim, -30);
      Date periode1erMoisTrim = this.menu.gl.addRetriveDays(periode2emeMoisTrim, -30);
      String mois1 = (new SimpleDateFormat("MMMM")).format(periode1erMoisTrim).toUpperCase();
      String mois2 = (new SimpleDateFormat("MMMM")).format(periode2emeMoisTrim).toUpperCase();
      String mois3 = (new SimpleDateFormat("MMMM")).format(periode3emeMoisTrim).toUpperCase();

      try {
         Date date_limite_rappel = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-15")).format(this.menu.gl.addRetriveDays(periode3emeMoisTrim, 15)));
         date_limite_rappel_str = (new SimpleDateFormat("dd MMMMM yyyy")).format(date_limite_rappel).toUpperCase();
      } catch (Exception e) {
         e.printStackTrace();
      }

      try {
         periode2emeMoisTrim = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periode2emeMoisTrim));
         periode1erMoisTrim = (new SimpleDateFormat("yyyy-MM-dd")).parse((new SimpleDateFormat("yyyy-MM-28")).format(periode1erMoisTrim));
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.lnCnamDS = new ArrayList();
      List<Employe> dl = this.menu.employeFrame.dataListInit;

      for(Employe rs : (List)dl.stream().filter((var0) -> !var0.isDetacheCnam()).sorted(Comparator.comparing(Employe::getId)).collect(Collectors.toList())) {
         Double assiete_soumise_mois3 = (double)0.0F;
         Double assiete_soumise_mois2 = assiete_soumise_mois3;
         Double assiete_soumise_mois1 = assiete_soumise_mois3;
         Paie paie = this.menu.pc.paieById(rs, this.menu.motifSN, periode1erMoisTrim);
         Double nb_jour_mois1 = paie != null ? paie.getNjt() : (double)0.0F;
         paie = this.menu.pc.paieById(rs, this.menu.motifSN, periode2emeMoisTrim);
         Double nb_jour_mois2 = paie != null ? paie.getNjt() : (double)0.0F;
         paie = this.menu.pc.paieById(rs, this.menu.motifSN, periode3emeMoisTrim);
         Double nb_jour_mois3 = paie != null ? paie.getNjt() : (double)0.0F;

         for(Motif rs2 : this.dlMotif) {
            if (rs2.isDeclarationSoumisCnam()) {
               Paie paieByPeriode = this.menu.pc.paieById(rs, rs2, periode1erMoisTrim);
               if (paieByPeriode != null) {
                  assiete_soumise_mois1 = assiete_soumise_mois1 + (paieByPeriode.getCnam() != (double)0.0F ? paieByPeriode.getBiCnam() : (double)0.0F);
               }

               paieByPeriode = this.menu.pc.paieById(rs, rs2, periode2emeMoisTrim);
               if (paieByPeriode != null) {
                  assiete_soumise_mois2 = assiete_soumise_mois2 + (paieByPeriode.getCnam() != (double)0.0F ? paieByPeriode.getBiCnam() : (double)0.0F);
               }

               paieByPeriode = this.menu.pc.paieById(rs, rs2, periode3emeMoisTrim);
               if (paieByPeriode != null) {
                  assiete_soumise_mois3 = assiete_soumise_mois3 + (paieByPeriode.getCnam() != (double)0.0F ? paieByPeriode.getBiCnam() : (double)0.0F);
               }
            }
         }

         masse_salariale_soumise1_total = masse_salariale_soumise1_total + assiete_soumise_mois1;
         masse_salariale_soumise2_total = masse_salariale_soumise2_total + assiete_soumise_mois2;
         masse_salariale_soumise3_total = masse_salariale_soumise3_total + assiete_soumise_mois3;
         Double ri = assiete_soumise_mois1 + assiete_soumise_mois2 + assiete_soumise_mois3;
         if (ri > (double)0.0F) {
            ++no;
            Listenominativecnam ln = new Listenominativecnam();
            ln.setAssieteSoumiseMois1(assiete_soumise_mois1);
            ln.setAssieteSoumiseMois2(assiete_soumise_mois2);
            ln.setAssieteSoumiseMois3(assiete_soumise_mois3);
            ln.setDateEntre(this.menu.fdf.format(rs.getDateEmbauche()));
            ln.setDateSortie(rs.getDateDebauche() != null ? this.menu.fdf.format(rs.getDateDebauche()) : "-");
            ln.setMatriculeFonc((long)rs.getId());
            ln.setNbJourMois1(nb_jour_mois1);
            ln.setNbJourMois2(nb_jour_mois2);
            ln.setNbJourMois3(nb_jour_mois3);
            ln.setNni(rs.getNni());
            ln.setNo((long)no);
            ln.setNoCnam(rs.getNoCnam());
            String var10001 = rs.getPrenom();
            ln.setNomEmploye(var10001 + " " + rs.getNom());
            ln.setPeriode((Date)this.tPeriode.getSelectedItem());
            this.lnCnamDS.add(ln);
         }
      }

      int noTrimestre = this.menu.gl.quarterNumber((Date)this.tPeriode.getSelectedItem());
      int anneeDec = this.menu.gl.yearFromDate((Date)this.tPeriode.getSelectedItem());
      Map<Object, Object> param = new HashMap();
      param.put("DECLARATIONCNAMTRIM_PERIODE", this.menu.pf.format((Date)this.tPeriode.getSelectedItem()));
      param.put("DECLARATIONCNAMTRIM_ANNEE", anneeDec);
      param.put("DECLARATIONCNAMTRIM_TRIMESTRE", noTrimestre);
      param.put("DECLARATIONCNAMTRIM_EFFECTIF", no);
      param.put("DECLARATIONCNAMTRIM_MASSE_S1", masse_salariale_soumise1_total);
      param.put("DECLARATIONCNAMTRIM_MASSE_S2", masse_salariale_soumise2_total);
      param.put("DECLARATIONCNAMTRIM_MASSE_S3", masse_salariale_soumise3_total);
      param.put("DECLARATIONCNAMTRIM_MOIS1", mois1);
      param.put("DECLARATIONCNAMTRIM_MOIS2", mois2);
      param.put("DECLARATIONCNAMTRIM_MOIS3", mois3);
      param.put("DECLARATIONCNAMTRIM_DATE_LIMIT", date_limite_rappel_str);

      try {
         this.menu.gl.afficherReportParamOnly("declarationCNAM", param);
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.menu.viewMessage(this.msgLabel, "", false);
   }

   private void decITS() {
      List<Paie> dl = this.dlPaie;
      int nbSal = ((List)dl.stream().filter((var1x) -> this.menu.df.format(var1x.getPeriode()).equalsIgnoreCase(this.menu.df.format((Date)this.tPeriode.getSelectedItem())) && var1x.getMotif().getId() == 1).collect(Collectors.toList())).size();
      dl = (List)dl.stream().filter((var1x) -> this.menu.df.format(var1x.getPeriode()).equalsIgnoreCase(this.menu.df.format((Date)this.tPeriode.getSelectedItem())) && var1x.getMotif().isDeclarationSoumisIts()).collect(Collectors.toList());
      double t1 = dl.stream().mapToDouble((var0) -> var0.getItsTranche1()).sum();
      double t2 = dl.stream().mapToDouble((var0) -> var0.getItsTranche2()).sum();
      double t3 = dl.stream().mapToDouble((var0) -> var0.getItsTranche3()).sum();
      double its = dl.stream().mapToDouble((var0) -> var0.getIts()).sum();
      double bt = dl.stream().mapToDouble((var0) -> var0.getBt()).sum();
      double bni = dl.stream().mapToDouble((var0) -> var0.getBni()).sum();
      double avnat = dl.stream().mapToDouble((var0) -> var0.getBiAvnat()).sum();
      double abat = (double)(nbSal * 6000);
      double x = its - t1 - t2;
      double var10000 = x - its;
      Map<Object, Object> param = new HashMap();
      param.put("DECLARATIONITS_PERIODE", this.tPeriode.getSelectedItem());
      param.put("DECLARATIONITS_NBSALARIES", nbSal);
      param.put("DECLARATIONITS_REMUNERATIONBRU", bt);
      param.put("DECLARATIONITS_ABATEMENTFORFET", abat);
      param.put("DECLARATIONITS_REMUNERATIONIMP", bt - bni - abat);
      param.put("DECLARATIONITS_REMUNERATIONNON", bni);
      param.put("DECLARATIONITS_REMUNERATIONT1", t1);
      param.put("DECLARATIONITS_REMUNERATIONT2", t2);
      param.put("DECLARATIONITS_REMUNERATIONT3", x > (double)0.0F ? x : (double)0.0F);
      param.put("DECLARATIONITS_TOTALITS", its);
      param.put("DECLARATIONITS_AVANTAGESNATURE", avnat);

      try {
         this.menu.gl.afficherReportParamOnly("dITS", param);
      } catch (Exception e) {
         e.printStackTrace();
         this.menu.viewMessage(this.msgLabel, "Error!", true);
      }

   }

   private void listeNominativeCNSSExcel() throws IOException, WriteException {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      String var10000 = (new SimpleDateFormat("MMMM-yyyy")).format(periode).toUpperCase();
      String fileName = "repport/LN_CNSS_" + var10000 + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "LN CNSS");
      this.menu.we.addLabelTitre(excelSheet, 0, 0, "R\u00e9publique Islamique de Mauritanie");
      this.menu.we.addLabelTitre(excelSheet, 0, 1, "N\u00b0 Employeur");
      this.menu.we.addLabelTitre(excelSheet, 1, 1, this.menu.paramsGen.getNoCnss());
      this.menu.we.addLabelTitre(excelSheet, 0, 2, "Nom de l'employeur");
      this.menu.we.addLabelTitre(excelSheet, 1, 2, this.menu.paramsGen.getNomEntreprise().toUpperCase());
      WriteExcel var8 = this.menu.we;
      String var10004 = (new SimpleDateFormat("MMMM-yyyy")).format(periode).toUpperCase();
      var8.addLabelTitre(excelSheet, 0, 3, "LISTE NOMINATIVE DU PERSONNEL : " + var10004);
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 0, 6, "Num\u00e9ro d'immat.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 1, 6, "NOM DES TRAVAILLEURS");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 2, 6, "NJT 1\u00e8r Mois");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 3, 6, "NJT 2\u00e8me Mois");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 4, 6, "NJT 3\u00e8me Mois");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 6, "NJT Total");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 6, 6, "REMUNERATION REELE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, 6, "REMUNERATION PLAFONEE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 8, 6, "DATE ENTREE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 9, 6, "DATE SORTIE");
      int row = 7;

      for(Listenominativecnss rs : this.lnCnssDS) {
         this.menu.we.addLabelBorder(excelSheet, 0, row, rs.getNoCnssemploye());
         this.menu.we.addLabelBorder(excelSheet, 1, row, rs.getNomEmploye());
         this.menu.we.addLabelBorder(excelSheet, 2, row, rs.getNbJour1erMois());
         this.menu.we.addLabelBorder(excelSheet, 3, row, rs.getNbJour2emeMois());
         this.menu.we.addLabelBorder(excelSheet, 4, row, rs.getNbJour3emeMois());
         this.menu.we.addNumberBorder(excelSheet, 5, row, rs.getTotalNbJour());
         this.menu.we.addNumberBorder(excelSheet, 6, row, rs.getRemunerationReeles());
         this.menu.we.addNumberBorder(excelSheet, 7, row, rs.getPlafond());
         this.menu.we.addLabelBorder(excelSheet, 8, row, rs.getDateEmbauche().toString());
         this.menu.we.addLabelBorder(excelSheet, 9, row, "");
         if (rs.getDateDebauche() != null) {
            this.menu.we.addLabelBorder(excelSheet, 9, row, rs.getDateDebauche().toString());
         }

         ++row;
      }

      this.menu.we.addNumberBoldBorderSilver(excelSheet, 6, row, this.menu.we.columnSum(excelSheet, 6, row, 7, row - 1));
      this.menu.we.addNumberBoldBorderSilver(excelSheet, 7, row, this.menu.we.columnSum(excelSheet, 7, row, 7, row - 1));
      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void listeNominativeCNAMExcel() throws IOException, WriteException {
      Date periode = (Date)this.tPeriode.getSelectedItem();
      String var10000 = (new SimpleDateFormat("MMMM-yyyy")).format(periode).toUpperCase();
      String fileName = "repport/LN_CNAM_TRIM_" + var10000 + ".xls";
      WritableWorkbook workbook = this.menu.we.excelWorkbook(fileName);
      WritableSheet excelSheet = this.menu.we.excelWritableSheet(workbook, "LN CNAM TRIMESTRIELLE");
      int trim = this.menu.gl.quarterNumber((Date)this.tPeriode.getSelectedItem());
      int annee = this.menu.gl.yearFromDate((Date)this.tPeriode.getSelectedItem());
      this.menu.we.addLabelTitre(excelSheet, 0, 0, "R\u00e9publique Islamique de Mauritanie");
      this.menu.we.addLabelTitre(excelSheet, 0, 1, "N\u00b0 DE LA STRICTURE");
      this.menu.we.addLabelTitre(excelSheet, 3, 1, ": " + this.menu.paramsGen.getNoCnam());
      this.menu.we.addLabelTitre(excelSheet, 0, 2, "NOM DE LA STRICTURE");
      this.menu.we.addLabelTitre(excelSheet, 3, 2, ": " + this.menu.paramsGen.getNomEntreprise().toUpperCase());
      this.menu.we.addLabelTitre(excelSheet, 0, 3, "LISTE NOMINATIVE DU PERSONNEL POUR LE TRIMESTRE: " + trim + " / " + annee);
      this.menu.we.addLabelTitre(excelSheet, 8, 0, "Cachet et signature de la stricture");
      int col = 0;
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 5, 5, "Date");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 7, 5, "Mois 1");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 9, 5, "Mois 2");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, 11, 5, "Mois 3");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "N\u00b0");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "MAT. FONC.");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "INAM");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "NNI");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "NOM ET PRENOM");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "EMBAUCHE");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "CESSATION");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "Assiette soumis \u00e0 cotisation");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "Nbre Hrs/Jrs");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "Assiette soumis \u00e0 cotisation");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "Nbre Hrs/Jrs");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "Assiette soumis \u00e0 cotisation");
      this.menu.we.addLabelBoldBorderWrap(excelSheet, col++, 6, "Nbre Hrs/Jrs");
      this.menu.we.setColumnWidth(excelSheet, 0, 6);
      this.menu.we.setColumnWidth(excelSheet, 1, 6);
      this.menu.we.setColumnWidth(excelSheet, 2, 12);
      this.menu.we.setColumnWidth(excelSheet, 3, 12);
      this.menu.we.setColumnWidth(excelSheet, 4, 30);
      this.menu.we.setColumnWidth(excelSheet, 5, 12);
      this.menu.we.setColumnWidth(excelSheet, 6, 12);
      this.menu.we.setColumnWidth(excelSheet, 7, 10);
      this.menu.we.setColumnWidth(excelSheet, 8, 10);
      this.menu.we.setColumnWidth(excelSheet, 9, 10);
      this.menu.we.setColumnWidth(excelSheet, 10, 10);
      this.menu.we.setColumnWidth(excelSheet, 11, 10);
      this.menu.we.setColumnWidth(excelSheet, 12, 10);
      int row = 7;

      for(Listenominativecnam rs : this.lnCnamDS) {
         col = 0;
         this.menu.we.addNumberBorder(excelSheet, col++, row, (double)rs.getNo());
         this.menu.we.addNumberBorder(excelSheet, col++, row, (double)rs.getMatriculeFonc());
         this.menu.we.addLabelBorder(excelSheet, col++, row, rs.getNoCnam());
         this.menu.we.addLabelBorder(excelSheet, col++, row, rs.getNni());
         this.menu.we.addLabelBorder(excelSheet, col++, row, rs.getNomEmploye());
         this.menu.we.addLabelBorder(excelSheet, col++, row, rs.getDateEntre());
         this.menu.we.addLabelBorder(excelSheet, col++, row, rs.getDateSortie() == null ? " " : rs.getDateSortie());
         this.menu.we.addNumberBorder(excelSheet, col++, row, rs.getAssieteSoumiseMois1());
         this.menu.we.addNumberBorder(excelSheet, col++, row, rs.getNbJourMois1());
         this.menu.we.addNumberBorder(excelSheet, col++, row, rs.getAssieteSoumiseMois2());
         this.menu.we.addNumberBorder(excelSheet, col++, row, rs.getNbJourMois2());
         this.menu.we.addNumberBorder(excelSheet, col++, row, rs.getAssieteSoumiseMois3());
         this.menu.we.addNumberBorder(excelSheet, col++, row, rs.getNbJourMois3());
         ++row;
      }

      workbook.write();
      workbook.close();
      this.menu.we.afficherExcel(fileName);
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.jLabel7 = new JLabel();
      this.btnExit = new JButton();
      this.pnlBody = new JPanel();
      this.tPeriode = new JComboBox();
      this.jLabel20 = new JLabel();
      this.progressBar = new JProgressBar();
      this.btnExport = new JButton();
      this.jLabel25 = new JLabel();
      this.tEtat = new JComboBox();
      this.btnRefresh = new JButton();
      this.tabbedPaneOptions = new JPanel();
      this.cCNSSPersonnelDeclare = new JCheckBox();
      this.msgLabel = new JLabel();
      this.setBackground(new Color(255, 255, 255));
      this.setBorder((Border)null);
      this.setAutoscrolls(true);
      this.setFont(new Font("Segoe UI Light", 0, 12));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(1000, 600));
      this.jPanel1.setBackground(new Color(255, 255, 255));
      this.jLabel7.setFont(new Font("Segoe UI Light", 0, 24));
      this.jLabel7.setForeground(new Color(0, 102, 153));
      this.jLabel7.setText("D\u00e9clarations mensuelles");
      this.jLabel7.setToolTipText("");
      this.btnExit.setBackground(new Color(255, 255, 255));
      this.btnExit.setContentAreaFilled(false);
      this.btnExit.setCursor(new Cursor(12));
      this.btnExit.setOpaque(true);
      this.btnExit.addActionListener(new 1(this));
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7, -2, 491, -2).addPreferredGap(ComponentPlacement.RELATED, 166, 32767).addComponent(this.btnExit, -2, 39, -2).addGap(19, 19, 19)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel7, -2, 37, -2).addComponent(this.btnExit, -1, -1, 32767));
      this.pnlBody.setBackground(new Color(255, 255, 255));
      this.tPeriode.setFont(new Font("Segoe UI Light", 0, 12));
      this.tPeriode.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
      this.tPeriode.addActionListener(new 2(this));
      this.jLabel20.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel20.setForeground(new Color(0, 102, 153));
      this.jLabel20.setText("P\u00e9riode");
      this.progressBar.setBackground(new Color(204, 204, 204));
      this.progressBar.setForeground(new Color(0, 102, 153));
      this.progressBar.setBorderPainted(false);
      this.progressBar.setOpaque(true);
      this.progressBar.setStringPainted(true);
      this.btnExport.setBackground(new Color(255, 255, 255));
      this.btnExport.setToolTipText("Exporter la balance vers Excel");
      this.btnExport.setContentAreaFilled(false);
      this.btnExport.setCursor(new Cursor(12));
      this.btnExport.setOpaque(true);
      this.btnExport.addActionListener(new 3(this));
      this.jLabel25.setFont(new Font("Segoe UI Light", 0, 12));
      this.jLabel25.setForeground(new Color(0, 102, 153));
      this.jLabel25.setText("Document");
      this.tEtat.setFont(new Font("Segoe UI Light", 0, 12));
      this.tEtat.setModel(new DefaultComboBoxModel(new String[]{"D\u00e9claration de l'ITS", "D\u00e9claration de la CNSS", "Liste nominative CNSS - Excel", "D\u00e9claration de la CNAM", "Liste nominative CNAM - Excel"}));
      this.tEtat.addActionListener(new 4(this));
      this.btnRefresh.setBackground(new Color(255, 255, 255));
      this.btnRefresh.setContentAreaFilled(false);
      this.btnRefresh.setCursor(new Cursor(12));
      this.btnRefresh.setOpaque(true);
      this.btnRefresh.addActionListener(new 5(this));
      this.tabbedPaneOptions.setBackground(new Color(255, 255, 255));
      this.tabbedPaneOptions.setBorder(BorderFactory.createTitledBorder((Border)null, "Options", 0, 0, new Font("Segoe UI Light", 1, 12), new Color(0, 102, 153)));
      this.tabbedPaneOptions.setForeground(new Color(0, 102, 153));
      this.cCNSSPersonnelDeclare.setBackground(new Color(255, 255, 255));
      this.cCNSSPersonnelDeclare.setFont(new Font("Segoe UI Light", 1, 12));
      this.cCNSSPersonnelDeclare.setForeground(new Color(0, 102, 153));
      this.cCNSSPersonnelDeclare.setText("Personnel declar\u00e9");
      this.cCNSSPersonnelDeclare.addActionListener(new 6(this));
      GroupLayout tabbedPaneOptionsLayout = new GroupLayout(this.tabbedPaneOptions);
      this.tabbedPaneOptions.setLayout(tabbedPaneOptionsLayout);
      tabbedPaneOptionsLayout.setHorizontalGroup(tabbedPaneOptionsLayout.createParallelGroup(Alignment.LEADING).addGroup(tabbedPaneOptionsLayout.createSequentialGroup().addContainerGap(528, 32767).addComponent(this.cCNSSPersonnelDeclare, -2, 164, -2).addContainerGap()));
      tabbedPaneOptionsLayout.setVerticalGroup(tabbedPaneOptionsLayout.createParallelGroup(Alignment.LEADING).addGroup(tabbedPaneOptionsLayout.createSequentialGroup().addContainerGap().addComponent(this.cCNSSPersonnelDeclare).addContainerGap(21, 32767)));
      GroupLayout pnlBodyLayout = new GroupLayout(this.pnlBody);
      this.pnlBody.setLayout(pnlBodyLayout);
      pnlBodyLayout.setHorizontalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, pnlBodyLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.progressBar, -2, 728, -2)).addGroup(pnlBodyLayout.createSequentialGroup().addContainerGap().addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.tPeriode, 0, -1, 32767).addComponent(this.jLabel20, -2, 144, -2)).addComponent(this.tEtat, -2, 250, -2).addComponent(this.jLabel25, -2, 250, -2).addGroup(pnlBodyLayout.createSequentialGroup().addComponent(this.btnRefresh, -2, 35, -2).addGap(640, 640, 640).addComponent(this.btnExport, -2, 35, -2)).addComponent(this.tabbedPaneOptions, -2, -1, -2)).addGap(0, 0, 32767))).addContainerGap()));
      pnlBodyLayout.setVerticalGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING).addGroup(pnlBodyLayout.createSequentialGroup().addGap(0, 0, 0).addComponent(this.progressBar, -2, -1, -2).addGap(18, 18, 18).addComponent(this.jLabel20).addGap(0, 0, 0).addComponent(this.tPeriode, -2, 30, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel25).addGap(0, 0, 0).addComponent(this.tEtat, -2, 30, -2).addGap(18, 18, 18).addComponent(this.tabbedPaneOptions, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(pnlBodyLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.btnExport, -1, -1, 32767).addComponent(this.btnRefresh, Alignment.TRAILING, -2, 35, -2)).addGap(3, 3, 3)));
      this.msgLabel.setBackground(new Color(255, 255, 255));
      this.msgLabel.setFont(new Font("Segoe UI Light", 1, 12));
      this.msgLabel.setForeground(new Color(255, 0, 0));
      this.msgLabel.setHorizontalAlignment(0);
      this.msgLabel.setToolTipText("");
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jPanel1, -1, -1, 32767).addComponent(this.msgLabel, -1, -1, 32767).addComponent(this.pnlBody, -2, 725, 32767)).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(this.pnlBody, -2, -1, -2).addGap(0, 0, 0).addComponent(this.msgLabel, -2, 22, -2)));
      this.setSize(new Dimension(727, 374));
   }

   private void btnExitActionPerformed(ActionEvent var1) {
      this.dispose();
   }

   private void btnExportActionPerformed(ActionEvent var1) {
      this.menu.viewMessage(this.msgLabel, "", false);
      Date periode = (Date)this.tPeriode.getSelectedItem();
      Calendar cal = Calendar.getInstance();
      cal.setTime(periode);
      int month = cal.get(2) + 1;
      Thread t = new 7(this, month);
      t.start();
   }

   private void tPeriodeActionPerformed(ActionEvent var1) {
   }

   private void tEtatActionPerformed(ActionEvent var1) {
      this.tabbedPaneOptions.setVisible(this.tEtat.getSelectedItem().equals("D\u00e9claration de la CNSS"));
   }

   private void btnRefreshActionPerformed(ActionEvent var1) {
      Thread t = new 8(this);
      t.start();
   }

   private void cCNSSPersonnelDeclareActionPerformed(ActionEvent var1) {
   }
}
