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
 * @author Prince vijayakumar M
 */
public interface IncomeTaxService {

    public Map IncomeTaxTentativeParticularsPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String endyear, String pfno, String reporttype, String filePathwithName, String sectionid);
    public Map IncomeTaxParticularsPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String epfno, String reporttype, String filePathwithName, String sectionid);
    public Map loadSectionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
}
