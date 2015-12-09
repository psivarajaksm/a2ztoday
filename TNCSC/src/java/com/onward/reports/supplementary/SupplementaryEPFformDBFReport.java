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

public class SupplementaryEPFformDBFReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[140];
    private char[] horizontalline = new char[140];
    private char[] equalline = new char[140];

    public SupplementaryEPFformDBFReport() {
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

    public void getEPFformPrintWriter(PaySlipModel psm, String filePath) {
        PaySlip_Earn_Deduction_Model model = new PaySlip_Earn_Deduction_Model();
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            pw.printf("%-3s%-3s%s%-2s%-10s%6s%8s%4s%5s%6s%5s%5s%8s%8s%5s", psm.getBranch(), psm.getPayslipmonth(), "\"", psm.getPayslipyear().substring(2, 4), psm.getPfno(), psm.getSalary(), psm.getEpf(), "0", psm.getEpfloan(), "0", psm.getVpf(), "0", psm.getPercentage367(), psm.getPercentage833(), "0");
            pw.println();
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
