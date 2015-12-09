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
public interface DAIncrementArrearService {

    public Map getEmployeeList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map saveDAIncrementProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String asondate, String dapercentage, String serialno, String epfno, String batchno);

    public Map getEmployeeListHTML(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String sectionid);

    public Map loadDADetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map loadbatchDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String daarrearid);

    public Map getBatchEmployeeListHTML(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String batchid);

    public Map saveDAIncrementReProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String asondate, String dapercentage, String serialno, String epfno, String batchno);
    
    public Map saveDAArrearBatchCreation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fileno, String batchperiod);

    public Map loadSectionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map removeFromDa(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String batchid, String epfno);

    public Map modifyDADetailsinHTML(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String batchid, String epfno);

    public Map getDAIncrementEarningsUpdation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String batchid);

    public Map getDADetailsCompManual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String subpaybillid, String processingdetailsid, String month, String year, String epfno, String dabatchno);

    public Map saveDaManual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String subpaybillid, String dmonthid, String dyearid, String basicamount, String perpayamt, String gradepayamt, String dueamt, String drawnamt, String arrearamt, String epfamt, String billtype);

    public Map getBatchEmployeeList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String batchid);

    public Map getDaIncrementArrearManualFormDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String batchid, String daareardiffid);

    public Map DaManualFormUpdation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String subpaybillid, String dmonthid, String dyearid, String basicamount, String perpayamt, String gradepayamt, String dueamt, String drawnamt, String arrearamt, String epfamt, String billtype, String epfno, String batchid,String daareardiffid);
}
