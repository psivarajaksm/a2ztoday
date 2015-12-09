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

public class LICScheduleReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] horizontalline = new char[80];
    private char[] equalline = new char[80];
    private int lineno = 1;
    private double grlicscheduletotal = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public LICScheduleReport() {
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

    public void LICScheduleheader(PaySlipModel psm, PrintWriter pw) {
        pw.println();
        pw.printf("%37s%s%3s%s%4s%s%-12s%12s%-11s", "TNCSC  LIC SCHEDULE FOR THE MONTH OF", " ", psm.getPayslipmonth(), " ", psm.getPayslipyear(), " ", psm.getBranch(), "PA CODE NO: ", "0002483071");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%-4s%-9s%-31s%-15s%-7s%9s", "SNO ", "EMPNO", "NAME", "DESIG", "AMOUNT", "POLICY NO");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void getLICSchedulePrintWriter(PaySlipModel psm, String filePath) {
        System.out.println("*********************************** LICScheduleReport class getLICSchedulePrintWriter method is calling ******************************");
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/LICSchedule.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (Integer.valueOf(psm.getSlipno()) == 1) {
                LICScheduleheader(psm, pw);
                lineno = 1;
                grlicscheduletotal = 0;
            }

            Iterator itr = psm.getLiclist().iterator();
            while (itr.hasNext()) {
                if (lineno == 52) {
                    pw.write(FORM_FEED);
                    LICScheduleheader(psm, pw);
                    lineno = 1;
                }
                PaySlip_Earn_Deduction_Model psedm = (PaySlip_Earn_Deduction_Model) itr.next();
                pw.printf("%3s%s%-8s%s%-30s%s%-10s%s%10s%s%-12s", psm.getSlipno(), " ", psm.getEmpno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psedm.getLicamount(), " ", psedm.getLicpolicyno());
                grlicscheduletotal += Double.valueOf(psedm.getLicamount());
                psm.setSlipno("");
                psm.setEmpno("");
                psm.setEmployeename("");
                psm.setDesignation("");
                pw.println();
                lineno++;
            }
            if (lineno == 52) {
//                System.out.println("lineno 52");
                pw.write(FORM_FEED);
                LICScheduleheader(psm, pw);
                lineno = 1;
            }
            pw.println();
            lineno++;
            if (psm.getPrintingrecordsize() == psm.getRecordno()) {
                pw.print(equalline);
                pw.println();
                pw.printf("%52s%3s%10s", "GRAND TOTAL", " : ", decimalFormat.format(grlicscheduletotal));
                pw.println();
                pw.print(equalline);
                pw.println();
                pw.write(FORM_FEED);
            }
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
