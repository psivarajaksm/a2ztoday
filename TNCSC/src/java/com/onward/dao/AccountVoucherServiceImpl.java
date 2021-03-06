/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.common.DateUtility;
import com.onward.persistence.payroll.*;
import com.onward.valueobjects.EDLIModel;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author root
 */
public class AccountVoucherServiceImpl extends OnwardAction implements AccountVoucherService {

    String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
//Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\r\n";

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getAccountsCodeListPartyDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        resultMap.put("accountcodelist", getAccountsCodeList(session));
        resultMap.put("partylist", getPartyList(session, LoggedInRegion));
        resultMap.put("banklist", getBankList(session, LoggedInRegion));
        resultMap.put("paymentmodelist", getPaymentModeList(session));
        resultMap.put("ancilarrylist", getAncillaryAccountsList(session));
        resultMap.put("booklist", loadAccountBookDetails(session, request, response, LoggedInRegion, LoggedInUser).get("booklist"));
        return resultMap;
    }

    public Map getPaymentModeList(Session session) {
        Map resultMap = new HashMap();
        Paymentmode paymentmodeObj;
        try {
            Criteria payModeCrit = session.createCriteria(Paymentmode.class);
            List payModeList = payModeCrit.list();
            if (payModeList.size() > 0) {
                for (int i = 0; i < payModeList.size(); i++) {
                    paymentmodeObj = (Paymentmode) payModeList.get(i);
                    resultMap.put(i, paymentmodeObj.getCode() + "  " + paymentmodeObj.getType());
                    resultMap.put(paymentmodeObj.getCode() + "  " + paymentmodeObj.getType(), paymentmodeObj.getCode());
                }
                resultMap.put("paymentmodelistlength", payModeList.size());
            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map getSactionedByList(Session session) {

        Map designationMap = new LinkedHashMap();
        designationMap.put("0", "--Select--");
        try {
            Criteria designatinCrit = session.createCriteria(Designationmaster.class);
            designatinCrit.add(Restrictions.sqlRestriction("designationcode='SE14'"));
            designatinCrit.addOrder(Order.asc("designation"));
            List<Designationmaster> designationList = designatinCrit.list();
            for (Designationmaster lbobj : designationList) {
                designationMap.put(lbobj.getDesignationcode(), lbobj.getDesignation());
            }
//            resultMap.put("designationlistlength", designationMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return designationMap;
    }

    public Map getBankList(Session session, String LoggedInRegion) {
        Map resultMap = new HashMap();
        Bankledger bankledgerObj;
        try {
            Criteria bankCrit = session.createCriteria(Bankledger.class);
            bankCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            List bankList = bankCrit.list();
            if (bankList.size() > 0) {
                for (int i = 0; i < bankList.size(); i++) {
                    bankledgerObj = (Bankledger) bankList.get(i);
                    resultMap.put(i, bankledgerObj.getCode() + "  " + bankledgerObj.getBankname());
                    resultMap.put(bankledgerObj.getCode() + "  " + bankledgerObj.getBankname(), bankledgerObj.getCode());
                }
                resultMap.put("banklistlength", bankList.size());
            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map getAccountsCodeList(Session session) {
        Map resultMap = new HashMap();
        Accountsheads accountsheadsObj;
        try {
            Criteria accCrit = session.createCriteria(Accountsheads.class);
            List accList = accCrit.list();
            if (accList.size() > 0) {
                for (int i = 0; i < accList.size(); i++) {
                    accountsheadsObj = (Accountsheads) accList.get(i);
                    resultMap.put(i, accountsheadsObj.getAcccode() + "  " + accountsheadsObj.getAccname());
                    resultMap.put(accountsheadsObj.getAcccode() + "  " + accountsheadsObj.getAccname(), accountsheadsObj.getAcccode());
                }
                resultMap.put("accountcodelistlength", accList.size());
            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map getPartyList(Session session, String LoggedInRegion) {
        Map resultMap = new HashMap();
        Partyledger partyledgerObj;
        try {
            Criteria parCrit = session.createCriteria(Partyledger.class);
            parCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            List parList = parCrit.list();
            if (parList.size() > 0) {
                for (int i = 0; i < parList.size(); i++) {
                    partyledgerObj = (Partyledger) parList.get(i);
                    resultMap.put(i, partyledgerObj.getCode() + "  " + partyledgerObj.getPartyname());
                    resultMap.put(partyledgerObj.getCode() + "  " + partyledgerObj.getPartyname(), partyledgerObj.getCode());
                }
                resultMap.put("partylistlength", parList.size());
            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map getAncillaryAccountsList(Session session) {
        Map resultMap = new HashMap();
        Ancillaryaccountadjcode ancillaryaccountadjcodeObj;
        try {
            Criteria ancillaryCrit = session.createCriteria(Ancillaryaccountadjcode.class);
            List ancillaryList = ancillaryCrit.list();
            if (ancillaryList.size() > 0) {
                for (int i = 0; i < ancillaryList.size(); i++) {
                    ancillaryaccountadjcodeObj = (Ancillaryaccountadjcode) ancillaryList.get(i);
                    resultMap.put(i, ancillaryaccountadjcodeObj.getId() + "-" + ancillaryaccountadjcodeObj.getName());
//                    resultMap.put(i + ancillaryaccountadjcodeObj.getId() + " " + ancillaryaccountadjcodeObj.getName(), ancillaryaccountadjcodeObj.getId());
                    resultMap.put(ancillaryaccountadjcodeObj.getId() + "-" + ancillaryaccountadjcodeObj.getName(), ancillaryaccountadjcodeObj.getTax());
                }
                resultMap.put("ancillarylistlength", ancillaryList.size());
            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map loadAccountBookDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String booktype) {
        Map resultMap = new HashMap();
        Map bookMap = new LinkedHashMap();
        bookMap.put("0", "--Select--");
        String bookCode = "";
        String bookName = "";
        try {
            Criteria bookCrit = session.createCriteria(Accountsbooks.class);
            bookCrit.add(Restrictions.sqlRestriction("grouptype='" + booktype + "'"));
            bookCrit.addOrder(Order.asc("bookname"));
            List<Accountsbooks> bookList = bookCrit.list();
            resultMap = new TreeMap();
            for (Accountsbooks lbobj : bookList) {

                bookCode = lbobj.getCode();
                bookName = lbobj.getBookname();


                bookMap.put(bookCode, bookName);
            }

            resultMap.put("booklist", bookMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public Map loadAccountBookDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map bookMap = new LinkedHashMap();
        bookMap.put("0", "--Select--");
        String bookCode = "";
        String bookName = "";
        try {
            Criteria bookCrit = session.createCriteria(Accountsbooks.class);
            bookCrit.addOrder(Order.asc("bookname"));
            List<Accountsbooks> bookList = bookCrit.list();
            resultMap = new TreeMap();
            for (Accountsbooks lbobj : bookList) {

                bookCode = lbobj.getCode();
                bookName = lbobj.getBookname();


                bookMap.put(bookCode, bookName);
            }

            resultMap.put("booklist", bookMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String book, String period, String voucherdate, String vouchertype) {
        Map resultMap = new HashMap();

        try {
            StringBuffer stringBuff = new StringBuffer();

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Computer No</th>");
            stringBuff.append("<th width=\"65%\">Remarks</th>");
            if (DateUtility.DateEqual(dateToString(getCurrentDate()), voucherdate)) {
                if (vouchertype.equalsIgnoreCase("J")) {
                    stringBuff.append("<th width=\"15%\">Journal No</th>");
                    stringBuff.append("<th width=\"10%\">Modification</th>");
                } else if (vouchertype.equalsIgnoreCase("R")) {
                    stringBuff.append("<th width=\"15%\">Receipt No</th>");
                    stringBuff.append("<th width=\"10%\">Modification</th>");
//                }else if(book.equalsIgnoreCase("4") && vouchertype.equalsIgnoreCase("P")){
//                    stringBuff.append("<th width=\"15%\">Voucher No</th>");
//                    stringBuff.append("<th width=\"10%\">Modification</th>");
                } else {
                    stringBuff.append("<th width=\"15%\">Voucher No</th>");
                    stringBuff.append("<th width=\"10%\">Modification</th>");
                }
            } else {
                if (vouchertype.equalsIgnoreCase("J")) {
                    stringBuff.append("<th width=\"25%\">Journal No</th>");
                } else if (vouchertype.equalsIgnoreCase("R")) {
                    stringBuff.append("<th width=\"25%\">Receipt No</th>");
                } else if (book.equalsIgnoreCase("4") && vouchertype.equalsIgnoreCase("P")) {
                    stringBuff.append("<th width=\"25%\">Voucher No</th>");
                } else {
                    stringBuff.append("<th width=\"15%\">Voucher No</th>");
                    stringBuff.append("<th width=\"10%\">Modification</th>");
                }
            }

//            if (vouchertype.equalsIgnoreCase("J") || vouchertype.equalsIgnoreCase("R")) {
//                if (DateUtility.DateEqual(dateToString(getCurrentDate()), voucherdate) ) {
//                    stringBuff.append("<th width=\"10%\">Modification</th>");
//                }
//
//                if (vouchertype.equalsIgnoreCase("J")) {
//                    stringBuff.append("<th width=\"15%\">Journal No</th>");
//                }
//                if (vouchertype.equalsIgnoreCase("R")) {
//                    stringBuff.append("<th width=\"15%\">Receipt No</th>");
//                }
//
//
//
//            } else {
//                if(book.equalsIgnoreCase("4") ){
//                    if( DateUtility.DateEqual(dateToString(getCurrentDate()), voucherdate)){
//                        stringBuff.append("<th width=\"15%\">Voucher No</th>");
//                        stringBuff.append("<th width=\"10%\">Modification</th>");
//                    }else{
//
//                    }
//                }
//                stringBuff.append("<th width=\"15%\">Voucher No</th>");
//                stringBuff.append("<th width=\"10%\">Modification</th>");
//            }

            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria vouCrit = session.createCriteria(Voucher.class);
            vouCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            vouCrit.add(Restrictions.sqlRestriction("vouchertype='" + vouchertype + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountbook='" + book + "'"));
            vouCrit.add(Restrictions.sqlRestriction("voucherdate='" + postgresDate(voucherdate) + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountingperiod='" + period + "'"));
            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));
//            if (vouchertype.equalsIgnoreCase("P")) {
//                vouCrit.add(Restrictions.sqlRestriction("locked is false"));
//            }

            List vouList = vouCrit.list();
            for (int i = 0; i < vouList.size(); i++) {
                Voucher voucherObj = (Voucher) vouList.get(i);
                stringBuff.append("<tr >");
                stringBuff.append("<td >" + voucherObj.getId() + "</td>");
                stringBuff.append("<td  >" + voucherObj.getNarration() + "</td>");
                if (voucherObj.getVoucherno() != null && voucherObj.getVoucherno().trim() != "") {
                    stringBuff.append("<td  align=\"center\">" + voucherObj.getVoucherno() + "</td>");
                } else {
                    stringBuff.append("<td  align=\"center\">" + "-" + "</td>");
                }
                if (DateUtility.DateEqual(dateToString(getCurrentDate()), voucherdate)) {
                    if (vouchertype.equalsIgnoreCase("R")) {
//                        stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"loancode\" id=\"" + "paycodeObj.getPaycode()" + "\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getId() + "')\">" + "</td>");
                        stringBuff.append("<td  align=\"center\">" + "-" + "</td>");
                    } else {
                        if (vouchertype.equalsIgnoreCase("P") && voucherObj.getLocked()) {
                            stringBuff.append("<td  align=\"center\">" + "-" + "</td>");
                        } else {
                            stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"loancode\" id=\"" + "paycodeObj.getPaycode()" + "\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getId() + "')\">" + "</td>");
                        }

                    }

                } else {
                    if (!book.equalsIgnoreCase("4") && vouchertype.equalsIgnoreCase("P")) {
                        if (vouchertype.equalsIgnoreCase("P") && voucherObj.getLocked()) {
                            stringBuff.append("<td  align=\"center\">" + "-" + "</td>");
                        } else {
                            stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"loancode\" id=\"" + "paycodeObj.getPaycode()" + "\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getId() + "')\">" + "</td>");
                        }
//                        stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"loancode\" id=\"" + "paycodeObj.getPaycode()" + "\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getId() + "')\">" + "</td>");
                    }
                }
//                if (!vouchertype.equalsIgnoreCase("R")) {
////                stringBuff.append("<td >" + "" + "</td>");
////                }else{
//                    stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"loancode\" id=\"" + "paycodeObj.getPaycode()" + "\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getId() + "')\">" + "</td>");
//                }


                //stringBuff.append("<td >" + "<input type=\"radio\" name=\"modivou\" id=\"modivou\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getAccountingyear().getId() + "','" + voucherObj.getVouchertype() + "','" + voucherObj.getAccountsbooks().getCode() + "');/>" + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            if (DateUtility.DateEqual(dateToString(getCurrentDate()), voucherdate)) {
                stringBuff.append("<tfoot>");
                stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"newvoucher\" id=\"newvoucher\" value=\"Add\"  onclick=\"showVoucherEntryForm();\"  >" + "</td>");
                stringBuff.append("</tfoot>");
            }
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherReceiptDetailDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"16%\">Region No</th>");
            stringBuff.append("<th width=\"20%\">Region Name</th>");
            stringBuff.append("<th width=\"16%\">No of Voucher received</th>");
            stringBuff.append("<th width=\"16%\">Receipt</th>");
            stringBuff.append("<th width=\"16%\">Payment</th>");
            stringBuff.append("<th width=\"16%\">Journal</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            StringBuilder query = new StringBuilder();
            query.append("select regionmaster.id,regionmaster.regionname,a.count from (select region,count(*) from voucher where voucherdate>='" + postgresDate(startingdate) + "' and voucherdate<='" + postgresDate(enddate) + "' group by region) as a left join regionmaster on regionmaster.id=a.region order by regionmaster.regionname ");
            SQLQuery subpayquery = session.createSQLQuery(query.toString());
            for (ListIterator its = subpayquery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                String regionid = (String) rows[0];
                String regionname = (String) rows[1];
                BigInteger cnt = (BigInteger) rows[2];
                stringBuff.append("<tr>");
                stringBuff.append("<td align=\"center\" >" + regionid + "</td>");
                stringBuff.append("<td align=\"center\" >" + regionname + "</td>");
                stringBuff.append("<td align=\"center\" >" + cnt + "</td>");
                stringBuff.append(getVoucherBreakup(session, regionid, startingdate, enddate));
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");
            resultMap.put("vouchergrid", stringBuff.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherReceiptDetailDetailsPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String filePathwithName) {
        Map map = new HashMap();
        BigDecimal debit;
        BigDecimal credit;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePathwithName);
        } catch (IOException ex) {
            Logger.getLogger(AccountVoucherServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder query = new StringBuilder();
        query.append("select regionmaster.id,regionmaster.regionname,a.count from (select region,count(*) from voucher where voucherdate>='" + postgresDate(startingdate) + "' and voucherdate<='" + postgresDate(enddate) + "' group by region) as a left join regionmaster on regionmaster.id=a.region order by regionmaster.regionname");
        SQLQuery trialbalancequery = session.createSQLQuery(query.toString());
        try {
            fileWriter.append("Voucher Receipt Details for the period " + startingdate + " to " + enddate + " as on " + new Date());
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(String.format("%100s", " ").replaceAll(" ", "-"));
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(String.format("%11s", "Region Code"));
            fileWriter.append(String.format("%15s", "Region Name"));
            fileWriter.append(String.format("%25s", "Total Voucher Received"));
            fileWriter.append(String.format("%15s", "Receipt"));
            fileWriter.append(String.format("%15s", "Payment"));
            fileWriter.append(String.format("%15s", "Journal"));
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(String.format("%100s", " ").replaceAll(" ", "-"));
            fileWriter.append(NEW_LINE_SEPARATOR);
            for (ListIterator ite = trialbalancequery.list().listIterator(); ite.hasNext();) {
                Object[] rows = (Object[]) ite.next();
                String regionid = (String) rows[0];
                String regionname = (String) rows[1];
                BigInteger cnt = (BigInteger) rows[2];
                fileWriter.append(String.format("%11s", regionid));
                fileWriter.append(String.format("%15s", regionname));
                fileWriter.append(String.format("%25s", cnt.toString()));
                Map result = getVoucherBreakupMap(session, regionid, startingdate, enddate);
                fileWriter.append(String.format("%15s", result.get("R")));
                fileWriter.append(String.format("%15s", result.get("P")));
                fileWriter.append(String.format("%15s", result.get("J")));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
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
        System.out.println("12345678");
        return map;
    }

    private String getVoucherBreakup(Session session, String region, String startdate, String enddate) {
        StringBuilder html = new StringBuilder();
        String queryStr = "select count(*) from voucher where region='" + region + "' and  voucherdate>='" + postgresDate(startdate) + "' and voucherdate<='" + postgresDate(enddate) + "' and vouchertype='R'";
        List cntList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (cntList.size() > 0) {
            BigInteger cnt = (BigInteger) cntList.get(0);
            html.append("<td align=\"center\" >" + cnt + "</td>");
        }
        queryStr = "select count(*) from voucher where region='" + region + "' and  voucherdate>='" + postgresDate(startdate) + "' and voucherdate<='" + postgresDate(enddate) + "' and vouchertype='P'";
        cntList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (cntList.size() > 0) {
            BigInteger cnt = (BigInteger) cntList.get(0);
            html.append("<td align=\"center\" >" + cnt + "</td>");
        }
        queryStr = "select count(*) from voucher where region='" + region + "' and  voucherdate>='" + postgresDate(startdate) + "' and voucherdate<='" + postgresDate(enddate) + "' and vouchertype='J'";
        cntList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (cntList.size() > 0) {
            BigInteger cnt = (BigInteger) cntList.get(0);
            html.append("<td align=\"center\" >" + cnt + "</td>");
        }
        return html.toString();
    }

    private Map getVoucherBreakupMap(Session session, String region, String startdate, String enddate) {
        Map result = new HashMap();
        StringBuilder html = new StringBuilder();
        String queryStr = "select count(*) from voucher where region='" + region + "' and  voucherdate>='" + postgresDate(startdate) + "' and voucherdate<='" + postgresDate(enddate) + "' and vouchertype='R'";
        List cntList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (cntList.size() > 0) {
            BigInteger cnt = (BigInteger) cntList.get(0);
            result.put("R", cnt);
        }
        queryStr = "select count(*) from voucher where region='" + region + "' and  voucherdate>='" + postgresDate(startdate) + "' and voucherdate<='" + postgresDate(enddate) + "' and vouchertype='P'";
        cntList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (cntList.size() > 0) {
            BigInteger cnt = (BigInteger) cntList.get(0);
            result.put("P", cnt);
        }
        queryStr = "select count(*) from voucher where region='" + region + "' and  voucherdate>='" + postgresDate(startdate) + "' and voucherdate<='" + postgresDate(enddate) + "' and vouchertype='J'";
        cntList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (cntList.size() > 0) {
            BigInteger cnt = (BigInteger) cntList.get(0);
            result.put("J", cnt);
        }
        return result;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherModifyDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String book, String period, String voucherdate, String vouchertype, String otherregion) {
        Map resultMap = new HashMap();

        try {
            StringBuffer stringBuff = new StringBuffer();

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Computer No</th>");
            stringBuff.append("<th width=\"65%\">Remarks</th>");
//            if (DateUtility.DateEqual(dateToString(getCurrentDate()), voucherdate) ) {
            if (vouchertype.equalsIgnoreCase("J")) {
                stringBuff.append("<th width=\"15%\">Journal No</th>");
                stringBuff.append("<th width=\"10%\">Modification</th>");
            } else if (vouchertype.equalsIgnoreCase("R")) {
                stringBuff.append("<th width=\"15%\">Receipt No</th>");
                stringBuff.append("<th width=\"10%\">Modification</th>");
//                }else if(book.equalsIgnoreCase("4") && vouchertype.equalsIgnoreCase("P")){
//                    stringBuff.append("<th width=\"15%\">Voucher No</th>");
//                    stringBuff.append("<th width=\"10%\">Modification</th>");
            } else {
                stringBuff.append("<th width=\"15%\">Voucher No</th>");
                stringBuff.append("<th width=\"10%\">Modification</th>");
            }
//            }else{
//                if (vouchertype.equalsIgnoreCase("J")) {
//                    stringBuff.append("<th width=\"25%\">Journal No</th>");
//                }else if (vouchertype.equalsIgnoreCase("R")) {
//                    stringBuff.append("<th width=\"25%\">Receipt No</th>");
//                }else if(book.equalsIgnoreCase("4") && vouchertype.equalsIgnoreCase("P")){
//                    stringBuff.append("<th width=\"25%\">Voucher No</th>");
//                }else{
//                    stringBuff.append("<th width=\"15%\">Voucher No</th>");
//                    stringBuff.append("<th width=\"10%\">Modification</th>");
//                }
//            }

//            if (vouchertype.equalsIgnoreCase("J") || vouchertype.equalsIgnoreCase("R")) {
//                if (DateUtility.DateEqual(dateToString(getCurrentDate()), voucherdate) ) {
//                    stringBuff.append("<th width=\"10%\">Modification</th>");
//                }
//
//                if (vouchertype.equalsIgnoreCase("J")) {
//                    stringBuff.append("<th width=\"15%\">Journal No</th>");
//                }
//                if (vouchertype.equalsIgnoreCase("R")) {
//                    stringBuff.append("<th width=\"15%\">Receipt No</th>");
//                }
//
//
//
//            } else {
//                if(book.equalsIgnoreCase("4") ){
//                    if( DateUtility.DateEqual(dateToString(getCurrentDate()), voucherdate)){
//                        stringBuff.append("<th width=\"15%\">Voucher No</th>");
//                        stringBuff.append("<th width=\"10%\">Modification</th>");
//                    }else{
//
//                    }
//                }
//                stringBuff.append("<th width=\"15%\">Voucher No</th>");
//                stringBuff.append("<th width=\"10%\">Modification</th>");
//            }

            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria vouCrit = session.createCriteria(Voucher.class);
            vouCrit.add(Restrictions.sqlRestriction("region='" + otherregion + "'"));
            vouCrit.add(Restrictions.sqlRestriction("vouchertype='" + vouchertype + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountbook='" + book + "'"));
            vouCrit.add(Restrictions.sqlRestriction("voucherdate='" + postgresDate(voucherdate) + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountingperiod='" + period + "'"));
            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));

            List vouList = vouCrit.list();
            for (int i = 0; i < vouList.size(); i++) {
                Voucher voucherObj = (Voucher) vouList.get(i);
                stringBuff.append("<tr >");
                stringBuff.append("<td >" + voucherObj.getId() + "</td>");
                stringBuff.append("<td  >" + voucherObj.getNarration() + "</td>");
                if (voucherObj.getVoucherno() != null && voucherObj.getVoucherno().trim() != "") {
                    stringBuff.append("<td  align=\"center\">" + voucherObj.getVoucherno() + "</td>");
                } else {
                    stringBuff.append("<td  align=\"center\">" + "-" + "</td>");
                }
//                if (DateUtility.DateEqual(dateToString(getCurrentDate()), voucherdate) ) {
                stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"loancode\" id=\"" + "paycodeObj.getPaycode()" + "\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getId() + "')\">" + "</td>");
//                }else{
//                     if(!book.equalsIgnoreCase("4") && vouchertype.equalsIgnoreCase("P")){
//                         stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"loancode\" id=\"" + "paycodeObj.getPaycode()" + "\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getId() + "')\">" + "</td>");
//                     }
//                }
//                if (!vouchertype.equalsIgnoreCase("R")) {
////                stringBuff.append("<td >" + "" + "</td>");
////                }else{
//                    stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"loancode\" id=\"" + "paycodeObj.getPaycode()" + "\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getId() + "')\">" + "</td>");
//                }


                //stringBuff.append("<td >" + "<input type=\"radio\" name=\"modivou\" id=\"modivou\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getAccountingyear().getId() + "','" + voucherObj.getVouchertype() + "','" + voucherObj.getAccountsbooks().getCode() + "');/>" + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
//            if (DateUtility.DateEqual(dateToString(getCurrentDate()), voucherdate) ) {
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"newvoucher\" id=\"newvoucher\" value=\"Add\"  onclick=\"showVoucherEntryForm();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
//            }
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());
            resultMap.put("regionname", getRegionmaster(session, otherregion).getRegionname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String booktype) {
        Map resultMap = new HashMap();
        int startYear = 0;
        int endYear = 0;
        String birthArray[];
        int retirementyear = 0;

        try {
            StringBuffer stringBuff = new StringBuffer();

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Computer No</th>");
            stringBuff.append("<th width=\"65%\">Remarks</th>");
            stringBuff.append("<th width=\"15%\">Voucher No</th>");
//            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("<th width=\"10%\">Modification</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
//            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\"  align=\"center\">" + "<input type=\"button\" CLASS=\"submitbu\" name=\"newvoucher\" id=\"newvoucher\" value=\"Add\"  onclick=\"showVoucherEntryForm();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            resultMap.put("period", accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear());
            resultMap.put("booklist", loadAccountBookDetails(session, request, response, LoggedInRegion, LoggedInUser, booktype).get("booklist"));
            resultMap.put("regionlist", loadRegionDetails(session).get("regionlist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getBankChallanEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();

        Map regionMap = new LinkedHashMap();
        String code = "";
        String bankname = "";
        try {
            Criteria rgnCrit = session.createCriteria(Bankledger.class);
            List<Bankledger> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Bankledger lbobj : rgnList) {
                code = lbobj.getCode();
                bankname = lbobj.getBankname();
                regionMap.put(code, bankname);
            }
            resultMap.put("banklist", regionMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {


            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"challantable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Challan No</th>");
            stringBuff.append("<th width=\"65%\">Date</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            ;
            stringBuff.append("</tr>");
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            StringBuffer stringBuff1 = new StringBuffer();
            stringBuff1.append("<FONT SIZE=2>");
            stringBuff1.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"receipttable\">");
            stringBuff1.append("<thead>");
            stringBuff1.append("<tr>");
            stringBuff1.append("<th width=\"10%\">Challan No</th>");
            stringBuff1.append("<th width=\"65%\">Date</th>");
            stringBuff1.append("</tr>");
            stringBuff1.append("</thead>");
            stringBuff1.append("<tbody>");
            stringBuff1.append("<tr >");
            stringBuff1.append("<td ></td>");
            stringBuff1.append("<td ></td>");
            ;
            stringBuff1.append("</tr>");
            stringBuff1.append("</tbody>");
            stringBuff1.append("</table>");
            stringBuff1.append("</FONT>");

            resultMap.put("challan", stringBuff.toString());
            resultMap.put("receiptdetails", stringBuff1.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getChallanDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String dateofchallan) {
        return getChallanDet(session, request, response, LoggedInRegion, LoggedInUser, type, dateofchallan);
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map addToChallanDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String recId, String challanno, String challandate) {
        Receiptpaymentdetails receiptpaymentdetailsObj;
        Transaction transaction;
        Criteria recptCrit = session.createCriteria(Receiptpaymentdetails.class);
        recptCrit.add(Restrictions.sqlRestriction("id='" + recId + "'"));
        List recptList = recptCrit.list();
        if (recptList.size() > 0) {
            receiptpaymentdetailsObj = (Receiptpaymentdetails) recptList.get(0);
            receiptpaymentdetailsObj.setBankchallan(CommonUtility.getBankchallan(session, challanno));
            transaction = session.beginTransaction();
            try {
                session.update(receiptpaymentdetailsObj);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

        }
        return getChallanDet(session, request, response, LoggedInRegion, LoggedInUser, type, challandate);
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map removeFromChallan(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String recpId) {
        Receiptpaymentdetails receiptpaymentdetailsObj;
        Transaction transaction;

        String challanId = "";
        Criteria recptCrit = session.createCriteria(Receiptpaymentdetails.class);
        recptCrit.add(Restrictions.sqlRestriction("id='" + recpId + "'"));
        List recptList = recptCrit.list();
        if (recptList.size() > 0) {
            receiptpaymentdetailsObj = (Receiptpaymentdetails) recptList.get(0);
            if (receiptpaymentdetailsObj.getBankchallan() != null) {
                challanId = receiptpaymentdetailsObj.getBankchallan().getId();
                receiptpaymentdetailsObj.setBankchallan(null);
                transaction = session.beginTransaction();
                try {
                    session.update(receiptpaymentdetailsObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
        }

        return loadChallanDet(session, request, response, LoggedInRegion, LoggedInUser, type, challanId);
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadChallanDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String challanId) {
        return loadChallanDet(session, request, response, LoggedInRegion, LoggedInUser, type, challanId);
    }

    public Map loadChallanDet(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String challanId) {
        Map resultMap = new HashMap();
        Criteria recptCrit = session.createCriteria(Receiptpaymentdetails.class);
        recptCrit.add(Restrictions.sqlRestriction("bankchallan='" + challanId + "'"));
        List recptList = recptCrit.list();
        StringBuffer stringBuff1 = new StringBuffer();
        stringBuff1.append("<FONT SIZE=2>");
        stringBuff1.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"challanreceipttable\">");
        stringBuff1.append("<thead>");
        stringBuff1.append("<tr>");
        stringBuff1.append("<th width=\"20%\">Voucher No</th>");
        stringBuff1.append("<th width=\"20%\">Voucher Date</th>");
        stringBuff1.append("<th width=\"20%\">Payment Mode</th>");
        stringBuff1.append("<th width=\"20%\">Number</th>");
        stringBuff1.append("<th width=\"10%\">Amount</th>");
        stringBuff1.append("<th width=\"10%\"></th>");
        stringBuff1.append("</tr>");
        stringBuff1.append("</thead>");
        stringBuff1.append("<tbody>");
        for (int i = 0; i < recptList.size(); i++) {
            Receiptpaymentdetails receiptpaymentdetailsObj = (Receiptpaymentdetails) recptList.get(i);
            stringBuff1.append("<tr >");
            stringBuff1.append("<td >" + receiptpaymentdetailsObj.getVoucher().getId() + "</td>");
            stringBuff1.append("<td >" + receiptpaymentdetailsObj.getVoucher().getVoucherdate() + "</td>");
            stringBuff1.append("<td >" + receiptpaymentdetailsObj.getPaymentmode().getType() + "</td>");
            stringBuff1.append("<td >" + receiptpaymentdetailsObj.getRefno() + "</td>");
            stringBuff1.append("<td >" + receiptpaymentdetailsObj.getAmount() + "</td>");
            stringBuff1.append("<td ><a href=\"#\" onclick=\"removeFromChallan('" + receiptpaymentdetailsObj.getId() + "');\" >Remove</a></td>");
            stringBuff1.append("</tr>");
        }
        stringBuff1.append("</tbody>");
        stringBuff1.append("</table>");
        stringBuff1.append("</FONT>");
        resultMap.put("receiptdetails", stringBuff1.toString());
        return resultMap;
    }

    public Map getChallanDet(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String dateofchallan) {
        Map resultMap = new HashMap();

        Map regionMap = new LinkedHashMap();
        Map challanMap = new LinkedHashMap();
        String code = "";
        String bankname = "";
        try {
            Criteria rgnCrit = session.createCriteria(Bankledger.class);
            List<Bankledger> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Bankledger lbobj : rgnList) {
                code = lbobj.getCode();
                bankname = lbobj.getBankname();
                regionMap.put(code, bankname);
            }
            resultMap.put("banklist", regionMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            Criteria challanCrit = session.createCriteria(Bankchallan.class);
            challanCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            challanCrit.add(Restrictions.sqlRestriction("challandate='" + postgresDate(dateofchallan) + "'"));
            challanCrit.add(Restrictions.sqlRestriction("type='" + type + "'"));
            challanCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            List challanList = challanCrit.list();
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"challantable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"20%\">Challan No</th>");
            stringBuff.append("<th width=\"20%\">Challan Date</th>");
            stringBuff.append("<th width=\"20%\">Bank</th>");
            stringBuff.append("<th width=\"20%\"></th>");
            stringBuff.append("<th width=\"20%\"></th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            for (int i = 0; i < challanList.size(); i++) {
                Bankchallan bankchallanObj = (Bankchallan) challanList.get(i);
                if (bankchallanObj.getBankledger() != null) {
                    challanMap.put(bankchallanObj.getId(), bankchallanObj.getId() + "-" + bankchallanObj.getChallandate() + "-" + bankchallanObj.getBankledger().getBankname());
                } else {
                    challanMap.put(bankchallanObj.getId(), bankchallanObj.getId() + "-" + bankchallanObj.getChallandate());
                }
                stringBuff.append("<tr >");
                stringBuff.append("<td >" + bankchallanObj.getId() + "</td>");
                stringBuff.append("<td >" + bankchallanObj.getChallandate() + "</td>");
                if (bankchallanObj.getBankledger() != null) {
                    stringBuff.append("<td >" + bankchallanObj.getBankledger().getBankname() + "</td>");
                } else {
                    stringBuff.append("<td ></td>");
                }
                stringBuff.append("<td ><a href=\"#\" onclick=\"showChallanDetails('" + bankchallanObj.getId() + "');\" >View</a></td>");
                stringBuff.append("<td ><a href=\"#\" onclick=\"printChallanDetails('" + bankchallanObj.getId() + "');\" >Print</a></td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            Criteria recptCrit = session.createCriteria(Receiptpaymentdetails.class);
            recptCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            recptCrit.add(Restrictions.sqlRestriction("voucher in (select id from voucher where voucherdate='" + postgresDate(dateofchallan) + "' and vouchertype='R'  )"));

            if (type.equalsIgnoreCase("chdd")) {
                recptCrit.add(Restrictions.sqlRestriction("paymentmode in ('2','3')"));
            }
            if (type.equalsIgnoreCase("cash")) {
                recptCrit.add(Restrictions.sqlRestriction("paymentmode in ('1')"));
            }
            recptCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            recptCrit.add(Restrictions.sqlRestriction("bankchallan is null"));
            List recptList = recptCrit.list();

            StringBuffer stringBuff1 = new StringBuffer();
            stringBuff1.append("<FONT SIZE=2>");
            stringBuff1.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"receipttable\">");
            stringBuff1.append("<thead>");
            stringBuff1.append("<tr>");
            stringBuff1.append("<th width=\"20%\"><input type=\"checkbox\" id=\"receiptall\" name=\"receiptall\" value=\"\" onclick=\"checkAll(this.value)\" /></th>");
            stringBuff1.append("<th width=\"20%\">Voucher No</th>");
            stringBuff1.append("<th width=\"40%\">Bank Name</th>");
//            stringBuff1.append("<th width=\"20%\">Voucher Date</th>");
//            stringBuff1.append("<th width=\"20%\">Payment Mode</th>");
            stringBuff1.append("<th width=\"20%\">Pay Mode</th>");
            stringBuff1.append("<th width=\"20%\">Cheque/DD Number</th>");
            stringBuff1.append("<th width=\"10%\">Amount</th>");
            stringBuff1.append("<th width=\"10%\"></th>");
            stringBuff1.append("</tr>");
            stringBuff1.append("</thead>");
            stringBuff1.append("<tbody>");
            for (int i = 0; i < recptList.size(); i++) {
                Receiptpaymentdetails receiptpaymentdetailsObj = (Receiptpaymentdetails) recptList.get(i);
                stringBuff1.append("<tr >");
                stringBuff1.append("<td align=\"center\"><input type=\"checkbox\" name=\"receipt\" id=\"receipt" + receiptpaymentdetailsObj.getId() + "\" value=\"" + receiptpaymentdetailsObj.getId() + "\" /></td>");
                stringBuff1.append("<td >" + receiptpaymentdetailsObj.getVoucher().getId() + "</td>");

                if (receiptpaymentdetailsObj.getBankledger() != null) {
                    stringBuff1.append("<td >" + receiptpaymentdetailsObj.getBankledger().getBankname() + "</td>");
                } else {
                    stringBuff1.append("<td >" + receiptpaymentdetailsObj.getOtherbankname() + "</td>");
                }
//                stringBuff1.append("<td >" + receiptpaymentdetailsObj.getBankledger().getBankname() + "</td>");
//                stringBuff1.append("<td >" + receiptpaymentdetailsObj.getVoucher().getVoucherdate() + "</td>");
//                stringBuff1.append("<td >" + receiptpaymentdetailsObj.getPaymentmode().getType() + "</td>");
                if (receiptpaymentdetailsObj.getPaymentmode() != null) {
                    stringBuff1.append("<td >" + receiptpaymentdetailsObj.getPaymentmode().getType() + "</td>");
                } else {
                    stringBuff1.append("<td ></td>");
                }
                stringBuff1.append("<td >" + receiptpaymentdetailsObj.getRefno() + "</td>");
                stringBuff1.append("<td >" + receiptpaymentdetailsObj.getAmount() + "</td>");
                stringBuff1.append("<td ><a href=\"#\" onclick=\"sendToChallan('" + receiptpaymentdetailsObj.getId() + "');\" >Send</a></td>");
                stringBuff1.append("</tr>");
            }
            stringBuff1.append("</tbody>");
            stringBuff1.append("<tfoot>");
            stringBuff1.append("<td >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"savebutton\" id=\"savebutton\" value=\"Create\" onclick=\"createChallan();\" >" + "</td>");
            stringBuff1.append("</tfoot>");
            stringBuff1.append("</tbody>");
            stringBuff1.append("</table>");
            stringBuff1.append("</FONT>");

            resultMap.put("challanlist", challanMap);
            resultMap.put("challan", stringBuff.toString());
            resultMap.put("receiptdetails", stringBuff1.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate) {
        Map resultMap = new HashMap();
        Transaction transaction;
        String voucherDetailsID = "";
        String voucherId;
        try {
            Regionmaster regionMasterObj = CommonUtility.getRegion(session, LoggedInRegion);

            String[] accountscodeArr = accountscode.split("TNCSCSEPATOR");
            String[] accountsamountArr = accountsamount.split("TNCSCSEPATOR");
            String[] accountsoptionArr = accountsoption.split("TNCSCSEPATOR");
            String[] partycodeArr = partycode.split("TNCSCSEPATOR");
            String[] partyamountArr = partyamount.split("TNCSCSEPATOR");
            String[] partypaymentmodeArr = partypaymentmode.split("TNCSCSEPATOR");
            String[] partyaliasArr = partyalias.split("TNCSCSEPATOR");
            String[] chequedateArr = chequedate.split("TNCSCSEPATOR");
            String[] refnoArr = refno.split("TNCSCSEPATOR");
            String[] bankcodeArr = bankcode.split("TNCSCSEPATOR");

            Voucher voucherObj = new Voucher();
            if (voucherType.equalsIgnoreCase("P") && !booktype.equalsIgnoreCase("4")) {
                //voucherId = SequenceNumberGenerator.getMaxPaymentVoucherid(session, LoggedInRegion, voucherType);
                voucherId = SequenceNumberGeneratorExt.getMaxPaymentVoucherid(session, LoggedInRegion, voucherType, period);
            } else if (voucherType.equalsIgnoreCase("J")) {
                voucherId = SequenceNumberGeneratorExt.getMaxJournalVoucherid(session, LoggedInRegion, voucherType, period);
            } else if (voucherType.equalsIgnoreCase("R")) {
                voucherId = SequenceNumberGeneratorExt.getMaxReceiptVoucherid(session, LoggedInRegion, voucherType, period);
            } else {
                voucherId = SequenceNumberGeneratorExt.getMaxPaymentVoucherid(session, LoggedInRegion, voucherType, period);
            }

            voucherObj.setId(voucherId);
            voucherObj.setAccountingyear(getAccountingYear(session, period));
            voucherObj.setAccountsbooks(getAccountBook(session, booktype));
            voucherObj.setNarration(naaration.toUpperCase());
            voucherObj.setRegionmaster(regionMasterObj);
            voucherObj.setVoucherdate(postgresDate(voucherdate));
            voucherObj.setVouchertype(voucherType);
            if (!voucherType.equalsIgnoreCase("P")) {
                voucherObj.setVoucherno(voucherId);
            }
            if (voucherType.equalsIgnoreCase("R")) {
                voucherObj.setLocked(Boolean.TRUE);
            } else {
                voucherObj.setLocked(Boolean.FALSE);
            }
            voucherObj.setFileno(fileno.toUpperCase());
            voucherObj.setSanctionedby(sanctionedby.toUpperCase().trim());
            voucherObj.setCreatedby(LoggedInUser);
            voucherObj.setCreateddate(getCurrentDate());

            voucherObj.setCancelled(Boolean.FALSE);
            transaction = session.beginTransaction();
            try {
                session.save(voucherObj);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

            for (int i = 0; i < accountscodeArr.length; i++) {
                if (!accountscodeArr[i].trim().equalsIgnoreCase("undefined")) {
                    String voucherDetaislId = SequenceNumberGeneratorExt.getMaxVoucherdetailsid(session, LoggedInRegion, period);
                    Voucherdetails voucherdetailsObj = new Voucherdetails();
                    voucherdetailsObj.setId(voucherDetaislId);
                    voucherdetailsObj.setRegionmaster(regionMasterObj);
                    voucherdetailsObj.setVoucher(voucherObj);
                    voucherdetailsObj.setAccountsheads(CommonUtility.geAccountsheads(session, accountscodeArr[i].trim()));
                    if (accountsamountArr[i].trim().length() > 0) {
                        voucherdetailsObj.setAmount(new BigDecimal(accountsamountArr[i].trim()));
                    } else {
                        voucherdetailsObj.setAmount(BigDecimal.ZERO);
                    }
                    voucherdetailsObj.setVoucheroption(accountsoptionArr[i].trim());
                    voucherdetailsObj.setCancelled(Boolean.FALSE);
                    if (accountsoptionArr[i].trim().equalsIgnoreCase("payment") || accountsoptionArr[i].trim().equalsIgnoreCase("debit")) {
                        if (accountsamountArr[i].trim().length() > 0) {
                            voucherdetailsObj.setDebit(new BigDecimal(accountsamountArr[i].trim()));
                        } else {
                            voucherdetailsObj.setDebit(BigDecimal.ZERO);
                        }
                    }
                    if (accountsoptionArr[i].trim().equalsIgnoreCase("receipt") || accountsoptionArr[i].trim().equalsIgnoreCase("credit") || accountsoptionArr[i].trim().equalsIgnoreCase("adjustment")) {
                        if (accountsamountArr[i].trim().length() > 0) {
                            voucherdetailsObj.setCredit(new BigDecimal(accountsamountArr[i].trim()));
                        } else {
                            voucherdetailsObj.setCredit(BigDecimal.ZERO);
                        }
                    }
                    voucherdetailsObj.setCreatedby(LoggedInUser);
                    voucherdetailsObj.setCreateddate(getCurrentDate());
                    voucherdetailsObj.setSerialno(i);
                    transaction = session.beginTransaction();
                    try {
                        session.persist(voucherdetailsObj);
                        session.flush();
                        session.clear();
                        transaction.commit();
                        voucherDetailsID = voucherDetaislId;
                    } catch (Exception e) {
                        transaction.rollback();
                    }
                }

            }

            if (partycode.trim().length() > 0) {
                for (int i = 0; i < partycodeArr.length; i++) {
                    if (!partycodeArr[i].equalsIgnoreCase("undefined")) {

                        Receiptpaymentdetails receiptpaymentdetailsObj = new Receiptpaymentdetails();
                        String id = SequenceNumberGeneratorExt.getReceiptpaymentdetailsid(session, LoggedInRegion, period);
                        receiptpaymentdetailsObj.setId(id);
                        receiptpaymentdetailsObj.setSerialno(i);
                        receiptpaymentdetailsObj.setRegion(LoggedInRegion);
                        if (partypaymentmodeArr[i].trim() == "1") {
                            receiptpaymentdetailsObj.setRefno("");
                            receiptpaymentdetailsObj.setBankledger(null);
                            receiptpaymentdetailsObj.setChequedate(null);
                        } else {
                            receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                            Bankledger bnam = CommonUtility.geBankledger(session, bankcodeArr[i].trim());
                            if (bnam != null) {
                                receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                            } else {
                                if (!bankcodeArr[i].trim().equalsIgnoreCase("undefined")) {
                                    receiptpaymentdetailsObj.setOtherbankname(bankcodeArr[i].trim());
                                }
                            }
                            receiptpaymentdetailsObj.setChequedate(postgresDate(chequedateArr[i].trim().replace('-', '/')));
                        }

//                    receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                        receiptpaymentdetailsObj.setVoucher(voucherObj);
                        receiptpaymentdetailsObj.setPaymentmode(CommonUtility.getPaymentmode(session, partypaymentmodeArr[i].trim()));
//                    receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                        if (partyamountArr[i].trim().length() > 0) {
                            receiptpaymentdetailsObj.setAmount(new BigDecimal(partyamountArr[i].trim()));
                        } else {
                            receiptpaymentdetailsObj.setAmount(BigDecimal.ZERO);
                        }
                        receiptpaymentdetailsObj.setCancelled(Boolean.FALSE);
                        receiptpaymentdetailsObj.setPartyledger(CommonUtility.getPartyledger(session, partycodeArr[i].trim()));
                        if (!partyaliasArr[i].equalsIgnoreCase("undefined") && !(partyaliasArr[i].toString().trim()).equalsIgnoreCase("")) {

                            receiptpaymentdetailsObj.setFavourof(partyaliasArr[i].trim().toUpperCase());
                        } else {
                            receiptpaymentdetailsObj.setFavourof(CommonUtility.getPartyledger(session, partycodeArr[i].trim()).getPartyname().toUpperCase());
                        }
                        receiptpaymentdetailsObj.setCreatedby(LoggedInUser);
                        receiptpaymentdetailsObj.setCreateddate(getCurrentDate());

                        transaction = session.beginTransaction();
                        try {
                            session.persist(receiptpaymentdetailsObj);
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }

                    }
                }
            }
            System.out.println("voucherId = " + voucherId);
            resultMap.put("voucherno", voucherId);
            resultMap.put("booktype", booktype);
            resultMap.put("voucherdate", voucherdate);
            resultMap.put("voucherDetailsID", voucherDetailsID);

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Transaction Failed");
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveVoucherDetailsALLRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate, String otherregion) {
        System.out.println("****************************** AccountVoucherServiceImpl class saveVoucherDetailsALLRegion method is calling ***************************");
        Map resultMap = new HashMap();
        Transaction transaction;
        String voucherDetailsID = "";
        String voucherId;

        System.out.println("period = " + period);
        System.out.println("booktype = " + booktype);
        System.out.println("voucherdate = " + voucherdate);
        System.out.println("voucherType = " + voucherType);
        System.out.println("updationtype = " + updationtype);
        System.out.println("voucherid = " + voucherid);
        System.out.println("naaration = " + naaration);
        System.out.println("accountscode = " + accountscode);
        System.out.println("accountsamount = " + accountsamount);
        System.out.println("accountsoption = " + accountsoption);
        System.out.println("partycode = " + partycode);
        System.out.println("partyamount = " + partyamount);
        System.out.println("partypaymentmode = " + partypaymentmode);
        System.out.println("refno = " + refno);
        System.out.println("bankcode = " + bankcode);
        System.out.println("sanctionedby = " + sanctionedby);
        System.out.println("fileno = " + fileno);
        System.out.println("partyalias = " + partyalias);
        System.out.println("chequedate = " + chequedate);
        System.out.println("otherregion = " + otherregion);
        try {
            // <editor-fold defaultstate="collapsed" desc="try">
            Regionmaster regionMasterObj = CommonUtility.getRegion(session, otherregion);

            String[] accountscodeArr = accountscode.split("TNCSCSEPATOR");
            String[] accountsamountArr = accountsamount.split("TNCSCSEPATOR");
            String[] accountsoptionArr = accountsoption.split("TNCSCSEPATOR");
            String[] partycodeArr = partycode.split("TNCSCSEPATOR");
            String[] partyamountArr = partyamount.split("TNCSCSEPATOR");
            String[] partypaymentmodeArr = partypaymentmode.split("TNCSCSEPATOR");
            String[] partyaliasArr = partyalias.split("TNCSCSEPATOR");
            String[] chequedateArr = chequedate.split("TNCSCSEPATOR");
            String[] refnoArr = refno.split("TNCSCSEPATOR");
            String[] bankcodeArr = bankcode.split("TNCSCSEPATOR");

            Voucher voucherObj = new Voucher();
            if (voucherType.equalsIgnoreCase("P") && !booktype.equalsIgnoreCase("4")) {
                voucherId = SequenceNumberGeneratorExt.getMaxPaymentVoucherid(session, otherregion, voucherType, period);
            } else if (voucherType.equalsIgnoreCase("J")) {
                voucherId = SequenceNumberGeneratorExt.getMaxJournalVoucherid(session, otherregion, voucherType, period);
            } else if (voucherType.equalsIgnoreCase("R")) {
                voucherId = SequenceNumberGeneratorExt.getMaxReceiptVoucherid(session, otherregion, voucherType, period);
            } else {
//                voucherId = SequenceNumberGenerator.getMaxBankVoucherid(session, otherregion, voucherType);
                voucherId = SequenceNumberGeneratorExt.getMaxPaymentVoucherid(session, otherregion, voucherType, period);
            }

            System.out.println("voucherId ::::::::::: " + voucherId);
            voucherObj.setId(voucherId);
            voucherObj.setAccountingyear(getAccountingYear(session, period));
            voucherObj.setAccountsbooks(getAccountBook(session, booktype));
            voucherObj.setNarration(naaration.toUpperCase());
            voucherObj.setRegionmaster(regionMasterObj);
            voucherObj.setVoucherdate(postgresDate(voucherdate));
            voucherObj.setVouchertype(voucherType);
            if (!voucherType.equalsIgnoreCase("P")) {
                voucherObj.setVoucherno(voucherId);
            }
            if (voucherType.equalsIgnoreCase("R")) {
                voucherObj.setLocked(Boolean.TRUE);
            } else {
                voucherObj.setLocked(Boolean.FALSE);
            }
            voucherObj.setFileno(fileno.toUpperCase());
            voucherObj.setSanctionedby(sanctionedby.toUpperCase().trim());
            voucherObj.setCreatedby(LoggedInUser);
            voucherObj.setCreateddate(getCurrentDate());

            voucherObj.setCancelled(Boolean.FALSE);
            transaction = session.beginTransaction();
            try {
                session.save(voucherObj);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

            for (int i = 0; i < accountscodeArr.length; i++) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                if (!accountscodeArr[i].trim().equalsIgnoreCase("undefined")) {
                    String voucherDetaislId = SequenceNumberGeneratorExt.getMaxVoucherdetailsid(session, otherregion, period);
                    Voucherdetails voucherdetailsObj = new Voucherdetails();
                    voucherdetailsObj.setId(voucherDetaislId);
                    voucherdetailsObj.setRegionmaster(regionMasterObj);
                    voucherdetailsObj.setVoucher(voucherObj);
                    voucherdetailsObj.setAccountsheads(CommonUtility.geAccountsheads(session, accountscodeArr[i].trim()));
                    if (accountsamountArr[i].trim().length() > 0) {
                        voucherdetailsObj.setAmount(new BigDecimal(accountsamountArr[i].trim()));
                    } else {
                        voucherdetailsObj.setAmount(BigDecimal.ZERO);
                    }
                    voucherdetailsObj.setVoucheroption(accountsoptionArr[i].trim());
                    voucherdetailsObj.setCancelled(Boolean.FALSE);
                    if (accountsoptionArr[i].trim().equalsIgnoreCase("payment") || accountsoptionArr[i].trim().equalsIgnoreCase("debit")) {
                        if (accountsamountArr[i].trim().length() > 0) {
                            voucherdetailsObj.setDebit(new BigDecimal(accountsamountArr[i].trim()));
                        } else {
                            voucherdetailsObj.setDebit(BigDecimal.ZERO);
                        }
                    }
                    if (accountsoptionArr[i].trim().equalsIgnoreCase("receipt") || accountsoptionArr[i].trim().equalsIgnoreCase("credit") || accountsoptionArr[i].trim().equalsIgnoreCase("adjustment")) {
                        if (accountsamountArr[i].trim().length() > 0) {
                            voucherdetailsObj.setCredit(new BigDecimal(accountsamountArr[i].trim()));
                        } else {
                            voucherdetailsObj.setCredit(BigDecimal.ZERO);
                        }
                    }
                    voucherdetailsObj.setCreatedby(LoggedInUser);
                    voucherdetailsObj.setCreateddate(getCurrentDate());
                    voucherdetailsObj.setSerialno(i);
                    transaction = session.beginTransaction();
                    try {
                        session.persist(voucherdetailsObj);
                        session.flush();
                        session.clear();
                        transaction.commit();
                        voucherDetailsID = voucherDetaislId;
                    } catch (Exception e) {
                        transaction.rollback();
                    }
                }
                // </editor-fold>

            }

            if (partycode.trim().length() > 0) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                for (int i = 0; i < partycodeArr.length; i++) {
                    if (!partycodeArr[i].equalsIgnoreCase("undefined")) {

                        Receiptpaymentdetails receiptpaymentdetailsObj = new Receiptpaymentdetails();
                        String id = SequenceNumberGeneratorExt.getReceiptpaymentdetailsid(session, otherregion, period);
                        receiptpaymentdetailsObj.setId(id);
                        receiptpaymentdetailsObj.setSerialno(i);
                        receiptpaymentdetailsObj.setRegion(otherregion);
                        if (partypaymentmodeArr[i].trim() == "1") {
                            receiptpaymentdetailsObj.setRefno("");
                            receiptpaymentdetailsObj.setBankledger(null);
                            receiptpaymentdetailsObj.setChequedate(null);
                        } else {
                            receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                            Bankledger bnam = CommonUtility.geBankledger(session, bankcodeArr[i].trim());
                            if (bnam != null) {
                                receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                            } else {
                                if (!bankcodeArr[i].trim().equalsIgnoreCase("undefined")) {
                                    receiptpaymentdetailsObj.setOtherbankname(bankcodeArr[i].trim());
                                }
                            }
                            receiptpaymentdetailsObj.setChequedate(postgresDate(chequedateArr[i].trim().replace('-', '/')));
                        }

//                    receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                        receiptpaymentdetailsObj.setVoucher(voucherObj);
                        receiptpaymentdetailsObj.setPaymentmode(CommonUtility.getPaymentmode(session, partypaymentmodeArr[i].trim()));
//                    receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                        if (partyamountArr[i].trim().length() > 0) {
                            receiptpaymentdetailsObj.setAmount(new BigDecimal(partyamountArr[i].trim()));
                        } else {
                            receiptpaymentdetailsObj.setAmount(BigDecimal.ZERO);
                        }
                        receiptpaymentdetailsObj.setCancelled(Boolean.FALSE);
                        receiptpaymentdetailsObj.setPartyledger(CommonUtility.getPartyledger(session, partycodeArr[i].trim()));
                        if (!partyaliasArr[i].equalsIgnoreCase("undefined") && !(partyaliasArr[i].toString().trim()).equalsIgnoreCase("")) {

                            receiptpaymentdetailsObj.setFavourof(partyaliasArr[i].trim().toUpperCase());
                        } else {
                            receiptpaymentdetailsObj.setFavourof(CommonUtility.getPartyledger(session, partycodeArr[i].trim()).getPartyname().toUpperCase());
                        }
                        receiptpaymentdetailsObj.setCreatedby(LoggedInUser);
                        receiptpaymentdetailsObj.setCreateddate(getCurrentDate());

                        transaction = session.beginTransaction();
                        try {
                            session.persist(receiptpaymentdetailsObj);
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }

                    }
                }
                // </editor-fold>
            }
            System.out.println("voucherId = " + voucherId);
            resultMap.put("voucherno", voucherId);
            resultMap.put("booktype", booktype);
            resultMap.put("voucherdate", voucherdate);
            resultMap.put("voucherDetailsID", voucherDetailsID);
            // </editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Transaction Failed");
        }

        return resultMap;
    }

    public Accountsbooks getAccountBook(Session session, String bookNo) {
        Accountsbooks accountsbooksObj = null;
        try {
            Criteria accCrit = session.createCriteria(Accountsbooks.class);
            accCrit.add(Restrictions.sqlRestriction("code='" + bookNo + "'"));
            List accList = accCrit.list();
            if (accList.size() > 0) {
                accountsbooksObj = (Accountsbooks) accList.get(0);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return accountsbooksObj;
    }

    public Accountingyear getAccountingYear(Session session, String code) {
        Accountingyear accountingyearObj = null;
        try {
            Criteria accCrit = session.createCriteria(Accountingyear.class);
            accCrit.add(Restrictions.sqlRestriction("id='" + code + "'"));
            List accList = accCrit.list();
            if (accList.size() > 0) {
                accountingyearObj = (Accountingyear) accList.get(0);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return accountingyearObj;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherDetailsForModi(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherid) {
        Map resultMap = new HashMap();
        resultMap.put("voucheraccountsdetails", getVoucherAccounts(session, voucherid));
        resultMap.put("voucherdetails", getVoucherDetails(session, voucherid));
        resultMap.put("voucherpartydetails", getPartyWisePaymentDetails(session, voucherid));
        return resultMap;
    }

    public Map getVoucherDetails(Session session, String voucherid) {
        Map resultMap = new HashMap();
        Voucher voucherObj;
        try {
            Criteria accCrit = session.createCriteria(Voucher.class);
            accCrit.add(Restrictions.sqlRestriction("id='" + voucherid + "'"));
            List accList = accCrit.list();
            if (accList.size() > 0) {
                voucherObj = (Voucher) accList.get(0);
                resultMap.put("narration", voucherObj.getNarration());
                resultMap.put("fileno", voucherObj.getFileno());
                resultMap.put("sanctionedby", voucherObj.getSanctionedby());
            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public Map getVoucherAccounts(Session session, String voucherid) {
        Map resultMap = new HashMap();
        Voucherdetails voucherdetailsObj;
        try {
            Criteria accCrit = session.createCriteria(Voucherdetails.class);
            accCrit.add(Restrictions.sqlRestriction("voucher='" + voucherid + "'"));
            accCrit.add(Restrictions.sqlRestriction("cancelled is false"));

            List accList = accCrit.list();
            System.out.println("size of voucher details" + accList.size());
            if (accList.size() > 0) {
                for (int i = 0; i < accList.size(); i++) {
                    voucherdetailsObj = (Voucherdetails) accList.get(i);
                    resultMap.put(i, voucherdetailsObj.getAccountsheads().getAcccode() + "  " + voucherdetailsObj.getAccountsheads().getAccname());
                    resultMap.put(i + accList.size(), voucherdetailsObj.getAmount());
                    if (voucherdetailsObj.getVoucheroption().equalsIgnoreCase("PAYMENT")) {
                        resultMap.put(i + accList.size() + accList.size(), "Payment");
                    }
                    if (voucherdetailsObj.getVoucheroption().equalsIgnoreCase("ADJUSTMENT")) {
                        resultMap.put(i + accList.size() + accList.size(), "Adjustment");
                    }
                    if (voucherdetailsObj.getVoucheroption().equalsIgnoreCase("RECEIPT")) {
                        resultMap.put(i + accList.size() + accList.size(), "Receipt");
                    }
                    if (voucherdetailsObj.getVoucheroption().equalsIgnoreCase("DEBIT")) {
                        resultMap.put(i + accList.size() + accList.size(), "Debit");
                    }
                    if (voucherdetailsObj.getVoucheroption().equalsIgnoreCase("CREDIT")) {
                        resultMap.put(i + accList.size() + accList.size(), "Credit");
                    }


                }
                resultMap.put("voucherdetailslength", accList.size());
            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map getPartyWisePaymentDetails(Session session, String voucherid) {
        Map resultMap = new HashMap();
        Receiptpaymentdetails receiptpaymentdetailsObj;
        try {
            Criteria partyCrit = session.createCriteria(Receiptpaymentdetails.class);
            partyCrit.add(Restrictions.sqlRestriction("voucher='" + voucherid + "'"));
            partyCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            List partyList = partyCrit.list();
            System.out.println("size of voucher details" + partyList.size());
            if (partyList.size() > 0) {
                for (int i = 0; i < partyList.size(); i++) {
                    receiptpaymentdetailsObj = (Receiptpaymentdetails) partyList.get(i);
                    if (receiptpaymentdetailsObj.getPartyledger() != null) {
                        resultMap.put(i, receiptpaymentdetailsObj.getPartyledger().getCode() + "  " + receiptpaymentdetailsObj.getPartyledger().getPartyname());
                    } else {
                        resultMap.put(i, "");
                    }
                    resultMap.put(i + partyList.size(), receiptpaymentdetailsObj.getAmount());
                    if (receiptpaymentdetailsObj.getPaymentmode() != null) {
                        resultMap.put(i + partyList.size() + partyList.size(), receiptpaymentdetailsObj.getPaymentmode().getCode() + "  " + receiptpaymentdetailsObj.getPaymentmode().getType());
                    } else {
                        resultMap.put(i + partyList.size() + partyList.size(), "");
                    }
                    resultMap.put(i + partyList.size() + partyList.size() + partyList.size(), receiptpaymentdetailsObj.getRefno());
                    if (receiptpaymentdetailsObj.getBankledger() != null) {
                        resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size(), receiptpaymentdetailsObj.getBankledger().getCode() + "  " + receiptpaymentdetailsObj.getBankledger().getBankname());
                    } else {
                        resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size(), receiptpaymentdetailsObj.getOtherbankname());
                    }
//                    resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size(), receiptpaymentdetailsObj.getFavourof());
                    if (receiptpaymentdetailsObj.getFavourof() != null) {
                        resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size(), receiptpaymentdetailsObj.getFavourof());
                    } else {
                        resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size(), "");
                    }
//                    System.out.println("i + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size()==="+i + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size());
                    if (receiptpaymentdetailsObj.getChequedate() != null) {
                        resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size(), dateToString(receiptpaymentdetailsObj.getChequedate()));
                    } else {
                        resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size(), "");
                    }

                }
                resultMap.put("voucherpartydetailslength", partyList.size());
            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map ModifyVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate) {
        Map resultMap = new HashMap();
        Transaction transaction;
        String voucherDetailsID = "";
        try {
            Regionmaster regionMasterObj = CommonUtility.getRegion(session, LoggedInRegion);

            String[] accountscodeArr = accountscode.split("TNCSCSEPATOR");
            String[] accountsamountArr = accountsamount.split("TNCSCSEPATOR");
            String[] accountsoptionArr = accountsoption.split("TNCSCSEPATOR");
            String[] partycodeArr = partycode.split("TNCSCSEPATOR");
            String[] partyamountArr = partyamount.split("TNCSCSEPATOR");
            String[] partypaymentmodeArr = partypaymentmode.split("TNCSCSEPATOR");
            String[] refnoArr = refno.split("TNCSCSEPATOR");
            String[] bankcodeArr = bankcode.split("TNCSCSEPATOR");
            String[] partyaliasArr = partyalias.split("TNCSCSEPATOR");
            String[] chequedateArr = chequedate.split("TNCSCSEPATOR");
            Voucher voucherObj = null;

            System.out.println("voucherid =" + voucherid);

            Criteria vouCrit = session.createCriteria(Voucher.class);
            vouCrit.add(Restrictions.sqlRestriction("id='" + voucherid + "'"));
            vouCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            vouCrit.add(Restrictions.sqlRestriction("vouchertype='" + voucherType + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountbook='" + booktype + "'"));
            vouCrit.add(Restrictions.sqlRestriction("voucherdate='" + postgresDate(voucherdate) + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountingperiod='" + period + "'"));
            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            List vouList = vouCrit.list();
            if (vouList.size() > 0) {
//            voucherObj = (Voucher) vouList.get(0);
                voucherObj = (Voucher) vouList.get(0);
                voucherObj.setAccountingyear(getAccountingYear(session, period));
                voucherObj.setAccountsbooks(getAccountBook(session, booktype));
                voucherObj.setNarration(naaration.toUpperCase());
                voucherObj.setRegionmaster(regionMasterObj);
//            voucherObj.setVoucherdate(postgresDate(voucherdate));
                voucherObj.setVouchertype(voucherType);
                voucherObj.setSanctionedby(sanctionedby.toUpperCase());

                voucherObj.setFileno(fileno.toUpperCase());
                voucherObj.setCancelled(Boolean.FALSE);
                voucherObj.setLocked(Boolean.FALSE);
                transaction = session.beginTransaction();
                try {
                    session.update(voucherObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }

            } else {
                voucherObj = new Voucher();
                String voucherId;                //= SequenceNumberGenerator.getMaxVoucherid(session, LoggedInRegion, voucherType);
                if (voucherType.equalsIgnoreCase("P") && !booktype.equalsIgnoreCase("4")) {
                    voucherId = SequenceNumberGeneratorExt.getMaxPaymentVoucherid(session, LoggedInRegion, voucherType, period);
                } else if (voucherType.equalsIgnoreCase("J")) {
                    voucherId = SequenceNumberGeneratorExt.getMaxJournalVoucherid(session, LoggedInRegion, voucherType, period);
                } else if (voucherType.equalsIgnoreCase("R")) {
                    voucherId = SequenceNumberGeneratorExt.getMaxReceiptVoucherid(session, LoggedInRegion, voucherType, period);
                } else {
                    voucherId = SequenceNumberGeneratorExt.getMaxPaymentVoucherid(session, LoggedInRegion, voucherType, period);
//                    voucherId = SequenceNumberGenerator.getMaxBankVoucherid(session, LoggedInRegion, voucherType);
                }
                voucherObj.setId(voucherId);
                voucherObj.setAccountingyear(getAccountingYear(session, period));
                voucherObj.setAccountsbooks(getAccountBook(session, booktype));
                voucherObj.setNarration(naaration.toUpperCase());
                voucherObj.setRegionmaster(regionMasterObj);
                voucherObj.setVoucherdate(postgresDate(voucherdate));
                voucherObj.setVouchertype(voucherType);
                voucherObj.setSanctionedby(sanctionedby.toUpperCase());
                voucherObj.setFileno(fileno.toUpperCase());
                voucherObj.setCancelled(Boolean.FALSE);
                voucherObj.setLocked(Boolean.FALSE);
                voucherObj.setCreatedby(LoggedInUser);
                voucherObj.setCreateddate(getCurrentDate());
                transaction = session.beginTransaction();
                try {
                    session.save(voucherObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }

            transaction = session.beginTransaction();
            try {
                session.createSQLQuery("UPDATE voucherdetails  SET cancelled  = true WHERE voucher='" + voucherObj.getId() + "' and region='" + LoggedInRegion + "'").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

            transaction = session.beginTransaction();
            try {
                session.createSQLQuery("UPDATE receiptpaymentdetails  SET cancelled  = true WHERE voucher='" + voucherObj.getId() + "' and region='" + LoggedInRegion + "'").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

            for (int i = 0; i < accountscodeArr.length; i++) {
                if (!accountscodeArr[i].equalsIgnoreCase("undefined")) {

                    Voucherdetails voucherdetailsObj = null;
                    Criteria vouDetCrit = session.createCriteria(Voucherdetails.class);
                    vouDetCrit.add(Restrictions.sqlRestriction("voucher='" + voucherObj.getId() + "'"));
                    vouDetCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
                    vouDetCrit.add(Restrictions.sqlRestriction("serialno=" + i));

                    List vouDetList = vouDetCrit.list();
                    if (vouDetList.size() > 0) {

                        voucherdetailsObj = (Voucherdetails) vouDetList.get(0);
                        voucherdetailsObj.setRegionmaster(regionMasterObj);
                        voucherdetailsObj.setVoucher(voucherObj);
                        voucherdetailsObj.setSerialno(i);
                        voucherdetailsObj.setAccountsheads(CommonUtility.geAccountsheads(session, accountscodeArr[i].trim()));
                        if (accountsamountArr[i].trim().length() > 0) {
                            voucherdetailsObj.setAmount(new BigDecimal(accountsamountArr[i].trim()));
                        } else {
                            voucherdetailsObj.setAmount(BigDecimal.ZERO);
                        }

                        voucherdetailsObj.setVoucheroption(accountsoptionArr[i].trim());
                        voucherdetailsObj.setCancelled(Boolean.FALSE);
                        if (accountsoptionArr[i].trim().equalsIgnoreCase("payment") || accountsoptionArr[i].trim().equalsIgnoreCase("debit")) {
                            if (accountsamountArr[i].trim().length() > 0) {
                                voucherdetailsObj.setDebit(new BigDecimal(accountsamountArr[i].trim()));
                            } else {
                                voucherdetailsObj.setDebit(BigDecimal.ZERO);
                            }
                        }

                        if (accountsoptionArr[i].trim().equalsIgnoreCase("receipt") || accountsoptionArr[i].trim().equalsIgnoreCase("credit") || accountsoptionArr[i].trim().equalsIgnoreCase("adjustment")) {
                            if (accountsamountArr[i].trim().length() > 0) {
                                voucherdetailsObj.setCredit(new BigDecimal(accountsamountArr[i].trim()));
                            } else {
                                voucherdetailsObj.setCredit(BigDecimal.ZERO);
                            }
                        }
                        voucherdetailsObj.setCreatedby(LoggedInUser);
                        voucherdetailsObj.setCreateddate(getCurrentDate());

                        voucherDetailsID = voucherdetailsObj.getId();

                        transaction = session.beginTransaction();
                        try {
                            session.update(voucherdetailsObj);
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }

                    } else {


//                        System.out.println("acc code " + accountscodeArr[i].trim());
                        String voucherDetaislId = SequenceNumberGeneratorExt.getMaxVoucherdetailsid(session, LoggedInRegion, period);
                        voucherdetailsObj = new Voucherdetails();
                        voucherdetailsObj.setId(voucherDetaislId);
                        voucherdetailsObj.setRegionmaster(regionMasterObj);
                        voucherdetailsObj.setSerialno(i);
                        voucherdetailsObj.setVoucher(voucherObj);
                        voucherdetailsObj.setAccountsheads(CommonUtility.geAccountsheads(session, accountscodeArr[i].trim()));
                        if (accountsamountArr[i].trim().length() > 0) {
                            voucherdetailsObj.setAmount(new BigDecimal(accountsamountArr[i].trim()));
                        } else {
                            voucherdetailsObj.setAmount(BigDecimal.ZERO);
                        }
                        voucherdetailsObj.setVoucheroption(accountsoptionArr[i].trim());
                        voucherdetailsObj.setCancelled(Boolean.FALSE);
                        if (accountsoptionArr[i].trim().equalsIgnoreCase("payment") || accountsoptionArr[i].trim().equalsIgnoreCase("debit")) {
                            if (accountsamountArr[i].trim().length() > 0) {
                                voucherdetailsObj.setDebit(new BigDecimal(accountsamountArr[i].trim()));
                            } else {
                                voucherdetailsObj.setDebit(BigDecimal.ZERO);
                            }
                        }

                        if (accountsoptionArr[i].trim().equalsIgnoreCase("receipt") || accountsoptionArr[i].trim().equalsIgnoreCase("credit") || accountsoptionArr[i].trim().equalsIgnoreCase("adjustment")) {
                            if (accountsamountArr[i].trim().length() > 0) {
                                voucherdetailsObj.setCredit(new BigDecimal(accountsamountArr[i].trim()));
                            } else {
                                voucherdetailsObj.setCredit(BigDecimal.ZERO);
                            }
                        }
                        voucherdetailsObj.setCreatedby(LoggedInUser);
                        voucherdetailsObj.setCreateddate(getCurrentDate());

                        voucherDetailsID = voucherDetaislId;

                        transaction = session.beginTransaction();
                        try {
                            session.persist(voucherdetailsObj);
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }
                    }

                }

            }
            if (!voucherType.trim().equalsIgnoreCase("J") || (!voucherType.trim().equalsIgnoreCase("P") && !booktype.equalsIgnoreCase("4"))) {
                if (partycode.trim().length() > 0) {
                    for (int i = 0; i < partycodeArr.length; i++) {
                        if (!partycodeArr[i].trim().equalsIgnoreCase("undefined")) {

                            Receiptpaymentdetails receiptpaymentdetailsObj = null;
                            Criteria recPayCrit = session.createCriteria(Receiptpaymentdetails.class);
                            recPayCrit.add(Restrictions.sqlRestriction("voucher='" + voucherObj.getId() + "'"));
                            recPayCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
                            recPayCrit.add(Restrictions.sqlRestriction("serialno=" + i));

                            List recPayList = recPayCrit.list();
                            if (recPayList.size() > 0) {
                                receiptpaymentdetailsObj = (Receiptpaymentdetails) recPayList.get(0);

//                        receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                                receiptpaymentdetailsObj.setVoucher(voucherObj);
                                receiptpaymentdetailsObj.setRegion(LoggedInRegion);
                                receiptpaymentdetailsObj.setPaymentmode(CommonUtility.getPaymentmode(session, partypaymentmodeArr[i].trim()));
//                        receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                                receiptpaymentdetailsObj.setSerialno(i);
                                if (partyamountArr[i].trim().length() > 0) {
                                    receiptpaymentdetailsObj.setAmount(new BigDecimal(partyamountArr[i].trim()));
                                } else {
                                    receiptpaymentdetailsObj.setAmount(BigDecimal.ZERO);
                                }
                                receiptpaymentdetailsObj.setCancelled(Boolean.FALSE);
                                receiptpaymentdetailsObj.setPartyledger(CommonUtility.getPartyledger(session, partycodeArr[i].trim()));
                                if (partypaymentmodeArr[i].trim() == "1") {
                                    receiptpaymentdetailsObj.setRefno("");
                                    receiptpaymentdetailsObj.setBankledger(null);
                                    receiptpaymentdetailsObj.setChequedate(null);
                                } else {
                                    receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                                    Bankledger bnam = CommonUtility.geBankledger(session, bankcodeArr[i].trim());
                                    if (bnam != null) {
                                        receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                                    } else {
                                        if (!bankcodeArr[i].trim().equalsIgnoreCase("undefined")) {
                                            receiptpaymentdetailsObj.setOtherbankname(bankcodeArr[i].trim());
                                        }
                                    }
                                    receiptpaymentdetailsObj.setChequedate(postgresDate(chequedateArr[i].trim().replace('-', '/')));
                                }
                                if (!partyaliasArr[i].equalsIgnoreCase("undefined") && !(partyaliasArr[i].toString().trim()).equalsIgnoreCase("")) {
                                    receiptpaymentdetailsObj.setFavourof(partyaliasArr[i].trim().toUpperCase());
                                } else {
                                    receiptpaymentdetailsObj.setFavourof(CommonUtility.getPartyledger(session, partycodeArr[i].trim()).getPartyname().toUpperCase());
                                }
                                receiptpaymentdetailsObj.setCreatedby(LoggedInUser);
                                receiptpaymentdetailsObj.setCreateddate(getCurrentDate());

                                transaction = session.beginTransaction();
                                try {
                                    session.update(receiptpaymentdetailsObj);
                                    transaction.commit();
                                } catch (Exception e) {
                                    transaction.rollback();
                                }

                            } else {

                                receiptpaymentdetailsObj = new Receiptpaymentdetails();
                                String id = SequenceNumberGeneratorExt.getReceiptpaymentdetailsid(session, LoggedInRegion, period);
                                receiptpaymentdetailsObj.setId(id);
                                receiptpaymentdetailsObj.setRegion(LoggedInRegion);
//                        receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                                receiptpaymentdetailsObj.setVoucher(voucherObj);
                                receiptpaymentdetailsObj.setSerialno(i);
                                receiptpaymentdetailsObj.setPaymentmode(CommonUtility.getPaymentmode(session, partypaymentmodeArr[i].trim()));
//                        receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                                if (partyamountArr[i].trim().length() > 0) {
                                    receiptpaymentdetailsObj.setAmount(new BigDecimal(partyamountArr[i].trim()));
                                } else {
                                    receiptpaymentdetailsObj.setAmount(BigDecimal.ZERO);
                                }
                                receiptpaymentdetailsObj.setCancelled(Boolean.FALSE);
                                receiptpaymentdetailsObj.setPartyledger(CommonUtility.getPartyledger(session, partycodeArr[i].trim()));
                                if (partypaymentmodeArr[i].trim() == "1") {
                                    receiptpaymentdetailsObj.setRefno("");
                                    receiptpaymentdetailsObj.setBankledger(null);
                                    receiptpaymentdetailsObj.setChequedate(null);
                                } else {
                                    receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                                    receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                                    receiptpaymentdetailsObj.setChequedate(postgresDate(chequedateArr[i].trim().replace('-', '/')));
                                }
                                if (!partyaliasArr[i].equalsIgnoreCase("undefined") && !(partyaliasArr[i].toString().trim()).equalsIgnoreCase("")) {
                                    receiptpaymentdetailsObj.setFavourof(partyaliasArr[i].trim().toUpperCase());
                                } else {
                                    receiptpaymentdetailsObj.setFavourof(CommonUtility.getPartyledger(session, partycodeArr[i].trim()).getPartyname().toUpperCase());
                                }
                                receiptpaymentdetailsObj.setCreatedby(LoggedInUser);
                                receiptpaymentdetailsObj.setCreateddate(getCurrentDate());

                                transaction = session.beginTransaction();
                                try {
                                    session.persist(receiptpaymentdetailsObj);
                                    transaction.commit();
                                } catch (Exception e) {
                                    transaction.rollback();
                                }
                            }


                        }
                    }
                }
            }
            System.out.println("After voucherid =" + voucherid);
            resultMap.put("voucherno", voucherid);
            resultMap.put("booktype", booktype);
            resultMap.put("voucherdate", voucherdate);
            resultMap.put("voucherDetailsID", voucherDetailsID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveModifyVoucherDetailsinALLRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate, String otherregion) {
        System.out.println("********************** AccountVoucherServiceImpl class saveModifyVoucherDetailsinALLRegion method is calling ***********************");
        Map resultMap = new HashMap();
        Transaction transaction;
        String voucherDetailsID = "";
        try {
            Regionmaster regionMasterObj = CommonUtility.getRegion(session, otherregion);
            String[] accountscodeArr = accountscode.split("TNCSCSEPATOR");
            String[] accountsamountArr = accountsamount.split("TNCSCSEPATOR");
            String[] accountsoptionArr = accountsoption.split("TNCSCSEPATOR");
            String[] partycodeArr = partycode.split("TNCSCSEPATOR");
            String[] partyamountArr = partyamount.split("TNCSCSEPATOR");
            String[] partypaymentmodeArr = partypaymentmode.split("TNCSCSEPATOR");
            String[] refnoArr = refno.split("TNCSCSEPATOR");
            String[] bankcodeArr = bankcode.split("TNCSCSEPATOR");
            String[] partyaliasArr = partyalias.split("TNCSCSEPATOR");
            String[] chequedateArr = chequedate.split("TNCSCSEPATOR");
            Voucher voucherObj = null;
            System.out.println("voucherid =" + voucherid);

            Criteria vouCrit = session.createCriteria(Voucher.class);
            vouCrit.add(Restrictions.sqlRestriction("id='" + voucherid + "'"));
            vouCrit.add(Restrictions.sqlRestriction("region='" + otherregion + "'"));
            vouCrit.add(Restrictions.sqlRestriction("vouchertype='" + voucherType + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountbook='" + booktype + "'"));
            vouCrit.add(Restrictions.sqlRestriction("voucherdate='" + postgresDate(voucherdate) + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountingperiod='" + period + "'"));
            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            List vouList = vouCrit.list();
            if (vouList.size() > 0) {
//            voucherObj = (Voucher) vouList.get(0);
                voucherObj = (Voucher) vouList.get(0);
                voucherObj.setAccountingyear(getAccountingYear(session, period));
                voucherObj.setAccountsbooks(getAccountBook(session, booktype));
                voucherObj.setNarration(naaration.toUpperCase());
                voucherObj.setRegionmaster(regionMasterObj);
//            voucherObj.setVoucherdate(postgresDate(voucherdate));
                voucherObj.setVouchertype(voucherType);
                voucherObj.setSanctionedby(sanctionedby.toUpperCase());

                voucherObj.setFileno(fileno.toUpperCase());
                voucherObj.setCancelled(Boolean.FALSE);
                voucherObj.setLocked(Boolean.FALSE);
                transaction = session.beginTransaction();
                try {
                    session.update(voucherObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }

            } else {
                voucherObj = new Voucher();
                String voucherId;                //= SequenceNumberGenerator.getMaxVoucherid(session, LoggedInRegion, voucherType);
                if (voucherType.equalsIgnoreCase("P") && !booktype.equalsIgnoreCase("4")) {
                    voucherId = SequenceNumberGeneratorExt.getMaxPaymentVoucherid(session, otherregion, voucherType, period);
                } else if (voucherType.equalsIgnoreCase("J")) {
                    voucherId = SequenceNumberGeneratorExt.getMaxJournalVoucherid(session, otherregion, voucherType, period);
                } else if (voucherType.equalsIgnoreCase("R")) {
                    voucherId = SequenceNumberGeneratorExt.getMaxReceiptVoucherid(session, otherregion, voucherType, period);
                } else {
                    voucherId = SequenceNumberGeneratorExt.getMaxPaymentVoucherid(session, otherregion, voucherType, period);
//                    voucherId = SequenceNumberGenerator.getMaxBankVoucherid(session, otherregion, voucherType);
                }
                voucherObj.setId(voucherId);
                voucherObj.setAccountingyear(getAccountingYear(session, period));
                voucherObj.setAccountsbooks(getAccountBook(session, booktype));
                voucherObj.setNarration(naaration.toUpperCase());
                voucherObj.setRegionmaster(regionMasterObj);
                voucherObj.setVoucherdate(postgresDate(voucherdate));
                voucherObj.setVouchertype(voucherType);
                voucherObj.setSanctionedby(sanctionedby.toUpperCase());
                voucherObj.setFileno(fileno.toUpperCase());
                voucherObj.setCancelled(Boolean.FALSE);
                voucherObj.setLocked(Boolean.FALSE);
                voucherObj.setCreatedby(LoggedInUser);
                voucherObj.setCreateddate(getCurrentDate());
                transaction = session.beginTransaction();
                try {
                    session.save(voucherObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }

            transaction = session.beginTransaction();
            try {
                session.createSQLQuery("UPDATE voucherdetails  SET cancelled  = true WHERE voucher='" + voucherObj.getId() + "' and region='" + otherregion + "'").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

            transaction = session.beginTransaction();
            try {
                session.createSQLQuery("UPDATE receiptpaymentdetails  SET cancelled  = true WHERE voucher='" + voucherObj.getId() + "' and region='" + otherregion + "'").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

            for (int i = 0; i < accountscodeArr.length; i++) {
                if (!accountscodeArr[i].equalsIgnoreCase("undefined")) {

                    Voucherdetails voucherdetailsObj = null;
                    Criteria vouDetCrit = session.createCriteria(Voucherdetails.class);
                    vouDetCrit.add(Restrictions.sqlRestriction("voucher='" + voucherObj.getId() + "'"));
                    vouDetCrit.add(Restrictions.sqlRestriction("region='" + otherregion + "'"));
                    vouDetCrit.add(Restrictions.sqlRestriction("serialno=" + i));

                    List vouDetList = vouDetCrit.list();
                    if (vouDetList.size() > 0) {

                        voucherdetailsObj = (Voucherdetails) vouDetList.get(0);
                        voucherdetailsObj.setRegionmaster(regionMasterObj);
                        voucherdetailsObj.setVoucher(voucherObj);
                        voucherdetailsObj.setSerialno(i);
                        voucherdetailsObj.setAccountsheads(CommonUtility.geAccountsheads(session, accountscodeArr[i].trim()));
                        if (accountsamountArr[i].trim().length() > 0) {
                            voucherdetailsObj.setAmount(new BigDecimal(accountsamountArr[i].trim()));
                        } else {
                            voucherdetailsObj.setAmount(BigDecimal.ZERO);
                        }

                        voucherdetailsObj.setVoucheroption(accountsoptionArr[i].trim());
                        voucherdetailsObj.setCancelled(Boolean.FALSE);
                        if (accountsoptionArr[i].trim().equalsIgnoreCase("payment") || accountsoptionArr[i].trim().equalsIgnoreCase("debit")) {
                            if (accountsamountArr[i].trim().length() > 0) {
                                voucherdetailsObj.setDebit(new BigDecimal(accountsamountArr[i].trim()));
                            } else {
                                voucherdetailsObj.setDebit(BigDecimal.ZERO);
                            }
                        }

                        if (accountsoptionArr[i].trim().equalsIgnoreCase("receipt") || accountsoptionArr[i].trim().equalsIgnoreCase("credit") || accountsoptionArr[i].trim().equalsIgnoreCase("adjustment")) {
                            if (accountsamountArr[i].trim().length() > 0) {
                                voucherdetailsObj.setCredit(new BigDecimal(accountsamountArr[i].trim()));
                            } else {
                                voucherdetailsObj.setCredit(BigDecimal.ZERO);
                            }
                        }
                        voucherdetailsObj.setCreatedby(LoggedInUser);
                        voucherdetailsObj.setCreateddate(getCurrentDate());

                        voucherDetailsID = voucherdetailsObj.getId();

                        transaction = session.beginTransaction();
                        try {
                            session.update(voucherdetailsObj);
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }

                    } else {


//                        System.out.println("acc code " + accountscodeArr[i].trim());
                        String voucherDetaislId = SequenceNumberGeneratorExt.getMaxVoucherdetailsid(session, otherregion, period);
                        voucherdetailsObj = new Voucherdetails();
                        voucherdetailsObj.setId(voucherDetaislId);
                        voucherdetailsObj.setRegionmaster(regionMasterObj);
                        voucherdetailsObj.setSerialno(i);
                        voucherdetailsObj.setVoucher(voucherObj);
                        voucherdetailsObj.setAccountsheads(CommonUtility.geAccountsheads(session, accountscodeArr[i].trim()));
                        if (accountsamountArr[i].trim().length() > 0) {
                            voucherdetailsObj.setAmount(new BigDecimal(accountsamountArr[i].trim()));
                        } else {
                            voucherdetailsObj.setAmount(BigDecimal.ZERO);
                        }
                        voucherdetailsObj.setVoucheroption(accountsoptionArr[i].trim());
                        voucherdetailsObj.setCancelled(Boolean.FALSE);
                        if (accountsoptionArr[i].trim().equalsIgnoreCase("payment") || accountsoptionArr[i].trim().equalsIgnoreCase("debit")) {
                            if (accountsamountArr[i].trim().length() > 0) {
                                voucherdetailsObj.setDebit(new BigDecimal(accountsamountArr[i].trim()));
                            } else {
                                voucherdetailsObj.setDebit(BigDecimal.ZERO);
                            }
                        }

                        if (accountsoptionArr[i].trim().equalsIgnoreCase("receipt") || accountsoptionArr[i].trim().equalsIgnoreCase("credit") || accountsoptionArr[i].trim().equalsIgnoreCase("adjustment")) {
                            if (accountsamountArr[i].trim().length() > 0) {
                                voucherdetailsObj.setCredit(new BigDecimal(accountsamountArr[i].trim()));
                            } else {
                                voucherdetailsObj.setCredit(BigDecimal.ZERO);
                            }
                        }
                        voucherdetailsObj.setCreatedby(LoggedInUser);
                        voucherdetailsObj.setCreateddate(getCurrentDate());

                        voucherDetailsID = voucherDetaislId;

                        transaction = session.beginTransaction();
                        try {
                            session.persist(voucherdetailsObj);
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }
                    }

                }

            }
            if (!voucherType.trim().equalsIgnoreCase("J") || (!voucherType.trim().equalsIgnoreCase("P") && !booktype.equalsIgnoreCase("4"))) {
                if (partycode.trim().length() > 0) {
                    for (int i = 0; i < partycodeArr.length; i++) {
                        if (!partycodeArr[i].trim().equalsIgnoreCase("undefined")) {

                            Receiptpaymentdetails receiptpaymentdetailsObj = null;
                            Criteria recPayCrit = session.createCriteria(Receiptpaymentdetails.class);
                            recPayCrit.add(Restrictions.sqlRestriction("voucher='" + voucherObj.getId() + "'"));
                            recPayCrit.add(Restrictions.sqlRestriction("region='" + otherregion + "'"));
                            recPayCrit.add(Restrictions.sqlRestriction("serialno=" + i));

                            List recPayList = recPayCrit.list();
                            if (recPayList.size() > 0) {
                                receiptpaymentdetailsObj = (Receiptpaymentdetails) recPayList.get(0);

//                        receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                                receiptpaymentdetailsObj.setVoucher(voucherObj);
//                                receiptpaymentdetailsObj.setRegion(LoggedInRegion);
                                receiptpaymentdetailsObj.setPaymentmode(CommonUtility.getPaymentmode(session, partypaymentmodeArr[i].trim()));
//                        receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                                receiptpaymentdetailsObj.setSerialno(i);
                                if (partyamountArr[i].trim().length() > 0) {
                                    receiptpaymentdetailsObj.setAmount(new BigDecimal(partyamountArr[i].trim()));
                                } else {
                                    receiptpaymentdetailsObj.setAmount(BigDecimal.ZERO);
                                }
                                receiptpaymentdetailsObj.setCancelled(Boolean.FALSE);
                                receiptpaymentdetailsObj.setPartyledger(CommonUtility.getPartyledger(session, partycodeArr[i].trim()));
                                if (partypaymentmodeArr[i].trim() == "1") {
                                    receiptpaymentdetailsObj.setRefno("");
                                    receiptpaymentdetailsObj.setBankledger(null);
                                    receiptpaymentdetailsObj.setChequedate(null);
                                } else {
                                    receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                                    Bankledger bnam = CommonUtility.geBankledger(session, bankcodeArr[i].trim());
                                    if (bnam != null) {
                                        receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                                    } else {
                                        if (!bankcodeArr[i].trim().equalsIgnoreCase("undefined")) {
                                            receiptpaymentdetailsObj.setOtherbankname(bankcodeArr[i].trim());
                                        }
                                    }
                                    receiptpaymentdetailsObj.setChequedate(postgresDate(chequedateArr[i].trim().replace('-', '/')));
                                }
                                if (!partyaliasArr[i].equalsIgnoreCase("undefined") && !(partyaliasArr[i].toString().trim()).equalsIgnoreCase("")) {
                                    receiptpaymentdetailsObj.setFavourof(partyaliasArr[i].trim().toUpperCase());
                                } else {
                                    receiptpaymentdetailsObj.setFavourof(CommonUtility.getPartyledger(session, partycodeArr[i].trim()).getPartyname().toUpperCase());
                                }
                                receiptpaymentdetailsObj.setCreatedby(LoggedInUser);
                                receiptpaymentdetailsObj.setCreateddate(getCurrentDate());

                                transaction = session.beginTransaction();
                                try {
                                    session.update(receiptpaymentdetailsObj);
                                    transaction.commit();
                                } catch (Exception e) {
                                    transaction.rollback();
                                }

                            } else {

                                receiptpaymentdetailsObj = new Receiptpaymentdetails();
                                String id = SequenceNumberGeneratorExt.getReceiptpaymentdetailsid(session, otherregion, period);
                                receiptpaymentdetailsObj.setId(id);
                                receiptpaymentdetailsObj.setRegion(LoggedInRegion);
//                        receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                                receiptpaymentdetailsObj.setVoucher(voucherObj);
                                receiptpaymentdetailsObj.setSerialno(i);
                                receiptpaymentdetailsObj.setPaymentmode(CommonUtility.getPaymentmode(session, partypaymentmodeArr[i].trim()));
//                        receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                                if (partyamountArr[i].trim().length() > 0) {
                                    receiptpaymentdetailsObj.setAmount(new BigDecimal(partyamountArr[i].trim()));
                                } else {
                                    receiptpaymentdetailsObj.setAmount(BigDecimal.ZERO);
                                }
                                receiptpaymentdetailsObj.setCancelled(Boolean.FALSE);
                                receiptpaymentdetailsObj.setPartyledger(CommonUtility.getPartyledger(session, partycodeArr[i].trim()));
                                if (partypaymentmodeArr[i].trim() == "1") {
                                    receiptpaymentdetailsObj.setRefno("");
                                    receiptpaymentdetailsObj.setBankledger(null);
                                    receiptpaymentdetailsObj.setChequedate(null);
                                } else {
                                    receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
                                    receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
                                    receiptpaymentdetailsObj.setChequedate(postgresDate(chequedateArr[i].trim().replace('-', '/')));
                                }
                                if (!partyaliasArr[i].equalsIgnoreCase("undefined") && !(partyaliasArr[i].toString().trim()).equalsIgnoreCase("")) {
                                    receiptpaymentdetailsObj.setFavourof(partyaliasArr[i].trim().toUpperCase());
                                } else {
                                    receiptpaymentdetailsObj.setFavourof(CommonUtility.getPartyledger(session, partycodeArr[i].trim()).getPartyname().toUpperCase());
                                }
                                receiptpaymentdetailsObj.setCreatedby(LoggedInUser);
                                receiptpaymentdetailsObj.setCreateddate(getCurrentDate());

                                transaction = session.beginTransaction();
                                try {
                                    session.persist(receiptpaymentdetailsObj);
                                    transaction.commit();
                                } catch (Exception e) {
                                    transaction.rollback();
                                }
                            }


                        }
                    }
                }
            }
            System.out.println("After voucherid =" + voucherid);
            resultMap.put("voucherno", voucherid);
            resultMap.put("booktype", booktype);
            resultMap.put("voucherdate", voucherdate);
            resultMap.put("voucherDetailsID", voucherDetailsID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map saveModifyVoucherDetailsinALLRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate, String otherregion) {
//        Map resultMap = new HashMap();
//        Transaction transaction;
//        String voucherDetailsID = "";
//        try {
//            Regionmaster regionMasterObj = CommonUtility.getRegion(session, otherregion);
//            String[] accountscodeArr = accountscode.split("TNCSCSEPATOR");
//            String[] accountsamountArr = accountsamount.split("TNCSCSEPATOR");
//            String[] accountsoptionArr = accountsoption.split("TNCSCSEPATOR");
//            String[] partycodeArr = partycode.split("TNCSCSEPATOR");
//            String[] partyamountArr = partyamount.split("TNCSCSEPATOR");
//            String[] partypaymentmodeArr = partypaymentmode.split("TNCSCSEPATOR");
//            String[] refnoArr = refno.split("TNCSCSEPATOR");
//            String[] bankcodeArr = bankcode.split("TNCSCSEPATOR");
//            String[] partyaliasArr = partyalias.split("TNCSCSEPATOR");
//            String[] chequedateArr = chequedate.split("TNCSCSEPATOR");
//            Voucher voucherObj = null;
//            System.out.println("voucherid =" + voucherid);
//            Criteria vouCrit = session.createCriteria(Voucher.class);
//            vouCrit.add(Restrictions.sqlRestriction("id='" + voucherid + "'"));
//            vouCrit.add(Restrictions.sqlRestriction("region='" + otherregion + "'"));
//            vouCrit.add(Restrictions.sqlRestriction("vouchertype='" + voucherType + "'"));
//            vouCrit.add(Restrictions.sqlRestriction("accountbook='" + booktype + "'"));
//            vouCrit.add(Restrictions.sqlRestriction("voucherdate='" + postgresDate(voucherdate) + "'"));
//            vouCrit.add(Restrictions.sqlRestriction("accountingperiod='" + period + "'"));
//            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));
//            List vouList = vouCrit.list();
//            if (vouList.size() > 0) {
//                voucherObj = (Voucher) vouList.get(0);
//                Voucherhistory voucherhistoryObj = new Voucherhistory();
//                String voucherHistoryId;
//                voucherHistoryId = SequenceNumberGenerator.getMaxHistoryVoucherid(session, otherregion);
//                BeanUtils.copyProperties(voucherhistoryObj, voucherObj);
//                voucherhistoryObj.setId(voucherHistoryId);
//                voucherhistoryObj.setVoucher(voucherObj);
//                transaction = session.beginTransaction();
//                try {
//                    session.save(voucherhistoryObj);
//                    transaction.commit();
//                } catch (Exception e) {
//                    transaction.rollback();
//                }
//                System.out.println("transaction.wasCommitted()====" + transaction.wasCommitted());
//                if (transaction.wasCommitted()) {
//                    voucherObj.setAccountingyear(getAccountingYear(session, period));
//                    voucherObj.setAccountsbooks(getAccountBook(session, booktype));
//                    voucherObj.setNarration(naaration.toUpperCase());
//                    voucherObj.setRegionmaster(regionMasterObj);
//                    voucherObj.setVouchertype(voucherType);
//                    voucherObj.setSanctionedby(sanctionedby.toUpperCase());
//                    voucherObj.setFileno(fileno.toUpperCase());
//                    voucherObj.setCancelled(Boolean.FALSE);
//                    voucherObj.setLocked(Boolean.FALSE);
//                    voucherObj.setCreatedby(LoggedInUser);
//                    voucherObj.setCreateddate(getCurrentDate());
//                    transaction = session.beginTransaction();
//                    try {
//                        session.update(voucherObj);
//                        transaction.commit();
//                    } catch (Exception e) {
//                        transaction.rollback();
//                    }
//                }
//
//                if (transaction.wasCommitted()) {
//                    Voucherdetails voucherdetailsnewObj = null;
//                    Criteria vouDetCrit = session.createCriteria(Voucherdetails.class);
//                    vouDetCrit.add(Restrictions.sqlRestriction("voucher='" + voucherObj.getId() + "'"));
//                    vouDetCrit.add(Restrictions.sqlRestriction("region='" + otherregion + "'"));
//                    vouDetCrit.add(Restrictions.sqlRestriction("cancelled is false"));
//                    List vouDetList = vouDetCrit.list();
//                    if (vouDetList.size() > 0) {
//                        for (int i = 0; i < vouDetList.size(); i++) {
//                            voucherdetailsnewObj = (Voucherdetails) vouDetList.get(i);
//                            Voucherdetailshistory voucherdetailshistoryObj = new Voucherdetailshistory();
//                            String voucherDetailsHistoryId;
//                            voucherDetailsHistoryId = SequenceNumberGenerator.getMaxHistoryVoucherDetailsid(session, otherregion);
//                            BeanUtils.copyProperties(voucherdetailshistoryObj, voucherdetailsnewObj);
//                            voucherdetailshistoryObj.setId(voucherDetailsHistoryId);
//                            voucherdetailshistoryObj.setVoucherdetails(voucherdetailsnewObj);
//                            transaction = session.beginTransaction();
//                            try {
//                                session.save(voucherdetailshistoryObj);
//                                transaction.commit();
//                            } catch (Exception e) {
//                                transaction.rollback();
//                            }
//                        }
//                    }
//                    if (transaction.wasCommitted()) {
//                        transaction = session.beginTransaction();
//                        try {
//                            session.createSQLQuery("UPDATE voucherdetails  SET cancelled  = true WHERE voucher='" + voucherObj.getId() + "' and region='" + otherregion + "'").executeUpdate();
//                            transaction.commit();
//                        } catch (Exception e) {
//                            transaction.rollback();
//                        }
//                    }
//
//                    if (transaction.wasCommitted()) {
//
//                        for (int i = 0; i < accountscodeArr.length; i++) {
//                            if (!accountscodeArr[i].equalsIgnoreCase("undefined")) {
//
//                                Voucherdetails voucherdetailsObj = null;
//                                Criteria vouDetCrit1 = session.createCriteria(Voucherdetails.class);
//                                vouDetCrit1.add(Restrictions.sqlRestriction("voucher='" + voucherObj.getId() + "'"));
//                                vouDetCrit1.add(Restrictions.sqlRestriction("region='" + otherregion + "'"));
//                                vouDetCrit1.add(Restrictions.sqlRestriction("serialno=" + i));
//
//                                List vouDetList1 = vouDetCrit1.list();
//                                if (vouDetList1.size() > 0) {
//
//                                    voucherdetailsObj = (Voucherdetails) vouDetList1.get(0);
//                                    voucherdetailsObj.setRegionmaster(regionMasterObj);
//                                    voucherdetailsObj.setVoucher(voucherObj);
//                                    voucherdetailsObj.setSerialno(i);
//                                    voucherdetailsObj.setAccountsheads(CommonUtility.geAccountsheads(session, accountscodeArr[i].trim()));
//                                    if (accountsamountArr[i].trim().length() > 0) {
//                                        voucherdetailsObj.setAmount(new BigDecimal(accountsamountArr[i].trim()));
//                                    } else {
//                                        voucherdetailsObj.setAmount(BigDecimal.ZERO);
//                                    }
//
//                                    voucherdetailsObj.setVoucheroption(accountsoptionArr[i].trim());
//                                    voucherdetailsObj.setCancelled(Boolean.FALSE);
//                                    if (accountsoptionArr[i].trim().equalsIgnoreCase("payment") || accountsoptionArr[i].trim().equalsIgnoreCase("debit")) {
//                                        if (accountsamountArr[i].trim().length() > 0) {
//                                            voucherdetailsObj.setDebit(new BigDecimal(accountsamountArr[i].trim()));
//                                        } else {
//                                            voucherdetailsObj.setDebit(BigDecimal.ZERO);
//                                        }
//                                    }
//
//                                    if (accountsoptionArr[i].trim().equalsIgnoreCase("receipt") || accountsoptionArr[i].trim().equalsIgnoreCase("credit") || accountsoptionArr[i].trim().equalsIgnoreCase("adjustment")) {
//                                        if (accountsamountArr[i].trim().length() > 0) {
//                                            voucherdetailsObj.setCredit(new BigDecimal(accountsamountArr[i].trim()));
//                                        } else {
//                                            voucherdetailsObj.setCredit(BigDecimal.ZERO);
//                                        }
//                                    }
//                                    voucherdetailsObj.setCreatedby(LoggedInUser);
//                                    voucherdetailsObj.setCreateddate(getCurrentDate());
//
//                                    voucherDetailsID = voucherdetailsObj.getId();
//
//                                    transaction = session.beginTransaction();
//                                    try {
//                                        session.update(voucherdetailsObj);
//                                        transaction.commit();
//                                    } catch (Exception e) {
//                                        transaction.rollback();
//                                    }
//
//                                } else {
//
//
////                        System.out.println("acc code " + accountscodeArr[i].trim());
//                                    String voucherDetaislId = SequenceNumberGenerator.getMaxVoucherdetailsid(session, otherregion);
//                                    voucherdetailsObj = new Voucherdetails();
//                                    voucherdetailsObj.setId(voucherDetaislId);
//                                    voucherdetailsObj.setRegionmaster(regionMasterObj);
//                                    voucherdetailsObj.setSerialno(i);
//                                    voucherdetailsObj.setVoucher(voucherObj);
//                                    voucherdetailsObj.setAccountsheads(CommonUtility.geAccountsheads(session, accountscodeArr[i].trim()));
//                                    if (accountsamountArr[i].trim().length() > 0) {
//                                        voucherdetailsObj.setAmount(new BigDecimal(accountsamountArr[i].trim()));
//                                    } else {
//                                        voucherdetailsObj.setAmount(BigDecimal.ZERO);
//                                    }
//                                    voucherdetailsObj.setVoucheroption(accountsoptionArr[i].trim());
//                                    voucherdetailsObj.setCancelled(Boolean.FALSE);
//                                    if (accountsoptionArr[i].trim().equalsIgnoreCase("payment") || accountsoptionArr[i].trim().equalsIgnoreCase("debit")) {
//                                        if (accountsamountArr[i].trim().length() > 0) {
//                                            voucherdetailsObj.setDebit(new BigDecimal(accountsamountArr[i].trim()));
//                                        } else {
//                                            voucherdetailsObj.setDebit(BigDecimal.ZERO);
//                                        }
//                                    }
//
//                                    if (accountsoptionArr[i].trim().equalsIgnoreCase("receipt") || accountsoptionArr[i].trim().equalsIgnoreCase("credit") || accountsoptionArr[i].trim().equalsIgnoreCase("adjustment")) {
//                                        if (accountsamountArr[i].trim().length() > 0) {
//                                            voucherdetailsObj.setCredit(new BigDecimal(accountsamountArr[i].trim()));
//                                        } else {
//                                            voucherdetailsObj.setCredit(BigDecimal.ZERO);
//                                        }
//                                    }
//                                    voucherdetailsObj.setCreatedby(LoggedInUser);
//                                    voucherdetailsObj.setCreateddate(getCurrentDate());
//
//                                    voucherDetailsID = voucherDetaislId;
//
//                                    transaction = session.beginTransaction();
//                                    try {
//                                        session.persist(voucherdetailsObj);
//                                        transaction.commit();
//                                    } catch (Exception e) {
//                                        transaction.rollback();
//                                    }
//                                }
//
//                            }
//
//                        }
//                    }
//
//                }
//
//
//                if (transaction.wasCommitted()) {
//                    Receiptpaymentdetails receiptpaymentdetailsnewObj = null;
//
//                    Criteria recPayCrit = session.createCriteria(Receiptpaymentdetails.class);
//                    recPayCrit.add(Restrictions.sqlRestriction("voucher='" + voucherObj.getId() + "'"));
//                    recPayCrit.add(Restrictions.sqlRestriction("region='" + otherregion + "'"));
//                    recPayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
//                    List recPayList = recPayCrit.list();
//                    if (recPayList.size() > 0) {
//                        for (int i = 0; i < recPayList.size(); i++) {
//                            receiptpaymentdetailsnewObj = (Receiptpaymentdetails) recPayList.get(i);
//                            Receiptpaymentdetailshistory receiptpaymentdetailshistoryObj = new Receiptpaymentdetailshistory();
//                            String receiptPaymentDetailsHistoryId;
//                            receiptPaymentDetailsHistoryId = SequenceNumberGenerator.getMaxHistoryReceiptPaymentDetailsid(session, otherregion);
//                            BeanUtils.copyProperties(receiptpaymentdetailshistoryObj, receiptpaymentdetailsnewObj);
//                            receiptpaymentdetailshistoryObj.setId(receiptPaymentDetailsHistoryId);
//                            receiptpaymentdetailshistoryObj.setReceiptpaymentdetails(receiptpaymentdetailsnewObj);
//                            transaction = session.beginTransaction();
//                            try {
//                                session.save(receiptpaymentdetailshistoryObj);
//                                transaction.commit();
//                            } catch (Exception e) {
//                                transaction.rollback();
//                            }
//                        }
//                    }
//                    if (transaction.wasCommitted()) {
//                        transaction = session.beginTransaction();
//                        try {
//                            session.createSQLQuery("UPDATE receiptpaymentdetails  SET cancelled  = true WHERE voucher='" + voucherObj.getId() + "' and region='" + otherregion + "'").executeUpdate();
//                            transaction.commit();
//                        } catch (Exception e) {
//                            transaction.rollback();
//                        }
//                    }
//                    if (transaction.wasCommitted()) {
//                        if (!voucherType.trim().equalsIgnoreCase("J") || (!voucherType.trim().equalsIgnoreCase("P") && !booktype.equalsIgnoreCase("4"))) {
//                            if (partycode.trim().length() > 0) {
//                                for (int i = 0; i < partycodeArr.length; i++) {
//                                    if (!partycodeArr[i].trim().equalsIgnoreCase("undefined")) {
//
//                                        Receiptpaymentdetails receiptpaymentdetailsObj = null;
//                                        Criteria recPayCrit1 = session.createCriteria(Receiptpaymentdetails.class);
//                                        recPayCrit1.add(Restrictions.sqlRestriction("voucher='" + voucherObj.getId() + "'"));
//                                        recPayCrit1.add(Restrictions.sqlRestriction("region='" + otherregion + "'"));
//                                        recPayCrit1.add(Restrictions.sqlRestriction("serialno=" + i));
//
//                                        List recPayList1 = recPayCrit1.list();
//                                        if (recPayList1.size() > 0) {
//                                            receiptpaymentdetailsObj = (Receiptpaymentdetails) recPayList1.get(0);
//
//                                            receiptpaymentdetailsObj.setVoucher(voucherObj);
//                                            receiptpaymentdetailsObj.setRegion(otherregion);
//                                            receiptpaymentdetailsObj.setPaymentmode(CommonUtility.getPaymentmode(session, partypaymentmodeArr[i].trim()));
//                                            receiptpaymentdetailsObj.setSerialno(i);
//                                            if (partyamountArr[i].trim().length() > 0) {
//                                                receiptpaymentdetailsObj.setAmount(new BigDecimal(partyamountArr[i].trim()));
//                                            } else {
//                                                receiptpaymentdetailsObj.setAmount(BigDecimal.ZERO);
//                                            }
//                                            receiptpaymentdetailsObj.setCancelled(Boolean.FALSE);
//                                            receiptpaymentdetailsObj.setPartyledger(CommonUtility.getPartyledger(session, partycodeArr[i].trim()));
//                                            if (partypaymentmodeArr[i].trim() == "1") {
//                                                receiptpaymentdetailsObj.setRefno("");
//                                                receiptpaymentdetailsObj.setBankledger(null);
//                                                receiptpaymentdetailsObj.setChequedate(null);
//                                            } else {
//                                                receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
//                                                Bankledger bnam = CommonUtility.geBankledger(session, bankcodeArr[i].trim());
//                                                if (bnam != null) {
//                                                    receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
//                                                } else {
//                                                    if (!bankcodeArr[i].trim().equalsIgnoreCase("undefined")) {
//                                                        receiptpaymentdetailsObj.setOtherbankname(bankcodeArr[i].trim());
//                                                    }
//                                                }
//                                                receiptpaymentdetailsObj.setChequedate(postgresDate(chequedateArr[i].trim().replace('-', '/')));
//                                            }
//                                            if (!partyaliasArr[i].equalsIgnoreCase("undefined") && !(partyaliasArr[i].toString().trim()).equalsIgnoreCase("")) {
//                                                receiptpaymentdetailsObj.setFavourof(partyaliasArr[i].trim().toUpperCase());
//                                            } else {
//                                                receiptpaymentdetailsObj.setFavourof(CommonUtility.getPartyledger(session, partycodeArr[i].trim()).getPartyname().toUpperCase());
//                                            }
//                                            receiptpaymentdetailsObj.setCreatedby(LoggedInUser);
//                                            receiptpaymentdetailsObj.setCreateddate(getCurrentDate());
//
//                                            transaction = session.beginTransaction();
//                                            try {
//                                                session.update(receiptpaymentdetailsObj);
//                                                transaction.commit();
//                                            } catch (Exception e) {
//                                                transaction.rollback();
//                                            }
//
//                                        } else {
//
//                                            receiptpaymentdetailsObj = new Receiptpaymentdetails();
//                                            String id = SequenceNumberGenerator.getReceiptpaymentdetailsid(session, otherregion);
//                                            receiptpaymentdetailsObj.setId(id);
//                                            receiptpaymentdetailsObj.setRegion(otherregion);
//                                            receiptpaymentdetailsObj.setVoucher(voucherObj);
//                                            receiptpaymentdetailsObj.setSerialno(i);
//                                            receiptpaymentdetailsObj.setPaymentmode(CommonUtility.getPaymentmode(session, partypaymentmodeArr[i].trim()));
//                                            if (partyamountArr[i].trim().length() > 0) {
//                                                receiptpaymentdetailsObj.setAmount(new BigDecimal(partyamountArr[i].trim()));
//                                            } else {
//                                                receiptpaymentdetailsObj.setAmount(BigDecimal.ZERO);
//                                            }
//                                            receiptpaymentdetailsObj.setCancelled(Boolean.FALSE);
//                                            receiptpaymentdetailsObj.setPartyledger(CommonUtility.getPartyledger(session, partycodeArr[i].trim()));
//                                            if (partypaymentmodeArr[i].trim() == "1") {
//                                                receiptpaymentdetailsObj.setRefno("");
//                                                receiptpaymentdetailsObj.setBankledger(null);
//                                                receiptpaymentdetailsObj.setChequedate(null);
//                                            } else {
//                                                receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
//                                                receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
//                                                receiptpaymentdetailsObj.setChequedate(postgresDate(chequedateArr[i].trim().replace('-', '/')));
//                                            }
//                                            if (!partyaliasArr[i].equalsIgnoreCase("undefined") && !(partyaliasArr[i].toString().trim()).equalsIgnoreCase("")) {
//                                                receiptpaymentdetailsObj.setFavourof(partyaliasArr[i].trim().toUpperCase());
//                                            } else {
//                                                receiptpaymentdetailsObj.setFavourof(CommonUtility.getPartyledger(session, partycodeArr[i].trim()).getPartyname().toUpperCase());
//                                            }
//                                            receiptpaymentdetailsObj.setCreatedby(LoggedInUser);
//                                            receiptpaymentdetailsObj.setCreateddate(getCurrentDate());
//
//                                            transaction = session.beginTransaction();
//                                            try {
//                                                session.persist(receiptpaymentdetailsObj);
//                                                transaction.commit();
//                                            } catch (Exception e) {
//                                                transaction.rollback();
//                                            }
//                                        }
//
//
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
//            System.out.println("After voucherid =" + voucherid);
//            resultMap.put("voucherno", voucherid);
//            resultMap.put("booktype", booktype);
//            resultMap.put("voucherdate", voucherdate);
//            resultMap.put("voucherDetailsID", voucherDetailsID);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return resultMap;
//    }
    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLedgerDetailsOLD(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto) {
        Map resultMap = new HashMap();
        double debit = 0;
        double credit = 0;
        try {

            StringBuffer obquery = new StringBuffer();
            String obdebit = "0";
            String obcredit = "0";
            obquery.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
            obquery.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(voucherdatefrom) + "' ");
            obquery.append(" else  v.voucherapproveddate < '" + postgresDate(voucherdatefrom) + "' end and vd.cancelled is false and v.cancelled is false and vd.accountcode='" + ledger + "' ");
            obquery.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");
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
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Voucher No</th>");
            stringBuff.append("<th width=\"10%\">Voucher Date</th>");
            stringBuff.append("<th width=\"55%\">Narration</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                StringBuffer query = new StringBuffer();
                query.append(" select v.id as voucherid, v.voucherdate as voucherdate, v.narration as narration, vd.amount as amount, vd.voucheroption as option from voucherdetails vd ");
                query.append(" left join voucher v on v.id=vd.voucher ");
                query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
                query.append(" and vd.accountcode='" + ledger + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false ");
                query.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id ");
                ledgerquery = session.createSQLQuery(query.toString());
                for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    String voucherid = (String) rows[0].toString();
                    String voucherdate = rows[1].toString();
                    String naration = rows[2].toString();
                    String amount = rows[3].toString();
                    String option = rows[4].toString();

                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + voucherid + "</td>");
                    stringBuff.append("<td >" + voucherdate + "</td>");
                    stringBuff.append("<td >" + CommonUtility.getVoucherNarration(session, voucherid) + "</td>");
                    if (option != null && option.trim().length() > 0) {
                        if (option.trim().equalsIgnoreCase("RECEIPT") || option.trim().equalsIgnoreCase("CREDIT") || option.trim().equalsIgnoreCase("ADJUSTMENT")) {
                            stringBuff.append("<td ></td>");
                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            credit = credit + Double.parseDouble(amount);
                        }
                        if (option.trim().equalsIgnoreCase("PAYMENT") || option.trim().equalsIgnoreCase("DEBIT")) {
                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            debit = debit + Double.parseDouble(amount);
                            stringBuff.append("<td ></td>");
                        }
                    } else {
                        stringBuff.append("<td ></td>");
                        stringBuff.append("<td ></td>");
                    }

                    stringBuff.append("</tr>");
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td td align=\"right\"  >" + new BigDecimal(debit) + "</td>");
            stringBuff.append("<td td align=\"right\" >" + new BigDecimal(credit) + "</td>");
            stringBuff.append("</tr>");
            BigDecimal obdr;
            BigDecimal obcr;
            BigDecimal cbdr;
            BigDecimal cbcr;
            if (!CommonUtility.geAccountsheads(session, ledger).getAccountgroups().getGrpcode().equalsIgnoreCase("30000")) {
                int res = new BigDecimal(obdebit).compareTo(new BigDecimal(obcredit));
                stringBuff.append("<tr >");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td >Opening Balance as on " + voucherdatefrom + "</td>");
                if (res == 1) {
                    obdr = new BigDecimal(obdebit).subtract(new BigDecimal(obcredit));
                    obcr = new BigDecimal("0");
                    stringBuff.append("<td td align=\"right\"  >" + obdr + "</td>");
                    stringBuff.append("<td td align=\"right\" ></td>");
                } else {
                    obcr = new BigDecimal(obcredit).subtract(new BigDecimal(obdebit));
                    obdr = new BigDecimal("0");
                    stringBuff.append("<td td align=\"right\"  ></td>");
                    stringBuff.append("<td td align=\"right\" >" + obcr + "</td>");
                }
                stringBuff.append("</tr>");


                stringBuff.append("<tr >");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td >Balance for the period </td>");

                int res2 = new BigDecimal(debit).compareTo(new BigDecimal(credit));
                if (res2 == 1) {
                    cbdr = new BigDecimal(debit).subtract(new BigDecimal(credit));
                    cbcr = new BigDecimal("0");
                    stringBuff.append("<td td align=\"right\"  >" + cbdr + "</td>");
                    stringBuff.append("<td td align=\"right\" ></td>");
                } else {
                    cbcr = new BigDecimal(credit).subtract(new BigDecimal(debit));
                    cbdr = new BigDecimal("0");
                    stringBuff.append("<td td align=\"right\"  ></td>");
                    stringBuff.append("<td td align=\"right\" >" + cbcr + "</td>");
                }
                stringBuff.append("</tr>");


                BigDecimal totcbdr = obdr.add(cbdr);
                BigDecimal totcbcr = obcr.add(cbcr);

                stringBuff.append("<tr >");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td >Closing Balance as on " + voucherdateto + "</td>");

                res2 = totcbdr.compareTo(totcbcr);
                if (res2 == 1) {
                    cbdr = totcbdr.subtract(totcbcr);
                    stringBuff.append("<td td align=\"right\"  >" + cbdr + "</td>");
                    stringBuff.append("<td td align=\"right\" ></td>");
                } else {
                    cbcr = totcbcr.subtract(totcbdr);
                    stringBuff.append("<td td align=\"right\"  ></td>");
                    stringBuff.append("<td td align=\"right\" >" + cbcr + "</td>");
                }
                stringBuff.append("</tr>");
            } else {
                stringBuff.append("<tr >");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td >Opening Balance as on " + voucherdatefrom + "</td>");
                stringBuff.append("<td td align=\"right\"  >" + obdebit + "</td>");
                stringBuff.append("<td td align=\"right\" >" + obcredit + "</td>");
                stringBuff.append("</tr>");

                stringBuff.append("<tr >");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td >Closing Balance as on " + voucherdateto + "</td>");
                stringBuff.append("<td td align=\"right\"  >" + new BigDecimal(debit).add(new BigDecimal(obdebit)) + "</td>");
                stringBuff.append("<td td align=\"right\" >" + new BigDecimal(credit).add(new BigDecimal(obcredit)) + "</td>");
                stringBuff.append("</tr>");

            }
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            stringBuff.append("<table width=\"100%\" align=\"center\" class=\"tableBorder2\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
            stringBuff.append("<tr class=\"lightrow\">");
            stringBuff.append("<td colspan=\"5\" align=\"center\">");
            stringBuff.append("<input type=\"button\" class=\"submitbu\" id=\"printbut\" value=\"Print\" onclick=\"onprintout();\">");
            stringBuff.append("<input type=\"reset\" class=\"submitbu\" value=\"Reset\">");
            stringBuff.append("</td>");
            stringBuff.append("<td>");
            stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
            stringBuff.append("<div id=\"paybillprintresult\"></div>");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
//            stringBuff.append("<tr class=\"darkrow\">");
//            stringBuff.append("<td colspan=\"6\" align=\"center\">");
//            stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
//            stringBuff.append("<div id=\"paybillprintresult\"></div>");
//            stringBuff.append("</td>");
//            stringBuff.append("</tr>");
            stringBuff.append("</table>");


            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());
            resultMap.put("ledgerdisp", ledger + "-" + CommonUtility.geAccountsheads(session, ledger).getAccname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto, String reporttype) {
        System.out.println("********************** AccountVoucherServiceImpl class getLedgerDetails method is calling ***********************");
        Map resultMap = new HashMap();
        double debit = 0;
        double credit = 0;
        try {

            StringBuffer obquery = new StringBuffer();
            String obdebit = "0";
            String obcredit = "0";
            if ("1".equalsIgnoreCase(reporttype)) {
                obquery.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                obquery.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher ");
                obquery.append(" where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(voucherdatefrom) + "' ");
                obquery.append(" else  v.voucherapproveddate < '" + postgresDate(voucherdatefrom) + "' end and vd.cancelled is false and v.cancelled is false and vd.accountcode='" + ledger + "' ");
                obquery.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");

            } else {
                obquery.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                obquery.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher  ");
                obquery.append(" left join accountgroups ag on '" + ledger + "' between accheadstartingno and accheadendingno ");
                obquery.append(" where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(voucherdatefrom) + "' ");
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
            StringBuilder stringBuff = new StringBuilder();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Voucher No</th>");
            stringBuff.append("<th width=\"10%\">Voucher Date</th>");
            stringBuff.append("<th width=\"55%\">Narration</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                StringBuffer query = new StringBuffer();
//                query.append(" select v.id as voucherid, v.voucherdate as voucherdate, v.narration as narration, vd.amount as amount, vd.voucheroption as option from voucherdetails vd ");
//                query.append(" left join voucher v on v.id=vd.voucher ");
//                query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
//                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
//                query.append(" and vd.accountcode='" + ledger + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false ");
//                query.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id ");
                if ("1".equalsIgnoreCase(reporttype)) {
                    query.append(" select v.id as voucherid, v.voucherdate as voucherdate, v.narration as narration, vd.amount as amount, vd.voucheroption as option from voucherdetails vd ");
                    query.append(" left join voucher v on v.id=vd.voucher ");
                    query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                    query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
                    query.append(" and vd.accountcode='" + ledger + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false ");
                    query.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id ");
                } else {
                    query.append(" select v.id as voucherid, v.voucherdate as voucherdate, v.narration as narration, vd.amount as amount, vd.voucheroption as option from voucherdetails vd ");
                    query.append(" left join voucher v on v.id=vd.voucher ");
                    query.append(" left join accountgroups ag on '" + ledger + "' between accheadstartingno and accheadendingno ");
                    query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                    query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
                    //query.append(" and vd.accountcode='" + ledger + "'   ");
                    query.append(" and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false ");
                    query.append(" group by ag.grpcode, v.id , v.voucherdate , v.narration , vd.amount , vd.voucheroption ");
                    query.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id ");
                }
                ledgerquery = session.createSQLQuery(query.toString());
                //System.out.println("size:::: " +  ledgerquery.list().size() );
                for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    String voucherid = (String) rows[0].toString();
                    String voucherdate = rows[1].toString();
                    String naration = rows[2].toString();
                    String amount = rows[3].toString();
                    String option = rows[4].toString();

                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + voucherid + "</td>");
                    stringBuff.append("<td >" + voucherdate + "</td>");
                    stringBuff.append("<td >" + CommonUtility.getVoucherNarration(session, voucherid) + "</td>");
                    if (option != null && option.trim().length() > 0) {
                        if (option.trim().equalsIgnoreCase("RECEIPT") || option.trim().equalsIgnoreCase("CREDIT") || option.trim().equalsIgnoreCase("ADJUSTMENT")) {
                            stringBuff.append("<td ></td>");
                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            credit = credit + Double.parseDouble(amount);
                        }
                        if (option.trim().equalsIgnoreCase("PAYMENT") || option.trim().equalsIgnoreCase("DEBIT")) {
                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            debit = debit + Double.parseDouble(amount);
                            stringBuff.append("<td ></td>");
                        }
                    } else {
                        stringBuff.append("<td ></td>");
                        stringBuff.append("<td ></td>");
                    }

                    stringBuff.append("</tr>");
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td td align=\"right\"  >" + new BigDecimal(debit) + "</td>");
            stringBuff.append("<td td align=\"right\" >" + new BigDecimal(credit) + "</td>");
            stringBuff.append("</tr>");
            BigDecimal obdr;
            BigDecimal obcr;
            BigDecimal cbdr;
            BigDecimal cbcr;
            if (CommonUtility.geAccountsheads(session, ledger) != null) {
                if (!CommonUtility.geAccountsheads(session, ledger).getAccountgroups().getGrpcode().equalsIgnoreCase("30000")) {
                    int res = new BigDecimal(obdebit).compareTo(new BigDecimal(obcredit));
                    stringBuff.append("<tr >");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td >Opening Balance as on " + voucherdatefrom + "</td>");
                    if (res == 1) {
                        obdr = new BigDecimal(obdebit).subtract(new BigDecimal(obcredit));
                        obcr = new BigDecimal("0");
                        stringBuff.append("<td td align=\"right\"  >" + obdr + "</td>");
                        stringBuff.append("<td td align=\"right\" ></td>");
                    } else {
                        obcr = new BigDecimal(obcredit).subtract(new BigDecimal(obdebit));
                        obdr = new BigDecimal("0");
                        stringBuff.append("<td td align=\"right\"  ></td>");
                        stringBuff.append("<td td align=\"right\" >" + obcr + "</td>");
                    }
                    stringBuff.append("</tr>");


                    stringBuff.append("<tr >");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td >Balance for the period </td>");

                    int res2 = new BigDecimal(debit).compareTo(new BigDecimal(credit));
                    if (res2 == 1) {
                        cbdr = new BigDecimal(debit).subtract(new BigDecimal(credit));
                        cbcr = new BigDecimal("0");
                        stringBuff.append("<td td align=\"right\"  >" + cbdr + "</td>");
                        stringBuff.append("<td td align=\"right\" ></td>");
                    } else {
                        cbcr = new BigDecimal(credit).subtract(new BigDecimal(debit));
                        cbdr = new BigDecimal("0");
                        stringBuff.append("<td td align=\"right\"  ></td>");
                        stringBuff.append("<td td align=\"right\" >" + cbcr + "</td>");
                    }
                    stringBuff.append("</tr>");


                    BigDecimal totcbdr = obdr.add(cbdr);
                    BigDecimal totcbcr = obcr.add(cbcr);

                    stringBuff.append("<tr >");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td >Closing Balance as on " + voucherdateto + "</td>");

                    res2 = totcbdr.compareTo(totcbcr);
                    if (res2 == 1) {
                        cbdr = totcbdr.subtract(totcbcr);
                        stringBuff.append("<td td align=\"right\"  >" + cbdr + "</td>");
                        stringBuff.append("<td td align=\"right\" ></td>");
                    } else {
                        cbcr = totcbcr.subtract(totcbdr);
                        stringBuff.append("<td td align=\"right\"  ></td>");
                        stringBuff.append("<td td align=\"right\" >" + cbcr + "</td>");
                    }
                    stringBuff.append("</tr>");
                } else {
                    stringBuff.append("<tr >");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td >Opening Balance as on " + voucherdatefrom + "</td>");
                    stringBuff.append("<td td align=\"right\"  >" + obdebit + "</td>");
                    stringBuff.append("<td td align=\"right\" >" + obcredit + "</td>");
                    stringBuff.append("</tr>");

                    stringBuff.append("<tr >");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td >Closing Balance as on " + voucherdateto + "</td>");
                    stringBuff.append("<td td align=\"right\"  >" + new BigDecimal(debit).add(new BigDecimal(obdebit)) + "</td>");
                    stringBuff.append("<td td align=\"right\" >" + new BigDecimal(credit).add(new BigDecimal(obcredit)) + "</td>");
                    stringBuff.append("</tr>");

                }
            } else {
                stringBuff.append("<tr >");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td >Opening Balance as on " + voucherdatefrom + "</td>");
                stringBuff.append("<td td align=\"right\"  >" + obdebit + "</td>");
                stringBuff.append("<td td align=\"right\" >" + obcredit + "</td>");
                stringBuff.append("</tr>");

                stringBuff.append("<tr >");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td >Closing Balance as on " + voucherdateto + "</td>");
                stringBuff.append("<td td align=\"right\"  >" + new BigDecimal(debit).add(new BigDecimal(obdebit)) + "</td>");
                stringBuff.append("<td td align=\"right\" >" + new BigDecimal(credit).add(new BigDecimal(obcredit)) + "</td>");
                stringBuff.append("</tr>");

            }
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            stringBuff.append("<table width=\"100%\" align=\"center\" class=\"tableBorder2\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
            stringBuff.append("<tr class=\"lightrow\">");
            stringBuff.append("<td colspan=\"5\" align=\"center\">");
            stringBuff.append("<input type=\"button\" class=\"submitbu\" id=\"printbut\" value=\"Print\" onclick=\"onprintout();\">");
            stringBuff.append("<input type=\"reset\" class=\"submitbu\" value=\"Reset\">");
            stringBuff.append("</td>");
            stringBuff.append("<td>");
            stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
            stringBuff.append("<div id=\"paybillprintresult\"></div>");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
//            stringBuff.append("<tr class=\"darkrow\">");
//            stringBuff.append("<td colspan=\"6\" align=\"center\">");
//            stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
//            stringBuff.append("<div id=\"paybillprintresult\"></div>");
//            stringBuff.append("</td>");
//            stringBuff.append("</tr>");
            stringBuff.append("</table>");


            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());
            resultMap.put("ledgerdisp", ledger + "-" + CommonUtility.geAccountsheads(session, ledger).getAccname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getConsolidatedLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto, String reporttype) {
        System.out.println("********************** AccountVoucherServiceImpl class getLedgerDetails method is calling ***********************");
        Map resultMap = new HashMap();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        SQLQuery regionquery = session.createSQLQuery("select * from regionmaster order by regionname");
        StringBuilder stringBuff = new StringBuilder();
        stringBuff.append("<FONT SIZE=2>");
        stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
        stringBuff.append("<thead>");
        stringBuff.append("<tr>");
        stringBuff.append("<th width=\"10%\">Region Code</th>");
        stringBuff.append("<th width=\"10%\">Region Name</th>");
        stringBuff.append("<th width=\"15%\">Debit</th>");
        stringBuff.append("<th width=\"15%\">Credit</th>");
        stringBuff.append("</tr>");
        stringBuff.append("</thead>");
        stringBuff.append("<tbody>");
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
            stringBuff.append("<tr >");
            stringBuff.append("<td >" + regionid + "</td>");
            stringBuff.append("<td >" + regionname + "</td>");
            stringBuff.append("<td align=\"right\"   >" + regioncurrentperioddebit + "</td>");
            stringBuff.append("<td align=\"right\"   >" + regioncurrentperiodcredit + "</td>");
            stringBuff.append("</tr>");
        }
        stringBuff.append("</tbody>");
        stringBuff.append("<tfoot>");
        stringBuff.append("<tr >");
        stringBuff.append("<td ></td>");
        stringBuff.append("<td ></td>");
        stringBuff.append("<td td align=\"right\"  >" + decimalFormat.format(debit) + "</td>");
        stringBuff.append("<td td align=\"right\" >" + decimalFormat.format(credit) + "</td>");
        stringBuff.append("</tr>");
        stringBuff.append("</tfoot>");
        stringBuff.append("</table>");
        stringBuff.append("</FONT>");

        stringBuff.append("<table width=\"100%\" align=\"center\" class=\"tableBorder2\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
        stringBuff.append("<tr class=\"lightrow\">");
        stringBuff.append("<td colspan=\"5\" align=\"center\">");
        stringBuff.append("<input type=\"button\" class=\"submitbu\" id=\"printbut\" value=\"Print\" onclick=\"onprintout();\">");
        stringBuff.append("<input type=\"reset\" class=\"submitbu\" value=\"Reset\">");
        stringBuff.append("</td>");
        stringBuff.append("<td>");
        stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
        stringBuff.append("<div id=\"paybillprintresult\"></div>");
        stringBuff.append("</td>");
        stringBuff.append("</tr>");
        stringBuff.append("</table>");
        resultMap.put("voucherdetails", stringBuff.toString());
        resultMap.put("ledgerdisp", ledger + "-" + CommonUtility.geAccountsheads(session, ledger).getAccname());
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLedgerDetailsByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto, String reporttype, String selectedRegion) {
        System.out.println("********************** AccountVoucherServiceImpl class getLedgerDetails method is calling ***********************");
        LoggedInRegion = selectedRegion;
        Map resultMap = new HashMap();
        double debit = 0;
        double credit = 0;
        try {

            StringBuffer obquery = new StringBuffer();
            String obdebit = "0";
            String obcredit = "0";
            if ("1".equalsIgnoreCase(reporttype)) {
                obquery.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                obquery.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher ");
                obquery.append(" where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(voucherdatefrom) + "' ");
                obquery.append(" else  v.voucherapproveddate < '" + postgresDate(voucherdatefrom) + "' end and vd.cancelled is false and v.cancelled is false and vd.accountcode='" + ledger + "' ");
                obquery.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");

            } else {
                obquery.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                obquery.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher  ");
                obquery.append(" left join accountgroups ag on '" + ledger + "' between accheadstartingno and accheadendingno ");
                obquery.append(" where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(voucherdatefrom) + "' ");
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
            StringBuilder stringBuff = new StringBuilder();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Voucher No</th>");
            stringBuff.append("<th width=\"10%\">Voucher Date</th>");
            stringBuff.append("<th width=\"55%\">Narration</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                StringBuffer query = new StringBuffer();
//                query.append(" select v.id as voucherid, v.voucherdate as voucherdate, v.narration as narration, vd.amount as amount, vd.voucheroption as option from voucherdetails vd ");
//                query.append(" left join voucher v on v.id=vd.voucher ");
//                query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
//                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
//                query.append(" and vd.accountcode='" + ledger + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false ");
//                query.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id ");
                if ("1".equalsIgnoreCase(reporttype)) {
                    query.append(" select v.id as voucherid, v.voucherdate as voucherdate, v.narration as narration, vd.amount as amount, vd.voucheroption as option from voucherdetails vd ");
                    query.append(" left join voucher v on v.id=vd.voucher ");
                    query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                    query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
                    query.append(" and vd.accountcode='" + ledger + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false ");
                    query.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id ");
                } else {
                    query.append(" select v.id as voucherid, v.voucherdate as voucherdate, v.narration as narration, vd.amount as amount, vd.voucheroption as option from voucherdetails vd ");
                    query.append(" left join voucher v on v.id=vd.voucher ");
                    query.append(" left join accountgroups ag on '" + ledger + "' between accheadstartingno and accheadendingno ");
                    query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                    query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
                    //query.append(" and vd.accountcode='" + ledger + "'   ");
                    query.append(" and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false ");
                    query.append(" group by ag.grpcode, v.id , v.voucherdate , v.narration , vd.amount , vd.voucheroption ");
                    query.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id ");
                }
                ledgerquery = session.createSQLQuery(query.toString());
                //System.out.println("size:::: " +  ledgerquery.list().size() );
                for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    String voucherid = (String) rows[0].toString();
                    String voucherdate = rows[1].toString();
                    String naration = rows[2].toString();
                    String amount = rows[3].toString();
                    String option = rows[4].toString();

                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + voucherid + "</td>");
                    stringBuff.append("<td >" + voucherdate + "</td>");
                    stringBuff.append("<td >" + CommonUtility.getVoucherNarration(session, voucherid) + "</td>");
                    if (option != null && option.trim().length() > 0) {
                        if (option.trim().equalsIgnoreCase("RECEIPT") || option.trim().equalsIgnoreCase("CREDIT") || option.trim().equalsIgnoreCase("ADJUSTMENT")) {
                            stringBuff.append("<td ></td>");
                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            credit = credit + Double.parseDouble(amount);
                        }
                        if (option.trim().equalsIgnoreCase("PAYMENT") || option.trim().equalsIgnoreCase("DEBIT")) {
                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            debit = debit + Double.parseDouble(amount);
                            stringBuff.append("<td ></td>");
                        }
                    } else {
                        stringBuff.append("<td ></td>");
                        stringBuff.append("<td ></td>");
                    }

                    stringBuff.append("</tr>");
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td td align=\"right\"  >" + new BigDecimal(debit) + "</td>");
            stringBuff.append("<td td align=\"right\" >" + new BigDecimal(credit) + "</td>");
            stringBuff.append("</tr>");
            BigDecimal obdr;
            BigDecimal obcr;
            BigDecimal cbdr;
            BigDecimal cbcr;
            if (CommonUtility.geAccountsheads(session, ledger) != null) {
                if (!CommonUtility.geAccountsheads(session, ledger).getAccountgroups().getGrpcode().equalsIgnoreCase("30000")) {
                    int res = new BigDecimal(obdebit).compareTo(new BigDecimal(obcredit));
                    stringBuff.append("<tr >");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td >Opening Balance as on " + voucherdatefrom + "</td>");
                    if (res == 1) {
                        obdr = new BigDecimal(obdebit).subtract(new BigDecimal(obcredit));
                        obcr = new BigDecimal("0");
                        stringBuff.append("<td td align=\"right\"  >" + obdr + "</td>");
                        stringBuff.append("<td td align=\"right\" ></td>");
                    } else {
                        obcr = new BigDecimal(obcredit).subtract(new BigDecimal(obdebit));
                        obdr = new BigDecimal("0");
                        stringBuff.append("<td td align=\"right\"  ></td>");
                        stringBuff.append("<td td align=\"right\" >" + obcr + "</td>");
                    }
                    stringBuff.append("</tr>");


                    stringBuff.append("<tr >");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td >Balance for the period </td>");

                    int res2 = new BigDecimal(debit).compareTo(new BigDecimal(credit));
                    if (res2 == 1) {
                        cbdr = new BigDecimal(debit).subtract(new BigDecimal(credit));
                        cbcr = new BigDecimal("0");
                        stringBuff.append("<td td align=\"right\"  >" + cbdr + "</td>");
                        stringBuff.append("<td td align=\"right\" ></td>");
                    } else {
                        cbcr = new BigDecimal(credit).subtract(new BigDecimal(debit));
                        cbdr = new BigDecimal("0");
                        stringBuff.append("<td td align=\"right\"  ></td>");
                        stringBuff.append("<td td align=\"right\" >" + cbcr + "</td>");
                    }
                    stringBuff.append("</tr>");


                    BigDecimal totcbdr = obdr.add(cbdr);
                    BigDecimal totcbcr = obcr.add(cbcr);

                    stringBuff.append("<tr >");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td >Closing Balance as on " + voucherdateto + "</td>");

                    res2 = totcbdr.compareTo(totcbcr);
                    if (res2 == 1) {
                        cbdr = totcbdr.subtract(totcbcr);
                        stringBuff.append("<td td align=\"right\"  >" + cbdr + "</td>");
                        stringBuff.append("<td td align=\"right\" ></td>");
                    } else {
                        cbcr = totcbcr.subtract(totcbdr);
                        stringBuff.append("<td td align=\"right\"  ></td>");
                        stringBuff.append("<td td align=\"right\" >" + cbcr + "</td>");
                    }
                    stringBuff.append("</tr>");
                } else {
                    stringBuff.append("<tr >");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td >Opening Balance as on " + voucherdatefrom + "</td>");
                    stringBuff.append("<td td align=\"right\"  >" + obdebit + "</td>");
                    stringBuff.append("<td td align=\"right\" >" + obcredit + "</td>");
                    stringBuff.append("</tr>");

                    stringBuff.append("<tr >");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td ></td>");
                    stringBuff.append("<td >Closing Balance as on " + voucherdateto + "</td>");
                    stringBuff.append("<td td align=\"right\"  >" + new BigDecimal(debit).add(new BigDecimal(obdebit)) + "</td>");
                    stringBuff.append("<td td align=\"right\" >" + new BigDecimal(credit).add(new BigDecimal(obcredit)) + "</td>");
                    stringBuff.append("</tr>");

                }
            } else {
                stringBuff.append("<tr >");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td >Opening Balance as on " + voucherdatefrom + "</td>");
                stringBuff.append("<td td align=\"right\"  >" + obdebit + "</td>");
                stringBuff.append("<td td align=\"right\" >" + obcredit + "</td>");
                stringBuff.append("</tr>");

                stringBuff.append("<tr >");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td ></td>");
                stringBuff.append("<td >Closing Balance as on " + voucherdateto + "</td>");
                stringBuff.append("<td td align=\"right\"  >" + new BigDecimal(debit).add(new BigDecimal(obdebit)) + "</td>");
                stringBuff.append("<td td align=\"right\" >" + new BigDecimal(credit).add(new BigDecimal(obcredit)) + "</td>");
                stringBuff.append("</tr>");

            }
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            stringBuff.append("<table width=\"100%\" align=\"center\" class=\"tableBorder2\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
            stringBuff.append("<tr class=\"lightrow\">");
            stringBuff.append("<td colspan=\"5\" align=\"center\">");
            stringBuff.append("<input type=\"button\" class=\"submitbu\" id=\"printbut\" value=\"Print\" onclick=\"onprintout();\">");
            stringBuff.append("<input type=\"reset\" class=\"submitbu\" value=\"Reset\">");
            stringBuff.append("</td>");
            stringBuff.append("<td>");
            stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
            stringBuff.append("<div id=\"paybillprintresult\"></div>");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
//            stringBuff.append("<tr class=\"darkrow\">");
//            stringBuff.append("<td colspan=\"6\" align=\"center\">");
//            stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
//            stringBuff.append("<div id=\"paybillprintresult\"></div>");
//            stringBuff.append("</td>");
//            stringBuff.append("</tr>");
            stringBuff.append("</table>");


            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());
            resultMap.put("ledgerdisp", ledger + "-" + CommonUtility.geAccountsheads(session, ledger).getAccname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getIRSLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto) {
        Map resultMap = new HashMap();
        System.out.println("ledger " + ledger);
        try {
            double debit = 0;
            double credit = 0;
            StringBuffer obquery = new StringBuffer();
            String obdebit = "0";
            String obcredit = "0";
            obquery.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
            obquery.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(voucherdatefrom) + "' ");
            obquery.append(" else  v.voucherapproveddate < '" + postgresDate(voucherdatefrom) + "' end and vd.cancelled is false and v.cancelled is false and vd.accountcode='" + ledger + "' ");
            obquery.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");
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
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" >");
            stringBuff.append("<tr>");
            stringBuff.append("<td width=\"50%\">");
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable1\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Select</th>");
            stringBuff.append("<th width=\"10%\">Voucher No</th>");
            stringBuff.append("<th width=\"10%\">Voucher Date</th>");
            stringBuff.append("<th width=\"55%\">Narration</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                StringBuffer query = new StringBuffer();
                query.append(" select v.id as voucherid, v.voucherdate as voucherdate, v.narration as narration, vd.amount as amount, vd.voucheroption as option, vd.id as voucherdetailsid from voucherdetails vd ");
                query.append(" left join voucher v on v.id=vd.voucher ");
                query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
                query.append(" and vd.accountcode='" + ledger + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false ");
                query.append(" and vd.id not in ((SELECT voucherdetailssource   as id  FROM interregionaccountsreconcil where cancelled is false and  voucherdetailssource is not null) union (SELECT voucherdetailsdestination as id  FROM interregionaccountsreconcil  where cancelled is false and voucherdetailsdestination is not null))   ");
                query.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id ");
                ledgerquery = session.createSQLQuery(query.toString());
                for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    String voucherid = (String) rows[0].toString();
                    String voucherdate = rows[1].toString();
                    String naration = rows[2].toString();
                    String amount = rows[3].toString();
                    String option = rows[4].toString();
                    String voucherdetailsid = rows[5].toString();
                    stringBuff.append("<tr >");
                    stringBuff.append("<th width=\"10%\"><input type=\"checkbox\" name=\"sourcevoucher\" id=\"sourcevoucher" + voucherdetailsid + "\" value=\"" + voucherdetailsid + "\" /></th>");
                    stringBuff.append("<td >" + voucherid + "</td>");
                    stringBuff.append("<td >" + voucherdate + "</td>");
                    stringBuff.append("<td >" + CommonUtility.getVoucherNarration(session, voucherid) + "</td>");
                    if (option != null && option.trim().length() > 0) {
                        if (option.trim().equalsIgnoreCase("RECEIPT") || option.trim().equalsIgnoreCase("CREDIT") || option.trim().equalsIgnoreCase("ADJUSTMENT")) {
                            stringBuff.append("<td ></td>");
                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            credit = credit + Double.parseDouble(amount);
                        }
                        if (option.trim().equalsIgnoreCase("PAYMENT") || option.trim().equalsIgnoreCase("DEBIT")) {
                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            debit = debit + Double.parseDouble(amount);
                            stringBuff.append("<td ></td>");
                        }
                    } else {
                        stringBuff.append("<td ></td>");
                        stringBuff.append("<td ></td>");
                    }

                    stringBuff.append("</tr>");
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");


            obquery = new StringBuffer();

            obquery.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
            obquery.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + "R24" + "' and v.region='" + "R24" + "'  and   case when v.voucherapproveddate is null then v.voucherdate < '" + postgresDate(voucherdatefrom) + "' ");
            obquery.append(" else  v.voucherapproveddate < '" + postgresDate(voucherdatefrom) + "' end and vd.cancelled is false and v.cancelled is false and vd.accountcode='" + "30001" + "' ");
            obquery.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");
            ledgerquery = session.createSQLQuery(obquery.toString());
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
            stringBuff.append("</td>");
            stringBuff.append("<td width=\"50%\">");
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable2\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Select</th>");
            stringBuff.append("<th width=\"10%\">Voucher No</th>");
            stringBuff.append("<th width=\"10%\">Voucher Date</th>");
            stringBuff.append("<th width=\"55%\">Narration</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                StringBuffer query = new StringBuffer();
                query.append(" select v.id as voucherid, v.voucherdate as voucherdate, v.narration as narration, vd.amount as amount, vd.voucheroption as option, vd.id as voucherdetailsid from voucherdetails vd ");
                query.append(" left join voucher v on v.id=vd.voucher ");
                query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
                query.append(" and vd.accountcode='" + CommonUtility.getRegionIRSCode(session, LoggedInRegion) + "'  and vd.region='" + CommonUtility.getRegionByIRSCode(session, ledger).getId() + "' and v.region='" + CommonUtility.getRegionByIRSCode(session, ledger).getId() + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false ");
                query.append(" and vd.id not in ((SELECT voucherdetailssource   as id  FROM interregionaccountsreconcil where cancelled is false and  voucherdetailssource is not null) union (SELECT voucherdetailsdestination as id  FROM interregionaccountsreconcil  where cancelled is false and voucherdetailsdestination is not null))  ");
                query.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id ");
                //System.out.println(query);
                ledgerquery = session.createSQLQuery(query.toString());
                for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    String voucherid = (String) rows[0].toString();
                    String voucherdate = rows[1].toString();
                    String naration = rows[2].toString();
                    String amount = rows[3].toString();
                    String option = rows[4].toString();
                    String voucherdetailsid = rows[5].toString();
                    stringBuff.append("<tr >");
                    stringBuff.append("<th width=\"10%\"><input type=\"checkbox\" name=\"destvoucher\" id=\"destvoucher" + voucherdetailsid + "\" value=\"" + voucherdetailsid + "\" /></th>");
                    stringBuff.append("<td >" + voucherid + "</td>");
                    stringBuff.append("<td >" + voucherdate + "</td>");
                    stringBuff.append("<td >" + CommonUtility.getVoucherNarration(session, voucherid) + "</td>");
                    if (option != null && option.trim().length() > 0) {
                        if (option.trim().equalsIgnoreCase("RECEIPT") || option.trim().equalsIgnoreCase("CREDIT") || option.trim().equalsIgnoreCase("ADJUSTMENT")) {
                            stringBuff.append("<td ></td>");
                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            credit = credit + Double.parseDouble(amount);
                        }
                        if (option.trim().equalsIgnoreCase("PAYMENT") || option.trim().equalsIgnoreCase("DEBIT")) {
                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            debit = debit + Double.parseDouble(amount);
                            stringBuff.append("<td ></td>");
                        }
                    } else {
                        stringBuff.append("<td ></td>");
                        stringBuff.append("<td ></td>");
                    }

                    stringBuff.append("</tr>");
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
            stringBuff.append("</table>");
            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());
            resultMap.put("ledgerdisp", ledger + "-" + CommonUtility.geAccountsheads(session, ledger).getAccname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getIRSReconciledLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto) {
        Map resultMap = new HashMap();
        try {
            StringBuffer obquery = new StringBuffer();
            obquery.append(" select irs.id, irsr.id from interregionaccountsreconcil irsr ");
            obquery.append(" left join interregionaccounts irs on irs.id=irsr.interregionaccounts ");
            obquery.append(" where ((irsr.voucherdetailssource in  ");
            obquery.append(" (select vd.id as voucherdetailsid from voucherdetails vd  ");
            obquery.append(" left join voucher v on v.id=vd.voucher  ");
            obquery.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "'  else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
            obquery.append(" and vd.accountcode in (select acccode from accountsheads where groupcode='30000')  ");
            obquery.append(" and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false  ");
            obquery.append(" and vd.id in");
            obquery.append(" ((SELECT voucherdetailssource   as id  FROM interregionaccountsreconcil where cancelled is false and  voucherdetailssource is not null) ");
            obquery.append(" union");
            obquery.append(" (SELECT voucherdetailsdestination as id  FROM interregionaccountsreconcil  where cancelled is false and voucherdetailsdestination is not null)) ");
            obquery.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id)) ");
            obquery.append(" or");
            obquery.append(" (irsr.voucherdetailsdestination in ");
            obquery.append(" (select vd.id as voucherdetailsid from voucherdetails vd  ");
            obquery.append(" left join voucher v on v.id=vd.voucher  ");
            obquery.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "'  else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
            obquery.append(" and vd.accountcode in (select acccode from accountsheads where groupcode='30000')  ");
            obquery.append(" and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false  ");
            obquery.append(" and vd.id in ");
            obquery.append(" ((SELECT voucherdetailssource   as id  FROM interregionaccountsreconcil where cancelled is false and  voucherdetailssource is not null)  ");
            obquery.append(" union ");
            obquery.append(" (SELECT voucherdetailsdestination as id  FROM interregionaccountsreconcil  where cancelled is false and voucherdetailsdestination is not null))  ");
            obquery.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id)) ) and irsr.cancelled is false ");
            obquery.append(" order by irs.date ");
            StringBuffer stringBuff = new StringBuffer();
            SQLQuery ledgerquery = session.createSQLQuery(obquery.toString());
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"irsreporttable\" >");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");

            stringBuff.append("<th>");
            stringBuff.append("Ref Id");
            stringBuff.append("</th>");

            stringBuff.append("<th>");
            stringBuff.append("Date");
            stringBuff.append("</th>");

            stringBuff.append("<th>");
            stringBuff.append("Remarks");
            stringBuff.append("</th>");

            stringBuff.append("<th>");
            stringBuff.append("Debit");
            stringBuff.append("</th>");

            stringBuff.append("<th>");
            stringBuff.append("Credit");
            stringBuff.append("</th>");

            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                String ledgerid = (String) rows[0].toString();
                stringBuff.append("<tr>");

                stringBuff.append("<th>");
                stringBuff.append(ledgerid);
                stringBuff.append("</th>");

                stringBuff.append("<th>");
                stringBuff.append("</th>");

                stringBuff.append("<th>");
                stringBuff.append("</th>");

                stringBuff.append("<th>");
                stringBuff.append("</th>");

                stringBuff.append("<th>");
                stringBuff.append("</th>");

                stringBuff.append("</tr>");

                Interregionaccountsreconcil interregionaccountsreconcilObj = null;
                Criteria intRecAccsCrit = session.createCriteria(Interregionaccountsreconcil.class);
                intRecAccsCrit.add(Restrictions.sqlRestriction("interregionaccounts='" + ledgerid + "'"));
                intRecAccsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List intRecAccsList = intRecAccsCrit.list();
                if (intRecAccsList.size() > 0) {
                    for (int i = 0; i < intRecAccsList.size(); i++) {//                        
                        interregionaccountsreconcilObj = (Interregionaccountsreconcil) intRecAccsList.get(i);
                        if (interregionaccountsreconcilObj.getVoucherdetailsByVoucherdetailssource() != null) {
                            Voucherdetails voucherdetailsObj = null;
                            Criteria voucherdetailsCrit = session.createCriteria(Voucherdetails.class);
                            voucherdetailsCrit.add(Restrictions.sqlRestriction("id='" + interregionaccountsreconcilObj.getVoucherdetailsByVoucherdetailssource().getId() + "'"));
                            List voucherdetailsList = voucherdetailsCrit.list();
                            if (voucherdetailsList.size() > 0) {
                                voucherdetailsObj = (Voucherdetails) voucherdetailsList.get(0);
                                stringBuff.append("<tr>");

                                stringBuff.append("<td>");
                                stringBuff.append(voucherdetailsObj.getId());
                                stringBuff.append("</td>");

                                stringBuff.append("<td>");
                                stringBuff.append(voucherdetailsObj.getVoucher().getVoucherdate());
                                stringBuff.append("</td>");

                                stringBuff.append("<td>");
                                stringBuff.append(voucherdetailsObj.getVoucher().getNarration());
                                stringBuff.append("</td>");

                                if (voucherdetailsObj.getVoucheroption() != null && voucherdetailsObj.getVoucheroption().trim().length() > 0) {
                                    if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("RECEIPT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("CREDIT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("ADJUSTMENT")) {
                                        stringBuff.append("<td ></td>");
                                        stringBuff.append("<td align=\"right\" >" + voucherdetailsObj.getAmount() + "</td>");
                                    }
                                    if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("PAYMENT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("DEBIT")) {
                                        stringBuff.append("<td align=\"right\" >" + voucherdetailsObj.getAmount() + "</td>");
                                        stringBuff.append("<td ></td>");
                                    }
                                } else {
                                    stringBuff.append("<td ></td>");
                                    stringBuff.append("<td ></td>");
                                }
                                stringBuff.append("</tr>");
                            }
                        }

                        if (interregionaccountsreconcilObj.getVoucherdetailsByVoucherdetailsdestination() != null) {
                            Voucherdetails voucherdetailsObj = null;
                            Criteria voucherdetailsCrit = session.createCriteria(Voucherdetails.class);
                            voucherdetailsCrit.add(Restrictions.sqlRestriction("id='" + interregionaccountsreconcilObj.getVoucherdetailsByVoucherdetailsdestination().getId() + "'"));
                            List voucherdetailsList = voucherdetailsCrit.list();
                            if (voucherdetailsList.size() > 0) {
                                voucherdetailsObj = (Voucherdetails) voucherdetailsList.get(0);
                                stringBuff.append("<tr>");

                                stringBuff.append("<td>");
                                stringBuff.append(voucherdetailsObj.getId());
                                stringBuff.append("</td>");

                                stringBuff.append("<td>");
                                stringBuff.append(voucherdetailsObj.getVoucher().getVoucherdate());
                                stringBuff.append("</td>");

                                stringBuff.append("<td>");
                                stringBuff.append(voucherdetailsObj.getVoucher().getNarration());
                                stringBuff.append("</td>");

                                if (voucherdetailsObj.getVoucheroption() != null && voucherdetailsObj.getVoucheroption().trim().length() > 0) {
                                    if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("RECEIPT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("CREDIT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("ADJUSTMENT")) {
                                        stringBuff.append("<td ></td>");
                                        stringBuff.append("<td align=\"right\" >" + voucherdetailsObj.getAmount() + "</td>");
                                    }
                                    if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("PAYMENT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("DEBIT")) {
                                        stringBuff.append("<td align=\"right\" >" + voucherdetailsObj.getAmount() + "</td>");
                                        stringBuff.append("<td ></td>");
                                    }
                                } else {
                                    stringBuff.append("<td ></td>");
                                    stringBuff.append("<td ></td>");
                                }
                                stringBuff.append("</tr>");
                            }
                        }

                    }
                }

            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("<input class=\"submitbu\" id=\"printbut\" value=\"Print\" onclick=\"onprintout();\" type=\"button\">");
            resultMap.put("voucherdetails", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getIRSReconciledLedgerDetailsExcellWrite(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto, String filePath) {
        Map map = new HashMap();
        try {
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet();
            Row row = sheet.createRow(0);
            row.setHeight((short) 500);
            HSSFCell cell = (HSSFCell) row.createCell((short) 0);
            Font font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            CellStyle style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("Tamil Nadu Civil Supplies Corporation, " + "");
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

            row = sheet.createRow(1);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);
            //cell.setCellValue("Service Register Report From " + fromdate + " To " + todate);
            cell.setCellValue("IRS Reconciliation " + LoggedInRegion + " vs " + ledger);
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(2);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "Ref No");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "Voucher No");
            sheet.setColumnWidth(1, (short) (4500));

            createHeader(wb, row, (short) 2, "Date");
            sheet.setColumnWidth(2, (short) (4500));

            createHeader(wb, row, (short) 3, "Remarks");
            sheet.setColumnWidth(3, (short) (2500));

            createHeader(wb, row, (short) 4, "Debit");
            sheet.setColumnWidth(4, (short) (7500));

            createHeader(wb, row, (short) 5, "Credit");
            sheet.setColumnWidth(5, (short) (3500));

//            createHeader(wb, row, (short) 6, "Remarks");
//            sheet.setColumnWidth(6, (short) (3500));
//
//            createHeader(wb, row, (short) 7, "Date");
//            sheet.setColumnWidth(7, (short) (3500));
//
//            createHeader(wb, row, (short) 8, "Vocher No");
//            sheet.setColumnWidth(8, (short) (3500));

            int j = 3;
            StringBuffer obquery = new StringBuffer();
            obquery.append(" select irs.id, irsr.id from interregionaccountsreconcil irsr ");
            obquery.append(" left join interregionaccounts irs on irs.id=irsr.interregionaccounts ");
            obquery.append(" where ((irsr.voucherdetailssource in  ");
            obquery.append(" (select vd.id as voucherdetailsid from voucherdetails vd  ");
            obquery.append(" left join voucher v on v.id=vd.voucher  ");
            obquery.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "'  else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
            obquery.append(" and vd.accountcode in (select acccode from accountsheads where groupcode='30000')  ");
            obquery.append(" and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false  ");
            obquery.append(" and vd.id in");
            obquery.append(" ((SELECT voucherdetailssource   as id  FROM interregionaccountsreconcil where cancelled is false and  voucherdetailssource is not null) ");
            obquery.append(" union");
            obquery.append(" (SELECT voucherdetailsdestination as id  FROM interregionaccountsreconcil  where cancelled is false and voucherdetailsdestination is not null)) ");
            obquery.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id)) ");
            obquery.append(" or");
            obquery.append(" (irsr.voucherdetailsdestination in ");
            obquery.append(" (select vd.id as voucherdetailsid from voucherdetails vd  ");
            obquery.append(" left join voucher v on v.id=vd.voucher  ");
            obquery.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "'  else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
            obquery.append(" and vd.accountcode in (select acccode from accountsheads where groupcode='30000')  ");
            obquery.append(" and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false and v.cancelled is false  ");
            obquery.append(" and vd.id in ");
            obquery.append(" ((SELECT voucherdetailssource   as id  FROM interregionaccountsreconcil where cancelled is false and  voucherdetailssource is not null)  ");
            obquery.append(" union ");
            obquery.append(" (SELECT voucherdetailsdestination as id  FROM interregionaccountsreconcil  where cancelled is false and voucherdetailsdestination is not null))  ");
            obquery.append(" order by v.voucherapproveddate,v.voucherno,v.voucherdate,v.id)) ) and irsr.cancelled is false ");
            obquery.append(" order by irs.date ");

            SQLQuery ledgerquery = session.createSQLQuery(obquery.toString());
            for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                String ledgerid = (String) rows[0].toString();

                row = sheet.createRow(j);
                j++;
                row.setHeight((short) 600);

                createContent(wb, row, (short) 0, ledgerid, CellStyle.ALIGN_RIGHT);
                sheet.setColumnWidth(0, (short) (1500));

                Interregionaccountsreconcil interregionaccountsreconcilObj = null;
                Criteria intRecAccsCrit = session.createCriteria(Interregionaccountsreconcil.class);
                intRecAccsCrit.add(Restrictions.sqlRestriction("interregionaccounts='" + ledgerid + "'"));
                intRecAccsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List intRecAccsList = intRecAccsCrit.list();
                if (intRecAccsList.size() > 0) {
                    for (int i = 0; i < intRecAccsList.size(); i++) {//                        
                        interregionaccountsreconcilObj = (Interregionaccountsreconcil) intRecAccsList.get(i);
                        if (interregionaccountsreconcilObj.getVoucherdetailsByVoucherdetailssource() != null) {
                            Voucherdetails voucherdetailsObj = null;
                            Criteria voucherdetailsCrit = session.createCriteria(Voucherdetails.class);
                            voucherdetailsCrit.add(Restrictions.sqlRestriction("id='" + interregionaccountsreconcilObj.getVoucherdetailsByVoucherdetailssource().getId() + "'"));
                            List voucherdetailsList = voucherdetailsCrit.list();
                            if (voucherdetailsList.size() > 0) {
                                voucherdetailsObj = (Voucherdetails) voucherdetailsList.get(0);

                                row = sheet.createRow(j);
                                j++;
                                row.setHeight((short) 600);

                                createContent(wb, row, (short) 1, voucherdetailsObj.getId(), CellStyle.ALIGN_RIGHT);
                                sheet.setColumnWidth(0, (short) (1500));

                                createContent(wb, row, (short) 2, voucherdetailsObj.getVoucher().getVoucherdate().toString(), CellStyle.ALIGN_RIGHT);
                                sheet.setColumnWidth(0, (short) (1500));

                                createContent(wb, row, (short) 3, voucherdetailsObj.getVoucher().getNarration(), CellStyle.ALIGN_RIGHT);
                                sheet.setColumnWidth(0, (short) (1500));



                                if (voucherdetailsObj.getVoucheroption() != null && voucherdetailsObj.getVoucheroption().trim().length() > 0) {
                                    if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("RECEIPT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("CREDIT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("ADJUSTMENT")) {
                                        createContent(wb, row, (short) 5, voucherdetailsObj.getAmount().toString(), CellStyle.ALIGN_RIGHT);
                                        sheet.setColumnWidth(0, (short) (1500));
                                    }
                                    if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("PAYMENT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("DEBIT")) {
                                        createContent(wb, row, (short) 4, voucherdetailsObj.getAmount().toString(), CellStyle.ALIGN_RIGHT);
                                        sheet.setColumnWidth(0, (short) (1500));
                                    }
                                }

                            }
                        }

                        if (interregionaccountsreconcilObj.getVoucherdetailsByVoucherdetailsdestination() != null) {
                            Voucherdetails voucherdetailsObj = null;
                            Criteria voucherdetailsCrit = session.createCriteria(Voucherdetails.class);
                            voucherdetailsCrit.add(Restrictions.sqlRestriction("id='" + interregionaccountsreconcilObj.getVoucherdetailsByVoucherdetailsdestination().getId() + "'"));
                            List voucherdetailsList = voucherdetailsCrit.list();
                            if (voucherdetailsList.size() > 0) {
                                voucherdetailsObj = (Voucherdetails) voucherdetailsList.get(0);

                                row = sheet.createRow(j);
                                j++;
                                row.setHeight((short) 600);

                                createContent(wb, row, (short) 1, voucherdetailsObj.getId(), CellStyle.ALIGN_RIGHT);
                                sheet.setColumnWidth(0, (short) (1500));

                                createContent(wb, row, (short) 2, voucherdetailsObj.getVoucher().getVoucherdate().toString(), CellStyle.ALIGN_RIGHT);
                                sheet.setColumnWidth(0, (short) (1500));

                                createContent(wb, row, (short) 3, voucherdetailsObj.getVoucher().getNarration(), CellStyle.ALIGN_RIGHT);
                                sheet.setColumnWidth(0, (short) (1500));



                                if (voucherdetailsObj.getVoucheroption() != null && voucherdetailsObj.getVoucheroption().trim().length() > 0) {
                                    if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("RECEIPT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("CREDIT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("ADJUSTMENT")) {
                                        createContent(wb, row, (short) 5, voucherdetailsObj.getAmount().toString(), CellStyle.ALIGN_RIGHT);
                                        sheet.setColumnWidth(0, (short) (1500));
                                    }
                                    if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("PAYMENT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("DEBIT")) {
                                        createContent(wb, row, (short) 4, voucherdetailsObj.getAmount().toString(), CellStyle.ALIGN_RIGHT);
                                        sheet.setColumnWidth(0, (short) (1500));
                                    }
                                }
                            }
                        }

                    }
                }

            }

            FileOutputStream fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    private void createContent(Workbook workbook, Row row, short column, String cellValue, short cellalign) {
        Cell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        Font font = workbook.createFont();
        font.setFontName("Arial");
        //font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    private void createHeader(Workbook workbook, Row row, short column, String cellValue) {
        Cell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map AdjustmentDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String chkarray, String chkarray1) {
        Map resultMap = new HashMap();
        try {
            String[] sourceVoucherDetails = chkarray.split("sai*-*sri*&*sai*-*sri");
            String[] destVoucherDetails = chkarray1.split("sai*-*sri*&*sai*-*sri");
            System.out.println(" sourceVoucherDetails " + chkarray);
            System.out.println(" destVoucherDetails " + chkarray1);
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" >");
            stringBuff.append("<tr>");
            stringBuff.append("<td width=\"50%\">");
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"adjtable1\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Voucher No</th>");
            stringBuff.append("<th width=\"10%\">Voucher Date</th>");
            stringBuff.append("<th width=\"55%\">Narration</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            float credit = 0;
            float debit = 0;
            float credit1 = 0;
            float debit1 = 0;
            for (int i = 0; i < sourceVoucherDetails.length; i++) {
                //System.out.println("Source Id" + sourceVoucherDetails[i]);
                Voucherdetails voucherdetailsObj = getVocherDeatils(session, sourceVoucherDetails[i]);
                if (voucherdetailsObj != null) {
                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + voucherdetailsObj.getVoucher().getId() + "</td>");
                    if (voucherdetailsObj.getVoucher() != null) {
                        stringBuff.append("<td >" + voucherdetailsObj.getVoucher().getVoucherdate() + "</td>");
                    } else {
                        stringBuff.append("<td ></td>");
                    }
                    stringBuff.append("<td >" + CommonUtility.getVoucherNarration(session, voucherdetailsObj.getVoucher().getId()) + "</td>");
                    if (voucherdetailsObj.getVoucheroption() != null && voucherdetailsObj.getVoucheroption().trim().length() > 0) {
                        if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("RECEIPT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("CREDIT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("ADJUSTMENT")) {
                            stringBuff.append("<td ></td>");
                            stringBuff.append("<td align=\"right\" >" + voucherdetailsObj.getAmount() + "</td>");
                            credit = credit + voucherdetailsObj.getAmount().floatValue();
                        }
                        if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("PAYMENT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("DEBIT")) {
                            stringBuff.append("<td align=\"right\" >" + voucherdetailsObj.getAmount() + "</td>");
                            debit = debit + voucherdetailsObj.getAmount().floatValue();
                            stringBuff.append("<td ></td>");
                        }
                    } else {
                        stringBuff.append("<td ></td>");
                        stringBuff.append("<td ></td>");
                    }

                    stringBuff.append("</tr>");
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");
            stringBuff.append("</td>");

            stringBuff.append("<td width=\"50%\">");
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"adjtable2\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Voucher No</th>");
            stringBuff.append("<th width=\"10%\">Voucher Date</th>");
            stringBuff.append("<th width=\"55%\">Narration</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            for (int i = 0; i < destVoucherDetails.length; i++) {
                //System.out.println("Dest Id" + destVoucherDetails[i]);
                Voucherdetails voucherdetailsObj = getVocherDeatils(session, destVoucherDetails[i]);
                if (voucherdetailsObj != null) {
                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + voucherdetailsObj.getVoucher().getId() + "</td>");
                    stringBuff.append("<td >" + voucherdetailsObj.getVoucher().getVoucherdate() + "</td>");
                    stringBuff.append("<td >" + CommonUtility.getVoucherNarration(session, voucherdetailsObj.getVoucher().getId()) + "</td>");
                    if (voucherdetailsObj.getVoucheroption() != null && voucherdetailsObj.getVoucheroption().trim().length() > 0) {
                        if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("RECEIPT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("CREDIT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("ADJUSTMENT")) {
                            stringBuff.append("<td ></td>");
                            stringBuff.append("<td align=\"right\" >" + voucherdetailsObj.getAmount() + "</td>");
                            credit1 = credit1 + voucherdetailsObj.getAmount().floatValue();
                        }
                        if (voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("PAYMENT") || voucherdetailsObj.getVoucheroption().trim().equalsIgnoreCase("DEBIT")) {
                            stringBuff.append("<td align=\"right\" >" + voucherdetailsObj.getAmount() + "</td>");
                            debit1 = debit1 + voucherdetailsObj.getAmount().floatValue();
                            stringBuff.append("<td ></td>");
                        }
                    } else {
                        stringBuff.append("<td ></td>");
                        stringBuff.append("<td ></td>");
                    }

                    stringBuff.append("</tr>");
                }
            }


            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
            stringBuff.append("</table>");
            //System.out.println(debit);
            //System.out.println(debit1);
            //System.out.println(credit);
            //System.out.println(credit1);
            if (debit1 == credit && credit1 == debit) {
                stringBuff.append("<input type=\"button\" class=\"submitbu\" id=\"printbut\" value=\"Apply\" onclick=\"createAdjustment();\">");
            }
            resultMap.put("adjustmentdetails", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map applyAdjustmentDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String chkarray, String chkarray1) {
        Map resultMap = new HashMap();
        String[] sourceVoucherDetails = chkarray.split("sai*-*sri*&*sai*-*sri");
        String[] destVoucherDetails = chkarray1.split("sai*-*sri*&*sai*-*sri");
        Transaction transaction = null;
        Interregionaccounts InterregionaccountsObj = null;
        try {

            //System.out.println("sai1");
            InterregionaccountsObj = new Interregionaccounts();
            InterregionaccountsObj.setId(SequenceNumberGenerator.getMaxSeqNumberInterregionaccounts(session, LoggedInRegion));
            InterregionaccountsObj.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
            transaction = session.beginTransaction();
            session.save(InterregionaccountsObj);
            transaction.commit();
            //System.out.println("sai2");
        } catch (Exception e) {
            //System.out.println("sdfasfds");
            e.printStackTrace();
            transaction.rollback();
        }
        for (int i = 0; i < sourceVoucherDetails.length; i++) {
            //System.out.println("Source Id" + sourceVoucherDetails[i]);
            Voucherdetails voucherdetailsObj = getVocherDeatils(session, sourceVoucherDetails[i]);
            Interregionaccountsreconcil interregionaccountsreconcilObj = null;
            Criteria intRecAccsCrit = session.createCriteria(Interregionaccountsreconcil.class);
            intRecAccsCrit.add(Restrictions.sqlRestriction("interregionaccounts='" + InterregionaccountsObj.getId() + "'"));
            intRecAccsCrit.add(Restrictions.sqlRestriction("voucherdetailssource='" + sourceVoucherDetails[i] + "'"));
            List intRecAccsList = intRecAccsCrit.list();
            if (intRecAccsList.size() > 0) {
                interregionaccountsreconcilObj = (Interregionaccountsreconcil) intRecAccsList.get(0);
                interregionaccountsreconcilObj.setCancelled(Boolean.FALSE);
                interregionaccountsreconcilObj.setInterregionaccounts(InterregionaccountsObj);
                interregionaccountsreconcilObj.setVoucherdetailsByVoucherdetailssource(voucherdetailsObj);
                interregionaccountsreconcilObj.setSourceamount(voucherdetailsObj.getAmount());
                transaction = session.beginTransaction();
                session.merge(interregionaccountsreconcilObj);
                transaction.commit();
            } else {
                interregionaccountsreconcilObj = new Interregionaccountsreconcil();
                interregionaccountsreconcilObj.setId(SequenceNumberGenerator.getMaxSeqNumberInterregionaccountsreconcil(session, LoggedInRegion));
                interregionaccountsreconcilObj.setCancelled(Boolean.FALSE);
                interregionaccountsreconcilObj.setInterregionaccounts(InterregionaccountsObj);
                interregionaccountsreconcilObj.setVoucherdetailsByVoucherdetailssource(voucherdetailsObj);
                interregionaccountsreconcilObj.setSourceamount(voucherdetailsObj.getAmount());
                transaction = session.beginTransaction();
                session.persist(interregionaccountsreconcilObj);
                transaction.commit();
            }

        }

        for (int i = 0; i < destVoucherDetails.length; i++) {
            //System.out.println("Dest Id" + destVoucherDetails[i]);
            Voucherdetails voucherdetailsObj = getVocherDeatils(session, destVoucherDetails[i]);

            Interregionaccountsreconcil interregionaccountsreconcilObj = null;
            Criteria intRecAccsCrit = session.createCriteria(Interregionaccountsreconcil.class);
            intRecAccsCrit.add(Restrictions.sqlRestriction("interregionaccounts='" + InterregionaccountsObj.getId() + "'"));
            intRecAccsCrit.add(Restrictions.sqlRestriction("voucherdetailsdestination='" + destVoucherDetails[i] + "'"));
            List intRecAccsList = intRecAccsCrit.list();
            if (intRecAccsList.size() > 0) {
                interregionaccountsreconcilObj = (Interregionaccountsreconcil) intRecAccsList.get(0);
                interregionaccountsreconcilObj.setCancelled(Boolean.FALSE);
                interregionaccountsreconcilObj.setInterregionaccounts(InterregionaccountsObj);
                interregionaccountsreconcilObj.setVoucherdetailsByVoucherdetailsdestination(voucherdetailsObj);
                interregionaccountsreconcilObj.setDestinationamount(voucherdetailsObj.getAmount());
                transaction = session.beginTransaction();
                session.merge(interregionaccountsreconcilObj);
                transaction.commit();
            } else {
                interregionaccountsreconcilObj = new Interregionaccountsreconcil();
                interregionaccountsreconcilObj.setId(SequenceNumberGenerator.getMaxSeqNumberInterregionaccountsreconcil(session, LoggedInRegion));
                interregionaccountsreconcilObj.setCancelled(Boolean.FALSE);
                interregionaccountsreconcilObj.setInterregionaccounts(InterregionaccountsObj);
                interregionaccountsreconcilObj.setVoucherdetailsByVoucherdetailsdestination(voucherdetailsObj);
                interregionaccountsreconcilObj.setDestinationamount(voucherdetailsObj.getAmount());
                transaction = session.beginTransaction();
                session.persist(interregionaccountsreconcilObj);
                transaction.commit();
            }
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLedgerEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Voucher No</th>");
            stringBuff.append("<th width=\"55%\">Remarks</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("<th width=\"5%\">Modification</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"newvoucher\" id=\"newvoucher\" value=\"Add\"  onclick=\"showVoucherEntryForm();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            resultMap.put("period", accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear());
            resultMap.put("booklist", loadAccountBookDetails(session, request, response, LoggedInRegion, LoggedInUser).get("booklist"));
            resultMap.put("ledgerlist", loadLedgerDetails(session, request, response, LoggedInRegion, LoggedInUser).get("ledgerlist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getIRSLedgerEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Voucher No</th>");
            stringBuff.append("<th width=\"55%\">Remarks</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("<th width=\"5%\">Modification</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"newvoucher\" id=\"newvoucher\" value=\"Add\"  onclick=\"showVoucherEntryForm();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            resultMap.put("period", accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear());
            resultMap.put("booklist", loadAccountBookDetails(session, request, response, LoggedInRegion, LoggedInUser).get("booklist"));
            resultMap.put("ledgerlist", loadIRSLedgerDetails(session, request, response, LoggedInRegion, LoggedInUser).get("ledgerlist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public Map loadIRSLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map bookMap = new LinkedHashMap();
        bookMap.put("0", "--Select--");
        String ledCode = "";
        String ledName = "";


        try {
            Criteria ledCrit = session.createCriteria(Accountsheads.class);
            ledCrit.add(Restrictions.sqlRestriction("groupcode='" + "30000" + "'"));
            ledCrit.addOrder(Order.asc("acccode"));
            ledCrit.addOrder(Order.asc("accname"));
            List<Accountsheads> ledList = ledCrit.list();
            resultMap = new TreeMap();
            for (Accountsheads lbobj : ledList) {

                ledCode = lbobj.getAcccode();
                ledName = lbobj.getAcccode() + " " + lbobj.getAccname();


                bookMap.put(ledCode, ledName);
            }

            resultMap.put(
                    "ledgerlist", bookMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public Map loadLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map bookMap = new LinkedHashMap();
        bookMap.put("0", "--Select--");
        String ledCode = "";
        String ledName = "";


        try {
            Criteria ledCrit = session.createCriteria(Accountsheads.class);
            ledCrit.addOrder(Order.asc("acccode"));
            ledCrit.addOrder(Order.asc("accname"));
            List<Accountsheads> ledList = ledCrit.list();
            resultMap = new TreeMap();
            for (Accountsheads lbobj : ledList) {

                ledCode = lbobj.getAcccode();
                ledName = lbobj.getAcccode() + " " + lbobj.getAccname();


                bookMap.put(ledCode, ledName);
            }

            resultMap.put(
                    "ledgerlist", bookMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getTrailBalanceEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Serial No</th>");
            stringBuff.append("<th width=\"55%\">Ledger</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            resultMap.put("period", accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear());
            resultMap.put("booklist", loadAccountBookDetails(session, request, response, LoggedInRegion, LoggedInUser).get("booklist"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getTrailBalanceDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdatefrom, String voucherdateto) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();
            DecimalFormat df = new DecimalFormat();
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Serial No</th>");
            stringBuff.append("<th width=\"55%\">Ledger</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                StringBuffer query = new StringBuffer();
                int i = 0;
//                query.append(" select vd.accountcode, sum(debit) as debit,sum(credit) as credit ");
//                query.append(" from voucherdetails vd  left join voucher v on v.id=vd.voucher  ");
//                query.append(" where v.voucherdate>='" + postgresDate(voucherdatefrom) + "' and v.voucherdate<='" + postgresDate(voucherdateto) + "' ");
//                query.append(" and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false  group by vd.accountcode ");

//                query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
//                query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and  v.voucherdate>='" + postgresDate(voucherdatefrom) + "' and v.voucherdate<='" + postgresDate(voucherdateto) + "' and vd.cancelled is false and v.cancelled is false ");
//                query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");

                query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end and vd.cancelled is false and v.cancelled is false  ");
                query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode  ");

                System.out.println("Trial Balance " + query);
                SQLQuery ledgerquery = session.createSQLQuery(query.toString());
                for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    String ledgerid = (String) rows[0].toString();
                    String debit = "0";
                    String credit = "0";
                    if (rows[1] != null) {
                        debit = rows[1].toString();
                    }
                    if (rows[2] != null) {
                        credit = rows[2].toString();
                    }
                    i++;
                    if (Float.parseFloat(debit) != Float.parseFloat(credit)) {
                        stringBuff.append("<tr >");
                        stringBuff.append("<td >" + ledgerid + "</td>");
                        stringBuff.append("<td >" + CommonUtility.geAccountsheads(session, ledgerid).getAccname() + "</td>");
                        if (!CommonUtility.geAccountsheads(session, ledgerid).getAccountgroups().getGrpcode().equalsIgnoreCase("30000")) {
                            if (Float.parseFloat(debit) > Float.parseFloat(credit)) {
                                BigDecimal deto = new BigDecimal(debit).subtract(new BigDecimal(credit));
                                stringBuff.append("<td align=\"right\" >" + deto + "</td>");
                                stringBuff.append("<td ></td>");
                            }
                            if (Float.parseFloat(debit) < Float.parseFloat(credit)) {
                                BigDecimal deto = new BigDecimal(credit).subtract(new BigDecimal(debit));
                                stringBuff.append("<td ></td>");
                                stringBuff.append("<td align=\"right\" >" + deto + "</td>");
                            }

                            if (Float.parseFloat(debit) == Float.parseFloat(credit)) {
                                stringBuff.append("<td ></td>");
                                stringBuff.append("<td ></td>");
                            }

                        } else {
                            stringBuff.append("<td align=\"right\" >" + debit + "</td>");
                            stringBuff.append("<td align=\"right\" >" + credit + "</td>");

                        }

                        stringBuff.append("</tr>");
                    }
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + " " + "<input type=\"button\" CLASS=\"submitbu\" name=\"tbprint\" id=\"tbprint\" value=\"Print\"  onclick=\"trialPrint();\"  >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"tbprint\" id=\"tbprint\" value=\"CSV\"  onclick=\"trialBalanceCsvPrepare();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getConsolidatedTrailBalanceDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdatefrom, String voucherdateto) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();
            DecimalFormat df = new DecimalFormat();
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Serial No</th>");
            stringBuff.append("<th width=\"55%\">Ledger</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                StringBuffer query = new StringBuffer();
                int i = 0;
//                query.append(" select vd.accountcode, sum(debit) as debit,sum(credit) as credit ");
//                query.append(" from voucherdetails vd  left join voucher v on v.id=vd.voucher  ");
//                query.append(" where v.voucherdate>='" + postgresDate(voucherdatefrom) + "' and v.voucherdate<='" + postgresDate(voucherdateto) + "' ");
//                query.append(" and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false  group by vd.accountcode ");

//                query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
//                query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and  v.voucherdate>='" + postgresDate(voucherdatefrom) + "' and v.voucherdate<='" + postgresDate(voucherdateto) + "' and vd.cancelled is false and v.cancelled is false ");
//                query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");

                query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end and vd.cancelled is false and v.cancelled is false  ");
                query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode  ");

                System.out.println("Trial Balance " + query);
                SQLQuery ledgerquery = session.createSQLQuery(query.toString());
                for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    String ledgerid = (String) rows[0].toString();
                    String debit = "0";
                    String credit = "0";
                    if (rows[1] != null) {
                        debit = rows[1].toString();
                    }
                    if (rows[2] != null) {
                        credit = rows[2].toString();
                    }
                    i++;
                    if (Float.parseFloat(debit) != Float.parseFloat(credit)) {
                        stringBuff.append("<tr >");
                        stringBuff.append("<td >" + ledgerid + "</td>");
                        stringBuff.append("<td >" + CommonUtility.geAccountsheads(session, ledgerid).getAccname() + "</td>");
                        if (!CommonUtility.geAccountsheads(session, ledgerid).getAccountgroups().getGrpcode().equalsIgnoreCase("30000")) {
                            if (Float.parseFloat(debit) > Float.parseFloat(credit)) {
                                BigDecimal deto = new BigDecimal(debit).subtract(new BigDecimal(credit));
                                stringBuff.append("<td align=\"right\" >" + deto + "</td>");
                                stringBuff.append("<td ></td>");
                            }
                            if (Float.parseFloat(debit) < Float.parseFloat(credit)) {
                                BigDecimal deto = new BigDecimal(credit).subtract(new BigDecimal(debit));
                                stringBuff.append("<td ></td>");
                                stringBuff.append("<td align=\"right\" >" + deto + "</td>");
                            }

                            if (Float.parseFloat(debit) == Float.parseFloat(credit)) {
                                stringBuff.append("<td ></td>");
                                stringBuff.append("<td ></td>");
                            }

                        } else {
                            stringBuff.append("<td align=\"right\" >" + debit + "</td>");
                            stringBuff.append("<td align=\"right\" >" + credit + "</td>");

                        }

                        stringBuff.append("</tr>");
                    }
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + " " + "<input type=\"button\" CLASS=\"submitbu\" name=\"tbprint\" id=\"tbprint\" value=\"Print\"  onclick=\"trialPrint();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getTrailBalanceDetailsByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdatefrom, String voucherdateto, String selectedRegion) {
        LoggedInRegion = selectedRegion;
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();
            DecimalFormat df = new DecimalFormat();
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Serial No</th>");
            stringBuff.append("<th width=\"55%\">Ledger</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                StringBuffer query = new StringBuffer();
                int i = 0;
//                query.append(" select vd.accountcode, sum(debit) as debit,sum(credit) as credit ");
//                query.append(" from voucherdetails vd  left join voucher v on v.id=vd.voucher  ");
//                query.append(" where v.voucherdate>='" + postgresDate(voucherdatefrom) + "' and v.voucherdate<='" + postgresDate(voucherdateto) + "' ");
//                query.append(" and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and vd.cancelled is false  group by vd.accountcode ");

//                query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
//                query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and  v.voucherdate>='" + postgresDate(voucherdatefrom) + "' and v.voucherdate<='" + postgresDate(voucherdateto) + "' and vd.cancelled is false and v.cancelled is false ");
//                query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ");

                query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end and vd.cancelled is false and v.cancelled is false  ");
                query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode  ");

                System.out.println("Trial Balance " + query);
                SQLQuery ledgerquery = session.createSQLQuery(query.toString());
                for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    String ledgerid = (String) rows[0].toString();
                    String debit = "0";
                    String credit = "0";
                    if (rows[1] != null) {
                        debit = rows[1].toString();
                    }
                    if (rows[2] != null) {
                        credit = rows[2].toString();
                    }
                    i++;
                    if (Float.parseFloat(debit) != Float.parseFloat(credit)) {
                        stringBuff.append("<tr >");
                        stringBuff.append("<td >" + ledgerid + "</td>");
                        stringBuff.append("<td >" + CommonUtility.geAccountsheads(session, ledgerid).getAccname() + "</td>");
                        if (!CommonUtility.geAccountsheads(session, ledgerid).getAccountgroups().getGrpcode().equalsIgnoreCase("30000")) {
                            if (Float.parseFloat(debit) > Float.parseFloat(credit)) {
                                BigDecimal deto = new BigDecimal(debit).subtract(new BigDecimal(credit));
                                stringBuff.append("<td align=\"right\" >" + deto + "</td>");
                                stringBuff.append("<td ></td>");
                            }
                            if (Float.parseFloat(debit) < Float.parseFloat(credit)) {
                                BigDecimal deto = new BigDecimal(credit).subtract(new BigDecimal(debit));
                                stringBuff.append("<td ></td>");
                                stringBuff.append("<td align=\"right\" >" + deto + "</td>");
                            }

                            if (Float.parseFloat(debit) == Float.parseFloat(credit)) {
                                stringBuff.append("<td ></td>");
                                stringBuff.append("<td ></td>");
                            }

                        } else {
                            stringBuff.append("<td align=\"right\" >" + debit + "</td>");
                            stringBuff.append("<td align=\"right\" >" + credit + "</td>");

                        }

                        stringBuff.append("</tr>");
                    }
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + " " + "<input type=\"button\" CLASS=\"submitbu\" name=\"tbprint\" id=\"tbprint\" value=\"Print\"  onclick=\"trialPrint();\"  >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"tbprint\" id=\"tbprint\" value=\"\"  onclick=\"trialBalanceCsvPrepare();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getProgressiveTrailBalanceDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdateto) {
        String voucherdatefrom = "";
        Map resultMap = new HashMap();
        try {
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");

            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            voucherdatefrom = "01/04/" + accountingyearObj.getStartyear();
            String startYear = "01/04/" + accountingyearObj.getStartyear();
            String endYear = "31/03/" + accountingyearObj.getEndyear();

            if (DateUtility.DateGreaterThanOrEqual(voucherdatefrom, startYear) && DateUtility.DateLessThanOrEqual(voucherdateto, endYear)) {
                StringBuffer stringBuff = new StringBuffer();
                DecimalFormat df = new DecimalFormat();
                df.setMinimumFractionDigits(2);
                df.setMaximumFractionDigits(2);
                stringBuff.append("<FONT SIZE=2>");
                stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
                stringBuff.append("<thead>");
                stringBuff.append("<tr>");
                stringBuff.append("<th width=\"10%\">Serial No</th>");
                stringBuff.append("<th width=\"55%\">Ledger</th>");
                stringBuff.append("<th width=\"15%\">Debit</th>");
                stringBuff.append("<th width=\"15%\">Credit</th>");
                stringBuff.append("</tr>");
                stringBuff.append("</thead>");
                stringBuff.append("<tbody>");
                if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                    StringBuffer query = new StringBuffer();
                    int i = 0;
                    query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
                    query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                    query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end and vd.cancelled is false and v.cancelled is false  ");
                    query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode  ");

                    //System.out.println(query);
                    SQLQuery ledgerquery = session.createSQLQuery(query.toString());
                    for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                        Object[] rows = (Object[]) its.next();
                        String ledgerid = (String) rows[0].toString();
                        String debit = "0";
                        String credit = "0";
                        if (rows[1] != null) {
                            debit = rows[1].toString();
                        }
                        if (rows[2] != null) {
                            credit = rows[2].toString();
                        }
                        i++;
                        if (Float.parseFloat(debit) != Float.parseFloat(credit)) {
                            stringBuff.append("<tr >");
                            stringBuff.append("<td >" + ledgerid + "</td>");
                            stringBuff.append("<td >" + CommonUtility.geAccountsheads(session, ledgerid).getAccname() + "</td>");
                            if (!CommonUtility.geAccountsheads(session, ledgerid).getAccountgroups().getGrpcode().equalsIgnoreCase("30000")) {
                                if (Float.parseFloat(debit) > Float.parseFloat(credit)) {
                                    BigDecimal deto = new BigDecimal(debit).subtract(new BigDecimal(credit));
                                    stringBuff.append("<td align=\"right\" >" + deto + "</td>");
                                    stringBuff.append("<td ></td>");
                                }
                                if (Float.parseFloat(debit) < Float.parseFloat(credit)) {
                                    BigDecimal deto = new BigDecimal(credit).subtract(new BigDecimal(debit));
                                    stringBuff.append("<td ></td>");
                                    stringBuff.append("<td align=\"right\" >" + deto + "</td>");
                                }

                                if (Float.parseFloat(debit) == Float.parseFloat(credit)) {
                                    stringBuff.append("<td ></td>");
                                    stringBuff.append("<td ></td>");
                                }

                            } else {
                                stringBuff.append("<td align=\"right\" >" + debit + "</td>");
                                stringBuff.append("<td align=\"right\" >" + credit + "</td>");

                            }

                            stringBuff.append("</tr>");
                        }
                    }
                }
                stringBuff.append("</tbody>");
                stringBuff.append("<tfoot>");
                stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"tbprint\" id=\"tbprint\" value=\"Print\"  onclick=\"progressivetrialPrint();\"  >" + "</td>");
                stringBuff.append("</tfoot>");
                stringBuff.append("</table>");
                stringBuff.append("</FONT>");

                resultMap.put("voucherdatefrom", voucherdatefrom);
                resultMap.put("voucherdetails", stringBuff.toString());
            } else {
                resultMap.put("ERROR", "Given Period to is not in selected accouting period");
            }

//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPartyLedgerEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Voucher No</th>");
            stringBuff.append("<th width=\"55%\">Remarks</th>");
            stringBuff.append("<th width=\"15%\">Debit</th>");
            stringBuff.append("<th width=\"15%\">Credit</th>");
            stringBuff.append("<th width=\"5%\">Modification</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"newvoucher\" id=\"newvoucher\" value=\"Add\"  onclick=\"showVoucherEntryForm();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            resultMap.put("period", accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear());
            resultMap.put("booklist", loadAccountBookDetails(session, request, response, LoggedInRegion, LoggedInUser).get("booklist"));
            resultMap.put("ledgerlist", loadPartyLedgerDetails(session, request, response, LoggedInRegion, LoggedInUser).get("ledgerlist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public Map loadPartyLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map bookMap = new LinkedHashMap();
        bookMap.put("0", "--Select--");
        String ledCode = "";
        String ledName = "";


        try {
            Criteria ledCrit = session.createCriteria(Partyledger.class);
            ledCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            ledCrit.addOrder(Order.asc("partyname"));
            List<Partyledger> ledList = ledCrit.list();
            resultMap = new TreeMap();
            for (Partyledger lbobj : ledList) {

                ledCode = lbobj.getCode();
                ledName = lbobj.getPartyname();


                bookMap.put(ledCode, ledName);
            }

            resultMap.put(
                    "ledgerlist", bookMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map createBankChallan(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String checkBoxValues, String dateofchallan, String bank, String remarks, String period) {
        Transaction transaction;
        String[] strArray = null;
//        strArray = checkBoxValues.split(java.util.regex.Pattern.quote("-"));
        strArray = checkBoxValues.split(java.util.regex.Pattern.quote("~"));
//        String challanId = SequenceNumberGenerator.getMaxSeqNumberBankChallan(session, LoggedInRegion);
        String challanId = SequenceNumberGeneratorExt.getMaxSeqNumberBankChallan(session, LoggedInRegion, period);
        transaction = session.beginTransaction();
        try {
            Bankchallan bankchallanObj = new Bankchallan();
            bankchallanObj.setChallandate(postgresDate(dateofchallan));
            bankchallanObj.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
            bankchallanObj.setType(type);
            bankchallanObj.setRemarks(remarks);
            bankchallanObj.setBankledger(CommonUtility.getBankledger(session, bank));
            bankchallanObj.setCancelled(Boolean.FALSE);
            bankchallanObj.setId(challanId);
            session.save(bankchallanObj);
            for (String recptid : strArray) {
                Criteria vouCrit = session.createCriteria(Receiptpaymentdetails.class);
                vouCrit.add(Restrictions.sqlRestriction("id='" + recptid + "'"));
                List vouList = vouCrit.list();
                if (vouList.size() > 0) {
                    Receiptpaymentdetails receiptpaymentdetailsObj = (Receiptpaymentdetails) vouList.get(0);
                    receiptpaymentdetailsObj.setBankchallan(bankchallanObj);
                    session.merge(receiptpaymentdetailsObj);
                }
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return getChallanDet(session, request, response, LoggedInRegion, LoggedInUser, type, dateofchallan);
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveAncillaryDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherdetailsid, String ancillaryid, String calamount, String ancillarycode, String ancillarytaxpercentage, String ancillaryamount) {
        Map resultMap = new HashMap();
        Transaction transaction;

        try {
            Regionmaster regionMasterObj = CommonUtility.getRegion(session, LoggedInRegion);

            String[] ancillarycodeArr = ancillarycode.split("TNCSCSEPATOR");
            String[] ancillarytaxpercentageArr = ancillarytaxpercentage.split("TNCSCSEPATOR");
            String[] ancillaryamountArr = ancillaryamount.split("TNCSCSEPATOR");

            //System.out.println("ancillarycodeArr=====" + ancillarycodeArr);
            transaction = session.beginTransaction();
            try {
                session.createSQLQuery("UPDATE ancillaruadjdetails  SET cancelled  = true WHERE voucherdetailsid='" + voucherdetailsid + "' and region='" + LoggedInRegion + "'").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

            for (int i = 0; i < ancillarycodeArr.length; i++) {
                if (!ancillarycodeArr[i].trim().equalsIgnoreCase("undefined")) {
                    Ancillaruadjdetails ancillarydetailsObj = null;
                    String[] ancillarycodeID = ancillarycodeArr[i].split("-");
                    String ancillaryaccountadjcodeId = ancillarycodeID[0];

                    //System.out.println("ancillarycodeID===" + ancillaryaccountadjcodeId);
                    //System.out.println("voucherdetailsid===" + voucherdetailsid);
                    Criteria ancillarydetailsCrit = session.createCriteria(Ancillaruadjdetails.class);
                    ancillarydetailsCrit.add(Restrictions.sqlRestriction("voucherdetailsid='" + voucherdetailsid + "'"));
                    ancillarydetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
                    ancillarydetailsCrit.add(Restrictions.sqlRestriction("serialno=" + i));

                    List ancillarydetailsList = ancillarydetailsCrit.list();
                    //System.out.println("ancillarydetailsList.size()====" + ancillarydetailsList.size());
                    if (ancillarydetailsList.size() > 0) {
                        ancillarydetailsObj = (Ancillaruadjdetails) ancillarydetailsList.get(0);

                        ancillarydetailsObj.setRegionmaster(regionMasterObj);
                        ancillarydetailsObj.setVoucherdetails(getVocherDeatils(session, voucherdetailsid));
                        //System.out.println(" ancillarycodeArr[i]====" + ancillarycodeArr[i]);


                        //                    ancillarydetailsObj.setAncillaryaccountadjcode(getAncillaryAccountCode(session, ancillarycodeID[0]));
                        ancillarydetailsObj.setAncillaryaccountadjcode(getAncillaryAccountCode(session, ancillaryaccountadjcodeId));
//                            ancillarydetailsObj.setAncillaryaccountadjcode(getAncillaryAccountCode(session, ancillarycodeArr[i].split(" ")[0]));
                        ancillarydetailsObj.setAcgroup(getAncillaryAccountCode(session, ancillaryaccountadjcodeId).getGroup());
//                            ancillarydetailsObj.setAcgroup(getAncillaryAccountCode(session, ancillarycodeID[0]).getGroup());
                        if (calamount.length() > 0) {
                            ancillarydetailsObj.setPaymentamount(new BigDecimal(calamount));
                        } else {
                            ancillarydetailsObj.setPaymentamount(BigDecimal.ZERO);
                        }
                        if (ancillarytaxpercentageArr[i].trim().length() > 0) {
                            ancillarydetailsObj.setTax(new BigDecimal(ancillarytaxpercentageArr[i].trim()));
                        } else {
                            ancillarydetailsObj.setTax(BigDecimal.ZERO);
                        }
                        if (ancillaryamountArr[i].trim().length() > 0) {
                            ancillarydetailsObj.setAmount(new BigDecimal(ancillaryamountArr[i].trim()));
                        } else {
                            ancillarydetailsObj.setAmount(BigDecimal.ZERO);
                        }
                        ancillarydetailsObj.setCancelled(Boolean.FALSE);
                        ancillarydetailsObj.setSerialno(i);
                        ancillarydetailsObj.setCreatedby(LoggedInUser);
                        ancillarydetailsObj.setCreateddate(getCurrentDate());

                        transaction = session.beginTransaction();
                        try {
                            session.update(ancillarydetailsObj);
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }
                    } else {
                        String ancillaryDetaislId = SequenceNumberGenerator.getMaxSeqNumberAncillaryAdjustAmtid(session, LoggedInRegion);
                        ancillarydetailsObj = new Ancillaruadjdetails();
                        ancillarydetailsObj.setId(ancillaryDetaislId);
                        ancillarydetailsObj.setRegionmaster(regionMasterObj);
                        ancillarydetailsObj.setVoucherdetails(getVocherDeatils(session, voucherdetailsid));
                        //System.out.println(" ancillarycodeArr[i]====" + ancillarycodeArr[i]);
//                        String[] ancillarycodeID = ancillarycodeArr[i].split(" ");
//                        System.out.println("ancillarycodeID===" + ancillarycodeID[0]);
                        //                    ancillarydetailsObj.setAncillaryaccountadjcode(getAncillaryAccountCode(session, ancillarycodeID[0]));
                        ancillarydetailsObj.setAncillaryaccountadjcode(getAncillaryAccountCode(session, ancillaryaccountadjcodeId));
                        ancillarydetailsObj.setAcgroup(getAncillaryAccountCode(session, ancillaryaccountadjcodeId).getGroup());
//                        ancillarydetailsObj.setAncillaryaccountadjcode(getAncillaryAccountCode(session, ancillarycodeArr[i].split(" ")[0]));
//                        ancillarydetailsObj.setAcgroup(getAncillaryAccountCode(session, ancillarycodeID[0]).getGroup());
                        if (calamount.length() > 0) {
                            ancillarydetailsObj.setPaymentamount(new BigDecimal(calamount));
                        } else {
                            ancillarydetailsObj.setPaymentamount(BigDecimal.ZERO);
                        }
                        if (ancillarytaxpercentageArr[i].trim().length() > 0) {
                            ancillarydetailsObj.setTax(new BigDecimal(ancillarytaxpercentageArr[i].trim()));
                        } else {
                            ancillarydetailsObj.setTax(BigDecimal.ZERO);
                        }
                        if (ancillaryamountArr[i].trim().length() > 0) {
                            ancillarydetailsObj.setAmount(new BigDecimal(ancillaryamountArr[i].trim()));
                        } else {
                            ancillarydetailsObj.setAmount(BigDecimal.ZERO);
                        }
                        ancillarydetailsObj.setCancelled(Boolean.FALSE);
                        ancillarydetailsObj.setSerialno(i);
                        ancillarydetailsObj.setCreatedby(LoggedInUser);
                        ancillarydetailsObj.setCreateddate(getCurrentDate());

                        transaction = session.beginTransaction();
                        try {
                            session.persist(ancillarydetailsObj);
                            session.flush();
                            session.clear();
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }
                    }
                }
            }
            resultMap.put("Success", "Ancillary Account Successfully Saved");
//            resultMap.put("ancillarydetailsid", "Ancillary Account Successfully Saved");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Ancillary Account Transaction Failed");

        }
        return resultMap;
    }

    public static Ancillaryaccountadjcode getAncillaryAccountCode(Session session, String ancillaryaccountadjID) {
        Ancillaryaccountadjcode ancillaryaccountadjcodeObj = null;
        Criteria ancillaryAccountAdjCrit = session.createCriteria(Ancillaryaccountadjcode.class);
        ancillaryAccountAdjCrit.add(Restrictions.sqlRestriction("id='" + ancillaryaccountadjID + "'"));
        List ancillaryAccountAdjList = ancillaryAccountAdjCrit.list();
        if (ancillaryAccountAdjList.size() > 0) {
            ancillaryaccountadjcodeObj = (Ancillaryaccountadjcode) ancillaryAccountAdjList.get(0);
        }
        return ancillaryaccountadjcodeObj;
    }

    public static Voucherdetails getVocherDeatils(Session session, String voucherdetailsid) {
        Voucherdetails voucherdetailsObj = null;
        Criteria voucherdetailsCrit = session.createCriteria(Voucherdetails.class);
        voucherdetailsCrit.add(Restrictions.sqlRestriction("id='" + voucherdetailsid + "'"));
        List voucherdetailsList = voucherdetailsCrit.list();
        if (voucherdetailsList.size() > 0) {
            voucherdetailsObj = (Voucherdetails) voucherdetailsList.get(0);
        }
        return voucherdetailsObj;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getAncillaryDetailsforModi(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherid) {
        Map resultMap = new HashMap();
        resultMap.put("ancillaryadjdetails", getAncillaryAdjAccounts(session, voucherid));
        return resultMap;
    }

    public Map getAncillaryAdjAccounts(Session session, String voucherid) {
        Map resultMap = new HashMap();
        Ancillaruadjdetails ancillaruadjdetailsObj;
        Voucherdetails voucherdetailsObj;
        String voucherDetailsID = "";
        BigDecimal ancillaryAmount = new BigDecimal(0.00);
        try {

            Criteria accCrit = session.createCriteria(Voucherdetails.class);
            accCrit.add(Restrictions.sqlRestriction("voucher='" + voucherid + "'"));
            accCrit.add(Restrictions.sqlRestriction("cancelled is false"));

            List accList = accCrit.list();
            //System.out.println("size of voucher details" + accList.size());
            if (accList.size() > 0) {
                for (int i = 0; i < accList.size(); i++) {
                    voucherdetailsObj = (Voucherdetails) accList.get(i);
                    voucherDetailsID = voucherdetailsObj.getId();
                    ancillaryAmount = voucherdetailsObj.getAmount();
                }

            }
            if (!voucherDetailsID.equalsIgnoreCase("")) {
                Criteria ancillaruadjdetailsCrit = session.createCriteria(Ancillaruadjdetails.class);
                ancillaruadjdetailsCrit.add(Restrictions.sqlRestriction("voucherdetailsid='" + voucherDetailsID + "'"));
                ancillaruadjdetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));

                List ancillaruadjdetailsList = ancillaruadjdetailsCrit.list();
                //System.out.println("size of ancillary details" + ancillaruadjdetailsList.size());

                if (ancillaruadjdetailsList.size() > 0) {
                    for (int i = 0; i < ancillaruadjdetailsList.size(); i++) {
                        ancillaruadjdetailsObj = (Ancillaruadjdetails) ancillaruadjdetailsList.get(i);
                        resultMap.put(i, ancillaruadjdetailsObj.getAncillaryaccountadjcode().getId() + "  " + ancillaruadjdetailsObj.getAncillaryaccountadjcode().getName());
                        resultMap.put(i + ancillaruadjdetailsList.size(), ancillaruadjdetailsObj.getTax());
                        resultMap.put(i + ancillaruadjdetailsList.size() + ancillaruadjdetailsList.size(), ancillaruadjdetailsObj.getAmount());
                    }
                    resultMap.put("ancillaryadjdetailslength", ancillaruadjdetailsList.size());
                    resultMap.put("voucherDetailsID", voucherDetailsID);
                    resultMap.put("ancillaryAmount", ancillaryAmount);
                } else {
                    resultMap.put("ERROR", "ERROR");
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map loadRegionDetails(Session session) {
        Map resultMap = new HashMap();
        Map regionMap = new LinkedHashMap();
        regionMap.put("0", "--Select--");
        String regionid = "";
        String regionname = "";

        try {
            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.addOrder(Order.asc("id"));
            List<Regionmaster> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Regionmaster lbobj : rgnList) {
                regionid = lbobj.getId();
                regionname = lbobj.getRegionname();
                regionMap.put(regionid, regionname);
            }
            resultMap.put("regionlist", regionMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadLedgerAccountsHeads(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map bookMap = new LinkedHashMap();
        bookMap.put("0", "--Select--");
        String ledCode = "";
        String ledName = "";


        try {
            Criteria ledCrit = session.createCriteria(Accountsheads.class);
            ledCrit.add(Restrictions.sqlRestriction("acccode in('3001','3003','3004','3013','3014','3015','3016','3017','3019','3020','3024','23002','24006','55024')"));
            ledCrit.addOrder(Order.asc("acccode"));
            ledCrit.addOrder(Order.asc("accname"));
            List<Accountsheads> ledList = ledCrit.list();
            resultMap = new TreeMap();
            for (Accountsheads lbobj : ledList) {

                ledCode = lbobj.getAcccode();
                ledName = lbobj.getAcccode() + "-" + lbobj.getAccname();
                bookMap.put(ledCode, ledName);
            }
            resultMap.put("ledgerlist", bookMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPaybillVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String book, String period, String voucherdate, String vouchertype, String month, String year, String paybilltype) {
        System.out.println("***************************** AccountVoucherServiceImpl class getPaybillVoucherDetails method is calling **********************************");
        Map resultMap = new HashMap();
        try {
            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());
            resultMap.put("voucheraccountsdetails", getPayrollVoucherAccounts(session, LoggedInRegion, month, year, paybilltype));
            resultMap.put("voucherdetails", getPayrollVoucherDetails(session, month, year, voucherdate, paybilltype));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getPayrollVoucherCategory(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        System.out.println("************************ AccountVoucherServiceImpl class getPayrollVoucherCategory method is calling ***********************");
        StringBuilder str = new StringBuilder();
        try {
            Criteria vouCrit = session.createCriteria(Payrollvouchercategory.class);
            vouCrit.addOrder(Order.asc("id"));
            List vouList = vouCrit.list();
            str.append("<select ");
            str.append("class=\"combobox\" ");
            str.append("name=\"paybilltype\" id=\"paybilltype\">");
            for (int i = 0; i < vouList.size(); i++) {
                Payrollvouchercategory prvc = (Payrollvouchercategory) vouList.get(i);
                str.append("<option value=\"");
                str.append(String.valueOf(prvc.getId()));
                str.append("\">");
                str.append(prvc.getCategory());
                str.append("</option>");
            }
            str.append("</select>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str.toString();
    }

    public Map getPayrollVoucherAccounts(Session session, String LoggedInRegion, String month, String year, String paybilltype) {
        Map resultMap = new HashMap();
        Voucherdetails voucherdetailsObj;
        try {
            StringBuilder earningsb = new StringBuilder();

            earningsb.append("select sum(eet.amount) amount, pm.paycodeserial, ah.acccode, ah.accname from employeeearningstransactions eet ");
            earningsb.append("left join paycodemaster pm on pm.paycode=eet.earningmasterid ");
            earningsb.append("left join accountsheads ah on ah.acccode=pm.paycodeserial ");
            earningsb.append("left join payrollprocessingdetails ppd on ppd.id=eet.payrollprocessingdetailsid ");
            earningsb.append("where ");
            earningsb.append("eet.cancelled is false ");
            earningsb.append("and ppd.process is true ");
            earningsb.append("and ppd.month=" + month + " ");
            earningsb.append("and ppd.year=" + year + " ");
            earningsb.append("and ppd.accregion='" + LoggedInRegion + "' ");

            if (paybilltype.equals("2") || paybilltype.equals("4")) {
                earningsb.append("and ppd.paymentmode='B' ");
            } else if (paybilltype.equals("1") || paybilltype.equals("3")) {
                earningsb.append("and ppd.paymentmode='C' ");
            }

            if (paybilltype.equals("3") || paybilltype.equals("4")) {
                earningsb.append("and ppd.section in ('S13', 'S14') ");
            } else if (paybilltype.equals("1") || paybilltype.equals("2")) {
                earningsb.append("and ppd.section not in ('S13', 'S14') ");
            }

            earningsb.append("group by pm.paycodeserial, ah.acccode, ah.accname");

            StringBuilder deductionsb = new StringBuilder();

            deductionsb.append("select sum(edt.amount) amount, pm.paycodeserial, ah.acccode, ah.accname from employeedeductionstransactions edt ");
            deductionsb.append("left join paycodemaster pm on pm.paycode=edt.deductionmasterid ");
            deductionsb.append("left join accountsheads ah on ah.acccode=pm.paycodeserial ");
            deductionsb.append("left join payrollprocessingdetails ppd on ppd.id=edt.payrollprocessingdetailsid ");
            deductionsb.append("where ");
            deductionsb.append("edt.cancelled is false ");
            deductionsb.append("and ppd.process is true ");
            deductionsb.append("and ppd.month=" + month + " ");
            deductionsb.append("and ppd.year=" + year + " ");
            deductionsb.append("and ppd.accregion='" + LoggedInRegion + "' ");
            if (paybilltype.equals("2") || paybilltype.equals("4")) {
                deductionsb.append("and ppd.paymentmode='B' ");
            } else if (paybilltype.equals("1") || paybilltype.equals("3")) {
                deductionsb.append("and ppd.paymentmode='C' ");
            }

            if (paybilltype.equals("3") || paybilltype.equals("4")) {
                deductionsb.append("and ppd.section in ('S13', 'S14') ");
            } else if (paybilltype.equals("1") || paybilltype.equals("2")) {
                deductionsb.append("and ppd.section not in ('S13', 'S14') ");
            }
            deductionsb.append("group by pm.paycodeserial, ah.acccode, ah.accname");

            System.out.println("earningsb.toString() = " + earningsb.toString());
            System.out.println("deductionsb.toString() = " + deductionsb.toString());

            SQLQuery earningsquery = session.createSQLQuery(earningsb.toString());
            SQLQuery deductionquery = session.createSQLQuery(deductionsb.toString());

            int earningrecordsize = earningsquery.list().size();

            int deductionrecordsize = deductionquery.list().size();

            int totalrecords = earningrecordsize + deductionrecordsize;

            int i = 0;
            if (earningrecordsize > 0) {
                for (ListIterator ite = earningsquery.list().listIterator(); ite.hasNext();) {
                    Object[] obj = (Object[]) ite.next();
                    if (obj[1] != null) {
                        BigDecimal bd = (BigDecimal) obj[0];
                        String accountcode = (String) obj[2];
                        String accountname = (String) obj[3];
                        double amount = bd.doubleValue();
                        resultMap.put(i, accountcode + "  " + accountname);
                        resultMap.put(i + totalrecords, bd);
                        resultMap.put(i + totalrecords + totalrecords, "Payment");
                        i++;
                    }
                }
            }

            if (deductionrecordsize > 0) {
                for (ListIterator ite = deductionquery.list().listIterator(); ite.hasNext();) {
                    Object[] obj = (Object[]) ite.next();
                    if (obj[1] != null) {
                        BigDecimal bd = (BigDecimal) obj[0];
                        String accountcode = (String) obj[2];
                        String accountname = (String) obj[3];
                        double amount = bd.doubleValue();
                        resultMap.put(i, accountcode + "  " + accountname);
                        resultMap.put(i + totalrecords, bd);
                        resultMap.put(i + totalrecords + totalrecords, "Adjustment");
                        i++;
                    }
                }
            }

            resultMap.put("voucherdetailslength", totalrecords);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map getPayrollVoucherDetails(Session session, String month, String year, String voucherdate, String paybilltype) {
        Map resultMap = new HashMap();
        Voucher voucherObj;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("PAID TO ");

            if (paybilltype.equals("1") || paybilltype.equals("2")) {
                sb.append("CORPORATION ");
            } else if (paybilltype.equals("3") || paybilltype.equals("4")) {
                sb.append("DEPUTATION ");
            }

            sb.append("STAFF SALARY FOR THE MONTH OF ");
            sb.append(months[Integer.valueOf(month) - 1] + " " + year);
            sb.append(" INDL. WISE ");

            if (paybilltype.equals("1") || paybilltype.equals("3")) {
                sb.append("CHEQUE ");
            } else if (paybilltype.equals("2") || paybilltype.equals("4")) {
                sb.append("CASH ");
            }

            resultMap.put("narration", sb.toString());
            resultMap.put("fileno", "DT " + voucherdate);
            resultMap.put("sanctionedby", "M. BILLS");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map SavePayrollVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String category, String voucherno) {
        Map map = new HashMap();
        Transaction transaction = null;
        try {
            String payrollvoucherid = SequenceNumberGenerator.getMaxPayrollVoucherid(session, LoggedInRegion);
            Payrollvoucher payrollvoucher = new Payrollvoucher();
            payrollvoucher.setId(payrollvoucherid);
            payrollvoucher.setPayrollmonth(Integer.valueOf(month));
            payrollvoucher.setPayrollyear(Integer.valueOf(year));
            Payrollvouchercategory payrollvouchercategory = null;

            Criteria lrCrit = session.createCriteria(Payrollvouchercategory.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + category + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                payrollvouchercategory = (Payrollvouchercategory) ldList.get(0);
            }

            payrollvoucher.setPayrollvouchercategory(payrollvouchercategory);
            payrollvoucher.setVoucherno(voucherno);
            transaction = session.beginTransaction();
            session.save(payrollvoucher);
            transaction.commit();

        } catch (Exception ex) {
            transaction.rollback();
            map.put("ERROR", "Payroll Voucher Details Insertion Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map checkPayrollVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String category) {
        Map map = new HashMap();
        try {
            Payrollvouchercategory payrollvouchercategory = null;
            Payrollvoucher payrollvoucher = null;
            Criteria c = session.createCriteria(Payrollvouchercategory.class);
            c.add(Restrictions.sqlRestriction("id = '" + category + "' "));
            List cateList = c.list();
            if (cateList.size() > 0) {
                payrollvouchercategory = (Payrollvouchercategory) cateList.get(0);
            }

            Criteria criteria = session.createCriteria(Payrollvoucher.class);
            criteria.add(Restrictions.sqlRestriction("payrollmonth = " + Integer.valueOf(month)));
            criteria.add(Restrictions.sqlRestriction("payrollyear = " + Integer.valueOf(year)));
            criteria.add(Restrictions.sqlRestriction("category = " + Integer.valueOf(category)));
            List ldList = criteria.list();
            System.out.println("ldList size = " + ldList.size());
            System.out.println("month = " + month);
            System.out.println("year = " + year);
            System.out.println("category = " + category);
            if (ldList.size() > 0) {
                payrollvoucher = (Payrollvoucher) ldList.get(0);
                String Message = "Voucher Already Prepared for the Given Period \n Voucher No = " + payrollvoucher.getVoucherno();
                map.put("CHECK", Message);
            } else {
                map.put("CHECK", null);
            }
        } catch (Exception ex) {
            map.put("CHECK", "Check Voucher Details Error!");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map prepareTrialBalanceCSV(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountingperiod, String accountingperiodfrom, String accountingperiodto, String filePathwithName) {
        Map map = new HashMap();
        BigDecimal debit;
        BigDecimal credit;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePathwithName);
        } catch (IOException ex) {
            Logger.getLogger(AccountVoucherServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder query = new StringBuilder();
        query.append(" select groupcode, accname,det.* from (");
        query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit   from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + accountingperiod + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(accountingperiodfrom) + "' and '" + postgresDate(accountingperiodto) + "'  else  v.voucherapproveddate between '" + postgresDate(accountingperiodfrom) + "' and '" + postgresDate(accountingperiodto) + "' end and vd.cancelled is false and v.cancelled is false   GROUP BY accountcode,voucheroption ) as saisiva group by accountcode ) as det ");
        query.append(" join accountsheads on acccode=accountcode order by accname ");
        System.out.println("csv query" + query);
        SQLQuery trialbalancequery = session.createSQLQuery(query.toString());
        try {
            for (ListIterator ite = trialbalancequery.list().listIterator(); ite.hasNext();) {
                Object[] obj = (Object[]) ite.next();

                String groupcode = (String) obj[0];
                String accountcode = (String) obj[2];
                String accountname = (String) obj[1];
                if (obj[3] != null) {
                    debit = (BigDecimal) obj[3];
                } else {
                    debit = new BigDecimal("0.00");
                }
                if (obj[4] != null) {
                    credit = (BigDecimal) obj[4];
                } else {
                    credit = new BigDecimal("0.00");
                }

                System.out.println(groupcode);

                fileWriter.append(groupcode);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(accountcode);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(accountname);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(debit.toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(credit.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

            }
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
        System.out.println("12345678");
        return map;
    }
}
