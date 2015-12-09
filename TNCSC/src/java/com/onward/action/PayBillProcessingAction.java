/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.EmployeePayBillProcess;
import com.onward.dao.OeslModule;
import com.onward.util.PayrollProcessThread;
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
public class PayBillProcessingAction extends OnwardAction {

    public ActionForward billsProcessingPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PayrollProcessThread PayrollProcessThread = new PayrollProcessThread("Pay Roll Process", request, response);
        PayrollProcessThread.start();
        return mapping.findForward("billsProcessingPage");
    }

    public ActionForward epfpreparation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("epfpreparation");
    }

    /**
     *
     * @param processMonthDate Pay Bill Process Month date
     * @param serialno
     * @param epfno Employee Provident Fund Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains result of the save Transaction.
     */
    public Map payrollProcess(String processMonthDate, String serialno, String epfno, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillProcess employeePayBillProcessObj = (EmployeePayBillProcess) injector.getInstance(EmployeePayBillProcess.class);

        return employeePayBillProcessObj.payRollProcess(null, request, response, null, null, processMonthDate, serialno, epfno);
    }

    public Map preparePayRoll(String processMonthDate, String serialno, String epfno, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillProcess employeePayBillProcessObj = (EmployeePayBillProcess) injector.getInstance(EmployeePayBillProcess.class);
        return employeePayBillProcessObj.preparePayRoll(null, request, response, null, null, processMonthDate, serialno, epfno);
    }

    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains total number of employees
     */
    public Map getEmployeeList(HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillProcess EmployeePayBillProcessObj = (EmployeePayBillProcess) injector.getInstance(EmployeePayBillProcess.class);
        return EmployeePayBillProcessObj.getEmployeeList(null, request, response, null, null);
    }

    public Map getPayrollProcessDate(HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillProcess EmployeePayBillProcessObj = (EmployeePayBillProcess) injector.getInstance(EmployeePayBillProcess.class);
        return EmployeePayBillProcessObj.getPayrollProcessDate(null, request, response, null, null);
    }
}
