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

public class ReceiptReport extends OnwardAction {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char[] BOLD1 = {(char) 14, (char) 18};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] equalline = new char[80];
    private char[] horizontalline = new char[80];
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public ReceiptReport() {
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

        String voucherno = am.getVoucherno();
        if (am.getAccountbook().equals("1")) {
            voucherno += " - CL1";
        } else if (am.getAccountbook().equals("2")) {
            voucherno += " - CL2";
        } else if (am.getAccountbook().equals("3")) {
            voucherno += " - NC1";
        }

        pw.write(BOLD1);
        pw.printf("%-7s%-27s%-8s%-11s", "RECP NO.:", voucherno, "DATE  : ", am.getAccdate());
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

            pw.printf("%42s%-38s", "   Received with Thanks from Thiru/Mr/Mrs ", am.getReceivername());
            pw.println();
            pw.println();

            StringBuffer buffer = new StringBuffer();
            buffer.append(" (Rupees ");
            buffer.append(new AmoConvertion().ConvertNumToStr(Math.round(Double.valueOf(am.getAmount()))));
            buffer.append(" Only.)");

            Iterator itr = JoinString(buffer.toString(), 80, " ").iterator();
            while (itr.hasNext()) {
                pw.printf("%-80s", itr.next());
                pw.println();
            }

            pw.printf("%s", "by Cash/DD/Cheque");
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.write(BOLD);
            pw.printf("%s", "  Mode DD/Cheque No  Date              Bank                             Amount");
            pw.write(RELEASE);
            pw.println();
            pw.print(horizontalline);
            pw.println();
            itr = am.getChequedetailslist().iterator();
            while (itr.hasNext()) {
                AccountsSubModel asm = (AccountsSubModel) itr.next();
                Iterator it = JoinString(asm.getBankname(), 35, ",").iterator();
                int k = 1;
                while (it.hasNext()) {
                    pw.printf("%-6s%s%-12s%s%-10s%s%-35s%s%12s", (k == 1) ? asm.getPaymenttype() : "", " ", (k == 1) ? asm.getChequeno() : "", " ", (k == 1) ? asm.getCdate() : "", " ", it.next(), " ", (k == 1) ? asm.getAmount() : "");
                    pw.println();
                    k++;
                }
            }

            pw.print(horizontalline);
            pw.println();
            pw.write(BOLD);
            pw.printf("%s", "Ac.code     Gr.Code           Ac.Head                                   Amount");
            pw.write(RELEASE);
            pw.println();
            pw.print(horizontalline);
            pw.println();
            itr = am.getAccountdetailslist().iterator();
            while (itr.hasNext()) {
                AccountsSubModel asm = (AccountsSubModel) itr.next();
                pw.printf("%-12s%s%-12s%s%-40s%s%12s", asm.getAccno(), " ", asm.getGroupname(), " ", asm.getAccname(), " ", asm.getAmount());
                pw.println();
            }

            pw.println();
            pw.println();
            pw.write(BOLD);
            pw.printf("%s", "   NARRATION");
            pw.write(RELEASE);
            pw.println();

            itr = JoinString(am.getDetails(), 80, " ").iterator();
            while (itr.hasNext()) {
                pw.printf("%-80s", itr.next());
                pw.println();
            }

            pw.println();
            pw.printf("%77s", "Tamil Nadu Civil Supplies Corporation");
            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.println();
            pw.printf("%-9s%-20s%s%47s", "File No: ", am.getFileno(), " ", "Asst./ Cashier  Supdt. D.M A/c");
            pw.println();
            pw.printf("%77s",am.getRegion());
            pw.println();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
