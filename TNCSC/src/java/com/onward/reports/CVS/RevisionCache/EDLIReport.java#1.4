/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports;

/**
 *
 * @author Onward
 */
import com.onward.valueobjects.EDLIModel;
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
import org.apache.poi.ss.util.CellRangeAddress;

public class EDLIReport {

    public boolean GenerateEDLIEmployeeDetailsexcel(Map map, String filePath) {
        boolean result = false;
        try {
            String region = (String) map.get("regionname");
            String category = (String) map.get("category");
            //String fromdate = (String) map.get("fromdate");
            //String todate = (String) map.get("todate");
            List<String> headerlist = (List) map.get("headerlist");
            List<EDLIModel> contentlist = (List) map.get("contentlist");

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
            //cell.setCellValue("Service Register Report From " + fromdate + " To " + todate);
            cell.setCellValue("EDLI Employee Report - " + category);
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

            createHeader(wb, row, (short) 3, "GENDER");
            sheet.setColumnWidth(3, (short) (2500));

            createHeader(wb, row, (short) 4, "EMPLOYEE NAME");
            sheet.setColumnWidth(4, (short) (7500));

            createHeader(wb, row, (short) 5, "DESIGNATION");
            sheet.setColumnWidth(5, (short) (3500));

            createHeader(wb, row, (short) 6, "DATE OF BIRTH");
            sheet.setColumnWidth(6, (short) (3500));

            createHeader(wb, row, (short) 7, "DATE OF APPOINTMENT");
            sheet.setColumnWidth(7, (short) (3500));

            createHeader(wb, row, (short) 8, "MONTH OF RETIREMENT");
            sheet.setColumnWidth(8, (short) (3500));

            createHeader(wb, row, (short) 9, "YEAR OF RETIREMENT");
            sheet.setColumnWidth(9, (short) (3500));
            
            createHeader(wb, row, (short) 10, "TOTAL APRIL EARNINGS");
            sheet.setColumnWidth(10, (short) (3500));

            int j = 3;

            Iterator iterator = contentlist.iterator();
            while (iterator.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);

                EDLIModel edlim = (EDLIModel) iterator.next();

                createContent(wb, row, (short) 0, String.valueOf(edlim.getSlipno()), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, edlim.getRegionname(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(1, (short) (4500));

                createContent(wb, row, (short) 2, edlim.getPfno(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(2, (short) (4500));

                createContent(wb, row, (short) 3, edlim.getGender(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(3, (short) (2500));

                createContent(wb, row, (short) 4, edlim.getEmployeename(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(4, (short) (7500));

                createContent(wb, row, (short) 5, edlim.getDesignation(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(5, (short) (3500));

                createContent(wb, row, (short) 6, edlim.getDateofbirth(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(6, (short) (4000));

                createContent(wb, row, (short) 7, edlim.getDateofappoinment(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(7, (short) (4000));

                createContent(wb, row, (short) 8, edlim.getRetmonth(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(8, (short) (4000));

                createContent(wb, row, (short) 9, edlim.getRetyear(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(9, (short) (4000));
                
                createContent(wb, row, (short) 10, edlim.getEarningsamount(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (4000));

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
    
    public boolean GenerateEDLIEmployeeEarningsDeductionDetailsexcel(Map map, String filePath, String reporttype) {
        boolean result = false;
        try {
            String region = (String) map.get("regionname");
            String category = (String) map.get("category");
            List<String> headerlist = (List) map.get("headerlist");
            List<EDLIModel> contentlist = (List) map.get("contentlist");

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
            //cell.setCellValue("Service Register Report From " + fromdate + " To " + todate);
            cell.setCellValue("EDLI Employee Report - " + category);
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

            createHeader(wb, row, (short) 3, "GENDER");
            sheet.setColumnWidth(3, (short) (2500));

            createHeader(wb, row, (short) 4, "EMPLOYEE NAME");
            sheet.setColumnWidth(4, (short) (7500));

            createHeader(wb, row, (short) 5, "DESIGNATION");
            sheet.setColumnWidth(5, (short) (3500));

            createHeader(wb, row, (short) 6, "DATE OF BIRTH");
            sheet.setColumnWidth(6, (short) (3500));

            createHeader(wb, row, (short) 7, "DATE OF APPOINTMENT");
            sheet.setColumnWidth(7, (short) (3500));

            createHeader(wb, row, (short) 8, "MONTH OF RETIREMENT");
            sheet.setColumnWidth(8, (short) (3500));

            createHeader(wb, row, (short) 9, "YEAR OF RETIREMENT");
            sheet.setColumnWidth(9, (short) (3500));           
            
            if ("1".equalsIgnoreCase(reporttype)) {
                createHeader(wb, row, (short) 10, "BASIC PAY");
                sheet.setColumnWidth(10, (short) (3500));

                createHeader(wb, row, (short) 11, "SPL PAY");
                sheet.setColumnWidth(11, (short) (3500));

                createHeader(wb, row, (short) 12, "PER PAY");
                sheet.setColumnWidth(12, (short) (3500));

                createHeader(wb, row, (short) 13, "GRADE PAY");
                sheet.setColumnWidth(13, (short) (3500));

                createHeader(wb, row, (short) 14, "DA");
                sheet.setColumnWidth(14, (short) (3500));

                createHeader(wb, row, (short) 15, "HRA");
                sheet.setColumnWidth(15, (short) (3500));

                createHeader(wb, row, (short) 16, "CCA");
                sheet.setColumnWidth(16, (short) (3500));

                createHeader(wb, row, (short) 17, "CONV ALLOWANCE");
                sheet.setColumnWidth(17, (short) (3500));

                createHeader(wb, row, (short) 18, "OTHER ALLOWANCE");
                sheet.setColumnWidth(18, (short) (3500));

                createHeader(wb, row, (short) 19, "TOTAL EARNINGS");
                sheet.setColumnWidth(19, (short) (3500));
            }else if ("2".equalsIgnoreCase(reporttype)) {                
                createHeader(wb, row, (short) 10, "GPF/EPF");
                sheet.setColumnWidth(10, (short) (3500));
                
                createHeader(wb, row, (short) 11, "V.P.F.");
                sheet.setColumnWidth(11, (short) (3500));
                
                createHeader(wb, row, (short) 12, "S.P.F.");
                sheet.setColumnWidth(12, (short) (3500));
                
                createHeader(wb, row, (short) 13, "F.B.F");
                sheet.setColumnWidth(13, (short) (3500));
                
                createHeader(wb, row, (short) 14, "H.I.S.");
                sheet.setColumnWidth(14, (short) (3500));
                
                createHeader(wb, row, (short) 15, "INCOME TAX");
                sheet.setColumnWidth(15, (short) (3500));
                
                createHeader(wb, row, (short) 16, "EPF LOAN");
                sheet.setColumnWidth(16, (short) (3500));
                
                createHeader(wb, row, (short) 17, "F.A.");
                sheet.setColumnWidth(17, (short) (3500));
                
                createHeader(wb, row, (short) 18, "OTHER DEDUCATION");
                sheet.setColumnWidth(18, (short) (3500));
                
                createHeader(wb, row, (short) 19, "TOTAL DEDUCTION");
                sheet.setColumnWidth(19, (short) (3500));
                
            }else if ("3".equalsIgnoreCase(reporttype)) {
                createHeader(wb, row, (short) 10, "TOTAL EARNINGS");
                sheet.setColumnWidth(10, (short) (3500));
                
                createHeader(wb, row, (short) 11, "TOTAL DEDUCTION");
                sheet.setColumnWidth(11, (short) (3500));
                
                createHeader(wb, row, (short) 12, "NET SALARY");
                sheet.setColumnWidth(12, (short) (3500));
                
                createHeader(wb, row, (short) 13, "DESIGNATION GROUP");
                sheet.setColumnWidth(13, (short) (3500));
            }

            int j = 3;
            String totalearningsamount = "";
            String totaldeductionamount = "";
            String totalnetamount = "";

            Iterator iterator = contentlist.iterator();
            while (iterator.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);

                EDLIModel edlim = (EDLIModel) iterator.next();

                createContent(wb, row, (short) 0, String.valueOf(edlim.getSlipno()), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, edlim.getRegionname(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(1, (short) (4500));

                createContent(wb, row, (short) 2, edlim.getPfno(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(2, (short) (4500));

                createContent(wb, row, (short) 3, edlim.getGender(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(3, (short) (2500));

                createContent(wb, row, (short) 4, edlim.getEmployeename(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(4, (short) (7500));

                createContent(wb, row, (short) 5, edlim.getDesignation(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(5, (short) (3500));

                createContent(wb, row, (short) 6, edlim.getDateofbirth(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(6, (short) (4000));

                createContent(wb, row, (short) 7, edlim.getDateofappoinment(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(7, (short) (4000));

                createContent(wb, row, (short) 8, edlim.getRetmonth(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(8, (short) (4000));

                createContent(wb, row, (short) 9, edlim.getRetyear(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(9, (short) (4000));
                
                if ("1".equalsIgnoreCase(reporttype)) {
                    createContent(wb, row, (short) 10, edlim.getBasicpay(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(10, (short) (4000));

                    createContent(wb, row, (short) 11, edlim.getSplpay(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(11, (short) (4000));

                    createContent(wb, row, (short) 12, edlim.getPerpay(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(12, (short) (4000));

                    createContent(wb, row, (short) 13, edlim.getGradepay(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(13, (short) (4000));

                    createContent(wb, row, (short) 14, edlim.getDa(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(14, (short) (4000));

                    createContent(wb, row, (short) 15, edlim.getHra(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(15, (short) (4000));

                    createContent(wb, row, (short) 16, edlim.getCca(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(16, (short) (4000));

                    createContent(wb, row, (short) 17, edlim.getConvallow(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(17, (short) (4000));

                    createContent(wb, row, (short) 18, edlim.getOtheramounts(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(18, (short) (4000));

                    createContent(wb, row, (short) 19, edlim.getEarningsamount(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(19, (short) (4000));
                    totalearningsamount = edlim.getTotalearningsamount();
                }else if("2".equalsIgnoreCase(reporttype)){
                    createContent(wb, row, (short) 10, edlim.getGpf(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(10, (short) (4000));
                    
                    createContent(wb, row, (short) 11, edlim.getVpf(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(11, (short) (4000));
                    
                    createContent(wb, row, (short) 12, edlim.getSpf(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(12, (short) (4000));
                    
                    createContent(wb, row, (short) 13, edlim.getFbf(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(13, (short) (4000));
                    
                    createContent(wb, row, (short) 14, edlim.getHis(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(14, (short) (4000));
                    
                    createContent(wb, row, (short) 15, edlim.getIncometax(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(15, (short) (4000));
                    
                    createContent(wb, row, (short) 16, edlim.getEpfloan(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(16, (short) (4000));
                    
                    createContent(wb, row, (short) 17, edlim.getFa(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(17, (short) (4000));
                    
                    createContent(wb, row, (short) 18, edlim.getOtheramounts(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(18, (short) (4000));
                    
                    createContent(wb, row, (short) 19, edlim.getDeductionamount(), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(19, (short) (4000));
                    totaldeductionamount = edlim.getTotaldeductionamount();
                    
                }else if("3".equalsIgnoreCase(reporttype)){
                    totalearningsamount = edlim.getTotalearningsamount();
                    totaldeductionamount = edlim.getTotaldeductionamount();
                    totalnetamount = edlim.getTotalnetamount();
                    if ("groupwise".equalsIgnoreCase(edlim.getEarningsamount())) {
                        createContent(wb, row, (short) 9, "GROUP TOTAL", CellStyle.ALIGN_RIGHT);
                        sheet.setColumnWidth(9, (short) (4000));
                        createContent(wb, row, (short) 10, edlim.getDesignationEargrouptotal(), CellStyle.ALIGN_RIGHT);
                        sheet.setColumnWidth(10, (short) (4000));
                        createContent(wb, row, (short) 11, edlim.getDesignationDedgrouptotal(), CellStyle.ALIGN_RIGHT);
                        sheet.setColumnWidth(11, (short) (4000));
                        createContent(wb, row, (short) 12, edlim.getDesignationNetgrouptotal(), CellStyle.ALIGN_RIGHT);
                        sheet.setColumnWidth(12, (short) (4000));
                    } else {
                        createContent(wb, row, (short) 10, edlim.getEarningsamount(), CellStyle.ALIGN_RIGHT);
                        sheet.setColumnWidth(10, (short) (4000));

                        createContent(wb, row, (short) 11, edlim.getDeductionamount(), CellStyle.ALIGN_RIGHT);
                        sheet.setColumnWidth(11, (short) (4000));

                        createContent(wb, row, (short) 12, edlim.getNetamount(), CellStyle.ALIGN_RIGHT);
                        sheet.setColumnWidth(12, (short) (4000));

                        createContent(wb, row, (short) 13, edlim.getDesignationgroup(), CellStyle.ALIGN_RIGHT);
                        sheet.setColumnWidth(13, (short) (4000));
                    }
                }

                j++;
            }
            if ("1".equalsIgnoreCase(reporttype)) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                createContent(wb, row, (short) 19, totalearningsamount, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(19, (short) (4000));
            }else if ("2".equalsIgnoreCase(reporttype)) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                createContent(wb, row, (short) 19, totaldeductionamount, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(19, (short) (4000));
            }else if ("3".equalsIgnoreCase(reporttype)) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                createContent(wb, row, (short) 9, "GRAND TOTAL", CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (4000));
                createContent(wb, row, (short) 10, totalearningsamount, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (4000));
                createContent(wb, row, (short) 11, totaldeductionamount, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (4000));
                createContent(wb, row, (short) 12, totalnetamount, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (4000));
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
        Font font = workbook.createFont();
        font.setFontName("Arial");
        //font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }
}
