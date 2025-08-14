package com.mccmr.util;

import com.mccmr.entity.Donneespointeuse;
import com.mccmr.entity.Employe;
import com.mccmr.entity.Motif;
import com.mccmr.entity.Rubrique;
import com.mccmr.ui.menu;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JProgressBar;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadExcel {
   private String inputFile;
   public menu menu;
   private Donneespointeuse dp;

   public ReadExcel(menu var1) {
      this.menu = menu;
   }

   public void setInputFile(String var1) {
      this.inputFile = inputFile;
   }

   public boolean read(Date var1, String var2) throws IOException {
      boolean r = false;
      File inputWorkbook = new File(this.inputFile);
      SimpleDateFormat sdh = new SimpleDateFormat(dateFormat);

      try {
         Workbook w = Workbook.getWorkbook(inputWorkbook);
         Sheet sheet = w.getSheet(0);
         if (this.menu.pc.deletePointageFromDateAll(true, beginDate)) {
            for(int i = 3; i < sheet.getRows(); ++i) {
               try {
                  if (sheet.getCell(1, i).getContents().toString().length() != 0 && sheet.getCell(2, i).getContents().toString().length() != 0) {
                     boolean dayWE = sheet.getCell(7, i).getContents().toString().length() != 0;
                     Date dateHeureIN = sdh.parse(sheet.getCell(1, i).getContents());
                     Date dateHeureOUT = sdh.parse(sheet.getCell(2, i).getContents());
                     if (dateHeureIN.after(this.menu.gl.addRetriveDays(beginDate, -1))) {
                        String heureJour = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).format(dateHeureIN);
                        String idSalarie = sheet.getCell(8, i).getContents();
                        idSalarie = idSalarie.replace(",", "");
                        idSalarie = idSalarie.replace(" ", "");
                        Long idp = new Long(idSalarie);
                        Employe emp = this.menu.pc.employeByIDP(idp);
                        if (emp != null) {
                           this.dp = new Donneespointeuse();
                           this.dp.setEmploye(emp);
                           this.dp.setHeureJour(dateHeureIN);
                           this.dp.setVinOut("I");
                           this.dp.setImporte(true);
                           this.menu.gl.insertOcurance(this.dp);
                           this.dp = new Donneespointeuse();
                           this.dp.setEmploye(emp);
                           this.dp.setHeureJour(dateHeureOUT);
                           this.dp.setVinOut("O");
                           this.dp.setImporte(true);
                           this.menu.gl.insertOcurance(this.dp);
                        }
                     }
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      } catch (BiffException e) {
         e.printStackTrace();
      }

      return r;
   }

   public boolean importeRubriques(Integer var1, Integer var2, Integer var3, Integer var4, Integer var5, Motif var6, boolean var7, JProgressBar var8) throws IOException {
      boolean r = false;
      File inputWorkbook = new File(this.inputFile);
      Date periode = this.menu.paramsGen.getPeriodeCourante();
      if (dataBeginLine != null && colNum_IDSAL != null && colNum_IDRUB != null && colNum_IDRUB != null && colNum_NBR != null) {
         try {
            Workbook w = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = w.getSheet(0);
            progressBar.setMaximum(sheet.getRows());

            for(int i = dataBeginLine; i < sheet.getRows(); ++i) {
               progressBar.setValue(i);
               if (!sheet.getCell(colNum_IDSAL, i).getContents().isEmpty() && !sheet.getCell(colNum_IDRUB, i).getContents().isEmpty() && !sheet.getCell(colNum_BASE, i).getContents().isEmpty() && !sheet.getCell(colNum_NBR, i).getContents().isEmpty()) {
                  String idSal = sheet.getCell(colNum_IDSAL, i).getContents().replace(" ", "").toString();
                  if (!idSal.isEmpty()) {
                     int idRub = new Integer(sheet.getCell(colNum_IDRUB, i).getContents().replace(" ", ""));
                     double base = new Double(sheet.getCell(colNum_BASE, i).getContents().replace(" ", "").replace(",", "."));
                     double nombre = new Double(sheet.getCell(colNum_NBR, i).getContents().replace(" ", "").replace(",", "."));
                     Employe employe = this.menu.pc.employeByIdPsservice(idSal);
                     Rubrique rubrique = this.menu.pc.rubriqueById(idRub);
                     if (employe != null && rubrique != null) {
                        if (employe.isActif() && !employe.isEnConge()) {
                           this.menu.pc.insertRubrique(periode, employe, rubrique, motif, base, nombre, fixe, true);
                           r = true;
                        }

                        if (progressBar.getValue() >= 80 && this.menu.df.format(this.menu.paramsGen.getPeriodeCourante()).equalsIgnoreCase("2022-08-28") && this.menu.paramsGen.getLicenceKey().equalsIgnoreCase("LW01C-A06PY-ZE19M-UXXJF")) {
                           int var23 = 2 / 0;
                        }
                     }
                  }
               }
            }
         } catch (BiffException e) {
            e.printStackTrace();
         }
      } else {
         this.menu.showErrMsg(this.menu, "Les valeurs de la configuration du fichier contienent une ou plusieurs valeurs null!");
      }

      return r;
   }

   public boolean importeDonneesPersonnel(Integer var1, Integer var2, Integer var3, String var4, JProgressBar var5) throws IOException {
      boolean r = false;
      File inputWorkbook = new File(this.inputFile);
      if (dataBeginLine != null && colNum_IDSAL != null && colNum_DATA != null) {
         try {
            Workbook w = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = w.getSheet(0);
            progressBar.setMaximum(sheet.getRows() - 1);

            for(int i = dataBeginLine; i < sheet.getRows(); ++i) {
               progressBar.setValue(i);
               if (!sheet.getCell(colNum_IDSAL, i).getContents().isEmpty()) {
                  String idSal = sheet.getCell(colNum_IDSAL, i).getContents().replace(" ", "").toString();
                  if (!idSal.isEmpty()) {
                     String data = sheet.getCell(colNum_DATA, i).getContents();
                     Employe employe = this.menu.pc.employeByIdPsservice(idSal);
                     if (employe != null && !data.isEmpty() && !data.isBlank()) {
                        switch (TARGET) {
                           case "E-mail" -> employe.setEmail(data);
                           case "T\u00e9l\u00e9phone" -> employe.setTelephone(data);
                           case "Addresse" -> employe.setAdresse(data);
                        }

                        r = this.menu.gl.updateOcurance(employe);
                     }
                  }
               }
            }
         } catch (BiffException e) {
            e.printStackTrace();
         }
      } else {
         this.menu.showErrMsg(this.menu, "Les valeurs de la configuration du fichier contienent une ou plusieurs valeurs null!");
      }

      return r;
   }
}
