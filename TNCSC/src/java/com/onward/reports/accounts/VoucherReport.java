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

public class VoucherReport extends OnwardAction {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char[] BOLD1 = {(char) 14, (char) 18};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] equalline = new char[80];
    private char[] horizontalline = new char[80];
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public VoucherReport() {
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
        StringBuffer buffer = new StringBuffer();
        buffer.append("Under Rs.");
        long netpaymenttop = Math.round(Double.valueOf(am.getNetpayment()) + 1);
        buffer.append(netpaymenttop);
        buffer.append(" (Rupees ");
        buffer.append(new AmoConvertion().ConvertNumToStr(netpaymenttop));
        buffer.append(" Only.)");

        pw.println();
        Iterator itr = JoinString(buffer.toString(), 80, " ").iterator();
        while (itr.hasNext()) {
            pw.printf("%-80s", itr.next());
            pw.println();
        }

        pw.write(BOLD1);
        pw.printf("%-39s", "TAMIL NADU CIVIL SUPPLIES CORPORATION.,");
        pw.write(RELEASE);
        pw.println();

        pw.printf("%34s%-15s", "", am.getRegion());
        pw.println();

        pw.write(BOLD1);
        String voucherno = am.getVoucherno();
        if (am.getAccountbook().equals("1")) {
            voucherno += " - CL1";
        } else if (am.getAccountbook().equals("2")) {
            voucherno += " - CL2";
        } else if (am.getAccountbook().equals("3")) {
            voucherno += " - NC1";
        }
        pw.printf("%3s%-29s%6s%-13s", "   ", voucherno, "V.NO :", (am.getCasherno() != null) ? am.getCasherno() : "");
        pw.write(RELEASE);
        pw.println();

        pw.write(BOLD1);
//        pw.printf("%3s%-29s%-6s%-10s", "   ", am.getAccdate(), "DATE :", am.getSanctioneddate());
        pw.printf("%3s%-29s%-6s%-10s", "   ", am.getAccdate(), "DATE :", (am.getVoucherapproveddate() != null) ? am.getVoucherapproveddate() : "");
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
            pw.printf("%s", "    Ac.code Gr.Name        Ac.Name                         Debit         Credit ");
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

            if (lineno < 15) {
                int lp = 15 - lineno;
                for (int i = 0; i < lp; i++) {
                    pw.println();
                }
                pw.print(horizontalline);
                pw.println();
                pw.write(BOLD);
                pw.printf("%28s%-23s%14s%s%-18s", "", "NET PAYMENT", am.getNetpayment(), " ", am.getPayment());
                pw.write(RELEASE);
                pw.println();
                pw.print(horizontalline);
                pw.println();
            } else {
                pw.print(horizontalline);
                pw.println();
                pw.write(BOLD);
                pw.printf("%28s%-23s%14s%s%-18s", "", "NET PAYMENT", am.getNetpayment(), " ", am.getPayment());
                pw.write(RELEASE);
                pw.println();
                pw.print(horizontalline);
                pw.println();
                pw.write(FORM_FEED);
                Header(am, pw);

            }

            pw.write(BOLD);
            pw.printf("%s", "NARRATION");
            pw.write(RELEASE);
            pw.println();
            pw.println();

            itr = JoinString(am.getDetails(), 60, " ").iterator();
            while (itr.hasNext()) {
                pw.printf("%-60s", itr.next());
                pw.println();
            }

            pw.println();
            pw.println();
            pw.printf("%-16s%-20s", "SANCTIONED BY :", am.getSanctionedby());
            pw.println();
            pw.printf("%-16s%-40s", "FILE NO.      : ", am.getFileno());
            pw.println();
            for (int i = 0; i < am.getInfavourof().length; i++) {
                pw.printf("%-24s%-40s", "CHEQUE IN FAVOUR OF M/S", am.getInfavourof()[i]);
                pw.println();
            }

            pw.print(horizontalline);
            pw.println();

            pw.printf("%-30s%-14s", "Passed for Net Payment for Rs.", am.getNetpayment());
            pw.println();

            StringBuffer buffer = new StringBuffer();
            buffer.append("(Rupees ");
            long netpay = Math.round(Double.valueOf(am.getNetpayment()));
            buffer.append(new AmoConvertion().ConvertNumToStr(netpay));
            buffer.append("Only.)");

            itr = JoinString(buffer.toString(), 80, " ").iterator();
            while (itr.hasNext()) {
                pw.printf("%-80s", itr.next());
                pw.println();
            }

            pw.println();

            if (Double.valueOf(am.getAdjustmenttotal()) != 0) {

                pw.printf("%-21s%-14s", "by adjustment for Rs.", am.getAdjustmenttotal());
                pw.println();

                buffer = new StringBuffer();
                buffer.append("(Rupees ");
                long adjpay = Math.round(Double.valueOf(am.getAdjustmenttotal()));
                buffer.append(new AmoConvertion().ConvertNumToStr(adjpay));
                buffer.append("Only.)");

                itr = JoinString(buffer.toString(), 80, " ").iterator();
                while (itr.hasNext()) {
                    pw.printf("%-80s", itr.next());
                    pw.println();
                }
            }

            pw.println();
            pw.println();
            pw.println();
            pw.printf("%s", "                        ASST.     SUPDT.     AM/DM     D.A     MANAGER  CE");
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.println();

            pw.printf("%s", "Received Cheque/Cash bearing No.                           Dt.");
            pw.println();

            pw.printf("%s", "for a sum of Rs.                 (Rupees");
            pw.println();

            pw.printf("%s", "                                 )from the Manager(Bills)T.N.C.S.C.");
            pw.println();

            pw.printf("%s", "                                                 HEADOFFICE");
            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.println();

            pw.printf("%10s%-46s%-9s", "Ch.No   : ", am.getChequeno(), "Signature");
            pw.println();
            pw.println();

            pw.printf("%10s%-45s%-11s", "Ch.Date : ", am.getChequedate(), "Designation");
            pw.println();

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
