/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.MasterService;
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
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author root
 */
public class PayBillMasterAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    MasterService MasterServiceObj = (MasterService) injector.getInstance(MasterService.class);

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward payCodeMasterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("payCodeMasterPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward regionMasterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("regionMasterPage");
    }
     /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward sectionMasterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("sectionMasterPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward designationMasterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("designationMasterPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward ccaAndHRAMasterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("ccaAndHRAMasterPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward stopPaymentPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("stopPaymentPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeAccountAssignPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeAccountAssignPage");
    }
    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward slapDetailsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("slapDetailsPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward MastersUploadPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("mastersUploadPage");
    }

    /**
     *
     * @param paycodetype       Currently selected type of Pay code
     * @param orderbyString     Map order by the paycode or paycode name
     * @param request
     * @param response
     * @return                  It Returns map.Map Contains Next Paycode Serial Number
     */
    public Map getPayCodeSerial(String paycodetype, String orderbyString, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getPayCodeSerial(null, request, response, null, null, paycodetype, orderbyString);
    }

    /**
     *
     * @param paycodetype       Selected Pay Code Type
     * @param paycode           Auto generated Paycode Serial Number
     * @param paycodename       Pay code Name
     * @param paypercentage     Pay code Percentage
     * @param request
     * @param response
     * @return          It Returns map.Map Contains result of the save Transaction.
     */
    public Map savePayCodeMaster(String paycodetype, String paycode, String paycodename, String paypercentage, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.savePayCodeMaster(null, request, response, null, null, paycodetype, paycode, paycodename, paypercentage);
    }

//    /**
//     *
//     * @param regiontype        Selected Region Type
//     * @param request
//     * @param response
//     * @return                  It Returns map.Map Contains Next  Region Serial Number
//     */
//    public Map getRegionCodeSerial(String regiontype, HttpServletRequest request, HttpServletResponse response) {
//        return MasterServiceObj.getRegionCodeSerial(null, request, response, null, null, regiontype);
//    }

    /**
     *
     * @param regioncode        Newly Created Region code
     * @param regionname        Newly Created Region Name
     * @param request
     * @param response
     * @return          It Returns map.Map Contains result of the save Transaction.
     */
    public Map saveRegionMaster(String regioncode, String regionname, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.saveRegionMaster(null, request, response, null, null, regioncode, regionname);
    }

    /**
     *
     * @param sectioncode        Newly Created Section code
     * @param sectionname        Newly Created Section Name
     * @param request
     * @param response
     * @return          It Returns map.Map Contains result of the save Transaction.
     */
    public Map saveSectionMaster(String region,String parentcode,String sectioncode, String sectionname, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.saveSectionMaster(null, request, response, null, null, region,parentcode,sectioncode, sectionname);
    }

    /**
     *
     * @param sectiontype      Selected Section Type
     * @param request
     * @param response
     * @return                  It Returns map.Map Contains Next  Section Serial Number
     */
//    public Map getSectionCodeSerial(String sectiontype, HttpServletRequest request, HttpServletResponse response) {
//        return MasterServiceObj.getSectionCodeSerial(null, request, response, null, null, sectiontype);
//    }
//
//    /**
//     *
//     * @param sectioncode        Newly Created Section code
//     * @param sectionname        Newly Created Section Name
//     * @param request
//     * @param response
//     * @return          It Returns map.Map Contains result of the save Transaction.
//     */
//    public Map saveSectionMaster(String sectioncode, String sectionname, HttpServletRequest request, HttpServletResponse response) {
//        return MasterServiceObj.saveSectionMaster(null, request, response, null, null, sectioncode, sectionname);
//    }

    /**
     *
     * @param cadretype         Employee's Cadre Type         
     * @param wingtype          Employee's Wing Type         
     * @param request
     * @param response
     * @return                 It Returns map.Map Contains Next Designation code Serial Number
     */
    public Map getDesignationCodeSerial(String cadretype, String wingtype, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getDesignationCodeSerial(null, request, response, null, null, cadretype, wingtype);
    }

    /**
     *
     * @param designationcode       Newly Created Designation code 
     * @param designationname       Newly Created Designation Name 
     * @param payscalecode          Newly Created Designation Pay scale 
     * @param orderno               order number of Designation creation
     * @param request
     * @param response
     * @return          It Returns map.Map Contains result of the save Transaction.
     */
    public Map saveDesignationMaster(String designationcode, String designationname, String payscalecode, String orderno, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.saveDesignationMaster(null, request, response, null, null, designationcode, designationname, payscalecode, orderno);
    }

    /**
     *
     * @param request
     * @param response
     * @return         It Returns map.Map Contains List of existing Designation Details.
     */
    public Map getDesignationDetails(HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getDesignationDetails(null, request, response, null, null);
    }

//    /**
//     *
//     * @param request
//     * @param response
//     * @return         It Returns map.Map Contains List of existing Region or Section Details.
//     */
//    public Map getRegionAndSectionDetails(HttpServletRequest request, HttpServletResponse response) {
//        return MasterServiceObj.getRegionAndSectionDetails(null, request, response, null, null);
//    }
      /**
     *
     * @param request
     * @param response
     * @return         It Returns map.Map Contains List of existing Region or Section Details.
     */
    public Map getRegionDetails(HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getRegionDetails(null, request, response, null, null);
    }
    /**
     *
     * @param request
     * @param response
     * @return         It Returns map.Map Contains List of existing Region or Section Details.
     */
    public Map getSectionDetails(HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getSectionDetails(null, request, response, null, null);
    }


    /**
     *
     * @param request
     * @param response
     * @return         It Returns map.Map Contains List of existing Earnings or Deductions or Loans Details.
     */
    public Map getLoadEarningsDetails(HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getLoadEarningsDetails(null, request, response, null, null);
    }

    /**
     *
     * @param request
     * @param response
     * @param paytype
     * @return
     */
    public Map getLoadAssignedPayCodes(HttpServletRequest request, HttpServletResponse response, String paytype) {
        return MasterServiceObj.getLoadAssignedPayCodes(null, request, response, null, null, paytype);
    }

    /**
     *
     * @param paytype           Selected pay code 
     * @param paycodeIds        slap pay codes
     * @param request
     * @param response
     * @return          It Returns map.Map Contains result of the save Transaction.
     */
    public Map saveCCAHRAMaster(String paytype, String paycodeIds, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.saveCCAHRAMaster(null, request, response, null, null, paytype, paycodeIds);
    }

    /**
     *
     * @param epfno             Employee Provident Fund Number
     * @param request
     * @param response
     * @return         It Returns map.Map Contains Employee Name
     */
    public Map loadEmployeeDetails(String epfno, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.loadEmployeeDetails(null, request, response, null, null, epfno);
    }

    /**
     *
     * @param epfno             Employee Provident Fund Number
     * @param reasontype        Reason Type foe stop Payment
     * @param reasondate        Stop payment Date
     * @param remarks           Stop Payment Remarks
     * @param request
     * @param response
     * @return          It Returns map.Map Contains result of the save Transaction.
     */
    public Map saveStopPayment(String epfno, String reasontype, String reasondate, String remarks, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.saveStopPayment(null, request, response, null, null, epfno, reasontype, reasondate, remarks);
    }

    /**
     *
     * @param epfno             Employee Provident Fund Number
     * @param request
     * @param response
     * @return         It Returns map.Map Contains Employee Name
     */
    public Map loadEmployeeAccountDetails(String epfno, String deductioncode, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.loadEmployeeAccountDetails(null, request, response, null, null, epfno, deductioncode);
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public Map loadDeductionDetails(HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.loadDeductionDetails(null, request, response, null, null);

    }

    /**
     *
     * @param epfno             Employee Provident Fund Number
     * @param reasontype        Reason Type foe stop Payment
     * @param reasondate        Stop payment Date
     * @param remarks           Stop Payment Remarks
     * @param request
     * @param response
     * @return          It Returns map.Map Contains result of the save Transaction.
     */
    public Map saveEmployeeAccountCode(String deductioncode, String epfno, String empaccountcode, String serialno, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.saveEmployeeAccountCode(null, request, response, null, null, deductioncode, epfno, empaccountcode, serialno);
    }

    public ActionForward mastersDatasUploadSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        try {
            boolean errorFlag = false;

            DynaValidatorForm uploadForm = (DynaValidatorForm) form;

            FormFile uploadFile = (FormFile) uploadForm.get("uFile");

            String uploadingFileName = uploadFile.getFileName();
            String uploadPath = request.getRealPath("/") + "MastersUploadFiles";
            String filePathwithName = uploadPath + "/" + uploadingFileName;

            //create the upload folder if not exists
            File folder = new File(uploadPath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File ex_file = new File(uploadPath + "/" + uploadingFileName);
                if (ex_file.exists()) {
                    ex_file.delete();
                }
            }

            System.out.println("uploadPath = " + uploadPath);

            if (!uploadingFileName.equals("")) {

                File uploadFileObject = new File(uploadPath, uploadingFileName);

                FileOutputStream fileOutStream = new FileOutputStream(uploadFileObject);
                fileOutStream.write(uploadFile.getFileData());
                fileOutStream.flush();
                fileOutStream.close();
            } else {
                errorFlag = true;
            }
            Map resultMap = new HashMap();
//            System.out.println("filePathwithName -> " + filePathwithName);
            resultMap = MasterServiceObj.MasterDataUpdata(null, request, response, null, null, filePathwithName);
            request.setAttribute("status", resultMap.get("message"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mapping.findForward("mastersUploadPage");
    }

    public Map getLoadEarningsTypes(HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getLoadEarningsTypes(null, request, response, null, null);
    }
    public Map createRowinHTML(String noofrows, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.createRowinHTML(null, request, response, null, null, noofrows);
    }
    public Map getDatasForModify(String earningcode, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getDatasForModify(null, request, response, null, null, earningcode);
    }

    public Map saveEarningsSlapDetails(String earningstypes, String effectdate, String[] amountrangefrom, String[] amountrangeto, String[] slapamount, String[] slappercentage, String orderno, String totalrows, String[] hiddeniarray, String funtype, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.saveEarningsSlapDetails(null, request, response, null, null, earningstypes, effectdate, amountrangefrom, amountrangeto, slapamount, slappercentage, orderno, totalrows, hiddeniarray, funtype);
    }

    /**
     *
     * @param request
     * @param response
     * @return         It Returns map.Map Contains List of existing Earnings or Deductions or Loans Details.
     */
//    public Map getLoadSections(HttpServletRequest request, HttpServletResponse response) {
//        return MasterServiceObj.getLoadSections(null, request, response, null, null);
//    }
    /**
     *
     * @param request
     * @param response
     * @return         It Returns map.Map Contains List of existing Earnings or Deductions or Loans Details.
     */
    public Map getLoadRegionSections(String regionid,String sectionid,HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getLoadRegionSections(null, request, response, null, null,regionid,sectionid);
    }
}
