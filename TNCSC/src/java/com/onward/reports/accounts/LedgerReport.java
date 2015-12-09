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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jboss.management.j2ee.JVM;

public class LedgerReport extends OnwardAction {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] equalline = new char[80];
    private char[] horizontalline = new char[80];
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private String line = " | ";
    private String line1 = "|";
    private String line2 = " |";
    private double debittotal = 0;
    private double credittotal = 0;
    private int lineno = 1;
    private int pno = 1;
    private String vdate = "";
    private String vdateandyear = "";
    String perviousaccountcode ="";
    private double debitbalanceamount = 0;
    private double creditbalanceamount = 0;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Date date = new Date();

    public LedgerReport() {
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

    public void Header(AccountsModel am, PrintWriter pw) {
        pw.println();
        pw.printf("%49s%-30s", "TAMIL NADU CIVIL SUPPLIES CORPORATION., ", am.getRegion());
        pw.println();
//        pw.printf("%39s%11s%2s%11s", "LEDGER FOR THE PERIOD FROM   ", am.getFromdate(), " - ", am.getTodate());
//        pw.println();
        pw.printf("%31s%11s%3s%11s%2s%-19s%s", "LEDGER FOR THE PERIOD FROM   ", am.getFromdate(), " - ", am.getTodate(), " (", dateFormat.format(date), ")");
        pw.println();
        pw.printf("%21s%-7s%-2s%-40s", "ACCOUNT HEAD :  ", am.getAccountcode(), "-", am.getAccountname());
        pw.println();
        pw.printf("%20s%-6s%-2s%-40s%8s%2s", "GROUP HEAD   : ", am.getGroupcode(), "-", am.getGroupname(), "Page.No:", String.valueOf(pno));
        pw.println();
        pw.printf("%s", "  ------------------------------------------------------------------------------");
        pw.println();
        pw.printf("%s", "  |  DATE  |  V/R/P NO                         |      DEBIT    |     CREDIT    |");
        pw.println();
        pw.printf("%s", "  |----------------------------------------------------------------------------|");
        pw.println();
        pno++;
    }

     public void HeaderGroup(AccountsModel am, PrintWriter pw, String accountCode) {
        pw.println();
        pw.printf("%49s%-30s", "TAMIL NADU CIVIL SUPPLIES CORPORATION., ", am.getRegion());
        pw.println();
        pw.printf("%31s%11s%3s%11s%2s%-19s%s", "LEDGER FOR THE PERIOD FROM   ", am.getFromdate(), " - ", am.getTodate(), " (", dateFormat.format(date), ")");
        pw.println();
        pw.printf("%21s%-7s%-2s%-40s", "ACCOUNT HEAD :  ", accountCode, "-", am.getAccountname());
        pw.println();
        pw.printf("%20s%-6s%-2s%-40s%8s%2s", "GROUP HEAD   : ", am.getGroupcode(), "-", am.getGroupname(), "Page.No:", String.valueOf(pno));
        pw.println();
        pw.printf("%s", "  ------------------------------------------------------------------------------");
        pw.println();
        pw.printf("%s", "  |  DATE  |  V/R/P NO                         |      DEBIT    |     CREDIT    |");
        pw.println();
        pw.printf("%s", "  |----------------------------------------------------------------------------|");
        pw.println();
        pno++;
    }
     
    public void GrandTotal(AccountsModel am, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                pw.printf("%s", "  |========|===================================|===============|===============|");
                pw.println();
                pw.printf("%3s%8s%s%-5s%-30s%s%15s%s%15s%s", "  |", "", "|", vdateandyear, " Total", "|", decimalFormat.format(debittotal), "|", decimalFormat.format(credittotal), "|");
                pw.println();
                pw.printf("%s", "  |========|===================================|===============|===============|");
                pw.println();
                pw.printf("%3s%8s%s%-35s%s%15s%s%15s%s", "  |", "", "|", "Previous Month Balance", "|", decimalFormat.format(debitbalanceamount), "|", decimalFormat.format(creditbalanceamount), "|");
                pw.println();
                pw.printf("%s", "  |========|===================================|===============|===============|");
                pw.println();
                double debitbalance = debittotal + debitbalanceamount;
                double creditbalance = credittotal + creditbalanceamount;
                pw.printf("%3s%8s%s%-35s%s%15s%s%15s%s", "  |", "", "|", "Progressive Total", "|", decimalFormat.format(debitbalance), "|", decimalFormat.format(creditbalance), "|");
                pw.println();
                pw.printf("%s", "  |========|===================================|===============|===============|");
                pw.println();
//                System.out.println("am.getGroupcode() = " + am.getGroupcode());
//                System.out.println("am.getGroupname() = " + am.getGroupname());
                if (debitbalance > creditbalance) {
                    pw.printf("%3s%8s%s%-5s%-30s%s%15s%s%15s%s", "  |", "", "|", vdateandyear, " Balance", "|", decimalFormat.format(debitbalance - creditbalance) + "Dr", "|", "", "|");
                    pw.println();
                } else if (debitbalance < creditbalance) {
                    pw.printf("%3s%8s%s%-5s%-30s%s%15s%s%15s%s", "  |", "", "|", vdateandyear, " Balance", "|", "", "|", decimalFormat.format(creditbalance - debitbalance) + "Cr", "|");
                    pw.println();
                } else {
                    pw.printf("%3s%8s%s%-5s%-30s%s%15s%s%15s%s", "  |", "", "|", vdateandyear, " Balance", "|", "", "|", "", "|");
                    pw.println();
                }

                pw.printf("%s", "  |========|===================================|===============|===============|");
                pw.println();
                debittotal = 0;
                credittotal = 0;
                debitbalanceamount = 0;
                creditbalanceamount = 0;
                lineno = 1;
                pno = 1;

                pw.flush();
                pw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void monthwisetotal(AccountsModel am, PrintWriter pw) {
        pw.printf("%s", "  |========|===================================|===============|===============|");
        pw.println();
        pw.printf("%3s%8s%s%-5s%-30s%s%15s%s%15s%s", "  |", "", "|", vdateandyear, " Total", "|", decimalFormat.format(debittotal), "|", decimalFormat.format(credittotal), "|");
        pw.println();
        pw.printf("%s", "  |========|===================================|===============|===============|");
        pw.println();
        pw.printf("%3s%8s%s%-35s%s%15s%s%15s%s", "  |", "", "|", "Previous Month Balance", "|", decimalFormat.format(debitbalanceamount), "|", decimalFormat.format(creditbalanceamount), "|");
        pw.println();
        pw.printf("%s", "  |========|===================================|===============|===============|");
        pw.println();
        double debitbalance = debittotal + debitbalanceamount;
        double creditbalance = credittotal + creditbalanceamount;
        pw.printf("%3s%8s%s%-35s%s%15s%s%15s%s", "  |", "", "|", "Progressive Total", "|", decimalFormat.format(debitbalance), "|", decimalFormat.format(creditbalance), "|");
        pw.println();
        pw.printf("%s", "  |========|===================================|===============|===============|");
        pw.println();
        if (debitbalance > creditbalance) {
            debitbalanceamount = debitbalance - creditbalance;
            creditbalanceamount = 0;
            pw.printf("%3s%8s%s%-5s%-30s%s%15s%s%15s%s", "  |", "", "|", vdateandyear, " Balance", "|", decimalFormat.format(debitbalance - creditbalance), "|", "", "|");
            pw.println();
        } else if (debitbalance < creditbalance) {
            debitbalanceamount = 0;
            creditbalanceamount = creditbalance - debitbalance;
            pw.printf("%3s%8s%s%-5s%-30s%s%15s%s%15s%s", "  |", "", "|", vdateandyear, " Balance", "|", "", "|", decimalFormat.format(creditbalance - debitbalance), "|");
            pw.println();
        } else {
            debitbalanceamount = 0;
            creditbalanceamount = 0;
            pw.printf("%3s%8s%s%-5s%-30s%s%15s%s%15s%s", "  |", "", "|", vdateandyear, " Balance", "|", "", "|", "", "|");
            pw.println();
        }


        pw.printf("%s", "  |========|===================================|===============|===============|");
        pw.println();
        debittotal = 0;
        credittotal = 0;
//        lineno = 1;
//        pno = 1;
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
                vdate = am.getAccdate().substring(3, 5);
                vdateandyear = am.getAccdate().substring(3, 8);
                debitbalanceamount = Double.valueOf(am.getDebitamount());
                creditbalanceamount = Double.valueOf(am.getCreditamount());
                perviousaccountcode = am.getAccountcode();
            }

            if (lineno + 15 > 52) {
                    pw.printf("%s", "  |----------------------------------------------------------------------------|");
                    pw.println();
                    lineno = 1;
                    pw.write(FORM_FEED);
                    HeaderGroup(am, pw, perviousaccountcode);                
                }
            if ((!vdate.equals(am.getAccdate().substring(3, 5)))||(!perviousaccountcode.equalsIgnoreCase(am.getAccountcode()))) {              
                monthwisetotal(am, pw);
                vdate = am.getAccdate().substring(3, 5);
                vdateandyear = am.getAccdate().substring(3, 8);
                String accountcode = am.getAccountcode();
                if (!accountcode.equalsIgnoreCase(perviousaccountcode)) {
                    debitbalanceamount = 0;
                    creditbalanceamount = 0;
                    lineno = 1;
                    pw.write(FORM_FEED);
                    Header(am, pw);
                }
                perviousaccountcode = am.getAccountcode();
            }

            if (lineno > 50) {
                pw.printf("%s", "  |----------------------------------------------------------------------------|");
                pw.println();
                lineno = 1;
                pw.write(FORM_FEED);
                Header(am, pw);
            }

            if (am.getVoucheroption().equalsIgnoreCase("RECEIPT") || am.getVoucheroption().equalsIgnoreCase("CREDIT") || am.getVoucheroption().equalsIgnoreCase("ADJUSTMENT")) {
                pw.printf("%3s%8s%s%4s%-31s%s%15s%s%15s%s", "  |", am.getAccdate(), "|", "    ", am.getCompno() + " - " + am.getVoucherno(), "|", "", "|", am.getAmount(), "|");
                pw.println();
                lineno++;
                credittotal += Double.valueOf(am.getAmount());
            } else if (am.getVoucheroption().equalsIgnoreCase("PAYMENT") || am.getVoucheroption().equalsIgnoreCase("DEBIT")) {
                pw.printf("%3s%8s%s%4s%-31s%s%15s%s%15s%s", "  |", am.getAccdate(), "|", "    ", am.getCompno() + " - " + am.getVoucherno(), "|", am.getAmount(), "|", "", "|");
                pw.println();
                lineno++;
                debittotal += Double.valueOf(am.getAmount());
            }

            if (lineno > 50) {
                pw.printf("%s", "  |----------------------------------------------------------------------------|");
                pw.println();
                lineno = 1;
                pw.write(FORM_FEED);
                Header(am, pw);
            }

            Iterator itr = JoinString(am.getDetails(), 35, " ").iterator();
            while (itr.hasNext()) {
                String str = (String) itr.next();
//                System.out.println(str.trim()+" - >"+str.length());
                pw.printf("%3s%8s%s%-35s%s%15s%s%15s%s", "  |", "", "|", str.trim(), "|", "", "|", "", "|");
                pw.println();
                lineno++;
            }

            pw.printf("%3s%8s%s%-35s%s%15s%s%15s%s", "  |", "", "|", "", "|", "", "|", "", "|");
            pw.println();
            lineno++;

            if (lineno > 50) {
                pw.printf("%s", "  |----------------------------------------------------------------------------|");
                pw.println();
                lineno = 1;
                pw.write(FORM_FEED);
                Header(am, pw);
            }
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
