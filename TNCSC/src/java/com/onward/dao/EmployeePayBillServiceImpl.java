/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.AmountInWords;
import com.onward.common.CommonUtility;
import com.onward.persistence.payroll.*;
import com.onward.valueobjects.PaySlipModel;
import com.onward.valueobjects.PaySlip_Earn_Deduction_Model;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.SQLQuery;
import com.onward.common.DateParser;
import com.onward.reports.EmployeeDBFReport;
import com.onward.reports.regular.*;
import com.tncscpayroll.transferobjects.*;
import java.io.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 *
 * @author root
 */
public class EmployeePayBillServiceImpl extends OnwardAction implements EmployeePayBillService {

    String classname = "";

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeEarningsandDeductionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeProvidentFundNumber) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + employeeProvidentFundNumber + "' "));
        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("employeename", empmasterObj.getEmployeename());
            resultMap.put("fathername", empmasterObj.getFathername());
            resultMap.put("dateofbirth", dateToString(empmasterObj.getDateofbirth()));
            resultMap.put("doa", dateToString(empmasterObj.getDateofappoinment()));
            resultMap.put("dateofprobation", dateToString(empmasterObj.getDateofprobation()));

            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.add(Restrictions.sqlRestriction("id = '" + empmasterObj.getRegion() + "'"));

            List<Regionmaster> rgnList = rgnCrit.list();
            Regionmaster lbobj = rgnList.get(0);
            resultMap.put("branchname", lbobj.getRegionname());

            Criteria secCrit = session.createCriteria(Sectionmaster.class);
            secCrit.add(Restrictions.sqlRestriction("id = '" + empmasterObj.getSection() + "'"));
            List secList = secCrit.list();
            Sectionmaster secobj = (Sectionmaster) secList.get(0);
            resultMap.put("section", secobj.getSectionname());


            Criteria desCrit = session.createCriteria(Designationmaster.class);
            desCrit.add(Restrictions.sqlRestriction("designationcode = '" + empmasterObj.getDesignation() + "'"));
            List<Designationmaster> desList = desCrit.list();
            Designationmaster desobj = desList.get(0);
            resultMap.put("designation", desobj.getDesignation());
        } else {
            resultMap.put("ERROR", "Given EPF Number is Wrong. ");

        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmptyEarningsAndDeductions(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        resultMap.put("earningslist", getEarningsList(session));
        resultMap.put("deductionslist", getDeductionsList(session));
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeEarningsAndDeductions(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeProvidentFundNumber) {
//        String regionCode = (String) request.getSession(false).getAttribute("regioncode");
//        System.out.println("Region Code Taken From Session" + regionCode);
        Map resultMap = new HashMap();
//        resultMap.put("earningslist", getEarningsList(session));
//        resultMap.put("deductionslist", getDeductionsList(session));
        resultMap.put("employeeearningslist", getEmployeeEarningsList(session, employeeProvidentFundNumber, LoggedInRegion));
        resultMap.put("employeedeductionslist", getEmployeeDeductionsList(session, employeeProvidentFundNumber, LoggedInRegion));
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeEarningsAndDeductionsInc(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeProvidentFundNumber, String asondate) {

        Supplementatypaybill supplementatypaybillObj = null;
        Map resultMap = new HashMap();

        Criteria supPayrollCrit = session.createCriteria(Supplementatypaybill.class);
        supPayrollCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeeProvidentFundNumber + "' "));
        supPayrollCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
        supPayrollCrit.add(Restrictions.sqlRestriction("type='INCREMENTARREAR'"));
        supPayrollCrit.add(Restrictions.sqlRestriction("date = '" + postgresDate(asondate) + "' "));
        List supPayrollList = supPayrollCrit.list();
        if (supPayrollList.size() <= 0) {
            resultMap.put("status", "NEW");
            resultMap.put("earningslist", getEarningsList(session));
            resultMap.put("deductionslist", getDeductionsList(session));
            resultMap.put("employeeearningslist", getEmployeeEarningsListInc(session, employeeProvidentFundNumber, LoggedInRegion));
            resultMap.put("employeedeductionslist", getEmployeeDeductionsList(session, employeeProvidentFundNumber, LoggedInRegion));
        } else {
            resultMap.put("status", "OLD");
        }
        return resultMap;
    }

    public Map getEmployeeEarningsListInc(Session session, String employeeProvidentFundNumber, String LoggedInRegion) {
        Map resultMap = new HashMap();
        int slno = 0;
        try {
            Criteria lrCrit = session.createCriteria(Salarystructureactual.class);
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeeProvidentFundNumber + "' "));
//            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("periodto is null "));
            lrCrit.addOrder(Order.desc("periodfrom"));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Salarystructureactual salarystructureObj = (Salarystructureactual) ldList.get(0);
                resultMap.put("salarystructureid", salarystructureObj.getId());
                resultMap.put("salaryeffectdat", dateToString(salarystructureObj.getPeriodfrom()));
                resultMap.put("salaryeffectorderno", salarystructureObj.getOrderno());
                Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salarystructureObj.getId() + "' "));
                earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                earDetailsCrit.addOrder(Order.desc("earningmasterid"));
                //earDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid in ('E01','E03', 'E04', 'E06', 'E07', 'E25')"));

                List earDetailsList = earDetailsCrit.list();
                resultMap.put("employeeearningslength", earDetailsList.size());
                if (earDetailsList.size() > 0) {
                    for (int j = 0; j < earDetailsList.size(); j++) {
                        Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);
                        resultMap.put(slno, getPaycodeDetails(session, employeeearningsdetailsactualObj.getEarningmasterid()));
                        resultMap.put(slno + earDetailsList.size(), employeeearningsdetailsactualObj.getAmount());
                        //System.out.println(slno + "            " + employeeearningsdetailsactualObj.getAmount());
                        slno = slno + 1;
                    }

                }

            } else {
                resultMap.put("salaryeffectdat", "");
                resultMap.put("salaryeffectorderno", "");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map getEmployeeEarningsList(Session session, String employeeProvidentFundNumber, String LoggedInRegion) {
        Map resultMap = new HashMap();
        int slno = 0;
        try {
            Criteria lrCrit = session.createCriteria(Salarystructureactual.class);
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeeProvidentFundNumber + "' "));
//            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("periodto is null "));
            lrCrit.addOrder(Order.desc("periodfrom"));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Salarystructureactual salarystructureObj = (Salarystructureactual) ldList.get(0);
                resultMap.put("salarystructureid", salarystructureObj.getId());
                resultMap.put("salaryeffectdat", dateToString(salarystructureObj.getPeriodfrom()));
                resultMap.put("salaryeffectorderno", salarystructureObj.getOrderno());
                Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salarystructureObj.getId() + "' "));
                earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List earDetailsList = earDetailsCrit.list();
                resultMap.put("employeeearningslength", earDetailsList.size());
                if (earDetailsList.size() > 0) {
                    for (int j = 0; j < earDetailsList.size(); j++) {
                        Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);
                        resultMap.put(slno, getPaycodeDetails(session, employeeearningsdetailsactualObj.getEarningmasterid()));
                        resultMap.put(slno + earDetailsList.size(), employeeearningsdetailsactualObj.getAmount());
                        //System.out.println(slno + "            " + employeeearningsdetailsactualObj.getAmount());
                        slno = slno + 1;
                    }

                }

            } else {
                resultMap.put("salaryeffectdat", "");
                resultMap.put("salaryeffectorderno", "");
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map getEmployeeDeductionsList(Session session, String employeeProvidentFundNumber, String LoggedInRegion) {
        Map resultMap = new HashMap();
        int slno = 0;
        try {
            Criteria lrCrit = session.createCriteria(Salarystructureactual.class);
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeeProvidentFundNumber + "' "));
//            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("periodto is null "));
            lrCrit.addOrder(Order.desc("periodfrom"));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {

                Salarystructureactual salarystructureObj = (Salarystructureactual) ldList.get(0);

                Criteria dedDetailsCrit = session.createCriteria(Employeedeductiondetailsactual.class);
                dedDetailsCrit.add(Restrictions.sqlRestriction("salarystructureactualid = '" + salarystructureObj.getId() + "' "));
                dedDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List dedDetailsList = dedDetailsCrit.list();
                resultMap.put("employeedeductionslength", dedDetailsList.size());
                if (dedDetailsList.size() > 0) {
                    for (int j = 0; j < dedDetailsList.size(); j++) {
                        Employeedeductiondetailsactual employeedeductiondetailsObj = (Employeedeductiondetailsactual) dedDetailsList.get(j);
                        resultMap.put(slno, getPaycodeDetails(session, employeedeductiondetailsObj.getDeductionmasterid()));
                        resultMap.put(slno + dedDetailsList.size(), employeedeductiondetailsObj.getAmount());
                        resultMap.put(slno + dedDetailsList.size() + dedDetailsList.size(), employeedeductiondetailsObj.getDednNo());
                        slno = slno + 1;
                    }
                }

            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map getEarningsList(Session session) {
        Map resultMap = new HashMap();
        Paycodemaster paycodemasterObj;
        int slno = 0;
        try {
            Criteria earCrit = session.createCriteria(Paycodemaster.class);
            earCrit.add(Restrictions.sqlRestriction("paycodetype ='E'"));
            List earList = earCrit.list();
            if (earList.size() > 0) {
                for (int i = 0; i < earList.size(); i++) {
                    paycodemasterObj = (Paycodemaster) earList.get(i);
                    resultMap.put(i, paycodemasterObj.getPaycode() + "  " + paycodemasterObj.getPaycodename());
                    resultMap.put(paycodemasterObj.getPaycode() + "  " + paycodemasterObj.getPaycodename(), paycodemasterObj.getPaycode());
                }
                resultMap.put("earningslistlength", earList.size());
            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public Map getDeductionsList(Session session) {
        Map resultMap = new HashMap();
        Paycodemaster paycodemasterObj;
        int slno = 0;
        try {
            Criteria earCrit = session.createCriteria(Paycodemaster.class);
            earCrit.add(Restrictions.sqlRestriction("paycodetype ='D'"));
            List earList = earCrit.list();
            if (earList.size() > 0) {
                for (int i = 0; i < earList.size(); i++) {
                    paycodemasterObj = (Paycodemaster) earList.get(i);
                    resultMap.put(i, paycodemasterObj.getPaycode() + "  " + paycodemasterObj.getPaycodename());
                    resultMap.put(paycodemasterObj.getPaycode() + "  " + paycodemasterObj.getPaycodename(), paycodemasterObj.getPaycode());
                }
                resultMap.put("deductionslength", earList.size());
            } else {
                resultMap.put("ERROR", "ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    public String getPaycodeDetails(Session session, String payCode) {
        String resultString = "";
        Paycodemaster paycodemasterObj;
        int slno = 0;
        try {
            Criteria earCrit = session.createCriteria(Paycodemaster.class);
            earCrit.add(Restrictions.sqlRestriction("paycode ='" + payCode + "'"));
            List earList = earCrit.list();
            if (earList.size() > 0) {
                paycodemasterObj = (Paycodemaster) earList.get(0);
                resultString = paycodemasterObj.getPaycode() + "  " + paycodemasterObj.getPaycodename();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultString;
    }

    public boolean isPaycode(Session session, String payCode) {
        boolean status = false;
        try {
            Criteria earCrit = session.createCriteria(Paycodemaster.class);
            earCrit.add(Restrictions.sqlRestriction("paycode ='" + payCode + "'"));
            List earList = earCrit.list();
            if (earList.size() > 0) {
                status = true;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return status;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeDeduction(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeProvidentFundNumber, String dateofeffect, String ordernumber) {
        Map resultMap = new HashMap();
        Employeemaster employeemasterObj = null;
        Employeeearningsdetails employeeearningsdetailsObjNew = null;
        Employeedeductiondetails employeedeductionsdetailsObjNew = null;
        StringBuffer resultHTML = new StringBuffer();
        String classname = "";
        String periodTo;
        try {
            employeemasterObj = getEmployeeDetails(session, employeeProvidentFundNumber);
            Criteria lrCrit = session.createCriteria(Salarystructure.class);
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeeProvidentFundNumber + "' "));
            lrCrit.add(Restrictions.sqlRestriction("periodto is null"));
            lrCrit.addOrder(Order.desc("periodfrom"));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                int rowCount = 0;
                for (int i = 0; i < ldList.size(); i++) {
                    Salarystructure employeeEarningsObje = (Salarystructure) ldList.get(i);
                    String previousId = employeeEarningsObje.getId();
                    String currentId = getMaxSeqNumber(session, LoggedInRegion);
                    employeeEarningsObje.setAccregion(LoggedInRegion);
                    Transaction transaction = session.beginTransaction();
                    employeeEarningsObje.setPeriodto(postgresDate(dateofeffect));
                    session.save(employeeEarningsObje);

                    Salarystructure salaryObjNew = new Salarystructure();
                    salaryObjNew.setId(currentId);
                    salaryObjNew.setEmployeemaster(employeemasterObj);
                    salaryObjNew.setOrderno(ordernumber);
                    salaryObjNew.setAccregion(LoggedInRegion);
                    salaryObjNew.setPeriodfrom(postgresDate(dateofeffect));
                    session.save(salaryObjNew);
                    transaction.commit();

                    Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetails.class);
                    earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + previousId + "' "));
                    List earDetailsList = earDetailsCrit.list();
                    //System.out.println("Size " + earDetailsList.size());
                    //System.out.println("previous id " + previousId);
                    if (earDetailsList.size() > 0) {
                        for (int j = 0; j < earDetailsList.size(); j++) {
                            Employeeearningsdetails employeeearningsdetailsObj = (Employeeearningsdetails) earDetailsList.get(j);
                            employeeearningsdetailsObjNew = new Employeeearningsdetails();
                            employeeearningsdetailsObjNew.setId(getMaxSeqNumberEarningDetails(session, LoggedInRegion));
                            transaction = session.beginTransaction();
                            employeeearningsdetailsObjNew.setAmount(employeeearningsdetailsObj.getAmount());
                            employeeearningsdetailsObjNew.setPercentage(employeeearningsdetailsObj.getPercentage());
                            employeeearningsdetailsObjNew.setSalarystructure(salaryObjNew);
                            employeeearningsdetailsObjNew.setIspercentage(employeeearningsdetailsObj.getIspercentage());
                            employeeearningsdetailsObjNew.setEarningmasterid(employeeearningsdetailsObj.getEarningmasterid());
                            employeeearningsdetailsObjNew.setAccregion(LoggedInRegion);
                            session.save(employeeearningsdetailsObjNew);
                            //System.out.println(j);
                            transaction.commit();

                        }

                    }

                    Criteria deductionDetailsCrit = session.createCriteria(Employeedeductiondetails.class);
                    deductionDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + previousId + "' "));
                    List deductionDetailsList = deductionDetailsCrit.list();
                    //System.out.println("Size in deduction" + deductionDetailsList.size());
                    //System.out.println("previous id " + previousId);
                    if (deductionDetailsList.size() > 0) {
                        for (int j = 0; j < deductionDetailsList.size(); j++) {
                            Employeedeductiondetails employeedeductionsdetailsObj = (Employeedeductiondetails) deductionDetailsList.get(j);
                            employeedeductionsdetailsObjNew = new Employeedeductiondetails();
                            employeedeductionsdetailsObjNew.setId(getMaxSeqNumberdeductionDetails(session, LoggedInRegion));
                            transaction = session.beginTransaction();
                            employeedeductionsdetailsObjNew.setAmount(employeedeductionsdetailsObj.getAmount());
                            employeedeductionsdetailsObjNew.setPercentage(employeedeductionsdetailsObj.getPercentage());
                            employeedeductionsdetailsObjNew.setSalarystructure(salaryObjNew);
                            employeedeductionsdetailsObjNew.setIspercentage(employeedeductionsdetailsObj.getIspercentage());
                            employeedeductionsdetailsObjNew.setDeductionmasterid(employeedeductionsdetailsObj.getDeductionmasterid());
                            employeedeductionsdetailsObjNew.setAccregion(LoggedInRegion);
                            session.save(employeedeductionsdetailsObjNew);
                            //System.out.println(j);
                            transaction.commit();

                        }

                    }
                }
                resultMap.put("earningsDetails", getEmployeeEarningsHtml(session, employeeProvidentFundNumber));
                resultMap.put("deductionDetails", getEmployeeDeductionHtml(session, employeeProvidentFundNumber));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeListForAttendance(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String section, String epfno) {
        Map resultMap = new HashMap();
        Employeemaster employeemasterObj;
        Criteria empCrit = session.createCriteria(Employeemaster.class);
        List empList = empCrit.list();
        long recors = empList.size();
        if (empList.size() > 0) {
            for (int i = 0; i < empList.size(); i++) {
                employeemasterObj = (Employeemaster) empList.get(i);
                resultMap.put(i, employeemasterObj.getEpfno());
                resultMap.put(recors + i, employeemasterObj.getEmployeename());
            }

        }
        resultMap.put("length", String.valueOf(empList.size()));
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map regionList = new HashMap();
        Regionmaster regionmasterObj;
        Criteria regCrit = session.createCriteria(Regionmaster.class);
        List regList = regCrit.list();
        long recors = regList.size();
        if (regList.size() > 0) {
            for (int i = 0; i < regList.size(); i++) {
                regionmasterObj = (Regionmaster) regList.get(i);
                regionList.put(regionmasterObj.getId(), regionmasterObj.getRegionname());
            }
        }
        resultMap.put("regionlist", regionList);
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeListForAttendance(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String epfno, String name, String present, String woff, String eldays, String mldays, String cldays, String ndays, String susdays, String others, String llp, String ulep, String totdays, String curRec, String totRec) {
        String regionCode = LoggedInRegion;
        Map resultMap = new HashMap();
        Transaction transaction;
        long currentRecords = Long.parseLong(curRec) + 1;
        resultMap.put("currentRecords", currentRecords);
        resultMap.put("totalrecords", totRec);
        String Dtformat1 = "dd/MM/yyyy";
        String Datestring = attendancemonth;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iDay = cal.get(Calendar.DATE);
        int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal.get(Calendar.YEAR);
        int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        int iwoff = 0;
        int ieldays = 0;
        int imldays = 0;
        int icldays = 0;
        int indays = 0;
        int isusdays = 0;
        int iothers = 0;
        int illp = 0;
        int iulep = 0;
        int itotdays = 0;
        int ipresent = 0;

        if (woff.trim().length() > 0) {
            iwoff = Integer.parseInt(woff);
        }

        if (eldays.trim().length() > 0) {
            ieldays = Integer.parseInt(eldays);
        }

        if (mldays.trim().length() > 0) {
            imldays = Integer.parseInt(mldays);
        }

        if (cldays.trim().length() > 0) {
            icldays = Integer.parseInt(cldays);
        }

        if (ndays.trim().length() > 0) {
            indays = Integer.parseInt(ndays);
        }

        if (susdays.trim().length() > 0) {
            isusdays = Integer.parseInt(susdays);
        }

        if (others.trim().length() > 0) {
            iothers = Integer.parseInt(others);
        }

        if (llp.trim().length() > 0) {
            illp = Integer.parseInt(llp);
        }

        if (ulep.trim().length() > 0) {
            iulep = Integer.parseInt(ulep);
        }

        if (totdays.trim().length() > 0) {
            itotdays = Integer.parseInt(totdays);
        }
        if (present.trim().length() > 0) {
            ipresent = Integer.parseInt(present);
        }

        Criteria empAttendanceCrit = session.createCriteria(Employeeattendance.class);
        empAttendanceCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
        empAttendanceCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
        empAttendanceCrit.add(Restrictions.sqlRestriction("year=" + iYear));
        List empAttendanceList = empAttendanceCrit.list();
        if (empAttendanceList.size() > 0) {
            Employeeattendance employeeattendanceObj = (Employeeattendance) empAttendanceList.get(0);
            employeeattendanceObj.setPresent(ipresent);
            employeeattendanceObj.setMonth(iMonth);
            employeeattendanceObj.setYear(iYear);

            employeeattendanceObj.setWoff(iwoff);
            employeeattendanceObj.setEldays(ieldays);
            employeeattendanceObj.setMldays(imldays);
            employeeattendanceObj.setCldays(icldays);
            employeeattendanceObj.setMdays(indays);
            employeeattendanceObj.setSuspsdays(isusdays);
            employeeattendanceObj.setOthers(iothers);
            employeeattendanceObj.setLlp(illp);
            employeeattendanceObj.setUelp(iulep);
            employeeattendanceObj.setTotdays(itotdays);
            transaction = session.beginTransaction();
            session.update(employeeattendanceObj);
            transaction.commit();
        } else {
            Employeeattendance employeeattendanceObj = new Employeeattendance();
            String id = getMaxEmployeeAttendanceSeqNumber(session, regionCode);
            employeeattendanceObj.setId(id);
            employeeattendanceObj.setEmployeemaster(getEmployeeDetails(session, epfno)); //.setEmployeeprovidentfundnumber(epfno);
            employeeattendanceObj.setPresent(ipresent);
            employeeattendanceObj.setMonth(iMonth);
            employeeattendanceObj.setYear(iYear);
            employeeattendanceObj.setWoff(iwoff);
            employeeattendanceObj.setEldays(ieldays);
            employeeattendanceObj.setMldays(imldays);
            employeeattendanceObj.setCldays(icldays);
            employeeattendanceObj.setMdays(indays);
            employeeattendanceObj.setSuspsdays(isusdays);
            employeeattendanceObj.setOthers(iothers);
            employeeattendanceObj.setLlp(illp);
            employeeattendanceObj.setUelp(iulep);
            employeeattendanceObj.setTotdays(itotdays);
            employeeattendanceObj.setTotdays(itotdays);
            employeeattendanceObj.setAccregion(LoggedInRegion);
            transaction = session.beginTransaction();
            session.save(employeeattendanceObj);
            transaction.commit();

        }

        resultMap.put("message", "Updated Successfully");
        return resultMap;
    }

    public synchronized String getMaxEmployeeAttendanceSeqNumber(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeeattendanceid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeeattendanceid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return maxStr;
    }

    public synchronized String getMaxSeqNumber(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeeearningsslno();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeeearningsslno(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return maxStr;
    }

    public synchronized String getMaxSeqNumberEarningDetails(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeeearningdetailsslno();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeeearningdetailsslno(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return maxStr;
    }

    public synchronized String getMaxSeqNumberdeductionDetails(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeedeductiondetailsslno();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeedeductiondetailsslno(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return maxStr;
    }

    public Employeemaster getEmployeeDetails(Session session, String employeeProvidentFundNumber) {
        Employeemaster employeemasterObj = null;
        try {
            Criteria lrCrit = session.createCriteria(Employeemaster.class);
            lrCrit.add(Restrictions.sqlRestriction("epfno = '" + employeeProvidentFundNumber + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                employeemasterObj = (Employeemaster) ldList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }


        return employeemasterObj;
    }

    public String getEmployeeEarningsHtml(Session session, String employeeProvidentFundNumber) {
        StringBuffer resultHTML = new StringBuffer();
        String classname = "";
        String periodTo;
        try {
            Criteria lrCrit = session.createCriteria(Salarystructure.class);
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeeProvidentFundNumber + "' "));
            lrCrit.add(Restrictions.sqlRestriction("  periodto  is null "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Earnings </td>").append("<td>Amount</td>").append("<td>Modify</td>").append("</tr>");
                int rowCount = 0;
                for (int i = 0; i < ldList.size(); i++) {

                    Salarystructure EmployeeearningsObj = (Salarystructure) ldList.get(i);
                    if (EmployeeearningsObj.getPeriodto() != null) {
                        periodTo = dateToString(EmployeeearningsObj.getPeriodto());
                    } else {
                        periodTo = "Till Date";
                    }
                    resultHTML.append("<tr class=\"rowColor1\">").append("<td></td>").append("<td>" + EmployeeearningsObj.getPeriodfrom() + " to " + periodTo + " </td>").append("<td></td>").append("<td></td>").append("</tr>");

                    Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetails.class);
                    earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + EmployeeearningsObj.getId() + "' "));
                    List earDetailsList = earDetailsCrit.list();
                    if (earDetailsList.size() > 0) {
                        for (int j = 0; j < earDetailsList.size(); j++) {
                            if (j % 2 == 0) {
                                classname = "rowColor2";
                            } else {
                                classname = "rowColor1";
                            }
                            Employeeearningsdetails EmployeeearningsdetailsObj = (Employeeearningsdetails) earDetailsList.get(j);
                            resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (j + 1) + "</td>").append("<td align=\"center\">" + EmployeeearningsdetailsObj.getEarningmasterid() + " </td>").append("<td align=\"right\">" + EmployeeearningsdetailsObj.getAmount() + "</td>").append("<td align=\"center\"><img src=\"images/edit.gif\" border=\"0\"><img src=\"images/delete.jpeg\" border=\"0\"></td>").append("</tr>");
                        }

                    }
                }
                resultHTML.append("</table>");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultHTML.toString();
    }

    public String getEmployeeDeductionHtml(Session session, String employeeProvidentFundNumber) {
        StringBuffer resultHTML = new StringBuffer();
        String classname = "";
        String periodTo;
        try {
            Criteria lrCrit = session.createCriteria(Salarystructure.class);
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeeProvidentFundNumber + "' "));
            lrCrit.add(Restrictions.sqlRestriction("  periodto  is null "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Deductions </td>").append("<td>Amount</td>").append("<td>Modify</td>").append("</tr>");
                int rowCount = 0;
                for (int i = 0; i < ldList.size(); i++) {

                    Salarystructure EmployeedeductionObj = (Salarystructure) ldList.get(i);
                    if (EmployeedeductionObj.getPeriodto() != null) {
                        periodTo = dateToString(EmployeedeductionObj.getPeriodto());
                    } else {
                        periodTo = "Till Date";
                    }
                    resultHTML.append("<tr class=\"rowColor1\">").append("<td></td>").append("<td>" + EmployeedeductionObj.getPeriodfrom() + " to " + periodTo + " </td>").append("<td></td>").append("<td></td>").append("</tr>");

                    Criteria earDetailsCrit = session.createCriteria(Employeedeductiondetails.class);
                    earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + EmployeedeductionObj.getId() + "' "));
                    List earDetailsList = earDetailsCrit.list();
                    if (earDetailsList.size() > 0) {
                        for (int j = 0; j < earDetailsList.size(); j++) {
                            if (j % 2 == 0) {
                                classname = "rowColor2";
                            } else {
                                classname = "rowColor1";
                            }
                            Employeedeductiondetails EmployeedeductionsdetailsObj = (Employeedeductiondetails) earDetailsList.get(j);
                            resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (j + 1) + "</td>").append("<td align=\"center\">" + EmployeedeductionsdetailsObj.getDeductionmasterid() + " </td>").append("<td align=\"right\">" + EmployeedeductionsdetailsObj.getAmount() + "</td>").append("<td align=\"center\"> <img src=\"images/edit.gif\"  id=\"editearnings\" name=\"editearnings\"><img src=\"images/delete.jpeg\"></td>").append("</tr>");
                        }

                    }
                }
                resultHTML.append("</table>");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeSalaryStructure(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String employeeid, String salarystructureid, String orderno, String salarystruceffeftfrom, String code, String amt, String actcode, String curRec, String totRec) {
        HashMap resultMap = new HashMap();
        Salarystructureactual salarystructureactualObj;
        String salStrucId = "";
        BigDecimal amount;
        Transaction transaction;

        if (salarystructureid == null) {
            Criteria salStrutCrit = session.createCriteria(Salarystructureactual.class);
            salStrutCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeeid + "'"));
            salStrutCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
            salStrutCrit.add(Restrictions.sqlRestriction("periodfrom='" + postgresDate(salarystruceffeftfrom) + "'"));
            salStrutCrit.add(Restrictions.sqlRestriction("periodto is null"));
            List salStrutList = salStrutCrit.list();
            if (salStrutList.size() > 0) {
                salarystructureactualObj = (Salarystructureactual) salStrutList.get(0);
                salStrucId = salarystructureactualObj.getId();
                //System.out.println("Salary structure Already Exists");
            } else {
                Criteria salStrutCrit1 = session.createCriteria(Salarystructureactual.class);
                salStrutCrit1.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeeid + "'"));
                salStrutCrit1.add(Restrictions.sqlRestriction("periodto is null"));
                List salStrutList1 = salStrutCrit1.list();
                if (salStrutList1.size() > 0) {
                    Salarystructureactual salarystructureactualObj1 = (Salarystructureactual) salStrutList1.get(0);
                    salarystructureactualObj1.setOrderno(orderno);
                    salarystructureactualObj1.setPeriodto(postgresDate(salarystruceffeftfrom));

                    transaction = session.beginTransaction();
                    session.update(salarystructureactualObj1);
                    transaction.commit();

                    salarystructureactualObj = new Salarystructureactual();
                    salStrucId = getMaxSeqNumberSalaryStructureActual(session, LoggedInRegion);
                    salarystructureactualObj.setId(salStrucId);
                    salarystructureactualObj.setEmployeemaster(getEmployeeDetails(session, employeeid));
                    salarystructureactualObj.setOrderno(orderno);
                    salarystructureactualObj.setPeriodfrom(postgresDate(salarystruceffeftfrom));
                    salarystructureactualObj.setAccregion(LoggedInRegion);
                    salarystructureactualObj.setCreatedby(LoggedInUser);
                    salarystructureactualObj.setCreateddate(getCurrentDate());
                    transaction = session.beginTransaction();
                    session.save(salarystructureactualObj);
                    transaction.commit();

                } else {

                    salarystructureactualObj = new Salarystructureactual();
                    salStrucId = getMaxSeqNumberSalaryStructureActual(session, LoggedInRegion);
                    salarystructureactualObj.setId(salStrucId);
                    salarystructureactualObj.setOrderno(orderno);
                    salarystructureactualObj.setEmployeemaster(getEmployeeDetails(session, employeeid));
                    salarystructureactualObj.setPeriodfrom(postgresDate(salarystruceffeftfrom));
                    salarystructureactualObj.setAccregion(LoggedInRegion);
                    salarystructureactualObj.setCreatedby(LoggedInUser);
                    salarystructureactualObj.setCreateddate(getCurrentDate());
                    transaction = session.beginTransaction();
                    session.save(salarystructureactualObj);
                    transaction.commit();
                }
            }
        } else {
            salStrucId = salarystructureid;
        }

        long currentRecords = Long.parseLong(curRec);
        if (type.equalsIgnoreCase("earnings")) {
            if (currentRecords == 0) {
                session.createSQLQuery("UPDATE employeeearningsdetailsactual  SET cancelled  = true WHERE salarystructureid='" + salStrucId + "'").executeUpdate();
            }

            if (isPaycode(session, code)) {

                Criteria salStrutEatCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                salStrutEatCrit.add(Restrictions.sqlRestriction("earningmasterid='" + code + "'"));
                salStrutEatCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                salStrutEatCrit.add(Restrictions.sqlRestriction("salarystructureid='" + salStrucId + "'"));
                List salStrutEatList = salStrutEatCrit.list();
                if (salStrutEatList.size() > 0) {
                    Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) salStrutEatList.get(0);
                    employeeearningsdetailsactualObj.setEarningmasterid(code);
                    if (amt != null && amt.trim().length() != 0) {
                        amount = new BigDecimal(amt);
                    } else {
                        amount = new BigDecimal(0);
                    }
                    employeeearningsdetailsactualObj.setAmount(amount);
                    employeeearningsdetailsactualObj.setCancelled(Boolean.FALSE);
                    transaction = session.beginTransaction();
                    session.update(employeeearningsdetailsactualObj);
                    transaction.commit();

                } else {

                    Criteria salStrCrit = session.createCriteria(Salarystructureactual.class);
                    salStrCrit.add(Restrictions.sqlRestriction("id='" + salStrucId + "'"));
                    List salStrList = salStrCrit.list();
                    if (salStrList.size() > 0) {
                        Salarystructureactual salarystructureactual = (Salarystructureactual) salStrList.get(0);

                        Employeeearningsdetailsactual employeeearningsdetailsactualObj = new Employeeearningsdetailsactual();
                        String earId = getMaxSeqNumberEarningsActual(session, LoggedInRegion);
                        employeeearningsdetailsactualObj.setId(earId);
                        employeeearningsdetailsactualObj.setEarningmasterid(code);
                        if (amt != null && amt.trim().length() != 0) {

                            amount = new BigDecimal(amt);
                        } else {
                            amount = new BigDecimal(0);
                        }
                        employeeearningsdetailsactualObj.setAmount(amount);
                        employeeearningsdetailsactualObj.setCancelled(Boolean.FALSE);
                        employeeearningsdetailsactualObj.setSalarystructureactual(salarystructureactual);
                        employeeearningsdetailsactualObj.setAccregion(LoggedInRegion);
                        transaction = session.beginTransaction();
                        session.save(employeeearningsdetailsactualObj);
                        transaction.commit();
                    }
                }
            }
        }


        if (type.equalsIgnoreCase("deductions")) {
            if (currentRecords == 0) {
                session.createSQLQuery("UPDATE employeedeductiondetailsactual SET  cancelled  = true  WHERE   salarystructureactualid='" + salStrucId + "'").executeUpdate();
            }
            if (isPaycode(session, code)) {
                Criteria salStrutDedCrit = session.createCriteria(Employeedeductiondetailsactual.class);
                salStrutDedCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + code + "'"));
                salStrutDedCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                salStrutDedCrit.add(Restrictions.sqlRestriction("salarystructureactualid='" + salStrucId + "'"));
                List salStrutDedList = salStrutDedCrit.list();
                if (salStrutDedList.size() > 0) {
                    Employeedeductiondetailsactual employeedeductiondetailsactualObj = (Employeedeductiondetailsactual) salStrutDedList.get(0);

                    employeedeductiondetailsactualObj.setDeductionmasterid(code);
                    if (amt != null && amt.trim().length() != 0) {
                        amount = new BigDecimal(amt);
                    } else {
                        amount = new BigDecimal(0);
                    }
                    employeedeductiondetailsactualObj.setAmount(amount);
                    employeedeductiondetailsactualObj.setDednNo(actcode);
                    employeedeductiondetailsactualObj.setCancelled(Boolean.FALSE);
                    transaction = session.beginTransaction();
                    session.update(employeedeductiondetailsactualObj);
                    transaction.commit();


                } else {
                    Criteria salStrCrit = session.createCriteria(Salarystructureactual.class);
                    salStrCrit.add(Restrictions.sqlRestriction("id='" + salStrucId + "'"));
                    List salStrList = salStrCrit.list();
                    if (salStrList.size() > 0) {
                        Salarystructureactual salarystructureactual = (Salarystructureactual) salStrList.get(0);

                        Employeedeductiondetailsactual employeedeductiondetailsactualObj = new Employeedeductiondetailsactual();
                        String deduId = getMaxSeqNumberDeductionsActual(session, LoggedInRegion);
                        employeedeductiondetailsactualObj.setId(deduId);
                        employeedeductiondetailsactualObj.setSalarystructureactual(salarystructureactual);
                        employeedeductiondetailsactualObj.setDeductionmasterid(code);
                        employeedeductiondetailsactualObj.setCancelled(Boolean.FALSE);
                        if (amt != null && amt.trim().length() != 0) {
                            amount = new BigDecimal(amt);
                        } else {
                            amount = new BigDecimal(0);
                        }
                        employeedeductiondetailsactualObj.setAmount(amount);
                        employeedeductiondetailsactualObj.setDednNo(actcode);
                        employeedeductiondetailsactualObj.setAccregion(LoggedInRegion);
                        transaction = session.beginTransaction();
                        session.save(employeedeductiondetailsactualObj);
                        transaction.commit();

                    }
                }

            }

        }


        currentRecords = currentRecords + 1;
        resultMap.put("salarystructureid", salStrucId);
        resultMap.put("currentRecords", currentRecords);
        resultMap.put("totalrecords", totRec);

        return resultMap;
    }

    public synchronized String getMaxSeqNumberSalaryStructureActual(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSalarystructureactualid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSalarystructureactualid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return maxStr;
    }

    public synchronized String getMaxSeqNumberDeductionsActual(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeedeductiondetailsactualid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeedeductiondetailsactualid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return maxStr;
    }

    public synchronized String getMaxSeqNumberEarningsActual(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeeearningsdetailsactualid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeeearningsdetailsactualid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return maxStr;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeListForLLP(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String section, String epfno, String attendancemonth) {
        Map resultMap = new HashMap();
        Map llpMap = new HashMap();
        Employeemaster employeemasterObj;
        Criteria empCrit = session.createCriteria(Employeemaster.class);
        empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empList = empCrit.list();
        if (empList.size() > 0) {
            for (int i = 0; i < empList.size(); i++) {
                employeemasterObj = (Employeemaster) empList.get(i);
                resultMap.put(employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename(), employeemasterObj.getEpfno());
                resultMap.put(i, employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename());
            }

        }
        resultMap.put("length", String.valueOf(empList.size()));
        resultMap.put("llpdetails", getEmployeeLLPDetails(session, attendancemonth, LoggedInRegion));
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeMiscDeductions(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String deductioncode) {
        Map resultMap = new HashMap();
        Map llpMap = new HashMap();
        Employeemaster employeemasterObj;
        Criteria empCrit = session.createCriteria(Employeemaster.class);
        empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empList = empCrit.list();
        if (empList.size() > 0) {
            for (int i = 0; i < empList.size(); i++) {
                employeemasterObj = (Employeemaster) empList.get(i);
                resultMap.put(employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename().trim(), employeemasterObj.getEpfno());
                resultMap.put("ACC" + employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename().trim(), getDeductionCode(session, employeemasterObj.getEpfno(), deductioncode));
                resultMap.put(i, employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename().trim());
            }

        }
        resultMap.put("length", String.valueOf(empList.size()));
        resultMap.put("llpdetails", getEmployeeMiscDeducTable(session, attendancemonth, deductioncode, LoggedInRegion));
        resultMap.put("bondetails", getEmployeeBonusDetail(session, attendancemonth, deductioncode, LoggedInRegion));
        return resultMap;
    }

    public Map getEmployeeMiscDeducTable(Session session, String attendancemonth, String deductioncode, String LoggedInRegion) {
        String Datestring = attendancemonth;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal.get(Calendar.YEAR);
        Map resultMap = new LinkedHashMap();
        int listsize = 0;
        StringBuilder sb = new StringBuilder();
        String Mis_Query = "select employeeprovidentfundnumber, amount,deductionaccountcode from miscdeductions where accregion='" + LoggedInRegion + "' and month=" + iMonth + " and year=" + iYear + " and "
                + "deductionscode='" + deductioncode + "' order by cast(REPLACE(TRANSLATE(id, REPLACE(TRANSLATE(id,'0123456789', RPAD('#',LENGTH(id),'#')),'#',''), "
                + "RPAD('#',LENGTH(id),'#')),'#','') as numeric)";
        SQLQuery misquery = session.createSQLQuery(Mis_Query);
        String classname = "";
        if (misquery.list().size() > 0) {
            sb.append("<table width=\"100%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
            sb.append("<tr class=\"gridmenu\">").append("<td></td><td> S.No</td>").append("<td> EMPLOYEE NAME</td>").append("<td> AMOUNTS</td>").append("<td> ACCOUNT CODE</td>");
            int i = 0;
            listsize = misquery.list().size();
            for (ListIterator it = misquery.list().listIterator(); it.hasNext();) {
                Object[] row = (Object[]) it.next();
                String epfno = (String) row[0];
                String amount = row[1].toString();
                String accountCode = (String) row[2];
                if (accountCode == null || accountCode.isEmpty() || accountCode.length() <= 0) {
                    accountCode = "";
                }
                if (i % 2 == 0) {
                    classname = "rowColor1";
                } else {
                    classname = "rowColor2";
                }
                resultMap.put(i, getEmployeeDetailsForLLP(session, epfno));
                resultMap.put(i + listsize, amount);
                resultMap.put(i + listsize + listsize, accountCode);
                i++;
                sb.append("<tr class=\"" + classname + "\"><td align=\"center\"><input type=\"radio\" name=\"billno\" id=\"" + epfno + "\" onclick=\"getModifyData('" + epfno + "','" + amount + "','" + accountCode + "')\"></td>").append("<td>" + i + "</td><td>" + getEmployeeDetailsForLLP(session, epfno) + "</td><td>" + amount + "</td><td>" + accountCode + "</td></tr>");
            }
            sb.append("</table>");
        }
        resultMap.put("length", listsize);
        resultMap.put("table", sb.toString());
        return resultMap;
    }

    public Map getEmployeeBonusDetail(Session session, String attendancemonth, String bonustype, String LoggedInRegion) {
        String Datestring = attendancemonth;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal.get(Calendar.YEAR);
        Map resultMap = new LinkedHashMap();
        int listsize = 0;
        StringBuilder sb = new StringBuilder();
        String Mis_Query = "select epfno, earningsamount, deductionamount from bonusdetails where region='" + LoggedInRegion + "' and bonustype='" + bonustype + "' and  month='" + iMonth + "' and  year='" + iYear + "' order by id ";
        SQLQuery misquery = session.createSQLQuery(Mis_Query);
        String classname = "";
        if (misquery.list().size() > 0) {
            sb.append("<table width=\"100%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
            sb.append("<tr class=\"gridmenu\">").append("<td> S.No</td>").append("<td> EMPLOYEE NAME</td>").append("<td> EARNINGS AMNOUNT</td>").append("<td> DEDUCTION AMNOUNT</td>");
            int i = 0;
            listsize = misquery.list().size();
            for (ListIterator it = misquery.list().listIterator(); it.hasNext();) {
                Object[] row = (Object[]) it.next();
                String epfno = (String) row[0];
                String amount = row[1].toString();
                String accountCode = row[2].toString();
                if (i % 2 == 0) {
                    classname = "rowColor1";
                } else {
                    classname = "rowColor2";
                }
                resultMap.put(i, getEmployeeDetailsForLLP(session, epfno));
                resultMap.put(i + listsize, amount);
                resultMap.put(i + listsize + listsize, accountCode);
                i++;
                sb.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + i + "</td><td>" + getEmployeeDetailsForLLP(session, epfno) + "</td><td>" + amount + "</td><td>" + accountCode + "</td></tr>");
            }
            sb.append("</table>");
        } else {
            sb.append("<table width=\"100%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
            sb.append("<tr class=\"gridmenu\">").append("<td> S.No</td>").append("<td> EMPLOYEE NAME</td>").append("<td> EARNINGS AMNOUNT</td>").append("<td> DEDUCTION AMNOUNT</td>");
            sb.append("</table>");
        }
        resultMap.put("length", listsize);
        resultMap.put("table", sb.toString());
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeMiscDeductionsold(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String deductioncode) {
        Map resultMap = new HashMap();
        Map llpMap = new HashMap();
        Employeemaster employeemasterObj;
        Criteria empCrit = session.createCriteria(Employeemaster.class);
        empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empList = empCrit.list();
        if (empList.size() > 0) {
            for (int i = 0; i < empList.size(); i++) {
                employeemasterObj = (Employeemaster) empList.get(i);
                resultMap.put(employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename(), employeemasterObj.getEpfno());
                resultMap.put("ACC" + employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename(), getDeductionCode(session, employeemasterObj.getEpfno(), deductioncode));
                resultMap.put(i, employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename());
            }

        }
        resultMap.put("length", String.valueOf(empList.size()));
        resultMap.put("llpdetails", getEmployeeMiscDeduc(session, attendancemonth, deductioncode, LoggedInRegion));
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeePreviousMiscDeductions(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String deductioncode) {
        Map resultMap = new HashMap();
        Map llpMap = new HashMap();
        Employeemaster employeemasterObj;
        Criteria empCrit = session.createCriteria(Employeemaster.class);
        empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empList = empCrit.list();
        if (empList.size() > 0) {
            for (int i = 0; i < empList.size(); i++) {
                employeemasterObj = (Employeemaster) empList.get(i);
                resultMap.put(employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename(), employeemasterObj.getEpfno());
                resultMap.put("ACC" + employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename(), getDeductionCode(session, employeemasterObj.getEpfno(), deductioncode));
                resultMap.put(i, employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename());
            }

        }
        resultMap.put("length", String.valueOf(empList.size()));
        resultMap.put("llpdetails", getEmployeePreviousMiscDeduc(session, attendancemonth, deductioncode, LoggedInRegion));
        return resultMap;
    }

    public String getDeductionCode(Session session, String epfno, String paycode) {
        String resultString = "";
        Employeedeductionaccountcode employeedeductionaccountcodeObj;
        try {
            Criteria earCrit = session.createCriteria(Employeedeductionaccountcode.class);
            earCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber ='" + epfno + "'"));
            earCrit.add(Restrictions.sqlRestriction("paycode ='" + paycode + "'"));
            List earList = earCrit.list();
            if (earList.size() > 0) {
                employeedeductionaccountcodeObj = (Employeedeductionaccountcode) earList.get(0);
                resultString = employeedeductionaccountcodeObj.getDeductionaccountcode();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultString;
    }

    public Map getEmployeeMiscDeduc(Session session, String attendancemonth, String deductioncode, String LoggedInRegion) {
        Miscdeductions miscdeductionsObj;
        String Dtformat1 = "dd/MM/yyyy";
        String Datestring = attendancemonth;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iDay = cal.get(Calendar.DATE);
        int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal.get(Calendar.YEAR);
        int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        Map resultMap = new LinkedHashMap();
        int listsize = 0;
        /*
         * ModifY Start
         */

        String Mis_Query = "select employeeprovidentfundnumber, amount,deductionaccountcode from miscdeductions where accregion='" + LoggedInRegion + "' and month=" + iMonth + " and year=" + iYear + " and "
                + "deductionscode='" + deductioncode + "' order by cast(REPLACE(TRANSLATE(id, REPLACE(TRANSLATE(id,'0123456789', RPAD('#',LENGTH(id),'#')),'#',''), "
                + "RPAD('#',LENGTH(id),'#')),'#','') as numeric)";

        SQLQuery misquery = session.createSQLQuery(Mis_Query);
        if (misquery.list().size() > 0) {

            List<PaySlip_Earn_Deduction_Model> liclist = new ArrayList<PaySlip_Earn_Deduction_Model>();
            int i = 0;
            listsize = misquery.list().size();

            for (ListIterator it = misquery.list().listIterator(); it.hasNext();) {
                Object[] row = (Object[]) it.next();
                String epfno = (String) row[0];
                String amount = row[1].toString();
                String accountCode = (String) row[2];

                resultMap.put(i, getEmployeeDetailsForLLP(session, epfno));
                resultMap.put(i + listsize, amount);
                resultMap.put(i + listsize + listsize, accountCode);
                i++;
            }
        }


        /*
         * ModifY Start
         */
//
//        Criteria empLLPCrit = session.createCriteria(Miscdeductions.class);

//        empLLPCrit.add(Restrictions.sqlRestriction("month = " + iMonth + " "));
//        empLLPCrit.add(Restrictions.sqlRestriction("year = " + iYear + " "));
//        empLLPCrit.add(Restrictions.sqlRestriction("amount > " + "0" + " "));
//        empLLPCrit.add(Restrictions.sqlRestriction("deductionscode = '" + deductioncode + "' "));
//        empLLPCrit.addOrder(Order.desc("cast(REPLACE(TRANSLATE(id, REPLACE(TRANSLATE(id,'0123456789', RPAD('#',LENGTH(id),'#')),'#',''), RPAD('#',LENGTH(id),'#')),'#','') as numeric)"));
//        List empLLPList = empLLPCrit.list();
//        if (empLLPList.size() > 0) {
//            for (int i = 0; i < empLLPList.size(); i++) {
//                miscdeductionsObj = (Miscdeductions) empLLPList.get(i);
//                resultMap.put(i, getEmployeeDetailsForLLP(session, miscdeductionsObj.getEmployeemaster().getEpfno()));
//                resultMap.put(i + empLLPList.size(), miscdeductionsObj.getAmount());
//            }
//
//        }
        resultMap.put("length", listsize);
        return resultMap;
    }

    public Map getEmployeePreviousMiscDeduc(Session session, String attendancemonth, String deductioncode, String LoggedInRegion) {

        String Datestring = attendancemonth;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal.get(Calendar.YEAR);
        if (iMonth > 1) {
            iMonth = cal.get(Calendar.MONTH);
        } else {
            iMonth = 12;
            iYear = iYear - 1;
        }

        Map resultMap = new LinkedHashMap();
        int listsize = 0;
        String Mis_Query = "select employeeprovidentfundnumber, amount,deductionaccountcode from miscdeductions where accregion='" + LoggedInRegion + "' and month=" + iMonth + " and year=" + iYear + " and "
                + "deductionscode='" + deductioncode + "' order by cast(REPLACE(TRANSLATE(id, REPLACE(TRANSLATE(id,'0123456789', RPAD('#',LENGTH(id),'#')),'#',''), "
                + "RPAD('#',LENGTH(id),'#')),'#','') as numeric)";

        SQLQuery misquery = session.createSQLQuery(Mis_Query);
        if (misquery.list().size() > 0) {

            List<PaySlip_Earn_Deduction_Model> liclist = new ArrayList<PaySlip_Earn_Deduction_Model>();
            int i = 0;
            listsize = misquery.list().size();

            for (ListIterator it = misquery.list().listIterator(); it.hasNext();) {
                Object[] row = (Object[]) it.next();
                String epfno = (String) row[0];
                String amount = row[1].toString();
                String accountCode = (String) row[2];

                resultMap.put(i, getEmployeeDetailsForLLP(session, epfno));
                resultMap.put(i + listsize, amount);
                resultMap.put(i + listsize + listsize, accountCode);
                i++;
            }
        }


        resultMap.put("length", listsize);
        return resultMap;
    }

    public Map getEmployeeLLPDetails(Session session, String attendancemonth, String LoggedInRegion) {
        Employeeattendance employeeattendanceObj;
        String Dtformat1 = "dd/MM/yyyy";
        String Datestring = attendancemonth;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iDay = cal.get(Calendar.DATE);
        int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal.get(Calendar.YEAR);
        int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        Map resultMap = new HashMap();


        Criteria empLLPCrit = session.createCriteria(Employeeattendance.class);
        empLLPCrit.add(Restrictions.sqlRestriction("month = " + iMonth + " "));
        empLLPCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
        empLLPCrit.add(Restrictions.sqlRestriction("year = " + iYear + " "));
        empLLPCrit.add(Restrictions.sqlRestriction("llp > " + "0" + " "));
        List empLLPList = empLLPCrit.list();
        if (empLLPList.size() > 0) {
            for (int i = 0; i < empLLPList.size(); i++) {
                employeeattendanceObj = (Employeeattendance) empLLPList.get(i);
                resultMap.put(i, getEmployeeDetailsForLLP(session, employeeattendanceObj.getEmployeemaster().getEpfno()));
                resultMap.put(i + empLLPList.size(), employeeattendanceObj.getLlp());
            }

        }
        resultMap.put("length", empLLPList.size());
        return resultMap;
    }

    public String getEmployeeDetailsForLLP(Session session, String employeeProvidentFundNumber) {
        String empDetStr = "";
        Employeemaster employeemasterObj = null;
        try {
            Criteria lrCrit = session.createCriteria(Employeemaster.class);
            lrCrit.add(Restrictions.sqlRestriction("epfno = '" + employeeProvidentFundNumber + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                employeemasterObj = (Employeemaster) ldList.get(0);
                empDetStr = employeemasterObj.getEpfno() + "    " + employeemasterObj.getEmployeename();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return empDetStr;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeLLP(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String code, String llpdays, String curRec, String totRec) {
        Map resultMap = new HashMap();
        Transaction transaction;
        String Dtformat1 = "dd/MM/yyyy";
        String Datestring = attendancemonth;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iDay = cal.get(Calendar.DATE);
        int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal.get(Calendar.YEAR);
        int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        long currentRecords = Long.parseLong(curRec);

        if (currentRecords == 0) {
            transaction = session.beginTransaction();
            session.createSQLQuery("UPDATE employeeattendance  SET llp  = 0 WHERE year=" + iYear + "  and month=" + iMonth).executeUpdate();
            transaction.commit();
        }
        if (code != null) {
            if (code.trim().length() > 0) {

                Criteria attCrit = session.createCriteria(Employeeattendance.class);
                attCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + code + "'"));
                attCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                attCrit.add(Restrictions.sqlRestriction("year=" + iYear));
                List attList = attCrit.list();
                if (attList.size() > 0) {
                    Employeeattendance employeeattendanceObje = (Employeeattendance) attList.get(0);
                    employeeattendanceObje.setLlp(Integer.parseInt(llpdays));

                    transaction = session.beginTransaction();
                    session.update(employeeattendanceObje);
                    transaction.commit();

                } else {
                    Employeeattendance employeeattendanceObje = new Employeeattendance();
                    String id = getMaxAttendanceid(session, LoggedInRegion);
                    employeeattendanceObje.setId(id);
                    employeeattendanceObje.setMonth(iMonth);
                    employeeattendanceObje.setYear(iYear);
                    employeeattendanceObje.setEmployeemaster(getEmployeeDetails(session, code));
                    employeeattendanceObje.setLlp(Integer.parseInt(llpdays));
                    employeeattendanceObje.setAccregion(LoggedInRegion);
                    transaction = session.beginTransaction();
                    session.save(employeeattendanceObje);
                    transaction.commit();


                }
            }
        }

        currentRecords = currentRecords + 1;
        resultMap.put("currentRecords", currentRecords);
        resultMap.put("totalrecords", totRec);
        return resultMap;
    }

    public synchronized String getMaxAttendanceid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeeattendanceid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeeattendanceid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return maxStr;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadDeductionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductionType) {
        Map resultMap = new HashMap();
        Map deductionlist = new LinkedHashMap();
        Paycodemaster paycodemasterObj;
        Criteria lrCrit = session.createCriteria(Paycodemaster.class);
        lrCrit.add(Restrictions.sqlRestriction("paycodetype in ('D','L','R')"));
        //CR - PR.TAX,THIRFT add for new screen
        if ("thirft".equalsIgnoreCase(deductionType)) {
            lrCrit.add(Restrictions.sqlRestriction("grouphead in ('PR.TAX','THIRFT')"));
        }
        lrCrit.addOrder(Order.asc("paycodename"));
        List ldList = lrCrit.list();
        if (ldList.size() > 0) {
            for (int i = 0; i < ldList.size(); i++) {
                paycodemasterObj = (Paycodemaster) ldList.get(i);
                deductionlist.put(paycodemasterObj.getPaycode(), paycodemasterObj.getPaycodename());

            }

        }
        resultMap.put("deductionlist", deductionlist);
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeMiscDeductions(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String deductioncode, String code, String deductionAmount, String curRec, String totRec) {
        Map resultMap = new HashMap();
        Transaction transaction;
        String Dtformat1 = "dd/MM/yyyy";
        String Datestring = attendancemonth;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iDay = cal.get(Calendar.DATE);
        int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal.get(Calendar.YEAR);
        int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        long currentRecords = Long.parseLong(curRec);
        /*if (currentRecords == 0) {
        transaction = session.beginTransaction();
        session.createSQLQuery("UPDATE miscdeductions  SET amount  = 0 WHERE accregion='" + LoggedInRegion + "' and  year=" + iYear + "  and month=" + iMonth + " and deductionscode='" + deductioncode + "'").executeUpdate();
        transaction.commit();
        }*/
        if (code != null) {
            if (code.trim().length() > 0) {

                Criteria attCrit = session.createCriteria(Miscdeductions.class);
                attCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + code + "'"));
                attCrit.add(Restrictions.sqlRestriction("deductionscode='" + deductioncode + "'"));
                attCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                attCrit.add(Restrictions.sqlRestriction("year=" + iYear));

                List attList = attCrit.list();
                if (attList.size() > 0) {
                    Miscdeductions miscdeductionsObje = (Miscdeductions) attList.get(0);
                    miscdeductionsObje.setAmount(new BigDecimal(deductionAmount));
                    miscdeductionsObje.setCreatedby(LoggedInUser);
                    miscdeductionsObje.setCreateddate(getCurrentDate());

                    transaction = session.beginTransaction();
                    session.update(miscdeductionsObje);
                    transaction.commit();

                } else {
                    Miscdeductions miscdeductionsObje = new Miscdeductions();
                    String id = getMaxMiscDeductionid(session, LoggedInRegion);
                    miscdeductionsObje.setId(id);

                    miscdeductionsObje.setMonth(iMonth);
                    miscdeductionsObje.setYear(iYear);
                    miscdeductionsObje.setDeductionscode(deductioncode);
                    miscdeductionsObje.setEmployeemaster(getEmployeeDetails(session, code));
                    miscdeductionsObje.setAmount(new BigDecimal(deductionAmount));
                    miscdeductionsObje.setAccregion(LoggedInRegion);
                    miscdeductionsObje.setCreatedby(LoggedInUser);
                    miscdeductionsObje.setCreateddate(getCurrentDate());
                    transaction = session.beginTransaction();
                    session.save(miscdeductionsObje);
                    transaction.commit();


                }
            }
        }
        currentRecords = currentRecords + 1;
        resultMap.put("currentRecords", currentRecords);
        resultMap.put("totalrecords", totRec);
        return resultMap;
    }

    public synchronized String getMaxMiscDeductionid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getMiscdeductionsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setMiscdeductionsid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return maxStr;
    }

    public BigDecimal EarningsCalculation(Session session, String salaryStructureId, String earningMasterid, String processDate) {
        BigDecimal amount = new BigDecimal(0);
        float total = 0;
        float earamt = 0;
        Ccahra ccahraObj;
        Employeeearningsdetails employeeearningsdetailsObj;
        Criteria ccaHRA = session.createCriteria(Ccahra.class);
        ccaHRA.add(Restrictions.sqlRestriction("ccahra='" + earningMasterid + "'"));
        List ccaHRAList = ccaHRA.list();
        if (ccaHRAList.size() > 0) {
            for (int i = 0; i < ccaHRAList.size(); i++) {
                ccahraObj = (Ccahra) ccaHRAList.get(i);
                Criteria earCrit = session.createCriteria(Employeeearningsdetails.class);
                earCrit.add(Restrictions.sqlRestriction("salarystructureid='" + salaryStructureId + "'"));
                earCrit.add(Restrictions.sqlRestriction("earningmasterid ='" + ccahraObj.getPaycodemaster().getPaycode() + "'"));
                List earCritList = earCrit.list();
                if (earCritList.size() > 0) {
                    employeeearningsdetailsObj = (Employeeearningsdetails) earCritList.get(0);
                    total = total + employeeearningsdetailsObj.getAmount().floatValue();
                }

            }
        }
        String Dtformat1 = "dd/MM/yyyy";
        String Datestring = processDate;
        String salaryStructureOrder = null;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iDay = cal.get(Calendar.DATE);
        int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal.get(Calendar.YEAR);
        int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
        String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
//        String queryStr = " periodfrom<='" + fromDate + "' and " + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
        String querystr = " case when periodto is null then periodfrom <= '" + postgresDate(processDate) + "' else '" + postgresDate(processDate) + "' between "
                + "periodfrom and periodto end";
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

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeePayBillPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String sectionname, String year, String month, String filePath, String paymenttype) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
            // Get Number of Days in a Month
            Calendar calendar = Calendar.getInstance();
            int currentyear = Integer.valueOf(year);
            int currentmonth = Integer.valueOf(month);
            int currentdate = 1;
            calendar.set(currentyear, currentmonth - 1, currentdate);
            int no_of_days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

//            PayBillPrinter pbp = new PayBillPrinter();
            PayslipReport pr = new PayslipReport();
            PaySlipModel psm = new PaySlipModel();
            String EmployeeDetails_Query = null;
            int previousmonth = 0;
            int previousyear = 0;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;
            Map deductionMapfinal = new HashMap();
            Map earningMapfinal = new HashMap();

            if (paymenttype.equals("all")) {
                // <editor-fold>
                if ((epfno == null || epfno.equals("")) && (sectionname == null || sectionname.equals(""))) {
                    // <editor-fold>
                    EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.salarydays,pp.year,pp.month,em.employeename,em.dateofbirth, "
                            + "em.dateofappoinment,em.dateofconfirmation,rm.regionname,sn.sectionname,dm.designation,em.employeecode, pp.salarystructureid,"
                            + "dm.payscalecode, em.eslp, pp.subsection, em.banksbaccount, em.pancardno, rm.billssigningauthority from payrollprocessingdetails pp "
                            + "left join sectionmaster sn on pp.section=sn.id "
                            + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                            + "left join regionmaster rm on pp.accregion=rm.id "
                            + "left join designationmaster dm on pp.designation=dm.designationcode "
                            + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.process  is true "
                            + "order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";
                    // </editor-fold>
                } else if ((epfno != null || epfno.length() > 0) && (sectionname == null || sectionname.equals(""))) {
                    // <editor-fold>
                    EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.salarydays,pp.year,pp.month,em.employeename,em.dateofbirth, "
                            + "em.dateofappoinment,em.dateofconfirmation,rm.regionname,sn.sectionname,dm.designation,em.employeecode, pp.salarystructureid,"
                            + "dm.payscalecode, em.eslp, pp.subsection, em.banksbaccount, em.pancardno, rm.billssigningauthority from payrollprocessingdetails pp "
                            + "left join sectionmaster sn on pp.section=sn.id "
                            + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                            + "left join regionmaster rm on pp.accregion=rm.id "
                            + "left join designationmaster dm on pp.designation=dm.designationcode "
                            + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.process  is true "
                            + "and pp.employeeprovidentfundnumber='" + epfno + "' order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";
                    // </editor-fold>
                } else if ((epfno == null || epfno.equals("")) && (sectionname != null || sectionname.length() > 0)) {
                    // <editor-fold>
                    EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.salarydays,pp.year,pp.month,em.employeename,em.dateofbirth, "
                            + "em.dateofappoinment,em.dateofconfirmation,rm.regionname,sn.sectionname,dm.designation,em.employeecode, pp.salarystructureid,"
                            + "dm.payscalecode, em.eslp, pp.subsection, em.banksbaccount, em.pancardno, rm.billssigningauthority from payrollprocessingdetails pp "
                            + "left join sectionmaster sn on pp.section=sn.id "
                            + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                            + "left join regionmaster rm on pp.accregion=rm.id "
                            + "left join designationmaster dm on pp.designation=dm.designationcode "
                            + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.process  is true "
                            + "and pp.section='" + sectionname + "' order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";
                    // </editor-fold>
                } else if ((epfno != null || epfno.length() > 0) && (sectionname != null || sectionname.length() > 0)) {
                    // <editor-fold>
                    EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.salarydays,pp.year,pp.month,em.employeename,em.dateofbirth, "
                            + "em.dateofappoinment,em.dateofconfirmation,rm.regionname,sn.sectionname,dm.designation,em.employeecode, pp.salarystructureid,"
                            + "dm.payscalecode, em.eslp, pp.subsection, em.banksbaccount, em.pancardno, rm.billssigningauthority from payrollprocessingdetails pp "
                            + "left join sectionmaster sn on pp.section=sn.id "
                            + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                            + "left join regionmaster rm on pp.accregion=rm.id "
                            + "left join designationmaster dm on pp.designation=dm.designationcode "
                            + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.process  is true and pp.section='" + sectionname + "' "
                            + "and pp.employeeprovidentfundnumber='" + epfno + "' order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";
                    // </editor-fold>
                }
                // </editor-fold>
            } else if (paymenttype.equals("cash")) {
                // <editor-fold>
                EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.salarydays,pp.year,pp.month,em.employeename,em.dateofbirth,"
                        + "em.dateofappoinment,em.dateofconfirmation,rm.regionname,sn.sectionname,dm.designation,em.employeecode, pp.salarystructureid,"
                        + "dm.payscalecode, em.eslp, pp.subsection, em.banksbaccount, em.pancardno, rm.billssigningauthority from payrollprocessingdetails pp "
                        + "left join sectionmaster sn on pp.section=sn.id "
                        + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                        + "left join regionmaster rm on pp.accregion=rm.id "
                        + "left join designationmaster dm on pp.designation=dm.designationcode "
                        + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.process is true and pp.paymentmode='C' "
                        + "order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";
                // </editor-fold>
            } else if (paymenttype.equals("bank")) {
                // <editor-fold>
                EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.salarydays,pp.year,pp.month,em.employeename,em.dateofbirth,"
                        + "em.dateofappoinment,em.dateofconfirmation,rm.regionname,sn.sectionname,dm.designation,em.employeecode, pp.salarystructureid,"
                        + "dm.payscalecode, em.eslp, pp.subsection, em.banksbaccount, em.pancardno, rm.billssigningauthority from payrollprocessingdetails pp "
                        + "left join sectionmaster sn on pp.section=sn.id "
                        + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                        + "left join regionmaster rm on pp.accregion=rm.id "
                        + "left join designationmaster dm on pp.designation=dm.designationcode "
                        + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.process is true and pp.paymentmode='B' "
                        + "order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";
                // </editor-fold>
            }
            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            if (Integer.valueOf(month) == 1) {
                previousmonth = 11;
                previousyear = Integer.valueOf(year) - 1;
            } else {
                previousmonth = Integer.valueOf(month) - 2;
                previousyear = Integer.valueOf(year);
            }

            int slipno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                // <editor-fold>
                Object[] rows = (Object[]) its.next();
                psm.setPaymenttype(paymenttype);
                psm.setPfno((String) rows[0]);
                earn_dedu_id = ((String) rows[1]);
                if (rows[2] != null) {
                    psm.setPayday(rows[2].toString());
                } else {
                    psm.setPayday("");
                }
                psm.setPayslipyear(rows[3].toString());
                psm.setPayslipmonth(months[(Integer) rows[4] - 1]);
                psm.setPayslipmonthprevious(months[previousmonth]);
                psm.setPayslipyearprevious(String.valueOf(previousyear));
                psm.setEmployeename((String) rows[5]);
                if (rows[6] == null) {
                    psm.setDateofbirth("");
                } else {
                    String date = rows[6].toString();
                    psm.setDateofbirth(DateParser.convertTheDateIntoDisplayFormat(date));
                }
                if (rows[7] == null) {
                    psm.setDateofappointment("");
                } else {
                    String date = rows[7].toString();
                    psm.setDateofappointment(DateParser.convertTheDateIntoDisplayFormat(date));
                }
                if (rows[8] == null) {
                    psm.setDateofconformation("");
                } else {
                    String date = rows[8].toString();
                    psm.setDateofconformation(DateParser.convertTheDateIntoDisplayFormat(date));
                }
                psm.setBranch((String) rows[9]);
                psm.setSectionname((String) rows[10]);
                psm.setDesignation((String) rows[11]);
                psm.setEmpno((String) rows[12]);
                salarystructureid = ((String) rows[13]);
                if (rows[14] != null) {
                    psm.setPayscale((String) rows[14]);
                } else {
                    psm.setPayscale("");
                }
                if (rows[15] != null) {
                    String date = rows[15].toString();
                    psm.setDateofelsurrenderdate(DateParser.convertTheDateIntoDisplayFormat(date));
                } else {
                    psm.setDateofelsurrenderdate("");
                }

                if (rows[17] != null) {
                    String bankaccountno = (String) rows[17];
                    if (bankaccountno.equalsIgnoreCase("Null") || bankaccountno.equalsIgnoreCase("NULL")) {
                        psm.setBankaccountno("");
                    } else {
                        psm.setBankaccountno((String) rows[17]);
                    }
                } else {
                    psm.setBankaccountno("");
                }

                if (rows[18] != null) {
                    String pancardno = (String) rows[18];
                    if (pancardno.equalsIgnoreCase("Null") || pancardno.equalsIgnoreCase("NULL")) {
                        psm.setPancardno("");
                    } else {
                        psm.setPancardno((String) rows[18]);
                    }
                } else {
                    psm.setPancardno("");
                }
                psm.setSigningauthority((String) rows[19]);

                String Deduction_Query = "select pm.paycodename,edt.amount from  paycodemaster pm,employeedeductionstransactions edt where "
                        + "edt.payrollprocessingdetailsid = '" + earn_dedu_id + "' and edt.deductionmasterid = pm.paycode and cancelled is false order by pm.paycode";

                String Earnings_Query = "select pm.paycodename,eet.amount from  paycodemaster pm,employeeearningstransactions eet where "
                        + "eet.payrollprocessingdetailsid = '" + earn_dedu_id + "' and eet.earningmasterid = pm.paycode and cancelled is false order by pm.paycode";

                String Loan_Query = "select employeemaster.epfno,paycodemaster.paycodename, employeeloansandadvances.loanamount, "
                        + "employeeloansandadvancesdetails.loanbalance,employeeloansandadvances.totalinstallment, "
                        + "employeeloansandadvancesdetails.nthinstallment, employeeloansandadvances.currentinstallment "
                        + "from employeeloansandadvancesdetails "
                        + "left join payrollprocessingdetails on payrollprocessingdetails.id=employeeloansandadvancesdetails.payrollprocessingdetailsid "
                        + "left join employeeloansandadvances on employeeloansandadvances.id=employeeloansandadvancesdetails.employeeloansandadvancesid "
                        + "left join paycodemaster on paycodemaster.paycode=employeeloansandadvances.deductioncode "
                        + "left join employeemaster on employeemaster.epfno= payrollprocessingdetails.employeeprovidentfundnumber "
                        + "where "
                        + "payrollprocessingdetails.id='" + earn_dedu_id + "' and "
                        + "employeeloansandadvancesdetails.cancelled is false order by paycodemaster.paycode";

//                String Loan_Query = "select employeemaster.epfno,paycodemaster.paycodename, employeeloansandadvances.loanamount,"
//                        + "employeeloansandadvances.loanbalance,employeeloansandadvances.totalinstallment, employeeloansandadvances."
//                        + "currentinstallment from employeeloansandadvancesdetails left join payrollprocessingdetails on "
//                        + "payrollprocessingdetails.id=employeeloansandadvancesdetails.payrollprocessingdetailsid left join "
//                        + "employeeloansandadvances on employeeloansandadvances.id=employeeloansandadvancesdetails.employeeloansandadvancesid "
//                        + "left join paycodemaster on paycodemaster.paycode=employeeloansandadvances.deductioncode left join employeemaster "
//                        + "on employeemaster.epfno= payrollprocessingdetails.employeeprovidentfundnumber where payrollprocessingdetails.id='" + earn_dedu_id + "'  "
//                        + "and employeeloansandadvancesdetails.cancelled is false order by paycodemaster.paycode";

                String OrginalEarnings = "select eed.earningmasterid,eed.amount from employeeearningsdetails eed where eed.salarystructureid = '" + salarystructureid + "'";

                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);
                SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);
                SQLQuery loanquery = session.createSQLQuery(Loan_Query);
                SQLQuery originalearningsquery = session.createSQLQuery(OrginalEarnings);
                /*
                 * PayBillPrinter Model and Map Declaration
                 */

                Map<Integer, PaySlip_Earn_Deduction_Model> earn_ded_map = new HashMap<Integer, PaySlip_Earn_Deduction_Model>();
                Map<Integer, PaySlip_Earn_Deduction_Model> loanmap = new HashMap<Integer, PaySlip_Earn_Deduction_Model>();
                PaySlip_Earn_Deduction_Model psedm;

                List<PaySlip_Earn_Deduction_Model> deductionlist = new ArrayList<PaySlip_Earn_Deduction_Model>();
                List<PaySlip_Earn_Deduction_Model> earninglist = new ArrayList<PaySlip_Earn_Deduction_Model>();
                List<PaySlip_Earn_Deduction_Model> loanlist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                double sum = 0;
                String deductionStr = "";
                Map deductionMap = new HashMap();
                Map earningMap = new HashMap();
                Map dedStr = new HashMap();
                dedStr.put("E.P.F.", "E.P.F.");
                dedStr.put("H.I.S.", "H.I.S.");
                dedStr.put("BASIC PAY", "BASIC PAY");
                dedStr.put("H.R.A.", "H.R.A.");
                dedStr.put("D.A.", "D.A.");
                dedStr.put("GRADEPAY", "GRADEPAY");

                for (ListIterator it = deductionquery.list().listIterator(); it.hasNext();) {
                    // <editor-fold>
                    Object[] row = (Object[]) it.next();
                    psedm = new PaySlip_Earn_Deduction_Model();
                    deductionStr = (String) row[0].toString().trim();
                    psedm.setDeductionname(DeduStringSet(deductionStr));
                    if (dedStr.get(deductionStr) != null) {
                        if (deductionStr.equalsIgnoreCase(dedStr.get(deductionStr).toString())) {
                            deductionMap.put(deductionStr, deductionStr);
                        }
                    }
                    psedm.setDeductionamount((row[1]).toString());
                    sum += Double.valueOf(psedm.getDeductionamount());
                    deductionlist.add(psedm);
                    // </editor-fold>
                }
                if (deductionMap.size() != 2) {
                    deductionMap = new HashMap();
                    int siz = deductionMapfinal.size();
                    deductionMap.put("empno", psm.getPfno());
                    deductionMap.put("empname", psm.getEmployeename());
                    deductionMap.put("sectionname", psm.getSectionname());
                    deductionMap.put("designation", psm.getDesignation());
                    deductionMapfinal.put(siz + 1, deductionMap);
                }
                psm.setTotaldeductions(String.valueOf(decimalFormat.format(sum)));//Set Total Deduction Amount

                sum = 0;
                for (ListIterator it = earningsquery.list().listIterator(); it.hasNext();) {
                    // <editor-fold>
                    Object[] row = (Object[]) it.next();
                    psedm = new PaySlip_Earn_Deduction_Model();
                    deductionStr = (String) row[0].toString().trim();
                    psedm.setEarningsname(EarnStringSet(deductionStr));
                    if (dedStr.get(deductionStr) != null) {
                        if (deductionStr.equalsIgnoreCase(dedStr.get(deductionStr).toString())) {
                            earningMap.put(deductionStr, deductionStr);
                        }
                    }
                    psedm.setEarningsamount((row[1]).toString());
                    sum += Double.valueOf(psedm.getEarningsamount());
                    earninglist.add(psedm);
                    // </editor-fold>
                }
                if (earningMap.size() != 4) {
                    earningMap = new HashMap();
                    int siz = earningMapfinal.size();
                    earningMap.put("empno", psm.getPfno());
                    earningMap.put("empname", psm.getEmployeename());
                    earningMap.put("sectionname", psm.getSectionname());
                    earningMap.put("designation", psm.getDesignation());
                    earningMapfinal.put(siz + 1, earningMap);
                }
                psm.setTotalearnings(String.valueOf(decimalFormat.format(sum)));//Set Total Earnings Amount

                for (ListIterator it = loanquery.list().listIterator(); it.hasNext();) {
                    // <editor-fold>
                    Object[] row = (Object[]) it.next();
                    psedm = new PaySlip_Earn_Deduction_Model();
                    if (row[1] != null) {
                        psedm.setLoanname(LoanStringSet(row[1].toString()));
                    } else {
                        psedm.setLoanname("");
                    }
                    psedm.setLoanamount((row[2]).toString());
                    psedm.setLoanbalance((row[3]).toString());
                    psedm.setTotalinstallment((row[4]).toString());
                    psedm.setCurrentinstallment((row[5]).toString());
                    psedm.setInstallment(psedm.getCurrentinstallment() + "/" + psedm.getTotalinstallment());
                    loanlist.add(psedm);
                    // </editor-fold>
                }
                String basicpay = null;
                String da = null;
                String hra = null;
                String cca = null;
                String gradepay = null;

                for (ListIterator it = originalearningsquery.list().listIterator(); it.hasNext();) {
                    // <editor-fold>
                    Object[] row = (Object[]) it.next();
                    if (row[0].toString().equalsIgnoreCase("E01")) {
                        basicpay = row[1].toString();
                    }
                    if (row[0].toString().equalsIgnoreCase("E04")) {
                        da = row[1].toString();
                        if (da.equalsIgnoreCase("0.00")) {
                            String processmonth = nft.format(Integer.valueOf(p_month));
                            String process_Date = "01" + "/" + processmonth + "/" + p_year;
                            da = String.valueOf(decimalFormat.format(EarningsCalculation(session, salarystructureid, "E04", process_Date)));
                        }
                    }
                    if (row[0].toString().equalsIgnoreCase("E06")) {
                        hra = row[1].toString();
                        if (hra.equalsIgnoreCase("0.00")) {
                            String processmonth = nft.format(Integer.valueOf(p_month));
                            String process_Date = "01" + "/" + processmonth + "/" + p_year;
                            hra = String.valueOf(decimalFormat.format(EarningsCalculation(session, salarystructureid, "E06", process_Date)));
                        }
                    }
                    if (row[0].toString().equalsIgnoreCase("E07")) {
                        cca = row[1].toString();
                        if (cca.equalsIgnoreCase("0.00")) {
                            String processmonth = nft.format(Integer.valueOf(p_month));
                            String process_Date = "01" + "/" + processmonth + "/" + p_year;
                            cca = String.valueOf(decimalFormat.format(EarningsCalculation(session, salarystructureid, "E07", process_Date)));
                        }
                    }
                    if (row[0].toString().equalsIgnoreCase("E25")) {
                        gradepay = row[1].toString();
                        if (gradepay.equalsIgnoreCase("0.00")) {
                            String processmonth = nft.format(Integer.valueOf(p_month));
                            String process_Date = "01" + "/" + processmonth + "/" + p_year;
                            gradepay = String.valueOf(decimalFormat.format(EarningsCalculation(session, salarystructureid, "E25", process_Date)));
                        }
                    }
                    // </editor-fold>
                }

                if (basicpay != null) {
                    psm.setBasicpay(basicpay);
                } else {
                    psm.setBasicpay("");
                }
                if (da != null) {
                    psm.setDa(da);
                } else {
                    psm.setDa("");
                }
                if (hra != null) {
                    psm.setHra(hra);
                } else {
                    psm.setHra("");
                }
                if (cca != null) {
                    psm.setCca(cca);
                } else {
                    psm.setCca("");
                }
                if (gradepay != null) {
                    psm.setGrpay(gradepay);
                } else {
                    psm.setGrpay("");
                }

                double netsalary = Double.valueOf(psm.getTotalearnings()) - Double.valueOf(psm.getTotaldeductions());
                psm.setNetsalary(String.valueOf(decimalFormat.format(netsalary)));//Set Net Salary
                /**
                 * ********** Earnings and Deduction Map Generation Start
                 * ****************
                 */
                int emptylist;
                if (deductionlist.size() < 21) {
                    emptylist = 21 - (21 - deductionlist.size());
                    for (int i = emptylist; i < 21; i++) {
                        psedm = new PaySlip_Earn_Deduction_Model();
                        psedm.setDeductionname(" ");
                        psedm.setDeductionamount(" ");
                        deductionlist.add(i, psedm);
                    }
                }

                if (earninglist.size() < 21) {
                    emptylist = 21 - (21 - earninglist.size());
                    for (int i = emptylist; i < 21; i++) {
                        psedm = new PaySlip_Earn_Deduction_Model();
                        psedm.setEarningsname(" ");
                        psedm.setEarningsamount(" ");
                        earninglist.add(i, psedm);
                    }
                }

                for (int i = 0; i < 21; i++) {
                    PaySlip_Earn_Deduction_Model earn = earninglist.get(i);
                    PaySlip_Earn_Deduction_Model dedu = deductionlist.get(i);
                    psedm = new PaySlip_Earn_Deduction_Model();
                    psedm.setEarningsname(earn.getEarningsname());
                    psedm.setEarningsamount(earn.getEarningsamount());
                    psedm.setDeductionname(dedu.getDeductionname());
                    psedm.setDeductionamount(dedu.getDeductionamount());
                    earn_ded_map.put(i, psedm);
                }
                psm.setEarn_ded_map(earn_ded_map);//Set Earnings and Deduction Map

                /**
                 * ********** Earnings and Deduction Map Generation End
                 * ****************
                 */
                /**
                 * ********** Loan Map Generation Start ****************
                 */
                if (loanlist.size() < 13) {
                    emptylist = 13 - (13 - loanlist.size());
                    for (int i = emptylist; i < 13; i++) {
                        psedm = new PaySlip_Earn_Deduction_Model();
                        psedm.setLoanname(" ");
                        psedm.setLoanamount(" ");
                        psedm.setLoanbalance(" ");
                        psedm.setInstallment(" ");
                        loanlist.add(i, psedm);
                    }
                }

                for (int i = 0; i < 13; i++) {
                    PaySlip_Earn_Deduction_Model loan = loanlist.get(i);
                    loanmap.put(i, loan);
                }
                psm.setLoanmap(loanmap);//Set Loan,loan amount, loan Balance and Installment Map
                /**
                 * ********** Loan Map Generation End ****************
                 */
                psm.setSlipno(String.valueOf(slipno));
                //psm.setDistrict("CHENNAI");
                psm.setPincode("600 010");
//                psm.setPayscale("9300 - 34800");
//                psm.setDateofelsurrenderdate("");
                psm.setRate("");
                psm.setPayday(String.valueOf(no_of_days));
                if (Double.valueOf(psm.getNetsalary()) > 0) {
                    psm.setNetsalarywords(AmountInWords.convertAmountintoWords(psm.getNetsalary()));
                } else {
                    psm.setNetsalarywords(" ");
                }
                psm.setEl("0.0");
                psm.setUel("0.0");
                psm.setMl("0.0");
                psm.setLlp("0.0");
                psm.setCl("0.0");
//                if (earningsquery.list().size() == 0) {
//                    System.out.println("psm.getEmpno() ="+psm.getEmpno());
//                }else{
                pr.getPayBillPrintWriter(psm, filePath);
                slipno++;
//                }  
                // </editor-fold>
            }


            StringBuilder sb = new StringBuilder();
            sb.append("select em.epfno, em.employeename, dm.designation, sn.sectionname, rm.regionname, spd.reasoncode from employeemaster em ");
            sb.append("left join stoppayrolldetails spd on spd.epfno=em.epfno ");
            sb.append("left join sectionmaster sn on em.section=sn.id ");
            sb.append("left join regionmaster rm on em.region=rm.id ");
            sb.append("left join designationmaster dm on em.designation=dm.designationcode ");
            sb.append("where ");
            sb.append("em.process is false and em.region='" + LoggedInRegion + "' and spd.enddate is null order by em.epfno,em.section,em.designation");

            SQLQuery pncquery = session.createSQLQuery(sb.toString());
            String region = null;
            String period = months[Integer.valueOf(month) - 1] + "-" + year.substring(2, 4);
            List<PaySlipModel> pnclist = new ArrayList<PaySlipModel>();
            Map pncmap = new HashMap();
            int i = 1;
            if (pncquery.list().size() > 0) {
                for (ListIterator it1 = pncquery.list().listIterator(); it1.hasNext();) {
                    Object[] rs = (Object[]) it1.next();
                    String pfno = (String) rs[0];
                    String employeename = (String) rs[1];
                    String designation = (String) rs[2];
                    String section = (String) rs[3];
                    region = (String) rs[4];
                    String reason = "";
                    if (rs[5] != null) {
                        reason = (String) rs[5];
                    }

                    psm = new PaySlipModel();
                    psm.setSlipno(String.valueOf(i));
                    psm.setEpf(pfno);
                    psm.setEmployeename(employeename);
                    psm.setDesignation(SubString(designation, 10));
                    psm.setSectionname(SubString(section, 10));
                    psm.setRemarks(SubString(reason, 10));
                    pnclist.add(psm);
                    i++;
                }
                pncmap.put("pnclist", pnclist);
                pncmap.put("region", region);
                pncmap.put("period", period);
                pr.PNCSlip(pncmap, filePath);
            }

            if (deductionMapfinal.size() > 0) {
                i = 1;
                pncmap = new HashMap();
                pnclist = new ArrayList<PaySlipModel>();
                for (int loop = 1; loop <= deductionMapfinal.size(); loop++) {
                    psm = new PaySlipModel();
                    HashMap deductionMap = (HashMap) deductionMapfinal.get(loop);
                    psm.setSlipno(String.valueOf(i));
                    psm.setEpf((String) deductionMap.get("empno"));
                    psm.setEmployeename((String) deductionMap.get("empname"));
                    psm.setDesignation((String) deductionMap.get("designation"));
                    psm.setSectionname((String) deductionMap.get("sectionname"));
                    psm.setRemarks("Nil Dedn");
                    pnclist.add(psm);
                    i++;
                }
                pncmap.put("pnclist", pnclist);
                pncmap.put("region", region);
                pncmap.put("period", period);
                pncmap.put("heading", "DEDUCTION MISSING DETAIL LIST FOR THE MONTH OF " + period);
                pr.missingEarDedSlip(pncmap, filePath);
            }
            if (earningMapfinal.size() > 0) {
                i = 1;
                pncmap = new HashMap();
                pnclist = new ArrayList<PaySlipModel>();
                for (int loop = 1; loop <= earningMapfinal.size(); loop++) {
                    psm = new PaySlipModel();
                    HashMap earningMap = (HashMap) earningMapfinal.get(loop);
                    psm.setSlipno(String.valueOf(i));
                    psm.setEpf((String) earningMap.get("empno"));
                    psm.setEmployeename((String) earningMap.get("empname"));
                    psm.setDesignation((String) earningMap.get("designation"));
                    psm.setSectionname((String) earningMap.get("sectionname"));
                    psm.setRemarks("Nil Earn");
                    pnclist.add(psm);
                    i++;
                }
                pncmap.put("pnclist", pnclist);
                pncmap.put("region", region);
                pncmap.put("period", period);
                pncmap.put("heading", "EARNING MISSING DETAIL LIST FOR THE MONTH OF " + period);
                pr.missingEarDedSlip(pncmap, filePath);
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Paybill Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeePayBillDownloandFileCreation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName) {
        Map map = new HashMap();
        try {
            PrintWriter pw = null;
            File file = new File(filePathwithName);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.print(GetEmployeePayBillDetailsForDownloand(session, month, year, LoggedInRegion));
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map EmployeeSupplementaryBillPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String billno, String filePath) {
//        SupplementaryBillServiceImpl suppObj = new SupplementaryBillServiceImpl();
//
//        return suppObj.EmployeeSupplementaryBillPrintOut(session, epfno, billno, filePath);
//    }
    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeAcquitanceSlipPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String paymenttype) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            AcquitanceReport ar = new AcquitanceReport();
            PaySlipModel psm;
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            if (paymenttype.equals("all")) {

                EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,dm.designation,"
                        + "em.banksbaccount,pp.year,pp.month,pp.subsection from payrollprocessingdetails pp "
                        + "left join employeemaster em on em.epfno = pp.employeeprovidentfundnumber "
                        + "left join regionmaster rm on rm.id=pp.accregion "
                        + "left join sectionmaster sn on pp.section=sn.id "
                        + "left join designationmaster dm on pp.designation=dm.designationcode "
                        + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.process  is true "
                        + "order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";

//                EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,dm.designation,"
//                        + "em.banksbaccount,pp.year,pp.month,pp.subsection from payrollprocessingdetails pp,employeemaster em,regionmaster rm,sectionmaster sn,"
//                        + "designationmaster dm where em.epfno = pp.employeeprovidentfundnumber and pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion=rm.id and "
//                        + "pp.section=sn.id and em.designation=dm.designationcode and pp.process  is true and rm.defaultregion is true  order by "
//                        + "pp.section,pp.subsection,dm.orderno";

            } else if (paymenttype.equals("cash")) {

                EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,dm.designation,"
                        + "em.banksbaccount,pp.year,pp.month,pp.subsection from payrollprocessingdetails pp "
                        + "left join employeemaster em on em.epfno = pp.employeeprovidentfundnumber "
                        + "left join regionmaster rm on rm.id=pp.accregion "
                        + "left join sectionmaster sn on pp.section=sn.id "
                        + "left join designationmaster dm on pp.designation=dm.designationcode "
                        + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.process  is true and pp.paymentmode='C' "
                        + "order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";

//                EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,dm.designation,"
//                        + "em.banksbaccount,pp.year,pp.month,pp.subsection from payrollprocessingdetails pp,employeemaster em,regionmaster rm,"
//                        + "sectionmaster sn,designationmaster dm where em.epfno = pp.employeeprovidentfundnumber and pp.year=" + p_year + " and pp.month=" + p_month + " and "
//                        + "pp.accregion=rm.id and pp.section=sn.id and em.designation=dm.designationcode and pp.paymentmode='C' and "
//                        + "pp.process  is true and rm.defaultregion is true  order by pp.section,pp.subsection,dm.orderno";

            } else if (paymenttype.equals("bank")) {

                EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,dm.designation,"
                        + "em.banksbaccount,pp.year,pp.month,pp.subsection from payrollprocessingdetails pp "
                        + "left join employeemaster em on em.epfno = pp.employeeprovidentfundnumber "
                        + "left join regionmaster rm on rm.id=pp.accregion "
                        + "left join sectionmaster sn on pp.section=sn.id "
                        + "left join designationmaster dm on pp.designation=dm.designationcode "
                        + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.process  is true and pp.paymentmode='B' "
                        + "order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";

//                EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,dm.designation,"
//                        + "em.banksbaccount,pp.year,pp.month,pp.subsection from payrollprocessingdetails pp,employeemaster em,regionmaster rm,"
//                        + "sectionmaster sn,designationmaster dm where em.epfno = pp.employeeprovidentfundnumber and pp.year=" + p_year + " and pp.month=" + p_month + " and "
//                        + "pp.accregion=rm.id and pp.section=sn.id and em.designation=dm.designationcode and pp.paymentmode='B' and "
//                        + "pp.process  is true and rm.defaultregion is true  order by pp.section,pp.subsection,dm.orderno";

            }

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int printingrecordsize = employeequery.list().size();

            int slipno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                psm = new PaySlipModel();
                psm.setPaymenttype(paymenttype);
                Object[] rows = (Object[]) its.next();
                psm.setSlipno(String.valueOf(slipno));
                String earn_dedu_id = (String) rows[0];
                psm.setPfno((String) rows[1]);
                psm.setEmpno((String) rows[2]);
                psm.setEmployeename((String) rows[3]);
                if (rows[4] != null) {
                    psm.setFpfno((String) rows[4]);
                } else {
                    psm.setFpfno("");
                }
                psm.setBranch((String) rows[5]);
                psm.setSectionname(SecStringSet((String) rows[6]));
                psm.setDesignation((String) rows[7]);
                psm.setBankaccountno((String) rows[8]);
                psm.setPayslipyear(rows[9].toString());
                psm.setPayslipmonth(months[(Integer) rows[10] - 1]);

                String Deduction_Query = "select sum(amount) as deductionamount from employeedeductionstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                String Earnings_Query = "select sum(amount) as earningsamount from employeeearningstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);
                List deductionlist = deductionquery.list();
                if (deductionlist.get(0) != null) {
                    psm.setTotaldeductions(deductionlist.get(0).toString());
                } else {
                    psm.setTotaldeductions("0.00");
                }

                SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);
                List earningslist = earningsquery.list();
                if (earningslist.get(0) != null) {
                    psm.setTotalearnings(earningslist.get(0).toString());
                } else {
                    psm.setTotalearnings("0.00");
                }

                double netsalary = Double.valueOf(psm.getTotalearnings()) - Double.valueOf(psm.getTotaldeductions());
                psm.setNetsalary(String.valueOf(decimalFormat.format(netsalary)));//Set Net Salary
                psm.setPrintingrecordsize(printingrecordsize);
                ar.getAcquitanceSlipPrintWriter(psm, filePath);
                slipno++;
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Acquitance Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeEarningsLedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//            PayBillPrinter pbp = new PayBillPrinter();
            EarningsLedgerReport ledgerReport = new EarningsLedgerReport();
            PaySlipModel psm;
            Payrollprocessingdetails payrollprocessingdetails = new Payrollprocessingdetails();
            String EmployeeDetails_Query = null;
            PaySlip_Earn_Deduction_Model psedm = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,dm.designation,"
                    + "em.banksbaccount,pp.year,pp.month,pp.process,pp.remarks,pp.subsection from payrollprocessingdetails pp "
                    + "left join employeemaster em on em.epfno = pp.employeeprovidentfundnumber "
                    + "left join regionmaster rm on pp.accregion=rm.id "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' "
                    + "order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";

//            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,dm.designation,"
//                    + "em.banksbaccount,pp.year,pp.month,pp.process,pp.remarks,pp.subsection from payrollprocessingdetails pp,employeemaster em,"
//                    + "regionmaster rm,sectionmaster sn,designationmaster dm where em.epfno = pp.employeeprovidentfundnumber and "
//                    + "pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion=rm.id and pp.section=sn.id and em.designation=dm.designationcode and "
//                    + "rm.defaultregion is true order by pp.section,pp.subsection,dm.orderno";

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            int printingrecordsize = employeequery.list().size();
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Map<String, PaySlip_Earn_Deduction_Model> earningsmap = new ConcurrentHashMap<String, PaySlip_Earn_Deduction_Model>();
//                Map<String, PaySlip_Earn_Deduction_Model> copyearningsmap = new HashMap<String, PaySlip_Earn_Deduction_Model>();
                psm = new PaySlipModel();
                Object[] rows = (Object[]) its.next();
                boolean check = false;
                if (!Boolean.valueOf(rows[11].toString())) {
                    String remarks = (String) rows[12];
                    if (remarks.equalsIgnoreCase("UNDER SUSPENSION") || remarks.equalsIgnoreCase("NO REPORT") || remarks.equalsIgnoreCase("ON LEAVE")) {
                        check = true;
                    }
//                    if (((String) rows[12]).equals("ON LEAVE")) {
//                        check = true;
//                    }
                } else {
                    check = true;
                }

                if (check) {
                    psm.setSlipno(String.valueOf(slipno));

                    String earn_dedu_id = (String) rows[0];
                    psm.setPfno((String) rows[1]);
                    psm.setEmpno((String) rows[2]);
                    psm.setEmployeename((String) rows[3]);
                    if (rows[4] != null) {
                        psm.setFpfno((String) rows[4]);
                    } else {
                        psm.setFpfno("");
                    }
                    psm.setBranch((String) rows[5]);
                    psm.setSectionname((String) rows[6]);
                    psm.setDesignation((String) rows[7]);
                    psm.setBankaccountno((String) rows[8]);
                    psm.setPayslipyear(rows[9].toString());
                    psm.setPayslipmonth(months[(Integer) rows[10] - 1]);
                    psm.setProcess(Boolean.valueOf(rows[11].toString()));
                    psm.setRemarks((String) rows[12]);

                    String Earnings_Query = "select pm.paycode, pm.paycodename, eet.amount from  paycodemaster pm,employeeearningstransactions eet where "
                            + "eet.payrollprocessingdetailsid = '" + earn_dedu_id + "' and eet.earningmasterid = pm.paycode and cancelled is false order by pm.paycode";

                    String EarningsSum_Query = "select sum(amount) as earningsamount from employeeearningstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                    SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);

                    List<PaySlip_Earn_Deduction_Model> earninglist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                    for (ListIterator it = earningsquery.list().listIterator(); it.hasNext();) {
                        Object[] row = (Object[]) it.next();
                        psedm = new PaySlip_Earn_Deduction_Model();
                        String paycode = (String) row[0];
                        psedm.setEarningsname(LoanStringSet((String) row[1]));
                        psedm.setEarningsamount(row[2].toString());
                        earningsmap.put(paycode, psedm);
                    }

                    SQLQuery earningsSumquery = session.createSQLQuery(EarningsSum_Query);
                    List earningssumlist = earningsSumquery.list();
                    if (earningssumlist.get(0) != null) {
                        psm.setGrosssalary(earningssumlist.get(0).toString());
                    } else {
                        psm.setGrosssalary("0.00");
                    }

                    List<String> list = new ArrayList<String>();


                    for (String key : earningsmap.keySet()) {
                        if (key.equalsIgnoreCase("E01")) {
                            PaySlip_Earn_Deduction_Model ep = earningsmap.get(key);
                            psm.setBasicpay(ep.getEarningsamount());
                            list.add(key);
                        }
                        if (key.equalsIgnoreCase("E02")) {
                            PaySlip_Earn_Deduction_Model ep = earningsmap.get(key);
                            psm.setSplpay(ep.getEarningsamount());
                            list.add(key);
                        }
                        if (key.equalsIgnoreCase("E04")) {
                            PaySlip_Earn_Deduction_Model ep = earningsmap.get(key);
                            psm.setDa(ep.getEarningsamount());
                            list.add(key);
                        }
                        if (key.equalsIgnoreCase("E06")) {
                            PaySlip_Earn_Deduction_Model ep = earningsmap.get(key);
                            psm.setHra(ep.getEarningsamount());
                            list.add(key);
                        }
                        if (key.equalsIgnoreCase("E07")) {
                            PaySlip_Earn_Deduction_Model ep = earningsmap.get(key);
                            psm.setCca(ep.getEarningsamount());
                            list.add(key);
                        }
                    }
                    Iterator removeitr = list.iterator();
                    while (removeitr.hasNext()) {
                        String removekey = (String) removeitr.next();
                        earningsmap.remove(removekey);
                    }

                    List<PaySlip_Earn_Deduction_Model> earnlist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                    for (PaySlip_Earn_Deduction_Model pse : earningsmap.values()) {
                        earnlist.add(pse);
                    }

                    if (earnlist.size() < 3) {
                        for (int i = earnlist.size(); i < 3; i++) {
                            psedm = new PaySlip_Earn_Deduction_Model();
                            psedm.setEarningsname("");
                            psedm.setEarningsamount("");
                            earnlist.add(psedm);
                        }
                    }

                    psm.setOtherallowance(earnlist);

//                pbp.getAcquitanceSlipPrintWriter(psm, filePath);
                    //slipno++;
                    psm.setPrintingrecordsize(printingrecordsize);
                    ledgerReport.getEarningsLedgerPrintWriter(psm, filePath);
//                pbp.getEarningsLedgerPrintWriter(psm, filePath);
                    slipno++;
                }
            }
            ledgerReport.GrandTotal(filePath);
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Earnings Ledger Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeDeductionLedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//            PayBillPrinter pbp = new PayBillPrinter();
            DeductionLedgerReport ledgerReport = new DeductionLedgerReport();
            PaySlipModel psm = new PaySlipModel();
            String EmployeeDetails_Query = null;
            PaySlip_Earn_Deduction_Model psedm = null;
            PaySlip_Earn_Deduction_Model psedm1 = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);

            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,dm.designation,"
                    + "em.banksbaccount,pp.year,pp.month,pp.paymentmode,pp.subsection,pp.process,pp.remarks from payrollprocessingdetails pp "
                    + "left join employeemaster em on em.epfno = pp.employeeprovidentfundnumber "
                    + "left join regionmaster rm on pp.accregion=rm.id "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "'  "
                    + "order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";

//            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,dm.designation,"
//                    + "em.banksbaccount,pp.year,pp.month,pp.paymentmode,pp.subsection from payrollprocessingdetails pp,employeemaster em,regionmaster rm,"
//                    + "sectionmaster sn,designationmaster dm where em.epfno = pp.employeeprovidentfundnumber and pp.year=" + p_year + " "
//                    + "and pp.month=" + p_month + " and pp.accregion=rm.id and pp.section=sn.id and em.designation=dm.designationcode and "
//                    + "rm.defaultregion is true order by pp.section,pp.subsection,dm.orderno";

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            int printingrecordsize = employeequery.list().size();
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Map<String, PaySlip_Earn_Deduction_Model> deductionmap = new ConcurrentHashMap<String, PaySlip_Earn_Deduction_Model>();
                Object[] rows = (Object[]) its.next();

                boolean check = false;
                if (!Boolean.valueOf(rows[13].toString())) {
                    String remarks = (String) rows[14];
                    if (remarks.equalsIgnoreCase("UNDER SUSPENSION") || remarks.equalsIgnoreCase("NO REPORT") || remarks.equalsIgnoreCase("ON LEAVE")) {
                        check = true;
                    }
//                    if (((String) rows[14]).equals("ON LEAVE")) {
//                        check = true;
//                    }
                } else {
                    check = true;
                }

                if (check) {
                    psm.setSlipno(String.valueOf(slipno));

                    String earn_dedu_id = (String) rows[0];
                    psm.setPfno((String) rows[1]);
                    psm.setEmpno((String) rows[2]);
                    psm.setEmployeename((String) rows[3]);
                    if (rows[4] != null) {
                        psm.setFpfno((String) rows[4]);
                    } else {
                        psm.setFpfno("earn_dedu_id = " + earn_dedu_id);
                    }
                    psm.setBranch((String) rows[5]);
                    psm.setSectionname((String) rows[6]);
                    psm.setDesignation((String) rows[7]);
                    psm.setBankaccountno((String) rows[8]);
                    psm.setPayslipyear(rows[9].toString());
                    psm.setPayslipmonth(months[(Integer) rows[10] - 1]);
                    psm.setPaymentmode(rows[11].toString());
                    psm.setProcess(Boolean.valueOf(rows[13].toString()));
                    psm.setRemarks((String) rows[14]);

                    String Deduction_Query = "select pm.paycode,pm.paycodename,edt.amount from  paycodemaster pm,employeedeductionstransactions edt where "
                            + "edt.payrollprocessingdetailsid = '" + earn_dedu_id + "' and edt.deductionmasterid = pm.paycode and cancelled is false order by pm.paycode";

                    String DeductionSum_Query = "select sum(amount) as deductionamount from employeedeductionstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                    String EarningsSum_Query = "select sum(amount) as earningsamount from employeeearningstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                    SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);

                    List<PaySlip_Earn_Deduction_Model> earninglist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                    for (ListIterator it = deductionquery.list().listIterator(); it.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Voucher">
                        Object[] row = (Object[]) it.next();
                        psedm = new PaySlip_Earn_Deduction_Model();
                        String paycode = (String) row[0];

                        String LoanInstall_Query = "select employeeloansandadvances.deductioncode,employeeloansandadvances.totalinstallment,"
                                + "employeeloansandadvances.currentinstallment from employeeloansandadvancesdetails left join employeeloansandadvances on "
                                + "employeeloansandadvances.id=employeeloansandadvancesdetails.employeeloansandadvancesid where "
                                + "employeeloansandadvancesdetails.payrollprocessingdetailsid= '" + earn_dedu_id + "' and "
                                + "employeeloansandadvances.deductioncode='" + paycode + "'";

                        SQLQuery loanquery = session.createSQLQuery(LoanInstall_Query);

                        for (ListIterator itl = loanquery.list().listIterator(); itl.hasNext();) {
                            Object[] ro = (Object[]) itl.next();
                            if (ro[0] != null) {
                                psedm.setTotalinstallment(ro[1].toString());
                                psedm.setCurrentinstallment(ro[2].toString());
                            } else {
                                psedm.setTotalinstallment("");
                                psedm.setCurrentinstallment("");
                            }
                        }

                        psedm.setPaycode(paycode);

                        psedm.setDeductionname((String) row[1]);
                        psedm.setDeductionamount(row[2].toString());
                        deductionmap.put(paycode, psedm);
                        // </editor-fold>
                    }
                    String gpf = "";
                    String epf = "";
                    String vpf = "";
                    String spf = "";

                    Iterator<String> it1 = deductionmap.keySet().iterator();
                    while (it1.hasNext()) {
                        // <editor-fold defaultstate="collapsed" desc="Voucher">
                        String key = it1.next();
                        if (key.equalsIgnoreCase("D01")) {
                            PaySlip_Earn_Deduction_Model model = deductionmap.get("D01");
                            gpf = model.getDeductionamount();
                            deductionmap.remove("D01");
                        }
                        if (key.equalsIgnoreCase("D02")) {
                            PaySlip_Earn_Deduction_Model model = deductionmap.get("D02");
                            epf = model.getDeductionamount();
                            deductionmap.remove("D02");
                        }
                        if (key.equalsIgnoreCase("D03")) {
                            PaySlip_Earn_Deduction_Model model = deductionmap.get("D03");
                            vpf = model.getDeductionamount();
                            deductionmap.remove("D03");
                        }
                        if (key.equalsIgnoreCase("D04")) {
                            PaySlip_Earn_Deduction_Model model = deductionmap.get("D04");
                            spf = model.getDeductionamount();
                            deductionmap.remove("D04");
                        }
                        // </editor-fold>
                    }

                    psm.setGpf(gpf);
                    psm.setEpf(epf);
                    psm.setVpf(vpf);
                    psm.setSpf(spf);
                    SQLQuery deductionSumquery = session.createSQLQuery(DeductionSum_Query);
                    List deductionsumlist = deductionSumquery.list();
                    if (deductionsumlist.get(0) != null) {
                        psm.setTotaldeductions(deductionsumlist.get(0).toString());
                    } else {
                        psm.setTotaldeductions("0.00");
                    }

                    SQLQuery earningsSumquery = session.createSQLQuery(EarningsSum_Query);
                    List earningssumlist = earningsSumquery.list();
                    if (earningssumlist.get(0) != null) {
                        psm.setTotalearnings(earningssumlist.get(0).toString());
                    } else {
                        psm.setTotalearnings("0.00");
                    }

                    double netsalary = Double.valueOf(psm.getTotalearnings()) - Double.valueOf(psm.getTotaldeductions());
                    psm.setNetsalary(String.valueOf(decimalFormat.format(netsalary)));//Set Net Salary

                    if (psm.getPaymentmode().equals("C")) {
                        psm.setCashamount(psm.getNetsalary());
                        psm.setChequeamount("");
                    } else if (psm.getPaymentmode().equals("B")) {
                        psm.setCashamount("");
                        psm.setChequeamount(psm.getNetsalary());
                    }

                    Map<String, PaySlip_Earn_Deduction_Model> sorteddeductionmap = sortByKeys(deductionmap);
                    List<PaySlip_Earn_Deduction_Model> deductionlist = new ArrayList<PaySlip_Earn_Deduction_Model>();
                    for (PaySlip_Earn_Deduction_Model pse : sorteddeductionmap.values()) {
                        deductionlist.add(pse);
                    }

                    if (deductionlist.size() < 3) {
                        for (int i = deductionlist.size(); i < 3; i++) {
                            psedm = new PaySlip_Earn_Deduction_Model();
                            psedm.setDeductionname("");
                            psedm.setDeductionamount("");
                            psedm.setCurrentinstallment("");
                            psedm.setTotalinstallment("");
                            deductionlist.add(psedm);
                        }
                    }

                    psm.setTotalrecovery(deductionlist);
                    psm.setPrintingrecordsize(printingrecordsize);
                    ledgerReport.getDeductionLedgerPrintWriter(psm, filePath);
//                pbp.getDeductionLedgerPrintWriter(psm, filePath);
                    slipno++;
                }
            }
            ledgerReport.GrandPrint(psm, filePath);
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Deduction Ledger Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSalaryAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String paymenttype) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//            PayBillPrinter pbp = new PayBillPrinter();
            AbstractReport ar = new AbstractReport();
            PaySlipModel psm;
            String SectionDetails_Query = null;
            PaySlip_Earn_Deduction_Model psedm = null;
            PaySlip_Earn_Deduction_Model psedm1 = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
//            SectionDetails_Query = "select id,sectionname from sectionmaster order by id";
            SectionDetails_Query = "select sm.id,sm.sectionname from payrollprocessingdetails pr,sectionmaster sm "
                    + " where pr.month=" + month + " and pr.year=" + year + " and pr.process='t' and pr.accregion='" + LoggedInRegion + "' and pr.section=sm.id"
                    + " group by sm.id,sm.sectionname order by sm.id";

            SQLQuery sectionquery = session.createSQLQuery(SectionDetails_Query);

            String Region_Query = "select regionname from regionmaster where id='" + LoggedInRegion + "'";

            SQLQuery regionquery = session.createSQLQuery(Region_Query);

            List regionlist = regionquery.list();
            Object obj = regionlist.get(0);
            String regionname = (String) obj;
            if (sectionquery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int recordno = 1;
            int printingrecordsize = sectionquery.list().size();
            for (ListIterator its = sectionquery.list().listIterator(); its.hasNext();) {
                psm = new PaySlipModel();
                psm.setPaymenttype(paymenttype);
                Object[] rows = (Object[]) its.next();
                String section_id = (String) rows[0];
                psm.setSectionname(SecStringSet((String) rows[1]));
                psm.setRegion(regionname);

                String EarningsList_Query = null;
                String DeductionList_Query = null;
                String AbstractEarningList_Query = null;
                String AbstractDeductionList_Query = null;
                if (paymenttype.equals("cash")) {

                    EarningsList_Query = "select ppd.section, pcm.paycodename,sum(eet.amount),ppd.subsection from employeeearningstransactions eet "
                            + "left join  paycodemaster pcm on pcm.paycode=eet.earningmasterid left join  payrollprocessingdetails ppd on "
                            + "ppd.id=eet.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and ppd.section='" + section_id + "' and "
                            + "ppd.paymentmode='C' and eet.cancelled is false and ppd.accregion='" + LoggedInRegion + "' group by ppd.section,pcm.paycodename,"
                            + "ppd.subsection order by pcm.paycodename";

                    DeductionList_Query = "select ppd.section, pcm.paycodename,sum(edt.amount) from employeedeductionstransactions edt "
                            + "left join  paycodemaster pcm on pcm.paycode=edt.deductionmasterid left join  payrollprocessingdetails ppd on "
                            + "ppd.id=edt.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and ppd.section='" + section_id + "' "
                            + "and ppd.paymentmode='C' and edt.cancelled is false and ppd.accregion='" + LoggedInRegion + "' group by "
                            + "ppd.section,pcm.paycodename order by pcm.paycodename";

                    AbstractEarningList_Query = "select pcm.paycode,pcm.paycodename,sum(eet.amount) from employeeearningstransactions eet "
                            + "left join  paycodemaster pcm on   pcm.paycode=eet.earningmasterid left join  payrollprocessingdetails ppd on "
                            + "ppd.id=eet.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and ppd.paymentmode='C' "
                            + "and ppd.section not in('S13','S14') and eet.cancelled is false and ppd.accregion='" + LoggedInRegion + "' group by "
                            + "pcm.paycode,pcm.paycodename order by pcm.paycode";

                    AbstractDeductionList_Query = "select pcm.paycode,pcm.paycodename,sum(edt.amount) from employeedeductionstransactions edt "
                            + "left join  paycodemaster pcm on   pcm.paycode=edt.deductionmasterid left join  payrollprocessingdetails ppd on "
                            + "ppd.id=edt.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and ppd.paymentmode='C' and "
                            + "ppd.section not in('S13','S14') and edt.cancelled is false and ppd.accregion='" + LoggedInRegion + "' "
                            + "group by pcm.paycode,pcm.paycodename order by pcm.paycode";

                } else if (paymenttype.equals("bank")) {

                    EarningsList_Query = "select ppd.section, pcm.paycodename,sum(eet.amount),ppd.subsection from employeeearningstransactions eet "
                            + "left join  paycodemaster pcm on pcm.paycode=eet.earningmasterid left join  payrollprocessingdetails ppd on "
                            + "ppd.id=eet.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and ppd.section='" + section_id + "' and "
                            + "ppd.paymentmode='B' and eet.cancelled is false and ppd.accregion='" + LoggedInRegion + "' group by ppd.section,pcm.paycodename,"
                            + "ppd.subsection order by pcm.paycodename";

                    DeductionList_Query = "select ppd.section, pcm.paycodename,sum(edt.amount) from employeedeductionstransactions edt "
                            + "left join  paycodemaster pcm on pcm.paycode=edt.deductionmasterid left join  payrollprocessingdetails ppd on "
                            + "ppd.id=edt.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and ppd.section='" + section_id + "' "
                            + "and ppd.paymentmode='B' and edt.cancelled is false and ppd.accregion='" + LoggedInRegion + "' group by "
                            + "ppd.section,pcm.paycodename order by pcm.paycodename";

                    AbstractEarningList_Query = "select pcm.paycode,pcm.paycodename,sum(eet.amount) from employeeearningstransactions eet "
                            + "left join  paycodemaster pcm on   pcm.paycode=eet.earningmasterid left join  payrollprocessingdetails ppd on "
                            + "ppd.id=eet.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and ppd.paymentmode='B' "
                            + "and ppd.section not in('S13','S14') and eet.cancelled is false and ppd.accregion='" + LoggedInRegion + "' group by "
                            + "pcm.paycode,pcm.paycodename order by pcm.paycode";

                    AbstractDeductionList_Query = "select pcm.paycode,pcm.paycodename,sum(edt.amount) from employeedeductionstransactions edt "
                            + "left join  paycodemaster pcm on   pcm.paycode=edt.deductionmasterid left join  payrollprocessingdetails ppd on "
                            + "ppd.id=edt.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and ppd.paymentmode='B' and "
                            + "ppd.section not in('S13','S14') and edt.cancelled is false and ppd.accregion='" + LoggedInRegion + "' "
                            + "group by pcm.paycode,pcm.paycodename order by pcm.paycode";

                } else if (paymenttype.equals("all")) {

                    EarningsList_Query = "select ppd.section, pcm.paycodename,sum(eet.amount) from employeeearningstransactions eet "
                            + "left join  paycodemaster pcm on   pcm.paycode=eet.earningmasterid left join  payrollprocessingdetails ppd "
                            + "on ppd.id=eet.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and "
                            + "ppd.section='" + section_id + "' and eet.cancelled is false and ppd.accregion='" + LoggedInRegion + "' group by ppd.section,"
                            + "pcm.paycodename order by pcm.paycodename";

                    DeductionList_Query = "select ppd.section, pcm.paycodename,sum(edt.amount) from employeedeductionstransactions edt "
                            + "left join  paycodemaster pcm on   pcm.paycode=edt.deductionmasterid left join  payrollprocessingdetails ppd "
                            + "on ppd.id=edt.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and "
                            + "ppd.section='" + section_id + "' and edt.cancelled is false and ppd.accregion='" + LoggedInRegion + "' group by ppd.section,"
                            + "pcm.paycodename order by pcm.paycodename";

                    AbstractEarningList_Query = "select pcm.paycode,pcm.paycodename,sum(eet.amount) from employeeearningstransactions eet "
                            + "left join  paycodemaster pcm on   pcm.paycode=eet.earningmasterid left join  payrollprocessingdetails ppd "
                            + "on ppd.id=eet.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and "
                            + "ppd.section not in('S13','S14') and eet.cancelled is false and ppd.accregion='" + LoggedInRegion + "' group by pcm.paycode,"
                            + "pcm.paycodename order by pcm.paycode";

                    AbstractDeductionList_Query = "select pcm.paycode,pcm.paycodename,sum(edt.amount) from employeedeductionstransactions edt "
                            + "left join  paycodemaster pcm on   pcm.paycode=edt.deductionmasterid left join  payrollprocessingdetails ppd "
                            + "on ppd.id=edt.payrollprocessingdetailsid left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                            + "left join regionmaster rm on ppd.accregion=rm.id where ppd.month=" + p_month + " and ppd.year=" + p_year + " and "
                            + "ppd.section not in('S13','S14') and edt.cancelled is false and ppd.accregion='" + LoggedInRegion + "' group by pcm.paycode,"
                            + "pcm.paycodename order by pcm.paycode";

                }

                SQLQuery earningsquery = session.createSQLQuery(EarningsList_Query);

                SQLQuery deductionquery = session.createSQLQuery(DeductionList_Query);

                SQLQuery abstract_earningsquery = session.createSQLQuery(AbstractEarningList_Query);

                SQLQuery abstract_deductionquery = session.createSQLQuery(AbstractDeductionList_Query);

                int earningrecordsize = earningsquery.list().size();

                int deductionrecordsize = deductionquery.list().size();

                double earningstotal = 0;

                double deductiontotal = 0;

                List<PaySlip_Earn_Deduction_Model> earningslist = new ArrayList<PaySlip_Earn_Deduction_Model>();
                for (ListIterator ite = earningsquery.list().listIterator(); ite.hasNext();) {
                    Object[] rowe = (Object[]) ite.next();
                    //psm.setSectionname((String) rowe[0]);
                    psedm = new PaySlip_Earn_Deduction_Model();
                    String subearn = (String) rowe[1];
                    if (subearn != null) {
                        psedm.setEarningsname(SecStringSet(subearn));
                    } else {
                        psedm.setEarningsname(subearn);
                    }
                    psedm.setEarningsamount(rowe[2].toString());
                    earningstotal += Double.valueOf(psedm.getEarningsamount());
                    earningslist.add(psedm);
                }
                List<PaySlip_Earn_Deduction_Model> deductionlist = new ArrayList<PaySlip_Earn_Deduction_Model>();
                for (ListIterator itd = deductionquery.list().listIterator(); itd.hasNext();) {
                    Object[] rowd = (Object[]) itd.next();
//                        psm.setSectionname((String) rowd[0]);
                    psedm = new PaySlip_Earn_Deduction_Model();
                    String subded = (String) rowd[1];
                    if (subded != null) {
                        psedm.setDeductionname(SecStringSet(subded));
                    } else {
                        psedm.setDeductionname(subded);
                    }
                    psedm.setDeductionamount(rowd[2].toString());
                    deductiontotal += Double.valueOf(psedm.getDeductionamount());
                    deductionlist.add(psedm);
                }
                if (earningslist.size() < deductionlist.size()) {
                    int emptylist = deductionlist.size() - (deductionlist.size() - earningslist.size());
                    for (int i = emptylist; i < deductionlist.size(); i++) {
                        psedm = new PaySlip_Earn_Deduction_Model();
                        psedm.setEarningsname(" ");
                        psedm.setEarningsamount(" ");
                        earningslist.add(i, psedm);
                    }
                } else {
                    int emptylist = earningslist.size() - (earningslist.size() - deductionlist.size());
                    for (int i = emptylist; i < earningslist.size(); i++) {
                        psedm = new PaySlip_Earn_Deduction_Model();
                        psedm.setEarningsname(" ");
                        psedm.setEarningsamount(" ");
                        deductionlist.add(i, psedm);
                    }
                }
                double totalnetsalary = earningstotal - deductiontotal;
                psm.setPayslipyear(year);
                psm.setPayslipmonth(months[Integer.valueOf(month) - 1]);
                psm.setTotalearnings(decimalFormat.format(earningstotal));
                psm.setTotaldeductions(decimalFormat.format(deductiontotal));
                psm.setEarningslist(earningslist);
                psm.setDeductionlist(deductionlist);
                psm.setNetsalary(decimalFormat.format(totalnetsalary));
                psm.setNetsalarywords(AmountInWords.convertAmountintoWords(psm.getNetsalary()));
//                    psm.setGrandsalary(decimalFormat.format(grandsalary));
//                    psm.setGrandsalarywords(AmountInWords.convertAmountintoWords(psm.getGrandsalary()));
                psm.setPrintingrecordsize(printingrecordsize);
                psm.setRecordno(recordno);

                ar.getSalaryAbstractPrintWriter(psm, filePath);

                if (psm.getPrintingrecordsize() == psm.getRecordno()) {

                    psm = new PaySlipModel();

                    double abstractearningstotal = 0;
                    double abstractdeductiontotal = 0;

                    List<PaySlip_Earn_Deduction_Model> abstractearningslist = new ArrayList<PaySlip_Earn_Deduction_Model>();
                    for (ListIterator ite = abstract_earningsquery.list().listIterator(); ite.hasNext();) {
                        Object[] rowe = (Object[]) ite.next();
                        psedm = new PaySlip_Earn_Deduction_Model();
                        String subearn = (String) rowe[1];
                        if (subearn != null) {
                            psedm.setEarningsname(SecStringSet(subearn));
                        } else {
                            psedm.setEarningsname(subearn);
                        }
                        psedm.setEarningsamount(rowe[2].toString());
                        abstractearningstotal += Double.valueOf(psedm.getEarningsamount());
                        abstractearningslist.add(psedm);
                    }

                    List<PaySlip_Earn_Deduction_Model> abstractdeductionlist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                    for (ListIterator itd = abstract_deductionquery.list().listIterator(); itd.hasNext();) {
                        Object[] rowd = (Object[]) itd.next();
                        psedm = new PaySlip_Earn_Deduction_Model();
                        String subded = (String) rowd[1];
                        if (subded != null) {
                            psedm.setDeductionname(SecStringSet(subded));
                        } else {
                            psedm.setDeductionname(subded);
                        }
                        psedm.setDeductionamount(rowd[2].toString());
                        abstractdeductiontotal += Double.valueOf(psedm.getDeductionamount());
                        abstractdeductionlist.add(psedm);
                    }

                    if (abstractearningslist.size() < abstractdeductionlist.size()) {
                        int emptylist = abstractdeductionlist.size() - (abstractdeductionlist.size() - abstractearningslist.size());
                        for (int i = emptylist; i < abstractdeductionlist.size(); i++) {
                            psedm = new PaySlip_Earn_Deduction_Model();
                            psedm.setEarningsname(" ");
                            psedm.setEarningsamount(" ");
                            abstractearningslist.add(i, psedm);
                        }
                    } else {
                        int emptylist = abstractearningslist.size() - (abstractearningslist.size() - abstractdeductionlist.size());
                        for (int i = emptylist; i < abstractearningslist.size(); i++) {
                            psedm = new PaySlip_Earn_Deduction_Model();
                            psedm.setEarningsname(" ");
                            psedm.setEarningsamount(" ");
                            abstractdeductionlist.add(i, psedm);
                        }
                    }

                    double abstracttotalnetsalary = abstractearningstotal - abstractdeductiontotal;

                    psm.setSectionname("");
                    psm.setPayslipyear(year);
                    psm.setPayslipmonth(months[Integer.valueOf(month) - 1]);
                    psm.setTotalearnings(decimalFormat.format(abstractearningstotal));
                    psm.setTotaldeductions(decimalFormat.format(abstractdeductiontotal));
                    psm.setEarningslist(abstractearningslist);
                    psm.setDeductionlist(abstractdeductionlist);
                    psm.setNetsalary(decimalFormat.format(abstracttotalnetsalary));
                    psm.setNetsalarywords(AmountInWords.convertAmountintoWords(psm.getNetsalary()));
//                        psm.setGrandsalary(decimalFormat.format(grandsalary));
//                        psm.setGrandsalarywords(AmountInWords.convertAmountintoWords(psm.getGrandsalary()));
                    psm.setPrintingrecordsize(printingrecordsize);
                    psm.setRecordno(recordno);

                    ar.getSalaryAbstractPrintWriter(psm, filePath);
                }
                recordno++;
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Salary Abstract Slip Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeEPFformPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            EPFformReport epfr = new EPFformReport();
            PaySlipModel psm = new PaySlipModel();
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;

            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,"
                    + "dm.designation,em.employeecode,pp.section from payrollprocessingdetails pp "
                    + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno  "
                    + "left join regionmaster rm on rm.id=pp.accregion "
                    + "left join designationmaster dm on pp.designation=dm.designationcode"
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' "
                    + "and pp.section not in('S13','S14') and pp.process is true order by pp.section,pp.subsection,dm.orderno,pp.employeeprovidentfundnumber";

//            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,"
//                    + "dm.designation,em.employeecode,pp.section from payrollprocessingdetails pp, employeemaster em,regionmaster rm,designationmaster dm "
//                    + "where pp.employeeprovidentfundnumber = em.epfno and pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion=rm.id and "
//                    + "em.designation=dm.designationcode and pp.section not in('S13','S14') and rm.defaultregion is true and pp.process is true "
//                    + "order by pp.section,pp.subsection,dm.orderno";

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);

            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            int printingrecordsize = employeequery.list().size();
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                psm.setPfno((String) rows[0]);
                earn_dedu_id = ((String) rows[1]);
                psm.setPayslipyear(rows[2].toString());
                psm.setPayslipmonth(months[(Integer) rows[3] - 1]);
                psm.setEmployeename((String) rows[4]);
                psm.setBranch((String) rows[5]);
                psm.setDesignation((String) rows[6]);
                psm.setEmpno((String) rows[7]);
                psm.setSectionname((String) rows[8]);

//                String Deduction_Query = "select pm.paycodename,edt.deductionmasterid,edt.amount from employeedeductionstransactions edt,paycodemaster pm "
//                        + "where edt.payrollprocessingdetailsid='" + earn_dedu_id + "' and edt.cancelled is false and edt.deductionmasterid=pm.paycode";
//
//                String Earnings_Query = "select pm.paycodename,eet.earningmasterid,eet.amount from employeeearningstransactions  eet,"
//                        + "paycodemaster pm where eet.payrollprocessingdetailsid='" + earn_dedu_id + "' and eet.cancelled is false and eet.earningmasterid=pm.paycode";

                String Deduction_Query = "select edt.deductionmasterid,edt.amount from employeedeductionstransactions edt,paycodemaster pm "
                        + "where edt.payrollprocessingdetailsid='" + earn_dedu_id + "' and edt.cancelled is false and edt.deductionmasterid=pm.paycode";

                String Salary_Query = "select sum(edt.amount) salary from  employeeearningstransactions edt where edt.payrollprocessingdetailsid='" + earn_dedu_id + "' "
                        + "and edt.cancelled is false and edt.earningmasterid in (select paycode from ccahra where ccahra='D02') ";

//                String Earnings_Query = "select eet.earningmasterid,eet.amount from employeeearningstransactions  eet,"
//                        + "paycodemaster pm where eet.payrollprocessingdetailsid='" + earn_dedu_id + "' and eet.cancelled is false and eet.earningmasterid=pm.paycode";


                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);
                SQLQuery salaryquery = session.createSQLQuery(Salary_Query);
                /*
                 * PayBillPrinter Model and Map Declaration
                 */

                Map<String, String> earn_ded_map = new HashMap<String, String>();

                double epf = 0;
                double epfloan = 0;
                double vpf = 0;
                double Salary = 0;
                Salary = Math.E;
                double employertotal = 0;

                for (ListIterator it = deductionquery.list().listIterator(); it.hasNext();) {
                    Object[] row = (Object[]) it.next();
                    if (row[0] != null) {
                        String deductionname = (String) row[0];
                        String deductionamount = "0";
                        if (row[1] != null) {
                            deductionamount = row[1].toString();
                        } else {
                            deductionamount = "0";
                        }
                        earn_ded_map.put(deductionname, deductionamount);
                    }
                }

                Iterator itr = earn_ded_map.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry ex = (Entry) itr.next();
                }

                if (earn_ded_map.get("L02") != null) {
                    epfloan = Double.valueOf(earn_ded_map.get("L02"));
                    long roundepfloan = Math.round(epfloan);
                    psm.setEpfloan(String.valueOf(roundepfloan));
                } else {
                    psm.setEpfloan("0");
                }

                if (earn_ded_map.get("D03") != null) {
                    vpf = Double.valueOf(earn_ded_map.get("D03"));
                    long roundvpf = Math.round(vpf);
                    psm.setVpf(String.valueOf(roundvpf));
                } else {
                    psm.setVpf("0");
                }

                if (earn_ded_map.get("D02") != null) {
                    epf = Double.valueOf(earn_ded_map.get("D02"));
                    long roundepf = Math.round(epf);
                    psm.setEpf(String.valueOf(roundepf));
                } else {
                    psm.setEpf("0");
                }

                if (salaryquery.list().get(0) != null) {
                    BigDecimal Sal = (BigDecimal) salaryquery.list().get(0);
                    Salary = Sal.doubleValue();
                    long roundsalary = Math.round(Salary);
                    psm.setSalary(String.valueOf(roundsalary));
                } else {
                    long roundsalary = 0;
                    psm.setSalary(String.valueOf(roundsalary));
                }
                employertotal = epf + epfloan + vpf;
                long roundepf = Math.round(epf);
                long roundemployertotal = Math.round(employertotal);
                psm.setEmployertotal(String.valueOf(roundemployertotal));
                psm.setRemarkstotal(String.valueOf(roundepf));
                psm.setSlipno(String.valueOf(slipno));
                psm.setPrintingrecordsize(printingrecordsize);

                epfr.getEPFformPrintWriter(psm, filePath);
//                pbp.getEPFformPrintWriter(psm, filePath);
                slipno++;
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "EPF Form Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeEPFformDBFPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            EPFformDBFReport epfr = new EPFformDBFReport();
            PaySlipModel psm = null;
            String EmployeeDetails_Query = null;
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;


//            EmployeeDetails_Query = "select ppd.id, ppd.employeeprovidentfundnumber,em.employeecode,eet.earningmasterid,eet.amount from "
            EmployeeDetails_Query = "select ppd.id, left(split_part(ppd.employeeprovidentfundnumber, '/',1),6) as employeeprovidentfundnumber,"
                    + " left(split_part(em.employeecode,'/',1),6) as employeecode,eet.earningmasterid,eet.amount from "
                    + "employeeearningstransactions eet left join payrollprocessingdetails ppd on eet.payrollprocessingdetailsid=ppd.id "
                    + "left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber "
                    + "left join regionmaster rm on rm.id=ppd.accregion where ppd.year=" + p_year + " and ppd.month=" + p_month + " and "
                    + "ppd.process is true and em.epfno=ppd.employeeprovidentfundnumber and eet.payrollprocessingdetailsid = ppd.id and "
                    + "eet.cancelled is false and ppd.accregion='" + LoggedInRegion + "' order by ppd.section,ppd.subsection,em.designation";

//            EmployeeDetails_Query = "select ppd.id, ppd.employeeprovidentfundnumber,em.employeecode,eet.earningmasterid,eet.amount from "
//                    + "employeeearningstransactions eet left join payrollprocessingdetails ppd on eet.payrollprocessingdetailsid=ppd.id "
//                    + "left join employeemaster em on em.epfno=ppd.employeeprovidentfundnumber where ppd.year=" + p_year + " and ppd.month=" + p_month + " and "
//                    + "ppd.process is true and em.epfno=ppd.employeeprovidentfundnumber and eet.payrollprocessingdetailsid = ppd.id and "
//                    + "eet.cancelled is false order by ppd.section,ppd.subsection,em.designation";

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            double total = 0;
            int printingrecordsize = employeequery.list().size();
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                psm = new PaySlipModel();
                earn_dedu_id = ((String) rows[0]);
                psm.setPfno((String) rows[1]);
                if (rows[2] != null) {
                    psm.setEmpno((String) rows[2]);
                } else {
                    psm.setEmpno("");
                }
                psm.setPaycode((String) rows[3]);
                psm.setAmount(rows[4].toString());
                BigDecimal amo = (BigDecimal) rows[4];
                double amount = amo.doubleValue();
                total += amount;
//                psm.setAmount(String.valueOf(amount));
                epfr.getEPFformPrintWriter(psm, filePath);
//                pbp.getEPFformPrintWriter(psm, filePath);
                slipno++;
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "EPF Form (Flat file) Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeLICSchedulePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String reporttype) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            LICScheduleReport lICScheduleReport = new LICScheduleReport();
            PaySlipModel psm;
            PaySlip_Earn_Deduction_Model psedm;
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;

            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,dm.designation,em.employeecode,"
                    + "pp.salarystructureid from payrollprocessingdetails pp "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                    + "left join regionmaster rm on rm.id=pp.accregion "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' "
                    + "and pp.process  is true order by pp.section,pp.subsection,dm.orderno";

//            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,dm.designation,em.employeecode, "
//                    + "pp.salarystructureid from payrollprocessingdetails pp,sectionmaster sn, employeemaster em,regionmaster rm,"
//                    + "designationmaster dm where pp.employeeprovidentfundnumber = em.epfno and pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion=rm.id "
//                    + "and pp.section=sn.id and em.designation=dm.designationcode and pp.process  is true and rm.defaultregion is true order by "
//                    + "pp.section,pp.subsection,dm.orderno"; 

            String LICSchedule_Query = " select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,dm.designation,"
                    + " em.employeecode, pp.salarystructureid ,ed.deductionmasterid,ed.dedn_no,ed.amount"
                    + " from payrollprocessingdetails pp "
                    + " left join sectionmaster sn on pp.section=sn.id "
                    + " left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                    + " left join regionmaster rm on rm.id=pp.accregion "
                    + " left join designationmaster dm on pp.designation=dm.designationcode "
                    + " left join salarystructure as ss on ss.id=pp.salarystructureid "
                    + " left join employeedeductiondetails as ed on ss.id=ed.salarystructureid and ed.deductionmasterid in "
                    + " (select paycode from paycodemaster where grouphead = 'LIC') and ed.cancelled is false "
                    + " where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' "
                    + " and pp.process  is true and ed.deductionmasterid is not null  order by ed.dedn_no,pp.section,pp.subsection,dm.orderno ";

            if ("TXT".equalsIgnoreCase(reporttype)) {
                SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
                if (employeequery.list().size() == 0) {
                    map.put("ERROR", "There is no Record for the given Inputs");
                    return map;
                }
                int printingrecordsize = employeequery.list().size();
                int slipno = 1;
                int recordno = 1;
                for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    psm = new PaySlipModel();
                    psm.setRecordno(recordno);
                    psm.setPfno((String) rows[0]);
                    earn_dedu_id = ((String) rows[1]);
                    psm.setPayslipyear(rows[2].toString());
                    psm.setPayslipmonth(months[(Integer) rows[3] - 1]);
                    psm.setEmployeename((String) rows[4]);
                    psm.setBranch((String) rows[5]);
                    if (rows[6] != null) {
                        psm.setDesignation(DeduStringSet((String) rows[6]));
                    } else {
                        psm.setDesignation("");
                    }
                    psm.setEmpno((String) rows[7]);
                    salarystructureid = ((String) rows[8]);
                    psm.setPrintingrecordsize(printingrecordsize);

                    String LIC_Amount = "select deductionmasterid,amount from employeedeductionstransactions where "
                            + "cancelled is false and payrollprocessingdetailsid='" + earn_dedu_id + "' and deductionmasterid in "
                            + "(select paycode from paycodemaster where grouphead = 'LIC')";

                    String LIC_POLICY_Query = "select employeedeductiondetails.deductionmasterid,employeedeductiondetails.dedn_no from employeedeductiondetails "
                            + "left join salarystructure on salarystructure.id=employeedeductiondetails.salarystructureid where salarystructure.id='" + salarystructureid + "' "
                            + "and employeedeductiondetails.deductionmasterid in (select paycode from paycodemaster where grouphead = 'LIC') "
                            + "and employeedeductiondetails.cancelled is false";

                    SQLQuery licAmountquery = session.createSQLQuery(LIC_Amount);

                    SQLQuery licPolicyquery = session.createSQLQuery(LIC_POLICY_Query);

                    if (licAmountquery.list().size() > 0) {

                        Map<String, String> licmap = new HashMap<String, String>();

                        for (ListIterator it = licPolicyquery.list().listIterator(); it.hasNext();) {
                            Object[] row = (Object[]) it.next();
                            String deductionmasterid = (String) row[0];
                            String policyno = (String) row[1];
                            licmap.put(deductionmasterid, policyno);
                        }

                        List<PaySlip_Earn_Deduction_Model> liclist = new ArrayList<PaySlip_Earn_Deduction_Model>();
                        for (ListIterator it = licAmountquery.list().listIterator(); it.hasNext();) {
                            psedm = new PaySlip_Earn_Deduction_Model();
                            Object[] row = (Object[]) it.next();
                            String paycode = (String) row[0];
                            String amount = row[1].toString();
                            if (licmap.get(paycode) != null) {
                                psedm.setLicpolicyno(licmap.get(paycode));
                            } else {
                                psedm.setLicpolicyno("");
                            }
                            psedm.setLicamount(amount);
                            liclist.add(psedm);
                        }
                        psm.setSlipno(String.valueOf(slipno));
                        psm.setLiclist(liclist);
                        lICScheduleReport.getLICSchedulePrintWriter(psm, filePath);
                        slipno++;
                    }
                    recordno++;
                }
            } else if ("EXL".equalsIgnoreCase(reporttype)) {
                List<PaySlipModel> contentlist = new ArrayList<PaySlipModel>();
                SQLQuery employeequery = session.createSQLQuery(LICSchedule_Query);
                if (employeequery.list().size() == 0) {
                    map.put("ERROR", "There is no Record for the given Inputs");
                    return map;
                }
                int slipno = 1;
                for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    psm = new PaySlipModel();
                    psm.setSlipno(String.valueOf(slipno));
                    psm.setPfno((String) rows[0]);
                    earn_dedu_id = ((String) rows[1]);
                    psm.setPayslipyear(rows[2].toString());
                    psm.setPayslipmonth(months[(Integer) rows[3] - 1]);
                    psm.setEmployeename((String) rows[4]);
                    psm.setBranch((String) rows[5]);
                    if (rows[6] != null) {
                        psm.setDesignation(DeduStringSet((String) rows[6]));
                    } else {
                        psm.setDesignation("");
                    }
                    psm.setEmpno((String) rows[7]);
                    salarystructureid = ((String) rows[8]);
                    psm.setLlp((String) rows[9]);
                    psm.setPlino((String) rows[10]);
                    psm.setAmount(rows[11].toString());
                    contentlist.add(psm);
                    slipno++;
                }
                map.put("contentlist", contentlist);
                map.put("regionname", LoggedInRegion);
                map.put("year", p_year);
                map.put("month", p_month);
                lICScheduleReport.GenerateLICEmployeeDetailsexcel(map, filePath);
            }
            map.put("ERROR", null);
        } catch (Exception ex) {
            map.put("ERROR", "LIC Schedule Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeHBASchedulePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            HBAScheduleReport hBAScheduleReport = new HBAScheduleReport();
            PaySlipModel psm;
            PaySlip_Earn_Deduction_Model psedm, psedm1;
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            String[] hbacodes = {"L07", "L08", "L25", "L36", "L37", "L38"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;
            double totalhba;

            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,"
                    + "sn.sectionname,dm.designation,em.employeecode from payrollprocessingdetails pp "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno  "
                    + "left join regionmaster rm on pp.accregion=rm.id "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' order by pp.section,dm.orderno";

//            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,"
//                    + "sn.sectionname,dm.designation,em.employeecode from payrollprocessingdetails pp,sectionmaster sn, employeemaster em,"
//                    + "regionmaster rm,designationmaster dm where pp.employeeprovidentfundnumber = em.epfno and pp.year=" + p_year + " and pp.month=" + p_month + " "
//                    + "and pp.accregion=rm.id and pp.section=sn.id and em.designation=dm.designationcode and rm.defaultregion is true "
//                    + "order by pp.section,dm.orderno";
//
            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int printingrecordsize = employeequery.list().size();
            int slipno = 1;
            int recordno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                psm = new PaySlipModel();
                psm.setPfno((String) rows[0]);
                earn_dedu_id = ((String) rows[1]);
                psm.setPayslipyear(rows[2].toString());
                psm.setPayslipmonth(months[(Integer) rows[3] - 1]);
                psm.setEmployeename((String) rows[4]);
                psm.setBranch((String) rows[5]);
                psm.setSectionname((String) rows[6]);
                if (rows[7] != null) {
                    psm.setDesignation(DeduStringSet((String) rows[7]));
                } else {
                    psm.setDesignation("");
                }
                psm.setEmpno((String) rows[8]);
                psm.setPrintingrecordsize(printingrecordsize);

                String HBA_Query = "select pm.paycode,pm.paycodename,edt.amount from employeedeductionstransactions edt,paycodemaster pm where "
                        + "edt.payrollprocessingdetailsid='" + earn_dedu_id + "' and pm.paycode = edt.deductionmasterid and pm.grouphead='HBA' and edt.cancelled is false";

                String HBA_Installment_Query = "select ela.deductioncode,elad.nthinstallment,ela.totalinstallment from "
                        + "employeeloansandadvances ela,employeeloansandadvancesdetails elad where ela.employeeprovidentfundnumber='" + psm.getPfno() + "' "
                        + "and elad.employeeloansandadvancesid=ela.id and elad.payrollprocessingdetailsid='" + earn_dedu_id + "' and elad.cancelled is false";

                SQLQuery hbainstallmentquery = session.createSQLQuery(HBA_Installment_Query);
                Map<String, PaySlip_Earn_Deduction_Model> dedmap = new HashMap<String, PaySlip_Earn_Deduction_Model>();
                for (ListIterator inst = hbainstallmentquery.list().listIterator(); inst.hasNext();) {
                    psedm1 = new PaySlip_Earn_Deduction_Model();
                    Object[] ro = (Object[]) inst.next();
                    String pcode = (String) ro[0];
                    psedm1.setCurrentinstallment(ro[1].toString());
                    psedm1.setTotalinstallment(ro[2].toString());
                    dedmap.put(pcode, psedm1);
                }

                SQLQuery hbaquery = session.createSQLQuery(HBA_Query);
                if (hbaquery.list().size() > 0) {

                    totalhba = 0;
                    Map<String, PaySlip_Earn_Deduction_Model> hbamap = new HashMap<String, PaySlip_Earn_Deduction_Model>();

                    for (ListIterator it = hbaquery.list().listIterator(); it.hasNext();) {
                        psedm = new PaySlip_Earn_Deduction_Model();
                        Object[] row = (Object[]) it.next();
                        String paycode = (String) row[0];
                        String paycodename = (String) row[1];
                        String amount = row[2].toString();

                        psedm.setPaycode(paycode);
                        psedm.setPaycodename(paycodename);
                        psedm.setDeductionamount(amount);
                        totalhba += Double.valueOf(psedm.getDeductionamount());
                        PaySlip_Earn_Deduction_Model pedm = dedmap.get(psedm.getPaycode());
                        psedm.setCurrentinstallment(pedm.getCurrentinstallment());
                        psedm.setTotalinstallment(pedm.getTotalinstallment());
                        psedm.setInstallment("(" + psedm.getCurrentinstallment() + "/" + psedm.getTotalinstallment() + ")");
                        hbamap.put(psedm.getPaycode(), psedm);
                    }
                    psm.setSlipno(String.valueOf(slipno));
                    for (int i = 0; i < hbacodes.length; i++) {
                        if (hbamap.get(hbacodes[i]) == null) {
                            psedm = new PaySlip_Earn_Deduction_Model();
                            psedm.setPaycode(hbacodes[i]);
                            psedm.setPaycodename("");
                            psedm.setDeductionamount("0.00");
                            psedm.setCurrentinstallment("");
                            psedm.setTotalinstallment("");
                            psedm.setInstallment("");
                            hbamap.put(psedm.getPaycode(), psedm);
                        }
                    }
                    psm.setDeduction_map(hbamap);
                    psm.setTotalhba(String.valueOf(decimalFormat.format(totalhba)));
                    psm.setRecordno(recordno);
                    hBAScheduleReport.getHBASchedulePrintWriter(psm, filePath);
                    slipno++;
                }
                if (recordno == psm.getPrintingrecordsize()) {
                    hBAScheduleReport.getHBAScheduleGrandTotal(filePath);
                } else {
                    recordno++;
                }
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "HBA Schedule Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeHBAScheduleConsolidatedPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
            HBAScheduleReport hBAScheduleReport = new HBAScheduleReport();
            PaySlipModel psm;
            PaySlip_Earn_Deduction_Model psedm, psedm1;
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            String[] hbacodes = {"L07", "L08", "L25", "L36", "L37", "L38"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;
            double totalhba;

            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,"
                    + "sn.sectionname,dm.designation,em.employeecode from payrollprocessingdetails pp "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                    + "left join regionmaster rm on pp.accregion=rm.id "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' order by pp.section,dm.orderno";

//            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,"
//                    + "sn.sectionname,dm.designation,em.employeecode from payrollprocessingdetails pp,sectionmaster sn, employeemaster em,"
//                    + "regionmaster rm,designationmaster dm where pp.employeeprovidentfundnumber = em.epfno and pp.year=" + p_year + " and pp.month=" + p_month + " "
//                    + "and pp.accregion=rm.id and pp.section=sn.id and em.designation=dm.designationcode and rm.defaultregion is true "
//                    + "order by pp.section,dm.orderno";
//
            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int printingrecordsize = employeequery.list().size();
            int slipno = 1;
            int recordno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                psm = new PaySlipModel();
                psm.setPfno((String) rows[0]);
                earn_dedu_id = ((String) rows[1]);
                psm.setPayslipyear(rows[2].toString());
                psm.setPayslipmonth(months[(Integer) rows[3] - 1]);
                psm.setEmployeename((String) rows[4]);
                psm.setBranch((String) rows[5]);
                psm.setSectionname((String) rows[6]);
                if (rows[7] != null) {
                    psm.setDesignation(DeduStringSet((String) rows[7]));
                } else {
                    psm.setDesignation("");
                }
                psm.setEmpno((String) rows[8]);
                psm.setPrintingrecordsize(printingrecordsize);

                String HBA_Query = "select pm.paycode,pm.paycodename,edt.amount from employeedeductionstransactions edt,paycodemaster pm where "
                        + "edt.payrollprocessingdetailsid='" + earn_dedu_id + "' and pm.paycode = edt.deductionmasterid and pm.grouphead='HBA' and edt.cancelled is false";

                String HBA_Installment_Query = "select ela.deductioncode,elad.nthinstallment,ela.totalinstallment from "
                        + "employeeloansandadvances ela,employeeloansandadvancesdetails elad where ela.employeeprovidentfundnumber='" + psm.getPfno() + "' "
                        + "and elad.employeeloansandadvancesid=ela.id and elad.payrollprocessingdetailsid='" + earn_dedu_id + "' and elad.cancelled is false";

                SQLQuery hbainstallmentquery = session.createSQLQuery(HBA_Installment_Query);
                Map<String, PaySlip_Earn_Deduction_Model> dedmap = new HashMap<String, PaySlip_Earn_Deduction_Model>();
                for (ListIterator inst = hbainstallmentquery.list().listIterator(); inst.hasNext();) {
                    psedm1 = new PaySlip_Earn_Deduction_Model();
                    Object[] ro = (Object[]) inst.next();
                    String pcode = (String) ro[0];
                    psedm1.setCurrentinstallment(ro[1].toString());
                    psedm1.setTotalinstallment(ro[2].toString());
                    dedmap.put(pcode, psedm1);
                }

                SQLQuery hbaquery = session.createSQLQuery(HBA_Query);
                if (hbaquery.list().size() > 0) {

                    totalhba = 0;
                    Map<String, PaySlip_Earn_Deduction_Model> hbamap = new HashMap<String, PaySlip_Earn_Deduction_Model>();

                    for (ListIterator it = hbaquery.list().listIterator(); it.hasNext();) {
                        psedm = new PaySlip_Earn_Deduction_Model();
                        Object[] row = (Object[]) it.next();
                        String paycode = (String) row[0];
                        String paycodename = (String) row[1];
                        String amount = row[2].toString();

                        psedm.setPaycode(paycode);
                        psedm.setPaycodename(paycodename);
                        psedm.setDeductionamount(amount);
                        totalhba += Double.valueOf(psedm.getDeductionamount());
                        PaySlip_Earn_Deduction_Model pedm = dedmap.get(psedm.getPaycode());
                        psedm.setCurrentinstallment(pedm.getCurrentinstallment());
                        psedm.setTotalinstallment(pedm.getTotalinstallment());
                        psedm.setInstallment("(" + psedm.getCurrentinstallment() + "/" + psedm.getTotalinstallment() + ")");
                        hbamap.put(psedm.getPaycode(), psedm);
                    }
                    psm.setSlipno(String.valueOf(slipno));
                    for (int i = 0; i < hbacodes.length; i++) {
                        if (hbamap.get(hbacodes[i]) == null) {
                            psedm = new PaySlip_Earn_Deduction_Model();
                            psedm.setPaycode(hbacodes[i]);
                            psedm.setPaycodename("");
                            psedm.setDeductionamount("0.00");
                            psedm.setCurrentinstallment("");
                            psedm.setTotalinstallment("");
                            psedm.setInstallment("");
                            hbamap.put(psedm.getPaycode(), psedm);
                        }
                    }
                    psm.setDeduction_map(hbamap);
                    psm.setTotalhba(String.valueOf(decimalFormat.format(totalhba)));
                    psm.setRecordno(recordno);
                    hBAScheduleReport.getHBASchedulePrintWriter(psm, filePath);
                    slipno++;
                }
                if (recordno == psm.getPrintingrecordsize()) {
                    hBAScheduleReport.getHBAScheduleGrandTotal(filePath);
                } else {
                    recordno++;
                }
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "HBA Schedule Consolidated Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeHBAInterestSchedulePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            HBAScheduleInterestReport hBAScheduleInterestReport = new HBAScheduleInterestReport();
            PaySlipModel psm;
            PaySlip_Earn_Deduction_Model psedm, psedm1;
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            String[] hbacodes = {"L23", "L30", "L39", "L40"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;
            double totalhba;

            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,"
                    + "sn.sectionname,dm.designation,em.employeecode from payrollprocessingdetails pp "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                    + "left join regionmaster rm on pp.accregion=rm.id "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' order by pp.section,dm.orderno";

//            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,"
//                    + "sn.sectionname,dm.designation,em.employeecode from payrollprocessingdetails pp,sectionmaster sn, employeemaster em,"
//                    + "regionmaster rm,designationmaster dm where pp.employeeprovidentfundnumber = em.epfno and pp.year=" + p_year + " and "
//                    + "pp.month=" + p_month + " and pp.accregion=rm.id and pp.section=sn.id and em.designation=dm.designationcode and rm.defaultregion is true "
//                    + "order by pp.section,dm.orderno";
//
            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int printingrecordsize = employeequery.list().size();
            int slipno = 1;
            int recordno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                psm = new PaySlipModel();
                psm.setPfno((String) rows[0]);
                earn_dedu_id = ((String) rows[1]);
                psm.setPayslipyear(rows[2].toString());
                psm.setPayslipmonth(months[(Integer) rows[3] - 1]);
                psm.setEmployeename((String) rows[4]);
                psm.setBranch((String) rows[5]);
                psm.setSectionname((String) rows[6]);
                if (rows[7] != null) {
                    psm.setDesignation(DeduStringSet((String) rows[7]));
                } else {
                    psm.setDesignation("");
                }
                psm.setEmpno((String) rows[8]);
                psm.setPrintingrecordsize(printingrecordsize);

                String HBA_Query = "select pm.paycode,pm.paycodename,edt.amount from employeedeductionstransactions edt,paycodemaster pm where "
                        + "edt.payrollprocessingdetailsid='" + earn_dedu_id + "' and pm.paycode = edt.deductionmasterid and pm.grouphead='HBAINT' and edt.cancelled is false";

                String HBA_Installment_Query = "select ela.deductioncode,elad.nthinstallment,ela.totalinstallment from "
                        + "employeeloansandadvances ela,employeeloansandadvancesdetails elad where ela.employeeprovidentfundnumber='" + psm.getPfno() + "' "
                        + "and elad.employeeloansandadvancesid=ela.id and elad.payrollprocessingdetailsid='" + earn_dedu_id + "' and elad.cancelled is false";

                SQLQuery hbainstallmentquery = session.createSQLQuery(HBA_Installment_Query);
                Map<String, PaySlip_Earn_Deduction_Model> dedmap = new HashMap<String, PaySlip_Earn_Deduction_Model>();
                for (ListIterator inst = hbainstallmentquery.list().listIterator(); inst.hasNext();) {
                    psedm1 = new PaySlip_Earn_Deduction_Model();
                    Object[] ro = (Object[]) inst.next();
                    String pcode = (String) ro[0];
                    psedm1.setCurrentinstallment(ro[1].toString());
                    psedm1.setTotalinstallment(ro[2].toString());
                    dedmap.put(pcode, psedm1);
                }

                SQLQuery hbaquery = session.createSQLQuery(HBA_Query);
                if (hbaquery.list().size() > 0) {

                    totalhba = 0;
                    Map<String, PaySlip_Earn_Deduction_Model> hbamap = new HashMap<String, PaySlip_Earn_Deduction_Model>();

                    for (ListIterator it = hbaquery.list().listIterator(); it.hasNext();) {
                        psedm = new PaySlip_Earn_Deduction_Model();
                        Object[] row = (Object[]) it.next();
                        String paycode = (String) row[0];
                        String paycodename = (String) row[1];
                        String amount = row[2].toString();

                        psedm.setPaycode(paycode);
                        psedm.setPaycodename(paycodename);
                        psedm.setDeductionamount(amount);
                        totalhba += Double.valueOf(psedm.getDeductionamount());
                        PaySlip_Earn_Deduction_Model pedm = dedmap.get(psedm.getPaycode());
                        if (pedm != null) {
                            psedm.setCurrentinstallment(pedm.getCurrentinstallment());
                            psedm.setTotalinstallment(pedm.getTotalinstallment());
                            psedm.setInstallment("(" + psedm.getCurrentinstallment() + "/" + psedm.getTotalinstallment() + ")");
                        } else {
                            psedm.setCurrentinstallment("");
                            psedm.setTotalinstallment("");
                            psedm.setInstallment("(" + "" + "/" + "" + ")");
                        }
                        hbamap.put(psedm.getPaycode(), psedm);
                    }
                    psm.setSlipno(String.valueOf(slipno));
                    for (int i = 0; i < hbacodes.length; i++) {
                        if (hbamap.get(hbacodes[i]) == null) {
                            psedm = new PaySlip_Earn_Deduction_Model();
                            psedm.setPaycode(hbacodes[i]);
                            psedm.setPaycodename("");
                            psedm.setDeductionamount("0.00");
                            psedm.setCurrentinstallment("");
                            psedm.setTotalinstallment("");
                            psedm.setInstallment("");
                            hbamap.put(psedm.getPaycode(), psedm);
                        }
                    }
                    psm.setDeduction_map(hbamap);
                    psm.setTotalhba(String.valueOf(decimalFormat.format(totalhba)));
                    psm.setRecordno(recordno);
                    hBAScheduleInterestReport.getHBASchedulePrintWriter(psm, filePath);
                    slipno++;
                }
                if (recordno == psm.getPrintingrecordsize()) {
                    hBAScheduleInterestReport.getHBAScheduleGrandTotal(filePath);
                } else {
                    recordno++;
                }
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "HBA Schedule Interest Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeVehicleAdvancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            VehicleAdvanceReport vehicleAdvanceReport = new VehicleAdvanceReport();
            PaySlipModel psm;
            PaySlip_Earn_Deduction_Model psedm, psedm1;
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            String[] vehiclecodes = {"L09", "L10", "L11", "L21", "L22", "L32"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;
            double totalhba;

            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,"
                    + "sn.sectionname,dm.designation,em.employeecode from payrollprocessingdetails pp "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                    + "left join regionmaster rm on pp.accregion=rm.id "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' order by pp.section,dm.orderno";

//            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber,pp.id,pp.year,pp.month,em.employeename,rm.regionname,"
//                    + "sn.sectionname,dm.designation,em.employeecode from payrollprocessingdetails pp,sectionmaster sn, employeemaster em,"
//                    + "regionmaster rm,designationmaster dm where pp.employeeprovidentfundnumber = em.epfno and pp.year=" + p_year + " and "
//                    + "pp.month=" + p_month + " and pp.accregion=rm.id and pp.section=sn.id and em.designation=dm.designationcode and rm.defaultregion is true "
//                    + "order by pp.section,dm.orderno";
//
            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int printingrecordsize = employeequery.list().size();
            int slipno = 1;
            int recordno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                psm = new PaySlipModel();
                psm.setPfno((String) rows[0]);
                earn_dedu_id = ((String) rows[1]);
                psm.setPayslipyear(rows[2].toString());
                psm.setPayslipmonth(months[(Integer) rows[3] - 1]);
                psm.setEmployeename((String) rows[4]);
                psm.setBranch((String) rows[5]);
                psm.setSectionname((String) rows[6]);
                if (rows[7] != null) {
                    psm.setDesignation(DeduStringSet((String) rows[7]));
                } else {
                    psm.setDesignation("");
                }
                psm.setEmpno((String) rows[8]);
                psm.setPrintingrecordsize(printingrecordsize);

                String VEHICLE_Query = "select pm.paycode,pm.paycodename,edt.amount from employeedeductionstransactions edt,paycodemaster pm where "
                        + "edt.payrollprocessingdetailsid='" + earn_dedu_id + "' and pm.paycode = edt.deductionmasterid and pm.grouphead='VEHI' and edt.cancelled is false";

                String VEHICLE_Advance_Query = "select ela.deductioncode,elad.nthinstallment,ela.totalinstallment from "
                        + "employeeloansandadvances ela,employeeloansandadvancesdetails elad where ela.employeeprovidentfundnumber='" + psm.getPfno() + "' "
                        + "and elad.employeeloansandadvancesid=ela.id and elad.payrollprocessingdetailsid='" + earn_dedu_id + "' and elad.cancelled is false";

                SQLQuery vehicleadvancequery = session.createSQLQuery(VEHICLE_Advance_Query);
                Map<String, PaySlip_Earn_Deduction_Model> dedmap = new HashMap<String, PaySlip_Earn_Deduction_Model>();
                for (ListIterator inst = vehicleadvancequery.list().listIterator(); inst.hasNext();) {
                    psedm1 = new PaySlip_Earn_Deduction_Model();
                    Object[] ro = (Object[]) inst.next();
                    String pcode = (String) ro[0];
                    psedm1.setCurrentinstallment(ro[1].toString());
                    psedm1.setTotalinstallment(ro[2].toString());
                    dedmap.put(pcode, psedm1);
                }

                SQLQuery vehiclequery = session.createSQLQuery(VEHICLE_Query);
                if (vehiclequery.list().size() > 0) {

                    totalhba = 0;
                    Map<String, PaySlip_Earn_Deduction_Model> hbamap = new HashMap<String, PaySlip_Earn_Deduction_Model>();

                    for (ListIterator it = vehiclequery.list().listIterator(); it.hasNext();) {
                        psedm = new PaySlip_Earn_Deduction_Model();
                        Object[] row = (Object[]) it.next();
                        String paycode = (String) row[0];
                        String paycodename = (String) row[1];
                        String amount = row[2].toString();

                        psedm.setPaycode(paycode);
                        psedm.setPaycodename(paycodename);
                        psedm.setDeductionamount(amount);
                        totalhba += Double.valueOf(psedm.getDeductionamount());
                        PaySlip_Earn_Deduction_Model pedm = dedmap.get(psedm.getPaycode());
                        psedm.setCurrentinstallment(pedm.getCurrentinstallment());
                        psedm.setTotalinstallment(pedm.getTotalinstallment());
                        psedm.setInstallment("(" + psedm.getCurrentinstallment() + "/" + psedm.getTotalinstallment() + ")");
                        hbamap.put(psedm.getPaycode(), psedm);
                    }
                    psm.setSlipno(String.valueOf(slipno));
                    for (int i = 0; i < vehiclecodes.length; i++) {
                        if (hbamap.get(vehiclecodes[i]) == null) {
                            psedm = new PaySlip_Earn_Deduction_Model();
                            psedm.setPaycode(vehiclecodes[i]);
                            psedm.setPaycodename("");
                            psedm.setDeductionamount("0.00");
                            psedm.setCurrentinstallment("");
                            psedm.setTotalinstallment("");
                            psedm.setInstallment("");
                            hbamap.put(psedm.getPaycode(), psedm);
                        }
                    }
                    psm.setDeduction_map(hbamap);
                    psm.setTotalhba(String.valueOf(decimalFormat.format(totalhba)));
                    psm.setRecordno(recordno);
                    vehicleAdvanceReport.getVehicleAdvancePrintWriter(psm, filePath);
                    slipno++;
                }
                if (recordno == psm.getPrintingrecordsize()) {
                    vehicleAdvanceReport.getVehicleAdvanceGrandTotal(filePath);
                } else {
                    recordno++;
                }
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Vehicle Advance Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeePLISchedulePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            PLIScheduleReport pLIScheduleReport = new PLIScheduleReport();
            PaySlipModel psm;
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;

            EmployeeDetails_Query = "select ppd.id,em.epfno,em.employeename,rm.regionname,dm.designation,ppd.year,ppd.month from payrollprocessingdetails ppd "
                    + "left join employeemaster em on em.epfno = ppd.employeeprovidentfundnumber "
                    + "left join designationmaster dm on dm.designationcode = ppd.designation "
                    + "left join regionmaster rm on ppd.accregion=rm.id where ppd.year=" + p_year + " and ppd.month=" + p_month + " and ppd.accregion='" + LoggedInRegion + "' "
                    + "order by ppd.section,dm.orderno";

//            EmployeeDetails_Query = "select ppd.id,em.epfno,em.employeename,rm.regionname,dm.designation,ppd.year,ppd.month from "
//                    + "employeemaster em, payrollprocessingdetails ppd,designationmaster dm,regionmaster rm where em.epfno = ppd.employeeprovidentfundnumber "
//                    + "and ppd.year=" + p_year + " and ppd.month=" + p_month + " and dm.designationcode = em.designation and rm.id=ppd.accregion and rm.defaultregion is true "
//                    + "order by ppd.section,dm.orderno";
//
            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
//            System.out.println("employeequery.list().size()" + employeequery.list().size());
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int printingrecordsize = employeequery.list().size();
            int slipno = 1;
            int recordno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                psm = new PaySlipModel();
                earn_dedu_id = ((String) rows[0]);
                psm.setPfno((String) rows[1]);
                psm.setEmployeename((String) rows[2]);
                psm.setBranch((String) rows[3]);
                psm.setDesignation((String) rows[4]);
                psm.setPayslipyear(rows[5].toString());
                psm.setPayslipmonth(months[(Integer) rows[6] - 1]);
                psm.setPrintingrecordsize(printingrecordsize);

                String PLICode_Query = "select paycode,paycodename from paycodemaster where grouphead='PLI' order by paycode";

                SQLQuery plicodequery = session.createSQLQuery(PLICode_Query);

                if (plicodequery.list().size() > 0) {
                    for (ListIterator it = plicodequery.list().listIterator(); it.hasNext();) {
                        Object[] row = (Object[]) it.next();
                        String paycode = ((String) row[0]);
                        String paycodename = ((String) row[1]);

                        String PLIAmount_Query = "select deductionmasterid,amount from employeedeductionstransactions "
                                + "where payrollprocessingdetailsid='" + earn_dedu_id + "' and deductionmasterid='" + paycode + "' and cancelled is false";

                        SQLQuery pliamountquery = session.createSQLQuery(PLIAmount_Query);

                        if (pliamountquery.list().size() > 0) {

                            String amount = "";
                            String plino = "";

                            for (ListIterator it1 = pliamountquery.list().listIterator(); it1.hasNext();) {
                                Object[] row1 = (Object[]) it1.next();
                                paycode = ((String) row1[0]);
                                amount = (row1[1].toString());
                            }

                            String PLINO_Query = "select salarystructureactual.employeeprovidentfundnumber,employeedeductiondetailsactual.dedn_no "
                                    + "from salarystructureactual left join employeedeductiondetailsactual on employeedeductiondetailsactual."
                                    + "salarystructureactualid=salarystructureactual.id where salarystructureactual.periodto is null and "
                                    + "salarystructureactual.employeeprovidentfundnumber='" + psm.getPfno() + "'and employeedeductiondetailsactual.deductionmasterid='" + paycode + "' "
                                    + "and employeedeductiondetailsactual.cancelled is false";

                            SQLQuery plinoquery = session.createSQLQuery(PLINO_Query);

                            if (plinoquery.list().size() > 0) {

                                for (ListIterator it2 = plinoquery.list().listIterator(); it2.hasNext();) {
                                    Object[] row2 = (Object[]) it2.next();
                                    String pfno = ((String) row2[0]);
                                    if (row2[0] != null) {
                                        plino = ((String) row2[1]);
                                    } else {
                                        plino = "";
                                    }
                                    psm.setPlino(plino);
                                    psm.setAmount(amount);
                                    psm.setSlipno(String.valueOf(slipno));
//                                    System.out.println(psm.getSlipno() + "\t" + psm.getPfno() + "\t" + psm.getEmployeename() + "\t" + psm.getDesignation() + "\t" + psm.getPlino() + "\t" + psm.getAmount());
                                    pLIScheduleReport.getPLISchedulePrintWriter(psm, filePath);
                                    slipno++;
                                }
                            }
                        }
                    }
                }
                if (recordno == psm.getPrintingrecordsize()) {
                    pLIScheduleReport.getPLIScheduleGrandTotalPrintWriter(psm, filePath);
                } else {
                    recordno++;
                }
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "PLI Interest Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeBankFloppyPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String paymenttype) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            BankFloppyReport floppyReport = new BankFloppyReport();
            PaySlipModel psm;
            String EmployeeDetails_Query = null;
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);

            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,"
                    + "dm.designation,em.banksbaccount,pp.year,pp.month,pp.section from payrollprocessingdetails pp "
                    + "left join employeemaster em on em.epfno = pp.employeeprovidentfundnumber "
                    + "left join regionmaster rm on pp.accregion=rm.id "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.paymentmode='" + paymenttype + "' and pp.process is true "
                    + "order by pp.section,dm.orderno";

//            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,"
//                    + "dm.designation,em.banksbaccount,pp.year,pp.month,pp.section from payrollprocessingdetails pp,employeemaster em,"
//                    + "regionmaster rm,sectionmaster sn,designationmaster dm where em.epfno = pp.employeeprovidentfundnumber and pp.year=" + p_year + " "
//                    + "and pp.month=" + p_month + " and rm.id=pp.accregion and pp.section=sn.id and em.designation=dm.designationcode and "
//                    + "pp.paymentmode='" + paymenttype + "' and pp.process  is true and rm.defaultregion is true  order by pp.section,dm.orderno";
//

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int printingrecordsize = employeequery.list().size();

            int slipno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                psm = new PaySlipModel();
                psm.setPaymenttype(paymenttype);
                Object[] rows = (Object[]) its.next();
                psm.setSlipno(String.valueOf(slipno));
                String earn_dedu_id = (String) rows[0];
                psm.setPfno((String) rows[1]);
                psm.setEmpno((String) rows[2]);
                psm.setEmployeename((String) rows[3]);
                if (rows[4] != null) {
                    psm.setFpfno((String) rows[4]);
                } else {
                    psm.setFpfno("");
                }
                psm.setBranch((String) rows[5]);
                psm.setSectionname(SecStringSet((String) rows[6]));
                psm.setDesignation((String) rows[7]);
                if (rows[8] == null) {
                    psm.setBankaccountno("");
                } else {
                    psm.setBankaccountno((String) rows[8]);
                }
                psm.setPayslipyear(rows[9].toString());
                psm.setPayslipmonth(months[(Integer) rows[10] - 1]);
                psm.setSectioncode((String) rows[11]);

                String Deduction_Query = "select sum(amount) as deductionamount from employeedeductionstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                String Earnings_Query = "select sum(amount) as earningsamount from employeeearningstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);
                List deductionlist = deductionquery.list();
                if (deductionlist.get(0) != null) {
                    psm.setTotaldeductions(deductionlist.get(0).toString());
                } else {
                    psm.setTotaldeductions("0.00");
                }

                SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);
                List earningslist = earningsquery.list();
                if (earningslist.get(0) != null) {
                    psm.setTotalearnings(earningslist.get(0).toString());
                } else {
                    psm.setTotalearnings("0.00");
                }

                double netsalary = Double.valueOf(psm.getTotalearnings()) - Double.valueOf(psm.getTotaldeductions());
                psm.setNetsalary(String.valueOf(decimalFormat.format(netsalary)));//Set Net Salary
                psm.setPrintingrecordsize(printingrecordsize);
                floppyReport.getBankfloppyPrintWriter(psm, filePath);
                slipno++;
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Bank Floppy Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeBankTextPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String paymenttype) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            BankTextReport btr = new BankTextReport();
            PaySlipModel psm;
            String EmployeeDetails_Query = null;
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);

            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,"
                    + "dm.designation,em.banksbaccount,pp.year,pp.month,pp.section from payrollprocessingdetails pp "
                    + "left join employeemaster em on em.epfno = pp.employeeprovidentfundnumber "
                    + "left join regionmaster rm on pp.accregion=rm.id "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.paymentmode='" + paymenttype + "' "
                    + "and pp.process is true order by pp.section,dm.orderno";

//            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,"
//                    + "dm.designation,em.banksbaccount,pp.year,pp.month,pp.section from payrollprocessingdetails pp,employeemaster em,"
//                    + "regionmaster rm,sectionmaster sn,designationmaster dm where em.epfno = pp.employeeprovidentfundnumber and pp.year=" + p_year + " "
//                    + "and pp.month=" + p_month + " and rm.id=pp.accregion and pp.section=sn.id and em.designation=dm.designationcode and "
//                    + "pp.paymentmode='" + paymenttype + "' and pp.process  is true and rm.defaultregion is true order by pp.section,dm.orderno";
//

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int printingrecordsize = employeequery.list().size();

            int slipno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                psm = new PaySlipModel();
                psm.setPaymenttype(paymenttype);
                Object[] rows = (Object[]) its.next();
                psm.setSlipno(String.valueOf(slipno));
                String earn_dedu_id = (String) rows[0];
                psm.setPfno((String) rows[1]);
                psm.setEmpno((String) rows[2]);
                psm.setEmployeename((String) rows[3]);
                if (rows[4] != null) {
                    psm.setFpfno((String) rows[4]);
                } else {
                    psm.setFpfno("");
                }
                psm.setBranch((String) rows[5]);
                psm.setSectionname(SecStringSet((String) rows[6]));
                psm.setDesignation((String) rows[7]);
                if (rows[8] == null) {
                    psm.setBankaccountno("");
                } else {
                    psm.setBankaccountno((String) rows[8]);
                }
                psm.setPayslipyear(rows[9].toString());
                psm.setPayslipmonth(months[(Integer) rows[10] - 1]);
                psm.setSectioncode((String) rows[11]);

                String Deduction_Query = "select sum(amount) as deductionamount from employeedeductionstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                String Earnings_Query = "select sum(amount) as earningsamount from employeeearningstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);
                List deductionlist = deductionquery.list();
                if (deductionlist.get(0) != null) {
                    psm.setTotaldeductions(deductionlist.get(0).toString());
                } else {
                    psm.setTotaldeductions("0.00");
                }

                SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);
                List earningslist = earningsquery.list();
                if (earningslist.get(0) != null) {
                    psm.setTotalearnings(earningslist.get(0).toString());
                } else {
                    psm.setTotalearnings("0.00");
                }

                double netsalary = Double.valueOf(psm.getTotalearnings()) - Double.valueOf(psm.getTotaldeductions());
                psm.setNetsalary(String.valueOf(decimalFormat.format(netsalary)));//Set Net Salary
                psm.setPrintingrecordsize(printingrecordsize);
                btr.getBankTextPrintWriter(psm, filePath);
                slipno++;
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Acquitance Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeBankTextChequePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String paymenttype, String chequeno) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            BankTextReport btr = new BankTextReport();
            PaySlipModel psm;
            String EmployeeDetails_Query = null;
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);

            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,"
                    + "dm.designation,em.banksbaccount,pp.year,pp.month,pp.section from payrollprocessingdetails pp "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join regionmaster rm on pp.accregion=rm.id "
                    + "left join employeemaster em on em.epfno = pp.employeeprovidentfundnumber "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where pp.year=" + p_year + " and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' "
                    + "and pp.paymentmode='" + paymenttype + "' and pp.process is true order by pp.section, cast(dm.orderno as numeric)";

//            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,"
//                    + "dm.designation,em.banksbaccount,pp.year,pp.month,pp.section from payrollprocessingdetails pp,employeemaster em,"
//                    + "regionmaster rm,sectionmaster sn,designationmaster dm where em.epfno = pp.employeeprovidentfundnumber and pp.year=" + p_year + " "
//                    + "and pp.month=" + p_month + " and pp.accregion='" + LoggedInRegion + "' and pp.section=sn.id and em.designation=dm.designationcode and "
//                    + "pp.paymentmode='" + paymenttype + "' and pp.process  is true and rm.id='" + LoggedInRegion + "' order by pp.section,dm.orderno";

//            EmployeeDetails_Query = "select pp.id,em.epfno,em.employeecode,em.employeename,em.fpfno,rm.regionname,sn.sectionname,"
//                    + "dm.designation,em.banksbaccount,pp.year,pp.month,pp.section from payrollprocessingdetails pp,employeemaster em,"
//                    + "regionmaster rm,sectionmaster sn,designationmaster dm where em.epfno = pp.employeeprovidentfundnumber and pp.year=" + p_year + " "
//                    + "and pp.month=" + p_month + " and rm.id=pp.accregion and pp.section=sn.id and em.designation=dm.designationcode and "
//                    + "pp.paymentmode='" + paymenttype + "' and pp.process  is true and rm.defaultregion is true order by pp.section,dm.orderno";
//

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int printingrecordsize = employeequery.list().size();

            int slipno = 1;

            int recordno = 1;

            StringTokenizer st = new StringTokenizer(chequeno, "@@");
            List list = new ArrayList();
            while (st.hasMoreElements()) {
                String row = (String) st.nextElement();
                if (!(row.trim()).equals("##")) {
                    String sp[] = row.split("##");
                    long start = 0, end = 0;
                    if (sp[0] != null) {
                        start = Integer.valueOf(sp[0]);
                    }
                    if (sp.length > 1) {
                        if (sp[1] != null) {
                            end = Integer.valueOf(sp[1]);
                        }
                    }
                    if (start > 0) {
                        if (end > 0) {
                            for (long i = start; i <= end; i++) {
                                list.add(i);
                                recordno++;
                            }
                        } else {
                            long endno = start + (printingrecordsize - recordno);
                            for (long i = start; i <= endno; i++) {
                                list.add(i);
                            }
                        }
                    }
                }
            }

            if (printingrecordsize > list.size()) {
                map.put("ERROR", "Enter the Valid Ending Check no (or) Ending Checkno should be empty!");
                return map;
            }
            int i = 0;

            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                psm = new PaySlipModel();
                psm.setPaymenttype(paymenttype);
                Object[] rows = (Object[]) its.next();
                psm.setSlipno(String.valueOf(slipno));
                String earn_dedu_id = (String) rows[0];
                psm.setPfno((String) rows[1]);
                psm.setEmpno((String) rows[2]);
                psm.setEmployeename((String) rows[3]);
                if (rows[4] != null) {
                    psm.setFpfno((String) rows[4]);
                } else {
                    psm.setFpfno("");
                }
                psm.setBranch((String) rows[5]);
                psm.setSectionname(SecStringSet((String) rows[6]));
                psm.setDesignation((String) rows[7]);
//                psm.setBankaccountno(String.valueOf(startingchequeno));
                long val = (Long) list.get(i);
                psm.setBankaccountno(String.valueOf(val));
                psm.setPayslipyear(rows[9].toString());
                psm.setPayslipmonth(months[(Integer) rows[10] - 1]);
                psm.setSectioncode((String) rows[11]);

                String Deduction_Query = "select sum(amount) as deductionamount from employeedeductionstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                String Earnings_Query = "select sum(amount) as earningsamount from employeeearningstransactions where payrollprocessingdetailsid= '" + earn_dedu_id + "' and cancelled is false";

                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);
                List deductionlist = deductionquery.list();
                if (deductionlist.get(0) != null) {
                    psm.setTotaldeductions(deductionlist.get(0).toString());
                } else {
                    psm.setTotaldeductions("0.00");
                }

                SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);
                List earningslist = earningsquery.list();
                if (earningslist.get(0) != null) {
                    psm.setTotalearnings(earningslist.get(0).toString());
                } else {
                    psm.setTotalearnings("0.00");
                }

                double netsalary = Double.valueOf(psm.getTotalearnings()) - Double.valueOf(psm.getTotaldeductions());
                psm.setNetsalary(String.valueOf(decimalFormat.format(netsalary)));//Set Net Salary
                psm.setPrintingrecordsize(printingrecordsize);
                btr.getBankTextPrintWriter(psm, filePath);
                slipno++;
//                startingchequeno++;
                i++;
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Acquitance Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeDeductionList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map<String, String> deductionMap = new LinkedHashMap<String, String>();
        try {
            Criteria empDetailsCrit = session.createCriteria(Paycodemaster.class);
//            empDetailsCrit.add(Restrictions.sqlRestriction("paycodetype = 'D' "));
            empDetailsCrit.addOrder(Order.asc("paycodename"));
            List empDetailsList = empDetailsCrit.list();
            if (empDetailsList.size() > 0) {
                for (int i = 0; i < empDetailsList.size(); i++) {
                    Paycodemaster paycodemaster = (Paycodemaster) empDetailsList.get(i);
                    deductionMap.put(paycodemaster.getPaycode(), paycodemaster.getPaycodename());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return deductionMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeDeductionAllPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductionid, String year, String month, String reporttype, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DeductionAllReport dar = new DeductionAllReport();
            PaySlipModel psm = new PaySlipModel();
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            String Deductionid = deductionid;
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);

            if (Integer.valueOf(reporttype) == 1) {

                EmployeeDetails_Query = "select ppd.id,em.epfno,em.employeename,sm.sectionname,dm.designation,rm.regionname,ppd.year,ppd.month "
                        + "from payrollprocessingdetails ppd "
                        + "left join employeemaster em on ppd.employeeprovidentfundnumber = em.epfno "
                        + "left join designationmaster dm on dm.designationcode=ppd.designation "
                        + "left join sectionmaster sm on sm.id=ppd.section "
                        + "left join regionmaster rm on ppd.accregion=rm.id "
                        + "where ppd.year=" + p_year + " and ppd.month=" + p_month + " and ppd.accregion='" + LoggedInRegion + "' "
                        + "and ppd.section = 'S13' order by ppd.section,dm.orderno";

            } else if (Integer.valueOf(reporttype) == 2) {

                EmployeeDetails_Query = "select ppd.id,em.epfno,em.employeename,sm.sectionname,dm.designation,rm.regionname,ppd.year,ppd.month "
                        + "from payrollprocessingdetails ppd "
                        + "left join employeemaster em on ppd.employeeprovidentfundnumber = em.epfno "
                        + "left join designationmaster dm on dm.designationcode=ppd.designation "
                        + "left join sectionmaster sm on sm.id=ppd.section "
                        + "left join regionmaster rm on ppd.accregion=rm.id "
                        + "where ppd.year=" + p_year + " and ppd.month=" + p_month + " and ppd.accregion='" + LoggedInRegion + "' "
                        + "and ppd.section = 'S14' order by ppd.section,dm.orderno";

            } else if (Integer.valueOf(reporttype) == 3) {

                EmployeeDetails_Query = "select ppd.id,em.epfno,em.employeename,sm.sectionname,dm.designation,rm.regionname,ppd.year,ppd.month "
                        + "from payrollprocessingdetails ppd "
                        + "left join employeemaster em on ppd.employeeprovidentfundnumber = em.epfno "
                        + "left join designationmaster dm on dm.designationcode=ppd.designation "
                        + "left join sectionmaster sm on sm.id=ppd.section "
                        + "left join regionmaster rm on ppd.accregion=rm.id "
                        + "where ppd.year=" + p_year + " and ppd.month=" + p_month + " and ppd.accregion='" + LoggedInRegion + "' "
                        + "and ppd.section not in ('S13','S14') order by ppd.section,dm.orderno";
            }
            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            int printingrecordsize = employeequery.list().size();
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Map<String, PaySlip_Earn_Deduction_Model> deductionmap = new ConcurrentHashMap<String, PaySlip_Earn_Deduction_Model>();
                Object[] rows = (Object[]) its.next();
                psm.setSlipno(String.valueOf(slipno));
                String earn_dedu_id = (String) rows[0];
                psm.setPfno((String) rows[1]);
                psm.setEmployeename((String) rows[2]);
                psm.setSectionname((String) rows[3]);
                psm.setDesignation((String) rows[4]);
                psm.setBranch((String) rows[5]);
                psm.setPayslipyear(rows[6].toString());
                psm.setPayslipmonth(months[(Integer) rows[7] - 1]);

                String Deduction_Query = "select edt.deductionmasterid,pm.paycodename,edt.amount from employeedeductionstransactions edt, "
                        + "paycodemaster pm where edt.payrollprocessingdetailsid='" + earn_dedu_id + "' and edt.cancelled is false "
                        + "and edt.deductionmasterid='" + Deductionid + "' and pm.paycode='" + Deductionid + "'";

                String Earnings_Query = "select eet.earningmasterid,pm.paycodename,eet.amount from employeeearningstransactions eet,"
                        + "paycodemaster pm  where eet.payrollprocessingdetailsid='" + earn_dedu_id + "' and eet.cancelled is false "
                        + "and eet.earningmasterid='" + Deductionid + "' and pm.paycode='" + Deductionid + "'";

                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);

                SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);

                if (deductionquery.list().size() > 0) {

                    for (ListIterator it = deductionquery.list().listIterator(); it.hasNext();) {
                        Object[] row = (Object[]) it.next();
                        String deductionmasterid = (String) row[0];
                        String deductionname = (String) row[1];
                        String amount = row[2].toString();
                        psm.setDeductionname(deductionname);
                        psm.setAmount(amount);

                        String LoanInstall_Query = "select employeeloansandadvances.deductioncode,employeeloansandadvances.totalinstallment,"
                                + "employeeloansandadvances.currentinstallment,employeeloansandadvances.fileno from employeeloansandadvancesdetails left join "
                                + "employeeloansandadvances on employeeloansandadvances.id=employeeloansandadvancesdetails.employeeloansandadvancesid "
                                + "where employeeloansandadvancesdetails.payrollprocessingdetailsid= '" + earn_dedu_id + "' and employeeloansandadvances.deductioncode='" + Deductionid + "'";

                        SQLQuery loanquery = session.createSQLQuery(LoanInstall_Query);

                        if (loanquery.list().size() > 0) {
                            for (ListIterator itl = loanquery.list().listIterator(); itl.hasNext();) {
                                Object[] ro = (Object[]) itl.next();
                                String totalinstallment = "";
                                String currentinstallment = "";
                                String fileNo = "";
                                if (ro[1] != null) {
                                    totalinstallment = ro[1].toString();
                                }
                                if (ro[2] != null) {
                                    currentinstallment = ro[2].toString();
                                }
                                if (ro[3] != null) {
                                    fileNo = (String) ro[3];
                                }
                                psm.setInstallment("(" + currentinstallment + "/" + totalinstallment + ")");
                                psm.setFileno(fileNo);
                            }
                        } else {
                            psm.setInstallment("");
                        }
                        dar.getDeductionAllPrintWriter(psm, filePath);
                        slipno++;
                    }
                } else if (earningsquery.list().size() > 0) {

                    for (ListIterator it = earningsquery.list().listIterator(); it.hasNext();) {
                        Object[] row = (Object[]) it.next();
                        String earningsmasterid = (String) row[0];
                        String earningname = (String) row[1];
                        String amount = row[2].toString();
                        psm.setDeductionname(earningname);
                        psm.setAmount(amount);
                        psm.setInstallment("");
                        dar.getDeductionAllPrintWriter(psm, filePath);
                        slipno++;
                    }
                }
            }
            dar.DeductionAllGrandTotal(psm, filePath);
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    private static String EarnStringSet(String str) {
        if (str.length() > 9) {
            return str.substring(0, 9);
        } else {
            return str;
        }
    }

    private static String DeduStringSet(String str) {
        if (str.length() > 10) {
            return str.substring(0, 10);
        } else {
            return str;
        }
    }

    private static String LoanStringSet(String str) {
        if (str.length() > 10) {
            return str.substring(0, 10);
        } else {
            return str;
        }
    }

    private static String SecStringSet(String str) {
        if (str.length() > 15) {
            return str.substring(0, 15);
        } else {
            return str;
        }
    }

    private String GetEmployeePayBillDetailsForDownloand(Session session, String month, String year, String region) {
        String result = "";
        PayBillDetails payBillDetailsObject = new PayBillDetails();
        payBillDetailsObject.setMonth(month);
        payBillDetailsObject.setYear(year);
        payBillDetailsObject.setRegion(region);
        List<EmployeeLoansandAdvancesPost> employeeLoansandAdvancesPostList = new ArrayList<EmployeeLoansandAdvancesPost>();
        List<EmployeeLoansandAdvancesPost> employeeLoansandAdvancesPostModiList = new ArrayList<EmployeeLoansandAdvancesPost>();
        List<PayrollprocessingdetailsPost> payrollprocessingdetailsPostList = new ArrayList<PayrollprocessingdetailsPost>();
        List<SupplementatypaybillPost> supplementatypaybillPostList = new ArrayList<SupplementatypaybillPost>();
        Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvances.class);
        empLoanDetailsCrit.add(Restrictions.sqlRestriction("extract(month from loandate)=" + month));
        empLoanDetailsCrit.add(Restrictions.sqlRestriction("extract(year from loandate)=" + year));
        List empLoanDetailsList = empLoanDetailsCrit.list();
        payBillDetailsObject.setYear(year);
        payBillDetailsObject.setMonth(month);
        payBillDetailsObject.setRegion(region);
        payBillDetailsObject.setNewloans(String.valueOf(empLoanDetailsList.size()));
        if (empLoanDetailsList.size() > 0) {
            for (int i = 0; i < empLoanDetailsList.size(); i++) {
                Employeeloansandadvances employeeloansandadvancesObj = (Employeeloansandadvances) empLoanDetailsList.get(i);

                EmployeeLoansandAdvancesPost employeeLoansandAdvancesPostObj = new EmployeeLoansandAdvancesPost();
                if (employeeloansandadvancesObj.getId() != null) {
                    employeeLoansandAdvancesPostObj.setId(employeeloansandadvancesObj.getId());
                }
                if (employeeloansandadvancesObj.getLoandate() != null) {
                    employeeLoansandAdvancesPostObj.setLoandate(employeeloansandadvancesObj.getLoandate().toString());
                }
                if (employeeloansandadvancesObj.getLoanamount().toString() != null) {
                    employeeLoansandAdvancesPostObj.setLoanamount(employeeloansandadvancesObj.getLoanamount().toString());
                }
                if (employeeloansandadvancesObj.getTotalinstallment() != null) {
                    employeeLoansandAdvancesPostObj.setTotalinstallment(employeeloansandadvancesObj.getTotalinstallment().toString());
                }
                if (employeeloansandadvancesObj.getEmployeemaster().getEpfno() != null) {
                    employeeLoansandAdvancesPostObj.setEpfno(employeeloansandadvancesObj.getEmployeemaster().getEpfno());
                }
                if (employeeloansandadvancesObj.getCurrentinstallment() != null) {
                    employeeLoansandAdvancesPostObj.setCurrentinstallment(employeeloansandadvancesObj.getCurrentinstallment().toString());
                }
                if (employeeloansandadvancesObj.getDeductioncode() != null) {
                    employeeLoansandAdvancesPostObj.setDeductioncode(employeeloansandadvancesObj.getDeductioncode());
                }
                if (employeeloansandadvancesObj.getLoanbalance() != null) {
                    employeeLoansandAdvancesPostObj.setLoanbalance(employeeloansandadvancesObj.getLoanbalance().toString());
                }
                if (employeeloansandadvancesObj.getLoantype() != null) {
                    employeeLoansandAdvancesPostObj.setLoantype(employeeloansandadvancesObj.getLoantype());
                }
                if (employeeloansandadvancesObj.getStatus() != null) {
                    employeeLoansandAdvancesPostObj.setStatus(employeeloansandadvancesObj.getStatus());
                }
                if (employeeloansandadvancesObj.getRegionno() != null) {
                    employeeLoansandAdvancesPostObj.setRegionno(employeeloansandadvancesObj.getRegionno());
                }
                if (employeeloansandadvancesObj.getFileno() != null) {
                    employeeLoansandAdvancesPostObj.setFileno(employeeloansandadvancesObj.getFileno());
                }
                if (employeeloansandadvancesObj.getFirstinstallmentamount() != null) {
                    employeeLoansandAdvancesPostObj.setFirstinstallmentamount(employeeloansandadvancesObj.getFirstinstallmentamount().toString());
                }
                if (employeeloansandadvancesObj.getInstallmentamount() != null) {
                    employeeLoansandAdvancesPostObj.setInstallmentamount(employeeloansandadvancesObj.getInstallmentamount().toString());
                }

                employeeLoansandAdvancesPostList.add(employeeLoansandAdvancesPostObj);
            }
        }

        payBillDetailsObject.setEmployeeLoansandAdvancesPostList(employeeLoansandAdvancesPostList);

        //PayrollprocessingdetailsPost
        Criteria empPayProcessDetailsCrit = session.createCriteria(Payrollprocessingdetails.class);
//        empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("process is true"));
        empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("month=" + month));
        empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("year=" + year));
        List empPayProcessDetails = empPayProcessDetailsCrit.list();
        payBillDetailsObject.setProcesscount(String.valueOf(empPayProcessDetails.size()));
        if (empPayProcessDetails.size() > 0) {
            for (int i = 0; i < empPayProcessDetails.size(); i++) {
                Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) empPayProcessDetails.get(i);

                PayrollprocessingdetailsPost payrollprocessingdetailsPostObj = new PayrollprocessingdetailsPost();
                payrollprocessingdetailsPostObj.setId(payrollprocessingdetailsObj.getId());
                if (payrollprocessingdetailsObj.getEmployeemaster() != null) {
                    payrollprocessingdetailsPostObj.setEmployeemasterId(payrollprocessingdetailsObj.getEmployeemaster().getEpfno());
                }
                if (payrollprocessingdetailsObj.getEnddate() != null) {
                    payrollprocessingdetailsPostObj.setEnddate(payrollprocessingdetailsObj.getEnddate().toString());
                }
                if (payrollprocessingdetailsObj.getLeaveavailed() != null) {
                    payrollprocessingdetailsPostObj.setLeaveavailed(payrollprocessingdetailsObj.getLeaveavailed().toString());
                }
                if (payrollprocessingdetailsObj.getLeaveeligible() != null) {
                    payrollprocessingdetailsPostObj.setLeaveeligible(payrollprocessingdetailsObj.getLeaveeligible().toString());
                }
                if (payrollprocessingdetailsObj.getMonth() != null) {
                    payrollprocessingdetailsPostObj.setMonth(payrollprocessingdetailsObj.getMonth().toString());
                }
                if (payrollprocessingdetailsObj.getPayrollprocessingid() != null) {
                    payrollprocessingdetailsPostObj.setPayrollprocessingid(payrollprocessingdetailsObj.getPayrollprocessingid().toString());
                }
                if (payrollprocessingdetailsObj.getProcess() != null) {
                    payrollprocessingdetailsPostObj.setProcess(payrollprocessingdetailsObj.getProcess().toString());
                }
                if (payrollprocessingdetailsObj.getProcessedregular() != null) {
                    payrollprocessingdetailsPostObj.setProcessedregular(payrollprocessingdetailsObj.getProcessedregular().toString());
                }
                if (payrollprocessingdetailsObj.getSalarystructureid() != null) {
                    payrollprocessingdetailsPostObj.setSalarystructureid(payrollprocessingdetailsObj.getSalarystructureid().toString());
                }
                if (payrollprocessingdetailsObj.getStartdate() != null) {
                    payrollprocessingdetailsPostObj.setStartdate(payrollprocessingdetailsObj.getStartdate().toString());
                }
                if (payrollprocessingdetailsObj.getWorkedday() != null) {
                    payrollprocessingdetailsPostObj.setWorkedday(payrollprocessingdetailsObj.getWorkedday().toString());
                }
                if (payrollprocessingdetailsObj.getWorkingday() != null) {
                    payrollprocessingdetailsPostObj.setWorkingday(payrollprocessingdetailsObj.getWorkingday().toString());
                }
                if (payrollprocessingdetailsObj.getYear() != null) {
                    payrollprocessingdetailsPostObj.setYear(payrollprocessingdetailsObj.getYear().toString());
                }
                if (payrollprocessingdetailsObj.getAccregion() != null) {
                    payrollprocessingdetailsPostObj.setRegion(payrollprocessingdetailsObj.getAccregion().toString());
                }
                payrollprocessingdetailsPostObj.setEmployeecategory(payrollprocessingdetailsObj.getEmployeecategory());
                payrollprocessingdetailsPostObj.setDesignation(payrollprocessingdetailsObj.getDesignation());

                Criteria empEarnDetailsCrit = session.createCriteria(Employeeearningstransactions.class);
                empEarnDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                List empEarnDetailsList = empEarnDetailsCrit.list();
                if (empEarnDetailsList.size() > 0) {
                    List<EmployeeearningstransactionsPost> employeeearningstransactionsPostList = new ArrayList<EmployeeearningstransactionsPost>();
                    for (int j = 0; j < empEarnDetailsList.size(); j++) {
                        Employeeearningstransactions employeeearningstransactionsObj = (Employeeearningstransactions) empEarnDetailsList.get(j);


                        EmployeeearningstransactionsPost employeeearningstransactionsPostObj = new EmployeeearningstransactionsPost();
                        employeeearningstransactionsPostObj.setId(employeeearningstransactionsObj.getId());
                        employeeearningstransactionsPostObj.setEarningmasterid(employeeearningstransactionsObj.getEarningmasterid());
                        employeeearningstransactionsPostObj.setPayrollprocessingdetailsId(employeeearningstransactionsObj.getPayrollprocessingdetails().getId());
                        if (employeeearningstransactionsObj.getAmount() != null) {
                            employeeearningstransactionsPostObj.setAmount(employeeearningstransactionsObj.getAmount().toString());
                        }
                        if (employeeearningstransactionsObj.getCancelled() != null) {
                            employeeearningstransactionsPostObj.setCancelled(employeeearningstransactionsObj.getCancelled().toString());
                        }
                        employeeearningstransactionsPostList.add(employeeearningstransactionsPostObj);
                    }
                    payrollprocessingdetailsPostObj.setEmployeeearningstransactionsPostList(employeeearningstransactionsPostList);
                }

                Criteria empDeducDetailsCrit = session.createCriteria(Employeedeductionstransactions.class);
                empDeducDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                List empDeducDetailsList = empDeducDetailsCrit.list();
                if (empDeducDetailsList.size() > 0) {
                    List<EmployeedeductionstransactionsPost> employeedeductionstransactionsPostList = new ArrayList<EmployeedeductionstransactionsPost>();
                    for (int j = 0; j < empDeducDetailsList.size(); j++) {
                        Employeedeductionstransactions employeedeductionstransactionsObj = (Employeedeductionstransactions) empDeducDetailsList.get(j);

                        EmployeedeductionstransactionsPost employeedeductionstransactionsPostObj = new EmployeedeductionstransactionsPost();
                        employeedeductionstransactionsPostObj.setId(employeedeductionstransactionsObj.getId());
                        if (employeedeductionstransactionsObj.getAmount() != null) {
                            employeedeductionstransactionsPostObj.setAmount(employeedeductionstransactionsObj.getAmount().toString());
                        }
                        if (employeedeductionstransactionsObj.getCancelled() != null) {
                            employeedeductionstransactionsPostObj.setCancelled(employeedeductionstransactionsObj.getCancelled().toString());
                        }
                        employeedeductionstransactionsPostObj.setDeductionmasterid(employeedeductionstransactionsObj.getDeductionmasterid());
                        if (employeedeductionstransactionsObj.getPayrollprocessingdetails() != null) {
                            employeedeductionstransactionsPostObj.setPayrollprocessingdetailsId(employeedeductionstransactionsObj.getPayrollprocessingdetails().getId());
                        }
                        if (employeedeductionstransactionsObj.getType() != null) {
                            employeedeductionstransactionsPostObj.setType(employeedeductionstransactionsObj.getType().toString());
                        }

                        employeedeductionstransactionsPostList.add(employeedeductionstransactionsPostObj);
                    }
                    payrollprocessingdetailsPostObj.setEmployeedeductionstransactionsPostList(employeedeductionstransactionsPostList);
                }

                Criteria empLoanDeducDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
                empLoanDeducDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                List empLoanDeducDetailsList = empLoanDeducDetailsCrit.list();
                if (empLoanDeducDetailsList.size() > 0) {
                    List<EmployeeloansandadvancesdetailsPost> employeeloansandadvancesdetailsPostList = new ArrayList<EmployeeloansandadvancesdetailsPost>();
                    for (int j = 0; j < empLoanDeducDetailsList.size(); j++) {
                        Employeeloansandadvancesdetails employeeloansandadvancesdetailsObj = (Employeeloansandadvancesdetails) empLoanDeducDetailsList.get(j);


                        EmployeeloansandadvancesdetailsPost employeeloansandadvancesdetailsPostObj = new EmployeeloansandadvancesdetailsPost();
                        employeeloansandadvancesdetailsPostObj.setId(employeeloansandadvancesdetailsObj.getId());
                        if (employeeloansandadvancesdetailsObj.getCancelled() != null) {
                            employeeloansandadvancesdetailsPostObj.setCancelled(employeeloansandadvancesdetailsObj.getCancelled().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getInstallmentamount() != null) {
                            employeeloansandadvancesdetailsPostObj.setInstallmentamount(employeeloansandadvancesdetailsObj.getInstallmentamount().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getLoanbalance() != null) {
                            employeeloansandadvancesdetailsPostObj.setLoanbalance(employeeloansandadvancesdetailsObj.getLoanbalance().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getNthinstallment() != null) {
                            employeeloansandadvancesdetailsPostObj.setNthinstallment(employeeloansandadvancesdetailsObj.getNthinstallment().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getPayrollprocessingdetails() != null) {
                            employeeloansandadvancesdetailsPostObj.setPayrollprocessingdetailsId(employeeloansandadvancesdetailsObj.getPayrollprocessingdetails().getId());
                        }
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances() != null) {
                            employeeloansandadvancesdetailsPostObj.setLoansandadvancesid(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getId());
                        }

                        EmployeeLoansandAdvancesPost employeeLoansandAdvancesPostObj = new EmployeeLoansandAdvancesPost();

                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getCurrentinstallment() != null) {
                            employeeLoansandAdvancesPostObj.setCurrentinstallment(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getCurrentinstallment().toString());
                        }
                        employeeLoansandAdvancesPostObj.setDeductioncode(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getDeductioncode());
                        employeeLoansandAdvancesPostObj.setEpfno(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getEmployeemaster().getEpfno());
                        employeeLoansandAdvancesPostObj.setFileno(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFileno());
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFirstinstallmentamount() != null) {
                            employeeLoansandAdvancesPostObj.setFirstinstallmentamount(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFirstinstallmentamount().toString());
                        }
                        employeeLoansandAdvancesPostObj.setId(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getId());
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getInstallmentamount() != null) {
                            employeeLoansandAdvancesPostObj.setInstallmentamount(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getInstallmentamount().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanamount() != null) {
                            employeeLoansandAdvancesPostObj.setLoanamount(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanamount().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanbalance() != null) {
                            employeeLoansandAdvancesPostObj.setLoanbalance(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanbalance().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoandate() != null) {
                            employeeLoansandAdvancesPostObj.setLoandate(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoandate().toString());
                        }
                        employeeLoansandAdvancesPostObj.setLoantype(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoantype());
                        employeeLoansandAdvancesPostObj.setRegionno(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getRegionno());
                        employeeLoansandAdvancesPostObj.setStatus(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getStatus());
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getTotalinstallment() != null) {
                            employeeLoansandAdvancesPostObj.setTotalinstallment(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getTotalinstallment().toString());
                        }

                        employeeLoansandAdvancesPostModiList.add(employeeLoansandAdvancesPostObj);
                        employeeloansandadvancesdetailsPostList.add(employeeloansandadvancesdetailsPostObj);
                    }
                    payrollprocessingdetailsPostObj.setEmployeeloansandadvancesdetailsPostList(employeeloansandadvancesdetailsPostList);
                }

                payrollprocessingdetailsPostList.add(payrollprocessingdetailsPostObj);
            }

        }
        payBillDetailsObject.setPayrollprocessingdetailsPostList(payrollprocessingdetailsPostList);

        //Supplementarybill
        Criteria suppPaybillCrit = session.createCriteria(Supplementatypaybill.class);
        suppPaybillCrit.add(Restrictions.sqlRestriction("extract(month from date)=" + month));
        suppPaybillCrit.add(Restrictions.sqlRestriction("extract(year from date)=" + year));
        List suppPaybillList = suppPaybillCrit.list();
        payBillDetailsObject.setSuppcount(String.valueOf(suppPaybillList.size()));
        if (suppPaybillList.size() > 0) {
            for (int i = 0; i < suppPaybillList.size(); i++) {
                Supplementatypaybill supplementatypaybillObj = (Supplementatypaybill) suppPaybillList.get(i);

                SupplementatypaybillPost supplementatypaybillPostObj = new SupplementatypaybillPost();
                supplementatypaybillPostObj.setId(supplementatypaybillObj.getId());
                supplementatypaybillPostObj.setAccregion(supplementatypaybillObj.getAccregion());
                if (supplementatypaybillObj.getCancelled() != null) {
                    supplementatypaybillPostObj.setCancelled(supplementatypaybillObj.getCancelled().toString());
                }
                if (supplementatypaybillObj.getDate() != null) {
                    supplementatypaybillPostObj.setDate(supplementatypaybillObj.getDate().toString());
                }
                if (supplementatypaybillObj.getEmployeemaster() != null) {
                    supplementatypaybillPostObj.setEmployeemasterId(supplementatypaybillObj.getEmployeemaster().getEpfno());
                }
                if (supplementatypaybillObj.getNoofdays() != null) {
                    supplementatypaybillPostObj.setNoofdays(supplementatypaybillObj.getNoofdays().toString());
                }
                supplementatypaybillPostObj.setEmployeecategory(supplementatypaybillObj.getEmployeecategory());
                supplementatypaybillPostObj.setPaymentmode(supplementatypaybillObj.getPaymentmode());
                supplementatypaybillPostObj.setSection(supplementatypaybillObj.getSection());
                if (supplementatypaybillObj.getSldate() != null) {
                    supplementatypaybillPostObj.setSldate(supplementatypaybillObj.getSldate().toString());
                }
                supplementatypaybillPostObj.setSubsection(supplementatypaybillObj.getSubsection());
                supplementatypaybillPostObj.setType(supplementatypaybillObj.getType());

                Criteria suppPaybillProcessCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                suppPaybillProcessCrit.add(Restrictions.sqlRestriction("supplementatypaybillid='" + supplementatypaybillObj.getId() + "'"));
                List suppPaybillProcessList = suppPaybillProcessCrit.list();
                if (suppPaybillProcessList.size() > 0) {
                    List<SupplementarypayrollprocessingdetailsPost> supplementarypayrollprocessingdetailsPostList = new ArrayList<SupplementarypayrollprocessingdetailsPost>();
                    for (int j = 0; j < suppPaybillProcessList.size(); j++) {
                        Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) suppPaybillProcessList.get(j);


                        SupplementarypayrollprocessingdetailsPost supplementarypayrollprocessingdetailsPostObj = new SupplementarypayrollprocessingdetailsPost();
                        supplementarypayrollprocessingdetailsPostObj.setId(supplementarypayrollprocessingdetailsObj.getId());
                        supplementarypayrollprocessingdetailsPostObj.setAccregion(supplementarypayrollprocessingdetailsObj.getAccregion());
                        if (supplementarypayrollprocessingdetailsObj.getCalculatedmonth() != null) {
                            supplementarypayrollprocessingdetailsPostObj.setCalculatedmonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth().toString());
                        }
                        if (supplementarypayrollprocessingdetailsObj.getCalculatedyear() != null) {
                            supplementarypayrollprocessingdetailsPostObj.setCalculatedyear(supplementarypayrollprocessingdetailsObj.getCalculatedyear().toString());
                        }
                        if (supplementarypayrollprocessingdetailsObj.getCancelled() != null) {
                            supplementarypayrollprocessingdetailsPostObj.setCancelled(supplementarypayrollprocessingdetailsObj.getCancelled().toString());
                        }
                        if (supplementarypayrollprocessingdetailsObj.getNooddayscalculated() != null) {
                            supplementarypayrollprocessingdetailsPostObj.setNooddayscalculated(supplementarypayrollprocessingdetailsObj.getNooddayscalculated().toString());
                        }
                        if (supplementarypayrollprocessingdetailsObj.getSupplementatypaybill() != null) {
                            supplementarypayrollprocessingdetailsPostObj.setSupplementatypaybillId(supplementarypayrollprocessingdetailsObj.getSupplementatypaybill().getId());
                        }

                        Criteria suppEarTranCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                        suppEarTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        List suppEarTranList = suppEarTranCrit.list();
                        if (suppEarTranList.size() > 0) {
                            List<SupplementaryemployeeearningstransactionsPost> SupplementaryemployeeearningstransactionsPostList = new ArrayList<SupplementaryemployeeearningstransactionsPost>();
                            for (int k = 0; k < suppEarTranList.size(); k++) {
                                Supplementaryemployeeearningstransactions supplementaryemployeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) suppEarTranList.get(k);

                                SupplementaryemployeeearningstransactionsPost supplementaryemployeeearningstransactionsPostObj = new SupplementaryemployeeearningstransactionsPost();
                                supplementaryemployeeearningstransactionsPostObj.setId(supplementaryemployeeearningstransactionsObj.getId());
                                supplementaryemployeeearningstransactionsPostObj.setAccregion(supplementaryemployeeearningstransactionsObj.getAccregion());
                                if (supplementaryemployeeearningstransactionsObj.getAmount() != null) {
                                    supplementaryemployeeearningstransactionsPostObj.setAmount(supplementaryemployeeearningstransactionsObj.getAmount().toString());
                                }
                                if (supplementaryemployeeearningstransactionsObj.getCancelled() != null) {
                                    supplementaryemployeeearningstransactionsPostObj.setCancelled(supplementaryemployeeearningstransactionsObj.getCancelled().toString());
                                }
                                supplementaryemployeeearningstransactionsPostObj.setEarningmasterid(supplementaryemployeeearningstransactionsObj.getEarningmasterid());
                                if (supplementaryemployeeearningstransactionsObj.getSupplementarypayrollprocessingdetails() != null) {
                                    supplementaryemployeeearningstransactionsPostObj.setSupplementarypayrollprocessingdetailsId(supplementaryemployeeearningstransactionsObj.getSupplementarypayrollprocessingdetails().getId());
                                }

                                SupplementaryemployeeearningstransactionsPostList.add(supplementaryemployeeearningstransactionsPostObj);
                            }
                            supplementarypayrollprocessingdetailsPostObj.setSupplementaryemployeeearningstransactionsPostList(SupplementaryemployeeearningstransactionsPostList);
                        }


                        Criteria suppDedTranCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                        suppDedTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        List suppDedTranList = suppDedTranCrit.list();
                        if (suppDedTranList.size() > 0) {
                            List<SupplementaryemployeedeductionstransactionsPost> supplementaryemployeedeductionstransactionsPostList = new ArrayList<SupplementaryemployeedeductionstransactionsPost>();
                            for (int k = 0; k < suppDedTranList.size(); k++) {
                                Supplementaryemployeedeductionstransactions supplementaryemployeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) suppDedTranList.get(k);

                                SupplementaryemployeedeductionstransactionsPost supplementaryemployeedeductionstransactionsPostObj = new SupplementaryemployeedeductionstransactionsPost();
                                supplementaryemployeedeductionstransactionsPostObj.setId(supplementaryemployeedeductionstransactionsObj.getId());
                                supplementaryemployeedeductionstransactionsPostObj.setAccregion(supplementaryemployeedeductionstransactionsObj.getAccregion());
                                if (supplementaryemployeedeductionstransactionsObj.getAmount() != null) {
                                    supplementaryemployeedeductionstransactionsPostObj.setAmount(supplementaryemployeedeductionstransactionsObj.getAmount().toString());
                                }
                                if (supplementaryemployeedeductionstransactionsObj.getCancelled() != null) {
                                    supplementaryemployeedeductionstransactionsPostObj.setCancelled(supplementaryemployeedeductionstransactionsObj.getCancelled().toString());
                                }
                                supplementaryemployeedeductionstransactionsPostObj.setDeductionmasterid(supplementaryemployeedeductionstransactionsObj.getDeductionmasterid());
                                if (supplementaryemployeedeductionstransactionsObj.getSupplementarypayrollprocessingdetails() != null) {
                                    supplementaryemployeedeductionstransactionsPostObj.setSupplementarypayrollprocessingdetailsId(supplementaryemployeedeductionstransactionsObj.getSupplementarypayrollprocessingdetails().getId());
                                }
                                supplementaryemployeedeductionstransactionsPostObj.setType(supplementaryemployeedeductionstransactionsObj.getType());

                                supplementaryemployeedeductionstransactionsPostList.add(supplementaryemployeedeductionstransactionsPostObj);
                            }
                            supplementarypayrollprocessingdetailsPostObj.setSupplementaryemployeedeductionstransactionsPostList(supplementaryemployeedeductionstransactionsPostList);
                        }



                        Criteria suppLoanDedCrit = session.createCriteria(Supplementaryemployeeloansandadvancesdetails.class);
                        suppLoanDedCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        List suppLoanDedList = suppLoanDedCrit.list();
                        if (suppLoanDedList.size() > 0) {
                            List<SupplementaryemployeeloansandadvancesdetailsPost> supplementaryemployeeloansandadvancesdetailsPostList = new ArrayList<SupplementaryemployeeloansandadvancesdetailsPost>();
                            for (int k = 0; k < suppLoanDedList.size(); k++) {
                                Supplementaryemployeeloansandadvancesdetails supplementaryemployeeloansandadvancesdetailsObj = (Supplementaryemployeeloansandadvancesdetails) suppLoanDedList.get(k);

                                SupplementaryemployeeloansandadvancesdetailsPost supplementaryemployeeloansandadvancesdetailsPostObj = new SupplementaryemployeeloansandadvancesdetailsPost();
                                supplementaryemployeeloansandadvancesdetailsPostObj.setId(supplementaryemployeeloansandadvancesdetailsObj.getId());
                                supplementaryemployeeloansandadvancesdetailsPostObj.setAccregion(supplementaryemployeeloansandadvancesdetailsObj.getAccregion());
                                if (supplementaryemployeeloansandadvancesdetailsObj.getCancelled() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setCancelled(supplementaryemployeeloansandadvancesdetailsObj.getCancelled().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setEmployeeloansandadvancesId(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getId());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getInstallmentamount() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setInstallmentamount(supplementaryemployeeloansandadvancesdetailsObj.getInstallmentamount().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getLoanbalance() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setLoanbalance(supplementaryemployeeloansandadvancesdetailsObj.getLoanbalance().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getNthinstallment() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setNthinstallment(supplementaryemployeeloansandadvancesdetailsObj.getNthinstallment().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getSupplementarypayrollprocessingdetails() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setSupplementarypayrollprocessingdetailsId(supplementaryemployeeloansandadvancesdetailsObj.getSupplementarypayrollprocessingdetails().getId());
                                }

                                EmployeeLoansandAdvancesPost employeeLoansandAdvancesPostObj = new EmployeeLoansandAdvancesPost();
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getCurrentinstallment() != null) {
                                    employeeLoansandAdvancesPostObj.setCurrentinstallment(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getCurrentinstallment().toString());
                                }
                                employeeLoansandAdvancesPostObj.setDeductioncode(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getDeductioncode());
                                employeeLoansandAdvancesPostObj.setEpfno(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getEmployeemaster().getEpfno());
                                employeeLoansandAdvancesPostObj.setFileno(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFileno());
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFirstinstallmentamount() != null) {
                                    employeeLoansandAdvancesPostObj.setFirstinstallmentamount(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFirstinstallmentamount().toString());
                                }
                                employeeLoansandAdvancesPostObj.setId(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getId());
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getInstallmentamount() != null) {
                                    employeeLoansandAdvancesPostObj.setInstallmentamount(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getInstallmentamount().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanamount() != null) {
                                    employeeLoansandAdvancesPostObj.setLoanamount(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanamount().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanbalance() != null) {
                                    employeeLoansandAdvancesPostObj.setLoanbalance(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanbalance().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoandate() != null) {
                                    employeeLoansandAdvancesPostObj.setLoandate(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoandate().toString());
                                }
                                employeeLoansandAdvancesPostObj.setLoantype(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoantype());
                                employeeLoansandAdvancesPostObj.setRegionno(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getRegionno());
                                employeeLoansandAdvancesPostObj.setStatus(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getStatus());
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getTotalinstallment() != null) {
                                    employeeLoansandAdvancesPostObj.setTotalinstallment(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getTotalinstallment().toString());
                                }

                                employeeLoansandAdvancesPostModiList.add(employeeLoansandAdvancesPostObj);

                                supplementaryemployeeloansandadvancesdetailsPostList.add(supplementaryemployeeloansandadvancesdetailsPostObj);
                            }
                            supplementarypayrollprocessingdetailsPostObj.setSupplementaryemployeeloansandadvancesdetailsPostList(supplementaryemployeeloansandadvancesdetailsPostList);
                        }


                        supplementarypayrollprocessingdetailsPostList.add(supplementarypayrollprocessingdetailsPostObj);

                    }
                    supplementatypaybillPostObj.setSupplementarypayrollprocessingdetailsPostList(supplementarypayrollprocessingdetailsPostList);
                }

                supplementatypaybillPostList.add(supplementatypaybillPostObj);

            }
        }


        payBillDetailsObject.setSupplementatypaybillPostList(supplementatypaybillPostList);
        payBillDetailsObject.setEmployeeLoansandAdvancesPostModiList(employeeLoansandAdvancesPostModiList);
        payBillDetailsObject.setModiloans(String.valueOf(employeeLoansandAdvancesPostModiList.size()));


        try {
            JAXBContext context = JAXBContext.newInstance(PayBillDetails.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(payBillDetailsObject, stringWriter);
            result = stringWriter.toString();
        } catch (Exception e) {
        }

        return result;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeFestivalAdvancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String type, String filePathwithName) {
        Map map = new HashMap();
        FestivalAdvanceReport far = new FestivalAdvanceReport();
        String QUERY = null;
        PaySlipModel psm = null;
        try {
            if (type.equals("B") || type.equals("C")) {
                QUERY = "select em.employeename,ela.employeeprovidentfundnumber,sm.sectionname,dm.designation,rm.regionname,ela.loanamount,em.banksbaccount "
                        + "from employeeloansandadvances ela "
                        + "left join employeemaster em on em.epfno=ela.employeeprovidentfundnumber "
                        + "left join sectionmaster sm on sm.id=em.section "
                        + "left join designationmaster dm on dm.designationcode=em.designation "
                        + "left join regionmaster rm on rm.id=ela.accregion "
                        + "where ela.loandate='" + postgresDate(asondate) + "' and ela.deductioncode='L06' and "
                        + "ela.accregion='" + LoggedInRegion + "' and em.paymentmode='" + type + "' order by em.section,dm.orderno";
            } else {
                QUERY = "select em.employeename,ela.employeeprovidentfundnumber,sm.sectionname,dm.designation,rm.regionname,ela.loanamount,em.banksbaccount "
                        + "from employeeloansandadvances ela "
                        + "left join employeemaster em on em.epfno=ela.employeeprovidentfundnumber "
                        + "left join sectionmaster sm on sm.id=em.section "
                        + "left join designationmaster dm on dm.designationcode=em.designation "
                        + "left join regionmaster rm on rm.id=ela.accregion "
                        + "where ela.loandate='" + postgresDate(asondate) + "' and ela.deductioncode='L06' and ela.accregion='" + LoggedInRegion + "' order by em.section,dm.orderno";
            }
            SQLQuery FAQuery = session.createSQLQuery(QUERY);
            if (FAQuery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            for (ListIterator its = FAQuery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                psm = new PaySlipModel();
                psm.setSlipno(String.valueOf(slipno));
                psm.setEmployeename((String) rows[0]);
                psm.setEpf((String) rows[1]);
                psm.setSectionname(SecStringSet((String) rows[2]));
                psm.setDesignation(SecStringSet((String) rows[3]));
                psm.setRegion((String) rows[4]);
                psm.setAmount(rows[5].toString());
                psm.setBankaccountno((String) rows[6]);
                psm.setDate(asondate);
                if (type.equals("A")) {
                    psm.setPaymentmode("(ALL)");
                } else if (type.equals("B")) {
                    psm.setPaymentmode("(BANK)");
                } else if (type.equals("C")) {
                    psm.setPaymentmode("(CASH)");
                }
                far.getFestivalAdvancePrintWriter(psm, filePathwithName);
                slipno++;
            }
            far.grandTotal(filePathwithName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeThrftSocietyPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            ThrftSocietyReport tsr = new ThrftSocietyReport();
            PaySlipModel psm = null;
            String EmployeeDetails_Query = null;
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);

            EmployeeDetails_Query = "select ppd.id,em.epfno,em.employeename,sm.sectionname,dm.designation,rm.regionname,ppd.year,ppd.month "
                    + "from payrollprocessingdetails ppd "
                    + "left join employeemaster em on ppd.employeeprovidentfundnumber = em.epfno "
                    + "left join designationmaster dm on dm.designationcode=ppd.designation "
                    + "left join sectionmaster sm on sm.id=ppd.section "
                    + "left join regionmaster rm on ppd.accregion=rm.id "
                    + "left join employeedeductionaccountcode edc on edc.employeeprovidentfundnumber=ppd.employeeprovidentfundnumber "
                    + "where ppd.year=" + p_year + " and ppd.month=" + p_month + " and ppd.accregion='" + LoggedInRegion + "' "
                    + "and ppd.section not in ('S13','S14') order by edc.deductionaccountcode";

//            EmployeeDetails_Query = "select ppd.id,em.epfno,em.employeename,sm.sectionname,dm.designation,rm.regionname,ppd.year,ppd.month "
//                    + "from payrollprocessingdetails ppd "
//                    + "left join employeemaster em on ppd.employeeprovidentfundnumber = em.epfno "
//                    + "left join designationmaster dm on dm.designationcode=ppd.designation "
//                    + "left join sectionmaster sm on sm.id=ppd.section "
//                    + "left join regionmaster rm on ppd.accregion=rm.id "
//                    + "where ppd.year=" + p_year + " and ppd.month=" + p_month + " and ppd.accregion='" + LoggedInRegion + "' "
//                    + "and ppd.section not in ('S13','S14') order by ppd.section,dm.orderno";


            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            int printingrecordsize = employeequery.list().size();
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Map<String, PaySlip_Earn_Deduction_Model> deductionmap = new ConcurrentHashMap<String, PaySlip_Earn_Deduction_Model>();
                psm = new PaySlipModel();
                Object[] rows = (Object[]) its.next();
                psm.setSlipno(String.valueOf(slipno));
                String earn_dedu_id = (String) rows[0];
                psm.setPfno((String) rows[1]);
                psm.setEmployeename((String) rows[2]);
                psm.setSectionname((String) rows[3]);
                psm.setDesignation((String) rows[4]);
                psm.setRegion((String) rows[5]);
                psm.setPayslipmonth(months[(Integer) rows[7] - 1] + "\"" + (rows[6].toString()).substring(2, 4));
//                psm.setPayslipyear((rows[6].toString()).substring(2, 4));
//                psm.setPayslipmonth(months[(Integer) rows[7] - 1]);

                String Deduction_Query = "select deductionmasterid,amount from employeedeductionstransactions where "
                        + "payrollprocessingdetailsid='" + earn_dedu_id + "' and cancelled is false and deductionmasterid='D11'";
                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);

                if (deductionquery.list().size() > 0) {

                    for (ListIterator it = deductionquery.list().listIterator(); it.hasNext();) {
                        Object[] row = (Object[]) it.next();
                        String deductionmasterid = (String) row[0];
                        String amount = row[1].toString();
                        psm.setAmount(amount);

                        String ThrftNoQuery = "SELECT deductionaccountcode FROM employeedeductionaccountcode where "
                                + "employeeprovidentfundnumber='" + psm.getPfno() + "' and paycode='D11'";

                        SQLQuery thrftquery = session.createSQLQuery(ThrftNoQuery);

                        if (thrftquery.list().size() > 0) {
                            String societyno = (String) thrftquery.list().get(0);
                            psm.setSocietyno(societyno);
                        } else {
                            psm.setSocietyno("");
                        }
                        tsr.getPrintWriter(psm, filePath);
                        slipno++;
                    }
                }
            }
            tsr.GrandTotal(psm, filePath);
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeStorageLossPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String grouptype, String filePath) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            StorageLossReport slr = new StorageLossReport();
            PaySlipModel psm = null;
            String EmployeeDetails_Query = null;
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month) + 1;
            String groupname = null;

            if (grouptype.equals("STLOSS")) {
                groupname = "STORAGE LOSS";
            }
            if (grouptype.equals("LIC")) {
                groupname = "LIC";
            }
            if (grouptype.equals("PLI")) {
                groupname = "PLI";
            }
            if (grouptype.equals("HBA")) {
                groupname = "HBA";
            }
            if (grouptype.equals("HBAINT")) {
                groupname = "HBA INTEREST";
            }
            if (grouptype.equals("VEHI")) {
                groupname = "VEHICLE ADVANCE";
            }

            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber, em.employeename, dm.designation, sm.sectionname, "
                    + "pp.salarystructureid,dm.orderno,rm.regionname,pp.id "
                    + "from employeedeductionstransactions edt "
                    + "left join payrollprocessingdetails pp on pp.id=edt.payrollprocessingdetailsid "
                    + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
                    + "left join designationmaster dm on dm.designationcode= em.designation "
                    + "left join sectionmaster sm on sm.id=pp.section "
                    + "left join regionmaster rm on rm.id=pp.accregion "
                    + "where edt.cancelled is false and edt.deductionmasterid in "
                    + "(select paycode from paycodemaster where grouphead = '" + grouptype + "') "
                    + "and pp.month=" + p_month + " and pp.year=" + p_year + " and pp.process is true and pp.accregion='" + LoggedInRegion + "' "
                    + "group by pp.employeeprovidentfundnumber, em.employeename, dm.designation, "
                    + "pp.section,sm.sectionname,pp.salarystructureid,dm.orderno,rm.regionname,pp.id "
                    + "order by pp.section,dm.orderno";

//            EmployeeDetails_Query = "select pp.employeeprovidentfundnumber, em.employeename, dm.designation, pp.section,sm.sectionname, pm.paycodename, "
//                    + "edt.deductionmasterid, edt.amount,pp.salarystructureid  "
//                    + "from employeedeductionstransactions edt "
//                    + "left join payrollprocessingdetails pp on pp.id=edt.payrollprocessingdetailsid "
//                    + "left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno "
//                    + "left join designationmaster dm on dm.designationcode= em.designation "
//                    + "left join sectionmaster sm on sm.id=pp.section "
//                    + "left join paycodemaster pm on pm.paycode=edt.deductionmasterid  "
//                    + "where edt.cancelled is false and "
//                    + "edt.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "') "
//                    + "and pp.month=" + p_month + " and pp.year=" + p_year + " and pp.process is true and pp.accregion='" + LoggedInRegion + "' "
//                    + "order by pp.section,dm.orderno,pp.employeeprovidentfundnumber,pm.paycode";

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;

            List<PaySlip_Earn_Deduction_Model> schedulelist = null;

            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                psm = new PaySlipModel();
                Object[] rows = (Object[]) its.next();

                psm.setPfno((String) rows[0]);
                psm.setEmployeename((String) rows[1]);
                psm.setDesignation(SubString((String) rows[2], 12));
                psm.setSectionname(SubString((String) rows[3], 15));
                String salarystructureid = (String) rows[4];
                psm.setRegion((String) rows[6]);
                String payrollprocessid = (String) rows[7];

                String DEDUCTIONQUERY = "select edt.deductionmasterid,pm.paycodename,edt.amount,pm.paycodetype from employeedeductionstransactions edt "
                        + "left join paycodemaster pm on pm.paycode=edt.deductionmasterid "
                        + "where edt.payrollprocessingdetailsid = '" + payrollprocessid + "' "
                        + "and edt.cancelled is false "
                        + "and edt.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "')";

                SQLQuery deduction_query = session.createSQLQuery(DEDUCTIONQUERY);

                schedulelist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                PaySlip_Earn_Deduction_Model psedm = null;
                for (ListIterator it1 = deduction_query.list().listIterator(); it1.hasNext();) {
                    psedm = new PaySlip_Earn_Deduction_Model();
                    Object[] row1 = (Object[]) it1.next();

                    String deductioncode = (String) row1[0];
                    psedm.setPaycodename(SubString((String) row1[1], 12));
                    BigDecimal bigamount = (BigDecimal) row1[2];
                    psedm.setDeductionamount(decimalFormat.format(bigamount.doubleValue()));
                    char ptype = (Character) row1[3];

                    String FILENOQUERY = null;

                    if (ptype == 'L') {

                        FILENOQUERY = "select fileno from employeeloansandadvances ela "
                                + "left join employeeloansandadvancesdetails elad on elad.employeeloansandadvancesid=ela.id "
                                + "where ela.deductioncode='" + deductioncode + "' and elad.payrollprocessingdetailsid='" + payrollprocessid + "' "
                                + "and elad.cancelled is false";

                    } else if (ptype == 'D') {

                        FILENOQUERY = "select edd.dedn_no from employeedeductiondetails edd  "
                                + "left join salarystructure ss on ss.id=edd.salarystructureid where ss.id='" + salarystructureid + "' "
                                + "and edd.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "') "
                                + "and edd.cancelled is false and edd.deductionmasterid='" + deductioncode + "'";

                    }
                    SQLQuery fileno_query = session.createSQLQuery(FILENOQUERY);
                    List filenolist = fileno_query.list();

                    if (filenolist.size() > 0) {
                        if (filenolist.get(0) != null) {
                            psedm.setLicpolicyno(SubString(filenolist.get(0).toString(), 23));
                        } else {
                            psedm.setLicpolicyno("");
                        }
                    } else {
                        psedm.setLicpolicyno("");
                    }

                    schedulelist.add(psedm);
                }
                psm.setLiclist(schedulelist);
                psm.setSlipno(String.valueOf(slipno));
                psm.setPayslipmonth(months[Integer.valueOf(month)] + "/" + year);
                psm.setSchedulename(groupname);
                slr.getPrintWriter(psm, filePath);

                slipno++;
            }
            slr.GrandTotal(psm, filePath);
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Storage Loss Report Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    private String SubString(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        } else {
            return str;
        }
    }

    private Map<String, PaySlip_Earn_Deduction_Model> sortByKeys(Map<String, PaySlip_Earn_Deduction_Model> deductionmap) {
        List<String> keys = new LinkedList<String>(deductionmap.keySet());
        Collections.sort(keys);
        Map<String, PaySlip_Earn_Deduction_Model> sortedMap = new LinkedHashMap<String, PaySlip_Earn_Deduction_Model>();
        for (String key : keys) {
            sortedMap.put(key, deductionmap.get(key));
        }
        return sortedMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map employeeDBFPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String reportType, String empcategory) {
        Map map = new HashMap();
        try {
            EmployeeDBFReport epfr = new EmployeeDBFReport();
            PaySlipModel psm = null;
            String EmployeeDetails_Query = null;

            if ("DBF".equalsIgnoreCase(reportType)) {
                EmployeeDetails_Query = "select epfno, region, section, employeename, designation, banksbaccount from employeemaster "
                        + "where region='" + LoggedInRegion + "' and category='" + empcategory + "' order by section, designation ";
            } else {
                EmployeeDetails_Query = "select em.region,em.epfno,em.employeename,dm.designation,sm.sectionname,em.banksbaccount,em.paymentmode,dm.payscalecode"
                        + " from employeemaster as em "
                        + " left join designationmaster as dm on em.designation=dm.designationcode"
                        + " left join sectionmaster as sm on em.section=sm.id"
                        + " where  em.region='" + LoggedInRegion + "' and em.category='" + empcategory + "' order by section, designation ";
            }

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            String epfno = "";
            List<PaySlipModel> emplist = new ArrayList<PaySlipModel>();
            if ("DBF".equalsIgnoreCase(reportType)) {
                for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    psm = new PaySlipModel();
                    epfno = (String) rows[0];
                    psm.setEpf(SubString(epfno, 10));
                    psm.setRegion((String) rows[1]);
                    psm.setSectioncode((String) rows[2]);
                    psm.setEmployeename((String) rows[3]);
                    psm.setDesignation((String) rows[4]);
                    psm.setEmpno(SubString(epfno, 7));
                    if (rows[5] != null) {
                        String bankaccountno = (String) rows[5];
                        if ((bankaccountno).equalsIgnoreCase("Null")) {
                            psm.setBankaccountno("");
                        } else {
                            psm.setBankaccountno((String) rows[5]);
                        }
                    } else {
                        psm.setBankaccountno("");
                    }
                    emplist.add(psm);

                }
                epfr.getEmployeePrintWriter(emplist, filePath, reportType);
            } else {
                for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    psm = new PaySlipModel();
                    psm.setRegion((String) rows[0]);
                    psm.setEpf((String) rows[1]);
                    psm.setEmployeename((String) rows[2]);
                    psm.setDesignation((String) rows[3]);
                    psm.setSectionname((String) rows[4]);
                    if (rows[5] != null) {
                        String bankaccountno = (String) rows[5];
                        if ((bankaccountno).equalsIgnoreCase("Null")) {
                            psm.setBankaccountno("");
                        } else {
                            psm.setBankaccountno((String) rows[5]);
                        }
                    } else {
                        psm.setBankaccountno("");
                    }
                    psm.setPaymentmode((String) rows[6]);
                    psm.setPayscale((String) rows[7]);
                    emplist.add(psm);
                }
                epfr.getEmployeePrintWriter(emplist, filePath, reportType);
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "EPF Form (Flat file) Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeBonusdetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String bonustype, String code, String earningAmount, String deductionAmount, String curRec, String totRec) {
        Map resultMap = new HashMap();
        Transaction transaction;
        String Dtformat1 = "dd/MM/yyyy";
        String Datestring = attendancemonth;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal.get(Calendar.YEAR);
        long currentRecords = Long.parseLong(curRec);
        if (code != null) {
            if (code.trim().length() > 0) {

                Criteria attCrit = session.createCriteria(Bonusdetails.class);
                attCrit.add(Restrictions.sqlRestriction("epfno='" + code + "'"));
                attCrit.add(Restrictions.sqlRestriction("bonustype='" + bonustype + "'"));
                attCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                attCrit.add(Restrictions.sqlRestriction("year=" + iYear));

                List attList = attCrit.list();
                if (attList.size() > 0) {
                    Bonusdetails bonusdetailsObj = (Bonusdetails) attList.get(0);
                    bonusdetailsObj.setEarningsamount(new BigDecimal(earningAmount));
                    bonusdetailsObj.setDeductionamount(new BigDecimal(deductionAmount));

                    transaction = session.beginTransaction();
                    session.update(bonusdetailsObj);
                    transaction.commit();
                } else {
                    Bonusdetails bonusdetailsObj = new Bonusdetails();
                    bonusdetailsObj.setId(getmaxofBonusdetails(session));
                    bonusdetailsObj.setEpfno(code);
                    bonusdetailsObj.setMonth(iMonth);
                    bonusdetailsObj.setYear(iYear);
                    bonusdetailsObj.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                    bonusdetailsObj.setEarningsamount(new BigDecimal(earningAmount));
                    bonusdetailsObj.setDeductionamount(new BigDecimal(deductionAmount));
                    bonusdetailsObj.setBonustype(bonustype);
                    transaction = session.beginTransaction();
                    session.save(bonusdetailsObj);
                    transaction.commit();
                }
            }
        }
        currentRecords = currentRecords + 1;
        resultMap.put("currentRecords", currentRecords);
        resultMap.put("totalrecords", totRec);
        return resultMap;
    }

    public synchronized int getmaxofBonusdetails(Session session) {
        int maxSequenceNumber = 1;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select  max(cast(id as int)) as maxsequencenumber from bonusdetails");
            List imlist = session.createSQLQuery(sb.toString()).list();
            if (imlist.size() > 0) {
                if (imlist.get(0) != null) {
                    maxSequenceNumber = (Integer) imlist.get(0);
                    maxSequenceNumber = maxSequenceNumber + 1;
                } else {
                    maxSequenceNumber = 1;
                }
            } else {
                maxSequenceNumber = 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return maxSequenceNumber;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeePayStructureDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "'"));
        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("employeename", empmasterObj.getEmployeename());
            resultMap.put("designation", empmasterObj.getDesignation());
            resultMap.put("fathername", empmasterObj.getFathername());
            resultMap.put("dateofbirth", dateToString(empmasterObj.getDateofbirth()));
            resultMap.put("doa", dateToString(empmasterObj.getDateofappoinment()));
            resultMap.put("dateofprobation", dateToString(empmasterObj.getDateofprobation()));

            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.add(Restrictions.sqlRestriction("id = '" + empmasterObj.getRegion() + "'"));

            List<Regionmaster> rgnList = rgnCrit.list();
            Regionmaster lbobj = rgnList.get(0);
            resultMap.put("branchname", lbobj.getRegionname());

            Criteria secCrit = session.createCriteria(Sectionmaster.class);
//            Criteria secCrit = session.createCriteria(Sectionmaster.class).add(Restrictions.sqlRestriction("id='"+empmasterObj.getSection()+"'"));
            secCrit.add(Restrictions.sqlRestriction("id = '" + empmasterObj.getSection() + "'"));
            List secList = secCrit.list();
            Sectionmaster secobj = (Sectionmaster) secList.get(0);
            resultMap.put("section", secobj.getSectionname());
            Criteria desCrit = session.createCriteria(Designationmaster.class);
            desCrit.add(Restrictions.sqlRestriction("designationcode = '" + empmasterObj.getDesignation() + "'"));
            List<Designationmaster> desList = desCrit.list();
            Designationmaster desobj = desList.get(0);
            resultMap.put("designation", desobj.getDesignation());
            resultMap.put("salarystructureid", getEmployeePayStructureId(session, epfno, LoggedInRegion));
            resultMap.put("earningdetails", getEmployeePayStructureEarningDetails(session, epfno, LoggedInRegion).toString());;
            resultMap.put("deductiondetails", getEmployeePayStructureDeductionDetails(session, epfno, LoggedInRegion).toString());;
        } else {
            resultMap.put("ERROR", "Given EPF Number is Wrong. ");

        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeePayStructureDetailsEarningDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        resultMap.put("earningdetails", getEmployeePayStructureEarningDetails(session, epfno, LoggedInRegion).toString());;
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeePayStructureDetailsDeductionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        resultMap.put("deductiondetails", getEmployeePayStructureDeductionDetails(session, epfno, LoggedInRegion).toString());;
        return resultMap;
    }

    public String getEmployeePayStructureId(Session session, String epfno, String LoggedInRegion) {
        StringBuffer resultHTML = new StringBuffer();
        try {
            Criteria lrCrit = session.createCriteria(Salarystructureactual.class);
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("periodto is null "));
            lrCrit.addOrder(Order.desc("periodfrom"));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Salarystructureactual salarystructureObj = (Salarystructureactual) ldList.get(0);
                resultHTML.append(salarystructureObj.getId());
            } else {
                Transaction transaction;
                Salarystructureactual salarystructureactualObj = new Salarystructureactual();
                String salStrucId = getMaxSeqNumberSalaryStructureActual(session, LoggedInRegion);
                resultHTML.append(salStrucId);
                salarystructureactualObj.setId(salStrucId);
                salarystructureactualObj.setEmployeemaster(getEmployeeDetails(session, epfno));
                salarystructureactualObj.setOrderno("Order Num");
                salarystructureactualObj.setPeriodfrom(getCurrentDate());
                salarystructureactualObj.setAccregion(LoggedInRegion);
                //salarystructureactualObj.setCreatedby(LoggedInUser);
                salarystructureactualObj.setCreateddate(getCurrentDate());
                transaction = session.beginTransaction();
                session.save(salarystructureactualObj);
                transaction.commit();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultHTML.toString();
    }

    public String getEmployeePayStructureEarningDetails(Session session, String epfno, String LoggedInRegion) {
        StringBuffer resultHTML = new StringBuffer();
        resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
        resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>EARNINGS</td>").append("<td>AMOUNT</td>").append("<td>modify</td>").append("</tr>");
        try {
            Criteria lrCrit = session.createCriteria(Salarystructureactual.class);
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("periodto is null "));
            lrCrit.addOrder(Order.desc("periodfrom"));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Salarystructureactual salarystructureObj = (Salarystructureactual) ldList.get(0);
                Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salarystructureObj.getId() + "' "));
                earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List earDetailsList = earDetailsCrit.list();
                if (earDetailsList.size() > 0) {
                    for (int j = 0; j < earDetailsList.size(); j++) {
                        Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);
                        if ((j % 2) == 0) {
                            resultHTML.append("<tr class=\"rowColor1\" >").append("<td>" + (j + 1) + "</td>").append("<td>" + getPaycodeDetails(session, employeeearningsdetailsactualObj.getEarningmasterid()) + "</td>").append("<td>" + employeeearningsdetailsactualObj.getAmount() + "</td>").append("<td><input name=\"earningmodifyradio\" id=\"earningmodifyradio\" onclick=\"modifyEarningDetails('" + employeeearningsdetailsactualObj.getId() + "')\" type=\"radio\"></td>").append("</tr>");
                        } else {
                            resultHTML.append("<tr class=\"rowColor2\" >").append("<td>" + (j + 1) + "</td>").append("<td>" + getPaycodeDetails(session, employeeearningsdetailsactualObj.getEarningmasterid()) + "</td>").append("<td>" + employeeearningsdetailsactualObj.getAmount() + "</td>").append("<td><input name=\"earningmodifyradio\" id=\"earningmodifyradio\" onclick=\"modifyEarningDetails('" + employeeearningsdetailsactualObj.getId() + "')\" type=\"radio\"></td>").append("</tr>");
                        }
                    }

                }

            } else {
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        resultHTML.append("</table>");
        return resultHTML.toString();
    }

    public String getEmployeePayStructureDeductionDetails(Session session, String epfno, String LoggedInRegion) {
        StringBuffer resultHTML = new StringBuffer();
        resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
        resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>DEDUCTIONS</td>").append("<td>AMOUNT</td>").append("<td>DEDUCTION A/C NO</td>").append("<td>modify</td>").append("</tr>");
        try {
            Criteria lrCrit = session.createCriteria(Salarystructureactual.class);
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("periodto is null "));
            lrCrit.addOrder(Order.desc("periodfrom"));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Salarystructureactual salarystructureObj = (Salarystructureactual) ldList.get(0);
                Criteria earDetailsCrit = session.createCriteria(Employeedeductiondetailsactual.class);
                earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureactualid = '" + salarystructureObj.getId() + "' "));
                earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List earDetailsList = earDetailsCrit.list();
                if (earDetailsList.size() > 0) {
                    for (int j = 0; j < earDetailsList.size(); j++) {
                        Employeedeductiondetailsactual employeedeductiondetailsactualObj = (Employeedeductiondetailsactual) earDetailsList.get(j);
                        if ((j % 2) == 0) {
                            resultHTML.append("<tr class=\"rowColor1\" >").append("<td>" + (j + 1) + "</td>").append("<td>" + getPaycodeDetails(session, employeedeductiondetailsactualObj.getDeductionmasterid()) + "</td>").append("<td>" + employeedeductiondetailsactualObj.getAmount() + "</td>").append("<td>" + employeedeductiondetailsactualObj.getDednNo() + "</td>").append("<td><input name=\"deductionmodifyradio\" id=\"deductionmodifyradio\" onclick=\"modifyDeductionDetails('" + employeedeductiondetailsactualObj.getId() + "')\" type=\"radio\"></td>").append("</tr>");
                        } else {
                            resultHTML.append("<tr class=\"rowColor2\" >").append("<td>" + (j + 1) + "</td>").append("<td>" + getPaycodeDetails(session, employeedeductiondetailsactualObj.getDeductionmasterid()) + "</td>").append("<td>" + employeedeductiondetailsactualObj.getAmount() + "</td>").append("<td>" + employeedeductiondetailsactualObj.getDednNo() + "</td>").append("<td><input name=\"deductionmodifyradio\" id=\"deductionmodifyradio\" onclick=\"modifyDeductionDetails('" + employeedeductiondetailsactualObj.getId() + "')\" type=\"radio\"></td>").append("</tr>");
                        }
                    }

                }

            } else {
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        resultHTML.append("</table>");
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getEmployeeEarningDetailsById(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeearningsdetailsactualid) {
        StringBuffer resultHTML = new StringBuffer();
        try {
            Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
            earDetailsCrit.add(Restrictions.sqlRestriction("id = '" + employeeearningsdetailsactualid + "' "));
            List earDetailsList = earDetailsCrit.list();
            if (earDetailsList.size() > 0) {

                Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(0);
                resultHTML.append("<table table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                resultHTML.append("<tr class=\"lightrow\">");
                resultHTML.append("<td width=\"20%\" class=\"textalign\">Select Earnings Detail</td>");
                resultHTML.append("<td width=\"5%\" class=\"mandatory\">*</td>");
                resultHTML.append("<td width=\"25%\"class=\"textfieldalign\"><select class=\"combobox\" name=\"earningcodemodify\" id=\"earningcodemodify\">" + getPaycodeCombo(session, "E", employeeearningsdetailsactualObj.getEarningmasterid()) + "</select></td>");
                resultHTML.append("</tr>");
                resultHTML.append("<tr class=\"lightrow\">");
                resultHTML.append("<td width=\"20%\" class=\"textalign\">Amount</td>");
                resultHTML.append("<td width=\"5%\" class=\"mandatory\">*</td>");
                resultHTML.append("<td width=\"25%\"class=\"textfieldalign\"><input type=\"text\" value=\"" + employeeearningsdetailsactualObj.getAmount() + "\" id=\"earningamountmodify\" onblur=\"checkFloat(this.id,'Loan Amount');\" size=\"20\"/></td>");
                resultHTML.append("</tr>");
                resultHTML.append("</table>");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getEmployeeDeductionDetailsById(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeedeductiondetailsactualid) {
        StringBuffer resultHTML = new StringBuffer();
        try {
            Criteria dedDetailsCrit = session.createCriteria(Employeedeductiondetailsactual.class);
            dedDetailsCrit.add(Restrictions.sqlRestriction("id = '" + employeedeductiondetailsactualid + "' "));
            List dedDetailsList = dedDetailsCrit.list();
            if (dedDetailsList.size() > 0) {
                Employeedeductiondetailsactual employeedeductiondetailsactualObj = (Employeedeductiondetailsactual) dedDetailsList.get(0);
                resultHTML.append("<table table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                resultHTML.append("<tr class=\"lightrow\">");
                resultHTML.append("<td width=\"20%\" class=\"textalign\">Select Deduction Detail</td>");
                resultHTML.append("<td width=\"5%\" class=\"mandatory\">*</td>");
                resultHTML.append("<td width=\"25%\"class=\"textfieldalign\"><select class=\"combobox\" name=\"deductioncodemodify\" id=\"deductioncodemodify\">" + getPaycodeCombo(session, "D", employeedeductiondetailsactualObj.getDeductionmasterid()) + "</select></td>");
                resultHTML.append("</tr>");
                resultHTML.append("<tr class=\"lightrow\">");
                resultHTML.append("<td width=\"20%\" class=\"textalign\">Amount</td>");
                resultHTML.append("<td width=\"5%\" class=\"mandatory\">*</td>");
                resultHTML.append("<td width=\"25%\"class=\"textfieldalign\"><input type=\"text\" id=\"deductionamountmodify\" value=\"" + employeedeductiondetailsactualObj.getAmount() + "\" onblur=\"checkFloat(this.id,'Loan Amount');\" size=\"20\"/></td>");
                resultHTML.append("</tr>");
                resultHTML.append("<tr class=\"lightrow\">");
                resultHTML.append("<td width=\"20%\" class=\"textalign\">Deduction A/c Code</td>");
                resultHTML.append("<td width=\"5%\" class=\"mandatory\"></td>");
                resultHTML.append("<td width=\"25%\"class=\"textfieldalign\"><input type=\"text\" id=\"deductionaccountmodify\" value=\"" + employeedeductiondetailsactualObj.getDednNo() + "\"  size=\"50\"/></td>");
                resultHTML.append("</tr>");
                resultHTML.append("</table>");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHTML.toString();
    }

    public String getPaycodeCombo(Session session, String paycodetype, String selected) {
        StringBuilder result = new StringBuilder();
        try {
            Criteria paycodeCrit = session.createCriteria(Paycodemaster.class);
            paycodeCrit.add(Restrictions.sqlRestriction("paycodetype = '" + paycodetype.toUpperCase() + "' "));
            paycodeCrit.addOrder(Order.asc("paycodename"));
            List<Paycodemaster> PaycodeNameList = paycodeCrit.list();
            //result.append("<select class=\"combobox\" name=\"earningnameadd\" id=\"earningnameadd\">");
            for (Paycodemaster lbobj : PaycodeNameList) {
                if (lbobj.getPaycode().equalsIgnoreCase(selected)) {
                    result.append("<option selected value=\"" + lbobj.getPaycode() + "\">" + lbobj.getPaycodename() + "</option>");
                } else {
                    result.append("<option value=\"" + lbobj.getPaycode() + "\">" + lbobj.getPaycodename() + "</option>");
                }
            }
            //result.append("</select>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map updateEmployeeearningsdetailsactual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String employeeearningsdetailsactualid, String earningcodemodify, String earningamountmodify) {
        Map resultMap = new HashMap();
        BigDecimal amount;
        Transaction transaction = session.beginTransaction();
        Criteria salStrutEatCrit = session.createCriteria(Employeeearningsdetailsactual.class);
        salStrutEatCrit.add(Restrictions.sqlRestriction("id='" + employeeearningsdetailsactualid + "'"));
        List salStrutEatList = salStrutEatCrit.list();
        if (salStrutEatList.size() > 0) {
            Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) salStrutEatList.get(0);
            employeeearningsdetailsactualObj.setEarningmasterid(earningcodemodify);
            if (earningamountmodify != null && earningamountmodify.trim().length() != 0) {
                amount = new BigDecimal(earningamountmodify);
            } else {
                amount = new BigDecimal(0);
            }
            employeeearningsdetailsactualObj.setAmount(amount);
            employeeearningsdetailsactualObj.setCancelled(Boolean.FALSE);
            transaction = session.beginTransaction();
            session.update(employeeearningsdetailsactualObj);
            transaction.commit();

        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map updateEmployeedeductiondetailsactual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String employeedeductiondetailsactualid, String deductioncodemodify, String deductionamountmodify,String deductionaccountmodify) {
        Map resultMap = new HashMap();
        BigDecimal amount;
        Transaction transaction = session.beginTransaction();
        Criteria salStrutEatCrit = session.createCriteria(Employeedeductiondetailsactual.class);
        salStrutEatCrit.add(Restrictions.sqlRestriction("id='" + employeedeductiondetailsactualid + "'"));
        List salStrutEatList = salStrutEatCrit.list();
        if (salStrutEatList.size() > 0) {
            Employeedeductiondetailsactual employeedeductiondetailsactualObj = (Employeedeductiondetailsactual) salStrutEatList.get(0);
            employeedeductiondetailsactualObj.setDeductionmasterid(deductioncodemodify);
            if (deductionamountmodify != null && deductionamountmodify.trim().length() != 0) {
                amount = new BigDecimal(deductionamountmodify);
            } else {
                amount = new BigDecimal(0);
            }
            employeedeductiondetailsactualObj.setAmount(amount);
            employeedeductiondetailsactualObj.setDednNo(deductionaccountmodify);
            employeedeductiondetailsactualObj.setCancelled(Boolean.FALSE);
            transaction = session.beginTransaction();
            session.update(employeedeductiondetailsactualObj);
            transaction.commit();

        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeearningsdetailsactual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String salarystructureid, String earningnameadd, String earningamountadd) {
        Map resultMap = new HashMap();
        System.out.println("salarystructureid " + salarystructureid);
        BigDecimal amount;
        Transaction transaction = session.beginTransaction();
        Employeeearningsdetailsactual employeeearningsdetailsactualObj = new Employeeearningsdetailsactual();
        String earId = getMaxSeqNumberEarningsActual(session, LoggedInRegion);
        employeeearningsdetailsactualObj.setId(earId);
        employeeearningsdetailsactualObj.setEarningmasterid(earningnameadd);
        if (earningamountadd != null && earningamountadd.trim().length() != 0) {
            amount = new BigDecimal(earningamountadd);
        } else {
            amount = new BigDecimal(0);
        }
        employeeearningsdetailsactualObj.setAmount(amount);
        employeeearningsdetailsactualObj.setCancelled(Boolean.FALSE);
        employeeearningsdetailsactualObj.setSalarystructureactual(getSalarystructureactual(session, salarystructureid));
        employeeearningsdetailsactualObj.setAccregion(LoggedInRegion);
        transaction = session.beginTransaction();
        session.save(employeeearningsdetailsactualObj);
        transaction.commit();


        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeedeductiondetailsactual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String salarystructureid, String deductionnameadd, String deductionamountadd, String deductionaccountadd) {
        Map resultMap = new HashMap();
        BigDecimal amount;
        Transaction transaction = session.beginTransaction();
        Employeedeductiondetailsactual employeedeductiondetailsactualObj = new Employeedeductiondetailsactual();
        String deduId = getMaxSeqNumberDeductionsActual(session, LoggedInRegion);
        employeedeductiondetailsactualObj.setId(deduId);
        employeedeductiondetailsactualObj.setSalarystructureactual(getSalarystructureactual(session, salarystructureid));
        employeedeductiondetailsactualObj.setDeductionmasterid(deductionnameadd);
        employeedeductiondetailsactualObj.setCancelled(Boolean.FALSE);
        if (deductionamountadd != null && deductionamountadd.trim().length() != 0) {
            amount = new BigDecimal(deductionamountadd);
        } else {
            amount = new BigDecimal(0);
        }
        employeedeductiondetailsactualObj.setAmount(amount);
        employeedeductiondetailsactualObj.setDednNo(deductionaccountadd);
        employeedeductiondetailsactualObj.setAccregion(LoggedInRegion);
        transaction = session.beginTransaction();
        session.save(employeedeductiondetailsactualObj);
        transaction.commit();


        return resultMap;
    }

    public Salarystructureactual getSalarystructureactual(Session session, String id) {
        Salarystructureactual salarystructureObj = null;
        Criteria lrCrit = session.createCriteria(Salarystructureactual.class);
        lrCrit.add(Restrictions.sqlRestriction("id = '" + id + "' "));
        List ldList = lrCrit.list();
        if (ldList.size() > 0) {
            salarystructureObj = (Salarystructureactual) ldList.get(0);
        }
        return salarystructureObj;
    }
}
