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

public class SupplementaryEPFformReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[140];
    private char[] horizontalline = new char[140];
    private char[] equalline = new char[140];
    private int pgno = 0;
    private double pagesalary = 0;
    private double pageepf = 0;
    private double pageloan = 0;
    private double pagevpf = 0;
    private double pageemptotal = 0;
    private double pageremarkstotal = 0;
    private double grsalary = 0;
    private double grepf = 0;
    private double grloan = 0;
    private double grvpf = 0;
    private double gremptotal = 0;
    private double grremarkstotal = 0;

    public SupplementaryEPFformReport() {
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 140; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 140; i++) {
            equalline[i] = '=';
        }

    }

    public void EPFformheader(PrintWriter pw, PaySlipModel psm) {
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
        pw.printf("%126s", "SNO EPF.NO     EMPLOYEE NAME                 DESIGNATION   SALARY      EMPLOYEE CONTRIBUTION      EMPLOYEE EMPLOYER CALCULATED");
        pw.println();
        pw.printf("%126s", "NO.                                                                   EPF    LOAN     VPF  DA-VPF   TOTAL   CONTRI  MONTH&YEAR");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void EPFformgrandtotal(PrintWriter pw, PaySlipModel psm) {
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
        pw.printf("%9s%3s%s%-44s%8s%8s%8s%8s%8s%8s%8s", "Page No.", pgno, " ", "PAGE TOTAL", Math.round(pagesalary), Math.round(pageepf), Math.round(pageloan), Math.round(pagevpf), "0", Math.round(pageemptotal),  Math.round(pageremarkstotal));
        pagesalary = 0;
        pageepf = 0;
        pageloan = 0;
        pagevpf = 0;
        pageemptotal = 0;
        pageremarkstotal = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%13s%-44s%8s%8s%8s%8s%8s%8s%8s", "", "GRAND TOTAL", Math.round(grsalary), Math.round(grepf), Math.round(grloan), Math.round(grvpf), "0", Math.round(gremptotal), Math.round(grremarkstotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.write(FORM_FEED);
        pw.println();
    }

    public void EPFformpagetotal(PrintWriter pw, PaySlipModel psm) {
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
        pw.printf("%9s%3s%s%-44s%8s%8s%8s%8s%8s%8s%8s", "Page No.", pgno, " ", "PAGE TOTAL", Math.round(pagesalary), Math.round(pageepf), Math.round(pageloan), Math.round(pagevpf), "0", Math.round(pageemptotal), Math.round(pageremarkstotal));
        pagesalary = 0;
        pageepf = 0;
        pageloan = 0;
        pagevpf = 0;
        pageemptotal = 0;
        pageremarkstotal = 0;
        pw.println();
        pw.print(equalline);
        EPFformheader(pw, psm);
    }

    public void getEPFformPrintWriter(PaySlipModel psm, String filePath) {
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
                pgno = 1;
                pagesalary = 0;
                pageepf = 0;
                pageloan = 0;
                pagevpf = 0;
                pageemptotal = 0;
                pageremarkstotal = 0;

                grsalary = 0;
                grepf = 0;
                grloan = 0;
                grvpf = 0;
                gremptotal = 0;
                grremarkstotal = 0;
                EPFformheader(pw, psm);
            }
            pw.printf("%3s%s%-10s%s%-30s%s%-10s%s%8s%s%7s%s%7s%s%7s%s%7s%s%7s%s%7s%3s%3s%s%4s", psm.getSlipno(), " ", psm.getPfno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psm.getSalary(), " ", psm.getEpf(), " ", psm.getEpfloan(), " ", psm.getVpf(), " ", "0", " ", psm.getEmployertotal(), " ", psm.getRemarkstotal(), "", psm.getPayslipmonthprevious(), "-", psm.getPayslipyearprevious());
            pagesalary += Double.valueOf(psm.getSalary());
            pageepf += Double.valueOf(psm.getEpf());
            pageloan += Double.valueOf(psm.getEpfloan());
            pagevpf += Double.valueOf(psm.getVpf());
            pageemptotal += Double.valueOf(psm.getEmployertotal());
            pageremarkstotal += Double.valueOf(psm.getRemarkstotal());

            grsalary += Double.valueOf(psm.getSalary());
            grepf += Double.valueOf(psm.getEpf());
            grloan += Double.valueOf(psm.getEpfloan());
            grvpf += Double.valueOf(psm.getVpf());
            gremptotal += Double.valueOf(psm.getEmployertotal());
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
}
