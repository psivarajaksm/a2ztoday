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

public class BankTextReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private String checksectionname = "";
    private double sectionnetttotal = 0;
    private double grantnetsalary = 0;
    private int pageno = 0;
    private DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private char[] horizontalsign = new char[70];
    private char[] horizontalline = new char[70];
    private char[] equalline = new char[70];
    private int recordno;

    public BankTextReport() {
        for (int i = 0; i < 70; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 70; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 70; i++) {
            equalline[i] = '=';
        }
    }

    public void grantBankText(PrintWriter pw, PaySlipModel psm) {
        pw.print(horizontalline);
        pw.println();
        pw.printf("%15s%-35s%-8s%12s", "", checksectionname, "TOTAL", decimalFormat.format(sectionnetttotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%44s%-14s%12s", "", "GRAND TOTAL   ", decimalFormat.format(grantnetsalary));
        pw.println();
        pw.print(equalline);
        pw.write(FORM_FEED);
        sectionnetttotal = 0;
        grantnetsalary = 0;
    }

    public void headerBankText(PrintWriter pw, PaySlipModel psm) {
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%s", "SNO   EPFNO      EMPLOYEE NAME     SALARY CREDIT SBI A/C NO     AMOUNT");
        pw.println();
        pw.printf("%35s%-4s%3s%s%3s", "", "FOR", psm.getPayslipmonth(), "-", psm.getPayslipyear());
        pw.println();
        pw.print(horizontalsign);
        pw.println();
//        sectionnetttotal = 0;
//        grantnetsalary = 0;
    }

    public void sectionwiseBankText(PrintWriter pw, PaySlipModel psm) {
        pw.print(horizontalline);
        pw.println();
        recordno++;
        pw.printf("%15s%-35s%-8s%12s", "", checksectionname, "TOTAL", decimalFormat.format(sectionnetttotal));
        pw.println();
        recordno++;
        pw.print(equalline);
        pw.println();
        recordno++;
        sectionnetttotal = 0;
    }

    public void getBankTextPrintWriter(PaySlipModel psm, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (Integer.valueOf(psm.getSlipno()) == 1) {
                checksectionname = psm.getSectionname();
                headerBankText(pw, psm);
                recordno = 1;
            }
            if (!checksectionname.equals(psm.getSectionname())) {
                sectionwiseBankText(pw, psm);
                checksectionname = psm.getSectionname();
            }
            pw.printf("%3s%s%-10s%s%-30s%s%13s%s%10s", psm.getSlipno(), " ", psm.getPfno(), " ", psm.getEmployeename(), " ", (psm.getBankaccountno() == null) ? "" : psm.getBankaccountno(), " ", psm.getNetsalary());
            sectionnetttotal += Double.valueOf(psm.getNetsalary());
            grantnetsalary += Double.valueOf(psm.getNetsalary());
            pw.println();
            recordno++;
            if (recordno >= 50) {
                recordno = 1;
                pw.print(FORM_FEED);
                pw.println();
                headerBankText(pw, psm);

            }
            pw.println();
            recordno++;
            if (recordno >= 50) {
                recordno = 1;
                pw.print(FORM_FEED);
                pw.println();
                headerBankText(pw, psm);
            }
            if (Integer.valueOf(psm.getSlipno()) == psm.getPrintingrecordsize()) {
                grantBankText(pw, psm);
            }
//            pw.print(horizontalline);
//            pw.println();
//            pw.printf("%15s%-35s%-8s%12s", "", psm.getSectionname(), "TOTAL", "XXXXXXXXXX");
//            pw.println();
//            pw.print(equalline);
//            pw.println();
//            pw.printf("%44s%-14s%12s", "", "GRAND TOTAL   ", "  XXXXXXXXXX");
//            pw.println();
//            pw.print(equalline);
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
