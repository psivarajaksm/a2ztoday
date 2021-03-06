/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.supplementary;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.reports.regular.*;
import com.onward.valueobjects.DaArrearModel;
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

public class SupplementaryDAAcquittanceBank {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[105];
    private char[] horizontalline = new char[105];
    private char[] equalline = new char[105];
    private int pgno = 0;
    private String sectionname;
    private double daarrear = 0;
    private double epf = 0;
    private double net = 0;
    private double sectiondaarrear = 0;
    private double sectionepf = 0;
    private double sectionnet = 0;
    private int lineno = 1;
    DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public SupplementaryDAAcquittanceBank() {
        for (int i = 0; i < 105; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 105; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 105; i++) {
            equalline[i] = '=';
        }
    }

    public void sectiontotal(PrintWriter pw) {
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%12s%-49s%9s%s%9s%s%9s", "", "SECTION TOTAL", decimalFormat.format(sectiondaarrear), " ", decimalFormat.format(sectionepf), " ", decimalFormat.format(sectionnet));
        sectiondaarrear = 0;
        sectionepf = 0;
        sectionnet = 0;
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.print(FORM_FEED);
        lineno = 1;

    }

    public void grandtotal(String filePath) {
        System.out.println("********************* grandtotal is calling ****************************");
        PrintWriter pw = null;
        File file = new File(filePath);
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%12s%-49s%9s%s%9s%s%9s", "", "SECTION TOTAL", decimalFormat.format(sectiondaarrear), " ", decimalFormat.format(sectionepf), " ", decimalFormat.format(sectionnet));
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%14s%-47s%9s%s%9s%s%9s", "", "GRAND TOTAL", decimalFormat.format(daarrear), " ", decimalFormat.format(epf), " ", decimalFormat.format(net));
        sectiondaarrear = 0;
        sectionepf = 0;
        sectionnet = 0;
        daarrear = 0;
        epf = 0;
        net = 0;
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.print(FORM_FEED);
        pw.flush();
        pw.close();

    }

    public void header(PrintWriter pw, DaArrearModel dam) {
        pw.println();
        pw.printf("%6s%-39s%s", "      ", "TAMIL NADU CIVIL SUPPLIES CORPORATIO., ", dam.getRegion());
        pw.println();
        pw.printf("%15s%-26s%6s%4s%6s%5s", "", "ACQUITTANCE FOR DA ARREAR ", dam.getStartmonth(), " TO ", dam.getEndmonth(), dam.getPaymenttype());
        pw.println();
//        pw.printf("%22s%s", " NAME OF THE SECTION: ", dam.getSection());
        pw.printf("%-23s%-50s%-11s%-25s", " NAME OF THE SECTION : ", dam.getSection(), "BATCH.NO : ", dam.getBatchno());
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%s", " S.NO EMP.NO.      NAME AND DESIGNATION          DESIGNATION   DA ARR      EPF      NET");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void DAAcquittanceBankReportPrint(DaArrearModel dam, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (dam.getSlipno() == 1) {
                header(pw, dam);
                sectionname = dam.getSection();
            }
            if (!dam.getSection().equals(sectionname)) {
                sectiontotal(pw);
                header(pw, dam);
                sectionname = dam.getSection();
            }

            if (lineno >= 40) {
                pw.print(FORM_FEED);
                header(pw, dam);
                lineno = 1;
            }
            String bankaccountno = "";
            if (dam.getBankaccountno().equals("Null") || dam.getBankaccountno().equals("NULL")) {
                bankaccountno = "";
            } else {
                bankaccountno = dam.getBankaccountno();
            }
            pw.printf("%5s%s%-12s%s%-30s%s%-10s%s%9s%s%9s%s%9s%s%13s", dam.getSlipno(), " ", dam.getEpfno(), " ", dam.getEmployeename(), " ", dam.getDesignation(), " ", dam.getDaarrear(), " ", dam.getEpf(), " ", dam.getNet(), " ", bankaccountno);
            daarrear += Double.valueOf(dam.getDaarrear());
            epf += Double.valueOf(dam.getEpf());
            net += Double.valueOf(dam.getNet());

            sectiondaarrear += Double.valueOf(dam.getDaarrear());
            sectionepf += Double.valueOf(dam.getEpf());
            sectionnet += Double.valueOf(dam.getNet());

            pw.println();
            lineno++;
            pw.println();
            lineno++;
            pw.println();
            lineno++;
            pw.println();
            lineno++;

            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
