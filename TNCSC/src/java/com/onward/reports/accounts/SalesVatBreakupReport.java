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
public class SalesVatBreakupReport {

    public boolean GeneratePurchaseVatBreakupXML(AccountsModel am, String filePath) {
        boolean result = false;
        try {
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet();

            Row row = sheet.createRow(1);
            HSSFCell cell = (HSSFCell) row.createCell((short) 0);
            Font font = wb.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontName("Courier New");
            font.setFontHeightInPoints((short) 12);
            CellStyle style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("Tamil Nadu Civil Supplies Corporation, Head Office. Sales A/c Vat  "+am.getTaxpercentage());
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));

            row = sheet.createRow(2);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontName("Courier New");
            font.setFontHeightInPoints((short) 11);
            style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("Account For the Month Of :"+am.getAccdate());
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(3);
            row.setHeight((short) 500);

            createHeader(wb, row, (short) 0, "REGION");
            sheet.setColumnWidth(0, (short) (6000));

            int i = 1;

            for (String commodity : am.getCommoditymap().values()) {
                createHeader(wb, row, (short) i, commodity);
                sheet.setColumnWidth(i, (short) (4000));
                i++;
            }
            createHeader(wb, row, (short) i, "TOTAL");
            sheet.setColumnWidth(i, (short) (4000));

            int j = 4;

            Iterator iterator = am.getAllregionCommodityMap().entrySet().iterator();
            while (iterator.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);

                Map.Entry ma = (Entry) iterator.next();

                createContent(wb, row, (short) 0, (String) ma.getKey(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(0, (short) (6000));

                List list = (List) ma.getValue();
                Iterator iit = list.iterator();
                int k = 1;
                while (iit.hasNext()) {
                    createContent1(wb, row, (short) k, (String) iit.next(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(k, (short) (4000));
                    k++;
                }
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
        font.setFontName("Courier New");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 9);
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
        font.setFontName("Courier New");
        font.setFontHeightInPoints((short) 9);
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
        font.setFontName("Courier New");
        font.setFontHeightInPoints((short) 9);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFont(font);
        cellStyle.setAlignment(cellalign);
        cell.setCellStyle(cellStyle);
    }
}
