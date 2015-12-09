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

public class BudgetFMAReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[210];
    private char[] horizontalline = new char[210];
    private char[] equalline = new char[210];
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
    private double groupHorevisedestimate = 0;
    private double groupHobudgetestimate = 0;
    private double grandFirstactual = 0;
    private double grandSecondactual = 0;
    private double grandThirdactual = 0;
    private double grandAverage = 0;
    private double grandBudgetestimate = 0;
    private double grandActualfirsthalf = 0;
    private double grandProbablesecondhalf = 0;
    private double grandRevisedestimate = 0;
    private double grandBudgetestimatenext = 0;
    private double grandHorevisedestimate = 0;
    private double grandHobudgetestimate = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int lineno = 1;
    private double groupFma = 0;
    private double groupHOFma = 0;
    private double grandFma = 0;
    private double grandHoFma = 0;

    public BudgetFMAReport() {
        for (int i = 0; i < 193; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 193; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 193; i++) {
            equalline[i] = '=';
        }
    }

    public void budgetReportHeader(PrintWriter pw, ReportModel psm) {
        int startyear = psm.getStartyear();
        int endyear = psm.getEndyear();
        int nextyear = psm.getEndyear()+1;
        String endyearsubset = String.valueOf(endyear).substring(2, 4);
        String nextyearsubset = String.valueOf(nextyear).substring(2, 4);

        pw.println();
        lineno++;
        pw.printf("%s", "                                              ANNEXURE - IV");
        pw.println();
        lineno++;
        pw.printf("%s%s", "                   TAMIL NADU CIVIL SUPPLIES CORPORATION,", psm.getRegionname());
        pw.println();
        lineno++;
        pw.printf("%44s%4s%s%2s%25s%4s%s%4s%78s", "REVISED ESTIMATE FOR ", startyear, "-", endyearsubset, " AND BUDGET ESTIMATE FOR ", startyear+1, "-", startyear+2, "RUPEES IN LAKHS");
        pw.println();
        lineno++;
        pw.print(horizontalline);
        pw.println();
        lineno++;
        pw.printf("%s", "  SL  HEAD OF                                     ACTUALS IN               AVERAGE FOR   BUDGET EST.    ACTUAL  PROBABLE FOR REV.ESTI.    BUDGET        FMA      Rev.FMA         HEAD OFFICE");
//        pw.printf("%s", "  SL  HEAD OF                                     ACTUALS IN               AVERAGE FOR   BUDGET EST.    ACTUAL  PROBABLE FOR REV.ESTI.    BUDGET          HEAD OFFICE");
        pw.println();
        lineno++;
        pw.printf("%-41s%4s%s%4s%2s%4s%s%4s%2s%4s%s%4s%19s%4s%s%2s%27s%4s%s%2s%17s%4s%s%2s%4s%4s%s%2s%19s", "  NO  ACCOUNT", psm.getFirststartyear(), "-", psm.getFirstendyear(), "  ", psm.getSecondstartyear(), "-", psm.getSecondendyear(), "   ", psm.getThirdstartyear(), "-", psm.getThirdendyear(), "  THREE YEARS   IN ", startyear, "-", endyearsubset, "    UPTO SEP  CT TO MAR    ", startyear, "-", endyearsubset, "    ESTIMATE     ", startyear, "-", endyearsubset,"    ", startyear, "-", endyearsubset,"      R.E.        B.E.");
        pw.println();
        lineno++;
        pw.printf("%173s%4s%s%2s%5s%4s%s%2s", "", startyear, "-", endyearsubset, "     ", startyear + 1, "-", nextyearsubset);
        pw.println();
        lineno++;
        pw.print(horizontalline);
        pw.println();
        lineno++;
    }

    public void budgetReportGroupTotal(PrintWriter pw) {
        pw.printf("%36s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s", "              GROUP TOTAL           ", " ", decimalFormat.format(groupFirstactual), " ", decimalFormat.format(groupSecondactual), " ", decimalFormat.format(groupThirdactual), " ", decimalFormat.format(groupAverage), " ", decimalFormat.format(groupBudgetestimate), " ", decimalFormat.format(groupActualfirsthalf), " ", decimalFormat.format(groupProbablesecondhalf), " ", decimalFormat.format(groupRevisedestimate), " ", decimalFormat.format(groupBudgetestimatenext), " ", decimalFormat.format(groupFma), " ", decimalFormat.format(groupHOFma), " ", decimalFormat.format(groupHorevisedestimate), " ", decimalFormat.format(groupHobudgetestimate));
//        pw.printf("%36s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s", "              GROUP TOTAL           ", " ", decimalFormat.format(groupFirstactual), " ", decimalFormat.format(groupSecondactual), " ", decimalFormat.format(groupThirdactual), " ", decimalFormat.format(groupAverage), " ", decimalFormat.format(groupBudgetestimate), " ", decimalFormat.format(groupActualfirsthalf), " ", decimalFormat.format(groupProbablesecondhalf), " ", decimalFormat.format(groupRevisedestimate), " ", decimalFormat.format(groupBudgetestimatenext), " ", "0.00", " ", "0.00");
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
        groupHorevisedestimate = 0;
        groupHobudgetestimate = 0;
        groupFma = 0;
        groupHOFma = 0;
        
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
            pw.printf("%36s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s", "              GROUP TOTAL           ", " ", decimalFormat.format(groupFirstactual), " ", decimalFormat.format(groupSecondactual), " ", decimalFormat.format(groupThirdactual), " ", decimalFormat.format(groupAverage), " ", decimalFormat.format(groupBudgetestimate), " ", decimalFormat.format(groupActualfirsthalf), " ", decimalFormat.format(groupProbablesecondhalf), " ", decimalFormat.format(groupRevisedestimate), " ", decimalFormat.format(groupBudgetestimatenext), " ", decimalFormat.format(groupFma), " ", decimalFormat.format(groupHOFma), " ", decimalFormat.format(groupHorevisedestimate), " ", decimalFormat.format(groupHobudgetestimate));
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.printf("%36s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s", "              GRAND TOTAL           ", " ", decimalFormat.format(grandFirstactual), " ", decimalFormat.format(grandSecondactual), " ", decimalFormat.format(grandThirdactual), " ", decimalFormat.format(grandAverage), " ", decimalFormat.format(grandBudgetestimate), " ", decimalFormat.format(grandActualfirsthalf), " ", decimalFormat.format(grandProbablesecondhalf), " ", decimalFormat.format(grandRevisedestimate), " ", decimalFormat.format(grandBudgetestimatenext), " ", decimalFormat.format(grandFma), " ", decimalFormat.format(grandHoFma), " ", decimalFormat.format(grandHorevisedestimate), " ", decimalFormat.format(grandHobudgetestimate));
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

    public void getBudgetFMAReportPrintWriter(ReportModel psm, String filePath) {
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

            pw.printf("%4s%-2s%-30s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s", psm.getSlipno(), ". ", psm.getLedgername(), " ", psm.getFirstactual(), " ", psm.getSecondactual(), " ", psm.getThirdactual(), " ", psm.getAverage(), " ", psm.getBudgetestimate(), " ", psm.getActualfirsthalf(), " ", psm.getProbablesecondhalf(), " ", psm.getRevisedestimate(), " ", psm.getBudgetestimatenext()," ", psm.getFma()," ", psm.getHofma(), " ", psm.getHorevisedestimate(), " ", psm.getHobudgetestimate());
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
            groupRevisedestimate += Double.valueOf(psm.getRevisedestimate());
            groupBudgetestimatenext += Double.valueOf(psm.getBudgetestimatenext());

            if (psm.getHorevisedestimate() != null || psm.getHorevisedestimate().length()>0) {
                groupHorevisedestimate += Double.valueOf(psm.getHorevisedestimate());
            } else {
                groupHorevisedestimate += 0;
            }

            if (psm.getHobudgetestimate() != null || psm.getHobudgetestimate().length()>0) {
                groupHobudgetestimate += Double.valueOf(psm.getHobudgetestimate());
            } else {
                groupHobudgetestimate += 0;
            }
            
            if (psm.getFma() != null || psm.getFma().length()>0) {
                groupFma += Double.valueOf(psm.getFma());
            } else {
                groupFma += 0;
            }
            
            if (psm.getHofma() != null || psm.getHofma().length()>0) {
                groupHOFma += Double.valueOf(psm.getHofma());
            } else {
                groupHOFma += 0;
            }

            grandFirstactual += Double.valueOf(psm.getFirstactual());
            grandSecondactual += Double.valueOf(psm.getSecondactual());
            grandThirdactual += Double.valueOf(psm.getThirdactual());
            grandAverage += Double.valueOf(psm.getAverage());
            grandBudgetestimate += Double.valueOf(psm.getBudgetestimate());
            grandActualfirsthalf += Double.valueOf(psm.getActualfirsthalf());
            grandProbablesecondhalf += Double.valueOf(psm.getProbablesecondhalf());
            grandRevisedestimate += Double.valueOf(psm.getRevisedestimate());
            grandBudgetestimatenext += Double.valueOf(psm.getBudgetestimatenext());

            if (psm.getHorevisedestimate() != null || psm.getHorevisedestimate().length()>0) {
                grandHorevisedestimate += Double.valueOf(psm.getHorevisedestimate());
            } else {
                grandHorevisedestimate += 0;
            }

            if (psm.getHobudgetestimate() != null || psm.getHobudgetestimate().length()>0) {
                grandHobudgetestimate += Double.valueOf(psm.getHobudgetestimate());
            } else {
                grandHobudgetestimate += 0;
            }
            
            if (psm.getFma() != null || psm.getFma().length()>0) {
                grandFma += Double.valueOf(psm.getFma());
            } else {
                grandFma += 0;
            }
            
            if (psm.getHofma() != null || psm.getHofma().length()>0) {
                grandHoFma += Double.valueOf(psm.getHofma());
            } else {
                grandHoFma += 0;
            }


//            pw.printf("%36s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s", "              GROUP TOTAL           ", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00");
//            pw.println();
//            pw.printf("%36s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s%s%11s", "              GRAND TOTAL           ", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00", " ", "       0.00");
//            pw.write(FORM_FEED);

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
    
}
