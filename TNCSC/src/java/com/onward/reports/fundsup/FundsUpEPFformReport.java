/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.fundsup;

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

public class FundsUpEPFformReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[150];
    private char[] horizontalline = new char[150];
    private char[] equalline = new char[150];
    private long salary = 0;
    private long epf = 0;
    private long epfloan = 0;
    private long vpf = 0;
    private long davpf = 0;
    private long nrl = 0;
    private long employertotal = 0;
    private long ecfb = 0;
    private long ecpf = 0;
    private long grand_salary = 0;
    private long grand_epf = 0;
    private long grand_epfloan = 0;
    private long grand_vpf = 0;
    private long grand_davpf = 0;
    private long grand_nrl = 0;
    private long grand_employertotal = 0;
    private long grand_ecfb = 0;
    private long grand_ecpf = 0;
    private String type;
    private int sno;

    public FundsUpEPFformReport() {
        for (int i = 0; i < 150; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 150; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 150; i++) {
            equalline[i] = '=';
        }

    }

    public void EPFformheader(PrintWriter pw, PaySlipModel psm) {

        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%-39s%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, ", psm.getBranch());
        pw.println();
        pw.write(BOLD);
//        pw.printf("%92s%-15s%s", "", "E.P.F. SCHEDULE", psm.getEpfcategory());
        pw.printf("%92s%-16s%s", "", "E.P.F. SCHEDULE ", psm.getSupplementarytype());
        pw.write(RELEASE);
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%-17s%3s%s%4s", "", "For the Month of ", psm.getPayslipmonth(), "-", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%-140s", "SNO    EPFNO        NAME                       DESIGNATION    EMP_SAL  EMP_SUB EMP_LREC  EMP_VPF EMP_DAVPF EMP_NRL     TOTAL MNG_CON PEN_CON DABATCHNO");
        pw.println();
        pw.printf("%140s", "                                                                          12%                                                 3.67%    8.33%");
        pw.println();
        pw.print(horizontalsign);
        pw.println();

    }

    public void EPFformTotalPrint(PrintWriter pw, String category) {
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%43s%-6s%10s%10s%s%8s%s%8s%s%8s%s%8s%s%8s%s%10s%s%6s%s%6s", category, " TOTAL", "", salary, " ", epf, " ", epfloan, " ", vpf, " ", davpf, " ", nrl, " ", employertotal, " ", ecfb, " ", ecpf);
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.write(FORM_FEED);
        pw.println();
        salary = 0;
        epf = 0;
        epfloan = 0;
        vpf = 0;
        davpf = 0;
        nrl = 0;
        employertotal = 0;
        ecfb = 0;
        ecpf = 0;
    }

    public void EPFformGrandTotalPrint(String filePath, String category) {

        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.print(horizontalsign);
            pw.println();
            pw.printf("%43s%-6s%10s%10s%s%8s%s%8s%s%8s%s%8s%s%8s%s%10s%s%6s%s%6s", category, " TOTAL", "", salary, " ", epf, " ", epfloan, " ", vpf, " ", davpf, " ", nrl, " ", employertotal, " ", ecfb, " ", ecpf);
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            pw.printf("%31s%11s%17s%10s%s%8s%s%8s%s%8s%s%8s%s%8s%s%10s%s%6s%s%6s", "", "GRAND TOTAL", "", grand_salary, " ", grand_epf, " ", grand_epfloan, " ", grand_vpf, " ", grand_davpf, " ", grand_nrl, " ", grand_employertotal, " ", grand_ecfb, " ", grand_ecpf);
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            pw.println();
            pw.printf("%24s%45s", "", "=============================================");
            pw.println();
            pw.printf("%24s%-34s%10s%s", "", "|EMP_SUBCRIPTION  (12%)         | ", grand_epf, "|");
            pw.println();
            pw.printf("%69s", "                        |                               |           |");
            pw.println();
            pw.printf("%24s%-34s%10s%s", "", "|EMP_LOAN RECOVERY              | ", grand_epfloan, "|");
            pw.println();
            pw.printf("%69s", "                        |                               |           |");
            pw.println();
            pw.printf("%24s%-34s%10s%s", "", "|EMP_VOLUNTARY                  | ", grand_vpf, "|");
            pw.println();
            pw.printf("%69s", "                        |                               |           |");
            pw.println();
            pw.printf("%24s%-34s%10s%s", "", "|EMP_DA.VOLUNTARY               | ", grand_davpf, "|");
            pw.println();
            pw.printf("%69s", "                        |                               |           |");
            pw.println();
            pw.printf("%24s%-34s%10s%s", "", "|EMP_NRL RECOVERY               | ", grand_nrl, "|");
            pw.println();
            pw.printf("%69s", "                        |                               |           |");
            pw.println();
            pw.printf("%24s%-34s%10s%s", "", "|MANAGEMENT CONTRIBUTION (3.67%)| ", grand_ecfb, "|");
            pw.println();
            pw.printf("%69s", "                        |                               |           |");
            pw.println();
            pw.printf("%24s%-34s%10s%s", "", "|TOTAL                          | ", (grand_epf + grand_epfloan + grand_vpf + grand_davpf + grand_nrl + grand_ecfb), "|");
            pw.println();
            pw.printf("%69s", "                        |                               |           |");
            pw.println();
            pw.printf("%24s%-34s%10s%s", "", "|PENSION CONTRIBUTION    (8.33%)| ", grand_ecpf, "|");
            pw.println();
            pw.printf("%24s%45s", "", "=============================================");
            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.printf("%80s", "MANAGER (ACCOUNTS)/ DEPUTY MANAGER (ACCOUNTS)");
            pw.write(FORM_FEED);

            pw.flush();
            pw.close();
            salary = 0;
            epf = 0;
            epfloan = 0;
            vpf = 0;
            davpf = 0;
            nrl = 0;
            employertotal = 0;
            ecfb = 0;
            ecpf = 0;

            grand_salary = 0;
            grand_epf = 0;
            grand_epfloan = 0;
            grand_vpf = 0;
            grand_davpf = 0;
            grand_nrl = 0;
            grand_employertotal = 0;
            grand_ecfb = 0;
            grand_ecpf = 0;


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void getEPFformPrintWriter(PaySlipModel psm, String filePath) {

        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (Integer.valueOf(psm.getSlipno()) == 1) {
                EPFformheader(pw, psm);
                type = psm.getSupplementarytype();
                sno = 1;
            }

            if (Integer.valueOf(psm.getSlipno()) % 28 == 0) {
                pw.write(FORM_FEED);
                EPFformheader(pw, psm);
            }

            if (!type.equals(psm.getSupplementarytype())) {
                EPFformTotalPrint(pw, type);
                EPFformheader(pw, psm);
                type = psm.getSupplementarytype();
                sno = 1;
            }

            pw.printf("%3s%s%-12s%s%-30s%s%-10s%s%10s%s%8s%s%8s%s%8s%s%8s%s%8s%s%10s%s%6s%s%6s%s%8s", sno, " ", psm.getPfno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psm.getSalary(), " ", psm.getEpf(), " ", psm.getEpfloan(), " ", psm.getVpf(), " ", psm.getDavpf(), " ", psm.getNrl(), " ", psm.getEmployertotal(), " ", psm.getPercentage367(), " ", psm.getPercentage833(), " ", (psm.getDabatchno() != null) ? psm.getDabatchno() : "");
            salary += Long.valueOf(psm.getSalary());
            epf += Long.valueOf(psm.getEpf());
            epfloan += Long.valueOf(psm.getEpfloan());
            vpf += Long.valueOf(psm.getVpf());
            davpf += Long.valueOf(psm.getDavpf());
            nrl += Long.valueOf(psm.getNrl());
            employertotal += Long.valueOf(psm.getEmployertotal());
            ecfb += Long.valueOf(psm.getPercentage367());
            ecpf += Long.valueOf(psm.getPercentage833());

            grand_salary += Long.valueOf(psm.getSalary());
            grand_epf += Long.valueOf(psm.getEpf());
            grand_epfloan += Long.valueOf(psm.getEpfloan());
            grand_vpf += Long.valueOf(psm.getVpf());
            grand_davpf += Long.valueOf(psm.getDavpf());
            grand_nrl += Long.valueOf(psm.getNrl());
            grand_employertotal += Long.valueOf(psm.getEmployertotal());
            grand_ecfb += Long.valueOf(psm.getPercentage367());
            grand_ecpf += Long.valueOf(psm.getPercentage833());

            sno++;

            pw.println();
            pw.println();
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
