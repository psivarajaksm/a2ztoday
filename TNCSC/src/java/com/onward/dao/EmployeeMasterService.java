/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.onward.dao;

import com.onward.valueobjects.UserViewModel;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

/**
 *
 * @author root
 */
public interface  EmployeeMasterService {
    public Map loadRegionDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map loadRegionDetailsforDeputation(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map getEmployeeEPFNo(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String epfno);
    public Map getEmployeeDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String epfno);
    public Map getEmployeeDetailsFromEPFMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno);
    public Map saveEmployeeMaster(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String epfno,String fpfno,String employeename,String fathername,String gender,String dateofbirth,String region,
                                String section,String dateofappoinment,String dateofprobation,String dateofconfirmation,String designation,
                                String community,String pancardno,String paymentmode,String bankcode,String banksbaccount, String empcategory, String positiondate, String positiontime, String nativeregion);
    public Map getMenuDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,UserViewModel userObj);
    public Map getEmployeeGPFNo(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String epfno);
    public Map getGovernmentEmployeeDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String epfno);
    public Map loadEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno);
    public Map saveChangeEpfno(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String empcategory, String epfno, String newepfno);
    
     
}
