/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.accounts;

import com.onward.valueobjects.AccountsModel;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
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
public class ReceiptRealizationReport1 {

    public boolean GeneratePurchaseVatExportXML(Map ReceiptRealizationmap, String filePath) {
        boolean result = false;
        String region = (String) ReceiptRealizationmap.get("regionname");
        String fromdate = (String) ReceiptRealizationmap.get("fromdate");
        String todate = (String) ReceiptRealizationmap.get("todate");
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
            cell.setCellValue("Receipt Realization Report From " + fromdate + " To " + todate);
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(2);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("REALIZED REPORT");
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            // <editor-fold defaultstate="collapsed" desc="Realized Part">

            row = sheet.createRow(3);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "SNO");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "VOUCHER DATE");
            sheet.setColumnWidth(1, (short) (4500));

            createHeader(wb, row, (short) 2, "REMITTANCE DATE");
            sheet.setColumnWidth(2, (short) (4500));

            createHeader(wb, row, (short) 3, "REALIZATION DATE");
            sheet.setColumnWidth(3, (short) (4500));

            createHeader(wb, row, (short) 4, "CHEQUE/DD NO");
            sheet.setColumnWidth(4, (short) (6500));

            createHeader(wb, row, (short) 5, "AMOUNT");
            sheet.setColumnWidth(5, (short) (4500));

            int j = 4;

            List plist = (List) ReceiptRealizationmap.get("realizedlist");

            double realizedtotalamount = 0;
            double unrealizedremittancedtotalamount = 0;
            double unrealizedunremittancedtotalamount = 0;
            double unrealizedpendingtotalamount = 0;

            Iterator iterator = plist.iterator();
            while (iterator.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);

                AccountsModel am = (AccountsModel) iterator.next();

                createContent(wb, row, (short) 0, (String) am.getPageno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, am.getVoucherapproveddate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(1, (short) (4500));

                createContent(wb, row, (short) 2, am.getRemittancedate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(2, (short) (4500));

                createContent(wb, row, (short) 3, am.getRealizationdate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(3, (short) (4500));

                createContent(wb, row, (short) 4, am.getChequeno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (6500));

                createContent1(wb, row, (short) 5, am.getAmount(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (4500));

                realizedtotalamount += Double.valueOf(am.getAmount());

                j++;
            }

            row = sheet.createRow(j);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 4, "REALIZED TOTAL", CellStyle.ALIGN_CENTER);
            sheet.setColumnWidth(4, (short) (6500));

            createContent1(wb, row, (short) 5, String.valueOf(realizedtotalamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (4500));

            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="UnRealized Remittanced Part">

            j = j + 2;

            row = sheet.createRow(j);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("UNREALIZED REMITTANCE REPORT");
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(j + 1);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "SNO");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "VOUCHER DATE");
            sheet.setColumnWidth(1, (short) (4500));

            createHeader(wb, row, (short) 2, "REMITTANCE DATE");
            sheet.setColumnWidth(2, (short) (4500));

            createHeader(wb, row, (short) 3, "REALIZATION DATE");
            sheet.setColumnWidth(3, (short) (4500));

            createHeader(wb, row, (short) 4, "CHEQUE/DD NO");
            sheet.setColumnWidth(4, (short) (6500));

            createHeader(wb, row, (short) 5, "AMOUNT");
            sheet.setColumnWidth(5, (short) (4500));

            j = j + 2;

            plist = (List) ReceiptRealizationmap.get("unrealizedremittedlist");

            iterator = plist.iterator();
            while (iterator.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);

                AccountsModel am = (AccountsModel) iterator.next();

                createContent(wb, row, (short) 0, (String) am.getPageno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, am.getVoucherapproveddate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(1, (short) (4500));

                createContent(wb, row, (short) 2, am.getRemittancedate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(2, (short) (4500));

                createContent(wb, row, (short) 3, am.getRealizationdate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(3, (short) (4500));

                createContent(wb, row, (short) 4, am.getChequeno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (6500));

                createContent1(wb, row, (short) 5, am.getAmount(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (4500));

                unrealizedremittancedtotalamount += Double.valueOf(am.getAmount());

                j++;
            }

            row = sheet.createRow(j);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 4, "UNREALIZED REMITTANCE TOTAL", CellStyle.ALIGN_CENTER);
            sheet.setColumnWidth(4, (short) (6500));

            createContent1(wb, row, (short) 5, String.valueOf(unrealizedremittancedtotalamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (4500));

            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="UnRealized Un Remittance Part">

            j = j + 2;

            row = sheet.createRow(j);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("UNREALIZED UNREMITTANCE REPORT");
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(j + 1);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "SNO");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "VOUCHER DATE");
            sheet.setColumnWidth(1, (short) (4500));

            createHeader(wb, row, (short) 2, "REMITTANCE DATE");
            sheet.setColumnWidth(2, (short) (4500));

            createHeader(wb, row, (short) 3, "REALIZATION DATE");
            sheet.setColumnWidth(3, (short) (4500));

            createHeader(wb, row, (short) 4, "CHEQUE/DD NO");
            sheet.setColumnWidth(4, (short) (6500));

            createHeader(wb, row, (short) 5, "AMOUNT");
            sheet.setColumnWidth(5, (short) (4500));

            j = j + 2;

            plist = (List) ReceiptRealizationmap.get("unrealizedunremittancelist");

            iterator = plist.iterator();
            while (iterator.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);

                AccountsModel am = (AccountsModel) iterator.next();

                createContent(wb, row, (short) 0, (String) am.getPageno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, am.getVoucherapproveddate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(1, (short) (4500));

                createContent(wb, row, (short) 2, am.getRemittancedate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(2, (short) (4500));

                createContent(wb, row, (short) 3, am.getRealizationdate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(3, (short) (4500));

                createContent(wb, row, (short) 4, am.getChequeno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (6500));

                createContent1(wb, row, (short) 5, am.getAmount(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (4500));

                unrealizedunremittancedtotalamount += Double.valueOf(am.getAmount());

                j++;
            }

            row = sheet.createRow(j);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 4, "UNREALIZED UNREMITTANCE TOTAL", CellStyle.ALIGN_CENTER);
            sheet.setColumnWidth(4, (short) (6500));

            createContent1(wb, row, (short) 5, String.valueOf(unrealizedunremittancedtotalamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (4500));

            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="UnRealized Pending Part">

            j = j + 2;

            row = sheet.createRow(j);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("UNREALIZED PENDING REPORT");
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(j + 1);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "SNO");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "VOUCHER DATE");
            sheet.setColumnWidth(1, (short) (4500));

            createHeader(wb, row, (short) 2, "REMITTANCE DATE");
            sheet.setColumnWidth(2, (short) (4500));

            createHeader(wb, row, (short) 3, "REALIZATION DATE");
            sheet.setColumnWidth(3, (short) (4500));

            createHeader(wb, row, (short) 4, "CHEQUE/DD NO");
            sheet.setColumnWidth(4, (short) (6500));

            createHeader(wb, row, (short) 5, "AMOUNT");
            sheet.setColumnWidth(5, (short) (4500));

            /*Unrealized Pending Part*/

            j = j + 2;

            plist = (List) ReceiptRealizationmap.get("unrealizedpendinglist");

            iterator = plist.iterator();
            while (iterator.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);

                AccountsModel am = (AccountsModel) iterator.next();

                createContent(wb, row, (short) 0, (String) am.getPageno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, am.getVoucherapproveddate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(1, (short) (4500));

                createContent(wb, row, (short) 2, am.getRemittancedate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(2, (short) (4500));

                createContent(wb, row, (short) 3, am.getRealizationdate(), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(3, (short) (4500));

                createContent(wb, row, (short) 4, am.getChequeno(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (6500));

                createContent1(wb, row, (short) 5, am.getAmount(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (4500));

                unrealizedpendingtotalamount += Double.valueOf(am.getAmount());

                j++;
            }

            row = sheet.createRow(j);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 4, "UNREALIZED PENDING TOTAL", CellStyle.ALIGN_CENTER);
            sheet.setColumnWidth(4, (short) (6500));

            createContent1(wb, row, (short) 5, String.valueOf(unrealizedpendingtotalamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (4500));

            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Grand Part">

            row = sheet.createRow(j + 3);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 4, "REALIZED TOTAL", CellStyle.ALIGN_CENTER);
            sheet.setColumnWidth(4, (short) (6500));

            createContent1(wb, row, (short) 5, String.valueOf(realizedtotalamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (4500));

            row = sheet.createRow(j + 4);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 4, "UNREALIZED REMITTANCE TOTAL", CellStyle.ALIGN_CENTER);
            sheet.setColumnWidth(4, (short) (6500));

            createContent1(wb, row, (short) 5, String.valueOf(unrealizedremittancedtotalamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (4500));

            row = sheet.createRow(j + 5);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 4, "UNREALIZED UNREMITTANCE TOTAL", CellStyle.ALIGN_CENTER);
            sheet.setColumnWidth(4, (short) (6500));

            createContent1(wb, row, (short) 5, String.valueOf(unrealizedunremittancedtotalamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (4500));

            row = sheet.createRow(j + 6);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 4, "UNREALIZED PENDING TOTAL", CellStyle.ALIGN_CENTER);
            sheet.setColumnWidth(4, (short) (6500));

            createContent1(wb, row, (short) 5, String.valueOf(unrealizedpendingtotalamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (4500));

            row = sheet.createRow(j + 7);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 4, "GRANT TOTAL", CellStyle.ALIGN_CENTER);
            sheet.setColumnWidth(4, (short) (6500));

            createContent1(wb, row, (short) 5, String.valueOf(realizedtotalamount + unrealizedremittancedtotalamount + unrealizedunremittancedtotalamount + unrealizedpendingtotalamount), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (4500));

            // </editor-fold>

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
