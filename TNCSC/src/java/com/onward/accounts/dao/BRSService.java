/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

/**
 *
 * @author Karthikeyan S
 */
public interface BRSService {

    public Map getAccountBook(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map uploadReconciliationFile(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod, String serverPath);

    public Map getRegionWiseBrsDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod, String region);

    public Map saveBRSApprovalList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod, String region, String approvalarray);

    public Map getRegionWiseBrsReportDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod, String region, String status);

    public Map getBrsReportDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod);

    public Map BRSRegionWiseReport(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod, String region, String status, String filePathwithName);
}
