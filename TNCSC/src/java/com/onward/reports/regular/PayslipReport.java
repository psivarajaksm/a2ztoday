/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.regular;

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
import java.util.Set;
import org.jboss.management.j2ee.JVM;

/**
 *
 * @author Prince vijayakumar.M
 */
public class PayslipReport {

    private static final char[] FORM_FEED = {(char) 12};
    private static final char LARGEFONT = (char) 14;
    private static final char[] BOLD = {(char) 14, (char) 15};
    private static final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] equalline = new char[80];
    private char[] horizontalline = new char[80];
    private String checksectionname = "";
    private int pagebreakno = 0;

    public PayslipReport() {
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

    public void PNCSlip(Map map, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.write(FORM_FEED);
            pw.println();

            pw.printf("%53s%-20s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, ", (String) map.get("region"));
            pw.println();

            pw.printf("%52s%-8s", "PAY NOT CLAIM LIST FOR THE MONTH OF ", (String) map.get("period"));
            pw.println();

            pw.print(equalline);
            pw.println();

            pw.printf("%s", "SNO  EPFNO         NAME                          DESIG      SECTION    REASON");
            pw.println();

            pw.print(equalline);
            pw.println();

            List pnclist = (List) map.get("pnclist");
            Iterator itr = pnclist.iterator();
            while (itr.hasNext()) {
                PaySlipModel psm = (PaySlipModel) itr.next();
                pw.printf("%3s%s%-12s%s%-30s%s%-10s%s%-10s%s%-10s", psm.getSlipno(), " ", psm.getEpf(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psm.getSectionname(), " ", psm.getRemarks());
                pw.println();
                pw.println();
            }
//            pw.printf("%3s%s%-12s%s%-30s%s%-10s%s%-10s%s%-10s", "XXX", " ", "XXXXXXXXXXXX", " ", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", " ", "XXXXXXXXXX", " ", "XXXXXXXXXX", " ", "XXXXXXXXXX");
//            pw.println();
//            pw.println();

            pw.print(horizontalline);
            pw.println();

            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void missingEarDedSlip(Map map, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.write(FORM_FEED);
            pw.println();

            pw.printf("%53s%-20s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, ", (String) map.get("region"));
            pw.println();

            pw.printf("%13s%19s", " ",(String) map.get("heading"));
            pw.println();

            pw.print(equalline);
            pw.println();

            pw.printf("%s", "SNO  EPFNO         NAME                          DESIG      SECTION    REASON");
            pw.println();

            pw.print(equalline);
            pw.println();

            List pnclist = (List) map.get("pnclist");
            Iterator itr = pnclist.iterator();
            while (itr.hasNext()) {
                PaySlipModel psm = (PaySlipModel) itr.next();
                pw.printf("%3s%s%-12s%s%-30s%s%-10s%s%-10s%s%-10s", psm.getSlipno(), " ", psm.getEpf(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psm.getSectionname(), " ", psm.getRemarks());
                pw.println();
                pw.println();
            }
            pw.print(horizontalline);
            pw.println();

            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getPayBillPrintWriter(PaySlipModel psm, String filePath) {
//        System.out.println("********************* PayslipReport class getPayBillPrintWriter method is calling *****************");
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
            String sectionname = psm.getSectionname();
            if (psm.getSlipno().equals("1")) {
                checksectionname = psm.getSectionname();
                pagebreakno = Integer.parseInt(psm.getSlipno()) + 2;
            }
            if (!checksectionname.equals(psm.getSectionname())) {
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

            char[] horizontalline = new char[132];
            for (int i = 0; i < 132; i++) {
                horizontalline[i] = '=';
            }
            pw.println();
            pw.print(horizontalline);
            pw.println();
            String verline = "|";
            pw.printf("%s%-49s%s%-19s%s%-19s%s%-35s%-4s%s", verline, "      TAMIL NADU CIVIL SUPPLIES CORPORATION", verline, "      EARNINGS", verline, "     DEDUCTIONS", verline, "    LOANS AND ADVANCES  | SLIP NO:  ", psm.getSlipno(), verline);
            pw.println();
            String district_details = psm.getDistrict() + " - " + psm.getPincode();
            pw.printf("%s%-49s%s%-19s%s%-19s%s%-40s%s", verline, "", verline, "-------------------", verline, "-------------------", verline, "----------------------------------------", verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(0);
            pw.printf("%s%-49s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%-10s%s%-10s%s%-7s%s", verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "   LOAN", verline, " LOAN AMT", verline, " LOAN BAL", verline, " INST", verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(1);
            pw.printf("%s%7s%25s%s%s%-11s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%-10s%s%-10s%s%-7s%s", verline, "", "PAYSLIP FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "----------", verline, "----------", verline, "----------", verline, "-------", verline);
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
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "DESIG", ":", psm.getDesignation(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
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
            pw.printf("%s%-7s%s%9s%s%-7s%s%6s%s%16s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "BASIC", ":", psm.getBasicpay(), verline, "E.L", ":", psm.getEl(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, StringSplit(psm.getNetsalarywords()).subSequence(0, 39), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(18);
            pw.printf("%s%-7s%s%9s%s%-7s%s%6s%s%16s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "D.A", ":", psm.getDa(), verline, "U.E.L", ":", psm.getUel(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, StringSplit(psm.getNetsalarywords()).subSequence(39, 79), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(19);
            pw.printf("%s%-7s%s%9s%s%-7s%s%6s%s%16s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "GR.PAY", ":", psm.getGrpay(), verline, "M.L", ":", psm.getMl(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "----------------------------------------", verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(20);
            pw.printf("%s%-7s%s%9s%s%-7s%s%6s%s%16s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "HRA", ":", psm.getHra(), verline, "L.L.P", ":", psm.getLlp(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "", verline);
            pw.println();
            pw.printf("%s%-7s%s%9s%s%-7s%s%6s%s%16s%s%-19s%s%-19s%s%-40s%s", verline, "C C A", ":", psm.getCca(), verline, "C.L", ":", psm.getCl(), verline, "----------------", verline, "-------------------", verline, "-------------------", verline, "", verline);
            pw.println();
            pw.printf("%s%-17s%s%-14s%s%-7s%s%-8s%s%-9s%10s%s%-10s%9s%s%20s%-20s%s", verline, "", verline, "", verline, "PAY DAY", ":", psm.getPayday(), verline, "TOTAL", psm.getTotalearnings(), verline, "TOTAL", psm.getTotaldeductions(), verline, psm.getSigningauthority(), "(Bills)", verline);
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.printf("%53s%-3s%5s%9s%3s%5s", "(*) LEAVE & ATTENDANCE particulars belongs to 16th ", psm.getPayslipmonthprevious(), psm.getPayslipyearprevious(), "to 15th ", psm.getPayslipmonth(), psm.getPayslipyear());
            pw.println();
            pw.println();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
