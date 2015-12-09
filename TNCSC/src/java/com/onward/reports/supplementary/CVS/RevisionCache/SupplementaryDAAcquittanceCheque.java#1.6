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

public class SupplementaryDAAcquittanceCheque {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[83];
    private char[] horizontalline = new char[83];
    private char[] equalline = new char[83];
    private int pgno = 0;
    private String section = null;
    private double sectiontotal = 0;
    private double grandtotal = 0;
    private int lineno;
    DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public SupplementaryDAAcquittanceCheque() {
        for (int i = 0; i < 83; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 83; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 83; i++) {
            equalline[i] = '=';
        }
    }

    public void DABillRepor(PrintWriter pw, PaySlipModel psm) {
    }

    public void sectiontotal(PrintWriter pw) {
        pw.print(horizontalline);
        pw.println();
        lineno++;
        pw.printf("%28s%-37s%5s%13s", "", section, "TOTAL", decimalFormat.format(sectiontotal));
        sectiontotal = 0;
        pw.println();
        lineno++;
        pw.print(equalline);
        pw.println();
        lineno++;
    }

    public void grandtotal(String filePath) {
        PrintWriter pw = null;
        File file = new File(filePath);
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%28s%-37s%5s%13s", "", section, "TOTAL", decimalFormat.format(sectiontotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%70s%13s", "GRAND TOTAL", decimalFormat.format(grandtotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.flush();
        pw.close();
        sectiontotal = 0;
        grandtotal = 0;
    }

    public void header(PrintWriter pw, DaArrearModel dam) {
        pw.println();
        pw.printf("%48s%-23s", "TAMIL NADU CIVIL SUPPLIES CORPORTION, ", dam.getRegion());
        pw.println();
        pw.printf("%58s", "ACQUITTANCE FOR DA ARREAR BILL - CHEQUE PAYMENT");
        pw.println();
        pw.printf("%-11s%-25s", "BATCH.NO : ",dam.getBatchno());
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%s", "SNO.  EPFNO        EMPLOYEE NAME                          CHEQUE NO        AMOUNT");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void DAAcquittanceChequeReportPrint(DaArrearModel dam, String filePath) {
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
                section = dam.getSection();
                lineno = 1;
            }
            if (!section.equals(dam.getSection())) {
                sectiontotal(pw);
                if (lineno >= 48) {
                    pw.write(FORM_FEED);
                    header(pw, dam);
                    lineno = 1;
                }
                section = dam.getSection();
            }
            if (lineno >= 48) {
                pw.write(FORM_FEED);
                header(pw, dam);
                lineno = 1;
            }
            pw.printf("%3s%2s%-12s%s%-30s%19s%16s", dam.getSlipno(), ". ", dam.getEpfno(), "", dam.getEmployeename(), dam.getChequeno(), dam.getNet());
            pw.println();
            lineno++;
            pw.println();
            lineno++;
            if (lineno >= 48) {
                pw.write(FORM_FEED);
                header(pw, dam);
                lineno = 1;
            }
            sectiontotal += Double.valueOf(dam.getNet());
            grandtotal += Double.valueOf(dam.getNet());
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
