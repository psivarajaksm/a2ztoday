/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.dao.GlobalDBOpenCloseAndUserPrivilages;
import com.onward.persistence.payroll.Accountingyear;
import com.onward.persistence.payroll.Commoditymaster;
import com.onward.persistence.payroll.Vatonsales;
import com.onward.dao.SequenceNumberGenerator;
//import com.onward.persistence.payroll.*;
import com.onward.persistence.payroll.Csttaxtable;
import com.onward.persistence.payroll.Partyledger;
import com.onward.persistence.payroll.Paycodemaster;
import com.onward.persistence.payroll.Vatonpurchase;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sivaraja_p
 */
public class TaxImpl extends OnwardAction implements Tax {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPurchaseTaxTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"5%\">Month</th>");
            stringBuff.append("<th width=\"5%\">Year</th>");
            stringBuff.append("<th width=\"5%\">company name</th>");
            stringBuff.append("<th width=\"5%\">Tin No</th>");
            stringBuff.append("<th width=\"5%\">Bill No</th>");
            stringBuff.append("<th width=\"5%\">Bill Date</th>");
            stringBuff.append("<th width=\"5%\">Commodity Name</th>");
            stringBuff.append("<th width=\"5%\">Rate</th>");
            stringBuff.append("<th width=\"5%\">Quantity</th>");
            stringBuff.append("<th width=\"5%\">Amount</th>");
            stringBuff.append("<th width=\"5%\">Tax %</th>");
            stringBuff.append("<th width=\"5%\">Tax Amount</th>");

            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"taxentryadd\" id=\"taxentryadd\" value=\"Add\"  onclick=\"showTaxEntryForm();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            resultMap.put("period", accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPurchaseTaxTableDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"5%\">Month</th>");
            stringBuff.append("<th width=\"5%\">Year</th>");
            stringBuff.append("<th width=\"5%\">company name</th>");
            stringBuff.append("<th width=\"5%\">Tin No</th>");
            stringBuff.append("<th width=\"5%\">Bill No</th>");
            stringBuff.append("<th width=\"5%\">Bill Date</th>");
            stringBuff.append("<th width=\"5%\">Commodity Name</th>");
            stringBuff.append("<th width=\"5%\">Rate</th>");
            stringBuff.append("<th width=\"5%\">Quantity</th>");
            stringBuff.append("<th width=\"5%\">Amount</th>");
            stringBuff.append("<th width=\"5%\">Tax %</th>");
            stringBuff.append("<th width=\"5%\">Tax Amount</th>");
            stringBuff.append("<th width=\"5%\">Total</th>");
            stringBuff.append("<th width=\"5%\">Modify</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            try {
                System.out.println(month);
                System.out.println(year);
                System.out.println(LoggedInRegion);
                Criteria vatOnPurchaseCrit = session.createCriteria(Vatonpurchase.class);
                vatOnPurchaseCrit.add(Restrictions.sqlRestriction("regionmaster = '" + LoggedInRegion + "' "));
                vatOnPurchaseCrit.add(Restrictions.sqlRestriction("month = " + month));
                vatOnPurchaseCrit.add(Restrictions.sqlRestriction("year = " + year));
                vatOnPurchaseCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List<Vatonpurchase> vatOnPurchaseList = vatOnPurchaseCrit.list();
                for (Vatonpurchase vonp : vatOnPurchaseList) {


                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + vonp.getId() + "</td>");
                    stringBuff.append("<td >" + vonp.getMonth() + "</td>");
                    stringBuff.append("<td >" + vonp.getYear() + "</td>");
                    if (vonp.getPartyledger() != null) {
                        stringBuff.append("<td >" + vonp.getPartyledger().getPartyname() + "</td>");
                    } else {
                        stringBuff.append("<td ></td>");
                    }
                    if (vonp.getPartyledger() != null) {
                        stringBuff.append("<td >" + vonp.getPartyledger().getTinno() + "</td>");
                    } else {
                        stringBuff.append("<td ></td>");
                    }
                    stringBuff.append("<td >" + vonp.getBillno() + "</td>");
                    stringBuff.append("<td >" + vonp.getBilldate() + "</td>");
                    if (vonp.getCommoditymaster() != null) {
                        stringBuff.append("<td >" + vonp.getCommoditymaster().getName() + "</td>");
                    } else {
                        stringBuff.append("<td ></td>");
                    }
                    stringBuff.append("<td >" + vonp.getRate() + "</td>");
                    stringBuff.append("<td >" + vonp.getQuantity() + "</td>");
                    stringBuff.append("<td >" + vonp.getAmount() + "</td>");
                    stringBuff.append("<td >" + vonp.getTaxpercentage() + "</td>");
                    stringBuff.append("<td >" + vonp.getTaxamount() + "</td>");
                    stringBuff.append("<td >" + vonp.getTotamount() + "</td>");
                    stringBuff.append("<td ><input type=\"radio\" name=\"loancode\" id=\"" + vonp.getId() + "\" onclick=\"setSetPurchaseTaxId('" + vonp.getId() + "')\"></td>");
                    stringBuff.append("</tr>");
                }
            } catch (Exception e) {
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"taxentryadd\" id=\"taxentryadd\" value=\"Add\"  onclick=\"showTaxEntryForm();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            resultMap.put("period", accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getSalesTaxTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        int i = 1;

        try {
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"5%\">Commodity Name</th>");
            stringBuff.append("<th width=\"5%\">Quantity</th>");
            stringBuff.append("<th width=\"5%\">Rate</th>");
            stringBuff.append("<th width=\"5%\">Rate Value</th>");
            stringBuff.append("<th width=\"5%\">Tax Percentage</th>");
            stringBuff.append("<th width=\"5%\">Tax Amount</th>");
            stringBuff.append("<th width=\"5%\">Total Amount</th>");
            stringBuff.append("<th width=\"5%\">Modify</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria commdityCrit = session.createCriteria(Commoditymaster.class);
            commdityCrit.add(Restrictions.sqlRestriction("vatsales is true"));
            commdityCrit.addOrder(Order.asc("commodityorder"));
            List<Commoditymaster> commdityList = commdityCrit.list();
            if (commdityList.size() > 0) {
                for (Commoditymaster commoditymasterObj : commdityList) {

                    Criteria salesCrit = session.createCriteria(Vatonsales.class);
                    salesCrit.add(Restrictions.sqlRestriction("commodity='" + commoditymasterObj.getId() + "'"));
                    salesCrit.add(Restrictions.sqlRestriction("regionmaster = '" + LoggedInRegion + "' "));
                    salesCrit.add(Restrictions.sqlRestriction("month=" + month));
                    salesCrit.add(Restrictions.sqlRestriction("year=" + year));
                    salesCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List<Vatonsales> vatonsalesList = salesCrit.list();
                    if (vatonsalesList.size() > 0) {
                        Vatonsales vatonsalesObj = vatonsalesList.get(0);
                        stringBuff.append("<tr>");
                        stringBuff.append("<td>" + i + "</td>");
                        stringBuff.append("<td>" + commoditymasterObj.getCode() + " - " + commoditymasterObj.getName() + "</td>");
                        stringBuff.append("<td align=\"right\">" + vatonsalesObj.getQuantity() + "</td>");
                        stringBuff.append("<td align=\"right\">" + vatonsalesObj.getRate() + "</td>");
                        stringBuff.append("<td align=\"right\">" + vatonsalesObj.getAmount() + "</td>");
//                        stringBuff.append("<td align=\"right\">" + vatonsalesObj.getTaxpercentage() + "</td>");
                        stringBuff.append("<td align=\"right\">" + commoditymasterObj.getVatpercentage() + "</td>");
                        stringBuff.append("<td align=\"right\">" + vatonsalesObj.getTaxamount() + "</td>");
                        stringBuff.append("<td align=\"right\">" + vatonsalesObj.getTotamount() + "</td>");
                        if (commoditymasterObj.getIsmodimyonpurchase()) {
                            stringBuff.append("<td align=\"center\"><input type=\"radio\" name=\"salescode\" id=\"" + commoditymasterObj.getId() + "\" "
                                    + "onclick=\"setSalesDetails('" + commoditymasterObj.getId() + "','" + commoditymasterObj.getName() + "','"
                                    + vatonsalesObj.getId() + "','" + vatonsalesObj.getQuantity() + "','" + vatonsalesObj.getRate() + "','"
                                    + vatonsalesObj.getAmount() + "','" + commoditymasterObj.getVatpercentage() + "','"
                                    + vatonsalesObj.getTaxamount() + "','" + vatonsalesObj.getTotamount() + "','Yes')\"></td>").append("</tr>");
                        } else {
                            stringBuff.append("<td align=\"center\"><input type=\"radio\" name=\"salescode\" id=\"" + commoditymasterObj.getId() + "\" "
                                    + "onclick=\"setSalesDetails('" + commoditymasterObj.getId() + "','" + commoditymasterObj.getName() + "','"
                                    + vatonsalesObj.getId() + "','" + vatonsalesObj.getQuantity() + "','" + commoditymasterObj.getRate() + "','"
                                    + vatonsalesObj.getAmount() + "','" + commoditymasterObj.getVatpercentage() + "','"
                                    + vatonsalesObj.getTaxamount() + "','" + vatonsalesObj.getTotamount() + "','No')\"></td>").append("</tr>");
                        }

                        stringBuff.append("</tr>");
                        i++;

                    } else {
                        stringBuff.append("<tr>");
                        stringBuff.append("<td>" + i + "</td>");
                        stringBuff.append("<td>" + commoditymasterObj.getCode() + " - " + commoditymasterObj.getName() + "</td>");
                        stringBuff.append("<td align=\"right\">0.00</td>");
                        stringBuff.append("<td align=\"right\">0.00</td>");
                        stringBuff.append("<td align=\"right\">0.00</td>");
                        stringBuff.append("<td align=\"right\">" + commoditymasterObj.getVatpercentage() + "</td>");
                        stringBuff.append("<td align=\"right\">0.00</td>");
                        stringBuff.append("<td align=\"right\">0.00</td>");
                        if (commoditymasterObj.getIsmodimyonpurchase()) {
                            stringBuff.append("<td align=\"center\"><input type=\"radio\" name=\"salescode\" id=\"" + commoditymasterObj.getId() + "\" onclick=\"setSalesDetails('" + commoditymasterObj.getId() + "','" + commoditymasterObj.getCode() + " - " + commoditymasterObj.getName() + "',0,0,0,0,'" + commoditymasterObj.getVatpercentage() + "',0,0,'Yes')\"></td>").append("</tr>");
                        } else {
                            stringBuff.append("<td align=\"center\"><input type=\"radio\" name=\"salescode\" id=\"" + commoditymasterObj.getId() + "\" onclick=\"setSalesDetails('" + commoditymasterObj.getId() + "','" + commoditymasterObj.getCode() + " - " + commoditymasterObj.getName() + "',0,0,'" + commoditymasterObj.getRate() + "',0,'" + commoditymasterObj.getVatpercentage() + "',0,0,'No')\"></td>").append("</tr>");
                        }


                        stringBuff.append("</tr>");
                        i++;
                    }
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("salesdetails", stringBuff.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            resultMap.put("period", accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getCstTaxTableDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"5%\">Month</th>");
            stringBuff.append("<th width=\"5%\">Year</th>");
            stringBuff.append("<th width=\"5%\">company name</th>");
            stringBuff.append("<th width=\"5%\">CST No</th>");
//            stringBuff.append("<th width=\"5%\">Bill No</th>");
//            stringBuff.append("<th width=\"5%\">Bill Date</th>");
            stringBuff.append("<th width=\"5%\">Commodity Name</th>");
            stringBuff.append("<th width=\"5%\">Rate</th>");
            stringBuff.append("<th width=\"5%\">Quantity</th>");
            stringBuff.append("<th width=\"5%\">Amount</th>");
            stringBuff.append("<th width=\"5%\">Tax %</th>");
            stringBuff.append("<th width=\"5%\">Tax Amount</th>");
            stringBuff.append("<th width=\"5%\">Total</th>");
            stringBuff.append("<th width=\"5%\">Modify</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            try {
                System.out.println(month);
                System.out.println(year);
                System.out.println(LoggedInRegion);
                Criteria csttaxCrit = session.createCriteria(Csttaxtable.class);
                csttaxCrit.add(Restrictions.sqlRestriction("regionmaster = '" + LoggedInRegion + "' "));
                csttaxCrit.add(Restrictions.sqlRestriction("month = " + month));
                csttaxCrit.add(Restrictions.sqlRestriction("year = " + year));
                csttaxCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List<Csttaxtable> csttaxList = csttaxCrit.list();
                for (Csttaxtable csttaxtableObj : csttaxList) {


                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + csttaxtableObj.getId() + "</td>");
                    stringBuff.append("<td >" + csttaxtableObj.getMonth() + "</td>");
                    stringBuff.append("<td >" + csttaxtableObj.getYear() + "</td>");
                    stringBuff.append("<td >" + csttaxtableObj.getPartyledger() + "</td>");
                    stringBuff.append("<td >" + csttaxtableObj.getCstnumber() + "</td>");
//                    stringBuff.append("<td >" + csttaxtableObj.getBillno() + "</td>");
//                    stringBuff.append("<td >" + csttaxtableObj.getBilldate() + "</td>");
                    stringBuff.append("<td >" + csttaxtableObj.getCommodity() + "</td>");                    
                    stringBuff.append("<td >" + csttaxtableObj.getRate() + "</td>");
                    stringBuff.append("<td >" + csttaxtableObj.getQuantity() + "</td>");
                    stringBuff.append("<td >" + csttaxtableObj.getAmount() + "</td>");
                    stringBuff.append("<td >" + csttaxtableObj.getTaxpercentage() + "</td>");
                    stringBuff.append("<td >" + csttaxtableObj.getTaxamount() + "</td>");
                    stringBuff.append("<td >" + csttaxtableObj.getTotamount() + "</td>");
                    stringBuff.append("<td ><input type=\"radio\" name=\"loancode\" id=\"" + csttaxtableObj.getId() + "\" onclick=\"setCstTaxId('" + csttaxtableObj.getId() + "')\"></td>");
                    stringBuff.append("</tr>");
                }
            } catch (Exception e) {
            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"taxentryadd\" id=\"taxentryadd\" value=\"Add\"  onclick=\"showTaxEntryForm();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("csttaxdetails", stringBuff.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            resultMap.put("period", accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getCSTTaxTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        try {
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"5%\">Month</th>");
            stringBuff.append("<th width=\"5%\">Year</th>");
            stringBuff.append("<th width=\"5%\">company name</th>");
            stringBuff.append("<th width=\"5%\">Tin No</th>");
            stringBuff.append("<th width=\"5%\">Bill No</th>");
            stringBuff.append("<th width=\"5%\">Bill Date</th>");
            stringBuff.append("<th width=\"5%\">Commodity Name</th>");
            stringBuff.append("<th width=\"5%\">Rate</th>");
            stringBuff.append("<th width=\"5%\">Quantity</th>");
            stringBuff.append("<th width=\"5%\">Amount</th>");
            stringBuff.append("<th width=\"5%\">Tax %</th>");
            stringBuff.append("<th width=\"5%\">Tax Amount</th>");

            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            stringBuff.append("<tr >");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("<td ></td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"taxentryadd\" id=\"taxentryadd\" value=\"Add\"  onclick=\"showTaxEntryForm();\"  >" + "</td>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("voucherdetails", stringBuff.toString());
            String periodcode = (String) request.getSession(false).getAttribute("financialYear");
            resultMap.put("periodcode", periodcode);
            Accountingyear accountingyearObj = CommonUtility.getAccountingyear(session, periodcode);
            resultMap.put("period", accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadCompanyAndCommodityNames(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map partyMap = new LinkedHashMap();
        Map commodityMap = new LinkedHashMap();
        partyMap.put("0", "--Select--");
        commodityMap.put("0", "--Select--");
        String companycode = "";
        String companyame = "";
        String commoditycode = "";
        String commodityname = "";

        try {
            Criteria partyNameCrit = session.createCriteria(Partyledger.class);
            partyNameCrit.add(Restrictions.sqlRestriction("region = '" + LoggedInRegion + "' "));
            partyNameCrit.addOrder(Order.asc("partyname"));
            List<Partyledger> partyNameList = partyNameCrit.list();
            for (Partyledger lbobj : partyNameList) {
                companycode = lbobj.getCode();
                companyame = lbobj.getPartyname();
                partyMap.put(companycode, companyame);
            }
            resultMap.put("companyname", partyMap);


            Criteria commodityNameCrit = session.createCriteria(Commoditymaster.class);
            commodityNameCrit.add(Restrictions.sqlRestriction("vatpurchase is true"));
            commodityNameCrit.addOrder(Order.asc("name"));
            List<Commoditymaster> commodityNameList = commodityNameCrit.list();
            for (Commoditymaster bobj : commodityNameList) {
                commoditycode = bobj.getId();
                commodityname = bobj.getCode() + " - " + bobj.getName();
                commodityMap.put(commoditycode, commodityname);
            }
            resultMap.put("commodityname", commodityMap);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getTINnumber(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String partyid) {
        Map resultMap = new HashMap();
        try {
            Criteria partyNameCrit = session.createCriteria(Partyledger.class);
            partyNameCrit.add(Restrictions.sqlRestriction("region = '" + LoggedInRegion + "' "));
            partyNameCrit.add(Restrictions.sqlRestriction("code = '" + partyid + "' "));
            List<Partyledger> partyNameList = partyNameCrit.list();
            for (Partyledger lbobj : partyNameList) {
                resultMap.put("tinnumber", lbobj.getTinno());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadCompanyNames(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map loanMap = new LinkedHashMap();
        loanMap.put("0", "--Select--");
        String loancode = "";
        String loanname = "";
        try {
            Criteria loanNameCrit = session.createCriteria(Paycodemaster.class);
            //loanNameCrit.add(Restrictions.sqlRestriction("paycodetype = '" + paycodetype.toUpperCase() + "' "));
            loanNameCrit.addOrder(Order.asc("paycodename"));
            List<Paycodemaster> loanNameList = loanNameCrit.list();
            resultMap = new TreeMap();
            for (Paycodemaster lbobj : loanNameList) {
                loancode = lbobj.getPaycode();
                loanname = lbobj.getPaycodename();
                loanMap.put(loancode, loanname);
            }

            resultMap.put("companyname", loanMap);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadCommodityNames(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map loanMap = new LinkedHashMap();
        loanMap.put("0", "--Select--");
        String loancode = "";
        String loanname = "";
        try {
            Criteria loanNameCrit = session.createCriteria(Paycodemaster.class);
            //loanNameCrit.add(Restrictions.sqlRestriction("paycodetype = '" + paycodetype.toUpperCase() + "' "));
            loanNameCrit.addOrder(Order.asc("paycodename"));
            List<Paycodemaster> loanNameList = loanNameCrit.list();
            resultMap = new TreeMap();
            for (Paycodemaster lbobj : loanNameList) {
                loancode = lbobj.getPaycode();
                loanname = lbobj.getPaycodename();
                loanMap.put(loancode, loanname);
            }

            resultMap.put("commodityname", loanMap);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveVatOnPurchase(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String vatonpurchaseid, String month, String year, String companyname, String billdate, String billno, String commodityname, String quantity, String rate, String amount, String vat, String vatamt, String total) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            if (vatonpurchaseid.trim().length() > 0) {
                Criteria vatOnPurchaseCrit = session.createCriteria(Vatonpurchase.class);
                vatOnPurchaseCrit.add(Restrictions.sqlRestriction("id = '" + vatonpurchaseid.trim() + "' "));
                List<Vatonpurchase> vatOnPurchaseList = vatOnPurchaseCrit.list();
                if (vatOnPurchaseList.size() > 0) {
                    Vatonpurchase vatonpurchase = (Vatonpurchase) vatOnPurchaseList.get(0);

                    vatonpurchase.setAmount(new BigDecimal(amount));
                    vatonpurchase.setBilldate(postgresDate(billdate));
                    vatonpurchase.setBillno(billno);
                    vatonpurchase.setCancelled(Boolean.FALSE);
                    vatonpurchase.setCommoditymaster(CommonUtility.geCommoditymaster(session, commodityname));
                    vatonpurchase.setCreatedby(LoggedInUser);
                    vatonpurchase.setCreateddate(getCurrentDate());
                    vatonpurchase.setMonth(Integer.parseInt(month));
                    vatonpurchase.setYear(Integer.parseInt(year));
                    vatonpurchase.setPartyledger(CommonUtility.getPartyledger(session, companyname));
                    if (quantity.trim().length() > 0) {
                        vatonpurchase.setQuantity(new BigDecimal(quantity));
                    } else {
                        vatonpurchase.setQuantity(new BigDecimal(0));
                    }
                    vatonpurchase.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                    vatonpurchase.setTaxamount(new BigDecimal(vatamt));
                    vatonpurchase.setTaxpercentage(new BigDecimal(vat));
                    vatonpurchase.setTotamount(new BigDecimal(total));
                    if (rate.trim().length() > 0) {
                        vatonpurchase.setRate(new BigDecimal(rate));
                    } else {
                        vatonpurchase.setRate(new BigDecimal(0));
                    }

                    transaction = session.beginTransaction();
                    session.update(vatonpurchase);
                    transaction.commit();


                }


            } else {
                Vatonpurchase vatonpurchase = new Vatonpurchase();
                vatonpurchase.setId(SequenceNumberGenerator.getMaxSeqNumberVatonPurchase(session, LoggedInRegion));
                vatonpurchase.setAccregion(LoggedInRegion);
                vatonpurchase.setAmount(new BigDecimal(amount));
                vatonpurchase.setBilldate(postgresDate(billdate));
                vatonpurchase.setBillno(billno);
                vatonpurchase.setCancelled(Boolean.FALSE);
                vatonpurchase.setCommoditymaster(CommonUtility.geCommoditymaster(session, commodityname));
                vatonpurchase.setCreatedby(LoggedInUser);
                vatonpurchase.setCreateddate(getCurrentDate());
                vatonpurchase.setMonth(Integer.parseInt(month));
                vatonpurchase.setYear(Integer.parseInt(year));
                vatonpurchase.setPartyledger(CommonUtility.getPartyledger(session, companyname));
                if (quantity.trim().length() > 0) {
                    vatonpurchase.setQuantity(new BigDecimal(quantity));
                } else {
                    vatonpurchase.setQuantity(new BigDecimal(0));
                }
                vatonpurchase.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                vatonpurchase.setTaxamount(new BigDecimal(vatamt));
                vatonpurchase.setTaxpercentage(new BigDecimal(vat));
                vatonpurchase.setTotamount(new BigDecimal(total));
                if (rate.trim().length() > 0) {
                    vatonpurchase.setRate(new BigDecimal(rate));
                } else {
                    vatonpurchase.setRate(new BigDecimal(0));
                }
                transaction = session.beginTransaction();
                session.save(vatonpurchase);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
//    public Map saveCSTTax(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String vatonpurchaseid, String month, String year, String companyname, String billdate, String billno, String commodityname, String quantity, String rate, String amount, String vat, String vatamt, String total) {
    public Map saveCSTTax(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String csttaxid, String month, String year, String companyname, String commodityname, String quantity, String rate, String amount, String vat, String vatamt, String total,String tinnumber,String cstno) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            if (csttaxid.trim().length() > 0) {
                Criteria cstTaxCrit = session.createCriteria(Csttaxtable.class);
                cstTaxCrit.add(Restrictions.sqlRestriction("id = '" + csttaxid.trim() + "' "));
                List<Csttaxtable> cstTaxList = cstTaxCrit.list();                
                if (cstTaxList.size() > 0) {
                    Csttaxtable csttaxtableObj = (Csttaxtable) cstTaxList.get(0);

                    csttaxtableObj.setAmount(new BigDecimal(amount));
//                    vatonpurchase.setBilldate(postgresDate(billdate));
//                    vatonpurchase.setBillno(billno);
                    csttaxtableObj.setCancelled(Boolean.FALSE);
                    csttaxtableObj.setCommodity(commodityname.toUpperCase());
                    csttaxtableObj.setCreatedby(LoggedInUser);
                    csttaxtableObj.setCreateddate(getCurrentDate());
                    csttaxtableObj.setMonth(Integer.parseInt(month));
                    csttaxtableObj.setYear(Integer.parseInt(year));
                    csttaxtableObj.setPartyledger(companyname.toUpperCase());
                    csttaxtableObj.setTinnumber(tinnumber);
                    csttaxtableObj.setCstnumber(cstno);
                    if (quantity.trim().length() > 0) {
                        csttaxtableObj.setQuantity(new BigDecimal(quantity));
                    } else {
                        csttaxtableObj.setQuantity(new BigDecimal(0));
                    }
                    csttaxtableObj.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                    csttaxtableObj.setTaxamount(new BigDecimal(vatamt));
                    csttaxtableObj.setTaxpercentage(new BigDecimal(vat));
                    csttaxtableObj.setTotamount(new BigDecimal(total));
                    if (rate.trim().length() > 0) {
                        csttaxtableObj.setRate(new BigDecimal(rate));
                    } else {
                        csttaxtableObj.setRate(new BigDecimal(0));
                    }

                    transaction = session.beginTransaction();
                    session.update(csttaxtableObj);
                    transaction.commit();


                }


            } else {
                Csttaxtable csttaxObj = new Csttaxtable();
                csttaxObj.setId(SequenceNumberGenerator.getMaxSeqNumberTaxonCST(session, LoggedInRegion));
                csttaxObj.setAccregion(LoggedInRegion);
                csttaxObj.setAmount(new BigDecimal(amount));
//                vatonpurchase.setBilldate(postgresDate(billdate));
//                vatonpurchase.setBillno(billno);
                csttaxObj.setCancelled(Boolean.FALSE);
                csttaxObj.setCommodity(commodityname.toUpperCase());
                csttaxObj.setCreatedby(LoggedInUser);
                csttaxObj.setCreateddate(getCurrentDate());
                csttaxObj.setMonth(Integer.parseInt(month));
                csttaxObj.setYear(Integer.parseInt(year));
                csttaxObj.setPartyledger(companyname.toUpperCase());
                csttaxObj.setTinnumber(tinnumber);
                csttaxObj.setCstnumber(cstno);
                if (quantity.trim().length() > 0) {
                    csttaxObj.setQuantity(new BigDecimal(quantity));
                } else {
                    csttaxObj.setQuantity(new BigDecimal(0));
                }
                csttaxObj.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                csttaxObj.setTaxamount(new BigDecimal(vatamt));
                csttaxObj.setTaxpercentage(new BigDecimal(vat));
                csttaxObj.setTotamount(new BigDecimal(total));
                if (rate.trim().length() > 0) {
                    csttaxObj.setRate(new BigDecimal(rate));
                } else {
                    csttaxObj.setRate(new BigDecimal(0));
                }
                transaction = session.beginTransaction();
                session.save(csttaxObj);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveVatOnSales(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String salesid, String month, String year, String commodityid, String quantity, String rate, String ratevalue, String taxpercentage, String taxamount, String totalamount) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        Vatonsales vatonsalesObj = null;


        try {
            Criteria salesCrit = session.createCriteria(Vatonsales.class);
            salesCrit.add(Restrictions.sqlRestriction("commodity='" + commodityid + "'"));
            salesCrit.add(Restrictions.sqlRestriction("month=" + month));
            salesCrit.add(Restrictions.sqlRestriction("year=" + year));
            List<Vatonsales> vatonsalesList = salesCrit.list();

            if (vatonsalesList.size()
                    > 0) {
                vatonsalesObj = vatonsalesList.get(0);
                vatonsalesObj.setQuantity(new BigDecimal(quantity));
                vatonsalesObj.setAccregion(LoggedInRegion);
                vatonsalesObj.setRate(new BigDecimal(rate));
                vatonsalesObj.setAmount(new BigDecimal(ratevalue));
                vatonsalesObj.setTaxamount(new BigDecimal(taxamount));
                vatonsalesObj.setTaxpercentage(new BigDecimal(taxpercentage));
                vatonsalesObj.setTotamount(new BigDecimal(totalamount));
                vatonsalesObj.setCancelled(Boolean.FALSE);
                vatonsalesObj.setCreatedby(LoggedInUser);

//                java.sql.Date dbToDaysDate = new java.sql.Date(new Date().getTime());
//                System.out.println("dbToDaysDate==" + dbToDaysDate);
//
//                String toDayDate = dateToString(dbToDaysDate);
//                System.out.println("toDayDate==" + toDayDate);

                vatonsalesObj.setCreateddate(getCurrentDate());
                vatonsalesObj.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                transaction = session.beginTransaction();
                session.update(vatonsalesObj);
                transaction.commit();
            } else {
                vatonsalesObj = new Vatonsales();
                vatonsalesObj.setId(SequenceNumberGenerator.getMaxSeqNumberVatonSales(session, LoggedInRegion));
                vatonsalesObj.setMonth(Integer.parseInt(month));
                vatonsalesObj.setYear(Integer.parseInt(year));
                vatonsalesObj.setCommoditymaster(CommonUtility.geCommoditymaster(session, commodityid));
                vatonsalesObj.setQuantity(new BigDecimal(quantity));
                vatonsalesObj.setAccregion(LoggedInRegion);
                vatonsalesObj.setRate(new BigDecimal(rate));
                vatonsalesObj.setAmount(new BigDecimal(ratevalue));
                vatonsalesObj.setTaxamount(new BigDecimal(taxamount));
                vatonsalesObj.setTaxpercentage(new BigDecimal(taxpercentage));
                vatonsalesObj.setTotamount(new BigDecimal(totalamount));
                vatonsalesObj.setCancelled(Boolean.FALSE);
                vatonsalesObj.setCreatedby(LoggedInUser);
                vatonsalesObj.setCreateddate(getCurrentDate());
                vatonsalesObj.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                transaction = session.beginTransaction();
                session.save(vatonsalesObj);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            resultMap.put("ERROR", "Transaction Failed");
        }
        resultMap.put("month", month);
        resultMap.put("year", year);
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPurchaseonTaxDetailsForModi(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String purchaseonvatid) {
        Map resultMap = new HashMap();
        Criteria vatOnPurchaseCrit = session.createCriteria(Vatonpurchase.class);        
        vatOnPurchaseCrit.add(Restrictions.sqlRestriction("id = '" + purchaseonvatid + "' "));
        vatOnPurchaseCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        List<Vatonpurchase> vatOnPurchaseList = vatOnPurchaseCrit.list();

        if (vatOnPurchaseList.size()
                > 0) {
            Vatonpurchase vonp = (Vatonpurchase) vatOnPurchaseList.get(0);
            resultMap.put("vatonpurchaseid", vonp.getId());
            resultMap.put("companyname", vonp.getPartyledger().getCode());
            resultMap.put("billdate", dateToString(vonp.getBilldate()));
            resultMap.put("billno", vonp.getBillno());
            resultMap.put("commodityname", vonp.getCommoditymaster().getId());
            resultMap.put("quantity", vonp.getQuantity());
            resultMap.put("rate", vonp.getRate());
            resultMap.put("amount", vonp.getAmount());
            resultMap.put("vat", vonp.getTaxpercentage());
            resultMap.put("vatamt", vonp.getTaxamount());
            resultMap.put("total", vonp.getTotamount());
            resultMap.put("tinnumber", vonp.getPartyledger().getTinno());
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getCstTaxDetailsForModi(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String csttaxid) {
        Map resultMap = new HashMap();
        Criteria cstTaxCrit = session.createCriteria(Csttaxtable.class);
        cstTaxCrit.add(Restrictions.sqlRestriction("id = '" + csttaxid + "' "));
        cstTaxCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        List<Csttaxtable> cstTaxList = cstTaxCrit.list();

        if (cstTaxList.size()
                > 0) {
            Csttaxtable csttaxtableObj = (Csttaxtable) cstTaxList.get(0);
            resultMap.put("csttaxid", csttaxtableObj.getId());
            resultMap.put("companyname", csttaxtableObj.getPartyledger());
//            resultMap.put("billdate", dateToString(csttaxtableObj.getBilldate()));
//            resultMap.put("billno", csttaxtableObj.getBillno());
            resultMap.put("commodityname", csttaxtableObj.getCommodity());
            resultMap.put("quantity", csttaxtableObj.getQuantity());
            resultMap.put("rate", csttaxtableObj.getRate());
            resultMap.put("amount", csttaxtableObj.getAmount());
            resultMap.put("vat", csttaxtableObj.getTaxpercentage());
            resultMap.put("vatamt", csttaxtableObj.getTaxamount());
            resultMap.put("total", csttaxtableObj.getTotamount());
            resultMap.put("tinnumber", csttaxtableObj.getTinnumber());
            resultMap.put("cstno", csttaxtableObj.getCstnumber());
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDetailsForPurahseVat(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String commodityId) {
        Map resultMap = new HashMap();
        Criteria commodityCrit = session.createCriteria(Commoditymaster.class);
        commodityCrit.add(Restrictions.sqlRestriction("id = '" + commodityId + "' "));
        List<Commoditymaster> commodityList = commodityCrit.list();
        if (commodityList.size() > 0) {
            Commoditymaster commmast = (Commoditymaster) commodityList.get(0);
            resultMap.put("vatper", commmast.getVatpercentage());
            resultMap.put("rate", commmast.getRate());
            if (commmast.getIsmodimyonpurchase()) {
                resultMap.put("ismodify", "yes");
            } else {
                resultMap.put("ismodify", "no");
            }

        }
        return resultMap;
    }
}
