/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

/**
 *
 * @author Sivaraja
 */
public interface Tax {

    public Map getPurchaseTaxTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getCSTTaxTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getSalesTaxTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);

    public Map getCstTaxTableDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);

    public Map getPurchaseTaxTableDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);

    public Map loadCompanyNames(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map loadCommodityNames(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map loadCompanyAndCommodityNames(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map saveVatOnPurchase(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String vatonpurchaseid, String month, String year, String companyname, String billdate, String billno, String commodityname, String quantity, String rate, String amount, String vat, String vatamt, String total);

    public Map saveCSTTax(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String csttaxid, String month, String year, String companyname, String commodityname, String quantity, String rate, String amount, String vat, String vatamt, String total,String tinnumber,String cstno);
//    public Map saveCSTTax(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String vatonpurchaseid, String month, String year, String companyname, String billdate, String billno, String commodityname, String quantity, String rate, String amount, String vat, String vatamt, String total);

    public Map saveVatOnSales(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String salesid, String month, String year, String commodityid, String quantity, String rate, String ratevalue, String taxpercentage, String taxamount, String totalamount);

    public Map getPurchaseonTaxDetailsForModi(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String purchaseonvatid);

    public Map getCstTaxDetailsForModi(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String csttaxid);

    public Map getTINnumber(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String partyid);
    
    public Map getDetailsForPurahseVat(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String commodityId);
}
