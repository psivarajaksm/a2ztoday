/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.budget.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.action.OnwardAction;
import com.onward.budget.dao.FileUploadService;
import com.onward.common.DateParser;
import com.onward.dao.OeslModule;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author root
 */
public class FileUploadAction extends OnwardAction {

    DateParser dateParser = new DateParser();
    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    FileUploadService fileUploadServiceObj = (FileUploadService) injector.getInstance(FileUploadService.class);
    /*
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */

    public ActionForward fileUploadPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("fileUploadPage");
    }

    /**
     *
     * @param request
     * @param response
     * @return        It Returns map.Map Contains List of existing Region Details.
     */
    public Map loadRegionDetails(HttpServletRequest request, HttpServletResponse response) {
        return fileUploadServiceObj.loadRegionDetails(null, request, response, null, null);
    }

    /**
     *
     * @param request
     * @param response
     * @return        
     */
//    public Map upLoadTxtFile(String filename,HttpServletRequest request, HttpServletResponse response) {
//        return fileUploadServiceObj.upLoadTxtFile(null, request, response, null, null,filename);
//    }
//    public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//        DynaValidatorForm formObj = (DynaValidatorForm) form;
//        Map resultMap = new HashMap();
//        try {
//            FormFile formFile = (FormFile) formObj.get("fileuploadname");
//            resultMap = fileUploadServiceObj.upLoadTxtFile(null, request, response, null, null, formFile);
//            request.setAttribute("message", resultMap.get("message"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
////            request.setAttribute("message", "Failed to upload");
//        }
//        return mapping.findForward("fileUploadPage");
//
//    }
//
//    /**
//     *
//     * @param request
//     * @param response
//     * @return        It Returns map.Map Contains List of existing Region Details.
//     */
//    public Map displayTextFileDatas(HttpServletRequest request, HttpServletResponse response) {
//        return fileUploadServiceObj.displayTextFileDatas(null, request, response, null, null);
//    }
//
//    public Map getExistingDatas(String region,String month,String year,HttpServletRequest request, HttpServletResponse response) {
//        return fileUploadServiceObj.getExistingDatas(null, request, response, null, null,region,month,year);
//    }
//    public Map uploadTxtFileDatatoDB(String isolddata,String region,String month,String year,HttpServletRequest request, HttpServletResponse response) {
//        return fileUploadServiceObj.uploadTxtFileDatatoDB(null, request, response, null, null,isolddata,region,month,year);
//    }
}
