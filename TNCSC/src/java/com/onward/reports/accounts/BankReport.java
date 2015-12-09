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
import com.onward.common.AmoConvertion;
import com.onward.valueobjects.AccountsModel;
import com.onward.valueobjects.AccountsSubModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Iterator;


public class BankReport extends OnwardAction {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char[] BOLD1 = {(char) 14, (char) 18};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] equalline = new char[80];
    private char[] horizontalline = new char[80];
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public BankReport() {
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

        pw.write(BOLD1);
        pw.printf("%-39s", "TAMIL NADU CIVIL SUPPLIES CORPORATION.,");
        pw.write(RELEASE);
        pw.println();

        pw.printf("%34s%-15s", "", am.getRegion());
        pw.println();

        pw.write(BOLD1);
        pw.printf("%44s%-9s", "Year  : ", am.getAccountingperiod());
        pw.write(RELEASE);
        pw.println();

        pw.write(BOLD1);
        pw.printf("%44s%-8s", "Month : ", am.getAccountingmonthandyear());
        pw.write(RELEASE);
        pw.println();

        String voucherno = am.getVoucherno();
        if (am.getAccountbook().equals("1")) {
            voucherno += " - CL1";
        } else if (am.getAccountbook().equals("2")) {
            voucherno += " - CL2";
        } else if (am.getAccountbook().equals("3")) {
            voucherno += " - NC1";
        }

        pw.write(BOLD1);
//        pw.printf("%-7s%-27s%-8s%-11s", "BANK NO.:", am.getVoucherno(), "DATE  : ", am.getAccdate());
        pw.printf("%-7s%-27s%-8s%-11s", "BANK NO.:", voucherno, "DATE  : ", am.getAccdate());
        pw.write(RELEASE);
        pw.println();

        pw.print(horizontalline);
        pw.println();
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
            Header(am, pw);

            pw.write(BOLD);
            pw.printf("%s", "    Ac.code  Gr.Name        Ac.Name                        Debit         Credit");
            pw.write(RELEASE);
            pw.println();
            pw.print(horizontalline);
            pw.println();

            int lineno = 1;

            Iterator itr = am.getPaymentlist().iterator();
            while (itr.hasNext()) {
                AccountsSubModel asm = (AccountsSubModel) itr.next();
                pw.printf("%s%9s%s%-38s%s%14s%s%-14s", " ", "", " ", asm.getGroupname(), " ", "", " ", "");
                pw.println();
                lineno++;
                pw.printf("%s%9s%s%-38s%s%14s%s%-14s", " ", asm.getAccno(), " ", asm.getAccname(), " ", asm.getAmount(), " ", "");
                pw.println();
                lineno++;
            }

            itr = am.getAdjustmentlist().iterator();
            while (itr.hasNext()) {
                AccountsSubModel asm = (AccountsSubModel) itr.next();
                pw.printf("%s%9s%s%-38s%s%14s%s%-14s", " ", "", " ", asm.getGroupname(), " ", "", " ", "");
                pw.println();
                lineno++;
                pw.printf("%s%9s%s%-38s%s%14s%s%14s", " ", asm.getAccno(), " ", asm.getAccname(), " ", " ", " ", asm.getAmount());
                pw.println();
                lineno++;
            }

            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.println();

            pw.print(horizontalline);
            pw.println();
            pw.write(BOLD);
            pw.printf("%s%9s%s%-38s%s%14s%s%14s", " ", "", " ", "", " ", am.getDebittotal(), " ", am.getCredittotal());
            pw.write(RELEASE);
            pw.println();
            pw.print(horizontalline);
            pw.println();

            lineno = lineno + 10 + 9;
            System.out.println("lineno = " + lineno);
            if (lineno + 30 > 58) {
                pw.write(FORM_FEED);
                Header(am, pw);
            }

            pw.println();

            StringBuffer buffer = new StringBuffer();
            buffer.append(" (Rupees ");
            buffer.append(new AmoConvertion().ConvertNumToStr(Math.round(Double.valueOf(am.getDebittotal()))));
            buffer.append(" Only.)");

            pw.println();
            itr = JoinString(buffer.toString(), 80, " ").iterator();
            while (itr.hasNext()) {
                pw.printf("%-80s", itr.next());
                pw.println();
            }

            pw.println();
            pw.println();

            itr = JoinString(am.getDetails(), 60, " ").iterator();
            while (itr.hasNext()) {
                pw.printf("%-60s", itr.next());
                pw.println();
            }

            pw.println();
            pw.println();
            pw.println();
            pw.printf("%s", "                        ASST.     SUPDT.     AM/DM     D.A     MANAGER  CE");
            pw.println();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
