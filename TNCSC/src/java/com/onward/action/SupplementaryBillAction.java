/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.OeslModule;
import com.onward.dao.ReportNameService;
import com.onward.dao.SupplementaryBillService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author root
 */
public class SupplementaryBillAction extends OnwardAction {

    private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    SupplementaryBillService SupplementaryBillServiceObj = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
    ReportNameService reportNameService = (ReportNameService) injector.getInstance(ReportNameService.class);

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward supplementarybillPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("supplementarybillPage");
    }

    /**
     *
     * @param epfno Employee Provident Fund Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains Employee Name
     */
    public Map loadEmployeeDetails(String epfno, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.loadEmployeeDetails(null, request, response, null, null, epfno);
    }

    /**
     *
     * @param epfno Employee Provident Fund Number
     * @param surrenderdays Number of leave surrende day for Supplementary
     * @param orderno Supplementary Order Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains result of the save Transaction.
     */
    public Map saveLeaveSurrenderBill(String epfno, String asondate, String surrenderdays, String orderno, String funtype, String surrenderdate, String designationname, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.saveLeaveSurrenderBill(null, request, response, null, null, epfno, asondate, surrenderdays, orderno, funtype, surrenderdate, designationname);
    }

    /**
     *
     * @param epfno Employee Provident Fund Number
     * @param month For the Month of Supplementary Bill
     * @param year For the year of Supplementary Bill
     * @param noofdays Number of days for Supplementary Bill
     * @param isnewmap
     * @param billdate Supplementary Bill Date
     * @param orderno Supplementary Order Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains all newly added months for
     * Supplementary Bill.
     */
    public Map displayAddedSupplementaryDetails(String epfno, String month, String year, String noofdays, String isnewmap, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.displayAddedSupplementaryDetails(null, request, response, null, null, epfno, month, year, noofdays, isnewmap);
    }

    /**
     *
     * @param mapId Row Id for the display Table.
     * @param request
     * @param response
     * @return It Returns map.Map Contains after deletion the particular month
     * of Supplementary Bill.
     */
    public Map deleteSupplementaryBillData(String mapId, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.deleteSupplementaryBillData(null, request, response, null, null, mapId);
    }

    /**
     *
     * @param epfno
     * @param request
     * @param response
     * @return It Returns map.Map Contains result of the save Transaction.
     */
    public Map saveSupplementaryBill(String epfno, String asondate, String orderno, String billno, HttpServletRequest request, HttpServletResponse response) {


        return SupplementaryBillServiceObj.saveSupplementaryBill(null, request, response, null, null, epfno, asondate, orderno, billno);
    }

    public Map modifySupplementaryBillDatas(String epfno, String asondate, String Billtype, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.modifySupplementaryBillDatas(null, request, response, null, null, epfno, asondate, Billtype);
    }

    public Map modifyIncrementArrears(String epfno, String asondate, String Billtype, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.modifyIncrementArrears(null, request, response, null, null, epfno, asondate, Billtype);
    }

    public Map modifySurrenderLeaveDatas(String epfno, String asondate, String Billtype, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.modifySurrenderLeaveDatas(null, request, response, null, null, epfno, asondate, Billtype);
    }

    public Map deleteSurrenderLeaveDatas(String epfno, String asondate, String Billtype, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.deleteSurrenderLeaveDatas(null, request, response, null, null, epfno, asondate, Billtype);
    }

    public Map displaySupplementaryBillsData(String billid, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.displaySupplementaryBillsData(null, request, response, null, null, billid);
    }

    public Map displayAddModifyDataDetails(String mapId, String month, String year, String noofdays, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.displayAddModifyDataDetails(null, request, response, null, null, mapId, month, year, noofdays);

    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeSupplementaryBillPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeSupplementaryBillPrint");
    }

    public ActionForward employeeSupplementaryEpfFormPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeSupplementaryEPFformPrint");
    }

    public ActionForward employeeSupplementaryEPFformDBFPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeSupplementaryEPFformDBFPrint");
    }

    public ActionForward employeeSupplementaryAbstractPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class employeeSupplementaryAbstractPrintpage Method is calling ************************");
        return mapping.findForward("employeeSupplementaryAbstractPrint");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeSupplementaryBillPrintOut(String epfno, String asondate, String billtype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeePayBillPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            String AsOnMonth = months[Integer.valueOf(asondate.substring(3, 5)) - 1];
//            String AsOnYear = asondate.substring(6, 10);
//            fileName = "Sslip" + AsOnMonth + AsOnYear + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "SSLP", asondate.substring(3, 5), asondate.substring(6, 10));

            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryBillPrintOut(null, request, response, null, null, epfno, asondate, billtype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeSupplementaryEPFFormPrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeEPFFormPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Tepf" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "SEPF", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryEPFformPrintOut(null, request, response, fileName, fileName, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeSupplementaryLICSchedulePrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeSupplementaryLICSchedulePrintout Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Slic" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "SLIC", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryLICSchedulePrintOut(null, request, response, fileName, fileName, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeSupplementaryPayDBFPrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeSupplementaryPayDBFPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Sdpf" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
//            String fileName = reportNameService.getFileName(null, request, response, null, null, "SDBF", month, year);  \\ CR change in filename 
            String fileName = reportNameService.getFileName(null, request, response, null, null, "S", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryPayDBFPrintOut(null, request, response, fileName, fileName, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeSupplementaryEPFFormDBFPrintout(String year, String month, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeSupplementaryEPFFormDBFPrintout Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Tepf" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "SDBF", month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryEPFformDBFPrintOut(null, request, response, fileName, fileName, year, month, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param request
     * @param response
     * @param billdate
     * @param billtype
     * @return
     */
    public Map displaySupplementaryAbstractDetails(HttpServletRequest request, HttpServletResponse response, String billdate, String billtype) {
        return SupplementaryBillServiceObj.displaySupplementaryAbstractDetails(null, request, response, null, null, billdate, billtype);
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public Map EmployeeSupplementaryAbstractPrintOut(String asondate, String billtype, String billnumbers, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeePayBillPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            String AsOnMonth = months[Integer.valueOf(asondate.substring(3, 5)) - 1];
//            String AsOnYear = asondate.substring(6, 10);
//            fileName = "Sabs" + AsOnMonth + AsOnYear + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "SEPF", asondate.substring(3, 5), asondate.substring(6, 10));
            String[] billnumbers_array = billnumbers.split(",");
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryAbstractPrintOut(null, request, response, null, null, billnumbers_array, asondate, billtype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
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

    public ActionForward employeeSupplementaryDeductionAllPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        OeslModule notOnWeekendsModule = new OeslModule();
        Injector injector = Guice.createInjector(notOnWeekendsModule);
        SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
        Map<String, String> map = supplementaryBillService.getSupplementaryEmployeeDeductionList(null, request, response, null, null);
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
        return mapping.findForward("employeeSupplementaryDeductionAllPrint");
    }

    public ActionForward employeeSupplementaryLICSchedulePrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeSupplementaryLICSchedulePrint");
    }

    public ActionForward employeeSupplementaryPayDBFPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("year", getYear(2012, 2025));
        return mapping.findForward("employeeSupplementaryPayDBFPrint");
    }

    public Map EmployeeSupplementaryDeductionAllPrintOut(String year, String month, String deductionid, String reporttype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeeSupplementaryDeductionAllPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = deductionid.substring(0, 3) + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, deductionid.substring(0, 3), month, year);
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryDeductionAllPrintOut(null, request, response, null, null, deductionid, year, month, reporttype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map getEmployeeEarningsforIA(String epfno, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.getEmployeeEarningsforIA(null, request, response, null, null, epfno);
    }

    public ActionForward employeeSupplementaryDABillPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeSupplementaryDABillPrint");
    }

    public ActionForward employeeSupplementaryDAAcquitanceCashPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeSupplementaryDAAcquitanceCashPrint");
    }

    public ActionForward employeeSupplementaryDAAcquitanceChequePrintPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeSupplementaryDAAcquitanceChequePrint");
    }

    public Map saveIncrementArrearBill(String epfno, String asondate, String orderno, String startmonth, String startyear, String endmonth, String endyear, String earningCode, String earningAmount, String designationname, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.saveIncrementArrearBill(null, request, response, null, null, epfno, asondate, orderno, startmonth, startyear, endmonth, endyear, earningCode, earningAmount, designationname);
    }

    public Map ModifyIncrementArrearBill(String epfno, String asondate, String orderno, String startmonth, String startyear, String endmonth, String endyear, String billno, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.ModifyIncrementArrearBill(null, request, response, null, null, epfno, asondate, orderno, startmonth, startyear, endmonth, endyear, billno);
    }

    public Map getIncrementArrearDetails(String epfno, String asondate, String billno, HttpServletRequest request, HttpServletResponse response) {
        String[] earcode= new String[10];
        return SupplementaryBillServiceObj.getIncrementArrearDetails(null, request, response, null, null, epfno, asondate, billno, earcode);
    }

    public Map getIncrementEarnings(String employeeProvidentFundNumber, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.getIncrementEarnings(null, request, response, null, null, employeeProvidentFundNumber);
    }

    public Map getDueDetails(String epfno, String month, String year, String supsalstrucidid, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.getDueDetails(null, request, response, null, null, epfno, month, year, supsalstrucidid);
    }

    public Map updateincrementarreardue(String supsalstrucidid, String earningCode, String earningAmount, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.updateincrementarreardue(null, request, response, null, null, supsalstrucidid, earningCode, earningAmount);
    }

    public Map getIncrementEarningsUpdation(String epfno, String month, String year, String asondate,String designationname, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.getIncrementEarningsUpdation(null, request, response, null, null, epfno, month, year, asondate, designationname);
    }
    
    public Map updateincrementarrearmanual(String supsalstrucidid, String earningCode, String earningAmount, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.updateincrementarrearmanual(null, request, response, null, null, supsalstrucidid, earningCode, earningAmount);
    }

    public Map getManIncDetails(String epfno, String month, String year, String processid, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.getManIncDetails(null, request, response, null, null, epfno, month, year, processid);
    }

    public Map saveIncrementArrearDetails(String epfno, String asondate, HttpServletRequest request, HttpServletResponse response) {
        return SupplementaryBillServiceObj.saveIncrementArrearDetails(null, request, response, null, null, epfno, asondate);
    }

    public ActionForward employeeSupplementaryIncrementArrearPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeSupplementaryIncrementArrearPrint");
    }

    public ActionForward employeeSupplementaryIncrementArrearAcquittancePrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeSupplementaryIncrementArrearAcquittancePrint");
    }

    public Map PrintIncrementArrearDetails(String epfno, String asondate, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class PrintIncrementArrearDetails Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "IncrementArrear.txt";
//            fileName = "Aslp" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "IARR", asondate.substring(3, 5), asondate.substring(6, 10));
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            System.out.println("filePathwithName = " + filePathwithName);
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
            map = supplementaryBillService.PrintIncrementArrearDetails(null, request, response, null, null, epfno, asondate, filePathwithName);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeSupplementaryDABillPrintOut(String dabatch, String epfno, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeeSupplementaryDABillPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Aslp" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
//            String fileName = reportNameService.getFileName(null, request, response, null, null, "DA", asondate.substring(3, 5), asondate.substring(6, 10));
            String fileName = "DA" + dabatch + ".txt";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryDABillPrintOut(null, request, response, null, null, dabatch, epfno, filePathwithName);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeSupplementaryDAAcquitanceCashPrintOut(String dabatch, String atype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeeSupplementaryDAAcquitanceCashPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "DAACQUITANCE.txt";
//            String fileName = reportNameService.getFileName(null, request, response, null, null, "DACQC", asondate.substring(3, 5), asondate.substring(6, 10));
            String fileName = dabatch;
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryDAAcquitanceCashPrintOut(null, request, response, null, null, dabatch, atype, filePathwithName);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeSupplementaryDAAcquitanceChequePrintOut(String dabatch, String chequeno, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeeSupplementaryDAAcquitanceChequePrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "DABILL.txt";
//            String fileName = reportNameService.getFileName(null, request, response, null, null, "DACQB", asondate.substring(3, 5), asondate.substring(6, 10));
            String fileName = dabatch;
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryDAAcquitanceChequePrintOut(null, request, response, null, null, dabatch, chequeno, filePathwithName);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map EmployeeSupplementaryIncrementArrearAcquitanceDetails(String asondate, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeeSupplementaryIncrementArrearAcquitanceDetails Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "IncrementArrear.txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "IAACQ", asondate.substring(3, 5), asondate.substring(6, 10));
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            System.out.println("filePathwithName = " + filePathwithName);
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
            map = supplementaryBillService.EmployeeSupplementaryIncrementArrearAcquitanceDetails(null, request, response, null, null, asondate, filePathwithName);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward supplementaryPaidDetailsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeSupplementaryPaidDetailsPage");
    }

    public Map getSupplementaryPaidDetails(String startingdate, String enddate, String supplementarytype, String epfno, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class getSupplementaryPaidDetails Method is calling ************************");
        return SupplementaryBillServiceObj.getSupplementaryPaidDetails(null, request, response, null, null, startingdate, enddate, supplementarytype, epfno);
    }

    public ActionForward employeeSupplementaryAcquaintancePrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeSupplementaryAcquaintancePrint");
    }

    public Map employeeSupplementaryAcquaintancePrintOut(String asondate, String billtype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class employeeSupplementaryAcquaintancePrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Slic" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "SACQ", asondate.substring(3, 5), asondate.substring(6, 10));
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.employeeSupplementaryAcquaintancePrintOut(null, request, response, null, null, asondate, billtype, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward employeeSupplementaryIncrementArrearAbstractPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeSupplementaryIncrementArrearAbstractPrintpage");
    }

    public Map EmployeeSupplementaryIncrementArrearAbstractDetails(String asondate, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeeSupplementaryIncrementArrearAbstractDetails Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "IncrementArrear.txt";
            String fileName = reportNameService.getFileName(null, request, response, null, null, "IAACQ", asondate.substring(3, 5), asondate.substring(6, 10));
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            String filePathwithName = filePath + "/" + fileName;
            System.out.println("filePathwithName = " + filePathwithName);
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
            map = supplementaryBillService.EmployeeSupplementaryIncrementArrearAbstractDetails(null, request, response, null, null, asondate, filePathwithName);

            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
    
    public ActionForward employeeSupplementaryEarningsLedgerPrintpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeSupplementaryEarningsLedgerPrintpage");
    }
    
    public Map EmployeeSupplementaryEarningsLedgerDetails(String asondate, String ledgertype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeeSupplementaryEarningsLedgerDetails Method is calling ************************");
        Map map = new HashMap();
        try {
            String fname = "";
            if("1".equalsIgnoreCase(ledgertype)){
                fname = "EAR";
            }else if("2".equalsIgnoreCase(ledgertype)){
                fname = "DED";
            }
            String fileName = reportNameService.getFileName(null, request, response, null, null, fname, asondate.substring(3, 5), asondate.substring(6, 10));
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            if("1".equalsIgnoreCase(ledgertype)){
                map = supplementaryBillService.employeeSupplementaryEarningsLedgerPrintOut(null, request, response, null, null, asondate, filePathwithName);
            }else if("2".equalsIgnoreCase(ledgertype)){
                map = supplementaryBillService.employeeSupplementaryDeductionLedgerPrintOut(null, request, response, null, null, asondate, filePathwithName);
            }
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward employeeSupplementaryDADBFPrintPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class employeeSupplementaryDADBFPrintPage Method is calling ************************");
        return mapping.findForward("employeeSupplementaryDADBFPrintPage");
    }

    public Map EmployeeSupplementaryDADBFPrintOut(String dabatch, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeeSupplementaryDADBFPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Slic" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
//            String fileName = reportNameService.getFileName(null, request, response, null, null, "SACQ", asondate.substring(3, 5), asondate.substring(6, 10));
            String fileName = dabatch + "DBF";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryDADBFPrintOut(null, request, response, null, null, dabatch, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward employeeSupplementaryDAAbstractPrintPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class employeeSupplementaryDADBFPrintPage Method is calling ************************");
        return mapping.findForward("employeeSupplementaryDAAbstractPrintPage");
    }

    public Map EmployeeSupplementaryDAAbstractPrintOut(String dabatch, String paymentmode,String amonth,String ayear,String bmonth,String byear, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeeSupplementaryDAAbstractPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Slic" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
//            String fileName = reportNameService.getFileName(null, request, response, null, null, "SACQ", asondate.substring(3, 5), asondate.substring(6, 10));
            String fileName = dabatch + "DBF";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryDAAbstractPrintOut(null, request, response, null, null, dabatch, paymentmode, amonth, ayear, bmonth, byear, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward employeeSupplementaryDALedgerPrintPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class employeeSupplementaryDALedgerPrintPage Method is calling ************************");
        return mapping.findForward("employeeSupplementaryDALedgerPrintPage");
    }

    public Map EmployeeSupplementaryDALedgerPrintOut(String dabatch, String month, String year, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** SupplementaryBillAction.class EmployeeSupplementaryDALedgerPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
//            String fileName = null;
//            fileName = "Slic" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
//            String fileName = reportNameService.getFileName(null, request, response, null, null, "SACQ", asondate.substring(3, 5), asondate.substring(6, 10));
            String fileName = dabatch + "Ledger";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);

            SupplementaryBillService supplementaryBillService = (SupplementaryBillService) injector.getInstance(SupplementaryBillService.class);
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
            map = supplementaryBillService.EmployeeSupplementaryDALedgerPrintOut(null, request, response, null, null, dabatch, month, year, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
}
