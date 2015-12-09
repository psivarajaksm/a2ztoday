/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.DAIncrementArrearService;
import com.onward.dao.OeslModule;
import com.onward.dao.ReportNameService;
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
public class DAIncrementArrearAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    DAIncrementArrearService DAIncrementArrearServiceObj = (DAIncrementArrearService) injector.getInstance(DAIncrementArrearService.class);
    ReportNameService reportNameService = (ReportNameService) injector.getInstance(ReportNameService.class);

    public ActionForward DAIncrementArrearPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("DAIncrementArrearPage");

    }
    //"DA Arrear Batch Creation"
    public ActionForward DAArrearBatchCreationPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("DAArrearBatchCreationPage");
    }

    public Map saveDAArrearBatchCreation(String fileno, String batchperiod, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.saveDAArrearBatchCreation(null, request, response, null, null, fileno, batchperiod);
    }
        
    public ActionForward DAIncrementArrearReprocessPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("DAIncrementArrearReprocessPage");

    }

    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains total number of employees
     */
    public Map getEmployeeList(HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.getEmployeeList(null, request, response, null, null);
    }

    public Map getEmployeeListHTML(String sectionid, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.getEmployeeListHTML(null, request, response, null, null, sectionid);
    }

    public Map saveDAIncrementProcess(String fromdate, String todate, String asondate, String dapercentage, String serialno, String epfno, String batchno, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.saveDAIncrementProcess(null, request, response, null, null, fromdate, todate, asondate, dapercentage, serialno, epfno, batchno);

    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public Map loadDADetails(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** loadDADetails calling ************************");
        return DAIncrementArrearServiceObj.loadDADetails(null, request, response, null, null);

    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public Map loadbatchDetails(String daarrearid, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.loadbatchDetails(null, request, response, null, null, daarrearid);

    }

    public Map getBatchEmployeeListHTML(String dabatchid, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.getBatchEmployeeListHTML(null, request, response, null, null, dabatchid);
    }

    public Map removeFromDa(String batchid, String epfno, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.removeFromDa(null, request, response, null, null, batchid, epfno);
    }

    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains List of existing Region Details.
     */
    public Map loadSectionDetails(HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.loadSectionDetails(null, request, response, null, null);
    }

    public Map modifyDADetailsinHTML(String batchid, String epfno, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.modifyDADetailsinHTML(null, request, response, null, null, batchid, epfno);
    }

    public Map getDAIncrementEarningsUpdation(String epfno, String month, String year, String batchid, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.getDAIncrementEarningsUpdation(null, request, response, null, null, epfno, month, year, batchid);
    }

    public Map getDADetailsCompManual(String subpaybillid, String processingdetailsid, String month, String year, String epfno, String dabatchno, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.getDADetailsCompManual(null, request, response, null, null, subpaybillid, processingdetailsid, month, year, epfno, dabatchno);
    }

    public Map saveDaManual(String subpaybillid, String dmonthid, String dyearid, String basicamount, String perpayamt, String gradepayamt, String dueamt, String drawnamt, String arrearamt, String epfamt, String billtype, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.saveDaManual(null, request, response, null, null, subpaybillid, dmonthid, dyearid, basicamount, perpayamt, gradepayamt, dueamt, drawnamt, arrearamt, epfamt, billtype);
    }

    public ActionForward DAIncrementArrearReprocessUpdatePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("DAIncrementArrearReprocessUpdatePage");
    }

    public Map getBatchEmployeeList(String dabatchid, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.getBatchEmployeeList(null, request, response, null, null, dabatchid);
    }

    public Map getDaIncrementArrearManualFormDetails(String epfno, String batchid, String daareardiffid, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.getDaIncrementArrearManualFormDetails(null, request, response, null, null, epfno, batchid, daareardiffid);
    }

    public Map DaManualFormUpdation(String subpaybillid, String dmonthid, String dyearid, String basicamount, String perpayamt, String gradepayamt, String dueamt, String drawnamt, String arrearamt, String epfamt, String billtype, String epfno, String batchid, String daareardiffid, HttpServletRequest request, HttpServletResponse response) {
        return DAIncrementArrearServiceObj.DaManualFormUpdation(null, request, response, null, null, subpaybillid, dmonthid, dyearid, basicamount, perpayamt, gradepayamt, dueamt, drawnamt, arrearamt, epfamt, billtype, epfno, batchid, daareardiffid);
    }
}
