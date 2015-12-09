/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.bonus;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.reports.regular.*;
import com.onward.valueobjects.PaySlipModel;
import com.onward.valueobjects.PaySlip_Earn_Deduction_Model;
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

public class BonusBDPReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[80];
    private char[] horizontalline = new char[80];
    private char[] equalline = new char[80];

    public BonusBDPReport() {
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

    public void getBonusBDPPrintWriter(PaySlipModel psm, String filePath) {
//        System.out.println("***************** Abstract class getSalaryAbstractPrintWriter method is calling *******************");
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            pw.println();
//            pw.printf("%-58s%4s%s%4s", "TNCSC  REGULAR & SUPLEMENTARY PAY PARTICULAR FOR THE YEAR", psm.getPayslipstartingdate(), "-", psm.getPayslipenddate());
            pw.printf("%-57s%4s%s%2s%2s%-15s", "TNCSC REGULAR & SUPLEMENTARY PAY PARTICULAR FOR THE YEAR ", psm.getPayslipstartingdate(), "-", psm.getPayslipenddate(), ", ", psm.getRegion());
            pw.println();
            pw.println();
            pw.printf("%s%-12s%s%-30s%s%-15s%s%-15s", " ", psm.getPfno(), " ", psm.getEmployeename(), " ", psm.getDesignation(), " ", psm.getSectionname());
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            pw.printf("%4s%72s", "", "MONTH      BASIC      S.PAY       PP       GR.PAY       DA         TOTAL");
            pw.println();
            pw.print(horizontalsign);
            pw.println();

            Iterator itr = psm.getEarningslist().iterator();
            while (itr.hasNext()) {
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();
                pw.printf("%4s%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%12s", "", psedm1.getPeriod(), " ", psedm1.getBasic(), " ", psedm1.getSpay(), " ", psedm1.getPp(), " ", psedm1.getGrpay(), " ", psedm1.getDa(), " ", psedm1.getTotalamount());
                pw.println();
            }
            pw.print(horizontalline);
            pw.println();
            pw.printf("%4s%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%12s", "", "TOTAL", " ", psm.getTotalmap().get("basic_total"), " ", psm.getTotalmap().get("spay_total"), " ", psm.getTotalmap().get("pp_total"), " ", psm.getTotalmap().get("grpay_total"), " ", psm.getTotalmap().get("da_total"), " ", psm.getTotalmap().get("tot_total"));
            pw.println();
            pw.print(horizontalline);
            pw.println();
            Iterator itr1 = psm.getSupplementarylist().iterator();
            while (itr1.hasNext()) {
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr1.next();
//                pw.printf("%4s%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%12s%s%-3s%s%-6s", "", psedm1.getPeriod(), " ", psedm1.getBasic(), " ", psedm1.getSpay(), " ", psedm1.getPp(), " ", psedm1.getGrpay(), " ", psedm1.getDa(), " ", psedm1.getTotalamount(), " ", (psedm1.getTotalamount().length() > 0) ? "IA" : " ", " ", psedm1.getCalculatedperiod());
                pw.printf("%4s%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%12s%s%-15s", "", psedm1.getPeriod(), " ", psedm1.getBasic(), " ", psedm1.getSpay(), " ", psedm1.getPp(), " ", psedm1.getGrpay(), " ", psedm1.getDa(), " ", psedm1.getTotalamount(), " ", psedm1.getSupplementarytype());
                pw.println();
            }
            pw.print(horizontalline);
            pw.println();
            pw.printf("%4s%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%12s", "", "TOTAL", " ", psm.getTotalmap().get("sup_basictotal"), " ", psm.getTotalmap().get("sup_spaytotal"), " ", psm.getTotalmap().get("sup_pptotal"), " ", psm.getTotalmap().get("sup_grpaytotal"), " ", psm.getTotalmap().get("sup_datotal"), " ", psm.getTotalmap().get("sup_tottotal"));
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.printf("%-10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%12s", "GRANDTOTAL", " ", psm.getTotalmap().get("grant_basictotal"), " ", psm.getTotalmap().get("grant_spaytotal"), " ", psm.getTotalmap().get("grant_pptotal"), " ", psm.getTotalmap().get("grant_grpaytotal"), " ", psm.getTotalmap().get("grant_datotal"), " ", psm.getTotalmap().get("grant_tottotal"));
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.write(FORM_FEED);
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
