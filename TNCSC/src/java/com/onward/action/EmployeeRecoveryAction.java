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
public class EmployeeRecoveryAction extends OnwardAction {

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
    public ActionForward revoceryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("revoceryPage");
    }
    /**
     *
     * @param request
     * @param response
     * @return              List of Loan Names
     */
    public Map loadLoanNames(String paycodetype,HttpServletRequest request, HttpServletResponse response) {
        return employeeLoansAndAdvancesObj.loadLoanNames(null, request, response, null, null,paycodetype);
    }
    /**
     *
     * @param employeeid                Employee Provident Fund Number
     * @param request
     * @param response
     * @return                          Employee details and Display Employee's Current Recovery Details
     */
    public Map getEmployeeDetailsNRecoveryDetails(String employeeid, HttpServletRequest request, HttpServletResponse response) {
        
        return employeeLoansAndAdvancesObj.getEmployeeDetailsNRecoveryDetails(null, request, response, null, null, employeeid);
    }

    /**
     *
     * @param employeeid                                Employee Provident Fund Number
     * @param recoveryname                              Selected Recovery Name
     * @param recoverydate                              Recovery Date Accepted
     * @param recoveryamount                            Recovery Amount
     * @param noofinstallment                           Total Number of Recovery Installment
     * @param firstinstallmentamt                       First Recovery Installment
     * @param successiveinstallmentamt                  Successive Nth Recovery Installment
     * @param fileno                                    File Number
     * @param request
     * @param response
     * @return                                          result of the transaction of Recovery process
     */
    public Map saveEmployeeRecovery(String employeeid, String recoveryname, String recoverydate, String recoveryamount, String noofinstallment, String firstinstallmentamt, String successiveinstallmentamt, String fileno, HttpServletRequest request, HttpServletResponse response) {
        
        return employeeLoansAndAdvancesObj.saveEmployeeRecovery(null, request, response, null, null, null, employeeid, recoveryname, recoverydate, recoveryamount, noofinstallment, firstinstallmentamt, successiveinstallmentamt, fileno);
    }
}
