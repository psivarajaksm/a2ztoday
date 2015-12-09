/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.supplementary;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.valueobjects.IncrementArrearModel;
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

public class SupplementaryIncrementArrearAcquitance {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] horizontalline = new char[80];
    private char[] equalline = new char[80];
    private DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private double totalpayment = 0;

    public SupplementaryIncrementArrearAcquitance() {
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

    public void header(IncrementArrearModel iam, PrintWriter pw) {
        pw.println();
        pw.printf("%11s%-39s%s", "", "TAMIL NADU CIVIL SUPPLIES CORPORATION,", "CHENNAI");
        pw.println();
        pw.printf("%7s%s", "", "PAY ACQUITANCE SLIP (SUPPLEMENTARY BILL) FOR INCREMENT ARREAR");
        pw.println();
        pw.printf("%-9s%-55s%s", "REGION :", iam.getRegion(), iam.getPaymentdate());
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%s", " SNO  EMP NO       EMP_NAME                        DESIG           PAYMENT");
        pw.println();
        pw.print(horizontalline);
    }

    public void total(IncrementArrearModel iam, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.println();
            pw.printf("%18s%-47s%10s", "", "GRAND TOTAL", decimalFormat.format(totalpayment));
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            totalpayment = 0;
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void getIncrementArrearAcquitancePrintWriter(IncrementArrearModel iam, String filePath) {
//        System.out.println("***************** SupplementaryAbstractReport class getSalaryAbstractPrintWriter method is calling *******************");
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (iam.getSlipno() == 1) {
                header(iam, pw);
            }
            if (iam.getSlipno() % 6 == 0) {
                pw.println();
                pw.print(FORM_FEED);
                header(iam, pw);
            }
            pw.println();
            pw.printf("%3s%2s%-12s%s%-30s%s%-15s%s%10s", iam.getSlipno(), ". ", iam.getEpfno(), " ", iam.getName(), " ", (iam.getDesignation() == null) ? "" : iam.getDesignation(), " ", iam.getPayment());
            totalpayment += Double.valueOf(iam.getPayment());
            pw.println();
            pw.printf("%49s%-20s"," ", (iam.getBankno() == null) ? "" : iam.getBankno());
            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.print(horizontalsign);
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
