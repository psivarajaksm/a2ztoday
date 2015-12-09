/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.onward.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

/**
 *
 * @author root
 */
public interface MasterService {
    public Map getPayCodeSerial(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String paycodetype,String orderbyString );
    public Map savePayCodeMaster(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String paycodetype,String paycode,String paycodename,String paypercentage );
//    public Map getRegionCodeSerial(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String regiontype );
    public Map saveRegionMaster(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String regioncode,String regionname);
    public Map saveSectionMaster(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String region,String parentcode,String sectioncode, String sectionname);
//    public Map getSectionCodeSerial(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String sectiontype );
//    public Map saveSectionMaster(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String sectioncode,String sectionname);
    public Map getDesignationCodeSerial(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String cadretype,String wingtype);
    public Map saveDesignationMaster(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String designationcode, String designationname,String payscalecode,String orderno);
    public Map getDesignationDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
//    public Map getRegionAndSectionDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map getRegionDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map getSectionDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
//    public Map getPayCodeDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map getLoadEarningsDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map getLoadAssignedPayCodes(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String paytype);
    public Map saveCCAHRAMaster(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String paytype,String paycodeIds);
    public Map loadEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno);    
    public Map saveStopPayment(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String reasontype,String reasondate,String remarks);
    public Map loadEmployeeAccountDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno,String deductioncode);
    public Map loadDeductionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map saveEmployeeAccountCode(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductioncode, String epfno, String empaccountcode,String serialno);
    public Map MasterDataUpdata(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String filePathwithName);
    public Map getLoadEarningsTypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map createRowinHTML(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String noofrows);
    public Map getDatasForModify(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String earningcode);
    public Map saveEarningsSlapDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String earningstypes,String effectdate,String[] amountrangefrom,String[] amountrangeto,String[] slapamount,String[] slappercentage,String orderno,String totalrows,String[] hiddeniarray,String funtype);
//    public Map getLoadSections(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map getLoadRegionSections(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String regionid,String sectionid);
}
