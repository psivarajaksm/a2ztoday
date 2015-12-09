/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.regular;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.valueobjects.PaySlipModel;
import com.onward.valueobjects.PaySlip_Earn_Deduction_Model;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.apache.poi.ss.util.CellRangeAddress;

public class LICScheduleReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] horizontalline = new char[80];
    private char[] equalline = new char[80];
    private int lineno = 1;
    private double grlicscheduletotal = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public LICScheduleReport() {
        for (int i = 0; i < 80; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 80; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 80; i++) {
            equalline[i] = '=';
        }
    }

    public void LICScheduleheader(PaySlipModel psm, PrintWriter pw) {
        pw.println();
        pw.printf("%37s%s%3s%s%4s%s%-12s%12s%-11s", "TNCSC  LIC SCHEDULE FOR THE MONTH OF", " ", psm.getPayslipmonth(), " ", psm.getPayslipyear(), " ", psm.getBranch(), "PA CODE NO: ", "0002483071");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%-4s%-9s%-31s%-15s%-7s%9s", "SNO ", "EPFNO", "NAME", "DESIG", "AMOUNT", "POLICY NO");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void getLICSchedulePrintWriter(PaySlipModel psm, String filePath) {
        System.out.println("*********************************** LICScheduleReport class getLICSchedulePrintWriter method is calling ******************************");
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (Integer.valueOf(psm.getSlipno()) == 1) {
                LICScheduleheader(psm, pw);
                lineno = 1;
                grlicscheduletotal = 0;
            }

            Iterator itr = psm.getLiclist().iterator();
            while (itr.hasNext()) {
                if (lineno == 52) {
                    pw.write(FORM_FEED);
                    LICScheduleheader(psm, pw);
                    lineno = 1;
                }
                PaySlip_Earn_Deduction_Model psedm = (PaySlip_Earn_Deduction_Model) itr.next();
                pw.printf("%3s%s%-8s%s%-30s%s%-10s%s%10s%s%-12s", psm.getSlipno(), " ", psm.getPfno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psedm.getLicamount(), " ", psedm.getLicpolicyno());
                grlicscheduletotal += Double.valueOf(psedm.getLicamount());
                psm.setSlipno("");
                psm.setPfno("");
                psm.setEmployeename("");
                psm.setDesignation("");
                pw.println();
                lineno++;
            }
            if (lineno == 52) {
//                System.out.println("lineno 52");
                pw.write(FORM_FEED);
                LICScheduleheader(psm, pw);
                lineno = 1;
            }
            pw.println();
            lineno++;
            if (psm.getPrintingrecordsize() == psm.getRecordno()) {
                pw.print(equalline);
                pw.println();
                pw.printf("%52s%3s%10s", "GRAND TOTAL", " : ", decimalFormat.format(grlicscheduletotal));
                pw.println();
                pw.print(equalline);
                pw.println();
                pw.write(FORM_FEED);
            }
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void GenerateLICEmployeeDetailsexcel(Map map, String filePath) {
        try {
            String region = (String) map.get("regionname");
            int year =  Integer.parseInt(map.get("year")+"");
            int month =  Integer.parseInt(map.get("month")+"");
            List<PaySlipModel> contentlist = (List) map.get("contentlist");

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
            cell.setCellValue("TNCSC LIC SCHEDULE FOR THE MONTH OF - " + month + " " + year + " " + region + " PA CODE NO: 002483071 ");
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(2);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "SNO");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "EPFNO");
            sheet.setColumnWidth(1, (short) (4500));

            createHeader(wb, row, (short) 2, "YEAR");
            sheet.setColumnWidth(2, (short) (4500));

            createHeader(wb, row, (short) 3, "MONTH");
            sheet.setColumnWidth(3, (short) (2500));

            createHeader(wb, row, (short) 4, "EMPLOYEE NAME");
            sheet.setColumnWidth(4, (short) (7500));

            createHeader(wb, row, (short) 5, "AMOUNT");
            sheet.setColumnWidth(5, (short) (3500));

            createHeader(wb, row, (short) 6, "POLICY NO");
            sheet.setColumnWidth(6, (short) (3500));

            int j = 3;
            Iterator iterator = contentlist.iterator();
            while (iterator.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);

                PaySlipModel paySlipModel = (PaySlipModel) iterator.next();

                createContent(wb, row, (short) 0, String.valueOf(paySlipModel.getSlipno()), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                createContent(wb, row, (short) 1, paySlipModel.getPfno(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(1, (short) (4500));

                createContent(wb, row, (short) 2, paySlipModel.getPayslipyear(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(2, (short) (4500));

                createContent(wb, row, (short) 3, paySlipModel.getPayslipmonth(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(3, (short) (2500));

                createContent(wb, row, (short) 4, paySlipModel.getEmployeename(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(4, (short) (7500));

                createContent(wb, row, (short) 5, paySlipModel.getAmount(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(5, (short) (3500));

                createContent(wb, row, (short) 6, paySlipModel.getPlino(), CellStyle.ALIGN_LEFT);
                sheet.setColumnWidth(6, (short) (4000));

                j++;
            }
            FileOutputStream fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
            fileOut.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
