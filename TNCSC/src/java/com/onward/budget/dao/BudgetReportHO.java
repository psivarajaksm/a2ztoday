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

public class BudgetReportHO {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] horizontalline = new char[80];
    private char[] equalline = new char[80];
    private String groupname = "";
    private double groupHorevisedestimate = 0;
    private double groupHobudgetestimate = 0;
    private double groupbudgetestimate = 0;
    private double groupPreviousbudgetestimate = 0;
    private double grandHorevisedestimate = 0;
    private double grandHobudgetestimate = 0;
    private double grandbudgetestimate = 0;
    private double grandPreviousbudgetestimate = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int lineno = 1;

    public BudgetReportHO() {
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

    public void budgetReportHeader(PrintWriter pw, ReportModel psm) {
        int startyear = psm.getStartyear();
        int endyear = psm.getEndyear();
        String endyearsubset = String.valueOf(endyear).substring(2, 4);

        pw.println();
        lineno++;
        pw.printf("%s%s", "              TAMIL NADU CIVIL SUPPLIES CORPORATION,", psm.getRegionname());
        pw.println();
        lineno++;
        if (psm.getRegionname().equalsIgnoreCase("All Region")) {
            pw.printf("%37s%4s%s%2s%25s%4s%s%4s", "  CONSOLIDATION REVISED ESTIMATE FOR ", startyear, "-", endyearsubset, " AND BUDGET ESTIMATE FOR ", startyear + 1, "-", startyear + 2);
        } else {
            pw.printf("%23s%4s%s%2s%25s%4s%s%4s", "  REVISED ESTIMATE FOR ", startyear, "-", endyearsubset, " AND BUDGET ESTIMATE FOR ", startyear + 1, "-", startyear + 2);
        }
        pw.println();
        lineno++;
        pw.printf("%s", "                                           (RUPEES IN LAKHS)");
        pw.println();
        lineno++;
        pw.printf("%s", " |===================================================================================================================|");
//        pw.printf("%s", " |========================================================================================|");11,13,13
        pw.println();
        lineno++;
        pw.printf("%s", " |  SL | ACC.CODE | NAME OF THE A/C HEAD            |   ACTUAL   |   BUD.EST  |   REV.EST  |    FMA     |  BUD.EST   |");
//        pw.printf("%s", " |  SL | ACC.CODE | NAME OF THE A/C HEAD            |   ACTUAL   |   BUD.EST  |   REV.EST  |  BUD.EST   |");
        pw.println();
        lineno++;
        pw.printf("%56s%4s%s%2s%6s%4s%s%2s%6s%4s%s%2s%4s%4s%s%2s%5s%4s%s%4s%3s", " |  NO |          |                                 |   ", startyear-1, "-", Integer.parseInt(endyearsubset)-1, "  |   ", startyear, "-", endyearsubset, "  |   ",startyear, "-", endyearsubset, "  |  ", startyear, "-", endyearsubset, "  | ", startyear + 1, "-", startyear + 2, "  |");
//        pw.printf("%56s%4s%s%2s%6s%4s%s%2s%6s%4s%s%2s%4s%4s%s%4s%3s", " |  NO |          |                                 |   ", startyear-1, "-", Integer.parseInt(endyearsubset)-1, "  |   ", startyear, "-", endyearsubset, "  |   ", startyear, "-", endyearsubset, "  | ", startyear + 1, "-", startyear + 2, "  |");
        pw.println();
        lineno++;
        pw.printf("%s", " |-----|----------|---------------------------------|------------|------------|------------|------------|------------|");
        pw.println();
        lineno++;
    }

    public void budgetReportGroupTotal(PrintWriter pw) {
        pw.printf("%s", " |-----|----------|---------------------------------|------------|------------|------------|------------|------------|");
        pw.println();
        pw.printf("%54s%10s%3s%10s%3s%10s%3s%13s%10s%2s", " |     |          |         GROUP TOTAL             | ", decimalFormat.format(groupPreviousbudgetestimate), " | ", decimalFormat.format(groupbudgetestimate), " | ", decimalFormat.format(groupHorevisedestimate), " | ","           | ", decimalFormat.format(groupHobudgetestimate), " |");
        pw.println();
        lineno++;
        pw.printf("%s", " |-----|----------|---------------------------------|------------|------------|------------|------------|------------|");
        pw.println();
        lineno++;
        groupHorevisedestimate = 0;
        groupHobudgetestimate = 0;
        groupbudgetestimate = 0;
        groupPreviousbudgetestimate = 0;
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
            pw.printf("%s", " |-----|----------|---------------------------------|------------|------------|------------|------------|------------|");
            pw.println();
            pw.printf("%54s%10s%3s%10s%3s%10s%3s%13s%10s%2s", " |     |          |         GROUP TOTAL             | ", decimalFormat.format(groupPreviousbudgetestimate), " | ",decimalFormat.format(groupbudgetestimate), " | ", decimalFormat.format(groupHorevisedestimate), " | ","           | ", decimalFormat.format(groupHobudgetestimate), " |");
            pw.println();
            pw.printf("%s", " |-----|----------|---------------------------------|------------|------------|------------|------------|------------|");
            pw.println();

            pw.printf("%54s%10s%3s%10s%3s%10s%3s%13s%10s%2s", " |     |          |         GRAND TOTAL             | ", decimalFormat.format(grandPreviousbudgetestimate), " | ", decimalFormat.format(grandbudgetestimate), " | ", decimalFormat.format(grandHorevisedestimate), " | ","           | ", decimalFormat.format(grandHobudgetestimate), " |");
            pw.println();
            pw.printf("%s", " |-----|----------|---------------------------------|------------|------------|------------|------------|------------|");

            pw.println();
            pw.flush();
            pw.close();
            groupHorevisedestimate = 0;
            groupHobudgetestimate = 0;
            grandHorevisedestimate = 0;
            grandHobudgetestimate = 0;
            groupbudgetestimate = 0;
            groupPreviousbudgetestimate = 0;
            grandbudgetestimate = 0;
            grandPreviousbudgetestimate = 0;

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
                pw.printf("%20s%-32s%66s", " |     |          | ", psm.getLedgergroupname(), "|            |            |            |            |            |");
                pw.println();
                lineno++;
                pw.printf("%s", " |     |          | ~~~~~~~~~~~~~~~~~~~~~~~~~~      |            |            |            |            |            |");
                pw.println();
                lineno++;
            }

            if (!groupname.equals(psm.getLedgergroupname())) {
                budgetReportGroupTotal(pw);
                if (lineno >= 59) {
                    pw.println();
                    pw.write(FORM_FEED);
                    lineno = 1;
                    budgetReportHeader(pw, psm);
                }
                pw.printf("%20s%-32s%66s", " |     |          | ", psm.getLedgergroupname(), "|            |            |            |            |            |");
                pw.println();
                lineno++;
                pw.printf("%s", " |     |          | ~~~~~~~~~~~~~~~~~~~~~~~~~~      |            |            |            |            |            |");
                pw.println();
                lineno++;
                groupname = psm.getLedgergroupname();
            }

            if (lineno >= 61) {
                pw.println();
                pw.write(FORM_FEED);
                lineno = 1;
                budgetReportHeader(pw, psm);
            }

            pw.printf("%3s%3s%3s%-8s%2s%-33s%2s%10s%2s%10s%3s%10s%3s%13s%10s%2s", " | ", psm.getSlipno(), ".| ",psm.getAcccode()," |", psm.getLedgername(), "| ", psm.getThirdactual(), " | ", psm.getBudgetestimate(), " | ", psm.getHorevisedestimate(), " | ", "           | ", psm.getHobudgetestimate(), " |");
            pw.println();
            lineno++;

            if (lineno >= 61) {
                pw.println();
                pw.write(FORM_FEED);
                lineno = 1;
                budgetReportHeader(pw, psm);
            }

            pw.printf("%s", " |     |          |                                 |            |            |            |            |            |");
            pw.println();
            lineno++;

            if (lineno >= 61) {
                pw.println();
                pw.write(FORM_FEED);
                lineno = 1;
                budgetReportHeader(pw, psm);
            }

            if (psm.getHorevisedestimate() != null || psm.getHorevisedestimate().length() > 0) {
                groupHorevisedestimate += Double.valueOf(psm.getHorevisedestimate());
            } else {
                groupHorevisedestimate += 0;
            }

            if (psm.getHobudgetestimate() != null || psm.getHobudgetestimate().length() > 0) {
                groupHobudgetestimate += Double.valueOf(psm.getHobudgetestimate());
            } else {
                groupHobudgetestimate += 0;
            }

            if (psm.getBudgetestimate() != null || psm.getBudgetestimate().length() > 0) {
                groupbudgetestimate += Double.valueOf(psm.getBudgetestimate());
            } else {
                groupbudgetestimate += 0;
            }
            
            if (psm.getThirdactual() != null || psm.getThirdactual().length() > 0) {
                groupPreviousbudgetestimate += Double.valueOf(psm.getThirdactual());
            } else {
                groupPreviousbudgetestimate += 0;
            }

            if (psm.getHorevisedestimate() != null || psm.getHorevisedestimate().length() > 0) {
                grandHorevisedestimate += Double.valueOf(psm.getHorevisedestimate());
            } else {
                grandHorevisedestimate += 0;
            }

            if (psm.getHobudgetestimate() != null || psm.getHobudgetestimate().length() > 0) {
                grandHobudgetestimate += Double.valueOf(psm.getHobudgetestimate());
            } else {
                grandHobudgetestimate += 0;
            }

            if (psm.getBudgetestimate() != null || psm.getBudgetestimate().length() > 0) {
                grandbudgetestimate += Double.valueOf(psm.getBudgetestimate());
            } else {
                grandbudgetestimate += 0;
            }
            
            if (psm.getThirdactual() != null || psm.getThirdactual().length() > 0) {
                grandPreviousbudgetestimate += Double.valueOf(psm.getThirdactual());
            } else {
                grandPreviousbudgetestimate += 0;
            }

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
}
