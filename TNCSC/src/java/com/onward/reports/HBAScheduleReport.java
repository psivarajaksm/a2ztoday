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

public class HBAScheduleReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[140];
    private char[] horizontalline = new char[140];
    private char[] equalline = new char[140];
    private String checksectionname = "";
    private int lineno = 1;
    private double hbaL07 = 0;
    private double hbaL08 = 0;
    private double hbaL25 = 0;
    private double hbaL36 = 0;
    private double hbaL37 = 0;
    private double hbaL38 = 0;
    private double hbatotal = 0;
    private double gr_hbaL07 = 0;
    private double gr_hbaL08 = 0;
    private double gr_hbaL25 = 0;
    private double gr_hbaL36 = 0;
    private double gr_hbaL37 = 0;
    private double gr_hbaL38 = 0;
    private double gr_hbatotal = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public HBAScheduleReport() {
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
        System.out.println("******************* HBAScheduleReport class getHBAScheduleGrandTotal method is calling ***************");

        PrintWriter pw = null;
        File file = new File(filePath + "/HBASchedule.txt");
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        pw.print(horizontalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "SECTION TOTAL", decimalFormat.format(hbaL07), " ", decimalFormat.format(hbaL08), " ", decimalFormat.format(hbaL25), " ", decimalFormat.format(hbaL36), " ", decimalFormat.format(hbaL37), " ", decimalFormat.format(hbaL38), " ", decimalFormat.format(hbatotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "GRAND TOTAL", decimalFormat.format(gr_hbaL07), " ", decimalFormat.format(gr_hbaL08), " ", decimalFormat.format(gr_hbaL25), " ", decimalFormat.format(gr_hbaL36), " ", decimalFormat.format(gr_hbaL37), " ", decimalFormat.format(gr_hbaL38), " ", decimalFormat.format(gr_hbatotal));
        pw.println();
        pw.print(equalline);
        pw.flush();
        pw.close();

        hbaL07 = 0;
        hbaL08 = 0;
        hbaL25 = 0;
        hbaL36 = 0;
        hbaL37 = 0;
        hbaL38 = 0;
        hbatotal = 0;

        gr_hbaL07 = 0;
        gr_hbaL08 = 0;
        gr_hbaL25 = 0;
        gr_hbaL36 = 0;
        gr_hbaL37 = 0;
        gr_hbaL38 = 0;
        gr_hbatotal = 0;
    }

    public void getHBAScheduleheader(PaySlipModel psm, PrintWriter pw) {

        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%16s%s", "T.N.C.S.C. LTD.,", psm.getBranch());
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%s", "", "HBA SCHEDULE");
        pw.write(RELEASE);
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%-17s%3s%3s%4s", "", "For the Month of ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%13s%33s%11s%11s%11s%11s%11s%11s%5s", "SNO EMP.NO   ", "EMPLOYEE NAME                    ", "H.B.A.-1   ", "H.B.A.-2   ", "H.B.A.-3   ", "H.B.A.-4   ", "H.B.A.-5   ", "H.B.A.-6      ", "TOTAL");
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
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "SECTION TOTAL", decimalFormat.format(hbaL07), " ", decimalFormat.format(hbaL08), " ", decimalFormat.format(hbaL25), " ", decimalFormat.format(hbaL36), " ", decimalFormat.format(hbaL37), " ", decimalFormat.format(hbaL38), " ", decimalFormat.format(hbatotal));
        hbaL07 = 0;
        hbaL08 = 0;
        hbaL25 = 0;
        hbaL36 = 0;
        hbaL37 = 0;
        hbaL38 = 0;
        hbatotal = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%11s%s", "SECTION  : ", psm.getSectionname());

    }

    public void getHBASchedulePrintWriter(PaySlipModel psm, String filePath) {
        System.out.println("******************* HBAScheduleReport class getHBASchedulePrintWriter method is calling ***************");
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/HBASchedule.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (Integer.valueOf(psm.getSlipno()) == 1) {
                checksectionname = psm.getSectionname();
                lineno = 1;
                getHBAScheduleheader(psm, pw);
                hbaL07 = 0;
                hbaL08 = 0;
                hbaL25 = 0;
                hbaL36 = 0;
                hbaL37 = 0;
                hbaL38 = 0;
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
            pw.printf("%3s%s%-8s%s%-30s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", psm.getSlipno(), " ", psm.getEmpno(), " ", psm.getEmployeename(), " ", psm.getDeduction_map().get("L07").getDeductionamount(), " ", psm.getDeduction_map().get("L08").getDeductionamount(), " ", psm.getDeduction_map().get("L25").getDeductionamount(), " ", psm.getDeduction_map().get("L36").getDeductionamount(), " ", psm.getDeduction_map().get("L37").getDeductionamount(), " ", psm.getDeduction_map().get("L38").getDeductionamount(), " ", psm.getTotalhba());

            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getHBAScheduleheader(psm, pw);
                lineno = 1;
            }
            pw.printf("%13s%-32s%9s%11s%11s%11s%11s%11s", "", psm.getDesignation(), psm.getDeduction_map().get("L07").getInstallment(), psm.getDeduction_map().get("L08").getInstallment(), psm.getDeduction_map().get("L25").getInstallment(), psm.getDeduction_map().get("L36").getInstallment(), psm.getDeduction_map().get("L37").getInstallment(), psm.getDeduction_map().get("L38").getInstallment());
            hbaL07 += Double.valueOf(psm.getDeduction_map().get("L07").getDeductionamount());
            hbaL08 += Double.valueOf(psm.getDeduction_map().get("L08").getDeductionamount());
            hbaL25 += Double.valueOf(psm.getDeduction_map().get("L25").getDeductionamount());
            hbaL36 += Double.valueOf(psm.getDeduction_map().get("L36").getDeductionamount());
            hbaL37 += Double.valueOf(psm.getDeduction_map().get("L37").getDeductionamount());
            hbaL38 += Double.valueOf(psm.getDeduction_map().get("L38").getDeductionamount());
            hbatotal += Double.valueOf(psm.getTotalhba());

            gr_hbaL07 += Double.valueOf(psm.getDeduction_map().get("L07").getDeductionamount());
            gr_hbaL08 += Double.valueOf(psm.getDeduction_map().get("L08").getDeductionamount());
            gr_hbaL25 += Double.valueOf(psm.getDeduction_map().get("L25").getDeductionamount());
            gr_hbaL36 += Double.valueOf(psm.getDeduction_map().get("L36").getDeductionamount());
            gr_hbaL37 += Double.valueOf(psm.getDeduction_map().get("L37").getDeductionamount());
            gr_hbaL38 += Double.valueOf(psm.getDeduction_map().get("L38").getDeductionamount());
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
