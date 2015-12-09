/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.BonusBillService;
import com.onward.dao.IncomeTaxService;
import com.onward.dao.OeslModule;
import com.onward.dao.ReportNameService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import javax.servlet.ServletOutputStream;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author root
 */
public class IncomeTaxAction extends OnwardAction {

    private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    OeslModule incometaxModule = new OeslModule();
    Injector injector = Guice.createInjector(incometaxModule);
    IncomeTaxService incomeTaxService = (IncomeTaxService) injector.getInstance(IncomeTaxService.class);
    ReportNameService reportNameService = (ReportNameService) injector.getInstance(ReportNameService.class);

    public String getYear(int startingyear, int endingyear) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("<select ");
        buffer.append("class=\"combobox\" ");
        buffer.append("name=\"year\" id=\"year\">");
//            buffer.append("<option value=\"");
//            buffer.append("0");
//            buffer.append("\">");
//            buffer.append("----------Select----------");
//            buffer.append("</option>");
        for (int i = startingyear; i <= endingyear; i++) {
            buffer.append("<option value=\"");
            buffer.append(i);
            buffer.append("\">");
            buffer.append(i);
            buffer.append("</option>");
        }
        buffer.append("</select>");
        return buffer.toString();
    }

    public ActionForward tentativePayReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("tentativePayReportPage");
    }
    
    public ActionForward incomeTaxReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("incomeTaxReportPage");
    }

    public Map IncomeTaxTentativeParticularsPrintOut(String startyear, String endyear, String pfno, String reporttype, String sectionid, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** IncomeTaxAction.class IncomeTaxTentativeParticularsPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "incometax.txt";
            String fileName = null;           
            if (reporttype.equals("1")) {
                fileName = "IT"+sectionid+".txt";
            } else {
                fileName = "IT"+sectionid+".xls";
            }
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            BonusBillService bonusBillService = (BonusBillService) injector.getInstance(BonusBillService.class);
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
            map = incomeTaxService.IncomeTaxTentativeParticularsPrintOut(null, request, response, null, null,  startyear,  endyear, pfno, reporttype, filePathwithName, sectionid);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
    
    public Map IncomeTaxParticularsPrintOut(String startyear, String epfno, String reporttype, String sectionid, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** IncomeTaxAction.class IncomeTaxParticularsPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = "ITreportPart"+sectionid+".txt";
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
            map = incomeTaxService.IncomeTaxParticularsPrintOut(null, request, response, null, null,  startyear, epfno, reporttype, filePathwithName, sectionid);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map loadSectionDetails(HttpServletRequest request, HttpServletResponse response) {
        return incomeTaxService.loadSectionDetails(null, request, response, null, null);
    }
    
}
