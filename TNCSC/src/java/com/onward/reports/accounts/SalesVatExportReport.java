/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.accounts;

import com.onward.valueobjects.AccountsModel;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Font;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
/**
 *
 * @author Prince vijayakumar M
 */
public class SalesVatExportReport {

    public boolean GeneratePurchaseVatExportXML(List purchaselist, String filePath) {
        boolean result = false;
        double totalamount = 0;
        double totaltaxamount = 0;
        try {
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet();

            Row row = sheet.createRow(0);
            row.setHeight((short) 500);

            createHeader(wb, row, (short) 0, "serial_no");
            sheet.setColumnWidth(0, (short) (3500));

            createHeader(wb, row, (short) 1, "Name_of_buyer");
            sheet.setColumnWidth(1, (short) (10000));

            createHeader(wb, row, (short) 2, "Buyer_TIN");
            sheet.setColumnWidth(2, (short) (4000));

            createHeader(wb, row, (short) 3, "commodity_code");
            sheet.setColumnWidth(3, (short) (4200));

            createHeader(wb, row, (short) 4, "Invoice_No");
            sheet.setColumnWidth(4, (short) (3500));

            createHeader(wb, row, (short) 5, "Invoice_Date");
            sheet.setColumnWidth(5, (short) (3500));

            createHeader(wb, row, (short) 6, "Sales_Value");
            sheet.setColumnWidth(6, (short) (4200));

            createHeader(wb, row, (short) 7, "Tax_rate");
            sheet.setColumnWidth(7, (short) (3000));

            createHeader(wb, row, (short) 8, "VAT_CST_paid");
            sheet.setColumnWidth(8, (short) (4000));

            createHeader(wb, row, (short) 9, "Category");
            sheet.setColumnWidth(9, (short) (3500));

            int j = 1;

            Iterator iterator = purchaselist.iterator();
            while (iterator.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);

                AccountsModel am = (AccountsModel) iterator.next();

                createContent2(wb, row, (short) 0, (String) am.getPageno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3500));

                createContent(wb, row, (short) 1, (String) am.getCompanyname(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(1, (short) (10000));

                createContent2(wb, row, (short) 2, (String) am.getTinno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (4000));

                createContent2(wb, row, (short) 3, (String) am.getCommoditycode(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (4200));

                createContent(wb, row, (short) 4, (String) am.getBillno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3500));

                createContent(wb, row, (short) 5, (String) am.getAccdate(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3500));

                createContent1(wb, row, (short) 6, (String) am.getAmount(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (4200));
                totalamount += Double.valueOf(am.getAmount());

                createContent1(wb, row, (short) 7, (String) am.getTaxpercentage(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, (String) am.getTaxamount(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (4000));
                totaltaxamount += Double.valueOf(am.getTaxamount());

                createContent(wb, row, (short) 9, (String) "R", CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(9, (short) (3500));

                j++;

            }

            row = sheet.createRow(j);
            row.setHeight((short) 300);

            createContent1(wb, row, (short) 6, String.valueOf(totalamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(6, (short) (4200));

            createContent1(wb, row, (short) 8, String.valueOf(totaltaxamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(8, (short) (4000));

            FileOutputStream fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
            fileOut.close();
            result = true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;

    }

    private void createHeader(Workbook workbook, Row row, short column, String cellValue) {
        Cell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    private void createContent(Workbook workbook, Row row, short column, String cellValue, short cellalign) {
        Cell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFont(font);
        cellStyle.setAlignment(cellalign);
        cell.setCellStyle(cellStyle);
    }

    private void createContent1(Workbook workbook, Row row, short column, String cellValue, short cellalign) {
        Cell cell = row.createCell(column);
        cell.setCellValue(new Double(cellValue));
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFont(font);
        cellStyle.setAlignment(cellalign);
        cell.setCellStyle(cellStyle);
    }

    private void createContent2(Workbook workbook, Row row, short column, String cellValue, short cellalign) {
        Cell cell = row.createCell(column);
        cell.setCellValue(new Long(cellValue));
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFont(font);
        cellStyle.setAlignment(cellalign);
        cell.setCellStyle(cellStyle);
    }
}
