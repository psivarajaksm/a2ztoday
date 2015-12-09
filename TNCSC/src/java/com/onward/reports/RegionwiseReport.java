/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports;

import com.onward.valueobjects.RecoveryDetail;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Onward
 */
public class RegionwiseReport {

    public boolean GenerateScheduleGrid(Map map, String filePath) {
        boolean result = false;
        try {
            String region = (String) map.get("regionname");
            String colgrtotal = (String) map.get("colgrtotal");
            String rowaprtotal = (String) map.get("rowaprtotal");
            String rowmaytotal = (String) map.get("rowmaytotal");
            String rowjuntotal = (String) map.get("rowjuntotal");
            String rowjultotal = (String) map.get("rowjultotal");
            String rowaugtotal = (String) map.get("rowaugtotal");
            String rowseptotal = (String) map.get("rowseptotal");
            String rowocttotal = (String) map.get("rowocttotal");
            String rownovtotal = (String) map.get("rownovtotal");
            String rowdectotal = (String) map.get("rowdectotal");
            String rowjantotal = (String) map.get("rowjantotal");
            String rowfebtotal = (String) map.get("rowfebtotal");
            String rowmartotal = (String) map.get("rowmartotal");

            String grouptype = (String) map.get("grouptype");
            String reporttype = (String) map.get("reporttype");
            List<RecoveryDetail> contentlist = (List) map.get("contentlist");

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
            if ("1".equalsIgnoreCase(reporttype)) {
                reporttype = "(Regular)";
            } else if ("2".equalsIgnoreCase(reporttype)) {
                reporttype = "(Supplementary)";
            }
            cell.setCellValue("Regionwise Report " + reporttype + " for the Group Type : " + grouptype);
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(2);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "SNO");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "REGION NAME");
            sheet.setColumnWidth(1, (short) (4500));

            createHeader(wb, row, (short) 2, "EPFNO");
            sheet.setColumnWidth(2, (short) (4500));

            createHeader(wb, row, (short) 3, "EMPLOYEE NAME");
            sheet.setColumnWidth(3, (short) (2500));

            createHeader(wb, row, (short) 4, "DESIGNATION");
            sheet.setColumnWidth(4, (short) (7500));

            createHeader(wb, row, (short) 5, "APR");
            sheet.setColumnWidth(5, (short) (3500));

            createHeader(wb, row, (short) 6, "MAY");
            sheet.setColumnWidth(6, (short) (3500));

            createHeader(wb, row, (short) 7, "JUN");
            sheet.setColumnWidth(7, (short) (3500));

            createHeader(wb, row, (short) 8, "JUL");
            sheet.setColumnWidth(8, (short) (3500));

            createHeader(wb, row, (short) 9, "AUG");
            sheet.setColumnWidth(9, (short) (3500));

            createHeader(wb, row, (short) 10, "SEP");
            sheet.setColumnWidth(10, (short) (3500));

            createHeader(wb, row, (short) 11, "OCT");
            sheet.setColumnWidth(11, (short) (3500));

            createHeader(wb, row, (short) 12, "NOV");
            sheet.setColumnWidth(12, (short) (3500));

            createHeader(wb, row, (short) 13, "DEC");
            sheet.setColumnWidth(13, (short) (3500));

            createHeader(wb, row, (short) 14, "JAN");
            sheet.setColumnWidth(14, (short) (3500));

            createHeader(wb, row, (short) 15, "FEB");
            sheet.setColumnWidth(15, (short) (3500));

            createHeader(wb, row, (short) 16, "MAR");
            sheet.setColumnWidth(16, (short) (3500));

            createHeader(wb, row, (short) 17, "TOTAL");
            sheet.setColumnWidth(17, (short) (3500));

            int j = 3;

            Iterator iterator = contentlist.iterator();
            while (iterator.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                RecoveryDetail psedm = (RecoveryDetail) iterator.next();

                createContent(wb, row, (short) 0, String.valueOf(psedm.getSno()), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, region, CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(1, (short) (4500));

                createContent(wb, row, (short) 2, psedm.getEpfno(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(2, (short) (4500));

                createContent(wb, row, (short) 3, psedm.getEmployeename(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(3, (short) (2500));

                createContent(wb, row, (short) 4, psedm.getDesignation(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(4, (short) (7500));

                createContent(wb, row, (short) 5, psedm.getApr(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(5, (short) (3500));

                createContent(wb, row, (short) 6, psedm.getMay(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(6, (short) (4000));

                createContent(wb, row, (short) 7, psedm.getJune(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(7, (short) (4000));

                createContent(wb, row, (short) 8, psedm.getJuly(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(8, (short) (4000));

                createContent(wb, row, (short) 9, psedm.getAug(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(9, (short) (4000));

                createContent(wb, row, (short) 10, psedm.getSep(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(10, (short) (4000));

                createContent(wb, row, (short) 11, psedm.getOct(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(11, (short) (4000));

                createContent(wb, row, (short) 12, psedm.getNov(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(12, (short) (4000));

                createContent(wb, row, (short) 13, psedm.getDec(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(13, (short) (4000));

                createContent(wb, row, (short) 14, psedm.getJan(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(14, (short) (4000));

                createContent(wb, row, (short) 15, psedm.getFeb(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(15, (short) (4000));

                createContent(wb, row, (short) 16, psedm.getMar(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(16, (short) (4000));

                createContent(wb, row, (short) 17, psedm.getColTotal(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(17, (short) (4000));

                j++;
            }
            row = sheet.createRow(j);
            row.setHeight((short) 300);
            createContent(wb, row, (short) 4, "MONTHWISE TOTAL", CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(4, (short) (4000));
            createContent(wb, row, (short) 5, rowaprtotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (4000));
            createContent(wb, row, (short) 6, rowmaytotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(6, (short) (4000));
            createContent(wb, row, (short) 7, rowjuntotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(7, (short) (4000));
            createContent(wb, row, (short) 8, rowjultotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(8, (short) (4000));
            createContent(wb, row, (short) 9, rowaugtotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(9, (short) (4000));
            createContent(wb, row, (short) 10, rowseptotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(10, (short) (4000));
            createContent(wb, row, (short) 11, rowocttotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(11, (short) (4000));
            createContent(wb, row, (short) 12, rownovtotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(12, (short) (4000));
            createContent(wb, row, (short) 13, rowdectotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(13, (short) (4000));
            createContent(wb, row, (short) 14, rowjantotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(14, (short) (4000));
            createContent(wb, row, (short) 15, rowfebtotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(15, (short) (4000));
            createContent(wb, row, (short) 16, rowmartotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(16, (short) (4000));
            createContent(wb, row, (short) 17, colgrtotal, CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(17, (short) (4000));

            FileOutputStream fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
            fileOut.close();
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
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }
}
