/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.OeslModule;
import com.onward.dao.UserTypeService;
import com.onward.util.AppProps;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JRException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.util.*;

/**
 *
 * @author root
 */
public class UserTypeAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    UserTypeService usertypeServiceObj = (UserTypeService) injector.getInstance(UserTypeService.class);

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward userTypeCreationPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("userTypeCreationPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward duplicatePayBillPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("duplicatePayBillPage");
    }
    
    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward lastPayCertificatePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("lastPayCertificatePage");
    }

    /**
     *
     * @param request
     * @param response
     * @return        It Returns map.Map Contains List of existing Region Details.
     */
    public Map getUsertypes(HttpServletRequest request, HttpServletResponse response) {
        return usertypeServiceObj.getUsertypes(null, request, response, null, null);
    }

    /**
     *
     * @param request
     * @param response
     * @return          It Returns map.Map Contains result of the save Transaction.
     */
    public Map saveUserType(String funtype, String usertypeid, String usertype, HttpServletRequest request, HttpServletResponse response) {
        return usertypeServiceObj.saveUserType(null, request, response, null, null, funtype, usertypeid, usertype);
    }

    public Map FinacialYearStatus(String fyearvalue, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = new HashMap();
        if (!fyearvalue.equalsIgnoreCase("0")) {
            request.getSession(false).removeAttribute("financialYear");
            request.getSession(false).setAttribute("financialYear", fyearvalue);
            resultMap.put("success", "success");
        }
        return resultMap;
    }

    public ActionForward duplicatePaySlipPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
//        System.out.println("*************************** UserTypeAction class duplicatePaySlipPrint method ******************************");

        Logger logger = Logger.getLogger("com.elcot");
        String epfno = (String) request.getParameter("epfno");
        String month = (String) request.getParameter("month");
        String year = (String) request.getParameter("year");
        String monthandyear = (String) request.getParameter("monthandyear");
        String billtype = (String) request.getParameter("billtype");
//        System.out.println("fromDate==" + fromDate);
//        System.out.println("toDate==" + toDate);
        LinkedList paysliplist = new LinkedList();
//        try {
        paysliplist = usertypeServiceObj.duplicatePaySlipPrint(null, request, response, null, null, epfno, month, year,billtype);
        if (paysliplist.size() > 0) {
            try {
                JRMapCollectionDataSource trialBalanceDS = new JRMapCollectionDataSource(paysliplist);

                String reportsPath = getReportsPath();
                String reportName = "duplicatepayslip";

                Map parameters = new HashMap();
                parameters.put("epfno", epfno);
                parameters.put("monthandyear", monthandyear.toUpperCase());
//            parameters.put("todate", toDate);




                String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";



                //Specify the jasper path

                String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";


                JasperReport jasperReport = null;
                if (new File(jrxml).exists()) {
                    jasperReport = JasperCompileManager.compileReport(jrxml);
                } else if (new File(jasper).exists()) {
                    jasperReport = (JasperReport) JRLoader.loadObject(jasper);
                } else {
                    logger.info("@@@@@@@@ \" JRXML,JASPER ARE NOT FOUND IN THE SPECIFIED LOCATION \" @@@@@@@@@ ");
                }

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, trialBalanceDS);

                JRExporter exporter = null;
                if (jasperPrint != null) {
                    OutputStream outStream = null;

                    outStream = response.getOutputStream();
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment;filename=" + epfno + "_duplicateslip.pdf");
                    exporter = new JRPdfExporter();

                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                    exporter.exportReport();
                    return null;
                } else {
                    logger.info(" [ Accounts Report Action : trialBalancePrint ] No Records Available ");
                    return mapping.findForward("failure");

                }

            } catch (IOException ex) {
//            Logger.getLogger(AccountsReportsAction.class.getName()).log(Level.SEVERE, null, ex);
//        }
            }
            return null;
        } else {
            request.setAttribute("message","No Record(s) Found for the Given Input");
            return mapping.findForward("failure");
        }

//        } catch (IOException ex) {
//            Logger.getLogger(AccountsReportsAction.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
    }
   
    public ActionForward LPCPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JRException {
        Logger logger = Logger.getLogger("com.onward");
        String epfno = (String) request.getParameter("epfno");
        String month = (String) request.getParameter("month");
        String year = (String) request.getParameter("year");
        String monthandyear = (String) request.getParameter("monthandyear");
        String billtype = (String) request.getParameter("billtype");
        Map reportMap = usertypeServiceObj.LPCPrint(null, request, response, null, null, epfno, month, year, billtype);
        if (reportMap.size() > 0) {
            try {                
                String reportsPath = getReportsPath();
                String reportName = "lpcmain";
                Map parameters = new HashMap();                
                JRMapCollectionDataSource subReportDS = new JRMapCollectionDataSource((List)reportMap.get("loanmap"));
                JRMapCollectionDataSource subReportDS1 = new JRMapCollectionDataSource((List)reportMap.get("list"));
                parameters.put("reportspath", reportsPath.concat("/jrxml/"));                
                List wholeDataList = new ArrayList();
                for (int i = 1; i <= 2; i++) {
                    Map allDetails = new HashMap();
                    allDetails.put("subReportDS", "subReportDS");
                    allDetails.put("subReportDS1", "subReportDS1");
                    allDetails.put("group", i);
                    wholeDataList.add(allDetails);
                }
                HashMap mainReportDetails = new HashMap();
                mainReportDetails.put("subReportDS", subReportDS);
                mainReportDetails.put("subReportDS1", subReportDS1);
                JRMapCollectionDataSource mainDS = new JRMapCollectionDataSource(wholeDataList);
                
                parameters.put("reportdetails", mainReportDetails);
                parameters.put("epfno", epfno);
                parameters.put("monthandyear", monthandyear.toUpperCase());
                parameters.put("regionname", (String)reportMap.get("regionname"));
                
                String jrxml = reportsPath.concat("/jrxml/") + reportName + ".jrxml";
                String jasper = reportsPath.concat("/jrxml/") + reportName + ".jasper";
                JasperReport jasperReport = null;
                if (new File(jrxml).exists()) {
                    jasperReport = JasperCompileManager.compileReport(jrxml);
                } else if (new File(jasper).exists()) {
                    jasperReport = (JasperReport) JRLoader.loadObject(jasper);
                } else {
                    logger.info("@@@@@@@@ \" JRXML,JASPER ARE NOT FOUND IN THE SPECIFIED LOCATION \" @@@@@@@@@ ");
                }
                
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, mainDS);
                JRExporter exporter = null;
                if (jasperPrint != null) {
                    OutputStream outStream = null;
                    outStream = response.getOutputStream();
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment;filename=" + epfno + "_LPC.pdf");
                    exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
                    exporter.exportReport();
                    return null;
                } else {
                    logger.info(" [ UserType Action : LPCPrint ] No Records Available ");
                    return mapping.findForward("failure");
                }
            } catch (IOException ex) {
            }
            return null;
        } else {
            request.setAttribute("message", "No Record(s) Found for the Given Input");
            return mapping.findForward("failure");
        }
    }
    
    private String getReportsPath() {
        String webPath = "WEB-INF/report";
        String jrxmlPath = this.getServlet().getServletContext().getRealPath("/");
        AppProps app = AppProps.getInstance();
        if (app.getProperty("appContext") == null) {
            app.setProperty("appContext", jrxmlPath);
            app.loadProps();
        }
        return app.getProperty("appContext").concat(webPath);
    }
}
