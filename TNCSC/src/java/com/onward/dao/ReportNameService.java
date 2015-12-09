/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

/**
 *
 * @author Prince vijayakumar M
 */
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

public interface ReportNameService {

    public String getFileName(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String reportname, String month, String year);
    public String getFileNameByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String reportname, String month, String year,String selectedRegion);
    public String getFileName(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String reportname);
    public String getFileNameByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String reportname,String selectedRegion);    
    
}
