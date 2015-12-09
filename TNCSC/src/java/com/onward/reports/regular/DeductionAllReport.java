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

public class DeductionAllReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[106];
    private char[] equalline = new char[106];
    private char[] horizontalline = new char[106];
    private int lineno = 1;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int count = 0;
    private double pagetotal = 0;
    private double sectiontotal = 0;
    private double grandtotal = 0;
    private int pageno = 1;
    private String checksectionname = "";

    public DeductionAllReport() {
        for (int i = 0; i < 106; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 106; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 106; i++) {
            equalline[i] = '=';
        }
    }

    public void DeductionAllHeader(PaySlipModel psm, PrintWriter pw) {
        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%-12s%s", "T.N.C.S.C.,", psm.getBranch());
        pw.println();
        pw.write(BOLD);
        pw.printf("%46s%-20s%s", "", psm.getDeductionname(), "SCHEDULE");
        pw.println();
        pw.printf("%50s%-17s%3s%3s%4s", "", "FOR THE MONTH OF ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%-4s%-11s%-31s%-17s%s", "SNO", "EPF.NO", "EMPLOYEE NAME", "DESIGNATION", "Rs.");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%-10s%s", "SECTION :", psm.getSectionname());
        pw.println();
    }

    public void DeductionAllGrandTotal(PaySlipModel psm, String filePath) {
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
            pw.printf("%-9s%-3s%27s%21s%9s", "Page No. ", pageno, "PAGE TOTAL", "", decimalFormat.format(pagetotal));
            pw.println();
            pw.print(equalline);
            pw.println();
            pw.printf("%39s%21s%9s", "SECTION TOTAL", "", decimalFormat.format(sectiontotal));
            pw.println();
            pw.print(equalline);
            pw.println();
            pw.printf("%39s%21s%9s", "GRAND TOTAL", "", decimalFormat.format(grandtotal));
            pw.println();
            pw.print(equalline);
            pw.println();
            pw.write(FORM_FEED);
            pagetotal = 0;
            sectiontotal = 0;
            grandtotal = 0;
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void DeductionAllSectionTotal(PaySlipModel psm, PrintWriter pw) {
        pw.print(horizontalline);
        pw.println();
        pw.printf("%39s%21s%9s", "SECTION TOTAL", "", decimalFormat.format(sectiontotal));
        sectiontotal = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%-10s%s", "SECTION :", psm.getSectionname());
        pw.println();

    }

    public void DeductionAllPageTotal(PaySlipModel psm, PrintWriter pw) {
        pw.print(horizontalline);
        pw.println();
        pw.printf("%-9s%-3s%27s%21s%9s", "Page No. ", pageno, "PAGE TOTAL", "", decimalFormat.format(pagetotal));
        pw.println();
        pagetotal = 0;
        pageno++;
        pw.print(equalline);
        pw.write(FORM_FEED);
        count = 1;
    }

    public void getDeductionAllPrintWriter(PaySlipModel psm, String filePath) {
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
                DeductionAllHeader(psm, pw);
                checksectionname = psm.getSectionname();
            }

            if (!checksectionname.equals(psm.getSectionname())) {
                DeductionAllSectionTotal(psm, pw);
                checksectionname = psm.getSectionname();
                count += 4;
            }

            if (count > 52) {
                DeductionAllPageTotal(psm, pw);
                DeductionAllHeader(psm, pw);
            }

            pw.println();
            count++;
            pw.printf("%3s%s%-10s%s%-30s%s%-13s%s%9s%s%-9s%s%-25s", psm.getSlipno(), " ", psm.getPfno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psm.getAmount(), " ", psm.getInstallment()," ",psm.getFileno());
            pw.println();
            pagetotal += Double.valueOf(psm.getAmount());
            sectiontotal += Double.valueOf(psm.getAmount());
            grandtotal += Double.valueOf(psm.getAmount());
            count++;
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
