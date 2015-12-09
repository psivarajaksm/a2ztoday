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

public class CashBookBankReport extends OnwardAction{

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[132];
    private char[] equalline = new char[132];
    private char[] horizontalline = new char[132];
    private int lineno = 1;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int count = 0;
    private String line = " | ";
    private String line1 = "|";
    private String line2 = " |";
    private double day_payment = 0;
    private double day_adjustment = 0;
    private double grant_payment = 0;
    private double grant_adjustment = 0;
    private String checkdate = "";
    private int pageno = 1;
    private String cashbookname = "";

    public CashBookBankReport() {
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

    public void Header(AccountsModel am, PrintWriter pw) {
        pw.println();
        pw.write(BOLD);
        pw.printf("%64s%-15s", "TAMIL NADU CIVIL SUPPLIES CORPORATION., ", am.getRegion());
        pw.write(RELEASE);
        pw.println();
        cashbookname = "(" + am.getCashbookname() + ")";
        pw.printf("%-15s%20s%-9s%21s%10s%4s%10s%11s%2s", am.getCashbook(), "          CASH BOOK ", cashbookname, " FOR THE PERIOD FROM ", am.getFromdate(), " To ", am.getTodate(), "   Page No:", pageno);
        pw.println();
        pw.printf("%s", " ---------------------------------------------------------------------------------------------------------------------------------------");
        pw.println();
        pw.printf("%s", " |    DATE    |    VR.NO  |   COMP.NO  |   CODE   |          PAYMENT DETAILS              |    AMOUNT    |    PAYMENT   |  ADJUSTMENT  |");
        pw.println();
        pw.printf("%s", " |------------|-----------|------------|----------|---------------------------------------|--------------|--------------|--------------|");
        pw.println();
        pageno++;
    }

    public void PageTotal(PrintWriter pw) {
        pw.printf("%s", " ---------------------------------------------------------------------------------------------------------------------------------------");
        pw.println();
        pw.write(FORM_FEED);
    }

    public void GrandTotal(AccountsModel am, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                pw.printf("%s", " |-------------------------------------------------------------------------------------------------------------------------------------|");
                pw.println();
                pw.printf("%106s%14s%s%14s%s", " |                                                                                 Day Total             |", decimalFormat.format(day_payment), line1, decimalFormat.format(day_adjustment), line1);
                pw.println();
                pw.printf("%s", " |=====================================================================================================================================|");
                pw.println();
                pw.printf("%106s%14s%s%14s%s", " |                                                                                 Grant Total           |", decimalFormat.format(grant_payment), line1, decimalFormat.format(grant_adjustment), line1);
                pw.println();
                pw.printf("%s", " |=====================================================================================================================================|");
                pw.flush();
                pw.close();

                day_payment = 0;
                day_adjustment = 0;
                grant_payment = 0;
                grant_adjustment = 0;

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void DayTotal(AccountsModel am, PrintWriter pw) {
        pw.printf("%s", " |-------------------------------------------------------------------------------------------------------------------------------------|");
        pw.println();
        pw.printf("%106s%14s%s%14s%s", " |                                                                                 Day Total             |", decimalFormat.format(day_payment), line1, decimalFormat.format(day_adjustment), line1);
        pw.println();
        pw.printf("%s", " |-------------------------------------------------------------------------------------------------------------------------------------|");
        pw.println();
        pw.write(FORM_FEED);
        day_payment = 0;
        day_adjustment = 0;
        lineno = 1;
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
            if (Integer.valueOf(am.getSlipno()) == 1) {
                Header(am, pw);
                checkdate = am.getAccdate();
                lineno = 1;
            }
            if (!checkdate.equals(am.getAccdate())) {
                DayTotal(am, pw);
                Header(am, pw);
                checkdate = am.getAccdate();
            }

            if (lineno > 48) {
                PageTotal(pw);
                Header(am, pw);
                lineno = 1;
            }

            Iterator pitr = am.getPaymentmap().entrySet().iterator();
            while (pitr.hasNext()) {
                Map.Entry pmap = (Entry) pitr.next();
                pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%37s%2s%14s%s%14s%s%14s%s", line, am.getAccdate(), line, (am.getVoucherno() == null) ? "" : am.getVoucherno(), line, (am.getCompno() == null) ? "" : am.getCompno(), line, "", line, Align((String) pmap.getKey(), 37, "CENTER"), line2, "", line1, "", line1, "", line1);
                pw.println();
                lineno++;
                List plist = (List) pmap.getValue();
                Iterator itr = plist.iterator();
                while (itr.hasNext()) {
                    AccountsSubModel asm = (AccountsSubModel) itr.next();
                    pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%37s%2s%14s%s%14s%s%14s%s", line, "", line, "", line, "", line, asm.getAccno(), line, Align(asm.getAccname(), 37, "LEFT"), line2, asm.getAmount(), line1, "", line1, "", line1);
                    pw.println();
                    paymenttotal += Double.valueOf(asm.getAmount());
                    lineno++;
                }
            }

            if (lineno > 48) {
                PageTotal(pw);
                Header(am, pw);
                lineno = 1;
            }


            if (am.getPaymentmap().size() > 0) {
                pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%37s%2s%14s%s%14s%s%14s%s", line, "", line, "", line, "", line, "", line, "", line2, "--------------", line1, "", line1, "", line1);
                pw.println();
                lineno++;
                pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%37s%2s%14s%s%14s%s%14s%s", line, "", line, "", line, "", line, "", line, "", line2, decimalFormat.format(paymenttotal), line1, "", line1, "", line1);
                pw.println();
                lineno++;
                pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%37s%2s%14s%s%14s%s%14s%s", line, "", line, "", line, "", line, "", line, "", line2, "==============", line1, "", line1, "", line1);
                pw.println();
                lineno++;
            }
            if (lineno > 48) {
                PageTotal(pw);
                Header(am, pw);
                lineno = 1;
            }

            Iterator aitr = am.getAdjustmentmap().entrySet().iterator();
            while (aitr.hasNext()) {
                Map.Entry amap = (Entry) aitr.next();
                pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%37s%2s%14s%s%14s%s%14s%s", line, am.getAccdate(), line, (am.getVoucherno() == null) ? "" : am.getVoucherno(), line, (am.getCompno() == null) ? "" : am.getCompno(), line, "", line, Align((String) amap.getKey(), 37, "CENTER"), line2, "", line1, "", line1, "", line1);
                pw.println();
                lineno++;
                List alist = (List) amap.getValue();
                Iterator itr = alist.iterator();
                while (itr.hasNext()) {
                    AccountsSubModel asm = (AccountsSubModel) itr.next();
                    pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%37s%2s%14s%s%14s%s%14s%s", line, "", line, "", line, "", line, asm.getAccno(), line, Align(asm.getAccname(), 37, "LEFT"), line2, asm.getAmount(), line1, "", line1, "", line1);
                    pw.println();
                    adjustmenttotal += Double.valueOf(asm.getAmount());
                    lineno++;
                }
            }

            if (lineno > 48) {
                PageTotal(pw);
                Header(am, pw);
                lineno = 1;
            }


            if (am.getAdjustmentmap().size() > 0) {
                pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%37s%2s%14s%s%14s%s%14s%s", line, "", line, "", line, "", line, "", line, "", line2, "--------------", line1, "", line1, "", line1);
                pw.println();
                lineno++;
                pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%37s%2s%14s%s%14s%s%14s%s", line, "", line, "", line, "", line, "", line, "", line2, decimalFormat.format(adjustmenttotal), line1, "", line1, "", line1);
                pw.println();
                lineno++;
                pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%37s%2s%14s%s%14s%s%14s%s", line, "", line, "", line, "", line, "", line, "", line2, "==============", line1, "", line1, "", line1);
                pw.println();
                lineno++;
            }
            if (lineno > 48) {
                PageTotal(pw);
                Header(am, pw);
                lineno = 1;
            }


            Iterator itr = am.getNarrationlist().iterator();
            int i = 1;
            while (itr.hasNext()) {
                String str = (String) itr.next();
                pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%-37s%2s%14s%s%14s%s%14s%s", line, "", line, "", line, "", line, "", line, str.trim(), line2, "", line1, (i == 1) ? decimalFormat.format(paymenttotal - adjustmenttotal) : "", line1, (i == 1) ? decimalFormat.format(adjustmenttotal) : "", line1);
                pw.println();
                lineno++;
                i++;
            }
            if (lineno > 48) {
                PageTotal(pw);
                Header(am, pw);
                lineno = 1;
            }


            day_payment += paymenttotal - adjustmenttotal;
            day_adjustment += adjustmenttotal;
            grant_payment += paymenttotal - adjustmenttotal;
            grant_adjustment += adjustmenttotal;

            pw.printf("%3s%10s%3s%9s%3s%10s%3s%8s%3s%-37s%2s%14s%s%14s%s%14s%s", line, "", line, "", line, "", line, "", line, "", line2, "", line1, "", line1, "", line1);
            pw.println();
            lineno++;

            if (lineno > 48) {
                PageTotal(pw);
                Header(am, pw);
                lineno = 1;
            }


            paymenttotal = 0;
            adjustmenttotal = 0;

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
