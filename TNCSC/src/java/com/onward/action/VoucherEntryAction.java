/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.*;
import com.onward.dao.AccountVoucherService;
import com.onward.dao.OeslModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sivaraja_p
 */
public class VoucherEntryAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    AccountVoucherService accountVoucherService = (AccountVoucherService) injector.getInstance(AccountVoucherService.class);

    public ActionForward paymentVoucherEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("paymentVoucherEntryPage");
    }

    public ActionForward receiptVoucherEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("receiptVoucherEntryPage");
    }

    public ActionForward journalVoucherEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("journalVoucherEntryPage");
    }

    public ActionForward paymentToBankPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("paymentToBankPage");
    }

    public ActionForward receiptFromBankPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("receiptFromBankPage");
    }

    public ActionForward paymentVoucherModifyPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("paymentVoucherModifyPage");
    }

    public ActionForward receiptVoucherModifyPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("receiptVoucherModifyPage");
    }

    public ActionForward journalVoucherModifyPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("journalVoucherModifyPage");
    }

    public ActionForward bankEntriesModifyPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("bankEntriesModifyPage");
    }

    public ActionForward PaybillVoucherEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String category = accountVoucherService.getPayrollVoucherCategory(null, request, response, null, null);
        request.getSession().setAttribute("category", category);
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("PaybillVoucherEntryPage");
    }

    public String getYear(int startingyear, int endingyear) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("<select ");
        buffer.append("class=\"combobox\" ");
        buffer.append("name=\"year\" id=\"year\">");
        for (int i = startingyear; i <= endingyear; i++) {
            buffer.append("<option value=\"");
            buffer.append(i);
            buffer.append("\">");
            buffer.append(i);
            buffer.append("</option>");
        }
        buffer.append("</select>");
        return buffer.toString();
    }
}