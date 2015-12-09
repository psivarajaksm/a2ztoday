/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.regionwise;

import com.onward.valueobjects.AccountsModel;
import com.onward.valueobjects.RegionwiseModel;
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
public class ServiceRegisterDetailsReport {

    public boolean GenerateServiceRegisterDetailsexcel(Map map, String filePath) {
        boolean result = false;
        try {
            String region = (String) map.get("regionname");
            String fromdate = (String) map.get("fromdate");
            String todate = (String) map.get("todate");
            List<String> headerlist = (List) map.get("headerlist");
            List<RegionwiseModel> contentlist = (List) map.get("contentlist");

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
            cell.setCellValue("Service Register Report From " + fromdate + " To " + todate);
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(2);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "SNO");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "EPFNO");
            sheet.setColumnWidth(1, (short) (4500));

            createHeader(wb, row, (short) 2, "EMPLOYEE NAME");
            sheet.setColumnWidth(2, (short) (6500));

            createHeader(wb, row, (short) 3, "DESIGNATION");
            sheet.setColumnWidth(3, (short) (5500));

            Iterator itr = headerlist.iterator();
            int h = 4;
            while (itr.hasNext()) {
                createHeader(wb, row, (short) h, (String) itr.next());
                sheet.setColumnWidth(h, (short) (4500));
                h++;
            }

            int j = 3;

            Iterator iterator = contentlist.iterator();
            while (iterator.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);

                RegionwiseModel rm = (RegionwiseModel) iterator.next();

                createContent(wb, row, (short) 0, String.valueOf(rm.getSlipno()), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, rm.getPfno(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(1, (short) (4500));

                createContent(wb, row, (short) 2, rm.getEmployeename(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(2, (short) (6500));

                createContent(wb, row, (short) 3, rm.getDesignation(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(3, (short) (5500));

                int c = 4;
                Iterator it = rm.getAmountlist().iterator();
                while (it.hasNext()) {
                    String amo = (String) it.next();
                    createContent(wb, row, (short) c, amo, CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(c, (short) (4500));
                    c++;
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

}
