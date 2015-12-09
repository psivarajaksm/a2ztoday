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

public class FestivalAdvanceReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[91];
    private char[] equalline = new char[91];
    private char[] horizontalline = new char[91];
    private int lineno = 1;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private double sectiontotal = 0;
    private double grandtotal = 0;
    private String section = "";

    public FestivalAdvanceReport() {
        for (int i = 0; i < 91; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 91; i++) {
            equalline[i] = '=';
        }
        for (int i = 0; i < 91; i++) {
            horizontalline[i] = '-';
        }
    }

    public void grandTotal(String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.print(horizontalline);
            pw.println();
            pw.printf("%-81s%10s", " SECTION TOTAL :", decimalFormat.format(sectiontotal));
            pw.println();
            pw.print(equalline);
            pw.println();
            pw.printf("%-81s%10s", " GRAND TOTAL   :", decimalFormat.format(grandtotal));
            pw.println();
            pw.print(equalline);
            pw.println();
            pw.flush();
            pw.close();
            sectiontotal = 0;
            grandtotal = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sectiontotal(PrintWriter pw) {
        pw.print(horizontalline);
        pw.println();
        pw.printf("%-81s%10s", " SECTION TOTAL :", decimalFormat.format(sectiontotal));
        sectiontotal = 0;
        pw.println();
        pw.print(horizontalline);
        pw.println();
    }

    public void header(PaySlipModel psm, PrintWriter pw) {
        pw.println();
        pw.printf("%70s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, CHENNAI");
        pw.println();
        pw.printf("%45s%-6s%4s%-10s", "FESTIVAL ADVANCE", psm.getPaymentmode(), " ON ", psm.getDate());
        pw.println();
        pw.printf("%-9s%-57s%-10s%-15s", "REGION : ", psm.getRegion(), "SECTION : ", psm.getSectionname());
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%s", " SNO EPF.NO       EMPLOYEE NAME                DESIGNATION       BANK A/C NO        AMOUNT");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void getFestivalAdvancePrintWriter(PaySlipModel psm, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        String bankacno = "";
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (Integer.valueOf(psm.getSlipno()) == 1) {
                header(psm, pw);
                section = psm.getSectionname();
            }
            if (!psm.getSectionname().equals(section)) {
                sectiontotal(pw);
                header(psm, pw);
                section = psm.getSectionname();
            }
            if(psm.getBankaccountno().equals("Null") || psm.getBankaccountno()==null){
                bankacno = "";
            }else{
                bankacno = psm.getBankaccountno();
            }
            pw.printf("%4s%s%-12s%s%-30s%s%-15s%s%-15s%s%10s", psm.getSlipno(), " ", psm.getEpf(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", bankacno, " ", psm.getAmount());
            sectiontotal += Double.valueOf(psm.getAmount());
            grandtotal += Double.valueOf(psm.getAmount());
            pw.println();
            pw.println();
            pw.println();
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
