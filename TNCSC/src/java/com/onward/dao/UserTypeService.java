/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import java.util.LinkedList;

/**
 *
 * @author root
 */
public interface UserTypeService {

    public Map getUsertypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map saveUserType(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String funtype, String usertypeid, String usertype);

    public Map saveMenuAssign(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String usertypes, String menucodes);

    public Map getAssignedMenus(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String usertypes);

    public Map getUserTypeSize(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public LinkedList duplicatePaySlipPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year,String billtype);
    
    public Map LPCPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year,String billtype);
}
