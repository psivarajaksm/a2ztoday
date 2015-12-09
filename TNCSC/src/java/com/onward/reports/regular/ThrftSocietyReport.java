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

public class ThrftSocietyReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[86];
    private char[] equalline = new char[86];
    private char[] horizontalline = new char[86];
    private int lineno = 1;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int count = 0;
    private double pagetotal = 0;
    private double grandtotal = 0;
    private int pageno = 1;

    public ThrftSocietyReport() {
        for (int i = 0; i < 86; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 86; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 86; i++) {
            equalline[i] = '=';
        }
    }

    public void Header(PaySlipModel psm, PrintWriter pw) {
        pw.println();
        pw.printf("%-39s%-7s%s%-15s", " TNCSC  THRFT SOCIETY FOR THE MONTH OF ", psm.getPayslipmonth(), " ", psm.getRegion());
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%s", "SNO  EPFNO        NAME                           DESIG             AMOUNT  ACCOUNT NO");
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
            pw.printf("%7s%-3s%-54s%10s", "PAGE ", pageno, " TOTAL", decimalFormat.format(pagetotal));
            pw.println();
            pw.print(equalline);
            pw.println();
            pw.printf("%-64s%10s", "  GRAND TOTAL", decimalFormat.format(grandtotal));
            pw.println();
            pw.print(equalline);
            pw.println();
            pw.flush();
            pw.close();
            pagetotal = 0;
            grandtotal = 0;
            pageno = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void PageTotal(PaySlipModel psm, PrintWriter pw) {
        pw.print(horizontalline);
        pw.println();
        pw.printf("%7s%-3s%-54s%10s", "PAGE ", pageno, " TOTAL", decimalFormat.format(pagetotal));
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.print(FORM_FEED);
        pagetotal = 0;
        pageno++;
    }

    public void getPrintWriter(PaySlipModel psm, String filePath) {
//        System.out.println("******************* PLIScheduleReport class getDeductionAllPrintWriter method is calling ***************");
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
            }
            if (Integer.valueOf(psm.getSlipno()) % 25 == 0) {
                PageTotal(psm, pw);
                Header(psm, pw);
            }
            pw.printf("%3s%s%-12s%s%-30s%s%-15s%s%10s%s%-10s", psm.getSlipno(), " ", psm.getPfno(), " ", psm.getEmployeename(), " ", (psm.getDesignation() == null) ? "" : psm.getDesignation(), " ", psm.getAmount(), " ", psm.getSocietyno());
            pw.println();
            pw.println();
            pagetotal += Double.valueOf(psm.getAmount());
            grandtotal += Double.valueOf(psm.getAmount());
//            pw.print(horizontalline);
//            pw.println();
//            pw.printf("%7s%-3s%-54s%10s", "PAGE ", "XXX", " TOTAL", "xxxxxxxxxx");
//            pw.println();
//            pw.print(equalline);
//            pw.println();
//            pw.printf("%-64s%10s", "  GRAND TOTAL", "xxxxxxxxxx");
//            pw.println();
//            pw.print(equalline);
//            pw.println();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
