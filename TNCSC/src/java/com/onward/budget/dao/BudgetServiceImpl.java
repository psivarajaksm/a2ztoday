/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.budget.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.common.HibernateUtil;
import com.onward.dao.EmployeePayBillProcessImpl;
import com.onward.dao.GlobalDBOpenCloseAndUserPrivilages;
import com.onward.persistence.payroll.Accountingyear;
import com.onward.persistence.payroll.Budget;
import com.onward.persistence.payroll.Budgetdetails;
import com.onward.persistence.payroll.Ccahra;
import com.onward.persistence.payroll.Earningslapdetails;
import com.onward.persistence.payroll.Employeeearningsdetails;
import com.onward.persistence.payroll.Employeeearningsdetailsactual;
import com.onward.persistence.payroll.Ledgermaster;
import com.onward.persistence.payroll.Monthlyexpendituredetails;
import com.onward.persistence.payroll.Paycodemaster;
import com.onward.persistence.payroll.Regionmaster;
import com.onward.valueobjects.ReportModel;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.struts.upload.FormFile;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author user
 */
public class BudgetServiceImpl extends OnwardAction implements BudgetService {

    public static final String DEFAULT_ENCODING = "UTF-8";
//    static BASE64Encoder enc = new BASE64Encoder();
//    static BASE64Decoder dec = new BASE64Decoder();
    private static final String NEW_LINE_SEPARATOR = "\r\n";

    @GlobalDBOpenCloseAndUserPrivilages
    public void createBudgetDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String startyear, String endyear) {
        String firstBudgetId = "";
        String secondBudgetId = "";
        String thirdBudgetId = "";
        String nextBudgetId = "";
        String currentBudgetId = "";


        int currentstartyear = Integer.parseInt(startyear);
        int currentendyear = Integer.parseInt(endyear);
        Criteria currentbudgetCrit = session.createCriteria(Budget.class);
        currentbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        currentbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + currentstartyear));
        currentbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        currentbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + currentendyear));
        List<Budget> currentbudgetList = currentbudgetCrit.list();
        Budget currentbudgetObj = null;
        if (currentbudgetList.size() > 0) {
            currentbudgetObj = (Budget) currentbudgetList.get(0);
            currentBudgetId = currentbudgetObj.getId();
        }

        int firststartyear = Integer.parseInt(startyear) - 3;
        int firstendyear = Integer.parseInt(endyear) - 3;
        Criteria firstbudgetCrit = session.createCriteria(Budget.class);
        firstbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        firstbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + firststartyear));
        firstbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        firstbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + firstendyear));
        List<Budget> firstbudgetList = firstbudgetCrit.list();
        Budget firstbudgetObj = null;
        if (firstbudgetList.size() > 0) {
            firstbudgetObj = (Budget) firstbudgetList.get(0);
            firstBudgetId = firstbudgetObj.getId();
        }

        int secondstartyear = Integer.parseInt(startyear) - 2;
        int secondendyear = Integer.parseInt(endyear) - 2;
        Criteria secondbudgetCrit = session.createCriteria(Budget.class);
        secondbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        secondbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + secondstartyear));
        secondbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        secondbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + secondendyear));
        List<Budget> secondbudgetList = secondbudgetCrit.list();
        Budget secondbudgetObj = null;
        if (secondbudgetList.size() > 0) {
            secondbudgetObj = (Budget) secondbudgetList.get(0);
            secondBudgetId = secondbudgetObj.getId();
        }

        int thirdstartyear = Integer.parseInt(startyear) - 1;
        int thirdendyear = Integer.parseInt(endyear) - 1;
        Criteria thirdbudgetCrit = session.createCriteria(Budget.class);
        thirdbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + thirdstartyear));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + thirdendyear));
        List<Budget> thirdbudgetList = thirdbudgetCrit.list();
        Budget thirdbudgetObj = null;
        if (thirdbudgetList.size() > 0) {
            thirdbudgetObj = (Budget) thirdbudgetList.get(0);
            thirdBudgetId = thirdbudgetObj.getId();
        }

        int nextstartyear = Integer.parseInt(startyear) + 1;
        int nextendyear = Integer.parseInt(endyear) + 1;
        Criteria nextbudgetCrit = session.createCriteria(Budget.class);
        nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
        List<Budget> nextbudgetList = nextbudgetCrit.list();
        Budget nextbudgetObj = null;
        if (nextbudgetList.size() > 0) {
            nextbudgetObj = (Budget) nextbudgetList.get(0);
            nextBudgetId = nextbudgetObj.getId();
        }


        try {
            PrintWriter pw = null;
            File file = new File(filePath + "/" + LoggedInRegion + "buddn" + ".txt");
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            String result = getBudgetDetails(session, firstBudgetId, secondBudgetId, thirdBudgetId, currentBudgetId, nextBudgetId, startyear, endyear, LoggedInRegion);
            pw.printf(result);
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveBudgetDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String budgetdetailsid, String ledgerid, String ledgername, String fistactual, String secondactual, String thirdactual, String average, String currentbudgetestimate, String actualfirsthalf, String probablesecondhalf, String currentrevisedestimate, String nextyearbudgetestimate, String currentrevisedestimateho, String nextyearbudgetestimateho) {
        Map resultMap = new HashMap();
        try {
            int startyear = 0;
            int endyear = 0;
            String budgetid = "";
            String firstBudgetId = "";
            String secondBudgetId = "";
            String thirdBudgetId = "";

            Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
            budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
            budgetDetailsCrit.add(Restrictions.sqlRestriction("id='" + budgetdetailsid + "'"));
            budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgerid + "'"));
            ////System.out.println(LoggedInRegion + budgetdetailsid + "led" + ledgerid);
            List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
            Budgetdetails budgetdetailsObj;
            if (budgetdetailsList.size() > 0) {
                budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);
                budgetid = budgetdetailsObj.getBudget().getId();
            }
            //System.out.println("Budget" + budgetid);

            Criteria budgetCrit = session.createCriteria(Budget.class);
            budgetCrit.add(Restrictions.sqlRestriction("id='" + budgetid + "'"));
            List<Budget> budgetList = budgetCrit.list();
            Budget budgetObj = null;
            if (budgetList.size() > 0) {
                budgetObj = (Budget) budgetList.get(0);
                startyear = budgetObj.getStartyear();
                endyear = budgetObj.getEndyear();
            }



            int firststartyear = startyear - 3;
            int firstendyear = endyear - 3;
            Criteria firstbudgetCrit = session.createCriteria(Budget.class);
            firstbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            firstbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + firststartyear));
            firstbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            firstbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + firstendyear));
            List<Budget> firstbudgetList = firstbudgetCrit.list();
            //System.out.println("frst start year" + firststartyear + "endyear" + firstendyear);
            Budget firstbudgetObj = null;
            if (firstbudgetList.size() > 0) {
                firstbudgetObj = (Budget) firstbudgetList.get(0);
                firstBudgetId = firstbudgetObj.getId();
            }

            int secondstartyear = startyear - 2;
            int secondendyear = endyear - 2;
            Criteria secondbudgetCrit = session.createCriteria(Budget.class);
            secondbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            secondbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + secondstartyear));
            secondbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            secondbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + secondendyear));
            List<Budget> secondbudgetList = secondbudgetCrit.list();
            Budget secondbudgetObj = null;
            if (secondbudgetList.size() > 0) {
                secondbudgetObj = (Budget) secondbudgetList.get(0);
                secondBudgetId = secondbudgetObj.getId();
            }

            int thirdstartyear = startyear - 1;
            int thirdendyear = endyear - 1;
            Criteria thirdbudgetCrit = session.createCriteria(Budget.class);
            thirdbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + thirdstartyear));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + thirdendyear));
            List<Budget> thirdbudgetList = thirdbudgetCrit.list();
            Budget thirdbudgetObj = null;
            if (thirdbudgetList.size() > 0) {
                thirdbudgetObj = (Budget) thirdbudgetList.get(0);
                thirdBudgetId = thirdbudgetObj.getId();
            }

            int nextstartyear = startyear + 1;
            int nextendyear = endyear + 1;
            Criteria nextbudgetCrit = session.createCriteria(Budget.class);
            nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
            List<Budget> nextbudgetList = nextbudgetCrit.list();
            Budget nextbudgetObj = null;
            if (nextbudgetList.size() > 0) {
                nextbudgetObj = (Budget) nextbudgetList.get(0);

            }

            Ledgermaster ledgermasterObj = getLedgetMaster(session, ledgerid);
            saveBudgetDetails(session, firstbudgetObj, ledgermasterObj, LoggedInRegion, fistactual);
            saveBudgetDetails(session, secondbudgetObj, ledgermasterObj, LoggedInRegion, secondactual);
            saveBudgetDetails(session, thirdbudgetObj, ledgermasterObj, LoggedInRegion, thirdactual);

            Criteria budgetDetails1Crit = session.createCriteria(Budgetdetails.class);
            budgetDetails1Crit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
            budgetDetails1Crit.add(Restrictions.sqlRestriction("id='" + budgetdetailsid + "'"));
            budgetDetails1Crit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
            List<Budgetdetails> budgetdetails1List = budgetDetails1Crit.list();
            Budgetdetails budgetdetails1Obj;
            if (budgetdetails1List.size() > 0) {
                budgetdetails1Obj = (Budgetdetails) budgetdetails1List.get(0);

                budgetdetails1Obj.setActualoffirsthalfyesr(new BigDecimal(actualfirsthalf));
                budgetdetails1Obj.setProbableforsecondhalfyear(new BigDecimal(probablesecondhalf));
                budgetdetails1Obj.setBudgetestimate(new BigDecimal(currentbudgetestimate));
                budgetdetails1Obj.setRevisedbudgetestimate(new BigDecimal(currentrevisedestimate));


                Transaction transaction;
                transaction = session.beginTransaction();
                session.update(budgetdetails1Obj);
                transaction.commit();

            } else {
                budgetdetails1Obj = new Budgetdetails();
                String id = getMaxBudgetdetailsid(session, LoggedInRegion);
                budgetdetails1Obj.setId(id);
                budgetdetails1Obj.setBudget(budgetObj);
                budgetdetails1Obj.setRegionmaster(getRegion(session, LoggedInRegion));
                budgetdetails1Obj.setLedgermaster(ledgermasterObj);

                budgetdetails1Obj.setActualoffirsthalfyesr(new BigDecimal(actualfirsthalf));
                budgetdetails1Obj.setProbableforsecondhalfyear(new BigDecimal(probablesecondhalf));
                budgetdetails1Obj.setBudgetestimate(new BigDecimal(currentbudgetestimate));
                budgetdetails1Obj.setRevisedbudgetestimate(new BigDecimal(currentrevisedestimate));



                Transaction transaction;
                transaction = session.beginTransaction();
                session.save(budgetdetails1Obj);
                transaction.commit();
            }


            saveNextEstimate(session, nextbudgetObj, ledgermasterObj, LoggedInRegion, nextyearbudgetestimate);

            resultMap.put("status", "Successfully Saved");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveBudgetDetailsHO(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String budgetdetailsid, String ledgerid, String ledgername, String fistactual, String secondactual, String thirdactual, String average, String currentbudgetestimate, String actualfirsthalf, String probablesecondhalf, String currentrevisedestimate, String nextyearbudgetestimate, String currentrevisedestimateho, String nextyearbudgetestimateho) {

        Map resultMap = new HashMap();
        int startyear = 0;
        int endyear = 0;
        String budgetid = "";
        String firstBudgetId = "";
        String secondBudgetId = "";
        String thirdBudgetId = "";
        String regionid = "";

        Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
        budgetDetailsCrit.add(Restrictions.sqlRestriction("id='" + budgetdetailsid + "'"));
        List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
        Budgetdetails budgetdetailsObj;
        if (budgetdetailsList.size() > 0) {
            budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);
            budgetdetailsObj.setHobudget(new BigDecimal(currentrevisedestimateho));
            budgetid = budgetdetailsObj.getBudget().getId();
            regionid = budgetdetailsObj.getRegionmaster().getId();

            Transaction transaction;
            transaction = session.beginTransaction();
            session.update(budgetdetailsObj);
            transaction.commit();
        }



        //System.out.println("Budget" + budgetid);

        Criteria budgetCrit = session.createCriteria(Budget.class);
        budgetCrit.add(Restrictions.sqlRestriction("id='" + budgetid + "'"));
        List<Budget> budgetList = budgetCrit.list();
        Budget budgetObj = null;
        if (budgetList.size() > 0) {
            budgetObj = (Budget) budgetList.get(0);
            startyear = budgetObj.getStartyear();
            endyear = budgetObj.getEndyear();
        }



        int firststartyear = startyear - 3;
        int firstendyear = endyear - 3;
        Criteria firstbudgetCrit = session.createCriteria(Budget.class);
        firstbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        firstbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + firststartyear));
        firstbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        firstbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + firstendyear));
        List<Budget> firstbudgetList = firstbudgetCrit.list();
        //System.out.println("frst start year" + firststartyear + "endyear" + firstendyear);
        Budget firstbudgetObj = null;
        if (firstbudgetList.size() > 0) {
            firstbudgetObj = (Budget) firstbudgetList.get(0);
            firstBudgetId = firstbudgetObj.getId();
        }

        int secondstartyear = startyear - 2;
        int secondendyear = endyear - 2;
        Criteria secondbudgetCrit = session.createCriteria(Budget.class);
        secondbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        secondbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + secondstartyear));
        secondbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        secondbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + secondendyear));
        List<Budget> secondbudgetList = secondbudgetCrit.list();
        Budget secondbudgetObj = null;
        if (secondbudgetList.size() > 0) {
            secondbudgetObj = (Budget) secondbudgetList.get(0);
            secondBudgetId = secondbudgetObj.getId();
        }

        int thirdstartyear = startyear - 1;
        int thirdendyear = endyear - 1;
        Criteria thirdbudgetCrit = session.createCriteria(Budget.class);
        thirdbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + thirdstartyear));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + thirdendyear));
        List<Budget> thirdbudgetList = thirdbudgetCrit.list();
        Budget thirdbudgetObj = null;
        if (thirdbudgetList.size() > 0) {
            thirdbudgetObj = (Budget) thirdbudgetList.get(0);
            thirdBudgetId = thirdbudgetObj.getId();
        }

        int nextstartyear = startyear + 1;
        int nextendyear = endyear + 1;
        Criteria nextbudgetCrit = session.createCriteria(Budget.class);
        nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
        List<Budget> nextbudgetList = nextbudgetCrit.list();
        Budget nextbudgetObj = null;
        if (nextbudgetList.size() > 0) {
            nextbudgetObj = (Budget) nextbudgetList.get(0);

        }

        Ledgermaster ledgermasterObj = getLedgetMaster(session, ledgerid);
        saveBudgetDetails(session, firstbudgetObj, ledgermasterObj, regionid, fistactual);
        saveBudgetDetails(session, secondbudgetObj, ledgermasterObj, regionid, secondactual);
        saveBudgetDetails(session, thirdbudgetObj, ledgermasterObj, regionid, thirdactual);

        Criteria budgetDetails1Crit = session.createCriteria(Budgetdetails.class);
        budgetDetails1Crit.add(Restrictions.sqlRestriction("regioncode='" + regionid + "'"));
        budgetDetails1Crit.add(Restrictions.sqlRestriction("id='" + budgetdetailsid + "'"));
        budgetDetails1Crit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
        List<Budgetdetails> budgetdetails1List = budgetDetails1Crit.list();
        Budgetdetails budgetdetails1Obj;
        if (budgetdetails1List.size() > 0) {
            budgetdetails1Obj = (Budgetdetails) budgetdetails1List.get(0);

            budgetdetails1Obj.setActualoffirsthalfyesr(new BigDecimal(actualfirsthalf));
            budgetdetails1Obj.setProbableforsecondhalfyear(new BigDecimal(probablesecondhalf));
            budgetdetails1Obj.setBudgetestimate(new BigDecimal(currentbudgetestimate));
            budgetdetails1Obj.setRevisedbudgetestimate(new BigDecimal(currentrevisedestimate));
            budgetdetails1Obj.setHobudget(new BigDecimal(currentrevisedestimateho));

//            
//            budgetdetails1Obj.setActualoffirsthalfyesr(new BigDecimal(actualfirsthalf));
//            budgetdetails1Obj.setProbableforsecondhalfyear(new BigDecimal(probablesecondhalf));
//            budgetdetails1Obj.setBudgetestimate(new BigDecimal(currentbudgetestimate));
//            budgetdetails1Obj.setRevisedbudgetestimate(new BigDecimal(currentrevisedestimate));

            Transaction transaction;
            transaction = session.beginTransaction();
            session.update(budgetdetails1Obj);
            transaction.commit();

        }


        saveNextEstimate(session, nextbudgetObj, ledgermasterObj, regionid, nextyearbudgetestimate);
        saveNextEstimateHO(session, nextbudgetObj, ledgermasterObj, regionid, nextyearbudgetestimateho);

        resultMap.put("status", "Successfully Saved");
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveBudgetDetailsHO1(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String budgetdetailsid, String ledgerid, String ledgername, String fistactual, String secondactual, String thirdactual, String average, String currentbudgetestimate, String actualfirsthalf, String probablesecondhalf, String currentrevisedestimate, String nextyearbudgetestimate, String currentrevisedestimateho, String nextyearbudgetestimateho) {
        Map resultMap = new HashMap();
        int startyear = 0;
        int endyear = 0;
        String budgetid = "";
        String firstBudgetId = "";
        String secondBudgetId = "";
        String thirdBudgetId = "";
        String regionid = "";

        Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
        budgetDetailsCrit.add(Restrictions.sqlRestriction("id='" + budgetdetailsid + "'"));
        List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
        Budgetdetails budgetdetailsObj;
        if (budgetdetailsList.size() > 0) {
            budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);
            budgetdetailsObj.setHobudget(new BigDecimal(currentrevisedestimateho));
            budgetid = budgetdetailsObj.getBudget().getId();
            regionid = budgetdetailsObj.getRegionmaster().getId();

            Transaction transaction;
            transaction = session.beginTransaction();
            session.update(budgetdetailsObj);
            transaction.commit();
        }


        Criteria budgetCrit = session.createCriteria(Budget.class);
        budgetCrit.add(Restrictions.sqlRestriction("id='" + budgetid + "'"));
        List<Budget> budgetList = budgetCrit.list();
        Budget budgetObj = null;
        if (budgetList.size() > 0) {
            budgetObj = (Budget) budgetList.get(0);
            startyear = budgetObj.getStartyear();
            endyear = budgetObj.getEndyear();
        }





        int nextstartyear = startyear + 1;
        int nextendyear = endyear + 1;
        Criteria nextbudgetCrit = session.createCriteria(Budget.class);
        nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
        List<Budget> nextbudgetList = nextbudgetCrit.list();
        Budget nextbudgetObj = null;
        if (nextbudgetList.size() > 0) {
            nextbudgetObj = (Budget) nextbudgetList.get(0);
        }

        Ledgermaster ledgermasterObj = getLedgetMaster(session, ledgerid);

        saveNextEstimateHO(session, nextbudgetObj, ledgermasterObj, regionid, nextyearbudgetestimateho);

        resultMap.put("status", "Successfully Saved");
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getBudgetForRevision(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String startyear, String endyear) {
        System.out.println("*************************** BudgetServiceImpl class getBudgetForRevision method is calling *********************");
        Map resultMap = new HashMap();
        try {
            String budgetId = "";
            String firstBudgetId = "";
            String secondBudgetId = "";
            String thirdBudgetId = "";
            String nextBudgetId = "";
            String classname = "";

            StringBuffer resultHTML = new StringBuffer();
            //System.out.println("start year" + startyear + "end year" + endyear);

            int firststartyear = Integer.parseInt(startyear) - 3;
            int firstendyear = Integer.parseInt(endyear) - 3;
            Criteria firstbudgetCrit = session.createCriteria(Budget.class);
            firstbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            firstbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + firststartyear));
            firstbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            firstbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + firstendyear));
            List<Budget> firstbudgetList = firstbudgetCrit.list();
            Budget firstbudgetObj = null;
            if (firstbudgetList.size() > 0) {
                firstbudgetObj = (Budget) firstbudgetList.get(0);
                firstBudgetId = firstbudgetObj.getId();
            }

            int secondstartyear = Integer.parseInt(startyear) - 2;
            int secondendyear = Integer.parseInt(endyear) - 2;
            Criteria secondbudgetCrit = session.createCriteria(Budget.class);
            secondbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            secondbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + secondstartyear));
            secondbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            secondbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + secondendyear));
            List<Budget> secondbudgetList = secondbudgetCrit.list();
            Budget secondbudgetObj = null;
            if (secondbudgetList.size() > 0) {
                secondbudgetObj = (Budget) secondbudgetList.get(0);
                secondBudgetId = secondbudgetObj.getId();
            }

            int thirdstartyear = Integer.parseInt(startyear) - 1;
            int thirdendyear = Integer.parseInt(endyear) - 1;
            Criteria thirdbudgetCrit = session.createCriteria(Budget.class);
            thirdbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + thirdstartyear));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + thirdendyear));
            List<Budget> thirdbudgetList = thirdbudgetCrit.list();
            Budget thirdbudgetObj = null;
            if (thirdbudgetList.size() > 0) {
                thirdbudgetObj = (Budget) thirdbudgetList.get(0);
                thirdBudgetId = thirdbudgetObj.getId();
            }

            int nextstartyear = Integer.parseInt(startyear) + 1;
            int nextendyear = Integer.parseInt(endyear) + 1;
            Criteria nextbudgetCrit = session.createCriteria(Budget.class);
            nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
            List<Budget> nextbudgetList = nextbudgetCrit.list();
            Budget nextbudgetObj = null;
            if (nextbudgetList.size() > 0) {
                nextbudgetObj = (Budget) nextbudgetList.get(0);
                nextBudgetId = nextbudgetObj.getId();
            }


            Criteria budgetCrit = session.createCriteria(Budget.class);
            budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
            budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
            List<Budget> budgetList = budgetCrit.list();
            Budget budgetObj = null;
            if (budgetList.size() > 0) {
                budgetObj = (Budget) budgetList.get(0);
                budgetId = budgetObj.getId();
            }
//            System.out.println("budgetId ::::::::::::::::::" + budgetId);
            String ledgeryear = "b" + budgetId;
//            System.out.println("ledgeryear ::::::::::::::::" + ledgeryear);

            if (budgetId.trim().length() > 0) {
                Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
                ledgerCrit.add(Restrictions.sqlRestriction(" " + ledgeryear + " is true "));
                List<Ledgermaster> ledgerList = ledgerCrit.list();
                if (ledgerList.size() > 0) {
                    resultHTML.append("<FONT SIZE=2>");
                    resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"budgettable\">");
                    resultHTML.append("<thead>");
                    resultHTML.append("<tr>");
//                resultHTML.append("<table width=\"95%\"  id=\"budgettable\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                resultHTML.append("<tr><td valign=\"top\">");
//                resultHTML.append("<table id=\"budgettable\" width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                resultHTML.append("<tr class=\"gridmenu\">");
                    resultHTML.append("<td>S.No</td>");
                    resultHTML.append("<td>Account</td>");
                    resultHTML.append("<td>" + firststartyear + "-" + firstendyear + "</td>");
                    resultHTML.append("<td>" + secondstartyear + "-" + secondendyear + "</td>");
                    resultHTML.append("<td>" + thirdstartyear + "-" + thirdendyear + "</td>");
                    resultHTML.append("<td>Average</td>");
                    resultHTML.append("<td>Budget Estimate<br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>Actual <br>Upto Sep</td>");
                    resultHTML.append("<td>Probable <br>oct to Mar</td>");
                    resultHTML.append("<td>Rev. Esti<br>" + startyear + "-" + endyear + "</td>");
//                resultHTML.append("<td>Rev. Esti</td>");
                    resultHTML.append("<td>Budget Estimate<br>" + endyear + "-" + (Integer.parseInt(endyear) + 1) + "</td>");
                    resultHTML.append("<td>Rev. Esti <br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>Budget Estimate <br>" + endyear + "-" + (Integer.parseInt(endyear) + 1) + "</td>");
                    resultHTML.append("<td>Modify</td>");
                    resultHTML.append("</tr>");
                    resultHTML.append("</thead>");
                    resultHTML.append("<tbody>");

                    for (int i = 0; i < ledgerList.size(); i++) {
                        Ledgermaster ledgermasterObj = (Ledgermaster) ledgerList.get(i);

//                        System.out.println("LoggedInRegion = " + LoggedInRegion);
//                        System.out.println("budgetId = " + budgetId);
//                        System.out.println("ledgermasterObj.getId()  = " + ledgermasterObj.getId());

                        Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
                        List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
                        Budgetdetails budgetdetailsObj;
                        if (budgetdetailsList.size() > 0) {
                            budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);
                        } else {
                            budgetdetailsObj = new Budgetdetails();
                            String id = getMaxBudgetdetailsid(session, LoggedInRegion);
                            //System.out.println("id ::::::::::::: " + id);
                            budgetdetailsObj.setId(id);
                            budgetdetailsObj.setBudget(budgetObj);
                            budgetdetailsObj.setRegionmaster(getRegion(session, LoggedInRegion));
                            budgetdetailsObj.setLedgermaster(ledgermasterObj);
                            budgetdetailsObj.setActual(BigDecimal.ZERO);
                            budgetdetailsObj.setBudgetestimate(BigDecimal.ZERO);
                            budgetdetailsObj.setRevisedbudgetestimate(BigDecimal.ZERO);
                            budgetdetailsObj.setActualoffirsthalfyesr(BigDecimal.ZERO);
                            budgetdetailsObj.setActualofsecondhalfyesr(BigDecimal.ZERO);
                            budgetdetailsObj.setProbableforsecondhalfyear(BigDecimal.ZERO);
                            budgetdetailsObj.setHobudget(BigDecimal.ZERO);
                            budgetdetailsObj.setHorevisedbudget(BigDecimal.ZERO);


                            Transaction transaction;
                            transaction = session.beginTransaction();
                            session.save(budgetdetailsObj);
                            transaction.commit();
                        }
                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }

//                    System.out.println("firstBudgetId==="+firstBudgetId);
//                    System.out.println("secondBudgetId==="+secondBudgetId);
//                    System.out.println("thirdBudgetId==="+thirdBudgetId);
//                    System.out.println("nextBudgetId==="+nextBudgetId);
                        BigDecimal firstactual = getBudget(session, LoggedInRegion, firstBudgetId, ledgermasterObj.getId());
                        BigDecimal secondactual = getBudget(session, LoggedInRegion, secondBudgetId, ledgermasterObj.getId());
                        BigDecimal thirdactual = getBudgetSan(session, LoggedInRegion, thirdBudgetId, ledgermasterObj.getId());
                        float average = 0;
                        if (firstactual.floatValue() != 0 || secondactual.floatValue() != 0 || thirdactual.floatValue() != 0) {
                            average = (firstactual.floatValue() + secondactual.floatValue() + thirdactual.floatValue()) / 3;
                        }
                        BigDecimal averageActual = StringtobigDecimal(String.valueOf(average));

                        BigDecimal budgetestimate = budgetdetailsObj.getBudgetestimate();
                        BigDecimal actualfirsthalf = budgetdetailsObj.getActualoffirsthalfyesr();
                        BigDecimal probablesecondhalf = budgetdetailsObj.getProbableforsecondhalfyear();
                        BigDecimal revisedestimate = budgetdetailsObj.getRevisedbudgetestimate();
                        BigDecimal budgetestimatenext = getBudget(session, LoggedInRegion, nextBudgetId, ledgermasterObj.getId());

                        BigDecimal budgetho = budgetdetailsObj.getHobudget();
//                    BigDecimal revisedestimateho = budgetdetailsObj.getHorevisedbudget();
                        BigDecimal revisedestimateho = getBudgetNextYearHORevisedEstimate(session, LoggedInRegion, nextBudgetId, ledgermasterObj.getId());



                        resultHTML.append("<tr class=\"" + classname + "\">");
                        resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                        resultHTML.append("<td align=\"left\">" + ledgermasterObj.getLedgername() + "</td>");
                        resultHTML.append("<td align=\"center\">" + firstactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + secondactual + "</td>");
                        resultHTML.append("<td align=\"center\">" + thirdactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + averageActual + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimate + "</td>");
//                    resultHTML.append("<td align=\"right\">" + revisedestimateho + "</td>");
                        resultHTML.append("<td align=\"right\">" + actualfirsthalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + probablesecondhalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimate + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimatenext + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetho + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimateho + "</td>");
                        resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"loancode\" id=\"" + budgetdetailsObj.getId() + "\" onclick=\"setBudgetDetails('" + budgetdetailsObj.getId() + "','" + budgetdetailsObj.getLedgermaster().getId() + "','" + budgetdetailsObj.getLedgermaster().getLedgername() + "','" + firstactual + "','" + secondactual + "','" + thirdactual + "','" + averageActual + "','" + budgetestimate + "','" + actualfirsthalf + "','" + probablesecondhalf + "','" + revisedestimate + "','" + budgetestimatenext + "','" + budgetho + "','" + revisedestimateho + "')\"></td>").append("</tr>");

                    }
//                resultHTML.append("<tr class=\"" + classname + "\">").append("<td colspan=\"12\" align=\"center\">" + "<input type=\"button\" class=\"submitbu\" name=\"show\" id=\"show\" value=\"Download\" onclick=\"downloadbudget('" + startyear + "','" + endyear + "')\">" + "</td>").append("</tr>");
                    resultHTML.append("</tbody>");
                    resultHTML.append("</table>");
                    resultHTML.append("</FONT>");
                }
            }
//            System.out.println(":::::::::::resultHTML.toString() = " + resultHTML.toString());
            resultMap.put("budgetdetails", resultHTML.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getBudgetForRevisionHO(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String startyear, String endyear) {
        String budgetId = "";
        String firstBudgetId = "";
        String secondBudgetId = "";
        String thirdBudgetId = "";
        String nextBudgetId = "";
        String classname = "";
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();
        //System.out.println("start year" + startyear + "end year" + endyear);

        int firststartyear = Integer.parseInt(startyear) - 3;
        int firstendyear = Integer.parseInt(endyear) - 3;
        Criteria firstbudgetCrit = session.createCriteria(Budget.class);
        firstbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        firstbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + firststartyear));
        firstbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        firstbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + firstendyear));
        List<Budget> firstbudgetList = firstbudgetCrit.list();
        Budget firstbudgetObj = null;
        if (firstbudgetList.size() > 0) {
            firstbudgetObj = (Budget) firstbudgetList.get(0);
            firstBudgetId = firstbudgetObj.getId();
        }

        int secondstartyear = Integer.parseInt(startyear) - 2;
        int secondendyear = Integer.parseInt(endyear) - 2;
        Criteria secondbudgetCrit = session.createCriteria(Budget.class);
        secondbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        secondbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + secondstartyear));
        secondbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        secondbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + secondendyear));
        List<Budget> secondbudgetList = secondbudgetCrit.list();
        Budget secondbudgetObj = null;
        if (secondbudgetList.size() > 0) {
            secondbudgetObj = (Budget) secondbudgetList.get(0);
            secondBudgetId = secondbudgetObj.getId();
        }

        int thirdstartyear = Integer.parseInt(startyear) - 1;
        int thirdendyear = Integer.parseInt(endyear) - 1;
        Criteria thirdbudgetCrit = session.createCriteria(Budget.class);
        thirdbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + thirdstartyear));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + thirdendyear));
        List<Budget> thirdbudgetList = thirdbudgetCrit.list();
        Budget thirdbudgetObj = null;
        if (thirdbudgetList.size() > 0) {
            thirdbudgetObj = (Budget) thirdbudgetList.get(0);
            thirdBudgetId = thirdbudgetObj.getId();
        }

        int nextstartyear = Integer.parseInt(startyear) + 1;
        int nextendyear = Integer.parseInt(endyear) + 1;
        Criteria nextbudgetCrit = session.createCriteria(Budget.class);
        nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
        List<Budget> nextbudgetList = nextbudgetCrit.list();
        Budget nextbudgetObj = null;
        if (nextbudgetList.size() > 0) {
            nextbudgetObj = (Budget) nextbudgetList.get(0);
            nextBudgetId = nextbudgetObj.getId();
        }


        Criteria budgetCrit = session.createCriteria(Budget.class);
        budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
        budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
        List<Budget> budgetList = budgetCrit.list();
        Budget budgetObj = null;
        if (budgetList.size() > 0) {
            budgetObj = (Budget) budgetList.get(0);
            budgetId = budgetObj.getId();
        }
        String ledgeryear = "b" + budgetId;
        if (budgetId.trim().length() > 0) {
            Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
            ledgerCrit.add(Restrictions.sqlRestriction(" " + ledgeryear + " is true "));
            List<Ledgermaster> ledgerList = ledgerCrit.list();
            if (ledgerList.size() > 0) {
                resultHTML.append("<FONT SIZE=2>");
                resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"budgetdet\">");
                resultHTML.append("<thead>");
                resultHTML.append("<tr>");
                resultHTML.append("<th>S.No</th>");
                resultHTML.append("<th>Account</th>");
                resultHTML.append("<th>" + firststartyear + "-" + firstendyear + "</th>");
                resultHTML.append("<th>" + secondstartyear + "-" + secondendyear + "</th>");
                resultHTML.append("<th>" + thirdstartyear + "-" + thirdendyear + "</th>");
                resultHTML.append("<th>Average</th>");
                resultHTML.append("<th>Budget Estimate<br>" + startyear + "-" + endyear + "</th>");
                resultHTML.append("<th>Actual <br>Upto Sep</th>");
                resultHTML.append("<th>Probable <br>oct to Mar</th>");
                resultHTML.append("<th>Rev. Esti<br>" + startyear + "-" + endyear + "</th>");
//                resultHTML.append("<td>Rev. Esti</td>");
                resultHTML.append("<th>Budget Estimate<br>" + endyear + "-" + (Integer.parseInt(endyear) + 1) + "</th>");
                resultHTML.append("<th>Rev. Esti <br>" + startyear + "-" + endyear + "</td>");
                resultHTML.append("<th>Budget Estimate <br>" + endyear + "-" + (Integer.parseInt(endyear) + 1) + "</th>");
                resultHTML.append("<th>Modify</th>");
                resultHTML.append("</tr>");
                resultHTML.append("</thead>");
                resultHTML.append("<tbody>");

                for (int i = 0; i < ledgerList.size(); i++) {
                    Ledgermaster ledgermasterObj = (Ledgermaster) ledgerList.get(i);

                    Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
                    budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + region + "'"));
                    budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                    budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
                    List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
                    Budgetdetails budgetdetailsObj = null;
                    if (budgetdetailsList.size() > 0) {
                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }

                        budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);

                        BigDecimal firstactual = getBudget(session, region, firstBudgetId, ledgermasterObj.getId());
                        BigDecimal secondactual = getBudget(session, region, secondBudgetId, ledgermasterObj.getId());
                        BigDecimal thirdactual = getBudget(session, region, thirdBudgetId, ledgermasterObj.getId());

                        float average = 0;
                        if (firstactual.floatValue() != 0 || secondactual.floatValue() != 0 || thirdactual.floatValue() != 0) {
                            average = (firstactual.floatValue() + secondactual.floatValue() + thirdactual.floatValue()) / 3;
                        }

                        BigDecimal averageActual = StringtobigDecimal(String.valueOf(average));

                        BigDecimal budgetestimate = budgetdetailsObj.getBudgetestimate();
                        BigDecimal actualfirsthalf = budgetdetailsObj.getActualoffirsthalfyesr();
                        BigDecimal probablesecondhalf = budgetdetailsObj.getProbableforsecondhalfyear();
                        BigDecimal revisedestimate = budgetdetailsObj.getRevisedbudgetestimate();

                        BigDecimal budgetestimatenext = getBudget(session, region, nextBudgetId, ledgermasterObj.getId());
                        BigDecimal budgetho = budgetdetailsObj.getHobudget();
                        BigDecimal revisedestimateho = getBudgetNextYearHORevisedEstimate(session, region, nextBudgetId, ledgermasterObj.getId());


//                        System.out.println("iddddddd=====" + budgetdetailsObj.getId());

                        resultHTML.append("<tr >");
                        resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                        resultHTML.append("<td align=\"left\">" + ledgermasterObj.getLedgername() + "</td>");
                        resultHTML.append("<td align=\"center\">" + firstactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + secondactual + "</td>");
                        resultHTML.append("<td align=\"center\">" + thirdactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + averageActual + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimate + "</td>");
                        resultHTML.append("<td align=\"right\">" + actualfirsthalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + probablesecondhalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimate + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimatenext + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetho + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimateho + "</td>");
                        resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"loancode\" id=\"" + budgetdetailsObj.getId() + "\" onclick=\"setBudgetDetails('" + budgetdetailsObj.getId() + "','" + budgetdetailsObj.getLedgermaster().getId() + "','" + budgetdetailsObj.getLedgermaster().getLedgername() + "','" + firstactual + "','" + secondactual + "','" + thirdactual + "','" + averageActual + "','" + budgetestimate + "','" + actualfirsthalf + "','" + probablesecondhalf + "','" + revisedestimate + "','" + budgetestimatenext + "','" + budgetho + "','" + revisedestimateho + "')\"></td>");
                        resultHTML.append("</tr>");
                    }
                }
//                resultHTML.append("<tr class=\"" + classname + "\">")
//                        .append("<td colspan=\"12\" align=\"center\">" + "<input type=\"button\" class=\"submitbu\" name=\"show\" id=\"show\" value=\"Download\" onclick=\"downloadbudget('" + startyear + "','" + endyear + "')\">" + "</td>")
//                        .append("</tr>");
                resultHTML.append("</tbody>");
                resultHTML.append("</table>");
                resultHTML.append("</FONT>");
            }
        }
        resultMap.put("budgetdetails", resultHTML.toString());
        return resultMap;
    }

    public synchronized String getMaxBudgetdetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getBudgetdetailsid();
//                System.out.println("maxStr = " + maxNoStr);
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setBudgetdetailsid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
//            System.out.println("BudgetID = " + maxStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return maxStr;
    }

    public Regionmaster getRegion(Session session, String regionCode) {
        Regionmaster regionmasterObj = null;
        Criteria empRegionCrit = session.createCriteria(Regionmaster.class);
        empRegionCrit.add(Restrictions.sqlRestriction("id='" + regionCode + "'"));
        List empRegionList = empRegionCrit.list();
        if (empRegionList.size() > 0) {
            regionmasterObj = (Regionmaster) empRegionList.get(0);
        }
        return regionmasterObj;
    }

    public BigDecimal getBudget(Session session, String regionCode, String budgetId, String ledgermaster) {
        BigDecimal actual = null;
        Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
        budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + regionCode + "'"));
        budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
        budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermaster + "'"));
        List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
        Budgetdetails budgetdetailsObj;
        if (budgetdetailsList.size() > 0) {
            budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);
            //actual = budgetdetailsObj.getActual();
            actual = budgetdetailsObj.getHobudget();
        } else {
            actual = new BigDecimal("0");
        }
        return actual;
    }

    public BigDecimal getBudgetSan(Session session, String regionCode, String budgetId, String ledgermaster) {
        BigDecimal actual = null;
        Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
        budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + regionCode + "'"));
        budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
        budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermaster + "'"));
        List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
        Budgetdetails budgetdetailsObj;
        if (budgetdetailsList.size() > 0) {
            budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);
            actual = budgetdetailsObj.getHobudget();
        } else {
            actual = new BigDecimal("0");
        }
        return actual;
    }

    public BigDecimal getBudgetNextYearHORevisedEstimate(Session session, String regionCode, String budgetId, String ledgermaster) {
        BigDecimal actual = null;
        Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
        budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + regionCode + "'"));
        budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
        budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermaster + "'"));
        List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
        Budgetdetails budgetdetailsObj;
        if (budgetdetailsList.size() > 0) {
            budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);
            actual = budgetdetailsObj.getHorevisedbudget();
        } else {
            actual = new BigDecimal("0");
        }
        return actual;
    }

    public void saveBudgetDetails(Session session, Budget budgetObj, Ledgermaster ledgermasterObj, String LoggedInRegion, String actual) {
        System.out.println("************************** BudgetSericeImpl class saveBudgetDetails method is calling ************************");

        try {
            //BigDecimal actual = null;
            Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
            budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
            budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetObj.getId() + "'"));
            budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
            List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
            Budgetdetails budgetdetailsObj;
            if (budgetdetailsList.size() > 0) {
                budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);

                budgetdetailsObj.setActual(new BigDecimal(actual));
                Transaction transaction;
                transaction = session.beginTransaction();
                session.update(budgetdetailsObj);
                transaction.commit();

            } else {
                budgetdetailsObj = new Budgetdetails();
                String id = getMaxBudgetdetailsid(session, LoggedInRegion);
                budgetdetailsObj.setId(id);
                budgetdetailsObj.setBudget(budgetObj);
                budgetdetailsObj.setRegionmaster(getRegion(session, LoggedInRegion));
                budgetdetailsObj.setLedgermaster(ledgermasterObj);
                budgetdetailsObj.setActual(new BigDecimal(actual));
                budgetdetailsObj.setBudgetestimate(BigDecimal.ZERO);
                budgetdetailsObj.setRevisedbudgetestimate(BigDecimal.ZERO);
                budgetdetailsObj.setActualoffirsthalfyesr(BigDecimal.ZERO);
                budgetdetailsObj.setActualofsecondhalfyesr(BigDecimal.ZERO);
                budgetdetailsObj.setProbableforsecondhalfyear(BigDecimal.ZERO);
                budgetdetailsObj.setHobudget(BigDecimal.ZERO);
                budgetdetailsObj.setHorevisedbudget(BigDecimal.ZERO);


                Transaction transaction;
                transaction = session.beginTransaction();
                session.save(budgetdetailsObj);
                transaction.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void saveNextEstimate(Session session, Budget budgetObj, Ledgermaster ledgermasterObj, String LoggedInRegion, String nextEstimate) {
        System.out.println("****************** BudgetServiceImpl class saveNextEstimate method is calling ***************");
        try {
            //BigDecimal actual = null;
            Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
            budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
            budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetObj.getId() + "'"));
            budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
            List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
            Budgetdetails budgetdetailsObj;
            if (budgetdetailsList.size() > 0) {
                budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);

                budgetdetailsObj.setActual(new BigDecimal(nextEstimate));
                Transaction transaction;
                transaction = session.beginTransaction();
                session.update(budgetdetailsObj);
                transaction.commit();

            } else {
                budgetdetailsObj = new Budgetdetails();
                String id = getMaxBudgetdetailsid(session, LoggedInRegion);
                budgetdetailsObj.setId(id);
                budgetdetailsObj.setBudget(budgetObj);
                budgetdetailsObj.setRegionmaster(getRegion(session, LoggedInRegion));
                budgetdetailsObj.setLedgermaster(ledgermasterObj);
                budgetdetailsObj.setActual(new BigDecimal(nextEstimate));
                budgetdetailsObj.setBudgetestimate(BigDecimal.ZERO);
                budgetdetailsObj.setRevisedbudgetestimate(BigDecimal.ZERO);
                budgetdetailsObj.setActualoffirsthalfyesr(BigDecimal.ZERO);
                budgetdetailsObj.setActualofsecondhalfyesr(BigDecimal.ZERO);
                budgetdetailsObj.setProbableforsecondhalfyear(BigDecimal.ZERO);
                budgetdetailsObj.setHobudget(BigDecimal.ZERO);
                budgetdetailsObj.setHorevisedbudget(BigDecimal.ZERO);


                Transaction transaction;
                transaction = session.beginTransaction();
                session.save(budgetdetailsObj);
                transaction.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void saveNextEstimateHO(Session session, Budget budgetObj, Ledgermaster ledgermasterObj, String LoggedInRegion, String nextEstimate) {
        //BigDecimal actual = null;
        Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
        budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
        budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetObj.getId() + "'"));
        budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
        List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
        Budgetdetails budgetdetailsObj;
        if (budgetdetailsList.size() > 0) {
            budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);

            budgetdetailsObj.setHorevisedbudget(new BigDecimal(nextEstimate));
            Transaction transaction;
            transaction = session.beginTransaction();
            session.update(budgetdetailsObj);
            transaction.commit();

        } else {
            budgetdetailsObj = new Budgetdetails();
            String id = getMaxBudgetdetailsid(session, LoggedInRegion);
            budgetdetailsObj.setId(id);
            budgetdetailsObj.setBudget(budgetObj);
            budgetdetailsObj.setRegionmaster(getRegion(session, LoggedInRegion));
            budgetdetailsObj.setLedgermaster(ledgermasterObj);
            budgetdetailsObj.setActual(BigDecimal.ZERO);
            budgetdetailsObj.setBudgetestimate(BigDecimal.ZERO);
            budgetdetailsObj.setRevisedbudgetestimate(BigDecimal.ZERO);
            budgetdetailsObj.setActualoffirsthalfyesr(BigDecimal.ZERO);
            budgetdetailsObj.setActualofsecondhalfyesr(BigDecimal.ZERO);
            budgetdetailsObj.setProbableforsecondhalfyear(BigDecimal.ZERO);
            budgetdetailsObj.setHobudget(BigDecimal.ZERO);
            budgetdetailsObj.setHorevisedbudget(new BigDecimal(nextEstimate));


            Transaction transaction;
            transaction = session.beginTransaction();
            session.save(budgetdetailsObj);
            transaction.commit();
        }

    }

    public Ledgermaster getLedgetMaster(Session session, String ledgerid) {
        Ledgermaster ledgermasterObj = null;
        Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
        ledgerCrit.add(Restrictions.sqlRestriction("id='" + ledgerid + "'"));
        List<Ledgermaster> ledgerList = ledgerCrit.list();
        if (ledgerList.size() > 0) {
            ledgermasterObj = (Ledgermaster) ledgerList.get(0);
        }
        return ledgermasterObj;
    }

    public Budget getBudget(Session session, String budgetId) {
        Budget budgetObj = null;
        Criteria budgetCrit = session.createCriteria(Budget.class);
        budgetCrit.add(Restrictions.sqlRestriction("id='" + budgetId + "'"));
        List<Budget> budgetList = budgetCrit.list();
        if (budgetList.size() > 0) {
            budgetObj = (Budget) budgetList.get(0);
        }
        return budgetObj;
    }

    public String getBudgetDetails(Session session, String firstBudgetId, String secondBudgetId, String thirdBudgetId, String currentBudgetId, String nextBudgetId, String startyear, String endyear, String LoggedInRegion) {

        String result = "";
        String encoded = "";
        BudgetDetailObject budgetDetailObjectObject = new BudgetDetailObject();
        budgetDetailObjectObject.setStartyear(startyear);
        budgetDetailObjectObject.setEndyear(endyear);
        budgetDetailObjectObject.setRegion(LoggedInRegion);

        List<BudgetDetailsObjectModel> budgetDetailsObjectModelList = new ArrayList<BudgetDetailsObjectModel>();
        for (int j = 0; j < 5; j++) {
            Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
            if (j == 0) {
                budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + firstBudgetId + "'"));
                //System.out.println("firstBudgetId" + firstBudgetId);
            }
            if (j == 1) {
                budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + secondBudgetId + "'"));
                //System.out.println("secondBudgetId" + secondBudgetId);
            }
            if (j == 2) {
                budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + thirdBudgetId + "'"));
                //System.out.println("thirdBudgetId" + thirdBudgetId);
            }
            if (j == 3) {
                budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + currentBudgetId + "'"));
            }
            if (j == 4) {
                budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + nextBudgetId + "'"));
            }

            List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();

            Budgetdetails budgetdetailsObj;
            //System.out.println("Size" + budgetdetailsList.size());
            if (budgetdetailsList.size() > 0) {
                for (int i = 0; i < budgetdetailsList.size(); i++) {
                    budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(i);

                    BudgetDetailsObjectModel budgetDetailsObjectModelObj = new BudgetDetailsObjectModel();
                    budgetDetailsObjectModelObj.setActual(budgetdetailsObj.getActual().toString());
                    budgetDetailsObjectModelObj.setActualoffirsthalfyesr(budgetdetailsObj.getActualoffirsthalfyesr().toString());
                    budgetDetailsObjectModelObj.setActualofsecondhalfyesr(budgetdetailsObj.getActualofsecondhalfyesr().toString());
                    budgetDetailsObjectModelObj.setBudget(budgetdetailsObj.getBudget().getId().toString());
                    budgetDetailsObjectModelObj.setBudgetestimate(budgetdetailsObj.getBudgetestimate().toString());
                    budgetDetailsObjectModelObj.setId(budgetdetailsObj.getId());
                    budgetDetailsObjectModelObj.setLedgermaster(budgetdetailsObj.getLedgermaster().getId());
                    budgetDetailsObjectModelObj.setProbableforsecondhalfyear(budgetdetailsObj.getProbableforsecondhalfyear().toString());
                    budgetDetailsObjectModelObj.setRegionmaster(budgetdetailsObj.getRegionmaster().getId());
                    budgetDetailsObjectModelObj.setRevisedbudgetestimate(budgetdetailsObj.getRevisedbudgetestimate().toString());
                    budgetDetailsObjectModelObj.setHobudget(budgetdetailsObj.getHobudget().toString());
                    budgetDetailsObjectModelObj.setHorevisedbudget(budgetdetailsObj.getHorevisedbudget().toString());

                    budgetDetailsObjectModelList.add(budgetDetailsObjectModelObj);
                    //System.out.println("Budget Id" + budgetdetailsObj.getId());
                }
            }

        }


        budgetDetailObjectObject.setBudgetDetailsObjectModelList(budgetDetailsObjectModelList);

        try {
            JAXBContext context = JAXBContext.newInstance(BudgetDetailObject.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(budgetDetailObjectObject, stringWriter);
            result = stringWriter.toString();


            String txt = result;
            String key = "sairam";
            txt = xorMessage(txt, key);
            //encoded = base64encode(txt);
//            System.out.println(" is encoded to: " + encoded + " and that is decoding to: " + (txt = base64decode(encoded)));
//            System.out.print("XOR-ing back to original: " + xorMessage(txt, key));


//            System.out.println(encoded);
        } catch (Exception e) {
        }
        return encoded;
    }

//    public static String base64encode(String text) {
//        try {
//            String rez = enc.encode(text.getBytes(DEFAULT_ENCODING));
//            return rez;
//        } catch (UnsupportedEncodingException e) {
//            return null;
//        }
//    }//base64encode
//    public static String base64decode(String text) {
//
//        try {
//            return new String(dec.decodeBuffer(text), DEFAULT_ENCODING);
//        } catch (IOException e) {
//            return null;
//        }
//
//    }//base64decode
    public static String xorMessage(String message, String key) {
        try {
            if (message == null || key == null) {
                return null;
            }

            char[] keys = key.toCharArray();
            char[] mesg = message.toCharArray();

            int ml = mesg.length;
            int kl = keys.length;
            char[] newmsg = new char[ml];

            for (int i = 0; i < ml; i++) {
                newmsg[i] = (char) (mesg[i] ^ keys[i % kl]);
            }//for i
            mesg = null;
            keys = null;
            return new String(newmsg);
        } catch (Exception e) {
            return null;
        }
    }//xorMessage

//    private static String readFileAsString(String filePath)
//            throws java.io.IOException {
//        StringBuffer fileData = new StringBuffer(1000);
//        BufferedReader reader = new BufferedReader(
//                new FileReader(filePath));
//        char[] buf = new char[1024];
//        int numRead = 0;
//        while ((numRead = reader.read(buf)) != -1) {
//            String readData = String.valueOf(buf, 0, numRead);
//            fileData.append(readData);
//            buf = new char[1024];
//        }
//        reader.close();
//        return fileData.toString();
//    }
    @GlobalDBOpenCloseAndUserPrivilages
    public Map checkuploadexists(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String accyear) {
        Map resultMap = new HashMap();
        try {
            //System.out.println("region===" + region.toString().trim());
            //System.out.println("accyear===" + accyear.toString().trim());
            Criteria bdtCrit = session.createCriteria(Budgetdetails.class);
//            budgetDetailsCrit.add(Restrictions.sqlRestriction("id='" + budgetDetailsObjectModelObj.getId() + "'"));
            bdtCrit.add(Restrictions.sqlRestriction("regioncode='" + region.toString().trim() + "'"));
            bdtCrit.add(Restrictions.sqlRestriction("budgetid='" + accyear.toString().trim() + "'"));
            List budgetList = bdtCrit.list();
            if (budgetList.size() > 0) {
                resultMap.put("ERROR", "Already this Region Budget File Uploaded.Do You Want to Upload Again?");
            } else {
                resultMap.put("message", "continue");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            request.setAttribute("message", "Failed to upload");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map upLoadTxtFile(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, FormFile filename) {
        Map resultMap = new HashMap();
        FileOutputStream fop = null;
        try {
            String respectivePath = getFilePath(request, LoggedInRegion);

            FormFile formFile = (FormFile) filename;

            byte[] byteFile = formFile.getFileData();
            File f = new File(respectivePath);
            fop = new FileOutputStream(f);
            fop.write(byteFile);
            fop.close();

            resultMap.put("message", "success");
        } catch (Exception ex) {
            ex.printStackTrace();
            resultMap.put("message", "Failed");
//            request.setAttribute("message", "Failed to upload");
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map displayTextFileDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        String respectivePath = getFilePath(request, LoggedInRegion);
        String content = null;
        String result = "";
        BudgetDetailObject budgetDetailObjectObj = null;
        try {
            content = readFileAsString(respectivePath);

            String key = "sairam";
            String txt = "";
            //base64decode(content);
            result = xorMessage(txt, key);
            //System.out.println("----------" + result);

            JAXBContext context;
            context = JAXBContext.newInstance(BudgetDetailObject.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            budgetDetailObjectObj = (BudgetDetailObject) unmarshaller.unmarshal(new StringReader(result));


            List<BudgetDetailsObjectModel> budgetDetailObjectList = budgetDetailObjectObj.getBudgetDetailsObjectModelList();
            for (int i = 0; i < budgetDetailObjectList.size(); i++) {

                BudgetDetailsObjectModel budgetDetailsObjectModelObj = (BudgetDetailsObjectModel) budgetDetailObjectList.get(i);

                Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
                budgetDetailsCrit.add(Restrictions.sqlRestriction("id='" + budgetDetailsObjectModelObj.getId() + "'"));

                List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
                Budgetdetails budgetdetailsObj;
                if (budgetdetailsList.size() > 0) {
                    budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);

                    budgetdetailsObj.setBudget(getBudget(session, budgetDetailsObjectModelObj.getBudget()));
                    budgetdetailsObj.setRegionmaster(getRegion(session, budgetDetailsObjectModelObj.getRegionmaster()));
                    budgetdetailsObj.setLedgermaster(getLedgetMaster(session, budgetDetailsObjectModelObj.getLedgermaster()));
                    budgetdetailsObj.setActual(new BigDecimal(budgetDetailsObjectModelObj.getActual()));
                    budgetdetailsObj.setBudgetestimate(new BigDecimal(budgetDetailsObjectModelObj.getBudgetestimate()));
                    budgetdetailsObj.setRevisedbudgetestimate(new BigDecimal(budgetDetailsObjectModelObj.getRevisedbudgetestimate()));
                    budgetdetailsObj.setActualoffirsthalfyesr(new BigDecimal(budgetDetailsObjectModelObj.getActualoffirsthalfyesr()));
                    budgetdetailsObj.setActualofsecondhalfyesr(new BigDecimal(budgetDetailsObjectModelObj.getActualofsecondhalfyesr()));
                    budgetdetailsObj.setProbableforsecondhalfyear(new BigDecimal(budgetDetailsObjectModelObj.getProbableforsecondhalfyear()));
//                    budgetdetailsObj.setHobudget(new BigDecimal(budgetDetailsObjectModelObj.getHobudget()));
//                    budgetdetailsObj.setHorevisedbudget(new BigDecimal(budgetDetailsObjectModelObj.getHorevisedbudget()));

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.update(budgetdetailsObj);
                    transaction.commit();

                } else {
                    budgetdetailsObj = new Budgetdetails();

                    budgetdetailsObj.setId(budgetDetailsObjectModelObj.getId());
                    budgetdetailsObj.setBudget(getBudget(session, budgetDetailsObjectModelObj.getBudget()));
                    budgetdetailsObj.setRegionmaster(getRegion(session, budgetDetailsObjectModelObj.getRegionmaster()));
                    budgetdetailsObj.setLedgermaster(getLedgetMaster(session, budgetDetailsObjectModelObj.getLedgermaster()));
                    budgetdetailsObj.setActual(new BigDecimal(budgetDetailsObjectModelObj.getActual()));
                    budgetdetailsObj.setBudgetestimate(new BigDecimal(budgetDetailsObjectModelObj.getBudgetestimate()));
                    budgetdetailsObj.setRevisedbudgetestimate(new BigDecimal(budgetDetailsObjectModelObj.getRevisedbudgetestimate()));
                    budgetdetailsObj.setActualoffirsthalfyesr(new BigDecimal(budgetDetailsObjectModelObj.getActualoffirsthalfyesr()));
                    budgetdetailsObj.setActualofsecondhalfyesr(new BigDecimal(budgetDetailsObjectModelObj.getActualofsecondhalfyesr()));
                    budgetdetailsObj.setProbableforsecondhalfyear(new BigDecimal(budgetDetailsObjectModelObj.getProbableforsecondhalfyear()));
                    budgetdetailsObj.setHobudget(new BigDecimal(budgetDetailsObjectModelObj.getHobudget()));
                    budgetdetailsObj.setHorevisedbudget(new BigDecimal(budgetDetailsObjectModelObj.getHorevisedbudget()));


                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.save(budgetdetailsObj);
                    transaction.commit();
                }



            }

            StringBuffer resultHTML = new StringBuffer();
            resultHTML.append("<table width=\"20%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr >").append("<td>Region Name</td>").append("<td>" + budgetDetailObjectObj.getRegion() + "</td>").append("</tr>");
            resultHTML.append("<tr >").append("<td>Budget Year</td>").append("<td>" + budgetDetailObjectObj.getStartyear() + "-" + budgetDetailObjectObj.getEndyear() + "</td>").append("</tr>");
            //resultHTML.append("<tr >").append("<td colspan=\"2\">" + "<input type=\"button\" class=\"submitbu\" name=\"save\" id=\"save\" value=\"Save\" onclick=\"SaveFileToDB()\">" + "</td>").append("</tr>");
            resultHTML.append("</table>");
            resultMap.put("display", resultHTML.toString());

        } catch (IOException ex) {
            resultMap.put("ERROR", "Given Text File Corrupted");
            //System.out.println("=================");
            Logger.getLogger(BudgetServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            //System.out.println("************");
            resultMap.put("ERROR", "Given Text File corrupted");
            Logger.getLogger(BudgetServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
        }
        return resultMap;
    }

    public Map saveTextFiletoDB(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        String respectivePath = getFilePath(request, LoggedInRegion);
        String content;
        String result = "";
        BudgetDetailObject budgetDetailObjectObj = null;
        try {
            content = readFileAsString(respectivePath);

            String key = "sairam";
            String txt = "";
            //base64decode(content);
            result = xorMessage(txt, key);
//            System.out.println(result);

        } catch (IOException ex) {
            Logger.getLogger(BudgetServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            JAXBContext context;
            context = JAXBContext.newInstance(BudgetDetailObject.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            budgetDetailObjectObj = (BudgetDetailObject) unmarshaller.unmarshal(new StringReader(result));
        } catch (JAXBException ex) {

            Logger.getLogger(BudgetServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<BudgetDetailsObjectModel> budgetDetailObjectList = budgetDetailObjectObj.getBudgetDetailsObjectModelList();
        for (int i = 0; i < budgetDetailObjectList.size(); i++) {

            BudgetDetailsObjectModel budgetDetailsObjectModelObj = (BudgetDetailsObjectModel) budgetDetailObjectList.get(i);

            Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
            budgetDetailsCrit.add(Restrictions.sqlRestriction("id='" + budgetDetailsObjectModelObj.getId() + "'"));

            List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
            Budgetdetails budgetdetailsObj;
            if (budgetdetailsList.size() > 0) {
                budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);

                budgetdetailsObj.setBudget(getBudget(session, budgetDetailsObjectModelObj.getBudget()));
                budgetdetailsObj.setRegionmaster(getRegion(session, budgetDetailsObjectModelObj.getRegionmaster()));
                budgetdetailsObj.setLedgermaster(getLedgetMaster(session, budgetDetailsObjectModelObj.getLedgermaster()));
                budgetdetailsObj.setActual(new BigDecimal(budgetDetailsObjectModelObj.getActual()));
                budgetdetailsObj.setBudgetestimate(new BigDecimal(budgetDetailsObjectModelObj.getBudgetestimate()));
                budgetdetailsObj.setRevisedbudgetestimate(new BigDecimal(budgetDetailsObjectModelObj.getRevisedbudgetestimate()));
                budgetdetailsObj.setActualoffirsthalfyesr(new BigDecimal(budgetDetailsObjectModelObj.getActualoffirsthalfyesr()));
                budgetdetailsObj.setActualofsecondhalfyesr(new BigDecimal(budgetDetailsObjectModelObj.getActualofsecondhalfyesr()));
                budgetdetailsObj.setProbableforsecondhalfyear(new BigDecimal(budgetDetailsObjectModelObj.getProbableforsecondhalfyear()));

                Transaction transaction;
                transaction = session.beginTransaction();
                session.update(budgetdetailsObj);
                transaction.commit();

            } else {
                budgetdetailsObj = new Budgetdetails();

                budgetdetailsObj.setId(budgetDetailsObjectModelObj.getId());
                budgetdetailsObj.setBudget(getBudget(session, budgetDetailsObjectModelObj.getBudget()));
                budgetdetailsObj.setRegionmaster(getRegion(session, budgetDetailsObjectModelObj.getRegionmaster()));
                budgetdetailsObj.setLedgermaster(getLedgetMaster(session, budgetDetailsObjectModelObj.getLedgermaster()));
                budgetdetailsObj.setActual(new BigDecimal(budgetDetailsObjectModelObj.getActual()));
                budgetdetailsObj.setBudgetestimate(new BigDecimal(budgetDetailsObjectModelObj.getBudgetestimate()));
                budgetdetailsObj.setRevisedbudgetestimate(new BigDecimal(budgetDetailsObjectModelObj.getRevisedbudgetestimate()));
                budgetdetailsObj.setActualoffirsthalfyesr(new BigDecimal(budgetDetailsObjectModelObj.getActualoffirsthalfyesr()));
                budgetdetailsObj.setActualofsecondhalfyesr(new BigDecimal(budgetDetailsObjectModelObj.getActualofsecondhalfyesr()));
                budgetdetailsObj.setProbableforsecondhalfyear(new BigDecimal(budgetDetailsObjectModelObj.getProbableforsecondhalfyear()));



                Transaction transaction;
                transaction = session.beginTransaction();
                session.save(budgetdetailsObj);
                transaction.commit();
            }



        }
        resultMap.put("status", "Saved Successfully");
        return resultMap;
    }

    private String getFilePath(HttpServletRequest request, String LoggedInRegion) {
        String path = request.getRealPath("/");
        //System.out.println("path====" + path);
        String sessionid = request.getSession(false).getId();
        String osName = System.getProperty("os.name");
        String fileName = "budgetfile";
        String folder = path + LoggedInRegion + "_" + sessionid;
//        String folder = path+userid+"_"+sessionid;

        File dir = new File(folder);
        dir.delete();
        if (!dir.exists()) {
            dir.mkdir();
        }

        String resPath = "";
        if (osName.equalsIgnoreCase("Linux") || osName.equalsIgnoreCase("Unix")) {
            resPath = folder + "/" + fileName + ".txt";
        } else {
            resPath = folder + "\\" + fileName + ".txt";
        }
        //System.out.println(" resPath : " + resPath);
        return resPath;
    }

    public String getMyRegionName(Session session, String region) {
        String regionName = "";

        Criteria empRegionCrit = session.createCriteria(Regionmaster.class);
        empRegionCrit.add(Restrictions.sqlRestriction("id='" + region + "'"));
        List empRegionList = empRegionCrit.list();
        if (empRegionList.size() > 0) {
            Regionmaster regionmasterObj = (Regionmaster) empRegionList.get(0);
            regionName = regionmasterObj.getRegionname();
        }

        return regionName;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map BudgetReportPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String region, String startyear, String endyear) {
        String budgetId = "";
        String firstBudgetId = "";
        String secondBudgetId = "";
        String thirdBudgetId = "";
        String nextBudgetId = "";
        String classname = "";
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        ReportModel reportModel = null;
        BudgetReport budgetReport = new BudgetReport();
        //System.out.println("start year" + startyear + "end year" + endyear);

        int firststartyear = Integer.parseInt(startyear) - 3;
        int firstendyear = Integer.parseInt(endyear) - 3;
        Criteria firstbudgetCrit = session.createCriteria(Budget.class);
        firstbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        firstbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + firststartyear));
        firstbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        firstbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + firstendyear));
        List<Budget> firstbudgetList = firstbudgetCrit.list();
        Budget firstbudgetObj = null;
        if (firstbudgetList.size() > 0) {
            firstbudgetObj = (Budget) firstbudgetList.get(0);
            firstBudgetId = firstbudgetObj.getId();
        }

        int secondstartyear = Integer.parseInt(startyear) - 2;
        int secondendyear = Integer.parseInt(endyear) - 2;
        Criteria secondbudgetCrit = session.createCriteria(Budget.class);
        secondbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        secondbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + secondstartyear));
        secondbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        secondbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + secondendyear));
        List<Budget> secondbudgetList = secondbudgetCrit.list();
        Budget secondbudgetObj = null;
        if (secondbudgetList.size() > 0) {
            secondbudgetObj = (Budget) secondbudgetList.get(0);
            secondBudgetId = secondbudgetObj.getId();
        }

        int thirdstartyear = Integer.parseInt(startyear) - 1;
        int thirdendyear = Integer.parseInt(endyear) - 1;
        Criteria thirdbudgetCrit = session.createCriteria(Budget.class);
        thirdbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + thirdstartyear));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + thirdendyear));
        List<Budget> thirdbudgetList = thirdbudgetCrit.list();
        Budget thirdbudgetObj = null;
        if (thirdbudgetList.size() > 0) {
            thirdbudgetObj = (Budget) thirdbudgetList.get(0);
            thirdBudgetId = thirdbudgetObj.getId();
        }

        int nextstartyear = Integer.parseInt(startyear) + 1;
        int nextendyear = Integer.parseInt(endyear) + 1;
        Criteria nextbudgetCrit = session.createCriteria(Budget.class);
        nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
        List<Budget> nextbudgetList = nextbudgetCrit.list();
        Budget nextbudgetObj = null;
        if (nextbudgetList.size() > 0) {
            nextbudgetObj = (Budget) nextbudgetList.get(0);
            nextBudgetId = nextbudgetObj.getId();
        }


        Criteria budgetCrit = session.createCriteria(Budget.class);
        budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
        budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
        List<Budget> budgetList = budgetCrit.list();
        Budget budgetObj = null;
        if (budgetList.size() > 0) {
            budgetObj = (Budget) budgetList.get(0);
            budgetId = budgetObj.getId();
        }
        String ledgeryear = "b" + budgetId;
        if (budgetId.trim().length() > 0) {
            Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
            ledgerCrit.add(Restrictions.sqlRestriction(" " + ledgeryear + " is true "));
            List<Ledgermaster> ledgerList = ledgerCrit.list();
            if (ledgerList.size() > 0) {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">");
                resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr class=\"gridmenu\">");
                resultHTML.append("<td>S.No</td>");
                resultHTML.append("<td>Account</td>");
                resultHTML.append("<td>" + firststartyear + "-" + firstendyear + "</td>");
                resultHTML.append("<td>" + secondstartyear + "-" + secondendyear + "</td>");
                resultHTML.append("<td>" + thirdstartyear + "-" + thirdendyear + "</td>");
                resultHTML.append("<td>Average</td>");
                resultHTML.append("<td>Budget Estimate<br>" + startyear + "-" + endyear + "</td>");
                resultHTML.append("<td>Actual <br>Upto Sep</td>");
                resultHTML.append("<td>Probable <br>oct to Mar</td>");
                resultHTML.append("<td>Rev. Esti<br>" + startyear + "-" + endyear + "</td>");
//                resultHTML.append("<td>Rev. Esti</td>");
                resultHTML.append("<td>Budget Estimate<br>" + endyear + "-" + (Integer.parseInt(endyear) + 1) + "</td>");
                resultHTML.append("<td>Rev. Esti <br>" + startyear + "-" + endyear + "</td>");
                resultHTML.append("<td>Budget Estimate <br>" + endyear + "-" + (Integer.parseInt(endyear) + 1) + "</td>");
                resultHTML.append("<td>Modify</td>");
                resultHTML.append("</tr>");

                int slipno = 1;
                for (int i = 0; i < ledgerList.size(); i++) {
                    Ledgermaster ledgermasterObj = (Ledgermaster) ledgerList.get(i);

                    Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
                    budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + region + "'"));
                    budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                    budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
                    List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
                    Budgetdetails budgetdetailsObj = null;
                    if (budgetdetailsList.size() > 0) {
                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }

                        budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);

                        BigDecimal firstactual = getBudget(session, region, firstBudgetId, ledgermasterObj.getId());
                        BigDecimal secondactual = getBudget(session, region, secondBudgetId, ledgermasterObj.getId());
                        BigDecimal thirdactual = getBudget(session, region, thirdBudgetId, ledgermasterObj.getId());

                        float average = 0;
                        if (firstactual.floatValue() != 0 || secondactual.floatValue() != 0 || thirdactual.floatValue() != 0) {
                            average = (firstactual.floatValue() + secondactual.floatValue() + thirdactual.floatValue()) / 3;
                        }

                        BigDecimal averageActual = StringtobigDecimal(String.valueOf(average));

                        BigDecimal budgetestimate = budgetdetailsObj.getBudgetestimate();
                        BigDecimal actualfirsthalf = budgetdetailsObj.getActualoffirsthalfyesr();
                        BigDecimal probablesecondhalf = budgetdetailsObj.getProbableforsecondhalfyear();
                        BigDecimal revisedestimate = budgetdetailsObj.getRevisedbudgetestimate();

                        BigDecimal budgetestimatenext = getBudget(session, region, nextBudgetId, ledgermasterObj.getId());
                        BigDecimal budgetho = budgetdetailsObj.getHobudget();
                        BigDecimal revisedestimateho = getBudgetNextYearHORevisedEstimate(session, region, nextBudgetId, ledgermasterObj.getId());


//                        System.out.println("iddddddd=====" + budgetdetailsObj.getId());

                        resultHTML.append("<tr class=\"" + classname + "\">");
                        resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                        resultHTML.append("<td align=\"left\">" + ledgermasterObj.getLedgername() + "</td>");
                        resultHTML.append("<td align=\"center\">" + firstactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + secondactual + "</td>");
                        resultHTML.append("<td align=\"center\">" + thirdactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + averageActual + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimate + "</td>");
                        resultHTML.append("<td align=\"right\">" + actualfirsthalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + probablesecondhalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimate + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimatenext + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetho + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimateho + "</td>");
                        resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"loancode\" id=\"" + budgetdetailsObj.getId() + "\" onclick=\"setBudgetDetails('" + budgetdetailsObj.getId() + "','" + budgetdetailsObj.getLedgermaster().getId() + "','" + budgetdetailsObj.getLedgermaster().getLedgername() + "','" + firstactual + "','" + secondactual + "','" + thirdactual + "','" + averageActual + "','" + budgetestimate + "','" + actualfirsthalf + "','" + probablesecondhalf + "','" + revisedestimate + "','" + budgetestimatenext + "','" + budgetho + "','" + revisedestimateho + "')\"></td>");
                        resultHTML.append("</tr>");

                        String ledgergroupname = ledgermasterObj.getLedgergroupmaster().getLedgergroupname();
                        reportModel = new ReportModel();

                        //System.out.println("startyear = "+startyear);
                        //System.out.println("endyear = "+endyear);

                        reportModel.setStartyear(Integer.valueOf(startyear));
                        reportModel.setEndyear(Integer.valueOf(endyear));

                        reportModel.setSlipno(slipno);
                        reportModel.setLedgername(ledgermasterObj.getLedgername());
                        reportModel.setFirstactual(firstactual.toString());
                        reportModel.setSecondactual(secondactual.toString());
                        reportModel.setThirdactual(thirdactual.toString());
                        reportModel.setAverage(String.valueOf(decimalFormat.format(average)));
                        reportModel.setBudgetestimate(budgetestimate.toString());
                        reportModel.setActualfirsthalf(actualfirsthalf.toString());
                        reportModel.setProbablesecondhalf(probablesecondhalf.toString());
                        reportModel.setRevisedestimate(revisedestimate.toString());
                        reportModel.setBudgetestimatenext(budgetestimatenext.toString());
                        reportModel.setLedgergroupname(ledgergroupname);
                        reportModel.setFirststartyear(String.valueOf(firststartyear));
                        reportModel.setFirstendyear(String.valueOf(firstendyear));
                        reportModel.setSecondstartyear(String.valueOf(secondstartyear));
                        reportModel.setSecondendyear(String.valueOf(secondendyear));
                        reportModel.setThirdstartyear(String.valueOf(thirdstartyear));
                        reportModel.setThirdendyear(String.valueOf(thirdendyear));
                        reportModel.setHorevisedestimate(String.valueOf(budgetho));
                        reportModel.setHobudgetestimate(String.valueOf(revisedestimateho));
                        reportModel.setRegionname(getMyRegionName(session, region));

                        budgetReport.getBudgetReportPrintWriter(reportModel, filePath);
                        slipno++;
                    }
                }
//                resultHTML.append("<tr class=\"" + classname + "\">")
//                        .append("<td colspan=\"12\" align=\"center\">" + "<input type=\"button\" class=\"submitbu\" name=\"show\" id=\"show\" value=\"Download\" onclick=\"downloadbudget('" + startyear + "','" + endyear + "')\">" + "</td>")
//                        .append("</tr>");
                budgetReport.budgetReportGrandTotal(filePath);
                resultHTML.append("</table>");
            }
        }

        resultMap.put("budgetdetails", resultHTML.toString());
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map BudgetReportRegionPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String startyear, String endyear) {
        String budgetId = "";
        String firstBudgetId = "";
        String secondBudgetId = "";
        String thirdBudgetId = "";
        String nextBudgetId = "";
        String classname = "";
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();
        ReportModel reportModel = null;
        try {
            BudgetRegionReport budgetRegionReport = new BudgetRegionReport();

            //System.out.println("start year" + startyear + "end year" + endyear);

            int firststartyear = Integer.parseInt(startyear) - 3;
            int firstendyear = Integer.parseInt(endyear) - 3;
            Criteria firstbudgetCrit = session.createCriteria(Budget.class);
            firstbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            firstbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + firststartyear));
            firstbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            firstbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + firstendyear));
            List<Budget> firstbudgetList = firstbudgetCrit.list();
            Budget firstbudgetObj = null;
            if (firstbudgetList.size() > 0) {
                firstbudgetObj = (Budget) firstbudgetList.get(0);
                firstBudgetId = firstbudgetObj.getId();
            }

            int secondstartyear = Integer.parseInt(startyear) - 2;
            int secondendyear = Integer.parseInt(endyear) - 2;
            Criteria secondbudgetCrit = session.createCriteria(Budget.class);
            secondbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            secondbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + secondstartyear));
            secondbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            secondbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + secondendyear));
            List<Budget> secondbudgetList = secondbudgetCrit.list();
            Budget secondbudgetObj = null;
            if (secondbudgetList.size() > 0) {
                secondbudgetObj = (Budget) secondbudgetList.get(0);
                secondBudgetId = secondbudgetObj.getId();
            }

            int thirdstartyear = Integer.parseInt(startyear) - 1;
            int thirdendyear = Integer.parseInt(endyear) - 1;
            Criteria thirdbudgetCrit = session.createCriteria(Budget.class);
            thirdbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + thirdstartyear));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + thirdendyear));
            List<Budget> thirdbudgetList = thirdbudgetCrit.list();
            Budget thirdbudgetObj = null;
            if (thirdbudgetList.size() > 0) {
                thirdbudgetObj = (Budget) thirdbudgetList.get(0);
                thirdBudgetId = thirdbudgetObj.getId();
            }

            int nextstartyear = Integer.parseInt(startyear) + 1;
            int nextendyear = Integer.parseInt(endyear) + 1;
            Criteria nextbudgetCrit = session.createCriteria(Budget.class);
            nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
            List<Budget> nextbudgetList = nextbudgetCrit.list();
            Budget nextbudgetObj = null;
            if (nextbudgetList.size() > 0) {
                nextbudgetObj = (Budget) nextbudgetList.get(0);
                nextBudgetId = nextbudgetObj.getId();
            }


            Criteria budgetCrit = session.createCriteria(Budget.class);
            budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
            budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
            List<Budget> budgetList = budgetCrit.list();
            Budget budgetObj = null;
            if (budgetList.size() > 0) {
                budgetObj = (Budget) budgetList.get(0);
                budgetId = budgetObj.getId();
            }
            if (budgetId.trim().length() > 0) {
                Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
//                ledgerCrit.addOrder(Order.asc("ledgergroupid"));
                List<Ledgermaster> ledgerList = ledgerCrit.list();
                if (ledgerList.size() > 0) {
                    resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                    resultHTML.append("<tr><td valign=\"top\">");
                    resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                    resultHTML.append("<tr class=\"gridmenu\">");
                    resultHTML.append("<td>S.No</td>");
                    resultHTML.append("<td>Account</td>");
                    resultHTML.append("<td>" + firststartyear + "-" + firstendyear + " Actual</td>");
                    resultHTML.append("<td>" + secondstartyear + "-" + secondendyear + " Actual</td>");
                    resultHTML.append("<td>" + thirdstartyear + "-" + thirdendyear + " Actual</td>");
                    resultHTML.append("<td>Average</td>");
                    resultHTML.append("<td>Budget Estimate</td>");
                    resultHTML.append("<td>Actual Upto Sep</td>");
                    resultHTML.append("<td>Probable oct to Mar</td>");
                    resultHTML.append("<td>Rev. Esti</td>");
                    resultHTML.append("<td>Budget Estimate</td>");
                    resultHTML.append("<td>Modify</td>");
                    resultHTML.append("</tr>");

                    int slipno = 1;
                    for (int i = 0; i < ledgerList.size(); i++) {
                        Ledgermaster ledgermasterObj = (Ledgermaster) ledgerList.get(i);

                        Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
                        List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
                        Budgetdetails budgetdetailsObj;
                        if (budgetdetailsList.size() > 0) {
                            budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);
                        } else {
                            budgetdetailsObj = new Budgetdetails();
                            String id = getMaxBudgetdetailsid(session, LoggedInRegion);
                            budgetdetailsObj.setId(id);
                            budgetdetailsObj.setBudget(budgetObj);
                            budgetdetailsObj.setRegionmaster(getRegion(session, LoggedInRegion));
                            budgetdetailsObj.setLedgermaster(ledgermasterObj);
                            budgetdetailsObj.setActual(BigDecimal.ZERO);
                            budgetdetailsObj.setBudgetestimate(BigDecimal.ZERO);
                            budgetdetailsObj.setRevisedbudgetestimate(BigDecimal.ZERO);
                            budgetdetailsObj.setActualoffirsthalfyesr(BigDecimal.ZERO);
                            budgetdetailsObj.setActualofsecondhalfyesr(BigDecimal.ZERO);
                            budgetdetailsObj.setProbableforsecondhalfyear(BigDecimal.ZERO);


                            Transaction transaction;
                            transaction = session.beginTransaction();
                            session.save(budgetdetailsObj);
                            transaction.commit();
                        }
                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }


                        BigDecimal firstactual = getBudget(session, LoggedInRegion, firstBudgetId, ledgermasterObj.getId());
                        BigDecimal secondactual = getBudget(session, LoggedInRegion, secondBudgetId, ledgermasterObj.getId());
                        BigDecimal thirdactual = getBudget(session, LoggedInRegion, thirdBudgetId, ledgermasterObj.getId());

                        float average = 0;
                        if (firstactual.floatValue() != 0 || secondactual.floatValue() != 0 || thirdactual.floatValue() != 0) {
                            average = (firstactual.floatValue() + secondactual.floatValue() + thirdactual.floatValue()) / 3;
                        }

                        BigDecimal budgetestimate = budgetdetailsObj.getBudgetestimate();
                        BigDecimal actualfirsthalf = budgetdetailsObj.getActualoffirsthalfyesr();
                        BigDecimal probablesecondhalf = budgetdetailsObj.getProbableforsecondhalfyear();
                        BigDecimal revisedestimate = budgetdetailsObj.getRevisedbudgetestimate();
                        BigDecimal budgetestimatenext = getBudget(session, LoggedInRegion, nextBudgetId, ledgermasterObj.getId());



                        resultHTML.append("<tr class=\"" + classname + "\">");
                        resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                        resultHTML.append("<td align=\"left\">" + ledgermasterObj.getLedgername() + "</td>");
                        resultHTML.append("<td align=\"center\">" + firstactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + secondactual + "</td>");
                        resultHTML.append("<td align=\"center\">" + thirdactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + average + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimate + "</td>");
                        resultHTML.append("<td align=\"right\">" + actualfirsthalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + probablesecondhalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimate + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimatenext + "</td>");

                        String ledgergroupname = ledgermasterObj.getLedgergroupmaster().getLedgergroupname();
                        reportModel = new ReportModel();

                        //System.out.println("startyear = "+startyear);
                        //System.out.println("endyear = "+endyear);

                        reportModel.setStartyear(Integer.valueOf(startyear));
                        reportModel.setEndyear(Integer.valueOf(endyear));

                        reportModel.setSlipno(slipno);
                        reportModel.setLedgername(ledgermasterObj.getLedgername());
                        reportModel.setFirstactual(firstactual.toString());
                        reportModel.setSecondactual(secondactual.toString());
                        reportModel.setThirdactual(thirdactual.toString());
                        reportModel.setAverage(String.valueOf(decimalFormat.format(average)));
                        reportModel.setBudgetestimate(budgetestimate.toString());
                        reportModel.setActualfirsthalf(actualfirsthalf.toString());
                        reportModel.setProbablesecondhalf(probablesecondhalf.toString());
                        reportModel.setRevisedestimate(revisedestimate.toString());
                        reportModel.setBudgetestimatenext(budgetestimatenext.toString());
                        reportModel.setLedgergroupname(ledgergroupname);
                        reportModel.setFirststartyear(String.valueOf(firststartyear));
                        reportModel.setFirstendyear(String.valueOf(firstendyear));
                        reportModel.setSecondstartyear(String.valueOf(secondstartyear));
                        reportModel.setSecondendyear(String.valueOf(secondendyear));
                        reportModel.setThirdstartyear(String.valueOf(thirdstartyear));
                        reportModel.setThirdendyear(String.valueOf(thirdendyear));
                        reportModel.setHorevisedestimate("");
                        reportModel.setHobudgetestimate("");

                        budgetRegionReport.getBudgetReportPrintWriter(reportModel, filePath);
                        slipno++;

                        resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"loancode\" id=\"" + budgetdetailsObj.getId() + "\" onclick=\"setBudgetDetails('" + budgetdetailsObj.getId() + "','" + budgetdetailsObj.getLedgermaster().getId() + "','" + budgetdetailsObj.getLedgermaster().getLedgername() + "','" + firstactual + "','" + secondactual + "','" + thirdactual + "','" + average + "','" + budgetestimate + "','" + actualfirsthalf + "','" + probablesecondhalf + "','" + revisedestimate + "','" + budgetestimatenext + "')\"></td>").append("</tr>");

                    }
                    //System.out.println("::::::::::::::::::::::::::::: filePath - > "+filePath);
                    budgetRegionReport.budgetReportGrandTotal(filePath);
                    resultHTML.append("<tr class=\"" + classname + "\">").append("<td colspan=\"12\" align=\"center\">" + "<input type=\"button\" class=\"submitbu\" name=\"show\" id=\"show\" value=\"Download\" onclick=\"downloadbudget('" + startyear + "','" + endyear + "')\">" + "</td>").append("</tr>");

                    resultHTML.append("</table>");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        resultMap.put("budgetdetails", resultHTML.toString());
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map BudgetReportHOPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String region, String startyear, String endyear) {
        System.out.println("************************ BudgetServiceImpl classs BudgetReportHOPrint method is calling ***************************");
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        ReportModel reportModel = null;
        BudgetReportHO budgetReportHO = new BudgetReportHO();
//        System.out.println("start year" + startyear + "end year" + endyear);
//        System.out.println("region = " + region);
        String budgetId = "";
        String nextBudgetId = "";
        String previousBudgetId = "";


        int previousstartyear = Integer.parseInt(startyear) - 1;
        int previousendyear = Integer.parseInt(endyear) - 1;
        Criteria previousbudgetCrit = session.createCriteria(Budget.class);
        previousbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        previousbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + previousstartyear));
        previousbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        previousbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + previousendyear));
        List<Budget> previcousbudgetList = previousbudgetCrit.list();
        Budget previousbudgetObj = null;
        if (previcousbudgetList.size() > 0) {
            previousbudgetObj = (Budget) previcousbudgetList.get(0);
            previousBudgetId = previousbudgetObj.getId();
        }

        int nextstartyear = Integer.parseInt(startyear) + 1;
        int nextendyear = Integer.parseInt(endyear) + 1;
        Criteria nextbudgetCrit = session.createCriteria(Budget.class);
        nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
        List<Budget> nextbudgetList = nextbudgetCrit.list();
        Budget nextbudgetObj = null;
        if (nextbudgetList.size() > 0) {
            nextbudgetObj = (Budget) nextbudgetList.get(0);
            nextBudgetId = nextbudgetObj.getId();
        }

//        System.out.println("nextBudgetId = " + nextBudgetId);

        Criteria budgetCrit = session.createCriteria(Budget.class);
        budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
        budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
        List<Budget> budgetList = budgetCrit.list();
        Budget budgetObj = null;
        if (budgetList.size() > 0) {
            budgetObj = (Budget) budgetList.get(0);
            budgetId = budgetObj.getId();
        }

//        System.out.println("budgetId = " + budgetId);
        String ledgeryear = "b" + budgetId;

        int slipno = 1;
        Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
        ledgerCrit.add(Restrictions.sqlRestriction(" " + ledgeryear + " is true "));

        List<Ledgermaster> ledgerList = ledgerCrit.list();
        if (ledgerList.size() > 0) {
            for (int i = 0; i < ledgerList.size(); i++) {
                Ledgermaster ledgermasterObj = (Ledgermaster) ledgerList.get(i);

//                System.out.println("ledgermasterObj.getId() = " + ledgermasterObj.getId());
//                System.out.println("ledgermasterObj.getLedgergroupmaster().getLedgergroupname() = " + ledgermasterObj.getLedgergroupmaster().getLedgergroupname());
//                System.out.println("ledgermasterObj.getLedgername() = " + ledgermasterObj.getLedgername());

                reportModel = new ReportModel();

                Criteria ReCriteria = session.createCriteria(Budgetdetails.class);
                ReCriteria.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                ReCriteria.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
                if (!region.equals("0")) {
                    ReCriteria.add(Restrictions.sqlRestriction("regioncode='" + region + "'"));
                }
                double reamount = 0;
                double budgetamount = 0;
                List ReList = ReCriteria.list();
                if (ReList.size() > 0) {
                    for (int j = 0; j < ReList.size(); j++) {
                        Budgetdetails bd = (Budgetdetails) ReList.get(j);
                        BigDecimal bigHobudget = bd.getHobudget();
                        BigDecimal bighorevisedbudget = bd.getBudgetestimate();
                        reamount += bigHobudget.doubleValue();
                        budgetamount += bighorevisedbudget.doubleValue();
                    }
                }

                Criteria BeCriteria = session.createCriteria(Budgetdetails.class);
                BeCriteria.add(Restrictions.sqlRestriction("budgetid='" + nextBudgetId + "'"));
                BeCriteria.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
                if (!region.equals("0")) {
                    BeCriteria.add(Restrictions.sqlRestriction("regioncode='" + region + "'"));
                }
                double beamount = 0;
                List BeList = BeCriteria.list();
                if (BeList.size() > 0) {
                    for (int j = 0; j < BeList.size(); j++) {
                        Budgetdetails bd = (Budgetdetails) BeList.get(j);
                        BigDecimal bigHorevisedbudget = bd.getHorevisedbudget();
                        beamount += bigHorevisedbudget.doubleValue();
                    }
                }

                BigDecimal thirdactual = getBudget(session, LoggedInRegion, previousBudgetId, ledgermasterObj.getId());

                reportModel.setAcccode(ledgermasterObj.getId());
                reportModel.setLedgergroupname(ledgermasterObj.getLedgergroupmaster().getLedgergroupname());
                reportModel.setLedgername(ledgermasterObj.getLedgername());
                reportModel.setBudgetestimate(decimalFormat.format(budgetamount));
                reportModel.setHorevisedestimate(decimalFormat.format(reamount));
                reportModel.setHobudgetestimate(decimalFormat.format(beamount));
                reportModel.setStartyear(Integer.valueOf(startyear));
                reportModel.setEndyear(Integer.valueOf(endyear));
                reportModel.setThirdactual(thirdactual.toString());
                if (region.equals("0")) {
                    reportModel.setRegionname("All Region");
                } else {
                    reportModel.setRegionname(getMyRegionName(session, region));
                }
                reportModel.setSlipno(slipno);
                budgetReportHO.getBudgetReportPrintWriter(reportModel, filePath);
                slipno++;
            }
        }
        budgetReportHO.budgetReportGrandTotal(filePath);
        resultMap.put("budgetdetails", resultHTML.toString());
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getMyRegionCode(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        String regionCode = "";
        SessionFactory _factory = HibernateUtil.getSessionFactory();
        Session hibernate_session = _factory.openSession();
        Criteria empRegionCrit = hibernate_session.createCriteria(Regionmaster.class);
        empRegionCrit.add(Restrictions.sqlRestriction("defaultregion is true"));
        List empRegionList = empRegionCrit.list();
        if (empRegionList.size() > 0) {
            Regionmaster regionmasterObj = (Regionmaster) empRegionList.get(0);
            regionCode = regionmasterObj.getId();
        }
        hibernate_session.close();
        return regionCode;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map regionMap = new LinkedHashMap();
        Map yearMap = new LinkedHashMap();
        regionMap.put("0", "--Select--");
        yearMap.put("0", "--Select--");
        String regionid = "";
        String regionname = "";

        try {
            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.add(Restrictions.like("id", "R%"));
            rgnCrit.addOrder(Order.asc("regionname"));
            List<Regionmaster> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Regionmaster lbobj : rgnList) {
                regionid = lbobj.getId();
                regionname = lbobj.getRegionname();
                regionMap.put(regionid, regionname);
            }

            Criteria yearCrit = session.createCriteria(Budget.class);
            yearCrit.addOrder(Order.asc("id"));
            List<Budget> yearList = yearCrit.list();

            for (Budget lbobj : yearList) {
                yearMap.put(lbobj.getId(), lbobj.getStartyear() + "-" + lbobj.getEndyear());
            }

            resultMap.put("regionlist", regionMap);
            resultMap.put("yearlist", yearMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getBudgetLedgerList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        StringBuffer buffer = new StringBuffer();
        try {
            Criteria criteria = session.createCriteria(Ledgermaster.class);
            List ledgerlist = criteria.list();
            //System.out.println("Ledger List size = " + ledgerlist.size());
            if (ledgerlist.size() > 0) {
                buffer.append("<select ");
                buffer.append("class=\"combobox\" ");
                buffer.append("name=\"ledgerid\" id=\"ledgerid\">");
                buffer.append("<option value=\"");
                buffer.append("0");
                buffer.append("\">");
                buffer.append("-----------------Select----------------");
                buffer.append("</option>");
                buffer.append("<option value=\"");
                buffer.append("All");
                buffer.append("\">");
                buffer.append("All");
                buffer.append("</option>");
                for (int i = 0; i < ledgerlist.size(); i++) {
                    Ledgermaster ledgermaster = (Ledgermaster) ledgerlist.get(i);
                    buffer.append("<option value=\"");
                    buffer.append(ledgermaster.getId());
                    buffer.append("\">");
                    buffer.append(ledgermaster.getLedgername());
                    buffer.append("</option>");
                }
                buffer.append("</select>");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return buffer.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getRegionList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        StringBuffer buffer = new StringBuffer();
        try {
            Criteria criteria = session.createCriteria(Regionmaster.class);
            criteria.addOrder(Order.asc("regionname"));
            List rgnList = criteria.list();
            //System.out.println("Ledger List size = " + rgnList.size());
            if (rgnList.size() > 0) {
                buffer.append("<select ");
                buffer.append("class=\"combobox\" ");
                buffer.append("name=\"region\" id=\"region\">");
                buffer.append("<option value=\"");
                buffer.append("0");
                buffer.append("\">");
                buffer.append("All Region");
                buffer.append("</option>");
                for (int i = 0; i < rgnList.size(); i++) {
                    Regionmaster regionmaster = (Regionmaster) rgnList.get(i);
                    buffer.append("<option value=\"");
                    buffer.append(regionmaster.getId());
                    buffer.append("\">");
                    buffer.append(regionmaster.getRegionname());
                    buffer.append("</option>");
                }
                buffer.append("</select>");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return buffer.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map BudgetScheduleReportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String LedgerID, String startyear, String endyear) {
        //System.out.println("************************ BudgetServiceImpl classs BudgetScheduleReportPrintOut method is calling ***************************");
        Map resultMap = new HashMap();
        StringBuffer resultHTML = null;
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        String ledgerid = null;
        String ledgername = null;
        //System.out.println("LedgerID = " + LedgerID);
        if (LedgerID.equals("All")) {
            try {
                Criteria criteria = session.createCriteria(Ledgermaster.class);
                List list = criteria.list();
                if (list.size() > 0) {
                    resultHTML = new StringBuffer();
                    for (int j = 0; j < list.size(); j++) {
                        Ledgermaster ledgetObj = (Ledgermaster) list.get(j);
                        ledgerid = ledgetObj.getId();
                        ledgername = ledgetObj.getLedgername();
                        ReportModel reportModel = null;
                        BudgetScheduleReport budgetScheduleReport = new BudgetScheduleReport();
                        String budgetId = "";
                        String nextBudgetId = "";

                        int nextstartyear = Integer.parseInt(startyear) + 1;
                        int nextendyear = Integer.parseInt(endyear) + 1;
                        Criteria nextbudgetCrit = session.createCriteria(Budget.class);
                        nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
                        nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
                        nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
                        nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
                        List<Budget> nextbudgetList = nextbudgetCrit.list();
                        Budget nextbudgetObj = null;
                        if (nextbudgetList.size() > 0) {
                            nextbudgetObj = (Budget) nextbudgetList.get(0);
                            nextBudgetId = nextbudgetObj.getId();
                        }

                        Criteria budgetCrit = session.createCriteria(Budget.class);
                        budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
                        budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
                        budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
                        budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
                        List<Budget> budgetList = budgetCrit.list();
                        Budget budgetObj = null;
                        if (budgetList.size() > 0) {
                            budgetObj = (Budget) budgetList.get(0);
                            budgetId = budgetObj.getId();
                        }

                        Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
                        ledgerCrit.add(Restrictions.sqlRestriction("id='" + ledgerid + "'"));
                        List<Ledgermaster> ledgerlist = ledgerCrit.list();
                        if (ledgerlist.size() > 0) {
                            Ledgermaster ledgermaster = ledgerlist.get(0);
                            ledgername = ledgermaster.getLedgername();
                        }

                        int slipno = 1;

                        Criteria regionCrit = session.createCriteria(Regionmaster.class);
                        regionCrit.addOrder(Order.asc("id"));
                        List<Regionmaster> regionList = regionCrit.list();
                        if (regionList.size() > 0) {
                            for (int i = 0; i < regionList.size(); i++) {
                                reportModel = new ReportModel();
                                Regionmaster regionmasterObj = regionList.get(i);
                                reportModel.setRegionname(regionmasterObj.getRegionname());

                                Criteria ReCriteria = session.createCriteria(Budgetdetails.class);
                                ReCriteria.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                                ReCriteria.add(Restrictions.sqlRestriction("ledgermaster='" + ledgerid + "'"));
                                ReCriteria.add(Restrictions.sqlRestriction("regioncode='" + regionmasterObj.getId() + "'"));
                                List ReList = ReCriteria.list();
                                if (ReList.size() > 0) {
                                    Budgetdetails bd = (Budgetdetails) ReList.get(0);
                                    BigDecimal bigHobudget = bd.getHobudget();
                                    reportModel.setHorevisedestimate(decimalFormat.format(bigHobudget.doubleValue()));
                                } else {
                                    reportModel.setHorevisedestimate("0.00");
                                }

                                Criteria BeCriteria = session.createCriteria(Budgetdetails.class);
                                BeCriteria.add(Restrictions.sqlRestriction("budgetid='" + nextBudgetId + "'"));
                                BeCriteria.add(Restrictions.sqlRestriction("ledgermaster='" + ledgerid + "'"));
                                BeCriteria.add(Restrictions.sqlRestriction("regioncode='" + regionmasterObj.getId() + "'"));
                                List BeList = BeCriteria.list();
                                if (BeList.size() > 0) {
                                    Budgetdetails bd = (Budgetdetails) BeList.get(0);
                                    BigDecimal bigHorevisedbudget = bd.getHorevisedbudget();
                                    reportModel.setHobudgetestimate(decimalFormat.format(bigHorevisedbudget.doubleValue()));
                                } else {
                                    reportModel.setHobudgetestimate("0.00");
                                }

                                reportModel.setStartyear(Integer.valueOf(startyear));
                                reportModel.setEndyear(Integer.valueOf(endyear));
                                reportModel.setLedgername(ledgername);
                                reportModel.setSlipno(slipno);

                                budgetScheduleReport.getBudgetScheduleReportPrintWriter(reportModel, filePath);
                                slipno++;
                            }
                        }
                        budgetScheduleReport.budgetScheduleReportGrandTotal(filePath);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            ledgerid = LedgerID;
            resultHTML = new StringBuffer();
            ReportModel reportModel = null;
            BudgetScheduleReport budgetScheduleReport = new BudgetScheduleReport();
//        String ledgername = null;
//        System.out.println("start year" + startyear + "end year" + endyear);
//        System.out.println("ledgerid = " + ledgerid);
            String budgetId = "";
            String nextBudgetId = "";

            int nextstartyear = Integer.parseInt(startyear) + 1;
            int nextendyear = Integer.parseInt(endyear) + 1;
            Criteria nextbudgetCrit = session.createCriteria(Budget.class);
            nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
            List<Budget> nextbudgetList = nextbudgetCrit.list();
            Budget nextbudgetObj = null;
            if (nextbudgetList.size() > 0) {
                nextbudgetObj = (Budget) nextbudgetList.get(0);
                nextBudgetId = nextbudgetObj.getId();
            }

//        System.out.println("nextBudgetId = " + nextBudgetId);

            Criteria budgetCrit = session.createCriteria(Budget.class);
            budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
            budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
            List<Budget> budgetList = budgetCrit.list();
            Budget budgetObj = null;
            if (budgetList.size() > 0) {
                budgetObj = (Budget) budgetList.get(0);
                budgetId = budgetObj.getId();
            }

            Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
            ledgerCrit.add(Restrictions.sqlRestriction("id='" + ledgerid + "'"));
            List<Ledgermaster> ledgerlist = ledgerCrit.list();
            if (ledgerlist.size() > 0) {
                Ledgermaster ledgermaster = ledgerlist.get(0);
                ledgername = ledgermaster.getLedgername();
            }


            int slipno = 1;


            Criteria regionCrit = session.createCriteria(Regionmaster.class);
            regionCrit.addOrder(Order.asc("id"));
            List<Regionmaster> regionList = regionCrit.list();
            if (regionList.size() > 0) {
                for (int i = 0; i < regionList.size(); i++) {
                    reportModel = new ReportModel();
                    Regionmaster regionmasterObj = regionList.get(i);
                    reportModel.setRegionname(regionmasterObj.getRegionname());

//                System.out.println("budgetId = "+budgetId);
//                System.out.println("nextBudgetId = "+nextBudgetId);
//                System.out.println("ledgerid = "+ledgerid);
//                System.out.println("regioncode = "+regionmasterObj.getId());

                    Criteria ReCriteria = session.createCriteria(Budgetdetails.class);
                    ReCriteria.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                    ReCriteria.add(Restrictions.sqlRestriction("ledgermaster='" + ledgerid + "'"));
                    ReCriteria.add(Restrictions.sqlRestriction("regioncode='" + regionmasterObj.getId() + "'"));
                    List ReList = ReCriteria.list();
                    if (ReList.size() > 0) {
                        Budgetdetails bd = (Budgetdetails) ReList.get(0);
                        BigDecimal bigHobudget = bd.getHobudget();
                        reportModel.setHorevisedestimate(decimalFormat.format(bigHobudget.doubleValue()));
                    } else {
                        reportModel.setHorevisedestimate("0.00");
                    }

                    Criteria BeCriteria = session.createCriteria(Budgetdetails.class);
                    BeCriteria.add(Restrictions.sqlRestriction("budgetid='" + nextBudgetId + "'"));
                    BeCriteria.add(Restrictions.sqlRestriction("ledgermaster='" + ledgerid + "'"));
                    BeCriteria.add(Restrictions.sqlRestriction("regioncode='" + regionmasterObj.getId() + "'"));
                    List BeList = BeCriteria.list();
                    if (BeList.size() > 0) {
                        Budgetdetails bd = (Budgetdetails) BeList.get(0);
                        BigDecimal bigHorevisedbudget = bd.getHorevisedbudget();
                        reportModel.setHobudgetestimate(decimalFormat.format(bigHorevisedbudget.doubleValue()));
                    } else {
                        reportModel.setHobudgetestimate("0.00");
                    }

                    reportModel.setStartyear(Integer.valueOf(startyear));
                    reportModel.setEndyear(Integer.valueOf(endyear));
                    reportModel.setLedgername(ledgername);
                    reportModel.setSlipno(slipno);

//                System.out.println("************************************************************************");
//                System.out.println("reportModel.getStartyear() = "+reportModel.getStartyear());
//                System.out.println("reportModel.getEndyear() = "+reportModel.getEndyear());
//                System.out.println("reportModel.getRegionname() = "+reportModel.getRegionname());
//                System.out.println("reportModel.getHobudgetestimate() = "+reportModel.getHobudgetestimate());
//                System.out.println("reportModel.getHorevisedestimate() = "+reportModel.getHorevisedestimate());
//                System.out.println("reportModel.getLedgername() = "+reportModel.getLedgername());
//                System.out.println("reportModel.getSlipno() = "+reportModel.getSlipno());
//                System.out.println("************************************************************************");

                    budgetScheduleReport.getBudgetScheduleReportPrintWriter(reportModel, filePath);
                    slipno++;
                }
            }
            budgetScheduleReport.budgetScheduleReportGrandTotal(filePath);
        }



        resultMap.put("budgetdetails", resultHTML.toString());
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getBudgetUpdationDetailsDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String endyear) {
        Map resultMap = new HashMap();
        String budgetId = "";

        Criteria budgetCrit = session.createCriteria(Budget.class);
        budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
        budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
        List<Budget> budgetList = budgetCrit.list();
        Budget budgetObj = null;
        if (budgetList.size() > 0) {
            budgetObj = (Budget) budgetList.get(0);
            budgetId = budgetObj.getId();
        }


        StringBuffer stringBuff = new StringBuffer();
        stringBuff.append("<table id=\"myTable\" class=\"tablesorter\"> ");
        stringBuff.append("<tr  class=\"gridmenu\" > ");
        stringBuff.append("<th colspan=\"2\" >To be updated</th> ");
        stringBuff.append("</tr> ");
        stringBuff.append("<tr  class=\"gridmenu\" > ");
        stringBuff.append("<th>Sl.No</th> ");
        stringBuff.append("<th>Region Name</th> ");
        stringBuff.append("</tr> ");
        Criteria proceDetailsCrit = session.createCriteria(Regionmaster.class);
        List procesDetailsList = proceDetailsCrit.list();
        int j = 0;
        int k = procesDetailsList.size();
        resultMap.put("length", k);
        if (procesDetailsList.size() > 0) {
            for (int i = 0; i < k; i++) {
                Regionmaster regionmasterObj = (Regionmaster) procesDetailsList.get(i);
                String budget_Query = "select sum(revisedbudgetestimate) as amount from budgetdetails where budgetid= '" + budgetId + "' and regioncode='" + regionmasterObj.getId() + "'";
                SQLQuery budgetquery = session.createSQLQuery(budget_Query);
                List budgetlist = budgetquery.list();
                resultMap.put(j, regionmasterObj.getRegionname());
                if (budgetlist.get(0) != null) {
                    System.out.println(budget_Query);
                    System.out.println("data found" + budgetlist.get(0).toString());
                    resultMap.put(k + j, budgetlist.get(0));
                } else {
                    resultMap.put(k + j, 0);
                    stringBuff.append("   <tr  class=\"rowColor1\"> ");
                    stringBuff.append("  <td>" + (i + 1) + "</td> ");
                    stringBuff.append("  <td>" + regionmasterObj.getRegionname() + "</td> ");
                    stringBuff.append("   </tr> ");
                }
                j = j + 1;

            }
        }
        stringBuff.append(" </table> ");

        resultMap.put("tobecompleted", stringBuff.toString());
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getMonthExpenditure(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String budgetid, String region) {
        System.out.println("*************************** BudgetServiceImpl class getMonthExpenditure method is calling *********************");
        Map resultMap = new HashMap();
        try {
            String budgetId = "";
            String classname = "";
            String startyear = "";
            String endyear = "";

            String accperiod = "";
            if ("0".equalsIgnoreCase(region)) {
                LoggedInRegion = LoggedInRegion;
            } else {
                LoggedInRegion = region;
            }
            StringBuffer resultHTML = new StringBuffer();

            Criteria budgetCrit = session.createCriteria(Budget.class);
            budgetCrit.add(Restrictions.sqlRestriction("id='" + budgetid + "'"));
            List<Budget> budgetList = budgetCrit.list();
            Budget budgetObj = null;
            if (budgetList.size() > 0) {
                budgetObj = (Budget) budgetList.get(0);
                budgetId = budgetObj.getId();
                startyear = String.valueOf(budgetObj.getStartyear());
                endyear = String.valueOf(budgetObj.getEndyear());
                accperiod = startyear + " - " + endyear;
            }
            String ledgeryear = "b" + budgetId;
            //System.out.println("ledgeryear ::::::::::::::::" + ledgeryear);
            if (year.equalsIgnoreCase(startyear) || year.equalsIgnoreCase(endyear)) {
                if ((month.equalsIgnoreCase("1") && year.equalsIgnoreCase(startyear)) || (month.equalsIgnoreCase("2") && year.equalsIgnoreCase(startyear)) || (month.equalsIgnoreCase("3") && year.equalsIgnoreCase(startyear))) {
                    resultMap.put("ERROR", "Selected Expenditure Month & Year is not in Budget Period");
                } else {
                    if (budgetId.trim().length() > 0) {
                        Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
                        ledgerCrit.add(Restrictions.sqlRestriction(" " + ledgeryear + " is true "));
                        List<Ledgermaster> ledgerList = ledgerCrit.list();
                        if (ledgerList.size() > 0) {
                            resultHTML.append("<FONT SIZE=2>");
                            resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"budgettable\">");
                            resultHTML.append("<thead>");
                            resultHTML.append("<tr>");
                            resultHTML.append("<td>S.No</td>");
                            resultHTML.append("<td>Account Code</td>");
                            resultHTML.append("<td>Account Name</td>");
                            resultHTML.append("<td>Budget Estimated<br>" + startyear + "-" + endyear + "</td>");
                            resultHTML.append("<td>During the Month Expenditure</td>");
                            resultHTML.append("<td>Upto the Month Expenditure</td>");
                            resultHTML.append("<td>Balance</td>");
//                    resultHTML.append("<td>FMA</td>");
                            resultHTML.append("<td>Modify</td>");
                            resultHTML.append("</tr>");
                            resultHTML.append("</thead>");
                            resultHTML.append("<tbody>");

                            for (int i = 0; i < ledgerList.size(); i++) {
                                Ledgermaster ledgermasterObj = (Ledgermaster) ledgerList.get(i);

                                Criteria budgetDetailsCrit = session.createCriteria(Monthlyexpendituredetails.class);
                                budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
                                budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                                budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
                                budgetDetailsCrit.add(Restrictions.sqlRestriction("month='" + month + "'"));
                                budgetDetailsCrit.add(Restrictions.sqlRestriction("year='" + year + "'"));
                                List<Monthlyexpendituredetails> expendituredetailsList = budgetDetailsCrit.list();
                                Monthlyexpendituredetails monthlyExpendituredetailsObj;
                                if (expendituredetailsList.size() > 0) {
                                    monthlyExpendituredetailsObj = (Monthlyexpendituredetails) expendituredetailsList.get(0);
                                } else {
                                    monthlyExpendituredetailsObj = new Monthlyexpendituredetails();
                                    String id = getMaxExpendituredetailsid(session, LoggedInRegion);
                                    monthlyExpendituredetailsObj.setId(id);
                                    monthlyExpendituredetailsObj.setBudget(budgetObj);
                                    monthlyExpendituredetailsObj.setRegionmaster(getRegion(session, LoggedInRegion));
                                    monthlyExpendituredetailsObj.setLedgermaster(ledgermasterObj);
                                    monthlyExpendituredetailsObj.setEstimatedbudget(BigDecimal.ZERO);
                                    monthlyExpendituredetailsObj.setCurrentmonthbutget(BigDecimal.ZERO);
                                    monthlyExpendituredetailsObj.setBalance(BigDecimal.ZERO);
                                    monthlyExpendituredetailsObj.setFmaamount(BigDecimal.ZERO);
                                    monthlyExpendituredetailsObj.setMonth(Integer.parseInt(month));
                                    monthlyExpendituredetailsObj.setYear(Integer.parseInt(year));
                                    Transaction transaction;
                                    transaction = session.beginTransaction();
                                    session.save(monthlyExpendituredetailsObj);
                                    transaction.commit();
                                }
                                if (i % 2 == 0) {
                                    classname = "rowColor1";
                                } else {
                                    classname = "rowColor2";
                                }

                                BigDecimal firstactual = getBudget(session, LoggedInRegion, budgetid, ledgermasterObj.getId());
                                BigDecimal uptotheMonthExpenditure = getUptotheMonthExpenditure(session, LoggedInRegion, ledgermasterObj.getId(), startyear, month, year);
                                BigDecimal balance = firstactual.subtract(uptotheMonthExpenditure);


                                resultHTML.append("<tr class=\"" + classname + "\">");
                                resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                                resultHTML.append("<td align=\"left\">" + ledgermasterObj.getId() + "</td>");
                                resultHTML.append("<td align=\"left\">" + ledgermasterObj.getLedgername() + "</td>");
                                resultHTML.append("<td align=\"right\">" + firstactual + "</td>");
                                resultHTML.append("<td align=\"right\">" + monthlyExpendituredetailsObj.getCurrentmonthbutget() + "</td>");
                                resultHTML.append("<td align=\"right\">" + uptotheMonthExpenditure + "</td>");
                                resultHTML.append("<td align=\"right\">" + balance + "</td>");
//                        resultHTML.append("<td align=\"right\">" + monthlyExpendituredetailsObj.getFmaamount() + "</td>");
                                resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"loancode\" id=\"" + monthlyExpendituredetailsObj.getId() + "\" onclick=\"setExpenditureDetails('" + monthlyExpendituredetailsObj.getId() + "','" + ledgermasterObj.getId() + "','" + ledgermasterObj.getLedgername() + "','" + firstactual + "','" + accperiod + "','" + monthlyExpendituredetailsObj.getCurrentmonthbutget() + "','" + monthlyExpendituredetailsObj.getFmaamount() + "','" + uptotheMonthExpenditure + "','" + balance + "')\"></td>").append("</tr>");

                            }

                            resultHTML.append("</tbody>");
                            resultHTML.append("</table>");
                            resultHTML.append("</FONT>");
                        }
                    }
                    resultMap.put("budgetdetails", resultHTML.toString());
                }
            } else {
                resultMap.put("ERROR", "Selected Expenditure Month & Year is not in Budget Period");
            }
//            System.out.println(":::::::::::resultHTML.toString() = " + resultHTML.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    public synchronized String getMaxExpendituredetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getMonthlyexpenditureid();
//                System.out.println("maxStr = " + maxNoStr);
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setMonthlyexpenditureid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
//            System.out.println("BudgetID = " + maxStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return maxStr;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadBudgetYearDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map budgetMap = new LinkedHashMap();
        budgetMap.put("0", "--Select--");
        String id = "";
        String budgetYear = "";

        try {
            Criteria rgnCrit = session.createCriteria(Budget.class);
            rgnCrit.addOrder(Order.asc("id"));
            List<Budget> budgetList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Budget lbobj : budgetList) {
                id = lbobj.getId();
                budgetYear = lbobj.getStartyear() + " - " + lbobj.getEndyear();
                budgetMap.put(id, budgetYear);

            }
            resultMap.put("budgetlist", budgetMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public BigDecimal getUptotheMonthExpenditure(Session session, String regionCode, String ledgermaster, String startyear, String endmonth, String endyear) {
        BigDecimal totalExpenditure = new BigDecimal(0.00);
        String conditionStr = "";
        if (startyear.equalsIgnoreCase(endyear)) {
            conditionStr = "year=" + startyear + " and month between 4 and " + endmonth;
        } else {
            conditionStr = "((year=" + startyear + " and month between 4 and 12) or  (year=" + endyear + " and month between 1 and " + endmonth + "))";
        }
        Criteria monthlyexpendituredetailsCrit = session.createCriteria(Monthlyexpendituredetails.class);
        monthlyexpendituredetailsCrit.add(Restrictions.sqlRestriction(conditionStr));
        monthlyexpendituredetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + regionCode + "'"));
//        monthlyexpendituredetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
        monthlyexpendituredetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermaster + "'"));
        List<Monthlyexpendituredetails> monthlyexpendituredetailsList = monthlyexpendituredetailsCrit.list();
        if (monthlyexpendituredetailsList.size() > 0) {
            for (int i = 0; i < monthlyexpendituredetailsList.size(); i++) {
                Monthlyexpendituredetails monthlyexpendituredetailsObj = (Monthlyexpendituredetails) monthlyexpendituredetailsList.get(i);
                totalExpenditure = totalExpenditure.add(monthlyexpendituredetailsObj.getCurrentmonthbutget());
            }
        }
        return totalExpenditure;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveBudgetExpenditureDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String expendituredetailsid, String ledgerid, String monthexpenditure, String fmaamount, String region) {
        Map resultMap = new HashMap();
        try {
            Ledgermaster ledgermasterObj = getLedgetMaster(session, ledgerid);
            if ("0".equalsIgnoreCase(region)) {
                LoggedInRegion = LoggedInRegion;
            } else {
                LoggedInRegion = region;
            }
            Criteria expenditureDetailsCrit = session.createCriteria(Monthlyexpendituredetails.class);
            expenditureDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
            expenditureDetailsCrit.add(Restrictions.sqlRestriction("id='" + expendituredetailsid + "'"));
            expenditureDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
            List<Monthlyexpendituredetails> expendituredetailsList = expenditureDetailsCrit.list();
            Monthlyexpendituredetails monthlyExpendituredetailsObj;
            if (expendituredetailsList.size() > 0) {
                monthlyExpendituredetailsObj = (Monthlyexpendituredetails) expendituredetailsList.get(0);
                monthlyExpendituredetailsObj.setCurrentmonthbutget(new BigDecimal(monthexpenditure));
                monthlyExpendituredetailsObj.setFmaamount(new BigDecimal(fmaamount));
                Transaction transaction;
                transaction = session.beginTransaction();
                session.update(monthlyExpendituredetailsObj);
                transaction.commit();
            }

            resultMap.put("status", "Successfully Saved");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getBudgetForFMAProbable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String startyear, String endyear) {
        System.out.println("*************************** BudgetServiceImpl class getBudgetForRevision method is calling *********************");
        Map resultMap = new HashMap();
        try {
            String budgetId = "";
            String firstBudgetId = "";
            String secondBudgetId = "";
            String thirdBudgetId = "";
            String nextBudgetId = "";
            String classname = "";

            StringBuffer resultHTML = new StringBuffer();
            //System.out.println("start year" + startyear + "end year" + endyear);

            int firststartyear = Integer.parseInt(startyear) - 3;
            int firstendyear = Integer.parseInt(endyear) - 3;
            Criteria firstbudgetCrit = session.createCriteria(Budget.class);
            firstbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            firstbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + firststartyear));
            firstbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            firstbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + firstendyear));
            List<Budget> firstbudgetList = firstbudgetCrit.list();
            Budget firstbudgetObj = null;
            if (firstbudgetList.size() > 0) {
                firstbudgetObj = (Budget) firstbudgetList.get(0);
                firstBudgetId = firstbudgetObj.getId();
            }

            int secondstartyear = Integer.parseInt(startyear) - 2;
            int secondendyear = Integer.parseInt(endyear) - 2;
            Criteria secondbudgetCrit = session.createCriteria(Budget.class);
            secondbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            secondbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + secondstartyear));
            secondbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            secondbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + secondendyear));
            List<Budget> secondbudgetList = secondbudgetCrit.list();
            Budget secondbudgetObj = null;
            if (secondbudgetList.size() > 0) {
                secondbudgetObj = (Budget) secondbudgetList.get(0);
                secondBudgetId = secondbudgetObj.getId();
            }

            int thirdstartyear = Integer.parseInt(startyear) - 1;
            int thirdendyear = Integer.parseInt(endyear) - 1;
            Criteria thirdbudgetCrit = session.createCriteria(Budget.class);
            thirdbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + thirdstartyear));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + thirdendyear));
            List<Budget> thirdbudgetList = thirdbudgetCrit.list();
            Budget thirdbudgetObj = null;
            if (thirdbudgetList.size() > 0) {
                thirdbudgetObj = (Budget) thirdbudgetList.get(0);
                thirdBudgetId = thirdbudgetObj.getId();
            }

            int nextstartyear = Integer.parseInt(startyear) + 1;
            int nextendyear = Integer.parseInt(endyear) + 1;
            Criteria nextbudgetCrit = session.createCriteria(Budget.class);
            nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
            List<Budget> nextbudgetList = nextbudgetCrit.list();
            Budget nextbudgetObj = null;
            if (nextbudgetList.size() > 0) {
                nextbudgetObj = (Budget) nextbudgetList.get(0);
                nextBudgetId = nextbudgetObj.getId();
            }


            Criteria budgetCrit = session.createCriteria(Budget.class);
            budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
            budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
            List<Budget> budgetList = budgetCrit.list();
            Budget budgetObj = null;
            if (budgetList.size() > 0) {
                budgetObj = (Budget) budgetList.get(0);
                budgetId = budgetObj.getId();
            }
//            System.out.println("budgetId ::::::::::::::::::" + budgetId);
            String ledgeryear = "b" + budgetId;
//            System.out.println("ledgeryear ::::::::::::::::" + ledgeryear);

            if (budgetId.trim().length() > 0) {
                Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
                ledgerCrit.add(Restrictions.sqlRestriction(" " + ledgeryear + " is true "));
                List<Ledgermaster> ledgerList = ledgerCrit.list();
                if (ledgerList.size() > 0) {
                    resultHTML.append("<FONT SIZE=2>");
                    resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"budgettable\">");
                    resultHTML.append("<thead>");
                    resultHTML.append("<tr>");
//                resultHTML.append("<table width=\"95%\"  id=\"budgettable\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                resultHTML.append("<tr><td valign=\"top\">");
//                resultHTML.append("<table id=\"budgettable\" width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                resultHTML.append("<tr class=\"gridmenu\">");
                    resultHTML.append("<td>S.No</td>");
                    resultHTML.append("<td>Account</td>");
                    resultHTML.append("<td>" + firststartyear + "-" + firstendyear + "</td>");
                    resultHTML.append("<td>" + secondstartyear + "-" + secondendyear + "</td>");
                    resultHTML.append("<td>" + thirdstartyear + "-" + thirdendyear + "</td>");
                    resultHTML.append("<td>Average</td>");
                    resultHTML.append("<td>Budget Estimate<br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>Actual <br>Upto Sep</td>");
                    resultHTML.append("<td>Probable <br>oct to Mar</td>");
                    resultHTML.append("<td>Rev. Esti<br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>FMA<br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>Budget Estimate<br>" + endyear + "-" + (Integer.parseInt(endyear) + 1) + "</td>");
                    resultHTML.append("<td>Rev. Esti <br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>Rev. FMA<br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>Budget Estimate <br>" + endyear + "-" + (Integer.parseInt(endyear) + 1) + "</td>");
                    resultHTML.append("<td>Modify</td>");
                    resultHTML.append("</tr>");
                    resultHTML.append("</thead>");
                    resultHTML.append("<tbody>");

                    for (int i = 0; i < ledgerList.size(); i++) {
                        Ledgermaster ledgermasterObj = (Ledgermaster) ledgerList.get(i);

//                        System.out.println("LoggedInRegion = " + LoggedInRegion);
//                        System.out.println("budgetId = " + budgetId);
//                        System.out.println("ledgermasterObj.getId()  = " + ledgermasterObj.getId());

                        Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
                        List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
                        Budgetdetails budgetdetailsObj = null;
                        if (budgetdetailsList.size() > 0) {
                            budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);
                        } else {
                            budgetdetailsObj = new Budgetdetails();
                            String id = getMaxBudgetdetailsid(session, LoggedInRegion);
                            System.out.println("id ::::::::::::: " + id);
                            budgetdetailsObj.setId(id);
                            budgetdetailsObj.setBudget(budgetObj);
                            budgetdetailsObj.setRegionmaster(getRegion(session, LoggedInRegion));
                            budgetdetailsObj.setLedgermaster(ledgermasterObj);
                            budgetdetailsObj.setActual(BigDecimal.ZERO);
                            budgetdetailsObj.setBudgetestimate(BigDecimal.ZERO);
                            budgetdetailsObj.setRevisedbudgetestimate(BigDecimal.ZERO);
                            budgetdetailsObj.setActualoffirsthalfyesr(BigDecimal.ZERO);
                            budgetdetailsObj.setActualofsecondhalfyesr(BigDecimal.ZERO);
                            budgetdetailsObj.setProbableforsecondhalfyear(BigDecimal.ZERO);
                            budgetdetailsObj.setHobudget(BigDecimal.ZERO);
                            budgetdetailsObj.setHorevisedbudget(BigDecimal.ZERO);
                            budgetdetailsObj.setFma(BigDecimal.ZERO);
                            budgetdetailsObj.setHofma(BigDecimal.ZERO);


                            Transaction transaction;
                            transaction = session.beginTransaction();
                            session.save(budgetdetailsObj);
                            transaction.commit();
                        }
                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }

//                    System.out.println("firstBudgetId==="+firstBudgetId);
//                    System.out.println("secondBudgetId==="+secondBudgetId);
//                    System.out.println("thirdBudgetId==="+thirdBudgetId);
//                    System.out.println("nextBudgetId==="+nextBudgetId);
                        BigDecimal firstactual = getBudget(session, LoggedInRegion, firstBudgetId, ledgermasterObj.getId());
                        BigDecimal secondactual = getBudget(session, LoggedInRegion, secondBudgetId, ledgermasterObj.getId());
                        BigDecimal thirdactual = getBudget(session, LoggedInRegion, thirdBudgetId, ledgermasterObj.getId());

                        float average = 0;
                        if (firstactual.floatValue() != 0 || secondactual.floatValue() != 0 || thirdactual.floatValue() != 0) {
                            average = (firstactual.floatValue() + secondactual.floatValue() + thirdactual.floatValue()) / 3;
                        }
                        BigDecimal averageActual = StringtobigDecimal(String.valueOf(average));

                        BigDecimal budgetestimate = budgetdetailsObj.getBudgetestimate();
                        BigDecimal actualfirsthalf = budgetdetailsObj.getActualoffirsthalfyesr();
                        BigDecimal probablesecondhalf = budgetdetailsObj.getProbableforsecondhalfyear();
                        BigDecimal revisedestimate = budgetdetailsObj.getRevisedbudgetestimate();
                        BigDecimal budgetestimatenext = getBudget(session, LoggedInRegion, nextBudgetId, ledgermasterObj.getId());

                        BigDecimal budgetho = budgetdetailsObj.getHobudget();
                        BigDecimal fma = budgetdetailsObj.getFma();
                        BigDecimal hofma = budgetdetailsObj.getHofma();
//                    BigDecimal revisedestimateho = budgetdetailsObj.getHorevisedbudget();
                        BigDecimal revisedestimateho = getBudgetNextYearHORevisedEstimate(session, LoggedInRegion, nextBudgetId, ledgermasterObj.getId());



                        resultHTML.append("<tr class=\"" + classname + "\">");
                        resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                        resultHTML.append("<td align=\"left\">" + ledgermasterObj.getLedgername() + "</td>");
                        resultHTML.append("<td align=\"center\">" + firstactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + secondactual + "</td>");
                        resultHTML.append("<td align=\"center\">" + thirdactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + averageActual + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimate + "</td>");
//                    resultHTML.append("<td align=\"right\">" + revisedestimateho + "</td>");
                        resultHTML.append("<td align=\"right\">" + actualfirsthalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + probablesecondhalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimate + "</td>");
                        resultHTML.append("<td align=\"right\">" + fma + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimatenext + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetho + "</td>");
                        resultHTML.append("<td align=\"right\">" + hofma + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimateho + "</td>");
                        resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"loancode\" id=\"" + budgetdetailsObj.getId() + "\" onclick=\"setBudgetDetails('" + budgetdetailsObj.getId() + "','" + budgetdetailsObj.getLedgermaster().getId() + "','" + budgetdetailsObj.getLedgermaster().getLedgername() + "','" + fma + "','" + hofma + "')\"></td>").append("</tr>");
//                        resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"loancode\" id=\"" + budgetdetailsObj.getId() + "\" onclick=\"setBudgetDetails('" + budgetdetailsObj.getId() + "','" + budgetdetailsObj.getLedgermaster().getId() + "','" + budgetdetailsObj.getLedgermaster().getLedgername() + "','" + firstactual + "','" + secondactual + "','" + thirdactual + "','" + averageActual + "','" + budgetestimate + "','" + actualfirsthalf + "','" + probablesecondhalf + "','" + revisedestimate + "','" + budgetestimatenext + "','" + budgetho + "','" + revisedestimateho + "')\"></td>").append("</tr>");

                    }
//                resultHTML.append("<tr class=\"" + classname + "\">").append("<td colspan=\"12\" align=\"center\">" + "<input type=\"button\" class=\"submitbu\" name=\"show\" id=\"show\" value=\"Download\" onclick=\"downloadbudget('" + startyear + "','" + endyear + "')\">" + "</td>").append("</tr>");
                    resultHTML.append("</tbody>");
                    resultHTML.append("</table>");
                    resultHTML.append("</FONT>");
                }
            }
//            System.out.println(":::::::::::resultHTML.toString() = " + resultHTML.toString());
            resultMap.put("budgetdetails", resultHTML.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveProbableFMADetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String budgetdetailsid, String ledgerid, String ledgername, String fmaamt, String hofmaamt) {
        Map resultMap = new HashMap();
        try {
            Ledgermaster ledgermasterObj = getLedgetMaster(session, ledgerid);

            Criteria budgetDetails1Crit = session.createCriteria(Budgetdetails.class);
            budgetDetails1Crit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
            budgetDetails1Crit.add(Restrictions.sqlRestriction("id='" + budgetdetailsid + "'"));
            budgetDetails1Crit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
            List<Budgetdetails> budgetdetails1List = budgetDetails1Crit.list();
            Budgetdetails budgetdetails1Obj;
            if (budgetdetails1List.size() > 0) {
                budgetdetails1Obj = (Budgetdetails) budgetdetails1List.get(0);
                budgetdetails1Obj.setFma(new BigDecimal(fmaamt));
                //budgetdetails1Obj.setHofma(new BigDecimal(hofmaamt));                

                Transaction transaction;
                transaction = session.beginTransaction();
                session.update(budgetdetails1Obj);
                transaction.commit();

            }
            resultMap.put("status", "Successfully Saved");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map ControlofExpenditureReportPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePathwithName, String region, String smonth, String syear, String period) {
        System.out.println("********************* BudgetServiceImpl class ControlofExpenditureReportPrint method is calling *****************");
        Map map = new HashMap();
        try {
            ControlofExpenditureReport cer = new ControlofExpenditureReport();
            ReportModel rm = null;
            int pageno = 1;
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
//            System.out.println("smonth = " + smonth);
//            System.out.println("syear = " + syear);

            String EXPENDITUREQUERY = "select rm.regionname, b.startyear,b.endyear, lm.ledgername,lgm.ledgergroupname,"
                    //+ "bd.actual,med.currentmonthbutget, med.estimatedbudget, "
                    + "bd.hobudget,med.currentmonthbutget, med.estimatedbudget, "
                    + "med.balance, med.month, med.year, lm.id FROM monthlyexpendituredetails med "
                    + "left join budget b on b.id=med.budgetid "
                    + "left join ledgermaster lm on lm.id=med.ledgermaster "
                    + "left join ledgergroupmaster lgm on lgm.id=lm.ledgergroupid  "
                    + "left join regionmaster rm on rm.id=med.regioncode "
                    + "left join budgetdetails bd on bd.budgetid=b.id "
                    + "where "
                    + "med.regioncode='" + region + "' "
                    + "and med.month=" + Integer.valueOf(smonth) + " "
                    + "and med.year=" + Integer.valueOf(syear) + " "
                    + "and med.budgetid='" + period + "' "
                    + "and bd.regioncode='" + region + "' "
                    + "and bd.budgetid='" + period + "' "
                    + "and bd.ledgermaster=med.ledgermaster "
                    + "order by lm.ledgergroupid";

            SQLQuery expenditure_query = session.createSQLQuery(EXPENDITUREQUERY);
//            System.out.println("VOUCHER_QUERY ->" + VOUCHER_QUERY);
            if (expenditure_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = expenditure_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                rm = new ReportModel();
                Object[] rows = (Object[]) its.next();
                String regionname = (String) rows[0];
                String startyear = String.valueOf((Integer) rows[1]);
                String endyear = String.valueOf((Integer) rows[2]);
                String budgetperiod = startyear + "-" + endyear.substring(2, 4);
                String ledgername = (String) rows[3];
                String ledgergroupname = (String) rows[4];

                BigDecimal bigactual = (BigDecimal) rows[5];
                BigDecimal bigcurrent = (BigDecimal) rows[6];
                BigDecimal bigestimated = (BigDecimal) rows[7];
                BigDecimal bigbalance = (BigDecimal) rows[8];

                Double doubleactual = bigactual.doubleValue();
                Double doublecurrent = bigcurrent.doubleValue();
                Double doubleestimated = bigestimated.doubleValue();
                Double doublebalance = bigbalance.doubleValue();

                String actual = decimalFormat.format(doubleactual);
                String current = decimalFormat.format(doublecurrent);
                String estimated = decimalFormat.format(doubleestimated);
                String balance = decimalFormat.format(doublebalance);
                String bmonth = String.valueOf((Integer) rows[9]);
                String byear = String.valueOf((Integer) rows[10]);
//                System.out.println("bmonth = " + bmonth);
//                System.out.println("byear = " + byear);
                String ledgerid = (String) rows[11];
                String monthyear = months[(Integer.valueOf(bmonth)) - 1] + "\"" + byear;

                rm.setSlipno(pageno);
                rm.setRegionname(regionname);
                rm.setBudgetperiod(budgetperiod);
                rm.setLedgername(ledgername);
                rm.setLedgerid(ledgerid);
                rm.setLedgergroupname(ledgergroupname);
                rm.setActual(actual);
                rm.setCurrentbudget(current);
                BigDecimal bd = getUptotheMonthExpenditure(session, region, ledgerid, startyear, smonth, endyear);
                double doublebd = bd.doubleValue();
                rm.setUptothebudget(decimalFormat.format(doublebd));
                rm.setEstimatedbudget(estimated);
                rm.setBudgetbalance(balance);
                rm.setBudgetmonthandyear(monthyear);
                rm.setExcessexpense("0.00");
                cer.getControlofExpenditureReportPrintWriter(rm, filePathwithName);
                pageno++;
            }
            cer.ControlofExpenditureReportGrandTotal(filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getHOBudgetForFMARevision(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String startyear, String endyear) {
        System.out.println("*************************** BudgetServiceImpl class getHOBudgetForFMARevision method is calling *********************");
        Map resultMap = new HashMap();
        try {
            String budgetId = "";
            String firstBudgetId = "";
            String secondBudgetId = "";
            String thirdBudgetId = "";
            String nextBudgetId = "";
            String classname = "";

            StringBuffer resultHTML = new StringBuffer();
            //System.out.println("start year" + startyear + "end year" + endyear);

            int firststartyear = Integer.parseInt(startyear) - 3;
            int firstendyear = Integer.parseInt(endyear) - 3;
            Criteria firstbudgetCrit = session.createCriteria(Budget.class);
            firstbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            firstbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + firststartyear));
            firstbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            firstbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + firstendyear));
            List<Budget> firstbudgetList = firstbudgetCrit.list();
            Budget firstbudgetObj = null;


            if (firstbudgetList.size()
                    > 0) {
                firstbudgetObj = (Budget) firstbudgetList.get(0);
                firstBudgetId = firstbudgetObj.getId();
            }
            int secondstartyear = Integer.parseInt(startyear) - 2;
            int secondendyear = Integer.parseInt(endyear) - 2;
            Criteria secondbudgetCrit = session.createCriteria(Budget.class);

            secondbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            secondbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + secondstartyear));
            secondbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            secondbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + secondendyear));
            List<Budget> secondbudgetList = secondbudgetCrit.list();
            Budget secondbudgetObj = null;


            if (secondbudgetList.size()
                    > 0) {
                secondbudgetObj = (Budget) secondbudgetList.get(0);
                secondBudgetId = secondbudgetObj.getId();
            }
            int thirdstartyear = Integer.parseInt(startyear) - 1;
            int thirdendyear = Integer.parseInt(endyear) - 1;
            Criteria thirdbudgetCrit = session.createCriteria(Budget.class);

            thirdbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + thirdstartyear));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            thirdbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + thirdendyear));
            List<Budget> thirdbudgetList = thirdbudgetCrit.list();
            Budget thirdbudgetObj = null;


            if (thirdbudgetList.size()
                    > 0) {
                thirdbudgetObj = (Budget) thirdbudgetList.get(0);
                thirdBudgetId = thirdbudgetObj.getId();
            }
            int nextstartyear = Integer.parseInt(startyear) + 1;
            int nextendyear = Integer.parseInt(endyear) + 1;
            Criteria nextbudgetCrit = session.createCriteria(Budget.class);

            nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
            List<Budget> nextbudgetList = nextbudgetCrit.list();
            Budget nextbudgetObj = null;


            if (nextbudgetList.size()
                    > 0) {
                nextbudgetObj = (Budget) nextbudgetList.get(0);
                nextBudgetId = nextbudgetObj.getId();
            }
            Criteria budgetCrit = session.createCriteria(Budget.class);

            budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
            budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
            budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
            budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
            List<Budget> budgetList = budgetCrit.list();
            Budget budgetObj = null;


            if (budgetList.size()
                    > 0) {
                budgetObj = (Budget) budgetList.get(0);
                budgetId = budgetObj.getId();
            }
//            System.out.println("budgetId ::::::::::::::::::" + budgetId);
            String ledgeryear = "b" + budgetId;
//            System.out.println("ledgeryear ::::::::::::::::" + ledgeryear);


            if (budgetId.trim().length() > 0) {
                Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
                ledgerCrit.add(Restrictions.sqlRestriction(" " + ledgeryear + " is true "));
                List<Ledgermaster> ledgerList = ledgerCrit.list();
                if (ledgerList.size() > 0) {
                    resultHTML.append("<FONT SIZE=2>");
                    resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"budgettable\">");
                    resultHTML.append("<thead>");
                    resultHTML.append("<tr>");
//                resultHTML.append("<table width=\"95%\"  id=\"budgettable\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                resultHTML.append("<tr><td valign=\"top\">");
//                resultHTML.append("<table id=\"budgettable\" width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                resultHTML.append("<tr class=\"gridmenu\">");
                    resultHTML.append("<td>S.No</td>");
                    resultHTML.append("<td>Account</td>");
                    resultHTML.append("<td>" + firststartyear + "-" + firstendyear + "</td>");
                    resultHTML.append("<td>" + secondstartyear + "-" + secondendyear + "</td>");
                    resultHTML.append("<td>" + thirdstartyear + "-" + thirdendyear + "</td>");
                    resultHTML.append("<td>Average</td>");
                    resultHTML.append("<td>Budget Estimate<br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>Actual <br>Upto Sep</td>");
                    resultHTML.append("<td>Probable <br>oct to Mar</td>");
                    resultHTML.append("<td>Rev. Esti<br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>FMA<br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>Budget Estimate<br>" + endyear + "-" + (Integer.parseInt(endyear) + 1) + "</td>");
                    resultHTML.append("<td>Rev. Esti <br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>Rev. FMA<br>" + startyear + "-" + endyear + "</td>");
                    resultHTML.append("<td>Budget Estimate <br>" + endyear + "-" + (Integer.parseInt(endyear) + 1) + "</td>");
                    resultHTML.append("<td>Modify</td>");
                    resultHTML.append("</tr>");
                    resultHTML.append("</thead>");
                    resultHTML.append("<tbody>");

                    for (int i = 0; i < ledgerList.size(); i++) {
                        Ledgermaster ledgermasterObj = (Ledgermaster) ledgerList.get(i);

//                        System.out.println("LoggedInRegion = " + LoggedInRegion);
//                        System.out.println("budgetId = " + budgetId);
//                        System.out.println("ledgermasterObj.getId()  = " + ledgermasterObj.getId());

                        Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                        budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
                        List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
                        Budgetdetails budgetdetailsObj = null;
                        if (budgetdetailsList.size() > 0) {
                            budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);
                        } else {
                            budgetdetailsObj = new Budgetdetails();
                            String id = getMaxBudgetdetailsid(session, LoggedInRegion);
                            System.out.println("id ::::::::::::: " + id);
                            budgetdetailsObj.setId(id);
                            budgetdetailsObj.setBudget(budgetObj);
                            budgetdetailsObj.setRegionmaster(getRegion(session, LoggedInRegion));
                            budgetdetailsObj.setLedgermaster(ledgermasterObj);
                            budgetdetailsObj.setActual(BigDecimal.ZERO);
                            budgetdetailsObj.setBudgetestimate(BigDecimal.ZERO);
                            budgetdetailsObj.setRevisedbudgetestimate(BigDecimal.ZERO);
                            budgetdetailsObj.setActualoffirsthalfyesr(BigDecimal.ZERO);
                            budgetdetailsObj.setActualofsecondhalfyesr(BigDecimal.ZERO);
                            budgetdetailsObj.setProbableforsecondhalfyear(BigDecimal.ZERO);
                            budgetdetailsObj.setHobudget(BigDecimal.ZERO);
                            budgetdetailsObj.setHorevisedbudget(BigDecimal.ZERO);
                            budgetdetailsObj.setFma(BigDecimal.ZERO);
                            budgetdetailsObj.setHofma(BigDecimal.ZERO);


                            Transaction transaction;
                            transaction = session.beginTransaction();
                            session.save(budgetdetailsObj);
                            transaction.commit();
                        }
                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }

//                    System.out.println("firstBudgetId==="+firstBudgetId);
//                    System.out.println("secondBudgetId==="+secondBudgetId);
//                    System.out.println("thirdBudgetId==="+thirdBudgetId);
//                    System.out.println("nextBudgetId==="+nextBudgetId);
                        BigDecimal firstactual = getBudget(session, LoggedInRegion, firstBudgetId, ledgermasterObj.getId());
                        BigDecimal secondactual = getBudget(session, LoggedInRegion, secondBudgetId, ledgermasterObj.getId());
                        BigDecimal thirdactual = getBudget(session, LoggedInRegion, thirdBudgetId, ledgermasterObj.getId());

                        float average = 0;
                        if (firstactual.floatValue() != 0 || secondactual.floatValue() != 0 || thirdactual.floatValue() != 0) {
                            average = (firstactual.floatValue() + secondactual.floatValue() + thirdactual.floatValue()) / 3;
                        }
                        BigDecimal averageActual = StringtobigDecimal(String.valueOf(average));

                        BigDecimal budgetestimate = budgetdetailsObj.getBudgetestimate();
                        BigDecimal actualfirsthalf = budgetdetailsObj.getActualoffirsthalfyesr();
                        BigDecimal probablesecondhalf = budgetdetailsObj.getProbableforsecondhalfyear();
                        BigDecimal revisedestimate = budgetdetailsObj.getRevisedbudgetestimate();
                        BigDecimal budgetestimatenext = getBudget(session, LoggedInRegion, nextBudgetId, ledgermasterObj.getId());

                        BigDecimal budgetho = budgetdetailsObj.getHobudget();
                        BigDecimal fma = budgetdetailsObj.getFma();
                        BigDecimal hofma = budgetdetailsObj.getHofma();
//                    BigDecimal revisedestimateho = budgetdetailsObj.getHorevisedbudget();
                        BigDecimal revisedestimateho = getBudgetNextYearHORevisedEstimate(session, LoggedInRegion, nextBudgetId, ledgermasterObj.getId());



                        resultHTML.append("<tr class=\"" + classname + "\">");
                        resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                        resultHTML.append("<td align=\"left\">" + ledgermasterObj.getLedgername() + "</td>");
                        resultHTML.append("<td align=\"center\">" + firstactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + secondactual + "</td>");
                        resultHTML.append("<td align=\"center\">" + thirdactual + "</td>");
                        resultHTML.append("<td align=\"right\">" + averageActual + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimate + "</td>");
//                    resultHTML.append("<td align=\"right\">" + revisedestimateho + "</td>");
                        resultHTML.append("<td align=\"right\">" + actualfirsthalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + probablesecondhalf + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimate + "</td>");
                        resultHTML.append("<td align=\"right\">" + fma + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetestimatenext + "</td>");
                        resultHTML.append("<td align=\"right\">" + budgetho + "</td>");
                        resultHTML.append("<td align=\"right\">" + hofma + "</td>");
                        resultHTML.append("<td align=\"right\">" + revisedestimateho + "</td>");
                        resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"loancode\" id=\"" + budgetdetailsObj.getId() + "\" onclick=\"setBudgetDetails('" + budgetdetailsObj.getId() + "','" + budgetdetailsObj.getLedgermaster().getId() + "','" + budgetdetailsObj.getLedgermaster().getLedgername() + "','" + fma + "','" + hofma + "')\"></td>").append("</tr>");
//                        resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"loancode\" id=\"" + budgetdetailsObj.getId() + "\" onclick=\"setBudgetDetails('" + budgetdetailsObj.getId() + "','" + budgetdetailsObj.getLedgermaster().getId() + "','" + budgetdetailsObj.getLedgermaster().getLedgername() + "','" + firstactual + "','" + secondactual + "','" + thirdactual + "','" + averageActual + "','" + budgetestimate + "','" + actualfirsthalf + "','" + probablesecondhalf + "','" + revisedestimate + "','" + budgetestimatenext + "','" + budgetho + "','" + revisedestimateho + "')\"></td>").append("</tr>");

                    }
//                resultHTML.append("<tr class=\"" + classname + "\">").append("<td colspan=\"12\" align=\"center\">" + "<input type=\"button\" class=\"submitbu\" name=\"show\" id=\"show\" value=\"Download\" onclick=\"downloadbudget('" + startyear + "','" + endyear + "')\">" + "</td>").append("</tr>");
                    resultHTML.append("</tbody>");
                    resultHTML.append("</table>");
                    resultHTML.append("</FONT>");
                }
            }
//            System.out.println(":::::::::::resultHTML.toString() = " + resultHTML.toString());

            resultMap.put(
                    "budgetdetails", resultHTML.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveHORevisionFMADetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String budgetdetailsid, String ledgerid, String ledgername, String fmaamt, String hofmaamt) {
        Map resultMap = new HashMap();
        try {
            Ledgermaster ledgermasterObj = getLedgetMaster(session, ledgerid);

            Criteria budgetDetails1Crit = session.createCriteria(Budgetdetails.class);
            budgetDetails1Crit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
            budgetDetails1Crit.add(Restrictions.sqlRestriction("id='" + budgetdetailsid + "'"));
            budgetDetails1Crit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
            List<Budgetdetails> budgetdetails1List = budgetDetails1Crit.list();
            Budgetdetails budgetdetails1Obj;


            if (budgetdetails1List.size()
                    > 0) {
                budgetdetails1Obj = (Budgetdetails) budgetdetails1List.get(0);
                budgetdetails1Obj.setFma(new BigDecimal(fmaamt));
                budgetdetails1Obj.setHofma(new BigDecimal(hofmaamt));

                Transaction transaction;
                transaction = session.beginTransaction();
                session.update(budgetdetails1Obj);
                transaction.commit();

            }

            resultMap.put(
                    "status", "Successfully Saved");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map ControlofExpenditureAccountReportPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePathwithName, String ledgerid, String smonth, String syear, String period) {
        System.out.println("********************* BudgetServiceImpl class ControlofExpenditureAccountReportPrint method is calling *****************");
        Map map = new HashMap();
        try {
            ControlofExpenditureAccountReport cer = new ControlofExpenditureAccountReport();
            ReportModel rm = null;
            int pageno = 1;
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
//            System.out.println("smonth = " + smonth);
//            System.out.println("syear = " + syear);

            StringBuilder sb = new StringBuilder();

            sb.append("select rm.regionname, ");
            sb.append("case when b.startyear is null then (select startyear from budget where id='" + period + "') else b.startyear end as startyear, ");
            sb.append("case when b.endyear is null then (select endyear from budget where id='" + period + "') else b.endyear end as endyear,");
            sb.append("case when med.ledgermaster is null then '" + ledgerid + "' else med.ledgermaster end as ledgerid,");
            sb.append("case when lm.ledgername is null then (select ledgername from ledgermaster where id='" + ledgerid + "') else lm.ledgername end as ledgername, ");
            //sb.append("case when bd.actual is null then '0.00' else bd.actual end as actual, ");
            sb.append("case when bd.hobudget is null then '0.00' else bd.hobudget end as actual, ");
            sb.append("case when med.currentmonthbutget is null then '0.00' else med.currentmonthbutget end as currentmonthbudget, ");
            sb.append("case when med.estimatedbudget is null then '0.00' else med.estimatedbudget end as estimatedbudget, ");
            sb.append("case when med.balance is null then '0.00' else med.balance end as balance,");
            sb.append("case when med.month is null then " + Integer.valueOf(smonth) + " else med.month end as currentmonth,");
            sb.append("case when med.year is null then " + Integer.valueOf(syear) + " else med.year end as currentyear,");
            sb.append("case when b.id is null then '" + period + "' else b.id end as budgetid,");
            sb.append("rm.id   ");
            sb.append("from regionmaster  rm ");
            sb.append("left join monthlyexpendituredetails med on med.regioncode=rm.id ");
            sb.append("and med.month=" + Integer.valueOf(smonth) + " ");
            sb.append("and med.year=" + Integer.valueOf(syear) + " ");
            sb.append("and med.budgetid='" + period + "' ");
            sb.append("and med.ledgermaster='" + ledgerid + "' ");
            sb.append("left join budget b on med.budgetid=b.id ");
            sb.append("left join budgetdetails bd on bd.budgetid=b.id ");
            sb.append("and bd.regioncode=rm.id  ");
            sb.append("and bd.ledgermaster=med.ledgermaster ");
            sb.append("left join ledgermaster lm on lm.id=med.ledgermaster ");
            sb.append("order by rm.id");

            String EXPENDITUREQUERY = sb.toString();

//            String EXPENDITUREQUERY = "select rm.regionname, b.startyear,b.endyear, med.ledgermaster, lm.ledgername, bd.actual, med.currentmonthbutget,"
//                    + "med.estimatedbudget, med.balance, med.month, med.year, b.id as budgetid, rm.id   from regionmaster  rm "
//                    + "left join monthlyexpendituredetails med on med.regioncode=rm.id "
//                    + "and med.month=" + Integer.valueOf(smonth) + " "
//                    + "and med.year=" + Integer.valueOf(syear) + " "
//                    + "and med.budgetid='" + period + "' "
//                    + "and med.ledgermaster='" + ledgerid + "' "
//                    + "left join budget b on med.budgetid=b.id "
//                    + "left join budgetdetails bd on bd.budgetid=b.id "
//                    + "and bd.regioncode=rm.id  "
//                    + "and bd.ledgermaster=med.ledgermaster "
//                    + "left join ledgermaster lm on lm.id=med.ledgermaster "
//                    + "order by rm.id";

            SQLQuery expenditure_query = session.createSQLQuery(EXPENDITUREQUERY);
//            System.out.println("EXPENDITUREQUERY ->" + EXPENDITUREQUERY);
            if (expenditure_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = expenditure_query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                rm = new ReportModel();
                Object[] rows = (Object[]) its.next();
                String regionname = (String) rows[0];

                /*
                 * rm.regionname, b.startyear,b.endyear, med.ledgermaster, lm.ledgername, bd.actual, med.currentmonthbutget, med.estimatedbudget, "
                + "med.balance, med.fmaamount, med.month, b.id as budgetid
                 */
                String startyear = String.valueOf((Integer) rows[1]);
                String endyear = String.valueOf((Integer) rows[2]);
                String budgetperiod = startyear + "-" + endyear.substring(2, 4);
                String ledgerCode = (String) rows[3];
                String ledgername = (String) rows[4];

                String actual = "0.00";
                String current = "0.00";
                String estimated = "0.00";
                String balance = "0.00";
                if (rows[5] != null) {
                    BigDecimal bigactual = (BigDecimal) rows[5];
                    Double doubleactual = bigactual.doubleValue();
                    actual = decimalFormat.format(doubleactual);
                }
                if (rows[6] != null) {
                    BigDecimal bigcurrent = (BigDecimal) rows[6];
                    Double doublecurrent = bigcurrent.doubleValue();
                    current = decimalFormat.format(doublecurrent);
                }
                if (rows[7] != null) {
                    BigDecimal bigestimated = (BigDecimal) rows[7];
                    Double doubleestimated = bigestimated.doubleValue();
                    estimated = decimalFormat.format(doubleestimated);
                }
                if (rows[8] != null) {
                    BigDecimal bigbalance = (BigDecimal) rows[8];
                    Double doublebalance = bigbalance.doubleValue();
                    balance = decimalFormat.format(doublebalance);
                }
                String bmonth = String.valueOf((Integer) rows[9]);
                String byear = String.valueOf((Integer) rows[10]);
                String budgetid = (String) rows[11];
                String monthyear = months[(Integer.valueOf(bmonth)) - 1] + "\"" + byear;
                String regionid = (String) rows[12];

                rm.setSlipno(pageno);
                rm.setRegionname(regionname);
                rm.setBudgetperiod(budgetperiod);
                rm.setLedgername(ledgername);
                rm.setLedgerid(ledgerid);
                rm.setActual(actual);
                rm.setCurrentbudget(current);
                BigDecimal bd = new BigDecimal(0.00);
                bd = getUptotheMonthExpenditure(session, regionid, ledgerid, startyear, smonth, endyear);
                double doublebd = bd.doubleValue();
                rm.setUptothebudget(decimalFormat.format(doublebd));
                rm.setEstimatedbudget(estimated);
                Double balanceamont = (Double.valueOf(actual) - Double.valueOf(decimalFormat.format(doublebd)));
                if (balanceamont >= 0) {
                    rm.setBudgetbalance(decimalFormat.format(balanceamont));
                    rm.setExcessexpense("0.00");
                } else {
                    rm.setBudgetbalance("0.00");
                    rm.setExcessexpense(decimalFormat.format(balanceamont * -1));
                }
                //rm.setBudgetbalance(balance);
                rm.setBudgetmonthandyear(monthyear);
                //rm.setExcessexpense("0.00");
                cer.getControlofExpenditureAccountReportPrintWriter(rm, filePathwithName);
                pageno++;
            }
            cer.ControlofExpenditureAccountReportGrandTotal(filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map BudgetFMAReportPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String region, String startyear, String endyear) {
        String budgetId = "";
        String firstBudgetId = "";
        String secondBudgetId = "";
        String thirdBudgetId = "";
        String nextBudgetId = "";
        String classname = "";
        Map resultMap = new HashMap();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        ReportModel reportModel = null;
        BudgetFMAReport budgetFMAReport = new BudgetFMAReport();
        //System.out.println("start year" + startyear + "end year" + endyear);

        int firststartyear = Integer.parseInt(startyear) - 3;
        int firstendyear = Integer.parseInt(endyear) - 3;
        Criteria firstbudgetCrit = session.createCriteria(Budget.class);
        firstbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        firstbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + firststartyear));
        firstbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        firstbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + firstendyear));
        List<Budget> firstbudgetList = firstbudgetCrit.list();
        Budget firstbudgetObj = null;
        if (firstbudgetList.size() > 0) {
            firstbudgetObj = (Budget) firstbudgetList.get(0);
            firstBudgetId = firstbudgetObj.getId();
        }

        int secondstartyear = Integer.parseInt(startyear) - 2;
        int secondendyear = Integer.parseInt(endyear) - 2;
        Criteria secondbudgetCrit = session.createCriteria(Budget.class);
        secondbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        secondbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + secondstartyear));
        secondbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        secondbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + secondendyear));
        List<Budget> secondbudgetList = secondbudgetCrit.list();
        Budget secondbudgetObj = null;
        if (secondbudgetList.size() > 0) {
            secondbudgetObj = (Budget) secondbudgetList.get(0);
            secondBudgetId = secondbudgetObj.getId();
        }

        int thirdstartyear = Integer.parseInt(startyear) - 1;
        int thirdendyear = Integer.parseInt(endyear) - 1;
        Criteria thirdbudgetCrit = session.createCriteria(Budget.class);
        thirdbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + thirdstartyear));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        thirdbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + thirdendyear));
        List<Budget> thirdbudgetList = thirdbudgetCrit.list();
        Budget thirdbudgetObj = null;
        if (thirdbudgetList.size() > 0) {
            thirdbudgetObj = (Budget) thirdbudgetList.get(0);
            thirdBudgetId = thirdbudgetObj.getId();
        }

        int nextstartyear = Integer.parseInt(startyear) + 1;
        int nextendyear = Integer.parseInt(endyear) + 1;
        Criteria nextbudgetCrit = session.createCriteria(Budget.class);
        nextbudgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("startyear=" + nextstartyear));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        nextbudgetCrit.add(Restrictions.sqlRestriction("endyear=" + nextendyear));
        List<Budget> nextbudgetList = nextbudgetCrit.list();
        Budget nextbudgetObj = null;
        if (nextbudgetList.size() > 0) {
            nextbudgetObj = (Budget) nextbudgetList.get(0);
            nextBudgetId = nextbudgetObj.getId();
        }


        Criteria budgetCrit = session.createCriteria(Budget.class);
        budgetCrit.add(Restrictions.sqlRestriction("startmonth=4"));
        budgetCrit.add(Restrictions.sqlRestriction("startyear=" + startyear));
        budgetCrit.add(Restrictions.sqlRestriction("endmonth=3"));
        budgetCrit.add(Restrictions.sqlRestriction("endyear=" + endyear));
        List<Budget> budgetList = budgetCrit.list();
        Budget budgetObj = null;
        if (budgetList.size() > 0) {
            budgetObj = (Budget) budgetList.get(0);
            budgetId = budgetObj.getId();
        }
        String ledgeryear = "b" + budgetId;
        if (budgetId.trim().length() > 0) {
            Criteria ledgerCrit = session.createCriteria(Ledgermaster.class);
            ledgerCrit.add(Restrictions.sqlRestriction(" " + ledgeryear + " is true "));
            List<Ledgermaster> ledgerList = ledgerCrit.list();
            if (ledgerList.size() > 0) {


                int slipno = 1;
                for (int i = 0; i < ledgerList.size(); i++) {
                    Ledgermaster ledgermasterObj = (Ledgermaster) ledgerList.get(i);

                    Criteria budgetDetailsCrit = session.createCriteria(Budgetdetails.class);
                    budgetDetailsCrit.add(Restrictions.sqlRestriction("regioncode='" + region + "'"));
                    budgetDetailsCrit.add(Restrictions.sqlRestriction("budgetid='" + budgetId + "'"));
                    budgetDetailsCrit.add(Restrictions.sqlRestriction("ledgermaster='" + ledgermasterObj.getId() + "'"));
                    List<Budgetdetails> budgetdetailsList = budgetDetailsCrit.list();
                    Budgetdetails budgetdetailsObj = null;
                    if (budgetdetailsList.size() > 0) {
                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }

                        budgetdetailsObj = (Budgetdetails) budgetdetailsList.get(0);

                        BigDecimal firstactual = getBudget(session, region, firstBudgetId, ledgermasterObj.getId());
                        BigDecimal secondactual = getBudget(session, region, secondBudgetId, ledgermasterObj.getId());
                        BigDecimal thirdactual = getBudget(session, region, thirdBudgetId, ledgermasterObj.getId());

                        float average = 0;
                        if (firstactual.floatValue() != 0 || secondactual.floatValue() != 0 || thirdactual.floatValue() != 0) {
                            average = (firstactual.floatValue() + secondactual.floatValue() + thirdactual.floatValue()) / 3;
                        }

                        BigDecimal averageActual = StringtobigDecimal(String.valueOf(average));

                        BigDecimal budgetestimate = budgetdetailsObj.getBudgetestimate();
                        BigDecimal actualfirsthalf = budgetdetailsObj.getActualoffirsthalfyesr();
                        BigDecimal probablesecondhalf = budgetdetailsObj.getProbableforsecondhalfyear();
                        BigDecimal revisedestimate = budgetdetailsObj.getRevisedbudgetestimate();

                        BigDecimal budgetestimatenext = getBudget(session, region, nextBudgetId, ledgermasterObj.getId());
                        BigDecimal budgetho = budgetdetailsObj.getHobudget();
                        BigDecimal revisedestimateho = getBudgetNextYearHORevisedEstimate(session, region, nextBudgetId, ledgermasterObj.getId());


                        BigDecimal fma = budgetdetailsObj.getFma();
                        BigDecimal hoFma = budgetdetailsObj.getHofma();
//                        System.out.println("iddddddd=====" + budgetdetailsObj.getId());



                        String ledgergroupname = ledgermasterObj.getLedgergroupmaster().getLedgergroupname();
                        reportModel = new ReportModel();

                        //System.out.println("startyear = "+startyear);
                        //System.out.println("endyear = "+endyear);

                        reportModel.setStartyear(Integer.valueOf(startyear));
                        reportModel.setEndyear(Integer.valueOf(endyear));

                        reportModel.setSlipno(slipno);
                        reportModel.setLedgername(ledgermasterObj.getLedgername());
                        reportModel.setFirstactual(firstactual.toString());
                        reportModel.setSecondactual(secondactual.toString());
                        reportModel.setThirdactual(thirdactual.toString());
                        reportModel.setAverage(String.valueOf(decimalFormat.format(average)));
                        reportModel.setBudgetestimate(budgetestimate.toString());
                        reportModel.setActualfirsthalf(actualfirsthalf.toString());
                        reportModel.setProbablesecondhalf(probablesecondhalf.toString());
                        reportModel.setRevisedestimate(revisedestimate.toString());
                        reportModel.setBudgetestimatenext(budgetestimatenext.toString());
                        reportModel.setLedgergroupname(ledgergroupname);
                        reportModel.setFirststartyear(String.valueOf(firststartyear));
                        reportModel.setFirstendyear(String.valueOf(firstendyear));
                        reportModel.setSecondstartyear(String.valueOf(secondstartyear));
                        reportModel.setSecondendyear(String.valueOf(secondendyear));
                        reportModel.setThirdstartyear(String.valueOf(thirdstartyear));
                        reportModel.setThirdendyear(String.valueOf(thirdendyear));
                        reportModel.setHorevisedestimate(String.valueOf(budgetho));
                        reportModel.setHobudgetestimate(String.valueOf(revisedestimateho));
                        reportModel.setRegionname(getMyRegionName(session, region));
                        reportModel.setFma(String.valueOf(fma));
                        reportModel.setHofma(String.valueOf(hoFma));

                        budgetFMAReport.getBudgetFMAReportPrintWriter(reportModel, filePath);
                        slipno++;
                    }
                }
//                resultHTML.append("<tr class=\"" + classname + "\">")
//                        .append("<td colspan=\"12\" align=\"center\">" + "<input type=\"button\" class=\"submitbu\" name=\"show\" id=\"show\" value=\"Download\" onclick=\"downloadbudget('" + startyear + "','" + endyear + "')\">" + "</td>")
//                        .append("</tr>");
                budgetFMAReport.budgetReportGrandTotal(filePath);
            }
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map controlOfEpenditure(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountingperiod, String accountingperiodfrom, String accountingperiodto, String filePathwithName) {
        Map map = new HashMap();
        float allotmenttradingexpensestot = 0;
        float allotmentestablishmentchargestot = 0;
        float allotmentadminexpensestot = 0;
        float allotmentmaintenanceexpensestot = 0;
        float allotmentmiscexpensestot = 0;
        float allotmentstaffadvancestot = 0;
        float actualtradingexpensestot = 0;
        float actualestablishmentchargestot = 0;
        float actualadminexpensestot = 0;
        float actualmaintenanceexpensestot = 0;
        float actualmiscexpensestot = 0;
        float actualstaffadvancestot = 0;
        float tradingexpensespertot = 0;
        float establishmentchargespertot = 0;
        float adminexpensespertot = 0;
        float maintenanceexpensespertot = 0;
        float miscexpensespertot = 0;
        float staffadvancespertot = 0;
        BigDecimal allotmenttradingexpenses;
        BigDecimal actualtradingexpenses;
        float tradingexpensesper;

        BigDecimal allotmentestablishmentcharges;
        BigDecimal actualestablishmentcharges;
        float establishmentchargesper;

        BigDecimal allotmentadminexpenses;
        BigDecimal actualadminexpenses;
        float adminexpensesper;

        BigDecimal allotmentmaintenanceexpenses;
        BigDecimal actualmaintenanceexpenses;
        float maintenanceexpensesper;

        BigDecimal allotmentmiscexpenses;
        BigDecimal actualmiscexpenses;
        float miscexpensesper;

        BigDecimal allotmentstaffadvances;
        BigDecimal actualstaffadvances;
        float staffadvancesper;

        String verline = "|";
        int n = 204;
        char[] chars = new char[n];
        Arrays.fill(chars, '-');
        String dotline = new String(chars);
        String dotline2 = new String("|-----|----------------|----------|----------|-------|----------|----------|-------|----------|----------|-------|----------|----------|-------|----------|----------|-------|----------|----------|-------|");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePathwithName);
        } catch (IOException ex) {
            Logger.getLogger(BudgetServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder query = new StringBuilder();
        query.append(" select actual.regioncode, actual.regionname, ");
        query.append(" allotmenttradingexpenses, actualtradingexpenses,   ");
        query.append(" allotmentestablishmentcharges,actualestablishmentcharges, ");
        query.append(" allotmentadminexpenses, actualadminexpenses, ");
        query.append(" allotmentmaintenanceexpenses, actualmaintenanceexpenses, ");
        query.append(" allotmentmiscexpenses,actualmiscexpenses, ");
        query.append(" allotmentstaffadvances,actualstaffadvances ");
        query.append(" from (select mexp.regioncode as regioncode,rm.regionname as regionname, ");
        query.append(" sum(case when led.ledgergroupid='M01' then mexp.currentmonthbutget end) as actualtradingexpenses,  ");
        query.append(" sum(case when led.ledgergroupid='M02' then mexp.currentmonthbutget end) as actualestablishmentcharges, ");
        query.append(" sum(case when led.ledgergroupid='M03' then mexp.currentmonthbutget end) as actualadminexpenses, ");
        query.append(" sum(case when led.ledgergroupid='M04' then mexp.currentmonthbutget end) as actualmaintenanceexpenses, ");
        query.append(" sum(case when led.ledgergroupid='M05' then mexp.currentmonthbutget end) as actualmiscexpenses, ");
        query.append(" sum(case when led.ledgergroupid='M08' then mexp.currentmonthbutget end) as actualstaffadvances ");
        query.append(" from monthlyexpendituredetails mexp ");
        query.append(" left join ledgermaster led on led.id=mexp.ledgermaster  ");
        query.append(" left join regionmaster rm on rm.id=mexp.regioncode ");
        query.append(" where mexp.budgetid='" + accountingperiod + "' ");
        query.append(" group by mexp.regioncode,rm.regionname order by regioncode ) as actual ");

        query.append(" left join ");

        query.append(" (select bd.regioncode,rm.regionname,  ");
        query.append(" sum(case when led.ledgergroupid='M01' then bd.hobudget end) as allotmenttradingexpenses,  ");
        query.append(" sum(case when led.ledgergroupid='M02' then bd.hobudget end) as allotmentestablishmentcharges, ");
        query.append(" sum(case when led.ledgergroupid='M03' then bd.hobudget end) as allotmentadminexpenses, ");
        query.append(" sum(case when led.ledgergroupid='M04' then bd.hobudget end) as allotmentmaintenanceexpenses, ");
        query.append(" sum(case when led.ledgergroupid='M05' then bd.hobudget end) as allotmentmiscexpenses, ");
        query.append(" sum(case when led.ledgergroupid='M08' then bd.hobudget end) as allotmentstaffadvances ");
        query.append(" from budgetdetails bd ");
        query.append(" left join ledgermaster led on led.id=bd.ledgermaster  ");
        query.append(" left join regionmaster rm on rm.id=bd.regioncode ");
        query.append(" where bd.budgetid='" + accountingperiod + "'");
        query.append(" group by bd.regioncode,rm.regionname order by regioncode)  as allotment ");

        query.append(" ON actual.regioncode=allotment.regioncode ");

        System.out.println("query" + query);
        SQLQuery trialbalancequery = session.createSQLQuery(query.toString());
        try {
            fileWriter.append("TAMIL NADU CIVIL SUPPLIES CORPORATION HEAD OFFICE");
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Control of expenditure fot the period " + accountingperiodfrom);
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(dotline);
            fileWriter.append(NEW_LINE_SEPARATOR);

            String formatted = String.format("%s%5s%s%16s%s"
                    + "%29s%s"
                    + "%29s%s"
                    + "%29s%s"
                    + "%29s%s"
                    + "%29s%s"
                    + "%29s%s",
                    verline, " ", verline, " ",
                    verline, "TRADING EXPENSES",
                    verline, "ESTABLISHMENT CHARGES",
                    verline, "ADMINISTRATIVE EXPENSES",
                    verline, "MAINTENANCE & REPAIRS",
                    verline, "MISCEALANEOUES",
                    verline, "STAFF ADVANCES", verline);
            fileWriter.append(formatted);
            fileWriter.append(NEW_LINE_SEPARATOR);

            formatted = String.format("%s%5s%s%16s%s"
                    + "%10s%s%10s%s%7s%s"
                    + "%10s%s%10s%s%7s%s"
                    + "%10s%s%10s%s%7s%s"
                    + "%10s%s%10s%s%7s%s"
                    + "%10s%s%10s%s%7s%s"
                    + "%10s%s%10s%s%7s%s",
                    verline, "SL.No", verline, "REGION NAME",
                    verline, "ALLOTMENT", verline, "ACTUAL", verline, "IN %",
                    verline, "ALLOTMENT", verline, "ACTUAL", verline, "IN %",
                    verline, "ALLOTMENT", verline, "ACTUAL", verline, "IN %",
                    verline, "ALLOTMENT", verline, "ACTUAL", verline, "IN %",
                    verline, "ALLOTMENT", verline, "ACTUAL", verline, "IN %",
                    verline, "ALLOTMENT", verline, "ACTUAL", verline, "IN %", verline);
            fileWriter.append(formatted);
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(dotline);
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (IOException ex) {
            Logger.getLogger(BudgetServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            int i = 0;
            for (ListIterator ite = trialbalancequery.list().listIterator(); ite.hasNext();) {
                i = i + 1;
                Object[] obj = (Object[]) ite.next();

                String groupcode = (String) obj[0];
                String accountcode = (String) obj[1];

                if (obj[2] != null) {
                    allotmenttradingexpenses = (BigDecimal) obj[2];
                } else {
                    allotmenttradingexpenses = new BigDecimal("0.00");
                }

                if (obj[3] != null) {
                    actualtradingexpenses = (BigDecimal) obj[3];
                } else {
                    actualtradingexpenses = new BigDecimal("0.00");
                }
                tradingexpensesper = getPercentage(allotmenttradingexpenses, actualtradingexpenses);

                if (obj[4] != null) {
                    allotmentestablishmentcharges = (BigDecimal) obj[4];
                } else {
                    allotmentestablishmentcharges = new BigDecimal("0.00");
                }
                if (obj[5] != null) {
                    actualestablishmentcharges = (BigDecimal) obj[5];
                } else {
                    actualestablishmentcharges = new BigDecimal("0.00");
                }
                establishmentchargesper = getPercentage(allotmentestablishmentcharges, actualestablishmentcharges);


                if (obj[6] != null) {
                    allotmentadminexpenses = (BigDecimal) obj[6];
                } else {
                    allotmentadminexpenses = new BigDecimal("0.00");
                }
                if (obj[7] != null) {
                    actualadminexpenses = (BigDecimal) obj[7];
                } else {
                    actualadminexpenses = new BigDecimal("0.00");
                }
                adminexpensesper = getPercentage(allotmentadminexpenses, actualadminexpenses);

                if (obj[8] != null) {
                    allotmentmaintenanceexpenses = (BigDecimal) obj[8];
                } else {
                    allotmentmaintenanceexpenses = new BigDecimal("0.00");
                }
                if (obj[9] != null) {
                    actualmaintenanceexpenses = (BigDecimal) obj[9];
                } else {
                    actualmaintenanceexpenses = new BigDecimal("0.00");
                }
                maintenanceexpensesper = getPercentage(allotmentmaintenanceexpenses, actualmaintenanceexpenses);

                if (obj[10] != null) {
                    allotmentmiscexpenses = (BigDecimal) obj[10];
                } else {
                    allotmentmiscexpenses = new BigDecimal("0.00");
                }
                if (obj[11] != null) {
                    actualmiscexpenses = (BigDecimal) obj[11];
                } else {
                    actualmiscexpenses = new BigDecimal("0.00");
                }
                miscexpensesper = getPercentage(allotmentmiscexpenses, actualmiscexpenses);

                if (obj[12] != null) {
                    allotmentstaffadvances = (BigDecimal) obj[12];
                } else {
                    allotmentstaffadvances = new BigDecimal("0.00");
                }
                if (obj[13] != null) {
                    actualstaffadvances = (BigDecimal) obj[13];
                } else {
                    actualstaffadvances = new BigDecimal("0.00");
                }
                staffadvancesper = getPercentage(allotmentstaffadvances, actualstaffadvances);

                String formatted = String.format("%s%5s%s%16s%s"
                        + "%10s%s%10s%s%7s%s"
                        + "%10s%s%10s%s%7s%s"
                        + "%10s%s%10s%s%7s%s"
                        + "%10s%s%10s%s%7s%s"
                        + "%10s%s%10s%s%7s%s"
                        + "%10s%s%10s%s%7s%s",
                        verline, i, verline, accountcode,
                        verline, allotmenttradingexpenses, verline, actualtradingexpenses, verline, tradingexpensesper > 100 ? String.format("%-7s", tradingexpensesper) : String.format("%7s", tradingexpensesper),
                        verline, allotmentestablishmentcharges, verline, actualestablishmentcharges, verline, establishmentchargesper > 100 ? String.format("%-7s", establishmentchargesper) : String.format("%7s", establishmentchargesper),
                        verline, allotmentadminexpenses, verline, actualadminexpenses, verline, adminexpensesper > 100 ? String.format("%-7s", adminexpensesper) : String.format("%7s", adminexpensesper),
                        verline, allotmentmaintenanceexpenses, verline, actualmaintenanceexpenses, verline, maintenanceexpensesper > 100 ? String.format("%-7s", maintenanceexpensesper) : String.format("%7s", maintenanceexpensesper),
                        verline, allotmentmiscexpenses, verline, actualmiscexpenses, verline, miscexpensesper > 100 ? String.format("%-7s", miscexpensesper) : String.format("%7s", miscexpensesper),
                        verline, allotmentstaffadvances, verline, actualstaffadvances, verline, staffadvancesper > 100 ? String.format("%-7s", staffadvancesper) : String.format("%7s", staffadvancesper), verline);
                fileWriter.append(formatted);
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(dotline2);
                fileWriter.append(NEW_LINE_SEPARATOR);
                allotmenttradingexpensestot = allotmenttradingexpensestot + allotmenttradingexpenses.floatValue();
                allotmentestablishmentchargestot = allotmentestablishmentchargestot + allotmentestablishmentcharges.floatValue();
                allotmentadminexpensestot = allotmentadminexpensestot + allotmentadminexpenses.floatValue();
                allotmentmaintenanceexpensestot = allotmentmaintenanceexpensestot + allotmentmaintenanceexpenses.floatValue();
                allotmentmiscexpensestot = allotmentmiscexpensestot + allotmentmiscexpenses.floatValue();
                allotmentstaffadvancestot = allotmentstaffadvancestot + allotmentstaffadvances.floatValue();
                actualtradingexpensestot = actualtradingexpensestot + actualtradingexpenses.floatValue();
                actualestablishmentchargestot = actualestablishmentchargestot + actualestablishmentcharges.floatValue();
                actualadminexpensestot = actualadminexpensestot + actualadminexpenses.floatValue();
                actualmaintenanceexpensestot = actualmaintenanceexpensestot + actualmaintenanceexpenses.floatValue();
                actualmiscexpensestot = actualmiscexpensestot + actualmiscexpenses.floatValue();
                actualstaffadvancestot = actualstaffadvancestot + actualstaffadvances.floatValue();
                tradingexpensespertot = tradingexpensespertot + tradingexpensesper;
                establishmentchargespertot = establishmentchargespertot + establishmentchargesper;
                adminexpensespertot = adminexpensespertot + adminexpensesper;
                maintenanceexpensespertot = maintenanceexpensespertot + maintenanceexpensesper;
                miscexpensespertot = miscexpensespertot + miscexpensesper;
                staffadvancespertot = staffadvancespertot + staffadvancesper;

            }
//            fileWriter.append(dotline);
//            fileWriter.append(NEW_LINE_SEPARATOR);
            String formatted = String.format("%s%5s%s%16s%s"
                    + "%10s%s%10s%s%7s%s"
                    + "%10s%s%10s%s%7s%s"
                    + "%10s%s%10s%s%7s%s"
                    + "%10s%s%10s%s%7s%s"
                    + "%10s%s%10s%s%7s%s"
                    + "%10s%s%10s%s%7s%s",
                    verline, "", verline, "",
                    verline, getRoundValue(allotmenttradingexpensestot), verline, getRoundValue(actualtradingexpensestot), verline, getPercentage(allotmenttradingexpensestot, actualtradingexpensestot),
                    verline, getRoundValue(allotmentestablishmentchargestot), verline, getRoundValue(actualestablishmentchargestot), verline, getPercentage(allotmentestablishmentchargestot, actualestablishmentchargestot),
                    verline, getRoundValue(allotmentadminexpensestot), verline, getRoundValue(actualadminexpensestot), verline, getPercentage(allotmentadminexpensestot, actualadminexpensestot),
                    verline, getRoundValue(allotmentmaintenanceexpensestot), verline, getRoundValue(actualmaintenanceexpensestot), verline, getPercentage(allotmentmaintenanceexpensestot, actualmaintenanceexpensestot),
                    verline, getRoundValue(allotmentmiscexpensestot), verline, getRoundValue(actualmiscexpensestot), verline, getPercentage(allotmentmiscexpensestot, actualmiscexpensestot),
                    verline, getRoundValue(allotmentstaffadvancestot), verline, getRoundValue(actualstaffadvancestot), verline, getPercentage(allotmentstaffadvancestot, actualstaffadvancestot), verline);
            fileWriter.append(formatted);
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(dotline);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
        System.out.println("12345678");
        return map;
    }

    private float getPercentage(BigDecimal allotment, BigDecimal actual) {
        float per = 0;
        if (allotment.compareTo(new BigDecimal("0.00")) > 0 && actual.compareTo(new BigDecimal("0.00")) > 0) {
            per = actual.floatValue() / allotment.floatValue() * 100;
        } else {
            per = 0;
        }
        BigDecimal bd = new BigDecimal(Float.toString(per));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private float getPercentage(float allotment, float actual) {
        float per = 0;
        if (allotment > 0 && actual > 0) {
            per = actual / allotment * 100;
        } else {
            per = 0;
        }
        BigDecimal bd = new BigDecimal(Float.toString(per));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private float getRoundValue(float amount) {
        BigDecimal bd = new BigDecimal(Float.toString(amount));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPayDetailsForBudgerExcellWrite(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String filePath) {
        Map map = new HashMap();
        try {
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet();
            Row row = sheet.createRow(0);
            row.setHeight((short) 500);
            HSSFCell cell = (HSSFCell) row.createCell((short) 0);
            Font font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            CellStyle style1 = wb.createCellStyle();
            style1.setFont(font);
            cell.setCellValue("Tamil Nadu Civil Supplies Corporation, " + "");
            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

            row = sheet.createRow(1);
            row.setHeight((short) 500);
            cell = (HSSFCell) row.createCell((short) 0);
            font = wb.createFont();
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 10);
            style1 = wb.createCellStyle();
            style1.setFont(font);

            cell.setCellStyle(style1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

            row = sheet.createRow(2);
            row.setHeight((short) 600);

            createHeader(wb, row, (short) 0, "EPF no");
            sheet.setColumnWidth(0, (short) (1500));

            createHeader(wb, row, (short) 1, "Name");
            sheet.setColumnWidth(1, (short) (1500));

            createHeader(wb, row, (short) 2, "Designation");
            sheet.setColumnWidth(2, (short) (1500));

            createHeader(wb, row, (short) 3, "Grade");
            sheet.setColumnWidth(2, (short) (1500));


            Criteria payCodeCrit1 = session.createCriteria(Paycodemaster.class);
            payCodeCrit1.add(Restrictions.sqlRestriction("paycodetype='E'"));
            payCodeCrit1.addOrder(Order.asc("paycode"));
            List payCodeList1 = payCodeCrit1.list();
            Float[] totbot = new Float[payCodeList1.size()];
            int size=payCodeList1.size();
            if (payCodeList1.size() > 0) {
                for (int i = 0; i < payCodeList1.size(); i++) {//                        
                    Paycodemaster paycodemasterObj = (Paycodemaster) payCodeList1.get(i);
                    totbot[i] = 0f;
                    createHeader(wb, row, (short) (i + 4), paycodemasterObj.getPaycodename());
                    sheet.setColumnWidth(i + 4, (short) (3500));

                }
            }


            int j = 3;
            int l = 0;
            StringBuffer obquery = new StringBuffer();
            obquery.append("select em.epfno,em.employeename,ss.id,dm.designation,dm.designationgroup from employeemaster em left join ( select * from salarystructureactual where periodto is null ) ss on ss.employeeprovidentfundnumber=em.epfno left join designationmaster dm on dm.designationcode=em.designation  where category='R' and em.region='" + region + "' order by em.region,em.section,em.designation");
            SQLQuery ledgerquery = session.createSQLQuery(obquery.toString());
            for (ListIterator its = ledgerquery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                String epfno = (String) rows[0];
                String empname = (String) rows[1];
                String salstructureid = (String) rows[2];
                String designation = (String) rows[3];
                String grade = (String) rows[4];

                row = sheet.createRow(j);
                j++;
                row.setHeight((short) 600);

                createHeader(wb, row, (short) 0, epfno);
                sheet.setColumnWidth(0, (short) (1500));

                createHeader(wb, row, (short) 1, empname);
                sheet.setColumnWidth(1, (short) (1500));

                createHeader(wb, row, (short) 2, designation);
                sheet.setColumnWidth(2, (short) (1500));

                createHeader(wb, row, (short) 3, grade);
                sheet.setColumnWidth(2, (short) (1500));



                System.out.println("epfno :" + epfno);

//                createContent(wb, row, (short) 0, epfno, CellStyle.ALIGN_RIGHT);
//                sheet.setColumnWidth(0, (short) (1500));
                int k = 4;
                float sidetot = 0;
                Criteria payCodeCrit = session.createCriteria(Paycodemaster.class);
                payCodeCrit.add(Restrictions.sqlRestriction("paycodetype='E'"));
                payCodeCrit.addOrder(Order.asc("paycode"));
                List payCodeList = payCodeCrit.list();
                if (payCodeList.size() > 0) {
                    for (int i = 0; i < payCodeList.size(); i++) {//                        
                        Paycodemaster paycodemasterObj = (Paycodemaster) payCodeList.get(i);

                        Criteria earningDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                        earningDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid='" + salstructureid + "'"));
                        earningDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + paycodemasterObj.getPaycode() + "'"));
                        earningDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List earningDetailsList = earningDetailsCrit.list();
                        if (earningDetailsList.size() > 0) {
                            Employeeearningsdetailsactual earningObj = (Employeeearningsdetailsactual) earningDetailsList.get(0);
                            if (paycodemasterObj.getPaycode().equalsIgnoreCase("E04")) {
                                float t = Math.round(EarningsCalculation(session, salstructureid, paycodemasterObj.getPaycode(), new Date().toString()).floatValue());
                                createContent(wb, row, (short) k, String.valueOf(t), CellStyle.ALIGN_RIGHT);
                                sidetot = sidetot + t;
                                totbot[i] = totbot[i] + t;
                            } else {
                                createContent(wb, row, (short) k, earningObj.getAmount().toString(), CellStyle.ALIGN_RIGHT);
                                sidetot = sidetot + earningObj.getAmount().floatValue();
                                totbot[i] = totbot[i] + earningObj.getAmount().floatValue();
                            }
                            sheet.setColumnWidth(k, (short) (1500));

                        }
                        k = k + 1;

                    }
                    createContent(wb, row, (short) k, String.valueOf(sidetot), CellStyle.ALIGN_RIGHT);
                    sheet.setColumnWidth(k, (short) (1500));
                }

                l = l + 1;
            }
            row = sheet.createRow(l + 7);
            row.setHeight((short) 600);

            for (int i = 0; i < size; i++) {//                        

                createHeader(wb, row, (short) (i + 4), totbot[i].toString());
                sheet.setColumnWidth(i + 4, (short) (3500));

            }

            FileOutputStream fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    private void createContent(Workbook workbook, Row row, short column, String cellValue, short cellalign) {
        Cell cell = row.createCell(column);
        cell.setCellValue(Float.parseFloat(cellValue));        
        Font font = workbook.createFont();
        font.setFontName("Arial");
        //font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = workbook.createCellStyle();
        //cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    private void createHeader(Workbook workbook, Row row, short column, String cellValue) {
        Cell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    public BigDecimal EarningsCalculation(Session session, String salaryStructureId, String earningMasterid, String processDate) {

        float total = 0;
        float earamt = 0;
        Ccahra ccahraObj;
        Employeeearningsdetailsactual employeeearningsdetailsObj;
        Criteria ccaHRA = session.createCriteria(Ccahra.class);
        ccaHRA.add(Restrictions.sqlRestriction("ccahra='" + earningMasterid + "'"));
        List ccaHRAList = ccaHRA.list();
        if (ccaHRAList.size() > 0) {
            for (int i = 0; i < ccaHRAList.size(); i++) {
                ccahraObj = (Ccahra) ccaHRAList.get(i);
                Criteria earCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                earCrit.add(Restrictions.sqlRestriction("salarystructureid ='" + salaryStructureId + "'"));
                earCrit.add(Restrictions.sqlRestriction("earningmasterid ='" + ccahraObj.getPaycodemaster().getPaycode() + "'"));
                earCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List earCritList = earCrit.list();
                if (earCritList.size() > 0) {
                    employeeearningsdetailsObj = (Employeeearningsdetailsactual) earCritList.get(0);
                    total = total + employeeearningsdetailsObj.getAmount().floatValue();
                }

            }
        }

        String querystr = "periodto is null";
        Criteria earSlapCrit = session.createCriteria(Earningslapdetails.class);
        earSlapCrit.add(Restrictions.sqlRestriction("earningcode='" + earningMasterid + "'"));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction(querystr));
        List earSlapList = earSlapCrit.list();
        if (earSlapList.size() > 0) {
            Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(0);
            EarningslapdetailsObj.getAmount();
            if (EarningslapdetailsObj.getAmount().equals(new BigDecimal("0.00"))) {
                float perc = EarningslapdetailsObj.getPercentage().floatValue() / 100;
                earamt = total * perc;
            }

        }

        BigDecimal earamount = new BigDecimal(earamt);
        return earamount;
    }
}
