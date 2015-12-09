/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.accounts;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.valueobjects.AccountsModel;
import com.onward.valueobjects.AccountsSubModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jboss.management.j2ee.JVM;

public class PurchaseAbstractReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[125];
    private char[] equalline = new char[125];
    private char[] horizontalline = new char[125];
    private int lineno = 1;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int count = 0;
    private int pageno = 1;
    private long grantquantitytotal = 0;
    private double grantvaluetotal = 0;
    private double granttaxtotal = 0;
    private double granttottotal = 0;
    private double grantpercentagevaluetotal = 0;
    private double grantpercentagetaxtotal = 0;
    private double grantpercentagetottotal = 0;

    public PurchaseAbstractReport() {
        for (int i = 0; i < 125; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 125; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 125; i++) {
            equalline[i] = '=';
        }
    }

    public void Header(AccountsModel am, PrintWriter pw) {
        pw.println();
        pw.printf("%s", "   TAMIL NADU CIVIL SUPPLIES CORPORATION.,");
        pw.println();
        pw.printf("%55s%-5s", "   TAXABLE GOODS PURCHASED (ABSTRACT) DURING THE MONTH ", am.getAccountingmonthandyear());
        pw.println();
        pw.printf("%23s%-25s", "NAME OF THE REGION: ", am.getRegion());
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%s", "SNO SELLER NAME AND ADDRESS                                  QUANTITY         VALUE                TAX                TOTAL");
        pw.println();
        pw.print(horizontalline);
        pw.println();
    }

    public void GrandTotal(AccountsModel am, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                pw.println();
                pw.print(equalline);
                pw.println();
                pw.printf("%3s%s%-57s%s%-8s%14s%20s%20s", "", " ", "Grand Total", " ", grantquantitytotal, decimalFormat.format(grantvaluetotal), decimalFormat.format(granttaxtotal), decimalFormat.format(granttottotal));
                pw.println();
                pw.print(equalline);
                pw.println();
                grantquantitytotal = 0;
                grantvaluetotal = 0;
                granttaxtotal = 0;
                granttottotal = 0;
                pw.flush();
                pw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getPrintWriter(AccountsModel am, String filePath) {

        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (Integer.valueOf(am.getPageno()) == 1) {
                Header(am, pw);
            }
            if (lineno > 53) {
                pw.write(FORM_FEED);
                pw.println();
                Header(am, pw);
                lineno = 1;
            }

            pw.printf("%3s%s%-57s%s%-8s%14s%20s%20s", am.getPageno(), " ", am.getCompanyname(), " ", am.getQuantity(), am.getValue(), am.getTaxamount(), am.getTotalamount());
            pw.println();
            lineno++;
            pw.println();
            lineno++;

            if (lineno > 53) {
                pw.write(FORM_FEED);
                pw.println();
                Header(am, pw);
                lineno = 1;
            }

            grantquantitytotal += Double.valueOf(am.getQuantity());
            grantvaluetotal += Double.valueOf(am.getValue());
            granttaxtotal += Double.valueOf(am.getTaxamount());
            granttottotal += Double.valueOf(am.getTotalamount());

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void HeaderPercentage(AccountsModel am, PrintWriter pw) {
        pw.print(horizontalline);
        pw.println();
        pw.printf("%s", "     TAX PERCENTAGE              VALUE           TAX AMOUNT               TOTAL");
        pw.println();
        pw.print(horizontalline);
        pw.println();
    }

    public void GrandTotalPercentage(AccountsModel am, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                pw.println();
                pw.print(equalline);
                pw.println();
                pw.printf("%20s%19s%20s%20s", "GRAND TOTAL", decimalFormat.format(grantpercentagevaluetotal), decimalFormat.format(grantpercentagetaxtotal), decimalFormat.format(grantpercentagetottotal));
                pw.println();
                pw.print(equalline);
                pw.println();
                grantpercentagevaluetotal = 0;
                grantpercentagetaxtotal = 0;
                grantpercentagetottotal = 0;
                pw.flush();
                pw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getPrintWriterPercentage(AccountsModel am, String filePath) {

        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (Integer.valueOf(am.getPageno()) == 1) {
                HeaderPercentage(am, pw);
            }
            pw.printf("%14s%s%24s%20s%20s", am.getTaxpercentage(), "%", am.getValue(), am.getTaxamount(), am.getTotalamount());
            grantpercentagevaluetotal += Double.valueOf(am.getValue());
            grantpercentagetaxtotal += Double.valueOf(am.getTaxamount());
            grantpercentagetottotal += Double.valueOf(am.getTotalamount());
            pw.println();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
