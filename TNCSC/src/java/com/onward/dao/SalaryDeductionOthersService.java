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
 * @author sk
 */
public interface SalaryDeductionOthersService {
    public Map getLoadDeductionTypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map checkDeductionProcess(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductionmode);
    public Map salayDeductionOthersProcess( Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String deductiontypes, String deductionmode,String month,String year,String deductionamt,String isnew);
    public Map saveDeductionOthers(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String hdnemployees,String installmentmode,String delimiter,String isNew);
    public Map loadFinanicalYears(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map proffesionalTaxDeductionProcess(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String finanicalyear,String deductionmode,String installmentmode,String isnew);
    
}
