/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.accounts;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.action.OnwardAction;
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

public class SalesVatAbstractReport extends OnwardAction {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[87];
    private char[] equalline = new char[87];
    private char[] horizontalline = new char[87];
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private String line = " | ";
    private String line1 = "|";
    private String line2 = " |";
    private String line3 = "| ";
    private double totamount = 0;
    private double taxamount = 0;
    private double grandtotamount = 0;
    private double grandtaxamount = 0;
    private String percentage;
    private int lineno;

    public SalesVatAbstractReport() {
        for (int i = 0; i < 87; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 87; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 87; i++) {
            equalline[i] = '=';
        }
    }

    public void Header(AccountsModel am, PrintWriter pw) {
        pw.println();
        pw.write(BOLD);
        pw.printf("%61s", "Tamil Nadu Civil Supplies Corporation");
        pw.write(RELEASE);
        pw.println();
        pw.write(BOLD);
        pw.printf("%49s", "Head Office");
        pw.write(RELEASE);
        pw.println();
        pw.printf("%60s%-8s", "Sales Vat Abstract For the Month Of :", am.getAccdate());
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%-87s", "|SNO  COMMODITY                                     TAX %       VALUE       TAX AMOUNT|");
        pw.println();
        pw.printf("%-87s", "|-------------------------------------------------------------------------------------|");
        pw.println();
    }

    public void GrandTotal(AccountsModel am, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                pw.printf("%s", "|-------------------------------------------------------------------------------------|");
                pw.println();
                pw.printf("%-22s%5s%-32s%13s%s%13s%s", "|", percentage, "% Total", decimalFormat.format(totamount), " ", decimalFormat.format(taxamount), "|");
                pw.println();
                pw.printf("%s", "|-------------------------------------------------------------------------------------|");
                pw.println();
                pw.printf("%59s%13s%s%13s%s", "|                      Grand Total                         ", decimalFormat.format(grandtotamount), " ", decimalFormat.format(grandtaxamount), "|");
                pw.println();
                pw.printf("%s", "---------------------------------------------------------------------------------------");
                pw.println();
                pw.write(FORM_FEED);
                pw.flush();
                pw.close();
                totamount = 0;
                taxamount = 0;
                grandtaxamount = 0;
                grandtotamount = 0;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void percentageTotal(AccountsModel am, PrintWriter pw) {
        pw.printf("%s", "|-------------------------------------------------------------------------------------|");
        pw.println();
        pw.printf("%-22s%5s%-32s%13s%s%13s%s", "|", percentage, "% Total", decimalFormat.format(totamount), " ", decimalFormat.format(taxamount), "|");
        pw.println();
        pw.printf("%s", "|-------------------------------------------------------------------------------------|");
        pw.println();
        totamount = 0;
        taxamount = 0;
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
            if ((Integer.valueOf(am.getSlipno())) == 1) {
                Header(am, pw);
                percentage = am.getTaxpercentage();
                lineno=1;
            }
            if (!percentage.equals(am.getTaxpercentage())) {
                percentageTotal(am, pw);
                percentage = am.getTaxpercentage();
            }
            if(lineno>50){
                pw.write(FORM_FEED);
                pw.println();
                Header(am, pw);
            }
            pw.printf("%s%3s%2s%-45s%s%5s%2s%13s%s%13s%s", "|", am.getSlipno(), "  ", am.getCommodityname(), " ", am.getTaxpercentage(), "  ", am.getTotalamount(), " ", am.getTaxamount(), "|");
            pw.println();
            lineno++;
            if(lineno>50){
                pw.write(FORM_FEED);
                pw.println();
                Header(am, pw);
            }
            totamount += Double.valueOf(am.getTotalamount());
            taxamount += Double.valueOf(am.getTaxamount());
            grandtotamount += Double.valueOf(am.getTotalamount());
            grandtaxamount += Double.valueOf(am.getTaxamount());
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
