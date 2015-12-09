/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

public interface BonusBillService {

    public Map EmployeeBonusPDPPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startmonth, String startyear, String endmonth, String endyear, String pfno, String category, String section, String filePathwithName);
  
    public Map RetiredInTransferEmployeeBonusPDPPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startmonth, String startyear, String endmonth, String endyear, String epfno1, String category, String section, String filePathwithName);
}
