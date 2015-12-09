/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.accounts.dao.Tax;
import com.onward.action.OnwardAction;
import com.onward.dao.OeslModule;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author root
 */
public class TaxAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    Tax taxService = (Tax) injector.getInstance(Tax.class);

    public ActionForward purchaseTaxPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("purchaseTaxPage");
    }

    public ActionForward salesTaxPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("salesTaxPage");
    }

    public ActionForward cstTaxPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("cstTaxPage");
    }

    public Map getPurchaseTaxTable(HttpServletRequest request, HttpServletResponse response) {
        return taxService.getPurchaseTaxTable(null, request, response, null, null);
    }

    public Map getCSTTaxTable(HttpServletRequest request, HttpServletResponse response) {
        return taxService.getCSTTaxTable(null, request, response, null, null);
    }

    public Map getSalesTaxTable(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        return taxService.getSalesTaxTable(null, request, response, null, null, month, year);
    }
    public Map getCstTaxTableDetails(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        return taxService.getCstTaxTableDetails(null, request, response, null, null, month, year);
    }

    public Map getPurchaseTaxTableDetails(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        return taxService.getPurchaseTaxTableDetails(null, request, response, null, null, month, year);
    }

    public Map loadCompanyAndCommodityNames(HttpServletRequest request, HttpServletResponse response) {
        return taxService.loadCompanyAndCommodityNames(null, request, response, null, null);
    }

    public Map saveVatOnPurchase(String vatonpurchaseid, String month, String year, String companyname, String billdate, String billno, String commodityname, String quantity, String rate, String amount, String vat, String vatamt, String total, HttpServletRequest request, HttpServletResponse response) {
        return taxService.saveVatOnPurchase(null, request, response, null, null, vatonpurchaseid, month, year, companyname, billdate, billno, commodityname, quantity, rate, amount, vat, vatamt, total);
    }

    public Map saveCSTTax(String csttaxid, String month, String year, String companyname, String commodityname, String quantity, String rate, String amount, String vat, String vatamt, String total,String tinnumber,String cstno, HttpServletRequest request, HttpServletResponse response) {
        return taxService.saveCSTTax(null, request, response, null, null, csttaxid, month, year, companyname, commodityname, quantity, rate, amount, vat, vatamt, total,tinnumber,cstno);
    }

    public Map saveVatOnSales(String salesid, String month, String year, String commodityid, String quantity, String rate, String ratevalue, String taxpercentage, String taxamount, String totalamount, HttpServletRequest request, HttpServletResponse response) {
        return taxService.saveVatOnSales(null, request, response, null, null, salesid, month, year, commodityid, quantity, rate, ratevalue, taxpercentage, taxamount, totalamount);
    }

    public Map getPurchaseonTaxDetailsForModi(String vatonpurchaseid, HttpServletRequest request, HttpServletResponse response) {
        return taxService.getPurchaseonTaxDetailsForModi(null, request, response, null, null, vatonpurchaseid);
    }

    public Map getCstTaxDetailsForModi(String csttaxid, HttpServletRequest request, HttpServletResponse response) {
        return taxService.getCstTaxDetailsForModi(null, request, response, null, null, csttaxid);
    }

    public Map getTINnumber(String partyid, HttpServletRequest request, HttpServletResponse response) {
        return taxService.getTINnumber(null, request, response, null, null, partyid);
    }
    public Map getDetailsForPurahseVat(String commodityId, HttpServletRequest request, HttpServletResponse response) {
        return taxService.getDetailsForPurahseVat(null, request, response, null, null, commodityId);
    }
}
