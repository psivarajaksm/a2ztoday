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

public class StorageLossReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[110];
    private char[] equalline = new char[110];
    private char[] horizontalline = new char[110];
    private int lineno = 1;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int count = 0;
    private double pagetotal = 0;
    private double grandtotal = 0;
    private int pageno = 1;

    public StorageLossReport() {
        for (int i = 0; i < 110; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 110; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 110; i++) {
            equalline[i] = '=';
        }
    }

    public void Header(PaySlipModel psm, PrintWriter pw) {
        pw.println();
        pw.printf("%69s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
        pw.println();
        pw.printf("%-19s%20s%27s%-8s", "", psm.getSchedulename(), "SCHEDULE FOR THE MONTH OF ", psm.getPayslipmonth());
        pw.println();
        pw.printf("%36s%-30s", "", psm.getRegion());
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%s", "SNO  EPF.NO        EMPLOYEE NAME                 DESIGNATION  GROUP NAME     FILE.NO                    AMOUNT");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void GrandTotal(PaySlipModel psm, String filePath) {
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
            pw.printf("%8s%-39s%-52s%11s", "Page NO.", pageno, "PAGE TOTAL", decimalFormat.format(pagetotal));
            pw.println();
            pw.print(equalline);
            pw.println();
            pw.printf("%99s%11s", "GRAND TOTAL                                          ", decimalFormat.format(grandtotal));
            pw.println();
            pw.print(equalline);
            pw.println();
            pw.print(FORM_FEED);
            pagetotal = 0;
            grandtotal = 0;
            pageno = 1;
            lineno = 1;
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void PageTotal(PaySlipModel psm, PrintWriter pw) {
        pw.print(horizontalline);
        pw.println();
        pw.printf("%8s%-39s%-52s%11s", "Page NO.", pageno, "PAGE TOTAL", decimalFormat.format(pagetotal));
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.print(FORM_FEED);
        lineno = 1;
        pagetotal = 0;
        pageno++;
    }

    public void getPrintWriter(PaySlipModel psm, String filePath) {
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
                Header(psm, pw);
                lineno = 1;
            }

            if (lineno > 48) {
                PageTotal(psm, pw);
                Header(psm, pw);
            }

            Iterator itr = psm.getLiclist().iterator();
            int x = 1;
            while (itr.hasNext()) {
                PaySlip_Earn_Deduction_Model psedm = (PaySlip_Earn_Deduction_Model) itr.next();
                if (x == 1) {
                    pw.printf("%3s%s%-13s%s%-30s%s%-12s%s%-12s%s%-23s%s%11s", psm.getSlipno(), " ", psm.getPfno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psedm.getPaycodename(), " ", psedm.getLicpolicyno(), " ", psedm.getDeductionamount());
                    pw.println();
                } else {
                    pw.printf("%62s%-12s%s%-23s%s%11s", "                                                              ", psedm.getPaycodename(), " ", psedm.getLicpolicyno(), " ", psedm.getDeductionamount());
                    pw.println();
                }
                grandtotal += Double.valueOf(psedm.getDeductionamount());
                pagetotal += Double.valueOf(psedm.getDeductionamount());
                lineno++;
                x++;
                if (lineno > 48) {
                    PageTotal(psm, pw);
                    Header(psm, pw);
                }
            }
            pw.println();
            lineno++;
            if (lineno > 48) {
                PageTotal(psm, pw);
                Header(psm, pw);
            }
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
