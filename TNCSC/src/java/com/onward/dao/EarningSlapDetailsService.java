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
public interface EarningSlapDetailsService {
    public Map getLoadEarningsTypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map getLoadDedutionTypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map createRowinHTML(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String noofrows);
    public Map getDatasForModify(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String earningcode);
    public Map getPTDatasForModify(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String deductioncode);
    public Map saveEarningsSlapDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String earningstypes,String effectdate,String[] amountrangefrom,String[] amountrangeto,String[] slapamount,String[] slappercentage,String orderno,String totalrows,String[] hiddeniarray,String funtype);
    public Map savePTSlapDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String earningstypes,String effectdate,String[] amountrangefrom,String[] amountrangeto,String[] slapamount,String[] slappercentage,String orderno,String totalrows,String[] hiddeniarray,String funtype);
    public Map saveStopProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String processtype,String processmonth,String processyear);
}
