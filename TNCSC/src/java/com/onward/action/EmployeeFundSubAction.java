/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.EmployeeFundSubService;
import com.onward.dao.OeslModule;
import com.onward.dao.ReportNameService;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author root
 */
public class EmployeeFundSubAction extends OnwardAction {

    private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    
    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    EmployeeFundSubService employeeFundsUpServiceObj = (EmployeeFundSubService) injector.getInstance(EmployeeFundSubService.class);
    ReportNameService reportNameService = (ReportNameService) injector.getInstance(ReportNameService.class);

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeFundSubPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeFundSubPage");
    }
    /**
     *
     * @param epfno Employee Provident Fund Number
     * @param request
     * @param response
     * @return Employee details and Display Employee's Current Details
     */
    public Map getEmployeeDetails(String epfno,String employeetype, HttpServletRequest request, HttpServletResponse response) {
        return employeeFundsUpServiceObj.getEmployeeDetails(null, request, response, null, null, epfno,employeetype);
    }
    /**
     *
     * @param epfno Employee Type
     * @param request
     * @param response
     * @return 
     */
    public Map getRecordedDetails(String employeetype,String month,String year, HttpServletRequest request, HttpServletResponse response) {
        return employeeFundsUpServiceObj.getRecordedDetails(null, request, response, null, null, employeetype,month,year);
    }
    public Map getDetailsForModification(String epfId, HttpServletRequest request, HttpServletResponse response) {
        return employeeFundsUpServiceObj.getDetailsForModification(null, request, response, null, null, epfId);
    }


    public Map modifyEmployeeEPF(String epfid,String epfnumber,String salary,String epfwhole,String fbr,String rl,String vpf,String epf,String fbf,String nrc, String smonth, String syear, HttpServletRequest request, HttpServletResponse response) {
        return employeeFundsUpServiceObj.modifyEmployeeEPF(null, request, response, null, null, epfid,epfnumber,salary,epfwhole,fbr,rl,vpf,epf,fbf,nrc,smonth, syear);
    }
    public Map saveEmployeeEPF(String month,String year,String employeetype,String epfno,String employeename,String salaryamt, String smonth, String syear, HttpServletRequest request, HttpServletResponse response) {
        return employeeFundsUpServiceObj.saveEmployeeEPF(null, request, response, null, null, month,year,employeetype,epfno,employeename,salaryamt,smonth, syear);
    }
    
    public ActionForward employeeFundSubEpfFormPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeFundSubEpfFormPrintPage");
    }
    
    public ActionForward employeeFundSubEpfFormDBFPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeFundSubEpfFormDBFPrintPage");
    }

    public ActionForward employeeFundSubEpfFormDownloadPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeFundSubEpfFormDownloadPrintPage");
    }

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

    public Map EmployeeFundsUpEPFFormDBFPrintout(String year, String month, String empcategory, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Depf" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, empcategory+"", month, year);
            String rcode = fileName.substring(1, 3);
            fileName = empcategory+""+rcode+""+months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            EmployeeFundSubService employeeFundSubService = (EmployeeFundSubService) injector.getInstance(EmployeeFundSubService.class);
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
            map = employeeFundSubService.EmployeeFundsUpEPFformDBFPrintOut(null, request, response, fileName, fileName, year, month, filePathwithName, empcategory);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward PopupReportXML(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaValidatorForm dvf = (DynaValidatorForm) form;
            String fileName = (String) dvf.get("fileName");
            String filePath = (String) dvf.get("filePath");

            response.setContentType("text/xml");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File NOt FOund");
            } else {
                FileInputStream in = new FileInputStream(file);
                int fileSize = (int) file.length();
                ServletOutputStream out = response.getOutputStream();
                byte[] outputByte = new byte[fileSize];
                while (in.read(outputByte, 0, fileSize) != -1) {
                    out.write(outputByte, 0, fileSize);
                }
                in.close();
                out.flush();
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public Map EmployeeFundsUpEPFFormDownloadPrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
            String fileName = null;
//            fileName = "EPFForm.txt";
            fileName = "Depf" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".xml";
            ReportNameService reportNameService = (ReportNameService) injector.getInstance(ReportNameService.class);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            EmployeeFundSubService employeeFundSubService = (EmployeeFundSubService) injector.getInstance(EmployeeFundSubService.class);
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
            map = employeeFundSubService.EmployeeFundsUpEPFformDownloadPrintOut(null, request, response, fileName, fileName, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeFundsUpEPFFormPrintout(String year, String month, String empcategory, HttpServletRequest request, HttpServletResponse response) {        
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Tepf" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "FEPF", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            EmployeeFundSubService employeeFundSubService = (EmployeeFundSubService) injector.getInstance(EmployeeFundSubService.class);
            String filePath = request.getRealPath("/") + "PayBillPrint_"+empcategory;
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
            map = employeeFundSubService.EmployeeFundsUpEPFformPrintOut(null, request, response, fileName, fileName, year, month, empcategory, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

}
