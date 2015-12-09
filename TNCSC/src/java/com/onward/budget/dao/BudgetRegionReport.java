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

public class BudgetRegionReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[190];
    private char[] horizontalline = new char[190];
    private char[] equalline = new char[190];
    private String groupname = "";
    private double groupFirstactual = 0;
    private double groupSecondactual = 0;
    private double groupThirdactual = 0;
    private double groupAverage = 0;
    private double groupBudgetestimate = 0;
    private double groupActualfirsthalf = 0;
    private double groupProbablesecondhalf = 0;
    private double groupRevisedestimate = 0;
    private double groupBudgetestimatenext = 0;
    private double grandFirstactual = 0;
    private double grandSecondactual = 0;
    private double grandThirdactual = 0;
    private double grandAverage = 0;
    private double grandBudgetestimate = 0;
    private double grandActualfirsthalf = 0;
    private double grandProbablesecondhalf = 0;
    private double grandRevisedestimate = 0;
    private double grandBudgetestimatenext = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int lineno = 1;

    public BudgetRegionReport() {
        for (int i = 0; i < 190; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 190; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 190; i++) {
            equalline[i] = '=';
        }
    }

    public void budgetReportHeader(PrintWriter pw, ReportModel psm) {
        int startyear = psm.getStartyear();
        int endyear = psm.getEndyear();
        String endyearsubset = String.valueOf(endyear).substring(2, 4);
        
        pw.println();
        lineno++;
        pw.printf("%s", "                                              ANNEXURE - IV");
        pw.println();
        lineno++;
        pw.printf("%s", "                   TAMIL NADU CIVIL SUPPLIES CORPORATION,");
        pw.println();
        lineno++;
        pw.printf("%44s%4s%s%2s%25s%4s%s%4s%78s", "REVISED ESTIMATE FOR ", startyear, "-", endyearsubset, " AND BUDGET ESTIMATE FOR ", startyear+1, "-", startyear+2, "RUPEES IN LAKHS");
        pw.println();
        lineno++;
        pw.print(horizontalline);
        pw.println();
        lineno++;
        pw.printf("%s", "  SL  HEAD OF                                     ACTUALS IN               AVERAGE FOR   BUDGET EST.    ACTUAL  PROBABLE FOR REV.ESTI.    BUDGET          HEAD OFFICE");
        pw.println();
        lineno++;
        pw.printf("%-41s%4s%s%4s%2s%4s%s%4s%2s%4s%s%4s%19s%4s%s%2s%27s%4s%s%2s%35s", "  NO  ACCOUNT", psm.getFirststartyear(), "-", psm.getFirstendyear(), "  ", psm.getSecondstartyear(), "-", psm.getSecondendyear(), "   ", psm.getThirdstartyear(), "-", psm.getThirdendyear(), "  THREE YEARS   IN ", startyear, "-", endyearsubset, "    UPTO SEP  CT TO MAR    ", startyear, "-", endyearsubset, "    ESTIMATE       R.E.        B.E.");
        pw.println();
        lineno++;
        pw.print(horizontalline);
        pw.println();
        lineno++;
    }

    public void budgetReportGroupTotal(PrintWriter pw) {
        pw.printf("%36s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s", "              GROUP TOTAL           ", " ", decimalFormat.format(groupFirstactual), " ", decimalFormat.format(groupSecondactual), " ", decimalFormat.format(groupThirdactual), " ", decimalFormat.format(groupAverage), " ", decimalFormat.format(groupBudgetestimate), " ", decimalFormat.format(groupActualfirsthalf), " ", decimalFormat.format(groupProbablesecondhalf), " ", decimalFormat.format(groupRevisedestimate), " ", decimalFormat.format(groupBudgetestimatenext), " ", "0.00", " ", "0.00");
        pw.println();
        lineno++;
        pw.print(horizontalline);
        pw.println();
        lineno++;
        groupFirstactual = 0;
        groupSecondactual = 0;
        groupThirdactual = 0;
        groupAverage = 0;
        groupBudgetestimate = 0;
        groupActualfirsthalf = 0;
        groupProbablesecondhalf = 0;
        groupRevisedestimate = 0;
        groupBudgetestimatenext = 0;
    }

    public void budgetReportGrandTotal(String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.printf("%36s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s", "              GRAND TOTAL           ", " ", decimalFormat.format(grandFirstactual), " ", decimalFormat.format(grandSecondactual), " ", decimalFormat.format(grandThirdactual), " ", decimalFormat.format(grandAverage), " ", decimalFormat.format(grandBudgetestimate), " ", decimalFormat.format(grandActualfirsthalf), " ", decimalFormat.format(grandProbablesecondhalf), " ", decimalFormat.format(grandRevisedestimate), " ", decimalFormat.format(grandBudgetestimatenext), " ", "0.00", " ", "0.00");
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.flush();
            pw.close();
            groupFirstactual = 0;
            groupSecondactual = 0;
            groupThirdactual = 0;
            groupAverage = 0;
            groupBudgetestimate = 0;
            groupActualfirsthalf = 0;
            groupProbablesecondhalf = 0;
            groupRevisedestimate = 0;
            groupBudgetestimatenext = 0;

            grandFirstactual = 0;
            grandSecondactual = 0;
            grandThirdactual = 0;
            grandAverage = 0;
            grandBudgetestimate = 0;
            grandActualfirsthalf = 0;
            grandProbablesecondhalf = 0;
            grandRevisedestimate = 0;
            grandBudgetestimatenext = 0;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getBudgetReportPrintWriter(ReportModel psm, String filePath) {
//        System.out.println("***************** Abstract class getSalaryAbstractPrintWriter method is calling *******************");
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (psm.getSlipno() == 1) {
                budgetReportHeader(pw, psm);
                groupname = psm.getLedgergroupname();
                pw.printf("%6s%s", "", psm.getLedgergroupname());
                pw.println();
                lineno++;
                pw.printf("%6s%s", "", "~~~~~~~~~~~~~~~~~~~~~~~~~~");
                pw.println();
                lineno++;
            }

            if (!groupname.equals(psm.getLedgergroupname())) {
                budgetReportGroupTotal(pw);
                pw.printf("%6s%s", "", psm.getLedgergroupname());
                pw.println();
                lineno++;
                pw.printf("%6s%s", "", "~~~~~~~~~~~~~~~~~~~~~~~~~~");
                pw.println();
                lineno++;

                if (lineno >= 61) {
                    pw.println();
                    pw.print(FORM_FEED);
                    lineno = 1;
                    budgetReportHeader(pw, psm);
                }

                groupname = psm.getLedgergroupname();
            }

            if (lineno >= 61) {
                pw.println();
                pw.print(FORM_FEED);
                lineno = 1;
                budgetReportHeader(pw, psm);
            }

            pw.printf("%4s%-2s%-30s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s", psm.getSlipno(), ". ", psm.getLedgername(), " ", psm.getFirstactual(), " ", psm.getSecondactual(), " ", psm.getThirdactual(), " ", psm.getAverage(), " ", psm.getBudgetestimate(), " ", psm.getActualfirsthalf(), " ", psm.getProbablesecondhalf(), " ", psm.getRevisedestimate(), " ", psm.getBudgetestimatenext(), " ", psm.getHorevisedestimate(), " ", psm.getHobudgetestimate());
            pw.println();
            lineno++;

            if (lineno >= 61) {
                pw.println();
                pw.print(FORM_FEED);
                lineno = 1;
                budgetReportHeader(pw, psm);
            }

            pw.print(horizontalline);
            pw.println();
            lineno++;

            if (lineno >= 61) {
                pw.println();
                pw.print(FORM_FEED);
                lineno = 1;
                budgetReportHeader(pw, psm);
            }

            groupFirstactual += Double.valueOf(psm.getFirstactual());
            groupSecondactual += Double.valueOf(psm.getSecondactual());
            groupThirdactual += Double.valueOf(psm.getThirdactual());
            groupAverage += Double.valueOf(psm.getAverage());
            groupBudgetestimate += Double.valueOf(psm.getBudgetestimate());
            groupActualfirsthalf += Double.valueOf(psm.getActualfirsthalf());
            groupProbablesecondhalf += Double.valueOf(psm.getProbablesecondhalf());

            grandFirstactual += Double.valueOf(psm.getFirstactual());
            grandSecondactual += Double.valueOf(psm.getSecondactual());
            grandThirdactual += Double.valueOf(psm.getThirdactual());
            grandAverage += Double.valueOf(psm.getAverage());
            grandBudgetestimate += Double.valueOf(psm.getBudgetestimate());
            grandActualfirsthalf += Double.valueOf(psm.getActualfirsthalf());
            grandProbablesecondhalf += Double.valueOf(psm.getProbablesecondhalf());
            grandRevisedestimate += Double.valueOf(psm.getRevisedestimate());
            grandBudgetestimatenext += Double.valueOf(psm.getBudgetestimatenext());

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
}
