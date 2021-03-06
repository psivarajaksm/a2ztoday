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
public interface EmployeePayBillProcess {
   public Map payRollProcess(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dateOfMonth, String serialno,String epfno);
   public Map preparePayRoll(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dateOfMonth, String serialno,String epfno);
   public Map getEmployeeList(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
   public Map getPayrollProcessDate(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
   public Map startPayRollProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
}
