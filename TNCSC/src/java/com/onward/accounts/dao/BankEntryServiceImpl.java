/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.common.DateParser;
import com.onward.common.DateUtility;
import com.onward.common.ReadExcel;
import com.onward.dao.GlobalDBOpenCloseAndUserPrivilages;
import com.onward.persistence.payroll.Accountingyear;
import com.onward.persistence.payroll.Bankledger;
import com.onward.persistence.payroll.Bankreconciliationdetails;
import com.onward.persistence.payroll.Regionmaster;
import com.onward.persistence.payroll.Voucher;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.upload.FormFile;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author root
 */
public class BankEntryServiceImpl extends OnwardAction implements BankEntryService {

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
    public Map getBankDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        System.out.println("************************** BankEntryServiceImpl Class getBankDetails method is calling *********************************");
        Map bankmap = new HashMap();
        Map banknamelist = new HashMap();
        try {
            Criteria bankCrit = session.createCriteria(Bankledger.class);
            bankCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            List bankList = bankCrit.list();
            if (bankList.size() > 0) {
                for (int i = 0; i < bankList.size(); i++) {
                    Bankledger bankledger = (Bankledger) bankList.get(i);
                    banknamelist.put(bankledger.getCode(), bankledger.getBankname());
                }
            }
            bankmap.put("banklist", banknamelist);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bankmap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map uploadReconciliationFile(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String bankname, String serverPath) {
        System.out.println("************************** BankEntryServiceImpl Class uploadReconciliationFile method is calling *********************************");
        Map resultMap = new HashMap();
        int ryear = Integer.valueOf(year);
        int rmonth = Integer.valueOf(month);
        String periodcode = (String) request.getSession(false).getAttribute("financialYear");
        Bankreconciliationdetails brd = null;
        Transaction transaction = session.beginTransaction();
        try {
            Map xlmap = new ReadExcel().getData(serverPath);
            Iterator itr = xlmap.entrySet().iterator();
            while (itr.hasNext()) {
                // <editor-fold defaultstate="collapsed" desc="Map Iterator">
                Map.Entry e = (Map.Entry) itr.next();
                int key = (Integer) e.getKey();
                List list = (List) e.getValue();
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    // <editor-fold defaultstate="collapsed" desc="Iterator">
                    brd = new Bankreconciliationdetails();

                    brd.setId(getBankReconciliationDetailsId(session, LoggedInRegion));

                    String val = (String) it.next();
                    if (val != null) {
                        val = val.trim();
                        if (val.length() > 0) {
                            brd.setTransactiondate(DateParser.postgresDate1(val));
                        }
                    }

                    val = (String) it.next();
                    if (val != null) {
                        val = val.trim();
                        if (val.length() > 0) {
                            brd.setValuedate(DateParser.postgresDate1(val));
                        }
                    }

                    val = (String) it.next();
                    if (val != null) {
                        val = val.trim();
                        if (val.length() > 0) {
                            brd.setDescription(val);
                        }
                    }

                    val = (String) it.next();
                    if (val != null) {
                        val = val.trim();
                        if (val.length() > 0) {
                            if (isNumeric(val)) {
                                double ref = Double.valueOf(val);
                                long lref = Math.round(ref);
                                brd.setReferenceno(String.valueOf(lref));
                            } else {
                                brd.setReferenceno(val);
                            }
                        }
                    }

                    val = (String) it.next();
                    if (val != null) {
                        val = val.trim();
                        if (val.length() > 0) {
                            if (isNumeric(val)) {
                                double branch = Double.valueOf(val);
                                long lbranch = Math.round(branch);
                                brd.setBranchcode(String.valueOf(lbranch));
                            } else {
                                brd.setBranchcode(val);
                            }
                        }
                    }

                    val = (String) it.next();
                    if (val != null) {
                        val = val.trim();
                        if (val.length() > 0) {
                            val = val.replaceAll(",", "");
                            double amo = Double.valueOf(val);
                            BigDecimal damount = new BigDecimal(amo);
                            brd.setDebit(damount);
                        }
                    }

                    val = (String) it.next();
                    if (val != null) {
                        val = val.trim();
                        if (val.length() > 0) {
                            val = val.replaceAll(",", "");
                            double amo = Double.valueOf(val);
                            BigDecimal camount = new BigDecimal(amo);
                            brd.setCredit(camount);
                        }
                    }

                    val = (String) it.next();
                    if (val != null) {
                        val = val.trim();
                        System.out.println("Region = " + val);
                    }

                    val = (String) it.next();
                    if (val != null) {
                        val = val.trim();
                        if (val.length() > 0) {
                            val = val.replaceAll(",", "");
                            double amo = Double.valueOf(val);
                            BigDecimal balamount = new BigDecimal(amo);
                            brd.setBalance(balamount);
                        }
                    }

                    Bankledger bankledger = null;
                    Criteria blCrit = session.createCriteria(Bankledger.class);
                    blCrit.add(Restrictions.sqlRestriction("code='" + bankname + "'"));
                    List bankList = blCrit.list();
                    if (bankList.size() > 0) {
                        bankledger = (Bankledger) bankList.get(0);
                        brd.setBankledger(bankledger);
                    }

                    brd.setMonth(rmonth);
                    brd.setYear(ryear);

                    Regionmaster regionmaster = null;
                    Criteria rmCrit = session.createCriteria(Regionmaster.class);
                    rmCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
                    List regionList = rmCrit.list();
                    if (regionList.size() > 0) {
                        regionmaster = (Regionmaster) regionList.get(0);
                        brd.setRegionmaster(regionmaster);
                    }

                    Accountingyear accountingyear = null;
                    Criteria ayCrit = session.createCriteria(Accountingyear.class);
                    ayCrit.add(Restrictions.sqlRestriction("id='" + periodcode + "'"));
                    List ayList = ayCrit.list();
                    if (ayList.size() > 0) {
                        accountingyear = (Accountingyear) ayList.get(0);
                        brd.setAccountingyear(accountingyear);
                    }

                    brd.setCancelled(Boolean.FALSE);
                    session.save(brd);
                    if (key % 20 == 0) {
                        session.flush();
                        session.clear();
                    }
                    // </editor-fold>
                }
                // </editor-fold>
            }
//            if (!transaction.wasCommitted()) {
                transaction.commit();
//            }
//            transaction.commit();
            resultMap.put("result", "Successfully Uploaded");
        } catch (Exception ex) {
            transaction.rollback();
            resultMap.put("ERROR", "File Uploaded Error!");
            ex.printStackTrace();
        }
        return resultMap;
    }

    public synchronized String getBankReconciliationDetailsId(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getBankreconciliationdetailsid();
//                System.out.println("maxStr = " + maxNoStr);
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setBankreconciliationdetailsid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
//            System.out.println("BudgetID = " + maxStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return maxStr;
    }

    private boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
