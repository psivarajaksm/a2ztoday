/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.google.inject.*;
import com.onward.action.OnwardAction;
import com.onward.common.AmountInWords;
import com.onward.common.CommonUtility;
import com.onward.common.DateUtility;
import com.onward.persistence.payroll.Accountingyear;
import com.onward.persistence.payroll.Accountsbooks;
import com.onward.persistence.payroll.Accountsheads;
import com.onward.persistence.payroll.Bankchallan;
import com.onward.persistence.payroll.Receiptpaymentdetails;
import com.onward.persistence.payroll.Regionmaster;
import com.onward.reports.accounts.BankChallanCashReport;
import com.onward.reports.accounts.BankChallanChequeReport;
import com.onward.reports.accounts.BankReport;
import com.onward.reports.accounts.CashBookBankReport;
import com.onward.reports.accounts.CashBookJournalReport;
import com.onward.reports.accounts.CashBookReceiptReport;
import com.onward.reports.accounts.CashBookReport;
import com.onward.reports.accounts.ChequeRegisterReport;
import com.onward.reports.accounts.JournalReport;
import com.onward.reports.accounts.LedgerReport;
import com.onward.reports.accounts.LedgerReportIRS;
import com.onward.reports.accounts.PaymentRealizationReport;
import com.onward.reports.accounts.PurchaseAbstractReport;
import com.onward.reports.accounts.PurchaseInterface;
import com.onward.reports.accounts.PurchaseTaxable;
import com.onward.reports.accounts.PurchaseTaxableBreakup;
import com.onward.reports.accounts.PurchaseVatAbstractReport;
import com.onward.reports.accounts.PurchaseVatBreakupReport;
import com.onward.reports.accounts.PurchaseVatExportReport;
import com.onward.reports.accounts.ReceiptRealizationReport;
import com.onward.reports.accounts.ReceiptRealizationReport1;
import com.onward.reports.accounts.ReceiptReport;
import com.onward.reports.accounts.SalesTaxReport;
import com.onward.reports.accounts.SalesVatAbstractReport;
import com.onward.reports.accounts.SalesVatBreakupReport;
import com.onward.reports.accounts.SalesVatExportReport;
import com.onward.reports.accounts.VoucherReport;
import com.onward.valueobjects.AccountsModel;
import com.onward.valueobjects.AccountsSubModel;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Prince vijayakumar M
 */
public class AccountReportServiceImpl extends OnwardAction implements AccountReportService {

    private static final String NEW_LINE_SEPARATOR = "\r\n";

    @GlobalDBOpenCloseAndUserPrivilages
    public Map paymentcashBookPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName) {
        System.out.println("********************* EmployeePayBillServiceImpl class paymentcashBookPrintout method is calling *****************");
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        CashBookReport cbr = new CashBookReport();
        int lineno = 1;
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

//            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherapproveddate,v.narration,ab.bookname,v.voucherno from voucher v "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsbooks ab on ab.code=v.accountbook "
//                    + "where v.region='" + LoggedInRegion + "' and v.voucherapproveddate between '" + postgresDate(startingdate) + "' "
//                    + "and '" + postgresDate(enddate) + "' and v.accountbook='" + cashbooktype + "' and v.cancelled is false and v.vouchertype='P' "
//                    + "order by v.voucherapproveddate";

            String VOUCHER_QUERY = " select id,regionname,voucherapproveddate,narration,bookname,voucherno,vda,vno, cast(replace(replace(replace(replace(replace(replace(replace(xx,'A',''),'S',''),'R',''),'J',''),'E',''),'C',''),'B','') as integer) as xxx from (select v.id as id,rm.regionname as regionname,v.voucherapproveddate as voucherapproveddate,v.narration as narration,ab.bookname as bookname,v.voucherno as voucherno, case when v.voucherapproveddate is null then v.voucherdate else v.voucherapproveddate end as vda, rpad(v.voucherno,8,'0') as vno, case when position('-' in v.voucherno)>0 then trim(split_part(v.voucherno,'-',1)) else case when position('/' in v.voucherno)>=0 then trim(split_part(v.voucherno,'/',1)) else v.voucherno end  end as xx from voucher v  "
                    + " left join regionmaster rm on rm.id=v.region "
                    + " left join accountsbooks ab on ab.code=v.accountbook "
                    + " where v.region='" + LoggedInRegion + "' and "
                    + " case when v.voucherapproveddate is null then v.voucherdate  between '" + postgresDate(startingdate) + "' and '" + postgresDate(enddate) + "'  else v.voucherapproveddate between '" + postgresDate(startingdate) + "' and '" + postgresDate(enddate) + "' end "
                    + " and v.accountbook='" + cashbooktype + "' and v.cancelled is false and v.vouchertype='P' "
                    + " order by v.voucherapproveddate) as x order by vda,xxx,id ";

            SQLQuery voucher_query = session.createSQLQuery(VOUCHER_QUERY);
            //System.out.println("VOUCHER_QUERY ->" + VOUCHER_QUERY);
            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();

                Object[] rows = (Object[]) its.next();
                String vid = (String) rows[0];
//                accountsModel.setVoucherno(vid);
                accountsModel.setRegion((String) rows[1]);
//                accountsModel.setAccdate(rows[2].toString());
                Date date = (Date) rows[6];
                accountsModel.setAccdate(dateToString(date));
                String narration = (String) rows[3];
                narration = "(" + narration + ".)";
                String bookname = (String) rows[4];
                String voucherno = (String) rows[5];
                accountsModel.setVoucherno(voucherno);
                accountsModel.setCompno(vid);
                accountsModel.setCashbookname(cashbookname);
                String checkwithnarration = null;
                List<String> narrationlist = new ArrayList<String>();
//                accountsModel.setDetails((String) rows[3]);
//                System.out.println(vid + "\t" + accountsModel.getRegion() + "\t" + accountsModel.getAccdate() + "\t" + accountsModel.getDetails());

                String[] oprationtype = {"Payment", "Adjustment"};

//                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
//                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                Map<String, List> paymentmap = new HashMap<String, List>();
                Map<String, List> adjustmentmap = new HashMap<String, List>();


                for (int i = 0; i < oprationtype.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Payment and Recipt">
                    String VOUCHERDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,vd.voucheroption,vd.serialno,vd.voucher from voucherdetails vd "
                            + "left join accountsheads ah on ah.acccode=vd.accountcode "
                            + "left join accountgroups ag on ag.id=ah.groupcode "
                            + "where voucher='" + vid + "' and cancelled is false and vd.voucheroption='" + oprationtype[i] + "'";

                    SQLQuery voucherdetails_query = session.createSQLQuery(VOUCHERDETAILS_QUERY);
//                    System.out.println("VOUCHERDETAILS_QUERY ->" + VOUCHERDETAILS_QUERY);
                    for (ListIterator its1 = voucherdetails_query.list().listIterator(); its1.hasNext();) {
                        Object[] row1 = (Object[]) its1.next();
                        AccountsSubModel asm = new AccountsSubModel();
//                    String vid = (String) row1[0];
                        String accountno = (String) row1[0];
                        String accountname = (String) row1[1];
                        String groupname = (String) row1[2];
                        BigDecimal amo = (BigDecimal) row1[3];
                        double amount = amo.doubleValue();
                        String voucheroption = (String) row1[4];
                        int serialno = 0;
                        if (row1[5] != null) {
                            Integer integer = (Integer) row1[5];
                            serialno = integer.intValue();
                        }
                        asm.setAccno(accountno);
                        asm.setAccname(accountname);
                        asm.setGroupname(groupname);
                        asm.setOption(voucheroption);
                        asm.setAmount(decimalFormat.format(amount));
                        asm.setSerialno(String.valueOf(serialno));
                        if (oprationtype[i].equals("Payment")) {
                            if (paymentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> plist = paymentmap.get(asm.getGroupname());
                                plist.add(asm);
                                paymentmap.put(asm.getGroupname(), plist);
                            } else {
                                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
                                PaymentList.add(asm);
                                paymentmap.put(asm.getGroupname(), PaymentList);
                            }
//                            PaymentList.add(asm);
                        } else {
                            if (adjustmentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> alist = adjustmentmap.get(asm.getGroupname());
                                alist.add(asm);
                                adjustmentmap.put(asm.getGroupname(), alist);
                            } else {
                                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                                AdjustmentList.add(asm);
                                adjustmentmap.put(asm.getGroupname(), AdjustmentList);
                            }
//                            AdjustmentList.add(asm);
                        }
//                        System.out.println(accountno + "\t" + accountname + "\t" + groupname + "\t" + amount + "\t" + serialno);
                    }

                    // </editor-fold>
                }
//                System.out.println("******************** PaymentMap Start **********************");
//                Iterator pitr = paymentmap.entrySet().iterator();
//                while (pitr.hasNext()) {
//                    Map.Entry pmap = (Entry) pitr.next();
//                    System.out.println(pmap.getKey());
//                    List plist = (List) pmap.getValue();
//                    Iterator ip = plist.iterator();
//                    while (ip.hasNext()) {
//                        AccountsSubModel as = (AccountsSubModel) ip.next();
//                        System.out.println(as.getGroupname() + " -> " + as.getAccname());
//                    }
//                }
//                System.out.println("******************** PaymentMap End **********************");
//
//
//                System.out.println("******************** AdjustmentMap Start**********************");
//                Iterator aitr = adjustmentmap.entrySet().iterator();
//                while (aitr.hasNext()) {
//                    Map.Entry amap = (Entry) aitr.next();
//                    System.out.println(amap.getKey());
//                    List alist = (List) amap.getValue();
//                    Iterator ia = alist.iterator();
//                    while (ia.hasNext()) {
//                        AccountsSubModel as = (AccountsSubModel) ia.next();
//                        System.out.println(as.getGroupname() + " -> " + as.getAccname());
//                    }
//                }
//                System.out.println("******************** AdjustmentMap End**********************");
//                System.out.println("PaymentList.size() = " + PaymentList.size());
//                System.out.println("AdjustmentList.size() = " + AdjustmentList.size());

                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and region='" + LoggedInRegion + "' and cancelled is false";

//                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
//                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and paymentmode='2' and region='" + LoggedInRegion + "' and cancelled is false";

                SQLQuery chequeno_query = session.createSQLQuery(CHEQUEQUERY);

                if (chequeno_query.list().size() > 0) {
                    StringBuffer chequebuff = new StringBuffer();
                    chequebuff.append("Cheque/DD No:");
                    for (ListIterator it2 = chequeno_query.list().listIterator(); it2.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Cheque No">
                        Object[] row2 = (Object[]) it2.next();
                        String bankname = (String) row2[0];
                        String refno = (String) row2[1];
                        String checkdetails = "";
                        String checkno = "";
                        if (row2[2] != null) {
                            checkdetails = (String) row2[2];
                        }
                        if (row2[3] != null) {
                            checkno = (String) row2[3];
                        }

                        chequebuff.append(refno);
                        chequebuff.append(",");
                        // </editor-fold>
                    }
//                    System.out.println("chequebuff.toString()=" + chequebuff.toString());
                    if (chequebuff.toString().length() > 0) {
                        narrationlist.addAll(JoinString(chequebuff.toString(), 36, ","));
                    }
//                    System.out.println("checkno = " + chequebuff.toString());
                }
                narrationlist.addAll(JoinString(narration, 37, " "));

//                Iterator itr = narrationlist.iterator();
//                while (itr.hasNext()) {
//                    System.out.println(itr.next());
//                }

                accountsModel.setCashbook(bookname);
                accountsModel.setFromdate(startingdate);
                accountsModel.setTodate(enddate);
                accountsModel.setSlipno(String.valueOf(lineno));
                accountsModel.setNarrationlist(narrationlist);
                accountsModel.setPaymentmap(paymentmap);
                accountsModel.setAdjustmentmap(adjustmentmap);
                cbr.getPrintWriter(accountsModel, filePathwithName);
                lineno++;
                // </editor-fold>
            }
            cbr.GrandTotal(accountsModel, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map paymentcashBookPrintoutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName, String selectedRegion) {
        LoggedInRegion = selectedRegion;
        System.out.println("********************* EmployeePayBillServiceImpl class paymentcashBookPrintout method is calling *****************");
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        CashBookReport cbr = new CashBookReport();
        int lineno = 1;
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

//            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherapproveddate,v.narration,ab.bookname,v.voucherno from voucher v "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsbooks ab on ab.code=v.accountbook "
//                    + "where v.region='" + LoggedInRegion + "' and v.voucherapproveddate between '" + postgresDate(startingdate) + "' "
//                    + "and '" + postgresDate(enddate) + "' and v.accountbook='" + cashbooktype + "' and v.cancelled is false and v.vouchertype='P' "
//                    + "order by v.voucherapproveddate";

            String VOUCHER_QUERY = " select id,regionname,voucherapproveddate,narration,bookname,voucherno,vda,vno, cast(replace(replace(replace(replace(replace(replace(replace(xx,'A',''),'S',''),'R',''),'J',''),'E',''),'C',''),'B','') as integer) as xxx from (select v.id as id,rm.regionname as regionname,v.voucherapproveddate as voucherapproveddate,v.narration as narration,ab.bookname as bookname,v.voucherno as voucherno, case when v.voucherapproveddate is null then v.voucherdate else v.voucherapproveddate end as vda, rpad(v.voucherno,8,'0') as vno, case when position('-' in v.voucherno)>0 then trim(split_part(v.voucherno,'-',1)) else case when position('/' in v.voucherno)>=0 then trim(split_part(v.voucherno,'/',1)) else v.voucherno end  end as xx from voucher v  "
                    + " left join regionmaster rm on rm.id=v.region "
                    + " left join accountsbooks ab on ab.code=v.accountbook "
                    + " where v.region='" + LoggedInRegion + "' and "
                    + " case when v.voucherapproveddate is null then v.voucherdate  between '" + postgresDate(startingdate) + "' and '" + postgresDate(enddate) + "'  else v.voucherapproveddate between '" + postgresDate(startingdate) + "' and '" + postgresDate(enddate) + "' end "
                    + " and v.accountbook='" + cashbooktype + "' and v.cancelled is false and v.vouchertype='P' "
                    + " order by v.voucherapproveddate) as x order by vda,xxx,id ";

            SQLQuery voucher_query = session.createSQLQuery(VOUCHER_QUERY);
            //System.out.println("VOUCHER_QUERY ->" + VOUCHER_QUERY);
            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();

                Object[] rows = (Object[]) its.next();
                String vid = (String) rows[0];
//                accountsModel.setVoucherno(vid);
                accountsModel.setRegion((String) rows[1]);
//                accountsModel.setAccdate(rows[2].toString());
                Date date = (Date) rows[6];
                accountsModel.setAccdate(dateToString(date));
                String narration = (String) rows[3];
                narration = "(" + narration + ".)";
                String bookname = (String) rows[4];
                String voucherno = (String) rows[5];
                accountsModel.setVoucherno(voucherno);
                accountsModel.setCompno(vid);
                accountsModel.setCashbookname(cashbookname);
                String checkwithnarration = null;
                List<String> narrationlist = new ArrayList<String>();
//                accountsModel.setDetails((String) rows[3]);
//                System.out.println(vid + "\t" + accountsModel.getRegion() + "\t" + accountsModel.getAccdate() + "\t" + accountsModel.getDetails());

                String[] oprationtype = {"Payment", "Adjustment"};

//                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
//                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                Map<String, List> paymentmap = new HashMap<String, List>();
                Map<String, List> adjustmentmap = new HashMap<String, List>();


                for (int i = 0; i < oprationtype.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Payment and Recipt">
                    String VOUCHERDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,vd.voucheroption,vd.serialno,vd.voucher from voucherdetails vd "
                            + "left join accountsheads ah on ah.acccode=vd.accountcode "
                            + "left join accountgroups ag on ag.id=ah.groupcode "
                            + "where voucher='" + vid + "' and cancelled is false and vd.voucheroption='" + oprationtype[i] + "'";

                    SQLQuery voucherdetails_query = session.createSQLQuery(VOUCHERDETAILS_QUERY);
//                    System.out.println("VOUCHERDETAILS_QUERY ->" + VOUCHERDETAILS_QUERY);
                    for (ListIterator its1 = voucherdetails_query.list().listIterator(); its1.hasNext();) {
                        Object[] row1 = (Object[]) its1.next();
                        AccountsSubModel asm = new AccountsSubModel();
//                    String vid = (String) row1[0];
                        String accountno = (String) row1[0];
                        String accountname = (String) row1[1];
                        String groupname = (String) row1[2];
                        BigDecimal amo = (BigDecimal) row1[3];
                        double amount = amo.doubleValue();
                        String voucheroption = (String) row1[4];
                        int serialno = 0;
                        if (row1[5] != null) {
                            Integer integer = (Integer) row1[5];
                            serialno = integer.intValue();
                        }
                        asm.setAccno(accountno);
                        asm.setAccname(accountname);
                        asm.setGroupname(groupname);
                        asm.setOption(voucheroption);
                        asm.setAmount(decimalFormat.format(amount));
                        asm.setSerialno(String.valueOf(serialno));
                        if (oprationtype[i].equals("Payment")) {
                            if (paymentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> plist = paymentmap.get(asm.getGroupname());
                                plist.add(asm);
                                paymentmap.put(asm.getGroupname(), plist);
                            } else {
                                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
                                PaymentList.add(asm);
                                paymentmap.put(asm.getGroupname(), PaymentList);
                            }
//                            PaymentList.add(asm);
                        } else {
                            if (adjustmentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> alist = adjustmentmap.get(asm.getGroupname());
                                alist.add(asm);
                                adjustmentmap.put(asm.getGroupname(), alist);
                            } else {
                                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                                AdjustmentList.add(asm);
                                adjustmentmap.put(asm.getGroupname(), AdjustmentList);
                            }
//                            AdjustmentList.add(asm);
                        }
//                        System.out.println(accountno + "\t" + accountname + "\t" + groupname + "\t" + amount + "\t" + serialno);
                    }

                    // </editor-fold>
                }
//                System.out.println("******************** PaymentMap Start **********************");
//                Iterator pitr = paymentmap.entrySet().iterator();
//                while (pitr.hasNext()) {
//                    Map.Entry pmap = (Entry) pitr.next();
//                    System.out.println(pmap.getKey());
//                    List plist = (List) pmap.getValue();
//                    Iterator ip = plist.iterator();
//                    while (ip.hasNext()) {
//                        AccountsSubModel as = (AccountsSubModel) ip.next();
//                        System.out.println(as.getGroupname() + " -> " + as.getAccname());
//                    }
//                }
//                System.out.println("******************** PaymentMap End **********************");
//
//
//                System.out.println("******************** AdjustmentMap Start**********************");
//                Iterator aitr = adjustmentmap.entrySet().iterator();
//                while (aitr.hasNext()) {
//                    Map.Entry amap = (Entry) aitr.next();
//                    System.out.println(amap.getKey());
//                    List alist = (List) amap.getValue();
//                    Iterator ia = alist.iterator();
//                    while (ia.hasNext()) {
//                        AccountsSubModel as = (AccountsSubModel) ia.next();
//                        System.out.println(as.getGroupname() + " -> " + as.getAccname());
//                    }
//                }
//                System.out.println("******************** AdjustmentMap End**********************");
//                System.out.println("PaymentList.size() = " + PaymentList.size());
//                System.out.println("AdjustmentList.size() = " + AdjustmentList.size());

                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and region='" + LoggedInRegion + "' and cancelled is false";

//                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
//                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and paymentmode='2' and region='" + LoggedInRegion + "' and cancelled is false";

                SQLQuery chequeno_query = session.createSQLQuery(CHEQUEQUERY);

                if (chequeno_query.list().size() > 0) {
                    StringBuffer chequebuff = new StringBuffer();
                    chequebuff.append("Cheque/DD No:");
                    for (ListIterator it2 = chequeno_query.list().listIterator(); it2.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Cheque No">
                        Object[] row2 = (Object[]) it2.next();
                        String bankname = (String) row2[0];
                        String refno = (String) row2[1];
                        String checkdetails = "";
                        String checkno = "";
                        if (row2[2] != null) {
                            checkdetails = (String) row2[2];
                        }
                        if (row2[3] != null) {
                            checkno = (String) row2[3];
                        }

                        chequebuff.append(refno);
                        chequebuff.append(",");
                        // </editor-fold>
                    }
//                    System.out.println("chequebuff.toString()=" + chequebuff.toString());
                    if (chequebuff.toString().length() > 0) {
                        narrationlist.addAll(JoinString(chequebuff.toString(), 36, ","));
                    }
//                    System.out.println("checkno = " + chequebuff.toString());
                }
                narrationlist.addAll(JoinString(narration, 37, " "));

//                Iterator itr = narrationlist.iterator();
//                while (itr.hasNext()) {
//                    System.out.println(itr.next());
//                }

                accountsModel.setCashbook(bookname);
                accountsModel.setFromdate(startingdate);
                accountsModel.setTodate(enddate);
                accountsModel.setSlipno(String.valueOf(lineno));
                accountsModel.setNarrationlist(narrationlist);
                accountsModel.setPaymentmap(paymentmap);
                accountsModel.setAdjustmentmap(adjustmentmap);
                cbr.getPrintWriter(accountsModel, filePathwithName);
                lineno++;
                // </editor-fold>
            }
            cbr.GrandTotal(accountsModel, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map receiptcashBookPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName) {
        System.out.println("********************* EmployeePayBillServiceImpl class receiptcashBookPrintout method is calling *****************");
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        CashBookReceiptReport cbrr = new CashBookReceiptReport();
        int lineno = 1;
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherdate,v.narration,ab.bookname,v.fileno from voucher v "
                    + "left join regionmaster rm on rm.id=v.region "
                    + "left join accountsbooks ab on ab.code=v.accountbook "
                    + "where v.region='" + LoggedInRegion + "' and v.voucherdate between '" + postgresDate(startingdate) + "' "
                    + "and '" + postgresDate(enddate) + "' and v.accountbook='" + cashbooktype + "' and v.cancelled is false and v.vouchertype='R' "
                    //+ "order by v.voucherdate, cast(replace(replace(v.id,'" + LoggedInRegion + "',''),'R','') as integer)";
                    + "order by v.voucherdate, cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer)";

            SQLQuery voucher_query = session.createSQLQuery(VOUCHER_QUERY);
//            System.out.println("VOUCHER_QUERY ->" + VOUCHER_QUERY);
            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String vid = (String) rows[0];
                accountsModel.setRegion((String) rows[1]);
                Date date = (Date) rows[2];
                accountsModel.setAccdate(dateToString(date));
                String narration = (String) rows[3];
                String bookname = (String) rows[4];
                String fileno = (String) rows[5];

                String FAVOUROF_QUERY = "select favourof from receiptpaymentdetails where voucher='" + vid + "' and cancelled is false group by favourof";

                SQLQuery favourof_query = session.createSQLQuery(FAVOUROF_QUERY);

                StringBuilder favourOf = new StringBuilder();

                for (ListIterator its1 = favourof_query.list().listIterator(); its1.hasNext();) {
                    String ro = (String) its1.next();
                    favourOf.append(ro);
                    favourOf.append(" ");
                }

                narration = "(" + favourOf.toString().trim() + " - " + fileno.trim() + ")";
//                narration = "(" + favourOf.toString().trim() + " - " + fileno.trim() + " - " + narration.trim() + ")";
                accountsModel.setVoucherno(vid);
                accountsModel.setCompno(vid);
                accountsModel.setCashbookname(cashbookname);
                String checkwithnarration = null;
                List<String> narrationlist = new ArrayList<String>();

//                List<AccountsSubModel> CreditList = new ArrayList<AccountsSubModel>();
                Map<String, List> Creditmap = new HashMap<String, List>();
                String VOUCHERDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,vd.voucheroption,"
                        + "vd.serialno,vd.voucher,ah.groupcode from voucherdetails vd "
                        + "left join accountsheads ah on ah.acccode=vd.accountcode "
                        + "left join accountgroups ag on ag.id=ah.groupcode "
                        + "where voucher='" + vid + "' and cancelled is false and vd.voucheroption='Receipt'";

                SQLQuery voucherdetails_query = session.createSQLQuery(VOUCHERDETAILS_QUERY);
                for (ListIterator its1 = voucherdetails_query.list().listIterator(); its1.hasNext();) {
                    Object[] row1 = (Object[]) its1.next();
                    AccountsSubModel asm = new AccountsSubModel();
                    String accountno = (String) row1[0];
                    String accountname = (String) row1[1];
                    String groupname = (String) row1[2];
                    BigDecimal amo = (BigDecimal) row1[3];
                    double amount = amo.doubleValue();
                    String voucheroption = (String) row1[4];
                    int serialno = 0;
                    if (row1[5] != null) {
                        Integer integer = (Integer) row1[5];
                        serialno = integer.intValue();
                    }
                    String groupcode = (String) row1[7];
                    asm.setAccno(accountno);
                    asm.setAccname(accountname);
                    asm.setGroupname(groupname);
                    asm.setOption(voucheroption);
                    asm.setAmount(decimalFormat.format(amount));
                    asm.setSerialno(String.valueOf(serialno));
                    asm.setGroupcode(groupcode);
//                    CreditList.add(asm);

                    if (Creditmap.get(asm.getGroupname()) != null) {
                        List<AccountsSubModel> clist = Creditmap.get(asm.getGroupname());
                        clist.add(asm);
                        Creditmap.put(asm.getGroupname(), clist);
                    } else {
                        List<AccountsSubModel> CreditList = new ArrayList<AccountsSubModel>();
                        CreditList.add(asm);
                        Creditmap.put(asm.getGroupname(), CreditList);
                    }

                }

                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno, realizeddate "
                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and region='" + LoggedInRegion + "' and cancelled is false";


                SQLQuery chequeno_query = session.createSQLQuery(CHEQUEQUERY);

                String realizationdate = "";

                if (chequeno_query.list().size() > 0) {
                    StringBuffer chequebuff = new StringBuffer();
                    chequebuff.append("Cheque/DD No:");
                    for (ListIterator it2 = chequeno_query.list().listIterator(); it2.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Cheque No">
                        Object[] row2 = (Object[]) it2.next();
                        String bankname = (String) row2[0];
                        String refno = (String) row2[1];
                        String checkdetails = "";
                        String checkno = "";
                        if (row2[2] != null) {
                            checkdetails = (String) row2[2];
                        }
                        if (row2[3] != null) {
                            checkno = (String) row2[3];
                        }
                        if (row2[4] != null) {
                            Date realdate = (Date) row2[4];
                            realizationdate = dateToString(realdate);
                        }
                        chequebuff.append(refno);
                        chequebuff.append(",");
                        // </editor-fold>
                    }
//                    System.out.println("chequebuff.toString()=" + chequebuff.toString());
                    if (chequebuff.toString().length() > 0) {
                        narrationlist.addAll(JoinString(chequebuff.toString(), 36, ","));
                    }
                }
                narrationlist.addAll(JoinString(narration, 37, " "));

//                accountsModel.setCreditlist(CreditList);
                accountsModel.setCreditmap(Creditmap);
                accountsModel.setCashbook(bookname);
                accountsModel.setFromdate(startingdate);
                accountsModel.setTodate(enddate);
                accountsModel.setSlipno(String.valueOf(lineno));
                accountsModel.setNarrationlist(narrationlist);
                accountsModel.setRealizationdate(realizationdate);
                cbrr.getPrintWriter(accountsModel, filePathwithName);
                lineno++;
                // </editor-fold>
            }
            cbrr.GrandTotal(accountsModel, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map receiptcashBookPrintoutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName, String selectedRegion) {
        System.out.println("********************* EmployeePayBillServiceImpl class receiptcashBookPrintout method is calling *****************");
        LoggedInRegion = selectedRegion;
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        CashBookReceiptReport cbrr = new CashBookReceiptReport();
        int lineno = 1;
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherdate,v.narration,ab.bookname,v.fileno from voucher v "
                    + "left join regionmaster rm on rm.id=v.region "
                    + "left join accountsbooks ab on ab.code=v.accountbook "
                    + "where v.region='" + LoggedInRegion + "' and v.voucherdate between '" + postgresDate(startingdate) + "' "
                    + "and '" + postgresDate(enddate) + "' and v.accountbook='" + cashbooktype + "' and v.cancelled is false and v.vouchertype='R' "
                    //+ "order by v.voucherdate, cast(replace(replace(v.id,'" + LoggedInRegion + "',''),'R','') as integer)";
                    + "order by v.voucherdate, cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer)";

            SQLQuery voucher_query = session.createSQLQuery(VOUCHER_QUERY);
//            System.out.println("VOUCHER_QUERY ->" + VOUCHER_QUERY);
            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String vid = (String) rows[0];
                accountsModel.setRegion((String) rows[1]);
                Date date = (Date) rows[2];
                accountsModel.setAccdate(dateToString(date));
                String narration = (String) rows[3];
                String bookname = (String) rows[4];
                String fileno = (String) rows[5];

                String FAVOUROF_QUERY = "select favourof from receiptpaymentdetails where voucher='" + vid + "' and cancelled is false group by favourof";

                SQLQuery favourof_query = session.createSQLQuery(FAVOUROF_QUERY);

                StringBuilder favourOf = new StringBuilder();

                for (ListIterator its1 = favourof_query.list().listIterator(); its1.hasNext();) {
                    String ro = (String) its1.next();
                    favourOf.append(ro);
                    favourOf.append(" ");
                }

                narration = "(" + favourOf.toString().trim() + " - " + fileno.trim() + ")";
//                narration = "(" + favourOf.toString().trim() + " - " + fileno.trim() + " - " + narration.trim() + ")";
                accountsModel.setVoucherno(vid);
                accountsModel.setCompno(vid);
                accountsModel.setCashbookname(cashbookname);
                String checkwithnarration = null;
                List<String> narrationlist = new ArrayList<String>();

//                List<AccountsSubModel> CreditList = new ArrayList<AccountsSubModel>();
                Map<String, List> Creditmap = new HashMap<String, List>();
                String VOUCHERDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,vd.voucheroption,"
                        + "vd.serialno,vd.voucher,ah.groupcode from voucherdetails vd "
                        + "left join accountsheads ah on ah.acccode=vd.accountcode "
                        + "left join accountgroups ag on ag.id=ah.groupcode "
                        + "where voucher='" + vid + "' and cancelled is false and vd.voucheroption='Receipt'";

                SQLQuery voucherdetails_query = session.createSQLQuery(VOUCHERDETAILS_QUERY);
                for (ListIterator its1 = voucherdetails_query.list().listIterator(); its1.hasNext();) {
                    Object[] row1 = (Object[]) its1.next();
                    AccountsSubModel asm = new AccountsSubModel();
                    String accountno = (String) row1[0];
                    String accountname = (String) row1[1];
                    String groupname = (String) row1[2];
                    BigDecimal amo = (BigDecimal) row1[3];
                    double amount = amo.doubleValue();
                    String voucheroption = (String) row1[4];
                    int serialno = 0;
                    if (row1[5] != null) {
                        Integer integer = (Integer) row1[5];
                        serialno = integer.intValue();
                    }
                    String groupcode = (String) row1[7];
                    asm.setAccno(accountno);
                    asm.setAccname(accountname);
                    asm.setGroupname(groupname);
                    asm.setOption(voucheroption);
                    asm.setAmount(decimalFormat.format(amount));
                    asm.setSerialno(String.valueOf(serialno));
                    asm.setGroupcode(groupcode);
//                    CreditList.add(asm);

                    if (Creditmap.get(asm.getGroupname()) != null) {
                        List<AccountsSubModel> clist = Creditmap.get(asm.getGroupname());
                        clist.add(asm);
                        Creditmap.put(asm.getGroupname(), clist);
                    } else {
                        List<AccountsSubModel> CreditList = new ArrayList<AccountsSubModel>();
                        CreditList.add(asm);
                        Creditmap.put(asm.getGroupname(), CreditList);
                    }

                }

                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno, realizeddate "
                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and region='" + LoggedInRegion + "' and cancelled is false";


                SQLQuery chequeno_query = session.createSQLQuery(CHEQUEQUERY);

                String realizationdate = "";

                if (chequeno_query.list().size() > 0) {
                    StringBuffer chequebuff = new StringBuffer();
                    chequebuff.append("Cheque/DD No:");
                    for (ListIterator it2 = chequeno_query.list().listIterator(); it2.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Cheque No">
                        Object[] row2 = (Object[]) it2.next();
                        String bankname = (String) row2[0];
                        String refno = (String) row2[1];
                        String checkdetails = "";
                        String checkno = "";
                        if (row2[2] != null) {
                            checkdetails = (String) row2[2];
                        }
                        if (row2[3] != null) {
                            checkno = (String) row2[3];
                        }
                        if (row2[4] != null) {
                            Date realdate = (Date) row2[4];
                            realizationdate = dateToString(realdate);
                        }
                        chequebuff.append(refno);
                        chequebuff.append(",");
                        // </editor-fold>
                    }
//                    System.out.println("chequebuff.toString()=" + chequebuff.toString());
                    if (chequebuff.toString().length() > 0) {
                        narrationlist.addAll(JoinString(chequebuff.toString(), 36, ","));
                    }
                }
                narrationlist.addAll(JoinString(narration, 37, " "));

//                accountsModel.setCreditlist(CreditList);
                accountsModel.setCreditmap(Creditmap);
                accountsModel.setCashbook(bookname);
                accountsModel.setFromdate(startingdate);
                accountsModel.setTodate(enddate);
                accountsModel.setSlipno(String.valueOf(lineno));
                accountsModel.setNarrationlist(narrationlist);
                accountsModel.setRealizationdate(realizationdate);
                cbrr.getPrintWriter(accountsModel, filePathwithName);
                lineno++;
                // </editor-fold>
            }
            cbrr.GrandTotal(accountsModel, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map bankcashBookPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName) {
        System.out.println("********************* EmployeePayBillServiceImpl class bankcashBookPrintout method is calling *****************");
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        CashBookBankReport cbr = new CashBookBankReport();
        int lineno = 1;
        try {

            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherdate,v.narration,ab.bookname,v.voucherno from voucher v "
                    + "left join regionmaster rm on rm.id=v.region "
                    + "left join accountsbooks ab on ab.code=v.accountbook "
                    + "where v.region='" + LoggedInRegion + "' and v.voucherdate between '" + postgresDate(startingdate) + "' "
                    + "and '" + postgresDate(enddate) + "' and v.accountbook='4' and v.cancelled is false and v.vouchertype='P' "
                    //  + "order by v.voucherdate, cast(replace(replace(v.id,'" + LoggedInRegion + "',''),'P','') as integer)";
                    + "order by v.voucherdate, cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'P','') as integer)";

//            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherapproveddate,v.narration,ab.bookname,v.voucherno from voucher v "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsbooks ab on ab.code=v.accountbook "
//                    + "where v.region='" + LoggedInRegion + "' and v.voucherapproveddate between '" + postgresDate(startingdate) + "' "
//                    + "and '" + postgresDate(enddate) + "' and v.accountbook='" + cashbooktype + "' and v.cancelled is false and v.vouchertype='P' "
//                    + "order by v.voucherapproveddate";

            SQLQuery voucher_query = session.createSQLQuery(VOUCHER_QUERY);
//            System.out.println("VOUCHER_QUERY ->" + VOUCHER_QUERY);
            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();

                Object[] rows = (Object[]) its.next();
                String vid = (String) rows[0];
//                accountsModel.setVoucherno(vid);
                accountsModel.setRegion((String) rows[1]);
//                accountsModel.setAccdate(rows[2].toString());
                Date date = (Date) rows[2];
                accountsModel.setAccdate(dateToString(date));
                String narration = (String) rows[3];
                narration = "(" + narration.trim() + ".)";
                String bookname = (String) rows[4];
                String voucherno = (String) rows[5];
                accountsModel.setVoucherno(vid);
                accountsModel.setCompno(vid);
                accountsModel.setCashbookname(cashbookname);
                String checkwithnarration = null;
                List<String> narrationlist = new ArrayList<String>();
//                accountsModel.setDetails((String) rows[3]);
//                System.out.println(vid + "\t" + accountsModel.getRegion() + "\t" + accountsModel.getAccdate() + "\t" + accountsModel.getDetails());

                String[] oprationtype = {"Payment", "Adjustment"};

//                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
//                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                Map<String, List> paymentmap = new HashMap<String, List>();
                Map<String, List> adjustmentmap = new HashMap<String, List>();


                for (int i = 0; i < oprationtype.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Payment and Recipt">
                    String VOUCHERDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,vd.voucheroption,vd.serialno,vd.voucher from voucherdetails vd "
                            + "left join accountsheads ah on ah.acccode=vd.accountcode "
                            + "left join accountgroups ag on ag.id=ah.groupcode "
                            + "where voucher='" + vid + "' and cancelled is false and vd.voucheroption='" + oprationtype[i] + "'";

                    SQLQuery voucherdetails_query = session.createSQLQuery(VOUCHERDETAILS_QUERY);
//                    System.out.println("VOUCHERDETAILS_QUERY ->" + VOUCHERDETAILS_QUERY);
                    for (ListIterator its1 = voucherdetails_query.list().listIterator(); its1.hasNext();) {
                        Object[] row1 = (Object[]) its1.next();
                        AccountsSubModel asm = new AccountsSubModel();
//                    String vid = (String) row1[0];
                        String accountno = (String) row1[0];
                        String accountname = (String) row1[1];
                        String groupname = (String) row1[2];
                        BigDecimal amo = (BigDecimal) row1[3];
                        double amount = amo.doubleValue();
                        String voucheroption = (String) row1[4];
                        int serialno = 0;
                        if (row1[5] != null) {
                            Integer integer = (Integer) row1[5];
                            serialno = integer.intValue();
                        }
                        asm.setAccno(accountno);
                        asm.setAccname(accountname);
                        asm.setGroupname(groupname);
                        asm.setOption(voucheroption);
                        asm.setAmount(decimalFormat.format(amount));
                        asm.setSerialno(String.valueOf(serialno));
                        if (oprationtype[i].equals("Payment")) {
                            if (paymentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> plist = paymentmap.get(asm.getGroupname());
                                plist.add(asm);
                                paymentmap.put(asm.getGroupname(), plist);
                            } else {
                                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
                                PaymentList.add(asm);
                                paymentmap.put(asm.getGroupname(), PaymentList);
                            }
//                            PaymentList.add(asm);
                        } else {
                            if (adjustmentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> alist = adjustmentmap.get(asm.getGroupname());
                                alist.add(asm);
                                adjustmentmap.put(asm.getGroupname(), alist);
                            } else {
                                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                                AdjustmentList.add(asm);
                                adjustmentmap.put(asm.getGroupname(), AdjustmentList);
                            }
//                            AdjustmentList.add(asm);
                        }
//                        if (oprationtype[i].equals("Payment")) {
//                            PaymentList.add(asm);
//                        } else {
//                            AdjustmentList.add(asm);
//                        }
//                        System.out.println(accountno + "\t" + accountname + "\t" + groupname + "\t" + amount + "\t" + serialno);
                    }

                    // </editor-fold>
                }
//                System.out.println("PaymentList.size() = " + PaymentList.size());
//                System.out.println("AdjustmentList.size() = " + AdjustmentList.size());

                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and region='" + LoggedInRegion + "' and cancelled is false";

//                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
//                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and paymentmode='2' and region='" + LoggedInRegion + "' and cancelled is false";

                SQLQuery chequeno_query = session.createSQLQuery(CHEQUEQUERY);

                if (chequeno_query.list().size() > 0) {
                    StringBuffer chequebuff = new StringBuffer();
                    chequebuff.append("Cheque/DD No:");
                    for (ListIterator it2 = chequeno_query.list().listIterator(); it2.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Cheque No">
                        Object[] row2 = (Object[]) it2.next();
                        String bankname = (String) row2[0];
                        String refno = (String) row2[1];
                        String checkdetails = "";
                        String checkno = "";
                        if (row2[2] != null) {
                            checkdetails = (String) row2[2];
                        }
                        if (row2[3] != null) {
                            checkno = (String) row2[3];
                        }

                        chequebuff.append(refno);
                        chequebuff.append(",");
                        // </editor-fold>
                    }
//                    System.out.println("chequebuff.toString()=" + chequebuff.toString());
                    if (chequebuff.toString().length() > 0) {
                        narrationlist.addAll(JoinString(chequebuff.toString(), 36, ","));
                    }
//                    System.out.println("checkno = " + chequebuff.toString());
                }
                narrationlist.addAll(JoinString(narration, 37, " "));

//                Iterator itr = narrationlist.iterator();
//                while (itr.hasNext()) {
//                    System.out.println(itr.next());
//                }

//                accountsModel.setPaymentlist(PaymentList);
//                accountsModel.setAdjustmentlist(AdjustmentList);
                accountsModel.setPaymentmap(paymentmap);
                accountsModel.setAdjustmentmap(adjustmentmap);
                accountsModel.setCashbook(bookname);
                accountsModel.setFromdate(startingdate);
                accountsModel.setTodate(enddate);
                accountsModel.setSlipno(String.valueOf(lineno));
                accountsModel.setNarrationlist(narrationlist);
                cbr.getPrintWriter(accountsModel, filePathwithName);
                lineno++;
                // </editor-fold>
            }
            cbr.GrandTotal(accountsModel, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map bankcashBookPrintoutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName, String selectedRegion) {
        System.out.println("********************* EmployeePayBillServiceImpl class bankcashBookPrintout method is calling *****************");
        LoggedInRegion = selectedRegion;
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        CashBookBankReport cbr = new CashBookBankReport();
        int lineno = 1;
        try {

            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherdate,v.narration,ab.bookname,v.voucherno from voucher v "
                    + "left join regionmaster rm on rm.id=v.region "
                    + "left join accountsbooks ab on ab.code=v.accountbook "
                    + "where v.region='" + LoggedInRegion + "' and v.voucherdate between '" + postgresDate(startingdate) + "' "
                    + "and '" + postgresDate(enddate) + "' and v.accountbook='4' and v.cancelled is false and v.vouchertype='P' "
                    //  + "order by v.voucherdate, cast(replace(replace(v.id,'" + LoggedInRegion + "',''),'P','') as integer)";
                    + "order by v.voucherdate, cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'P','') as integer)";

//            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherapproveddate,v.narration,ab.bookname,v.voucherno from voucher v "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsbooks ab on ab.code=v.accountbook "
//                    + "where v.region='" + LoggedInRegion + "' and v.voucherapproveddate between '" + postgresDate(startingdate) + "' "
//                    + "and '" + postgresDate(enddate) + "' and v.accountbook='" + cashbooktype + "' and v.cancelled is false and v.vouchertype='P' "
//                    + "order by v.voucherapproveddate";

            SQLQuery voucher_query = session.createSQLQuery(VOUCHER_QUERY);
//            System.out.println("VOUCHER_QUERY ->" + VOUCHER_QUERY);
            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();

                Object[] rows = (Object[]) its.next();
                String vid = (String) rows[0];
//                accountsModel.setVoucherno(vid);
                accountsModel.setRegion((String) rows[1]);
//                accountsModel.setAccdate(rows[2].toString());
                Date date = (Date) rows[2];
                accountsModel.setAccdate(dateToString(date));
                String narration = (String) rows[3];
                narration = "(" + narration.trim() + ".)";
                String bookname = (String) rows[4];
                String voucherno = (String) rows[5];
                accountsModel.setVoucherno(vid);
                accountsModel.setCompno(vid);
                accountsModel.setCashbookname(cashbookname);
                String checkwithnarration = null;
                List<String> narrationlist = new ArrayList<String>();
//                accountsModel.setDetails((String) rows[3]);
//                System.out.println(vid + "\t" + accountsModel.getRegion() + "\t" + accountsModel.getAccdate() + "\t" + accountsModel.getDetails());

                String[] oprationtype = {"Payment", "Adjustment"};

//                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
//                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                Map<String, List> paymentmap = new HashMap<String, List>();
                Map<String, List> adjustmentmap = new HashMap<String, List>();


                for (int i = 0; i < oprationtype.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Payment and Recipt">
                    String VOUCHERDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,vd.voucheroption,vd.serialno,vd.voucher from voucherdetails vd "
                            + "left join accountsheads ah on ah.acccode=vd.accountcode "
                            + "left join accountgroups ag on ag.id=ah.groupcode "
                            + "where voucher='" + vid + "' and cancelled is false and vd.voucheroption='" + oprationtype[i] + "'";

                    SQLQuery voucherdetails_query = session.createSQLQuery(VOUCHERDETAILS_QUERY);
//                    System.out.println("VOUCHERDETAILS_QUERY ->" + VOUCHERDETAILS_QUERY);
                    for (ListIterator its1 = voucherdetails_query.list().listIterator(); its1.hasNext();) {
                        Object[] row1 = (Object[]) its1.next();
                        AccountsSubModel asm = new AccountsSubModel();
//                    String vid = (String) row1[0];
                        String accountno = (String) row1[0];
                        String accountname = (String) row1[1];
                        String groupname = (String) row1[2];
                        BigDecimal amo = (BigDecimal) row1[3];
                        double amount = amo.doubleValue();
                        String voucheroption = (String) row1[4];
                        int serialno = 0;
                        if (row1[5] != null) {
                            Integer integer = (Integer) row1[5];
                            serialno = integer.intValue();
                        }
                        asm.setAccno(accountno);
                        asm.setAccname(accountname);
                        asm.setGroupname(groupname);
                        asm.setOption(voucheroption);
                        asm.setAmount(decimalFormat.format(amount));
                        asm.setSerialno(String.valueOf(serialno));
                        if (oprationtype[i].equals("Payment")) {
                            if (paymentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> plist = paymentmap.get(asm.getGroupname());
                                plist.add(asm);
                                paymentmap.put(asm.getGroupname(), plist);
                            } else {
                                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
                                PaymentList.add(asm);
                                paymentmap.put(asm.getGroupname(), PaymentList);
                            }
//                            PaymentList.add(asm);
                        } else {
                            if (adjustmentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> alist = adjustmentmap.get(asm.getGroupname());
                                alist.add(asm);
                                adjustmentmap.put(asm.getGroupname(), alist);
                            } else {
                                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                                AdjustmentList.add(asm);
                                adjustmentmap.put(asm.getGroupname(), AdjustmentList);
                            }
//                            AdjustmentList.add(asm);
                        }
//                        if (oprationtype[i].equals("Payment")) {
//                            PaymentList.add(asm);
//                        } else {
//                            AdjustmentList.add(asm);
//                        }
//                        System.out.println(accountno + "\t" + accountname + "\t" + groupname + "\t" + amount + "\t" + serialno);
                    }

                    // </editor-fold>
                }
//                System.out.println("PaymentList.size() = " + PaymentList.size());
//                System.out.println("AdjustmentList.size() = " + AdjustmentList.size());

                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and region='" + LoggedInRegion + "' and cancelled is false";

//                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
//                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and paymentmode='2' and region='" + LoggedInRegion + "' and cancelled is false";

                SQLQuery chequeno_query = session.createSQLQuery(CHEQUEQUERY);

                if (chequeno_query.list().size() > 0) {
                    StringBuffer chequebuff = new StringBuffer();
                    chequebuff.append("Cheque/DD No:");
                    for (ListIterator it2 = chequeno_query.list().listIterator(); it2.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Cheque No">
                        Object[] row2 = (Object[]) it2.next();
                        String bankname = (String) row2[0];
                        String refno = (String) row2[1];
                        String checkdetails = "";
                        String checkno = "";
                        if (row2[2] != null) {
                            checkdetails = (String) row2[2];
                        }
                        if (row2[3] != null) {
                            checkno = (String) row2[3];
                        }

                        chequebuff.append(refno);
                        chequebuff.append(",");
                        // </editor-fold>
                    }
//                    System.out.println("chequebuff.toString()=" + chequebuff.toString());
                    if (chequebuff.toString().length() > 0) {
                        narrationlist.addAll(JoinString(chequebuff.toString(), 36, ","));
                    }
//                    System.out.println("checkno = " + chequebuff.toString());
                }
                narrationlist.addAll(JoinString(narration, 37, " "));

//                Iterator itr = narrationlist.iterator();
//                while (itr.hasNext()) {
//                    System.out.println(itr.next());
//                }

//                accountsModel.setPaymentlist(PaymentList);
//                accountsModel.setAdjustmentlist(AdjustmentList);
                accountsModel.setPaymentmap(paymentmap);
                accountsModel.setAdjustmentmap(adjustmentmap);
                accountsModel.setCashbook(bookname);
                accountsModel.setFromdate(startingdate);
                accountsModel.setTodate(enddate);
                accountsModel.setSlipno(String.valueOf(lineno));
                accountsModel.setNarrationlist(narrationlist);
                cbr.getPrintWriter(accountsModel, filePathwithName);
                lineno++;
                // </editor-fold>
            }
            cbr.GrandTotal(accountsModel, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map journalcashBookPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName) {
        System.out.println("********************* EmployeePayBillServiceImpl class journalcashBookPrintout method is calling *****************");
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        CashBookJournalReport cbr = new CashBookJournalReport();
        int lineno = 1;
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherdate,v.narration,ab.bookname,v.voucherno from voucher v "
                    + "left join regionmaster rm on rm.id=v.region "
                    + "left join accountsbooks ab on ab.code=v.accountbook "
                    + "where v.region='" + LoggedInRegion + "' and v.voucherdate between '" + postgresDate(startingdate) + "' "
                    + "and '" + postgresDate(enddate) + "' and v.cancelled is false and v.vouchertype='J' "
                    //  + "order by v.voucherdate, cast(replace(replace(v.id,'" + LoggedInRegion + "',''),'J','') as integer)";
                    + "order by v.voucherdate, cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'J','') as integer)";

//            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherapproveddate,v.narration,ab.bookname,v.voucherno from voucher v "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsbooks ab on ab.code=v.accountbook "
//                    + "where v.region='" + LoggedInRegion + "' and v.voucherapproveddate between '" + postgresDate(startingdate) + "' "
//                    + "and '" + postgresDate(enddate) + "' and v.accountbook='" + cashbooktype + "' and v.cancelled is false and v.vouchertype='P' "
//                    + "order by v.voucherapproveddate";

            SQLQuery voucher_query = session.createSQLQuery(VOUCHER_QUERY);
//            System.out.println("VOUCHER_QUERY ->" + VOUCHER_QUERY);
            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();

                Object[] rows = (Object[]) its.next();
                String vid = (String) rows[0];
//                accountsModel.setVoucherno(vid);
                accountsModel.setRegion((String) rows[1]);
//                accountsModel.setAccdate(rows[2].toString());
                Date date = (Date) rows[2];
                accountsModel.setAccdate(dateToString(date));
                String narration = (String) rows[3];
                narration = "(" + narration.trim() + ".)";
                String bookname = (String) rows[4];
                String voucherno = (String) rows[5];
                accountsModel.setVoucherno(vid);
                accountsModel.setCompno(vid);
                accountsModel.setCashbookname(cashbookname);
                String checkwithnarration = null;
                List<String> narrationlist = new ArrayList<String>();
//                accountsModel.setDetails((String) rows[3]);
//                System.out.println(vid + "\t" + accountsModel.getRegion() + "\t" + accountsModel.getAccdate() + "\t" + accountsModel.getDetails());

                String[] oprationtype = {"Debit", "Credit"};

//                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
//                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                Map<String, List> paymentmap = new HashMap<String, List>();
                Map<String, List> adjustmentmap = new HashMap<String, List>();


                for (int i = 0; i < oprationtype.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Payment and Recipt">
                    String VOUCHERDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,vd.voucheroption,vd.serialno,vd.voucher from voucherdetails vd "
                            + "left join accountsheads ah on ah.acccode=vd.accountcode "
                            + "left join accountgroups ag on ag.id=ah.groupcode "
                            + "where voucher='" + vid + "' and cancelled is false and vd.voucheroption='" + oprationtype[i] + "'";

                    SQLQuery voucherdetails_query = session.createSQLQuery(VOUCHERDETAILS_QUERY);
//                    System.out.println("VOUCHERDETAILS_QUERY ->" + VOUCHERDETAILS_QUERY);
                    for (ListIterator its1 = voucherdetails_query.list().listIterator(); its1.hasNext();) {
                        Object[] row1 = (Object[]) its1.next();
                        AccountsSubModel asm = new AccountsSubModel();
//                    String vid = (String) row1[0];
                        String accountno = (String) row1[0];
                        String accountname = (String) row1[1];
                        String groupname = (String) row1[2];
                        BigDecimal amo = (BigDecimal) row1[3];
                        double amount = amo.doubleValue();
                        String voucheroption = (String) row1[4];
                        int serialno = 0;
                        if (row1[5] != null) {
                            Integer integer = (Integer) row1[5];
                            serialno = integer.intValue();
                        }
                        asm.setAccno(accountno);
                        asm.setAccname(accountname);
                        asm.setGroupname(groupname);
                        asm.setOption(voucheroption);
                        asm.setAmount(decimalFormat.format(amount));
                        asm.setSerialno(String.valueOf(serialno));
                        if (oprationtype[i].equals("Debit")) {
                            if (paymentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> plist = paymentmap.get(asm.getGroupname());
                                plist.add(asm);
                                paymentmap.put(asm.getGroupname(), plist);
                            } else {
                                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
                                PaymentList.add(asm);
                                paymentmap.put(asm.getGroupname(), PaymentList);
                            }
//                            PaymentList.add(asm);
                        } else {
                            if (adjustmentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> alist = adjustmentmap.get(asm.getGroupname());
                                alist.add(asm);
                                adjustmentmap.put(asm.getGroupname(), alist);
                            } else {
                                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                                AdjustmentList.add(asm);
                                adjustmentmap.put(asm.getGroupname(), AdjustmentList);
                            }
//                            AdjustmentList.add(asm);
                        }
//                        System.out.println(accountno + "\t" + accountname + "\t" + groupname + "\t" + amount + "\t" + serialno);
                    }

                    // </editor-fold>
                }
//                System.out.println("******************** PaymentMap Start **********************");
//                Iterator pitr = paymentmap.entrySet().iterator();
//                while (pitr.hasNext()) {
//                    Map.Entry pmap = (Entry) pitr.next();
//                    System.out.println(pmap.getKey());
//                    List plist = (List) pmap.getValue();
//                    Iterator ip = plist.iterator();
//                    while (ip.hasNext()) {
//                        AccountsSubModel as = (AccountsSubModel) ip.next();
//                        System.out.println(as.getGroupname() + " -> " + as.getAccname());
//                    }
//                }
//                System.out.println("******************** PaymentMap End **********************");
//
//
//                System.out.println("******************** AdjustmentMap Start**********************");
//                Iterator aitr = adjustmentmap.entrySet().iterator();
//                while (aitr.hasNext()) {
//                    Map.Entry amap = (Entry) aitr.next();
//                    System.out.println(amap.getKey());
//                    List alist = (List) amap.getValue();
//                    Iterator ia = alist.iterator();
//                    while (ia.hasNext()) {
//                        AccountsSubModel as = (AccountsSubModel) ia.next();
//                        System.out.println(as.getGroupname() + " -> " + as.getAccname());
//                    }
//                }
//                System.out.println("******************** AdjustmentMap End**********************");
//                System.out.println("PaymentList.size() = " + PaymentList.size());
//                System.out.println("AdjustmentList.size() = " + AdjustmentList.size());

                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and region='" + LoggedInRegion + "' and cancelled is false";

//                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
//                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and paymentmode='2' and region='" + LoggedInRegion + "' and cancelled is false";

                SQLQuery chequeno_query = session.createSQLQuery(CHEQUEQUERY);

//                System.out.println("CHEQUEQUERY -> " + CHEQUEQUERY);

                if (chequeno_query.list().size() > 0) {
                    StringBuffer chequebuff = new StringBuffer();
                    chequebuff.append("Cheque/DD No:");
                    for (ListIterator it2 = chequeno_query.list().listIterator(); it2.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Cheque No">
                        Object[] row2 = (Object[]) it2.next();
                        String bankname = (String) row2[0];
                        String refno = (String) row2[1];
                        String checkdetails = "";
                        String checkno = "";
                        if (row2[2] != null) {
                            checkdetails = (String) row2[2];
                        }
                        if (row2[3] != null) {
                            checkno = (String) row2[3];
                        }

                        chequebuff.append(refno);
                        chequebuff.append(",");
                        // </editor-fold>
                    }
//                    System.out.println("chequebuff.toString()=" + chequebuff.toString());
                    if (chequebuff.toString().length() > 0) {
                        narrationlist.addAll(JoinString(chequebuff.toString(), 36, ","));
                    }
//                    System.out.println("checkno = " + chequebuff.toString());
                }
                narrationlist.addAll(JoinString(narration, 37, " "));

//                Iterator itr = narrationlist.iterator();
//                while (itr.hasNext()) {
//                    System.out.println(itr.next());
//                }

                accountsModel.setCashbook(bookname);
                accountsModel.setFromdate(startingdate);
                accountsModel.setTodate(enddate);
                accountsModel.setSlipno(String.valueOf(lineno));
                accountsModel.setNarrationlist(narrationlist);
                accountsModel.setPaymentmap(paymentmap);
                accountsModel.setAdjustmentmap(adjustmentmap);
                cbr.getPrintWriter(accountsModel, filePathwithName);
                lineno++;
                // </editor-fold>
            }
            cbr.GrandTotal(accountsModel, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map journalcashBookPrintoutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName, String selectedRegion) {
        System.out.println("********************* EmployeePayBillServiceImpl class journalcashBookPrintout method is calling *****************");
        LoggedInRegion = selectedRegion;
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        CashBookJournalReport cbr = new CashBookJournalReport();
        int lineno = 1;
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherdate,v.narration,ab.bookname,v.voucherno from voucher v "
                    + "left join regionmaster rm on rm.id=v.region "
                    + "left join accountsbooks ab on ab.code=v.accountbook "
                    + "where v.region='" + LoggedInRegion + "' and v.voucherdate between '" + postgresDate(startingdate) + "' "
                    + "and '" + postgresDate(enddate) + "' and v.cancelled is false and v.vouchertype='J' "
                    //  + "order by v.voucherdate, cast(replace(replace(v.id,'" + LoggedInRegion + "',''),'J','') as integer)";
                    + "order by v.voucherdate, cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'J','') as integer)";

//            String VOUCHER_QUERY = "select v.id,rm.regionname,v.voucherapproveddate,v.narration,ab.bookname,v.voucherno from voucher v "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsbooks ab on ab.code=v.accountbook "
//                    + "where v.region='" + LoggedInRegion + "' and v.voucherapproveddate between '" + postgresDate(startingdate) + "' "
//                    + "and '" + postgresDate(enddate) + "' and v.accountbook='" + cashbooktype + "' and v.cancelled is false and v.vouchertype='P' "
//                    + "order by v.voucherapproveddate";

            SQLQuery voucher_query = session.createSQLQuery(VOUCHER_QUERY);
//            System.out.println("VOUCHER_QUERY ->" + VOUCHER_QUERY);
            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();

                Object[] rows = (Object[]) its.next();
                String vid = (String) rows[0];
//                accountsModel.setVoucherno(vid);
                accountsModel.setRegion((String) rows[1]);
//                accountsModel.setAccdate(rows[2].toString());
                Date date = (Date) rows[2];
                accountsModel.setAccdate(dateToString(date));
                String narration = (String) rows[3];
                narration = "(" + narration.trim() + ".)";
                String bookname = (String) rows[4];
                String voucherno = (String) rows[5];
                accountsModel.setVoucherno(vid);
                accountsModel.setCompno(vid);
                accountsModel.setCashbookname(cashbookname);
                String checkwithnarration = null;
                List<String> narrationlist = new ArrayList<String>();
//                accountsModel.setDetails((String) rows[3]);
//                System.out.println(vid + "\t" + accountsModel.getRegion() + "\t" + accountsModel.getAccdate() + "\t" + accountsModel.getDetails());

                String[] oprationtype = {"Debit", "Credit"};

//                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
//                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                Map<String, List> paymentmap = new HashMap<String, List>();
                Map<String, List> adjustmentmap = new HashMap<String, List>();


                for (int i = 0; i < oprationtype.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Payment and Recipt">
                    String VOUCHERDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,vd.voucheroption,vd.serialno,vd.voucher from voucherdetails vd "
                            + "left join accountsheads ah on ah.acccode=vd.accountcode "
                            + "left join accountgroups ag on ag.id=ah.groupcode "
                            + "where voucher='" + vid + "' and cancelled is false and vd.voucheroption='" + oprationtype[i] + "'";

                    SQLQuery voucherdetails_query = session.createSQLQuery(VOUCHERDETAILS_QUERY);
//                    System.out.println("VOUCHERDETAILS_QUERY ->" + VOUCHERDETAILS_QUERY);
                    for (ListIterator its1 = voucherdetails_query.list().listIterator(); its1.hasNext();) {
                        Object[] row1 = (Object[]) its1.next();
                        AccountsSubModel asm = new AccountsSubModel();
//                    String vid = (String) row1[0];
                        String accountno = (String) row1[0];
                        String accountname = (String) row1[1];
                        String groupname = (String) row1[2];
                        BigDecimal amo = (BigDecimal) row1[3];
                        double amount = amo.doubleValue();
                        String voucheroption = (String) row1[4];
                        int serialno = 0;
                        if (row1[5] != null) {
                            Integer integer = (Integer) row1[5];
                            serialno = integer.intValue();
                        }
                        asm.setAccno(accountno);
                        asm.setAccname(accountname);
                        asm.setGroupname(groupname);
                        asm.setOption(voucheroption);
                        asm.setAmount(decimalFormat.format(amount));
                        asm.setSerialno(String.valueOf(serialno));
                        if (oprationtype[i].equals("Debit")) {
                            if (paymentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> plist = paymentmap.get(asm.getGroupname());
                                plist.add(asm);
                                paymentmap.put(asm.getGroupname(), plist);
                            } else {
                                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
                                PaymentList.add(asm);
                                paymentmap.put(asm.getGroupname(), PaymentList);
                            }
//                            PaymentList.add(asm);
                        } else {
                            if (adjustmentmap.get(asm.getGroupname()) != null) {
                                List<AccountsSubModel> alist = adjustmentmap.get(asm.getGroupname());
                                alist.add(asm);
                                adjustmentmap.put(asm.getGroupname(), alist);
                            } else {
                                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();
                                AdjustmentList.add(asm);
                                adjustmentmap.put(asm.getGroupname(), AdjustmentList);
                            }
//                            AdjustmentList.add(asm);
                        }
//                        System.out.println(accountno + "\t" + accountname + "\t" + groupname + "\t" + amount + "\t" + serialno);
                    }

                    // </editor-fold>
                }
//                System.out.println("******************** PaymentMap Start **********************");
//                Iterator pitr = paymentmap.entrySet().iterator();
//                while (pitr.hasNext()) {
//                    Map.Entry pmap = (Entry) pitr.next();
//                    System.out.println(pmap.getKey());
//                    List plist = (List) pmap.getValue();
//                    Iterator ip = plist.iterator();
//                    while (ip.hasNext()) {
//                        AccountsSubModel as = (AccountsSubModel) ip.next();
//                        System.out.println(as.getGroupname() + " -> " + as.getAccname());
//                    }
//                }
//                System.out.println("******************** PaymentMap End **********************");
//
//
//                System.out.println("******************** AdjustmentMap Start**********************");
//                Iterator aitr = adjustmentmap.entrySet().iterator();
//                while (aitr.hasNext()) {
//                    Map.Entry amap = (Entry) aitr.next();
//                    System.out.println(amap.getKey());
//                    List alist = (List) amap.getValue();
//                    Iterator ia = alist.iterator();
//                    while (ia.hasNext()) {
//                        AccountsSubModel as = (AccountsSubModel) ia.next();
//                        System.out.println(as.getGroupname() + " -> " + as.getAccname());
//                    }
//                }
//                System.out.println("******************** AdjustmentMap End**********************");
//                System.out.println("PaymentList.size() = " + PaymentList.size());
//                System.out.println("AdjustmentList.size() = " + AdjustmentList.size());

                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and region='" + LoggedInRegion + "' and cancelled is false";

//                String CHEQUEQUERY = "SELECT bankname, refno, chequedetails, chequeno "
//                        + "FROM receiptpaymentdetails where voucher = '" + vid + "' and paymentmode='2' and region='" + LoggedInRegion + "' and cancelled is false";

                SQLQuery chequeno_query = session.createSQLQuery(CHEQUEQUERY);

//                System.out.println("CHEQUEQUERY -> " + CHEQUEQUERY);

                if (chequeno_query.list().size() > 0) {
                    StringBuffer chequebuff = new StringBuffer();
                    chequebuff.append("Cheque/DD No:");
                    for (ListIterator it2 = chequeno_query.list().listIterator(); it2.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Cheque No">
                        Object[] row2 = (Object[]) it2.next();
                        String bankname = (String) row2[0];
                        String refno = (String) row2[1];
                        String checkdetails = "";
                        String checkno = "";
                        if (row2[2] != null) {
                            checkdetails = (String) row2[2];
                        }
                        if (row2[3] != null) {
                            checkno = (String) row2[3];
                        }

                        chequebuff.append(refno);
                        chequebuff.append(",");
                        // </editor-fold>
                    }
//                    System.out.println("chequebuff.toString()=" + chequebuff.toString());
                    if (chequebuff.toString().length() > 0) {
                        narrationlist.addAll(JoinString(chequebuff.toString(), 36, ","));
                    }
//                    System.out.println("checkno = " + chequebuff.toString());
                }
                narrationlist.addAll(JoinString(narration, 37, " "));

//                Iterator itr = narrationlist.iterator();
//                while (itr.hasNext()) {
//                    System.out.println(itr.next());
//                }

                accountsModel.setCashbook(bookname);
                accountsModel.setFromdate(startingdate);
                accountsModel.setTodate(enddate);
                accountsModel.setSlipno(String.valueOf(lineno));
                accountsModel.setNarrationlist(narrationlist);
                accountsModel.setPaymentmap(paymentmap);
                accountsModel.setAdjustmentmap(adjustmentmap);
                cbr.getPrintWriter(accountsModel, filePathwithName);
                lineno++;
                // </editor-fold>
            }
            cbr.GrandTotal(accountsModel, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbooktype, String voucherno) {
        System.out.println("***************************** AccountReportServiceImpl class getVoucherDetails method is calling ********************************");
        Map map = new HashMap();
        //System.out.println("startingdate = " + startingdate);
        //System.out.println("enddate = " + enddate);
        //System.out.println("voucherno = " + voucherno);
        //System.out.println("cashbooktype = " + cashbooktype);
        try {
            StringBuffer buffer = new StringBuffer();
            String QUERY = null;
            if (voucherno.length() > 0) {
                QUERY = "select v.id,a.bookname,v.narration,v.voucherdate from voucher v "
                        + "left join accountsbooks a on a.code=v.accountbook "
                        + "where v.id='" + voucherno + "' and cancelled is false and v.region='" + LoggedInRegion + "' and vouchertype='P' order by voucherdate";
            } else if (voucherno.length() == 0) {
                QUERY = "select v.id,a.bookname,v.narration,v.voucherdate from voucher v "
                        + "left join accountsbooks a on a.code=v.accountbook "
                        + "where voucherdate between '" + postgresDate(startingdate) + "' and "
                        + "'" + postgresDate(enddate) + "' and accountbook='" + cashbooktype + "' and "
                        + "cancelled is false and v.region='" + LoggedInRegion + "' and vouchertype='P' order by voucherdate";
            }

            SQLQuery voucher_query = session.createSQLQuery(QUERY);

            //System.out.println("voucher_query -> " + QUERY);

            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            } else {
                String vno = null;
                String bookname = null;
                String narration = null;
                String vdate = "";
                String classname = "";

                buffer.append("<table width=\"60%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                buffer.append("<tr class=\"gridmenu\">");
                buffer.append("<td>S.No</td>");
                buffer.append("<td>Voucher No</td>");
                buffer.append("<td>Date</td>");
                buffer.append("<td>Book Name</td>");
                buffer.append("<td>Narration</td>");
                buffer.append("<td width=\"5%\">");
//                buffer.append("<input type=\"checkbox\" name=\"billheader\" id=\"billheader\" value=\"billno\" onclick=\"SelectAll(this)\">");
                buffer.append("</td>");
                buffer.append("</tr>");

                int i = 0;
                for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    vno = (String) rows[0];
                    bookname = (String) rows[1];
                    narration = (String) rows[2];
                    Date date = (Date) rows[3];
                    vdate = dateToString(date);

                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }

                    buffer.append("<tr class=\"" + classname + "\">");
                    buffer.append("<td align=\"center\">" + (i + 1) + "</td>");
                    buffer.append("<td align=\"center\">" + vno + "</td>");
                    buffer.append("<td align=\"center\">" + vdate + "</td>");
                    buffer.append("<td align=\"center\">" + bookname + "</td>");
                    buffer.append("<td align=\"left\">" + narration + "</td>");
                    buffer.append("<td align=\"center\">");
                    String param = vno + "&" + vdate + "&" + cashbooktype;
                    buffer.append("<input type=\"radio\" name=\"vno\" id=\"vno\" value=" + param + ">");
                    buffer.append("</td>");

                    buffer.append("</tr>");
                    i++;
                }
                buffer.append("</table>");
                map.put("vouchergrid", buffer.toString());
            }
        } catch (Exception ex) {
            map.put("ERROR", "Given the Input is Invalid");
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherDetailsByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbooktype, String voucherno, String selectedRegion) {
        System.out.println("***************************** AccountReportServiceImpl class getVoucherDetails method is calling ********************************");
        LoggedInRegion = selectedRegion;
        Map map = new HashMap();
        //System.out.println("startingdate = " + startingdate);
        //System.out.println("enddate = " + enddate);
        //System.out.println("voucherno = " + voucherno);
        //System.out.println("cashbooktype = " + cashbooktype);
        try {
            StringBuffer buffer = new StringBuffer();
            String QUERY = null;
            if (voucherno.length() > 0) {
                QUERY = "select v.id,a.bookname,v.narration,v.voucherdate,v.voucherno from voucher v "
                        + "left join accountsbooks a on a.code=v.accountbook "
                        + "where v.id='" + voucherno + "' and cancelled is false and v.region='" + LoggedInRegion + "' and vouchertype='P' order by voucherdate";
            } else if (voucherno.length() == 0) {
                QUERY = "select v.id,a.bookname,v.narration,v.voucherdate,v.voucherno from voucher v "
                        + "left join accountsbooks a on a.code=v.accountbook "
                        + "where voucherdate between '" + postgresDate(startingdate) + "' and "
                        + "'" + postgresDate(enddate) + "' and accountbook='" + cashbooktype + "' and "
                        + "cancelled is false and v.region='" + LoggedInRegion + "' and vouchertype='P' order by voucherdate";
            }

            SQLQuery voucher_query = session.createSQLQuery(QUERY);

            //System.out.println("voucher_query -> " + QUERY);

            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            } else {
                String vno = null;
                String bookname = null;
                String narration = null;
                String vdate = "";
                String classname = "";
                String vouchernoreg = "";

                buffer.append("<table width=\"90%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                buffer.append("<tr class=\"gridmenu\">");
                buffer.append("<td>S.No</td>");
                buffer.append("<td>Voucher No</td>");
                buffer.append("<td>Date</td>");
                buffer.append("<td>Book Name</td>");
                buffer.append("<td>Narration</td>");
                buffer.append("<td>Voucher No</td>");
                buffer.append("<td>Debit</td>");
                buffer.append("<td>Credit</td>");
                buffer.append("<td> ");
//                buffer.append("<input type=\"checkbox\" name=\"billheader\" id=\"billheader\" value=\"billno\" onclick=\"SelectAll(this)\">");
                buffer.append("</td>");
                buffer.append("</tr>");

                int i = 0;
                for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    vno = (String) rows[0];
                    bookname = (String) rows[1];
                    narration = (String) rows[2];
                    Date date = (Date) rows[3];
                    vdate = dateToString(date);
                    vouchernoreg = (String) rows[4];
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }

                    buffer.append("<tr class=\"" + classname + "\">");
                    buffer.append("<td align=\"center\">" + (i + 1) + "</td>");
                    buffer.append("<td align=\"center\">" + vno + "</td>");
                    buffer.append("<td align=\"center\">" + vdate + "</td>");
                    buffer.append("<td align=\"center\">" + bookname + "</td>");
                    buffer.append("<td align=\"left\">" + narration + "</td>");
                    buffer.append("<td align=\"left\">" + vouchernoreg + "</td>");
                    buffer.append(getVoucherDebitCreditDetails(session, vno));
                    buffer.append("<td align=\"center\">");
                    String param = vno + "&" + vdate + "&" + cashbooktype;
                    buffer.append("<input type=\"radio\" name=\"vno\" id=\"vno\" value=" + param + ">");
                    buffer.append("</td>");

                    buffer.append("</tr>");
                    i++;
                }
                buffer.append("</table>");
                map.put("vouchergrid", buffer.toString());
            }
        } catch (Exception ex) {
            map.put("ERROR", "Given the Input is Invalid");
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map voucherPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherno, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class voucherPrintout method is calling ********************************");
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        VoucherReport cbr = new VoucherReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");

        try {
            String VOUCHERQUERY = "select rm.regionname,ab.bookname,v.voucherdate,v.sanctionedby,v.fileno,v.narration,"
                    + "v.sanctiondate,v.printed,v.id,v.accountbook,v.voucherno,v.voucherapproveddate from voucher v "
                    + "left join accountsbooks ab on ab.code=v.accountbook "
                    + "left join regionmaster rm on rm.id=v.region where v.id='" + voucherno + "' and v.cancelled is false";

//            String VOUCHERQUERY = "select rm.regionname,ab.bookname,v.voucherdate,v.sanctionedby,v.fileno,v.narration,"
//                    + "v.sanctiondate,v.printed,v.id from voucher v "
//                    + "left join accountsbooks ab on ab.code=v.accountbook "
//                    + "left join regionmaster rm on rm.id=v.region where v.id='" + voucherno + "' and v.accountbook='1' and v.cancelled is false";

            SQLQuery voucher_query = session.createSQLQuery(VOUCHERQUERY);
            //System.out.println("VOUCHERQUERY ->" + VOUCHERQUERY);
            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                accountsModel.setRegion((String) rows[0]);
                accountsModel.setCashbook((String) rows[1]);

                Date date = (Date) rows[2];
                accountsModel.setAccdate(dateToString(date));
//                accountsModel.setAccdate((String) rows[2]);

                if (rows[3] != null) {
                    accountsModel.setSanctionedby((String) rows[3]);
                } else {
                    accountsModel.setSanctionedby("");
                }
                if (rows[4] != null) {
                    accountsModel.setFileno((String) rows[4]);
                } else {
                    accountsModel.setFileno("");
                }
                accountsModel.setDetails((String) rows[5]);

                if (rows[6] != null) {
                    accountsModel.setSanctioneddate((String) rows[6]);
                } else {
                    accountsModel.setSanctioneddate("");
                }

                if (rows[7] != null) {
                    accountsModel.setPrinted(String.valueOf((Boolean) rows[7]));
                } else {
                    accountsModel.setPrinted("");
                }

                String voucherid = (String) rows[8];
                String casherno = "";
                String voucherapproveddate = "";
                accountsModel.setVoucherno(voucherid);
                accountsModel.setAccountbook((String) rows[9]);
                if (rows[10] != null) {
                    casherno = (String) rows[10];
                    accountsModel.setCasherno(casherno);
                }
                if (rows[11] != null) {
                    Date vappdate = (Date) rows[11];
                    voucherapproveddate = dateToString(vappdate);
                    accountsModel.setVoucherapproveddate(voucherapproveddate);
                }

                String[] oprationtype = {"Payment", "Adjustment"};

                List<AccountsSubModel> PaymentList = new ArrayList<AccountsSubModel>();
                List<AccountsSubModel> AdjustmentList = new ArrayList<AccountsSubModel>();

                double paymenttotal = 0;
                double adjustmenttotal = 0;
                double netpayment = 0;


                for (int i = 0; i < oprationtype.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Payment and Recipt">
                    String VOUCHERDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,"
                            + "vd.voucheroption,vd.serialno from voucherdetails vd "
                            + "left join accountsheads ah on ah.acccode=vd.accountcode "
                            + "left join accountgroups ag on ag.id=ah.groupcode "
                            + "where vd.voucher='" + voucherid + "' and vd.cancelled is false and vd.voucheroption='" + oprationtype[i] + "'";

                    SQLQuery voucherdetails_query = session.createSQLQuery(VOUCHERDETAILS_QUERY);
//                    System.out.println("VOUCHERDETAILS_QUERY ->" + VOUCHERDETAILS_QUERY);
                    for (ListIterator its1 = voucherdetails_query.list().listIterator(); its1.hasNext();) {
                        Object[] row1 = (Object[]) its1.next();
                        AccountsSubModel asm = new AccountsSubModel();
//                    String vid = (String) row1[0];
                        String accountno = (String) row1[0];
                        String accountname = (String) row1[1];
                        String groupname = (String) row1[2];
                        BigDecimal amo = (BigDecimal) row1[3];
                        double amount = amo.doubleValue();
                        String voucheroption = (String) row1[4];
                        int serialno = 0;
                        if (row1[5] != null) {
                            Integer integer = (Integer) row1[5];
                            serialno = integer.intValue();
                        }
                        asm.setAccno(accountno);
                        asm.setAccname(accountname);
                        asm.setGroupname(groupname);
                        asm.setOption(voucheroption);
                        asm.setAmount(decimalFormat.format(amount));
                        asm.setSerialno(String.valueOf(serialno));
                        if (oprationtype[i].equals("Payment")) {
                            PaymentList.add(asm);
                            paymenttotal += amount;
                        } else {
                            AdjustmentList.add(asm);
                            adjustmenttotal += amount;
                        }
//                        System.out.println(accountno + "\t" + accountname + "\t" + groupname + "\t" + amount + "\t" + serialno);
                    }
                    // </editor-fold>
                }
                netpayment = paymenttotal - adjustmenttotal;
                String PAYMENTMODEQUERY = "select distinct(select type from paymentmode where code=paymentmode) as "
                        + "paymenttype from receiptpaymentdetails where cancelled is false and voucher='" + voucherid + "'";

                SQLQuery paymentmode_query = session.createSQLQuery(PAYMENTMODEQUERY);

                StringBuffer buf = new StringBuffer();
                buf.append(" BY ");
                if (paymentmode_query.list().size() > 0) {
                    for (int i = 0; i < paymentmode_query.list().size(); i++) {
                        Object row2 = paymentmode_query.list().get(i);
                        buf.append((String) row2);
                        if ((i + 1) != paymentmode_query.list().size()) {
                            buf.append(", ");
                        }
                    }
                }

                String INFAVOURQUERY = "select favourof from receiptpaymentdetails  where voucher='" + voucherid + "' and cancelled is false";

                SQLQuery infavour_query = session.createSQLQuery(INFAVOURQUERY);

                String[] infavourof = new String[infavour_query.list().size()];
                if (infavour_query.list().size() > 0) {
                    for (int i = 0; i < infavour_query.list().size(); i++) {
                        Object row2 = infavour_query.list().get(i);
                        infavourof[i] = (String) row2;
                    }
                }

                String chequeno = "";
                String chequedate = "";

                String CHEQUENO = "select refno,chequedate from receiptpaymentdetails where voucher='" + voucherid + "' and cancelled is false";
                SQLQuery chequeno_query = session.createSQLQuery(CHEQUENO);

                if (chequeno_query.list().size() > 0) {
                    Object obj[] = (Object[]) chequeno_query.list().get(0);
                    if (obj[0] != null) {
                        chequeno = (String) obj[0];
                    }
                    if (obj[1] != null) {
                        Date chdate = (Date) obj[1];
                        chequedate = dateToString(chdate);
                    }
                }

                accountsModel.setInfavourof(infavourof);
                accountsModel.setPayment(buf.toString());
                accountsModel.setNetpayment(decimalFormat.format(netpayment));
                accountsModel.setAdjustmenttotal(decimalFormat.format(adjustmenttotal));
                accountsModel.setPaymentlist(PaymentList);
                accountsModel.setAdjustmentlist(AdjustmentList);
                accountsModel.setChequeno(chequeno);
                accountsModel.setChequedate(chequedate);
                cbr.getPrintWriter(accountsModel, filePathwithName);
//                long np = Math.round(netpayment);
//                AmountInWords.convertNumToWord(np);

                // </editor-fold>
            }
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getJournalDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbooktype, String journalno) {
        System.out.println("***************************** AccountReportServiceImpl class getVoucherDetails method is calling ********************************");
        Map map = new HashMap();
        //System.out.println("startingdate = " + startingdate);
        //System.out.println("enddate = " + enddate);
        //System.out.println("journalno = " + journalno);
        //System.out.println("cashbooktype = " + cashbooktype);
        try {
            StringBuffer buffer = new StringBuffer();
            String QUERY = null;
            if (journalno.length() > 0) {
                QUERY = "select v.id,a.bookname,v.narration,v.voucherdate from voucher v "
                        + "left join accountsbooks a on a.code=v.accountbook "
                        + "where v.id='" + journalno + "' and cancelled is false and v.region='" + LoggedInRegion + "' and vouchertype='J' order by voucherdate";
            } else if (journalno.length() == 0) {
                QUERY = "select v.id,a.bookname,v.narration,v.voucherdate from voucher v "
                        + "left join accountsbooks a on a.code=v.accountbook "
                        + "where voucherdate between '" + postgresDate(startingdate) + "' and "
                        + "'" + postgresDate(enddate) + "' and accountbook='" + cashbooktype + "' and "
                        + "cancelled is false and v.region='" + LoggedInRegion + "' and vouchertype='J' order by voucherdate";
            }

            SQLQuery journal_query = session.createSQLQuery(QUERY);

            //System.out.println("journal_query -> " + QUERY);

            if (journal_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            } else {
                String vno = null;
                String bookname = null;
                String narration = null;
                String vdate = "";
                String classname = "";

                buffer.append("<table width=\"60%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                buffer.append("<tr class=\"gridmenu\">");
                buffer.append("<td>S.No</td>");
                buffer.append("<td>Journal No</td>");
                buffer.append("<td>Date</td>");
                buffer.append("<td>Book Name</td>");
                buffer.append("<td>Narration</td>");
                buffer.append("<td width=\"5%\">");
//                buffer.append("<input type=\"checkbox\" name=\"billheader\" id=\"billheader\" value=\"billno\" onclick=\"SelectAll(this)\">");
                buffer.append("</td>");
                buffer.append("</tr>");

                int i = 0;
                for (ListIterator its = journal_query.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    vno = (String) rows[0];
                    bookname = (String) rows[1];
                    narration = (String) rows[2];
                    Date date = (Date) rows[3];
                    vdate = dateToString(date);

                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }

                    buffer.append("<tr class=\"" + classname + "\">");
                    buffer.append("<td align=\"center\">" + (i + 1) + "</td>");
                    buffer.append("<td align=\"center\">" + vno + "</td>");
                    buffer.append("<td align=\"center\">" + vdate + "</td>");
                    buffer.append("<td align=\"center\">" + bookname + "</td>");
                    buffer.append("<td align=\"left\">" + narration + "</td>");
                    buffer.append("<td align=\"center\">");
                    String param = vno + "&" + vdate + "&" + cashbooktype;
                    buffer.append("<input type=\"radio\" name=\"vno\" id=\"vno\" value=" + param + ">");
                    buffer.append("</td>");

                    buffer.append("</tr>");
                    i++;
                }
                buffer.append("</table>");
                map.put("journalgrid", buffer.toString());
            }
        } catch (Exception ex) {
            map.put("ERROR", "Given the Input is Invalid");
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map journalPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String journalno, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class journalPrintout method is calling ********************************");
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        JournalReport jr = new JournalReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        try {
            String JOURNALQUERY = "select v.id,re.regionname,ab.bookname,v.voucherdate,ay.startmonth, ay.startyear, ay.endmonth, "
                    + "ay.endyear,v.narration,v.accountbook from voucher v "
                    + "left join regionmaster re on re.id=v.region "
                    + "left join accountsbooks ab on ab.code=v.accountbook "
                    + "left join accountingyear ay on ay.id=v.accountingperiod "
                    + "where v.id='" + journalno + "' and cancelled is false order by v.voucherdate";

//            String VOUCHERQUERY = "select rm.regionname,ab.bookname,v.voucherdate,v.sanctionedby,v.fileno,v.narration,"
//                    + "v.sanctiondate,v.printed,v.id from voucher v "
//                    + "left join accountsbooks ab on ab.code=v.accountbook "
//                    + "left join regionmaster rm on rm.id=v.region where v.id='" + voucherno + "' and v.accountbook='1' and v.cancelled is false";

            SQLQuery journal_query = session.createSQLQuery(JOURNALQUERY);
            //System.out.println("JOURNALQUERY ->" + JOURNALQUERY);
            if (journal_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = journal_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String journalid = (String) rows[0];
                accountsModel.setVoucherno(journalid);
                accountsModel.setRegion((String) rows[1]);
                accountsModel.setCashbook((String) rows[2]);
                Date date = (Date) rows[3];
                String jdate = dateToString(date);
                accountsModel.setAccdate(jdate);
                accountsModel.setAccountingperiod(rows[5].toString() + "-" + rows[7].toString());
                accountsModel.setAccountingmonthandyear(months[Integer.valueOf(jdate.substring(3, 5)) - 1] + "/" + jdate.substring(6, 10));
                accountsModel.setDetails((String) rows[8]);
                accountsModel.setAccountbook((String) rows[9]);

                String[] oprationtype = {"Debit", "Credit"};

                List<AccountsSubModel> DebitList = new ArrayList<AccountsSubModel>();
                List<AccountsSubModel> CreditList = new ArrayList<AccountsSubModel>();

                double debittotal = 0;
                double credittotal = 0;


                for (int i = 0; i < oprationtype.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Payment and Recipt">
                    String JOURNALDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,"
                            + "vd.voucheroption,vd.serialno from voucherdetails vd "
                            + "left join accountsheads ah on ah.acccode=vd.accountcode "
                            + "left join accountgroups ag on ag.id=ah.groupcode "
                            + "where vd.voucher='" + journalid + "' and vd.cancelled is false and vd.voucheroption='" + oprationtype[i] + "' order by vd.serialno";

                    SQLQuery journaldetails_query = session.createSQLQuery(JOURNALDETAILS_QUERY);
//                    System.out.println("VOUCHERDETAILS_QUERY ->" + VOUCHERDETAILS_QUERY);
                    for (ListIterator its1 = journaldetails_query.list().listIterator(); its1.hasNext();) {
                        Object[] row1 = (Object[]) its1.next();
                        AccountsSubModel asm = new AccountsSubModel();
//                    String vid = (String) row1[0];
                        String accountno = (String) row1[0];
                        String accountname = (String) row1[1];
                        String groupname = (String) row1[2];
                        BigDecimal amo = (BigDecimal) row1[3];
                        double amount = amo.doubleValue();
                        String voucheroption = (String) row1[4];
                        int serialno = 0;
                        if (row1[5] != null) {
                            Integer integer = (Integer) row1[5];
                            serialno = integer.intValue();
                        }
                        asm.setAccno(accountno);
                        asm.setAccname(accountname);
                        asm.setGroupname(groupname);
                        asm.setOption(voucheroption);
                        asm.setAmount(decimalFormat.format(amount));
                        asm.setSerialno(String.valueOf(serialno));
                        if (oprationtype[i].equals("Debit")) {
                            DebitList.add(asm);
                            debittotal += amount;
                        } else {
                            CreditList.add(asm);
                            credittotal += amount;
                        }
//                        System.out.println(accountno + "\t" + accountname + "\t" + groupname + "\t" + amount + "\t" + serialno);
                    }
                    // </editor-fold>
                }
                accountsModel.setDebittotal(decimalFormat.format(debittotal));
                accountsModel.setCredittotal(decimalFormat.format(credittotal));
                accountsModel.setPaymentlist(DebitList);
                accountsModel.setAdjustmentlist(CreditList);
                jr.getPrintWriter(accountsModel, filePathwithName);
//                long np = Math.round(netpayment);
//                AmountInWords.convertNumToWord(np);

                // </editor-fold>
            }
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getBankDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbooktype, String bankno) {
        System.out.println("***************************** AccountReportServiceImpl class getBankDetails method is calling ********************************");
        Map map = new HashMap();
        //System.out.println("startingdate = " + startingdate);
        //System.out.println("enddate = " + enddate);
        //System.out.println("bankno = " + bankno);
        //System.out.println("cashbooktype = " + cashbooktype);
        try {
            StringBuffer buffer = new StringBuffer();
            String QUERY = null;
            if (bankno.length() > 0) {
                QUERY = "select v.id,a.bookname,v.narration,v.voucherdate from voucher v "
                        + "left join accountsbooks a on a.code=v.accountbook "
                        + "where v.id='" + bankno + "' and cancelled is false and v.region='" + LoggedInRegion + "' and vouchertype='P' order by voucherdate";
            } else if (bankno.length() == 0) {
                QUERY = "select v.id,a.bookname,v.narration,v.voucherdate from voucher v "
                        + "left join accountsbooks a on a.code=v.accountbook "
                        + "where voucherdate between '" + postgresDate(startingdate) + "' and "
                        + "'" + postgresDate(enddate) + "' and accountbook='4' and "
                        + "cancelled is false and v.region='" + LoggedInRegion + "' and vouchertype='P' order by voucherdate";
            }

            SQLQuery journal_query = session.createSQLQuery(QUERY);

            //System.out.println("journal_query -> " + QUERY);

            if (journal_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            } else {
                String vno = null;
                String bookname = null;
                String narration = null;
                String vdate = "";
                String classname = "";

                buffer.append("<table width=\"60%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                buffer.append("<tr class=\"gridmenu\">");
                buffer.append("<td>S.No</td>");
                buffer.append("<td>Bank No</td>");
                buffer.append("<td>Date</td>");
                buffer.append("<td>Book Name</td>");
                buffer.append("<td>Narration</td>");
                buffer.append("<td width=\"5%\">");
//                buffer.append("<input type=\"checkbox\" name=\"billheader\" id=\"billheader\" value=\"billno\" onclick=\"SelectAll(this)\">");
                buffer.append("</td>");
                buffer.append("</tr>");

                int i = 0;
                for (ListIterator its = journal_query.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    vno = (String) rows[0];
                    bookname = (String) rows[1];
                    narration = (String) rows[2];
                    Date date = (Date) rows[3];
                    vdate = dateToString(date);

                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }

                    buffer.append("<tr class=\"" + classname + "\">");
                    buffer.append("<td align=\"center\">" + (i + 1) + "</td>");
                    buffer.append("<td align=\"center\">" + vno + "</td>");
                    buffer.append("<td align=\"center\">" + vdate + "</td>");
                    buffer.append("<td align=\"center\">" + bookname + "</td>");
                    buffer.append("<td align=\"left\">" + narration + "</td>");
                    buffer.append("<td align=\"center\">");
                    String param = vno + "&" + vdate + "&" + cashbooktype;
                    buffer.append("<input type=\"radio\" name=\"vno\" id=\"vno\" value=" + param + ">");
                    buffer.append("</td>");

                    buffer.append("</tr>");
                    i++;
                }
                buffer.append("</table>");
                map.put("journalgrid", buffer.toString());
            }
        } catch (Exception ex) {
            map.put("ERROR", "Given the Input is Invalid");
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map bankPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String bankno, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class bankPrintout method is calling ********************************");
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        BankReport br = new BankReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        try {
            String JOURNALQUERY = "select v.id,re.regionname,ab.bookname,v.voucherdate,ay.startmonth, ay.startyear, ay.endmonth, "
                    + "ay.endyear,v.narration,v.accountbook from voucher v "
                    + "left join regionmaster re on re.id=v.region "
                    + "left join accountsbooks ab on ab.code=v.accountbook "
                    + "left join accountingyear ay on ay.id=v.accountingperiod "
                    + "where v.id='" + bankno + "' and cancelled is false order by v.voucherdate";

//            String VOUCHERQUERY = "select rm.regionname,ab.bookname,v.voucherdate,v.sanctionedby,v.fileno,v.narration,"
//                    + "v.sanctiondate,v.printed,v.id from voucher v "
//                    + "left join accountsbooks ab on ab.code=v.accountbook "
//                    + "left join regionmaster rm on rm.id=v.region where v.id='" + voucherno + "' and v.accountbook='1' and v.cancelled is false";

            SQLQuery journal_query = session.createSQLQuery(JOURNALQUERY);
            //System.out.println("JOURNALQUERY ->" + JOURNALQUERY);
            if (journal_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = journal_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                accountsModel = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String journalid = (String) rows[0];
                accountsModel.setVoucherno(journalid);
                accountsModel.setRegion((String) rows[1]);
                accountsModel.setCashbook((String) rows[2]);
                Date date = (Date) rows[3];
                String jdate = dateToString(date);
                accountsModel.setAccdate(jdate);
                accountsModel.setAccountingperiod(rows[5].toString() + "-" + rows[7].toString());
                accountsModel.setAccountingmonthandyear(months[Integer.valueOf(jdate.substring(3, 5)) - 1] + "/" + jdate.substring(6, 10));
                accountsModel.setDetails((String) rows[8]);
                accountsModel.setAccountbook((String) rows[9]);

                String[] oprationtype = {"Payment", "Adjustment"};

                List<AccountsSubModel> DebitList = new ArrayList<AccountsSubModel>();
                List<AccountsSubModel> CreditList = new ArrayList<AccountsSubModel>();

                double debittotal = 0;
                double credittotal = 0;


                for (int i = 0; i < oprationtype.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Payment and Recipt">
                    String JOURNALDETAILS_QUERY = "select vd.accountcode,ah.accname,ag.groupname,vd.amount,"
                            + "vd.voucheroption,vd.serialno from voucherdetails vd "
                            + "left join accountsheads ah on ah.acccode=vd.accountcode "
                            + "left join accountgroups ag on ag.id=ah.groupcode "
                            + "where vd.voucher='" + journalid + "' and vd.cancelled is false and vd.voucheroption='" + oprationtype[i] + "'";

                    SQLQuery journaldetails_query = session.createSQLQuery(JOURNALDETAILS_QUERY);
//                    System.out.println("VOUCHERDETAILS_QUERY ->" + VOUCHERDETAILS_QUERY);
                    for (ListIterator its1 = journaldetails_query.list().listIterator(); its1.hasNext();) {
                        Object[] row1 = (Object[]) its1.next();
                        AccountsSubModel asm = new AccountsSubModel();
//                    String vid = (String) row1[0];
                        String accountno = (String) row1[0];
                        String accountname = (String) row1[1];
                        String groupname = (String) row1[2];
                        BigDecimal amo = (BigDecimal) row1[3];
                        double amount = amo.doubleValue();
                        String voucheroption = (String) row1[4];
                        int serialno = 0;
                        if (row1[5] != null) {
                            Integer integer = (Integer) row1[5];
                            serialno = integer.intValue();
                        }
                        asm.setAccno(accountno);
                        asm.setAccname(accountname);
                        asm.setGroupname(groupname);
                        asm.setOption(voucheroption);
                        asm.setAmount(decimalFormat.format(amount));
                        asm.setSerialno(String.valueOf(serialno));
                        if (oprationtype[i].equals("Payment")) {
                            DebitList.add(asm);
                            debittotal += amount;
                        } else {
                            CreditList.add(asm);
                            credittotal += amount;
                        }
//                        System.out.println(accountno + "\t" + accountname + "\t" + groupname + "\t" + amount + "\t" + serialno);
                    }
                    // </editor-fold>
                }
                accountsModel.setDebittotal(decimalFormat.format(debittotal));
                accountsModel.setCredittotal(decimalFormat.format(credittotal));
                accountsModel.setPaymentlist(DebitList);
                accountsModel.setAdjustmentlist(CreditList);
                br.getPrintWriter(accountsModel, filePathwithName);
//                long np = Math.round(netpayment);
//                AmountInWords.convertNumToWord(np);

                // </editor-fold>
            }
        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getReceiptDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbooktype, String receiptno) {
        System.out.println("***************************** AccountReportServiceImpl class getReceiptDetails method is calling ********************************");
        Map map = new HashMap();
        //System.out.println("startingdate = " + startingdate);
        //System.out.println("enddate = " + enddate);
        //System.out.println("receiptno = " + receiptno);
        //System.out.println("cashbooktype = " + cashbooktype);
        try {
            StringBuffer buffer = new StringBuffer();
            String QUERY = null;
            if (receiptno.length() > 0) {
                QUERY = "select v.id,a.bookname,v.narration,v.voucherdate from voucher v "
                        + "left join accountsbooks a on a.code=v.accountbook "
                        + "where v.id='" + receiptno + "' and cancelled is false and v.region='" + LoggedInRegion + "' and vouchertype='R' order by voucherdate";
            } else if (receiptno.length() == 0) {
                QUERY = "select v.id,a.bookname,v.narration,v.voucherdate from voucher v "
                        + "left join accountsbooks a on a.code=v.accountbook "
                        + "where voucherdate between '" + postgresDate(startingdate) + "' and "
                        + "'" + postgresDate(enddate) + "' and accountbook='" + cashbooktype + "' and "
                        + "cancelled is false and v.region='" + LoggedInRegion + "' and vouchertype='R' order by voucherdate";
            }

            SQLQuery journal_query = session.createSQLQuery(QUERY);

            //System.out.println("journal_query -> " + QUERY);

            if (journal_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            } else {
                String vno = null;
                String bookname = null;
                String narration = null;
                String vdate = "";
                String classname = "";

                buffer.append("<table width=\"60%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                buffer.append("<tr class=\"gridmenu\">");
                buffer.append("<td>S.No</td>");
                buffer.append("<td>Receipt No</td>");
                buffer.append("<td>Date</td>");
                buffer.append("<td>Book Name</td>");
                buffer.append("<td>Narration</td>");
                buffer.append("<td width=\"5%\">");
//                buffer.append("<input type=\"checkbox\" name=\"billheader\" id=\"billheader\" value=\"billno\" onclick=\"SelectAll(this)\">");
                buffer.append("</td>");
                buffer.append("</tr>");

                int i = 0;
                for (ListIterator its = journal_query.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    vno = (String) rows[0];
                    bookname = (String) rows[1];
                    narration = (String) rows[2];
                    Date date = (Date) rows[3];
                    vdate = dateToString(date);

                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }

                    buffer.append("<tr class=\"" + classname + "\">");
                    buffer.append("<td align=\"center\">" + (i + 1) + "</td>");
                    buffer.append("<td align=\"center\">" + vno + "</td>");
                    buffer.append("<td align=\"center\">" + vdate + "</td>");
                    buffer.append("<td align=\"center\">" + bookname + "</td>");
                    buffer.append("<td align=\"left\">" + narration + "</td>");
                    buffer.append("<td align=\"center\">");
                    String param = vno + "&" + vdate + "&" + cashbooktype;
                    //System.out.println("param = " + param);
                    buffer.append("<input type=\"radio\" name=\"vno\" id=\"vno\" value=" + param + ">");
                    buffer.append("</td>");

                    buffer.append("</tr>");
                    i++;
                }
                buffer.append("</table>");
                map.put("receiptgrid", buffer.toString());
            }
        } catch (Exception ex) {
            map.put("ERROR", "Given the Input is Invalid");
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map ReceiptPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String receiptno, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class ReceiptPrintout method is calling ********************************");
        Map map = new HashMap();
        AccountsModel accountsModel = null;
        ReceiptReport rr = new ReceiptReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        try {
            String VOUCHERQUERY = "select v.id,rm.regionname,v.voucherdate,v.fileno,v.narration,v.accountbook from voucher v  "
                    + "left join regionmaster rm on rm.id=v.region "
                    + "where v.id='" + receiptno + "' and cancelled is false order by  voucherdate";

            String VOUCHERDETAILSQUERY = "select vd.accountcode,ah.accname,vd.amount,vd.voucheroption,ah.groupcode from voucherdetails vd "
                    + "left join accountsheads ah on vd.accountcode=ah.acccode "
                    + "where vd.voucher='" + receiptno + "' and vd.voucheroption='Receipt' and vd.cancelled is false";

            String RECEIPTQUERY = "select pl.partyname,rd.otherbankname,pm.type,rd.refno,rd.chequedate,rd.favourof,rd.amount from receiptpaymentdetails rd "
                    + "left join partyledger pl on pl.code=rd.partyledger "
                    + "left join bankledger bl on bl.code=rd.bankname "
                    + "left join paymentmode pm on pm.code=rd.paymentmode "
                    + "where rd.voucher = '" + receiptno + "' and rd.cancelled is false";

            SQLQuery voucher_query = session.createSQLQuery(VOUCHERQUERY);
            SQLQuery voucherdetails_query = session.createSQLQuery(VOUCHERDETAILSQUERY);
            SQLQuery receipt_query = session.createSQLQuery(RECEIPTQUERY);

            //System.out.println("VOUCHERQUERY ->" + VOUCHERQUERY);
            //System.out.println("VOUCHERDETAILSQUERY ->" + VOUCHERDETAILSQUERY);
            //System.out.println("RECEIPTQUERY ->" + RECEIPTQUERY);
            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            Object[] vou = (Object[]) voucher_query.list().get(0);

            List<AccountsSubModel> accountdetailslist = new ArrayList<AccountsSubModel>();
            List<AccountsSubModel> chequedetailslist = new ArrayList<AccountsSubModel>();

            accountsModel = new AccountsModel();

            accountsModel.setVoucherno((String) vou[0]);
            accountsModel.setRegion((String) vou[1]);
            Date date = (Date) vou[2];
            accountsModel.setAccdate(dateToString(date));
            accountsModel.setFileno((String) vou[3]);
            accountsModel.setDetails((String) vou[4]);
            accountsModel.setAccountbook((String) vou[5]);

            String receivername = "";

            double totalamount = 0;

            for (ListIterator its = voucherdetails_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                AccountsSubModel asm = new AccountsSubModel();
                Object[] rows = (Object[]) its.next();
                asm.setAccno((String) rows[0]);
                asm.setAccname((String) rows[1]);
                BigDecimal amo = (BigDecimal) rows[2];
                double amount = amo.doubleValue();
                totalamount += amount;
                asm.setAmount(decimalFormat.format(amount));
                String voucheroption = (String) rows[3];
                asm.setGroupname((String) rows[4]);
                accountdetailslist.add(asm);
                //</editor-fold>
            }

            accountsModel.setAmount(decimalFormat.format(totalamount));
            accountsModel.setAccountdetailslist(accountdetailslist);

            for (ListIterator its = receipt_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                AccountsSubModel asm = new AccountsSubModel();
                Object[] rows = (Object[]) its.next();
                asm.setPartyname((String) rows[0]);
                asm.setBankname((String) rows[1]);
                asm.setPaymenttype((String) rows[2]);
                asm.setChequeno((String) rows[3]);
                date = (Date) rows[4];
                asm.setCdate(dateToString(date));
                asm.setFavourof((String) rows[5]);
                BigDecimal amo = (BigDecimal) rows[6];
                double amount = amo.doubleValue();
                asm.setAmount(decimalFormat.format(amount));
                chequedetailslist.add(asm);
//                receivername = asm.getPartyname();
                receivername = asm.getFavourof();
                //</editor-fold>
            }

            accountsModel.setReceivername(receivername);

            accountsModel.setChequedetailslist(chequedetailslist);

            rr.getPrintWriter(accountsModel, filePathwithName);

        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getBookType(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Criteria bookDetailsCrit = session.createCriteria(Accountsbooks.class);
        bookDetailsCrit.add(Restrictions.sqlRestriction("grouptype ='1'"));
        List bookDetailsList = bookDetailsCrit.list();
        StringBuffer buf = new StringBuffer();
        buf.append("<select class=\"combobox\" name=\"type\" id=\"type\">");
        if (bookDetailsList.size() > 0) {
            for (int i = 0; i < bookDetailsList.size(); i++) {
                Accountsbooks accountsbooks = (Accountsbooks) bookDetailsList.get(i);
                buf.append("<option value=\"" + (i + 1) + "\">" + accountsbooks.getBookname() + "</option>");
            }
        }
        buf.append("</select>");
        return buf.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map PurchasePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String breakuptype, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class PurchasePrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        PurchaseInterface pcr = null;
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        int pageno = 1;
        //System.out.println("year = " + year);
        //System.out.println("month = " + month);
        //System.out.println("breakuptype = " + breakuptype);
        String PURCHASEQUERY = null;
        try {

            if (breakuptype.equals("1")) {
                PURCHASEQUERY = "select rm.regionname, vp.year, vp.month, pl.partyname,pl.tinno, cm.name,cm.code,vp.quantity,"
                        + "vp.amount, vp.taxpercentage, vp.taxamount, vp.totamount,vp.billno, vp.billdate, vp.rate from vatonpurchase vp "
                        + "left join partyledger pl on pl.code=vp.partyledger "
                        + "left join commoditymaster cm on cm.id=vp.commodity "
                        + "left join regionmaster rm on rm.id=vp.accregion "
                        + "where vp.year=" + year + " and vp.month=" + month + " and vp.accregion='" + LoggedInRegion + "' "
                        + "and vp.cancelled is false and vp.taxpercentage!=0.0 order by vp.commodity,vp.partyledger,vp.taxpercentage";
                pcr = new PurchaseTaxable();

            } else if (breakuptype.equals("2")) {
                PURCHASEQUERY = "select rm.regionname, vp.year, vp.month, pl.partyname,pl.tinno, cm.name,cm.code,vp.quantity,"
                        + "vp.amount, vp.taxpercentage, vp.taxamount, vp.totamount,vp.billno, vp.billdate, vp.rate from vatonpurchase vp "
                        + "left join partyledger pl on pl.code=vp.partyledger "
                        + "left join commoditymaster cm on cm.id=vp.commodity "
                        + "left join regionmaster rm on rm.id=vp.accregion "
                        + "where vp.year=" + year + " and vp.month=" + month + " and vp.accregion='" + LoggedInRegion + "' "
                        + "and vp.cancelled is false and vp.taxpercentage=0.0 order by vp.commodity,vp.partyledger,vp.taxpercentage";
                pcr = new PurchaseTaxable();
            } else if (breakuptype.equals("3")) {
                PURCHASEQUERY = "select rm.regionname, vp.year, vp.month, pl.partyname,pl.tinno, cm.name,cm.code,vp.quantity,"
                        + "vp.amount, vp.taxpercentage, vp.taxamount, vp.totamount,vp.billno, vp.billdate, vp.rate from vatonpurchase vp "
                        + "left join partyledger pl on pl.code=vp.partyledger "
                        + "left join commoditymaster cm on cm.id=vp.commodity "
                        + "left join regionmaster rm on rm.id=vp.accregion "
                        + "where vp.year=" + year + " and vp.month=" + month + " and vp.accregion='" + LoggedInRegion + "' "
                        + "and vp.cancelled is false and vp.taxpercentage!=0.0 order by vp.partyledger,vp.taxpercentage";
                pcr = new PurchaseTaxableBreakup();
            } else if (breakuptype.equals("4")) {
                PURCHASEQUERY = "select rm.regionname, vp.year, vp.month, pl.partyname,pl.tinno, cm.name,cm.code,vp.quantity,"
                        + "vp.amount, vp.taxpercentage, vp.taxamount, vp.totamount,vp.billno, vp.billdate, vp.rate from vatonpurchase vp "
                        + "left join partyledger pl on pl.code=vp.partyledger "
                        + "left join commoditymaster cm on cm.id=vp.commodity "
                        + "left join regionmaster rm on rm.id=vp.accregion "
                        + "where vp.year=" + year + " and vp.month=" + month + " and vp.accregion='" + LoggedInRegion + "' "
                        + "and vp.cancelled is false and vp.taxpercentage=0.0 order by vp.partyledger,vp.taxpercentage";
                pcr = new PurchaseTaxableBreakup();
            }

            SQLQuery purchase_query = session.createSQLQuery(PURCHASEQUERY);

            //System.out.println("PURCHASEQUERY ->" + PURCHASEQUERY);
            if (purchase_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = purchase_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                am.setRegion((String) rows[0]);
                String pyear = rows[1].toString();
                int pmonth = Integer.valueOf(rows[2].toString());
                am.setAccountingmonthandyear(months[pmonth - 1] + pyear.substring(2, 4));
                am.setCompanyname((String) rows[3]);
                am.setTinno((String) rows[4]);
                am.setCommodityname((String) rows[5]);
                am.setCommoditycode((String) rows[6]);
                BigDecimal bquantity, bvalue, btaxpercen, btaxamount, btotalamount, brate;
                double quantity, value, taxpercen, taxamount, totalamount, rate;
                bquantity = (BigDecimal) rows[7];
                bvalue = (BigDecimal) rows[8];
                btaxpercen = (BigDecimal) rows[9];
                btaxamount = (BigDecimal) rows[10];
                btotalamount = (BigDecimal) rows[11];
                String billno = (String) rows[12];
                Date date = (Date) rows[13];
                brate = (BigDecimal) rows[14];

                quantity = bquantity.doubleValue();
                value = bvalue.doubleValue();
                taxpercen = btaxpercen.doubleValue();
                taxamount = btaxamount.doubleValue();
                totalamount = btotalamount.doubleValue();
                rate = brate.doubleValue();

                long lquantity = Math.round(quantity);
                am.setQuantity(String.valueOf(lquantity));
                am.setValue(decimalFormat.format(value));
                am.setTaxpercentage(decimalFormat1.format(taxpercen));
                am.setTaxamount(decimalFormat.format(taxamount));
                am.setTotalamount(decimalFormat.format(totalamount));
                am.setBillno(billno);
                am.setAccdate(dateToString(date));
                am.setRate(decimalFormat.format(rate));
                am.setPageno(String.valueOf(pageno));
                am.setBreakuptype(breakuptype);
                pcr.getPrintWriter(am, filePathwithName);
                pageno++;
                // </editor-fold>
            }
            pcr.GrandTotal(am, filePathwithName);

        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map PurchaseAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class PurchaseAbstractPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        PurchaseAbstractReport par = new PurchaseAbstractReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        int pageno = 1;
        //System.out.println("year = " + year);
        //System.out.println("month = " + month);
        try {

            String PURCHASEQUERYCOMPANYWISE = "select rm.regionname, vp.year, vp.month, pl.partyname,vp.taxpercentage,sum(vp.quantity) as quantity,"
                    + "sum(vp.amount) as value, sum(vp.taxamount) as taxamount,sum(vp.totamount) as totalamount from vatonpurchase vp "
                    + "left join partyledger pl on pl.code=vp.partyledger "
                    + "left join regionmaster rm on rm.id=vp.accregion "
                    + "where vp.year=" + year + " and vp.month= " + month + "  and vp.accregion='" + LoggedInRegion + "' "
                    + "and vp.cancelled is false group by rm.regionname, vp.year, vp.month, pl.partyname,vp.taxpercentage "
                    + "order by pl.partyname,vp.taxpercentage";

            String PURCHASEQUERYPERCENTAGEWISE = "select taxpercentage,sum(amount) as value,sum(taxamount) as taxamount,"
                    + "sum(totamount) as totalamount from vatonpurchase where year=" + year + " and month= " + month + "  and accregion='" + LoggedInRegion + "' "
                    + "and cancelled is false group by taxpercentage order by taxpercentage";

            SQLQuery purchase_company_query = session.createSQLQuery(PURCHASEQUERYCOMPANYWISE);
            SQLQuery purchase_percentage_query = session.createSQLQuery(PURCHASEQUERYPERCENTAGEWISE);

            //System.out.println("PURCHASEQUERYCOMPANYWISE ->" + PURCHASEQUERYCOMPANYWISE);
            //System.out.println("PURCHASEQUERYPERCENTAGEWISE ->" + PURCHASEQUERYPERCENTAGEWISE);
            if (purchase_company_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = purchase_company_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                am.setRegion((String) rows[0]);
                String pyear = rows[1].toString();
                int pmonth = Integer.valueOf(rows[2].toString());
                am.setAccountingmonthandyear(months[pmonth - 1] + pyear.substring(2, 4));
                am.setCompanyname((String) rows[3]);
                BigDecimal bquantity, bvalue, btaxpercen, btaxamount, btotalamount;
                double quantity, value, taxpercen, taxamount, totalamount, rate;
                btaxpercen = (BigDecimal) rows[4];
                bquantity = (BigDecimal) rows[5];
                bvalue = (BigDecimal) rows[6];
                btaxamount = (BigDecimal) rows[7];
                btotalamount = (BigDecimal) rows[8];

                quantity = bquantity.doubleValue();
                value = bvalue.doubleValue();
                taxpercen = btaxpercen.doubleValue();
                taxamount = btaxamount.doubleValue();
                totalamount = btotalamount.doubleValue();

                long lquantity = Math.round(quantity);
                am.setQuantity(String.valueOf(lquantity));
                am.setValue(decimalFormat.format(value));
                am.setTaxpercentage(decimalFormat1.format(taxpercen));
                am.setTaxamount(decimalFormat.format(taxamount));
                am.setTotalamount(decimalFormat.format(totalamount));
                am.setPageno(String.valueOf(pageno));
                par.getPrintWriter(am, filePathwithName);
                pageno++;
                // </editor-fold>
            }
            par.GrandTotal(am, filePathwithName);

            int pgno = 1;

            for (ListIterator its = purchase_percentage_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();

                BigDecimal bvalue, btaxpercen, btaxamount, btotalamount;
                double value, taxpercen, taxamount, totalamount;

                btaxpercen = (BigDecimal) rows[0];
                bvalue = (BigDecimal) rows[1];
                btaxamount = (BigDecimal) rows[2];
                btotalamount = (BigDecimal) rows[3];

                value = bvalue.doubleValue();
                taxpercen = btaxpercen.doubleValue();
                taxamount = btaxamount.doubleValue();
                totalamount = btotalamount.doubleValue();

                am.setValue(decimalFormat.format(value));
                am.setTaxpercentage(decimalFormat1.format(taxpercen));
                am.setTaxamount(decimalFormat.format(taxamount));
                am.setTotalamount(decimalFormat.format(totalamount));
                am.setPageno(String.valueOf(pgno));
                par.getPrintWriterPercentage(am, filePathwithName);
                pgno++;
                // </editor-fold>
            }
            par.GrandTotalPercentage(am, filePathwithName);

        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map SalesTaxPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String breakuptype, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class SalesTaxPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        SalesTaxReport str = new SalesTaxReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        int pageno = 1;
        //System.out.println("year = " + year);
        //System.out.println("month = " + month);
        //System.out.println("breakuptype = " + breakuptype);
        String SALESTAXQUERY = null;
        try {

            if (breakuptype.equals("1")) {
                SALESTAXQUERY = "select rm.regionname,v.year,v.month,cm.name,cm.code,v.quantity,v.amount,v.taxpercentage,"
                        + "v.taxamount,v.totamount from vatonsales v "
                        + "left join commoditymaster cm on cm.id=v.commodity "
                        + "left join regionmaster rm on rm.id=v.accregion "
                        + "where year=" + year + " and month=" + month + " and accregion='" + LoggedInRegion + "' and cancelled is false "
                        + "and v.taxpercentage!=0 order by cm.name,v.taxpercentage";

            } else if (breakuptype.equals("2")) {
                SALESTAXQUERY = "select rm.regionname,v.year,v.month,cm.name,cm.code,v.quantity,v.amount,v.taxpercentage,"
                        + "v.taxamount,v.totamount from vatonsales v "
                        + "left join commoditymaster cm on cm.id=v.commodity "
                        + "left join regionmaster rm on rm.id=v.accregion "
                        + "where year=" + year + " and month=" + month + " and accregion='" + LoggedInRegion + "' and cancelled is false "
                        + "and v.taxpercentage=0 order by cm.name,v.taxpercentage";
            }

            SQLQuery sales_query = session.createSQLQuery(SALESTAXQUERY);

            //System.out.println("SALESTAXQUERY ->" + SALESTAXQUERY);
            if (sales_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = sales_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                am.setRegion((String) rows[0]);
                String pyear = rows[1].toString();
                int pmonth = Integer.valueOf(rows[2].toString());
                am.setAccountingmonthandyear(months[pmonth - 1] + pyear.substring(2, 4));
                am.setCommodityname((String) rows[3]);
                am.setCommoditycode((String) rows[4]);
                BigDecimal bquantity, bvalue, btaxpercen, btaxamount, btotalamount;
                double quantity, value, taxpercen, taxamount, totalamount;
                bquantity = (BigDecimal) rows[5];
                bvalue = (BigDecimal) rows[6];
                btaxpercen = (BigDecimal) rows[7];
                btaxamount = (BigDecimal) rows[8];
                btotalamount = (BigDecimal) rows[9];

                quantity = bquantity.doubleValue();
                value = bvalue.doubleValue();
                taxpercen = btaxpercen.doubleValue();
                taxamount = btaxamount.doubleValue();
                totalamount = btotalamount.doubleValue();

                long lquantity = Math.round(quantity);
                am.setQuantity(String.valueOf(lquantity));
                am.setValue(decimalFormat.format(value));
                am.setTaxpercentage(decimalFormat1.format(taxpercen));
                am.setTaxamount(decimalFormat.format(taxamount));
                am.setTotalamount(decimalFormat.format(totalamount));
                am.setPageno(String.valueOf(pageno));
                am.setBreakuptype(breakuptype);
                str.getPrintWriter(am, filePathwithName);
                pageno++;
                // </editor-fold>
            }
            str.GrandTotal(am, filePathwithName);

        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map BankChallanCashPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String challanno, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class BankChallanCashPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        BankChallanCashReport bccr = new BankChallanCashReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        int pageno = 1;
        String CHALLANQUERY = null;
        try {

            CHALLANQUERY = "SELECT b.id, b.challandate, bl.bankname, rm.regionname,sum(rpd.amount) as amount, bl.branchname "
                    + "FROM bankchallan b "
                    + "left join bankledger bl on bl.code=b.bank "
                    + "left join regionmaster rm on rm.id=b.region "
                    + "left join receiptpaymentdetails rpd on rpd.bankchallan = b.id "
                    + "where b.region='" + LoggedInRegion + "' and b.cancelled is false and b.id='" + challanno + "' "
                    + "and rpd.paymentmode='1' group by b.id, b.challandate, bl.bankname, rm.regionname, bl.branchname";

            SQLQuery challan_query = session.createSQLQuery(CHALLANQUERY);

            //System.out.println("CHALLANQUERY ->" + CHALLANQUERY);
            if (challan_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = challan_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String cno = (String) rows[0];
                Date date = (Date) rows[1];
                String bankname = (String) rows[2];
                String regionname = (String) rows[3];
                regionname = regionname + " - " + cno;
                BigDecimal bamount = (BigDecimal) rows[4];
                double damount = bamount.doubleValue();
                String branchname = (String) rows[5];

                am.setChallanno(cno);
                am.setAccdate(dateToString(date));
                am.setBankname(bankname);
                am.setRegion(regionname);
                am.setAmount(decimalFormat.format(damount));
                am.setBranchname(branchname);
                String currentdate = dateToString(getCurrentDate());

                Criteria challanCrit = session.createCriteria(Bankchallan.class);
                challanCrit.add(Restrictions.sqlRestriction("id='" + challanno + "'"));
                challanCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List challanList = challanCrit.list();
                if (challanList.size() > 0) {
                    Bankchallan bankchallan = (Bankchallan) challanList.get(0);
                    bankchallan.setPrintdate(getCurrentDate());
                    Transaction transaction = session.beginTransaction();
                    session.update(bankchallan);
                    transaction.commit();
                }

                am.setSanctioneddate(currentdate.substring(0, 3) + months[(Integer.valueOf(currentdate.substring(3, 5))) - 1] + currentdate.substring(5, 10));
                System.out.println("am.getSanctioneddate() = " + am.getSanctioneddate());
                bccr.getPrintWriter(am, filePathwithName);
                // </editor-fold>
            }

        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map BankChallanChequePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String challanno, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class BankChallanChequePrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        BankChallanChequeReport bccr = new BankChallanChequeReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        int pageno = 1;
        String CHALLANQUERY = null;
        //System.out.println("challanno = " + challanno);
        try {

            CHALLANQUERY = "SELECT b.id, b.challandate, bl.bankname, rm.regionname, b.amount, bl.branchname,bl.accountno "
                    + "FROM bankchallan b "
                    + "left join bankledger bl on bl.code=b.bank "
                    + "left join regionmaster rm on rm.id=b.region "
                    + "where b.region='" + LoggedInRegion + "' and b.cancelled is false and b.id='" + challanno + "'";

            SQLQuery challan_query = session.createSQLQuery(CHALLANQUERY);

            //System.out.println("CHALLANQUERY ->" + CHALLANQUERY);
            if (challan_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = challan_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String cno = (String) rows[0];
                Date date = (Date) rows[1];
                String bankname = (String) rows[2];
                String regionname = (String) rows[3];
                String branchname = (String) rows[3];
                String bankaccountno = (String) rows[6];
                
                am.setChallanno(cno);
                am.setAccdate(dateToString(date));
                am.setBankname(bankname);
                am.setRegion(regionname);
                am.setBranchname(branchname);
                am.setBankaccountno(bankaccountno);
                String currentdate = dateToString(getCurrentDate());
                am.setSanctioneddate(currentdate.substring(0, 3) + months[(Integer.valueOf(currentdate.substring(3, 5))) - 1] + currentdate.substring(5, 10));

                String RECEIPTQUERY = "SELECT rpd.id, rpd.otherbankname, rpd.paymentmode, rpd.refno, rpd.amount, rpd.chequedate, rpd.bankchallan,bl.accountno "
                        + "FROM receiptpaymentdetails rpd  left join bankledger bl on bl.code=rpd.bankname "
                        + "where bankchallan='" + cno + "' and paymentmode in ('2','3') "
                        + "and cancelled is false order by rpd.bankname";

                SQLQuery receipt_query = session.createSQLQuery(RECEIPTQUERY);

                AccountsSubModel asm = null;
                List<AccountsSubModel> banklist = new ArrayList<AccountsSubModel>();

                for (ListIterator its1 = receipt_query.list().listIterator(); its1.hasNext();) {
                    // <editor-fold defaultstate="collapsed" desc="Voucher">
                    asm = new AccountsSubModel();
                    Object[] row1 = (Object[]) its1.next();
                    String recno = (String) row1[0];
                    String bname = (String) row1[1];
                    String paymentmode = (String) row1[2];
                    String chequeno = (String) row1[3];
                    BigDecimal bamount = (BigDecimal) row1[4];
                    double damount = bamount.doubleValue();
                    Date cdate = (Date) row1[5];
                    String accountno = (String) row1[7];
                    
                    asm.setReceiptno(recno);
                    asm.setBankname(bname);
                    asm.setPaymenttype(paymentmode);
                    asm.setChequeno(chequeno);
                    asm.setAmount(decimalFormat.format(damount));
                    asm.setCdate(dateToString(cdate));
                    asm.setAccno(accountno);

                    Criteria challanCrit = session.createCriteria(Bankchallan.class);
                    challanCrit.add(Restrictions.sqlRestriction("id='" + challanno + "'"));
                    challanCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List challanList = challanCrit.list();
                    if (challanList.size() > 0) {
                        Bankchallan bankchallan = (Bankchallan) challanList.get(0);
                        bankchallan.setPrintdate(getCurrentDate());
                        Transaction transaction = session.beginTransaction();
                        session.update(bankchallan);
                        transaction.commit();
                    }

                    banklist.add(asm);
                    // </editor-fold>
                }

                am.setChequedetailslist(banklist);
                bccr.getPrintWriter(am, filePathwithName);

                // </editor-fold>
            }

        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map LedgerPrintOutOLD(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountbookno, String period, String fromdate, String todate, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class BankChallanChequePrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        LedgerReport lr = new LedgerReport();
        LedgerReportIRS iRS = new LedgerReportIRS();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String LEDGERQUERY = null;
        String OPENINGBALANCEQUERY = null;
        try {

            LEDGERQUERY = "select voucherid, fileno, region, accountbook, "
                    + "voucherdate, narration, accountcode, accname, groupcode, groupname, amount, option, vouno, appdate, "
                    + "case when appdate is null then voucherdate else appdate end as voudate  from "
                    + "(select v.id as voucherid, v.fileno as fileno, rm.regionname as region, v.accountbook as accountbook, "
                    + "v.voucherdate as voucherdate, v.narration as narration, vd.accountcode as accountcode, ah.accname as accname, "
                    + "ah.groupcode as groupcode, ag.groupname as groupname, vd.amount as amount, vd.voucheroption as option, "
                    + "v.voucherno as vouno, v.voucherapproveddate as appdate from voucherdetails vd "
                    + "left join voucher v on v.id=vd.voucher "
                    + "left join regionmaster rm on rm.id=v.region "
                    + "left join accountsheads ah on ah.acccode=vd.accountcode "
                    + "left join accountgroups ag on ag.id=ah.groupcode "
                    + "where "
                    + "case when "
                    + "v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' "
                    + "else  "
                    + "v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end and vd.accountcode='" + accountbookno + "' "
                    + "and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' "
                    + "and vd.cancelled is false and v.cancelled is false "
                    + "order by v.voucherdate,v.voucherapproveddate,v.voucherno,v.id ) as dd order by voudate,vouno,voucherid";

//            LEDGERQUERY = "select v.id as voucherid, v.fileno, rm.regionname, v.accountbook, v.voucherdate as voucherdate, v.narration as narration, "
//                    + "vd.accountcode, ah.accname, ah.groupcode, ag.groupname, vd.amount as amount, vd.voucheroption as option, v.voucherno, v.voucherapproveddate "
//                    + "from voucherdetails vd "
//                    + "left join voucher v on v.id=vd.voucher "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsheads ah on ah.acccode=vd.accountcode "
//                    + "left join accountgroups ag on ag.id=ah.groupcode "
//                    + "where "
//                    + "case when "
//                    + "v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' "
//                    + "else  "
//                    + "v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end and vd.accountcode='" + accountbookno + "' "
//                    + "and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' "
//                    + "and vd.cancelled is false and v.cancelled is false "
//                    + "order by v.voucherdate,v.voucherapproveddate,v.voucherno,v.id";

            OPENINGBALANCEQUERY = "select accountcode,coalesce(sum(debit),'0.00') as debit , coalesce(sum(credit),'0.00') as credit from ( select accountcode, "
                    + "case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit, "
                    + "case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit "
                    + "from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' "
                    + "and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(fromdate) + "' "
                    + "else  v.voucherapproveddate < '" + postgresDate(fromdate) + "' end and vd.cancelled is false and v.cancelled is false "
                    + "and vd.accountcode='" + accountbookno + "' "
                    + "GROUP BY accountcode,voucheroption ) as saisiva group by accountcode";


//            LEDGERQUERY = "select v.id,v.fileno,rm.regionname,v.accountbook,v.voucherdate,v.narration,vd.accountcode,ah.accname,"
//                    + "ah.groupcode,ag.groupname,vd.amount,vd.voucheroption from voucher v "
//                    + "left join voucherdetails vd on vd.voucher=v.id "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsheads ah on ah.acccode=vd.accountcode "
//                    + "left join accountgroups ag on ag.id=ah.groupcode "
//                    + "where v.voucherdate between '" + postgresDate(fromdate) + "' "
//                    + "and '" + postgresDate(todate) + "' and v.region='" + LoggedInRegion + "' "
//                    + "and v.cancelled is false and vd.cancelled is false and v.accountingperiod='" + period + "' and "
//                    + "vd.accountcode='" + accountbookno + "' order by v.voucherdate";


            SQLQuery ledger_query = session.createSQLQuery(LEDGERQUERY);
            SQLQuery openingbalance_query = session.createSQLQuery(OPENINGBALANCEQUERY);

            //System.out.println("LEDGERQUERY ->" + LEDGERQUERY);
            if (ledger_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            //System.out.println("OPENINGBALANCEQUERY = " + OPENINGBALANCEQUERY);
            double oDebit = 0;
            double oCredit = 0;
            if (openingbalance_query.list().size() > 0) {
                Object obj[] = (Object[]) openingbalance_query.list().get(0);
                //System.out.println("openingbalance_query.list().size() = " + openingbalance_query.list().size());
                BigDecimal openingDebit = (BigDecimal) obj[1];
                BigDecimal openingCredit = (BigDecimal) obj[2];
                oDebit = openingDebit.doubleValue();
                oCredit = openingCredit.doubleValue();
            }

            for (ListIterator its = ledger_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String vno = (String) rows[0];
                String fileno = (String) rows[1];
                am.setCompno(vno);
                am.setRegion((String) rows[2]);
                am.setAccountbook((String) rows[3]);
                Date vdate = null;
                if (rows[13] != null) {
                    vdate = (Date) rows[13];
                } else {
                    vdate = (Date) rows[4];
                }
                String voucherdate = dateToString(vdate);
                am.setAccdate(voucherdate.substring(0, 6) + voucherdate.substring(8, 10));
                if (((vno.substring(3, 4)).trim()).equals("R")) {
                    String favourof = "";
                    Criteria criteria = session.createCriteria(Receiptpaymentdetails.class);
                    criteria.add(Restrictions.sqlRestriction("voucher = '" + vno + "'"));
                    List favouroflist = criteria.list();
                    if (favouroflist.size() > 0) {
                        Receiptpaymentdetails receiptpaymentdetails = (Receiptpaymentdetails) favouroflist.get(0);
                        favourof = receiptpaymentdetails.getFavourof();
                    }
                    StringBuilder builder = new StringBuilder();
                    String narration = (String) rows[5];
                    builder.append(narration.trim());
                    builder.append(", ");
                    builder.append(fileno);
                    builder.append(", ");
                    builder.append(favourof);
                    am.setDetails(builder.toString());
//                    System.out.println(am.getDetails());
                } else {
                    am.setDetails((String) rows[5]);
                }
                am.setAccountcode((String) rows[6]);
                am.setAccountname((String) rows[7]);
                am.setGroupcode((String) rows[8]);
                am.setGroupname((String) rows[9]);
                BigDecimal bamount = (BigDecimal) rows[10];
                double damount = bamount.doubleValue();
                am.setAmount(decimalFormat.format(damount));
                am.setVoucheroption((String) rows[11]);
                am.setVoucherno((String) rows[12]);
                am.setFromdate(fromdate.substring(0, 3) + months[(Integer.valueOf(fromdate.substring(3, 5))) - 1] + fromdate.substring(5, 10));
                am.setTodate(todate.substring(0, 3) + months[(Integer.valueOf(todate.substring(3, 5))) - 1] + todate.substring(5, 10));
                am.setPageno(String.valueOf(pageno));
                am.setDebitamount(decimalFormat.format(oDebit));
                am.setCreditamount(decimalFormat.format(oCredit));

                if (am.getGroupcode().equals("30000")) {
                    iRS.getPrintWriter(am, filePathwithName);
                } else {
                    lr.getPrintWriter(am, filePathwithName);
                }

                pageno++;

                // </editor-fold>
            }
            if (am.getGroupcode().equals("30000")) {
                iRS.GrandTotal(am, filePathwithName);
            } else {
                lr.GrandTotal(am, filePathwithName);
            }


        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map LedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountbookno, String period, String fromdate, String todate, String reporttype, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class BankChallanChequePrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        LedgerReport lr = new LedgerReport();
        LedgerReportIRS iRS = new LedgerReportIRS();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String LEDGERQUERY = null;
        String OPENINGBALANCEQUERY = null;
        try {
            //System.out.println("reporttype::: " + reporttype);
            LEDGERQUERY = "select voucherid, fileno, region, accountbook, "
                    + "voucherdate, narration, accountcode, accname, groupcode, groupname, amount, option, vouno, appdate, "
                    + "case when appdate is null then voucherdate else appdate end as voudate  from "
                    + "(select v.id as voucherid, v.fileno as fileno, rm.regionname as region, v.accountbook as accountbook, "
                    + "v.voucherdate as voucherdate, v.narration as narration, vd.accountcode as accountcode, ah.accname as accname, "
                    + "ah.groupcode as groupcode, ag.groupname as groupname, vd.amount as amount, vd.voucheroption as option, "
                    + "v.voucherno as vouno, v.voucherapproveddate as appdate from voucherdetails vd "
                    + "left join voucher v on v.id=vd.voucher "
                    + "left join regionmaster rm on rm.id=v.region "
                    + "left join accountsheads ah on ah.acccode=vd.accountcode "
                    + "left join accountgroups ag on ag.id=ah.groupcode "
                    + "where "
                    + "case when "
                    + "v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' "
                    + "else  "
                    + "v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end ";
            if ("1".equalsIgnoreCase(reporttype)) {
                LEDGERQUERY = LEDGERQUERY + " and vd.accountcode='" + accountbookno + "' ";
            }
            LEDGERQUERY = LEDGERQUERY + " and '" + accountbookno + "' between ag.accheadstartingno and ag.accheadendingno  "
                    + " and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' "
                    + "and vd.cancelled is false and v.cancelled is false "
                    + " group by ag.grpcode,v.id , v.fileno, rm.regionname, v.accountbook , "
                    + "v.voucherdate, v.narration , vd.accountcode, ah.accname ,ah.groupcode, ag.groupname, vd.amount, vd.voucheroption, "
                    + "v.voucherno, v.voucherapproveddate,vd.serialno  "
                    + "order by vd.accountcode,v.voucherdate,v.voucherapproveddate,v.voucherno,v.id ) as dd order by accountcode,voudate,vouno,voucherid";

//            LEDGERQUERY = "select v.id as voucherid, v.fileno, rm.regionname, v.accountbook, v.voucherdate as voucherdate, v.narration as narration, "
//                    + "vd.accountcode, ah.accname, ah.groupcode, ag.groupname, vd.amount as amount, vd.voucheroption as option, v.voucherno, v.voucherapproveddate "
//                    + "from voucherdetails vd "
//                    + "left join voucher v on v.id=vd.voucher "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsheads ah on ah.acccode=vd.accountcode "
//                    + "left join accountgroups ag on ag.id=ah.groupcode "
//                    + "where "
//                    + "case when "
//                    + "v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' "
//                    + "else  "
//                    + "v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end and vd.accountcode='" + accountbookno + "' "
//                    + "and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' "
//                    + "and vd.cancelled is false and v.cancelled is false "
//                    + "order by v.voucherdate,v.voucherapproveddate,v.voucherno,v.id";

            OPENINGBALANCEQUERY = "select accountcode,coalesce(sum(debit),'0.00') as debit , coalesce(sum(credit),'0.00') as credit from ( select accountcode, "
                    + "case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit, "
                    + "case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit "
                    + "from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' "
                    + "and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(fromdate) + "' "
                    + "else  v.voucherapproveddate < '" + postgresDate(fromdate) + "' end and vd.cancelled is false and v.cancelled is false "
                    + "and vd.accountcode='" + accountbookno + "' "
                    + "GROUP BY accountcode,voucheroption ) as saisiva group by accountcode";


//            LEDGERQUERY = "select v.id,v.fileno,rm.regionname,v.accountbook,v.voucherdate,v.narration,vd.accountcode,ah.accname,"
//                    + "ah.groupcode,ag.groupname,vd.amount,vd.voucheroption from voucher v "
//                    + "left join voucherdetails vd on vd.voucher=v.id "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsheads ah on ah.acccode=vd.accountcode "
//                    + "left join accountgroups ag on ag.id=ah.groupcode "
//                    + "where v.voucherdate between '" + postgresDate(fromdate) + "' "
//                    + "and '" + postgresDate(todate) + "' and v.region='" + LoggedInRegion + "' "
//                    + "and v.cancelled is false and vd.cancelled is false and v.accountingperiod='" + period + "' and "
//                    + "vd.accountcode='" + accountbookno + "' order by v.voucherdate";


            SQLQuery ledger_query = session.createSQLQuery(LEDGERQUERY);
//            SQLQuery openingbalance_query = session.createSQLQuery(OPENINGBALANCEQUERY);

            //System.out.println("LEDGERQUERY ->" + LEDGERQUERY);
            if (ledger_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            //System.out.println("OPENINGBALANCEQUERY = " + OPENINGBALANCEQUERY);
            double oDebit = 0;
            double oCredit = 0;
//            
//            if (openingbalance_query.list().size() > 0) {
//                Object obj[] = (Object[]) openingbalance_query.list().get(0);
//                System.out.println("openingbalance_query.list().size() = " + openingbalance_query.list().size());
//                BigDecimal openingDebit = (BigDecimal) obj[1];
//                BigDecimal openingCredit = (BigDecimal) obj[2];
//                oDebit = openingDebit.doubleValue();
//                oCredit = openingCredit.doubleValue();
//            }

            for (ListIterator its = ledger_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String vno = (String) rows[0];
                String fileno = (String) rows[1];
                am.setCompno(vno);
                am.setRegion((String) rows[2]);
                am.setAccountbook((String) rows[3]);
                Date vdate = null;
                if (rows[13] != null) {
                    vdate = (Date) rows[13];
                } else {
                    vdate = (Date) rows[4];
                }
                String voucherdate = dateToString(vdate);
                am.setAccdate(voucherdate.substring(0, 6) + voucherdate.substring(8, 10));
                if (((vno.substring(3, 4)).trim()).equals("R")) {
                    String favourof = "";
                    Criteria criteria = session.createCriteria(Receiptpaymentdetails.class);
                    criteria.add(Restrictions.sqlRestriction("voucher = '" + vno + "'"));
                    List favouroflist = criteria.list();
                    if (favouroflist.size() > 0) {
                        Receiptpaymentdetails receiptpaymentdetails = (Receiptpaymentdetails) favouroflist.get(0);
                        favourof = receiptpaymentdetails.getFavourof();
                    }
                    StringBuilder builder = new StringBuilder();
                    String narration = (String) rows[5];
                    builder.append(narration.trim());
                    builder.append(", ");
                    builder.append(fileno);
                    builder.append(", ");
                    builder.append(favourof);
                    am.setDetails(builder.toString());
//                    System.out.println(am.getDetails());
                } else {
                    am.setDetails((String) rows[5]);
                }
                am.setAccountcode((String) rows[6]);
                am.setAccountname((String) rows[7]);
                am.setGroupcode((String) rows[8]);
                am.setGroupname((String) rows[9]);
                BigDecimal bamount = (BigDecimal) rows[10];
                double damount = bamount.doubleValue();
                am.setAmount(decimalFormat.format(damount));
                am.setVoucheroption((String) rows[11]);
                am.setVoucherno((String) rows[12]);
                am.setFromdate(fromdate.substring(0, 3) + months[(Integer.valueOf(fromdate.substring(3, 5))) - 1] + fromdate.substring(5, 10));
                am.setTodate(todate.substring(0, 3) + months[(Integer.valueOf(todate.substring(3, 5))) - 1] + todate.substring(5, 10));
                am.setPageno(String.valueOf(pageno));

                OPENINGBALANCEQUERY = "select accountcode,coalesce(sum(debit),'0.00') as debit , coalesce(sum(credit),'0.00') as credit from ( select accountcode, "
                        + "case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit, "
                        + "case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit "
                        + "from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' "
                        + "and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(fromdate) + "' "
                        + "else  v.voucherapproveddate < '" + postgresDate(fromdate) + "' end and vd.cancelled is false and v.cancelled is false "
                        + "and vd.accountcode='" + am.getAccountcode() + "' "
                        + "GROUP BY accountcode,voucheroption ) as saisiva group by accountcode";

                SQLQuery openingbalance_query = session.createSQLQuery(OPENINGBALANCEQUERY);
                oDebit = 0;
                oCredit = 0;
                if (openingbalance_query.list().size() > 0) {
                    Object obj[] = (Object[]) openingbalance_query.list().get(0);
                    BigDecimal openingDebit = (BigDecimal) obj[1];
                    BigDecimal openingCredit = (BigDecimal) obj[2];
                    oDebit = openingDebit.doubleValue();
                    oCredit = openingCredit.doubleValue();
                }

                am.setDebitamount(decimalFormat.format(oDebit));
                am.setCreditamount(decimalFormat.format(oCredit));

                if (am.getGroupcode().equals("30000")) {
                    iRS.getPrintWriter(am, filePathwithName);
                } else {
                    lr.getPrintWriter(am, filePathwithName);
                }

                pageno++;

                // </editor-fold>
            }
            if (am.getGroupcode().equals("30000")) {
                iRS.GrandTotal(am, filePathwithName);
            } else {
                lr.GrandTotal(am, filePathwithName);
            }

            //System.out.println("in final...");
        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map consolidatedLedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String ledger, String period, String voucherdatefrom, String voucherdateto, String reporttype, String filePathwithName) {
        System.out.println("********************** AccountVoucherServiceImpl class getLedgerDetails method is calling ***********************");
        Map resultMap = new HashMap();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        SQLQuery regionquery = session.createSQLQuery("select * from regionmaster order by regionname");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePathwithName);
        } catch (IOException ex) {
            Logger.getLogger(AccountVoucherServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Accountsheads accountsheads = CommonUtility.geAccountsheads(session, ledger);
            fileWriter.append(ledger + accountsheads.getAccname() + " (Consolidated) for the period " + voucherdatefrom + " to " + voucherdateto + " as on " + new Date());
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(String.format("%100s", " ").replaceAll(" ", "-"));
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(String.format("%25s", "Region Code"));
            fileWriter.append(String.format("%25s", "Region Name"));
            fileWriter.append(String.format("%25s", "Debit"));
            fileWriter.append(String.format("%25s", "Credit"));
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(String.format("%100s", " ").replaceAll(" ", "-"));
            fileWriter.append(NEW_LINE_SEPARATOR);
            double debit = 0;
            double credit = 0;
            for (ListIterator regits = regionquery.list().listIterator(); regits.hasNext();) {
                Object[] regionrow = (Object[]) regits.next();
                String regionid = (String) regionrow[0].toString();
                String regionname = regionrow[1].toString();
                BigDecimal regioncurrentperioddebit = new BigDecimal("0.00");
                BigDecimal regioncurrentperiodcredit = new BigDecimal("0.00");
                System.out.println("Region Name Region Name");
                try {
                    StringBuffer obquery = new StringBuffer();
                    String obdebit = "0";
                    String obcredit = "0";
                    if ("1".equalsIgnoreCase(reporttype)) {
                        obquery.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                        obquery.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher ");
                        obquery.append(" where v.accountingperiod='" + period + "'  and vd.region='" + regionid + "' and v.region='" + regionid + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(voucherdatefrom) + "' ");
                        obquery.append(" else  v.voucherapproveddate < '" + postgresDate(voucherdatefrom) + "' end and vd.cancelled is false and v.cancelled is false and vd.accountcode='" + ledger + "' ");
                        obquery.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");

                    } else {
                        obquery.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                        obquery.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher  ");
                        obquery.append(" left join accountgroups ag on '" + ledger + "' between accheadstartingno and accheadendingno ");
                        obquery.append(" where v.accountingperiod='" + period + "'  and vd.region='" + regionid + "' and v.region='" + regionid + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(voucherdatefrom) + "' ");
                        obquery.append(" else  v.voucherapproveddate < '" + postgresDate(voucherdatefrom) + "' end and vd.cancelled is false and v.cancelled is false and vd.accountcode='" + ledger + "' ");
                        obquery.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");
                    }
                    SQLQuery ledgerquery = session.createSQLQuery(obquery.toString());
                    for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                        Object[] rows = (Object[]) its.next();
                        String ledgerid = (String) rows[0].toString();
                        if (rows[1] != null) {
                            obdebit = rows[1].toString();
                        }
                        if (rows[2] != null) {
                            obcredit = rows[2].toString();
                        }
                    }

                    if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                        StringBuffer query = new StringBuffer();

                        if ("1".equalsIgnoreCase(reporttype)) {
                            query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                            query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher ");
                            query.append(" where v.accountingperiod='" + period + "'  and vd.region='" + regionid + "' and v.region='" + regionid + "'  and   case when v.voucherapproveddate is null then v.voucherdate between  '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                            query.append(" else  v.voucherapproveddate between  '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end and vd.cancelled is false and v.cancelled is false and vd.accountcode='" + ledger + "' ");
                            query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");

                        } else {
                            query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                            query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher  ");
                            query.append(" left join accountgroups ag on '" + ledger + "' between accheadstartingno and accheadendingno ");
                            query.append(" where v.accountingperiod='" + period + "'  and vd.region='" + regionid + "' and v.region='" + regionid + "'  and   case when v.voucherapproveddate is null then v.voucherdate between  '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                            query.append(" else  v.voucherapproveddate between  '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end and vd.cancelled is false and v.cancelled is false and vd.accountcode='" + ledger + "' ");
                            query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");
                        }

                        ledgerquery = session.createSQLQuery(query.toString());
                        //System.out.println("size:::: " +  ledgerquery.list().size() );
                        for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                            Object[] rows = (Object[]) its.next();
                            String ledgerid = (String) rows[0].toString();

                            if (rows[1] != null) {
                                regioncurrentperioddebit = (BigDecimal) rows[1];
                            }

                            if (rows[2] != null) {
                                regioncurrentperiodcredit = (BigDecimal) rows[2];
                            }
                            debit = debit + regioncurrentperioddebit.doubleValue();
                            credit = credit + regioncurrentperiodcredit.doubleValue();

                        }
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
                fileWriter.append(String.format("%25s", regionid));
                fileWriter.append(String.format("%25s", regionname));
                fileWriter.append(String.format("%25s", regioncurrentperioddebit.toString()));
                fileWriter.append(String.format("%25s", regioncurrentperiodcredit.toString()));

                fileWriter.append(NEW_LINE_SEPARATOR);

            }
            fileWriter.append(String.format("%100s", " ").replaceAll(" ", "-"));
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(String.format("%25s", " "));
            fileWriter.append(String.format("%25s", " "));
            fileWriter.append(String.format("%25s", decimalFormat.format(debit)));
            fileWriter.append(String.format("%25s", decimalFormat.format(credit)));

            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(String.format("%100s", " ").replaceAll(" ", "-"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map LedgerPrintOutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountbookno, String period, String fromdate, String todate, String reporttype, String filePathwithName, String selectedRegion) {
        System.out.println("***************************** AccountReportServiceImpl class BankChallanChequePrintOut method is calling ********************************");
        LoggedInRegion = selectedRegion;
        Map map = new HashMap();
        AccountsModel am = null;
        LedgerReport lr = new LedgerReport();
        LedgerReportIRS iRS = new LedgerReportIRS();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String LEDGERQUERY = null;
        String OPENINGBALANCEQUERY = null;
        try {
            //System.out.println("reporttype::: " + reporttype);
            LEDGERQUERY = "select voucherid, fileno, region, accountbook, "
                    + "voucherdate, narration, accountcode, accname, groupcode, groupname, amount, option, vouno, appdate, "
                    + "case when appdate is null then voucherdate else appdate end as voudate  from "
                    + "(select v.id as voucherid, v.fileno as fileno, rm.regionname as region, v.accountbook as accountbook, "
                    + "v.voucherdate as voucherdate, v.narration as narration, vd.accountcode as accountcode, ah.accname as accname, "
                    + "ah.groupcode as groupcode, ag.groupname as groupname, vd.amount as amount, vd.voucheroption as option, "
                    + "v.voucherno as vouno, v.voucherapproveddate as appdate from voucherdetails vd "
                    + "left join voucher v on v.id=vd.voucher "
                    + "left join regionmaster rm on rm.id=v.region "
                    + "left join accountsheads ah on ah.acccode=vd.accountcode "
                    + "left join accountgroups ag on ag.id=ah.groupcode "
                    + "where "
                    + "case when "
                    + "v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' "
                    + "else  "
                    + "v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end ";
            if ("1".equalsIgnoreCase(reporttype)) {
                LEDGERQUERY = LEDGERQUERY + " and vd.accountcode='" + accountbookno + "' ";
            }
            LEDGERQUERY = LEDGERQUERY + " and '" + accountbookno + "' between ag.accheadstartingno and ag.accheadendingno  "
                    + " and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' "
                    + "and vd.cancelled is false and v.cancelled is false "
                    + " group by ag.grpcode,v.id , v.fileno, rm.regionname, v.accountbook , "
                    + "v.voucherdate, v.narration , vd.accountcode, ah.accname ,ah.groupcode, ag.groupname, vd.amount, vd.voucheroption, "
                    + "v.voucherno, v.voucherapproveddate,vd.serialno  "
                    + "order by vd.accountcode,v.voucherdate,v.voucherapproveddate,v.voucherno,v.id ) as dd order by accountcode,voudate,vouno,voucherid";

//            LEDGERQUERY = "select v.id as voucherid, v.fileno, rm.regionname, v.accountbook, v.voucherdate as voucherdate, v.narration as narration, "
//                    + "vd.accountcode, ah.accname, ah.groupcode, ag.groupname, vd.amount as amount, vd.voucheroption as option, v.voucherno, v.voucherapproveddate "
//                    + "from voucherdetails vd "
//                    + "left join voucher v on v.id=vd.voucher "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsheads ah on ah.acccode=vd.accountcode "
//                    + "left join accountgroups ag on ag.id=ah.groupcode "
//                    + "where "
//                    + "case when "
//                    + "v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' "
//                    + "else  "
//                    + "v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end and vd.accountcode='" + accountbookno + "' "
//                    + "and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' "
//                    + "and vd.cancelled is false and v.cancelled is false "
//                    + "order by v.voucherdate,v.voucherapproveddate,v.voucherno,v.id";

            OPENINGBALANCEQUERY = "select accountcode,coalesce(sum(debit),'0.00') as debit , coalesce(sum(credit),'0.00') as credit from ( select accountcode, "
                    + "case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit, "
                    + "case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit "
                    + "from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' "
                    + "and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(fromdate) + "' "
                    + "else  v.voucherapproveddate < '" + postgresDate(fromdate) + "' end and vd.cancelled is false and v.cancelled is false "
                    + "and vd.accountcode='" + accountbookno + "' "
                    + "GROUP BY accountcode,voucheroption ) as saisiva group by accountcode";


//            LEDGERQUERY = "select v.id,v.fileno,rm.regionname,v.accountbook,v.voucherdate,v.narration,vd.accountcode,ah.accname,"
//                    + "ah.groupcode,ag.groupname,vd.amount,vd.voucheroption from voucher v "
//                    + "left join voucherdetails vd on vd.voucher=v.id "
//                    + "left join regionmaster rm on rm.id=v.region "
//                    + "left join accountsheads ah on ah.acccode=vd.accountcode "
//                    + "left join accountgroups ag on ag.id=ah.groupcode "
//                    + "where v.voucherdate between '" + postgresDate(fromdate) + "' "
//                    + "and '" + postgresDate(todate) + "' and v.region='" + LoggedInRegion + "' "
//                    + "and v.cancelled is false and vd.cancelled is false and v.accountingperiod='" + period + "' and "
//                    + "vd.accountcode='" + accountbookno + "' order by v.voucherdate";


            SQLQuery ledger_query = session.createSQLQuery(LEDGERQUERY);
//            SQLQuery openingbalance_query = session.createSQLQuery(OPENINGBALANCEQUERY);

            //System.out.println("LEDGERQUERY ->" + LEDGERQUERY);
            if (ledger_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            //System.out.println("OPENINGBALANCEQUERY = " + OPENINGBALANCEQUERY);
            double oDebit = 0;
            double oCredit = 0;
//            
//            if (openingbalance_query.list().size() > 0) {
//                Object obj[] = (Object[]) openingbalance_query.list().get(0);
//                System.out.println("openingbalance_query.list().size() = " + openingbalance_query.list().size());
//                BigDecimal openingDebit = (BigDecimal) obj[1];
//                BigDecimal openingCredit = (BigDecimal) obj[2];
//                oDebit = openingDebit.doubleValue();
//                oCredit = openingCredit.doubleValue();
//            }

            for (ListIterator its = ledger_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String vno = (String) rows[0];
                String fileno = (String) rows[1];
                am.setCompno(vno);
                am.setRegion((String) rows[2]);
                am.setAccountbook((String) rows[3]);
                Date vdate = null;
                if (rows[13] != null) {
                    vdate = (Date) rows[13];
                } else {
                    vdate = (Date) rows[4];
                }
                String voucherdate = dateToString(vdate);
                am.setAccdate(voucherdate.substring(0, 6) + voucherdate.substring(8, 10));
                if (((vno.substring(3, 4)).trim()).equals("R")) {
                    String favourof = "";
                    Criteria criteria = session.createCriteria(Receiptpaymentdetails.class);
                    criteria.add(Restrictions.sqlRestriction("voucher = '" + vno + "'"));
                    List favouroflist = criteria.list();
                    if (favouroflist.size() > 0) {
                        Receiptpaymentdetails receiptpaymentdetails = (Receiptpaymentdetails) favouroflist.get(0);
                        favourof = receiptpaymentdetails.getFavourof();
                    }
                    StringBuilder builder = new StringBuilder();
                    String narration = (String) rows[5];
                    builder.append(narration.trim());
                    builder.append(", ");
                    builder.append(fileno);
                    builder.append(", ");
                    builder.append(favourof);
                    am.setDetails(builder.toString());
//                    System.out.println(am.getDetails());
                } else {
                    am.setDetails((String) rows[5]);
                }
                am.setAccountcode((String) rows[6]);
                am.setAccountname((String) rows[7]);
                am.setGroupcode((String) rows[8]);
                am.setGroupname((String) rows[9]);
                BigDecimal bamount = (BigDecimal) rows[10];
                double damount = bamount.doubleValue();
                am.setAmount(decimalFormat.format(damount));
                am.setVoucheroption((String) rows[11]);
                am.setVoucherno((String) rows[12]);
                am.setFromdate(fromdate.substring(0, 3) + months[(Integer.valueOf(fromdate.substring(3, 5))) - 1] + fromdate.substring(5, 10));
                am.setTodate(todate.substring(0, 3) + months[(Integer.valueOf(todate.substring(3, 5))) - 1] + todate.substring(5, 10));
                am.setPageno(String.valueOf(pageno));

                OPENINGBALANCEQUERY = "select accountcode,coalesce(sum(debit),'0.00') as debit , coalesce(sum(credit),'0.00') as credit from ( select accountcode, "
                        + "case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit, "
                        + "case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit "
                        + "from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' "
                        + "and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(fromdate) + "' "
                        + "else  v.voucherapproveddate < '" + postgresDate(fromdate) + "' end and vd.cancelled is false and v.cancelled is false "
                        + "and vd.accountcode='" + am.getAccountcode() + "' "
                        + "GROUP BY accountcode,voucheroption ) as saisiva group by accountcode";

                SQLQuery openingbalance_query = session.createSQLQuery(OPENINGBALANCEQUERY);
                oDebit = 0;
                oCredit = 0;
                if (openingbalance_query.list().size() > 0) {
                    Object obj[] = (Object[]) openingbalance_query.list().get(0);
                    BigDecimal openingDebit = (BigDecimal) obj[1];
                    BigDecimal openingCredit = (BigDecimal) obj[2];
                    oDebit = openingDebit.doubleValue();
                    oCredit = openingCredit.doubleValue();
                }

                am.setDebitamount(decimalFormat.format(oDebit));
                am.setCreditamount(decimalFormat.format(oCredit));

                if (am.getGroupcode().equals("30000")) {
                    iRS.getPrintWriter(am, filePathwithName);
                } else {
                    lr.getPrintWriter(am, filePathwithName);
                }

                pageno++;

                // </editor-fold>
            }
            if (am.getGroupcode().equals("30000")) {
                iRS.GrandTotal(am, filePathwithName);
            } else {
                lr.GrandTotal(am, filePathwithName);
            }

            //System.out.println("in final...");
        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public LinkedList trialBalancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode) {
        System.out.println("***************************** AccountReportServiceImpl class trialBalancePrintOut method is calling ********************************");
        Map map = new HashMap();
        Map cardDetails = null;
//        AccountsModel am = null;
        LinkedList tb = new LinkedList();
//        LedgerReport lr = new LedgerReport();
//        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");

//        int pageno = 1;
//        String LEDGERQUERY = null;
        try {

            if (!fromdate.trim().equalsIgnoreCase("null") && !todate.trim().equalsIgnoreCase("null") && todate.trim().length() > 0) {
                StringBuffer query = new StringBuffer();

                query.append(" select cast(orderno as INTEGER) as orderno,groupname,accname,groupcode,accountcode, ");
                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') > coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when ( groupcode!='30000' and coalesce(sum(debit),'0.00') = coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when (groupcode='30000') then coalesce(sum(debit),'0.00') end ,'0.00') as debit, ");
                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') < coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(credit),'0.00') - coalesce(sum(debit),'0.00')  ");
                query.append(" when ( groupcode!='30000' and coalesce(sum(debit),'0.00') = coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when (groupcode='30000') then coalesce(sum(credit),'0.00') end ,'0.00') as credit ");
//                query.append(" select groupname,accname,groupcode,accountcode,coalesce(sum(debit),'0.00') as debit , coalesce(sum(credit),'0.00') as credit  ");
//                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') >coalesce(sum(credit),'0.00') ) ");
//                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') end ,'0.00') as debit, ");
//                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') < coalesce(sum(credit),'0.00') )  ");
//                query.append(" then coalesce(sum(credit),'0.00') - coalesce(sum(debit),'0.00') end,'0.00') as credit ");
                query.append(" from ( ");
                query.append(" select ag.orderno,groupname,accname,groupcode,accountcode,  ");
                query.append(" case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit, ");
                query.append(" case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt'or vd.voucheroption='Credit'  ");
                query.append(" then sum(vd.amount) end as credit   ");
                query.append(" from voucherdetails vd  ");
                query.append(" left join voucher v on v.id=vd.voucher  ");
                query.append(" left join accountsheads ah on ah.acccode=vd.accountcode  ");
                query.append(" left join accountgroups ag on ag.grpcode=ah.groupcode  ");
                query.append(" where v.accountingperiod='" + periodcode + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'   ");
                query.append(" and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end and vd.cancelled is false and v.cancelled is false  ");
                query.append(" GROUP BY ag.orderno,groupname,accname,groupcode,accountcode,voucheroption ) as x group by orderno,groupname,accname,groupcode,accountcode  order by orderno,groupcode,accountcode ");

                Query tbquery = session.createSQLQuery(query.toString());
                List payList = tbquery.list();
                //System.out.println("query===" + query);

                for (ListIterator its = payList.listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();

                    cardDetails = new HashMap();
//                    cardDetails.put("orderno", (String) rows[0]);
                    cardDetails.put("groupname", (String) rows[1]);
                    cardDetails.put("accountname", (String) rows[2]);
                    cardDetails.put("groupcode", (String) rows[3]);
                    cardDetails.put("accountcode", (String) rows[4]);
                    cardDetails.put("debit", (BigDecimal) rows[5]);
                    cardDetails.put("credit", (BigDecimal) rows[6]);
                    cardDetails.put("regionname", getRegionmaster(session, LoggedInRegion).getRegionname());

                    tb.add(cardDetails);

                    // </editor-fold>
                }
//                SQLQuery ledgerquery = session.createSQLQuery(query.toString());
            }

        } catch (Exception ex) {
            map.put("ERROR", "trialBalancePrintOut Report Generated Error");
            ex.printStackTrace();
        }
        return tb;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public LinkedList consolidatedTrialBalancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode) {
        System.out.println("***************************** AccountReportServiceImpl class trialBalancePrintOut method is calling ********************************");
        Map map = new HashMap();
        Map cardDetails = null;
//        AccountsModel am = null;
        LinkedList tb = new LinkedList();
//        LedgerReport lr = new LedgerReport();
//        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");

//        int pageno = 1;
//        String LEDGERQUERY = null;
        try {

            if (!fromdate.trim().equalsIgnoreCase("null") && !todate.trim().equalsIgnoreCase("null") && todate.trim().length() > 0) {
                StringBuffer query = new StringBuffer();

                query.append(" select cast(orderno as INTEGER) as orderno,groupname,accname,groupcode,accountcode, ");
                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') > coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when ( groupcode!='30000' and coalesce(sum(debit),'0.00') = coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when (groupcode='30000') then coalesce(sum(debit),'0.00') end ,'0.00') as debit, ");
                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') < coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(credit),'0.00') - coalesce(sum(debit),'0.00')  ");
                query.append(" when ( groupcode!='30000' and coalesce(sum(debit),'0.00') = coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when (groupcode='30000') then coalesce(sum(credit),'0.00') end ,'0.00') as credit ");
//                query.append(" select groupname,accname,groupcode,accountcode,coalesce(sum(debit),'0.00') as debit , coalesce(sum(credit),'0.00') as credit  ");
//                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') >coalesce(sum(credit),'0.00') ) ");
//                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') end ,'0.00') as debit, ");
//                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') < coalesce(sum(credit),'0.00') )  ");
//                query.append(" then coalesce(sum(credit),'0.00') - coalesce(sum(debit),'0.00') end,'0.00') as credit ");
                query.append(" from ( ");
                query.append(" select ag.orderno,groupname,accname,groupcode,accountcode,  ");
                query.append(" case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit, ");
                query.append(" case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt'or vd.voucheroption='Credit'  ");
                query.append(" then sum(vd.amount) end as credit   ");
                query.append(" from voucherdetails vd  ");
                query.append(" left join voucher v on v.id=vd.voucher  ");
                query.append(" left join accountsheads ah on ah.acccode=vd.accountcode  ");
                query.append(" left join accountgroups ag on ag.grpcode=ah.groupcode  ");
                query.append(" where v.accountingperiod='" + periodcode + "' ");
                query.append(" and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end and vd.cancelled is false and v.cancelled is false  ");
                query.append(" GROUP BY ag.orderno,groupname,accname,groupcode,accountcode,voucheroption ) as x group by orderno,groupname,accname,groupcode,accountcode  order by orderno,groupcode,accountcode ");

                Query tbquery = session.createSQLQuery(query.toString());
                List payList = tbquery.list();
                //System.out.println("query===" + query);

                for (ListIterator its = payList.listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();

                    cardDetails = new HashMap();
//                    cardDetails.put("orderno", (String) rows[0]);
                    cardDetails.put("groupname", (String) rows[1]);
                    cardDetails.put("accountname", (String) rows[2]);
                    cardDetails.put("groupcode", (String) rows[3]);
                    cardDetails.put("accountcode", (String) rows[4]);
                    cardDetails.put("debit", (BigDecimal) rows[5]);
                    cardDetails.put("credit", (BigDecimal) rows[6]);
                    cardDetails.put("regionname", "All Regions Consolidated");

                    tb.add(cardDetails);

                    // </editor-fold>
                }
//                SQLQuery ledgerquery = session.createSQLQuery(query.toString());
            }

        } catch (Exception ex) {
            map.put("ERROR", "trialBalancePrintOut Report Generated Error");
            ex.printStackTrace();
        }
        return tb;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public LinkedList trialBalancePrintOutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode, String selectedRegion) {
        System.out.println("***************************** AccountReportServiceImpl class trialBalancePrintOut method is calling ********************************");
        Map map = new HashMap();
        LoggedInRegion = selectedRegion;
        Map cardDetails = null;
//        AccountsModel am = null;
        LinkedList tb = new LinkedList();
//        LedgerReport lr = new LedgerReport();
//        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");

//        int pageno = 1;
//        String LEDGERQUERY = null;
        try {

            if (!fromdate.trim().equalsIgnoreCase("null") && !todate.trim().equalsIgnoreCase("null") && todate.trim().length() > 0) {
                StringBuffer query = new StringBuffer();

                query.append(" select cast(orderno as INTEGER) as orderno,groupname,accname,groupcode,accountcode, ");
                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') > coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when ( groupcode!='30000' and coalesce(sum(debit),'0.00') = coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when (groupcode='30000') then coalesce(sum(debit),'0.00') end ,'0.00') as debit, ");
                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') < coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(credit),'0.00') - coalesce(sum(debit),'0.00')  ");
                query.append(" when ( groupcode!='30000' and coalesce(sum(debit),'0.00') = coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when (groupcode='30000') then coalesce(sum(credit),'0.00') end ,'0.00') as credit ");
//                query.append(" select groupname,accname,groupcode,accountcode,coalesce(sum(debit),'0.00') as debit , coalesce(sum(credit),'0.00') as credit  ");
//                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') >coalesce(sum(credit),'0.00') ) ");
//                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') end ,'0.00') as debit, ");
//                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') < coalesce(sum(credit),'0.00') )  ");
//                query.append(" then coalesce(sum(credit),'0.00') - coalesce(sum(debit),'0.00') end,'0.00') as credit ");
                query.append(" from ( ");
                query.append(" select ag.orderno,groupname,accname,groupcode,accountcode,  ");
                query.append(" case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit, ");
                query.append(" case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt'or vd.voucheroption='Credit'  ");
                query.append(" then sum(vd.amount) end as credit   ");
                query.append(" from voucherdetails vd  ");
                query.append(" left join voucher v on v.id=vd.voucher  ");
                query.append(" left join accountsheads ah on ah.acccode=vd.accountcode  ");
                query.append(" left join accountgroups ag on ag.grpcode=ah.groupcode  ");
                query.append(" where v.accountingperiod='" + periodcode + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'   ");
                query.append(" and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end and vd.cancelled is false and v.cancelled is false  ");
                query.append(" GROUP BY ag.orderno,groupname,accname,groupcode,accountcode,voucheroption ) as x group by orderno,groupname,accname,groupcode,accountcode  order by orderno,groupcode,accountcode ");

                Query tbquery = session.createSQLQuery(query.toString());
                List payList = tbquery.list();
                //System.out.println("query===" + query);

                for (ListIterator its = payList.listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();

                    cardDetails = new HashMap();
//                    cardDetails.put("orderno", (String) rows[0]);
                    cardDetails.put("groupname", (String) rows[1]);
                    cardDetails.put("accountname", (String) rows[2]);
                    cardDetails.put("groupcode", (String) rows[3]);
                    cardDetails.put("accountcode", (String) rows[4]);
                    cardDetails.put("debit", (BigDecimal) rows[5]);
                    cardDetails.put("credit", (BigDecimal) rows[6]);
                    cardDetails.put("regionname", getRegionmaster(session, LoggedInRegion).getRegionname());

                    tb.add(cardDetails);

                    // </editor-fold>
                }
//                SQLQuery ledgerquery = session.createSQLQuery(query.toString());
            }

        } catch (Exception ex) {
            map.put("ERROR", "trialBalancePrintOut Report Generated Error");
            ex.printStackTrace();
        }
        return tb;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public LinkedList progressivetrialBalancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode) {
        System.out.println("***************************** AccountReportServiceImpl class trialBalancePrintOut method is calling ********************************");
        Map map = new HashMap();
        Map cardDetails = null;
//        AccountsModel am = null;
        LinkedList tb = new LinkedList();
//        LedgerReport lr = new LedgerReport();
//        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");

//        int pageno = 1;
//        String LEDGERQUERY = null;
        try {

            if (!fromdate.trim().equalsIgnoreCase("null") && !todate.trim().equalsIgnoreCase("null") && todate.trim().length() > 0) {
                StringBuffer query = new StringBuffer();

                query.append(" select cast(orderno as INTEGER) as orderno,groupname,accname,groupcode,accountcode, ");
                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') > coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when ( groupcode!='30000' and coalesce(sum(debit),'0.00') = coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when (groupcode='30000') then coalesce(sum(debit),'0.00') end ,'0.00') as debit, ");
                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') < coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(credit),'0.00') - coalesce(sum(debit),'0.00')  ");
                query.append(" when ( groupcode!='30000' and coalesce(sum(debit),'0.00') = coalesce(sum(credit),'0.00') )  ");
                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') ");
                query.append(" when (groupcode='30000') then coalesce(sum(credit),'0.00') end ,'0.00') as credit ");
//                query.append(" select groupname,accname,groupcode,accountcode,coalesce(sum(debit),'0.00') as debit , coalesce(sum(credit),'0.00') as credit  ");
//                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') >coalesce(sum(credit),'0.00') ) ");
//                query.append(" then coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') end ,'0.00') as debit, ");
//                query.append(" coalesce(case when ( groupcode!='30000' and coalesce(sum(debit),'0.00') < coalesce(sum(credit),'0.00') )  ");
//                query.append(" then coalesce(sum(credit),'0.00') - coalesce(sum(debit),'0.00') end,'0.00') as credit ");
                query.append(" from ( ");
                query.append(" select ag.orderno,groupname,accname,groupcode,accountcode,  ");
                query.append(" case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit, ");
                query.append(" case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt'or vd.voucheroption='Credit'  ");
                query.append(" then sum(vd.amount) end as credit   ");
                query.append(" from voucherdetails vd  ");
                query.append(" left join voucher v on v.id=vd.voucher  ");
                query.append(" left join accountsheads ah on ah.acccode=vd.accountcode  ");
                query.append(" left join accountgroups ag on ag.grpcode=ah.groupcode  ");
                query.append(" where v.accountingperiod='" + periodcode + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'   ");
                query.append(" and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end and vd.cancelled is false and v.cancelled is false  ");
                query.append(" GROUP BY ag.orderno,groupname,accname,groupcode,accountcode,voucheroption ) as x group by orderno,groupname,accname,groupcode,accountcode  order by orderno,groupcode,accountcode ");

                Query tbquery = session.createSQLQuery(query.toString());
                List payList = tbquery.list();
                //System.out.println("query===" + query);

                for (ListIterator its = payList.listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();

                    cardDetails = new HashMap();
//                    cardDetails.put("orderno", (String) rows[0]);
                    cardDetails.put("groupname", (String) rows[1]);
                    cardDetails.put("accountname", (String) rows[2]);
                    cardDetails.put("groupcode", (String) rows[3]);
                    cardDetails.put("accountcode", (String) rows[4]);
                    cardDetails.put("debit", (BigDecimal) rows[5]);
                    cardDetails.put("credit", (BigDecimal) rows[6]);
                    cardDetails.put("regionname", getRegionmaster(session, LoggedInRegion).getRegionname());

                    tb.add(cardDetails);

                    // </editor-fold>
                }
//                SQLQuery ledgerquery = session.createSQLQuery(query.toString());
            }

        } catch (Exception ex) {
            map.put("ERROR", "trialBalancePrintOut Report Generated Error");
            ex.printStackTrace();
        }
        return tb;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public LinkedList ledgerPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode, String accountcode) {
        System.out.println("***************************** AccountReportServiceImpl class ledgerPrint method is calling ********************************");
        Map map = new HashMap();
        Map cardDetails = null;
        LinkedList lederprintlist = new LinkedList();
        try {

            if (!fromdate.trim().equalsIgnoreCase("null") && !todate.trim().equalsIgnoreCase("null") && todate.trim().length() > 0) {
                StringBuffer ledgerquery = new StringBuffer();
                ledgerquery.append(" select v.id,to_char(case when v.voucherapproveddate is null then v.voucherdate  ");
                ledgerquery.append(" else  v.voucherapproveddate  end,'dd/mm/yyyy') as vdate,vd.amount,rpd.favourof ");
                ledgerquery.append(" from voucher v ");
                ledgerquery.append(" left join voucherdetails as vd on vd.voucher=v.id ");
                ledgerquery.append(" left join receiptpaymentdetails as rpd on rpd.voucher=v.id ");
                ledgerquery.append(" where v.vouchertype='R'  and accountcode='" + accountcode + "' and  ");
                ledgerquery.append("  v.accountingperiod='" + periodcode + "'  and rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'   ");
                ledgerquery.append(" and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' ");
                ledgerquery.append(" else  v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end and rpd.cancelled is false and v.cancelled is false  ");


                Query ledgerQuery = session.createSQLQuery(ledgerquery.toString());
                List ledgerList = ledgerQuery.list();
                //System.out.println("ledgerquery===" + ledgerquery);

                for (ListIterator its = ledgerList.listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();

                    cardDetails = new HashMap();
                    cardDetails.put("receiptno", (String) rows[0]);
                    cardDetails.put("voucherdate", (String) rows[1]);
                    cardDetails.put("receiptamt", (BigDecimal) rows[2]);
                    cardDetails.put("receivedfrom", (String) rows[3]);
                    cardDetails.put("regionname", getRegionmaster(session, LoggedInRegion).getRegionname());

                    lederprintlist.add(cardDetails);

                    // </editor-fold>
                }
//                SQLQuery ledgerquery = session.createSQLQuery(query.toString());
            }

        } catch (Exception ex) {
            map.put("ERROR", "ledgerPrint Report Generated Error");
            ex.printStackTrace();
        }
        return lederprintlist;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getReceiptHeadWiseAbstractPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode) {
//        System.out.println("***************************** AccountReportServiceImpl class receiptHeadWiseAbstractPrint method is calling ********************************");
        Map map = new HashMap();
        Map cardDetails = null;
        LinkedList receiptAbstractlist = new LinkedList();
        try {
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            String startYear = "01/04/" + accountingyearObj.getStartyear();
            String endYear = "31/03/" + accountingyearObj.getEndyear();

            if (!fromdate.trim().equalsIgnoreCase("null") && !todate.trim().equalsIgnoreCase("null") && todate.trim().length() > 0) {
                if (DateUtility.DateGreaterThanOrEqual(fromdate, startYear) && DateUtility.DateLessThanOrEqual(todate, endYear)) {

                    StringBuffer query = new StringBuffer();
                    query.append(" select cast(orderno as INTEGER) as orderno,groupname,accname,groupcode,accountcode, ");
                    query.append("  coalesce(sum(credit),'0.00') as credit    ");
                    query.append(" from ( ");
                    query.append(" select ag.orderno,groupname,accname,groupcode,accountcode,  ");
                    query.append(" case when vd.voucheroption='Receipt' then sum(vd.amount) end as credit     ");
                    query.append(" from voucherdetails vd  ");
                    query.append(" left join voucher v on v.id=vd.voucher  ");
                    query.append(" left join accountsheads ah on ah.acccode=vd.accountcode  ");
                    query.append(" left join accountgroups ag on ag.grpcode=ah.groupcode  ");
                    query.append(" where v.accountingperiod='" + periodcode + "' and vouchertype='R' and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'   ");
                    query.append(" and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' ");
                    query.append(" else  v.voucherapproveddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' end and vd.cancelled is false and v.cancelled is false  ");
                    query.append(" GROUP BY ag.orderno,groupname,accname,groupcode,accountcode,voucheroption ) as x group by orderno,groupname,accname,groupcode,accountcode  order by orderno,groupcode,accountcode ");


                    Query receiptQuery = session.createSQLQuery(query.toString());
                    List receiptList = receiptQuery.list();
//                System.out.println("receiptQuery===" + receiptQuery);
                    if (receiptList.size() > 0) {
                        for (ListIterator its = receiptList.listIterator(); its.hasNext();) {
                            Object[] rows = (Object[]) its.next();

                            cardDetails = new HashMap();
//                    cardDetails.put("orderno", (String) rows[0]);
                            cardDetails.put("groupname", (String) rows[1]);
                            cardDetails.put("accountname", (String) rows[2]);
                            cardDetails.put("groupcode", (String) rows[3]);
                            cardDetails.put("accountcode", (String) rows[4]);
                            cardDetails.put("credit", (BigDecimal) rows[5]);

                            receiptAbstractlist.add(cardDetails);
                        }
                        map.put("receiptAbstractlist", receiptAbstractlist);
                        map.put("regionname", getRegionmaster(session, LoggedInRegion).getRegionname());
                        map.put("ERROR", "");

                    } else {
                        map.put("ERROR", "No Record Found");
                    }
                } else {
                    map.put("ERROR", "Given From date and to date  is not in selected accouting period");
                }

            }

        } catch (Exception ex) {
            map.put("ERROR", "receiptHeadWiseAbstractPrint Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getCashBookAbstractPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode, String ctype, String accbook) {
//        System.out.println("***************************** AccountReportServiceImpl class receiptHeadWiseAbstractPrint method is calling ********************************");
        Map map = new HashMap();
        Map cardDetails = null;
        LinkedList cashbookAbstractlist = new LinkedList();
        try {
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            String startYear = "01/04/" + accountingyearObj.getStartyear();
            String endYear = "31/03/" + accountingyearObj.getEndyear();




            if (!fromdate.trim().equalsIgnoreCase("null") && !todate.trim().equalsIgnoreCase("null") && todate.trim().length() > 0) {
                if (DateUtility.DateGreaterThanOrEqual(fromdate, startYear) && DateUtility.DateLessThanOrEqual(todate, endYear)) {
                    StringBuffer query = new StringBuffer();

                    if (ctype.equalsIgnoreCase("R")) {
                        query.append(" select cast(generate_series as date) generate_series, coalesce(sum(credit),'0.00') as totalamount,coalesce(sum(debit),'0.00') as debit, ");
                        query.append(" coalesce(sum(credit),'0.00') as credit ");
                        query.append(" from generate_series('" + postgresDate(fromdate) + "',   '" + postgresDate(todate) + "', cast('1 day' as interval)) generate_series ");
                        query.append(" left join voucher v on v.voucherdate=generate_series and v.vouchertype='R' and v.region='" + LoggedInRegion + "' and v.cancelled is false and  v.accountbook='" + accbook + "'  and  v.accountingperiod='" + periodcode + "'  ");
                        query.append(" left join voucherdetails vd on vd.voucher=v.id and vd.region='" + LoggedInRegion + "' and vd.cancelled is false ");
                        query.append(" GROUP BY    generate_series ");
                        query.append(" order BY    generate_series ");
                    } else if (ctype.equalsIgnoreCase("P")) {
                        query.append(" select cast(generate_series as date)  generate_series,coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') as totalamount,coalesce(sum(debit),'0.00') as  debit,coalesce(sum(credit),'0.00') as credit from ( ");
                        query.append(" select generate_series,debit,coalesce(sum(credit),'0.00') as credit from ( ");
                        query.append(" select  cast(generate_series as date) generate_series, ");
                        query.append(" case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,   ");
                        query.append(" case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt'or vd.voucheroption='Credit'  ");
                        query.append(" then sum(vd.amount) end as credit     ");
                        query.append(" from generate_series('" + postgresDate(fromdate) + "',   '" + postgresDate(todate) + "', cast ('1 day' as interval)) generate_series  ");
                        query.append(" left join voucher v on v.region='" + LoggedInRegion + "' ");
                        query.append(" and case when v.voucherapproveddate is null then v.voucherdate=cast(generate_series as date)  ");
                        query.append(" else  v.voucherapproveddate=cast (generate_series as date)    end  ");
                        query.append(" and v.cancelled is false  ");
                        query.append(" and v.accountingperiod='" + periodcode + "'   and v.vouchertype='" + ctype.toUpperCase() + "'  and  v.accountbook='" + accbook + "' ");
                        query.append(" left join voucherdetails vd on v.id=vd.voucher   and vd.cancelled is false and vd.region='" + LoggedInRegion + "' ");
                        query.append(" left join accountsheads ah on ah.acccode=vd.accountcode   ");
                        query.append(" left join accountgroups ag on ag.grpcode=ah.groupcode   ");
                        query.append(" GROUP BY generate_series,vd.voucheroption order by generate_series ");
                        query.append(" ) as x ");
                        query.append(" GROUP BY generate_series,debit order by generate_series ");
                        query.append(" ) as y ");
                        query.append(" GROUP BY generate_series order by generate_series ");
                    } else if (ctype.equalsIgnoreCase("J")) {
                        query.append(" select cast(generate_series as date)  generate_series,coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') as totalamount,coalesce(sum(debit),'0.00') as  debit,coalesce(sum(credit),'0.00') as credit from ( ");
                        query.append(" select generate_series,debit,coalesce(sum(credit),'0.00') as credit from ( ");
                        query.append(" select  cast(generate_series as date) generate_series, ");
                        query.append(" case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,   ");
                        query.append(" case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt'or vd.voucheroption='Credit'  ");
                        query.append(" then sum(vd.amount) end as credit     ");
                        query.append(" from generate_series('" + postgresDate(fromdate) + "',   '" + postgresDate(todate) + "', cast ('1 day' as interval)) generate_series  ");
                        query.append(" left join voucher v on v.region='" + LoggedInRegion + "' ");
                        query.append(" and case when v.voucherapproveddate is null then v.voucherdate=cast(generate_series as date)  ");
                        query.append(" else  v.voucherapproveddate=cast (generate_series as date)    end  ");
                        query.append(" and v.cancelled is false  ");
                        query.append(" and v.accountingperiod='" + periodcode + "'   and v.vouchertype='" + ctype.toUpperCase() + "'  and  v.accountbook='" + accbook + "' ");
                        query.append(" left join voucherdetails vd on v.id=vd.voucher   and vd.cancelled is false and vd.region='" + LoggedInRegion + "' ");
                        query.append(" left join accountsheads ah on ah.acccode=vd.accountcode   ");
                        query.append(" left join accountgroups ag on ag.grpcode=ah.groupcode   ");
                        query.append(" GROUP BY generate_series,vd.voucheroption order by generate_series ");
                        query.append(" ) as x ");
                        query.append(" GROUP BY generate_series,debit order by generate_series ");
                        query.append(" ) as y ");
                        query.append(" GROUP BY generate_series order by generate_series ");

                    } else if (ctype.equalsIgnoreCase("B")) {
                        query.append(" select cast(generate_series as date)  generate_series,coalesce(sum(debit),'0.00') - coalesce(sum(credit),'0.00') as totalamount,coalesce(sum(debit),'0.00') as  debit,coalesce(sum(credit),'0.00') as credit from ( ");
                        query.append(" select generate_series,debit,coalesce(sum(credit),'0.00') as credit from ( ");
                        query.append(" select  cast(generate_series as date) generate_series, ");
                        query.append(" case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,   ");
                        query.append(" case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt'or vd.voucheroption='Credit'  ");
                        query.append(" then sum(vd.amount) end as credit     ");
                        query.append(" from generate_series('" + postgresDate(fromdate) + "',   '" + postgresDate(todate) + "', cast ('1 day' as interval)) generate_series  ");
                        query.append(" left join voucher v on v.region='" + LoggedInRegion + "' ");
                        query.append(" and case when v.voucherapproveddate is null then v.voucherdate=cast(generate_series as date)  ");
                        query.append(" else  v.voucherapproveddate=cast (generate_series as date)    end  ");
                        query.append(" and v.cancelled is false  ");
                        query.append(" and v.accountingperiod='" + periodcode + "'   and v.vouchertype='P'  and  v.accountbook='" + accbook + "' ");
                        query.append(" left join voucherdetails vd on v.id=vd.voucher   and vd.cancelled is false and vd.region='" + LoggedInRegion + "' ");
                        query.append(" left join accountsheads ah on ah.acccode=vd.accountcode   ");
                        query.append(" left join accountgroups ag on ag.grpcode=ah.groupcode   ");
                        query.append(" GROUP BY generate_series,vd.voucheroption order by generate_series ");
                        query.append(" ) as x ");
                        query.append(" GROUP BY generate_series,debit order by generate_series ");
                        query.append(" ) as y ");
                        query.append(" GROUP BY generate_series order by generate_series ");
                    }


                    SQLQuery receiptQuery = session.createSQLQuery(query.toString());
                    List receiptList = receiptQuery.list();
                    //System.out.println("query===" + query.toString());
                    if (receiptList.size() > 0) {
                        for (ListIterator its = receiptList.listIterator(); its.hasNext();) {
                            Object[] rows = (Object[]) its.next();

                            cardDetails = new HashMap();
                            cardDetails.put("serialdate", dateToString((java.sql.Date) rows[0]));
                            cardDetails.put("totalamount", (BigDecimal) rows[1]);
                            cardDetails.put("debit", (BigDecimal) rows[2]);
                            cardDetails.put("credit", (BigDecimal) rows[3]);

                            cashbookAbstractlist.add(cardDetails);
                        }
                        map.put("cashbookAbstractlist", cashbookAbstractlist);
                        map.put("regionname", getRegionmaster(session, LoggedInRegion).getRegionname());
                        map.put("ERROR", "");

                    } else {
                        map.put("ERROR", "No Record Found");
                    }
                } else {
                    map.put("ERROR", "Given From date and to date  is not in selected accouting period");
                }

            }

        } catch (Exception ex) {
            map.put("ERROR", "receiptHeadWiseAbstractPrint Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map PurchaseVatBreakupPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String percentage, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class PurchaseVatBreakupPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        PurchaseVatBreakupReport pvbr = new PurchaseVatBreakupReport();
        LedgerReport lr = new LedgerReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String COMMODITYQUERY = null;
        String REGIONWISEQUERY = null;
        try {
//            System.out.println("month = " + month);
//            System.out.println("year = " + year);
//            System.out.println("percentage = " + percentage);
//            System.out.println("filePathwithName = " + filePathwithName);
            BigDecimal vatpercentage = new BigDecimal(Double.valueOf(percentage));
            int MONTH = Integer.valueOf(month) + 1;
            int YEAR = Integer.valueOf(year);

            COMMODITYQUERY = "select vp.commodity,cm.name,cm.commodityorder from vatonpurchase vp "
                    + "left join commoditymaster cm on cm.id=vp.commodity "
                    + "where vp.taxpercentage=" + vatpercentage + " and vp.month=" + MONTH + " and vp.year=" + YEAR + " and vp.cancelled is false "
                    + "group by vp.commodity,cm.name,cm.commodityorder order by cm.commodityorder";

            REGIONWISEQUERY = "select vp.regionmaster,rm.regionname,vp.commodity,sum(vp.totamount) from vatonpurchase vp "
                    + "left join regionmaster rm on rm.id=vp.regionmaster "
                    + "where vp.taxpercentage=" + vatpercentage + " and vp.month=" + MONTH + " and vp.year=" + YEAR + " and vp.cancelled is false "
                    + "group by vp.regionmaster,rm.regionname,vp.commodity order by vp.regionmaster";

            SQLQuery commodity_query = session.createSQLQuery(COMMODITYQUERY);
            SQLQuery regionwise_query = session.createSQLQuery(REGIONWISEQUERY);

//            System.out.println("COMMODITYQUERY ->" + COMMODITYQUERY);
            if (commodity_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            Map<String, String> commoditymap = new LinkedHashMap<String, String>();
            List<String> commoditylist = new ArrayList<String>();

            for (ListIterator its = commodity_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                String commoditycode = ((String) rows[0]).trim();
                String commodityname = ((String) rows[1]).trim();
                commoditymap.put(commoditycode, commodityname);
                commoditylist.add(commoditycode);
                // </editor-fold>
            }

            double[] comtotal = new double[commoditymap.size() + 1];

            for (int i = 0; i < comtotal.length; i++) {
                comtotal[i] = 0;
            }

//            for (String commname : commoditymap.values()) {
//                System.out.println("Commodity Name = " + commname);
//            }
//
//            Iterator itr = commoditymap.entrySet().iterator();
//            while (itr.hasNext()) {
//                Map.Entry m = (Entry) itr.next();
//                System.out.println(m.getKey() + " -> " + m.getValue());
//            }

            String rcode = null;
            int i = 1;

            Map<String, Map> regionwisemap = new LinkedHashMap<String, Map>();
            Map<String, Double> submap = null;

            for (ListIterator its = regionwise_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                String regioncode = ((String) rows[0]).trim();
                String regionname = ((String) rows[1]).trim();
                String commcode = ((String) rows[2]).trim();
                BigDecimal bamount = (BigDecimal) rows[3];
                double amount = bamount.doubleValue();

                if (i == 1) {
                    rcode = regionname;
                    submap = new HashMap();
                }

                if (!rcode.equals(regionname)) {
                    regionwisemap.put(rcode, submap);
                    submap = new HashMap<String, Double>();
                    rcode = regionname;
                }
                submap.put(commcode, amount);
                i++;
                // </editor-fold>
            }
            regionwisemap.put(rcode, submap);

//            System.out.println("******************* Display regionwisemap **********************");
//
//            itr = regionwisemap.entrySet().iterator();
//            while (itr.hasNext()) {
//                Map.Entry m = (Entry) itr.next();
//                System.out.println(m.getKey() + " -> " + m.getValue());
//                Map smap= (Map) m.getValue();
//                Iterator it = smap.entrySet().iterator();
//                while(it.hasNext()){
//                    Map.Entry sm = (Entry) it.next();
//                    System.out.println(m.getKey()+" - "+sm.getKey()+" - "+sm.getValue());
//                }
//            }


            Iterator itr = regionwisemap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry m = (Entry) itr.next();
                Map<String, Double> smap = (Map) m.getValue();

                Iterator it = commoditylist.iterator();
                while (it.hasNext()) {
                    String ccode = (String) it.next();
                    if (smap.get(ccode) == null) {
                        smap.put(ccode, 0.00);
                    }
                }
                regionwisemap.put((String) m.getKey(), smap);
            }

            List<String> regionwiseList = null;
            Map<String, List> AllregionCommodityMap = new LinkedHashMap<String, List>();

            itr = regionwisemap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry m = (Entry) itr.next();
                Map<String, Double> regmap = (Map) m.getValue();

                regionwiseList = new ArrayList<String>();
                Iterator it = commoditylist.iterator();
                double totalamount = 0;
                int t = 0;
                while (it.hasNext()) {
                    String ccode = (String) it.next();
                    totalamount += (Double) regmap.get(ccode);
                    comtotal[t] += (Double) regmap.get(ccode);
                    regionwiseList.add(decimalFormat.format((regmap.get(ccode))));
                    t++;
                }
                comtotal[t] += totalamount;
                regionwiseList.add(decimalFormat.format(totalamount));
                AllregionCommodityMap.put((String) m.getKey(), regionwiseList);
            }
            regionwiseList = new ArrayList<String>();
            for (int a = 0; a < comtotal.length; a++) {
                regionwiseList.add(decimalFormat.format(comtotal[a]));
            }
            AllregionCommodityMap.put("TOTAL", regionwiseList);

//            Iterator iterator = AllregionCommodityMap.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry ma = (Entry) iterator.next();
//                System.out.print(ma.getKey() + "\t");
//                List list = (List) ma.getValue();
//                Iterator iit = list.iterator();
//                while (iit.hasNext()) {
//                    System.out.print(iit.next() + "\t");
//                }
//                System.out.println();
//            }

            am = new AccountsModel();
            am.setAccdate(months[Integer.valueOf(month)] + "/" + year);
            am.setTaxpercentage(percentage + "%");
            am.setCommoditymap(commoditymap);
            am.setAllregionCommodityMap(AllregionCommodityMap);
            pvbr.GeneratePurchaseVatBreakupXML(am, filePathwithName);


        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map SalesVatBreakupPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String percentage, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class SalesVatBreakupPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        SalesVatBreakupReport svbr = new SalesVatBreakupReport();
        LedgerReport lr = new LedgerReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String COMMODITYQUERY = null;
        String REGIONWISEQUERY = null;
        try {
//            System.out.println("month = " + month);
//            System.out.println("year = " + year);
//            System.out.println("percentage = " + percentage);
//            System.out.println("filePathwithName = " + filePathwithName);
            BigDecimal vatpercentage = new BigDecimal(Double.valueOf(percentage));
            int MONTH = Integer.valueOf(month) + 1;
            int YEAR = Integer.valueOf(year);

            COMMODITYQUERY = "select vs.commodity,cm.name,cm.commodityorder from vatonsales vs "
                    + "left join commoditymaster cm on cm.id=vs.commodity "
                    + "where vs.taxpercentage= " + vatpercentage + " and vs.month= " + MONTH + " and vs.year= " + YEAR + " and vs.cancelled is false "
                    + "group by vs.commodity,cm.name,cm.commodityorder order by cm.commodityorder";

            REGIONWISEQUERY = "select vs.regionmaster,rm.regionname,vs.commodity,sum(vs.totamount) from vatonsales vs "
                    + "left join regionmaster rm on rm.id=vs.regionmaster "
                    + "where vs.taxpercentage=" + vatpercentage + " and vs.month=" + MONTH + " and vs.year=" + YEAR + " and vs.cancelled is false "
                    + "group by vs.regionmaster,rm.regionname,vs.commodity order by vs.commodity";

            SQLQuery commodity_query = session.createSQLQuery(COMMODITYQUERY);
            SQLQuery regionwise_query = session.createSQLQuery(REGIONWISEQUERY);

//            System.out.println("COMMODITYQUERY ->" + COMMODITYQUERY);
            if (commodity_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            Map<String, String> commoditymap = new LinkedHashMap<String, String>();
            List<String> commoditylist = new ArrayList<String>();

            for (ListIterator its = commodity_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                String commoditycode = ((String) rows[0]).trim();
                String commodityname = ((String) rows[1]).trim();
                commoditymap.put(commoditycode, commodityname);
                commoditylist.add(commoditycode);
                // </editor-fold>
            }

            double[] comtotal = new double[commoditymap.size() + 1];

            for (int i = 0; i < comtotal.length; i++) {
                comtotal[i] = 0;
            }

//            for (String commname : commoditymap.values()) {
//                System.out.println("Commodity Name = " + commname);
//            }
//
//            Iterator itr = commoditymap.entrySet().iterator();
//            while (itr.hasNext()) {
//                Map.Entry m = (Entry) itr.next();
////                System.out.println(m.getKey() + " -> " + m.getValue());
//            }

            String rcode = null;
            int i = 1;

            Map<String, Map> regionwisemap = new LinkedHashMap<String, Map>();
            Map<String, Double> submap = null;

            for (ListIterator its = regionwise_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                String regioncode = ((String) rows[0]).trim();
                String regionname = ((String) rows[1]).trim();
                String commcode = ((String) rows[2]).trim();
                BigDecimal bamount = (BigDecimal) rows[3];
                double amount = bamount.doubleValue();

                if (i == 1) {
                    rcode = regionname;
                    submap = new HashMap();
                }

                if (!rcode.equals(regionname)) {
                    regionwisemap.put(rcode, submap);
                    submap = new HashMap<String, Double>();
                    rcode = regionname;
                }
                submap.put(commcode, amount);
                i++;
                // </editor-fold>
            }
            regionwisemap.put(rcode, submap);

//            System.out.println("******************* Display regionwisemap **********************");
//
//            itr = regionwisemap.entrySet().iterator();
//            while (itr.hasNext()) {
//                Map.Entry m = (Entry) itr.next();
//                System.out.println(m.getKey() + " -> " + m.getValue());
//                Map smap= (Map) m.getValue();
//                Iterator it = smap.entrySet().iterator();
//                while(it.hasNext()){
//                    Map.Entry sm = (Entry) it.next();
//                    System.out.println(m.getKey()+" - "+sm.getKey()+" - "+sm.getValue());
//                }
//            }


            Iterator itr = regionwisemap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry m = (Entry) itr.next();
                Map<String, Double> smap = (Map) m.getValue();

                Iterator it = commoditylist.iterator();
                while (it.hasNext()) {
                    String ccode = (String) it.next();
                    if (smap.get(ccode) == null) {
                        smap.put(ccode, 0.00);
                    }
                }
                regionwisemap.put((String) m.getKey(), smap);
            }

            List<String> regionwiseList = null;
            Map<String, List> AllregionCommodityMap = new LinkedHashMap<String, List>();

            itr = regionwisemap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry m = (Entry) itr.next();
                Map<String, Double> regmap = (Map) m.getValue();

                regionwiseList = new ArrayList<String>();
                Iterator it = commoditylist.iterator();
                double totalamount = 0;
                int t = 0;
                while (it.hasNext()) {
                    String ccode = (String) it.next();
                    totalamount += (Double) regmap.get(ccode);
                    comtotal[t] += (Double) regmap.get(ccode);
                    regionwiseList.add(decimalFormat.format((regmap.get(ccode))));
                    t++;
                }
                comtotal[t] += totalamount;
                regionwiseList.add(decimalFormat.format(totalamount));
                AllregionCommodityMap.put((String) m.getKey(), regionwiseList);
            }
            regionwiseList = new ArrayList<String>();
            for (int a = 0; a < comtotal.length; a++) {
                regionwiseList.add(decimalFormat.format(comtotal[a]));
            }
            AllregionCommodityMap.put("TOTAL", regionwiseList);

//            Iterator iterator = AllregionCommodityMap.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry ma = (Entry) iterator.next();
//                System.out.print(ma.getKey() + "\t");
//                List list = (List) ma.getValue();
//                Iterator iit = list.iterator();
//                while (iit.hasNext()) {
//                    System.out.print(iit.next() + "\t");
//                }
//                System.out.println();
//            }

            am = new AccountsModel();
            am.setAccdate(months[Integer.valueOf(month)] + "/" + year);
            am.setTaxpercentage(percentage + "%");
            am.setCommoditymap(commoditymap);
            am.setAllregionCommodityMap(AllregionCommodityMap);
            svbr.GeneratePurchaseVatBreakupXML(am, filePathwithName);


        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map PurchaseVatAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class PurchaseVatAbstractPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        PurchaseVatAbstractReport pvar = new PurchaseVatAbstractReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int slipno = 1;
        String ABSTRACTQUERY = null;
        int MONTH = Integer.valueOf(month) + 1;
        int YEAR = Integer.valueOf(year);
        try {

            ABSTRACTQUERY = "SELECT cm.name,vp.taxpercentage,sum(vp.taxamount) as taxamount,"
                    + "sum(vp.totamount) as totalamount FROM vatonpurchase vp "
                    + "left join commoditymaster cm on cm.id=vp.commodity "
                    + "where vp.month = " + MONTH + " and vp.year = " + YEAR + " and vp.cancelled is false "
                    + "group by cm.name,vp.taxpercentage order by vp.taxpercentage";

            SQLQuery abstract_query = session.createSQLQuery(ABSTRACTQUERY);

            //System.out.println("ABSTRACTQUERY ->" + ABSTRACTQUERY);
            if (abstract_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = abstract_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String commodity = (String) rows[0];
                BigDecimal percen = (BigDecimal) rows[1];
                double taxpercentage = percen.doubleValue();
                BigDecimal btaxamount = (BigDecimal) rows[2];
                double taxamount = btaxamount.doubleValue();
                BigDecimal btotalamount = (BigDecimal) rows[3];
                double totalamount = btotalamount.doubleValue();

                am.setCommodityname(SubString(commodity, 44));
                am.setTaxpercentage(decimalFormat.format(taxpercentage));
                am.setAccdate(months[Integer.valueOf(month)] + "/" + year);
                am.setTaxamount(decimalFormat.format(taxamount));
                am.setTotalamount(decimalFormat.format(totalamount));
                am.setSlipno(String.valueOf(slipno));

                pvar.getPrintWriter(am, filePathwithName);
                slipno++;
                // </editor-fold>
            }
            pvar.GrandTotal(am, filePathwithName);

        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map SalesVatAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class SalesVatAbstractPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        SalesVatAbstractReport svar = new SalesVatAbstractReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int slipno = 1;
        String ABSTRACTQUERY = null;
        int MONTH = Integer.valueOf(month) + 1;
        int YEAR = Integer.valueOf(year);
        try {

            ABSTRACTQUERY = "SELECT cm.name,vs.taxpercentage,sum(vs.taxamount) as taxamount,"
                    + "sum(vs.totamount) as totalamount FROM vatonsales vs "
                    + "left join commoditymaster cm on cm.id=vs.commodity "
                    + "where vs.month =" + MONTH + " and vs.year =" + YEAR + " and vs.cancelled is false "
                    + "group by cm.name,vs.taxpercentage order by vs.taxpercentage";

            SQLQuery abstract_query = session.createSQLQuery(ABSTRACTQUERY);

            //System.out.println("ABSTRACTQUERY ->" + ABSTRACTQUERY);
            if (abstract_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = abstract_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                am = new AccountsModel();
                Object[] rows = (Object[]) its.next();
                String commodity = (String) rows[0];
                BigDecimal percen = (BigDecimal) rows[1];
                double taxpercentage = percen.doubleValue();
                BigDecimal btaxamount = (BigDecimal) rows[2];
                double taxamount = btaxamount.doubleValue();
                BigDecimal btotalamount = (BigDecimal) rows[3];
                double totalamount = btotalamount.doubleValue();

                am.setCommodityname(SubString(commodity, 44));
                am.setTaxpercentage(decimalFormat.format(taxpercentage));
                am.setAccdate(months[Integer.valueOf(month)] + "/" + year);
                am.setTaxamount(decimalFormat.format(taxamount));
                am.setTotalamount(decimalFormat.format(totalamount));
                am.setSlipno(String.valueOf(slipno));

                svar.getPrintWriter(am, filePathwithName);
                slipno++;
                // </editor-fold>
            }
            svar.GrandTotal(am, filePathwithName);

        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    private String SubString(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        } else {
            return str;
        }
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getAccountBook(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        System.out.println("***************************** AccountReportServiceImpl class getAccountBook method is calling ********************************");
        StringBuilder builder = new StringBuilder();
        try {

            String GETACCOUNTBOOK = "SELECT code, bookname  FROM accountsbooks";

            SQLQuery getAccountBook_query = session.createSQLQuery(GETACCOUNTBOOK);

            //System.out.println("GETACCOUNTBOOK ->" + GETACCOUNTBOOK);
            builder.append("<select class=\"combobox\" name=\"type\" id=\"type\">");

            for (ListIterator its = getAccountBook_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                String code = (String) rows[0];
                String bookname = (String) rows[1];
                builder.append("<option value=\"" + code + "\">" + bookname + "</option>");
                // </editor-fold>
            }
            builder.append("</select>");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getChallanDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String challantype, String challanno) {
        System.out.println("***************************** AccountReportServiceImpl class getChallanDetails method is calling ********************************");
        Map map = new HashMap();
        //System.out.println("startingdate = " + startingdate);
        //System.out.println("enddate = " + enddate);
        //System.out.println("challanno = " + challanno);
        //System.out.println("challantype = " + challantype);
        try {
            StringBuffer buffer = new StringBuffer();
            String QUERY = null;
            if (challanno.length() > 0) {
                QUERY = "select bc.id,bc.challandate,bl.bankname,bc.type from bankchallan bc "
                        + "left join bankledger bl on bl.code=bc.bank "
                        + "where "
                        + "bc.challandate between '" + postgresDate(startingdate) + "' "
                        + "and '" + postgresDate(enddate) + "' "
                        + "and bc.region='" + LoggedInRegion + "' "
                        + "and bc.cancelled is false "
                        + "and bc.type='" + challantype + "' "
                        + "and bc.id='" + challanno + "' "
                        + "and bc.id in (select bankchallan from receiptpaymentdetails GROUP BY bankchallan) "
                        + "order by bc.challandate,bc.id";

//                QUERY = "select bc.id,bc.challandate,bl.bankname,bc.type from bankchallan bc "
//                        + "left join bankledger bl on bl.code=bc.bank "
//                        + "where "
//                        + "bc.challandate between '" + postgresDate(startingdate) + "' "
//                        + "and '" + postgresDate(enddate) + "' "
//                        + "and bc.region='" + LoggedInRegion + "' "
//                        + "and bc.cancelled is false "
//                        + "and bc.type='" + challantype + "' "
//                        + "and bc.id='" + challanno + "' "
//                        + "order by bc.id,bc.challandate";
            } else if (challanno.length() == 0) {
                QUERY = "select bc.id,bc.challandate,bl.bankname,bc.type from bankchallan bc "
                        + "left join bankledger bl on bl.code=bc.bank "
                        + "where "
                        + "bc.challandate between '" + postgresDate(startingdate) + "' "
                        + "and '" + postgresDate(enddate) + "' "
                        + "and bc.region='" + LoggedInRegion + "' "
                        + "and bc.cancelled is false "
                        + "and bc.type='" + challantype + "' "
                        + "and bc.id in (select bankchallan from receiptpaymentdetails GROUP BY bankchallan) "
                        + "order by bc.challandate,bc.id";

//                QUERY = "select bc.id,bc.challandate,bl.bankname,bc.type from bankchallan bc "
//                        + "left join bankledger bl on bl.code=bc.bank "
//                        + "where "
//                        + "bc.challandate between '" + postgresDate(startingdate) + "' "
//                        + "and '" + postgresDate(enddate) + "' "
//                        + "and bc.region='" + LoggedInRegion + "' "
//                        + "and bc.cancelled is false "
//                        + "and bc.type='" + challantype + "' "
//                        + "order by bc.id,bc.challandate";
            }

            SQLQuery voucher_query = session.createSQLQuery(QUERY);

            //System.out.println("voucher_query -> " + QUERY);

            if (voucher_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            } else {
                String chno;
                String challandate;
                String bankname;
                String paymenttype;
                String classname = "";

                buffer.append("<table width=\"60%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                buffer.append("<tr class=\"gridmenu\">");
                buffer.append("<td>S.No</td>");
                buffer.append("<td>Challan No</td>");
                buffer.append("<td>Date</td>");
                buffer.append("<td>Bank Name</td>");
                buffer.append("<td>Payment Type</td>");
                buffer.append("<td width=\"5%\">");
//                buffer.append("<input type=\"checkbox\" name=\"billheader\" id=\"billheader\" value=\"billno\" onclick=\"SelectAll(this)\">");
                buffer.append("</td>");
                buffer.append("</tr>");

                int i = 0;
                for (ListIterator its = voucher_query.list().listIterator(); its.hasNext();) {
                    chno = "";
                    challandate = "";
                    bankname = "";
                    paymenttype = "";

                    Object[] rows = (Object[]) its.next();
                    chno = (String) rows[0];
                    Date date = (Date) rows[1];
                    bankname = (String) rows[2];
                    paymenttype = (String) rows[3];

                    challandate = dateToString(date);

                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }

                    buffer.append("<tr class=\"" + classname + "\">");
                    buffer.append("<td align=\"center\">" + (i + 1) + "</td>");
                    buffer.append("<td align=\"center\">" + chno + "</td>");
                    buffer.append("<td align=\"center\">" + challandate + "</td>");
                    buffer.append("<td align=\"center\">" + bankname + "</td>");
                    buffer.append("<td align=\"center\">" + (paymenttype.equals("cash") ? "Cash" : "Cheque/DD") + "</td>");
                    buffer.append("<td align=\"center\">");
                    buffer.append("<input type=\"radio\" name=\"vno\" id=\"vno\" value=" + chno + ">");
                    buffer.append("</td>");

                    buffer.append("</tr>");
                    i++;
                }
                buffer.append("</table>");
                map.put("challangrid", buffer.toString());
                map.put("challantype", challantype);
            }
        } catch (Exception ex) {
            map.put("ERROR", "Given the Input is Invalid");
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map PurchaseVatExportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class PurchaseVatBreakupPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        PurchaseVatExportReport pver = new PurchaseVatExportReport();
        LedgerReport lr = new LedgerReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String QUERY = null;
        try {
            int MONTH = Integer.valueOf(month) + 1;
            int YEAR = Integer.valueOf(year);

            QUERY = "select pl.partyname, pl.tinno, cm.code, vp.billno, vp.billdate, vp.amount, vp.taxpercentage, vp.taxamount from vatonpurchase vp "
                    + "left join partyledger pl on pl.code=vp.partyledger "
                    + "left join commoditymaster cm on cm.id=vp.commodity "
                    + "where vp.cancelled is false and vp.month=" + MONTH + " and "
                    + "vp.year=" + YEAR + " order by pl.partyname,cm.code";

            SQLQuery query = session.createSQLQuery(QUERY);

//            System.out.println("COMMODITYQUERY ->" + COMMODITYQUERY);
            if (query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            List<AccountsModel> purchaselist = new ArrayList<AccountsModel>();

            for (ListIterator its = query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                am = new AccountsModel();
                am.setCompanyname((String) rows[0]);
                am.setTinno((String) rows[1]);
                am.setCommoditycode((String) rows[2]);
                am.setBillno((String) rows[3]);
                Date billdate = (Date) rows[4];
                am.setAccdate(dateToString(billdate));
                BigDecimal bigamount = (BigDecimal) rows[5];
                BigDecimal taxpercentage = (BigDecimal) rows[6];
                BigDecimal taxamount = (BigDecimal) rows[7];
                am.setAmount(decimalFormat.format(bigamount.doubleValue()));
                am.setTaxpercentage(decimalFormat.format(taxpercentage.doubleValue()));
                am.setTaxamount(decimalFormat.format(taxamount.doubleValue()));
                am.setPageno(String.valueOf(pageno));
                purchaselist.add(am);
                pageno++;
                // </editor-fold>
            }

            pver.GeneratePurchaseVatExportXML(purchaselist, filePathwithName);


        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map SalesVatExportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class SalesVatExportPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        SalesVatExportReport sver = new SalesVatExportReport();
        LedgerReport lr = new LedgerReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("####0.0");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String QUERY = null;
        try {
            int MONTH = Integer.valueOf(month) + 1;
            int YEAR = Integer.valueOf(year);

            QUERY = "select pl.partyname, pl.tinno, cm.code, vs.amount, vs.taxpercentage, vs.taxamount from vatonsales vs "
                    + "left join partyledger pl on pl.code=vs.partyledger "
                    + "left join commoditymaster cm on cm.id=vs.commodity "
                    + "where vs.cancelled is false and vs.month=" + MONTH + " and "
                    + "vs.year=" + YEAR + " order by vs.taxpercentage,cm.code";

//            QUERY = "select pl.partyname, pl.tinno, cm.code, vp.billno, vp.billdate, vp.amount, vp.taxpercentage, vp.taxamount from vatonpurchase vp "
//                    + "left join partyledger pl on pl.code=vp.partyledger "
//                    + "left join commoditymaster cm on cm.id=vp.commodity "
//                    + "where vp.cancelled is false and vp.month=" + MONTH + " and "
//                    + "vp.year=" + YEAR + " order by pl.partyname,cm.code";

            SQLQuery query = session.createSQLQuery(QUERY);

//            System.out.println("COMMODITYQUERY ->" + COMMODITYQUERY);
            if (query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            List<AccountsModel> saleslist = new ArrayList<AccountsModel>();

            for (ListIterator its = query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                am = new AccountsModel();

                if (rows[0] == null) {
                    am.setCompanyname("");
                } else {
                    am.setCompanyname((String) rows[0]);
                }

                if (rows[1] == null) {
                    am.setTinno("0");
                } else {
                    am.setTinno((String) rows[1]);
                }

                am.setCommoditycode((String) rows[2]);
                am.setBillno("");
                am.setAccdate("");
                BigDecimal bigamount = (BigDecimal) rows[3];
                BigDecimal taxpercentage = (BigDecimal) rows[4];
                BigDecimal taxamount = (BigDecimal) rows[5];
                am.setAmount(decimalFormat.format(bigamount.doubleValue()));
                am.setTaxpercentage(decimalFormat.format(taxpercentage.doubleValue()));
                am.setTaxamount(decimalFormat.format(taxamount.doubleValue()));
                am.setPageno(String.valueOf(pageno));
                saleslist.add(am);
                pageno++;
                // </editor-fold>
            }

            sver.GeneratePurchaseVatExportXML(saleslist, filePathwithName);


        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map PaymentRealizationPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String periodsele, String voucherdatefrom, String voucherdateto, String book, String realizationstatus, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class PaymentRealizationPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        PaymentRealizationReport prr = new PaymentRealizationReport();
        LedgerReport lr = new LedgerReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String QUERY = null;
        try {

            boolean isrealized = Boolean.parseBoolean(realizationstatus.trim());

            StringBuilder sb = new StringBuilder();

            sb.append("select voucherid,voucherdate,narration,accountbook, recppayid,partyname, otherbankname, refno,paymentmode,amount,challanid,challandate,bankcode,bankname,");
            sb.append(" bankbranchname,bankaccountno,realized,realizeddate,vnx, cast(REPLACE(TRANSLATE(xx, REPLACE(TRANSLATE(xx,'0123456789', RPAD('#',LENGTH(xx),'#')),'#',''),  RPAD('#',LENGTH(xx),'#')),'#','') as INTEGER) as xxx,chequedate   from ");
            sb.append(" (select v.id as voucherid, case when v.voucherapproveddate is null then v.voucherdate else v.voucherapproveddate end as voucherdate, v.narration as narration,ab.bookname as accountbook, rpd.id as recppayid, rpd.favourof as partyname ,rpd.otherbankname as otherbankname,");
            sb.append(" rpd.refno as refno, pm.type as paymentmode, rpd.amount as amount, rpd.bankchallan as challanid, bc.challandate as challandate, bc.bank as bankcode, bl.bankname as bankname,");
            sb.append(" bl.branchname as bankbranchname, bl.accountno as bankaccountno, rpd.realized as realized, rpd.realizeddate as realizeddate, v.voucherno as vnx, case when position('-' in v.voucherno)>0 then trim(split_part(v.voucherno,'-',1)) else case when position('/' in v.voucherno)>=0 then trim(split_part(v.voucherno,'/',1)) else v.voucherno end  end as xx, rpd.chequedate as chequedate ");
            sb.append(" from receiptpaymentdetails rpd ");
            sb.append(" left join voucher v on v.id=rpd.voucher ");
            sb.append(" left join partyledger pl on pl.code=rpd.partyledger ");
            sb.append(" left join bankchallan bc on bc.id=rpd.bankchallan ");
            sb.append(" left join bankledger bl on bl.code=rpd.bankname ");
            sb.append(" left join paymentmode pm on pm.code=rpd.paymentmode ");
            sb.append(" left join accountsbooks ab on ab.code=v.accountbook ");
            sb.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "'");
            sb.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end  ");
            sb.append(" and (rpd.realized is " + isrealized + "  or rpd.realized is null) and v.accountbook='" + book + "' and  rpd.region='" + LoggedInRegion + "' ");
            sb.append(" and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + periodsele + "' and rpd.cancelled is false and v.cancelled is false ");
            sb.append(" and rpd.paymentmode in ('2','3') and v.vouchertype='P') as x");
            sb.append(" order by voucherdate, xxx");

            QUERY = sb.toString();

            //System.out.println("Query  -> " + QUERY);

            Criteria reCrit = session.createCriteria(Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";
            if (reList.size() > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }

//            QUERY = "select pl.partyname, pl.tinno, cm.code, vp.billno, vp.billdate, vp.amount, vp.taxpercentage, vp.taxamount from vatonpurchase vp "
//                    + "left join partyledger pl on pl.code=vp.partyledger "
//                    + "left join commoditymaster cm on cm.id=vp.commodity "
//                    + "where vp.cancelled is false and vp.month=" + MONTH + " and "
//                    + "vp.year=" + YEAR + " order by pl.partyname,cm.code";

            SQLQuery query = session.createSQLQuery(QUERY);

//            System.out.println("COMMODITYQUERY ->" + COMMODITYQUERY);
            if (query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            List<AccountsModel> realizationlist = new ArrayList<AccountsModel>();
            Map PaymentRealizationmap = new HashMap();

            for (ListIterator its = query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                am = new AccountsModel();

                String chequeno = "";
                String amount = "";
                boolean paymentrealizationstatus = false;
                String realizationdate = "";
                String chequedate = "";

                if (rows[7] != null) {
                    chequeno = (String) rows[7];
                }

                if (rows[9] != null) {
                    BigDecimal bigamount = (BigDecimal) rows[9];
                    double douamount = bigamount.doubleValue();
                    amount = decimalFormat.format(douamount);
                }

                if (rows[17] != null) {
                    Date reldate = (Date) rows[17];
                    realizationdate = dateToString(reldate);
                }

                if (rows[20] != null) {
                    Date chedate = (Date) rows[20];
                    chequedate = dateToString(chedate);
                }

//                System.out.println(chequeno + " - " + amount + " - " + realizationdate + " - " + chequedate);

                am.setChequeno(chequeno);
                am.setAmount(amount);
                am.setAccdate(realizationdate);
                am.setChequedate(chequedate);
//                am.setRegion(regionname);
//                am.setFromdate(voucherdatefrom);
//                am.setTodate(voucherdateto);
                am.setPageno(String.valueOf(pageno));
                realizationlist.add(am);
                pageno++;
                // </editor-fold>
            }

            PaymentRealizationmap.put("regionname", regionname);
            PaymentRealizationmap.put("fromdate", voucherdatefrom);
            PaymentRealizationmap.put("todate", voucherdateto);
            PaymentRealizationmap.put("realizationlist", realizationlist);

            boolean result = prr.GeneratePurchaseVatExportXML(PaymentRealizationmap, filePathwithName);

            if (!result) {
                map.put("ERROR", "Report Generation Error!");
            }

        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map ReceiptRealizationPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String realizationstatus, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class PaymentRealizationPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        ReceiptRealizationReport prr = new ReceiptRealizationReport();
        LedgerReport lr = new LedgerReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String QUERY = null;
        try {

            String periodcode = (String) request.getSession(false).getAttribute("financialYear");

            boolean isrealized = Boolean.parseBoolean(realizationstatus.trim());

            StringBuilder sb = new StringBuilder();

            sb.append("select cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer) as voucherid, ");
            sb.append("v.voucherdate as voucherdate, ");
            sb.append("v.narration as narration, ");
            sb.append("ab.bookname as accountbook, ");
            sb.append("rpd.id as recppayid, ");
            sb.append("rpd.favourof as partyname, ");
            sb.append("rpd.otherbankname as otherbankname, ");
            sb.append("rpd.refno as refno, ");
            sb.append("pm.type as paymentmode, ");
            sb.append("rpd.amount as amount, ");
            sb.append("rpd.bankchallan as challanid, ");
            sb.append("bc.challandate as challandate, ");
            sb.append("bc.printdate as printdate, ");
            sb.append("bc.bank as bankcode, ");
            sb.append("bl.bankname as bankname, ");
            sb.append("bl.branchname as bankbranchname, ");
            sb.append("bl.accountno as bankaccountno, ");
            sb.append("rpd.realized as realized, ");
            sb.append("rpd.realizeddate as realizeddate, ");
            sb.append("cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer) as vounumord, ");
            sb.append("rpd.chequedate as chequedate ");
            sb.append("from receiptpaymentdetails rpd ");
            sb.append("left join voucher v on v.id=rpd.voucher ");
            sb.append("left join partyledger pl on pl.code=rpd.partyledger ");
            sb.append("left join bankchallan bc on bc.id=rpd.bankchallan ");
            sb.append("left join bankledger bl on bl.code=bc.bank ");
            sb.append("left join accountsbooks ab on ab.code=v.accountbook ");
            sb.append("left join paymentmode pm on pm.code=rpd.paymentmode ");
            sb.append("where rpd.region='" + LoggedInRegion + "' ");
            sb.append("and v.region='" + LoggedInRegion + "' ");
            sb.append("and v.accountingperiod='" + periodcode + "' ");
            sb.append("and rpd.cancelled is false ");
            sb.append("and v.cancelled is false ");
            if (isrealized) {
                sb.append("and rpd.realized is true ");
            } else {
                sb.append("and (rpd.realized is false or rpd.realized  is null) ");
            }
            sb.append("and rpd.paymentmode in ('1','2','3') ");
            sb.append("and v.vouchertype='R' ");
            sb.append("order by voucherdate, voucherid ");

//            sb.append("select v.id as voucherid,");
//            sb.append("v.voucherdate as voucherdate,");
//            sb.append("v.narration as narration,");
//            sb.append("ab.bookname as accountbook,");
//            sb.append("rpd.id as recppayid,");
//            sb.append("rpd.favourof as partyname,");
//            sb.append("rpd.otherbankname as otherbankname,");
//            sb.append("rpd.refno as refno,");
//            sb.append("pm.type as paymentmode,");
//            sb.append("rpd.amount as amount,");
//            sb.append("rpd.bankchallan as challanid,");
//            sb.append("bc.challandate as challandate,");
//            sb.append("bc.bank as bankcode,");
//            sb.append("bl.bankname as bankname,");
//            sb.append("bl.branchname as bankbranchname,");
//            sb.append("bl.accountno as bankaccountno,");
//            sb.append("rpd.realized as realized,");
//            sb.append("rpd.realizeddate as realizeddate,");
//            sb.append("rpd.chequedate as chequedate ");
//            sb.append("from receiptpaymentdetails rpd ");
//            sb.append("left join voucher v on v.id=rpd.voucher ");
//            sb.append("left join partyledger pl on pl.code=rpd.partyledger ");
//            sb.append("left join bankchallan bc on bc.id=rpd.bankchallan ");
//            sb.append("left join bankledger bl on bl.code=bc.bank ");
//            sb.append("left join accountsbooks ab on ab.code=v.accountbook ");
//            sb.append("left join paymentmode pm on pm.code=rpd.paymentmode ");
//            sb.append("where rpd.region='" + LoggedInRegion + "' ");
//            sb.append("and v.region='" + LoggedInRegion + "' ");
//            sb.append("and v.accountingperiod='" + periodcode + "' ");
//            sb.append("and rpd.cancelled is false ");
//            sb.append("and v.cancelled is false ");

            QUERY = sb.toString();

//            System.out.println("Query  -> " + QUERY);

            Criteria reCrit = session.createCriteria(Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";
            if (reList.size() > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }

            SQLQuery query = session.createSQLQuery(QUERY);

//            System.out.println("COMMODITYQUERY ->" + COMMODITYQUERY);
            if (query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            List<AccountsModel> realizationlist = new ArrayList<AccountsModel>();
            Map ReceiptRealizationmap = new HashMap();

            for (ListIterator its = query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                am = new AccountsModel();

                String chequeno = "";
                String amount = "";
                boolean paymentrealizationstatus = false;
                String realizationdate = "";
                String chequedate = "";
                String remittancedate = "";
                String paymentmode = "";
                String challanno = "";

                if (rows[7] != null) {
                    chequeno = (String) rows[7];
                }

                if (rows[8] != null) {
                    paymentmode = (String) rows[8];
                }

                if (rows[9] != null) {
                    BigDecimal bigamount = (BigDecimal) rows[9];
                    double douamount = bigamount.doubleValue();
                    amount = decimalFormat.format(douamount);
                }

                if (rows[10] != null) {
                    challanno = (String) rows[10];
                }

                if (rows[12] != null) {
                    Date remitdate = (Date) rows[12];
                    remittancedate = dateToString(remitdate);
                }

                if (rows[18] != null) {
                    Date reldate = (Date) rows[18];
                    realizationdate = dateToString(reldate);
                }

                if (rows[20] != null) {
                    Date chedate = (Date) rows[20];
                    chequedate = dateToString(chedate);
                }

//                System.out.println(chequeno + " - " + amount + " - " + realizationdate + " - " + chequedate);

                am.setChequeno(chequeno);
                am.setPaymentmode(paymentmode);
                am.setAmount(amount);
                am.setChallanno(challanno);
                am.setRemittancedate(remittancedate);
                am.setAccdate(realizationdate);
                am.setChequedate(chequedate);
//                am.setRegion(regionname);
//                am.setFromdate(voucherdatefrom);
//                am.setTodate(voucherdateto);
                am.setPageno(String.valueOf(pageno));
                realizationlist.add(am);
                pageno++;
                // </editor-fold>
            }

            ReceiptRealizationmap.put("regionname", regionname);
//            PaymentRealizationmap.put("fromdate", voucherdatefrom);
//            PaymentRealizationmap.put("todate", voucherdateto);
            ReceiptRealizationmap.put("realizationlist", realizationlist);

            boolean result = prr.GeneratePurchaseVatExportXML(ReceiptRealizationmap, filePathwithName);

            if (!result) {
                map.put("ERROR", "Report Generation Error!");
            }

        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map ChequeRegisterPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String periodsele, String voucherdatefrom, String voucherdateto, String book, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class ChequeRegisterPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        ChequeRegisterReport crr = new ChequeRegisterReport();
        LedgerReport lr = new LedgerReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String BANKQUERY = null;
        String REGISTERQUERY = null;
        Map chequeregistermap = new HashMap();
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("select voucherid,voucherdate,narration,accountbook, recppayid,partyname, otherbankname, refno,paymentmode,amount,challanid,challandate,bankcode,bankname, ");
            sb.append("bankbranchname,bankaccountno,realized,realizeddate,vnx, cast(REPLACE(TRANSLATE(xx, REPLACE(TRANSLATE(xx,'0123456789', RPAD('#',LENGTH(xx),'#')),'#',''),  RPAD('#',LENGTH(xx),'#')),'#','') as INTEGER) as xxx,chequedate,paymenttype  from ");
            sb.append("(select v.id as voucherid, case when v.voucherapproveddate is null then v.voucherdate else v.voucherapproveddate end as voucherdate, v.narration as narration,ab.bookname as accountbook, rpd.id as recppayid, rpd.favourof as partyname ,rpd.otherbankname as otherbankname, ");
            sb.append("rpd.refno as refno, pm.type as paymentmode, rpd.amount as amount, rpd.bankchallan as challanid, bc.challandate as challandate, bl.code as bankcode, bl.bankname as bankname, ");
            sb.append("bl.branchname as bankbranchname, bl.accountno as bankaccountno, rpd.realized as realized, rpd.realizeddate as realizeddate, v.voucherno as vnx, case when position('-' in v.voucherno)>0 then trim(split_part(v.voucherno,'-',1)) else case when position('/' in v.voucherno)>=0 then trim(split_part(v.voucherno,'/',1)) else v.voucherno end  end as xx,rpd.chequedate as chequedate ");
            sb.append(",pm.type as paymenttype ");
            sb.append("from receiptpaymentdetails rpd  ");
            sb.append("left join voucher v on v.id=rpd.voucher ");
            sb.append("left join partyledger pl on pl.code=rpd.partyledger ");
            sb.append("left join bankchallan bc on bc.id=rpd.bankchallan ");
            sb.append("left join bankledger bl on bl.code=rpd.bankname ");
            sb.append("left join paymentmode pm on pm.code=rpd.paymentmode ");
            sb.append("left join accountsbooks ab on ab.code=v.accountbook ");
            sb.append("where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
            sb.append("else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end ");
            sb.append("and  v.accountbook='" + book + "' and  rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' ");
            sb.append("and v.accountingperiod='" + periodsele + "' and rpd.cancelled is false and v.cancelled is false ");
//            sb.append("and rpd.paymentmode in ('1','2','3') ");
            sb.append("and v.vouchertype='P') as x ");
            sb.append("order by cast(xx as integer),refno");

//            sb.append(" select voucherid,voucherdate,narration,accountbook, recppayid,partyname, otherbankname, refno,paymentmode,amount,challanid,challandate,bankcode,bankname, ");
//            sb.append(" bankbranchname,bankaccountno,realized,realizeddate,vnx, cast(REPLACE(TRANSLATE(xx, REPLACE(TRANSLATE(xx,'0123456789', RPAD('#',LENGTH(xx),'#')),'#',''),  RPAD('#',LENGTH(xx),'#')),'#','') as INTEGER) as xxx,chequedate  from");
//            sb.append(" (select v.id as voucherid, case when v.voucherapproveddate is null then v.voucherdate else v.voucherapproveddate end as voucherdate, v.narration as narration,ab.bookname as accountbook, rpd.id as recppayid, rpd.favourof as partyname ,rpd.otherbankname as otherbankname, ");
//            sb.append(" rpd.refno as refno, pm.type as paymentmode, rpd.amount as amount, rpd.bankchallan as challanid, bc.challandate as challandate, bl.code as bankcode, bl.bankname as bankname, ");
//            sb.append(" bl.branchname as bankbranchname, bl.accountno as bankaccountno, rpd.realized as realized, rpd.realizeddate as realizeddate, v.voucherno as vnx, case when position('-' in v.voucherno)>0 then trim(split_part(v.voucherno,'-',1)) else case when position('/' in v.voucherno)>=0 then trim(split_part(v.voucherno,'/',1)) else v.voucherno end  end as xx,rpd.chequedate as chequedate ");
//            sb.append(" from receiptpaymentdetails rpd  ");
//            sb.append(" left join voucher v on v.id=rpd.voucher ");
//            sb.append(" left join partyledger pl on pl.code=rpd.partyledger ");
//            sb.append(" left join bankchallan bc on bc.id=rpd.bankchallan ");
//            sb.append(" left join bankledger bl on bl.code=rpd.bankname ");
//            sb.append(" left join paymentmode pm on pm.code=rpd.paymentmode ");
//            sb.append(" left join accountsbooks ab on ab.code=v.accountbook ");
//            sb.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
//            sb.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
//            sb.append(" and  v.accountbook='" + book + "' and  rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + periodsele + "' and rpd.cancelled is false and v.cancelled is false and rpd.paymentmode in ('1','2','3') and v.vouchertype='P') as x ");
//            sb.append(" order by cast(xx as integer),refno  ");

            REGISTERQUERY = sb.toString();

            System.out.println("REGISTERQUERY = " + REGISTERQUERY);

            BANKQUERY = "select code,bankname from bankledger where region = '" + LoggedInRegion + "' order by code";

            Criteria reCrit = session.createCriteria(Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";
            if (reList.size() > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }

            SQLQuery register_query = session.createSQLQuery(REGISTERQUERY);
            SQLQuery bank_query = session.createSQLQuery(BANKQUERY);

//            System.out.println("COMMODITYQUERY ->" + COMMODITYQUERY);
            if (register_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            Map headermap = new LinkedHashMap();

            for (ListIterator it = bank_query.list().listIterator(); it.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] row = (Object[]) it.next();
                headermap.put((String) row[0], (String) row[1]);
                // </editor-fold>
            }

            int slipno = 1;

            List<AccountsModel> chequelist = new ArrayList<AccountsModel>();

            for (ListIterator its = register_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();

                String voucherid = "";
                String voucherdate = "";
                String narration = "";
                String accountbook = "";
                String recppayid = "";
                String partyname = "";
                String otherbankname = "";
                String refno = "";
                String paymentmode = "";
                String amount = "";
                String challanid = "";
                String challandate = "";
                String bankcode = "";
                String bankname = "";
                String bankbranchname = "";
                String bankaccountno = "";
                String realized = "";
                String realizeddate = "";
                String vno = "";
                String chequedate = "";

                if (rows[0] != null) {
                    voucherid = rows[0].toString();
                } else {
                    voucherid = "";
                }

                if (rows[1] != null) {
                    voucherdate = rows[1].toString();
                } else {
                    voucherdate = "";
                }
                if (rows[2] != null) {
                    narration = rows[2].toString();
                } else {
                    narration = "";
                }
                if (rows[3] != null) {
                    accountbook = rows[3].toString();
                } else {
                    accountbook = "";
                }

                if (rows[4] != null) {
                    recppayid = rows[4].toString();
                } else {
                    recppayid = "";
                }
                if (rows[5] != null) {
                    partyname = rows[5].toString();
                } else {
                    partyname = "";
                }

                if (rows[6] != null) {
                    otherbankname = rows[6].toString();
                } else {
                    otherbankname = "";
                }
                if (rows[7] != null) {
                    refno = rows[7].toString();
                } else {
                    refno = "";
                }
                if (rows[8] != null) {
                    paymentmode = rows[8].toString();
                } else {
                    paymentmode = "";
                }
                if (rows[9] != null) {
                    BigDecimal bd = (BigDecimal) rows[9];
                    amount = decimalFormat.format(bd.doubleValue());
                }
                if (rows[10] != null) {
                    challanid = rows[10].toString();
                } else {
                    challanid = "";
                }
                if (rows[11] != null) {
                    challandate = rows[11].toString();
                }
                if (rows[12] != null) {
                    bankcode = rows[12].toString();
                } else {
                    bankcode = "";
                }

                if (rows[13] != null) {
                    bankname = rows[13].toString();
                } else {
                    bankname = "";
                }
                if (rows[14] != null) {
                    bankbranchname = rows[14].toString();
                } else {
                    bankbranchname = "";
                }
                if (rows[15] != null) {
                    bankaccountno = rows[15].toString();
                } else {
                    bankaccountno = "";
                }
                if (rows[16] != null) {
                    realized = rows[16].toString();
                } else {
                    realized = "false";
                }
                if (rows[17] != null) {
                    realizeddate = rows[17].toString();
                } else {
                    realizeddate = "";
                }

                if (rows[18] != null) {
                    vno = rows[18].toString();
                } else {
                    vno = "";
                }


                if (rows[20] != null) {
                    Date chdate = (Date) rows[20];
                    chequedate = dateToString(chdate);
                } else {
                    chequedate = "";
                }

                am = new AccountsModel();
                am.setSlipno(String.valueOf(slipno));
                am.setChequeno(refno);
                am.setChequedate(chequedate);
                am.setCompno(voucherid);
                am.setVoucherno(vno);
                am.setVoucherapproveddate(voucherdate);
                am.setPartyname(partyname);

                List<String> banklist = new ArrayList<String>();

                Iterator itr = headermap.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry bm = (Entry) itr.next();
                    String bn = (String) bm.getKey();
//                    System.out.println("bankcode "+bankcode+ " | bn  "+bn);
                    if (bankcode.equalsIgnoreCase(bn)) {
                        banklist.add(amount);
                    } else {
                        banklist.add("0.00");
                    }

                }
                am.setBanklist(banklist);
                chequelist.add(am);
                slipno++;
                // </editor-fold>
            }
//            Iterator chit = chequelist.iterator();
//            while(chit.hasNext()){
//                AccountsModel am1 = (AccountsModel) chit.next();
//                StringBuilder st = new StringBuilder();
//                st.append(am1.getSlipno());
//                st.append(" - ");
//                st.append(am1.getChequeno());
//                st.append(" - ");
//                st.append(am1.getChequedate());
//                st.append(" - ");
//                st.append(am1.getCompno());
//                st.append(" - ");
//                st.append(am1.getVoucherno());
//                st.append(" - ");
//                st.append(am1.getVoucherapproveddate());
//                st.append(" - ");
//                st.append(am1.getPartyname());
//                st.append(" - ");
//               
//                Iterator bit = (am1.getBanklist()).iterator();
//                while(bit.hasNext()){
//                    double bankname = (Double) bit.next();
//                    st.append(bankname);
//                    st.append(" - ");
//                }
//                System.out.println(st.toString());
//            }

            chequeregistermap.put("fromdate", voucherdatefrom);
            chequeregistermap.put("todate", voucherdateto);
            chequeregistermap.put("regionname", regionname);
            chequeregistermap.put("chequelist", chequelist);
            chequeregistermap.put("headermap", headermap);
//            chequeregistermap.put("region", regionname);
            crr.GenerateChequeRegisterExportXML(chequeregistermap, filePathwithName);


        } catch (Exception ex) {
            map.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return map;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map ReceiptRealizationReportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String filePathwithName) {
        System.out.println("***************************** AccountReportServiceImpl class ReceiptRealizationReportPrintOut method is calling ********************************");
        Map map = new HashMap();
        AccountsModel am = null;
        ReceiptRealizationReport1 rrr = new ReceiptRealizationReport1();
        LedgerReport lr = new LedgerReport();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;

        try {

            StringBuilder sbreal = new StringBuilder();
            StringBuilder sbunrealremitted = new StringBuilder();
            StringBuilder sbunrealunremitted = new StringBuilder();
            StringBuilder sbunrealb = new StringBuilder();

            sbreal.append("select * from ((select v.id as voucherid,rpd.id, v.voucherdate as voucherdate, rpd.refno, rpd.amount, rpd.realized, rpd.realizeddate, bc.printdate from receiptpaymentdetails  rpd ");
            sbreal.append("left join voucher v on v.id=rpd.voucher ");
            sbreal.append("left join bankchallan bc on bc.id=rpd.bankchallan ");
            sbreal.append("where ");
            sbreal.append("v.region='" + LoggedInRegion + "' ");
            sbreal.append("and rpd.region='" + LoggedInRegion + "' ");
            sbreal.append("and v.voucherdate between '" + postgresDate(startingdate) + "' ");
            sbreal.append("and '" + postgresDate(enddate) + "' ");
            sbreal.append("and rpd.realizeddate between '" + postgresDate(startingdate) + "' and '" + postgresDate(enddate) + "' ");
            sbreal.append("and v.cancelled is false ");
            sbreal.append("and v.vouchertype='R' ");
            sbreal.append("and v.cancelled is false ");
            sbreal.append("and rpd.cancelled is false ");
            sbreal.append("and rpd.realized is true) ");
            sbreal.append("union ");
            sbreal.append("(select v.id  as voucherid, rpd.id, v.voucherdate as voucherdate, rpd.refno, rpd.amount, rpd.realized, rpd.realizeddate, bc.printdate from receiptpaymentdetails  rpd ");
            sbreal.append("left join voucher v on v.id=rpd.voucher ");
            sbreal.append("left join bankchallan bc on bc.id=rpd.bankchallan ");
            sbreal.append("where v.region='" + LoggedInRegion + "' ");
            sbreal.append("and rpd.region='" + LoggedInRegion + "' ");
            sbreal.append("and v.voucherdate <'" + postgresDate(startingdate) + "'  ");
            sbreal.append("and v.cancelled is false ");
            sbreal.append("and v.vouchertype='R' ");
            sbreal.append("and rpd.cancelled is false ");
            sbreal.append("and (rpd.realizeddate between '" + postgresDate(startingdate) + "' and '" + postgresDate(enddate) + "')) ");
            sbreal.append("order by voucherdate) as x order by cast(replace(replace(trim(split_part(voucherid,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer) ");

            sbunrealremitted.append("select v.id,rpd.id, v.voucherdate, rpd.refno, rpd.amount, rpd.realized, rpd.realizeddate, bc.printdate from receiptpaymentdetails  rpd ");
            sbunrealremitted.append("left join voucher v on v.id=rpd.voucher ");
            sbunrealremitted.append("left join bankchallan bc on bc.id=rpd.bankchallan ");
            sbunrealremitted.append("where ");
            sbunrealremitted.append("v.region='" + LoggedInRegion + "' ");
            sbunrealremitted.append("and rpd.region='" + LoggedInRegion + "' ");
            sbunrealremitted.append("and v.voucherdate between '" + postgresDate(startingdate) + "' ");
            sbunrealremitted.append("and '" + postgresDate(enddate) + "' ");
            sbunrealremitted.append("and v.cancelled is false ");
            sbunrealremitted.append("and v.vouchertype='R' ");
            sbunrealremitted.append("and v.cancelled is false ");
            sbunrealremitted.append("and rpd.cancelled is false ");
//            sbunrealremitted.append("and (rpd.realized is false or rpd.realized  is null) ");
            sbunrealremitted.append("and (rpd.realized is false or rpd.realized  is null or rpd.realizeddate>'" + postgresDate(enddate) + "') ");
            sbunrealremitted.append("and bc.printdate is not null ");
            sbunrealremitted.append("and bc.printdate between '" + postgresDate(startingdate) + "' and '" + postgresDate(enddate) + "' ");
            sbunrealremitted.append("order by v.voucherdate, cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer) ");

            sbunrealunremitted.append("select v.id,rpd.id, v.voucherdate, rpd.refno, rpd.amount, rpd.realized, rpd.realizeddate, bc.printdate from receiptpaymentdetails  rpd ");
            sbunrealunremitted.append("left join voucher v on v.id=rpd.voucher ");
            sbunrealunremitted.append("left join bankchallan bc on bc.id=rpd.bankchallan ");
            sbunrealunremitted.append("where ");
            sbunrealunremitted.append("v.region='" + LoggedInRegion + "' ");
            sbunrealunremitted.append("and rpd.region='" + LoggedInRegion + "' ");
            sbunrealunremitted.append("and v.voucherdate between '" + postgresDate(startingdate) + "' ");
            sbunrealunremitted.append("and '" + postgresDate(enddate) + "' ");
            sbunrealunremitted.append("and v.cancelled is false ");
            sbunrealunremitted.append("and v.vouchertype='R' ");
            sbunrealunremitted.append("and v.cancelled is false ");
            sbunrealunremitted.append("and rpd.cancelled is false ");
//            sbunrealunremitted.append("and (rpd.realized is false or rpd.realized  is null) ");
            sbunrealunremitted.append("and (rpd.realized is false or rpd.realized  is null or rpd.realizeddate>'" + postgresDate(enddate) + "') ");
            sbunrealunremitted.append("and (bc.printdate is null or bc.printdate > '" + postgresDate(enddate) + "') ");
            sbunrealunremitted.append("order by v.voucherdate, cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer) ");

            sbunrealb.append("select v.id,rpd.id, v.voucherdate, rpd.refno, rpd.amount, rpd.realized, rpd.realizeddate, bc.printdate from receiptpaymentdetails  rpd ");
            sbunrealb.append("left join voucher v on v.id=rpd.voucher ");
            sbunrealb.append("left join bankchallan bc on bc.id=rpd.bankchallan ");
            sbunrealb.append("where v.region='" + LoggedInRegion + "' ");
            sbunrealb.append("and rpd.region='" + LoggedInRegion + "' ");
            sbunrealb.append("and v.voucherdate <'" + postgresDate(startingdate) + "'  ");
            sbunrealb.append("and v.cancelled is false ");
            sbunrealb.append("and v.vouchertype='R' ");
            sbunrealb.append("and rpd.cancelled is false ");
            sbunrealb.append("and rpd.realizeddate is null ");

            //System.out.println("Realized query = " + sbreal.toString());
            //System.out.println("Unrealized Remited query = " + sbunrealremitted.toString());
            //System.out.println("unrealized unremitted query = " + sbunrealunremitted.toString());
            //System.out.println("Unrealized Bending query = " + sbunrealb.toString());


            SQLQuery realizedquery = session.createSQLQuery(sbreal.toString());
            SQLQuery unrealizedremittancequery = session.createSQLQuery(sbunrealremitted.toString());
            SQLQuery unrealizedunremittancequery = session.createSQLQuery(sbunrealunremitted.toString());
            SQLQuery unrealizedPendingquery = session.createSQLQuery(sbunrealb.toString());

            List<AccountsModel> realizedlist = new ArrayList<AccountsModel>();
            List<AccountsModel> unrealizedremittedlist = new ArrayList<AccountsModel>();
            List<AccountsModel> unrealizedunremittancelist = new ArrayList<AccountsModel>();
            List<AccountsModel> unrealizedPendinglist = new ArrayList<AccountsModel>();
            Map ReceiptRealizationmap = new HashMap();

            for (ListIterator its = realizedquery.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Realized List">
                Object[] rows = (Object[]) its.next();

                String voucherno = (String) rows[0];
                String receiptandpaymentno = (String) rows[1];
                Date vdate = (Date) rows[2];
                String voucherdate = dateToString(vdate);
                String chequeno = (String) rows[3];
                BigDecimal bigamount = (BigDecimal) rows[4];
                double amount = bigamount.doubleValue();
                Date rdate = (Date) rows[6];
                String realizeddate = dateToString(rdate);
                Date pdate = (Date) rows[7];
                String remittancedate = dateToString(pdate);

                am = new AccountsModel();
                am.setVoucherapproveddate(voucherdate);
                am.setRemittancedate(remittancedate);
                am.setRealizationdate(realizeddate);
                am.setChequeno(chequeno);
                am.setAmount(decimalFormat.format(amount));
                am.setPageno(String.valueOf(pageno));
                realizedlist.add(am);
                pageno++;
                // </editor-fold>
            }

            pageno = 1;

            for (ListIterator its = unrealizedremittancequery.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="UnRealized Remittanced List">
                Object[] rows = (Object[]) its.next();

                String voucherno = (String) rows[0];
                String receiptandpaymentno = (String) rows[1];
                String voucherdate = "";
                if (rows[2] != null) {
                    Date vdate = (Date) rows[2];
                    voucherdate = dateToString(vdate);
                }
                String chequeno = (String) rows[3];
                BigDecimal bigamount = (BigDecimal) rows[4];
                double amount = bigamount.doubleValue();
                String realizeddate = "";
                if (rows[6] != null) {
                    Date rdate = (Date) rows[6];
                    realizeddate = dateToString(rdate);
                }
                String remittancedate = "";
                if (rows[7] != null) {
                    Date pdate = (Date) rows[7];
                    remittancedate = dateToString(pdate);
                }

                am = new AccountsModel();
                am.setVoucherapproveddate(voucherdate);
                am.setRemittancedate(remittancedate);
                am.setRealizationdate(realizeddate);
                am.setChequeno(chequeno);
                am.setAmount(decimalFormat.format(amount));
                am.setPageno(String.valueOf(pageno));
                unrealizedremittedlist.add(am);
                pageno++;
                // </editor-fold>
            }

            pageno = 1;

            for (ListIterator its = unrealizedunremittancequery.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="UnRealized UnRemittanced List">
                Object[] rows = (Object[]) its.next();

                String voucherno = (String) rows[0];
                String receiptandpaymentno = (String) rows[1];
                String voucherdate = "";
                if (rows[2] != null) {
                    Date vdate = (Date) rows[2];
                    voucherdate = dateToString(vdate);
                }
                String chequeno = (String) rows[3];
                BigDecimal bigamount = (BigDecimal) rows[4];
                double amount = bigamount.doubleValue();
                String realizeddate = "";
                if (rows[6] != null) {
                    Date rdate = (Date) rows[6];
                    realizeddate = dateToString(rdate);
                }
                String remittancedate = "";
                if (rows[7] != null) {
                    Date pdate = (Date) rows[7];
                    remittancedate = dateToString(pdate);
                }

                am = new AccountsModel();
                am.setVoucherapproveddate(voucherdate);
                am.setRemittancedate(remittancedate);
                am.setRealizationdate(realizeddate);
                am.setChequeno(chequeno);
                am.setAmount(decimalFormat.format(amount));
                am.setPageno(String.valueOf(pageno));
                unrealizedunremittancelist.add(am);
                pageno++;
                // </editor-fold>
            }

            pageno = 1;

            for (ListIterator its = unrealizedPendingquery.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="UnRealized Pending List">
                Object[] rows = (Object[]) its.next();

                String voucherno = (String) rows[0];
                String receiptandpaymentno = (String) rows[1];
                Date vdate = (Date) rows[2];
                String voucherdate = dateToString(vdate);
                String chequeno = (String) rows[3];
                BigDecimal bigamount = (BigDecimal) rows[4];
                double amount = bigamount.doubleValue();
                Date rdate = (Date) rows[6];
                String realizeddate = dateToString(rdate);
                Date pdate = (Date) rows[7];
                String remittancedate = dateToString(pdate);

                am = new AccountsModel();
                am.setVoucherapproveddate(voucherdate);
                am.setRemittancedate(remittancedate);
                am.setRealizationdate(realizeddate);
                am.setChequeno(chequeno);
                am.setAmount(decimalFormat.format(amount));
                am.setPageno(String.valueOf(pageno));
                unrealizedPendinglist.add(am);
                pageno++;
                // </editor-fold>
            }

            Criteria reCrit = session.createCriteria(Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";
            if (reList.size() > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }

            ReceiptRealizationmap.put("fromdate", startingdate);
            ReceiptRealizationmap.put("todate", enddate);
            ReceiptRealizationmap.put("realizedlist", realizedlist);
            ReceiptRealizationmap.put("unrealizedremittedlist", unrealizedremittedlist);
            ReceiptRealizationmap.put("unrealizedunremittancelist", unrealizedunremittancelist);
            ReceiptRealizationmap.put("unrealizedpendinglist", unrealizedPendinglist);

            boolean result = rrr.GeneratePurchaseVatExportXML(ReceiptRealizationmap, filePathwithName);

            if (!result) {
                map.put("ERROR", "Report Generation Error!");
            }

        } catch (Exception ex) {
            map.put("ERROR", "Receipt Realization Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map PaymentRealizationAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String periodsele, String voucherdatefrom, String voucherdateto, String book, String realizationstatus) {
        System.out.println("***************************** AccountReportServiceImpl class PaymentRealizationAbstractPrintOut method is calling ********************************");
        Map PaymentRealizationmap = new HashMap();
//        AccountsModel am = null;
        Map paymentmap = null;
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int pageno = 1;
        String QUERY = null;
        String fromdate = (voucherdatefrom == null) ? "" : voucherdatefrom;
        String todate = (voucherdateto == null) ? "" : voucherdateto;
        try {

            boolean isrealized = Boolean.parseBoolean(realizationstatus.trim());

            List<Map> realizationlist = new ArrayList<Map>();

            StringBuilder sb = new StringBuilder();

            sb.append("(select rpd.refno as chequeno, rpd.chequedate as chequedate, rpd.amount as amount, rpd.realizeddate as realizeddate from receiptpaymentdetails  rpd ");
            sb.append("left join voucher v on v.id=rpd.voucher ");
            sb.append("where ");
            sb.append("rpd.cancelled is false ");
            sb.append("and v.cancelled is false ");
            sb.append("and rpd.region='" + LoggedInRegion + "' ");
            sb.append("and v.region='" + LoggedInRegion + "' ");
            sb.append("and v.accountbook='" + book + "' ");
            sb.append("and v.accountingperiod='" + periodsele + "' ");
            sb.append("and v.vouchertype='P' ");
            if (isrealized) {
                sb.append("and rpd.realized is true ");
                if (fromdate.length() > 0 && todate.length() > 0) {
                    sb.append("and rpd.realizeddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' ) ");
                }
            } else {
                sb.append("and (rpd.realized is null or rpd.realized is false) and rpd.chequedate <= '" + postgresDate(todate) + "') ");
//                sb.append("and (rpd.realized is null or rpd.realized is false)) ");
            }
            sb.append("union ");
            sb.append("(SELECT refno as chequeno, chequedate as chequedate, amount as amount, realizeddate as realizeddate FROM receiptpaymentdetailspending ");
            sb.append("where ");
            sb.append("region='" + LoggedInRegion + "' ");
            sb.append("and accountingperiod='3' ");
            sb.append("and accountbook='" + book + "' ");
            sb.append("and cancelled is false ");
            if (isrealized) {
                sb.append("and realized is true ");
                if (fromdate.length() > 0 && todate.length() > 0) {
                    sb.append("and realizeddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' ) ");
                }
            } else {
                sb.append("and (realized is null or realized is false) and chequedate <= '" + postgresDate(todate) + "') ");
//                sb.append("and (realized is null or realized is false)) ");
            }
            sb.append("order by realizeddate,chequedate, chequeno");

            //System.out.println("query = " + sb.toString());

//            sb.append("select rpd.refno, rpd.chequedate, rpd.amount, rpd.realizeddate from receiptpaymentdetails  rpd ");
//            sb.append("left join voucher v on v.id=rpd.voucher ");
//            sb.append("where ");
//            sb.append("rpd.cancelled is false ");
//            sb.append("and v.cancelled is false ");
//            sb.append("and rpd.region='" + LoggedInRegion + "' ");
//            sb.append("and v.region='" + LoggedInRegion + "' ");
//            sb.append("and v.accountbook='" + book + "' ");
//            sb.append("and v.accountingperiod='" + periodsele + "' ");
//            sb.append("and v.vouchertype='P' ");
//            if (isrealized) {
//                sb.append("and rpd.realized is true ");
//                if (fromdate.length() > 0 && todate.length() > 0) {
//                    sb.append("and rpd.realizeddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' ");
//                }
//            } else {
//                sb.append("and (rpd.realized is null or rpd.realized is false) ");
//            }
//            sb.append("order by rpd.chequedate, rpd.refno");


            QUERY = sb.toString();

            //System.out.println("Query  -> " + QUERY);

            Criteria reCrit = session.createCriteria(Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";
            if (reList.size() > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }

            SQLQuery query = session.createSQLQuery(QUERY);
//            if (query.list().size() == 0) {
//                PaymentRealizationmap.put("ERROR", "There is no Record for the given Inputs");
//                return PaymentRealizationmap;
//            }            

            for (ListIterator its = query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                paymentmap = new HashMap();
//                am = new AccountsModel();

                String chequeno = "";
                String amount = "";
                boolean paymentrealizationstatus = false;
                String realizationdate = "";
                String chequedate = "";

                if (rows[0] != null) {
                    chequeno = (String) rows[0];
                }

                if (rows[1] != null) {
                    Date chedate = (Date) rows[1];
                    chequedate = dateToString(chedate);
                }

                if (rows[2] != null) {
                    BigDecimal bigamount = (BigDecimal) rows[2];
                    double douamount = bigamount.doubleValue();
                    amount = decimalFormat.format(douamount);
                }

                if (rows[3] != null) {
                    Date reldate = (Date) rows[3];
                    realizationdate = dateToString(reldate);
                }
//                am.setChequeno(chequeno);
//                am.setAmount(amount);
//                am.setAccdate(realizationdate);
//                am.setChequedate(chequedate);
//                am.setPageno(String.valueOf(pageno));
                paymentmap.put("pageno", String.valueOf(pageno));
                paymentmap.put("chequedate", chequedate);
                paymentmap.put("chequeno", chequeno);
                paymentmap.put("amount", amount);
                paymentmap.put("accdate", realizationdate);
                realizationlist.add(paymentmap);
                pageno++;
                // </editor-fold>
            }

            PaymentRealizationmap.put("regionname", regionname);
            PaymentRealizationmap.put("fromdate", fromdate);
            PaymentRealizationmap.put("todate", todate);
            PaymentRealizationmap.put("realizationlist", realizationlist);


        } catch (Exception ex) {
            PaymentRealizationmap.put("ERROR", "Purchase Report Generated Error");
            ex.printStackTrace();
        }
        return PaymentRealizationmap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRealizationAbstractPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode, String ctype, String accbook) {
        //        System.out.println("***************************** AccountReportServiceImpl class receiptHeadWiseAbstractPrint method is calling ********************************");
        Map map = new HashMap();
        Map cardDetails = null;
        LinkedList cashbookAbstractlist = new LinkedList();
        try {
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            String startYear = "01/04/" + accountingyearObj.getStartyear();
            String endYear = "31/03/" + accountingyearObj.getEndyear();

            if (!fromdate.trim().equalsIgnoreCase("null") && !todate.trim().equalsIgnoreCase("null") && todate.trim().length() > 0) {
                if (DateUtility.DateGreaterThanOrEqual(fromdate, startYear) && DateUtility.DateLessThanOrEqual(todate, endYear)) {
                    StringBuffer query = new StringBuffer();

                    query.append("select realizeddate, sum(amount) as amount from receiptpaymentdetails rpd ");
                    query.append("left join voucher v on v.id=rpd.voucher ");
                    query.append("where ");
                    query.append("rpd.realizeddate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "' ");
                    if (ctype.equalsIgnoreCase("P")) {
                        query.append("and v.vouchertype='P' ");
                        query.append("and  v.accountbook='" + accbook + "' ");
                    } else if (ctype.equalsIgnoreCase("R")) {
                        query.append("and v.vouchertype='R' ");
                        query.append("and  v.accountbook='" + accbook + "' ");
                    } else if (ctype.equalsIgnoreCase("J")) {
                        query.append("and v.vouchertype='J' ");
                        query.append("and  v.accountbook='5' ");
                    } else if (ctype.equalsIgnoreCase("B")) {
                        query.append("and v.vouchertype='P' ");
                        query.append("and  v.accountbook='4' ");
                    }
                    query.append("and rpd.cancelled is false ");
                    query.append("and v.region='" + LoggedInRegion + "' ");
                    query.append("group by rpd.realizeddate ");
                    query.append("order by rpd.realizeddate ");


                    SQLQuery receiptQuery = session.createSQLQuery(query.toString());
                    List receiptList = receiptQuery.list();
                    //System.out.println("query===" + query.toString());
                    if (receiptList.size() > 0) {
                        for (ListIterator its = receiptList.listIterator(); its.hasNext();) {
                            Object[] rows = (Object[]) its.next();

                            cardDetails = new HashMap();
                            cardDetails.put("serialdate", dateToString((java.sql.Date) rows[0]));
                            cardDetails.put("credit", (BigDecimal) rows[1]);
//                            cardDetails.put("debit", (BigDecimal) rows[2]);
//                            cardDetails.put("credit", (BigDecimal) rows[3]);

                            cashbookAbstractlist.add(cardDetails);
                        }
                        map.put("cashbookAbstractlist", cashbookAbstractlist);
                        map.put("regionname", getRegionmaster(session, LoggedInRegion).getRegionname());
                        map.put("ERROR", "");

                    } else {
                        map.put("ERROR", "No Record Found");
                    }
                } else {
                    map.put("ERROR", "Given From date and to date  is not in selected accouting period");
                }

            }

        } catch (Exception ex) {
            map.put("ERROR", "receiptHeadWiseAbstractPrint Report Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    private String getVoucherDebitCreditDetails(Session session, String voucherid) {
        StringBuilder html = new StringBuilder();
        SQLQuery Query = session.createSQLQuery("select sum(debit) as debit ,sum(credit) as credit from voucherdetails where voucher='" + voucherid + "'");
        List qList = Query.list();
        //System.out.println("query===" + query.toString());
        if (qList.size() > 0) {
            for (ListIterator its = qList.listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                if (rows[0] != null) {
                    BigDecimal debit = (BigDecimal) rows[0];
                    html.append("<td align=\"right\" >" + debit + "</td>");
                } else {
                    html.append("<td align=\"right\" >0.00</td>");
                }
                if (rows[1] != null) {
                    BigDecimal credit = (BigDecimal) rows[1];
                    html.append("<td align=\"right\" >" + credit + "</td>");
                } else {
                    html.append("<td align=\"right\" >0.00</td>");
                }

            }
        }
        return html.toString();
    }
}
