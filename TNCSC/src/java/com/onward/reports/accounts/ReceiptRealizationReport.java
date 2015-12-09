/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.accounts;

import com.onward.valueobjects.AccountsModel;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Prince vijayakumar M
 */
public class ReceiptRealizationReport {

    public boolean GeneratePurchaseVatExportXML(Map ReceiptRealizationmap, String filePath) {
        boolean result = false;
        String region = (String) ReceiptRealizationmap.get("regionname");
//        String fromdate = (String) PaymentRealizationmap.get("fromdate");
//        String todate = (String) PaymentRealizationmap.get("todate");
        try {

            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet();

            Row row = sheet.createRow(0);
            row.setHeight((short) 500);
            HSSFCell cell = (HSSFCell) row.createCell((short) 0);
            Font font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            CellStyle style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("Tamil Nadu Civil Supplies Corporation, " + region);
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

            row = sheet.createRow(1);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);
//            cell.setCellValue("Payment Realization Report From " + fromdate + " To " + todate);
            cell.setCellValue("Receipt Realization Report");
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(2);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "SNO");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "CHEQUE/DD NO");
            sheet.setColumnWidth(1, (short) (4500));

            createHeader(wb, row, (short) 2, "AMOUNT");
            sheet.setColumnWidth(2, (short) (4500));

            createHeader(wb, row, (short) 3, "REMITTANCE DATE");
            sheet.setColumnWidth(3, (short) (4500));

            createHeader(wb, row, (short) 4, "REALIZATION DATE");
            sheet.setColumnWidth(4, (short) (4500));

            createHeader(wb, row, (short) 5, "PAYMENT MODE");
            sheet.setColumnWidth(5, (short) (4500));

            createHeader(wb, row, (short) 6, "CHALLAN NO");
            sheet.setColumnWidth(6, (short) (4500));

            createHeader(wb, row, (short) 7, "CHEQUE/DD DATE");
            sheet.setColumnWidth(7, (short) (4500));

            int j = 3;

            List plist = (List) ReceiptRealizationmap.get("realizationlist");

            Iterator iterator = plist.iterator();
            while (iterator.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);

                AccountsModel am = (AccountsModel) iterator.next();

                createContent(wb, row, (short) 0, (String) am.getPageno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, am.getChequeno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (4500));

                createContent1(wb, row, (short) 2, am.getAmount(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (4500));

                createContent(wb, row, (short) 3, am.getRemittancedate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(3, (short) (4500));

                createContent(wb, row, (short) 4, am.getAccdate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(4, (short) (4500));

                createContent(wb, row, (short) 5, am.getPaymentmode(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(5, (short) (4500));

                createContent(wb, row, (short) 6, am.getChallanno(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(6, (short) (4500));

                createContent(wb, row, (short) 7, am.getChequedate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(7, (short) (4500));

                j++;
            }

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
