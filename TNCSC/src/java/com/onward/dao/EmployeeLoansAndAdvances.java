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
public interface EmployeeLoansAndAdvances {
public Map getEmployeeDetailsNLoanDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno);
public Map getEmployeeDetailsNRecoveryDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno);
public Map loadLoanNames(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String paycodetype);
public Map saveEmployeeLoan(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String regionCode,String loanid, String epfno, String loanname, String loandate, String loanamount, String noofinstallment, String firstinstallmentamt, String successiveinstallmentamt, String fileno,String completedins, String loanbalance);
public Map getLoanDetailsForModification(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String loanId,String epfno); 
public Map saveEmployeeRecovery(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String regionCode,String epfno, String recoveryname, String recoverydate, String recoveryamount, String noofinstallment, String firstinstallmentamt, String successiveinstallmentamt, String fileno);

}
