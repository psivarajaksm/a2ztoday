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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jboss.management.j2ee.JVM;

public class SupplementaryDADBFReport {

    DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[94];
    private char[] horizontalline = new char[94];
    private char[] equalline = new char[94];
    private int pgno = 0;
    private double nettotal = 0;

    public SupplementaryDADBFReport() {
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

    public void DABillReportPrint(List<DaArrearModel> dalist, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Iterator itr = dalist.iterator();
            while (itr.hasNext()) {
                DaArrearModel dam = (DaArrearModel) itr.next();
                /*
                 * dam.setEpfno(epfno);
                dam.setRegion(region);
                dam.setSection(section);
                dam.setPaymenttype(paymentmode);
                dam.setDesignation(designation);
                dam.setEmployeename(employeename);
                dam.setBankaccountno(bankaccountno);
                 * dam.setDaarrear(decimalFormat.format(daamount));
                dam.setEpf(decimalFormat.format(epfamount));
                 */
                BigDecimal b= new BigDecimal(Double.valueOf(dam.getEpf()));
                b = b.setScale(0, RoundingMode.HALF_UP);
                
                double netamount = Double.valueOf(dam.getDaarrear()) - b.doubleValue();
                pw.printf("%-13s%2s%-20s%-30s%14s", (dam.getBankaccountno()==null)?"":dam.getBankaccountno(), dam.getSectioncode().substring(1, 3), dam.getSection(), dam.getEmployeename(), decimalFormat.format(netamount));
                pw.println();
                nettotal += netamount;
            }
            System.out.println("nettotal  = "+nettotal);
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
