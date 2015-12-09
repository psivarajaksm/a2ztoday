/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.onward.dao.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import net.sf.jasperreports.engine.JRException;
import org.apache.struts.action.*;

import com.google.inject.Guice;
import com.google.inject.Injector;
//import com.itextpdf.text.Rectangle;
import com.onward.dao.OeslModule;

import java.io.*;
import java.util.*;
import org.apache.struts.validator.DynaValidatorForm;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Rectangle;
import com.onward.common.ApplicationConstants;
import com.onward.util.AppProps;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author sivaraja_p
 */
public class RegionwiseReportAction extends OnwardAction {

    OeslModule oeslModule = new OeslModule();
    Injector injector = Guice.createInjector(oeslModule);
    RegionwiseReportService regionwiseReportService = injector.getInstance(RegionwiseReportService.class);
    ReportNameService reportNameService = (ReportNameService) injector.getInstance(ReportNameService.class);
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public ActionForward EmployeeSchedulePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String grouptype = regionwiseReportService.getGroupType(null, request, response, null, null);
        String regionlist = regionwiseReportService.getRegionList(null, request, response, null, null);
        request.getSession().setAttribute("grouptype", grouptype);
        request.getSession().setAttribute("regionlist", regionlist);
        return mapping.findForward("EmployeeSchedulePage");
    }

    public Map getScheduleGrid(String syear, String smonth, String eyear, String emonth, String epfno, String grouptype, String region, String reporttype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** RegionwiseReportAction.class getScheduleGrid Method is calling ************************");
        Map map = new HashMap();     
        try {
            String rtype = "";
            if("1".equalsIgnoreCase(reporttype)){
                rtype="_REG_";
            }else if("2".equalsIgnoreCase(reporttype)){
                rtype="_SUP_";
            }
            String fileName = "SCD" + rtype + "_" + grouptype + "_" + region + ".xls";
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
            //map = edliServiceObj.edliEmployeeEarningsDeductionPrintOut(null, request, response, null, null, syear, smonth, ayear, amonth, regioncode, category, reporttype, filePathwithName);
            map = regionwiseReportService.getScheduleGrid(null, request, response, null, null, syear, smonth, eyear, emonth, epfno, grouptype, region, reporttype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward EarningsDetailsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** RegionwiseReportAction.class EarningsDetailsPage Method is calling ************************");
        String earninglist = regionwiseReportService.getEarningList(null, request, response, null, null);
        String regionlist = regionwiseReportService.getRegionList(null, request, response, null, null);
        request.getSession().setAttribute("earninglist", earninglist);
        request.getSession().setAttribute("regionlist", regionlist);
        return mapping.findForward("EarningsDetailsPage");
    }

    public Map getEarningDetailsGrid(String syear, String smonth, String eyear, String emonth, String epfno, String earningid, String region, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** RegionwiseReportAction.class getEarningDetailsGrid Method is calling ************************");
        Map map = new HashMap();
        try {
            map = regionwiseReportService.getEarningDetailsGrid(null, request, response, null, null, syear, smonth, eyear, emonth, epfno, earningid, region);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward ServiceRegisterVerificationPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** RegionwiseReportAction.class ServiceRegisterVerificationPage Method is calling ************************");
        String regionlist = regionwiseReportService.getRegionList1(null, request, response, null, null);
        request.getSession().setAttribute("regionlist", regionlist);
        return mapping.findForward("ServiceRegisterVerificationPage");
    }

    public Map getServiceRegisterDetailsGrid(String syear, String smonth, String eyear, String emonth, String epfno, String region, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** RegionwiseReportAction.class getServiceRegisterDetailsGrid Method is calling ************************");
        Map map = new HashMap();
        try {
            map = regionwiseReportService.getServiceRegisterDetailsGrid(null, request, response, null, null, syear, smonth, eyear, emonth, epfno, region);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map getServiceRegisterDetailsReport(String syear, String smonth, String eyear, String emonth, String epfno, String region, HttpServletRequest request, HttpServletResponse response) {

        System.out.println("********************** RegionwiseReportAction.class getServiceRegisterDetailsReport Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
//            String fileName = "Sales" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".xls";
            String fileName = "ServiceRegister" + region + ".xls";
//            String fileName = "Purchase.xls";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;

            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = regionwiseReportService.getServiceRegisterDetailsReport(null, request, response, null, null, syear, smonth, eyear, emonth, epfno, region, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
}
