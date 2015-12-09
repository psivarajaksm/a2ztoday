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

public class SupplementaryDAAbstractReport {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[71];
    private char[] horizontalline = new char[71];
    private char[] equalline = new char[71];
    private double daarrear = 0;
    private double epf = 0;
    private double net = 0;
    DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    public SupplementaryDAAbstractReport() {
        for (int i = 0; i < 71; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 71; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 71; i++) {
            equalline[i] = '=';
        }
    }

    public void grandtotal(String filePath) {
        PrintWriter pw = null;
        File file = new File(filePath);
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        pw.print(horizontalline);
        pw.println();
        pw.printf("%-30s%13s%s%13s%s%13s", "       GRAND TOTAL            ", decimalFormat.format(daarrear), " ", decimalFormat.format(epf), " ", decimalFormat.format(net));
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.print(FORM_FEED);
        pw.flush();
        pw.close();
        daarrear = 0;
        epf = 0;
        net = 0;
    }

    public void header(PrintWriter pw, DaArrearModel dam) {
        pw.println();
        pw.printf("%45s%-25s", "TAMIL NADU CIVIL SUPPLIES CORPORATION, ", dam.getRegion());
        pw.println();
        pw.printf("%42s,%8s,%4s%8s%-15s", "DA ARREAR ABSTRACT FOR THE PERIOD ", dam.getStartmonth()," TO ", dam.getEndmonth(), dam.getPaymenttype());
        pw.println();
        pw.printf("%-11s%-25s", "BATCH.NO : ",dam.getBatchno());
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%s", "SNO    SECTION NAME              DA ARREAR          EPF           NET");
        pw.println();
        pw.print(horizontalsign);
        pw.println();
    }

    public void DAAbstractReportPrint(DaArrearModel dam, String filePath) {
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
                header(pw, dam);
            }

            double netamount = Double.valueOf(dam.getDaarrear()) - Double.valueOf(dam.getEpf());
            pw.printf("%3s%s%-25s%s%13s%s%13s%s%13s", String.valueOf(dam.getSlipno()), " ", dam.getSection(), " ", dam.getDaarrear(), " ", dam.getEpf(), " ", decimalFormat.format(netamount));
            pw.println();
            pw.println();
            daarrear += Double.valueOf(dam.getDaarrear());
            epf += Double.valueOf(dam.getEpf());
            net += netamount;

            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
