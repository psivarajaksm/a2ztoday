/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.epf.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.action.OnwardAction;
import com.onward.dao.OeslModule;
import com.onward.epf.dao.EPFTransactionService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sivaraja_p
 */
public class EPFLoanAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    EPFTransactionService epfService = (EPFTransactionService) injector.getInstance(EPFTransactionService.class);

    public ActionForward loanapplicationreceiptpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("loanapplicationreceiptpage");
    }

    public ActionForward loanworkingsheetpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("loanworkingsheetpage");
    }

    public ActionForward loanapprovalpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("loanapprovalpage");
    }

    public ActionForward loanvoucherpreparation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("loanvoucherpreparation");
    }

    public ActionForward finalsettlementworkingsheetpag(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("finalsettlementworkingsheetpage");
    }

    public ActionForward loanaccountslippage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("loanaccountslippage");
    }

    public ActionForward loanchecklistpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("loanchecklistpage");
    }

    public Map getEpfLoanApplReceipt(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getEpfLoanApplReceipt(null, request, response, null, null, month, year);
    }

    public Map getEpfLoanApplications(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getEpfLoanApplications(null, request, response, null, null, month, year);
    }
    
    public Map getEpfLoanApplicationsForWorkingSheet(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getEpfLoanApplicationsForWorkingSheet(null, request, response, null, null, month, year);
    }

    public Map getEpfLoanTransaction(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getEpfLoanTransaction(null, request, response, null, null, month, year);
    }

    public Map getEpfLoanForApproval(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getEpfLoanForApproval(null, request, response, null, null, month, year);
    }

    public Map loadLoanTypes(HttpServletRequest request, HttpServletResponse response) {
        return epfService.loadLoanTypes(null, request, response, null, null);
    }

    public Map saveLoanApplicationReceiptEntry(String epfloanappreceiptid, String epfnumber, String applicationdate, String loantype, String loanamount, String tapalno, String mmonth, String myear,String isfinalsettlement,String loaninstallment, HttpServletRequest request, HttpServletResponse response) {

        return epfService.saveLoanApplicationReceiptEntry(null, request, response, null, null, epfloanappreceiptid, epfnumber, applicationdate, loantype, loanamount, tapalno, mmonth, myear,isfinalsettlement,loaninstallment);
    }

    public Map getEmployeeDetails(String epfo, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getEmployeeDetails(null, request, response, null, null, epfo);
    }

    public Map getEPFLoanApplicationReceiptDetails(String epfloanid, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getEPFLoanApplicationReceiptDetails(null, request, response, null, null, epfloanid);
    }

    public Map getWorkingSheet(String loanids, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getWorkingSheet(null, request, response, null, null, loanids);
    }
}
