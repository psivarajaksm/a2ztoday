/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.common;

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
 * @author Prince vijayakumar M
 */
public class PayBillPrinter {

    private static final char[] FORM_FEED = {(char) 12};
    private static final char LARGEFONT = (char) 14;
    private static final char[] BOLD = {(char) 14, (char) 15};
    private static final char RELEASE = (char) 18;
    private String checksectionname = "";
    private int pagebreakno = 0;
    private double earningstotal = 0;
    private double deductiontotal = 0;
    private double netsalary = 0;
    public static double pagetotalearnings = 0;
    public static double pagetotaldeduction = 0;
    public static double pagenetttotal = 0;
    public static double sectiontotalearnings = 0;
    public static double sectiontotaldeduction = 0;
    public static double sectionnetttotal = 0;
    public static double grantearningstotal = 0;
    public static double grantdeductiontotal = 0;
    public static double grantnetsalary = 0;
//    private boolean flag = false;
    public static int pageno = 0;
    private static int lineno = 1;
    private static int sno = 1;
    private double sec_basicpay = 0;
    private double sec_splpay = 0;
    private double sec_da = 0;
    private double sec_hra = 0;
    private double sec_cca = 0;
    private double sec_earnings = 0;
    private double sec_grosssalry = 0;
    private double pagewise_basicpay = 0;
    private double pagewise_splpay = 0;
    private double pagewise_da = 0;
    private double pagewise_hra = 0;
    private double pagewise_cca = 0;
    private double pagewise_earnings = 0;
    private double pagewise_grosssalry = 0;
    private double grant_basicpay = 0;
    private double grant_splpay = 0;
    private double grant_da = 0;
    private double grant_hra = 0;
    private double grant_cca = 0;
    private double grant_earnings = 0;
    private double grant_grosssalry = 0;
    public static double pagewise_epf = 0;
    public static double pagewise_vpf = 0;
    public static double pagewise_spf = 0;
    public static double pagewise_deductions = 0;
    public static double pagewise_totalrecovery = 0;
    public static double pagewise_cash = 0;
    public static double pagewise_cheque = 0;
    public static double section_epf = 0;
    public static double section_vpf = 0;
    public static double section_spf = 0;
    public static double section_deductions = 0;
    public static double section_totalrecovery = 0;
    public static double section_cash = 0;
    public static double section_cheque = 0;
    public static double grand_epf = 0;
    public static double grand_vpf = 0;
    public static double grand_spf = 0;
    public static double grand_deductions = 0;
    public static double grand_totalrecovery = 0;
    public static double grand_cash = 0;
    public static double grand_cheque = 0;
    public static double pagesalary = 0;
    public static double pageepf = 0;
    public static double pageloan = 0;
    public static double pagevpf = 0;
    public static double pageemptotal = 0;
    public static double pageper833 = 0;
    public static double pageper367 = 0;
    public static double pageremarkstotal = 0;
    public static double grsalary = 0;
    public static double grepf = 0;
    public static double grloan = 0;
    public static double grvpf = 0;
    public static double gremptotal = 0;
    public static double grper833 = 0;
    public static double grper367 = 0;
    public static double grremarkstotal = 0;
    public int checkspace = 0;
    public final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    public static int pgno = 0;
    public static String secname = "";
    public static double grlicscheduletotal = 0;
    public static double hbaL07 = 0;
    public static double hbaL08 = 0;
    public static double hbaL25 = 0;
    public static double hbaL36 = 0;
    public static double hbaL37 = 0;
    public static double hbaL38 = 0;
    public static double hbatotal = 0;
    public static double gr_hbaL07 = 0;
    public static double gr_hbaL08 = 0;
    public static double gr_hbaL25 = 0;
    public static double gr_hbaL36 = 0;
    public static double gr_hbaL37 = 0;
    public static double gr_hbaL38 = 0;
    public static double gr_hbatotal = 0;
    public static double vehL09 = 0;
    public static double vehL10 = 0;
    public static double vehL11 = 0;
    public static double vehL32 = 0;
    public static double vehL21 = 0;
    public static double vehL22 = 0;
    public static double vehtotal = 0;
    public static double gr_vehL09 = 0;
    public static double gr_vehL10 = 0;
    public static double gr_vehL11 = 0;
    public static double gr_vehL32 = 0;
    public static double gr_vehL21 = 0;
    public static double gr_vehL22 = 0;
    public static double gr_vehtotal = 0;

    public static String StringSplit(String strname) {
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

    public static String checkempty(String str) {
        return (str.length() > 1) ? ":" : " ";
    }

    public void getPayBillPrintWriter(PaySlipModel psm, String filePath) {
        //System.out.println("********************* PayBillPrinter class getPayBillPrintWriter method is calling *****************");
        try {
            PaySlip_Earn_Deduction_Model psedm = new PaySlip_Earn_Deduction_Model();
            PaySlip_Earn_Deduction_Model psedm1 = new PaySlip_Earn_Deduction_Model();
            PrintWriter pw = null;
            File file = new File(filePath + "/PayBillSlip.txt");
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
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "EMP.NO", ":", psm.getEmpno(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
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
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "PF NUM", ":", psm.getPfno(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
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
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "DATE OF CONF", ":", psm.getDateofconformation(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
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
//            pw.printf("%s%-7s%-2s%-10s%s%21s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "BASIC", ":", psm.getBasicpay(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, StringSplit(psm.getNetsalarywords()).subSequence(0, 39), verline);
            pw.printf("%s%-7s%s%9s%s%-7s%s%6s%s%16s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "BASIC", ":", psm.getBasicpay(), verline, "E.L", ":", psm.getEl(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, StringSplit(psm.getNetsalarywords()).subSequence(0, 39), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(18);
//            pw.printf("%s%-15s%-2s%-10s%s%21s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "D.A", ":", psm.getDa(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, StringSplit(psm.getNetsalarywords()).subSequence(39, 79), verline);
            pw.printf("%s%-7s%s%9s%s%-7s%s%6s%s%16s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "D.A", ":", psm.getDa(), verline, "U.E.L", ":", psm.getUel(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, StringSplit(psm.getNetsalarywords()).subSequence(39, 79), verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(19);
//            pw.printf("%s%-15s%-2s%-10s%s%21s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "GR.PAY", ":", psm.getGrpay(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "----------------------------------------", verline);
            pw.printf("%s%-7s%s%9s%s%-7s%s%6s%s%16s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "GR.PAY", ":", psm.getGrpay(), verline, "M.L", ":", psm.getMl(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "----------------------------------------", verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(20);
//            pw.printf("%s%-15s%-2s%-10s%s%21s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "HRA", ":", psm.getHra(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "", verline);
            pw.printf("%s%-7s%s%9s%s%-7s%s%6s%s%16s%s%-9s%s%9s%s%-10s%s%8s%s%-40s%s", verline, "HRA", ":", psm.getHra(), verline, "L.L.P", ":", psm.getLlp(), verline, "", verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "", verline);
            pw.println();
//            pw.printf("%s%-15s%-2s%-10s%s%21s%s%-19s%s%-19s%s%-40s%s", verline, "C C A", ":", psm.getCca(), verline, "---------------------", verline, "-------------------", verline, "-------------------", verline, "", verline);
            pw.printf("%s%-7s%s%9s%s%-7s%s%6s%s%16s%s%-19s%s%-19s%s%-40s%s", verline, "C C A", ":", psm.getCca(), verline, "C.L", ":", psm.getCl(), verline, "----------------", verline, "-------------------", verline, "-------------------", verline, "", verline);
            pw.println();
//            pw.printf("%s%-15s%-2s%-10s%s%4s%-8s%-2s%-7s%s%-9s%10s%s%-10s%9s%s%-40s%s", verline, "", "", "", verline, "", "PAY DAY", ":", psm.getPayday(), verline, "TOTAL", psm.getTotalearnings(), verline, "TOTAL", psm.getTotaldeductions(), verline, "              Supdt.(Bills)", verline);
            pw.printf("%s%-17s%s%-14s%s%-7s%s%-8s%s%-9s%10s%s%-10s%9s%s%-40s%s", verline, "", verline, "", verline, "PAY DAY", ":", psm.getPayday(), verline, "TOTAL", psm.getTotalearnings(), verline, "TOTAL", psm.getTotaldeductions(), verline, "              Supdt.(Bills)", verline);
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.printf("%53s%-3s%5s%9s%3s%5s", "(*) LEAVE & ATTENDANCE particulars belongs to 16st ", psm.getPayslipmonth(), psm.getPayslipyear(), "to 15th ", psm.getPayslipmonth(), psm.getPayslipyear());
            pw.println();
            pw.println();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getSupplementaryPrintWriter(PaySlipModel psm, String filePath) {
        System.out.println("********************* PayBillPrinter class getSupplementaryPrintWriter method is calling *****************");
        try {
            PaySlip_Earn_Deduction_Model psedm = new PaySlip_Earn_Deduction_Model();
            PaySlip_Earn_Deduction_Model psedm1 = new PaySlip_Earn_Deduction_Model();
            PrintWriter pw = null;
            File file = new File(filePath + "/SupplementarySlip.txt");
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
            String district_details = psm.getDistrict() + " - " + psm.getPincode();
            pw.printf("%s%-15s%-34s%s%-19s%s%-19s%s%-40s%s", verline, "", district_details, verline, "-------------------", verline, "-------------------", verline, "----------------------------------------", verline);
            pw.println();
            psedm = psm.getEarn_ded_map().get(0);
            pw.printf("%s%-29s%-3s%-3s%-13s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%-10s%s%-10s%s%-7s%s", verline, "             LEAVE SALARY FOR ", psm.getPayslipmonth(), " - ", psm.getPayslipyear(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, "   LOAN", verline, " LOAN AMT", verline, " LOAN BAL", verline, " INST", verline);
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
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "EMP.NO", ":", psm.getEmpno(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
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
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "PF NUM", ":", psm.getPfno(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
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
            pw.printf("%s%-15s%-2s%-32s%s%-9s%s%9s%s%-10s%s%8s%s%-10s%s%10s%s%10s%s%7s%s", verline, "DATE OF CONF", ":", psm.getDateofconformation(), verline, psedm.getEarningsname(), checkempty(psedm.getEarningsname()), psedm.getEarningsamount(), verline, psedm.getDeductionname(), checkempty(psedm.getDeductionname()), psedm.getDeductionamount(), verline, psedm1.getLoanname(), verline, psedm1.getLoanamount(), verline, psedm1.getLoanbalance(), verline, psedm1.getInstallment(), verline);
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
            pw.printf("%s%-15s%-2s%-10s%s%4s%-8s%-2s%-7s%s%-9s%10s%s%-10s%9s%s%-40s%s", verline, "", "", "", verline, "", "PAY DAY", ":", psm.getPayday(), verline, "TOTAL", psm.getTotalearnings(), verline, "TOTAL", psm.getTotaldeductions(), verline, "              Supdt.(Bills)", verline);
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void headerAcquitanceSlip(PrintWriter pw, PaySlipModel psm) {
        char[] horizontalsign = new char[80];
        char[] horizontalline = new char[80];
        char[] equalline = new char[80];
        for (int i = 0; i < 80; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 80; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 80; i++) {
            equalline[i] = '=';
        }
        pw.println();
        pw.printf("%57s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, CHENNAI");
        pw.println();
        pw.printf("%47s%3s%3s%4s", "PAY ACQUITANCE SLIP (" + psm.getPaymenttype() + ") FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.println();
        pw.printf("%-9s%-46s%-10s%-15s", "REGION : ", "HEAD OFFICE", "SECTION : ", psm.getSectionname());
//                pw.printf("%-9s%-43s%7s%3s%-10s%6s", "REGION : ", "HEAD OFFICE", "SECTION", " : ", psm.getSectionname(), psm.getSectionname());
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%4s%12s%24s%13s%12s%13s", "SNO", "EMP.NO", "EMPLOYEE NAME", "EARNINGS", "RECOVERY", "NET");
        pw.println();
        pw.print(horizontalsign);
    }

    public static void pagetotalAcquitanceSlip(PrintWriter pw) {
        char[] horizontalsign = new char[80];
        char[] horizontalline = new char[80];
        char[] equalline = new char[80];
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        for (int i = 0; i < 80; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 80; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 80; i++) {
            equalline[i] = '=';
        }
        pw.println();
        pw.printf("%-12s%41s%12s%13s", "Page Total :", decimalFormat.format(pagetotalearnings), decimalFormat.format(pagetotaldeduction), decimalFormat.format(pagenetttotal));
        pagetotalearnings = 0;
        pagetotaldeduction = 0;
        pagenetttotal = 0;
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.println();
        pw.printf("%33s%10s%4s%33s", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "Page No. :", pageno, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        pageno++;
        pw.println();
        pw.write(FORM_FEED);
    }

    public static void sectionwiseAcquitanceSlip(PrintWriter pw) {
        char[] horizontalsign = new char[80];
        char[] horizontalline = new char[80];
        char[] equalline = new char[80];
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        for (int i = 0; i < 80; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 80; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 80; i++) {
            equalline[i] = '=';
        }
        pw.println();
        pw.printf("%-12s%41s%12s%13s", "Page Total :", decimalFormat.format(pagetotalearnings), decimalFormat.format(pagetotaldeduction), decimalFormat.format(pagenetttotal));
        pagetotalearnings = 0;
        pagetotaldeduction = 0;
        pagenetttotal = 0;
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%-15s%38s%12s%13s", "Section Total :", decimalFormat.format(sectiontotalearnings), decimalFormat.format(sectiontotaldeduction), decimalFormat.format(sectionnetttotal));
        sectiontotalearnings = 0;
        sectiontotaldeduction = 0;
        sectionnetttotal = 0;
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.println();
        pw.printf("%33s%10s%4s%33s", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "Page No. :", pageno, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        pageno++;
        pw.println();
        pw.write(FORM_FEED);
    }

    public static void grantAcquitanceSlip(PrintWriter pw) {
        char[] horizontalsign = new char[80];
        char[] horizontalline = new char[80];
        char[] equalline = new char[80];
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        for (int i = 0; i < 80; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 80; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 80; i++) {
            equalline[i] = '=';
        }
        pw.println();
        pw.printf("%-12s%41s%12s%13s", "Page Total :", decimalFormat.format(pagetotalearnings), decimalFormat.format(pagetotaldeduction), decimalFormat.format(pagenetttotal));
        pagetotalearnings = 0;
        pagetotaldeduction = 0;
        pagenetttotal = 0;
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%-15s%38s%12s%13s", "Section Total :", decimalFormat.format(sectiontotalearnings), decimalFormat.format(sectiontotaldeduction), decimalFormat.format(sectionnetttotal));
        sectiontotalearnings = 0;
        sectiontotaldeduction = 0;
        sectionnetttotal = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%-15s%38s%12s%13s", "Grand Total :", decimalFormat.format(grantearningstotal), decimalFormat.format(grantdeductiontotal), decimalFormat.format(grantnetsalary));
        grantearningstotal = 0;
        grantdeductiontotal = 0;
        grantnetsalary = 0;
        pw.println();
        pw.print(equalline);
    }
    
    public void getAcquitanceSlipPrintWriter(PaySlipModel psm, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/AcquitanceSlip.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            char[] horizontalsign = new char[80];
            char[] horizontalline = new char[80];
            char[] equalline = new char[80];
            for (int i = 0; i < 80; i++) {
                horizontalsign[i] = '~';
            }
            for (int i = 0; i < 80; i++) {
                horizontalline[i] = '-';
            }
            for (int i = 0; i < 80; i++) {
                equalline[i] = '=';
            }
            String sectionname = psm.getSectionname();
            if (psm.getSlipno().equals("1")) {
                pageno = 1;
                checksectionname = psm.getSectionname();
                headerAcquitanceSlip(pw, psm);
            }
            if (!checksectionname.equals(psm.getSectionname())) {
                checksectionname = psm.getSectionname();
                sectionwiseAcquitanceSlip(pw);
                headerAcquitanceSlip(pw, psm);
            }
            pw.println();
            pw.printf("%4s%12s%24s%13s%12s%13s", psm.getSlipno(), psm.getEmpno(), psm.getEmployeename(), psm.getTotalearnings(), psm.getTotaldeductions(), psm.getNetsalary());
            pw.println();
            pw.printf("%40s", psm.getDesignation());
            pw.println();
            pw.printf("%40s", psm.getBankaccountno());
            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.print(horizontalline);
            pw.println();

            
            pagetotalearnings += Double.valueOf(psm.getTotalearnings());
            pagetotaldeduction += Double.valueOf(psm.getTotaldeductions());
            pagenetttotal += (pagetotalearnings-pagetotaldeduction);
            System.out.println("");

            sectiontotalearnings += Double.valueOf(psm.getTotalearnings());
            sectiontotaldeduction += Double.valueOf(psm.getTotaldeductions());
            sectionnetttotal += (sectiontotalearnings-sectiontotaldeduction);

            grantearningstotal += Double.valueOf(psm.getTotalearnings());
            grantdeductiontotal += Double.valueOf(psm.getTotaldeductions());
            grantnetsalary += (grantearningstotal-grantdeductiontotal);            

            if(Integer.valueOf(psm.getSlipno())%6==0){
                pagetotalAcquitanceSlip(pw);
                headerAcquitanceSlip(pw, psm);
            }
            if (Integer.valueOf(psm.getSlipno()) == psm.getPrintingrecordsize()) {
                grantAcquitanceSlip(pw);
            }
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public void getEarningsLedgerPrintWriter(PaySlipModel psm, String filePath) {
//        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
//        try {
//            PrintWriter pw = null;
//            File file = new File(filePath + "/EarningsLedger.txt");
//            try {
//                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//
//            char[] horizontalsign = new char[132];
//            char[] horizontalline = new char[132];
//            char[] equalline = new char[132];
//            for (int i = 0; i < 132; i++) {
//                horizontalsign[i] = '~';
//            }
//            for (int i = 0; i < 132; i++) {
//                horizontalline[i] = '-';
//            }
//            for (int i = 0; i < 132; i++) {
//                equalline[i] = '=';
//            }
//            if (Integer.valueOf(psm.getSlipno()) == 1) {
//                lineno = 1;
//            }
//            String sectionname = psm.getSectionname();
//            if (lineno == 1) {
//                checksectionname = psm.getSectionname();
//                pageno = 1;
//                pw.println();
//                pw.write(LARGEFONT);
//                pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
//                pw.println();
//                pw.write(BOLD);
//                pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
//                pw.write(RELEASE);
//                pw.println();
//                pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
//                pw.println();
//                pw.print(horizontalsign);
//                pw.println();
//                pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
//                pw.println();
//                pw.print(horizontalsign);
//                pw.println();
//            }
//            if (!checksectionname.equals(psm.getSectionname())) {
//                pw.print(horizontalline);
//                pw.println();
//                pw.printf("%16s%3s%15s%18s%11s%12s%11s%11s%21s%13s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
//                pageno++;
//                pagewise_basicpay = 0;
//                pagewise_splpay = 0;
//                pagewise_da = 0;
//                pagewise_hra = 0;
//                pagewise_cca = 0;
//                pagewise_earnings = 0;
//                pagewise_grosssalry = 0;
//                pw.println();
//                pw.printf("%34s%18s%11s%12s%11s%11s%21s%13s", "SECTION TOTAL", decimalFormat.format(sec_basicpay), decimalFormat.format(sec_splpay), decimalFormat.format(sec_da), decimalFormat.format(sec_hra), decimalFormat.format(sec_cca), decimalFormat.format(sec_earnings), decimalFormat.format(sec_grosssalry));
//                sec_basicpay = 0;
//                sec_splpay = 0;
//                sec_da = 0;
//                sec_hra = 0;
//                sec_cca = 0;
//                sec_earnings = 0;
//                sec_grosssalry = 0;
//                pw.println();
//                pw.print(equalline);
//                pw.println();
//                pw.write(FORM_FEED);
//                checksectionname = psm.getSectionname();
//                pw.println();
//                pw.write(LARGEFONT);
//                pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
//                pw.println();
//                pw.write(BOLD);
//                pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
//                pw.write(RELEASE);
//                pw.println();
//                pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
//                pw.println();
//                pw.print(horizontalsign);
//                pw.println();
//                pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
//                pw.println();
//                pw.print(horizontalsign);
//                pw.println();
//                lineno = 1;
//            }
//
//            model = psm.getOtherallowance().get(0);
//            if (lineno == 55) {
//                pw.print(horizontalline);
//                pw.println();
//                pw.printf("%16s%3s%15s%18s%11s%12s%11s%11s%21s%13s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
//                pageno++;
//                pagewise_basicpay = 0;
//                pagewise_splpay = 0;
//                pagewise_da = 0;
//                pagewise_hra = 0;
//                pagewise_cca = 0;
//                pagewise_earnings = 0;
//                pagewise_grosssalry = 0;
//                pw.println();
//                pw.print(equalline);
//                pw.println();
//                pw.write(FORM_FEED);
//                pw.println();
//                pw.write(LARGEFONT);
//                pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
//                pw.println();
//                pw.write(BOLD);
//                pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
//                pw.write(RELEASE);
//                pw.println();
//                pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
//                pw.println();
//                pw.print(horizontalsign);
//                pw.println();
//                pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
//                pw.println();
//                pw.print(horizontalsign);
//                pw.println();
//                lineno = 1;
//            }
//            pw.printf("%3s%12s%25s%12s%11s%12s%11s%11s%11s%10s%13s", psm.getSlipno(), psm.getEmpno(), psm.getEmployeename(), psm.getBasicpay(), psm.getSplpay(), psm.getDa(), psm.getHra(), psm.getCca(), model.getEarningsname(), model.getEarningsamount(), psm.getGrosssalary());
//            if (psm.getBasicpay() == null || psm.getBasicpay().equals("")) {
//                sec_basicpay += 0;
//                pagewise_basicpay += 0;
//                grant_basicpay += 0;
//            } else {
//                sec_basicpay += Double.valueOf(psm.getBasicpay());
//                pagewise_basicpay += Double.valueOf(psm.getBasicpay());
//                grant_basicpay += Double.valueOf(psm.getBasicpay());
//            }
//
//            if (psm.getSplpay() == null || psm.getSplpay().equals("")) {
//                sec_splpay += 0;
//                pagewise_splpay += 0;
//                grant_splpay += 0;
//            } else {
//                sec_splpay += Double.valueOf(psm.getSplpay());
//                pagewise_splpay += Double.valueOf(psm.getSplpay());
//                grant_splpay += Double.valueOf(psm.getSplpay());
//            }
//
//            if (psm.getDa() == null || psm.getDa().equals("")) {
//                sec_da += 0;
//                pagewise_da += 0;
//                grant_da += 0;
//            } else {
//                sec_da += Double.valueOf(psm.getDa());
//                pagewise_da += Double.valueOf(psm.getDa());
//                grant_da += Double.valueOf(psm.getDa());
//            }
//
//            if (psm.getHra() == null || psm.getHra().equals("")) {
//                sec_hra += 0;
//                pagewise_hra += 0;
//                grant_hra += 0;
//            } else {
//                sec_hra += Double.valueOf(psm.getHra());
//                pagewise_hra += Double.valueOf(psm.getHra());
//                grant_hra += Double.valueOf(psm.getHra());
//            }
//
//            if (psm.getCca() == null || psm.getCca().equals("")) {
//                sec_cca += 0;
//                pagewise_cca += 0;
//                grant_cca += 0;
//            } else {
//                sec_cca += Double.valueOf(psm.getCca());
//                pagewise_cca += Double.valueOf(psm.getCca());
//                grant_cca += Double.valueOf(psm.getCca());
//            }
//
//            if (model.getEarningsamount() == null || model.getEarningsamount().equals("")) {
//                sec_earnings += 0;
//                pagewise_earnings += 0;
//                grant_earnings += 0;
//            } else {
//                sec_earnings += Double.valueOf(model.getEarningsamount());
//                pagewise_earnings += Double.valueOf(model.getEarningsamount());
//                grant_earnings += Double.valueOf(model.getEarningsamount());
//            }
//
//            if (psm.getGrosssalary() == null || psm.getGrosssalary().equals("")) {
//                sec_grosssalry += 0;
//                pagewise_grosssalry += 0;
//                grant_grosssalry += 0;
//            } else {
//                sec_grosssalry += Double.valueOf(psm.getGrosssalary());
//                pagewise_grosssalry += Double.valueOf(psm.getGrosssalary());
//                grant_grosssalry += Double.valueOf(psm.getGrosssalary());
//            }
//            sno++;
//            lineno++;
//            pw.println();
//
//            model = psm.getOtherallowance().get(1);
//            if (lineno == 55) {
//                pw.print(horizontalline);
//                pw.println();
//                pw.printf("%16s%3s%15s%18s%11s%12s%11s%11s%21s%13s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
//                pageno++;
//                pagewise_basicpay = 0;
//                pagewise_splpay = 0;
//                pagewise_da = 0;
//                pagewise_hra = 0;
//                pagewise_cca = 0;
//                pagewise_earnings = 0;
//                pagewise_grosssalry = 0;
//                pw.println();
//                pw.print(equalline);
//                pw.println();
//                pw.write(FORM_FEED);
//                pw.println();
//                pw.write(LARGEFONT);
//                pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
//                pw.println();
//                pw.write(BOLD);
//                pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
//                pw.write(RELEASE);
//                pw.println();
//                pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
//                pw.println();
//                pw.print(horizontalsign);
//                pw.println();
//                pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
//                pw.println();
//                pw.print(horizontalsign);
//                pw.println();
//                lineno = 1;
//            }
//            pw.printf("%15s%25s%57s%11s%10s", psm.getPfno(), psm.getDesignation(), "", model.getEarningsname(), model.getEarningsamount());
//            if (model.getEarningsamount() == null || model.getEarningsamount().equals("")) {
//                sec_earnings += 0;
//                pagewise_earnings += 0;
//                grant_earnings += 0;
//            } else {
//                sec_earnings += Double.valueOf(model.getEarningsamount());
//                pagewise_earnings += Double.valueOf(model.getEarningsamount());
//                grant_earnings += Double.valueOf(model.getEarningsamount());
//            }
//            lineno++;
//            pw.println();
//
//            model = psm.getOtherallowance().get(2);
//            if (lineno == 55) {
//                pw.print(horizontalline);
//                pw.println();
//                pw.printf("%16s%3s%15s%18s%11s%12s%11s%11s%21s%13s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
//                pageno++;
//                pagewise_basicpay = 0;
//                pagewise_splpay = 0;
//                pagewise_da = 0;
//                pagewise_hra = 0;
//                pagewise_cca = 0;
//                pagewise_earnings = 0;
//                pagewise_grosssalry = 0;
//                pw.println();
//                pw.print(equalline);
//                pw.println();
//                pw.write(FORM_FEED);
//                pw.println();
//                pw.write(LARGEFONT);
//                pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
//                pw.println();
//                pw.write(BOLD);
//                pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
//                pw.write(RELEASE);
//                pw.println();
//                pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
//                pw.println();
//                pw.print(horizontalsign);
//                pw.println();
//                pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
//                pw.println();
//                pw.print(horizontalsign);
//                pw.println();
//                lineno = 1;
//            }
//            pw.printf("%15s%82s%11s%10s", psm.getBankaccountno(), "", model.getEarningsname(), model.getEarningsamount());
//            if (model.getEarningsamount() == null || model.getEarningsamount().equals("")) {
//                sec_earnings += 0;
//                pagewise_earnings += 0;
//                grant_earnings += 0;
//            } else {
//                sec_earnings += Double.valueOf(model.getEarningsamount());
//                pagewise_earnings += Double.valueOf(model.getEarningsamount());
//                grant_earnings += Double.valueOf(model.getEarningsamount());
//            }
//            lineno++;
//            pw.println();
//            if (psm.getOtherallowance().size() == 3) {
//                pw.println();
//                lineno++;
//
//                pw.println();
//                lineno++;
//
//                pw.println();
//                lineno++;
//            }
//
//            for (int i = 3; i < psm.getOtherallowance().size(); i++) {
//                model = psm.getOtherallowance().get(i);
//                if (lineno == 55) {
//                    pw.print(horizontalline);
//                    pw.println();
//                    pw.printf("%16s%3s%15s%18s%11s%12s%11s%11s%21s%13s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
//                    pageno++;
//                    pagewise_basicpay = 0;
//                    pagewise_splpay = 0;
//                    pagewise_da = 0;
//                    pagewise_hra = 0;
//                    pagewise_cca = 0;
//                    pagewise_earnings = 0;
//                    pagewise_grosssalry = 0;
//                    pw.println();
//                    pw.print(equalline);
//                    pw.println();
//                    pw.write(FORM_FEED);
//                    pw.println();
//                    pw.write(LARGEFONT);
//                    pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
//                    pw.println();
//                    pw.write(BOLD);
//                    pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
//                    pw.write(RELEASE);
//                    pw.println();
//                    pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
//                    pw.println();
//                    pw.print(horizontalsign);
//                    pw.println();
//                    pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
//                    pw.println();
//                    pw.print(horizontalsign);
//                    pw.println();
//                    lineno = 1;
//                }
//                pw.printf("%97s%11s%10s", "", model.getEarningsname(), model.getEarningsamount());
//                if (model.getEarningsamount() == null || model.getEarningsamount().equals("")) {
//                    sec_earnings += 0;
//                    pagewise_earnings += 0;
//                    grant_earnings += 0;
//                } else {
//                    sec_earnings += Double.valueOf(model.getEarningsamount());
//                    pagewise_earnings += Double.valueOf(model.getEarningsamount());
//                    grant_earnings += Double.valueOf(model.getEarningsamount());
//                }
//                lineno++;
//                pw.println();
//            }
//            if (Integer.valueOf(psm.getSlipno()) == psm.getPrintingrecordsize()) {
//                pw.print(horizontalline);
//                pw.println();
//                pw.printf("%16s%3s%15s%18s%11s%12s%11s%11s%21s%13s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
//                pageno++;
//                pagewise_basicpay = 0;
//                pagewise_splpay = 0;
//                pagewise_da = 0;
//                pagewise_hra = 0;
//                pagewise_cca = 0;
//                pagewise_earnings = 0;
//                pagewise_grosssalry = 0;
//                pw.println();
//                pw.printf("%34s%18s%11s%12s%11s%11s%21s%13s", "SECTION TOTAL", decimalFormat.format(sec_basicpay), decimalFormat.format(sec_splpay), decimalFormat.format(sec_da), decimalFormat.format(sec_hra), decimalFormat.format(sec_cca), decimalFormat.format(sec_earnings), decimalFormat.format(sec_grosssalry));
//                sec_basicpay = 0;
//                sec_splpay = 0;
//                sec_da = 0;
//                sec_hra = 0;
//                sec_cca = 0;
//                sec_earnings = 0;
//                sec_grosssalry = 0;
//                pw.println();
//                pw.print(equalline);
//                pw.println();
//                pw.printf("%34s%18s%11s%12s%11s%11s%21s%13s", "GRAND TOTAL", decimalFormat.format(grant_basicpay), decimalFormat.format(grant_splpay), decimalFormat.format(grant_da), decimalFormat.format(grant_hra), decimalFormat.format(grant_cca), decimalFormat.format(grant_earnings), decimalFormat.format(grant_grosssalry));
//                pw.println();
//                pw.print(equalline);
//                pw.print(FORM_FEED);
//            }
//            if (psm.getOtherallowance().size() > 3) {
//                pw.println();
//                lineno++;
//
//                pw.println();
//                lineno++;
//
//                pw.println();
//                lineno++;
//            }
//            pw.flush();
//            pw.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public static void header(PrintWriter pw, PaySlipModel psm) {
        char[] horizontalsign = new char[132];
        for (int i = 0; i < 132; i++) {
            horizontalsign[i] = '~';
        }
        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
        pw.println();
        pw.write(BOLD);
        pw.printf("%-33s%3s%3s%4s", "RECOVERY LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.printf("%9s%-35s%-10s%-20s", "REGION : ", psm.getBranch(), "SECTION : ", psm.getSectionname());
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%3s%31s%11s%11s%10s%20s%18s%23s", "SNO", "EMP.NO EMP.NAME & DESIGNATION", "GPF/EPF", "V.P.F.", "S.P.F.", "OTHER DEDUCTIONS", "TOTAL", "NET SALARY");
        pw.println();
        pw.printf("%106s%12s%12s", "RECOVERY", "CASH", "CHEQUE");
        pw.println();
        pw.print(horizontalsign);
        pw.println();

    }

    public static void PagewisePrint(PrintWriter pw) {
        char[] horizontalsign = new char[132];
        char[] horizontalline = new char[132];
        char[] equalline = new char[132];
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        for (int i = 0; i < 132; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 132; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 132; i++) {
            equalline[i] = '=';
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%13s%4s%16s%12s%11s%10s%22s%18s%12s%12s", "PAGE NO :", pgno, "PAGE TOTAL", decimalFormat.format(pagewise_epf), decimalFormat.format(pagewise_vpf), decimalFormat.format(pagewise_spf), decimalFormat.format(pagewise_deductions), decimalFormat.format(pagewise_totalrecovery), decimalFormat.format(pagewise_cash), decimalFormat.format(pagewise_cheque));
        pgno++;
        pagewise_epf = 0;
        pagewise_vpf = 0;
        pagewise_spf = 0;
        pagewise_deductions = 0;
        pagewise_totalrecovery = 0;
        pagewise_cash = 0;
        pagewise_cheque = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.write(FORM_FEED);
    }

    public static void SectionPrint(PrintWriter pw) {
        char[] horizontalsign = new char[132];
        char[] horizontalline = new char[132];
        char[] equalline = new char[132];
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        for (int i = 0; i < 132; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 132; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 132; i++) {
            equalline[i] = '=';
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%13s%4s%16s%12s%11s%10s%22s%18s%12s%12s", "PAGE NO :", pgno, "PAGE TOTAL", decimalFormat.format(pagewise_epf), decimalFormat.format(pagewise_vpf), decimalFormat.format(pagewise_spf), decimalFormat.format(pagewise_deductions), decimalFormat.format(pagewise_totalrecovery), decimalFormat.format(pagewise_cash), decimalFormat.format(pagewise_cheque));
        pgno++;
        pagewise_epf = 0;
        pagewise_vpf = 0;
        pagewise_spf = 0;
        pagewise_deductions = 0;
        pagewise_totalrecovery = 0;
        pagewise_cash = 0;
        pagewise_cheque = 0;
        pw.println();
        pw.printf("%33s%12s%11s%10s%22s%18s%12s%12s", "SECTION TOTAL", decimalFormat.format(section_epf), decimalFormat.format(section_vpf), decimalFormat.format(section_spf), decimalFormat.format(section_deductions), decimalFormat.format(section_totalrecovery), decimalFormat.format(section_cash), decimalFormat.format(section_cheque));
        pw.println();
        section_epf = 0;
        section_vpf = 0;
        section_spf = 0;
        section_deductions = 0;
        section_totalrecovery = 0;
        section_cash = 0;
        section_cheque = 0;
        pw.print(equalline);
        pw.println();
        pw.write(FORM_FEED);
    }

    public static void GrandPrint(PrintWriter pw) {
        char[] horizontalsign = new char[132];
        char[] horizontalline = new char[132];
        char[] equalline = new char[132];
        pgno++;
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        for (int i = 0; i < 132; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 132; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 132; i++) {
            equalline[i] = '=';
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%13s%4s%16s%12s%11s%10s%22s%18s%12s%12s", "PAGE NO :", pgno, "PAGE TOTAL", decimalFormat.format(pagewise_epf), decimalFormat.format(pagewise_vpf), decimalFormat.format(pagewise_spf), decimalFormat.format(pagewise_deductions), decimalFormat.format(pagewise_totalrecovery), decimalFormat.format(pagewise_cash), decimalFormat.format(pagewise_cheque));
        pgno++;
        pagewise_epf = 0;
        pagewise_vpf = 0;
        pagewise_spf = 0;
        pagewise_deductions = 0;
        pagewise_totalrecovery = 0;
        pagewise_cash = 0;
        pagewise_cheque = 0;
        pw.println();
        pw.printf("%33s%12s%11s%10s%22s%18s%12s%12s", "SECTION TOTAL", decimalFormat.format(section_epf), decimalFormat.format(section_vpf), decimalFormat.format(section_spf), decimalFormat.format(section_deductions), decimalFormat.format(section_totalrecovery), decimalFormat.format(section_cash), decimalFormat.format(section_cheque));
        pw.println();
        section_epf = 0;
        section_vpf = 0;
        section_spf = 0;
        section_deductions = 0;
        section_totalrecovery = 0;
        section_cash = 0;
        section_cheque = 0;
        pw.print(equalline);
        pw.println();
        pw.printf("%33s%12s%11s%10s%22s%18s%12s%12s", "GRAND TOTAL", decimalFormat.format(grand_epf), decimalFormat.format(grand_vpf), decimalFormat.format(grand_spf), decimalFormat.format(grand_deductions), decimalFormat.format(grand_totalrecovery), decimalFormat.format(grand_cash), decimalFormat.format(grand_cheque));
        pw.println();
        pw.print(equalline);
        pw.write(FORM_FEED);
    }

    public void getDeductionLedgerPrintWriter(PaySlipModel psm, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/DeductionLedger.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            char[] horizontalsign = new char[132];
            char[] horizontalline = new char[132];
            char[] equalline = new char[132];
            for (int i = 0; i < 132; i++) {
                horizontalsign[i] = '~';
            }
            for (int i = 0; i < 132; i++) {
                horizontalline[i] = '-';
            }
            for (int i = 0; i < 132; i++) {
                equalline[i] = '=';
            }
            String sectionname = psm.getSectionname();

            if (Integer.valueOf(psm.getSlipno()) == 1) {
                lineno = 1;
                grand_epf = 0;
                grand_vpf = 0;
                grand_spf = 0;
                grand_deductions = 0;
                grand_totalrecovery = 0;
                grand_cash = 0;
                grand_cheque = 0;

            }

            if (lineno == 1) {
                secname = psm.getSectionname();
                pgno = 1;
                header(pw, psm);
            }
            if (!secname.equals(psm.getSectionname())) {
                SectionPrint(pw);
                header(pw, psm);
                secname = psm.getSectionname();
                lineno = 1;
            }

            if (lineno == 41) {
                PagewisePrint(pw);
                header(pw, psm);
                lineno = 1;
            }

            model = psm.getTotalrecovery().get(0);

            if (model.getCurrentinstallment() == null || model.getCurrentinstallment().length() == 0 || model.getTotalinstallment() == null || model.getTotalinstallment().length() == 0) {
                pw.printf("%3s%8s%23s%11s%11s%10s%11s%11s%18s%12s%12s", psm.getSlipno(), psm.getEmpno(), psm.getEmployeename(), psm.getEpf(), psm.getVpf(), psm.getSpf(), model.getDeductionname(), model.getDeductionamount(), psm.getTotaldeductions(), psm.getCashamount(), psm.getChequeamount());
            } else {
                pw.printf("%3s%8s%23s%11s%11s%10s%11s%11s%s%2s%s%2s%s%11s%12s%12s", psm.getSlipno(), psm.getEmpno(), psm.getEmployeename(), psm.getEpf(), psm.getVpf(), psm.getSpf(), model.getDeductionname(), model.getDeductionamount(), "(", model.getCurrentinstallment(), "/", model.getTotalinstallment(), ")", psm.getTotaldeductions(), psm.getCashamount(), psm.getChequeamount());
            }

            if (psm.getEpf() == null || psm.getEpf().equals("")) {
                section_epf += 0;
                pagewise_epf += 0;
                grand_epf += 0;
            } else {
                section_epf += Double.valueOf(psm.getEpf());
                pagewise_epf += Double.valueOf(psm.getEpf());
                grand_epf += Double.valueOf(psm.getEpf());
            }

            if (psm.getVpf() == null || psm.getVpf().equals("")) {
                section_vpf += 0;
                pagewise_vpf += 0;
                grand_vpf += 0;
            } else {
                section_vpf += Double.valueOf(psm.getVpf());
                pagewise_vpf += Double.valueOf(psm.getVpf());
                grand_vpf += Double.valueOf(psm.getVpf());
            }

            if (psm.getSpf() == null || psm.getSpf().equals("")) {
                section_spf += 0;
                pagewise_spf += 0;
                grand_spf += 0;
            } else {
                section_spf += Double.valueOf(psm.getSpf());
                pagewise_spf += Double.valueOf(psm.getSpf());
                grand_spf += Double.valueOf(psm.getSpf());
            }

            if (model.getDeductionamount() == null || model.getDeductionamount().equals("")) {
                section_deductions += 0;
                pagewise_deductions += 0;
                grand_deductions += 0;
            } else {
                section_deductions += Double.valueOf(model.getDeductionamount());
                pagewise_deductions += Double.valueOf(model.getDeductionamount());
                grand_deductions += Double.valueOf(model.getDeductionamount());
            }

            if (psm.getTotaldeductions() == null || psm.getTotaldeductions().equals("")) {
                section_totalrecovery += 0;
                pagewise_totalrecovery += 0;
                grand_totalrecovery += 0;
            } else {
                section_totalrecovery += Double.valueOf(psm.getTotaldeductions());
                pagewise_totalrecovery += Double.valueOf(psm.getTotaldeductions());
                grand_totalrecovery += Double.valueOf(psm.getTotaldeductions());
            }

            if (psm.getCashamount() == null || psm.getCashamount().equals("")) {
                section_cash += 0;
                pagewise_cash += 0;
                grand_cash += 0;
            } else {
                section_cash += Double.valueOf(psm.getCashamount());
                pagewise_cash += Double.valueOf(psm.getCashamount());
                grand_cash += Double.valueOf(psm.getCashamount());
            }

            if (psm.getChequeamount() == null || psm.getChequeamount().equals("")) {
                section_cheque += 0;
                pagewise_cheque += 0;
                grand_cheque += 0;
            } else {
                section_cheque += Double.valueOf(psm.getChequeamount());
                pagewise_cheque += Double.valueOf(psm.getChequeamount());
                grand_cheque += Double.valueOf(psm.getChequeamount());
            }
            pw.println();
            lineno++;
            if (lineno == 41) {
                PagewisePrint(pw);
                header(pw, psm);
                lineno = 1;
            }
            model = psm.getTotalrecovery().get(1);
            if (model.getCurrentinstallment() == null || model.getCurrentinstallment().length() == 0 || model.getTotalinstallment() == null || model.getTotalinstallment().length() == 0) {
                pw.printf("%4s%-10s%20s%43s%11s", "", psm.getPfno(), psm.getDesignation(), model.getDeductionname(), model.getDeductionamount());
            } else {
                pw.printf("%4s%-10s%20s%43s%11s%s%2s%s%2s%s", "", psm.getPfno(), psm.getDesignation(), model.getDeductionname(), model.getDeductionamount(), "(", model.getCurrentinstallment(), "/", model.getTotalinstallment(), ")");
            }
            pw.println();
            lineno++;
            if (lineno == 41) {
                PagewisePrint(pw);
                header(pw, psm);
                lineno = 1;
            }
            model = psm.getTotalrecovery().get(2);
            if (model.getCurrentinstallment() == null || model.getCurrentinstallment().length() == 0 || model.getTotalinstallment() == null || model.getTotalinstallment().length() == 0) {
                pw.printf("%15s%62s%11s", psm.getBankaccountno(), model.getDeductionname(), model.getDeductionamount());
            } else {
                pw.printf("%15s%62s%11s%s%2s%s%2s%s", psm.getBankaccountno(), model.getDeductionname(), model.getDeductionamount(), "(", model.getCurrentinstallment(), "/", model.getTotalinstallment(), ")");
            }
            pw.println();
            lineno++;
            if (lineno == 41) {
                PagewisePrint(pw);
                header(pw, psm);
                lineno = 1;
            }
            for (int i = 3; i < psm.getTotalrecovery().size(); i++) {
                model = psm.getTotalrecovery().get(i);
                if (model.getCurrentinstallment() == null || model.getCurrentinstallment().length() == 0 || model.getTotalinstallment() == null || model.getTotalinstallment().length() == 0) {
                    pw.printf("%77s%11s", model.getDeductionname(), model.getDeductionamount());
                } else {
                    pw.printf("%77s%11s%s%2s%s%2s%s", model.getDeductionname(), model.getDeductionamount(), "(", model.getCurrentinstallment(), "/", model.getTotalinstallment(), ")");
                }
                pw.println();
                lineno++;
                if (lineno == 41) {
                    PagewisePrint(pw);
                    header(pw, psm);
                    lineno = 1;
                }
            }
            if ((lineno + 1) == 41) {
            } else {
                pw.println();
                lineno++;
            }
            if (Integer.valueOf(psm.getSlipno()) == psm.getPrintingrecordsize()) {
                GrandPrint(pw);
            }
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getSalaryAbstractPrintWriter(PaySlipModel psm, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/SalaryAbstract.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            char[] horizontalsign = new char[80];
            char[] horizontalline = new char[80];
            char[] equalline = new char[80];
            for (int i = 0; i < 80; i++) {
                horizontalsign[i] = '~';
            }
            for (int i = 0; i < 80; i++) {
                horizontalline[i] = '-';
            }
            for (int i = 0; i < 80; i++) {
                equalline[i] = '=';
            }

            pw.println();
            pw.printf("%57s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, CHENNAI");
            pw.println();
            pw.printf("%47s%3s%3s%4s", "SALARY ABSTRACT(" + psm.getPaymenttype() + ") FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
//            pw.printf("%47s%3s%3s%4s", "SALARY ABSTRACT(Cash) FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
            pw.println();
            pw.printf("%-9s%-46s%-10s%-15s", "REGION : ", "HEAD OFFICE", "SECTION : ", psm.getSectionname());
//                pw.printf("%-9s%-43s%7s%3s%-10s%6s", "REGION : ", "HEAD OFFICE", "SECTION", " : ", psm.getSectionname(), psm.getSectionname());
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            pw.printf("%20s%15s%21s%16s", "ACCOUNT HEAD", "EARNINGS", "ACCOUNT HEAD", "DEDUCTIONS");
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            int listsize = psm.getEarningslist().size();
            for (int i = 0; i < listsize; i++) {
                PaySlip_Earn_Deduction_Model psedm = psm.getEarningslist().get(i);
                PaySlip_Earn_Deduction_Model psedm1 = psm.getDeductionlist().get(i);
                pw.printf("%7s%-15s%15s%6s%-15s%15s", "", psedm.getEarningsname(), psedm.getEarningsamount(), "", psedm1.getDeductionname(), psedm1.getDeductionamount());
                pw.println();
            }
            pw.printf("%25s%12s%24s%12s", "", "------------", "", "------------");
            pw.println();
            pw.printf("%21s%16s%22s%14s", "TOTAL EARNINGS", psm.getTotalearnings(), "TOTAL DEDUCTIONS", psm.getTotaldeductions());
            pw.println();
            pw.write(equalline);
            pw.println();
            pw.printf("%41s%3s%3s%4s%7s%-13s", "NET AMOUNT PAYABLE FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear(), "IS RS.", psm.getNetsalary());
            pw.println();
            pw.println();
            pw.println();
            pw.printf("%10s%s", "", psm.getNetsalarywords());
            pw.println();
            pw.println();
            pw.println();

//            if(psm.getPrintingrecordsize()==psm.getRecordno()){
//                pw.println();
//                pw.write(equalline);
//                pw.println();
//                pw.printf("%41s%3s%3s%4s%7s%-13s", "TOTAL NET AMOUNT PAYABLE FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear(), "IS RS.", psm.getGrandsalary());
//                pw.println();
//                pw.println();
//                pw.println();
//                pw.printf("%10s%s", "", psm.getGrandsalarywords());
//                pw.println();
//                pw.println();
//                pw.println();
//            }

            pw.printf("%29s%33s", "D.M.(BILLS)", "MANAGER(BILLS)");
            pw.println();
            pw.write(FORM_FEED);
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getSalaryAbstractCashPrintWriter(PaySlipModel psm, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/SalaryAbstractCash.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            char[] horizontalsign = new char[80];
            char[] horizontalline = new char[80];
            char[] equalline = new char[80];
            for (int i = 0; i < 80; i++) {
                horizontalsign[i] = '~';
            }
            for (int i = 0; i < 80; i++) {
                horizontalline[i] = '-';
            }
            for (int i = 0; i < 80; i++) {
                equalline[i] = '=';
            }

            pw.println();
            pw.printf("%57s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, CHENNAI");
            pw.println();
            pw.printf("%47s%3s%3s%4s", "SALARY ABSTRACT(Cash) FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
            pw.println();
            pw.printf("%-9s%-46s%-10s%-15s", "REGION : ", "HEAD OFFICE", "SECTION : ", psm.getSectionname());
//                pw.printf("%-9s%-43s%7s%3s%-10s%6s", "REGION : ", "HEAD OFFICE", "SECTION", " : ", psm.getSectionname(), psm.getSectionname());
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            pw.printf("%20s%15s%21s%16s", "ACCOUNT HEAD", "EARNINGS", "ACCOUNT HEAD", "DEDUCTIONS");
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            int listsize = psm.getEarningslist().size();
            for (int i = 0; i < listsize; i++) {
                PaySlip_Earn_Deduction_Model psedm = psm.getEarningslist().get(i);
                PaySlip_Earn_Deduction_Model psedm1 = psm.getDeductionlist().get(i);
                pw.printf("%7s%-15s%15s%6s%-15s%15s", "", psedm.getEarningsname(), psedm.getEarningsamount(), "", psedm1.getDeductionname(), psedm1.getDeductionamount());
                pw.println();
            }
            pw.printf("%25s%12s%24s%12s", "", "------------", "", "------------");
            pw.println();
            pw.printf("%21s%16s%22s%14s", "TOTAL EARNINGS", psm.getTotalearnings(), "TOTAL DEDUCTIONS", psm.getTotaldeductions());
            pw.println();
            pw.write(equalline);
            pw.println();
            pw.printf("%41s%3s%3s%4s%7s%-13s", "NET AMOUNT PAYABLE FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear(), "IS RS.", psm.getNetsalary());
            pw.println();
            pw.println();
            pw.println();
            pw.printf("%10s%s", "", psm.getNetsalarywords());
            pw.println();
            pw.println();
            pw.println();

            if (psm.getPrintingrecordsize() == psm.getRecordno()) {
                pw.println();
                pw.write(equalline);
                pw.println();
                pw.printf("%41s%3s%3s%4s%7s%-13s", "TOTAL NET AMOUNT PAYABLE FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear(), "IS RS.", psm.getGrandsalary());
                pw.println();
                pw.println();
                pw.println();
                pw.printf("%10s%s", "", psm.getGrandsalarywords());
                pw.println();
                pw.println();
                pw.println();
            }

            pw.printf("%29s%33s", "D.M.(BILLS)", "MANAGER(BILLS)");
            pw.println();
            pw.write(FORM_FEED);
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getSalaryAbstractBankPrintWriter(PaySlipModel psm, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/SalaryAbstractBank.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            char[] horizontalsign = new char[80];
            char[] horizontalline = new char[80];
            char[] equalline = new char[80];
            for (int i = 0; i < 80; i++) {
                horizontalsign[i] = '~';
            }
            for (int i = 0; i < 80; i++) {
                horizontalline[i] = '-';
            }
            for (int i = 0; i < 80; i++) {
                equalline[i] = '=';
            }

            pw.println();
            pw.printf("%57s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, CHENNAI");
            pw.println();
            pw.printf("%47s%3s%3s%4s", "SALARY ABSTRACT(Bank) FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
            pw.println();
            pw.printf("%-9s%-46s%-10s%-15s", "REGION : ", "HEAD OFFICE", "SECTION : ", psm.getSectionname());
//                pw.printf("%-9s%-43s%7s%3s%-10s%6s", "REGION : ", "HEAD OFFICE", "SECTION", " : ", psm.getSectionname(), psm.getSectionname());
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            pw.printf("%20s%15s%21s%16s", "ACCOUNT HEAD", "EARNINGS", "ACCOUNT HEAD", "DEDUCTIONS");
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            int listsize = psm.getEarningslist().size();
            for (int i = 0; i < listsize; i++) {
                PaySlip_Earn_Deduction_Model psedm = psm.getEarningslist().get(i);
                PaySlip_Earn_Deduction_Model psedm1 = psm.getDeductionlist().get(i);
                pw.printf("%7s%-15s%15s%6s%-15s%15s", "", psedm.getEarningsname(), psedm.getEarningsamount(), "", psedm1.getDeductionname(), psedm1.getDeductionamount());
                pw.println();
            }
            pw.printf("%25s%12s%24s%12s", "", "------------", "", "------------");
            pw.println();
            pw.printf("%21s%16s%22s%14s", "TOTAL EARNINGS", psm.getTotalearnings(), "TOTAL DEDUCTIONS", psm.getTotaldeductions());
            pw.println();
            pw.write(equalline);
            pw.println();
            pw.printf("%41s%3s%3s%4s%7s%-13s", "NET AMOUNT PAYABLE FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear(), "IS RS.", psm.getNetsalary());
            pw.println();
            pw.println();
            pw.println();
            pw.printf("%10s%s", "", psm.getNetsalarywords());
            pw.println();
            pw.println();
            pw.println();

            if (psm.getPrintingrecordsize() == psm.getRecordno()) {
                pw.println();
                pw.write(equalline);
                pw.println();
                pw.printf("%41s%3s%3s%4s%7s%-13s", "TOTAL NET AMOUNT PAYABLE FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear(), "IS RS.", psm.getGrandsalary());
                pw.println();
                pw.println();
                pw.println();
                pw.printf("%10s%s", "", psm.getGrandsalarywords());
                pw.println();
                pw.println();
                pw.println();
            }

            pw.printf("%29s%33s", "D.M.(BILLS)", "MANAGER(BILLS)");
            pw.println();
            pw.write(FORM_FEED);
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void EPFformheader(PrintWriter pw, PaySlipModel psm) {
        char[] horizontalsign = new char[140];
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%11s%s", "T.N.C.S.C.,", psm.getBranch());
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%s", "", "E.P.F. FORM 1");
        pw.write(RELEASE);
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%17s%3s%s%4s", "", "For the Month of ", psm.getPayslipmonth(), "-", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%-5s%-7s%-30s%-15s%-12s%-30s%-18s%-7s", "SNO","EPF.NO","EMPLOYEE NAME","DESIGNATION","SALARY","EMPLOYEE CONTRIBUTION","EMPLOYER CONTRI","REMARKS");
        pw.println();
        pw.printf("%-67s%-7s%-9s%-5s%-9s%-7s%-7s%-8s%5s", "NO.", "EPF", "LOAN", "VPF", "DA-VPF", "TOTAL", "8.33%", "3.67%", "TOTAL");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public static void EPFformpagetotal(PrintWriter pw, PaySlipModel psm) {
        char[] horizontalsign = new char[140];
        char[] horizontalline = new char[140];
        char[] equalline = new char[140];
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 140; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 140; i++) {
            equalline[i] = '=';
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%9s%3s%s%-41s%8s%8s%8s%8s%8s%8s%7s%7s%8s", "Page No.", pgno, " ", "PAGE TOTAL", Math.round(pagesalary), Math.round(pageepf), Math.round(pageloan), Math.round(pagevpf), "0", Math.round(pageemptotal), Math.round(pageper833), Math.round(pageper367), Math.round(pageremarkstotal));
        pagesalary = 0;
        pageepf = 0;
        pageloan = 0;
        pagevpf = 0;
        pageemptotal = 0;
        pageper833 = 0;
        pageper367 = 0;
        pageremarkstotal = 0;
        pw.println();
        pw.print(equalline);
        EPFformheader(pw, psm);
    }

    public static void EPFformgrandtotal(PrintWriter pw, PaySlipModel psm) {
        char[] horizontalsign = new char[140];
        char[] horizontalline = new char[140];
        char[] equalline = new char[140];
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 140; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 140; i++) {
            equalline[i] = '=';
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%9s%3s%s%-41s%8s%8s%8s%8s%8s%8s%7s%7s%8s", "Page No.", pgno, " ", "PAGE TOTAL", Math.round(pagesalary), Math.round(pageepf), Math.round(pageloan), Math.round(pagevpf), "0", Math.round(pageemptotal), Math.round(pageper833), Math.round(pageper367), Math.round(pageremarkstotal));
        pagesalary = 0;
        pageepf = 0;
        pageloan = 0;
        pagevpf = 0;
        pageemptotal = 0;
        pageper833 = 0;
        pageper367 = 0;
        pageremarkstotal = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%13s%-41s%8s%8s%8s%8s%8s%8s%7s%7s%8s", "", "GRAND TOTAL", Math.round(grsalary), Math.round(grepf), Math.round(grloan), Math.round(grvpf), "0", Math.round(gremptotal), Math.round(grper833), Math.round(grper367), Math.round(grremarkstotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.write(FORM_FEED);
        pw.println();
    }

    public void getEPFformPrintWriter(PaySlipModel psm, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/EPFForm.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            char[] horizontalsign = new char[140];
            char[] horizontalline = new char[140];
            char[] equalline = new char[140];
            for (int i = 0; i < 140; i++) {
                horizontalsign[i] = '~';
            }
            for (int i = 0; i < 140; i++) {
                horizontalline[i] = '-';
            }
            for (int i = 0; i < 140; i++) {
                equalline[i] = '=';
            }

            if (Integer.valueOf(psm.getSlipno()) == 1) {
                pgno = 1;
                pagesalary = 0;
                pageepf = 0;
                pageloan = 0;
                pagevpf = 0;
                pageemptotal = 0;
                pageper833 = 0;
                pageper367 = 0;
                pageremarkstotal = 0;

                grsalary = 0;
                grepf = 0;
                grloan = 0;
                grvpf = 0;
                gremptotal = 0;
                grper833 = 0;
                grper367 = 0;
                grremarkstotal = 0;
                EPFformheader(pw, psm);
            }
            pw.printf("%3s%s%7s%s%-30s%s%10s%s%8s%s%7s%s%7s%s%7s%s%7s%s%7s%s%6s%s%6s%s%7s", psm.getSlipno(), " ", psm.getPfno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psm.getSalary(), " ", psm.getEpf(), " ", psm.getEpfloan(), " ", psm.getVpf(), " ", "0", " ", psm.getEmployertotal(), " ", psm.getPercentage833(), " ", psm.getPercentage367(), " ", psm.getRemarkstotal());
            pagesalary += Double.valueOf(psm.getSalary());
            pageepf += Double.valueOf(psm.getEpf());
            pageloan += Double.valueOf(psm.getEpfloan());
            pagevpf += Double.valueOf(psm.getVpf());
            pageemptotal += Double.valueOf(psm.getEmployertotal());
            pageper833 += Double.valueOf(psm.getPercentage833());
            pageper367 += Double.valueOf(psm.getPercentage367());
            pageremarkstotal += Double.valueOf(psm.getRemarkstotal());

            grsalary += Double.valueOf(psm.getSalary());
            grepf += Double.valueOf(psm.getEpf());
            grloan += Double.valueOf(psm.getEpfloan());
            grvpf += Double.valueOf(psm.getVpf());
            gremptotal += Double.valueOf(psm.getEmployertotal());
            grper833 += Double.valueOf(psm.getPercentage833());
            grper367 += Double.valueOf(psm.getPercentage367());
            grremarkstotal += Double.valueOf(psm.getRemarkstotal());

            pw.println();
            pw.println();

            int slno = Integer.valueOf(psm.getSlipno());
            int totalrecords = Integer.valueOf(psm.getPrintingrecordsize());

            if (slno == totalrecords) {
                EPFformgrandtotal(pw, psm);
            } else if (Integer.valueOf(psm.getSlipno()) % 25 == 0) {
                EPFformpagetotal(pw, psm);
                pgno++;
            }
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void LICScheduleheader(PaySlipModel psm, PrintWriter pw) {
        char[] horizontalsign = new char[80];
        char[] horizontalline = new char[80];
        char[] equalline = new char[80];
        for (int i = 0; i < 80; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 80; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 80; i++) {
            equalline[i] = '=';
        }
        pw.println();
        pw.printf("%37s%s%3s%s%4s%s%-12s%12s%-11s", "TNCSC  LIC SCHEDULE FOR THE MONTH OF", " ", psm.getPayslipmonth(), " ", psm.getPayslipyear(), " ", psm.getBranch(), "PA CODE NO: ", "0002483071");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%-4s%-9s%-31s%-15s%-7s%9s", "SNO ", "EMPNO", "NAME", "DESIG", "AMOUNT", "POLICY NO");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void getLICSchedulePrintWriter(PaySlipModel psm, String filePath) {
        System.out.println("*********************************** PayBillPrinter class getLICSchedulePrintWriter method is calling");
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/LICSchedule.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            char[] horizontalsign = new char[80];
            char[] horizontalline = new char[80];
            char[] equalline = new char[80];
            for (int i = 0; i < 80; i++) {
                horizontalsign[i] = '~';
            }
            for (int i = 0; i < 80; i++) {
                horizontalline[i] = '-';
            }
            for (int i = 0; i < 80; i++) {
                equalline[i] = '=';
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
                pw.printf("%3s%s%-8s%s%-30s%s%-10s%s%10s%s%-12s", psm.getSlipno(), " ", psm.getEmpno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psedm.getLicamount(), " ", psedm.getLicpolicyno());
                grlicscheduletotal += Double.valueOf(psedm.getLicamount());
                psm.setSlipno("");
                psm.setEmpno("");
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

    public static void getHBAScheduleheader(PaySlipModel psm, PrintWriter pw) {
        char[] horizontalsign = new char[140];
        char[] horizontalline = new char[140];
        char[] equalline = new char[140];
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 140; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 140; i++) {
            equalline[i] = '=';
        }

        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%16s%s", "T.N.C.S.C.    .,", psm.getBranch());
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%s", "", "HBA SCHEDULE");
        pw.write(RELEASE);
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%-17s%3s%3s%4s", "", "For the Month of ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%13s%33s%11s%11s%11s%11s%11s%11s%5s", "SNO EMP.NO   ", "EMPLOYEE NAME                    ", "H.B.A.-1   ", "H.B.A.-2   ", "H.B.A.-3   ", "H.B.A.-4   ", "H.B.A.-5   ", "H.B.A.-6      ", "TOTAL");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%11s%s", "SECTION  : ", psm.getSectionname());
    }

    public static void getHBAScheduleSectionTotal(PaySlipModel psm, PrintWriter pw) {
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        char[] horizontalsign = new char[140];
        char[] horizontalline = new char[140];
        char[] equalline = new char[140];
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 140; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 140; i++) {
            equalline[i] = '=';
        }

        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "SECTION TOTAL", decimalFormat.format(hbaL07), " ", decimalFormat.format(hbaL08), " ", decimalFormat.format(hbaL25), " ", decimalFormat.format(hbaL36), " ", decimalFormat.format(hbaL37), " ", decimalFormat.format(hbaL38), " ", decimalFormat.format(hbatotal));
        hbaL07 = 0;
        hbaL08 = 0;
        hbaL25 = 0;
        hbaL36 = 0;
        hbaL37 = 0;
        hbaL38 = 0;
        hbatotal = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%11s%s", "SECTION  : ", psm.getSectionname());

    }

    public static void getHBAScheduleGrandTotal(String filePath) {
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        PrintWriter pw = null;
        File file = new File(filePath + "/HBASchedule.txt");
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        char[] horizontalsign = new char[140];
        char[] horizontalline = new char[140];
        char[] equalline = new char[140];
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 140; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 140; i++) {
            equalline[i] = '=';
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "SECTION TOTAL", decimalFormat.format(hbaL07), " ", decimalFormat.format(hbaL08), " ", decimalFormat.format(hbaL25), " ", decimalFormat.format(hbaL36), " ", decimalFormat.format(hbaL37), " ", decimalFormat.format(hbaL38), " ", decimalFormat.format(hbatotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "GRAND TOTAL", decimalFormat.format(gr_hbaL07), " ", decimalFormat.format(gr_hbaL08), " ", decimalFormat.format(gr_hbaL25), " ", decimalFormat.format(gr_hbaL36), " ", decimalFormat.format(gr_hbaL37), " ", decimalFormat.format(gr_hbaL38), " ", decimalFormat.format(gr_hbatotal));
        pw.println();
        pw.print(equalline);
        pw.flush();
        pw.close();

        hbaL07 = 0;
        hbaL08 = 0;
        hbaL25 = 0;
        hbaL36 = 0;
        hbaL37 = 0;
        hbaL38 = 0;
        hbatotal = 0;

        gr_hbaL07 = 0;
        gr_hbaL08 = 0;
        gr_hbaL25 = 0;
        gr_hbaL36 = 0;
        gr_hbaL37 = 0;
        gr_hbaL38 = 0;
        gr_hbatotal = 0;
    }

    public void getHBASchedulePrintWriter(PaySlipModel psm, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/HBASchedule.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            char[] horizontalsign = new char[140];
            char[] horizontalline = new char[140];
            char[] equalline = new char[140];
            for (int i = 0; i < 140; i++) {
                horizontalsign[i] = '~';
            }
            for (int i = 0; i < 140; i++) {
                horizontalline[i] = '-';
            }
            for (int i = 0; i < 140; i++) {
                equalline[i] = '=';
            }

//            System.out.println("psm.getRecordno() " + psm.getRecordno());
//            System.out.println("psm.getPrintingrecordsize() " + psm.getPrintingrecordsize());

            if (Integer.valueOf(psm.getSlipno()) == 1) {
                checksectionname = psm.getSectionname();
                lineno = 1;
                getHBAScheduleheader(psm, pw);
                hbaL07 = 0;
                hbaL08 = 0;
                hbaL25 = 0;
                hbaL36 = 0;
                hbaL37 = 0;
                hbaL38 = 0;
                hbatotal = 0;
            }
            if (!checksectionname.equalsIgnoreCase(psm.getSectionname())) {
                checksectionname = psm.getSectionname();
                getHBAScheduleSectionTotal(psm, pw);
                lineno = lineno + 3;
                if (lineno >= 52) {
                    pw.write(FORM_FEED);
                    pw.println();
                    getHBAScheduleheader(psm, pw);
                    lineno = 1;
                }
            }
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }
            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }


            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }
            pw.printf("%3s%s%-8s%s%-30s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", psm.getSlipno(), " ", psm.getEmpno(), " ", psm.getEmployeename(), " ", psm.getDeduction_map().get("L07").getDeductionamount(), " ", psm.getDeduction_map().get("L08").getDeductionamount(), " ", psm.getDeduction_map().get("L25").getDeductionamount(), " ", psm.getDeduction_map().get("L36").getDeductionamount(), " ", psm.getDeduction_map().get("L37").getDeductionamount(), " ", psm.getDeduction_map().get("L38").getDeductionamount(), " ", psm.getTotalhba());

            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }
            pw.printf("%13s%-32s%9s%11s%11s%11s%11s%11s", "", psm.getDesignation(), psm.getDeduction_map().get("L07").getInstallment(), psm.getDeduction_map().get("L08").getInstallment(), psm.getDeduction_map().get("L25").getInstallment(), psm.getDeduction_map().get("L36").getInstallment(), psm.getDeduction_map().get("L37").getInstallment(), psm.getDeduction_map().get("L38").getInstallment());
            hbaL07 += Double.valueOf(psm.getDeduction_map().get("L07").getDeductionamount());
            hbaL08 += Double.valueOf(psm.getDeduction_map().get("L08").getDeductionamount());
            hbaL25 += Double.valueOf(psm.getDeduction_map().get("L25").getDeductionamount());
            hbaL36 += Double.valueOf(psm.getDeduction_map().get("L36").getDeductionamount());
            hbaL37 += Double.valueOf(psm.getDeduction_map().get("L37").getDeductionamount());
            hbaL38 += Double.valueOf(psm.getDeduction_map().get("L38").getDeductionamount());
            hbatotal += Double.valueOf(psm.getTotalhba());

            gr_hbaL07 += Double.valueOf(psm.getDeduction_map().get("L07").getDeductionamount());
            gr_hbaL08 += Double.valueOf(psm.getDeduction_map().get("L08").getDeductionamount());
            gr_hbaL25 += Double.valueOf(psm.getDeduction_map().get("L25").getDeductionamount());
            gr_hbaL36 += Double.valueOf(psm.getDeduction_map().get("L36").getDeductionamount());
            gr_hbaL37 += Double.valueOf(psm.getDeduction_map().get("L37").getDeductionamount());
            gr_hbaL38 += Double.valueOf(psm.getDeduction_map().get("L38").getDeductionamount());
            gr_hbatotal += Double.valueOf(psm.getTotalhba());

            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }


            pw.printf("%13s%-10s", "", psm.getPfno());
            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }

            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getVehicleAdvanceheader(PaySlipModel psm, PrintWriter pw) {
        char[] horizontalsign = new char[140];
        char[] horizontalline = new char[140];
        char[] equalline = new char[140];
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 140; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 140; i++) {
            equalline[i] = '=';
        }

        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%11s%s", "T.N.C.S.C.,", psm.getBranch());
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%s", "", "VEHICLE ADVANCES SCHEDULE");
        pw.write(RELEASE);
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%-17s%3s%3s%4s", "", "For the Month of ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.print(horizontalsign);
        pw.println();
//        pw.printf("%-13s%-33s%-11s%-11s%-11s%-11s%-11s%-11s%5s", "SNO EMP.NO", "EMPLOYEE NAME", "CAR.ADV", "SCO.ADV", "CYC.ADV", "CAR.INT", "SCO.INT", "CYC.INT", "TOTAL");
        pw.printf("%-13s%-34s%-11s%-11s%-11s%-11s%-11s%-11s%5s", "SNO EMP.NO", "EMPLOYEE NAME", "CAR.ADV", "SCO.ADV", "CYC.ADV", "CAR.INT", "SCO.INT", "CYC.INT", "TOTAL");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%11s%s", "SECTION  : ", psm.getSectionname());
        pw.println();
    }

    public static void getVehicleAdvanceSectionTotal(PaySlipModel psm, PrintWriter pw) {
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        char[] horizontalsign = new char[140];
        char[] horizontalline = new char[140];
        char[] equalline = new char[140];
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 140; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 140; i++) {
            equalline[i] = '=';
        }

        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "SECTION TOTAL", decimalFormat.format(vehL09), " ", decimalFormat.format(vehL10), " ", decimalFormat.format(vehL11), " ", decimalFormat.format(vehL32), " ", decimalFormat.format(vehL21), " ", decimalFormat.format(vehL22), " ", decimalFormat.format(vehtotal));
        vehL09 = 0;
        vehL10 = 0;
        vehL11 = 0;
        vehL32 = 0;
        vehL21 = 0;
        vehL22 = 0;
        vehtotal = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%11s%s", "SECTION  : ", psm.getSectionname());

    }

    public static void getVehicleAdvanceGrandTotal(String filePath) {
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        PrintWriter pw = null;
        File file = new File(filePath + "/VehicleAdvance.txt");
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        char[] horizontalsign = new char[140];
        char[] horizontalline = new char[140];
        char[] equalline = new char[140];
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 140; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 140; i++) {
            equalline[i] = '=';
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "SECTION TOTAL", decimalFormat.format(vehL09), " ", decimalFormat.format(vehL10), " ", decimalFormat.format(vehL11), " ", decimalFormat.format(vehL32), " ", decimalFormat.format(vehL21), " ", decimalFormat.format(vehL22), " ", decimalFormat.format(vehtotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "GRAND TOTAL", decimalFormat.format(gr_vehL09), " ", decimalFormat.format(gr_vehL10), " ", decimalFormat.format(gr_vehL11), " ", decimalFormat.format(gr_vehL32), " ", decimalFormat.format(gr_vehL21), " ", decimalFormat.format(gr_vehL22), " ", decimalFormat.format(gr_vehtotal));
        pw.println();
        pw.print(equalline);
        pw.flush();
        pw.close();

        vehL09 = 0;
        vehL10 = 0;
        vehL11 = 0;
        vehL32 = 0;
        vehL21 = 0;
        vehL22 = 0;
        vehtotal = 0;

        gr_vehL09 = 0;
        gr_vehL10 = 0;
        gr_vehL11 = 0;
        gr_vehL32 = 0;
        gr_vehL21 = 0;
        gr_vehL22 = 0;
        gr_vehtotal = 0;
    }

    public void getVehicleAdvancePrintWriter(PaySlipModel psm, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/VehicleAdvance.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            char[] horizontalsign = new char[140];
            char[] horizontalline = new char[140];
            char[] equalline = new char[140];
            for (int i = 0; i < 140; i++) {
                horizontalsign[i] = '~';
            }
            for (int i = 0; i < 140; i++) {
                horizontalline[i] = '-';
            }
            for (int i = 0; i < 140; i++) {
                equalline[i] = '=';
            }

//            System.out.println("psm.getRecordno() " + psm.getRecordno());
//            System.out.println("psm.getPrintingrecordsize() " + psm.getPrintingrecordsize());

            if (Integer.valueOf(psm.getSlipno()) == 1) {
                checksectionname = psm.getSectionname();
                lineno = 1;
                getVehicleAdvanceheader(psm, pw);
                vehL09 = 0;
                vehL10 = 0;
                vehL11 = 0;
                vehL32 = 0;
                vehL21 = 0;
                vehL22 = 0;
                vehtotal = 0;
            }
            if (!checksectionname.equalsIgnoreCase(psm.getSectionname())) {
                checksectionname = psm.getSectionname();
                getVehicleAdvanceSectionTotal(psm, pw);
                lineno = lineno + 3;
                if (lineno >= 52) {
                    pw.write(FORM_FEED);
                    pw.println();
                    getVehicleAdvanceheader(psm, pw);
                    lineno = 1;
                }
            }
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }
            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }


            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }
            pw.printf("%3s%s%-8s%s%-30s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", psm.getSlipno(), " ", psm.getEmpno(), " ", psm.getEmployeename(), " ", psm.getDeduction_map().get("L09").getDeductionamount(), " ", psm.getDeduction_map().get("L10").getDeductionamount(), " ", psm.getDeduction_map().get("L11").getDeductionamount(), " ", psm.getDeduction_map().get("L32").getDeductionamount(), " ", psm.getDeduction_map().get("L21").getDeductionamount(), " ", psm.getDeduction_map().get("L22").getDeductionamount(), " ", psm.getTotalhba());

            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }
            pw.printf("%13s%-32s%9s%11s%11s%11s%11s%11s", "", psm.getDesignation(), psm.getDeduction_map().get("L09").getInstallment(), psm.getDeduction_map().get("L10").getInstallment(), psm.getDeduction_map().get("L11").getInstallment(), psm.getDeduction_map().get("L32").getInstallment(), psm.getDeduction_map().get("L21").getInstallment(), psm.getDeduction_map().get("L22").getInstallment());
            vehL09 += Double.valueOf(psm.getDeduction_map().get("L09").getDeductionamount());
            vehL10 += Double.valueOf(psm.getDeduction_map().get("L10").getDeductionamount());
            vehL11 += Double.valueOf(psm.getDeduction_map().get("L11").getDeductionamount());
            vehL32 += Double.valueOf(psm.getDeduction_map().get("L32").getDeductionamount());
            vehL21 += Double.valueOf(psm.getDeduction_map().get("L21").getDeductionamount());
            vehL22 += Double.valueOf(psm.getDeduction_map().get("L22").getDeductionamount());
            vehtotal += Double.valueOf(psm.getTotalhba());

            gr_vehL09 += Double.valueOf(psm.getDeduction_map().get("L09").getDeductionamount());
            gr_vehL10 += Double.valueOf(psm.getDeduction_map().get("L10").getDeductionamount());
            gr_vehL11 += Double.valueOf(psm.getDeduction_map().get("L11").getDeductionamount());
            gr_vehL32 += Double.valueOf(psm.getDeduction_map().get("L32").getDeductionamount());
            gr_vehL21 += Double.valueOf(psm.getDeduction_map().get("L21").getDeductionamount());
            gr_vehL22 += Double.valueOf(psm.getDeduction_map().get("L22").getDeductionamount());
            gr_vehtotal += Double.valueOf(psm.getTotalhba());

            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }


            pw.printf("%13s%-10s", "", psm.getPfno());
            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }

            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
