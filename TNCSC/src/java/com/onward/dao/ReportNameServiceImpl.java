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

public class ReportNameServiceImpl implements ReportNameService {

    private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    @GlobalDBOpenCloseAndUserPrivilages
    public String getFileName(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String reportname, String month, String year) {
        String Month = months[Integer.valueOf(month) - 1];
        String Year = year.substring(2, 4);
        String Region = LoggedInRegion;
        String fileName = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append(Region);
        buffer.append(reportname);
        buffer.append(Month);
        buffer.append(Year);
        buffer.append(".txt");
        return buffer.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getFileNameByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String reportname, String month, String year, String selectedRegion) {
        LoggedInRegion = selectedRegion;
        String Month = months[Integer.valueOf(month) - 1];
        String Year = year.substring(2, 4);
        String Region = LoggedInRegion;
        String fileName = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append(Region);
        buffer.append(reportname);
        buffer.append(Month);
        buffer.append(Year);
        buffer.append(".txt");
        return buffer.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getFileName(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String reportname) {
        String Region = LoggedInRegion;
        String fileName = null;
        StringBuffer buffer = new StringBuffer();
//        buffer.append(Region);
//        buffer.append("-");
        buffer.append(reportname);
        buffer.append(".txt");
        return buffer.toString();
    }

    public String getFileNameByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String reportname, String selectedRegion) {
        LoggedInRegion = selectedRegion;
        String Region = LoggedInRegion;
        String fileName = null;
        StringBuffer buffer = new StringBuffer();
//        buffer.append(Region);
//        buffer.append("-");
        buffer.append(reportname);
        buffer.append(".txt");
        return buffer.toString();
    }
}
