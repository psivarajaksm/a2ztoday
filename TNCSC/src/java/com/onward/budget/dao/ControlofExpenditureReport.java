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

public class ControlofExpenditureReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[120];
    private char[] horizontalline = new char[120];
    private char[] equalline = new char[120];
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int lineno;
    private String groupname = "";
    int pageno;
    private double groupfundsallot = 0;
    private double groupcurrent = 0;
    private double groupuptothe = 0;
    private double groupbalance = 0;
    private double groupexcees = 0;
    private double grandfundsallot = 0;
    private double grandcurrent = 0;
    private double granduptothe = 0;
    private double grandbalance = 0;
    private double grandexcees = 0;

    public ControlofExpenditureReport() {
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

    public void ControlofExpenditureReportHeader(PrintWriter pw, ReportModel rm) {
        pw.printf("%s", "                                  TAMIL NADU CIVIL SUPPLIES CORPORATION");
        pw.println();
        pw.printf("%73s%-6s", "CONTROL OF EXPENDITURE STATEMENT FOR THE MONTH OF ", rm.getBudgetmonthandyear());
        pw.println();
        pw.printf("%59s%-7s", "ACCOUNTING PERIOD : ", rm.getBudgetperiod());
        pw.println();
        pw.printf("%11s%-53s%37s%-2s", "  REGION : ", rm.getRegionname(), "(RS. in Lakhs)             Page No : ", pageno);
        pw.println();
        pw.printf("%s", " =======================================================================================================");
        pw.println();
        pw.printf("%s", " | SNO   ACCOUNT HEAD OF ACCOUNT                     FUNDS       EXPS       EXPS     BALANCE     EXCESS|");
        pw.println();
        pw.printf("%s", " |        CODE                                       ALLOT      DURING      UPTO       FUND       EXPS |");
        pw.println();
//        pw.printf("%-61s%-11s%-22s%10s", " |                                                           ", rm.getBudgetmonthandyear(), rm.getBudgetmonthandyear(), "IF ANY   |");
        pw.printf("%-63s%-11s%-22s%8s", " |", rm.getBudgetmonthandyear(), rm.getBudgetmonthandyear(), "IF ANY |");
        pw.println();
        pw.printf("%s", " |=====================================================================================================|");
        pw.println();
        lineno = 0;
        pageno++;
    }

    public void ControlofExpenditureReportGrandTotal(String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.printf("%48s%10s%s%10s%s%10s%s%10s%s%10s%2s", " |                GROUP TOTAL                   ", decimalFormat.format(groupfundsallot), " ", decimalFormat.format(groupcurrent), " ", decimalFormat.format(groupuptothe), " ", decimalFormat.format(groupfundsallot - groupuptothe), " ", decimalFormat.format(groupexcees), " |");
            pw.println();
            pw.printf("%s", " |-----------------------------------------------------------------------------------------------------|");
            pw.println();
            pw.printf("%48s%10s%s%10s%s%10s%s%10s%s%10s%2s", " |                GRAND TOTAL                   ", decimalFormat.format(grandfundsallot), " ", decimalFormat.format(grandcurrent), " ", decimalFormat.format(granduptothe), " ", decimalFormat.format(grandfundsallot - granduptothe), " ", decimalFormat.format(grandexcees), " |");
            pw.println();
            pw.printf("%s", " |-----------------------------------------------------------------------------------------------------|");
            pw.println();
            pw.flush();
            pw.close();
            groupfundsallot = 0;
            groupcurrent = 0;
            groupuptothe = 0;
            groupbalance = 0;
            groupexcees = 0;

            grandfundsallot = 0;
            grandcurrent = 0;
            granduptothe = 0;
            grandbalance = 0;
            grandexcees = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ControlofExpenditureReportGroupTotal(PrintWriter pw) {
        try {
//            pw.printf("%s", " |-----------------------------------------------------------------------------------------------------|");
//            pw.println();
//            lineno++;
            pw.printf("%48s%10s%s%10s%s%10s%s%10s%s%10s%2s", " |                GROUP TOTAL                   ", decimalFormat.format(groupfundsallot), " ", decimalFormat.format(groupcurrent), " ", decimalFormat.format(groupuptothe), " ", decimalFormat.format(groupfundsallot - groupuptothe), " ", decimalFormat.format(groupexcees), " |");
            pw.println();
            lineno++;
            groupfundsallot = 0;
            groupcurrent = 0;
            groupuptothe = 0;
            groupbalance = 0;
            groupexcees = 0;

            pw.printf("%s", " |-----------------------------------------------------------------------------------------------------|");
            pw.println();
            lineno++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getControlofExpenditureReportPrintWriter(ReportModel rm, String filePath) {
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
                ControlofExpenditureReportHeader(pw, rm);
            }

            if (rm.getSlipno() == 1) {
                pageno = 0;
                lineno = 0;
                pageno++;
                ControlofExpenditureReportHeader(pw, rm);
                groupname = rm.getLedgergroupname();
                pw.printf("%3s%-100s%s", " | ", rm.getLedgergroupname(), "|");
                pw.println();
                lineno++;
                pw.printf("%s", " | ***********************************                                                                 |");
                pw.println();
                lineno++;
            }

            if (!groupname.equals(rm.getLedgergroupname())) {
                ControlofExpenditureReportGroupTotal(pw);
                pw.printf("%3s%-100s%s", " | ", rm.getLedgergroupname(), "|");
                pw.println();
                lineno++;
                pw.printf("%s", " | ***********************************                                                                 |");
                pw.println();
                lineno++;
                groupname = rm.getLedgergroupname();
            }

            pw.printf("%3s%3s%2s%7s%s%-30s%2s%10s%s%10s%s%10s%s%10s%s%10s%2s", " | ", rm.getSlipno(), ". ", rm.getLedgerid(), " ", rm.getLedgername(), "  ", rm.getActual(), " ", rm.getCurrentbudget(), " ", rm.getUptothebudget(), " ", decimalFormat.format((Double.valueOf(rm.getActual()) - Double.valueOf(rm.getUptothebudget()))), " ", rm.getExcessexpense(), " |");
            pw.println();

            grandfundsallot += Double.valueOf(rm.getActual());
            grandcurrent += Double.valueOf(rm.getCurrentbudget());
            granduptothe += Double.valueOf(rm.getUptothebudget());
            grandbalance += Double.valueOf(rm.getBudgetbalance());
            grandexcees += Double.valueOf(rm.getExcessexpense());

            groupfundsallot += Double.valueOf(rm.getActual());
            groupcurrent += Double.valueOf(rm.getCurrentbudget());
            groupuptothe += Double.valueOf(rm.getUptothebudget());
            groupbalance += Double.valueOf(rm.getBudgetbalance());
            groupexcees += Double.valueOf(rm.getExcessexpense());

            lineno++;
            pw.printf("%s", " |-----------------------------------------------------------------------------------------------------|");
            pw.println();
            lineno++;

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}