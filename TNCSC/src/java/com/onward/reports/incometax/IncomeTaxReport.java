/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.incometax;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.valueobjects.PaySlipModel;
import com.onward.valueobjects.PaySlip_Earn_Deduction_Model;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

public class IncomeTaxReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[150];
    private char[] horizontalline = new char[150];
    private char[] equalline = new char[150];

    public IncomeTaxReport() {
        for (int i = 0; i < 150; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 150; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 150; i++) {
            equalline[i] = '=';
        }
    }

    public void getIncomeTaxPrintWriter(PaySlipModel psm, String filePath) {
//        System.out.println("***************** IncomeTaxReport class getIncomeTaxPrintWriter method is calling *******************");
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
            pw.printf("%-7s%-17s%-39s%4s%s%2s", "TNCSC.,", psm.getBranch(), "TENTATIVE PAY PARTICULARS FOR THE YEAR ", psm.getPayslipstartingdate(), "-", psm.getPayslipenddate());
            pw.println();
            pw.printf("%-14s%-35s%-8s%-15s", "NAME         :", psm.getEmployeename(), "EPF NO :", psm.getPfno());
            pw.println();
            pw.printf("%-14s%-35s%-13s%-64s%-9s", "DESIGNATION  :", psm.getDesignation(), "SECTION     :", psm.getSectionname(), "");
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            pw.printf("%s", "MONTH           PAY      S.PAY     DA      GRADE PAY    HRA     CCA     OTHERS    GROSS      I.TAX     EPF/GPF   VPF    PROF.TAX       HIS        FBF ");
            pw.println();
            pw.print(horizontalsign);
            pw.println();

            Iterator itr = psm.getRegularlist().iterator();
            while (itr.hasNext()) {
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();
                pw.printf("%-10s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", psedm1.getPeriod(), psedm1.getBasic(), " ", psedm1.getSpay(), " ", psedm1.getDa(), " ", psedm1.getGrpay(), " ", psedm1.getHra(), " ", psedm1.getCca(), " ", psedm1.getOthers(), " ", psedm1.getGross(), " ", psedm1.getItax(), " ", psedm1.getEpf(), " ", psedm1.getVpf(), " ", psedm1.getProftax(), " ", psedm1.getHis(), " ", psedm1.getFbf());
                pw.println();
                pw.println();
            }

            pw.print(horizontalline);
            pw.println();
            pw.printf("%-10s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", "TOTAL", psm.getTotalmap().get("basic_total"), " ", psm.getTotalmap().get("spay_total"), " ", psm.getTotalmap().get("da_total"), " ", psm.getTotalmap().get("grpay_total"), " ", psm.getTotalmap().get("hra_total"), " ", psm.getTotalmap().get("cca_total"), " ", psm.getTotalmap().get("others_total"), " ", psm.getTotalmap().get("gross_total"), " ", psm.getTotalmap().get("it_total"), " ", psm.getTotalmap().get("epf_total"), " ", psm.getTotalmap().get("vpf_total"), " ", psm.getTotalmap().get("proftax_total")," ", psm.getTotalmap().get("his_total")," ", psm.getTotalmap().get("fbf_total"));
            pw.println();
            pw.println();

            itr = psm.getLeavesurrenderlist().iterator();
            while (itr.hasNext()) {
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();
                pw.printf("%-3s%-7s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", "SL", (psedm1.getPeriod() != null) ? psedm1.getPeriod() : "", psedm1.getBasic(), " ", psedm1.getSpay(), " ", psedm1.getDa(), " ", psedm1.getGrpay(), " ", psedm1.getHra(), " ", psedm1.getCca(), " ", psedm1.getOthers(), " ", psedm1.getGross(), " ", psedm1.getItax(), " ", psedm1.getEpf(), " ", psedm1.getVpf(), " ", psedm1.getProftax(), " ", psedm1.getHis(), " ", psedm1.getFbf());
                pw.println();
                pw.println();
            }

            itr = psm.getDaarrearlist().iterator();
            while (itr.hasNext()) {
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();
                pw.printf("%-3s%-7s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", "DA", (psedm1.getPeriod() != null) ? psedm1.getPeriod() : "", psedm1.getBasic(), " ", psedm1.getSpay(),  " ", psedm1.getDa(), " ", psedm1.getGrpay(), " ", psedm1.getHra(), " ", psedm1.getCca(), " ", psedm1.getOthers(), " ", psedm1.getGross(), " ", psedm1.getItax(), " ", psedm1.getEpf(), " ", psedm1.getVpf(), " ", psedm1.getProftax(), " ", psedm1.getHis(), " ", psedm1.getFbf());
                pw.println();
                pw.println();
            }

            itr = psm.getIncrementarrearlist().iterator();
            while (itr.hasNext()) {
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();
                pw.printf("%-3s%-7s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", "IC", (psedm1.getPeriod() != null) ? psedm1.getPeriod() : "", psedm1.getBasic(), " ", psedm1.getSpay(), " ", psedm1.getDa(), " ", psedm1.getGrpay(), " ", psedm1.getHra(), " ", psedm1.getCca(), " ", psedm1.getOthers(), " ", psedm1.getGross(), " ", psedm1.getItax(), " ", psedm1.getEpf(), " ", psedm1.getVpf(), " ", psedm1.getProftax(), " ", psedm1.getHis(), " ", psedm1.getFbf());
                pw.println();
                pw.println();
            }
            
            itr = psm.getBonuslist().iterator();
            while (itr.hasNext()) {
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr.next();
                pw.printf("%-3s%-7s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", psedm1.getPaycodename(), (psedm1.getPeriod() != null) ? psedm1.getPeriod() : "", psedm1.getBasic(), " ", psedm1.getSpay(), " ", psedm1.getDa(), " ", psedm1.getGrpay(), " ", psedm1.getHra(), " ", psedm1.getCca(), " ", psedm1.getOthers(), " ", psedm1.getGross(), " ", psedm1.getItax(), " ", psedm1.getEpf(), " ", psedm1.getVpf(), " ", psedm1.getProftax(), " ", psedm1.getHis(), " ", psedm1.getFbf());
                pw.println();
                pw.println();
            }

            pw.println();
//            pw.printf("%-91s%9s", "BONUS", "0.00");
//            pw.println();
            pw.printf("%s", "");
            pw.println();
            pw.printf("%s", "");
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.printf("%-10s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", "TOTAL", psm.getTotalmap().get("grant_basic_total"), " ", psm.getTotalmap().get("grant_spay_total"), " ", psm.getTotalmap().get("grant_da_total"), " ", psm.getTotalmap().get("grant_grpay_total"), " ", psm.getTotalmap().get("grant_hra_total"), " ", psm.getTotalmap().get("grant_cca_total"), " ", psm.getTotalmap().get("grant_others_total"), " ", psm.getTotalmap().get("grant_gross_total"), " ", psm.getTotalmap().get("grant_it_total"), " ", psm.getTotalmap().get("grant_epf_total"), " ", psm.getTotalmap().get("grant_vpf_total"), " ", psm.getTotalmap().get("grant_proftax_total"), " ", psm.getTotalmap().get("grant_his_total"), " ", psm.getTotalmap().get("grant_fbf_total"));
            pw.println();
            pw.print(horizontalline);
            pw.println();
            pw.print(FORM_FEED);
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void getITPrintWriter(HashMap fmap, String filePath,String reportype) {
//        System.out.println("***************** IncomeTaxReport class getIncomeTaxPrintWriter method is calling *******************");        
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }            
            pw.println();
            //pw.printf("%-7s%-17s%-39s%4s%s%2s", "TNCSC.,", "--", "INCOME TAX PARTICULARS FOR THE YEAR ", psm.getPayslipstartingdate(), "-", "---");
            //pw.println();
            //pw.printf("%-14s%-35s%-8s%-15s", "NAME         :", psm.getEmployeename(), "EPF NO :", psm.getPfno());
            //pw.println();
            //pw.printf("%-14s%-35s%-13s%-64s%-9s", "DESIGNATION  :", psm.getDesignation(), "SECTION     :", psm.getSectionname(), "");
            pw.println();
            pw.print(horizontalsign);
            pw.println();            
            pw.printf("%s", "SNO   EPF NO        NAME                     SECTION       DESIGNATION      PAN NO   TOTAL EARNINGS  I.TAX TOTAL EARNINGS I.TAX TOTAL EARNINGS  I.TAX ");            
            pw.println();
            if ("1".equalsIgnoreCase(reportype)) {
                pw.printf("%98s%-14s%-23s%-23s", "  ", "APR", "MAY", "JUN");
            } else if ("2".equalsIgnoreCase(reportype)) {
                pw.printf("%98s%-14s%-23s%-23s", "  ", "JUL", "AUG", "SEP");
            } else if ("3".equalsIgnoreCase(reportype)) {
                pw.printf("%98s%-14s%-23s%-23s", "  ", "OCT", "NOV", "DEC");
            } else if ("4".equalsIgnoreCase(reportype)) {
                pw.printf("%90s%-19s%-23s%-23s", "  ", "JAN", "FEB", "MAR");
            }
            pw.println();
            pw.print(horizontalsign);
            pw.println();
            for (int loop = 1; loop <= fmap.size(); loop++) {
                PaySlipModel psm = (PaySlipModel) fmap.get(loop);
                String earamount1 = "0.0";
                String earamount2 = "0.0";
                String earamount3 = "0.0";
                String deduction1 = "0.0";
                String deduction2 = "0.0";
                String deduction3 = "0.0";               
                
                PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) psm.getDeduction_map().get("earning1");
                if (psedm1 != null) {
                    earamount1 = psedm1.getEarningsamount();
                }
                PaySlip_Earn_Deduction_Model psedm2 = (PaySlip_Earn_Deduction_Model) psm.getDeduction_map().get("earning2");
                if (psedm2 != null) {
                    earamount2 = psedm2.getEarningsamount();
                }
                PaySlip_Earn_Deduction_Model psedm3 = (PaySlip_Earn_Deduction_Model) psm.getDeduction_map().get("earning3");
                if (psedm3 != null) {
                    earamount3 = psedm3.getEarningsamount();
                }
                PaySlip_Earn_Deduction_Model psedm4 = (PaySlip_Earn_Deduction_Model) psm.getDeduction_map().get("deduction1");
                if (psedm4 != null) {
                    deduction1 = psedm4.getDeductionamount();
                }
                PaySlip_Earn_Deduction_Model psedm5 = (PaySlip_Earn_Deduction_Model) psm.getDeduction_map().get("deduction2");
                if (psedm5 != null) {
                    deduction2 = psedm5.getDeductionamount();
                }
                PaySlip_Earn_Deduction_Model psedm6 = (PaySlip_Earn_Deduction_Model) psm.getDeduction_map().get("deduction3");
                if (psedm6 != null) {
                    deduction3 = psedm6.getDeductionamount();
                }
                pw.printf("%-5s%-11s%-27s%s%-15s%s%-15s%s%-10s%s%9s%s%9s%s%10s%s%9s%s%10s%s%9s", psm.getSlipno(), psm.getPfno(), psm.getEmployeename(), " ", (psm.getSectionname().length()>10)? psm.getSectionname().substring(0, 10): psm.getSectionname() , " ",(psm.getDesignation().length()>10)? psm.getDesignation().subSequence(0, 10) : psm.getDesignation(), " ", psm.getPancardno(), " ",earamount1 , " ", deduction1, " ",earamount2, " ", deduction2, " ",earamount3, " ", deduction3);
                pw.println();
                pw.println();
            }
            pw.print(horizontalline);
            pw.println();
            pw.println();
            pw.print(FORM_FEED);
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
