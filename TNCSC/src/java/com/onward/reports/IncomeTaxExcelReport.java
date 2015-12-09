/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports;

import com.onward.reports.accounts.*;

import com.onward.valueobjects.AccountsModel;
import com.onward.valueobjects.PaySlipModel;
import com.onward.valueobjects.PaySlip_Earn_Deduction_Model;
import java.io.File;
import java.io.FileInputStream;
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
public class IncomeTaxExcelReport {

    private int j = 0;

    public boolean GenerateIncomeTaxExportXML(PaySlipModel psm, String filePath) {
        boolean result = false;

        try {
            File xlsxFile = new File(filePath);
            Workbook wb = null;
            Sheet sheet = null;
            if (xlsxFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(xlsxFile);
                wb = new HSSFWorkbook(fileInputStream);
                sheet = wb.getSheet("IncomeTax");
            } else {
                wb = new HSSFWorkbook();
                sheet = wb.createSheet("IncomeTax");
            }

            Row row = sheet.createRow(j);
            row.setHeight((short) 500);
            HSSFCell cell = (HSSFCell) row.createCell((short) 0);
            Font font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            CellStyle style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("TNCSC.," + psm.getBranch() + "    TENTATIVE PAY PARTICULARS FOR THE YEAR " + psm.getPayslipstartingdate() + "-" + psm.getPayslipenddate());
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            j++;

            row = sheet.createRow(j);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("NAME         :" + psm.getEmployeename() + "     EPF NO : " + psm.getPfno());
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));
            j++;

            row = sheet.createRow(j);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("DESIGNATION  : " + psm.getDesignation() + "     SECTION     : " + psm.getSectionname());
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));
            j++;

            row = sheet.createRow(j);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "MONTH");
            sheet.setColumnWidth(0, (short) (3000));

            createHeader(wb, row, (short) 1, "PAY");
            sheet.setColumnWidth(1, (short) (3000));

            createHeader(wb, row, (short) 2, "S.PAY");
            sheet.setColumnWidth(2, (short) (3000));

            createHeader(wb, row, (short) 3, "DA");
            sheet.setColumnWidth(3, (short) (3000));

            createHeader(wb, row, (short) 4, "GRADE PAY");
            sheet.setColumnWidth(4, (short) (3000));

            createHeader(wb, row, (short) 5, "HRA");
            sheet.setColumnWidth(5, (short) (3000));

            createHeader(wb, row, (short) 6, "CCA");
            sheet.setColumnWidth(6, (short) (3000));

            createHeader(wb, row, (short) 7, "OTHERS");
            sheet.setColumnWidth(7, (short) (3000));

            createHeader(wb, row, (short) 8, "GROSS");
            sheet.setColumnWidth(8, (short) (3000));

            createHeader(wb, row, (short) 9, "I.TAX");
            sheet.setColumnWidth(9, (short) (3000));

            createHeader(wb, row, (short) 10, "EPF/GPF");
            sheet.setColumnWidth(10, (short) (3000));

            createHeader(wb, row, (short) 11, "VPF");
            sheet.setColumnWidth(11, (short) (3000));

            createHeader(wb, row, (short) 12, "PROF.TAX");
            sheet.setColumnWidth(12, (short) (3000));

            createHeader(wb, row, (short) 13, "HIS");
            sheet.setColumnWidth(13, (short) (3000));

            createHeader(wb, row, (short) 14, "FBF");
            sheet.setColumnWidth(14, (short) (3000));

            j++;


            Iterator itr = psm.getRegularlist().iterator();
            while (itr.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();

                createContent(wb, row, (short) 0, (String) psedm1.getPeriod(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3000));

                createContent1(wb, row, (short) 1, psedm1.getBasic(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (3000));

                createContent1(wb, row, (short) 2, psedm1.getSpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (3000));

                createContent1(wb, row, (short) 3, psedm1.getDa(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (3000));

                createContent1(wb, row, (short) 4, psedm1.getGrpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3000));

                createContent1(wb, row, (short) 5, psedm1.getHra(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3000));

                createContent1(wb, row, (short) 6, psedm1.getCca(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, psedm1.getOthers(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, psedm1.getGross(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (3000));

                createContent1(wb, row, (short) 9, psedm1.getItax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (3000));

                createContent1(wb, row, (short) 10, psedm1.getEpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (3000));

                createContent1(wb, row, (short) 11, psedm1.getVpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (3000));

                createContent1(wb, row, (short) 12, psedm1.getProftax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (3000));

                createContent1(wb, row, (short) 13, psedm1.getHis(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(13, (short) (3000));

                createContent1(wb, row, (short) 14, psedm1.getFbf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(14, (short) (3000));
                j++;
            }

            row = sheet.createRow(j);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 0, (String) "TOTAL", CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(0, (short) (3000));

            createContent2(wb, row, (short) 1, psm.getTotalmap().get("basic_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(1, (short) (3000));

            createContent2(wb, row, (short) 2, psm.getTotalmap().get("spay_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(2, (short) (3000));

            createContent2(wb, row, (short) 3, psm.getTotalmap().get("da_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(3, (short) (3000));

            createContent2(wb, row, (short) 4, psm.getTotalmap().get("grpay_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(4, (short) (3000));

            createContent2(wb, row, (short) 5, psm.getTotalmap().get("hra_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (3000));

            createContent2(wb, row, (short) 6, psm.getTotalmap().get("cca_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(6, (short) (3000));

            createContent2(wb, row, (short) 7, psm.getTotalmap().get("others_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(7, (short) (3000));

            createContent2(wb, row, (short) 8, psm.getTotalmap().get("gross_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(8, (short) (3000));

            createContent2(wb, row, (short) 9, psm.getTotalmap().get("it_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(9, (short) (3000));

            createContent2(wb, row, (short) 10, psm.getTotalmap().get("epf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(10, (short) (3000));

            createContent2(wb, row, (short) 11, psm.getTotalmap().get("vpf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(11, (short) (3000));

            createContent2(wb, row, (short) 12, psm.getTotalmap().get("proftax_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(12, (short) (3000));

            createContent2(wb, row, (short) 13, psm.getTotalmap().get("his_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(13, (short) (3000));

            createContent2(wb, row, (short) 14, psm.getTotalmap().get("fbf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(14, (short) (3000));
            j++;

            itr = psm.getLeavesurrenderlist().iterator();
            while (itr.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();

                String type = null;
                if (psedm1.getPeriod() != null) {
                    type = "SL" + psedm1.getPeriod();
                } else {
                    type = "SL";
                }

                createContent(wb, row, (short) 0, (String) type, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3000));

                createContent1(wb, row, (short) 1, psedm1.getBasic(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (3000));

                createContent1(wb, row, (short) 2, psedm1.getSpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (3000));

                createContent1(wb, row, (short) 3, psedm1.getDa(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (3000));

                createContent1(wb, row, (short) 4, psedm1.getGrpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3000));

                createContent1(wb, row, (short) 5, psedm1.getHra(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3000));

                createContent1(wb, row, (short) 6, psedm1.getCca(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, psedm1.getOthers(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, psedm1.getGross(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (3000));

                createContent1(wb, row, (short) 9, psedm1.getItax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (3000));

                createContent1(wb, row, (short) 10, psedm1.getEpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (3000));

                createContent1(wb, row, (short) 11, psedm1.getVpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (3000));

                createContent1(wb, row, (short) 12, psedm1.getProftax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (3000));

                createContent1(wb, row, (short) 13, psedm1.getHis(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(13, (short) (3000));

                createContent1(wb, row, (short) 14, psedm1.getFbf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(14, (short) (3000));

                j++;
            }

            itr = psm.getDaarrearlist().iterator();
            while (itr.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();

                String type = null;
                if (psedm1.getPeriod() != null) {
                    type = "DA" + psedm1.getPeriod();
                } else {
                    type = "DA";
                }

                createContent(wb, row, (short) 0, (String) type, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3000));

                createContent1(wb, row, (short) 1, psedm1.getBasic(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (3000));

                createContent1(wb, row, (short) 2, psedm1.getSpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (3000));

                createContent1(wb, row, (short) 3, psedm1.getDa(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (3000));

                createContent1(wb, row, (short) 4, psedm1.getGrpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3000));

                createContent1(wb, row, (short) 5, psedm1.getHra(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3000));

                createContent1(wb, row, (short) 6, psedm1.getCca(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, psedm1.getOthers(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, psedm1.getGross(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (3000));

                createContent1(wb, row, (short) 9, psedm1.getItax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (3000));

                createContent1(wb, row, (short) 10, psedm1.getEpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (3000));

                createContent1(wb, row, (short) 11, psedm1.getVpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (3000));

                createContent1(wb, row, (short) 12, psedm1.getProftax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (3000));

                createContent1(wb, row, (short) 13, psedm1.getHis(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(13, (short) (3000));

                createContent1(wb, row, (short) 14, psedm1.getFbf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(14, (short) (3000));

                j++;
            }

            itr = psm.getIncrementarrearlist().iterator();
            while (itr.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();

                String type = null;
                if (psedm1.getPeriod() != null) {
                    type = "IA" + psedm1.getPeriod();
                } else {
                    type = "IA";
                }

                createContent(wb, row, (short) 0, (String) type, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3000));

                createContent1(wb, row, (short) 1, psedm1.getBasic(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (3000));

                createContent1(wb, row, (short) 2, psedm1.getSpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (3000));

                createContent1(wb, row, (short) 3, psedm1.getDa(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (3000));

                createContent1(wb, row, (short) 4, psedm1.getGrpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3000));

                createContent1(wb, row, (short) 5, psedm1.getHra(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3000));

                createContent1(wb, row, (short) 6, psedm1.getCca(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, psedm1.getOthers(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, psedm1.getGross(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (3000));

                createContent1(wb, row, (short) 9, psedm1.getItax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (3000));

                createContent1(wb, row, (short) 10, psedm1.getEpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (3000));

                createContent1(wb, row, (short) 11, psedm1.getVpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (3000));

                createContent1(wb, row, (short) 12, psedm1.getProftax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (3000));

                createContent1(wb, row, (short) 13, psedm1.getHis(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(13, (short) (3000));

                createContent1(wb, row, (short) 14, psedm1.getFbf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(14, (short) (3000));

                j++;
            }

            itr = psm.getBonuslist().iterator();
            while (itr.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();

                String type = null;
                if (psedm1.getPeriod() != null) {
                    //type = "BON" + psedm1.getPeriod();
                    type = psedm1.getPaycodename() + psedm1.getPeriod();
                } else {
                    type = psedm1.getPaycodename();
                }

                createContent(wb, row, (short) 0, (String) type, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3000));

                createContent1(wb, row, (short) 1, psedm1.getBasic(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (3000));

                createContent1(wb, row, (short) 2, psedm1.getSpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (3000));

                createContent1(wb, row, (short) 3, psedm1.getDa(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (3000));

                createContent1(wb, row, (short) 4, psedm1.getGrpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3000));

                createContent1(wb, row, (short) 5, psedm1.getHra(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3000));

                createContent1(wb, row, (short) 6, psedm1.getCca(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, psedm1.getOthers(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, psedm1.getGross(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (3000));

                createContent1(wb, row, (short) 9, psedm1.getItax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (3000));

                createContent1(wb, row, (short) 10, psedm1.getEpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (3000));

                createContent1(wb, row, (short) 11, psedm1.getVpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (3000));

                createContent1(wb, row, (short) 12, psedm1.getProftax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (3000));

                createContent1(wb, row, (short) 13, psedm1.getHis(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(13, (short) (3000));

                createContent1(wb, row, (short) 14, psedm1.getFbf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(14, (short) (3000));

                j++;
            }

            row = sheet.createRow(j);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 0, (String) "TOTAL", CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(0, (short) (3000));

            createContent2(wb, row, (short) 1, psm.getTotalmap().get("grant_basic_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(1, (short) (3000));

            createContent2(wb, row, (short) 2, psm.getTotalmap().get("grant_spay_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(2, (short) (3000));

            createContent2(wb, row, (short) 3, psm.getTotalmap().get("grant_da_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(3, (short) (3000));

            createContent2(wb, row, (short) 4, psm.getTotalmap().get("grant_grpay_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(4, (short) (3000));

            createContent2(wb, row, (short) 5, psm.getTotalmap().get("grant_hra_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (3000));

            createContent2(wb, row, (short) 6, psm.getTotalmap().get("grant_cca_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(6, (short) (3000));

            createContent2(wb, row, (short) 7, psm.getTotalmap().get("grant_others_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(7, (short) (3000));

            createContent2(wb, row, (short) 8, psm.getTotalmap().get("grant_gross_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(8, (short) (3000));

            createContent2(wb, row, (short) 9, psm.getTotalmap().get("grant_it_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(9, (short) (3000));

            createContent2(wb, row, (short) 10, psm.getTotalmap().get("grant_epf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(10, (short) (3000));

            createContent2(wb, row, (short) 11, psm.getTotalmap().get("grant_vpf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(11, (short) (3000));

            createContent2(wb, row, (short) 12, psm.getTotalmap().get("grant_proftax_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(12, (short) (3000));

            createContent2(wb, row, (short) 13, psm.getTotalmap().get("grant_his_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(13, (short) (3000));

            createContent2(wb, row, (short) 14, psm.getTotalmap().get("grant_fbf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(14, (short) (3000));
//            j++;

            j = j + 2;

            FileOutputStream fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
            fileOut.close();
            result = true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;

    }

    public boolean GenerateIncomeTaxExportXMLOLD(PaySlipModel psm, String filePath) {
        boolean result = false;

        try {
            File xlsxFile = new File(filePath);
            Workbook wb = null;
            Sheet sheet = null;
            if (xlsxFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(xlsxFile);
                wb = new HSSFWorkbook(fileInputStream);
                sheet = wb.getSheet("IncomeTax");
            } else {
                wb = new HSSFWorkbook();
                sheet = wb.createSheet("IncomeTax");
            }

            Row row = sheet.createRow(j);
            row.setHeight((short) 500);
            HSSFCell cell = (HSSFCell) row.createCell((short) 0);
            Font font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            CellStyle style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("TNCSC.," + psm.getBranch() + "    TENTATIVE PAY PARTICULARS FOR THE YEAR " + psm.getPayslipstartingdate() + "-" + psm.getPayslipenddate());
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            j++;

            row = sheet.createRow(j);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("NAME         :" + psm.getEmployeename() + "     EPF NO : " + psm.getPfno());
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));
            j++;

            row = sheet.createRow(j);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("DESIGNATION  : " + psm.getDesignation() + "     SECTION     : " + psm.getSectionname());
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));
            j++;

            row = sheet.createRow(j);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "MONTH");
            sheet.setColumnWidth(0, (short) (3000));

            createHeader(wb, row, (short) 1, "PAY");
            sheet.setColumnWidth(1, (short) (3000));

            createHeader(wb, row, (short) 2, "S.PAY");
            sheet.setColumnWidth(2, (short) (3000));

//            createHeader(wb, row, (short) 3, "PP");
//            sheet.setColumnWidth(3, (short) (3000));

            createHeader(wb, row, (short) 4, "DA");
            sheet.setColumnWidth(4, (short) (3000));

            createHeader(wb, row, (short) 5, "GRADE PAY");
            sheet.setColumnWidth(5, (short) (3000));

            createHeader(wb, row, (short) 6, "HRA");
            sheet.setColumnWidth(6, (short) (3000));

            createHeader(wb, row, (short) 7, "CCA");
            sheet.setColumnWidth(7, (short) (3000));

            createHeader(wb, row, (short) 8, "OTHERS");
            sheet.setColumnWidth(8, (short) (3000));

            createHeader(wb, row, (short) 9, "GROSS");
            sheet.setColumnWidth(9, (short) (3000));

            createHeader(wb, row, (short) 10, "I.TAX");
            sheet.setColumnWidth(10, (short) (3000));

            createHeader(wb, row, (short) 11, "EPF");
            sheet.setColumnWidth(11, (short) (3000));

            createHeader(wb, row, (short) 12, "VPF");
            sheet.setColumnWidth(12, (short) (3000));

            createHeader(wb, row, (short) 13, "PROF.TAX");
            sheet.setColumnWidth(13, (short) (3000));

            createHeader(wb, row, (short) 14, "HIS");
            sheet.setColumnWidth(14, (short) (3000));

            createHeader(wb, row, (short) 15, "FBF");
            sheet.setColumnWidth(15, (short) (3000));

            j++;


            Iterator itr = psm.getRegularlist().iterator();
            while (itr.hasNext()) {

                row = sheet.createRow(j);
                row.setHeight((short) 300);
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();

                createContent(wb, row, (short) 0, (String) psedm1.getPeriod(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3000));

                createContent1(wb, row, (short) 1, psedm1.getBasic(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (3000));

                createContent1(wb, row, (short) 2, psedm1.getSpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (3000));

                createContent1(wb, row, (short) 3, psedm1.getPp(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (3000));

                createContent1(wb, row, (short) 4, psedm1.getDa(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3000));

                createContent1(wb, row, (short) 5, psedm1.getGrpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3000));

                createContent1(wb, row, (short) 6, psedm1.getHra(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, psedm1.getCca(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, psedm1.getOthers(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (3000));

                createContent1(wb, row, (short) 9, psedm1.getGross(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (3000));

                createContent1(wb, row, (short) 10, psedm1.getItax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (3000));

                createContent1(wb, row, (short) 11, psedm1.getEpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (3000));

                createContent1(wb, row, (short) 12, psedm1.getVpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (3000));

                createContent1(wb, row, (short) 13, psedm1.getProftax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(13, (short) (3000));

                createContent1(wb, row, (short) 14, psedm1.getHis(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(14, (short) (3000));

                createContent1(wb, row, (short) 15, psedm1.getFbf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(15, (short) (3000));
                j++;
            }

            row = sheet.createRow(j);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 0, (String) "TOTAL", CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(0, (short) (3000));

            createContent2(wb, row, (short) 1, psm.getTotalmap().get("basic_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(1, (short) (3000));

            createContent2(wb, row, (short) 2, psm.getTotalmap().get("spay_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(2, (short) (3000));

            createContent2(wb, row, (short) 3, psm.getTotalmap().get("pp_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(3, (short) (3000));

            createContent2(wb, row, (short) 4, psm.getTotalmap().get("da_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(4, (short) (3000));

            createContent2(wb, row, (short) 5, psm.getTotalmap().get("grpay_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (3000));

            createContent2(wb, row, (short) 6, psm.getTotalmap().get("hra_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(6, (short) (3000));

            createContent2(wb, row, (short) 7, psm.getTotalmap().get("cca_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(7, (short) (3000));

            createContent2(wb, row, (short) 8, psm.getTotalmap().get("others_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(8, (short) (3000));

            createContent2(wb, row, (short) 9, psm.getTotalmap().get("gross_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(9, (short) (3000));

            createContent2(wb, row, (short) 10, psm.getTotalmap().get("it_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(10, (short) (3000));

            createContent2(wb, row, (short) 11, psm.getTotalmap().get("epf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(11, (short) (3000));

            createContent2(wb, row, (short) 12, psm.getTotalmap().get("vpf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(12, (short) (3000));

            createContent2(wb, row, (short) 13, psm.getTotalmap().get("proftax_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(13, (short) (3000));

            createContent2(wb, row, (short) 14, psm.getTotalmap().get("his_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(14, (short) (3000));

            createContent2(wb, row, (short) 15, psm.getTotalmap().get("fbf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(15, (short) (3000));
            j++;

            itr = psm.getLeavesurrenderlist().iterator();
            while (itr.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();

                String type = null;
                if (psedm1.getPeriod() != null) {
                    type = "SL" + psedm1.getPeriod();
                } else {
                    type = "SL";
                }

                createContent(wb, row, (short) 0, (String) type, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3000));

                createContent1(wb, row, (short) 1, psedm1.getBasic(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (3000));

                createContent1(wb, row, (short) 2, psedm1.getSpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (3000));

                createContent1(wb, row, (short) 3, psedm1.getPp(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (3000));

                createContent1(wb, row, (short) 4, psedm1.getDa(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3000));

                createContent1(wb, row, (short) 5, psedm1.getGrpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3000));

                createContent1(wb, row, (short) 6, psedm1.getHra(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, psedm1.getCca(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, psedm1.getOthers(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (3000));

                createContent1(wb, row, (short) 9, psedm1.getGross(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (3000));

                createContent1(wb, row, (short) 10, psedm1.getItax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (3000));

                createContent1(wb, row, (short) 11, psedm1.getEpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (3000));

                createContent1(wb, row, (short) 12, psedm1.getVpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (3000));

                createContent1(wb, row, (short) 13, psedm1.getProftax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(13, (short) (3000));

                createContent1(wb, row, (short) 14, psedm1.getHis(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(14, (short) (3000));

                createContent1(wb, row, (short) 15, psedm1.getFbf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(15, (short) (3000));

                j++;
            }

            itr = psm.getDaarrearlist().iterator();
            while (itr.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();

                String type = null;
                if (psedm1.getPeriod() != null) {
                    type = "DA" + psedm1.getPeriod();
                } else {
                    type = "DA";
                }

                createContent(wb, row, (short) 0, (String) type, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3000));

                createContent1(wb, row, (short) 1, psedm1.getBasic(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (3000));

                createContent1(wb, row, (short) 2, psedm1.getSpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (3000));

                createContent1(wb, row, (short) 3, psedm1.getPp(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (3000));

                createContent1(wb, row, (short) 4, psedm1.getDa(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3000));

                createContent1(wb, row, (short) 5, psedm1.getGrpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3000));

                createContent1(wb, row, (short) 6, psedm1.getHra(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, psedm1.getCca(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, psedm1.getOthers(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (3000));

                createContent1(wb, row, (short) 9, psedm1.getGross(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (3000));

                createContent1(wb, row, (short) 10, psedm1.getItax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (3000));

                createContent1(wb, row, (short) 11, psedm1.getEpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (3000));

                createContent1(wb, row, (short) 12, psedm1.getVpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (3000));

                createContent1(wb, row, (short) 13, psedm1.getProftax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(13, (short) (3000));

                createContent1(wb, row, (short) 14, psedm1.getHis(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(14, (short) (3000));

                createContent1(wb, row, (short) 15, psedm1.getFbf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(15, (short) (3000));

                j++;
            }

            itr = psm.getIncrementarrearlist().iterator();
            while (itr.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();

                String type = null;
                if (psedm1.getPeriod() != null) {
                    type = "IA" + psedm1.getPeriod();
                } else {
                    type = "IA";
                }

                createContent(wb, row, (short) 0, (String) type, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3000));

                createContent1(wb, row, (short) 1, psedm1.getBasic(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (3000));

                createContent1(wb, row, (short) 2, psedm1.getSpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (3000));

                createContent1(wb, row, (short) 3, psedm1.getPp(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (3000));

                createContent1(wb, row, (short) 4, psedm1.getDa(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3000));

                createContent1(wb, row, (short) 5, psedm1.getGrpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3000));

                createContent1(wb, row, (short) 6, psedm1.getHra(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, psedm1.getCca(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, psedm1.getOthers(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (3000));

                createContent1(wb, row, (short) 9, psedm1.getGross(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (3000));

                createContent1(wb, row, (short) 10, psedm1.getItax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (3000));

                createContent1(wb, row, (short) 11, psedm1.getEpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (3000));

                createContent1(wb, row, (short) 12, psedm1.getVpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (3000));

                createContent1(wb, row, (short) 13, psedm1.getProftax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(13, (short) (3000));

                createContent1(wb, row, (short) 14, psedm1.getHis(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(14, (short) (3000));

                createContent1(wb, row, (short) 15, psedm1.getFbf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(15, (short) (3000));

                j++;
            }

            itr = psm.getBonuslist().iterator();
            while (itr.hasNext()) {
                row = sheet.createRow(j);
                row.setHeight((short) 300);
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();

                String type = null;
                if (psedm1.getPeriod() != null) {
                    //type = "BON" + psedm1.getPeriod();
                    type = psedm1.getPaycodename() + psedm1.getPeriod();
                } else {
                    type = psedm1.getPaycodename();
                }

                createContent(wb, row, (short) 0, (String) type, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (3000));

                createContent1(wb, row, (short) 1, psedm1.getBasic(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(1, (short) (3000));

                createContent1(wb, row, (short) 2, psedm1.getSpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(2, (short) (3000));

                createContent1(wb, row, (short) 3, psedm1.getPp(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(3, (short) (3000));

                createContent1(wb, row, (short) 4, psedm1.getDa(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(4, (short) (3000));

                createContent1(wb, row, (short) 5, psedm1.getGrpay(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(5, (short) (3000));

                createContent1(wb, row, (short) 6, psedm1.getHra(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(6, (short) (3000));

                createContent1(wb, row, (short) 7, psedm1.getCca(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(7, (short) (3000));

                createContent1(wb, row, (short) 8, psedm1.getOthers(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(8, (short) (3000));

                createContent1(wb, row, (short) 9, psedm1.getGross(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(9, (short) (3000));

                createContent1(wb, row, (short) 10, psedm1.getItax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(10, (short) (3000));

                createContent1(wb, row, (short) 11, psedm1.getEpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(11, (short) (3000));

                createContent1(wb, row, (short) 12, psedm1.getVpf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(12, (short) (3000));

                createContent1(wb, row, (short) 13, psedm1.getProftax(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(13, (short) (3000));

                createContent1(wb, row, (short) 14, psedm1.getHis(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(14, (short) (3000));

                createContent1(wb, row, (short) 15, psedm1.getFbf(), CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(15, (short) (3000));

                j++;
            }

            row = sheet.createRow(j);
            row.setHeight((short) 300);

            createContent(wb, row, (short) 0, (String) "TOTAL", CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(0, (short) (3000));

            createContent2(wb, row, (short) 1, psm.getTotalmap().get("grant_basic_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(1, (short) (3000));

            createContent2(wb, row, (short) 2, psm.getTotalmap().get("grant_spay_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(2, (short) (3000));

            createContent2(wb, row, (short) 3, psm.getTotalmap().get("grant_pp_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(3, (short) (3000));

            createContent2(wb, row, (short) 4, psm.getTotalmap().get("grant_da_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(4, (short) (3000));

            createContent2(wb, row, (short) 5, psm.getTotalmap().get("grant_grpay_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (3000));

            createContent2(wb, row, (short) 6, psm.getTotalmap().get("grant_hra_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(6, (short) (3000));

            createContent2(wb, row, (short) 7, psm.getTotalmap().get("grant_cca_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(7, (short) (3000));

            createContent2(wb, row, (short) 8, psm.getTotalmap().get("grant_others_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(8, (short) (3000));

            createContent2(wb, row, (short) 9, psm.getTotalmap().get("grant_gross_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(9, (short) (3000));

            createContent2(wb, row, (short) 10, psm.getTotalmap().get("grant_it_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(10, (short) (3000));

            createContent2(wb, row, (short) 11, psm.getTotalmap().get("grant_epf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(11, (short) (3000));

            createContent2(wb, row, (short) 12, psm.getTotalmap().get("grant_vpf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(12, (short) (3000));

            createContent2(wb, row, (short) 13, psm.getTotalmap().get("grant_proftax_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(13, (short) (3000));

            createContent2(wb, row, (short) 14, psm.getTotalmap().get("grant_his_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(14, (short) (3000));

            createContent2(wb, row, (short) 15, psm.getTotalmap().get("grant_fbf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(15, (short) (3000));
//            j++;

            j = j + 2;

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
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);

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
        String cellVal = cellValue.trim();
        if (cellVal == null || cellVal.length() == 0) {
            cell.setCellValue(new Double("0.00"));
        } else {
            cell.setCellValue(new Double(cellVal));
        }
    }

    private void createContent2(Workbook workbook, Row row, short column, String cellValue, short cellalign) {
        Cell cell = row.createCell(column);
        String cellVal = cellValue.trim();
        if (cellVal == null || cellVal.length() == 0) {
            cell.setCellValue(new Double("0.00"));
        } else {
            cell.setCellValue(new Double(cellVal));
        }
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);

    }

    public boolean GenerateIncomeTaxAbstractExportXML(PaySlipModel psm, String filePath) {
        boolean result = false;
        boolean heading = false;

        try {
            File xlsxFile = new File(filePath);
            Workbook wb = null;
            Sheet sheet = null;
            if (xlsxFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(xlsxFile);
                wb = new HSSFWorkbook(fileInputStream);
                sheet = wb.getSheet("IncomeTaxAbstract");
                heading = false;
            } else {
                wb = new HSSFWorkbook();
                sheet = wb.createSheet("IncomeTaxAbstract");
                heading = true;
            }

            Row row = sheet.createRow(j);
            if (heading) {
                row.setHeight((short) 600);

                createHeader(wb, row, (short) 0, "EMPLOYEE NAME");
                sheet.setColumnWidth(0, (short) (3000));

                createHeader(wb, row, (short) 1, "EPF NO");
                sheet.setColumnWidth(1, (short) (3000));
                
                createHeader(wb, row, (short) 2, "DESIGNATION");
                sheet.setColumnWidth(2, (short) (3000));

                createHeader(wb, row, (short) 3, "PAY");
                sheet.setColumnWidth(3, (short) (3000));

                createHeader(wb, row, (short) 4, "S.PAY");
                sheet.setColumnWidth(4, (short) (3000));

                createHeader(wb, row, (short) 5, "DA");
                sheet.setColumnWidth(5, (short) (3000));

                createHeader(wb, row, (short) 6, "GRADE PAY");
                sheet.setColumnWidth(6, (short) (3000));

                createHeader(wb, row, (short) 7, "HRA");
                sheet.setColumnWidth(7, (short) (3000));

                createHeader(wb, row, (short) 8, "CCA");
                sheet.setColumnWidth(8, (short) (3000));

                createHeader(wb, row, (short) 9, "OTHERS");
                sheet.setColumnWidth(9, (short) (3000));

                createHeader(wb, row, (short) 10, "GROSS");
                sheet.setColumnWidth(10, (short) (3000));

                createHeader(wb, row, (short) 11, "I.TAX");
                sheet.setColumnWidth(11, (short) (3000));

                createHeader(wb, row, (short) 12, "EPF/GPF");
                sheet.setColumnWidth(12, (short) (3000));

                createHeader(wb, row, (short) 13, "VPF");
                sheet.setColumnWidth(13, (short) (3000));

                createHeader(wb, row, (short) 14, "PROF.TAX");
                sheet.setColumnWidth(14, (short) (3000));

                createHeader(wb, row, (short) 15, "HIS");
                sheet.setColumnWidth(15, (short) (3000));

                createHeader(wb, row, (short) 16, "FBF");
                sheet.setColumnWidth(16, (short) (3000));
                j++;
                row = sheet.createRow(j);
            }
            row.setHeight((short) 500);
            createContent(wb, row, (short) 0, psm.getEmployeename(), CellStyle.ALIGN_LEFT);
            sheet.setColumnWidth(0, (short) (3000));

            createContent(wb, row, (short) 1, psm.getPfno(), CellStyle.ALIGN_LEFT);
            sheet.setColumnWidth(1, (short) (3000));
            
            createContent(wb, row, (short) 2, psm.getDesignation(), CellStyle.ALIGN_LEFT);
            sheet.setColumnWidth(2, (short) (3000));

            createContent2(wb, row, (short) 3, psm.getTotalmap().get("basic_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(3, (short) (3000));

            createContent2(wb, row, (short) 4, psm.getTotalmap().get("spay_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(4, (short) (3000));

            createContent2(wb, row, (short) 5, psm.getTotalmap().get("da_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(5, (short) (3000));

            createContent2(wb, row, (short) 6, psm.getTotalmap().get("grpay_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(6, (short) (3000));

            createContent2(wb, row, (short) 7, psm.getTotalmap().get("hra_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(7, (short) (3000));

            createContent2(wb, row, (short) 8, psm.getTotalmap().get("cca_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(8, (short) (3000));

            createContent2(wb, row, (short) 9, psm.getTotalmap().get("others_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(9, (short) (3000));

            createContent2(wb, row, (short) 10, psm.getTotalmap().get("gross_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(10, (short) (3000));

            createContent2(wb, row, (short) 11, psm.getTotalmap().get("it_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(11, (short) (3000));

            createContent2(wb, row, (short) 12, psm.getTotalmap().get("epf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(12, (short) (3000));

            createContent2(wb, row, (short) 13, psm.getTotalmap().get("vpf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(13, (short) (3000));

            createContent2(wb, row, (short) 14, psm.getTotalmap().get("proftax_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(14, (short) (3000));

            createContent2(wb, row, (short) 15, psm.getTotalmap().get("his_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(15, (short) (3000));

            createContent2(wb, row, (short) 16, psm.getTotalmap().get("fbf_total"), CellStyle.ALIGN_RIGHT);
            sheet.setColumnWidth(16, (short) (3000));
            j++;

            FileOutputStream fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
            fileOut.close();
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
