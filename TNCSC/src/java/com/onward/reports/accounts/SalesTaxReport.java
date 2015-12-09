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

public class SalesTaxReport implements PurchaseInterface {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[105];
    private char[] equalline = new char[105];
    private char[] horizontalline = new char[105];
    private int lineno = 1;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int count = 0;
    private int pageno = 1;
    private double granttottotal = 0;

    public SalesTaxReport() {
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

    public void Header(AccountsModel am, PrintWriter pw) {
        pw.println();
        pw.printf("%s", "   TAMIL NADU CIVIL SUPPLIES CORPORATION.,");
        pw.println();
        if (am.getBreakuptype().equals("1")) {
            pw.printf("%39s%-5s", "TAXABLE GOODS SOLD DURING THE MONTH ", am.getAccountingmonthandyear());
        } else if (am.getBreakuptype().equals("2")) {
            pw.printf("%54s%-5s", "NON-TAXABLE / EXEMPTED GOODS SOLD DURING THE MONTH ", am.getAccountingmonthandyear());
        }
        pw.println();
        pw.printf("%s%s", "   NAME OF THE REGION: ", "KARUR");
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%s", "SL  COMMODITY                CODE         QUANTITY           VALUE   RATE OF         TAX          TOTAL");
        pw.println();
        pw.printf("%s", "NO.                                                                   VAT");
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
                pw.printf("%60s%45s", "Grand Total", decimalFormat.format(granttottotal));
                pw.println();
                pw.print(equalline);
                pw.println();
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
            double paymenttotal = 0;
            double adjustmenttotal = 0;
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (Integer.valueOf(am.getPageno()) == 1) {
                Header(am, pw);
            }

            pw.printf("%3s%s%-25s%s%-6s%s%13s%s%16s%s%5s%s%15s%s%15s", am.getPageno(), " ", am.getCommodityname(), " ", am.getCommoditycode(), " ", am.getQuantity(), " ", am.getValue(), " ", am.getTaxpercentage(), " ", am.getTaxamount(), " ", am.getTotalamount());
            pw.println();
            lineno++;
            pw.println();
            lineno++;
            granttottotal += Double.valueOf(am.getTotalamount());

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
