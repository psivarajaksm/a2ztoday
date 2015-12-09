/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.budget.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.upload.FormFile;
import org.hibernate.Session;

/**
 *
 * @author user
 */
public interface BudgetService {

    public Map getBudgetForRevision(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String startyear, String endyear);
    
    public Map getBudgetForRevisionHO(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String startyear, String endyear);

    public void createBudgetDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String startyear, String endyear);

    public Map saveBudgetDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String budgetdetailsid, String ledgerid, String ledgername, String fistactual, String secondactual, String thirdactual, String average, String currentbudgetestimate, String actualfirsthalf, String probablesecondhalf, String currentrevisedestimate, String nextyearbudgetestimate, String currentrevisedestimateho, String nextyearbudgetestimateho);
    
    public Map saveBudgetDetailsHO(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String budgetdetailsid, String ledgerid, String ledgername, String fistactual, String secondactual, String thirdactual, String average, String currentbudgetestimate, String actualfirsthalf, String probablesecondhalf, String currentrevisedestimate, String nextyearbudgetestimate, String currentrevisedestimateho, String nextyearbudgetestimateho);

    public Map upLoadTxtFile(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, FormFile filename);

    public Map displayTextFileDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map saveTextFiletoDB(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map BudgetReportPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String region, String startyear, String endyear);
    
    public Map BudgetReportHOPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String region, String startyear, String endyear);

    public Map BudgetScheduleReportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String ledgerid, String startyear, String endyear);

    public Map BudgetReportRegionPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String startyear, String endyear);

    public String getMyRegionCode(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map checkuploadexists(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String accyear);

    public Map getBudgetUpdationDetailsDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String endyear);

    public String getBudgetLedgerList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public String getRegionList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getMonthExpenditure(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String budgetid, String region);

    public Map loadBudgetYearDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map ControlofExpenditureReportPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePathwithName, String region, String smonth, String syear, String period);

    public Map saveBudgetExpenditureDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String expendituredetailsid, String ledgerid, String monthexpenditure, String fmaamount, String region);

    public Map getBudgetForFMAProbable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String startyear, String endyear);
    
    public Map saveProbableFMADetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String budgetdetailsid, String ledgerid, String ledgername, String fmaamt, String hofmaamt);

    public Map getHOBudgetForFMARevision(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String startyear, String endyear);
    
    public Map saveHORevisionFMADetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String budgetdetailsid, String ledgerid, String ledgername, String fmaamt, String hofmaamt);
    
    public Map ControlofExpenditureAccountReportPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePathwithName, String ledgerid, String smonth, String syear, String period);
    
    public Map BudgetFMAReportPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String region, String startyear, String endyear);
    
    public Map controlOfEpenditure(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountingperiod, String accountingperiodfrom, String accountingperiodto, String filePathwithName);
    
    public Map getPayDetailsForBudgerExcellWrite(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String filePath);
}
