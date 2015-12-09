/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.BonusBillService;
import com.onward.dao.GlobalDBOpenCloseAndUserPrivilagesImp;
import com.onward.dao.OeslModule;
import com.onward.dao.ReportNameService;
import com.onward.valueobjects.UserViewModel;
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
public class BonusBillAction extends OnwardAction {

    private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    OeslModule oeslModule = new OeslModule();
    Injector inject = Guice.createInjector(oeslModule);
    ReportNameService reportNameService = (ReportNameService) inject.getInstance(ReportNameService.class);

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

    public ActionForward employeeBonusPDPPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeBonusPDPPage");
    }
    
    public ActionForward employeeBonusTransRetiredPDPPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeBonusTransRetiredPDPPage");
    }    

    public Map EmployeeBonusPDPPrintOut(String startmonth, String startyear, String endmonth, String endyear, String pfno, String category, String section, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** BonusBillAction.class EmployeeBonusPDPPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Bon" + months[Integer.valueOf(endmonth) - 1] + endyear.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "BON", endmonth, endyear);
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
            map = bonusBillService.EmployeeBonusPDPPrintOut(null, request, response, null, null, startmonth, startyear, endmonth, endyear, pfno, category,section, filePathwithName);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
    
    public Map RetiredInTransferEmployeeBonusPDPPrintOut(String startmonth, String startyear, String endmonth, String endyear, String pfno, String category, String section, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** BonusBillAction.class EmployeeBonusPDPPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Bon" + months[Integer.valueOf(endmonth) - 1] + endyear.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "BON", endmonth, endyear);
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
            map = bonusBillService.RetiredInTransferEmployeeBonusPDPPrintOut(null, request, response, null, null, startmonth, startyear, endmonth, endyear, pfno, category,section, filePathwithName);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }    
}
