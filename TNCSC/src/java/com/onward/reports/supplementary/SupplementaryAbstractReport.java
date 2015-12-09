/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.supplementary;

/**
 *
 * @author Prince vijayakumar.M
 */

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

public class SupplementaryAbstractReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] horizontalline = new char[80];
    private char[] equalline = new char[80];

    public SupplementaryAbstractReport() {
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

    public void getSalaryAbstractPrintWriter(PaySlipModel psm, String filePath) {
//        System.out.println("***************** SupplementaryAbstractReport class getSalaryAbstractPrintWriter method is calling *******************");
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            pw.println();
//            pw.printf("%57s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, CHENNAI");
            pw.printf("%62s", "TAMIL NADU CIVIL SUPPLIES CORPORATION., CHENNAI");
            pw.println();
//            pw.printf("%47s%3s%3s%4s", "SALARY ABSTRACT(" + psm.getPaymenttype() + ") FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
            pw.printf("%45s%-24s", "SALARY ABSTRACT (SUPPLEMENTARY BILL) ", psm.getBilltype());
            pw.println();
//            pw.printf("%-9s%-46s%-10s%-15s", "REGION : ", "HEAD OFFICE", "SECTION : ", psm.getSectionname());
            pw.printf("%-9s%-46s%10s", "REGION : ", psm.getBranch(), psm.getPayslipyear());
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
            pw.printf("%19s%s%-19s%4s%-10s%9s%11s", "NET AMOUNT PAYABLE", " ", psm.getBilltype(), "ON ", psm.getPayslipyear(), "IS RS.  ", psm.getNetsalary());
            pw.println();
            pw.println();
            pw.println();
            pw.printf("%10s%s", "", psm.getNetsalarywords());
            pw.println();
            pw.println();
            pw.println();
            pw.printf("%29s%33s", "D.M.(BILLS)", "MANAGER(BILLS)");
            pw.println();
            pw.write(FORM_FEED);
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
