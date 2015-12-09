/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.edli.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

/**
 *
 * @author user
 */
public interface EDLIService {
    public Map getEDLIDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String month,String year);
    public Map getDesignationWiseEmployeesDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year,String  empcatagory);
    public Map getDesignationEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year,String designationcode,String  empcatagory);
    public Map getAppointmentDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String fromyear,String toyear);
    public Map getRegionMonthwiseAppointmentEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String month,String regionid,String year);
    public Map getALLRegionMonthwiseAppointmentEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);
    public Map getRegionAllMonthsAppointmentEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String regionid, String endyear);
    public Map getAllRegionAppointmentEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String endyear);
    public Map getRetirementDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String fromyear,String toyear);
    public Map getRegionMonthwiseRetirementEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String month,String regionid,String year);
    public Map getALLRegionMonthwiseRetirementEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);
    public Map getRegionAllMonthsRetirementEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String regionid, String endyear);
    public Map getAllRegionRetirementEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String endyear);
    public Map loadRegionDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map getRegionwiseEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String selecteddate);
    public Map edliEmployeeReportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String category,String filePathwithName);
    public Map edliEmployeeEarningsDeductionPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String syear, String smonth, String ayear, String amonth, String region, String category, String reporttype, String filePathwithName);
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

