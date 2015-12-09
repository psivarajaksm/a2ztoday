/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.EmployeeMasterService;
import com.onward.dao.OeslModule;
import com.onward.persistence.payroll.Employeemaster;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author root
 */
public class EmployeeMasterAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    EmployeeMasterService EmployeeMasterServiceObj = (EmployeeMasterService) injector.getInstance(EmployeeMasterService.class);

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward employeeMasterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeMasterPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward governmentEmpMasterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("governmentEmpMasterPage");
    }
    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward changeEpfno(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("changeEpfno");
    }

    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains List of existing Region Details.
     */
    public Map loadRegionDetails(HttpServletRequest request, HttpServletResponse response) {
        return EmployeeMasterServiceObj.loadRegionDetails(null, request, response, null, null);
    }
    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains List of existing Region Details.
     */
    public Map loadRegionDetailsforDeputation(HttpServletRequest request, HttpServletResponse response) {
        return EmployeeMasterServiceObj.loadRegionDetailsforDeputation(null, request, response, null, null);
    }

    /**
     *
     * @param epfo Employee Provident Fund Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains check EPF No is Existing or not.
     */
    public Map getEmployeeEPFNo(String epfo, HttpServletRequest request, HttpServletResponse response) {
        return EmployeeMasterServiceObj.getEmployeeEPFNo(null, request, response, null, null, epfo);
    }
    /**
     *
     * @param epfo Employee Provident Fund Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains check EPF No is Existing or not.
     */
    public Map getEmployeeGPFNo(String epfo, HttpServletRequest request, HttpServletResponse response) {
        return EmployeeMasterServiceObj.getEmployeeGPFNo(null, request, response, null, null, epfo);
    }

    /**
     *
     * @param epfo Employee Provident Fund Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains Employee Details for appropriate EPF in EPF Master
     */
    public Map getEmployeeDetailsFromEPFMaster(String epfo, HttpServletRequest request, HttpServletResponse response) {
        return EmployeeMasterServiceObj.getEmployeeDetailsFromEPFMaster(null, request, response, null, null, epfo);
    }

    /**
     *
     * @param epfo Employee Provident Fund Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains Employee Details for appropriate EPF
     * No.
     */
    public Map getEmployeeDetails(String epfo, HttpServletRequest request, HttpServletResponse response) {
        return EmployeeMasterServiceObj.getEmployeeDetails(null, request, response, null, null, epfo);
    }
    /**
     *
     * @param epfo Employee Provident Fund Number
     * @param request
     * @param response
     * @return It Returns map.Map Contains Employee Details for appropriate EPF
     * No.
     */
    public Map getGovernmentEmployeeDetails(String epfo, HttpServletRequest request, HttpServletResponse response) {
        return EmployeeMasterServiceObj.getGovernmentEmployeeDetails(null, request, response, null, null, epfo);
    }

    /**
     *
     * @param epfno Employee Provident Fund Number
     * @param fpfno
     * @param employeename Employee Name
     * @param fathername Employee Father name
     * @param gender Gender
     * @param dateofbirth Employee Date of Birth
     * @param region Employee Current region
     * @param section Employee Current Section
     * @param dateofappoinment Employee Date of Appointment
     * @param dateofprobation Employee Probation Date
     * @param dateofconfirmation
     * @param designation Employee Designation
     * @param community Employee Community
     * @param pancardno Employee pancard number
     * @param paymentmode Employee Salary Payment Mode
     * @param bankcode Employee bank code
     * @param banksbaccount Employee bank account number
     * @param request
     * @param response
     * @return It Returns map.Map Contains result of the save Transaction.
     */
    public Map saveEmployeeMaster(String epfno, String fpfno, String employeename, String fathername, String gender, String dateofbirth, String region,
            String section, String dateofappoinment, String dateofprobation, String dateofconfirmation, String designation,
            String community, String pancardno, String paymentmode, String bankcode, String banksbaccount, String empcategory, String positiondate, String positiontime, String nativeregion, HttpServletRequest request, HttpServletResponse response) {

        return EmployeeMasterServiceObj.saveEmployeeMaster(null, request, response, null, null, epfno, fpfno, employeename, fathername,
                gender, dateofbirth, region, section, dateofappoinment, dateofprobation, dateofconfirmation, designation,
                community, pancardno, paymentmode, bankcode, banksbaccount, empcategory,positiondate,positiontime,nativeregion);
    }

    /**
     *
     * @param epfno             Employee Provident Fund Number
     * @param request
     * @param response
     * @return         It Returns map.Map Contains Employee Name
     */
    public Map loadEmployeeDetails(String epfno, HttpServletRequest request, HttpServletResponse response) {
        return EmployeeMasterServiceObj.loadEmployeeDetails(null, request, response, null, null, epfno);
    }


      public Map saveChangeEpfno(String empcategory,String epfno, String newepfno, HttpServletRequest request, HttpServletResponse response) {
        return EmployeeMasterServiceObj.saveChangeEpfno(null, request, response, null, null,empcategory, epfno, newepfno);
    }
}
