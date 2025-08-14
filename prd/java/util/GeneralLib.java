package com.mccmr.util;

import com.mccmr.entity.Motif;
import com.mccmr.entity.Paie;
import com.mccmr.entity.Rubriquepaie;
import com.mccmr.entity.Utilisateurs;
import com.mccmr.ui.menu;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GeneralLib {
   public menu menu;

   public GeneralLib(menu var1) {
      this.menu = menu;
   }

   public static String coolNumberFormat(double var0) {
      String suffix = " KMBT";
      String formattedNumber = "";
      NumberFormat formatter = new DecimalFormat("#,###.#");
      int power = (int)StrictMath.log10(value);
      value /= Math.pow((double)10.0F, (double)(power / 3 * 3));
      formattedNumber = formatter.format(value);
      formattedNumber = formattedNumber + suffix.charAt(power / 3);
      return formattedNumber.length() > 4 ? formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;
   }

   public static String byPaddingZeros(int var0, int var1) {
      return String.format("%0" + paddingLength + "d", value);
   }

   public int selectedLinesSize(JTable var1) {
      int r = 0;

      for(int i = 0; i < listTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmBulletinPaie)listTable.getModel()).getValueAt(i, 0)) {
            ++r;
         }
      }

      return r;
   }

   public boolean netIsAvailable() {
      try {
         URL url = new URL("http://www.google.com");
         URLConnection conn = url.openConnection();
         conn.connect();
         return true;
      } catch (MalformedURLException e) {
         throw new RuntimeException(e);
      } catch (IOException var4) {
         return false;
      }
   }

   public Licencekey codeLicencekey(String var1) {
      Licencekey r = null;

      try {
         URL url = new URL("https://www.mccmr.com/eliya-soft-licence/oneELIYAPaieLicence?licenceID=" + code);
         HttpURLConnection conn = (HttpURLConnection)url.openConnection();
         conn.setRequestMethod("GET");
         conn.connect();
         int responsecode = conn.getResponseCode();
         if (responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
         }

         String inline = "";

         Scanner scanner;
         for(scanner = new Scanner(url.openStream()); scanner.hasNext(); inline = inline + scanner.nextLine()) {
         }

         scanner.close();
         JSONParser parse = new JSONParser();
         JSONObject data_obj = (JSONObject)parse.parse(inline);
         JSONObject obj = (JSONObject)data_obj.get("data");
         if (obj != null) {
            r = new Licencekey();
            r.setNbSalaryCode(obj.get("nbSalaryCode").toString());
            r.setCustumerActiveVersion(obj.get("custumerActiveVersion").toString());
            r.setCompanytName(obj.get("companytName").toString());
            r.setPub(obj.get("pub") != null ? obj.get("pub").toString() : null);
            r.setLicencePeriodicity(obj.get("licencePeriodicity").toString());
            Date dateInitLicence = (new SimpleDateFormat("yyyy-MM-dd")).parse(obj.get("dateInitLicence").toString());
            Date dateCurentLicence = (new SimpleDateFormat("yyyy-MM-dd")).parse(obj.get("dateCurentLicence").toString());
            r.setDateInitLicence(dateInitLicence);
            r.setDateCurentLicence(dateCurentLicence);
            r.setId(0L);
         }

         conn.disconnect();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return r;
   }

   public void codeLicencekeyApply(String var1) {
      try {
         URL url = new URL("https://www.mccmr.com/eliya-soft-licence/updateELIYAPaieLicence/" + code);
         HttpURLConnection http = (HttpURLConnection)url.openConnection();
         http.setRequestMethod("PUT");
         http.setDoOutput(true);
         http.setRequestProperty("Content-Type", "application/json");
         InetAddress ip = InetAddress.getLocalHost();
         String var10000 = ip.getHostAddress();
         String data = "{\n  \"ip\": \"" + var10000 + "\",\n  \"host_name\": \"" + ip.getHostName() + "\"\n}";
         byte[] out = data.getBytes(StandardCharsets.UTF_8);
         OutputStream stream = http.getOutputStream();
         stream.write(out);
         http.disconnect();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public String onlineCurrentVersion() {
      String r = null;
      if (this.netIsAvailable()) {
         try {
            URL url = new URL("https://www.mccmr.com/eliya-soft-licence/getSoftwareCurrentVesrion?softwareID=eliyapaie");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
               throw new RuntimeException("HttpResponseCode: " + responsecode);
            }

            String inline = "";

            Scanner scanner;
            for(scanner = new Scanner(url.openStream()); scanner.hasNext(); inline = inline + scanner.nextLine()) {
            }

            scanner.close();
            JSONParser parse = new JSONParser();
            JSONObject data_obj = (JSONObject)parse.parse(inline);
            JSONObject obj = (JSONObject)data_obj.get("data");
            r = obj.get("currentVersion").toString();
            conn.disconnect();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      return r;
   }

   public int maxSalaries(String var1) {
      int r = 2;
      if (code != null) {
         if (code.equalsIgnoreCase("VCSHXP")) {
            r = 20;
         }

         if (code.equalsIgnoreCase("RMCPML")) {
            r = 30;
         }

         if (code.equalsIgnoreCase("EXLWUQ")) {
            r = 40;
         }

         if (code.equalsIgnoreCase("ETYCPL")) {
            r = 50;
         }

         if (code.equalsIgnoreCase("TYCBBQ")) {
            r = 100;
         }

         if (code.equalsIgnoreCase("RRIMX")) {
            r = 150;
         }

         if (code.equalsIgnoreCase("EMCNB")) {
            r = 200;
         }

         if (code.equalsIgnoreCase("YUSNB")) {
            r = 250;
         }

         if (code.equalsIgnoreCase("YUSOK")) {
            r = 300;
         }

         if (code.equalsIgnoreCase("ANDKI")) {
            r = 400;
         }

         if (code.equalsIgnoreCase("PLBGA")) {
            r = 500;
         }

         if (code.equalsIgnoreCase("TYUIN")) {
            r = 600;
         }

         if (code.equalsIgnoreCase("VXPJU")) {
            r = 700;
         }

         if (code.equalsIgnoreCase("USLLH")) {
            r = 800;
         }

         if (code.equalsIgnoreCase("JAKNN")) {
            r = 900;
         }

         if (code.equalsIgnoreCase("MNXNJ")) {
            r = 1000;
         }

         if (code.equalsIgnoreCase("LPSJH")) {
            r = 1500;
         }
      }

      return r;
   }

   public boolean isCracked() {
      Boolean r = false;
      if (this.menu.gl.netIsAvailable() && this.menu.paramsGen.getLicenceKey() != null && this.menu.paramsGen.getLicenceKey().length() == 23) {
         Licencekey lk = this.menu.gl.codeLicencekey(this.menu.paramsGen.getLicenceKey());
         if (lk != null) {
            r = !this.menu.paramsGen.getNomEntreprise().equals(lk.getCompanytName());
            this.menu.paramsGen.setNomEntreprise(lk.getCompanytName());
            this.menu.paramsGen.setNbSalaryCode(lk.getNbSalaryCode());
            this.menu.paramsGen.setCustumerActiveVersion(lk.getCustumerActiveVersion());
            this.updateOcurance(this.menu.paramsGen);
         }
      }

      return r;
   }

   private static void saveBytesToFile(String var0, byte[] var1) throws IOException {
      FileOutputStream outputStream = new FileOutputStream(filePath);
      outputStream.write(fileBytes);
      outputStream.close();
   }

   private static byte[] readBytesFromFile(String var0) throws IOException {
      File inputFile = new File(filePath);
      FileInputStream inputStream = new FileInputStream(inputFile);
      byte[] fileBytes = new byte[(int)inputFile.length()];
      inputStream.read(fileBytes);
      inputStream.close();
      return fileBytes;
   }

   public static Image scaleImage(Image var0, int var1, int var2) {
      BufferedImage img = new BufferedImage(width, height, 2);
      Graphics2D g = (Graphics2D)img.getGraphics();
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g.drawImage(source, 0, 0, width, height, (ImageObserver)null);
      g.dispose();
      return img;
   }

   public void formatLebleMsgBox(JLabel var1) {
      label.setHorizontalAlignment(4);
      label.setPreferredSize(new Dimension(60, 25));
   }

   public void clearFrame(JFrame var1) {
      for(int i = 0; i < frame.getComponentCount(); ++i) {
         if (frame.getComponent(i) instanceof JFormattedTextField) {
            JFormattedTextField o = (JFormattedTextField)frame.getComponent(i);
            o.setValue(0);
         }
      }

   }

   public Utilisateurs UserByID(String var1) {
      Utilisateurs rs = null;
      rs = (Utilisateurs)this.menu.entityManager.find(Utilisateurs.class, login);
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

   public double valCurentDevise() {
      return (double)1.0F;
   }

   public void validateValue(JFormattedTextField var1) {
      if (arg.getValue() == null) {
         arg.setValue(0);
      }

   }

   public void validateValue(JFormattedTextField var1, double var2) {
      if (arg.getValue() == null) {
         arg.setValue(defaultValue);
      }

   }

   public Date addRetriveDays(Date var1, int var2) {
      Calendar cal = Calendar.getInstance();
      if (date != null) {
         cal.setTime(date);
         cal.add(5, nbJour);
      }

      return cal.getTime();
   }

   public Integer yearFromDate(Date var1) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(periode);
      int month = cal.get(1);
      return month;
   }

   public double differenceDateHeure(Date var1, Date var2) {
      double UNE_HEURE = (double)3600000.0F;
      return (double)(dateFin.getTime() - dateDebut.getTime()) / UNE_HEURE;
   }

   public long differenceDateMois(Date var1, Date var2) {
      long UNE_HEURE = 3600000L;
      return (dateFin.getTime() - dateDebut.getTime() + UNE_HEURE) / UNE_HEURE * 24L * 30L;
   }

   public long differenceDateJour(Date var1, Date var2) {
      long UNE_HEURE = 3600000L;
      return (dateFin.getTime() - dateDebut.getTime() + UNE_HEURE) / UNE_HEURE * 24L;
   }

   public long ageYears(Date var1) {
      Date now = new Date();
      long r = (long)((int)Math.floor((double)((now.getTime() - dateNaissance.getTime()) / 86400000L / 365L)));
      return r;
   }

   public Integer quarterNumber(Date var1) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(periode);
      int month = cal.get(2);
      return month >= 0 && month <= 2 ? 1 : (month >= 3 && month <= 5 ? 2 : (month >= 6 && month <= 8 ? 3 : 4));
   }

   public boolean insertOcurance(Object var1) {
      boolean r = false;

      try {
         if (!this.menu.entityManager.getTransaction().isActive()) {
            this.menu.entityManager.getTransaction().begin();
         }

         this.menu.entityManager.persist(o);
         this.menu.entityManager.flush();
         this.menu.entityManager.getTransaction().commit();
         r = true;
      } catch (Exception e) {
         e.printStackTrace();
         this.menu.entityManager.getTransaction().rollback();
      }

      return r;
   }

   public boolean updateOcurance(Object var1) {
      boolean r = false;

      try {
         if (!this.menu.entityManager.getTransaction().isActive()) {
            this.menu.entityManager.getTransaction().begin();
         }

         this.menu.entityManager.merge(o);
         this.menu.entityManager.flush();
         this.menu.entityManager.getTransaction().commit();
         r = true;
      } catch (Exception e) {
         e.printStackTrace();
         this.menu.entityManager.getTransaction().rollback();
      }

      return r;
   }

   public boolean deleteOcurance(Object var1) {
      boolean r = false;

      try {
         if (!this.menu.entityManager.getTransaction().isActive()) {
            this.menu.entityManager.getTransaction().begin();
         }

         this.menu.entityManager.remove(o);
         this.menu.entityManager.flush();
         this.menu.entityManager.getTransaction().commit();
         r = true;
      } catch (Exception e) {
         e.printStackTrace();
         this.menu.entityManager.getTransaction().rollback();
      }

      return r;
   }

   public void exQuery(String var1) {
      try {
         if (!this.menu.entityManager.getTransaction().isActive()) {
            this.menu.entityManager.getTransaction().begin();
         }

         this.menu.entityManager.createQuery(queryString).executeUpdate();
         this.menu.entityManager.flush();
         this.menu.entityManager.getTransaction().commit();
      } catch (Exception e) {
         e.printStackTrace();
         this.menu.entityManager.getTransaction().rollback();
      }

   }

   private void setRepportEntrepriseParam(Map<String, Object> var1) {
      param.put("PARAMGEN_NOMENTREPRISE", this.menu.paramsGen.getNomEntreprise());
      param.put("PARAMGEN_ACTIVITEENTREPRISE", this.menu.paramsGen.getActiviteEntreprise());
      param.put("PARAMGEN_TELEPHONE", this.menu.paramsGen.getTelephone());
      param.put("PARAMGEN_FAX", this.menu.paramsGen.getFax());
      param.put("PARAMGEN_EMAIL", this.menu.paramsGen.getEmail());
      param.put("PARAMGEN_ADRESSE", this.menu.paramsGen.getAdresse());
      param.put("PARAMGEN_BD", this.menu.paramsGen.getBd());
      param.put("PARAMGEN_SITEWEB", this.menu.paramsGen.getSiteweb());
      param.put("PARAMGEN_EMAIL", this.menu.paramsGen.getEmail());
      param.put("PARAMGEN_NOCNAM", this.menu.paramsGen.getNoCnam());
      param.put("PARAMGEN_NOCNSS", this.menu.paramsGen.getNoCnss());
      param.put("PARAMGEN_NOITS", this.menu.paramsGen.getNoIts());
      param.put("PARAMGEN_VILLESIEGE", this.menu.paramsGen.getVilleSiege());
      param.put("PARAMGEN_PUB", this.menu.paramsGen.getPub());
      if (this.menu.paramsGen.getLogo() != null && this.menu.paramsGen.getLogo().length > 0) {
         byte[] bAvatar = this.menu.paramsGen.getLogo();
         InputStream targetStream = new ByteArrayInputStream(bAvatar);
         param.put("PARAMGEN_LOGO", targetStream);
      }

   }

   public FichePaieReportInput assemble(JTable var1, List<Paie> var2, Motif var3, Date var4) {
      FichePaieReportInput fichePaiesReportInput = new FichePaieReportInput(this);
      List<FichePaie> fichPaies = new ArrayList();

      for(int i = 0; i < listTable.getRowCount(); ++i) {
         if ((Boolean)((ModelClass.tmBulletinPaie)listTable.getModel()).getValueAt(i, 0)) {
            int idEmploye = ((Number)((ModelClass.tmBulletinPaie)listTable.getModel()).getValueAt(i, 1)).intValue();
            Paie paie = (Paie)((List)dlPaie.stream().filter((var4x) -> var4x.getEmploye().getId() == idEmploye && var4x.getMotif().getId() == motif.getId() && this.menu.df.format(var4x.getPeriode()).equalsIgnoreCase(this.menu.df.format(periode))).collect(Collectors.toList())).get(0);
            if (paie != null) {
               FichePaie fichePaie = new FichePaie();
               fichePaie.setEmploye_contratHeureSemaine(paie.getEmploye().getContratHeureSemaine());
               fichePaie.setEmploye_dateAnciennete(paie.getEmploye().getDateAnciennete());
               fichePaie.setEmploye_dateEmbauche(paie.getEmploye().getDateEmbauche());
               fichePaie.setEmploye_id(paie.getEmploye().getId());
               fichePaie.setEmploye_idPsservice(paie.getEmploye().getIdPsservice());
               fichePaie.setEmploye_nni(paie.getEmploye().getNni());
               fichePaie.setEmploye_noCNAM(paie.getEmploye().getNoCnam());
               fichePaie.setEmploye_noCNSS(paie.getEmploye().getNoCnss());
               fichePaie.setEmploye_nom(paie.getEmploye().getNom());
               fichePaie.setEmploye_prenom(paie.getEmploye().getPrenom());
               fichePaie.setMotif_nom(motif.getNom());
               fichePaie.setPaie_BNI(paie.getBni());
               fichePaie.setPaie_BT(paie.getBt());
               fichePaie.setPaie_CNAM(paie.getCnam());
               fichePaie.setPaie_CNSS(paie.getCnss());
               fichePaie.setPaie_ITS(paie.getIts());
               fichePaie.setPaie_RCNAM(paie.getRcnam());
               fichePaie.setPaie_RCNSS(paie.getRcnss());
               fichePaie.setPaie_RITS(paie.getRits());
               fichePaie.setPaie_banque(paie.getBanque());
               fichePaie.setPaie_biAVNAT(paie.getBiAvnat());
               fichePaie.setPaie_biCNAM(paie.getBiCnam());
               fichePaie.setPaie_biCNSS(paie.getBiCnss());
               fichePaie.setPaie_categorie(paie.getCategorie());
               fichePaie.setPaie_contratHeureMois(paie.getContratHeureMois());
               fichePaie.setPaie_departement(paie.getDepartement());
               fichePaie.setPaie_domicilie(paie.isDomicilie());
               fichePaie.setPaie_modePaiement(paie.getModePaiement());
               fichePaie.setPaie_nbrHS(paie.getNbrHs());
               fichePaie.setPaie_net(paie.getNet());
               fichePaie.setPaie_njt(paie.getNjt());
               fichePaie.setPaie_noCompteBanque(paie.getNoCompteBanque());
               fichePaie.setPaie_notePaie(paie.getNotePaie());
               fichePaie.setPaie_paieAu(paie.getPaieAu());
               fichePaie.setPaie_paieDu(paie.getPaieDu());
               fichePaie.setPaie_periodeLettre(paie.getPeriodeLettre());
               fichePaie.setPaie_poste(paie.getPoste());
               fichePaie.setPaie_retenuesBrut(paie.getRetenuesBrut());
               fichePaie.setPaie_retenuesNet(paie.getRetenuesNet());
               List<Rubriquepaie> dl = new ArrayList(this.menu.pc.empRubriquepaie(paie.getEmploye(), motif, periode));
               List<FichePaieDetail> fichePaieDetailList = new ArrayList();
               dl.forEach((var1x) -> {
                  FichePaieDetail pd = new FichePaieDetail();
                  pd.setCodeRub(var1x.getRubrique().getId());
                  pd.setLibelle(var1x.getRubrique().getLibelle());
                  pd.setSens(var1x.getRubrique().getSens());
                  pd.setBase(var1x.getBase());
                  pd.setNombre(var1x.getNombre());
                  pd.setMontant(var1x.getMontant());
                  fichePaieDetailList.add(pd);
               });
               fichePaie.setFichePaieDetailList(fichePaieDetailList);
               fichPaies.add(fichePaie);
            }
         }
      }

      JRBeanCollectionDataSource fichePaieDataSource = new JRBeanCollectionDataSource(fichPaies, false);
      fichePaiesReportInput.setFichPaieDataSource(fichePaieDataSource);
      return fichePaiesReportInput;
   }

   public void afficherReportParamOnly(String var1, Map var2) throws ClassNotFoundException, SQLException, JRException {
      this.setRepportEntrepriseParam(param);
      InputStream reportSource = this.getClass().getResourceAsStream("/com/mccmr/report/" + reportName + ".jrxml");
      if (reportSource != null) {
         try {
            JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, new JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint, false);
         } catch (Exception ex) {
            JOptionPane.showMessageDialog((Component)null, ex);
            ex.printStackTrace();
         } finally {
            ;
         }
      } else {
         JOptionPane.showMessageDialog((Component)null, "Fichier source non disponible!");
      }

   }

   public void exportPDFReport(String var1, Map var2, String var3, Date var4, String var5, File var6, boolean var7) throws ClassNotFoundException, SQLException, JRException {
      this.setRepportEntrepriseParam(param);
      File periodeDir;
      if (selectedPath == null) {
         periodeDir = new File("repport/Paie_" + motifName + "_" + this.menu.filePeriodeDF.format(periode));
         if (!periodeDir.exists()) {
            periodeDir.mkdir();
         }
      } else {
         periodeDir = new File(String.valueOf(selectedPath) + "/Paie_" + motifName + "_" + this.menu.filePeriodeDF.format(periode));
         if (!periodeDir.exists()) {
            periodeDir.mkdir();
         }
      }

      String fileName = String.valueOf(periodeDir) + "/BP_" + motifName + "_" + this.menu.filePeriodeDF.format(periode) + "_" + salId + ".pdf";
      File file = new File(fileName);
      InputStream reportSource = this.getClass().getResourceAsStream("/com/mccmr/report/" + reportName + ".jrxml");

      try {
         ByteArrayOutputStream bos = new ByteArrayOutputStream();

         try {
            JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, new JREmptyDataSource());
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            SimpleOutputStreamExporterOutput simpleOutputStreamExporterOutput = new SimpleOutputStreamExporterOutput(bos);
            exporter.setExporterOutput(simpleOutputStreamExporterOutput);
            SimplePdfExporterConfiguration simplePdfExporterConfiguration = new SimplePdfExporterConfiguration();
            simplePdfExporterConfiguration.setMetadataAuthor("ELIYA-Paie");
            exporter.setConfiguration(simplePdfExporterConfiguration);
            exporter.exportReport();
            FileUtils.writeByteArrayToFile(file, bos.toByteArray());
            simpleOutputStreamExporterOutput.close();
         } catch (Throwable var20) {
            try {
               bos.close();
            } catch (Throwable var19) {
               var20.addSuppressed(var19);
            }

            throw var20;
         }

         bos.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

      if (showFolder) {
         try {
            Desktop.getDesktop().open(new File("repport/Paie_" + motifName + "_" + this.menu.filePeriodeDF.format(periode)));
         } catch (Exception ex) {
            ex.printStackTrace();
         }
      }

   }

   public void printReport(String var1, Map var2, boolean var3) throws ClassNotFoundException, SQLException, JRException {
      this.setRepportEntrepriseParam(param);
      InputStream reportSource = this.getClass().getResourceAsStream("/com/mccmr/report/" + reportName + ".jrxml");

      try {
         JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
         JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, new JREmptyDataSource());
         JasperPrintManager.printReport(jasperPrint, showPrinterDialog);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }
}
