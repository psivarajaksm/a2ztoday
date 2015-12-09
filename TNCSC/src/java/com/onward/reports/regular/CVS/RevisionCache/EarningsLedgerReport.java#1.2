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

public class EarningsLedgerReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[132];
    private char[] horizontalline = new char[132];
    private char[] equalline = new char[132];
    private char[] equalline1 = new char[80];
    private char[] multiline = new char[80];
    private int lineno = 1;
    private int pageno = 0;
    private String checksectionname = "";
    private double pagewise_basicpay = 0;
    private double pagewise_splpay = 0;
    private double pagewise_da = 0;
    private double pagewise_hra = 0;
    private double pagewise_cca = 0;
    private double pagewise_earnings = 0;
    private double pagewise_grosssalry = 0;
    private double sec_basicpay = 0;
    private double sec_splpay = 0;
    private double sec_da = 0;
    private double sec_hra = 0;
    private double sec_cca = 0;
    private double sec_earnings = 0;
    private double sec_grosssalry = 0;
    private double grant_basicpay = 0;
    private double grant_splpay = 0;
    private double grant_da = 0;
    private double grant_hra = 0;
    private double grant_cca = 0;
    private double grant_earnings = 0;
    private double grant_grosssalry = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int sno = 1;

    public EarningsLedgerReport() {
        for (int i = 0; i < 132; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 132; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 132; i++) {
            equalline[i] = '=';
        }
        for (int i = 0; i < 80; i++) {
            equalline1[i] = '=';
        }
        for (int i = 0; i < 80; i++) {
            multiline[i] = '*';
        }

    }

    public void GrandTotal(String filePath) {
        PrintWriter pw = null;
        File file = new File(filePath);
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%17s%3s%15s%18s%11s%12s%11s%11s%21s%12s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
        pageno++;
        pagewise_basicpay = 0;
        pagewise_splpay = 0;
        pagewise_da = 0;
        pagewise_hra = 0;
        pagewise_cca = 0;
        pagewise_earnings = 0;
        pagewise_grosssalry = 0;
        pw.println();
        pw.printf("%35s%18s%11s%12s%11s%11s%21s%12s", "SECTION TOTAL", decimalFormat.format(sec_basicpay), decimalFormat.format(sec_splpay), decimalFormat.format(sec_da), decimalFormat.format(sec_hra), decimalFormat.format(sec_cca), decimalFormat.format(sec_earnings), decimalFormat.format(sec_grosssalry));
        sec_basicpay = 0;
        sec_splpay = 0;
        sec_da = 0;
        sec_hra = 0;
        sec_cca = 0;
        sec_earnings = 0;
        sec_grosssalry = 0;
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%35s%18s%11s%12s%11s%11s%21s%12s", "GRAND TOTAL", decimalFormat.format(grant_basicpay), decimalFormat.format(grant_splpay), decimalFormat.format(grant_da), decimalFormat.format(grant_hra), decimalFormat.format(grant_cca), decimalFormat.format(grant_earnings), decimalFormat.format(grant_grosssalry));
        pw.println();
        pw.print(equalline);
        pw.print(FORM_FEED);
        pw.flush();
        pw.close();
    }

    public void getEarningsLedgerPrintWriter(PaySlipModel psm, String filePath) {
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
                lineno = 1;
            }

            String sectionname = psm.getSectionname();
            if (lineno == 1) {
                checksectionname = psm.getSectionname();
                pageno = 1;
                pw.println();
                pw.write(LARGEFONT);
                pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
                pw.println();
                pw.write(BOLD);
                pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
                pw.write(RELEASE);
                pw.println();
                pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
                pw.println();
                pw.print(horizontalsign);
                pw.println();
//                pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
                pw.printf("%-132s", "SNO EMP.NO      EMP. NAME & DESIGNATION         PAY    SPL. PAY        D.A       H.R.A      C.C.A    OTHER ALLOWANCES   GROSS SALARY");
                pw.println();
                pw.print(horizontalsign);
                pw.println();
            }
            if (!checksectionname.equals(psm.getSectionname())) {
                pw.print(horizontalline);
                pw.println();
                pw.printf("%17s%3s%15s%18s%11s%12s%11s%11s%21s%13s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
                pageno++;
                pagewise_basicpay = 0;
                pagewise_splpay = 0;
                pagewise_da = 0;
                pagewise_hra = 0;
                pagewise_cca = 0;
                pagewise_earnings = 0;
                pagewise_grosssalry = 0;
                pw.println();
                pw.printf("%35s%18s%11s%12s%11s%11s%21s%13s", "SECTION TOTAL", decimalFormat.format(sec_basicpay), decimalFormat.format(sec_splpay), decimalFormat.format(sec_da), decimalFormat.format(sec_hra), decimalFormat.format(sec_cca), decimalFormat.format(sec_earnings), decimalFormat.format(sec_grosssalry));
                sec_basicpay = 0;
                sec_splpay = 0;
                sec_da = 0;
                sec_hra = 0;
                sec_cca = 0;
                sec_earnings = 0;
                sec_grosssalry = 0;
                pw.println();
                pw.print(equalline);
                pw.println();
                pw.write(FORM_FEED);
                checksectionname = psm.getSectionname();
                pw.println();
                pw.write(LARGEFONT);
                pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
                pw.println();
                pw.write(BOLD);
                pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
                pw.write(RELEASE);
                pw.println();
                pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
                pw.println();
                pw.print(horizontalsign);
                pw.println();
//                pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
                pw.printf("%-132s", "SNO EMP.NO      EMP. NAME & DESIGNATION         PAY    SPL. PAY        D.A       H.R.A      C.C.A    OTHER ALLOWANCES   GROSS SALARY");

                pw.println();
                pw.print(horizontalsign);
                pw.println();
                lineno = 1;
            }

            model = psm.getOtherallowance().get(0);
            if (lineno == 55) {
                pw.print(horizontalline);
                pw.println();
                pw.printf("%17s%3s%15s%18s%11s%12s%11s%11s%21s%13s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
                pageno++;
                pagewise_basicpay = 0;
                pagewise_splpay = 0;
                pagewise_da = 0;
                pagewise_hra = 0;
                pagewise_cca = 0;
                pagewise_earnings = 0;
                pagewise_grosssalry = 0;
                pw.println();
                pw.print(equalline);
                pw.println();
                pw.write(FORM_FEED);
                pw.println();
                pw.write(LARGEFONT);
                pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
                pw.println();
                pw.write(BOLD);
                pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
                pw.write(RELEASE);
                pw.println();
                pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
                pw.println();
                pw.print(horizontalsign);
                pw.println();
//                pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
                pw.printf("%-132s", "SNO EMP.NO      EMP. NAME & DESIGNATION         PAY    SPL. PAY        D.A       H.R.A      C.C.A    OTHER ALLOWANCES   GROSS SALARY");

                pw.println();
                pw.print(horizontalsign);
                pw.println();
                lineno = 1;
            }
            if (psm.isProcess()) {
                pw.printf("%-4s%-12s%-25s%12s%11s%12s%11s%11s%s%-11s%10s%12s", psm.getSlipno(), psm.getEmpno(), psm.getEmployeename(), psm.getBasicpay(), psm.getSplpay(), psm.getDa(), psm.getHra(), psm.getCca(), " ", model.getEarningsname(), model.getEarningsamount(), psm.getGrosssalary());
            } else {
//                pw.printf("%-4s%-12s%-25s%s%-15s%s", psm.getSlipno(), psm.getEmpno(), psm.getEmployeename(), "**********************************", psm.getRemarks(), "**********************************");
                pw.printf("%-4s%-12s%-25s%58s%-15s", psm.getSlipno(), psm.getEmpno(), psm.getEmployeename(), "PAY NOT CLAIM - ", psm.getRemarks());
            }
            if (psm.getBasicpay() == null || psm.getBasicpay().equals("")) {
                sec_basicpay += 0;
                pagewise_basicpay += 0;
                grant_basicpay += 0;
            } else {
                sec_basicpay += Double.valueOf(psm.getBasicpay());
                pagewise_basicpay += Double.valueOf(psm.getBasicpay());
                grant_basicpay += Double.valueOf(psm.getBasicpay());
            }

            if (psm.getSplpay() == null || psm.getSplpay().equals("")) {
                sec_splpay += 0;
                pagewise_splpay += 0;
                grant_splpay += 0;
            } else {
                sec_splpay += Double.valueOf(psm.getSplpay());
                pagewise_splpay += Double.valueOf(psm.getSplpay());
                grant_splpay += Double.valueOf(psm.getSplpay());
            }

            if (psm.getDa() == null || psm.getDa().equals("")) {
                sec_da += 0;
                pagewise_da += 0;
                grant_da += 0;
            } else {
                sec_da += Double.valueOf(psm.getDa());
                pagewise_da += Double.valueOf(psm.getDa());
                grant_da += Double.valueOf(psm.getDa());
            }

            if (psm.getHra() == null || psm.getHra().equals("")) {
                sec_hra += 0;
                pagewise_hra += 0;
                grant_hra += 0;
            } else {
                sec_hra += Double.valueOf(psm.getHra());
                pagewise_hra += Double.valueOf(psm.getHra());
                grant_hra += Double.valueOf(psm.getHra());
            }

            if (psm.getCca() == null || psm.getCca().equals("")) {
                sec_cca += 0;
                pagewise_cca += 0;
                grant_cca += 0;
            } else {
                sec_cca += Double.valueOf(psm.getCca());
                pagewise_cca += Double.valueOf(psm.getCca());
                grant_cca += Double.valueOf(psm.getCca());
            }

            if (model.getEarningsamount() == null || model.getEarningsamount().equals("")) {
                sec_earnings += 0;
                pagewise_earnings += 0;
                grant_earnings += 0;
            } else {
                sec_earnings += Double.valueOf(model.getEarningsamount());
                pagewise_earnings += Double.valueOf(model.getEarningsamount());
                grant_earnings += Double.valueOf(model.getEarningsamount());
            }

            if (psm.getGrosssalary() == null || psm.getGrosssalary().equals("")) {
                sec_grosssalry += 0;
                pagewise_grosssalry += 0;
                grant_grosssalry += 0;
            } else {
                sec_grosssalry += Double.valueOf(psm.getGrosssalary());
                pagewise_grosssalry += Double.valueOf(psm.getGrosssalary());
                grant_grosssalry += Double.valueOf(psm.getGrosssalary());
            }
            sno++;
            lineno++;
            pw.println();

            model = psm.getOtherallowance().get(1);
            if (lineno == 55) {
                pw.print(horizontalline);
                pw.println();
                pw.printf("%17s%3s%15s%18s%11s%12s%11s%11s%21s%12s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
                pageno++;
                pagewise_basicpay = 0;
                pagewise_splpay = 0;
                pagewise_da = 0;
                pagewise_hra = 0;
                pagewise_cca = 0;
                pagewise_earnings = 0;
                pagewise_grosssalry = 0;
                pw.println();
                pw.print(equalline);
                pw.println();
                pw.write(FORM_FEED);
                pw.println();
                pw.write(LARGEFONT);
                pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
                pw.println();
                pw.write(BOLD);
                pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
                pw.write(RELEASE);
                pw.println();
                pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
                pw.println();
                pw.print(horizontalsign);
                pw.println();
//                pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
                pw.printf("%-132s", "SNO EMP.NO      EMP. NAME & DESIGNATION         PAY    SPL. PAY        D.A       H.R.A      C.C.A    OTHER ALLOWANCES   GROSS SALARY");

                pw.println();
                pw.print(horizontalsign);
                pw.println();
                lineno = 1;
            }
//            pw.printf("%4s%-11s%25s%57s%11s%10s", "",psm.getPfno(), psm.getDesignation(), "", model.getEarningsname(), model.getEarningsamount());
            pw.printf("%4s%-12s%-83s%-11s%10s", "", psm.getPfno(), psm.getDesignation(), model.getEarningsname(), model.getEarningsamount());
            if (model.getEarningsamount() == null || model.getEarningsamount().equals("")) {
                sec_earnings += 0;
                pagewise_earnings += 0;
                grant_earnings += 0;
            } else {
                sec_earnings += Double.valueOf(model.getEarningsamount());
                pagewise_earnings += Double.valueOf(model.getEarningsamount());
                grant_earnings += Double.valueOf(model.getEarningsamount());
            }
            lineno++;
            pw.println();

            model = psm.getOtherallowance().get(2);
            if (lineno == 55) {
                pw.print(horizontalline);
                pw.println();
                pw.printf("%17s%3s%15s%18s%11s%12s%11s%11s%21s%12s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
                pageno++;
                pagewise_basicpay = 0;
                pagewise_splpay = 0;
                pagewise_da = 0;
                pagewise_hra = 0;
                pagewise_cca = 0;
                pagewise_earnings = 0;
                pagewise_grosssalry = 0;
                pw.println();
                pw.print(equalline);
                pw.println();
                pw.write(FORM_FEED);
                pw.println();
                pw.write(LARGEFONT);
                pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
                pw.println();
                pw.write(BOLD);
                pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
                pw.write(RELEASE);
                pw.println();
                pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
                pw.println();
                pw.print(horizontalsign);
                pw.println();
//                pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
                pw.printf("%-132s", "SNO EMP.NO      EMP. NAME & DESIGNATION         PAY    SPL. PAY        D.A       H.R.A      C.C.A    OTHER ALLOWANCES   GROSS SALARY");

                pw.println();
                pw.print(horizontalsign);
                pw.println();
                lineno = 1;
            }
            pw.printf("%15s%82s%11s%10s", psm.getBankaccountno(), "", model.getEarningsname(), model.getEarningsamount());
            if (model.getEarningsamount() == null || model.getEarningsamount().equals("")) {
                sec_earnings += 0;
                pagewise_earnings += 0;
                grant_earnings += 0;
            } else {
                sec_earnings += Double.valueOf(model.getEarningsamount());
                pagewise_earnings += Double.valueOf(model.getEarningsamount());
                grant_earnings += Double.valueOf(model.getEarningsamount());
            }
            lineno++;
            pw.println();
            if (psm.getOtherallowance().size() == 3) {
                pw.println();
                lineno++;

                pw.println();
                lineno++;

                pw.println();
                lineno++;
            }

            for (int i = 3; i < psm.getOtherallowance().size(); i++) {
                model = psm.getOtherallowance().get(i);
                if (lineno == 55) {
                    pw.print(horizontalline);
                    pw.println();
                    pw.printf("%17s%3s%15s%18s%11s%12s%11s%11s%21s%12s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
                    pageno++;
                    pagewise_basicpay = 0;
                    pagewise_splpay = 0;
                    pagewise_da = 0;
                    pagewise_hra = 0;
                    pagewise_cca = 0;
                    pagewise_earnings = 0;
                    pagewise_grosssalry = 0;
                    pw.println();
                    pw.print(equalline);
                    pw.println();
                    pw.write(FORM_FEED);
                    pw.println();
                    pw.write(LARGEFONT);
                    pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
                    pw.println();
                    pw.write(BOLD);
                    pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
                    pw.write(RELEASE);
                    pw.println();
                    pw.printf("%-9s%-35s%-10s%-20s", "REGION :", psm.getBranch(), "SECTION :", psm.getSectionname());
                    pw.println();
                    pw.print(horizontalsign);
                    pw.println();
//                    pw.printf("%-9s%-8s%-32s%-6s%-16s%-9s%-11s%-10s%-18s%-12s", "SNO", "EMP.NO", "EMP. NAME & DESIGNATION", "PAY", "SPL. PAY", "D.A.", "H.R.A.", "C.C.A.", "OTHER ALLOWANCES.", "GROSS SALARY");
                    pw.printf("%-132s", "SNO EMP.NO      EMP. NAME & DESIGNATION         PAY    SPL. PAY        D.A       H.R.A      C.C.A    OTHER ALLOWANCES   GROSS SALARY");

                    pw.println();
                    pw.print(horizontalsign);
                    pw.println();
                    lineno = 1;
                }
                pw.printf("%97s%11s%10s", "", model.getEarningsname(), model.getEarningsamount());
                if (model.getEarningsamount() == null || model.getEarningsamount().equals("")) {
                    sec_earnings += 0;
                    pagewise_earnings += 0;
                    grant_earnings += 0;
                } else {
                    sec_earnings += Double.valueOf(model.getEarningsamount());
                    pagewise_earnings += Double.valueOf(model.getEarningsamount());
                    grant_earnings += Double.valueOf(model.getEarningsamount());
                }
                lineno++;
                pw.println();
            }
//            if (Integer.valueOf(psm.getSlipno()) == psm.getPrintingrecordsize()) {
//                pw.print(horizontalline);
//                pw.println();
//                pw.printf("%17s%3s%15s%18s%11s%12s%11s%11s%21s%12s", "PAGE NO :", pageno, "PAGE TOTAL", decimalFormat.format(pagewise_basicpay), decimalFormat.format(pagewise_splpay), decimalFormat.format(pagewise_da), decimalFormat.format(pagewise_hra), decimalFormat.format(pagewise_cca), decimalFormat.format(pagewise_earnings), decimalFormat.format(pagewise_grosssalry));
//                pageno++;
//                pagewise_basicpay = 0;
//                pagewise_splpay = 0;
//                pagewise_da = 0;
//                pagewise_hra = 0;
//                pagewise_cca = 0;
//                pagewise_earnings = 0;
//                pagewise_grosssalry = 0;
//                pw.println();
//                pw.printf("%35s%18s%11s%12s%11s%11s%21s%12s", "SECTION TOTAL", decimalFormat.format(sec_basicpay), decimalFormat.format(sec_splpay), decimalFormat.format(sec_da), decimalFormat.format(sec_hra), decimalFormat.format(sec_cca), decimalFormat.format(sec_earnings), decimalFormat.format(sec_grosssalry));
//                sec_basicpay = 0;
//                sec_splpay = 0;
//                sec_da = 0;
//                sec_hra = 0;
//                sec_cca = 0;
//                sec_earnings = 0;
//                sec_grosssalry = 0;
//                pw.println();
//                pw.print(equalline);
//                pw.println();
//                pw.printf("%35s%18s%11s%12s%11s%11s%21s%12s", "GRAND TOTAL", decimalFormat.format(grant_basicpay), decimalFormat.format(grant_splpay), decimalFormat.format(grant_da), decimalFormat.format(grant_hra), decimalFormat.format(grant_cca), decimalFormat.format(grant_earnings), decimalFormat.format(grant_grosssalry));
//                pw.println();
//                pw.print(equalline);
//                pw.print(FORM_FEED);
//            }
            if (psm.getOtherallowance().size() > 3) {
                pw.println();
                lineno++;

                pw.println();
                lineno++;

                pw.println();
                lineno++;
            }
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getSalaryWithHeldPrintWriter(PaySlipModel psm, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/EarningsLedger.txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.write(LARGEFONT);
            pw.printf("%s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
            pw.println();
            pw.write(BOLD);
            pw.printf("%-33s%3s%3s%4s", "EARNINGS LEDGER FOR THE MONTH OF", psm.getPayslipmonth(), " - ", psm.getPayslipyear());
            pw.write(RELEASE);
            pw.println();
            pw.print(multiline);
            pw.println();
            pw.printf("%48s", "SALARY WITHHELD MEMBER'S LIST");
            pw.println();
            pw.print(multiline);
            pw.println();
            pw.printf("%-15s%-31s%-16s%7s", "SNO PF.NO", "EMPLOYEE NAME", "DESIGNATION", "SECTION");
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            pw.printf("%3s%s%-10s%s%-30s%s%-15s%s%-15s", "111", " ", psm.getPfno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psm.getSectionname());
            pw.println();
            pw.print(equalline1);
            pw.println();
            pw.printf("%16s%3s", "PAGE NO :", pageno);
            pw.println();
            pw.print(equalline1);
            pw.println();
            pw.write(FORM_FEED);

        } catch (Exception ex) {
        }
    }
}
