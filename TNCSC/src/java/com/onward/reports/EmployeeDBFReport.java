/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports;

/**
 *
 * @author Prince vijayakumar.M
 */
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
import java.util.List;
import java.util.Set;
import org.jboss.management.j2ee.JVM;

public class EmployeeDBFReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[140];
    private char[] horizontalline = new char[140];
    private char[] equalline = new char[140];

    public EmployeeDBFReport() {
        for (int i = 0; i < 140; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 140; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 140; i++) {
            equalline[i] = '=';
        }

    }

    public void getEmployeePrintWriter(List<PaySlipModel> emplist, String filePath, String reportType) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            PaySlipModel psm = null;
            Iterator itr = emplist.iterator();
            if ("DBF".equalsIgnoreCase(reportType)) {
                while (itr.hasNext()) {
                    psm = (PaySlipModel) itr.next();
                    pw.printf("%-7s%-3s%-3s%-35s%-4s%-10s%-14s", psm.getEmpno(), psm.getRegion(), psm.getSectioncode(), psm.getEmployeename(), psm.getDesignation(), psm.getEpf(), psm.getBankaccountno());
                    pw.println();
                }
            } else {
                while (itr.hasNext()) {
                    psm = (PaySlipModel) itr.next();
                    pw.printf("%-37s", psm.getRegion() + "," + psm.getEpf() + "," + psm.getEmployeename() + "," + psm.getDesignation() + "," + psm.getSectionname() + "," + psm.getBankaccountno() + "," + psm.getPaymentmode() + "," + psm.getPayscale());
                    pw.println();
                }
            }

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    
}
