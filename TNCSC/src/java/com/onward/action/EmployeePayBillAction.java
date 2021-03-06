/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.EmployeePayBillService;
import com.onward.dao.OeslModule;
import com.onward.common.HibernateUtil;
import com.onward.dao.ReportNameService;
import com.onward.persistence.payroll.Regionmaster;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author root
 */
public class EmployeePayBillAction extends OnwardAction {

    private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    OeslModule oeslModule = new OeslModule();
    Injector inject = Guice.createInjector(oeslModule);
    ReportNameService reportNameService = (ReportNameService) inject.getInstance(ReportNameService.class);

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeePayBillsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeePayBillsPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeePayStructurePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeePayStructurePage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map getEmployeePayStructureDetails(String epfno, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.getEmployeePayStructureDetails(null, request, response, null, null, epfno);
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeefundsuppage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeefundsuppage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeAttendancePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeAttendancePage");
    }

    public ActionForward downloadPaybillPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("downloadPaybillPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward leavelossofpayPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("leavelossofpayPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward miscTransactionDeductionPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("miscTransactionDeductionPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward miscTransactionExcelDeductionPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("miscTransactionExcelDeductionPage");
    }

    /**
     *
     * @param employeeProvidentFundNumber
     * @param request
     * @param response
     * @return
     */
    public Map getEmployeeDeductionDetails(String employeeProvidentFundNumber, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.getEmployeeEarningsandDeductionDetails(null, request, response, null, null, employeeProvidentFundNumber);
    }

    /**
     *
     * @param employeeProvidentFundNumber
     * @param request
     * @param response
     * @return
     */
    public Map getEmployeeEarningsAndDeductions(String employeeProvidentFundNumber, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.getEmployeeEarningsAndDeductions(null, request, response, null, null, employeeProvidentFundNumber);
    }

    public Map getEmptyarningsAndDeductions(HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.getEmptyEarningsAndDeductions(null, request, response, null, null);
    }

    public Map getEmployeeEarningsAndDeductionsInc(String employeeProvidentFundNumber, String asondate, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.getEmployeeEarningsAndDeductionsInc(null, request, response, null, null, employeeProvidentFundNumber, asondate);
    }

    /**
     *
     * @param employeeProvidentFundNumber
     * @param dateofeffect
     * @param ordernumber
     * @param request
     * @param response
     * @return
     */
    public Map getEmployeeDeduction(String employeeProvidentFundNumber, String dateofeffect, String ordernumber, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.getEmployeeDeduction(null, request, response, null, null, employeeProvidentFundNumber, dateofeffect, ordernumber);
    }

    /**
     *
     * @param region
     * @param section
     * @param epfno
     * @param request
     * @param response
     * @return
     */
    public Map getEmployeeListForAttendance(String region, String section, String epfno, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Sairam get emplyee list for attendance");
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.getEmployeeListForAttendance(null, request, response, null, null, region, section, epfno);
    }

    /**
     *
     * @param region
     * @param section
     * @param epfno
     * @param attendancemonth
     * @param request
     * @param response
     * @return
     */
    public Map getEmployeeListForLLP(String region, String section, String epfno, String attendancemonth, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.getEmployeeListForLLP(null, request, response, null, null, region, section, epfno, attendancemonth);
    }

    /**
     *
     * @param attendancemonth
     * @param deductioncode
     * @param request
     * @param response
     * @return
     */
    public Map getEmployeeMiscDeductions(String attendancemonth, String deductioncode, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.getEmployeeMiscDeductions(null, request, response, null, null, attendancemonth, deductioncode);
    }

    public Map getEmployeePreviousMiscDeductions(String attendancemonth, String deductioncode, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.getEmployeePreviousMiscDeductions(null, request, response, null, null, attendancemonth, deductioncode);
    }

    /**
     *
     * @param attendancemonth
     * @param epfno
     * @param name
     * @param present
     * @param woff
     * @param eldays
     * @param mldays
     * @param cldays
     * @param ndays
     * @param susdays
     * @param others
     * @param llp
     * @param ulep
     * @param totdays
     * @param curRec
     * @param totRec
     * @param request
     * @param response
     * @return
     */
    public Map saveEmployeeListForAttendance(String attendancemonth, String epfno, String name, String present, String woff, String eldays, String mldays, String cldays, String ndays, String susdays, String others, String llp, String ulep, String totdays, String curRec, String totRec, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(epfno + name + present + woff + eldays + mldays + cldays + ndays + susdays + others + llp + ulep + totdays);
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return EmployeePayBillServiceObj.saveEmployeeListForAttendance(null, request, response, null, null, attendancemonth, epfno, name, present, woff, eldays, mldays, cldays, ndays, susdays, others, llp, ulep, totdays, curRec, totRec);
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeePayBillsPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeePayBillPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeePayBillsCashPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeePayBillCashPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeePayBillsBankPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeePayBillBankPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeAcquaintanceSlipPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeAcquaintanceSlipPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeAcquaintanceSlipCashPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeAcquaintanceSlipCashPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeAcquaintanceSlipBankPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeAcquaintanceSlipBankPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeEarningsLedgerPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeEarningsLedgerPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeDeductionLedgerPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeDeductionLedgerPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeSalaryAbstractPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeSalaryAbstractPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeSalaryAbstractCashPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeSalaryAbstractCashPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeSalaryAbstractBankPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeSalaryAbstractBankPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeEPFformPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeEPFformPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeEPFformDBFPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeEPFformDBFPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeLICSchedulePrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeLICSchedulePrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeHBASchedulePrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeHBASchedulePrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeHBAScheduleConsolidatedPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeHBAScheduleConsolidatedPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeHBAInterestSchedulePrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeHBAInterestSchedulePrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeVehicleAdvancePrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeVehicleAdvancePrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeePLISchedulePrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeePLISchedulePrint");
    }

    public ActionForward employeeBankFloppyPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeBankFloppyPrint");
    }

    public ActionForward employeeBankTextPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeBankTextPrint");
    }

    public ActionForward employeeBankTextChequePrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeBankTextChequePrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeDeductionAllPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        Map<String, String> map = EmployeePayBillServiceObj.getEmployeeDeductionList(null, request, response, null, null);
        if (map != null) {
            StringBuffer buffer = new StringBuffer();
            Iterator itr = map.entrySet().iterator();
            buffer.append("<select ");
            buffer.append("class=\"combobox\" ");
            buffer.append("name=\"deductionid\" id=\"deductionid\">");
            buffer.append("<option value=\"");
            buffer.append("0");
            buffer.append("\">");
            buffer.append("----------Select----------");
            buffer.append("</option>");
            while (itr.hasNext()) {
                Map.Entry me = (Entry) itr.next();
                buffer.append("<option value=\"");
                buffer.append(me.getKey());
                buffer.append("\">");
                buffer.append(me.getValue());
                buffer.append("</option>");
            }
            buffer.append("</select>");
            System.out.println("Buffer value=> " + buffer.toString());
            request.getSession(false).setAttribute("deductionlist", buffer.toString());
        } else {
            System.out.println("Deduction List is Null");
        }
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeDeductionAllPrint");
    }

    /**
     *
     * @param type
     * @param employeeid
     * @param salarystructureid
     * @param orderno
     * @param salarystruceffeftfrom
     * @param code
     * @param amt
     * @param actcode
     * @param curRec
     * @param totRec
     * @param request
     * @param response
     * @return
     */
    public Map saveEmployeeSalaryStructure(String type, String employeeid, String salarystructureid, String orderno, String salarystruceffeftfrom, String code, String amt, String actcode, String curRec, String totRec, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return employeePayBillServiceObj.saveEmployeeSalaryStructure(null, request, response, null, null, type, employeeid, salarystructureid, orderno, salarystruceffeftfrom, code, amt, actcode, curRec, totRec);
    }

    /**
     *
     * @param attendancemonth
     * @param code
     * @param llpdays
     * @param curRec
     * @param totRec
     * @param request
     * @param response
     * @return
     */
    public Map saveEmployeeLLP(String attendancemonth, String code, String llpdays, String curRec, String totRec, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return employeePayBillServiceObj.saveEmployeeLLP(null, request, response, null, null, attendancemonth, code, llpdays, curRec, totRec);
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public Map loadDeductionDetails(String deductionType, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return employeePayBillServiceObj.loadDeductionDetails(null, request, response, null, null, deductionType);

    }

    /**
     *
     * @param attendancemonth
     * @param deductioncode
     * @param code
     * @param deductionAmount
     * @param curRec
     * @param totRec
     * @param request
     * @param response
     * @return
     */
    public Map saveEmployeeMiscDeductions(String attendancemonth, String deductioncode, String code, String deductionAmount, String curRec, String totRec, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return employeePayBillServiceObj.saveEmployeeMiscDeductions(null, request, response, null, null, attendancemonth, deductioncode, code, deductionAmount, curRec, totRec);
    }

    public ActionForward PopupReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("************************ PopupReport is calling **************************");
        try {
            DynaValidatorForm dvf = (DynaValidatorForm) form;
            String fileName = (String) dvf.get("fileName");
            String filePath = (String) dvf.get("filePath");

            response.setContentType("text/plain");
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

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeePayBillPrintOut(String epfno, String sectionname, String year, String month, String paymenttype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeePayBillPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = null;
            if (paymenttype.equals("all")) {
//                fileName = "Aslp" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "ASLP", month, year);
            } else if (paymenttype.equals("cash")) {
//                fileName = "Cslp" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "CSLP", month, year);
            } else if (paymenttype.equals("bank")) {
//                fileName = "Bslp" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "BSLP", month, year);
            }
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
                ex_file.setReadOnly();
            }
            map = EmployeePayBillServiceObj.EmployeePayBillPrintOut(null, request, response, null, null, epfno, sectionname, year, month, filePathwithName, paymenttype);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeePayBillDownload(String month, String year, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
//            String fileName = getMyRegionCode() + "PBF" + month + year;
            String fileName = reportNameService.getFileName(null, request, response, null, null, "PBF", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
            String filePath = request.getRealPath("/") + "PayBillDownload";
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
                ex_file.setReadOnly();
            }
            map = EmployeePayBillServiceObj.EmployeePayBillDownloandFileCreation(null, request, response, null, null, month, year, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeAcquitanceSlipPrintOut(String year, String month, String paymenttype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeAcquitanceSlipPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = null;
            if (paymenttype.equals("all")) {
//                fileName = "Aacq" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "AACQ", month, year);
            } else if (paymenttype.equals("cash")) {
//                fileName = "Cacq" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "CACQ", month, year);
            } else if (paymenttype.equals("bank")) {
//                fileName = "Bacq" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "BACQ", month, year);
            }

            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeAcquitanceSlipPrintOut(null, request, response, null, null, year, month, filePathwithName, paymenttype);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeEarningsLedgerPrintOut(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeEarningsLedgerPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Eled" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "EAR", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeEarningsLedgerPrintOut(null, request, response, null, null, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeDeductionLedgerPrintOut(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeDeductionLedgerPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Dled" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "DED", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeDeductionLedgerPrintOut(null, request, response, null, null, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeSalaryAbstractPrintOut(String year, String month, String paymenttype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeSalaryAbstractPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = null;
            if (paymenttype.equals("all")) {
//                fileName = "Aabs" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "AABS", month, year);
            } else if (paymenttype.equals("cash")) {
//                fileName = "Cabs" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "CABS", month, year);
            } else if (paymenttype.equals("bank")) {
//                fileName = "Babs" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "BABS", month, year);
            }
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeSalaryAbstractPrintOut(null, request, response, null, null, year, month, filePathwithName, paymenttype);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeEPFFormPrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeEPFFormPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Tepf" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "REPF", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeEPFformPrintOut(null, request, response, null, null, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeEPFFormDBFPrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeEPFFormDBFPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Depf" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "P", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeEPFformDBFPrintOut(null, request, response, null, null, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeLICSchedulePrintout(String year, String month, String reporttype, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "LIC" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "LIC", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            if ("EXL".equalsIgnoreCase(reporttype)) {
                fileName = fileName.substring(0, fileName.length() - 3) + "xls";
            }
            String filePathwithName = filePath + "/" + fileName;
            //create the upload folder if not exists
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                if ("EXL".equalsIgnoreCase(reporttype)) {
                    fileName = fileName.substring(0, fileName.length() - 3) + "xls";
                }
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = EmployeePayBillServiceObj.EmployeeLICSchedulePrintOut(null, request, response, null, null, year, month, filePathwithName, reporttype);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeHBASchedulePrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeHBASchedulePrintout Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "hba" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "HBA", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeHBASchedulePrintOut(null, request, response, null, null, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeHBAScheduleConsolidatedPrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeHBAScheduleConsolidatedPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Chba" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "CHBA", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeHBAScheduleConsolidatedPrintOut(null, request, response, null, null, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeHBAInterestSchedulePrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Ihba" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "IHBA", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeHBAInterestSchedulePrintOut(null, request, response, null, null, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     *
     */
    public Map EmployeeVehicleAdvancePrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "VA" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "VA", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeVehicleAdvancePrintOut(null, request, response, null, null, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeePLISchedulePrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "pli" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "PLI", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeePLISchedulePrintOut(null, request, response, null, null, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeBankFloppyPrintOut(String year, String month, String paymenttype, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
            String fileName = null;
            if (paymenttype.equals("B")) {
//                fileName = "Bflo" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "BFLO", month, year);
            } else if (paymenttype.equals("C")) {
//                fileName = "Cflo" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "CFLO", month, year);
            }

            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeBankFloppyPrintOut(null, request, response, null, null, year, month, filePathwithName, paymenttype);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeBankTextPrintOut(String year, String month, String paymenttype, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
            String fileName = null;
            if (paymenttype.equals("B")) {
//                fileName = "Btxt" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "BTXT", month, year);
            } else if (paymenttype.equals("C")) {
//                fileName = "Ctxt" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "CTXT", month, year);
            }

            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeBankTextPrintOut(null, request, response, null, null, year, month, filePathwithName, paymenttype);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeBankTextChequePrintOut(String year, String month, String paymenttype, String chequeno, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeBankTextChequePrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = null;
            System.out.println("chequeno = " + chequeno);
//            StringTokenizer st = new StringTokenizer(chequeno, "@@");
//            List list = new ArrayList();
//            while (st.hasMoreElements()) {
//                String row = (String) st.nextElement();
//                String sp[] = row.split("##");
//                int start = 0, end = 0;
//                if (sp[0] != null) {
//                    start = Integer.valueOf(sp[0]);
//                }
//                if (sp[1] != null) {
//                    end = Integer.valueOf(sp[1]);
//                }
//            }
            if (paymenttype.equals("B")) {
//                fileName = "Btxt" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "BCHE", month, year);
            } else if (paymenttype.equals("C")) {
//                fileName = "Ctxt" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
                fileName = reportNameService.getFileName(null, request, response, null, null, "CCHE", month, year);
            }

            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeBankTextChequePrintOut(null, request, response, null, null, year, month, filePathwithName, paymenttype, chequeno);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeDeductionAllPrintOut(String year, String month, String deductionid, String reporttype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeePLISchedulePrintout Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = deductionid.substring(0, 3) + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, deductionid.substring(0, 3), month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeDeductionAllPrintOut(null, request, response, null, null, deductionid, year, month, reporttype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

//            if (map.get("ERROR") != null) {
//                request.getSession(false).setAttribute("paybillprintresult", (String) map.get("ERROR"));
//                System.out.println("(String) map.get('Error') " + (String) map.get("ERROR"));
//                return mapping.findForward("employeeVehicleAdvancePrint");
//            } else {
//                response.setContentType("text/plain");
//                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//                File file = new File(filePath + "/" + fileName);
//                if (!file.exists()) {
//                    System.out.println("File NOt FOund");
//                } else {
//                    FileInputStream in = new FileInputStream(file);
//                    int fileSize = (int) file.length();
//                    ServletOutputStream out = response.getOutputStream();
//                    byte[] outputByte = new byte[fileSize];
//                    while (in.read(outputByte, 0, fileSize) != -1) {
//                        out.write(outputByte, 0, fileSize);
//                    }
//                    in.close();
//                    out.flush();
//                    out.close();
//                }
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public String getMyRegionCode() {
        String regionCode = "";
        SessionFactory _factory = HibernateUtil.getSessionFactory();
        Session hibernate_session = _factory.openSession();
        Criteria empRegionCrit = hibernate_session.createCriteria(Regionmaster.class);
        empRegionCrit.add(Restrictions.sqlRestriction("defaultregion is true"));
        List empRegionList = empRegionCrit.list();
        if (empRegionList.size() > 0) {
            Regionmaster regionmasterObj = (Regionmaster) empRegionList.get(0);
            regionCode = regionmasterObj.getId();
        }
        hibernate_session.close();
        return regionCode;
    }

    public ActionForward employeeFestivalAdvancePrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeFestivalAdvancePrint");
    }

    public Map employeeFestivalAdvancePrintout(String asondate, String type, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class employeeFestivalAdvancePrintout Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = null;
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(asondate));
            int month = cal.get(Calendar.MONTH);
            int date = cal.get(Calendar.DATE);
            String year = String.valueOf(cal.get(Calendar.YEAR));
            System.out.println("month = " + month);
            System.out.println("date = " + date);
            System.out.println("year = " + year);
//            fileName = "LICSchedule.txt";
//            fileName = "FA" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".txt";
            fileName = reportNameService.getFileName(null, request, response, null, null, "FA", String.valueOf(month), year);
            System.out.println("fileName = " + fileName);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeFestivalAdvancePrintOut(null, request, response, null, null, asondate, type, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward employeeThrftSocietyPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeThrftSocietyPrint");
    }

    public Map EmployeeThrftSocietyPrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeThrftSocietyPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "LIC" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "THR", String.valueOf(month), year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeThrftSocietyPrintout(null, request, response, null, null, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward employeeStorageLossPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeStorageLossPrint");
    }

    public Map EmployeeStorageLossPrintOut(String year, String month, String grouptype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeStorageLossPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "LIC" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, grouptype, String.valueOf(month), year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.EmployeeStorageLossPrintOut(null, request, response, null, null, year, month, grouptype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward employeeDBFPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeDBFPrintpage");
    }

    public Map employeeDBFPrintout(String reportType, String empcategory, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class employeeDBFPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = "";
            if ("DBF".equalsIgnoreCase(reportType)) {
                fileName = "EmploeeDBF.txt";
            } else {
                fileName = "EmploeeCSV.txt";
            }
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
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
            map = EmployeePayBillServiceObj.employeeDBFPrintout(null, request, response, null, null, filePathwithName, reportType, empcategory);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map addTextBox(HttpServletRequest request, HttpServletResponse response, String no) {
        System.out.println("********************** EmployeePayBillAction.class addTextBox Method is calling ************************");
        Map map = new HashMap();
        int rowno = Integer.valueOf(no);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
            for (int i = 0; i < rowno; i++) {
                sb.append("<tr class=\"lightrow\">");
                sb.append("<td width=\"20%\" class=\"textalign\">Starting Cheque Number</td>");
                sb.append("<td width=\"5%\" class=\"mandatory\">*</td>");
                sb.append("<td width=\"25%\" class=\"textfieldalign\" >");
                sb.append("<input type=\"text\" class=\"textbox\" name=\"startchequeno" + i + "\" id=\"startchequeno" + i + "\" >");
                sb.append("</td>");
                sb.append("<td width=\"20%\" class=\"textalign\">Ending Cheque Number</td>");
                sb.append("<td width=\"5%\" class=\"mandatory\">*</td>");
                sb.append("<td width=\"25%\" class=\"textfieldalign\" >");
                sb.append("<input type=\"text\" class=\"textbox\" name=\"endchequeno" + i + "\" id=\"endchequeno" + i + "\">");
//            sb.append("<input type=\"button\" class=\"submitbu\" id=\"add1\" value=\"Add\" onclick=\"addTextBox('1');\">");
                sb.append("</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");
            map.put("txtbox", sb.toString());
            map.put("ERROR", null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward employeeBonusdetailsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeebonusdetailspage");
    }

    public Map saveEmployeeBonusdetails(String attendancemonth, String bonustype, String code, String earningAmount, String deductionAmount, String curRec, String totRec, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        return employeePayBillServiceObj.saveEmployeeBonusdetails(null, request, response, null, null, attendancemonth, bonustype, code, earningAmount, deductionAmount, curRec, totRec);
    }

    public Map getEmployeeDeductionDetailsByEmployeedeductiondetailsactualid(String employeedeductiondetailsactualid, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        Map map = new HashMap();
        map.put("deductiondetails", employeePayBillServiceObj.getEmployeeDeductionDetailsById(null, request, response, null, null, employeedeductiondetailsactualid));
        return map;
    }

    public Map getEmployeeEarningDetailsByEmployeeearningsdetailsactualid(String employeeearningsdetailsactualid, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        Map map = new HashMap();
        map.put("earningdetails", employeePayBillServiceObj.getEmployeeEarningDetailsById(null, request, response, null, null, employeeearningsdetailsactualid));
        return map;
    }

    public Map modifyEmployeeEarningDetailsByEmployeeearningsdetailsactualid(String epfno, String employeeearningsdetailsactualid, String earningcodemodify, String earningamountmodify, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        employeePayBillServiceObj.updateEmployeeearningsdetailsactual(null, request, response, null, null, epfno, employeeearningsdetailsactualid, earningcodemodify, earningamountmodify);
        return employeePayBillServiceObj.getEmployeePayStructureDetailsEarningDetails(null, request, response, null, null, epfno);
    }

    public Map modifyEmployeeDeductionDetailsByEmployeedeductiondetailsactualid(String epfno, String employeedeductiondetailsactualid, String deductioncodemodify, String deductionamountmodify,String deductionaccountmodify, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        employeePayBillServiceObj.updateEmployeedeductiondetailsactual(null, request, response, null, null, epfno, employeedeductiondetailsactualid, deductioncodemodify, deductionamountmodify,deductionaccountmodify);
        return employeePayBillServiceObj.getEmployeePayStructureDetailsDeductionDetails(null, request, response, null, null, epfno);
    }

    public Map addEmployeeEarningDetailsBySalarystructureid(String epfno, String salarystructureid, String earningnameadd, String earningamountadd, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        employeePayBillServiceObj.saveEmployeeearningsdetailsactual(null, request, response, null, null, salarystructureid, earningnameadd, earningamountadd);
        return employeePayBillServiceObj.getEmployeePayStructureDetailsEarningDetails(null, request, response, null, null, epfno);
    }

    public Map addEmployeeDeductionDetailsBySalarystructureid(String epfno, String salarystructureid, String deductionnameadd, String deductionamountadd, String deductionaccountadd, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        EmployeePayBillService employeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
        employeePayBillServiceObj.saveEmployeedeductiondetailsactual(null, request, response, null, null, salarystructureid, deductionnameadd, deductionamountadd, deductionaccountadd);
        return employeePayBillServiceObj.getEmployeePayStructureDetailsDeductionDetails(null, request, response, null, null, epfno);
    }
}
