/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.budget.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.action.OnwardAction;
import com.onward.budget.dao.BudgetService;
import com.onward.dao.OeslModule;
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
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author user
 */
public class BudgetAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    BudgetService budgetServiceObj = (BudgetService) injector.getInstance(BudgetService.class);
    private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public ActionForward budgetRevisionEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("budgetRevisionEntry");
    }

    public ActionForward BudgetUpdateStatusPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("BudgetUpdateStatusPage");
    }

    public ActionForward budgetRevisionEntryHo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("budgetRevisionEntryHo");
    }

    public ActionForward budgetUploadPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("budgetUploadPage");
    }

    public Map getBudgetForRevision(String region, String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        return budgetServiceObj.getBudgetForRevision(null, request, response, null, null, region, startyear, endyear);
    }

    public ActionForward budgetReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("budgetReportPage");
    }
    
    public ActionForward proposesSalaryExpenses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("proposesSalaryExpenses");
    }    

    public ActionForward budgetReportHO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("****************** BudgetAction class budgetReportHO method is calling ***********************");
        request.getSession().setAttribute("regionList", budgetServiceObj.getRegionList(null, request, response, null, null));
        return mapping.findForward("budgetReportHOPage");
    }

    public ActionForward budgetRegionReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("budgetRegionReportPage");
    }

    public ActionForward budgetschedulereport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("ledgerList", budgetServiceObj.getBudgetLedgerList(null, request, response, null, null));
        return mapping.findForward("budgetschedulereportPage");
    }

    public Map getBudgetForRevisionHO(String region, String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        return budgetServiceObj.getBudgetForRevisionHO(null, request, response, null, null, region, startyear, endyear);
    }

    public Map saveBudgetDetails(String budgetdetailsid, String ledgerid, String ledgername, String fistactual, String secondactual, String thirdactual, String average, String currentbudgetestimate, String actualfirsthalf, String probablesecondhalf, String currentrevisedestimate, String nextyearbudgetestimate, String currentrevisedestimateho, String nextyearbudgetestimateho, HttpServletRequest request, HttpServletResponse response) {

        return budgetServiceObj.saveBudgetDetails(null, request, response, null, null, budgetdetailsid, ledgerid, ledgername, fistactual, secondactual, thirdactual, average, currentbudgetestimate, actualfirsthalf, probablesecondhalf, currentrevisedestimate, nextyearbudgetestimate, currentrevisedestimateho, nextyearbudgetestimateho);
    }

    public Map saveBudgetDetailsHO(String budgetdetailsid, String ledgerid, String ledgername, String fistactual, String secondactual, String thirdactual, String average, String currentbudgetestimate, String actualfirsthalf, String probablesecondhalf, String currentrevisedestimate, String nextyearbudgetestimate, String currentrevisedestimateho, String nextyearbudgetestimateho, HttpServletRequest request, HttpServletResponse response) {

        return budgetServiceObj.saveBudgetDetailsHO(null, request, response, null, null, budgetdetailsid, ledgerid, ledgername, fistactual, secondactual, thirdactual, average, currentbudgetestimate, actualfirsthalf, probablesecondhalf, currentrevisedestimate, nextyearbudgetestimate, currentrevisedestimateho, nextyearbudgetestimateho);
    }

    public ActionForward downLoadBudgetDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaValidatorForm dvf = (DynaValidatorForm) form;
        String startYear = (String) dvf.get("startyear");
        String endYear = (String) dvf.get("endyear");


        String fileName = budgetServiceObj.getMyRegionCode(null, request, response, null, null);
        fileName = fileName + "buddn";
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".txt");

        String filePath = request.getRealPath("/");
        File file = new File(filePath + "/" + fileName + ".txt");

        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdir();
        } else {
            File ex_file = new File(filePath + "/" + fileName + ".txt");
            if (ex_file.exists()) {
                ex_file.delete();
            }
        }
        budgetServiceObj.createBudgetDetails(null, request, response, null, null, filePath, startYear, endYear);
        if (!file.exists()) {
            System.out.println("File NOt FOund");
        } else {
            try {
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
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains List of existing Region Details.
     */
    public Map loadRegionDetails(HttpServletRequest request, HttpServletResponse response) {
        return budgetServiceObj.loadRegionDetails(null, request, response, null, null);
    }

    public Map checkuploadexists(String region, String accyear, HttpServletRequest request, HttpServletResponse response) {

        return budgetServiceObj.checkuploadexists(null, request, response, null, null, region, accyear);
    }

    public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaValidatorForm formObj = (DynaValidatorForm) form;
        Map resultMap = new HashMap();
        try {
            FormFile formFile = (FormFile) formObj.get("fileuploadname");
            resultMap = budgetServiceObj.upLoadTxtFile(null, request, response, null, null, formFile);
            request.setAttribute("message", resultMap.get("message"));
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("message", "Failed to upload");
        }
        return mapping.findForward("budgetUploadPage");

    }

    public Map displayTextFileDatas(HttpServletRequest request, HttpServletResponse response) {

        return budgetServiceObj.displayTextFileDatas(null, request, response, null, null);
    }

    public Map saveTextFiletoDB(HttpServletRequest request, HttpServletResponse response) {

        return budgetServiceObj.saveTextFiletoDB(null, request, response, null, null);
    }

    public ActionForward PopupReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*********************** PopupReport is calling ***************************");
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

    public Map BudgetReportPrintOut(String region, String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** BudgetAction.class BudgetReportPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = null;
//            fileName = budgetServiceObj.getMyRegionCode(null, request, response, null, null);
            fileName = region + "Bud.txt";
//            fileName = "Aslp" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            System.out.println("filePath = " + filePath);
            String filePathwithName = filePath + "/" + fileName;
            //create the upload folder if not exists
            File folder = new File(filePath);
            if (!folder.exists()) {
                System.out.println("::::::::::::::::::::::::::: Folder Not Exist :::::::::::::::::::::::::::");
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = budgetServiceObj.BudgetReportPrint(null, request, response, null, null, filePathwithName, region, startyear, endyear);


            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map BudgetReportHOPrintOut(String region, String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** BudgetAction.class BudgetReportHOPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = null;
            fileName = budgetServiceObj.getMyRegionCode(null, request, response, null, null);
            fileName = region + "Bud.txt";
//            fileName = "Aslp" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            System.out.println("filePath = " + filePath);
            String filePathwithName = filePath + "/" + fileName;
            //create the upload folder if not exists
            File folder = new File(filePath);
            if (!folder.exists()) {
                System.out.println("::::::::::::::::::::::::::: Folder Not Exist :::::::::::::::::::::::::::");
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = budgetServiceObj.BudgetReportHOPrint(null, request, response, null, null, filePathwithName, region, startyear, endyear);


            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map BudgetScheduleReportPrintOut(String ledgerid, String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** BudgetAction.class BudgetScheduleReportPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = null;
            fileName = budgetServiceObj.getMyRegionCode(null, request, response, null, null);
            fileName = ledgerid + "Bud.txt";
//            fileName = "Aslp" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            System.out.println("filePath = " + filePath);
            String filePathwithName = filePath + "/" + fileName;
            //create the upload folder if not exists
            File folder = new File(filePath);
            if (!folder.exists()) {
                System.out.println("::::::::::::::::::::::::::: Folder Not Exist :::::::::::::::::::::::::::");
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = budgetServiceObj.BudgetScheduleReportPrintOut(null, request, response, null, null, filePathwithName, ledgerid, startyear, endyear);


            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map BudgetReportRegionPrintOut(String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** BudgetAction.class BudgetReportRegionPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = null;
            fileName = budgetServiceObj.getMyRegionCode(null, request, response, null, null);
            fileName = fileName + "Bud.txt";
//            fileName = "Aslp" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            System.out.println("filePath = " + filePath);
            String filePathwithName = filePath + "/" + fileName;
            //create the upload folder if not exists
            File folder = new File(filePath);
            if (!folder.exists()) {
                System.out.println("::::::::::::::::::::::::::: Folder Not Exist :::::::::::::::::::::::::::");
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = budgetServiceObj.BudgetReportRegionPrint(null, request, response, null, null, filePathwithName, startyear, endyear);


            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map getBudgetUpdationDetailsDetails(String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        return budgetServiceObj.getBudgetUpdationDetailsDetails(null, request, response, null, null, startyear, endyear);
    }

    public ActionForward budgetMonthlyEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("isMonthlyEntryAdmin", "No");
        return mapping.findForward("budgetMonthlyEntryPage");
    }

    public ActionForward budgetAdminMonthlyEntryPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("isMonthlyEntryAdmin", "Yes");
        request.getSession().setAttribute("regionlist", budgetServiceObj.getRegionList(null, request, response, null, null));
        return mapping.findForward("budgetMonthlyEntryPage");
    }

    public Map getMonthExpenditure(String month, String year, String budgetid, String region, HttpServletRequest request, HttpServletResponse response) {
        return budgetServiceObj.getMonthExpenditure(null, request, response, null, null, month, year, budgetid, region);
    }

    public Map loadBudgetYearDetails(HttpServletRequest request, HttpServletResponse response) {
        return budgetServiceObj.loadBudgetYearDetails(null, request, response, null, null);
    }

    public ActionForward ControlofExpenditureReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("ControlofExpenditureReportPage");
    }

    public ActionForward controlOfExpenditureBoard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("controlOfExpenditureBoard");
    }

    public Map ControlofExpenditureReportPrintOut(String region, String smonth, String syear, String period, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** BudgetAction.class ControlofExpenditureReportPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            int month = Integer.valueOf(smonth) + 1;
            String fileName = region + "Expenditure" + months[month - 1] + syear.substring(2, 4) + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            System.out.println("filePath = " + filePath);
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
            map = budgetServiceObj.ControlofExpenditureReportPrint(null, request, response, null, null, filePathwithName, region, String.valueOf(month), syear, period);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map saveBudgetExpenditureDetails(String expendituredetailsid, String ledgerid, String monthexpenditure, String fmaamount, String region, HttpServletRequest request, HttpServletResponse response) {
        return budgetServiceObj.saveBudgetExpenditureDetails(null, request, response, null, null, expendituredetailsid, ledgerid, monthexpenditure, fmaamount, region);
    }

    public ActionForward fmaProbableEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("fmaProbableEntryPage");
    }

    public Map getBudgetForFMAProbable(String region, String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        return budgetServiceObj.getBudgetForFMAProbable(null, request, response, null, null, region, startyear, endyear);
    }

    public Map saveProbableFMADetails(String budgetdetailsid, String ledgerid, String ledgername, String fmaamt, String hofmaamt, HttpServletRequest request, HttpServletResponse response) {

        return budgetServiceObj.saveProbableFMADetails(null, request, response, null, null, budgetdetailsid, ledgerid, ledgername, fmaamt, hofmaamt);
    }

    public ActionForward fmaHORevisionEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("fmaHORevisionEntryPage");
    }

    public Map getHOBudgetForFMARevision(String region, String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        return budgetServiceObj.getHOBudgetForFMARevision(null, request, response, null, null, region, startyear, endyear);
    }

    public Map saveHORevisionFMADetails(String budgetdetailsid, String ledgerid, String ledgername, String fmaamt, String hofmaamt, HttpServletRequest request, HttpServletResponse response) {

        return budgetServiceObj.saveHORevisionFMADetails(null, request, response, null, null, budgetdetailsid, ledgerid, ledgername, fmaamt, hofmaamt);
    }

    public ActionForward ControlofExpenditureAccountHeadReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("ledgerList", budgetServiceObj.getBudgetLedgerList(null, request, response, null, null));
        return mapping.findForward("ControlofExpenditureAccountHeadReportPage");
    }

    public Map ControlofExpenditureAccountReportPrintOut(String ledgerid, String smonth, String syear, String period, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** BudgetAction.class ControlofExpenditureAccountReportPrintOut Method is calling ************************");
        Map map = new HashMap();
        System.out.println("ledgerid = " + ledgerid);
        try {
            int month = Integer.valueOf(smonth) + 1;
            String fileName = ledgerid + "Expenditure" + months[month - 1] + syear.substring(2, 4) + ".txt";
            String filePath = request.getRealPath("/") + "PayBillPrint";
            System.out.println("filePath = " + filePath);
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
            map = budgetServiceObj.ControlofExpenditureAccountReportPrint(null, request, response, null, null, filePathwithName, ledgerid, String.valueOf(month), syear, period);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public ActionForward budgetFMAReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("budgetFMAReportPage");
    }

    public Map BudgetFMAReportPrintOut(String region, String startyear, String endyear, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** BudgetAction.class BudgetReportPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = null;
//            fileName = budgetServiceObj.getMyRegionCode(null, request, response, null, null);
            fileName = region + "Bud.txt";
//            fileName = "Aslp" + months[Integer.valueOf(month) - 1] + year.substring(2, 4) + ".txt";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            String filePath = request.getRealPath("/") + "PayBillPrint";
            System.out.println("filePath = " + filePath);
            String filePathwithName = filePath + "/" + fileName;
            //create the upload folder if not exists
            File folder = new File(filePath);
            if (!folder.exists()) {
                System.out.println("::::::::::::::::::::::::::: Folder Not Exist :::::::::::::::::::::::::::");
                folder.mkdir();
            } else {
                File ex_file = new File(filePath + "/" + fileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }
            map = budgetServiceObj.BudgetFMAReportPrint(null, request, response, null, null, filePathwithName, region, startyear, endyear);


            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map prepareControlOfExpenditure(String accountingperiod, String accountperiodfrom, String accountperiodto, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("********************** EmployeePayBillAction.class EmployeePayBillPrintOut Method is calling ************************");
        Map map = new HashMap();
        try {
            String fileName = "controlofexpenditureboard.txt";
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            //EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
            String filePath = request.getRealPath("/") + "controlofexpenditure";
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
            map = budgetServiceObj.controlOfEpenditure(null, request, response, null, null, accountingperiod, accountperiodfrom, accountperiodto, filePathwithName);
            System.out.println("fileName " + fileName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public Map getPayDetailsForBudgerExcellWrite(String region, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
            String regioncode = (String) request.getSession().getAttribute("regioncode");
            String fileName = "salexp" + ".xls";
            String filePath = request.getRealPath("/") + "budget";
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
            map = budgetServiceObj.getPayDetailsForBudgerExcellWrite(null, request, response, null, null,region, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;

    }
}
