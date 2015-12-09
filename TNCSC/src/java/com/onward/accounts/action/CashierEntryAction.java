/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.accounts.dao.CashierEntryService;
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
public class CashierEntryAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    CashierEntryService cashierEntryService = (CashierEntryService) injector.getInstance(CashierEntryService.class);

    public ActionForward cashierEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("cashierEntryPage");
    }

    public ActionForward cashBookChangePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("cashBookChangePage");
    }

    public ActionForward voucherCancellationPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("voucherCancellationPage");
    }

    public ActionForward paymentRealizationEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("paymentRealizationEntryPage");
    }

    public ActionForward receiptRealizationEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("receiptRealizationEntryPage");
    }

    public ActionForward remittanceEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("remittanceEntryPage");
    }

    public ActionForward adminremittanceEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("adminremittanceEntryPage");
    }

    public ActionForward chequeRegisterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("chequeRegisterPage");
    }

    public ActionForward voucherDateNoChangePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("voucherDateNoChangePage");
    }

    public ActionForward interRegionSuspenseReconcilationPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("interRegionSuspenseReconcilationPage");
    }

    public ActionForward interRegionSuspenseReconciledPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("interRegionSuspenseReconciledPage");
    }

    public Map getVoucherDetails(String voucherno, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getVoucherDetails(null, request, response, null, null, voucherno);
    }

    public Map getVoucherDetailsForModi(String voucherid, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getVoucherDetailsForModi(null, request, response, null, null, voucherid);
    }

    public Map getAccountsCodeListPartyDetails(HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getAccountsCodeListPartyDetails(null, request, response, null, null);
    }

    public Map saveVoucherDetails(String voucherType, String voucherid, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String vouchernumber, String voucherdate, String chequedate, String partyalias, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.saveModifiedVoucherDetails(null, request, response, null, null, voucherType, voucherid, partycode, partyamount, partypaymentmode, refno, bankcode, vouchernumber, voucherdate, chequedate, partyalias);

    }

    public Map getAccountBook(HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getAccountBook(null, request, response, null, null);
    }

    public Map loadBanks(HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.loadBanks(null, request, response, null, null);
    }

    public Map getVoucherAccountsDetails(String accounttype, String accountdate, String accountbooktype, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getVoucherAccountsDetails(null, request, response, null, null, accounttype, accountdate, accountbooktype);
    }

    public Map saveAccountBookChange(String accountbooktype, String voucherids, String accountdate, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.saveAccountBookChange(null, request, response, null, null, accountbooktype, voucherids, accountdate);
    }

    public Map getVoucherDetailsForCancel(String accounttype, String accountdate, String accountbooktype, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getVoucherDetailsForCancel(null, request, response, null, null, accounttype, accountdate, accountbooktype);
    }

    public Map saveVoucherCancellation(String accountbooktype, String voucherids, String accountdate, String vouchertype, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.saveVoucherCancellation(null, request, response, null, null, accountbooktype, voucherids, accountdate, vouchertype);
    }

    public Map getReceiptDetails(HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getReceiptDetails(null, request, response, null, null);
    }

    public Map saveReceiptRealizationDate(String recpayid, String realizationdate, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.saveReceiptRealizationDate(null, request, response, null, null, recpayid, realizationdate);
    }

    public Map getPaymentDetails(String period, String voucherdatefrom, String voucherdateto, String book, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getPaymentDetails(null, request, response, null, null, period, voucherdatefrom, voucherdateto, book);
    }

    public Map savePaymentRealizationDate(String recpayid, String realizationdate, String period, String voucherdatefrom, String voucherdateto, String book, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.savePaymentRealizationDate(null, request, response, null, null, recpayid, realizationdate, period, voucherdatefrom, voucherdateto, book);
    }

    public Map getChequeRegister(String period, String voucherdatefrom, String voucherdateto, String book, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getChequeRegister(null, request, response, null, null, period, voucherdatefrom, voucherdateto, book);
    }

    public Map changeChequeDetails(String recpayid, String chequedate, String period, String voucherdatefrom, String voucherdateto, String book, String chequeno, String accbank, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.changeChequeDetails(null, request, response, null, null, recpayid, chequedate, period, voucherdatefrom, voucherdateto, book, chequeno, accbank);
    }

    public Map getVoucherDetailsForModify(String fromdate, String todate, String accountbooktype, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getVoucherDetailsForModify(null, request, response, null, null, fromdate, todate, accountbooktype);
    }

    public Map changeVoucherDateandnoDetails(String voucherid, String voucherno, String voucherapproveddate, String fromdate, String todate, String accbook, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.changeVoucherDateandnoDetails(null, request, response, null, null, voucherid, voucherno, voucherapproveddate, fromdate, todate, accbook);
    }

    public Map getRemittanceDetails(String fromdate, String todate, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getRemittanceDetails(null, request, response, null, null, fromdate, todate);
    }

    public Map getRemittanceEntryDetails(String fromdate, String todate, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getRemittanceEntryDetails(null, request, response, null, null, fromdate, todate);
    }

    public Map saveRemittanceDate(String challanid, String remittancedate, String fromdate, String todate, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.saveRemittanceDate(null, request, response, null, null, challanid, remittancedate, fromdate, todate);
    }

    public ActionForward paymentRealizationAbstractEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("paymentRealizationAbstractEntryPage");
    }

    public Map getPaymentAbstractDetails(String period, String book, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.getPaymentAbstractDetails(null, request, response, null, null, period, book);
    }

    public Map savePaymentRealizationAbstractDate(String recpayid, String realizationdate, String period, String book, String dbtype, HttpServletRequest request, HttpServletResponse response) {
        return cashierEntryService.savePaymentRealizationAbstractDate(null, request, response, null, null, recpayid, realizationdate, period, book, dbtype);
    }
}
