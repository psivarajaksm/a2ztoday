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

public class BudgetScheduleReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[66];
    private char[] horizontalline = new char[66];
    private char[] equalline = new char[66];
    private double grandHorevisedestimate = 0;
    private double grandHobudgetestimate = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int lineno = 1;

    public BudgetScheduleReport() {
        for (int i = 0; i < 66; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 66; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 66; i++) {
            equalline[i] = '=';
        }
    }

    public void budgetScheduleReportHeader(PrintWriter pw, ReportModel psm) {
        int startyear = psm.getStartyear();
        int endyear = psm.getEndyear();
        String endyearsubset = String.valueOf(endyear).substring(2, 4);
        String nextyearsubset = String.valueOf(endyear + 1).substring(2, 4);

//        pw.printf("%s", "                           ANNEXURE - IV");
//        pw.println();
        pw.printf("%s", "          TAMIL NADU CIVIL SUPPLIES CORPORATION");
        pw.println();
        pw.printf("%23s%4s%s%2s%25s%4s%s%2s", "  REVISED ESTIMATE FOR ", startyear, "-", endyearsubset, " AND BUDGET ESTIMATE FOR ", endyear, "-", nextyearsubset);
        pw.println();
//        pw.printf("%2s%-40s%s%17s", "", psm.getLedgername(), " ", "(RUPEES IN LAKHS)");
        pw.printf("%17s%-28s%17s", "  ACCOUNT HEAD : ", psm.getLedgername(), "(RUPEES IN LAKHS)");
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%s", "  SL  REGION NAME                            HEAD OFFICE");
        pw.println();
        pw.printf("%s", "  NO                                       R.E.        B.E.");
        pw.println();
        pw.print(horizontalline);
        pw.println();
    }

    public void budgetScheduleReportGrandTotal(String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.printf("%36s%11s%s%11s", "              GRAND TOTAL           ", decimalFormat.format(grandHorevisedestimate), " ", decimalFormat.format(grandHobudgetestimate));
//            pw.printf("%36s%11s%s%11s", "              GRAND TOTAL           ", "250.56", " ", "475.55");
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.write(FORM_FEED);
            pw.flush();
            pw.close();
            grandHorevisedestimate = 0;
            grandHobudgetestimate = 0;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getBudgetScheduleReportPrintWriter(ReportModel psm, String filePath) {
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
                budgetScheduleReportHeader(pw, psm);
            }

            pw.printf("%4s%-2s%-30s%11s%s%11s", psm.getSlipno(), ". ", psm.getRegionname(), psm.getHorevisedestimate(), " ", psm.getHobudgetestimate());
            if (psm.getHorevisedestimate() != null) {
                grandHorevisedestimate += Double.valueOf(psm.getHorevisedestimate());
            } else {
                grandHorevisedestimate += 0;
            }

            if (psm.getHobudgetestimate() != null) {
                grandHobudgetestimate += Double.valueOf(psm.getHobudgetestimate());
            } else {
                grandHobudgetestimate += 0;
            }
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
}
