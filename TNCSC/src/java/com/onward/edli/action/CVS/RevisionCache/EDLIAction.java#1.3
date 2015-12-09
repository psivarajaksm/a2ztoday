/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.edli.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.action.OnwardAction;
import com.onward.dao.OeslModule;
import com.onward.edli.dao.EDLIService;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author user
 */
public class EDLIAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    EDLIService edliServiceObj = (EDLIService) injector.getInstance(EDLIService.class);

    public ActionForward edliReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("edliReportPage");
    }

    public ActionForward empRetirementDetailsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("empRetirementDetailsPage");
    }

    public ActionForward empAppointmentDetailsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("empAppointmentDetailsPage");
    }

    public ActionForward regionwiseEmpDetailsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("regionwiseEmpDetailsPage");
    }

    public Map getEDLIDetails(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        return edliServiceObj.getEDLIDetails(null, request, response, null, null, month, year);
    }

    public Map getDesignationWiseEmployeesDetails(String month, String year, String empcategory, HttpServletRequest request, HttpServletResponse response) {
        return edliServiceObj.getDesignationWiseEmployeesDetails(null, request, response, null, null, month, year, empcategory);
    }

    public Map getDesignationEmployees(String month, String year, String designationcode, String empcategory, HttpServletRequest request, HttpServletResponse response) {
        return edliServiceObj.getDesignationEmployees(null, request, response, null, null, month, year, designationcode, empcategory);
    }

    public Map getAppointmentDetails(String fromyear, String toyear, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("get region wise employee details");
        return edliServiceObj.getAppointmentDetails(null, request, response, null, null, fromyear, toyear);
    }

    public Map getRegionMonthwiseAppointmentEmployees(String month, String regionid, String year, HttpServletRequest request, HttpServletResponse response) {
        return edliServiceObj.getRegionMonthwiseAppointmentEmployees(null, request, response, null, null, month, regionid, year);
    }

    public Map getALLRegionMonthwiseAppointmentEmployees(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("get region wise employee details");
        return edliServiceObj.getALLRegionMonthwiseAppointmentEmployees(null, request, response, null, null, month, year);
    }

    public Map getRegionAllMonthsAppointmentEmployees(String startyear, String regionid, String endyear, HttpServletRequest request, HttpServletResponse response) {
        return edliServiceObj.getRegionAllMonthsAppointmentEmployees(null, request, response, null, null, startyear, regionid, endyear);
    }

    public Map getAllRegionAppointmentEmployees(String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        return edliServiceObj.getAllRegionAppointmentEmployees(null, request, response, null, null, startyear, endyear);
    }

    public Map getRetirementDetails(String fromyear, String toyear, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("get region wise employee details");
        return edliServiceObj.getRetirementDetails(null, request, response, null, null, fromyear, toyear);
    }

    public Map getRegionMonthwiseRetirementEmployees(String month, String regionid, String year, HttpServletRequest request, HttpServletResponse response) {
        return edliServiceObj.getRegionMonthwiseRetirementEmployees(null, request, response, null, null, month, regionid, year);
    }

    public Map getALLRegionMonthwiseRetirementEmployees(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("get region wise employee details");
        return edliServiceObj.getALLRegionMonthwiseRetirementEmployees(null, request, response, null, null, month, year);
    }

    public Map getRegionAllMonthsRetirementEmployees(String startyear, String regionid, String endyear, HttpServletRequest request, HttpServletResponse response) {
        return edliServiceObj.getRegionAllMonthsRetirementEmployees(null, request, response, null, null, startyear, regionid, endyear);
    }

    public Map getAllRegionRetirementEmployees(String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        return edliServiceObj.getAllRegionRetirementEmployees(null, request, response, null, null, startyear, endyear);
    }

    public Map loadRegionDetails(HttpServletRequest request, HttpServletResponse response) {
        return edliServiceObj.loadRegionDetails(null, request, response, null, null);
    }

    public Map getRegionwiseEmployeeDetails(String selecteddate, HttpServletRequest request, HttpServletResponse response) {

        return edliServiceObj.getRegionwiseEmployeeDetails(null, request, response, null, null, selecteddate);
    }
    
    public ActionForward edliEmployeeReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("edliEmployeeReportPage");
    }
    public Map edliEmployeeReportPrintOut(String category, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
            String regioncode = (String) request.getSession().getAttribute("regioncode");
            String fileName = "edli_" + regioncode + "_" + category + ".xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            //create the upload folder if not exists
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = edliServiceObj.edliEmployeeReportPrintOut(null, request, response, null, null,  category, filePathwithName);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
    public ActionForward edliEmployeeEarningsReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("edliEmployeeEarningsReportPage");
    }
    public Map edliEmployeeEarningsDeductionPrintOut(String syear, String smonth, String ayear, String amonth, String region, String category, String reporttype, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
            String regioncode = (String) request.getSession().getAttribute("regioncode");
            String fnam = "";
            if("1".equalsIgnoreCase(reporttype)){
                fnam = "ERN";
            }else if("2".equalsIgnoreCase(reporttype)){
                fnam = "DCT";
            }else if("3".equalsIgnoreCase(reporttype)){
                fnam = "ERN_DCT";
            }
            String fileName = "edli_" + fnam + "_" + regioncode + "_" + category + ".xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            //create the upload folder if not exists
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = edliServiceObj.edliEmployeeEarningsDeductionPrintOut(null, request, response, null, null, syear, smonth, ayear, amonth, regioncode, category, reporttype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
}
