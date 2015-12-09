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

public class HBAScheduleInterestReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[140];
    private char[] horizontalline = new char[140];
    private char[] equalline = new char[140];
    private String checksectionname = "";
    private int lineno = 1;
    private double hbaL23 = 0;
    private double hbaL30 = 0;
    private double hbaL39 = 0;
    private double hbaL40 = 0;
    private double hbatotal = 0;
    private double gr_hbaL23 = 0;
    private double gr_hbaL30 = 0;
    private double gr_hbaL39 = 0;
    private double gr_hbaL40 = 0;
    private double gr_hbatotal = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public HBAScheduleInterestReport() {
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

    public void getHBAScheduleGrandTotal(String filePath) {
        System.out.println("******************* HBAScheduleInterestReport class getHBAScheduleGrandTotal method is calling ***************");
        
        PrintWriter pw = null;
        File file = new File(filePath);
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        pw.print(horizontalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s", "", "SECTION TOTAL", decimalFormat.format(hbaL23), " ", decimalFormat.format(hbaL30), " ", decimalFormat.format(hbaL39), " ", decimalFormat.format(hbaL40), " ", decimalFormat.format(hbatotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s", "", "GRAND TOTAL", decimalFormat.format(gr_hbaL23), " ", decimalFormat.format(gr_hbaL30), " ", decimalFormat.format(gr_hbaL39), " ", decimalFormat.format(gr_hbaL40), " ", decimalFormat.format(gr_hbatotal));
        pw.println();
        pw.print(equalline);
        pw.flush();
        pw.close();

        hbaL23 = 0;
        hbaL30 = 0;
        hbaL39 = 0;
        hbaL40 = 0;
        hbatotal = 0;

        gr_hbaL23 = 0;
        gr_hbaL30 = 0;
        gr_hbaL39 = 0;
        gr_hbaL40 = 0;
        gr_hbatotal = 0;
    }

    public void getHBAScheduleheader(PaySlipModel psm, PrintWriter pw) {

        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%11s%s", "T.N.C.S.C.,", psm.getBranch());
        pw.println();
        pw.write(BOLD);
        pw.printf("%71s%s", "", "INTEREST-HBA SCHEDULE");
        pw.write(RELEASE);
        pw.println();
        pw.write(BOLD);
        pw.printf("%65s%-17s%3s%3s%4s", "", "For the Month of ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%-13s%-33s%-11s%-11s%-11s%-13s%5s", "SNO EMP.NO", "EMPLOYEE NAME", "HBA-INT-1", "HBA-INT-2", "HBA-INT-3", "HBA-INT-4", "TOTAL");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%11s%s", "SECTION  : ", psm.getSectionname());
        pw.println();
        pw.println();
    }

    public void getHBAScheduleSectionTotal(PaySlipModel psm, PrintWriter pw) {

        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s", "", "SECTION TOTAL", decimalFormat.format(hbaL23), " ", decimalFormat.format(hbaL30), " ", decimalFormat.format(hbaL39), " ", decimalFormat.format(hbaL40), " ", decimalFormat.format(hbatotal));
        hbaL23 = 0;
        hbaL30 = 0;
        hbaL39 = 0;
        hbaL40 = 0;
        hbatotal = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%11s%s", "SECTION  : ", psm.getSectionname());

    }

    public void getHBASchedulePrintWriter(PaySlipModel psm, String filePath) {
        System.out.println("******************* HBAScheduleInterestReport class getHBASchedulePrintWriter method is calling ***************");
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
                checksectionname = psm.getSectionname();
                lineno = 1;
                getHBAScheduleheader(psm, pw);
                hbaL23 = 0;
                hbaL30 = 0;
                hbaL39 = 0;
                hbaL40 = 0;
                hbatotal = 0;
            }
            if (!checksectionname.equalsIgnoreCase(psm.getSectionname())) {
                checksectionname = psm.getSectionname();
                getHBAScheduleSectionTotal(psm, pw);
                lineno = lineno + 3;
                if (lineno >= 52) {
                    pw.write(FORM_FEED);
                    pw.println();
                    getHBAScheduleheader(psm, pw);
                    lineno = 1;
                }
            }
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }
            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }


            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }
            pw.printf("%3s%s%-8s%s%-30s%s%10s%s%10s%s%10s%s%10s%s%10s", psm.getSlipno(), " ", psm.getEmpno(), " ", psm.getEmployeename(), " ", psm.getDeduction_map().get("L23").getDeductionamount(), " ", psm.getDeduction_map().get("L30").getDeductionamount(), " ", psm.getDeduction_map().get("L39").getDeductionamount(), " ", psm.getDeduction_map().get("L40").getDeductionamount(), " ", psm.getTotalhba());

            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }
            pw.printf("%13s%-32s%9s%11s%11s%11s", "", psm.getDesignation(), psm.getDeduction_map().get("L23").getInstallment(), psm.getDeduction_map().get("L30").getInstallment(), psm.getDeduction_map().get("L39").getInstallment(), psm.getDeduction_map().get("L40").getInstallment());
            hbaL23 += Double.valueOf(psm.getDeduction_map().get("L23").getDeductionamount());
            hbaL30 += Double.valueOf(psm.getDeduction_map().get("L30").getDeductionamount());
            hbaL39 += Double.valueOf(psm.getDeduction_map().get("L39").getDeductionamount());
            hbaL40 += Double.valueOf(psm.getDeduction_map().get("L40").getDeductionamount());
            hbatotal += Double.valueOf(psm.getTotalhba());

            gr_hbaL23 += Double.valueOf(psm.getDeduction_map().get("L23").getDeductionamount());
            gr_hbaL30 += Double.valueOf(psm.getDeduction_map().get("L30").getDeductionamount());
            gr_hbaL39 += Double.valueOf(psm.getDeduction_map().get("L39").getDeductionamount());
            gr_hbaL40 += Double.valueOf(psm.getDeduction_map().get("L40").getDeductionamount());
            gr_hbatotal += Double.valueOf(psm.getTotalhba());

            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }

            pw.printf("%13s%-10s", "", psm.getPfno());
            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }

            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
