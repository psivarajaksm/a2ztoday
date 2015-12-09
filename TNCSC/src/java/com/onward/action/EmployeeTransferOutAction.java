/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.EmployeePayBillService;
import com.onward.dao.EmployeeTransferOutService;
import com.onward.dao.OeslModule;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
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
 * @author root
 */
public class EmployeeTransferOutAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    EmployeeTransferOutService EmployeeTransferAndPostingsServiceObj = (EmployeeTransferOutService) injector.getInstance(EmployeeTransferOutService.class);

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward transferandpostingsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("transferandpostingsPage");
    }

    public ActionForward transferandpostingsUpdatePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("transferandpostingsUpdatePage");
    }

    /**
     *
     * @param epfno Employee Provident Fund Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains Employee Name and Current Region
     */
    public Map loadEmployeeDetails(String epfno, HttpServletRequest request, HttpServletResponse response) {
        return EmployeeTransferAndPostingsServiceObj.loadEmployeeDetails(null, request, response, null, null, epfno);
    }

    /**
     *
     * @param epfno Employee Provident Fund Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains Employee Name and Current Region
     */
    public Map loadTransferEmployeeDetails(String epfno, HttpServletRequest request, HttpServletResponse response) {
        return EmployeeTransferAndPostingsServiceObj.loadTransferEmployeeDetails(null, request, response, null, null, epfno);
    }

    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains List of all Region Name
     */
    public Map loadRegionDetails(HttpServletRequest request, HttpServletResponse response) {
        return EmployeeTransferAndPostingsServiceObj.loadRegionDetails(null, request, response, null, null);
    }

    /**
     *
     * @param epfno Employee Provident Fund Number
     * @param regioncode Employee Transfered Region Code
     * @param transferdate Transfer Date
     * @param employeename Employee Name
     * @param regiontext Employee Transfered Region Name
     * @param request
     * @param response
     * @return It Returns map.Map Contains result of the save Transaction.
     */
//    public Map saveEmployeeTransfer(String epfno, HttpServletRequest request, HttpServletResponse response) {
//        Map map = new HashMap();
//        try {
//            String fileName = epfno + ".txt";
//            OeslModule notOnWeekendsModule = new OeslModule();
//            Injector injector = Guice.createInjector(notOnWeekendsModule);
//            EmployeePayBillService EmployeePayBillServiceObj = (EmployeePayBillService) injector.getInstance(EmployeePayBillService.class);
//            String filePath = request.getRealPath("/") + "employeedetails";
//            String filePathwithName = filePath + "/" + fileName;
//            //create the upload folder if not exists
//            File folder = new File(filePath);
//            if (!folder.exists()) {
//                folder.mkdir();
//            } else {
//                File ex_file = new File(filePath + "/" + fileName);
//                if (ex_file.exists()) {
//                    ex_file.delete();
//                }
//                ex_file.setReadOnly();
//            }
//            map = EmployeeTransferAndPostingsServiceObj.createEmployeeDetails(null, request, response, null, null, filePathwithName, epfno);
//            map.put("fileName", fileName);
//            map.put("filePath", filePathwithName);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return map;
//
//
//    }
        public Map saveEmployeeTransfer(String epfno,String transferregion, HttpServletRequest request, HttpServletResponse response) {
            return EmployeeTransferAndPostingsServiceObj.saveEmployeeTransfer(null, request, response, null, null, epfno,transferregion);

        }

        public Map saveTransferedEmployee(String epfno, String section, HttpServletRequest request, HttpServletResponse response) {
            return EmployeeTransferAndPostingsServiceObj.saveTransferedEmployee(null, request, response, null, null, epfno, section);

        }

    public ActionForward PopupReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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

    public Map checkuploadexists(String epfno, HttpServletRequest request, HttpServletResponse response) {
        return EmployeeTransferAndPostingsServiceObj.checkuploadexists(null, request, response, null, null, epfno);
    }

    public Map displayFileDatas(String filename, HttpServletRequest request, HttpServletResponse response) {

        return EmployeeTransferAndPostingsServiceObj.displayFileDatas(null, request, response, null, null, filename);
    }

    public Map saveUploadedDatas(String filename, HttpServletRequest request, HttpServletResponse response) {

        return EmployeeTransferAndPostingsServiceObj.saveUploadedDatas(null, request, response, null, null, filename);
    }

    public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaValidatorForm formObj = (DynaValidatorForm) form;
        Map resultMap = new HashMap();
        String epfno = "";
        try {
            FormFile formFile = (FormFile) formObj.get("fileuploadname");
            resultMap = EmployeeTransferAndPostingsServiceObj.upLoadTxtFile(null, request, response, null, null, formFile, epfno);
            request.removeAttribute("filepath");
            request.removeAttribute("message");
            request.setAttribute("filepath", resultMap.get("filepath"));
            request.setAttribute("message", resultMap.get("message"));
            System.out.println("Test==" + resultMap.get("filepath"));
            System.out.println("message==" + resultMap.get("message"));
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("message", "Failed to upload");
        }
        return mapping.findForward("transferandpostingsUpdatePage");

    }
}
