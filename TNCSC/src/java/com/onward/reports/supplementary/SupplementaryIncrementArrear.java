/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.supplementary;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.valueobjects.IncrementArrearModel;
import com.onward.valueobjects.IncrementArrearsubModel;
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

public class SupplementaryIncrementArrear {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[130];
    private char[] horizontalline = new char[130];
    private char[] equalline = new char[130];
    private DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private String epfno = "";
    private double duebasic = 0;
    private double duepp = 0;
    private double duegrpay = 0;
    private double duesplpay = 0;
    private double dueda = 0;
    private double duehra = 0;
    private double duecca = 0;
    private double duetotal = 0;
    private double totaldue = 0;
    private double drawnbasic = 0;
    private double drawnpp = 0;
    private double drawngrpay = 0;
    private double drawnsplpay = 0;
    private double drawnda = 0;
    private double drawnhra = 0;
    private double drawncca = 0;
    private double drawntotal = 0;
    private double drawnepf = 0;
    private double totaldrawn = 0;

    public SupplementaryIncrementArrear() {
        for (int i = 0; i < 130; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 130; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 130; i++) {
            equalline[i] = '=';
        }
    }

    public void header(IncrementArrearModel iam, PrintWriter pw) {
        pw.println();
        pw.printf("%38s", "TAMIL NADU CIVIL SUPPLIES CORPORATION");
        pw.println();
        pw.printf("%25s", "INCREMENT ARREAR DETAILS");
        pw.println();
        pw.printf("%9s%-35s%-10s%-15s", " NAME  : ", iam.getName(), "EMP_NO  : ", iam.getEpfno());
        pw.println();
        pw.printf("%-9s%-35s%-11s%-25s", " DESIG : ", iam.getDesignation(), "EMP_SECT : ", iam.getSection());
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%s", "                       MONTH      BASIC       PP      GRPAY    SPL.PAY       DA       HRA       CCA      TOTAL      EPF   PAYMENT");
        pw.println();
        pw.print(horizontalline);
    }

    public void total(IncrementArrearModel iam, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.print(horizontalsign);
            pw.println();
            pw.printf("%-30s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", "TOTAL DUE", decimalFormat.format(duebasic), " ", decimalFormat.format(duepp), " ", decimalFormat.format(duegrpay), " ", decimalFormat.format(duesplpay), " ", decimalFormat.format(dueda), " ", decimalFormat.format(duehra), " ", decimalFormat.format(duecca));
            pw.println();
            pw.printf("%-30s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", "TOTAL DRAWN", decimalFormat.format(drawnbasic), " ", decimalFormat.format(drawnpp), " ", decimalFormat.format(drawngrpay), " ", decimalFormat.format(drawnsplpay), " ", decimalFormat.format(drawnda), " ", decimalFormat.format(drawnhra), " ", decimalFormat.format(drawncca));
            pw.println();
            pw.printf("%-30s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", "TOTAL DIFF.", decimalFormat.format(duebasic - drawnbasic), " ", decimalFormat.format(duepp - drawnpp), " ", decimalFormat.format(duegrpay - drawngrpay), " ", decimalFormat.format(duesplpay - drawnsplpay), " ", decimalFormat.format(dueda - drawnda), " ", decimalFormat.format(duehra - drawnhra), " ", decimalFormat.format(duecca - drawncca), " ", decimalFormat.format(totaldrawn), " ", decimalFormat.format(drawnepf), " ", decimalFormat.format(totaldrawn - drawnepf));
            pw.println();
            pw.print(horizontalsign);
            pw.println();

            duebasic = 0;
            duepp = 0;
            duegrpay = 0;
            duesplpay = 0;
            dueda = 0;
            duehra = 0;
            duecca = 0;
            duetotal = 0;
            drawnbasic = 0;
            drawnpp = 0;
            drawngrpay = 0;
            drawnsplpay = 0;
            drawnda = 0;
            drawnhra = 0;
            drawncca = 0;
            drawntotal = 0;
            drawnepf = 0;
            totaldrawn = 0;

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void getIncrementArrearPrintWriter(IncrementArrearModel iam, String filePath) {
//        System.out.println("***************** SupplementaryAbstractReport class getSalaryAbstractPrintWriter method is calling *******************");
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            if (iam.getSlipno() == 1) {
                header(iam, pw);
                epfno = iam.getEpfno();
            }
            if (!epfno.equals(iam.getEpfno())) {
                total(iam, filePath);
                header(iam, pw);
                epfno = iam.getEpfno();
            }

            double dbasic = 0;
            double dpp = 0;
            double dgrpay = 0;
            double dsplpay = 0;
            double dda = 0;
            double dhra = 0;
            double dcca = 0;
            double dtotal = 0;
            double drbasic = 0;
            double drpp = 0;
            double drgrpay = 0;
            double drsplpay = 0;
            double drda = 0;
            double drhra = 0;
            double drcca = 0;
            double drtotal = 0;
            double drepf = 0;

            pw.println();
            Iterator itr = iam.getIncrementarrearlist().iterator();
            while (itr.hasNext()) {
                IncrementArrearsubModel asub = (IncrementArrearsubModel) itr.next();
                if (asub.getType().equalsIgnoreCase("DUE")) {
                    double total = 0;
                    duebasic += Double.valueOf(asub.getBasic());
                    total += Double.valueOf(asub.getBasic());
                    dbasic += Double.valueOf(asub.getBasic());

                    duepp += Double.valueOf(asub.getPp());
                    total += Double.valueOf(asub.getPp());
                    dpp += Double.valueOf(asub.getPp());

                    duegrpay += Double.valueOf(asub.getGrpay());
                    total += Double.valueOf(asub.getGrpay());
                    dgrpay += Double.valueOf(asub.getGrpay());

                    duesplpay += Double.valueOf(asub.getSplpay());
                    total += Double.valueOf(asub.getSplpay());
                    dsplpay += Double.valueOf(asub.getSplpay());

                    dueda += Double.valueOf(asub.getDa());
                    total += Double.valueOf(asub.getDa());
                    dda += Double.valueOf(asub.getDa());

                    duehra += Double.valueOf(asub.getHra());
                    total += Double.valueOf(asub.getHra());
                    dhra += Double.valueOf(asub.getHra());

                    duecca += Double.valueOf(asub.getCca());
                    total += Double.valueOf(asub.getCca());
                    dcca += Double.valueOf(asub.getCca());

                    duetotal += total;
                    dtotal += total;

                    pw.printf("%-22s%6s%s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", asub.getType(), asub.getPeriod(), " ", asub.getBasic(), " ", asub.getPp(), " ", asub.getGrpay(), " ", asub.getSplpay(), " ", asub.getDa(), " ", asub.getHra(), " ", asub.getCca(), " ", decimalFormat.format(total));


                } else {
                    double total = 0;
                    double eepf = 0;

                    drawnbasic += Double.valueOf(asub.getBasic());
                    total += Double.valueOf(asub.getBasic());
                    drbasic += Double.valueOf(asub.getBasic());

                    drawnpp += Double.valueOf(asub.getPp());
                    total += Double.valueOf(asub.getPp());
                    drpp += Double.valueOf(asub.getPp());

                    drawngrpay += Double.valueOf(asub.getGrpay());
                    total += Double.valueOf(asub.getGrpay());
                    drgrpay += Double.valueOf(asub.getGrpay());

                    drawnsplpay += Double.valueOf(asub.getSplpay());
                    total += Double.valueOf(asub.getSplpay());
                    drsplpay += Double.valueOf(asub.getSplpay());

                    drawnda += Double.valueOf(asub.getDa());
                    total += Double.valueOf(asub.getDa());
                    drda += Double.valueOf(asub.getDa());

                    drawnhra += Double.valueOf(asub.getHra());
                    total += Double.valueOf(asub.getHra());
                    drhra += Double.valueOf(asub.getHra());

                    drawncca += Double.valueOf(asub.getCca());
                    total += Double.valueOf(asub.getCca());
                    drcca += Double.valueOf(asub.getCca());

                    drawntotal += total;
                    drtotal += total;

//                    drawnepf += Double.valueOf(asub.getEpf());

//                    eepf = Double.valueOf(asub.getEpf());
//                    drepf += Double.valueOf(asub.getEpf());

                    pw.printf("%-22s%6s%s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", asub.getType(), asub.getPeriod(), " ", asub.getBasic(), " ", asub.getPp(), " ", asub.getGrpay(), " ", asub.getSplpay(), " ", asub.getDa(), " ", asub.getHra(), " ", asub.getCca(), " ", decimalFormat.format(total), " ", " ", " ", " ");
//                    pw.printf("%-22s%6s%s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", asub.getType(), asub.getPeriod(), " ", asub.getBasic(), " ", asub.getPp(), " ", asub.getGrpay(), " ", asub.getSplpay(), " ", asub.getDa(), " ", asub.getHra(), " ", asub.getCca(), " ", decimalFormat.format(total), " ", asub.getEpf(), " ", decimalFormat.format(total - eepf));
                }
                pw.println();
            }
            drawnepf += Double.valueOf(iam.getEpfamount());
            double epfamount = Double.valueOf(iam.getEpfamount());
//            System.out.println("epfamount = " + epfamount);
            totaldrawn += (dtotal - drtotal);
            pw.printf("%-22s%6s%s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", "DIFF", " ", " ", decimalFormat.format(dbasic - drbasic), " ", decimalFormat.format(dpp - drpp), " ", decimalFormat.format(dgrpay - drgrpay), " ", decimalFormat.format(dsplpay - drsplpay), " ", decimalFormat.format(dda - drda), " ", decimalFormat.format(dhra - drhra), " ", decimalFormat.format(dcca - drcca), " ", decimalFormat.format(dtotal - drtotal), " ", decimalFormat.format(epfamount), " ", decimalFormat.format((dtotal - drtotal) - epfamount));
//            pw.printf("%-22s%6s%s%10s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s%s%9s", "DIFF", " ", " ", decimalFormat.format(drbasic - dbasic), " ", decimalFormat.format(drpp - dpp), " ", decimalFormat.format(drgrpay - dgrpay), " ", decimalFormat.format(drsplpay - dsplpay), " ", decimalFormat.format(drda - dda), " ", decimalFormat.format(drhra - dhra), " ", decimalFormat.format(drcca - dcca), " ", decimalFormat.format(drtotal - dtotal), " ", decimalFormat.format(iam.getEpfamount()), " ", decimalFormat.format((drtotal - dtotal) - drepf));
            pw.println();
            pw.println();
            pw.flush();
            pw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
