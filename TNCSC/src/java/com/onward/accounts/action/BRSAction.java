/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.accounts.dao.BRSService;
import com.onward.action.OnwardAction;
import com.onward.dao.OeslModule;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Karthikeyan S
 */
public class BRSAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    BRSService bRSService = (BRSService) injector.getInstance(BRSService.class);

    public ActionForward bankReconciliationUploadPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("bankReconciliationUploadPage");
    }

    public Map getAccountBook(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("************************ BRSAction getAccountBook ***********************************8");
        return bRSService.getAccountBook(null, request, response, null, null);
    }

    public ActionForward uploadReconciliationFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("************************ BankEntryAction class uploadReconciliationFile method is calling ********************");
        try {
            DynaValidatorForm dvf = (DynaValidatorForm) form;
            String month = (String) dvf.get("month");
            String year = (String) dvf.get("year");
            String accountbook = (String) dvf.get("accountbook");
            String accountingperiod = (String) dvf.get("accountingperiod");
            FormFile formFile = (FormFile) dvf.get("fileuploadname");
            String uploadingFileName = formFile.getFileName();

            String uploadPath = getServlet().getServletContext().getRealPath("/") + "/upload";
            String serverPath = uploadPath + "/" + uploadingFileName;

            File folder = new File(uploadPath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            File uploadFileObject = null;
            if (!uploadingFileName.equals("")) {
                uploadFileObject = new File(serverPath);
                FileOutputStream fileOutStream = new FileOutputStream(uploadFileObject);
                fileOutStream.write(formFile.getFileData());
                fileOutStream.flush();
                fileOutStream.close();
            }

            File file = new File(serverPath);
            if (!file.exists()) {
                return mapping.findForward("bankReconciliationUploadPage");
            } else {
                Map resultMap = bRSService.uploadReconciliationFile(null, request, response, null, null, month, year, accountbook, accountingperiod, serverPath);
//                request.setAttribute("message", resultMap.get("message"));
//                if (uploadFileObject.exists()) {
//                    uploadFileObject.delete();
//                }
                if (((String) resultMap.get("ERROR")) == null) {
                    request.setAttribute("message", "Successfully Uploaded");
                } else {
                    request.setAttribute("message", (String) resultMap.get("ERROR"));
                }
            }
//           

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mapping.findForward("bankReconciliationUploadPage");
    }

    public ActionForward brsCheckListPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("brsCheckListPage");
    }

    public Map getRegionWiseBrsDetails(String month, String year, String accountbook, String accountingperiod, String region, HttpServletRequest request, HttpServletResponse response) {
        return bRSService.getRegionWiseBrsDetails(null, request, response, null, null, month, year, accountbook, accountingperiod, region);
    }

    public Map saveBRSApprovalList(String month, String year, String accountbook, String accountingperiod, String region, String approvalarray, HttpServletRequest request, HttpServletResponse response) {
        return bRSService.saveBRSApprovalList(null, request, response, null, null, month, year, accountbook, accountingperiod, region, approvalarray);
    }

    public ActionForward brsReportPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("brsReportPage");
    }

    public Map getRegionWiseBrsReportDetails(String month, String year, String accountbook, String accountingperiod, String region, String status, HttpServletRequest request, HttpServletResponse response) {
        return bRSService.getRegionWiseBrsReportDetails(null, request, response, null, null, month, year, accountbook, accountingperiod, region, status);
    }

    public ActionForward brsDetailsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("brsDetailsPage");
    }

    public Map getBrsReportDetails(String month, String year, String accountbook, String accountingperiod, HttpServletRequest request, HttpServletResponse response) {
        return bRSService.getBrsReportDetails(null, request, response, null, null, month, year, accountbook, accountingperiod);
    }

    public Map BRSRegionWiseReport(String month, String year, String accountbook, String accountingperiod, String region, String status, HttpServletRequest request, HttpServletResponse response) {

        System.out.println("********************** BRSAction.class BRSRegionWiseReport Method is calling ************************");
        Map map = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
//            String fileName = reportNameService.getFileName(null, request, response, null, null, accountbookno);
//            String fileName = "Sales" + months[Integer.valueOf(month)] + year.substring(2, 4) + ".xls";
            String fileName = region + "BRSReport" + ".xls";
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
            map = bRSService.BRSRegionWiseReport(null, request, response, null, null, month, year, accountbook, accountingperiod, region, status, filePathwithName);
            map.put("fileName", fileName);
            map.put("filePath", filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
}
