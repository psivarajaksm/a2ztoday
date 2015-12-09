/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.supplementary;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.reports.regular.*;
import com.onward.valueobjects.DaArrearModel;
import com.onward.valueobjects.DaArrearSubModel;
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

public class SupplementaryDABillReport {

    DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[94];
    private char[] horizontalline = new char[94];
    private char[] equalline = new char[94];
    private int pgno = 0;
    private long basic_total = 0;
    private long perpay_total = 0;
    private long grpay_total = 0;
    private long due_total = 0;
    private long drawn_total = 0;
    private long arrear_total = 0;
    private long epf_total = 0;
    private long net_total = 0;
    private long basic_sec_total = 0;
    private long perpay_sec_total = 0;
    private long grpay_sec_total = 0;
    private long due_sec_total = 0;
    private long drawn_sec_total = 0;
    private long arrear_sec_total = 0;
    private long epf_sec_total = 0;
    private long net_sec_total = 0;
    private long basic_grand_total = 0;
    private long perpay_grand_total = 0;
    private long grpay_grand_total = 0;
    private long due_grand_total = 0;
    private long drawn_grand_total = 0;
    private long arrear_grand_total = 0;
    private long epf_grand_total = 0;
    private long net_grand_total = 0;
    private String section = "";

    public SupplementaryDABillReport() {
        for (int i = 0; i < 94; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 94; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 94; i++) {
            equalline[i] = '=';
        }

    }

    public void DABillRepor(PrintWriter pw, DaArrearModel dam) {
    }

    public void DABillReportheader(PrintWriter pw, DaArrearModel dam) {
        pw.println();
        pw.printf("%8s%s", "", "TAMILNADU CIVIL SUPPLIES CORPORATION, HEAD OFFICE, CHENNAI.");
        pw.println();
        pw.printf("%16s%-30s%6s%4s%6s", "", "DA ARREAR BILL FOR THE PERIOD ", dam.getStartmonth(), " TO ", dam.getEndmonth());
        pw.println();
//        pw.printf("%-9s%s", "REGION : ", dam.getRegion());
        pw.printf("%-9s%-30s%-17s%-20s", "REGION : ", dam.getRegion(), "BATCH.NO       : ", dam.getBatchno());
        pw.println();
        pw.printf("%-10s%-29s%-17s%s", "EMP.NO. : ", dam.getEpfno(), "NAME           : ", dam.getEmployeename());
        pw.println();
        pw.printf("%-10s%-29s%-17s%s", "SECTION : ", dam.getSection(), "DESIGNATION    : ", dam.getDesignation());
        pw.println();
        pw.print(horizontalsign);
        pw.println();
        pw.printf("%38s%s", "", "DEARNESS ALLOWANCE");
        pw.println();
        if (dam.getSection().equals("DEPUTATION")) {
            pw.printf("%s", " MONTH      BASIC              GRADEPAY        DUE      DRAWN     ARREAR                  NET");
        } else {
            pw.printf("%s", " MONTH      BASIC    PER PAY   GRADEPAY        DUE      DRAWN     ARREAR        EPF       NET");
        }
//        pw.printf("%s", " MONTH      BASIC    PER PAY   GRADEPAY        DUE      DRAWN     ARREAR        EPF       NET");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void DABillReportsectiontotal(DaArrearModel dam, PrintWriter pw) {
        pw.print(equalline);
        pw.println();
        if (dam.getSection().equals("DEPUTATION")) {
            pw.printf("%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "SE.TOT", " ", basic_sec_total, " ", "", " ", grpay_sec_total, " ", due_sec_total, " ", drawn_sec_total, " ", arrear_sec_total, " ", "", " ", net_sec_total);
        } else {
            pw.printf("%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "SE.TOT", " ", basic_sec_total, " ", perpay_sec_total, " ", grpay_sec_total, " ", due_sec_total, " ", drawn_sec_total, " ", arrear_sec_total, " ", epf_sec_total, " ", net_sec_total);
        }
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.print(FORM_FEED);
        basic_total = 0;
        perpay_total = 0;
        grpay_total = 0;
        due_total = 0;
        drawn_total = 0;
        arrear_total = 0;
        epf_total = 0;
        net_total = 0;
        basic_sec_total = 0;
        perpay_sec_total = 0;
        grpay_sec_total = 0;
        due_sec_total = 0;
        drawn_sec_total = 0;
        arrear_sec_total = 0;
        epf_sec_total = 0;
        net_sec_total = 0;
    }

    public void DABillReportGrandtotal(DaArrearModel dam, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.print(equalline);
            pw.println();
            if (dam.getSection().equals("DEPUTATION")) {
                pw.printf("%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "SE.TOT", " ", basic_sec_total, " ", "", " ", grpay_sec_total, " ", due_sec_total, " ", drawn_sec_total, " ", arrear_sec_total, " ", "", " ", net_sec_total);
            } else {
                pw.printf("%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "SE.TOT", " ", basic_sec_total, " ", perpay_sec_total, " ", grpay_sec_total, " ", due_sec_total, " ", drawn_sec_total, " ", arrear_sec_total, " ", epf_sec_total, " ", net_sec_total);
            }

            pw.println();
            pw.print(equalline);
            pw.println();
            if (dam.getSection().equals("DEPUTATION")) {
                pw.printf("%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "GR.TOT", " ", basic_grand_total, " ", "", " ", grpay_grand_total, " ", due_grand_total, " ", drawn_grand_total, " ", arrear_grand_total, " ", "", " ", net_grand_total);
            } else {
                pw.printf("%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "GR.TOT", " ", basic_grand_total, " ", perpay_grand_total, " ", grpay_grand_total, " ", due_grand_total, " ", drawn_grand_total, " ", arrear_grand_total, " ", epf_grand_total, " ", net_grand_total);
            }

            pw.println();
            pw.print(equalline);
            pw.println();
            pw.print(FORM_FEED);
            pw.println();
            pw.println();
            basic_total = 0;
            perpay_total = 0;
            grpay_total = 0;
            due_total = 0;
            drawn_total = 0;
            arrear_total = 0;
            epf_total = 0;
            net_total = 0;

            basic_sec_total = 0;
            perpay_sec_total = 0;
            grpay_sec_total = 0;
            due_sec_total = 0;
            drawn_sec_total = 0;
            arrear_sec_total = 0;
            epf_sec_total = 0;
            net_sec_total = 0;

            basic_grand_total = 0;
            perpay_grand_total = 0;
            grpay_grand_total = 0;
            due_grand_total = 0;
            drawn_grand_total = 0;
            arrear_grand_total = 0;
            epf_grand_total = 0;
            net_grand_total = 0;
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void DABillReportPrint(DaArrearModel dam, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (dam.getSlipno() == 1) {
                section = dam.getSection();
            }
            if (!(dam.getSlipno() % 2 == 0)) {
                pw.println();
                pw.write(FORM_FEED);
            }
            if (!dam.getSection().equals(section)) {
                DABillReportsectiontotal(dam, pw);
                section = dam.getSection();
            }
            DABillReportheader(pw, dam);
            Iterator itr = dam.getDaarrearlist().iterator();
            while (itr.hasNext()) {
                DaArrearSubModel dasm = (DaArrearSubModel) itr.next();
//                String type[] = {"REGULAR", "SUPPLEMENTARY", "INCREMENTARREAR", "LEAVESURRENDER", "SUPLEMENTARYBILL"};
                String type = "";
                if (dasm.getType().equals("SUPPLEMENTARY") || dasm.getType().equals("SUPLEMENTARYBILL")) {
                    type = "SB";
                } else if (dasm.getType().equals("INCREMENTARREAR")) {
                    type = "IA";
                } else if (dasm.getType().equals("LEAVESURRENDER")) {
                    type = "SL";
                } else if (dasm.getType().equals("MANUAL")) {
                    type = "MA";
                }

                long epfAmount = Math.round(Double.valueOf(Double.valueOf(dasm.getEpfamount())));

//                long epfAmount = 0;
//                if (!type.equals("SL")) {
//                    epfAmount = Math.round(Double.valueOf(Double.valueOf(dasm.getEpfamount())));
//                }
                if (dam.getSection().equals("DEPUTATION")) {
                    pw.printf("%6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%3s", dasm.getProcessmonth(), " ", Math.round(Double.valueOf(dasm.getBasic())), " ", "", " ", Math.round(Double.valueOf(dasm.getGrpay())), " ", Math.round(Double.valueOf(dasm.getDue())), " ", Math.round(Double.valueOf(dasm.getDrawn())), " ", Math.round(Double.valueOf(dasm.getArrear())), " ", "", " ", Math.round(Double.valueOf(dasm.getNet())), type);
                } else {
                    pw.printf("%6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%3s", dasm.getProcessmonth(), " ", Math.round(Double.valueOf(dasm.getBasic())), " ", Math.round(Double.valueOf(dasm.getPerpay())), " ", Math.round(Double.valueOf(dasm.getGrpay())), " ", Math.round(Double.valueOf(dasm.getDue())), " ", Math.round(Double.valueOf(dasm.getDrawn())), " ", Math.round(Double.valueOf(dasm.getArrear())), " ", epfAmount, " ", Math.round(Double.valueOf(dasm.getNet())), type);
                }

                basic_total += Math.round(Math.round(Double.valueOf(dasm.getBasic())));
                if (!dam.getSection().equals("DEPUTATION")) {
                    perpay_total += Math.round(Double.valueOf(dasm.getPerpay()));
                }
                grpay_total += Math.round(Double.valueOf(dasm.getGrpay()));
                due_total += Math.round(Double.valueOf(dasm.getDue()));
                drawn_total += Math.round(Double.valueOf(dasm.getDrawn()));
                arrear_total += Math.round(Double.valueOf(dasm.getArrear()));
                if (!dam.getSection().equals("DEPUTATION")) {
                    epf_total += Math.round(Double.valueOf(epfAmount));
                }
                net_total += Math.round(Double.valueOf(dasm.getNet()));

                basic_grand_total += Math.round(Double.valueOf(dasm.getBasic()));
                if (!dam.getSection().equals("DEPUTATION")) {
                    perpay_grand_total += Math.round(Double.valueOf(dasm.getPerpay()));
                }
                grpay_grand_total += Math.round(Double.valueOf(dasm.getGrpay()));
                due_grand_total += Math.round(Double.valueOf(dasm.getDue()));
                drawn_grand_total += Math.round(Double.valueOf(dasm.getDrawn()));
                arrear_grand_total += Math.round(Double.valueOf(dasm.getArrear()));
                if (!dam.getSection().equals("DEPUTATION")) {
                    epf_grand_total += Math.round(Double.valueOf(epfAmount));
                }
                net_grand_total += Math.round(Double.valueOf(dasm.getNet()));


                basic_sec_total += Math.round(Double.valueOf(dasm.getBasic()));
                if (!dam.getSection().equals("DEPUTATION")) {
                    perpay_sec_total += Math.round(Double.valueOf(dasm.getPerpay()));
                }
                grpay_sec_total += Math.round(Double.valueOf(dasm.getGrpay()));
                due_sec_total += Math.round(Double.valueOf(dasm.getDue()));
                drawn_sec_total += Math.round(Double.valueOf(dasm.getDrawn()));
                arrear_sec_total += Math.round(Double.valueOf(dasm.getArrear()));
                if (!dam.getSection().equals("DEPUTATION")) {
                    epf_sec_total += Math.round(Double.valueOf(epfAmount));
                }
                net_sec_total += Math.round(Double.valueOf(dasm.getNet()));

                pw.println();
                pw.println();
            }
            pw.print(horizontalline);
            pw.println();
            if (dam.getSection().equals("DEPUTATION")) {
                pw.printf("%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "TOTAL", " ", basic_total, " ", "", " ", grpay_total, " ", due_total, " ", drawn_total, " ", arrear_total, " ", "", " ", net_total);
            } else {
                pw.printf("%-6s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s%s%10s", "TOTAL", " ", basic_total, " ", perpay_total, " ", grpay_total, " ", due_total, " ", drawn_total, " ", arrear_total, " ", epf_total, " ", net_total);
            }

            pw.println();
            pw.print(equalline);
            pw.println();
            pw.println();
            pw.println();
            pw.println();


            basic_total = 0;
            perpay_total = 0;
            grpay_total = 0;
            due_total = 0;
            drawn_total = 0;
            arrear_total = 0;
            epf_total = 0;
            net_total = 0;
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
