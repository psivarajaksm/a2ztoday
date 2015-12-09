/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

/**
 *
 * @author Prince vijayakumar M
 */
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

public interface RegionwiseReportService {

    public String getGroupType(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getScheduleGrid(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String syear, String smonth, String eyear, String emonth, String epfno, String grouptype, String region, String reporttype, String  filePathwithName);

    public String getRegionList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public String getEarningList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getEarningDetailsGrid(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String syear, String smonth, String eyear, String emonth, String epfno, String earningid, String region);

    public Map getServiceRegisterDetailsGrid(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String syear, String smonth, String eyear, String emonth, String epfno, String region);

    public String getRegionList1(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    
    public Map getServiceRegisterDetailsReport(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String syear, String smonth, String eyear, String emonth, String epfno, String region, String filePathwithName);
}
