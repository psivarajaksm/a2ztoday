/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.accounts;

import com.onward.valueobjects.AccountsModel;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.Map.Entry;
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
public class ChequeRegisterReport {

    public boolean GenerateChequeRegisterExportXML(Map ReceiptRealizationmap, String filePath) {
        boolean result = false;
        String region = (String) ReceiptRealizationmap.get("regionname");
        String fromdate = (String) ReceiptRealizationmap.get("fromdate");
        String todate = (String) ReceiptRealizationmap.get("todate");
        LinkedHashMap headermap = (LinkedHashMap) ReceiptRealizationmap.get("headermap");

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
            cell.setCellValue("TAMIL NADU CIVIL SUPPLIES CORPORATION, " + region);
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
            cell.setCellValue("CHEQUE REGISTER FROM " + fromdate + " TO " + todate);
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(2);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "SNO");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "CHEQUE NO");
            sheet.setColumnWidth(1, (short) (3500));

            createHeader(wb, row, (short) 2, "CHEQUE DATE");
            sheet.setColumnWidth(2, (short) (3500));

            createHeader(wb, row, (short) 3, "COMPUTER NO");
            sheet.setColumnWidth(3, (short) (3500));

            createHeader(wb, row, (short) 4, "VOUCHER NO");
            sheet.setColumnWidth(4, (short) (3500));

            createHeader(wb, row, (short) 5, "VOUCHER APPROVED DATE");
            sheet.setColumnWidth(5, (short) (3500));

            createHeader(wb, row, (short) 6, "PARTY NAME");
            sheet.setColumnWidth(6, (short) (8500));

            int cl = 7;

            Iterator hitr = headermap.entrySet().iterator();

            while (hitr.hasNext()) {
                Map.Entry me = (Map.Entry) hitr.next();
                String bankname = (String) me.getValue();

                createHeader(wb, row, (short) cl, bankname);
                sheet.setColumnWidth(cl, (short) (4500));

                cl++;
            }

            int j = 3;

            List chequelist = (List) ReceiptRealizationmap.get("chequelist");

            Iterator iterator = chequelist.iterator();
            while (iterator.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);

                AccountsModel am = (AccountsModel) iterator.next();

                createContent(wb, row, (short) 0, (String) am.getSlipno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, am.getChequeno(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(1, (short) (3500));

                createContent(wb, row, (short) 2, am.getChequedate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(2, (short) (3500));

                createContent(wb, row, (short) 3, am.getCompno(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(3, (short) (3500));

                createContent(wb, row, (short) 4, am.getVoucherno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3500));

                createContent(wb, row, (short) 5, am.getVoucherapproveddate(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3500));

                createContent(wb, row, (short) 6, am.getPartyname(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (8500));

                int cll = 7;
                Iterator bit = (am.getBanklist()).iterator();
                while (bit.hasNext()) {
                    String amount = (String) bit.next();
                    createContent1(wb, row, (short) cll, amount, CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(cll, (short) (4500));
                    cll++;
                }

                j++;
            }

//            row = sheet.createRow(j);
//            row.setHeight((short) 300);
//            
//            createContent(wb, row, (short) 4, "REALIZED TOTAL", CellStyle.ALIGN_CENTER);
//            sheet.setColumnWidth(4, (short) (6500));
//            
//            createContent1(wb, row, (short) 5, String.valueOf(realizedtotalamount), CellStyle.ALIGN_RIGHT);
//            sheet.setColumnWidth(5, (short) (4500));


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
//        CellStyle cellStyle = workbook.createCellStyle();
//        Font font = workbook.createFont();
//        font.setFontName("Arial");
//        font.setFontHeightInPoints((short) 10);
//        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
//        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        cellStyle.setFont(font);
//        cellStyle.setAlignment(cellalign);
//        cell.setCellStyle(cellStyle);
    }

    private void createContent1(Workbook workbook, Row row, short column, String cellValue, short cellalign) {
        Cell cell = row.createCell(column);
        cell.setCellValue(new Double(cellValue));
//        CellStyle cellStyle = workbook.createCellStyle();
//        Font font = workbook.createFont();
//        font.setFontName("Arial");
//        font.setFontHeightInPoints((short) 10);
//        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
//        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        cellStyle.setFont(font);
//        cellStyle.setAlignment(cellalign);
//        cell.setCellStyle(cellStyle);
    }

    private void createContent2(Workbook workbook, Row row, short column, String cellValue, short cellalign) {
        Cell cell = row.createCell(column);
        cell.setCellValue(new Long(cellValue));
//        CellStyle cellStyle = workbook.createCellStyle();
//        Font font = workbook.createFont();
//        font.setFontName("Arial");
//        font.setFontHeightInPoints((short) 10);
//        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
//        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        cellStyle.setFont(font);
//        cellStyle.setAlignment(cellalign);
//        cell.setCellStyle(cellStyle);
    }
}
