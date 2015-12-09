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
public interface EmployeeFundSubService {

    public Map getEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String employeetype);

    public Map getRecordedDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeetype, String month, String year);

    public Map getDetailsForModification(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfId);

    public Map modifyEmployeeEPF(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfid, String epfnumber, String salary, String epfwhole, String fbr, String rl, String vpf, String epf, String fbf, String nrc, String smonth, String syear);

    public Map saveEmployeeEPF(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String employeetype, String epfno, String employeename, String salaryamt, String smonth, String syear);

    public Map EmployeeFundsUpEPFformDBFPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String empcategory);

    public Map EmployeeFundsUpEPFformDownloadPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeFundsUpEPFformPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String empcategory, String filePath);
}
