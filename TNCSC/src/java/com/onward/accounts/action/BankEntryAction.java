/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.accounts.dao.BankEntryService;
import com.onward.action.OnwardAction;
import com.onward.dao.OeslModule;
import java.io.File;
import java.io.FileOutputStream;
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
 * @author root
 */
public class BankEntryAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    BankEntryService bankEntryService = (BankEntryService) injector.getInstance(BankEntryService.class);

    public ActionForward bankReconciliationUploadPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("bankReconciliationUploadPage");
    }

    public Map getBankDetails(HttpServletRequest request, HttpServletResponse response) {
        return bankEntryService.getBankDetails(null, request, response, null, null);
    }

    public ActionForward uploadReconciliationFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("************************ BankEntryAction class uploadReconciliationFile method is calling ********************");
        try {
            DynaValidatorForm dvf = (DynaValidatorForm) form;
            String month = (String) dvf.get("month");
            String year = (String) dvf.get("year");
            String bankname = (String) dvf.get("bankname");
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

            Map resultMap = bankEntryService.uploadReconciliationFile(null, request, response, null, null, month, year, bankname, serverPath);
            
            if(uploadFileObject.exists()){
                uploadFileObject.delete();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mapping.findForward("bankReconciliationUploadPage");
    }
}
