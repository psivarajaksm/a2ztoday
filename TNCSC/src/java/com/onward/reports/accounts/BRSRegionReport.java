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
public class BRSRegionReport {

    public boolean GenerateBRSRegionReportExcel(Map brsdetailsMap, String filePath) {
        boolean result = false;
        String regionname = (String) brsdetailsMap.get("regionname");
        String monthandyear = (String) brsdetailsMap.get("monthandyear");
        String brsstatus = (String) brsdetailsMap.get("brsstatus");
        List brsDetailsList = (List) brsdetailsMap.get("brsDetailsList");

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
            cell.setCellValue("TAMIL NADU CIVIL SUPPLIES CORPORATION, " + regionname);
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
            cell.setCellValue("BANK RECONCILIATION REPORT FOR THE PERIOD OF " + monthandyear);
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(2);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "SNO");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "REGION NAME");
            sheet.setColumnWidth(1, (short) (3500));

            createHeader(wb, row, (short) 2, "TRANSACTION DATE");
            sheet.setColumnWidth(2, (short) (3000));

            createHeader(wb, row, (short) 3, "VALUE DATE");
            sheet.setColumnWidth(3, (short) (3000));

            createHeader(wb, row, (short) 4, "DESCRIPTION");
            sheet.setColumnWidth(4, (short) (10000));

            createHeader(wb, row, (short) 5, "REF NO/CHEQUE NO");
            sheet.setColumnWidth(5, (short) (8000));

            createHeader(wb, row, (short) 6, "BRANCH CODE");
            sheet.setColumnWidth(6, (short) (3000));

            createHeader(wb, row, (short) 7, "DEBIT");
            sheet.setColumnWidth(7, (short) (4500));

            createHeader(wb, row, (short) 8, "CREDIT");
            sheet.setColumnWidth(8, (short) (4500));

            createHeader(wb, row, (short) 9, "BALANCE");
            sheet.setColumnWidth(9, (short) (4500));

            createHeader(wb, row, (short) 10, "STATUS");
            sheet.setColumnWidth(10, (short) (2000));

            int j = 3;


            Iterator iterator = brsDetailsList.iterator();
            while (iterator.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);
                
                List rowList =(List) iterator.next();


                createContent(wb, row, (short) 0, (String) rowList.get(0), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, (String) rowList.get(1), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(1, (short) (3500));

                createContent(wb, row, (short) 2, (String) rowList.get(2), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(2, (short) (3000));

                createContent(wb, row, (short) 3, (String) rowList.get(3), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(3, (short) (3000));

                createContent(wb, row, (short) 4, (String) rowList.get(4), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(4, (short) (10000));

                createContent(wb, row, (short) 5, (String) rowList.get(5), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(5, (short) (8000));

                createContent(wb, row, (short) 6, (String) rowList.get(6), CellStyle.ALIGN_CENTER);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, (String) rowList.get(7), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (4500));

                createContent1(wb, row, (short) 8, (String) rowList.get(8), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (4500));

                createContent1(wb, row, (short) 9, (String) rowList.get(9), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (4500));

                createContent(wb, row, (short) 10, (String) rowList.get(10), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(10, (short) (2000));

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
    }

    private void createContent1(Workbook workbook, Row row, short column, String cellValue, short cellalign) {
        Cell cell = row.createCell(column);
        cell.setCellValue(new Double(cellValue));
    }

    private void createContent2(Workbook workbook, Row row, short column, String cellValue, short cellalign) {
        Cell cell = row.createCell(column);
        cell.setCellValue(new Long(cellValue));
    }
}
