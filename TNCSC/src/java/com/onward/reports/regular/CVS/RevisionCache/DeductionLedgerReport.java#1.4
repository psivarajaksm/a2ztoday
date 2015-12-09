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

public class DeductionLedgerReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[132];
    private char[] horizontalline = new char[132];
    private char[] equalline = new char[132];
    private double pagewise_epf = 0;
    private double pagewise_vpf = 0;
    private double pagewise_spf = 0;
    private double pagewise_deductions = 0;
    private double pagewise_totalrecovery = 0;
    private double pagewise_cash = 0;
    private double pagewise_cheque = 0;
    private double section_epf = 0;
    private double section_vpf = 0;
    private double section_spf = 0;
    private double section_deductions = 0;
    private double section_totalrecovery = 0;
    private double section_cash = 0;
    private double section_cheque = 0;
    private double grand_epf = 0;
    private double grand_vpf = 0;
    private double grand_spf = 0;
    private double grand_deductions = 0;
    private double grand_totalrecovery = 0;
    private double grand_cash = 0;
    private double grand_cheque = 0;
    private int lineno = 1;
    private int sno = 1;
    private int pgno = 0;
    private String secname = "";
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public DeductionLedgerReport() {
        for (int i = 0; i < 132; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 132; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 132; i++) {
            equalline[i] = '=';
        }

    }

    public void header(PrintWriter pw, PaySlipModel psm) {
        char[] horizontalsign = new char[132];
        for (int i = 0; i < 132; i++) {
            horizontalsign[i] = '~';
        }
        pw.println();
        pw.write(LARGEFONT);
        pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
        pw.println();
        pw.write(BOLD);
        pw.printf("%-33s%3s%3s%4s", "RECOVERY LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
        pw.write(RELEASE);
        pw.println();
        pw.printf("%9s%-35s%-10s%-20s", "REGION : ", psm.getBranch(), "SECTION : ", psm.getSectionname());
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%3s%31s%11s%11s%10s%20s%18s%23s", "SNO", "EMP.NO EMP.NAME & DESIGNATION", "GPF/EPF", "V.P.F.", "S.P.F.", "OTHER DEDUCTIONS", "TOTAL", "NET SALARY");
        pw.println();
        pw.printf("%106s%12s%12s", "RECOVERY", "CASH", "CHEQUE");
        pw.println();
        pw.print(horizontalsign);
        pw.println();

    }

    public void SectionPrint(PrintWriter pw) {
        char[] horizontalsign = new char[132];
        char[] horizontalline = new char[132];
        char[] equalline = new char[132];
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        for (int i = 0; i < 132; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 132; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 132; i++) {
            equalline[i] = '=';
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%13s%4s%16s%12s%11s%10s%22s%18s%12s%12s", "PAGE NO :", pgno, "PAGE TOTAL", decimalFormat.format(pagewise_epf), decimalFormat.format(pagewise_vpf), decimalFormat.format(pagewise_spf), decimalFormat.format(pagewise_deductions), decimalFormat.format(pagewise_totalrecovery), decimalFormat.format(pagewise_cash), decimalFormat.format(pagewise_cheque));
        pgno++;
        pagewise_epf = 0;
        pagewise_vpf = 0;
        pagewise_spf = 0;
        pagewise_deductions = 0;
        pagewise_totalrecovery = 0;
        pagewise_cash = 0;
        pagewise_cheque = 0;
        pw.println();
        pw.printf("%33s%12s%11s%10s%22s%18s%12s%12s", "SECTION TOTAL", decimalFormat.format(section_epf), decimalFormat.format(section_vpf), decimalFormat.format(section_spf), decimalFormat.format(section_deductions), decimalFormat.format(section_totalrecovery), decimalFormat.format(section_cash), decimalFormat.format(section_cheque));
        pw.println();
        section_epf = 0;
        section_vpf = 0;
        section_spf = 0;
        section_deductions = 0;
        section_totalrecovery = 0;
        section_cash = 0;
        section_cheque = 0;
        pw.print(equalline);
        pw.println();
        pw.write(FORM_FEED);
    }

    public void PagewisePrint(PrintWriter pw) {
        char[] horizontalsign = new char[132];
        char[] horizontalline = new char[132];
        char[] equalline = new char[132];
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        for (int i = 0; i < 132; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 132; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 132; i++) {
            equalline[i] = '=';
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%13s%4s%16s%12s%11s%10s%22s%18s%12s%12s", "PAGE NO :", pgno, "PAGE TOTAL", decimalFormat.format(pagewise_epf), decimalFormat.format(pagewise_vpf), decimalFormat.format(pagewise_spf), decimalFormat.format(pagewise_deductions), decimalFormat.format(pagewise_totalrecovery), decimalFormat.format(pagewise_cash), decimalFormat.format(pagewise_cheque));
        pgno++;
        pagewise_epf = 0;
        pagewise_vpf = 0;
        pagewise_spf = 0;
        pagewise_deductions = 0;
        pagewise_totalrecovery = 0;
        pagewise_cash = 0;
        pagewise_cheque = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.write(FORM_FEED);
    }

    public void GrandPrint(PaySlipModel psm, String filePath) {
        PrintWriter pw = null;
        File file = new File(filePath);
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        char[] horizontalsign = new char[132];
        char[] horizontalline = new char[132];
        char[] equalline = new char[132];
//        pgno++;
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        for (int i = 0; i < 132; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 132; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 132; i++) {
            equalline[i] = '=';
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%13s%4s%16s%12s%11s%10s%22s%18s%12s%12s", "PAGE NO :", pgno, "PAGE TOTAL", decimalFormat.format(pagewise_epf), decimalFormat.format(pagewise_vpf), decimalFormat.format(pagewise_spf), decimalFormat.format(pagewise_deductions), decimalFormat.format(pagewise_totalrecovery), decimalFormat.format(pagewise_cash), decimalFormat.format(pagewise_cheque));
        pgno++;
        pagewise_epf = 0;
        pagewise_vpf = 0;
        pagewise_spf = 0;
        pagewise_deductions = 0;
        pagewise_totalrecovery = 0;
        pagewise_cash = 0;
        pagewise_cheque = 0;
        pw.println();
        pw.printf("%33s%12s%11s%10s%22s%18s%12s%12s", "SECTION TOTAL", decimalFormat.format(section_epf), decimalFormat.format(section_vpf), decimalFormat.format(section_spf), decimalFormat.format(section_deductions), decimalFormat.format(section_totalrecovery), decimalFormat.format(section_cash), decimalFormat.format(section_cheque));
        pw.println();
        section_epf = 0;
        section_vpf = 0;
        section_spf = 0;
        section_deductions = 0;
        section_totalrecovery = 0;
        section_cash = 0;
        section_cheque = 0;
        pw.print(equalline);
        pw.println();
        pw.printf("%33s%12s%11s%10s%22s%18s%12s%12s", "GRAND TOTAL", decimalFormat.format(grand_epf), decimalFormat.format(grand_vpf), decimalFormat.format(grand_spf), decimalFormat.format(grand_deductions), decimalFormat.format(grand_totalrecovery), decimalFormat.format(grand_cash), decimalFormat.format(grand_cheque));
        pw.println();
        pw.print(equalline);
        pw.write(FORM_FEED);
        pw.flush();
        pw.close();
    }

    public void getDeductionLedgerPrintWriter(PaySlipModel psm, String filePath) {
//        System.out.println("****************** DeductionLedgerReport class getDeductionLedgerPrintWriter method is calling **************************");
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            char[] horizontalsign = new char[132];
            char[] horizontalline = new char[132];
            char[] equalline = new char[132];
            for (int i = 0; i < 132; i++) {
                horizontalsign[i] = '~';
            }
            for (int i = 0; i < 132; i++) {
                horizontalline[i] = '-';
            }
            for (int i = 0; i < 132; i++) {
                equalline[i] = '=';
            }
            String sectionname = psm.getSectionname();

            if (Integer.valueOf(psm.getSlipno()) == 1) {
                lineno = 1;
                grand_epf = 0;
                grand_vpf = 0;
                grand_spf = 0;
                grand_deductions = 0;
                grand_totalrecovery = 0;
                grand_cash = 0;
                grand_cheque = 0;

            }

            if (lineno == 1) {
                secname = psm.getSectionname();
                pgno = 1;
                header(pw, psm);
            }
            if (!secname.equals(psm.getSectionname())) {
                SectionPrint(pw);
                header(pw, psm);
                secname = psm.getSectionname();
                lineno = 1;
            }

            if (lineno == 41) {
                PagewisePrint(pw);
                header(pw, psm);
                lineno = 1;
            }

            if (psm.isProcess()) {
                model = psm.getTotalrecovery().get(0);
                if (model.getCurrentinstallment() == null || model.getCurrentinstallment().length() == 0 || model.getTotalinstallment() == null || model.getTotalinstallment().length() == 0) {
                    pw.printf("%3s%8s%23s%11s%11s%10s%11s%11s%18s%12s%12s", psm.getSlipno(), (psm.getEmpno() != null) ? psm.getEmpno() : "", psm.getEmployeename(), (psm.getSectionname().equals("DEPUTATION") || psm.getSectionname().equals("OFFICER")) ? psm.getGpf() : psm.getEpf(), psm.getVpf(), psm.getSpf(), model.getDeductionname(), model.getDeductionamount(), psm.getTotaldeductions(), psm.getCashamount(), psm.getChequeamount());
                } else {
                    pw.printf("%3s%8s%23s%11s%11s%10s%11s%11s%s%2s%s%2s%s%11s%12s%12s", psm.getSlipno(), (psm.getEmpno() != null) ? psm.getEmpno() : "", psm.getEmployeename(), (psm.getSectionname().equals("DEPUTATION") || psm.getSectionname().equals("OFFICER")) ? psm.getGpf() : psm.getEpf(), psm.getVpf(), psm.getSpf(), model.getDeductionname(), model.getDeductionamount(), "(", model.getCurrentinstallment(), "/", model.getTotalinstallment(), ")", psm.getTotaldeductions(), psm.getCashamount(), psm.getChequeamount());
                }
            } else {
                pw.printf("%3s%8s%23s%11s%55s%-20s", psm.getSlipno(), (psm.getEmpno() != null) ? psm.getEmpno() : "", psm.getEmployeename(), (psm.getSectionname().equals("DEPUTATION") || psm.getSectionname().equals("OFFICER")) ? psm.getGpf() : psm.getEpf(), "PAY NOT CLAIM - ", psm.getRemarks());
            }

//            model = psm.getTotalrecovery().get(0);
//
//            if (model.getCurrentinstallment() == null || model.getCurrentinstallment().length() == 0 || model.getTotalinstallment() == null || model.getTotalinstallment().length() == 0) {
//                pw.printf("%3s%8s%23s%11s%11s%10s%11s%11s%18s%12s%12s", psm.getSlipno(), (psm.getEmpno() != null) ? psm.getEmpno() : "", psm.getEmployeename(), (psm.getSectionname().equals("DEPUTATION") || psm.getSectionname().equals("OFFICER")) ? psm.getGpf() : psm.getEpf(), psm.getVpf(), psm.getSpf(), model.getDeductionname(), model.getDeductionamount(), psm.getTotaldeductions(), psm.getCashamount(), psm.getChequeamount());
//            } else {
//                pw.printf("%3s%8s%23s%11s%11s%10s%11s%11s%s%2s%s%2s%s%11s%12s%12s", psm.getSlipno(), (psm.getEmpno() != null) ? psm.getEmpno() : "", psm.getEmployeename(), (psm.getSectionname().equals("DEPUTATION") || psm.getSectionname().equals("OFFICER")) ? psm.getGpf() : psm.getEpf(), psm.getVpf(), psm.getSpf(), model.getDeductionname(), model.getDeductionamount(), "(", model.getCurrentinstallment(), "/", model.getTotalinstallment(), ")", psm.getTotaldeductions(), psm.getCashamount(), psm.getChequeamount());
//            }

            if (psm.getEpf() == null || psm.getEpf().equals("")) {
                section_epf += 0;
                pagewise_epf += 0;
                grand_epf += 0;
            } else {
                section_epf += Double.valueOf(psm.getEpf());
                pagewise_epf += Double.valueOf(psm.getEpf());
                grand_epf += Double.valueOf(psm.getEpf());
            }

            if (psm.getVpf() == null || psm.getVpf().equals("")) {
                section_vpf += 0;
                pagewise_vpf += 0;
                grand_vpf += 0;
            } else {
                section_vpf += Double.valueOf(psm.getVpf());
                pagewise_vpf += Double.valueOf(psm.getVpf());
                grand_vpf += Double.valueOf(psm.getVpf());
            }

            if (psm.getSpf() == null || psm.getSpf().equals("")) {
                section_spf += 0;
                pagewise_spf += 0;
                grand_spf += 0;
            } else {
                section_spf += Double.valueOf(psm.getSpf());
                pagewise_spf += Double.valueOf(psm.getSpf());
                grand_spf += Double.valueOf(psm.getSpf());
            }

            if (model.getDeductionamount() == null || model.getDeductionamount().equals("")) {
                section_deductions += 0;
                pagewise_deductions += 0;
                grand_deductions += 0;
            } else {
                section_deductions += Double.valueOf(model.getDeductionamount());
                pagewise_deductions += Double.valueOf(model.getDeductionamount());
                grand_deductions += Double.valueOf(model.getDeductionamount());
            }

            if (psm.getTotaldeductions() == null || psm.getTotaldeductions().equals("")) {
                section_totalrecovery += 0;
                pagewise_totalrecovery += 0;
                grand_totalrecovery += 0;
            } else {
                section_totalrecovery += Double.valueOf(psm.getTotaldeductions());
                pagewise_totalrecovery += Double.valueOf(psm.getTotaldeductions());
                grand_totalrecovery += Double.valueOf(psm.getTotaldeductions());
            }

            if (psm.getCashamount() == null || psm.getCashamount().equals("")) {
                section_cash += 0;
                pagewise_cash += 0;
                grand_cash += 0;
            } else {
                section_cash += Double.valueOf(psm.getCashamount());
                pagewise_cash += Double.valueOf(psm.getCashamount());
                grand_cash += Double.valueOf(psm.getCashamount());
            }

            if (psm.getChequeamount() == null || psm.getChequeamount().equals("")) {
                section_cheque += 0;
                pagewise_cheque += 0;
                grand_cheque += 0;
            } else {
                section_cheque += Double.valueOf(psm.getChequeamount());
                pagewise_cheque += Double.valueOf(psm.getChequeamount());
                grand_cheque += Double.valueOf(psm.getChequeamount());
            }
            pw.println();
            lineno++;
            if (lineno == 41) {
                PagewisePrint(pw);
                header(pw, psm);
                lineno = 1;
            }
            model = psm.getTotalrecovery().get(1);
            if (model.getCurrentinstallment() == null || model.getCurrentinstallment().length() == 0 || model.getTotalinstallment() == null || model.getTotalinstallment().length() == 0) {
                pw.printf("%4s%-10s%20s%43s%11s", "", psm.getPfno(), (psm.getDesignation() != null) ? psm.getDesignation() : "", model.getDeductionname(), model.getDeductionamount());
            } else {
                pw.printf("%4s%-10s%20s%43s%11s%s%2s%s%2s%s", "", psm.getPfno(), (psm.getDesignation() != null) ? psm.getDesignation() : "", model.getDeductionname(), model.getDeductionamount(), "(", model.getCurrentinstallment(), "/", model.getTotalinstallment(), ")");
            }
            pw.println();
            lineno++;
            if (lineno == 41) {
                PagewisePrint(pw);
                header(pw, psm);
                lineno = 1;
            }
            model = psm.getTotalrecovery().get(2);
            if (model.getCurrentinstallment() == null || model.getCurrentinstallment().length() == 0 || model.getTotalinstallment() == null || model.getTotalinstallment().length() == 0) {
                pw.printf("%15s%62s%11s", (psm.getBankaccountno().equals("NULL") || psm.getBankaccountno().equals("Null")) ? "" : psm.getBankaccountno(), model.getDeductionname(), model.getDeductionamount());
            } else {
                pw.printf("%15s%62s%11s%s%2s%s%2s%s", (psm.getBankaccountno().equals("NULL") || psm.getBankaccountno().equals("Null")) ? "" : psm.getBankaccountno(), model.getDeductionname(), model.getDeductionamount(), "(", model.getCurrentinstallment(), "/", model.getTotalinstallment(), ")");
            }
            pw.println();
            lineno++;
            if (lineno == 41) {
                PagewisePrint(pw);
                header(pw, psm);
                lineno = 1;
            }
            for (int i = 3; i < psm.getTotalrecovery().size(); i++) {
                model = psm.getTotalrecovery().get(i);
                if (model.getCurrentinstallment() == null || model.getCurrentinstallment().length() == 0 || model.getTotalinstallment() == null || model.getTotalinstallment().length() == 0) {
                    pw.printf("%77s%11s", model.getDeductionname(), model.getDeductionamount());
                } else {
                    pw.printf("%77s%11s%s%2s%s%2s%s", model.getDeductionname(), model.getDeductionamount(), "(", model.getCurrentinstallment(), "/", model.getTotalinstallment(), ")");
                }
                pw.println();
                lineno++;
                if (lineno == 41) {
                    PagewisePrint(pw);
                    header(pw, psm);
                    lineno = 1;
                }
            }
            if ((lineno + 1) == 41) {
            } else {
                pw.println();
                lineno++;
            }
//            if (Integer.valueOf(psm.getSlipno()) == psm.getPrintingrecordsize()) {
//                GrandPrint(pw);
//            }
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
