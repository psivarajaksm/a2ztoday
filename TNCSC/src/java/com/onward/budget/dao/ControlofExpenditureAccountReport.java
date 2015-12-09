/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.budget.dao;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.valueobjects.ReportModel;
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

public class ControlofExpenditureAccountReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[120];
    private char[] horizontalline = new char[120];
    private char[] equalline = new char[120];
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int lineno;
    int pageno;
    private double grandfundsallot = 0;
    private double grandcurrent = 0;
    private double granduptothe = 0;
    private double grandbalance = 0;
    private double grandexcees = 0;

    public ControlofExpenditureAccountReport() {
        for (int i = 0; i < 120; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 120; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 120; i++) {
            equalline[i] = '=';
        }
    }

    public void ControlofExpenditureAccountReportHeader(PrintWriter pw, ReportModel rm) {
        pw.printf("%s", "                             TAMIL NADU CIVIL SUPPLIES CORPORATION");
        pw.println();
        pw.printf("%70s%-6s", "CONTROL OF EXPENDITURE STATEMENT FOR THE MONTH OF ", rm.getBudgetmonthandyear());
        pw.println();
        pw.printf("%54s%-7s", "ACCOUNTING PERIOD : ", rm.getBudgetperiod());
        pw.println();
        pw.printf("%16s%-40s%36s%-2s", "  LEDGER NAME : ", rm.getLedgername(), "(RS. in Lakhs)            Page No : ", pageno);
        pw.println();
        pw.printf("%s", " ==============================================================================================");
        pw.println();
        pw.printf("%s", " | SNO   REGION NAME                        FUNDS      EXPS       EXPS      BALANCE     EXCESS|");
        pw.println();
        pw.printf("%s", " |                                          ALLOT     DURING      UPTO       FUND        EXPS |");
        pw.println();
        pw.printf("%53s%-11s%-24s%7s", " |                                                   ", rm.getBudgetmonthandyear(), rm.getBudgetmonthandyear(), "IF ANY|");
        pw.println();
        pw.printf("%s", " |============================================================================================|");
        pw.println();
        lineno = 0;
        pageno++;
    }

    public void ControlofExpenditureAccountReportGrandTotal(String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //pw.printf("%39s%10s%s%10s%s%10s%s%10s%s%10s%2s", " |                GRAND TOTAL          ", decimalFormat.format(grandfundsallot), " ", decimalFormat.format(grandcurrent), " ", decimalFormat.format(granduptothe), " ", decimalFormat.format(grandfundsallot - granduptothe), " ", decimalFormat.format(grandexcees), " |");
            pw.printf("%39s%10s%s%10s%s%10s%s%10s%s%10s%2s", " |                GRAND TOTAL          ", decimalFormat.format(grandfundsallot), " ", decimalFormat.format(grandcurrent), " ", decimalFormat.format(granduptothe), " ", decimalFormat.format(grandbalance), " ", decimalFormat.format(grandexcees), " |");
            pw.println();
            pw.printf("%s", " |--------------------------------------------------------------------------------------------|");
            pw.println();
            pw.flush();
            pw.close();

            grandfundsallot = 0;
            grandcurrent = 0;
            granduptothe = 0;
            grandbalance = 0;
            grandexcees = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getControlofExpenditureAccountReportPrintWriter(ReportModel rm, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (lineno > 48) {
                pw.write(FORM_FEED);
                pw.println();
                ControlofExpenditureAccountReportHeader(pw, rm);
            }

            if (rm.getSlipno() == 1) {
                pageno = 0;
                lineno = 0;
                pageno++;
                ControlofExpenditureAccountReportHeader(pw, rm);
            }

            //pw.printf("%3s%3s%2s%-30s%s%10s%s%10s%s%10s%s%10s%s%10s%2s", " | ", rm.getSlipno(), ". ", rm.getRegionname(), " ", rm.getActual(), " ", rm.getCurrentbudget(), " ", rm.getUptothebudget(), " ", decimalFormat.format((Double.valueOf(rm.getActual()) - Double.valueOf(rm.getUptothebudget()))), " ", rm.getExcessexpense(), " |");
            pw.printf("%3s%3s%2s%-30s%s%10s%s%10s%s%10s%s%10s%s%10s%2s", " | ", rm.getSlipno(), ". ", rm.getRegionname(), " ", rm.getActual(), " ", rm.getCurrentbudget(), " ", rm.getUptothebudget(), " ", rm.getBudgetbalance(), " ", rm.getExcessexpense(), " |");
            pw.println();

            grandfundsallot += Double.valueOf(rm.getActual());
            grandcurrent += Double.valueOf(rm.getCurrentbudget());
            granduptothe += Double.valueOf(rm.getUptothebudget());
            grandbalance += Double.valueOf(rm.getBudgetbalance());
            grandexcees += Double.valueOf(rm.getExcessexpense());

            lineno++;
            pw.printf("%s", " |--------------------------------------------------------------------------------------------|");
            pw.println();
            lineno++;

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}