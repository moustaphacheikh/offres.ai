package com.mccmr.util;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import jxl.Cell;
import jxl.CellType;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriteExcel {
   public WriteExcel() {
   }

   public void afficherExcel(String var1) throws IOException {
      if (Desktop.isDesktopSupported()) {
         Desktop desktop = Desktop.getDesktop();
         if (desktop.isSupported(Action.OPEN)) {
            desktop.open(new File(fileName));
         }
      }

   }

   public WritableWorkbook excelWorkbook(String var1) throws IOException, WriteException {
      File file = new File(fileNamen);
      WorkbookSettings wbSettings = new WorkbookSettings();
      wbSettings.setLocale(new Locale("fr", "FR"));
      WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
      return workbook;
   }

   public WritableSheet excelWritableSheet(WritableWorkbook var1, String var2) throws IOException, WriteException {
      workbook.createSheet(sheetName, 0);
      WritableSheet excelSheet = workbook.getSheet(0);
      return excelSheet;
   }

   public void addLabelTitre(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 9, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelTitreWrap(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 9, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setWrap(true);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelTitreBorder(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 9, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      wcf.setWrap(true);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelTitreBorderWrap(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 9, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      wcf.setWrap(true);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelBold(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelBoldWrap(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setWrap(true);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelBoldBorder(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelBoldBorderSilver(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      wcf.setBackground(Colour.GREY_25_PERCENT);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelBoldBorderGold(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setBackground(Colour.GOLD);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelBoldBorderWrap(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      wcf.setWrap(true);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelTBS(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableCell cell = sheet.getWritableCell(column, row);
      Label l = (Label)cell;
      l.setString(value);
   }

   public void addNumberTBS(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableCell cell = sheet.getWritableCell(column, row);
      Number l = (Number)cell;
      l.setValue(value);
   }

   public void addLabel(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      WritableCellFormat wcf = new WritableCellFormat(font);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelMark(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      font.setColour(Colour.RED);
      WritableCellFormat wcf = new WritableCellFormat(font);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelWrap(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setWrap(true);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelBorder(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addLabelBorderWrap(WritableSheet var1, int var2, int var3, String var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      WritableCellFormat wcf = new WritableCellFormat(font);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      wcf.setWrap(true);
      Label label = new Label(column, row, value, wcf);
      sheet.addCell(label);
   }

   public void addNumberBold(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumberBoldWrap(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      wcf.setWrap(true);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumberBoldBorder(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumberBoldBorderGold(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      wcf.setBackground(Colour.GOLD);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumberBoldBorderSilver(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      wcf.setBackground(Colour.GREY_25_PERCENT);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumberBoldBorderWrap(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      wcf.setWrap(true);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumber(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumberWrap(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      wcf.setWrap(true);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumberBorderCol(WritableSheet var1, int var2, int var3, Double var4, Colour var5) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      wcf.setBackground(color);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumberBorder(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumberBorderWrap(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
      wcf.setWrap(true);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addMark(WritableSheet var1, int var2, int var3) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
      font.setColour(Colour.RED);
      WritableCellFormat wcf = new WritableCellFormat(font);
      Label label = new Label(column, row, "", wcf);
      sheet.addCell(label);
   }

   public void addFormuleSum(WritableSheet var1, int var2, int var3, int var4, int var5) throws RowsExceededException, WriteException {
      String form = "SUM(" + this.colName(column) + rowBegin + ":" + this.colName(column) + rowEnd + ")";
      StringBuilder buf = new StringBuilder();
      buf.append(form);
      Formula formule = new Formula(column, row, buf.toString());
      sheet.addCell(formule);
   }

   public double columnSum(WritableSheet var1, int var2, int var3, int var4, int var5) throws RowsExceededException, WriteException {
      double r = (double)0.0F;

      for(int i = rowBegin; i <= rowEnd; ++i) {
         Cell cell = sheet.getCell(column, i);
         CellType type = cell.getType();
         if (type == CellType.NUMBER) {
            r += new Double(sheet.getCell(column, i).getContents().replace(",", "."));
         }
      }

      return r;
   }

   private String colName(int var1) {
      String r = null;
      switch (colNumber) {
         case 0 -> r = "A";
         case 1 -> r = "B";
         case 2 -> r = "C";
         case 3 -> r = "D";
         case 4 -> r = "E";
         case 5 -> r = "F";
         case 6 -> r = "G";
         case 7 -> r = "H";
         case 8 -> r = "I";
         case 9 -> r = "J";
         case 10 -> r = "K";
         case 11 -> r = "L";
         case 12 -> r = "M";
         case 13 -> r = "N";
         case 14 -> r = "O";
         case 15 -> r = "P";
         case 16 -> r = "Q";
         case 17 -> r = "R";
         case 18 -> r = "S";
         case 19 -> r = "T";
         case 20 -> r = "U";
         case 21 -> r = "V";
         case 22 -> r = "W";
         case 23 -> r = "X";
         case 24 -> r = "Y";
         case 25 -> r = "Z";
      }

      return r;
   }

   public void addNumberBoldGold(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      font.setColour(Colour.GOLD);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void addNumberBoldSilver(WritableSheet var1, int var2, int var3, Double var4) throws RowsExceededException, WriteException {
      WritableFont font = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
      font.setColour(Colour.GREY_25_PERCENT);
      WritableCellFormat wcf = new WritableCellFormat(font, NumberFormats.THOUSANDS_INTEGER);
      Number number = new Number(column, row, value, wcf);
      sheet.addCell(number);
   }

   public void setColumnWidth(WritableSheet var1, int var2, int var3) throws RowsExceededException, WriteException {
      sheet.setColumnView(column, size);
   }

   public void setRowWidth(WritableSheet var1, int var2, int var3) throws RowsExceededException, WriteException {
      sheet.setColumnView(row, size);
   }
}
