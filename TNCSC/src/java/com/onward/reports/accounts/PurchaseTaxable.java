/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.reports.accounts;

/**
 *
 * @author Prince vijayakumar.M
 */
import com.onward.valueobjects.AccountsModel;
import com.onward.valueobjects.AccountsSubModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jboss.management.j2ee.JVM;

public class PurchaseTaxable implements PurchaseInterface {

    private final char[] FORM_FEED = {(char) 12};
    private final char LARGEFONT = (char) 14;
    private final char[] BOLD = {(char) 14, (char) 15};
    private final char RELEASE = (char) 18;
    private char[] horizontalsign = new char[192];
    private char[] equalline = new char[192];
    private char[] horizontalline = new char[192];
    private int lineno = 1;
    private final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
    private int count = 0;
    private int pageno = 1;
    private String percentage;
    private String company;
    private String commodity;
    private long percenquantitytotal = 0;
    private double percenvaluetotal = 0;
    private double percentaxtotal = 0;
    private double percentottotal = 0;
    private long commodquantitytotal = 0;
    private double commodvaluetotal = 0;
    private double commodtaxtotal = 0;
    private double commodtottotal = 0;
    private long companyquantitytotal = 0;
    private double companyvaluetotal = 0;
    private double companytaxtotal = 0;
    private double companytottotal = 0;
    private long grantquantitytotal = 0;
    private double grantvaluetotal = 0;
    private double granttaxtotal = 0;
    private double granttottotal = 0;

    public PurchaseTaxable() {
        for (int i = 0; i < 192; i++) {
            horizontalsign[i] = '~';
        }
        for (int i = 0; i < 192; i++) {
            horizontalline[i] = '-';
        }
        for (int i = 0; i < 192; i++) {
            equalline[i] = '=';
        }
    }

    public void Header(AccountsModel am, PrintWriter pw) {
        pw.println();
        pw.printf("%s", "   TAMIL NADU CIVIL SUPPLIES CORPORATION,");
        pw.println();
        if (am.getBreakuptype().equals("1")) {
            pw.printf("%44s%-5s", "TAXABLE GOODS PURCHASED DURING THE MONTH ", am.getAccountingmonthandyear());
        } else if (am.getBreakuptype().equals("2")) {
            pw.printf("%48s%-5s", "NON-TAXABLE GOODS PURCHASED DURING THE MONTH ", am.getAccountingmonthandyear());
        }
//        pw.printf("%44s%-5s", "TAXABLE GOODS PURCHASED DURING THE MONTH ", am.getAccountingmonthandyear());
        pw.println();
        pw.printf("%23s%-25s", "NAME OF THE REGION: ", am.getRegion());
        pw.println();
        pw.print(horizontalline);
        pw.println();
        pw.printf("%s", "SL  SELLER NAME AND ADDRESS          TIN NO       BILLNO              DATE   CODE   COMMODITY                         QUANTITY     RATE         VALUE  RATE OF           TAX           TOTAL");
        pw.println();
        pw.printf("%s", "NO                                                                                                                                                       TAX");
        pw.println();
        pw.print(horizontalline);
        pw.println();

    }

    public void PercentagewiseTotal(AccountsModel am, PrintWriter pw) {
        pw.println();
        pw.print(equalline);
        pw.println();
        pw.printf("%51s%11s%-5s%-49s%8s%27s%22s%16s", "", "Percentage(", percentage, ") Total", percenquantitytotal, decimalFormat.format(percenvaluetotal), decimalFormat.format(percentaxtotal), decimalFormat.format(percentottotal));
        pw.println();
        pw.print(equalline);
        pw.println();
        percenquantitytotal = 0;
        percenvaluetotal = 0;
        percentaxtotal = 0;
        percentottotal = 0;
    }

    public void CompanywiseTotal(AccountsModel am, PrintWriter pw) {
        if (percentottotal > 0) {
            if ((lineno + 4) > 45) {
                pw.write(FORM_FEED);
                pw.println();
                Header(am, pw);
                lineno = 1;
            } else {
                lineno += 4;
            }
            PercentagewiseTotal(am, pw);
        }
        pw.println();
        pw.print(equalline);
        pw.println();
        String comp = "(" + company + ")";
        pw.printf("%75s%-41s%8s%27s%22s%16s", "Company Total ", comp, companyquantitytotal, decimalFormat.format(companyvaluetotal), decimalFormat.format(companytaxtotal), decimalFormat.format(companytottotal));
        pw.println();
        pw.print(equalline);
        pw.println();

        percenquantitytotal = 0;
        percenvaluetotal = 0;
        percentaxtotal = 0;
        percentottotal = 0;
        companyquantitytotal = 0;
        companyvaluetotal = 0;
        companytaxtotal = 0;
        companytottotal = 0;
    }

    public void CommoditywiseTotal(AccountsModel am, PrintWriter pw) {
        if (percentottotal > 0) {
            if ((lineno + 4) > 45) {
                pw.write(FORM_FEED);
                pw.println();
                Header(am, pw);
                lineno = 1;
            } else {
                lineno += 4;
            }
            PercentagewiseTotal(am, pw);
        }
        if (companytottotal > 0) {
            if ((lineno + 4) > 45) {
                pw.write(FORM_FEED);
                pw.println();
                Header(am, pw);
                lineno = 1;
            } else {
                lineno += 4;
            }
            CompanywiseTotal(am, pw);
        }
        pw.println();
        pw.print(equalline);
        pw.println();
        String comm = "(" + commodity + ")";
        pw.printf("%75s%-41s%8s%27s%22s%16s", "Commodity Total ", comm, commodquantitytotal, decimalFormat.format(commodvaluetotal), decimalFormat.format(commodtaxtotal), decimalFormat.format(commodtottotal));
        pw.println();
        pw.print(equalline);
        pw.println();

        percenquantitytotal = 0;
        percenvaluetotal = 0;
        percentaxtotal = 0;
        percentottotal = 0;
        companyquantitytotal = 0;
        companyvaluetotal = 0;
        companytaxtotal = 0;
        companytottotal = 0;
        commodquantitytotal = 0;
        commodvaluetotal = 0;
        commodtaxtotal = 0;
        commodtottotal = 0;
    }

    public void GrandTotal(AccountsModel am, String filePath) {
        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                if (percentottotal > 0) {
                    if ((lineno + 4) > 45) {
                        pw.write(FORM_FEED);
                        pw.println();
                        Header(am, pw);
                        lineno = 1;
                    } else {
                        lineno += 4;
                    }
                    PercentagewiseTotal(am, pw);
                }
                if (companytottotal > 0) {
                    if ((lineno + 4) > 45) {
                        pw.write(FORM_FEED);
                        pw.println();
                        Header(am, pw);
                        lineno = 1;
                    } else {
                        lineno += 4;
                    }
                    CompanywiseTotal(am, pw);
                }
                if (commodtottotal > 0) {
                    if ((lineno + 4) > 45) {
                        pw.write(FORM_FEED);
                        pw.println();
                        Header(am, pw);
                        lineno = 1;
                    } else {
                        lineno += 4;
                    }
                    CommoditywiseTotal(am, pw);
                }
                pw.println();
                pw.print(equalline);
                pw.println();
                pw.printf("%116s%8s%27s%22s%16s", "Grand Total                                            ", grantquantitytotal, decimalFormat.format(grantvaluetotal), decimalFormat.format(granttaxtotal), decimalFormat.format(granttottotal));
                pw.println();
                pw.print(equalline);
                pw.println();

                percenquantitytotal = 0;
                percenvaluetotal = 0;
                percentaxtotal = 0;
                percentottotal = 0;
                companyquantitytotal = 0;
                companyvaluetotal = 0;
                companytaxtotal = 0;
                companytottotal = 0;
                commodquantitytotal = 0;
                commodvaluetotal = 0;
                commodtaxtotal = 0;
                commodtottotal = 0;
                grantquantitytotal = 0;
                grantvaluetotal = 0;
                granttaxtotal = 0;
                granttottotal = 0;
                pw.flush();
                pw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getPrintWriter(AccountsModel am, String filePath) {

        try {
            PrintWriter pw = null;
            File file = new File(filePath);
            double paymenttotal = 0;
            double adjustmenttotal = 0;
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (lineno > 45) {
                pw.write(FORM_FEED);
                pw.println();
                Header(am, pw);
                lineno = 1;
            }

            if (Integer.valueOf(am.getPageno()) == 1) {
                percentage = am.getTaxpercentage();
                company = am.getCompanyname();
                commodity = am.getCommodityname();
                Header(am, pw);
            }

            if (!commodity.equals(am.getCommodityname())) {
                if ((lineno + 4) > 45) {
                    pw.write(FORM_FEED);
                    pw.println();
                    Header(am, pw);
                    lineno = 1;
                } else {
                    lineno += 4;
                }
                CommoditywiseTotal(am, pw);
                percentage = am.getTaxpercentage();
                company = am.getCompanyname();
                commodity = am.getCommodityname();
            }

            if (!company.equals(am.getCompanyname())) {
                if ((lineno + 4) > 45) {
                    pw.write(FORM_FEED);
                    pw.println();
                    Header(am, pw);
                    lineno = 1;
                } else {
                    lineno += 4;
                }
                CompanywiseTotal(am, pw);
                percentage = am.getTaxpercentage();
                company = am.getCompanyname();
            }

            if (!percentage.equals(am.getTaxpercentage())) {
                if ((lineno + 4) > 45) {
                    pw.write(FORM_FEED);
                    pw.println();
                    Header(am, pw);
                    lineno = 1;
                } else {
                    lineno += 4;
                }
                PercentagewiseTotal(am, pw);
                percentage = am.getTaxpercentage();
            }

            pw.printf("%-3s%s%-33s%s%-13s%s%-14s%s%-10s%s%-6s%s%-34s%s%4s%s%10s%s%15s%s%5s%s%15s%s%15s", am.getPageno(), " ", am.getCompanyname(), " ", am.getTinno(), " ", am.getBillno(), " ", am.getAccdate(), " ", am.getCommoditycode(), " ", am.getCommodityname(), " ", am.getQuantity(), " ", am.getRate(), " ", am.getValue(), " ", am.getTaxpercentage(), " ", am.getTaxamount(), " ", am.getTotalamount());
            pw.println();
            lineno++;
            if (lineno > 45) {
                pw.write(FORM_FEED);
                pw.println();
                Header(am, pw);
                lineno = 1;
            }
            pw.println();
            lineno++;
            if (lineno > 45) {
                pw.write(FORM_FEED);
                pw.println();
                Header(am, pw);
                lineno = 1;
            }

            percenquantitytotal += Double.valueOf(am.getQuantity());
            percenvaluetotal += Double.valueOf(am.getValue());
            percentaxtotal += Double.valueOf(am.getTaxamount());
            percentottotal += Double.valueOf(am.getTotalamount());

            commodquantitytotal += Double.valueOf(am.getQuantity());
            commodvaluetotal += Double.valueOf(am.getValue());
            commodtaxtotal += Double.valueOf(am.getTaxamount());
            commodtottotal += Double.valueOf(am.getTotalamount());

            companyquantitytotal += Double.valueOf(am.getQuantity());
            companyvaluetotal += Double.valueOf(am.getValue());
            companytaxtotal += Double.valueOf(am.getTaxamount());
            companytottotal += Double.valueOf(am.getTotalamount());

            grantquantitytotal += Double.valueOf(am.getQuantity());
            grantvaluetotal += Double.valueOf(am.getValue());
            granttaxtotal += Double.valueOf(am.getTaxamount());
            granttottotal += Double.valueOf(am.getTotalamount());

            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
