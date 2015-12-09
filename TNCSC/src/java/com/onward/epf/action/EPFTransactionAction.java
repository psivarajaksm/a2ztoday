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
 * @author Karthikeyan.S
 */
public class EPFTransactionAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    EPFTransactionService epfService = (EPFTransactionService) injector.getInstance(EPFTransactionService.class);

    public ActionForward epftransactionPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("epftransactionPage");
    }
    public Map getEpfTransaction(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getEpfTransaction(null, request, response, null, null, month, year);
    }

    public Map getDetailsForModification(String epfId, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getDetailsForModification(null, request, response, null, null, epfId);
    }

    public Map getEmployeeDetails(String epfId, HttpServletRequest request, HttpServletResponse response) {
        return epfService.getEmployeeDetails(null, request, response, null, null, epfId);
    }
    public Map saveEmployeeEPF(String epfid,String epfnumber,String salary,String epfwhole,String fbr,String rl,String vpf,String epf,String fbf,String nrc, String smonth, String syear,String empcategory, String payrollcategory, HttpServletRequest request, HttpServletResponse response) {
        return epfService.saveEmployeeEPF(null, request, response, null, null, epfid,epfnumber,salary,epfwhole,fbr,rl,vpf,epf,fbf,nrc,smonth, syear,empcategory,payrollcategory);
    }
}
