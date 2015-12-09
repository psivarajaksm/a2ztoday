/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.EmployeeLoansAndAdvances;
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
public class EmployeeLoansandAdvancesAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    EmployeeLoansAndAdvances employeeLoansAndAdvancesObj = (EmployeeLoansAndAdvances) injector.getInstance(EmployeeLoansAndAdvances.class);

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward loansandAdvancesPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("loansandAdvancesPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward transactionDeductionPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("transactionDeductionPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward professionalTaxDeductionPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("professionalTaxDeductionPage");
    }

    /**
     *
     * @param request
     * @param response
     * @return List of Loan Names
     */
    public Map loadLoanNames(String paycodetype, HttpServletRequest request, HttpServletResponse response) {
        return employeeLoansAndAdvancesObj.loadLoanNames(null, request, response, null, null, paycodetype);
    }

    /**
     *
     * @param epfno Employee Provident Fund Number
     * @param request
     * @param response
     * @return Employee details and Display Employee's Current Loan Details
     */
    public Map getEmployeeDetailsNLoanDetails(String epfno, HttpServletRequest request, HttpServletResponse response) {
        return employeeLoansAndAdvancesObj.getEmployeeDetailsNLoanDetails(null, request, response, null, null, epfno);
    }

    /**
     *
     * @param epfno Employee Provident Fund Number
     * @param loanname Selected Loan Name
     * @param loandate Loan Date Accepted
     * @param loanamount Loan Amount
     * @param noofinstallment Total Number of Loan Installment
     * @param firstinstallmentamt First Loan Installment
     * @param successiveinstallmentamt Successive Nth Installment
     * @param fileno File Number
     * @param request
     * @param response
     * @return result of the transaction of Loan process
     */
    public Map saveEmployeeLoan(String loanid,String epfno, String loanname, String loandate, String loanamount, String noofinstallment, String firstinstallmentamt, String successiveinstallmentamt, String fileno, String completedins, String loanbalance, HttpServletRequest request, HttpServletResponse response) {
        String regionCode = "";
        return employeeLoansAndAdvancesObj.saveEmployeeLoan(null, request, response, null, null, regionCode, loanid, epfno, loanname, loandate, loanamount, noofinstallment, firstinstallmentamt, successiveinstallmentamt, fileno,completedins, loanbalance);
    }

    public Map getLoanDetailsForModification(String loanId,String epfno, HttpServletRequest request, HttpServletResponse response) {
        return employeeLoansAndAdvancesObj.getLoanDetailsForModification(null, request, response, null, null, loanId,epfno);
    }

    
}
