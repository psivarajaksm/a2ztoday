/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.epf.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

/**
 *
 * @author Karthikeyan.S
 */
public interface EPFTransactionService {

    public Map getEpfTransaction(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);

    public Map getDetailsForModification(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfId);

    public Map getEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfId);

    public Map saveEmployeeEPF(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfid, String epfnumber, String salary, String epfwhole, String fbr, String rl, String vpf, String epf, String fbf, String nrc, String smonth, String syear, String empcategory, String payrollcategory);

    public Map getEpfLoanApplReceipt(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);

    public Map getEpfLoanApplications(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);

    public Map getEpfLoanApplicationsForWorkingSheet(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);

    public Map getEpfLoanTransaction(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);

    public Map getEpfLoanForApproval(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year);

    public Map loadLoanTypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map saveLoanApplicationReceiptEntry(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String epfloanappreceiptid, String epfnumber, String applicationdate, String loantype, String loanamount, String tapalno, String mmonth, String myear,String isfinalsettlement,String loaninstallment);
    
    public Map getEPFLoanApplicationReceiptDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfloanid);

    public Map getWorkingSheet(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String loanids);
}
