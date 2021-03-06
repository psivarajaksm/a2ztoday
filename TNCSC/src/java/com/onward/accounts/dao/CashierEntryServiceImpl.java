/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.common.DateUtility;
import com.onward.dao.GlobalDBOpenCloseAndUserPrivilages;
import com.onward.dao.SequenceNumberGeneratorExt;
import com.onward.persistence.payroll.Accountingyear;
import com.onward.persistence.payroll.Accountsbooks;
import com.onward.persistence.payroll.Accountsheads;
import com.onward.persistence.payroll.Bankchallan;
import com.onward.persistence.payroll.Bankledger;
import com.onward.persistence.payroll.Designationmaster;
import com.onward.persistence.payroll.Partyledger;
import com.onward.persistence.payroll.Paymentmode;
import com.onward.persistence.payroll.Receiptpaymentdetails;
import com.onward.persistence.payroll.Receiptpaymentdetailspending;
import com.onward.persistence.payroll.Voucher;
import com.onward.persistence.payroll.Voucherdetails;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author root
 */
public class CashierEntryServiceImpl extends OnwardAction implements CashierEntryService {

    private DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherno) {
        Map resultMap = new HashMap();
//        Boolean errorFlag=false;

        try {
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");

            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            String startYear = "01/04/" + accountingyearObj.getStartyear();
            String endYear = "31/03/" + accountingyearObj.getEndyear();

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

            Criteria vouCrit = session.createCriteria(Voucher.class);
            vouCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            vouCrit.add(Restrictions.sqlRestriction("id='" + voucherno + "'"));
            vouCrit.add(Restrictions.sqlRestriction("vouchertype='P'"));
//            vouCrit.add(Restrictions.sqlRestriction("vouchertype='" + vouchertype + "'"));
//            vouCrit.add(Restrictions.sqlRestriction("accountbook='" + book + "'"));
//            vouCrit.add(Restrictions.sqlRestriction("voucherdate='" + postgresDate(voucherdate) + "'"));
//            vouCrit.add(Restrictions.sqlRestriction("accountingperiod='" + period + "'"));
            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));

            List vouList = vouCrit.list();

            if (vouList.size() > 0) {
                for (int i = 0; i < vouList.size(); i++) {
                    Voucher voucherObj = (Voucher) vouList.get(i);
//                System.out.println("=="+DateUtility.DateGreaterThanOrEqual(dateToString(voucherObj.getVoucherdate()),startYear));
//                System.out.println("=less="+DateUtility.DateLessThanOrEqual(dateToString(voucherObj.getVoucherdate()),endYear));
                    if (DateUtility.DateGreaterThanOrEqual(dateToString(voucherObj.getVoucherdate()), startYear) && DateUtility.DateLessThanOrEqual(dateToString(voucherObj.getVoucherdate()), endYear)) {
//                    System.out.println("date is correct accoiuntin period");
                        stringBuff.append("<tr >");
                        stringBuff.append("<td >" + voucherObj.getId() + "</td>");
                        stringBuff.append("<td  >" + voucherObj.getNarration() + "</td>");
                        if (voucherObj.getVoucherno() != null && voucherObj.getVoucherno().trim() != "") {
                            stringBuff.append("<td  align=\"center\">" + voucherObj.getVoucherno() + "</td>");
                        } else {
                            stringBuff.append("<td  align=\"center\">" + "-" + "</td>");
                        }
//                        stringBuff.append("<td >" + "" + "</td>");
                        stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"loancode\" id=\"" + "paycodeObj.getPaycode()" + "\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "')\">" + "</td>");
                        //stringBuff.append("<td >" + "<input type=\"radio\" name=\"modivou\" id=\"modivou\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getAccountingyear().getId() + "','" + voucherObj.getVouchertype() + "','" + voucherObj.getAccountsbooks().getCode() + "');/>" + "</td>");
                        stringBuff.append("</tr>");

                    } else {
                        resultMap.put("ERROR", "Given Voucher Number is not in selected accouting period");
                        break;
//                    System.out.println("date is not correct accoiuntin period");
                    }

                }
            } else {
                resultMap.put("ERROR", "Given Voucher Number is not a Payment Voucher");
            }

            stringBuff.append("</tbody>");
//            stringBuff.append("<tfoot>");
//            stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"newvoucher\" id=\"newvoucher\" value=\"Add\"  onclick=\"showVoucherEntryForm();\"  >" + "</td>");
//            stringBuff.append("</tfoot>");
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
    public Map getVoucherDetailsForModi(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherid) {
        Map resultMap = new HashMap();
        try {
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");

            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            String startYear = "01/04/" + accountingyearObj.getStartyear();
            String endYear = "31/03/" + accountingyearObj.getEndyear();

            Criteria vouCrit = session.createCriteria(Voucher.class);
            vouCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            vouCrit.add(Restrictions.sqlRestriction("id='" + voucherid + "'"));
            vouCrit.add(Restrictions.sqlRestriction("vouchertype='P'"));
            vouCrit.add(Restrictions.sqlRestriction("accountbook!='4'"));
            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));

            List vouList = vouCrit.list();

            if (vouList.size() > 0) {
//                for (int i = 0; i < vouList.size(); i++) {
                Voucher voucherObj = (Voucher) vouList.get(0);
                if (voucherObj.getLocked()) {
                    resultMap.put("ERROR", "Given Payment Voucher is Locked.Contact Adminstrator");
                } else {
                    if (DateUtility.DateGreaterThanOrEqual(dateToString(voucherObj.getVoucherdate()), startYear) && DateUtility.DateLessThanOrEqual(dateToString(voucherObj.getVoucherdate()), endYear)) {
                        resultMap.put("voucheraccountsdetails", getVoucherAccounts(session, voucherid));
                        resultMap.put("voucherdetails", getVoucherDetails(session, voucherid));
                        resultMap.put("voucherpartydetails", getPartyWisePaymentDetails(session, voucherid));
                        resultMap.put("cashbook", voucherObj.getAccountsbooks().getBookname());
                        resultMap.put("vochdate", dateToString(voucherObj.getVoucherdate()));
                    } else {
                        resultMap.put("ERROR", "Given Voucher Number is not in selected accouting period");
//                        break;
//                    System.out.println("date is not correct accoiuntin period");
                    }
                }
//                }
            } else {
                resultMap.put("ERROR", "Given Voucher Number is not a Payment Voucher");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
                resultMap.put("voucherno", voucherObj.getVoucherno());
                resultMap.put("voucherapproveddate", dateToString(voucherObj.getVoucherapproveddate()));
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
            //System.out.println("size of voucher details" + accList.size());
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
            //System.out.println("size of voucher details" + partyList.size());
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
                        resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size(), "");
                    }
                    if (receiptpaymentdetailsObj.getFavourof() != null) {
                        resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size(), receiptpaymentdetailsObj.getFavourof());
                    } else {
                        resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size(), "");
                    }
                    if (receiptpaymentdetailsObj.getChequedate() != null) {
                        resultMap.put(i + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size() + partyList.size(), dateToString(receiptpaymentdetailsObj.getChequedate()).replace('-', '/'));
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
    public Map getAccountsCodeListPartyDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        resultMap.put("accountcodelist", getAccountsCodeList(session));
        resultMap.put("partylist", getPartyList(session, LoggedInRegion));
        resultMap.put("banklist", getBankList(session, LoggedInRegion));
        resultMap.put("paymentmodelist", getPaymentModeList(session));
//        resultMap.put("sactionedbylist", getSactionedByList(session));
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
//            designatinCrit.add(Restrictions.sqlRestriction("designationcode='SE14'"));
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
    public Map saveModifiedVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherType, String voucherid, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String vouchernumber, String voucherdate, String chequedate, String partyalias) {
        Map resultMap = new HashMap();
        Transaction transaction;
        try {
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            String[] partycodeArr = partycode.split("TNCSCSEPATOR");
            String[] partyamountArr = partyamount.split("TNCSCSEPATOR");
            String[] partypaymentmodeArr = partypaymentmode.split("TNCSCSEPATOR");
            String[] refnoArr = refno.split("TNCSCSEPATOR");
            String[] bankcodeArr = bankcode.split("TNCSCSEPATOR");
            String[] chequedateArr = chequedate.split("TNCSCSEPATOR");
            String[] partyaliasArr = partyalias.split("TNCSCSEPATOR");
            Voucher voucherObj = null;
            
            boolean validate = false;

            List reflist = new ArrayList();
            List partylist = new ArrayList();

            for (int i = 0; i < partypaymentmodeArr.length; i++) {
                if (partypaymentmodeArr[i].trim().equals("2")) {
                    if (partycodeArr[i].equals("R017")) {
                        reflist.add(refnoArr[i]);
                        partylist.add(partycodeArr[i]);
                    }
                }
            }

            Set refset = new HashSet(reflist);
            //System.out.println("reflist.size() = " + reflist.size());
            //System.out.println("refset.size() = " + refset.size());

            if (refset.size() < reflist.size()) {
                resultMap.put("ERROR", "Duplicate Cheque No Occurs!");
                return resultMap;
            }

//            for(int i=0;i<partycodeArr.length;i++){
//                System.out.println("partycodeArr["+i+"] = "+partycodeArr[i]);
//            }
//            System.out.println("********************************************");
//            for(int i=0;i<partypaymentmodeArr.length;i++){
//                System.out.println("partypaymentmodeArr["+i+"] = "+partypaymentmodeArr[i]);
//            }
//            System.out.println("********************************************");
//            for (int i = 0; i < refnoArr.length; i++) {
//                System.out.println("refnoArr[" + i + "] = " + refnoArr[i]);
//            }

            Criteria vouCrit = session.createCriteria(Voucher.class);
            vouCrit.add(Restrictions.sqlRestriction("id='" + voucherid + "'"));
            vouCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            List vouList = vouCrit.list();
            if (vouList.size() > 0) {
                voucherObj = (Voucher) vouList.get(0);
                voucherObj.setVoucherno(vouchernumber.toString());
                voucherObj.setVoucherapproveddate(postgresDate(voucherdate));
                voucherObj.setLocked(Boolean.TRUE);
                transaction = session.beginTransaction();
                try {
                    session.update(voucherObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }


            transaction = session.beginTransaction();
            try {
                session.createSQLQuery("UPDATE receiptpaymentdetails  SET cancelled  = true WHERE voucher='" + voucherid + "' and region='" + LoggedInRegion + "'").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

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
                                receiptpaymentdetailsObj.setFavourof(partyaliasArr[i].trim());
                            } else {
                                receiptpaymentdetailsObj.setFavourof(CommonUtility.getPartyledger(session, partycodeArr[i].trim()).getPartyname());
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
                            //String id = SequenceNumberGenerator.getReceiptpaymentdetailsid(session, LoggedInRegion);
                            String id = SequenceNumberGeneratorExt.getReceiptpaymentdetailsid(session, LoggedInRegion, periodcode);
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
                                receiptpaymentdetailsObj.setFavourof(partyaliasArr[i].trim());
                            } else {
                                receiptpaymentdetailsObj.setFavourof(CommonUtility.getPartyledger(session, partycodeArr[i].trim()).getPartyname());
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
//            if (!voucherType.trim().equalsIgnoreCase("J")) {
//                for (int i = 0; i < partycodeArr.length; i++) {
//                    if (!partycodeArr[i].trim().equalsIgnoreCase("undefined")) {
//
//                        Receiptpaymentdetails receiptpaymentdetailsObj = null;
//                        Criteria recPayCrit = session.createCriteria(Receiptpaymentdetails.class);
//                        recPayCrit.add(Restrictions.sqlRestriction("voucher='" + voucherid + "'"));
//                        recPayCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
//                        recPayCrit.add(Restrictions.sqlRestriction("serialno=" + i));
//
//                        List recPayList = recPayCrit.list();
//                        if (recPayList.size() > 0) {
//                            receiptpaymentdetailsObj = (Receiptpaymentdetails) recPayList.get(0);
//
////                            if (partypaymentmodeArr[i].trim() == "1") {
////                                receiptpaymentdetailsObj.setRefno("");
////                                receiptpaymentdetailsObj.setBankledger(null);
////                                receiptpaymentdetailsObj.setChequedate(null);
////                            } else {
//                            receiptpaymentdetailsObj.setRefno(refnoArr[i].trim());
//                            receiptpaymentdetailsObj.setBankledger(CommonUtility.geBankledger(session, bankcodeArr[i].trim()));
//                            receiptpaymentdetailsObj.setChequedate(postgresDate(chequedateArr[i].trim().replace('-', '/')));
////                            }
//
//                            receiptpaymentdetailsObj.setCancelled(Boolean.FALSE);
//
//                            transaction = session.beginTransaction();
//                            try {
//                                session.update(receiptpaymentdetailsObj);
//                                transaction.commit();
//                            } catch (Exception e) {
//                                transaction.rollback();
//                            }
//
//                        }
//
//
//                    }
//                }
//            }
            resultMap.put("voucherno", voucherid);
            resultMap.put("Success", "Given Voucher Details are saved");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Given Voucher Details not Updated ID problem");
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getAccountBook(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Accountsbooks masterObj;
        Map resultMap = new HashMap();

        try {
            Map accountbooklist = new LinkedHashMap();
            accountbooklist.put("0", "--Select--");
            Criteria lrCrit = session.createCriteria(Accountsbooks.class);
//            lrCrit.addOrder(Order.asc("groupname"));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                for (int i = 0; i < ldList.size(); i++) {
                    masterObj = (Accountsbooks) ldList.get(i);
                    accountbooklist.put(masterObj.getCode(), masterObj.getBookname());

                }

            }
            resultMap.put("accountbooklist", accountbooklist);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherAccountsDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accounttype, String accountdate, String accountbooktype) {
        Map resultMap = new HashMap();
        Voucher voucherObj = null;
        Voucherdetails voucherdetailsObj = null;
        BigDecimal payment = new BigDecimal(0.00);
        BigDecimal adjustment = new BigDecimal(0.00);
        String amt = "";


        try {
            StringBuffer stringBuff = new StringBuffer();
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
//            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"voucheraccounttable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Sl.No</th>");
            stringBuff.append("<th width=\"15%\">Voucher No</th>");
            stringBuff.append("<th width=\"45%\">Remarks</th>");
            stringBuff.append("<th width=\"15%\">Amount</th>");
            stringBuff.append("<th width=\"15%\">Select</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria vouCrit = session.createCriteria(Voucher.class);
            vouCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            vouCrit.add(Restrictions.sqlRestriction("vouchertype='" + accounttype + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountbook='" + accountbooktype + "'"));
            vouCrit.add(Restrictions.sqlRestriction("voucherdate='" + postgresDate(accountdate) + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountingperiod='" + periodcode + "'"));
            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));
//            vouCrit.addOrder(Order.asc(Projections.property("cast(substring(id from 5 for 6) as numeric)").toString()));
            vouCrit.addOrder(Order.asc((Projections.property("id").toString().replaceAll(LoggedInRegion + accounttype, ""))));
//            vouCrit.addOrder(Order.asc("replace( id, '"+LoggedInRegion+""+accounttype+"', '') "));
//            vouCrit.addOrder(Order.asc("cast(substring(id from 5 for 6) as numeric)"));

            List vouList = vouCrit.list();
            for (int i = 0; i < vouList.size(); i++) {
                voucherObj = (Voucher) vouList.get(i);


                String SQL_QUERY = "select CASE WHEN coalesce(sum(debit),0) > coalesce(sum(credit),0)"
                        + " THEN coalesce(sum(debit),0) - coalesce(sum(credit),0)"
                        + " ELSE coalesce(sum(credit),0) - coalesce(sum(debit),0) END as amount "
                        + "  from voucherdetails where voucher='" + voucherObj.getId() + "' and region='" + LoggedInRegion + "' and cancelled is false";
                Query query = session.createSQLQuery(SQL_QUERY);
                List amtList = query.list();
                if (amtList.size() > 0 && amtList.get(0) != null) {
                    amt = amtList.get(0).toString();
                }
//                        BigDecimal [] amount = (BigDecimal []) query.uniqueResult();
//                        payment=new BigDecimal((String) amount[0]);

//                        Criteria voudetailsCrit = session.createCriteria(Voucherdetails.class);
//                        voudetailsCrit.add(Restrictions.sqlRestriction("voucher='" + voucherObj.getId() + "'"));
//                        voudetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
//                        voudetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
//                        List voudetailsList = voudetailsCrit.list();
//                        for (int j = 0; j < voudetailsList.size(); j++) {
//                            voucherdetailsObj = (Voucherdetails) voudetailsList.get(j);
//                            if(voucherdetailsObj.getDebit()!=null){
//                                payment=payment.add(voucherdetailsObj.getDebit());
//                            }
//                            if(voucherdetailsObj.getCredit()!=null){
//                                adjustment=adjustment.add(voucherdetailsObj.getCredit());
//                            }
//
//                        }

                stringBuff.append("<tr >");
                stringBuff.append("<td >" + (i + 1) + "</td>");
                stringBuff.append("<td >" + voucherObj.getId() + "</td>");
                stringBuff.append("<td  >" + voucherObj.getNarration() + "</td>");
                stringBuff.append("<td  >" + amt + "</td>");
                stringBuff.append("<td  align=\"center\">" + "<input type=\"checkbox\" name=\"accbookchangeName\" id=\"" + voucherObj.getId() + "\" value=\"" + voucherObj.getId() + "\" >" + "</td>");
//                stringBuff.append("<td  align=\"center\">" + "<input type=\"checkbox\" name=\"accbookchangeName\" id=\"" + voucherObj.getId() + "\" value=\"" + voucherObj.getId() + "\" onclick=\"addChangeAccountBookDetails('" + voucherObj.getId() + "')\">" + "</td>");
                //stringBuff.append("<td >" + "<input type=\"radio\" name=\"modivou\" id=\"modivou\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getAccountingyear().getId() + "','" + voucherObj.getVouchertype() + "','" + voucherObj.getAccountsbooks().getCode() + "');/>" + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"4\" align=\"center\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"savebutton\" id=\"savebutton\" value=\"Save\"  onclick=\"saveAccountBook();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucheraccountdetails", stringBuff.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveAccountBookChange(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountbooktype, String voucherids, String accountdate) {
        Map resultMap = new HashMap();
        String[] strArray = null;
        Transaction transaction;
        try {
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            transaction = session.beginTransaction();
            try {
                String vouchernos = voucherids.substring(0, voucherids.length() - 1);
                vouchernos = vouchernos.replaceAll(",", "','");
//                System.out.println("vouchernos===" + vouchernos);
                session.createSQLQuery("UPDATE voucher  SET accountbook  = '" + accountbooktype + "' WHERE id in ('" + vouchernos + "') and cancelled is false and voucherdate='" + postgresDate(accountdate) + "' and accountingperiod='" + periodcode + "'  and region='" + LoggedInRegion + "'").executeUpdate();
                transaction.commit();
                resultMap.put("success", "Account Book Change  Successfully Saved");
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
                resultMap.put("ERROR", "Account Book Change Transaction Faild");
            }

        } catch (Exception e) {
            resultMap.put("ERROR", "Account Book Change Transaction Faild");
        }

        return resultMap;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherDetailsForCancel(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accounttype, String accountdate, String accountbooktype) {
        Map resultMap = new HashMap();
        Voucher voucherObj = null;
        Voucherdetails voucherdetailsObj = null;
        BigDecimal payment = new BigDecimal(0.00);
        BigDecimal adjustment = new BigDecimal(0.00);
        String amt = "";


        try {
            StringBuffer stringBuff = new StringBuffer();
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
//            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"voucheraccounttable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Sl.No</th>");
            stringBuff.append("<th width=\"15%\">Voucher No</th>");
            stringBuff.append("<th width=\"45%\">Remarks</th>");
            stringBuff.append("<th width=\"15%\">Amount</th>");
            stringBuff.append("<th width=\"15%\">Select</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria vouCrit = session.createCriteria(Voucher.class);
            vouCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            vouCrit.add(Restrictions.sqlRestriction("vouchertype='" + accounttype + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountbook='" + accountbooktype + "'"));
            vouCrit.add(Restrictions.sqlRestriction("voucherdate='" + postgresDate(accountdate) + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountingperiod='" + periodcode + "'"));
            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            vouCrit.addOrder(Order.asc((Projections.property("id").toString().replaceAll(LoggedInRegion + accounttype, ""))));


            List vouList = vouCrit.list();
            for (int i = 0; i < vouList.size(); i++) {
                voucherObj = (Voucher) vouList.get(i);


                String SQL_QUERY = "select CASE WHEN coalesce(sum(debit),0) > coalesce(sum(credit),0)"
                        + " THEN coalesce(sum(debit),0) - coalesce(sum(credit),0)"
                        + " ELSE coalesce(sum(credit),0) - coalesce(sum(debit),0) END as amount "
                        + "  from voucherdetails where voucher='" + voucherObj.getId() + "' and region='" + LoggedInRegion + "' and cancelled is false";
                Query query = session.createSQLQuery(SQL_QUERY);
                List amtList = query.list();
                if (amtList.size() > 0 && amtList.get(0) != null) {
                    amt = amtList.get(0).toString();
                }

                stringBuff.append("<tr >");
                stringBuff.append("<td >" + (i + 1) + "</td>");
                stringBuff.append("<td >" + voucherObj.getId() + "</td>");
                stringBuff.append("<td  >" + voucherObj.getNarration() + "</td>");
                stringBuff.append("<td  >" + amt + "</td>");
                stringBuff.append("<td  align=\"center\">" + "<input type=\"checkbox\" name=\"vouchercancelName\" id=\"" + voucherObj.getId() + "\" value=\"" + voucherObj.getId() + "\" >" + "</td>");
                //stringBuff.append("<td >" + "<input type=\"radio\" name=\"modivou\" id=\"modivou\" onclick=\"showVoucherDetails('" + voucherObj.getId() + "','" + voucherObj.getAccountingyear().getId() + "','" + voucherObj.getVouchertype() + "','" + voucherObj.getAccountsbooks().getCode() + "');/>" + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"4\" align=\"center\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"savebutton\" id=\"savebutton\" value=\"Save\"  onclick=\"saveAccountBook();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucheraccountdetails", stringBuff.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveVoucherCancellation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountbooktype, String voucherids, String accountdate, String vouchertype) {
        Map resultMap = new HashMap();
        String[] strArray = null;
        Transaction transaction;
        try {
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            transaction = session.beginTransaction();
            try {
                String vouchernos = voucherids.substring(0, voucherids.length() - 1);
                vouchernos = vouchernos.replaceAll(",", "','");

                if (vouchertype.equalsIgnoreCase("P") || vouchertype.equalsIgnoreCase("R")) {
                    try {
                        session.createSQLQuery("UPDATE receiptpaymentdetails  SET cancelled  = true WHERE voucher in ('" + vouchernos + "') and cancelled is false and  region='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                    } catch (Exception e) {
                        transaction.rollback();
                    }
                    if (transaction.wasCommitted()) {
                        transaction = session.beginTransaction();
                        try {
                            session.createSQLQuery("UPDATE voucherdetails  SET cancelled  = true WHERE voucher in ('" + vouchernos + "') and cancelled is false and  region='" + LoggedInRegion + "'").executeUpdate();
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }
                    }

                    if (transaction.wasCommitted()) {
                        transaction = session.beginTransaction();
                        try {
                            session.createSQLQuery("UPDATE voucher  SET cancelled  = true WHERE id in ('" + vouchernos + "') and cancelled is false and accountbook  = '" + accountbooktype + "' and voucherdate='" + postgresDate(accountdate) + "' and accountingperiod='" + periodcode + "'  and region='" + LoggedInRegion + "'").executeUpdate();
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }
                    }
                } else {

                    transaction = session.beginTransaction();
                    try {
                        session.createSQLQuery("UPDATE voucherdetails  SET cancelled  = true WHERE voucher in ('" + vouchernos + "') and cancelled is false and  region='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                    } catch (Exception e) {
                        transaction.rollback();
                    }


                    if (transaction.wasCommitted()) {
                        transaction = session.beginTransaction();
                        try {
                            session.createSQLQuery("UPDATE voucher  SET cancelled  = true WHERE id in ('" + vouchernos + "') and cancelled is false and accountbook  = '" + accountbooktype + "' and voucherdate='" + postgresDate(accountdate) + "' and accountingperiod='" + periodcode + "'  and region='" + LoggedInRegion + "'").executeUpdate();
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                        }
                    }
                }


                resultMap.put("success", "Selected Vouchers Cancelled");
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
                resultMap.put("ERROR", "Voucher Cancellation Transaction Faild");
            }

        } catch (Exception e) {
            resultMap.put("ERROR", "Voucher Cancellation Transaction Faild");
        }

        return resultMap;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getReceiptDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new LinkedHashMap();
        try {
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            StringBuffer stringBuff = new StringBuffer();
            DecimalFormat df = new DecimalFormat();
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"8%\">S No</th>");
            stringBuff.append("<th width=\"8%\">Voucher No</th>");
            stringBuff.append("<th width=\"8%\">Date</th>");
            stringBuff.append("<th width=\"8%\">Party Name</th>");
            stringBuff.append("<th width=\"8%\">Bank</th>");
            stringBuff.append("<th width=\"8%\">Ch/DD</th>");
            stringBuff.append("<th width=\"8%\">Ch/DD no</th>");
            stringBuff.append("<th width=\"8%\">Amount</th>");
            stringBuff.append("<th width=\"8%\">Depo.Bank</th>");
            stringBuff.append("<th width=\"8%\">Challan No</th>");
            stringBuff.append("<th width=\"8%\">Date</th>");
            stringBuff.append("<th width=\"8%\">Real Status</th>");
            stringBuff.append("<th width=\"8%\">Real Stat</th>");
            stringBuff.append("<th width=\"2%\"></th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
//            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
            StringBuffer query = new StringBuffer();
            int i = 0;
//                query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
//                query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
//                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end and vd.cancelled is false and v.cancelled is false  ");
//                query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode  ");

            query.append(" select cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer) as voucherid, v.voucherdate as voucherdate, v.narration as narration,ab.bookname as accountbook, rpd.id as recppayid, rpd.favourof as partyname ,rpd.otherbankname as otherbankname, ");
            query.append(" rpd.refno as refno, pm.type as paymentmode, rpd.amount as amount, rpd.bankchallan as challanid, bc.challandate as challandate, bc.bank as bankcode, bl.bankname as bankname, ");
            query.append(" bl.branchname as bankbranchname, bl.accountno as bankaccountno, rpd.realized as realized, rpd.realizeddate as realizeddate, cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer) as vounumord ");
            query.append(" from receiptpaymentdetails rpd  ");
            query.append(" left join voucher v on v.id=rpd.voucher ");
            query.append(" left join partyledger pl on pl.code=rpd.partyledger ");
            query.append(" left join bankchallan bc on bc.id=rpd.bankchallan ");
            query.append(" left join bankledger bl on bl.code=bc.bank ");
            query.append(" left join accountsbooks ab on ab.code=v.accountbook ");
            query.append(" left join paymentmode pm on pm.code=rpd.paymentmode ");
            query.append(" where rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + periodcode + "' and rpd.cancelled is false and v.cancelled is false and (rpd.realized is false or rpd.realized  is null)  and v.vouchertype='R' ");
//            query.append(" where rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + periodcode + "' and rpd.cancelled is false and v.cancelled is false and (rpd.realized is false or rpd.realized  is null) and rpd.paymentmode in ('1','2','3') and v.vouchertype='R' ");
            query.append(" order by voucherdate, voucherid ");

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

            //System.out.println(query);
            SQLQuery ledgerquery = session.createSQLQuery(query.toString());
            for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                if (rows[0] != null) {
                    voucherid = LoggedInRegion + "R" + rows[0].toString();
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
                    amount = rows[9].toString();
                } else {
                    amount = "";
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

                i++;
                stringBuff.append("<tr >");
                stringBuff.append("<td >" + i + "</td>");
                stringBuff.append("<td >" + voucherid + "</td>");
                stringBuff.append("<td >" + voucherdate + "</td>");
                stringBuff.append("<td >" + partyname + "</td>");
                stringBuff.append("<td >" + otherbankname + "</td>");
                stringBuff.append("<td >" + paymentmode + "</td>");
                stringBuff.append("<td >" + refno + "</td>");
                stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                stringBuff.append("<td >" + bankname + "</td>");
                stringBuff.append("<td >" + challanid + "</td>");
                stringBuff.append("<td >" + challandate + "</td>");
                stringBuff.append("<td >" + realized + "</td>");
                stringBuff.append("<td >" + realizeddate + "</td>");
                stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"recid\" id=\"recid\" onclick=\"showRealiseForm('" + recppayid + "')\">" + "</td>");
                stringBuff.append("</tr>");

            }
//            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            stringBuff.append("<table width=\"100%\" align=\"center\" class=\"tableBorder2\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
            stringBuff.append("<tr class=\"lightrow\">");
            stringBuff.append("<td colspan=\"6\" align=\"center\">");
            stringBuff.append("<input type=\"checkbox\" name=\"isrealized\" id=\"isrealized\" value=\"checkbox\" />Is Realized ?");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
            stringBuff.append("<tr class=\"darkrow\">");
            stringBuff.append("<td colspan=\"6\" align=\"center\">");
            stringBuff.append("<input type=\"button\" class=\"submitbu\" id=\"printbut\" value=\"Print\" onclick=\"onprintout();\">");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
            stringBuff.append("<tr class=\"lightrow\">");
            stringBuff.append("<td colspan=\"6\" align=\"center\">");
            stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
            stringBuff.append("<div id=\"paybillprintresult\"></div>");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
            stringBuff.append("</table>");

            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveReceiptRealizationDate(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String recpayid, String realizationdate) {
        Map resultMap = new HashMap();
        Transaction transaction;
        Receiptpaymentdetails receiptpaymentdetailsObj = null;
        Criteria recPayCrit = session.createCriteria(Receiptpaymentdetails.class);
        recPayCrit.add(Restrictions.sqlRestriction("id='" + recpayid + "'"));
        List recPayList = recPayCrit.list();
        if (recPayList.size() > 0) {
            receiptpaymentdetailsObj = (Receiptpaymentdetails) recPayList.get(0);
            receiptpaymentdetailsObj.setRealized(Boolean.TRUE);
            receiptpaymentdetailsObj.setRealizeddate(postgresDate(realizationdate));
            transaction = session.beginTransaction();
            try {
                session.update(receiptpaymentdetailsObj);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
        resultMap.put("voucherdetails", getReceiptDetails(null, request, response, null, null).get("voucherdetails"));
        return resultMap;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPaymentDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdatefrom, String voucherdateto, String book) {
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
            stringBuff.append("<th width=\"8%\">Cash Book</th>");
            stringBuff.append("<th width=\"8%\">Comp No</th>");
            stringBuff.append("<th width=\"8%\">Voucher No</th>");
            stringBuff.append("<th width=\"8%\">Date</th>");
            stringBuff.append("<th width=\"8%\">Party Name</th>");
            stringBuff.append("<th width=\"8%\">Bank</th>");
            stringBuff.append("<th width=\"8%\">Ch/DD</th>");
            stringBuff.append("<th width=\"8%\">Ch/DD No</th>");
            stringBuff.append("<th width=\"8%\">Amount</th>");
            stringBuff.append("<th width=\"8%\">Real Status</th>");
            stringBuff.append("<th width=\"8%\">Real Stat</th>");
            stringBuff.append("<th width=\"2%\"></th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                StringBuffer query = new StringBuffer();
                int i = 0;
//                query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
//                query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
//                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end and vd.cancelled is false and v.cancelled is false  ");
//                query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode  ");
                query.append(" select voucherid,voucherdate,narration,accountbook, recppayid,partyname, otherbankname, refno,paymentmode,amount,challanid,challandate,bankcode,bankname, ");
                query.append(" bankbranchname,bankaccountno,realized,realizeddate,vnx, cast(REPLACE(TRANSLATE(xx, REPLACE(TRANSLATE(xx,'0123456789', RPAD('#',LENGTH(xx),'#')),'#',''),  RPAD('#',LENGTH(xx),'#')),'#','') as INTEGER) as xxx  from");
                query.append(" (select v.id as voucherid, case when v.voucherapproveddate is null then v.voucherdate else v.voucherapproveddate end as voucherdate, v.narration as narration,ab.bookname as accountbook, rpd.id as recppayid, rpd.favourof as partyname ,rpd.otherbankname as otherbankname, ");
                query.append(" rpd.refno as refno, pm.type as paymentmode, rpd.amount as amount, rpd.bankchallan as challanid, bc.challandate as challandate, bc.bank as bankcode, bl.bankname as bankname, ");
                query.append(" bl.branchname as bankbranchname, bl.accountno as bankaccountno, rpd.realized as realized, rpd.realizeddate as realizeddate, v.voucherno as vnx, case when position('-' in v.voucherno)>0 then trim(split_part(v.voucherno,'-',1)) else case when position('/' in v.voucherno)>=0 then trim(split_part(v.voucherno,'/',1)) else v.voucherno end  end as xx ");
                query.append(" from receiptpaymentdetails rpd  ");
                query.append(" left join voucher v on v.id=rpd.voucher ");
                query.append(" left join partyledger pl on pl.code=rpd.partyledger ");
                query.append(" left join bankchallan bc on bc.id=rpd.bankchallan ");
                query.append(" left join bankledger bl on bl.code=rpd.bankname ");
                query.append(" left join paymentmode pm on pm.code=rpd.paymentmode ");
                query.append(" left join accountsbooks ab on ab.code=v.accountbook ");
                query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
                query.append(" and (rpd.realized is false  or rpd.realized is null) and v.accountbook='" + book + "' and  rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and rpd.cancelled is false and v.cancelled is false and (rpd.paymentmode in ('2','3') or length(trim(rpd.refno))>0) and v.vouchertype='P') as x ");
                query.append(" order by voucherdate, xxx ");

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

                //System.out.println(query);
                SQLQuery ledgerquery = session.createSQLQuery(query.toString());
                for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
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
                        amount = rows[9].toString();
                    } else {
                        amount = "";
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

                    i++;
                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + accountbook + "</td>");
                    stringBuff.append("<td >" + voucherid + "</td>");
                    stringBuff.append("<td >" + vno + "</td>");
                    stringBuff.append("<td >" + voucherdate + "</td>");
                    stringBuff.append("<td >" + partyname + "</td>");
                    stringBuff.append("<td >" + bankname + "</td>");
                    stringBuff.append("<td >" + paymentmode + "</td>");
                    stringBuff.append("<td >" + refno + "</td>");
                    stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                    stringBuff.append("<td >" + realized + "</td>");
                    stringBuff.append("<td >" + realizeddate + "</td>");
                    stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"recid\" id=\"recid\" onclick=\"showRealiseForm('" + recppayid + "')\">" + "</td>");
                    stringBuff.append("</tr>");

                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            stringBuff.append("<table width=\"100%\" align=\"center\" class=\"tableBorder2\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
            stringBuff.append("<tr class=\"lightrow\">");
            stringBuff.append("<td colspan=\"6\" align=\"center\">");
            stringBuff.append("<input type=\"checkbox\" name=\"isrealized\" id=\"isrealized\" value=\"checkbox\" />Is Realized ?");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
            stringBuff.append("<tr class=\"darkrow\">");
            stringBuff.append("<td colspan=\"6\" align=\"center\">");
            stringBuff.append("<input type=\"button\" class=\"submitbu\" id=\"printbut\" value=\"Print\" onclick=\"onprintout();\">");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
            stringBuff.append("<tr class=\"lightrow\">");
            stringBuff.append("<td colspan=\"6\" align=\"center\">");
            stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
            stringBuff.append("<div id=\"paybillprintresult\"></div>");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
            stringBuff.append("</table>");

            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map savePaymentRealizationDate(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String recpayid, String realizationdate, String period, String voucherdatefrom, String voucherdateto, String book) {
        Map resultMap = new HashMap();
        Transaction transaction;
        Receiptpaymentdetails receiptpaymentdetailsObj = null;
        Criteria recPayCrit = session.createCriteria(Receiptpaymentdetails.class);
        recPayCrit.add(Restrictions.sqlRestriction("id='" + recpayid + "'"));
        List recPayList = recPayCrit.list();
        if (recPayList.size() > 0) {
            receiptpaymentdetailsObj = (Receiptpaymentdetails) recPayList.get(0);
            receiptpaymentdetailsObj.setRealized(Boolean.TRUE);
            receiptpaymentdetailsObj.setRealizeddate(postgresDate(realizationdate));
            transaction = session.beginTransaction();
            try {
                session.update(receiptpaymentdetailsObj);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
        resultMap.put("voucherdetails", getPaymentDetails(null, request, response, null, null, period, voucherdatefrom, voucherdateto, book).get("voucherdetails"));
        return resultMap;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getChequeRegister(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdatefrom, String voucherdateto, String book) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();
            DecimalFormat df = new DecimalFormat();
            java.util.Date chequeDate = null;
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"2%\">S No</th>");
            stringBuff.append("<th width=\"4%\">Modify</th>");
            stringBuff.append("<th width=\"6%\">Cheque No</th>");
            stringBuff.append("<th width=\"6%\">Cheque Date</th>");
            stringBuff.append("<th width=\"6%\">Comp No</th>");
            stringBuff.append("<th width=\"6%\">Voucher No</th>");
            stringBuff.append("<th width=\"6%\">Voucher Date</th>");
            stringBuff.append("<th width=\"6%\">Payment Type</th>");
            stringBuff.append("<th width=\"10%\">Paid to Whome</th>");
            Bankledger bankledgerObj;
            Criteria bankCrit = session.createCriteria(Bankledger.class);
            bankCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            List bankList = bankCrit.list();
            String[] code = new String[bankList.size()];
            float[] tot = new float[bankList.size()];
            BigDecimal[] total = new BigDecimal[bankList.size()];
            if (bankList.size() > 0) {
                for (int i = 0; i < bankList.size(); i++) {
                    bankledgerObj = (Bankledger) bankList.get(i);
                    stringBuff.append("<th width=\"8%\">" + bankledgerObj.getBankname() + "</th>");
                    code[i] = bankledgerObj.getCode();
                    tot[i] = 0;
                    total[i] = new BigDecimal(0.00);
                }
            }

            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
                StringBuffer query = new StringBuffer();
                int i = 0;
                query.append(" select voucherid,voucherdate,narration,accountbook, recppayid,partyname, otherbankname, refno,paymentmode,amount,challanid,challandate,bankcode,bankname, ");
                query.append(" bankbranchname,bankaccountno,realized,realizeddate,vnx, cast(REPLACE(TRANSLATE(xx, REPLACE(TRANSLATE(xx,'0123456789', RPAD('#',LENGTH(xx),'#')),'#',''),  RPAD('#',LENGTH(xx),'#')),'#','') as INTEGER) as xxx,chequedate,paymenttype  from");
                query.append(" (select v.id as voucherid, case when v.voucherapproveddate is null then v.voucherdate else v.voucherapproveddate end as voucherdate, v.narration as narration,ab.bookname as accountbook, rpd.id as recppayid, rpd.favourof as partyname ,rpd.otherbankname as otherbankname, ");
                query.append(" rpd.refno as refno, pm.type as paymentmode, rpd.amount as amount, rpd.bankchallan as challanid, bc.challandate as challandate, bl.code as bankcode, bl.bankname as bankname, ");
                query.append(" bl.branchname as bankbranchname, bl.accountno as bankaccountno, rpd.realized as realized, rpd.realizeddate as realizeddate, v.voucherno as vnx, case when position('-' in v.voucherno)>0 then trim(split_part(v.voucherno,'-',1)) else case when position('/' in v.voucherno)>=0 then trim(split_part(v.voucherno,'/',1)) else v.voucherno end  end as xx,rpd.chequedate as chequedate ");
                query.append(" ,pm.type as paymenttype ");
                query.append(" from receiptpaymentdetails rpd  ");
                query.append(" left join voucher v on v.id=rpd.voucher ");
                query.append(" left join partyledger pl on pl.code=rpd.partyledger ");
                query.append(" left join bankchallan bc on bc.id=rpd.bankchallan ");
                query.append(" left join bankledger bl on bl.code=rpd.bankname ");
                query.append(" left join paymentmode pm on pm.code=rpd.paymentmode ");
                query.append(" left join accountsbooks ab on ab.code=v.accountbook ");
                query.append(" where case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end   ");
                query.append(" and  v.accountbook='" + book + "' and  rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and rpd.cancelled is false and v.cancelled is false  and v.vouchertype='P') as x ");
//                query.append(" and  v.accountbook='" + book + "' and  rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and rpd.cancelled is false and v.cancelled is false and rpd.paymentmode in ('1','2','3') and v.vouchertype='P') as x ");
                query.append(" order by cast(xx as integer),refno  ");
//                query.append(" order by voucherdate,  cast(xx as integer)  ");
//                order by cast(xx as integer),refno

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
                BigDecimal amt = new BigDecimal(0.00);
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
                String paymenttype = "";

                //System.out.println(query);
                SQLQuery ledgerquery = session.createSQLQuery(query.toString());
                for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
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
                        amount = rows[9].toString();
                        amt = new BigDecimal(rows[9].toString());
                    } else {
                        amount = "";
                        amt = new BigDecimal(0.00);
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
                        bankcode = "0";
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
//                        chequedate = rows[20].toString();
                        chequeDate = (java.util.Date) rows[20];
                        chequedate = chequeDate.toString();
                    } else {
                        chequedate = "";
                    }
                    if (rows[21] != null) {
//                        chequedate = rows[20].toString();

                        paymenttype = rows[21].toString();
                    } else {
                        paymenttype = "";
                    }


                    i++;
                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + i + "</td>");
                    if (rows[20] != null) {
                        stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"recid\" id=\"recid\" onclick=\"showRealiseForm('" + recppayid + "','" + dateToString(chequeDate) + "','" + refno + "','" + dateToString((java.util.Date) rows[1]) + "','" + bankcode + "')\">" + "</td>");

                    } else {
                        stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"recid\" id=\"recid\" onclick=\"showRealiseForm('" + recppayid + "','','" + refno + "','" + dateToString((java.util.Date) rows[1]) + "','" + bankcode + "')\">" + "</td>");
                    }
                    stringBuff.append("<td >" + refno + "</td>");
                    stringBuff.append("<td >" + chequedate + "</td>");
                    stringBuff.append("<td >" + voucherid + "</td>");
                    stringBuff.append("<td >" + vno + "</td>");
                    stringBuff.append("<td >" + voucherdate + "</td>");
                    stringBuff.append("<td >" + paymenttype + "</td>");
                    stringBuff.append("<td >" + partyname + "</td>");
                    for (int o = 0; o < code.length; o++) {
                        if (bankcode.equalsIgnoreCase(code[o])) {
                            stringBuff.append("<td align=\"right\" >" + amt + "</td>");
//                            stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                            tot[o] = tot[o] + Float.parseFloat(amount);
                            total[o] = total[o].add(amt);
                        } else {
                            stringBuff.append("<td align=\"right\" ></td>");
                        }
                    }
//                    stringBuff.append("<td align=\"right\" ></td>");


                    stringBuff.append("</tr>");
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"2%\"></th>");
            stringBuff.append("<th width=\"4%\"></th>");
            stringBuff.append("<th width=\"6%\"></th>");
            stringBuff.append("<th width=\"6%\"></th>");
            stringBuff.append("<th width=\"6%\"></th>");
            stringBuff.append("<th width=\"6%\"></th>");
            stringBuff.append("<th width=\"6%\"></th>");
            stringBuff.append("<th width=\"6%\"></th>");
            stringBuff.append("<th width=\"10%\"></th>");
            for (int o = 0; o < code.length; o++) {
                stringBuff.append("<td width=\"8%\" align=\"right\" >" + total[o] + "</td>");
//                stringBuff.append("<td width=\"8%\" align=\"right\" >" + new BigDecimal(tot[o]) + "</td>");
            }
            stringBuff.append("<th width=\"2%\"></th>");
            stringBuff.append("</tr>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            stringBuff.append("<table width=\"100%\" align=\"center\" class=\"tableBorder2\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
            stringBuff.append("<tr class=\"darkrow\">");
            stringBuff.append("<td colspan=\"6\" align=\"center\">");
            stringBuff.append("<input type=\"button\" class=\"submitbu\" id=\"printbut\" value=\"Print\" onclick=\"onprintout();\">");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
            stringBuff.append("<tr class=\"lightrow\">");
            stringBuff.append("<td colspan=\"6\" align=\"center\">");
            stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
            stringBuff.append("<div id=\"paybillprintresult\"></div>");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
            stringBuff.append("</table>");

            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map changeChequeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String recpayid, String chequedate, String period, String voucherdatefrom, String voucherdateto, String book, String chequeno, String accbank) {
        Map resultMap = new HashMap();
        Transaction transaction;
        Receiptpaymentdetails receiptpaymentdetailsObj = null;
        Criteria recPayCrit = session.createCriteria(Receiptpaymentdetails.class);
        recPayCrit.add(Restrictions.sqlRestriction("id='" + recpayid + "'"));
        recPayCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List recPayList = recPayCrit.list();
        if (recPayList.size() > 0) {
            receiptpaymentdetailsObj = (Receiptpaymentdetails) recPayList.get(0);
            receiptpaymentdetailsObj.setChequedate(postgresDate(chequedate));
            receiptpaymentdetailsObj.setRefno(chequeno);
            Bankledger bnam = CommonUtility.geBankledger(session, accbank);
            if (bnam != null) {
                receiptpaymentdetailsObj.setBankledger(bnam);
            }
            transaction = session.beginTransaction();
            try {
                session.update(receiptpaymentdetailsObj);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
        resultMap.put("voucherdetails", getChequeRegister(null, request, response, null, null, period, voucherdatefrom, voucherdateto, book).get("voucherdetails"));
        return resultMap;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getVoucherDetailsForModify(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String accountbooktype) {
        Map resultMap = new HashMap();
        Voucher voucherObj = null;
        Voucherdetails voucherdetailsObj = null;
        BigDecimal payment = new BigDecimal(0.00);
        BigDecimal adjustment = new BigDecimal(0.00);
        String amt = "";


        try {
            StringBuffer stringBuff = new StringBuffer();
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
//            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"voucheraccounttable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"10%\">Comp No</th>");
            stringBuff.append("<th width=\"15%\">Voucher No</th>");
            stringBuff.append("<th width=\"15%\">Voucher Date</th>");
            stringBuff.append("<th width=\"45%\">Remarks</th>");
            stringBuff.append("<th width=\"10%\">Amount</th>");
            stringBuff.append("<th width=\"5%\">Select</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria vouCrit = session.createCriteria(Voucher.class);
            vouCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            vouCrit.add(Restrictions.sqlRestriction("vouchertype='P'"));
            vouCrit.add(Restrictions.sqlRestriction("accountbook='" + accountbooktype + "'"));
            vouCrit.add(Restrictions.sqlRestriction("voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "'"));
            vouCrit.add(Restrictions.sqlRestriction("accountingperiod='" + periodcode + "'"));
            vouCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            vouCrit.addOrder(Order.asc("voucherno"));
//            vouCrit.add(Restrictions.sqlRestriction("locked is true"));
//            vouCrit.addOrder(Order.asc("cast(replace(id ,'"+LoggedInRegion + 'P'+"','') as int)"));
//            vouCrit.addOrder(Order.asc((Projections.property("id").toString().replaceAll(LoggedInRegion + 'P', ""))));

            String Voucher_QUERY = "select *,cast((case when position('-' in voucherno)>0 then substring(trim(split_part(voucherno,'-',1)) FROM '[0-9]+') "
                    + "else case when position('/' in voucherno)>=0 then substring(trim(split_part(voucherno,'/',1)) FROM '[0-9]+')  else substring(voucherno FROM '[0-9]+') end  end )  as integer ) as xx from voucher where region='" + LoggedInRegion + "' and vouchertype in ('P','R') "
                    + " and accountbook='" + accountbooktype + "' and voucherdate between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "'"
                    + " and accountingperiod='" + periodcode + ""
                    + "' and cancelled is false order by cast((case when position('-' in voucherno)>0 then substring(trim(split_part(voucherno,'-',1)) FROM '[0-9]+') else case when position('/' in voucherno)>=0 then  substring(trim(split_part(voucherno,'/',1)) FROM '[0-9]+') else substring(voucherno FROM '[0-9]+')  end  end ) as integer) ";
            Query query1 = session.createSQLQuery(Voucher_QUERY).addEntity(Voucher.class);
            System.out.println(Voucher_QUERY.toString());
//                List amtList = query.list();

            List vouList = query1.list();
//            List vouList = vouCrit.list();
            for (int i = 0; i < vouList.size(); i++) {
                voucherObj = (Voucher) vouList.get(i);
                String voucherno = "";
                if (voucherObj.getVoucherno() != null) {
                    voucherno = voucherObj.getVoucherno();
                }



                String SQL_QUERY = "select CASE WHEN coalesce(sum(debit),0) > coalesce(sum(credit),0)"
                        + " THEN coalesce(sum(debit),0) - coalesce(sum(credit),0)"
                        + " ELSE coalesce(sum(credit),0) - coalesce(sum(debit),0) END as amount "
                        + "  from voucherdetails where voucher='" + voucherObj.getId() + "' and region='" + LoggedInRegion + "' and cancelled is false";
                Query query = session.createSQLQuery(SQL_QUERY);
                List amtList = query.list();
                if (amtList.size() > 0 && amtList.get(0) != null) {
                    amt = amtList.get(0).toString();
                }

                stringBuff.append("<tr >");
                stringBuff.append("<td >" + (i + 1) + "</td>");
                stringBuff.append("<td >" + voucherObj.getId() + "</td>");
                if (voucherObj.getVoucherno() != null) {
                    stringBuff.append("<td align=\"center\" >" + voucherno + "</td>");
                } else {
                    stringBuff.append("<td align=\"center\"> - </td>");
                }
                stringBuff.append("<td align=\"center\" >" + dateToString(voucherObj.getVoucherapproveddate()) + "</td>");
                stringBuff.append("<td  >" + voucherObj.getNarration() + "</td>");
                stringBuff.append("<td   align=\"right\">" + amt + "</td>");
                stringBuff.append("<td  align=\"center\"><input type=\"radio\" name=\"modivou\" id=\"modivou\" onclick=\"showUpdateVoucherDetails('" + voucherObj.getId() + "','" + voucherno + "','" + dateToString(voucherObj.getVoucherapproveddate()) + "','" + dateToString(voucherObj.getVoucherdate()) + "');\"/></td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
//            stringBuff.append("<tfoot>");
//            stringBuff.append("<td colspan=\"4\" align=\"center\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"savebutton\" id=\"savebutton\" value=\"Save\"  onclick=\"saveAccountBook();\"  >" + "</td>");
//            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucheraccountdetails", stringBuff.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map changeVoucherDateandnoDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherid, String voucherno, String voucherapproveddate, String fromdate, String todate, String accbook) {
        Map resultMap = new HashMap();
        Transaction transaction;
        Voucher voucherObj = null;
        String periodcode = (String) request.getSession(false).getAttribute("financialYear");
        Criteria modiCrit = session.createCriteria(Voucher.class);
        modiCrit.add(Restrictions.sqlRestriction("id='" + voucherid + "'"));
        modiCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List modiList = modiCrit.list();
        if (modiList.size() > 0) {
            voucherObj = (Voucher) modiList.get(0);
            voucherObj.setVoucherapproveddate(postgresDate(voucherapproveddate));
            voucherObj.setVoucherno(voucherno);
            transaction = session.beginTransaction();
            try {
                session.update(voucherObj);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
        resultMap.put("voucheraccountdetails", getVoucherDetailsForModify(null, request, response, null, null, fromdate, todate, accbook).get("voucheraccountdetails"));
        return resultMap;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadBanks(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map bankMap = new LinkedHashMap();
        bankMap.put("0", "--Select--");

        try {
            Criteria bankCrit = session.createCriteria(Bankledger.class);
            bankCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            bankCrit.addOrder(Order.asc("bankname"));
            List<Bankledger> bankList = bankCrit.list();
            resultMap = new TreeMap();
            for (Bankledger bankobj : bankList) {
                bankMap.put(bankobj.getCode(), bankobj.getBankname());
            }
            resultMap.put("banklist", bankMap);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRemittanceDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate) {
        Map resultMap = new LinkedHashMap();
        try {
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            StringBuffer stringBuff = new StringBuffer();
            DecimalFormat df = new DecimalFormat();
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"8%\">S No</th>");
            stringBuff.append("<th width=\"8%\">Voucher No</th>");
            stringBuff.append("<th width=\"8%\">Date</th>");
//            stringBuff.append("<th width=\"8%\">Party Name</th>");
//            stringBuff.append("<th width=\"8%\">Bank</th>");
            stringBuff.append("<th width=\"8%\">Ch/DD</th>");
            stringBuff.append("<th width=\"8%\">Ch/DD no</th>");
            stringBuff.append("<th width=\"8%\">Amount</th>");
            stringBuff.append("<th width=\"8%\">Depo.Bank</th>");
            stringBuff.append("<th width=\"8%\">Challan No</th>");
//            stringBuff.append("<th width=\"8%\">Date</th>");
//            stringBuff.append("<th width=\"8%\">Real Status</th>");
//            stringBuff.append("<th width=\"8%\">Real Stat</th>");
            stringBuff.append("<th width=\"8%\">Remittance Date</th>");
//            stringBuff.append("<th width=\"2%\"></th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
//            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
            StringBuffer query = new StringBuffer();
            int i = 0;
//                query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
//                query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
//                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end and vd.cancelled is false and v.cancelled is false  ");
//                query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode  ");

            query.append(" select cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer) as voucherid, v.voucherdate as voucherdate, v.narration as narration,ab.bookname as accountbook, rpd.id as recppayid, rpd.favourof as partyname ,rpd.otherbankname as otherbankname, ");
            query.append(" rpd.refno as refno, pm.type as paymentmode, rpd.amount as amount, rpd.bankchallan as challanid, bc.challandate as challandate, bc.bank as bankcode, bl.bankname as bankname, ");
            query.append(" bl.branchname as bankbranchname, bl.accountno as bankaccountno, rpd.realized as realized, rpd.realizeddate as realizeddate,bc.printdate as remittancedate, cast(replace(rpd.bankchallan,'" + LoggedInRegion + "','') as integer) as challanidd ");
            query.append(" from receiptpaymentdetails rpd  ");
            query.append(" left join voucher v on v.id=rpd.voucher ");
            query.append(" left join partyledger pl on pl.code=rpd.partyledger ");
            query.append(" left join bankchallan bc on bc.id=rpd.bankchallan ");
            query.append(" left join bankledger bl on bl.code=bc.bank ");
            query.append(" left join accountsbooks ab on ab.code=v.accountbook ");
            query.append(" left join paymentmode pm on pm.code=rpd.paymentmode ");
            query.append(" where rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  ");
            query.append(" and v.voucherdate  between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "'  ");
            query.append("and v.accountingperiod='" + periodcode + "' and rpd.cancelled is false and v.cancelled is false and  v.vouchertype='R' ");
//            query.append(" where rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + periodcode + "' and rpd.cancelled is false and v.cancelled is false and (rpd.realized is false or rpd.realized  is null) and rpd.paymentmode in ('1','2','3') and v.vouchertype='R' ");
            query.append(" order by challanidd,voucherdate, voucherid ");

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
            String remittancedate = "";

            //System.out.println(query);
            SQLQuery ledgerquery = session.createSQLQuery(query.toString());
            for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                if (rows[0] != null) {
                    voucherid = LoggedInRegion + "R" + rows[0].toString();
                } else {
                    voucherid = "";
                }

                if (rows[1] != null) {
//                    voucherdate = rows[1].toString();
                    voucherdate = (dateToString((java.util.Date) rows[1])).toString();

//                    voucherdate=dateToString(postgresDate(voucherdate.toString()));
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
                    amount = rows[9].toString();
                } else {
                    amount = "";
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

//                    remittancedate = rows[18].toString();
                    remittancedate = (dateToString((java.util.Date) rows[18])).toString();
                } else {
                    remittancedate = "";
                }

                i++;
                stringBuff.append("<tr >");
                stringBuff.append("<td align=\"center\">" + i + "</td>");
                stringBuff.append("<td >" + voucherid + "</td>");
                stringBuff.append("<td align=\"center\">" + voucherdate + "</td>");
//                stringBuff.append("<td >" + partyname + "</td>");
//                stringBuff.append("<td >" + otherbankname + "</td>");
                stringBuff.append("<td >" + paymentmode + "</td>");
                stringBuff.append("<td >" + refno + "</td>");
                stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                stringBuff.append("<td >" + bankname + "</td>");
                stringBuff.append("<td >" + challanid + "</td>");
//                stringBuff.append("<td >" + challandate + "</td>");
//                stringBuff.append("<td >" + realized + "</td>");
//                stringBuff.append("<td >" + realizeddate + "</td>");
                stringBuff.append("<td align=\"center\">" + remittancedate + "</td>");
//                stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"recid\" id=\"recid\" onclick=\"showRemittanceForm('" + challanid + "','" + remittancedate + "')\">" + "</td>");
                stringBuff.append("</tr>");

            }
//            }
            stringBuff.append("</tbody>");
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
    public Map getRemittanceEntryDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate) {
        Map resultMap = new LinkedHashMap();
        try {
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            StringBuffer stringBuff = new StringBuffer();
            DecimalFormat df = new DecimalFormat();
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"vouchertable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"8%\">S No</th>");
            stringBuff.append("<th width=\"8%\">Voucher No</th>");
            stringBuff.append("<th width=\"8%\">Date</th>");
//            stringBuff.append("<th width=\"8%\">Party Name</th>");
//            stringBuff.append("<th width=\"8%\">Bank</th>");
            stringBuff.append("<th width=\"8%\">Ch/DD</th>");
            stringBuff.append("<th width=\"8%\">Ch/DD no</th>");
            stringBuff.append("<th width=\"8%\">Amount</th>");
            stringBuff.append("<th width=\"8%\">Depo.Bank</th>");
            stringBuff.append("<th width=\"8%\">Challan No</th>");
//            stringBuff.append("<th width=\"8%\">Date</th>");
//            stringBuff.append("<th width=\"8%\">Real Status</th>");
//            stringBuff.append("<th width=\"8%\">Real Stat</th>");
            stringBuff.append("<th width=\"8%\">Remittance Date</th>");
            stringBuff.append("<th width=\"2%\"></th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
//            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
            StringBuffer query = new StringBuffer();
            int i = 0;
//                query.append(" select accountcode,sum(debit) as debit , sum(credit) as credit from ( select accountcode, case when vd.voucheroption='Payment' or vd.voucheroption='Debit' then sum(vd.amount) end as debit,case when vd.voucheroption='Adjustment' or vd.voucheroption='Receipt' or vd.voucheroption='Credit' then sum(vd.amount) end as credit  ");
//                query.append(" from voucherdetails vd left join voucher v on v.id=vd.voucher where v.accountingperiod='" + period + "'  and vd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  and   case when v.voucherapproveddate is null then v.voucherdate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' ");
//                query.append(" else  v.voucherapproveddate between '" + postgresDate(voucherdatefrom) + "' and '" + postgresDate(voucherdateto) + "' end and vd.cancelled is false and v.cancelled is false  ");
//                query.append(" GROUP BY accountcode,voucheroption ) as saisiva group by accountcode  ");

            query.append(" select cast(replace(replace(trim(split_part(v.id,'-',1)),'" + LoggedInRegion + "',''),'R','') as integer) as voucherid, v.voucherdate as voucherdate, v.narration as narration,ab.bookname as accountbook, rpd.id as recppayid, rpd.favourof as partyname ,rpd.otherbankname as otherbankname, ");
            query.append(" rpd.refno as refno, pm.type as paymentmode, rpd.amount as amount, rpd.bankchallan as challanid, bc.challandate as challandate, bc.bank as bankcode, bl.bankname as bankname, ");
            query.append(" bl.branchname as bankbranchname, bl.accountno as bankaccountno, rpd.realized as realized, rpd.realizeddate as realizeddate,bc.printdate as remittancedate, cast(replace(rpd.bankchallan,'" + LoggedInRegion + "','') as integer) as challanidd ");
            query.append(" from receiptpaymentdetails rpd  ");
            query.append(" left join voucher v on v.id=rpd.voucher ");
            query.append(" left join partyledger pl on pl.code=rpd.partyledger ");
            query.append(" left join bankchallan bc on bc.id=rpd.bankchallan ");
            query.append(" left join bankledger bl on bl.code=bc.bank ");
            query.append(" left join accountsbooks ab on ab.code=v.accountbook ");
            query.append(" left join paymentmode pm on pm.code=rpd.paymentmode ");
            query.append(" where rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "'  ");
            query.append(" and v.voucherdate  between '" + postgresDate(fromdate) + "' and '" + postgresDate(todate) + "'  ");
            query.append("and v.accountingperiod='" + periodcode + "' and rpd.cancelled is false and v.cancelled is false and  v.vouchertype='R' ");
//            query.append(" where rpd.region='" + LoggedInRegion + "' and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + periodcode + "' and rpd.cancelled is false and v.cancelled is false and (rpd.realized is false or rpd.realized  is null) and rpd.paymentmode in ('1','2','3') and v.vouchertype='R' ");
            query.append(" order by challanidd,voucherdate, voucherid ");

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
            String remittancedate = "";

            //System.out.println(query);
            SQLQuery ledgerquery = session.createSQLQuery(query.toString());
            for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                if (rows[0] != null) {
                    voucherid = LoggedInRegion + "R" + rows[0].toString();
                } else {
                    voucherid = "";
                }

                if (rows[1] != null) {
//                    voucherdate = rows[1].toString();
                    voucherdate = (dateToString((java.util.Date) rows[1])).toString();

//                    voucherdate=dateToString(postgresDate(voucherdate.toString()));
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
                    amount = rows[9].toString();
                } else {
                    amount = "";
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

//                    remittancedate = rows[18].toString();
                    remittancedate = (dateToString((java.util.Date) rows[18])).toString();
                } else {
                    remittancedate = "";
                }

                i++;
                stringBuff.append("<tr >");
                stringBuff.append("<td align=\"center\">" + i + "</td>");
                stringBuff.append("<td >" + voucherid + "</td>");
                stringBuff.append("<td align=\"center\">" + voucherdate + "</td>");
//                stringBuff.append("<td >" + partyname + "</td>");
//                stringBuff.append("<td >" + otherbankname + "</td>");
                stringBuff.append("<td >" + paymentmode + "</td>");
                stringBuff.append("<td >" + refno + "</td>");
                stringBuff.append("<td align=\"right\" >" + amount + "</td>");
                stringBuff.append("<td >" + bankname + "</td>");
                stringBuff.append("<td >" + challanid + "</td>");
//                stringBuff.append("<td >" + challandate + "</td>");
//                stringBuff.append("<td >" + realized + "</td>");
//                stringBuff.append("<td >" + realizeddate + "</td>");
                stringBuff.append("<td align=\"center\">" + remittancedate + "</td>");
                stringBuff.append("<td  align=\"center\">" + "<input type=\"radio\" name=\"recid\" id=\"recid\" onclick=\"showRemittanceForm('" + challanid + "','" + remittancedate + "')\">" + "</td>");
                stringBuff.append("</tr>");

            }
//            }
            stringBuff.append("</tbody>");
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
    public Map saveRemittanceDate(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String challanid, String remittancedate, String fromdate, String todate) {
        Map resultMap = new HashMap();
        Transaction transaction;
//        Bankchallan bankchallanObj = null;
        Criteria challanCrit = session.createCriteria(Bankchallan.class);
        challanCrit.add(Restrictions.sqlRestriction("id='" + challanid + "'"));
        List challanList = challanCrit.list();
        if (challanList.size() > 0) {
            for (int i = 0; i < challanList.size(); i++) {
                Bankchallan bankchallanObj = (Bankchallan) challanList.get(i);
//                bankchallanObj.setRealized(Boolean.TRUE);
                bankchallanObj.setPrintdate(postgresDate(remittancedate));
                transaction = session.beginTransaction();
                try {
                    session.update(bankchallanObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
        } else {
            resultMap.put("ERROR", "Please Create a Bank Challan First for this Receipt ");
        }
        resultMap.put("voucherdetails", getRemittanceDetails(null, request, response, null, null, fromdate, todate).get("voucherdetails"));
        return resultMap;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPaymentAbstractDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String book) {
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
            stringBuff.append("<th width=\"5%\">Sno</th>");
            stringBuff.append("<th width=\"25%\">Cheque Date</th>");
            stringBuff.append("<th width=\"25%\">Cheque No</th>");
            stringBuff.append("<th width=\"30%\">Amount</th>");
            stringBuff.append("<th width=\"10%\">Real Status</th>");
            stringBuff.append("<th width=\"5%\"></th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
//            if (!voucherdatefrom.trim().equalsIgnoreCase("null") && !voucherdateto.trim().equalsIgnoreCase("null") && voucherdateto.trim().length() > 0) {
            StringBuffer query = new StringBuffer();
            int i = 1;

            query.append("(select rpd.chequedate as chequedate, rpd.refno as chequeno, sum(rpd.amount) as amount, rpd.realized as realized, 'NEW' as dbtype from receiptpaymentdetails rpd ");
            query.append("left join voucher v on v.id=rpd.voucher ");
            query.append("left join accountsbooks ab on ab.code=v.accountbook ");
            query.append("where ");
            query.append("(rpd.realized is false  or rpd.realized is null) ");
            query.append("and v.accountbook='" + book + "' ");
            query.append("and  rpd.region='" + LoggedInRegion + "' ");
            query.append("and v.region='" + LoggedInRegion + "' ");
            query.append("and v.accountingperiod='" + period + "' ");
            query.append("and rpd.cancelled is false ");
            query.append("and v.cancelled is false ");
            query.append("and (rpd.paymentmode in ('2','3') or length(trim(rpd.refno))>0) ");
            query.append("and v.vouchertype='P' ");
            query.append("group by rpd.refno,rpd.chequedate,rpd.realized) ");
            query.append("union ");
            query.append("(select chequedate as chequedate, refno as chequeno, sum(amount) as amount, realized as realized, 'OLD' as dbtype from receiptpaymentdetailspending ");
            query.append("where ");
            query.append("(realized is false  or realized is null) ");
            query.append("and region='" + LoggedInRegion + "' ");
            query.append("and accountingperiod='3' ");
            query.append("and accountbook='" + book + "' ");
            query.append("and cancelled is false group by chequedate, refno, realized) ");
            query.append("order by chequedate,chequeno");

//            query.append("select rpd.chequedate, rpd.refno, sum(rpd.amount), rpd.realized from receiptpaymentdetails rpd ");
//            query.append("left join voucher v on v.id=rpd.voucher ");
//            query.append("left join accountsbooks ab on ab.code=v.accountbook ");
//            query.append("where ");
//            query.append("(rpd.realized is false  or rpd.realized is null) and v.accountbook='" + book + "' and  rpd.region='" + LoggedInRegion + "' ");
//            query.append("and v.region='" + LoggedInRegion + "' and v.accountingperiod='" + period + "' and rpd.cancelled is false ");
//            query.append("and v.cancelled is false ");
//            query.append("and (rpd.paymentmode in ('2','3') or length(trim(rpd.refno))>0) and v.vouchertype='P' ");
//            query.append("group by rpd.refno,rpd.chequedate,rpd.realized order by rpd.chequedate,rpd.refno");

            String chequedate = "";
            String refno = "";
            String amount = "";
            String realized = "";
            String dbtype = "";

            //System.out.println(query);
            SQLQuery ledgerquery = session.createSQLQuery(query.toString());
            for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();

                if (rows[0] != null) {
                    Date date = (Date) rows[0];
                    chequedate = dateToString(date);
                } else {
                    chequedate = "";
                }
                if (rows[1] != null) {
                    refno = rows[1].toString();
                } else {
                    refno = "";
                }
                if (rows[2] != null) {
                    BigDecimal amo = (BigDecimal) rows[2];
                    double douamount = amo.doubleValue();
                    amount = decimalFormat.format(douamount);
                } else {
                    amount = "";
                }
                if (rows[3] != null) {
                    realized = rows[3].toString();
                } else {
                    realized = "false";
                }

                dbtype = (String) rows[4];

                stringBuff.append("<tr >");
                stringBuff.append("<td align=\"center\">" + i + "</td>");
                stringBuff.append("<td align=\"center\">" + chequedate + "</td>");
                stringBuff.append("<td align=\"center\">" + refno + "</td>");
                stringBuff.append("<td align=\"right\">" + amount + "</td>");
                stringBuff.append("<td align=\"center\">" + realized + "</td>");
                stringBuff.append("<td align=\"center\">" + "<input type=\"radio\" name=\"recid\" id=\"recid\" onclick=\"showRealiseForm('" + (refno + "/" + dbtype) + "')\">" + "</td>");
                stringBuff.append("</tr>");
                i++;

            }
//            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            stringBuff.append("<table width=\"100%\" align=\"center\" class=\"tableBorder2\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
//            stringBuff.append("<tr class=\"lightrow\">");
//            stringBuff.append("<td colspan=\"6\" align=\"center\">");
//            stringBuff.append("<input type=\"checkbox\" name=\"isrealized\" id=\"isrealized\" value=\"checkbox\" />Is Realized ?");
//            stringBuff.append("</td>");
//            stringBuff.append("</tr>");
            stringBuff.append("<tr class=\"darkrow\">");
            stringBuff.append("<td colspan=\"6\" align=\"center\">");
            stringBuff.append("<input type=\"button\" class=\"submitbu\" id=\"printbut\" value=\"Print\" onclick=\"onprintout();\">");
            stringBuff.append("</td>");
            stringBuff.append("</tr>");
//            stringBuff.append("<tr class=\"lightrow\">");
//            stringBuff.append("<td colspan=\"6\" align=\"center\">");
//            stringBuff.append("<div id=\"paybillprogressbar\" style=\"display: none\"><img src=\"images/paybillprint.gif\" width=\"128\" height=\"15\"/></div>");
//            stringBuff.append("<div id=\"paybillprintresult\"></div>");
//            stringBuff.append("</td>");
//            stringBuff.append("</tr>");
            stringBuff.append("</table>");

            resultMap.put("voucherdetails", stringBuff.toString());
//            resultMap.put("bookdisp", getAccountBook(session, book).getBookname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map savePaymentRealizationAbstractDate(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String recpayid, String realizationdate, String period, String book, String dbtype) {
        Map resultMap = new HashMap();
        Transaction transaction;
        if (dbtype.equals("NEW")) {
            Receiptpaymentdetails receiptpaymentdetailsObj = null;
            Criteria recPayCrit = session.createCriteria(Receiptpaymentdetails.class);
            recPayCrit.add(Restrictions.sqlRestriction("refno='" + recpayid + "'"));
            List recPayList = recPayCrit.list();
            if (recPayList.size() > 0) {
                for (int i = 0; i < recPayList.size(); i++) {
                    receiptpaymentdetailsObj = (Receiptpaymentdetails) recPayList.get(i);
//                System.out.println("receiptpaymentdetailsObj.getId() = "+receiptpaymentdetailsObj.getId());
                    receiptpaymentdetailsObj.setRealized(Boolean.TRUE);
                    receiptpaymentdetailsObj.setRealizeddate(postgresDate(realizationdate));
                    transaction = session.beginTransaction();
                    try {
                        session.update(receiptpaymentdetailsObj);
                        transaction.commit();
                    } catch (Exception e) {
                        transaction.rollback();
                    }
                }
            }
        } else if (dbtype.equals("OLD")) {
            Receiptpaymentdetailspending receiptpaymentdetailspending = null;
            Criteria recPayCrit = session.createCriteria(Receiptpaymentdetailspending.class);
            recPayCrit.add(Restrictions.sqlRestriction("refno='" + recpayid + "'"));
            List recPayList = recPayCrit.list();
            if (recPayList.size() > 0) {
                for (int i = 0; i < recPayList.size(); i++) {
                    receiptpaymentdetailspending = (Receiptpaymentdetailspending) recPayList.get(i);
//                System.out.println("receiptpaymentdetailsObj.getId() = "+receiptpaymentdetailsObj.getId());
                    receiptpaymentdetailspending.setRealized(Boolean.TRUE);
                    receiptpaymentdetailspending.setRealizeddate(postgresDate(realizationdate));
                    transaction = session.beginTransaction();
                    try {
                        session.update(receiptpaymentdetailspending);
                        transaction.commit();
                    } catch (Exception e) {
                        transaction.rollback();
                    }
                }
            }
        }
        resultMap.put("voucherdetails", getPaymentAbstractDetails(null, request, response, null, null, period, book).get("voucherdetails"));
        return resultMap;
    }
}
