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
import java.util.Set;
import org.jboss.management.j2ee.JVM;

public class AcquitanceReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private String checksectionname = "";
    private double pagetotalearnings = 0;
    private double pagetotaldeduction = 0;
    private double pagenetttotal = 0;
    private double sectiontotalearnings = 0;
    private double sectiontotaldeduction = 0;
    private double sectionnetttotal = 0;
    private double grantearningstotal = 0;
    private double grantdeductiontotal = 0;
    private double grantnetsalary = 0;
    private int pageno = 0;
    private DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private char[] horizontalsign = new char[80];
    private char[] horizontalline = new char[80];
    private char[] equalline = new char[80];
    private int recordno;

    public AcquitanceReport() {
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

    public void pagetotalAcquitanceSlip(PrintWriter pw) {
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

    public void grantAcquitanceSlip(PrintWriter pw) {
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

    public void headerAcquitanceSlip(PrintWriter pw, PaySlipModel psm) {
        pw.println();
        pw.printf("%57s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, CHENNAI");
        pw.println();
        pw.printf("%47s%3s%3s%4s", "PAY ACQUITANCE SLIP (" + psm.getPaymenttype() + ") FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.println();
        pw.printf("%-9s%-46s%-10s%-15s", "REGION : ", psm.getBranch(), "SECTION : ", psm.getSectionname());
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%4s%12s%24s%13s%12s%13s", "SNO", "EPF.NO", "EMPLOYEE NAME", "EARNINGS", "RECOVERY", "NET");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void sectionwiseAcquitanceSlip(PrintWriter pw) {
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

    public void getAcquitanceSlipPrintWriter(PaySlipModel psm, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (psm.getSlipno().equals("1")) {
                pageno = 1;
                checksectionname = psm.getSectionname();
                headerAcquitanceSlip(pw, psm);
                recordno = 1;
            }
            if (!checksectionname.equals(psm.getSectionname()) || recordno == 7) {
                if (!checksectionname.equals(psm.getSectionname()) && recordno == 7) {
                    checksectionname = psm.getSectionname();
                    sectionwiseAcquitanceSlip(pw);
                    headerAcquitanceSlip(pw, psm);
                    recordno = 1;

                } else if (!checksectionname.equals(psm.getSectionname()) && recordno != 7) {
                    checksectionname = psm.getSectionname();
                    sectionwiseAcquitanceSlip(pw);
                    headerAcquitanceSlip(pw, psm);
                    recordno = 1;
                } else if (recordno == 7 && checksectionname.equals(psm.getSectionname())) {
                    pagetotalAcquitanceSlip(pw);
                    headerAcquitanceSlip(pw, psm);
                    recordno = 1;
                }

            }
            pw.printf("%4s%12s%24s%13s%12s%13s", psm.getSlipno(), psm.getPfno(), psm.getEmployeename(), psm.getTotalearnings(), psm.getTotaldeductions(), psm.getNetsalary());
            pw.println();
            pw.printf("%40s", psm.getDesignation());
//            pw.printf("%10s%-15s%15s", "", psm.getBankaccountno(),psm.getDesignation());
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
            pagenetttotal += Double.valueOf(psm.getNetsalary());
            System.out.println("");

            sectiontotalearnings += Double.valueOf(psm.getTotalearnings());
            sectiontotaldeduction += Double.valueOf(psm.getTotaldeductions());
            sectionnetttotal += Double.valueOf(psm.getNetsalary());

            grantearningstotal += Double.valueOf(psm.getTotalearnings());
            grantdeductiontotal += Double.valueOf(psm.getTotaldeductions());
            grantnetsalary += Double.valueOf(psm.getNetsalary());

            recordno++;
            if (Integer.valueOf(psm.getSlipno()) == psm.getPrintingrecordsize()) {
                grantAcquitanceSlip(pw);
            }
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
