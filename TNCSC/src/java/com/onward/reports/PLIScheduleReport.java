/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports;

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

public class PLIScheduleReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] equalline = new char[80];
    private char[] dotline = new char[80];
    private int lineno = 1;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private double pagetotal = 0;
    private double grandtotal = 0;

    public PLIScheduleReport() {
        for (int i = 0; i < 80; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 80; i++) {
            equalline[i] = '=';
        }
        for (int i = 0; i < 80; i++) {
            dotline[i] = '-';
        }
    }

    public void getPLIScheduleGrandTotalPrintWriter(PaySlipModel psm, String filePath) {
        System.out.println("******************* PLIScheduleReport class getHBASchedulePrintWriter method is calling ***************");
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/PLISchedule.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.println();
            pw.print(dotline);
            pw.println();
            pw.printf("%-8s%3s%28s%31s%8s", "Page No", "1", "PAGE TOTAL", "", decimalFormat.format(pagetotal));
            pw.println();
            pw.print(equalline);
            pw.println();
            pw.printf("%39s%31s%8s", "GRAND TOTAL", "", decimalFormat.format(grandtotal));
            pw.println();
            pw.print(equalline);
            pw.write(FORM_FEED);
            pw.println();
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void PLIScheduleHeader(PaySlipModel psm, PrintWriter pw) {
        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%s%s", "T.N.C.S.C. LTD.,", psm.getBranch());
        pw.println();
        pw.write(BOLD);
        pw.printf("%s", "PLI SCHEDULE");
        pw.write(RELEASE);
        pw.println();
        pw.write(BOLD);
        pw.printf("%-17s%3s%3s%4s", "For the Month of", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%-17s%-29s%-14s%6s", "SNO EPF.NO", "EMPLOYEE NAME", "DESIGNATION   ", "PLI NO", "AMOUNT");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void getPLISchedulePrintWriter(PaySlipModel psm, String filePath) {
//        System.out.println("******************* PLIScheduleReport class getHBASchedulePrintWriter method is calling ***************");
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/PLISchedule.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (Integer.valueOf(psm.getSlipno()) == 1) {
                PLIScheduleHeader(psm, pw);
            }
            pw.printf("%3s%s%-10s%s%-30s%s%-11s%s%-11s%s%8s", psm.getSlipno(), " ", psm.getPfno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psm.getPlino(), " ", psm.getAmount());
            pw.println();
            pw.println();
            pagetotal += Double.valueOf(psm.getAmount());
            grandtotal += Double.valueOf(psm.getAmount());
            lineno++;
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
