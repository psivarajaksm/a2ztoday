/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.supplementary;

import com.onward.reports.regular.*;
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
import java.util.Set;
import org.jboss.management.j2ee.JVM;

/**
 *
 * @author Prince vijayakumar.M
 */
public class SupplementaryPayslipReport {

    private static final char[] FORM_FEED = {(char) 12};
    private static final char LARGEFONT = (char) 14;
    private static final char[] BOLD = {(char) 14, (char) 15};
    private static final char RELEASE = (char) 18;
    private String checksectionname = "";
    private int pagebreakno = 0;

    public String StringSplit(String strname) {
        char[] org = strname.toCharArray();
        int charlen = org.length;
        char[] chr = new char[80];
        for (int i = 0; i < 80; i++) {
            if (i < charlen) {
                chr[i] = org[i];
            } else {
                chr[i] = ' ';
            }
        }
        return new String(chr);
    }

    public String checkempty(String str) {
        return (str.length() > 1) ? ":" : " ";
    }

    public void getSupplementaryPrintWriter(PaySlipModel psm, String filePath) {
        System.out.println("********************* SupplementaryPayslipReport class getSupplementaryPrintWriter method is calling *****************");
        try {
            PaySlip_Earn_Deduction_Model psedm = new PaySlip_Earn_Deduction_Model();
            PaySlip_Earn_Deduction_Model psedm1 = new PaySlip_Earn_Deduction_Model();
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            char[] horizontalline = new char[132];
            for (int i = 0; i < 132; i++) {
                horizontalline[i] = '=';
            }
            String sectionname = psm.getSectionname();
            if (psm.getSlipno().equals("1")) {
                checksectionname = psm.getSectionname();
                pagebreakno = Integer.parseInt(psm.getSlipno()) + 2;
            }
            if (!checksectionname.equals(psm.getSectionname())) {
//                System.out.println("psm.getSlipno() -> " + psm.getSlipno());
                pw.write(FORM_FEED);
                pw.println();
                checksectionname = psm.getSectionname();
                pagebreakno = Integer.parseInt(psm.getSlipno()) + 2;

            }
            if (pagebreakno == Integer.parseInt(psm.getSlipno())) {
                pw.write(FORM_FEED);
                pw.println();
                pagebreakno += 2;
            }
            pw.print(horizontalline);
            pw.println();
            String verline = "|";

            pw.printf("%s%-49s%s%-19s%s%-19s%s%-35s%-4s%s", verline, "      TAMIL NADU CIVIL SUPPLIES CORPORATION", verline, "      EARNINGS", verline, "     DEDUCTIONS", verline, "    LOANS AND ADVANCES  | SLIP NO:  ", psm.getSlipno(), verline);
            pw.println();
            String district_details = "";
            if (psm.getPincode() == null || psm.getPincode().length() == 0) {
                district_details = psm.getDistrict();
            } else {
                district_details = psm.getDistrict() + " - " + psm.getPincode();
            }
            pw.printf("%s%-15s%-34s%s%-19s%s%-19s%s%-40s%s", verline, "", district_details, verline, "-------------------", verline, "-------------------", verline, "----------------------------------------", verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(0);
            if (psm.getBilltype().equals("LEAVESURRENDER")) {
                pw.printf("%s%49s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%-10s%s%-10s%s%-7s%s", verline, "             SURRENDER LEAVE SALARY              ", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "   LOAN", verline, " LOAN AMT", verline, " LOAN BAL", verline, " INST", verline);
            } else if (psm.getBilltype().equals("SUPLEMENTARYBILL")) {
                pw.printf("%s%26s%7s%4s%-11s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%-10s%s%-10s%s%-7s%s", verline, "LEAVE SALARY FROM ", psm.getPayslipstartingdate(), " TO ", psm.getPayslipenddate(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "   LOAN", verline, " LOAN AMT", verline, " LOAN BAL", verline, " INST", verline);

            }

//            pw.printf("%s%-29s%-3s%-3s%-13s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%-10s%s%-10s%s%-7s%s", verline, "             LEAVE SALARY FOR ", psm.getPayslipmonth(), " - ", psm.getPayslipyear(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "   LOAN", verline, " LOAN AMT", verline, " LOAN BAL", verline, " INST", verline);
//            pw.printf("%s%26s%7s%4s%-12s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%-10s%s%-10s%s%-7s%s", verline, "LEAVE SALARY FROM ", "MAR\"12", " TO ", "APR\"12", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "   LOAN", verline, " LOAN AMT", verline, " LOAN BAL", verline, " INST", verline);
//            pw.printf("%s%49s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%-10s%s%-10s%s%-7s%s", verline, "             SURRENDER LEAVE SALARY              ", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "   LOAN", verline, " LOAN AMT", verline, " LOAN BAL", verline, " INST", verline);

            pw.println();
            psedm = psm.getEarn_ded_map().get(1);
            pw.printf("%s%-37s%s%s%-6s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%-10s%s%-10s%s%-7s%s", verline, "    PAYSLIP (SUPPLEMENTARY BILL) FOR ", psm.getPayslipmonth(), " - ", psm.getPayslipyear(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "----------", verline, "----------", verline, "----------", verline, "-------", verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(2);
            psedm1 = psm.getLoanmap().get(0);
            pw.printf("%s%-49s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "-------------------------------------------------", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(3);
            psedm1 = psm.getLoanmap().get(1);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "PF NUM", ":", psm.getPfno(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(4);
            psedm1 = psm.getLoanmap().get(2);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "EMP.NAME", ":", psm.getEmployeename(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(5);
            psedm1 = psm.getLoanmap().get(3);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "DESIG", ":", (psm.getDesignation() == null) ? "" : psm.getDesignation(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(6);
            psedm1 = psm.getLoanmap().get(4);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "PAN.NO", ":", psm.getPancardno(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(7);
            psedm1 = psm.getLoanmap().get(5);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "BRANCH", ":", psm.getBranch(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(8);
            psedm1 = psm.getLoanmap().get(6);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "SEC NAME", ":", psm.getSectionname(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(9);
            psedm1 = psm.getLoanmap().get(7);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "PAY SCAL", ":", psm.getPayscale(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(10);
            psedm1 = psm.getLoanmap().get(8);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "DATE OF BIRTH", ":", psm.getDateofbirth(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(11);
            psedm1 = psm.getLoanmap().get(9);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "DATE OF APPT", ":", psm.getDateofappointment(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(12);
            psedm1 = psm.getLoanmap().get(10);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "BANK A/C NO", ":", psm.getBankaccountno(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(13);
            psedm1 = psm.getLoanmap().get(11);
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "LST EL SUR DT", ":", psm.getDateofelsurrenderdate(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(14);
            psedm1 = psm.getLoanmap().get(12);
            pw.printf("%s%-42s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "-------------------------------------------------", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(15);
            pw.printf("%s%15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "RATE ", ":", psm.getRate(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "----------------------------------------", verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(16);
            pw.printf("%s%-42s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%-2s%-28s%s", verline, "-------------------------------------------------", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "NET PAY", ":", psm.getNetsalary(), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(17);
            pw.printf("%s%-15s%-2s%-10s%s%21s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "BASIC", ":", psm.getBasicpay(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, StringSplit(psm.getNetsalarywords()).subSequence(0, 39), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(18);
            pw.printf("%s%-15s%-2s%-10s%s%21s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "D.A", ":", psm.getDa(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, StringSplit(psm.getNetsalarywords()).subSequence(39, 79), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(19);
            pw.printf("%s%-15s%-2s%-10s%s%21s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "GR.PAY", ":", psm.getGrpay(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "----------------------------------------", verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(20);
            pw.printf("%s%-15s%-2s%-10s%s%21s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "HRA", ":", psm.getHra(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "", verline);
            pw.println();
            pw.printf("%s%-15s%-2s%-10s%s%21s%s%-19s%s%-19s%s%-40s%s", verline, "C C A", ":", psm.getCca(), verline, "---------------------", verline, "-------------------", verline, "-------------------", verline, "", verline);
            pw.println();
            pw.printf("%s%-15s%-2s%-10s%s%4s%-8s%-2s%-7s%s%-9s%10s%s%-10s%9s%s%20s%-20s%s", verline, "", "", "", verline, "", "PAY DAY", ":", psm.getPayday(), verline, "TOTAL", psm.getTotalearnings(), verline, "TOTAL", psm.getTotaldeductions(), verline, psm.getSigningauthority(), "(Bills)", verline);
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
