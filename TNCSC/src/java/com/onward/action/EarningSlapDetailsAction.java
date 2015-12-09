/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.EarningSlapDetailsService;
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
public class EarningSlapDetailsAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    EarningSlapDetailsService slapDetailsObj = (EarningSlapDetailsService) injector.getInstance(EarningSlapDetailsService.class);
     /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward slapDetailsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("slapDetailsPage");
    }

    public ActionForward professionaltaxSlapPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("professionaltaxSlapPage");
    }
    
    public ActionForward stopProcessPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("stopProcessPage");
    }
    /**
     *
     * @param request
     * @param response
     * @return              List of Earninigs Type Names
     */
    public Map getLoadEarningsTypes(HttpServletRequest request, HttpServletResponse response) {
        return slapDetailsObj.getLoadEarningsTypes(null, request, response, null, null);
    }
    public Map getLoadDedutionTypes(HttpServletRequest request, HttpServletResponse response) {
        return slapDetailsObj.getLoadDedutionTypes(null, request, response, null, null);
    }
    public Map createRowinHTML(String noofrows, HttpServletRequest request, HttpServletResponse response) {
        return slapDetailsObj.createRowinHTML(null, request, response, null, null,noofrows);
    }
    public Map getDatasForModify(String earningcode, HttpServletRequest request, HttpServletResponse response) {
        return slapDetailsObj.getDatasForModify(null, request, response, null, null,earningcode);
    }
    public Map getPTDatasForModify(String deductioncode, HttpServletRequest request, HttpServletResponse response) {
        return slapDetailsObj.getPTDatasForModify(null, request, response, null, null,deductioncode);
    }

    public Map saveEarningsSlapDetails(String earningstypes,String effectdate,String[] amountrangefrom,String[] amountrangeto,String[] slapamount,String[] slappercentage,String orderno,String totalrows,String[] hiddeniarray,String funtype,HttpServletRequest request, HttpServletResponse response) {
        return slapDetailsObj.saveEarningsSlapDetails(null, request, response, null, null,earningstypes,effectdate,amountrangefrom,amountrangeto,slapamount,slappercentage,orderno,totalrows,hiddeniarray,funtype);
    }

    public Map savePTSlapDetails(String earningstypes,String effectdate,String[] amountrangefrom,String[] amountrangeto,String[] slapamount,String[] slappercentage,String orderno,String totalrows,String[] hiddeniarray,String funtype,HttpServletRequest request, HttpServletResponse response) {
        return slapDetailsObj.savePTSlapDetails(null, request, response, null, null,earningstypes,effectdate,amountrangefrom,amountrangeto,slapamount,slappercentage,orderno,totalrows,hiddeniarray,funtype);
    }
    public Map saveStopProcess(String processtype,String processmonth,String processyear, HttpServletRequest request, HttpServletResponse response) {
        return slapDetailsObj.saveStopProcess(null, request, response, null, null,processtype,processmonth,processyear);
    }
}
