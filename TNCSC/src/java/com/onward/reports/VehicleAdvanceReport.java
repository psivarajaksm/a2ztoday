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

public class VehicleAdvanceReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private char[] horizontalsign = new char[140];
    private char[] horizontalline = new char[140];
    private char[] equalline = new char[140];
    private String checksectionname = "";
    private int lineno = 1;
    private double vehL09 = 0;
    private double vehL10 = 0;
    private double vehL11 = 0;
    private double vehL32 = 0;
    private double vehL21 = 0;
    private double vehL22 = 0;
    private double vehtotal = 0;
    private double gr_vehL09 = 0;
    private double gr_vehL10 = 0;
    private double gr_vehL11 = 0;
    private double gr_vehL32 = 0;
    private double gr_vehL21 = 0;
    private double gr_vehL22 = 0;
    private double gr_vehtotal = 0;

    public VehicleAdvanceReport() {
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

    public void getVehicleAdvanceheader(PaySlipModel psm, PrintWriter pw) {
        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%16s%s", "T.N.C.S.C. LTD.,", psm.getBranch());
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%s", "", "VEHICLE ADVANCES SCHEDULE");
        pw.write(RELEASE);
        pw.println();
        pw.write(BOLD);
        pw.printf("%92s%-17s%3s%3s%4s", "", "For the Month of ", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.print(horizontalsign);
        pw.println();
//        pw.printf("%-13s%-33s%-11s%-11s%-11s%-11s%-11s%-11s%5s", "SNO EMP.NO", "EMPLOYEE NAME", "CAR.ADV", "SCO.ADV", "CYC.ADV", "CAR.INT", "SCO.INT", "CYC.INT", "TOTAL");
        pw.printf("%-13s%-34s%-11s%-11s%-11s%-11s%-11s%-11s%5s", "SNO EMP.NO", "EMPLOYEE NAME", "CAR.ADV", "SCO.ADV", "CYC.ADV", "CAR.INT", "SCO.INT", "CYC.INT", "TOTAL");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%11s%s", "SECTION  : ", psm.getSectionname());
        pw.println();
    }

    public void getVehicleAdvanceSectionTotal(PaySlipModel psm, PrintWriter pw) {

        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "SECTION TOTAL", decimalFormat.format(vehL09), " ", decimalFormat.format(vehL10), " ", decimalFormat.format(vehL11), " ", decimalFormat.format(vehL32), " ", decimalFormat.format(vehL21), " ", decimalFormat.format(vehL22), " ", decimalFormat.format(vehtotal));
        vehL09 = 0;
        vehL10 = 0;
        vehL11 = 0;
        vehL32 = 0;
        vehL21 = 0;
        vehL22 = 0;
        vehtotal = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%11s%s", "SECTION  : ", psm.getSectionname());

    }

    public void getVehicleAdvanceGrandTotal(String filePath) {
        PrintWriter pw = null;
        File file = new File(filePath + "/VehicleAdvance.txt");
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "SECTION TOTAL", decimalFormat.format(vehL09), " ", decimalFormat.format(vehL10), " ", decimalFormat.format(vehL11), " ", decimalFormat.format(vehL32), " ", decimalFormat.format(vehL21), " ", decimalFormat.format(vehL22), " ", decimalFormat.format(vehtotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%18s%-26s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "", "GRAND TOTAL", decimalFormat.format(gr_vehL09), " ", decimalFormat.format(gr_vehL10), " ", decimalFormat.format(gr_vehL11), " ", decimalFormat.format(gr_vehL32), " ", decimalFormat.format(gr_vehL21), " ", decimalFormat.format(gr_vehL22), " ", decimalFormat.format(gr_vehtotal));
        pw.println();
        pw.print(equalline);
        pw.flush();
        pw.close();

        vehL09 = 0;
        vehL10 = 0;
        vehL11 = 0;
        vehL32 = 0;
        vehL21 = 0;
        vehL22 = 0;
        vehtotal = 0;

        gr_vehL09 = 0;
        gr_vehL10 = 0;
        gr_vehL11 = 0;
        gr_vehL32 = 0;
        gr_vehL21 = 0;
        gr_vehL22 = 0;
        gr_vehtotal = 0;
    }

    public void getVehicleAdvancePrintWriter(PaySlipModel psm, String filePath) {
        System.out.println("***************** VehicleAdvanceReport class getVehicleAdvancePrintWriter method is calling *******************");
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/VehicleAdvance.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (Integer.valueOf(psm.getSlipno()) == 1) {
                checksectionname = psm.getSectionname();
                lineno = 1;
                getVehicleAdvanceheader(psm, pw);
                vehL09 = 0;
                vehL10 = 0;
                vehL11 = 0;
                vehL32 = 0;
                vehL21 = 0;
                vehL22 = 0;
                vehtotal = 0;
            }
            if (!checksectionname.equalsIgnoreCase(psm.getSectionname())) {
                checksectionname = psm.getSectionname();
                getVehicleAdvanceSectionTotal(psm, pw);
                lineno = lineno + 3;
                if (lineno >= 52) {
                    pw.write(FORM_FEED);
                    pw.println();
                    getVehicleAdvanceheader(psm, pw);
                    lineno = 1;
                }
            }
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }
            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }
            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }
            pw.printf("%3s%s%-8s%s%-30s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", psm.getSlipno(), " ", psm.getEmpno(), " ", psm.getEmployeename(), " ", psm.getDeduction_map().get("L09").getDeductionamount(), " ", psm.getDeduction_map().get("L10").getDeductionamount(), " ", psm.getDeduction_map().get("L11").getDeductionamount(), " ", psm.getDeduction_map().get("L32").getDeductionamount(), " ", psm.getDeduction_map().get("L21").getDeductionamount(), " ", psm.getDeduction_map().get("L22").getDeductionamount(), " ", psm.getTotalhba());

            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }
            pw.printf("%13s%-32s%9s%11s%11s%11s%11s%11s", "", psm.getDesignation(), psm.getDeduction_map().get("L09").getInstallment(), psm.getDeduction_map().get("L10").getInstallment(), psm.getDeduction_map().get("L11").getInstallment(), psm.getDeduction_map().get("L32").getInstallment(), psm.getDeduction_map().get("L21").getInstallment(), psm.getDeduction_map().get("L22").getInstallment());
            vehL09 += Double.valueOf(psm.getDeduction_map().get("L09").getDeductionamount());
            vehL10 += Double.valueOf(psm.getDeduction_map().get("L10").getDeductionamount());
            vehL11 += Double.valueOf(psm.getDeduction_map().get("L11").getDeductionamount());
            vehL32 += Double.valueOf(psm.getDeduction_map().get("L32").getDeductionamount());
            vehL21 += Double.valueOf(psm.getDeduction_map().get("L21").getDeductionamount());
            vehL22 += Double.valueOf(psm.getDeduction_map().get("L22").getDeductionamount());
            vehtotal += Double.valueOf(psm.getTotalhba());

            gr_vehL09 += Double.valueOf(psm.getDeduction_map().get("L09").getDeductionamount());
            gr_vehL10 += Double.valueOf(psm.getDeduction_map().get("L10").getDeductionamount());
            gr_vehL11 += Double.valueOf(psm.getDeduction_map().get("L11").getDeductionamount());
            gr_vehL32 += Double.valueOf(psm.getDeduction_map().get("L32").getDeductionamount());
            gr_vehL21 += Double.valueOf(psm.getDeduction_map().get("L21").getDeductionamount());
            gr_vehL22 += Double.valueOf(psm.getDeduction_map().get("L22").getDeductionamount());
            gr_vehtotal += Double.valueOf(psm.getTotalhba());

            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }
            
            pw.printf("%13s%-10s", "", psm.getPfno());
            pw.println();
            lineno++;
            if (lineno == 52) {
                pw.write(FORM_FEED);
                pw.println();
                getVehicleAdvanceheader(psm, pw);
                lineno = 1;
            }

            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
