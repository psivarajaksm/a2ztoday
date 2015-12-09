/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.OeslModule;
import com.onward.dao.SalaryDeductionOthersService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sk
 */
public class SalaryDeductionOthersAction  extends OnwardAction {
    
    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    SalaryDeductionOthersService deductionObj = (SalaryDeductionOthersService) injector.getInstance(SalaryDeductionOthersService.class);
    
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
     * @return              List of Deduction Type Names
     */
    public Map getLoadDeductionTypes(HttpServletRequest request, HttpServletResponse response) {
        return deductionObj.getLoadDeductionTypes(null, request, response, null, null);
    }
    /** 
     * 
     * @param deductionmode
     * @param request
     * @param response
     * @return
     */
    public Map checkDeductionProcess(String deductionmode, HttpServletRequest request, HttpServletResponse response) {
        return deductionObj.checkDeductionProcess(null, request, response, null, null, deductionmode);
    }
    /**
     *
     * @param deductiontypes        Deduction Code
     * @param deductionmode         Type of Deduction of salary deduction Process
     * @param month                 Deduction Month
     * @param year                  Deduction Year
     * @param deductionamt          Deduction Amount    
     * @param request
     * @param response
     * @return   Display the all employees deduction amount , Deduction month and year
     */
    public Map salayDeductionOthersProcess(String deductiontypes, String deductionmode, String month, String year, String deductionamt,String isnew, HttpServletRequest request, HttpServletResponse response) {
        return deductionObj.salayDeductionOthersProcess(null, request, response, null, null, deductiontypes, deductionmode, month, year, deductionamt,isnew);
    }
    /**
     *
     * @param hdnemployees      Except Employee's for Deductions
     * @param installmentmode   Professional tax deduction  for single installment or double installment
     * @param delimiter         Delimiter for split the concatenation EPF no of Except Employee's for Deductions
     * @param request
     * @param response
     * @return                  result of the transaction of deduction process
     */
    public Map saveDeductionOthers(String hdnemployees, String installmentmode, String delimiter,String isNew, HttpServletRequest request, HttpServletResponse response) {
        return deductionObj.saveDeductionOthers(null, request, response, null, null, hdnemployees, installmentmode, delimiter,isNew);
    }
    /**
     *
     * @param request
     * @param response
     * @return   Display Current Financial Year
     */
    public Map loadFinanicalYears(HttpServletRequest request, HttpServletResponse response) {
        return deductionObj.loadFinanicalYears(null, request, response, null, null);
    }

    /**
     *
     * @param finanicalyear     Current Financial Year
     * @param deductionmode     Professional tax deduction mode are First half yearly or second half yearly
     * @param installmentmode   Professional tax deduction  for single installment or double installment
     * @param request
     * @param response
     * @return                    Display the all employees Professional tax deduction amount , Deduction month and year
     */
    public Map proffesionalTaxDeductionProcess(String finanicalyear, String deductionmode, String installmentmode,String isnew, HttpServletRequest request, HttpServletResponse response) {
        return deductionObj.proffesionalTaxDeductionProcess(null, request, response, null, null, finanicalyear, deductionmode, installmentmode,isnew);
    }
}
