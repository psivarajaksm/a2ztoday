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

public class BankChallanChequeReport extends OnwardAction {

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
    private double totalamount = 0;

    public BankChallanChequeReport() {
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
        pw.printf("%31s%-35s", "", am.getBankname());
        pw.println();
        pw.printf("%24s%-40s", "", (am.getBranchname() == null) ? "" : am.getBranchname());
        pw.println();
        pw.println();
        pw.write(BOLD);
        pw.printf("%55s%-30s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, ", am.getRegion());
        pw.write(RELEASE);
        pw.println();
        pw.println();
        pw.printf("%68s%-10s", "Current Account Pay-in-Slip                      Remittance  Date:", am.getSanctioneddate());
        pw.println();
        pw.printf("%13s%-38s%-17s", "Account No:", am.getBankaccountno(), "Realisation Date:");
        pw.println();
        pw.printf("%s", " -------------------------------------------------------------------------------");
        pw.println();
        pw.printf("%s", " |sno|Name of the Drawee Branch          |DD/CHQ.NO |   DATE   |      AMOUNT   |");
        pw.println();
        pw.printf("%s", " |---|-----------------------------------|----------|----------|---------------|");
        pw.println();
    }

    public void GrandTotal(AccountsModel am, PrintWriter pw) {
        pw.printf("%s", " |-----------------------------------------------------------------------------|");
        pw.println();
        pw.printf("%64s%15s%s", " |                                     TOTAL                   |", decimalFormat.format(totalamount), line1);
        pw.println();
        pw.printf("%s", " -------------------------------------------------------------------------------");
        pw.println();        

        StringBuffer buffer = new StringBuffer();
        long netpaymenttop = Math.round(totalamount);
        buffer.append(" (Rupees ");
        buffer.append(new AmoConvertion().ConvertNumToStr(netpaymenttop));
        buffer.append(" Only.)");

        pw.println();
        Iterator itr = JoinString(buffer.toString(), 80, " ").iterator();
        while (itr.hasNext()) {
            pw.printf("%-80s", itr.next());
            pw.println();
        }

        pw.println();
        pw.println();
        pw.println();
        pw.printf("%s", "  Branch Manager/Accountant                             Signature of remitter");
        pw.println();
        pw.printf("%s", "                                                          For TNCSC Limited");
        pw.println();
        pw.printf("%-60s%-30s", "  ScrollTransfer/Cashier                                    ", am.getRegion());
        pw.println();
        pw.println();
        pw.write(FORM_FEED);
        pw.println();

        totalamount = 0;

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

            Iterator itr = am.getChequedetailslist().iterator();
            int i = 1;
            while (itr.hasNext()) {
                if (i > 20) {
                    GrandTotal(am, pw);
                    Header(am, pw);
                    i = 1;
                }
                AccountsSubModel asm = (AccountsSubModel) itr.next();
                pw.printf("%2s%3s%s%-35s%s%-10s%s%-10s%s%15s%s", line2, i, line1, asm.getBankname(), line1, asm.getChequeno(), line1, asm.getCdate(), line1, asm.getAmount(), line1);
                totalamount += Double.valueOf(asm.getAmount());
                pw.println();
                pw.printf("%2s%3s%s%-35s%s%-10s%s%-10s%s%15s%s", line2, "", line1, "", line1, "", line1, "", line1, "", line1);
                pw.println();
                i++;
            }
            GrandTotal(am, pw);


            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
